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
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang.StringUtils;
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

import com.glaf.base.modules.sys.model.TenantConfig;
import com.glaf.base.modules.sys.service.DictoryService;
import com.glaf.base.modules.sys.service.SysTenantService;
import com.glaf.base.modules.sys.service.SysTreeService;
import com.glaf.base.modules.sys.service.TenantConfigService;

import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.heathcare.SysConfig;
import com.glaf.heathcare.bean.DailyDietaryExportBean;
import com.glaf.heathcare.bean.DayDietaryBean;
import com.glaf.heathcare.bean.DayDietaryStatisticsBean;
import com.glaf.heathcare.bean.DietaryBean;
import com.glaf.heathcare.bean.DietaryExportBean;
import com.glaf.heathcare.service.DietaryCountService;
import com.glaf.heathcare.service.DietaryItemService;
import com.glaf.heathcare.service.DietaryService;
import com.glaf.heathcare.service.DietaryTemplateService;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.matrix.data.domain.StatsHistory;
import com.glaf.matrix.data.service.StatsHistoryService;
import com.glaf.report.bean.ReportContainer;
import com.glaf.report.data.ReportDefinition;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/dietaryExport")
@RequestMapping("/heathcare/dietaryExport")
public class DietaryExportController {
	protected static final Log logger = LogFactory.getLog(DietaryExportController.class);

	protected static ConcurrentMap<String, String> concurrentMap = new ConcurrentHashMap<String, String>();

	protected DictoryService dictoryService;

	protected DietaryService dietaryService;

	protected DietaryCountService dietaryCountService;

	protected DietaryItemService dietaryItemService;

	protected DietaryTemplateService dietaryTemplateService;

	protected FoodCompositionService foodCompositionService;

	protected SysTreeService sysTreeService;

	protected StatsHistoryService statsHistoryService;

	protected TenantConfigService tenantConfigService;

	protected SysTenantService sysTenantService;

	public DietaryExportController() {

	}

	@ResponseBody
	@RequestMapping("/exportTenantWeek")
	public void exportTenantWeek(HttpServletRequest request, HttpServletResponse response) {
		String tenantId = request.getParameter("tenantId");
		String exportType = request.getParameter("exportType");
		if (StringUtils.isNotEmpty(tenantId) && StringUtils.equals(exportType, "xls")) {

			int year = RequestUtils.getInt(request, "year");
			int week = RequestUtils.getInt(request, "week");
			byte[] data = null;
			Workbook workbook = null;
			InputStream is = null;
			ByteArrayInputStream bais = null;
			ByteArrayOutputStream baos = null;
			BufferedOutputStream bos = null;
			try {

				ReportDefinition rdf = ReportContainer.getContainer().getReportDefinition("rpt_dietary");
				data = rdf.getData();
				if (data != null) {
					DietaryExportBean exportBean = new DietaryExportBean();
					Map<String, Object> context = RequestUtils.getParameterMap(request);
					Map<String, Object> dataMap = exportBean.prepareData(tenantId, year, week);
					context.putAll(dataMap);

					bais = new ByteArrayInputStream(data);
					is = new BufferedInputStream(bais);
					baos = new ByteArrayOutputStream();
					bos = new BufferedOutputStream(baos);

					Context context2 = PoiTransformer.createInitialContext();

					Set<Entry<String, Object>> entrySet = context.entrySet();
					for (Entry<String, Object> entry : entrySet) {
						String key = entry.getKey();
						Object value = entry.getValue();
						context2.putVar(key, value);
						// logger.debug(key);
					}
					org.jxls.util.JxlsHelper.getInstance().processTemplate(is, bos, context2);

					bos.flush();
					baos.flush();
					data = baos.toByteArray();

					ResponseUtils.download(request, response, data,
							"export" + DateUtils.getNowYearMonthDayHHmmss() + ".xls");
				}
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

	@ResponseBody
	@RequestMapping("/exportWeek")
	public void exportWeek(HttpServletRequest request, HttpServletResponse response) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		/**
		 * 角色HealthPhysician和TenantAdmin可以执行汇总
		 */
		if (loginContext.getRoles().contains("HealthPhysician") || loginContext.getRoles().contains("TenantAdmin")) {
			int year = RequestUtils.getInt(request, "year");
			int week = RequestUtils.getInt(request, "week");
			if (concurrentMap.get("xls_" + loginContext.getActorId()) != null) {
				try {
					Thread.sleep(5000L);
				} catch (InterruptedException e) {
				}
			}

			String exportType = request.getParameter("exportType");
			if (StringUtils.equals(exportType, "xls")) {
				byte[] data = null;
				Workbook workbook = null;
				InputStream is = null;
				ByteArrayInputStream bais = null;
				ByteArrayOutputStream baos = null;
				BufferedOutputStream bos = null;
				try {
					concurrentMap.put("xls_" + loginContext.getActorId(), "1");
					ReportDefinition rdf = ReportContainer.getContainer().getReportDefinition("rpt_dietary");
					data = rdf.getData();
					if (data != null) {
						DietaryExportBean exportBean = new DietaryExportBean();
						Map<String, Object> context = RequestUtils.getParameterMap(request);
						Map<String, Object> dataMap = exportBean.prepareData(loginContext.getTenantId(), year, week);
						context.putAll(dataMap);

						bais = new ByteArrayInputStream(data);
						is = new BufferedInputStream(bais);
						baos = new ByteArrayOutputStream();
						bos = new BufferedOutputStream(baos);

						Context context2 = PoiTransformer.createInitialContext();

						Set<Entry<String, Object>> entrySet = context.entrySet();
						for (Entry<String, Object> entry : entrySet) {
							String key = entry.getKey();
							Object value = entry.getValue();
							context2.putVar(key, value);
							// logger.debug(key);
						}
						org.jxls.util.JxlsHelper.getInstance().processTemplate(is, bos, context2);

						bos.flush();
						baos.flush();
						data = baos.toByteArray();

						ResponseUtils.download(request, response, data,
								"export" + DateUtils.getNowYearMonthDayHHmmss() + ".xls");
					}
				} catch (Exception ex) {
					// ex.printStackTrace();
					logger.error(ex);
				} finally {
					data = null;
					concurrentMap.remove("xls_" + loginContext.getActorId());
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
	}

	@javax.annotation.Resource
	public void setDictoryService(DictoryService dictoryService) {
		this.dictoryService = dictoryService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.dietaryCountService")
	public void setDietaryCountService(DietaryCountService dietaryCountService) {
		this.dietaryCountService = dietaryCountService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.dietaryItemService")
	public void setDietaryItemService(DietaryItemService dietaryItemService) {
		this.dietaryItemService = dietaryItemService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.dietaryService")
	public void setDietaryService(DietaryService dietaryService) {
		this.dietaryService = dietaryService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.dietaryTemplateService")
	public void setDietaryTemplateService(DietaryTemplateService dietaryTemplateService) {
		this.dietaryTemplateService = dietaryTemplateService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.foodCompositionService")
	public void setFoodCompositionService(FoodCompositionService foodCompositionService) {
		this.foodCompositionService = foodCompositionService;
	}

	@javax.annotation.Resource
	public void setStatsHistoryService(StatsHistoryService statsHistoryService) {
		this.statsHistoryService = statsHistoryService;
	}

	@javax.annotation.Resource
	public void setSysTenantService(SysTenantService sysTenantService) {
		this.sysTenantService = sysTenantService;
	}

	@javax.annotation.Resource
	public void setSysTreeService(SysTreeService sysTreeService) {
		this.sysTreeService = sysTreeService;
	}

	@javax.annotation.Resource
	public void setTenantConfigService(TenantConfigService tenantConfigService) {
		this.tenantConfigService = tenantConfigService;
	}

	@RequestMapping("/showDayExport")
	public ModelAndView showDayExport(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		RequestUtils.setRequestParameterToAttribute(request);

		// int week2 = dietaryService.getNowWeek(loginContext.getTenantId(),
		// year, SysConfig.getSemester());
		// request.setAttribute("week", week2);

		int fullDay = RequestUtils.getInt(request, "fullDay");
		int yearx = RequestUtils.getInt(request, "year");
		int weekx = RequestUtils.getInt(request, "week");
		int semester = SysConfig.getSemester();

		if (yearx > 0 && weekx > 0) {
			List<Integer> days = dietaryService.getDays(loginContext.getTenantId(), yearx, semester, weekx);
			request.setAttribute("days", days);
			if (fullDay == 0) {
				if (days != null && days.size() > 0) {
					fullDay = days.get(0);
				}
			}
		}

		Calendar calendar = Calendar.getInstance();
		if (fullDay > 0) {
			calendar.setTime(DateUtils.toDate(String.valueOf(fullDay)));

			int suitNo = fullDay;
			int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

			request.setAttribute("suitNo", suitNo);
			request.setAttribute("dayOfWeek", dayOfWeek);
		}

		/**
		 * 角色HealthPhysician和TenantAdmin可以执行汇总
		 */
		if (loginContext.getRoles().contains("HealthPhysician") || loginContext.getRoles().contains("TenantAdmin")) {
			request.setAttribute("dietary_copy_add_perm", true);
			if (fullDay > 0) {

				calendar.setTime(DateUtils.toDate(String.valueOf(fullDay)));
				int year = calendar.get(Calendar.YEAR);
				int week = dietaryService.getWeek(loginContext.getTenantId(), fullDay);
				if (concurrentMap.get("day_html_" + loginContext.getActorId()) != null) {
					try {
						Thread.sleep(5000L);
					} catch (InterruptedException e) {
					}
				}

				try {
					concurrentMap.put("day_html_" + loginContext.getActorId(), "1");

					String id = "day_html_" + loginContext.getTenantId() + "_" + fullDay;
					StatsHistory his = statsHistoryService.getStatsHistory(id);
					if (his != null) {
						if (((System.currentTimeMillis() - his.getUpdateTime().getTime()) > DateUtils.MINUTE * 5)) {
							DayDietaryBean bean = new DayDietaryBean();
							bean.executeCountAll(loginContext.getTenantId(), week, fullDay);
							bean.executeCountItems(loginContext.getTenantId(), week, fullDay);

							DayDietaryStatisticsBean statisticsBean = new DayDietaryStatisticsBean();
							statisticsBean.execute(loginContext.getTenantId(), fullDay);

							his.setUpdateBy(loginContext.getActorId());
							his.setUpdateTime(new Date());

							statsHistoryService.save(his);
						}
					} else {
						DayDietaryBean bean = new DayDietaryBean();
						bean.executeCountAll(loginContext.getTenantId(), week, fullDay);
						bean.executeCountItems(loginContext.getTenantId(), week, fullDay);
						DayDietaryStatisticsBean statisticsBean = new DayDietaryStatisticsBean();
						statisticsBean.execute(loginContext.getTenantId(), fullDay);

						his = new StatsHistory();
						his.setId(id);
						his.setTenantId(loginContext.getTenantId());
						his.setYear(year);
						his.setWeek(week);
						his.setFullDay(fullDay);
						his.setSemester(SysConfig.getSemester());
						his.setFullDay(DateUtils.getNowYearMonthDay());
						his.setTitle("Day Dietary Export Html");
						his.setType("day_dietary_export_html");
						his.setCreateBy(loginContext.getActorId());
						his.setUpdateBy(loginContext.getActorId());
						statsHistoryService.save(his);
					}

					DailyDietaryExportBean exportBean = new DailyDietaryExportBean();
					TenantConfig tenantConfig = tenantConfigService
							.getTenantConfigByTenantId(loginContext.getTenantId());
					long typeId = tenantConfig.getTypeId();
					Map<String, Object> dataMap = exportBean.prepareData(loginContext.getTenantId(), fullDay, typeId);
					Set<Entry<String, Object>> entrySet = dataMap.entrySet();
					for (Entry<String, Object> entry : entrySet) {
						String key = entry.getKey();
						Object value = entry.getValue();
						request.setAttribute(key, value);
						logger.debug("key:" + key);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					logger.error(ex);
				} finally {
					concurrentMap.remove("day_html_" + loginContext.getActorId());
				}
			}
		}

		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		List<Integer> years = new ArrayList<Integer>();
		List<Integer> months = new ArrayList<Integer>();
		List<Integer> weeks = new ArrayList<Integer>();

		years.add(year);
		months.add(month);
		if (month == 12) {
			years.add(year + 1);
			months.add(1);
		} else {
			months.add(month + 1);
		}

		for (int i = 1; i <= 20; i++) {
			weeks.add(i);
		}

		request.setAttribute("years", years);
		request.setAttribute("months", months);
		request.setAttribute("weeks", weeks);
		request.setAttribute("year", year);
		request.setAttribute("month", month);
		request.setAttribute("day", day);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("dietaryExport.showDayExport");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/dietary/showDayExport", modelMap);
	}

	@RequestMapping("/showExport")
	public ModelAndView showExport(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		List<Integer> years = new ArrayList<Integer>();
		List<Integer> months = new ArrayList<Integer>();
		List<Integer> days = new ArrayList<Integer>();
		List<Integer> weeks = new ArrayList<Integer>();

		years.add(year);
		months.add(month);
		if (month == 12) {
			years.add(year + 1);
			months.add(1);
		} else {
			months.add(month + 1);
		}

		for (int i = 1; i <= 31; i++) {
			days.add(i);
		}

		for (int i = 1; i <= 20; i++) {
			weeks.add(i);
		}

		request.setAttribute("years", years);
		request.setAttribute("months", months);
		request.setAttribute("days", days);
		request.setAttribute("weeks", weeks);
		request.setAttribute("year", year);
		request.setAttribute("month", month);
		request.setAttribute("day", day);
		request.setAttribute("semester", SysConfig.getSemester());

		// int week2 = dietaryService.getNowWeek(loginContext.getTenantId(),
		// year, SysConfig.getSemester());
		// request.setAttribute("week", week2);

		/**
		 * 角色HealthPhysician和TenantAdmin可以执行汇总
		 */
		if (loginContext.getRoles().contains("HealthPhysician") || loginContext.getRoles().contains("TenantAdmin")) {
			request.setAttribute("dietary_copy_add_perm", true);
			year = RequestUtils.getInt(request, "year");
			int week = RequestUtils.getInt(request, "week");

			if (year > 0 && week > 0) {

				if (concurrentMap.get("html_" + loginContext.getActorId()) != null) {
					try {
						Thread.sleep(5000L);
					} catch (InterruptedException e) {
					}
				}

				try {
					concurrentMap.put("html_" + loginContext.getActorId(), "1");

					String id = "html_" + loginContext.getTenantId() + "_" + year + "_" + SysConfig.getSemester() + "_"
							+ week;
					StatsHistory his = statsHistoryService.getStatsHistory(id);
					if (his != null) {
						if (((System.currentTimeMillis() - his.getUpdateTime().getTime()) > DateUtils.MINUTE * 5)) {
							DietaryBean bean = new DietaryBean();
							bean.executeCountAll(loginContext.getTenantId(), year, week);
							bean.executeCountItems(loginContext.getTenantId(), year, week);
							his.setUpdateBy(loginContext.getActorId());
							his.setUpdateTime(new Date());
							statsHistoryService.save(his);
						}
					} else {
						DietaryBean bean = new DietaryBean();
						bean.executeCountAll(loginContext.getTenantId(), year, week);
						bean.executeCountItems(loginContext.getTenantId(), year, week);
						his = new StatsHistory();
						his.setId(id);
						his.setTenantId(loginContext.getTenantId());
						his.setYear(year);
						his.setMonth(month);
						his.setWeek(week);
						his.setSemester(SysConfig.getSemester());
						his.setFullDay(DateUtils.getNowYearMonthDay());
						his.setTitle("Dietary Export Html");
						his.setType("dietary_export_html");
						his.setCreateBy(loginContext.getActorId());
						his.setUpdateBy(loginContext.getActorId());
						statsHistoryService.save(his);
					}

					if (year > 0 && week > 0) {
						if (loginContext.getRoles().contains("HealthPhysician")
								|| loginContext.getRoles().contains("TenantAdmin")) {
							try {
								int semester = SysConfig.getSemester();
								DayDietaryStatisticsBean bean = new DayDietaryStatisticsBean();
								bean.execute(loginContext.getTenantId(), year, semester, week);
							} catch (Exception ex) {
							}
						}
					}

					if (week > 0) {
						DietaryExportBean exportBean = new DietaryExportBean();
						Map<String, Object> dataMap = exportBean.prepareData(loginContext.getTenantId(), year, week);
						Set<Entry<String, Object>> entrySet = dataMap.entrySet();
						for (Entry<String, Object> entry : entrySet) {
							String key = entry.getKey();
							Object value = entry.getValue();
							request.setAttribute(key, value);
							logger.debug("key:" + key);
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					logger.error(ex);
				} finally {
					concurrentMap.remove("html_" + loginContext.getActorId());
				}
			}
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("dietaryExport.showExport");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/dietary/showExport", modelMap);
	}

	@RequestMapping("/showTenantDayExport")
	public ModelAndView showTenantDayExport(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String tenantId = request.getParameter("tenantId");

		int fullDay = RequestUtils.getInt(request, "fullDay");
		int yearx = RequestUtils.getInt(request, "year");
		int weekx = RequestUtils.getInt(request, "week");
		int semester = SysConfig.getSemester();

		if (yearx > 0 && weekx > 0) {
			List<Integer> days = dietaryService.getDays(tenantId, yearx, semester, weekx);
			request.setAttribute("days", days);
			if (fullDay == 0) {
				if (days != null && days.size() > 0) {
					fullDay = days.get(0);
				}
			}
		}

		if (fullDay == 0) {
			fullDay = DateUtils.getNowYearMonthDay();
		}

		Calendar calendar = Calendar.getInstance();
		if (fullDay > 0) {
			calendar.setTime(DateUtils.toDate(String.valueOf(fullDay)));

			int suitNo = fullDay;
			int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

			request.setAttribute("suitNo", suitNo);
			request.setAttribute("dayOfWeek", dayOfWeek);
		}

		LoginContext loginContext = RequestUtils.getLoginContext(request);
		/**
		 * 角色HygieneOrg可以执行汇总
		 */
		if (loginContext.getRoles().contains("HygieneOrg")) {
			request.setAttribute("dietary_copy_add_perm", false);
			if (fullDay > 0) {

				calendar.setTime(DateUtils.toDate(String.valueOf(fullDay)));
				int year = calendar.get(Calendar.YEAR);
				int week = dietaryService.getWeek(tenantId, fullDay);
				if (concurrentMap.get("day_html_" + tenantId) != null) {
					try {
						Thread.sleep(5000L);
					} catch (InterruptedException e) {
					}
				}

				try {
					concurrentMap.put("day_html_" + tenantId, "1");

					String id = "day_html_" + tenantId + "_" + fullDay;
					StatsHistory his = statsHistoryService.getStatsHistory(id);
					if (his != null) {
						if (((System.currentTimeMillis() - his.getUpdateTime().getTime()) > DateUtils.MINUTE * 5)) {
							DayDietaryBean bean = new DayDietaryBean();
							bean.executeCountAll(tenantId, week, fullDay);
							bean.executeCountItems(tenantId, week, fullDay);

							DayDietaryStatisticsBean statisticsBean = new DayDietaryStatisticsBean();
							statisticsBean.execute(tenantId, fullDay);

							his.setUpdateBy(loginContext.getActorId());
							his.setUpdateTime(new Date());

							statsHistoryService.save(his);
						}
					} else {
						DayDietaryBean bean = new DayDietaryBean();
						bean.executeCountAll(tenantId, week, fullDay);
						bean.executeCountItems(tenantId, week, fullDay);
						DayDietaryStatisticsBean statisticsBean = new DayDietaryStatisticsBean();
						statisticsBean.execute(tenantId, fullDay);

						his = new StatsHistory();
						his.setId(id);
						his.setYear(year);
						his.setWeek(week);
						his.setFullDay(fullDay);
						his.setSemester(SysConfig.getSemester());
						his.setFullDay(DateUtils.getNowYearMonthDay());
						his.setTitle("Day Dietary Export Html");
						his.setType("day_dietary_export_html");
						his.setCreateBy(loginContext.getActorId());
						his.setUpdateBy(loginContext.getActorId());
						statsHistoryService.save(his);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					logger.error(ex);
				}
			}
		}

		try {
			DailyDietaryExportBean exportBean = new DailyDietaryExportBean();
			TenantConfig tenantConfig = tenantConfigService.getTenantConfigByTenantId(tenantId);
			long typeId = tenantConfig.getTypeId();
			Map<String, Object> dataMap = exportBean.prepareData(tenantId, fullDay, typeId);
			Set<Entry<String, Object>> entrySet = dataMap.entrySet();
			for (Entry<String, Object> entry : entrySet) {
				String key = entry.getKey();
				Object value = entry.getValue();
				request.setAttribute(key, value);
				logger.debug("key:" + key);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}

		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		List<Integer> years = new ArrayList<Integer>();
		List<Integer> months = new ArrayList<Integer>();
		List<Integer> weeks = new ArrayList<Integer>();

		years.add(year);
		months.add(month);
		if (month == 12) {
			years.add(year + 1);
			months.add(1);
		} else {
			months.add(month + 1);
		}

		for (int i = 1; i <= 20; i++) {
			weeks.add(i);
		}

		request.setAttribute("years", years);
		request.setAttribute("months", months);
		request.setAttribute("weeks", weeks);
		request.setAttribute("year", year);
		request.setAttribute("month", month);
		request.setAttribute("day", day);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("dietaryExport.showTenantDayExport");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/dietary/showTenantDayExport", modelMap);
	}

	@RequestMapping("/showTenantExport")
	public ModelAndView showTenantExport(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		List<Integer> years = new ArrayList<Integer>();
		List<Integer> months = new ArrayList<Integer>();
		List<Integer> days = new ArrayList<Integer>();
		List<Integer> weeks = new ArrayList<Integer>();

		years.add(year);
		months.add(month);
		if (month == 12) {
			years.add(year + 1);
			months.add(1);
		} else {
			months.add(month + 1);
		}

		for (int i = 1; i <= 31; i++) {
			days.add(i);
		}

		for (int i = 1; i <= 20; i++) {
			weeks.add(i);
		}

		request.setAttribute("years", years);
		request.setAttribute("months", months);
		request.setAttribute("days", days);
		request.setAttribute("weeks", weeks);
		request.setAttribute("year", year);
		request.setAttribute("month", month);
		request.setAttribute("day", day);
		request.setAttribute("semester", SysConfig.getSemester());

		int week = RequestUtils.getInt(request, "week");
		String tenantId = request.getParameter("tenantId");
		try {
			if (week > 0) {
				DietaryExportBean exportBean = new DietaryExportBean();
				Map<String, Object> dataMap = exportBean.prepareData(tenantId, year, week);
				Set<Entry<String, Object>> entrySet = dataMap.entrySet();
				for (Entry<String, Object> entry : entrySet) {
					String key = entry.getKey();
					Object value = entry.getValue();
					request.setAttribute(key, value);
					logger.debug("key:" + key);
				}
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
			logger.error(ex);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("dietaryExport.showTenantExport");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/dietary/showTenantExport", modelMap);
	}

}
