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

package com.glaf.base.online.job;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.glaf.base.online.service.UserOnlineService;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.domain.SystemProperty;
import com.glaf.core.job.BaseJob;
import com.glaf.core.service.ISystemPropertyService;
import com.glaf.core.util.DateUtils;

public class ClearOnlineUserJob extends BaseJob {

	protected final static Log logger = LogFactory.getLog(ClearOnlineUserJob.class);

	protected static AtomicLong lastExecuteTime = new AtomicLong(System.currentTimeMillis());

	public ClearOnlineUserJob() {

	}

	public void runJob(JobExecutionContext context) throws JobExecutionException {
		if ((System.currentTimeMillis() - lastExecuteTime.get()) < DateUtils.MINUTE) {
			return;
		}
		String jobName = context.getJobDetail().getKey().getName();
		logger.info("Executing job: " + jobName + " executing at " + DateUtils.getDateTime(new Date()));
		int timeoutSeconds = 300;
		UserOnlineService userOnlineService = ContextFactory.getBean("userOnlineService");
		ISystemPropertyService systemPropertyService = ContextFactory.getBean("systemPropertyService");
		SystemProperty p = systemPropertyService.getSystemProperty("SYS", "login_time_check");
		if (p != null && p.getValue() != null && StringUtils.isNumeric(p.getValue())) {
			timeoutSeconds = Integer.parseInt(p.getValue());
		}
		if (timeoutSeconds < 60) {
			timeoutSeconds = 60;
		}
		if (timeoutSeconds > 1800) {
			timeoutSeconds = 1800;
		}
		userOnlineService.deleteTimeoutUsers(timeoutSeconds);
		lastExecuteTime.set(System.currentTimeMillis());
	}

}