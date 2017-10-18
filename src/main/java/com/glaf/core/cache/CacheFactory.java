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

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.glaf.core.config.BaseConfiguration;
import com.glaf.core.config.Configuration;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.util.ReflectUtils;

public class CacheFactory {
	protected static final Log logger = LogFactory.getLog(CacheFactory.class);
	protected static final String DEFAULT_CONFIG = "com/glaf/core/cache/guava/guavacache-context.xml";
	protected static final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<String, Cache>();
	protected static final List<CacheItem> items = new CopyOnWriteArrayList<CacheItem>();
	protected static final List<String> regions = new CopyOnWriteArrayList<String>();
	protected static Configuration conf = BaseConfiguration.create();
	protected static ExecutorService pool = Executors.newFixedThreadPool(10);
	private static volatile ApplicationContext ctx;

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
					for (CacheItem item : items) {
						String _region = SystemConfig.getRegionName(item.getRegion());
						cache.remove(_region, item.getKey());
					}
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

	public static ApplicationContext getApplicationContext() {
		return ctx;
	}

	public static Object getBean(Object name) {
		if (ctx == null) {
			ctx = reload();
		}
		return ctx.getBean((String) name);
	}

	protected static Cache getCache() {
		Cache cache = null;
		if (SystemConfig.getBoolean("use_level2_cache")) {
			cache = cacheMap.get("j2cache");
			if (cache == null) {
				String cacheClass = "com.glaf.j2cache.J2CacheImpl";
				cache = (Cache) ReflectUtils.instantiate(cacheClass);
				if (cache != null) {
					cacheMap.put("j2cache", cache);
				}
			}
		} else {
			String provider = SystemConfig.getStringValue("cache_provider", "guava");
			cache = cacheMap.get(provider);
			if (cache == null) {
				if (StringUtils.equals(provider, "ehcache")) {
					String cacheClass = "com.glaf.core.cache.ehcache.EHCacheImpl";
					cache = (Cache) ReflectUtils.instantiate(cacheClass);
				} else if (StringUtils.equals(provider, "ehcache3")) {
					String cacheClass = "com.glaf.core.cache.ehcache3.EHCache3Impl";
					cache = (Cache) ReflectUtils.instantiate(cacheClass);
				} else if (StringUtils.equals(provider, "geode")) {
					String cacheClass = "com.glaf.core.cache.geode.GeodeCacheImpl";
					cache = (Cache) ReflectUtils.instantiate(cacheClass);
				} else if (StringUtils.equals(provider, "j2cache")) {
					String cacheClass = "com.glaf.j2cache.J2CacheImpl";
					cache = (Cache) ReflectUtils.instantiate(cacheClass);
				} else {
					String cacheClass = "com.glaf.core.cache.guava.GuavaCache";
					cache = (Cache) ReflectUtils.instantiate(cacheClass);
				}
				if (cache != null) {
					cacheMap.put(provider, cache);
				}
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
							// logger.debug(_region + " get object'" + key + "'
							// from cache.");
							String val = value.toString();
							val = new String(Base64.decodeBase64(val), "UTF-8");
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
				remove(region, key);
				String _region = SystemConfig.getRegionName(region);
				String cacheKey = _region + "_" + key;
				cacheKey = DigestUtils.md5Hex(cacheKey.getBytes());
				int limitSize = conf.getInt("cache.limitSize", 1024000);// 1024KB
				if (value.length() < limitSize) {
					String val = Base64.encodeBase64String(value.getBytes("UTF-8"));
					cache.put(_region, cacheKey, val);
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
					// logger.debug(_region + ":" + cacheKey + " put into
					// cache.");
				}
			}
		} catch (Throwable ex) {

		}
	}

	protected static ApplicationContext reload() {
		try {
			if (null != ctx) {
				Cache cache = getCache();
				if (cache != null) {
					if (items != null && !items.isEmpty()) {
						for (CacheItem item : items) {
							String _region = SystemConfig.getRegionName(item.getRegion());
							try {
								cache.remove(_region, item.getKey());
							} catch (Throwable ex) {
							}
						}
						items.clear();
					}
					if (regions != null && !regions.isEmpty()) {
						for (String region : regions) {
							cache.clear(region);
						}
					}
				}
				ctx = null;
			}

			String configLocation = DEFAULT_CONFIG;

			if (SystemConfig.getBoolean("use_level2_cache")) {
				logger.info("use j2cache...");
				configLocation = "com/glaf/j2cache/j2cache-context.xml";
			} else {
				if (CacheProperties.getString("sys.cache.context") != null) {
					configLocation = CacheProperties.getString("sys.cache.context");
				}
			}

			ctx = new ClassPathXmlApplicationContext(configLocation);
			logger.info("################# CacheFactory ##############");
			logger.info("load cache config: " + configLocation);
			return ctx;
		} catch (Exception ex) {
			if (logger.isDebugEnabled()) {

				logger.debug(ex);
			}
			throw new CacheException("can't reload cache", ex);
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
				items.remove(new CacheItem(_region, cacheKey));
			}
		} catch (Throwable ex) {

		}
	}

	protected static void shutdown() {
		try {
			Cache cache = getCache();
			if (cache != null) {
				if (items != null && !items.isEmpty()) {
					for (CacheItem item : items) {
						String _region = SystemConfig.getRegionName(item.getRegion());
						cache.remove(_region, item.getKey());
					}
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
