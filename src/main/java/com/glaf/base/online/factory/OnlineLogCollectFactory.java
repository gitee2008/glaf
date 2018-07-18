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

package com.glaf.base.online.factory;

import java.util.List;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.base.online.domain.UserOnlineLog;
import com.glaf.base.online.service.UserOnlineLogService;

import com.glaf.core.config.Environment;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.domain.Database;
import com.glaf.core.domain.TableDefinition;
import com.glaf.core.identity.User;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.util.DBUtils;

public class OnlineLogCollectFactory {
	private static class OnlineLogCollectHolder {
		public static OnlineLogCollectFactory instance = new OnlineLogCollectFactory();
	}

	public class RefreshTask implements Runnable {

		protected boolean success = false;
		protected int retry = 0;

		public void run() {
			if (!dataList.isEmpty()) {
				logger.debug("准备入库数据......");
				List<UserOnlineLog> new_dataList = new CopyOnWriteArrayList<UserOnlineLog>();
				new_dataList.addAll(dataList);
				dataList.clear();
				retry = 0;
				success = false;
				while (retry < 3 && !success) {
					try {
						retry++;
						Environment.setCurrentSystemName(Environment.DEFAULT_SYSTEM_NAME);
						getUserOnlineLogService().saveAll(new_dataList);
						if (!StringUtils.equals(systemName, Environment.DEFAULT_SYSTEM_NAME)) {
							Environment.setCurrentSystemName(systemName);
							getUserOnlineLogService().saveAll(new_dataList);
						}
						success = true;
						new_dataList.clear();
					} catch (Exception ex) {
						logger.error(ex);
						try {
							TimeUnit.MILLISECONDS.sleep(new Random().nextInt(50));// 随机等待
						} catch (InterruptedException e) {
						}
					}
				}
			}
		}
	}

	public class RemoveTask implements Runnable {

		public void run() {
			try {
				logger.debug("准备删除历史数据......");

			} catch (Exception ex) {
				logger.error(ex);
			}

			try {
				List<User> users = com.glaf.core.security.IdentityFactory.getUsers();
				if (users != null && !users.isEmpty()) {
					for (User user : users) {
						userMap.put(user.getActorId(), user.getName());
					}
				}
			} catch (Exception ex) {
				logger.error(ex);
			}
		}

	}

	protected static final Log logger = LogFactory.getLog(OnlineLogCollectFactory.class);

	protected static volatile String systemName = Environment.DEFAULT_SYSTEM_NAME;

	protected static volatile TableDefinition tableDefinition = null;

	protected static volatile IDatabaseService databaseService = null;

	protected static volatile UserOnlineLogService userOnlineLogService = null;

	protected static ConcurrentMap<String, String> userMap = new ConcurrentHashMap<String, String>();

	protected static List<UserOnlineLog> dataList = new CopyOnWriteArrayList<UserOnlineLog>();

	public static void checkAndCreateDB() {
		Database database = getDatabaseService().getDatabaseByMapping("log");
		if (database != null) {
			systemName = database.getName();
		}

		if (!DBUtils.tableExists(systemName, "USER_ONLINE_LOG")) {
			TableDefinition table = new TableDefinition();
			table.setTableName("USER_ONLINE_LOG");
			List<ColumnDefinition> columns = DBUtils.getColumnDefinitions("USER_ONLINE_LOG");
			if (columns != null && !columns.isEmpty()) {
				for (ColumnDefinition column : columns) {
					if (column.isPrimaryKey()) {
						table.setIdColumn(column);
						columns.remove(column);
						break;
					}
					if (StringUtils.equalsIgnoreCase(column.getColumnName(), "ID_")) {
						column.setPrimaryKey(true);
						table.setIdColumn(column);
						columns.remove(column);
						break;
					}
				}
				table.setColumns(columns);
				DBUtils.createTable(systemName, table);
			}
		}
	}

	public static IDatabaseService getDatabaseService() {
		if (databaseService == null) {
			databaseService = ContextFactory.getBean("databaseService");
		}
		return databaseService;
	}

	public static OnlineLogCollectFactory getInstance() {
		return OnlineLogCollectHolder.instance;
	}

	public static String getSystemName() {
		return systemName;
	}

	public static ConcurrentMap<String, String> getUserMap() {
		if (userMap.isEmpty()) {
			try {
				List<User> users = com.glaf.core.security.IdentityFactory.getUsers();
				if (users != null && !users.isEmpty()) {
					for (User user : users) {
						userMap.put(user.getActorId(), user.getName());
					}
				}
			} catch (Exception ex) {
				logger.error(ex);
			}
		}
		return userMap;
	}

	public static UserOnlineLogService getUserOnlineLogService() {
		if (userOnlineLogService == null) {
			userOnlineLogService = ContextFactory.getBean("userOnlineLogService");
		}
		return userOnlineLogService;
	}

	protected ScheduledExecutorService scheduledThreadPool = Executors.newSingleThreadScheduledExecutor();

	protected ScheduledExecutorService scheduledThreadPool2 = Executors.newSingleThreadScheduledExecutor();

	private OnlineLogCollectFactory() {
		try {
			checkAndCreateDB();
		} catch (Exception ex) {
			logger.error(ex);
		}
		try {
			List<User> users = com.glaf.core.security.IdentityFactory.getUsers();
			if (users != null && !users.isEmpty()) {
				for (User user : users) {
					userMap.put(user.getActorId(), user.getName());
				}
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
		startScheduler();
	}

	public void collectData(String userId, UserOnlineLog data) {
		dataList.add(data);
	}

	public void startScheduler() {
		RefreshTask command = new RefreshTask();
		scheduledThreadPool.scheduleAtFixedRate(command, 1, 5, TimeUnit.SECONDS);// 每5秒入库一次采集的数据

		RemoveTask command2 = new RemoveTask();
		scheduledThreadPool2.scheduleAtFixedRate(command2, 1, 60, TimeUnit.MINUTES);// 每60分钟执行一次删除历史数据
	}

}