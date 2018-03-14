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
import com.glaf.base.modules.sys.model.SysTree;
import com.glaf.base.modules.sys.service.SysTreeService;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.identity.User;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.Constants;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.StringTools;
import com.glaf.core.util.Tools;

import com.glaf.heathcare.SysConfig;
import com.glaf.heathcare.bean.DayFoodActualQuantityStatisticsBean;
import com.glaf.heathcare.domain.ActualRepastPerson;
import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.domain.GoodsActualQuantity;
import com.glaf.heathcare.domain.GoodsOutStock;
import com.glaf.heathcare.domain.GoodsPlanQuantity;
import com.glaf.heathcare.query.ActualRepastPersonQuery;
import com.glaf.heathcare.query.GoodsActualQuantityQuery;
import com.glaf.heathcare.query.GoodsOutStockQuery;
import com.glaf.heathcare.query.GoodsPlanQuantityQuery;
import com.glaf.heathcare.service.ActualRepastPersonService;
import com.glaf.heathcare.service.DietaryService;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.heathcare.service.GoodsActualQuantityService;
import com.glaf.heathcare.service.GoodsOutStockService;
import com.glaf.heathcare.service.GoodsPlanQuantityService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/goodsActualQuantity")
@RequestMapping("/heathcare/goodsActualQuantity")
public class GoodsActualQuantityController {
	protected static final Log logger = LogFactory.getLog(GoodsActualQuantityController.class);

	protected DietaryService dietaryService;

	protected ActualRepastPersonService actualRepastPersonService;

	protected FoodCompositionService foodCompositionService;

	protected GoodsActualQuantityService goodsActualQuantityService;

	protected GoodsOutStockService goodsOutStockService;

	protected GoodsPlanQuantityService goodsPlanQuantityService;

	protected SysTreeService sysTreeService;

	public GoodsActualQuantityController() {

	}

	@ResponseBody
	@RequestMapping("/audit")
	public byte[] audit(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.getRoles().contains("TenantAdmin") || loginContext.getRoles().contains("Kitchen")) {
			long id = RequestUtils.getLong(request, "id");
			String ids = request.getParameter("ids");
			GoodsActualQuantity goodsActualQuantity = null;
			try {
				if (id > 0) {
					goodsActualQuantity = goodsActualQuantityService.getGoodsActualQuantity(loginContext.getTenantId(),
							id);
				}
				if (goodsActualQuantity != null) {
					if (!StringUtils.equals(loginContext.getTenantId(), goodsActualQuantity.getTenantId())) {
						return ResponseUtils.responseJsonResult(false, "数据只能所属机构修改。");
					}
					goodsActualQuantity.setBusinessStatus(9);
					goodsActualQuantity.setConfirmBy(loginContext.getActorId());
					goodsActualQuantity.setConfirmTime(new Date());
					this.goodsActualQuantityService.updateGoodsActualQuantityStatus(goodsActualQuantity);
					return ResponseUtils.responseJsonResult(true);
				}
				if (StringUtils.isNotEmpty(ids)) {
					List<Long> pIds = StringTools.splitToLong(ids);
					GoodsActualQuantityQuery query = new GoodsActualQuantityQuery();
					query.tenantId(loginContext.getTenantId());
					query.ids(pIds);
					List<GoodsActualQuantity> list = goodsActualQuantityService.list(query);
					if (list != null && !list.isEmpty()) {
						for (GoodsActualQuantity p : list) {
							if (!StringUtils.equals(loginContext.getTenantId(), p.getTenantId())) {
								return ResponseUtils.responseJsonResult(false, "数据只能所属机构修改。");
							}
							p.setBusinessStatus(9);
							p.setConfirmBy(loginContext.getActorId());
							p.setConfirmTime(new Date());
						}
						goodsActualQuantityService.updateGoodsActualQuantityStatus(list);
						return ResponseUtils.responseJsonResult(true);
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@RequestMapping("/batchEdit")
	public ModelAndView batchEdit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		// Date date = RequestUtils.getDate(request, "date");
		Date date = new Date();
		GoodsPlanQuantityQuery query = new GoodsPlanQuantityQuery();
		query.tenantId(loginContext.getTenantId());
		query.fullDay(DateUtils.getYearMonthDay(date));

		GoodsActualQuantityQuery query2 = new GoodsActualQuantityQuery();
		query2.tenantId(loginContext.getTenantId());
		query2.fullDay(DateUtils.getYearMonthDay(date));
		query2.businessStatus(0);

		List<GoodsActualQuantity> goodsList = goodsActualQuantityService.list(query2);
		Map<Long, Double> qtyMap = new HashMap<Long, Double>();
		if (goodsList != null && !goodsList.isEmpty()) {
			for (GoodsActualQuantity model : goodsList) {
				qtyMap.put(model.getGoodsId(), model.getQuantity());
			}
		}

		GoodsOutStockQuery query4 = new GoodsOutStockQuery();
		query4.tenantId(loginContext.getTenantId());
		query4.fullDay(DateUtils.getYearMonthDay(date));
		query4.businessStatus(9);// 已经审核过的量
		List<GoodsOutStock> outGoodsList = goodsOutStockService.list(query4);

		Map<Long, Double> qtyMap2 = new HashMap<Long, Double>();
		if (outGoodsList != null && !outGoodsList.isEmpty()) {
			for (GoodsOutStock model : outGoodsList) {
				qtyMap2.put(model.getGoodsId(), model.getQuantity());
			}
		}

		List<GoodsPlanQuantity> planList = goodsPlanQuantityService.list(query);
		if (planList != null && !planList.isEmpty()) {
			for (GoodsPlanQuantity model : planList) {
				if (qtyMap.get(model.getGoodsId()) != null) {
					model.setRealQuantity(qtyMap.get(model.getGoodsId()));
				}
				if (qtyMap2.get(model.getGoodsId()) != null) {
					model.setOutStockQuantity(qtyMap2.get(model.getGoodsId()));
				}
			}
			request.setAttribute("planList", planList);
			logger.debug("plan list size:" + planList.size());
		}

		List<Integer> weeks = new ArrayList<Integer>();
		for (int i = 1; i <= 20; i++) {
			weeks.add(i);
		}
		request.setAttribute("weeks", weeks);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR);
		// int maxWeek = dietaryService.getMaxWeek(loginContext.getTenantId(),
		// year, SysConfig.getSemester());
		// request.setAttribute("maxWeek", maxWeek);
		int nowWeek = dietaryService.getNowWeek(loginContext.getTenantId(), year, SysConfig.getSemester());
		request.setAttribute("nowWeek", nowWeek);
		request.setAttribute("now", new Date());

		SysTree root = sysTreeService.getSysTreeByCode("FoodCategory");
		if (root != null) {
			List<SysTree> foodCategories = sysTreeService.getSysTreeList(root.getId());
			request.setAttribute("foodCategories", foodCategories);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("goodsActualQuantity.batchEdit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/goodsActualQuantity/batchEdit", modelMap);
	}

	@ResponseBody
	@RequestMapping("/copyOutStock")
	public byte[] copyOutStock(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		Date dateTime = RequestUtils.getDate(request, "dateTime");
		if (loginContext.getLimit() != Constants.UNLIMIT) {
			if (dateTime == null || DateUtils.afterTime(dateTime, new Date())) {
				return ResponseUtils.responseJsonResult(false, "日期必须是当天或之前的日期。");
			}
		}
		int fullDay = DateUtils.getYearMonthDay(dateTime);
		boolean includePlan = RequestUtils.getBoolean(request, "includePlan");
		try {
			goodsActualQuantityService.copyOutStock(loginContext.getTenantId(), fullDay, actorId, includePlan);
			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@ResponseBody
	@RequestMapping("/copyPurchase")
	public byte[] copyPurchase(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		Date dateTime = RequestUtils.getDate(request, "dateTime");
		if (loginContext.getLimit() != Constants.UNLIMIT) {
			if (dateTime == null || DateUtils.afterTime(dateTime, new Date())) {
				return ResponseUtils.responseJsonResult(false, "日期必须是当天或之前的日期。");
			}
		}
		int fullDay = DateUtils.getYearMonthDay(dateTime);
		boolean includePlan = RequestUtils.getBoolean(request, "includePlan");
		try {
			goodsActualQuantityService.copyPurchase(loginContext.getTenantId(), fullDay, actorId, includePlan);
			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
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
					GoodsActualQuantity goodsActualQuantity = goodsActualQuantityService
							.getGoodsActualQuantity(loginContext.getTenantId(), Long.valueOf(x));
					if (goodsActualQuantity != null && ((loginContext.getRoles().contains("Kitchen")
							|| loginContext.getRoles().contains("TenantAdmin"))
							&& StringUtils.equals(goodsActualQuantity.getTenantId(), loginContext.getTenantId()))) {
						if (goodsActualQuantity.getBusinessStatus() == 0) {
							goodsActualQuantityService.deleteById(loginContext.getTenantId(),
									goodsActualQuantity.getId());
						}
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			GoodsActualQuantity goodsActualQuantity = goodsActualQuantityService
					.getGoodsActualQuantity(loginContext.getTenantId(), Long.valueOf(id));
			if (goodsActualQuantity != null
					&& ((loginContext.getRoles().contains("Kitchen") || loginContext.getRoles().contains("TenantAdmin"))
							&& StringUtils.equals(goodsActualQuantity.getTenantId(), loginContext.getTenantId()))) {
				if (goodsActualQuantity.getBusinessStatus() == 0) {
					goodsActualQuantityService.deleteById(loginContext.getTenantId(), goodsActualQuantity.getId());
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
		request.setAttribute("usageTime", DateUtils.getDate(new Date()));
		int nowWeek = 0;
		GoodsActualQuantity goodsActualQuantity = goodsActualQuantityService
				.getGoodsActualQuantity(loginContext.getTenantId(), RequestUtils.getLong(request, "id"));
		if (goodsActualQuantity != null) {
			request.setAttribute("goodsActualQuantity", goodsActualQuantity);
			if (goodsActualQuantity.getUsageTime() != null) {
				request.setAttribute("usageTime", DateUtils.getDate(goodsActualQuantity.getUsageTime()));
			}
			nowWeek = goodsActualQuantity.getWeek();
			if (goodsActualQuantity.getGoodsId() > 0) {
				FoodComposition foodComposition = foodCompositionService
						.getFoodComposition(goodsActualQuantity.getGoodsId());
				if (foodComposition != null) {
					List<FoodComposition> foods = foodCompositionService
							.getFoodCompositions(foodComposition.getNodeId());
					request.setAttribute("foods", foods);
					request.setAttribute("nodeId", foodComposition.getNodeId());
				}
			}
		}

		SysTree root = sysTreeService.getSysTreeByCode("FoodCategory");
		if (root != null) {
			List<SysTree> foodCategories = sysTreeService.getSysTreeList(root.getId());
			request.setAttribute("foodCategories", foodCategories);
		}

		List<Integer> weeks = new ArrayList<Integer>();
		for (int i = 1; i <= 20; i++) {
			weeks.add(i);
		}
		request.setAttribute("weeks", weeks);
		request.setAttribute("now", new Date());

		if (nowWeek == 0) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			int year = calendar.get(Calendar.YEAR);
			nowWeek = dietaryService.getNowWeek(loginContext.getTenantId(), year, SysConfig.getSemester());
		}

		request.setAttribute("nowWeek", nowWeek);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("goodsActualQuantity.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/goodsActualQuantity/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug("params:" + params);
		JSONObject result = new JSONObject();

		GoodsActualQuantityQuery query = new GoodsActualQuantityQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		if (!loginContext.isSystemAdministrator()) {
			if (loginContext.getRoles().contains("TenantAdmin") || loginContext.getRoles().contains("HealthPhysician")
					|| loginContext.getRoles().contains("Buyer") || loginContext.getRoles().contains("Kitchen")) {
				query.tenantId(loginContext.getTenantId());
			} else {
				return result.toJSONString().getBytes("UTF-8");
			}
		}

		String avgQuantity = request.getParameter("avgQuantity");
		ActualRepastPersonQuery query2 = new ActualRepastPersonQuery();

		Date usageTime = RequestUtils.getDate(request, "usageTime");
		if (usageTime != null) {
			query.fullDay(DateUtils.getYearMonthDay(usageTime));
			query2.fullDay(DateUtils.getYearMonthDay(usageTime));
		}

		Date startTime = RequestUtils.getDate(request, "startTime");
		if (startTime != null) {
			query.usageTimeGreaterThanOrEqual(startTime);
			query2.fullDayGreaterThanOrEqual(DateUtils.getYearMonthDay(startTime));
		}

		Date endTime = RequestUtils.getEndDate(request, "endTime");
		if (endTime != null) {
			query.usageTimeLessThanOrEqual(endTime);
			query2.fullDayLessThanOrEqual(DateUtils.getYearMonthDay(endTime));
		}

		String showToday = request.getParameter("showToday");
		if (StringUtils.isNotEmpty(showToday)) {
			query.fullDay(DateUtils.getNowYearMonthDay());
			query2.fullDay(DateUtils.getNowYearMonthDay());
		}

		int start = 0;
		int limit = 50;
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
			limit = 50;
		}

		int total = goodsActualQuantityService.getGoodsActualQuantityCountByQueryCriteria(query);
		if (total > 0) {
			Map<Integer, Integer> personCntMap = new HashMap<Integer, Integer>();
			if (StringUtils.equals(avgQuantity, "on")) {
				List<ActualRepastPerson> rows = actualRepastPersonService.list(query2);
				if (rows != null && !rows.isEmpty()) {
					for (ActualRepastPerson p : rows) {
						Integer cnt = personCntMap.get(p.getFullDay());
						if (cnt == null) {
							cnt = new Integer(0);
						}
						cnt = cnt + p.getFemale() + p.getMale();
						personCntMap.put(p.getFullDay(), cnt);
					}
				}
			}

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

			List<GoodsActualQuantity> list = goodsActualQuantityService.getGoodsActualQuantitysByQueryCriteria(start,
					limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				Map<String, User> userMap = IdentityFactory.getUserMap(loginContext.getTenantId());

				for (GoodsActualQuantity goodsActualQuantity : list) {
					JSONObject rowJSON = goodsActualQuantity.toJsonObject();
					rowJSON.put("id", goodsActualQuantity.getId());
					rowJSON.put("rowId", goodsActualQuantity.getId());
					rowJSON.put("quantity", goodsActualQuantity.getQuantity() + "KG");
					rowJSON.put("goodsActualQuantityId", goodsActualQuantity.getId());
					rowJSON.put("startIndex", ++start);
					User user = userMap.get(goodsActualQuantity.getConfirmBy());
					if (user != null) {
						rowJSON.put("confirmName", user.getName());
					}
					if (StringUtils.equals(avgQuantity, "on")) {
						Integer cnt = personCntMap.get(goodsActualQuantity.getFullDay());
						if (cnt != null && cnt.intValue() > 0) {
							double avg = goodsActualQuantity.getQuantity() * 1000 / cnt;
							avg = Math.round(avg * 10D) / 10D;
							rowJSON.put("quantity", avg + "g");
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

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/goodsActualQuantity/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("goodsActualQuantity.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/heathcare/goodsActualQuantity/query", modelMap);
	}

	@RequestMapping("report")
	public ModelAndView report(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/goodsActualQuantity/report", modelMap);
	}

	@RequestMapping("/reviewJson")
	@ResponseBody
	public byte[] reviewJson(HttpServletRequest request, ModelMap modelMap) throws IOException {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug("params:" + params);
		JSONObject result = new JSONObject();

		GoodsActualQuantityQuery query = new GoodsActualQuantityQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);

		String tenantId = request.getParameter("tenantId");
		query.tenantId(tenantId);

		String avgQuantity = request.getParameter("avgQuantity");
		ActualRepastPersonQuery query2 = new ActualRepastPersonQuery();

		Date usageTime = RequestUtils.getDate(request, "usageTime");
		if (usageTime != null) {
			query.fullDay(DateUtils.getYearMonthDay(usageTime));
			query2.fullDay(DateUtils.getYearMonthDay(usageTime));
		}

		Date startTime = RequestUtils.getDate(request, "startTime");
		if (startTime != null) {
			query.usageTimeGreaterThanOrEqual(startTime);
			query2.fullDayGreaterThanOrEqual(DateUtils.getYearMonthDay(startTime));
		}

		Date endTime = RequestUtils.getEndDate(request, "endTime");
		if (endTime != null) {
			query.usageTimeLessThanOrEqual(endTime);
			query2.fullDayLessThanOrEqual(DateUtils.getYearMonthDay(endTime));
		}

		String showToday = request.getParameter("showToday");
		if (StringUtils.isNotEmpty(showToday)) {
			query.fullDay(DateUtils.getNowYearMonthDay());
			query2.fullDay(DateUtils.getNowYearMonthDay());
		}

		int start = 0;
		int limit = 50;
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
			limit = 50;
		}

		int total = goodsActualQuantityService.getGoodsActualQuantityCountByQueryCriteria(query);
		if (total > 0) {
			Map<Integer, Integer> personCntMap = new HashMap<Integer, Integer>();
			if (StringUtils.equals(avgQuantity, "on")) {
				List<ActualRepastPerson> rows = actualRepastPersonService.list(query2);
				if (rows != null && !rows.isEmpty()) {
					for (ActualRepastPerson p : rows) {
						Integer cnt = personCntMap.get(p.getFullDay());
						if (cnt == null) {
							cnt = new Integer(0);
						}
						cnt = cnt + p.getFemale() + p.getMale();
						personCntMap.put(p.getFullDay(), cnt);
					}
				}
			}

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

			List<GoodsActualQuantity> list = goodsActualQuantityService.getGoodsActualQuantitysByQueryCriteria(start,
					limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				Map<String, User> userMap = IdentityFactory.getUserMap(tenantId);

				for (GoodsActualQuantity goodsActualQuantity : list) {
					JSONObject rowJSON = goodsActualQuantity.toJsonObject();
					rowJSON.put("id", goodsActualQuantity.getId());
					rowJSON.put("rowId", goodsActualQuantity.getId());
					rowJSON.put("quantity", goodsActualQuantity.getQuantity() + "KG");
					rowJSON.put("goodsActualQuantityId", goodsActualQuantity.getId());
					rowJSON.put("startIndex", ++start);
					User user = userMap.get(goodsActualQuantity.getConfirmBy());
					if (user != null) {
						rowJSON.put("confirmName", user.getName());
					}
					if (StringUtils.equals(avgQuantity, "on")) {
						Integer cnt = personCntMap.get(goodsActualQuantity.getFullDay());
						if (cnt != null && cnt.intValue() > 0) {
							double avg = goodsActualQuantity.getQuantity() * 1000 / cnt;
							avg = Math.round(avg);
							rowJSON.put("quantity", avg + "g");
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

	@RequestMapping("/reviewlist")
	public ModelAndView reviewlist(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/goodsActualQuantity/review_list", modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveBatch")
	public byte[] saveBatch(HttpServletRequest request) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		int week = RequestUtils.getInt(request, "week");
		// Date date = RequestUtils.getDate(request, "date");

		Date date = new Date();
		GoodsPlanQuantityQuery query = new GoodsPlanQuantityQuery();
		query.tenantId(loginContext.getTenantId());
		query.fullDay(DateUtils.getYearMonthDay(date));

		GoodsActualQuantityQuery query2 = new GoodsActualQuantityQuery();
		query2.tenantId(loginContext.getTenantId());
		query2.fullDay(DateUtils.getYearMonthDay(date));
		query2.businessStatus(0);

		List<GoodsActualQuantity> distList = new ArrayList<GoodsActualQuantity>();

		List<GoodsActualQuantity> goodsList = goodsActualQuantityService.list(query2);
		Map<Long, Double> qtyMap = new HashMap<Long, Double>();
		if (goodsList != null && !goodsList.isEmpty()) {
			for (GoodsActualQuantity model : goodsList) {
				double quantity = RequestUtils.getDouble(request, "qty_" + model.getGoodsId());
				double price = RequestUtils.getDouble(request, "price_" + model.getGoodsId());
				double totalPrice = RequestUtils.getDouble(request, "totalPrice_" + model.getGoodsId());
				model.setQuantity(quantity);
				model.setPrice(price);
				model.setTotalPrice(totalPrice);
				qtyMap.put(model.getGoodsId(), quantity);
				distList.add(model);
			}
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		List<GoodsPlanQuantity> planList = goodsPlanQuantityService.list(query);
		if (planList != null && !planList.isEmpty()) {
			// int week = dietaryService.getMaxWeek(loginContext.getTenantId(),
			// year, SysConfig.getSemester());
			for (GoodsPlanQuantity model : planList) {
				if (qtyMap.get(model.getGoodsId()) == null) {
					double qty = RequestUtils.getDouble(request, "qty_" + model.getGoodsId());
					if (qty > 0) {
						GoodsActualQuantity m = new GoodsActualQuantity();
						m.setBusinessStatus(0);
						m.setCreateBy(loginContext.getActorId());
						m.setGoodsId(model.getGoodsId());
						m.setGoodsName(model.getGoodsName());
						m.setGoodsNodeId(model.getGoodsNodeId());
						m.setUsageTime(date);
						m.setTenantId(loginContext.getTenantId());
						m.setYear(year);
						m.setMonth(month);
						m.setDay(day);
						m.setWeek(week);
						m.setSemester(SysConfig.getSemester());
						m.setFullDay(DateUtils.getYearMonthDay(date));
						m.setQuantity(qty);
						distList.add(m);
					}
				}
			}
			goodsActualQuantityService.saveAll(distList);
			return ResponseUtils.responseJsonResult(true);
		}

		return ResponseUtils.responseJsonResult(false);
	}

	@ResponseBody
	@RequestMapping("/saveGoodsActualQuantity")
	public byte[] saveGoodsActualQuantity(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		if (loginContext.getRoles().contains("TenantAdmin") || loginContext.getRoles().contains("Kitchen")) {
			long id = RequestUtils.getLong(request, "id");
			GoodsActualQuantity goodsActualQuantity = null;
			try {
				if (id > 0) {
					goodsActualQuantity = goodsActualQuantityService.getGoodsActualQuantity(loginContext.getTenantId(),
							id);
				}
				if (goodsActualQuantity == null) {
					goodsActualQuantity = new GoodsActualQuantity();
					goodsActualQuantity.setTenantId(loginContext.getTenantId());
				} else {
					if (!StringUtils.equals(loginContext.getTenantId(), goodsActualQuantity.getTenantId())) {
						return ResponseUtils.responseJsonResult(false, "数据只能所属机构修改。");
					}
					if (loginContext.getLimit() != Constants.UNLIMIT) {
						if (goodsActualQuantity.getFullDay() != DateUtils.getNowYearMonthDay()) {
							return ResponseUtils.responseJsonResult(false, "实际使用量只能当天修改。");
						}
					}
				}

				Tools.populate(goodsActualQuantity, params);

				Date usageTime = RequestUtils.getDate(request, "usageTime");
				if (usageTime == null) {
					usageTime = new Date();
				}
				goodsActualQuantity.setGoodsId(RequestUtils.getLong(request, "goodsId"));
				goodsActualQuantity.setQuantity(RequestUtils.getDouble(request, "quantity"));
				goodsActualQuantity.setPrice(RequestUtils.getDouble(request, "price"));
				goodsActualQuantity.setTotalPrice(RequestUtils.getDouble(request, "totalPrice"));
				goodsActualQuantity.setUnit(request.getParameter("unit"));
				goodsActualQuantity.setBusinessStatus(0);
				goodsActualQuantity.setCreateBy(actorId);
				goodsActualQuantity.setUsageTime(usageTime);
				goodsActualQuantity.setSemester(SysConfig.getSemester());
				goodsActualQuantity.setFullDay(DateUtils.getYearMonthDay(usageTime));
				goodsActualQuantity.setWeek(RequestUtils.getInt(request, "week"));

				Calendar calendar = Calendar.getInstance();
				calendar.setTime(goodsActualQuantity.getUsageTime());
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH) + 1;
				int day = calendar.get(Calendar.DAY_OF_MONTH);
				goodsActualQuantity.setYear(year);
				goodsActualQuantity.setMonth(month);
				goodsActualQuantity.setDay(day);

				if (goodsActualQuantity.getGoodsId() > 0) {
					FoodComposition fd = foodCompositionService.getFoodComposition(goodsActualQuantity.getGoodsId());
					goodsActualQuantity.setGoodsName(fd.getName());
					goodsActualQuantity.setGoodsNodeId(fd.getNodeId());
				}

				this.goodsActualQuantityService.save(goodsActualQuantity);

				return ResponseUtils.responseJsonResult(true);
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.actualRepastPersonService")
	public void setActualRepastPersonService(ActualRepastPersonService actualRepastPersonService) {
		this.actualRepastPersonService = actualRepastPersonService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.dietaryService")
	public void setDietaryService(DietaryService dietaryService) {
		this.dietaryService = dietaryService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.foodCompositionService")
	public void setFoodCompositionService(FoodCompositionService foodCompositionService) {
		this.foodCompositionService = foodCompositionService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.goodsActualQuantityService")
	public void setGoodsActualQuantityService(GoodsActualQuantityService goodsActualQuantityService) {
		this.goodsActualQuantityService = goodsActualQuantityService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.goodsOutStockService")
	public void setGoodsOutStockService(GoodsOutStockService goodsOutStockService) {
		this.goodsOutStockService = goodsOutStockService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.goodsPlanQuantityService")
	public void setGoodsPlanQuantityService(GoodsPlanQuantityService goodsPlanQuantityService) {
		this.goodsPlanQuantityService = goodsPlanQuantityService;
	}

	@javax.annotation.Resource
	public void setSysTreeService(SysTreeService sysTreeService) {
		this.sysTreeService = sysTreeService;
	}

	@RequestMapping("/showCopy")
	public ModelAndView showCopy(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("goodsActualQuantity.copy");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/goodsActualQuantity/copy", modelMap);
	}

	@RequestMapping("/showCopy2")
	public ModelAndView showCopy2(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("goodsActualQuantity.copy2");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/goodsActualQuantity/copy2", modelMap);
	}

	@RequestMapping("/showDayExport")
	public ModelAndView showDayExport(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		request.setAttribute("tenantId", loginContext.getTenantId());
		String tenantId = loginContext.getTenantId();
		if (StringUtils.isNotEmpty(request.getParameter("tenantId"))) {
			tenantId = request.getParameter("tenantId");
		}
		int fullDay = RequestUtils.getInt(request, "fullDay");
		if (fullDay > 0) {
			DayFoodActualQuantityStatisticsBean bean = new DayFoodActualQuantityStatisticsBean();
			bean.execute(tenantId, fullDay);
		}

		Date date = RequestUtils.getDate(request, "date");
		if (date != null) {
			logger.debug("---------------execute---------------------------");
			DayFoodActualQuantityStatisticsBean bean = new DayFoodActualQuantityStatisticsBean();
			bean.execute(tenantId, DateUtils.getYearMonthDay(date));
			fullDay = DateUtils.getYearMonthDay(date);
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(DateUtils.toDate(String.valueOf(fullDay)));

		int suitNo = fullDay;
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		request.setAttribute("suitNo", suitNo);
		request.setAttribute("dayOfWeek", dayOfWeek);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view);
		}

		String x_view = ViewProperties.getString("goodsActualQuantity.showDayExport");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view);
		}

		return new ModelAndView("/heathcare/goodsActualQuantity/showDayExport");
	}

	@RequestMapping("/showExport")
	public ModelAndView showExport(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		List<Integer> weeks = new ArrayList<Integer>();
		for (int i = 1; i <= 20; i++) {
			weeks.add(i);
		}
		request.setAttribute("weeks", weeks);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view);
		}

		String x_view = ViewProperties.getString("goodsActualQuantity.showExport");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view);
		}

		return new ModelAndView("/heathcare/goodsActualQuantity/showExport");
	}

	@RequestMapping("/showSectionExport")
	public ModelAndView showSectionExport(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String tenantId = loginContext.getTenantId();
		if (StringUtils.isNotEmpty(request.getParameter("tenantId"))) {
			tenantId = request.getParameter("tenantId");
		}

		Date startDate = RequestUtils.getDate(request, "startDate");
		Date endDate = RequestUtils.getDate(request, "endDate");
		if (startDate != null && endDate != null) {
			logger.debug("---------------execute2---------------------------");
			String batchNo = tenantId + "_" + loginContext.getActorId() + "_aq10000";
			DayFoodActualQuantityStatisticsBean bean = new DayFoodActualQuantityStatisticsBean();
			bean.execute(tenantId, batchNo, startDate, endDate);
			request.setAttribute("batchNo", batchNo);
		}

		String sysFlag = "N";
		int suitNo = 10000;
		int dayOfWeek = 0;
		request.setAttribute("suitNo", suitNo);
		request.setAttribute("sysFlag", sysFlag);
		request.setAttribute("dayOfWeek", dayOfWeek);
		request.setAttribute("tenantId", tenantId);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view);
		}

		String x_view = ViewProperties.getString("goodsActualQuantity.showSectionExport");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view);
		}

		return new ModelAndView("/heathcare/goodsActualQuantity/showSectionExport");
	}

	@RequestMapping("/view")
	public ModelAndView view(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		GoodsActualQuantity goodsActualQuantity = goodsActualQuantityService
				.getGoodsActualQuantity(loginContext.getTenantId(), RequestUtils.getLong(request, "id"));
		request.setAttribute("goodsActualQuantity", goodsActualQuantity);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view);
		}

		String x_view = ViewProperties.getString("goodsActualQuantity.view");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view);
		}

		return new ModelAndView("/heathcare/goodsActualQuantity/view");
	}

}
