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

package com.glaf.matrix.sync.service;

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
import com.glaf.matrix.sync.domain.SyncHistory;
import com.glaf.matrix.sync.mapper.SyncHistoryMapper;
import com.glaf.matrix.sync.query.SyncHistoryQuery;

@Service("com.glaf.matrix.sync.service.syncHistoryService")
@Transactional(readOnly = true)
public class SyncHistoryServiceImpl implements SyncHistoryService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected SyncHistoryMapper syncHistoryMapper;

	public SyncHistoryServiceImpl() {

	}

	public int count(SyncHistoryQuery query) {
		return syncHistoryMapper.getSyncHistoryCount(query);
	}

	@Transactional
	public void deleteById(Long id) {
		if (id != null) {
			syncHistoryMapper.deleteSyncHistoryById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				syncHistoryMapper.deleteSyncHistoryById(id);
			}
		}
	}

	public SyncHistory getSyncHistory(Long id) {
		if (id == null) {
			return null;
		}
		SyncHistory syncHistory = syncHistoryMapper.getSyncHistoryById(id);
		return syncHistory;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getSyncHistoryCountByQueryCriteria(SyncHistoryQuery query) {
		return syncHistoryMapper.getSyncHistoryCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<SyncHistory> getSyncHistorysByQueryCriteria(int start, int pageSize, SyncHistoryQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<SyncHistory> rows = sqlSessionTemplate.selectList("getSyncHistorys", query, rowBounds);
		return rows;
	}

	public List<SyncHistory> list(SyncHistoryQuery query) {
		List<SyncHistory> list = syncHistoryMapper.getSyncHistorys(query);
		return list;
	}

	@Transactional
	public void save(SyncHistory syncHistory) {
		if (syncHistory.getId() == 0) {
			syncHistory.setId(idGenerator.nextId("SYS_SYNC_HISTORY"));
			syncHistory.setCreateTime(new Date());

			syncHistoryMapper.insertSyncHistory(syncHistory);
		} else {
			syncHistoryMapper.updateSyncHistory(syncHistory);
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

	@javax.annotation.Resource(name = "com.glaf.matrix.sync.mapper.SyncHistoryMapper")
	public void setSyncHistoryMapper(SyncHistoryMapper syncHistoryMapper) {
		this.syncHistoryMapper = syncHistoryMapper;
	}

}
