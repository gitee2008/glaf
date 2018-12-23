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

package com.glaf.base.modules.sys.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.glaf.core.base.TableModel;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.security.Authentication;
import com.glaf.core.cache.CacheFactory;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.service.ITableDataService;
import com.glaf.core.util.Constants;
import com.glaf.core.util.PageResult;
import com.glaf.core.util.UUID32;

import com.glaf.base.modules.sys.SysConstants;
import com.glaf.base.modules.sys.mapper.SysAccessMapper;
import com.glaf.base.modules.sys.mapper.SysApplicationMapper;
import com.glaf.base.modules.sys.mapper.SysRoleMapper;
import com.glaf.base.modules.sys.mapper.SysUserMapper;
import com.glaf.base.modules.sys.mapper.SysUserRoleMapper;
import com.glaf.base.modules.sys.model.Membership;
import com.glaf.base.modules.sys.model.SysRole;
import com.glaf.base.modules.sys.model.SysUser;
import com.glaf.base.modules.sys.model.SysUserRole;
import com.glaf.base.modules.sys.model.UserRole;
import com.glaf.base.modules.sys.query.SysUserQuery;
import com.glaf.base.modules.sys.query.UserRoleQuery;
import com.glaf.base.modules.sys.service.MembershipService;
import com.glaf.base.modules.sys.service.SysUserService;
import com.glaf.base.modules.sys.util.PinyinUtils;
import com.glaf.base.modules.sys.util.SysRoleJsonFactory;
import com.glaf.base.modules.sys.util.SysUserJsonFactory;

@Service("sysUserService")
@Transactional(readOnly = true)
public class SysUserServiceImpl implements SysUserService {
	protected final static Log logger = LogFactory.getLog(SysUserServiceImpl.class);

	// protected static ConcurrentMap<String, String> passwordMap = new
	// ConcurrentHashMap<String, String>();

	protected IdGenerator idGenerator;

	protected MembershipService membershipService;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected SysAccessMapper sysAccessMapper;

	protected SysApplicationMapper sysApplicationMapper;

	protected SysRoleMapper sysRoleMapper;

	protected SysUserMapper sysUserMapper;

	protected SysUserRoleMapper sysUserRoleMapper;

	protected ITableDataService tableDataService;

	public SysUserServiceImpl() {

	}

	/**
	 * 修改用户密码
	 * 
	 * @param account
	 * @param password
	 */
	public void changePassword(String account, String password) {
		if (!StringUtils.equals(password, "88888888") && password.length() < 64) {
			String pwd_hash = DigestUtils.md5Hex(account.toLowerCase() + DigestUtils.sha512Hex(password));
			SysUser bean = new SysUser();
			bean.setUserId(account);
			bean.setPasswordHash(pwd_hash);
			// passwordMap.put(account, pwd_hash);
			sysUserMapper.updateUserPassword(bean);
		}
	}

	/**
	 * 检查用户登录密锁是否正确
	 * 
	 * @param account
	 * @param loginSecret
	 * @return
	 */
	public boolean checkLoginSecret(String account, String loginSecret) {
		boolean result = false;
		SysUser bean = sysUserMapper.getSysUserLoginSecret(account);
		if (StringUtils.isNotEmpty(loginSecret) && StringUtils.equals(loginSecret, bean.getLoginSecret())) {
			result = true;
		}
		return result;
	}

	/**
	 * 检查用户密码是否正确
	 * 
	 * @param account
	 * @param password
	 * @return
	 */
	public boolean checkPassword(String account, String password) {
		boolean result = false;
		String pwd_hash = DigestUtils.md5Hex(account.toLowerCase() + DigestUtils.sha512Hex(password));
		String pwd = sysUserMapper.getPasswordHashByAccount(account);
		if (StringUtils.isNotEmpty(password) && StringUtils.equals(pwd_hash, pwd)) {
			result = true;
		}
		return result;
	}

	public int count(SysUserQuery query) {
		return sysUserMapper.getSysUserCount(query);
	}

	@Transactional
	public boolean create(SysUser bean) {
		bean.setCreateTime(new Date());
		bean.setToken(UUID32.getUUID());
		if (StringUtils.isEmpty(bean.getPasswordHash())) {
			bean.setPasswordHash("888888");
		}
		String pwd_hash = DigestUtils
				.md5Hex(bean.getActorId().toLowerCase() + DigestUtils.sha512Hex(bean.getPasswordHash()));
		bean.setPasswordHash(pwd_hash);
		this.save(bean);
		return true;
	}

	@Transactional
	public void createRoleUser(String roleId, String actorId) {
		SysUser user = this.findByAccount(actorId);

		if (SystemConfig.getBoolean("use_query_cache")) {
			CacheFactory.clear(Constants.CACHE_ROLE_REGION);
			CacheFactory.clear(Constants.CACHE_USER_REGION);
		}

		TableModel table = new TableModel();
		table.setTableName("SYS_USER_ROLE");
		table.addStringColumn("ID", idGenerator.getNextId());
		table.addIntegerColumn("AUTHORIZED", 0);
		table.addStringColumn("ROLEID", roleId);
		table.addStringColumn("USERID", actorId);
		table.addStringColumn("TENANTID", user.getTenantId());
		tableDataService.insertTableData(table);

		TableModel table2 = new TableModel();
		table2.setTableName("SYS_MEMBERSHIP");
		table2.addLongColumn("ID_", idGenerator.nextId());
		table2.addStringColumn("ACTORID_", actorId);
		table2.addStringColumn("ROLEID_", roleId);
		table2.addStringColumn("TENANTID_", user.getTenantId());
		table2.addLongColumn("NODEID_", user.getOrganizationId());
		table2.addStringColumn("TYPE_", "SysUserRole");
		tableDataService.insertTableData(table2);

	}

	@Transactional
	public boolean delete(SysUser bean) {
		if (SystemConfig.getBoolean("use_query_cache")) {
			CacheFactory.clear(Constants.CACHE_USER_REGION);
		}

		TableModel table = new TableModel();
		table.setTableName("SYS_USER");
		table.addStringColumn("USERID", bean.getUserId());
		tableDataService.deleteTableData(table);

		TableModel table1 = new TableModel();
		table1.setTableName("SYS_USER_ROLE");
		table1.addIntegerColumn("AUTHORIZED", 0);
		table1.addStringColumn("USERID", bean.getUserId());
		tableDataService.deleteTableData(table1);

		TableModel table2 = new TableModel();
		table2.setTableName("SYS_MEMBERSHIP");
		table2.addStringColumn("ACTORID_", bean.getUserId());
		tableDataService.deleteTableData(table2);

		return true;
	}

	@Transactional
	public void deleteRoleUser(String roleId, String actorId) {
		if (SystemConfig.getBoolean("use_query_cache")) {
			CacheFactory.clear(Constants.CACHE_ROLE_REGION);
			CacheFactory.clear(Constants.CACHE_USER_REGION);
		}

		SysUser user = this.findByAccount(actorId);

		TableModel table = new TableModel();
		table.setTableName("SYS_USER_ROLE");
		table.addIntegerColumn("AUTHORIZED", 0);
		table.addStringColumn("ROLEID", roleId);
		table.addStringColumn("USERID", actorId);
		tableDataService.deleteTableData(table);

		TableModel table2 = new TableModel();
		table2.setTableName("SYS_MEMBERSHIP");
		table2.addStringColumn("ACTORID_", actorId);
		table2.addStringColumn("ROLEID_", roleId);
		table2.addLongColumn("NODEID_", user.getOrganizationId());
		tableDataService.deleteTableData(table2);

	}

	/**
	 * 删除角色用户
	 * 
	 * @param role
	 * @param userIds
	 */
	@Transactional
	public void deleteRoleUsers(SysRole role, String[] userIds) {

		if (SystemConfig.getBoolean("use_query_cache")) {
			CacheFactory.clear(Constants.CACHE_ROLE_REGION);
			CacheFactory.clear(Constants.CACHE_USER_REGION);
		}

		for (int i = 0; i < userIds.length; i++) {
			SysUser user = this.findById(userIds[i]);
			if (user != null) {
				logger.debug(user.getName());
				TableModel table = new TableModel();
				table.setTableName("SYS_USER_ROLE");
				table.addIntegerColumn("AUTHORIZED", 0);
				table.addStringColumn("ROLEID", role.getId());
				table.addStringColumn("USERID", user.getActorId());
				tableDataService.deleteTableData(table);

				TableModel table2 = new TableModel();
				table2.setTableName("SYS_MEMBERSHIP");
				table2.addStringColumn("ACTORID_", user.getUserId());
				table2.addStringColumn("ROLEID_", role.getId());
				tableDataService.deleteTableData(table2);
			}
		}

	}

	public SysUser findByAccount(String account) {
		String cacheKey = "user_" + account;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString(Constants.CACHE_USER_REGION, cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONObject json = JSON.parseObject(text);
					return SysUserJsonFactory.jsonToObject(json);
				} catch (Exception ex) {
					// Ignore error
				}
			}
		}
		SysUser user = sysUserMapper.getSysUserByAccount(account);
		if (user != null) {
			if (SystemConfig.getBoolean("use_query_cache")) {
				JSONObject json = user.toJsonObject();
				CacheFactory.put(Constants.CACHE_USER_REGION, cacheKey, json.toJSONString());
			}
			return user;
		}
		return null;
	}

	public SysUser findByAccountWithAll(String account) {
		String cacheKey = "user_all_" + account;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString(Constants.CACHE_USER_REGION, cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONObject json = JSON.parseObject(text);
					return SysUserJsonFactory.jsonToObject(json);
				} catch (Exception ex) {
					// Ignore error
				}
			}
		}

		SysUser user = sysUserMapper.getSysUserByAccount(account);
		if (user != null) {
			List<String> actorIds = new ArrayList<String>();
			actorIds.add(account);
			List<SysRole> roles = this.getUserRoles(actorIds);
			user.getRoles().addAll(roles);
			List<SysUserRole> userRoles = sysUserRoleMapper.getSysUserRolesByUserId(user.getActorId());
			user.getUserRoles().addAll(userRoles);

			if (SystemConfig.getBoolean("use_query_cache")) {
				JSONObject jsonObject = SysUserJsonFactory.toJsonObject(user);
				CacheFactory.put(Constants.CACHE_USER_REGION, cacheKey, jsonObject.toJSONString());
			}

			return user;
		}

		return null;
	}

	@Override
	public SysUser findById(String account) {
		String cacheKey = "user_" + account;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString(Constants.CACHE_USER_REGION, cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONObject json = JSON.parseObject(text);
					return SysUserJsonFactory.jsonToObject(json);
				} catch (Exception ex) {
					// Ignore error
				}
			}
		}

		SysUser user = sysUserMapper.getSysUserByAccount(account);
		if (user != null) {
			if (SystemConfig.getBoolean("use_query_cache")) {
				JSONObject json = user.toJsonObject();
				CacheFactory.put(Constants.CACHE_USER_REGION, cacheKey, json.toJSONString());
			}
		}
		return user;
	}

	public SysUser findByMail(String mail) {
		SysUser user = sysUserMapper.getSysUserByMail(mail);
		return user;
	}

	public SysUser findByMobile(String mobile) {
		String cacheKey = "user_" + mobile;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString(Constants.CACHE_USER_REGION, cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONObject json = JSON.parseObject(text);
					return SysUserJsonFactory.jsonToObject(json);
				} catch (Exception ex) {
					// Ignore error
				}
			}
		}
		SysUser user = sysUserMapper.getSysUserByMobile(mobile);
		if (user != null) {
			if (SystemConfig.getBoolean("use_query_cache")) {
				JSONObject json = user.toJsonObject();
				CacheFactory.put(Constants.CACHE_USER_REGION, cacheKey, json.toJSONString());
			}
		}
		return user;
	}

	public List<SysUser> getAllUsers(SysUserQuery query) {
		RowBounds rowBounds = new RowBounds(0, 50000);
		List<SysUser> rows = sqlSessionTemplate.selectList("getSysUsers", query, rowBounds);
		return rows;
	}

	public List<UserRole> getRoleUserViews(UserRoleQuery query) {
		return sysUserMapper.getRoleUserViews(query);
	}

	public List<SysUser> getSupplierUser(String supplierNo) {
		SysUserQuery query = new SysUserQuery();
		query.setUserId(supplierNo);
		query.setDeleteFlag(0);
		List<SysUser> users = this.list(query);
		return users;
	}

	public SysUser getSysUserByAppId(String appId) {
		SysUser user = sysUserMapper.getSysUserByAppId(appId);
		return user;
	}

	public int getSysUserCountByQueryCriteria(SysUserQuery query) {
		return sysUserMapper.getSysUserCount(query);
	}

	public int getSysUserExCountByQueryCriteria(SysUserQuery query) {
		return sysUserMapper.getSysUserExCount(query);
	}

	public List<SysUser> getSysUserList(int userType) {
		SysUserQuery query = new SysUserQuery();
		query.userType(userType);
		query.setDeleteFlag(0);
		return this.list(query);
	}

	public PageResult getSysUserList(int pageNo, int pageSize) {
		// 计算总数
		PageResult pager = new PageResult();
		SysUserQuery query = new SysUserQuery();
		query.setDeleteFlag(0);

		int count = this.count(query);
		if (count == 0) {// 结果集为空
			pager.setPageSize(pageSize);
			return pager;
		}
		query.setOrderBy(" E.LOCKED asc, E.USERNAME asc ");

		int start = pageSize * (pageNo - 1);
		List<SysUser> list = this.getSysUsersByQueryCriteria(start, pageSize, query);
		pager.setResults(list);
		pager.setPageSize(pageSize);
		pager.setCurrentPageNo(pageNo);
		pager.setTotalRecordCount(count);

		return pager;
	}

	/**
	 * 获取全部员工数据集 分页列表
	 * 
	 * @param pageNo   int
	 * @param pageSize int
	 * @param query
	 * @return
	 */
	public PageResult getSysUserList(int pageNo, int pageSize, SysUserQuery query) {
		PageResult pager = new PageResult();

		int count = this.count(query);
		if (count == 0) {// 结果集为空
			pager.setPageSize(pageSize);
			return pager;
		}
		query.setOrderBy(" E.LOCKED asc, E.USERNAME asc ");

		int start = pageSize * (pageNo - 1);
		List<SysUser> list = this.getSysUsersByQueryCriteria(start, pageSize, query);
		pager.setResults(list);
		pager.setPageSize(pageSize);
		pager.setCurrentPageNo(pageNo);
		pager.setTotalRecordCount(count);

		return pager;
	}

	public List<SysUser> getSysUserList(long organizationId) {
		SysUserQuery query = new SysUserQuery();
		query.organizationId(organizationId);
		query.setDeleteFlag(0);
		return this.list(query);
	}

	public PageResult getSysUserList(long organizationId, int pageNo, int pageSize) {
		// 计算总数
		PageResult pager = new PageResult();
		SysUserQuery query = new SysUserQuery();
		query.organizationId(organizationId);
		query.setDeleteFlag(0);

		int count = this.count(query);
		if (count == 0) {// 结果集为空
			pager.setPageSize(pageSize);
			return pager;
		}
		query.setOrderBy(" E.LOCKED asc, E.USERNAME asc ");

		int start = pageSize * (pageNo - 1);
		List<SysUser> list = this.getSysUsersByQueryCriteria(start, pageSize, query);
		pager.setResults(list);
		pager.setPageSize(pageSize);
		pager.setCurrentPageNo(pageNo);
		pager.setTotalRecordCount(count);

		return pager;
	}

	public PageResult getSysUserList(long organizationId, String fullName, int pageNo, int pageSize) {
		// 计算总数
		PageResult pager = new PageResult();
		SysUserQuery query = new SysUserQuery();
		query.organizationId(organizationId);
		query.setDeleteFlag(0);

		if (fullName != null && fullName.trim().length() > 0) {
			query.nameLike(fullName);
		}
		int count = this.count(query);
		if (count == 0) {// 结果集为空
			pager.setPageSize(pageSize);
			return pager;
		}
		query.setOrderBy(" E.LOCKED asc, E.USERNAME asc ");

		int start = pageSize * (pageNo - 1);
		List<SysUser> list = this.getSysUsersByQueryCriteria(start, pageSize, query);
		pager.setResults(list);
		pager.setPageSize(pageSize);
		pager.setCurrentPageNo(pageNo);
		pager.setTotalRecordCount(count);

		return pager;
	}

	public PageResult getSysUserList(long organizationId, String userName, String account, int pageNo, int pageSize) {
		// 计算总数
		PageResult pager = new PageResult();
		SysUserQuery query = new SysUserQuery();
		query.organizationId(organizationId);
		query.setDeleteFlag(0);

		int count = this.count(query);
		if (count == 0) {// 结果集为空
			pager.setPageSize(pageSize);
			return pager;
		}
		query.setOrderBy(" E.LOCKED asc, E.USERNAME asc  ");

		int start = pageSize * (pageNo - 1);
		List<SysUser> list = this.getSysUsersByQueryCriteria(start, pageSize, query);
		pager.setResults(list);
		pager.setPageSize(pageSize);
		pager.setCurrentPageNo(pageNo);
		pager.setTotalRecordCount(count);

		return pager;
	}

	/**
	 * 获取列表
	 * 
	 * @param tenantId
	 * 
	 * @return List
	 */
	public List<SysUser> getSysUserListByTenantId(String tenantId) {
		return sysUserMapper.getSysUsersByTenantId(tenantId);
	}

	public List<SysUser> getSysUserLoginSecretList(SysUserQuery query) {
		return sysUserMapper.getSysUserLoginSecretList(query);
	}

	public List<SysUser> getSysUsersByQueryCriteria(int start, int pageSize, SysUserQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<SysUser> rows = sqlSessionTemplate.selectList("getSysUsers", query, rowBounds);
		return rows;
	}

	/**
	 * 获取某个角色代码的用户
	 * 
	 * @param roleCode
	 * @return
	 */
	public List<SysUser> getSysUsersByRoleCode(String roleCode) {
		if (roleCode == null) {
			return null;
		}
		return sysUserMapper.getSysUsersByRoleCode(roleCode);
	}

	/**
	 * 获取某个角色代码的用户
	 * 
	 * @param roleCode
	 * @return
	 */
	public List<SysUser> getSysUsersByRoleId(String roleId) {
		return sysUserMapper.getSysUsersByRoleId(roleId);
	}

	public List<SysUser> getSysUsersByRoleIds(List<String> roleIds) {
		return sysUserMapper.getSysUsersByRoleIds(roleIds);
	}

	public List<SysUser> getSysUsersExByQueryCriteria(int start, int pageSize, SysUserQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<SysUser> rows = sqlSessionTemplate.selectList("getSysUsersEx", query, rowBounds);
		return rows;
	}

	public List<SysUser> getSysUserWithOrganizationList() {
		SysUserQuery query = new SysUserQuery();
		query.setDeleteFlag(0);
		List<SysUser> users = this.list(query);
		if (users != null && !users.isEmpty()) {
		}
		return users;
	}

	/**
	 * 获取某个用户的首页链接
	 * 
	 * @param actorId
	 * @return
	 */
	public String getUserIndexUrl(String actorId) {
		List<SysRole> list = this.getUserRoles(actorId);
		if (list != null && !list.isEmpty()) {
			Collections.sort(list);
			for (SysRole role : list) {
				if (StringUtils.isNotEmpty(role.getIndexUrl())) {
					return role.getIndexUrl();
				}
			}
		}
		return null;
	}

	/**
	 * 获取某些用户的角色
	 * 
	 * @param actorIds
	 * @return
	 */
	public List<SysRole> getUserRoles(List<String> actorIds) {
		List<SysRole> roles = new ArrayList<SysRole>();
		if (actorIds != null && !actorIds.isEmpty()) {
			for (String actorId : actorIds) {
				List<SysRole> list = this.getUserRoles(actorId);
				if (list != null && !list.isEmpty()) {
					for (SysRole role : list) {
						if (!roles.contains(role)) {
							roles.add(role);
						}
					}
				}
			}
		}
		return roles;
	}

	public List<SysRole> getUserRoles(String actorId) {
		String cacheKey = "cache_userrole_" + actorId;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString(Constants.CACHE_USER_ROLE_REGION, cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONArray jsonArray = JSON.parseArray(text);
					return SysRoleJsonFactory.arrayToList(jsonArray);
				} catch (Exception ex) {
					// Ignore error
				}
			}
		}

		List<SysRole> roles = new ArrayList<SysRole>();
		List<SysRole> list = sysRoleMapper.getSysRolesOfUser(actorId);
		if (list != null && !list.isEmpty()) {
			for (SysRole role : list) {
				if (!roles.contains(role)) {
					roles.add(role);
				}
			}
		}

		if (SystemConfig.getBoolean("use_query_cache")) {
			JSONArray jsonArray = SysRoleJsonFactory.listToArray(roles);
			CacheFactory.put(Constants.CACHE_USER_ROLE_REGION, cacheKey, jsonArray.toJSONString());
		}

		return roles;
	}

	public boolean isPermission(SysUser user, String roleCode) {
		boolean flag = false;

		List<String> actorIds = new ArrayList<String>();
		actorIds.add(user.getActorId());
		List<SysRole> roles = this.getUserRoles(actorIds);
		if (roles != null && !roles.isEmpty()) {
			for (SysRole role : roles) {
				if (role != null && StringUtils.equals(role.getCode(), roleCode)) {
					// 代判断用户是否拥有此角色
					flag = true;
					break;
				}
			}
		}

		return flag;
	}

	public List<SysUser> list(SysUserQuery query) {
		List<SysUser> list = sysUserMapper.getSysUsers(query);
		return list;
	}

	/**
	 * 登录失败
	 * 
	 * @param actorId 登录用户编号
	 * @return
	 */
	@Transactional
	public void loginFailure(String actorId) {
		SysUser user = sysUserMapper.getSysUserByAccount(actorId);
		if (user != null) {
			if (user.getLoginRetry() >= 5) {
				user.setLockLoginTime(new Date());
			}
			user.setLoginRetry(user.getLoginRetry() + 1);
			sysUserMapper.updateUserLoginRetry(user);
		}
	}

	@Transactional
	public boolean register(SysUser bean) {
		bean.setCreateTime(new Date());
		bean.setToken(UUID32.getUUID());
		String pwd_hash = DigestUtils
				.md5Hex(bean.getActorId().toLowerCase() + DigestUtils.sha512Hex(bean.getPasswordHash()));
		bean.setPasswordHash(pwd_hash);
		this.save(bean);
		this.createRoleUser("TenantAdmin", bean.getUserId());
		return true;
	}

	/**
	 * 重置登录信息
	 * 
	 * @param actorId 登录用户编号
	 * @return
	 */
	@Transactional
	public void resetLoginStatus(String actorId) {
		SysUser user = sysUserMapper.getSysUserByAccount(actorId);
		if (user != null) {
			user.setLoginRetry(0);
			user.setLockLoginTime(null);
			user.setLoginSecret(UUID32.getUUID() + UUID32.getUUID() + UUID32.getUUID() + UUID32.getUUID());
			user.setLoginSecretUpdateTime(new Date());
			sysUserMapper.resetLoginStatus(user);
		}
	}

	/**
	 * 重置用户状态
	 * 
	 * @param userIds 用户编号集合
	 * @param locked  状态
	 */
	@Transactional
	public void resetStatus(List<String> userIds, int locked) {
		if (userIds != null && !userIds.isEmpty()) {
			for (String userId : userIds) {
				SysUser user = sysUserMapper.getSysUserByAccount(userId);
				if (user != null) {
					user.setLocked(locked);
					sysUserMapper.updateSysUser(user);
				}
			}
		}
	}

	@Transactional
	public SysUser resetUserToken(String actorId) {
		SysUser sysUser = this.findByAccount(actorId);
		if (sysUser != null) {
			if (SystemConfig.getBoolean("use_query_cache")) {
				String cacheKey = "user_" + sysUser.getActorId();
				CacheFactory.remove(Constants.CACHE_USER_REGION, cacheKey);

				cacheKey = "user_all_" + sysUser.getActorId();
				CacheFactory.remove(Constants.CACHE_USER_REGION, cacheKey);

				cacheKey = "cache_user_" + sysUser.getActorId();
				CacheFactory.remove(Constants.CACHE_USER_REGION, cacheKey);

				cacheKey = "cache_userrole_" + sysUser.getActorId();
				CacheFactory.remove(Constants.CACHE_USER_ROLE_REGION, cacheKey);
			}

			sysUser.setToken(UUID32.getUUID() + UUID32.getUUID() + UUID32.getUUID() + UUID32.getUUID());
			sysUser.setTokenTime(new Date(System.currentTimeMillis()));
			sysUserMapper.resetUserToken(sysUser);

		}
		return sysUser;
	}

	@Transactional
	public void save(SysUser sysUser) {
		if (this.findByAccount(sysUser.getUserId()) == null) {
			sysUser.setCreateTime(new Date());
			sysUser.setToken(UUID32.getUUID() + UUID32.getUUID() + UUID32.getUUID() + UUID32.getUUID());
			sysUser.setTokenTime(new Date(System.currentTimeMillis()));
			sysUser.setSyncFlag(0);
			sysUser.setNamePinyin(PinyinUtils.converterToFirstSpell(sysUser.getName(), true));
			sysUserMapper.insertSysUser(sysUser);
		} else {

			if (SystemConfig.getBoolean("use_query_cache")) {
				String cacheKey = "user_" + sysUser.getActorId();
				CacheFactory.remove(Constants.CACHE_USER_REGION, cacheKey);

				cacheKey = "user_all_" + sysUser.getActorId();
				CacheFactory.remove(Constants.CACHE_USER_REGION, cacheKey);

				cacheKey = "cache_user_" + sysUser.getActorId();
				CacheFactory.remove(Constants.CACHE_USER_REGION, cacheKey);

				cacheKey = "cache_userrole_" + sysUser.getActorId();
				CacheFactory.remove(Constants.CACHE_USER_ROLE_REGION, cacheKey);
			}

			sysUser.setNamePinyin(PinyinUtils.converterToFirstSpell(sysUser.getName(), true));
			sysUser.setUpdateDate(new Date(System.currentTimeMillis()));
			sysUser.setSyncFlag(0);
			sysUser.setSyncTime(null);

			sysUserMapper.updateSysUser(sysUser);

		}

		TableModel table = new TableModel();
		table.setTableName("SYS_MEMBERSHIP");
		table.addStringColumn("ACTORID_", sysUser.getUserId());
		table.addStringColumn("TYPE_", "Superior");
		if (sysUser.getTenantId() != null) {
			table.addStringColumn("TENANTID_", sysUser.getTenantId());
		} else {
			table.addStringColumn("TENANTID_", SysConstants.SYSTEM_TENANT);
		}
		tableDataService.deleteTableData(table);

	}

	/**
	 * 保存角色用户
	 * 
	 * @param roleId
	 * @param userIds
	 */
	@Transactional
	public void saveAddRoleUsers(String tenantId, String roleId, int authorized, List<String> userIds) {

		if (SystemConfig.getBoolean("use_query_cache")) {
			CacheFactory.clear(Constants.CACHE_USER_REGION);
			CacheFactory.clear(Constants.CACHE_USER_ROLE_REGION);
			CacheFactory.clear(Constants.CACHE_USER_ROLE_CODE_REGION);
		}

		for (String userId : userIds) {
			SysUser user = sysUserMapper.getSysUserByAccount(userId);
			if (user != null) {
				SysUserRole userRole = new SysUserRole();
				userRole.setId(idGenerator.getNextId());
				userRole.setUserId(user.getActorId());
				userRole.setRoleId(roleId);
				userRole.setTenantId(tenantId);
				userRole.setAuthorized(authorized);
				userRole.setCreateDate(new Date());
				userRole.setCreateBy(Authentication.getAuthenticatedActorId());
				sysUserRoleMapper.insertSysUserRole(userRole);

				Membership membership = new Membership();
				membership.setActorId(user.getActorId());
				membership.setModifyDate(new java.util.Date());
				membership.setModifyBy(Authentication.getAuthenticatedActorId());
				membership.setRoleId(roleId);
				membership.setNodeId(user.getOrganizationId());
				membership.setObjectId("SysUserRole");
				membership.setObjectValue(roleId);
				membership.setType("SysUserRole");
				membership.setTenantId(tenantId);
				membership.setAttribute(String.valueOf(authorized));
				membershipService.save(membership);
			}
		}

	}

	/**
	 * 保存角色用户
	 * 
	 * @param roleId
	 * @param userIds
	 */
	@Transactional
	public void saveRoleUsers(String tenantId, String roleId, int authorized, List<String> userIds) {

		if (SystemConfig.getBoolean("use_query_cache")) {
			CacheFactory.clear(Constants.CACHE_USER_REGION);
			CacheFactory.clear(Constants.CACHE_USER_ROLE_REGION);
			CacheFactory.clear(Constants.CACHE_USER_ROLE_CODE_REGION);
		}

		TableModel tm = new TableModel();
		tm.setTableName("SYS_USER_ROLE");
		tm.addStringColumn("ROLEID", roleId);
		tm.addStringColumn("TENANTID", tenantId);
		tm.addIntegerColumn("AUTHORIZED", authorized);
		tableDataService.deleteTableData(tm);

		TableModel table = new TableModel();
		table.setTableName("SYS_MEMBERSHIP");
		table.addStringColumn("ROLEID_", roleId);
		table.addStringColumn("TENANTID_", tenantId);
		table.addStringColumn("OBJECTID_", "SysUserRole");
		table.addStringColumn("TYPE_", "SysUserRole");
		table.addStringColumn("ATTRIBUTE_", String.valueOf(authorized));
		tableDataService.deleteTableData(table);

		for (String userId : userIds) {
			SysUser user = sysUserMapper.getSysUserByAccount(userId);
			if (user != null) {
				SysUserRole userRole = new SysUserRole();
				userRole.setId(idGenerator.getNextId());
				userRole.setUserId(user.getActorId());
				userRole.setRoleId(roleId);
				userRole.setTenantId(tenantId);
				userRole.setAuthorized(authorized);
				userRole.setCreateDate(new Date());
				userRole.setCreateBy(Authentication.getAuthenticatedActorId());
				sysUserRoleMapper.insertSysUserRole(userRole);

				Membership membership = new Membership();
				membership.setActorId(user.getActorId());
				membership.setModifyDate(new java.util.Date());
				membership.setModifyBy(Authentication.getAuthenticatedActorId());
				membership.setRoleId(roleId);
				membership.setNodeId(user.getOrganizationId());
				membership.setObjectId("SysUserRole");
				membership.setObjectValue(roleId);
				membership.setType("SysUserRole");
				membership.setTenantId(tenantId);
				membership.setAttribute(String.valueOf(authorized));
				membershipService.save(membership);
			}
		}

	}

	@Resource
	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	@Resource
	public void setMembershipService(MembershipService membershipService) {
		this.membershipService = membershipService;
	}

	@Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@Resource
	public void setSysAccessMapper(SysAccessMapper sysAccessMapper) {
		this.sysAccessMapper = sysAccessMapper;
	}

	@Resource
	public void setSysApplicationMapper(SysApplicationMapper sysApplicationMapper) {
		this.sysApplicationMapper = sysApplicationMapper;
	}

	@Resource
	public void setSysRoleMapper(SysRoleMapper sysRoleMapper) {
		this.sysRoleMapper = sysRoleMapper;
	}

	@Resource
	public void setSysUserMapper(SysUserMapper sysUserMapper) {
		this.sysUserMapper = sysUserMapper;
	}

	@Resource
	public void setSysUserRoleMapper(SysUserRoleMapper sysUserRoleMapper) {
		this.sysUserRoleMapper = sysUserRoleMapper;
	}

	@Resource
	public void setTableDataService(ITableDataService tableDataService) {
		this.tableDataService = tableDataService;
	}

	@Transactional
	public boolean update(SysUser user) {
		if (SystemConfig.getBoolean("use_query_cache")) {
			String cacheKey = "user_" + user.getActorId();
			CacheFactory.remove(Constants.CACHE_USER_REGION, cacheKey);

			cacheKey = "user_all_" + user.getActorId();
			CacheFactory.remove(Constants.CACHE_USER_REGION, cacheKey);

			cacheKey = "cache_user_" + user.getActorId();
			CacheFactory.remove(Constants.CACHE_USER_REGION, cacheKey);

			cacheKey = "cache_userrole_" + user.getActorId();
			CacheFactory.remove(Constants.CACHE_USER_ROLE_REGION, cacheKey);
		}

		user.setLoginRetry(0);
		user.setLockLoginTime(null);
		user.setUpdateDate(new Date());
		user.setSyncFlag(0);
		user.setSyncTime(null);
		user.setNamePinyin(PinyinUtils.converterToFirstSpell(user.getName(), true));
		sysUserMapper.updateSysUser(user);

		TableModel table = new TableModel();
		table.setTableName("SYS_MEMBERSHIP");
		table.addStringColumn("ACTORID_", user.getUserId());
		table.addStringColumn("TYPE_", "Superior");
		if (user.getTenantId() != null) {
			table.addStringColumn("TENANTID_", user.getTenantId());
		} else {
			table.addStringColumn("TENANTID_", SysConstants.SYSTEM_TENANT);
		}
		tableDataService.deleteTableData(table);

		return true;
	}

	@Transactional
	public boolean updateUser(SysUser sysUser) {
		if (SystemConfig.getBoolean("use_query_cache")) {
			String cacheKey = "user_" + sysUser.getActorId();
			CacheFactory.remove(Constants.CACHE_USER_REGION, cacheKey);

			cacheKey = "user_all_" + sysUser.getActorId();
			CacheFactory.remove(Constants.CACHE_USER_REGION, cacheKey);

			cacheKey = "cache_user_" + sysUser.getActorId();
			CacheFactory.remove(Constants.CACHE_USER_REGION, cacheKey);

			cacheKey = "cache_userrole_" + sysUser.getActorId();
			CacheFactory.remove(Constants.CACHE_USER_ROLE_REGION, cacheKey);

		}

		sysUser.setLoginRetry(0);
		sysUser.setLockLoginTime(null);
		sysUserMapper.updateSysUser(sysUser);

		return true;
	}

	/**
	 * 更新用户登录信息
	 * 
	 * @param model
	 */
	@Transactional
	public void updateUserLoginInfo(SysUser user) {
		if (SystemConfig.getBoolean("use_query_cache")) {
			String cacheKey = "user_" + user.getActorId();
			CacheFactory.remove(Constants.CACHE_USER_REGION, cacheKey);

			cacheKey = "user_all_" + user.getActorId();
			CacheFactory.remove(Constants.CACHE_USER_REGION, cacheKey);

			cacheKey = "cache_user_" + user.getActorId();
			CacheFactory.remove(Constants.CACHE_USER_REGION, cacheKey);

			cacheKey = "cache_userrole_" + user.getActorId();
			CacheFactory.remove(Constants.CACHE_USER_ROLE_REGION, cacheKey);
		}

		sysUserMapper.updateSysUserLoginInfo(user);

	}

	/**
	 * 更新登录密锁
	 * 
	 * @param model
	 */
	@Transactional
	public void updateUserLoginSecret(SysUser sysUser) {
		if (SystemConfig.getBoolean("use_query_cache")) {
			String cacheKey = "user_" + sysUser.getActorId();
			CacheFactory.remove(Constants.CACHE_USER_REGION, cacheKey);

			cacheKey = "user_all_" + sysUser.getActorId();
			CacheFactory.remove(Constants.CACHE_USER_REGION, cacheKey);

			cacheKey = "cache_user_" + sysUser.getActorId();
			CacheFactory.remove(Constants.CACHE_USER_REGION, cacheKey);

			cacheKey = "cache_userrole_" + sysUser.getActorId();
			CacheFactory.remove(Constants.CACHE_USER_ROLE_REGION, cacheKey);
		}

		sysUserMapper.updateUserLoginSecret(sysUser);
	}

	@Transactional
	public boolean updateUserRole(SysUser user, int authorized, Set<SysRole> newRoles) {
		if (SystemConfig.getBoolean("use_query_cache")) {
			String cacheKey = "user_" + user.getActorId();
			CacheFactory.remove(Constants.CACHE_USER_REGION, cacheKey);

			cacheKey = "user_all_" + user.getActorId();
			CacheFactory.remove(Constants.CACHE_USER_REGION, cacheKey);

			cacheKey = "cache_user_" + user.getActorId();
			CacheFactory.remove(Constants.CACHE_USER_REGION, cacheKey);

			cacheKey = "cache_userrole_" + user.getActorId();
			CacheFactory.remove(Constants.CACHE_USER_ROLE_REGION, cacheKey);

			CacheFactory.clear(Constants.CACHE_USER_REGION);
			CacheFactory.clear(Constants.CACHE_USER_ROLE_REGION);
			CacheFactory.clear(Constants.CACHE_USER_ROLE_CODE_REGION);
		}

		// 先删除用户之前的权限
		List<SysUserRole> userRoles = sysUserRoleMapper.getSysUserRolesByUserId(user.getActorId());
		if (userRoles != null && !userRoles.isEmpty()) {
			for (SysUserRole userRole : userRoles) {
				TableModel table = new TableModel();
				table.setTableName("SYS_USER_ROLE");
				table.addStringColumn("USERID", user.getActorId());
				table.addStringColumn("ROLEID", userRole.getRoleId());
				if (user.getTenantId() != null) {
					table.addStringColumn("TENANTID", user.getTenantId());
				} else {
					table.addStringColumn("TENANTID", SysConstants.SYSTEM_TENANT);
				}
				table.addIntegerColumn("AUTHORIZED", authorized);
				tableDataService.deleteTableData(table);
			}
		}

		List<Membership> memberships = new ArrayList<Membership>();

		// 增加新权限
		if (newRoles != null && !newRoles.isEmpty()) {
			Iterator<SysRole> iter = newRoles.iterator();
			while (iter.hasNext()) {
				SysRole role = iter.next();
				SysUserRole userRole = new SysUserRole();
				userRole.setId(idGenerator.getNextId());
				userRole.setUserId(user.getActorId());
				userRole.setRoleId(String.valueOf(role.getId()));
				if (user.getTenantId() != null) {
					userRole.setTenantId(user.getTenantId());
				} else {
					userRole.setTenantId(SysConstants.SYSTEM_TENANT);
				}
				userRole.setAuthorized(authorized);
				userRole.setCreateDate(new Date());
				userRole.setCreateBy(user.getUpdateBy());
				sysUserRoleMapper.insertSysUserRole(userRole);

				Membership membership = new Membership();
				membership.setActorId(user.getUserId());
				membership.setModifyBy(user.getUpdateBy());
				membership.setModifyDate(new java.util.Date());
				membership.setNodeId(user.getOrganizationId());
				membership.setRoleId(role.getId());
				membership.setObjectId("SysUserRole");
				membership.setObjectValue(userRole.getId());
				membership.setType("SysUserRole");
				if (user.getTenantId() != null) {
					membership.setTenantId(user.getTenantId());
				} else {
					membership.setTenantId(SysConstants.SYSTEM_TENANT);
				}
				membership.setAttribute(String.valueOf(authorized));
				memberships.add(membership);
			}
		}

		membershipService.saveMemberships(user.getOrganizationId(), "SysUserRole", memberships);

		return true;
	}

}
