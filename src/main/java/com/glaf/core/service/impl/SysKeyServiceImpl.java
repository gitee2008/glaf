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

package com.glaf.core.service.impl;

import com.glaf.core.domain.SysKey;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.mapper.SysKeyMapper;
import com.glaf.core.query.SysKeyQuery;
import com.glaf.core.service.SysKeyService;
import com.glaf.core.util.DBUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service("sysKeyService")
@Transactional(readOnly = true)
public class SysKeyServiceImpl implements SysKeyService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private IdGenerator idGenerator;

	private SqlSessionTemplate sqlSessionTemplate;

	private SysKeyMapper sysKeyMapper;

	public int count(SysKeyQuery query) {
		return sysKeyMapper.getSysKeyCount(query);
	}

	@Transactional
	public void deleteById(String id) {
		if (id != null) {
			sysKeyMapper.deleteSysKeyById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (String id : ids) {
				sysKeyMapper.deleteSysKeyById(id);
			}
		}
	}

	public SysKey getSysKey(String id) {
		if (id == null) {
			return null;
		}
		if (StringUtils.equals(DBUtils.POSTGRESQL, DBConnectionFactory.getDatabaseType())) {
			return sysKeyMapper.getSysKeyById_postgres(id);
		}
		return sysKeyMapper.getSysKeyById(id);
	}

	/**
	 * 根据查询参数获取记录总数
	 *
	 * @return
	 */
	public int getSysKeyCountByQueryCriteria(SysKeyQuery query) {
		return sysKeyMapper.getSysKeyCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 *
	 * @return
	 */
	public List<SysKey> getSysKeysByQueryCriteria(int start, int pageSize, SysKeyQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		return sqlSessionTemplate.selectList("getSysKeys", query, rowBounds);
	}

	public List<SysKey> list(SysKeyQuery query) {
		return sysKeyMapper.getSysKeys(query);
	}

	@Transactional
	public void save(SysKey sysKey) {
		if (StringUtils.isEmpty(sysKey.getId())) {
			sysKey.setId(idGenerator.getNextId("SYS_KEY"));
			sysKey.setCreateDate(new Date());
			if (StringUtils.equals(DBUtils.POSTGRESQL, DBConnectionFactory.getDatabaseType())) {
				sysKeyMapper.insertSysKey_postgres(sysKey);
			} else {
				sysKeyMapper.insertSysKey(sysKey);
			}
		} else {
			if (StringUtils.equals(DBUtils.POSTGRESQL, DBConnectionFactory.getDatabaseType())) {
				if (sysKeyMapper.getSysKeyById_postgres(sysKey.getId()) == null) {
					sysKey.setCreateDate(new Date());
					sysKeyMapper.insertSysKey(sysKey);
				}
			} else {
				if (sysKeyMapper.getSysKeyById(sysKey.getId()) == null) {
					sysKey.setCreateDate(new Date());
					sysKeyMapper.insertSysKey(sysKey);
				}
			}
		}
	}

	@javax.annotation.Resource
	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@javax.annotation.Resource
	public void setSysKeyMapper(SysKeyMapper sysKeyMapper) {
		this.sysKeyMapper = sysKeyMapper;
	}

}
