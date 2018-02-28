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

package com.glaf.base.web.springmvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import com.glaf.base.modules.sys.model.SysRole;
import com.glaf.base.modules.sys.model.SysTenant;
import com.glaf.base.modules.sys.model.SysUser;
import com.glaf.base.modules.sys.query.SysUserQuery;
import com.glaf.base.modules.sys.service.SysRoleService;
import com.glaf.base.modules.sys.service.SysTenantService;
import com.glaf.base.modules.sys.service.SysUserService;
import com.glaf.base.utils.ParamUtil;
import com.glaf.base.utils.RequestUtil;

import com.glaf.core.cache.CacheUtils;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.service.ITableDataService;
import com.glaf.core.util.PageResult;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;

@Controller("/user")
@RequestMapping("/user")
public class UserController {
	protected static final Log logger = LogFactory.getLog(UserController.class);

	protected SysRoleService sysRoleService;

	protected SysUserService sysUserService;

	protected ITableDataService tableDataService;

	protected SysTenantService sysTenantService;

	@RequestMapping("json")
	@ResponseBody
	public byte[] json(HttpServletRequest request) throws IOException {
		String nameOrCode = RequestUtils.getString(request, "nameOrCode");
		String type = RequestUtils.getString(request, "type", "");
		// String subType = RequestUtils.getString(request, "subType","");
		String roleId = RequestUtils.getString(request, "roleId", "");
		long organizationId = RequestUtils.getLong(request, "organizationId", 0);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SysUserQuery query = new SysUserQuery();
		Tools.populate(query, params);
		if (StringUtils.isNotEmpty(nameOrCode)) {
			query.setOrganizationNameOrCode("1");
			query.setOrganizationNameLike(nameOrCode);
			query.setOrganizationCodeLike(nameOrCode);
		}
		// type判断增加过滤sql
		if ("roleAddUser".equals(type)) {// 全局角色或部门角色添加用户
			query.setSysOrganizationId(organizationId);
			if (organizationId != 0) {// 部门角色配置用户时只能选择本部门的用户
				query.setOrganizationId(organizationId);
			}
			if (StringUtils.isNotEmpty(roleId)) {
				query.setRoleId(roleId);
			}
		} else if ("groupUserAddUser".equals(type)) {// 群组用户添加用户
			query.setGroupUserId(roleId);
		} else if ("groupLeaderAddUser".equals(type)) {// 群组领导添加用户
			query.setGroupLeaderId(roleId);
		} else if ("addReturn".equals(type)) {// 群组或角色添加管理用户
			String accountsNotIn = RequestUtils.getString(request, "userIdsNotIn", "");
			if (StringUtils.isNotEmpty(accountsNotIn)) {
				List<String> list = new ArrayList<String>();
				String[] accountArray = accountsNotIn.split(",");
				for (String x : accountArray) {
					if (StringUtils.isNotEmpty(x))
						list.add(x);
				}
				query.setUserIdsNotIn(list);
			}
		}
		query.setLocked(0);// 有效用户

		int start = 0;
		int limit = 10;
		String orderName = null;
		String order = null;

		int pageNo = ParamUtils.getInt(params, "page");
		limit = ParamUtils.getInt(params, "rows");
		start = (pageNo - 1) * limit;
		orderName = ParamUtil.getParameter(request, "sort", "");
		order = ParamUtil.getParameter(request, "order", "");

		if (start < 0) {
			start = 0;
		}

		if (limit <= 0) {
			limit = PageResult.DEFAULT_PAGE_SIZE;
		}

		JSONObject result = new JSONObject();
		int total = sysUserService.getSysUserCountByQueryCriteria(query);
		if (total > 0) {
			result.put("total", total);
			result.put("totalCount", total);
			result.put("totalRecords", total);
			result.put("start", start);
			result.put("startIndex", start);
			result.put("limit", limit);
			result.put("pageSize", limit);

			if (StringUtils.isNotEmpty(orderName)) {
				query.setOrderBy(orderName + " " + order);
			}

			List<SysUser> list = sysUserService.getSysUsersByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (SysUser sysUser : list) {
					JSONObject rowJSON = sysUser.toJsonObject();
					rowJSON.put("id", sysUser.getId());
					rowJSON.put("actorId", sysUser.getUserId());
					rowJSON.put("startIndex", ++start);
					rowsJSON.add(rowJSON);
				}

			}
		} else {
			result.put("total", total);
			result.put("totalCount", total);
			JSONArray rowsJSON = new JSONArray();
			result.put("rows", rowsJSON);
		}
		return result.toString().getBytes("UTF-8");
	}

	/**
	 * 显示修改页面
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/prepareModifyInfo")
	public ModelAndView prepareModifyInfo(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		SysUser user = RequestUtil.getLoginUser(request);
		SysUser bean = sysUserService.findByAccount(user.getUserId());
		request.setAttribute("user", bean);

		String x_view = ViewProperties.getString("identity.user.prepareModifyInfo");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/user/user_change_info", modelMap);
	}

	/**
	 * 显示修改页面
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/prepareModifyPwd")
	public ModelAndView prepareModifyPwd(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		SysUser bean = RequestUtil.getLoginUser(request);
		request.setAttribute("bean", bean);

		String x_view = ViewProperties.getString("identity.user.prepareModifyPwd");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/user/user_modify_pwd", modelMap);
	}

	/**
	 * 提交修改信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveModify")
	@ResponseBody
	public byte[] saveModify(HttpServletRequest request) {
		String id = ParamUtil.getParameter(request, "id");
		try {
			SysUser bean = sysUserService.findById(id);
			boolean ret = false;
			if (bean != null) {
				bean.setName(ParamUtil.getParameter(request, "name"));
				bean.setSex(ParamUtil.getIntParameter(request, "sex", 0));
				bean.setMobile(ParamUtil.getParameter(request, "mobile"));
				bean.setEmail(ParamUtil.getParameter(request, "email"));
				bean.setTelephone(ParamUtil.getParameter(request, "telephone"));
				bean.setEvection(ParamUtil.getIntParameter(request, "evection", 0));
				bean.setLocked(ParamUtil.getIntParameter(request, "locked", 0));
				bean.setHeadship(ParamUtil.getParameter(request, "headship"));
				bean.setUserType(ParamUtil.getIntParameter(request, "userType", 0));
				bean.setUpdateBy(RequestUtils.getActorId(request));
				ret = sysUserService.update(bean);
				return ResponseUtils.responseResult(ret);
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
		return ResponseUtils.responseResult(false);
	}

	/**
	 * 提交修改信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveModifyInfo")
	@ResponseBody
	public byte[] saveModifyInfo(HttpServletRequest request) {
		SysUser bean = RequestUtil.getLoginUser(request);
		boolean ret = false;
		if (bean != null) {
			try {
				SysUser user = sysUserService.findById(bean.getActorId());
				user.setMobile(ParamUtil.getParameter(request, "mobile"));
				user.setEmail(ParamUtil.getParameter(request, "email"));
				user.setTelephone(ParamUtil.getParameter(request, "telephone"));
				user.setUpdateBy(RequestUtils.getActorId(request));
				ret = sysUserService.update(user);
				CacheUtils.clearUserCache(user.getUserId());
				return ResponseUtils.responseResult(ret);
			} catch (Exception ex) {
				logger.error(ex);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	/**
	 * 修改用户密码
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/savePwd")
	@ResponseBody
	public byte[] savePwd(HttpServletRequest request) {
		SysUser bean = RequestUtil.getLoginUser(request);
		String oldPwd = ParamUtil.getParameter(request, "oldPwd");
		String newPwd = ParamUtil.getParameter(request, "newPwd");
		if (bean != null && StringUtils.isNotEmpty(oldPwd) && StringUtils.isNotEmpty(newPwd)) {
			try {
				SysUser user = sysUserService.findById(bean.getActorId());
				if (sysUserService.checkPassword(user.getUserId(), oldPwd)) {
					sysUserService.changePassword(user.getUserId(), newPwd);
					return ResponseUtils.responseResult(true);
				}
				return ResponseUtils.responseJsonResult(false, "原始密码不正确。");
			} catch (Exception ex) {
				logger.error(ex);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	/**
	 * 修改用户密码
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/savePwd2")
	@ResponseBody
	public byte[] savePwd2(HttpServletRequest request) {
		SysUser bean = RequestUtil.getLoginUser(request);
		String newPwd = ParamUtil.getParameter(request, "newPwd");
		if (bean != null && StringUtils.isNotEmpty(newPwd)) {
			try {
				SysUser user = sysUserService.findById(bean.getActorId());
				if (user != null) {
					sysUserService.changePassword(user.getUserId(), newPwd);
					return ResponseUtils.responseResult(true);
				}
			} catch (Exception ex) {
				logger.error(ex);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@javax.annotation.Resource
	public void setSysRoleService(SysRoleService sysRoleService) {
		this.sysRoleService = sysRoleService;
	}

	@javax.annotation.Resource
	public void setSysTenantService(SysTenantService sysTenantService) {
		this.sysTenantService = sysTenantService;
	}

	@javax.annotation.Resource
	public void setSysUserService(SysUserService sysUserService) {
		this.sysUserService = sysUserService;
	}

	@javax.annotation.Resource
	public void setTableDataService(ITableDataService tableDataService) {
		this.tableDataService = tableDataService;
	}

	@RequestMapping("/showUser")
	public ModelAndView showUser(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String userId = ParamUtil.getParameter(request, "actorId");
		userId = RequestUtils.decodeString(userId);
		SysUser user = sysUserService.findById(userId);
		if (user != null && user.getTenantId() != null) {
			SysTenant tenant = sysTenantService.getSysTenantByTenantId(user.getTenantId());
			request.setAttribute("tenant", tenant);
		}
		request.setAttribute("user", user);

		String x_view = ViewProperties.getString("user.view");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/user/user_view", modelMap);
	}

	/**
	 * 显示修改页面
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/view")
	public ModelAndView view(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String actorId = ParamUtil.getParameter(request, "actorId");
		actorId = RequestUtils.decodeString(actorId);
		SysUser bean = sysUserService.findByAccount(actorId);

		if (bean != null && bean.getTenantId() != null) {
			SysTenant tenant = sysTenantService.getSysTenantByTenantId(bean.getTenantId());
			request.setAttribute("tenant", tenant);
		}

		request.setAttribute("bean", bean);
		request.setAttribute("user", bean);

		String x_view = ViewProperties.getString("user.view");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/user/user_view", modelMap);
	}

	@RequestMapping("/viewRoleUsers")
	public ModelAndView viewRoleUsers(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String id = ParamUtil.getParameter(request, "id");
		SysRole sysRole = null;
		if (id != null) {
			sysRole = sysRoleService.findById(id);
			request.setAttribute("sysRole", sysRole);
		}

		if (sysRole == null) {
			id = ParamUtil.getParameter(request, "roleId");
			sysRole = sysRoleService.findById(id);
			request.setAttribute("sysRole", sysRole);
		}

		String x_view = ViewProperties.getString("role.viewRoleUsers");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/user/viewRoleUsers", modelMap);
	}

}