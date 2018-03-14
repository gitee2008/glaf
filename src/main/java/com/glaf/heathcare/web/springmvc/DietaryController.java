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
import java.util.ArrayList;
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
import com.glaf.base.modules.sys.model.TenantConfig;
import com.glaf.base.modules.sys.service.DictoryService;
import com.glaf.base.modules.sys.service.TenantConfigService;
import com.glaf.core.base.BaseItem;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.Constants;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.StringTools;
import com.glaf.core.util.Tools;

import com.glaf.heathcare.SysConfig;
import com.glaf.heathcare.bean.DietaryBean;
import com.glaf.heathcare.bean.DietaryClearBean;
import com.glaf.heathcare.bean.GoodsPurchasePlanBean;
import com.glaf.heathcare.bean.DailyGoodsPurchasePlanBean;
import com.glaf.heathcare.domain.Dietary;
import com.glaf.heathcare.domain.DietaryTemplate;
import com.glaf.heathcare.domain.PersonInfo;
import com.glaf.heathcare.query.DietaryQuery;
import com.glaf.heathcare.query.DietaryTemplateQuery;
import com.glaf.heathcare.service.DietaryCountService;
import com.glaf.heathcare.service.DietaryItemService;
import com.glaf.heathcare.service.DietaryService;
import com.glaf.heathcare.service.DietaryTemplateService;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.heathcare.service.GoodsInStockService;
import com.glaf.heathcare.service.GoodsOutStockService;
import com.glaf.heathcare.service.GoodsPurchasePlanService;
import com.glaf.heathcare.service.PersonInfoService;

import net.iharder.Base64;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/dietary")
@RequestMapping("/heathcare/dietary")
public class DietaryController {
	protected static final Log logger = LogFactory.getLog(DietaryController.class);

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

	public DietaryController() {

	}

	/**
	 * 每日采购计划
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addDailyParchasePlan")
	public byte[] addDailyParchasePlan(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		/**
		 * 角色HealthPhysician、Buyer和TenantAdmin可以增加食谱采购
		 */
		if (loginContext.getRoles().contains("HealthPhysician") || loginContext.getRoles().contains("Buyer")
				|| loginContext.getRoles().contains("TenantAdmin")) {
			int year = RequestUtils.getInt(request, "year");
			int month = RequestUtils.getInt(request, "month");
			int week = RequestUtils.getInt(request, "week");
			int fullDay = RequestUtils.getInt(request, "fullDay");
			String objectIds = request.getParameter("objectIds");

			List<PersonInfo> persons = personInfoService.getPersonInfos(loginContext.getTenantId());
			if (persons == null || persons.isEmpty()) {
				return ResponseUtils.responseJsonResult(false, "计划就餐人数未设置，请先设置才可生成采购计划！");
			}

			if (StringUtils.isNotEmpty(objectIds)) {
				List<Long> ids = StringTools.splitToLong(objectIds);
				if (ids != null && !ids.isEmpty()) {
					// GoodsPurchasePlanBean purchaseBean = new GoodsPurchasePlanBean();
					// purchaseBean.addParchasePlan(loginContext, ids);
					return ResponseUtils.responseResult(true);
				}
			} else {
				try {
					logger.debug("正在生成一日采购计划......");
					DailyGoodsPurchasePlanBean purchaseBean = new DailyGoodsPurchasePlanBean();
					purchaseBean.addDailyParchasePlan(loginContext, year, month, week, fullDay);
					return ResponseUtils.responseResult(true);
				} catch (Exception ex) {
					ex.printStackTrace();
					logger.error(ex);
				}
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@ResponseBody
	@RequestMapping("/addDietary")
	public byte[] addDietary(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		/**
		 * 角色HealthPhysician和TenantAdmin可以增加食谱
		 */
		if (loginContext.getRoles().contains("HealthPhysician") || loginContext.getRoles().contains("TenantAdmin")) {
			long templateId = RequestUtils.getLong(request, "templateId");
			if (templateId > 0) {
				try {
					long newId = dietaryService.copyTemplate(loginContext, templateId);

					Calendar calendar = Calendar.getInstance();
					calendar.setTime(new Date());
					int year = calendar.get(Calendar.YEAR);
					int month = calendar.get(Calendar.MONTH) + 1;
					int week = RequestUtils.getInt(request, "week");

					Dietary dietary = dietaryService.getDietary(loginContext.getTenantId(), newId);

					dietary.setYear(year);
					dietary.setMonth(month);
					dietary.setWeek(week);

					dietary.setSemester(SysConfig.getSemester());
					dietaryService.save(dietary);
					return ResponseUtils.responseResult(true);
				} catch (Exception ex) {
					ex.printStackTrace();
					logger.error(ex);
				}
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@ResponseBody
	@RequestMapping("/addParchasePlan")
	public byte[] addParchasePlan(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		/**
		 * 角色HealthPhysician、Buyer和TenantAdmin可以增加食谱采购
		 */
		if (loginContext.getRoles().contains("HealthPhysician") || loginContext.getRoles().contains("Buyer")
				|| loginContext.getRoles().contains("TenantAdmin")) {
			int year = RequestUtils.getInt(request, "year");
			int month = RequestUtils.getInt(request, "month");
			int week = RequestUtils.getInt(request, "week");
			String objectIds = request.getParameter("objectIds");

			List<PersonInfo> persons = personInfoService.getPersonInfos(loginContext.getTenantId());
			if (persons == null || persons.isEmpty()) {
				return ResponseUtils.responseJsonResult(false, "计划就餐人数未设置，请先设置才可生成采购计划！");
			}

			if (StringUtils.isNotEmpty(objectIds)) {
				List<Long> ids = StringTools.splitToLong(objectIds);
				if (ids != null && !ids.isEmpty()) {
					// GoodsPurchasePlanBean purchaseBean = new GoodsPurchasePlanBean();
					// purchaseBean.addParchasePlan(loginContext, ids);
					return ResponseUtils.responseResult(true);
				}
			} else {
				try {
					logger.debug("正在生成采购计划......");
					GoodsPurchasePlanBean purchaseBean = new GoodsPurchasePlanBean();
					purchaseBean.addParchasePlan(loginContext, year, month, week);
					return ResponseUtils.responseResult(true);
				} catch (Exception ex) {
					ex.printStackTrace();
					logger.error(ex);
				}
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@ResponseBody
	@RequestMapping("/batchAdd")
	public byte[] batchAdd(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug("params:" + params);
		logger.debug("limit:" + loginContext.getLimit());
		/**
		 * 角色HealthPhysician和TenantAdmin可以增加食谱
		 */
		if (loginContext.isSystemAdministrator() || loginContext.getRoles().contains("HealthPhysician")
				|| loginContext.getRoles().contains("TenantAdmin")) {
			int suitNo = RequestUtils.getInt(request, "suitNo");
			int week = RequestUtils.getInt(request, "week");
			String sysFlag = request.getParameter("sysFlag");

			DietaryTemplateQuery query = new DietaryTemplateQuery();
			if (StringUtils.equals(sysFlag, "Y")) {
				query.sysFlag(sysFlag);
			} else {
				query.tenantId(loginContext.getTenantId());
			}
			query.suitNo(suitNo);

			List<DietaryTemplate> templates = null;
			List<Long> templateIds = new ArrayList<Long>();

			int errorCount = 0;

			for (int i = 1; i <= 5; i++) {
				query.dayOfWeek(i);
				String dateString = request.getParameter("dateString" + i);
				if (StringUtils.isNotEmpty(dateString)) {
					Date date = DateUtils.toDate(dateString);
					if (loginContext.getLimit() != Constants.UNLIMIT) {
						if (date == null || date.getTime() < System.currentTimeMillis()) {
							return ResponseUtils.responseJsonResult(false, "日期必须大于当前日期。");
						}
						if (date.getTime() > (System.currentTimeMillis() + DateUtils.DAY * 30)) {
							return ResponseUtils.responseJsonResult(false, "日期必须是一月以内的日期。");
						}
					}
					try {
						templates = dietaryTemplateService.list(query);
						if (templates != null && !templates.isEmpty()) {
							templateIds.clear();
							for (DietaryTemplate tpl : templates) {
								templateIds.add(tpl.getId());
							}
							/**
							 * 将选择的模板复制一份作为新实例，以便修改其中的组成部分。
							 */
							dietaryService.copyTemplates(loginContext, date, week, templateIds);
						}
					} catch (Exception ex) {
						errorCount++;
						logger.error(ex);
					}
				}
			}
			if (errorCount == 0) {
				return ResponseUtils.responseJsonResult(true);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@RequestMapping("/batchCal")
	public ModelAndView batchCal(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		TenantConfig tenantConfig = tenantConfigService.getTenantConfigByTenantId(loginContext.getTenantId());
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

		String x_view = ViewProperties.getString("dietary.batchCal");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/dietary/batchCal", modelMap);
	}

	@RequestMapping("/batchEdit")
	public ModelAndView batchEdit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		TenantConfig tenantConfig = tenantConfigService.getTenantConfigByTenantId(loginContext.getTenantId());
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

		int maxWeek = dietaryService.getMaxWeek(loginContext.getTenantId(), year, SysConfig.getSemester());
		request.setAttribute("maxWeek", maxWeek);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("dietary.batchEdit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/dietary/batchEdit", modelMap);
	}

	@ResponseBody
	@RequestMapping("/calculate")
	public byte[] calculate(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug("params:" + params);
		long id = RequestUtils.getLong(request, "id");
		if (id > 0) {
			Dietary dietary = dietaryService.getDietary(loginContext.getTenantId(), id);
			if (dietary != null) {
				// DietaryBean bean = new DietaryBean();
				// bean.calculate(loginContext.getTenantId(), dietary.getId());
				dietaryService.calculate(loginContext.getTenantId(), dietary.getId());
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@ResponseBody
	@RequestMapping("/calculateAll")
	public byte[] calculateAll(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String objectIds = request.getParameter("objectIds");
		if (StringUtils.isNotEmpty(objectIds)) {
			DietaryBean bean = new DietaryBean();
			bean.calculateAll(loginContext.getTenantId(), StringTools.splitToLong(objectIds));
			return ResponseUtils.responseResult(true);
		}
		return ResponseUtils.responseResult(false);
	}

	@ResponseBody
	@RequestMapping("/delete")
	public byte[] delete(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Long id = RequestUtils.getLong(request, "id");
		String ids = request.getParameter("ids");
		if (StringUtils.isNotEmpty(ids)) {
			StringTokenizer token = new StringTokenizer(ids, ",");
			while (token.hasMoreTokens()) {
				String x = token.nextToken();
				if (StringUtils.isNotEmpty(x)) {
					Dietary dietary = dietaryService.getDietary(loginContext.getTenantId(), Long.valueOf(x));
					if (dietary != null && ((StringUtils.equals(dietary.getCreateBy(), loginContext.getActorId()))
							|| (StringUtils.equals(dietary.getTenantId(), loginContext.getTenantId())
									&& (loginContext.getRoles().contains("TenantAdmin")
											|| loginContext.getRoles().contains("HealthPhysician"))))) {
						if (!StringUtils.equals(dietary.getPurchaseFlag(), "Y")) {
							dietaryService.deleteById(loginContext.getTenantId(), dietary.getId());
						} else {
							return ResponseUtils.responseJsonResult(false, "不能删除已经结单的记录。");
						}
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			Dietary dietary = dietaryService.getDietary(loginContext.getTenantId(), Long.valueOf(id));
			if (dietary != null && ((StringUtils.equals(dietary.getCreateBy(), loginContext.getActorId()))
					|| (StringUtils.equals(dietary.getTenantId(), loginContext.getTenantId())
							&& (loginContext.getRoles().contains("TenantAdmin")
									|| loginContext.getRoles().contains("HealthPhysician"))))) {
				if (!StringUtils.equals(dietary.getPurchaseFlag(), "Y")) {
					dietaryService.deleteById(loginContext.getTenantId(), dietary.getId());
					return ResponseUtils.responseResult(true);
				} else {
					return ResponseUtils.responseJsonResult(false, "不能删除已经结单的记录。");
				}
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		Dietary dietary = dietaryService.getDietary(loginContext.getTenantId(), RequestUtils.getLong(request, "id"));
		if (dietary != null) {
			request.setAttribute("dietary", dietary);
			Date date = DateUtils.toDate(String.valueOf(dietary.getFullDay()));
			request.setAttribute("date", date);
		}

		TenantConfig tenantConfig = tenantConfigService.getTenantConfigByTenantId(loginContext.getTenantId());
		if (tenantConfig != null && tenantConfig.getTypeId() > 0) {
			Dictory dict = dictoryService.find(tenantConfig.getTypeId());
			List<Dictory> dicts = dictoryService.getDictories(dict.getCode() + "%");
			request.setAttribute("dictoryList", dicts);
		} else {
			// List<Dictory> dictoryList =
			// dictoryService.getDictoryList(4501L);// 4501是餐点分类编号
			List<Dictory> dictoryList = dictoryService.getDictoryListByCategory("CAT_MEAL");
			request.setAttribute("dictoryList", dictoryList);
		}

		List<Integer> weeks = new ArrayList<Integer>();
		for (int i = 1; i <= 20; i++) {
			weeks.add(i);
		}
		request.setAttribute("weeks", weeks);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("dietary.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/dietary/edit", modelMap);
	}

	@ResponseBody
	@RequestMapping("/executeCount")
	public byte[] executeCount(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		/**
		 * 角色HealthPhysician和TenantAdmin可以执行汇总
		 */
		if (loginContext.getRoles().contains("HealthPhysician") || loginContext.getRoles().contains("TenantAdmin")) {
			int year = RequestUtils.getInt(request, "year");
			int week = RequestUtils.getInt(request, "week");
			try {
				DietaryBean bean = new DietaryBean();
				bean.executeCountAll(loginContext.getTenantId(), year, week);
				bean.executeCountItems(loginContext.getTenantId(), year, week);
				return ResponseUtils.responseResult(true);
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	public DietaryTemplateService getDietaryTemplateService() {
		return dietaryTemplateService;
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug("params:" + params);
		DietaryQuery query = new DietaryQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);
		query.setSemester(SysConfig.getSemester());

		int fullDay = RequestUtils.getInt(request, "fullDay");
		int year = RequestUtils.getInt(request, "year");
		int week = RequestUtils.getInt(request, "week");

		if (year > 0) {
			query.year(year);
		}

		if (week > 0) {
			query.week(week);
		}

		if (fullDay > 0) {
			query.fullDay(fullDay);
		}

		if (!loginContext.isSystemAdministrator()) {
			query.tenantId(loginContext.getTenantId());
		}

		String nameLike = request.getParameter("nameLike_enc");
		if (StringUtils.isNotEmpty(nameLike)) {
			query.setNameLike(RequestUtils.decodeString(nameLike));
		}

		long typeId = RequestUtils.getLong(request, "typeId");
		if (typeId > 0) {
			List<Long> typeIds = new ArrayList<Long>();
			if (typeId == 3301) {// 三餐一点
				typeIds.add(3301L);
				typeIds.add(3302L);
				typeIds.add(3303L);
				typeIds.add(3304L);
				typeIds.add(3305L);
			} else if (typeId == 3401) {// 三餐两点
				typeIds.add(3401L);
				typeIds.add(3402L);
				typeIds.add(3403L);
				typeIds.add(3404L);
				typeIds.add(3405L);
				typeIds.add(3406L);
			} else if (typeId == 3411) {// 二餐二点
				typeIds.add(3411L);
				typeIds.add(3412L);
				typeIds.add(3413L);
				typeIds.add(3414L);
				typeIds.add(3415L);
			} else if (typeId == 3311) {// 二餐一点
				typeIds.add(3311L);
				typeIds.add(3312L);
				typeIds.add(3313L);
				typeIds.add(3314L);
			} else if (typeId == 3321) {// 一餐二点
				typeIds.add(3321L);
				typeIds.add(3322L);
				typeIds.add(3323L);
				typeIds.add(3324L);
			}
			query.setTypeId(null);
			query.typeIds(typeIds);
		}

		int start = 0;
		int limit = 100;
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
			limit = 100;
		}

		JSONObject result = new JSONObject();
		int total = dietaryService.getDietaryCountByQueryCriteria(query);
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

			List<Dietary> list = dietaryService.getDietarysByQueryCriteria(start, limit, query);

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

				for (Dietary dietary : list) {
					JSONObject rowJSON = dietary.toJsonObject();
					rowJSON.put("id", dietary.getId());
					rowJSON.put("rowId", dietary.getId());
					rowJSON.put("dietaryId", dietary.getId());
					rowJSON.put("startIndex", ++start);
					rowJSON.put("typeName", nameMap2.get(dietary.getTypeId()));
					if (dietary.getDayOfWeek() != 0) {
						switch (dietary.getDayOfWeek()) {
						case 1:
							rowJSON.put("dayOfWeekName", "星期一");
							break;
						case 2:
							rowJSON.put("dayOfWeekName", "星期二");
							break;
						case 3:
							rowJSON.put("dayOfWeekName", "星期三");
							break;
						case 4:
							rowJSON.put("dayOfWeekName", "星期四");
							break;
						case 5:
							rowJSON.put("dayOfWeekName", "星期五");
							break;
						case 6:
							rowJSON.put("dayOfWeekName", "星期六");
							break;
						case 7:
							rowJSON.put("dayOfWeekName", "星期日");
							break;
						default:
							rowJSON.put("dayOfWeekName", "-");
							break;
						}
					}
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
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		String nameLike = request.getParameter("nameLike");
		if (StringUtils.isNotEmpty(nameLike)) {
			nameLike = nameLike.trim();
			request.setAttribute("nameLike_enc", RequestUtils.encodeString(nameLike));
			request.setAttribute("nameLike_base64", Base64.encodeBytes(nameLike.getBytes()));
		}

		String wordLike = request.getParameter("wordLike");
		if (StringUtils.isNotEmpty(wordLike)) {
			wordLike = wordLike.trim();
			request.setAttribute("wordLike_enc", RequestUtils.encodeString(wordLike));
			request.setAttribute("wordLike_base64", Base64.encodeBytes(wordLike.getBytes()));
		}

		int fullDay = RequestUtils.getInt(request, "fullDay");
		int yearx = RequestUtils.getInt(request, "year");
		int weekx = RequestUtils.getInt(request, "week");
		int semester = SysConfig.getSemester();

		if (yearx > 0 && weekx > 0) {
			List<Integer> days = dietaryService.getDays(loginContext.getTenantId(), yearx, semester, weekx);
			request.setAttribute("days", days);
			if (fullDay == 0) {
				if (days != null && days.size() > 0) {
					fullDay = days.get(0);
				}
			}
		}

		Calendar calendar = Calendar.getInstance();
		if (fullDay > 0) {
			calendar.setTime(DateUtils.toDate(String.valueOf(fullDay)));

			int suitNo = fullDay;
			int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

			request.setAttribute("suitNo", suitNo);
			request.setAttribute("dayOfWeek", dayOfWeek);
		}

		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		List<Integer> years = new ArrayList<Integer>();
		List<Integer> months = new ArrayList<Integer>();
		List<Integer> weeks = new ArrayList<Integer>();

		years.add(year);
		months.add(month);
		if (month == 12) {
			years.add(year + 1);
			months.add(1);
		} else {
			months.add(month + 1);
		}

		for (int i = 1; i <= 20; i++) {
			weeks.add(i);
		}

		request.setAttribute("years", years);
		request.setAttribute("months", months);
		request.setAttribute("weeks", weeks);
		request.setAttribute("year", year);
		request.setAttribute("month", month);
		request.setAttribute("day", day);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/dietary/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("dietary.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/heathcare/dietary/query", modelMap);
	}

	@ResponseBody
	@RequestMapping("/removeDayPlan")
	public byte[] removeDayPlan(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		/**
		 * 角色HealthPhysician、Buyer和TenantAdmin可以删除计划数据
		 */
		if (loginContext.getRoles().contains("HealthPhysician") || loginContext.getRoles().contains("TenantAdmin")) {
			Date date = RequestUtils.getDate(request, "dateString");
			int dateAfter = DateUtils.getNowYearMonthDay();
			try {
				logger.debug("准备删除计划数据......");
				if (date != null) {
					DietaryClearBean bean = new DietaryClearBean();
					bean.removePlanData(loginContext.getTenantId(), DateUtils.getYearMonthDay(date), dateAfter);
					return ResponseUtils.responseResult(true);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@ResponseBody
	@RequestMapping("/removeWeekPlan")
	public byte[] removeWeekPlan(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		/**
		 * 角色HealthPhysician、Buyer和TenantAdmin可以删除计划数据
		 */
		if (loginContext.getRoles().contains("HealthPhysician") || loginContext.getRoles().contains("TenantAdmin")) {
			int year = RequestUtils.getInt(request, "year");
			int week = RequestUtils.getInt(request, "week");
			int dateAfter = DateUtils.getNowYearMonthDay();
			try {
				logger.debug("准备删除计划数据......");
				DietaryClearBean bean = new DietaryClearBean();
				int semester = SysConfig.getSemester();
				bean.removePlanData(loginContext.getTenantId(), year, semester, week, dateAfter);
				return ResponseUtils.responseResult(true);
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@ResponseBody
	@RequestMapping("/saveBatch")
	public byte[] saveBatch(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		/**
		 * 角色HealthPhysician和TenantAdmin可以增加食谱
		 */
		if (loginContext.isSystemAdministrator() || loginContext.getRoles().contains("HealthPhysician")
				|| loginContext.getRoles().contains("TenantAdmin")) {
			String dateString = request.getParameter("dateString");
			Date date = DateUtils.toDate(dateString);
			if (loginContext.getLimit() != Constants.UNLIMIT) {
				if (date == null || date.getTime() < System.currentTimeMillis()) {
					return ResponseUtils.responseJsonResult(false, "日期必须大于当前日期。");
				}
				if (date.getTime() > (System.currentTimeMillis() + DateUtils.DAY * 30)) {
					return ResponseUtils.responseJsonResult(false, "日期必须是一月以内的日期。");
				}
			}
			String objectIds = request.getParameter("objectIds");
			int week = RequestUtils.getInt(request, "week");
			try {
				if (StringUtils.isNotEmpty(objectIds)) {
					List<Long> templateIds = StringTools.splitToLong(objectIds);
					/**
					 * 将选择的模板复制一份作为新实例，以便修改其中的组成部分。
					 */
					dietaryService.copyTemplates(loginContext, date, week, templateIds);
					return ResponseUtils.responseJsonResult(true);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@ResponseBody
	@RequestMapping("/saveDietary")
	public byte[] saveDietary(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		/**
		 * 角色HealthPhysician和TenantAdmin可以增加食谱
		 */
		if (loginContext.getRoles().contains("HealthPhysician") || loginContext.getRoles().contains("TenantAdmin")) {
			String name = request.getParameter("name");
			if (StringUtils.isEmpty(name)) {
				return ResponseUtils.responseJsonResult(false, "名称必须输入。");
			}
			Map<String, Object> params = RequestUtils.getParameterMap(request);
			String actorId = loginContext.getActorId();
			long id = RequestUtils.getLong(request, "id");
			String dateString = request.getParameter("dateString");
			Date date = DateUtils.toDate(dateString);
			if (loginContext.getLimit() != Constants.UNLIMIT) {
				if (date == null || date.getTime() < System.currentTimeMillis()) {
					return ResponseUtils.responseJsonResult(false, "日期必须大于当前日期。");
				}
				if (date.getTime() > (System.currentTimeMillis() + DateUtils.DAY * 30)) {
					return ResponseUtils.responseJsonResult(false, "日期必须是一月以内的日期。");
				}
			}
			Dietary dietary = null;
			try {
				if (id > 0) {
					dietary = dietaryService.getDietary(loginContext.getTenantId(), id);
					if (dietary != null && StringUtils.equals(dietary.getPurchaseFlag(), "Y")) {
						return ResponseUtils.responseJsonResult(false, "食谱数据已经提交采购了，不能修改。");
					}
				}
				if (dietary == null) {
					dietary = new Dietary();
					Tools.populate(dietary, params);
					dietary.setTenantId(loginContext.getTenantId());
				} else {
					if (!StringUtils.equals(loginContext.getTenantId(), dietary.getTenantId())) {
						return ResponseUtils.responseJsonResult(false, "食谱数据只能所属机构修改。");
					}
					Tools.populate(dietary, params);
				}

				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH) + 1;
				int week = calendar.get(Calendar.WEEK_OF_MONTH);
				int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
				int day = calendar.get(Calendar.DAY_OF_MONTH);

				dietary.setName(request.getParameter("name"));
				dietary.setDescription(request.getParameter("description"));
				dietary.setType(request.getParameter("type"));
				dietary.setTypeId(RequestUtils.getLong(request, "typeId"));
				dietary.setSortNo(RequestUtils.getInt(request, "sortNo"));
				dietary.setSemester(SysConfig.getSemester());
				dietary.setCreateBy(actorId);
				dietary.setYear(year);
				dietary.setMonth(month);
				dietary.setWeek(week);
				dietary.setDay(day);
				dietary.setDayOfWeek(dayOfWeek);
				dietary.setFullDay(DateUtils.getYearMonthDay(date));

				this.dietaryService.save(dietary);

				return ResponseUtils.responseJsonResult(true);
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			}

		}
		return ResponseUtils.responseJsonResult(false);
	}

	@RequestMapping("/searchlist")
	public ModelAndView searchlist(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		RequestUtils.setRequestParameterToAttribute(request);

		// List<Dictory> dictoryList = dictoryService.getDictoryList(4501L);//
		// 4501是餐点分类编号
		List<Dictory> dictoryList = dictoryService.getDictoryListByCategory("CAT_MEAL");
		request.setAttribute("dictoryList", dictoryList);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;

		List<Integer> years = new ArrayList<Integer>();
		List<Integer> months = new ArrayList<Integer>();

		List<Integer> weeks = new ArrayList<Integer>();

		years.add(year);
		months.add(month);
		if (month == 12) {
			years.add(year + 1);
			months.add(1);
		} else {
			months.add(month + 1);
		}

		for (int i = 1; i <= 20; i++) {
			weeks.add(i);
		}

		request.setAttribute("years", years);
		request.setAttribute("months", months);
		request.setAttribute("weeks", weeks);
		request.setAttribute("year", year);
		request.setAttribute("month", month);

		int yearx = RequestUtils.getInt(request, "year");
		int weekx = RequestUtils.getInt(request, "week");
		int semester = SysConfig.getSemester();

		if (yearx > 0 && weekx > 0) {
			List<Integer> days = dietaryService.getDays(loginContext.getTenantId(), yearx, semester, weekx);
			List<BaseItem> items = new ArrayList<BaseItem>();
			for (int fullDay : days) {
				BaseItem item = new BaseItem();
				item.setName(DateUtils.getDate(DateUtils.toDate(String.valueOf(fullDay))));
				item.setValue(String.valueOf(fullDay));
				items.add(item);
			}
			request.setAttribute("items", items);
		}

		String nameLike = request.getParameter("nameLike");
		if (StringUtils.isNotEmpty(nameLike)) {
			nameLike = nameLike.trim();
			request.setAttribute("nameLike_enc", RequestUtils.encodeString(nameLike));
			request.setAttribute("nameLike_base64", Base64.encodeBytes(nameLike.getBytes()));
		}

		String wordLike = request.getParameter("wordLike");
		if (StringUtils.isNotEmpty(wordLike)) {
			wordLike = wordLike.trim();
			request.setAttribute("wordLike_enc", RequestUtils.encodeString(wordLike));
			request.setAttribute("wordLike_base64", Base64.encodeBytes(wordLike.getBytes()));
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/dietary/searchlist", modelMap);
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

	@RequestMapping("/showRemove")
	public ModelAndView showRemove(HttpServletRequest request, ModelMap modelMap) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;

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

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/dietary/showRemove", modelMap);
	}

}
