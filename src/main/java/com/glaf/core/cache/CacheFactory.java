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

package com.glaf.core.cache;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.core.config.BaseConfiguration;
import com.glaf.core.config.Configuration;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.util.ReflectUtils;

public class CacheFactory {
	protected static final Log logger = LogFactory.getLog(CacheFactory.class);
	protected static final ConcurrentMap<String, CacheItem> cacheKeyMap = new ConcurrentHashMap<String, CacheItem>();
	protected static final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<String, Cache>();
	protected static final List<CacheItem> items = new CopyOnWriteArrayList<CacheItem>();
	protected static final List<String> regions = new CopyOnWriteArrayList<String>();
	protected static Configuration conf = BaseConfiguration.create();
	protected static ExecutorService pool = Executors.newFixedThreadPool(50);

	public static void clear(final String region) {
		Cache cache = getCache();
		if (cache != null) {
			String _region = SystemConfig.getRegionName(region);
			try {
				cache.clear(_region);
			} catch (Throwable ex) {
			}
		}
	}

	protected static void clearAll() {
		try {
			Cache cache = getCache();
			if (cache != null) {
				if (items != null && !items.isEmpty()) {
					items.clear();
				}
				if (regions != null && !regions.isEmpty()) {
					for (String region : regions) {
						cache.clear(region);
					}
				}
			}
			logger.info("cache clear ok.");
		} catch (Throwable ex) {
		} finally {
			items.clear();
		}
	}

	protected static Cache getCache() {
		Cache cache = null;
		String provider = CacheProperties.getString("cache_provider");
		if (provider != null) {
			cache = cacheMap.get(provider);
		}
		if (cache == null) {
			if (StringUtils.equals(provider, "cacheonix")) {
				String cacheClass = "com.glaf.core.cache.cacheonix.CacheonixCache";
				cache = (Cache) ReflectUtils.instantiate(cacheClass);
			} else if (StringUtils.equals(provider, "caffeine")) {
				String cacheClass = "com.glaf.core.cache.caffeine.CaffeineCache";
				cache = (Cache) ReflectUtils.instantiate(cacheClass);
			} else if (StringUtils.equals(provider, "ehcache")) {
				String cacheClass = "com.glaf.core.cache.ehcache.EHCacheImpl";
				cache = (Cache) ReflectUtils.instantiate(cacheClass);
			} else if (StringUtils.equals(provider, "guava")) {
				String cacheClass = "com.glaf.core.cache.guava.GuavaCache";
				cache = (Cache) ReflectUtils.instantiate(cacheClass);
			} else if (StringUtils.equals(provider, "j2cache")) {
				String cacheClass = "com.glaf.core.cache.j2cache.J2CacheImpl";
				cache = (Cache) ReflectUtils.instantiate(cacheClass);
			} else {
				provider = "caffeine";
				String cacheClass = "com.glaf.core.cache.caffeine.CaffeineCache";
				cache = (Cache) ReflectUtils.instantiate(cacheClass);
			}
			if (cache != null && provider != null) {
				logger.debug("cache provider:" + cache.getClass().getName());
				cacheMap.put(provider, cache);
			}
		}
		return cache;
	}

	public static List<CacheItem> getCacheItems() {
		return items;
	}

	public static String getString(final String region, final String key) {
		boolean waitFor = true;
		Callable<String> task = new Callable<String>() {
			@Override
			public String call() throws Exception {
				try {
					Cache cache = getCache();
					if (cache != null) {
						String _region = SystemConfig.getRegionName(region);
						String cacheKey = _region + "_" + key;
						cacheKey = DigestUtils.md5Hex(cacheKey.getBytes());
						Object value = cache.get(_region, cacheKey);
						if (value != null) {
							String val = value.toString();
							val = new String(com.glaf.core.util.Hex.hex2byte(val), "UTF-8");
							return val;
						}
					}
				} catch (Throwable ex) {
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
		} catch (Exception e) {
			logger.error(e);
		}

		return null;
	}

	public static void put(final String region, final String key, final String value) {
		try {
			Cache cache = getCache();
			if (cache != null && key != null && value != null) {
				// remove(region, key);
				String _region = SystemConfig.getRegionName(region);
				String cacheKey = _region + "_" + key;
				cacheKey = DigestUtils.md5Hex(cacheKey.getBytes());
				int limitSize = conf.getInt("cache.limitSize", 5120000);// 5MB
				if (value.length() < limitSize) {
					String val = com.glaf.core.util.Hex.byte2hex(value.getBytes("UTF-8"));
					cache.put(_region, cacheKey, val);
					logger.debug("put object into cache.");
					if (!regions.contains(_region)) {
						regions.add(_region);
					}
					CacheItem item = new CacheItem();
					item.setRegion(region);
					item.setName(key);
					item.setKey(cacheKey);
					item.setLastModified(System.currentTimeMillis());
					item.setSize(value.length());
					items.add(item);
					cacheKeyMap.put(cacheKey, item);
				}
			}
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
	}

	public static void put(final String region, final String key, final String value, long timeToLiveInSeconds) {
		try {
			Cache cache = getCache();
			if (cache != null && key != null && value != null) {
				// remove(region, key);
				String _region = SystemConfig.getRegionName(region);
				String cacheKey = _region + "_" + key;
				cacheKey = DigestUtils.md5Hex(cacheKey.getBytes());
				int limitSize = conf.getInt("cache.limitSize", 5120000);// 5MB
				if (value.length() < limitSize) {
					String val = com.glaf.core.util.Hex.byte2hex(value.getBytes("UTF-8"));
					cache.put(_region, cacheKey, val, timeToLiveInSeconds);
					logger.debug("put object into cache.");
					if (!regions.contains(_region)) {
						regions.add(_region);
					}
					CacheItem item = new CacheItem();
					item.setRegion(region);
					item.setName(key);
					item.setKey(cacheKey);
					item.setLastModified(System.currentTimeMillis());
					item.setTimeToLiveInSeconds(timeToLiveInSeconds);
					item.setSize(value.length());
					items.add(item);
					cacheKeyMap.put(cacheKey, item);
				}
			}
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
	}

	public static void remove(final String region, final String key) {
		try {
			Cache cache = getCache();
			if (cache != null) {
				String _region = SystemConfig.getRegionName(region);
				String cacheKey = _region + "_" + key;
				cacheKey = DigestUtils.md5Hex(cacheKey.getBytes());
				cache.remove(_region, cacheKey);
				logger.debug("remove object from cache.");
				CacheItem item = cacheKeyMap.get(cacheKey);
				if (item != null) {
					items.remove(item);
				}
				cacheKeyMap.remove(cacheKey);
			}
		} catch (Throwable ex) {
		}
	}

	protected static void shutdown() {
		try {
			Cache cache = getCache();
			if (cache != null) {
				if (items != null && !items.isEmpty()) {
					items.clear();
				}
				if (regions != null && !regions.isEmpty()) {
					for (String region : regions) {
						cache.clear(region);
					}
				}
			}
		} catch (Throwable ex) {
		}
	}

}
