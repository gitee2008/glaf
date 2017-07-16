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
import com.glaf.core.config.ViewProperties;
import com.glaf.core.identity.User;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;
import com.glaf.matrix.data.domain.SysTable;
import com.glaf.matrix.data.domain.TableColumn;
import com.glaf.matrix.data.domain.TableDataItem;
import com.glaf.matrix.data.query.SysTableQuery;
import com.glaf.matrix.data.query.TableDataItemQuery;
import com.glaf.matrix.data.service.ITableService;
import com.glaf.matrix.data.service.TableDataItemService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/sys/tableDataItem")
@RequestMapping("/sys/tableDataItem")
public class TableDataItemController {
	protected static final Log logger = LogFactory.getLog(TableDataItemController.class);

	protected ITableService tableService;

	protected TableDataItemService tableDataItemService;

	public TableDataItemController() {

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
					TableDataItem tableDataItem = tableDataItemService.getTableDataItem(String.valueOf(x));
					if (tableDataItem != null
							&& (StringUtils.equals(tableDataItem.getCreateBy(), loginContext.getActorId())
									|| loginContext.isSystemAdministrator())) {
						tableDataItemService.deleteById(tableDataItem.getId());
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			TableDataItem tableDataItem = tableDataItemService.getTableDataItem(String.valueOf(id));
			if (tableDataItem != null && (StringUtils.equals(tableDataItem.getCreateBy(), loginContext.getActorId())
					|| loginContext.isSystemAdministrator())) {
				tableDataItemService.deleteById(tableDataItem.getId());
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		request.removeAttribute("canSubmit");

		TableDataItem tableDataItem = tableDataItemService.getTableDataItem(request.getParameter("id"));
		if (tableDataItem != null) {
			request.setAttribute("tableDataItem", tableDataItem);

			List<TableColumn> list = tableService.getTableColumnsByTableName(tableDataItem.getTableName());
			if (list != null && !list.isEmpty()) {
				TableColumn idColumn = new TableColumn();
				idColumn.setColumnName("ID_");
				idColumn.setTitle("编号");
				list.add(idColumn);
				request.setAttribute("columns", list);
			}
		}

		SysTableQuery query = new SysTableQuery();
		query.type("useradd");
		List<SysTable> tables = tableService.list(query);
		request.setAttribute("tables", tables);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("tableDataItem.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/tableDataItem/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		TableDataItemQuery query = new TableDataItemQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);
		query.type("useradd");

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
		int total = tableDataItemService.getTableDataItemCountByQueryCriteria(query);
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

			List<TableDataItem> list = tableDataItemService.getTableDataItemsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (TableDataItem tableDataItem : list) {
					JSONObject rowJSON = tableDataItem.toJsonObject();
					rowJSON.put("id", tableDataItem.getId());
					rowJSON.put("rowId", tableDataItem.getId());
					rowJSON.put("tableDataItemId", tableDataItem.getId());
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

		return new ModelAndView("/sys/tableDataItem/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("tableDataItem.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/sys/tableDataItem/query", modelMap);
	}

	@RequestMapping("/save")
	public ModelAndView save(HttpServletRequest request, ModelMap modelMap) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);

		TableDataItem tableDataItem = new TableDataItem();
		Tools.populate(tableDataItem, params);

		tableDataItem.setTitle(request.getParameter("title"));
		tableDataItem.setDescription(request.getParameter("description"));
		tableDataItem.setTableName(request.getParameter("tableName"));
		tableDataItem.setNameColumn(request.getParameter("nameColumn"));
		tableDataItem.setValueColumn(request.getParameter("valueColumn"));
		tableDataItem.setSortColumn(request.getParameter("sortColumn"));
		tableDataItem.setFilterFlag(request.getParameter("filterFlag"));
		tableDataItem.setLocked(RequestUtils.getInt(request, "locked"));
		tableDataItem.setType("useradd");
		tableDataItem.setCreateBy(actorId);

		tableDataItemService.save(tableDataItem);

		return this.list(request, modelMap);
	}

	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	public @ResponseBody TableDataItem saveOrUpdate(HttpServletRequest request,
			@RequestBody Map<String, Object> model) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		TableDataItem tableDataItem = new TableDataItem();
		try {
			Tools.populate(tableDataItem, model);
			tableDataItem.setTitle(ParamUtils.getString(model, "title"));
			tableDataItem.setDescription(ParamUtils.getString(model, "description"));
			tableDataItem.setTableName(ParamUtils.getString(model, "tableName"));
			tableDataItem.setNameColumn(ParamUtils.getString(model, "nameColumn"));
			tableDataItem.setValueColumn(ParamUtils.getString(model, "valueColumn"));
			tableDataItem.setSortColumn(request.getParameter("sortColumn"));
			tableDataItem.setFilterFlag(ParamUtils.getString(model, "filterFlag"));
			tableDataItem.setLocked(RequestUtils.getInt(request, "locked"));
			tableDataItem.setType("useradd");
			tableDataItem.setCreateBy(actorId);
			this.tableDataItemService.save(tableDataItem);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return tableDataItem;
	}

	@ResponseBody
	@RequestMapping("/saveTableDataItem")
	public byte[] saveTableDataItem(HttpServletRequest request) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		TableDataItem tableDataItem = new TableDataItem();
		try {
			Tools.populate(tableDataItem, params);
			tableDataItem.setTitle(request.getParameter("title"));
			tableDataItem.setDescription(request.getParameter("description"));
			tableDataItem.setTableName(request.getParameter("tableName"));
			tableDataItem.setNameColumn(request.getParameter("nameColumn"));
			tableDataItem.setValueColumn(request.getParameter("valueColumn"));
			tableDataItem.setSortColumn(request.getParameter("sortColumn"));
			tableDataItem.setFilterFlag(request.getParameter("filterFlag"));
			tableDataItem.setLocked(RequestUtils.getInt(request, "locked"));
			tableDataItem.setType("useradd");
			tableDataItem.setCreateBy(actorId);
			this.tableDataItemService.save(tableDataItem);

			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource
	public void setTableDataItemService(TableDataItemService tableDataItemService) {
		this.tableDataItemService = tableDataItemService;
	}

	@javax.annotation.Resource
	public void setTableService(ITableService tableService) {
		this.tableService = tableService;
	}

	@RequestMapping("/update")
	public ModelAndView update(HttpServletRequest request, ModelMap modelMap) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);

		TableDataItem tableDataItem = tableDataItemService.getTableDataItem(request.getParameter("id"));

		Tools.populate(tableDataItem, params);

		tableDataItem.setTitle(request.getParameter("title"));
		tableDataItem.setDescription(request.getParameter("description"));
		tableDataItem.setTableName(request.getParameter("tableName"));
		tableDataItem.setNameColumn(request.getParameter("nameColumn"));
		tableDataItem.setValueColumn(request.getParameter("valueColumn"));
		tableDataItem.setSortColumn(request.getParameter("sortColumn"));
		tableDataItem.setFilterFlag(request.getParameter("filterFlag"));
		tableDataItem.setLocked(RequestUtils.getInt(request, "locked"));
		tableDataItem.setType("useradd");
		tableDataItem.setUpdateBy(actorId);

		tableDataItemService.save(tableDataItem);

		return this.list(request, modelMap);
	}

	@RequestMapping("/view")
	public ModelAndView view(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		TableDataItem tableDataItem = tableDataItemService.getTableDataItem(request.getParameter("id"));
		request.setAttribute("tableDataItem", tableDataItem);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view);
		}

		String x_view = ViewProperties.getString("tableDataItem.view");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view);
		}

		return new ModelAndView("/sys/tableDataItem/view");
	}

}
