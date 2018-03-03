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
import java.util.Calendar;
import java.util.Collection;
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

import com.glaf.core.base.ColumnModel;
import com.glaf.core.base.ListModel;
import com.glaf.core.base.TableModel;
import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.service.ITableDataService;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.DateUtils;
import com.glaf.heathcare.SysConfig;
import com.glaf.heathcare.domain.Dietary;
import com.glaf.heathcare.domain.DietaryItem;
import com.glaf.heathcare.domain.DietaryTemplate;
import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.domain.GoodsPurchasePlan;
import com.glaf.heathcare.mapper.DietaryItemMapper;
import com.glaf.heathcare.mapper.DietaryMapper;
import com.glaf.heathcare.mapper.DietaryTemplateMapper;
import com.glaf.heathcare.query.DietaryItemQuery;
import com.glaf.heathcare.query.DietaryQuery;
import com.glaf.heathcare.query.FoodCompositionQuery;
import com.glaf.heathcare.service.DietaryItemService;
import com.glaf.heathcare.service.DietaryService;
import com.glaf.heathcare.service.DietaryTemplateService;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.heathcare.service.GoodsPurchasePlanService;
import com.glaf.heathcare.util.DietaryDomainFactory;

@Service("com.glaf.heathcare.service.dietaryService")
@Transactional(readOnly = true)
public class DietaryServiceImpl implements DietaryService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected DietaryMapper dietaryMapper;

	protected DietaryItemMapper dietaryItemMapper;

	protected DietaryTemplateMapper dietaryTemplateMapper;

	protected DietaryItemService dietaryItemService;

	protected DietaryTemplateService dietaryTemplateService;

	protected ITableDataService tableDataService;

	protected GoodsPurchasePlanService goodsPurchasePlanService;

	protected FoodCompositionService foodCompositionService;

	public DietaryServiceImpl() {

	}

	@Transactional
	public void batchPurchase(String tenantId, Collection<Long> ids, String purchaseFlag, List<GoodsPurchasePlan> list) {
		/**
		 * 批量插入采购单
		 */
		goodsPurchasePlanService.bulkInsert(tenantId, list);

		/**
		 * 修改食谱单状态为已经加入采购
		 */
		TableModel table = new TableModel();
		table.setTableName("HEALTH_DIETARY" + IdentityFactory.getTenantHash(tenantId));
		table.addIntegerColumn("BUSINESSSTATUS_", 9);
		table.addStringColumn("PURCHASEFLAG_", purchaseFlag);

		ColumnModel idColumn = new ColumnModel();
		for (Long id : ids) {
			idColumn.setColumnName("ID_");
			idColumn.setJavaType("Long");
			idColumn.setValue(id);
			table.setIdColumn(idColumn);
			tableDataService.updateTableData(table);
		}

	}

	@Transactional
	public void bulkInsert(String tenantId, Date date, List<Dietary> list) {
		List<TableModel> tableModels = new ArrayList<TableModel>();
		for (Dietary dietary : list) {
			if (dietary.getId() == 0) {
				dietary.setId(idGenerator.nextId("HEALTH_DIETARY"));
				TableModel table = new TableModel();
				table.addLongColumn("DIETARYID_", dietary.getId());
				if (StringUtils.isNotEmpty(tenantId)) {
					dietary.setTenantId(tenantId);
					table.setTableName("HEALTH_DIETARY_ITEM" + IdentityFactory.getTenantHash(tenantId));
					table.addStringColumn("TENANTID_", tenantId);
				} else {
					table.setTableName("HEALTH_DIETARY_ITEM");
				}

				ColumnModel idColumn = new ColumnModel();
				idColumn.setColumnName("TEMPLATEID_");
				idColumn.setJavaType("Long");
				idColumn.setValue(dietary.getTemplateId());
				table.setIdColumn(idColumn);
				tableModels.add(table);
			}
		}

		int batch_size = 50;
		List<Dietary> rows = new ArrayList<Dietary>(batch_size);

		for (Dietary bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					dietaryMapper.bulkInsertDietary_oracle(rows);
				} else {
					ListModel listModel = new ListModel();
					if (StringUtils.isNotEmpty(tenantId)) {
						listModel.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
					}
					listModel.setList(rows);
					dietaryMapper.bulkInsertDietary(listModel);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				dietaryMapper.bulkInsertDietary_oracle(rows);
			} else {
				ListModel listModel = new ListModel();
				if (StringUtils.isNotEmpty(tenantId)) {
					listModel.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
				}
				listModel.setList(rows);
				dietaryMapper.bulkInsertDietary(listModel);
			}
			rows.clear();
		}

		if (tableModels.size() > 0) {
			tableDataService.updateAllTableData(tableModels);
		}
	}

	@Transactional
	public void calculate(String tenantId, long dietaryId) {
		DietaryItemQuery query = new DietaryItemQuery();
		query.dietaryId(dietaryId);
		query.tenantId(tenantId);
		List<DietaryItem> items = dietaryItemService.list(query);
		List<Long> foodIds = new ArrayList<Long>();
		for (DietaryItem item : items) {
			if (!foodIds.contains(item.getFoodId())) {
				foodIds.add(item.getFoodId());
			}
		}

		FoodCompositionQuery query2 = new FoodCompositionQuery();
		query2.setIds(foodIds);
		List<FoodComposition> foods = foodCompositionService.list(query2);// 获取食物成分
		Map<Long, FoodComposition> foodMap = new HashMap<Long, FoodComposition>();
		for (FoodComposition food : foods) {
			foodMap.put(food.getId(), food);
		}

		DietaryQuery query4 = new DietaryQuery();
		query4.setDietaryId(dietaryId);
		query4.tenantId(tenantId);
		List<Dietary> dietarys = this.list(query4);
		if (dietarys != null && !dietarys.isEmpty()) {
			for (Dietary dietary : dietarys) {
				dietary.setHeatEnergy(0);
				dietary.setProtein(0);
				dietary.setFat(0);
				dietary.setCarbohydrate(0);
				dietary.setVitaminA(0);
				dietary.setVitaminB1(0);
				dietary.setVitaminB2(0);
				dietary.setVitaminB6(0);
				dietary.setVitaminB12(0);
				dietary.setVitaminC(0);
				dietary.setCarotene(0);
				dietary.setRetinol(0);
				dietary.setNicotinicCid(0);
				dietary.setCalcium(0);
				dietary.setIron(0);
				dietary.setZinc(0);
				dietary.setIodine(0);
				dietary.setPhosphorus(0);

				for (DietaryItem item : items) {
					if (!(item.getDietaryId() == dietary.getId())) {
						continue;
					}
					FoodComposition food = foodMap.get(item.getFoodId());
					if (food == null) {
						continue;
					}
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
					dietary.setHeatEnergy(dietary.getHeatEnergy() + food.getHeatEnergy() * factor);// 累加计算
					dietary.setProtein(dietary.getProtein() + food.getProtein() * factor);// 累加计算
					dietary.setFat(dietary.getFat() + food.getFat() * factor);// 累加计算
					dietary.setCarbohydrate(dietary.getCarbohydrate() + food.getCarbohydrate() * factor);// 累加计算
					dietary.setVitaminA(dietary.getVitaminA() + food.getVitaminA() * factor);// 累加计算
					dietary.setVitaminB1(dietary.getVitaminB1() + food.getVitaminB1() * factor);// 累加计算
					dietary.setVitaminB2(dietary.getVitaminB2() + food.getVitaminB2() * factor);// 累加计算
					dietary.setVitaminB6(dietary.getVitaminB6() + food.getVitaminB6() * factor);// 累加计算
					dietary.setVitaminB12(dietary.getVitaminB12() + food.getVitaminB12() * factor);// 累加计算
					dietary.setVitaminC(dietary.getVitaminC() + food.getVitaminC() * factor);// 累加计算
					dietary.setCarotene(dietary.getCarotene() + food.getCarotene() * factor);// 累加计算
					dietary.setRetinol(dietary.getRetinol() + food.getRetinol() * factor);// 累加计算
					dietary.setNicotinicCid(dietary.getNicotinicCid() + food.getNicotinicCid() * factor);// 累加计算
					dietary.setCalcium(dietary.getCalcium() + food.getCalcium() * factor);// 累加计算
					dietary.setIron(dietary.getIron() + food.getIron() * factor);// 累加计算
					dietary.setZinc(dietary.getZinc() + food.getZinc() * factor);// 累加计算
					dietary.setIodine(dietary.getIodine() + food.getIodine() * factor);// 累加计算
					dietary.setPhosphorus(dietary.getPhosphorus() + food.getPhosphorus() * factor);// 累加计算
				}
			}
			this.updateAll(tenantId, dietarys);
		}
	}

	@Transactional
	public long copyTemplate(LoginContext loginContext, long templateId) {
		DietaryTemplate dietaryTemplate = dietaryTemplateService.getDietaryTemplate(templateId);
		if (dietaryTemplate != null) {
			List<DietaryItem> items = dietaryItemService.getDietaryItemsByTemplateId(templateId);
			if (items != null && !items.isEmpty()) {
				Dietary dietary = new Dietary();
				DietaryDomainFactory.copyProperties(dietary, dietaryTemplate);
				dietary.setId(idGenerator.nextId("HEALTH_DIETARY_TEMPLATE"));
				dietary.setCreateTime(new Date());
				dietary.setCreateBy(loginContext.getActorId());
				dietary.setUpdateBy(loginContext.getActorId());
				dietary.setTenantId(loginContext.getTenantId());
				dietary.setDescription(dietaryTemplate.getDescription());
				// dietary.setAgeGroup(dietaryTemplate.getAgeGroup());
				// dietary.setEnableFlag(dietaryTemplate.getEnableFlag());
				// dietary.setInstanceFlag(dietaryTemplate.getInstanceFlag());
				dietary.setName(dietaryTemplate.getName());
				dietary.setShareFlag(dietaryTemplate.getShareFlag());
				// dietary.setSysFlag(dietaryTemplate.getSysFlag());
				dietary.setType(dietaryTemplate.getType());
				dietary.setTypeId(dietaryTemplate.getTypeId());
				dietary.setVerifyFlag(dietaryTemplate.getVerifyFlag());
				dietary.setTemplateId(dietaryTemplate.getId());

				dietaryMapper.insertDietary(dietary);

				for (DietaryItem item : items) {
					item.setId(idGenerator.nextId("HEALTH_DIETARY_ITEM"));
					item.setCreateTime(new Date());
					item.setCreateBy(loginContext.getActorId());
					item.setTenantId(loginContext.getTenantId());
					item.setDietaryId(dietary.getId());
					// item.setFullDay(fullday);
					item.setTemplateId(0);
				}

				ListModel listModel = new ListModel();
				if (StringUtils.isNotEmpty(loginContext.getTenantId())) {
					listModel.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(loginContext.getTenantId())));
				}
				listModel.setList(items);
				dietaryItemMapper.bulkInsertDietaryItem(listModel);

				return dietary.getId();
			}
		}
		return -1;
	}

	@Transactional
	public List<Long> copyTemplates(LoginContext loginContext, Date date, int week, List<Long> templateIds) {
		List<Long> newDietaryIds = new ArrayList<Long>();
		if (templateIds != null && !templateIds.isEmpty()) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH) + 1;
			int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			int fullday = DateUtils.getYearMonthDay(date);

			if (date.getTime() > System.currentTimeMillis()) {

				if (StringUtils.isNotEmpty(loginContext.getTenantId())) {
					TableModel table = new TableModel();
					table.setTableName(
							"HEALTH_DIETARY_ITEM" + IdentityFactory.getTenantHash(loginContext.getTenantId()));
					table.addStringColumn("TENANTID_", loginContext.getTenantId());
					table.addIntegerColumn("FULLDAY_", fullday);
					tableDataService.deleteTableData(table);// 删除食谱明细

					table.setTableName("HEALTH_DIETARY" + IdentityFactory.getTenantHash(loginContext.getTenantId()));
					table.addStringColumn("TENANTID_", loginContext.getTenantId());
					table.addIntegerColumn("FULLDAY_", fullday);
					tableDataService.deleteTableData(table);// 删除食谱

					table.setTableName("GOODS_PURCHASE" + IdentityFactory.getTenantHash(loginContext.getTenantId()));
					table.addStringColumn("TENANTID_", loginContext.getTenantId());
					table.addIntegerColumn("FULLDAY_", fullday);
					tableDataService.deleteTableData(table);// 删除采购申请

					table.setTableName(
							"GOODS_PLAN_QUANTITY" + IdentityFactory.getTenantHash(loginContext.getTenantId()));
					table.addStringColumn("TENANTID_", loginContext.getTenantId());
					table.addIntegerColumn("FULLDAY_", fullday);
					tableDataService.deleteTableData(table);// 删除计划数量
				}

			}

			for (Long templateId : templateIds) {
				DietaryTemplate dietaryTemplate = dietaryTemplateService.getDietaryTemplate(templateId);
				if (dietaryTemplate != null) {
					List<DietaryItem> items = dietaryItemService.getDietaryItemsByTemplateId(templateId);
					if (items != null && !items.isEmpty()) {
						Dietary dietary = new Dietary();
						DietaryDomainFactory.copyProperties(dietary, dietaryTemplate);
						dietary.setId(idGenerator.nextId("HEALTH_DIETARY_TEMPLATE"));
						dietary.setCreateTime(new Date());
						dietary.setCreateBy(loginContext.getActorId());
						dietary.setUpdateBy(loginContext.getActorId());
						dietary.setTenantId(loginContext.getTenantId());
						dietary.setDescription(dietaryTemplate.getDescription());
						// dietary.setAgeGroup(dietaryTemplate.getAgeGroup());
						// dietary.setEnableFlag(dietaryTemplate.getEnableFlag());
						// dietary.setInstanceFlag(dietaryTemplate.getInstanceFlag());
						dietary.setName(dietaryTemplate.getName());
						dietary.setShareFlag(dietaryTemplate.getShareFlag());
						// dietary.setSysFlag(dietaryTemplate.getSysFlag());
						dietary.setType(dietaryTemplate.getType());
						dietary.setTypeId(dietaryTemplate.getTypeId());
						dietary.setVerifyFlag(dietaryTemplate.getVerifyFlag());
						dietary.setTemplateId(dietaryTemplate.getId());

						dietary.setYear(year);
						dietary.setMonth(month);
						dietary.setWeek(week);
						dietary.setDay(day);
						dietary.setDayOfWeek(dayOfWeek);

						dietary.setFullDay(DateUtils.getYearMonthDay(date));
						dietary.setSortNo(dietaryTemplate.getSortNo());
						dietary.setSemester(SysConfig.getSemester());

						if (StringUtils.isNotEmpty(dietary.getTenantId())) {
							dietary.setTableSuffix(
									String.valueOf(IdentityFactory.getTenantHash(dietary.getTenantId())));
						}

						dietaryMapper.insertDietary(dietary);

						for (DietaryItem item : items) {
							item.setId(idGenerator.nextId("HEALTH_DIETARY_ITEM"));
							item.setCreateTime(new Date());
							item.setCreateBy(loginContext.getActorId());
							item.setTenantId(loginContext.getTenantId());
							item.setDietaryId(dietary.getId());
							item.setTemplateId(0);
							item.setFullDay(fullday);
							if (StringUtils.isNotEmpty(dietary.getTenantId())) {
								item.setTableSuffix(
										String.valueOf(IdentityFactory.getTenantHash(dietary.getTenantId())));
							}
						}

						ListModel listModel = new ListModel();
						if (StringUtils.isNotEmpty(loginContext.getTenantId())) {
							listModel.setTableSuffix(
									String.valueOf(IdentityFactory.getTenantHash(loginContext.getTenantId())));
						}
						listModel.setList(items);
						dietaryItemMapper.bulkInsertDietaryItem(listModel);

						newDietaryIds.add(dietary.getId());
					}
				}
			}
		}
		return newDietaryIds;
	}

	public int count(DietaryQuery query) {
		return dietaryMapper.getDietaryCount(query);
	}

	@Transactional
	public void deleteById(String tenantId, long dietaryId) {
		if (dietaryId != 0) {
			DietaryItemQuery q = new DietaryItemQuery();
			q.setDietaryId(dietaryId);
			if (StringUtils.isNotEmpty(tenantId)) {
				q.tenantId(tenantId);
				q.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
			}
			dietaryItemMapper.deleteDietaryItemsByDietaryId(q);

			DietaryQuery query = new DietaryQuery();
			query.setDietaryId(dietaryId);
			if (StringUtils.isNotEmpty(tenantId)) {
				query.tenantId(tenantId);
				query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
			}

			dietaryMapper.deleteDietaryById(query);
		}
	}

	@Transactional
	public void deleteByIds(String tenantId, List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long dietaryId : ids) {
				DietaryItemQuery q = new DietaryItemQuery();
				q.setDietaryId(dietaryId);
				if (StringUtils.isNotEmpty(tenantId)) {
					q.tenantId(tenantId);
					q.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
				}
				dietaryItemMapper.deleteDietaryItemsByDietaryId(q);

				DietaryQuery query = new DietaryQuery();
				query.setDietaryId(dietaryId);
				if (StringUtils.isNotEmpty(tenantId)) {
					query.tenantId(tenantId);
					query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
				}

				dietaryMapper.deleteDietaryById(query);
			}
		}
	}

	public List<Integer> getDays(String tenantId, int year, int semester, int week) {
		List<Integer> days = new ArrayList<Integer>();
		DietaryQuery query = new DietaryQuery();
		query.year(year);
		query.week(week);
		query.semester(semester);

		if (StringUtils.isNotEmpty(tenantId)) {
			query.tenantId(tenantId);
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
		}

		List<Dietary> list = dietaryMapper.getDietarys(query);
		if (list != null && !list.isEmpty()) {
			for (Dietary d : list) {
				if (!days.contains(d.getFullDay())) {
					days.add(d.getFullDay());
				}
			}
		}
		return days;
	}

	public Dietary getDietary(String tenantId, long dietaryId) {
		if (dietaryId == 0) {
			return null;
		}

		DietaryQuery query = new DietaryQuery();
		query.setDietaryId(dietaryId);
		if (StringUtils.isNotEmpty(tenantId)) {
			query.tenantId(tenantId);
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
		}

		Dietary dietary = dietaryMapper.getDietaryById(query);
		if (dietary != null) {
			DietaryItemQuery qx = new DietaryItemQuery();
			qx.setDietaryId(dietaryId);
			if (StringUtils.isNotEmpty(tenantId)) {
				qx.tenantId(tenantId);
				qx.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
			}
			List<DietaryItem> items = dietaryItemMapper.getDietaryItemsByDietaryId(qx);
			dietary.setItems(items);
		}

		return dietary;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getDietaryCountByQueryCriteria(DietaryQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		return dietaryMapper.getDietaryCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<Dietary> getDietarysByQueryCriteria(int start, int pageSize, DietaryQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<Dietary> rows = sqlSessionTemplate.selectList("getDietarys", query, rowBounds);
		return rows;
	}

	public int getMaxWeek(String tenantId, int year, int semester) {
		DietaryQuery query = new DietaryQuery();
		query.year(year);
		query.semester(semester);
		if (StringUtils.isNotEmpty(tenantId)) {
			query.tenantId(tenantId);
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
		}
		Integer week = dietaryMapper.getMaxWeek(query);
		if (week != null) {
			return week.intValue();
		}
		return 0;
	}

	public int getNowWeek(String tenantId, int year, int semester) {
		DietaryQuery query = new DietaryQuery();
		query.year(year);
		query.semester(semester);
		query.fullDay(DateUtils.getNowYearMonthDay());
		if (StringUtils.isNotEmpty(tenantId)) {
			query.tenantId(tenantId);
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
		}
		List<Dietary> list = dietaryMapper.getDietarys(query);
		if (list != null && !list.isEmpty()) {
			return list.get(0).getWeek();
		}
		return 0;
	}

	public int getWeek(String tenantId, int fullDay) {
		DietaryQuery query = new DietaryQuery();
		query.fullDay(fullDay);
		if (StringUtils.isNotEmpty(tenantId)) {
			query.tenantId(tenantId);
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
		}
		List<Dietary> list = dietaryMapper.getDietarys(query);
		if (list != null && !list.isEmpty()) {
			return list.get(0).getWeek();
		}
		return 0;
	}

	public List<Dietary> list(DietaryQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		List<Dietary> list = dietaryMapper.getDietarys(query);
		return list;
	}

	@Transactional
	public void save(Dietary dietary) {
		if (StringUtils.isNotEmpty(dietary.getTenantId())) {
			dietary.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(dietary.getTenantId())));
		}

		if (dietary.getId() == 0) {
			dietary.setId(idGenerator.nextId("HEALTH_DIETARY"));
			dietary.setCreateTime(new Date());
			dietaryMapper.insertDietary(dietary);
		} else {
			dietaryMapper.updateDietary(dietary);
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

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.DietaryMapper")
	public void setDietaryMapper(DietaryMapper dietaryMapper) {
		this.dietaryMapper = dietaryMapper;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.DietaryTemplateMapper")
	public void setDietaryTemplateMapper(DietaryTemplateMapper dietaryTemplateMapper) {
		this.dietaryTemplateMapper = dietaryTemplateMapper;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.dietaryTemplateService")
	public void setDietaryTemplateService(DietaryTemplateService dietaryTemplateService) {
		this.dietaryTemplateService = dietaryTemplateService;
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.foodCompositionService")
	public void setFoodCompositionService(FoodCompositionService foodCompositionService) {
		this.foodCompositionService = foodCompositionService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.goodsPurchasePlanService")
	public void setGoodsPurchasePlanService(GoodsPurchasePlanService goodsPurchasePlanService) {
		this.goodsPurchasePlanService = goodsPurchasePlanService;
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

	@javax.annotation.Resource
	public void setTableDataService(ITableDataService tableDataService) {
		this.tableDataService = tableDataService;
	}

	@Transactional
	public void updateAll(String tenantId, List<Dietary> list) {
		for (Dietary dietary : list) {
			if (StringUtils.isNotEmpty(dietary.getTenantId())) {
				dietary.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(dietary.getTenantId())));
			}
			dietaryMapper.updateDietary(dietary);
		}
	}

}
