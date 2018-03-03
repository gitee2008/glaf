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
public interface PersonService {

	@Transactional
	void bulkInsert(List<Person> list);

	int getGradePersonCount(String gradeId);

	/**
	 * 根据主键获取一条记录
	 * 
	 * @return
	 */
	Person getPerson(String id);

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	int getPersonCountByQueryCriteria(PersonQuery query);

	/**
	 * 根据查询参数获取记录列表
	 * 
	 * @return
	 */
	List<Person> getPersons(String gradeId);

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	List<Person> getPersonsByQueryCriteria(int start, int pageSize, PersonQuery query);

	/**
	 * 根据查询参数获取记录列表
	 * 
	 * @return
	 */
	List<Person> getTenantPersons(String tenantId);

	/**
	 * 根据查询参数获取记录列表
	 * 
	 * @return
	 */
	List<Person> list(PersonQuery query);

	/**
	 * 保存一条记录
	 * 
	 * @return
	 */
	@Transactional
	void save(Person person);

	/**
	 * 保存一条记录
	 * 
	 * @return
	 */
	@Transactional
	void update(Person person);

}
