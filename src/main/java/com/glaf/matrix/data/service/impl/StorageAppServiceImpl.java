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
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.id.*;
import com.glaf.core.util.UUID32;
import com.glaf.core.dao.*;

import com.glaf.matrix.data.mapper.*;
import com.glaf.matrix.data.domain.*;
import com.glaf.matrix.data.query.*;
import com.glaf.matrix.data.service.*;

@Service("com.glaf.matrix.data.service.storageAppService")
@Transactional(readOnly = true)
public class StorageAppServiceImpl implements StorageAppService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected StorageAppMapper storageAppMapper;

	public StorageAppServiceImpl() {

	}

	public int count(StorageAppQuery query) {
		return storageAppMapper.getStorageAppCount(query);
	}

	@Transactional
	public void deleteById(Long id) {
		if (id != null) {
			storageAppMapper.deleteStorageAppById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				storageAppMapper.deleteStorageAppById(id);
			}
		}
	}

	public StorageApp getStorageApp(Long id) {
		if (id == null) {
			return null;
		}
		StorageApp storageApp = storageAppMapper.getStorageAppById(id);
		return storageApp;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getStorageAppCountByQueryCriteria(StorageAppQuery query) {
		return storageAppMapper.getStorageAppCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<StorageApp> getStorageAppsByQueryCriteria(int start, int pageSize, StorageAppQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<StorageApp> rows = sqlSessionTemplate.selectList("getStorageApps", query, rowBounds);
		return rows;
	}

	@Transactional
	public int incrementVersion(long storageId) {
		StorageApp storageApp = this.getStorageApp(storageId);
		storageApp.setVersion(storageApp.getVersion() + 1);
		storageApp.setUpdateTime(new Date());
		storageAppMapper.incrementVersion(storageApp);
		return storageApp.getVersion();
	}

	public List<StorageApp> list(StorageAppQuery query) {
		List<StorageApp> list = storageAppMapper.getStorageApps(query);
		return list;
	}

	@Transactional
	public void save(StorageApp storageApp) {
		if (storageApp.getId() == 0) {
			storageApp.setId(idGenerator.nextId("SYS_STORAGE_APP"));
			storageApp.setCreateTime(new Date());
			if (StringUtils.isEmpty(storageApp.getDeploymentId())) {
				storageApp.setDeploymentId(UUID32.getUUID());
			}
			storageAppMapper.insertStorageApp(storageApp);
		} else {
			storageApp.setUpdateTime(new Date());
			storageAppMapper.updateStorageApp(storageApp);
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

	@javax.annotation.Resource(name = "com.glaf.matrix.data.mapper.StorageAppMapper")
	public void setStorageAppMapper(StorageAppMapper storageAppMapper) {
		this.storageAppMapper = storageAppMapper;
	}

}
