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
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;
import com.glaf.heathcare.domain.Person;
import com.glaf.heathcare.domain.PersonLinkman;
import com.glaf.heathcare.query.PersonLinkmanQuery;
import com.glaf.heathcare.service.PersonLinkmanService;
import com.glaf.heathcare.service.PersonService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/personLinkman")
@RequestMapping("/heathcare/personLinkman")
public class PersonLinkmanController {
	protected static final Log logger = LogFactory.getLog(PersonLinkmanController.class);

	protected PersonService personService;

	protected PersonLinkmanService personLinkmanService;

	public PersonLinkmanController() {

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
					PersonLinkman personLinkman = personLinkmanService.getPersonLinkman(x);
					if (personLinkman != null
							&& ((StringUtils.equals(personLinkman.getTenantId(), loginContext.getTenantId())
									&& (loginContext.getRoles().contains("TenantAdmin")
											|| loginContext.getRoles().contains("HealthPhysician")
											|| loginContext.getRoles().contains("Teacher"))))) {
						personLinkmanService.deleteById(personLinkman.getId());
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			PersonLinkman personLinkman = personLinkmanService.getPersonLinkman(id);
			if (personLinkman != null && ((StringUtils.equals(personLinkman.getTenantId(), loginContext.getTenantId())
					&& (loginContext.getRoles().contains("TenantAdmin")
							|| loginContext.getRoles().contains("HealthPhysician")
							|| loginContext.getRoles().contains("Teacher"))))) {
				personLinkmanService.deleteById(personLinkman.getId());
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		PersonLinkman personLinkman = personLinkmanService.getPersonLinkman(request.getParameter("id"));
		if (personLinkman != null) {
			request.setAttribute("personLinkman", personLinkman);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("personLinkman.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/personLinkman/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		PersonLinkmanQuery query = new PersonLinkmanQuery();
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
		int total = personLinkmanService.getPersonLinkmanCountByQueryCriteria(query);
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

			List<PersonLinkman> list = personLinkmanService.getPersonLinkmansByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (PersonLinkman personLinkman : list) {
					JSONObject rowJSON = personLinkman.toJsonObject();
					rowJSON.put("id", personLinkman.getId());
					rowJSON.put("rowId", personLinkman.getId());
					rowJSON.put("personLinkmanId", personLinkman.getId());
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

		return new ModelAndView("/heathcare/personLinkman/list", modelMap);
	}

	@ResponseBody
	@RequestMapping("/savePersonLinkman")
	public byte[] savePersonLinkman(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		PersonLinkman personLinkman = new PersonLinkman();
		String personId = request.getParameter("personId");
		Person person = null;
		try {
			if (StringUtils.isNotEmpty(personId)) {
				person = personService.getPerson(personId);
			}

			if (person != null && ((StringUtils.equals(person.getTenantId(), loginContext.getTenantId())
					&& (loginContext.getRoles().contains("TenantAdmin")
							|| loginContext.getRoles().contains("HealthPhysician")
							|| loginContext.getRoles().contains("Teacher"))))) {

				Tools.populate(personLinkman, params);
				personLinkman.setTenantId(loginContext.getTenantId());
				personLinkman.setPersonId(personId);
				personLinkman.setName(request.getParameter("name"));
				personLinkman.setRelationship(request.getParameter("relationship"));
				personLinkman.setCompany(request.getParameter("company"));
				personLinkman.setMobile(request.getParameter("mobile"));
				personLinkman.setMail(request.getParameter("mail"));
				personLinkman.setWardship(request.getParameter("wardship"));
				personLinkman.setRemark(request.getParameter("remark"));
				personLinkman.setCreateBy(actorId);
				personLinkman.setUpdateBy(actorId);

				this.personLinkmanService.save(personLinkman);

				return ResponseUtils.responseJsonResult(true);
			}

		} catch (Exception ex) {
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.personService")
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.personLinkmanService")
	public void setPersonLinkmanService(PersonLinkmanService personLinkmanService) {
		this.personLinkmanService = personLinkmanService;
	}

}
