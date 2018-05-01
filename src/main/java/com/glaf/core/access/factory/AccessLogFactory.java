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

package com.glaf.core.access.factory;

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

import com.glaf.core.access.domain.AccessLog;
import com.glaf.core.access.domain.AccessTotal;
import com.glaf.core.access.handler.AccessLogStorage;
import com.glaf.core.access.query.AccessLogQuery;
import com.glaf.core.access.service.AccessLogService;
import com.glaf.core.access.util.AccessLogDomainFactory;
import com.glaf.core.config.BaseConfiguration;
import com.glaf.core.config.Configuration;
import com.glaf.core.config.Environment;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.domain.Database;
import com.glaf.core.domain.TableDefinition;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.util.ClassUtils;
import com.glaf.core.util.DBUtils;

public class AccessLogFactory {

	private static class AccessLogHolder {
		public static AccessLogFactory instance = new AccessLogFactory();
	}

	public class SaveLogTask implements Runnable {

		public void run() {
			saveAll();
		}

	}

	protected final static Log logger = LogFactory.getLog(AccessLogFactory.class);

	protected static ConcurrentLinkedQueue<AccessLog> accessLogs = new ConcurrentLinkedQueue<AccessLog>();

	protected static ConcurrentMap<String, String> tables = new ConcurrentHashMap<String, String>();

	protected static ConcurrentMap<String, Long> uriMap = new ConcurrentHashMap<String, Long>();

	protected static ConcurrentMap<String, Integer> accessMap = new ConcurrentHashMap<String, Integer>();

	protected static volatile String systemName = Environment.DEFAULT_SYSTEM_NAME;

	protected static volatile Configuration conf = BaseConfiguration.create();

	protected static volatile AccessLogService accessLogService;

	public static void checkLogTable(String systemName, String tableName) {
		String key = systemName + tableName;
		key = key.toLowerCase();
		if (!tables.containsKey(key)) {
			TableDefinition tableDefinition = AccessLogDomainFactory.getTableDefinition(tableName);
			try {
				if (!DBUtils.tableExists(systemName, tableName)) {
					DBUtils.createTable(systemName, tableDefinition);
					tables.put(key, tableName);
				} else {
					tables.put(key, tableName);
				}
			} catch (Exception ex) {

			}
		}
	}

	public static AccessLogService getAccessLogService() {
		if (accessLogService == null) {
			accessLogService = ContextFactory.getBean("accessLogService");
		}
		return accessLogService;
	}

	public static AccessLogFactory getInstance() {
		return AccessLogHolder.instance;
	}

	public static void loadTables(String systemName) {
		List<String> tablenames = DBUtils.getTables(systemName);
		if (tables != null && !tablenames.isEmpty()) {
			for (String table : tablenames) {
				String key = systemName + table;
				key = key.toLowerCase();
				tables.put(key, table);
			}
		}
	}

	protected ScheduledExecutorService scheduledThreadPool = Executors.newSingleThreadScheduledExecutor();

	protected long lastUpdate = System.currentTimeMillis();

	private AccessLogFactory() {
		try {
			IDatabaseService databaseService = (IDatabaseService) ContextFactory.getBean("databaseService");
			Database database = databaseService.getDatabaseByMapping("log");
			if (database != null) {
				systemName = database.getName();
				checkLogTable(systemName, AccessLogDomainFactory.TABLENAME);
			}
		} catch (Exception ex) {

		}
		try {
			loadTables(systemName);
		} catch (Exception ex) {

		}
	}

	public void addLog(AccessLog log) {
		log.setAccessTime(new Date());
		String uri = log.getUri();
		if (uri.indexOf("?") != -1) {
			uri = uri.substring(0, uri.indexOf("?"));
		}
		log.setUri(uri);
		try {
			accessLogs.offer(log);
		} catch (Exception ex) {
		}
	}

	/**
	 * 根据条件统计某天的访问数量
	 * 
	 * @param query
	 * @return
	 */
	public List<AccessTotal> getAccessTotal(AccessLogQuery query) {
		String className = conf.get("access_log_storage_strategy_class");
		if (StringUtils.isNotEmpty(className)) {
			Class<?> clazz = ClassUtils.classForName(className);
			Object object = BeanUtils.instantiateClass(clazz);
			if (object instanceof AccessLogStorage) {
				AccessLogStorage storage = (AccessLogStorage) object;
				return storage.getAccessTotal(query);
			}
		}
		String currentName = Environment.getCurrentSystemName();
		try {
			Environment.setCurrentSystemName(systemName);
			return getAccessLogService().getAccessTotal(query);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			Environment.setCurrentSystemName(currentName);
		}
	}

	/**
	 * 获取某个uri最新的指定条数的日志列表
	 * 
	 * @param uri
	 * @param offset
	 * @param limit
	 * @return
	 */
	public List<AccessLog> getLatestLogs(String uri, int offset, int limit) {
		String className = conf.get("access_log_storage_strategy_class");
		if (StringUtils.isNotEmpty(className)) {
			Class<?> clazz = ClassUtils.classForName(className);
			Object object = BeanUtils.instantiateClass(clazz);
			if (object instanceof AccessLogStorage) {
				AccessLogStorage storage = (AccessLogStorage) object;
				return storage.getLatestLogs(uri, offset, limit);
			}
		}
		String currentName = Environment.getCurrentSystemName();
		try {
			Environment.setCurrentSystemName(systemName);
			return getAccessLogService().getLatestLogs(uri, offset, limit);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			Environment.setCurrentSystemName(currentName);
		}
	}

	/**
	 * 获取某个模块日志总数
	 * 
	 * @param moduleId
	 * @return
	 */
	public int getTotal(String uri) {
		String className = conf.get("access_log_storage_strategy_class");
		if (StringUtils.isNotEmpty(className)) {
			Class<?> clazz = ClassUtils.classForName(className);
			Object object = BeanUtils.instantiateClass(clazz);
			if (object instanceof AccessLogStorage) {
				AccessLogStorage storage = (AccessLogStorage) object;
				return storage.getTotal(uri);
			}
		}
		String currentName = Environment.getCurrentSystemName();
		try {
			Environment.setCurrentSystemName(systemName);
			return getAccessLogService().getTotal(uri);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			Environment.setCurrentSystemName(currentName);
		}
	}

	public void saveAll() {
		if (accessLogs.isEmpty()) {
			return;
		}
		logger.debug("accessLogs.size:" + accessLogs.size());
		logger.debug("strategy:" + conf.get("access_log_storage_strategy"));
		String className = conf.get("access_log_storage_strategy_class");
		if (StringUtils.isNotEmpty(className)) {
			Class<?> clazz = ClassUtils.classForName(className);
			Object object = BeanUtils.instantiateClass(clazz);
			if (object instanceof AccessLogStorage) {
				AccessLogStorage storage = (AccessLogStorage) object;
				List<AccessLog> rows = new ArrayList<AccessLog>();
				while (!accessLogs.isEmpty()) {
					AccessLog bean = accessLogs.poll();
					rows.add(bean);
				}
				storage.saveLogs(rows);
			}
		} else {
			int retry = 0;
			boolean success = false;
			String currentName = Environment.getCurrentSystemName();
			List<AccessLog> rows = new ArrayList<AccessLog>();
			while (!accessLogs.isEmpty()) {
				AccessLog bean = accessLogs.poll();
				rows.add(bean);
				if (rows.size() > 0 && rows.size() % 2000 == 0) {
					success = false;
					retry = 0;
					while (retry < 3 && !success) {
						try {
							retry++;
							Environment.setCurrentSystemName(systemName);
							getAccessLogService().bulkInsert(rows);
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
						Environment.setCurrentSystemName(systemName);
						getAccessLogService().bulkInsert(rows);
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
			//logger.debug("->accessLogs.size:" + accessLogs.size());
		}
	}

	public void startScheduler() {
		SaveLogTask command = new SaveLogTask();
		scheduledThreadPool.scheduleAtFixedRate(command, 30, conf.getInt("access_log_storage_delay", 60),
				TimeUnit.SECONDS);
	}

}
