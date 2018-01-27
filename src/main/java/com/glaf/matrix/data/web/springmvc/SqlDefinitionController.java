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

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import com.glaf.core.config.ViewProperties;
import com.glaf.core.identity.User;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.JdbcUtils;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.StringTools;
import com.glaf.core.util.Tools;
import com.glaf.core.util.security.RSAUtils;
import com.glaf.matrix.data.domain.SqlDefinition;
import com.glaf.matrix.data.query.SqlDefinitionQuery;
import com.glaf.matrix.data.service.SqlDefinitionService;

@Controller("/sys/sql/definition")
@RequestMapping("/sys/sql/definition")
public class SqlDefinitionController {
	protected static final Log logger = LogFactory.getLog(SqlDefinitionController.class);

	protected IDatabaseService databaseService;

	protected SqlDefinitionService sqlDefinitionService;

	public SqlDefinitionController() {

	}

	@RequestMapping("/choose")
	public ModelAndView choose(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		SqlDefinitionQuery query = new SqlDefinitionQuery();
		List<SqlDefinition> list = sqlDefinitionService.list(query);
		request.setAttribute("list", list);

		String selected = request.getParameter("selected");
		if (StringUtils.isNotEmpty(selected)) {
			List<Long> ids = StringTools.splitToLong(selected);
			List<SqlDefinition> selectedList = new ArrayList<SqlDefinition>();
			List<SqlDefinition> unselectedList = new ArrayList<SqlDefinition>();
			for (SqlDefinition def : list) {
				if (ids.contains(def.getId())) {
					selectedList.add(def);
				} else {
					unselectedList.add(def);
				}
			}
			request.setAttribute("selectedList", selectedList);
			request.setAttribute("list", unselectedList);
		}

		String x_view = ViewProperties.getString("sqlDefinition.choose");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/sqlDefinition/choose", modelMap);
	}

	@ResponseBody
	@RequestMapping("/delete")
	public byte[] delete(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Long id = RequestUtils.getLong(request, "id");
		if (id != null && id > 0) {
			SqlDefinition sqlDefinition = sqlDefinitionService.getSqlDefinition(id);
			if (sqlDefinition != null && (StringUtils.equals(sqlDefinition.getCreateBy(), loginContext.getActorId())
					|| loginContext.isSystemAdministrator())) {
				sqlDefinitionService.deleteById(id);
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@ResponseBody
	@RequestMapping("/drop")
	public byte[] drop(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext != null && loginContext.isSystemAdministrator()) {
			SqlDefinitionQuery query = new SqlDefinitionQuery();
			List<SqlDefinition> list = sqlDefinitionService.list(query);
			if (list != null && !list.isEmpty()) {
				Connection connection = null;
				Statement statement = null;
				try {
					connection = DBConnectionFactory.getConnection();
					connection.setAutoCommit(false);
					for (SqlDefinition sqlDef : list) {
						String tableName = "SQL_RESULT" + sqlDef.getId();
						if (StringUtils.isNotEmpty(sqlDef.getTargetTableName())
								&& StringUtils.startsWithIgnoreCase(sqlDef.getTargetTableName(), "SQL_RESULT")) {
							tableName = sqlDef.getTargetTableName();
						}
						if (DBUtils.tableExists(connection, tableName)) {
							statement = connection.createStatement();
							statement.executeUpdate(" drop table " + tableName);
							connection.commit();
							JdbcUtils.close(statement);
							logger.info("drop table:" + tableName);
						}
					}
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				} finally {
					JdbcUtils.close(connection);
					JdbcUtils.close(statement);
				}
			}
			return ResponseUtils.responseResult(true);
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SqlDefinitionQuery query = new SqlDefinitionQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		if (!loginContext.isSystemAdministrator()) {
			String actorId = loginContext.getActorId();
			query.createBy(actorId);
		}

		SqlDefinition sqlDefinition = sqlDefinitionService.getSqlDefinition(RequestUtils.getLong(request, "id"));
		List<SqlDefinition> list = sqlDefinitionService.list(query);
		if (list != null && !list.isEmpty() && sqlDefinition != null) {
			for (SqlDefinition m : list) {
				if (m.getId() == sqlDefinition.getId()) {
					list.remove(m);
					break;
				}
			}
		}

		request.setAttribute("submitFlag", "true");

		if (sqlDefinition != null) {
			if (!StringUtils.equals(sqlDefinition.getCreateBy(), loginContext.getActorId())) {
				request.setAttribute("submitFlag", "false");
			}
			request.setAttribute("sqlDefinition", sqlDefinition);
			request.setAttribute("sqlDefId_enc", RSAUtils.encryptString(String.valueOf(sqlDefinition.getId())));
		}

		if (loginContext.isSystemAdministrator()) {
			request.setAttribute("submitFlag", "true");
		}

		request.setAttribute("parentList", list);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("sqlDefinition.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/sqlDefinition/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SqlDefinitionQuery query = new SqlDefinitionQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

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
		int total = sqlDefinitionService.getSqlDefinitionCountByQueryCriteria(query);
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

			List<SqlDefinition> list = sqlDefinitionService.getSqlDefinitionsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (SqlDefinition sqlDefinition : list) {
					JSONObject rowJSON = sqlDefinition.toJsonObject();
					rowJSON.put("id", sqlDefinition.getId());
					rowJSON.put("rowId", sqlDefinition.getId());
					rowJSON.put("sqlDefinitionId", sqlDefinition.getId());
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

		return new ModelAndView("/sys/sqlDefinition/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("sqlDefinition.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/sys/sqlDefinition/query", modelMap);
	}

	@RequestMapping("/save")
	public ModelAndView save(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);

		SqlDefinition sqlDefinition = new SqlDefinition();
		Tools.populate(sqlDefinition, params);

		sqlDefinition.setParentId(RequestUtils.getLong(request, "parentId"));
		sqlDefinition.setName(request.getParameter("name"));
		sqlDefinition.setCode(request.getParameter("code"));
		sqlDefinition.setTitle(request.getParameter("title"));
		sqlDefinition.setSql(request.getParameter("sql"));
		sqlDefinition.setCountSql(request.getParameter("countSql"));
		sqlDefinition.setRowKey(request.getParameter("rowKey"));
		sqlDefinition.setFetchFlag(request.getParameter("fetchFlag"));
		sqlDefinition.setDeleteFetch(request.getParameter("deleteFetch"));
		sqlDefinition.setScheduleFlag(request.getParameter("scheduleFlag"));
		sqlDefinition.setShareFlag(request.getParameter("shareFlag"));
		sqlDefinition.setSaveFlag(request.getParameter("saveFlag"));
		sqlDefinition.setPublicFlag(request.getParameter("publicFlag"));
		sqlDefinition.setExportFlag(request.getParameter("exportFlag"));
		sqlDefinition.setExportTableName(request.getParameter("exportTableName"));
		sqlDefinition.setExportTemplate(request.getParameter("exportTemplate"));
		sqlDefinition.setTargetTableName(request.getParameter("targetTableName"));
		sqlDefinition.setAggregationFlag(request.getParameter("aggregationFlag"));
		sqlDefinition.setCreateBy(actorId);
		sqlDefinition.setUpdateBy(actorId);

		if (!DBUtils.isLegalQuerySql(sqlDefinition.getSql())) {
			throw new RuntimeException(" SQL statement illegal ");
		}

		if (!DBUtils.isAllowedSql(sqlDefinition.getSql())) {
			throw new RuntimeException(" SQL statement illegal ");
		}

		sqlDefinition.setOperation("select");
		sqlDefinitionService.save(sqlDefinition);

		return this.list(request, modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveAsSqlDefinition")
	public byte[] saveAsSqlDefinition(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SqlDefinition sqlDefinition = new SqlDefinition();
		try {
			Tools.populate(sqlDefinition, params);
			sqlDefinition.setParentId(RequestUtils.getLong(request, "parentId"));
			sqlDefinition.setName(request.getParameter("name"));
			sqlDefinition.setCode(request.getParameter("code"));
			sqlDefinition.setTitle(request.getParameter("title"));
			sqlDefinition.setSql(request.getParameter("sql"));
			sqlDefinition.setCountSql(request.getParameter("countSql"));
			sqlDefinition.setRowKey(request.getParameter("rowKey"));
			sqlDefinition.setDataItemFlag(request.getParameter("dataItemFlag"));
			sqlDefinition.setFetchFlag(request.getParameter("fetchFlag"));
			sqlDefinition.setDeleteFetch(request.getParameter("deleteFetch"));
			sqlDefinition.setScheduleFlag(request.getParameter("scheduleFlag"));
			sqlDefinition.setShareFlag(request.getParameter("shareFlag"));
			sqlDefinition.setSaveFlag(request.getParameter("saveFlag"));
			sqlDefinition.setPublicFlag(request.getParameter("publicFlag"));
			sqlDefinition.setExportFlag(request.getParameter("exportFlag"));
			sqlDefinition.setExportTableName(request.getParameter("exportTableName"));
			sqlDefinition.setExportTemplate(request.getParameter("exportTemplate"));
			sqlDefinition.setTargetTableName(request.getParameter("targetTableName"));
			sqlDefinition.setAggregationFlag(request.getParameter("aggregationFlag"));
			sqlDefinition.setCreateBy(actorId);
			sqlDefinition.setUpdateBy(actorId);
			sqlDefinition.setId(0L);

			if (!DBUtils.isLegalQuerySql(sqlDefinition.getSql())) {
				throw new RuntimeException(" SQL statement illegal ");
			}

			if (!DBUtils.isAllowedSql(sqlDefinition.getSql())) {
				throw new RuntimeException(" SQL statement illegal ");
			}

			sqlDefinition.setOperation("select");
			this.sqlDefinitionService.save(sqlDefinition);

			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@ResponseBody
	@RequestMapping("/saveSqlDefinition")
	public byte[] saveSqlDefinition(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SqlDefinition sqlDefinition = new SqlDefinition();
		try {
			Tools.populate(sqlDefinition, params);
			sqlDefinition.setParentId(RequestUtils.getLong(request, "parentId"));
			sqlDefinition.setName(request.getParameter("name"));
			sqlDefinition.setCode(request.getParameter("code"));
			sqlDefinition.setTitle(request.getParameter("title"));
			sqlDefinition.setSql(request.getParameter("sql"));
			sqlDefinition.setCountSql(request.getParameter("countSql"));
			sqlDefinition.setRowKey(request.getParameter("rowKey"));
			sqlDefinition.setFetchFlag(request.getParameter("fetchFlag"));
			sqlDefinition.setDeleteFetch(request.getParameter("deleteFetch"));
			sqlDefinition.setScheduleFlag(request.getParameter("scheduleFlag"));
			sqlDefinition.setShareFlag(request.getParameter("shareFlag"));
			sqlDefinition.setSaveFlag(request.getParameter("saveFlag"));
			sqlDefinition.setPublicFlag(request.getParameter("publicFlag"));
			sqlDefinition.setExportFlag(request.getParameter("exportFlag"));
			sqlDefinition.setExportTableName(request.getParameter("exportTableName"));
			sqlDefinition.setExportTemplate(request.getParameter("exportTemplate"));
			sqlDefinition.setTargetTableName(request.getParameter("targetTableName"));
			sqlDefinition.setAggregationFlag(request.getParameter("aggregationFlag"));
			sqlDefinition.setCreateBy(actorId);
			sqlDefinition.setUpdateBy(actorId);

			if (!DBUtils.isLegalQuerySql(sqlDefinition.getSql())) {
				throw new RuntimeException(" SQL statement illegal ");
			}

			if (!DBUtils.isAllowedSql(sqlDefinition.getSql())) {
				throw new RuntimeException(" SQL statement illegal ");
			}

			sqlDefinition.setOperation("select");
			this.sqlDefinitionService.save(sqlDefinition);

			return ResponseUtils.responseJsonResult(true);
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
	public void setSqlDefinitionService(SqlDefinitionService sqlDefinitionService) {
		this.sqlDefinitionService = sqlDefinitionService;
	}

	@RequestMapping("/showList")
	public ModelAndView showList(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/sys/sqlDefinition/showList", modelMap);
	}

	@RequestMapping("/update")
	public ModelAndView update(HttpServletRequest request, ModelMap modelMap) {
		User user = RequestUtils.getUser(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);

		SqlDefinition sqlDefinition = sqlDefinitionService.getSqlDefinition(RequestUtils.getLong(request, "id"));

		Tools.populate(sqlDefinition, params);

		sqlDefinition.setParentId(RequestUtils.getLong(request, "parentId"));
		sqlDefinition.setName(request.getParameter("name"));
		sqlDefinition.setCode(request.getParameter("code"));
		sqlDefinition.setTitle(request.getParameter("title"));
		sqlDefinition.setSql(request.getParameter("sql"));
		sqlDefinition.setCountSql(request.getParameter("countSql"));
		sqlDefinition.setRowKey(request.getParameter("rowKey"));
		sqlDefinition.setDataItemFlag(request.getParameter("dataItemFlag"));
		sqlDefinition.setFetchFlag(request.getParameter("fetchFlag"));
		sqlDefinition.setDeleteFetch(request.getParameter("deleteFetch"));
		sqlDefinition.setScheduleFlag(request.getParameter("scheduleFlag"));
		sqlDefinition.setPublicFlag(request.getParameter("publicFlag"));
		sqlDefinition.setExportFlag(request.getParameter("exportFlag"));
		sqlDefinition.setExportTableName(request.getParameter("exportTableName"));
		sqlDefinition.setExportTemplate(request.getParameter("exportTemplate"));
		sqlDefinition.setTargetTableName(request.getParameter("targetTableName"));
		sqlDefinition.setAggregationFlag(request.getParameter("aggregationFlag"));
		sqlDefinition.setUpdateBy(user.getActorId());

		if (!DBUtils.isLegalQuerySql(sqlDefinition.getSql())) {
			throw new RuntimeException(" SQL statement illegal ");
		}

		if (!DBUtils.isAllowedSql(sqlDefinition.getSql())) {
			throw new RuntimeException(" SQL statement illegal ");
		}

		sqlDefinition.setOperation("select");
		sqlDefinitionService.save(sqlDefinition);

		return this.list(request, modelMap);
	}

}
