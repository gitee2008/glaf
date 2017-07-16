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

package com.glaf.base.callback;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.core.web.callback.LoginCallback;

public class LoginCallbackThread extends Thread {

	private static final Log logger = LogFactory.getLog(LoginCallbackThread.class);

	protected String actorId;
	protected LoginCallback loginCallback;
	protected HttpServletRequest request;
	protected HttpServletResponse response;

	public LoginCallbackThread(LoginCallback loginCallback, String actorId, HttpServletRequest request,
			HttpServletResponse response) {
		this.loginCallback = loginCallback;
		this.actorId = actorId;
		this.request = request;
		this.response = response;
	}

	public void run() {
		logger.debug("-------------------------callback--------------------");
		logger.debug(loginCallback.getClass().getName());
		loginCallback.afterLogin(actorId, request, response);
	}

}
