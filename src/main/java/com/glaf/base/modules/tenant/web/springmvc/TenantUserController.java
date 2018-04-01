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

package com.glaf.base.modules.tenant.web.springmvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import com.glaf.base.modules.sys.SysConstants;
import com.glaf.base.modules.sys.business.UpdateTreeBean;
import com.glaf.base.modules.sys.model.Dictory;
import com.glaf.base.modules.sys.model.SysOrganization;
import com.glaf.base.modules.sys.model.SysRole;
import com.glaf.base.modules.sys.model.SysTenant;
import com.glaf.base.modules.sys.model.SysUser;
import com.glaf.base.modules.sys.query.SysUserQuery;
import com.glaf.base.modules.sys.service.DictoryService;
import com.glaf.base.modules.sys.service.SysOrganizationService;
import com.glaf.base.modules.sys.service.SysRoleService;
import com.glaf.base.modules.sys.service.SysTenantService;
import com.glaf.base.modules.sys.service.SysUserService;
import com.glaf.base.utils.ParamUtil;
import com.glaf.base.utils.RequestUtil;
import com.glaf.core.base.TreeModel;
import com.glaf.core.cache.CacheUtils;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.LoginContext;
import com.glaf.core.service.EntityService;
import com.glaf.core.service.ITableDataService;
import com.glaf.core.util.PageResult;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.StringTools;
import com.glaf.core.util.Tools;

@Controller("/tenant/user")
@RequestMapping("/tenant/user")
public class TenantUserController {
	private static final Log logger = LogFactory.getLog(TenantUserController.class);

	protected DictoryService dictoryService;

	protected EntityService entityService;

	protected SysRoleService sysRoleService;

	protected SysUserService sysUserService;

	protected ITableDataService tableDataService;

	protected SysOrganizationService sysOrganizationService;

	protected SysTenantService sysTenantService;

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		long organizationId = RequestUtils.getLong(request, "organizationId");

		Map<String, Object> params = RequestUtils.getParameterMap(request);

		SysUserQuery query = new SysUserQuery();
		Tools.populate(query, params);
		query.setDeleteFlag(0);
		query.setUserType(8);

		if (loginContext.isSystemAdministrator()) {
			query.tenantId(request.getParameter("tenantId"));
		} else {
			query.tenantId(loginContext.getTenantId());
		}

		if (organizationId > 0) {
			query.organizationId(organizationId);
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
			limit = PageResult.DEFAULT_PAGE_SIZE;
		}

		JSONObject result = new JSONObject();
		int total = sysUserService.getSysUserCountByQueryCriteria(query);
		if (loginContext.isTenantAdmin()) {
			total = total - 1;
		}

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

			List<SysUser> list = sysUserService.getSysUsersByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				for (SysUser sysUser : list) {
					if (loginContext.isTenantAdmin()
							&& StringUtils.equals(sysUser.getActorId(), loginContext.getActorId())) {
						continue;
					}
					JSONObject rowJSON = sysUser.toJsonObject();
					rowJSON.put("id", sysUser.getId());
					rowJSON.put("userId", sysUser.getUserId());
					rowJSON.put("startIndex", ++start);
					rowJSON.remove("token");
					rowsJSON.add(rowJSON);
				}

				result.put("rows", rowsJSON);

			}
		} else {
			result.put("total", total);
			result.put("totalCount", total);
			JSONArray rowsJSON = new JSONArray();
			result.put("rows", rowsJSON);
		}
		return result.toString().getBytes("UTF-8");
	}

	@RequestMapping
	public ModelAndView list(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String x_view = ViewProperties.getString("tenant.user.list");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/tenant/user/list", modelMap);
	}

	/**
	 * 显示增加页面
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/prepareAdd")
	public ModelAndView prepareAdd(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String tenantId = request.getParameter("tenantId");
		if (!loginContext.isSystemAdministrator()) {
			tenantId = loginContext.getTenantId();
		}
		List<SysOrganization> list = sysOrganizationService.getSysOrganizationList(tenantId);
		if (list != null && !list.isEmpty()) {
			Map<Long, TreeModel> treeMap = new HashMap<Long, TreeModel>();
			List<TreeModel> treeModels = new ArrayList<TreeModel>();
			for (SysOrganization model2 : list) {
				treeModels.add(model2);
				treeMap.put(model2.getId(), model2);
			}
			StringTokenizer token = null;
			StringBuilder blank = new StringBuilder();
			UpdateTreeBean bean = new UpdateTreeBean();
			List<SysOrganization> modelList = new ArrayList<SysOrganization>();
			for (SysOrganization model2 : list) {
				String treeId = bean.getTreeId(treeMap, model2);
				if (treeId != null && treeId.indexOf("|") != -1) {
					token = new StringTokenizer(treeId, "|");
					if (token != null) {
						model2.setLevel(token.countTokens());
						blank.delete(0, blank.length());
						for (int i = 0; i < model2.getLevel(); i++) {
							blank.append("&nbsp;&nbsp;&nbsp;&nbsp;");
						}
						model2.setBlank(blank.toString());
					}
				}
				modelList.add(model2);
			}
			request.setAttribute("trees", modelList);
		}

		List<Dictory> dictories = dictoryService.getDictoryList(SysConstants.USER_HEADSHIP);
		modelMap.put("dictories", dictories);

		List<Dictory> accounts = dictoryService.getDictoryList(SysConstants.USER_ACCOUNTTYPE);
		modelMap.put("accountTypeDictories", accounts);

		String x_view = ViewProperties.getString("tenant.user.prepareAdd");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/tenant/user/user_add", modelMap);
	}

	/**
	 * 显示修改页面
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/prepareModify")
	public ModelAndView prepareModify(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String tenantId = request.getParameter("tenantId");
		if (!loginContext.isSystemAdministrator()) {
			tenantId = loginContext.getTenantId();
		}

		String userId = ParamUtil.getParameter(request, "userId");
		userId = RequestUtils.decodeString(userId);
		SysUser user = sysUserService.findById(userId);
		if (user != null) {
			tenantId = user.getTenantId();
			List<SysOrganization> list = sysOrganizationService.getSysOrganizationList(tenantId);
			if (list != null && !list.isEmpty()) {
				Map<Long, TreeModel> treeMap = new HashMap<Long, TreeModel>();
				List<TreeModel> treeModels = new ArrayList<TreeModel>();
				for (SysOrganization model2 : list) {
					treeModels.add(model2);
					treeMap.put(model2.getId(), model2);
				}
				StringTokenizer token = null;
				StringBuilder blank = new StringBuilder();
				UpdateTreeBean bean = new UpdateTreeBean();
				List<SysOrganization> modelList = new ArrayList<SysOrganization>();
				for (SysOrganization model2 : list) {
					String treeId = bean.getTreeId(treeMap, model2);
					if (treeId != null && treeId.indexOf("|") != -1) {
						token = new StringTokenizer(treeId, "|");
						if (token != null) {
							model2.setLevel(token.countTokens());
							blank.delete(0, blank.length());
							for (int i = 0; i < model2.getLevel(); i++) {
								blank.append("&nbsp;&nbsp;&nbsp;&nbsp;");
							}
							model2.setBlank(blank.toString());
						}
					}
					modelList.add(model2);
				}
				request.setAttribute("trees", modelList);
			}

			request.setAttribute("user", user);
			request.setAttribute("userId_encode", RequestUtils.encodeString(user.getActorId()));
		}

		List<Dictory> dictories = dictoryService.getDictoryList(SysConstants.USER_HEADSHIP);
		modelMap.put("dictories", dictories);

		List<Dictory> accounts = dictoryService.getDictoryList(SysConstants.USER_ACCOUNTTYPE);
		modelMap.put("accountTypeDictories", accounts);

		String x_view = ViewProperties.getString("tenant.user.prepareModify");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/tenant/user/user_modify", modelMap);
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
		request.setAttribute("bean", bean);

		List<Dictory> dictories = dictoryService.getDictoryList(SysConstants.USER_HEADSHIP);
		modelMap.put("dictories", dictories);

		String x_view = ViewProperties.getString("tenant.user.prepareModifyInfo");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/tenant/user/user_change_info", modelMap);
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

		String x_view = ViewProperties.getString("tenant.user.prepareModifyPwd");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/tenant/user/user_modify_pwd", modelMap);
	}

	/**
	 * 显示重置密码页面
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/prepareResetPwd")
	public ModelAndView prepareResetPwd(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String userId = ParamUtil.getParameter(request, "userId");
		userId = RequestUtils.decodeString(userId);
		SysUser bean = sysUserService.findById(userId);
		request.setAttribute("user", bean);
		request.setAttribute("userId_encode", ParamUtil.getParameter(request, "userId"));

		String x_view = ViewProperties.getString("tenant.user.prepareResetPwd");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/tenant/user/user_reset_pwd", modelMap);
	}

	/**
	 * 重置登录信息
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/resetLoginStatus")
	public byte[] resetLoginStatus(HttpServletRequest request) {
		logger.debug(RequestUtils.getParameterMap(request));
		String userId = request.getParameter("userId");
		userId = RequestUtils.decodeString(userId);
		SysUser user = sysUserService.findByAccount(userId);
		if (user != null) {
			sysUserService.resetLoginStatus(userId);
			return ResponseUtils.responseJsonResult(true, "重置登录信息成功。");
		}
		return ResponseUtils.responseResult(false);
	}

	/**
	 * 重置用户密码
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/resetPwd")
	@ResponseBody
	public byte[] resetPwd(HttpServletRequest request) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		if (loginContext.isSystemAdministrator() || loginContext.isTenantAdmin()) {
			logger.debug(loginContext.getActorId() + " is admin");
		}

		/**
		 * 只允许admin账户才能重置其他用户的密码
		 */
		if (loginContext.isTenantAdmin() || (loginContext.isSystemAdministrator()
				&& (StringUtils.equalsIgnoreCase(loginContext.getActorId(), "admin")))) {
			String userId = ParamUtil.getParameter(request, "userId");
			userId = RequestUtils.decodeString(userId);
			SysUser user = sysUserService.findById(userId);
			if (user != null) {
				if (loginContext.isTenantAdmin()) {
					if (!StringUtils.equals(user.getTenantId(), loginContext.getTenantId())) {
						return ResponseUtils.responseResult(false);
					}
				}
				/**
				 * admin系统管理员的密码不允许重置
				 */
				if (!(StringUtils.equalsIgnoreCase(user.getUserId(), "admin"))) {
					String newPwd = ParamUtil.getParameter(request, "newPwd");
					if (user != null && StringUtils.isNotEmpty(newPwd)) {
						logger.info(loginContext.getActorId() + "重置" + user.getName() + "的密码。");
						try {
							sysUserService.changePassword(user.getUserId(), newPwd);
							return ResponseUtils.responseResult(true);
						} catch (Exception ex) {
							logger.error(ex);
						}
					}
				}
			}
		}

		return ResponseUtils.responseResult(false);
	}

	/**
	 * 提交增加信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveAdd")
	@ResponseBody
	public byte[] saveAdd(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isTenantAdmin() || loginContext.isSystemAdministrator()) {
			String tenantId = request.getParameter("tenantId");
			SysTenant sysTenant = null;
			try {
				if (!loginContext.isSystemAdministrator()) {
					tenantId = loginContext.getTenantId();
				}
				sysTenant = sysTenantService.getSysTenantByTenantId(tenantId);
				if (sysTenant != null && sysTenant.getLocked() == 0) {
					SysUser bean = new SysUser();
					Tools.populate(bean, RequestUtils.getParameterMap(request));
					bean.setOrganizationId(RequestUtils.getLong(request, "organizationId"));
					bean.setTenantId(sysTenant.getTenantId());
					bean.setUserId(String.valueOf(sysTenant.getId()
							+ StringTools.getDigit3Id(entityService.nextId("tenant_" + sysTenant.getId()).intValue())));
					bean.setName(ParamUtil.getParameter(request, "name"));
					String password = ParamUtil.getParameter(request, "password");
					bean.setPasswordHash(password);
					bean.setSex(ParamUtil.getIntParameter(request, "sex", 0));
					bean.setMobile(ParamUtil.getParameter(request, "mobile"));
					bean.setEmail(ParamUtil.getParameter(request, "email"));
					bean.setTelephone(ParamUtil.getParameter(request, "telephone"));
					bean.setLocked(ParamUtil.getIntParameter(request, "locked", 0));
					bean.setHeadship(ParamUtil.getParameter(request, "headship"));
					bean.setAccountType(8);
					bean.setUserType(8);
					bean.setEvection(0);
					bean.setCreateTime(new Date());
					bean.setCreateBy(RequestUtils.getActorId(request));
					bean.setUpdateBy(RequestUtils.getActorId(request));

					if (StringUtils.isNotEmpty(bean.getUserId()) && StringUtils.isNotEmpty(bean.getName())) {
						if (sysUserService.findByAccount(bean.getUserId()) == null) {
							if (sysUserService.create(bean)) {
								return ResponseUtils.responseJsonResult(true, "新用户编号为" + bean.getUserId());
							}
							return ResponseUtils.responseResult(false);
						}
					}
				} else {
					return ResponseUtils.responseJsonResult(false, "必须是管理员才能创建用户。");
				}
			} catch (Exception ex) {
				logger.error(ex);
			}
		} else {
			return ResponseUtils.responseJsonResult(false, "管理员才能创建用户。");
		}
		return ResponseUtils.responseResult(false);
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
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isTenantAdmin() || loginContext.isSystemAdministrator()) {
			String userId = ParamUtil.getParameter(request, "userId");
			try {
				userId = RequestUtils.decodeString(userId);
				logger.debug("userId = " + userId);
				SysUser bean = sysUserService.findById(userId);
				boolean ret = false;
				if (bean != null && (loginContext.isSystemAdministrator()
						|| StringUtils.equals(loginContext.getTenantId(), bean.getTenantId()))) {
					Tools.populate(bean, RequestUtils.getParameterMap(request));
					bean.setOrganizationId(RequestUtils.getLong(request, "organizationId"));
					bean.setName(ParamUtil.getParameter(request, "name"));
					bean.setSex(ParamUtil.getIntParameter(request, "sex", 0));
					bean.setMobile(ParamUtil.getParameter(request, "mobile"));
					bean.setEmail(ParamUtil.getParameter(request, "email"));
					bean.setTelephone(ParamUtil.getParameter(request, "telephone"));
					bean.setEvection(ParamUtil.getIntParameter(request, "evection", 0));
					bean.setLocked(ParamUtil.getIntParameter(request, "locked", 0));
					bean.setHeadship(ParamUtil.getParameter(request, "headship"));
					bean.setUpdateBy(RequestUtils.getActorId(request));
					bean.setUserId(userId);
					logger.debug("organizationId:" + bean.getOrganizationId());
					ret = sysUserService.update(bean);
					return ResponseUtils.responseResult(ret);
				}
			} catch (Exception ex) {
				logger.error(ex);
			}
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
			} catch (Exception ex) {
				logger.error(ex);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	/**
	 * 设置用户角色
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveRole")
	@ResponseBody
	public byte[] saveRole(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isTenantAdmin() || loginContext.isSystemAdministrator()) {
			String userId = ParamUtil.getParameter(request, "userId");
			SysUser user = sysUserService.findById(userId);// 查找用户对象
			if (user != null && (loginContext.isSystemAdministrator()
					|| StringUtils.equals(loginContext.getTenantId(), user.getTenantId()))) {// 用户存在
				String[] ids = ParamUtil.getParameterValues(request, "id");// 获取页面参数
				if (ids != null) {
					Set<SysRole> newRoles = new HashSet<SysRole>();
					for (int i = 0; i < ids.length; i++) {
						logger.debug("id[" + i + "]=" + ids[i]);
						SysRole role = sysRoleService.findById(ids[i]);// 查找角色对象
						if (role != null) {
							newRoles.add(role);// 加入到角色列表
						}
					}
					user.setUpdateBy(RequestUtils.getActorId(request));
					if (sysUserService.updateUserRole(user, 0, newRoles)) {// 授权成功
						return ResponseUtils.responseResult(true);
					}
				}
			}
		}

		return ResponseUtils.responseResult(false);
	}

	/**
	 * 提交修改信息
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveUserRole")
	public byte[] saveUserRole(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isTenantAdmin() || loginContext.isSystemAdministrator()) {
			String roleId = ParamUtil.getParameter(request, "roleId");
			String userId = request.getParameter("userId");
			String operation = request.getParameter("operation");
			SysRole bean = sysRoleService.findById(roleId);
			SysUser user = sysUserService.findByAccountWithAll(userId);
			if (bean != null && user != null && (loginContext.isSystemAdministrator()
					|| StringUtils.equals(loginContext.getTenantId(), user.getTenantId()))) {
				if (StringUtils.equals(operation, "revoke")) {
					sysUserService.deleteRoleUser(roleId, userId);
				} else {
					sysUserService.createRoleUser(roleId, userId);
				}
				return ResponseUtils.responseResult(true);
			}
		}

		return ResponseUtils.responseResult(false);
	}

	/**
	 * 设置用户角色
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveUserRoles")
	public byte[] saveUserRoles(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isTenantAdmin() || loginContext.isSystemAdministrator()) {
			String userId = RequestUtils.decodeString(request.getParameter("userId"));
			String objectIds = request.getParameter("objectIds");
			logger.debug("userId:" + userId);
			SysUser user = sysUserService.findById(userId);// 查找用户对象
			logger.debug("user:" + user);
			if (user != null && (loginContext.isSystemAdministrator()
					|| StringUtils.equals(loginContext.getTenantId(), user.getTenantId()))) {
				Set<SysRole> newRoles = new HashSet<SysRole>();
				if (StringUtils.isNotEmpty(objectIds)) {
					List<String> roleIds = StringTools.split(objectIds);// 获取页面参数
					if (roleIds != null) {
						for (int i = 0; i < roleIds.size(); i++) {
							logger.debug("roleId[" + i + "]=" + roleIds.get(i));
							SysRole role = sysRoleService.findById(roleIds.get(i));// 查找角色对象
							if (role != null) {
								newRoles.add(role);// 加入到角色列表
							}
						}
					}
				}
				logger.debug("newRoles:" + newRoles);
				user.setUpdateBy(RequestUtils.getActorId(request));
				if (sysUserService.updateUserRole(user, 0, newRoles)) {
					// 授权成功
					return ResponseUtils.responseResult(true);
				}
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@javax.annotation.Resource
	public void setDictoryService(DictoryService dictoryService) {
		this.dictoryService = dictoryService;
	}

	@javax.annotation.Resource
	public void setEntityService(EntityService entityService) {
		this.entityService = entityService;
	}

	@javax.annotation.Resource
	public void setSysOrganizationService(SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
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

	/**
	 * 显示角色
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/showRole")
	public ModelAndView showRole(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String userId = ParamUtil.getParameter(request, "userId");
		userId = RequestUtils.decodeString(userId);
		logger.debug("userId:" + userId);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isTenantAdmin() || loginContext.isSystemAdministrator()) {
			SysUser user = sysUserService.findByAccountWithAll(userId);
			if (user != null && (loginContext.isSystemAdministrator()
					|| StringUtils.equals(loginContext.getTenantId(), user.getTenantId()))) {
				List<SysRole> list = new ArrayList<SysRole>();
				List<SysRole> roles = sysRoleService.getSysRoleList();
				if (roles != null && !roles.isEmpty()) {
					for (SysRole role : roles) {
						if (StringUtils.equals(role.getIsUseOrganization(), "Y")) {
							list.add(role);
						}
					}
				}

				List<SysRole> userRoles = sysUserService.getUserRoles(userId);
				List<String> selecteds = new ArrayList<String>();

				if (userRoles != null && !userRoles.isEmpty()) {
					for (SysRole role : userRoles) {
						selecteds.add(role.getId());
					}
				}

				request.setAttribute("user", user);
				request.setAttribute("list", list);

				logger.debug("可分配角色:" + list.size());

				StringBuilder bufferx = new StringBuilder();
				StringBuilder buffery = new StringBuilder();

				if (list != null && list.size() > 0) {
					for (int j = 0; j < list.size(); j++) {
						SysRole r = (SysRole) list.get(j);
						if (selecteds != null && selecteds.contains(r.getId())) {
							buffery.append("\n<option value=\"").append(r.getId()).append("\">").append(r.getName())
									.append("</option>");
						} else {
							bufferx.append("\n<option value=\"").append(r.getId()).append("\">").append(r.getName())
									.append("</option>");
						}
					}
				}

				request.setAttribute("bufferx", bufferx.toString());
				request.setAttribute("buffery", buffery.toString());
			}
		}

		String x_view = ViewProperties.getString("tenant.user.userRole");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		// 显示列表页面
		return new ModelAndView("/tenant/user/userRole", modelMap);
	}

	/**
	 * 查看角色用户
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/showUser")
	public ModelAndView showUser(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String userId = ParamUtil.getParameter(request, "userId");
		userId = RequestUtils.decodeString(userId);
		SysUser user = sysUserService.findById(userId);
		request.setAttribute("user", user);

		String x_view = ViewProperties.getString("tenant.user.showUser");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/tenant/user/user_info", modelMap);
	}

}