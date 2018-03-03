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
import com.glaf.heathcare.domain.FoodDRI;
import com.glaf.heathcare.mapper.FoodDRIMapper;
import com.glaf.heathcare.query.FoodDRIQuery;
import com.glaf.heathcare.service.FoodDRIService;
import com.glaf.heathcare.util.FoodDRIJsonFactory;

@Service("com.glaf.heathcare.service.foodDRIService")
@Transactional(readOnly = true)
public class FoodDRIServiceImpl implements FoodDRIService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected FoodDRIMapper foodDRIMapper;

	public FoodDRIServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<FoodDRI> list) {
		for (FoodDRI foodDRI : list) {
			if (foodDRI.getId() == 0) {
				foodDRI.setId(idGenerator.nextId("HEALTH_FOOD_DRI"));
			}
		}
		if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
			foodDRIMapper.bulkInsertFoodDRI_oracle(list);
		} else {
			foodDRIMapper.bulkInsertFoodDRI(list);
		}
	}

	public int count(FoodDRIQuery query) {
		return foodDRIMapper.getFoodDRICount(query);
	}

	@Transactional
	public void deleteById(Long id) {
		if (id != null) {
			foodDRIMapper.deleteFoodDRIById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				foodDRIMapper.deleteFoodDRIById(id);
			}
		}
	}

	public FoodDRI getFoodDRI(Long id) {
		if (id == null) {
			return null;
		}
		FoodDRI model = null;
		String cacheKey = "food_dri_" + id;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString("food_dri", cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					com.alibaba.fastjson.JSONObject json = JSON.parseObject(text);
					model = FoodDRIJsonFactory.jsonToObject(json);
					if (model != null) {
						return model;
					}
				} catch (Exception ex) {
				}
			}
		}
		FoodDRI foodDRI = foodDRIMapper.getFoodDRIById(id);
		if (foodDRI != null) {
			if (SystemConfig.getBoolean("use_query_cache")) {
				CacheFactory.put("food_dri", cacheKey, foodDRI.toJsonObject().toJSONString());
			}
		}
		return foodDRI;
	}

	public FoodDRI getFoodDRIByAge(int age) {
		FoodDRI model = this.getFoodDRIByAge(age, 3401L);
		return model;
	}

	public FoodDRI getFoodDRIByAge(int age, long typeId) {
		FoodDRI model = null;
		String cacheKey = "food_dria_" + age + "_" + typeId;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString("food_dria", cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					com.alibaba.fastjson.JSONObject json = JSON.parseObject(text);
					model = FoodDRIJsonFactory.jsonToObject(json);
					if (model != null) {
						return model;
					}
				} catch (Exception ex) {
				}
			}
		}

		FoodDRIQuery query = new FoodDRIQuery();
		query.age(age);
		query.typeId(typeId);
		List<FoodDRI> list = foodDRIMapper.getFoodDRIs(query);
		if (list != null && !list.isEmpty()) {
			model = list.get(0);
			if (SystemConfig.getBoolean("use_query_cache")) {
				CacheFactory.put("food_dria", cacheKey, model.toJsonObject().toJSONString());
			}
		}
		return model;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getFoodDRICountByQueryCriteria(FoodDRIQuery query) {
		return foodDRIMapper.getFoodDRICount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<FoodDRI> getFoodDRIsByQueryCriteria(int start, int pageSize, FoodDRIQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<FoodDRI> rows = sqlSessionTemplate.selectList("getFoodDRIs", query, rowBounds);
		return rows;
	}

	public List<FoodDRI> list(FoodDRIQuery query) {
		List<FoodDRI> list = foodDRIMapper.getFoodDRIs(query);
		return list;
	}

	@Transactional
	public void save(FoodDRI foodDRI) {
		if (foodDRI.getId() == 0) {
			foodDRI.setId(idGenerator.nextId("HEALTH_FOOD_DRI"));
			foodDRI.setCreateTime(new Date());
			foodDRIMapper.insertFoodDRI(foodDRI);
		} else {
			if (SystemConfig.getBoolean("use_query_cache")) {
				String cacheKey = "food_dri_" + foodDRI.getId();
				CacheFactory.remove("food_dri", cacheKey);
				cacheKey = "food_dria_" + foodDRI.getAge();
				CacheFactory.remove("food_dria", cacheKey);
			}
			foodDRI.setUpdateTime(new Date());
			foodDRIMapper.updateFoodDRI(foodDRI);
		}
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.FoodDRIMapper")
	public void setFoodDRIMapper(FoodDRIMapper foodDRIMapper) {
		this.foodDRIMapper = foodDRIMapper;
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
