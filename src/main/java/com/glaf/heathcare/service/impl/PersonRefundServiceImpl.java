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
import com.glaf.core.util.*;

import com.glaf.heathcare.mapper.*;
import com.glaf.heathcare.domain.*;
import com.glaf.heathcare.query.*;
import com.glaf.heathcare.service.PersonRefundService;

@Service("com.glaf.heathcare.service.personRefundService")
@Transactional(readOnly = true)
public class PersonRefundServiceImpl implements PersonRefundService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected PersonRefundMapper personRefundMapper;

	public PersonRefundServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<PersonRefund> list) {
		long ts = System.currentTimeMillis();
		for (PersonRefund personRefund : list) {
			if (StringUtils.isEmpty(personRefund.getId())) {
				personRefund.setId(personRefund.getPersonId() + "_" + ts++);
			}
		}

		int batch_size = 100;
		List<PersonRefund> rows = new ArrayList<PersonRefund>(batch_size);

		for (PersonRefund bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					personRefundMapper.bulkInsertPersonRefund_oracle(rows);
				} else {
					personRefundMapper.bulkInsertPersonRefund(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				personRefundMapper.bulkInsertPersonRefund_oracle(rows);
			} else {
				personRefundMapper.bulkInsertPersonRefund(rows);
			}
			rows.clear();
		}
	}

	public int count(PersonRefundQuery query) {
		return personRefundMapper.getPersonRefundCount(query);
	}

	@Transactional
	public void deleteById(String id) {
		if (id != null) {
			personRefundMapper.deletePersonRefundById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (String id : ids) {
				personRefundMapper.deletePersonRefundById(id);
			}
		}
	}

	public PersonRefund getPersonRefund(String id) {
		if (id == null) {
			return null;
		}
		PersonRefund personRefund = personRefundMapper.getPersonRefundById(id);
		return personRefund;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getPersonRefundCountByQueryCriteria(PersonRefundQuery query) {
		return personRefundMapper.getPersonRefundCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<PersonRefund> getPersonRefundsByQueryCriteria(int start, int pageSize, PersonRefundQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<PersonRefund> rows = sqlSessionTemplate.selectList("getPersonRefunds", query, rowBounds);
		return rows;
	}

	public List<PersonRefund> list(PersonRefundQuery query) {
		List<PersonRefund> list = personRefundMapper.getPersonRefunds(query);
		return list;
	}

	@Transactional
	public void save(PersonRefund personRefund) {
		if (StringUtils.isEmpty(personRefund.getId())) {
			personRefund.setId(personRefund.getPersonId() + "_" + System.currentTimeMillis());
			personRefund.setCreateTime(new Date());
			personRefundMapper.insertPersonRefund(personRefund);
		} else {
			personRefundMapper.updatePersonRefund(personRefund);
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

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.PersonRefundMapper")
	public void setPersonRefundMapper(PersonRefundMapper personRefundMapper) {
		this.personRefundMapper = personRefundMapper;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@Transactional
	public void updatePersonRefundStatus(List<PersonRefund> personRefunds) {
		for (PersonRefund personRefund : personRefunds) {
			personRefund.setBusinessStatus(9);
			personRefund.setConfirmTime(new Date());
			personRefundMapper.updatePersonRefundStatus(personRefund);
		}
	}

	@Transactional
	public void updatePersonRefundStatus(PersonRefund personRefund) {
		personRefund.setBusinessStatus(9);
		personRefund.setConfirmTime(new Date());
		personRefundMapper.updatePersonRefundStatus(personRefund);
	}

}
