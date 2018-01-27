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

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glaf.core.config.BaseConfiguration;
import com.glaf.core.config.Configuration;
import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.domain.TableDefinition;
import com.glaf.core.jdbc.BulkInsertBean;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.JdbcUtils;
import com.glaf.core.util.LowerLinkedMap;

public class SQLiteToTable {
	protected static Configuration conf = BaseConfiguration.create();

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	public SQLiteToTable() {

	}

	public boolean execute(String systemName, DataSource dataSource, String tableName, int batchSize) {
		List<String> keys = new ArrayList<String>();

		String sourceIdColumnName = null;

		Connection sourceConn = null;
		PreparedStatement sourcePsmt = null;
		ResultSet sourceRS = null;

		Connection targetConn = null;
		PreparedStatement targetPsmt = null;
		ResultSet targetRS = null;

		PreparedStatement targetInsertPsmt = null;

		try {
			List<ColumnDefinition> srcColumns = DBUtils.getColumnDefinitions(systemName, tableName);
			List<String> primaryKeys = DBUtils.getUpperCasePrimaryKeys(systemName, tableName);

			if (primaryKeys != null && !primaryKeys.isEmpty() && primaryKeys.size() == 1) {
				sourceIdColumnName = primaryKeys.get(0).toLowerCase();
			}

			logger.debug(tableName + " primary keys: " + primaryKeys);

			sourceConn = dataSource.getConnection();

			targetConn = DBConnectionFactory.getConnection(systemName);
			targetConn.setAutoCommit(false);

			int total = 0;
			int total2 = 0;
			sourcePsmt = sourceConn.prepareStatement(" select count(*) from " + tableName.toUpperCase());
			sourceRS = sourcePsmt.executeQuery();
			if (sourceRS.next()) {
				total = sourceRS.getInt(1);
			}

			JdbcUtils.close(sourceRS);
			JdbcUtils.close(sourcePsmt);

			targetPsmt = targetConn.prepareStatement(" select count(*) from " + tableName.toUpperCase());
			targetRS = targetPsmt.executeQuery();
			if (targetRS.next()) {
				total2 = targetRS.getInt(1);
			}

			JdbcUtils.close(targetRS);
			JdbcUtils.close(targetPsmt);

			if (total > 0 && total2 == 0) {
				int totalXY = 0;
				Map<String, Object> dataMap = null;

				TableDefinition tableDefinition = new TableDefinition();
				tableDefinition.setTableName(tableName);
				tableDefinition.setColumns(srcColumns);

				logger.debug("columns:" + srcColumns);

				List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
				BulkInsertBean bulkInsertBean = new BulkInsertBean();
				sourcePsmt = sourceConn.prepareStatement(" select * from " + tableName.toUpperCase());
				sourceRS = sourcePsmt.executeQuery();
				ResultSetMetaData rsmd = sourceRS.getMetaData();
				while (sourceRS.next()) {
					totalXY++;
					dataMap = this.toMap(rsmd, sourceRS);
					if (sourceIdColumnName != null && dataMap.get(sourceIdColumnName) != null) {
						if (!keys.contains(dataMap.get(sourceIdColumnName).toString())) {
							keys.add(dataMap.get(sourceIdColumnName).toString());
						} else {
							continue;
						}
					}
					dataList.add(dataMap);
					if (totalXY > 0 && totalXY % batchSize == 0) {
						bulkInsertBean.bulkInsert(null, targetConn, tableDefinition, dataList);
						dataList.clear();
						logger.info(tableName + "已经导入记录条数:" + totalXY);
					}
				}
				if (dataList.size() > 0) {
					bulkInsertBean.bulkInsert(null, targetConn, tableDefinition, dataList);
					dataList.clear();
				}
				targetConn.commit();
				logger.info(tableName + "已经成功导入记录总条数:" + totalXY);
			}

			return true;

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("execute sql error", ex);
			throw new RuntimeException(ex);
		} finally {

			JdbcUtils.close(sourceRS);
			JdbcUtils.close(sourcePsmt);
			JdbcUtils.close(sourceConn);

			JdbcUtils.close(targetRS);
			JdbcUtils.close(targetPsmt);
			JdbcUtils.close(targetInsertPsmt);
			JdbcUtils.close(targetConn);
		}
	}

	public Map<String, Object> toMap(ResultSetMetaData rsmd, ResultSet rs) throws SQLException, IOException {
		Map<String, Object> result = new LowerLinkedMap();
		int count = rsmd.getColumnCount();
		for (int i = 1; i <= count; i++) {
			String columnName = rsmd.getColumnName(i);
			columnName = columnName.toLowerCase();
			Object object = rs.getObject(i);
			if (object != null) {
				if (object instanceof java.sql.Clob) {
					java.sql.Clob clob = (java.sql.Clob) object;
					String str = this.clobToString(clob);
					object = str;
				}
			}
			result.put(columnName, object);
		}
		return result;
	}

	public String clobToString(java.sql.Clob clob) throws SQLException, IOException {
		try {
			Reader inStream = clob.getCharacterStream();
			char[] c = new char[(int) clob.length()];
			inStream.read(c);
			String data = new String(c);
			inStream.close();
			return data;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

}
