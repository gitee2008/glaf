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

import java.util.*;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.ParamUtils;
import com.glaf.heathcare.domain.ActualRepastPerson;
import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.domain.GoodsActualQuantity;
import com.glaf.heathcare.query.ActualRepastPersonQuery;
import com.glaf.heathcare.query.FoodCompositionQuery;
import com.glaf.heathcare.query.GoodsActualQuantityQuery;
import com.glaf.heathcare.service.ActualRepastPersonService;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.heathcare.service.GoodsActualQuantityService;

public class DietaryNutritionCountPreprocessor implements IReportPreprocessor {

	@Override
	public void prepare(LoginContext loginContext, int year, int month, Map<String, Object> params) {
		Date startDate = ParamUtils.getDate(params, "startDate");
		Date endDate = ParamUtils.getDate(params, "endDate");
		if (startDate != null && endDate != null) {
			String tenantId = loginContext.getTenantId();
			ActualRepastPersonService actualRepastPersonService = ContextFactory
					.getBean("com.glaf.heathcare.service.actualRepastPersonService");
			FoodCompositionService foodCompositionService = ContextFactory
					.getBean("com.glaf.heathcare.service.foodCompositionService");
			GoodsActualQuantityService goodsActualQuantityService = ContextFactory
					.getBean("com.glaf.heathcare.service.goodsActualQuantityService");

			FoodCompositionQuery q = new FoodCompositionQuery();
			List<FoodComposition> foods = foodCompositionService.list(q);
			Map<Long, FoodComposition> foodMap = new HashMap<Long, FoodComposition>();
			if (foods != null && !foods.isEmpty()) {
				for (FoodComposition food : foods) {
					foodMap.put(food.getId(), food);
				}
			}

			ActualRepastPersonQuery q2 = new ActualRepastPersonQuery();
			q2.tenantId(tenantId);
			q2.fullDayGreaterThanOrEqual(DateUtils.getYearMonthDay(startDate));
			q2.fullDayLessThanOrEqual(DateUtils.getYearMonthDay(endDate));

			List<ActualRepastPerson> persons = actualRepastPersonService.list(q2);
			int totalPerson = 0;
			if (persons != null && !persons.isEmpty()) {
				for (ActualRepastPerson p : persons) {
					totalPerson = totalPerson + p.getMale();
					totalPerson = totalPerson + p.getFemale();
				}
			}

			GoodsActualQuantityQuery query = new GoodsActualQuantityQuery();
			query.tenantId(tenantId);
			query.businessStatus(9);
			query.usageTimeGreaterThanOrEqual(startDate);
			query.usageTimeLessThanOrEqual(endDate);
			query.setOrderBy("  E.GOODSNODEID_ asc, E.GOODSID_ asc ");

			List<GoodsActualQuantity> rows = goodsActualQuantityService.list(query);
			if (rows != null && !rows.isEmpty()) {
				Set<Integer> fullDaySet = new HashSet<Integer>();
				Map<Long, FoodComposition> foodMap2 = new HashMap<Long, FoodComposition>();
				List<FoodComposition> rows0 = new ArrayList<FoodComposition>();// 谷类薯类
				List<FoodComposition> rows1 = new ArrayList<FoodComposition>();// 蔬菜菌藻水果
				List<FoodComposition> rows2 = new ArrayList<FoodComposition>();// 畜肉、禽肉、乳类、蛋类及鱼虾蟹贝类
				List<FoodComposition> rows3 = new ArrayList<FoodComposition>();// 其他

				FoodComposition fd = null;
				for (GoodsActualQuantity m : rows) {
					fd = foodMap.get(m.getGoodsId());
					if (fd == null) {
						continue;
					}
					if (!fullDaySet.contains(m.getFullDay())) {
						fullDaySet.add(m.getFullDay());
					}
					switch ((int) m.getGoodsNodeId()) {
					case 4402:// 谷类
					case 4403:// 薯类
						FoodComposition food0 = foodMap2.get(m.getGoodsId());
						if (food0 == null) {
							food0 = new FoodComposition();
							food0.setId(m.getGoodsId());
							food0.setNodeId(m.getGoodsNodeId());
							foodMap2.put(m.getGoodsId(), food0);
							rows0.add(food0);
						}
						food0.setQuantity(food0.getQuantity() + m.getQuantity());
						break;
					case 4405:// 蔬菜
					case 4406:// 菌藻
					case 4407:// 水果
						FoodComposition food1 = foodMap2.get(m.getGoodsId());
						if (food1 == null) {
							food1 = new FoodComposition();
							food1.setId(m.getGoodsId());
							food1.setNodeId(m.getGoodsNodeId());
							foodMap2.put(m.getGoodsId(), food1);
							rows1.add(food1);
						}
						food1.setQuantity(food1.getQuantity() + m.getQuantity());
						break;
					case 4409:
					case 4410:
					case 4411:
					case 4412:
					case 4413:
						FoodComposition food2 = foodMap2.get(m.getGoodsId());
						if (food2 == null) {
							food2 = new FoodComposition();
							food2.setId(m.getGoodsId());
							food2.setNodeId(m.getGoodsNodeId());
							foodMap2.put(m.getGoodsId(), food2);
							rows2.add(food2);
						}
						food2.setQuantity(food2.getQuantity() + m.getQuantity());
						break;
					default:
						FoodComposition food3 = foodMap2.get(m.getGoodsId());
						if (food3 == null) {
							food3 = new FoodComposition();
							food3.setId(m.getGoodsId());
							food3.setNodeId(m.getGoodsNodeId());
							foodMap2.put(m.getGoodsId(), food3);
							rows3.add(food3);
						}
						food3.setQuantity(food3.getQuantity() + m.getQuantity());
						break;
					}
				}

				int days = fullDaySet.size();
				if (totalPerson > 0 && days > 0) {
					FoodComposition total = new FoodComposition();
					for (FoodComposition food : rows0) {
						fd = foodMap.get(food.getId());
						double quantity = food.getQuantity();
						quantity = quantity * 1000;// 统一将千克（kg）转换成克（g）
						quantity = quantity / (totalPerson * days);// 转成每人食用克数
						if (fd != null) {
							quantity = quantity * fd.getRadical() / 100D;// 转成可食部的量
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
						quantity = quantity * 1000;// 统一将千克（kg）转换成克（g）
						quantity = quantity / (totalPerson * days);// 转成每人食用克数
						if (fd != null) {
							quantity = quantity * fd.getRadical() / 100D;// 转成可食部的量
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
						quantity = quantity * 1000;// 统一将千克（kg）转换成克（g）
						quantity = quantity / (totalPerson * days);// 转成每人食用克数
						if (fd != null) {
							quantity = quantity * fd.getRadical() / 100D;// 转成可食部的量
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
						quantity = quantity * 1000;// 统一将千克（kg）转换成克（g）
						quantity = quantity / (totalPerson * days);// 转成每人食用克数
						if (fd != null) {
							quantity = quantity * fd.getRadical() / 100D;// 转成可食部的量
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
					avg.setQuantity(total.getQuantity() / days);
					avg.setHeatEnergy(total.getHeatEnergy() / days);
					avg.setProtein(total.getProtein() / days);
					avg.setFat(total.getFat() / days);
					avg.setCarbohydrate(total.getCarbohydrate() / days);
					avg.setVitaminA(total.getVitaminA() / days);
					avg.setVitaminB1(total.getVitaminB1() / days);
					avg.setVitaminB2(total.getVitaminB2() / days);
					avg.setVitaminB6(total.getVitaminB6() / days);
					avg.setVitaminB12(total.getVitaminB12() / days);
					avg.setVitaminC(total.getVitaminC() / days);
					avg.setVitaminE(total.getVitaminE() / days);
					avg.setNicotinicCid(total.getNicotinicCid() / days);
					avg.setRetinol(total.getRetinol() / days);
					avg.setCalcium(total.getCalcium() / days);
					avg.setIron(total.getIron() / days);
					avg.setZinc(total.getZinc() / days);
					avg.setIodine(total.getIodine() / days);
					avg.setPhosphorus(total.getPhosphorus() / days);

					params.put("startDate", DateUtils.getDateTime("yyyy年MM月dd日", startDate));
					params.put("endDate", DateUtils.getDateTime("yyyy年MM月dd日", endDate));
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

}
