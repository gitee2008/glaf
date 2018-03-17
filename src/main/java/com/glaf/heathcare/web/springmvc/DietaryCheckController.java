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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.glaf.base.modules.sys.model.Dictory;
import com.glaf.base.modules.sys.model.TenantConfig;
import com.glaf.base.modules.sys.service.DictoryService;
import com.glaf.base.modules.sys.service.TenantConfigService;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.RequestUtils;
import com.glaf.heathcare.service.DietaryCountService;
import com.glaf.heathcare.service.DietaryItemService;
import com.glaf.heathcare.service.DietaryService;
import com.glaf.heathcare.service.DietaryTemplateService;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.heathcare.service.GoodsInStockService;
import com.glaf.heathcare.service.GoodsOutStockService;
import com.glaf.heathcare.service.GoodsPurchasePlanService;
import com.glaf.heathcare.service.PersonInfoService;

@Controller("/heathcare/dietaryCheck")
@RequestMapping("/heathcare/dietaryCheck")
public class DietaryCheckController {

	protected static final Log logger = LogFactory.getLog(DietaryCheckController.class);

	protected DictoryService dictoryService;

	protected DietaryService dietaryService;

	protected DietaryCountService dietaryCountService;

	protected DietaryItemService dietaryItemService;

	protected DietaryTemplateService dietaryTemplateService;

	protected FoodCompositionService foodCompositionService;

	protected GoodsInStockService goodsInStockService;

	protected GoodsOutStockService goodsOutStockService;

	protected GoodsPurchasePlanService goodsPurchasePlanService;

	protected PersonInfoService personInfoService;

	protected TenantConfigService tenantConfigService;

	@RequestMapping("/fee")
	public ModelAndView fee(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		String tenantId = request.getParameter("tenantId");
		if (StringUtils.isEmpty(tenantId)) {
			tenantId = loginContext.getTenantId();
		}

		TenantConfig tenantConfig = tenantConfigService.getTenantConfigByTenantId(tenantId);
		if (tenantConfig != null && tenantConfig.getTypeId() > 0) {
			Dictory dict = dictoryService.find(tenantConfig.getTypeId());
			List<Dictory> dicts = dictoryService.getDictories(dict.getCode() + "%");
			request.setAttribute("dictoryList", dicts);
		} else {
			List<Dictory> dictoryList = dictoryService.getDictoryList(4501L);// 4501是餐点分类编号
			request.setAttribute("dictoryList", dictoryList);
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		List<Integer> years = new ArrayList<Integer>();
		List<Integer> months = new ArrayList<Integer>();
		List<Integer> days = new ArrayList<Integer>();
		List<Integer> weeks = new ArrayList<Integer>();

		years.add(year);
		months.add(month);
		if (month == 12) {
			years.add(year + 1);
			months.add(1);
		} else {
			months.add(month + 1);
		}

		for (int i = 1; i <= 31; i++) {
			days.add(i);
		}

		for (int i = 1; i <= 20; i++) {
			weeks.add(i);
		}

		request.setAttribute("years", years);
		request.setAttribute("months", months);
		request.setAttribute("days", days);
		request.setAttribute("weeks", weeks);
		request.setAttribute("year", year);
		request.setAttribute("month", month);
		request.setAttribute("day", day);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("dietaryCheck.fee");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/dietaryCheck/feeMain", modelMap);
	}

	@RequestMapping("/intake")
	public ModelAndView intake(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		String tenantId = request.getParameter("tenantId");
		if (StringUtils.isEmpty(tenantId)) {
			tenantId = loginContext.getTenantId();
		}

		TenantConfig tenantConfig = tenantConfigService.getTenantConfigByTenantId(tenantId);
		if (tenantConfig != null && tenantConfig.getTypeId() > 0) {
			Dictory dict = dictoryService.find(tenantConfig.getTypeId());
			List<Dictory> dicts = dictoryService.getDictories(dict.getCode() + "%");
			request.setAttribute("dictoryList", dicts);
		} else {
			List<Dictory> dictoryList = dictoryService.getDictoryList(4501L);// 4501是餐点分类编号
			request.setAttribute("dictoryList", dictoryList);
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		List<Integer> years = new ArrayList<Integer>();
		List<Integer> months = new ArrayList<Integer>();
		List<Integer> days = new ArrayList<Integer>();
		List<Integer> weeks = new ArrayList<Integer>();

		years.add(year);
		months.add(month);
		if (month == 12) {
			years.add(year + 1);
			months.add(1);
		} else {
			months.add(month + 1);
		}

		for (int i = 1; i <= 31; i++) {
			days.add(i);
		}

		for (int i = 1; i <= 20; i++) {
			weeks.add(i);
		}

		request.setAttribute("years", years);
		request.setAttribute("months", months);
		request.setAttribute("days", days);
		request.setAttribute("weeks", weeks);
		request.setAttribute("year", year);
		request.setAttribute("month", month);
		request.setAttribute("day", day);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("dietaryCheck.intake");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/dietaryCheck/intakeMain", modelMap);
	}

	@RequestMapping("/nutrient")
	public ModelAndView nutrient(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		String tenantId = request.getParameter("tenantId");
		if (StringUtils.isEmpty(tenantId)) {
			tenantId = loginContext.getTenantId();
		}

		TenantConfig tenantConfig = tenantConfigService.getTenantConfigByTenantId(tenantId);
		if (tenantConfig != null && tenantConfig.getTypeId() > 0) {
			Dictory dict = dictoryService.find(tenantConfig.getTypeId());
			List<Dictory> dicts = dictoryService.getDictories(dict.getCode() + "%");
			request.setAttribute("dictoryList", dicts);
		} else {
			List<Dictory> dictoryList = dictoryService.getDictoryList(4501L);// 4501是餐点分类编号
			request.setAttribute("dictoryList", dictoryList);
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		List<Integer> years = new ArrayList<Integer>();
		List<Integer> months = new ArrayList<Integer>();
		List<Integer> days = new ArrayList<Integer>();
		List<Integer> weeks = new ArrayList<Integer>();

		years.add(year);
		months.add(month);
		if (month == 12) {
			years.add(year + 1);
			months.add(1);
		} else {
			months.add(month + 1);
		}

		for (int i = 1; i <= 31; i++) {
			days.add(i);
		}

		for (int i = 1; i <= 20; i++) {
			weeks.add(i);
		}

		request.setAttribute("years", years);
		request.setAttribute("months", months);
		request.setAttribute("days", days);
		request.setAttribute("weeks", weeks);
		request.setAttribute("year", year);
		request.setAttribute("month", month);
		request.setAttribute("day", day);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("dietaryCheck.nutrient");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/dietaryCheck/nutrientMain", modelMap);
	}

	@javax.annotation.Resource
	public void setDictoryService(DictoryService dictoryService) {
		this.dictoryService = dictoryService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.dietaryCountService")
	public void setDietaryCountService(DietaryCountService dietaryCountService) {
		this.dietaryCountService = dietaryCountService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.dietaryItemService")
	public void setDietaryItemService(DietaryItemService dietaryItemService) {
		this.dietaryItemService = dietaryItemService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.dietaryService")
	public void setDietaryService(DietaryService dietaryService) {
		this.dietaryService = dietaryService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.dietaryTemplateService")
	public void setDietaryTemplateService(DietaryTemplateService dietaryTemplateService) {
		this.dietaryTemplateService = dietaryTemplateService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.foodCompositionService")
	public void setFoodCompositionService(FoodCompositionService foodCompositionService) {
		this.foodCompositionService = foodCompositionService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.goodsInStockService")
	public void setGoodsInStockService(GoodsInStockService goodsInStockService) {
		this.goodsInStockService = goodsInStockService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.goodsOutStockService")
	public void setGoodsOutStockService(GoodsOutStockService goodsOutStockService) {
		this.goodsOutStockService = goodsOutStockService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.goodsPurchasePlanService")
	public void setGoodsPurchasePlanService(GoodsPurchasePlanService goodsPurchasePlanService) {
		this.goodsPurchasePlanService = goodsPurchasePlanService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.personInfoService")
	public void setPersonInfoService(PersonInfoService personInfoService) {
		this.personInfoService = personInfoService;
	}

	@javax.annotation.Resource
	public void setTenantConfigService(TenantConfigService tenantConfigService) {
		this.tenantConfigService = tenantConfigService;
	}

}
