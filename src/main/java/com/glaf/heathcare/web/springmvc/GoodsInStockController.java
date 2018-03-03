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
import com.glaf.core.util.security.RSAUtils;
import com.glaf.heathcare.SysConfig;
import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.domain.GoodsInStock;
import com.glaf.heathcare.domain.GoodsPurchase;
import com.glaf.heathcare.query.GoodsInStockQuery;
import com.glaf.heathcare.query.GoodsPurchaseQuery;
import com.glaf.heathcare.service.DietaryService;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.heathcare.service.GoodsInStockService;
import com.glaf.heathcare.service.GoodsPurchaseService;
import com.glaf.matrix.data.factory.DataFileFactory;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/goodsInStock")
@RequestMapping("/heathcare/goodsInStock")
public class GoodsInStockController {
	protected static final Log logger = LogFactory.getLog(GoodsInStockController.class);

	protected DietaryService dietaryService;

	protected FoodCompositionService foodCompositionService;

	protected GoodsInStockService goodsInStockService;

	protected GoodsPurchaseService goodsPurchaseService;

	protected SysTreeService sysTreeService;

	public GoodsInStockController() {

	}

	@ResponseBody
	@RequestMapping("/audit")
	public byte[] audit(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isSystemAdministrator() || loginContext.getRoles().contains("TenantAdmin")
				|| loginContext.getRoles().contains("StockManager")) {
			long id = RequestUtils.getLong(request, "id");
			String ids = request.getParameter("ids");
			GoodsInStock goodsInStock = null;
			try {
				if (id > 0) {
					goodsInStock = goodsInStockService.getGoodsInStock(loginContext.getTenantId(), id);
				}
				if (goodsInStock != null) {
					if (!StringUtils.equals(loginContext.getTenantId(), goodsInStock.getTenantId())) {
						return ResponseUtils.responseJsonResult(false, "入库数据只能所属机构修改。");
					}
					goodsInStock.setBusinessStatus(9);
					goodsInStock.setConfirmBy(loginContext.getActorId());
					goodsInStock.setConfirmTime(new Date());
					this.goodsInStockService.updateGoodsInStockStatus(goodsInStock);
					return ResponseUtils.responseJsonResult(true);
				}
				if (StringUtils.isNotEmpty(ids)) {
					List<Long> pIds = StringTools.splitToLong(ids);
					GoodsInStockQuery query = new GoodsInStockQuery();
					query.tenantId(loginContext.getTenantId());
					query.ids(pIds);
					List<GoodsInStock> list = goodsInStockService.list(query);
					if (list != null && !list.isEmpty()) {
						for (GoodsInStock p : list) {
							if (!StringUtils.equals(loginContext.getTenantId(), p.getTenantId())) {
								return ResponseUtils.responseJsonResult(false, "数据只能所属机构修改。");
							}
							p.setBusinessStatus(9);
							p.setConfirmBy(loginContext.getActorId());
							p.setConfirmTime(new Date());
						}
						goodsInStockService.updateGoodsInStockStatus(list);
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

		GoodsInStockQuery query2 = new GoodsInStockQuery();
		query2.tenantId(loginContext.getTenantId());
		query2.fullDay(DateUtils.getYearMonthDay(date));
		query2.businessStatus(0);

		List<GoodsInStock> goodsList = goodsInStockService.list(query2);
		Map<Long, GoodsInStock> qtyMap = new HashMap<Long, GoodsInStock>();
		if (goodsList != null && !goodsList.isEmpty()) {
			for (GoodsInStock model : goodsList) {
				qtyMap.put(model.getGoodsId(), model);
			}
		}

		GoodsPurchaseQuery query = new GoodsPurchaseQuery();
		query.tenantId(loginContext.getTenantId());
		query.fullDay(DateUtils.getYearMonthDay(date));
		List<GoodsPurchase> planList = goodsPurchaseService.list(query);
		if (planList != null && !planList.isEmpty()) {
			for (GoodsPurchase model : planList) {
				if (qtyMap.get(model.getGoodsId()) != null) {
					GoodsInStock m = qtyMap.get(model.getGoodsId());
					model.setQuantity(m.getQuantity());
					model.setPrice(m.getPrice());
					model.setTotalPrice(m.getTotalPrice());
					model.setExpiryDate(m.getExpiryDate());
					model.setAddress(m.getAddress());
					model.setBatchNo(m.getBatchNo());
					model.setSupplier(m.getSupplier());
					model.setContact(m.getContact());
					model.setStandard(m.getStandard());
				}
			}
			request.setAttribute("planList", planList);
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR);
		// int maxWeek = dietaryService.getMaxWeek(loginContext.getTenantId(),
		// year, SysConfig.getSemester());
		// request.setAttribute("maxWeek", maxWeek);
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

		String x_view = ViewProperties.getString("goodsInStock.batchEdit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/goodsInStock/batchEdit", modelMap);
	}

	@ResponseBody
	@RequestMapping("/copyPurchase")
	public byte[] copyPurchase(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		Date dateTime = RequestUtils.getDate(request, "dateTime");
		if (loginContext.getLimit() != Constants.UNLIMIT) {
			if (dateTime == null || DateUtils.afterTime(dateTime, new Date())) {
				return ResponseUtils.responseJsonResult(false, "入库日期必须是当天或之前的日期。");
			}
		}
		int fullDay = DateUtils.getYearMonthDay(dateTime);
		try {
			goodsInStockService.copyPurchase(loginContext.getTenantId(), fullDay, actorId);
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
					GoodsInStock goodsInStock = goodsInStockService.getGoodsInStock(loginContext.getTenantId(),
							Long.valueOf(x));
					if (goodsInStock != null && ((loginContext.getRoles().contains("TenantAdmin")
							|| loginContext.getRoles().contains("Buyer")
							|| loginContext.getRoles().contains("StockManager"))
							&& StringUtils.equals(loginContext.getTenantId(), goodsInStock.getTenantId()))) {
						if (goodsInStock.getBusinessStatus() == 0) {
							DataFileFactory.getInstance().deleteByBusinessKey(loginContext.getTenantId(),
									"goods_instock", "goods_instock_" + goodsInStock.getId());
							goodsInStockService.deleteById(loginContext.getTenantId(), goodsInStock.getId());
						}
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			GoodsInStock goodsInStock = goodsInStockService.getGoodsInStock(loginContext.getTenantId(),
					Long.valueOf(id));
			if (goodsInStock != null && ((loginContext.getRoles().contains("TenantAdmin")
					|| loginContext.getRoles().contains("Buyer") || loginContext.getRoles().contains("StockManager"))
					&& StringUtils.equals(loginContext.getTenantId(), goodsInStock.getTenantId()))) {
				if (goodsInStock.getBusinessStatus() == 0) {
					DataFileFactory.getInstance().deleteByBusinessKey(loginContext.getTenantId(), "goods_instock",
							"goods_instock_" + goodsInStock.getId());
					goodsInStockService.deleteById(loginContext.getTenantId(), goodsInStock.getId());
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
		GoodsInStock goodsInStock = goodsInStockService.getGoodsInStock(loginContext.getTenantId(),
				RequestUtils.getLong(request, "id"));
		if (goodsInStock != null) {
			request.setAttribute("goodsInStock", goodsInStock);
			request.setAttribute("status_enc",
					RSAUtils.encryptString(String.valueOf(goodsInStock.getBusinessStatus())));
			logger.debug("status_enc:" + request.getAttribute("status_enc"));
			logger.debug("status->" + RSAUtils.decryptString((String) request.getAttribute("status_enc")));
			if (goodsInStock.getBusinessStatus() != 9) {
				if (StringUtils.equals(request.getParameter("audit"), "true")) {
					request.setAttribute("status_enc", RSAUtils.encryptString(String.valueOf("2")));
				}
			}
			if (goodsInStock.getGoodsId() > 0) {
				FoodComposition foodComposition = foodCompositionService.getFoodComposition(goodsInStock.getGoodsId());
				if (foodComposition != null) {
					List<FoodComposition> foods = foodCompositionService
							.getFoodCompositions(foodComposition.getNodeId());
					request.setAttribute("foods", foods);
					request.setAttribute("nodeId", foodComposition.getNodeId());
				}
			}
		} else {
			request.setAttribute("status_enc", RSAUtils.encryptString(String.valueOf("0")));
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

		String x_view = ViewProperties.getString("goodsInStock.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/goodsInStock/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug("params:" + params);
		JSONObject result = new JSONObject();
		GoodsInStockQuery query = new GoodsInStockQuery();
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

		Date inStockTime = RequestUtils.getDate(request, "inStockTime");
		if (inStockTime != null) {
			query.fullDay(DateUtils.getYearMonthDay(inStockTime));
		}

		Date inStockTime_start = RequestUtils.getDate(request, "startTime");
		if (inStockTime_start != null) {
			query.inStockTimeGreaterThanOrEqual(inStockTime_start);
		}

		Date inStockTime_end = RequestUtils.getEndDate(request, "endTime");
		if (inStockTime_end != null) {
			query.inStockTimeLessThanOrEqual(inStockTime_end);
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

		int total = goodsInStockService.getGoodsInStockCountByQueryCriteria(query);
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

			List<GoodsInStock> list = goodsInStockService.getGoodsInStocksByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				Map<String, User> userMap = IdentityFactory.getUserMap(loginContext.getTenantId());

				for (GoodsInStock goodsInStock : list) {
					JSONObject rowJSON = goodsInStock.toJsonObject();
					rowJSON.put("id", goodsInStock.getId());
					rowJSON.put("rowId", goodsInStock.getId());
					rowJSON.put("goodsInStockId", goodsInStock.getId());
					rowJSON.put("startIndex", ++start);
					User user = userMap.get(goodsInStock.getConfirmBy());
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

		return new ModelAndView("/heathcare/goodsInStock/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("goodsInStock.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/heathcare/goodsInStock/query", modelMap);
	}

	@RequestMapping("/reviewJson")
	@ResponseBody
	public byte[] reviewJson(HttpServletRequest request, ModelMap modelMap) throws IOException {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug("params:" + params);
		JSONObject result = new JSONObject();
		GoodsInStockQuery query = new GoodsInStockQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.businessStatus(9);

		String tenantId = request.getParameter("tenantId");
		query.tenantId(tenantId);

		Date inStockTime = RequestUtils.getDate(request, "inStockTime");
		if (inStockTime != null) {
			query.fullDay(DateUtils.getYearMonthDay(inStockTime));
		}

		Date inStockTime_start = RequestUtils.getDate(request, "startTime");
		if (inStockTime_start != null) {
			query.inStockTimeGreaterThanOrEqual(inStockTime_start);
		}

		Date inStockTime_end = RequestUtils.getEndDate(request, "endTime");
		if (inStockTime_end != null) {
			query.inStockTimeLessThanOrEqual(inStockTime_end);
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

		int total = goodsInStockService.getGoodsInStockCountByQueryCriteria(query);
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

			List<GoodsInStock> list = goodsInStockService.getGoodsInStocksByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				Map<String, User> userMap = IdentityFactory.getUserMap(tenantId);

				for (GoodsInStock goodsInStock : list) {
					JSONObject rowJSON = goodsInStock.toJsonObject();
					rowJSON.put("id", goodsInStock.getId());
					rowJSON.put("rowId", goodsInStock.getId());
					rowJSON.put("goodsInStockId", goodsInStock.getId());
					rowJSON.put("startIndex", ++start);
					User user = userMap.get(goodsInStock.getConfirmBy());
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

		return new ModelAndView("/heathcare/goodsInStock/review_list", modelMap);
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

		GoodsInStockQuery query2 = new GoodsInStockQuery();
		query2.tenantId(loginContext.getTenantId());
		query2.fullDay(DateUtils.getYearMonthDay(date));
		query2.businessStatus(0);

		List<GoodsInStock> distList = new ArrayList<GoodsInStock>();

		List<GoodsInStock> goodsList = goodsInStockService.list(query2);
		Map<Long, Double> qtyMap = new HashMap<Long, Double>();
		if (goodsList != null && !goodsList.isEmpty()) {
			for (GoodsInStock model : goodsList) {
				double quantity = RequestUtils.getDouble(request, "qty_" + model.getGoodsId());
				if (quantity > 0) {
					model.setQuantity(quantity);
					model.setPrice(RequestUtils.getDouble(request, "price_" + model.getGoodsId()));
					model.setTotalPrice(RequestUtils.getDouble(request, "totalPrice_" + model.getGoodsId()));
					model.setExpiryDate(RequestUtils.getDate(request, "expiryDate_" + model.getGoodsId()));
					model.setAddress(request.getParameter("address_" + model.getGoodsId()));
					model.setBatchNo(request.getParameter("batchNo_" + model.getGoodsId()));
					model.setSupplier(request.getParameter("supplier_" + model.getGoodsId()));
					model.setContact(request.getParameter("contact_" + model.getGoodsId()));
					model.setStandard(request.getParameter("standard_" + model.getGoodsId()));
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

		List<GoodsPurchase> purchaseList = goodsPurchaseService.list(query);
		if (purchaseList != null && !purchaseList.isEmpty()) {
			for (GoodsPurchase model : purchaseList) {
				if (qtyMap.get(model.getGoodsId()) == null) {
					double qty = RequestUtils.getDouble(request, "qty_" + model.getGoodsId());
					if (qty > 0) {
						GoodsInStock m = new GoodsInStock();
						m.setBusinessStatus(0);
						m.setInStockTime(date);
						m.setCreateBy(loginContext.getActorId());
						m.setGoodsId(model.getGoodsId());
						m.setGoodsName(model.getGoodsName());
						m.setGoodsNodeId(model.getGoodsNodeId());
						m.setPrice(RequestUtils.getDouble(request, "price_" + model.getGoodsId()));
						m.setTotalPrice(RequestUtils.getDouble(request, "totalPrice_" + model.getGoodsId()));
						m.setExpiryDate(RequestUtils.getDate(request, "expiryDate_" + model.getGoodsId()));
						m.setBatchNo(request.getParameter("batchNo_" + model.getGoodsId()));
						m.setSupplier(request.getParameter("supplier_" + model.getGoodsId()));
						m.setContact(request.getParameter("contact_" + model.getGoodsId()));
						m.setStandard(request.getParameter("standard_" + model.getGoodsId()));
						m.setAddress(request.getParameter("address_" + model.getGoodsId()));
						m.setTenantId(loginContext.getTenantId());
						m.setYear(year);
						m.setMonth(month);
						m.setDay(day);
						m.setWeek(week);
						m.setSemester(SysConfig.getSemester());
						m.setFullDay(DateUtils.getYearMonthDay(date));
						m.setQuantity(qty);
						if (loginContext.getLimit() != Constants.UNLIMIT) {
							if (m.getExpiryDate() != null
									&& DateUtils.getYearMonthDay(m.getExpiryDate()) <= DateUtils.getNowYearMonthDay()) {
								return ResponseUtils.responseJsonResult(false, "有效期不能早于当天日期。");
							}
						}
						distList.add(m);
					}
				}
			}
			goodsInStockService.saveAll(distList);
			return ResponseUtils.responseJsonResult(true);
		}

		return ResponseUtils.responseJsonResult(false);
	}

	@ResponseBody
	@RequestMapping("/saveGoodsInStock")
	public byte[] saveGoodsInStock(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		if (loginContext.getRoles().contains("TenantAdmin") || loginContext.getRoles().contains("Buyer")
				|| loginContext.getRoles().contains("StockManager")) {
			long id = RequestUtils.getLong(request, "id");
			Date inStockTime = RequestUtils.getDate(request, "inStockTime");
			Date expiryDate = RequestUtils.getDate(request, "expiryDate");
			if (inStockTime != null) {
				if (loginContext.getLimit() != Constants.UNLIMIT) {
					if (DateUtils.getYearMonthDay(inStockTime) > DateUtils.getNowYearMonthDay()) {
						return ResponseUtils.responseJsonResult(false, "入库数据只能是当天及之前。");
					}
					if ((DateUtils.getDaysBetween(inStockTime, new Date())) > 30) {
						return ResponseUtils.responseJsonResult(false, "入库时间只能在一月以内。");
					}
				}
			} else {
				if (inStockTime == null) {
					inStockTime = new Date();// 不输入默认为当天日期
				}
			}
			if (expiryDate != null) {
				if (loginContext.getLimit() != Constants.UNLIMIT) {
					if (DateUtils.getYearMonthDay(expiryDate) <= DateUtils.getNowYearMonthDay()) {
						return ResponseUtils.responseJsonResult(false, "有效期不能早于当天日期。");
					}
					if (inStockTime != null
							&& DateUtils.getYearMonthDay(inStockTime) >= DateUtils.getYearMonthDay(expiryDate)) {
						return ResponseUtils.responseJsonResult(false, "有效期不能早于入库日期。");
					}
				}
			}
			double quantity = RequestUtils.getDouble(request, "quantity");
			if (quantity <= 0 || quantity > 5000) {
				return ResponseUtils.responseJsonResult(false, "入库数量必须大于0小于5000。");
			}
			GoodsInStock goodsInStock = null;
			try {
				if (id > 0) {
					goodsInStock = goodsInStockService.getGoodsInStock(loginContext.getTenantId(), id);
				}
				if (goodsInStock == null) {
					goodsInStock = new GoodsInStock();
					goodsInStock.setTenantId(loginContext.getTenantId());
				} else {
					if (!StringUtils.equals(loginContext.getTenantId(), goodsInStock.getTenantId())) {
						return ResponseUtils.responseJsonResult(false, "入库数据只能所属机构修改。");
					}
					if (loginContext.getLimit() != Constants.UNLIMIT) {
						if (DateUtils.getYearMonthDay(goodsInStock.getInStockTime()) != DateUtils
								.getNowYearMonthDay()) {
							return ResponseUtils.responseJsonResult(false, "入库数据只能当天修改。");
						}
					}
				}
				Tools.populate(goodsInStock, params);

				goodsInStock.setSemester(SysConfig.getSemester());
				goodsInStock.setGoodsId(RequestUtils.getLong(request, "goodsId"));
				goodsInStock.setInStockTime(inStockTime);
				goodsInStock.setQuantity(quantity);
				goodsInStock.setUnit(request.getParameter("unit"));
				goodsInStock.setPrice(RequestUtils.getDouble(request, "price"));
				goodsInStock.setTotalPrice(RequestUtils.getDouble(request, "totalPrice"));
				goodsInStock.setExpiryDate(expiryDate);
				goodsInStock.setBatchNo(request.getParameter("batchNo"));
				goodsInStock.setSupplier(request.getParameter("supplier"));
				goodsInStock.setContact(request.getParameter("contact"));
				goodsInStock.setStandard(request.getParameter("standard"));
				goodsInStock.setReceiverId(request.getParameter("receiverId"));
				goodsInStock.setReceiverName(request.getParameter("receiverName"));
				goodsInStock.setRemark(request.getParameter("remark"));
				goodsInStock.setCreateBy(actorId);

				if (goodsInStock.getGoodsId() > 0) {
					FoodComposition fd = foodCompositionService.getFoodComposition(goodsInStock.getGoodsId());
					goodsInStock.setGoodsName(fd.getName());
					goodsInStock.setGoodsNodeId(fd.getNodeId());
				}
				this.goodsInStockService.save(goodsInStock);

				return ResponseUtils.responseJsonResult(true);
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

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.goodsInStockService")
	public void setGoodsInStockService(GoodsInStockService goodsInStockService) {
		this.goodsInStockService = goodsInStockService;
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

		String x_view = ViewProperties.getString("goodsInStock.copy");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/goodsInStock/copy", modelMap);
	}

	@RequestMapping("/view")
	public ModelAndView view(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		GoodsInStock goodsInStock = goodsInStockService.getGoodsInStock(loginContext.getTenantId(),
				RequestUtils.getLong(request, "id"));
		request.setAttribute("goodsInStock", goodsInStock);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view);
		}

		String x_view = ViewProperties.getString("goodsInStock.view");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view);
		}

		return new ModelAndView("/heathcare/goodsInStock/view");
	}

}
