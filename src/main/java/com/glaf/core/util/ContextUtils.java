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

package com.glaf.core.util;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.core.config.Environment;

public class ContextUtils {
	protected static final Log logger = LogFactory.getLog(ContextUtils.class);

	protected static ConcurrentMap<String, AtomicInteger> accessCounter = new ConcurrentHashMap<String, AtomicInteger>();

	protected static ConcurrentMap<String, Long> accessDenyMap = new ConcurrentHashMap<String, Long>();

	protected static ConcurrentMap<Object, Object> dataMap = new ConcurrentHashMap<Object, Object>();

	public static void addDenyAccess(String userId) {
		if (!accessDenyMap.containsKey(userId)) {
			accessDenyMap.put(userId, System.currentTimeMillis());
		}
	}

	public static void clear() {
		dataMap.clear();
	}

	public static boolean contains(Object key) {
		String sys_name = Environment.getCurrentSystemName();
		if (sys_name != null) {
			String cacheKey = sys_name + "_" + key;
			if (dataMap.get(cacheKey) != null) {
				return true;
			}
		}
		if (dataMap.get(key) != null) {
			return true;
		}
		return false;
	}

	public static boolean denyAccess(String userId) {
		boolean accessDeny = false;
		if (accessDenyMap.containsKey(userId)) {
			long ts = accessDenyMap.get(userId);
			if ((System.currentTimeMillis() - ts) < DateUtils.MINUTE * 30) {
				accessDeny = true;
			}
		}
		return accessDeny;
	}

	public static Object get(Object key) {
		String sys_name = Environment.getCurrentSystemName();
		if (sys_name != null) {
			String cacheKey = sys_name + "_" + key;
			if (dataMap.containsKey(cacheKey)) {
				return dataMap.get(cacheKey);
			}
		}
		if (dataMap.containsKey(key)) {
			return dataMap.get(key);
		}
		return null;
	}

	public static Map<String, Long> getAccessDenyMap() {
		return accessDenyMap;
	}

	public static String getContextPath() {
		return (String) get("__contextPath__");
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

	public static void put(Object key, Object value) {
		String sys_name = Environment.getCurrentSystemName();
		if (key != null && value != null) {
			String cacheKey = sys_name + "_" + key;
			dataMap.put(cacheKey, value);
			dataMap.put(key, value);
		}
	}

	public static void remove(Object key) {
		String sys_name = Environment.getCurrentSystemName();
		if (key != null) {
			String cacheKey = sys_name + "_" + key;
			dataMap.remove(cacheKey);
			dataMap.remove(key);
		}
	}

	public static void removeDenyAccess(String userId) {
		accessDenyMap.remove(userId);
	}

	public static void reset() {
		accessCounter.clear();
		accessDenyMap.clear();
	}

	public static void setContextPath(String contextPath) {
		put("__contextPath__", contextPath);
	}

	private ContextUtils() {

	}
}