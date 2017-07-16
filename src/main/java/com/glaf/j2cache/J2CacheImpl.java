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

package com.glaf.j2cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.core.cache.Cache;
import com.glaf.core.config.SystemConfig;

public class J2CacheImpl implements Cache {

	protected static final Log logger = LogFactory.getLog(J2CacheImpl.class);

	protected static final CacheChannel cacheChannel = J2Cache.getChannel();

	public J2CacheImpl() {
		logger.info("-----------------------J2Cache---------------------------");
	}

	public void clear() {
		String region = SystemConfig.getRegionName("j2cache");
		cacheChannel.clear(region);
		logger.debug("###################################");
		logger.debug(region + " clear from j2cache");
		logger.debug("###################################");
	}

	public void clear(String region) {
		cacheChannel.clear(region);
		logger.debug("###################################");
		logger.debug(region + " clear from j2cache");
		logger.debug("###################################");
	}

	public Object get(String key) {
		String region = SystemConfig.getRegionName("j2cache");
		CacheObject object = cacheChannel.get(region, key);
		if (object != null) {
			return object.getValue();
		}
		return null;
	}

	public String get(String region, String key) {
		CacheObject object = cacheChannel.get(region, key);
		if (object != null) {
			return (String) object.getValue();
		}
		return null;
	}

	public CacheChannel getCacheChannel() {
		return cacheChannel;
	}

	public void put(String key, String value) {
		String region = SystemConfig.getRegionName("j2cache");
		cacheChannel.set(region, key, value);
		// logger.debug(key + " put into j2cache");
	}

	public void put(String region, String key, String value) {
		cacheChannel.set(region, key, value);
	}

	public void remove(String key) {
		String region = SystemConfig.getRegionName("j2cache");
		cacheChannel.evict(region, key);
		// logger.debug(key + " remove from j2cache");
	}

	public void remove(String region, String key) {
		cacheChannel.evict(region, key);
	}

	public void shutdown() {
		String region = SystemConfig.getRegionName("j2cache");
		cacheChannel.clear(region);
		logger.debug("###################################");
		logger.debug(region + " clear from j2cache");
		logger.debug("###################################");
	}

}
