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

package com.glaf.core.jdbc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import com.glaf.core.config.BaseConfiguration;
import com.glaf.core.config.Configuration;
import com.glaf.core.config.DBConfiguration;
import com.glaf.core.dialect.Dialect;
import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.entity.SqlExecutor;
import com.glaf.core.util.CaseInsensitiveHashMap;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.FieldType;
import com.glaf.core.util.JdbcUtils;
import com.glaf.core.util.QueryUtils;
import com.glaf.core.util.StringTools;

public class QueryHelper {
	protected static final Log logger = LogFactory.getLog(QueryHelper.class);

	protected static Configuration conf = BaseConfiguration.create();

	protected static TypeHandlerRegistry typeHandlerRegistry = new TypeHandlerRegistry();

	public final static String newline = System.getProperty("line.separator");

	public QueryHelper() {

	}

	public List<ColumnDefinition> getColumnDefinitions(String sql, Map<String, Object> params) {
		return this.getColumnDefinitions(null, sql, params);
	}

	@SuppressWarnings("unchecked")
	public List<ColumnDefinition> getColumnDefinitions(String systemName, String sql, Map<String, Object> params) {
		if (!DBUtils.isLegalQuerySql(sql)) {
			throw new RuntimeException(" SQL statement illegal ");
		}
		if (!DBUtils.isAllowedSql(sql)) {
			throw new RuntimeException(" SQL statement illegal ");
		}
		long ts = 0;
		SqlExecutor sqlExecutor = QueryUtils.replaceSQL(sql, params);
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		try {
			if (StringUtils.isNotEmpty(systemName)) {
				conn = DBConnectionFactory.getConnection(systemName);
			} else {
				conn = DBConnectionFactory.getConnection();
			}
			ts = System.currentTimeMillis();
			QueryConnectionFactory.getInstance().register(ts, conn);
			psmt = conn.prepareStatement(sqlExecutor.getSql());
			if (sqlExecutor.getParameter() != null) {
				List<Object> values = (List<Object>) sqlExecutor.getParameter();
				JdbcUtils.fillStatement(psmt, values);
			}
			rs = psmt.executeQuery();
			rsmd = rs.getMetaData();
			int count = rsmd.getColumnCount();
			List<ColumnDefinition> columns = new ArrayList<ColumnDefinition>();
			for (int index = 1; index <= count; index++) {
				int sqlType = rsmd.getColumnType(index);
				ColumnDefinition column = new ColumnDefinition();
				column.setIndex(index);
				column.setColumnName(rsmd.getColumnName(index));
				column.setColumnLabel(rsmd.getColumnLabel(index));
				column.setJavaType(FieldType.getJavaType(sqlType));
				column.setPrecision(rsmd.getPrecision(index));
				column.setScale(rsmd.getScale(index));
				if (column.getScale() == 0 && sqlType == Types.NUMERIC) {
					column.setJavaType("Long");
				}
				column.setName(StringTools.camelStyle(column.getColumnLabel().toLowerCase()));
				columns.add(column);
			}
			return columns;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			if (conn != null) {
				QueryConnectionFactory.getInstance().unregister(ts, conn);
			}
			JdbcUtils.close(rs);
			JdbcUtils.close(psmt);
			JdbcUtils.close(conn);
		}
	}

	@SuppressWarnings("unchecked")
	public List<ColumnDefinition> getColumnList(Connection conn, String sql, Map<String, Object> paramMap) {
		if (!DBUtils.isLegalQuerySql(sql)) {
			throw new RuntimeException(" SQL statement illegal ");
		}
		if (!DBUtils.isAllowedSql(sql)) {
			throw new RuntimeException(" SQL statement illegal ");
		}

		PreparedStatement psmt = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		try {
			List<Object> values = null;
			if (paramMap != null) {
				SqlExecutor sqlExecutor = DBUtils.replaceSQL(sql, paramMap);
				sql = sqlExecutor.getSql();
				values = (List<Object>) sqlExecutor.getParameter();
			}

			psmt = conn.prepareStatement(sql);

			if (values != null && !values.isEmpty()) {
				JdbcUtils.fillStatement(psmt, values);
			}

			rs = psmt.executeQuery();

			rsmd = rs.getMetaData();

			int count = rsmd.getColumnCount();
			List<ColumnDefinition> columns = new ArrayList<ColumnDefinition>();

			for (int index = 1; index <= count; index++) {
				int sqlType = rsmd.getColumnType(index);
				ColumnDefinition column = new ColumnDefinition();
				column.setIndex(index);
				column.setColumnName(rsmd.getColumnLabel(index));
				column.setColumnLabel(rsmd.getColumnLabel(index));
				column.setJavaType(FieldType.getJavaType(sqlType));
				column.setPrecision(rsmd.getPrecision(index));
				column.setScale(rsmd.getScale(index));
				if (column.getScale() == 0 && sqlType == Types.NUMERIC) {
					column.setJavaType("Long");
				}
				columns.add(column);
			}

			return columns;
		} catch (Exception ex) {
			logger.error(ex);
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(psmt);
			JdbcUtils.close(rs);
		}
	}

	@SuppressWarnings("unchecked")
	public List<ColumnDefinition> getColumns(Connection conn, String sql, Map<String, Object> paramMap) {
		if (!DBUtils.isLegalQuerySql(sql)) {
			throw new RuntimeException(" SQL statement illegal ");
		}
		if (!DBUtils.isAllowedSql(sql)) {
			throw new RuntimeException(" SQL statement illegal ");
		}
		List<ColumnDefinition> columns = new java.util.ArrayList<ColumnDefinition>();
		PreparedStatement psmt = null;
		ResultSetMetaData rsmd = null;
		ResultSet rs = null;
		try {
			List<Object> values = null;
			if (paramMap != null) {
				SqlExecutor sqlExecutor = QueryUtils.replaceSQL(sql, paramMap);
				sql = sqlExecutor.getSql();
				values = (List<Object>) sqlExecutor.getParameter();
			}

			logger.debug("sql:\n" + sql);
			logger.debug("values:" + values);

			psmt = conn.prepareStatement(sql);

			if (values != null && !values.isEmpty()) {
				JdbcUtils.fillStatement(psmt, values);
			}

			rs = psmt.executeQuery();
			rsmd = rs.getMetaData();
			int count = rsmd.getColumnCount();
			for (int i = 1; i <= count; i++) {
				int sqlType = rsmd.getColumnType(i);
				ColumnDefinition column = new ColumnDefinition();
				column.setColumnLabel(rsmd.getColumnLabel(i));
				column.setColumnName(rsmd.getColumnName(i));
				column.setJavaType(FieldType.getJavaType(sqlType));
				column.setPrecision(rsmd.getPrecision(i));
				column.setScale(rsmd.getScale(i));
				column.setName(StringTools.camelStyle(column.getColumnLabel().toLowerCase()));
				if (column.getScale() == 0 && sqlType == Types.NUMERIC) {
					column.setJavaType("Long");
				}
				if (!columns.contains(column)) {
					columns.add(column);
				}
				logger.debug(column.getColumnName() + " sqlType:" + sqlType + " precision:" + column.getPrecision()
						+ " scale:" + column.getScale());
			}

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(psmt);
			JdbcUtils.close(rs);
		}
		return columns;
	}

	@SuppressWarnings("unchecked")
	public int getCount(Connection conn, String sql, Map<String, Object> params) {
		if (!DBUtils.isLegalQuerySql(sql)) {
			throw new RuntimeException(" SQL statement illegal ");
		}
		if (!DBUtils.isAllowedSql(sql)) {
			throw new RuntimeException(" SQL statement illegal ");
		}
		SqlExecutor sqlExecutor = QueryUtils.replaceSQL(sql, params);
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			psmt = conn.prepareStatement(sqlExecutor.getSql());
			if (sqlExecutor.getParameter() != null) {
				List<Object> values = (List<Object>) sqlExecutor.getParameter();
				JdbcUtils.fillStatement(psmt, values);
			}
			rs = psmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
			return 0;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(rs);
			JdbcUtils.close(psmt);
		}
	}

	public String getInsertScript(Connection conn, String targetTable, String dbType, String sql,
			Map<String, Object> paramMap) {
		List<ColumnDefinition> columns = getColumns(conn, sql, paramMap);
		List<Map<String, Object>> list = getResultList(conn, sql, paramMap);
		StringBuilder buffer = new StringBuilder();
		if (list != null && !list.isEmpty()) {
			Object value = null;
			CaseInsensitiveHashMap lowMap = new CaseInsensitiveHashMap();
			for (Map<String, Object> dataMap : list) {
				lowMap.clear();
				lowMap.putAll(dataMap);
				buffer.append(" insert into ").append(targetTable).append("( ");
				for (ColumnDefinition column : columns) {
					buffer.append(column.getColumnLabel().toLowerCase()).append(", ");
				}
				buffer.delete(buffer.length() - 2, buffer.length());
				buffer.append(") values ( ");
				for (ColumnDefinition column : columns) {
					value = lowMap.get(column.getColumnLabel().toLowerCase());
					if (value != null) {
						if (value instanceof Short) {
							buffer.append(value).append(", ");
						} else if (value instanceof Integer) {
							buffer.append(value).append(", ");
						} else if (value instanceof Long) {
							buffer.append(value).append(", ");
						} else if (value instanceof Double) {
							buffer.append(value).append(", ");
						} else if (value instanceof java.math.BigInteger) {
							buffer.append(value).append(", ");
						} else if (value instanceof java.math.BigDecimal) {
							buffer.append(value).append(", ");
						} else if (value instanceof Date) {
							if (StringUtils.equals(dbType, "oracle")) {
								buffer.append("to_date('").append(DateUtils.getDateTime((Date) value))
										.append("','YYYY-MM-DD HH24:MI:SS'), ");
							} else {
								buffer.append("'").append(DateUtils.getDateTime((Date) value)).append("', ");
							}
						} else {
							String str = StringTools.replaceIgnoreCase(value.toString(), "'", "''");
							str = StringTools.replaceIgnoreCase(str, newline, "\n");
							buffer.append("'").append(value).append("', ");
						}
					} else {
						buffer.append("null, ");
					}
				}
				buffer.delete(buffer.length() - 2, buffer.length());
				buffer.append("); ");
				buffer.append(newline);
			}
		}
		return buffer.toString();
	}

	@SuppressWarnings("unchecked")
	public int getInt(String systemName, String sql, Map<String, Object> paramMap) {
		if (!DBUtils.isLegalQuerySql(sql)) {
			throw new RuntimeException(" SQL statement illegal ");
		}
		if (!DBUtils.isAllowedSql(sql)) {
			throw new RuntimeException(" SQL statement illegal ");
		}
		int ret = 0;
		long ts = 0;
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnectionFactory.getConnection(systemName);
			ts = System.currentTimeMillis();
			QueryConnectionFactory.getInstance().register(ts, conn);
			List<Object> values = null;
			if (paramMap != null) {
				SqlExecutor sqlExecutor = QueryUtils.replaceSQL(sql, paramMap);
				sql = sqlExecutor.getSql();
				values = (List<Object>) sqlExecutor.getParameter();
			}

			sql = DBUtils.removeOrders(sql);

			logger.debug("sql:\n" + sql);
			logger.debug("values:" + values);

			psmt = conn.prepareStatement(sql);

			if (values != null && !values.isEmpty()) {
				JdbcUtils.fillStatement(psmt, values);
			}

			rs = psmt.executeQuery();
			if (rs.next()) {
				ret = rs.getInt(1);
			}
		} catch (Exception ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			if (conn != null) {
				QueryConnectionFactory.getInstance().unregister(ts, conn);
			}
			JdbcUtils.close(rs);
			JdbcUtils.close(psmt);
			JdbcUtils.close(conn);
		}

		return ret;
	}

	/**
	 * @param conn
	 *            数据库连接对象
	 * @param sqlExecutor
	 *            查询对象
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getResultList(Connection conn, SqlExecutor sqlExecutor, int limit) {
		if (!DBUtils.isLegalQuerySql(sqlExecutor.getSql())) {
			throw new RuntimeException(" SQL statement illegal ");
		}
		if (!DBUtils.isAllowedSql(sqlExecutor.getSql())) {
			throw new RuntimeException(" SQL statement illegal ");
		}
		logger.debug("sql:" + sqlExecutor.getSql());
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		try {
			psmt = conn.prepareStatement(sqlExecutor.getSql());
			if (sqlExecutor.getParameter() != null) {
				logger.debug("parameter:" + sqlExecutor.getParameter());
				List<Object> values = (List<Object>) sqlExecutor.getParameter();
				JdbcUtils.fillStatement(psmt, values);
			}

			rs = psmt.executeQuery();

			if (conf.getBoolean("useMyBatisResultHandler", false)) {

				resultList = this.getResults(rs, limit);

			} else {

				rsmd = rs.getMetaData();

				int count = rsmd.getColumnCount();
				List<ColumnDefinition> columns = new ArrayList<ColumnDefinition>();

				for (int index = 1; index <= count; index++) {
					int sqlType = rsmd.getColumnType(index);
					ColumnDefinition column = new ColumnDefinition();
					column.setIndex(index);
					column.setColumnName(rsmd.getColumnName(index));
					column.setColumnLabel(rsmd.getColumnLabel(index));
					column.setJavaType(FieldType.getJavaType(sqlType));
					column.setPrecision(rsmd.getPrecision(index));
					column.setScale(rsmd.getScale(index));
					if (column.getScale() == 0 && sqlType == Types.NUMERIC) {
						column.setJavaType("Long");
					}
					column.setName(StringTools.camelStyle(column.getColumnLabel().toLowerCase()));
					columns.add(column);
				}
				int startIndex = 0;
				while (rs.next() && startIndex < limit) {
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
						} else if ("Blob".equals(javaType)) {
							// ignore
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
					resultList.add(rowMap);
				}
			}

			logger.debug(">resultList size=" + resultList.size());
			return resultList;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(psmt);
			JdbcUtils.close(rs);
		}
	}

	/**
	 * @param conn
	 *            数据库连接对象
	 * @param sqlExecutor
	 *            查询语句
	 * @param start
	 *            开始记录游标，从0开始
	 * @param pageSize
	 *            每页记录数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getResultList(Connection conn, SqlExecutor sqlExecutor, int start, int limit) {
		if (!DBUtils.isLegalQuerySql(sqlExecutor.getSql())) {
			throw new RuntimeException(" SQL statement illegal ");
		}
		if (!DBUtils.isAllowedSql(sqlExecutor.getSql())) {
			throw new RuntimeException(" SQL statement illegal ");
		}
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		String sql = sqlExecutor.getSql();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		boolean supportsPhysicalPage = false;
		try {
			Dialect dialect = DBConfiguration.getDatabaseDialect(conn);
			if (dialect != null && dialect.supportsPhysicalPage()) {
				supportsPhysicalPage = true;
				sql = dialect.getLimitString(sql, start, limit);
				logger.debug("sql=" + sqlExecutor.getSql());
				logger.debug(">>sql=" + sql);
			}

			psmt = conn.prepareStatement(sql);
			if (sqlExecutor.getParameter() != null) {
				List<Object> values = (List<Object>) sqlExecutor.getParameter();
				JdbcUtils.fillStatement(psmt, values);
				logger.debug(">>values=" + values);
			}

			rs = psmt.executeQuery();

			if (conf.getBoolean("useMyBatisResultHandler", false)) {

				resultList = this.getResults(rs, limit);

			} else {
				rsmd = rs.getMetaData();

				int count = rsmd.getColumnCount();
				List<ColumnDefinition> columns = new ArrayList<ColumnDefinition>();

				for (int i = 1; i <= count; i++) {
					int sqlType = rsmd.getColumnType(i);
					ColumnDefinition column = new ColumnDefinition();
					column.setIndex(i);
					column.setColumnName(rsmd.getColumnName(i));
					column.setColumnLabel(rsmd.getColumnLabel(i));
					column.setJavaType(FieldType.getJavaType(sqlType));
					column.setPrecision(rsmd.getPrecision(i));
					column.setScale(rsmd.getScale(i));
					if (column.getScale() == 0 && sqlType == Types.NUMERIC) {
						column.setJavaType("Long");
					}
					column.setName(StringTools.camelStyle(column.getColumnLabel().toLowerCase()));
					columns.add(column);
				}

				if (!supportsPhysicalPage) {
					logger.debug("---------------------skipRows:" + start);
					JdbcUtils.skipRows(rs, start);
				}

				logger.debug("---------------------columns:" + columns.size());
				logger.debug("---------------------start:" + start);
				logger.debug("---------------------limit:" + limit);
				int index = 0;
				while (rs.next() && index < limit) {
					index++;
					// logger.debug("---------------------row index:" + index);

					Map<String, Object> rowMap = new LinkedHashMap<String, Object>();
					Iterator<ColumnDefinition> iterator = columns.iterator();
					while (iterator.hasNext()) {
						ColumnDefinition column = iterator.next();
						String columnLabel = column.getColumnLabel();
						String javaType = column.getJavaType();

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
						} else if ("Blob".equals(javaType)) {

						} else {
							Object value = rs.getObject(column.getIndex());
							if (value != null) {
								if (value instanceof String) {
									value = (String) value.toString().trim();
								}
								rowMap.put(columnLabel, value);
							} else {
								rowMap.put(columnLabel, null);
							}
						}
					}
					resultList.add(rowMap);
				}
			}

			logger.debug(">resultList size = " + resultList.size());
			return resultList;
		} catch (Exception ex) {
			logger.error(ex);

			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(psmt);
			JdbcUtils.close(rs);
		}
	}

	public List<Map<String, Object>> getResultList(Connection conn, String sql, Map<String, Object> paramMap) {
		if (!DBUtils.isLegalQuerySql(sql)) {
			throw new RuntimeException(" SQL statement illegal ");
		}
		if (!DBUtils.isAllowedSql(sql)) {
			throw new RuntimeException(" SQL statement illegal ");
		}
		SqlExecutor sqlExecutor = QueryUtils.replaceSQL(sql, paramMap);
		return this.getResultList(conn, sqlExecutor, 50000);
	}

	public List<Map<String, Object>> getResultList(Connection conn, String sql, Map<String, Object> paramMap,
			int limit) {
		if (!DBUtils.isLegalQuerySql(sql)) {
			throw new RuntimeException(" SQL statement illegal ");
		}
		if (!DBUtils.isAllowedSql(sql)) {
			throw new RuntimeException(" SQL statement illegal ");
		}
		SqlExecutor sqlExecutor = QueryUtils.replaceSQL(sql, paramMap);
		return this.getResultList(conn, sqlExecutor, limit);
	}

	public List<Map<String, Object>> getResultList(SqlExecutor sqlExecutor, int start, int pageSize) {
		long ts = 0;
		Connection conn = null;
		try {
			conn = DBConnectionFactory.getConnection();
			ts = System.currentTimeMillis();
			QueryConnectionFactory.getInstance().register(ts, conn);
			return this.getResultList(conn, sqlExecutor, start, pageSize);
		} catch (Exception ex) {

			throw new RuntimeException(ex);
		} finally {
			if (conn != null) {
				QueryConnectionFactory.getInstance().unregister(ts, conn);
			}
			JdbcUtils.close(conn);
		}
	}

	/**
	 * 根据SQL语句获取结果集
	 * 
	 * @param sql
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getResultList(String sql, Map<String, Object> paramMap) {
		if (!DBUtils.isLegalQuerySql(sql)) {
			throw new RuntimeException(" SQL statement illegal ");
		}
		if (!DBUtils.isAllowedSql(sql)) {
			throw new RuntimeException(" SQL statement illegal ");
		}
		long ts = 0;
		Connection conn = null;
		try {
			conn = DBConnectionFactory.getConnection();
			ts = System.currentTimeMillis();
			QueryConnectionFactory.getInstance().register(ts, conn);
			return this.getResultList(conn, sql, paramMap);
		} catch (Exception ex) {

			throw new RuntimeException(ex);
		} finally {
			if (conn != null) {
				QueryConnectionFactory.getInstance().unregister(ts, conn);
			}
			JdbcUtils.close(conn);
		}
	}

	/**
	 * 根据SQL语句获取指定页码的结果集
	 * 
	 * @param sql
	 * @param start
	 *            ，从0开始
	 * @param pageSize
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getResultList(String sql, Map<String, Object> paramMap, int start, int pageSize) {
		if (!DBUtils.isLegalQuerySql(sql)) {
			throw new RuntimeException(" SQL statement illegal ");
		}
		if (!DBUtils.isAllowedSql(sql)) {
			throw new RuntimeException(" SQL statement illegal ");
		}
		long ts = 0;
		SqlExecutor sqlExecutor = QueryUtils.replaceSQL(sql, paramMap);
		Connection conn = null;
		try {
			conn = DBConnectionFactory.getConnection();
			ts = System.currentTimeMillis();
			QueryConnectionFactory.getInstance().register(ts, conn);
			return this.getResultList(conn, sqlExecutor, start, pageSize);
		} catch (Exception ex) {

			throw new RuntimeException(ex);
		} finally {
			if (conn != null) {
				QueryConnectionFactory.getInstance().unregister(ts, conn);
			}
			JdbcUtils.close(conn);
		}
	}

	/**
	 * 根据SQL语句获取结果集
	 * 
	 * @param systemName
	 * @param sqlExecutor
	 * @return
	 */
	public List<Map<String, Object>> getResultList(String systemName, SqlExecutor sqlExecutor) {
		long ts = 0;
		Connection conn = null;
		try {
			conn = DBConnectionFactory.getConnection(systemName);
			ts = System.currentTimeMillis();
			QueryConnectionFactory.getInstance().register(ts, conn);
			return this.getResultList(conn, sqlExecutor, 50000);
		} catch (Exception ex) {

			throw new RuntimeException(ex);
		} finally {
			if (conn != null) {
				QueryConnectionFactory.getInstance().unregister(ts, conn);
			}
			JdbcUtils.close(conn);
		}
	}

	/**
	 * 根据指定数据源及SQL语句获取结果集
	 * 
	 * @param systemName
	 * @param sql
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getResultList(String systemName, String sql, Map<String, Object> paramMap) {
		if (!DBUtils.isLegalQuerySql(sql)) {
			throw new RuntimeException(" SQL statement illegal ");
		}
		if (!DBUtils.isAllowedSql(sql)) {
			throw new RuntimeException(" SQL statement illegal ");
		}
		long ts = 0;
		Connection conn = null;
		try {
			conn = DBConnectionFactory.getConnection(systemName);
			ts = System.currentTimeMillis();
			logger.debug("connnection:" + conn);
			QueryConnectionFactory.getInstance().register(ts, conn);
			return this.getResultList(conn, sql, paramMap);
		} catch (Exception ex) {

			throw new RuntimeException(ex);
		} finally {
			if (conn != null) {
				QueryConnectionFactory.getInstance().unregister(ts, conn);
			}
			JdbcUtils.close(conn);
		}
	}

	/**
	 * 根据指定数据源及SQL语句获取指定页码的结果集
	 * 
	 * @param systemName
	 * @param sql
	 * @param firstResult
	 *            ，从0开始
	 * @param maxResults
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getResultList(String systemName, String sql, Map<String, Object> paramMap,
			int start, int pageSize) {
		if (!DBUtils.isLegalQuerySql(sql)) {
			throw new RuntimeException(" SQL statement illegal ");
		}
		if (!DBUtils.isAllowedSql(sql)) {
			throw new RuntimeException(" SQL statement illegal ");
		}
		SqlExecutor sqlExecutor = QueryUtils.replaceSQL(sql, paramMap);
		long ts = 0;
		Connection conn = null;
		try {
			conn = DBConnectionFactory.getConnection(systemName);
			ts = System.currentTimeMillis();
			QueryConnectionFactory.getInstance().register(ts, conn);
			return this.getResultList(conn, sqlExecutor, start, pageSize);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			if (conn != null) {
				QueryConnectionFactory.getInstance().unregister(ts, conn);
			}
			JdbcUtils.close(conn);
		}
	}

	public List<Map<String, Object>> getResults(ResultSet rs, int limit) {
		logger.debug("--------------use mybatis results----------------");
		try {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			List<String> columns = new ArrayList<String>();
			List<TypeHandler<?>> typeHandlers = new ArrayList<TypeHandler<?>>();
			ResultSetMetaData rsmd = rs.getMetaData();
			for (int i = 0, n = rsmd.getColumnCount(); i < n; i++) {
				columns.add(rsmd.getColumnLabel(i + 1));
				try {
					Class<?> type = Resources.classForName(rsmd.getColumnClassName(i + 1));
					TypeHandler<?> typeHandler = typeHandlerRegistry.getTypeHandler(type);
					if (typeHandler == null) {
						typeHandler = typeHandlerRegistry.getTypeHandler(Object.class);
					}
					typeHandlers.add(typeHandler);
				} catch (Exception ex) {

					typeHandlers.add(typeHandlerRegistry.getTypeHandler(Object.class));
				}
			}
			int index = 0;
			while (rs.next() && index < limit) {
				index++;
				Map<String, Object> row = new HashMap<String, Object>();
				for (int i = 0, n = columns.size(); i < n; i++) {
					String name = columns.get(i);
					TypeHandler<?> handler = typeHandlers.get(i);
					Object value = handler.getResult(rs, name);
					row.put(name, value);
					if (value != null && value instanceof java.util.Date) {
						java.util.Date date = (java.util.Date) value;
						row.put(name + "_date", DateUtils.getDate(date));
						row.put(name + "_datetime", DateUtils.getDateTime(date));
					}
				}
				list.add(row);
			}
			return list;
		} catch (SQLException ex) {
			logger.error(ex);

			throw new RuntimeException(ex);
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (SQLException e) {
			}
		}
	}

	@SuppressWarnings("unchecked")
	public int getTotal(Connection conn, SqlExecutor sqlExecutor) {
		if (!DBUtils.isLegalQuerySql(sqlExecutor.getSql())) {
			throw new RuntimeException(" SQL statement illegal ");
		}
		if (!DBUtils.isAllowedSql(sqlExecutor.getSql())) {
			throw new RuntimeException(" SQL statement illegal ");
		}
		int total = 0;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			psmt = conn.prepareStatement(sqlExecutor.getSql());
			if (sqlExecutor.getParameter() != null) {
				List<Object> values = (List<Object>) sqlExecutor.getParameter();
				JdbcUtils.fillStatement(psmt, values);
			}

			rs = psmt.executeQuery();
			if (rs.next()) {
				Object object = rs.getObject(1);
				if (object instanceof Integer) {
					Integer iCount = (Integer) object;
					total = iCount.intValue();
				} else if (object instanceof Long) {
					Long iCount = (Long) object;
					total = iCount.intValue();
				} else if (object instanceof BigDecimal) {
					BigDecimal bg = (BigDecimal) object;
					total = bg.intValue();
				} else if (object instanceof BigInteger) {
					BigInteger bi = (BigInteger) object;
					total = bi.intValue();
				} else {
					String x = object.toString();
					if (StringUtils.isNotEmpty(x)) {
						total = Integer.parseInt(x);
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex);

			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(psmt);
			JdbcUtils.close(rs);
		}
		return total;
	}

	@SuppressWarnings("unchecked")
	public int getTotal(Connection conn, String sql, Map<String, Object> paramMap) {
		if (!DBUtils.isLegalQuerySql(sql)) {
			throw new RuntimeException(" SQL statement illegal ");
		}
		if (!DBUtils.isAllowedSql(sql)) {
			throw new RuntimeException(" SQL statement illegal ");
		}
		int total = -1;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			List<Object> values = null;
			if (paramMap != null) {
				SqlExecutor sqlExecutor = QueryUtils.replaceSQL(sql, paramMap);
				sql = sqlExecutor.getSql();
				values = (List<Object>) sqlExecutor.getParameter();
			}

			sql = DBUtils.removeOrders(sql);

			logger.debug("sql:\n" + sql);
			logger.debug("values:" + values);

			psmt = conn.prepareStatement(sql);

			if (values != null && !values.isEmpty()) {
				JdbcUtils.fillStatement(psmt, values);
			}

			rs = psmt.executeQuery();
			if (rs.next()) {
				Object object = rs.getObject(1);
				if (object != null) {
					if (object instanceof Integer) {
						Integer iCount = (Integer) object;
						total = iCount.intValue();
					} else if (object instanceof Long) {
						Long iCount = (Long) object;
						total = iCount.intValue();
					} else if (object instanceof BigDecimal) {
						BigDecimal bg = (BigDecimal) object;
						total = bg.intValue();
					} else if (object instanceof BigInteger) {
						BigInteger bi = (BigInteger) object;
						total = bi.intValue();
					} else {
						String x = object.toString();
						if (StringUtils.isNotEmpty(x)) {
							total = Integer.parseInt(x);
						}
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(psmt);
			JdbcUtils.close(rs);
		}

		return total;
	}

	public int getTotal(SqlExecutor sqlExecutor) {
		long ts = 0;
		Connection conn = null;
		try {
			conn = DBConnectionFactory.getConnection();
			ts = System.currentTimeMillis();
			QueryConnectionFactory.getInstance().register(ts, conn);
			return this.getTotal(conn, sqlExecutor);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			if (conn != null) {
				QueryConnectionFactory.getInstance().unregister(ts, conn);
			}
			JdbcUtils.close(conn);
		}
	}

	public int getTotal(String sql, Map<String, Object> paramMap) {
		long ts = 0;
		SqlExecutor sqlExecutor = QueryUtils.replaceSQL(sql, paramMap);
		Connection conn = null;
		try {
			conn = DBConnectionFactory.getConnection();
			ts = System.currentTimeMillis();
			QueryConnectionFactory.getInstance().register(ts, conn);
			return this.getTotal(conn, sqlExecutor);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			if (conn != null) {
				QueryConnectionFactory.getInstance().unregister(ts, conn);
			}
			JdbcUtils.close(conn);
		}
	}

	public int getTotal(String systemName, SqlExecutor sqlExecutor) {
		long ts = 0;
		Connection conn = null;
		try {
			conn = DBConnectionFactory.getConnection(systemName);
			ts = System.currentTimeMillis();
			QueryConnectionFactory.getInstance().register(ts, conn);
			return this.getTotal(conn, sqlExecutor);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			if (conn != null) {
				QueryConnectionFactory.getInstance().unregister(ts, conn);
			}
			JdbcUtils.close(conn);
		}
	}

	public int getTotal(String systemName, String sql, Map<String, Object> paramMap) {
		long ts = 0;
		SqlExecutor sqlExecutor = QueryUtils.replaceSQL(sql, paramMap);
		Connection conn = null;
		try {
			conn = DBConnectionFactory.getConnection(systemName);
			ts = System.currentTimeMillis();
			QueryConnectionFactory.getInstance().register(ts, conn);
			return this.getTotal(conn, sqlExecutor);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			if (conn != null) {
				QueryConnectionFactory.getInstance().unregister(ts, conn);
			}
			JdbcUtils.close(conn);
		}
	}

	@SuppressWarnings("unchecked")
	public int getTotalRecords(Connection conn, String sql, Map<String, Object> paramMap) {
		if (!DBUtils.isLegalQuerySql(sql)) {
			throw new RuntimeException(" SQL statement illegal ");
		}
		if (!DBUtils.isAllowedSql(sql)) {
			throw new RuntimeException(" SQL statement illegal ");
		}
		int total = 0;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			List<Object> values = null;
			if (paramMap != null) {
				SqlExecutor sqlExecutor = QueryUtils.replaceSQL(sql, paramMap);
				sql = sqlExecutor.getSql();
				values = (List<Object>) sqlExecutor.getParameter();
			}

			sql = DBUtils.removeOrders(sql);

			logger.debug("sql:\n" + sql);
			logger.debug("values:" + values);

			psmt = conn.prepareStatement(sql);

			if (values != null && !values.isEmpty()) {
				JdbcUtils.fillStatement(psmt, values);
			}

			rs = psmt.executeQuery();
			while (rs.next()) {
				total = total + 1;
			}
		} catch (Exception ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(psmt);
			JdbcUtils.close(rs);
		}

		return total;
	}

	@SuppressWarnings("unchecked")
	public int getTotalRecords(String systemName, String sql, Map<String, Object> paramMap) {
		if (!DBUtils.isLegalQuerySql(sql)) {
			throw new RuntimeException(" SQL statement illegal ");
		}
		if (!DBUtils.isAllowedSql(sql)) {
			throw new RuntimeException(" SQL statement illegal ");
		}
		int total = 0;
		long ts = 0;
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnectionFactory.getConnection(systemName);
			ts = System.currentTimeMillis();
			QueryConnectionFactory.getInstance().register(ts, conn);
			List<Object> values = null;
			if (paramMap != null) {
				SqlExecutor sqlExecutor = QueryUtils.replaceSQL(sql, paramMap);
				sql = sqlExecutor.getSql();
				values = (List<Object>) sqlExecutor.getParameter();
			}

			sql = DBUtils.removeOrders(sql);

			logger.debug("sql:\n" + sql);
			logger.debug("values:" + values);

			psmt = conn.prepareStatement(sql);

			if (values != null && !values.isEmpty()) {
				JdbcUtils.fillStatement(psmt, values);
			}

			rs = psmt.executeQuery();
			while (rs.next()) {
				total = total + 1;
			}
		} catch (Exception ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			if (conn != null) {
				QueryConnectionFactory.getInstance().unregister(ts, conn);
			}
			JdbcUtils.close(rs);
			JdbcUtils.close(psmt);
			JdbcUtils.close(conn);
		}

		return total;
	}

	public Map<String, Object> selectOne(Connection conn, SqlExecutor sqlExecutor) {
		List<Map<String, Object>> results = getResultList(conn, sqlExecutor, 1);
		if (results != null && results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * 根据SQL语句获取结果集
	 * 
	 * @param sql
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> selectOne(String sql, Map<String, Object> paramMap) {
		if (!DBUtils.isLegalQuerySql(sql)) {
			throw new RuntimeException(" SQL statement illegal ");
		}
		if (!DBUtils.isAllowedSql(sql)) {
			throw new RuntimeException(" SQL statement illegal ");
		}
		SqlExecutor sqlExecutor = QueryUtils.replaceSQL(sql, paramMap);
		long ts = 0;
		Connection conn = null;
		try {
			conn = DBConnectionFactory.getConnection();
			ts = System.currentTimeMillis();
			QueryConnectionFactory.getInstance().register(ts, conn);
			return this.selectOne(conn, sqlExecutor);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			if (conn != null) {
				QueryConnectionFactory.getInstance().unregister(ts, conn);
			}
			JdbcUtils.close(conn);
		}
	}

	public Map<String, Object> selectOne(String systemName, SqlExecutor sqlExecutor) {
		long ts = 0;
		Connection conn = null;
		try {
			conn = DBConnectionFactory.getConnection(systemName);
			ts = System.currentTimeMillis();
			QueryConnectionFactory.getInstance().register(ts, conn);
			List<Map<String, Object>> results = getResultList(conn, sqlExecutor, 1);
			if (results != null && results.size() > 0) {
				return results.get(0);
			}
			return null;
		} catch (Exception ex) {

			throw new RuntimeException(ex);
		} finally {
			if (conn != null) {
				QueryConnectionFactory.getInstance().unregister(ts, conn);
			}
			JdbcUtils.close(conn);
		}
	}

	/**
	 * 根据SQL语句获取结果集
	 * 
	 * @param sql
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> selectOne(String systemName, String sql, Map<String, Object> paramMap) {
		SqlExecutor sqlExecutor = QueryUtils.replaceSQL(sql, paramMap);
		long ts = 0;
		Connection conn = null;
		try {
			conn = DBConnectionFactory.getConnection(systemName);
			ts = System.currentTimeMillis();
			QueryConnectionFactory.getInstance().register(ts, conn);
			return this.selectOne(conn, sqlExecutor);
		} catch (Exception ex) {

			throw new RuntimeException(ex);
		} finally {
			if (conn != null) {
				QueryConnectionFactory.getInstance().unregister(ts, conn);
			}
			JdbcUtils.close(conn);
		}
	}

	public Map<String, Object> toMap(ResultSet rs) throws SQLException {
		Map<String, Object> result = new CaseInsensitiveHashMap();
		ResultSetMetaData rsmd = rs.getMetaData();
		int count = rsmd.getColumnCount();
		for (int i = 1; i <= count; i++) {
			String columnName = rsmd.getColumnLabel(i);
			if (StringUtils.isEmpty(columnName)) {
				columnName = rsmd.getColumnName(i);
			}
			Object object = rs.getObject(i);
			columnName = columnName.toLowerCase();
			String name = StringTools.camelStyle(columnName);
			result.put(name, object);
			result.put(columnName, object);
		}
		return result;
	}

}