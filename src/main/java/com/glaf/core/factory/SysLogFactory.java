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

package com.glaf.core.factory;

import java.sql.Connection;
import java.sql.Statement;
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
import com.glaf.core.config.DatabaseConnectionConfig;
import com.glaf.core.config.Environment;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.domain.Database;
import com.glaf.core.domain.SysDataLog;
import com.glaf.core.domain.TableDefinition;
import com.glaf.core.domain.util.SysDataLogTableUtils;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.logging.LogStorage;
import com.glaf.core.security.Authentication;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.service.SysDataLogService;
import com.glaf.core.util.ClassUtils;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.JdbcUtils;

public class SysLogFactory {

	public class SaveLogTask implements Runnable {

		public void run() {
			saveAll();
		}

	}

	private static class SysLogHolder {
		public static SysLogFactory instance = new SysLogFactory();
	}

	protected final static Log logger = LogFactory.getLog(SysLogFactory.class);

	protected static ConcurrentLinkedQueue<SysDataLog> dataLogs = new ConcurrentLinkedQueue<SysDataLog>();

	protected static ConcurrentMap<String, String> tables = new ConcurrentHashMap<String, String>();

	protected static volatile String systemName = null;

	protected static volatile Configuration conf = BaseConfiguration.create();

	protected static volatile SysDataLogService sysDataLogService;

	public static void checkLogTable(String systemName, String tableName) {
		String key = systemName + "_" + tableName;
		key = key.toLowerCase();
		if (!tables.containsKey(key)) {
			TableDefinition tableDefinition = SysDataLogTableUtils.getTableDefinition(tableName);
			try {
				if (!DBUtils.tableExists(systemName, tableName)) {
					DBUtils.createTable(systemName, tableDefinition);
					tables.put(key, tableName);
				} else {
					tables.put(key, tableName);
				}
			} catch (Exception ex) {
				logger.error(ex);
			}
		}
	}

	public static SysLogFactory getInstance() {
		return SysLogHolder.instance;
	}

	public static SysDataLogService getSysDataLogService() {
		if (sysDataLogService == null) {
			sysDataLogService = ContextFactory.getBean("sysDataLogService");
		}
		return sysDataLogService;
	}

	public static void loadTables(String systemName) {
		if (StringUtils.isNotEmpty(systemName)) {
			List<String> tablenames = DBUtils.getTables(systemName);
			if (tables != null && !tablenames.isEmpty()) {
				for (String table : tablenames) {
					String key = systemName + "_" + table;
					key = key.toLowerCase();
					tables.put(key, table);
				}
			}
		}
	}

	protected ScheduledExecutorService scheduledThreadPool = Executors.newSingleThreadScheduledExecutor();

	protected long lastUpdate = System.currentTimeMillis();

	private SysLogFactory() {
		try {
			IDatabaseService databaseService = (IDatabaseService) ContextFactory.getBean("databaseService");
			Database database = databaseService.getDatabaseByMapping("log");
			if (database != null) {
				DatabaseConnectionConfig cfg = new DatabaseConnectionConfig();
				if (cfg.checkConnectionImmediately(database)) {
					systemName = database.getName();
				}
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
		if (StringUtils.isNotEmpty(systemName)) {
			try {
				loadTables(systemName);
			} catch (Exception ex) {
				logger.error(ex);
			}
		}
	}

	public void addLog(SysDataLog sysLog) {
		if (StringUtils.isNotEmpty(systemName)) {
			sysLog.setCreateTime(new Date());
			sysLog.setSuffix("_" + DateUtils.getNowYearMonthDay());
			if (sysLog.getActorId() == null) {
				sysLog.setActorId(Authentication.getAuthenticatedActorId());
			}
			try {
				dataLogs.offer(sysLog);
				if (StringUtils.isNotEmpty(sysLog.getModuleId())) {
					String tableName = "LOG_" + sysLog.getModuleId();
					checkLogTable(systemName, tableName);
				}
			} catch (Exception ex) {
			}
		}
	}

	public void checkAndCreateLogDB() {
		IDatabaseService databaseService = (IDatabaseService) ContextFactory.getBean("databaseService");
		Database database = databaseService.getDatabaseByMapping("log");
		if (database == null) {// 不存在日志库
			Database master = databaseService.getDatabaseByMapping("master");
			if (master != null && "Y".equals(master.getVerify())) {
				Database logDB = master.clone();

				logDB.setMapping("log");
				logDB.setSection("LOG");
				logDB.setActive("1");
				logDB.setInitFlag("Y");
				logDB.setDiscriminator("L");
				logDB.setLevel(3);
				logDB.setUseType("LOG");
				logDB.setRunType("INST");
				logDB.setTitle("日志库");
				logDB.setDbname(logDB.getDbname() + "_LOG");
				logDB.setKey(master.getKey());
				logDB.setUser(master.getUser());
				logDB.setPassword(master.getPassword());
				logDB.setId(0);
				DatabaseConnectionConfig cfg = new DatabaseConnectionConfig();
				Connection conn = null;
				Statement stmt = null;
				boolean checkOK = false;
				try {
					conn = cfg.getConnection(logDB);
					if (conn != null) {
						checkOK = true;
					}
				} catch (Exception ex) {
					logger.error(ex);
				} finally {
					JdbcUtils.close(stmt);
					JdbcUtils.close(conn);
				}
				try {
					if (!checkOK) {
						conn = cfg.getConnection(master);
						String dbType = DBConnectionFactory.getDatabaseType(conn);
						logger.debug("dbType:" + dbType);
						if (!StringUtils.equals(DBUtils.POSTGRESQL, dbType)) {
							conn.setAutoCommit(false);
						} else {
							conn.setAutoCommit(true);
						}
						if (StringUtils.equals(DBUtils.POSTGRESQL, dbType)) {
							logDB.setDbname(logDB.getDbname().toLowerCase());
						}
						stmt = conn.createStatement();
						stmt.executeUpdate(" CREATE DATABASE " + logDB.getDbname());
						if (!StringUtils.equals(DBUtils.POSTGRESQL, dbType)) {
							conn.commit();
						}
						JdbcUtils.close(stmt);
						JdbcUtils.close(conn);

						logDB.setActive("1");
						databaseService.insert(logDB);

					}
				} catch (Exception ex) {
					logger.error(ex);
				} finally {
					JdbcUtils.close(stmt);
					JdbcUtils.close(conn);
				}
			}
		}
	}

	/**
	 * 获取某个模块最新的指定条数的日志列表
	 * 
	 * @param moduleId
	 * @param offset
	 * @param limit
	 * @return
	 */
	public List<SysDataLog> getLatestLogs(String moduleId, int offset, int limit) {
		String className = conf.get("log_storage_strategy_class");
		if (StringUtils.isNotEmpty(className)) {
			Class<?> clazz = ClassUtils.classForName(className);
			Object object = BeanUtils.instantiateClass(clazz);
			if (object instanceof LogStorage) {
				LogStorage storage = (LogStorage) object;
				return storage.getLatestLogs(moduleId, offset, limit);
			}
		}
		if (StringUtils.isNotEmpty(systemName)) {
			String currentName = Environment.getCurrentSystemName();
			try {
				Environment.setCurrentSystemName(systemName);
				return getSysDataLogService().getLatestLogs(moduleId, offset, limit);
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			} finally {
				Environment.setCurrentSystemName(currentName);
			}
		}
		return null;
	}

	/**
	 * 获取某个模块日志总数
	 * 
	 * @param moduleId
	 * @return
	 */
	public int getTotal(String moduleId) {
		String className = conf.get("log_storage_strategy_class");
		if (StringUtils.isNotEmpty(className)) {
			Class<?> clazz = ClassUtils.classForName(className);
			Object object = BeanUtils.instantiateClass(clazz);
			if (object instanceof LogStorage) {
				LogStorage storage = (LogStorage) object;
				return storage.getTotal(moduleId);
			}
		}
		if (StringUtils.isNotEmpty(systemName)) {
			String currentName = Environment.getCurrentSystemName();
			try {
				Environment.setCurrentSystemName(systemName);
				return getSysDataLogService().getTotal(moduleId);
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			} finally {
				Environment.setCurrentSystemName(currentName);
			}
		}
		return 0;
	}

	public void saveAll() {
		if (dataLogs.isEmpty()) {
			return;
		}
		if (StringUtils.isNotEmpty(systemName)) {
			logger.debug("dataLogs.size:" + dataLogs.size());
			logger.debug("strategy:" + conf.get("log_storage_strategy"));
			String className = conf.get("log_storage_strategy_class");
			if (StringUtils.isNotEmpty(className)) {
				Class<?> clazz = ClassUtils.classForName(className);
				Object object = BeanUtils.instantiateClass(clazz);
				if (object instanceof LogStorage) {
					LogStorage storage = (LogStorage) object;
					List<SysDataLog> rows = new ArrayList<SysDataLog>();
					while (!dataLogs.isEmpty()) {
						SysDataLog bean = dataLogs.poll();
						rows.add(bean);
					}
					storage.saveLogs(rows);
				}
			} else {
				int retry = 0;
				boolean success = false;
				String currentName = Environment.getCurrentSystemName();
				List<SysDataLog> rows = new ArrayList<SysDataLog>();
				while (!dataLogs.isEmpty()) {
					SysDataLog bean = dataLogs.poll();
					rows.add(bean);
					if (rows.size() > 0 && rows.size() % 100 == 0) {
						if (bean.getModuleId() != null) {
							String tableName = "LOG_" + bean.getModuleId();
							checkLogTable(systemName, tableName);
							SysDataLog model = new SysDataLog();
							BeanUtils.copyProperties(bean, model);
							model.setSuffix("_" + bean.getModuleId());
							rows.add(model);
						}
						success = false;
						retry = 0;
						while (retry < 3 && !success) {
							try {
								retry++;
								String tableName = "LOG_" + DateUtils.getNowYearMonthDay();
								checkLogTable(systemName, tableName);
								Environment.setCurrentSystemName(systemName);
								getSysDataLogService().saveLogs(rows);
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
							String tableName = "LOG_" + DateUtils.getNowYearMonthDay();
							checkLogTable(systemName, tableName);
							Environment.setCurrentSystemName(systemName);
							getSysDataLogService().saveLogs(rows);
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
				logger.debug("->dataLogs.size:" + dataLogs.size());
			}
		}
	}

	public void startScheduler() {
		SaveLogTask command = new SaveLogTask();
		scheduledThreadPool.scheduleAtFixedRate(command, 10, conf.getInt("log_storage_delay", 30), TimeUnit.SECONDS);
	}

}
