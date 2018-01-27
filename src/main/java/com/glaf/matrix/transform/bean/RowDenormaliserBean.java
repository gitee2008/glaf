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

package com.glaf.matrix.transform.bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.domain.Database;
import com.glaf.core.domain.TableDefinition;
import com.glaf.core.entity.SqlExecutor;
import com.glaf.core.jdbc.BulkInsertBean;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.JdbcUtils;
import com.glaf.core.util.LowerLinkedMap;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.StringTools;
import com.glaf.matrix.data.helper.MyBatisHelper;
import com.glaf.matrix.data.util.ExceptionUtils;
import com.glaf.matrix.transform.domain.RowDenormaliser;
import com.glaf.matrix.transform.service.RowDenormaliserService;

public class RowDenormaliserBean {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected long databaseId;

	protected String transformId;

	protected boolean result;

	protected Map<String, Object> parameter;

	public RowDenormaliserBean() {

	}

	public RowDenormaliserBean(long databaseId, String transformId, Map<String, Object> parameter) {
		this.databaseId = databaseId;
		this.transformId = transformId;
		this.parameter = parameter;
	}

	@SuppressWarnings("unchecked")
	public boolean execute(long databaseId, String transformId, Map<String, Object> parameter) {
		IDatabaseService databaseService = ContextFactory.getBean("databaseService");
		RowDenormaliserService rowDenormaliserService = ContextFactory
				.getBean("com.glaf.matrix.transform.service.rowDenormaliserService");
		RowDenormaliser rowDenormaliser = rowDenormaliserService.getRowDenormaliser(transformId);
		String targetTableName = rowDenormaliser.getTargetTableName();
		targetTableName = targetTableName.trim();
		if (!(StringUtils.startsWithIgnoreCase(targetTableName, "etl_")
				|| StringUtils.startsWithIgnoreCase(targetTableName, "sync_")
				|| StringUtils.startsWithIgnoreCase(targetTableName, "tmp")
				|| StringUtils.startsWithIgnoreCase(targetTableName, "useradd_")
				|| StringUtils.startsWithIgnoreCase(targetTableName, "tree_table_"))) {
			logger.warn("目标表必须是以etl_,sync_,useradd_,tmp开头!!!");
			return false;
		}
		Connection conn = null;
		PreparedStatement psmt = null;
		PreparedStatement psmt2 = null;
		SqlExecutor sqlExecutor = null;
		SqlExecutor sqlExecutor2 = null;
		ResultSet rs = null;
		try {
			Database database = databaseService.getDatabaseById(databaseId);
			conn = DBConnectionFactory.getConnection(database.getName());

			TableDefinition tableDefinition = new TableDefinition();
			tableDefinition.setTableName(rowDenormaliser.getTargetTableName());

			List<ColumnDefinition> columns = DBUtils.getColumnDefinitions(conn, rowDenormaliser.getSourceTableName());

			if (!DBUtils.tableExists(conn, rowDenormaliser.getTargetTableName())) {
				ColumnDefinition idColumn = new ColumnDefinition();
				idColumn.setColumnName("EX_ID_");
				idColumn.setJavaType("String");
				idColumn.setLength(64);
				tableDefinition.setIdColumn(idColumn);

				ColumnDefinition col1 = new ColumnDefinition();
				col1.setColumnName("EX_TRANSFORMID_");
				col1.setJavaType("String");
				col1.setLength(50);
				tableDefinition.addColumn(col1);

				for (ColumnDefinition column : columns) {
					if (StringUtils.equalsIgnoreCase(column.getColumnName(), rowDenormaliser.getPrimaryKey())) {
						tableDefinition.addColumn(column);
					}
				}

				ColumnDefinition col2 = new ColumnDefinition();
				col2.setColumnName(rowDenormaliser.getTargetColumn());
				col2.setJavaType(rowDenormaliser.getTargetColumnType());
				col2.setLength(200);
				tableDefinition.addColumn(col2);

				DBUtils.createTable(conn, tableDefinition);
			} else {
				List<ColumnDefinition> columns2 = DBUtils.getColumnDefinitions(conn,
						rowDenormaliser.getTargetTableName());
				tableDefinition.setColumns(columns2);
			}

			List<String> aggregateColumns = StringTools.split(rowDenormaliser.getAggregateColumns());

			List<String> syncColumns = StringTools.split(rowDenormaliser.getSyncColumns());
			Map<String, ColumnDefinition> columnMap = new HashMap<String, ColumnDefinition>();
			for (ColumnDefinition column : columns) {
				columnMap.put(column.getColumnName().toLowerCase(), column);
				if (aggregateColumns.contains(column.getColumnName().toLowerCase())) {
					tableDefinition.addColumn(column);
				} else if (syncColumns.contains(column.getColumnName().toLowerCase())) {
					tableDefinition.addColumn(column);
				} else if (StringUtils.equals(rowDenormaliser.getDateDimensionColumn(),
						column.getColumnName().toLowerCase())) {
					tableDefinition.addColumn(column);
				}
			}

			if (StringUtils.isNotEmpty(rowDenormaliser.getDateDimensionColumn())) {
				ColumnDefinition col2 = new ColumnDefinition();
				col2.setColumnName(rowDenormaliser.getDateDimensionColumn() + "_YYYY");
				col2.setJavaType("Integer");
				tableDefinition.addColumn(col2);

				ColumnDefinition col2x = new ColumnDefinition();
				col2x.setColumnName(rowDenormaliser.getDateDimensionColumn() + "_YYYYMM");
				col2x.setJavaType("Integer");
				tableDefinition.addColumn(col2x);

				ColumnDefinition col2xy = new ColumnDefinition();
				col2xy.setColumnName(rowDenormaliser.getDateDimensionColumn() + "_YYYYMMDD");
				col2xy.setJavaType("Integer");
				tableDefinition.addColumn(col2xy);

				ColumnDefinition col2xyz = new ColumnDefinition();
				col2xyz.setColumnName(rowDenormaliser.getDateDimensionColumn() + "_YYYYMMDDHH");
				col2xyz.setJavaType("Integer");
				tableDefinition.addColumn(col2xyz);

				ColumnDefinition col3 = new ColumnDefinition();
				col3.setColumnName(rowDenormaliser.getDateDimensionColumn() + "_MM");
				col3.setJavaType("Integer");
				tableDefinition.addColumn(col3);

				ColumnDefinition col4 = new ColumnDefinition();
				col4.setColumnName(rowDenormaliser.getDateDimensionColumn() + "_DD");
				col4.setJavaType("Integer");
				tableDefinition.addColumn(col4);

				ColumnDefinition col5 = new ColumnDefinition();
				col5.setColumnName(rowDenormaliser.getDateDimensionColumn() + "_HH");
				col5.setJavaType("Integer");
				tableDefinition.addColumn(col5);

				ColumnDefinition colxyz = new ColumnDefinition();
				colxyz.setColumnName(rowDenormaliser.getTargetColumn() + "_HOUR");
				colxyz.setJavaType("String");
				colxyz.setLength(200);
				tableDefinition.addColumn(colxyz);

			}

			DBUtils.alterTable(conn, tableDefinition);

			ColumnDefinition idColumn = null;
			ColumnDefinition incrementColumn = null;
			for (ColumnDefinition column : columns) {
				if (StringUtils.equalsIgnoreCase(column.getColumnName(), rowDenormaliser.getPrimaryKey())) {
					idColumn = column;
				}
				if (StringUtils.isNotEmpty(rowDenormaliser.getIncrementColumn())
						&& StringUtils.equalsIgnoreCase(column.getColumnName(), rowDenormaliser.getIncrementColumn())) {
					incrementColumn = column;
				}
			}

			if (StringUtils.equals(rowDenormaliser.getDeleteFetch(), "Y")) {
				StringBuilder deleteBuffer = new StringBuilder();
				deleteBuffer.append(" delete from ").append(rowDenormaliser.getTargetTableName())
						.append(" where ex_transformid_ = '").append(rowDenormaliser.getId()).append("' ");

				if (StringUtils.isNotEmpty(rowDenormaliser.getSqlCriteria())) {
					sqlExecutor2 = DBUtils.replaceSQL(rowDenormaliser.getSqlCriteria(), parameter);
					deleteBuffer.append(sqlExecutor2.getSql());
				}

				logger.debug("delete sql: " + deleteBuffer.toString());
				conn.setAutoCommit(false);
				psmt = conn.prepareStatement(deleteBuffer.toString());
				if (sqlExecutor2 != null && sqlExecutor2.getParameter() != null) {
					List<Object> values = (List<Object>) sqlExecutor2.getParameter();
					JdbcUtils.fillStatement(psmt, values);
					logger.debug("values: " + values);
				}
				psmt.executeUpdate();
				JdbcUtils.close(psmt);
				conn.commit();
			}

			StringBuilder buffer = new StringBuilder();
			buffer.append(" select ").append(rowDenormaliser.getPrimaryKey()).append(", ")
					.append(rowDenormaliser.getTransformColumn());
			if (StringUtils.isNotEmpty(rowDenormaliser.getDateDimensionColumn())) {
				buffer.append(", ").append(rowDenormaliser.getDateDimensionColumn());
			}

			for (String syncColumn : syncColumns) {
				if (StringUtils.equalsIgnoreCase(syncColumn, rowDenormaliser.getPrimaryKey())) {
					continue;
				}
				if (StringUtils.equalsIgnoreCase(syncColumn, rowDenormaliser.getDateDimensionColumn())) {
					continue;
				}
				if (StringUtils.equalsIgnoreCase(syncColumn, rowDenormaliser.getTransformColumn())) {
					continue;
				}
				buffer.append(", ").append(syncColumn);
			}

			for (String columnName : aggregateColumns) {
				if (syncColumns.contains(columnName)) {
					continue;
				}
				if (StringUtils.equalsIgnoreCase(columnName, rowDenormaliser.getPrimaryKey())) {
					continue;
				}
				if (StringUtils.equalsIgnoreCase(columnName, rowDenormaliser.getDateDimensionColumn())) {
					continue;
				}
				if (StringUtils.equalsIgnoreCase(columnName, rowDenormaliser.getTransformColumn())) {
					continue;
				}
				buffer.append(", ").append(columnName);
			}

			buffer.append(" from ").append(rowDenormaliser.getSourceTableName()).append(" where 1=1 ");

			if (StringUtils.isNotEmpty(rowDenormaliser.getSqlCriteria())) {
				sqlExecutor = DBUtils.replaceSQL(rowDenormaliser.getSqlCriteria(), parameter);
				buffer.append(sqlExecutor.getSql());
			}

			if (!DBUtils.isLegalQuerySql(buffer.toString())) {
				throw new RuntimeException(" SQL statement illegal ");
			}

			Object incrementValue = null;
			if (incrementColumn != null) {
				StringBuilder bufferxy = new StringBuilder();
				bufferxy.append(" select max(").append(incrementColumn.getColumnName()).append(") from ")
						.append(rowDenormaliser.getTargetTableName());
				logger.debug("max sql: " + bufferxy.toString());
				psmt = conn.prepareStatement(bufferxy.toString());
				rs = psmt.executeQuery();
				if (rs.next()) {
					incrementValue = rs.getObject(1);
				}
				JdbcUtils.close(rs);
				JdbcUtils.close(psmt);
			}

			List<Object> values = null;
			if (sqlExecutor != null && sqlExecutor.getParameter() != null) {
				values = (List<Object>) sqlExecutor.getParameter();
			}
			logger.debug("incrementColumn:" + incrementColumn);
			logger.debug("incrementValue:" + incrementValue);
			if (incrementColumn != null && incrementValue != null) {
				buffer.append(" and ").append(incrementColumn.getColumnName()).append(" > ? ");
				if (values == null) {
					values = new ArrayList<Object>();
				}
				values.add(incrementValue);
			}

			logger.debug("query sql: " + buffer.toString());
			psmt = conn.prepareStatement(buffer.toString());
			if (values != null && !values.isEmpty()) {
				JdbcUtils.fillStatement(psmt, values);
				logger.debug("values: " + values);
			}
			rs = psmt.executeQuery();
			MyBatisHelper helper = new MyBatisHelper();
			List<Map<String, Object>> dataList = helper.getResults(rs);

			JdbcUtils.close(rs);
			JdbcUtils.close(psmt);

			if (dataList != null && !dataList.isEmpty()) {
				List<Map<String, Object>> dataList2 = new ArrayList<Map<String, Object>>();
				for (Map<String, Object> dataMap : dataList) {
					LowerLinkedMap lowCaseMap = new LowerLinkedMap();
					lowCaseMap.putAll(dataMap);
					dataList2.add(lowCaseMap);
				}

				Set<String> keys = new HashSet<String>();

				buffer.delete(0, buffer.length());
				buffer.append(" select ex_id_ from ").append(rowDenormaliser.getTargetTableName());
				psmt = conn.prepareStatement(buffer.toString());
				rs = psmt.executeQuery();
				while (rs.next()) {
					keys.add(rs.getString(1));
				}
				JdbcUtils.close(rs);
				JdbcUtils.close(psmt);

				List<Map<String, Object>> insertList = new ArrayList<Map<String, Object>>();

				int year = 0;
				int month = 0;
				int day = 0;
				int hour = 0;
				Date date = null;
				String key = null;
				String text = null;
				String item = null;
				Date dimensionDate = null;
				ColumnDefinition col = null;
				Map<String, Object> row = null;
				SimpleDateFormat formatter = null;
				java.util.StringTokenizer token = null;

				Calendar calendar = Calendar.getInstance();

				String delimiter = rowDenormaliser.getDelimiter();
				if (StringUtils.isEmpty(delimiter)) {
					delimiter = ",";
				}

				for (Map<String, Object> dataMap : dataList2) {
					buffer.delete(0, buffer.length());
					String str = ParamUtils.getString(dataMap, rowDenormaliser.getTransformColumn());
					if (str == null || str.trim().length() == 0) {
						continue;
					}

					if (StringUtils.isNotEmpty(rowDenormaliser.getDateDimensionColumn())) {
						dimensionDate = ParamUtils.getDate(dataMap, rowDenormaliser.getDateDimensionColumn());
						if (dimensionDate != null) {
							calendar.setTime(dimensionDate);
							year = calendar.get(Calendar.YEAR);
							month = calendar.get(Calendar.MONTH) + 1;
							day = calendar.get(Calendar.DAY_OF_MONTH);
							hour = calendar.get(Calendar.HOUR_OF_DAY);
							dataMap.put(rowDenormaliser.getDateDimensionColumn() + "_yyyy", year);
							dataMap.put(rowDenormaliser.getDateDimensionColumn() + "_mm", month);
							dataMap.put(rowDenormaliser.getDateDimensionColumn() + "_dd", day);
							dataMap.put(rowDenormaliser.getDateDimensionColumn() + "_hh", hour);

							formatter = new SimpleDateFormat("yyyyMM", Locale.getDefault());
							text = formatter.format(dimensionDate);
							dataMap.put(rowDenormaliser.getDateDimensionColumn() + "_yyyymm", Integer.parseInt(text));

							formatter = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
							text = formatter.format(dimensionDate);
							dataMap.put(rowDenormaliser.getDateDimensionColumn() + "_yyyymmdd", Integer.parseInt(text));

							formatter = new SimpleDateFormat("yyyyMMddHH", Locale.getDefault());
							text = formatter.format(dimensionDate);
							dataMap.put(rowDenormaliser.getDateDimensionColumn() + "_yyyymmddhh",
									Integer.parseInt(text));

						}
					}
					token = new StringTokenizer(str, delimiter);
					while (token.hasMoreTokens()) {
						item = token.nextToken();
						if (StringUtils.isNotEmpty(item)) {
							if (dimensionDate != null) {
								formatter = new SimpleDateFormat("yyyyMMddHH", Locale.getDefault());
								text = formatter.format(dimensionDate);
								dataMap.put(rowDenormaliser.getTargetColumn() + "_hour",
										item + "_" + Integer.parseInt(text));
							}

							buffer.append(rowDenormaliser.getId()).append("_")
									.append(ParamUtils.getString(dataMap, rowDenormaliser.getPrimaryKey())).append("_")
									.append(item);
							for (String columnName : aggregateColumns) {
								if (StringUtils.equalsIgnoreCase(columnName, rowDenormaliser.getPrimaryKey())) {
									continue;
								}
								col = columnMap.get(columnName);
								if (col != null) {
									switch (col.getJavaType()) {
									case "Date":
										date = ParamUtils.getDate(dataMap, columnName);
										if (date != null) {
											buffer.append("_").append(date.getTime());
										} else {
											buffer.append("_null");
										}
										break;
									default:
										buffer.append("_").append(ParamUtils.getString(dataMap, columnName));
										break;
									}
								}
							}

							key = DigestUtils.md5Hex(buffer.toString());
							if (!keys.contains(key)) {
								row = new HashMap<String, Object>();
								row.putAll(dataMap);

								switch (idColumn.getJavaType()) {
								case "Integer":
									row.put(idColumn.getColumnName().toLowerCase(),
											ParamUtils.getInt(dataMap, idColumn.getColumnName().toLowerCase()));
									break;
								case "Long":
									row.put(idColumn.getColumnName().toLowerCase(),
											ParamUtils.getLong(dataMap, idColumn.getColumnName().toLowerCase()));
									break;
								case "Double":
									row.put(idColumn.getColumnName().toLowerCase(),
											ParamUtils.getDouble(dataMap, idColumn.getColumnName().toLowerCase()));
									break;
								case "Date":
									row.put(idColumn.getColumnName().toLowerCase(),
											ParamUtils.getDate(dataMap, idColumn.getColumnName().toLowerCase()));
									break;
								default:
									row.put(idColumn.getColumnName().toLowerCase(),
											ParamUtils.getString(dataMap, idColumn.getColumnName().toLowerCase()));
									break;
								}

								switch (rowDenormaliser.getTargetColumnType()) {
								case "Integer":
									row.put(rowDenormaliser.getTargetColumn().toLowerCase(), Integer.parseInt(item));
									break;
								case "Long":
									row.put(rowDenormaliser.getTargetColumn().toLowerCase(), Long.parseLong(item));
									break;
								case "Double":
									row.put(rowDenormaliser.getTargetColumn().toLowerCase(), Double.parseDouble(item));
									break;
								case "Date":
									row.put(rowDenormaliser.getTargetColumn().toLowerCase(), DateUtils.toDate(item));
									break;
								default:
									row.put(rowDenormaliser.getTargetColumn().toLowerCase(), item);
									break;
								}
								row.put("ex_transformid_", transformId);
								row.put("ex_id_", key);
								insertList.add(row);
								keys.add(key);
							}
						}
					}
				}

				logger.debug("待插入记录条数:" + insertList.size());
				BulkInsertBean bulkInsertBean = new BulkInsertBean();
				bulkInsertBean.bulkInsert(null, conn, tableDefinition, insertList);
				conn.commit();
			}

			return true;
		} catch (Exception ex) {
			ExceptionUtils.addMsg("row_denormaliser_" + transformId, ex.getMessage());
			logger.error("execute error", ex);
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(rs);
			JdbcUtils.close(psmt);
			JdbcUtils.close(psmt2);
			JdbcUtils.close(conn);
		}
	}

}
