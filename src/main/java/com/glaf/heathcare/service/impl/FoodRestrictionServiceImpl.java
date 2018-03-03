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
import com.glaf.heathcare.service.FoodRestrictionService;

@Service("com.glaf.heathcare.service.foodRestrictionService")
@Transactional(readOnly = true)
public class FoodRestrictionServiceImpl implements FoodRestrictionService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected FoodRestrictionMapper foodRestrictionMapper;

	public FoodRestrictionServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<FoodRestriction> list) {
		for (FoodRestriction foodRestriction : list) {
			if (foodRestriction.getId() == 0) {
				foodRestriction.setId(idGenerator.nextId("HEALTH_FOOD_RESTRICTION"));
			}
		}

		int batch_size = 50;
		List<FoodRestriction> rows = new ArrayList<FoodRestriction>(batch_size);

		for (FoodRestriction bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					foodRestrictionMapper.bulkInsertFoodRestriction_oracle(rows);
				} else {
					foodRestrictionMapper.bulkInsertFoodRestriction(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				foodRestrictionMapper.bulkInsertFoodRestriction_oracle(rows);
			} else {
				foodRestrictionMapper.bulkInsertFoodRestriction(rows);
			}
			rows.clear();
		}
	}

	public int count(FoodRestrictionQuery query) {
		return foodRestrictionMapper.getFoodRestrictionCount(query);
	}

	@Transactional
	public void deleteById(long id) {
		if (id != 0) {
			foodRestrictionMapper.deleteFoodRestrictionById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				foodRestrictionMapper.deleteFoodRestrictionById(id);
			}
		}
	}

	public FoodRestriction getFoodRestriction(long id) {
		if (id == 0) {
			return null;
		}
		FoodRestriction foodRestriction = foodRestrictionMapper.getFoodRestrictionById(id);
		return foodRestriction;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getFoodRestrictionCountByQueryCriteria(FoodRestrictionQuery query) {
		return foodRestrictionMapper.getFoodRestrictionCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<FoodRestriction> getFoodRestrictionsByQueryCriteria(int start, int pageSize,
			FoodRestrictionQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<FoodRestriction> rows = sqlSessionTemplate.selectList("getFoodRestrictions", query, rowBounds);
		return rows;
	}

	public List<FoodRestriction> list(FoodRestrictionQuery query) {
		List<FoodRestriction> list = foodRestrictionMapper.getFoodRestrictions(query);
		return list;
	}

	@Transactional
	public void save(FoodRestriction foodRestriction) {
		if (foodRestriction.getId() == 0) {
			foodRestriction.setId(idGenerator.nextId("HEALTH_FOOD_RESTRICTION"));
			foodRestriction.setCreateTime(new Date());

			foodRestrictionMapper.insertFoodRestriction(foodRestriction);
		} else {
			foodRestriction.setUpdateTime(new Date());
			foodRestrictionMapper.updateFoodRestriction(foodRestriction);
		}
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.FoodRestrictionMapper")
	public void setFoodRestrictionMapper(FoodRestrictionMapper foodRestrictionMapper) {
		this.foodRestrictionMapper = foodRestrictionMapper;
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

}
