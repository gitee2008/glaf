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

package com.glaf.matrix.combination.tree;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.matrix.util.Constants;

public class TreeNodeSumBuilder {

	protected static Log logger = LogFactory.getLog(TreeNodeSumBuilder.class);

	public void sum(List<TreeNode> treeNodes) {
		if (treeNodes == null || treeNodes.size() > Constants.MAX_TREE_NODE) {
			return;
		}
		Map<Long, TreeNode> treeNodeMap = new ConcurrentHashMap<Long, TreeNode>();
		Map<Long, TreeNode> lockedMap = new ConcurrentHashMap<Long, TreeNode>();

		int len = treeNodes.size();
		for (int i = 0; i < len; i++) {
			TreeNode treeNode = treeNodes.get(i);
			if (treeNode.getId() == treeNode.getParentId()) {
				treeNode.setParent(null);
				treeNode.setParentId(-1);
			}
			if (treeNode.getId() > 0) {
				treeNodeMap.put(treeNode.getId(), treeNode);
			}
			if (treeNode.getLocked() != 0) {
				/**
				 * 记录已经禁用的节点
				 */
				lockedMap.put(treeNode.getId(), treeNode);
			}
		}

		logger.debug("treeNodeMap size: " + treeNodeMap.size());

		for (int i = 0; i < len; i++) {
			TreeNode treeNode = treeNodes.get(i);
			TreeNode parent = treeNodeMap.get(treeNode.getParentId());
			if (parent != null) {
				parent.addChild(treeNode);
				treeNode.setParent(parent);
			}
			/**
			 * 找到某个节点的父节点，如果被禁用，那么当前节点也设置为禁用
			 */
			if (parent != null && lockedMap.get(treeNode.getParentId()) != null) {
				treeNode.setLocked(1);
			}
		}

		ForkJoinPool forkJoinPool = new ForkJoinPool();
		for (int i = 0; i < len; i++) {
			TreeNode treeNode = treeNodes.get(i);
			if (treeNode.getParent() == null) {// 从根节点开始计算
				logger.debug("开始计算根节点:" + treeNode.toString());
				// this.sum(treeNode);
				TreeNodeSumTask task = new TreeNodeSumTask(treeNode);
				forkJoinPool.submit(task);// 利用多核线程池并行计算
			}
		}

		try {
			forkJoinPool.awaitTermination(200, TimeUnit.MILLISECONDS);
		} catch (InterruptedException ex) {
		}
		forkJoinPool.shutdown();
	}

}