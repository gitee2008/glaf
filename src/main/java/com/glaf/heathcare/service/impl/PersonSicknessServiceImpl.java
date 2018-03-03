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
import java.util.Calendar;
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
import com.glaf.heathcare.domain.PersonSickness;
import com.glaf.heathcare.mapper.PersonSicknessMapper;
import com.glaf.heathcare.query.PersonSicknessQuery;
import com.glaf.heathcare.service.PersonSicknessService;

@Service("com.glaf.heathcare.service.personSicknessService")
@Transactional(readOnly = true)
public class PersonSicknessServiceImpl implements PersonSicknessService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected PersonSicknessMapper personSicknessMapper;

	public PersonSicknessServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<PersonSickness> list) {
		Calendar calendar = Calendar.getInstance();
		for (PersonSickness personSickness : list) {
			if (StringUtils.isEmpty(personSickness.getId())) {
				personSickness.setId(idGenerator.getNextId("HEALTH_PERSON_SICKNESS"));
				personSickness.setCreateTime(new Date());

				if (personSickness.getConfirmTime() != null) {
					calendar.setTime(personSickness.getConfirmTime());
				} else if (personSickness.getClinicTime() != null) {
					calendar.setTime(personSickness.getClinicTime());
				} else {
					calendar.setTime(personSickness.getCreateTime());
				}

				personSickness.setYear(calendar.get(Calendar.YEAR));
				personSickness.setMonth(calendar.get(Calendar.MONTH + 1));
			}
		}

		int batch_size = 50;
		List<PersonSickness> rows = new ArrayList<PersonSickness>(batch_size);

		for (PersonSickness bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					personSicknessMapper.bulkInsertPersonSickness_oracle(rows);
				} else {
					personSicknessMapper.bulkInsertPersonSickness(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				personSicknessMapper.bulkInsertPersonSickness_oracle(rows);
			} else {
				personSicknessMapper.bulkInsertPersonSickness(rows);
			}
			rows.clear();
		}
	}

	@Transactional
	public void deleteById(String id) {
		if (id != null) {
			personSicknessMapper.deletePersonSicknessById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (String id : ids) {
				personSicknessMapper.deletePersonSicknessById(id);
			}
		}
	}

	public int count(PersonSicknessQuery query) {
		return personSicknessMapper.getPersonSicknessCount(query);
	}

	public List<PersonSickness> list(PersonSicknessQuery query) {
		List<PersonSickness> list = personSicknessMapper.getPersonSicknesss(query);
		return list;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getPersonSicknessCountByQueryCriteria(PersonSicknessQuery query) {
		return personSicknessMapper.getPersonSicknessCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<PersonSickness> getPersonSicknesssByQueryCriteria(int start, int pageSize, PersonSicknessQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<PersonSickness> rows = sqlSessionTemplate.selectList("getPersonSicknesss", query, rowBounds);
		return rows;
	}

	public PersonSickness getPersonSickness(String id) {
		if (id == null) {
			return null;
		}
		PersonSickness personSickness = personSicknessMapper.getPersonSicknessById(id);
		return personSickness;
	}

	@Transactional
	public void save(PersonSickness personSickness) {
		if (StringUtils.isEmpty(personSickness.getId())) {
			personSickness.setId(idGenerator.getNextId("HEALTH_PERSON_SICKNESS"));
			personSickness.setCreateTime(new Date());

			Calendar calendar = Calendar.getInstance();
			if (personSickness.getConfirmTime() != null) {
				calendar.setTime(personSickness.getConfirmTime());
			} else if (personSickness.getClinicTime() != null) {
				calendar.setTime(personSickness.getClinicTime());
			} else {
				calendar.setTime(personSickness.getCreateTime());
			}
			personSickness.setYear(calendar.get(Calendar.YEAR));
			personSickness.setMonth(calendar.get(Calendar.MONTH + 1));

			personSicknessMapper.insertPersonSickness(personSickness);
		} else {
			
			Calendar calendar = Calendar.getInstance();
			if (personSickness.getConfirmTime() != null) {
				calendar.setTime(personSickness.getConfirmTime());
			} else if (personSickness.getClinicTime() != null) {
				calendar.setTime(personSickness.getClinicTime());
			} else {
				calendar.setTime(personSickness.getCreateTime());
			}
			personSickness.setYear(calendar.get(Calendar.YEAR));
			personSickness.setMonth(calendar.get(Calendar.MONTH + 1));
			
			personSicknessMapper.updatePersonSickness(personSickness);
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

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.PersonSicknessMapper")
	public void setPersonSicknessMapper(PersonSicknessMapper personSicknessMapper) {
		this.personSicknessMapper = personSicknessMapper;
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
