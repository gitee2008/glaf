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
import com.glaf.base.modules.sys.service.DictoryService;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;
import com.glaf.heathcare.SysConfig;
import com.glaf.heathcare.domain.MonthlyMealFee;
import com.glaf.heathcare.query.MonthlyMealFeeQuery;
import com.glaf.heathcare.service.MonthlyMealFeeService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/monthlyMealFee")
@RequestMapping("/heathcare/monthlyMealFee")
public class MonthlyMealFeeController {
	protected static final Log logger = LogFactory.getLog(MonthlyMealFeeController.class);

	protected DictoryService dictoryService;

	protected MonthlyMealFeeService monthlyMealFeeService;

	public MonthlyMealFeeController() {

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
					MonthlyMealFee monthlyMealFee = monthlyMealFeeService.getMonthlyMealFee(Long.valueOf(x));
					if (monthlyMealFee != null
							&& ((StringUtils.equals(monthlyMealFee.getTenantId(), loginContext.getTenantId())
									&& loginContext.getRoles().contains("TenantAdmin")))) {
						if (DateUtils.getYearMonth(monthlyMealFee.getCreateTime()) != DateUtils
								.getYearMonth(new Date())) {
							return ResponseUtils.responseJsonResult(false, "数据只能当月删除。");
						}
						monthlyMealFeeService.deleteById(monthlyMealFee.getId());
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			MonthlyMealFee monthlyMealFee = monthlyMealFeeService.getMonthlyMealFee(Long.valueOf(id));
			if (monthlyMealFee != null && ((StringUtils.equals(monthlyMealFee.getTenantId(), loginContext.getTenantId())
					&& loginContext.getRoles().contains("TenantAdmin")))) {
				if (DateUtils.getYearMonth(monthlyMealFee.getCreateTime()) != DateUtils.getYearMonth(new Date())) {
					return ResponseUtils.responseJsonResult(false, "数据只能当月删除。");
				}
				monthlyMealFeeService.deleteById(monthlyMealFee.getId());
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		MonthlyMealFee monthlyMealFee = monthlyMealFeeService.getMonthlyMealFee(RequestUtils.getLong(request, "id"));
		if (monthlyMealFee != null) {
			request.setAttribute("monthlyMealFee", monthlyMealFee);
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR);

		List<Integer> years = new ArrayList<Integer>();
		years.add(year);

		List<Integer> months = new ArrayList<Integer>();
		for (int i = 1; i <= 12; i++) {
			months.add(i);
		}

		request.setAttribute("years", years);
		request.setAttribute("months", months);

		List<Dictory> dictoryList = dictoryService.getDictoryList(5001L);// 5001是班级分类编号
		request.setAttribute("dictoryList", dictoryList);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("monthlyMealFee.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/monthlyMealFee/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		MonthlyMealFeeQuery query = new MonthlyMealFeeQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		if (!loginContext.isSystemAdministrator()) {
			query.tenantId(loginContext.getTenantId());
		} else {
			String tenantId = request.getParameter("tenantId");
			query.tenantId(tenantId);
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

		JSONObject result = new JSONObject();
		int total = monthlyMealFeeService.getMonthlyMealFeeCountByQueryCriteria(query);
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

			List<MonthlyMealFee> list = monthlyMealFeeService.getMonthlyMealFeesByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				List<Dictory> dictoryList = dictoryService.getDictoryList(5001L);// 5001是班级分类编号
				Map<String, String> nameMap = new HashMap<String, String>();
				if (dictoryList != null && !dictoryList.isEmpty()) {
					for (Dictory dict : dictoryList) {
						nameMap.put(dict.getCode(), dict.getName());
					}
				}
				for (MonthlyMealFee monthlyMealFee : list) {
					JSONObject rowJSON = monthlyMealFee.toJsonObject();
					rowJSON.put("id", monthlyMealFee.getId());
					rowJSON.put("rowId", monthlyMealFee.getId());
					rowJSON.put("monthlyMealFeeId", monthlyMealFee.getId());
					rowJSON.put("startIndex", ++start);
					if (nameMap.get(monthlyMealFee.getClassType()) != null) {
						rowJSON.put("classTypeName", nameMap.get(monthlyMealFee.getClassType()));
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

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR);

		List<Integer> years = new ArrayList<Integer>();
		years.add(year);

		List<Integer> months = new ArrayList<Integer>();
		for (int i = 1; i <= 12; i++) {
			months.add(i);
		}

		request.setAttribute("years", years);
		request.setAttribute("months", months);

		List<Dictory> dictoryList = dictoryService.getDictoryList(5001L);// 5001是班级分类编号
		request.setAttribute("dictoryList", dictoryList);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/monthlyMealFee/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("monthlyMealFee.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/heathcare/monthlyMealFee/query", modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveMonthlyMealFee")
	public byte[] saveMonthlyMealFee(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		String actorId = loginContext.getActorId();

		if (loginContext.isTenantAdmin()) {
			long id = RequestUtils.getLong(request, "id");
			MonthlyMealFee monthlyMealFee = null;
			try {
				if (id > 0) {
					monthlyMealFee = monthlyMealFeeService.getMonthlyMealFee(id);
				}
				if (monthlyMealFee == null) {
					MonthlyMealFeeQuery query = new MonthlyMealFeeQuery();
					query.tenantId(loginContext.getTenantId());
					query.setYear(RequestUtils.getInt(request, "year"));
					query.setMonth(RequestUtils.getInt(request, "month"));
					query.setClassType(request.getParameter("classType"));
					if (monthlyMealFeeService.getMonthlyMealFeeCountByQueryCriteria(query) > 0) {
						return ResponseUtils.responseJsonResult(false, "当月数据已经存在，请在已有数据上修改。");
					}
					monthlyMealFee = new MonthlyMealFee();
					monthlyMealFee.setCreateBy(actorId);
					monthlyMealFee.setTenantId(loginContext.getTenantId());
				} else {
					if (!StringUtils.equals(loginContext.getTenantId(), monthlyMealFee.getTenantId())) {
						return ResponseUtils.responseJsonResult(false, "数据只能所属机构修改。");
					}
					if (DateUtils.getYearMonth(monthlyMealFee.getCreateTime()) != DateUtils.getYearMonth(new Date())) {
						return ResponseUtils.responseJsonResult(false, "数据只能当月修改。");
					}
				}

				Tools.populate(monthlyMealFee, params);

				monthlyMealFee.setClassType(request.getParameter("classType"));
				monthlyMealFee.setReceiptFund(RequestUtils.getDouble(request, "receiptFund"));
				monthlyMealFee.setPersonTotal(RequestUtils.getInt(request, "personTotal"));
				monthlyMealFee.setAbsentPerson(RequestUtils.getInt(request, "absentPerson"));
				monthlyMealFee.setAbsentDay(RequestUtils.getInt(request, "absentDay"));
				monthlyMealFee.setAbsentRefund(RequestUtils.getDouble(request, "absentRefund"));
				monthlyMealFee.setYear(RequestUtils.getInt(request, "year"));
				monthlyMealFee.setMonth(RequestUtils.getInt(request, "month"));
				monthlyMealFee.setSemester(SysConfig.getSemester());
				monthlyMealFee.setUpdateBy(actorId);
				this.monthlyMealFeeService.save(monthlyMealFee);

				return ResponseUtils.responseJsonResult(true);
			} catch (Exception ex) {
				 ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource
	public void setDictoryService(DictoryService dictoryService) {
		this.dictoryService = dictoryService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.monthlyMealFeeService")
	public void setMonthlyMealFeeService(MonthlyMealFeeService monthlyMealFeeService) {
		this.monthlyMealFeeService = monthlyMealFeeService;
	}

}
