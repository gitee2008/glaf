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
import java.util.ArrayList;
import java.util.HashMap;
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
import com.glaf.core.base.ColumnModel;
import com.glaf.core.base.TableModel;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.factory.DataServiceFactory;
import com.glaf.core.identity.User;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;
import com.glaf.matrix.data.domain.SysTable;
import com.glaf.matrix.data.domain.TableCorrelation;
import com.glaf.matrix.data.query.TableCorrelationQuery;
import com.glaf.matrix.data.service.ITableService;
import com.glaf.matrix.data.service.TableCorrelationService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/sys/tableCorrelation")
@RequestMapping("/sys/tableCorrelation")
public class TableCorrelationController {
	protected static final Log logger = LogFactory.getLog(TableCorrelationController.class);

	protected ITableService tableService;

	protected TableCorrelationService tableCorrelationService;

	public TableCorrelationController() {

	}

	@ResponseBody
	@RequestMapping("/delete")
	public byte[] delete(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String id = RequestUtils.getString(request, "id");
		String ids = request.getParameter("ids");
		if (StringUtils.isNotEmpty(ids)) {
			StringTokenizer token = new StringTokenizer(ids, ",");
			while (token.hasMoreTokens()) {
				String x = token.nextToken();
				if (StringUtils.isNotEmpty(x)) {
					TableCorrelation tableCorrelation = tableCorrelationService.getTableCorrelation(String.valueOf(x));
					if (tableCorrelation != null
							&& (StringUtils.equals(tableCorrelation.getCreateBy(), loginContext.getActorId())
									|| loginContext.isSystemAdministrator())) {
						tableCorrelationService.deleteById(tableCorrelation.getId());
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			TableCorrelation tableCorrelation = tableCorrelationService.getTableCorrelation(String.valueOf(id));
			if (tableCorrelation != null
					&& (StringUtils.equals(tableCorrelation.getCreateBy(), loginContext.getActorId())
							|| loginContext.isSystemAdministrator())) {
				tableCorrelationService.deleteById(tableCorrelation.getId());
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String tableId = request.getParameter("tableId");

		TableCorrelation tableCorrelation = tableCorrelationService.getTableCorrelation(request.getParameter("id"));
		if (tableCorrelation != null) {
			tableId = tableCorrelation.getMasterTableId();
			request.setAttribute("tableCorrelation", tableCorrelation);
		}

		SysTable masterTable = null;
		List<SysTable> tables = tableService.getAllSysTables();
		Map<String, String> tableMap = new HashMap<String, String>();
		if (tables != null && !tables.isEmpty()) {
			for (SysTable t : tables) {
				tableMap.put(t.getTableId(), t.getTitle());
				if (StringUtils.equals(tableId, t.getTableId())) {
					masterTable = t;
					request.setAttribute("masterTable", t);
				}
			}
			tables.remove(masterTable);
			request.setAttribute("tables", tables);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("tableCorrelation.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/tableCorrelation/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		TableCorrelationQuery query = new TableCorrelationQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);
		query.masterTableId(request.getParameter("tableId"));

		if (!loginContext.isSystemAdministrator()) {
			String actorId = loginContext.getActorId();
			query.createBy(actorId);
		}

		List<TableCorrelation> list = tableCorrelationService.getTableCorrelationsByQueryCriteria(0, 100, query);
		JSONObject result = new JSONObject();
		if (list != null && !list.isEmpty()) {
			JSONArray rowsJSON = new JSONArray();
			int start = 0;
			int total = list.size();
			result.put("total", total);
			result.put("totalCount", total);
			result.put("totalRecords", total);
			result.put("start", 0);
			result.put("startIndex", 0);
			result.put("limit", total);
			result.put("pageSize", total);
			result.put("rows", rowsJSON);

			List<SysTable> tables = tableService.getAllSysTables();
			Map<String, String> tableMap = new HashMap<String, String>();
			if (tables != null && !tables.isEmpty()) {
				for (SysTable t : tables) {
					tableMap.put(t.getTableId(), t.getTitle());
				}
			}

			for (TableCorrelation tableCorrelation : list) {
				JSONObject rowJSON = tableCorrelation.toJsonObject();
				rowJSON.put("id", tableCorrelation.getId());
				rowJSON.put("tableCorrelationId", tableCorrelation.getId());
				rowJSON.put("masterTableTitle", tableMap.get(tableCorrelation.getMasterTableId()));
				rowJSON.put("slaveTableTitle", tableMap.get(tableCorrelation.getSlaveTableId()));
				rowJSON.put("startIndex", ++start);
				rowsJSON.add(rowJSON);
			}
			result.put("rows", rowsJSON);

		} else {
			JSONArray rowsJSON = new JSONArray();
			result.put("rows", rowsJSON);
			result.put("total", 0);
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

		return new ModelAndView("/sys/tableCorrelation/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("tableCorrelation.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/sys/tableCorrelation/query", modelMap);
	}

	@RequestMapping("/save")
	public ModelAndView save(HttpServletRequest request, ModelMap modelMap) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);

		TableCorrelation tableCorrelation = new TableCorrelation();
		Tools.populate(tableCorrelation, params);

		tableCorrelation.setMasterTableId(request.getParameter("masterTableId"));
		tableCorrelation.setSlaveTableId(request.getParameter("slaveTableId"));
		tableCorrelation.setInsertCascade(request.getParameter("insertCascade"));
		tableCorrelation.setDeleteCascade(request.getParameter("deleteCascade"));
		tableCorrelation.setUpdateCascade(request.getParameter("updateCascade"));
		tableCorrelation.setRelationshipType(request.getParameter("relationshipType"));
		tableCorrelation.setCreateBy(actorId);

		List<SysTable> tables = tableService.getAllSysTables();
		Map<String, String> tableMap = new HashMap<String, String>();
		if (tables != null && !tables.isEmpty()) {
			for (SysTable t : tables) {
				tableMap.put(t.getTableId(), t.getTableName());
			}
		}

		tableCorrelation.setMasterTableName(tableMap.get(tableCorrelation.getMasterTableId()));
		tableCorrelation.setSlaveTableName(tableMap.get(tableCorrelation.getSlaveTableId()));

		tableCorrelationService.save(tableCorrelation);

		return this.list(request, modelMap);
	}

	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	public @ResponseBody TableCorrelation saveOrUpdate(HttpServletRequest request,
			@RequestBody Map<String, Object> model) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		TableCorrelation tableCorrelation = new TableCorrelation();
		try {
			Tools.populate(tableCorrelation, model);
			tableCorrelation.setMasterTableId(ParamUtils.getString(model, "masterTableId"));
			tableCorrelation.setSlaveTableId(ParamUtils.getString(model, "slaveTableId"));
			tableCorrelation.setInsertCascade(ParamUtils.getString(model, "insertCascade"));
			tableCorrelation.setDeleteCascade(ParamUtils.getString(model, "deleteCascade"));
			tableCorrelation.setUpdateCascade(ParamUtils.getString(model, "updateCascade"));
			tableCorrelation.setRelationshipType(ParamUtils.getString(model, "relationshipType"));
			tableCorrelation.setCreateBy(actorId);
			List<SysTable> tables = tableService.getAllSysTables();
			Map<String, String> tableMap = new HashMap<String, String>();
			if (tables != null && !tables.isEmpty()) {
				for (SysTable t : tables) {
					tableMap.put(t.getTableId(), t.getTableName());
				}
			}

			tableCorrelation.setMasterTableName(tableMap.get(tableCorrelation.getMasterTableId()));
			tableCorrelation.setSlaveTableName(tableMap.get(tableCorrelation.getSlaveTableId()));
			this.tableCorrelationService.save(tableCorrelation);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return tableCorrelation;
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
						t1.setTableName("SYS_TABLE_CORRELATION");
						ColumnModel idColumn = new ColumnModel();
						idColumn.setColumnName("ID_");
						idColumn.setJavaType("String");
						idColumn.setValue(item);
						t1.setIdColumn(idColumn);
						ColumnModel sortColumn = new ColumnModel();
						sortColumn.setColumnName("SORTNO_");
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
					ex.printStackTrace();
					logger.error(ex);
				}
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@ResponseBody
	@RequestMapping("/saveTableCorrelation")
	public byte[] saveTableCorrelation(HttpServletRequest request) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		TableCorrelation tableCorrelation = new TableCorrelation();
		try {
			Tools.populate(tableCorrelation, params);
			tableCorrelation.setMasterTableId(request.getParameter("masterTableId"));
			tableCorrelation.setSlaveTableId(request.getParameter("slaveTableId"));
			tableCorrelation.setInsertCascade(request.getParameter("insertCascade"));
			tableCorrelation.setDeleteCascade(request.getParameter("deleteCascade"));
			tableCorrelation.setUpdateCascade(request.getParameter("updateCascade"));
			tableCorrelation.setRelationshipType(request.getParameter("relationshipType"));
			tableCorrelation.setCreateBy(actorId);
			List<SysTable> tables = tableService.getAllSysTables();
			Map<String, String> tableMap = new HashMap<String, String>();
			if (tables != null && !tables.isEmpty()) {
				for (SysTable t : tables) {
					tableMap.put(t.getTableId(), t.getTableName());
				}
			}

			tableCorrelation.setMasterTableName(tableMap.get(tableCorrelation.getMasterTableId()));
			tableCorrelation.setSlaveTableName(tableMap.get(tableCorrelation.getSlaveTableId()));
			this.tableCorrelationService.save(tableCorrelation);

			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource
	public void setTableCorrelationService(TableCorrelationService tableCorrelationService) {
		this.tableCorrelationService = tableCorrelationService;
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
		String masterTableId = request.getParameter("tableId");
		List<SysTable> tables = tableService.getAllSysTables();
		Map<String, String> tableMap = new HashMap<String, String>();
		if (tables != null && !tables.isEmpty()) {
			for (SysTable t : tables) {
				tableMap.put(t.getTableId(), t.getTableName());
			}
		}

		//TableCorrelationQuery query = new TableCorrelationQuery();
		//query.masterTableId(masterTableId);
		List<TableCorrelation> list = tableCorrelationService.getTableCorrelationsByMasterTableId(masterTableId);
		if (list != null && !list.isEmpty()) {
			for (TableCorrelation t : list) {
				t.setMasterTableTitle(tableMap.get(t.getMasterTableId()));
				t.setSlaveTableTitle(tableMap.get(t.getSlaveTableId()));
			}
			request.setAttribute("tables", list);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("table.showSort");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/tableCorrelation/showSort", modelMap);
	}

	@RequestMapping("/update")
	public ModelAndView update(HttpServletRequest request, ModelMap modelMap) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);

		TableCorrelation tableCorrelation = tableCorrelationService.getTableCorrelation(request.getParameter("id"));

		Tools.populate(tableCorrelation, params);

		tableCorrelation.setMasterTableId(request.getParameter("masterTableId"));
		tableCorrelation.setSlaveTableId(request.getParameter("slaveTableId"));
		tableCorrelation.setInsertCascade(request.getParameter("insertCascade"));
		tableCorrelation.setDeleteCascade(request.getParameter("deleteCascade"));
		tableCorrelation.setUpdateCascade(request.getParameter("updateCascade"));
		tableCorrelation.setRelationshipType(request.getParameter("relationshipType"));
		tableCorrelation.setUpdateBy(actorId);
		List<SysTable> tables = tableService.getAllSysTables();
		Map<String, String> tableMap = new HashMap<String, String>();
		if (tables != null && !tables.isEmpty()) {
			for (SysTable t : tables) {
				tableMap.put(t.getTableId(), t.getTableName());
			}
		}

		tableCorrelation.setMasterTableName(tableMap.get(tableCorrelation.getMasterTableId()));
		tableCorrelation.setSlaveTableName(tableMap.get(tableCorrelation.getSlaveTableId()));

		tableCorrelationService.save(tableCorrelation);

		return this.list(request, modelMap);
	}

	@RequestMapping("/view")
	public ModelAndView view(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		TableCorrelation tableCorrelation = tableCorrelationService.getTableCorrelation(request.getParameter("id"));
		request.setAttribute("tableCorrelation", tableCorrelation);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view);
		}

		String x_view = ViewProperties.getString("tableCorrelation.view");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view);
		}

		return new ModelAndView("/sys/tableCorrelation/view");
	}

}
