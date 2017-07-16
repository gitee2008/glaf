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

package com.glaf.shiro.cache.zookeeper;

import java.util.Collection;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.util.SerializationUtils;

import com.glaf.core.config.SystemConfig;
import com.glaf.core.factory.ZooKeeperFactory;

public class ZooKeeperCache<K, V> implements Cache<K, V> {

	private static final Log logger = LogFactory.getLog(ZooKeeperCache.class);

	private volatile String groupName;

	public ZooKeeperCache(String groupName) {
		if (groupName == null) {
			throw new IllegalArgumentException("groupName argument cannot be null.");
		}
		this.groupName = groupName;
	}

	public String getSessionPrefix() {
		if (groupName == null) {
			groupName = "/shiro_session";
		}
		groupName = groupName + "/" + SystemConfig.getRegionName("shiro");
		return groupName;
	}

	public void clear() throws CacheException {

	}

	@SuppressWarnings("unchecked")
	public V get(K key) throws CacheException {
		try {
			return (V) ZooKeeperFactory.getInstance().getObject(getSessionPrefix(), key);
		} catch (Exception ex) {
			logger.error("Error occured when get data from zookeeper", ex);
		}
		return null;
	}

	public Set<K> keys() {
		return null;
	}

	public V put(K key, V value) throws CacheException {
		try {
			byte[] data = SerializationUtils.serialize(value);
			ZooKeeperFactory.getInstance().put(getSessionPrefix(), key, data);
		} catch (Exception ex) {
			logger.error("Error occured when put data into zookeeper", ex);
		}
		return null;
	}

	public V remove(K key) throws CacheException {
		try {
			ZooKeeperFactory.getInstance().remove(getSessionPrefix(), key.toString());
		} catch (Exception ex) {
			logger.error("Error occured when remove data from zookeeper", ex);
		}
		return null;
	}

	public int size() {
		return 0;
	}

	public Collection<V> values() {
		return null;
	}

}
