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

package com.glaf.core.resource;

import java.util.Enumeration;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.glaf.core.config.GlobalConfig;
import com.glaf.core.config.SystemConfig;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 分布式配置管理器
 * 
 * 
 */
class ResourceManager {

	protected final static Logger log = LoggerFactory.getLogger(ResourceManager.class);
	protected final static ConcurrentMap<String, Resource> providerCache = new ConcurrentHashMap<String, Resource>();
	protected static volatile Properties props = new Properties();
	protected static ResourceProvider _provider;

	/**
	 * Clear the resource
	 */
	public final static void clear(String region) {
		Resource resource = getResource(region, false);
		if (resource != null) {
			resource.clear();
		}
	}

	/**
	 * 获取分布式配置中的数据
	 * 
	 * @param region
	 * @param key
	 * @return
	 */
	public final static byte[] getData(String region, String key) {
		if (region != null && key != null) {
			Resource resource = getResource(region, false);
			if (resource != null) {
				return resource.getData(key);
			}
		}
		return null;
	}

	private final static ResourceProvider getProviderInstance(String value) throws Exception {
		String className = value;

		if ("redis".equalsIgnoreCase(value)) {
			className = "com.glaf.core.resource.redis.RedisResourceProvider";
		}

		if (StringUtils.isNotEmpty(className)) {
			return (ResourceProvider) Class.forName(className).newInstance();
		}

		return null;
	}

	private final static Properties getProviderProperties(Properties props, ResourceProvider provider) {
		Properties new_props = new Properties();
		Enumeration<Object> keys = props.keys();
		String prefix = provider.name() + '.';
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			if (key.startsWith(prefix))
				new_props.setProperty(key.substring(prefix.length()), props.getProperty(key));
		}
		return new_props;
	}

	private final static Resource getResource(String resource_name, boolean autoCreate) {
		String providerName = SystemConfig.getString("resource.provider");
		// log.debug("providerName:"+providerName);
		if (StringUtils.isNotEmpty(providerName)) {
			if (providerCache.get(providerName) != null) {
				// log.debug("resource provider:"+providerName);
				return providerCache.get(providerName);
			} else {
				try {
					ResourceProvider resourceProvider = getProviderInstance(providerName);
					Resource resource = resourceProvider.buildResource(resource_name, autoCreate);
					providerCache.put(providerName, resource);
					log.debug("->resource provider:" + providerName);
					return resource;
				} catch (Exception ex) {

				}
			}
		}
		return (_provider).buildResource(resource_name, autoCreate);
	}

	public static void initResourceProvider() {
		try {
			props = GlobalConfig.getProperties("sys_resource");
			if (props == null || props.isEmpty()) {
				props = GlobalConfig.getConfigProperties("resource.properties");
			}
			_provider = getProviderInstance(props.getProperty("resource.provider_class"));
			if (_provider != null) {
				_provider.start(getProviderProperties(props, ResourceManager._provider));
				log.info("Using ResourceProvider : " + _provider.getClass().getName());
			}
		} catch (Exception ex) {
			log.error("Unabled to initialize config providers", ex);
		}
	}

	/**
	 * 写入分布式配置
	 * 
	 * @param region
	 * @param key
	 * @param value
	 */
	public final static void put(String region, String key, byte[] value) {
		if (region != null && key != null && value != null) {
			Resource resource = getResource(region, true);
			if (resource != null) {
				resource.put(key, value);
			}
		}
	}

	/**
	 * 清除分布式配置中的某个数据
	 * 
	 * @param region
	 * @param key
	 */
	public final static void remove(String region, String key) {
		if (region != null && key != null) {
			Resource resource = getResource(region, false);
			if (resource != null) {
				resource.remove(key);
			}
		}
	}

	/**
	 * 关闭分布式配置
	 */
	public final static void shutdown() {
		_provider.stop();
	}

}
