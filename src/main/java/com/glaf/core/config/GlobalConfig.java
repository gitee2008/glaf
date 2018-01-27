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

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.domain.Database;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.util.FileUtils;
import com.glaf.core.util.IOUtils;
import com.glaf.core.util.JdbcUtils;
import com.glaf.core.util.PropertiesUtils;

public class GlobalConfig {

	protected final static Logger logger = LoggerFactory.getLogger(GlobalConfig.class);

	public static Properties getConfigProperties(String filename) {
		String path = SystemProperties.getConfigRootPath() + "/conf/" + filename;
		logger.info("load properties file:" + path);
		InputStream inputStream = null;
		try {
			inputStream = FileUtils.getInputStream(path);
			if (inputStream != null) {
				Properties props = PropertiesUtils.loadProperties(inputStream);
				if (props != null && !props.isEmpty()) {
					return props;
				}
			}
		} catch (IOException ex) {
			// ex.printStackTrace();
			logger.error("load properties error", ex);
		} finally {
			IOUtils.closeStream(inputStream);
		}
		return null;
	}

	public static Properties getProperties(String category) {
		Properties props = new Properties();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnectionFactory.getConnection();
			pstmt = conn.prepareStatement(" select NAME_, VALUE_ from SYS_PROPERTY where CATEGORY_ = ? ");
			pstmt.setString(1, category);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String name = rs.getString(1);
				String value = rs.getString(2);
				props.put(name, value);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("get system config data error", ex);
		} finally {
			JdbcUtils.close(rs);
			JdbcUtils.close(pstmt);
			JdbcUtils.close(conn);
		}
		logger.debug("props size:" + props.size());
		return props;
	}

	public static void saveProperties(String category, Properties props) {
		Map<String, String> dataMap = new HashMap<String, String>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnectionFactory.getConnection();
			conn.setAutoCommit(false);

			pstmt = conn.prepareStatement(" select ID_, NAME_ from SYS_PROPERTY where CATEGORY_ = ? ");
			pstmt.setString(1, category);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String id = rs.getString(1);
				String name = rs.getString(2);
				dataMap.put(name, id);
			}

			JdbcUtils.close(rs);
			JdbcUtils.close(pstmt);

			pstmt = conn.prepareStatement(" update SYS_PROPERTY set VALUE_ = ? where ID_ = ? ");
			java.util.Enumeration<?> e = props.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String value = props.getProperty(key);
				if (dataMap.get(key) != null) {
					pstmt.setString(1, value);
					pstmt.setString(2, dataMap.get(key));
					pstmt.addBatch();
				}
			}

			pstmt.executeBatch();
			conn.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("save system config data error", ex);
		} finally {
			JdbcUtils.close(rs);
			JdbcUtils.close(pstmt);
			JdbcUtils.close(conn);
		}
	}

	public static void setCurrentSystemName(LoginContext loginContext) {
		if (loginContext.getTenant() == null || loginContext.getTenantId() == null) {
			return;
		}
		if (StringUtils.isNotEmpty(loginContext.getCurrentSystemName())) {
			/**
			 * 切换到用户的环境名称
			 */
			Environment.setCurrentSystemName(loginContext.getCurrentSystemName());
			return;
		}
		long databaseId = loginContext.getTenant().getDatabaseId();
		if (databaseId > 0) {
			/**
			 * 切换到默认主库查询数据库信息
			 */
			Environment.setCurrentSystemName(Environment.DEFAULT_SYSTEM_NAME);
			IDatabaseService databaseService = ContextFactory.getBean("databaseService");
			Database database = databaseService.getDatabaseById(databaseId);
			if (database != null) {
				/**
				 * 切换到用户的环境名称
				 */
				Environment.setCurrentSystemName(database.getName());
			}
		}
	}

	public GlobalConfig() {

	}

}