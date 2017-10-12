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
package com.glaf.core.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glaf.core.config.GlobalConfig;
import com.glaf.core.util.PropertiesUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class RedisFactory {
	private static class RedisHolder {
		public static RedisFactory instance = new RedisFactory();
	}

	private static Logger logger = LoggerFactory.getLogger(RedisFactory.class);

	protected static ExecutorService pool = Executors.newFixedThreadPool(10);

	protected static volatile AtomicInteger counter = new AtomicInteger(0);

	protected static AtomicBoolean loading = new AtomicBoolean(false);

	protected static AtomicBoolean configSuccess = new AtomicBoolean(false);

	protected static AtomicInteger totalRetry = new AtomicInteger(0);

	private static volatile ShardedJedisPool shardedJedisPool;

	/**
	 * 构建锁
	 */
	private static final Lock LOCK = new ReentrantLock();

	private static final int MAX_RETRIES = 10;

	private static final int numRetries = 3;

	public static RedisFactory getInstance() {
		return RedisHolder.instance;
	}

	private RedisFactory() {

	}

	/**
	 * 添加到List中（同时设置过期时间）
	 * 
	 * @param key
	 *            key值
	 * @param seconds
	 *            过期时间 单位s
	 * @param value
	 * @return
	 */
	public boolean addList(String key, int seconds, String... value) {
		boolean result = addList(key, value);
		if (result) {
			long i = expire(key, seconds);
			return i == 1;
		}
		return false;
	}

	/**
	 * 添加到List(只新增)
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean addList(String key, List<String> list) {
		if (key == null || list == null || list.size() == 0) {
			return false;
		}
		for (String value : list) {
			addList(key, value);
		}
		return true;
	}

	/**
	 * 添加到List
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean addList(String key, String... value) {
		if (key == null || value == null) {
			return false;
		}
		ShardedJedis shardedJedis = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				shardedJedis.lpush(key, value);
				return true;
			}
		} catch (Exception ex) {
			logger.error("setList error.", ex);
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return false;
	}

	/**
	 * 添加到Set中（同时设置过期时间）
	 * 
	 * @param key
	 *            key值
	 * @param seconds
	 *            过期时间 单位s
	 * @param value
	 * @return
	 */
	public boolean addSet(String key, int seconds, String... value) {
		boolean result = addSet(key, value);
		if (result) {
			long i = expire(key, seconds);
			return i == 1;
		}
		return false;
	}

	/**
	 * 添加到Set中
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean addSet(String key, String... value) {
		if (key == null || value == null) {
			return false;
		}
		ShardedJedis shardedJedis = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				shardedJedis.sadd(key, value);
				return true;
			}
		} catch (Exception ex) {
			logger.error("setList error.", ex);
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return false;
	}

	/**
	 * 返回 region 指定的哈希集中所有字段的key值
	 * 
	 * @param region
	 * @return
	 */
	public Set<byte[]> byteHKeys(String region) {
		ShardedJedis shardedJedis = null;
		Set<byte[]> retSet = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				retSet = shardedJedis.hkeys(region.getBytes());
			}
		} catch (Exception ex) {
			logger.error("hkeys error.", ex);
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return retSet;
	}

	/**
	 * @param key
	 * @param value
	 * @return 判断值是否包含在set中
	 */
	public boolean containsInSet(String key, String value) {
		if (key == null || value == null) {
			return false;
		}
		ShardedJedis shardedJedis = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				return shardedJedis.sismember(key, value);
			}
		} catch (Exception ex) {
			logger.error("setList error.", ex);
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return false;
	}

	/**
	 * 检查List长度
	 * 
	 * @param key
	 * @return
	 */
	public long countList(String key) {
		if (key == null) {
			return 0;
		}
		ShardedJedis shardedJedis = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				return shardedJedis.llen(key);
			}
		} catch (Exception ex) {
			logger.error("countList error.", ex);
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return 0;
	}

	/**
	 * 检查Set长度
	 * 
	 * @param key
	 * @return
	 */
	public long countSet(String key) {
		if (key == null) {
			return 0;
		}
		ShardedJedis shardedJedis = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				return shardedJedis.scard(key);
			}
		} catch (Exception ex) {
			logger.error("countSet error.", ex);
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return 0;
	}

	/**
	 * 计算排序长度
	 * 
	 * @param key
	 * @param startScore
	 * @param endScore
	 * @return
	 */
	public long countSoredSet(String key, long startScore, long endScore) {
		ShardedJedis shardedJedis = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				Long count = shardedJedis.zcount(key, startScore, endScore);
				return count == null ? 0L : count;
			}
		} catch (Exception ex) {
			logger.error("countSoredSet error.", ex);
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return 0L;
	}

	public long decr(String key) {
		ShardedJedis shardedJedis = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				return shardedJedis.decr(key);
			}
		} catch (Exception ex) {
			logger.error("incr error.", ex);
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return 0;
	}

	public boolean del(byte[] key) {
		ShardedJedis shardedJedis = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				shardedJedis.del(key);
				return true;
			}
		} catch (Exception ex) {
			logger.error("del error.", ex);
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return false;
	}

	public boolean del(String key) {
		ShardedJedis shardedJedis = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				shardedJedis.del(key);
				return true;
			}
		} catch (Exception ex) {
			logger.error("redis del error.", ex.getMessage());
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return false;
	}

	/**
	 * 删除集合
	 * 
	 * @return
	 */
	public boolean del(String[] okeys) {
		ShardedJedis shardedJedis = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				if (okeys != null && okeys.length > 0) {
					for (String key : okeys) {
						shardedJedis.del(key);
					}
					return true;
				}
			}
		} catch (Exception ex) {
			logger.error("redis del  error.", ex.getMessage());
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return false;
	}

	public void deleteKey(String key) {
		int tries = 0;
		boolean sucess = false;
		do {
			tries++;
			try {
				if (key != null) {
					Jedis jedis = this.getShardedJedis().getShard("master");
					jedis.del(key);
					jedis.close();
				}
				sucess = true;
			} catch (JedisConnectionException e) {
				logger.error("Jedis connection failed, retrying..." + tries);
				if (tries == numRetries) {
					throw e;
				}
			}
		} while (!sucess && tries <= numRetries);
	}

	/**
	 * 删除HashSet对象
	 * 
	 * @param region
	 *            域名
	 * @param key
	 *            键值
	 * @return 删除的记录数
	 */
	public long delHSet(String region, String key) {
		ShardedJedis shardedJedis = null;
		long count = 0;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				count = shardedJedis.hdel(region, key);
			}
		} catch (Exception ex) {
			logger.error("delHSet error.", ex);
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return count;
	}

	/**
	 * 删除HashSet对象
	 * 
	 * @param region
	 *            域名
	 * @param key
	 *            键值
	 * @return 删除的记录数
	 */
	public long delHSet(String region, String... key) {
		ShardedJedis shardedJedis = null;
		long count = 0;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				count = shardedJedis.hdel(region, key);
			}
		} catch (Exception ex) {
			logger.error("delHSet error.", ex);
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return count;
	}

	/**
	 * 删除排序集合
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean delSortedSet(String key, String value) {
		ShardedJedis shardedJedis = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				long count = shardedJedis.zrem(key, value);
				return count > 0;
			}
		} catch (Exception ex) {
			logger.error("delSortedSet error.", ex);
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return false;
	}

	/**
	 * 判断key是否存在
	 * 
	 * @param region
	 *            域名
	 * @param key
	 *            键值
	 * @return
	 */
	public boolean existsHSet(String region, String key) {
		ShardedJedis shardedJedis = null;
		boolean isExist = false;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				isExist = shardedJedis.hexists(region, key);
			}
		} catch (Exception ex) {
			logger.error("existsHSet error.", ex);
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return isExist;
	}

	/**
	 * 设置一个key的过期时间（单位：秒）
	 * 
	 * @param key
	 *            key值
	 * @param seconds
	 *            多少秒后过期
	 * @return 1：设置了过期时间 0：没有设置过期时间/不能设置过期时间
	 */
	public long expire(byte[] key, int seconds) {
		if (key == null) {
			return 0;
		}
		ShardedJedis shardedJedis = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				return shardedJedis.expire(key, seconds);
			}
		} catch (Exception ex) {
			logger.error("EXPIRE error[key=" + new String(key) + " seconds=" + seconds + "]" + ex.getMessage(), ex);
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return 0;
	}

	/**
	 * 设置一个key的过期时间（单位：秒）
	 * 
	 * @param key
	 *            key值
	 * @param seconds
	 *            多少秒后过期
	 * @return 1：设置了过期时间 0：没有设置过期时间/不能设置过期时间
	 */
	public long expire(String key, int seconds) {
		if (key == null || key.equals("")) {
			return 0;
		}
		ShardedJedis shardedJedis = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				return shardedJedis.expire(key, seconds);
			}
		} catch (Exception ex) {
			logger.error("EXPIRE error[key=" + key + " seconds=" + seconds + "]" + ex.getMessage(), ex);
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return 0;
	}

	/**
	 * 设置一个key在某个时间点过期
	 * 
	 * @param key
	 *            key值
	 * @param unixTimestamp
	 *            unix时间戳，从1970-01-01 00:00:00开始到现在的秒数
	 * @return 1：设置了过期时间 0：没有设置过期时间/不能设置过期时间
	 */
	public long expireAt(String key, int unixTimestamp) {
		if (key == null || key.equals("")) {
			return 0;
		}
		ShardedJedis shardedJedis = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				return shardedJedis.expireAt(key, unixTimestamp);
			}
		} catch (Exception ex) {
			logger.error("EXPIRE error[key=" + key + " unixTimestamp=" + unixTimestamp + "]" + ex.getMessage(), ex);
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return 0;
	}

	public byte[] getByteArray(String key) {
		int tries = 0;
		boolean sucess = false;
		byte[] array = new byte[1];
		do {
			tries++;
			try {
				if (key != null) {
					Jedis jedis = this.getShardedJedis().getShard("master");
					array = jedis.get(key.getBytes());
					jedis.close();
				}
				sucess = true;
			} catch (JedisConnectionException e) {
				logger.error("Jedis connection failed, retrying..." + tries);
				if (tries == numRetries) {
					throw e;
				}
			}
		} while (!sucess && tries <= numRetries);
		return array;
	}

	public byte[] getBytes(final byte[] key) {
		ShardedJedis shardedJedis = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				byte[] value = shardedJedis.get(key);
				return value;
			}
		} catch (Exception ex) {
			// logger.error("redis get error.", ex.getMessage());
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return null;
	}

	public byte[] getBytes(final byte[] region, final byte[] key) {
		ShardedJedis shardedJedis = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				byte[] value = shardedJedis.hget(region, key);
				return value;
			}
		} catch (Exception ex) {
			// logger.error("redis get error.", ex.getMessage());
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return null;
	}

	public byte[] getBytesAsync(final byte[] key) {
		boolean waitFor = true;
		Callable<byte[]> task = new Callable<byte[]>() {
			@Override
			public byte[] call() throws Exception {
				ShardedJedis shardedJedis = null;
				try {
					if (configSuccess.get()) {
						shardedJedis = getShardedJedis();
						byte[] value = shardedJedis.get(key);
						return value;
					}
				} catch (Exception ex) {
					logger.error("redis get error.", ex.getMessage());
					returnBrokenResource(shardedJedis);
				} finally {
					returnResource(shardedJedis);
				}
				return null;
			}
		};

		try {
			Future<byte[]> result = pool.submit(task);
			long start = System.currentTimeMillis();
			// 如果需要等待执行结果
			if (waitFor) {
				while (true) {
					if (result.isDone()) {
						return result.get();
					}
					if (System.currentTimeMillis() - start > 200) {
						break;
					}
				}
			}
		} catch (Exception ex) {
			logger.error("", ex);
		}

		return null;
	}

	/**
	 * 获得HashSet对象
	 * 
	 * @param region
	 *            域名
	 * @param key
	 *            键值
	 * @return Json String or String value
	 */
	public String getHSet(String region, String key) {
		ShardedJedis shardedJedis = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				return shardedJedis.hget(region, key);
			}
		} catch (Exception ex) {
			logger.error("getHSet error.", ex);
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return null;
	}

	/**
	 * 获取List
	 * 
	 * @param key
	 * @return
	 */
	public List<String> getList(String key) {
		ShardedJedis shardedJedis = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				return shardedJedis.lrange(key, 0, -1);
			}
		} catch (Exception ex) {
			logger.error("getList error.", ex);
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return null;
	}

	public Jedis getResource() {
		Jedis jedis = null;
		int retries = 0;
		while (true) {
			try {
				if (totalRetry.get() < 100) {
					jedis = getShardedJedisPool().getResource().getShard("master");
					logger.debug("call redis count:" + counter.incrementAndGet());
					return jedis;
				}
				throw new RuntimeException("Can't get redis, Please check config.");
			} catch (java.lang.Throwable ex) {
				if (retries++ == MAX_RETRIES) {
					totalRetry.incrementAndGet();
					if (shardedJedisPool != null && shardedJedisPool.getNumActive() <= 0) {
						shardedJedisPool.close();
						shardedJedisPool.destroy();
						shardedJedisPool = null;
						logger.warn("shardedJedisPool is closed.");
					}
					logger.error("getShardedJedis error.", ex.getMessage());
					throw new RuntimeException(ex);
				}
				try {
					TimeUnit.MILLISECONDS.sleep(20 + new Random().nextInt(100));// 活锁
				} catch (InterruptedException e) {
				}
			}
		}
	}

	/**
	 * 获得排序打分
	 * 
	 * @param key
	 * @return
	 */
	public Double getScore(String key, String member) {
		ShardedJedis shardedJedis = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				return shardedJedis.zscore(key, member);
			}
		} catch (Exception ex) {
			logger.error("getSoredSet error.", ex.getMessage());
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return null;
	}

	/**
	 * 获取Set
	 * 
	 * @param key
	 * @return
	 */
	public Set<String> getSet(String key) {
		ShardedJedis shardedJedis = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				return shardedJedis.smembers(key);
			}
		} catch (Exception ex) {
			logger.error("getList error.", ex);
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return null;
	}

	public ShardedJedis getShardedJedis() {
		ShardedJedis shardedJedis = null;
		int retries = 0;
		while (true) {
			try {
				if (totalRetry.get() < 1000) {
					if (configSuccess.get()) {
						shardedJedis = getShardedJedisPool().getResource();
						logger.debug("->call redis count:" + counter.incrementAndGet());
						return shardedJedis;
					}
				}
				return null;
			} catch (java.lang.Throwable ex) {
				if (retries++ == MAX_RETRIES) {
					totalRetry.incrementAndGet();
					if (shardedJedisPool != null) {
						shardedJedisPool.close();
						shardedJedisPool.destroy();
						shardedJedisPool = null;
						logger.warn("shardedJedisPool is closed.");
					}
					logger.error("getShardedJedis error", ex.getMessage());
					throw new RuntimeException(ex);
				}
				try {
					TimeUnit.MILLISECONDS.sleep(20 + new Random().nextInt(100));// 活锁
				} catch (InterruptedException e) {
				}
			}
		}
	}

	protected ShardedJedisPool getShardedJedisPool() {
		int retries = 0;
		while (true) {
			try {
				boolean success = LOCK.tryLock(20, TimeUnit.MILLISECONDS);
				if (success && shardedJedisPool == null) {
					Properties props = GlobalConfig.getConfigProperties("redis.properties");
					logger.debug("redis properties:" + props);
					if (props != null && !props.isEmpty()) {
						totalRetry.set(0);
						JedisPoolConfig config = new JedisPoolConfig();
						config.setEvictionPolicyClassName("org.apache.commons.pool2.impl.DefaultEvictionPolicy");
						config.setMinIdle(PropertiesUtils.getInt(props, "minIdle", 2));
						config.setMaxIdle(PropertiesUtils.getInt(props, "maxIdle", 10));
						// 最大连接数
						config.setMaxTotal(PropertiesUtils.getInt(props, "maxTotal", 10000));
						// 每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认10
						config.setNumTestsPerEvictionRun(PropertiesUtils.getInt(props, "numTestsPerEvictionRun", 10));
						// 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常,
						// 小于零:阻塞不确定的时间,
						// 默认-1
						config.setMaxWaitMillis(PropertiesUtils.getInt(props, "maxWaitMillis", 2000));
						config.setTimeBetweenEvictionRunsMillis(
								PropertiesUtils.getInt(props, "timeBetweenEvictionRunsMillis", 30000));
						// 逐出连接的最小空闲时间, 默认1800000毫秒(30分钟)
						config.setMinEvictableIdleTimeMillis(
								PropertiesUtils.getInt(props, "minEvictableIdleTimeMillis", 1800000));

						// 对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数
						// 时直接逐出,不再根据MinEvictableIdleTimeMillis判断 (默认逐出策略)
						config.setSoftMinEvictableIdleTimeMillis(
								PropertiesUtils.getInt(props, "softMinEvictableIdleTimeMillis", 10));
						// 获得一个jedis实例的时候是否检查连接可用性
						config.setTestOnBorrow(PropertiesUtils.getBoolean(props, "testOnBorrow", true));
						// 在空闲时检查有效性, 默认false
						config.setTestWhileIdle(PropertiesUtils.getBoolean(props, "testWhileIdle", false));
						// 在获取连接的时候检查有效性, 默认false
						config.setTestOnReturn(PropertiesUtils.getBoolean(props, "testOnReturn", false));
						// 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
						config.setBlockWhenExhausted(PropertiesUtils.getBoolean(props, "blockWhenExhausted", true));
						config.setLifo(PropertiesUtils.getBoolean(props, "lifo", false));

						String password = props.getProperty("redis.password");

						List<JedisShardInfo> servers = new ArrayList<JedisShardInfo>();
						if (props.getProperty("redis.master.host") != null
								&& props.getProperty("redis.master.port") != null) {
							JedisShardInfo master = new JedisShardInfo(props.getProperty("redis.master.host").trim(),
									Integer.parseInt(props.getProperty("redis.master.port").trim()),
									(StringUtils.isEmpty(password) ? null : password));
							servers.add(master);
						}

						if (props.getProperty("redis.slave.host") != null
								&& props.getProperty("redis.slave.port") != null) {
							JedisShardInfo slave = new JedisShardInfo(props.getProperty("redis.slave.host").trim(),
									Integer.parseInt(props.getProperty("redis.slave.port").trim()),
									(StringUtils.isEmpty(password) ? null : password));
							servers.add(slave);
						}

						if (StringUtils.isNotEmpty(props.getProperty("redis.slaves"))) {
							StringTokenizer token = new StringTokenizer(props.getProperty("redis.slaves"), ",");
							while (token.hasMoreTokens()) {
								String item = token.nextToken();
								if (StringUtils.contains(item, ":")) {
									String h = item.substring(0, item.indexOf(":"));
									int p = Integer.parseInt(item.substring(item.indexOf(":") + 1, item.length()));
									JedisShardInfo slave = new JedisShardInfo(h, p,
											(StringUtils.isEmpty(password) ? null : password));
									servers.add(slave);
									logger.info("add redis slave " + h + ":" + p);
								} else {
									JedisShardInfo slave = new JedisShardInfo(item, 6379,
											(StringUtils.isEmpty(password) ? null : password));
									servers.add(slave);
									logger.info("add redis slave " + item + ":6379");
								}
							}
						}
						logger.info("prepare create redis pool...");
						shardedJedisPool = new ShardedJedisPool(config, servers);
						configSuccess.set(true);
					}
				}
				return shardedJedisPool;
			} catch (Exception ex) {
				if (retries++ == MAX_RETRIES) {
					configSuccess.set(false);
					throw new RuntimeException(ex);
				}
				try {
					TimeUnit.MILLISECONDS.sleep(20 + new Random().nextInt(100));// 活锁
				} catch (InterruptedException e) {
				}
			} finally {
				LOCK.unlock();
			}
		}
	}

	/**
	 * 获得排序集合
	 * 
	 * @param key
	 * @param startScore
	 * @param endScore
	 * @param orderByDesc
	 * @return
	 */
	public Set<String> getSoredSet(String key, long startScore, long endScore, boolean orderByDesc) {
		ShardedJedis shardedJedis = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				if (orderByDesc) {
					return shardedJedis.zrevrangeByScore(key, endScore, startScore);
				} else {
					return shardedJedis.zrangeByScore(key, startScore, endScore);
				}
			}
		} catch (Exception ex) {
			logger.error("getSoredSet error.", ex);
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return null;
	}

	/**
	 * 获得排序集合
	 * 
	 * @param key
	 * @param startRange
	 * @param endRange
	 * @param orderByDesc
	 * @return
	 */
	public Set<String> getSoredSetByRange(String key, int startRange, int endRange, boolean orderByDesc) {
		ShardedJedis shardedJedis = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				if (orderByDesc) {
					return shardedJedis.zrevrange(key, startRange, endRange);
				} else {
					return shardedJedis.zrange(key, startRange, endRange);
				}
			}
		} catch (Exception ex) {
			logger.error("getSoredSetByRange error.", ex);
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return null;
	}

	public String getString(String key, String defaultValue) {
		ShardedJedis shardedJedis = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				String value = shardedJedis.get(key);
				return value == null ? defaultValue : value;
			}
		} catch (Exception ex) {
			logger.error("redis get error.", ex.getMessage());
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return defaultValue;
	}

	public String getStringAsync(final String key) {
		boolean waitFor = true;
		Callable<String> task = new Callable<String>() {
			@Override
			public String call() throws Exception {
				ShardedJedis shardedJedis = null;
				try {
					if (configSuccess.get()) {
						shardedJedis = getShardedJedis();
						String value = shardedJedis.get(key);
						return value;
					}
				} catch (Exception ex) {
					logger.error("redis get error.", ex.getMessage());
					returnBrokenResource(shardedJedis);
				} finally {
					returnResource(shardedJedis);
				}
				return null;
			}
		};

		try {
			Future<String> result = pool.submit(task);
			long start = System.currentTimeMillis();
			// 如果需要等待执行结果
			if (waitFor) {
				while (true) {
					if (result.isDone()) {
						return result.get();
					}
					if (System.currentTimeMillis() - start > 200) {
						break;
					}
				}
			}
		} catch (Exception ex) {
			logger.error("", ex);
		}

		return null;
	}

	public boolean hdel(final byte[] region, final byte[] key) {
		ShardedJedis shardedJedis = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				shardedJedis.hdel(region, key);
				return true;
			}
		} catch (Exception ex) {
			logger.error("redis del error.", ex.getMessage());
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return false;
	}

	/**
	 * 删除集合
	 * 
	 * @return
	 */
	public boolean hdel(final byte[] region, final List<byte[]> okeys) {
		ShardedJedis shardedJedis = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				if (okeys != null && okeys.size() > 0) {
					for (byte[] key : okeys) {
						shardedJedis.hdel(region, key);
					}
					return true;
				}
			}
		} catch (Exception ex) {
			logger.error("redis del  error.", ex.getMessage());
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return false;
	}

	public void hdelAsync(final byte[] region, final byte[] key) {
		boolean waitFor = true;
		long start = System.currentTimeMillis();
		Callable<Void> task = new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				ShardedJedis shardedJedis = null;
				try {
					if (configSuccess.get()) {
						shardedJedis = getShardedJedis();
						shardedJedis.hdel(region, key);
					}
				} catch (Exception ex) {
					logger.error("redis get error", ex.getMessage());
					returnBrokenResource(shardedJedis);
				} finally {
					returnResource(shardedJedis);
				}
				return null;
			}
		};

		try {
			Future<Void> result = pool.submit(task);
			// 如果需要等待执行结果
			if (waitFor) {
				while (true) {
					if (result.isDone()) {
						long time = System.currentTimeMillis() - start;
						logger.debug("redis remove ok, 耗时(毫秒): " + time);
						return;
					}
					if (System.currentTimeMillis() - start > 200) {
						break;
					}
				}
			}
		} catch (Exception ex) {
		}
	}

	public byte[] hgetAsync(final byte[] region, final byte[] key) {
		boolean waitFor = true;
		long start = System.currentTimeMillis();
		Callable<byte[]> task = new Callable<byte[]>() {
			@Override
			public byte[] call() throws Exception {
				ShardedJedis shardedJedis = null;
				try {
					if (configSuccess.get()) {
						shardedJedis = getShardedJedis();
						byte[] value = shardedJedis.hget(region, key);
						return value;
					}
				} catch (Exception ex) {
					logger.error("redis get error", ex.getMessage());
					returnBrokenResource(shardedJedis);
				} finally {
					returnResource(shardedJedis);
				}
				return null;
			}
		};

		try {
			Future<byte[]> result = pool.submit(task);
			// 如果需要等待执行结果
			if (waitFor) {
				while (true) {
					if (result.isDone()) {
						long time = System.currentTimeMillis() - start;
						logger.debug("redis get ok, 耗时(毫秒): " + time);
						return result.get();
					}
					if (System.currentTimeMillis() - start > 200) {
						break;
					}
				}
			}
		} catch (Exception ex) {
			logger.error("", ex);
		}
		return null;
	}

	/**
	 * 返回 region 指定的哈希集中所有字段的key值
	 * 
	 * @param region
	 * @return
	 */
	public Set<String> hkeys(String region) {
		ShardedJedis shardedJedis = null;
		Set<String> retList = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				retList = shardedJedis.hkeys(region);
			}
		} catch (Exception ex) {
			logger.error("hkeys error.", ex);
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return retList;
	}

	public boolean hset(byte[] region, byte[] key, byte[] value) {
		ShardedJedis shardedJedis = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				shardedJedis.hset(region, key, value);
				return true;
			}
		} catch (Exception ex) {
			logger.error("redis set error.", ex.getMessage());
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return false;
	}

	public void hsetAsync(final byte[] region, final byte[] key, final byte[] value) {
		boolean waitFor = true;
		long start = System.currentTimeMillis();
		Callable<Void> task = new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				ShardedJedis shardedJedis = null;
				try {
					if (configSuccess.get()) {
						shardedJedis = getShardedJedis();
						shardedJedis.hset(region, key, value);
					}
				} catch (Exception ex) {
					logger.error("redis get error", ex.getMessage());
					returnBrokenResource(shardedJedis);
				} finally {
					returnResource(shardedJedis);
				}
				return null;
			}
		};

		try {
			Future<Void> result = pool.submit(task);
			if (waitFor) {
				while (true) {
					if (result.isDone()) {
						long time = System.currentTimeMillis() - start;
						logger.debug("redis hset ok, 耗时(毫秒): " + time);
						return;
					}
					if (System.currentTimeMillis() - start > 200) {
						break;
					}
				}
			}
		} catch (Exception ex) {
		}
	}

	/**
	 * 返回 region 指定的哈希集中所有字段的value值
	 * 
	 * @param region
	 * @return
	 */

	public List<String> hvals(String region) {
		ShardedJedis shardedJedis = null;
		List<String> retList = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				retList = shardedJedis.hvals(region);
			}
		} catch (Exception ex) {
			logger.error("hvals error.", ex);
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return retList;
	}

	public long incr(String key) {
		ShardedJedis shardedJedis = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				return shardedJedis.incr(key);
			}
		} catch (Exception ex) {
			logger.error("incr error.", ex);
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return 0;
	}

	/**
	 * 获取List
	 * 
	 * @param key
	 * @return
	 */
	public List<String> keys(String key) {
		ShardedJedis shardedJedis = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				return shardedJedis.lrange(key, 0, -1);
			}
		} catch (Exception ex) {
			logger.error("getList error.", ex);
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return null;
	}

	/**
	 * 返回 region 指定的哈希key值总数
	 * 
	 * @param domain
	 * @return
	 */
	public long lenHset(String region) {
		ShardedJedis shardedJedis = null;
		long retList = 0;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				retList = shardedJedis.hlen(region);
			}
		} catch (Exception ex) {
			logger.error("hkeys error.", ex);
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return retList;
	}

	/**
	 * 截取List
	 * 
	 * @param key
	 * @param start
	 *            起始位置
	 * @param end
	 *            结束位置
	 * @return
	 */
	public List<String> rangeList(String key, long start, long end) {
		if (key == null || key.equals("")) {
			return null;
		}
		ShardedJedis shardedJedis = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				return shardedJedis.lrange(key, start, end);
			}
		} catch (Exception ex) {
			logger.error("rangeList 出错[key=" + key + " start=" + start + " end=" + end + "]" + ex.getMessage(), ex);
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return null;
	}

	/**
	 * 从list中删除value 默认count 1
	 * 
	 * @param key
	 * @param values
	 *            值list
	 * @return
	 */
	public int removeListValue(String key, List<String> values) {
		return removeListValue(key, 1, values);
	}

	/**
	 * 从list中删除value
	 * 
	 * @param key
	 * @param count
	 * @param values
	 *            值list
	 * @return
	 */
	public int removeListValue(String key, long count, List<String> values) {
		int result = 0;
		if (values != null && values.size() > 0) {
			for (String value : values) {
				if (removeListValue(key, count, value)) {
					result++;
				}
			}
		}
		return result;
	}

	/**
	 * 从list中删除value
	 * 
	 * @param key
	 * @param count
	 *            要删除个数
	 * @param value
	 * @return
	 */
	public boolean removeListValue(String key, long count, String value) {
		ShardedJedis shardedJedis = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				shardedJedis.lrem(key, count, value);
				return true;
			}
		} catch (Exception ex) {
			logger.error("getList error.", ex);
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return false;
	}

	/**
	 * 从set中删除value
	 * 
	 * @param key
	 * @return
	 */
	public boolean removeSetValue(String key, String... value) {
		ShardedJedis shardedJedis = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				shardedJedis.srem(key, value);
				return true;
			}
		} catch (Exception ex) {
			logger.error("getList error.", ex);
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return false;
	}

	private void returnBrokenResource(ShardedJedis shardedJedis) {
		if (shardedJedis != null) {
			try {
				shardedJedis.close();
				shardedJedis = null;
			} catch (Exception e) {
				logger.error("returnResource error.", e);
			}
		}
	}

	public void returnResource(Jedis jedis, boolean b) {
		if (jedis != null) {
			try {
				jedis.close();
				jedis = null;
			} catch (Exception e) {
				logger.error("returnResource error.", e);
			}
		}
	}

	private void returnResource(ShardedJedis shardedJedis) {
		if (shardedJedis != null) {
			try {
				shardedJedis.close();
				shardedJedis = null;
			} catch (Exception e) {
				logger.error("returnResource error.", e);
			}
		}
	}

	/**
	 * 全局扫描hset
	 * 
	 * @param match
	 *            field匹配模式
	 * @return
	 */
	public List<Map.Entry<String, String>> scanHSet(String region, String match) {
		ShardedJedis shardedJedis = null;
		try {
			int cursor = 0;
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				ScanParams scanParams = new ScanParams();
				scanParams.match(match);
				Jedis jedis = shardedJedis.getShard(region);
				ScanResult<Map.Entry<String, String>> scanResult;
				List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>();
				do {
					scanResult = jedis.hscan(region, String.valueOf(cursor), scanParams);
					list.addAll(scanResult.getResult());
					cursor = Integer.parseInt(scanResult.getStringCursor());
				} while (cursor > 0);
				return list;
			}
		} catch (Exception ex) {
			logger.error("scanHSet error.", ex);
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return null;
	}

	public boolean set(byte[] key, byte[] value) {
		ShardedJedis shardedJedis = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				shardedJedis.set(key, value);
				return true;
			}
		} catch (Exception ex) {
			logger.error("redis set error.", ex.getMessage());
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return false;
	}

	public boolean set(byte[] key, byte[] value, int seconds) {
		ShardedJedis shardedJedis = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				shardedJedis.setex(key, seconds, value);
				return true;
			}
		} catch (Exception ex) {
			logger.error("redis set error.", ex.getMessage());
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return false;
	}

	public boolean set(String key, String value) {
		ShardedJedis shardedJedis = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				shardedJedis.set(key, value);
				return true;
			}
		} catch (Exception ex) {
			logger.error("redis set error.", ex.getMessage());
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return false;
	}

	public boolean set(String key, String value, int seconds) {
		ShardedJedis shardedJedis = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				shardedJedis.setex(key, seconds, value);
				return true;
			}
		} catch (Exception ex) {
			logger.error("redis set error.", ex.getMessage());
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return false;
	}

	public void setByteArray(String key, byte[] value) {
		int tries = 0;
		boolean sucess = false;
		do {
			tries++;
			try {
				if (key != null && value != null) {
					Jedis jedis = this.getShardedJedis().getShard("master");
					jedis.set(key.getBytes(), value);
					jedis.close();
				}
				sucess = true;
			} catch (JedisConnectionException e) {
				logger.error("Jedis connection failed, retrying..." + tries);
				if (tries == numRetries) {
					throw e;
				}
			}
		} while (!sucess && tries <= numRetries);
	}

	/**
	 * 设置HashSet对象
	 * 
	 * @param region
	 *            域名
	 * @param key
	 *            键值
	 * @param value
	 *            Json String or String value
	 * @return
	 */
	public boolean setHSet(String region, String key, String value) {
		if (value == null)
			return false;
		ShardedJedis shardedJedis = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				shardedJedis.hset(region, key, value);
				return true;
			}
		} catch (Exception ex) {
			logger.error("setHSet error.", ex);
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return false;
	}

	/**
	 * 设置排序集合
	 * 
	 * @param key
	 * @param score
	 * @param value
	 * @return
	 */
	public boolean setSortedSet(String key, long score, String value) {
		ShardedJedis shardedJedis = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				shardedJedis.zadd(key, score, value);
				return true;
			}
		} catch (Exception ex) {
			logger.error("setSortedSet error.", ex);
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return false;
	}

	public Long setStringIfKeyNotExists(byte[] key, byte[] value) {
		int tries = 0;
		Long retVal = null;
		boolean sucess = false;
		do {
			tries++;
			try {
				if (key != null && value != null) {
					Jedis jedis = this.getShardedJedis().getShard("master");
					retVal = jedis.setnx(key, value);
					jedis.close();
				}
				sucess = true;
			} catch (JedisConnectionException e) {
				logger.error("Jedis connection failed, retrying..." + tries);
				if (tries == numRetries) {
					throw e;
				}
			}
		} while (!sucess && tries <= numRetries);
		return retVal;
	}

	/**
	 * 截断一个List
	 * 
	 * @param key
	 *            列表key
	 * @param start
	 *            开始位置 从0开始
	 * @param end
	 *            结束位置
	 * @return 状态码
	 */
	public String trimList(String key, long start, long end) {
		if (key == null || key.equals("")) {
			return "-";
		}
		ShardedJedis shardedJedis = null;
		try {
			if (configSuccess.get()) {
				shardedJedis = this.getShardedJedis();
				return shardedJedis.ltrim(key, start, end);
			}
		} catch (Exception ex) {
			logger.error("LTRIM 出错[key=" + key + " start=" + start + " end=" + end + "]" + ex.getMessage(), ex);
			returnBrokenResource(shardedJedis);
		} finally {
			returnResource(shardedJedis);
		}
		return "-";
	}
}
