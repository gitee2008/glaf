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

package com.glaf.matrix.data.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.entity.SqlExecutor;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.LowerLinkedMap;
import com.glaf.core.util.QueryUtils;

import com.glaf.matrix.data.domain.SqlCriteria;
import com.glaf.matrix.data.service.SqlCriteriaService;

public class SqlCriteriaHelper {
	protected final static Log logger = LogFactory.getLog(SqlCriteriaHelper.class);

	protected SqlCriteriaService sqlCriteriaService;

	public SqlCriteriaHelper() {

	}

	public SqlExecutor buildMyBatisSql(String moduleId, String businessKey, Map<String, Object> params) {
		SqlExecutor sqlExecutor = new SqlExecutor();
		sqlExecutor.setSql("");
		if (params != null && !params.isEmpty()) {
			// SqlCriteriaQuery query = new SqlCriteriaQuery();
			// query.moduleId(moduleId);
			// query.businessKey(businessKey);
			List<SqlCriteria> list = getSqlCriteriaService().getSqlCriterias(businessKey, moduleId);
			if (list != null && !list.isEmpty()) {
				String tmp = null;
				Object value = null;
				String operator = null;
				String condition = null;
				String columnType = null;
				String paramName = null;
				String tableAlias = null;
				StringTokenizer token = null;
				StringBuilder buffer = new StringBuilder();
				Map<String, Object> parameter = new HashMap<String, Object>();
				LowerLinkedMap dataMap = new LowerLinkedMap();
				dataMap.putAll(params);
				int paramIndex = 0;
				for (SqlCriteria m : list) {
					if (m.getParamName() != null) {
						for (int level = 0; level < 10; level++) {
							if (level == m.getLevel()) {
								value = dataMap.get(m.getParamName().toLowerCase());
								if (value != null) {
									tableAlias = m.getTableAlias();
									columnType = m.getColumnType();
									operator = m.getOperator();

									if (StringUtils.isEmpty(condition)) {
										condition = " AND ";
									}

									buffer.append(condition);// 增加连接条件

									/**
									 * 判断是否为集合参数
									 */
									if (StringUtils.equals(m.getCollectionFlag(), "Y")) {
										String separator = ",";
										if (StringUtils.isNotEmpty(m.getSeparator())) {
											separator = m.getSeparator();
										}
										buffer.append(" ");
										if (StringUtils.isNotEmpty(tableAlias)) {
											buffer.append(tableAlias).append(".");
										}
										buffer.append(m.getColumnName()).append(" IN ( ");
										token = new StringTokenizer(value.toString(), separator);
										while (token.hasMoreTokens()) {
											tmp = token.nextToken();
											paramName = "param_" + (String.valueOf(paramIndex++));
											switch (columnType) {
											case "Integer":
												parameter.put(paramName, Integer.parseInt(tmp));
												buffer.append(" #{").append(paramName).append("}, ");
												break;
											case "Long":
												parameter.put(paramName, Long.parseLong(tmp));
												buffer.append(" #{").append(paramName).append("}, ");
												break;
											case "Double":
												parameter.put(paramName, Double.parseDouble(tmp));
												buffer.append(" #{").append(paramName).append("}, ");
												break;
											case "Date":
												parameter.put(paramName, DateUtils.toDate(tmp));
												buffer.append(" #{").append(paramName).append("}, ");
												break;
											case "String":
												parameter.put(paramName, tmp);
												buffer.append(" #{").append(paramName).append("}, ");
												break;
											default:
												parameter.put(paramName, tmp);
												buffer.append(" #{").append(paramName).append("}, ");
												break;
											}
										}
										buffer.delete(buffer.length() - 2, buffer.length());
										buffer.append(" ) ");
									} else {
										paramName = "param_" + (String.valueOf(paramIndex++));
										tmp = value.toString();
										if (StringUtils.isNotEmpty(m.getSql())) {
											// buffer.append(m.getSql());
											String sql = m.getSql();
											sql = QueryUtils.replaceSQLParas(sql, params);// 动态参数替换
											buffer.append(sql);
										} else {
											buffer.append(" ");
											if (StringUtils.isNotEmpty(tableAlias)) {
												buffer.append(tableAlias).append(".");
											}
											if (StringUtils.isEmpty(operator)) {
												operator = "=";
											}
											buffer.append(m.getColumnName()).append(" ").append(operator).append("  #{")
													.append(paramName).append("} ");

											switch (columnType) {
											case "Integer":
												parameter.put(paramName, Integer.parseInt(tmp));
												break;
											case "Long":
												parameter.put(paramName, Long.parseLong(tmp));
												break;
											case "Double":
												parameter.put(paramName, Double.parseDouble(tmp));
												break;
											case "Date":
												parameter.put(paramName, DateUtils.toDate(tmp));
												break;
											case "String":
												if (StringUtils.equalsIgnoreCase(operator, "LIKE")
														|| StringUtils.equalsIgnoreCase(operator, "NOT LIKE")) {
													if (!StringUtils.contains(tmp, "%")) {
														parameter.put(paramName, "%" + tmp + "%");
													} else {
														parameter.put(paramName, tmp);
													}
												} else {
													parameter.put(paramName, tmp);
												}
												break;
											default:
												parameter.put(paramName, tmp);
												break;
											}
										}
									}
								} else {
									/**
									 * 参数必须但如果参数没有值，抛出异常
									 */
									if (StringUtils.equals(m.getRequiredFlag(), "Y")) {
										throw new java.lang.IllegalArgumentException(m.getParamName() + " is null");
									}
								}
								break;
							}
						}
					}
				}
				logger.debug("sql:" + buffer.toString());
				logger.debug("parameter:" + parameter);
				sqlExecutor.setParameter(parameter);
				sqlExecutor.setSql(buffer.toString());
			}
		}
		return sqlExecutor;
	}

	public SqlExecutor buildMyBatisSql(String moduleId, String businessKey, String tableAlias,
			Map<String, Object> params) {
		SqlExecutor sqlExecutor = new SqlExecutor();
		sqlExecutor.setSql("");
		if (params != null && !params.isEmpty()) {
			// SqlCriteriaQuery query = new SqlCriteriaQuery();
			// query.moduleId(moduleId);
			// query.businessKey(businessKey);
			List<SqlCriteria> list = getSqlCriteriaService().getSqlCriterias(businessKey, moduleId);
			if (list != null && !list.isEmpty()) {
				String tmp = null;
				Object value = null;
				String operator = null;
				String condition = null;
				String columnType = null;
				String paramName = null;
				StringTokenizer token = null;
				StringBuilder buffer = new StringBuilder();
				Map<String, Object> parameter = new HashMap<String, Object>();
				LowerLinkedMap dataMap = new LowerLinkedMap();
				dataMap.putAll(params);
				int paramIndex = 0;
				for (SqlCriteria m : list) {
					if (m.getParamName() != null) {
						for (int level = 0; level < 10; level++) {
							if (level == m.getLevel()) {
								value = dataMap.get(m.getParamName().toLowerCase());
								if (value != null) {
									tableAlias = m.getTableAlias();
									columnType = m.getColumnType();
									operator = m.getOperator();

									if (StringUtils.isEmpty(condition)) {
										condition = " AND ";
									}

									buffer.append(condition);// 增加连接条件

									/**
									 * 判断是否为集合参数
									 */
									if (StringUtils.equals(m.getCollectionFlag(), "Y")) {
										String separator = ",";
										if (StringUtils.isNotEmpty(m.getSeparator())) {
											separator = m.getSeparator();
										}
										buffer.append(" ");
										if (StringUtils.isNotEmpty(tableAlias)) {
											buffer.append(tableAlias).append(".");
										}
										buffer.append(m.getColumnName()).append(" IN ( ");
										token = new StringTokenizer(value.toString(), separator);
										while (token.hasMoreTokens()) {
											tmp = token.nextToken();
											paramName = "param_" + (String.valueOf(paramIndex++));
											switch (columnType) {
											case "Integer":
												parameter.put(paramName, Integer.parseInt(tmp));
												buffer.append(" #{").append(paramName).append("}, ");
												break;
											case "Long":
												parameter.put(paramName, Long.parseLong(tmp));
												buffer.append(" #{").append(paramName).append("}, ");
												break;
											case "Double":
												parameter.put(paramName, Double.parseDouble(tmp));
												buffer.append(" #{").append(paramName).append("}, ");
												break;
											case "Date":
												parameter.put(paramName, DateUtils.toDate(tmp));
												buffer.append(" #{").append(paramName).append("}, ");
												break;
											case "String":
												parameter.put(paramName, tmp);
												buffer.append(" #{").append(paramName).append("}, ");
												break;
											default:
												parameter.put(paramName, tmp);
												buffer.append(" #{").append(paramName).append("}, ");
												break;
											}
										}
										buffer.delete(buffer.length() - 2, buffer.length());
										buffer.append(" ) ");
									} else {
										paramName = "param_" + (String.valueOf(paramIndex++));
										tmp = value.toString();
										if (StringUtils.isNotEmpty(m.getSql())) {
											// buffer.append(m.getSql());
											String sql = m.getSql();
											sql = QueryUtils.replaceSQLParas(sql, params);// 动态参数替换
											buffer.append(sql);
										} else {
											buffer.append(" ");
											if (StringUtils.isNotEmpty(tableAlias)) {
												buffer.append(tableAlias).append(".");
											}
											if (StringUtils.isEmpty(operator)) {
												operator = "=";
											}
											buffer.append(m.getColumnName()).append(" ").append(operator).append("  #{")
													.append(paramName).append("} ");

											switch (columnType) {
											case "Integer":
												parameter.put(paramName, Integer.parseInt(tmp));
												break;
											case "Long":
												parameter.put(paramName, Long.parseLong(tmp));
												break;
											case "Double":
												parameter.put(paramName, Double.parseDouble(tmp));
												break;
											case "Date":
												parameter.put(paramName, DateUtils.toDate(tmp));
												break;
											case "String":
												if (StringUtils.equalsIgnoreCase(operator, "LIKE")
														|| StringUtils.equalsIgnoreCase(operator, "NOT LIKE")) {
													if (!StringUtils.contains(tmp, "%")) {
														parameter.put(paramName, "%" + tmp + "%");
													} else {
														parameter.put(paramName, tmp);
													}
												} else {
													parameter.put(paramName, tmp);
												}
												break;
											default:
												parameter.put(paramName, tmp);
												break;
											}
										}
									}
								} else {
									/**
									 * 参数必须但如果参数没有值，抛出异常
									 */
									if (StringUtils.equals(m.getRequiredFlag(), "Y")) {
										throw new java.lang.IllegalArgumentException(m.getParamName() + " is null");
									}
								}
								break;
							}
						}
					}
				}
				logger.debug("sql:" + buffer.toString());
				logger.debug("parameter:" + parameter);
				sqlExecutor.setParameter(parameter);
				sqlExecutor.setSql(buffer.toString());
			}
		}
		return sqlExecutor;
	}

	public SqlExecutor buildSql(String moduleId, String businessKey, Map<String, Object> params) {
		SqlExecutor sqlExecutor = new SqlExecutor();
		sqlExecutor.setSql("");
		if (params != null && !params.isEmpty()) {
			// SqlCriteriaQuery query = new SqlCriteriaQuery();
			// query.moduleId(moduleId);
			// query.businessKey(businessKey);
			List<SqlCriteria> list = getSqlCriteriaService().getSqlCriterias(businessKey, moduleId);
			if (list != null && !list.isEmpty()) {
				String tmp = null;
				Object value = null;
				String operator = null;
				String condition = null;
				String columnType = null;
				String tableAlias = null;
				StringTokenizer token = null;
				StringBuilder buffer = new StringBuilder();
				List<Object> values = new ArrayList<Object>();
				LowerLinkedMap dataMap = new LowerLinkedMap();
				dataMap.putAll(params);
				for (SqlCriteria m : list) {
					if (m.getParamName() != null) {
						for (int level = 0; level < 10; level++) {
							if (level == m.getLevel()) {
								value = dataMap.get(m.getParamName().toLowerCase());
								if (value != null) {
									tableAlias = m.getTableAlias();
									columnType = m.getColumnType();
									operator = m.getOperator();

									if (StringUtils.isEmpty(condition)) {
										condition = " AND ";
									}

									buffer.append(condition);// 增加连接条件

									/**
									 * 判断是否为集合参数
									 */
									if (StringUtils.equals(m.getCollectionFlag(), "Y")) {
										String separator = ",";
										if (StringUtils.isNotEmpty(m.getSeparator())) {
											separator = m.getSeparator();
										}
										buffer.append(" ");
										if (StringUtils.isNotEmpty(tableAlias)) {
											buffer.append(tableAlias).append(".");
										}
										buffer.append(m.getColumnName()).append(" IN ( ");
										token = new StringTokenizer(value.toString(), separator);
										while (token.hasMoreTokens()) {
											tmp = token.nextToken();
											switch (columnType) {
											case "Integer":
												values.add(Integer.parseInt(tmp));
												buffer.append(" ?, ");
												break;
											case "Long":
												values.add(Long.parseLong(tmp));
												buffer.append(" ?, ");
												break;
											case "Double":
												values.add(Double.parseDouble(tmp));
												buffer.append(" ?, ");
												break;
											case "Date":
												values.add(DateUtils.toDate(tmp));
												buffer.append(" ?, ");
												break;
											case "String":
												values.add(tmp);
												buffer.append(" ?, ");
												break;
											default:
												values.add(tmp);
												buffer.append(" ?, ");
												break;
											}
										}
										buffer.delete(buffer.length() - 2, buffer.length());
										buffer.append(" ) ");
									} else {
										tmp = value.toString();
										if (StringUtils.isNotEmpty(m.getSql())) {
											// buffer.append(m.getSql());
											String sql = m.getSql();
											sql = QueryUtils.replaceSQLParas(sql, params);// 动态参数替换
											buffer.append(sql);
										} else {
											buffer.append(" ");
											if (StringUtils.isNotEmpty(tableAlias)) {
												buffer.append(tableAlias).append(".");
											}
											if (StringUtils.isEmpty(operator)) {
												operator = "=";
											}
											buffer.append(m.getColumnName()).append(" ").append(operator).append(" ? ");

											switch (columnType) {
											case "Integer":
												values.add(Integer.parseInt(tmp));
												break;
											case "Long":
												values.add(Long.parseLong(tmp));
												break;
											case "Double":
												values.add(Double.parseDouble(tmp));
												break;
											case "Date":
												values.add(DateUtils.toDate(tmp));
												break;
											case "String":
												if (StringUtils.equalsIgnoreCase(operator, "LIKE")
														|| StringUtils.equalsIgnoreCase(operator, "NOT LIKE")) {
													if (!StringUtils.contains(tmp, "%")) {
														values.add("%" + tmp + "%");
													} else {
														values.add(tmp);
													}
												} else {
													values.add(tmp);
												}
												break;
											default:
												values.add(tmp);
												break;
											}
										}
									}
								} else {
									/**
									 * 参数必须但如果参数没有值，抛出异常
									 */
									if (StringUtils.equals(m.getRequiredFlag(), "Y")) {
										throw new java.lang.IllegalArgumentException(m.getParamName() + " is null");
									}
								}
								break;
							}
						}
					}
				}
				logger.debug("sql:" + buffer.toString());
				logger.debug("values:" + values);
				sqlExecutor.setParameter(values);
				sqlExecutor.setSql(buffer.toString());
			}
		}
		return sqlExecutor;
	}

	public SqlCriteriaService getSqlCriteriaService() {
		if (sqlCriteriaService == null) {
			sqlCriteriaService = ContextFactory.getBean("sqlCriteriaService");
		}
		return sqlCriteriaService;
	}

	public void setSqlCriteriaService(SqlCriteriaService sqlCriteriaService) {
		this.sqlCriteriaService = sqlCriteriaService;
	}

}
