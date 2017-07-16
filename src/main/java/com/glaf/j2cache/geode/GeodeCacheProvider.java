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

package com.glaf.j2cache.geode;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.glaf.j2cache.Cache;
import com.glaf.j2cache.CacheException;
import com.glaf.j2cache.CacheExpiredListener;
import com.glaf.j2cache.CacheProvider;

public class GeodeCacheProvider implements CacheProvider {

	protected static ConcurrentMap<String, GeodeCache> cacheConcurrentMap = new ConcurrentHashMap<String, GeodeCache>();

	protected int cacheSize = 50000;

	protected int expireMinutes = 10;

	@Override
	public Cache buildCache(String regionName, boolean autoCreate, CacheExpiredListener listener)
			throws CacheException {
		GeodeCache cache = cacheConcurrentMap.get(regionName);
		if (cache == null) {
			cache = new GeodeCache(regionName, getCacheSize(), getExpireMinutes());
			cacheConcurrentMap.put(regionName, cache);
		}
		return cache;
	}

	public int getCacheSize() {
		return cacheSize;
	}

	public int getExpireMinutes() {
		return expireMinutes;
	}

	private int getProperty(Properties props, String key, int defaultValue) {
		try {
			return Integer.parseInt(props.getProperty(key, String.valueOf(defaultValue)).trim());
		} catch (Exception e) {
			return defaultValue;
		}
	}

	@Override
	public String name() {
		return "guava";
	}

	public void setCacheSize(int cacheSize) {
		this.cacheSize = cacheSize;
	}

	public void setExpireMinutes(int expireMinutes) {
		this.expireMinutes = expireMinutes;
	}

	@Override
	public void start(Properties props) throws CacheException {
		cacheSize = getProperty(props, "cacheSize", 50000);
		expireMinutes = getProperty(props, "expireMinutes", 10);
	}

	@Override
	public void stop() {

	}

}
