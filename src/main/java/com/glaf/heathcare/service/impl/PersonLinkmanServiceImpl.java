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
import com.glaf.heathcare.service.PersonLinkmanService;

@Service("com.glaf.heathcare.service.personLinkmanService")
@Transactional(readOnly = true)
public class PersonLinkmanServiceImpl implements PersonLinkmanService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected PersonLinkmanMapper personLinkmanMapper;

	public PersonLinkmanServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<PersonLinkman> list) {
		for (PersonLinkman personLinkman : list) {
			if (StringUtils.isEmpty(personLinkman.getId())) {
				personLinkman.setId(idGenerator.getNextId("HEALTH_PERSON_LINKMAN"));
			}
		}

		int batch_size = 100;
		List<PersonLinkman> rows = new ArrayList<PersonLinkman>(batch_size);

		for (PersonLinkman bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					personLinkmanMapper.bulkInsertPersonLinkman_oracle(rows);
				} else {
					personLinkmanMapper.bulkInsertPersonLinkman(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				personLinkmanMapper.bulkInsertPersonLinkman_oracle(rows);
			} else {
				personLinkmanMapper.bulkInsertPersonLinkman(rows);
			}
			rows.clear();
		}
	}

	public int count(PersonLinkmanQuery query) {
		return personLinkmanMapper.getPersonLinkmanCount(query);
	}

	@Transactional
	public void deleteById(String id) {
		if (id != null) {
			personLinkmanMapper.deletePersonLinkmanById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (String id : ids) {
				personLinkmanMapper.deletePersonLinkmanById(id);
			}
		}
	}

	public List<PersonLinkman> getLinkmans(String personId) {
		PersonLinkmanQuery query = new PersonLinkmanQuery();
		query.personId(personId);
		List<PersonLinkman> list = personLinkmanMapper.getPersonLinkmans(query);
		return list;
	}

	public PersonLinkman getPersonLinkman(String id) {
		if (id == null) {
			return null;
		}
		PersonLinkman personLinkman = personLinkmanMapper.getPersonLinkmanById(id);
		return personLinkman;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getPersonLinkmanCountByQueryCriteria(PersonLinkmanQuery query) {
		return personLinkmanMapper.getPersonLinkmanCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<PersonLinkman> getPersonLinkmansByQueryCriteria(int start, int pageSize, PersonLinkmanQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<PersonLinkman> rows = sqlSessionTemplate.selectList("getPersonLinkmans", query, rowBounds);
		return rows;
	}

	public List<PersonLinkman> list(PersonLinkmanQuery query) {
		List<PersonLinkman> list = personLinkmanMapper.getPersonLinkmans(query);
		return list;
	}

	@Transactional
	public void save(PersonLinkman linkman) {
		if (StringUtils.isEmpty(linkman.getId())) {
			linkman.setId(UUID32.getUUID());
			linkman.setCreateTime(new Date());

			personLinkmanMapper.insertPersonLinkman(linkman);
		} else {
			personLinkmanMapper.updatePersonLinkman(linkman);
		}
	}

	/**
	 * 
	 * @param linkmans
	 */
	@Transactional
	public void saveAll(List<PersonLinkman> linkmans) {
		for (PersonLinkman linkman : linkmans) {
			if (StringUtils.isNotEmpty(linkman.getId())) {
				PersonLinkman model = this.getPersonLinkman(linkman.getId());
				if (model != null) {
					model.setCompany(linkman.getCompany());
					model.setName(linkman.getName());
					model.setMail(linkman.getMail());
					model.setMobile(linkman.getMobile());
					model.setRelationship(linkman.getRelationship());
					model.setWardship(linkman.getWardship());
					model.setRemark(linkman.getRemark());
					personLinkmanMapper.updatePersonLinkman(model);
				} else {
					linkman.setId(UUID32.getUUID());
					linkman.setCreateTime(new Date());
					personLinkmanMapper.insertPersonLinkman(linkman);
				}
			} else {
				linkman.setId(UUID32.getUUID());
				linkman.setCreateTime(new Date());
				personLinkmanMapper.insertPersonLinkman(linkman);
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

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.PersonLinkmanMapper")
	public void setPersonLinkmanMapper(PersonLinkmanMapper personLinkmanMapper) {
		this.personLinkmanMapper = personLinkmanMapper;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

}
