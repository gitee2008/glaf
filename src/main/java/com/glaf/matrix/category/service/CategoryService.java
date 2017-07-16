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

package com.glaf.matrix.category.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.glaf.matrix.category.domain.CategoryAccess;
import com.glaf.matrix.category.domain.Category;
import com.glaf.matrix.category.query.CategoryQuery;

@Transactional(readOnly = true)
public interface CategoryService {

	/**
	 * 新增分类的人员授权
	 * 
	 * @param categoryId
	 * @param actorId
	 */
	@Transactional
	void createAccessor(long categoryId, String actorId);

	/**
	 * 删除分类的人员授权
	 * 
	 * @param categoryId
	 * @param actorId
	 */
	@Transactional
	void deleteAccessor(long categoryId, String actorId);

	/**
	 * 根据主键删除分类信息
	 * 
	 * @return
	 */
	@Transactional
	void deleteById(Long id);

	/**
	 * 根据主键删除多条记录
	 * 
	 * @return
	 */
	@Transactional
	void deleteByIds(List<Long> ids);

	/**
	 * 获取全部分类的授权信息
	 * 
	 * @return
	 */
	List<CategoryAccess> getAllCategoryAccesses();

	/**
	 * 根据主键获取一个分类信息
	 * 
	 * @return
	 */
	Category getCategory(Long id);

	/**
	 * 根据code获取一个分类信息
	 * 
	 * @return
	 */
	Category getCategoryByCode(String code);

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	int getCategoryCountByQueryCriteria(CategoryQuery query);

	/**
	 * 某个用户能看到的分类信息
	 * 
	 * @param actorId
	 * @return
	 */
	List<Category> getCategories(String actorId);

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	List<Category> getCategoriesByQueryCriteria(int start, int pageSize, CategoryQuery query);

	/**
	 * 根据查询参数获取记录列表
	 * 
	 * @return
	 */
	List<Category> list(CategoryQuery query);

	/**
	 * 保存一条分类信息
	 * 
	 * @return
	 */
	@Transactional
	void save(Category category);

	/**
	 * 保存分类的人员信息
	 * 
	 * @param categoryId
	 * @param accessors
	 */
	@Transactional
	void saveAccessors(long categoryId, Collection<String> accessors);

	/**
	 * 保存人员的分类信息
	 * 
	 * @param accessor
	 * @param categoryIds
	 */
	@Transactional
	void saveAccessors(String accessor, Collection<Long> categoryIds);

	/**
	 * 保存一条分类信息
	 * 
	 * @return
	 */
	@Transactional
	void update(Category category);

	/**
	 * 更新用户分类视图
	 * 
	 * @param userId
	 */
	@Transactional
	void updateCategoryView(String userId);

	/**
	 * 更新节点的treeId
	 * 
	 * @param treeIdMap
	 */
	@Transactional
	void updateTreeIds(Map<Long, String> treeIdMap);

}
