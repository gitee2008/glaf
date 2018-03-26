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

package com.glaf.matrix.combination.job;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.domain.Database;
import com.glaf.core.job.BaseJob;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.StringTools;
import com.glaf.matrix.data.domain.ExecutionLog;
import com.glaf.matrix.data.util.ExecutionLogFactory;
import com.glaf.matrix.data.util.ExecutionUtils;
import com.glaf.matrix.combination.domain.CombinationApp;
import com.glaf.matrix.combination.domain.CombinationHistory;
import com.glaf.matrix.combination.query.CombinationAppQuery;
import com.glaf.matrix.combination.service.CombinationAppService;
import com.glaf.matrix.combination.service.CombinationHistoryService;
import com.glaf.matrix.combination.task.CombinationAppTask;
import com.glaf.matrix.util.SysParams;

public class CombinationAppJob extends BaseJob {

	protected static AtomicLong lastExecuteTime = new AtomicLong(System.currentTimeMillis());
	
	protected static int F_STEP = 0;

	public void runJob(JobExecutionContext context) throws JobExecutionException {
		logger.info("-----------------------CombinationAppJob-------------------------");
		if (F_STEP > 0) {
			if ((System.currentTimeMillis() - lastExecuteTime.get()) < DateUtils.SECOND * 30) {
				logger.info("间隔时间未到，不执行。");
				return;
			}
		}
		try {
			TimeUnit.SECONDS.sleep(1 + new Random().nextInt(30));// 随机等待，避免Job同时执行
		} catch (InterruptedException e) {
		}
		F_STEP++;
		IDatabaseService databaseService = ContextFactory.getBean("databaseService");
		CombinationAppService combinationAppService = ContextFactory
				.getBean("com.glaf.matrix.combination.service.combinationAppService");
		CombinationHistoryService combinationHistoryService = ContextFactory
				.getBean("com.glaf.matrix.combination.service.combinationHistoryService");
		CombinationAppQuery query = new CombinationAppQuery();
		query.autoSyncFlag("Y");
		query.active("Y");

		List<CombinationApp> rows = combinationAppService.list(query);
		if (rows != null && !rows.isEmpty()) {
			Map<String, Object> parameter = new HashMap<String, Object>();
			SysParams.putInternalParams(parameter);
			int runDay = DateUtils.getNowYearMonthDay();
			long ts = System.currentTimeMillis();
			CombinationAppTask task = null;
			List<ExecutionLog> logs = null;
			Database database = null;
			String jobNo = null;
			for (CombinationApp app : rows) {
				if (StringUtils.isNotEmpty(app.getTargetDatabaseIds())) {
					List<Long> targetDatabaseIds = StringTools.splitToLong(app.getTargetDatabaseIds(), ",");
					for (long targetDatabaseId : targetDatabaseIds) {
						jobNo = "combination_app_" + app.getId() + "_" + targetDatabaseId + "_" + runDay;
						if (!ExecutionUtils.contains(jobNo)) {
							try {
								CombinationHistory last = combinationHistoryService
										.getLatestCombinationHistory(app.getId(), targetDatabaseId);
								if (last != null && last.getStatus() == 1) {
									/**
									 * 判断是否满足时间间隔
									 */
									if ((ts - last.getCreateTime().getTime()) < app.getInterval() * 60 * 1000) {
										continue;
									}
								}
								long start = System.currentTimeMillis();
								/**
								 * 判断是否可以执行
								 */
								logs = ExecutionLogFactory.getInstance().getTodayExecutionLogs("combination_app",
										String.valueOf(app.getId()));
								if (!ExecutionLogFactory.getInstance().canExecution(logs, jobNo)) {
									continue;
								}

								database = databaseService.getDatabaseById(targetDatabaseId);
								if (database != null) {
									ExecutionUtils.put(jobNo, new Date());
									task = new CombinationAppTask(app.getSrcDatabaseId(), targetDatabaseId, app.getId(),
											jobNo, parameter);
									CombinationHistory his = new CombinationHistory();
									his.setCreateBy("system");
									his.setDatabaseId(targetDatabaseId);
									his.setDeploymentId(app.getDeploymentId());
									his.setSyncId(app.getId());
									if (!task.execute()) {
										his.setStatus(-1);
									} else {
										his.setStatus(1);
									}
									his.setTotalTime((int) (System.currentTimeMillis() - start));
									his.setDatabaseName(database.getTitle() + "[" + database.getHost() + "]");
									combinationHistoryService.save(his);
								}
							} catch (Exception ex) {
								logger.error(ex);
							} finally {
								ExecutionUtils.remove(jobNo);
							}
						}
					}
				}
			}
		}
		lastExecuteTime.set(System.currentTimeMillis());
	}

}
