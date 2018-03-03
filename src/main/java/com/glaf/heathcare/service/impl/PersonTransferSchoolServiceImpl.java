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
import com.glaf.heathcare.service.*;

@Service("com.glaf.heathcare.service.personTransferSchoolService")
@Transactional(readOnly = true)
public class PersonTransferSchoolServiceImpl implements PersonTransferSchoolService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected PersonTransferSchoolMapper personTransferSchoolMapper;

	public PersonTransferSchoolServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<PersonTransferSchool> list) {
		for (PersonTransferSchool personTransferSchool : list) {
			if (StringUtils.isEmpty(personTransferSchool.getId())) {
				personTransferSchool.setId(idGenerator.getNextId("HEALTH_PERSON_TRANSFER_SCHOOL"));
				personTransferSchool.setCreateTime(new Date());
			}
		}

		int batch_size = 50;
		List<PersonTransferSchool> rows = new ArrayList<PersonTransferSchool>(batch_size);

		for (PersonTransferSchool bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % 100 == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					personTransferSchoolMapper.bulkInsertPersonTransferSchool_oracle(rows);
				} else {
					personTransferSchoolMapper.bulkInsertPersonTransferSchool(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				personTransferSchoolMapper.bulkInsertPersonTransferSchool_oracle(rows);
			} else {
				personTransferSchoolMapper.bulkInsertPersonTransferSchool(rows);
			}
			rows.clear();
		}
	}

	public int count(PersonTransferSchoolQuery query) {
		return personTransferSchoolMapper.getPersonTransferSchoolCount(query);
	}

	@Transactional
	public void deleteById(String id) {
		if (id != null) {
			personTransferSchoolMapper.deletePersonTransferSchoolById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (String id : ids) {
				personTransferSchoolMapper.deletePersonTransferSchoolById(id);
			}
		}
	}

	public PersonTransferSchool getPersonTransferSchool(String id) {
		if (id == null) {
			return null;
		}
		PersonTransferSchool personTransferSchool = personTransferSchoolMapper.getPersonTransferSchoolById(id);
		return personTransferSchool;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getPersonTransferSchoolCountByQueryCriteria(PersonTransferSchoolQuery query) {
		return personTransferSchoolMapper.getPersonTransferSchoolCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<PersonTransferSchool> getPersonTransferSchoolsByQueryCriteria(int start, int pageSize,
			PersonTransferSchoolQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<PersonTransferSchool> rows = sqlSessionTemplate.selectList("getPersonTransferSchools", query, rowBounds);
		return rows;
	}

	public List<PersonTransferSchool> list(PersonTransferSchoolQuery query) {
		List<PersonTransferSchool> list = personTransferSchoolMapper.getPersonTransferSchools(query);
		return list;
	}

	@Transactional
	public void save(PersonTransferSchool personTransferSchool) {
		if (StringUtils.isEmpty(personTransferSchool.getId())) {
			personTransferSchool.setId(idGenerator.getNextId("HEALTH_PERSON_TRANSFER_SCHOOL"));
			personTransferSchool.setCreateTime(new Date());
			// personTransferSchool.setDeleteFlag(0);
			personTransferSchoolMapper.insertPersonTransferSchool(personTransferSchool);
		} else {
			personTransferSchoolMapper.updatePersonTransferSchool(personTransferSchool);
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

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.PersonTransferSchoolMapper")
	public void setPersonTransferSchoolMapper(PersonTransferSchoolMapper personTransferSchoolMapper) {
		this.personTransferSchoolMapper = personTransferSchoolMapper;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

}
