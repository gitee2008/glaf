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

import com.glaf.base.modules.sys.model.SysTenant;
import com.glaf.base.modules.sys.model.TenantConfig;
import com.glaf.base.modules.sys.service.SysTenantService;
import com.glaf.base.modules.sys.service.TenantConfigService;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;
import com.glaf.heathcare.domain.GradeInfo;
import com.glaf.heathcare.query.GradeInfoQuery;
import com.glaf.heathcare.report.IReportPreprocessor;
import com.glaf.heathcare.service.GradeInfoService;
import com.glaf.report.bean.ReportContainer;
import com.glaf.report.data.ReportDefinition;

@Controller("/heathcare/reportMain")
@RequestMapping("/heathcare/reportMain")
public class ReportMainController {

	protected static final Log logger = LogFactory.getLog(ReportMainController.class);

	protected GradeInfoService gradeInfoService;

	protected SysTenantService sysTenantService;

	protected TenantConfigService tenantConfigService;

	@ResponseBody
	@RequestMapping("/exportXls")
	public void exportXls(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		params.put("tenantId", loginContext.getTenantId());
		params.put("tableSuffix", IdentityFactory.getTenantHash(loginContext.getTenantId()));
		int year = RequestUtils.getInt(request, "year");
		int month = RequestUtils.getInt(request, "month");

		TenantConfig tenantConfig = tenantConfigService.getTenantConfigByTenantId(loginContext.getTenantId());
		if (tenantConfig != null) {
			params.put("tenantConfig", tenantConfig);
			params.put("sysName", tenantConfig.getSysName());
		}

		SysTenant tenant = sysTenantService.getSysTenantByTenantId(loginContext.getTenantId());
		if (tenant != null) {
			params.put("orgName", tenant.getName());
			params.put("tenant", tenant);
		}
		params.put("year", year);
		params.put("month", month);

		params.put("username", loginContext.getUser().getName());

		String gradeId = request.getParameter("gradeId");
		if (StringUtils.isNotEmpty(gradeId)) {
			GradeInfo gradeInfo = gradeInfoService.getGradeInfo(gradeId);
			params.put("gradeInfo", gradeInfo);
			params.put("gradeName", gradeInfo.getName());
		}

		byte[] data = null;
		String reportId = request.getParameter("reportId");
		ReportDefinition rdf = ReportContainer.getContainer().getReportDefinition(reportId);
		logger.debug("reportId:" + reportId);
		if (rdf != null) {
			data = rdf.getData();
			IReportPreprocessor reportPreprocessor = null;
			Workbook workbook = null;
			InputStream is = null;
			ByteArrayInputStream bais = null;
			ByteArrayOutputStream baos = null;
			BufferedOutputStream bos = null;
			try {

				if (StringUtils.isNotEmpty(rdf.getPrepareClass())) {
					reportPreprocessor = (IReportPreprocessor) com.glaf.core.util.ReflectUtils
							.instantiate(rdf.getPrepareClass());
					reportPreprocessor.prepare(loginContext, year, month, params);
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

	@RequestMapping
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

		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		GradeInfoQuery query = new GradeInfoQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		if (!loginContext.isSystemAdministrator()) {
			logger.debug("perms:" + loginContext.getPermissions());
			query.tenantId(loginContext.getTenantId());
			if (!(loginContext.hasPermission("HealthPhysician", "or")
					|| loginContext.hasPermission("TenantAdmin", "or"))) {
				logger.debug("gradeIds:" + loginContext.getGradeIds());
				query.gradeIds(loginContext.getGradeIds());
			}
		}

		List<GradeInfo> list = gradeInfoService.list(query);
		request.setAttribute("grades", list);

		return new ModelAndView("/heathcare/reportMain/main", modelMap);
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.gradeInfoService")
	public void setGradeInfoService(GradeInfoService gradeInfoService) {
		this.gradeInfoService = gradeInfoService;
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
