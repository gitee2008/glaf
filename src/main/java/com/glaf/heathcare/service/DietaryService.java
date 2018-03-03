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

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.security.LoginContext;
import com.glaf.heathcare.domain.Dietary;
import com.glaf.heathcare.domain.GoodsPurchasePlan;
import com.glaf.heathcare.query.DietaryQuery;

@Transactional(readOnly = true)
public interface DietaryService {

	@Transactional
	void batchPurchase(String tenantId, Collection<Long> ids, String purchaseFlag, List<GoodsPurchasePlan> list);

	@Transactional
	void bulkInsert(String tenantId, Date date, List<Dietary> list);

	@Transactional
	void calculate(String tenantId, long dietaryId);

	@Transactional
	long copyTemplate(LoginContext loginContext, long templateId);

	@Transactional
	List<Long> copyTemplates(LoginContext loginContext, Date date, int week, List<Long> templateIds);

	/**
	 * 根据主键删除记录
	 * 
	 * @return
	 */
	@Transactional
	void deleteById(String tenantId, long id);

	/**
	 * 根据主键删除多条记录
	 * 
	 * @return
	 */
	@Transactional
	void deleteByIds(String tenantId, List<Long> ids);

	List<Integer> getDays(String tenantId, int year, int semester, int week);

	/**
	 * 根据主键获取一条记录
	 * 
	 * @return
	 */
	Dietary getDietary(String tenantId, long id);

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	int getDietaryCountByQueryCriteria(DietaryQuery query);

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	List<Dietary> getDietarysByQueryCriteria(int start, int pageSize, DietaryQuery query);

	int getMaxWeek(String tenantId, int year, int semester);

	int getNowWeek(String tenantId, int year, int semester);

	int getWeek(String tenantId, int fullDay);

	/**
	 * 根据查询参数获取记录列表
	 * 
	 * @return
	 */
	List<Dietary> list(DietaryQuery query);

	/**
	 * 保存一条记录
	 * 
	 * @return
	 */
	@Transactional
	void save(Dietary dietary);

	@Transactional
	void updateAll(String tenantId, List<Dietary> list);

}
