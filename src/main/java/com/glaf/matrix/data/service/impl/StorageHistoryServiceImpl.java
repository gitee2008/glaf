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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.id.*;
import com.glaf.core.dao.*;

import com.glaf.matrix.data.mapper.*;
import com.glaf.matrix.data.domain.*;
import com.glaf.matrix.data.query.*;
import com.glaf.matrix.data.service.*;

@Service("com.glaf.matrix.data.service.storageHistoryService")
@Transactional(readOnly = true)
public class StorageHistoryServiceImpl implements StorageHistoryService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected StorageHistoryMapper storageHistoryMapper;

	public StorageHistoryServiceImpl() {

	}

	public int count(StorageHistoryQuery query) {
		return storageHistoryMapper.getStorageHistoryCount(query);
	}

	@Transactional
	public void deleteById(Long id) {
		if (id != null) {
			storageHistoryMapper.deleteStorageHistoryById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				storageHistoryMapper.deleteStorageHistoryById(id);
			}
		}
	}

	public StorageHistory getLatestStorageHistoryByStorageId(long storageId) {
		StorageHistory storageHistory = storageHistoryMapper.getLatestStorageHistoryByStorageId(storageId);
		return storageHistory;
	}

	public StorageHistory getStorageHistory(Long id) {
		if (id == null) {
			return null;
		}
		StorageHistory storageHistory = storageHistoryMapper.getStorageHistoryById(id);
		return storageHistory;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getStorageHistoryCountByQueryCriteria(StorageHistoryQuery query) {
		return storageHistoryMapper.getStorageHistoryCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<StorageHistory> getStorageHistorysByQueryCriteria(int start, int pageSize, StorageHistoryQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<StorageHistory> rows = sqlSessionTemplate.selectList("getStorageHistorys", query, rowBounds);
		return rows;
	}

	public List<StorageHistory> list(StorageHistoryQuery query) {
		List<StorageHistory> list = storageHistoryMapper.getStorageHistorys(query);
		return list;
	}

	@Transactional
	public void save(StorageHistory storageHistory) {
		if (storageHistory.getId() == 0) {
			storageHistory.setId(idGenerator.nextId("SYS_STORAGE_HISTORY"));
			storageHistory.setCreateTime(new Date());
			storageHistoryMapper.insertStorageHistory(storageHistory);
		} else {
			storageHistoryMapper.updateStorageHistory(storageHistory);
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

	@javax.annotation.Resource(name = "com.glaf.matrix.data.mapper.StorageHistoryMapper")
	public void setStorageHistoryMapper(StorageHistoryMapper storageHistoryMapper) {
		this.storageHistoryMapper = storageHistoryMapper;
	}

}
