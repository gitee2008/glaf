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

package com.glaf.base.modules.sys.service;

import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.glaf.base.modules.sys.model.SysRole;
import com.glaf.base.modules.sys.model.SysUser;
import com.glaf.base.modules.sys.model.UserRole;
import com.glaf.base.modules.sys.query.SysUserQuery;
import com.glaf.base.modules.sys.query.UserRoleQuery;
import com.glaf.core.util.PageResult;

@Transactional(readOnly = true)
public interface SysUserService {

	/**
	 * 修改用户密码
	 * 
	 * @param account
	 * @param password
	 */
	void changePassword(String account, String password);

	/**
	 * 检查用户登录密锁是否正确
	 * 
	 * @param account
	 * @param loginSecret
	 * @return
	 */
	boolean checkLoginSecret(String account, String loginSecret);

	/**
	 * 检查用户密码是否正确
	 * 
	 * @param account
	 * @param password
	 * @return
	 */
	boolean checkPassword(String account, String password);

	/**
	 * 保存
	 * 
	 * @param bean
	 *            SysUser
	 * @return boolean
	 */
	@Transactional
	boolean create(SysUser bean);

	@Transactional
	void createRoleUser(String roleId, String actorId);

	/**
	 * 删除
	 * 
	 * @param bean
	 *            SysUser
	 * @return boolean
	 */
	@Transactional
	boolean delete(SysUser bean);

	@Transactional
	void deleteRoleUser(String roleId, String actorId);

	/**
	 * 删除部门角色用户
	 * 
	 * @param organizationRole
	 * @param userIds
	 */
	@Transactional
	void deleteRoleUsers(SysRole role, String[] userIds);

	/**
	 * 按名称查找对象
	 * 
	 * @param name
	 *            String
	 * @return SysUser
	 */
	SysUser findByAccount(String account);

	/**
	 * 按名称查找对象
	 * 
	 * @param name
	 *            String
	 * @return SysUser
	 */
	SysUser findByAccountWithAll(String account);

	/**
	 * 按名称查找对象
	 * 
	 * @param actorId
	 *            String
	 * @return SysUser
	 */
	SysUser findById(String actorId);

	/**
	 * 按邮箱查找对象
	 * 
	 * @param mail
	 *            String
	 * @return SysUser
	 */
	SysUser findByMail(String mail);

	/**
	 * 按手机查找对象
	 * 
	 * @param mobile
	 *            String
	 * @return SysUser
	 */
	SysUser findByMobile(String mobile);

	/**
	 * 根据条件获取全部用户
	 * 
	 * @param query
	 * @return
	 */
	List<SysUser> getAllUsers(SysUserQuery query);

	List<UserRole> getRoleUserViews(UserRoleQuery query);

	/**
	 * 查找供应商用户 flag = true 表示该用户存在，否则为不存在
	 * 
	 * @param supplierNo
	 * @return
	 */
	List<SysUser> getSupplierUser(String supplierNo);

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	int getSysUserCountByQueryCriteria(SysUserQuery query);

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	int getSysUserExCountByQueryCriteria(SysUserQuery query);

	/**
	 * 获取列表
	 * 
	 * @param userType
	 *            int
	 * @return List
	 */
	List<SysUser> getSysUserList(int userType);

	/**
	 * 获取全部员工数据集 分页列表
	 * 
	 * @param pageNo
	 *            int
	 * @param pageSize
	 *            int
	 * @return
	 */
	PageResult getSysUserList(int pageNo, int pageSize);

	/**
	 * 获取全部员工数据集 分页列表
	 * 
	 * @param pageNo
	 *            int
	 * @param pageSize
	 *            int
	 * @param query
	 * @return
	 */
	PageResult getSysUserList(int pageNo, int pageSize, SysUserQuery query);

	/**
	 * 获取列表
	 * 
	 * @param organizationId
	 *            int
	 * @return List
	 */
	List<SysUser> getSysUserList(long organizationId);

	/**
	 * 获取特定部门的员工数据集 分页列表
	 * 
	 * @param organizationId
	 *            int
	 * @param pageNo
	 *            int
	 * @param pageSize
	 *            int
	 * @return
	 */
	PageResult getSysUserList(long organizationId, int pageNo, int pageSize);

	/**
	 * 查询获取sysUser列表
	 * 
	 * @param organizationId
	 * @param fullName
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageResult getSysUserList(long organizationId, String fullName, int pageNo, int pageSize);

	/**
	 * 获取列表
	 * 
	 */
	PageResult getSysUserList(long organizationId, String userName, String account, int pageNo, int pageSize);

	/**
	 * 获取列表
	 * 
	 * @param tenantId
	 * 
	 * @return List
	 */
	List<SysUser> getSysUserListByTenantId(String tenantId);

	List<SysUser> getSysUserLoginSecretList(SysUserQuery query);

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	List<SysUser> getSysUsersByQueryCriteria(int start, int pageSize, SysUserQuery query);

	/**
	 * 获取某个角色代码的用户
	 * 
	 * @param roleCode
	 * @return
	 */
	List<SysUser> getSysUsersByRoleCode(String roleCode);

	/**
	 * 获取某个角色编号的用户
	 * 
	 * @param roleId
	 * @return
	 */
	List<SysUser> getSysUsersByRoleId(String roleId);

	/**
	 * 获取多个角色编号的用户
	 * 
	 * @param roleIds
	 * @return
	 */
	List<SysUser> getSysUsersByRoleIds(List<String> roleIds);

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	List<SysUser> getSysUsersExByQueryCriteria(int start, int pageSize, SysUserQuery query);

	/**
	 * 获取列表
	 * 
	 * @param organizationId
	 *            int
	 * @return List
	 */
	List<SysUser> getSysUserWithOrganizationList();

	/**
	 * 获取某个用户的首页链接
	 * 
	 * @param actorId
	 * @return
	 */
	String getUserIndexUrl(String actorId);

	/**
	 * 获取某些用户的角色
	 * 
	 * @param actorIds
	 * @return
	 */
	List<SysRole> getUserRoles(List<String> actorIds);

	/**
	 * 获取某个用户的角色
	 * 
	 * @param actorId
	 * @return
	 */
	List<SysRole> getUserRoles(String actorId);

	/**
	 * 判断用户是否有权限
	 * 
	 * @param user
	 * @param code
	 * @return
	 */
	boolean isPermission(SysUser user, String roleCode);

	/**
	 * 登录失败
	 * 
	 * @param actorId
	 *            登录用户编号
	 * @return
	 */
	@Transactional
	void loginFailure(String actorId);

	@Transactional
	boolean register(SysUser bean);

	/**
	 * 重置登录信息
	 * 
	 * @param actorId
	 *            登录用户编号
	 * @return
	 */
	@Transactional
	void resetLoginStatus(String actorId);

	/**
	 * 重置用户状态
	 * 
	 * @param userIds
	 *            用户编号集合
	 * @param status
	 *            状态
	 */
	@Transactional
	void resetStatus(List<String> userIds, int status);

	/**
	 * 重置用户令牌及密锁
	 * 
	 * @param actorId
	 * @return
	 */
	@Transactional
	SysUser resetUserToken(String actorId);

	/**
	 * 保存角色用户
	 * 
	 * @param tenantId
	 * @param roleId
	 * @param userIds
	 */
	@Transactional
	void saveAddRoleUsers(String tenantId, String roleId, int authorized, List<String> userIds);

	/**
	 * 保存角色用户
	 * 
	 * @param tenantId
	 * @param roleId
	 * @param userIds
	 */
	@Transactional
	void saveRoleUsers(String tenantId, String roleId, int authorized, List<String> userIds);

	/**
	 * 更新
	 * 
	 * @param bean
	 *            SysUser
	 * @return boolean
	 */
	@Transactional
	boolean update(SysUser bean);

	/**
	 * 修改用户信息
	 * 
	 * @param bean
	 * @return
	 */
	@Transactional
	boolean updateUser(SysUser bean);

	/**
	 * 更新用户登录信息
	 * 
	 * @param model
	 */
	@Transactional
	void updateUserLoginInfo(SysUser model);

	/**
	 * 更新登录密锁
	 * 
	 * @param model
	 */
	@Transactional
	void updateUserLoginSecret(SysUser model);

	/**
	 * 设置用户角色
	 * 
	 * @param user
	 *            用户
	 * @param newRoles
	 *            角色集合
	 * @return
	 */
	@Transactional
	boolean updateUserRole(SysUser user, int authorized, Set<SysRole> newRoles);

}