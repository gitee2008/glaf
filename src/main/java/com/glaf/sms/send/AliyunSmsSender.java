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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.util.ParamUtils;

import com.glaf.sms.domain.SmsServer;
import com.glaf.sms.domain.SmsVerifyMessage;
import com.glaf.sms.service.SmsServerService;
import com.glaf.sms.service.SmsVerifyMessageService;

public class AliyunSmsSender implements SmsSender {
	protected static final Log logger = LogFactory.getLog(AliyunSmsSender.class);

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
		if (server != null) {
			StringBuilder buff = new StringBuilder();
			buff.append(server.getServerIP());
			if (StringUtils.isNotEmpty(server.getPath())) {
				buff.append(server.getPath());
			}
			logger.debug("准备发送短信给用户:" + smsVerifyMessage.getMobile());
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("code", smsVerifyMessage.getVerificationCode());
			params.put("randomCode", smsVerifyMessage.getVerificationCode());
			params.put("phoneNumber", smsVerifyMessage.getMobile());
			params.put("accessKeyId", server.getAccessKeyId());
			params.put("accessKeySecret", server.getAccessKeySecret());
			params.put("signName", server.getSignName());
			params.put("templateCode", server.getTemplateCode());
			Map<String, Object> dataMap = AliyunSmsSenderUtils.send(params);
			if (dataMap != null) {
				int code = ParamUtils.getInt(dataMap, "code");
				smsVerifyMessage.setSendTime(new java.util.Date());
				smsVerifyMessage.setSendTimeMs(System.currentTimeMillis());
				if (code == 200) {// 成功
					smsVerifyMessage.setStatus(9);
				} else {
					smsVerifyMessage.setStatus(-1);
				}
				getSmsVerifyMessageService().update(smsVerifyMessage);
			}
		}
	}

}
