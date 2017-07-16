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

package com.glaf.shiro.cache.redis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glaf.core.factory.RedisFactory;
import com.glaf.core.util.SerializationUtils;

/**
 * Redis 分布式缓存实现
 * 
 */
public class RedisCache<K, V> implements org.apache.shiro.cache.Cache<K, V> {

	private final static Logger log = LoggerFactory.getLogger(RedisCache.class);

	private String region;

	public RedisCache(String region) {
		this.region = region;
	}

	@Override
	public void clear() {

	}

	@SuppressWarnings("rawtypes")
	public void evict(List keys) {
		if (keys == null || keys.size() == 0) {
			return;
		}
		try {
			String[] newkeys = new String[keys.size()];
			for (int i = 0; i < newkeys.length; i++) {
				newkeys[i] = getKeyName(keys.get(i));
			}
			RedisFactory.getInstance().del(newkeys);
		} catch (Exception ex) {
			log.error("Error occured when remove data from redis", ex);
		}
	}

	public void evict(Object key) {
		try {
			RedisFactory.getInstance().del(getKeyName(key));
		} catch (Exception ex) {
			log.error("Error occured when remove data from redis", ex);
		}
	}

	@SuppressWarnings("unchecked")
	public V get(K key) throws CacheException {
		if (null == key) {
			return null;
		}
		try {
			byte[] raw = RedisFactory.getInstance().getBytes(getKeyName(key).getBytes());
			if (raw != null) {
				return (V) SerializationUtils.unserialize(raw);
			}
		} catch (Exception ex) {
			log.error("Error occured when get data from redis", ex);
		}
		return null;
	}

	public byte[] getData(String key) {
		if (null == key) {
			return null;
		}
		try {
			byte[] raw = RedisFactory.getInstance().getBytes(getKeyName(key).getBytes());
			return raw;
		} catch (Exception ex) {
			log.error("Error occured when get data from redis", ex);
		}
		return null;
	}

	/**
	 * 生成分布式配置的 key
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private String getKeyName(Object key) {
		if (key instanceof Number) {
			return region + ":I:" + key;
		} else {
			Class keyClass = key.getClass();
			if (String.class.equals(keyClass) || StringBuffer.class.equals(keyClass)
					|| StringBuilder.class.equals(keyClass))
				return region + ":S:" + key;
		}
		return region + ":O:" + key;
	}

	@SuppressWarnings("unchecked")
	public Set<K> keys() {
		try {
			Set<String> keys = new HashSet<String>();
			List<String> list = new ArrayList<String>();
			list.addAll(RedisFactory.getInstance().keys(region + ":*"));
			for (int i = 0; i < list.size(); i++) {
				list.set(i, list.get(i).substring(region.length() + 3));
				keys.add(list.get(i));
			}
			return (Set<K>) keys;
		} catch (Exception ex) {
			log.error("Error occured when get keys from redis", ex);
		}
		return null;
	}

	public V put(K key, V value) throws CacheException {
		if (value == null) {
			evict(key);
		} else {
			byte[] name = getKeyName(key).getBytes();
			byte[] data = SerializationUtils.serialize(value);
			try {
				RedisFactory.getInstance().set(name, data);
			} catch (Exception ex) {
				log.error("Error occured when put data into redis", ex);
			}
		}
		return null;
	}

	public void put(String key, byte[] value) {
		if (value == null) {
			evict(key);
		} else {
			try {
				RedisFactory.getInstance().set(getKeyName(key).getBytes(), value);
			} catch (Exception ex) {
				log.error("Error occured when put data into redis", ex);
			}
		}
	}

	public V remove(K key) throws CacheException {
		this.evict(key);
		return null;
	}

	public void remove(String key) {
		this.evict(key);
	}

	public int size() {
		return -1;
	}

	@SuppressWarnings("unchecked")
	public Collection<V> values() {
		try {
			Collection<V> values = new HashSet<V>();
			List<String> list = new ArrayList<String>();
			list.addAll(RedisFactory.getInstance().keys(region + ":*"));
			for (int i = 0; i < list.size(); i++) {
				list.set(i, list.get(i).substring(region.length() + 3));
				String key = list.get(i);
				byte[] raw = RedisFactory.getInstance().getBytes(getKeyName(key).getBytes());
				V value = (V) SerializationUtils.unserialize(raw);
				values.add(value);
			}
			return values;
		} catch (Exception ex) {
			log.error("Error occured when get values from redis", ex);
		}
		return null;
	}

}
