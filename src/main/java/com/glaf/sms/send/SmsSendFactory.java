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

import org.apache.commons.codec.binary.StringUtils;

import com.glaf.core.context.ContextFactory;
import com.glaf.sms.domain.SmsServer;
import com.glaf.sms.domain.SmsVerifyMessage;
import com.glaf.sms.service.SmsServerService;

public class SmsSendFactory {
	protected static SmsServerService smsServerService;

	protected static SmsSender aliyunSender = new AliyunSmsSender();

	protected static SmsSender customSender = new CustomSmsSender();

	public static SmsServerService getSmsServerService() {
		if (smsServerService == null) {
			smsServerService = ContextFactory.getBean("com.glaf.sms.service.smsServerService");
		}
		return smsServerService;
	}

	public static void sendSms(String serverId, SmsVerifyMessage message) {
		SmsServer server = getSmsServerService().getSmsServer(serverId);
		if (server != null) {
			if (StringUtils.equals(server.getProvider(), "aliyun")) {
				aliyunSender.sendSms(server, message);
			} else {
				customSender.sendSms(server, message);
			}
		}
	}

	private SmsSendFactory() {

	}

}
