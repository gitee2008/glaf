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
import java.util.List;
import java.util.Map;

import com.glaf.core.context.ContextFactory;
import com.glaf.heathcare.domain.CompositionCount;
import com.glaf.heathcare.domain.DietaryItem;
import com.glaf.heathcare.domain.DietaryTemplate;
import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.query.DietaryItemQuery;
import com.glaf.heathcare.query.DietaryTemplateQuery;
import com.glaf.heathcare.query.FoodCompositionQuery;
import com.glaf.heathcare.service.DietaryCountService;
import com.glaf.heathcare.service.DietaryItemService;
import com.glaf.heathcare.service.DietaryTemplateService;
import com.glaf.heathcare.service.FoodCompositionService;

public class DietaryTemplateBean {

	protected DietaryCountService dietaryCountService;

	protected DietaryItemService dietaryItemService;

	protected DietaryTemplateService dietaryTemplateService;

	protected FoodCompositionService foodCompositionService;

	public DietaryTemplateBean() {

	}

	public void calculateAll(List<Long> templateIds) {
		DietaryItemQuery query = new DietaryItemQuery();
		query.templateIds(templateIds);
		List<DietaryItem> items = getDietaryItemService().list(query);
		List<Long> foodIds = new ArrayList<Long>();
		for (DietaryItem item : items) {
			if (!foodIds.contains(item.getFoodId())) {
				foodIds.add(item.getFoodId());
			}
		}

		FoodCompositionQuery query2 = new FoodCompositionQuery();
		query2.setIds(foodIds);
		List<FoodComposition> foods = getFoodCompositionService().list(query2);// 获取食物成分
		Map<Long, FoodComposition> foodMap = new HashMap<Long, FoodComposition>();
		for (FoodComposition food : foods) {
			foodMap.put(food.getId(), food);
		}

		DietaryTemplateQuery query4 = new DietaryTemplateQuery();
		query4.setIds(templateIds);
		List<DietaryTemplate> templates = getDietaryTemplateService().list(query4);
		if (templates != null && !templates.isEmpty()) {
			for (DietaryTemplate dietaryTemplate : templates) {
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
					if (!(item.getTemplateId() == dietaryTemplate.getId())) {
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
			}
			getDietaryTemplateService().updateAll(templates);
		}
	}

	public CompositionCount countMulti(List<Long> templateIds) {
		CompositionCount count = new CompositionCount();
		DietaryItemQuery query = new DietaryItemQuery();
		query.templateIds(templateIds);
		List<DietaryItem> items = getDietaryItemService().list(query);
		List<Long> foodIds = new ArrayList<Long>();
		for (DietaryItem item : items) {
			if (!foodIds.contains(item.getFoodId())) {
				foodIds.add(item.getFoodId());
			}
		}

		FoodCompositionQuery query2 = new FoodCompositionQuery();
		query2.setIds(foodIds);
		List<FoodComposition> foods = getFoodCompositionService().list(query2);// 获取食物成分
		Map<Long, FoodComposition> foodMap = new HashMap<Long, FoodComposition>();
		for (FoodComposition food : foods) {
			foodMap.put(food.getId(), food);
		}

		double quantity = 0;
		// double radical = 0;
		double realQuantity = 0;
		for (DietaryItem item : items) {
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
			quantity = item.getQuantity();
			// radical = food.getRadical();
			realQuantity = 0;
			// if (radical < 100 && radical > 0) {
			/**
			 * 计算每一份的实际量
			 */
			// realQuantity = quantity * (radical / 100);
			// } else {
			realQuantity = quantity;
			// }

			double factor = realQuantity / 100;// 转换成100g为标准

			count.setHeatEnergy(count.getHeatEnergy() + food.getHeatEnergy() * factor);// 累加计算
			count.setProtein(count.getProtein() + food.getProtein() * factor);// 累加计算
			count.setFat(count.getFat() + food.getFat() * factor);// 累加计算
			count.setCarbohydrate(count.getCarbohydrate() + food.getCarbohydrate() * factor);// 累加计算
			count.setVitaminA(count.getVitaminA() + food.getVitaminA() * factor);// 累加计算
			count.setVitaminB1(count.getVitaminB1() + food.getVitaminB1() * factor);// 累加计算
			count.setVitaminB2(count.getVitaminB2() + food.getVitaminB2() * factor);// 累加计算
			count.setVitaminB6(count.getVitaminB6() + food.getVitaminB6() * factor);// 累加计算
			count.setVitaminB12(count.getVitaminB12() + food.getVitaminB12() * factor);// 累加计算
			count.setVitaminC(count.getVitaminC() + food.getVitaminC() * factor);// 累加计算
			count.setCarotene(count.getCarotene() + food.getCarotene() * factor);// 累加计算
			count.setRetinol(count.getRetinol() + food.getRetinol() * factor);// 累加计算
			count.setNicotinicCid(count.getNicotinicCid() + food.getNicotinicCid() * factor);// 累加计算
			count.setCalcium(count.getCalcium() + food.getCalcium() * factor);// 累加计算
			count.setIron(count.getIron() + food.getIron() * factor);// 累加计算
			count.setZinc(count.getZinc() + food.getZinc() * factor);// 累加计算
			count.setIodine(count.getIodine() + food.getIodine() * factor);// 累加计算
			count.setPhosphorus(count.getPhosphorus() + food.getPhosphorus() * factor);// 累加计算
			count.setQuantity(count.getQuantity() + item.getQuantity());
		}

		return count;
	}

	public List<CompositionCount> countMultiItems(List<Long> templateIds) {
		List<CompositionCount> countList = new ArrayList<CompositionCount>();
		Map<Long, CompositionCount> countMap = new HashMap<Long, CompositionCount>();
		DietaryItemQuery query = new DietaryItemQuery();
		query.templateIds(templateIds);
		List<DietaryItem> items = getDietaryItemService().list(query);
		List<Long> foodIds = new ArrayList<Long>();
		for (DietaryItem item : items) {
			if (!foodIds.contains(item.getFoodId())) {
				foodIds.add(item.getFoodId());
			}
		}

		FoodCompositionQuery query2 = new FoodCompositionQuery();
		query2.setIds(foodIds);
		List<FoodComposition> foods = getFoodCompositionService().list(query2);// 获取食物成分
		Map<Long, FoodComposition> foodMap = new HashMap<Long, FoodComposition>();
		for (FoodComposition food : foods) {
			foodMap.put(food.getId(), food);
		}

		double quantity = 0;
		// double radical = 0;
		double realQuantity = 0;
		for (DietaryItem item : items) {
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
			quantity = item.getQuantity();
			// radical = food.getRadical();
			realQuantity = 0;
			// if (radical < 100 && radical > 0) {
			/**
			 * 计算每一份的实际量
			 */
			// realQuantity = quantity * (radical / 100);
			// } else {
			realQuantity = quantity;
			// }

			double factor = realQuantity / 100;// 转换成100g为标准
			CompositionCount count = countMap.get(food.getNodeId());
			if (count == null) {
				count = new CompositionCount();
				count.setNodeId(food.getNodeId());
				countList.add(count);
			}
			count.setHeatEnergy(count.getHeatEnergy() + food.getHeatEnergy() * factor);// 累加计算
			count.setProtein(count.getProtein() + food.getProtein() * factor);// 累加计算
			count.setFat(count.getFat() + food.getFat() * factor);// 累加计算
			count.setCarbohydrate(count.getCarbohydrate() + food.getCarbohydrate() * factor);// 累加计算
			count.setVitaminA(count.getVitaminA() + food.getVitaminA() * factor);// 累加计算
			count.setVitaminB1(count.getVitaminB1() + food.getVitaminB1() * factor);// 累加计算
			count.setVitaminB2(count.getVitaminB2() + food.getVitaminB2() * factor);// 累加计算
			count.setVitaminB6(count.getVitaminB6() + food.getVitaminB6() * factor);// 累加计算
			count.setVitaminB12(count.getVitaminB12() + food.getVitaminB12() * factor);// 累加计算
			count.setVitaminC(count.getVitaminC() + food.getVitaminC() * factor);// 累加计算
			count.setCarotene(count.getCarotene() + food.getCarotene() * factor);// 累加计算
			count.setRetinol(count.getRetinol() + food.getRetinol() * factor);// 累加计算
			count.setNicotinicCid(count.getNicotinicCid() + food.getNicotinicCid() * factor);// 累加计算
			count.setCalcium(count.getCalcium() + food.getCalcium() * factor);// 累加计算
			count.setIron(count.getIron() + food.getIron() * factor);// 累加计算
			count.setZinc(count.getZinc() + food.getZinc() * factor);// 累加计算
			count.setIodine(count.getIodine() + food.getIodine() * factor);// 累加计算
			count.setPhosphorus(count.getPhosphorus() + food.getPhosphorus() * factor);// 累加计算
			count.setQuantity(count.getQuantity() + item.getQuantity());
			countMap.put(food.getNodeId(), count);
		}

		return countList;
	}

	public DietaryCountService getDietaryCountService() {
		if (dietaryCountService == null) {
			dietaryCountService = ContextFactory.getBean("com.glaf.heathcare.service.dietaryCountService");
		}
		return dietaryCountService;
	}

	public DietaryItemService getDietaryItemService() {
		if (dietaryItemService == null) {
			dietaryItemService = ContextFactory.getBean("com.glaf.heathcare.service.dietaryItemService");
		}
		return dietaryItemService;
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

}
