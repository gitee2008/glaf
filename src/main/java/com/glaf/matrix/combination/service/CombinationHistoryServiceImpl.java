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

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;
import com.glaf.matrix.combination.domain.CombinationHistory;
import com.glaf.matrix.combination.mapper.CombinationHistoryMapper;
import com.glaf.matrix.combination.query.CombinationHistoryQuery;

@Service("com.glaf.matrix.combination.service.combinationHistoryService")
@Transactional(readOnly = true)
public class CombinationHistoryServiceImpl implements CombinationHistoryService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected CombinationHistoryMapper combinationHistoryMapper;

	public CombinationHistoryServiceImpl() {

	}

	public int count(CombinationHistoryQuery query) {
		return combinationHistoryMapper.getCombinationHistoryCount(query);
	}

	@Transactional
	public void deleteById(Long id) {
		if (id != null) {
			combinationHistoryMapper.deleteCombinationHistoryById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				combinationHistoryMapper.deleteCombinationHistoryById(id);
			}
		}
	}

	public CombinationHistory getCombinationHistory(Long id) {
		if (id == null) {
			return null;
		}
		CombinationHistory combinationHistory = combinationHistoryMapper.getCombinationHistoryById(id);
		return combinationHistory;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getCombinationHistoryCountByQueryCriteria(CombinationHistoryQuery query) {
		return combinationHistoryMapper.getCombinationHistoryCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<CombinationHistory> getCombinationHistorysByQueryCriteria(int start, int pageSize,
			CombinationHistoryQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<CombinationHistory> rows = sqlSessionTemplate.selectList("getCombinationHistorys", query, rowBounds);
		return rows;
	}

	public CombinationHistory getLatestCombinationHistory(long syncId, long databaseId) {
		CombinationHistoryQuery query = new CombinationHistoryQuery();
		query.syncId(syncId);
		query.databaseId(databaseId);
		List<CombinationHistory> list = combinationHistoryMapper.getCombinationHistorys(query);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	public List<CombinationHistory> list(CombinationHistoryQuery query) {
		List<CombinationHistory> list = combinationHistoryMapper.getCombinationHistorys(query);
		return list;
	}

	@Transactional
	public void save(CombinationHistory combinationHistory) {
		if (combinationHistory.getId() == 0) {
			combinationHistory.setId(idGenerator.nextId("SYS_COMBINATION_HISTORY"));
			combinationHistory.setCreateTime(new Date());

			combinationHistoryMapper.insertCombinationHistory(combinationHistory);
		} else {
			combinationHistoryMapper.updateCombinationHistory(combinationHistory);
		}
	}

	@javax.annotation.Resource(name = "com.glaf.matrix.combination.mapper.CombinationHistoryMapper")
	public void setCombinationHistoryMapper(CombinationHistoryMapper combinationHistoryMapper) {
		this.combinationHistoryMapper = combinationHistoryMapper;
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
