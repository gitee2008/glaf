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
import com.glaf.base.modules.sys.model.SysTenant;
import com.glaf.base.modules.sys.model.SysTree;
import com.glaf.base.modules.sys.service.SysTenantService;
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
import com.glaf.heathcare.domain.GoodsPurchase;
import com.glaf.heathcare.domain.GoodsStock;
import com.glaf.heathcare.query.GoodsPurchaseQuery;
import com.glaf.heathcare.query.GoodsStockQuery;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.heathcare.service.GoodsPurchaseService;
import com.glaf.heathcare.service.GoodsStockService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/goodsPurchase")
@RequestMapping("/heathcare/goodsPurchase")
public class GoodsPurchaseController {
	protected static final Log logger = LogFactory.getLog(GoodsPurchaseController.class);

	protected FoodCompositionService foodCompositionService;

	protected GoodsPurchaseService goodsPurchaseService;

	protected GoodsStockService goodsStockService;

	protected SysTenantService sysTenantService;

	protected SysTreeService sysTreeService;

	public GoodsPurchaseController() {

	}

	@ResponseBody
	@RequestMapping("/audit")
	public byte[] audit(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.getRoles().contains("TenantAdmin") || loginContext.getRoles().contains("StockManager")) {
			long id = RequestUtils.getLong(request, "id");
			String ids = request.getParameter("ids");
			GoodsPurchase goodsPurchase = null;
			try {
				if (id > 0) {
					goodsPurchase = goodsPurchaseService.getGoodsPurchase(loginContext.getTenantId(), id);
				}
				if (goodsPurchase != null) {
					if (!StringUtils.equals(loginContext.getTenantId(), goodsPurchase.getTenantId())) {
						return ResponseUtils.responseJsonResult(false, "采购数据只能所属机构修改。");
					}
					goodsPurchase.setBusinessStatus(9);
					goodsPurchase.setConfirmBy(loginContext.getActorId());
					goodsPurchase.setConfirmTime(new Date());
					this.goodsPurchaseService.updateGoodsPurchaseStatus(goodsPurchase);
					return ResponseUtils.responseJsonResult(true);
				}
				if (StringUtils.isNotEmpty(ids)) {
					List<Long> pIds = StringTools.splitToLong(ids);
					GoodsPurchaseQuery query = new GoodsPurchaseQuery();
					query.tenantId(loginContext.getTenantId());
					query.ids(pIds);
					List<GoodsPurchase> list = goodsPurchaseService.list(query);
					if (list != null && !list.isEmpty()) {
						for (GoodsPurchase p : list) {
							if (!StringUtils.equals(loginContext.getTenantId(), p.getTenantId())) {
								return ResponseUtils.responseJsonResult(false, "采购数据只能所属机构修改。");
							}
							p.setBusinessStatus(9);
							p.setConfirmBy(loginContext.getActorId());
							p.setConfirmTime(new Date());
						}
						goodsPurchaseService.updateGoodsPurchaseStatus(list);
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

	@ResponseBody
	@RequestMapping("/copyPurchasePlan")
	public byte[] copyPurchasePlan(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		Date purchaseTime = RequestUtils.getDate(request, "purchaseTime");
		if (loginContext.getLimit() != Constants.UNLIMIT) {
			if (purchaseTime == null || DateUtils.afterTime(purchaseTime, new Date())) {
				return ResponseUtils.responseJsonResult(false, "采购日期必须是当天或之前的日期。");
			}
		}
		int fullDay = DateUtils.getYearMonthDay(purchaseTime);
		try {
			goodsPurchaseService.copyPurchasePlan(loginContext.getTenantId(), fullDay, actorId);
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
					GoodsPurchase goodsPurchase = goodsPurchaseService.getGoodsPurchase(loginContext.getTenantId(),
							Long.valueOf(x));
					if (goodsPurchase != null && (((loginContext.getRoles().contains("Buyer")
							|| loginContext.getRoles().contains("StockManager")
							|| loginContext.getRoles().contains("TenantAdmin"))
							&& StringUtils.equals(goodsPurchase.getTenantId(), loginContext.getTenantId())))) {
						if (goodsPurchase.getBusinessStatus() == 0) {
							goodsPurchaseService.deleteById(loginContext.getTenantId(), goodsPurchase.getId());
						}
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			GoodsPurchase goodsPurchase = goodsPurchaseService.getGoodsPurchase(loginContext.getTenantId(),
					Long.valueOf(id));
			if (goodsPurchase != null
					&& (((loginContext.getRoles().contains("Buyer") || loginContext.getRoles().contains("StockManager")
							|| loginContext.getRoles().contains("TenantAdmin"))
							&& StringUtils.equals(goodsPurchase.getTenantId(), loginContext.getTenantId())))) {
				if (goodsPurchase.getBusinessStatus() == 0) {
					goodsPurchaseService.deleteById(loginContext.getTenantId(), goodsPurchase.getId());
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

		GoodsPurchase goodsPurchase = goodsPurchaseService.getGoodsPurchase(loginContext.getTenantId(),
				RequestUtils.getLong(request, "id"));
		if (goodsPurchase != null) {
			request.setAttribute("goodsPurchase", goodsPurchase);
			if (goodsPurchase.getGoodsId() > 0) {
				FoodComposition foodComposition = foodCompositionService.getFoodComposition(goodsPurchase.getGoodsId());
				if (foodComposition != null) {
					List<FoodComposition> foods = foodCompositionService
							.getFoodCompositions(foodComposition.getNodeId());
					request.setAttribute("foods", foods);
					request.setAttribute("nodeId", foodComposition.getNodeId());
				}
			}
		}

		SysTenant tenant = sysTenantService.getSysTenantByTenantId(loginContext.getTenantId());
		if (tenant != null) {
			request.setAttribute("tenant", tenant);
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

		String x_view = ViewProperties.getString("goodsPurchase.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/goodsPurchase/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		JSONObject result = new JSONObject();
		GoodsPurchaseQuery query = new GoodsPurchaseQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		String purchaseTime = request.getParameter("purchaseTime");
		if (StringUtils.isNotEmpty(purchaseTime)) {
			query.fullDay(DateUtils.getYearMonthDay(DateUtils.toDate(purchaseTime)));
		}

		Date purchaseTime_start = RequestUtils.getDate(request, "startTime");
		if (purchaseTime_start != null) {
			query.purchaseTimeGreaterThanOrEqual(purchaseTime_start);
		}

		Date purchaseTime_end = RequestUtils.getEndDate(request, "endTime");
		if (purchaseTime_end != null) {
			query.purchaseTimeLessThanOrEqual(purchaseTime_end);
		}

		int week = RequestUtils.getInt(request, "week");
		if (week > 0) {
			query.week(week);
			query.semester(SysConfig.getSemester());
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			int year = calendar.get(Calendar.YEAR);
			query.year(year);
		}

		if (!loginContext.isSystemAdministrator()) {
			if (loginContext.getRoles().contains("TenantAdmin") || loginContext.getRoles().contains("HealthPhysician")
					|| loginContext.getRoles().contains("Buyer") || loginContext.getRoles().contains("StockManager")) {
				query.tenantId(loginContext.getTenantId());
			} else {
				return result.toJSONString().getBytes("UTF-8");
			}
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
			limit = 50;
		}

		int total = goodsPurchaseService.getGoodsPurchaseCountByQueryCriteria(query);
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

			List<GoodsPurchase> list = goodsPurchaseService.getGoodsPurchasesByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				Map<String, User> userMap = IdentityFactory.getUserMap(loginContext.getTenantId());

				GoodsStockQuery query2 = new GoodsStockQuery();
				query2.tenantId(loginContext.getTenantId());
				query2.setQuantityGreaterThan(0D);
				List<GoodsStock> list2 = goodsStockService.list(query2);
				Map<Long, Double> qtyMap = new HashMap<Long, Double>();
				if (list2 != null && !list2.isEmpty()) {
					for (GoodsStock stock : list2) {
						qtyMap.put(stock.getGoodsId(), stock.getQuantity());
					}
				}

				logger.debug(qtyMap);

				for (GoodsPurchase goodsPurchase : list) {
					JSONObject rowJSON = goodsPurchase.toJsonObject();
					rowJSON.put("id", goodsPurchase.getId());
					rowJSON.put("rowId", goodsPurchase.getId());
					rowJSON.put("goodsPurchaseId", goodsPurchase.getId());
					rowJSON.put("startIndex", ++start);
					if (goodsPurchase.getQuantity() > 0) {
						rowJSON.put("quantity", Math.round(goodsPurchase.getQuantity() * 10D) / 10D);
					}
					if (goodsPurchase.getBusinessStatus() == 0) {
						if (qtyMap.get(goodsPurchase.getGoodsId()) != null) {
							goodsPurchase.setStockQuantity(qtyMap.get(goodsPurchase.getGoodsId()));
							rowJSON.put("stockQuantity", goodsPurchase.getStockQuantity());
						}
					}
					User user = userMap.get(goodsPurchase.getConfirmBy());
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

		List<Integer> weeks = new ArrayList<Integer>();

		for (int i = 1; i <= 20; i++) {
			weeks.add(i);
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR);
		request.setAttribute("year", year);
		request.setAttribute("weeks", weeks);
		request.setAttribute("semester", SysConfig.getSemester());

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/goodsPurchase/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("goodsPurchase.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/heathcare/goodsPurchase/query", modelMap);
	}

	@RequestMapping("/reviewJson")
	@ResponseBody
	public byte[] reviewJson(HttpServletRequest request) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		JSONObject result = new JSONObject();
		GoodsPurchaseQuery query = new GoodsPurchaseQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.businessStatus(9);

		String tenantId = request.getParameter("tenantId");
		query.tenantId(tenantId);

		String purchaseTime = request.getParameter("purchaseTime");
		if (StringUtils.isNotEmpty(purchaseTime)) {
			query.fullDay(DateUtils.getYearMonthDay(DateUtils.toDate(purchaseTime)));
		}

		Date purchaseTime_start = RequestUtils.getDate(request, "startTime");
		if (purchaseTime_start != null) {
			query.purchaseTimeGreaterThanOrEqual(purchaseTime_start);
		}

		Date purchaseTime_end = RequestUtils.getEndDate(request, "endTime");
		if (purchaseTime_end != null) {
			query.purchaseTimeLessThanOrEqual(purchaseTime_end);
		}

		int week = RequestUtils.getInt(request, "week");
		if (week > 0) {
			query.week(week);
			query.semester(SysConfig.getSemester());
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			int year = calendar.get(Calendar.YEAR);
			query.year(year);
		}

		if (!loginContext.isSystemAdministrator()) {
			if (loginContext.getRoles().contains("TenantAdmin") || loginContext.getRoles().contains("Buyer")
					|| loginContext.getRoles().contains("StockManager")) {
				query.tenantId(loginContext.getTenantId());
			} else {
				return result.toJSONString().getBytes("UTF-8");
			}
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
			limit = 50;
		}

		int total = goodsPurchaseService.getGoodsPurchaseCountByQueryCriteria(query);
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

			List<GoodsPurchase> list = goodsPurchaseService.getGoodsPurchasesByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				Map<String, User> userMap = IdentityFactory.getUserMap(loginContext.getTenantId());

				GoodsStockQuery query2 = new GoodsStockQuery();
				query2.tenantId(loginContext.getTenantId());
				query2.setQuantityGreaterThan(0D);
				List<GoodsStock> list2 = goodsStockService.list(query2);
				Map<Long, Double> qtyMap = new HashMap<Long, Double>();
				if (list2 != null && !list2.isEmpty()) {
					for (GoodsStock stock : list2) {
						qtyMap.put(stock.getGoodsId(), stock.getQuantity());
					}
				}

				logger.debug(qtyMap);

				for (GoodsPurchase goodsPurchase : list) {
					JSONObject rowJSON = goodsPurchase.toJsonObject();
					rowJSON.put("id", goodsPurchase.getId());
					rowJSON.put("rowId", goodsPurchase.getId());
					rowJSON.put("goodsPurchaseId", goodsPurchase.getId());
					rowJSON.put("startIndex", ++start);
					if (goodsPurchase.getQuantity() > 0) {
						rowJSON.put("quantity", Math.round(goodsPurchase.getQuantity() * 10D) / 10D);
					}
					if (goodsPurchase.getBusinessStatus() == 0) {
						if (qtyMap.get(goodsPurchase.getGoodsId()) != null) {
							goodsPurchase.setStockQuantity(qtyMap.get(goodsPurchase.getGoodsId()));
							rowJSON.put("stockQuantity", goodsPurchase.getStockQuantity());
						}
					}
					User user = userMap.get(goodsPurchase.getConfirmBy());
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

		return new ModelAndView("/heathcare/goodsPurchase/review_list", modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveGoodsPurchase")
	public byte[] saveGoodsPurchase(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug(params);
		if (loginContext.isSystemAdministrator() || loginContext.getRoles().contains("TenantAdmin")
				|| loginContext.getRoles().contains("Buyer") || loginContext.getRoles().contains("StockManager")) {
			long id = RequestUtils.getLong(request, "id");
			Date purchaseTime = RequestUtils.getDate(request, "purchaseTime");
			if (loginContext.getLimit() != Constants.UNLIMIT) {
				if (purchaseTime == null || DateUtils.afterTime(purchaseTime, new Date())) {
					return ResponseUtils.responseJsonResult(false, "采购日期必须是当天或之前的日期。");
				}
			}
			GoodsPurchase goodsPurchase = null;
			try {
				if (id > 0) {
					goodsPurchase = goodsPurchaseService.getGoodsPurchase(loginContext.getTenantId(), id);
				}
				if (goodsPurchase == null) {
					goodsPurchase = new GoodsPurchase();
					goodsPurchase.setTenantId(loginContext.getTenantId());
					// goodsPurchase.setSysFlag("N");// 新生成的采购单，并非系统自动生成。
				} else {
					if (!StringUtils.equals(loginContext.getTenantId(), goodsPurchase.getTenantId())) {
						return ResponseUtils.responseJsonResult(false, "采购数据只能所属机构修改。");
					}
				}
				params.remove("sysFlag");

				Tools.populate(goodsPurchase, params);

				goodsPurchase.setGoodsId(RequestUtils.getLong(request, "goodsId"));
				goodsPurchase.setPurchaseTime(purchaseTime);
				goodsPurchase.setQuantity(RequestUtils.getDouble(request, "quantity"));
				goodsPurchase.setUnit(request.getParameter("unit"));
				goodsPurchase.setPrice(RequestUtils.getDouble(request, "price"));
				goodsPurchase.setTotalPrice(RequestUtils.getDouble(request, "totalPrice"));
				goodsPurchase.setProposerId(request.getParameter("proposerId"));
				goodsPurchase.setProposerName(request.getParameter("proposerName"));
				goodsPurchase.setBatchNo(request.getParameter("batchNo"));
				goodsPurchase.setSupplier(request.getParameter("supplier"));
				goodsPurchase.setContact(request.getParameter("contact"));
				goodsPurchase.setStandard(request.getParameter("standard"));
				goodsPurchase.setAddress(request.getParameter("address"));
				goodsPurchase.setExpiryDate(RequestUtils.getDate(request, "expiryDate"));
				goodsPurchase.setInvoiceFlag(request.getParameter("invoiceFlag"));
				goodsPurchase.setRemark(request.getParameter("remark"));
				goodsPurchase.setSemester(SysConfig.getSemester());
				goodsPurchase.setCreateBy(actorId);

				if (goodsPurchase.getGoodsId() > 0) {
					FoodComposition fd = foodCompositionService.getFoodComposition(goodsPurchase.getGoodsId());
					goodsPurchase.setGoodsName(fd.getName());
					goodsPurchase.setGoodsNodeId(fd.getNodeId());
				}
				this.goodsPurchaseService.save(goodsPurchase);

				return ResponseUtils.responseJsonResult(true);
			} catch (Exception ex) {
				// ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.foodCompositionService")
	public void setFoodCompositionService(FoodCompositionService foodCompositionService) {
		this.foodCompositionService = foodCompositionService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.goodsPurchaseService")
	public void setGoodsPurchaseService(GoodsPurchaseService goodsPurchaseService) {
		this.goodsPurchaseService = goodsPurchaseService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.goodsStockService")
	public void setGoodsStockService(GoodsStockService goodsStockService) {
		this.goodsStockService = goodsStockService;
	}

	@javax.annotation.Resource
	public void setSysTenantService(SysTenantService sysTenantService) {
		this.sysTenantService = sysTenantService;
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

		String x_view = ViewProperties.getString("goodsPurchase.copy");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/goodsPurchase/copy", modelMap);
	}

	@RequestMapping("/view")
	public ModelAndView view(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		GoodsPurchase goodsPurchase = goodsPurchaseService.getGoodsPurchase(loginContext.getTenantId(),
				RequestUtils.getLong(request, "id"));
		request.setAttribute("goodsPurchase", goodsPurchase);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view);
		}

		String x_view = ViewProperties.getString("goodsPurchase.view");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view);
		}

		return new ModelAndView("/heathcare/goodsPurchase/view");
	}

}
