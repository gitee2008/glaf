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
package com.glaf.matrix.data.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.core.config.SystemProperties;
import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.FileUtils;
import com.glaf.core.util.JdbcUtils;
import com.zaxxer.hikari.HikariDataSource;

public class SQLiteToGenaralDB {

	protected static final Log logger = LogFactory.getLog(SQLiteToGenaralDB.class);

	protected static AtomicBoolean running = new AtomicBoolean(false);

	public boolean importData(String systemName, String sqliteDB) {
		List<String> tables = DBUtils.getTables(systemName);
		return this.importData(systemName, sqliteDB, tables);
	}

	public boolean importData(String systemName, String sqliteDB, List<String> tables) {
		List<ColumnDefinition> columns = null;
		SQLiteToTable tableDB = new SQLiteToTable();
		// List<String> tables = new ArrayList<String>();
		// tables.add("SYS_TEMPLATE");
		Map<String, String> tableErrMap = new HashMap<String, String>();
		List<String> errorTables = new ArrayList<String>();
		SQLiteHelper sqliteHelper = new SQLiteHelper();
		HikariDataSource sourceDataSource = null;
		java.sql.Connection conn = null;
		if (!running.get()) {
			try {
				running.set(true);
				sourceDataSource = sqliteHelper.getDataSource(sqliteDB);
				conn = sourceDataSource.getConnection();
				List<String> tbls = DBUtils.getTables(conn);
				// List<String> tbls = new ArrayList<String>();
				// tbls.add("SYS_TEMPLATE");
				if (tbls != null && !tbls.isEmpty()) {
					for (String tableName : tables) {
						if (StringUtils.endsWithIgnoreCase(tableName, "_log")) {
							continue;
						}
						if (DBUtils.isTemoraryTable(tableName)) {
							continue;
						}
						if (!tbls.contains(tableName.toLowerCase())) {
							continue;
						}
						try {
							tableName = tableName.toUpperCase();
							columns = DBUtils.getColumnDefinitions(systemName, tableName);
							if (columns != null && !columns.isEmpty()) {
								int batchSize = 1000;
								for (ColumnDefinition column : columns) {
									if (StringUtils.equals(column.getJavaType(), "Blob")) {
										batchSize = 10;
										break;
									}
								}
								tableDB.execute(systemName, sourceDataSource, tableName, batchSize);
							}
						} catch (Exception ex) {
							errorTables.add(tableName);
							tableErrMap.put(tableName, ex.getMessage());
							logger.error(ex);
						}
					}
				}

				if (errorTables.size() > 0) {
					StringBuilder buff = new StringBuilder();
					for (String t : errorTables) {
						logger.error(t + "-->" + tableErrMap.get(t));
						buff.append(t).append(FileUtils.newline);
					}

					String filename = SystemProperties.getConfigRootPath() + "/db/" + sqliteDB + "_import_error.log";
					FileUtils.save(filename, buff.toString().getBytes());
					logger.error("总共有" + errorTables.size() + "个表导入失败，请参考日志文件手工处理" + filename);
					return false;
				} else {
					logger.info("Data Import OK!");
					logger.info("数据导入成功！");
					return true;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			} finally {
				running.set(false);
				JdbcUtils.close(conn);
				if (sourceDataSource != null) {
					sourceDataSource.close();
					sourceDataSource = null;
				}
			}
		}
		return false;
	}

}
