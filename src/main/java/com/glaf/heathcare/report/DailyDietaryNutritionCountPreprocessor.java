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

package com.glaf.heathcare.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.ParamUtils;
import com.glaf.heathcare.domain.Dietary;
import com.glaf.heathcare.domain.DietaryItem;
import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.query.DietaryItemQuery;
import com.glaf.heathcare.query.DietaryQuery;
import com.glaf.heathcare.query.FoodCompositionQuery;
import com.glaf.heathcare.service.DietaryItemService;
import com.glaf.heathcare.service.DietaryService;
import com.glaf.heathcare.service.FoodCompositionService;

public class DailyDietaryNutritionCountPreprocessor implements IReportPreprocessor {

	@Override
	public void prepare(LoginContext loginContext, int year, int month, Map<String, Object> params) {
		int fullDay = ParamUtils.getInt(params, "fullDay");
		if (fullDay != 0) {
			DietaryService dietaryService = ContextFactory.getBean("com.glaf.heathcare.service.dietaryService");
			DietaryItemService dietaryItemService = ContextFactory
					.getBean("com.glaf.heathcare.service.dietaryItemService");
			FoodCompositionService foodCompositionService = ContextFactory
					.getBean("com.glaf.heathcare.service.foodCompositionService");

			FoodCompositionQuery q = new FoodCompositionQuery();
			List<FoodComposition> foods = foodCompositionService.list(q);
			Map<Long, FoodComposition> foodMap = new HashMap<Long, FoodComposition>();
			if (foods != null && !foods.isEmpty()) {
				for (FoodComposition food : foods) {
					foodMap.put(food.getId(), food);
				}
			}

			DietaryQuery qx = new DietaryQuery();
			qx.fullDay(fullDay);
			qx.tenantId(loginContext.getTenantId());

			List<Dietary> dietaries = dietaryService.list(qx);
			List<Long> dietaryIds = new ArrayList<Long>();
			for (Dietary tpl : dietaries) {
				dietaryIds.add(tpl.getId());
			}
			DietaryItemQuery query = new DietaryItemQuery();
			query.tenantId(loginContext.getTenantId());
			query.dietaryIds(dietaryIds);
			List<DietaryItem> rows = dietaryItemService.list(query);
			if (rows != null && !rows.isEmpty()) {
				Map<Long, FoodComposition> foodMap2 = new HashMap<Long, FoodComposition>();
				List<FoodComposition> rows0 = new ArrayList<FoodComposition>();// 谷类薯类
				List<FoodComposition> rows1 = new ArrayList<FoodComposition>();// 蔬菜菌藻水果
				List<FoodComposition> rows2 = new ArrayList<FoodComposition>();// 畜肉、禽肉、乳类、蛋类及鱼虾蟹贝类
				List<FoodComposition> rows3 = new ArrayList<FoodComposition>();// 其他
				FoodComposition fd = null;
				for (DietaryItem item : rows) {
					fd = foodMap.get(item.getFoodId());
					if (fd == null) {
						continue;
					}
					switch ((int) fd.getNodeId()) {
					case 4402:// 谷类
					case 4403:// 薯类
						FoodComposition food0 = foodMap2.get(item.getFoodId());
						if (food0 == null) {
							food0 = new FoodComposition();
							food0.setId(item.getFoodId());
							food0.setNodeId(fd.getNodeId());
							foodMap2.put(item.getFoodId(), food0);
							rows0.add(food0);
						}
						food0.setQuantity(food0.getQuantity() + item.getQuantity());
						break;
					case 4405:// 蔬菜
					case 4406:// 菌藻
					case 4407:// 水果
						FoodComposition food1 = foodMap2.get(item.getFoodId());
						if (food1 == null) {
							food1 = new FoodComposition();
							food1.setId(item.getFoodId());
							food1.setNodeId(fd.getNodeId());
							foodMap2.put(item.getFoodId(), food1);
							rows1.add(food1);
						}
						food1.setQuantity(food1.getQuantity() + item.getQuantity());
						break;
					case 4409:
					case 4410:
					case 4411:
					case 4412:
					case 4413:
						FoodComposition food2 = foodMap2.get(item.getFoodId());
						if (food2 == null) {
							food2 = new FoodComposition();
							food2.setId(item.getFoodId());
							food2.setNodeId(fd.getNodeId());
							foodMap2.put(item.getFoodId(), food2);
							rows2.add(food2);
						}
						food2.setQuantity(food2.getQuantity() + item.getQuantity());
						break;
					default:
						FoodComposition food3 = foodMap2.get(item.getFoodId());
						if (food3 == null) {
							food3 = new FoodComposition();
							food3.setId(item.getFoodId());
							food3.setNodeId(fd.getNodeId());
							foodMap2.put(item.getFoodId(), food3);
							rows3.add(food3);
						}
						food3.setQuantity(food3.getQuantity() + item.getQuantity());
						break;
					}
				}

				FoodComposition total = new FoodComposition();
				for (FoodComposition food : rows0) {
					fd = foodMap.get(food.getId());
					double quantity = food.getQuantity();
					if (fd != null) {
						// quantity = quantity * fd.getRadical() / 100D;// 转成可食部的量
						food.setName(fd.getName());
						food.setQuantity(quantity);
						food.setHeatEnergy(fd.getHeatEnergy() * quantity / 100D);
						food.setProtein(fd.getProtein() * quantity / 100D);
						food.setFat(fd.getFat() * quantity / 100D);
						food.setCarbohydrate(fd.getCarbohydrate() * quantity / 100D);
						food.setVitaminA(fd.getVitaminA() * quantity / 100D);
						food.setVitaminB1(fd.getVitaminB1() * quantity / 100D);
						food.setVitaminB2(fd.getVitaminB2() * quantity / 100D);
						food.setVitaminC(fd.getVitaminC() * quantity / 100D);
						food.setVitaminE(fd.getVitaminE() * quantity / 100D);
						food.setNicotinicCid(fd.getNicotinicCid() * quantity / 100D);
						food.setRetinol(fd.getRetinol() * quantity / 100D);
						food.setCalcium(fd.getCalcium() * quantity / 100D);
						food.setIron(fd.getIron() * quantity / 100D);
						food.setZinc(fd.getZinc() * quantity / 100D);
						food.setIodine(fd.getIodine() * quantity / 100D);
						food.setPhosphorus(fd.getPhosphorus() * quantity / 100D);

						total.setQuantity(total.getQuantity() + quantity);
						total.setHeatEnergy(total.getHeatEnergy() + food.getHeatEnergy());
						total.setProtein(total.getProtein() + food.getProtein());
						total.setFat(total.getFat() + food.getFat());
						total.setCarbohydrate(total.getCarbohydrate() + food.getCarbohydrate());
						total.setVitaminA(total.getVitaminA() + food.getVitaminA());
						total.setVitaminB1(total.getVitaminB1() + food.getVitaminB1());
						total.setVitaminB2(total.getVitaminB2() + food.getVitaminB2());
						total.setVitaminB6(total.getVitaminB6() + food.getVitaminB6());
						total.setVitaminB12(total.getVitaminB12() + food.getVitaminB12());
						total.setVitaminC(total.getVitaminC() + food.getVitaminC());
						total.setVitaminE(total.getVitaminE() + food.getVitaminE());
						total.setNicotinicCid(total.getNicotinicCid() + food.getNicotinicCid());
						total.setRetinol(total.getRetinol() + food.getRetinol());
						total.setCalcium(total.getCalcium() + food.getCalcium());
						total.setIron(total.getIron() + food.getIron());
						total.setZinc(total.getZinc() + food.getZinc());
						total.setIodine(total.getIodine() + food.getIodine());
						total.setPhosphorus(total.getPhosphorus() + food.getPhosphorus());
					}
				}

				for (FoodComposition food : rows1) {
					fd = foodMap.get(food.getId());
					double quantity = food.getQuantity();
					if (fd != null) {
						// quantity = quantity * fd.getRadical() / 100D;// 转成可食部的量
						food.setName(fd.getName());
						food.setQuantity(quantity);
						food.setHeatEnergy(fd.getHeatEnergy() * quantity / 100D);
						food.setProtein(fd.getProtein() * quantity / 100D);
						food.setFat(fd.getFat() * quantity / 100D);
						food.setCarbohydrate(fd.getCarbohydrate() * quantity / 100D);
						food.setVitaminA(fd.getVitaminA() * quantity / 100D);
						food.setVitaminB1(fd.getVitaminB1() * quantity / 100D);
						food.setVitaminB2(fd.getVitaminB2() * quantity / 100D);
						food.setVitaminC(fd.getVitaminC() * quantity / 100D);
						food.setVitaminE(fd.getVitaminE() * quantity / 100D);
						food.setNicotinicCid(fd.getNicotinicCid() * quantity / 100D);
						food.setRetinol(fd.getRetinol() * quantity / 100D);
						food.setCalcium(fd.getCalcium() * quantity / 100D);
						food.setIron(fd.getIron() * quantity / 100D);
						food.setZinc(fd.getZinc() * quantity / 100D);
						food.setIodine(fd.getIodine() * quantity / 100D);
						food.setPhosphorus(fd.getPhosphorus() * quantity / 100D);

						total.setQuantity(total.getQuantity() + quantity);
						total.setHeatEnergy(total.getHeatEnergy() + food.getHeatEnergy());
						total.setProtein(total.getProtein() + food.getProtein());
						total.setFat(total.getFat() + food.getFat());
						total.setCarbohydrate(total.getCarbohydrate() + food.getCarbohydrate());
						total.setVitaminA(total.getVitaminA() + food.getVitaminA());
						total.setVitaminB1(total.getVitaminB1() + food.getVitaminB1());
						total.setVitaminB2(total.getVitaminB2() + food.getVitaminB2());
						total.setVitaminB6(total.getVitaminB6() + food.getVitaminB6());
						total.setVitaminB12(total.getVitaminB12() + food.getVitaminB12());
						total.setVitaminC(total.getVitaminC() + food.getVitaminC());
						total.setVitaminE(total.getVitaminE() + food.getVitaminE());
						total.setNicotinicCid(total.getNicotinicCid() + food.getNicotinicCid());
						total.setRetinol(total.getRetinol() + food.getRetinol());
						total.setCalcium(total.getCalcium() + food.getCalcium());
						total.setIron(total.getIron() + food.getIron());
						total.setZinc(total.getZinc() + food.getZinc());
						total.setIodine(total.getIodine() + food.getIodine());
						total.setPhosphorus(total.getPhosphorus() + food.getPhosphorus());
					}
				}

				for (FoodComposition food : rows2) {
					fd = foodMap.get(food.getId());
					double quantity = food.getQuantity();
					if (fd != null) {
						// quantity = quantity * fd.getRadical() / 100D;// 转成可食部的量
						food.setName(fd.getName());
						food.setQuantity(quantity);
						food.setHeatEnergy(fd.getHeatEnergy() * quantity / 100D);
						food.setProtein(fd.getProtein() * quantity / 100D);
						food.setFat(fd.getFat() * quantity / 100D);
						food.setCarbohydrate(fd.getCarbohydrate() * quantity / 100D);
						food.setVitaminA(fd.getVitaminA() * quantity / 100D);
						food.setVitaminB1(fd.getVitaminB1() * quantity / 100D);
						food.setVitaminB2(fd.getVitaminB2() * quantity / 100D);
						food.setVitaminC(fd.getVitaminC() * quantity / 100D);
						food.setVitaminE(fd.getVitaminE() * quantity / 100D);
						food.setNicotinicCid(fd.getNicotinicCid() * quantity / 100D);
						food.setRetinol(fd.getRetinol() * quantity / 100D);
						food.setCalcium(fd.getCalcium() * quantity / 100D);
						food.setIron(fd.getIron() * quantity / 100D);
						food.setZinc(fd.getZinc() * quantity / 100D);
						food.setIodine(fd.getIodine() * quantity / 100D);
						food.setPhosphorus(fd.getPhosphorus() * quantity / 100D);

						total.setQuantity(total.getQuantity() + quantity);
						total.setHeatEnergy(total.getHeatEnergy() + food.getHeatEnergy());
						total.setProtein(total.getProtein() + food.getProtein());
						total.setFat(total.getFat() + food.getFat());
						total.setCarbohydrate(total.getCarbohydrate() + food.getCarbohydrate());
						total.setVitaminA(total.getVitaminA() + food.getVitaminA());
						total.setVitaminB1(total.getVitaminB1() + food.getVitaminB1());
						total.setVitaminB2(total.getVitaminB2() + food.getVitaminB2());
						total.setVitaminB6(total.getVitaminB6() + food.getVitaminB6());
						total.setVitaminB12(total.getVitaminB12() + food.getVitaminB12());
						total.setVitaminC(total.getVitaminC() + food.getVitaminC());
						total.setVitaminE(total.getVitaminE() + food.getVitaminE());
						total.setNicotinicCid(total.getNicotinicCid() + food.getNicotinicCid());
						total.setRetinol(total.getRetinol() + food.getRetinol());
						total.setCalcium(total.getCalcium() + food.getCalcium());
						total.setIron(total.getIron() + food.getIron());
						total.setZinc(total.getZinc() + food.getZinc());
						total.setIodine(total.getIodine() + food.getIodine());
						total.setPhosphorus(total.getPhosphorus() + food.getPhosphorus());
					}
				}

				for (FoodComposition food : rows3) {
					fd = foodMap.get(food.getId());
					double quantity = food.getQuantity();
					if (fd != null) {
						// quantity = quantity * fd.getRadical() / 100D;// 转成可食部的量
						food.setName(fd.getName());
						food.setQuantity(quantity);
						food.setHeatEnergy(fd.getHeatEnergy() * quantity / 100D);
						food.setProtein(fd.getProtein() * quantity / 100D);
						food.setFat(fd.getFat() * quantity / 100D);
						food.setCarbohydrate(fd.getCarbohydrate() * quantity / 100D);
						food.setVitaminA(fd.getVitaminA() * quantity / 100D);
						food.setVitaminB1(fd.getVitaminB1() * quantity / 100D);
						food.setVitaminB2(fd.getVitaminB2() * quantity / 100D);
						food.setVitaminC(fd.getVitaminC() * quantity / 100D);
						food.setVitaminE(fd.getVitaminE() * quantity / 100D);
						food.setNicotinicCid(fd.getNicotinicCid() * quantity / 100D);
						food.setRetinol(fd.getRetinol() * quantity / 100D);
						food.setCalcium(fd.getCalcium() * quantity / 100D);
						food.setIron(fd.getIron() * quantity / 100D);
						food.setZinc(fd.getZinc() * quantity / 100D);
						food.setIodine(fd.getIodine() * quantity / 100D);
						food.setPhosphorus(fd.getPhosphorus() * quantity / 100D);

						total.setQuantity(total.getQuantity() + quantity);
						total.setHeatEnergy(total.getHeatEnergy() + food.getHeatEnergy());
						total.setProtein(total.getProtein() + food.getProtein());
						total.setFat(total.getFat() + food.getFat());
						total.setCarbohydrate(total.getCarbohydrate() + food.getCarbohydrate());
						total.setVitaminA(total.getVitaminA() + food.getVitaminA());
						total.setVitaminB1(total.getVitaminB1() + food.getVitaminB1());
						total.setVitaminB2(total.getVitaminB2() + food.getVitaminB2());
						total.setVitaminB6(total.getVitaminB6() + food.getVitaminB6());
						total.setVitaminB12(total.getVitaminB12() + food.getVitaminB12());
						total.setVitaminC(total.getVitaminC() + food.getVitaminC());
						total.setVitaminE(total.getVitaminE() + food.getVitaminE());
						total.setNicotinicCid(total.getNicotinicCid() + food.getNicotinicCid());
						total.setRetinol(total.getRetinol() + food.getRetinol());
						total.setCalcium(total.getCalcium() + food.getCalcium());
						total.setIron(total.getIron() + food.getIron());
						total.setZinc(total.getZinc() + food.getZinc());
						total.setIodine(total.getIodine() + food.getIodine());
						total.setPhosphorus(total.getPhosphorus() + food.getPhosphorus());
					}
				}

				FoodComposition avg = new FoodComposition();
				avg.setQuantity(total.getQuantity());
				avg.setHeatEnergy(total.getHeatEnergy());
				avg.setProtein(total.getProtein());
				avg.setFat(total.getFat());
				avg.setCarbohydrate(total.getCarbohydrate());
				avg.setVitaminA(total.getVitaminA());
				avg.setVitaminB1(total.getVitaminB1());
				avg.setVitaminB2(total.getVitaminB2());
				avg.setVitaminB6(total.getVitaminB6());
				avg.setVitaminB12(total.getVitaminB12());
				avg.setVitaminC(total.getVitaminC());
				avg.setVitaminE(total.getVitaminE());
				avg.setNicotinicCid(total.getNicotinicCid());
				avg.setRetinol(total.getRetinol());
				avg.setCalcium(total.getCalcium());
				avg.setIron(total.getIron());
				avg.setZinc(total.getZinc());
				avg.setIodine(total.getIodine());
				avg.setPhosphorus(total.getPhosphorus());

				params.put("t", total);
				params.put("avg", avg);
				params.put("rows0", rows0);
				params.put("rows1", rows1);
				params.put("rows2", rows2);
				params.put("rows3", rows3);
			}
		}
	}

}
