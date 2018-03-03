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
import com.glaf.heathcare.domain.TriphopathiaItem;
import com.glaf.heathcare.query.TriphopathiaItemQuery;
import com.glaf.heathcare.service.GradeInfoService;
import com.glaf.heathcare.service.PersonService;
import com.glaf.heathcare.service.TriphopathiaItemService;
import com.glaf.heathcare.service.TriphopathiaService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/triphopathiaItem")
@RequestMapping("/heathcare/triphopathiaItem")
public class TriphopathiaItemController {
	protected static final Log logger = LogFactory.getLog(TriphopathiaItemController.class);

	protected GradeInfoService gradeInfoService;

	protected PersonService personService;

	protected TriphopathiaService triphopathiaService;

	protected TriphopathiaItemService triphopathiaItemService;

	public TriphopathiaItemController() {

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
					TriphopathiaItem triphopathiaItem = triphopathiaItemService.getTriphopathiaItem(Long.valueOf(x));
					if (triphopathiaItem != null) {
						if ((loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician"))
								&& (StringUtils.equals(loginContext.getTenantId(), triphopathiaItem.getTenantId()))
								|| loginContext.getGradeIds().contains(triphopathiaItem.getGradeId())) {
							triphopathiaItemService.deleteById(triphopathiaItem.getId());
						}
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			TriphopathiaItem triphopathiaItem = triphopathiaItemService.getTriphopathiaItem(Long.valueOf(id));
			if (triphopathiaItem != null) {
				if ((loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician"))
						&& (StringUtils.equals(loginContext.getTenantId(), triphopathiaItem.getTenantId()))
						|| loginContext.getGradeIds().contains(triphopathiaItem.getGradeId())) {
					triphopathiaItemService.deleteById(triphopathiaItem.getId());
					return ResponseUtils.responseResult(true);
				}
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		TriphopathiaItem triphopathiaItem = triphopathiaItemService
				.getTriphopathiaItem(RequestUtils.getLong(request, "id"));
		if (triphopathiaItem != null) {
			request.setAttribute("triphopathiaItem", triphopathiaItem);
			String personId = triphopathiaItem.getPersonId();
			if (StringUtils.isNotEmpty(personId)) {
				Person person = personService.getPerson(personId);
				request.setAttribute("person", person);
			}
		} else {
			String personId = request.getParameter("personId");
			if (StringUtils.isNotEmpty(personId)) {
				Person person = personService.getPerson(personId);
				personId = person.getId();
				request.setAttribute("person", person);
			} else {
				String gradeId = request.getParameter("gradeId");
				if (gradeId != null && gradeId.trim().length() > 0) {
					List<Person> persons = personService.getPersons(gradeId);
					request.setAttribute("persons", persons);
				}
			}
		}

		long triphopathiaId = RequestUtils.getLong(request, "triphopathiaId");
		Triphopathia triphopathia = triphopathiaService.getTriphopathia(triphopathiaId);
		request.setAttribute("triphopathia", triphopathia);
		if (request.getAttribute("person") == null) {
			String personId = triphopathia.getPersonId();
			Person person = personService.getPerson(personId);
			request.setAttribute("person", person);
		}
		
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("triphopathiaItem.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/triphopathiaItem/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		TriphopathiaItemQuery query = new TriphopathiaItemQuery();
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

		int total = triphopathiaItemService.getTriphopathiaItemCountByQueryCriteria(query);
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

			List<TriphopathiaItem> list = triphopathiaItemService.getTriphopathiaItemsByQueryCriteria(start, limit,
					query);

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

				for (TriphopathiaItem triphopathiaItem : list) {
					JSONObject rowJSON = triphopathiaItem.toJsonObject();
					rowJSON.put("id", triphopathiaItem.getId());
					rowJSON.put("rowId", triphopathiaItem.getId());
					rowJSON.put("triphopathiaItemId", triphopathiaItem.getId());
					rowJSON.put("startIndex", ++start);
					person = personMap.get(triphopathiaItem.getPersonId());
					if (person != null) {
						rowJSON.put("sex", person.getSex());
						if (person.getBirthday() != null) {
							triphopathiaItem.setBirthday(person.getBirthday());
							rowJSON.put("birthday", DateUtils.getDate(person.getBirthday()));
							rowJSON.put("birthday_date", DateUtils.getDate(person.getBirthday()));
							rowJSON.put("birthday_datetime", DateUtils.getDateTime(person.getBirthday()));
							rowJSON.put("checkAgeOfMonth",
									triphopathiaItem.getAgeOfTheMoon() > 0 ? triphopathiaItem.getAgeOfTheMoon() : "-");
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

		long triphopathiaId = RequestUtils.getLong(request, "triphopathiaId");
		Triphopathia triphopathia = triphopathiaService.getTriphopathia(triphopathiaId);
		request.setAttribute("triphopathia", triphopathia);
		
		String personId = triphopathia.getPersonId();
		Person person = personService.getPerson(personId);
		request.setAttribute("person", person);
		request.setAttribute("personId", person.getId());
		

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/triphopathiaItem/list", modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveTriphopathiaItem")
	public byte[] saveTriphopathiaItem(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		String actorId = loginContext.getActorId();
		long id = RequestUtils.getLong(request, "id");
		String gradeId = request.getParameter("gradeId");
		String personId = request.getParameter("personId");
		TriphopathiaItem triphopathiaItem = null;
		try {
			if (id > 0) {
				triphopathiaItem = triphopathiaItemService.getTriphopathiaItem(id);
			}

			Person person = personService.getPerson(personId);

			if (triphopathiaItem == null) {
				triphopathiaItem = new TriphopathiaItem();
				Tools.populate(triphopathiaItem, params);
				triphopathiaItem.setTriphopathiaId(RequestUtils.getLong(request, "triphopathiaId"));
				triphopathiaItem.setTenantId(loginContext.getTenantId());
				triphopathiaItem.setGradeId(request.getParameter("gradeId"));
				triphopathiaItem.setPersonId(person.getId());
				triphopathiaItem.setName(person.getName());
				triphopathiaItem.setSex(person.getSex());
				triphopathiaItem.setBirthday(person.getBirthday());
			} else {
				if (loginContext.isTenantAdmin() || loginContext.getRoles().contains("HealthPhysician")) {
					if (!(StringUtils.equals(loginContext.getTenantId(), triphopathiaItem.getTenantId()))) {
						return ResponseUtils.responseJsonResult(false, "您没有该数据操作权限。");
					}
				} else {
					if (!loginContext.getGradeIds().contains(gradeId)) {
						return ResponseUtils.responseJsonResult(false, "您没有该数据操作权限。");
					}
				}
			}

			triphopathiaItem.setType(request.getParameter("type"));
			triphopathiaItem.setHeight(RequestUtils.getDouble(request, "height"));
			triphopathiaItem.setWeight(RequestUtils.getDouble(request, "weight"));
			triphopathiaItem.setSymptom(request.getParameter("symptom"));
			triphopathiaItem.setSuggest(request.getParameter("suggest"));
			triphopathiaItem.setResult(request.getParameter("result"));
			triphopathiaItem.setEvaluate(request.getParameter("evaluate"));
			triphopathiaItem.setRemark(request.getParameter("remark"));
			triphopathiaItem.setCheckDoctor(request.getParameter("checkDoctor"));
			triphopathiaItem.setCheckDoctorId(request.getParameter("checkDoctorId"));
			if (StringUtils.isEmpty(triphopathiaItem.getCheckDoctorId())) {
				triphopathiaItem.setCheckDoctorId(actorId);
			}
			triphopathiaItem.setCreateBy(actorId);
			this.triphopathiaItemService.save(triphopathiaItem);

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

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.triphopathiaItemService")
	public void setTriphopathiaItemService(TriphopathiaItemService triphopathiaItemService) {
		this.triphopathiaItemService = triphopathiaItemService;
	}

}
