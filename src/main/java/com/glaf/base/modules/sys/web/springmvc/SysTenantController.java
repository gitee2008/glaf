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

package com.glaf.base.modules.sys.web.springmvc;

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
import com.glaf.core.identity.*;
import com.glaf.core.security.*;
import com.glaf.core.util.*;
import com.glaf.base.district.domain.District;
import com.glaf.base.district.service.DistrictService;
import com.glaf.base.modules.sys.model.*;
import com.glaf.base.modules.sys.query.*;
import com.glaf.base.modules.sys.service.*;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/sys/tenant")
@RequestMapping("/sys/tenant")
public class SysTenantController {
	protected static final Log logger = LogFactory.getLog(SysTenantController.class);

	protected DistrictService districtService;

	protected SysTenantService sysTenantService;

	public SysTenantController() {

	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		List<District> provinces = districtService.getDistrictList(0);
		request.setAttribute("provinces", provinces);

		SysTenant sysTenant = sysTenantService.getSysTenant(RequestUtils.getLong(request, "id"));
		if (sysTenant != null) {
			request.setAttribute("sysTenant", sysTenant);

			if (sysTenant.getProvinceId() > 0) {
				List<District> citys = districtService.getDistrictList(sysTenant.getProvinceId());
				request.setAttribute("citys", citys);
			}
			if (sysTenant.getCityId() > 0) {
				List<District> areas = districtService.getDistrictList(sysTenant.getCityId());
				request.setAttribute("areas", areas);
			}
			if (sysTenant.getAreaId() > 0) {
				List<District> towns = districtService.getDistrictList(sysTenant.getAreaId());
				request.setAttribute("towns", towns);
			}
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("sysTenant.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/tenant/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SysTenantQuery query = new SysTenantQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		String namePinyinLike = request.getParameter("namePinyinLike");
		if (StringUtils.isNotEmpty(namePinyinLike)) {
			query.setNamePinyinLike(namePinyinLike);
		}

		int start = 0;
		int limit = 100;
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
			limit = 100;
		}

		JSONObject result = new JSONObject();
		int total = sysTenantService.getSysTenantCountByQueryCriteria(query);
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

			List<SysTenant> list = sysTenantService.getSysTenantsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (SysTenant sysTenant : list) {
					JSONObject rowJSON = sysTenant.toJsonObject();
					rowJSON.put("id", sysTenant.getId());
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

		List<String> charList = new ArrayList<String>();
		for (int i = 65; i < 91; i++) {
			charList.add("" + (char) i);
		}
		request.setAttribute("charList", charList);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/sys/tenant/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("sysTenant.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/sys/tenant/query", modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveSysTenant")
	public byte[] saveSysTenant(HttpServletRequest request) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		long id = RequestUtils.getLong(request, "id");
		SysTenant sysTenant = null;
		try {
			if (id > 0) {
				sysTenant = sysTenantService.getSysTenant(id);
			}
			if (sysTenant == null) {
				sysTenant = new SysTenant();
				sysTenant.setCreateBy(actorId);
			}
			sysTenant.setName(request.getParameter("name"));
			sysTenant.setCode(request.getParameter("code"));
			sysTenant.setLevel(RequestUtils.getInt(request, "level"));
			sysTenant.setTenantType(RequestUtils.getInt(request, "tenantType"));
			sysTenant.setProvinceId(RequestUtils.getLong(request, "provinceId"));
			if (sysTenant.getProvinceId() > 0) {
				District m = districtService.getDistrict(sysTenant.getProvinceId());
				sysTenant.setProvince(m.getName());
			}
			sysTenant.setCityId(RequestUtils.getLong(request, "cityId"));
			if (sysTenant.getCityId() > 0) {
				District m = districtService.getDistrict(sysTenant.getCityId());
				sysTenant.setCity(m.getName());
			}
			sysTenant.setAreaId(RequestUtils.getLong(request, "areaId"));
			if (sysTenant.getAreaId() > 0) {
				District m = districtService.getDistrict(sysTenant.getAreaId());
				sysTenant.setArea(m.getName());
			}
			sysTenant.setTownId(RequestUtils.getLong(request, "townId"));
			if (sysTenant.getTownId() > 0) {
				District m = districtService.getDistrict(sysTenant.getTownId());
				sysTenant.setTown(m.getName());
			}
			sysTenant.setPrincipal(request.getParameter("principal"));
			sysTenant.setTelephone(request.getParameter("telephone"));
			sysTenant.setAddress(request.getParameter("address"));
			sysTenant.setProperty(request.getParameter("property"));
			sysTenant.setVerify(request.getParameter("verify"));
			sysTenant.setTicketFlag(request.getParameter("ticketFlag"));
			sysTenant.setType(request.getParameter("type"));
			sysTenant.setLimit(RequestUtils.getInt(request, "limit"));
			sysTenant.setLocked(RequestUtils.getInt(request, "locked"));
			sysTenant.setDisableDataConstraint(request.getParameter("disableDataConstraint"));
			sysTenant.setUpdateBy(actorId);
			this.sysTenantService.save(sysTenant);

			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}

	@javax.annotation.Resource
	public void setSysTenantService(SysTenantService sysTenantService) {
		this.sysTenantService = sysTenantService;
	}

}
