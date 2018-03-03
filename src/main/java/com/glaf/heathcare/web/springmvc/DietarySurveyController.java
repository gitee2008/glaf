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
import java.util.Calendar;
import java.util.Date;
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
import com.glaf.base.modules.sys.model.TenantConfig;
import com.glaf.base.modules.sys.service.DictoryService;
import com.glaf.base.modules.sys.service.SysTreeService;
import com.glaf.base.modules.sys.service.TenantConfigService;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.Constants;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;

import com.glaf.heathcare.SysConfig;
import com.glaf.heathcare.domain.DietarySurvey;
import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.query.DietarySurveyQuery;
import com.glaf.heathcare.service.DietarySurveyService;
import com.glaf.heathcare.service.FoodCompositionService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/dietarySurvey")
@RequestMapping("/heathcare/dietarySurvey")
public class DietarySurveyController {
	protected static final Log logger = LogFactory.getLog(DietarySurveyController.class);

	protected DictoryService dictoryService;

	protected DietarySurveyService dietarySurveyService;

	protected FoodCompositionService foodCompositionService;

	protected SysTreeService sysTreeService;

	protected TenantConfigService tenantConfigService;

	public DietarySurveyController() {

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
					DietarySurvey dietarySurvey = dietarySurveyService.getDietarySurvey(Long.valueOf(x));
					if (dietarySurvey != null) {
						if ((loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician"))
								&& (StringUtils.equals(loginContext.getTenantId(), dietarySurvey.getTenantId()))) {
							dietarySurveyService.deleteById(dietarySurvey.getId());
						}
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			DietarySurvey dietarySurvey = dietarySurveyService.getDietarySurvey(Long.valueOf(id));
			if (dietarySurvey != null) {
				if ((loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician"))
						&& (StringUtils.equals(loginContext.getTenantId(), dietarySurvey.getTenantId()))) {
					dietarySurveyService.deleteById(dietarySurvey.getId());
					return ResponseUtils.responseResult(true);
				}
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		LoginContext loginContext = RequestUtils.getLoginContext(request);

		DietarySurvey dietarySurvey = dietarySurveyService.getDietarySurvey(RequestUtils.getLong(request, "id"));
		if (dietarySurvey != null) {
			request.setAttribute("dietarySurvey", dietarySurvey);
			if (dietarySurvey.getFoodNodeId() != 0) {
				List<FoodComposition> foods = foodCompositionService.getFoodCompositions(dietarySurvey.getFoodNodeId());
				request.setAttribute("foods", foods);
			}

			if (dietarySurvey.getFoodId() != 0) {
				FoodComposition food = foodCompositionService.getFoodComposition(dietarySurvey.getFoodId());
				dietarySurvey.setFoodName(food.getName());
			}
		}

		if (dietarySurvey == null) {
			SysTree root = sysTreeService.getSysTreeByCode("FoodCategory");
			if (root != null) {
				List<SysTree> foodCategories = sysTreeService.getSysTreeList(root.getId());
				request.setAttribute("foodCategories", foodCategories);
			}
		}

		TenantConfig tenantConfig = tenantConfigService.getTenantConfigByTenantId(loginContext.getTenantId());
		if (tenantConfig != null && tenantConfig.getTypeId() > 0) {
			Dictory dict = dictoryService.find(tenantConfig.getTypeId());
			List<Dictory> dicts = dictoryService.getDictories(dict.getCode() + "%");
			request.setAttribute("dictoryList", dicts);
		} else {
			List<Dictory> dictoryList = dictoryService.getDictoryList(4501L);// 4501是餐点分类编号
			// List<Dictory> dictoryList =
			// dictoryService.getDictoryListByCategory("CAT_MEAL");
			request.setAttribute("dictoryList", dictoryList);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("dietarySurvey.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/dietarySurvey/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		DietarySurveyQuery query = new DietarySurveyQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		JSONObject result = new JSONObject();

		if (loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician")) {
			query.tenantId(loginContext.getTenantId());
		} else {
			return result.toJSONString().getBytes();
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

		int total = dietarySurveyService.getDietarySurveyCountByQueryCriteria(query);
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

			List<DietarySurvey> list = dietarySurveyService.getDietarySurveysByQueryCriteria(start, limit, query);

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

				for (DietarySurvey dietarySurvey : list) {
					JSONObject rowJSON = dietarySurvey.toJsonObject();
					rowJSON.put("id", dietarySurvey.getId());
					rowJSON.put("surveyId", dietarySurvey.getId());
					rowJSON.put("dietarySurveyId", dietarySurvey.getId());
					rowJSON.put("typeName", nameMap2.get(dietarySurvey.getTypeId()));
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

		return new ModelAndView("/heathcare/dietarySurvey/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("dietarySurvey.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/heathcare/dietarySurvey/query", modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveDietarySurvey")
	public byte[] saveDietarySurvey(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		long id = RequestUtils.getLong(request, "id");
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		DietarySurvey dietarySurvey = null;
		try {
			if (id > 0) {
				dietarySurvey = dietarySurveyService.getDietarySurvey(id);
			}
			if (dietarySurvey == null) {
				dietarySurvey = new DietarySurvey();
				Tools.populate(dietarySurvey, params);
				dietarySurvey.setTenantId(loginContext.getTenantId());
			} else {
				if (loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician")) {
					if (!(StringUtils.equals(loginContext.getTenantId(), dietarySurvey.getTenantId()))) {
						return ResponseUtils.responseJsonResult(false, "您没有该数据操作权限。");
					}
				} else {
					return ResponseUtils.responseJsonResult(false, "您没有该数据操作权限。");
				}
			}

			String dateString = request.getParameter("mealTime");
			Date mealTime = DateUtils.toDate(dateString);
			if (loginContext.getLimit() != Constants.UNLIMIT) {
				if (mealTime == null || mealTime.getTime() > System.currentTimeMillis()) {
					return ResponseUtils.responseJsonResult(false, "日期必须小于当前日期。");
				}
				if (mealTime.getTime() < (System.currentTimeMillis() - DateUtils.DAY * 7)) {
					return ResponseUtils.responseJsonResult(false, "日期必须是7天以内的日期。");
				}
			}

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(mealTime);
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH) + 1;
			dietarySurvey.setYear(year);
			dietarySurvey.setMonth(month);
			dietarySurvey.setSemester(SysConfig.getSemester());
			dietarySurvey.setFullDay(DateUtils.getYearMonthDay(mealTime));
			dietarySurvey.setMealTime(mealTime);

			long foodId = RequestUtils.getLong(request, "foodId");
			FoodComposition foodComposition = foodCompositionService.getFoodComposition(foodId);
			if (foodComposition != null) {
				dietarySurvey.setFoodId(foodId);
				dietarySurvey.setFoodName(foodComposition.getName());
				dietarySurvey.setFoodNodeId(foodComposition.getNodeId());
			}

			dietarySurvey.setTypeId(RequestUtils.getLong(request, "typeId"));
			dietarySurvey.setWeight(RequestUtils.getDouble(request, "weight"));
			dietarySurvey.setWeightPercent(RequestUtils.getDouble(request, "weightPercent"));
			dietarySurvey.setCookedWeight(RequestUtils.getDouble(request, "cookedWeight"));
			dietarySurvey.setWeightCookedPercent(RequestUtils.getDouble(request, "weightCookedPercent"));
			dietarySurvey.setPrice(RequestUtils.getDouble(request, "price"));
			dietarySurvey.setTotalPrice(RequestUtils.getDouble(request, "totalPrice"));
			dietarySurvey.setRemark(request.getParameter("remark"));
			dietarySurvey.setCreateBy(actorId);
			this.dietarySurveyService.save(dietarySurvey);

			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource
	public void setDictoryService(DictoryService dictoryService) {
		this.dictoryService = dictoryService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.dietarySurveyService")
	public void setDietarySurveyService(DietarySurveyService dietarySurveyService) {
		this.dietarySurveyService = dietarySurveyService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.foodCompositionService")
	public void setFoodCompositionService(FoodCompositionService foodCompositionService) {
		this.foodCompositionService = foodCompositionService;
	}

	@javax.annotation.Resource
	public void setSysTreeService(SysTreeService sysTreeService) {
		this.sysTreeService = sysTreeService;
	}

	@javax.annotation.Resource
	public void setTenantConfigService(TenantConfigService tenantConfigService) {
		this.tenantConfigService = tenantConfigService;
	}

}
