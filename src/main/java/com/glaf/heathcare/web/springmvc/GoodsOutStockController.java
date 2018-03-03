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
import com.glaf.heathcare.domain.GoodsInStock;
import com.glaf.heathcare.domain.GoodsOutStock;
import com.glaf.heathcare.domain.GoodsPlanQuantity;
import com.glaf.heathcare.domain.GoodsStock;
import com.glaf.heathcare.query.FoodCompositionQuery;
import com.glaf.heathcare.query.GoodsInStockQuery;
import com.glaf.heathcare.query.GoodsOutStockQuery;
import com.glaf.heathcare.query.GoodsPlanQuantityQuery;
import com.glaf.heathcare.query.GoodsStockQuery;
import com.glaf.heathcare.service.DietaryService;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.heathcare.service.GoodsInStockService;
import com.glaf.heathcare.service.GoodsOutStockService;
import com.glaf.heathcare.service.GoodsPlanQuantityService;
import com.glaf.heathcare.service.GoodsStockService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/goodsOutStock")
@RequestMapping("/heathcare/goodsOutStock")
public class GoodsOutStockController {
	protected static final Log logger = LogFactory.getLog(GoodsOutStockController.class);

	protected DietaryService dietaryService;

	protected FoodCompositionService foodCompositionService;

	protected GoodsInStockService goodsInStockService;

	protected GoodsOutStockService goodsOutStockService;

	protected GoodsStockService goodsStockService;

	protected GoodsPlanQuantityService goodsPlanQuantityService;

	protected SysTreeService sysTreeService;

	public GoodsOutStockController() {

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
			GoodsOutStock goodsOutStock = null;
			try {
				if (StringUtils.isNotEmpty(ids)) {
					List<Long> pIds = StringTools.splitToLong(ids);
					for (Long pId : pIds) {
						goodsOutStock = goodsOutStockService.getGoodsOutStock(loginContext.getTenantId(), pId);
						if (goodsOutStock != null) {
							if (!StringUtils.equals(loginContext.getTenantId(), goodsOutStock.getTenantId())) {
								return ResponseUtils.responseJsonResult(false, "出库数据只能所属机构修改。");
							}

							GoodsInStockQuery query1 = new GoodsInStockQuery();
							query1.tenantId(loginContext.getTenantId());
							query1.setGoodsId(goodsOutStock.getGoodsId());
							query1.businessStatus(9);// 已经确定入库的物品
							List<GoodsInStock> goods1 = goodsInStockService.list(query1);

							GoodsOutStockQuery query2 = new GoodsOutStockQuery();
							query2.tenantId(loginContext.getTenantId());
							query2.setGoodsId(goodsOutStock.getGoodsId());
							query2.businessStatus(9);// 已经确定出库的物品
							List<GoodsOutStock> goods2 = goodsOutStockService.list(query2);

							if (goods1 != null && !goods1.isEmpty()) {
								double existsQty = 0.0;
								for (GoodsInStock model : goods1) {
									existsQty = existsQty + model.getQuantity();
								}
								if (goods2 != null && !goods2.isEmpty()) {
									for (GoodsOutStock model : goods2) {
										existsQty = existsQty - model.getQuantity();
									}
								}
								existsQty = Math.round(existsQty * 10D) / 10D;
								if (existsQty > 0 && existsQty >= goodsOutStock.getQuantity()) {
									goodsOutStock.setBusinessStatus(9);
									goodsOutStock.setConfirmBy(actorId);
									goodsOutStock.setConfirmTime(new Date());
									this.goodsOutStockService.updateGoodsOutStockStatus(goodsOutStock);
								} else {
									return ResponseUtils.responseJsonResult(false,
											goodsOutStock.getGoodsName() + "出库申请数量" + goodsOutStock.getQuantity()
													+ "已经超过库存数量" + existsQty + "，不能出库。");
								}
							}
						}
					}
					return ResponseUtils.responseJsonResult(true);
				}
				if (id > 0) {
					goodsOutStock = goodsOutStockService.getGoodsOutStock(loginContext.getTenantId(), id);
				}
				if (goodsOutStock != null) {
					if (!StringUtils.equals(loginContext.getTenantId(), goodsOutStock.getTenantId())) {
						return ResponseUtils.responseJsonResult(false, "出库数据只能所属机构修改。");
					}
					GoodsInStockQuery query1 = new GoodsInStockQuery();
					query1.tenantId(loginContext.getTenantId());
					query1.setGoodsId(goodsOutStock.getGoodsId());
					query1.businessStatus(9);// 已经确定入库的物品
					List<GoodsInStock> goods1 = goodsInStockService.list(query1);

					GoodsOutStockQuery query2 = new GoodsOutStockQuery();
					query2.tenantId(loginContext.getTenantId());
					query2.setGoodsId(goodsOutStock.getGoodsId());
					query2.businessStatus(9);// 已经确定出库的物品
					List<GoodsOutStock> goods2 = goodsOutStockService.list(query2);

					if (goods1 != null && !goods1.isEmpty()) {
						double existsQty = 0.0;
						for (GoodsInStock model : goods1) {
							existsQty = existsQty + model.getQuantity();
						}
						if (goods2 != null && !goods2.isEmpty()) {
							for (GoodsOutStock model : goods2) {
								existsQty = existsQty - model.getQuantity();
							}
						}
						existsQty = Math.round(existsQty * 10D) / 10D;
						if (existsQty > 0 && existsQty >= goodsOutStock.getQuantity()) {
							goodsOutStock.setBusinessStatus(9);
							goodsOutStock.setConfirmBy(actorId);
							goodsOutStock.setConfirmTime(new Date());
							this.goodsOutStockService.updateGoodsOutStockStatus(goodsOutStock);
							return ResponseUtils.responseJsonResult(true);
						} else {
							return ResponseUtils.responseJsonResult(false,
									"出库申请数量" + goodsOutStock.getQuantity() + "已经超过库存数量" + existsQty + "，不能出库。");
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
		GoodsPlanQuantityQuery query = new GoodsPlanQuantityQuery();
		query.tenantId(loginContext.getTenantId());
		query.fullDay(DateUtils.getYearMonthDay(date));
		List<GoodsPlanQuantity> planList = goodsPlanQuantityService.list(query);

		GoodsOutStockQuery query2 = new GoodsOutStockQuery();
		query2.tenantId(loginContext.getTenantId());
		query2.fullDay(DateUtils.getYearMonthDay(date));
		query2.businessStatus(0);
		List<GoodsOutStock> goodsList = goodsOutStockService.list(query2);

		GoodsStockQuery query3 = new GoodsStockQuery();
		query3.tenantId(loginContext.getTenantId());
		query3.quantityGreaterThan(0D);
		List<GoodsStock> stocklist = goodsStockService.list(query3);

		Map<Long, Double> qtyMap = new HashMap<Long, Double>();
		if (goodsList != null && !goodsList.isEmpty()) {
			for (GoodsOutStock model : goodsList) {
				qtyMap.put(model.getGoodsId(), model.getQuantity());
			}
		}

		Map<Long, Double> stockQtyMap = new HashMap<Long, Double>();
		if (stocklist != null && !stocklist.isEmpty()) {
			for (GoodsStock model : stocklist) {
				stockQtyMap.put(model.getGoodsId(), model.getQuantity());
			}
		}

		if (planList != null && !planList.isEmpty()) {
			for (GoodsPlanQuantity model : planList) {
				if (qtyMap.get(model.getGoodsId()) != null) {
					model.setRealQuantity(qtyMap.get(model.getGoodsId()));
				}
				if (stockQtyMap.get(model.getGoodsId()) != null) {
					model.setStockQuantity(stockQtyMap.get(model.getGoodsId()));
				}
			}
			request.setAttribute("planList", planList);
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

		String x_view = ViewProperties.getString("goodsOutStock.batchEdit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/goodsOutStock/batchEdit", modelMap);
	}

	@RequestMapping("/calStockJson")
	@ResponseBody
	public byte[] calStockJson(HttpServletRequest request) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		JSONObject result = new JSONObject();
		JSONArray rowsJSON = new JSONArray();
		result.put("rows", rowsJSON);
		result.put("total", 0);
		long nodeId = RequestUtils.getLong(request, "nodeId");
		try {
			List<FoodComposition> list = null;
			if (nodeId > 0) {
				list = foodCompositionService.getFoodCompositions(nodeId);
			} else {
				FoodCompositionQuery query = new FoodCompositionQuery();
				list = foodCompositionService.list(query);
			}
			if (list != null && !list.isEmpty()) {
				GoodsInStockQuery query1 = new GoodsInStockQuery();
				query1.tenantId(loginContext.getTenantId());
				query1.businessStatus(9);// 已经确定入库的物品
				List<GoodsInStock> goods1 = goodsInStockService.list(query1);

				GoodsOutStockQuery query2 = new GoodsOutStockQuery();
				query2.tenantId(loginContext.getTenantId());
				query2.businessStatus(9);// 已经确定出库的物品
				List<GoodsOutStock> goods2 = goodsOutStockService.list(query2);

				if (goods1 != null && !goods1.isEmpty()) {
					Map<Long, Double> goods1Map = new HashMap<Long, Double>();
					Map<Long, String> goods1UnitMap = new HashMap<Long, String>();
					Map<Long, Date> goods1DateMap = new HashMap<Long, Date>();
					Map<Long, Double> goods2Map = new HashMap<Long, Double>();
					for (GoodsInStock model : goods1) {
						Double value = goods1Map.get(model.getGoodsId());
						if (value == null) {
							value = new Double(0.0D);
						}
						value = value + model.getQuantity();
						goods1Map.put(model.getGoodsId(), value);
						goods1UnitMap.put(model.getGoodsId(), model.getUnit());
						if (model.getExpiryDate() != null) {
							Date date = goods1DateMap.get(model.getGoodsId());
							if (date != null) {
								if (model.getExpiryDate().getTime() < date.getTime()) {
									goods1DateMap.put(model.getGoodsId(), model.getExpiryDate());
								}
							} else {
								goods1DateMap.put(model.getGoodsId(), model.getExpiryDate());
							}
						}
					}
					if (goods2 != null && !goods2.isEmpty()) {
						for (GoodsOutStock model : goods2) {
							Double value = goods2Map.get(model.getGoodsId());
							if (value == null) {
								value = new Double(0.0D);
							}
							value = value + model.getQuantity();
							goods2Map.put(model.getGoodsId(), value);
						}
					}
					List<FoodComposition> exists = new ArrayList<FoodComposition>();
					for (FoodComposition fd : list) {
						if (StringUtils.equals(fd.getDailyFlag(), "Y")) {
							continue;// 每日采购不入库房,所以为0库存
						}
						Double value = goods1Map.get(fd.getId());
						if (value != null) {
							Double v2 = goods2Map.get(fd.getId());
							if (v2 != null) {
								if (value - v2 == 0) {
									continue;
								}
								fd.setQuantity(value - v2);
							} else {
								fd.setQuantity(value);
							}
							exists.add(fd);
						}
					}

					if (!exists.isEmpty()) {
						int startIndex = 0;
						Date date = null;
						for (FoodComposition foodComposition : exists) {
							JSONObject rowJSON = foodComposition.toJsonObject();
							rowJSON.put("startIndex", ++startIndex);
							rowJSON.put("id", foodComposition.getId());
							rowJSON.put("rowId", foodComposition.getId());
							rowJSON.put("foodCompositionId", foodComposition.getId());
							rowJSON.put("quantity", foodComposition.getQuantity());
							rowJSON.put("unit", goods1UnitMap.get(foodComposition.getId()));
							date = goods1DateMap.get(foodComposition.getId());
							if (date != null) {
								rowJSON.put("expiryDate", DateUtils.getDate(date));
							}
							rowsJSON.add(rowJSON);
						}
						result.put("rows", rowsJSON);
						result.put("total", exists.size());
					}
				}
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
			logger.error(ex);
		}

		return result.toJSONString().getBytes("UTF-8");
	}

	@ResponseBody
	@RequestMapping("/copyInStock")
	public byte[] copyInStock(HttpServletRequest request) {
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
			goodsOutStockService.copyInStock(loginContext.getTenantId(), fullDay, actorId);
			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			ex.printStackTrace();
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
				return ResponseUtils.responseJsonResult(false, "采购日期必须是当天或之前的日期。");
			}
		}
		int fullDay = DateUtils.getYearMonthDay(dateTime);
		try {
			goodsOutStockService.copyPurchase(loginContext.getTenantId(), fullDay, actorId);
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
					GoodsOutStock goodsOutStock = goodsOutStockService.getGoodsOutStock(loginContext.getTenantId(),
							Long.valueOf(x));
					if (goodsOutStock != null && ((loginContext.getRoles().contains("TenantAdmin")
							|| loginContext.getRoles().contains("Buyer")
							|| loginContext.getRoles().contains("StockManager"))
							&& StringUtils.equals(loginContext.getTenantId(), goodsOutStock.getTenantId()))) {
						if (goodsOutStock.getBusinessStatus() == 0) {
							goodsOutStockService.deleteById(loginContext.getTenantId(), goodsOutStock.getId());
						}
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			GoodsOutStock goodsOutStock = goodsOutStockService.getGoodsOutStock(loginContext.getTenantId(),
					Long.valueOf(id));
			if (goodsOutStock != null && ((loginContext.getRoles().contains("TenantAdmin")
					|| loginContext.getRoles().contains("Buyer") || loginContext.getRoles().contains("StockManager"))
					&& StringUtils.equals(loginContext.getTenantId(), goodsOutStock.getTenantId()))) {
				if (goodsOutStock.getBusinessStatus() == 0) {
					goodsOutStockService.deleteById(loginContext.getTenantId(), goodsOutStock.getId());
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
		GoodsOutStock goodsOutStock = goodsOutStockService.getGoodsOutStock(loginContext.getTenantId(),
				RequestUtils.getLong(request, "id"));
		if (goodsOutStock != null) {
			request.setAttribute("goodsOutStock", goodsOutStock);
			if (goodsOutStock.getGoodsId() > 0) {
				FoodComposition foodComposition = foodCompositionService.getFoodComposition(goodsOutStock.getGoodsId());
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

		String x_view = ViewProperties.getString("goodsOutStock.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/goodsOutStock/edit", modelMap);
	}

	@ResponseBody
	@RequestMapping("/goodsJson")
	public byte[] goodsJson(HttpServletRequest request) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		JSONObject result = new JSONObject();
		if (loginContext.isSystemAdministrator() || loginContext.getRoles().contains("TenantAdmin")
				|| loginContext.getRoles().contains("StockManager")) {
			long nodeId = RequestUtils.getLong(request, "nodeId");
			try {
				if (nodeId > 0) {
					List<FoodComposition> list = foodCompositionService.getFoodCompositions(nodeId);
					if (list != null && !list.isEmpty()) {
						GoodsInStockQuery query1 = new GoodsInStockQuery();
						query1.tenantId(loginContext.getTenantId());
						query1.businessStatus(9);// 已经确定入库的物品
						List<GoodsInStock> goods1 = goodsInStockService.list(query1);

						GoodsOutStockQuery query2 = new GoodsOutStockQuery();
						query2.tenantId(loginContext.getTenantId());
						query2.businessStatus(9);// 已经确定出库的物品
						List<GoodsOutStock> goods2 = goodsOutStockService.list(query2);

						if (goods1 != null && !goods1.isEmpty()) {
							Map<Long, Double> goods1Map = new HashMap<Long, Double>();
							Map<Long, Double> goods2Map = new HashMap<Long, Double>();
							for (GoodsInStock model : goods1) {
								Double value = goods1Map.get(model.getGoodsId());
								if (value == null) {
									value = new Double(0.0D);
								}
								value = value + model.getQuantity();
								goods1Map.put(model.getGoodsId(), value);
							}
							if (goods2 != null && !goods2.isEmpty()) {
								for (GoodsOutStock model : goods2) {
									Double value = goods2Map.get(model.getGoodsId());
									if (value == null) {
										value = new Double(0.0D);
									}
									value = value + model.getQuantity();
									goods2Map.put(model.getGoodsId(), value);
								}
							}
							List<FoodComposition> exists = new ArrayList<FoodComposition>();
							for (FoodComposition fd : list) {
								Double value = goods1Map.get(fd.getId());
								if (value != null) {
									Double v2 = goods2Map.get(fd.getId());
									if (v2 != null) {
										if (value - v2 == 0) {
											continue;
										}
										fd.setQuantity(value - v2);
									} else {
										fd.setQuantity(value);
									}
									exists.add(fd);
								}
							}

							if (!exists.isEmpty()) {
								JSONArray rowsJSON = new JSONArray();
								for (FoodComposition foodComposition : exists) {
									JSONObject rowJSON = foodComposition.toJsonObject();
									rowJSON.put("id", foodComposition.getId());
									rowJSON.put("rowId", foodComposition.getId());
									rowJSON.put("foodCompositionId", foodComposition.getId());
									rowJSON.put("quantity", foodComposition.getQuantity());
									rowsJSON.add(rowJSON);
								}
								result.put("rows", rowsJSON);
							}
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		return result.toJSONString().getBytes("UTF-8");
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		JSONObject result = new JSONObject();
		GoodsOutStockQuery query = new GoodsOutStockQuery();
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

		Date outStockTime = RequestUtils.getDate(request, "outStockTime");
		if (outStockTime != null) {
			query.fullDay(DateUtils.getYearMonthDay(outStockTime));
		}

		Date outStockTime_start = RequestUtils.getDate(request, "startTime");
		if (outStockTime_start != null) {
			query.outStockTimeGreaterThanOrEqual(outStockTime_start);
		}

		Date outStockTime_end = RequestUtils.getEndDate(request, "endTime");
		if (outStockTime_end != null) {
			query.outStockTimeLessThanOrEqual(outStockTime_end);
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

		int total = goodsOutStockService.getGoodsOutStockCountByQueryCriteria(query);
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

			List<GoodsOutStock> list = goodsOutStockService.getGoodsOutStocksByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				Map<String, User> userMap = IdentityFactory.getUserMap(loginContext.getTenantId());

				for (GoodsOutStock goodsOutStock : list) {
					JSONObject rowJSON = goodsOutStock.toJsonObject();
					rowJSON.put("id", goodsOutStock.getId());
					rowJSON.put("rowId", goodsOutStock.getId());
					rowJSON.put("goodsOutStockId", goodsOutStock.getId());
					rowJSON.put("startIndex", ++start);
					User user = userMap.get(goodsOutStock.getConfirmBy());
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

		return new ModelAndView("/heathcare/goodsOutStock/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("goodsOutStock.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/heathcare/goodsOutStock/query", modelMap);
	}

	@RequestMapping("/reviewJson")
	@ResponseBody
	public byte[] reviewJson(HttpServletRequest request, ModelMap modelMap) throws IOException {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		JSONObject result = new JSONObject();
		GoodsOutStockQuery query = new GoodsOutStockQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		String tenantId = request.getParameter("tenantId");
		query.tenantId(tenantId);

		Date outStockTime = RequestUtils.getDate(request, "outStockTime");
		if (outStockTime != null) {
			query.fullDay(DateUtils.getYearMonthDay(outStockTime));
		}

		Date outStockTime_start = RequestUtils.getDate(request, "startTime");
		if (outStockTime_start != null) {
			query.outStockTimeGreaterThanOrEqual(outStockTime_start);
		}

		Date outStockTime_end = RequestUtils.getEndDate(request, "endTime");
		if (outStockTime_end != null) {
			query.outStockTimeLessThanOrEqual(outStockTime_end);
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

		int total = goodsOutStockService.getGoodsOutStockCountByQueryCriteria(query);
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

			List<GoodsOutStock> list = goodsOutStockService.getGoodsOutStocksByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				Map<String, User> userMap = IdentityFactory.getUserMap(tenantId);

				for (GoodsOutStock goodsOutStock : list) {
					JSONObject rowJSON = goodsOutStock.toJsonObject();
					rowJSON.put("id", goodsOutStock.getId());
					rowJSON.put("rowId", goodsOutStock.getId());
					rowJSON.put("goodsOutStockId", goodsOutStock.getId());
					rowJSON.put("startIndex", ++start);
					User user = userMap.get(goodsOutStock.getConfirmBy());
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

		return new ModelAndView("/heathcare/goodsOutStock/review_list", modelMap);
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

		GoodsOutStockQuery query2 = new GoodsOutStockQuery();
		query2.tenantId(loginContext.getTenantId());
		query2.fullDay(DateUtils.getYearMonthDay(date));
		query2.businessStatus(0);

		List<GoodsOutStock> distList = new ArrayList<GoodsOutStock>();

		List<GoodsOutStock> goodsList = goodsOutStockService.list(query2);
		Map<Long, Double> qtyMap = new HashMap<Long, Double>();
		if (goodsList != null && !goodsList.isEmpty()) {
			for (GoodsOutStock model : goodsList) {
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

		List<GoodsPlanQuantity> planList = goodsPlanQuantityService.list(query);
		if (planList != null && !planList.isEmpty()) {
			for (GoodsPlanQuantity model : planList) {
				if (qtyMap.get(model.getGoodsId()) == null) {
					double qty = RequestUtils.getDouble(request, "qty_" + model.getGoodsId());
					if (qty > 0) {
						GoodsOutStock m = new GoodsOutStock();
						m.setBusinessStatus(0);
						m.setOutStockTime(date);
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
			goodsOutStockService.saveAll(distList);
			return ResponseUtils.responseJsonResult(true);
		}

		return ResponseUtils.responseJsonResult(false);
	}

	@ResponseBody
	@RequestMapping("/saveGoodsOutStock")
	public byte[] saveGoodsOutStock(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		if (loginContext.getRoles().contains("TenantAdmin") || loginContext.getRoles().contains("Buyer")
				|| loginContext.getRoles().contains("StockManager")) {
			long id = RequestUtils.getLong(request, "id");
			long goodsId = RequestUtils.getLong(request, "goodsId");
			double quantity = RequestUtils.getDouble(request, "quantity");
			if (quantity <= 0 || quantity > 5000) {
				return ResponseUtils.responseJsonResult(false, "出库数量必须大于0小于5000。");
			}
			Date outStockTime = RequestUtils.getDate(request, "outStockTime");
			if (outStockTime != null) {
				if (loginContext.getLimit() != Constants.UNLIMIT) {
					if (DateUtils.getYearMonthDay(outStockTime) > DateUtils.getNowYearMonthDay()) {
						return ResponseUtils.responseJsonResult(false, "出库数据只能是当天及之前。");
					}
					if ((DateUtils.getDaysBetween(outStockTime, new Date())) > 30) {
						return ResponseUtils.responseJsonResult(false, "出库时间只能在一月以内。");
					}
				}
			} else {
				if (outStockTime == null) {
					outStockTime = new Date();// 不输入默认为当天日期
				}
			}
			GoodsOutStock goodsOutStock = null;
			try {
				if (id > 0) {
					goodsOutStock = goodsOutStockService.getGoodsOutStock(loginContext.getTenantId(), id);
				}
				if (goodsOutStock == null) {
					goodsOutStock = new GoodsOutStock();
				} else {
					if (!StringUtils.equals(loginContext.getTenantId(), goodsOutStock.getTenantId())) {
						return ResponseUtils.responseJsonResult(false, "出库数据只能所属机构修改。");
					}
					if (loginContext.getLimit() != Constants.UNLIMIT) {
						if (DateUtils.getYearMonthDay(goodsOutStock.getOutStockTime()) != DateUtils
								.getNowYearMonthDay()) {
							return ResponseUtils.responseJsonResult(false, "出库数据只能当天修改。");
						}
					}
				}

				if (goodsId > 0) {
					FoodComposition fd = foodCompositionService.getFoodComposition(goodsId);
					if (fd != null) {
						goodsOutStock.setGoodsId(goodsId);
						goodsOutStock.setGoodsName(fd.getName());
						goodsOutStock.setGoodsNodeId(fd.getNodeId());

						GoodsInStockQuery query1 = new GoodsInStockQuery();
						query1.tenantId(loginContext.getTenantId());
						query1.businessStatus(9);// 已经确定入库的物品
						query1.setGoodsId(goodsOutStock.getGoodsId());
						List<GoodsInStock> goods1 = goodsInStockService.list(query1);

						GoodsOutStockQuery query2 = new GoodsOutStockQuery();
						query2.tenantId(loginContext.getTenantId());
						query2.businessStatus(9);// 已经确定出库的物品
						query2.setGoodsId(goodsOutStock.getGoodsId());
						List<GoodsOutStock> goods2 = goodsOutStockService.list(query2);

						if (goods1 != null && !goods1.isEmpty()) {
							double existsQty = 0.0;
							for (GoodsInStock model : goods1) {
								existsQty = existsQty + model.getQuantity();
							}
							if (goods2 != null && !goods2.isEmpty()) {
								for (GoodsOutStock model : goods2) {
									existsQty = existsQty - model.getQuantity();
								}
							}
							logger.debug("existsQty:" + existsQty);
							if (existsQty > 0 && existsQty >= RequestUtils.getDouble(request, "quantity")) {
								Tools.populate(goodsOutStock, params);
								goodsOutStock.setSemester(SysConfig.getSemester());
								goodsOutStock.setTenantId(loginContext.getTenantId());
								goodsOutStock.setGoodsId(RequestUtils.getLong(request, "goodsId"));
								goodsOutStock.setOutStockTime(outStockTime);
								goodsOutStock.setQuantity(quantity);
								goodsOutStock.setUnit(request.getParameter("unit"));
								goodsOutStock.setReceiverId(request.getParameter("receiverId"));
								goodsOutStock.setReceiverName(request.getParameter("receiverName"));
								goodsOutStock.setRemark(request.getParameter("remark"));
								goodsOutStock.setCreateBy(actorId);
								this.goodsOutStockService.save(goodsOutStock);
								return ResponseUtils.responseJsonResult(true);
							} else {
								logger.debug("库存不足，数量不能超过" + existsQty);
								return ResponseUtils.responseJsonResult(false, "库存不足，数量不能超过" + existsQty);
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

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.goodsInStockService")
	public void setGoodsInStockService(GoodsInStockService goodsInStockService) {
		this.goodsInStockService = goodsInStockService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.goodsOutStockService")
	public void setGoodsOutStockService(GoodsOutStockService goodsOutStockService) {
		this.goodsOutStockService = goodsOutStockService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.goodsPlanQuantityService")
	public void setGoodsPlanQuantityService(GoodsPlanQuantityService goodsPlanQuantityService) {
		this.goodsPlanQuantityService = goodsPlanQuantityService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.goodsStockService")
	public void setGoodsStockService(GoodsStockService goodsStockService) {
		this.goodsStockService = goodsStockService;
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

		String x_view = ViewProperties.getString("goodsOutStock.copy");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/goodsOutStock/copy", modelMap);
	}

	@RequestMapping("/showCopy2")
	public ModelAndView showCopy2(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("goodsOutStock.copy2");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/goodsOutStock/copy2", modelMap);
	}

	@RequestMapping("/stockJson")
	@ResponseBody
	public byte[] stockJson(HttpServletRequest request) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		JSONObject result = new JSONObject();
		JSONArray rowsJSON = new JSONArray();
		result.put("rows", rowsJSON);
		result.put("total", 0);
		long nodeId = RequestUtils.getLong(request, "nodeId");
		GoodsStockQuery query = new GoodsStockQuery();
		query.tenantId(loginContext.getTenantId());
		if (nodeId > 0) {
			query.goodsNodeId(nodeId);
		}
		query.setQuantityGreaterThan(0D);

		int start = 0;
		int limit = 1000;
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
			limit = 1000;
		}

		try {
			int total = goodsStockService.getGoodsStockCountByQueryCriteria(query);
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

				List<GoodsStock> list = goodsStockService.getGoodsStocksByQueryCriteria(start, limit, query);

				if (list != null && !list.isEmpty()) {

					result.put("rows", rowsJSON);

					for (GoodsStock goodsStock : list) {
						JSONObject rowJSON = goodsStock.toJsonObject();
						rowJSON.put("id", goodsStock.getId());
						rowJSON.put("name", goodsStock.getGoodsName());
						rowJSON.put("nodeId", goodsStock.getGoodsNodeId());
						rowJSON.put("foodCompositionId", goodsStock.getGoodsId());
						rowJSON.put("startIndex", ++start);
						rowsJSON.add(rowJSON);
					}

				}
			} else {
				result.put("rows", rowsJSON);
				result.put("total", total);
			}
			return result.toJSONString().getBytes("UTF-8");
		} catch (Exception ex) {
			// ex.printStackTrace();
			logger.error(ex);
		}

		return result.toJSONString().getBytes("UTF-8");
	}

	@RequestMapping("/stocklist")
	public ModelAndView stocklist(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		SysTree root = sysTreeService.getSysTreeByCode("FoodCategory");
		if (root != null) {
			List<SysTree> foodCategories = sysTreeService.getSysTreeList(root.getId());
			request.setAttribute("foodCategories", foodCategories);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/goodsOutStock/stocklist", modelMap);
	}

	@RequestMapping("/view")
	public ModelAndView view(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		GoodsOutStock goodsOutStock = goodsOutStockService.getGoodsOutStock(loginContext.getTenantId(),
				RequestUtils.getLong(request, "id"));
		request.setAttribute("goodsOutStock", goodsOutStock);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view);
		}

		String x_view = ViewProperties.getString("goodsOutStock.view");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view);
		}

		return new ModelAndView("/heathcare/goodsOutStock/view");
	}

}
