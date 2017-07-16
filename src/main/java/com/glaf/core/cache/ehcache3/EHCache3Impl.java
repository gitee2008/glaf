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

package com.glaf.core.cache.ehcache3;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.units.MemoryUnit;
import org.slf4j.Logger;

import static org.ehcache.config.builders.CacheConfigurationBuilder.newCacheConfigurationBuilder;
import static org.ehcache.config.builders.CacheManagerBuilder.newCacheManagerBuilder;
import static org.ehcache.config.builders.ResourcePoolsBuilder.heap;

import static org.slf4j.LoggerFactory.getLogger;

public class EHCache3Impl implements com.glaf.core.cache.Cache {

	protected static final Logger LOGGER = getLogger(EHCache3Impl.class);

	protected static ConcurrentMap<String, CacheManager> cacheMgrConcurrentMap = new ConcurrentHashMap<String, CacheManager>();

	protected static ConcurrentMap<String, Cache<String, String>> cacheConcurrentMap = new ConcurrentHashMap<String, Cache<String, String>>();

	public void clear(String region) {
		Cache<String, String> cache = getCache(region);
		cache.clear();
	}

	public String get(String region, String key) {
		Cache<String, String> cache = getCache(region);
		return cache.get(key);
	}

	public Cache<String, String> getCache(String region) {
		if (cacheConcurrentMap.get(region) != null) {
			return cacheConcurrentMap.get(region);
		}
		Cache<String, String> cache = getCacheManager(region).getCache(region, String.class, String.class);
		cacheConcurrentMap.put(region, cache);
		return cache;
	}

	public CacheManager getCacheManager(String region) {
		if (cacheMgrConcurrentMap.get(region) != null) {
			return cacheMgrConcurrentMap.get(region);
		}
		CacheManager cacheManager = newCacheManagerBuilder()
				.withCache(region,
						newCacheConfigurationBuilder(String.class, String.class, heap(100).offheap(10, MemoryUnit.MB)))
				.build(true);
		cacheMgrConcurrentMap.put(region, cacheManager);
		return cacheManager;
	}

	public void put(String region, String key, String value) {
		Cache<String, String> cache = getCache(region);
		cache.putIfAbsent(key, value);
	}

	public void remove(String region, String key) {
		Cache<String, String> cache = getCache(region);
		cache.remove(key);
	}

	public void shutdown() {
		Collection<CacheManager> rows = cacheMgrConcurrentMap.values();
		for (CacheManager mgr : rows) {
			mgr.close();
		}
	}

}
