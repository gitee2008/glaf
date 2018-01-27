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
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glaf.core.config.BaseConfiguration;
import com.glaf.core.config.Configuration;
import com.glaf.core.config.DBConfiguration;
import com.glaf.core.util.ClassUtils;
import com.glaf.core.util.JdbcUtils;
import com.glaf.core.util.PropertiesHelper;

public class Dbcp2ConnectionProvider implements ConnectionProvider {

	private static final Logger log = LoggerFactory.getLogger(Dbcp2ConnectionProvider.class);

	protected static Configuration conf = BaseConfiguration.create();

	protected static int MAX_RETRIES = conf.getInt("jdbc.connection.retryCount", 10);

	private volatile DataSource ds;
	private volatile Integer isolation;
	private volatile boolean autocommit;

	public Dbcp2ConnectionProvider() {
		log.info("----------------------------Dbcp2ConnectionProvider-----------------");
	}

	public void close() {

	}

	public void closeConnection(Connection conn) throws SQLException {
		conn.close();
	}

	public void configure(Properties props) {
		String jdbcDriverClass = props.getProperty(DBConfiguration.JDBC_DRIVER);
		String jdbcUrl = props.getProperty(DBConfiguration.JDBC_URL);
		Properties connectionProps = ConnectionProviderFactory.getConnectionProperties(props);

		log.info("DBCP2 using driver: " + jdbcDriverClass + " at URL: " + jdbcUrl);
		log.info("Connection properties: " + PropertiesHelper.maskOut(connectionProps, "password"));

		autocommit = PropertiesHelper.getBoolean(DBConfiguration.JDBC_AUTOCOMMIT, props);
		log.info("autocommit mode: " + autocommit);

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

		String dbUser = props.getProperty(DBConfiguration.JDBC_USER);
		String dbPassword = props.getProperty(DBConfiguration.JDBC_PASSWORD);
		String validationQuery = props.getProperty(ConnectionConstants.PROP_VALIDATIONQUERY);

		if (dbUser == null) {
			dbUser = "";
		}

		if (dbPassword == null) {
			dbPassword = "";
		}

		ConnectionFactory connFactory = new DriverManagerConnectionFactory(jdbcUrl, dbUser, dbPassword);

		PoolableConnectionFactory pcf = new PoolableConnectionFactory(connFactory, null);
		pcf.setPoolStatements(true);
		pcf.setDefaultReadOnly(Boolean.FALSE);
		pcf.setDefaultAutoCommit(Boolean.TRUE);
		pcf.setDefaultQueryTimeout(300);
		if (StringUtils.isNotEmpty(validationQuery)) {
			log.debug("validationQuery:" + validationQuery);
			pcf.setValidationQuery(validationQuery);
		}

		GenericObjectPool<PoolableConnection> connPool = new GenericObjectPool<PoolableConnection>(pcf);
		pcf.setPool(connPool);

		ds = new PoolingDataSource<PoolableConnection>(connPool);

		Connection conn = null;
		try {
			conn = connPool.borrowObject();
			if (conn == null) {
				throw new RuntimeException("DBCP2 connection pool can't get jdbc connection");
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
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
					if (connection.getAutoCommit() != autocommit) {
						connection.setAutoCommit(autocommit);
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
					throw new SQLException("dbcp2 can't getConnection", ex);
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