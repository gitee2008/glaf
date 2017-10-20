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

import java.io.IOException;

import com.glaf.core.cache.Cache;

import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.CacheObject;
import net.oschina.j2cache.J2Cache;

public class J2CacheImpl implements Cache {

	private CacheChannel cache = J2Cache.getChannel();

	@Override
	public void clear(String region) {
		this.cache.clear(region);
	}

	@Override
	public String get(String region, String key) {
		Object object = this.cache.get(region, key);
		if (object != null) {
			if (object instanceof CacheObject) {
				CacheObject cacheObject = (CacheObject) object;
				if (cacheObject.getValue() != null) {
					if (cacheObject.getValue() instanceof String) {
						String str = (String) cacheObject.getValue();
						try {
							return new String(com.glaf.core.util.Hex.hex2byte(str), "UTF-8");
						} catch (IOException ex) {
							return new String(com.glaf.core.util.Hex.hex2byte(str));
						}
					} else if (cacheObject.getValue() instanceof byte[]) {
						byte[] bytes = (byte[]) cacheObject.getValue();
						try {
							String str = new String(bytes, "UTF-8");
							return str;
						} catch (IOException ex) {
							String str = new String(bytes);
							return str;
						}
					}
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
				this.cache.set(region, key, com.glaf.core.util.Hex.byte2hex(value.getBytes("UTF-8")));
			} catch (IOException ex) {
				this.cache.set(region, key, com.glaf.core.util.Hex.byte2hex(value.getBytes()));
			}
		}
	}

	@Override
	public void remove(String region, String key) {
		this.cache.evict(region, key);
	}

	@Override
	public void shutdown() {
		this.cache.close();
	}

}
