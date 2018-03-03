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
import com.glaf.heathcare.service.DietarySurveyService;

@Service("com.glaf.heathcare.service.dietarySurveyService")
@Transactional(readOnly = true)
public class DietarySurveyServiceImpl implements DietarySurveyService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected DietarySurveyMapper dietarySurveyMapper;

	public DietarySurveyServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<DietarySurvey> list) {
		for (DietarySurvey dietarySurvey : list) {
			if (dietarySurvey.getId() == 0) {
				dietarySurvey.setId(idGenerator.nextId("HEALTH_DIETARY_SURVEY"));
				dietarySurvey.setCreateTime(new Date());
			}
		}

		int batch_size = 100;
		List<DietarySurvey> rows = new ArrayList<DietarySurvey>(batch_size);

		for (DietarySurvey bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % 100 == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					dietarySurveyMapper.bulkInsertDietarySurvey_oracle(rows);
				} else {
					dietarySurveyMapper.bulkInsertDietarySurvey(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				dietarySurveyMapper.bulkInsertDietarySurvey_oracle(rows);
			} else {
				dietarySurveyMapper.bulkInsertDietarySurvey(rows);
			}
			rows.clear();
		}
	}

	@Transactional
	public void deleteById(Long id) {
		if (id != null) {
			dietarySurveyMapper.deleteDietarySurveyById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				dietarySurveyMapper.deleteDietarySurveyById(id);
			}
		}
	}

	public int count(DietarySurveyQuery query) {
		return dietarySurveyMapper.getDietarySurveyCount(query);
	}

	public List<DietarySurvey> list(DietarySurveyQuery query) {
		List<DietarySurvey> list = dietarySurveyMapper.getDietarySurveys(query);
		return list;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getDietarySurveyCountByQueryCriteria(DietarySurveyQuery query) {
		return dietarySurveyMapper.getDietarySurveyCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<DietarySurvey> getDietarySurveysByQueryCriteria(int start, int pageSize, DietarySurveyQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<DietarySurvey> rows = sqlSessionTemplate.selectList("getDietarySurveys", query, rowBounds);
		return rows;
	}

	public DietarySurvey getDietarySurvey(Long id) {
		if (id == null) {
			return null;
		}
		DietarySurvey dietarySurvey = dietarySurveyMapper.getDietarySurveyById(id);
		return dietarySurvey;
	}

	@Transactional
	public void save(DietarySurvey dietarySurvey) {
		if (dietarySurvey.getId() == 0) {
			dietarySurvey.setId(idGenerator.nextId("HEALTH_DIETARY_SURVEY"));
			dietarySurvey.setCreateTime(new Date());

			dietarySurveyMapper.insertDietarySurvey(dietarySurvey);
		} else {
			dietarySurveyMapper.updateDietarySurvey(dietarySurvey);
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

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.DietarySurveyMapper")
	public void setDietarySurveyMapper(DietarySurveyMapper dietarySurveyMapper) {
		this.dietarySurveyMapper = dietarySurveyMapper;
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
