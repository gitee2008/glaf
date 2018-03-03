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
import com.glaf.heathcare.service.PersonPaymentService;

@Service("com.glaf.heathcare.service.personPaymentService")
@Transactional(readOnly = true)
public class PersonPaymentServiceImpl implements PersonPaymentService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected PersonPaymentMapper personPaymentMapper;

	public PersonPaymentServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<PersonPayment> list) {
		long ts = System.currentTimeMillis();
		for (PersonPayment personPayment : list) {
			if (StringUtils.isEmpty(personPayment.getId())) {
				personPayment.setId(personPayment.getPersonId() + "_" + ts++);
				personPayment.setCreateTime(new Date());
			}
		}

		int batch_size = 100;
		List<PersonPayment> rows = new ArrayList<PersonPayment>(batch_size);

		for (PersonPayment bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					personPaymentMapper.bulkInsertPersonPayment_oracle(rows);
				} else {
					personPaymentMapper.bulkInsertPersonPayment(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				personPaymentMapper.bulkInsertPersonPayment_oracle(rows);
			} else {
				personPaymentMapper.bulkInsertPersonPayment(rows);
			}
			rows.clear();
		}
	}

	public int count(PersonPaymentQuery query) {
		return personPaymentMapper.getPersonPaymentCount(query);
	}

	@Transactional
	public void deleteById(String id) {
		if (id != null) {
			personPaymentMapper.deletePersonPaymentById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (String id : ids) {
				personPaymentMapper.deletePersonPaymentById(id);
			}
		}
	}

	public PersonPayment getPersonPayment(String id) {
		if (id == null) {
			return null;
		}
		PersonPayment personPayment = personPaymentMapper.getPersonPaymentById(id);
		return personPayment;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getPersonPaymentCountByQueryCriteria(PersonPaymentQuery query) {
		return personPaymentMapper.getPersonPaymentCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<PersonPayment> getPersonPaymentsByQueryCriteria(int start, int pageSize, PersonPaymentQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<PersonPayment> rows = sqlSessionTemplate.selectList("getPersonPayments", query, rowBounds);
		return rows;
	}

	public List<PersonPayment> list(PersonPaymentQuery query) {
		List<PersonPayment> list = personPaymentMapper.getPersonPayments(query);
		return list;
	}

	@Transactional
	public void save(PersonPayment personPayment) {
		long ts = System.currentTimeMillis();
		if (StringUtils.isEmpty(personPayment.getId())) {
			personPayment.setId(personPayment.getPersonId() + "_" + ts++);
			personPayment.setCreateTime(new Date());
			personPaymentMapper.insertPersonPayment(personPayment);
		} else {
			personPaymentMapper.updatePersonPayment(personPayment);
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

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.PersonPaymentMapper")
	public void setPersonPaymentMapper(PersonPaymentMapper personPaymentMapper) {
		this.personPaymentMapper = personPaymentMapper;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@Transactional
	public void updatePersonPaymentStatus(List<PersonPayment> personPayments) {
        for(PersonPayment personPayment:personPayments){
        	personPayment.setBusinessStatus(9);
    		personPayment.setConfirmTime(new Date());
    		personPaymentMapper.updatePersonPaymentStatus(personPayment);
        }
	}

	@Transactional
	public void updatePersonPaymentStatus(PersonPayment personPayment) {
		personPayment.setBusinessStatus(9);
		personPayment.setConfirmTime(new Date());
		personPaymentMapper.updatePersonPaymentStatus(personPayment);
	}

}
