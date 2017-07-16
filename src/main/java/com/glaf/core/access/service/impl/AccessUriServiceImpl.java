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

package com.glaf.core.access.service.impl;

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
import com.glaf.core.util.DBUtils;

import com.glaf.core.access.mapper.*;
import com.glaf.core.access.domain.*;
import com.glaf.core.access.query.*;
import com.glaf.core.access.service.AccessUriService;

@Service("accessUriService")
@Transactional(readOnly = true)
public class AccessUriServiceImpl implements AccessUriService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected AccessUriMapper accessUriMapper;

	public AccessUriServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<AccessUri> list) {
		for (AccessUri accessUri : list) {
			if (accessUri.getId() == 0) {
				accessUri.setId(idGenerator.nextId("SYS_ACCESS_URI"));
			}
		}

		List<AccessUri> rows = new ArrayList<AccessUri>();

		for (AccessUri bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % 100 == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					accessUriMapper.bulkInsertAccessUri_oracle(list);
				} else {
					accessUriMapper.bulkInsertAccessUri(list);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				accessUriMapper.bulkInsertAccessUri_oracle(list);
			} else {
				accessUriMapper.bulkInsertAccessUri(list);
			}
			rows.clear();
		}
	}

	public int count(AccessUriQuery query) {
		return accessUriMapper.getAccessUriCount(query);
	}

	@Transactional
	public void deleteById(Long id) {
		if (id != null) {
			accessUriMapper.deleteAccessUriById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				accessUriMapper.deleteAccessUriById(id);
			}
		}
	}

	public AccessUri getAccessUri(Long id) {
		if (id == null) {
			return null;
		}
		AccessUri accessUri = accessUriMapper.getAccessUriById(id);
		return accessUri;
	}

	/**
	 * 根据uri获取一条记录
	 * 
	 * @return
	 */
	public AccessUri getAccessUriByUri(String uri) {
		return accessUriMapper.getAccessUriByUri(uri);
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getAccessUriCountByQueryCriteria(AccessUriQuery query) {
		return accessUriMapper.getAccessUriCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<AccessUri> getAccessUrisByQueryCriteria(int start, int pageSize, AccessUriQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<AccessUri> rows = sqlSessionTemplate.selectList("getAccessUris", query, rowBounds);
		return rows;
	}

	public List<AccessUri> list(AccessUriQuery query) {
		List<AccessUri> list = accessUriMapper.getAccessUris(query);
		return list;
	}

	@Transactional
	public void save(AccessUri accessUri) {
		if (accessUri.getId() == 0) {
			accessUri.setId(idGenerator.nextId("SYS_ACCESS_URI"));
			accessUriMapper.insertAccessUri(accessUri);
		} else {
			accessUriMapper.updateAccessUri(accessUri);
		}
	}

	@javax.annotation.Resource
	public void setAccessUriMapper(AccessUriMapper accessUriMapper) {
		this.accessUriMapper = accessUriMapper;
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
