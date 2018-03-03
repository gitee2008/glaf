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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.glaf.base.modules.sys.model.SysUser;
import com.glaf.base.modules.sys.query.SysTenantQuery;
import com.glaf.base.modules.sys.service.DictoryService;
import com.glaf.base.modules.sys.service.SysTenantService;
import com.glaf.base.modules.sys.service.SysUserService;

import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.LoginContext;
import com.glaf.core.tree.component.TreeComponent;
import com.glaf.core.tree.helper.TreeHelper;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;

import com.glaf.heathcare.domain.GradeInfo;
import com.glaf.heathcare.domain.Person;
import com.glaf.heathcare.helper.PermissionHelper;
import com.glaf.heathcare.query.GradeInfoQuery;
import com.glaf.heathcare.query.PersonQuery;
import com.glaf.heathcare.service.GradeInfoService;
import com.glaf.heathcare.service.PersonService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/gradeInfo")
@RequestMapping("/heathcare/gradeInfo")
public class GradeInfoController {
	protected static final Log logger = LogFactory.getLog(GradeInfoController.class);

	protected DictoryService dictoryService;

	protected GradeInfoService gradeInfoService;

	protected PersonService personService;

	protected SysTenantService sysTenantService;

	protected SysUserService sysUserService;

	public GradeInfoController() {

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
					GradeInfo gradeInfo = gradeInfoService.getGradeInfo(String.valueOf(x));
					if (gradeInfo != null && ((StringUtils.equals(gradeInfo.getTenantId(), loginContext.getTenantId())
							&& (loginContext.getRoles().contains("TenantAdmin")
									|| loginContext.getRoles().contains("HealthPhysician")
									|| loginContext.getRoles().contains("Teacher"))))) {
						if (personService.getGradePersonCount(gradeInfo.getId()) > 0) {
							return ResponseUtils.responseJsonResult(false, gradeInfo.getName() + "已经存在班级关联数据，不能删除。");
						}
						gradeInfo.setDeleteFlag(1);
						gradeInfoService.save(gradeInfo);
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			GradeInfo gradeInfo = gradeInfoService.getGradeInfo(String.valueOf(id));
			if (gradeInfo != null && ((StringUtils.equals(gradeInfo.getTenantId(), loginContext.getTenantId())
					&& (loginContext.getRoles().contains("TenantAdmin")
							|| loginContext.getRoles().contains("HealthPhysician")
							|| loginContext.getRoles().contains("Teacher"))))) {
				if (personService.getGradePersonCount(gradeInfo.getId()) > 0) {
					return ResponseUtils.responseJsonResult(false, gradeInfo.getName() + "已经存在班级关联数据，不能删除。");
				}
				gradeInfo.setDeleteFlag(1);
				gradeInfoService.save(gradeInfo);
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		GradeInfo gradeInfo = gradeInfoService.getGradeInfo(request.getParameter("id"));
		if (gradeInfo != null) {
			request.setAttribute("gradeInfo", gradeInfo);
			request.setAttribute("createGrade", false);
		} else {
			request.setAttribute("createGrade", true);
		}

		request.removeAttribute("gradeAdminPrivilege");

		if (loginContext.isTenantAdmin() || loginContext.isSystemAdministrator()) {
			request.setAttribute("gradeAdminPrivilege", true);
		}

		List<Dictory> dictoryList = dictoryService.getDictoryList(5001L);// 4501是班级分类编号
		request.setAttribute("dictoryList", dictoryList);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("gradeInfo.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/gradeInfo/edit", modelMap);
	}

	@RequestMapping("/gradeUsers")
	public ModelAndView gradeUsers(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		RequestUtils.setRequestParameterToAttribute(request);

		String id = request.getParameter("id");
		String privilege = request.getParameter("privilege");
		GradeInfo gradeInfo = null;
		if (id != null) {
			gradeInfo = gradeInfoService.getGradeInfo(id);
			request.setAttribute("gradeInfo", gradeInfo);
		}

		request.removeAttribute("gradeAdminPrivilege");

		if (loginContext.isTenantAdmin() || loginContext.isSystemAdministrator()) {
			request.setAttribute("gradeAdminPrivilege", true);
		}

		List<SysUser> allUsers = sysUserService.getSysUserListByTenantId(loginContext.getTenantId());
		List<String> userIds = gradeInfoService.getGradeUserIds(id, loginContext.getTenantId(), privilege);

		List<String> selecteds = new ArrayList<String>();

		if (userIds != null && !userIds.isEmpty()) {
			for (SysUser u : allUsers) {
				if (userIds.contains(u.getActorId())) {
					selecteds.add(u.getUserId());
				}
			}
		}

		StringBuilder bufferx = new StringBuilder();
		StringBuilder buffery = new StringBuilder();

		if (allUsers != null && allUsers.size() > 0) {
			for (int j = 0; j < allUsers.size(); j++) {
				SysUser r = (SysUser) allUsers.get(j);
				if (selecteds != null && selecteds.contains(r.getUserId())) {
					buffery.append("\n<option value=\"").append(r.getUserId()).append("\">").append(r.getName())
							.append(" [").append(r.getUserId()).append("]").append("</option>");
				} else {
					bufferx.append("\n<option value=\"").append(r.getUserId()).append("\">").append(r.getName())
							.append(" [").append(r.getUserId()).append("]").append("</option>");
				}
			}
		}

		request.setAttribute("bufferx", bufferx.toString());
		request.setAttribute("buffery", buffery.toString());

		String x_view = ViewProperties.getString("gradeInfo.gradeUsers");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/gradeInfo/gradeUsers", modelMap);
	}

	@RequestMapping("/grid")
	public ModelAndView grid(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		RequestUtils.setRequestParameterToAttribute(request);
		PermissionHelper helper = new PermissionHelper();
		helper.setPermission(request);

		request.removeAttribute("gradeAdminPrivilege");

		if (loginContext.isTenantAdmin() || loginContext.isSystemAdministrator()) {
			request.setAttribute("gradeAdminPrivilege", true);
		}

		if (loginContext.isSystemAdministrator()) {
			SysTenantQuery query = new SysTenantQuery();
			query.locked(0);
			List<SysTenant> tenants = sysTenantService.list(query);
			request.setAttribute("tenants", tenants);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/gradeInfo/grid", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		GradeInfoQuery query = new GradeInfoQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		if (!loginContext.isSystemAdministrator()) {
			logger.debug("perms:" + loginContext.getPermissions());
			query.tenantId(loginContext.getTenantId());
			if (!(loginContext.hasPermission("HealthPhysician", "or")
					|| loginContext.hasPermission("TenantAdmin", "or"))) {
				logger.debug("gradeIds:" + loginContext.getGradeIds());
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
		int total = gradeInfoService.getGradeInfoCountByQueryCriteria(query);
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

			List<GradeInfo> list = gradeInfoService.getGradeInfosByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				List<Dictory> dictoryList = dictoryService.getDictoryList(5001L);// 4501是班级分类编号
				Map<String, String> nameMap = new HashMap<String, String>();
				if (dictoryList != null && !dictoryList.isEmpty()) {
					for (Dictory dict : dictoryList) {
						nameMap.put(dict.getValue(), dict.getName());
					}
				}

				for (GradeInfo gradeInfo : list) {
					JSONObject rowJSON = gradeInfo.toJsonObject();
					rowJSON.put("id", gradeInfo.getId());
					rowJSON.put("rowId", gradeInfo.getId());
					rowJSON.put("gradeInfoId", gradeInfo.getId());
					rowJSON.put("startIndex", ++start);
					if (nameMap.get(String.valueOf(gradeInfo.getLevel())) != null) {
						rowJSON.put("classTypeName", nameMap.get(String.valueOf(gradeInfo.getLevel())));
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
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		RequestUtils.setRequestParameterToAttribute(request);
		PermissionHelper helper = new PermissionHelper();
		helper.setPermission(request);

		request.removeAttribute("gradeAdminPrivilege");

		if (loginContext.isTenantAdmin() || loginContext.isSystemAdministrator()) {
			request.setAttribute("gradeAdminPrivilege", true);
		}

		if (loginContext.isSystemAdministrator()) {
			SysTenantQuery query = new SysTenantQuery();
			query.locked(0);
			List<SysTenant> tenants = sysTenantService.list(query);
			request.setAttribute("tenants", tenants);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/gradeInfo/list", modelMap);
	}

	@RequestMapping("/list2")
	public ModelAndView list2(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		RequestUtils.setRequestParameterToAttribute(request);
		PermissionHelper helper = new PermissionHelper();
		helper.setPermission(request);

		request.removeAttribute("gradeAdminPrivilege");

		if (loginContext.isTenantAdmin() || loginContext.isSystemAdministrator()) {
			request.setAttribute("gradeAdminPrivilege", true);
		}

		if (loginContext.isSystemAdministrator()) {
			SysTenantQuery query = new SysTenantQuery();
			query.locked(0);
			List<SysTenant> tenants = sysTenantService.list(query);
			request.setAttribute("tenants", tenants);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/gradeInfo/list2", modelMap);
	}

	@RequestMapping("/list3")
	public ModelAndView list3(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		RequestUtils.setRequestParameterToAttribute(request);
		PermissionHelper helper = new PermissionHelper();
		helper.setPermission(request);

		request.removeAttribute("gradeAdminPrivilege");

		if (loginContext.isTenantAdmin() || loginContext.isSystemAdministrator()) {
			request.setAttribute("gradeAdminPrivilege", true);
		}

		if (loginContext.isSystemAdministrator()) {
			SysTenantQuery query = new SysTenantQuery();
			query.locked(0);
			List<SysTenant> tenants = sysTenantService.list(query);
			request.setAttribute("tenants", tenants);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/gradeInfo/list3", modelMap);
	}

	@RequestMapping("/list4")
	public ModelAndView list4(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		RequestUtils.setRequestParameterToAttribute(request);
		PermissionHelper helper = new PermissionHelper();
		helper.setPermission(request);

		request.removeAttribute("gradeAdminPrivilege");

		if (loginContext.isTenantAdmin() || loginContext.isSystemAdministrator()) {
			request.setAttribute("gradeAdminPrivilege", true);
		}

		if (loginContext.isSystemAdministrator()) {
			SysTenantQuery query = new SysTenantQuery();
			query.locked(0);
			List<SysTenant> tenants = sysTenantService.list(query);
			request.setAttribute("tenants", tenants);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/gradeInfo/list4", modelMap);
	}

	@RequestMapping("/list5")
	public ModelAndView list5(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		RequestUtils.setRequestParameterToAttribute(request);
		PermissionHelper helper = new PermissionHelper();
		helper.setPermission(request);

		request.removeAttribute("gradeAdminPrivilege");

		if (loginContext.isTenantAdmin() || loginContext.isSystemAdministrator()) {
			request.setAttribute("gradeAdminPrivilege", true);
		}

		if (loginContext.isSystemAdministrator()) {
			SysTenantQuery query = new SysTenantQuery();
			query.locked(0);
			List<SysTenant> tenants = sysTenantService.list(query);
			request.setAttribute("tenants", tenants);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/gradeInfo/list5", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("gradeInfo.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/heathcare/gradeInfo/query", modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveGradeInfo")
	public byte[] saveGradeInfo(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isTenantAdmin() || loginContext.isSystemAdministrator()) {
			String actorId = loginContext.getActorId();
			Map<String, Object> params = RequestUtils.getParameterMap(request);
			String id = request.getParameter("id");
			String createTeacherFlag = null;
			GradeInfo gradeInfo = null;
			try {
				if (StringUtils.isNotEmpty(id)) {
					gradeInfo = gradeInfoService.getGradeInfo(id);
				}
				if (gradeInfo == null) {
					gradeInfo = new GradeInfo();
					gradeInfo.setTenantId(loginContext.getTenantId());
					createTeacherFlag = request.getParameter("createTeacherFlag");
				} else {
					if (!StringUtils.equals(loginContext.getTenantId(), gradeInfo.getTenantId())) {
						return ResponseUtils.responseJsonResult(false, "数据只能所属机构修改。");
					}
				}

				Tools.populate(gradeInfo, params);

				gradeInfo.setName(request.getParameter("name"));
				gradeInfo.setCode(request.getParameter("code"));
				gradeInfo.setLevel(RequestUtils.getInt(request, "level"));
				gradeInfo.setPrincipal(request.getParameter("principal"));
				gradeInfo.setTelephone(request.getParameter("telephone"));
				gradeInfo.setType(request.getParameter("type"));
				gradeInfo.setYear(RequestUtils.getInt(request, "year"));
				gradeInfo.setRemark(request.getParameter("remark"));
				gradeInfo.setSortNo(RequestUtils.getInt(request, "sortNo"));
				gradeInfo.setLocked(RequestUtils.getInt(request, "locked"));
				gradeInfo.setOrganizationId(loginContext.getOrganizationId());
				gradeInfo.setCreateBy(actorId);
				this.gradeInfoService.save(gradeInfo);

				if (StringUtils.isNotEmpty(createTeacherFlag) && StringUtils.equals(createTeacherFlag, "Y")) {
					String tid = this.gradeInfoService.createTeacher(actorId, gradeInfo);
					return ResponseUtils.responseJsonResult(true, "班级创建成功，同时创建班级教师" + tid);
				}

				return ResponseUtils.responseJsonResult(true);
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@RequestMapping("saveGradeUsers")
	@ResponseBody
	public byte[] saveGradeUsers(HttpServletRequest request) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isTenantAdmin() || loginContext.isSystemAdministrator()) {
			String id = RequestUtils.getString(request, "id");
			String privilege = RequestUtils.getString(request, "privilege");
			if (id != null && privilege != null) {
				GradeInfo gradeInfo = gradeInfoService.getGradeInfo(id);
				if (gradeInfo != null) {
					if (loginContext.isTenantAdmin()
							&& !StringUtils.equals(loginContext.getTenantId(), gradeInfo.getTenantId())) {
						return ResponseUtils.responseJsonResult(false, "您没有该数据操作权限。");
					}
					String userIds = request.getParameter("objectIds");
					List<String> actorIds = new ArrayList<String>();
					String[] ids = userIds.split(",");
					for (int i = 0; i < ids.length; i++) {
						String userId = ids[i];
						actorIds.add(userId);
					}
					gradeInfoService.saveGradeUsers(id, loginContext.getTenantId(), privilege, actorIds);
					return ResponseUtils.responseJsonResult(true);
				}
			}
		}

		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource
	public void setDictoryService(DictoryService dictoryService) {
		this.dictoryService = dictoryService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.gradeInfoService")
	public void setGradeInfoService(GradeInfoService gradeInfoService) {
		this.gradeInfoService = gradeInfoService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.personService")
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	@javax.annotation.Resource
	public void setSysTenantService(SysTenantService sysTenantService) {
		this.sysTenantService = sysTenantService;
	}

	@javax.annotation.Resource
	public void setSysUserService(SysUserService sysUserService) {
		this.sysUserService = sysUserService;
	}

	@ResponseBody
	@RequestMapping("/treeJson")
	public byte[] treeJson(HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.debug("------------------------treeJson--------------------");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		List<TreeComponent> treeModels = new ArrayList<TreeComponent>();
		JSONArray result = new JSONArray();
		try {
			LoginContext loginContext = RequestUtils.getLoginContext(request);
			Map<String, Object> params = RequestUtils.getParameterMap(request);
			GradeInfoQuery query = new GradeInfoQuery();
			Tools.populate(query, params);
			query.deleteFlag(0);
			query.setActorId(loginContext.getActorId());
			query.setLoginContext(loginContext);

			if (!loginContext.isSystemAdministrator()) {
				logger.debug("perms:" + loginContext.getPermissions());
				query.tenantId(loginContext.getTenantId());
				if (!(loginContext.hasPermission("HealthPhysician", "or")
						|| loginContext.hasPermission("TenantAdmin", "or"))) {
					logger.debug("gradeIds:" + loginContext.getGradeIds());
					query.gradeIds(loginContext.getGradeIds());
				}
			}

			PersonQuery q = new PersonQuery();
			q.tenantId(loginContext.getTenantId());

			List<Person> persons = personService.list(q);

			List<GradeInfo> grades = gradeInfoService.list(query);

			for (GradeInfo grade : grades) {
				if (grade.getLocked() == 1) {
					continue;
				}
				Map<String, Object> dataMap = new HashMap<String, Object>();
				dataMap.put("type", "grade");
				TreeComponent comp = new TreeComponent();
				comp.setId(grade.getId());
				comp.setTitle(grade.getName());
				comp.setDataMap(dataMap);
				comp.setImage(request.getContextPath() + "/static/images/grade.gif");
				treeModels.add(comp);
				for (Person person : persons) {
					if (StringUtils.equals(person.getGradeId(), grade.getId())) {
						Map<String, Object> dataMap2 = new HashMap<String, Object>();
						dataMap2.put("type", "person");
						TreeComponent child = new TreeComponent();
						child.setId(person.getId());
						child.setParentId(grade.getId());
						child.setTitle(person.getName());
						child.setDataMap(dataMap2);
						if (StringUtils.equals(person.getSex(), "1")) {
							child.setImage(request.getContextPath() + "/static/images/user.png");
						} else {
							child.setImage(request.getContextPath() + "/static/images/user_female.png");
						}
						treeModels.add(child);
					}
				}
			}

			TreeHelper treeHelper = new TreeHelper();
			result = treeHelper.getJSONArray(treeModels, 0);

			return result.toString().getBytes("UTF-8");
		} catch (IOException e) {
			return result.toString().getBytes();
		}
	}

	@ResponseBody
	@RequestMapping("/treeJson2")
	public byte[] treeJson2(HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.debug("------------------------treeJson2--------------------");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		List<TreeComponent> treeModels = new ArrayList<TreeComponent>();
		JSONArray result = new JSONArray();
		try {
			Map<String, Object> params = RequestUtils.getParameterMap(request);
			GradeInfoQuery query = new GradeInfoQuery();
			Tools.populate(query, params);

			String tenantId = request.getParameter("tenantId");
			if (tenantId != null && loginContext.getRoles().contains("HygieneOrg")
					&& loginContext.getManagedTenantIds().contains(tenantId)) {
				query.tenantId(tenantId);
			} else {
				return result.toJSONString().getBytes();
			}

			PersonQuery q = new PersonQuery();
			q.tenantId(tenantId);

			List<Person> persons = personService.list(q);

			List<GradeInfo> grades = gradeInfoService.list(query);

			for (GradeInfo grade : grades) {
				if (grade.getLocked() == 1) {
					continue;
				}
				Map<String, Object> dataMap = new HashMap<String, Object>();
				dataMap.put("type", "grade");
				TreeComponent comp = new TreeComponent();
				comp.setId(grade.getId());
				comp.setTitle(grade.getName());
				comp.setDataMap(dataMap);
				comp.setImage(request.getContextPath() + "/static/images/grade.gif");
				treeModels.add(comp);
				for (Person person : persons) {
					if (StringUtils.equals(person.getGradeId(), grade.getId())) {
						Map<String, Object> dataMap2 = new HashMap<String, Object>();
						dataMap2.put("type", "person");
						TreeComponent child = new TreeComponent();
						child.setId(person.getId());
						child.setParentId(grade.getId());
						child.setTitle(person.getName());
						child.setDataMap(dataMap2);
						if (StringUtils.equals(person.getSex(), "1")) {
							child.setImage(request.getContextPath() + "/static/images/user.png");
						} else {
							child.setImage(request.getContextPath() + "/static/images/user_female.png");
						}
						treeModels.add(child);
					}
				}
			}

			TreeHelper treeHelper = new TreeHelper();
			result = treeHelper.getJSONArray(treeModels, 0);

			return result.toString().getBytes("UTF-8");
		} catch (IOException e) {
			return result.toString().getBytes();
		}
	}

	@RequestMapping("/view")
	public ModelAndView view(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		GradeInfo gradeInfo = gradeInfoService.getGradeInfo(request.getParameter("id"));
		request.setAttribute("gradeInfo", gradeInfo);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view);
		}

		String x_view = ViewProperties.getString("gradeInfo.view");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view);
		}

		return new ModelAndView("/heathcare/gradeInfo/view");
	}

}
