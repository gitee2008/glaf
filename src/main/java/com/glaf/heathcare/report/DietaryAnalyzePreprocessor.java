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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.base.modules.sys.model.TenantConfig;
import com.glaf.base.modules.sys.service.TenantConfigService;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.ParamUtils;
import com.glaf.heathcare.domain.ActualRepastPerson;
import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.domain.FoodDRI;
import com.glaf.heathcare.domain.FoodDRIPercent;
import com.glaf.heathcare.domain.GoodsActualQuantity;
import com.glaf.heathcare.query.ActualRepastPersonQuery;
import com.glaf.heathcare.query.FoodCompositionQuery;
import com.glaf.heathcare.query.GoodsActualQuantityQuery;
import com.glaf.heathcare.service.ActualRepastPersonService;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.heathcare.service.FoodDRIPercentService;
import com.glaf.heathcare.service.FoodDRIService;
import com.glaf.heathcare.service.GoodsActualQuantityService;

public class DietaryAnalyzePreprocessor implements IReportPreprocessor {

	protected static final Log logger = LogFactory.getLog(DietaryAnalyzePreprocessor.class);

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
			FoodDRIService foodDRIService = ContextFactory.getBean("com.glaf.heathcare.service.foodDRIService");
			FoodDRIPercentService foodDRIPercentService = ContextFactory
					.getBean("com.glaf.heathcare.service.foodDRIPercentService");
			TenantConfigService tenantConfigService = ContextFactory.getBean("tenantConfigService");
			TenantConfig tenantConfig = tenantConfigService.getTenantConfigByTenantId(loginContext.getTenantId());
			long typeId = tenantConfig.getTypeId();

			FoodDRI foodDRI = foodDRIService.getFoodDRIByAge(4);
			FoodDRIPercent foodDRIPercent = foodDRIPercentService.getFoodDRIPercent("3-6", typeId);
			logger.debug("foodDRIPercent:" + foodDRIPercent);
			if (foodDRI != null && foodDRIPercent != null) {
				foodDRI.setCalcium(foodDRI.getCalcium() * foodDRIPercent.getCalcium());
				foodDRI.setCarbohydrate(foodDRI.getCarbohydrate() * foodDRIPercent.getCarbohydrate());
				foodDRI.setFat(foodDRI.getFat() * foodDRIPercent.getFat());
				foodDRI.setHeatEnergy(foodDRI.getHeatEnergy() * foodDRIPercent.getHeatEnergy());
				foodDRI.setIodine(foodDRI.getIodine() * foodDRIPercent.getIodine());
				foodDRI.setIron(foodDRI.getIron() * foodDRIPercent.getIron());
				foodDRI.setNicotinicCid(foodDRI.getNicotinicCid() * foodDRIPercent.getNicotinicCid());
				foodDRI.setPhosphorus(foodDRI.getPhosphorus() * foodDRIPercent.getPhosphorus());
				foodDRI.setProtein(foodDRI.getProtein() * foodDRIPercent.getProtein());
				foodDRI.setRetinol(foodDRI.getRetinol() * foodDRIPercent.getRetinol());
				foodDRI.setVitaminA(foodDRI.getVitaminA() * foodDRIPercent.getVitaminA());
				foodDRI.setVitaminB1(foodDRI.getVitaminB1() * foodDRIPercent.getVitaminB1());
				foodDRI.setVitaminB12(foodDRI.getVitaminB12() * foodDRIPercent.getVitaminB12());
				foodDRI.setVitaminB2(foodDRI.getVitaminB2() * foodDRIPercent.getVitaminB2());
				foodDRI.setVitaminB6(foodDRI.getVitaminB6() * foodDRIPercent.getVitaminB6());
				foodDRI.setVitaminC(foodDRI.getVitaminC() * foodDRIPercent.getVitaminC());
				foodDRI.setVitaminE(foodDRI.getVitaminE() * foodDRIPercent.getVitaminE());
				foodDRI.setZinc(foodDRI.getZinc() * foodDRIPercent.getZinc());
				params.put("foodDRI", foodDRI);
				params.put("foodDRIPercent", foodDRIPercent);
			}

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

			Set<Integer> fullDaySet = new HashSet<Integer>();

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
				Map<Long, FoodComposition> foodMap2 = new HashMap<Long, FoodComposition>();
				List<FoodComposition> rows1 = new ArrayList<FoodComposition>();// 细粮
				List<FoodComposition> rows2 = new ArrayList<FoodComposition>();// 杂粮
				List<FoodComposition> rows3 = new ArrayList<FoodComposition>();// 糕点
				List<FoodComposition> rows4 = new ArrayList<FoodComposition>();// 干豆
				List<FoodComposition> rows6 = new ArrayList<FoodComposition>();// 蔬菜总量
				List<FoodComposition> rows7 = new ArrayList<FoodComposition>();// 绿橙蔬菜
				List<FoodComposition> rows8 = new ArrayList<FoodComposition>();// 水果
				List<FoodComposition> rows9 = new ArrayList<FoodComposition>();// 乳类
				List<FoodComposition> rows10 = new ArrayList<FoodComposition>();// 蛋类
				List<FoodComposition> rows11 = new ArrayList<FoodComposition>();// 肉类
				List<FoodComposition> rows13 = new ArrayList<FoodComposition>();// 鱼类
				List<FoodComposition> rows14 = new ArrayList<FoodComposition>();// 油脂类
				List<FoodComposition> rows15 = new ArrayList<FoodComposition>();// 糖

				double beansProtein = 0.0;
				double animalsProtein = 0.0;
				double totalHeatEnergy = 0.0;

				for (GoodsActualQuantity m : rows) {
					FoodComposition food = foodMap.get(m.getGoodsId());
					if (food == null) {
						continue;
					}
					if (!fullDaySet.contains(m.getFullDay())) {
						fullDaySet.add(m.getFullDay());
					}
					totalHeatEnergy = totalHeatEnergy + food.getHeatEnergy() * m.getQuantity() * 1000 / 100D;// 转成100克的热能
					switch ((int) food.getNodeId()) {
					case 4402:// 谷类及制品
						if (StringUtils.equals(food.getCerealFlag(), "O")) {// 细粮
							FoodComposition fd = foodMap2.get(m.getGoodsId());
							if (fd == null) {
								fd = new FoodComposition();
								fd.setId(m.getGoodsId());
								fd.setNodeId(m.getGoodsNodeId());
								foodMap2.put(m.getGoodsId(), fd);
								rows1.add(fd);
							}
							fd.setQuantity(fd.getQuantity() + m.getQuantity());
						} else if (StringUtils.equals(food.getCerealFlag(), "X")) {// 杂粮
							FoodComposition fd = foodMap2.get(m.getGoodsId());
							if (fd == null) {
								fd = new FoodComposition();
								fd.setId(m.getGoodsId());
								fd.setNodeId(m.getGoodsNodeId());
								foodMap2.put(m.getGoodsId(), fd);
								rows2.add(fd);
							}
							fd.setQuantity(fd.getQuantity() + m.getQuantity());
						} else if (StringUtils.equals(food.getCerealFlag(), "C")) {// 糕点
							FoodComposition fd = foodMap2.get(m.getGoodsId());
							if (fd == null) {
								fd = new FoodComposition();
								fd.setId(m.getGoodsId());
								fd.setNodeId(m.getGoodsNodeId());
								foodMap2.put(m.getGoodsId(), fd);
								rows3.add(fd);
							}
							fd.setQuantity(fd.getQuantity() + m.getQuantity());
						}
						break;

					case 4404:// 豆类及制品
						FoodComposition fd4 = foodMap2.get(m.getGoodsId());
						if (fd4 == null) {
							fd4 = new FoodComposition();
							fd4.setId(m.getGoodsId());
							fd4.setNodeId(m.getGoodsNodeId());
							foodMap2.put(m.getGoodsId(), fd4);
							rows4.add(fd4);
						}
						fd4.setQuantity(fd4.getQuantity() + m.getQuantity());
						break;

					case 4405:// 蔬菜
						FoodComposition fd6 = foodMap2.get(m.getGoodsId());
						if (fd6 == null) {
							fd6 = new FoodComposition();
							fd6.setId(m.getGoodsId());
							fd6.setNodeId(m.getGoodsNodeId());
							foodMap2.put(m.getGoodsId(), fd6);
							rows6.add(fd6);
						}
						fd6.setQuantity(fd6.getQuantity() + m.getQuantity());
						if (StringUtils.equals(food.getColorFlag(), "Y")) {
							FoodComposition fd7 = foodMap2.get(m.getGoodsId());
							if (fd7 == null) {
								fd7 = new FoodComposition();
								fd7.setId(m.getGoodsId());
								fd7.setNodeId(m.getGoodsNodeId());
								foodMap2.put(m.getGoodsId(), fd7);
								rows7.add(fd7);
							}
							fd7.setQuantity(fd7.getQuantity() + m.getQuantity());
						}

					case 4407:// 水果
						FoodComposition fd8 = foodMap2.get(m.getGoodsId());
						if (fd8 == null) {
							fd8 = new FoodComposition();
							fd8.setId(m.getGoodsId());
							fd8.setNodeId(m.getGoodsNodeId());
							foodMap2.put(m.getGoodsId(), fd8);
							rows8.add(fd8);
						}
						fd8.setQuantity(fd8.getQuantity() + m.getQuantity());
						break;

					case 4411:// 乳类
						FoodComposition fd9 = foodMap2.get(m.getGoodsId());
						if (fd9 == null) {
							fd9 = new FoodComposition();
							fd9.setId(m.getGoodsId());
							fd9.setNodeId(m.getGoodsNodeId());
							foodMap2.put(m.getGoodsId(), fd9);
							rows9.add(fd9);
						}
						fd9.setQuantity(fd9.getQuantity() + m.getQuantity());
						break;

					case 4412:// 蛋类
						FoodComposition fd10 = foodMap2.get(m.getGoodsId());
						if (fd10 == null) {
							fd10 = new FoodComposition();
							fd10.setId(m.getGoodsId());
							fd10.setNodeId(m.getGoodsNodeId());
							foodMap2.put(m.getGoodsId(), fd10);
							rows10.add(fd10);
						}
						fd10.setQuantity(fd10.getQuantity() + m.getQuantity());
						break;

					case 4409:// 畜肉类及制品
					case 4410:// 禽肉类及制品
						FoodComposition fd11 = foodMap2.get(m.getGoodsId());
						if (fd11 == null) {
							fd11 = new FoodComposition();
							fd11.setId(m.getGoodsId());
							fd11.setNodeId(m.getGoodsNodeId());
							foodMap2.put(m.getGoodsId(), fd11);
							rows11.add(fd11);
						}
						fd11.setQuantity(fd11.getQuantity() + m.getQuantity());
						break;

					case 4413:// 鱼虾蟹贝类及制品
						FoodComposition fd13 = foodMap2.get(m.getGoodsId());
						if (fd13 == null) {
							fd13 = new FoodComposition();
							fd13.setId(m.getGoodsId());
							fd13.setNodeId(m.getGoodsNodeId());
							foodMap2.put(m.getGoodsId(), fd13);
							rows13.add(fd13);
						}
						fd13.setQuantity(fd13.getQuantity() + m.getQuantity());
						break;

					case 4418:// 油脂类
						FoodComposition fd14 = foodMap2.get(m.getGoodsId());
						if (fd14 == null) {
							fd14 = new FoodComposition();
							fd14.setId(m.getGoodsId());
							fd14.setNodeId(m.getGoodsNodeId());
							foodMap2.put(m.getGoodsId(), fd14);
							rows14.add(fd14);
						}
						fd14.setQuantity(fd14.getQuantity() + m.getQuantity());
						break;

					case 4417:// 糖
						FoodComposition fd15 = foodMap2.get(m.getGoodsId());
						if (fd15 == null) {
							fd15 = new FoodComposition();
							fd15.setId(m.getGoodsId());
							fd15.setNodeId(m.getGoodsNodeId());
							foodMap2.put(m.getGoodsId(), fd15);
							rows15.add(fd15);
						}
						fd15.setQuantity(fd15.getQuantity() + m.getQuantity());
						break;

					default:
						break;
					}
				}

				int days = fullDaySet.size();

				if (totalPerson > 0 && days > 0) {
					FoodComposition total = new FoodComposition();
					total.setQuantity(0);

					for (FoodComposition food : rows1) {
						FoodComposition fd = foodMap.get(food.getId());
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

					params.put("item1", total.getQuantity());
					total.setQuantity(0);

					for (FoodComposition food : rows2) {
						FoodComposition fd = foodMap.get(food.getId());
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

					params.put("item2", total.getQuantity());
					total.setQuantity(0);

					for (FoodComposition food : rows3) {
						FoodComposition fd = foodMap.get(food.getId());
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

					params.put("item3", total.getQuantity());
					total.setQuantity(0);

					for (FoodComposition food : rows4) {
						FoodComposition fd = foodMap.get(food.getId());
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

							beansProtein = beansProtein + food.getProtein();
						}
					}

					params.put("item4", total.getQuantity());
					total.setQuantity(0);

					for (FoodComposition food : rows6) {
						FoodComposition fd = foodMap.get(food.getId());
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

					params.put("item6", total.getQuantity());
					total.setQuantity(0);

					for (FoodComposition food : rows7) {
						FoodComposition fd = foodMap.get(food.getId());
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

					params.put("item7", total.getQuantity());
					total.setQuantity(0);

					for (FoodComposition food : rows8) {
						FoodComposition fd = foodMap.get(food.getId());
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

					params.put("item8", total.getQuantity());
					total.setQuantity(0);

					for (FoodComposition food : rows9) {
						FoodComposition fd = foodMap.get(food.getId());
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

							animalsProtein = animalsProtein + food.getProtein();
						}
					}

					params.put("item9", total.getQuantity());
					total.setQuantity(0);

					for (FoodComposition food : rows10) {
						FoodComposition fd = foodMap.get(food.getId());
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

							animalsProtein = animalsProtein + food.getProtein();
						}
					}

					params.put("item10", total.getQuantity());
					total.setQuantity(0);

					for (FoodComposition food : rows11) {
						FoodComposition fd = foodMap.get(food.getId());
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

							animalsProtein = animalsProtein + food.getProtein();
						}
					}

					params.put("item11", total.getQuantity());
					total.setQuantity(0);

					for (FoodComposition food : rows13) {
						FoodComposition fd = foodMap.get(food.getId());
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

							animalsProtein = animalsProtein + food.getProtein();
						}
					}

					params.put("item13", total.getQuantity());
					total.setQuantity(0);

					for (FoodComposition food : rows14) {
						FoodComposition fd = foodMap.get(food.getId());
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

					params.put("item14", total.getQuantity());
					total.setQuantity(0);

					for (FoodComposition food : rows15) {
						FoodComposition fd = foodMap.get(food.getId());
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

					params.put("item15", total.getQuantity());
					total.setQuantity(0);

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

					params.put("avg", avg);
					params.put("fat", avg.getFat() * 9);
					params.put("protein", avg.getProtein() * 4);
					params.put("carbohydrate", avg.getCarbohydrate() * 4);

					double totalPersonHE = totalHeatEnergy / totalPerson;

					logger.debug("totalPerson:" + totalPerson);
					logger.debug("totalPersonHE:" + totalPersonHE);

					params.put("fatPercent", avg.getFat() * 9 / totalPersonHE * 100D);
					params.put("proteinPercent", avg.getProtein() * 4 / totalPersonHE * 100D);
					params.put("carbohydratePercent", avg.getCarbohydrate() * 4 / totalPersonHE * 100D);

					params.put("beansProtein", beansProtein * 4);
					params.put("animalProtein", animalsProtein * 4);

					params.put("beansProteinPercent", beansProtein * 4 / totalPersonHE * 100D);
					params.put("animalProteinPercent", animalsProtein * 4 / totalPersonHE * 100D);

					if (foodDRI != null) {
						FoodDRI percent = new FoodDRI();
						if (foodDRI.getCalcium() > 0) {
							percent.setCalcium(avg.getCalcium() / foodDRI.getCalcium() * 100D);
						}
						if (foodDRI.getCarbohydrate() > 0) {
							percent.setCarbohydrate(avg.getCarbohydrate() / foodDRI.getCarbohydrate() * 100D);
						}
						if (foodDRI.getFat() > 0) {
							percent.setFat(avg.getFat() / foodDRI.getFat() * 100D);
						}
						if (foodDRI.getHeatEnergy() > 0) {
							percent.setHeatEnergy(avg.getHeatEnergy() / foodDRI.getHeatEnergy() * 100D);
						}
						if (foodDRI.getIodine() > 0) {
							percent.setIodine(avg.getIodine() / foodDRI.getIodine() * 100D);
						}
						if (foodDRI.getIron() > 0) {
							percent.setIron(avg.getIron() / foodDRI.getIron() * 100D);
						}
						if (foodDRI.getNicotinicCid() > 0) {
							percent.setNicotinicCid(avg.getNicotinicCid() / foodDRI.getNicotinicCid() * 100D);
						}
						if (foodDRI.getPhosphorus() > 0) {
							percent.setPhosphorus(avg.getPhosphorus() / foodDRI.getPhosphorus() * 100D);
						}
						if (foodDRI.getProtein() > 0) {
							percent.setProtein(avg.getProtein() / foodDRI.getProtein() * 100D);
						}
						if (foodDRI.getRetinol() > 0) {
							percent.setRetinol(avg.getRetinol() / foodDRI.getRetinol() * 100D);
						}
						if (foodDRI.getVitaminA() > 0) {
							percent.setVitaminA(avg.getVitaminA() / foodDRI.getVitaminA() * 100D);
						}
						if (foodDRI.getVitaminB1() > 0) {
							percent.setVitaminB1(avg.getVitaminB1() / foodDRI.getVitaminB1() * 100D);
						}
						if (foodDRI.getVitaminB12() > 0) {
							percent.setVitaminB12(avg.getVitaminB12() / foodDRI.getVitaminB12() * 100D);
						}
						if (foodDRI.getVitaminB2() > 0) {
							percent.setVitaminB2(avg.getVitaminB2() / foodDRI.getVitaminB2() * 100D);
						}
						if (foodDRI.getVitaminB6() > 0) {
							percent.setVitaminB6(avg.getVitaminB6() / foodDRI.getVitaminB6() * 100D);
						}
						if (foodDRI.getVitaminC() > 0) {
							percent.setVitaminC(avg.getVitaminC() / foodDRI.getVitaminC() * 100D);
						}
						if (foodDRI.getZinc() > 0) {
							percent.setZinc(avg.getZinc() / foodDRI.getZinc() * 100D);
						}
						params.put("p", percent);
						params.put("percent", percent);

						String fatStd = "";
						fatStd = Math.round(foodDRI.getHeatEnergy() * 0.2) + "~"
								+ Math.round(foodDRI.getHeatEnergy() * 0.35);
						params.put("fatStd", fatStd);

						String proteinStd = "";
						proteinStd = Math.round(foodDRI.getHeatEnergy() * 0.12) + "~"
								+ Math.round(foodDRI.getHeatEnergy() * 0.15);
						params.put("proteinStd", proteinStd);

						String carbohydrateStd = "";
						carbohydrateStd = Math.round(foodDRI.getHeatEnergy() * 0.5) + "~"
								+ Math.round(foodDRI.getHeatEnergy() * 0.55);
						params.put("carbohydrateStd", carbohydrateStd);

					}

				}
			}
		}
	}

}
