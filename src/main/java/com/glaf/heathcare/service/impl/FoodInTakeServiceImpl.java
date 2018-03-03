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
import com.glaf.heathcare.service.FoodInTakeService;

@Service("com.glaf.heathcare.service.foodInTakeService")
@Transactional(readOnly = true)
public class FoodInTakeServiceImpl implements FoodInTakeService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected FoodInTakeMapper foodInTakeMapper;

	public FoodInTakeServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<FoodInTake> list) {
		for (FoodInTake foodInTake : list) {
			if (foodInTake.getId() == 0) {
				foodInTake.setId(idGenerator.nextId("HEALTH_FOOD_INTAKE"));
				foodInTake.setCreateTime(new Date());
			}
		}

		int batch_size = 100;
		List<FoodInTake> rows = new ArrayList<FoodInTake>(batch_size);

		for (FoodInTake bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % 100 == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					foodInTakeMapper.bulkInsertFoodInTake_oracle(rows);
				} else {
					foodInTakeMapper.bulkInsertFoodInTake(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				foodInTakeMapper.bulkInsertFoodInTake_oracle(rows);
			} else {
				foodInTakeMapper.bulkInsertFoodInTake(rows);
			}
			rows.clear();
		}
	}

	@Transactional
	public void deleteById(Long id) {
		if (id != null) {
			foodInTakeMapper.deleteFoodInTakeById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				foodInTakeMapper.deleteFoodInTakeById(id);
			}
		}
	}

	public int count(FoodInTakeQuery query) {
		return foodInTakeMapper.getFoodInTakeCount(query);
	}

	public List<FoodInTake> list(FoodInTakeQuery query) {
		List<FoodInTake> list = foodInTakeMapper.getFoodInTakes(query);
		return list;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getFoodInTakeCountByQueryCriteria(FoodInTakeQuery query) {
		return foodInTakeMapper.getFoodInTakeCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<FoodInTake> getFoodInTakesByQueryCriteria(int start, int pageSize, FoodInTakeQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<FoodInTake> rows = sqlSessionTemplate.selectList("getFoodInTakes", query, rowBounds);
		return rows;
	}

	public FoodInTake getFoodInTake(Long id) {
		if (id == null) {
			return null;
		}
		FoodInTake foodInTake = foodInTakeMapper.getFoodInTakeById(id);
		return foodInTake;
	}

	@Transactional
	public void save(FoodInTake foodInTake) {
		if (foodInTake.getId() == 0) {
			foodInTake.setId(idGenerator.nextId("HEALTH_FOOD_INTAKE"));
			foodInTake.setCreateTime(new Date());
			foodInTakeMapper.insertFoodInTake(foodInTake);
		} else {
			foodInTakeMapper.updateFoodInTake(foodInTake);
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

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.FoodInTakeMapper")
	public void setFoodInTakeMapper(FoodInTakeMapper foodInTakeMapper) {
		this.foodInTakeMapper = foodInTakeMapper;
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
