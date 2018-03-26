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
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glaf.core.util.LowerLinkedMap;
import com.glaf.core.config.BaseConfiguration;
import com.glaf.core.config.Configuration;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.domain.Database;
import com.glaf.core.domain.TableDefinition;
import com.glaf.core.el.Mvel2ExpressionEvaluator;
import com.glaf.core.entity.SqlExecutor;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.JdbcUtils;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.StringTools;

import com.glaf.matrix.transform.domain.ColumnTransform;
import com.glaf.matrix.transform.domain.TableTransform;

import com.glaf.matrix.transform.service.TableTransformService;
import com.glaf.matrix.data.helper.MyBatisHelper;
import com.glaf.matrix.data.helper.SqlCriteriaHelper;
import com.glaf.matrix.data.util.ExceptionUtils;

public class TableTransformBean implements java.lang.Runnable {

	protected static Configuration conf = BaseConfiguration.create();

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected SqlCriteriaHelper sqlHelper = new SqlCriteriaHelper();

	protected long databaseId;

	protected String transformId;

	protected boolean result;

	protected Map<String, Object> parameter;

	public TableTransformBean() {

	}

	public TableTransformBean(long databaseId, String transformId, Map<String, Object> parameter) {
		this.databaseId = databaseId;
		this.transformId = transformId;
		this.parameter = parameter;
	}

	@SuppressWarnings("unchecked")
	public boolean execute(long databaseId, String transformId, Map<String, Object> params) {
		IDatabaseService databaseService = ContextFactory.getBean("databaseService");
		TableTransformService tableTransformService = ContextFactory.getBean("tableTransformService");
		int batchSize = conf.getInt("batchSize", 1000);
		StringBuilder buffer = new StringBuilder();

		Connection conn = null;
		PreparedStatement psmt = null;
		PreparedStatement psmt2 = null;
		SqlExecutor sqlExecutor2 = null;
		ResultSet rs = null;
		try {
			TableTransform tableTransform = tableTransformService.getTableTransform(transformId);
			logger.debug("transformId:" + transformId);
			// logger.debug("tableTransform:" + tableTransform);
			String tableName = tableTransform.getTableName();
			buffer.append(" update ").append(tableName).append(" set ");
			sqlExecutor2 = sqlHelper.buildSql("matrix_table_transform", tableName, params);

			Database database = databaseService.getDatabaseById(databaseId);

			List<ColumnDefinition> columns = DBUtils.getColumnDefinitions(database.getName(), tableName);
			List<ColumnTransform> cols = tableTransform.getColumns();
			if (cols != null && !cols.isEmpty()) {
				Collections.sort(cols);
				ColumnDefinition idColumn = null;
				List<String> updateNames = new ArrayList<String>();
				List<String> existNames = new ArrayList<String>();

				for (ColumnDefinition column : columns) {
					if (column.isPrimaryKey()) {
						idColumn = column;
					}
					if (StringUtils.equalsIgnoreCase(column.getColumnName(), tableTransform.getPrimaryKey())) {
						idColumn = column;
					}
					existNames.add(column.getColumnName().toUpperCase());
				}

				if (idColumn == null) {
					ExceptionUtils.addMsg("table_transform_" + tableName, "转换表未设置主键字段");
					return false;
				}

				int index = 1;
				List<ColumnTransform> updateColumns = new ArrayList<ColumnTransform>();
				for (ColumnTransform c : cols) {
					if (!updateNames.contains(c.getTargetColumnName().trim().toLowerCase())) {
						updateNames.add(c.getTargetColumnName().trim().toLowerCase());
						buffer.append(c.getTargetColumnName()).append(" = ?, ");
						c.setPosition(index);
						updateColumns.add(c);
						index++;
					}
				}

				buffer.delete(buffer.length() - 2, buffer.length());
				buffer.append(" where ").append(idColumn.getColumnName()).append(" = ? ");

				for (ColumnTransform ct : cols) {
					if (!existNames.contains(ct.getTargetColumnName().toUpperCase())) {
						ColumnDefinition col = new ColumnDefinition();
						col.setColumnName(ct.getTargetColumnName());
						col.setJavaType(ct.getTargetType());
						col.setTitle(ct.getTitle());
						col.setLength(500);
						columns.add(col);
						existNames.add(ct.getTargetColumnName().toUpperCase());
					}
				}

				TableDefinition tableDefinition = new TableDefinition();
				tableDefinition.setTableName(tableName);
				tableDefinition.setColumns(columns);
				DBUtils.alterTable(database.getName(), tableDefinition);
				idColumn.setPosition(updateColumns.size() + 1);

				for (ColumnTransform cm : cols) {
					cm.setColumnName(cm.getColumnName().toLowerCase());
					cm.setTargetColumnName(cm.getTargetColumnName().toLowerCase());
				}

				String sql = null;

				int maxIndexId = 0;
				int minIndexId = 0;

				conn = DBConnectionFactory.getConnection(database.getName());

				if (StringUtils.equals(idColumn.getJavaType(), "String")) {
					sql = " select count(" + idColumn.getColumnName() + ") from " + tableTransform.getTableName();
					if (StringUtils.isNotEmpty(tableTransform.getSqlCriteria())) {
						sql = sql + " where 1=1 and " + tableTransform.getSqlCriteria();
					}
					if (!DBUtils.isAllowedSql(sql)) {
						throw new RuntimeException(sql + " is not allowed");
					}
					if (!DBUtils.isLegalQuerySql(sql)) {
						throw new RuntimeException(sql + " is not allowed");
					}

					psmt = conn.prepareStatement(sql + sqlExecutor2.getSql());
					if (sqlExecutor2.getParameter() != null) {
						List<Object> values = (List<Object>) sqlExecutor2.getParameter();
						JdbcUtils.fillStatement(psmt, values);
					}
					logger.debug(sql);
					rs = psmt.executeQuery();
				} else {
					sql = " select max(" + idColumn.getColumnName() + "), min(" + idColumn.getColumnName() + ") from "
							+ tableTransform.getTableName();
					if (StringUtils.isNotEmpty(tableTransform.getSqlCriteria())) {
						sql = sql + " where 1=1 and " + tableTransform.getSqlCriteria();
					}
					if (!DBUtils.isAllowedSql(sql)) {
						throw new RuntimeException(sql + " is not allowed");
					}
					if (!DBUtils.isLegalQuerySql(sql)) {
						throw new RuntimeException(sql + " is not allowed");
					}
					psmt = conn.prepareStatement(sql + sqlExecutor2.getSql());
					if (sqlExecutor2.getParameter() != null) {
						List<Object> values = (List<Object>) sqlExecutor2.getParameter();
						JdbcUtils.fillStatement(psmt, values);
					}
					rs = psmt.executeQuery();
				}

				if (rs.next()) {
					maxIndexId = rs.getInt(1);
					if (!StringUtils.equals(idColumn.getJavaType(), "String")) {
						minIndexId = rs.getInt(2);
					}
				}
				JdbcUtils.close(rs);
				JdbcUtils.close(psmt);

				int maxForEach = maxIndexId / batchSize + 1;

				if ((maxIndexId - minIndexId) < batchSize * 500) {
					maxForEach = maxIndexId / batchSize + 1;
				}

				if (StringUtils.equals(idColumn.getJavaType(), "String")) {
					maxForEach = 1;
				}

				if (maxForEach > 500) {
					maxForEach = 500;
				}

				String idColumnName = idColumn.getColumnName().toLowerCase();

				StringBuilder selectBuffer = new StringBuilder();
				if (StringUtils.isNotEmpty(tableTransform.getTransformColumns())) {
					selectBuffer.append(idColumnName).append(", ");
					selectBuffer.append(tableTransform.getTransformColumns());
				} else {
					selectBuffer.append("*");
				}

				for (int i = 0; i < maxForEach; i++) {
					if (StringUtils.equals(idColumn.getJavaType(), "String")) {
						sql = " select " + selectBuffer.toString() + " from " + tableTransform.getTableName();
						if (StringUtils.isNotEmpty(tableTransform.getSqlCriteria())) {
							sql = sql + " where 1=1 and " + tableTransform.getSqlCriteria();
						}
					} else {
						sql = " select " + selectBuffer.toString() + " from " + tableTransform.getTableName()
								+ " where " + idColumnName + " >= " + (minIndexId + i * batchSize) + " and "
								+ idColumnName + " < " + (minIndexId + (i + 1) * batchSize);
						if (StringUtils.isNotEmpty(tableTransform.getSqlCriteria())) {
							sql = sql + " and " + tableTransform.getSqlCriteria();
						}
					}
					logger.debug("query sql:" + sql);
					psmt = conn.prepareStatement(sql + sqlExecutor2.getSql());
					if (sqlExecutor2.getParameter() != null) {
						List<Object> values = (List<Object>) sqlExecutor2.getParameter();
						JdbcUtils.fillStatement(psmt, values);
					}
					rs = psmt.executeQuery();
					MyBatisHelper helper = new MyBatisHelper();
					List<Map<String, Object>> dataList = helper.getResults(rs);

					JdbcUtils.close(rs);
					JdbcUtils.close(psmt);

					if (dataList != null && !dataList.isEmpty()) {
						index = 0;
						String name = null;
						String targetColumn = null;
						String type = null;
						String text = null;
						String condition = null;
						Object objectValue = null;
						Date dateValue = null;
						Boolean boolValue = null;
						String strValue = null;
						int year = 0;
						int month = 0;
						int day = 0;
						int quarter = 0;
						int precision = -1;
						double value = 0;
						SimpleDateFormat formatter = null;
						Calendar calendar = Calendar.getInstance();
						NumberFormat nf = NumberFormat.getNumberInstance();
						logger.debug("update sql:" + buffer.toString());
						conn.setAutoCommit(false);
						psmt2 = conn.prepareStatement(buffer.toString());
						List<Map<String, Object>> dataList2 = new ArrayList<Map<String, Object>>();
						for (Map<String, Object> dataMap : dataList) {
							LowerLinkedMap lowCaseMap = new LowerLinkedMap();
							lowCaseMap.putAll(dataMap);
							// for (ColumnTransform cm : cols) {
							// lowCaseMap.remove(cm.getTargetColumnName());
							// }
							dataList2.add(lowCaseMap);
						}
						for (Map<String, Object> dataMap : dataList2) {
							index++;
							/**
							 * 转换规则，按规则逐列转换，满足一列转换一列，不满足的跳过
							 */
							for (ColumnTransform cm : cols) {
								name = cm.getColumnName().toLowerCase();
								targetColumn = cm.getTargetColumnName().toLowerCase();
								condition = cm.getCondition();

								boolValue = true;
								if (StringUtils.isNotEmpty(cm.getTransformIfTargetColumnNotEmpty())
										&& StringUtils.equals(cm.getTransformIfTargetColumnNotEmpty(), "N")) {
									if (dataMap.get(targetColumn) != null) {
										// dataMap.remove(cm.getColumnName().toLowerCase());
										boolValue = false;
										// logger.debug(targetColumn+"不转换！");
									}
								}
								if (StringUtils.isNotEmpty(condition)) {
									boolValue = false;
									try {
										// logger.debug(condition + "->:" +
										// dataMap);
										objectValue = Mvel2ExpressionEvaluator.evaluate(condition, dataMap);
										if (objectValue != null) {
											if (objectValue instanceof Boolean) {
												boolValue = (Boolean) objectValue;
												if (!boolValue) {
													// logger.debug(condition +
													// "不满足.");
													continue;// 如果不满足条件。
												}
											}
										}
									} catch (Exception ex) {
										// logger.error(condition + " : " +
										// lowCaseMap);
									}
								}
								if (boolValue) {
									if (StringUtils.isNotEmpty(cm.getExpression())) {
										try {
											objectValue = Mvel2ExpressionEvaluator.evaluate(cm.getExpression(),
													dataMap);
											dataMap.put(targetColumn, objectValue);
											dataMap.put(targetColumn.toLowerCase(), objectValue);
										} catch (Exception ex) {
											// logger.error("" + lowCaseMap);
											// logger.error(ex.getMessage());
											// objectValue =
											// Jexl3ExpressionEvaluator.evaluate(cm.getExpression(),
											// lowCaseMap);
											// dataMap.put(target, objectValue);
										}
									} else {
										type = cm.getType();
										if (StringUtils.startsWith(type, "date_")) {
											dateValue = ParamUtils.getDate(dataMap, name);
											if (dateValue != null) {
												calendar.setTime(dateValue);
												year = calendar.get(Calendar.YEAR);
												month = calendar.get(Calendar.MONTH) + 1;
												day = calendar.get(Calendar.DAY_OF_MONTH);
												if (month <= 3) {
													quarter = 1;
												} else if (month > 3 && month <= 6) {
													quarter = 2;
												}
												if (month > 6 && month <= 9) {
													quarter = 3;
												}
												if (month > 9) {
													quarter = 4;
												}
												if (StringUtils.equals("date_yyyyMMddHHmmss", type)) {
													formatter = new SimpleDateFormat("yyyyMMddHHmmss",
															Locale.getDefault());
													text = formatter.format(dateValue);
													dataMap.put(targetColumn, text);
													dataMap.put(targetColumn.toLowerCase(), text);
												} else if (StringUtils.equals("date_yyyyMMdd", type)) {
													formatter = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
													text = formatter.format(dateValue);
													dataMap.put(targetColumn, text);
													dataMap.put(targetColumn.toLowerCase(), text);
												} else if (StringUtils.equals("date_yyyyMM", type)) {
													formatter = new SimpleDateFormat("yyyyMM", Locale.getDefault());
													text = formatter.format(dateValue);
													dataMap.put(targetColumn, text);
													dataMap.put(targetColumn.toLowerCase(), text);
												} else if (StringUtils.equals("date_yyyy", type)) {
													dataMap.put(targetColumn, String.valueOf(year));
													dataMap.put(targetColumn.toLowerCase(), String.valueOf(year));
												} else if (StringUtils.equals("date_MM", type)) {
													dataMap.put(targetColumn, String.valueOf(month));
													dataMap.put(targetColumn.toLowerCase(), String.valueOf(month));
												} else if (StringUtils.equals("date_dd", type)) {
													dataMap.put(targetColumn, String.valueOf(day));
													dataMap.put(targetColumn.toLowerCase(), String.valueOf(day));
												} else if (StringUtils.equals("date_yyyyquarter", type)) {
													dataMap.put(targetColumn, year + "Q" + quarter);
													dataMap.put(targetColumn.toLowerCase(), year + "Q" + quarter);
												} else if (StringUtils.equals("date_quarterQ", type)) {
													dataMap.put(targetColumn, "Q" + quarter);
													dataMap.put(targetColumn.toLowerCase(), "Q" + quarter);
												} else if (StringUtils.equals("date_quarter", type)) {
													dataMap.put(targetColumn, quarter);
													dataMap.put(targetColumn.toLowerCase(), quarter);
												} else {
													dataMap.put(targetColumn, dateValue);
													dataMap.put(targetColumn.toLowerCase(), dateValue);
												}
											}
										} else if (StringUtils.equals(type, "countSplitByComma")) {
											strValue = ParamUtils.getString(dataMap, name);
											if (StringUtils.isNotEmpty(strValue)) {
												int count = 0;
												StringTokenizer token = new StringTokenizer(strValue, ",");
												while (token.hasMoreTokens()) {
													String tmp = token.nextToken();
													if (StringUtils.isNotEmpty(tmp)) {
														count++;
													}
												}
												dataMap.put(targetColumn, count);
												dataMap.put(targetColumn.toLowerCase(), count);
											}
										} else if (StringUtils.equals(type, "removeHtml")) {
											strValue = ParamUtils.getString(dataMap, name);
											if (StringUtils.isNotEmpty(strValue)) {
												String text2 = Jsoup.parse(strValue).text();
												dataMap.put(targetColumn, text2);
												dataMap.put(targetColumn.toLowerCase(), text2);
											}
										} else if (StringUtils.equals(type, "removeHtml2")) {
											strValue = ParamUtils.getString(dataMap, name);
											if (StringUtils.isNotEmpty(strValue)) {
												strValue = StringTools.replaceIgnoreCase(strValue, "<br>", "\n\r");
												strValue = StringTools.replaceIgnoreCase(strValue, "<br/>", "\n\r");
												// logger.debug(strValue);
												String text2 = Jsoup.parse(strValue).text();
												dataMap.put(targetColumn, text2);
												dataMap.put(targetColumn.toLowerCase(), text2);
											}
										} else if (StringUtils.equals(type, "removeBlank")) {
											strValue = ParamUtils.getString(dataMap, name);
											if (StringUtils.isNotEmpty(strValue)) {
												strValue = StringTools.replace(strValue, " ", "");
												strValue = StringTools.replace(strValue, " ", "");
												dataMap.put(targetColumn, strValue);
												dataMap.put(targetColumn.toLowerCase(), strValue);
											}
										} else {
											Object object = ParamUtils.getObject(dataMap, name);
											dataMap.put(targetColumn, object);
											dataMap.put(targetColumn.toLowerCase(), object);
										}
									}
								}
							}
							// logger.debug("dataMap:"+dataMap);
							for (ColumnTransform cm : updateColumns) {
								name = cm.getTargetColumnName().toLowerCase();
								switch (cm.getTargetType()) {
								case "Integer":
									psmt2.setInt(cm.getPosition(), ParamUtils.getInt(dataMap, name));
									break;
								case "Long":
									psmt2.setLong(cm.getPosition(), ParamUtils.getLong(dataMap, name));
									break;
								case "Double":
									precision = cm.getTargetColumnPrecision();
									value = ParamUtils.getDouble(dataMap, name);
									if (precision > 0) {
										nf.setMaximumFractionDigits(precision);
										// value = Double.valueOf(nf.format(value));
										String tmp = nf.format(value);
										tmp = StringTools.replace(tmp, ",", "");
										value = Double.valueOf(tmp);
									}
									psmt2.setDouble(cm.getPosition(), value);
									break;
								case "Date":
									if (ParamUtils.getDate(dataMap, name) != null) {
										psmt2.setTimestamp(cm.getPosition(),
												DateUtils.toTimestamp(ParamUtils.getDate(dataMap, name)));
									} else {
										psmt2.setTimestamp(cm.getPosition(), null);
									}
									break;
								case "String":
									psmt2.setString(cm.getPosition(), ParamUtils.getString(dataMap, name));
									break;
								default:
									psmt2.setObject(cm.getPosition(), dataMap.get(name));
									break;
								}
							}

							switch (idColumn.getJavaType()) {
							case "Integer":
								psmt2.setInt(idColumn.getPosition(), ParamUtils.getInt(dataMap, idColumnName));
								break;
							case "Long":
								psmt2.setLong(idColumn.getPosition(), ParamUtils.getLong(dataMap, idColumnName));
								break;
							case "Double":
								psmt2.setDouble(idColumn.getPosition(), ParamUtils.getDouble(dataMap, idColumnName));
								break;
							default:
								psmt2.setString(idColumn.getPosition(), ParamUtils.getString(dataMap, idColumnName));
								break;
							}
							// 做更新操作
							psmt2.addBatch();
							if (index > 0 && index % batchSize == 0) {
								psmt2.executeBatch();
								// conn.commit();
							}
						}
						psmt2.executeBatch();
						conn.commit();
					}
				}
			}

			return true;
		} catch (Exception ex) {
			ExceptionUtils.addMsg("table_transform_" + transformId, ex.getMessage());
			logger.error("execute error", ex);
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(rs);
			JdbcUtils.close(psmt);
			JdbcUtils.close(psmt2);
			JdbcUtils.close(conn);
		}
	}

	public boolean getResult() {
		return result;
	}

	public void run() {
		result = this.execute(databaseId, transformId, parameter);
	}

}
