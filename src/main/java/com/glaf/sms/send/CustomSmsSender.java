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

package com.glaf.sms.send;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.util.StringTools;
import com.glaf.core.util.http.HttpCoreUtils;
import com.glaf.sms.domain.SmsServer;
import com.glaf.sms.domain.SmsVerifyMessage;
import com.glaf.sms.service.SmsServerService;
import com.glaf.sms.service.SmsVerifyMessageService;

public class CustomSmsSender implements SmsSender {
	protected static final Log logger = LogFactory.getLog(CustomSmsSender.class);

	protected SmsServerService smsServerService;

	protected SmsVerifyMessageService smsVerifyMessageService;

	public SmsServerService getSmsServerService() {
		if (smsServerService == null) {
			smsServerService = ContextFactory.getBean("com.glaf.sms.service.smsServerService");
		}
		return smsServerService;
	}

	public void setSmsServerService(SmsServerService smsServerService) {
		this.smsServerService = smsServerService;
	}

	public SmsVerifyMessageService getSmsVerifyMessageService() {
		if (smsVerifyMessageService == null) {
			smsVerifyMessageService = ContextFactory.getBean("com.glaf.sms.service.smsVerifyMessageService");
		}
		return smsVerifyMessageService;
	}

	public void setSmsVerifyMessageService(SmsVerifyMessageService smsVerifyMessageService) {
		this.smsVerifyMessageService = smsVerifyMessageService;
	}

	public void sendSms(SmsServer server, SmsVerifyMessage smsVerifyMessage) {
		//SmsServer server = getSmsServerService().getSmsServer("REG");
		if (server != null) {
			StringBuilder buff = new StringBuilder();
			buff.append(server.getServerIP());
			if (StringUtils.isNotEmpty(server.getPath())) {
				buff.append(server.getPath());
			}
			String requestBody = server.getRequestBody();
			if (StringUtils.isNotEmpty(requestBody)) {
				requestBody = StringTools.replace(requestBody, "#{uuid}", smsVerifyMessage.getId());
				requestBody = StringTools.replace(requestBody, "#{mobile}", smsVerifyMessage.getMobile());
				requestBody = StringTools.replace(requestBody, "#{code}", smsVerifyMessage.getVerificationCode());
				logger.debug("准备发送短信给用户:" + smsVerifyMessage.getMobile());
				logger.debug("发送内容:" + requestBody);
				String text = HttpCoreUtils.doPost(buff.toString(), requestBody);
				logger.debug("响应消息体:" + text);
				if (StringUtils.isNotEmpty(text)) {
					JSONObject jsonObject = JSON.parseObject(text);
					smsVerifyMessage.setSendTime(new java.util.Date());
					smsVerifyMessage.setSendTimeMs(System.currentTimeMillis());
					String responseResult = server.getResponseResult();
					if (StringUtils.isNotEmpty(responseResult)) {
						if (responseResult.indexOf("/") != -1) {
							JSONObject json = jsonObject
									.getJSONObject(responseResult.substring(0, responseResult.indexOf("/")));
							JSONObject child = json.getJSONObject(
									responseResult.substring(responseResult.indexOf("/") + 1, responseResult.length()));
							if (child.getBooleanValue(responseResult.substring(responseResult.indexOf("/") + 1,
									responseResult.length()))) {
								smsVerifyMessage.setStatus(9);
							} else {
								smsVerifyMessage.setStatus(-1);
							}
							getSmsVerifyMessageService().update(smsVerifyMessage);
						} else {
							if (jsonObject.getBooleanValue(responseResult)) {
								smsVerifyMessage.setStatus(9);
							} else {
								smsVerifyMessage.setStatus(-1);
							}
							getSmsVerifyMessageService().update(smsVerifyMessage);
						}
					}
				}
			}
		}
	}

}
