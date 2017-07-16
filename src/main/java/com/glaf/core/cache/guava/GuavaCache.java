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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class GuavaCache implements com.glaf.core.cache.Cache {
	protected static final Log logger = LogFactory.getLog(GuavaCache.class);

	protected static ConcurrentMap<String, Cache<Object, Object>> cacheConcurrentMap = new ConcurrentHashMap<String, Cache<Object, Object>>();

	protected Cache<Object, Object> cache;

	protected int cacheSize = 200000;

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
	}

	public Object get(String key) {
		Object value = getCache().getIfPresent(key);
		if (value != null) {
			// logger.debug("get object from guava cache.");
		}
		return value;
	}

	public String get(String region, String key) {
		String value = (String)getCache(region).getIfPresent(key);
		if (value != null) {
			// logger.debug("get object from guava cache.");
		}
		return value;
	}

	public Cache<Object, Object> getCache() {
		if (cache == null) {
			cache = CacheBuilder.newBuilder().maximumSize(getCacheSize())
					.expireAfterAccess(getExpireMinutes(), TimeUnit.MINUTES).build();
		}
		return cache;
	}

	public Cache<Object, Object> getCache(String region) {
		Cache<Object, Object> regionCache = cacheConcurrentMap.get(region);
		if (regionCache == null) {
			regionCache = CacheBuilder.newBuilder().maximumSize(getCacheSize())
					.expireAfterAccess(getExpireMinutes(), TimeUnit.MINUTES).build();
			cacheConcurrentMap.put(region, regionCache);
		}
		return regionCache;
	}

	public int getCacheSize() {
		return cacheSize;
	}

	public int getExpireMinutes() {
		return expireMinutes;
	}

	public void put(String key, String value) {
		getCache().put(key, value);
	}

	public void put(String region, String key, String value) {
		getCache(region).put(key, value);
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
