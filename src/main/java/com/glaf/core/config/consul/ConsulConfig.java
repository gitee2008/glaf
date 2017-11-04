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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.kv.model.GetValue;
import com.glaf.core.config.Config;

public class ConsulConfig implements Config {
	protected final static Logger logger = LoggerFactory.getLogger(ConsulConfig.class);

	protected ConsulClient consul;

	public ConsulConfig(ConsulClient consul) {
		this.consul = consul;
	}

	@Override
	public void clear() {

	}

	@Override
	public String getString(String key) {
		Response<GetValue> value = consul.getKVValue(key);
		if (value != null && value.getValue() != null) {
			return value.getValue().getValue();
		}
		return null;
	}

	@Override
	public void put(String key, String value) {
		consul.setKVValue(key, value);
		//logger.debug("put value into consul.");
	}

	@Override
	public void remove(String key) {
		consul.deleteKVValue(key);
	}

}
