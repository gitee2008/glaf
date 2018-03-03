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
import java.util.Date;
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
import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;

import com.glaf.heathcare.domain.GradeInfo;
import com.glaf.heathcare.domain.Person;
import com.glaf.heathcare.domain.PersonLinkman;
import com.glaf.heathcare.helper.PermissionHelper;
import com.glaf.heathcare.query.PersonLinkmanQuery;
import com.glaf.heathcare.query.PersonQuery;
import com.glaf.heathcare.service.GradeInfoService;
import com.glaf.heathcare.service.PersonLinkmanService;
import com.glaf.heathcare.service.PersonService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/person")
@RequestMapping("/heathcare/person")
public class PersonController {
	protected static final Log logger = LogFactory.getLog(PersonController.class);

	protected DistrictService districtService;

	protected GradeInfoService gradeInfoService;

	protected PersonService personService;

	protected PersonLinkmanService personLinkmanService;

	public PersonController() {

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
					Person person = personService.getPerson(String.valueOf(x));
					if (person != null && ((StringUtils.equals(person.getTenantId(), loginContext.getTenantId())
							&& (loginContext.getRoles().contains("TenantAdmin")
									|| loginContext.getRoles().contains("HealthPhysician")
									|| loginContext.getRoles().contains("Teacher"))))) {
						person.setDeleteFlag(1);
						personService.update(person);
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			Person person = personService.getPerson(String.valueOf(id));
			if (person != null && ((StringUtils.equals(person.getTenantId(), loginContext.getTenantId())
					&& (loginContext.getRoles().contains("TenantAdmin")
							|| loginContext.getRoles().contains("HealthPhysician")
							|| loginContext.getRoles().contains("Teacher"))))) {
				person.setDeleteFlag(1);
				personService.update(person);
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		List<District> provinces = districtService.getDistrictList(0);
		request.setAttribute("provinces", provinces);

		List<GradeInfo> list = gradeInfoService.getGradeInfosByTenantId(loginContext.getTenantId());
		request.setAttribute("gradeInfos", list);

		String gradeId = request.getParameter("gradeId");
		Person person = personService.getPerson(request.getParameter("id"));
		if (person != null) {
			request.setAttribute("person", person);
			request.setAttribute("gradeId", person.getGradeId());
			gradeId = person.getGradeId();

			if (person.getProvinceId() > 0) {
				List<District> citys = districtService.getDistrictList(person.getProvinceId());
				request.setAttribute("citys", citys);
			}
			if (person.getCityId() > 0) {
				List<District> areas = districtService.getDistrictList(person.getCityId());
				request.setAttribute("areas", areas);
			}
			if (person.getAreaId() > 0) {
				List<District> towns = districtService.getDistrictList(person.getAreaId());
				request.setAttribute("towns", towns);
			}

			PersonLinkmanQuery query = new PersonLinkmanQuery();
			query.personId(person.getId());
			List<PersonLinkman> linkmans = personLinkmanService.list(query);
			if (linkmans != null && !linkmans.isEmpty()) {
				request.setAttribute("linkmans", linkmans);
				for (PersonLinkman linkman : linkmans) {
					if (linkman.getRelationship() != null) {
						request.setAttribute(linkman.getRelationship(), linkman);
					}
				}
			}

		} else {
			request.setAttribute("gradeId", gradeId);
		}

		boolean privilege_write = false;
		if (loginContext.isSystemAdministrator() || loginContext.getRoles().contains("TenantAdmin")
				|| loginContext.getRoles().contains("HealthPhysician") || loginContext.getRoles().contains("Teacher")) {
			privilege_write = true;
		} else {
			if (loginContext.isTenantAdmin()) {
				if (person != null && StringUtils.equals(loginContext.getTenantId(), person.getTenantId())) {
					privilege_write = true;
				}
			} else {
				if (loginContext.getGradeIds().contains(gradeId) && loginContext.getRoles().contains("Teacher")) {
					privilege_write = true;
				}
			}
		}

		String nameLike = request.getParameter("nameLike");
		if (StringUtils.isNotEmpty(nameLike)) {
			nameLike = RequestUtils.encodeString(nameLike);
			request.setAttribute("nameLike_enc", nameLike);
		}

		request.setAttribute("privilege_write", privilege_write);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("person.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/person/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug("params:" + params);
		PersonQuery query = new PersonQuery();
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

		String nameLike = request.getParameter("nameLike_enc");
		if (StringUtils.isNotEmpty(nameLike)) {
			nameLike = RequestUtils.decodeString(nameLike);
			query.setNameLike(nameLike);
		}

		int start = 0;
		int limit = 50;
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
			limit = 50;
		}

		JSONObject result = new JSONObject();
		int total = personService.getPersonCountByQueryCriteria(query);
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

			logger.debug("limit:" + limit);
			List<Person> list = personService.getPersonsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (Person person : list) {
					JSONObject rowJSON = person.toJsonObject();
					rowJSON.put("id", person.getId());
					rowJSON.put("rowId", person.getId());
					rowJSON.put("personId", person.getId());
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

		List<GradeInfo> list = gradeInfoService.getGradeInfosByTenantId(loginContext.getTenantId());
		request.setAttribute("gradeInfos", list);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/person/list", modelMap);
	}

	@RequestMapping("/list2")
	public ModelAndView list2(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		RequestUtils.setRequestParameterToAttribute(request);
		PermissionHelper helper = new PermissionHelper();
		helper.setPermission(request);

		List<GradeInfo> list = gradeInfoService.getGradeInfosByTenantId(loginContext.getTenantId());
		request.setAttribute("gradeInfos", list);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/person/list2", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("person.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/heathcare/person/query", modelMap);
	}

	@ResponseBody
	@RequestMapping("/savePerson")
	public byte[] savePerson(HttpServletRequest request) {
		logger.debug("params:" + RequestUtils.getParameterMap(request));
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		String id = request.getParameter("id");
		String gradeId = request.getParameter("gradeId");
		Person person = null;
		try {
			if (StringUtils.isNotEmpty(id)) {
				person = personService.getPerson(id);
			}
			if (person == null) {
				person = new Person();
				person.setTenantId(loginContext.getTenantId());
			} else {
				if (!loginContext.isSystemAdministrator()) {
					if (loginContext.isTenantAdmin()) {
						if (!StringUtils.equals(loginContext.getTenantId(), person.getTenantId())) {
							return ResponseUtils.responseJsonResult(false, "您没有该数据操作权限。");
						}
					} else {
						if (!loginContext.getGradeIds().contains(gradeId)) {
							return ResponseUtils.responseJsonResult(false, "您没有该数据操作权限。");
						}
					}
				}
			}

			String idCardNo = request.getParameter("idCardNo");
			if (StringUtils.isNotEmpty(idCardNo) && StringUtils.isNumeric(idCardNo)) {
				String bd = idCardNo.substring(6, 14);
				// logger.debug("birth day:" + bd);
				Date birthday = RequestUtils.getDate(request, "birthday");
				if (!StringUtils.equals(bd, DateUtils.getYearMonthDay(birthday) + "")) {
					return ResponseUtils.responseJsonResult(false, "身份证日期与出生日期不匹配。");
				}
			}

			person.setGradeId(gradeId);
			person.setIdCardNo(idCardNo);
			person.setName(request.getParameter("name"));
			person.setBirthday(RequestUtils.getDate(request, "birthday"));
			person.setPatriarch(request.getParameter("patriarch"));
			person.setTelephone(request.getParameter("telephone"));
			person.setProvinceId(RequestUtils.getLong(request, "provinceId"));
			person.setCityId(RequestUtils.getLong(request, "cityId"));
			person.setAreaId(RequestUtils.getLong(request, "areaId"));
			person.setTownId(RequestUtils.getLong(request, "townId"));
			person.setHomeAddress(request.getParameter("homeAddress"));
			person.setBirthAddress(request.getParameter("birthAddress"));
			person.setSex(request.getParameter("sex"));
			person.setRemark(request.getParameter("remark"));
			person.setFeedingHistory(request.getParameter("feedingHistory"));
			person.setPreviousHistory(request.getParameter("previousHistory"));
			person.setFoodAllergy(request.getParameter("foodAllergy"));
			person.setMedicineAllergy(request.getParameter("medicineAllergy"));
			person.setFather(request.getParameter("father"));
			person.setFatherTelephone(request.getParameter("fatherTelephone"));
			person.setFatherCompany(request.getParameter("fatherCompany"));
			person.setFatherWardship(request.getParameter("fatherWardship"));
			person.setMother(request.getParameter("mother"));
			person.setMotherTelephone(request.getParameter("motherTelephone"));
			person.setMotherCompany(request.getParameter("motherCompany"));
			person.setMotherWardship(request.getParameter("motherWardship"));
			person.setHeight(RequestUtils.getDouble(request, "height"));
			person.setWeight(RequestUtils.getDouble(request, "weight"));
			person.setCreateBy(actorId);

			this.personService.save(person);

			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.gradeInfoService")
	public void setGradeInfoService(GradeInfoService gradeInfoService) {
		this.gradeInfoService = gradeInfoService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.personService")
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.personLinkmanService")
	public void setPersonLinkmanService(PersonLinkmanService personLinkmanService) {
		this.personLinkmanService = personLinkmanService;
	}

	@RequestMapping("/view")
	public ModelAndView view(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		Person person = personService.getPerson(request.getParameter("id"));
		request.setAttribute("person", person);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view);
		}

		String x_view = ViewProperties.getString("person.view");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view);
		}

		return new ModelAndView("/heathcare/person/view");
	}

}
