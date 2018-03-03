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

package com.glaf.heathcare.bean;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.entity.SqlExecutor;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.jdbc.QueryConnectionFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.FieldType;
import com.glaf.core.util.JdbcUtils;
import com.glaf.core.util.QueryUtils;
import com.glaf.heathcare.domain.MealFeeCount;
import com.glaf.heathcare.service.MealFeeCountService;
import com.glaf.matrix.data.domain.AggregationDefinition;
import com.glaf.matrix.data.query.AggregationDefinitionQuery;
import com.glaf.matrix.data.service.AggregationDefinitionService;

public class MealFeeCountBean {
	protected final static Log logger = LogFactory.getLog(MealFeeCountBean.class);

	protected MealFeeCountService mealFeeCountService;

	protected AggregationDefinitionService aggregationDefinitionService;

	public MealFeeCountBean() {

	}

	@SuppressWarnings("unchecked")
	public void execute(LoginContext loginContext, String serviceKey, String type, int year, int semester, int month,
			Map<String, Object> params) {
		AggregationDefinitionQuery query = new AggregationDefinitionQuery();
		query.serviceKey(serviceKey);
		query.type("SQL");
		List<AggregationDefinition> list = getAggregationDefinitionService().list(query);
		if (list != null && !list.isEmpty()) {
			List<Map<String, Object>> datalist = new ArrayList<Map<String, Object>>();
			SqlExecutor sqlExecutor = null;
			Connection conn = null;
			PreparedStatement psmt = null;
			ResultSet rs = null;
			ResultSetMetaData rsmd = null;
			long ts = 0;
			try {
				conn = DBConnectionFactory.getConnection();
				ts = System.currentTimeMillis();
				QueryConnectionFactory.getInstance().register(ts, conn);
				for (AggregationDefinition m : list) {
					if (!DBUtils.isLegalQuerySql(m.getSql())) {
						throw new RuntimeException(" SQL statement illegal ");
					}
					if (!DBUtils.isAllowedSql(m.getSql())) {
						throw new RuntimeException(" SQL statement illegal ");
					}
					sqlExecutor = QueryUtils.replaceSQL(m.getSql(), params);
					psmt = conn.prepareStatement(sqlExecutor.getSql());
					if (sqlExecutor.getParameter() != null) {
						List<Object> values = (List<Object>) sqlExecutor.getParameter();
						JdbcUtils.fillStatement(psmt, values);
					}
					rs = psmt.executeQuery();
					if (StringUtils.equals(m.getResultFlag(), "M")) {
						rsmd = rs.getMetaData();
						int count = rsmd.getColumnCount();
						List<ColumnDefinition> columns = new ArrayList<ColumnDefinition>();
						for (int index = 1; index <= count; index++) {
							int sqlType = rsmd.getColumnType(index);
							ColumnDefinition column = new ColumnDefinition();
							column.setIndex(index);
							column.setColumnLabel(rsmd.getColumnLabel(index));
							column.setJavaType(FieldType.getJavaType(sqlType));
							columns.add(column);
						}

						int startIndex = 0;
						while (rs.next() && startIndex < 5000) {
							int index = 0;
							startIndex++;
							Map<String, Object> rowMap = new LinkedHashMap<String, Object>();
							Iterator<ColumnDefinition> iterator = columns.iterator();
							while (iterator.hasNext()) {
								ColumnDefinition column = iterator.next();
								String columnLabel = column.getColumnLabel();
								String javaType = column.getJavaType();
								index = index + 1;
								if ("String".equals(javaType)) {
									String value = rs.getString(column.getIndex());
									if (value != null) {
										value = value.trim();
										rowMap.put(columnLabel, value);
									} else {
										rowMap.put(columnLabel, value);
									}
								} else if ("Date".equals(javaType)) {
									try {
										rowMap.put(columnLabel, rs.getTimestamp(column.getIndex()));
									} catch (SQLException ex) {
										rowMap.put(columnLabel, rs.getDate(column.getIndex()));
									}
								} else {
									Object value = rs.getObject(column.getIndex());
									if (value != null) {
										if (value instanceof String) {
											value = (String) value.toString().trim();
										}
										rowMap.put(columnLabel, value);
									} else {
										rowMap.put(columnLabel, value);
									}
								}
							}
							datalist.add(rowMap);
						}
					} else {
						if (rs.next()) {
							Map<String, Object> dataMap = new HashMap<String, Object>();
							dataMap.put(m.getName(), rs.getObject(1));
							datalist.add(dataMap);
						}
					}
					JdbcUtils.close(rs);
					JdbcUtils.close(psmt);
				}
			} catch (Exception ex) {
				//ex.printStackTrace();
				logger.error(ex);
			} finally {
				if (conn != null) {
					QueryConnectionFactory.getInstance().unregister(ts, conn);
				}
				JdbcUtils.close(rs);
				JdbcUtils.close(psmt);
				JdbcUtils.close(conn);
			}

			//logger.debug("datalist:" + datalist);
			if (datalist.size() > 0) {
				getMealFeeCountService().delete(loginContext.getTenantId(), year, month, type);
				List<MealFeeCount> insertlist = new ArrayList<MealFeeCount>();
				for (Map<String, Object> dataMap : datalist) {
					Set<Entry<String, Object>> entrySet = dataMap.entrySet();
					for (Entry<String, Object> entry : entrySet) {
						String key = entry.getKey();
						Object value = entry.getValue();
						if (key != null && value != null) {
							if (value instanceof Double) {
								MealFeeCount cnt = new MealFeeCount();
								cnt.setMonth(month);
								cnt.setYear(year);
								cnt.setSemester(semester);
								cnt.setTenantId(loginContext.getTenantId());
								cnt.setType(type);
								cnt.setName(key);
								cnt.setValue((Double) value);
								insertlist.add(cnt);
							} else {
								try {
									double d = Double.parseDouble(value.toString());
									MealFeeCount cnt = new MealFeeCount();
									cnt.setMonth(month);
									cnt.setYear(year);
									cnt.setSemester(semester);
									cnt.setTenantId(loginContext.getTenantId());
									cnt.setType(type);
									cnt.setName(key);
									cnt.setValue(d);
									insertlist.add(cnt);
								} catch (Exception ex) {
									//ex.printStackTrace();
								}
							}
						}
					}
				}
				logger.debug("insertlist:" + insertlist.size());
				getMealFeeCountService().bulkInsert(loginContext.getTenantId(), insertlist);
			}
		}
	}

	public AggregationDefinitionService getAggregationDefinitionService() {
		if (aggregationDefinitionService == null) {
			aggregationDefinitionService = ContextFactory.getBean("aggregationDefinitionService");
		}
		return aggregationDefinitionService;
	}

	public MealFeeCountService getMealFeeCountService() {
		if (mealFeeCountService == null) {
			mealFeeCountService = ContextFactory.getBean("com.glaf.heathcare.service.mealFeeCountService");
		}
		return mealFeeCountService;
	}

	public void setAggregationDefinitionService(AggregationDefinitionService aggregationDefinitionService) {
		this.aggregationDefinitionService = aggregationDefinitionService;
	}

	public void setMealFeeCountService(MealFeeCountService mealFeeCountService) {
		this.mealFeeCountService = mealFeeCountService;
	}

}
