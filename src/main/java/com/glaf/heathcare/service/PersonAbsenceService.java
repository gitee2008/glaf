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

import java.util.*;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.heathcare.domain.*;
import com.glaf.heathcare.query.*;

@Transactional(readOnly = true)
public interface PersonAbsenceService {

	@Transactional
	void bulkInsert(String tenantId, List<PersonAbsence> list);

	/**
	 * 根据主键删除记录
	 * 
	 * @return
	 */
	@Transactional
	void deleteById(String tenantId, String id);

	/**
	 * 根据主键删除多条记录
	 * 
	 * @return
	 */
	@Transactional
	void deleteByIds(String tenantId, List<String> ids);

	/**
	 * 获取某个租户某年有缺席的日期
	 * 
	 * @param tenantId
	 * @param gradeId
	 * @param year
	 * @return
	 */
	List<Integer> getAbsenceDays(String tenantId, String gradeId, int year);

	/**
	 * 根据主键获取一条记录
	 * 
	 * @return
	 */
	PersonAbsence getPersonAbsence(String tenantId, String id);

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	int getPersonAbsenceCountByQueryCriteria(PersonAbsenceQuery query);

	/**
	 * 获取某个班级某天某个时段的缺勤记录
	 * 
	 * @param tenantId
	 *            租户编号
	 * @param gradeId
	 *            班级编号
	 * @param day
	 *            某天
	 * @param section
	 *            某时段
	 * @return
	 */
	List<PersonAbsence> getPersonAbsences(String tenantId, String gradeId, int day, String section);

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	List<PersonAbsence> getPersonAbsencesByQueryCriteria(int start, int pageSize, PersonAbsenceQuery query);

	/**
	 * 根据查询参数获取记录列表
	 * 
	 * @return
	 */
	List<PersonAbsence> list(PersonAbsenceQuery query);

	@Transactional
	void saveAll(String tenantId, String gradeId, int day, String section, String createBy, List<PersonAbsence> personAbsences);

}
