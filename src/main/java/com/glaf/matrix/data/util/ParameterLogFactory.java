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

package com.glaf.matrix.data.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;

import com.glaf.core.config.BaseConfiguration;
import com.glaf.core.config.Configuration;
import com.glaf.core.config.Environment;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.domain.Database;
import com.glaf.core.domain.TableDefinition;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.util.ClassUtils;
import com.glaf.core.util.DBUtils;
import com.glaf.matrix.data.domain.ParameterLog;
import com.glaf.matrix.handler.ParameterLogStorage;
import com.glaf.matrix.data.service.ParameterLogService;

public class ParameterLogFactory {

	private static class ParameterLogHolder {
		public static ParameterLogFactory instance = new ParameterLogFactory();
	}

	public class SaveParameterLogTask implements Runnable {

		public void run() {
			saveAll();
		}

	}

	protected static final Log logger = LogFactory.getLog(ParameterLogFactory.class);

	protected static ConcurrentMap<String, String> tables = new ConcurrentHashMap<String, String>();

	protected static ConcurrentLinkedQueue<ParameterLog> parameterLogs = new ConcurrentLinkedQueue<ParameterLog>();

	protected static volatile Configuration conf = BaseConfiguration.create();

	protected static volatile ParameterLogService parameterLogService;

	protected static volatile String systemName = Environment.DEFAULT_SYSTEM_NAME;

	public static void checkLogTable(String systemName, String tableName) {
		String key = systemName + "_" + tableName;
		key = key.toLowerCase();
		if (!tables.containsKey(key)) {
			TableDefinition tableDefinition = ParameterLogDomainFactory.getTableDefinition(tableName);
			try {
				if (!DBUtils.tableExists(systemName, tableName)) {
					DBUtils.createTable(systemName, tableDefinition);
					tables.put(key, tableName);
				} else {
					tables.put(key, tableName);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public static ParameterLogService getParameterLogService() {
		if (parameterLogService == null) {
			parameterLogService = ContextFactory.getBean("parameterLogService");
		}
		return parameterLogService;
	}

	public static ParameterLogFactory getInstance() {
		return ParameterLogHolder.instance;
	}

	public static void loadTables(String systemName) {
		List<String> tablenames = DBUtils.getTables(systemName);
		if (tables != null && !tablenames.isEmpty()) {
			for (String table : tablenames) {
				String key = systemName + "_" + table;
				key = key.toLowerCase();
				tables.put(key, table);
			}
		}
	}

	protected volatile boolean startScheduler = false;

	protected ScheduledExecutorService scheduledThreadPool = Executors.newSingleThreadScheduledExecutor();

	protected long lastUpdate = System.currentTimeMillis();

	private ParameterLogFactory() {
		if (!startScheduler) {
			this.startScheduler();
		}
		try {
			IDatabaseService databaseService = (IDatabaseService) ContextFactory.getBean("databaseService");
			Database database = databaseService.getDatabaseByMapping("log");
			if (database != null) {
				systemName = database.getName();
				checkLogTable(systemName, ParameterLogDomainFactory.TABLENAME);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			loadTables(systemName);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void addLog(ParameterLog log) {
		log.setCreateTime(new Date());
		try {
			parameterLogs.offer(log);
		} catch (Exception ex) {
		}
	}

	/**
	 * 判断某个Job是否可以执行其中的操作 如果状态为成功,就不再执行.
	 * 
	 * @param logs
	 * @param jobNo
	 * @return
	 */
	public boolean canExecution(List<ParameterLog> logs, String jobNo) {
		boolean canExecution = true;
		if (logs != null && !logs.isEmpty()) {
			for (ParameterLog log : logs) {
				if (StringUtils.equals(jobNo, log.getJobNo())) {
					if (log.getStatus() == 1) {// 如果状态为成功，就不再执行
						canExecution = false;
						break;
					}
				}
			}
		}
		return canExecution;
	}

	/**
	 * 获取某个类型最新的指定条数的日志列表
	 * 
	 * @param type
	 *            类型
	 * @param dataAfter
	 *            日期
	 * @param offset
	 * @param limit
	 * @return
	 */
	public List<ParameterLog> getLatestLogs(String type, Date dateAfter, int offset, int limit) {
		String className = conf.get("parameter_log_storage_strategy_class");
		if (StringUtils.isNotEmpty(className)) {
			Class<?> clazz = ClassUtils.classForName(className);
			Object object = BeanUtils.instantiateClass(clazz);
			if (object instanceof ParameterLogStorage) {
				ParameterLogStorage storage = (ParameterLogStorage) object;
				return storage.getLatestLogs(type, dateAfter, offset, limit);
			}
		}
		String currentName = Environment.getCurrentSystemName();
		try {
			Environment.setCurrentSystemName(systemName);
			return getParameterLogService().getLatestLogs(type, dateAfter, offset, limit);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			Environment.setCurrentSystemName(currentName);
		}
	}

	/**
	 * 获取某个类型某个实例当天的执行日志
	 * 
	 * @param type
	 * @param businessKey
	 * @return
	 */
	public List<ParameterLog> getTodayParameterLogs(String type, String businessKey) {
		try {
			return getParameterLogService().getTodayParameterLogs(type, businessKey);
		} catch (Exception ex) {
		}
		return null;
	}

	/**
	 * 获取某个类型某个日期之后日志总数
	 * 
	 * @param type
	 *            类型
	 * @param dataAfter
	 *            日期
	 * @return
	 */
	public int getTotal(String type, Date dateAfter) {
		String className = conf.get("parameter_log_storage_strategy_class");
		if (StringUtils.isNotEmpty(className)) {
			Class<?> clazz = ClassUtils.classForName(className);
			Object object = BeanUtils.instantiateClass(clazz);
			if (object instanceof ParameterLogStorage) {
				ParameterLogStorage storage = (ParameterLogStorage) object;
				return storage.getTotal(type, dateAfter);
			}
		}
		String currentName = Environment.getCurrentSystemName();
		try {
			Environment.setCurrentSystemName(systemName);
			return getParameterLogService().getTotal(type, dateAfter);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			Environment.setCurrentSystemName(currentName);
		}
	}

	public void saveAll() {
		if (parameterLogs.isEmpty()) {
			return;
		}
		logger.debug("parameterLogs.size:" + parameterLogs.size());
		logger.debug("strategy:" + conf.get("parameter_log_storage_strategy"));
		String className = conf.get("parameter_log_storage_strategy_class");
		if (StringUtils.isNotEmpty(className)) {
			Class<?> clazz = ClassUtils.classForName(className);
			Object object = BeanUtils.instantiateClass(clazz);
			if (object instanceof ParameterLogStorage) {
				ParameterLogStorage storage = (ParameterLogStorage) object;
				List<ParameterLog> rows = new ArrayList<ParameterLog>();
				while (!parameterLogs.isEmpty()) {
					ParameterLog bean = parameterLogs.poll();
					rows.add(bean);
				}
				storage.saveLogs(rows);
				rows.clear();
			}
		} else {
			int retry = 0;
			boolean success = false;
			String currentName = Environment.getCurrentSystemName();
			List<ParameterLog> rows = new ArrayList<ParameterLog>();
			while (!parameterLogs.isEmpty()) {
				ParameterLog bean = parameterLogs.poll();
				rows.add(bean);
				if (rows.size() > 0 && rows.size() % 200 == 0) {
					success = false;
					retry = 0;
					while (retry < 3 && !success) {
						try {
							retry++;
							getParameterLogService().bulkInsert(rows);
							success = true;
							rows.clear();
							break;
						} catch (Exception ex) {
							try {
								TimeUnit.MILLISECONDS.sleep(200 * retry + new Random().nextInt(1000));// 活锁
							} catch (InterruptedException e) {
							}
						}
					}
					rows.clear();
				}
			}

			if (rows.size() > 0) {
				success = false;
				retry = 0;
				while (retry < 3 && !success) {
					try {
						retry++;
						getParameterLogService().bulkInsert(rows);
						success = true;
						rows.clear();
						break;
					} catch (Exception ex) {
						try {
							TimeUnit.MILLISECONDS.sleep(200 * retry + new Random().nextInt(1000));// 活锁
						} catch (InterruptedException e) {
						}
					}
				}
				rows.clear();
			}
			Environment.setCurrentSystemName(currentName);
			lastUpdate = System.currentTimeMillis();
			logger.debug("->parameterLogs.size:" + parameterLogs.size());
		}
	}

	public void startScheduler() {
		SaveParameterLogTask command = new SaveParameterLogTask();
		scheduledThreadPool.scheduleAtFixedRate(command, 15, conf.getInt("parameter_log_storage_delay", 60),
				TimeUnit.SECONDS);
		startScheduler = true;
	}
}