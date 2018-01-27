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

package com.glaf.matrix.combination.service;

import java.util.*;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.matrix.combination.domain.*;
import com.glaf.matrix.combination.query.*;

@Transactional(readOnly = true)
public interface TreeTableAggregateService {

	/**
	 * 根据主键删除记录
	 * 
	 * @return
	 */
	@Transactional
	void deleteById(long id);

	@Transactional
	void deleteRuleById(long id);

	/**
	 * 根据主键获取一条记录
	 * 
	 * @return
	 */
	TreeTableAggregate getTreeTableAggregate(long id);

	/**
	 * 根据目标表获取一条记录
	 * 
	 * @return
	 */
	TreeTableAggregate getTreeTableAggregateByTargetTableName(String tableName);

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	int getTreeTableAggregateCountByQueryCriteria(TreeTableAggregateQuery query);

	TreeTableAggregateRule getTreeTableAggregateRule(long ruleId);

	List<TreeTableAggregateRule> getTreeTableAggregateRulesByTableName(String tableName);

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	List<TreeTableAggregate> getTreeTableAggregatesByQueryCriteria(int start, int pageSize,
			TreeTableAggregateQuery query);

	/**
	 * 根据查询参数获取记录列表
	 * 
	 * @return
	 */
	List<TreeTableAggregate> list(TreeTableAggregateQuery query);

	@Transactional
	void resetAllTreeTableAggregateStatus();

	/**
	 * 保存一条记录
	 * 
	 * @return
	 */
	@Transactional
	void save(TreeTableAggregate treeTableAggregate);

	/**
	 * 另存一条记录
	 * 
	 * @return
	 */
	@Transactional
	void saveAs(TreeTableAggregate treeTableAggregate);

	@Transactional
	void saveRule(TreeTableAggregateRule rule);

	/**
	 * 保存同步规则
	 * 
	 * @param aggregateId
	 * @param rules
	 */
	@Transactional
	void saveRules(long aggregateId, List<TreeTableAggregateRule> rules);

	@Transactional
	void updateTreeTableAggregateStatus(TreeTableAggregate model);

}
