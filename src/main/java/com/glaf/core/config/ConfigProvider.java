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

package com.glaf.core.config;

import java.util.Properties;

/**
 * Support for pluggable configs.
 * 
 * 
 */
public interface ConfigProvider {

	/**
	 * 分布式配置的标识名称
	 * 
	 * @return
	 */
	String name();

	/**
	 * Configure the config
	 * 
	 * @param regionName
	 *            the name of the config region
	 * @param autoCreate
	 *            autoCreate settings
	 */
	Config buildConfig(String regionName, boolean autoCreate);

	/**
	 * Callback to perform any necessary initialization of the underlying config
	 * implementation during SessionFactory construction.
	 * 
	 * @param properties
	 *            current configuration settings.
	 */
	void start(Properties props);

	/**
	 * Callback to perform any necessary cleanup of the underlying config
	 * implementation during SessionFactory.close().
	 */
	void stop();

}
