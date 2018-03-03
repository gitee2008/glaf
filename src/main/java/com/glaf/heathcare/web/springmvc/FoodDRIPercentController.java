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
import com.glaf.base.modules.sys.service.DictoryService;
import com.glaf.base.modules.sys.service.SysTreeService;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;

import com.glaf.heathcare.domain.FoodDRIPercent;
import com.glaf.heathcare.helper.PermissionHelper;
import com.glaf.heathcare.query.FoodDRIPercentQuery;
import com.glaf.heathcare.service.FoodDRIPercentService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/foodDRIPercent")
@RequestMapping("/heathcare/foodDRIPercent")
public class FoodDRIPercentController {
	protected static final Log logger = LogFactory.getLog(FoodDRIPercentController.class);

	protected FoodDRIPercentService foodDRIPercentService;

	protected DictoryService dictoryService;

	protected SysTreeService sysTreeService;

	public FoodDRIPercentController() {

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
					FoodDRIPercent foodDRIPercent = foodDRIPercentService.getFoodDRIPercent(Long.valueOf(x));
					if (foodDRIPercent != null
							&& (StringUtils.equals(foodDRIPercent.getCreateBy(), loginContext.getActorId())
									|| loginContext.isSystemAdministrator())) {
						foodDRIPercentService.deleteById(foodDRIPercent.getId());
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			FoodDRIPercent foodDRIPercent = foodDRIPercentService.getFoodDRIPercent(Long.valueOf(id));
			if (foodDRIPercent != null && (StringUtils.equals(foodDRIPercent.getCreateBy(), loginContext.getActorId())
					|| loginContext.isSystemAdministrator())) {
				foodDRIPercentService.deleteById(foodDRIPercent.getId());
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

		FoodDRIPercent foodDRIPercent = foodDRIPercentService.getFoodDRIPercent(RequestUtils.getLong(request, "id"));
		if (foodDRIPercent != null) {
			request.setAttribute("foodDRIPercent", foodDRIPercent);
		}

		List<Dictory> dictoryList = dictoryService.getDictoryList(4501L);// 4501是餐点分类编号
		request.setAttribute("dictoryList", dictoryList);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("foodDRIPercent.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/foodDRIPercent/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		FoodDRIPercentQuery query = new FoodDRIPercentQuery();
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
		int total = foodDRIPercentService.getFoodDRIPercentCountByQueryCriteria(query);
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

			List<FoodDRIPercent> list = foodDRIPercentService.getFoodDRIPercentsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				Map<Long, String> nameMap2 = new HashMap<Long, String>();
				List<Dictory> dictoryList = dictoryService.getDictoryList(4501L);// 4501是餐点分类编号
				if (dictoryList != null && !dictoryList.isEmpty()) {
					for (Dictory d : dictoryList) {
						nameMap2.put(d.getId(), d.getName());
					}
				}

				for (FoodDRIPercent foodDRIPercent : list) {
					JSONObject rowJSON = foodDRIPercent.toJsonObject();
					rowJSON.put("id", foodDRIPercent.getId());
					rowJSON.put("typeName", nameMap2.get(foodDRIPercent.getTypeId()));
					rowJSON.put("foodDRIPercentId", foodDRIPercent.getId());
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

		return new ModelAndView("/heathcare/foodDRIPercent/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("foodDRIPercent.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/heathcare/foodDRIPercent/query", modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveFoodDRIPercent")
	public byte[] saveFoodDRIPercent(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isSystemAdministrator()) {
			String actorId = loginContext.getActorId();
			Map<String, Object> params = RequestUtils.getParameterMap(request);
			FoodDRIPercent foodDRIPercent = new FoodDRIPercent();
			try {
				Tools.populate(foodDRIPercent, params);
				foodDRIPercent.setName(request.getParameter("name"));
				foodDRIPercent.setDescription(request.getParameter("description"));
				foodDRIPercent.setAgeGroup(request.getParameter("ageGroup"));
				foodDRIPercent.setType(request.getParameter("type"));
				foodDRIPercent.setTypeId(RequestUtils.getLong(request, "typeId"));
				foodDRIPercent.setHeatEnergy(RequestUtils.getDouble(request, "heatEnergy"));
				foodDRIPercent.setProtein(RequestUtils.getDouble(request, "protein"));
				foodDRIPercent.setFat(RequestUtils.getDouble(request, "fat"));
				foodDRIPercent.setCarbohydrate(RequestUtils.getDouble(request, "carbohydrate"));
				foodDRIPercent.setVitaminA(RequestUtils.getDouble(request, "vitaminA"));
				foodDRIPercent.setVitaminB1(RequestUtils.getDouble(request, "vitaminB1"));
				foodDRIPercent.setVitaminB2(RequestUtils.getDouble(request, "vitaminB2"));
				foodDRIPercent.setVitaminB6(RequestUtils.getDouble(request, "vitaminB6"));
				foodDRIPercent.setVitaminB12(RequestUtils.getDouble(request, "vitaminB12"));
				foodDRIPercent.setVitaminC(RequestUtils.getDouble(request, "vitaminC"));
				foodDRIPercent.setVitaminE(RequestUtils.getDouble(request, "vitaminE"));
				foodDRIPercent.setCarotene(RequestUtils.getDouble(request, "carotene"));
				foodDRIPercent.setRetinol(RequestUtils.getDouble(request, "retinol"));
				foodDRIPercent.setNicotinicCid(RequestUtils.getDouble(request, "nicotinicCid"));
				foodDRIPercent.setCalcium(RequestUtils.getDouble(request, "calcium"));
				foodDRIPercent.setIron(RequestUtils.getDouble(request, "iron"));
				foodDRIPercent.setZinc(RequestUtils.getDouble(request, "zinc"));
				foodDRIPercent.setIodine(RequestUtils.getDouble(request, "iodine"));
				foodDRIPercent.setPhosphorus(RequestUtils.getDouble(request, "phosphorus"));
				foodDRIPercent.setSortNo(RequestUtils.getInt(request, "sortNo"));
				foodDRIPercent.setEnableFlag(request.getParameter("enableFlag"));
				foodDRIPercent.setCreateBy(actorId);
				foodDRIPercent.setUpdateBy(actorId);

				this.foodDRIPercentService.save(foodDRIPercent);

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

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.foodDRIPercentService")
	public void setFoodDRIPercentService(FoodDRIPercentService foodDRIPercentService) {
		this.foodDRIPercentService = foodDRIPercentService;
	}

	@javax.annotation.Resource
	public void setSysTreeService(SysTreeService sysTreeService) {
		this.sysTreeService = sysTreeService;
	}

	@RequestMapping("/view")
	public ModelAndView view(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		FoodDRIPercent foodDRIPercent = foodDRIPercentService.getFoodDRIPercent(RequestUtils.getLong(request, "id"));
		request.setAttribute("foodDRIPercent", foodDRIPercent);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view);
		}

		String x_view = ViewProperties.getString("foodDRIPercent.view");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view);
		}

		return new ModelAndView("/heathcare/foodDRIPercent/view");
	}

}
