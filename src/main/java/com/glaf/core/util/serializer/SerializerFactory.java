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

package com.glaf.core.util.serializer;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glaf.core.config.CustomProperties;

/**
 * 管理器
 */
public class SerializerFactory {

	protected final static Logger log = LoggerFactory.getLogger(SerializerFactory.class);

	private static String serializer;

	public final static String getSerializer() {
		return serializer;
	}

	public static void init() {
		try {
			Properties props = CustomProperties.getProperties();
			SerializerFactory.serializer = props.getProperty("serialization");
		} catch (Exception e) {
			throw new RuntimeException("Unabled to initialize properties", e);
		}
	}

	private SerializerFactory() {
		try {
			init();
		} catch (Exception ex) {
			log.error("SerializerFactory init error", ex);
		}
	}

}
