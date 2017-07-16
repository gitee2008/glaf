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

package com.glaf.core.web;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.core.exceptions.AthenticationException;
import com.glaf.core.identity.User;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.SignUtils;

public class AuthHelper {
	protected static final Log logger = LogFactory.getLog(AuthHelper.class);

	protected static ConcurrentMap<String, Long> authMap = new ConcurrentHashMap<String, Long>();

	public void checkToken(HttpServletRequest request, HttpServletResponse response) {
		// logger.debug(RequestUtils.getParameterMap(request));
		String userId = request.getParameter("userId");
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String ip = RequestUtils.getIPAddress(request);

		if (StringUtils.isNotEmpty(userId)) {
			String cacheKey = DigestUtils.sha1Hex(ip + "_" + userId);
			if (authMap.get(cacheKey) != null) {
				long start = authMap.get(cacheKey);
				if ((System.currentTimeMillis() - start) < DateUtils.MINUTE * 5) {
					logger.info(ip + "->" + userId + " athentication ok.");
					// 验证通过
					return;
				}
			}
			User user = IdentityFactory.getUser(userId);
			logger.debug(user);
			if (user != null && StringUtils.isNotEmpty(user.getToken()) && StringUtils.isNotEmpty(signature)
					&& StringUtils.isNotEmpty(timestamp) && StringUtils.isNotEmpty(nonce)
					&& SignUtils.checkToken(user.getToken(), signature, timestamp, nonce)) {
				logger.info(ip + "->" + userId + " athentication success.");
				authMap.put(cacheKey, System.currentTimeMillis());
				// 验证通过
				return;
			}
		}
		logger.error(ip + "->" + userId + " athentication failed.");
		throw new AthenticationException("athentication failed");
	}

}
