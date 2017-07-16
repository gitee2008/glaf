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

import java.util.Enumeration;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class MailProperties {
	private static volatile Properties properties = new Properties();

	protected static AtomicBoolean loading = new AtomicBoolean(false);

	static {
		reload();
	}

	public static boolean getBoolean(String key) {
		if (hasObject(key)) {
			String value = properties.getProperty(key);
			return Boolean.valueOf(value).booleanValue();
		}
		return false;
	}

	public static boolean getBoolean(String key, boolean defaultValue) {
		if (hasObject(key)) {
			String value = properties.getProperty(key);
			return Boolean.valueOf(value).booleanValue();
		}
		return defaultValue;
	}

	public static double getDouble(String key) {
		if (hasObject(key)) {
			String value = properties.getProperty(key);
			return Double.parseDouble(value);
		}
		return 0;
	}

	public static int getInt(String key) {
		if (hasObject(key)) {
			String value = properties.getProperty(key);
			return Integer.parseInt(value);
		}
		return 0;
	}

	public static long getLong(String key) {
		if (hasObject(key)) {
			String value = properties.getProperty(key);
			return Long.parseLong(value);
		}
		return 0;
	}

	public static Properties getProperties() {
		Properties p = new Properties();
		Enumeration<?> e = properties.keys();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			String value = properties.getProperty(key);
			p.put(key, value);
		}
		return p;
	}

	public static String getString(String key) {
		if (hasObject(key)) {
			String value = properties.getProperty(key);
			return value;
		}
		return null;
	}

	public static String getString(String key, String defaultValue) {
		if (hasObject(key)) {
			String value = properties.getProperty(key);
			return value;
		}
		return defaultValue;
	}

	public static boolean hasObject(String key) {
		String value = properties.getProperty(key);
		if (value != null) {
			return true;
		}
		return false;
	}

	public static void reload() {
		if (!loading.get()) {
			try {
				loading.set(true);
				Properties props = GlobalConfig.getProperties("sys_mail");
				if (props == null || props.isEmpty()) {
					props = GlobalConfig.getConfigProperties("mail.properties");
				}
				if (props != null) {
					Enumeration<?> e = props.keys();
					while (e.hasMoreElements()) {
						String key = (String) e.nextElement();
						String value = props.getProperty(key);
						properties.setProperty(key, value);
						properties.setProperty(key.toLowerCase(), value);
						properties.setProperty(key.toUpperCase(), value);
					}
				}
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			} finally {
				loading.set(false);
			}
		}
	}

	private MailProperties() {

	}

}