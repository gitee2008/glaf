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

package com.glaf.matrix.transform.web.springmvc;

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

import com.glaf.core.config.ViewProperties;
import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.domain.Database;
import com.glaf.core.identity.*;
import com.glaf.core.security.*;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.util.*;

import com.glaf.matrix.transform.domain.*;
import com.glaf.matrix.transform.query.*;
import com.glaf.matrix.transform.service.*;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/sys/columnTransform")
@RequestMapping("/sys/columnTransform")
public class ColumnTransformController {
	protected static final Log logger = LogFactory.getLog(ColumnTransformController.class);

	protected ColumnTransformService columnTransformService;

	protected TableTransformService tableTransformService;

	protected IDatabaseService databaseService;

	public ColumnTransformController() {

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
					ColumnTransform columnTransform = columnTransformService.getColumnTransform(Long.valueOf(x));
					if (columnTransform != null
							&& (StringUtils.equals(columnTransform.getCreateBy(), loginContext.getActorId())
									|| loginContext.isSystemAdministrator())) {
						columnTransformService.deleteById(id);
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			ColumnTransform columnTransform = columnTransformService.getColumnTransform(Long.valueOf(id));
			if (columnTransform != null && (StringUtils.equals(columnTransform.getCreateBy(), loginContext.getActorId())
					|| loginContext.isSystemAdministrator())) {
				columnTransformService.deleteById(id);
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		long id = RequestUtils.getLong(request, "id");
		TableTransform tableTransform = null;
		ColumnTransform columnTransform = null;
		String transformId = request.getParameter("transformId");
		columnTransform = columnTransformService.getColumnTransform(id);
		if (columnTransform != null) {
			transformId = columnTransform.getTransformId();
			request.setAttribute("columnTransform", columnTransform);
			request.setAttribute("tableName", columnTransform.getTableName());
		}

		if (StringUtils.isNotEmpty(transformId)) {
			tableTransform = tableTransformService.getTableTransform(transformId);
		}

		if (tableTransform != null) {
			String tableName = tableTransform.getTableName();

			List<ColumnTransform> cols = columnTransformService.getColumnTransforms(transformId);

			List<Long> databaseIds = StringTools.splitToLong(tableTransform.getDatabaseIds());
			if (databaseIds != null && !databaseIds.isEmpty()) {
				Database database = databaseService.getDatabaseById(databaseIds.get(0));
				List<ColumnDefinition> columns = DBUtils.getColumnDefinitions(database.getName(), tableName);
				if (cols != null && !cols.isEmpty()) {
					List<String> existNames = new ArrayList<String>();
					for (ColumnDefinition column : columns) {
						existNames.add(column.getColumnName().toUpperCase());
					}
					for (ColumnTransform ct : cols) {
						if (!existNames.contains(ct.getTargetColumnName().toUpperCase())) {
							existNames.add(ct.getTargetColumnName().toUpperCase());
							ColumnDefinition col = new ColumnDefinition();
							col.setColumnName(ct.getTargetColumnName());
							col.setJavaType(ct.getTargetType());
							col.setTitle(ct.getTitle());
							columns.add(col);
						}
					}
				}
				request.setAttribute("columns", columns);
			}
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("columnTransform.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/matrix/columnTransform/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		ColumnTransformQuery query = new ColumnTransformQuery();
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
		int total = columnTransformService.getColumnTransformCountByQueryCriteria(query);
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

			List<ColumnTransform> list = columnTransformService.getColumnTransformsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (ColumnTransform columnTransform : list) {
					JSONObject rowJSON = columnTransform.toJsonObject();
					rowJSON.put("id", columnTransform.getId());
					rowJSON.put("rowId", columnTransform.getId());
					rowJSON.put("columnTransformId", columnTransform.getId());
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

		return new ModelAndView("/matrix/columnTransform/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("columnTransform.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/matrix/columnTransform/query", modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveColumnTransform")
	public byte[] saveColumnTransform(HttpServletRequest request) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		try {
			TableTransform tableTransform = tableTransformService
					.getTableTransform(request.getParameter("transformId"));
			if (tableTransform != null && StringUtils.isNotEmpty(tableTransform.getTableName())) {
				ColumnTransform columnTransform = new ColumnTransform();
				Tools.populate(columnTransform, params);

				columnTransform.setId(RequestUtils.getLong(request, "id"));
				columnTransform.setName(request.getParameter("name"));
				columnTransform.setTitle(request.getParameter("title"));
				columnTransform.setTransformId(tableTransform.getTransformId());
				columnTransform.setTableName(tableTransform.getTableName());
				columnTransform.setColumnName(request.getParameter("columnName"));
				columnTransform.setTargetType(request.getParameter("targetType"));
				columnTransform.setTargetColumnName(request.getParameter("targetColumnName"));
				columnTransform.setTargetColumnPrecision(RequestUtils.getInt(request, "targetColumnPrecision"));
				columnTransform.setSqlCriteria(request.getParameter("sqlCriteria"));
				columnTransform.setCondition(request.getParameter("condition"));
				columnTransform.setExpression(request.getParameter("expression"));
				columnTransform
						.setTransformIfTargetColumnNotEmpty(request.getParameter("transformIfTargetColumnNotEmpty"));
				columnTransform.setSort(RequestUtils.getInt(request, "sort"));
				columnTransform.setLocked(RequestUtils.getInt(request, "locked"));
				columnTransform.setType(request.getParameter("type"));
				columnTransform.setCreateBy(actorId);
				columnTransform.setUpdateBy(actorId);
				this.columnTransformService.save(columnTransform);

				return ResponseUtils.responseJsonResult(true);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource
	public void setColumnTransformService(ColumnTransformService columnTransformService) {
		this.columnTransformService = columnTransformService;
	}

	@javax.annotation.Resource
	public void setDatabaseService(IDatabaseService databaseService) {
		this.databaseService = databaseService;
	}

	@javax.annotation.Resource
	public void setTableTransformService(TableTransformService tableTransformService) {
		this.tableTransformService = tableTransformService;
	}

	@RequestMapping("/update")
	public ModelAndView update(HttpServletRequest request, ModelMap modelMap) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);

		ColumnTransform columnTransform = columnTransformService
				.getColumnTransform(RequestUtils.getLong(request, "id"));

		Tools.populate(columnTransform, params);

		columnTransform.setName(request.getParameter("name"));
		columnTransform.setTitle(request.getParameter("title"));
		columnTransform.setTableName(request.getParameter("tableName"));
		columnTransform.setColumnName(request.getParameter("columnName"));
		columnTransform.setTargetType(request.getParameter("targetType"));
		columnTransform.setTargetColumnName(request.getParameter("targetColumnName"));
		columnTransform.setTargetColumnPrecision(RequestUtils.getInt(request, "targetColumnPrecision"));
		columnTransform.setSqlCriteria(request.getParameter("sqlCriteria"));
		columnTransform.setCondition(request.getParameter("condition"));
		columnTransform.setExpression(request.getParameter("expression"));
		columnTransform.setTransformIfTargetColumnNotEmpty(request.getParameter("transformIfTargetColumnNotEmpty"));
		columnTransform.setSort(RequestUtils.getInt(request, "sort"));
		columnTransform.setLocked(RequestUtils.getInt(request, "locked"));
		columnTransform.setType(request.getParameter("type"));
		columnTransform.setCreateBy(actorId);
		columnTransform.setUpdateBy(actorId);
		columnTransformService.save(columnTransform);

		return this.list(request, modelMap);
	}

	@RequestMapping("/view")
	public ModelAndView view(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		ColumnTransform columnTransform = columnTransformService
				.getColumnTransform(RequestUtils.getLong(request, "id"));
		request.setAttribute("columnTransform", columnTransform);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view);
		}

		String x_view = ViewProperties.getString("columnTransform.view");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view);
		}

		return new ModelAndView("/matrix/columnTransform/view");
	}

}
