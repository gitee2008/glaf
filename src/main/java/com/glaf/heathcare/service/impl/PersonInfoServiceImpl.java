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
import com.glaf.core.util.UUID32;
import com.glaf.heathcare.domain.PersonInfo;
import com.glaf.heathcare.mapper.PersonInfoMapper;
import com.glaf.heathcare.query.PersonInfoQuery;
import com.glaf.heathcare.service.PersonInfoService;

@Service("com.glaf.heathcare.service.personInfoService")
@Transactional(readOnly = true)
public class PersonInfoServiceImpl implements PersonInfoService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected PersonInfoMapper personInfoMapper;

	public PersonInfoServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<PersonInfo> list) {
		for (PersonInfo personInfo : list) {
			if (StringUtils.isEmpty(personInfo.getId())) {
				personInfo.setId(UUID32.getUUID());
				personInfo.setCreateTime(new Date());
			}
		}

		int batch_size = 50;
		List<PersonInfo> rows = new ArrayList<PersonInfo>(batch_size);

		for (PersonInfo bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					personInfoMapper.bulkInsertPersonInfo_oracle(rows);
				} else {
					personInfoMapper.bulkInsertPersonInfo(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				personInfoMapper.bulkInsertPersonInfo_oracle(rows);
			} else {
				personInfoMapper.bulkInsertPersonInfo(rows);
			}
			rows.clear();
		}
	}

	public int count(PersonInfoQuery query) {
		return personInfoMapper.getPersonInfoCount(query);
	}

	@Transactional
	public void deleteById(String id) {
		if (id != null) {
			personInfoMapper.deletePersonInfoById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (String id : ids) {
				personInfoMapper.deletePersonInfoById(id);
			}
		}
	}

	public PersonInfo getPersonInfo(String id) {
		if (id == null) {
			return null;
		}
		PersonInfo personInfo = personInfoMapper.getPersonInfoById(id);
		return personInfo;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getPersonInfoCountByQueryCriteria(PersonInfoQuery query) {
		return personInfoMapper.getPersonInfoCount(query);
	}

	public List<PersonInfo> getPersonInfos(String tenantId) {
		PersonInfoQuery query = new PersonInfoQuery();
		query.tenantId(tenantId);
		List<PersonInfo> list = personInfoMapper.getPersonInfos(query);
		return list;
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<PersonInfo> getPersonInfosByQueryCriteria(int start, int pageSize, PersonInfoQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<PersonInfo> rows = sqlSessionTemplate.selectList("getPersonInfos", query, rowBounds);
		return rows;
	}

	public List<PersonInfo> list(PersonInfoQuery query) {
		List<PersonInfo> list = personInfoMapper.getPersonInfos(query);
		return list;
	}

	@Transactional
	public void save(PersonInfo personInfo) {
		if (StringUtils.isEmpty(personInfo.getId())) {
			personInfo.setId(UUID32.getUUID());
			personInfo.setCreateTime(new Date());
			personInfoMapper.insertPersonInfo(personInfo);
		} else {
			personInfoMapper.updatePersonInfo(personInfo);
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

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.PersonInfoMapper")
	public void setPersonInfoMapper(PersonInfoMapper personInfoMapper) {
		this.personInfoMapper = personInfoMapper;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

}
