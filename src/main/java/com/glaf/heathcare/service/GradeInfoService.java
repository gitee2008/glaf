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

package com.glaf.heathcare.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.glaf.heathcare.domain.GradeInfo;
import com.glaf.heathcare.domain.GradePrivilege;
import com.glaf.heathcare.query.GradeInfoQuery;

@Transactional(readOnly = true)
public interface GradeInfoService {

	@Transactional
	void bulkInsert(List<GradeInfo> list);

	@Transactional
	String createTeacher(String createBy, GradeInfo gradeInfo);

	/**
	 * 根据主键删除记录
	 * 
	 * @return
	 */
	@Transactional
	void deleteById(String id);

	/**
	 * 根据主键删除多条记录
	 * 
	 * @return
	 */
	@Transactional
	void deleteByIds(List<String> ids);

	/**
	 * 根据主键获取一条记录
	 * 
	 * @return
	 */
	GradeInfo getGradeInfo(String id);

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	int getGradeInfoCountByQueryCriteria(GradeInfoQuery query);

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	List<GradeInfo> getGradeInfosByQueryCriteria(int start, int pageSize, GradeInfoQuery query);

	/**
	 * 根据查询参数获取记录列表
	 * 
	 * @return
	 */
	List<GradeInfo> getGradeInfosByTenantId(String tenantId);

	List<GradePrivilege> getGradePrivileges(String gradeId, String tenantId);

	List<String> getGradeUserIds(String gradeId, String tenantId, String privilege);

	List<String> getPrivileges(String gradeId, String tenantId, String userId);
	
	
	/**
	 * 根据查询参数获取记录列表
	 * 
	 * @return
	 */
	List<GradeInfo> list(GradeInfoQuery query);

	/**
	 * 保存一条记录
	 * 
	 * @return
	 */
	@Transactional
	void save(GradeInfo gradeInfo);

	@Transactional
	void saveGradeUsers(String gradeId, String tenantId, String privilege, List<String> actorIds);

}
