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

package com.glaf.remote.service;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.remote.domain.RemotePermission;
import com.glaf.remote.mapper.RemotePermissionMapper;
import com.glaf.remote.query.RemotePermissionQuery;
import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;

@Service("com.glaf.remote.service.remotePermissionService")
@Transactional(readOnly = true)
public class RemotePermissionServiceImpl implements RemotePermissionService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected RemotePermissionMapper remotePermissionMapper;

	public RemotePermissionServiceImpl() {

	}

	public int count(RemotePermissionQuery query) {
		return remotePermissionMapper.getRemotePermissionCount(query);
	}

	@Transactional
	public void deleteById(long id) {
		if (id != 0) {
			remotePermissionMapper.deleteRemotePermissionById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				remotePermissionMapper.deleteRemotePermissionById(id);
			}
		}
	}

	public RemotePermission getRemotePermission(long id) {
		if (id == 0) {
			return null;
		}
		RemotePermission remotePermission = remotePermissionMapper.getRemotePermissionById(id);
		return remotePermission;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getRemotePermissionCountByQueryCriteria(RemotePermissionQuery query) {
		return remotePermissionMapper.getRemotePermissionCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<RemotePermission> getRemotePermissionsByQueryCriteria(int start, int pageSize,
			RemotePermissionQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<RemotePermission> rows = sqlSessionTemplate.selectList("getRemotePermissions", query, rowBounds);
		return rows;
	}

	public List<RemotePermission> list(RemotePermissionQuery query) {
		List<RemotePermission> list = remotePermissionMapper.getRemotePermissions(query);
		return list;
	}

	/**
	 * 保存多条记录
	 * 
	 * @return
	 */
	@Transactional
	public void saveAll(String type, List<RemotePermission> list) {
		remotePermissionMapper.deleteRemotePermissions(type);
		for (RemotePermission remotePermission : list) {
			if (remotePermission.getId() == 0) {
				remotePermission.setId(idGenerator.nextId("SYS_REMOTE_PERMISSION"));
				remotePermission.setCreateTime(new java.util.Date());
				remotePermission.setType(type);
				remotePermissionMapper.insertRemotePermission(remotePermission);
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

	@javax.annotation.Resource(name = "com.glaf.remote.mapper.RemotePermissionMapper")
	public void setRemotePermissionMapper(RemotePermissionMapper remotePermissionMapper) {
		this.remotePermissionMapper = remotePermissionMapper;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

}
