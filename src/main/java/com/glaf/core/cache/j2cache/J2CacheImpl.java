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

package com.glaf.core.cache.j2cache;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glaf.core.cache.Cache;

import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.CacheObject;
import net.oschina.j2cache.J2Cache;

public class J2CacheImpl implements Cache {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private static CacheChannel cacheChannel = J2Cache.getChannel();

	protected static final List<String> regions = new CopyOnWriteArrayList<String>();

	@Override
	public void clear(String region) {
		try {
			cacheChannel.clear(region);
		} catch (Exception e) {
		}
	}

	@Override
	public String get(String region, String key) {
		Object object = null;
		try {
			object = cacheChannel.get(region, key);
		} catch (Exception e) {
		}
		if (object != null) {
			logger.debug("get object from j2cache.");
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
	public void put(String region, String key, String value) {
		if (value != null) {
			try {
				cacheChannel.set(region, key, com.glaf.core.util.Hex.byte2hex(value.getBytes("UTF-8")), 1800L);
				if (!regions.contains(region)) {
					regions.add(region);
				}
				logger.debug("put object into j2cache.");
			} catch (Exception ex) {
				// logger.error("put j2cache error", ex);
				try {
					cacheChannel.set(region, key, com.glaf.core.util.Hex.byte2hex(value.getBytes()), 1800L);
				} catch (Exception e) {
				}
			}
		}
	}

	@Override
	public void put(String region, String key, String value, long timeToLiveInSeconds) {
		if (value != null) {
			if (timeToLiveInSeconds == 0) {
				timeToLiveInSeconds = 1800L;
			}
			try {
				cacheChannel.set(region, key, com.glaf.core.util.Hex.byte2hex(value.getBytes("UTF-8")),
						timeToLiveInSeconds);
				if (!regions.contains(region)) {
					regions.add(region);
				}
				logger.debug("put object into j2cache.");
			} catch (Exception ex) {
				// logger.error("put j2cache error", ex);
				try {
					cacheChannel.set(region, key, com.glaf.core.util.Hex.byte2hex(value.getBytes()),
							timeToLiveInSeconds);
				} catch (Exception e) {
				}
			}
		}
	}

	@Override
	public void remove(String region, String key) {
		try {
			cacheChannel.evict(region, key);
		} catch (Exception e) {
		}
	}

	@Override
	public void shutdown() {
		for (String region : regions) {
			try {
				cacheChannel.clear(region);
			} catch (Exception e) {
			}
		}
		cacheChannel.close();
	}

}
