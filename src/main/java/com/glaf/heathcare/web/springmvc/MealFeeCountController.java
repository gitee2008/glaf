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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.jxls.common.Context;
import org.jxls.transform.poi.PoiTransformer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.glaf.base.modules.sys.model.SysTenant;
import com.glaf.base.modules.sys.model.TenantConfig;
import com.glaf.base.modules.sys.service.SysTenantService;
import com.glaf.base.modules.sys.service.TenantConfigService;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.heathcare.bean.GoodsMonthCostBean;
import com.glaf.heathcare.domain.MealFeeCount;
import com.glaf.heathcare.domain.MonthlyFee;
import com.glaf.heathcare.domain.MonthlyMealFee;
import com.glaf.heathcare.query.MealFeeCountQuery;
import com.glaf.heathcare.service.MealFeeCountService;
import com.glaf.heathcare.service.MonthlyFeeService;
import com.glaf.heathcare.service.MonthlyMealFeeService;
import com.glaf.report.bean.ReportContainer;
import com.glaf.report.data.ReportDefinition;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/mealFeeCount")
@RequestMapping("/heathcare/mealFeeCount")
public class MealFeeCountController {
	protected static final Log logger = LogFactory.getLog(MealFeeCountController.class);

	protected MealFeeCountService mealFeeCountService;

	protected MonthlyFeeService monthlyFeeService;

	protected MonthlyMealFeeService monthlyMealFeeService;

	protected SysTenantService sysTenantService;

	protected TenantConfigService tenantConfigService;

	public MealFeeCountController() {

	}

	@ResponseBody
	@RequestMapping("/exportXls")
	public void exportXls(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String tenantId = request.getParameter("tenantId");
		if (StringUtils.isEmpty(tenantId)) {
			tenantId = loginContext.getTenantId();
		}
		params.put("tenantId", tenantId);
		params.put("tableSuffix", IdentityFactory.getTenantHash(tenantId));
		int year = RequestUtils.getInt(request, "year");
		int month = RequestUtils.getInt(request, "month");

		TenantConfig cfg = tenantConfigService.getTenantConfigByTenantId(tenantId);
		if (cfg != null) {
			params.put("sysName", cfg.getSysName());
		}

		SysTenant tenant = sysTenantService.getSysTenantByTenantId(tenantId);
		params.put("orgName", tenant.getName());
		params.put("year", year);
		params.put("month", month);

		// MealFeeCountBean countBean = new MealFeeCountBean();
		// countBean.execute(loginContext, "HEALTH_MEAL_FEE_COUNT", "ALL", year,
		// SysConfig.getSemester(month), month, params);

		GoodsMonthCostBean costBean = new GoodsMonthCostBean();
		costBean.execute(tenantId, year, month);

		MealFeeCountQuery query = new MealFeeCountQuery();
		query.tenantId(tenantId);
		query.year(year);
		query.month(month);

		List<MealFeeCount> list = mealFeeCountService.list(query);
		if (list != null && !list.isEmpty()) {
			for (MealFeeCount cnt : list) {
				params.put(cnt.getName(), cnt.getValue());
			}
		}

		MonthlyFee monthlyFee = monthlyFeeService.getMonthlyFee(tenantId, year, month);
		if (monthlyFee != null) {
			JSONObject json = monthlyFee.toJsonObject();
			Iterator<Entry<String, Object>> iterator = json.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> entry = iterator.next();
				String key = (String) entry.getKey();
				Object value = entry.getValue();
				if (value != null) {
					params.put(key, value);
					logger.debug(key + ": " + value);
				}
			}
		}

		List<MonthlyMealFee> rows = monthlyMealFeeService.getMonthlyMealFees(tenantId, year, month);
		if (rows != null && !rows.isEmpty()) {
			for (MonthlyMealFee fee : rows) {
				JSONObject json = fee.toJsonObject();
				Iterator<Entry<String, Object>> iterator = json.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<String, Object> entry = iterator.next();
					String key = (String) entry.getKey();
					Object value = entry.getValue();
					if (value != null) {
						params.put(fee.getClassType() + "_" + key, value);
						logger.debug(fee.getClassType() + "_" + key + ": " + value);
					}
				}
			}
		}

		logger.debug("params:" + params);

		byte[] data = null;
		ReportDefinition rdf = ReportContainer.getContainer().getReportDefinition("rpt_month_fee");
		data = rdf.getData();
		if (data != null) {
			Workbook workbook = null;
			InputStream is = null;
			ByteArrayInputStream bais = null;
			ByteArrayOutputStream baos = null;
			BufferedOutputStream bos = null;
			try {
				bais = new ByteArrayInputStream(data);
				is = new BufferedInputStream(bais);
				baos = new ByteArrayOutputStream();
				bos = new BufferedOutputStream(baos);

				Context context2 = PoiTransformer.createInitialContext();

				Set<Entry<String, Object>> entrySet = params.entrySet();
				for (Entry<String, Object> entry : entrySet) {
					String key = entry.getKey();
					Object value = entry.getValue();
					context2.putVar(key, value);
				}

				org.jxls.util.JxlsHelper.getInstance().processTemplate(is, bos, context2);

				bos.flush();
				baos.flush();
				data = baos.toByteArray();
				ResponseUtils.download(request, response, data,
						"export" + DateUtils.getNowYearMonthDayHHmmss() + ".xls");
			} catch (Exception ex) {
				// ex.printStackTrace();
				logger.error(ex);
			} finally {
				data = null;
				IOUtils.closeQuietly(is);
				IOUtils.closeQuietly(bais);
				IOUtils.closeQuietly(baos);
				IOUtils.closeQuietly(bos);
				if (workbook != null) {
					try {
						workbook.close();
					} catch (IOException e) {
					}
					workbook = null;
				}
			}
		}
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.mealFeeCountService")
	public void setMealFeeCountService(MealFeeCountService mealFeeCountService) {
		this.mealFeeCountService = mealFeeCountService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.monthlyFeeService")
	public void setMonthlyFeeService(MonthlyFeeService monthlyFeeService) {
		this.monthlyFeeService = monthlyFeeService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.monthlyMealFeeService")
	public void setMonthlyMealFeeService(MonthlyMealFeeService monthlyMealFeeService) {
		this.monthlyMealFeeService = monthlyMealFeeService;
	}

	@javax.annotation.Resource
	public void setSysTenantService(SysTenantService sysTenantService) {
		this.sysTenantService = sysTenantService;
	}

	@javax.annotation.Resource
	public void setTenantConfigService(TenantConfigService tenantConfigService) {
		this.tenantConfigService = tenantConfigService;
	}

	@RequestMapping("/showExport")
	public ModelAndView showExport(HttpServletRequest request, ModelMap modelMap) {
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

		return new ModelAndView("/heathcare/mealFeeCount/showExport", modelMap);
	}

}
