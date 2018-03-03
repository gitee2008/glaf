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
import com.glaf.heathcare.helper.PermissionHelper;
import com.glaf.heathcare.query.*;
import com.glaf.heathcare.service.*;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/personSickness")
@RequestMapping("/heathcare/personSickness")
public class PersonSicknessController {
	protected static final Log logger = LogFactory.getLog(PersonSicknessController.class);

	protected GradeInfoService gradeInfoService;

	protected PersonService personService;

	protected PersonSicknessService personSicknessService;

	public PersonSicknessController() {

	}

	@ResponseBody
	@RequestMapping("/delete")
	public byte[] delete(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String id = RequestUtils.getString(request, "id");
		String ids = request.getParameter("ids");
		if (StringUtils.isNotEmpty(ids)) {
			StringTokenizer token = new StringTokenizer(ids, ",");
			while (token.hasMoreTokens()) {
				String x = token.nextToken();
				if (StringUtils.isNotEmpty(x)) {
					PersonSickness personSickness = personSicknessService.getPersonSickness(String.valueOf(x));
					if (personSickness != null
							&& ((StringUtils.equals(personSickness.getTenantId(), loginContext.getTenantId())
									&& (loginContext.getRoles().contains("TenantAdmin")
											|| loginContext.getRoles().contains("HealthPhysician")
											|| loginContext.getRoles().contains("Teacher"))))) {
						if ((DateUtils.getDaysBetween(personSickness.getCreateTime(), new Date())) < 15) {
							personSicknessService.deleteById(personSickness.getId());
						} else {
							return ResponseUtils.responseJsonResult(false, "只能删除两周以内的数据！");
						}
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			PersonSickness personSickness = personSicknessService.getPersonSickness(String.valueOf(id));
			if (personSickness != null && ((StringUtils.equals(personSickness.getTenantId(), loginContext.getTenantId())
					&& (loginContext.getRoles().contains("TenantAdmin")
							|| loginContext.getRoles().contains("HealthPhysician")
							|| loginContext.getRoles().contains("Teacher"))))) {
				if ((DateUtils.getDaysBetween(personSickness.getCreateTime(), new Date())) < 15) {
					personSicknessService.deleteById(personSickness.getId());
					return ResponseUtils.responseResult(true);
				} else {
					return ResponseUtils.responseJsonResult(false, "只能删除两周以内的数据！");
				}
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String personId = request.getParameter("personId");
		String gradeId = request.getParameter("gradeId");

		PersonSickness personSickness = personSicknessService.getPersonSickness(request.getParameter("id"));
		if (personSickness != null) {
			gradeId = personSickness.getGradeId();
			personId = personSickness.getPersonId();
			request.setAttribute("personSickness", personSickness);
		}

		if (StringUtils.isNotEmpty(personId)) {
			Person person = personService.getPerson(personId);
			request.setAttribute("person", person);
		} else {
			if (gradeId != null && gradeId.trim().length() > 0) {
				List<Person> persons = personService.getPersons(gradeId);
				request.setAttribute("persons", persons);
			}
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("personSickness.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/personSickness/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		PersonSicknessQuery query = new PersonSicknessQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		if (!loginContext.isSystemAdministrator()) {
			query.tenantId(loginContext.getTenantId());
			if (!loginContext.hasPermission("TenantAdmin", "or")) {
				query.gradeIds(loginContext.getGradeIds());
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

		JSONObject result = new JSONObject();
		int total = personSicknessService.getPersonSicknessCountByQueryCriteria(query);
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

			List<PersonSickness> list = personSicknessService.getPersonSicknesssByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (PersonSickness personSickness : list) {
					JSONObject rowJSON = personSickness.toJsonObject();
					rowJSON.put("id", personSickness.getId());
					rowJSON.put("rowId", personSickness.getId());
					rowJSON.put("personSicknessId", personSickness.getId());
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
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		RequestUtils.setRequestParameterToAttribute(request);

		PermissionHelper helper = new PermissionHelper();
		helper.setPermission(request);

		String gradeId = request.getParameter("gradeId");
		String personId = request.getParameter("personId");
		if (StringUtils.isNotEmpty(personId)) {
			Person person = personService.getPerson(personId);
			request.setAttribute("person", person);
		} else {
			if (StringUtils.isNotEmpty(gradeId)) {
				GradeInfo gradeInfo = gradeInfoService.getGradeInfo(gradeId);
				request.setAttribute("gradeInfo", gradeInfo);
			}
		}

		List<GradeInfo> list = gradeInfoService.getGradeInfosByTenantId(loginContext.getTenantId());
		request.setAttribute("gradeInfos", list);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR);
		List<Integer> years = new ArrayList<Integer>();
		years.add(2017);
		years.add(year);
		if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER) {
			years.add(year - 1);
		}
		request.setAttribute("years", years);

		List<Integer> months = new ArrayList<Integer>();

		for (int i = 1; i <= 12; i++) {
			months.add(i);
		}

		request.setAttribute("months", months);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/personSickness/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("personSickness.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/heathcare/personSickness/query", modelMap);
	}

	@ResponseBody
	@RequestMapping("/savePersonSickness")
	public byte[] savePersonSickness(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		String id = request.getParameter("id");
		PersonSickness personSickness = null;
		try {
			if (StringUtils.isNotEmpty(id)) {
				personSickness = personSicknessService.getPersonSickness(id);
			}
			if (personSickness == null) {
				personSickness = new PersonSickness();

				Person person = personService.getPerson(request.getParameter("personId"));
				if (person != null) {
					Tools.populate(personSickness, params);
					personSickness.setName(person.getName());
					personSickness.setGradeId(person.getGradeId());
					personSickness.setPersonId(person.getId());
				}

				personSickness.setTenantId(loginContext.getTenantId());

			} else {
				if (personSickness != null
						&& !((StringUtils.equals(personSickness.getTenantId(), loginContext.getTenantId())
								&& (loginContext.getRoles().contains("TenantAdmin")
										|| loginContext.getRoles().contains("HealthPhysician")
										|| loginContext.getRoles().contains("Teacher"))))) {
					return ResponseUtils.responseJsonResult(false, "您没有该数据操作权限。");
				}
			}

			personSickness.setSickness(request.getParameter("sickness"));
			personSickness.setInfectiousFlag(request.getParameter("infectiousFlag"));
			personSickness.setInfectiousDisease(request.getParameter("infectiousDisease"));
			personSickness.setDiscoverTime(RequestUtils.getDate(request, "discoverTime"));
			personSickness.setReportTime(RequestUtils.getDate(request, "reportTime"));
			personSickness.setReportOrg(request.getParameter("reportOrg"));
			personSickness.setReportResponsible(request.getParameter("reportResponsible"));
			personSickness.setSupervisionOpinion(request.getParameter("supervisionOpinion"));
			personSickness.setConfirmTime(RequestUtils.getDate(request, "confirmTime"));
			personSickness.setReceiver1(request.getParameter("receiver1"));
			personSickness.setReceiveOrg1(request.getParameter("receiveOrg1"));
			personSickness.setReceiver2(request.getParameter("receiver2"));
			personSickness.setReceiveOrg2(request.getParameter("receiveOrg2"));
			personSickness.setSymptom(request.getParameter("symptom"));
			personSickness.setTreat(request.getParameter("treat"));
			personSickness.setHospital(request.getParameter("hospital"));
			personSickness.setClinicTime(RequestUtils.getDate(request, "clinicTime"));
			personSickness.setResult(request.getParameter("result"));
			personSickness.setRemark(request.getParameter("remark"));
			personSickness.setCreateBy(actorId);
			String[] reportWays = request.getParameterValues("reportWay");
			if (reportWays != null && reportWays.length > 0) {
				StringBuilder buff = new StringBuilder();
				for (String reportWay : reportWays) {
					buff.append(reportWay).append(", ");
				}
				personSickness.setReportWay(buff.toString());
			} else {
				personSickness.setReportWay("");
			}
			this.personSicknessService.save(personSickness);

			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
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

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.personSicknessService")
	public void setPersonSicknessService(PersonSicknessService personSicknessService) {
		this.personSicknessService = personSicknessService;
	}

	@RequestMapping("/view")
	public ModelAndView view(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		PersonSickness personSickness = personSicknessService.getPersonSickness(request.getParameter("id"));
		request.setAttribute("personSickness", personSickness);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view);
		}

		String x_view = ViewProperties.getString("personSickness.view");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view);
		}

		return new ModelAndView("/heathcare/personSickness/view");
	}

}
