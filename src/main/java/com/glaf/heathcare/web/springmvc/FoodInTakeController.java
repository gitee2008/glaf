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
import com.glaf.base.modules.sys.model.SysTree;
import com.glaf.base.modules.sys.model.TenantConfig;
import com.glaf.base.modules.sys.service.DictoryService;
import com.glaf.base.modules.sys.service.SysTreeService;
import com.glaf.base.modules.sys.service.TenantConfigService;
import com.glaf.core.config.ViewProperties;

import com.glaf.core.security.*;
import com.glaf.core.util.*;
import com.glaf.heathcare.SysConfig;
import com.glaf.heathcare.domain.*;
import com.glaf.heathcare.query.*;
import com.glaf.heathcare.service.*;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/foodInTake")
@RequestMapping("/heathcare/foodInTake")
public class FoodInTakeController {
	protected static final Log logger = LogFactory.getLog(FoodInTakeController.class);

	protected FoodInTakeService foodInTakeService;

	protected FoodCompositionService foodCompositionService;

	protected GradeInfoService gradeInfoService;

	protected DictoryService dictoryService;

	protected SysTreeService sysTreeService;

	protected TenantConfigService tenantConfigService;

	public FoodInTakeController() {

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
					FoodInTake foodInTake = foodInTakeService.getFoodInTake(Long.valueOf(x));
					if (foodInTake != null) {
						if ((loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician"))
								&& (StringUtils.equals(loginContext.getTenantId(), foodInTake.getTenantId()))
								|| loginContext.getGradeIds().contains(foodInTake.getGradeId())) {
							foodInTakeService.deleteById(foodInTake.getId());
						}
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			FoodInTake foodInTake = foodInTakeService.getFoodInTake(Long.valueOf(id));
			if (foodInTake != null && (StringUtils.equals(foodInTake.getCreateBy(), loginContext.getActorId())
					|| loginContext.isSystemAdministrator())) {
				if ((loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician"))
						&& (StringUtils.equals(loginContext.getTenantId(), foodInTake.getTenantId()))
						|| loginContext.getGradeIds().contains(foodInTake.getGradeId())) {
					foodInTakeService.deleteById(foodInTake.getId());
				}
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		LoginContext loginContext = RequestUtils.getLoginContext(request);

		FoodInTake foodInTake = foodInTakeService.getFoodInTake(RequestUtils.getLong(request, "id"));
		if (foodInTake != null) {
			request.setAttribute("foodInTake", foodInTake);
			if (foodInTake.getFoodNodeId() != 0) {
				List<FoodComposition> foods = foodCompositionService.getFoodCompositions(foodInTake.getFoodNodeId());
				request.setAttribute("foods", foods);
			}
			if (foodInTake.getFoodId() != 0) {
				FoodComposition food = foodCompositionService.getFoodComposition(foodInTake.getFoodId());
				foodInTake.setFoodName(food.getName());
			}
		} else {
			List<GradeInfo> list = gradeInfoService.getGradeInfosByTenantId(loginContext.getTenantId());
			request.setAttribute("gradeInfos", list);

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

		String x_view = ViewProperties.getString("foodInTake.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/foodInTake/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		FoodInTakeQuery query = new FoodInTakeQuery();
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

		int total = foodInTakeService.getFoodInTakeCountByQueryCriteria(query);
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

			List<FoodInTake> list = foodInTakeService.getFoodInTakesByQueryCriteria(start, limit, query);

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

				for (FoodInTake foodInTake : list) {
					JSONObject rowJSON = foodInTake.toJsonObject();
					rowJSON.put("id", foodInTake.getId());
					rowJSON.put("rowId", foodInTake.getId());
					rowJSON.put("foodInTakeId", foodInTake.getId());
					rowJSON.put("typeName", nameMap2.get(foodInTake.getTypeId()));
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

		return new ModelAndView("/heathcare/foodInTake/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("foodInTake.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/heathcare/foodInTake/query", modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveFoodInTake")
	public byte[] saveFoodInTake(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		long id = RequestUtils.getLong(request, "id");
		String gradeId = request.getParameter("gradeId");
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		FoodInTake foodInTake = null;
		try {
			if (id > 0) {
				foodInTake = foodInTakeService.getFoodInTake(id);
			}
			if (foodInTake == null) {
				foodInTake = new FoodInTake();
				Tools.populate(foodInTake, params);
				foodInTake.setTenantId(loginContext.getTenantId());
				foodInTake.setGradeId(gradeId);
			} else {
				if (loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician")) {
					if (!(StringUtils.equals(loginContext.getTenantId(), foodInTake.getTenantId()))) {
						return ResponseUtils.responseJsonResult(false, "您没有该数据操作权限。");
					}
				} else {
					if (!loginContext.getGradeIds().contains(gradeId)) {
						return ResponseUtils.responseJsonResult(false, "您没有该数据操作权限。");
					}
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
			foodInTake.setYear(year);
			foodInTake.setMonth(month);
			foodInTake.setSemester(SysConfig.getSemester());
			foodInTake.setFullDay(DateUtils.getYearMonthDay(mealTime));
			foodInTake.setMealTime(mealTime);

			long foodId = RequestUtils.getLong(request, "foodId");
			FoodComposition foodComposition = foodCompositionService.getFoodComposition(foodId);
			if (foodComposition != null) {
				foodInTake.setFoodId(foodId);
				foodInTake.setFoodName(foodComposition.getName());
				foodInTake.setFoodNodeId(foodComposition.getNodeId());
			}

			foodInTake.setPerson(RequestUtils.getInt(request, "person"));
			foodInTake.setAllocationWeight(RequestUtils.getDouble(request, "allocationWeight"));
			foodInTake.setRemainWeight(RequestUtils.getDouble(request, "remainWeight"));
			foodInTake.setMealWeight(RequestUtils.getDouble(request, "mealWeight"));
			foodInTake.setCreateBy(actorId);
			this.foodInTakeService.save(foodInTake);

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

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.foodCompositionService")
	public void setFoodCompositionService(FoodCompositionService foodCompositionService) {
		this.foodCompositionService = foodCompositionService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.foodInTakeService")
	public void setFoodInTakeService(FoodInTakeService foodInTakeService) {
		this.foodInTakeService = foodInTakeService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.gradeInfoService")
	public void setGradeInfoService(GradeInfoService gradeInfoService) {
		this.gradeInfoService = gradeInfoService;
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
