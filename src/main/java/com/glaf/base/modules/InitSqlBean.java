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
package com.glaf.base.modules;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.core.config.SystemProperties;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.util.FileUtils;
import com.glaf.core.util.IOUtils;
import com.glaf.core.util.JdbcUtils;

/**
 * 
 * 请设置JVM参数才能生效 -Dexecute_init_sql_on_startup=true
 *
 */
public class InitSqlBean {
	private final static Log logger = LogFactory.getLog(InitSqlBean.class);

	public void execute() {
		InputStream inputStream = null;
		try {
			String config = SystemProperties.getConfigRootPath() + "/conf/sql";
			File directory = new File(config);
			if (directory.exists() && directory.isDirectory()) {
				File[] filelist = directory.listFiles();
				if (filelist != null) {
					for (int i = 0, len = filelist.length; i < len; i++) {
						File file = filelist[i];
						if (file.isFile() && file.getName().endsWith(".sql")) {
							logger.info("load sql data:" + file.getName());
							inputStream = new FileInputStream(file);
							this.execute(file.getName(), inputStream);
							IOUtils.closeStream(inputStream);
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			IOUtils.closeStream(inputStream);
		}
	}

	public void execute(String filename, InputStream inputStream) {
		Connection conn = null;
		Statement statement = null;
		try {
			byte[] bytes = FileUtils.getBytes(inputStream);
			String sql = new String(bytes, "UTF-8");
			conn = DBConnectionFactory.getConnection();
			conn.setAutoCommit(false);
			statement = conn.createStatement();
			int index = 0;
			if (sql.indexOf(";") > 0) {
				StringTokenizer token = new StringTokenizer(sql, ";");
				while (token.hasMoreTokens()) {
					String tmp = token.nextToken();
					if (tmp.trim().length() == 0) {
						continue;
					}
					if (StringUtils.contains(tmp.toLowerCase(), " drop ")) {
						continue;
					}
					if (StringUtils.contains(tmp.toLowerCase(), " delete ")) {
						continue;
					}
					if (StringUtils.contains(tmp.toLowerCase(), " truncate ")) {
						continue;
					}
					statement.addBatch(tmp);
					index++;
					if (index % 5000 == 0) {
						statement.executeBatch();
						conn.commit();
						statement.clearBatch();
						logger.info(index + " execute......");
					}
				}
			}
			statement.executeBatch();
			conn.commit();
			logger.info(index + " execute......");
			logger.info(filename + " execute ok.");
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			JdbcUtils.close(statement);
			JdbcUtils.close(conn);
		}
	}

}
