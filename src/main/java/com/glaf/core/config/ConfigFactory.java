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

package com.glaf.core.config;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.glaf.core.util.FileUtils;
import com.glaf.core.util.hash.JenkinsHash;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * 分布式配置工厂类 如果需要进行分布式部署，需要将配置项写入分布式存储中
 */
public class ConfigFactory {
	protected static final Log logger = LogFactory.getLog(ConfigFactory.class);

	protected static Configuration conf = BaseConfiguration.create();

	protected static final ConfigChannel channel = ConfigChannel.getInstance();

	protected static final String DISTRIBUTED_ENABLED = "distributed.config.enabled";

	protected static List<String> regions = new java.util.concurrent.CopyOnWriteArrayList<String>();

	protected static Cache<String, String> cache = CacheBuilder.newBuilder().maximumSize(20000)
			.expireAfterWrite(5, TimeUnit.MINUTES).expireAfterAccess(5, TimeUnit.MINUTES).build();

	protected static ExecutorService pool = Executors.newFixedThreadPool(10);

	public static void clear(String region) {
		if (conf.getBoolean(DISTRIBUTED_ENABLED, false)) {
			final String regionName = SystemConfig.getRegionName(region);
			try {
				channel.clear(regionName);
				logger.debug("###################################");
				logger.debug(region + " clear.");
				logger.debug("###################################");
			} catch (Throwable ex) {
				logger.error(ex);
			}
		}
	}

	public static void clearAll() {
		if (conf.getBoolean(DISTRIBUTED_ENABLED, false)) {
			if (regions != null && !regions.isEmpty()) {
				for (String region : regions) {
					try {
						final String regionName = SystemConfig.getRegionName(region);
						channel.clear(regionName);
						logger.debug("###################################");
						logger.debug(region + " clear.");
						logger.debug("###################################");
					} catch (Throwable ex) {
						logger.error(ex);
					}
				}
			}
		}
		cache.invalidateAll();
		cache.cleanUp();
	}

	public static JSONObject getJSONObject(String region, String key) {
		String text = getString(region, key);
		if (StringUtils.isNotEmpty(text)) {
			JSONObject jsonObject = JSON.parseObject(text);
			return jsonObject;
		}
		return null;
	}

	public static String getString(final String region, final String key) {
		final String regionName = SystemConfig.getRegionName(region);
		logger.debug("regionName->" + regionName);
		final String complexKey = regionName + ":" + DigestUtils.md5Hex(key) + "_"
				+ Math.abs(JenkinsHash.getInstance().hash(key.getBytes()));
		if (conf.getBoolean(DISTRIBUTED_ENABLED, false)) {
			return channel.getString(regionName, complexKey);
		}

		return cache.getIfPresent(complexKey);
	}

	public static void put(final String region, final String key, final String value) {
		if (!regions.contains(region)) {
			regions.add(region);
		}
		final String regionName = SystemConfig.getRegionName(region);
		final String complexKey = regionName + ":" + DigestUtils.md5Hex(key) + "_"
				+ Math.abs(JenkinsHash.getInstance().hash(key.getBytes()));
		if (conf.getBoolean(DISTRIBUTED_ENABLED, false)) {
			try {
				channel.put(regionName, complexKey, value);
			} catch (Throwable ex) {
				logger.error(ex);
			}
		}
		if (value != null && value.length() < conf.getInt("cache_file_size", 200) * FileUtils.KB_SIZE) {
			cache.put(complexKey, value);
		}
	}

	public static void remove(String region, String key) {
		final String regionName = SystemConfig.getRegionName(region);
		final String complexKey = regionName + ":" + DigestUtils.md5Hex(key) + "_"
				+ Math.abs(JenkinsHash.getInstance().hash(key.getBytes()));
		if (conf.getBoolean(DISTRIBUTED_ENABLED, false)) {
			try {
				channel.remove(regionName, complexKey);
			} catch (Throwable ex) {
				logger.error(ex);
			}
		}
		cache.invalidate(complexKey);
	}

	private ConfigFactory() {

	}

}
