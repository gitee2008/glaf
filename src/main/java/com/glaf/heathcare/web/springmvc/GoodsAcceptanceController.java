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
import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.domain.GoodsAcceptance;
import com.glaf.heathcare.domain.GoodsPurchase;
import com.glaf.heathcare.query.GoodsAcceptanceQuery;
import com.glaf.heathcare.query.GoodsPurchaseQuery;
import com.glaf.heathcare.service.DietaryService;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.heathcare.service.GoodsAcceptanceService;
import com.glaf.heathcare.service.GoodsPlanQuantityService;
import com.glaf.heathcare.service.GoodsPurchaseService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/goodsAcceptance")
@RequestMapping("/heathcare/goodsAcceptance")
public class GoodsAcceptanceController {
	protected static final Log logger = LogFactory.getLog(GoodsAcceptanceController.class);

	protected DietaryService dietaryService;

	protected FoodCompositionService foodCompositionService;

	protected GoodsAcceptanceService goodsAcceptanceService;

	protected GoodsPlanQuantityService goodsPlanQuantityService;

	protected GoodsPurchaseService goodsPurchaseService;

	protected SysTreeService sysTreeService;

	public GoodsAcceptanceController() {

	}

	@ResponseBody
	@RequestMapping("/audit")
	public byte[] audit(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		if (loginContext.isSystemAdministrator() || loginContext.getRoles().contains("TenantAdmin")
				|| loginContext.getRoles().contains("StockManager")) {
			long id = RequestUtils.getLong(request, "id");
			String ids = request.getParameter("ids");
			GoodsAcceptance goodsAcceptance = null;
			try {
				if (StringUtils.isNotEmpty(ids)) {
					List<Long> pIds = StringTools.splitToLong(ids);
					for (Long pId : pIds) {
						goodsAcceptance = goodsAcceptanceService.getGoodsAcceptance(loginContext.getTenantId(), pId);
						if (goodsAcceptance != null) {
							if (!StringUtils.equals(loginContext.getTenantId(), goodsAcceptance.getTenantId())) {
								return ResponseUtils.responseJsonResult(false, "验收数据只能所属机构修改。");
							}

							GoodsPurchaseQuery query1 = new GoodsPurchaseQuery();
							query1.tenantId(loginContext.getTenantId());
							query1.setGoodsId(goodsAcceptance.getGoodsId());
							query1.businessStatus(9);// 已经确定的物品
							List<GoodsPurchase> goods1 = goodsPurchaseService.list(query1);

							GoodsAcceptanceQuery query2 = new GoodsAcceptanceQuery();
							query2.tenantId(loginContext.getTenantId());
							query2.setGoodsId(goodsAcceptance.getGoodsId());
							query2.businessStatus(9);// 已经确定验收的物品
							List<GoodsAcceptance> goods2 = goodsAcceptanceService.list(query2);

							if (goods1 != null && !goods1.isEmpty()) {
								double existsQty = 0.0;
								for (GoodsPurchase model : goods1) {
									existsQty = existsQty + model.getQuantity();
								}
								if (goods2 != null && !goods2.isEmpty()) {
									for (GoodsAcceptance model : goods2) {
										existsQty = existsQty - model.getQuantity();
									}
								}
								if (existsQty > 0 && existsQty >= goodsAcceptance.getQuantity()) {
									goodsAcceptance.setBusinessStatus(9);
									goodsAcceptance.setConfirmBy(actorId);
									goodsAcceptance.setConfirmTime(new Date());
									this.goodsAcceptanceService.updateGoodsAcceptanceStatus(goodsAcceptance);
								} else {
									return ResponseUtils.responseJsonResult(false,
											goodsAcceptance.getGoodsName() + "验收申请数量" + goodsAcceptance.getQuantity()
													+ "已经超过采购数量" + existsQty + "，不能验收。");
								}
							}
						}
					}
					return ResponseUtils.responseJsonResult(true);
				}
				if (id > 0) {
					goodsAcceptance = goodsAcceptanceService.getGoodsAcceptance(loginContext.getTenantId(), id);
				}
				if (goodsAcceptance != null) {
					if (!StringUtils.equals(loginContext.getTenantId(), goodsAcceptance.getTenantId())) {
						return ResponseUtils.responseJsonResult(false, "验收数据只能所属机构修改。");
					}
					GoodsPurchaseQuery query1 = new GoodsPurchaseQuery();
					query1.tenantId(loginContext.getTenantId());
					query1.setGoodsId(goodsAcceptance.getGoodsId());
					query1.businessStatus(9);// 已经确定的物品
					List<GoodsPurchase> goods1 = goodsPurchaseService.list(query1);

					GoodsAcceptanceQuery query2 = new GoodsAcceptanceQuery();
					query2.tenantId(loginContext.getTenantId());
					query2.setGoodsId(goodsAcceptance.getGoodsId());
					query2.businessStatus(9);// 已经确定验收的物品
					List<GoodsAcceptance> goods2 = goodsAcceptanceService.list(query2);

					if (goods1 != null && !goods1.isEmpty()) {
						double existsQty = 0.0;
						for (GoodsPurchase model : goods1) {
							existsQty = existsQty + model.getQuantity();
						}
						if (goods2 != null && !goods2.isEmpty()) {
							for (GoodsAcceptance model : goods2) {
								existsQty = existsQty - model.getQuantity();
							}
						}
						if (existsQty > 0 && existsQty >= goodsAcceptance.getQuantity()) {
							goodsAcceptance.setBusinessStatus(9);
							goodsAcceptance.setConfirmBy(actorId);
							goodsAcceptance.setConfirmTime(new Date());
							this.goodsAcceptanceService.updateGoodsAcceptanceStatus(goodsAcceptance);
							return ResponseUtils.responseJsonResult(true);
						} else {
							return ResponseUtils.responseJsonResult(false,
									"验收申请数量" + goodsAcceptance.getQuantity() + "已经超过采购数量" + existsQty + "，不能验收。");
						}
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
		GoodsPurchaseQuery query = new GoodsPurchaseQuery();
		query.tenantId(loginContext.getTenantId());
		query.fullDay(DateUtils.getYearMonthDay(date));
		query.businessStatus(9);
		List<GoodsPurchase> purchaseList = goodsPurchaseService.list(query);

		GoodsAcceptanceQuery query2 = new GoodsAcceptanceQuery();
		query2.tenantId(loginContext.getTenantId());
		query2.fullDay(DateUtils.getYearMonthDay(date));
		List<GoodsAcceptance> goodsList = goodsAcceptanceService.list(query2);

		Map<Long, Double> qtyMap = new HashMap<Long, Double>();
		if (goodsList != null && !goodsList.isEmpty()) {
			for (GoodsAcceptance model : goodsList) {
				qtyMap.put(model.getGoodsId(), model.getQuantity());
			}
		}

		if (purchaseList != null && !purchaseList.isEmpty()) {
			for (GoodsPurchase model : purchaseList) {
				if (qtyMap.get(model.getGoodsId()) != null) {
					model.setQuantity2(qtyMap.get(model.getGoodsId()));
				}
			}
			request.setAttribute("purchaseList", purchaseList);
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR);
		int nowWeek = dietaryService.getNowWeek(loginContext.getTenantId(), year, SysConfig.getSemester());
		request.setAttribute("nowWeek", nowWeek);
		request.setAttribute("now", new Date());

		List<Integer> weeks = new ArrayList<Integer>();
		for (int i = 1; i <= 20; i++) {
			weeks.add(i);
		}
		request.setAttribute("weeks", weeks);

		SysTree root = sysTreeService.getSysTreeByCode("FoodCategory");
		if (root != null) {
			List<SysTree> foodCategories = sysTreeService.getSysTreeList(root.getId());
			request.setAttribute("foodCategories", foodCategories);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("goodsAcceptance.batchEdit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/goodsAcceptance/batchEdit", modelMap);
	}

	@ResponseBody
	@RequestMapping("/copyPurchase")
	public byte[] copyPurchase(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		Date dateTime = RequestUtils.getDate(request, "dateTime");
		if (loginContext.getLimit() != Constants.UNLIMIT) {
			if (dateTime == null || DateUtils.afterTime(dateTime, new Date())) {
				return ResponseUtils.responseJsonResult(false, "采购日期必须是当天或之前的日期。");
			}
		}
		int fullDay = DateUtils.getYearMonthDay(dateTime);
		try {
			goodsAcceptanceService.copyPurchase(loginContext.getTenantId(), fullDay, actorId);
			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			ex.printStackTrace();
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
					GoodsAcceptance goodsAcceptance = goodsAcceptanceService
							.getGoodsAcceptance(loginContext.getTenantId(), Long.valueOf(x));
					if (goodsAcceptance != null && ((loginContext.getRoles().contains("TenantAdmin")
							|| loginContext.getRoles().contains("Buyer")
							|| loginContext.getRoles().contains("StockManager"))
							&& StringUtils.equals(loginContext.getTenantId(), goodsAcceptance.getTenantId()))) {
						if (goodsAcceptance.getBusinessStatus() == 0) {
							goodsAcceptanceService.deleteById(loginContext.getTenantId(), goodsAcceptance.getId());
						}
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			GoodsAcceptance goodsAcceptance = goodsAcceptanceService.getGoodsAcceptance(loginContext.getTenantId(),
					Long.valueOf(id));
			if (goodsAcceptance != null && ((loginContext.getRoles().contains("TenantAdmin")
					|| loginContext.getRoles().contains("Buyer") || loginContext.getRoles().contains("StockManager"))
					&& StringUtils.equals(loginContext.getTenantId(), goodsAcceptance.getTenantId()))) {
				if (goodsAcceptance.getBusinessStatus() == 0) {
					goodsAcceptanceService.deleteById(loginContext.getTenantId(), goodsAcceptance.getId());
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
		GoodsAcceptance goodsAcceptance = goodsAcceptanceService.getGoodsAcceptance(loginContext.getTenantId(),
				RequestUtils.getLong(request, "id"));
		if (goodsAcceptance != null) {
			request.setAttribute("goodsAcceptance", goodsAcceptance);
			if (goodsAcceptance.getGoodsId() > 0) {
				FoodComposition foodComposition = foodCompositionService
						.getFoodComposition(goodsAcceptance.getGoodsId());
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

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("goodsAcceptance.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/goodsAcceptance/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		JSONObject result = new JSONObject();
		GoodsAcceptanceQuery query = new GoodsAcceptanceQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		if (!loginContext.isSystemAdministrator()) {
			if (loginContext.getRoles().contains("TenantAdmin") || loginContext.getRoles().contains("HealthPhysician")
					|| loginContext.getRoles().contains("Buyer") || loginContext.getRoles().contains("StockManager")) {
				query.tenantId(loginContext.getTenantId());
				if (loginContext.getRoles().contains("Buyer")) {
					query.setCreateBy(loginContext.getActorId());
				}
			} else {
				return result.toJSONString().getBytes("UTF-8");
			}
		}

		Date acceptanceTime = RequestUtils.getDate(request, "acceptanceTime");
		if (acceptanceTime != null) {
			query.fullDay(DateUtils.getYearMonthDay(acceptanceTime));
		}

		Date acceptanceTime_start = RequestUtils.getDate(request, "startTime");
		if (acceptanceTime_start != null) {
			query.acceptanceTimeGreaterThanOrEqual(acceptanceTime_start);
		}

		Date acceptanceTime_end = RequestUtils.getEndDate(request, "endTime");
		if (acceptanceTime_end != null) {
			query.acceptanceTimeLessThanOrEqual(acceptanceTime_end);
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

		int total = goodsAcceptanceService.getGoodsAcceptanceCountByQueryCriteria(query);
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

			List<GoodsAcceptance> list = goodsAcceptanceService.getGoodsAcceptancesByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				Map<String, User> userMap = IdentityFactory.getUserMap(loginContext.getTenantId());

				for (GoodsAcceptance goodsAcceptance : list) {
					JSONObject rowJSON = goodsAcceptance.toJsonObject();
					rowJSON.put("id", goodsAcceptance.getId());
					rowJSON.put("rowId", goodsAcceptance.getId());
					rowJSON.put("goodsAcceptanceId", goodsAcceptance.getId());
					rowJSON.put("startIndex", ++start);
					User user = userMap.get(goodsAcceptance.getConfirmBy());
					if (user != null) {
						rowJSON.put("confirmName", user.getName());
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

		return new ModelAndView("/heathcare/goodsAcceptance/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("goodsAcceptance.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/heathcare/goodsAcceptance/query", modelMap);
	}

	@RequestMapping("/reviewJson")
	@ResponseBody
	public byte[] reviewJson(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		JSONObject result = new JSONObject();
		GoodsAcceptanceQuery query = new GoodsAcceptanceQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);

		String tenantId = request.getParameter("tenantId");
		if (StringUtils.isEmpty(tenantId)) {
			tenantId = loginContext.getTenantId();
		}
		query.tenantId(tenantId);

		Date acceptanceTime = RequestUtils.getDate(request, "acceptanceTime");
		if (acceptanceTime != null) {
			query.fullDay(DateUtils.getYearMonthDay(acceptanceTime));
		}

		Date acceptanceTime_start = RequestUtils.getDate(request, "startTime");
		if (acceptanceTime_start != null) {
			query.acceptanceTimeGreaterThanOrEqual(acceptanceTime_start);
		}

		Date acceptanceTime_end = RequestUtils.getEndDate(request, "endTime");
		if (acceptanceTime_end != null) {
			query.acceptanceTimeLessThanOrEqual(acceptanceTime_end);
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

		int total = goodsAcceptanceService.getGoodsAcceptanceCountByQueryCriteria(query);
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

			List<GoodsAcceptance> list = goodsAcceptanceService.getGoodsAcceptancesByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				Map<String, User> userMap = IdentityFactory.getUserMap(tenantId);

				for (GoodsAcceptance goodsAcceptance : list) {
					JSONObject rowJSON = goodsAcceptance.toJsonObject();
					rowJSON.put("id", goodsAcceptance.getId());
					rowJSON.put("rowId", goodsAcceptance.getId());
					rowJSON.put("goodsAcceptanceId", goodsAcceptance.getId());
					rowJSON.put("startIndex", ++start);
					User user = userMap.get(goodsAcceptance.getConfirmBy());
					if (user != null) {
						rowJSON.put("confirmName", user.getName());
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

		return new ModelAndView("/heathcare/goodsAcceptance/review_list", modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveBatch")
	public byte[] saveBatch(HttpServletRequest request) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		int week = RequestUtils.getInt(request, "week");
		// Date date = RequestUtils.getDate(request, "date");
		Date date = new Date();
		GoodsPurchaseQuery query = new GoodsPurchaseQuery();
		query.tenantId(loginContext.getTenantId());
		query.fullDay(DateUtils.getYearMonthDay(date));
		query.businessStatus(9);
		List<GoodsPurchase> purchaseList = goodsPurchaseService.list(query);

		GoodsAcceptanceQuery query2 = new GoodsAcceptanceQuery();
		query2.tenantId(loginContext.getTenantId());
		query2.fullDay(DateUtils.getYearMonthDay(date));
		query2.businessStatus(0);

		List<GoodsAcceptance> distList = new ArrayList<GoodsAcceptance>();

		List<GoodsAcceptance> goodsList = goodsAcceptanceService.list(query2);
		Map<Long, Double> qtyMap = new HashMap<Long, Double>();
		if (goodsList != null && !goodsList.isEmpty()) {
			for (GoodsAcceptance model : goodsList) {
				double quantity = RequestUtils.getDouble(request, "qty_" + model.getGoodsId());
				if (quantity > 0) {
					model.setQuantity(quantity);
					qtyMap.put(model.getGoodsId(), quantity);
					distList.add(model);
				}
			}
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		if (purchaseList != null && !purchaseList.isEmpty()) {
			for (GoodsPurchase model : purchaseList) {
				if (qtyMap.get(model.getGoodsId()) == null) {
					double qty = RequestUtils.getDouble(request, "qty_" + model.getGoodsId());
					if (qty > 0) {
						GoodsAcceptance m = new GoodsAcceptance();
						m.setBusinessStatus(0);
						m.setAcceptanceTime(date);
						m.setCreateBy(loginContext.getActorId());
						m.setGoodsId(model.getGoodsId());
						m.setGoodsName(model.getGoodsName());
						m.setGoodsNodeId(model.getGoodsNodeId());
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
			goodsAcceptanceService.saveAll(distList);
			return ResponseUtils.responseJsonResult(true);
		}

		return ResponseUtils.responseJsonResult(false);
	}

	@ResponseBody
	@RequestMapping("/saveGoodsAcceptance")
	public byte[] saveGoodsAcceptance(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		if (loginContext.getRoles().contains("TenantAdmin") || loginContext.getRoles().contains("Buyer")
				|| loginContext.getRoles().contains("StockManager")) {
			long id = RequestUtils.getLong(request, "id");
			long goodsId = RequestUtils.getLong(request, "goodsId");
			double quantity = RequestUtils.getDouble(request, "quantity");
			if (quantity <= 0 || quantity > 5000) {
				return ResponseUtils.responseJsonResult(false, "验收数量必须大于0小于5000。");
			}
			Date acceptanceTime = RequestUtils.getDate(request, "acceptanceTime");
			if (acceptanceTime != null) {
				if (loginContext.getLimit() != Constants.UNLIMIT) {
					if (DateUtils.getYearMonthDay(acceptanceTime) > DateUtils.getNowYearMonthDay()) {
						return ResponseUtils.responseJsonResult(false, "验收数据只能是当天及之前。");
					}
					if ((DateUtils.getDaysBetween(acceptanceTime, new Date())) > 30) {
						return ResponseUtils.responseJsonResult(false, "验收时间只能在一月以内。");
					}
				}
			} else {
				if (acceptanceTime == null) {
					acceptanceTime = new Date();// 不输入默认为当天日期
				}
			}
			GoodsAcceptance goodsAcceptance = null;
			try {
				if (id > 0) {
					goodsAcceptance = goodsAcceptanceService.getGoodsAcceptance(loginContext.getTenantId(), id);
				}
				if (goodsAcceptance == null) {
					goodsAcceptance = new GoodsAcceptance();
				} else {
					if (!StringUtils.equals(loginContext.getTenantId(), goodsAcceptance.getTenantId())) {
						return ResponseUtils.responseJsonResult(false, "验收数据只能所属机构修改。");
					}
					if (loginContext.getLimit() != Constants.UNLIMIT) {
						if (DateUtils.getYearMonthDay(goodsAcceptance.getAcceptanceTime()) != DateUtils
								.getNowYearMonthDay()) {
							return ResponseUtils.responseJsonResult(false, "验收数据只能当天修改。");
						}
					}
				}

				if (goodsId > 0) {
					FoodComposition fd = foodCompositionService.getFoodComposition(goodsId);
					if (fd != null) {
						goodsAcceptance.setGoodsId(goodsId);
						goodsAcceptance.setGoodsName(fd.getName());
						goodsAcceptance.setGoodsNodeId(fd.getNodeId());

						GoodsPurchaseQuery query1 = new GoodsPurchaseQuery();
						query1.tenantId(loginContext.getTenantId());
						query1.businessStatus(9);// 已经确定入库的物品
						query1.setGoodsId(goodsAcceptance.getGoodsId());
						List<GoodsPurchase> goods1 = goodsPurchaseService.list(query1);

						GoodsAcceptanceQuery query2 = new GoodsAcceptanceQuery();
						query2.tenantId(loginContext.getTenantId());
						query2.businessStatus(9);// 已经确定验收的物品
						query2.setGoodsId(goodsAcceptance.getGoodsId());
						List<GoodsAcceptance> goods2 = goodsAcceptanceService.list(query2);

						if (goods1 != null && !goods1.isEmpty()) {
							double existsQty = 0.0;
							for (GoodsPurchase model : goods1) {
								existsQty = existsQty + model.getQuantity();
							}
							if (goods2 != null && !goods2.isEmpty()) {
								for (GoodsAcceptance model : goods2) {
									existsQty = existsQty - model.getQuantity();
								}
							}
							logger.debug("existsQty:" + existsQty);
							if (existsQty > 0 && existsQty >= RequestUtils.getDouble(request, "quantity")) {
								Tools.populate(goodsAcceptance, params);
								goodsAcceptance.setSemester(SysConfig.getSemester());
								goodsAcceptance.setTenantId(loginContext.getTenantId());
								goodsAcceptance.setGoodsId(RequestUtils.getLong(request, "goodsId"));
								goodsAcceptance.setAcceptanceTime(acceptanceTime);
								goodsAcceptance.setQuantity(quantity);
								goodsAcceptance.setUnit(request.getParameter("unit"));
								goodsAcceptance.setReceiverId(request.getParameter("receiverId"));
								goodsAcceptance.setReceiverName(request.getParameter("receiverName"));
								goodsAcceptance.setRemark(request.getParameter("remark"));
								goodsAcceptance.setCreateBy(actorId);
								this.goodsAcceptanceService.save(goodsAcceptance);
								return ResponseUtils.responseJsonResult(true);
							} else {
								logger.debug("验收数量不能超过采购数量:" + existsQty);
								return ResponseUtils.responseJsonResult(false, "验收数量不能超过采购数量:" + existsQty);
							}
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.dietaryService")
	public void setDietaryService(DietaryService dietaryService) {
		this.dietaryService = dietaryService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.foodCompositionService")
	public void setFoodCompositionService(FoodCompositionService foodCompositionService) {
		this.foodCompositionService = foodCompositionService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.goodsAcceptanceService")
	public void setGoodsAcceptanceService(GoodsAcceptanceService goodsAcceptanceService) {
		this.goodsAcceptanceService = goodsAcceptanceService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.goodsPlanQuantityService")
	public void setGoodsPlanQuantityService(GoodsPlanQuantityService goodsPlanQuantityService) {
		this.goodsPlanQuantityService = goodsPlanQuantityService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.goodsPurchaseService")
	public void setGoodsPurchaseService(GoodsPurchaseService goodsPurchaseService) {
		this.goodsPurchaseService = goodsPurchaseService;
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

		String x_view = ViewProperties.getString("goodsAcceptance.copy");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/goodsAcceptance/copy", modelMap);
	}

	@RequestMapping("/view")
	public ModelAndView view(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		GoodsAcceptance goodsAcceptance = goodsAcceptanceService.getGoodsAcceptance(loginContext.getTenantId(),
				RequestUtils.getLong(request, "id"));
		request.setAttribute("goodsAcceptance", goodsAcceptance);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view);
		}

		String x_view = ViewProperties.getString("goodsAcceptance.view");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view);
		}

		return new ModelAndView("/heathcare/goodsAcceptance/view");
	}

}
