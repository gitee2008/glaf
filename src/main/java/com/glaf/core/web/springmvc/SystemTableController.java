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

package com.glaf.core.web.springmvc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.glaf.core.base.ColumnModel;
import com.glaf.core.base.TableModel;
import com.glaf.core.base.TablePage;
import com.glaf.core.config.BaseConfiguration;
import com.glaf.core.config.Configuration;
import com.glaf.core.config.Environment;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.domain.Database;
import com.glaf.core.domain.SystemParam;
import com.glaf.core.domain.TableDefinition;
import com.glaf.core.entity.hibernate.HibernateBeanFactory;
import com.glaf.core.entity.jpa.EntitySchemaUpdate;
import com.glaf.core.factory.TableFactory;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.jdbc.QueryHelper;
import com.glaf.core.query.TablePageQuery;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.service.ISystemParamService;
import com.glaf.core.service.ITableDataService;
import com.glaf.core.service.ITablePageService;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.FileUtils;
import com.glaf.core.util.JdbcUtils;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.QueryUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.StringTools;

@Controller("/sys/table")
@RequestMapping("/sys/table")
public class SystemTableController {
	protected static final Log logger = LogFactory.getLog(SystemTableController.class);

	protected final static String newline = System.getProperty("line.separator");

	protected static Configuration conf = BaseConfiguration.create();

	protected static AtomicBoolean running = new AtomicBoolean(false);

	protected IDatabaseService databaseService;

	protected ITableDataService tableDataService;

	protected ITablePageService tablePageService;

	protected ISystemParamService systemParamService;

	@RequestMapping("/dataList")
	@ResponseBody
	public byte[] dataList(HttpServletRequest request) {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug(params);

		String tableName = request.getParameter("tableName_enc");
		if (StringUtils.isNotEmpty(tableName)) {
			tableName = RequestUtils.decodeString(tableName);
		} else {
			tableName = request.getParameter("tableName");
		}

		if (StringUtils.startsWithIgnoreCase(tableName, "LOG_")) {
			throw new RuntimeException(tableName + " access deny");
		}

		JSONObject responseJSON = new JSONObject();

		if (!DBUtils.isAllowedTable(tableName)) {
			try {
				return responseJSON.toString().getBytes("UTF-8");
			} catch (IOException e) {
				return responseJSON.toString().getBytes();
			}
		}

		String gridType = ParamUtils.getString(params, "gridType");
		if (gridType == null) {
			gridType = "easyui";
		}

		int start = 0;
		int limit = 10;
		String orderName = null;
		String order = null;
		if ("easyui".equals(gridType)) {
			int pageNo = ParamUtils.getInt(params, "page");
			limit = ParamUtils.getInt(params, "rows");
			start = (pageNo - 1) * limit;
			orderName = ParamUtils.getString(params, "sort");
			order = ParamUtils.getString(params, "order");
		} else if ("extjs".equals(gridType)) {
			start = ParamUtils.getInt(params, "start");
			limit = ParamUtils.getInt(params, "limit");
			orderName = ParamUtils.getString(params, "sort");
			order = ParamUtils.getString(params, "dir");
		} else if ("yui".equals(gridType)) {
			start = ParamUtils.getInt(params, "startIndex");
			limit = ParamUtils.getInt(params, "results");
			orderName = ParamUtils.getString(params, "sort");
			order = ParamUtils.getString(params, "dir");
		}

		if (start < 0) {
			start = 0;
		}

		if (limit <= 0 || limit > Paging.MAX_RECORD_COUNT) {
			limit = Paging.MAX_RECORD_COUNT;
		}

		tableName = tableName.toUpperCase();

		Collection<String> rejects = new java.util.ArrayList<String>();

		rejects.add("SYS_USER");
		rejects.add("SYS_ROLE");
		rejects.add("DATA_FILE");
		rejects.add("SYS_MAIL_FILE");
		rejects.add("SYS_DBID");
		rejects.add("SYS_PROPERTY");
		rejects.add("SYS_DATABASE");
		rejects.add("SYS_SERVER");
		rejects.add("SYS_KEY");

		if (conf.get("table.rejects") != null) {
			String str = conf.get("table.rejects");
			List<String> list = StringTools.split(str);
			for (String t : list) {
				rejects.add(t.toUpperCase());
			}
		}

		TablePage tablePage = null;
		TablePageQuery tablePageQuery = new TablePageQuery();
		tablePageQuery.tableName(tableName);
		tablePageQuery.setFirstResult(start);
		tablePageQuery.setMaxResults(limit);

		if (orderName != null && orderName.trim().length() > 0) {
			if (StringUtils.equals(order, "asc")) {
				tablePageQuery.orderAsc(orderName);
			} else {
				tablePageQuery.orderDesc(orderName);
			}
		}

		if (!rejects.contains(tableName)) {
			String systemName = request.getParameter("systemName");
			String currentName = Environment.getCurrentSystemName();
			try {
				long startTs = System.currentTimeMillis();
				if (StringUtils.isNotEmpty(systemName)) {
					Environment.setCurrentSystemName(systemName);
				}
				tablePage = tablePageService.getTablePage(tablePageQuery, start, limit);
				long time = System.currentTimeMillis() - startTs;
				logger.debug("查询完成,记录总数:" + tablePage.getTotal() + " 用时(毫秒):" + time);
			} catch (Exception ex) {
				// ex.printStackTrace();
				logger.error(ex);
			} finally {
				Environment.setCurrentSystemName(currentName);
			}
		}

		responseJSON.put("total", 0);
		JSONArray rowsJSON = new JSONArray();
		if (tablePage != null && tablePage.getRows() != null) {
			responseJSON.put("total", tablePage.getTotal());
			for (Map<String, Object> dataMap : tablePage.getRows()) {
				JSONObject rowJSON = new JSONObject();
				Iterator<String> iterator = dataMap.keySet().iterator();
				while (iterator.hasNext()) {
					String name = (String) iterator.next();
					if (StringUtils.equalsIgnoreCase("password", name) || StringUtils.equalsIgnoreCase("pwd", name)) {
						continue;
					}
					Object value = dataMap.get(name);
					if (value != null) {
						if (value instanceof Date) {
							Date date = (Date) value;
							rowJSON.put(name, DateUtils.getDate(date));
							rowJSON.put(name.toLowerCase(), DateUtils.getDate(date));
						} else if (value instanceof byte[]) {
							rowJSON.put(name, "二进制流");
						} else if (value instanceof java.io.InputStream) {
							rowJSON.put(name, "二进制流");
						} else if (value instanceof java.sql.Blob) {
							rowJSON.put(name, "二进制流");
						} else if (value instanceof java.sql.Clob) {
							rowJSON.put(name, "长文本");
						} else {
							rowJSON.put(name, value);
							rowJSON.put(name.toLowerCase(), value);
						}
					}
				}
				rowJSON.put("startIndex", ++start);
				rowsJSON.add(rowJSON);
				dataMap.clear();
			}
		}

		if ("yui".equals(gridType)) {
			responseJSON.put("records", rowsJSON);
		} else {
			responseJSON.put("rows", rowsJSON);
		}

		try {
			return responseJSON.toString().getBytes("UTF-8");
		} catch (IOException e) {
			return responseJSON.toString().getBytes();
		}
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String tableName = request.getParameter("tableName_enc");
		if (StringUtils.isNotEmpty(tableName)) {
			tableName = RequestUtils.decodeString(tableName);
		} else {
			tableName = request.getParameter("tableName");
		}
		String businessKey = request.getParameter("businessKey");
		String primaryKey = null;
		ColumnDefinition idColumn = null;
		List<ColumnDefinition> columns = null;
		String systemName = request.getParameter("systemName");
		String currentName = Environment.getCurrentSystemName();
		if (StringUtils.isEmpty(systemName)) {
			systemName = Environment.DEFAULT_SYSTEM_NAME;
		}
		try {
			if (StringUtils.isNotEmpty(tableName)) {
				columns = TableFactory.getColumnDefinitions(systemName, tableName);
				modelMap.put("tableName", tableName);
				modelMap.put("tableName_enc", RequestUtils.encodeString(tableName));
				List<String> pks = DBUtils.getPrimaryKeys(systemName, tableName);
				if (pks != null && !pks.isEmpty()) {
					if (pks.size() == 1) {
						primaryKey = pks.get(0);
					}
				}
				if (primaryKey != null) {
					for (ColumnDefinition column : columns) {
						if (StringUtils.equalsIgnoreCase(primaryKey, column.getColumnName())) {
							idColumn = column;
							break;
						}
					}
				}
				if (idColumn != null) {
					TableModel tableModel = new TableModel();
					tableModel.setTableName(tableName);
					ColumnModel idCol = new ColumnModel();
					idCol.setColumnName(idColumn.getColumnName());
					idCol.setJavaType(idColumn.getJavaType());
					if ("Integer".equals(idColumn.getJavaType())) {
						idCol.setValue(Integer.parseInt(businessKey));
					} else if ("Long".equals(idColumn.getJavaType())) {
						idCol.setValue(Long.parseLong(businessKey));
					} else {
						idCol.setValue(businessKey);
					}
					tableModel.setIdColumn(idCol);
					Environment.setCurrentSystemName(systemName);
					Map<String, Object> dataMap = tableDataService.getTableDataByPrimaryKey(tableModel);
					Map<String, Object> rowMap = QueryUtils.lowerKeyMap(dataMap);
					for (ColumnDefinition column : columns) {
						Object value = rowMap.get(column.getColumnName().toLowerCase());
						column.setValue(value);
					}
					modelMap.put("idColumn", idColumn);
					modelMap.put("columns", columns);
				}
			}
		} catch (Exception ex) {

			logger.error(ex);
		} finally {
			Environment.setCurrentSystemName(currentName);
		}

		String x_view = ViewProperties.getString("sys_table.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/table/edit", modelMap);
	}

	@ResponseBody
	@RequestMapping("/exportAllInsertScripts")
	public void exportAllInsertScripts(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String dbType = request.getParameter("dbType");
		if (StringUtils.isNotEmpty(dbType)) {
			Map<String, Object> params = RequestUtils.getParameterMap(request);
			Database database = null;
			java.sql.Connection conn = null;
			java.sql.PreparedStatement psmt = null;
			java.sql.ResultSet rs = null;
			QueryHelper helper = new QueryHelper();
			StringBuilder buffer = new StringBuilder();
			try {
				if (StringUtils.isNotEmpty(request.getParameter("databaseId"))) {
					database = databaseService.getDatabaseById(RequestUtils.getLong(request, "databaseId"));
				}
				if (database != null) {
					conn = DBConnectionFactory.getConnection(database.getName());
				} else {
					conn = DBConnectionFactory.getConnection();
				}
				List<String> tableList = DBUtils.getTables(conn);
				for (String tableName : tableList) {
					if (tableName.toLowerCase().startsWith("act_")) {
						continue;
					}
					if (tableName.toLowerCase().startsWith("jbpm_")) {
						continue;
					}
					if (tableName.toLowerCase().startsWith("sys_identity_token")) {
						continue;
					}
					if (tableName.toLowerCase().startsWith("sys_database")) {
						continue;
					}
					if (tableName.toLowerCase().startsWith("sys_server")) {
						continue;
					}
					if (tableName.toLowerCase().startsWith("sys_key")) {
						continue;
					}
					if (tableName.toLowerCase().startsWith("sys_lob")) {
						continue;
					}
					if (tableName.toLowerCase().endsWith("_log")) {
						continue;
					}
					if (tableName.toLowerCase().endsWith("_history")) {
						continue;
					}

					psmt = conn.prepareStatement(" select count(*) from " + tableName);
					rs = psmt.executeQuery();
					if (rs.next() && rs.getInt(1) > 0) {
						buffer.append(newline);
						buffer.append(
								helper.getInsertScript(conn, tableName, dbType, " select * from " + tableName, params));
						buffer.append(newline);
						buffer.append(newline);
					}
					JdbcUtils.close(rs);
					JdbcUtils.close(psmt);
				}
				JdbcUtils.close(conn);
				ResponseUtils.download(request, response, buffer.toString().getBytes("UTF-8"),
						"insert_all_" + dbType + "_" + DateUtils.getNowYearMonthDayHHmm() + ".sql");
			} catch (Exception ex) {
				// ex.printStackTrace();
				logger.error(ex);
			} finally {
				JdbcUtils.close(rs);
				JdbcUtils.close(psmt);
				JdbcUtils.close(conn);
			}
		}
	}

	@ResponseBody
	@RequestMapping("/exportData")
	public void exportData(HttpServletRequest request, HttpServletResponse response) throws IOException {
		StringBuilder sb = new StringBuilder();
		String tables = request.getParameter("exportTables");
		String dbType = request.getParameter("dbType");
		if (StringUtils.isNotEmpty(dbType) && StringUtils.isNotEmpty(tables)) {
			List<String> list = StringTools.split(tables);
			for (String tablename : list) {
				if (StringUtils.isNotEmpty(tablename)) {
					if (StringUtils.endsWithIgnoreCase(tablename, "log")) {
						continue;
					}
					logger.debug("process table:" + tablename);
					List<ColumnDefinition> columns = TableFactory.getColumnDefinitions(tablename);
					TablePageQuery query = new TablePageQuery();
					query.tableName(tablename);
					query.firstResult(0);
					query.maxResults(20000);
					int count = tablePageService.getTableCount(query);
					if (count <= 20000) {
						List<Map<String, Object>> rows = tablePageService.getTableData(query);
						if (rows != null && !rows.isEmpty()) {
							for (Map<String, Object> dataMap : rows) {
								Map<String, Object> lowerMap = QueryUtils.lowerKeyMap(dataMap);
								if ("hbase".equals(dbType)) {
									sb.append(" upsert into ").append(tablename).append(" (");
								} else {
									sb.append(" insert into ").append(tablename).append(" (");
								}
								for (int i = 0, len = columns.size(); i < len; i++) {
									ColumnDefinition column = columns.get(i);
									sb.append(column.getColumnName().toLowerCase());
									if (i < columns.size() - 1) {
										sb.append(", ");
									}
								}
								sb.append(" ) values (");
								for (int i = 0, len = columns.size(); i < len; i++) {
									ColumnDefinition column = columns.get(i);
									Object value = lowerMap.get(column.getColumnName().toLowerCase());
									if (value != null) {
										if (value instanceof Short) {
											sb.append(value);
										} else if (value instanceof Integer) {
											sb.append(value);
										} else if (value instanceof Long) {
											sb.append(value);
										} else if (value instanceof Double) {
											sb.append(value);
										} else if (value instanceof String) {
											String str = (String) value;
											str = StringTools.replace(str, "'", "''");
											sb.append("'").append(str).append("'");
										} else if (value instanceof Date) {
											Date date = (Date) value;
											if (StringUtils.equalsIgnoreCase(dbType, "oracle")) {
												sb.append(" to_date('").append(DateUtils.getDateTime(date))
														.append("', 'yyyy-mm-dd hh24:mi:ss')");
											} else if (StringUtils.equalsIgnoreCase(dbType, "db2")) {
												sb.append(" TO_DATE('").append(DateUtils.getDateTime(date))
														.append("', ''YYY-MM-DD HH24:MI:SS')");
											} else {
												sb.append("'").append(DateUtils.getDateTime(date)).append("'");
											}
										} else {
											String str = value.toString();
											str = StringTools.replace(str, "'", "''");
											sb.append("'").append(str).append("'");
										}
									} else {
										sb.append("null");
									}
									if (i < columns.size() - 1) {
										sb.append(", ");
									}
								}
								sb.append(");");
								sb.append(FileUtils.newline);
							}
						}
					}
					sb.append(FileUtils.newline);
					sb.append(FileUtils.newline);
				}
			}
		}

		try {
			ResponseUtils.download(request, response, sb.toString().getBytes(),
					"insert_" + DateUtils.getNowYearMonthDayHHmm() + "." + dbType + ".sql");
		} catch (ServletException ex) {

		}
	}

	@ResponseBody
	@RequestMapping("/exportInsertScripts")
	public void exportInsertScripts(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String tables = request.getParameter("tables");
		String dbType = request.getParameter("dbType");
		if (StringUtils.isNotEmpty(dbType) && StringUtils.isNotEmpty(tables)) {
			Map<String, Object> params = RequestUtils.getParameterMap(request);
			List<String> tableList = StringTools.split(tables);
			Database database = null;
			java.sql.Connection conn = null;
			java.sql.PreparedStatement psmt = null;
			java.sql.ResultSet rs = null;
			QueryHelper helper = new QueryHelper();
			StringBuilder buffer = new StringBuilder();
			try {
				if (StringUtils.isNotEmpty(request.getParameter("databaseId"))) {
					database = databaseService.getDatabaseById(RequestUtils.getLong(request, "databaseId"));
				}
				if (database != null) {
					conn = DBConnectionFactory.getConnection(database.getName());
				} else {
					conn = DBConnectionFactory.getConnection();
				}
				for (String tableName : tableList) {
					psmt = conn.prepareStatement(" select count(*) from " + tableName);
					rs = psmt.executeQuery();
					if (rs.next() && rs.getInt(1) > 0) {
						buffer.append(newline);
						buffer.append(
								helper.getInsertScript(conn, tableName, dbType, " select * from " + tableName, params));
						buffer.append(newline);
						buffer.append(newline);
					}
					JdbcUtils.close(rs);
					JdbcUtils.close(psmt);
				}
				JdbcUtils.close(conn);
				ResponseUtils.download(request, response, buffer.toString().getBytes("UTF-8"),
						"insert_" + dbType + "_" + DateUtils.getNowYearMonthDayHHmm() + ".sql");
			} catch (Exception ex) {
				// ex.printStackTrace();
				logger.error(ex);
			} finally {
				JdbcUtils.close(rs);
				JdbcUtils.close(psmt);
				JdbcUtils.close(conn);
			}
		}
	}

	@ResponseBody
	@RequestMapping("/exportSysTables")
	public void exportSysTables(HttpServletRequest request, HttpServletResponse response) throws IOException {
		StringBuilder buffer = new StringBuilder();
		StringBuilder sb = new StringBuilder();
		JSONArray result = null;
		SystemParam param = systemParamService.getSystemParam("sys_table");
		if (param != null && StringUtils.isNotEmpty(param.getTextVal())) {
			result = JSON.parseArray(param.getTextVal());
		}
		String dbType = request.getParameter("dbType");
		if (StringUtils.isNotEmpty(dbType) && result != null) {
			for (int index = 0, len = result.size(); index < len; index++) {
				JSONObject json = result.getJSONObject(index);
				String tablename = json.getString("tablename");
				if (StringUtils.isNotEmpty(tablename)) {
					logger.debug("process table:" + tablename);
					List<ColumnDefinition> columns = TableFactory.getColumnDefinitions(tablename);
					TableDefinition tableDefinition = new TableDefinition();
					tableDefinition.setTableName(tablename);
					tableDefinition.setColumns(columns);
					for (ColumnDefinition column : columns) {
						if (column.isPrimaryKey()) {
							tableDefinition.setIdColumn(column);
							String sql = DBUtils.getCreateTableScript(dbType, tableDefinition);
							buffer.append(FileUtils.newline).append(sql).append(FileUtils.newline)
									.append(FileUtils.newline);
						}
					}
					TablePageQuery query = new TablePageQuery();
					query.tableName(tablename);
					query.firstResult(0);
					query.maxResults(5000);
					int count = tablePageService.getTableCount(query);
					if (count <= 5000) {
						List<Map<String, Object>> rows = tablePageService.getTableData(query);
						if (rows != null && !rows.isEmpty()) {
							for (Map<String, Object> dataMap : rows) {
								Map<String, Object> lowerMap = QueryUtils.lowerKeyMap(dataMap);
								if ("hbase".equals(dbType)) {
									sb.append(" upsert into ").append(tablename).append(" (");
								} else {
									sb.append(" insert into ").append(tablename).append(" (");
								}
								for (int i = 0, len2 = columns.size(); i < len2; i++) {
									ColumnDefinition column = columns.get(i);
									sb.append(column.getColumnName().toLowerCase());
									if (i < columns.size() - 1) {
										sb.append(", ");
									}
								}
								sb.append(" ) values (");
								for (int i = 0, len2 = columns.size(); i < len2; i++) {
									ColumnDefinition column = columns.get(i);
									Object value = lowerMap.get(column.getColumnName().toLowerCase());
									if (value != null) {
										if (value instanceof Short) {
											sb.append(value);
										} else if (value instanceof Integer) {
											sb.append(value);
										} else if (value instanceof Long) {
											sb.append(value);
										} else if (value instanceof Double) {
											sb.append(value);
										} else if (value instanceof String) {
											String str = (String) value;
											str = StringTools.replace(str, "'", "''");
											sb.append("'").append(str).append("'");
										} else if (value instanceof Date) {
											Date date = (Date) value;
											if (StringUtils.equalsIgnoreCase(dbType, "oracle")) {
												sb.append(" to_date('").append(DateUtils.getDateTime(date))
														.append("', 'yyyy-mm-dd hh24:mi:ss')");
											} else if (StringUtils.equalsIgnoreCase(dbType, "db2")) {
												sb.append(" TO_DATE('").append(DateUtils.getDateTime(date))
														.append("', ''YYY-MM-DD HH24:MI:SS')");
											} else {
												sb.append("'").append(DateUtils.getDateTime(date)).append("'");
											}
										} else {
											String str = value.toString();
											str = StringTools.replace(str, "'", "''");
											sb.append("'").append(str).append("'");
										}
									} else {
										sb.append("null");
									}
									if (i < columns.size() - 1) {
										sb.append(", ");
									}
								}
								sb.append(");");
								sb.append(FileUtils.newline);
							}
						}
					}
					sb.append(FileUtils.newline);
					sb.append(FileUtils.newline);
				}
			}
		}

		buffer.append(FileUtils.newline);
		buffer.append(FileUtils.newline);
		sb.insert(0, buffer.toString());
		try {
			ResponseUtils.download(request, response, sb.toString().getBytes(),
					"create_insert_sys_" + DateUtils.getNowYearMonthDayHHmm() + "." + dbType + ".sql");
		} catch (ServletException ex) {

		}
	}

	@ResponseBody
	@RequestMapping("/genCreateScripts")
	public void genCreateScripts(HttpServletRequest request, HttpServletResponse response) throws IOException {
		StringBuilder sb = new StringBuilder(8192);
		String tableNames = request.getParameter("tables");
		String dbType = request.getParameter("dbType");
		if (StringUtils.isNotEmpty(dbType) && StringUtils.isNotEmpty(tableNames)) {
			List<String> list = StringTools.split(tableNames);
			for (String tableName : list) {
				List<ColumnDefinition> columns = DBUtils.getColumnDefinitions(tableName);
				TableDefinition tableDefinition = new TableDefinition();
				tableDefinition.setTableName(tableName);
				tableDefinition.setColumns(columns);
				List<ColumnDefinition> idColumns = new ArrayList<ColumnDefinition>();
				for (ColumnDefinition column : columns) {
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

				String sql = DBUtils.getCreateTableScript(dbType, tableDefinition);
				sb.append(FileUtils.newline).append(sql).append(";").append(FileUtils.newline)
						.append(FileUtils.newline);
			}
		}

		try {
			ResponseUtils.download(request, response, sb.toString().getBytes(),
					"createTable_" + DateUtils.getNowYearMonthDayHHmm() + "." + dbType + ".sql");
		} catch (ServletException ex) {
			ex.printStackTrace();
		}
	}

	@RequestMapping("/initHB")
	public ModelAndView initHB(HttpServletRequest request, ModelMap modelMap) {
		return new ModelAndView("/sys/table/initHB", modelMap);
	}

	@ResponseBody
	@RequestMapping("/json")
	public byte[] json(HttpServletRequest request) throws IOException {
		JSONObject result = new JSONObject();
		JSONArray rowsJSON = new JSONArray();
		String[] types = { "TABLE" };
		Connection connection = null;
		try {
			connection = DBConnectionFactory.getConnection();
			DatabaseMetaData metaData = connection.getMetaData();
			ResultSet rs = metaData.getTables(null, null, null, types);
			int startIndex = 1;
			while (rs.next()) {
				String tableName = rs.getObject("TABLE_NAME").toString();
				if (!DBUtils.isAllowedTable(tableName)) {
					continue;
				}
				if (tableName.toLowerCase().startsWith("act_")) {
					continue;
				}
				if (tableName.toLowerCase().startsWith("jbpm_")) {
					continue;
				}
				if (tableName.toLowerCase().startsWith("sys_identity_token")) {
					continue;
				}
				if (tableName.toLowerCase().startsWith("sys_data_log")) {
					continue;
				}
				if (tableName.toLowerCase().startsWith("sys_database")) {
					continue;
				}
				if (tableName.toLowerCase().startsWith("sys_lob")) {
					continue;
				}
				if (tableName.toLowerCase().startsWith("sys_sql")) {
					continue;
				}
				if (tableName.toLowerCase().startsWith("sys_key")) {
					continue;
				}
				if (tableName.toLowerCase().startsWith("sys_property")) {
					continue;
				}
				if (tableName.toLowerCase().startsWith("sys_server")) {
					continue;
				}
				if (tableName.toLowerCase().startsWith("sys_user")) {
					continue;
				}
				if (tableName.toLowerCase().startsWith("log_")) {
					continue;
				}
				if (tableName.toLowerCase().startsWith("temp")) {
					continue;
				}
				if (tableName.toLowerCase().startsWith("tmp")) {
					continue;
				}

				if (DBUtils.isTemoraryTable(tableName)) {
					continue;
				}
				JSONObject json = new JSONObject();
				json.put("startIndex", startIndex++);
				json.put("cat", rs.getObject("TABLE_CAT"));
				json.put("schem", rs.getObject("TABLE_SCHEM"));
				json.put("tablename", tableName);
				json.put("tableName_enc", RequestUtils.encodeString(tableName));
				rowsJSON.add(json);
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(connection);
		}

		result.put("rows", rowsJSON);
		result.put("total", rowsJSON.size());
		return result.toJSONString().getBytes("UTF-8");
	}

	@RequestMapping
	public ModelAndView list(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String x_view = ViewProperties.getString("system_table.list");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/table/list", modelMap);
	}

	@RequestMapping("/resultList")
	public ModelAndView resultList(HttpServletRequest request) {
		String jx_view = request.getParameter("jx_view");
		RequestUtils.setRequestParameterToAttribute(request);
		String systemName = request.getParameter("systemName");
		String tableName = request.getParameter("tableName_enc");
		if (StringUtils.isNotEmpty(tableName)) {
			tableName = RequestUtils.decodeString(tableName);
		} else {
			tableName = request.getParameter("tableName");
		}
		if (StringUtils.isEmpty(systemName)) {
			systemName = Environment.DEFAULT_SYSTEM_NAME;
		}
		List<ColumnDefinition> columns = null;
		try {
			if (StringUtils.isNotEmpty(tableName)) {
				columns = TableFactory.getColumnDefinitions(systemName, tableName);
				request.setAttribute("columns", columns);
				request.setAttribute("tableName_enc", RequestUtils.encodeString(tableName));
				List<String> pks = DBUtils.getPrimaryKeys(systemName, tableName);
				if (pks != null && !pks.isEmpty()) {
					if (pks.size() == 1) {
						request.setAttribute("primaryKey", pks.get(0));
						for (ColumnDefinition column : columns) {
							if (StringUtils.equalsIgnoreCase(pks.get(0), column.getColumnName())) {
								request.setAttribute("idColumn", column);
								break;
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex);
		}

		if (StringUtils.isNotEmpty(jx_view)) {
			return new ModelAndView(jx_view);
		}

		String x_view = ViewProperties.getString("sys_table.resultList");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view);
		}
		return new ModelAndView("/sys/table/resultList");
	}

	@ResponseBody
	@RequestMapping("/save")
	public byte[] save(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String tableName = request.getParameter("tableName_enc");
		if (StringUtils.isNotEmpty(tableName)) {
			tableName = RequestUtils.decodeString(tableName);
		} else {
			tableName = request.getParameter("tableName");
		}

		if (StringUtils.startsWithIgnoreCase(tableName, "LOG_")) {
			throw new RuntimeException(tableName + " access deny");
		}

		Collection<String> rejects = new java.util.ArrayList<String>();
		rejects.add("SYS_USER");
		rejects.add("SYS_ROLE");
		rejects.add("DATA_FILE");
		rejects.add("SYS_MAIL_FILE");
		rejects.add("SYS_DBID");
		rejects.add("SYS_PROPERTY");
		rejects.add("SYS_DATABASE");
		rejects.add("SYS_SERVER");
		rejects.add("SYS_KEY");

		if (conf.get("table.rejects") != null) {
			String str = conf.get("table.rejects");
			List<String> list = StringTools.split(str);
			for (String t : list) {
				rejects.add(t.toUpperCase());
			}
		}

		String businessKey = request.getParameter("businessKey");
		String primaryKey = null;
		ColumnDefinition idColumn = null;
		List<ColumnDefinition> columns = null;
		try {
			/**
			 * 保证系统安全性不得修改系统表及工作流的数据
			 */
			if (StringUtils.isNotEmpty(tableName) && !rejects.contains(tableName.toUpperCase())
					&& !StringUtils.startsWithIgnoreCase(tableName, "user")
					&& !StringUtils.startsWithIgnoreCase(tableName, "net")
					&& !StringUtils.startsWithIgnoreCase(tableName, "sys")
					&& !StringUtils.startsWithIgnoreCase(tableName, "jbpm")
					&& !StringUtils.startsWithIgnoreCase(tableName, "act")) {
				columns = TableFactory.getColumnDefinitions(tableName);
				modelMap.put("tableName", tableName);
				modelMap.put("tableName_enc", RequestUtils.encodeString(tableName));
				List<String> pks = DBUtils.getPrimaryKeys(tableName);
				if (pks != null && !pks.isEmpty()) {
					if (pks.size() == 1) {
						primaryKey = pks.get(0);
					}
				}
				if (primaryKey != null) {
					for (ColumnDefinition column : columns) {
						if (StringUtils.equalsIgnoreCase(primaryKey, column.getColumnName())) {
							idColumn = column;
							break;
						}
					}
				}
				if (idColumn != null) {
					TableModel tableModel = new TableModel();
					tableModel.setTableName(tableName);
					ColumnModel idCol = new ColumnModel();
					idCol.setColumnName(idColumn.getColumnName());
					idCol.setJavaType(idColumn.getJavaType());
					if ("Integer".equals(idColumn.getJavaType())) {
						idCol.setValue(Integer.parseInt(businessKey));
					} else if ("Long".equals(idColumn.getJavaType())) {
						idCol.setValue(Long.parseLong(businessKey));
					} else {
						idCol.setValue(businessKey);
					}
					tableModel.setIdColumn(idCol);

					for (ColumnDefinition column : columns) {
						String value = request.getParameter(column.getColumnName());
						ColumnModel col = new ColumnModel();
						col.setColumnName(column.getColumnName());
						col.setJavaType(column.getJavaType());
						if (value != null && value.trim().length() > 0 && !value.equals("null")) {
							if ("Integer".equals(column.getJavaType())) {
								col.setValue(Integer.parseInt(value));
								tableModel.addColumn(col);
							} else if ("Long".equals(column.getJavaType())) {
								col.setValue(Long.parseLong(value));
								tableModel.addColumn(col);
							} else if ("Double".equals(column.getJavaType())) {
								col.setValue(Double.parseDouble(value));
								tableModel.addColumn(col);
							} else if ("Date".equals(column.getJavaType())) {
								col.setValue(DateUtils.toDate(value));
								tableModel.addColumn(col);
							} else if ("String".equals(column.getJavaType())) {
								col.setValue(value);
								tableModel.addColumn(col);
							}
						}
					}

					tableDataService.updateTableData(tableModel);

					return ResponseUtils.responseJsonResult(true);
				}
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource
	public void setDatabaseService(IDatabaseService databaseService) {
		this.databaseService = databaseService;
	}

	@javax.annotation.Resource
	public void setSystemParamService(ISystemParamService systemParamService) {
		this.systemParamService = systemParamService;
	}

	@javax.annotation.Resource
	public void setTableDataService(ITableDataService tableDataService) {
		this.tableDataService = tableDataService;
	}

	@javax.annotation.Resource
	public void setTablePageService(ITablePageService tablePageService) {
		this.tablePageService = tablePageService;
	}

	@ResponseBody
	@RequestMapping("/sysTables")
	public byte[] sysTables(HttpServletRequest request) throws IOException {
		JSONArray result = new JSONArray();
		SystemParam param = systemParamService.getSystemParam("sys_table");
		if (param != null && StringUtils.isNotEmpty(param.getTextVal())) {
			result = JSON.parseArray(param.getTextVal());
		}
		return result.toJSONString().getBytes("UTF-8");
	}

	@ResponseBody
	@RequestMapping("/updateHibernateDDL")
	public byte[] updateHibernateDDL(HttpServletRequest request) throws IOException {
		String systemName = request.getParameter("systemName");
		String currentName = Environment.getCurrentSystemName();
		try {
			if (StringUtils.isNotEmpty(systemName)) {
				Environment.setCurrentSystemName(systemName);
			}
			HibernateBeanFactory.reload();
			HibernateBeanFactory.getSessionFactory().close();
			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			logger.error(ex);
		} finally {
			Environment.setCurrentSystemName(currentName);
		}

		try {
			if (StringUtils.isNotEmpty(systemName)) {
				EntitySchemaUpdate bean = new EntitySchemaUpdate();
				bean.updateDDL(systemName);
			} else {
				EntitySchemaUpdate bean = new EntitySchemaUpdate();
				bean.updateDDL();
			}
			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			logger.error(ex);
		}

		return ResponseUtils.responseJsonResult(false);
	}

}
