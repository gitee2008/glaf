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

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.glaf.core.base.ConnectionDefinition;
import com.glaf.core.domain.Database;
import com.glaf.core.domain.util.DatabaseJsonFactory;
import com.glaf.core.security.SecurityUtils;

public class ZooKeeperDBConfig {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	public ConnectionDefinition getMaster(String address, String sysCode) {
		CuratorFramework zkClient = null;
		try {
			RetryPolicy retryPolicy = new ExponentialBackoffRetry(5000, 10);
			zkClient = CuratorFrameworkFactory.newClient(address, retryPolicy);
			zkClient.start();
			String path = "/databases/" + sysCode;
			byte[] bytes = zkClient.getData().forPath(path);
			if (bytes != null) {
				String json = new String(bytes);
				JSONObject jsonObject = JSON.parseObject(json);
				Database database = DatabaseJsonFactory.jsonToObject(jsonObject);
				DatabaseConnectionConfig cfg = new DatabaseConnectionConfig();
				if (cfg.checkConnectionImmediately(database)) {
					String password = SecurityUtils.decode(database.getKey(), database.getPassword());
					ConnectionDefinition connectionDefinition = new ConnectionDefinition();
					connectionDefinition.setName(Environment.DEFAULT_SYSTEM_NAME);
					connectionDefinition.setHost(database.getHost());
					connectionDefinition.setDatabase(database.getDbname());
					connectionDefinition.setPort(database.getPort());
					connectionDefinition.setUser(database.getUser());
					connectionDefinition.setPassword(password);
					String dbType = database.getType();
					Properties props = DBConfiguration.getTemplateProperties(dbType);
					if (props != null && !props.isEmpty()) {
						Map<String, Object> context = new HashMap<String, Object>();
						context.put("host", database.getHost());
						if (database.getPort() > 0) {
							context.put("port", database.getPort());
						} else {
							context.put("port", props.getProperty(DBConfiguration.PORT));
						}
						context.put("databaseName", database.getDbname());
						String driver = props.getProperty(DBConfiguration.JDBC_DRIVER);
						String url = props.getProperty(DBConfiguration.JDBC_URL);
						url = com.glaf.core.el.ExpressionTools.evaluate(url, context);
						connectionDefinition.setDriver(driver);
						connectionDefinition.setUrl(url);
						connectionDefinition.setType(dbType);
						return connectionDefinition;
					}
				}
			}
			return null;
		} catch (Exception ex) {
			logger.error("get database info error", ex);
			throw new RuntimeException(ex);
		} finally {
			if (zkClient != null) {
				zkClient.close();
				zkClient = null;
			}
		}
	}

	public void syncMaster(String address, String sysCode, Database database) {
		CuratorFramework zkClient = null;
		try {
			RetryPolicy retryPolicy = new ExponentialBackoffRetry(5000, 10);
			zkClient = CuratorFrameworkFactory.newClient(address, retryPolicy);
			zkClient.start();
			String path = "/databases/" + sysCode;
			JSONObject jsonObject = DatabaseJsonFactory.toJsonObject(database);
			DatabaseConnectionConfig cfg = new DatabaseConnectionConfig();
			if (cfg.checkConnectionImmediately(database)) {
				byte[] data = jsonObject.toJSONString().getBytes();
				Stat stat = zkClient.checkExists().forPath("/databases");
				if (stat == null) {
					zkClient.create().forPath("/databases");
				}
				stat = zkClient.checkExists().forPath(path);
				if (stat == null) {
					zkClient.create().withMode(CreateMode.PERSISTENT).inBackground().forPath(path, data);
					logger.debug("create key:" + path);
				} else {
					zkClient.setData().inBackground().forPath(path, data);
					logger.debug("update key:" + path);
				}
			}
		} catch (Exception ex) {
			logger.error("sync database info error", ex);
			throw new RuntimeException(ex);
		} finally {
			if (zkClient != null) {
				zkClient.close();
				zkClient = null;
			}
		}
	}

}
