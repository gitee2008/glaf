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

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;
import com.glaf.matrix.combination.domain.TreeTableAggregate;
import com.glaf.matrix.combination.domain.TreeTableAggregateRule;
import com.glaf.matrix.combination.mapper.TreeTableAggregateMapper;
import com.glaf.matrix.combination.mapper.TreeTableAggregateRuleMapper;
import com.glaf.matrix.combination.query.TreeTableAggregateQuery;
import com.glaf.matrix.combination.service.TreeTableAggregateService;

@Service("treeTableAggregateService")
@Transactional(readOnly = true)
public class TreeTableAggregateServiceImpl implements TreeTableAggregateService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected TreeTableAggregateMapper treeTableAggregateMapper;

	protected TreeTableAggregateRuleMapper treeTableAggregateRuleMapper;

	public TreeTableAggregateServiceImpl() {

	}

	public int count(TreeTableAggregateQuery query) {
		return treeTableAggregateMapper.getTreeTableAggregateCount(query);
	}

	@Transactional
	public void deleteById(long id) {
		if (id != 0) {
			treeTableAggregateRuleMapper.deleteTreeTableAggregateRulesByAggregateId(id);
			treeTableAggregateMapper.deleteTreeTableAggregateById(id);
		}
	}

	@Transactional
	public void deleteRuleById(long id) {
		treeTableAggregateRuleMapper.deleteTreeTableAggregateRuleById(id);
	}

	public TreeTableAggregate getTreeTableAggregate(long id) {
		if (id == 0) {
			return null;
		}
		TreeTableAggregate treeTableAggregate = treeTableAggregateMapper.getTreeTableAggregateById(id);
		if (treeTableAggregate != null) {
			List<TreeTableAggregateRule> rules = treeTableAggregateRuleMapper
					.getTreeTableAggregateRulesByAggregateId(id);
			treeTableAggregate.setRules(rules);
		}
		return treeTableAggregate;
	}

	/**
	 * 根据目标表获取一条记录
	 * 
	 * @return
	 */
	public TreeTableAggregate getTreeTableAggregateByTargetTableName(String tableName) {
		TreeTableAggregateQuery query = new TreeTableAggregateQuery();
		query.setTargetTableName(tableName);
		List<TreeTableAggregate> list = treeTableAggregateMapper.getTreeTableAggregates(query);
		if (list != null && !list.isEmpty()) {
			TreeTableAggregate treeTableAggregate = list.get(0);
			if (treeTableAggregate != null) {
				List<TreeTableAggregateRule> rules = treeTableAggregateRuleMapper
						.getTreeTableAggregateRulesByAggregateId(treeTableAggregate.getId());
				treeTableAggregate.setRules(rules);
			}
			return treeTableAggregate;
		}
		return null;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getTreeTableAggregateCountByQueryCriteria(TreeTableAggregateQuery query) {
		return treeTableAggregateMapper.getTreeTableAggregateCount(query);
	}

	public TreeTableAggregateRule getTreeTableAggregateRule(long ruleId) {
		return treeTableAggregateRuleMapper.getTreeTableAggregateRuleById(ruleId);
	}

	public List<TreeTableAggregateRule> getTreeTableAggregateRulesByTableName(String tableName) {
		return treeTableAggregateRuleMapper.getTreeTableAggregateRulesByTableName(tableName);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<TreeTableAggregate> getTreeTableAggregatesByQueryCriteria(int start, int pageSize,
			TreeTableAggregateQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<TreeTableAggregate> rows = sqlSessionTemplate.selectList("getTreeTableAggregates", query, rowBounds);
		return rows;
	}

	public List<TreeTableAggregate> list(TreeTableAggregateQuery query) {
		List<TreeTableAggregate> list = treeTableAggregateMapper.getTreeTableAggregates(query);
		return list;
	}

	@Transactional
	public void resetAllTreeTableAggregateStatus() {
		treeTableAggregateMapper.resetAllTreeTableAggregateStatus();
	}

	@Transactional
	public void save(TreeTableAggregate treeTableAggregate) {
		if (treeTableAggregate.getId() == null) {
			treeTableAggregate.setId(idGenerator.nextId("SYS_TREETABLE_AGGREGATE"));
			treeTableAggregate.setCreateTime(new Date());
			if (StringUtils.isEmpty(treeTableAggregate.getTargetTableName())) {
				treeTableAggregate.setTargetTableName("TREE_TABLE_AGGR" + treeTableAggregate.getId());
			}
			treeTableAggregateMapper.insertTreeTableAggregate(treeTableAggregate);
		} else {
			treeTableAggregate.setUpdateTime(new Date());
			treeTableAggregateMapper.updateTreeTableAggregate(treeTableAggregate);
		}
	}

	/**
	 * 另存一条记录
	 * 
	 * @return
	 */
	@Transactional
	public void saveAs(TreeTableAggregate treeTableAggregate) {
		long oldId = treeTableAggregate.getId();
		treeTableAggregate.setId(idGenerator.nextId("SYS_TREETABLE_AGGREGATE"));
		treeTableAggregate.setCreateTime(new Date());
		treeTableAggregate.setTargetTableName("TREE_TABLE_AGGR" + treeTableAggregate.getId());

		treeTableAggregateMapper.insertTreeTableAggregate(treeTableAggregate);

		List<TreeTableAggregateRule> rules = treeTableAggregateRuleMapper
				.getTreeTableAggregateRulesByAggregateId(oldId);
		if (rules != null && !rules.isEmpty()) {
			for (TreeTableAggregateRule rule : rules) {
				rule.setId(idGenerator.nextId());
				rule.setAggregateId(treeTableAggregate.getId());
				if (StringUtils.isEmpty(rule.getTargetColumnName())) {
					rule.setTargetColumnName("AGGR_" + treeTableAggregate.getId() + "_COL"
							+ idGenerator.nextId("TREETABLE_COL_" + rule.getAggregateId()));
				}
				treeTableAggregateRuleMapper.insertTreeTableAggregateRule(rule);
			}
		}
	}

	@Transactional
	public void saveRule(TreeTableAggregateRule rule) {
		if (rule.getId() == 0) {
			rule.setId(idGenerator.nextId());
			if (StringUtils.isEmpty(rule.getTargetColumnName())) {
				TreeTableAggregate bean = this.getTreeTableAggregate(rule.getAggregateId());
				rule.setTargetColumnName(
						"AGGR_" + bean.getId() + "_COL" + idGenerator.nextId("TREETABLE_COL_" + rule.getAggregateId()));
			}
			treeTableAggregateRuleMapper.insertTreeTableAggregateRule(rule);
		} else {
			treeTableAggregateRuleMapper.updateTreeTableAggregateRule(rule);
		}
	}

	/**
	 * 保存同步规则
	 * 
	 * @param aggregateId
	 * @param rules
	 */
	@Transactional
	public void saveRules(long aggregateId, List<TreeTableAggregateRule> rules) {
		if (rules != null && !rules.isEmpty()) {
			treeTableAggregateRuleMapper.deleteTreeTableAggregateRulesByAggregateId(aggregateId);
			for (TreeTableAggregateRule rule : rules) {
				rule.setId(idGenerator.nextId());
				rule.setAggregateId(aggregateId);
				if (StringUtils.isEmpty(rule.getTargetColumnName())) {
					TreeTableAggregate bean = this.getTreeTableAggregate(rule.getAggregateId());
					rule.setTargetColumnName("AGGR_" + bean.getId() + "_COL"
							+ idGenerator.nextId("TREETABLE_COL_" + rule.getAggregateId()));
				}
				treeTableAggregateRuleMapper.insertTreeTableAggregateRule(rule);
			}
		}
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource
	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	@javax.annotation.Resource
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@javax.annotation.Resource
	public void setTreeTableAggregateMapper(TreeTableAggregateMapper treeTableAggregateMapper) {
		this.treeTableAggregateMapper = treeTableAggregateMapper;
	}

	@javax.annotation.Resource
	public void setTreeTableAggregateRuleMapper(TreeTableAggregateRuleMapper treeTableAggregateRuleMapper) {
		this.treeTableAggregateRuleMapper = treeTableAggregateRuleMapper;
	}

	@Transactional
	public void updateTreeTableAggregateStatus(TreeTableAggregate model) {
		model.setSyncTime(new java.util.Date());
		treeTableAggregateMapper.updateTreeTableAggregateStatus(model);
	}

}
