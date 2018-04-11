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

package com.glaf.base.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

public class ContextUtil {
	private static ContextUtil instance = new ContextUtil();

	protected static ConcurrentMap<String, AtomicInteger> accessCounter = new ConcurrentHashMap<String, AtomicInteger>();

	protected static ConcurrentMap<String, AtomicInteger> loginCounter = new ConcurrentHashMap<String, AtomicInteger>();

	private static volatile ConcurrentMap<Object, Object> dataMap = new ConcurrentHashMap<Object, Object>();

	public static void clear() {
		accessCounter.clear();
	}

	public static Object get(Object key) {
		if (dataMap.containsKey(key)) {
			return dataMap.get(key);
		}
		return null;
	}

	public static String getContextPath() {
		return (String) dataMap.get("__contextPath__");
	}

	public static ContextUtil getInstance() {
		return instance;
	}

	public static int increase(String ipAddr) {
		if ((StringUtils.equalsIgnoreCase(ipAddr, "localhost") || StringUtils.equalsIgnoreCase(ipAddr, "127.0.0.1"))) {
			return 0;
		}
		String key = DigestUtils.sha1Hex(ipAddr);
		if (accessCounter.get(key) == null) {
			AtomicInteger total = new AtomicInteger(1);
			accessCounter.put(key, total);
			return total.get();
		} else {
			AtomicInteger total = accessCounter.get(key);
			total.incrementAndGet();
			accessCounter.put(key, total);
			return total.get();
		}
	}

	public static int increaseUser(String userId) {
		if ((StringUtils.equalsIgnoreCase(userId, "admin") || StringUtils.startsWithIgnoreCase(userId, "test"))) {
			return 0;
		}
		String key = userId;
		if (loginCounter.get(key) == null) {
			AtomicInteger total = new AtomicInteger(1);
			loginCounter.put(key, total);
			return total.get();
		} else {
			AtomicInteger total = loginCounter.get(key);
			total.incrementAndGet();
			loginCounter.put(key, total);
			return total.get();
		}
	}

	public static void put(Object key, Object value) {
		if (key != null && value != null) {
			dataMap.put(key, value);
		}
	}

	public static void remove(Object key) {
		if (key != null) {
			dataMap.remove(key);
		}
	}

	public static void setContextPath(String contextPath) {
		dataMap.put("__contextPath__", contextPath);
	}

	private ContextUtil() {

	}
}