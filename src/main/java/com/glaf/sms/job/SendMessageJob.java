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

package com.glaf.sms.job;

import java.io.BufferedWriter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.job.BaseJob;

import com.glaf.sms.domain.SmsClient;
import com.glaf.sms.domain.SmsMessage;
import com.glaf.sms.domain.SmsServer;
import com.glaf.sms.query.SmsClientQuery;
import com.glaf.sms.query.SmsServerQuery;
import com.glaf.sms.service.SmsClientService;
import com.glaf.sms.service.SmsHistoryMessageService;
import com.glaf.sms.service.SmsMessageService;
import com.glaf.sms.service.SmsServerService;

public class SendMessageJob extends BaseJob {
	protected final static Log logger = LogFactory.getLog(SendMessageJob.class);
	protected SmsMessageService smsMessageService;
	protected SmsServerService smsServerService;
	protected SmsClientService smsClientService;
	protected SmsHistoryMessageService smsHistoryMessageService;
	protected static AtomicInteger runNum = new AtomicInteger(0);

	public void runJob(JobExecutionContext context) throws JobExecutionException {

		if (runNum.get() > 0) {
			return;
		}

		smsServerService = ContextFactory.getBean("com.glaf.sms.service.smsServerService");
		smsMessageService = ContextFactory.getBean("com.glaf.sms.service.smsMessageService");

		int count = smsMessageService.getSmsMessageAbleSendCount();
		int sendSize = 1000; // 每次查询发送数量
		List<SmsMessage> list;

		SmsServerQuery serverQuery = new SmsServerQuery();
		serverQuery.setLocked(0);
		List<SmsServer> serverList = smsServerService.list(serverQuery);

		Thread thread = null;
		try {
			for (int i = 0, k = 0; i < count; i += sendSize, k++) {
				list = smsMessageService.getSmsMessageAbleSend();
				if (k >= serverList.size()) {
					k = 0;
				}
				while (runNum.get() > 5) {
					Thread.sleep(1000);
				}

				runNum.incrementAndGet();
				thread = new SendMessageThread(list, serverList.get(k));
				thread.start();
			}
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		} finally {
		}
	}
}

class SendMessageThread extends Thread {
	protected final static Log logger = LogFactory.getLog(SendMessageThread.class);
	protected SmsMessageService smsMessageService;
	protected SmsServerService smsServerService;
	protected SmsHistoryMessageService smsHistoryMessageService;
	protected SmsClientService SmsClientService;
	protected BufferedWriter bufferWriter;
	protected SimpleDateFormat sdf;

	private List<SmsMessage> list;
	private SmsServer smsServer;
	private Map<String, Integer> smsClientNumMap;

	public SendMessageThread(List<SmsMessage> list, SmsServer smsServer) {
		super();
		this.list = list;
		this.smsServer = smsServer;
		smsMessageService = ContextFactory.getBean("com.glaf.sms.service.smsMessageService");
		smsHistoryMessageService = ContextFactory.getBean("com.glaf.sms.service.smsHistoryMessageService");
		SmsClientService = ContextFactory.getBean("com.glaf.sms.service.smsClientService");
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		SmsClientQuery smsClientQuery = new SmsClientQuery();
		smsClientQuery.setLocked(0);
		List<SmsClient> smsClientList = SmsClientService.list(smsClientQuery);
		smsClientNumMap = new HashMap<String, Integer>();
		for (SmsClient smsClient : smsClientList) {
			smsClientNumMap.put(smsClient.getId(), smsClient.getLimit());
		}

	}

	@Override
	public void run() {

		try {
			this.send(list, smsServer);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		SendMessageJob.runNum.decrementAndGet();
	}

	public void send(List<SmsMessage> list, SmsServer smsServer) {

	}
}