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

package com.glaf.core.jdbc.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.DataSources;
import com.glaf.core.config.BaseConfiguration;
import com.glaf.core.config.Configuration;
import com.glaf.core.config.DBConfiguration;
import com.glaf.core.util.ClassUtils;
import com.glaf.core.util.JdbcUtils;
import com.glaf.core.util.PropertiesHelper;

public class C3P0ConnectionProvider implements ConnectionProvider {

	private static final Logger log = LoggerFactory.getLogger(C3P0ConnectionProvider.class);

	protected static Configuration conf = BaseConfiguration.create();

	protected static int MAX_RETRIES = conf.getInt("jdbc.connection.retryCount", 10);

	private volatile DataSource ds;
	private volatile Integer isolation;
	private volatile boolean isAutoCommit;

	public C3P0ConnectionProvider() {
		log.info("----------------------------C3P0ConnectionProvider-----------------");
	}

	public void close() {
		try {
			DataSources.destroy(ds);
		} catch (SQLException sqle) {
			log.warn("could not destroy C3P0 connection pool", sqle);
		}
	}

	public void closeConnection(Connection conn) throws SQLException {
		conn.close();
	}

	public void configure(Properties props) throws RuntimeException {
		String jdbcDriverClass = props.getProperty(DBConfiguration.JDBC_DRIVER);
		String jdbcUrl = props.getProperty(DBConfiguration.JDBC_URL);
		Properties connectionProps = ConnectionProviderFactory.getConnectionProperties(props);

		log.info("C3P0 using driver: " + jdbcDriverClass + " at URL: " + jdbcUrl);
		log.info("Connection properties: " + PropertiesHelper.maskOut(connectionProps, "password"));

		if (jdbcDriverClass == null) {
			log.warn("No JDBC Driver class was specified by property " + DBConfiguration.JDBC_DRIVER);
		} else {
			try {
				Class.forName(jdbcDriverClass);
			} catch (ClassNotFoundException cnfe) {
				try {
					ClassUtils.classForName(jdbcDriverClass);
				} catch (Exception e) {
					String msg = "JDBC Driver class not found: " + jdbcDriverClass;
					log.error(msg, e);
					throw new RuntimeException(msg, e);
				}
			}
		}

		try {

			Properties c3props = new Properties();

			for (Iterator<?> ii = props.keySet().iterator(); ii.hasNext();) {
				String key = (String) ii.next();
				if (key.startsWith("c3p0.")) {
					String newKey = key.substring(5);
					c3props.put(newKey, props.get(key));
				}
			}

			Properties allProps = (Properties) props.clone();
			allProps.putAll(c3props);

			Integer initialPoolSize = PropertiesHelper.getInteger(ConnectionConstants.PROP_INITIALSIZE, allProps);
			Integer minPoolSize = PropertiesHelper.getInteger(ConnectionConstants.PROP_MINACTIVE, allProps);
			if (initialPoolSize == null && minPoolSize != null) {
				c3props.put(ConnectionConstants.PROP_INITIALSIZE, String.valueOf(minPoolSize).trim());
			}

			isAutoCommit = PropertiesHelper.getBoolean(DBConfiguration.JDBC_AUTOCOMMIT, allProps);
			log.info("autocommit mode: " + isAutoCommit);

			DataSource unpooled = DataSources.unpooledDataSource(jdbcUrl, connectionProps);

			ds = DataSources.pooledDataSource(unpooled, allProps);
		} catch (Exception ex) {
			log.error("could not instantiate C3P0 connection pool", ex);
			throw new RuntimeException("Could not instantiate C3P0 connection pool", ex);
		}

		Connection conn = null;
		try {
			conn = ds.getConnection();
			if (conn == null) {
				throw new RuntimeException("C3P0 connection pool can't get jdbc connection");
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(conn);
		}

		String i = props.getProperty(DBConfiguration.JDBC_ISOLATION);
		if (i == null) {
			isolation = null;
		} else {
			isolation = new Integer(i);
			log.info("JDBC isolation level: " + DBConfiguration.isolationLevelToString(isolation.intValue()));
		}

	}

	public Connection getConnection() throws SQLException {
		Connection connection = null;
		int retries = 0;
		while (retries < MAX_RETRIES) {
			try {
				connection = ds.getConnection();
				if (connection != null) {
					if (isolation != null) {
						connection.setTransactionIsolation(isolation.intValue());
					}
					if (connection.getAutoCommit() != isAutoCommit) {
						connection.setAutoCommit(isAutoCommit);
					}
					return connection;
				} else {
					retries++;
					try {
						TimeUnit.MILLISECONDS.sleep(200 + new Random().nextInt(1000));// 活锁，随机等待
					} catch (InterruptedException e) {
					}
				}
			} catch (SQLException ex) {
				if (retries++ == MAX_RETRIES) {
					throw new SQLException("c3p0 can't getConnection", ex);
				}
				try {
					TimeUnit.MILLISECONDS.sleep(200 + new Random().nextInt(1000));// 活锁，随机等待
				} catch (InterruptedException e) {
				}
			}
		}
		return connection;
	}

	public DataSource getDataSource() {
		return ds;
	}

	public boolean supportsAggressiveRelease() {
		return false;
	}

}