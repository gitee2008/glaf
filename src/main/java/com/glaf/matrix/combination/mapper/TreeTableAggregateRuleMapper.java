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

package com.glaf.matrix.combination.mapper;

import java.util.List;

import org.springframework.stereotype.Component;
import com.glaf.matrix.combination.domain.*;

/**
 * 
 * Mapper接口
 *
 */

@Component
public interface TreeTableAggregateRuleMapper {

	void bulkInsertTreeTableAggregateRule(List<TreeTableAggregateRule> list);

	void bulkInsertTreeTableAggregateRule_oracle(List<TreeTableAggregateRule> list);

	void deleteTreeTableAggregateRuleById(Long id);

	void deleteTreeTableAggregateRulesByAggregateId(long aggregateId);

	TreeTableAggregateRule getTreeTableAggregateRuleById(Long id);

	List<TreeTableAggregateRule> getTreeTableAggregateRulesByAggregateId(long aggregateId);

	List<TreeTableAggregateRule> getTreeTableAggregateRulesByTableName(String tableName);
	
	void insertTreeTableAggregateRule(TreeTableAggregateRule model);

	void updateTreeTableAggregateRule(TreeTableAggregateRule model);

}
