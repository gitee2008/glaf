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

package com.glaf.core.cache.guava;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class GuavaCache implements com.glaf.core.cache.Cache {
	protected static final Log logger = LogFactory.getLog(GuavaCache.class);

	protected static ConcurrentMap<String, Cache<Object, Object>> cacheConcurrentMap = new ConcurrentHashMap<String, Cache<Object, Object>>();

	protected static AtomicBoolean running = new AtomicBoolean(false);

	protected Cache<Object, Object> cache;

	protected int cacheSize = 50000;

	protected int expireMinutes = 10;

	public GuavaCache() {

	}

	public void clear() {
		getCache().invalidateAll();
		getCache().cleanUp();

		Iterator<String> iterator = cacheConcurrentMap.keySet().iterator();
		while (iterator.hasNext()) {
			String region = iterator.next();
			getCache(region).invalidateAll();
			getCache(region).cleanUp();
		}
		cacheConcurrentMap.clear();
	}

	public void clear(String region) {
		getCache(region).invalidateAll();
		getCache(region).cleanUp();
		cacheConcurrentMap.remove(region);
	}

	public Object get(String key) {
		Object value = getCache().getIfPresent(key);
		if (value != null) {

		}
		return value;
	}

	@Override
	public String get(String region, String key) {
		Object value = getCache(region).getIfPresent(key);
		if (value != null) {
			if (value instanceof String) {
				return (String) value;
			}
		}
		return null;
	}

	public Cache<Object, Object> getCache() {
		if (cache == null) {
			if (!running.get()) {
				try {
					running.set(true);
					if (cache == null) {
						cache = CacheBuilder.newBuilder().maximumSize(getCacheSize())
								.expireAfterWrite(getExpireMinutes(), TimeUnit.MINUTES).build();
					}
				} finally {
					running.set(false);
				}
			}
		}
		return cache;
	}

	public Cache<Object, Object> getCache(String region) {
		Cache<Object, Object> regionCache = cacheConcurrentMap.get(region);
		if (regionCache == null) {
			if (!running.get()) {
				try {
					running.set(true);
					if (regionCache == null) {
						regionCache = CacheBuilder.newBuilder().maximumSize(getCacheSize())
								.expireAfterWrite(getExpireMinutes(), TimeUnit.MINUTES).build();
						cacheConcurrentMap.put(region, regionCache);
					}
				} finally {
					running.set(false);
				}
			}
		}
		return regionCache;
	}

	public int getCacheSize() {
		return cacheSize;
	}

	public int getExpireMinutes() {
		return expireMinutes;
	}

	public void put(String key, Object value) {
		Cache<Object, Object> cache = this.getCache();
		cache.invalidate(key);
		cache.put(key, value);
	}

	public void put(String region, String key, Object value) {
		Cache<Object, Object> cache = this.getCache(region);
		cache.invalidate(key);
		cache.put(key, value);
	}

	@Override
	public void put(String region, String key, String value) {
		Cache<Object, Object> cache = this.getCache(region);
		cache.invalidate(key);
		cache.put(key, value);
	}

	@Override
	public void put(String region, String key, String value, long timeToLiveInSeconds) {
		Cache<Object, Object> cache = this.getCache(region);
		cache.invalidate(key);
		cache.put(key, value);
	}

	public void remove(String key) {
		getCache().invalidate(key);
	}

	public void remove(String region, String key) {
		getCache(region).invalidate(key);
	}

	public void setCacheSize(int cacheSize) {
		this.cacheSize = cacheSize;
	}

	public void setExpireMinutes(int expireMinutes) {
		this.expireMinutes = expireMinutes;
	}

	public void shutdown() {
		getCache().invalidateAll();
		getCache().cleanUp();
		Iterator<String> iterator = cacheConcurrentMap.keySet().iterator();
		while (iterator.hasNext()) {
			String region = iterator.next();
			getCache(region).invalidateAll();
			getCache(region).cleanUp();
		}
		cacheConcurrentMap.clear();
	}
}
