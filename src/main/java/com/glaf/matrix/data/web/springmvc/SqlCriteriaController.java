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
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import com.alibaba.fastjson.*;
import com.glaf.core.base.BaseTree;
import com.glaf.core.base.ColumnModel;
import com.glaf.core.base.TableModel;
import com.glaf.core.base.TreeModel;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.factory.DataServiceFactory;
import com.glaf.core.identity.*;
import com.glaf.core.security.*;
import com.glaf.core.tree.helper.TreeHelper;
import com.glaf.core.tree.helper.TreeUpdateBean;
import com.glaf.core.util.*;

import com.glaf.matrix.data.domain.*;
import com.glaf.matrix.data.query.*;
import com.glaf.matrix.data.service.*;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/sys/sqlCriteria")
@RequestMapping("/sys/sqlCriteria")
public class SqlCriteriaController {
	protected static final Log logger = LogFactory.getLog(SqlCriteriaController.class);

	protected ITableService tableService;

	protected SqlCriteriaService sqlCriteriaService;

	public SqlCriteriaController() {

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
					SqlCriteria sqlCriteria = sqlCriteriaService.getSqlCriteria(Long.valueOf(x));
					if (sqlCriteria != null && (StringUtils.equals(sqlCriteria.getCreateBy(), loginContext.getActorId())
							|| loginContext.isSystemAdministrator())) {
						sqlCriteriaService.deleteById(sqlCriteria.getId());
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			SqlCriteria sqlCriteria = sqlCriteriaService.getSqlCriteria(Long.valueOf(id));
			if (sqlCriteria != null && (StringUtils.equals(sqlCriteria.getCreateBy(), loginContext.getActorId())
					|| loginContext.isSystemAdministrator())) {
				sqlCriteriaService.deleteById(sqlCriteria.getId());
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String tableName = request.getParameter("tableName");

		SqlCriteria sqlCriteria = sqlCriteriaService.getSqlCriteria(RequestUtils.getLong(request, "id"));
		if (sqlCriteria != null) {
			tableName = sqlCriteria.getTableName();
			request.setAttribute("sqlCriteria", sqlCriteria);
		}

		String moduleId = request.getParameter("moduleId");
		String businessKey = request.getParameter("businessKey");

		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SqlCriteriaQuery query = new SqlCriteriaQuery();
		Tools.populate(query, params);
		query.businessKey(businessKey);
		query.moduleId(moduleId);

		List<SqlCriteria> sqlCriterias = sqlCriteriaService.list(query);

		Map<Long, TreeModel> treeMap = new HashMap<Long, TreeModel>();
		List<TreeModel> treeModels = new ArrayList<TreeModel>();
		for (SqlCriteria c : sqlCriterias) {
			treeModels.add(c);
			treeMap.put(c.getId(), c);
		}

		StringTokenizer token = null;
		StringBuilder blank = new StringBuilder();
		TreeUpdateBean bean = new TreeUpdateBean();
		List<SqlCriteria> treeList = new ArrayList<SqlCriteria>();
		for (SqlCriteria m : sqlCriterias) {
			String treeId = bean.getTreeId(treeMap, m);
			if (treeId != null && treeId.indexOf("|") != -1) {
				token = new StringTokenizer(treeId, "|");
				if (token != null) {
					m.setLevel(token.countTokens());
					blank.delete(0, blank.length());
					for (int i = 0; i < m.getLevel(); i++) {
						blank.append("&nbsp;&nbsp;&nbsp;&nbsp;");
					}
					m.setBlank(blank.toString());
				}
			}
			treeList.add(m);
		}
		request.setAttribute("sqlCriterias", treeList);

		SysTableQuery query2 = new SysTableQuery();
		query2.type("useradd");
		query2.locked(0);
		List<SysTable> tables = tableService.list(query2);
		if (tables != null && !tables.isEmpty()) {
			for (SysTable table : tables) {
				if (StringUtils.equalsIgnoreCase(tableName, table.getTableName())) {
					request.setAttribute("table", table);
					break;
				}
			}
		}
		request.setAttribute("tables", tables);

		if (StringUtils.isNotEmpty(tableName)) {
			List<TableColumn> columns = tableService.getTableColumnsByTableName(tableName);
			request.setAttribute("columns", columns);
			logger.debug("columns:" + columns);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("sqlCriteria.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/sqlCriteria/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SqlCriteriaQuery query = new SqlCriteriaQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
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
		int total = sqlCriteriaService.getSqlCriteriaCountByQueryCriteria(query);
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

			List<SqlCriteria> list = sqlCriteriaService.getSqlCriteriasByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (SqlCriteria sqlCriteria : list) {
					JSONObject rowJSON = sqlCriteria.toJsonObject();
					rowJSON.put("id", sqlCriteria.getId());
					rowJSON.put("rowId", sqlCriteria.getId());
					rowJSON.put("sqlCriteriaId", sqlCriteria.getId());
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

		return new ModelAndView("/sys/sqlCriteria/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("sqlCriteria.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/sys/sqlCriteria/query", modelMap);
	}

	@RequestMapping("/save")
	public ModelAndView save(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);

		SqlCriteria sqlCriteria = new SqlCriteria();
		Tools.populate(sqlCriteria, params);

		sqlCriteria.setParentId(RequestUtils.getLong(request, "parentId"));
		sqlCriteria.setTenantId(loginContext.getTenantId());
		sqlCriteria.setName(request.getParameter("name"));
		sqlCriteria.setModuleId(request.getParameter("moduleId"));
		sqlCriteria.setBusinessKey(request.getParameter("businessKey"));
		sqlCriteria.setColumnName(request.getParameter("columnName"));
		sqlCriteria.setColumnType(request.getParameter("columnType"));
		sqlCriteria.setTableName(request.getParameter("tableName"));
		sqlCriteria.setTableAlias(request.getParameter("tableAlias"));
		sqlCriteria.setParamName(request.getParameter("paramName"));
		sqlCriteria.setParamTitle(request.getParameter("paramTitle"));
		sqlCriteria.setCollectionFlag(request.getParameter("collectionFlag"));
		sqlCriteria.setRequiredFlag(request.getParameter("requiredFlag"));
		sqlCriteria.setCondition(request.getParameter("condition"));
		sqlCriteria.setOperator(request.getParameter("operator"));
		sqlCriteria.setSeparator(request.getParameter("separator"));
		sqlCriteria.setSql(request.getParameter("sql"));
		sqlCriteria.setLocked(RequestUtils.getInt(request, "locked"));
		sqlCriteria.setCreateBy(actorId);
		sqlCriteriaService.save(sqlCriteria);

		return this.list(request, modelMap);
	}

	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	public @ResponseBody SqlCriteria saveOrUpdate(HttpServletRequest request, @RequestBody Map<String, Object> model) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		SqlCriteria sqlCriteria = new SqlCriteria();
		try {
			Tools.populate(sqlCriteria, model);
			sqlCriteria.setParentId(ParamUtils.getLong(model, "parentId"));
			sqlCriteria.setTenantId(loginContext.getTenantId());
			sqlCriteria.setName(ParamUtils.getString(model, "name"));
			sqlCriteria.setModuleId(ParamUtils.getString(model, "moduleId"));
			sqlCriteria.setBusinessKey(ParamUtils.getString(model, "businessKey"));
			sqlCriteria.setColumnName(ParamUtils.getString(model, "columnName"));
			sqlCriteria.setColumnType(ParamUtils.getString(model, "columnType"));
			sqlCriteria.setTableName(ParamUtils.getString(model, "tableName"));
			sqlCriteria.setTableAlias(ParamUtils.getString(model, "tableAlias"));
			sqlCriteria.setParamName(ParamUtils.getString(model, "paramName"));
			sqlCriteria.setParamTitle(ParamUtils.getString(model, "paramTitle"));
			sqlCriteria.setCollectionFlag(ParamUtils.getString(model, "collectionFlag"));
			sqlCriteria.setRequiredFlag(request.getParameter("requiredFlag"));
			sqlCriteria.setCondition(ParamUtils.getString(model, "condition"));
			sqlCriteria.setOperator(request.getParameter("operator"));
			sqlCriteria.setSeparator(ParamUtils.getString(model, "separator"));
			sqlCriteria.setSql(ParamUtils.getString(model, "sql"));
			sqlCriteria.setLocked(ParamUtils.getInt(model, "locked"));
			sqlCriteria.setCreateBy(actorId);
			sqlCriteria.setUpdateBy(actorId);

			this.sqlCriteriaService.save(sqlCriteria);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return sqlCriteria;
	}

	/**
	 * 提交增加信息
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/saveSort")
	@ResponseBody
	public byte[] saveSort(HttpServletRequest request) {
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
					t1.setTableName("SYS_SQL_CRITERIA");
					ColumnModel idColumn1 = new ColumnModel();
					idColumn1.setColumnName("ID_");
					idColumn1.setValue(Long.parseLong(item));
					t1.setIdColumn(idColumn1);
					ColumnModel column = new ColumnModel();
					column.setColumnName("SORTNO_");
					column.setValue(sort);
					t1.addColumn(column);
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
		return ResponseUtils.responseResult(false);
	}

	@ResponseBody
	@RequestMapping("/saveSqlCriteria")
	public byte[] saveSqlCriteria(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SqlCriteria sqlCriteria = new SqlCriteria();
		try {
			Tools.populate(sqlCriteria, params);
			sqlCriteria.setParentId(RequestUtils.getLong(request, "parentId"));
			sqlCriteria.setTenantId(loginContext.getActorId());
			sqlCriteria.setName(request.getParameter("name"));
			sqlCriteria.setModuleId(request.getParameter("moduleId"));
			sqlCriteria.setBusinessKey(request.getParameter("businessKey"));
			sqlCriteria.setColumnName(request.getParameter("columnName"));
			sqlCriteria.setColumnType(request.getParameter("columnType"));
			sqlCriteria.setTableName(request.getParameter("tableName"));
			sqlCriteria.setTableAlias(request.getParameter("tableAlias"));
			sqlCriteria.setParamName(request.getParameter("paramName"));
			sqlCriteria.setParamTitle(request.getParameter("paramTitle"));
			sqlCriteria.setCollectionFlag(request.getParameter("collectionFlag"));
			sqlCriteria.setRequiredFlag(request.getParameter("requiredFlag"));
			sqlCriteria.setCondition(request.getParameter("condition"));
			sqlCriteria.setOperator(request.getParameter("operator"));
			sqlCriteria.setSeparator(request.getParameter("separator"));
			sqlCriteria.setSql(request.getParameter("sql"));
			sqlCriteria.setLocked(RequestUtils.getInt(request, "locked"));
			sqlCriteria.setCreateBy(actorId);
			sqlCriteria.setUpdateBy(actorId);
			this.sqlCriteriaService.save(sqlCriteria);

			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource
	public void setSqlCriteriaService(SqlCriteriaService sqlCriteriaService) {
		this.sqlCriteriaService = sqlCriteriaService;
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
		long parentId = RequestUtils.getLongParameter(request, "parentId", 0);
		String moduleId = request.getParameter("moduleId");
		String businessKey = request.getParameter("businessKey");

		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SqlCriteriaQuery query = new SqlCriteriaQuery();
		Tools.populate(query, params);
		query.businessKey(businessKey);
		query.moduleId(moduleId);
		query.parentId(parentId);

		List<SqlCriteria> sqlCriterias = sqlCriteriaService.list(query);
		request.setAttribute("sqlCriterias", sqlCriterias);

		String x_view = ViewProperties.getString("sqlCriteria.showSort");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/sqlCriteria/showSort", modelMap);
	}

	@ResponseBody
	@RequestMapping("/treeJson")
	public byte[] treeJson(HttpServletRequest request) throws IOException {
		String moduleId = request.getParameter("moduleId");
		String businessKey = request.getParameter("businessKey");

		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SqlCriteriaQuery query = new SqlCriteriaQuery();
		Tools.populate(query, params);
		query.businessKey(businessKey);
		query.moduleId(moduleId);

		List<SqlCriteria> sqlCriterias = sqlCriteriaService.list(query);
		if (sqlCriterias != null && !sqlCriterias.isEmpty()) {
			List<TreeModel> trees = new ArrayList<TreeModel>();
			for (SqlCriteria p : sqlCriterias) {
				TreeModel t = new BaseTree();
				t.setId(p.getId());
				t.setParentId(p.getParentId());
				t.setTreeId(p.getTreeId());
				t.setIcon(p.getIcon());
				t.setIconCls(p.getIconCls());
				t.setName(p.getName());
				t.setCode(p.getCode());
				t.setDiscriminator(p.getDiscriminator());
				t.setLocked(p.getLocked());
				t.setCreateBy(p.getCreateBy());
				t.setCreateDate(p.getCreateTime());
				trees.add(t);
			}
			TreeHelper treeHelper = new TreeHelper();
			JSONArray result = treeHelper.getTreeJSONArray(trees);
			logger.debug(result.toJSONString());
			return result.toJSONString().getBytes("UTF-8");
		}

		JSONArray result = new JSONArray();
		return result.toJSONString().getBytes("UTF-8");
	}

	@RequestMapping("/update")
	public ModelAndView update(HttpServletRequest request, ModelMap modelMap) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);

		SqlCriteria sqlCriteria = sqlCriteriaService.getSqlCriteria(RequestUtils.getLong(request, "id"));

		Tools.populate(sqlCriteria, params);

		sqlCriteria.setParentId(RequestUtils.getLong(request, "parentId"));
		sqlCriteria.setTenantId(request.getParameter("tenantId"));
		sqlCriteria.setName(request.getParameter("name"));
		sqlCriteria.setModuleId(request.getParameter("moduleId"));
		sqlCriteria.setBusinessKey(request.getParameter("businessKey"));
		sqlCriteria.setColumnName(request.getParameter("columnName"));
		sqlCriteria.setColumnType(request.getParameter("columnType"));
		sqlCriteria.setTableName(request.getParameter("tableName"));
		sqlCriteria.setTableAlias(request.getParameter("tableAlias"));
		sqlCriteria.setParamName(request.getParameter("paramName"));
		sqlCriteria.setParamTitle(request.getParameter("paramTitle"));
		sqlCriteria.setCollectionFlag(request.getParameter("collectionFlag"));
		sqlCriteria.setRequiredFlag(request.getParameter("requiredFlag"));
		sqlCriteria.setCondition(request.getParameter("condition"));
		sqlCriteria.setOperator(request.getParameter("operator"));
		sqlCriteria.setSeparator(request.getParameter("separator"));
		sqlCriteria.setSql(request.getParameter("sql"));
		sqlCriteria.setLocked(RequestUtils.getInt(request, "locked"));
		sqlCriteria.setUpdateBy(actorId);
		sqlCriteriaService.save(sqlCriteria);

		return this.list(request, modelMap);
	}

	@RequestMapping("/view")
	public ModelAndView view(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		SqlCriteria sqlCriteria = sqlCriteriaService.getSqlCriteria(RequestUtils.getLong(request, "id"));
		request.setAttribute("sqlCriteria", sqlCriteria);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view);
		}

		String x_view = ViewProperties.getString("sqlCriteria.view");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view);
		}

		return new ModelAndView("/sys/sqlCriteria/view");
	}

}
