/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.glaf.matrix.data.factory;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.glaf.core.base.DataFile;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.domain.ServerEntity;
import com.glaf.core.query.ServerEntityQuery;
import com.glaf.core.security.SecurityUtils;
import com.glaf.core.service.IServerEntityService;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.StringTools;
import com.glaf.core.util.UUID32;

import com.glaf.matrix.data.util.DataFileJsonFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

public class RedisFileStorageFactory {
	private static class RedisSingletonHolder {
		public static RedisFileStorageFactory instance = new RedisFileStorageFactory();
	}

	public class RefreshTask implements Runnable {

		public void run() {
			try {
				init();
			} catch (Exception ex) {
				logger.error(ex);
			}
		}

	}

	protected final static Log logger = LogFactory.getLog(RedisFileStorageFactory.class);

	protected static ConcurrentMap<Integer, String> redisPosMap = new ConcurrentHashMap<Integer, String>();

	protected static ConcurrentMap<String, JedisPool> redisPoolMap = new ConcurrentHashMap<String, JedisPool>();

	protected static ConcurrentMap<String, ServerEntity> serverMap = new ConcurrentHashMap<String, ServerEntity>();

	protected static ScheduledExecutorService scheduledThreadPool = Executors.newSingleThreadScheduledExecutor();

	protected static volatile IServerEntityService serverEntityService;

	protected static volatile AtomicBoolean loaded = new AtomicBoolean(false);

	protected static final String DEFAULT_REGION = "default";

	public static RedisFileStorageFactory getInstance() {
		return RedisSingletonHolder.instance;
	}

	public static IServerEntityService getServerEntityService() {
		if (serverEntityService == null) {
			serverEntityService = ContextFactory.getBean("serverEntityService");
		}
		return serverEntityService;
	}

	private RedisFileStorageFactory() {
		startScheduler();
	}

	/**
	 * 通过文件编号删除内容
	 * 
	 * @param fileId
	 */
	public void deleteById(String fileId) {
		this.deleteById(DEFAULT_REGION, fileId);
	}

	/**
	 * 通过文件编号删除内容，删除全部节点
	 * 
	 * @param region
	 * @param fileId
	 */
	public void deleteById(String region, String fileId) {
		int size = redisPoolMap.size();
		if (size == 0) {
			if (!loaded.get()) {
				this.init();
				loaded.set(true);
			}
			size = redisPoolMap.size();
			if (size == 0) {
				logger.warn("redis cache server is empty!!!");
			}
			return;
		}
		int pos = 0;
		while (pos < size) {
			String key = redisPosMap.get(pos++);
			JedisPool pool = redisPoolMap.get(key);
			Jedis jedis = null;
			try {
				ServerEntity serverEntity = serverMap.get(key);
				if (serverEntity != null) {
					jedis = pool.getResource();
					if (jedis != null && jedis.isConnected()) {
						if (StringUtils.isNotEmpty(serverEntity.getDbname())
								&& StringUtils.isNumeric(serverEntity.getDbname())) {
							jedis.select(Integer.parseInt(serverEntity.getDbname()));
						}
						jedis.del(getKey(region, fileId));
						jedis.del(getJsonKey(region, fileId));
						logger.debug(key + "->" + region + ":" + fileId + " delete data from redis.");
					}
				}
			} catch (Exception ex) {
				logger.error("redis error", ex);
			} finally {
				if (jedis != null) {
					try {
						jedis.close();
					} catch (Exception ex) {
					}
				}
			}
		}
	}

	/**
	 * 通过文件编号获取内容
	 * 
	 * @param fileId
	 * @return
	 */
	public byte[] getData(String fileId) {
		return this.getData(DEFAULT_REGION, fileId);
	}

	/**
	 * 通过文件编号获取内容
	 * 
	 * @param region
	 * @param fileId
	 * @return
	 */
	public byte[] getData(String region, String fileId) {
		int size = redisPoolMap.size();
		if (size == 0) {
			if (!loaded.get()) {
				this.init();
				loaded.set(true);
			}
			size = redisPoolMap.size();
			if (size == 0) {
				logger.warn("redis cache server is empty!!!");
			}
			return null;
		}
		int pos = 0;
		String key = null;
		Jedis jedis = null;
		JedisPool pool = null;
		ServerEntity serverEntity = null;
		while (pos < size) {
			key = redisPosMap.get(pos++);
			pool = redisPoolMap.get(key);
			try {
				long start = System.currentTimeMillis();
				serverEntity = serverMap.get(key);
				if (serverEntity != null) {
					jedis = pool.getResource();
					if (jedis != null && jedis.isConnected()) {
						if (StringUtils.isNotEmpty(serverEntity.getDbname())
								&& StringUtils.isNumeric(serverEntity.getDbname())) {
							jedis.select(Integer.parseInt(serverEntity.getDbname()));
						}
						byte[] data = jedis.get(getKey(region, fileId));
						long ts = System.currentTimeMillis() - start;
						logger.debug("redis获取用时:" + ts);
						if (data != null) {
							logger.debug(key + "->" + region + ":" + fileId + " get data from redis.");
							return data;
						}
					}
				}
			} catch (Exception ex) {
				logger.error("redis get error", ex);
			} finally {
				if (jedis != null) {
					try {
						jedis.close();
					} catch (Exception ex) {
					}
				}
			}
		}
		return null;
	}

	/**
	 * 通过文件编号获取内容
	 * 
	 * @param region
	 * @param fileId
	 * @return
	 */
	public DataFile getDataFile(String region, String fileId) {
		int size = redisPoolMap.size();
		if (size == 0) {
			if (!loaded.get()) {
				this.init();
				loaded.set(true);
			}
			size = redisPoolMap.size();
			if (size == 0) {
				logger.warn("redis cache server is empty!!!");
			}
			return null;
		}
		int pos = 0;
		String key = null;
		Jedis jedis = null;
		JedisPool pool = null;
		ServerEntity serverEntity = null;
		while (pos < size) {
			key = redisPosMap.get(pos++);
			pool = redisPoolMap.get(key);
			try {
				long start = System.currentTimeMillis();
				serverEntity = serverMap.get(key);
				if (serverEntity != null) {
					jedis = pool.getResource();
					if (jedis != null && jedis.isConnected()) {
						if (StringUtils.isNotEmpty(serverEntity.getDbname())
								&& StringUtils.isNumeric(serverEntity.getDbname())) {
							jedis.select(Integer.parseInt(serverEntity.getDbname()));
						}
						DataFile dataFile = null;
						String text = jedis.get(getJsonKey(region, fileId));
						if (StringUtils.isNotEmpty(text)) {
							JSONObject json = JSON.parseObject(text);
							dataFile = DataFileJsonFactory.jsonToObject(json);
							if (dataFile != null) {
								byte[] data = jedis.get(getKey(region, fileId));
								dataFile.setData(data);
							}
						}
						long ts = System.currentTimeMillis() - start;
						logger.debug("redis获取用时:" + ts);
						return dataFile;
					}
				}
			} catch (Exception ex) {
				logger.error("redis get error", ex);
			} finally {
				if (jedis != null) {
					try {
						jedis.close();
					} catch (Exception ex) {
					}
				}
			}
		}
		return null;
	}

	protected String getJsonKey(String region, String fileId) {
		return (region + ":json:" + fileId);
	}

	protected byte[] getKey(String region, String fileId) {
		return (region + ":S:" + fileId).getBytes();
	}

	/**
	 * 获取某个redis服务器上的键
	 * 
	 * @param region
	 * @param server_name
	 * @return
	 */
	public Collection<String> getKeys(String region, String server_name) {
		Collection<String> keys = new HashSet<String>();
		ServerEntity serverEntity = serverMap.get(server_name);
		if (serverEntity != null) {
			Jedis jedis = null;
			try {
				JedisPool pool = redisPoolMap.get(server_name);
				jedis = pool.getResource();
				if (jedis != null && jedis.isConnected()) {
					if (StringUtils.isNotEmpty(serverEntity.getDbname())
							&& StringUtils.isNumeric(serverEntity.getDbname())) {
						jedis.select(Integer.parseInt(serverEntity.getDbname()));
					}
					Set<String> hkeys = jedis.keys(region + ":S:*");
					if (hkeys != null && !hkeys.isEmpty()) {
						for (String str : hkeys) {
							str = StringTools.replace(str, region + ":S:", "");
							keys.add(str);
						}
					}
					logger.debug("->keys:" + keys);
				}
			} catch (Exception ex) {
				// ex.printStackTrace();
				logger.error("redis error", ex);
			} finally {
				if (jedis != null) {
					try {
						jedis.close();
					} catch (Exception ex) {
					}
				}
			}
		}
		return keys;
	}

	public void init() {
		ServerEntityQuery query = new ServerEntityQuery();
		query.type("redis");
		query.active("1");

		List<ServerEntity> servers = getServerEntityService().list(query);
		if (servers != null && !servers.isEmpty()) {
			int index = 0;
			for (ServerEntity serverEntity : servers) {
				if (!StringUtils.startsWith(serverEntity.getMapping(), "file_")) {
					continue;
				}
				int timeout = Protocol.DEFAULT_TIMEOUT;
				String host = serverEntity.getHost();
				int port = serverEntity.getPort();
				String password = serverEntity.getPassword();
				String key = serverEntity.getKey();
				JedisPool pool = null;
				Jedis jedis = null;
				try {
					pool = redisPoolMap.get(serverEntity.getName());
					if (pool != null && !pool.isClosed()) {
						jedis = pool.getResource();
						if (StringUtils.isNotEmpty(serverEntity.getDbname())) {
							jedis.select(Integer.parseInt(serverEntity.getDbname()));
						}
						String value = DateUtils.getNowYearMonthDayHHmmss() + "_" + UUID32.getUUID();
						logger.info("check redis, set redis value:" + value);
						jedis.set("test".getBytes(), value.getBytes());
						jedis.expire("test".getBytes(), 60);
						boolean skip = false;
						if (StringUtils.equals(jedis.get("test"), value)) {
							skip = true;
						}
						if (skip) {
							continue;
						} else {
							pool.close();
							pool.destroy();
							pool = null;
							redisPoolMap.remove(serverEntity.getName());
						}
					}
				} catch (Exception ex) {
					logger.error("redis connection error", ex);
				} finally {
					if (jedis != null) {
						try {
							jedis.close();
						} catch (Exception ex) {
						}
					}
				}
				try {
					JedisPoolConfig config = new JedisPoolConfig();
					config.setEvictionPolicyClassName("org.apache.commons.pool2.impl.DefaultEvictionPolicy");
					config.setMaxTotal(-1);// 最大连接数,-1代表不限制
					config.setMaxIdle(2000);
					config.setMaxWaitMillis(5000L);// 获取连接时的最大等待毫秒数
					config.setNumTestsPerEvictionRun(10);// 每次逐出检查时 逐出的最大数目
					config.setMinEvictableIdleTimeMillis(864000000L);// 逐出连接的最小空闲时间, 默认1天(24小时)
					config.setSoftMinEvictableIdleTimeMillis(10);// 对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数
					config.setTimeBetweenEvictionRunsMillis(300000);
					config.setTestOnBorrow(true);// 获得一个jedis实例的时候是否检查连接可用性
					config.setBlockWhenExhausted(true);// 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
					config.setLifo(false);
					// logger.debug("p:" + password);
					password = SecurityUtils.decode(key, password);
					if (StringUtils.isNotEmpty(password) && !StringUtils.equals(password, "88888888")) {
						pool = new JedisPool(config, host, port, timeout, password);
					} else {
						pool = new JedisPool(config, host, port, timeout);
					}

					jedis = pool.getResource();
					if (StringUtils.isNotEmpty(serverEntity.getDbname())) {
						jedis.select(Integer.parseInt(serverEntity.getDbname()));
					}
					String value = DateUtils.getNowYearMonthDayHHmmss() + "_" + UUID32.getUUID();
					logger.info("set redis value:" + value);
					jedis.set("test".getBytes(), value.getBytes());
					jedis.expire("test".getBytes(), 60);
					if (StringUtils.equals(jedis.get("test"), value)) {
						redisPoolMap.put(serverEntity.getName(), pool);
						serverMap.put(serverEntity.getName(), serverEntity);
						redisPosMap.put(index++, serverEntity.getName());
						logger.info("redis pool has cache.");
					}
				} catch (Exception ex) {
					logger.error("redis connection error", ex);
				} finally {
					if (jedis != null) {
						try {
							jedis.close();
						} catch (Exception ex) {
						}
					}
				}
			}
		}
	}

	/**
	 * 存储内容
	 * 
	 * @param fileId
	 * @param data
	 */
	public void saveData(String fileId, byte[] data) {
		this.saveData(DEFAULT_REGION, fileId, data);
	}

	/**
	 * 存储内容到redis，写到全部节点
	 * 
	 * @param region
	 * @param fileId
	 * @param data
	 */
	public void saveData(String region, String fileId, byte[] data) {
		int size = redisPoolMap.size();
		if (size == 0) {
			if (!loaded.get()) {
				this.init();
				loaded.set(true);
			}
			size = redisPoolMap.size();
			if (size == 0) {
				logger.warn("redis cache server is empty!!!");
			}
			return;
		}
		int pos = 0;
		int rnum = 0;
		String key = null;
		Jedis jedis = null;
		JedisPool pool = null;
		ServerEntity serverEntity = null;
		java.util.Random rand = new java.util.Random();
		while (pos < size) {
			key = redisPosMap.get(pos++);
			pool = redisPoolMap.get(key);
			try {
				long start = System.currentTimeMillis();
				serverEntity = serverMap.get(key);
				if (serverEntity != null && data != null && pool != null && !pool.isClosed()) {
					jedis = pool.getResource();
					if (StringUtils.isNotEmpty(serverEntity.getDbname())
							&& StringUtils.isNumeric(serverEntity.getDbname())) {
						jedis.select(Integer.parseInt(serverEntity.getDbname()));
					}
					rnum = rand.nextInt(2);
					jedis.set(getKey(region, fileId), data);
					jedis.expire(getKey(region, fileId), 86400 - rnum);// 24小时以内
					logger.debug(key + "->" + region + ":" + fileId + " set into redis.");
					long ts = System.currentTimeMillis() - start;
					logger.debug("redis存储用时:" + ts);
				}
			} catch (Exception ex) {
				logger.error("redis set error", ex);
			} finally {
				if (jedis != null) {
					try {
						jedis.close();
					} catch (Exception ex) {
					}
				}
			}
		}
	}

	/**
	 * 存储内容到redis，写到全部节点
	 * 
	 * @param region
	 * @param fileId
	 * @param data
	 */
	public void saveDataFile(String region, String fileId, DataFile dataFile) {
		int size = redisPoolMap.size();
		if (size == 0) {
			if (!loaded.get()) {
				this.init();
				loaded.set(true);
			}
			size = redisPoolMap.size();
			if (size == 0) {
				logger.warn("redis cache server is empty!!!");
			}
			return;
		}
		int pos = 0;
		int rnum = 0;
		String key = null;
		Jedis jedis = null;
		JedisPool pool = null;
		ServerEntity serverEntity = null;
		java.util.Random rand = new java.util.Random();
		while (pos < size) {
			key = redisPosMap.get(pos++);
			pool = redisPoolMap.get(key);
			try {
				serverEntity = serverMap.get(key);
				if (serverEntity != null && dataFile != null && pool != null && !pool.isClosed()) {
					jedis = pool.getResource();
					if (StringUtils.isNotEmpty(serverEntity.getDbname())
							&& StringUtils.isNumeric(serverEntity.getDbname())) {
						jedis.select(Integer.parseInt(serverEntity.getDbname()));
					}
					rnum = rand.nextInt(9);
					jedis.set(getJsonKey(region, fileId), dataFile.toJsonObject().toJSONString());
					jedis.set(getKey(region, fileId), dataFile.getData());
					jedis.expire(getJsonKey(region, fileId), 86400 - rnum);// 24小时以内
					jedis.expire(getKey(region, fileId), 86400 - rnum);// 24小时以内
				}
			} catch (Exception ex) {
				logger.error("redis set error", ex);
			} finally {
				if (jedis != null) {
					try {
						jedis.close();
					} catch (Exception ex) {
					}
				}
			}
		}
	}

	public void startScheduler() {
		RefreshTask command = new RefreshTask();
		scheduledThreadPool.scheduleAtFixedRate(command, 60, 30, TimeUnit.SECONDS);// 每30秒检查一次
	}

}