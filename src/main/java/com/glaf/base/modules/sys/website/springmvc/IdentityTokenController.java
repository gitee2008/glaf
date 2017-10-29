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

package com.glaf.base.modules.sys.website.springmvc;

import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.glaf.base.modules.sys.model.IdentityToken;
import com.glaf.base.modules.sys.service.IdentityTokenService;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.StringTools;
import com.glaf.core.util.UUID32;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/public/token")
@RequestMapping("/public/token")
public class IdentityTokenController {
	protected static final Log logger = LogFactory.getLog(IdentityTokenController.class);

	protected IdentityTokenService identityTokenService;

	public IdentityTokenController() {

	}

	@ResponseBody
	@RequestMapping("/getAccessToken")
	public byte[] getAccessToken(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		IdentityToken identityToken = new IdentityToken();
		try {
			Random random = new Random();
			identityToken.setClientIP(RequestUtils.getIPAddress(request));
			identityToken.setCreateTime(new Date());
			identityToken.setNonce(UUID32.getUUID());
			identityToken.setRand1(StringTools.getRandomString(random.nextInt(50)));
			identityToken.setRand1(StringTools.getRandomString(random.nextInt(50)));
			identityToken.setTimeLive(120);
			identityToken.setTimeMillis(System.currentTimeMillis());
			identityToken.setToken(StringTools.getRandomString(random.nextInt(200)));
			identityToken.setType("ACL");
			this.identityTokenService.save(identityToken);
		} catch (Exception ex) {
			logger.error(ex);
		}
		return json.toJSONString().getBytes();
	}

	@javax.annotation.Resource(name = "identityTokenService")
	public void setIdentityTokenService(IdentityTokenService identityTokenService) {
		this.identityTokenService = identityTokenService;
	}

}
