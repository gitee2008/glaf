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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.core.config.BaseConfiguration;
import com.glaf.core.config.Configuration;
import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.domain.TableDefinition;
import com.glaf.core.jdbc.BulkInsertBean;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.JdbcUtils;
import com.glaf.core.util.LowerLinkedMap;

public class TableToSQLite {
	protected static Configuration conf = BaseConfiguration.create();

	protected static final Log logger = LogFactory.getLog(TableToSQLite.class);

	public TableToSQLite() {

	}

	public boolean execute(String systemName, DataSource targetDataSource, String tableName, int batchSize) {
		Connection sourceConn = null;
		PreparedStatement sourcePsmt = null;
		ResultSet sourceRS = null;
		Connection targetConn = null;
		try {
			List<ColumnDefinition> srcColumns = DBUtils.getColumnDefinitions(systemName, tableName);

			sourceConn = DBConnectionFactory.getConnection(systemName);

			int total = 0;
			sourcePsmt = sourceConn.prepareStatement(" select count(*) from " + tableName);
			sourceRS = sourcePsmt.executeQuery();
			if (sourceRS.next()) {
				total = sourceRS.getInt(1);
			}

			JdbcUtils.close(sourceRS);
			JdbcUtils.close(sourcePsmt);

			if (total > 0) {
				targetConn = targetDataSource.getConnection();
				targetConn.setAutoCommit(false);

				if (!DBUtils.tableExists(targetConn, tableName)) {
					TableDefinition tableDefinition = new TableDefinition();
					tableDefinition.setTableName(tableName);
					tableDefinition.setColumns(srcColumns);

					List<ColumnDefinition> idColumns = new ArrayList<ColumnDefinition>();
					for (ColumnDefinition column : srcColumns) {
						if (column.isPrimaryKey()) {
							idColumns.add(column);
						}
					}

					if (idColumns.size() > 0) {
						if (idColumns.size() == 1) {
							tableDefinition.setIdColumn(idColumns.get(0));
						} else {
							tableDefinition.setIdColumns(idColumns);
						}
					}

					DBUtils.createTable(targetConn, tableDefinition);
				}

				/**
				 * 删除掉目标表数据，再重新抓取
				 */
				String sql = " delete from " + tableName;
				logger.info("execute delete sql:" + sql);
				DBUtils.executeSchemaResourceIgnoreException(targetConn, sql);
				targetConn.commit();

				TableDefinition tableDefinition = new TableDefinition();
				tableDefinition.setTableName(tableName);
				tableDefinition.setColumns(srcColumns);

				int totalXY = 0;
				List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
				BulkInsertBean bulkInsertBean = new BulkInsertBean();
				sourcePsmt = sourceConn.prepareStatement(" select * from " + tableName.toUpperCase());
				sourceRS = sourcePsmt.executeQuery();
				ResultSetMetaData rsmd = sourceRS.getMetaData();
				while (sourceRS.next()) {
					totalXY++;
					dataList.add(this.toMap(rsmd, sourceRS));
					if (totalXY > 0 && totalXY % batchSize == 0) {
						bulkInsertBean.bulkInsert(null, targetConn, tableDefinition, dataList);
						dataList.clear();
						logger.info(tableName + "已经导出记录条数:" + totalXY);
					}
				}
				if (dataList.size() > 0) {
					bulkInsertBean.bulkInsert(null, targetConn, tableDefinition, dataList);
					dataList.clear();
				}
				targetConn.commit();
				logger.info(tableName + "已经成功导出记录总条数:" + totalXY);
			}

			return true;

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("execute export error", ex);
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(sourceRS);
			JdbcUtils.close(sourcePsmt);
			JdbcUtils.close(sourceConn);

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
