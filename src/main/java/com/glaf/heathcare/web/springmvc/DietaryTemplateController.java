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
import com.glaf.base.district.domain.District;
import com.glaf.base.district.service.DistrictService;
import com.glaf.base.modules.sys.model.Dictory;
import com.glaf.base.modules.sys.model.TenantConfig;
import com.glaf.base.modules.sys.service.DictoryService;
import com.glaf.base.modules.sys.service.SysTreeService;
import com.glaf.base.modules.sys.service.TenantConfigService;
import com.glaf.core.base.ColumnModel;
import com.glaf.core.base.TableModel;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.factory.DataServiceFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.StringTools;
import com.glaf.core.util.Tools;

import com.glaf.heathcare.bean.DietaryTemplateBean;
import com.glaf.heathcare.domain.DietaryTemplate;
import com.glaf.heathcare.helper.PermissionHelper;
import com.glaf.heathcare.query.DietaryTemplateQuery;
import com.glaf.heathcare.service.DietaryItemService;
import com.glaf.heathcare.service.DietaryTemplateService;
import com.glaf.heathcare.service.FoodCompositionService;

import net.iharder.Base64;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/dietaryTemplate")
@RequestMapping("/heathcare/dietaryTemplate")
public class DietaryTemplateController {
	protected static final Log logger = LogFactory.getLog(DietaryTemplateController.class);

	protected DictoryService dictoryService;

	protected DistrictService districtService;

	protected DietaryItemService dietaryItemService;

	protected DietaryTemplateService dietaryTemplateService;

	protected FoodCompositionService foodCompositionService;

	protected SysTreeService sysTreeService;

	protected TenantConfigService tenantConfigService;

	public DietaryTemplateController() {

	}

	@RequestMapping("/batchAdd")
	public ModelAndView batchAdd(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		List<Integer> weeks = new ArrayList<Integer>();
		for (int i = 1; i <= 20; i++) {
			weeks.add(i);
		}
		request.setAttribute("weeks", weeks);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("dietaryTemplate.batchAdd");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/dietaryTemplate/batchAdd", modelMap);
	}

	@ResponseBody
	@RequestMapping("/calculate")
	public byte[] calculate(HttpServletRequest request) {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug("params:" + params);
		long templateId = RequestUtils.getLong(request, "templateId");
		if (templateId > 0) {
			// DietaryTemplateBean bean = new DietaryTemplateBean();
			// bean.calculate(templateId);
			dietaryTemplateService.calculate(templateId);
			return ResponseUtils.responseResult(true);
		}
		return ResponseUtils.responseResult(false);
	}

	@ResponseBody
	@RequestMapping("/calculateAll")
	public byte[] calculateAll(HttpServletRequest request) {
		String objectIds = request.getParameter("objectIds");
		if (StringUtils.isNotEmpty(objectIds)) {
			DietaryTemplateBean bean = new DietaryTemplateBean();
			bean.calculateAll(StringTools.splitToLong(objectIds));
			return ResponseUtils.responseResult(true);
		}
		return ResponseUtils.responseResult(false);
	}

	@ResponseBody
	@RequestMapping("/delete")
	public byte[] delete(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Long id = RequestUtils.getLong(request, "id");
		String ids = request.getParameter("ids");
		if (StringUtils.isNotEmpty(ids)) {
			StringTokenizer token = new StringTokenizer(ids, ",");
			while (token.hasMoreTokens()) {
				String x = token.nextToken();
				if (StringUtils.isNotEmpty(x)) {
					DietaryTemplate dietaryTemplate = dietaryTemplateService.getDietaryTemplate(Long.valueOf(x));
					if (dietaryTemplate != null && (loginContext.hasSystemPermission()
							|| StringUtils.equals(dietaryTemplate.getCreateBy(), loginContext.getActorId()))) {
						dietaryTemplateService.deleteById(dietaryTemplate.getId());
					} else {
						return ResponseUtils.responseJsonResult(false, "您没有该数据的删除权限。");
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			DietaryTemplate dietaryTemplate = dietaryTemplateService.getDietaryTemplate(Long.valueOf(id));
			if (dietaryTemplate != null && (loginContext.hasSystemPermission()
					|| StringUtils.equals(dietaryTemplate.getCreateBy(), loginContext.getActorId()))) {
				dietaryTemplateService.deleteById(dietaryTemplate.getId());
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		RequestUtils.setRequestParameterToAttribute(request);
		request.removeAttribute("heathcare_curd_perm");
		if (loginContext.hasSystemPermission() || loginContext.isSystemAdministrator()) {
			request.setAttribute("heathcare_curd_perm", true);
		}

		DietaryTemplate dietaryTemplate = dietaryTemplateService
				.getDietaryTemplate(RequestUtils.getLong(request, "id"));
		if (dietaryTemplate != null) {
			request.setAttribute("dietaryTemplate", dietaryTemplate);
			if (StringUtils.equals(dietaryTemplate.getCreateBy(), loginContext.getActorId())) {
				request.setAttribute("heathcare_curd_perm", true);
			}
		} else {
			request.setAttribute("heathcare_curd_perm", true);
		}

		if (loginContext.isSystemAdministrator()) {
			List<Dictory> dictoryList = dictoryService.getDictoryList(4501L);// 4501是餐点分类编号
			// List<Dictory> dictoryList =
			// dictoryService.getDictoryListByCategory("CAT_MEAL");
			request.setAttribute("dictoryList", dictoryList);
		} else {
			TenantConfig tenantConfig = tenantConfigService.getTenantConfigByTenantId(loginContext.getTenantId());
			if (tenantConfig != null && tenantConfig.getTypeId() > 0) {
				Dictory dict = dictoryService.find(tenantConfig.getTypeId());
				List<Dictory> dicts = dictoryService.getDictories(dict.getCode() + "%");
				request.setAttribute("dictoryList", dicts);
			} else {
				List<Dictory> dictoryList = dictoryService.getDictoryList(4501L);// 4501是餐点分类编号
				// List<Dictory> dictoryList =
				// dictoryService.getDictoryListByCategory("CAT_MEAL");
				request.setAttribute("dictoryList", dictoryList);
			}
		}

		List<District> districts = districtService.getDistrictList(0);
		request.setAttribute("districts", districts);

		List<Integer> suitNos = new ArrayList<Integer>();
		for (int i = 1; i <= 80; i++) {
			suitNos.add(i);
		}
		request.setAttribute("suitNos", suitNos);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("dietaryTemplate.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/dietaryTemplate/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		DietaryTemplateQuery query = new DietaryTemplateQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);
		query.instanceFlag("N");

		String sysFlag = request.getParameter("sysFlag");
		String selected = request.getParameter("selected");

		if (StringUtils.equals(sysFlag, "Y")) {
			query.sysFlag("Y");
		} else {
			query.sysFlag("N");
			query.tenantId(loginContext.getTenantId());
		}

		if (loginContext.isSystemAdministrator()) {
			query.sysFlag("Y");
		}

		long typeId = RequestUtils.getLong(request, "typeId");
		if (typeId > 0) {
			List<Long> typeIds = new ArrayList<Long>();
			if (typeId == 3301) {// 三餐一点
				typeIds.add(3301L);
				typeIds.add(3302L);
				typeIds.add(3303L);
				typeIds.add(3304L);
				typeIds.add(3305L);
			} else if (typeId == 3401) {// 三餐两点
				typeIds.add(3401L);
				typeIds.add(3402L);
				typeIds.add(3403L);
				typeIds.add(3404L);
				typeIds.add(3405L);
				typeIds.add(3406L);
			} else if (typeId == 3411) {// 二餐二点
				typeIds.add(3411L);
				typeIds.add(3412L);
				typeIds.add(3413L);
				typeIds.add(3414L);
				typeIds.add(3415L);
			} else if (typeId == 3311) {// 二餐一点
				typeIds.add(3311L);
				typeIds.add(3312L);
				typeIds.add(3313L);
				typeIds.add(3314L);
			} else if (typeId == 3321) {// 一餐二点
				typeIds.add(3321L);
				typeIds.add(3322L);
				typeIds.add(3323L);
				typeIds.add(3324L);
			}
			query.setTypeId(null);
			query.typeIds(typeIds);
		}

		String nameLike = request.getParameter("nameLike_enc");
		if (StringUtils.isNotEmpty(nameLike)) {
			nameLike = RequestUtils.decodeString(nameLike);
			query.setNameLike(nameLike);
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
		int total = dietaryTemplateService.getDietaryTemplateCountByQueryCriteria(query);
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

			List<DietaryTemplate> list = dietaryTemplateService.getDietaryTemplatesByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				Map<Long, String> nameMap2 = new HashMap<Long, String>();
				List<Dictory> dictoryList = dictoryService.getDictoryList(4501L);// 4501是餐点分类编号
				if (dictoryList != null && !dictoryList.isEmpty()) {
					for (Dictory d : dictoryList) {
						nameMap2.put(d.getId(), d.getName());
					}
				}

				for (DietaryTemplate dietaryTemplate : list) {
					JSONObject rowJSON = dietaryTemplate.toJsonObject();
					rowJSON.put("id", dietaryTemplate.getId());
					rowJSON.put("rowId", dietaryTemplate.getId());
					rowJSON.put("templateId", dietaryTemplate.getId());
					rowJSON.put("typeName", nameMap2.get(dietaryTemplate.getTypeId()));
					rowJSON.put("startIndex", ++start);
					if (StringUtils.equals(selected, String.valueOf(dietaryTemplate.getId()))) {
						rowJSON.put("checked", 1);
					}

					switch (dietaryTemplate.getSeason()) {
					case 1:
						rowJSON.put("seasonName", "春季");
						break;
					case 2:
						rowJSON.put("seasonName", "夏季");
						break;
					case 3:
						rowJSON.put("seasonName", "秋季");
						break;
					case 4:
						rowJSON.put("seasonName", "冬季");
						break;
					default:
						rowJSON.put("seasonName", "-");
						break;
					}

					switch (dietaryTemplate.getDayOfWeek()) {
					case 1:
						rowJSON.put("dayOfWeekName", "星期一");
						break;
					case 2:
						rowJSON.put("dayOfWeekName", "星期二");
						break;
					case 3:
						rowJSON.put("dayOfWeekName", "星期三");
						break;
					case 4:
						rowJSON.put("dayOfWeekName", "星期四");
						break;
					case 5:
						rowJSON.put("dayOfWeekName", "星期五");
						break;
					case 6:
						rowJSON.put("dayOfWeekName", "星期六");
						break;
					case 7:
						rowJSON.put("dayOfWeekName", "星期日");
						break;
					default:
						rowJSON.put("dayOfWeekName", "-");
						break;
					}

					rowsJSON.add(rowJSON);
				}

				result.put("rows", rowsJSON);
			}
		} else {
			JSONArray rowsJSON = new JSONArray();
			result.put("rows", rowsJSON);
			result.put("total", total);
		}
		// logger.debug(result.toJSONString());
		return result.toJSONString().getBytes("UTF-8");
	}

	@RequestMapping
	public ModelAndView list(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		PermissionHelper helper = new PermissionHelper();
		helper.setUserPermission(request);

		// List<Dictory> dictoryList = dictoryService.getDictoryList(4501L);//
		// 4501是餐点分类编号
		List<Dictory> dictoryList = dictoryService.getDictoryListByCategory("CAT_MEAL");
		request.setAttribute("dictoryList", dictoryList);

		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (!loginContext.isSystemAdministrator()) {
			TenantConfig cfg = tenantConfigService.getTenantConfigByTenantId(loginContext.getTenantId());
			if (cfg != null) {
				request.setAttribute("tenantConfig", cfg);
				for (Dictory dict : dictoryList) {
					if (cfg.getTypeId() == dict.getId()) {
						request.setAttribute("typeDict", dict);
						request.setAttribute("typeId", dict.getId());
					}
				}
			}
		}

		if (StringUtils.isNotEmpty(request.getParameter("typeId"))) {
			request.setAttribute("typeId", request.getParameter("typeId"));
		}

		String wordLike = request.getParameter("wordLike");
		if (StringUtils.isNotEmpty(wordLike)) {
			wordLike = wordLike.trim();
			request.setAttribute("wordLike_enc", RequestUtils.encodeString(wordLike));
			request.setAttribute("wordLike_base64", Base64.encodeBytes(wordLike.getBytes()));
		}

		String nameLike = request.getParameter("nameLike");
		if (StringUtils.isNotEmpty(nameLike)) {
			request.setAttribute("nameLike_enc", RequestUtils.encodeString(nameLike));
			request.setAttribute("nameLike_base64", Base64.encodeBytes(nameLike.getBytes()));
		}

		List<District> districts = districtService.getDistrictList(0);
		request.setAttribute("districts", districts);

		List<Integer> suitNos = new ArrayList<Integer>();
		for (int i = 1; i <= 80; i++) {
			suitNos.add(i);
		}
		request.setAttribute("suitNos", suitNos);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/dietaryTemplate/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("dietaryTemplate.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/heathcare/dietaryTemplate/query", modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveAs")
	public byte[] saveAs(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		try {
			DietaryTemplate dietaryTemplate = dietaryTemplateService
					.getDietaryTemplate(RequestUtils.getLong(request, "id"));

			Tools.populate(dietaryTemplate, params);

			if (loginContext.isSystemAdministrator()) {
				dietaryTemplate.setSysFlag("Y");

			} else {
				dietaryTemplate.setTenantId(loginContext.getTenantId());
				dietaryTemplate.setSysFlag("N");
			}

			dietaryTemplate.setName(request.getParameter("name"));
			dietaryTemplate.setDescription(request.getParameter("description"));
			dietaryTemplate.setAgeGroup(request.getParameter("ageGroup"));
			dietaryTemplate.setProvince(request.getParameter("province"));
			dietaryTemplate.setRegion(request.getParameter("region"));
			dietaryTemplate.setSeason(RequestUtils.getInt(request, "season"));
			dietaryTemplate.setType(request.getParameter("type"));
			dietaryTemplate.setTypeId(RequestUtils.getLong(request, "typeId"));
			dietaryTemplate.setYear(RequestUtils.getInt(request, "year"));
			dietaryTemplate.setMonth(RequestUtils.getInt(request, "month"));
			dietaryTemplate.setDay(RequestUtils.getInt(request, "day"));
			dietaryTemplate.setWeek(RequestUtils.getInt(request, "week"));
			dietaryTemplate.setSortNo(RequestUtils.getInt(request, "sortNo"));
			dietaryTemplate.setSysFlag(request.getParameter("sysFlag"));
			dietaryTemplate.setEnableFlag(request.getParameter("enableFlag"));
			dietaryTemplate.setVerifyFlag(request.getParameter("verifyFlag"));
			dietaryTemplate.setCreateBy(actorId);
			dietaryTemplate.setUpdateBy(actorId);

			DietaryTemplateQuery query = new DietaryTemplateQuery();
			if (loginContext.isSystemAdministrator()) {
				query.sysFlag("Y");
			} else {
				query.tenantId(loginContext.getTenantId());
			}
			query.name(dietaryTemplate.getName());
			query.typeId(dietaryTemplate.getTypeId());
			query.dayOfWeek(dietaryTemplate.getDayOfWeek());
			query.suitNo(dietaryTemplate.getSuitNo());
			int count = dietaryTemplateService.getDietaryTemplateCountByQueryCriteria(query);
			if (count > 0) {
				return ResponseUtils.responseJsonResult(false, "菜名已经存在，请检查是否重复。");
			}

			if (loginContext.isSystemAdministrator()) {
				dietaryTemplate.setSysFlag("Y");
				dietaryTemplate.setShareFlag("Y");
				dietaryTemplate.setInstanceFlag("N");
			}
			dietaryTemplateService.saveAs(dietaryTemplate);
			return ResponseUtils.responseResult(true);
		} catch (Exception ex) {
			logger.error(ex);
		}

		return ResponseUtils.responseResult(false);
	}

	@ResponseBody
	@RequestMapping("/saveDietaryTemplate")
	public byte[] saveDietaryTemplate(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		String actorId = loginContext.getActorId();
		long id = RequestUtils.getLong(request, "id");
		DietaryTemplate dietaryTemplate = null;
		try {
			if (id > 0) {
				dietaryTemplate = dietaryTemplateService.getDietaryTemplate(id);
			}

			if (dietaryTemplate == null) {
				dietaryTemplate = new DietaryTemplate();
				Tools.populate(dietaryTemplate, params);

				dietaryTemplate.setInstanceFlag("N");// 增加的模板，非实例
				if (loginContext.isSystemAdministrator()) {
					dietaryTemplate.setSysFlag("Y");
				} else {
					dietaryTemplate.setSysFlag("N");
					dietaryTemplate.setTenantId(loginContext.getTenantId());
				}
			} else {
				if (!loginContext.isSystemAdministrator()) {
					if (!StringUtils.equals(dietaryTemplate.getCreateBy(), actorId)) {
						return ResponseUtils.responseJsonResult(false, "您没有修改该数据的权限。");
					}
				}
				Tools.populate(dietaryTemplate, params);
			}

			dietaryTemplate.setName(request.getParameter("name"));
			dietaryTemplate.setDescription(request.getParameter("description"));
			dietaryTemplate.setAgeGroup(request.getParameter("ageGroup"));
			dietaryTemplate.setProvince(request.getParameter("province"));
			dietaryTemplate.setRegion(request.getParameter("region"));
			dietaryTemplate.setSeason(RequestUtils.getInt(request, "season"));
			dietaryTemplate.setType(request.getParameter("type"));
			dietaryTemplate.setTypeId(RequestUtils.getLong(request, "typeId"));
			dietaryTemplate.setYear(RequestUtils.getInt(request, "year"));
			dietaryTemplate.setMonth(RequestUtils.getInt(request, "month"));
			dietaryTemplate.setDay(RequestUtils.getInt(request, "day"));
			dietaryTemplate.setDayOfWeek(RequestUtils.getInt(request, "dayOfWeek"));
			dietaryTemplate.setWeek(RequestUtils.getInt(request, "week"));
			dietaryTemplate.setSortNo(RequestUtils.getInt(request, "sortNo"));
			dietaryTemplate.setSuitNo(RequestUtils.getInt(request, "suitNo"));
			dietaryTemplate.setEnableFlag(request.getParameter("enableFlag"));
			dietaryTemplate.setVerifyFlag(request.getParameter("verifyFlag"));
			dietaryTemplate.setCreateBy(actorId);
			dietaryTemplate.setUpdateBy(actorId);

			if (dietaryTemplate.getId() == 0) {
				DietaryTemplateQuery query = new DietaryTemplateQuery();
				if (loginContext.isSystemAdministrator()) {
					query.sysFlag("Y");
				} else {
					query.tenantId(loginContext.getTenantId());
				}
				query.name(dietaryTemplate.getName());
				query.typeId(dietaryTemplate.getTypeId());
				query.dayOfWeek(dietaryTemplate.getDayOfWeek());
				query.suitNo(dietaryTemplate.getSuitNo());
				int count = dietaryTemplateService.getDietaryTemplateCountByQueryCriteria(query);
				if (count > 0) {
					return ResponseUtils.responseJsonResult(false, "菜名已经存在，请检查是否重复。");
				}
			}

			if (loginContext.isSystemAdministrator()) {
				dietaryTemplate.setSysFlag("Y");
				dietaryTemplate.setShareFlag("Y");
				dietaryTemplate.setInstanceFlag("N");
			}

			this.dietaryTemplateService.save(dietaryTemplate);

			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@ResponseBody
	@RequestMapping("/updateDietaryTemplateName")
	public byte[] updateDietaryTemplateName(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		long dietaryTemplateId = RequestUtils.getLong(request, "dietaryTemplateId");
		DietaryTemplate dietaryTemplate = null;
		try {
			if (dietaryTemplateId > 0) {
				dietaryTemplate = dietaryTemplateService.getDietaryTemplate(dietaryTemplateId);
			}
			if (dietaryTemplate != null) {
				if (!loginContext.isSystemAdministrator()) {
					if (!StringUtils.equals(dietaryTemplate.getCreateBy(), actorId)) {
						return ResponseUtils.responseJsonResult(false, "您没有修改该数据的权限。");
					}
				}
				dietaryTemplate.setName(request.getParameter("dietaryName"));
				dietaryTemplate.setUpdateBy(actorId);
				this.dietaryTemplateService.save(dietaryTemplate);
				return ResponseUtils.responseJsonResult(true);
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	/**
	 * 排序
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/saveSort")
	@ResponseBody
	public byte[] saveSort(HttpServletRequest request) {
		String items = request.getParameter("items");
		if (StringUtils.isNotEmpty(items)) {
			int sort = 0;
			List<TableModel> rows = new ArrayList<TableModel>();
			StringTokenizer token = new StringTokenizer(items, ",");
			while (token.hasMoreTokens()) {
				String item = token.nextToken();
				if (StringUtils.isNotEmpty(item)) {
					sort++;
					TableModel t1 = new TableModel();
					t1.setTableName("HEALTH_DIETARY_TEMPLATE");
					ColumnModel idColumn1 = new ColumnModel();
					idColumn1.setColumnName("ID_");
					idColumn1.setValue(Long.parseLong(item));
					t1.setIdColumn(idColumn1);
					ColumnModel column = new ColumnModel();
					column.setColumnName("SORTNO_");
					column.setValue(sort);
					t1.addColumn(column);
					rows.add(t1);
				}
			}
			try {
				DataServiceFactory.getInstance().updateAllTableData(rows);
				return ResponseUtils.responseResult(true);
			} catch (Exception ex) {
				logger.error(ex);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/search")
	public ModelAndView search(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		// List<Dictory> dictoryList = dictoryService.getDictoryList(4501L);//
		// 4501是餐点分类编号
		List<Dictory> dictoryList = dictoryService.getDictoryListByCategory("CAT_MEAL");
		request.setAttribute("dictoryList", dictoryList);

		String nameLike = request.getParameter("nameLike");
		if (StringUtils.isNotEmpty(nameLike)) {
			nameLike = nameLike.trim();
			request.setAttribute("nameLike_enc", RequestUtils.encodeString(nameLike));
		}

		List<District> districts = districtService.getDistrictList(0);
		request.setAttribute("districts", districts);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("dietaryTemplate.search");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/heathcare/dietaryTemplate/search", modelMap);
	}

	@RequestMapping("/searchlist")
	public ModelAndView searchlist(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		PermissionHelper helper = new PermissionHelper();
		helper.setUserPermission(request);

		String wordLike = request.getParameter("wordLike");
		if (StringUtils.isNotEmpty(wordLike)) {
			wordLike = wordLike.trim();
			request.setAttribute("wordLike_enc", RequestUtils.encodeString(wordLike));
			request.setAttribute("wordLike_base64", Base64.encodeBytes(wordLike.getBytes()));
		}

		String nameLike = request.getParameter("nameLike");
		if (StringUtils.isNotEmpty(nameLike)) {
			nameLike = nameLike.trim();
			request.setAttribute("nameLike_enc", RequestUtils.encodeString(nameLike));
			request.setAttribute("nameLike_base64", Base64.encodeBytes(nameLike.getBytes()));
		}

		List<District> districts = districtService.getDistrictList(0);
		request.setAttribute("districts", districts);

		List<Integer> suitNos = new ArrayList<Integer>();
		for (int i = 1; i <= 80; i++) {
			suitNos.add(i);
		}
		request.setAttribute("suitNos", suitNos);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/dietaryTemplate/searchlist", modelMap);
	}

	@RequestMapping("/selectlist")
	public ModelAndView selectlist(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		// List<Dictory> dictoryList = dictoryService.getDictoryList(4501L);//
		// 4501是餐点分类编号
		List<Dictory> dictoryList = dictoryService.getDictoryListByCategory("CAT_MEAL");
		request.setAttribute("dictoryList", dictoryList);

		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (!loginContext.isSystemAdministrator()) {
			TenantConfig cfg = tenantConfigService.getTenantConfigByTenantId(loginContext.getTenantId());
			request.setAttribute("tenantConfig", cfg);
			for (Dictory dict : dictoryList) {
				if (cfg.getTypeId() == dict.getId()) {
					request.setAttribute("typeDict", dict);
					request.setAttribute("typeId", dict.getId());
				}
			}
		}

		String nameLike = request.getParameter("nameLike");
		if (StringUtils.isNotEmpty(nameLike)) {
			nameLike = nameLike.trim();
			request.setAttribute("nameLike_enc", RequestUtils.encodeString(nameLike));
		}

		String sysFlag = request.getParameter("sysFlag");
		if (sysFlag == null) {
			sysFlag = "Y";
		}
		request.setAttribute("sysFlag", sysFlag);

		List<Integer> suitNos = new ArrayList<Integer>();
		for (int i = 1; i <= 80; i++) {
			suitNos.add(i);
		}
		request.setAttribute("suitNos", suitNos);

		List<District> districts = districtService.getDistrictList(0);
		request.setAttribute("districts", districts);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("dietaryTemplate.selectlist");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/heathcare/dietaryTemplate/selectlist", modelMap);
	}

	@javax.annotation.Resource
	public void setDictoryService(DictoryService dictoryService) {
		this.dictoryService = dictoryService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.dietaryItemService")
	public void setDietaryItemService(DietaryItemService dietaryItemService) {
		this.dietaryItemService = dietaryItemService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.dietaryTemplateService")
	public void setDietaryTemplateService(DietaryTemplateService dietaryTemplateService) {
		this.dietaryTemplateService = dietaryTemplateService;
	}

	@javax.annotation.Resource
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.foodCompositionService")
	public void setFoodCompositionService(FoodCompositionService foodCompositionService) {
		this.foodCompositionService = foodCompositionService;
	}

	@javax.annotation.Resource
	public void setSysTreeService(SysTreeService sysTreeService) {
		this.sysTreeService = sysTreeService;
	}

	@javax.annotation.Resource
	public void setTenantConfigService(TenantConfigService tenantConfigService) {
		this.tenantConfigService = tenantConfigService;
	}

	/**
	 * 显示排序页面
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/showSort")
	public ModelAndView showSort(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String objectIds = request.getParameter("objectIds");
		if (StringUtils.isNotEmpty(objectIds)) {
			List<Long> ids = StringTools.splitToLong(objectIds);
			DietaryTemplateQuery query = new DietaryTemplateQuery();
			query.setIds(ids);
			List<DietaryTemplate> list = dietaryTemplateService.list(query);
			request.setAttribute("list", list);
		}

		String x_view = ViewProperties.getString("dietaryTemplate.showSort");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/dietaryTemplate/showSort", modelMap);
	}

}
