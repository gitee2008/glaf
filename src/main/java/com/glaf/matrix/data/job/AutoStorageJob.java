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

package com.glaf.matrix.data.job;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.glaf.core.config.SystemProperties;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.domain.Database;
import com.glaf.core.job.BaseJob;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.FileUtils;
import com.glaf.core.util.StringTools;
import com.glaf.matrix.data.bean.GenaralDBToSQLite;
import com.glaf.matrix.data.domain.StorageApp;
import com.glaf.matrix.data.domain.StorageHistory;
import com.glaf.matrix.data.query.StorageAppQuery;
import com.glaf.matrix.data.service.StorageAppService;
import com.glaf.matrix.data.service.StorageHistoryService;

public class AutoStorageJob extends BaseJob {

	protected static AtomicLong lastExecuteTime = new AtomicLong(System.currentTimeMillis());

	protected static int F_STEP = 0;

	public void runJob(JobExecutionContext context) throws JobExecutionException {
		logger.info("-----------------------AutoStorageJob-------------------------");
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
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		/**
		 * 只在业务活动时间执行
		 */
		if (cal.get(Calendar.HOUR_OF_DAY) < 8 || cal.get(Calendar.HOUR_OF_DAY) > 22) {
			return;
		}

		IDatabaseService databaseService = ContextFactory.getBean("databaseService");
		StorageAppService storageAppService = ContextFactory.getBean("com.glaf.matrix.data.service.storageAppService");
		StorageHistoryService storageHistoryService = ContextFactory
				.getBean("com.glaf.matrix.data.service.storageHistoryService");
		StorageAppQuery query = new StorageAppQuery();
		query.autoSaveFlag("Y");
		long now = System.currentTimeMillis();
		long ts = 0;
		List<StorageApp> list = storageAppService.list(query);
		if (list != null && !list.isEmpty()) {
			String dbpath = null;
			String systemName = null;
			Database database = null;
			StorageHistory storageHistory = null;
			GenaralDBToSQLite bean = new GenaralDBToSQLite();
			String path = SystemProperties.getConfigRootPath() + "/storage";
			try {
				FileUtils.mkdirs(path);
			} catch (IOException e) {
			}

			for (StorageApp storageApp : list) {

				try {
					storageHistory = storageHistoryService.getLatestStorageHistoryByStorageId(storageApp.getId());
					if (storageHistory != null) {
						ts = storageHistory.getCreateTime().getTime() + 1000 * 60 * storageApp.getInterval();
						if (ts > now) {// 未到时间，跳过
							continue;
						}
					}

					int version = storageAppService.incrementVersion(storageApp.getId());

					String sqliteDB = "storage_" + storageApp.getId() + "_" + version + "_"
							+ DateUtils.getNowYearMonthDay() + ".db";

					if (StringUtils.isNotEmpty(storageApp.getTableNames())) {
						List<String> tables = StringTools.split(storageApp.getTableNames());

						if (storageApp.getDatabaseId() > 0) {
							database = databaseService.getDatabaseById(storageApp.getDatabaseId());
						}
						if (database != null) {
							systemName = database.getName();
						} else {
							systemName = com.glaf.core.config.Environment.DEFAULT_SYSTEM_NAME;
						}

						logger.debug("tables:" + tables);
						bean.export(systemName, tables, sqliteDB);

						dbpath = SystemProperties.getConfigRootPath() + "/db/" + sqliteDB;
						FileUtils.copy(dbpath, path + "/" + sqliteDB);

						storageHistory.setId(0);// 新建历史版本
						storageHistory.setCreateBy("system");
						storageHistory.setPath(sqliteDB);
						storageHistory.setSysFlag("AUTO");
						storageHistory.setVersion(version);
						storageHistory.setDeploymentId(storageApp.getDeploymentId());
						storageHistoryService.save(storageHistory);

					}
				} catch (Exception ex) {
					ex.printStackTrace();
					logger.error(ex);
				} finally {
					FileUtils.deleteFile(dbpath);
					try {
						Thread.sleep(5000);
						FileUtils.deleteFile(dbpath);
					} catch (InterruptedException e) {
					}
				}
			}
		}
		lastExecuteTime.set(System.currentTimeMillis());
	}

}
