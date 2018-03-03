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

package com.glaf.heathcare.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.base.modules.sys.model.SysTree;
import com.glaf.base.modules.sys.service.DictoryService;
import com.glaf.base.modules.sys.service.SysTreeService;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.DateUtils;
import com.glaf.heathcare.SysConfig;
import com.glaf.heathcare.domain.Dietary;
import com.glaf.heathcare.domain.DietaryItem;
import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.domain.GoodsInStock;
import com.glaf.heathcare.domain.GoodsOutStock;
import com.glaf.heathcare.domain.GoodsPurchasePlan;
import com.glaf.heathcare.helper.PersonInfoHelper;
import com.glaf.heathcare.query.DietaryItemQuery;
import com.glaf.heathcare.query.DietaryQuery;
import com.glaf.heathcare.query.FoodCompositionQuery;
import com.glaf.heathcare.query.GoodsInStockQuery;
import com.glaf.heathcare.query.GoodsOutStockQuery;
import com.glaf.heathcare.service.DietaryItemService;
import com.glaf.heathcare.service.DietaryService;
import com.glaf.heathcare.service.DietaryTemplateService;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.heathcare.service.GoodsInStockService;
import com.glaf.heathcare.service.GoodsOutStockService;
import com.glaf.heathcare.service.GoodsPlanQuantityService;
import com.glaf.heathcare.service.GoodsPurchasePlanService;
import com.glaf.heathcare.service.PersonInfoService;

public class GoodsPurchasePlanBean {

	protected static final Log logger = LogFactory.getLog(GoodsPurchasePlanBean.class);

	protected DictoryService dictoryService;

	protected SysTreeService sysTreeService;

	protected DietaryService dietaryService;

	protected DietaryItemService dietaryItemService;

	protected DietaryTemplateService dietaryTemplateService;

	protected FoodCompositionService foodCompositionService;

	protected GoodsInStockService goodsInStockService;

	protected GoodsOutStockService goodsOutStockService;

	protected GoodsPurchasePlanService goodsPurchasePlanService;

	protected GoodsPlanQuantityService goodsPlanQuantityService;

	protected PersonInfoService personInfoService;

	public GoodsPurchasePlanBean() {

	}

	/**
	 * 根据食谱编排形成采购计划
	 * 
	 * @param loginContext
	 * @param year
	 * @param month
	 * @param week
	 */
	public void addParchasePlan(LoginContext loginContext, int year, int month, int week) {
		DietaryQuery query = new DietaryQuery();
		query.tenantId(loginContext.getTenantId());
		query.setSemester(SysConfig.getSemester());
		query.dayGreaterThanOrEqual(1);
		query.businessStatus(0);
		query.year(year);
		query.week(week);

		List<Dietary> list = getDietaryService().list(query);// 获取选中的食谱
		if (list != null && !list.isEmpty()) {
			List<Long> planIds = new ArrayList<Long>();
			List<Long> dietaryIds = new ArrayList<Long>();
			List<Integer> fulldays = new ArrayList<Integer>();
			Map<Integer, List<Long>> templateIdsMap = new HashMap<Integer, List<Long>>();
			Map<Integer, List<Dietary>> dailyDietaryMap = new HashMap<Integer, List<Dietary>>();
			for (Dietary dietary : list) {
				if (!StringUtils.equals(dietary.getPurchaseFlag(), "Y")) {
					dietaryIds.add(dietary.getId());// 取得食谱模板编号
					planIds.add(dietary.getId());
					if (dietary.getFullDay() > 0) {
						List<Dietary> list2 = dailyDietaryMap.get(dietary.getFullDay());
						if (list2 == null) {
							list2 = new ArrayList<Dietary>();
						}
						list2.add(dietary);
						dailyDietaryMap.put(dietary.getFullDay(), list2);

						List<Long> list3 = templateIdsMap.get(dietary.getFullDay());
						if (list3 == null) {
							list3 = new ArrayList<Long>();
						}
						list3.add(dietary.getId());
						templateIdsMap.put(dietary.getFullDay(), list3);

						if (!fulldays.contains(dietary.getFullDay())) {
							fulldays.add(dietary.getFullDay());
						}
					}
				}
			}

			if (!dietaryIds.isEmpty()) {
				DietaryItemQuery query2 = new DietaryItemQuery();
				query2.tenantId(loginContext.getTenantId());
				query2.dietaryIds(dietaryIds);
				Collection<DietaryItem> items = getDietaryItemService().list(query2);// 取得食谱构成项即食物成分
				if (items != null && !items.isEmpty()) {
					List<Long> foodIds = new ArrayList<Long>();// 一周集中采购
					List<Long> dailyFoodIds = new ArrayList<Long>();// 当日采购
					for (DietaryItem item : items) {
						if (!foodIds.contains(item.getFoodId())) {
							foodIds.add(item.getFoodId());
						}
					}

					if (!foodIds.isEmpty()) {
						FoodCompositionQuery query3 = new FoodCompositionQuery();
						query3.setIds(foodIds);
						List<FoodComposition> foods = getFoodCompositionService().list(query3);// 获取食物成分
						List<FoodComposition> weekFoods = new ArrayList<FoodComposition>();
						Map<Long, Long> nodeIdMap = new HashMap<Long, Long>();
						Map<Long, FoodComposition> foodMap = new HashMap<Long, FoodComposition>();
						Map<Long, FoodComposition> dailyFoodMap = new HashMap<Long, FoodComposition>();
						// Map<Long, Double> radicalMap = new HashMap<Long,
						// Double>();
						for (FoodComposition food : foods) {
							foodMap.put(food.getId(), food);
							nodeIdMap.put(food.getId(), food.getNodeId());
							if (StringUtils.equals(food.getDailyFlag(), "Y")) {
								dailyFoodIds.add(food.getId());
								dailyFoodMap.put(food.getId(), food);
							} else {
								weekFoods.add(food);
							}
						}

						Map<Long, Double> percentMap = new HashMap<Long, Double>();
						SysTree root = getSysTreeService().getSysTreeByCode("FoodCategory");
						if (root != null) {
							List<SysTree> foodCategories = getSysTreeService().getSysTreeList(root.getId());
							if (foodCategories != null && !foodCategories.isEmpty()) {
								for (SysTree tree : foodCategories) {
									if (StringUtils.isNotEmpty(tree.getValue())
											&& StringUtils.isNumeric(tree.getValue())) {
										try {
											percentMap.put(tree.getId(), Double.parseDouble(tree.getValue()));
										} catch (Exception ex) {
										}
									}
								}
							}
						}

						Date date = null;
						// Double radical = 0D;
						Double percent = null;
						double quantity = 0;
						double realQuantity = 0;
						double totalRealQuantity = 0;
						FoodComposition foodComposition = null;

						PersonInfoHelper helper = new PersonInfoHelper();
						double totalPersonQuantity = helper.getPersonFoodAgeQuantity(loginContext.getTenantId(), 1.03);

						/**
						 * 如果是每日采购，不需要检查库存，每天按食谱排的计划进行采购，非每日采购的，可以进行每周一次集中采购
						 */

						List<GoodsPurchasePlan> purchaseList = new ArrayList<GoodsPurchasePlan>();

						for (Integer fullday : fulldays) {
							dietaryIds = templateIdsMap.get(fullday);
							if (dietaryIds != null && !dietaryIds.isEmpty()) {
								query2 = new DietaryItemQuery();
								query2.tenantId(loginContext.getTenantId());
								query2.dietaryIds(dietaryIds);
								items = getDietaryItemService().list(query2);// 取得食谱构成项即食物成分
								if (items != null && !items.isEmpty()) {
									Map<Long, DietaryItem> tempMap = new HashMap<Long, DietaryItem>();
									for (DietaryItem item : items) {
										DietaryItem tmpItem = tempMap.get(item.getFoodId());
										if (tmpItem == null) {
											tmpItem = item;
										} else {
											tmpItem.setQuantity(tmpItem.getQuantity() + item.getQuantity());
										}
										tempMap.put(item.getFoodId(), tmpItem);
									}

									items = tempMap.values();

									date = DateUtils.toDate(String.valueOf(fullday));
									for (DietaryItem item : items) {
										/**
										 * 获取每日采购的食物
										 */
										if (dailyFoodMap.get(item.getFoodId()) != null) {
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

											totalRealQuantity = totalPersonQuantity * realQuantity;

											foodComposition = foodMap.get(item.getFoodId());
											GoodsPurchasePlan purchase = new GoodsPurchasePlan();
											purchase.setCreateBy(loginContext.getActorId());
											purchase.setCreateTime(new Date());
											purchase.setGoodsId(foodComposition.getId());
											purchase.setGoodsName(foodComposition.getName());
											purchase.setGoodsNodeId(foodComposition.getNodeId());
											purchase.setQuantity(totalRealQuantity / 1000);// 克G转换为千克KG
											purchase.setUnit("KG");
											purchase.setSysFlag("Y");
											purchase.setBusinessStatus(0);
											purchase.setTenantId(loginContext.getTenantId());
											purchase.setPurchaseTime(date);
											purchase.setYear(year);
											purchase.setMonth(month);
											purchase.setWeek(week);
											purchase.setDay(fullday % 100);
											purchase.setFullDay(fullday);
											purchase.setSemester(SysConfig.getSemester());
											purchaseList.add(purchase);
										}
									}
								}
							}
						}

						GoodsInStockQuery query4 = new GoodsInStockQuery();
						query4.tenantId(loginContext.getTenantId());
						query4.businessStatus(9);// 已经确定入库的物品
						List<GoodsInStock> goods1 = getGoodsInStockService().list(query4);

						GoodsOutStockQuery query5 = new GoodsOutStockQuery();
						query5.tenantId(loginContext.getTenantId());
						query5.businessStatus(9);// 已经确定出库的物品
						List<GoodsOutStock> goods2 = getGoodsOutStockService().list(query5);

						Map<Long, Double> inStockMap = new HashMap<Long, Double>();
						Map<Long, Double> outStockMap = new HashMap<Long, Double>();

						if (goods1 != null && !goods1.isEmpty()) {
							for (GoodsInStock model : goods1) {
								Double value = inStockMap.get(model.getGoodsId());
								if (value == null) {
									value = new Double(0.0D);
								}
								value = value + model.getQuantity();
								inStockMap.put(model.getGoodsId(), value);
							}
							if (goods2 != null && !goods2.isEmpty()) {
								for (GoodsOutStock model : goods2) {
									Double value = outStockMap.get(model.getGoodsId());
									if (value == null) {
										value = new Double(0.0D);
									}
									value = value + model.getQuantity();
									outStockMap.put(model.getGoodsId(), value);
								}
							}
						}

						/**
						 * 计算总量
						 */
						Map<Long, Double> demandMap = new HashMap<Long, Double>();
						for (DietaryItem item : items) {
							/**
							 * 获取每种食品的食部
							 */
							// radical = radicalMap.get(item.getFoodId());
							// if (radical == null) {
							// radical = 100D;
							// }

							quantity = item.getQuantity();// 每一种食品的量

							// if (radical < 100) {
							/**
							 * 计算每一份的实际量
							 */
							// realQuantity = quantity * (1.0 + ((100 - radical)
							// / radical));
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

							totalRealQuantity = totalPersonQuantity * realQuantity;

							Double value = demandMap.get(item.getFoodId());
							if (value == null) {
								value = new Double(0.0D);
							}
							value = value + totalRealQuantity;
							demandMap.put(item.getFoodId(), value);
						}

						/**
						 * 根据库存数据，计算实际需要采购的量
						 */
						double remaining = 0;
						Double inStock = null;
						Double outStock = null;

						for (FoodComposition food : weekFoods) {
							if (food == null || !demandMap.containsKey(food.getId())) {
								continue;
							}
							inStock = inStockMap.get(food.getId());
							outStock = outStockMap.get(food.getId());
							totalRealQuantity = demandMap.get(food.getId());
							if (totalRealQuantity > 0) {
								if (inStock != null) {
									if (outStock != null) {
										remaining = inStock - outStock;
									} else {
										remaining = inStock;
									}
								}
								totalRealQuantity = totalRealQuantity - remaining;
							}

							GoodsPurchasePlan purchase = new GoodsPurchasePlan();
							purchase.setCreateBy(loginContext.getActorId());
							purchase.setCreateTime(new Date());
							purchase.setGoodsId(food.getId());
							purchase.setGoodsName(food.getName());
							purchase.setGoodsNodeId(food.getNodeId());
							purchase.setQuantity(totalRealQuantity / 1000);// 克G转换为千克KG
							purchase.setSysFlag("Y");
							purchase.setWeeklyFlag("Y");
							purchase.setUnit("KG");
							purchase.setBusinessStatus(0);
							purchase.setTenantId(loginContext.getTenantId());
							purchase.setPurchaseTime(DateUtils.getDateAfter(new Date(), 2));
							purchase.setYear(year);
							purchase.setMonth(month);
							purchase.setWeek(week);
							purchase.setSemester(SysConfig.getSemester());
							purchase.setDay(DateUtils.getYearMonthDay(purchase.getPurchaseTime()) % 100);
							purchase.setFullDay(DateUtils.getYearMonthDay(purchase.getPurchaseTime()));
							purchaseList.add(purchase);
						}

						/**
						 * 批量插入采购计划
						 */
						// getGoodsPurchasePlanService().bulkInsert(purchaseList);
						// getDietaryService().updatePurchaseFlag(planIds, "Y");
						getDietaryService().batchPurchase(loginContext.getTenantId(), planIds, "Y", purchaseList);
						logger.debug("----------------------生成用量计划表-----------------");
						createGoodsUsagePlan(loginContext, year, SysConfig.getSemester(), week);// 生成用量计划表
					}
				}
			}
		}
	}

	/**
	 * 根据食谱编排形成用量计划
	 * 
	 * @param loginContext
	 * @param year
	 * @param month
	 * @param week
	 */
	public void createGoodsUsagePlan(LoginContext loginContext, int year, int semester, int week) {
		PersonInfoHelper helper = new PersonInfoHelper();
		double totalPersonQuantity = helper.getPersonFoodAgeQuantity(loginContext.getTenantId(), 1.03);
		logger.debug("totalPersonQuantity=" + totalPersonQuantity);
		getGoodsPlanQuantityService().createGoodsUsagePlan(loginContext, year, semester, week, totalPersonQuantity);
	}

	public DictoryService getDictoryService() {
		if (dictoryService == null) {
			dictoryService = ContextFactory.getBean("dictoryService");
		}
		return dictoryService;
	}

	public DietaryItemService getDietaryItemService() {
		if (dietaryItemService == null) {
			dietaryItemService = ContextFactory.getBean("com.glaf.heathcare.service.dietaryItemService");
		}
		return dietaryItemService;
	}

	public DietaryService getDietaryService() {
		if (dietaryService == null) {
			dietaryService = ContextFactory.getBean("com.glaf.heathcare.service.dietaryService");
		}
		return dietaryService;
	}

	public DietaryTemplateService getDietaryTemplateService() {
		if (dietaryTemplateService == null) {
			dietaryTemplateService = ContextFactory.getBean("com.glaf.heathcare.service.dietaryTemplateService");
		}
		return dietaryTemplateService;
	}

	public FoodCompositionService getFoodCompositionService() {
		if (foodCompositionService == null) {
			foodCompositionService = ContextFactory.getBean("com.glaf.heathcare.service.foodCompositionService");
		}
		return foodCompositionService;
	}

	public GoodsInStockService getGoodsInStockService() {
		if (goodsInStockService == null) {
			goodsInStockService = ContextFactory.getBean("com.glaf.heathcare.service.goodsInStockService");
		}
		return goodsInStockService;
	}

	public GoodsOutStockService getGoodsOutStockService() {
		if (goodsOutStockService == null) {
			goodsOutStockService = ContextFactory.getBean("com.glaf.heathcare.service.goodsOutStockService");
		}
		return goodsOutStockService;
	}

	public GoodsPlanQuantityService getGoodsPlanQuantityService() {
		if (goodsPlanQuantityService == null) {
			goodsPlanQuantityService = ContextFactory.getBean("com.glaf.heathcare.service.goodsPlanQuantityService");
		}
		return goodsPlanQuantityService;
	}

	public GoodsPurchasePlanService getGoodsPurchasePlanService() {
		if (goodsPurchasePlanService == null) {
			goodsPurchasePlanService = ContextFactory.getBean("com.glaf.heathcare.service.goodsPurchasePlanService");
		}
		return goodsPurchasePlanService;
	}

	public PersonInfoService getPersonInfoService() {
		if (personInfoService == null) {
			personInfoService = ContextFactory.getBean("com.glaf.heathcare.service.personInfoService");
		}
		return personInfoService;
	}

	public SysTreeService getSysTreeService() {
		if (sysTreeService == null) {
			sysTreeService = ContextFactory.getBean("sysTreeService");
		}
		return sysTreeService;
	}

	public void setDictoryService(DictoryService dictoryService) {
		this.dictoryService = dictoryService;
	}

	public void setDietaryItemService(DietaryItemService dietaryItemService) {
		this.dietaryItemService = dietaryItemService;
	}

	public void setDietaryService(DietaryService dietaryService) {
		this.dietaryService = dietaryService;
	}

	public void setDietaryTemplateService(DietaryTemplateService dietaryTemplateService) {
		this.dietaryTemplateService = dietaryTemplateService;
	}

	public void setFoodCompositionService(FoodCompositionService foodCompositionService) {
		this.foodCompositionService = foodCompositionService;
	}

	public void setGoodsInStockService(GoodsInStockService goodsInStockService) {
		this.goodsInStockService = goodsInStockService;
	}

	public void setGoodsOutStockService(GoodsOutStockService goodsOutStockService) {
		this.goodsOutStockService = goodsOutStockService;
	}

	public void setGoodsPlanQuantityService(GoodsPlanQuantityService goodsPlanQuantityService) {
		this.goodsPlanQuantityService = goodsPlanQuantityService;
	}

	public void setGoodsPurchasePlanService(GoodsPurchasePlanService goodsPurchasePlanService) {
		this.goodsPurchasePlanService = goodsPurchasePlanService;
	}

	public void setPersonInfoService(PersonInfoService personInfoService) {
		this.personInfoService = personInfoService;
	}

	public void setSysTreeService(SysTreeService sysTreeService) {
		this.sysTreeService = sysTreeService;
	}

}
