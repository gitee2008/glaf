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
import com.glaf.heathcare.domain.FoodDRI;
import com.glaf.heathcare.helper.PermissionHelper;
import com.glaf.heathcare.query.FoodDRIQuery;
import com.glaf.heathcare.service.FoodDRIService;

@Controller("/heathcare/foodDRI")
@RequestMapping("/heathcare/foodDRI")
public class FoodDRIController {
	protected static final Log logger = LogFactory.getLog(FoodDRIController.class);

	protected FoodDRIService foodDRIService;

	protected DictoryService dictoryService;

	protected SysTreeService sysTreeService;

	public FoodDRIController() {

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
					FoodDRI foodDRI = foodDRIService.getFoodDRI(Long.valueOf(x));
					if (foodDRI != null && (StringUtils.equals(foodDRI.getCreateBy(), loginContext.getActorId())
							|| loginContext.isSystemAdministrator())) {
						foodDRIService.deleteById(foodDRI.getId());
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			FoodDRI foodDRI = foodDRIService.getFoodDRI(Long.valueOf(id));
			if (foodDRI != null && (StringUtils.equals(foodDRI.getCreateBy(), loginContext.getActorId())
					|| loginContext.isSystemAdministrator())) {
				foodDRIService.deleteById(foodDRI.getId());
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

		FoodDRI foodDRI = foodDRIService.getFoodDRI(RequestUtils.getLong(request, "id"));
		if (foodDRI != null) {
			request.setAttribute("foodDRI", foodDRI);
		}

		List<Dictory> dictoryList = dictoryService.getDictoryList(4501L);// 4501是餐点分类编号
		request.setAttribute("dictoryList", dictoryList);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("foodDRI.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/foodDRI/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		FoodDRIQuery query = new FoodDRIQuery();
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
		int total = foodDRIService.getFoodDRICountByQueryCriteria(query);
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

			List<FoodDRI> list = foodDRIService.getFoodDRIsByQueryCriteria(start, limit, query);

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

				for (FoodDRI foodDRI : list) {
					JSONObject rowJSON = foodDRI.toJsonObject();
					rowJSON.put("id", foodDRI.getId());
					rowJSON.put("typeName", nameMap2.get(foodDRI.getTypeId()));
					rowJSON.put("foodDRIId", foodDRI.getId());
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

		return new ModelAndView("/heathcare/foodDRI/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("foodDRI.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/heathcare/foodDRI/query", modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveFoodDRI")
	public byte[] saveFoodDRI(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isSystemAdministrator()) {
			String actorId = loginContext.getActorId();
			Map<String, Object> params = RequestUtils.getParameterMap(request);
			FoodDRI foodDRI = new FoodDRI();
			try {
				Tools.populate(foodDRI, params);
				foodDRI.setName(request.getParameter("name"));
				foodDRI.setDescription(request.getParameter("description"));
				foodDRI.setAge(RequestUtils.getInt(request, "age"));
				foodDRI.setType(request.getParameter("type"));
				foodDRI.setTypeId(RequestUtils.getLong(request, "typeId"));
				foodDRI.setHeatEnergy(RequestUtils.getDouble(request, "heatEnergy"));
				foodDRI.setProtein(RequestUtils.getDouble(request, "protein"));
				foodDRI.setFat(RequestUtils.getDouble(request, "fat"));
				foodDRI.setCarbohydrate(RequestUtils.getDouble(request, "carbohydrate"));
				foodDRI.setVitaminA(RequestUtils.getDouble(request, "vitaminA"));
				foodDRI.setVitaminB1(RequestUtils.getDouble(request, "vitaminB1"));
				foodDRI.setVitaminB2(RequestUtils.getDouble(request, "vitaminB2"));
				foodDRI.setVitaminB6(RequestUtils.getDouble(request, "vitaminB6"));
				foodDRI.setVitaminB12(RequestUtils.getDouble(request, "vitaminB12"));
				foodDRI.setVitaminC(RequestUtils.getDouble(request, "vitaminC"));
				foodDRI.setVitaminE(RequestUtils.getDouble(request, "vitaminE"));
				foodDRI.setCarotene(RequestUtils.getDouble(request, "carotene"));
				foodDRI.setRetinol(RequestUtils.getDouble(request, "retinol"));
				foodDRI.setNicotinicCid(RequestUtils.getDouble(request, "nicotinicCid"));
				foodDRI.setCalcium(RequestUtils.getDouble(request, "calcium"));
				foodDRI.setIron(RequestUtils.getDouble(request, "iron"));
				foodDRI.setZinc(RequestUtils.getDouble(request, "zinc"));
				foodDRI.setIodine(RequestUtils.getDouble(request, "iodine"));
				foodDRI.setPhosphorus(RequestUtils.getDouble(request, "phosphorus"));
				foodDRI.setSortNo(RequestUtils.getInt(request, "sortNo"));
				foodDRI.setEnableFlag(request.getParameter("enableFlag"));
				foodDRI.setCreateBy(actorId);
				foodDRI.setUpdateBy(actorId);
				this.foodDRIService.save(foodDRI);

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

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.foodDRIService")
	public void setFoodDRIService(FoodDRIService foodDRIService) {
		this.foodDRIService = foodDRIService;
	}

	@javax.annotation.Resource
	public void setSysTreeService(SysTreeService sysTreeService) {
		this.sysTreeService = sysTreeService;
	}

	@RequestMapping("/view")
	public ModelAndView view(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		FoodDRI foodDRI = foodDRIService.getFoodDRI(RequestUtils.getLong(request, "id"));
		request.setAttribute("foodDRI", foodDRI);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view);
		}

		String x_view = ViewProperties.getString("foodDRI.view");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view);
		}

		return new ModelAndView("/heathcare/foodDRI/view");
	}

}
