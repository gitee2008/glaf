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
public interface GoodsActualQuantityService {

	@Transactional
	void bulkInsert(String tenantId, List<GoodsActualQuantity> list);

	/**
	 * 复制某天的出库到用量表
	 * 
	 * @param tenantId
	 * @param fullDay
	 */
	@Transactional
	void copyOutStock(String tenantId, int fullDay, String userId, boolean includePlan);

	/**
	 * 复制某天的采购到用量表
	 * 
	 * @param tenantId
	 * @param fullDay
	 */
	@Transactional
	void copyPurchase(String tenantId, int fullDay, String userId, boolean includePlan);

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
	GoodsActualQuantity getGoodsActualQuantity(String tenantId, long id);

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	int getGoodsActualQuantityCountByQueryCriteria(GoodsActualQuantityQuery query);

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	List<GoodsActualQuantity> getGoodsActualQuantitysByQueryCriteria(int start, int pageSize,
			GoodsActualQuantityQuery query);

	/**
	 * 根据查询参数获取记录列表
	 * 
	 * @return
	 */
	List<GoodsActualQuantity> list(GoodsActualQuantityQuery query);

	/**
	 * 保存一条记录
	 * 
	 * @return
	 */
	@Transactional
	void save(GoodsActualQuantity goodsActualQuantity);

	@Transactional
	void saveAll(List<GoodsActualQuantity> list);

	@Transactional
	void updateGoodsActualQuantityStatus(GoodsActualQuantity goodsActualQuantity);

	@Transactional
	void updateGoodsActualQuantityStatus(List<GoodsActualQuantity> list);
}
