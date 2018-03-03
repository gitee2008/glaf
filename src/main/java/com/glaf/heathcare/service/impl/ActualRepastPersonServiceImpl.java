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

package com.glaf.heathcare.service.impl;

import java.util.ArrayList;
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
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.util.DBUtils;
import com.glaf.heathcare.domain.ActualRepastPerson;
import com.glaf.heathcare.mapper.ActualRepastPersonMapper;
import com.glaf.heathcare.query.ActualRepastPersonQuery;
import com.glaf.heathcare.service.ActualRepastPersonService;

@Service("com.glaf.heathcare.service.actualRepastPersonService")
@Transactional(readOnly = true)
public class ActualRepastPersonServiceImpl implements ActualRepastPersonService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected ActualRepastPersonMapper actualRepastPersonMapper;

	public ActualRepastPersonServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<ActualRepastPerson> list) {
		for (ActualRepastPerson actualRepastPerson : list) {
			if (StringUtils.isEmpty(actualRepastPerson.getId())) {
				actualRepastPerson.setId(idGenerator.getNextId("HEALTH_REPAST_PERSON"));
			}
		}

		int batch_size = 50;
		List<ActualRepastPerson> rows = new ArrayList<ActualRepastPerson>(batch_size);

		for (ActualRepastPerson bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					actualRepastPersonMapper.bulkInsertActualRepastPerson_oracle(rows);
				} else {
					actualRepastPersonMapper.bulkInsertActualRepastPerson(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				actualRepastPersonMapper.bulkInsertActualRepastPerson_oracle(rows);
			} else {
				actualRepastPersonMapper.bulkInsertActualRepastPerson(rows);
			}
			rows.clear();
		}
	}

	@Transactional
	public void deleteById(String id) {
		if (id != null) {
			actualRepastPersonMapper.deleteActualRepastPersonById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (String id : ids) {
				actualRepastPersonMapper.deleteActualRepastPersonById(id);
			}
		}
	}

	public int count(ActualRepastPersonQuery query) {
		return actualRepastPersonMapper.getActualRepastPersonCount(query);
	}

	public List<ActualRepastPerson> list(ActualRepastPersonQuery query) {
		List<ActualRepastPerson> list = actualRepastPersonMapper.getActualRepastPersons(query);
		return list;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getActualRepastPersonCountByQueryCriteria(ActualRepastPersonQuery query) {
		return actualRepastPersonMapper.getActualRepastPersonCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<ActualRepastPerson> getActualRepastPersonsByQueryCriteria(int start, int pageSize,
			ActualRepastPersonQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<ActualRepastPerson> rows = sqlSessionTemplate.selectList("getActualRepastPersons", query, rowBounds);
		return rows;
	}

	public ActualRepastPerson getActualRepastPerson(String id) {
		if (id == null) {
			return null;
		}
		ActualRepastPerson actualRepastPerson = actualRepastPersonMapper.getActualRepastPersonById(id);
		return actualRepastPerson;
	}

	@Transactional
	public void save(ActualRepastPerson actualRepastPerson) {
		if (StringUtils.isEmpty(actualRepastPerson.getId())) {
			actualRepastPerson.setId(idGenerator.getNextId("HEALTH_REPAST_PERSON"));
			actualRepastPerson.setCreateTime(new Date());
			actualRepastPersonMapper.insertActualRepastPerson(actualRepastPerson);
		} else {
			actualRepastPersonMapper.updateActualRepastPerson(actualRepastPerson);
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

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.ActualRepastPersonMapper")
	public void setActualRepastPersonMapper(ActualRepastPersonMapper actualRepastPersonMapper) {
		this.actualRepastPersonMapper = actualRepastPersonMapper;
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
