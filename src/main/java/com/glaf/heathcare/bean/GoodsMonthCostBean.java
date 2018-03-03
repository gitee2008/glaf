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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.core.context.ContextFactory;

import com.glaf.heathcare.SysConfig;
import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.domain.GoodsInStock;
import com.glaf.heathcare.domain.GoodsOutStock;
import com.glaf.heathcare.domain.MealFeeCount;
import com.glaf.heathcare.query.FoodCompositionQuery;
import com.glaf.heathcare.query.GoodsInStockQuery;
import com.glaf.heathcare.query.GoodsOutStockQuery;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.heathcare.service.GoodsInStockService;
import com.glaf.heathcare.service.GoodsOutStockService;
import com.glaf.heathcare.service.MealFeeCountService;

public class GoodsMonthCostBean {

	protected final static Log logger = LogFactory.getLog(GoodsMonthCostBean.class);

	protected FoodCompositionService foodCompositionService;

	protected MealFeeCountService mealFeeCountService;

	protected GoodsInStockService goodsInStockService;

	protected GoodsOutStockService goodsOutStockService;

	/**
	 * 统计每月的物品消费情况
	 * 
	 * @param loginContext
	 * @param year
	 * @param month
	 */
	public void execute(String tenantId, int year, int month) {
		GoodsInStockQuery query4 = new GoodsInStockQuery();
		query4.tenantId(tenantId);
		query4.year(year);
		query4.month(month);
		query4.businessStatus(9);// 已经确定本月入库的物品
		query4.setOrderBy(" E.INSTOCKTIME_ asc ");// 按入库时间排序，最先入库的排在前面
		List<GoodsInStock> goods1 = getGoodsInStockService().list(query4);

		Map<Long, Double> totalPriceMap = new HashMap<Long, Double>();// 本月入库的分类汇总
		Map<Long, Double> priceMap = new HashMap<Long, Double>();// 物品价格
		Map<Long, Double> qtyMap = new HashMap<Long, Double>();// 物品数量

		if (goods1 != null && !goods1.isEmpty()) {
			for (GoodsInStock m : goods1) {
				if (m.getGoodsNodeId() == 4402 || m.getGoodsNodeId() == 4418 || m.getGoodsNodeId() == 4419) {
					continue;// 要根据出库量来计算
				}
				Double value = totalPriceMap.get(m.getGoodsNodeId());
				if (value == null) {
					value = new Double(0.0D);
				}
				value = value + m.getTotalPrice();
				totalPriceMap.put(m.getGoodsNodeId(), value);// 每个类别的合计金额

				Double qty = qtyMap.get(m.getGoodsId());
				if (qty == null) {
					qty = new Double(0.0D);
				}
				qty = qty + m.getQuantity();
				qtyMap.put(m.getGoodsId(), qty);// 每个物品的合计数量

				Double price = priceMap.get(m.getGoodsId());
				if (price == null) {
					price = new Double(0.0D);
				}
				price = price + m.getTotalPrice();
				priceMap.put(m.getGoodsId(), price);// 每个物品的合计价格
			}
		}

		GoodsOutStockQuery query5 = new GoodsOutStockQuery();
		query5.tenantId(tenantId);
		query5.year(year);
		query5.month(month);
		query5.businessStatus(9);// 已经确定本月出库的物品
		query5.setOrderBy(" E.OUTSTOCKTIME_ asc ");// 按出库时间排序，最先出库的排在前面
		List<GoodsOutStock> goods2 = getGoodsOutStockService().list(query5);
		if (goods2 != null && !goods2.isEmpty()) {
			for (GoodsOutStock m : goods2) {
				if (m.getGoodsNodeId() == 4402 || m.getGoodsNodeId() == 4418 || m.getGoodsNodeId() == 4419) {
					Double value = totalPriceMap.get(m.getGoodsNodeId());
					if (value == null) {
						value = new Double(0.0D);
					}
					value = value + m.getTotalPrice();
					totalPriceMap.put(m.getGoodsNodeId(), value);// 每个类别的合计金额

					Double qty = qtyMap.get(m.getGoodsId());
					if (qty == null) {
						qty = new Double(0.0D);
					}
					qty = qty + m.getQuantity();
					qtyMap.put(m.getGoodsId(), qty);// 每个物品的合计数量

					Double price = priceMap.get(m.getGoodsId());
					if (price == null) {
						price = new Double(0.0D);
					}
					price = price + m.getTotalPrice();
					priceMap.put(m.getGoodsId(), price);// 每个物品的合计价格
				}
			}
		}

		if (totalPriceMap.size() > 0) {
			List<MealFeeCount> rows = new ArrayList<MealFeeCount>();
			Set<Entry<Long, Double>> entrySet = totalPriceMap.entrySet();
			for (Entry<Long, Double> entry : entrySet) {
				Long key = entry.getKey();
				Double value = entry.getValue();
				MealFeeCount model = new MealFeeCount();
				model.setMonth(month);
				model.setYear(year);
				model.setTenantId(tenantId);
				model.setSemester(SysConfig.getSemester(month));
				model.setName("category_fee_" + String.valueOf(key));
				model.setValue(value);
				model.setType("CategoryPrice");
				rows.add(model);
			}
			getMealFeeCountService().saveAll(tenantId, year, month, "CategoryPrice", rows);
		}

		if (qtyMap.size() > 0) {
			List<MealFeeCount> rows = new ArrayList<MealFeeCount>();
			Set<Entry<Long, Double>> entrySet = qtyMap.entrySet();
			for (Entry<Long, Double> entry : entrySet) {
				Long key = entry.getKey();
				Double value = entry.getValue();
				MealFeeCount model = new MealFeeCount();
				model.setMonth(month);
				model.setYear(year);
				model.setTenantId(tenantId);
				model.setSemester(SysConfig.getSemester(month));
				model.setName("goods_quantity_" + String.valueOf(key));
				model.setValue(value);
				model.setType("GoodsQuantity");
				rows.add(model);
			}
			getMealFeeCountService().saveAll(tenantId, year, month, "GoodsQuantity", rows);
		}

		if (priceMap.size() > 0) {
			FoodCompositionQuery query = new FoodCompositionQuery();
			query.locked(0);
			List<FoodComposition> foods = getFoodCompositionService().list(query);
			Map<Long, FoodComposition> foodMap = new HashMap<Long, FoodComposition>();
			for (FoodComposition food : foods) {
				foodMap.put(food.getId(), food);
			}
			FoodComposition food = null;
			double totalPrice = 0.0;
			Set<Long> exists = new HashSet<Long>();
			Map<String, Double> priceXYMap = new HashMap<String, Double>();// 物品价格
			List<MealFeeCount> rows = new ArrayList<MealFeeCount>();
			Set<Entry<Long, Double>> entrySet = priceMap.entrySet();
			for (Entry<Long, Double> entry : entrySet) {
				Long key = entry.getKey();
				Double value = entry.getValue();
				MealFeeCount model = new MealFeeCount();
				model.setMonth(month);
				model.setYear(year);
				model.setTenantId(tenantId);
				model.setSemester(SysConfig.getSemester(month));
				model.setName("goods_fee_" + String.valueOf(key));
				model.setValue(value);
				model.setType("GoodsFee");
				rows.add(model);
				totalPrice = totalPrice + value;

				food = foodMap.get(key);
				if (food != null) {

					if (food.getNodeId() == 4402) {
						if (StringUtils.contains(food.getName(), "米") || StringUtils.contains(food.getName(), "面")) {
							if (!exists.contains(food.getId())) {
								Double price = priceXYMap.get("riceFee");
								if (price == null) {
									price = new Double(0.0D);
								}
								price = price + value;
								priceXYMap.put("riceFee", price);// 米面的合计价格
								exists.add(food.getId());
							}
						}
					}

					if (StringUtils.isNotEmpty(food.getCerealFlag())) {
						if (StringUtils.equals(food.getCerealFlag(), "R")) {
							if (!exists.contains(food.getId())) {
								Double price = priceXYMap.get("riceFee");
								if (price == null) {
									price = new Double(0.0D);
								}
								price = price + value;
								priceXYMap.put("riceFee", price);// 米面的合计价格
								exists.add(food.getId());
							}
						} else if (StringUtils.equals(food.getCerealFlag(), "Z")) {
							Double price = priceXYMap.get("roughageFee");
							if (price == null) {
								price = new Double(0.0D);
							}
							price = price + value;
							priceXYMap.put("roughageFee", price);// 粗粮的合计价格
						}
					}

					if (food.getNodeId() == 4403) {
						Double price = priceXYMap.get("potatoFee");
						if (price == null) {
							price = new Double(0.0D);
						}
						price = price + value;
						priceXYMap.put("potatoFee", price);// 薯类的合计价格
					} else if (food.getNodeId() == 4404) {
						Double price = priceXYMap.get("beansFee");
						if (price == null) {
							price = new Double(0.0D);
						}
						price = price + value;
						priceXYMap.put("beansFee", price);// 豆类的合计价格

						if (StringUtils.contains(food.getName(), "黑豆") || StringUtils.contains(food.getName(), "黄豆")
								|| StringUtils.contains(food.getName(), "青豆")) {
							Double price2 = priceXYMap.get("mbeansFee");
							if (price2 == null) {
								price2 = new Double(0.0D);
							}
							price2 = price2 + value;
							priceXYMap.put("mbeansFee", price2);// 大豆的合计价格
						} else {
							Double price2 = priceXYMap.get("mixedBeansFee");
							if (price2 == null) {
								price2 = new Double(0.0D);
							}
							price2 = price2 + value;
							priceXYMap.put("mixedBeansFee", price2);// 杂豆的合计价格
						}

					} else if (food.getNodeId() == 4405) {
						Double price = priceXYMap.get("vegetableFee");
						if (price == null) {
							price = new Double(0.0D);
						}
						price = price + value;
						priceXYMap.put("vegetableFee", price);// 蔬菜类的合计价格
					} else if (food.getNodeId() == 4406) {
						Double price = priceXYMap.get("fungusFee");
						if (price == null) {
							price = new Double(0.0D);
						}
						price = price + value;
						priceXYMap.put("fungusFee", price);// 菌藻类的合计价格
					} else if (food.getNodeId() == 4407) {
						Double price = priceXYMap.get("fruitFree");
						if (price == null) {
							price = new Double(0.0D);
						}
						price = price + value;
						priceXYMap.put("fruitFree", price);// 水果类的合计价格
					} else if (food.getNodeId() == 4408) {
						Double price = priceXYMap.get("nutseedFee");
						if (price == null) {
							price = new Double(0.0D);
						}
						price = price + value;
						priceXYMap.put("nutseedFee", price);// 坚果、种子类的合计价格
					} else if (food.getNodeId() == 4409) {
						Double price = priceXYMap.get("livestockFee");
						if (price == null) {
							price = new Double(0.0D);
						}
						price = price + value;
						priceXYMap.put("livestockFee", price);// 畜肉类的合计价格
					} else if (food.getNodeId() == 4410) {
						Double price = priceXYMap.get("birdsFee");
						if (price == null) {
							price = new Double(0.0D);
						}
						price = price + value;
						priceXYMap.put("birdsFee", price);// 禽肉类的合计价格
					} else if (food.getNodeId() == 4411) {
						Double price = priceXYMap.get("milkFee");
						if (price == null) {
							price = new Double(0.0D);
						}
						price = price + value;
						priceXYMap.put("milkFee", price);// 乳类的合计价格
					} else if (food.getNodeId() == 4412) {
						Double price = priceXYMap.get("eggFee");
						if (price == null) {
							price = new Double(0.0D);
						}
						price = price + value;
						priceXYMap.put("eggFee", price);// 蛋类的合计价格
					} else if (food.getNodeId() == 4413) {
						Double price = priceXYMap.get("aquaticFee");
						if (price == null) {
							price = new Double(0.0D);
						}
						price = price + value;
						priceXYMap.put("aquaticFee", price);// 水产品类的合计价格
					} else if (food.getNodeId() == 4418) {
						Double price = priceXYMap.get("oilFee");
						if (price == null) {
							price = new Double(0.0D);
						}
						price = price + value;
						priceXYMap.put("oilFee", price);// 油脂类的合计价格

						if (StringUtils.contains(food.getName(), "猪") || StringUtils.contains(food.getName(), "牛")
								|| StringUtils.contains(food.getName(), "羊")
								|| StringUtils.contains(food.getName(), "鸡")
								|| StringUtils.contains(food.getName(), "鸭")
								|| StringUtils.contains(food.getName(), "鹅")
								|| StringUtils.contains(food.getName(), "鱼")) {
							Double price2 = priceXYMap.get("animalOilFee");
							if (price2 == null) {
								price2 = new Double(0.0D);
							}
							price2 = price2 + value;
							priceXYMap.put("animalOilFee", price2);// 动物油的合计价格
						} else {
							Double price2 = priceXYMap.get("vegetableOilFee");
							if (price2 == null) {
								price2 = new Double(0.0D);
							}
							price2 = price2 + value;
							priceXYMap.put("vegetableOilFee", price2);// 植物油的合计价格
						}
					} else if (food.getNodeId() == 4419) {
						Double price = priceXYMap.get("condimentFee");
						if (price == null) {
							price = new Double(0.0D);
						}
						price = price + value;
						priceXYMap.put("condimentFee", price);// 调味品类的合计价格
					}
				}
			}

			Set<Entry<String, Double>> entrySet2 = priceXYMap.entrySet();
			for (Entry<String, Double> entry : entrySet2) {
				String key = entry.getKey();
				Double value = entry.getValue();
				MealFeeCount model = new MealFeeCount();
				model.setMonth(month);
				model.setYear(year);
				model.setTenantId(tenantId);
				model.setSemester(SysConfig.getSemester(month));
				model.setName(key);
				model.setValue(value);
				model.setType("GoodsFee");
				rows.add(model);

				MealFeeCount model2 = new MealFeeCount();
				model2.setMonth(month);
				model2.setYear(year);
				model2.setTenantId(tenantId);
				model2.setSemester(SysConfig.getSemester(month));
				model2.setName(key + "Percent");
				if (totalPrice > 0) {
					model2.setValue(Math.round((value / totalPrice * 10000D)) / 100D);
				}
				model2.setType("GoodsFeePercent");
				rows.add(model2);
			}

			MealFeeCount model = new MealFeeCount();
			model.setMonth(month);
			model.setYear(year);
			model.setTenantId(tenantId);
			model.setSemester(SysConfig.getSemester(month));
			model.setName("totalPrice");
			model.setValue(totalPrice);
			model.setType("GoodsFee");
			rows.add(model);
			getMealFeeCountService().saveAll(tenantId, year, month, "GoodsFee", rows);
		}

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

	public MealFeeCountService getMealFeeCountService() {
		if (mealFeeCountService == null) {
			mealFeeCountService = ContextFactory.getBean("com.glaf.heathcare.service.mealFeeCountService");
		}
		return mealFeeCountService;
	}

	public void setGoodsInStockService(GoodsInStockService goodsInStockService) {
		this.goodsInStockService = goodsInStockService;
	}

	public void setGoodsOutStockService(GoodsOutStockService goodsOutStockService) {
		this.goodsOutStockService = goodsOutStockService;
	}

	public void setMealFeeCountService(MealFeeCountService mealFeeCountService) {
		this.mealFeeCountService = mealFeeCountService;
	}

}
