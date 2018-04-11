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

package com.glaf.core.config.redis;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.glaf.core.config.Config;
import com.glaf.core.config.ConfigProvider;

import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.CacheException;
import net.oschina.j2cache.J2CacheBuilder;
import net.oschina.j2cache.J2CacheConfig;

public class RedisConfigProvider implements ConfigProvider {
	protected final ConcurrentMap<String, Config> concurrentMap = new ConcurrentHashMap<String, Config>();

	private final static String CONFIG_FILE = "/redis_config.properties";

	private final static J2CacheBuilder builder;

	private final static CacheChannel cacheChannel;

	static {
		try {
			J2CacheConfig config = J2CacheConfig.initFromConfig(CONFIG_FILE);
			builder = J2CacheBuilder.init(config);
			cacheChannel = builder.getChannel();
		} catch (IOException e) {
			throw new CacheException("Failed to load redis configuration " + CONFIG_FILE, e);
		}
	}

	@Override
	public Config buildConfig(String regionName, boolean autoCreate) {
		Config config = concurrentMap.get(regionName);
		if (config == null) {
			config = new RedisConfig(regionName, cacheChannel);
			concurrentMap.put(regionName, config);
		}
		return config;
	}

	@Override
	public String name() {
		return "redis";
	}

	@Override
	public void start(Properties props) {

	}

	@Override
	public void stop() {
		builder.close();
	}

}
