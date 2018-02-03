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

package com.glaf.sms.service;

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

import com.glaf.sms.domain.SmsPerson;
import com.glaf.sms.mapper.SmsPersonMapper;
import com.glaf.sms.query.SmsPersonQuery;

@Service("com.glaf.sms.service.smsPersonService")
@Transactional(readOnly = true)
public class SmsPersonServiceImpl implements SmsPersonService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected SmsPersonMapper smsPersonMapper;

	public SmsPersonServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<SmsPerson> list) {
		for (SmsPerson smsPerson : list) {
			if (StringUtils.isEmpty(smsPerson.getId())) {
				smsPerson.setId(smsPerson.getMobile());
				smsPerson.setCreateTime(new Date());
			}
		}

		int batch_size = 50;
		List<SmsPerson> rows = new ArrayList<SmsPerson>(batch_size);

		for (SmsPerson bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					smsPersonMapper.bulkInsertSmsPerson_oracle(rows);
				} else {
					smsPersonMapper.bulkInsertSmsPerson(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				smsPersonMapper.bulkInsertSmsPerson_oracle(rows);
			} else {
				smsPersonMapper.bulkInsertSmsPerson(rows);
			}
			rows.clear();
		}
	}

	public int count(SmsPersonQuery query) {
		return smsPersonMapper.getSmsPersonCount(query);
	}

	@Transactional
	public void deleteById(String id) {
		if (id != null) {
			smsPersonMapper.deleteSmsPersonById(id);
		}
	}

	/**
	 * 根据clientId删除记录
	 * 
	 * @return
	 */
	@Transactional
	public void deleteByClientId(String clientId) {
		smsPersonMapper.deleteByClientId(clientId);
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (String id : ids) {
				smsPersonMapper.deleteSmsPersonById(id);
			}
		}
	}

	public SmsPerson getSmsPerson(String id) {
		if (id == null) {
			return null;
		}
		SmsPerson smsPerson = smsPersonMapper.getSmsPersonById(id);
		return smsPerson;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getSmsPersonCountByQueryCriteria(SmsPersonQuery query) {
		return smsPersonMapper.getSmsPersonCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<SmsPerson> getSmsPersonsByQueryCriteria(int start, int pageSize, SmsPersonQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<SmsPerson> rows = sqlSessionTemplate.selectList("getSmsPersons", query, rowBounds);
		return rows;
	}

	public List<SmsPerson> list(SmsPersonQuery query) {
		List<SmsPerson> list = smsPersonMapper.getSmsPersons(query);
		return list;
	}

	@Transactional
	public void save(SmsPerson smsPerson) {
		if (StringUtils.isEmpty(smsPerson.getId())) {
			smsPerson.setId(smsPerson.getMobile());
			smsPerson.setCreateTime(new Date());
			smsPersonMapper.insertSmsPerson(smsPerson);
		} else {
			smsPersonMapper.updateSmsPerson(smsPerson);
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

	@javax.annotation.Resource(name = "com.glaf.sms.mapper.SmsPersonMapper")
	public void setSmsPersonMapper(SmsPersonMapper smsPersonMapper) {
		this.smsPersonMapper = smsPersonMapper;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

}
