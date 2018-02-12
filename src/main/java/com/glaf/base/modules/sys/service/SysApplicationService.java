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

import com.glaf.base.modules.sys.model.SysApplication;
import com.glaf.base.modules.sys.query.SysApplicationQuery;
import com.glaf.core.util.PageResult;

@Transactional(readOnly = true)
public interface SysApplicationService {

	@Transactional
	void batchCreate(List<SysApplication> rows);

	/**
	 * 保存
	 * 
	 * @param bean
	 *            SysApplication
	 * @return boolean
	 */
	@Transactional
	boolean create(SysApplication bean);

	/**
	 * 删除
	 * 
	 * @param id
	 *            int
	 * @return boolean
	 */
	@Transactional
	boolean delete(long id);

	/**
	 * 删除
	 * 
	 * @param bean
	 *            SysApplication
	 * @return boolean
	 */
	@Transactional
	boolean delete(SysApplication bean);

	/**
	 * 批量删除
	 * 
	 * @param id
	 * @return
	 */
	@Transactional
	boolean deleteAll(long[] id);

	/**
	 * 按编码查找对象
	 * 
	 * @param code
	 *            String
	 * @return SysApplication
	 */
	SysApplication findByCode(String code);

	/**
	 * 获取对象
	 * 
	 * @param id
	 * @return
	 */
	SysApplication findById(long id);

	/**
	 * 按名称查找对象
	 * 
	 * @param name
	 *            String
	 * @return SysApplication
	 */
	SysApplication findByName(String name);

	List<SysApplication> getAllSysApplications();

	PageResult getApplicationList(int pageNo, int pageSize, SysApplicationQuery query);

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
	PageResult getApplicationList(long parentId, int pageNo, int pageSize);

	/**
	 * 获取某个模块的全部子节点及子孙节点
	 * 
	 * @param parentId
	 *            long
	 * @return List
	 */
	List<SysApplication> getApplicationListWithChildren(long parentId);

	/**
	 * 获取某个用户的全部模块列表
	 * 
	 * @return List
	 */
	List<SysApplication> getSysApplicationByUserId(String actorId);

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	int getSysApplicationCountByQueryCriteria(SysApplicationQuery query);

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	List<SysApplication> getSysApplicationsByQueryCriteria(int start, int pageSize, SysApplicationQuery query);

	List<SysApplication> getSysApplicationsByRoleCode(String roleCode);

	List<SysApplication> getSysApplicationsByRoleId(String roleId);

	/**
	 * 设置删除标记
	 * 
	 * @param id
	 * @param deleteFlag
	 */
	@Transactional
	void markDeleteFlag(long id, int deleteFlag);

	/**
	 * 设置删除标记
	 * 
	 * @param ids
	 * @param deleteFlag
	 */
	@Transactional
	void markDeleteFlag(long[] ids, int deleteFlag);

	@Transactional
	void saveRoleApplications(String roleId, List<Long> appIds);

	/**
	 * 排序
	 * 
	 * @param bean
	 *            SysApplication
	 * @param operate
	 *            int 操作
	 */
	@Transactional
	void sort(long parent, SysApplication bean, int operate);

	/**
	 * 更新
	 * 
	 * @param bean
	 *            SysApplication
	 * @return boolean
	 */
	@Transactional
	boolean update(SysApplication bean);
}