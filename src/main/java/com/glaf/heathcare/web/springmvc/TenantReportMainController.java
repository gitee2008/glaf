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

import com.glaf.base.district.domain.District;
import com.glaf.base.district.service.DistrictService;
import com.glaf.base.modules.sys.model.SysTenant;
import com.glaf.base.modules.sys.model.TenantConfig;
import com.glaf.base.modules.sys.service.SysTenantService;
import com.glaf.base.modules.sys.service.TenantConfigService;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;

import com.glaf.heathcare.tenant.ITenantReportPreprocessor;
import com.glaf.report.bean.ReportContainer;
import com.glaf.report.data.ReportDefinition;

@Controller("/heathcare/tenantReportMain")
@RequestMapping("/heathcare/tenantReportMain")
public class TenantReportMainController {

	protected static final Log logger = LogFactory.getLog(TenantReportMainController.class);

	protected DistrictService districtService;

	protected SysTenantService sysTenantService;

	protected TenantConfigService tenantConfigService;

	@ResponseBody
	@RequestMapping("/exportXls")
	public void exportXls(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		String tenantId = request.getParameter("tenantId");
		params.put("tenantId", tenantId);
		params.put("tableSuffix", IdentityFactory.getTenantHash(tenantId));
		int year = RequestUtils.getInt(request, "year");
		int month = RequestUtils.getInt(request, "month");

		TenantConfig tenantConfig = tenantConfigService.getTenantConfigByTenantId(tenantId);
		if (tenantConfig != null) {
			params.put("tenantConfig", tenantConfig);
			params.put("sysName", tenantConfig.getSysName());
		}

		SysTenant tenant = sysTenantService.getSysTenantByTenantId(tenantId);
		if (tenant != null) {
			params.put("orgName", tenant.getName());
			params.put("tenant", tenant);
		}
		params.put("year", year);
		params.put("month", month);

		byte[] data = null;
		String reportId = request.getParameter("reportId");
		ReportDefinition rdf = ReportContainer.getContainer().getReportDefinition(reportId);

		if (rdf != null) {
			data = rdf.getData();
			ITenantReportPreprocessor reportPreprocessor = null;
			Workbook workbook = null;
			InputStream is = null;
			ByteArrayInputStream bais = null;
			ByteArrayOutputStream baos = null;
			BufferedOutputStream bos = null;
			try {

				if (StringUtils.isNotEmpty(rdf.getPrepareClass())) {
					reportPreprocessor = (ITenantReportPreprocessor) com.glaf.core.util.ReflectUtils
							.instantiate(rdf.getPrepareClass());
					reportPreprocessor.prepare(tenant, params);
				}

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
				ex.printStackTrace();
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

	@RequestMapping("/grades")
	public ModelAndView grades(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR);
		request.setAttribute("year", year);
		request.setAttribute("month", calendar.get(Calendar.MONTH));

		List<Integer> years = new ArrayList<Integer>();
		if (year >= 2019) {
			years.add(year - 1);
		}

		years.add(year);

		List<Integer> months = new ArrayList<Integer>();
		for (int i = 1; i <= 12; i++) {
			months.add(i);
		}

		request.setAttribute("years", years);
		request.setAttribute("months", months);
		return new ModelAndView("/heathcare/tenant/grades", modelMap);
	}

	@RequestMapping("/list")
	public ModelAndView list(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		List<District> provinces = districtService.getDistrictList(0);
		request.setAttribute("provinces", provinces);

		long provinceId = RequestUtils.getLong(request, "provinceId");
		if (provinceId > 0) {
			List<District> citys = districtService.getDistrictList(provinceId);
			request.setAttribute("citys", citys);
		}

		long cityId = RequestUtils.getLong(request, "cityId");
		if (cityId > 0) {
			List<District> areas = districtService.getDistrictList(cityId);
			request.setAttribute("areas", areas);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("tenant.list");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/tenant/list", modelMap);
	}

	@RequestMapping("/main")
	public ModelAndView main(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR);
		request.setAttribute("year", year);
		request.setAttribute("month", calendar.get(Calendar.MONTH));

		List<Integer> years = new ArrayList<Integer>();
		if (year >= 2019) {
			years.add(year - 1);
		}

		years.add(year);

		List<Integer> months = new ArrayList<Integer>();
		for (int i = 1; i <= 12; i++) {
			months.add(i);
		}

		request.setAttribute("years", years);
		request.setAttribute("months", months);
		return new ModelAndView("/heathcare/tenant/main", modelMap);
	}

	@javax.annotation.Resource
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}

	@javax.annotation.Resource
	public void setSysTenantService(SysTenantService sysTenantService) {
		this.sysTenantService = sysTenantService;
	}

	@javax.annotation.Resource
	public void setTenantConfigService(TenantConfigService tenantConfigService) {
		this.tenantConfigService = tenantConfigService;
	}

}
