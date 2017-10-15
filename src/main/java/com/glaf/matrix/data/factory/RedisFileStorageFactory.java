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

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

	protected static ConcurrentMap<String, String> redisFileMap = new ConcurrentHashMap<String, String>();

	protected static ConcurrentMap<Integer, String> redisPosMap = new ConcurrentHashMap<Integer, String>();

	protected static ConcurrentMap<String, JedisPool> redisPoolMap = new ConcurrentHashMap<String, JedisPool>();

	protected static ConcurrentMap<String, ServerEntity> serverMap = new ConcurrentHashMap<String, ServerEntity>();

	protected static ScheduledExecutorService scheduledThreadPool = Executors.newSingleThreadScheduledExecutor();

	protected static volatile IServerEntityService serverEntityService;

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

	public byte[] getData(String fileId) {
		Jedis jedis = null;
		try {
			String key = redisFileMap.get(fileId);
			if (key != null) {
				ServerEntity serverEntity = serverMap.get(key);
				if (serverEntity != null) {
					JedisPool pool = redisPoolMap.get(key);
					jedis = pool.getResource();
					if (jedis != null && jedis.isConnected()) {
						if (StringUtils.isNotEmpty(serverEntity.getDbname())
								&& StringUtils.isNumeric(serverEntity.getDbname())) {
							jedis.select(Integer.parseInt(serverEntity.getDbname()));
						}
						return jedis.get(fileId.getBytes());
					}
				}
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
			logger.error("redis error", ex);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return null;
	}

	public DataFile getDataFile(String fileId) {
		Jedis jedis = null;
		try {
			String key = redisFileMap.get(fileId);
			if (key != null) {
				ServerEntity serverEntity = serverMap.get(key);
				if (serverEntity != null) {
					JedisPool pool = redisPoolMap.get(key);
					jedis = pool.getResource();
					if (jedis != null && jedis.isConnected()) {
						if (StringUtils.isNotEmpty(serverEntity.getDbname())
								&& StringUtils.isNumeric(serverEntity.getDbname())) {
							jedis.select(Integer.parseInt(serverEntity.getDbname()));
						}
						DataFile dataFile = null;
						String text = jedis.get(fileId + "_json");
						if (StringUtils.isNotEmpty(text)) {
							JSONObject json = JSON.parseObject(text);
							dataFile = DataFileJsonFactory.jsonToObject(json);
							if (dataFile != null) {
								byte[] data = jedis.get(fileId.getBytes());
								dataFile.setData(data);
							}
						}
						return dataFile;
					}
				}
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
			logger.error("redis error", ex);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return null;
	}

	public void init() {
		ServerEntityQuery query = new ServerEntityQuery();
		query.type("redis");
		query.active("1");
		query.verify("Y");

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
						continue;
					}
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
					logger.debug("set redis value:" + value);
					jedis.set("test".getBytes(), value.getBytes());
					if (StringUtils.equals(jedis.get("test"), value)) {
						redisPoolMap.put(serverEntity.getName(), pool);
						serverMap.put(serverEntity.getName(), serverEntity);
						redisPosMap.put(index++, serverEntity.getName());
					}
				} catch (Exception ex) {
					logger.error("redis connection error", ex);
				} finally {
					if (jedis != null) {
						jedis.close();
					}
				}
			}
		}
	}

	public void saveData(String fileId, byte[] data) {
		java.util.Random rand = new java.util.Random();
		int size = redisPoolMap.size();
		int pos = 0;
		int retry = 0;
		while (retry <= size) {
			pos = rand.nextInt(size - 1);
			if (pos < 0) {
				pos = 0;
			}
			String key = redisPosMap.get(pos);
			JedisPool pool = redisPoolMap.get(key);
			Jedis jedis = null;
			try {
				retry++;
				ServerEntity serverEntity = serverMap.get(key);
				if (serverEntity != null && data != null && pool != null && !pool.isClosed()) {
					jedis = pool.getResource();
					if (StringUtils.isNotEmpty(serverEntity.getDbname())
							&& StringUtils.isNumeric(serverEntity.getDbname())) {
						jedis.select(Integer.parseInt(serverEntity.getDbname()));
					}
					jedis.set(fileId.getBytes(), data);
					redisFileMap.put(fileId, key);
					return;
				}
			} catch (Exception ex) {
				logger.error("redis error", ex);
			} finally {
				if (jedis != null) {
					jedis.close();
				}
			}
		}
	}

	public void saveDataFile(String fileId, DataFile dataFile) {
		java.util.Random rand = new java.util.Random();
		int size = redisPoolMap.size();
		int pos = 0;
		int retry = 0;
		while (retry <= size) {
			pos = rand.nextInt(size - 1);
			if (pos < 0) {
				pos = 0;
			}
			String key = redisPosMap.get(pos);
			JedisPool pool = redisPoolMap.get(key);
			Jedis jedis = null;
			try {
				retry++;
				ServerEntity serverEntity = serverMap.get(key);
				if (serverEntity != null && dataFile != null && pool != null && !pool.isClosed()) {
					jedis = pool.getResource();
					if (StringUtils.isNotEmpty(serverEntity.getDbname())
							&& StringUtils.isNumeric(serverEntity.getDbname())) {
						jedis.select(Integer.parseInt(serverEntity.getDbname()));
					}
					jedis.set(fileId + "_json", dataFile.toJsonObject().toJSONString());
					jedis.set(fileId.getBytes(), dataFile.getData());
					redisFileMap.put(fileId, key);
					return;
				}
			} catch (Exception ex) {
				logger.error("redis error", ex);
			} finally {
				if (jedis != null) {
					jedis.close();
				}
			}
		}
	}

	public void startScheduler() {
		RefreshTask command = new RefreshTask();
		scheduledThreadPool.scheduleAtFixedRate(command, 1, 5, TimeUnit.MINUTES);// 每5分钟检查一次
	}

}