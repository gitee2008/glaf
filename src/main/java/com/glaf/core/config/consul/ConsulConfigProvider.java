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

package com.glaf.core.config.consul;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.ConsulRawClient;
import com.glaf.core.config.Config;
import com.glaf.core.config.ConfigProvider;

public class ConsulConfigProvider implements ConfigProvider {

	protected final ConcurrentMap<String, Config> concurrentMap = new ConcurrentHashMap<String, Config>();

	protected String host = "localhost";

	protected int port = 8500;

	@Override
	public Config buildConfig(String regionName, boolean autoCreate) {
		Config config = concurrentMap.get(regionName);
		if (config == null) {
			ConsulRawClient client = new ConsulRawClient(this.getHost(), this.getPort());
			ConsulClient consul = new ConsulClient(client);
			config = new ConsulConfig(consul);
			final Config currentConfig = concurrentMap.put(regionName, config);
			if (currentConfig != null) {
				config = currentConfig;
			}
		}
		return config;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	private int getProperty(Properties props, String key, int defaultValue) {
		try {
			return Integer.parseInt(props.getProperty(key, String.valueOf(defaultValue)).trim());
		} catch (Exception e) {
			return defaultValue;
		}
	}

	private String getProperty(Properties props, String key, String defaultValue) {
		return props.getProperty(key, defaultValue).trim();
	}

	@Override
	public String name() {
		return "consul";
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public void start(Properties props) {
		if (props != null && !props.isEmpty()) {
			host = this.getProperty(props, "host", "localhost");
			port = this.getProperty(props, "port", 8500);
		}
	}

	@Override
	public void stop() {

	}

}
