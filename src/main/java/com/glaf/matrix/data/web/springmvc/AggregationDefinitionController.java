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
import com.glaf.base.modules.sys.model.Dictory;
import com.glaf.base.modules.sys.service.DictoryService;
import com.glaf.core.base.ColumnModel;
import com.glaf.core.base.TableModel;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.factory.DataServiceFactory;
import com.glaf.core.identity.*;
import com.glaf.core.security.*;
import com.glaf.core.util.*;

import com.glaf.matrix.data.domain.*;
import com.glaf.matrix.data.query.*;
import com.glaf.matrix.data.service.*;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/sys/aggregationDefinition")
@RequestMapping("/sys/aggregationDefinition")
public class AggregationDefinitionController {
	protected static final Log logger = LogFactory.getLog(AggregationDefinitionController.class);

	protected DictoryService dictoryService;

	protected AggregationDefinitionService aggregationDefinitionService;

	public AggregationDefinitionController() {

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
					AggregationDefinition aggregationDefinition = aggregationDefinitionService
							.getAggregationDefinition(Long.valueOf(x));
					if (aggregationDefinition != null
							&& (StringUtils.equals(aggregationDefinition.getCreateBy(), loginContext.getActorId())
									|| loginContext.isSystemAdministrator())) {
						aggregationDefinitionService.deleteById(aggregationDefinition.getId());
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			AggregationDefinition aggregationDefinition = aggregationDefinitionService
					.getAggregationDefinition(Long.valueOf(id));
			if (aggregationDefinition != null
					&& (StringUtils.equals(aggregationDefinition.getCreateBy(), loginContext.getActorId())
							|| loginContext.isSystemAdministrator())) {
				aggregationDefinitionService.deleteById(aggregationDefinition.getId());
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		request.removeAttribute("canSubmit");

		AggregationDefinition aggregationDefinition = aggregationDefinitionService
				.getAggregationDefinition(RequestUtils.getLong(request, "id"));
		if (aggregationDefinition != null) {
			request.setAttribute("aggregationDefinition", aggregationDefinition);
		}

		List<Dictory> dictoryList = dictoryService.getDictoryList(4801L);// 4801是汇总定义分类编号
		request.setAttribute("dictoryList", dictoryList);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("aggregationDefinition.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/aggregationDefinition/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		AggregationDefinitionQuery query = new AggregationDefinitionQuery();
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
		int total = aggregationDefinitionService.getAggregationDefinitionCountByQueryCriteria(query);
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

			List<AggregationDefinition> list = aggregationDefinitionService
					.getAggregationDefinitionsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (AggregationDefinition aggregationDefinition : list) {
					JSONObject rowJSON = aggregationDefinition.toJsonObject();
					rowJSON.put("id", aggregationDefinition.getId());
					rowJSON.put("rowId", aggregationDefinition.getId());
					rowJSON.put("aggregationDefinitionId", aggregationDefinition.getId());
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

		List<Dictory> dictoryList = dictoryService.getDictoryList(4801L);// 4801是汇总定义分类编号
		request.setAttribute("dictoryList", dictoryList);
		
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/sys/aggregationDefinition/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("aggregationDefinition.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/sys/aggregationDefinition/query", modelMap);
	}

	@RequestMapping("/save")
	public ModelAndView save(HttpServletRequest request, ModelMap modelMap) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);

		AggregationDefinition aggregationDefinition = new AggregationDefinition();
		Tools.populate(aggregationDefinition, params);

		aggregationDefinition.setName(request.getParameter("name"));
		aggregationDefinition.setTitle(request.getParameter("title"));
		aggregationDefinition.setServiceKey(request.getParameter("serviceKey"));
		aggregationDefinition.setSql(request.getParameter("sql"));
		aggregationDefinition.setPrecision(RequestUtils.getInt(request, "precision"));
		aggregationDefinition.setTargetTableName(request.getParameter("targetTableName"));
		aggregationDefinition.setPartitionFlag(request.getParameter("partitionFlag"));
		aggregationDefinition.setResultFlag(request.getParameter("resultFlag"));
		aggregationDefinition.setType(request.getParameter("type"));
		aggregationDefinition.setSortNo(RequestUtils.getInt(request, "sortNo"));
		aggregationDefinition.setLocked(RequestUtils.getInt(request, "locked"));
		aggregationDefinition.setCreateBy(actorId);

		aggregationDefinitionService.save(aggregationDefinition);

		return this.list(request, modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveAggregationDefinition")
	public byte[] saveAggregationDefinition(HttpServletRequest request) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		AggregationDefinition aggregationDefinition = new AggregationDefinition();
		try {
			Tools.populate(aggregationDefinition, params);
			aggregationDefinition.setTitle(request.getParameter("title"));
			aggregationDefinition.setServiceKey(request.getParameter("serviceKey"));
			aggregationDefinition.setName(request.getParameter("name"));
			aggregationDefinition.setSql(request.getParameter("sql"));
			aggregationDefinition.setPrecision(RequestUtils.getInt(request, "precision"));
			aggregationDefinition.setTargetTableName(request.getParameter("targetTableName"));
			aggregationDefinition.setPartitionFlag(request.getParameter("partitionFlag"));
			aggregationDefinition.setResultFlag(request.getParameter("resultFlag"));
			aggregationDefinition.setType(request.getParameter("type"));
			aggregationDefinition.setSortNo(RequestUtils.getInt(request, "sortNo"));
			aggregationDefinition.setLocked(RequestUtils.getInt(request, "locked"));
			aggregationDefinition.setCreateBy(actorId);
			this.aggregationDefinitionService.save(aggregationDefinition);

			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
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
						t1.setTableName("SYS_AGGREGATION_DEF");
						ColumnModel idColumn = new ColumnModel();
						idColumn.setColumnName("ID_");
						idColumn.setJavaType("Long");
						idColumn.setValue(Long.parseLong(item));
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
					logger.error(ex);
				}
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@javax.annotation.Resource
	public void setAggregationDefinitionService(AggregationDefinitionService aggregationDefinitionService) {
		this.aggregationDefinitionService = aggregationDefinitionService;
	}

	@javax.annotation.Resource
	public void setDictoryService(DictoryService dictoryService) {
		this.dictoryService = dictoryService;
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

		String serviceKey = request.getParameter("serviceKey");
		AggregationDefinitionQuery query = new AggregationDefinitionQuery();
		query.serviceKey(serviceKey);
		List<AggregationDefinition> list = aggregationDefinitionService.list(query);
		request.setAttribute("list", list);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("aggregationDefinition.showSort");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/aggregationDefinition/showSort", modelMap);
	}

}
