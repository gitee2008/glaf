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

@Controller("/heathcare/vaccination")
@RequestMapping("/heathcare/vaccination")
public class VaccinationController {
	protected static final Log logger = LogFactory.getLog(VaccinationController.class);

	protected GradeInfoService gradeInfoService;

	protected PersonService personService;

	protected VaccinationService vaccinationService;

	public VaccinationController() {

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
					Vaccination vaccination = vaccinationService.getVaccination(Long.valueOf(x));
					if ((loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician"))
							&& (StringUtils.equals(loginContext.getTenantId(), vaccination.getTenantId()))
							|| loginContext.getGradeIds().contains(vaccination.getGradeId())) {
						vaccinationService.deleteById(vaccination.getId());
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			Vaccination vaccination = vaccinationService.getVaccination(Long.valueOf(id));
			if ((loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician"))
					&& (StringUtils.equals(loginContext.getTenantId(), vaccination.getTenantId()))
					|| loginContext.getGradeIds().contains(vaccination.getGradeId())) {
				vaccinationService.deleteById(vaccination.getId());
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		Vaccination vaccination = vaccinationService.getVaccination(RequestUtils.getLong(request, "id"));
		if (vaccination != null) {
			request.setAttribute("vaccination", vaccination);
			String personId = vaccination.getPersonId();
			if (StringUtils.isNotEmpty(personId)) {
				Person person = personService.getPerson(personId);
				request.setAttribute("person", person);
			}
		} else {
			String personId = request.getParameter("personId");
			if (StringUtils.isNotEmpty(personId)) {
				Person person = personService.getPerson(personId);
				request.setAttribute("person", person);
			} else {
				String gradeId = request.getParameter("gradeId");
				if (gradeId != null && gradeId.trim().length() > 0) {
					List<Person> persons = personService.getPersons(gradeId);
					request.setAttribute("persons", persons);
				}
			}
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("vaccination.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/vaccination/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		VaccinationQuery query = new VaccinationQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		JSONObject result = new JSONObject();

		String gradeId = request.getParameter("gradeId");

		if (StringUtils.isNotEmpty(gradeId)) {
			query.gradeId(gradeId);
		}

		if (loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician")) {
			query.tenantId(loginContext.getTenantId());
		} else if (loginContext.getRoles().contains("Teacher")) {
			query.tenantId(loginContext.getTenantId());
			if (loginContext.getGradeIds().contains(gradeId)) {
				query.gradeId(gradeId);
			} else {
				return result.toJSONString().getBytes();
			}
		} else {
			if (!loginContext.getGradeIds().contains(gradeId)) {
				return result.toJSONString().getBytes();
			}
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

		int total = vaccinationService.getVaccinationCountByQueryCriteria(query);
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

			List<Vaccination> list = vaccinationService.getVaccinationsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (Vaccination vaccination : list) {
					JSONObject rowJSON = vaccination.toJsonObject();
					rowJSON.put("id", vaccination.getId());
					rowJSON.put("rowId", vaccination.getId());
					rowJSON.put("vaccinationId", vaccination.getId());
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

		return new ModelAndView("/heathcare/vaccination/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("vaccination.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/heathcare/vaccination/query", modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveVaccination")
	public byte[] saveVaccination(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		long id = RequestUtils.getLong(request, "id");
		String gradeId = request.getParameter("gradeId");
		String personId = request.getParameter("personId");
		Vaccination vaccination = null;
		try {
			if (id > 0) {
				vaccination = vaccinationService.getVaccination(id);
			}

			Person person = personService.getPerson(personId);

			if (vaccination == null) {
				vaccination = new Vaccination();
				Tools.populate(vaccination, params);
				vaccination.setTenantId(loginContext.getTenantId());
				vaccination.setGradeId(person.getGradeId());
				vaccination.setPersonId(person.getId());
				vaccination.setName(person.getName());
				vaccination.setSex(person.getSex());
			} else {
				if (loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician")) {
					if (!(StringUtils.equals(loginContext.getTenantId(), vaccination.getTenantId()))) {
						return ResponseUtils.responseJsonResult(false, "您没有该数据操作权限。");
					}
				} else {
					if (!loginContext.getGradeIds().contains(gradeId)) {
						return ResponseUtils.responseJsonResult(false, "您没有该数据操作权限。");
					}
				}
			}

			vaccination.setVaccine(request.getParameter("vaccine"));
			vaccination.setSortNo(RequestUtils.getInt(request, "sortNo"));
			vaccination.setInoculateDate(RequestUtils.getDate(request, "inoculateDate"));
			vaccination.setDoctor(request.getParameter("doctor"));

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(vaccination.getInoculateDate());
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH) + 1;
			vaccination.setYear(year);
			vaccination.setMonth(month);

			vaccination.setCreateBy(actorId);
			this.vaccinationService.save(vaccination);

			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.gradeInfoService")
	public void setGradeInfoService(GradeInfoService gradeInfoService) {
		this.gradeInfoService = gradeInfoService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.personService")
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.vaccinationService")
	public void setVaccinationService(VaccinationService vaccinationService) {
		this.vaccinationService = vaccinationService;
	}

}
