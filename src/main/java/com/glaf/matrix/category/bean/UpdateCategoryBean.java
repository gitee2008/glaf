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

package com.glaf.matrix.category.bean;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

import com.glaf.matrix.category.domain.Category;
import com.glaf.matrix.category.query.CategoryQuery;
import com.glaf.matrix.category.service.CategoryService;
import com.glaf.core.context.ContextFactory;

public class UpdateCategoryBean {

	protected CategoryService categoryService;

	public CategoryService getCategoryService() {
		if (categoryService == null) {
			categoryService = ContextFactory.getBean("categoryService");
		}
		return categoryService;
	}

	protected String getTreeId(Map<Long, Category> dataMap, Category tree) {
		long parentId = tree.getParentId();
		long id = tree.getId();
		Category parent = dataMap.get(parentId);
		if (parent != null && parent.getId() != 0) {
			if (StringUtils.isEmpty(parent.getTreeId())) {
				return getTreeId(dataMap, parent) + id + "|";
			}
			if (!parent.getTreeId().endsWith("|")) {
				parent.setTreeId(parent.getTreeId() + "|");
			}
			if (parent.getTreeId() != null) {
				return parent.getTreeId() + id + "|";
			}
		}
		return tree.getTreeId();
	}

	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}
	
	/**
	 * 更新用户分类视图
	 * @param userId
	 */
	public void updateCategoryView(String userId){
		getCategoryService().updateCategoryView(userId);
	}

	public void updateTreeIds() {
		CategoryQuery query = new CategoryQuery();
		query.setParentId(0L);
		List<Category> topCategories = getCategoryService().list(query);
		if (topCategories != null && !topCategories.isEmpty()) {
			Map<Long, String> treeIdMap = new HashMap<Long, String>();
			for (Category root : topCategories) {
				if (StringUtils.isEmpty(root.getTreeId())) {
					treeIdMap.put(root.getId(), root.getId() + "|");
				}
			}
			if (!treeIdMap.isEmpty()) {
				getCategoryService().updateTreeIds(treeIdMap);
			}
			query = new CategoryQuery();
			List<Category> trees = getCategoryService().list(query);
			if (trees != null && !trees.isEmpty()) {
				Map<Long, Category> dataMap = new HashMap<Long, Category>();
				for (Category tree : trees) {
					dataMap.put(tree.getId(), tree);
				}
				treeIdMap.clear();
				for (Category tree : trees) {
					if (StringUtils.isEmpty(tree.getTreeId())) {
						String treeId = this.getTreeId(dataMap, tree);
						if (treeId != null && treeId.endsWith("|")) {
							treeIdMap.put(tree.getId(), treeId);
						}
					}
				}
				getCategoryService().updateTreeIds(treeIdMap);
			}
		}
	}

}
