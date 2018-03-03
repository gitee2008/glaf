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
import com.glaf.base.modules.sys.model.Dictory;
import com.glaf.base.modules.sys.model.SysTenant;
import com.glaf.base.modules.sys.query.SysTenantQuery;
import com.glaf.base.modules.sys.service.DictoryService;
import com.glaf.base.modules.sys.service.SysTenantService;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;

import com.glaf.heathcare.domain.PersonInfo;
import com.glaf.heathcare.helper.PersonInfoHelper;
import com.glaf.heathcare.query.PersonInfoQuery;
import com.glaf.heathcare.service.PersonInfoService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/personInfo")
@RequestMapping("/heathcare/personInfo")
public class PersonInfoController {
	protected static final Log logger = LogFactory.getLog(PersonInfoController.class);

	protected DictoryService dictoryService;

	protected PersonInfoService personInfoService;

	protected SysTenantService sysTenantService;

	public PersonInfoController() {

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
					PersonInfo personInfo = personInfoService.getPersonInfo(String.valueOf(x));
					if (personInfo != null && ((StringUtils.equals(personInfo.getTenantId(), loginContext.getTenantId())
							&& loginContext.getRoles().contains("TenantAdmin")))) {
						personInfoService.deleteById(personInfo.getId());
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			PersonInfo personInfo = personInfoService.getPersonInfo(String.valueOf(id));
			if (personInfo != null && ((StringUtils.equals(personInfo.getTenantId(), loginContext.getTenantId())
					&& loginContext.getRoles().contains("TenantAdmin")))) {
				personInfoService.deleteById(personInfo.getId());
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		PersonInfo personInfo = personInfoService.getPersonInfo(request.getParameter("id"));
		if (personInfo != null) {
			request.setAttribute("personInfo", personInfo);
		}

		if (loginContext.isTenantAdmin()) {
			request.setAttribute("privilege_write", true);
		} else {
			request.removeAttribute("privilege_write");
		}

		List<Dictory> dictoryList = dictoryService.getDictoryList(5001L);// 5001是班级分类编号
		request.setAttribute("dictoryList", dictoryList);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("personInfo.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/personInfo/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		PersonInfoQuery query = new PersonInfoQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		if (!loginContext.isSystemAdministrator()) {
			query.tenantId(loginContext.getTenantId());
		} else {
			String tenantId = request.getParameter("tenantId");
			query.tenantId(tenantId);
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
		int total = personInfoService.getPersonInfoCountByQueryCriteria(query);
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

			List<PersonInfo> list = personInfoService.getPersonInfosByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				List<Dictory> dictoryList = dictoryService.getDictoryList(5001L);// 5001是班级分类编号
				Map<String, String> nameMap = new HashMap<String, String>();
				if (dictoryList != null && !dictoryList.isEmpty()) {
					for (Dictory dict : dictoryList) {
						nameMap.put(dict.getCode(), dict.getName());
					}
				}

				for (PersonInfo personInfo : list) {
					JSONObject rowJSON = personInfo.toJsonObject();
					rowJSON.put("id", personInfo.getId());
					rowJSON.put("rowId", personInfo.getId());
					rowJSON.put("personInfoId", personInfo.getId());
					rowJSON.put("startIndex", ++start);
					if (nameMap.get(personInfo.getClassType()) != null) {
						rowJSON.put("classTypeName", nameMap.get(personInfo.getClassType()));
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
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		PersonInfoHelper helper = new PersonInfoHelper();
		String tenantId = request.getParameter("tenantId");
		if (!loginContext.isSystemAdministrator()) {
			tenantId = loginContext.getTenantId();
		}

		if (loginContext.isSystemAdministrator()) {
			SysTenantQuery query = new SysTenantQuery();
			query.locked(0);
			List<SysTenant> tenants = sysTenantService.list(query);
			request.setAttribute("tenants", tenants);
			if (StringUtils.isEmpty(tenantId)) {
				if (tenants != null && !tenants.isEmpty()) {
					tenantId = tenants.get(0).getTenantId();
					request.setAttribute("tenantId", tenantId);
				}
			}
		}

		List<PersonInfo> persons = personInfoService.getPersonInfos(tenantId);
		if (persons != null && !persons.isEmpty()) {
			int totalMale = 0;
			int totalFemale = 0;
			for (PersonInfo info : persons) {
				int male = info.getMale();
				int female = info.getFemale();
				totalMale = totalMale + male;
				totalFemale = totalFemale + female;
			}
			request.setAttribute("totalMale", totalMale);
			request.setAttribute("totalFemale", totalFemale);
			request.setAttribute("totalPerson", (totalMale + totalFemale));
		}

		double totalQuantity = helper.getPersonFoodAgeQuantity(tenantId, 1.03);
		request.setAttribute("totalQuantity", Math.round(totalQuantity));

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/personInfo/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("personInfo.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/heathcare/personInfo/query", modelMap);
	}

	@ResponseBody
	@RequestMapping("/savePersonInfo")
	public byte[] savePersonInfo(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		String actorId = loginContext.getActorId();
		if (loginContext.isTenantAdmin()) {
			String id = request.getParameter("id");
			PersonInfo personInfo = null;
			try {
				if (StringUtils.isNotEmpty(id)) {
					personInfo = personInfoService.getPersonInfo(id);
				}
				if (personInfo == null) {
					personInfo = new PersonInfo();
					personInfo.setCreateBy(actorId);
					personInfo.setTenantId(loginContext.getTenantId());
					personInfo.setOrganizationId(loginContext.getOrganizationId());
				} else {
					if (!StringUtils.equals(loginContext.getTenantId(), personInfo.getTenantId())) {
						return ResponseUtils.responseJsonResult(false, "数据只能所属机构修改。");
					}
				}

				Tools.populate(personInfo, params);
				personInfo.setMale(RequestUtils.getInt(request, "male"));
				personInfo.setFemale(RequestUtils.getInt(request, "female"));
				personInfo.setAge(RequestUtils.getInt(request, "age"));
				personInfo.setClassType(request.getParameter("classType"));
				personInfo.setSortNo(RequestUtils.getInt(request, "sortNo"));

				this.personInfoService.save(personInfo);

				return ResponseUtils.responseJsonResult(true);
			} catch (Exception ex) {
				logger.error(ex);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource
	public void setDictoryService(DictoryService dictoryService) {
		this.dictoryService = dictoryService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.personInfoService")
	public void setPersonInfoService(PersonInfoService personInfoService) {
		this.personInfoService = personInfoService;
	}

	@javax.annotation.Resource
	public void setSysTenantService(SysTenantService sysTenantService) {
		this.sysTenantService = sysTenantService;
	}

	@RequestMapping("/view")
	public ModelAndView view(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		PersonInfo personInfo = personInfoService.getPersonInfo(request.getParameter("id"));
		request.setAttribute("personInfo", personInfo);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view);
		}

		String x_view = ViewProperties.getString("personInfo.view");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view);
		}

		return new ModelAndView("/heathcare/personInfo/view");
	}

}
