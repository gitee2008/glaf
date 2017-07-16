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

import org.springframework.transaction.annotation.Transactional;

import com.glaf.base.modules.sys.model.SysOrganization;
import com.glaf.base.modules.sys.query.SysOrganizationQuery;
import com.glaf.core.util.PageResult;

@Transactional(readOnly = true)
public interface SysOrganizationService {

	/**
	 * 保存
	 * 
	 * @param bean
	 *            SysOrganization
	 * @return boolean
	 */
	@Transactional
	boolean create(SysOrganization bean);

	/**
	 * 删除
	 * 
	 * @param organizationId
	 *            int
	 * @return boolean
	 */
	@Transactional
	boolean delete(long organizationId);

	/**
	 * 删除
	 * 
	 * @param bean
	 *            SysOrganization
	 * @return boolean
	 */
	@Transactional
	boolean delete(SysOrganization bean);

	/**
	 * 批量删除
	 * 
	 * @param organizationIds
	 * @return
	 */
	@Transactional
	boolean deleteAll(long[] organizationIds);

	/**
	 * 按编码查找对象
	 * 
	 * @param code
	 * 
	 * @return SysOrganization
	 */
	SysOrganization findByCode(String code);

	/**
	 * 获取对象
	 * 
	 * @param organizationId
	 * @return
	 */
	SysOrganization findById(long organizationId);

	/**
	 * 按名称查找对象
	 * 
	 * @param name
	 *            String
	 * @return SysOrganization
	 */
	SysOrganization findByName(String name);

	/**
	 * 按部门编号查找对象
	 * 
	 * @param organizationno
	 * 
	 * @return SysOrganization
	 */

	SysOrganization findByNo(String organizationno);

	/**
	 * 获取某个部门及上级部门
	 * 
	 * @param organizationId
	 * @return
	 */
	SysOrganization getSysOrganization(long organizationId);

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	int getSysOrganizationCountByQueryCriteria(SysOrganizationQuery query);

	/**
	 * 获取列表
	 * 
	 * @return List
	 */
	List<SysOrganization> getSysOrganizationList();
	
	/**
	 * 获取列表
	 * @param tenantId
	 * @return List
	 */
	List<SysOrganization> getSysOrganizationList(String tenantId);

	/**
	 * 获取分页列表
	 * 
	 * @param parent
	 *            int
	 * @param pageNo
	 *            int
	 * @param pageSize
	 *            int
	 * @return
	 */
	PageResult getSysOrganizationList(int pageNo, int pageSize, SysOrganizationQuery query);

	/**
	 * 获取列表
	 * 
	 * @param parentId
	 *            long
	 * @return List
	 */
	List<SysOrganization> getSysOrganizationList(long parentId);

	/**
	 * 获取分页列表
	 * 
	 * @param parentId
	 *            int
	 * @param pageNo
	 *            int
	 * @param pageSize
	 *            int
	 * @return
	 */
	PageResult getSysOrganizationList(long parentId, int pageNo, int pageSize);

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	List<SysOrganization> getSysOrganizationsByQueryCriteria(int start, int pageSize, SysOrganizationQuery query);

	/**
	 * 获取某个部门及全部上级部门
	 * 
	 * @param organizationId
	 * @return
	 */
	SysOrganization getSysOrganizationWithAncestor(long organizationId);

	/**
	 * 获取列表
	 * 
	 * @param parentId
	 *            long
	 * @return List
	 */
	List<SysOrganization> getSysOrganizationWithChildren(long parentId);

	/**
	 * 获取某个租户的顶级机构
	 * 
	 * @param tenantId
	 * @return
	 */
	SysOrganization getTopOrganizationByTenantId(String tenantId);

	/**
	 * 获取部门列表信息
	 * 
	 * @param query
	 * @return
	 */
	List<SysOrganization> list(SysOrganizationQuery query);

	/**
	 * 设置删除标记
	 * 
	 * @param id
	 * @param deleteFlag
	 */
	@Transactional
	void markDeleteFlag(long organizationId, int deleteFlag);

	/**
	 * 设置删除标记
	 * 
	 * @param organizationIds
	 * @param deleteFlag
	 */
	@Transactional
	void markDeleteFlag(long[] organizationIds, int deleteFlag);

	/**
	 * 排序
	 * 
	 * @param bean
	 *            SysOrganization
	 * @param operate
	 *            int 操作
	 */
	@Transactional
	void sort(long parentId, SysOrganization bean, int operate);

	/**
	 * 更新
	 * 
	 * @param bean
	 *            SysOrganization
	 * @return boolean
	 */
	@Transactional
	boolean update(SysOrganization bean);
}