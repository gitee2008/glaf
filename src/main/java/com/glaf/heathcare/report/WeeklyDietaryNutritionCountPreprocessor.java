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

import com.glaf.base.modules.sys.model.TenantConfig;
import com.glaf.base.modules.sys.service.TenantConfigService;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.ParamUtils;
import com.glaf.heathcare.domain.DietaryItem;
import com.glaf.heathcare.domain.Dietary;
import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.domain.FoodDRI;
import com.glaf.heathcare.domain.FoodDRIPercent;
import com.glaf.heathcare.query.DietaryItemQuery;
import com.glaf.heathcare.query.DietaryQuery;
import com.glaf.heathcare.query.FoodCompositionQuery;
import com.glaf.heathcare.service.DietaryItemService;
import com.glaf.heathcare.service.DietaryService;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.heathcare.service.FoodDRIPercentService;
import com.glaf.heathcare.service.FoodDRIService;

public class WeeklyDietaryNutritionCountPreprocessor implements IReportPreprocessor {

	@Override
	public void prepare(LoginContext loginContext, int year, int month, Map<String, Object> params) {
		int week = ParamUtils.getInt(params, "week");
		if (year > 0 && week > 0) {
			DietaryService dietaryService = ContextFactory.getBean("com.glaf.heathcare.service.dietaryService");
			DietaryItemService dietaryItemService = ContextFactory
					.getBean("com.glaf.heathcare.service.dietaryItemService");
			FoodCompositionService foodCompositionService = ContextFactory
					.getBean("com.glaf.heathcare.service.foodCompositionService");
			FoodDRIService foodDRIService = ContextFactory.getBean("com.glaf.heathcare.service.foodDRIService");
			FoodDRIPercentService foodDRIPercentService = ContextFactory
					.getBean("com.glaf.heathcare.service.foodDRIPercentService");
			TenantConfigService tenantConfigService = ContextFactory.getBean("tenantConfigService");
			TenantConfig tenantConfig = tenantConfigService.getTenantConfigByTenantId(loginContext.getTenantId());
			long typeId = tenantConfig.getTypeId();
			FoodDRI foodDRI = foodDRIService.getFoodDRIByAge(4);
			FoodDRIPercent foodDRIPercent = foodDRIPercentService.getFoodDRIPercent("3-6", typeId);

			FoodCompositionQuery q = new FoodCompositionQuery();
			List<FoodComposition> foods = foodCompositionService.list(q);
			Map<Long, FoodComposition> foodMap = new HashMap<Long, FoodComposition>();
			if (foods != null && !foods.isEmpty()) {
				for (FoodComposition food : foods) {
					foodMap.put(food.getId(), food);
				}
			}

			DietaryQuery qx = new DietaryQuery();
			qx.tenantId(loginContext.getTenantId());
			qx.year(year);
			qx.week(week);

			List<Dietary> dietarys = dietaryService.list(qx);
			List<Long> dietaryIds = new ArrayList<Long>();
			for (Dietary dietary : dietarys) {
				dietaryIds.add(dietary.getId());
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

				FoodComposition subtotal0 = new FoodComposition();
				FoodComposition subtotal1 = new FoodComposition();
				FoodComposition subtotal2 = new FoodComposition();
				FoodComposition subtotal3 = new FoodComposition();
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

						subtotal0.setQuantity(subtotal0.getQuantity() + quantity);
						subtotal0.setHeatEnergy(subtotal0.getHeatEnergy() + food.getHeatEnergy());
						subtotal0.setProtein(subtotal0.getProtein() + food.getProtein());
						subtotal0.setFat(subtotal0.getFat() + food.getFat());
						subtotal0.setCarbohydrate(subtotal0.getCarbohydrate() + food.getCarbohydrate());
						subtotal0.setVitaminA(subtotal0.getVitaminA() + food.getVitaminA());
						subtotal0.setVitaminB1(subtotal0.getVitaminB1() + food.getVitaminB1());
						subtotal0.setVitaminB2(subtotal0.getVitaminB2() + food.getVitaminB2());
						subtotal0.setVitaminB6(subtotal0.getVitaminB6() + food.getVitaminB6());
						subtotal0.setVitaminB12(subtotal0.getVitaminB12() + food.getVitaminB12());
						subtotal0.setVitaminC(subtotal0.getVitaminC() + food.getVitaminC());
						subtotal0.setVitaminE(subtotal0.getVitaminE() + food.getVitaminE());
						subtotal0.setNicotinicCid(subtotal0.getNicotinicCid() + food.getNicotinicCid());
						subtotal0.setRetinol(subtotal0.getRetinol() + food.getRetinol());
						subtotal0.setCalcium(subtotal0.getCalcium() + food.getCalcium());
						subtotal0.setIron(subtotal0.getIron() + food.getIron());
						subtotal0.setZinc(subtotal0.getZinc() + food.getZinc());
						subtotal0.setIodine(subtotal0.getIodine() + food.getIodine());
						subtotal0.setPhosphorus(subtotal0.getPhosphorus() + food.getPhosphorus());
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

						subtotal1.setQuantity(subtotal1.getQuantity() + quantity);
						subtotal1.setHeatEnergy(subtotal1.getHeatEnergy() + food.getHeatEnergy());
						subtotal1.setProtein(subtotal1.getProtein() + food.getProtein());
						subtotal1.setFat(subtotal1.getFat() + food.getFat());
						subtotal1.setCarbohydrate(subtotal1.getCarbohydrate() + food.getCarbohydrate());
						subtotal1.setVitaminA(subtotal1.getVitaminA() + food.getVitaminA());
						subtotal1.setVitaminB1(subtotal1.getVitaminB1() + food.getVitaminB1());
						subtotal1.setVitaminB2(subtotal1.getVitaminB2() + food.getVitaminB2());
						subtotal1.setVitaminB6(subtotal1.getVitaminB6() + food.getVitaminB6());
						subtotal1.setVitaminB12(subtotal1.getVitaminB12() + food.getVitaminB12());
						subtotal1.setVitaminC(subtotal1.getVitaminC() + food.getVitaminC());
						subtotal1.setVitaminE(subtotal1.getVitaminE() + food.getVitaminE());
						subtotal1.setNicotinicCid(subtotal1.getNicotinicCid() + food.getNicotinicCid());
						subtotal1.setRetinol(subtotal1.getRetinol() + food.getRetinol());
						subtotal1.setCalcium(subtotal1.getCalcium() + food.getCalcium());
						subtotal1.setIron(subtotal1.getIron() + food.getIron());
						subtotal1.setZinc(subtotal1.getZinc() + food.getZinc());
						subtotal1.setIodine(subtotal1.getIodine() + food.getIodine());
						subtotal1.setPhosphorus(subtotal1.getPhosphorus() + food.getPhosphorus());
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

						subtotal2.setQuantity(subtotal2.getQuantity() + quantity);
						subtotal2.setHeatEnergy(subtotal2.getHeatEnergy() + food.getHeatEnergy());
						subtotal2.setProtein(subtotal2.getProtein() + food.getProtein());
						subtotal2.setFat(subtotal2.getFat() + food.getFat());
						subtotal2.setCarbohydrate(subtotal2.getCarbohydrate() + food.getCarbohydrate());
						subtotal2.setVitaminA(subtotal2.getVitaminA() + food.getVitaminA());
						subtotal2.setVitaminB1(subtotal2.getVitaminB1() + food.getVitaminB1());
						subtotal2.setVitaminB2(subtotal2.getVitaminB2() + food.getVitaminB2());
						subtotal2.setVitaminB6(subtotal2.getVitaminB6() + food.getVitaminB6());
						subtotal2.setVitaminB12(subtotal2.getVitaminB12() + food.getVitaminB12());
						subtotal2.setVitaminC(subtotal2.getVitaminC() + food.getVitaminC());
						subtotal2.setVitaminE(subtotal2.getVitaminE() + food.getVitaminE());
						subtotal2.setNicotinicCid(subtotal2.getNicotinicCid() + food.getNicotinicCid());
						subtotal2.setRetinol(subtotal2.getRetinol() + food.getRetinol());
						subtotal2.setCalcium(subtotal2.getCalcium() + food.getCalcium());
						subtotal2.setIron(subtotal2.getIron() + food.getIron());
						subtotal2.setZinc(subtotal2.getZinc() + food.getZinc());
						subtotal2.setIodine(subtotal2.getIodine() + food.getIodine());
						subtotal2.setPhosphorus(subtotal2.getPhosphorus() + food.getPhosphorus());
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

						subtotal3.setQuantity(subtotal3.getQuantity() + quantity);
						subtotal3.setHeatEnergy(subtotal3.getHeatEnergy() + food.getHeatEnergy());
						subtotal3.setProtein(subtotal3.getProtein() + food.getProtein());
						subtotal3.setFat(subtotal3.getFat() + food.getFat());
						subtotal3.setCarbohydrate(subtotal3.getCarbohydrate() + food.getCarbohydrate());
						subtotal3.setVitaminA(subtotal3.getVitaminA() + food.getVitaminA());
						subtotal3.setVitaminB1(subtotal3.getVitaminB1() + food.getVitaminB1());
						subtotal3.setVitaminB2(subtotal3.getVitaminB2() + food.getVitaminB2());
						subtotal3.setVitaminB6(subtotal3.getVitaminB6() + food.getVitaminB6());
						subtotal3.setVitaminB12(subtotal3.getVitaminB12() + food.getVitaminB12());
						subtotal3.setVitaminC(subtotal3.getVitaminC() + food.getVitaminC());
						subtotal3.setVitaminE(subtotal3.getVitaminE() + food.getVitaminE());
						subtotal3.setNicotinicCid(subtotal3.getNicotinicCid() + food.getNicotinicCid());
						subtotal3.setRetinol(subtotal3.getRetinol() + food.getRetinol());
						subtotal3.setCalcium(subtotal3.getCalcium() + food.getCalcium());
						subtotal3.setIron(subtotal3.getIron() + food.getIron());
						subtotal3.setZinc(subtotal3.getZinc() + food.getZinc());
						subtotal3.setIodine(subtotal3.getIodine() + food.getIodine());
						subtotal3.setPhosphorus(subtotal3.getPhosphorus() + food.getPhosphorus());

					}
				}

				FoodComposition avg0 = new FoodComposition();
				avg0.setQuantity(subtotal0.getQuantity() / 5);
				avg0.setHeatEnergy(subtotal0.getHeatEnergy() / 5);
				avg0.setProtein(subtotal0.getProtein() / 5);
				avg0.setFat(subtotal0.getFat() / 5);
				avg0.setCarbohydrate(subtotal0.getCarbohydrate() / 5);
				avg0.setVitaminA(subtotal0.getVitaminA() / 5);
				avg0.setVitaminB1(subtotal0.getVitaminB1() / 5);
				avg0.setVitaminB2(subtotal0.getVitaminB2() / 5);
				avg0.setVitaminB6(subtotal0.getVitaminB6() / 5);
				avg0.setVitaminB12(subtotal0.getVitaminB12() / 5);
				avg0.setVitaminC(subtotal0.getVitaminC() / 5);
				avg0.setVitaminE(subtotal0.getVitaminE() / 5);
				avg0.setNicotinicCid(subtotal0.getNicotinicCid() / 5);
				avg0.setRetinol(subtotal0.getRetinol() / 5);
				avg0.setCalcium(subtotal0.getCalcium() / 5);
				avg0.setIron(subtotal0.getIron() / 5);
				avg0.setZinc(subtotal0.getZinc() / 5);
				avg0.setIodine(subtotal0.getIodine() / 5);
				avg0.setPhosphorus(subtotal0.getPhosphorus() / 5);

				FoodComposition avg1 = new FoodComposition();
				avg1.setQuantity(subtotal1.getQuantity() / 5);
				avg1.setHeatEnergy(subtotal1.getHeatEnergy() / 5);
				avg1.setProtein(subtotal1.getProtein() / 5);
				avg1.setFat(subtotal1.getFat() / 5);
				avg1.setCarbohydrate(subtotal1.getCarbohydrate() / 5);
				avg1.setVitaminA(subtotal1.getVitaminA() / 5);
				avg1.setVitaminB1(subtotal1.getVitaminB1() / 5);
				avg1.setVitaminB2(subtotal1.getVitaminB2() / 5);
				avg1.setVitaminB6(subtotal1.getVitaminB6() / 5);
				avg1.setVitaminB12(subtotal1.getVitaminB12() / 5);
				avg1.setVitaminC(subtotal1.getVitaminC() / 5);
				avg1.setVitaminE(subtotal1.getVitaminE() / 5);
				avg1.setNicotinicCid(subtotal1.getNicotinicCid() / 5);
				avg1.setRetinol(subtotal1.getRetinol() / 5);
				avg1.setCalcium(subtotal1.getCalcium() / 5);
				avg1.setIron(subtotal1.getIron() / 5);
				avg1.setZinc(subtotal1.getZinc() / 5);
				avg1.setIodine(subtotal1.getIodine() / 5);
				avg1.setPhosphorus(subtotal1.getPhosphorus() / 5);

				FoodComposition avg2 = new FoodComposition();
				avg2.setQuantity(subtotal2.getQuantity() / 5);
				avg2.setHeatEnergy(subtotal2.getHeatEnergy() / 5);
				avg2.setProtein(subtotal2.getProtein() / 5);
				avg2.setFat(subtotal2.getFat() / 5);
				avg2.setCarbohydrate(subtotal2.getCarbohydrate() / 5);
				avg2.setVitaminA(subtotal2.getVitaminA() / 5);
				avg2.setVitaminB1(subtotal2.getVitaminB1() / 5);
				avg2.setVitaminB2(subtotal2.getVitaminB2() / 5);
				avg2.setVitaminB6(subtotal2.getVitaminB6() / 5);
				avg2.setVitaminB12(subtotal2.getVitaminB12() / 5);
				avg2.setVitaminC(subtotal2.getVitaminC() / 5);
				avg2.setVitaminE(subtotal2.getVitaminE() / 5);
				avg2.setNicotinicCid(subtotal2.getNicotinicCid() / 5);
				avg2.setRetinol(subtotal2.getRetinol() / 5);
				avg2.setCalcium(subtotal2.getCalcium() / 5);
				avg2.setIron(subtotal2.getIron() / 5);
				avg2.setZinc(subtotal2.getZinc() / 5);
				avg2.setIodine(subtotal2.getIodine() / 5);
				avg2.setPhosphorus(subtotal2.getPhosphorus() / 5);

				FoodComposition avg3 = new FoodComposition();
				avg3.setQuantity(subtotal3.getQuantity() / 5);
				avg3.setHeatEnergy(subtotal3.getHeatEnergy() / 5);
				avg3.setProtein(subtotal3.getProtein() / 5);
				avg3.setFat(subtotal3.getFat() / 5);
				avg3.setCarbohydrate(subtotal3.getCarbohydrate() / 5);
				avg3.setVitaminA(subtotal3.getVitaminA() / 5);
				avg3.setVitaminB1(subtotal3.getVitaminB1() / 5);
				avg3.setVitaminB2(subtotal3.getVitaminB2() / 5);
				avg3.setVitaminB6(subtotal3.getVitaminB6() / 5);
				avg3.setVitaminB12(subtotal3.getVitaminB12() / 5);
				avg3.setVitaminC(subtotal3.getVitaminC() / 5);
				avg3.setVitaminE(subtotal3.getVitaminE() / 5);
				avg3.setNicotinicCid(subtotal3.getNicotinicCid() / 5);
				avg3.setRetinol(subtotal3.getRetinol() / 5);
				avg3.setCalcium(subtotal3.getCalcium() / 5);
				avg3.setIron(subtotal3.getIron() / 5);
				avg3.setZinc(subtotal3.getZinc() / 5);
				avg3.setIodine(subtotal3.getIodine() / 5);
				avg3.setPhosphorus(subtotal3.getPhosphorus() / 5);

				FoodComposition avg = new FoodComposition();
				avg.setQuantity(total.getQuantity() / 5);
				avg.setHeatEnergy(total.getHeatEnergy() / 5);
				avg.setProtein(total.getProtein() / 5);
				avg.setFat(total.getFat() / 5);
				avg.setCarbohydrate(total.getCarbohydrate() / 5);
				avg.setVitaminA(total.getVitaminA() / 5);
				avg.setVitaminB1(total.getVitaminB1() / 5);
				avg.setVitaminB2(total.getVitaminB2() / 5);
				avg.setVitaminB6(total.getVitaminB6() / 5);
				avg.setVitaminB12(total.getVitaminB12() / 5);
				avg.setVitaminC(total.getVitaminC() / 5);
				avg.setVitaminE(total.getVitaminE() / 5);
				avg.setNicotinicCid(total.getNicotinicCid() / 5);
				avg.setRetinol(total.getRetinol() / 5);
				avg.setCalcium(total.getCalcium() / 5);
				avg.setIron(total.getIron() / 5);
				avg.setZinc(total.getZinc() / 5);
				avg.setIodine(total.getIodine() / 5);
				avg.setPhosphorus(total.getPhosphorus() / 5);

				if (foodDRI != null) {
					if (foodDRIPercent != null) {
						foodDRI.setHeatEnergy(foodDRIPercent.getHeatEnergy() * foodDRI.getHeatEnergy());
						foodDRI.setProtein(foodDRIPercent.getProtein() * foodDRI.getProtein());
						foodDRI.setFat(foodDRIPercent.getFat() * foodDRI.getFat());
						foodDRI.setCarbohydrate(foodDRIPercent.getCarbohydrate() * foodDRI.getCarbohydrate());
						foodDRI.setVitaminA(foodDRIPercent.getVitaminA() * foodDRI.getVitaminA());
						foodDRI.setVitaminB1(foodDRIPercent.getVitaminB1() * foodDRI.getVitaminB1());
						foodDRI.setVitaminB2(foodDRIPercent.getVitaminB2() * foodDRI.getVitaminB2());
						foodDRI.setVitaminB6(foodDRIPercent.getVitaminB6() * foodDRI.getVitaminB6());
						foodDRI.setVitaminB12(foodDRIPercent.getVitaminB12() * foodDRI.getVitaminB12());
						foodDRI.setVitaminC(foodDRIPercent.getVitaminC() * foodDRI.getVitaminC());
						foodDRI.setVitaminE(foodDRIPercent.getVitaminE() * foodDRI.getVitaminE());
						foodDRI.setNicotinicCid(foodDRIPercent.getNicotinicCid() * foodDRI.getNicotinicCid());
						foodDRI.setRetinol(foodDRIPercent.getRetinol() * foodDRI.getRetinol());
						foodDRI.setCalcium(foodDRIPercent.getCalcium() * foodDRI.getCalcium());
						foodDRI.setIron(foodDRIPercent.getIron() * foodDRI.getIron());
						foodDRI.setZinc(foodDRIPercent.getZinc() * foodDRI.getZinc());
						foodDRI.setIodine(foodDRIPercent.getIodine() * foodDRI.getIodine());
						foodDRI.setPhosphorus(foodDRIPercent.getPhosphorus() * foodDRI.getPhosphorus());
					}
					FoodComposition percent = new FoodComposition();
					if (foodDRI.getHeatEnergy() > 0) {
						percent.setHeatEnergy(
								Math.round((avg.getHeatEnergy() / foodDRI.getHeatEnergy()) * 10000D) / 100D);
					}
					if (foodDRI.getProtein() > 0) {
						percent.setProtein(Math.round((avg.getProtein() / foodDRI.getProtein()) * 10000D) / 100D);
					}
					if (foodDRI.getFat() > 0) {
						percent.setFat(Math.round((avg.getFat() / foodDRI.getFat()) * 10000D) / 100D);
					}
					if (foodDRI.getCarbohydrate() > 0) {
						percent.setCarbohydrate(
								Math.round((avg.getCarbohydrate() / foodDRI.getCarbohydrate()) * 10000D) / 100D);
					}
					if (foodDRI.getVitaminA() > 0) {
						percent.setVitaminA(Math.round((avg.getVitaminA() / foodDRI.getVitaminA()) * 10000D) / 100D);
					}
					if (foodDRI.getVitaminB1() > 0) {
						percent.setVitaminB1(Math.round((avg.getVitaminB1() / foodDRI.getVitaminB1()) * 10000D) / 100D);
					}
					if (foodDRI.getVitaminB2() > 0) {
						percent.setVitaminB2(Math.round((avg.getVitaminB2() / foodDRI.getVitaminB2()) * 10000D) / 100D);
					}
					if (foodDRI.getVitaminB6() > 0) {
						percent.setVitaminB6(Math.round((avg.getVitaminB6() / foodDRI.getVitaminB6()) * 10000D) / 100D);
					}
					if (foodDRI.getVitaminB12() > 0) {
						percent.setVitaminB12(
								Math.round((avg.getVitaminB12() / foodDRI.getVitaminB12()) * 10000D) / 100D);
					}
					if (foodDRI.getVitaminC() > 0) {
						percent.setVitaminC(Math.round((avg.getVitaminC() / foodDRI.getVitaminC()) * 10000D) / 100D);
					}
					if (foodDRI.getVitaminE() > 0) {
						percent.setVitaminE(Math.round((avg.getVitaminE() / foodDRI.getVitaminE()) * 10000D) / 100D);
					}
					if (foodDRI.getNicotinicCid() > 0) {
						percent.setNicotinicCid(
								Math.round((avg.getNicotinicCid() / foodDRI.getNicotinicCid()) * 10000D) / 100D);
					}
					if (foodDRI.getRetinol() > 0) {
						percent.setRetinol(Math.round((avg.getRetinol() / foodDRI.getRetinol()) * 10000D) / 100D);
					}
					if (foodDRI.getCalcium() > 0) {
						percent.setCalcium(Math.round((avg.getCalcium() / foodDRI.getCalcium()) * 10000D) / 100D);
					}
					if (foodDRI.getIron() > 0) {
						percent.setIron(Math.round((avg.getIron() / foodDRI.getIron()) * 10000D) / 100D);
					}
					if (foodDRI.getZinc() > 0) {
						percent.setZinc(Math.round((avg.getZinc() / foodDRI.getZinc()) * 10000D) / 100D);
					}
					if (foodDRI.getIodine() > 0) {
						percent.setIodine(Math.round((avg.getIodine() / foodDRI.getIodine()) * 10000D) / 100D);
					}
					if (foodDRI.getPhosphorus() > 0) {
						percent.setPhosphorus(
								Math.round((avg.getPhosphorus() / foodDRI.getPhosphorus()) * 10000D) / 100D);
					}

					params.put("std", foodDRI);
					params.put("percent", percent);
				}

				params.put("rows0", rows0);
				params.put("rows1", rows1);
				params.put("rows2", rows2);
				params.put("rows3", rows3);

				params.put("t0", subtotal0);
				params.put("t1", subtotal1);
				params.put("t2", subtotal2);
				params.put("t3", subtotal3);

				params.put("avg0", avg0);
				params.put("avg1", avg1);
				params.put("avg2", avg2);
				params.put("avg3", avg3);

				params.put("t", total);
				params.put("avg", avg);

			}
		}
	}

}
