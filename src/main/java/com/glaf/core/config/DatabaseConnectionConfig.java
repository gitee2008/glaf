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

package com.glaf.core.config;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

 
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.alibaba.druid.pool.DruidDataSource;
import com.glaf.core.base.ConnectionDefinition;
import com.glaf.core.config.DBConfiguration;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.query.DatabaseQuery;
import com.glaf.core.security.LoginContext;
import com.glaf.core.security.SecurityUtils;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.JdbcUtils;
import com.glaf.core.util.UUID32;
import com.glaf.core.domain.Database;
import com.glaf.core.domain.util.DatabaseDomainFactory;

public class DatabaseConnectionConfig implements ConnectionConfig {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected IDatabaseService databaseService;

	public DatabaseConnectionConfig() {

	}

	public void checkAndInitToken() {
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnectionFactory.getConnection();
			conn.setAutoCommit(false);
			if (!DBUtils.tableExists(conn, "SYS_DATABASE")) {
				DBUtils.createTable(conn, DatabaseDomainFactory.getTableDefinition());
				conn.commit();
			} else {
				if (DBUtils.tableExists(conn, "SYS_DATABASE")) {
					DBUtils.alterTable(conn, DatabaseDomainFactory.getTableDefinition());
					conn.commit();
				}
			}
			stmt = conn.createStatement();
			rs = stmt.executeQuery(" select ID_ from SYS_DATABASE where ACTIVE_ = '1' and TOKEN_ is null ");
			List<Long> databaseIds = new ArrayList<Long>();
			while (rs.next()) {
				databaseIds.add(rs.getLong(1));
			}
			pstmt = conn.prepareStatement(" update SYS_DATABASE set TOKEN_ = ? where ID_ = ? and TOKEN_ is null  ");
			Iterator<Long> iterator = databaseIds.iterator();
			while (iterator.hasNext()) {
				Long databaseId = iterator.next();
				pstmt.setString(1, UUID32.getUUID() + UUID32.getUUID());
				pstmt.setLong(2, databaseId);
				pstmt.addBatch();
			}
			pstmt.executeBatch();
			pstmt.close();
			conn.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("database checkAndInitToken error", ex);
		} finally {
			JdbcUtils.close(rs);
			JdbcUtils.close(stmt);
			JdbcUtils.close(pstmt);
			JdbcUtils.close(conn);
		}
	}

	public boolean checkConfig(Database database) {
		String name = database.getName();
		String dbType = database.getType();
		String host = database.getHost();
		int port = database.getPort();
		String databaseName = database.getDbname();
		String user = database.getUser();
		try {
			String password = SecurityUtils.decode(database.getKey(), database.getPassword());
			if (this.checkConnectionImmediately(database)) {
				DBConfiguration.addDataSourceProperties(name, dbType, host, port, databaseName, user, password);
				logger.debug("->name:" + name);
				if (DBConnectionFactory.checkConnection(name)) {
					return true;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(database.getTitle() + " config error", ex);
		}
		return false;
	}

	public boolean checkConnection(ConnectionDefinition connectionDefinition) {
		String dbType = connectionDefinition.getType();
		String host = connectionDefinition.getHost();
		int port = connectionDefinition.getPort();
		String databaseName = connectionDefinition.getDatabase();
		String user = connectionDefinition.getUser();
		Connection connection = null;
		HikariDataSource ds = null;
		DruidDataSource bds = null;
		try {
			String password = connectionDefinition.getPassword();
			Properties props = DBConfiguration.getTemplateProperties(dbType);
			if (props != null && !props.isEmpty()) {
				Map<String, Object> context = new HashMap<String, Object>();
				context.put("host", host);
				if (port > 0) {
					context.put("port", port);
				} else {
					context.put("port", props.getProperty(DBConfiguration.PORT));
				}
				context.put("databaseName", databaseName);
				String driver = props.getProperty(DBConfiguration.JDBC_DRIVER);
				String url = props.getProperty(DBConfiguration.JDBC_URL);
				url = com.glaf.core.el.ExpressionTools.evaluate(url, context);
				logger.debug("driver:" + driver);
				logger.debug("url:" + url);

				boolean isSQLite = false;
				if (StringUtils.startsWith(driver.trim(), "org.sqlite")) {
					isSQLite = true;
				}
				if (isSQLite || "hikari".equals(System.getProperty("jdbc_pool_type"))) {
					HikariConfig config = new HikariConfig();
					config.setDriverClassName(driver);
					config.setJdbcUrl(url);
					config.setMaximumPoolSize(2);
					// config.setMaxLifetime(1000L * 30);
					// config.setConnectionTimeout(5000L);
					// config.setIdleTimeout(1000L * 20);
					if (StringUtils.isNotEmpty(user)) {
						config.setUsername(user);
						if (password != null) {
							config.setPassword(password);
							ds = new HikariDataSource(config);
						} else {
							ds = new HikariDataSource(config);
						}
					} else {
						ds = new HikariDataSource(config);
					}
					if (ds != null) {
						connection = ds.getConnection();
					}
				} else {
					bds = new DruidDataSource();
					bds.setInitialSize(1);
					bds.setMaxActive(2);
					bds.setDriverClassName(driver);
					bds.setUrl(url);
					bds.setUsername(user);
					bds.setPassword(password);
					connection = bds.getConnection();
				}
			}

			if (connection != null) {
				return true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(connectionDefinition.getSubject() + " config error", ex);
		} finally {
			JdbcUtils.close(connection);
			if (ds != null) {
				ds.close();
				ds = null;
			}
			if (bds != null) {
				try {
					bds.close();
				} catch (Exception e) {
				}
				bds = null;
			}
		}
		return false;
	}

	public boolean checkConnectionImmediately(Database database) {
		String dbType = database.getType();
		String host = database.getHost();
		int port = database.getPort();
		String databaseName = database.getDbname();
		String user = database.getUser();
		Connection connection = null;
		HikariDataSource ds = null;
		DruidDataSource bds = null;
		try {
			String password = SecurityUtils.decode(database.getKey(), database.getPassword());
			Properties props = DBConfiguration.getTemplateProperties(dbType);
			if (props != null && !props.isEmpty()) {
				Map<String, Object> context = new HashMap<String, Object>();
				context.put("host", host);
				if (port > 0) {
					context.put("port", port);
				} else {
					context.put("port", props.getProperty(DBConfiguration.PORT));
				}
				context.put("databaseName", databaseName);
				String driver = props.getProperty(DBConfiguration.JDBC_DRIVER);
				String url = props.getProperty(DBConfiguration.JDBC_URL);
				url = com.glaf.core.el.ExpressionTools.evaluate(url, context);
				logger.debug("driver:" + driver);
				logger.debug("url:" + url);

				boolean isSQLite = false;
				if (StringUtils.startsWith(driver.trim(), "org.sqlite")) {
					isSQLite = true;
				}
				if (isSQLite || "hikari".equals(System.getProperty("jdbc_pool_type"))) {
					HikariConfig config = new HikariConfig();
					config.setDriverClassName(driver);
					config.setJdbcUrl(url);
					config.setMaximumPoolSize(2);
					// config.setMaxLifetime(1000L * 30);
					// config.setConnectionTimeout(5000L);
					// config.setIdleTimeout(1000L * 20);
					if (StringUtils.isNotEmpty(user)) {
						config.setUsername(user);
						if (password != null) {
							config.setPassword(password);
							ds = new HikariDataSource(config);
						} else {
							ds = new HikariDataSource(config);
						}
					} else {
						ds = new HikariDataSource(config);
					}
					if (ds != null) {
						connection = ds.getConnection();
					}
				} else {
					bds = new DruidDataSource();
					bds.setInitialSize(1);
					bds.setMaxActive(2);
					bds.setDriverClassName(driver);
					bds.setUrl(url);
					bds.setUsername(user);
					bds.setPassword(password);
					connection = bds.getConnection();
				}
			}

			if (connection != null) {
				return true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(database.getTitle() + " config error", ex);
		} finally {
			JdbcUtils.close(connection);
			if (ds != null) {
				ds.close();
				ds = null;
			}
			if (bds != null) {
				try {
					bds.close();
				} catch (Exception e) {
				}
				bds = null;
			}
		}
		return false;
	}

	public List<Database> getActiveDatabases(LoginContext loginContext) {
		DatabaseQuery query = new DatabaseQuery();
		query.active("1");
		List<Database> activeDatabases = new ArrayList<Database>();
		String currentSystemName = Environment.getCurrentSystemName();
		try {
			Environment.setCurrentSystemName(Environment.DEFAULT_SYSTEM_NAME);
			List<Database> databases = null;
			if (loginContext.isSystemAdministrator()) {
				databases = getDatabaseService().list(query);
			} else {
				databases = getDatabaseService().getDatabases(loginContext.getActorId());
			}
			if (databases != null && !databases.isEmpty()) {
				for (Database database : databases) {
					if ("1".equals(database.getActive())) {
						activeDatabases.add(database);
					}
				}
			}
		} finally {
			Environment.setCurrentSystemName(currentSystemName);
		}
		return activeDatabases;
	}

	public Connection getConnection(Database database) {
		String dbType = database.getType();
		String host = database.getHost();
		int port = database.getPort();
		String databaseName = database.getDbname();
		String user = database.getUser();
		Connection connection = null;
		HikariDataSource ds = null;
		DruidDataSource bds = null;
		try {
			String password = SecurityUtils.decode(database.getKey(), database.getPassword());
			Properties props = DBConfiguration.getTemplateProperties(dbType);
			if (props != null && !props.isEmpty()) {
				Map<String, Object> context = new HashMap<String, Object>();
				context.put("host", host);
				if (port > 0) {
					context.put("port", port);
				} else {
					context.put("port", props.getProperty(DBConfiguration.PORT));
				}
				context.put("databaseName", databaseName);
				String driver = props.getProperty(DBConfiguration.JDBC_DRIVER);
				String url = props.getProperty(DBConfiguration.JDBC_URL);
				url = com.glaf.core.el.ExpressionTools.evaluate(url, context);
				logger.debug("driver:" + driver);
				logger.debug("url:" + url);

				boolean isSQLite = false;
				if (StringUtils.startsWith(driver.trim(), "org.sqlite")) {
					isSQLite = true;
				}
				if (isSQLite || "hikari".equals(System.getProperty("jdbc_pool_type"))) {
					HikariConfig config = new HikariConfig();
					config.setDriverClassName(driver);
					config.setJdbcUrl(url);
					config.setMaximumPoolSize(2);
					// config.setMaxLifetime(1000L * 30);
					// config.setConnectionTimeout(5000L);
					// config.setIdleTimeout(1000L * 20);
					if (StringUtils.isNotEmpty(user)) {
						config.setUsername(user);
						if (password != null) {
							config.setPassword(password);
							ds = new HikariDataSource(config);
						} else {
							ds = new HikariDataSource(config);
						}
					} else {
						ds = new HikariDataSource(config);
					}
					if (ds != null) {
						connection = ds.getConnection();
					}
				} else {
					bds = new DruidDataSource();
					bds.setInitialSize(1);
					bds.setMaxActive(2);
					bds.setDriverClassName(driver);
					bds.setUrl(url);
					bds.setUsername(user);
					bds.setPassword(password);
					connection = bds.getConnection();
				}

			}

			if (connection != null) {
				return connection;
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return null;
	}

	public List<ConnectionDefinition> getConnectionDefinitions() {
		List<ConnectionDefinition> list = new ArrayList<ConnectionDefinition>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnectionFactory.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(" select * from SYS_DATABASE where ACTIVE_ = '1' and TYPE_ <> 'mongodb' ");
			while (rs.next()) {
				ConnectionDefinition model = new ConnectionDefinition();
				String key = rs.getString("KEY_");
				String password = rs.getString("PASSWORD_");
				String pass = SecurityUtils.decode(key, password);
				model.setName(rs.getString("NAME_"));
				model.setSubject(rs.getString("TITLE_"));
				model.setHost(rs.getString("HOST_"));
				model.setPort(rs.getInt("PORT_"));
				model.setType(rs.getString("TYPE_"));
				model.setDatabase(rs.getString("DBNAME_"));
				model.setUser(rs.getString("USER_"));
				model.setPassword(pass);
				list.add(model);
			}
			rs.close();
			stmt.close();
			rs = null;
			stmt = null;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("get databases error", ex);
		} finally {
			JdbcUtils.close(rs);
			JdbcUtils.close(stmt);
			JdbcUtils.close(conn);
		}
		logger.debug("connection size:" + list.size());
		return list;
	}

	public Database getDatabase(LoginContext loginContext, Long databaseId) {
		Database currentDB = null;
		if (databaseId != null && databaseId > 0) {
			String currentSystemName = Environment.getCurrentSystemName();
			try {
				Environment.setCurrentSystemName(Environment.DEFAULT_SYSTEM_NAME);

				DatabaseQuery query = new DatabaseQuery();
				query.active("1");
				List<Database> activeDatabases = new ArrayList<Database>();

				List<Database> databases = null;
				if (loginContext != null) {
					if (loginContext.isSystemAdministrator()) {
						databases = getDatabaseService().list(query);
					} else {
						databases = getDatabaseService().getDatabases(loginContext.getActorId());
					}
				}

				if (databases != null && !databases.isEmpty()) {
					for (Database database : databases) {
						if ("1".equals(database.getActive())) {
							activeDatabases.add(database);
						}
					}
				}

				if (!activeDatabases.isEmpty()) {
					if (databaseId > 0) {
						currentDB = getDatabaseService().getDatabaseById(databaseId);
						if (!activeDatabases.contains(currentDB)) {
							currentDB = null;
						}
					}
				}

			} finally {
				Environment.setCurrentSystemName(currentSystemName);
			}
		}

		return currentDB;
	}

	public Database getDatabase(Long databaseId) {
		Database database = null;
		if (databaseId != null && databaseId > 0) {
			String currentSystemName = Environment.getCurrentSystemName();
			try {
				Environment.setCurrentSystemName(Environment.DEFAULT_SYSTEM_NAME);
				database = getDatabaseService().getDatabaseById(databaseId);
			} finally {
				Environment.setCurrentSystemName(currentSystemName);
			}
		}
		return database;
	}

	public List<Database> getDatabases() {
		DatabaseQuery query = new DatabaseQuery();
		query.active("1");
		List<Database> activeDatabases = new ArrayList<Database>();
		String currentSystemName = Environment.getCurrentSystemName();
		try {
			Environment.setCurrentSystemName(Environment.DEFAULT_SYSTEM_NAME);
			List<Database> databases = getDatabaseService().list(query);
			if (databases != null && !databases.isEmpty()) {
				for (Database database : databases) {
					if ("1".equals(database.getActive())) {
						activeDatabases.add(database);
					}
				}
			}
		} finally {
			Environment.setCurrentSystemName(currentSystemName);
		}
		return activeDatabases;
	}

	public List<Database> getDatabases(LoginContext loginContext) {
		DatabaseQuery query = new DatabaseQuery();
		query.active("1");
		List<Database> activeDatabases = new ArrayList<Database>();

		String currentSystemName = Environment.getCurrentSystemName();
		try {
			Environment.setCurrentSystemName(Environment.DEFAULT_SYSTEM_NAME);
			List<Database> databases = null;
			if (loginContext.isSystemAdministrator()) {
				databases = getDatabaseService().list(query);
			} else {
				databases = getDatabaseService().getDatabases(loginContext.getActorId());
			}

			if (databases != null && !databases.isEmpty()) {
				for (Database database : databases) {
					if ("1".equals(database.getActive())) {
						activeDatabases.add(database);
					}
				}
			}
		} finally {
			Environment.setCurrentSystemName(currentSystemName);
		}
		return activeDatabases;
	}

	public List<Database> getDatabases(String useType) {
		DatabaseQuery query = new DatabaseQuery();
		query.active("1");
		query.setUseType(useType);
		List<Database> activeDatabases = new ArrayList<Database>();
		String currentSystemName = Environment.getCurrentSystemName();
		try {
			Environment.setCurrentSystemName(Environment.DEFAULT_SYSTEM_NAME);
			List<Database> databases = getDatabaseService().list(query);
			if (databases != null && !databases.isEmpty()) {
				for (Database database : databases) {
					if ("1".equals(database.getActive())) {
						activeDatabases.add(database);
					}
				}
			}
		} finally {
			Environment.setCurrentSystemName(currentSystemName);
		}
		return activeDatabases;
	}

	public IDatabaseService getDatabaseService() {
		if (databaseService == null) {
			databaseService = ContextFactory.getBean("databaseService");
		}
		return databaseService;
	}

	public void resetVerify(String name, boolean verify) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = DBConnectionFactory.getConnection();
			conn.setAutoCommit(false);
			stmt = conn.prepareStatement(" update SYS_DATABASE set VERIFY_ = ? where NAME_ = ? ");
			if (verify) {
				stmt.setString(1, "Y");
			} else {
				stmt.setString(1, "N");
			}
			stmt.setString(2, name);
			stmt.executeUpdate();
			stmt.close();
			conn.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("update database error", ex);
		} finally {
			JdbcUtils.close(stmt);
			JdbcUtils.close(conn);
		}
	}

	public void setDatabaseService(IDatabaseService databaseService) {
		this.databaseService = databaseService;
	}

}