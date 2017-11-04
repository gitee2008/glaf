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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glaf.core.config.Config;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.factory.RedisFactory;
import com.glaf.core.util.SerializationUtils;

/**
 * Redis 分布式配置实现
 * 
 */
public class RedisConfig implements Config {

	private final static Logger log = LoggerFactory.getLogger(RedisConfig.class);

	private String region;

	protected byte[] region2;

	public RedisConfig(String region) {
		this.region = getRegionName(region);
		this.region2 = region.getBytes();
	}

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

	public void destroy() {
		this.clear();
	}

	@SuppressWarnings("rawtypes")
	public void evict(List keys) {
		if (keys == null || keys.size() == 0) {
			return;
		}
		try {
			List<byte[]> okeys = new ArrayList<byte[]>();
			for (int i = 0, len = keys.size(); i < len; i++) {
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
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public void evict(Object key) {
		try {
			RedisFactory.getInstance().hdelAsync(region2, getKeyName(key));
			log.debug(key + " remove from redis cache");
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public Object get(Object key) {
		if (null == key) {
			return null;
		}
		Object object = null;
		try {
			byte[] bytes = RedisFactory.getInstance().hgetAsync(region2, getKeyName(key));
			if (bytes != null) {
				object = SerializationUtils.unserialize(bytes);
			}
		} catch (Exception ex) {
			log.error("Error occured when get data from redis cache", ex);
		}
		return object;
	}

	/**
	 * 生成缓存的 key
	 * 
	 * @param key
	 * @return
	 */
	public byte[] getKeyName(Object key) {
		if (key instanceof Number) {
			return ("I:" + key).getBytes();
		} else if (key instanceof String || key instanceof StringBuffer || key instanceof StringBuilder) {
			return ("S:" + key).getBytes();
		}
		return ("O:" + key).getBytes();
	}

	/**
	 * 在region里增加一个可选的层级,作为命名空间,使结构更加清晰 同时满足小型应用,多个cache共享一个redis database的场景
	 * 
	 * @param region
	 * @return
	 */
	private String getRegionName(String region) {
		String nameSpace = SystemConfig.getRegionName(region);
		if (StringUtils.isNotEmpty(nameSpace)) {
			region = nameSpace + ":" + region;
		}
		return region;
	}

	public String getString(String key) {
		if (null == key) {
			return null;
		}
		String str = null;
		try {
			byte[] bytes = RedisFactory.getInstance().hgetAsync(region2, getKeyName(key));
			if (bytes != null) {
				str = (String) SerializationUtils.unserialize(bytes);
			}
		} catch (Exception ex) {
			log.error("Error occured when get data from redis cache", ex);
		}
		return str;
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

	public void put(Object key, Object value) {
		if (value == null) {
			evict(key);
		} else {
			try {
				RedisFactory.getInstance().hsetAsync(region2, getKeyName(key), SerializationUtils.serialize(value));
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	public void put(String key, String value) {
		if (value == null) {
			evict(key);
		} else {
			try {
				RedisFactory.getInstance().hsetAsync(region2, getKeyName(key), SerializationUtils.serialize(value));
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	public void remove(String key) {
		this.evict(key);
	}

	public void update(Object key, Object value) {
		put(key, value);
	}
}
