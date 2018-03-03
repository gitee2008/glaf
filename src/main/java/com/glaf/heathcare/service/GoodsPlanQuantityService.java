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

import com.glaf.core.security.LoginContext;
import com.glaf.heathcare.domain.GoodsPlanQuantity;
import com.glaf.heathcare.query.GoodsPlanQuantityQuery;

@Transactional(readOnly = true)
public interface GoodsPlanQuantityService {

	@Transactional
	void bulkInsert(String tenantId, List<GoodsPlanQuantity> list);

	/**
	 * 根据食谱编排形成用量计划
	 * 
	 * @param loginContext
	 * @param year
	 * @param semester
	 * @param week
	 * @param totalPersonQuantity
	 */
	@Transactional
	void createGoodsUsagePlan(LoginContext loginContext, int year, int semester, int week, double totalPersonQuantity);

	/**
	 * 根据食谱编排形成用量计划
	 * 
	 * @param loginContext
	 * @param year
	 * @param semester
	 * @param week
	 * @param fullDay
	 * @param totalPersonQuantity
	 */
	@Transactional
	void createGoodsUsagePlan(LoginContext loginContext, int year, int semester, int week, int fullDay,
			double totalPersonQuantity);

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

	/**
	 * 根据主键获取一条记录
	 * 
	 * @return
	 */
	GoodsPlanQuantity getGoodsPlanQuantity(String tenantId, long id);

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	int getGoodsPlanQuantityCountByQueryCriteria(GoodsPlanQuantityQuery query);

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	List<GoodsPlanQuantity> getGoodsPlanQuantitysByQueryCriteria(int start, int pageSize, GoodsPlanQuantityQuery query);

	/**
	 * 根据查询参数获取记录列表
	 * 
	 * @return
	 */
	List<GoodsPlanQuantity> list(GoodsPlanQuantityQuery query);

	/**
	 * 保存一条记录
	 * 
	 * @return
	 */
	@Transactional
	void save(GoodsPlanQuantity goodsPlanQuantity);

}
