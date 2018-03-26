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

package com.glaf.core.cache;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.glaf.core.config.ConfigFactory;
import com.glaf.core.factory.TableFactory;
import com.glaf.core.job.BaseJob;
import com.glaf.core.util.DateUtils;

public class ClearCacheJob extends BaseJob {
	protected final static Log logger = LogFactory.getLog(ClearCacheJob.class);

	private static final int MAX_AVAILABLE = 1;
	// 将信号量初始化为 1，使得它在使用时最多只有一个可用的许可，从而可用作一个相互排斥的锁。
	private final Semaphore semaphore = new Semaphore(MAX_AVAILABLE, true);

	protected static AtomicLong lastExecuteTime = new AtomicLong(System.currentTimeMillis());

	public ClearCacheJob() {

	}

	public void clearAll() {
		try {
			semaphore.acquire();
			logger.debug("start clear cache...");
			TableFactory.clear();
			CacheFactory.clearAll();
			ConfigFactory.clearAll();
			logger.debug("end clear cache.");
		} catch (Exception ex) {
			logger.debug(ex);
		} finally {
			semaphore.release();
		}
	}

	public void runJob(JobExecutionContext context) throws JobExecutionException {
		if ((System.currentTimeMillis() - lastExecuteTime.get()) < DateUtils.MINUTE * 2) {
			return;
		}
		try {
			TimeUnit.SECONDS.sleep(5 + new Random().nextInt(20));// 随机，避免在集群环境下同时清理导致数据库瞬间过载。
		} catch (InterruptedException e) {
		}
		logger.debug("taskId:" + context.getJobDetail().getJobDataMap().getString("taskId"));
		this.clearAll();
		lastExecuteTime.set(System.currentTimeMillis());
		logger.debug("jobRunTime:" + context.getJobRunTime());
		logger.debug("nextFireTime:" + DateUtils.getDateTime(context.getNextFireTime()));
	}
}