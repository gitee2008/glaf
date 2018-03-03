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
import com.glaf.core.security.Authentication;
import com.glaf.core.util.UUID32;
import com.glaf.heathcare.domain.Person;
import com.glaf.heathcare.domain.PersonLinkman;
import com.glaf.heathcare.mapper.PersonMapper;
import com.glaf.heathcare.query.PersonQuery;
import com.glaf.heathcare.service.PersonLinkmanService;
import com.glaf.heathcare.service.PersonService;

@Service("com.glaf.heathcare.service.personService")
@Transactional(readOnly = true)
public class PersonServiceImpl implements PersonService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected PersonMapper personMapper;

	protected PersonLinkmanService personLinkmanService;

	public PersonServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<Person> list) {
		for (Person person : list) {
			this.save(person);
		}
	}

	public int count(PersonQuery query) {
		return personMapper.getPersonCount(query);
	}

	public int getGradePersonCount(String gradeId) {
		PersonQuery query = new PersonQuery();
		query.gradeId(gradeId);
		return personMapper.getPersonCount(query);
	}

	public Person getPerson(String id) {
		if (id == null) {
			return null;
		}
		Person person = personMapper.getPersonById(id);
		return person;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getPersonCountByQueryCriteria(PersonQuery query) {
		return personMapper.getPersonCount(query);
	}

	/**
	 * 根据查询参数获取记录列表
	 * 
	 * @return
	 */
	public List<Person> getPersons(String gradeId) {
		PersonQuery query = new PersonQuery();
		query.gradeId(gradeId);
		query.deleteFlag(0);
		List<Person> list = personMapper.getPersons(query);
		return list;
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<Person> getPersonsByQueryCriteria(int start, int pageSize, PersonQuery query) {
		logger.debug("pageSize:" + pageSize);
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<Person> rows = sqlSessionTemplate.selectList("getPersons", query, rowBounds);
		return rows;
	}

	/**
	 * 根据查询参数获取记录列表
	 * 
	 * @return
	 */
	public List<Person> getTenantPersons(String tenantId) {
		PersonQuery query = new PersonQuery();
		query.tenantId(tenantId);
		query.deleteFlag(0);
		List<Person> list = personMapper.getPersons(query);
		return list;
	}

	public List<Person> list(PersonQuery query) {
		List<Person> list = personMapper.getPersons(query);
		return list;
	}

	@Transactional
	public void save(Person person) {
		Calendar calendar = Calendar.getInstance();
		if (person.getBirthday() != null) {
			calendar.setTime(person.getBirthday());
			person.setYear(calendar.get(Calendar.YEAR));
		}
		if (StringUtils.isNotEmpty(person.getId())) {
			Person model = personMapper.getPersonById(person.getId());
			if (model != null) {
				person.setUpdateTime(new Date());
				personMapper.updatePerson(person);
			} else {
				person.setId(UUID32.getUUID());
				person.setCreateTime(new Date());
				personMapper.insertPerson(person);
			}
		} else {
			if (StringUtils.isEmpty(person.getId())) {
				person.setId(UUID32.getUUID());
				person.setCreateTime(new Date());
				personMapper.insertPerson(person);
			} else {
				person.setUpdateTime(new Date());
				personMapper.updatePerson(person);
			}
		}

		List<PersonLinkman> linkmans = personLinkmanService.getLinkmans(person.getId());

		if (StringUtils.isNotEmpty(person.getFather())) {
			boolean hasF = false;
			if (linkmans != null && !linkmans.isEmpty()) {
				for (PersonLinkman linkman : linkmans) {
					if (StringUtils.equals(linkman.getRelationship(), "father")) {
						linkman.setTenantId(person.getTenantId());
						linkman.setName(person.getFather());
						linkman.setCompany(person.getFatherCompany());
						linkman.setMobile(person.getFatherTelephone());
						linkman.setWardship(person.getFatherWardship());
						linkman.setRelationship("father");
						linkman.setUpdateBy(Authentication.getAuthenticatedActorId());
						linkman.setUpdateTime(new Date());
						personLinkmanService.save(linkman);
						hasF = true;
						break;
					}
				}
			}
			if (!hasF) {
				PersonLinkman linkman = new PersonLinkman();
				linkman.setTenantId(person.getTenantId());
				linkman.setPersonId(person.getId());
				linkman.setName(person.getFather());
				linkman.setCompany(person.getFatherCompany());
				linkman.setMobile(person.getFatherTelephone());
				linkman.setWardship(person.getFatherWardship());
				linkman.setRelationship("father");
				linkman.setCreateTime(new Date());
				linkman.setCreateBy(Authentication.getAuthenticatedActorId());
				personLinkmanService.save(linkman);
			}
		}

		if (StringUtils.isNotEmpty(person.getMother())) {
			boolean hasM = false;
			if (linkmans != null && !linkmans.isEmpty()) {
				for (PersonLinkman linkman : linkmans) {
					if (StringUtils.equals(linkman.getRelationship(), "mother")) {
						linkman.setTenantId(person.getTenantId());
						linkman.setName(person.getMother());
						linkman.setCompany(person.getMotherCompany());
						linkman.setMobile(person.getMotherTelephone());
						linkman.setWardship(person.getMotherWardship());
						linkman.setRelationship("mother");
						linkman.setUpdateBy(Authentication.getAuthenticatedActorId());
						linkman.setUpdateTime(new Date());
						personLinkmanService.save(linkman);
						hasM = true;
						break;
					}
				}
			}
			if (!hasM) {
				PersonLinkman linkman = new PersonLinkman();
				linkman.setTenantId(person.getTenantId());
				linkman.setPersonId(person.getId());
				linkman.setName(person.getMother());
				linkman.setCompany(person.getMotherCompany());
				linkman.setMobile(person.getMotherTelephone());
				linkman.setWardship(person.getMotherWardship());
				linkman.setRelationship("mother");
				linkman.setCreateTime(new Date());
				linkman.setCreateBy(Authentication.getAuthenticatedActorId());
				personLinkmanService.save(linkman);
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

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.personLinkmanService")
	public void setPersonLinkmanService(PersonLinkmanService personLinkmanService) {
		this.personLinkmanService = personLinkmanService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.PersonMapper")
	public void setPersonMapper(PersonMapper personMapper) {
		this.personMapper = personMapper;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@Transactional
	public void update(Person person) {
		person.setUpdateTime(new Date());
		personMapper.updatePerson(person);
	}

}
