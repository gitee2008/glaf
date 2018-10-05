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

package com.glaf.core.tree.helper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.core.base.TreeModel;
import com.glaf.core.tree.component.TreeComponent;
import com.glaf.core.tree.component.TreeRepository;

public class TreeRepositoryBuilder {

	protected static Log logger = LogFactory.getLog(TreeRepositoryBuilder.class);

	public TreeRepository build(List<TreeModel> treeModels) {
		Map<String, TreeModel> treeMap = new LinkedHashMap<String, TreeModel>();
		Map<String, TreeModel> lockedMap = new LinkedHashMap<String, TreeModel>();

		for (int i = 0, len = treeModels.size(); i < len; i++) {
			TreeModel treeModel = (TreeModel) treeModels.get(i);
			if (treeModel != null && treeModel.getId() == treeModel.getParentId()) {
				treeModel.setParentId(-1);
			}
			if (treeModel != null && treeModel.getId() > 0) {
				treeMap.put(String.valueOf(treeModel.getId()), treeModel);
			}
			if (treeModel != null && treeModel.getLocked() != 0) {
				/**
				 * 记录已经禁用的节点
				 */
				lockedMap.put(String.valueOf(treeModel.getId()), treeModel);
			}
		}

		for (int i = 0, len = treeModels.size(); i < len / 2; i++) {
			for (int j = 0, len2 = treeModels.size(); j < len2; j++) {
				TreeModel tree = treeModels.get(j);
				/**
				 * 找到某个节点的父节点，如果被禁用，那么当前节点也设置为禁用
				 */
				if (lockedMap.get(String.valueOf(tree.getParentId())) != null) {
					tree.setLocked(1);
				}
				TreeModel parent = treeMap.get(String.valueOf(tree.getParentId()));
				tree.setParent(parent);
			}
		}

		TreeRepository repository = new TreeRepository();
		for (int i = 0, len = treeModels.size(); i < len; i++) {
			TreeModel treeModel = treeModels.get(i);
			if (treeModel == null) {
				continue;
			}
			if (treeModel.getLocked() != 0) {
				continue;
			}
			if (treeModel.getParent() != null && treeModel.getParent().getLocked() != 0) {
				continue;
			}
			TreeComponent component = new TreeComponent();
			component.setId(String.valueOf(treeModel.getId()));
			component.setCode(String.valueOf(treeModel.getId()));
			component.setTitle(treeModel.getName());
			component.setChecked(treeModel.isChecked());
			component.setTreeObject(treeModel);
			component.setImage(treeModel.getIcon());
			component.setTreeModel(treeModel);
			component.setDescription(treeModel.getDescription());
			component.setLocation(treeModel.getUrl());
			component.setUrl(treeModel.getUrl());
			component.setTreeId(treeModel.getTreeId());
			component.setCls(treeModel.getIconCls());
			component.setLevel(treeModel.getLevel());
			component.setSortNo(treeModel.getSortNo());
			component.setDataMap(treeModel.getDataMap());
			repository.addTree(component);
			// logger.debug("add tree: " + component.getTitle());
		}

		for (int i = 0, len = treeModels.size(); i < len; i++) {
			TreeModel treeModel = treeModels.get(i);
			if (treeModel == null) {
				continue;
			}
			if (treeModel.getLocked() != 0) {
				continue;
			}
			if (treeModel.getParent() != null && treeModel.getParent().getLocked() != 0) {
				continue;
			}

			TreeComponent component = repository.getTree(String.valueOf(treeModel.getId()));
			String parentId = String.valueOf(treeModel.getParentId());
			if (treeMap.get(parentId) != null) {
				TreeComponent parentTree = repository.getTree(String.valueOf(parentId));
				if (parentTree == null) {
					TreeModel parent = treeMap.get(parentId);
					parentTree = new TreeComponent();
					parentTree.setId(String.valueOf(parent.getId()));
					parentTree.setCode(String.valueOf(parent.getId()));
					parentTree.setTitle(parent.getName());
					parentTree.setChecked(parent.isChecked());
					parentTree.setTreeObject(parent);
					parentTree.setImage(parent.getIcon());
					parentTree.setTreeModel(parent);
					parentTree.setDescription(parent.getDescription());
					parentTree.setLocation(parent.getUrl());
					parentTree.setUrl(parent.getUrl());
					parentTree.setTreeId(parent.getTreeId());
					parentTree.setCls(parent.getIconCls());
					parentTree.setLevel(parent.getLevel());
					parentTree.setSortNo(parent.getSortNo());
					parentTree.setDataMap(parent.getDataMap());
					// repository.addTree(parentTree);
				}
				component.setParent(parentTree);
			}

		}
		return repository;
	}

	protected TreeRepository buildM(List<TreeModel> treeModels) {
		// Collections.sort(treeModels);
		List<TreeModel> nodes = new java.util.ArrayList<TreeModel>();
		Map<String, TreeModel> treeMap = new LinkedHashMap<String, TreeModel>();
		Map<String, TreeModel> lockedMap = new LinkedHashMap<String, TreeModel>();

		for (int i = 0, len = treeModels.size(); i < len; i++) {
			TreeModel treeModel = (TreeModel) treeModels.get(i);
			if (treeModel.getId() == treeModel.getParentId()) {
				treeModel.setParentId(-1);
			}
			treeMap.put(String.valueOf(treeModel.getId()), treeModel);
			if (treeModel.getLocked() == 0) {
				nodes.add(treeModel);
			} else {
				/**
				 * 记录已经禁用的节点
				 */
				lockedMap.put(String.valueOf(treeModel.getId()), treeModel);
			}
		}

		for (int i = 0, len = nodes.size(); i < len / 2; i++) {
			for (int j = 0, len2 = nodes.size(); j < len2; j++) {
				TreeModel tree = nodes.get(j);
				/**
				 * 找到某个节点的父节点，如果被禁用，那么当前节点也设置为禁用
				 */
				if (lockedMap.get(String.valueOf(tree.getParentId())) != null) {
					tree.setLocked(1);
				}
				TreeModel parent = treeMap.get(String.valueOf(tree.getParentId()));
				tree.setParent(parent);
			}
		}

		TreeRepository repository = new TreeRepository();
		for (int i = 0, len = nodes.size(); i < len; i++) {
			TreeModel treeModel = nodes.get(i);
			if (treeModel.getLocked() != 0) {
				continue;
			}
			if (treeModel.getParent() != null && treeModel.getParent().getLocked() != 0) {
				continue;
			}

			TreeComponent component = new TreeComponent();
			component.setId(String.valueOf(treeModel.getId()));
			if (treeModel.getCode() != null) {
				component.setCode(treeModel.getCode());
			} else {
				component.setCode(String.valueOf(treeModel.getId()));
			}
			component.setTitle(treeModel.getName());
			component.setChecked(treeModel.isChecked());
			component.setTreeObject(treeModel);
			component.setImage(treeModel.getIcon());
			component.setTreeModel(treeModel);
			component.setDescription(treeModel.getDescription());
			component.setLocation(treeModel.getUrl());
			component.setUrl(treeModel.getUrl());
			component.setTreeId(treeModel.getTreeId());
			component.setCls(treeModel.getIconCls());
			component.setLevel(treeModel.getLevel());
			component.setDataMap(treeModel.getDataMap());

			String parentId = String.valueOf(treeModel.getParentId());
			if (StringUtils.isNotEmpty(parentId) && treeMap.get(parentId) != null) {
				TreeComponent parentTree = repository.getTree(parentId);
				if (parentTree == null) {
					TreeModel parent = treeMap.get(parentId);
					parentTree = new TreeComponent();
					parentTree.setId(String.valueOf(parent.getId()));
					parentTree.setCode(parent.getCode());
					parentTree.setTitle(parent.getName());
					parentTree.setChecked(parent.isChecked());
					parentTree.setTreeModel(parent);
					parentTree.setTreeObject(parent);
					parentTree.setDescription(parent.getDescription());
					parentTree.setLocation(parent.getUrl());
					parentTree.setUrl(parent.getUrl());
					parentTree.setTreeId(parent.getTreeId());
					parentTree.setCls(parent.getIconCls());
					parentTree.setLevel(parent.getLevel());
					parentTree.setDataMap(parent.getDataMap());
					repository.addTree(parentTree);
				}
				component.setParent(parentTree);
			}
			if (!repository.getTreeIds().contains(component.getId())) {
				repository.addTree(component);
			}
		}
		return repository;
	}

	public TreeRepository buildMenu(List<TreeModel> treeModels) {
		Map<String, TreeComponent> treeMap = new LinkedHashMap<String, TreeComponent>();
		List<TreeComponent> treeComponents = new ArrayList<TreeComponent>();
		for (int i = 0, len = treeModels.size(); i < len; i++) {
			TreeModel tree = (TreeModel) treeModels.get(i);
			TreeComponent component = new TreeComponent();
			component.setId(String.valueOf(tree.getId()));
			component.setTitle(tree.getName());
			component.setChecked(tree.isChecked());
			component.setCls(tree.getIconCls());
			component.setDescription(tree.getDescription());
			component.setImage(tree.getIcon());
			component.setUrl(tree.getUrl());
			component.setTreeId(tree.getTreeId());
			component.setLevel(tree.getLevel());
			component.setDataMap(tree.getDataMap());
			treeMap.put(component.getId(), component);
			if (tree.getParentId() > 0) {
				TreeComponent parent = treeMap.get(String.valueOf(tree.getParentId()));
				if (parent != null) {
					component.setParent(parent);
				}
			}
			treeComponents.add(component);
		}

		for (int i = 0, len = treeModels.size(); i < len; i++) {
			TreeModel tree = (TreeModel) treeModels.get(i);
			TreeComponent component = treeMap.get(String.valueOf(tree.getId()));
			TreeComponent parent = treeMap.get(String.valueOf(tree.getParentId()));
			if (parent != null) {
				component.setParent(parent);
			}
		}

		return this.buildTree(treeComponents);
	}

	public TreeRepository buildMyTree(List<TreeComponent> treeModels) {
		// Collections.sort(treeModels);
		List<TreeComponent> components = new java.util.ArrayList<TreeComponent>();
		Map<String, TreeComponent> treesMap = new java.util.HashMap<String, TreeComponent>();

		for (int i = 0, len = treeModels.size(); i < len; i++) {
			TreeComponent treeModel = (TreeComponent) treeModels.get(i);
			if (treeModel != null) {
				if (StringUtils.equals(treeModel.getId(), treeModel.getParentId())) {
					// 2018-09-19增加逻辑，如果父节点编号与当前节点编号一样，设置父节点为空，即为顶层节点。
					treeModel.setParentId(null);
					// treeModel.setParent(null);
				}
				treesMap.put(treeModel.getId().trim(), treeModel);
				components.add(treeModel);
			}
		}

		for (int k = 0, k_len = components.size() / 2; k < k_len; k++) {
			for (int i = 0, len = components.size(); i < len; i++) {
				TreeComponent treeModel = components.get(i);
				if (StringUtils.isNotEmpty(treeModel.getParentId())) {
					TreeComponent parent = treesMap.get(treeModel.getParentId().trim());
					if (parent != null) {
						parent.addChild(treeModel);
						treeModel.setParent(parent);
					}
				}
			}
		}

		TreeRepository repository = new TreeRepository();

		for (int i = 0, len = components.size(); i < len; i++) {
			TreeComponent treeModel = components.get(i);
			repository.addTree(treeModel);
		}

		for (int i = 0, len = components.size(); i < len; i++) {
			TreeComponent component = components.get(i);
			if (StringUtils.isNotEmpty(component.getParentId())) {
				String parentId = component.getParentId();
				if (treesMap.get(parentId.trim()) != null) {
					TreeComponent parentTree = repository.getTree(parentId);
					if (parentTree == null) {
						parentTree = treesMap.get(parentId.trim());
						TreeComponent child = repository.getTree(component.getId().trim());
						child.setParent(parentTree);
						child.setParentId(parentId);
						parentTree.addChild(child);
						repository.addTree(parentTree);
					}
				}
			}
		}

		return repository;
	}

	public TreeRepository buildTree(List<TreeComponent> treeModels) {
		Map<String, TreeComponent> treeMap = new LinkedHashMap<String, TreeComponent>();
		Map<String, TreeComponent> lockedMap = new LinkedHashMap<String, TreeComponent>();

		for (int i = 0, len = treeModels.size(); i < len; i++) {
			TreeComponent treeModel = (TreeComponent) treeModels.get(i);
			if (StringUtils.equals(treeModel.getId(), treeModel.getParentId())) {
				treeModel.setParent(null);
			}
			if (treeModel.getId() != null) {
				treeMap.put(treeModel.getId(), treeModel);
			}
			if (treeModel.getLocked() != 0) {
				/**
				 * 记录已经禁用的节点
				 */
				lockedMap.put(treeModel.getId(), treeModel);
			}
		}

		for (int i = 0, len = treeModels.size(); i < len / 2; i++) {
			for (int j = 0, len2 = treeModels.size(); j < len2; j++) {
				TreeComponent tree = treeModels.get(j);
				/**
				 * 找到某个节点的父节点，如果被禁用，那么当前节点也设置为禁用
				 */
				if (tree.getParent() != null && lockedMap.get(tree.getParentId()) != null) {
					tree.setLocked(1);
				}
				TreeComponent parent = treeMap.get(tree.getParentId());
				tree.setParent(parent);
			}
		}

		TreeRepository repository = new TreeRepository();
		for (int i = 0, len = treeModels.size(); i < len; i++) {
			TreeComponent treeModel = treeModels.get(i);
			if (treeModel == null) {
				continue;
			}
			if (treeModel.getLocked() != 0) {
				continue;
			}
			if (treeModel.getParent() != null && treeModel.getParent().getLocked() != 0) {
				continue;
			}
			TreeComponent component = new TreeComponent();
			component.setId(String.valueOf(treeModel.getId()));
			component.setCode(String.valueOf(treeModel.getId()));
			component.setTitle(treeModel.getTitle());
			component.setChecked(treeModel.isChecked());
			component.setTreeObject(treeModel);
			component.setImage(treeModel.getImage());
			component.setDescription(treeModel.getDescription());
			component.setLocation(treeModel.getUrl());
			component.setUrl(treeModel.getUrl());
			component.setTreeId(treeModel.getTreeId());
			component.setCls(treeModel.getCls());
			component.setLevel(treeModel.getLevel());
			component.setDataMap(treeModel.getDataMap());
			repository.addTree(component);
			// logger.debug("add tree: " + component.getTitle());
		}

		for (int i = 0, len = treeModels.size(); i < len; i++) {
			TreeComponent treeModel = treeModels.get(i);
			if (treeModel == null) {
				continue;
			}
			if (treeModel.getLocked() != 0) {
				continue;
			}
			if (treeModel.getParent() != null && treeModel.getParent().getLocked() != 0) {
				continue;
			}

			TreeComponent component = repository.getTree(String.valueOf(treeModel.getId()));
			String parentId = treeModel.getParentId();
			if (treeMap.get(parentId) != null) {
				TreeComponent parentTree = repository.getTree(parentId);
				if (parentTree == null) {
					TreeComponent parent = treeMap.get(parentId);
					parentTree = new TreeComponent();
					parentTree.setId(parent.getId());
					parentTree.setCode(parent.getId());
					parentTree.setTitle(parent.getTitle());
					parentTree.setChecked(parent.isChecked());
					parentTree.setTreeObject(parent);
					parentTree.setImage(parent.getImage());
					parentTree.setDescription(parent.getDescription());
					parentTree.setLocation(parent.getUrl());
					parentTree.setUrl(parent.getUrl());
					parentTree.setTreeId(parent.getTreeId());
					parentTree.setCls(parent.getCls());
					parentTree.setLevel(parent.getLevel());
					parentTree.setDataMap(parent.getDataMap());
					// repository.addTree(parentTree);
				}
				component.setParent(parentTree);
			}
		}
		return repository;
	}

	protected TreeRepository buildTreeM(List<TreeComponent> treeModels) {
		// Collections.sort(treeModels);
		List<TreeComponent> nodes = new java.util.ArrayList<TreeComponent>();
		Map<String, TreeComponent> treeMap = new LinkedHashMap<String, TreeComponent>();

		for (int i = 0, len = treeModels.size(); i < len; i++) {
			TreeComponent treeModel = (TreeComponent) treeModels.get(i);
			if (treeModel != null) {
				if (treeModel.getParent() != null
						&& StringUtils.equals(treeModel.getId(), treeModel.getParent().getId())) {
					treeModel.setParent(null);
				}
				treeMap.put(treeModel.getId(), treeModel);
				nodes.add(treeModel);
			}
		}

		TreeRepository repository = new TreeRepository();
		for (int i = 0, len = nodes.size(); i < len; i++) {
			TreeComponent treeModel = nodes.get(i);

			TreeComponent component = new TreeComponent();
			component.setId(String.valueOf(treeModel.getId()));
			if (treeModel.getCode() != null) {
				component.setCode(treeModel.getCode());
			} else {
				component.setCode(String.valueOf(treeModel.getId()));
			}
			component.setTitle(treeModel.getTitle());
			component.setChecked(treeModel.isChecked());
			component.setTreeObject(treeModel);
			component.setImage(treeModel.getImage());
			component.setDescription(treeModel.getDescription());
			component.setLocation(treeModel.getUrl());
			component.setUrl(treeModel.getUrl());
			component.setTreeId(treeModel.getTreeId());
			component.setCls(treeModel.getCls());
			component.setLevel(treeModel.getLevel());
			component.setDataMap(treeModel.getDataMap());

			if (treeModel.getParent() != null) {
				String parentId = String.valueOf(treeModel.getParent().getId());
				if (StringUtils.isNotEmpty(parentId) && treeMap.get(parentId) != null) {
					TreeComponent parentTree = repository.getTree(parentId);
					if (parentTree == null) {
						TreeComponent parent = treeMap.get(parentId);
						parentTree = new TreeComponent();
						parentTree.setId(String.valueOf(parent.getId()));
						parentTree.setCode(parent.getCode());
						parentTree.setTitle(parent.getTitle());
						parentTree.setChecked(parent.isChecked());
						parentTree.setTreeObject(parent);
						parentTree.setDescription(parent.getDescription());
						parentTree.setLocation(parent.getUrl());
						parentTree.setUrl(parent.getUrl());
						parentTree.setTreeId(parent.getTreeId());
						parentTree.setCls(parent.getCls());
						parentTree.setLevel(parent.getLevel());
						parentTree.setDataMap(parent.getDataMap());
						repository.addTree(parentTree);
					}
					component.setParent(parentTree);
				}
			}
			if (!repository.getTreeIds().contains(component.getId())) {
				repository.addTree(component);
			}
		}
		return repository;
	}

}