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
import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;

import com.glaf.heathcare.domain.Person;
import com.glaf.heathcare.domain.Triphopathia;
import com.glaf.heathcare.query.TriphopathiaQuery;
import com.glaf.heathcare.service.GradeInfoService;
import com.glaf.heathcare.service.PersonService;
import com.glaf.heathcare.service.TriphopathiaService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/triphopathia")
@RequestMapping("/heathcare/triphopathia")
public class TriphopathiaController {
	protected static final Log logger = LogFactory.getLog(TriphopathiaController.class);

	protected GradeInfoService gradeInfoService;

	protected PersonService personService;

	protected TriphopathiaService triphopathiaService;

	public TriphopathiaController() {

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
					Triphopathia triphopathia = triphopathiaService.getTriphopathia(Long.valueOf(x));
					if (triphopathia != null) {
						if ((loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician"))
								&& (StringUtils.equals(loginContext.getTenantId(), triphopathia.getTenantId()))
								|| loginContext.getGradeIds().contains(triphopathia.getGradeId())) {
							triphopathiaService.deleteById(triphopathia.getId());
						}
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			Triphopathia triphopathia = triphopathiaService.getTriphopathia(Long.valueOf(id));
			if (triphopathia != null) {
				if ((loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician"))
						&& (StringUtils.equals(loginContext.getTenantId(), triphopathia.getTenantId()))
						|| loginContext.getGradeIds().contains(triphopathia.getGradeId())) {
					triphopathiaService.deleteById(triphopathia.getId());
					return ResponseUtils.responseResult(true);
				}
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		Triphopathia triphopathia = triphopathiaService.getTriphopathia(RequestUtils.getLong(request, "id"));
		if (triphopathia != null) {
			request.setAttribute("triphopathia", triphopathia);
			String personId = triphopathia.getPersonId();
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

		String x_view = ViewProperties.getString("triphopathia.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/triphopathia/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		TriphopathiaQuery query = new TriphopathiaQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		JSONObject result = new JSONObject();

		String gradeId = request.getParameter("gradeId");

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

		int total = triphopathiaService.getTriphopathiaCountByQueryCriteria(query);
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

			List<Triphopathia> list = triphopathiaService.getTriphopathiasByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				List<Person> persons = null;
				if (StringUtils.isNotEmpty(gradeId)) {
					persons = personService.getPersons(gradeId);
				} else {
					persons = personService.getTenantPersons(loginContext.getTenantId());
				}

				Map<String, Person> personMap = new HashMap<String, Person>();
				if (persons != null && !persons.isEmpty()) {
					for (Person person : persons) {
						personMap.put(person.getId(), person);
					}
				}

				Person person = null;

				for (Triphopathia triphopathia : list) {
					JSONObject rowJSON = triphopathia.toJsonObject();
					rowJSON.put("id", triphopathia.getId());
					rowJSON.put("rowId", triphopathia.getId());
					rowJSON.put("triphopathiaId", triphopathia.getId());
					rowJSON.put("startIndex", ++start);
					person = personMap.get(triphopathia.getPersonId());
					if (person != null) {
						rowJSON.put("sex", person.getSex());
						if (person.getBirthday() != null) {
							triphopathia.setBirthday(person.getBirthday());
							rowJSON.put("birthday", DateUtils.getDate(person.getBirthday()));
							rowJSON.put("birthday_date", DateUtils.getDate(person.getBirthday()));
							rowJSON.put("birthday_datetime", DateUtils.getDateTime(person.getBirthday()));
							rowJSON.put("checkAgeOfMonth",
									triphopathia.getAgeOfTheMoon() > 0 ? triphopathia.getAgeOfTheMoon() : "-");
						}
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

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/triphopathia/list", modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveTriphopathia")
	public byte[] saveTriphopathia(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		String actorId = loginContext.getActorId();
		long id = RequestUtils.getLong(request, "id");
		String gradeId = request.getParameter("gradeId");
		String personId = request.getParameter("personId");
		Triphopathia triphopathia = null;
		try {
			if (id > 0) {
				triphopathia = triphopathiaService.getTriphopathia(id);
			}

			Person person = personService.getPerson(personId);

			if (triphopathia == null) {
				triphopathia = new Triphopathia();
				Tools.populate(triphopathia, params);
				triphopathia.setTenantId(loginContext.getTenantId());
				triphopathia.setGradeId(request.getParameter("gradeId"));
				triphopathia.setPersonId(person.getId());
				triphopathia.setName(person.getName());
				triphopathia.setSex(person.getSex());
				triphopathia.setBirthday(person.getBirthday());
			} else {
				if (loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician")) {
					if (!(StringUtils.equals(loginContext.getTenantId(), triphopathia.getTenantId()))) {
						return ResponseUtils.responseJsonResult(false, "您没有该数据操作权限。");
					}
				} else {
					if (!loginContext.getGradeIds().contains(gradeId)) {
						return ResponseUtils.responseJsonResult(false, "您没有该数据操作权限。");
					}
				}
			}

			triphopathia.setType(request.getParameter("type"));
			triphopathia.setDiseaseName(request.getParameter("diseaseName"));
			triphopathia.setDiscoverDate(RequestUtils.getDate(request, "discoverDate"));
			triphopathia.setClinicDate(RequestUtils.getDate(request, "clinicDate"));
			triphopathia.setClinicOrg(request.getParameter("clinicOrg"));
			triphopathia.setMedicalHistory(request.getParameter("medicalHistory"));
			triphopathia.setArchivingDate(RequestUtils.getDate(request, "archivingDate"));
			triphopathia.setCloseDate(RequestUtils.getDate(request, "closeDate"));
			triphopathia.setClinicalSituation(request.getParameter("clinicalSituation"));
			triphopathia.setHeight(RequestUtils.getDouble(request, "height"));
			triphopathia.setWeight(RequestUtils.getDouble(request, "weight"));
			triphopathia.setSymptom(request.getParameter("symptom"));
			triphopathia.setSuggest(request.getParameter("suggest"));
			triphopathia.setResult(request.getParameter("result"));
			triphopathia.setEvaluate(request.getParameter("evaluate"));
			triphopathia.setRemark(request.getParameter("remark"));
			triphopathia.setCheckDoctor(request.getParameter("checkDoctor"));
			triphopathia.setCheckDoctorId(request.getParameter("checkDoctorId"));
			if (StringUtils.isEmpty(triphopathia.getCheckDoctorId())) {
				triphopathia.setCheckDoctorId(actorId);
			}
			triphopathia.setCreateBy(actorId);
			this.triphopathiaService.save(triphopathia);

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

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.triphopathiaService")
	public void setTriphopathiaService(TriphopathiaService triphopathiaService) {
		this.triphopathiaService = triphopathiaService;
	}

}
