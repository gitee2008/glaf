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
import com.glaf.matrix.sync.domain.SyncApp;
import com.glaf.matrix.sync.domain.SyncItem;
import com.glaf.matrix.sync.mapper.SyncAppMapper;
import com.glaf.matrix.sync.mapper.SyncItemMapper;
import com.glaf.matrix.sync.query.SyncAppQuery;
import com.glaf.matrix.sync.query.SyncItemQuery;

@Service("com.glaf.matrix.sync.service.syncAppService")
@Transactional(readOnly = true)
public class SyncAppServiceImpl implements SyncAppService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected SyncAppMapper syncAppMapper;

	protected SyncItemMapper syncItemMapper;

	public SyncAppServiceImpl() {

	}

	public int count(SyncAppQuery query) {
		return syncAppMapper.getSyncAppCount(query);
	}

	@Transactional
	public void deleteById(Long id) {
		if (id != null) {
			syncAppMapper.deleteSyncAppById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				syncAppMapper.deleteSyncAppById(id);
			}
		}
	}

	public SyncApp getSyncApp(Long syncId) {
		if (syncId == null) {
			return null;
		}
		SyncApp syncApp = syncAppMapper.getSyncAppById(syncId);
		if (syncApp != null) {
			SyncItemQuery query = new SyncItemQuery();
			query.syncId(syncId);
			List<SyncItem> items = syncItemMapper.getSyncItems(query);
			syncApp.setItems(items);
		}
		return syncApp;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getSyncAppCountByQueryCriteria(SyncAppQuery query) {
		return syncAppMapper.getSyncAppCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<SyncApp> getSyncAppsByQueryCriteria(int start, int pageSize, SyncAppQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<SyncApp> rows = sqlSessionTemplate.selectList("getSyncApps", query, rowBounds);
		return rows;
	}

	public List<SyncApp> list(SyncAppQuery query) {
		List<SyncApp> list = syncAppMapper.getSyncApps(query);
		return list;
	}

	@Transactional
	public void save(SyncApp syncApp) {
		if (syncApp.getId() == 0) {
			syncApp.setId(idGenerator.nextId("SYS_SYNC_APP"));
			syncApp.setCreateTime(new Date());
			if (StringUtils.isEmpty(syncApp.getDeploymentId())) {
				syncApp.setDeploymentId(UUID32.getUUID());
			}
			syncAppMapper.insertSyncApp(syncApp);
		} else {
			syncAppMapper.updateSyncApp(syncApp);
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

	@javax.annotation.Resource(name = "com.glaf.matrix.sync.mapper.SyncAppMapper")
	public void setSyncAppMapper(SyncAppMapper syncAppMapper) {
		this.syncAppMapper = syncAppMapper;
	}

	@javax.annotation.Resource(name = "com.glaf.matrix.sync.mapper.SyncItemMapper")
	public void setSyncItemMapper(SyncItemMapper syncItemMapper) {
		this.syncItemMapper = syncItemMapper;
	}

}
