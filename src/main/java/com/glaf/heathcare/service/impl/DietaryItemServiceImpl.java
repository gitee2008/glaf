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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.glaf.core.base.ListModel;
import com.glaf.core.cache.CacheFactory;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.util.DBUtils;
import com.glaf.heathcare.domain.DietaryItem;
import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.mapper.DietaryItemMapper;
import com.glaf.heathcare.query.DietaryItemQuery;
import com.glaf.heathcare.query.FoodCompositionQuery;
import com.glaf.heathcare.service.DietaryItemService;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.heathcare.util.DietaryItemJsonFactory;

@Service("com.glaf.heathcare.service.dietaryItemService")
@Transactional(readOnly = true)
public class DietaryItemServiceImpl implements DietaryItemService {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected DietaryItemMapper dietaryItemMapper;

	protected FoodCompositionService foodCompositionService;

	public DietaryItemServiceImpl() {

	}

	@Transactional
	public void bulkInsert(String tenantId, List<DietaryItem> list) {
		FoodCompositionQuery query = new FoodCompositionQuery();
		query.locked(0);
		List<FoodComposition> foods = foodCompositionService.list(query);
		Map<Long, Long> typeIdMap = new HashMap<Long, Long>();
		for (FoodComposition fd : foods) {
			typeIdMap.put(fd.getId(), fd.getNodeId());
		}
		long lastModified = System.currentTimeMillis();
		for (DietaryItem dietaryItem : list) {
			if (StringUtils.isNotEmpty(tenantId)) {
				dietaryItem.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
			}
			if (dietaryItem.getId() == 0) {
				dietaryItem.setId(idGenerator.nextId("HEALTH_DIETARY_ITEM"));
			}
			dietaryItem.setTypeId(typeIdMap.get(dietaryItem.getFoodId()));
			dietaryItem.setLastModified(lastModified);
		}
		if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
			dietaryItemMapper.bulkInsertDietaryItem_oracle(list);
		} else {
			ListModel listModel = new ListModel();
			if (StringUtils.isNotEmpty(tenantId)) {
				listModel.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
			}
			listModel.setList(list);
			dietaryItemMapper.bulkInsertDietaryItem(listModel);
		}
		CacheFactory.clear("dietary_template");
		CacheFactory.clear("health_di_count");
		CacheFactory.clear("health_di_list");
	}

	@Transactional
	public void deleteById(long id) {
		if (id != 0) {
			DietaryItem dietaryItem = this.getDietaryItem(id);
			if (dietaryItem != null) {
				CacheFactory.clear("dietary_template");
				CacheFactory.clear("health_di_count");
				CacheFactory.clear("health_di_list");

				DietaryItemQuery query = new DietaryItemQuery();
				query.setId(id);
				query.setTableSuffix("");
				dietaryItemMapper.deleteDietaryItemById(query);
			}
		}
	}

	@Transactional
	public void deleteById(String tenantId, long id) {
		if (id != 0) {
			DietaryItemQuery query = new DietaryItemQuery();
			query.setId(id);
			if (StringUtils.isNotEmpty(tenantId)) {
				query.tenantId(tenantId);
				query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
			}

			CacheFactory.clear("dietary_template");
			CacheFactory.clear("health_di_count");
			CacheFactory.clear("health_di_list");

			dietaryItemMapper.deleteDietaryItemById(query);
		}
	}

	@Transactional
	public void deleteByIds(String tenantId, List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			CacheFactory.clear("dietary_template");
			CacheFactory.clear("health_di_count");
			CacheFactory.clear("health_di_list");
			for (Long id : ids) {
				DietaryItemQuery query = new DietaryItemQuery();
				query.setId(id);
				if (StringUtils.isNotEmpty(tenantId)) {
					query.tenantId(tenantId);
					query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
				}
				dietaryItemMapper.deleteDietaryItemById(query);
			}
		}
	}

	/**
	 * 根据主键获取一条记录
	 * 
	 * @return
	 */
	public DietaryItem getDietaryItem(long id) {
		if (id == 0) {
			return null;
		}
		DietaryItemQuery query = new DietaryItemQuery();
		query.setId(id);
		query.setTableSuffix("");
		DietaryItem dietaryItem = dietaryItemMapper.getDietaryItemById(query);
		return dietaryItem;
	}

	public DietaryItem getDietaryItem(String tenantId, long id) {
		if (id == 0) {
			return null;
		}
		DietaryItemQuery query = new DietaryItemQuery();
		query.setId(id);
		if (StringUtils.isNotEmpty(tenantId)) {
			query.tenantId(tenantId);
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
		}
		DietaryItem dietaryItem = dietaryItemMapper.getDietaryItemById(query);
		return dietaryItem;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getDietaryItemCountByQueryCriteria(DietaryItemQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		StringBuilder buffer = new StringBuilder();
		if (query.getDietaryId() == null && query.getTenantId() == null) {
			if (query.getTemplateId() != null && query.getTemplateId() > 0) {
				buffer.append("health_di_count_").append(query.getTemplateId());
				if (CacheFactory.getString("health_di_count", buffer.toString()) != null) {
					return Integer.parseInt(CacheFactory.getString("health_di_count", buffer.toString()));
				}
			}
		}
		int count = dietaryItemMapper.getDietaryItemCount(query);
		if (buffer.length() > 0) {
			CacheFactory.put("health_di_count", buffer.toString(), String.valueOf(count));
		}
		return count;
	}

	public List<DietaryItem> getDietaryItemsByDietaryId(String tenantId, long dietaryId) {
		DietaryItemQuery query = new DietaryItemQuery();
		query.dietaryId(dietaryId);
		if (StringUtils.isNotEmpty(tenantId)) {
			query.tenantId(tenantId);
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
		}
		return dietaryItemMapper.getDietaryItemsByDietaryId(query);
	}

	public List<DietaryItem> getDietaryItemsByTemplateId(long templateId) {
		StringBuilder buffer = new StringBuilder();
		buffer.append("health_di_list_").append(templateId);
		String cacheKey = buffer.toString();
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString("health_di_list", cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONArray array = JSON.parseArray(text);
					return DietaryItemJsonFactory.arrayToList(array);
				} catch (Exception ex) {
				}
			}
		}

		DietaryItemQuery query = new DietaryItemQuery();
		query.templateId(templateId);
		List<DietaryItem> list = dietaryItemMapper.getDietaryItemsByTemplateId(query);
		if (SystemConfig.getBoolean("use_query_cache")) {
			CacheFactory.put("health_di_list", cacheKey, DietaryItemJsonFactory.listToArray(list).toJSONString());
		}
		return list;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getDietaryTemplateItemCountByQueryCriteria(DietaryItemQuery query) {
		StringBuilder buffer = new StringBuilder();
		if (query.getDietaryId() == null && query.getTenantId() == null) {
			if (query.getTemplateId() != null && query.getTemplateId() > 0) {
				buffer.append("health_di_count_").append(query.getTemplateId());
				if (CacheFactory.getString("health_di_count", buffer.toString()) != null) {
					return Integer.parseInt(CacheFactory.getString("health_di_count", buffer.toString()));
				}
			}
		}
		int count = dietaryItemMapper.getDietaryItemCount(query);
		if (buffer.length() > 0) {
			CacheFactory.put("health_di_count", buffer.toString(), String.valueOf(count));
		}
		return count;
	}

	public List<DietaryItem> list(DietaryItemQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		StringBuilder buffer = new StringBuilder();
		if (query.getDietaryId() == null && query.getTenantId() == null) {
			if (query.getTemplateId() != null && query.getTemplateId() > 0) {
				buffer.append("health_di_list_").append(query.getTemplateId());
				String cacheKey = buffer.toString();
				if (SystemConfig.getBoolean("use_query_cache")) {
					String text = CacheFactory.getString("health_di_list", cacheKey);
					if (StringUtils.isNotEmpty(text)) {
						try {
							JSONArray array = JSON.parseArray(text);
							return DietaryItemJsonFactory.arrayToList(array);
						} catch (Exception ex) {
						}
					}
				}
			}
		}
		List<DietaryItem> list = dietaryItemMapper.getDietaryItems(query);
		if (buffer.length() > 0) {
			if (SystemConfig.getBoolean("use_query_cache")) {
				String cacheKey = buffer.toString();
				CacheFactory.put("health_di_list", cacheKey, DietaryItemJsonFactory.listToArray(list).toJSONString());
			}
		}
		return list;
	}

	@Transactional
	public void save(DietaryItem dietaryItem) {
		if (dietaryItem.getFoodId() > 0) {
			FoodComposition foodComposition = foodCompositionService.getFoodComposition(dietaryItem.getFoodId());
			if (foodComposition != null) {
				dietaryItem.setFoodName(foodComposition.getName());
				dietaryItem.setTypeId(foodComposition.getNodeId());
			}
		}
		if (StringUtils.isNotEmpty(dietaryItem.getTenantId())) {
			dietaryItem.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(dietaryItem.getTenantId())));
		}
		if (dietaryItem.getId() == 0) {
			dietaryItem.setId(idGenerator.nextId("HEALTH_DIETARY_ITEM"));
			dietaryItem.setCreateTime(new Date());
			dietaryItem.setLastModified(System.currentTimeMillis());
			dietaryItemMapper.insertDietaryItem(dietaryItem);
		} else {
			dietaryItem.setLastModified(System.currentTimeMillis());
			dietaryItemMapper.updateDietaryItem(dietaryItem);
		}
		CacheFactory.clear("dietary_template");
		CacheFactory.clear("health_di_count");
		CacheFactory.clear("health_di_list");
		StringBuilder buffer = new StringBuilder();
		buffer.append("health_di_list_").append(dietaryItem.getTemplateId());
		CacheFactory.remove("health_di_list", buffer.toString());
	}

	@Transactional
	public void saveTemplateItem(DietaryItem dietaryItem) {
		if (dietaryItem.getFoodId() > 0) {
			FoodComposition foodComposition = foodCompositionService.getFoodComposition(dietaryItem.getFoodId());
			if (foodComposition != null) {
				dietaryItem.setFoodName(foodComposition.getName());
				dietaryItem.setTypeId(foodComposition.getNodeId());
			}
		}
		if (dietaryItem.getId() == 0) {
			dietaryItem.setId(idGenerator.nextId("HEALTH_DIETARY_ITEM"));
			dietaryItem.setCreateTime(new Date());
			dietaryItem.setLastModified(System.currentTimeMillis());
			dietaryItemMapper.insertDietaryItem(dietaryItem);
		} else {
			dietaryItem.setLastModified(System.currentTimeMillis());
			dietaryItemMapper.updateDietaryItem(dietaryItem);
		}
		CacheFactory.clear("dietary_template");
		CacheFactory.clear("health_di_count");
		CacheFactory.clear("health_di_list");
		StringBuilder buffer = new StringBuilder();
		buffer.append("health_di_list_").append(dietaryItem.getTemplateId());
		CacheFactory.remove("health_di_list", buffer.toString());
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.DietaryItemMapper")
	public void setDietaryItemMapper(DietaryItemMapper dietaryItemMapper) {
		this.dietaryItemMapper = dietaryItemMapper;
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.foodCompositionService")
	public void setFoodCompositionService(FoodCompositionService foodCompositionService) {
		this.foodCompositionService = foodCompositionService;
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
