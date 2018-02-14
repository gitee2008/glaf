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
import com.glaf.matrix.data.domain.ExecutionLog;
import com.glaf.matrix.handler.ExecutionLogStorage;
import com.glaf.matrix.data.service.ExecutionLogService;

public class ExecutionLogFactory {

	private static class ExecutionLogHolder {
		public static ExecutionLogFactory instance = new ExecutionLogFactory();
	}

	public class SaveLogTask implements Runnable {

		public void run() {
			saveAll();
		}

	}

	protected static final Log logger = LogFactory.getLog(ExecutionLogFactory.class);

	protected static ConcurrentMap<String, String> tables = new ConcurrentHashMap<String, String>();

	protected static ConcurrentLinkedQueue<ExecutionLog> executionLogs = new ConcurrentLinkedQueue<ExecutionLog>();

	protected static volatile Configuration conf = BaseConfiguration.create();

	protected static volatile ExecutionLogService executionLogService;

	protected static volatile String systemName = Environment.DEFAULT_SYSTEM_NAME;

	public static void checkLogTable(String systemName, String tableName) {
		String key = systemName + "_" + tableName;
		key = key.toLowerCase();
		if (!tables.containsKey(key)) {
			TableDefinition tableDefinition = ExecutionLogDomainFactory.getTableDefinition(tableName);
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

	public static ExecutionLogService getExecutionLogService() {
		if (executionLogService == null) {
			executionLogService = ContextFactory.getBean("executionLogService");
		}
		return executionLogService;
	}

	public static ExecutionLogFactory getInstance() {
		return ExecutionLogHolder.instance;
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

	private ExecutionLogFactory() {
		if (!startScheduler) {
			this.startScheduler();
		}
		try {
			IDatabaseService databaseService = (IDatabaseService) ContextFactory.getBean("databaseService");
			Database database = databaseService.getDatabaseByMapping("log");
			if (database != null) {
				systemName = database.getName();
				checkLogTable(systemName, ExecutionLogDomainFactory.TABLENAME);
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

	public void addLog(ExecutionLog log) {
		log.setCreateTime(new Date());
		try {
			executionLogs.offer(log);
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
	public boolean canExecution(List<ExecutionLog> logs, String jobNo) {
		boolean canExecution = true;
		if (logs != null && !logs.isEmpty()) {
			for (ExecutionLog log : logs) {
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
	public List<ExecutionLog> getLatestLogs(String type, Date dateAfter, int offset, int limit) {
		String className = conf.get("execution_log_storage_strategy_class");
		if (StringUtils.isNotEmpty(className)) {
			Class<?> clazz = ClassUtils.classForName(className);
			Object object = BeanUtils.instantiateClass(clazz);
			if (object instanceof ExecutionLogStorage) {
				ExecutionLogStorage storage = (ExecutionLogStorage) object;
				return storage.getLatestLogs(type, dateAfter, offset, limit);
			}
		}
		String currentName = Environment.getCurrentSystemName();
		try {
			Environment.setCurrentSystemName(systemName);
			return getExecutionLogService().getLatestLogs(type, dateAfter, offset, limit);
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
	public List<ExecutionLog> getTodayExecutionLogs(String type, String businessKey) {
		try {
			return getExecutionLogService().getTodayExecutionLogs(type, businessKey);
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
		String className = conf.get("execution_log_storage_strategy_class");
		if (StringUtils.isNotEmpty(className)) {
			Class<?> clazz = ClassUtils.classForName(className);
			Object object = BeanUtils.instantiateClass(clazz);
			if (object instanceof ExecutionLogStorage) {
				ExecutionLogStorage storage = (ExecutionLogStorage) object;
				return storage.getTotal(type, dateAfter);
			}
		}
		String currentName = Environment.getCurrentSystemName();
		try {
			Environment.setCurrentSystemName(systemName);
			return getExecutionLogService().getTotal(type, dateAfter);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			Environment.setCurrentSystemName(currentName);
		}
	}

	public void saveAll() {
		if (executionLogs.isEmpty()) {
			return;
		}
		logger.debug("executionLogs.size:" + executionLogs.size());
		logger.debug("strategy:" + conf.get("execution_log_storage_strategy"));
		String className = conf.get("execution_log_storage_strategy_class");
		if (StringUtils.isNotEmpty(className)) {
			Class<?> clazz = ClassUtils.classForName(className);
			Object object = BeanUtils.instantiateClass(clazz);
			if (object instanceof ExecutionLogStorage) {
				ExecutionLogStorage storage = (ExecutionLogStorage) object;
				List<ExecutionLog> rows = new ArrayList<ExecutionLog>();
				while (!executionLogs.isEmpty()) {
					ExecutionLog bean = executionLogs.poll();
					rows.add(bean);
				}
				storage.saveLogs(rows);
				rows.clear();
			}
		} else {
			int retry = 0;
			boolean success = false;
			String currentName = Environment.getCurrentSystemName();
			List<ExecutionLog> rows = new ArrayList<ExecutionLog>();
			while (!executionLogs.isEmpty()) {
				ExecutionLog bean = executionLogs.poll();
				rows.add(bean);
				if (rows.size() > 0 && rows.size() % 200 == 0) {
					success = false;
					retry = 0;
					while (retry < 3 && !success) {
						try {
							retry++;
							getExecutionLogService().bulkInsert(rows);
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
						getExecutionLogService().bulkInsert(rows);
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
			logger.debug("->executionLogs.size:" + executionLogs.size());
		}
	}

	public void startScheduler() {
		SaveLogTask command = new SaveLogTask();
		scheduledThreadPool.scheduleAtFixedRate(command, 15, conf.getInt("execution_log_storage_delay", 60),
				TimeUnit.SECONDS);
		startScheduler = true;
	}
}