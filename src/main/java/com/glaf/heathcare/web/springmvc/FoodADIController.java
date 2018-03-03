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

package com.glaf.heathcare.web.springmvc;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.base.modules.sys.model.Dictory;
import com.glaf.base.modules.sys.model.SysTree;
import com.glaf.base.modules.sys.service.DictoryService;
import com.glaf.base.modules.sys.service.SysTreeService;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.PageResult;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;
import com.glaf.heathcare.domain.FoodADI;
import com.glaf.heathcare.helper.PermissionHelper;
import com.glaf.heathcare.query.FoodADIQuery;
import com.glaf.heathcare.service.FoodADIService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/foodADI")
@RequestMapping("/heathcare/foodADI")
public class FoodADIController {
	protected static final Log logger = LogFactory.getLog(FoodADIController.class);

	protected FoodADIService foodADIService;

	protected DictoryService dictoryService;

	protected SysTreeService sysTreeService;

	public FoodADIController() {

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
					FoodADI foodADI = foodADIService.getFoodADI(Long.valueOf(x));
					if (foodADI != null && (StringUtils.equals(foodADI.getCreateBy(), loginContext.getActorId())
							|| loginContext.isSystemAdministrator())) {
						foodADIService.deleteById(foodADI.getId());
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			FoodADI foodADI = foodADIService.getFoodADI(Long.valueOf(id));
			if (foodADI != null && (StringUtils.equals(foodADI.getCreateBy(), loginContext.getActorId())
					|| loginContext.isSystemAdministrator())) {
				foodADIService.deleteById(foodADI.getId());
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		PermissionHelper helper = new PermissionHelper();
		helper.setUserPermission(request);

		FoodADI foodADI = foodADIService.getFoodADI(RequestUtils.getLong(request, "id"));
		if (foodADI != null) {
			request.setAttribute("foodADI", foodADI);
		}

		SysTree root = sysTreeService.getSysTreeByCode("FoodCategory");
		if (root != null) {
			List<SysTree> foodCategories = sysTreeService.getSysTreeList(root.getId());
			request.setAttribute("foodCategories", foodCategories);
		}

		List<Dictory> dictoryList = dictoryService.getDictoryList(4501L);// 4501是餐点分类编号
		request.setAttribute("dictoryList", dictoryList);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("foodADI.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/foodADI/edit", modelMap);
	}

	@ResponseBody
	@RequestMapping("json")
	public byte[] json(HttpServletRequest request) throws IOException {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		FoodADIQuery query = new FoodADIQuery();
		Tools.populate(query, params);

		int start = 0;
		int limit = 10;
		String orderName = null;
		String order = null;

		int pageNo = ParamUtils.getInt(params, "page");
		limit = ParamUtils.getInt(params, "rows");
		start = (pageNo - 1) * limit;
		orderName = ParamUtils.getString(params, "sortName");
		order = ParamUtils.getString(params, "sortOrder");

		start = (pageNo - 1) * limit;

		if (start < 0) {
			start = 0;
		}

		if (limit <= 0) {
			limit = PageResult.DEFAULT_PAGE_SIZE;
		}

		JSONObject result = new JSONObject();
		int total = foodADIService.getFoodADICountByQueryCriteria(query);
		if (total > 0) {
			result.put("total", total);
			result.put("totalCount", total);
			result.put("totalRecords", total);
			result.put("start", start);
			result.put("startIndex", start);
			result.put("limit", limit);
			result.put("pageSize", limit);

			if (StringUtils.isNotEmpty(orderName)) {
				query.setSortColumn(orderName);
				if (StringUtils.equals(order, "desc")) {
					query.setSortOrder(" desc ");
				}
			}

			List<FoodADI> list = foodADIService.getFoodADIsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {

				Map<Long, String> nameMap = new HashMap<Long, String>();
				SysTree root = sysTreeService.getSysTreeByCode("FoodCategory");
				if (root != null) {
					List<SysTree> foodCategories = sysTreeService.getSysTreeList(root.getId());
					if (foodCategories != null && !foodCategories.isEmpty()) {
						for (SysTree tree : foodCategories) {
							nameMap.put(tree.getId(), tree.getName());
						}
					}
				}

				Map<Long, String> nameMap2 = new HashMap<Long, String>();
				List<Dictory> dictoryList = dictoryService.getDictoryList(4501L);// 4501是餐点分类编号
				if (dictoryList != null && !dictoryList.isEmpty()) {
					for (Dictory d : dictoryList) {
						nameMap2.put(d.getId(), d.getName());
					}
				}

				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (FoodADI foodADI : list) {
					JSONObject rowJSON = foodADI.toJsonObject();
					rowJSON.put("typeName", nameMap2.get(foodADI.getTypeId()));
					rowJSON.put("categoryName", nameMap.get(foodADI.getNodeId()));
					rowJSON.put("foodADIId", foodADI.getId());
					rowJSON.put("id", foodADI.getId());
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
		PermissionHelper helper = new PermissionHelper();
		helper.setUserPermission(request);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/foodADI/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("foodADI.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/heathcare/foodADI/query", modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveFoodADI")
	public byte[] saveFoodADI(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isSystemAdministrator()) {
			String actorId = loginContext.getActorId();
			Map<String, Object> params = RequestUtils.getParameterMap(request);
			FoodADI foodADI = new FoodADI();
			try {
				Tools.populate(foodADI, params);
				foodADI.setName(request.getParameter("name"));
				foodADI.setNodeId(RequestUtils.getLong(request, "nodeId"));
				foodADI.setDescription(request.getParameter("description"));
				foodADI.setLowest(RequestUtils.getDouble(request, "lowest"));
				foodADI.setAverage(RequestUtils.getDouble(request, "average"));
				foodADI.setHighest(RequestUtils.getDouble(request, "highest"));
				foodADI.setAgeGroup(request.getParameter("ageGroup"));
				foodADI.setType(request.getParameter("type"));
				foodADI.setTypeId(RequestUtils.getLong(request, "typeId"));
				foodADI.setSortNo(RequestUtils.getInt(request, "sortNo"));
				foodADI.setEnableFlag(request.getParameter("enableFlag"));
				foodADI.setCreateBy(actorId);
				foodADI.setUpdateBy(actorId);
				foodADI.setType(dictoryService.getCodeById(foodADI.getTypeId()));
				this.foodADIService.save(foodADI);

				return ResponseUtils.responseJsonResult(true);
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource
	public void setDictoryService(DictoryService dictoryService) {
		this.dictoryService = dictoryService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.foodADIService")
	public void setFoodADIService(FoodADIService foodADIService) {
		this.foodADIService = foodADIService;
	}

	@javax.annotation.Resource
	public void setSysTreeService(SysTreeService sysTreeService) {
		this.sysTreeService = sysTreeService;
	}

	@RequestMapping("/view")
	public ModelAndView view(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		FoodADI foodADI = foodADIService.getFoodADI(RequestUtils.getLong(request, "id"));
		request.setAttribute("foodADI", foodADI);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view);
		}

		String x_view = ViewProperties.getString("foodADI.view");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view);
		}

		return new ModelAndView("/heathcare/foodADI/view");
	}

}
