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

import com.alibaba.fastjson.JSON;
import com.glaf.core.cache.CacheFactory;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.util.DBUtils;
import com.glaf.heathcare.domain.FoodADI;
import com.glaf.heathcare.mapper.FoodADIMapper;
import com.glaf.heathcare.query.FoodADIQuery;
import com.glaf.heathcare.service.FoodADIService;
import com.glaf.heathcare.util.FoodADIJsonFactory;

@Service("com.glaf.heathcare.service.foodADIService")
@Transactional(readOnly = true)
public class FoodADIServiceImpl implements FoodADIService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected FoodADIMapper foodADIMapper;

	public FoodADIServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<FoodADI> list) {
		for (FoodADI foodADI : list) {
			if (foodADI.getId() == 0) {
				foodADI.setId(idGenerator.nextId("HEALTH_FOOD_ADI"));
			}
		}
		if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
			foodADIMapper.bulkInsertFoodADI_oracle(list);
		} else {
			foodADIMapper.bulkInsertFoodADI(list);
		}
	}

	public int count(FoodADIQuery query) {
		return foodADIMapper.getFoodADICount(query);
	}

	@Transactional
	public void deleteById(Long id) {
		if (id != null) {
			foodADIMapper.deleteFoodADIById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				foodADIMapper.deleteFoodADIById(id);
			}
		}
	}

	public FoodADI getFoodADI(Long id) {
		if (id == null) {
			return null;
		}
		String cacheKey = "food_adi_" + id;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString("food_adi", cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					com.alibaba.fastjson.JSONObject json = JSON.parseObject(text);
					FoodADI model = FoodADIJsonFactory.jsonToObject(json);
					if (model != null) {
						return model;
					}
				} catch (Exception ex) {
				}
			}
		}
		FoodADI foodADI = foodADIMapper.getFoodADIById(id);
		if (foodADI != null) {
			if (SystemConfig.getBoolean("use_query_cache")) {
				CacheFactory.put("food_adi", cacheKey, foodADI.toJsonObject().toJSONString());
			}
		}
		return foodADI;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getFoodADICountByQueryCriteria(FoodADIQuery query) {
		return foodADIMapper.getFoodADICount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<FoodADI> getFoodADIsByQueryCriteria(int start, int pageSize, FoodADIQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<FoodADI> rows = sqlSessionTemplate.selectList("getFoodADIs", query, rowBounds);
		return rows;
	}

	public List<FoodADI> list(FoodADIQuery query) {
		List<FoodADI> list = foodADIMapper.getFoodADIs(query);
		return list;
	}

	@Transactional
	public void save(FoodADI foodADI) {
		if (foodADI.getId() == 0) {
			foodADI.setId(idGenerator.nextId("HEALTH_FOOD_ADI"));
			foodADI.setCreateTime(new Date());
			foodADIMapper.insertFoodADI(foodADI);
		} else {
			String cacheKey = "food_adi_" + foodADI.getId();
			CacheFactory.remove("food_adi", cacheKey);
			foodADI.setUpdateTime(new Date());
			foodADIMapper.updateFoodADI(foodADI);
		}
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.FoodADIMapper")
	public void setFoodADIMapper(FoodADIMapper foodADIMapper) {
		this.foodADIMapper = foodADIMapper;
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
