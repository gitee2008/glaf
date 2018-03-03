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
import com.glaf.heathcare.domain.FoodDRIPercent;
import com.glaf.heathcare.mapper.FoodDRIPercentMapper;
import com.glaf.heathcare.query.FoodDRIPercentQuery;
import com.glaf.heathcare.service.FoodDRIPercentService;
import com.glaf.heathcare.util.FoodDRIPercentJsonFactory;

@Service("com.glaf.heathcare.service.foodDRIPercentService")
@Transactional(readOnly = true)
public class FoodDRIPercentServiceImpl implements FoodDRIPercentService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected FoodDRIPercentMapper foodDRIPercentMapper;

	public FoodDRIPercentServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<FoodDRIPercent> list) {
		for (FoodDRIPercent foodDRIPercent : list) {
			if (foodDRIPercent.getId() == 0) {
				foodDRIPercent.setId(idGenerator.nextId("HEALTH_FOOD_DRI_PERCENT"));
			}
		}
		if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
			foodDRIPercentMapper.bulkInsertFoodDRIPercent_oracle(list);
		} else {
			foodDRIPercentMapper.bulkInsertFoodDRIPercent(list);
		}
	}

	public int count(FoodDRIPercentQuery query) {
		return foodDRIPercentMapper.getFoodDRIPercentCount(query);
	}

	@Transactional
	public void deleteById(Long id) {
		if (id != null) {
			foodDRIPercentMapper.deleteFoodDRIPercentById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				foodDRIPercentMapper.deleteFoodDRIPercentById(id);
			}
		}
	}

	public FoodDRIPercent getFoodDRIPercent(Long id) {
		if (id == null) {
			return null;
		}
		String cacheKey = "food_drip_" + id;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString("food_drip", cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					com.alibaba.fastjson.JSONObject json = JSON.parseObject(text);
					FoodDRIPercent model = FoodDRIPercentJsonFactory.jsonToObject(json);
					if (model != null) {
						return model;
					}
				} catch (Exception ex) {
				}
			}
		}
		FoodDRIPercent foodDRIPercent = foodDRIPercentMapper.getFoodDRIPercentById(id);
		if (foodDRIPercent != null) {
			if (SystemConfig.getBoolean("use_query_cache")) {
				CacheFactory.put("food_drip", cacheKey, foodDRIPercent.toJsonObject().toJSONString());
			}
		}
		return foodDRIPercent;
	}

	public FoodDRIPercent getFoodDRIPercent(String ageGroup, long typeId) {
		String cacheKey = "food_drip_" + ageGroup + "_" + typeId;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString("food_drip", cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					com.alibaba.fastjson.JSONObject json = JSON.parseObject(text);
					FoodDRIPercent model = FoodDRIPercentJsonFactory.jsonToObject(json);
					if (model != null) {
						return model;
					}
				} catch (Exception ex) {
				}
			}
		}

		FoodDRIPercentQuery query = new FoodDRIPercentQuery();
		query.ageGroup(ageGroup);
		query.typeId(typeId);
		List<FoodDRIPercent> list = foodDRIPercentMapper.getFoodDRIPercents(query);
		if (list != null && !list.isEmpty()) {
			FoodDRIPercent model = list.get(0);
			if (SystemConfig.getBoolean("use_query_cache")) {
				CacheFactory.put("food_drip", cacheKey, model.toJsonObject().toJSONString());
			}
			return model;
		}
		return null;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getFoodDRIPercentCountByQueryCriteria(FoodDRIPercentQuery query) {
		return foodDRIPercentMapper.getFoodDRIPercentCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<FoodDRIPercent> getFoodDRIPercentsByQueryCriteria(int start, int pageSize, FoodDRIPercentQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<FoodDRIPercent> rows = sqlSessionTemplate.selectList("getFoodDRIPercents", query, rowBounds);
		return rows;
	}

	public List<FoodDRIPercent> list(FoodDRIPercentQuery query) {
		List<FoodDRIPercent> list = foodDRIPercentMapper.getFoodDRIPercents(query);
		return list;
	}

	@Transactional
	public void save(FoodDRIPercent foodDRIPercent) {
		if (foodDRIPercent.getId() == 0) {
			foodDRIPercent.setId(idGenerator.nextId("HEALTH_FOOD_DRI_PERCENT"));
			foodDRIPercent.setCreateTime(new Date());
			foodDRIPercentMapper.insertFoodDRIPercent(foodDRIPercent);
		} else {
			if (SystemConfig.getBoolean("use_query_cache")) {
				String cacheKey = "food_drip_" + foodDRIPercent.getId();
				CacheFactory.remove("food_drip", cacheKey);
				cacheKey = "food_drip_" + foodDRIPercent.getAgeGroup() + "_" + foodDRIPercent.getTypeId();
				CacheFactory.remove("food_drip", cacheKey);
			}
			foodDRIPercent.setUpdateTime(new Date());
			foodDRIPercentMapper.updateFoodDRIPercent(foodDRIPercent);
		}
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.FoodDRIPercentMapper")
	public void setFoodDRIPercentMapper(FoodDRIPercentMapper foodDRIPercentMapper) {
		this.foodDRIPercentMapper = foodDRIPercentMapper;
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
