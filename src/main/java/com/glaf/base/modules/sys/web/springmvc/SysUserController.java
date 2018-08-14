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

package com.glaf.base.modules.sys.web.springmvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.glaf.core.cache.CacheUtils;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.domain.ServerEntity;
import com.glaf.core.security.LoginContext;
import com.glaf.core.service.IServerEntityService;
import com.glaf.core.service.ITableDataService;
import com.glaf.core.util.PageResult;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.StringTools;
import com.glaf.core.util.Tools;

import com.glaf.base.modules.sys.SysConstants;
import com.glaf.base.modules.sys.model.Dictory;
import com.glaf.base.modules.sys.model.SysOrganization;
import com.glaf.base.modules.sys.model.SysRole;
import com.glaf.base.modules.sys.model.SysUser;
import com.glaf.base.modules.sys.query.SysUserQuery;
import com.glaf.base.modules.sys.service.DictoryService;
import com.glaf.base.modules.sys.service.SysOrganizationService;
import com.glaf.base.modules.sys.service.SysRoleService;
import com.glaf.base.modules.sys.service.SysUserService;
import com.glaf.base.utils.ParamUtil;
import com.glaf.base.utils.RequestUtil;

@Controller("/sys/user")
@RequestMapping("/sys/user")
public class SysUserController {
	private static final Log logger = LogFactory.getLog(SysUserController.class);

	protected DictoryService dictoryService;

	protected SysRoleService sysRoleService;

	protected SysUserService sysUserService;

	protected ITableDataService tableDataService;

	protected IServerEntityService serverEntityService;

	protected SysOrganizationService sysOrganizationService;

	/**
	 * 增加角色用户
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/addRoleUser")
	@ResponseBody
	public byte[] addRoleUser(HttpServletRequest request) {
		logger.debug("---------addRoleUser---------------------------");
		String roleId = ParamUtil.getParameter(request, "roleId");
		try {
			String[] userIds = ParamUtil.getParameterValues(request, "userId");
			for (int i = 0; i < userIds.length; i++) {
				SysUser user = sysUserService.findById(userIds[i]);
				if (user != null) {
					sysUserService.createRoleUser(roleId, user.getActorId());
				}
			}
			return ResponseUtils.responseResult(true);
		} catch (Exception ex) {
			logger.error(ex);
		}
		return ResponseUtils.responseResult(false);
	}

	/**
	 * 删除角色用户
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/delRoleUser")
	@ResponseBody
	public byte[] delRoleUser(HttpServletRequest request) {
		String roleId = ParamUtil.getParameter(request, "roleId");
		try {
			SysRole role = sysRoleService.findById(roleId);
			String[] userIds = ParamUtil.getParameterValues(request, "userId");
			sysUserService.deleteRoleUsers(role, userIds);
			return ResponseUtils.responseResult(true);
		} catch (Exception ex) {
			logger.error(ex);
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request) throws IOException {
		long organizationId = RequestUtils.getLong(request, "organizationId");

		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SysUserQuery query = new SysUserQuery();
		Tools.populate(query, params);
		query.setDeleteFlag(0);
		query.setUserType(0);

		if (organizationId > 0) {
			query.organizationId(organizationId);
		}
		
		String namePinyinLike = request.getParameter("namePinyinLike");
		if (StringUtils.isNotEmpty(namePinyinLike)) {
			query.setNamePinyinLike(namePinyinLike);
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

				result.put("rows", rowsJSON);

				for (SysUser sysUser : list) {
					JSONObject rowJSON = sysUser.toJsonObject();
					rowJSON.put("id", sysUser.getId());
					rowJSON.put("userId", sysUser.getUserId());
					rowJSON.put("startIndex", ++start);
					rowJSON.remove("token");
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

	@RequestMapping
	public ModelAndView list(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		request.setAttribute("parent", request.getParameter("parent"));

		ServerEntity serverEntity = serverEntityService.getServerEntityByMapping("GNRemote");
		if (serverEntity != null) {
			request.setAttribute("serverEntity", serverEntity);
		}
		
		List<String> charList = new ArrayList<String>();
		for (int i = 65; i < 91; i++) {
			charList.add("" + (char) i);
		}
		request.setAttribute("charList", charList);

		String x_view = ViewProperties.getString("user.list");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/sys/user/list", modelMap);
	}

	@RequestMapping("/organizationUsers")
	public ModelAndView organizationUsers(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String x_view = ViewProperties.getString("user.organizationUsers");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/sys/user/organizationUsers", modelMap);
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
		long organizationId = RequestUtils.getLong(request, "organizationId");
		if (organizationId > 0) {
			SysOrganization organization = sysOrganizationService.findById(organizationId);
			request.setAttribute("organization", organization);
			if (organization != null) {
				request.setAttribute("organizationName", organization.getName());
			}
		}

		List<Dictory> dictories = dictoryService.getDictoryList(SysConstants.USER_HEADSHIP);
		modelMap.put("dictories", dictories);

		List<Dictory> accounts = dictoryService.getDictoryList(SysConstants.USER_ACCOUNTTYPE);
		modelMap.put("accountTypeDictories", accounts);

		String x_view = ViewProperties.getString("user.prepareAdd");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/user/user_add", modelMap);
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
		long organizationId = RequestUtils.getLong(request, "organizationId");
		String userId = ParamUtil.getParameter(request, "userId");
		userId = RequestUtils.decodeString(userId);
		SysUser bean = sysUserService.findById(userId);
		if (bean != null) {
			organizationId = bean.getOrganizationId();
			request.setAttribute("user", bean);
			request.setAttribute("userId_encode", RequestUtils.encodeString(bean.getActorId()));
		}

		if (organizationId > 0) {
			SysOrganization organization = sysOrganizationService.findById(organizationId);
			request.setAttribute("organization", organization);
			if (organization != null) {
				request.setAttribute("organizationName", organization.getName());
			}
		}

		List<Dictory> dictories = dictoryService.getDictoryList(SysConstants.USER_HEADSHIP);
		modelMap.put("dictories", dictories);

		List<Dictory> accounts = dictoryService.getDictoryList(SysConstants.USER_ACCOUNTTYPE);
		modelMap.put("accountTypeDictories", accounts);

		String x_view = ViewProperties.getString("user.prepareModify");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/user/user_modify", modelMap);
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

		String x_view = ViewProperties.getString("user.prepareModifyInfo");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/user/user_change_info", modelMap);
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

		String x_view = ViewProperties.getString("user.prepareModifyPwd");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/user/user_modify_pwd", modelMap);
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

		String x_view = ViewProperties.getString("user.prepareResetPwd");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/user/user_reset_pwd", modelMap);
	}

	/**
	 * 显示重置密锁页面
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/prepareResetToken")
	public ModelAndView prepareResetToken(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String userId = ParamUtil.getParameter(request, "userId");
		userId = RequestUtils.decodeString(userId);
		SysUser bean = sysUserService.findById(userId);
		request.setAttribute("user", bean);

		String x_view = ViewProperties.getString("user.prepareResetToken");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/user/reset_token", modelMap);
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

		if (loginContext.isSystemAdministrator()) {
			logger.debug(loginContext.getActorId() + " is system admin");
		}

		/**
		 * 只允许admin账户才能重置其他用户的密码
		 */
		if (loginContext.isSystemAdministrator()
				&& (StringUtils.equalsIgnoreCase(loginContext.getActorId(), "admin"))) {
			String userId = ParamUtil.getParameter(request, "userId");
			SysUser bean = sysUserService.findById(userId);
			if (bean != null) {
				/**
				 * admin系统管理员的密码不允许重置
				 */
				if (!(StringUtils.equalsIgnoreCase(bean.getUserId(), "admin"))) {
					String newPwd = ParamUtil.getParameter(request, "newPwd");
					if (bean != null && StringUtils.isNotEmpty(newPwd)) {
						logger.info(loginContext.getActorId() + "重置" + bean.getName() + "的密码。");
						try {
							sysUserService.changePassword(bean.getUserId(), newPwd);
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
	 * 重置用户令牌及密锁
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/resetUserToken")
	public byte[] resetUserToken(HttpServletRequest request) {
		logger.debug(RequestUtils.getParameterMap(request));
		String userId = request.getParameter("userId");
		userId = RequestUtils.decodeString(userId);
		SysUser user = sysUserService.resetUserToken(userId);
		if (user != null) {
			return ResponseUtils.responseJsonResult(true, "令牌重置成功，请通知客户端程序更新令牌。");
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
		try {
			SysUser bean = new SysUser();
			Tools.populate(bean, RequestUtils.getParameterMap(request));
			bean.setOrganizationId(RequestUtils.getLong(request, "organizationId"));
			bean.setUserId(ParamUtil.getParameter(request, "userId"));
			bean.setName(ParamUtil.getParameter(request, "name"));
			String password = ParamUtil.getParameter(request, "password");
			bean.setPasswordHash(password);
			bean.setSex(ParamUtil.getIntParameter(request, "sex", 0));
			bean.setMobile(ParamUtil.getParameter(request, "mobile"));
			bean.setEmail(ParamUtil.getParameter(request, "email"));
			bean.setTelephone(ParamUtil.getParameter(request, "telephone"));
			bean.setLocked(ParamUtil.getIntParameter(request, "locked", 0));
			bean.setHeadship(ParamUtil.getParameter(request, "headship"));
			bean.setAccountType(ParamUtil.getIntParameter(request, "accountType", 0));
			bean.setUserType(ParamUtil.getIntParameter(request, "userType", 0));
			bean.setSecretLoginFlag(request.getParameter("secretLoginFlag"));
			bean.setEvection(0);
			bean.setCreateTime(new Date());
			bean.setLastLoginTime(new Date());
			bean.setCreateBy(RequestUtils.getActorId(request));
			bean.setUpdateBy(RequestUtils.getActorId(request));
			logger.debug("sex:" + ParamUtil.getIntParameter(request, "sex", 0));

			if (StringUtils.isNotEmpty(bean.getUserId()) && StringUtils.isNotEmpty(bean.getName())) {
				if (sysUserService.findByAccount(bean.getUserId()) == null) {
					if (sysUserService.create(bean)) {
						return ResponseUtils.responseResult(true);
					}
					return ResponseUtils.responseResult(false);
				}
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
	@RequestMapping("/saveModify")
	@ResponseBody
	public byte[] saveModify(HttpServletRequest request) {
		logger.debug(RequestUtils.getParameterMap(request));
		String userId = ParamUtil.getParameter(request, "userId");
		try {
			userId = RequestUtils.decodeString(userId);
			logger.debug("userId = " + userId);
			SysUser bean = sysUserService.findById(userId);
			logger.debug("user = " + bean);
			boolean ret = false;
			if (bean != null) {
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
				bean.setAccountType(ParamUtil.getIntParameter(request, "accountType", 0));
				bean.setUserType(ParamUtil.getIntParameter(request, "userType", 0));
				bean.setSecretLoginFlag(request.getParameter("secretLoginFlag"));
				bean.setUpdateBy(RequestUtils.getActorId(request));
				bean.setUserId(userId);
				logger.debug("organizationId:" + RequestUtils.getLong(request, "organizationId"));
				logger.debug("organizationId:" + bean.getOrganizationId());
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
		logger.debug(RequestUtils.getParameterMap(request));
		String userId = ParamUtil.getParameter(request, "userId");
		SysUser user = sysUserService.findById(userId);// 查找用户对象
		if (user != null) {// 用户存在
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
		logger.debug(RequestUtils.getParameterMap(request));
		String roleId = ParamUtil.getParameter(request, "roleId");
		String userId = request.getParameter("userId");
		String operation = request.getParameter("operation");
		SysRole bean = sysRoleService.findById(roleId);
		SysUser user = sysUserService.findByAccountWithAll(userId);
		if (bean != null && user != null) {
			if (StringUtils.equals(operation, "revoke")) {
				sysUserService.deleteRoleUser(roleId, userId);
			} else {
				sysUserService.createRoleUser(roleId, userId);
			}
			return ResponseUtils.responseResult(true);
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
		logger.debug(RequestUtils.getParameterMap(request));
		String userId = RequestUtils.decodeString(request.getParameter("userId"));
		String objectIds = request.getParameter("objectIds");
		logger.debug("userId:" + userId);
		SysUser user = sysUserService.findById(userId);// 查找用户对象
		logger.debug("user:" + user);
		if (user != null) {
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

		return ResponseUtils.responseResult(false);
	}

	@javax.annotation.Resource
	public void setDictoryService(DictoryService dictoryService) {
		this.dictoryService = dictoryService;
	}

	@javax.annotation.Resource
	public void setServerEntityService(IServerEntityService serverEntityService) {
		this.serverEntityService = serverEntityService;
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
		SysUser user = sysUserService.findByAccountWithAll(userId);

		List<SysRole> list = new ArrayList<SysRole>();
		List<SysRole> roles = sysRoleService.getSysRoleList();
		if (roles != null && !roles.isEmpty()) {
			for (SysRole role : roles) {
				list.add(role);
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

		String x_view = ViewProperties.getString("user.userRole");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		// 显示列表页面
		return new ModelAndView("/sys/user/userRole", modelMap);
	}

	/**
	 * 增加角色用户
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

		String x_view = ViewProperties.getString("user.showUser");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/user/user_info", modelMap);
	}

	/**
	 * 显示角色菜单
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/showUserMenus")
	public ModelAndView showUserMenus(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String userId = ParamUtil.getParameter(request, "userId");
		userId = RequestUtils.decodeString(userId);
		SysUser user = sysUserService.findById(userId);
		request.setAttribute("user", user);

		String x_view = ViewProperties.getString("user.userMenus");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/user/userMenus", modelMap);
	}

	@RequestMapping("/userjson")
	@ResponseBody
	public byte[] userjson(HttpServletRequest request) throws IOException {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SysUserQuery query = new SysUserQuery();
		Tools.populate(query, params);
		query.setDeleteFlag(0);
		query.setUserType(0);

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

				result.put("rows", rowsJSON);

				for (SysUser sysUser : list) {
					JSONObject rowJSON = sysUser.toJsonObject();
					rowJSON.put("id", sysUser.getId());
					rowJSON.put("userId", sysUser.getUserId());
					rowJSON.put("startIndex", ++start);
					rowJSON.remove("token");
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

	@RequestMapping("/userlist")
	public ModelAndView userlist(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String x_view = ViewProperties.getString("user.userlist");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/sys/user/userlist", modelMap);
	}
}