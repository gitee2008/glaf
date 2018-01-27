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

package com.glaf.matrix.data.service.impl;

import java.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.id.*;
import com.glaf.core.dao.*;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.util.*;

import com.glaf.matrix.data.mapper.*;
import com.glaf.matrix.data.domain.*;
import com.glaf.matrix.data.query.*;
import com.glaf.matrix.data.service.SyntheticFlowService;

@Service("syntheticFlowService")
@Transactional(readOnly = true)
public class SyntheticFlowServiceImpl implements SyntheticFlowService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected SyntheticFlowMapper syntheticFlowMapper;

	public SyntheticFlowServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<SyntheticFlow> list) {
		for (SyntheticFlow syntheticFlow : list) {
			if (syntheticFlow.getId() == null) {
				syntheticFlow.setId(idGenerator.nextId("SYS_SYNTHETIC_FLOW"));
			}
		}
		if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
			syntheticFlowMapper.bulkInsertSyntheticFlow_oracle(list);
		} else {
			syntheticFlowMapper.bulkInsertSyntheticFlow(list);
		}
	}

	public int count(SyntheticFlowQuery query) {
		return syntheticFlowMapper.getSyntheticFlowCount(query);
	}

	@Transactional
	public void deleteById(Long id) {
		if (id != null) {
			syntheticFlowMapper.deleteSyntheticFlowById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				syntheticFlowMapper.deleteSyntheticFlowById(id);
			}
		}
	}

	public SyntheticFlow getSyntheticFlow(Long id) {
		if (id == null) {
			return null;
		}
		SyntheticFlow syntheticFlow = syntheticFlowMapper.getSyntheticFlowById(id);
		return syntheticFlow;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getSyntheticFlowCountByQueryCriteria(SyntheticFlowQuery query) {
		return syntheticFlowMapper.getSyntheticFlowCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<SyntheticFlow> getSyntheticFlowsByQueryCriteria(int start, int pageSize, SyntheticFlowQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<SyntheticFlow> rows = sqlSessionTemplate.selectList("getSyntheticFlows", query, rowBounds);
		return rows;
	}

	public List<SyntheticFlow> list(SyntheticFlowQuery query) {
		List<SyntheticFlow> list = syntheticFlowMapper.getSyntheticFlows(query);
		return list;
	}

	/**
	 * 保存记录
	 * 
	 * @return
	 */
	@Transactional
	public void saveAll(String currentStep, String currentType, List<SyntheticFlow> rows) {
		SyntheticFlowQuery query = new SyntheticFlowQuery();
		query.currentStep(currentStep);
		query.currentType(currentType);
		List<SyntheticFlow> list = this.list(query);
		if (list != null && !list.isEmpty()) {
			for (SyntheticFlow flow : list) {
				this.deleteById(flow.getId());
			}
		}
		if (rows != null && !rows.isEmpty()) {
			for (SyntheticFlow flow : rows) {
				flow.setId(idGenerator.nextId("SYS_SYNTHETIC_FLOW"));
				flow.setCreateTime(new Date());
				flow.setCurrentStep(currentStep);
				flow.setCurrentType(currentType);
				syntheticFlowMapper.insertSyntheticFlow(flow);
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
	public void setSyntheticFlowMapper(SyntheticFlowMapper syntheticFlowMapper) {
		this.syntheticFlowMapper = syntheticFlowMapper;
	}

}
