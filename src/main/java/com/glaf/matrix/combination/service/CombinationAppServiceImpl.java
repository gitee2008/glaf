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
import com.glaf.core.util.UUID32;
import com.glaf.matrix.combination.domain.CombinationApp;
import com.glaf.matrix.combination.domain.CombinationItem;
import com.glaf.matrix.combination.mapper.CombinationAppMapper;
import com.glaf.matrix.combination.mapper.CombinationItemMapper;
import com.glaf.matrix.combination.query.CombinationAppQuery;
import com.glaf.matrix.combination.query.CombinationItemQuery;

@Service("com.glaf.matrix.combination.service.combinationAppService")
@Transactional(readOnly = true)
public class CombinationAppServiceImpl implements CombinationAppService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected CombinationAppMapper combinationAppMapper;

	protected CombinationItemMapper combinationItemMapper;

	public CombinationAppServiceImpl() {

	}

	public int count(CombinationAppQuery query) {
		return combinationAppMapper.getCombinationAppCount(query);
	}

	@Transactional
	public void deleteById(Long id) {
		if (id != null) {
			combinationAppMapper.deleteCombinationAppById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				combinationAppMapper.deleteCombinationAppById(id);
			}
		}
	}

	public CombinationApp getCombinationApp(Long syncId) {
		if (syncId == null) {
			return null;
		}
		CombinationApp combinationApp = combinationAppMapper.getCombinationAppById(syncId);
		if (combinationApp != null) {
			CombinationItemQuery query = new CombinationItemQuery();
			query.syncId(syncId);
			List<CombinationItem> items = combinationItemMapper.getCombinationItems(query);
			combinationApp.setItems(items);
		}
		return combinationApp;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getCombinationAppCountByQueryCriteria(CombinationAppQuery query) {
		return combinationAppMapper.getCombinationAppCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<CombinationApp> getCombinationAppsByQueryCriteria(int start, int pageSize, CombinationAppQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<CombinationApp> rows = sqlSessionTemplate.selectList("getCombinationApps", query, rowBounds);
		return rows;
	}

	public List<CombinationApp> list(CombinationAppQuery query) {
		List<CombinationApp> list = combinationAppMapper.getCombinationApps(query);
		return list;
	}

	@Transactional
	public void save(CombinationApp combinationApp) {
		if (combinationApp.getId() == 0) {
			combinationApp.setId(idGenerator.nextId("SYS_COMBINATION_APP"));
			combinationApp.setCreateTime(new Date());
			if (StringUtils.isEmpty(combinationApp.getDeploymentId())) {
				combinationApp.setDeploymentId(UUID32.getUUID());
			}
			combinationAppMapper.insertCombinationApp(combinationApp);
		} else {
			combinationAppMapper.updateCombinationApp(combinationApp);
		}
	}

	@Transactional
	public long saveAs(long syncId, String createBy) {
		CombinationApp model = this.getCombinationApp(syncId);
		if (model != null) {
			model.setId(idGenerator.nextId("SYS_COMBINATION_APP"));
			model.setCreateTime(new Date());
			model.setCreateBy(createBy);
			combinationAppMapper.insertCombinationApp(model);

			if (model.getItems() != null && !model.getItems().isEmpty()) {
				for (CombinationItem item : model.getItems()) {
					item.setId(idGenerator.nextId("SYS_COMBINATION_ITEM"));
					item.setCreateTime(new Date());
					item.setCreateBy(createBy);
					item.setSyncId(model.getId());
					combinationItemMapper.insertCombinationItem(item);
				}
			}

			return model.getId();
		}
		return -1;
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

	@javax.annotation.Resource(name = "com.glaf.matrix.combination.mapper.CombinationAppMapper")
	public void setCombinationAppMapper(CombinationAppMapper combinationAppMapper) {
		this.combinationAppMapper = combinationAppMapper;
	}

	@javax.annotation.Resource(name = "com.glaf.matrix.combination.mapper.CombinationItemMapper")
	public void setCombinationItemMapper(CombinationItemMapper combinationItemMapper) {
		this.combinationItemMapper = combinationItemMapper;
	}

}
