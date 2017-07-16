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
import java.io.InputStream;
import java.util.Properties;

import com.alibaba.fastjson.util.IOUtils;
import com.glaf.core.config.SystemProperties;
import com.glaf.core.util.FileUtils;

/**
 * 缓存入口
 * 
 * @author oschina.net
 */
public class J2Cache {

	private final static String CONFIG_FILE = "/conf/j2cache/j2cache.properties";
	private final static CacheChannel channel;
	private static Properties config;

	static {
		try {
			config = loadConfig();
			String cache_broadcast = config.getProperty("cache.broadcast");
			if ("redis".equalsIgnoreCase(cache_broadcast)) {
				channel = RedisCacheChannel.getInstance();
			} else if ("jgroups".equalsIgnoreCase(cache_broadcast)) {
				channel = JGroupsCacheChannel.getInstance();
			} else {
				channel = JGroupsCacheChannel.getInstance();
			}
		} catch (IOException e) {
			throw new CacheException("Unabled to load j2cache configuration " + CONFIG_FILE, e);
		}
	}

	public static CacheChannel getChannel() {
		return channel;
	}

	public static Properties getConfig() {
		return config;
	}

	/**
	 * 加载配置
	 * 
	 * @return
	 * @throws IOException
	 */
	static Properties loadConfig() throws IOException {
		Properties props = new Properties();
		InputStream configStream = null;
		try {
			configStream = FileUtils.getInputStream(SystemProperties.getConfigRootPath() + CONFIG_FILE);
			props.load(configStream);
		} finally {
			IOUtils.close(configStream);
		}

		return props;
	}

}
