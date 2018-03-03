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
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import com.alibaba.fastjson.*;

import com.glaf.core.config.ViewProperties;

import com.glaf.core.security.*;
import com.glaf.core.util.*;

import com.glaf.heathcare.domain.*;
import com.glaf.heathcare.query.*;
import com.glaf.heathcare.service.*;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/medicalExaminationDef")
@RequestMapping("/heathcare/medicalExaminationDef")
public class MedicalExaminationDefController {
	protected static final Log logger = LogFactory.getLog(MedicalExaminationDefController.class);

	protected MedicalExaminationService medicalExaminationService;

	protected MedicalExaminationDefService medicalExaminationDefService;

	public MedicalExaminationDefController() {

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
					MedicalExaminationDef medicalExaminationDef = medicalExaminationDefService
							.getMedicalExaminationDef(Long.valueOf(x));
					if ((loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician"))
							&& (StringUtils.equals(loginContext.getTenantId(), medicalExaminationDef.getTenantId()))) {
						if (medicalExaminationService.getMedicalExaminationCount(loginContext.getTenantId(),
								medicalExaminationDef.getCheckId()) == 0) {
							medicalExaminationDefService.deleteById(medicalExaminationDef.getId());
						} else {
							return ResponseUtils.responseJsonResult(false, "已经有关联数据，不允许删除主题。");
						}
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			MedicalExaminationDef medicalExaminationDef = medicalExaminationDefService
					.getMedicalExaminationDef(Long.valueOf(id));
			if ((loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician"))
					&& (StringUtils.equals(loginContext.getTenantId(), medicalExaminationDef.getTenantId()))) {
				if (medicalExaminationService.getMedicalExaminationCount(loginContext.getTenantId(),
						medicalExaminationDef.getCheckId()) == 0) {
					medicalExaminationDefService.deleteById(medicalExaminationDef.getId());
					return ResponseUtils.responseResult(true);
				} else {
					return ResponseUtils.responseJsonResult(false, "已经有关联数据，不允许删除主题。");
				}
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		MedicalExaminationDef medicalExaminationDef = medicalExaminationDefService
				.getMedicalExaminationDef(RequestUtils.getLong(request, "id"));
		if (medicalExaminationDef != null) {
			request.setAttribute("medicalExaminationDef", medicalExaminationDef);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("medicalExaminationDef.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/medicalExaminationDef/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		MedicalExaminationDefQuery query = new MedicalExaminationDefQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		JSONObject result = new JSONObject();
		if (loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician")) {
			query.tenantId(loginContext.getTenantId());
		} else {
			return result.toJSONString().getBytes();
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

		int total = medicalExaminationDefService.getMedicalExaminationDefCountByQueryCriteria(query);
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

			List<MedicalExaminationDef> list = medicalExaminationDefService
					.getMedicalExaminationDefsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (MedicalExaminationDef medicalExaminationDef : list) {
					JSONObject rowJSON = medicalExaminationDef.toJsonObject();
					rowJSON.put("id", medicalExaminationDef.getId());
					rowJSON.put("rowId", medicalExaminationDef.getId());
					rowJSON.put("medicalExaminationDefId", medicalExaminationDef.getId());
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

	@RequestMapping
	public ModelAndView list(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/medicalExaminationDef/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("medicalExaminationDef.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/heathcare/medicalExaminationDef/query", modelMap);
	}

	@RequestMapping("/reviewJson")
	@ResponseBody
	public byte[] reviewJson(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		MedicalExaminationDefQuery query = new MedicalExaminationDefQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);

		JSONObject result = new JSONObject();
		String tenantId = request.getParameter("tenantId");
		if (tenantId != null && loginContext.getRoles().contains("HygieneOrg")
				&& loginContext.getManagedTenantIds().contains(tenantId)) {
			query.tenantId(tenantId);
		} else {
			return result.toJSONString().getBytes();
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

		int total = medicalExaminationDefService.getMedicalExaminationDefCountByQueryCriteria(query);
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

			List<MedicalExaminationDef> list = medicalExaminationDefService
					.getMedicalExaminationDefsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (MedicalExaminationDef medicalExaminationDef : list) {
					JSONObject rowJSON = medicalExaminationDef.toJsonObject();
					rowJSON.put("id", medicalExaminationDef.getId());
					rowJSON.put("rowId", medicalExaminationDef.getId());
					rowJSON.put("medicalExaminationDefId", medicalExaminationDef.getId());
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

	@RequestMapping("/reviewlist")
	public ModelAndView reviewlist(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/medicalExaminationDef/review_list", modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveMedicalExaminationDef")
	public byte[] saveMedicalExaminationDef(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		long id = RequestUtils.getLong(request, "id");
		MedicalExaminationDef medicalExaminationDef = null;
		try {
			medicalExaminationDef = medicalExaminationDefService.getMedicalExaminationDef(id);
			if (medicalExaminationDef == null) {
				medicalExaminationDef = new MedicalExaminationDef();
			}
			medicalExaminationDef.setTenantId(loginContext.getTenantId());
			medicalExaminationDef.setTitle(request.getParameter("title"));
			medicalExaminationDef.setType(request.getParameter("type"));
			medicalExaminationDef.setCheckDate(RequestUtils.getDate(request, "checkDate"));
			medicalExaminationDef.setRemark(request.getParameter("remark"));
			medicalExaminationDef.setEnableFlag(request.getParameter("enableFlag"));

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(medicalExaminationDef.getCheckDate());
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH) + 1;
			medicalExaminationDef.setYear(year);
			medicalExaminationDef.setMonth(month);
			medicalExaminationDef.setDay(calendar.get(Calendar.DAY_OF_MONTH));
			medicalExaminationDef.setCreateBy(actorId);
			this.medicalExaminationDefService.save(medicalExaminationDef);

			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.medicalExaminationDefService")
	public void setMedicalExaminationDefService(MedicalExaminationDefService medicalExaminationDefService) {
		this.medicalExaminationDefService = medicalExaminationDefService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.medicalExaminationService")
	public void setMedicalExaminationService(MedicalExaminationService medicalExaminationService) {
		this.medicalExaminationService = medicalExaminationService;
	}

}
