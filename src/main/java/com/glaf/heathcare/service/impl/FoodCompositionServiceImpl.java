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
import com.alibaba.fastjson.JSONArray;
import com.glaf.core.cache.CacheFactory;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.util.DBUtils;
import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.mapper.FoodCompositionMapper;
import com.glaf.heathcare.query.FoodCompositionQuery;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.heathcare.util.FoodCompositionJsonFactory;

@Service("com.glaf.heathcare.service.foodCompositionService")
@Transactional(readOnly = true)
public class FoodCompositionServiceImpl implements FoodCompositionService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected FoodCompositionMapper foodCompositionMapper;

	public FoodCompositionServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<FoodComposition> list) {
		for (FoodComposition foodComposition : list) {
			if (foodComposition.getId() == 0) {
				foodComposition.setId(idGenerator.nextId("HEALTH_FOOD_COMPOSITION"));
			}
		}
		if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
			foodCompositionMapper.bulkInsertFoodComposition_oracle(list);
		} else {
			foodCompositionMapper.bulkInsertFoodComposition(list);
		}
	}

	public int count(FoodCompositionQuery query) {
		return foodCompositionMapper.getFoodCompositionCount(query);
	}

	@Transactional
	public void deleteById(Long id) {
		if (id != null) {
			foodCompositionMapper.deleteFoodCompositionById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				foodCompositionMapper.deleteFoodCompositionById(id);
			}
		}
	}

	public FoodComposition getFoodComposition(Long id) {
		if (id == null) {
			return null;
		}
		String cacheKey = "food_composition_" + id;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString("food_composition", cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					com.alibaba.fastjson.JSONObject json = JSON.parseObject(text);
					FoodComposition model = FoodCompositionJsonFactory.jsonToObject(json);
					if (model != null) {
						return model;
					}
				} catch (Exception ex) {
				}
			}
		}
		FoodComposition foodComposition = foodCompositionMapper.getFoodCompositionById(id);
		if (foodComposition != null) {
			if (SystemConfig.getBoolean("use_query_cache")) {
				CacheFactory.put("food_composition", cacheKey, foodComposition.toJsonObject().toJSONString());
			}
		}
		return foodComposition;
	}

	/**
	 * 根据名称获取成分
	 * 
	 * @param name
	 * @return
	 */
	public FoodComposition getFoodCompositionByName(String name) {
		String cacheKey = "food_composition_" + name;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString("food_composition", cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					com.alibaba.fastjson.JSONObject json = JSON.parseObject(text);
					FoodComposition model = FoodCompositionJsonFactory.jsonToObject(json);
					if (model != null) {
						return model;
					}
				} catch (Exception ex) {
				}
			}
		}
		FoodCompositionQuery query = new FoodCompositionQuery();
		query.name(name);
		List<FoodComposition> list = foodCompositionMapper.getFoodCompositions(query);
		if (list != null && !list.isEmpty()) {
			FoodComposition foodComposition = list.get(0);
			if (SystemConfig.getBoolean("use_query_cache")) {
				CacheFactory.put("food_composition", cacheKey, foodComposition.toJsonObject().toJSONString());
			}
			return foodComposition;
		}
		return null;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getFoodCompositionCountByQueryCriteria(FoodCompositionQuery query) {
		return foodCompositionMapper.getFoodCompositionCount(query);
	}

	/**
	 * 获取某个分类的食物
	 * 
	 * @param nodeId
	 * @return
	 */
	public List<FoodComposition> getFoodCompositions(long nodeId) {
		String cacheKey = "food_compositionx_" + nodeId;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString("food_composition", cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONArray array = JSON.parseArray(text);
					return FoodCompositionJsonFactory.arrayToList(array);
				} catch (Exception ex) {
				}
			}
		}

		FoodCompositionQuery query = new FoodCompositionQuery();
		query.nodeId(nodeId);
		List<FoodComposition> list = this.list(query);
		if (list != null && !list.isEmpty()) {
			if (SystemConfig.getBoolean("use_query_cache")) {
				JSONArray array = FoodCompositionJsonFactory.listToArray(list);
				CacheFactory.put("food_composition", cacheKey, array.toJSONString());
			}
		}
		return list;
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<FoodComposition> getFoodCompositionsByQueryCriteria(int start, int pageSize,
			FoodCompositionQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<FoodComposition> rows = sqlSessionTemplate.selectList("getFoodCompositions", query, rowBounds);
		return rows;
	}

	public List<FoodComposition> list(FoodCompositionQuery query) {
		List<FoodComposition> list = foodCompositionMapper.getFoodCompositions(query);
		return list;
	}

	@Transactional
	public void save(FoodComposition foodComposition) {
		if (foodComposition.getId() == 0) {
			foodComposition.setId(idGenerator.nextId("HEALTH_FOOD_COMPOSITION"));
			foodComposition.setCreateTime(new Date());
			foodComposition.setUpdateTime(new Date());
			foodCompositionMapper.insertFoodComposition(foodComposition);
		} else {
			if (SystemConfig.getBoolean("use_query_cache")) {
				String cacheKey = "food_composition_" + foodComposition.getId();
				CacheFactory.remove("food_composition", cacheKey);
				cacheKey = "food_composition_" + foodComposition.getName();
				CacheFactory.remove("food_composition", cacheKey);
				cacheKey = "food_compositionx_" + foodComposition.getNodeId();
				CacheFactory.remove("food_composition", cacheKey);
			}

			foodComposition.setUpdateTime(new Date());
			// logger.debug("-ncs:"+foodComposition.getNicotinicCid());
			foodCompositionMapper.updateFoodComposition(foodComposition);
		}
	}

	@Transactional
	public void saveAll(List<FoodComposition> list) {
		for (FoodComposition foodComposition : list) {
			FoodComposition model = foodCompositionMapper.getFoodCompositionByName(foodComposition.getName());
			if (model == null) {
				foodComposition.setId(idGenerator.nextId("HEALTH_FOOD_COMPOSITION"));
				foodComposition.setCreateTime(new Date());
				foodComposition.setUpdateTime(new Date());
				foodCompositionMapper.insertFoodComposition(foodComposition);
			}
		}
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.FoodCompositionMapper")
	public void setFoodCompositionMapper(FoodCompositionMapper foodCompositionMapper) {
		this.foodCompositionMapper = foodCompositionMapper;
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
