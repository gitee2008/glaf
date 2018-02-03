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

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.zaxxer.hikari.HikariDataSource;

import com.glaf.core.job.BaseJob;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.DateUtils;
import com.glaf.matrix.data.bean.SQLiteHelper;
import com.glaf.matrix.data.bean.TableToSQLite;

public class GenaralDBToSQLiteJob extends BaseJob {

	protected static AtomicLong lastExecuteTime = new AtomicLong(System.currentTimeMillis());

	protected static int F_STEP = 0;

	public void runJob(JobExecutionContext context) throws JobExecutionException {
		logger.info("-----------------------GenaralDBToSQLiteJob-------------------------");
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
		List<String> tables = DBUtils.getTables();
		if (tables != null && !tables.isEmpty()) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			TableToSQLite bean = new TableToSQLite();
			SQLiteHelper sqliteDBHelper = new SQLiteHelper();
			String sqliteDB = "sqlitedb_" + DateUtils.getNowYearMonthDay();
			HikariDataSource targetDataSource = null;
			try {
				targetDataSource = sqliteDBHelper.getDataSource(sqliteDB);
				for (String tableName : tables) {
					if (StringUtils.endsWithIgnoreCase(tableName, "_log")) {
						continue;
					}
					if (StringUtils.equalsIgnoreCase(tableName, "sys_scheduler_execution")) {
						continue;
					}
					logger.debug("准备复制表:" + tableName);
					try {
						bean.execute("default", targetDataSource, tableName, 1000);
						logger.debug("成功复制表:" + tableName);
					} catch (java.lang.Throwable ex) {
						logger.error(ex);
					}
				}
			} catch (Exception ex) {
				logger.error(ex);
			} finally {
				if (targetDataSource != null) {
					targetDataSource.close();
					targetDataSource = null;
				}
			}
		}

		lastExecuteTime.set(System.currentTimeMillis());
	}

}
