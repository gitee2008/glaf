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
import com.glaf.matrix.data.service.AggregationDefinitionService;

@Service("aggregationDefinitionService")
@Transactional(readOnly = true)
public class AggregationDefinitionServiceImpl implements AggregationDefinitionService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected AggregationDefinitionMapper aggregationDefinitionMapper;

	public AggregationDefinitionServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<AggregationDefinition> list) {
		for (AggregationDefinition aggregationDefinition : list) {
			if (aggregationDefinition.getId() == 0) {
				aggregationDefinition.setId(idGenerator.nextId("SYS_AGGREGATION_DEF"));
			}
		}

		int batch_size = 100;
		List<AggregationDefinition> rows = new ArrayList<AggregationDefinition>(batch_size);

		for (AggregationDefinition bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % 100 == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					aggregationDefinitionMapper.bulkInsertAggregationDefinition_oracle(rows);
				} else {
					aggregationDefinitionMapper.bulkInsertAggregationDefinition(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				aggregationDefinitionMapper.bulkInsertAggregationDefinition_oracle(rows);
			} else {
				aggregationDefinitionMapper.bulkInsertAggregationDefinition(rows);
			}
			rows.clear();
		}
	}

	public int count(AggregationDefinitionQuery query) {
		return aggregationDefinitionMapper.getAggregationDefinitionCount(query);
	}

	@Transactional
	public void deleteById(long id) {
		if (id != 0) {
			aggregationDefinitionMapper.deleteAggregationDefinitionById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				aggregationDefinitionMapper.deleteAggregationDefinitionById(id);
			}
		}
	}

	public AggregationDefinition getAggregationDefinition(long id) {
		if (id == 0) {
			return null;
		}
		AggregationDefinition aggregationDefinition = aggregationDefinitionMapper.getAggregationDefinitionById(id);
		return aggregationDefinition;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getAggregationDefinitionCountByQueryCriteria(AggregationDefinitionQuery query) {
		return aggregationDefinitionMapper.getAggregationDefinitionCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<AggregationDefinition> getAggregationDefinitionsByQueryCriteria(int start, int pageSize,
			AggregationDefinitionQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<AggregationDefinition> rows = sqlSessionTemplate.selectList("getAggregationDefinitions", query, rowBounds);
		return rows;
	}

	public List<AggregationDefinition> list(AggregationDefinitionQuery query) {
		List<AggregationDefinition> list = aggregationDefinitionMapper.getAggregationDefinitions(query);
		return list;
	}

	@Transactional
	public void save(AggregationDefinition aggregationDefinition) {
		if (aggregationDefinition.getId() == 0) {
			aggregationDefinition.setId(idGenerator.nextId("SYS_AGGREGATION_DEF"));
			aggregationDefinition.setCreateTime(new Date());
			aggregationDefinitionMapper.insertAggregationDefinition(aggregationDefinition);
		} else {
			aggregationDefinitionMapper.updateAggregationDefinition(aggregationDefinition);
		}
	}

	@javax.annotation.Resource(name = "com.glaf.matrix.data.mapper.AggregationDefinitionMapper")
	public void setAggregationDefinitionMapper(AggregationDefinitionMapper aggregationDefinitionMapper) {
		this.aggregationDefinitionMapper = aggregationDefinitionMapper;
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

}
