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

package com.glaf.matrix.transform.job;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.domain.Database;
import com.glaf.core.job.BaseJob;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.util.DateUtils;
import com.glaf.matrix.data.domain.ExecutionLog;
import com.glaf.matrix.data.util.ExecutionLogFactory;
import com.glaf.matrix.data.util.ExecutionUtils;
import com.glaf.matrix.transform.domain.TableTransform;
import com.glaf.matrix.transform.query.TableTransformQuery;
import com.glaf.matrix.transform.service.TableTransformService;
import com.glaf.matrix.transform.task.TableTransformTask;

public class TableTransformJob extends BaseJob {

	protected static AtomicLong lastExecuteTime = new AtomicLong(System.currentTimeMillis());

	protected static int F_STEP = 0;

	public void runJob(JobExecutionContext context) throws JobExecutionException {
		logger.info("-----------------------TableTransformJob-------------------------");
		if (F_STEP > 0) {
			if ((System.currentTimeMillis() - lastExecuteTime.get()) < DateUtils.MINUTE * 5) {
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
		TableTransformService tableTransformService = ContextFactory.getBean("tableTransformService");
		TableTransformQuery query = new TableTransformQuery();
		query.deleteFlag(0);
		query.locked(0);
		tableTransformService.resetAllTableTransformStatus();
		List<TableTransform> list = tableTransformService.list(query);
		if (list != null && !list.isEmpty()) {
			int errorCount = 0;
			String jobNo = null;
			Database database = null;
			StringTokenizer token = null;
			List<ExecutionLog> logs = null;
			TableTransformTask task = null;
			int runDay = DateUtils.getNowYearMonthDay();
			for (TableTransform tableTransform : list) {
				if (StringUtils.isNotEmpty(tableTransform.getDatabaseIds())) {
					errorCount = 0;
					try {
						token = new StringTokenizer(tableTransform.getDatabaseIds(), ",");
						while (token.hasMoreTokens()) {
							String x = token.nextToken();
							if (StringUtils.isNotEmpty(x) && StringUtils.isNumeric(x)) {
								jobNo = "table_transform_" + tableTransform.getTransformId() + "_" + x + "_"
										+ runDay;
								if (!ExecutionUtils.contains(jobNo)) {
									try {
										/**
										 * 判断是否可以执行
										 */
										logs = ExecutionLogFactory.getInstance().getTodayExecutionLogs(
												"matrix_table_transform", tableTransform.getTransformId());
										if (!ExecutionLogFactory.getInstance().canExecution(logs, jobNo)) {
											continue;
										}
										database = databaseService.getDatabaseById(Long.parseLong(x));
										if (database != null) {
											ExecutionUtils.put(jobNo, new Date());
											task = new TableTransformTask(database.getId(),
													tableTransform.getTransformId(), jobNo, null);
											if (!task.execute()) {
												errorCount++;
											}
										}
									} catch (Exception ex) {
										errorCount++;
										logger.error(ex);
									} finally {
										ExecutionUtils.remove(jobNo);
									}
								}
							}
						}
						if (errorCount == 0) {
							tableTransform.setTransformStatus(1);
						} else {
							tableTransform.setTransformStatus(-1);
						}
						tableTransformService.updateTableTransformStatus(tableTransform);
					} catch (Exception ex) {
						logger.error(ex);
					}
				}
			}
		}
		lastExecuteTime.set(System.currentTimeMillis());
	}

}
