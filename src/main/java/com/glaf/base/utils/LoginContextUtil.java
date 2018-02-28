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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.glaf.base.modules.sys.service.SysUserService;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.util.RequestUtils;

public class LoginContextUtil {
	private static LoginContextUtil instance = new LoginContextUtil();
	private static volatile ConcurrentMap<String, String> dataMap = new ConcurrentHashMap<String, String>();

	public static String get(String key) {
		if (dataMap.containsKey(key)) {
			return dataMap.get(key);
		}
		return null;
	}

	public static String getContextPath() {
		return (String) dataMap.get("__contextPath__");
	}

	public static String getIndexUrl(HttpServletRequest request) {
		String actorId = RequestUtils.getActorId(request);
		SysUserService sysUserService = ContextFactory.getBean("sysUserService");
		String indexUrl = sysUserService.getUserIndexUrl(actorId);
		if (StringUtils.equals(actorId, "admin")) {
			indexUrl = "/sys/tenant";
		}
		if (StringUtils.isEmpty(indexUrl)) {
			indexUrl = SystemConfig.getString("index_portal_link", "/static/html/main.html");
		}
		return indexUrl;
	}

	public static LoginContextUtil getInstance() {
		return instance;
	}

	public static void put(String key, String value) {
		if (key != null && value != null) {
			dataMap.put(key, value);
		}
	}

	public static void setContextPath(String contextPath) {
		dataMap.put("__contextPath__", contextPath);
	}

	private LoginContextUtil() {

	}
}