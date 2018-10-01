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

package com.glaf.matrix.export.bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.domain.Database;
import com.glaf.core.el.ExpressionTools;
import com.glaf.core.entity.SqlExecutor;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.JdbcUtils;
import com.glaf.core.util.LowerLinkedMap;
import com.glaf.core.util.QueryUtils;

import com.glaf.matrix.export.domain.XmlExport;
import com.glaf.matrix.export.domain.XmlExportItem;

public class XmlExportDataBean {

	protected static final Log logger = LogFactory.getLog(XmlExportDataBean.class);

	protected IDatabaseService databaseService;

	public IDatabaseService getDatabaseService() {
		if (databaseService == null) {
			databaseService = ContextFactory.getBean("databaseService");
		}
		return databaseService;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getListData(XmlExport xmlExport, Connection srcConn) {
		List<Map<String, Object>> datalist = new ArrayList<Map<String, Object>>();
		Map<String, Object> parameter = new HashMap<String, Object>();
		PreparedStatement srcPsmt = null;
		ResultSet srcRs = null;
		try {
			String sql = xmlExport.getSql();
			if (StringUtils.isNotEmpty(sql) && DBUtils.isLegalQuerySql(sql)) {
				Map<String, Object> params = xmlExport.getParameter();
				parameter.putAll(params);
				SqlExecutor sqlExecutor = QueryUtils.replaceSQL(sql, parameter);
				sql = sqlExecutor.getSql();
				logger.debug("sql:" + sql);
				// logger.debug("params:" + parameter);
				srcPsmt = srcConn.prepareStatement(sql);
				if (sqlExecutor.getParameter() != null) {
					// logger.debug("params:" + parameter);
					logger.debug("parameter:" + sqlExecutor.getParameter());
					List<Object> values = (List<Object>) sqlExecutor.getParameter();
					JdbcUtils.fillStatement(srcPsmt, values);
				}
				srcRs = srcPsmt.executeQuery();
				Map<String, XmlExportItem> itemMap = xmlExport.getItemMap();
				while (srcRs.next()) {
					datalist.add(this.toMap(srcRs, itemMap));
				}
				JdbcUtils.close(srcRs);
				JdbcUtils.close(srcPsmt);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("execute sql query error", ex);
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(srcRs);
			JdbcUtils.close(srcPsmt);
		}
		return datalist;
	}

	public List<Map<String, Object>> getListData(XmlExport xmlExport, Database srcDatabase) {
		Connection srcConn = null;
		try {
			srcConn = DBConnectionFactory.getConnection(srcDatabase.getName());
			return this.getListData(xmlExport, srcConn);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(srcConn);
		}
	}

	public List<Map<String, Object>> getListData(XmlExport xmlExport, long databaseId) {
		Connection srcConn = null;
		try {
			Database srcDatabase = getDatabaseService().getDatabaseById(databaseId);
			srcConn = DBConnectionFactory.getConnection(srcDatabase.getName());
			return this.getListData(xmlExport, srcConn);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(srcConn);
		}
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getMapData(XmlExport xmlExport, Connection srcConn) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		Map<String, Object> parameter = new HashMap<String, Object>();
		PreparedStatement srcPsmt = null;
		ResultSet srcRs = null;
		try {
			String sql = xmlExport.getSql();
			if (StringUtils.isNotEmpty(sql) && DBUtils.isLegalQuerySql(sql)) {
				Map<String, Object> params = xmlExport.getParameter();
				parameter.putAll(params);
				SqlExecutor sqlExecutor = QueryUtils.replaceSQL(sql, parameter);
				sql = sqlExecutor.getSql();
				logger.debug("sql:" + sql);
				// logger.debug("params:" + parameter);
				srcPsmt = srcConn.prepareStatement(sql);
				if (sqlExecutor.getParameter() != null) {
					// logger.debug("params:" + parameter);
					logger.debug("parameter:" + sqlExecutor.getParameter());
					List<Object> values = (List<Object>) sqlExecutor.getParameter();
					JdbcUtils.fillStatement(srcPsmt, values);
				}
				srcRs = srcPsmt.executeQuery();
				dataMap.putAll(params);
				if (srcRs.next()) {
					dataMap.putAll(this.toMap(srcRs, xmlExport.getItemMap()));
				}
				JdbcUtils.close(srcRs);
				JdbcUtils.close(srcPsmt);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("execute sql query error", ex);
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(srcRs);
			JdbcUtils.close(srcPsmt);
		}
		return dataMap;
	}

	public Map<String, Object> getMapData(XmlExport xmlExport, Database srcDatabase) {
		Connection srcConn = null;
		try {
			srcConn = DBConnectionFactory.getConnection(srcDatabase.getName());
			return this.getMapData(xmlExport, srcConn);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(srcConn);
		}
	}

	public Map<String, Object> getMapData(XmlExport xmlExport, long databaseId) {
		Connection srcConn = null;
		try {
			Database srcDatabase = getDatabaseService().getDatabaseById(databaseId);
			srcConn = DBConnectionFactory.getConnection(srcDatabase.getName());
			return this.getMapData(xmlExport, srcConn);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(srcConn);
		}
	}

	public void setDatabaseService(IDatabaseService databaseService) {
		this.databaseService = databaseService;
	}

	public Map<String, Object> toMap(ResultSet rs, Map<String, XmlExportItem> itemMap) throws SQLException {
		Map<String, Object> resultMap = new LowerLinkedMap();
		ResultSetMetaData rsmd = rs.getMetaData();
		DecimalFormat formater1 = new DecimalFormat("###");
		DecimalFormat formater2 = new DecimalFormat("###.##");
		int count = rsmd.getColumnCount();
		for (int i = 1; i <= count; i++) {
			String columnName = rsmd.getColumnLabel(i);
			if (StringUtils.isEmpty(columnName)) {
				columnName = rsmd.getColumnName(i);
			}
			columnName = columnName.toLowerCase();
			Object object = rs.getObject(i);
			if (object != null) {
				if (object instanceof java.util.Date) {
					java.util.Date date = (java.util.Date) object;
					resultMap.put(columnName, DateUtils.getDate(date));
				} else if (object instanceof Integer) {
					int val = (int) object;
					resultMap.put(columnName, formater1.format(val));
				} else if (object instanceof Long) {
					long val = (long) object;
					resultMap.put(columnName, formater1.format(val));
				} else if (object instanceof Double) {
					double val = (double) object;
					resultMap.put(columnName, formater2.format(val));
				} else if (object instanceof java.math.BigInteger) {
					java.math.BigInteger val = (java.math.BigInteger) object;
					resultMap.put(columnName, formater1.format(val.longValue()));
				} else if (object instanceof java.math.BigDecimal) {
					java.math.BigDecimal val = (java.math.BigDecimal) object;
					resultMap.put(columnName, formater2.format(val.doubleValue()));
				} else {
					resultMap.put(columnName, object);
				}
			} else {
				XmlExportItem item = itemMap.get(columnName);
				if (item != null && StringUtils.isNotEmpty(item.getDefaultValue())) {
					resultMap.put(columnName, item.getDefaultValue());
				}
			}
		}

		Set<Entry<String, XmlExportItem>> entrySet = itemMap.entrySet();
		for (Entry<String, XmlExportItem> entry : entrySet) {
			String key = entry.getKey();
			XmlExportItem item = entry.getValue();
			if (resultMap.get(key) == null && StringUtils.isNotEmpty(item.getExpression())) {
				String value = ExpressionTools.evaluate(item.getExpression(), resultMap);
				if (value != null) {
					resultMap.put(key, value);
				}
			}
		}

		return resultMap;
	}

}
