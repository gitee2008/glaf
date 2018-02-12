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

package com.glaf.matrix.combination.web.springmvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.glaf.core.config.DatabaseConnectionConfig;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.domain.Database;
import com.glaf.core.factory.TableFactory;
import com.glaf.core.identity.User;
import com.glaf.core.security.LoginContext;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.service.ITableDataService;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.StringTools;
import com.glaf.core.util.Tools;

import com.glaf.matrix.combination.domain.TreeTableAggregate;
import com.glaf.matrix.combination.query.TreeTableAggregateQuery;
import com.glaf.matrix.combination.service.TreeTableAggregateService;
import com.glaf.matrix.combination.task.TreeTableAggregateTask;
import com.glaf.matrix.data.service.ExecutionLogService;
import com.glaf.matrix.data.util.ExceptionUtils;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/sys/treeTableAggregate")
@RequestMapping("/sys/treeTableAggregate")
public class TreeTableAggregateController {
	protected static final Log logger = LogFactory.getLog(TreeTableAggregateController.class);

	protected IDatabaseService databaseService;

	protected ITableDataService tableDataService;

	protected ExecutionLogService executionLogService;

	protected TreeTableAggregateService treeTableAggregateService;

	public TreeTableAggregateController() {

	}

	@RequestMapping("/chooseColumns")
	public ModelAndView chooseColumns(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String sourceDatabaseIds = request.getParameter("databaseIds");
		List<Long> databaseIds = StringTools.splitToLong(sourceDatabaseIds);
		if (databaseIds != null && !databaseIds.isEmpty()) {
			String tableName = request.getParameter("tableName");
			for (Long databaseId : databaseIds) {
				Database database = databaseService.getDatabaseById(databaseId);
				List<ColumnDefinition> columns = null;
				if (database != null) {
					columns = DBUtils.getColumnDefinitions(database.getName(), tableName);
				} else {
					columns = DBUtils.getColumnDefinitions(tableName);
				}

				TreeTableAggregate treeTableAggregate = treeTableAggregateService
						.getTreeTableAggregate(RequestUtils.getLong(request, "treeTableAggregateId"));
				if (treeTableAggregate != null) {
					request.setAttribute("treeTableAggregate", treeTableAggregate);
				}

				if (columns != null && !columns.isEmpty()) {
					List<String> selected = new ArrayList<String>();
					List<ColumnDefinition> unselected = new ArrayList<ColumnDefinition>();
					if (treeTableAggregate != null && StringUtils.isNotEmpty(treeTableAggregate.getSyncColumns())) {
						List<String> cols = StringTools.split(treeTableAggregate.getSyncColumns());
						for (ColumnDefinition col : columns) {
							if (cols.contains(col.getColumnName()) || cols.contains(col.getColumnName().toLowerCase())
									|| cols.contains(col.getColumnName().toUpperCase())) {
								selected.add(col.getColumnName());
							} else {
								unselected.add(col);
							}
						}
						request.setAttribute("selected", selected);
						request.setAttribute("unselected", columns);
					} else {
						request.setAttribute("selected", selected);
						request.setAttribute("unselected", columns);
					}

					StringBuffer bufferx = new StringBuffer();
					StringBuffer buffery = new StringBuffer();

					if (columns != null && columns.size() > 0) {
						for (int j = 0; j < columns.size(); j++) {
							ColumnDefinition u = (ColumnDefinition) columns.get(j);
							if (selected != null && selected.contains(u.getColumnName())) {
								buffery.append("\n<option value=\"").append(u.getColumnName()).append("\">")
										.append(u.getColumnName()).append(" [").append(u.getColumnName()).append("]")
										.append("</option>");
							} else {
								bufferx.append("\n<option value=\"").append(u.getColumnName()).append("\">")
										.append(u.getColumnName()).append(" [").append(u.getColumnName()).append("]")
										.append("</option>");
							}
						}
					}
					request.setAttribute("bufferx", bufferx.toString());
					request.setAttribute("buffery", buffery.toString());
				}
				break;
			}
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("treeTableAggregate.chooseColumns");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/matrix/treeTableAggregate/chooseColumns", modelMap);
	}

	@RequestMapping("/chooseDatabases")
	public ModelAndView chooseDatabases(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		TreeTableAggregate model = treeTableAggregateService
				.getTreeTableAggregate(RequestUtils.getLong(request, "treeTableAggregateId"));
		if (model != null) {
			request.setAttribute("treeTableAggregate", model);
		}

		DatabaseConnectionConfig config = new DatabaseConnectionConfig();
		List<Database> databases = config.getDatabases();
		if (databases != null && !databases.isEmpty()) {
			List<Long> selecteds = new ArrayList<Long>();
			List<Database> unselected = new ArrayList<Database>();
			if (model != null && StringUtils.isNotEmpty(model.getDatabaseIds())) {
				List<Long> ids = StringTools.splitToLong(model.getDatabaseIds());
				for (Database database : databases) {
					if (ids.contains(database.getId())) {
						selecteds.add(database.getId());
					} else {
						unselected.add(database);
					}
				}
				request.setAttribute("selected", selecteds);
				request.setAttribute("unselected", databases);
			} else {
				request.setAttribute("selected", selecteds);
				request.setAttribute("unselected", databases);
			}

			StringBuffer bufferx = new StringBuffer();
			StringBuffer buffery = new StringBuffer();

			if (databases != null && databases.size() > 0) {
				for (int j = 0; j < databases.size(); j++) {
					Database d = (Database) databases.get(j);
					if (selecteds != null && selecteds.contains(d.getId())) {
						buffery.append("\n<option value=\"").append(d.getId()).append("\">").append(d.getTitle())
								.append(" [").append(d.getMapping()).append("]").append("</option>");
					} else {
						bufferx.append("\n<option value=\"").append(d.getId()).append("\">").append(d.getTitle())
								.append(" [").append(d.getMapping()).append("]").append("</option>");
					}
				}
				request.setAttribute("bufferx", bufferx.toString());
				request.setAttribute("buffery", buffery.toString());
			}
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("treeTableAggregate.chooseDatabases");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/matrix/treeTableAggregate/chooseDatabases", modelMap);
	}

	@ResponseBody
	@RequestMapping("/delete")
	public byte[] delete(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Long id = RequestUtils.getLong(request, "id");
		String ids = request.getParameter("ids");
		if (StringUtils.isNotEmpty(ids)) {
			StringTokenizer token = new StringTokenizer(ids, ",");
			while (token.hasMoreTokens()) {
				String x = token.nextToken();
				if (StringUtils.isNotEmpty(x)) {
					TreeTableAggregate treeTableAggregate = treeTableAggregateService
							.getTreeTableAggregate(Long.valueOf(x));
					if (treeTableAggregate != null
							&& (StringUtils.equals(treeTableAggregate.getCreateBy(), loginContext.getActorId())
									|| loginContext.isSystemAdministrator())) {
						treeTableAggregate.setDeleteFlag(1);
						treeTableAggregateService.save(treeTableAggregate);
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			TreeTableAggregate treeTableAggregate = treeTableAggregateService.getTreeTableAggregate(Long.valueOf(id));
			if (treeTableAggregate != null
					&& (StringUtils.equals(treeTableAggregate.getCreateBy(), loginContext.getActorId())
							|| loginContext.isSystemAdministrator())) {
				treeTableAggregate.setDeleteFlag(1);
				treeTableAggregateService.save(treeTableAggregate);
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@ResponseBody
	@RequestMapping("/dropAllTable")
	public byte[] dropAllTable(HttpServletRequest request, ModelMap modelMap) {
		boolean result = false;
		TreeTableAggregateQuery query = new TreeTableAggregateQuery();
		query.locked(0);
		List<TreeTableAggregate> rows = treeTableAggregateService.list(query);
		if (rows != null && !rows.isEmpty()) {
			TableFactory.clear();
			for (TreeTableAggregate treeTableAggregate : rows) {
				if (treeTableAggregate != null && StringUtils.isNotEmpty(treeTableAggregate.getTargetTableName())) {
					List<Long> databaseIds = StringTools.splitToLong(treeTableAggregate.getDatabaseIds());
					for (Long databaseId : databaseIds) {
						try {
							Database database = databaseService.getDatabaseById(databaseId);
							if (database != null) {
								if (StringUtils.startsWithIgnoreCase(treeTableAggregate.getTargetTableName(), "etl_")
										|| StringUtils.startsWithIgnoreCase(treeTableAggregate.getTargetTableName(),
												"tmp")
										|| StringUtils.startsWithIgnoreCase(treeTableAggregate.getTargetTableName(),
												"sync")
										|| StringUtils.startsWithIgnoreCase(treeTableAggregate.getTargetTableName(),
												"useradd")
										|| StringUtils.startsWithIgnoreCase(treeTableAggregate.getTargetTableName(),
												"tree_table_")) {
									if (DBUtils.tableExists(database.getName(),
											treeTableAggregate.getTargetTableName())) {
										executionLogService.deleteTodayExecutionLogs("treetable_aggregate",
												String.valueOf(treeTableAggregate.getId()));
										String ddlStatements = " drop table " + treeTableAggregate.getTargetTableName();
										logger.warn(ddlStatements);
										DBUtils.executeSchemaResource(database.getName(), ddlStatements);
									}
									result = true;
								}
							}
						} catch (Exception ex) {
							result = false;
							logger.error(ex);
						}
					}
				}
			}
			TableFactory.clear();
		}
		return ResponseUtils.responseResult(result);
	}

	@ResponseBody
	@RequestMapping("/dropTable")
	public byte[] dropTable(HttpServletRequest request, ModelMap modelMap) {
		long treeTableAggregateId = RequestUtils.getLong(request, "treeTableAggregateId");
		TreeTableAggregate treeTableAggregate = treeTableAggregateService.getTreeTableAggregate(treeTableAggregateId);
		if (treeTableAggregate != null && StringUtils.isNotEmpty(treeTableAggregate.getTargetTableName())) {
			TableFactory.clear();
			boolean result = true;
			List<Long> databaseIds = StringTools.splitToLong(treeTableAggregate.getDatabaseIds());
			for (Long databaseId : databaseIds) {
				try {
					Database database = databaseService.getDatabaseById(databaseId);
					if (database != null) {
						if (StringUtils.startsWithIgnoreCase(treeTableAggregate.getTargetTableName(), "etl_")
								|| StringUtils.startsWithIgnoreCase(treeTableAggregate.getTargetTableName(), "tmp")
								|| StringUtils.startsWithIgnoreCase(treeTableAggregate.getTargetTableName(), "sync")
								|| StringUtils.startsWithIgnoreCase(treeTableAggregate.getTargetTableName(), "useradd")
								|| StringUtils.startsWithIgnoreCase(treeTableAggregate.getTargetTableName(),
										"tree_table_")) {
							if (DBUtils.tableExists(database.getName(), treeTableAggregate.getTargetTableName())) {
								executionLogService.deleteTodayExecutionLogs("treetable_aggregate",
										String.valueOf(treeTableAggregate.getId()));
								String ddlStatements = " drop table " + treeTableAggregate.getTargetTableName();
								logger.warn(ddlStatements);
								DBUtils.executeSchemaResource(database.getName(), ddlStatements);
							}
						}
					}
				} catch (Exception ex) {
					logger.error(ex);
					result = false;
				}
			}
			TableFactory.clear();
			return ResponseUtils.responseResult(result);
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		TreeTableAggregate treeTableAggregate = treeTableAggregateService
				.getTreeTableAggregate(RequestUtils.getLong(request, "id"));
		if (treeTableAggregate != null) {
			request.setAttribute("treeTableAggregate", treeTableAggregate);
		}

		DatabaseConnectionConfig config = new DatabaseConnectionConfig();
		List<Database> databases = config.getDatabases();
		if (databases != null && !databases.isEmpty()) {
			StringBuilder buffer = new StringBuilder();
			if (treeTableAggregate != null && StringUtils.isNotEmpty(treeTableAggregate.getDatabaseIds())) {
				List<Long> ids = StringTools.splitToLong(treeTableAggregate.getDatabaseIds());
				for (Database database : databases) {
					if (ids.contains(database.getId())) {
						buffer.append(database.getTitle()).append("[").append(database.getMapping()).append("]")
								.append(",");
					}
				}
			}
			request.setAttribute("databases", databases);
			request.setAttribute("selectedDB", buffer.toString());
		}

		TreeTableAggregateQuery query2 = new TreeTableAggregateQuery();
		query2.locked(0);
		List<TreeTableAggregate> list = treeTableAggregateService.list(query2);
		// logger.debug("tables:" + list);
		if (list != null && !list.isEmpty()) {
			request.setAttribute("targetTables", list);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("treeTableAggregate.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/matrix/treeTableAggregate/edit", modelMap);
	}

	@ResponseBody
	@RequestMapping("/execute")
	public byte[] execute(HttpServletRequest request) {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		long treeTableAggregateId = RequestUtils.getLong(request, "treeTableAggregateId");
		int errorCount = 0;
		String jobNo = null;
		Database database = null;
		String runDay = DateUtils.getNowYearMonthDayHHmmss();
		StringBuilder buffer = new StringBuilder();
		TreeTableAggregate treeTableAggregate = treeTableAggregateService.getTreeTableAggregate(treeTableAggregateId);
		if (treeTableAggregate != null && StringUtils.isNotEmpty(treeTableAggregate.getDatabaseIds())) {
			if (StringUtils.isNotEmpty(treeTableAggregate.getDatabaseIds())) {
				boolean dyncCondition = false;
				if (StringUtils.equals(treeTableAggregate.getDynamicCondition(), "Y")) {
					dyncCondition = true;

					for (int i = 1; i <= 10; i++) {
						String pname = request.getParameter("param_name_" + i);
						String ptype = request.getParameter("param_type_" + i);
						String pvalue = request.getParameter("param_value_" + i);
						if (StringUtils.isNotEmpty(pname) && StringUtils.isNotEmpty(pvalue)) {
							switch (ptype) {
							case "Integer":
								params.put(pname, Integer.parseInt(pvalue));
								break;
							case "Long":
								params.put(pname, Long.parseLong(pvalue));
								break;
							case "Double":
								params.put(pname, Double.parseDouble(pvalue));
								break;
							case "Date":
								params.put(pname, DateUtils.toDate(pvalue));
								break;
							default:
								params.put(pname, pvalue);
								break;
							}
						}
					}
				}

				treeTableAggregate.setSyncTime(new Date());
				StringTokenizer token = new StringTokenizer(treeTableAggregate.getDatabaseIds(), ",");
				while (token.hasMoreTokens()) {
					String x = token.nextToken();
					if (StringUtils.isNotEmpty(x) && StringUtils.isNumeric(x)) {
						jobNo = "treetable_aggregate_" + treeTableAggregate.getId() + "_" + x + "_" + runDay;
						try {
							database = databaseService.getDatabaseById(Long.parseLong(x));
							if (database != null) {
								TreeTableAggregateTask task = new TreeTableAggregateTask(database.getId(),
										treeTableAggregate.getId(), true, jobNo, dyncCondition ? params : null);
								if (task.execute()) {
									buffer.append(database.getTitle()).append("[").append(database.getMapping())
											.append("]执行成功。\n");
								} else {
									errorCount++;
									buffer.append(database.getTitle()).append("[").append(database.getMapping())
											.append("]执行失败。\n");
									StringBuffer sb = ExceptionUtils
											.getMsgBuffer("treetable_aggregate_" + treeTableAggregate.getId());
									buffer.append(sb.toString());
								}
							}
						} catch (Exception ex) {
							errorCount++;
						} finally {
							ExceptionUtils.clear("treetable_aggregate_" + treeTableAggregate.getId());
						}
					}
				}

				if (errorCount == 0) {
					treeTableAggregate.setSyncStatus(1);
				} else {
					treeTableAggregate.setSyncStatus(-1);
				}
				treeTableAggregateService.updateTreeTableAggregateStatus(treeTableAggregate);
			}
		}

		return ResponseUtils.responseJsonResult(errorCount == 0 ? true : false, buffer.toString());
	}

	@ResponseBody
	@RequestMapping("/executeAll")
	public byte[] executeAll(HttpServletRequest request) {
		int errorCount = 0;
		int perErrorCount = 0;
		String jobNo = null;
		Database database = null;
		String runDay = DateUtils.getNowYearMonthDayHHmmss();
		StringBuilder buffer = new StringBuilder();
		TreeTableAggregateQuery query = new TreeTableAggregateQuery();
		query.locked(0);
		query.deleteFlag(0);
		List<TreeTableAggregate> rows = treeTableAggregateService.list(query);
		if (rows != null && !rows.isEmpty()) {
			for (TreeTableAggregate treeTableAggregate : rows) {
				if (treeTableAggregate != null && StringUtils.isNotEmpty(treeTableAggregate.getDatabaseIds())) {
					if (StringUtils.isNotEmpty(treeTableAggregate.getDatabaseIds())) {
						perErrorCount = 0;
						treeTableAggregate.setSyncTime(new Date());
						buffer.append("\n").append(treeTableAggregate.getTitle()).append("执行结果：\n");
						StringTokenizer token = new StringTokenizer(treeTableAggregate.getDatabaseIds(), ",");
						while (token.hasMoreTokens()) {
							String x = token.nextToken();
							if (StringUtils.isNotEmpty(x) && StringUtils.isNumeric(x)) {
								jobNo = "treetable_aggregate_" + treeTableAggregate.getId() + "_" + x + "_" + runDay;
								try {
									database = databaseService.getDatabaseById(Long.parseLong(x));
									if (database != null) {
										TreeTableAggregateTask task = new TreeTableAggregateTask(database.getId(),
												treeTableAggregate.getId(), false, jobNo, null);
										if (task.execute()) {
											buffer.append(database.getTitle()).append("[").append(database.getMapping())
													.append("]执行成功。\n");
										} else {
											errorCount++;
											perErrorCount++;
											buffer.append(database.getTitle()).append("[").append(database.getMapping())
													.append("]执行失败。\n");
											StringBuffer sb = ExceptionUtils
													.getMsgBuffer("treetable_aggregate_" + treeTableAggregate.getId());
											buffer.append(sb.toString());
										}
									}
								} catch (Exception ex) {
									errorCount++;
									perErrorCount++;
								} finally {
									ExceptionUtils.clear("treetable_aggregate_" + treeTableAggregate.getId());
								}
							}
						}
						if (perErrorCount == 0) {
							treeTableAggregate.setSyncStatus(1);
						} else {
							treeTableAggregate.setSyncStatus(-1);
						}
						treeTableAggregateService.updateTreeTableAggregateStatus(treeTableAggregate);
					}
				}
			}
		}

		return ResponseUtils.responseJsonResult(errorCount == 0 ? true : false, buffer.toString());
	}

	@ResponseBody
	@RequestMapping("/executeSpec")
	public byte[] executeSpec(HttpServletRequest request) {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		long databaseId = RequestUtils.getLong(request, "databaseId");
		long treeTableAggregateId = RequestUtils.getLong(request, "treeTableAggregateId");
		int errorCount = 0;
		String jobNo = null;
		Database database = null;
		StringBuilder buffer = new StringBuilder();
		String runDay = DateUtils.getNowYearMonthDayHHmmss();
		TreeTableAggregate treeTableAggregate = treeTableAggregateService.getTreeTableAggregate(treeTableAggregateId);
		if (treeTableAggregate != null && databaseId > 0) {
			boolean dyncCondition = false;
			if (StringUtils.equals(treeTableAggregate.getDynamicCondition(), "Y")) {
				dyncCondition = true;
			}
			try {
				database = databaseService.getDatabaseById(databaseId);
				if (database != null) {
					jobNo = "treetable_aggregate_" + treeTableAggregate.getId() + "_" + databaseId + "_" + runDay;
					TreeTableAggregateTask task = new TreeTableAggregateTask(database.getId(),
							treeTableAggregate.getId(), false, jobNo, dyncCondition ? params : null);
					if (task.execute()) {
						buffer.append(database.getTitle()).append("[").append(database.getMapping()).append("]执行成功。\n");
					} else {
						errorCount++;
						buffer.append(database.getTitle()).append("[").append(database.getMapping()).append("]执行失败。\n");
						StringBuffer sb = ExceptionUtils
								.getMsgBuffer("treetable_aggregate_" + treeTableAggregate.getId());
						buffer.append(sb.toString());
					}
				}
			} catch (Exception ex) {
				errorCount++;
				logger.error(ex);
			} finally {
				ExceptionUtils.clear("treetable_aggregate_" + treeTableAggregate.getId());
			}
		}

		return ResponseUtils.responseJsonResult(errorCount == 0 ? true : false, buffer.toString());
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		TreeTableAggregateQuery query = new TreeTableAggregateQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setPrivateFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);
		/**
		 * 此处业务逻辑需自行调整
		 */
		if (!loginContext.isSystemAdministrator()) {
			String actorId = loginContext.getActorId();
			query.createBy(actorId);
		}

		int start = 0;
		int limit = 10;
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
		int total = treeTableAggregateService.getTreeTableAggregateCountByQueryCriteria(query);
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

			List<TreeTableAggregate> list = treeTableAggregateService.getTreeTableAggregatesByQueryCriteria(start,
					limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (TreeTableAggregate treeTableAggregate : list) {
					JSONObject rowJSON = treeTableAggregate.toJsonObject();
					rowJSON.put("id", treeTableAggregate.getId());
					rowJSON.put("rowId", treeTableAggregate.getId());
					rowJSON.put("aggregateId", treeTableAggregate.getId());
					rowJSON.put("treeTableAggregateId", treeTableAggregate.getId());
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

	@RequestMapping
	public ModelAndView list(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/matrix/treeTableAggregate/list", modelMap);
	}

	@RequestMapping("/param")
	public ModelAndView param(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		TreeTableAggregate treeTableAggregate = treeTableAggregateService
				.getTreeTableAggregate(RequestUtils.getLong(request, "id"));
		request.setAttribute("treeTableAggregate", treeTableAggregate);

		String view = request.getParameter("param");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view);
		}

		String x_view = ViewProperties.getString("treeTableAggregate.param");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view);
		}

		return new ModelAndView("/matrix/treeTableAggregate/param");
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("treeTableAggregate.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/matrix/treeTableAggregate/query", modelMap);
	}

	@RequestMapping("/save")
	public ModelAndView save(HttpServletRequest request, ModelMap modelMap) {
		String targetTableName = request.getParameter("targetTableName");
		if (!(StringUtils.startsWithIgnoreCase(targetTableName, "etl_")
				|| StringUtils.startsWithIgnoreCase(targetTableName, "tmp")
				|| StringUtils.startsWithIgnoreCase(targetTableName, "sync")
				|| StringUtils.startsWithIgnoreCase(targetTableName, "useradd")
				|| StringUtils.startsWithIgnoreCase(targetTableName, "tree_table_"))) {
			throw new RuntimeException("目标表必须以etl_,tmp,sync,useradd,tree_table_前缀开头。");
		}
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);

		TreeTableAggregate treeTableAggregate = new TreeTableAggregate();
		Tools.populate(treeTableAggregate, params);

		treeTableAggregate.setName(request.getParameter("name"));
		treeTableAggregate.setTitle(request.getParameter("title"));
		treeTableAggregate.setType(request.getParameter("type"));
		treeTableAggregate.setSourceTableName(request.getParameter("sourceTableName"));
		treeTableAggregate.setSourceIdColumn(request.getParameter("sourceIdColumn"));
		treeTableAggregate.setSourceIndexIdColumn(request.getParameter("sourceIndexIdColumn"));
		treeTableAggregate.setSourceParentIdColumn(request.getParameter("sourceParentIdColumn"));
		treeTableAggregate.setSourceTreeIdColumn(request.getParameter("sourceTreeIdColumn"));
		treeTableAggregate.setSourceTextColumn(request.getParameter("sourceTextColumn"));
		treeTableAggregate.setSourceWbsIndexColumn(request.getParameter("sourceWbsIndexColumn"));
		treeTableAggregate.setSourceTableDateSplitColumn(request.getParameter("sourceTableDateSplitColumn"));
		treeTableAggregate.setSourceTableExecutionIds(request.getParameter("sourceTableExecutionIds"));
		treeTableAggregate.setDatabaseIds(request.getParameter("databaseIds"));
		treeTableAggregate.setDynamicCondition(request.getParameter("dynamicCondition"));
		treeTableAggregate.setSqlCriteria(request.getParameter("sqlCriteria"));
		treeTableAggregate.setStartYear(RequestUtils.getInt(request, "startYear"));
		treeTableAggregate.setStopYear(RequestUtils.getInt(request, "stopYear"));
		treeTableAggregate.setTargetTableName(request.getParameter("targetTableName"));
		treeTableAggregate.setTargetTableExecutionIds(request.getParameter("targetTableExecutionIds"));
		treeTableAggregate.setCreateTableFlag(request.getParameter("createTableFlag"));
		treeTableAggregate.setAggregateFlag(request.getParameter("aggregateFlag"));
		treeTableAggregate.setScheduleFlag(request.getParameter("scheduleFlag"));
		treeTableAggregate.setGenByMonth(request.getParameter("genByMonth"));
		treeTableAggregate.setDeleteFetch(request.getParameter("deleteFetch"));
		treeTableAggregate.setSyncColumns(request.getParameter("syncColumns"));
		treeTableAggregate.setSyncStatus(RequestUtils.getInt(request, "syncStatus"));
		treeTableAggregate.setSyncTime(RequestUtils.getDate(request, "syncTime"));
		treeTableAggregate.setSortNo(RequestUtils.getInt(request, "sortNo"));
		treeTableAggregate.setLocked(RequestUtils.getInt(request, "locked"));
		treeTableAggregate.setCreateBy(actorId);
		treeTableAggregate.setUpdateBy(actorId);

		treeTableAggregateService.save(treeTableAggregate);

		return this.list(request, modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveAs")
	public byte[] saveAs(HttpServletRequest request) {
		String targetTableName = request.getParameter("targetTableName");
		if (!(StringUtils.startsWithIgnoreCase(targetTableName, "etl_")
				|| StringUtils.startsWithIgnoreCase(targetTableName, "tmp")
				|| StringUtils.startsWithIgnoreCase(targetTableName, "sync")
				|| StringUtils.startsWithIgnoreCase(targetTableName, "useradd")
				|| StringUtils.startsWithIgnoreCase(targetTableName, "tree_table_"))) {
			return ResponseUtils.responseJsonResult(false, "目标表必须以etl_,tmp,sync,useradd,tree_table_前缀开头。");
		}
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		long id = RequestUtils.getLong(request, "id");
		TreeTableAggregate treeTableAggregate = treeTableAggregateService.getTreeTableAggregate(id);
		try {
			if (treeTableAggregate == null) {
				treeTableAggregate = new TreeTableAggregate();
			}
			Tools.populate(treeTableAggregate, params);
			treeTableAggregate.setName(request.getParameter("name"));
			treeTableAggregate.setTitle(request.getParameter("title"));
			treeTableAggregate.setType(request.getParameter("type"));
			treeTableAggregate.setSourceTableName(request.getParameter("sourceTableName"));
			treeTableAggregate.setSourceIdColumn(request.getParameter("sourceIdColumn"));
			treeTableAggregate.setSourceIndexIdColumn(request.getParameter("sourceIndexIdColumn"));
			treeTableAggregate.setSourceParentIdColumn(request.getParameter("sourceParentIdColumn"));
			treeTableAggregate.setSourceTreeIdColumn(request.getParameter("sourceTreeIdColumn"));
			treeTableAggregate.setSourceTextColumn(request.getParameter("sourceTextColumn"));
			treeTableAggregate.setSourceWbsIndexColumn(request.getParameter("sourceWbsIndexColumn"));
			treeTableAggregate.setSourceTableDateSplitColumn(request.getParameter("sourceTableDateSplitColumn"));
			treeTableAggregate.setSourceTableExecutionIds(request.getParameter("sourceTableExecutionIds"));
			treeTableAggregate.setDatabaseIds(request.getParameter("databaseIds"));
			treeTableAggregate.setDynamicCondition(request.getParameter("dynamicCondition"));
			treeTableAggregate.setSqlCriteria(request.getParameter("sqlCriteria"));
			treeTableAggregate.setStartYear(RequestUtils.getInt(request, "startYear"));
			treeTableAggregate.setStopYear(RequestUtils.getInt(request, "stopYear"));
			treeTableAggregate.setCreateTableFlag(request.getParameter("createTableFlag"));
			treeTableAggregate.setTargetTableName(request.getParameter("targetTableName"));
			treeTableAggregate.setTargetTableExecutionIds(request.getParameter("targetTableExecutionIds"));
			treeTableAggregate.setAggregateFlag(request.getParameter("aggregateFlag"));
			treeTableAggregate.setScheduleFlag(request.getParameter("scheduleFlag"));
			treeTableAggregate.setGenByMonth(request.getParameter("genByMonth"));
			treeTableAggregate.setDeleteFetch(request.getParameter("deleteFetch"));
			treeTableAggregate.setSyncColumns(request.getParameter("syncColumns"));
			treeTableAggregate.setSyncStatus(RequestUtils.getInt(request, "syncStatus"));
			treeTableAggregate.setSyncTime(RequestUtils.getDate(request, "syncTime"));
			treeTableAggregate.setSortNo(RequestUtils.getInt(request, "sortNo"));
			treeTableAggregate.setLocked(RequestUtils.getInt(request, "locked"));
			treeTableAggregate.setCreateBy(actorId);
			treeTableAggregate.setUpdateBy(actorId);

			this.treeTableAggregateService.saveAs(treeTableAggregate);

			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	public @ResponseBody TreeTableAggregate saveOrUpdate(HttpServletRequest request,
			@RequestBody Map<String, Object> model) {
		String targetTableName = request.getParameter("targetTableName");
		if (!(StringUtils.startsWithIgnoreCase(targetTableName, "etl_")
				|| StringUtils.startsWithIgnoreCase(targetTableName, "tmp")
				|| StringUtils.startsWithIgnoreCase(targetTableName, "sync")
				|| StringUtils.startsWithIgnoreCase(targetTableName, "useradd")
				|| StringUtils.startsWithIgnoreCase(targetTableName, "tree_table_"))) {
			throw new RuntimeException("目标表必须以etl_,tmp,sync,useradd,tree_table_前缀开头。");
		}
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		TreeTableAggregate treeTableAggregate = new TreeTableAggregate();
		try {
			Tools.populate(treeTableAggregate, model);
			treeTableAggregate.setName(ParamUtils.getString(model, "name"));
			treeTableAggregate.setTitle(ParamUtils.getString(model, "title"));
			treeTableAggregate.setType(ParamUtils.getString(model, "type"));
			treeTableAggregate.setSourceTableName(ParamUtils.getString(model, "sourceTableName"));
			treeTableAggregate.setSourceIdColumn(ParamUtils.getString(model, "sourceIdColumn"));
			treeTableAggregate.setSourceIndexIdColumn(ParamUtils.getString(model, "sourceIndexIdColumn"));
			treeTableAggregate.setSourceParentIdColumn(ParamUtils.getString(model, "sourceParentIdColumn"));
			treeTableAggregate.setSourceTreeIdColumn(ParamUtils.getString(model, "sourceTreeIdColumn"));
			treeTableAggregate.setSourceTextColumn(ParamUtils.getString(model, "sourceTextColumn"));
			treeTableAggregate.setSourceWbsIndexColumn(ParamUtils.getString(model, "sourceWbsIndexColumn"));
			treeTableAggregate.setSourceTableDateSplitColumn(ParamUtils.getString(model, "sourceTableDateSplitColumn"));
			treeTableAggregate.setSourceTableExecutionIds(request.getParameter("sourceTableExecutionIds"));
			treeTableAggregate.setDatabaseIds(ParamUtils.getString(model, "databaseIds"));
			treeTableAggregate.setDynamicCondition(request.getParameter("dynamicCondition"));
			treeTableAggregate.setSqlCriteria(request.getParameter("sqlCriteria"));
			treeTableAggregate.setStartYear(RequestUtils.getInt(request, "startYear"));
			treeTableAggregate.setStopYear(RequestUtils.getInt(request, "stopYear"));
			treeTableAggregate.setTargetTableName(ParamUtils.getString(model, "targetTableName"));
			treeTableAggregate.setTargetTableExecutionIds(request.getParameter("targetTableExecutionIds"));
			treeTableAggregate.setCreateTableFlag(ParamUtils.getString(model, "createTableFlag"));
			treeTableAggregate.setAggregateFlag(ParamUtils.getString(model, "aggregateFlag"));
			treeTableAggregate.setScheduleFlag(request.getParameter("scheduleFlag"));
			treeTableAggregate.setGenByMonth(request.getParameter("genByMonth"));
			treeTableAggregate.setDeleteFetch(ParamUtils.getString(model, "deleteFetch"));
			treeTableAggregate.setSyncColumns(request.getParameter("syncColumns"));
			treeTableAggregate.setSyncStatus(ParamUtils.getInt(model, "syncStatus"));
			treeTableAggregate.setSyncTime(ParamUtils.getDate(model, "syncTime"));
			treeTableAggregate.setSortNo(ParamUtils.getInt(model, "sortNo"));
			treeTableAggregate.setLocked(ParamUtils.getInt(model, "locked"));
			treeTableAggregate.setCreateBy(actorId);
			treeTableAggregate.setUpdateBy(actorId);
			this.treeTableAggregateService.save(treeTableAggregate);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return treeTableAggregate;
	}

	@ResponseBody
	@RequestMapping("/saveTreeTableAggregate")
	public byte[] saveTreeTableAggregate(HttpServletRequest request) {
		String targetTableName = request.getParameter("targetTableName");
		if (!(StringUtils.startsWithIgnoreCase(targetTableName, "etl_")
				|| StringUtils.startsWithIgnoreCase(targetTableName, "tmp")
				|| StringUtils.startsWithIgnoreCase(targetTableName, "sync")
				|| StringUtils.startsWithIgnoreCase(targetTableName, "useradd")
				|| StringUtils.startsWithIgnoreCase(targetTableName, "tree_table_"))) {
			return ResponseUtils.responseJsonResult(false, "目标表必须以etl_,tmp,sync,useradd,tree_table_前缀开头。");
		}
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		TreeTableAggregate treeTableAggregate = new TreeTableAggregate();
		try {
			Tools.populate(treeTableAggregate, params);
			treeTableAggregate.setName(request.getParameter("name"));
			treeTableAggregate.setTitle(request.getParameter("title"));
			treeTableAggregate.setType(request.getParameter("type"));
			treeTableAggregate.setSourceTableName(request.getParameter("sourceTableName"));
			treeTableAggregate.setSourceIdColumn(request.getParameter("sourceIdColumn"));
			treeTableAggregate.setSourceIndexIdColumn(request.getParameter("sourceIndexIdColumn"));
			treeTableAggregate.setSourceParentIdColumn(request.getParameter("sourceParentIdColumn"));
			treeTableAggregate.setSourceTreeIdColumn(request.getParameter("sourceTreeIdColumn"));
			treeTableAggregate.setSourceTextColumn(request.getParameter("sourceTextColumn"));
			treeTableAggregate.setSourceWbsIndexColumn(request.getParameter("sourceWbsIndexColumn"));
			treeTableAggregate.setSourceTableDateSplitColumn(request.getParameter("sourceTableDateSplitColumn"));
			treeTableAggregate.setSourceTableExecutionIds(request.getParameter("sourceTableExecutionIds"));
			treeTableAggregate.setDatabaseIds(request.getParameter("databaseIds"));
			treeTableAggregate.setDynamicCondition(request.getParameter("dynamicCondition"));
			treeTableAggregate.setSqlCriteria(request.getParameter("sqlCriteria"));
			treeTableAggregate.setStartYear(RequestUtils.getInt(request, "startYear"));
			treeTableAggregate.setStopYear(RequestUtils.getInt(request, "stopYear"));
			treeTableAggregate.setTargetTableName(request.getParameter("targetTableName"));
			treeTableAggregate.setTargetTableExecutionIds(request.getParameter("targetTableExecutionIds"));
			treeTableAggregate.setCreateTableFlag(request.getParameter("createTableFlag"));
			treeTableAggregate.setAggregateFlag(request.getParameter("aggregateFlag"));
			treeTableAggregate.setScheduleFlag(request.getParameter("scheduleFlag"));
			treeTableAggregate.setGenByMonth(request.getParameter("genByMonth"));
			treeTableAggregate.setDeleteFetch(request.getParameter("deleteFetch"));
			treeTableAggregate.setSyncColumns(request.getParameter("syncColumns"));
			treeTableAggregate.setSyncStatus(RequestUtils.getInt(request, "syncStatus"));
			treeTableAggregate.setSyncTime(RequestUtils.getDate(request, "syncTime"));
			treeTableAggregate.setSortNo(RequestUtils.getInt(request, "sortNo"));
			treeTableAggregate.setLocked(RequestUtils.getInt(request, "locked"));
			treeTableAggregate.setCreateBy(actorId);
			treeTableAggregate.setUpdateBy(actorId);

			this.treeTableAggregateService.save(treeTableAggregate);

			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource
	public void setDatabaseService(IDatabaseService databaseService) {
		this.databaseService = databaseService;
	}

	@javax.annotation.Resource
	public void setExecutionLogService(ExecutionLogService executionLogService) {
		this.executionLogService = executionLogService;
	}

	@javax.annotation.Resource
	public void setTableDataService(ITableDataService tableDataService) {
		this.tableDataService = tableDataService;
	}

	@javax.annotation.Resource
	public void setTreeTableAggregateService(TreeTableAggregateService treeTableAggregateService) {
		this.treeTableAggregateService = treeTableAggregateService;
	}

	@RequestMapping("/view")
	public ModelAndView view(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		TreeTableAggregate treeTableAggregate = treeTableAggregateService
				.getTreeTableAggregate(RequestUtils.getLong(request, "id"));
		request.setAttribute("treeTableAggregate", treeTableAggregate);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view);
		}

		String x_view = ViewProperties.getString("treeTableAggregate.view");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view);
		}

		return new ModelAndView("/matrix/treeTableAggregate/view");
	}

}
