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

package com.glaf.base.info.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.glaf.base.info.model.InfoCategory;
import com.glaf.base.info.query.InfoCategoryQuery;

@Transactional(readOnly = true)
public interface InfoCategoryService {

	/**
	 * 根据主键删除记录
	 * 
	 * @return
	 */
	@Transactional
	void deleteById(Long id);

	/**
	 * 根据主键获取一条记录
	 * 
	 * @return
	 */
	InfoCategory getInfoCategory(Long id);

	/**
	 * 根据uuid获取一条记录
	 * 
	 * @return
	 */
	InfoCategory getInfoCategoryByUUID(String uuid);

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	int getInfoCategoryCountByQueryCriteria(InfoCategoryQuery query);

	/**
	 * 获取某个分类的直接子节点列表
	 * 
	 * @param parentId
	 * @return
	 */
	List<InfoCategory> getCategoryList(long parentId);

	/**
	 * 获取某个用户创建的全部分类列表
	 * 
	 * @param tenantId
	 * @return
	 */
	List<InfoCategory> getCategoryList(String tenantId);

	/**
	 * 获取某个用户创建的某个分类的子分类列表
	 * 
	 * @param createBy
	 * @param parentId
	 * @return
	 */
	List<InfoCategory> getCategoryList(String tenantId, long parentId);

	/**
	 * 获取某个用户创建的某类型的子分类列表
	 * 
	 * @param createBy
	 * @param type
	 * @return
	 */
	List<InfoCategory> getCategoryList(String tenantId, String type);

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	List<InfoCategory> getInfoCategorysByQueryCriteria(int start, int pageSize, InfoCategoryQuery query);

	/**
	 * 根据查询参数获取记录列表
	 * 
	 * @return
	 */
	List<InfoCategory> list(InfoCategoryQuery query);

	/**
	 * 保存一条记录
	 * 
	 * @return
	 */
	@Transactional
	void save(InfoCategory infoCategory);

}
