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
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;

import com.glaf.heathcare.SysConfig;
import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.domain.GoodsPlanQuantity;
import com.glaf.heathcare.query.GoodsPlanQuantityQuery;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.heathcare.service.GoodsPlanQuantityService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/goodsPlanQuantity")
@RequestMapping("/heathcare/goodsPlanQuantity")
public class GoodsPlanQuantityController {
	protected static final Log logger = LogFactory.getLog(GoodsPlanQuantityController.class);

	protected FoodCompositionService foodCompositionService;

	protected GoodsPlanQuantityService goodsPlanQuantityService;

	protected SysTreeService sysTreeService;

	public GoodsPlanQuantityController() {

	}

	@ResponseBody
	@RequestMapping("/audit")
	public byte[] audit(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.getRoles().contains("TenantAdmin") || loginContext.getRoles().contains("Kitchen")) {
			long id = RequestUtils.getLong(request, "id");
			GoodsPlanQuantity goodsPlanQuantity = null;
			try {
				if (id > 0) {
					goodsPlanQuantity = goodsPlanQuantityService.getGoodsPlanQuantity(loginContext.getTenantId(), id);
				}
				if (goodsPlanQuantity != null) {
					if (!StringUtils.equals(loginContext.getTenantId(), goodsPlanQuantity.getTenantId())) {
						return ResponseUtils.responseJsonResult(false, "数据只能所属机构修改。");
					}
					goodsPlanQuantity.setBusinessStatus(9);
					goodsPlanQuantity.setConfirmBy(loginContext.getActorId());
					goodsPlanQuantity.setConfirmTime(new Date());
					// this.goodsPlanQuantityService.updateGoodsPlanQuantityStatus(goodsPlanQuantity);
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
					GoodsPlanQuantity goodsPlanQuantity = goodsPlanQuantityService
							.getGoodsPlanQuantity(loginContext.getTenantId(), Long.valueOf(x));
					if (goodsPlanQuantity != null && ((loginContext.getRoles().contains("Kitchen")
							|| loginContext.getRoles().contains("TenantAdmin"))
							&& StringUtils.equals(goodsPlanQuantity.getTenantId(), loginContext.getTenantId()))) {
						if (goodsPlanQuantity.getBusinessStatus() == 0
								&& !StringUtils.equals(goodsPlanQuantity.getSysFlag(), "Y")) {
							goodsPlanQuantityService.deleteById(loginContext.getTenantId(), goodsPlanQuantity.getId());
						}
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			GoodsPlanQuantity goodsPlanQuantity = goodsPlanQuantityService
					.getGoodsPlanQuantity(loginContext.getTenantId(), Long.valueOf(id));
			if (goodsPlanQuantity != null
					&& ((loginContext.getRoles().contains("Kitchen") || loginContext.getRoles().contains("TenantAdmin"))
							&& StringUtils.equals(goodsPlanQuantity.getTenantId(), loginContext.getTenantId()))) {
				if (goodsPlanQuantity.getBusinessStatus() == 0
						&& !StringUtils.equals(goodsPlanQuantity.getSysFlag(), "Y")) {
					goodsPlanQuantityService.deleteById(loginContext.getTenantId(), goodsPlanQuantity.getId());
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

		GoodsPlanQuantity goodsPlanQuantity = goodsPlanQuantityService.getGoodsPlanQuantity(loginContext.getTenantId(),
				RequestUtils.getLong(request, "id"));
		if (goodsPlanQuantity != null) {
			request.setAttribute("goodsPlanQuantity", goodsPlanQuantity);

			if (goodsPlanQuantity.getGoodsId() > 0) {
				FoodComposition foodComposition = foodCompositionService
						.getFoodComposition(goodsPlanQuantity.getGoodsId());
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

		String x_view = ViewProperties.getString("goodsPlanQuantity.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/goodsPlanQuantity/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug("params:" + params);

		JSONObject result = new JSONObject();

		GoodsPlanQuantityQuery query = new GoodsPlanQuantityQuery();
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

		Date usageTime = RequestUtils.getDate(request, "usageTime");
		if (usageTime != null) {
			query.fullDay(DateUtils.getYearMonthDay(usageTime));
		}

		Date usageTime_start = RequestUtils.getDate(request, "startTime");
		if (usageTime_start != null) {
			query.usageTimeGreaterThanOrEqual(usageTime_start);
		}

		Date usageTime_end = RequestUtils.getEndDate(request, "endTime");
		if (usageTime_end != null) {
			query.usageTimeLessThanOrEqual(usageTime_end);
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

		int total = goodsPlanQuantityService.getGoodsPlanQuantityCountByQueryCriteria(query);
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

			List<GoodsPlanQuantity> list = goodsPlanQuantityService.getGoodsPlanQuantitysByQueryCriteria(start, limit,
					query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				Map<String, User> userMap = IdentityFactory.getUserMap(loginContext.getTenantId());

				for (GoodsPlanQuantity goodsPlanQuantity : list) {
					JSONObject rowJSON = goodsPlanQuantity.toJsonObject();
					rowJSON.put("id", goodsPlanQuantity.getId());
					rowJSON.put("rowId", goodsPlanQuantity.getId());
					rowJSON.put("goodsPlanQuantityId", goodsPlanQuantity.getId());
					rowJSON.put("startIndex", ++start);
					if (goodsPlanQuantity.getQuantity() > 0) {
						rowJSON.put("quantity", Math.round(goodsPlanQuantity.getQuantity() * 10D) / 10D);
					}
					User user = userMap.get(goodsPlanQuantity.getConfirmBy());
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

		return new ModelAndView("/heathcare/goodsPlanQuantity/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("goodsPlanQuantity.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/heathcare/goodsPlanQuantity/query", modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveGoodsPlanQuantity")
	public byte[] saveGoodsPlanQuantity(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		if (loginContext.isSystemAdministrator() || loginContext.getRoles().contains("TenantAdmin")
				|| loginContext.getRoles().contains("Kitchen")) {
			long id = RequestUtils.getLong(request, "id");
			GoodsPlanQuantity goodsPlanQuantity = null;
			try {
				if (id > 0) {
					goodsPlanQuantity = goodsPlanQuantityService.getGoodsPlanQuantity(loginContext.getTenantId(), id);
				}
				if (goodsPlanQuantity == null) {
					goodsPlanQuantity = new GoodsPlanQuantity();
					goodsPlanQuantity.setTenantId(loginContext.getTenantId());
				} else {
					if (!StringUtils.equals(loginContext.getTenantId(), goodsPlanQuantity.getTenantId())) {
						return ResponseUtils.responseJsonResult(false, "数据只能所属机构修改。");
					}
					if (goodsPlanQuantity.getFullDay() != DateUtils.getNowYearMonthDay()) {
						return ResponseUtils.responseJsonResult(false, "实际使用量只能当天修改。");
					}
				}

				Tools.populate(goodsPlanQuantity, params);

				goodsPlanQuantity.setGoodsId(RequestUtils.getLong(request, "goodsId"));
				goodsPlanQuantity.setQuantity(RequestUtils.getDouble(request, "quantity"));
				goodsPlanQuantity.setUnit(request.getParameter("unit"));
				goodsPlanQuantity.setBusinessStatus(0);
				goodsPlanQuantity.setCreateBy(actorId);
				goodsPlanQuantity.setUsageTime(new Date());
				goodsPlanQuantity.setFullDay(DateUtils.getNowYearMonthDay());

				Calendar calendar = Calendar.getInstance();
				calendar.setTime(goodsPlanQuantity.getUsageTime());
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH) + 1;
				int day = calendar.get(Calendar.DAY_OF_MONTH);
				goodsPlanQuantity.setSemester(SysConfig.getSemester());
				goodsPlanQuantity.setYear(year);
				goodsPlanQuantity.setMonth(month);
				goodsPlanQuantity.setDay(day);

				if (goodsPlanQuantity.getGoodsId() > 0) {
					FoodComposition fd = foodCompositionService.getFoodComposition(goodsPlanQuantity.getGoodsId());
					goodsPlanQuantity.setGoodsName(fd.getName());
					goodsPlanQuantity.setGoodsNodeId(fd.getNodeId());
				}

				this.goodsPlanQuantityService.save(goodsPlanQuantity);

				return ResponseUtils.responseJsonResult(true);
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.foodCompositionService")
	public void setFoodCompositionService(FoodCompositionService foodCompositionService) {
		this.foodCompositionService = foodCompositionService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.goodsPlanQuantityService")
	public void setGoodsPlanQuantityService(GoodsPlanQuantityService goodsPlanQuantityService) {
		this.goodsPlanQuantityService = goodsPlanQuantityService;
	}

	@javax.annotation.Resource
	public void setSysTreeService(SysTreeService sysTreeService) {
		this.sysTreeService = sysTreeService;
	}

	@RequestMapping("/view")
	public ModelAndView view(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		GoodsPlanQuantity goodsPlanQuantity = goodsPlanQuantityService.getGoodsPlanQuantity(loginContext.getTenantId(),
				RequestUtils.getLong(request, "id"));
		request.setAttribute("goodsPlanQuantity", goodsPlanQuantity);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view);
		}

		String x_view = ViewProperties.getString("goodsPlanQuantity.view");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view);
		}

		return new ModelAndView("/heathcare/goodsPlanQuantity/view");
	}

}
