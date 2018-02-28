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

package com.glaf.base.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;

import com.glaf.core.cache.CacheFactory;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.service.EntityService;
import com.glaf.core.util.Constants;
import com.glaf.core.util.StringTools;

import com.glaf.base.modules.sys.model.SysRole;
import com.glaf.base.modules.sys.model.SysUser;
import com.glaf.base.modules.sys.service.SysApplicationService;
import com.glaf.base.modules.sys.service.SysRoleService;
import com.glaf.base.modules.sys.service.SysUserService;
import com.glaf.base.modules.sys.util.SysRoleJsonFactory;
import com.glaf.base.utils.ContextUtil;

public class BaseIdentityFactory {

	protected static final Log logger = LogFactory.getLog(BaseIdentityFactory.class);

	protected static volatile EntityService entityService;

	protected static volatile SysApplicationService sysApplicationService;

	protected static volatile SysRoleService sysRoleService;

	protected static volatile SysUserService sysUserService;

	/**
	 * 根据用户名获取用户对象
	 * 
	 * @param actorId
	 *            用户登录账号
	 * @return
	 */
	public static SysUser findByAccount(String actorId) {
		return getSysUserService().findByAccount(actorId);
	}

	public static EntityService getEntityService() {
		if (entityService == null) {
			entityService = ContextFactory.getBean("entityService");
		}
		return entityService;
	}

	/**
	 * 获取全部用户
	 * 
	 * @return
	 */
	public static Map<String, SysUser> getLowerCaseUserMap() {
		Map<String, SysUser> userMap = new LinkedHashMap<String, SysUser>();
		List<SysUser> users = getSysUserService().getSysUserList(0);
		if (users != null && !users.isEmpty()) {
			for (SysUser user : users) {
				userMap.put(user.getUserId().toLowerCase(), user);
			}
		}
		return userMap;
	}

	/**
	 * 通过角色代码获取角色
	 * 
	 * @param code
	 *            角色代码
	 * @return
	 */
	public static SysRole getRoleByCode(String code) {
		return getSysRoleService().findByCode(code);
	}

	/**
	 * 通过角色编号获取角色
	 * 
	 * @param id
	 *            角色ID
	 * @return
	 */
	public static SysRole getRoleById(String id) {
		return getSysRoleService().findById(id);
	}

	/**
	 * 获取全部角色 Map
	 * 
	 * @return
	 */
	public static Map<String, SysRole> getRoleMap() {
		Map<String, SysRole> roleMap = new HashMap<String, SysRole>();
		List<SysRole> roles = getSysRoleService().getSysRoleList();
		if (roles != null && !roles.isEmpty()) {
			for (SysRole role : roles) {
				roleMap.put(role.getCode(), role);
			}
		}
		return roleMap;
	}

	/**
	 * 获取全部角色
	 * 
	 * @return
	 */
	public static List<SysRole> getRoles() {
		List<SysRole> roles = getSysRoleService().getSysRoleList();
		return roles;
	}

	public static SysApplicationService getSysApplicationService() {
		if (sysApplicationService == null) {
			sysApplicationService = ContextFactory.getBean("sysApplicationService");
		}
		return sysApplicationService;
	}

	public static SysRoleService getSysRoleService() {
		if (sysRoleService == null) {
			sysRoleService = ContextFactory.getBean("sysRoleService");
		}
		return sysRoleService;
	}

	public static SysUserService getSysUserService() {
		if (sysUserService == null) {
			sysUserService = ContextFactory.getBean("sysUserService");
		}
		return sysUserService;
	}

	/**
	 * 根据用户名获取用户对象
	 * 
	 * @param actorId
	 *            用户登录账号
	 * @return
	 */
	public static SysUser getSysUserWithAll(String actorId) {
		SysUser user = getSysUserService().findByAccountWithAll(actorId);
		if (user != null) {
			ContextUtil.put(actorId, user);
		}
		return user;
	}

	/**
	 * 获取全部用户Map
	 * 
	 * @return
	 */
	public static Map<String, SysUser> getUserMap() {
		Map<String, SysUser> userMap = new LinkedHashMap<String, SysUser>();
		List<SysUser> users = getSysUserService().getSysUserList(0);
		if (users != null && !users.isEmpty()) {
			for (SysUser user : users) {
				userMap.put(user.getUserId(), user);
			}
		}
		return userMap;
	}

	/**
	 * 获取某些用户的角色代码
	 * 
	 * @param actorIds
	 * @return
	 */
	public static List<String> getUserRoleCodes(List<String> actorIds) {
		if (actorIds == null || actorIds.isEmpty()) {
			return new ArrayList<String>();
		}
		StringBuilder buffer = new StringBuilder();
		buffer.append(Constants.CACHE_USER_ROLE_CODE_KEY);
		for (String actorId : actorIds) {
			buffer.append("_").append(actorId);
		}

		String cacheKey = buffer.toString();
		String text = CacheFactory.getString(Constants.CACHE_USER_ROLE_CODE_REGION, cacheKey);
		if (text != null) {
			return StringTools.split(text);
		}

		List<String> codes = new ArrayList<String>();
		List<SysRole> list = getUserRoles(actorIds);
		buffer.delete(0, buffer.length());
		if (list != null && !list.isEmpty()) {
			for (SysRole role : list) {
				if (!codes.contains(role.getCode())) {
					codes.add(role.getCode());
					buffer.append(role.getCode()).append(",");
				}
			}
		}
		CacheFactory.put(Constants.CACHE_USER_ROLE_CODE_REGION, cacheKey, buffer.toString());
		return codes;
	}

	public static List<SysRole> getUserRoles(List<String> actorIds) {
		if (actorIds == null || actorIds.isEmpty()) {
			return new ArrayList<SysRole>();
		}

		StringBuilder buffer = new StringBuilder();
		buffer.append(Constants.CACHE_USER_ROLE_KEY);
		for (String actorId : actorIds) {
			buffer.append("_").append(actorId);
		}

		String cacheKey = buffer.toString();
		String text = CacheFactory.getString(Constants.CACHE_USER_ROLE_REGION, cacheKey);
		if (text != null) {
			try {
				com.alibaba.fastjson.JSONArray array = JSON.parseArray(text);
				return SysRoleJsonFactory.arrayToList(array);
			} catch (Exception ex) {
			}
		}

		List<SysRole> roles = getSysUserService().getUserRoles(actorIds);
		if (roles != null && !roles.isEmpty()) {
			com.alibaba.fastjson.JSONArray array = SysRoleJsonFactory.listToArray(roles);
			CacheFactory.put(Constants.CACHE_USER_ROLE_REGION, cacheKey, array.toJSONString());
		}
		return roles;
	}

	/**
	 * 获取某个用户及代理人的角色编号
	 * 
	 * @param actorId
	 *            用户登录账号
	 * @return
	 */
	public static List<String> getUserRoles(String actorId) {
		List<String> actorIds = new ArrayList<String>();
		actorIds.add(actorId);
		return getUserRoleCodes(actorIds);
	}

	public static void setSysApplicationService(SysApplicationService sysApplicationService) {
		BaseIdentityFactory.sysApplicationService = sysApplicationService;
	}

	public static void setSysRoleService(SysRoleService sysRoleService) {
		BaseIdentityFactory.sysRoleService = sysRoleService;
	}

	public static void setSysUserService(SysUserService sysUserService) {
		BaseIdentityFactory.sysUserService = sysUserService;
	}

}
