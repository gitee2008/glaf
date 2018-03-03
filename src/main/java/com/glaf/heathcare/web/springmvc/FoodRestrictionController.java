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
import com.glaf.base.modules.sys.model.SysTree;
import com.glaf.base.modules.sys.service.SysTreeService;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;
import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.domain.FoodRestriction;
import com.glaf.heathcare.query.FoodCompositionQuery;
import com.glaf.heathcare.query.FoodRestrictionQuery;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.heathcare.service.FoodRestrictionService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/foodRestriction")
@RequestMapping("/heathcare/foodRestriction")
public class FoodRestrictionController {
	protected static final Log logger = LogFactory.getLog(FoodRestrictionController.class);

	protected FoodCompositionService foodCompositionService;

	protected FoodRestrictionService foodRestrictionService;

	protected SysTreeService sysTreeService;

	public FoodRestrictionController() {

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
					FoodRestriction foodRestriction = foodRestrictionService.getFoodRestriction(Long.valueOf(x));
					if (foodRestriction != null
							&& (StringUtils.equals(foodRestriction.getCreateBy(), loginContext.getActorId())
									|| loginContext.isSystemAdministrator())) {
						foodRestrictionService.deleteById(foodRestriction.getId());
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			FoodRestriction foodRestriction = foodRestrictionService.getFoodRestriction(Long.valueOf(id));
			if (foodRestriction != null && (StringUtils.equals(foodRestriction.getCreateBy(), loginContext.getActorId())
					|| loginContext.isSystemAdministrator())) {
				foodRestrictionService.deleteById(foodRestriction.getId());
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		FoodRestriction foodRestriction = foodRestrictionService
				.getFoodRestriction(RequestUtils.getLong(request, "id"));
		if (foodRestriction != null) {
			request.setAttribute("foodRestriction", foodRestriction);

			if (foodRestriction.getRestrictionFoodId() > 0) {
				FoodComposition foodComposition = foodCompositionService
						.getFoodComposition(foodRestriction.getRestrictionFoodId());
				if (foodComposition != null) {
					List<FoodComposition> foods = foodCompositionService
							.getFoodCompositions(foodComposition.getNodeId());
					request.setAttribute("nodeId", foodComposition.getNodeId());
					request.setAttribute("foods", foods);
				}
			}

			if (foodRestriction.getFoodId() > 0) {
				FoodComposition foodComposition = foodCompositionService
						.getFoodComposition(foodRestriction.getFoodId());
				if (foodComposition != null) {
					request.setAttribute("foodComposition", foodComposition);
				}
			}
		} else {
			FoodComposition foodComposition = foodCompositionService
					.getFoodComposition(RequestUtils.getLong(request, "foodId"));
			request.setAttribute("foodComposition", foodComposition);
		}

		SysTree root = sysTreeService.getSysTreeByCode("FoodCategory");
		if (root != null) {
			List<SysTree> foodCategories = sysTreeService.getSysTreeList(root.getId());
			request.setAttribute("foodCategories", foodCategories);
		}

		FoodCompositionQuery query2 = new FoodCompositionQuery();
		List<FoodComposition> list2 = foodCompositionService.list(query2);
		request.setAttribute("list", list2);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("foodRestriction.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/foodRestriction/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		FoodRestrictionQuery query = new FoodRestrictionQuery();
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
		int total = foodRestrictionService.getFoodRestrictionCountByQueryCriteria(query);
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

			List<FoodRestriction> list = foodRestrictionService.getFoodRestrictionsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {

				FoodCompositionQuery query2 = new FoodCompositionQuery();
				List<FoodComposition> list2 = foodCompositionService.list(query2);
				Map<Long, FoodComposition> foodMap = new HashMap<Long, FoodComposition>();
				if (list2 != null && !list2.isEmpty()) {
					for (FoodComposition fd : list2) {
						foodMap.put(fd.getId(), fd);
					}
				}

				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (FoodRestriction foodRestriction : list) {
					JSONObject rowJSON = foodRestriction.toJsonObject();
					rowJSON.put("id", foodRestriction.getId());
					if (foodMap.get(foodRestriction.getFoodId()) != null) {
						rowJSON.put("foodName", foodMap.get(foodRestriction.getFoodId()).getName());
					}
					if (foodMap.get(foodRestriction.getRestrictionFoodId()) != null) {
						rowJSON.put("restrictionFoodName",
								foodMap.get(foodRestriction.getRestrictionFoodId()).getName());
					}
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

		return new ModelAndView("/heathcare/foodRestriction/list", modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveFoodRestriction")
	public byte[] saveFoodRestriction(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		FoodRestriction foodRestriction = new FoodRestriction();
		try {
			Tools.populate(foodRestriction, params);
			foodRestriction.setFoodId(RequestUtils.getLong(request, "foodId"));
			foodRestriction.setRestrictionFoodId(RequestUtils.getLong(request, "restrictionFoodId"));
			foodRestriction.setDescription(request.getParameter("description"));
			foodRestriction.setCreateBy(actorId);
			foodRestriction.setUpdateBy(actorId);
			this.foodRestrictionService.save(foodRestriction);

			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.foodCompositionService")
	public void setFoodCompositionService(FoodCompositionService foodCompositionService) {
		this.foodCompositionService = foodCompositionService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.foodRestrictionService")
	public void setFoodRestrictionService(FoodRestrictionService foodRestrictionService) {
		this.foodRestrictionService = foodRestrictionService;
	}

	@javax.annotation.Resource
	public void setSysTreeService(SysTreeService sysTreeService) {
		this.sysTreeService = sysTreeService;
	}

}
