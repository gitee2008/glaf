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
import com.glaf.matrix.combination.domain.CombinationItem;
import com.glaf.matrix.combination.mapper.CombinationItemMapper;
import com.glaf.matrix.combination.query.CombinationItemQuery;

@Service("com.glaf.matrix.combination.service.combinationItemService")
@Transactional(readOnly = true)
public class CombinationItemServiceImpl implements CombinationItemService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected CombinationItemMapper combinationItemMapper;

	public CombinationItemServiceImpl() {

	}

	public int count(CombinationItemQuery query) {
		return combinationItemMapper.getCombinationItemCount(query);
	}

	@Transactional
	public void deleteById(Long id) {
		if (id != null) {
			combinationItemMapper.deleteCombinationItemById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				combinationItemMapper.deleteCombinationItemById(id);
			}
		}
	}

	public CombinationItem getCombinationItem(Long id) {
		if (id == null) {
			return null;
		}
		CombinationItem combinationItem = combinationItemMapper.getCombinationItemById(id);
		return combinationItem;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getCombinationItemCountByQueryCriteria(CombinationItemQuery query) {
		return combinationItemMapper.getCombinationItemCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<CombinationItem> getCombinationItemsByQueryCriteria(int start, int pageSize, CombinationItemQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<CombinationItem> rows = sqlSessionTemplate.selectList("getCombinationItems", query, rowBounds);
		return rows;
	}

	public List<CombinationItem> list(CombinationItemQuery query) {
		List<CombinationItem> list = combinationItemMapper.getCombinationItems(query);
		return list;
	}

	@Transactional
	public void save(CombinationItem combinationItem) {
		if (combinationItem.getId() == 0) {
			combinationItem.setId(idGenerator.nextId("SYS_COMBINATION_ITEM"));
			combinationItem.setCreateTime(new Date());

			combinationItemMapper.insertCombinationItem(combinationItem);
		} else {
			combinationItemMapper.updateCombinationItem(combinationItem);
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

	@javax.annotation.Resource(name = "com.glaf.matrix.combination.mapper.CombinationItemMapper")
	public void setCombinationItemMapper(CombinationItemMapper combinationItemMapper) {
		this.combinationItemMapper = combinationItemMapper;
	}

}
