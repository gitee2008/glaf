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

package com.glaf.j2cache.geode;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.io.IOUtils;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glaf.core.config.SystemProperties;
import com.glaf.core.util.FileUtils;
import com.glaf.core.util.PropertiesUtils;
import com.glaf.j2cache.CacheException;

public class GeodeCache implements com.glaf.j2cache.Cache {
	private final static Logger logger = LoggerFactory.getLogger(GeodeCache.class);

	protected static ConcurrentMap<String, Region<String, Object>> regionConcurrentMap = new ConcurrentHashMap<String, Region<String, Object>>();

	protected static volatile ClientCache cache;

	protected String regionName;

	protected int cacheSize = 50000;

	protected int expireMinutes = 10;

	public GeodeCache(String regionName, int cacheSize, int expireMinutes) {
		this.regionName = regionName;
		this.cacheSize = cacheSize;
		this.expireMinutes = expireMinutes;
	}

	public void clear() throws CacheException {
		this.getRegion(regionName).clear();
	}

	public void destroy() throws CacheException {
		this.getRegion(regionName).clear();
		this.getRegion(regionName).close();
	}

	@SuppressWarnings("rawtypes")
	public void evict(List keys) throws CacheException {
		if (keys != null && !keys.isEmpty()) {
			for (Object key : keys) {
				this.getRegion(regionName).remove(key);
			}
		}
	}

	public void evict(Object key) throws CacheException {
		this.getRegion(regionName).remove(key);
	}

	public Object get(Object key) throws CacheException {
		return this.getRegion(regionName).get(key);
	}

	public ClientCache getCache() {
		if (cache == null) {
			String host = "127.0.0.1";
			int port = 10334;
			String filename = SystemProperties.getConfigRootPath() + "/conf/geode.properties";
			InputStream inStream = null;
			try {
				inStream = FileUtils.getInputStream(filename);
				if (inStream != null) {
					Properties props = PropertiesUtils.loadProperties(inStream);
					if (props != null && !props.isEmpty()) {
						host = props.getProperty("host");
						port = this.getProperty(props, "port", 10334);
					}
				}
			} catch (IOException ex) {
				ex.printStackTrace();
				logger.error("load geode config error", ex);
			} finally {
				IOUtils.closeQuietly(inStream);
			}
			cache = new ClientCacheFactory().addPoolLocator(host, port).create();
		}
		return cache;
	}

	public int getCacheSize() {
		return cacheSize;
	}

	public int getExpireMinutes() {
		return expireMinutes;
	}

	private int getProperty(Properties props, String key, int defaultValue) {
		try {
			return Integer.parseInt(props.getProperty(key, String.valueOf(defaultValue)).trim());
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public Region<String, Object> getRegion(String regionName) {
		if (regionConcurrentMap.get(regionName) != null) {
			return regionConcurrentMap.get(regionName);
		}
		Region<String, Object> region = getCache()
				.<String, Object>createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY).create(regionName);
		regionConcurrentMap.put(regionName, region);
		return region;
	}

	public List<?> keys() throws CacheException {
		return null;
	}

	public void put(Object key, Object value) throws CacheException {
		this.getRegion(regionName).put(key.toString(), value);
	}

	public void setCacheSize(int cacheSize) {
		this.cacheSize = cacheSize;
	}

	public void setExpireMinutes(int expireMinutes) {
		this.expireMinutes = expireMinutes;
	}

	public void update(Object key, Object value) throws CacheException {
		this.getRegion(regionName).put(key.toString(), value);
	}

}
