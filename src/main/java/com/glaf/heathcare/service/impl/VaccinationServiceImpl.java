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

@Service("com.glaf.heathcare.service.vaccinationService")
@Transactional(readOnly = true)
public class VaccinationServiceImpl implements VaccinationService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected VaccinationMapper vaccinationMapper;

	public VaccinationServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<Vaccination> list) {
		for (Vaccination vaccination : list) {
			if (vaccination.getId() == 0) {
				vaccination.setId(idGenerator.nextId("HEALTH_VACCINATION"));
				vaccination.setCreateTime(new Date());
			}
		}

		int batch_size = 50;
		List<Vaccination> rows = new ArrayList<Vaccination>(batch_size);

		for (Vaccination bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % 100 == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					vaccinationMapper.bulkInsertVaccination_oracle(rows);
				} else {
					vaccinationMapper.bulkInsertVaccination(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				vaccinationMapper.bulkInsertVaccination_oracle(rows);
			} else {
				vaccinationMapper.bulkInsertVaccination(rows);
			}
			rows.clear();
		}
	}

	public int count(VaccinationQuery query) {
		return vaccinationMapper.getVaccinationCount(query);
	}

	@Transactional
	public void deleteById(Long id) {
		if (id != null) {
			vaccinationMapper.deleteVaccinationById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				vaccinationMapper.deleteVaccinationById(id);
			}
		}
	}

	public Vaccination getVaccination(Long id) {
		if (id == null) {
			return null;
		}
		Vaccination vaccination = vaccinationMapper.getVaccinationById(id);
		return vaccination;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getVaccinationCountByQueryCriteria(VaccinationQuery query) {
		return vaccinationMapper.getVaccinationCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<Vaccination> getVaccinationsByQueryCriteria(int start, int pageSize, VaccinationQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<Vaccination> rows = sqlSessionTemplate.selectList("getVaccinations", query, rowBounds);
		return rows;
	}

	public List<Vaccination> list(VaccinationQuery query) {
		List<Vaccination> list = vaccinationMapper.getVaccinations(query);
		return list;
	}

	@Transactional
	public void save(Vaccination vaccination) {
		if (vaccination.getId() == 0) {
			vaccination.setId(idGenerator.nextId("HEALTH_VACCINATION"));
			vaccination.setCreateTime(new Date());
			// vaccination.setDeleteFlag(0);
			vaccinationMapper.insertVaccination(vaccination);
		} else {
			vaccinationMapper.updateVaccination(vaccination);
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

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.VaccinationMapper")
	public void setVaccinationMapper(VaccinationMapper vaccinationMapper) {
		this.vaccinationMapper = vaccinationMapper;
	}

}
