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
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glaf.core.config.ConnectionTask;
import com.glaf.core.config.DatabaseConnectionConfig;
import com.glaf.core.domain.Database;
import com.glaf.core.domain.TableDefinition;
import com.glaf.core.domain.util.SysKeyDomainFactory;
import com.glaf.core.entity.jpa.EntitySchemaUpdate;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.JdbcUtils;

public class DatabaseFactory {
	private static class DatabaseHolder {
		public static DatabaseFactory instance = new DatabaseFactory();
	}

	protected static List<Database> databases = new CopyOnWriteArrayList<Database>();

	protected static List<Long> fileDatabaseIds = new CopyOnWriteArrayList<Long>();

	protected static List<Database> activeDatabases = new CopyOnWriteArrayList<Database>();

	protected static ConcurrentMap<Long, String> databaseNames = new ConcurrentHashMap<Long, String>();

	public static List<Database> getActiveDatabases() {
		return activeDatabases;
	}

	public static DatabaseFactory getInstance() {
		return DatabaseHolder.instance;
	}

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private DatabaseFactory() {

	}

	public long getCurrentWriteFileDatabaseId() {
		List<Database> list = getDatabases();
		if (list != null && !list.isEmpty()) {
			for (Database database : list) {
				if (StringUtils.equals(database.getUseType(), "FILE")) {
					return database.getId();
				}
			}
		}
		return 0;
	}

	public static synchronized void clearAll() {
		databases.clear();
		databaseNames.clear();
		fileDatabaseIds.clear();
		activeDatabases.clear();
	}

	public synchronized void clearDatabases() {
		databases.clear();
		databaseNames.clear();
		fileDatabaseIds.clear();
		activeDatabases.clear();
	}

	public synchronized List<Database> getDatabases() {
		if (!databases.isEmpty()) {
			return databases;
		}
		List<Database> list = new ArrayList<Database>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnectionFactory.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(" select * from SYS_DATABASE where ACTIVE_ = '1' and TYPE_ <> 'mongodb' ");
			while (rs.next()) {
				Database model = new Database();
				model.setId(rs.getLong("ID_"));
				model.setName(rs.getString("NAME_"));
				model.setCode(rs.getString("CODE_"));
				model.setTitle(rs.getString("TITLE_"));
				model.setHost(rs.getString("HOST_"));
				model.setPort(rs.getInt("PORT_"));
				model.setKey(rs.getString("KEY_"));
				model.setType(rs.getString("TYPE_"));
				model.setDbname(rs.getString("DBNAME_"));
				model.setUser(rs.getString("USER_"));
				model.setPassword(rs.getString("PASSWORD_"));
				model.setActive(rs.getString("ACTIVE_"));
				model.setVerify(rs.getString("VERIFY_"));
				model.setInitFlag(rs.getString("INITFLAG_"));
				model.setLevel(rs.getInt("LEVEL_"));
				model.setUseType(rs.getString("USEYPE_"));
				model.setProviderClass(rs.getString("PROVIDERCLASS_"));
				list.add(model);
			}
			rs.close();
			stmt.close();
			rs = null;
			stmt = null;
		} catch (Exception ex) {
			logger.error("get databases error", ex);
		} finally {
			JdbcUtils.close(rs);
			JdbcUtils.close(stmt);
			JdbcUtils.close(conn);
		}

		if (list != null && !list.isEmpty()) {
			Collections.sort(list);
			for (Database database : list) {
				databases.add(database);
			}
		}
		logger.debug("databases size:" + databases.size());
		return databases;
	}

	public String getName(long databaseId) {
		if (databaseNames.isEmpty()) {
			List<Database> list = getDatabases();
			if (list != null && !list.isEmpty()) {
				for (Database database : list) {
					databaseNames.put(database.getId(), database.getName());
				}
			}
		}
		return databaseNames.get(databaseId);
	}

	/**
	 * 获取某个数据库实例的从库列表（某个主库的只读从库）
	 * 
	 * @param databaseId
	 * @return
	 */
	public List<Database> getSlaveDatabases(long databaseId) {
		List<Database> list = new ArrayList<Database>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnectionFactory.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(" select * from SYS_DATABASE where ACTIVE_ = '1' and PARENTID_ = " + databaseId);
			while (rs.next()) {
				Database model = new Database();
				model.setId(rs.getLong("ID_"));
				model.setName(rs.getString("NAME_"));
				model.setCode(rs.getString("CODE_"));
				model.setTitle(rs.getString("TITLE_"));
				model.setHost(rs.getString("HOST_"));
				model.setPort(rs.getInt("PORT_"));
				model.setKey(rs.getString("KEY_"));
				model.setType(rs.getString("TYPE_"));
				model.setDbname(rs.getString("DBNAME_"));
				model.setUser(rs.getString("USER_"));
				model.setPassword(rs.getString("PASSWORD_"));
				model.setActive(rs.getString("ACTIVE_"));
				model.setVerify(rs.getString("VERIFY_"));
				model.setInitFlag(rs.getString("INITFLAG_"));
				model.setLevel(rs.getInt("LEVEL_"));
				model.setUseType(rs.getString("USEYPE_"));
				model.setProviderClass(rs.getString("PROVIDERCLASS_"));
				list.add(model);
			}
			rs.close();
			stmt.close();
			rs = null;
			stmt = null;
		} catch (Exception ex) {
			logger.error("get databases error", ex);
		} finally {
			JdbcUtils.close(rs);
			JdbcUtils.close(stmt);
			JdbcUtils.close(conn);
		}
		return list;
	}

	public void reload() {
		try {
			if (!DBUtils.tableExists("SYS_KEY")) {
				TableDefinition tableDefinition = SysKeyDomainFactory.getTableDefinition();
				DBUtils.createTable(tableDefinition);
			}
			if (!DBUtils.tableExists("SYS_DATABASE")) {
				EntitySchemaUpdate bean = new EntitySchemaUpdate();
				bean.updateDDL();
			}
		} catch (Exception ex) {
			logger.error("init tables error", ex);
		}
		ForkJoinPool pool = ForkJoinPool.commonPool();
		logger.info("准备连接数据库...");
		try {
			DatabaseConnectionConfig config = new DatabaseConnectionConfig();
			config.checkAndInitToken();
			List<Database> databases = getDatabases();
			if (databases != null && !databases.isEmpty()) {
				for (Database database : databases) {
					ConnectionTask task = new ConnectionTask(database);
					Future<Database> result = pool.submit(task);
					if (result != null && result.get() != null) {
						activeDatabases.add(result.get());
					}
				}

				// 线程阻塞，等待所有任务完成
				try {
					pool.awaitTermination(500, TimeUnit.MILLISECONDS);
				} catch (InterruptedException ex) {
				}
			}
		} catch (java.lang.Throwable ex) {
			logger.error("load database list error", ex);
		} finally {
			pool.shutdown();
			logger.info("数据库连接完成。");
		}
	}

}