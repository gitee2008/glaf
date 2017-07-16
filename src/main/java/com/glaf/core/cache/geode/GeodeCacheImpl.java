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

package com.glaf.core.cache.geode;

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

import com.glaf.core.cache.Cache;
import com.glaf.core.cache.CacheException;
import com.glaf.core.config.SystemProperties;
import com.glaf.core.util.FileUtils;
import com.glaf.core.util.PropertiesUtils;

public class GeodeCacheImpl implements Cache {
	private final static Logger logger = LoggerFactory.getLogger(GeodeCacheImpl.class);

	protected static ConcurrentMap<String, Region<String, String>> regionConcurrentMap = new ConcurrentHashMap<String, Region<String, String>>();

	protected static volatile ClientCache cache;

	public GeodeCacheImpl() {

	}

	public void clear(String regionName) {
		this.getRegion(regionName).clear();
	}

	public String get(String regionName, String key) {
		logger.debug("region:" + regionName + "\tkey:" + key);
		String object = null;
		try {
			object = this.getRegion(regionName).get(key);
			logger.debug("object:" + object);
		} catch (Exception ex) {
			// ex.printStackTrace();
			logger.error("get object error from geode", ex);
		}
		return object;
	}

	public ClientCache getClientCache() {
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

	private int getProperty(Properties props, String key, int defaultValue) {
		try {
			return Integer.parseInt(props.getProperty(key, String.valueOf(defaultValue)).trim());
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public Region<String, String> getRegion(String regionName) {
		if (regionConcurrentMap.get(regionName) != null) {
			return regionConcurrentMap.get(regionName);
		}
		Region<String, String> region = getClientCache()
				.<String, String>createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY).create(regionName);
		regionConcurrentMap.put(regionName, region);
		return region;
	}

	public List<?> keys() throws CacheException {
		return null;
	}

	public void put(String regionName, String key, String value) {
		this.getRegion(regionName).put(key, value);
	}

	public void remove(String regionName, String key) {
		this.getRegion(regionName).remove(key);
	}

	public void shutdown() {
		if (!getClientCache().isClosed()) {
			getClientCache().close();
		}
	}

}
