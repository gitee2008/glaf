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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.DBUtils;
import com.glaf.heathcare.domain.DietaryItem;
import com.glaf.heathcare.domain.DietaryTemplate;
import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.mapper.DietaryItemMapper;
import com.glaf.heathcare.mapper.DietaryTemplateMapper;
import com.glaf.heathcare.query.DietaryItemQuery;
import com.glaf.heathcare.query.DietaryTemplateQuery;
import com.glaf.heathcare.query.FoodCompositionQuery;
import com.glaf.heathcare.service.DietaryItemService;
import com.glaf.heathcare.service.DietaryTemplateService;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.heathcare.util.DietaryTemplateJsonFactory;

@Service("com.glaf.heathcare.service.dietaryTemplateService")
@Transactional(readOnly = true)
public class DietaryTemplateServiceImpl implements DietaryTemplateService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected DietaryItemMapper dietaryItemMapper;

	protected DietaryItemService dietaryItemService;

	protected DietaryTemplateMapper dietaryTemplateMapper;

	protected FoodCompositionService foodCompositionService;

	public DietaryTemplateServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<DietaryTemplate> list) {
		CacheFactory.clear("dietary_template");
		for (DietaryTemplate dietaryTemplate : list) {
			if (dietaryTemplate.getId() == 0) {
				dietaryTemplate.setId(idGenerator.nextId("HEALTH_DIETARY_TEMPLATE"));
			}
		}
		if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
			dietaryTemplateMapper.bulkInsertDietaryTemplate_oracle(list);
		} else {
			dietaryTemplateMapper.bulkInsertDietaryTemplate(list);
		}
	}

	@Transactional
	public void calculate(long templateId) {
		if (templateId > 0) {
			DietaryTemplate dietaryTemplate = this.getDietaryTemplate(templateId);
			List<DietaryItem> items = dietaryItemService.getDietaryItemsByTemplateId(templateId);
			if (items != null && !items.isEmpty()) {
				List<Long> foodIds = new ArrayList<Long>();
				for (DietaryItem item : items) {
					if (!foodIds.contains(item.getFoodId())) {
						foodIds.add(item.getFoodId());
					}
				}

				FoodCompositionQuery query3 = new FoodCompositionQuery();
				query3.setIds(foodIds);
				List<FoodComposition> foods = foodCompositionService.list(query3);// 获取食物成分
				Map<Long, FoodComposition> foodMap = new HashMap<Long, FoodComposition>();
				for (FoodComposition food : foods) {
					foodMap.put(food.getId(), food);
				}

				dietaryTemplate.setHeatEnergy(0);
				dietaryTemplate.setProtein(0);
				dietaryTemplate.setFat(0);
				dietaryTemplate.setCarbohydrate(0);
				dietaryTemplate.setVitaminA(0);
				dietaryTemplate.setVitaminB1(0);
				dietaryTemplate.setVitaminB2(0);
				dietaryTemplate.setVitaminB6(0);
				dietaryTemplate.setVitaminB12(0);
				dietaryTemplate.setVitaminC(0);
				dietaryTemplate.setCarotene(0);
				dietaryTemplate.setRetinol(0);
				dietaryTemplate.setNicotinicCid(0);
				dietaryTemplate.setCalcium(0);
				dietaryTemplate.setIron(0);
				dietaryTemplate.setZinc(0);
				dietaryTemplate.setIodine(0);
				dietaryTemplate.setPhosphorus(0);

				for (DietaryItem item : items) {
					FoodComposition food = foodMap.get(item.getFoodId());
					/**
					 * 调味品不计入营养成分
					 */
					if (food.getNodeId() == 4419) {
						continue;
					}
					double quantity = item.getQuantity();
					// double radical = food.getRadical();
					double realQuantity = 0;
					// if (radical < 100 && radical > 0) {
					/**
					 * 计算每一份的实际量
					 */
					// realQuantity = quantity * (radical / 100);
					// } else {
					realQuantity = quantity;
					// }
					double factor = realQuantity / 100;// 转换成100g为标准
					dietaryTemplate.setHeatEnergy(dietaryTemplate.getHeatEnergy() + food.getHeatEnergy() * factor);// 累加计算
					dietaryTemplate.setProtein(dietaryTemplate.getProtein() + food.getProtein() * factor);// 累加计算
					dietaryTemplate.setFat(dietaryTemplate.getFat() + food.getFat() * factor);// 累加计算
					dietaryTemplate
							.setCarbohydrate(dietaryTemplate.getCarbohydrate() + food.getCarbohydrate() * factor);// 累加计算
					dietaryTemplate.setVitaminA(dietaryTemplate.getVitaminA() + food.getVitaminA() * factor);// 累加计算
					dietaryTemplate.setVitaminB1(dietaryTemplate.getVitaminB1() + food.getVitaminB1() * factor);// 累加计算
					dietaryTemplate.setVitaminB2(dietaryTemplate.getVitaminB2() + food.getVitaminB2() * factor);// 累加计算
					dietaryTemplate.setVitaminB6(dietaryTemplate.getVitaminB6() + food.getVitaminB6() * factor);// 累加计算
					dietaryTemplate.setVitaminB12(dietaryTemplate.getVitaminB12() + food.getVitaminB12() * factor);// 累加计算
					dietaryTemplate.setVitaminC(dietaryTemplate.getVitaminC() + food.getVitaminC() * factor);// 累加计算
					dietaryTemplate.setCarotene(dietaryTemplate.getCarotene() + food.getCarotene() * factor);// 累加计算
					dietaryTemplate.setRetinol(dietaryTemplate.getRetinol() + food.getRetinol() * factor);// 累加计算
					dietaryTemplate
							.setNicotinicCid(dietaryTemplate.getNicotinicCid() + food.getNicotinicCid() * factor);// 累加计算
					dietaryTemplate.setCalcium(dietaryTemplate.getCalcium() + food.getCalcium() * factor);// 累加计算
					dietaryTemplate.setIron(dietaryTemplate.getIron() + food.getIron() * factor);// 累加计算
					dietaryTemplate.setZinc(dietaryTemplate.getZinc() + food.getZinc() * factor);// 累加计算
					dietaryTemplate.setIodine(dietaryTemplate.getIodine() + food.getIodine() * factor);// 累加计算
					dietaryTemplate.setPhosphorus(dietaryTemplate.getPhosphorus() + food.getPhosphorus() * factor);// 累加计算
				}

				CacheFactory.clear("dietary_template");
				this.save(dietaryTemplate);
			}
		}
	}

	/**
	 * 复制食谱模板
	 * 
	 * @param loginContext
	 * @param suitNo
	 * @param sysFlag
	 * @param targetSuitNo
	 */
	@Transactional
	public void copyTemplates(LoginContext loginContext, int suitNo, String sysFlag, int targetSuitNo) {
		DietaryTemplateQuery query = new DietaryTemplateQuery();
		query.suitNo(suitNo);
		if (StringUtils.equals(sysFlag, "Y")) {
			query.sysFlag(sysFlag);
		} else {
			if (!loginContext.isSystemAdministrator()) {
				query.tenantId(loginContext.getTenantId());
			}
		}

		List<DietaryTemplate> list = this.list(query);
		if (list != null && !list.isEmpty()) {
			if (!loginContext.isSystemAdministrator()) {
				query.sysFlag("N");
				query.tenantId(loginContext.getTenantId());
			}
			query.suitNo(targetSuitNo);
			int total = this.count(query);
			logger.debug("total->" + total);
			if (total == 0) {
				List<Long> templateIds = new ArrayList<Long>();
				for (DietaryTemplate dietaryTemplate : list) {
					templateIds.add(dietaryTemplate.getId());
				}
				DietaryItemQuery query2 = new DietaryItemQuery();
				query2.templateIds(templateIds);
				List<DietaryItem> allItems = dietaryItemService.list(query2);
				if (allItems != null && !allItems.isEmpty()) {
					List<DietaryItem> items = null;

					Map<Long, List<DietaryItem>> dietaryTemplateMap = new HashMap<Long, List<DietaryItem>>();
					for (DietaryItem item : allItems) {
						items = dietaryTemplateMap.get(item.getTemplateId());
						if (items == null) {
							items = new ArrayList<DietaryItem>();
						}
						items.add(item);
						dietaryTemplateMap.put(item.getTemplateId(), items);
					}

					Date now = new Date();
					for (DietaryTemplate dietaryTemplate : list) {
						items = dietaryTemplateMap.get(dietaryTemplate.getId());
						if (items != null && !items.isEmpty()) {
							dietaryTemplate.setId(idGenerator.nextId("HEALTH_DIETARY_TEMPLATE"));
							dietaryTemplate.setCreateBy(loginContext.getActorId());
							dietaryTemplate.setCreateTime(now);
							dietaryTemplate.setEnableFlag("Y");
							dietaryTemplate.setSuitNo(targetSuitNo);
							dietaryTemplate.setUpdateBy(loginContext.getActorId());
							dietaryTemplate.setUpdateTime(now);

							if (StringUtils.equals(sysFlag, "Y")) {
								dietaryTemplate.setSysFlag(sysFlag);
							}

							if (!loginContext.isSystemAdministrator()) {
								dietaryTemplate.setSysFlag("N");
								dietaryTemplate.setTenantId(loginContext.getTenantId());
							}

							CacheFactory.clear("dietary_template");

							dietaryTemplateMapper.insertDietaryTemplate(dietaryTemplate);

							for (DietaryItem item : items) {
								item.setId(idGenerator.nextId("HEALTH_DIETARY_ITEM"));
								item.setTemplateId(dietaryTemplate.getId());
								item.setCreateBy(loginContext.getActorId());
								item.setCreateTime(now);
								if (!loginContext.isSystemAdministrator()) {
									item.setTenantId(loginContext.getTenantId());
								}
								dietaryItemMapper.insertDietaryItem(item);
							}
						}
					}
				}
			}
		}
	}

	public int count(DietaryTemplateQuery query) {
		return dietaryTemplateMapper.getDietaryTemplateCount(query);
	}

	@Transactional
	public void deleteById(long id) {
		if (id != 0) {
			CacheFactory.clear("dietary_template");
			DietaryItemQuery query = new DietaryItemQuery();
			query.templateId(id);
			dietaryItemMapper.deleteDietaryItemsByTemplateId(query);
			dietaryTemplateMapper.deleteDietaryTemplateById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			CacheFactory.clear("dietary_template");
			for (Long id : ids) {
				DietaryItemQuery query = new DietaryItemQuery();
				query.templateId(id);
				dietaryItemMapper.deleteDietaryItemsByTemplateId(query);
				dietaryTemplateMapper.deleteDietaryTemplateById(id);
			}
		}
	}

	public DietaryTemplate getDietaryTemplate(long templateId) {
		if (templateId == 0) {
			return null;
		}
		String cacheKey = "dietary_template_" + templateId;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString("dietary_template", cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					com.alibaba.fastjson.JSONObject json = JSON.parseObject(text);
					DietaryTemplate model = DietaryTemplateJsonFactory.jsonToObject(json);
					if (model != null) {
						return model;
					}
				} catch (Exception ex) {
				}
			}
		}

		DietaryTemplate dietaryTemplate = dietaryTemplateMapper.getDietaryTemplateById(templateId);
		if (dietaryTemplate != null) {
			DietaryItemQuery query = new DietaryItemQuery();
			query.templateId(templateId);
			List<DietaryItem> items = dietaryItemService.getDietaryItemsByTemplateId(templateId);
			dietaryTemplate.setItems(items);
			if (SystemConfig.getBoolean("use_query_cache")) {
				CacheFactory.put("dietary_template", cacheKey, dietaryTemplate.toJsonObject().toJSONString());
			}
		}
		return dietaryTemplate;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getDietaryTemplateCountByQueryCriteria(DietaryTemplateQuery query) {
		return dietaryTemplateMapper.getDietaryTemplateCount(query);
	}

	public List<DietaryTemplate> getDietaryTemplates(int dayOfWeek, int suitNo, String sysFlag) {
		String cacheKey = "dietary_template_" + dayOfWeek + "_" + suitNo + "_" + sysFlag;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString("dietary_template", cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONArray array = JSON.parseArray(text);
					return DietaryTemplateJsonFactory.arrayToList(array);
				} catch (Exception ex) {
				}
			}
		}

		DietaryTemplateQuery query = new DietaryTemplateQuery();
		query.dayOfWeek(dayOfWeek);
		query.suitNo(suitNo);
		query.sysFlag(sysFlag);
		List<DietaryTemplate> list = dietaryTemplateMapper.getDietaryTemplates(query);
		if (list != null && !list.isEmpty()) {
			if (SystemConfig.getBoolean("use_query_cache")) {
				JSONArray array = DietaryTemplateJsonFactory.listToArray(list);
				CacheFactory.put("dietary_template", cacheKey, array.toJSONString());
			}
		}
		return list;
	}

	public List<DietaryTemplate> getDietaryTemplates(int suitNo, String sysFlag) {
		String cacheKey = "dietary_template_" + "_" + suitNo + "_" + sysFlag;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString("dietary_template", cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONArray array = JSON.parseArray(text);
					return DietaryTemplateJsonFactory.arrayToList(array);
				} catch (Exception ex) {
				}
			}
		}

		DietaryTemplateQuery query = new DietaryTemplateQuery();
		query.suitNo(suitNo);
		query.sysFlag(sysFlag);
		List<DietaryTemplate> list = dietaryTemplateMapper.getDietaryTemplates(query);
		if (list != null && !list.isEmpty()) {
			if (SystemConfig.getBoolean("use_query_cache")) {
				JSONArray array = DietaryTemplateJsonFactory.listToArray(list);
				CacheFactory.put("dietary_template", cacheKey, array.toJSONString());
			}
		}
		return list;
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<DietaryTemplate> getDietaryTemplatesByQueryCriteria(int start, int pageSize,
			DietaryTemplateQuery query) {
		if (query.getSuitNo() != null && query.getSuitNo() > 0) {
			query.setOrderBy(" E.DAYOFWEEK_ asc, E.TYPEID_ asc, E.SORTNO_ asc ");
		}
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<DietaryTemplate> rows = sqlSessionTemplate.selectList("getDietaryTemplates", query, rowBounds);
		return rows;
	}

	public List<DietaryTemplate> list(DietaryTemplateQuery query) {
		if (query.getSuitNo() != null && query.getSuitNo() > 0) {
			query.setOrderBy(" E.DAYOFWEEK_ asc, E.TYPEID_ asc, E.SORTNO_ asc ");
		}
		List<DietaryTemplate> list = dietaryTemplateMapper.getDietaryTemplates(query);
		return list;
	}

	@Transactional
	public void save(DietaryTemplate dietaryTemplate) {
		if (dietaryTemplate.getId() == 0) {
			CacheFactory.clear("dietary_template");
			dietaryTemplate.setId(idGenerator.nextId("HEALTH_DIETARY_TEMPLATE"));
			dietaryTemplate.setCreateTime(new Date());
			dietaryTemplateMapper.insertDietaryTemplate(dietaryTemplate);
		} else {
			CacheFactory.clear("dietary_template");
			dietaryTemplate.setUpdateTime(new Date());
			dietaryTemplateMapper.updateDietaryTemplate(dietaryTemplate);
		}
	}

	@Transactional
	public void saveAs(DietaryTemplate dietaryTemplate) {
		DietaryTemplate model = this.getDietaryTemplate(dietaryTemplate.getId());
		if (model != null) {
			CacheFactory.clear("dietary_template");
			dietaryTemplate.setId(idGenerator.nextId("HEALTH_DIETARY_TEMPLATE"));
			dietaryTemplate.setCreateTime(new Date());
			dietaryTemplate.setEnableFlag("Y");
			dietaryTemplateMapper.insertDietaryTemplate(dietaryTemplate);

			if (model.getItems() != null && !model.getItems().isEmpty()) {
				for (DietaryItem item : model.getItems()) {
					item.setId(idGenerator.nextId("HEALTH_DIETARY_ITEM"));
					item.setTemplateId(dietaryTemplate.getId());
					item.setTenantId(dietaryTemplate.getTenantId());
					dietaryItemMapper.insertDietaryItem(item);
				}
			}
		}
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.DietaryItemMapper")
	public void setDietaryItemMapper(DietaryItemMapper dietaryItemMapper) {
		this.dietaryItemMapper = dietaryItemMapper;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.dietaryItemService")
	public void setDietaryItemService(DietaryItemService dietaryItemService) {
		this.dietaryItemService = dietaryItemService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.DietaryTemplateMapper")
	public void setDietaryTemplateMapper(DietaryTemplateMapper dietaryTemplateMapper) {
		this.dietaryTemplateMapper = dietaryTemplateMapper;
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

	@Transactional
	public void updateAll(List<DietaryTemplate> list) {
		if (list != null && !list.isEmpty()) {
			CacheFactory.clear("dietary_template");
			for (DietaryTemplate tpl : list) {
				dietaryTemplateMapper.updateDietaryTemplate(tpl);
			}
		}
	}

}
