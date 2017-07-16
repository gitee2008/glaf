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

package com.glaf.core.resource.redis;

import java.util.ArrayList;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glaf.core.factory.RedisFactory;
import com.glaf.core.resource.Resource;

/**
 * Redis 分布式配置实现
 * 
 */
public class RedisResource implements Resource {

	private final static Logger log = LoggerFactory.getLogger(RedisResource.class);

	protected String region;

	protected byte[] region2;

	public RedisResource(String region) {
		this.region = getRegionName(region);
		this.region2 = region.getBytes();
	}

	@Override
	public void clear() {
		try {
			RedisFactory.getInstance().del(region2);
			log.debug("delete region:" + region);
			List<String> keys = new ArrayList<String>();
			keys.addAll(RedisFactory.getInstance().hkeys(region));
			for (int i = 0; i < keys.size(); i++) {
				RedisFactory.getInstance().hdelAsync(region2, getKeyName(keys.get(i)));
			}
			log.debug("###################################");
			log.debug(region + " clear from redis ");
			log.debug("###################################");
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@SuppressWarnings("rawtypes")
	public void evict(List keys) {
		if (keys == null || keys.size() == 0) {
			return;
		}
		try {
			List<byte[]> okeys = new ArrayList<byte[]>();
			for (int i = 0; i < keys.size(); i++) {
				Object object = keys.get(i);
				if (object instanceof byte[]) {
					byte[] key = (byte[]) object;
					okeys.add(key);
				} else {
					byte[] key = getKeyName(object);
					okeys.add(key);
				}
			}
			RedisFactory.getInstance().hdel(region2, okeys);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void evict(Object key) {
		try {
			RedisFactory.getInstance().hdelAsync(region2, getKeyName(key));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public byte[] getData(String key) {
		if (null == key) {
			return null;
		}
		try {
			// log.debug("key:"+getKeyName(key));
			byte[] raw = RedisFactory.getInstance().hgetAsync(region2, getKeyName(key));
			return raw;
		} catch (Exception ex) {
			log.error("Error occured when get data from redis", ex);
		}
		return null;
	}

	/**
	 * 生成缓存的 key
	 * 
	 * @param key
	 * @return
	 */
	protected byte[] getKeyName(Object key) {
		if (key instanceof Number) {
			return ("I:" + key).getBytes();
		} else if (key instanceof String || key instanceof StringBuffer || key instanceof StringBuilder) {
			return ("S:" + key).getBytes();
		}
		return ("O:" + key).getBytes();
	}

	/**
	 * 在region里增加一个可选的层级,作为命名空间,使结构更加清晰 同时满足小型应用,多个Cache共享一个redis database的场景
	 * 
	 * @param region
	 * @return
	 */
	private String getRegionName(String region) {
		//String nameSpace = SystemConfig.getRegionName(region);
		//if (StringUtils.isNotEmpty(nameSpace)) {
		//	region = nameSpace + ":" + region;
		//}
		return region;
	}

	@SuppressWarnings("rawtypes")
	public List keys() {
		try {
			List<String> keys = new ArrayList<String>();
			keys.addAll(RedisFactory.getInstance().hkeys(region));
			return keys;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void put(String key, byte[] value) {
		// log.debug("key:" + getKeyName(key));
		if (value == null) {
			evict(key);
		} else {
			try {
				RedisFactory.getInstance().hsetAsync(region2, getKeyName(key), value);
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	public void remove(String key) {
		try {
			RedisFactory.getInstance().hdelAsync(region2, getKeyName(key));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
