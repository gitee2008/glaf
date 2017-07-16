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
package com.glaf.core.jdbc;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.druid.pool.DruidPooledConnection;

import com.glaf.core.config.BaseConfiguration;
import com.glaf.core.config.Configuration;
import com.glaf.core.security.Authentication;

public class QueryConnectionFactory {
	public class CheckConnectionTask implements Runnable {
		public void run() {
			if (!connectionMap.isEmpty()) {
				logger.debug("检测数据库查询......");
				Collection<ConnectionInfo> connectionList = connectionMap.values();
				Collection<ConnectionInfo> list = new ArrayList<ConnectionInfo>();
				if (connectionList != null && !connectionList.isEmpty()) {
					for (ConnectionInfo info : connectionList) {
						if (info.getConnection() != null) {
							list.add(info);
						}
					}
				}
				int size = list.size();
				long nowTime = System.currentTimeMillis();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				logger.debug("检测到数据库查询数目:" + size);
				for (ConnectionInfo info : list) {
					// 强制关闭超过30秒的连接
					if ((nowTime - info.getStartTime()) > conf.getInt("sql_query_timeout", 30000)) {
						Connection connection = info.getConnection();
						try {
							if (connection != null && !connection.isClosed()) {
								logger.debug("connection impl class:" + connection.getClass().getName());
								logger.warn("close query connection:" + info.toString());
								if (connection instanceof DruidPooledConnection) {
									logger.warn("准备关闭物理连接......");
									DruidPooledConnection conn = (DruidPooledConnection) connection;
									conn.abandond();// 丢弃连接，标识后连接池会自动回收。
									conn.getConnection().close();
									conn.close();
									conn = null;
									info.setConnection(null);
									connectionMap.remove(info.getId());
									logger.warn("物理连接已经关闭。");
								} else {
									connection.close();
									connection = null;
									info.setConnection(null);
									connectionMap.remove(info.getId());
									logger.warn("物理连接已经关闭。");
								}
								break;
							}
						} catch (Exception ex) {
							logger.error(ex);
						}
					}
				}
			}
		}
	}

	private static class QueryConnectionFactoryHolder {
		public static QueryConnectionFactory instance = new QueryConnectionFactory();
	}

	protected static final Log logger = LogFactory.getLog(QueryConnectionFactory.class);

	protected static final ConcurrentMap<String, ConnectionInfo> connectionMap = new ConcurrentHashMap<String, ConnectionInfo>();

	protected static volatile Configuration conf = BaseConfiguration.create();

	public static QueryConnectionFactory getInstance() {
		return QueryConnectionFactoryHolder.instance;
	}

	protected volatile boolean startScheduler = false;

	protected ScheduledExecutorService scheduledThreadPool = Executors.newSingleThreadScheduledExecutor();

	private QueryConnectionFactory() {
		if (!startScheduler) {
			this.startScheduler();
		}
	}

	public Collection<ConnectionInfo> getConnections() {
		return connectionMap.values();
	}

	/**
	 * 注册连接信息
	 * 
	 * @param ts
	 *            开始时间戳
	 * @param connection
	 *            数据库连接
	 */
	public void register(long ts, Connection connection) {
		if (connection != null) {
			String id = ts + "_" + DigestUtils.sha1Hex(connection.toString());
			ConnectionInfo info = new ConnectionInfo();
			info.setId(id);
			info.setConnection(connection);
			info.setStartTime(System.currentTimeMillis());
			if (Authentication.getAuthenticatedActorId() != null) {
				info.setActorId(Authentication.getAuthenticatedActorId());
			}
			connectionMap.put(id, info);
		}
	}

	public void startScheduler() {
		CheckConnectionTask command = new CheckConnectionTask();
		scheduledThreadPool.scheduleAtFixedRate(command, 20, 5, TimeUnit.SECONDS);
		startScheduler = true;
	}

	/**
	 * 释放连接信息
	 * 
	 * @param ts
	 *            开始时间戳
	 * @param connection
	 *            数据库连接
	 */
	public void unregister(long ts, Connection connection) {
		String id = ts + "_" + DigestUtils.sha1Hex(connection.toString());
		if (!connectionMap.isEmpty()) {
			connectionMap.remove(id);
		}
	}

}
