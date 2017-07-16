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
package com.glaf.core.access.job;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.glaf.core.access.factory.AccessLogFactory;
import com.glaf.core.job.BaseJob;
import com.glaf.core.util.DateUtils;

public class AccessLogSaveJob extends BaseJob {
	protected final static Log logger = LogFactory.getLog(AccessLogSaveJob.class);
	
	protected static AtomicLong lastExecuteTime = new AtomicLong(System.currentTimeMillis());

	public void runJob(JobExecutionContext context) throws JobExecutionException {
		if ((System.currentTimeMillis() - lastExecuteTime.get()) < 10000) {
			return;
		}
		String jobName = context.getJobDetail().getKey().getName();
		logger.info("Executing job: " + jobName + " executing at " + DateUtils.getDateTime(new Date()));
		AccessLogFactory.getInstance().saveAll();
		lastExecuteTime.set(System.currentTimeMillis());
	}

}
