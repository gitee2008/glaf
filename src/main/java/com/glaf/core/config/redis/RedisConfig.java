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
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glaf.core.config.Config;

import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.CacheObject;

public class RedisConfig implements Config {
	protected static final List<String> regions = new CopyOnWriteArrayList<String>();

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected String regionName;

	protected CacheChannel cacheChannel;

	public RedisConfig(String regionName, CacheChannel cacheChannel) {
		this.regionName = regionName;
		this.cacheChannel = cacheChannel;
	}

	@Override
	public void clear() {
		try {
			cacheChannel.clear(regionName);
		} catch (Exception e) {
		}
	}

	@Override
	public String getString(String key) {
		Object object = null;
		try {
			object = cacheChannel.get(regionName, key);
		} catch (Exception e) {
		}
		if (object != null) {
			logger.debug("get object from redis.");
			if (object instanceof net.oschina.j2cache.CacheObject) {
				CacheObject cacheObject = (CacheObject) object;
				Object value = cacheObject.getValue();
				if (value != null) {
					// logger.debug("value class:" + value.getClass().getName());
					if (value instanceof String) {
						String str = (String) value;
						try {
							return new String(com.glaf.core.util.Hex.hex2byte(str), "UTF-8");
						} catch (IOException ex) {
							return new String(com.glaf.core.util.Hex.hex2byte(str));
						}
					} else if (value instanceof byte[]) {
						byte[] bytes = (byte[]) value;
						try {
							String str = new String(bytes, "UTF-8");
							return str;
						} catch (IOException ex) {
							String str = new String(bytes);
							return str;
						}
					} else {
						return cacheObject.asString();
					}
				} else {
					logger.debug("value is null.");
					return cacheObject.asString();
				}
			} else {
				return object.toString();
			}
		}
		return null;
	}

	@Override
	public void put(String key, String value) {
		if (value != null) {
			try {
				cacheChannel.set(regionName, key, com.glaf.core.util.Hex.byte2hex(value.getBytes("UTF-8")), 7200L);
				if (!regions.contains(regionName)) {
					regions.add(regionName);
				}
				logger.debug("put object into redis.");
			} catch (Exception ex) {
				// logger.error("put j2cache error", ex);
				try {
					cacheChannel.set(regionName, key, com.glaf.core.util.Hex.byte2hex(value.getBytes()), 7200L);
				} catch (Exception e) {
				}
			}
		}
	}

	@Override
	public void remove(String key) {
		try {
			cacheChannel.evict(regionName, key);
		} catch (Exception e) {
		}
	}

}
