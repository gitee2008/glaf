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
package com.glaf.j2cache.redis;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.glaf.j2cache.Cache;
import com.glaf.j2cache.CacheException;
import com.glaf.j2cache.CacheExpiredListener;
import com.glaf.j2cache.CacheProvider;
import com.glaf.j2cache.redis.RedisCache;

/**
 * Redis 缓存实现
 */
public class RedisCacheProvider implements CacheProvider {

	protected static ConcurrentMap<String, RedisCache> concurrentMap = new ConcurrentHashMap<>();

	protected int expireMinutes;

	@Override
	public Cache buildCache(String regionName, boolean autoCreate, CacheExpiredListener listener)
			throws CacheException {
		RedisCache cache = concurrentMap.get(regionName);
		if (cache == null) {
			cache = new RedisCache(regionName, expireMinutes);
			concurrentMap.put(regionName, cache);
		}
		return cache;
	}

	public int getExpireMinutes() {
		return expireMinutes;
	}

	@Override
	public String name() {
		return "redis";
	}

	public void setExpireMinutes(int expireMinutes) {
		this.expireMinutes = expireMinutes;
	}

	@Override
	public void start(Properties props) throws CacheException {

	}

	@Override
	public void stop() {

	}
}
