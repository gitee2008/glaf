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

import com.glaf.base.modules.sys.model.SysTree;
import com.glaf.base.modules.sys.service.SysTreeService;
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
import com.glaf.heathcare.domain.Dietary;
import com.glaf.heathcare.domain.DietaryItem;
import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.domain.GoodsPlanQuantity;
import com.glaf.heathcare.mapper.GoodsPlanQuantityMapper;
import com.glaf.heathcare.query.DietaryItemQuery;
import com.glaf.heathcare.query.DietaryQuery;
import com.glaf.heathcare.query.FoodCompositionQuery;
import com.glaf.heathcare.query.GoodsPlanQuantityQuery;
import com.glaf.heathcare.service.DietaryItemService;
import com.glaf.heathcare.service.DietaryService;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.heathcare.service.GoodsPlanQuantityService;

@Service("com.glaf.heathcare.service.goodsPlanQuantityService")
@Transactional(readOnly = true)
public class GoodsPlanQuantityServiceImpl implements GoodsPlanQuantityService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected GoodsPlanQuantityMapper goodsPlanQuantityMapper;

	protected SysTreeService sysTreeService;

	protected DietaryService dietaryService;

	protected DietaryItemService dietaryItemService;

	protected FoodCompositionService foodCompositionService;

	protected ITableDataService tableDataService;

	public GoodsPlanQuantityServiceImpl() {

	}

	@Transactional
	public void bulkInsert(String tenantId, List<GoodsPlanQuantity> list) {
		for (GoodsPlanQuantity goodsPlanQuantity : list) {
			if (goodsPlanQuantity.getId() == 0) {
				goodsPlanQuantity.setId(idGenerator.nextId("GOODS_PLAN_QUANTITY"));
				goodsPlanQuantity.setCreateTime(new Date());
				if (StringUtils.isNotEmpty(tenantId)) {
					goodsPlanQuantity.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
				}
			}
		}

		int batch_size = 50;
		List<GoodsPlanQuantity> rows = new ArrayList<GoodsPlanQuantity>(batch_size);

		for (GoodsPlanQuantity bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					goodsPlanQuantityMapper.bulkInsertGoodsPlanQuantity_oracle(rows);
				} else {
					ListModel listModel = new ListModel();
					if (StringUtils.isNotEmpty(tenantId)) {
						listModel.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
					}
					listModel.setList(rows);
					goodsPlanQuantityMapper.bulkInsertGoodsPlanQuantity(listModel);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				goodsPlanQuantityMapper.bulkInsertGoodsPlanQuantity_oracle(rows);
			} else {
				ListModel listModel = new ListModel();
				if (StringUtils.isNotEmpty(tenantId)) {
					listModel.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
				}
				listModel.setList(rows);
				goodsPlanQuantityMapper.bulkInsertGoodsPlanQuantity(listModel);
			}
			rows.clear();
		}
	}

	public int count(GoodsPlanQuantityQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		return goodsPlanQuantityMapper.getGoodsPlanQuantityCount(query);
	}

	/**
	 * 根据食谱编排形成一周用量计划
	 * 
	 * @param loginContext
	 * @param year
	 * @param month
	 * @param week
	 * @param totalPersonQuantity
	 */
	@Transactional
	public void createGoodsUsagePlan(LoginContext loginContext, int year, int semester, int week,
			double totalPersonQuantity) {
		logger.debug("----------------------createGoodsUsagePlan-------------");
		DietaryQuery query = new DietaryQuery();
		query.tenantId(loginContext.getTenantId());
		query.year(year);
		query.semester(semester);
		query.week(week);
		query.dayGreaterThanOrEqual(1);
		query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));

		List<Dietary> list = dietaryService.list(query);// 获取选中的食谱
		if (list != null && !list.isEmpty()) {
			List<Integer> days = new ArrayList<Integer>();
			List<GoodsPlanQuantity> weekPlanList = new ArrayList<GoodsPlanQuantity>();
			Map<Integer, List<Long>> dietaryIdsMap = new HashMap<Integer, List<Long>>();
			Map<Integer, List<Dietary>> dailyDietaryMap = new HashMap<Integer, List<Dietary>>();
			for (Dietary dietary : list) {
				if (dietary.getFullDay() > 0) {
					if (!days.contains(dietary.getFullDay())) {
						days.add(dietary.getFullDay());
					}

					List<Dietary> list2 = dailyDietaryMap.get(dietary.getFullDay());
					if (list2 == null) {
						list2 = new ArrayList<Dietary>();
					}
					list2.add(dietary);
					dailyDietaryMap.put(dietary.getFullDay(), list2);

					List<Long> list3 = dietaryIdsMap.get(dietary.getFullDay());
					if (list3 == null) {
						list3 = new ArrayList<Long>();
					}
					list3.add(dietary.getId());
					dietaryIdsMap.put(dietary.getFullDay(), list3);
				}
			}

			String dateString = null;
			Date planDate = null;
			Calendar calendar = Calendar.getInstance();
			// Double radical = 0D;
			Double percent = null;
			double quantity = 0;
			double realQuantity = 0;
			List<Long> dietaryIds = null;
			List<Long> foodIds = new ArrayList<Long>();
			// Map<Long, Double> radicalMap = new HashMap<Long, Double>();
			Map<Long, FoodComposition> foodMap = new HashMap<Long, FoodComposition>();
			Map<Long, GoodsPlanQuantity> planMap = new HashMap<Long, GoodsPlanQuantity>();

			Map<Long, Double> percentMap = new HashMap<Long, Double>();
			SysTree root = sysTreeService.getSysTreeByCode("FoodCategory");
			if (root != null) {
				List<SysTree> foodCategories = sysTreeService.getSysTreeList(root.getId());
				if (foodCategories != null && !foodCategories.isEmpty()) {
					for (SysTree tree : foodCategories) {
						if (StringUtils.isNotEmpty(tree.getValue())) {
							try {
								percentMap.put(tree.getId(), Double.parseDouble(tree.getValue()));
							} catch (Exception ex) {
							}
						}
					}
				}
			}

			for (Integer fullday : days) {
				dateString = String.valueOf(fullday);
				planDate = DateUtils.toDate(dateString);
				dietaryIds = dietaryIdsMap.get(fullday);
				if (dietaryIds != null && !dietaryIds.isEmpty()) {
					DietaryItemQuery query2 = new DietaryItemQuery();
					query2.dietaryIds(dietaryIds);
					query2.tenantId(loginContext.getTenantId());
					query2.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query2.getTenantId())));
					List<DietaryItem> items = dietaryItemService.list(query2);// 取得食谱构成项即食物成分
					if (items != null && !items.isEmpty()) {
						foodIds.clear();
						for (DietaryItem item : items) {
							if (!foodIds.contains(item.getFoodId())) {
								foodIds.add(item.getFoodId());
							}
						}

						if (!foodIds.isEmpty()) {
							foodMap.clear();
							planMap.clear();
							// radicalMap.clear();

							FoodCompositionQuery query3 = new FoodCompositionQuery();
							query3.setIds(foodIds);
							List<FoodComposition> foods = foodCompositionService.list(query3);// 获取食物成分
							Map<Long, Long> nodeIdMap = new HashMap<Long, Long>();

							for (FoodComposition food : foods) {
								foodMap.put(food.getId(), food);
								nodeIdMap.put(food.getId(), food.getNodeId());
								// radical = food.getRadical();
								// if (radical == null) {
								// radical = 100D;
								// }
								// radicalMap.put(food.getId(), radical);
							}

							for (DietaryItem item : items) {
								FoodComposition food = foodMap.get(item.getFoodId());
								if (food == null) {
									continue;
								}
								GoodsPlanQuantity plan = planMap.get(item.getFoodId());
								if (plan == null) {
									plan = new GoodsPlanQuantity();
									calendar.setTime(planDate);
									plan.setCreateBy(loginContext.getActorId());
									plan.setTenantId(loginContext.getTenantId());
									plan.setGoodsId(food.getId());
									plan.setGoodsName(food.getName());
									plan.setGoodsNodeId(food.getNodeId());
									plan.setSysFlag("Y");
									plan.setBusinessStatus(0);
									plan.setUnit(item.getUnit());
									plan.setUsageTime(planDate);
									plan.setSemester(semester);
									plan.setFullDay(fullday);
									plan.setYear(year);
									plan.setMonth(calendar.get(Calendar.MONTH) + 1);
									plan.setWeek(week);
									plan.setDay(plan.getFullDay() % 100);

									// radical =
									// radicalMap.get(item.getFoodId());
									// if (radical == null) {
									// radical = 100D;
									// }

									quantity = item.getQuantity();// 每一种食品的量

									// if (radical < 100) {
									/**
									 * 计算每一份的实际量
									 */
									// realQuantity = quantity * (1.0 +
									// ((100 - radical) / radical));
									// } else {
									// realQuantity = quantity;
									// }

									/**
									 * 根据系统配置参数按类别计算
									 */
									percent = percentMap.get(nodeIdMap.get(item.getFoodId()));
									if (percent != null && percent > 1 && percent < 2) {
										realQuantity = quantity * percent;
									} else {
										realQuantity = quantity;
									}

									plan.setQuantity(realQuantity);
									planMap.put(food.getId(), plan);
								} else {
									// radical =
									// radicalMap.get(item.getFoodId());
									// if (radical == null) {
									// radical = 100D;
									// }

									quantity = item.getQuantity();// 每一种食品的量

									// if (radical < 100) {
									/**
									 * 计算每一份的实际量
									 */
									// realQuantity = quantity * (1.0 +
									// ((100 - radical) / radical));
									// } else {
									realQuantity = quantity;
									// }

									plan.setQuantity(plan.getQuantity() + realQuantity);
									planMap.put(food.getId(), plan);
								}
							}

							for (FoodComposition food : foods) {
								GoodsPlanQuantity plan = planMap.get(food.getId());
								if (plan != null) {
									plan.setQuantity(plan.getQuantity() * totalPersonQuantity);
									plan.setQuantity(plan.getQuantity() / 1000);
									plan.setUnit("KG");
									weekPlanList.add(plan);
								}
							}
						}
					}
				}
			}

			if (!weekPlanList.isEmpty()) {
				logger.debug("正在生成一周采购计划...");
				TableModel table = new TableModel();
				table.setTableName("GOODS_PLAN_QUANTITY"
						+ String.valueOf(IdentityFactory.getTenantHash(loginContext.getTenantId())));
				table.addStringColumn("TENANTID_", loginContext.getTenantId());
				table.addIntegerColumn("YEAR_", year);
				table.addIntegerColumn("SEMESTER_", semester);
				table.addIntegerColumn("WEEK_", week);
				table.addStringColumn("SYSFLAG_", "Y");
				tableDataService.deleteTableData(table);

				this.bulkInsert(loginContext.getTenantId(), weekPlanList);
			}
		}
	}

	/**
	 * 根据食谱编排形成每日用量计划
	 * 
	 * @param loginContext
	 * @param year
	 * @param month
	 * @param week
	 * @param fullDay
	 * @param totalPersonQuantity
	 */
	@Transactional
	public void createGoodsUsagePlan(LoginContext loginContext, int year, int semester, int week, int fullDay,
			double totalPersonQuantity) {
		logger.debug("----------------------createGoodsUsagePlan-------------");
		DietaryQuery query = new DietaryQuery();
		query.tenantId(loginContext.getTenantId());
		query.fullDay(fullDay);
		query.semester(semester);
		query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));

		List<Dietary> list = dietaryService.list(query);// 获取选中的食谱
		if (list != null && !list.isEmpty()) {
			List<Long> dietaryIds = new ArrayList<Long>();
			List<GoodsPlanQuantity> planList = new ArrayList<GoodsPlanQuantity>();
			for (Dietary dietary : list) {
				if (dietary.getFullDay() > 0) {
					dietaryIds.add(dietary.getId());
				}
			}

			Calendar calendar = Calendar.getInstance();
			// Double radical = 0D;
			Double percent = null;
			double quantity = 0;
			double realQuantity = 0;

			List<Long> foodIds = new ArrayList<Long>();
			// Map<Long, Double> radicalMap = new HashMap<Long, Double>();
			Map<Long, FoodComposition> foodMap = new HashMap<Long, FoodComposition>();
			Map<Long, GoodsPlanQuantity> planMap = new HashMap<Long, GoodsPlanQuantity>();

			Map<Long, Double> percentMap = new HashMap<Long, Double>();
			SysTree root = sysTreeService.getSysTreeByCode("FoodCategory");
			if (root != null) {
				List<SysTree> foodCategories = sysTreeService.getSysTreeList(root.getId());
				if (foodCategories != null && !foodCategories.isEmpty()) {
					for (SysTree tree : foodCategories) {
						if (StringUtils.isNotEmpty(tree.getValue())) {
							try {
								percentMap.put(tree.getId(), Double.parseDouble(tree.getValue()));
							} catch (Exception ex) {
							}
						}
					}
				}
			}

			if (dietaryIds != null && !dietaryIds.isEmpty()) {
				String dateString = String.valueOf(fullDay);
				Date planDate = DateUtils.toDate(dateString);

				DietaryItemQuery query2 = new DietaryItemQuery();
				query2.dietaryIds(dietaryIds);
				query2.tenantId(loginContext.getTenantId());
				query2.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query2.getTenantId())));
				List<DietaryItem> items = dietaryItemService.list(query2);// 取得食谱构成项即食物成分
				if (items != null && !items.isEmpty()) {
					foodIds.clear();
					for (DietaryItem item : items) {
						if (!foodIds.contains(item.getFoodId())) {
							foodIds.add(item.getFoodId());
						}
					}

					if (!foodIds.isEmpty()) {
						foodMap.clear();
						planMap.clear();
						// radicalMap.clear();

						FoodCompositionQuery query3 = new FoodCompositionQuery();
						query3.setIds(foodIds);
						List<FoodComposition> foods = foodCompositionService.list(query3);// 获取食物成分
						Map<Long, Long> nodeIdMap = new HashMap<Long, Long>();

						for (FoodComposition food : foods) {
							foodMap.put(food.getId(), food);
							nodeIdMap.put(food.getId(), food.getNodeId());
							// radical = food.getRadical();
							// if (radical == null) {
							// radical = 100D;
							// }
							// radicalMap.put(food.getId(), radical);
						}

						for (DietaryItem item : items) {
							FoodComposition food = foodMap.get(item.getFoodId());
							if (food == null) {
								continue;
							}
							GoodsPlanQuantity plan = planMap.get(item.getFoodId());
							if (plan == null) {
								plan = new GoodsPlanQuantity();
								calendar.setTime(planDate);
								plan.setCreateBy(loginContext.getActorId());
								plan.setTenantId(loginContext.getTenantId());
								plan.setGoodsId(food.getId());
								plan.setGoodsName(food.getName());
								plan.setGoodsNodeId(food.getNodeId());
								plan.setSysFlag("Y");
								plan.setBusinessStatus(0);
								plan.setUnit(item.getUnit());
								plan.setUsageTime(planDate);
								plan.setSemester(semester);
								plan.setFullDay(fullDay);
								plan.setYear(year);
								plan.setMonth(calendar.get(Calendar.MONTH) + 1);
								plan.setWeek(week);
								plan.setDay(plan.getFullDay() % 100);

								// radical =
								// radicalMap.get(item.getFoodId());
								// if (radical == null) {
								// radical = 100D;
								// }

								quantity = item.getQuantity();// 每一种食品的量

								// if (radical < 100) {
								/**
								 * 计算每一份的实际量
								 */
								// realQuantity = quantity * (1.0 +
								// ((100 - radical) / radical));
								// } else {
								// realQuantity = quantity;
								// }

								/**
								 * 根据系统配置参数按类别计算
								 */
								percent = percentMap.get(nodeIdMap.get(item.getFoodId()));
								if (percent != null && percent > 1 && percent < 2) {
									realQuantity = quantity * percent;
								} else {
									realQuantity = quantity;
								}

								plan.setQuantity(realQuantity);
								planMap.put(food.getId(), plan);
							} else {
								// radical =
								// radicalMap.get(item.getFoodId());
								// if (radical == null) {
								// radical = 100D;
								// }

								quantity = item.getQuantity();// 每一种食品的量

								// if (radical < 100) {
								/**
								 * 计算每一份的实际量
								 */
								// realQuantity = quantity * (1.0 +
								// ((100 - radical) / radical));
								// } else {
								realQuantity = quantity;
								// }

								plan.setQuantity(plan.getQuantity() + realQuantity);
								planMap.put(food.getId(), plan);
							}
						}

						for (FoodComposition food : foods) {
							GoodsPlanQuantity plan = planMap.get(food.getId());
							if (plan != null) {
								plan.setQuantity(plan.getQuantity() * totalPersonQuantity);
								plan.setQuantity(plan.getQuantity() / 1000);
								plan.setUnit("KG");
								planList.add(plan);
							}
						}
					}
				}
			}

			if (!planList.isEmpty()) {
				logger.debug("正在生成一天采购计划...");
				TableModel table = new TableModel();
				table.setTableName("GOODS_PLAN_QUANTITY"
						+ String.valueOf(IdentityFactory.getTenantHash(loginContext.getTenantId())));
				table.addStringColumn("TENANTID_", loginContext.getTenantId());
				table.addIntegerColumn("YEAR_", year);
				table.addIntegerColumn("SEMESTER_", semester);
				table.addIntegerColumn("FULLDAY_", fullDay);
				table.addStringColumn("SYSFLAG_", "Y");
				tableDataService.deleteTableData(table);

				this.bulkInsert(loginContext.getTenantId(), planList);
			}
		}
	}

	@Transactional
	public void deleteById(String tenantId, long id) {
		if (id != 0) {
			GoodsPlanQuantityQuery query = new GoodsPlanQuantityQuery();
			query.setId(id);
			if (StringUtils.isNotEmpty(tenantId)) {
				query.tenantId(tenantId);
				query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
			}
			goodsPlanQuantityMapper.deleteGoodsPlanQuantityById(query);
		}
	}

	@Transactional
	public void deleteByIds(String tenantId, List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				GoodsPlanQuantityQuery query = new GoodsPlanQuantityQuery();
				query.setId(id);
				if (StringUtils.isNotEmpty(tenantId)) {
					query.tenantId(tenantId);
					query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
				}
				goodsPlanQuantityMapper.deleteGoodsPlanQuantityById(query);
			}
		}
	}

	public GoodsPlanQuantity getGoodsPlanQuantity(String tenantId, long id) {
		if (id == 0) {
			return null;
		}
		GoodsPlanQuantityQuery query = new GoodsPlanQuantityQuery();
		query.setId(id);
		if (StringUtils.isNotEmpty(tenantId)) {
			query.tenantId(tenantId);
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
		}
		GoodsPlanQuantity goodsPlanQuantity = goodsPlanQuantityMapper.getGoodsPlanQuantityById(query);
		return goodsPlanQuantity;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getGoodsPlanQuantityCountByQueryCriteria(GoodsPlanQuantityQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		return goodsPlanQuantityMapper.getGoodsPlanQuantityCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<GoodsPlanQuantity> getGoodsPlanQuantitysByQueryCriteria(int start, int pageSize,
			GoodsPlanQuantityQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		List<GoodsPlanQuantity> rows = sqlSessionTemplate.selectList("getGoodsPlanQuantitys", query, rowBounds);
		return rows;
	}

	public List<GoodsPlanQuantity> list(GoodsPlanQuantityQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		List<GoodsPlanQuantity> list = goodsPlanQuantityMapper.getGoodsPlanQuantitys(query);
		return list;
	}

	@Transactional
	public void save(GoodsPlanQuantity goodsPlanQuantity) {
		if (StringUtils.isNotEmpty(goodsPlanQuantity.getTenantId())) {
			goodsPlanQuantity
					.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(goodsPlanQuantity.getTenantId())));
		}
		if (goodsPlanQuantity.getId() == 0) {
			goodsPlanQuantity.setId(idGenerator.nextId("GOODS_PLAN_QUANTITY"));
			goodsPlanQuantity.setCreateTime(new Date());
			goodsPlanQuantityMapper.insertGoodsPlanQuantity(goodsPlanQuantity);
		} else {
			goodsPlanQuantityMapper.updateGoodsPlanQuantity(goodsPlanQuantity);
		}
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.dietaryItemService")
	public void setDietaryItemService(DietaryItemService dietaryItemService) {
		this.dietaryItemService = dietaryItemService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.dietaryService")
	public void setDietaryService(DietaryService dietaryService) {
		this.dietaryService = dietaryService;
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.foodCompositionService")
	public void setFoodCompositionService(FoodCompositionService foodCompositionService) {
		this.foodCompositionService = foodCompositionService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.GoodsPlanQuantityMapper")
	public void setGoodsPlanQuantityMapper(GoodsPlanQuantityMapper goodsPlanQuantityMapper) {
		this.goodsPlanQuantityMapper = goodsPlanQuantityMapper;
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
	public void setSysTreeService(SysTreeService sysTreeService) {
		this.sysTreeService = sysTreeService;
	}

	@javax.annotation.Resource
	public void setTableDataService(ITableDataService tableDataService) {
		this.tableDataService = tableDataService;
	}

}
