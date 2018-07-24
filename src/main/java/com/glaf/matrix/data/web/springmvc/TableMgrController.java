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
package com.glaf.matrix.data.web.springmvc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.core.base.BaseItem;
import com.glaf.core.base.ColumnModel;
import com.glaf.core.base.TableModel;
import com.glaf.core.config.DatabaseConnectionConfig;
import com.glaf.core.config.SystemProperties;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.domain.Database;
import com.glaf.core.factory.DataServiceFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.service.ITablePageService;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.FileUtils;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.StringTools;
import com.glaf.core.util.Tools;
import com.glaf.core.util.ZipUtils;
import com.glaf.matrix.data.bean.GenaralDBToSQLite;
import com.glaf.matrix.data.bean.SQLiteToGenaralDB;
import com.glaf.matrix.data.domain.SqlDefinition;
import com.glaf.matrix.data.domain.SysTable;
import com.glaf.matrix.data.domain.TableColumn;
import com.glaf.matrix.data.domain.TableDataItem;
import com.glaf.matrix.data.factory.DataItemFactory;
import com.glaf.matrix.data.query.SqlDefinitionQuery;
import com.glaf.matrix.data.query.SysTableQuery;
import com.glaf.matrix.data.query.TableColumnQuery;
import com.glaf.matrix.data.query.TableDataItemQuery;
import com.glaf.matrix.data.service.ITableService;
import com.glaf.matrix.data.service.SqlDefinitionService;
import com.glaf.matrix.data.service.TableDataItemService;
import com.glaf.matrix.data.util.TableDomainFactory;

@Controller("/sys/tableMgr")
@RequestMapping("/sys/tableMgr")
public class TableMgrController {
	protected static final Log logger = LogFactory.getLog(TableMgrController.class);

	protected ITableService tableService;

	protected ITablePageService tablePageService;

	protected SqlDefinitionService sqlDefinitionService;

	protected TableDataItemService tableDataItemService;

	@RequestMapping("/columns")
	public ModelAndView columns(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/sys/tableMgr/columns", modelMap);
	}

	@RequestMapping("/columnsJson")
	@ResponseBody
	public byte[] columnsJson(HttpServletRequest request, ModelMap modelMap) throws IOException {
		String tableId = request.getParameter("tableId");
		JSONObject result = new JSONObject();
		List<TableColumn> list = tableService.getTableColumnsByTableId(tableId);
		if (list != null && !list.isEmpty()) {
			int start = 0;
			int total = list.size();
			result.put("total", total);
			result.put("totalCount", total);
			result.put("totalRecords", total);
			result.put("start", 0);
			result.put("startIndex", 0);
			result.put("limit", total);
			result.put("pageSize", total);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (TableColumn t : list) {
					JSONObject rowJSON = t.toJsonObject();
					rowJSON.put("startIndex", ++start);
					rowsJSON.add(rowJSON);
				}

			}
		} else {
			JSONArray rowsJSON = new JSONArray();
			result.put("rows", rowsJSON);
			result.put("total", 0);
		}
		return result.toJSONString().getBytes("UTF-8");
	}

	@RequestMapping("/columnsJsonArray")
	@ResponseBody
	public byte[] columnsJsonArray(HttpServletRequest request, ModelMap modelMap) throws IOException {
		String tableName = request.getParameter("tableName");
		JSONArray result = new JSONArray();
		JSONObject idJson = new JSONObject();
		idJson.put("columnName", "ID_");
		idJson.put("title", "编号");
		result.add(idJson);

		List<TableColumn> list = tableService.getTableColumnsByTableName(tableName);
		if (list != null && !list.isEmpty()) {
			for (TableColumn t : list) {
				JSONObject rowJSON = t.toJsonObject();
				result.add(rowJSON);
			}
		}

		return result.toJSONString().getBytes("UTF-8");
	}

	@RequestMapping("/databases")
	public ModelAndView databases(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		DatabaseConnectionConfig cfg = new DatabaseConnectionConfig();
		List<Database> databases = cfg.getDatabases();
		request.setAttribute("databases", databases);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/sys/tableMgr/databases", modelMap);
	}

	/**
	 * 删除表字段
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/deleteColumn")
	@ResponseBody
	public byte[] deleteColumn(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isSystemAdministrator()) {
			try {
				String tableId = request.getParameter("tableId");
				SysTable sysTable = null;
				if (StringUtils.isNotEmpty(tableId)) {
					sysTable = tableService.getSysTableById(tableId);
					if (sysTable != null) {
						if (!DBUtils.tableExists(sysTable.getTableName())) {
							tableService.deleteColumn(request.getParameter("id"));
							return ResponseUtils.responseResult(true);
						} else {
							TableColumn column = tableService.getTableColumn(request.getParameter("id"));
							if (column != null
									&& StringUtils.equalsIgnoreCase(column.getTableName(), sysTable.getTableName())) {
								List<ColumnDefinition> columns = DBUtils.getColumnDefinitions(sysTable.getTableName());
								if (columns != null && !columns.isEmpty()) {
									for (ColumnDefinition c : columns) {
										if (StringUtils.equalsIgnoreCase(column.getColumnName(), c.getColumnName())) {
											return ResponseUtils.responseJsonResult(false, "物理表的字段已经存在，不允许删除！");
										}
									}
								}
								tableService.deleteColumn(request.getParameter("id"));
								return ResponseUtils.responseResult(true);
							}
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	/**
	 * 删除表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/deleteTable")
	@ResponseBody
	public byte[] deleteTable(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isSystemAdministrator()) {
			try {
				String tableId = request.getParameter("tableId");
				SysTable tableDefinition = null;
				if (StringUtils.isNotEmpty(tableId)) {
					tableDefinition = tableService.getSysTableById(tableId);
					if (tableDefinition != null) {
						if (!DBUtils.tableExists(tableDefinition.getTableName())) {
							tableService.deleteTable(tableId);
							return ResponseUtils.responseResult(true);
						} else {
							return ResponseUtils.responseJsonResult(false, "物理表已经存在，不允许删除！");
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/editColumn")
	public ModelAndView editColumn(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		logger.debug(RequestUtils.getParameterMap(request));
		String columnId = request.getParameter("id");
		if (StringUtils.isNotEmpty(columnId)) {
			TableColumn columnDefinition = tableService.getTableColumn(columnId);
			request.setAttribute("column", columnDefinition);
		}

		List<BaseItem> items = new ArrayList<BaseItem>();
		List<BaseItem> datalist = DataItemFactory.getInstance().getTreeItems(4L);
		if (datalist != null && !datalist.isEmpty()) {
			for (BaseItem bean : datalist) {
				BaseItem item = new BaseItem();
				item.setName(bean.getName());
				item.setValue("@sys_dict:" + bean.getValue());
				items.add(item);
			}
		}

		TableDataItemQuery query = new TableDataItemQuery();
		query.locked(0);
		List<TableDataItem> list = tableDataItemService.list(query);
		if (list != null && !list.isEmpty()) {
			for (TableDataItem m : list) {
				BaseItem item = new BaseItem();
				item.setName(m.getTitle());
				item.setValue("@table:" + m.getId());
				items.add(item);
			}
		}

		SqlDefinitionQuery query2 = new SqlDefinitionQuery();
		query2.dataItemFlag("Y");
		query2.locked(0);
		List<SqlDefinition> list2 = sqlDefinitionService.list(query2);
		if (list2 != null && !list2.isEmpty()) {
			for (SqlDefinition m : list2) {
				BaseItem item = new BaseItem();
				item.setName(m.getTitle());
				item.setValue("@sql:" + m.getId());
				items.add(item);
			}
		}

		request.setAttribute("items", items);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/sys/tableMgr/editColumn", modelMap);
	}

	@RequestMapping("/editTable")
	public ModelAndView editTable(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String tableId = request.getParameter("tableId");
		if (StringUtils.isNotEmpty(tableId)) {
			SysTable tableDefinition = tableService.getSysTableById(tableId);
			request.setAttribute("tableDefinition", tableDefinition);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/sys/tableMgr/editTable", modelMap);
	}

	@RequestMapping("/exportDB")
	@ResponseBody
	public void exportDB(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String systemName = request.getParameter("systemName");
		String tables = request.getParameter("tables");
		if (StringUtils.isNotEmpty(systemName)) {
			String sqliteDB = systemName + DateUtils.getNowYearMonthDayHHmmss() + "_sqlite.db";
			GenaralDBToSQLite dbToSqliteDB = new GenaralDBToSQLite();
			if (StringUtils.isNotEmpty(tables)) {
				dbToSqliteDB.export(systemName, StringTools.split(tables), sqliteDB);
			} else {
				dbToSqliteDB.exportAll(systemName, sqliteDB);
			}
			String dbpath = SystemProperties.getConfigRootPath() + "/db/" + sqliteDB;
			String zipFilePath = SystemProperties.getConfigRootPath() + "/db/" + sqliteDB + ".zip";
			File[] files = new File[] { new File(dbpath) };
			ZipUtils.compressFile(files, zipFilePath);
			byte[] data = FileUtils.getBytes(zipFilePath);
			try {
				ResponseUtils.download(request, response, data, sqliteDB + ".zip");
			} catch (Exception ex) {
			}
		}
	}

	// http://127.0.0.1:8080/glaf/sys/tableMgr/exportSysTables?systemName=db_401
	// /sys/tableMgr/exportSysTables?systemName=db_602
	@RequestMapping("/exportSysTables")
	@ResponseBody
	public void exportSysTables(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String systemName = request.getParameter("systemName");
		if (StringUtils.isNotEmpty(systemName)) {
			String sqliteDB = systemName + DateUtils.getNowYearMonthDayHHmmss() + "_sqlite.db";
			GenaralDBToSQLite dbToSqliteDB = new GenaralDBToSQLite();
			List<String> tables = DBUtils.getTables(systemName);
			List<String> sysTables = new ArrayList<String>();
			sysTables.add("health_dishes");
			sysTables.add("health_dishes_item");
			sysTables.add("health_dietary_template");
			sysTables.add("health_dietary_item");
			sysTables.add("health_food_adi");
			sysTables.add("health_food_composition");
			sysTables.add("health_food_favorite");
			sysTables.add("health_food_dri");
			sysTables.add("health_food_dri_percent");
			sysTables.add("health_grade_info");
			sysTables.add("health_grade_privilege");
			sysTables.add("health_growth_standard");
			sysTables.add("health_person");
			sysTables.add("health_person_linkman");
			sysTables.add("health_person_info");
			sysTables.add("health_repast_person");

			for (String tbl : tables) {
				if (StringUtils.startsWithIgnoreCase(tbl, "sys_")) {
					sysTables.add(tbl);
				}
			}
			dbToSqliteDB.export(systemName, sysTables, sqliteDB);
			String dbpath = SystemProperties.getConfigRootPath() + "/db/" + sqliteDB;
			String zipFilePath = SystemProperties.getConfigRootPath() + "/db/" + sqliteDB + ".zip";
			File[] files = new File[] { new File(dbpath) };
			ZipUtils.compressFile(files, zipFilePath);
			byte[] data = FileUtils.getBytes(zipFilePath);
			try {
				ResponseUtils.download(request, response, data, sqliteDB + ".zip");
			} catch (Exception ex) {
			}
		}
	}

	@RequestMapping("/extColumns")
	public ModelAndView extColumns(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String targetId = request.getParameter("targetId");
		if (StringUtils.isNotEmpty(targetId)) {
			TableColumnQuery query = new TableColumnQuery();
			query.targetId(targetId);
			List<TableColumn> columns = tableService.getTableColumns(query);
			request.setAttribute("columns", columns);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/sys/tableMgr/extColumns", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SysTableQuery query = new SysTableQuery();
		Tools.populate(query, params);
		query.setCreateBy(loginContext.getActorId());
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);
		query.type("useradd");

		if (!loginContext.isSystemAdministrator()) {
			String actorId = loginContext.getActorId();
			query.createBy(actorId);
		}

		int start = 0;
		int limit = 50;
		String orderName = null;
		String order = null;

		int pageNo = ParamUtils.getInt(params, "page");
		limit = ParamUtils.getInt(params, "rows");
		start = (pageNo - 1) * limit;
		orderName = ParamUtils.getString(params, "sortName");
		order = ParamUtils.getString(params, "sortOrder");

		if (start < 0) {
			start = 0;
		}

		if (limit <= 0) {
			limit = Paging.DEFAULT_PAGE_SIZE;
		}

		JSONObject result = new JSONObject();
		int total = tableService.getSysTableCountByQueryCriteria(query);
		if (total > 0) {
			result.put("total", total);
			result.put("totalCount", total);
			result.put("totalRecords", total);
			result.put("start", start);
			result.put("startIndex", start);
			result.put("limit", limit);
			result.put("pageSize", limit);

			if (StringUtils.isNotEmpty(orderName)) {
				query.setSortOrder(orderName);
				if (StringUtils.equals(order, "desc")) {
					query.setSortOrder(" desc ");
				}
			}

			List<SysTable> list = tableService.getSysTablesByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (SysTable t : list) {
					JSONObject rowJSON = t.toJsonObject();
					rowJSON.put("id", t.getTableName());
					rowJSON.put("startIndex", ++start);
					rowsJSON.add(rowJSON);
				}

			}
		} else {
			JSONArray rowsJSON = new JSONArray();
			result.put("rows", rowsJSON);
			result.put("total", total);
		}
		return result.toJSONString().getBytes("UTF-8");
	}

	@RequestMapping("/loadDataPackage")
	@ResponseBody
	public byte[] loadDataPackage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String sqliteDB = request.getParameter("sqliteDB");
		String systemName = request.getParameter("systemName");
		if (StringUtils.isNotEmpty(sqliteDB) && StringUtils.isNotEmpty(systemName)) {
			String dbpath = SystemProperties.getConfigRootPath() + "/db/" + sqliteDB;
			File dbfile = new File(dbpath);
			if (dbfile.exists() && dbfile.isFile()) {
				try {
					SQLiteToGenaralDB db = new SQLiteToGenaralDB();
					boolean result = db.importData(systemName, sqliteDB);
					return ResponseUtils.responseResult(result);
				} catch (Exception ex) {
					logger.error(ex);
				}
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@ResponseBody
	@RequestMapping("/save")
	public byte[] save(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isSystemAdministrator()) {
			String actorId = loginContext.getActorId();
			Map<String, Object> params = RequestUtils.getParameterMap(request);
			String tableId = request.getParameter("tableId");
			SysTable tableDefinition = null;
			try {
				if (StringUtils.isNotEmpty(tableId)) {
					tableDefinition = tableService.getSysTableById(tableId);
				}
				if (tableDefinition == null) {
					String tableName = null;
					while (true) {
						if (tableName == null) {
							tableName = tableService.nextTableName("useradd");
						}
						if (DBUtils.tableExists(tableName)) {
							tableName = tableService.nextTableName("useradd");
						} else {
							tableDefinition = new SysTable();
							tableDefinition.setTableName(tableName);
							break;
						}
					}
				}

				Tools.populate(tableDefinition, params);
				tableDefinition.setCreateBy(actorId);
				tableDefinition.setCreateTime(new Date());
				tableDefinition.setAddType(1);
				tableDefinition.setSystemFlag("U");
				tableDefinition.setLocked(0);
				tableDefinition.setType("useradd");
				tableDefinition.setPartitionFlag(request.getParameter("partitionFlag"));
				tableDefinition.setReportId(request.getParameter("reportId"));
				tableDefinition.setFormReportId(request.getParameter("formReportId"));
				tableService.save(tableDefinition);

				return ResponseUtils.responseJsonResult(true);
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@ResponseBody
	@RequestMapping("/saveAs")
	public byte[] saveAs(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isSystemAdministrator()) {
			String actorId = loginContext.getActorId();
			String tableId = request.getParameter("tableId");
			Map<String, Object> params = RequestUtils.getParameterMap(request);
			SysTable tableDefinition = null;
			try {
				if (StringUtils.isNotEmpty(tableId)) {
					tableDefinition = tableService.getSysTableById(tableId);
				}
				if (tableDefinition != null && tableDefinition.getColumns() != null
						&& !tableDefinition.getColumns().isEmpty()) {
					String tableName = null;
					while (true) {
						if (tableName == null) {
							tableName = tableService.nextTableName("useradd");
						}
						if (DBUtils.tableExists(tableName)) {
							tableName = tableService.nextTableName("useradd");
						} else {
							tableDefinition.setTableName(tableName);
							break;
						}
					}

					Tools.populate(tableDefinition, params);
					tableDefinition.setCreateBy(actorId);
					tableDefinition.setCreateTime(new Date());
					tableDefinition.setAddType(1);
					tableDefinition.setSystemFlag("U");
					tableDefinition.setLocked(0);
					tableDefinition.setType("useradd");
					tableDefinition.setPartitionFlag(request.getParameter("partitionFlag"));
					tableDefinition.setReportId(request.getParameter("reportId"));
					tableDefinition.setFormReportId(request.getParameter("formReportId"));
					tableService.saveAs(tableDefinition, tableDefinition.getColumns());

					return ResponseUtils.responseJsonResult(true);
				}
			} catch (Exception ex) {
				// ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@ResponseBody
	@RequestMapping("/saveColumn")
	public byte[] saveColumn(HttpServletRequest request) {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isSystemAdministrator()) {
			String tableId = request.getParameter("tableId");
			SysTable tableDefinition = null;
			try {
				if (StringUtils.isNotEmpty(tableId)) {
					tableDefinition = tableService.getSysTableById(tableId);
				}
				if (tableDefinition != null) {
					String columnId = request.getParameter("id");
					if (StringUtils.isNotEmpty(columnId)) {
						TableColumn columnDefinition = tableService.getTableColumn(columnId);
						if (columnDefinition != null) {
							Tools.populate(columnDefinition, params);
							columnDefinition.setDataCode(request.getParameter("dataCode"));
							columnDefinition.setTableName(tableDefinition.getTableName());
							columnDefinition.setComment(request.getParameter("comment"));
							tableService.updateColumn(columnDefinition);
						}
					} else {
						TableColumn columnDefinition = new TableColumn();
						Tools.populate(columnDefinition, params);
						columnDefinition.setDataCode(request.getParameter("dataCode"));
						columnDefinition.setTableName(tableDefinition.getTableName());
						columnDefinition.setComment(request.getParameter("comment"));
						tableService.saveColumn(tableDefinition.getTableName(), columnDefinition);
					}
					return ResponseUtils.responseJsonResult(true);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@ResponseBody
	@RequestMapping("/saveColumns")
	public byte[] saveColumns(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isSystemAdministrator()) {
			String targetId = request.getParameter("targetId");
			try {
				if (StringUtils.isNotEmpty(targetId)) {
					TableColumnQuery query = new TableColumnQuery();
					query.targetId(targetId);
					List<TableColumn> columns = tableService.getTableColumns(query);
					for (TableColumn col : columns) {
						col.setTitle(request.getParameter("title_" + col.getId()));
						col.setColumnName(request.getParameter("columnName_" + col.getId()));
						col.setJavaType(request.getParameter("javaType_" + col.getId()));
						col.setLength(RequestUtils.getInt(request, "length_" + col.getId()));
					}
					tableService.saveColumns(targetId, columns);
					return ResponseUtils.responseJsonResult(true);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	/**
	 * 排序
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveSort")
	@ResponseBody
	public byte[] saveSort(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isSystemAdministrator()) {
			String items = request.getParameter("items");
			if (StringUtils.isNotEmpty(items)) {
				int sort = 0;
				List<TableModel> rows = new ArrayList<TableModel>();
				StringTokenizer token = new StringTokenizer(items, ",");
				while (token.hasMoreTokens()) {
					String item = token.nextToken();
					if (StringUtils.isNotEmpty(item)) {
						sort++;
						TableModel t1 = new TableModel();
						t1.setTableName("SYS_COLUMN");
						ColumnModel idColumn = new ColumnModel();
						idColumn.setColumnName("ID_");
						idColumn.setJavaType("String");
						idColumn.setValue(item);
						t1.setIdColumn(idColumn);
						ColumnModel sortColumn = new ColumnModel();
						sortColumn.setColumnName("ORDINAL_");
						sortColumn.setJavaType("Integer");
						sortColumn.setValue(sort);
						t1.addColumn(sortColumn);
						rows.add(t1);
					}
				}
				try {
					DataServiceFactory.getInstance().updateAllTableData(rows);
					return ResponseUtils.responseResult(true);
				} catch (Exception ex) {
					logger.error(ex);
				}
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@javax.annotation.Resource
	public void setSqlDefinitionService(SqlDefinitionService sqlDefinitionService) {
		this.sqlDefinitionService = sqlDefinitionService;
	}

	@javax.annotation.Resource
	public void setTableDataItemService(TableDataItemService tableDataItemService) {
		this.tableDataItemService = tableDataItemService;
	}

	@javax.annotation.Resource
	public void setTablePageService(ITablePageService tablePageService) {
		this.tablePageService = tablePageService;
	}

	@javax.annotation.Resource
	public void setTableService(ITableService tableService) {
		this.tableService = tableService;
	}

	/**
	 * 显示排序页面
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/showSort")
	public ModelAndView showSort(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String tableId = request.getParameter("tableId");
		List<TableColumn> columns = tableService.getTableColumnsByTableId(tableId);
		request.setAttribute("columns", columns);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("table.showSort");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/tableMgr/showSort", modelMap);
	}

	@RequestMapping("/tables")
	public ModelAndView tables(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		SysTableQuery query = new SysTableQuery();
		query.locked(0);
		query.type("useradd");
		try {
			List<SysTable> tables = tableService.list(query);
			if (tables != null && !tables.isEmpty()) {
				request.setAttribute("tables", tables);
				request.setAttribute("updateAllSchema", true);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/sys/tableMgr/tables", modelMap);
	}

	/**
	 * 更新全部表结构
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/updateAllSchema")
	@ResponseBody
	public byte[] updateAllSchema(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isSystemAdministrator() && StringUtils.equals(loginContext.getActorId(), "admin")) {
			SysTable sysTable = null;
			List<TableColumn> extendColumns = null;
			SysTableQuery query = new SysTableQuery();
			query.locked(0);
			query.type("useradd");
			try {
				List<SysTable> tables = tableService.list(query);
				if (tables != null && !tables.isEmpty()) {
					for (SysTable table : tables) {
						String tableId = table.getTableId();
						if (StringUtils.isNotEmpty(tableId)) {
							sysTable = tableService.getSysTableById(tableId);
							if (sysTable != null) {
								extendColumns = tableService.getTableColumnsByTableId(tableId);
								List<ColumnDefinition> extColumns = new ArrayList<ColumnDefinition>();
								if (extendColumns != null && !extendColumns.isEmpty()) {
									for (TableColumn col : extendColumns) {
										ColumnDefinition c = new ColumnDefinition();
										org.apache.commons.beanutils.PropertyUtils.copyProperties(c, col);
										c.setColumnName(col.getColumnName());
										c.setJavaType(col.getJavaType());
										c.setLength(col.getLength());
										extColumns.add(c);
									}
								}

								/**
								 * 如果是分区表，创建分表
								 */
								if (StringUtils.equals(sysTable.getPartitionFlag(), "Y")) {
									long databaseId = RequestUtils.getLong(request, "databaseId");
									if (databaseId > 0) {
										DatabaseConnectionConfig cfg = new DatabaseConnectionConfig();
										Database database = cfg.getDatabase(databaseId);
										if (StringUtils.equals(database.getUseType(), "GENERAL")) {
											try {
												TableDomainFactory.createTables(databaseId, sysTable.getTableName(),
														sysTable.getAuditFlag(), extColumns);
											} catch (Exception ex) {
												logger.error(ex);
											}
											try {
												TableDomainFactory.alterTables(databaseId, sysTable.getTableName(),
														sysTable.getAuditFlag(), extColumns);
											} catch (Exception ex) {
												logger.error(ex);
											}
										}
									}
								} else {
									TableDomainFactory.updateSchema(sysTable.getTableName(), sysTable.getAuditFlag(),
											extColumns);
								}
							}
						}
					}
					return ResponseUtils.responseResult(true);
				}
			} catch (Exception ex) {
				logger.error(ex);
			} finally {
				sysTable = null;
				extendColumns = null;
			}
		}
		return ResponseUtils.responseResult(false);
	}

	/**
	 * 更新表结构
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/updateSchema")
	@ResponseBody
	public byte[] updateSchema(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isSystemAdministrator() && StringUtils.equals(loginContext.getActorId(), "admin")) {
			try {
				String tableId = request.getParameter("tableId");
				SysTable sysTable = null;
				if (StringUtils.isNotEmpty(tableId)) {
					sysTable = tableService.getSysTableById(tableId);
					if (sysTable != null) {
						List<TableColumn> extendColumns = tableService.getTableColumnsByTableId(tableId);
						List<ColumnDefinition> extColumns = new ArrayList<ColumnDefinition>();
						if (extendColumns != null && !extendColumns.isEmpty()) {
							for (TableColumn col : extendColumns) {
								ColumnDefinition c = new ColumnDefinition();
								org.apache.commons.beanutils.PropertyUtils.copyProperties(c, col);
								c.setColumnName(col.getColumnName());
								c.setJavaType(col.getJavaType());
								c.setLength(col.getLength());
								extColumns.add(c);
							}
						}
						/**
						 * 如果是分区表，创建分表
						 */
						if (StringUtils.equals(sysTable.getPartitionFlag(), "Y")) {
							long databaseId = RequestUtils.getLong(request, "databaseId");
							if (databaseId > 0) {
								DatabaseConnectionConfig cfg = new DatabaseConnectionConfig();
								Database database = cfg.getDatabase(databaseId);
								if (StringUtils.equals(database.getUseType(), "GENERAL")) {
									try {
										TableDomainFactory.createTables(databaseId, sysTable.getTableName(),
												sysTable.getAuditFlag(), extColumns);
									} catch (Exception ex) {
										logger.error(ex);
									}
									try {
										TableDomainFactory.alterTables(databaseId, sysTable.getTableName(),
												sysTable.getAuditFlag(), extColumns);
									} catch (Exception ex) {
										logger.error(ex);
									}
								}
							}
						} else {
							TableDomainFactory.updateSchema(sysTable.getTableName(), sysTable.getAuditFlag(),
									extColumns);
							return ResponseUtils.responseResult(true);
						}
					}
				}
			} catch (Exception ex) {
				logger.error(ex);
			}
		}
		return ResponseUtils.responseResult(false);
	}

}
