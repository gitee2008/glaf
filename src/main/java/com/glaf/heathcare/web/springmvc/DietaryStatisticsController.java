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
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

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

import com.glaf.core.security.LoginContext;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;
import com.glaf.heathcare.SysConfig;
import com.glaf.heathcare.bean.DayDietaryStatisticsBean;
import com.glaf.heathcare.bean.DietaryStatisticsBean;
import com.glaf.heathcare.domain.DietaryStatistics;
import com.glaf.heathcare.query.DietaryStatisticsQuery;
import com.glaf.heathcare.service.DietaryStatisticsService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/dietaryStatistics")
@RequestMapping("/heathcare/dietaryStatistics")
public class DietaryStatisticsController {
	protected static final Log logger = LogFactory.getLog(DietaryStatisticsController.class);

	protected static final Semaphore semaphore1 = new Semaphore(1);

	protected static final Semaphore semaphore2 = new Semaphore(20);

	protected DietaryStatisticsService dietaryStatisticsService;

	public DietaryStatisticsController() {

	}

	@RequestMapping("/confirm")
	public ModelAndView confirm(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		return new ModelAndView("/heathcare/statistics/confirm", modelMap);
	}

	@RequestMapping("/execute")
	@ResponseBody
	public byte[] execute(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		int suitNo = RequestUtils.getInt(request, "suitNo");
		int dayOfWeek = RequestUtils.getInt(request, "dayOfWeek");
		int fullDay = RequestUtils.getInt(request, "fullDay");
		int year = RequestUtils.getInt(request, "year");
		int week = RequestUtils.getInt(request, "week");

		logger.debug("启用并发访问控制,可用许可数:" + semaphore2.availablePermits());
		if (semaphore2.availablePermits() == 0) {
			return ResponseUtils.responseJsonResult(false, "计算任务繁忙，请稍候再试。");
		}
		try {
			semaphore2.acquire();
			if (loginContext.isSystemAdministrator()) {
				if (suitNo > 0 && dayOfWeek > 0) {
					String sysFlag = "Y";
					DietaryStatisticsBean bean = new DietaryStatisticsBean();
					bean.execute(loginContext, suitNo, sysFlag);
					bean.execute(loginContext, suitNo, dayOfWeek, sysFlag);

					return ResponseUtils.responseJsonResult(true);
				} else {
					if (suitNo > 0) {
						String sysFlag = "Y";
						DietaryStatisticsBean bean = new DietaryStatisticsBean();
						bean.execute(loginContext, suitNo, sysFlag);
						return ResponseUtils.responseJsonResult(true);
					}
				}
			} else {
				if (loginContext.getRoles().contains("HealthPhysician")
						|| loginContext.getRoles().contains("TenantAdmin")) {
					if (suitNo > 0 && dayOfWeek > 0) {
						String sysFlag = "N";
						DietaryStatisticsBean bean = new DietaryStatisticsBean();
						bean.execute(loginContext, suitNo, sysFlag);
						bean.execute(loginContext, suitNo, dayOfWeek, sysFlag);

						return ResponseUtils.responseJsonResult(true);
					} else {
						if (suitNo > 0) {
							String sysFlag = "N";
							DietaryStatisticsBean bean = new DietaryStatisticsBean();
							bean.execute(loginContext, suitNo, sysFlag);
							return ResponseUtils.responseJsonResult(true);
						}
						if (fullDay > 0) {
							DayDietaryStatisticsBean bean = new DayDietaryStatisticsBean();
							bean.execute(loginContext.getTenantId(), fullDay);
							return ResponseUtils.responseJsonResult(true);
						}
						if (year > 0 && week > 0) {
							int semester = SysConfig.getSemester();
							DayDietaryStatisticsBean bean = new DayDietaryStatisticsBean();
							bean.execute(loginContext.getTenantId(), year, semester, week);
							return ResponseUtils.responseJsonResult(true);
						}
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex);
		} finally {
			semaphore2.release();
		}

		return ResponseUtils.responseJsonResult(false);
	}

	@RequestMapping("/executeAll")
	@ResponseBody
	public byte[] executeAll(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isSystemAdministrator()) {
			logger.debug("启用并发访问控制,可用许可数:" + semaphore1.availablePermits());
			if (semaphore1.availablePermits() == 0) {
				return ResponseUtils.responseJsonResult(false, "任务执行中，请等待执行完成。");
			}
			try {
				semaphore1.acquire();
				DietaryStatisticsBean bean = new DietaryStatisticsBean();
				bean.executeAll();
				return ResponseUtils.responseJsonResult(true);
			} catch (Exception ex) {
				logger.error(ex);
			} finally {
				semaphore1.release();
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		DietaryStatisticsQuery query = new DietaryStatisticsQuery();
		Tools.populate(query, params);

		String sysFlag = request.getParameter("sysFlag");

		if (StringUtils.equals(sysFlag, "Y")) {
			query.sysFlag("Y");
		} else {
			query.sysFlag("N");
			query.tenantId(loginContext.getTenantId());
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
		int total = dietaryStatisticsService.getDietaryStatisticsCountByQueryCriteria(query);
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

			List<DietaryStatistics> list = dietaryStatisticsService.getDietaryStatisticssByQueryCriteria(start, limit,
					query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (DietaryStatistics dietaryStatistics : list) {
					JSONObject rowJSON = dietaryStatistics.toJsonObject();
					rowJSON.put("id", dietaryStatistics.getId());
					rowJSON.put("rowId", dietaryStatistics.getId());
					rowJSON.put("dietaryStatisticsId", dietaryStatistics.getId());
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

	@RequestMapping("/jsonArray")
	@ResponseBody
	public byte[] jsonArray(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		DietaryStatisticsQuery query = new DietaryStatisticsQuery();
		Tools.populate(query, params);

		String sysFlag = request.getParameter("sysFlag");

		if (StringUtils.equals(sysFlag, "Y")) {
			query.sysFlag("Y");
		} else {
			query.sysFlag("N");
			query.tenantId(loginContext.getTenantId());
		}

		JSONArray result = new JSONArray();
		List<DietaryStatistics> list = dietaryStatisticsService.list(query);
		if (list != null && !list.isEmpty()) {
			for (DietaryStatistics dietaryStatistics : list) {
				JSONObject json = new JSONObject();
				json.put("name", dietaryStatistics.getName());
				json.put("value", dietaryStatistics.getValue());
				result.add(json);
			}
		}
		return result.toJSONString().getBytes("UTF-8");
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.dietaryStatisticsService")
	public void setDietaryStatisticsService(DietaryStatisticsService dietaryStatisticsService) {
		this.dietaryStatisticsService = dietaryStatisticsService;
	}

}
