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

package com.glaf.matrix.export.web.springmvc;

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
import org.springframework.web.bind.annotation.RequestMapping;
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
import com.glaf.matrix.export.domain.DataExportItem;
import com.glaf.matrix.export.query.DataExportItemQuery;
import com.glaf.matrix.export.service.DataExportItemService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/matrix/dataExportItem")
@RequestMapping("/matrix/dataExportItem")
public class DataExportItemController {
	protected static final Log logger = LogFactory.getLog(DataExportItemController.class);

	protected DataExportItemService dataExportItemService;

	public DataExportItemController() {

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
					DataExportItem dataExportItem = dataExportItemService.getDataExportItem(x);
					if (dataExportItem != null
							&& (StringUtils.equals(dataExportItem.getCreateBy(), loginContext.getActorId())
									|| loginContext.isSystemAdministrator())) {
						dataExportItemService.deleteById(dataExportItem.getId());
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			DataExportItem dataExportItem = dataExportItemService.getDataExportItem(id);
			if (dataExportItem != null && (StringUtils.equals(dataExportItem.getCreateBy(), loginContext.getActorId())
					|| loginContext.isSystemAdministrator())) {
				dataExportItemService.deleteById(dataExportItem.getId());
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		DataExportItem dataExportItem = dataExportItemService.getDataExportItem(RequestUtils.getString(request, "id"));
		if (dataExportItem != null) {
			request.setAttribute("dataExportItem", dataExportItem);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("dataExportItem.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/matrix/dataExportItem/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		DataExportItemQuery query = new DataExportItemQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		String expId = RequestUtils.getString(request, "expId");
		query.expId(expId);

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
		int total = dataExportItemService.getDataExportItemCountByQueryCriteria(query);
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

			List<DataExportItem> list = dataExportItemService.getDataExportItemsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (DataExportItem dataExportItem : list) {
					JSONObject rowJSON = dataExportItem.toJsonObject();
					rowJSON.put("id", dataExportItem.getId());
					rowJSON.put("rowId", dataExportItem.getId());
					rowJSON.put("dataExportItemId", dataExportItem.getId());
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

		return new ModelAndView("/matrix/dataExportItem/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("dataExportItem.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/matrix/dataExportItem/query", modelMap);
	}

	@ResponseBody
	@RequestMapping("/save")
	public byte[] save(HttpServletRequest request) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		DataExportItem dataExportItem = new DataExportItem();
		try {
			Tools.populate(dataExportItem, params);
			dataExportItem.setExpId(RequestUtils.getString(request, "expId"));
			dataExportItem.setDeploymentId(request.getParameter("deploymentId"));
			dataExportItem.setTitle(request.getParameter("title"));
			dataExportItem.setSql(request.getParameter("sql"));
			dataExportItem.setRecursionSql(request.getParameter("recursionSql"));
			dataExportItem.setRecursionColumns(request.getParameter("recursionColumns"));
			dataExportItem.setPrimaryKey(request.getParameter("primaryKey"));
			dataExportItem.setExpression(request.getParameter("expression"));
			dataExportItem.setResultFlag(request.getParameter("resultFlag"));
			dataExportItem.setSortNo(RequestUtils.getInt(request, "sortNo"));
			dataExportItem.setLocked(RequestUtils.getInt(request, "locked"));
			dataExportItem.setCreateBy(actorId);
			this.dataExportItemService.save(dataExportItem);

			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource(name = "com.glaf.matrix.export.service.dataExportItemService")
	public void setDataExportItemService(DataExportItemService dataExportItemService) {
		this.dataExportItemService = dataExportItemService;
	}

}
