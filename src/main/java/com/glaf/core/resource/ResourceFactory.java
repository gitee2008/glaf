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

package com.glaf.core.resource;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.core.config.*;
import com.glaf.core.util.FileUtils;
import com.glaf.core.util.hash.JenkinsHash;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * 分布式资源工厂类 如果需要进行分布式部署，需要将资源项写入分布式存储中
 */
public class ResourceFactory {
	protected static final Log logger = LogFactory.getLog(ResourceFactory.class);

	protected static Configuration conf = BaseConfiguration.create();

	protected static final ResourceChannel channel = ResourceChannel.getInstance();

	protected static final String DISTRIBUTED_ENABLED = "distributed.resource.enabled";

	protected static List<String> regions = new java.util.concurrent.CopyOnWriteArrayList<String>();

	protected static Cache<String, byte[]> cache = CacheBuilder.newBuilder().maximumSize(5000)
			.expireAfterWrite(30, TimeUnit.MINUTES).build();

	protected static ExecutorService pool = Executors.newFixedThreadPool(10);

	protected static volatile ConcurrentMap<String, String> mimeMapping = new ConcurrentHashMap<String, String>();

	protected static volatile ConcurrentMap<String, Integer> etagMap = new ConcurrentHashMap<String, Integer>();

	protected static volatile ConcurrentMap<String, String> etag2Map = new ConcurrentHashMap<String, String>();

	protected static volatile ConcurrentMap<String, Boolean> etag3Map = new ConcurrentHashMap<String, Boolean>();

	public static ConcurrentMap<String, String> getMimeMapping() {
		if (mimeMapping == null) {
			mimeMapping = new ConcurrentHashMap<String, String>();
		}
		return mimeMapping;
	}

	public static ConcurrentMap<String, Integer> getEtagMap() {
		if (etagMap == null) {
			etagMap = new ConcurrentHashMap<String, Integer>();
		}
		return etagMap;
	}

	public static ConcurrentMap<String, String> getEtag2Map() {
		if (etag2Map == null) {
			etag2Map = new ConcurrentHashMap<String, String>();
		}
		return etag2Map;
	}

	public static ConcurrentMap<String, Boolean> getEtag3Map() {
		if (etag3Map == null) {
			etag3Map = new ConcurrentHashMap<String, Boolean>();
		}
		return etag3Map;
	}

	public static void clear(String region) {
		if (SystemConfig.getBoolean(DISTRIBUTED_ENABLED)) {
			String regionName = SystemConfig.getRegionName(region);
			try {
				etagMap.clear();
				etag2Map.clear();
				etag3Map.clear();
				mimeMapping.clear();
				channel.clear(regionName);
				logger.debug("###################################");
				logger.debug(region + " clear.");
				logger.debug("###################################");
			} catch (Throwable ex) {
				// logger.error(ex);
			}
		}
	}

	public static void clearAll() {
		cache.invalidateAll();
		cache.cleanUp();
		etagMap.clear();
		etag2Map.clear();
		etag3Map.clear();
		mimeMapping.clear();
		if (regions != null && !regions.isEmpty()) {
			for (String region : regions) {
				try {
					String regionName = SystemConfig.getRegionName(region);
					channel.clear(regionName);
					logger.debug("###################################");
					logger.debug(region + " clear.");
					logger.debug("###################################");
				} catch (Throwable ex) {
					// logger.error(ex);
				}
			}
		}
	}

	public static byte[] getData(final String region, final String key) {
		final String regionName = SystemConfig.getRegionName(region);
		final String complexKey = regionName + ":" + DigestUtils.md5Hex(key) + "_"
				+ Math.abs(JenkinsHash.getInstance().hash(key.getBytes()));
		// logger.debug("regionName:"+regionName);
		byte[] bytes = cache.getIfPresent(complexKey);
		if (bytes != null) {
			return bytes;
		}
		if (SystemConfig.getBoolean(DISTRIBUTED_ENABLED)) {
			boolean waitFor = true;
			Callable<byte[]> task = new Callable<byte[]>() {
				@Override
				public byte[] call() throws Exception {
					return channel.getData(regionName, complexKey);
				}
			};
			try {
				Future<byte[]> result = pool.submit(task);
				long start = System.currentTimeMillis();
				// 如果需要等待执行结果
				if (waitFor) {
					while (true) {
						if (result.isDone()) {
							bytes = result.get();
							if (bytes != null) {
								cache.put(complexKey, bytes);
							}
						}
						if (System.currentTimeMillis() - start > 100) {
							break;
						}
					}
				}
			} catch (Throwable ex) {
				// logger.error(ex);
			}
		}
		return bytes;
	}

	public static void put(final String region, final String key, final byte[] value) {
		if (!regions.contains(region)) {
			regions.add(region);
		}
		final String regionName = SystemConfig.getRegionName(region);
		final String complexKey = regionName + ":" + DigestUtils.md5Hex(key) + "_"
				+ Math.abs(JenkinsHash.getInstance().hash(key.getBytes()));
		if (value != null && value.length < (conf.getInt("cache_file_size", 5000) * FileUtils.KB_SIZE)) {
			cache.put(complexKey, value);
			if (SystemConfig.getBoolean(DISTRIBUTED_ENABLED)
					&& value.length < (conf.getInt("cache_file_size", 1000) * FileUtils.KB_SIZE)) {
				try {
					channel.put(regionName, complexKey, value);
				} catch (Throwable ex) {
					// logger.error(ex);
				}
			}
		}
	}

	public static void remove(String region, String key) {
		final String regionName = SystemConfig.getRegionName(region);
		final String complexKey = regionName + ":" + DigestUtils.md5Hex(key) + "_"
				+ Math.abs(JenkinsHash.getInstance().hash(key.getBytes()));
		cache.invalidate(complexKey);
		if (SystemConfig.getBoolean(DISTRIBUTED_ENABLED)) {
			try {
				channel.remove(regionName, complexKey);
			} catch (Throwable ex) {
				// logger.error(ex);
			}
		}
	}

	private ResourceFactory() {

	}

}
