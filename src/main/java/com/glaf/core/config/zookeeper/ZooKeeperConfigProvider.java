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
package com.glaf.core.config.zookeeper;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.glaf.core.config.Config;
import com.glaf.core.config.ConfigProvider;

public class ZooKeeperConfigProvider implements ConfigProvider {

	protected final ConcurrentMap<String, Config> concurrentMap = new ConcurrentHashMap<String, Config>();

	public ZooKeeperConfigProvider() {

	}

	public Config buildConfig(String regionName, boolean autoCreate) {
		Config config = concurrentMap.get(regionName);
		if (config == null) {
			config = new ZooKeeperConfig(regionName);
			final Config currentConfig = concurrentMap.putIfAbsent(regionName, config);
			if (currentConfig != null) {
				config = currentConfig;
			}
		}
		return config;
	}

	public String name() {
		return "zookeeper";
	}

	public void start(Properties props) {

	}

	public void stop() {

	}
}
