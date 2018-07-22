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

package com.glaf.base.servlet;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.glaf.core.access.domain.AccessLog;
import com.glaf.core.access.factory.AccessLogFactory;
import com.glaf.core.config.Environment;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.context.ThreadHolderFactory;
import com.glaf.core.id.MyBatisDbIdGenerator;
import com.glaf.core.util.ContextUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ThreadContextHolder;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.base.modules.sys.model.SysUser;
import com.glaf.base.utils.Authentication;
import com.glaf.base.utils.ContextUtil;
import com.glaf.base.utils.RequestUtil;

public class SpringDispatcherServlet extends DispatcherServlet {

	protected static final Log logger = LogFactory.getLog(SpringDispatcherServlet.class);

	protected static final Semaphore semaphore = new Semaphore(50);

	private static final long serialVersionUID = 1L;

	public SpringDispatcherServlet() {
		super();
	}

	@Override
	protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long start = System.currentTimeMillis();
		String uri = request.getRequestURI();
		String ipAddr = RequestUtils.getIPAddress(request);
		String contextPath = request.getContextPath();
		String requestURI = request.getRequestURI();
		String resPath = requestURI.substring(contextPath.length(), requestURI.length());

		if (!StringUtils.startsWith(ipAddr, "192.168.")) {
			int num = ContextUtil.increase(ipAddr);
			if (num > 200000) {// 从某个IP地址来的访问量超过200000就重定向
				response.sendRedirect("http://www.example.com");
				return;
			}
		}

		JSONObject json = new JSONObject();
		json.put("start", start);
		json.put("url", uri);

		String actorId = null;
		try {
			// String systemName = RequestUtils.getCurrentSystem(request);
			// if (systemName != null) {
			// Environment.setCurrentSystemName(systemName);
			// json.put("systemName", systemName);
			// }

			actorId = RequestUtils.getActorId(request);
			if (actorId != null) {
				// logger.debug("actorId:" + actorId);
				Authentication.setAuthenticatedAccount(actorId);
				json.put("actorId", actorId);
			}

			if (actorId != null && ContextUtils.denyAccess(actorId)) {
				if (!(StringUtils.equals(actorId, "admin"))) {
					// 如果登录用户恶意刷新，直接重定向
					response.sendRedirect("http://www.example.com");
					return;
				}
			}

			SysUser user = RequestUtil.getLoginUser(request);
			if (user != null) {
				actorId = user.getActorId();
				json.put("actorId", user.getActorId());
				json.put("username", user.getName());
				Authentication.setAuthenticatedUser(user);
				com.glaf.core.security.Authentication.setAuthenticatedActorId(user.getActorId());
			} else {
				/**
				 * 取不到用户会话信息，跳转到登录页
				 */
				if (!(StringUtils.contains(uri, "/login") || StringUtils.contains(uri, "/static/"))) {
					logger.debug("->uri:" + uri);
					response.sendRedirect(request.getContextPath() + "/login");
					return;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		ThreadContextHolder.setHttpRequest(request);
		ThreadContextHolder.setHttpResponse(response);
		ThreadContextHolder.setServletContext(request.getServletContext());
		try {
			if (!StringUtils.startsWith(ipAddr, "192.168.")) {
				if (SystemConfig.getConcurrentAccessLimit()) {
					logger.debug("启用并发访问控制,可用许可数:" + semaphore.availablePermits());
					if (semaphore.availablePermits() == 0) {
						try {
							TimeUnit.MILLISECONDS.sleep(50 + new Random().nextInt(1000));
						} catch (InterruptedException e) {
						}
					}
					semaphore.acquire();
				}
			}
			if (StringUtils.contains(uri, "/rs/")) {
				request.setCharacterEncoding("UTF-8");
				response.setCharacterEncoding("UTF-8");
			}
			RequestUtils.setCurrentUser(request, response, Environment.getCurrentSystemName(), actorId);
			super.doService(request, response);
		} finally {
			if (!StringUtils.startsWith(ipAddr, "192.168.")) {
				if (SystemConfig.getConcurrentAccessLimit()) {
					semaphore.release();
				}
			}
			Environment.setCurrentSystemName(null);
			Environment.removeCurrentSystemName();
			ThreadContextHolder.clear();
			Authentication.clear();
			SystemConfig.clear();
			MyBatisDbIdGenerator.clear();
			com.glaf.core.jdbc.ConnectionThreadHolder.closeAndClear();
			com.glaf.core.security.Authentication.clear();
			ThreadHolderFactory.getInstance().closeAndClear();
			long end = System.currentTimeMillis();
			long time = end - start;
			json.put("end", end);
			json.put("time", time);
			logger.debug(uri + " process time(ms):" + time);
			logger.debug(json.toJSONString());

			if (SystemConfig.getBoolean("enableAccessLog") && !StringUtils.contains(uri, "/login")) {
				if (StringUtils.contains(uri, "/sys/")) {
					java.util.Enumeration<String> e = request.getParameterNames();
					while (e.hasMoreElements()) {
						String name = e.nextElement();
						String value = request.getParameter(name);
						if (StringUtils.isNotEmpty(value)) {
							json.put(name, value);
						} else {
							String[] values = request.getParameterValues(name);
							if (values != null && values.length > 0) {
								JSONArray array = new JSONArray();
								for (String v : values) {
									array.add(v);
								}
								json.put(name, array);
							}
						}
					}
					AccessLog accessLog = new AccessLog();
					accessLog.setIp(RequestUtils.getIPAddress(request));
					accessLog.setTimeMillis((int) time);
					accessLog.setAccessTime(new Date());
					accessLog.setUri(resPath);
					accessLog.setUserId(actorId);
					accessLog.setMethod(request.getMethod());
					accessLog.setContent(json.toJSONString());
					AccessLogFactory.getInstance().addLog(accessLog);
				} else if (StringUtils.contains(uri, "/heathcare/") || StringUtils.contains(uri, "/tableData")) {
					AccessLog accessLog = new AccessLog();
					accessLog.setIp(RequestUtils.getIPAddress(request));
					accessLog.setTimeMillis((int) time);
					accessLog.setAccessTime(new Date());
					accessLog.setUri(resPath);
					accessLog.setUserId(actorId);
					accessLog.setMethod(request.getMethod());
					accessLog.setContent(json.toJSONString());
					AccessLogFactory.getInstance().addLog(accessLog);
				}
			}
		}
	}

	@Override
	protected WebApplicationContext initWebApplicationContext() {
		WebApplicationContext wac = super.initWebApplicationContext();
		return wac;
	}

}