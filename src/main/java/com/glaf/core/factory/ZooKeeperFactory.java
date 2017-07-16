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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import com.glaf.core.cache.CacheException;
import com.glaf.core.config.GlobalConfig;
import com.glaf.core.util.SerializationUtils;

public class ZooKeeperFactory {
	private static class ZooKeeperHolder {
		public static ZooKeeperFactory instance = new ZooKeeperFactory();
	}

	protected static final Log logger = LogFactory.getLog(ZooKeeperFactory.class);

	private static final Charset CHARSET = Charset.forName("UTF-8");

	/**
	 * 构建锁
	 */
	private static final Lock LOCK = new ReentrantLock();

	private static final int MAX_RETRIES = 10;

	public static ZooKeeperFactory getInstance() {
		return ZooKeeperHolder.instance;
	}

	protected CuratorFramework zkClient;

	protected void checkRoot(String regionName) {
		String path = "/" + regionName.replace('.', '_');
		try {
			Stat stat = getClient().checkExists().forPath(path);
			if (stat == null) {
				getClient().create().forPath(path);
				logger.debug("zk create root path:" + path);
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
	}

	public void clear(String regionName) {
		checkRoot(regionName);
		String path = "/" + regionName.replace('.', '_');
		try {
			List<String> children = getClient().getChildren().forPath(path);
			if (children != null && !children.isEmpty()) {
				for (String child : children) {
					logger.debug("delete cache key:" + child);
					getClient().delete().inBackground().forPath(path + "/" + child);
				}
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
	}

	private CuratorFramework getClient() {
		int retries = 0;
		while (true) {
			try {
				boolean lockSuccess = LOCK.tryLock(20, TimeUnit.MILLISECONDS);
				if (lockSuccess) {
					if (zkClient == null) {
						Properties props = GlobalConfig.getProperties("sys_zookeeper");
						if (props == null || props.isEmpty()) {
							props = GlobalConfig.getConfigProperties("zookeeper.properties");
						}
						String servers = this.getProperty(props, "zookeeper_servers", "localhost:2181");
						int connectionTimeoutMs = this.getProperty(props, "zookeeper_connectionTimeoutMs", 1000 * 5);
						RetryPolicy retryPolicy = new ExponentialBackoffRetry(connectionTimeoutMs, Integer.MAX_VALUE);
						zkClient = CuratorFrameworkFactory.newClient(servers, retryPolicy);
						zkClient.start();
					}
				}
				if (zkClient != null) {
					return zkClient;
				}
			} catch (java.lang.Throwable ex) {
				if (retries++ == MAX_RETRIES) {
					throw new RuntimeException(ex);
				}
				try {
					TimeUnit.MILLISECONDS.sleep(200 + new Random().nextInt(1000));// 活锁
				} catch (InterruptedException e) {
				}
			} finally {
				LOCK.unlock();
			}
		}

	}

	public Object getObject(String regionName, Object key) throws CacheException {
		checkRoot(regionName);
		String path = "/" + regionName.replace('.', '_') + "/" + key;
		try {
			Stat stat = getClient().checkExists().forPath(path);
			if (stat != null) {
				byte[] data = getClient().getData().forPath(path);
				return SerializationUtils.unserialize(data);
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
		return null;
	}

	protected int getProperty(Properties props, String key, int defaultValue) {
		try {
			return Integer.parseInt(props.getProperty(key, String.valueOf(defaultValue)).trim());
		} catch (Exception e) {
			return defaultValue;
		}
	}

	protected String getProperty(Properties props, String key, String defaultValue) {
		return props.getProperty(key, defaultValue).trim();
	}

	public String getString(String regionName, String key) {
		checkRoot(regionName);
		String path = "/" + regionName.replace('.', '_') + "/" + key;
		try {
			Stat stat = getClient().checkExists().forPath(path);
			if (stat != null) {
				byte[] data = getClient().getData().forPath(path);
				return new String(data, CHARSET);
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public List keys(String regionName) throws CacheException {
		checkRoot(regionName);
		List<String> keys = new ArrayList<String>();
		String path = "/" + regionName.replace('.', '_');
		try {
			List<String> children = getClient().getChildren().forPath(path);
			if (children != null && !children.isEmpty()) {
				for (String child : children) {
					keys.add(child);
				}
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
		return keys;
	}

	public void put(final String regionName, final Object key, final Object value) throws CacheException {
		if (key == null || value == null) {
			throw new RuntimeException("key or value is null");
		}
		byte[] data = null;
		try {
			data = SerializationUtils.serialize(value);
		} catch (Exception ex) {

		}
		if (data == null) {
			throw new RuntimeException("serialize value failed");
		}
		if (data.length >= FileUtils.ONE_MB) {
			throw new RuntimeException("value size exceeds upper limit");
		}
		checkRoot(regionName);
		String path = "/" + regionName.replace('.', '_') + "/" + key;
		try {
			Stat stat = getClient().checkExists().forPath(path);
			if (stat == null) {
				getClient().create().withMode(CreateMode.PERSISTENT).inBackground().forPath(path, data);
				logger.debug("create key:" + key);
			} else {
				getClient().setData().inBackground().forPath(path, data);
				logger.debug("update key:" + key);
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
	}

	public void put(final String regionName, final String key, final String value) {
		if (key == null || value == null) {
			throw new RuntimeException("key or value is null");
		}
		if (value.length() >= FileUtils.ONE_MB) {
			throw new RuntimeException("value size exceeds upper limit");
		}
		checkRoot(regionName);
		String path = "/" + regionName.replace('.', '_') + "/" + key;
		try {
			Stat stat = getClient().checkExists().forPath(path);
			if (stat == null) {
				getClient().create().withMode(CreateMode.PERSISTENT).inBackground().forPath(path,
						value.getBytes(CHARSET));
				logger.debug("create key:" + key);
			} else {
				getClient().setData().inBackground().forPath(path, value.getBytes(CHARSET));
				logger.debug("update key:" + key);
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
	}

	public void remove(String regionName, String key) {
		checkRoot(regionName);
		String path = "/" + regionName.replace('.', '_') + "/" + key;
		try {
			Stat stat = getClient().checkExists().forPath(path);
			if (stat != null) {
				getClient().delete().inBackground().forPath(path);
				logger.debug("remove key:" + key);
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
	}

}
