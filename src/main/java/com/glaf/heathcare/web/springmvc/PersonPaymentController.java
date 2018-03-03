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
import java.util.Calendar;
import java.util.Date;
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
import com.glaf.base.modules.sys.model.SysUser;
import com.glaf.base.modules.sys.service.SysUserService;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.StringTools;
import com.glaf.core.util.Tools;
import com.glaf.heathcare.SysConfig;
import com.glaf.heathcare.domain.GradeInfo;
import com.glaf.heathcare.domain.Person;
import com.glaf.heathcare.domain.PersonPayment;
import com.glaf.heathcare.query.PersonPaymentQuery;
import com.glaf.heathcare.service.GradeInfoService;
import com.glaf.heathcare.service.PersonPaymentService;
import com.glaf.heathcare.service.PersonService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/personPayment")
@RequestMapping("/heathcare/personPayment")
public class PersonPaymentController {
	protected static final Log logger = LogFactory.getLog(PersonPaymentController.class);

	protected PersonService personService;

	protected PersonPaymentService personPaymentService;

	protected GradeInfoService gradeInfoService;

	protected SysUserService sysUserService;

	public PersonPaymentController() {

	}

	@ResponseBody
	@RequestMapping("/audit")
	public byte[] audit(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isSystemAdministrator() || loginContext.getRoles().contains("TenantAdmin")
				|| loginContext.getRoles().contains("HealthPhysician")) {
			String id = RequestUtils.getString(request, "id");
			String ids = request.getParameter("ids");
			PersonPayment personPayment = null;
			try {
				if (id != null) {
					personPayment = personPaymentService.getPersonPayment(id);
				}
				if (personPayment != null) {
					if (!StringUtils.equals(loginContext.getTenantId(), personPayment.getTenantId())) {
						return ResponseUtils.responseJsonResult(false, "数据只能所属机构修改。");
					}
					personPayment.setBusinessStatus(9);
					personPayment.setConfirmBy(loginContext.getActorId());
					personPayment.setConfirmTime(new Date());
					this.personPaymentService.updatePersonPaymentStatus(personPayment);
					return ResponseUtils.responseJsonResult(true);
				}
				if (StringUtils.isNotEmpty(ids)) {
					List<String> pIds = StringTools.split(ids);
					PersonPaymentQuery query = new PersonPaymentQuery();
					query.tenantId(loginContext.getTenantId());
					query.setIds(pIds);
					List<PersonPayment> list = personPaymentService.list(query);
					if (list != null && !list.isEmpty()) {
						for (PersonPayment p : list) {
							if (!StringUtils.equals(loginContext.getTenantId(), p.getTenantId())) {
								return ResponseUtils.responseJsonResult(false, "数据只能所属机构修改。");
							}
							p.setBusinessStatus(9);
							p.setConfirmBy(loginContext.getActorId());
							p.setConfirmTime(new Date());
						}
						personPaymentService.updatePersonPaymentStatus(list);
						return ResponseUtils.responseJsonResult(true);
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseJsonResult(false);
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
					PersonPayment personPayment = personPaymentService.getPersonPayment(String.valueOf(x));
					if (personPayment != null
							&& ((StringUtils.equals(personPayment.getTenantId(), loginContext.getTenantId())
									&& (loginContext.getRoles().contains("TenantAdmin")
											|| loginContext.getRoles().contains("HealthPhysician")
											|| loginContext.getRoles().contains("Teacher"))))) {
						if (personPayment.getBusinessStatus() == 0) {
							personPaymentService.deleteById(personPayment.getId());
						} else if (personPayment.getBusinessStatus() == 9) {
							return ResponseUtils.responseJsonResult(false, "不能删除审核通过的数据！");
						}
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			PersonPayment personPayment = personPaymentService.getPersonPayment(String.valueOf(id));
			if (personPayment != null && ((StringUtils.equals(personPayment.getTenantId(), loginContext.getTenantId())
					&& (loginContext.getRoles().contains("TenantAdmin")
							|| loginContext.getRoles().contains("HealthPhysician")
							|| loginContext.getRoles().contains("Teacher"))))) {
				if (personPayment.getBusinessStatus() == 0) {
					personPaymentService.deleteById(personPayment.getId());
					return ResponseUtils.responseResult(true);
				} else if (personPayment.getBusinessStatus() == 9) {
					return ResponseUtils.responseJsonResult(false, "不能删除审核通过的数据！");
				}
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String personId = request.getParameter("personId");

		PersonPayment personPayment = personPaymentService.getPersonPayment(request.getParameter("id"));
		if (personPayment != null) {
			personId = personPayment.getPersonId();
			request.setAttribute("personPayment", personPayment);
		}

		if (StringUtils.isNotEmpty(personId)) {
			Person person = personService.getPerson(personId);
			if (person != null) {
				request.setAttribute("person", person);
			}
		} else {
			String gradeId = request.getParameter("gradeId");
			if (gradeId != null && gradeId.trim().length() > 0) {
				List<Person> persons = personService.getPersons(gradeId);
				request.setAttribute("persons", persons);
			}
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("personPayment.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/personPayment/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		PersonPaymentQuery query = new PersonPaymentQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		if (!loginContext.isSystemAdministrator()) {
			String actorId = loginContext.getActorId();
			query.createBy(actorId);
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
		int total = personPaymentService.getPersonPaymentCountByQueryCriteria(query);
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

			List<PersonPayment> list = personPaymentService.getPersonPaymentsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				List<Person> persons = personService.getTenantPersons(loginContext.getTenantId());
				Map<String, String> nameMap = new HashMap<String, String>();
				for (Person person : persons) {
					nameMap.put(person.getId(), person.getName());
				}

				List<GradeInfo> grades = gradeInfoService.getGradeInfosByTenantId(loginContext.getTenantId());
				Map<String, String> gradeMap = new HashMap<String, String>();
				for (GradeInfo grade : grades) {
					gradeMap.put(grade.getId(), grade.getName());
				}

				List<SysUser> users = sysUserService.getSysUserListByTenantId(loginContext.getTenantId());
				Map<String, String> userMap = new HashMap<String, String>();
				for (SysUser user : users) {
					userMap.put(user.getUserId(), user.getName());
				}

				for (PersonPayment personPayment : list) {
					JSONObject rowJSON = personPayment.toJsonObject();
					rowJSON.put("id", personPayment.getId());
					rowJSON.put("rowId", personPayment.getId());
					rowJSON.put("personPaymentId", personPayment.getId());
					rowJSON.put("person", nameMap.get(personPayment.getPersonId()));
					rowJSON.put("grade", gradeMap.get(personPayment.getGradeId()));
					rowJSON.put("createByName", userMap.get(personPayment.getCreateBy()));
					rowJSON.put("confirmByName", userMap.get(personPayment.getConfirmBy()));
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

		return new ModelAndView("/heathcare/personPayment/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("personPayment.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/heathcare/personPayment/query", modelMap);
	}

	@ResponseBody
	@RequestMapping("/savePersonPayment")
	public byte[] savePersonPayment(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		logger.debug("params:" + RequestUtils.getParameterMap(request));
		String actorId = loginContext.getActorId();
		String id = request.getParameter("id");
		String personId = request.getParameter("personId");
		Person person = null;
		PersonPayment personPayment = null;
		try {

			person = personService.getPerson(personId);
			String gradeId = person.getGradeId();

			if (StringUtils.isNotEmpty(id)) {
				personPayment = personPaymentService.getPersonPayment(id);
			}

			if (personPayment == null) {
				if (!loginContext.isTenantAdmin()) {
					if (!loginContext.getGradeIds().contains(gradeId)) {
						return ResponseUtils.responseJsonResult(false, "您没有该数据操作权限。");
					}
				}
				personPayment = new PersonPayment();
				personPayment.setTenantId(loginContext.getTenantId());
				personPayment.setPersonId(person.getId());
				personPayment.setGradeId(person.getGradeId());
			} else {
				if (loginContext.isTenantAdmin()) {
					if (!StringUtils.equals(loginContext.getTenantId(), personPayment.getTenantId())) {
						return ResponseUtils.responseJsonResult(false, "您没有该数据操作权限。");
					}
				} else {
					if (!loginContext.getGradeIds().contains(gradeId)) {
						return ResponseUtils.responseJsonResult(false, "您没有该数据操作权限。");
					}
				}
			}

			personPayment.setType(request.getParameter("type"));
			personPayment.setMoney(RequestUtils.getDouble(request, "money"));
			personPayment.setPayTime(RequestUtils.getDate(request, "payTime"));
			personPayment.setSemester(SysConfig.getSemester());
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(personPayment.getPayTime());
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH) + 1;

			personPayment.setYear(year);
			personPayment.setMonth(month);
			personPayment.setRemark(request.getParameter("remark"));
			personPayment.setBusinessStatus(0);
			personPayment.setCreateBy(actorId);
			personPayment.setUpdateBy(actorId);

			this.personPaymentService.save(personPayment);

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

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.personPaymentService")
	public void setPersonPaymentService(PersonPaymentService personPaymentService) {
		this.personPaymentService = personPaymentService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.personService")
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	@javax.annotation.Resource
	public void setSysUserService(SysUserService sysUserService) {
		this.sysUserService = sysUserService;
	}

}
