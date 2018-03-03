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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.glaf.base.modules.sys.service.TenantConfigService;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.util.DateUtils;

import com.glaf.heathcare.domain.DietaryRptModel;
import com.glaf.heathcare.domain.DietaryStatistics;
import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.domain.GoodsActualQuantity;
import com.glaf.heathcare.query.FoodCompositionQuery;
import com.glaf.heathcare.query.GoodsActualQuantityQuery;
import com.glaf.heathcare.service.DietaryStatisticsService;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.heathcare.service.GoodsActualQuantityService;

public class DayFoodActualQuantityStatisticsBean {

	protected TenantConfigService tenantConfigService;

	protected GoodsActualQuantityService goodsActualQuantityService;

	protected DietaryStatisticsService dietaryStatisticsService;

	protected FoodCompositionService foodCompositionService;

	public void execute(String tenantId, int fullDay) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(DateUtils.toDate(String.valueOf(fullDay)));

		String sysFlag = "N";
		int suitNo = fullDay;
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

		GoodsActualQuantityQuery query = new GoodsActualQuantityQuery();
		query.tenantId(tenantId);
		query.fullDay(fullDay);

		List<GoodsActualQuantity> list = getGoodsActualQuantityService().list(query);
		if (list != null && !list.isEmpty()) {
			FoodCompositionQuery query2 = new FoodCompositionQuery();
			List<FoodComposition> foods = getFoodCompositionService().list(query2);
			Map<Long, Long> foodMap = new HashMap<Long, Long>();
			Map<Long, FoodComposition> foodXMap = new HashMap<Long, FoodComposition>();
			for (FoodComposition food : foods) {
				foodMap.put(food.getId(), food.getNodeId());
				foodXMap.put(food.getId(), food);
			}

			FoodComposition food = null;

			double heatEnergyGrain = 0.0;
			double heatEnergyBeans = 0.0;
			double heatEnergyAnimals = 0.0;
			double heatEnergyOthers = 0.0;

			double proteinBeans = 0.0;
			double proteinAnimals = 0.0;
			double proteinOthers = 0.0;

			double heatEnergyPlant = 0.0;
			double heatEnergyAnimal = 0.0;

			double heatEnergyFat = 0.0;
			double heatEnergyProtein = 0.0;
			double heatEnergyCarbohydrate = 0.0;

			double vitaminAAnimals = 0.0;
			double vitaminATotal = 0.0;

			double ironAnimals = 0.0;
			double ironTotal = 0.0;

			double fatAnimals2 = 0.0;
			double fatTotal2 = 0.0;

			double calciumTotal2 = 0.0;
			double phosphorusTotal2 = 0.0;

			for (GoodsActualQuantity item : list) {
				Long nodeId = foodMap.get(item.getGoodsId());
				food = foodXMap.get(item.getGoodsId());
				if (nodeId != null && food != null) {
					vitaminATotal = vitaminATotal + food.getVitaminA() * item.getQuantity() / 100.0D;
					ironTotal = ironTotal + food.getIron() * item.getQuantity() / 100.0D;
					calciumTotal2 = calciumTotal2 + food.getCalcium() * item.getQuantity() / 100.0D;
					phosphorusTotal2 = phosphorusTotal2 + food.getPhosphorus() * item.getQuantity() / 100.0D;
					fatTotal2 = fatTotal2 + food.getFat() * item.getQuantity() / 100.0D;
					switch (nodeId.intValue()) {
					case 4402:// 谷类
						heatEnergyGrain = heatEnergyGrain + food.getHeatEnergy() * item.getQuantity() / 100.0D;
						proteinOthers = proteinOthers + food.getProtein();
						heatEnergyPlant = heatEnergyPlant + food.getHeatEnergy() * item.getQuantity() / 100.0D;
						break;
					case 4404:// 豆类
						heatEnergyBeans = heatEnergyBeans + food.getHeatEnergy() * item.getQuantity() / 100.0D;
						proteinBeans = proteinBeans + food.getProtein();
						heatEnergyPlant = heatEnergyPlant + food.getHeatEnergy() * item.getQuantity() / 100.0D;
						break;
					case 4409:// 动物类
					case 4410:
					case 4411:
					case 4412:
					case 4413:
						heatEnergyAnimals = heatEnergyAnimals + food.getHeatEnergy() * item.getQuantity() / 100.0D;
						proteinAnimals = proteinAnimals + food.getProtein();
						heatEnergyAnimal = heatEnergyAnimal + food.getHeatEnergy() * item.getQuantity() / 100.0D;
						vitaminAAnimals = vitaminAAnimals + food.getVitaminA() * item.getQuantity() / 100.0D;
						ironAnimals = ironAnimals + food.getIron() * item.getQuantity() / 100.0D;
						fatAnimals2 = fatAnimals2 + food.getFat() * item.getQuantity() / 100.0D;
						break;
					default:// 其他
						heatEnergyOthers = heatEnergyOthers + food.getHeatEnergy() * item.getQuantity() / 100.0D;
						proteinOthers = proteinOthers + food.getProtein();
						heatEnergyPlant = heatEnergyPlant + food.getHeatEnergy() * item.getQuantity() / 100.0D;
						break;
					}
					if (food.getCarbohydrate() > 0) {
						heatEnergyCarbohydrate = heatEnergyCarbohydrate
								+ food.getCarbohydrate() * item.getQuantity() / 100D * 4;
					}
					if (food.getFat() > 0) {
						heatEnergyFat = heatEnergyFat + food.getFat() * item.getQuantity() / 100D * 9;
					}
					if (food.getProtein() > 0) {
						heatEnergyProtein = heatEnergyProtein + food.getProtein() * item.getQuantity() / 100D * 4;
					}
				}
			}

			List<DietaryStatistics> rows = new ArrayList<DietaryStatistics>();

			double tt = heatEnergyGrain + heatEnergyBeans + heatEnergyAnimals + heatEnergyOthers;
			double pp = proteinBeans + proteinAnimals + proteinOthers;

			DietaryStatistics m1 = new DietaryStatistics();
			m1.setTenantId(tenantId);
			m1.setSysFlag(sysFlag);
			m1.setSuitNo(suitNo);
			m1.setDayOfWeek(dayOfWeek);
			m1.setType("heatEnergyPerDayFoodActualQuantity");
			m1.setTitle("谷类");
			m1.setName("heatEnergyGrain");
			m1.setValue(Math.round(heatEnergyGrain));
			m1.setSortNo(4);
			rows.add(m1);

			DietaryStatistics m11 = new DietaryStatistics();
			m11.setTenantId(tenantId);
			m11.setSysFlag(sysFlag);
			m11.setSuitNo(suitNo);
			m11.setDayOfWeek(dayOfWeek);
			m11.setType("heatEnergyPercentPerDayFoodActualQuantity");
			m11.setTitle("谷类");
			m11.setName("heatEnergyGrainPercent");
			m11.setValue(Math.round(heatEnergyGrain / tt * 100D));
			m11.setSortNo(4);
			rows.add(m11);

			DietaryStatistics m2 = new DietaryStatistics();
			m2.setTenantId(tenantId);
			m2.setSysFlag(sysFlag);
			m2.setSuitNo(suitNo);
			m2.setDayOfWeek(dayOfWeek);
			m2.setType("heatEnergyPerDayFoodActualQuantity");
			m2.setTitle("豆类");
			m2.setName("heatEnergyBeans");
			m2.setValue(Math.round(heatEnergyBeans));
			m2.setSortNo(1);
			rows.add(m2);

			DietaryStatistics m22 = new DietaryStatistics();
			m22.setTenantId(tenantId);
			m22.setSysFlag(sysFlag);
			m22.setSuitNo(suitNo);
			m22.setDayOfWeek(dayOfWeek);
			m22.setType("heatEnergyPercentPerDayFoodActualQuantity");
			m22.setTitle("豆类");
			m22.setName("heatEnergyBeansPercent");
			m22.setValue(Math.round(heatEnergyBeans / tt * 100D));
			m22.setSortNo(1);
			rows.add(m22);

			DietaryStatistics m3 = new DietaryStatistics();
			m3.setTenantId(tenantId);
			m3.setSysFlag(sysFlag);
			m3.setSuitNo(suitNo);
			m3.setDayOfWeek(dayOfWeek);
			m3.setType("heatEnergyPerDayFoodActualQuantity");
			m3.setTitle("动物类食物");
			m3.setName("heatEnergyAnimals");
			m3.setValue(Math.round(heatEnergyAnimals));
			m3.setSortNo(2);
			rows.add(m3);

			DietaryStatistics m33 = new DietaryStatistics();
			m33.setTenantId(tenantId);
			m33.setSysFlag(sysFlag);
			m33.setSuitNo(suitNo);
			m33.setDayOfWeek(dayOfWeek);
			m33.setType("heatEnergyPercentPerDayFoodActualQuantity");
			m33.setTitle("动物类食物");
			m33.setName("heatEnergyAnimalsPercent");
			m33.setValue(Math.round(heatEnergyAnimals / tt * 100D));
			m33.setSortNo(2);
			rows.add(m33);

			DietaryStatistics m4 = new DietaryStatistics();
			m4.setTenantId(tenantId);
			m4.setSysFlag(sysFlag);
			m4.setSuitNo(suitNo);
			m4.setDayOfWeek(dayOfWeek);
			m4.setType("heatEnergyPerDayFoodActualQuantity");
			m4.setTitle("谷、薯、杂豆及其他");
			m4.setName("heatEnergyOthers");
			m4.setValue(Math.round(heatEnergyOthers));
			m4.setSortNo(3);
			rows.add(m4);

			DietaryStatistics m44 = new DietaryStatistics();
			m44.setTenantId(tenantId);
			m44.setSysFlag(sysFlag);
			m44.setSuitNo(suitNo);
			m44.setDayOfWeek(dayOfWeek);
			m44.setType("heatEnergyPercentPerDayFoodActualQuantity");
			m44.setTitle("谷、薯、杂豆及其他");
			m44.setName("heatEnergyOthersPercent");
			m44.setValue(Math.round(heatEnergyOthers / tt * 100D));
			m44.setSortNo(3);
			rows.add(m44);

			DietaryStatistics mp2 = new DietaryStatistics();
			mp2.setTenantId(tenantId);
			mp2.setSysFlag(sysFlag);
			mp2.setSuitNo(suitNo);
			mp2.setDayOfWeek(dayOfWeek);
			mp2.setType("proteinPerFoodActualQuantity");
			mp2.setTitle("豆类");
			mp2.setName("proteinBeans");
			mp2.setValue(Math.round(proteinBeans));
			mp2.setSortNo(1);
			rows.add(mp2);

			DietaryStatistics mp22 = new DietaryStatistics();
			mp22.setTenantId(tenantId);
			mp22.setSysFlag(sysFlag);
			mp22.setSuitNo(suitNo);
			mp22.setDayOfWeek(dayOfWeek);
			mp22.setType("proteinPercentPerDayFoodActualQuantity");
			mp22.setTitle("豆类");
			mp22.setName("proteinBeansPercent");
			mp22.setValue(Math.round(proteinBeans / pp * 100D));
			mp22.setSortNo(1);
			rows.add(mp22);

			DietaryStatistics mp3 = new DietaryStatistics();
			mp3.setTenantId(tenantId);
			mp3.setSysFlag(sysFlag);
			mp3.setSuitNo(suitNo);
			mp3.setDayOfWeek(dayOfWeek);
			mp3.setType("proteinPerDayFoodActualQuantity");
			mp3.setTitle("动物类食物");
			mp3.setName("proteinAnimals");
			mp3.setValue(Math.round(proteinAnimals));
			mp3.setSortNo(2);
			rows.add(mp3);

			DietaryStatistics mp33 = new DietaryStatistics();
			mp33.setTenantId(tenantId);
			mp33.setSysFlag(sysFlag);
			mp33.setSuitNo(suitNo);
			mp33.setDayOfWeek(dayOfWeek);
			mp33.setType("proteinPercentPerDayFoodActualQuantity");
			mp33.setTitle("动物类食物");
			mp33.setName("proteinAnimalsPercent");
			mp33.setValue(Math.round(proteinAnimals / pp * 100D));
			mp33.setSortNo(2);
			rows.add(mp33);

			DietaryStatistics mp4 = new DietaryStatistics();
			mp4.setTenantId(tenantId);
			mp4.setSysFlag(sysFlag);
			mp4.setSuitNo(suitNo);
			mp4.setDayOfWeek(dayOfWeek);
			mp4.setType("proteinPerDayFoodActualQuantity");
			mp4.setTitle("谷、薯、杂豆及其他");
			mp4.setName("proteinOthers");
			mp4.setValue(Math.round(proteinOthers));
			mp4.setSortNo(3);
			rows.add(mp4);

			DietaryStatistics mp44 = new DietaryStatistics();
			mp44.setTenantId(tenantId);
			mp44.setSysFlag(sysFlag);
			mp44.setSuitNo(suitNo);
			mp44.setDayOfWeek(dayOfWeek);
			mp44.setType("proteinPercentPerDayFoodActualQuantity");
			mp44.setTitle("谷、薯、杂豆及其他");
			mp44.setName("proteinOthersPercent");
			mp44.setValue(Math.round(proteinOthers / pp * 100D));
			mp44.setSortNo(3);
			rows.add(mp44);

			tt = heatEnergyPlant + heatEnergyAnimal;

			if (tt > 0) {
				DietaryStatistics mx1 = new DietaryStatistics();
				mx1.setTenantId(tenantId);
				mx1.setSysFlag(sysFlag);
				mx1.setSuitNo(suitNo);
				mx1.setDayOfWeek(dayOfWeek);
				mx1.setType("heatEnergyX1PerDayFoodActualQuantity");
				mx1.setTitle("动物性食物");
				mx1.setName("heatEnergyAnimal");
				mx1.setValue(Math.round(heatEnergyAnimal));
				mx1.setSortNo(1);
				rows.add(mx1);

				DietaryStatistics mx11 = new DietaryStatistics();
				mx11.setTenantId(tenantId);
				mx11.setSysFlag(sysFlag);
				mx11.setSuitNo(suitNo);
				mx11.setDayOfWeek(dayOfWeek);
				mx11.setType("heatEnergyX1PercentPerDayFoodActualQuantity");
				mx11.setTitle("动物性食物");
				mx11.setName("heatEnergyAnimalPercent");
				mx11.setValue(Math.round(heatEnergyAnimal / tt * 100D));
				mx11.setSortNo(1);
				rows.add(mx11);

				DietaryStatistics mx2 = new DietaryStatistics();
				mx2.setTenantId(tenantId);
				mx2.setSysFlag(sysFlag);
				mx2.setSuitNo(suitNo);
				mx2.setDayOfWeek(dayOfWeek);
				mx2.setType("heatEnergyX1PerDayFoodActualQuantity");
				mx2.setTitle("植物性食物");
				mx2.setName("heatEnergyPlant");
				mx2.setValue(Math.round(heatEnergyPlant));
				mx2.setSortNo(2);
				rows.add(mx2);

				DietaryStatistics mx22 = new DietaryStatistics();
				mx22.setTenantId(tenantId);
				mx22.setSysFlag(sysFlag);
				mx22.setSuitNo(suitNo);
				mx22.setDayOfWeek(dayOfWeek);
				mx22.setType("heatEnergyX1PercentPerDayFoodActualQuantity");
				mx22.setTitle("植物性食物");
				mx22.setName("heatEnergyPlantPercent");
				mx22.setValue(Math.round(heatEnergyPlant / tt * 100D));
				mx22.setSortNo(2);
				rows.add(mx22);

			}

			tt = heatEnergyFat + heatEnergyProtein + heatEnergyCarbohydrate;
			if (tt > 0) {
				DietaryStatistics mx1 = new DietaryStatistics();
				mx1.setTenantId(tenantId);
				mx1.setSysFlag(sysFlag);
				mx1.setSuitNo(suitNo);
				mx1.setDayOfWeek(dayOfWeek);
				mx1.setType("heatEnergyX2PerDayFoodActualQuantity");
				mx1.setTitle("脂肪");
				mx1.setName("heatEnergyFat");
				mx1.setValue(Math.round(heatEnergyFat));
				mx1.setSortNo(2);
				rows.add(mx1);

				DietaryStatistics mx2 = new DietaryStatistics();
				mx2.setTenantId(tenantId);
				mx2.setSysFlag(sysFlag);
				mx2.setSuitNo(suitNo);
				mx2.setDayOfWeek(dayOfWeek);
				mx2.setType("heatEnergyX2PerDayFoodActualQuantity");
				mx2.setTitle("蛋白质");
				mx2.setName("heatEnergyProtein");
				mx2.setValue(Math.round(heatEnergyProtein));
				mx2.setSortNo(1);
				rows.add(mx2);

				DietaryStatistics mx3 = new DietaryStatistics();
				mx3.setTenantId(tenantId);
				mx3.setSysFlag(sysFlag);
				mx3.setSuitNo(suitNo);
				mx3.setDayOfWeek(dayOfWeek);
				mx3.setType("heatEnergyX2PerDayFoodActualQuantity");
				mx3.setTitle("碳水化合物");
				mx3.setName("heatEnergyCarbohydrate");
				mx3.setValue(Math.round(heatEnergyCarbohydrate));
				mx3.setSortNo(3);
				rows.add(mx3);

				DietaryStatistics mx11 = new DietaryStatistics();
				mx11.setTenantId(tenantId);
				mx11.setSysFlag(sysFlag);
				mx11.setSuitNo(suitNo);
				mx11.setDayOfWeek(dayOfWeek);
				mx11.setType("heatEnergyX2PercentPerDayFoodActualQuantity");
				mx11.setTitle("脂肪");
				mx11.setName("heatEnergyFatPercent");
				mx11.setValue(Math.round(heatEnergyFat / tt * 100D));
				mx11.setSortNo(2);
				rows.add(mx11);

				DietaryStatistics mx12 = new DietaryStatistics();
				mx12.setTenantId(tenantId);
				mx12.setSysFlag(sysFlag);
				mx12.setSuitNo(suitNo);
				mx12.setDayOfWeek(dayOfWeek);
				mx12.setType("heatEnergyX2PercentPerDayFoodActualQuantity");
				mx12.setTitle("蛋白质");
				mx12.setName("heatEnergyProteinPercent");
				mx12.setValue(Math.round(heatEnergyProtein / tt * 100D));
				mx12.setSortNo(1);
				rows.add(mx12);

				DietaryStatistics mx13 = new DietaryStatistics();
				mx13.setTenantId(tenantId);
				mx13.setSysFlag(sysFlag);
				mx13.setSuitNo(suitNo);
				mx13.setDayOfWeek(dayOfWeek);
				mx13.setType("heatEnergyX2PercentPerDayFoodActualQuantity");
				mx13.setTitle("碳水化合物");
				mx13.setName("heatEnergyCarbohydratePercent");
				mx13.setValue(Math.round(heatEnergyCarbohydrate / tt * 100D));
				mx13.setSortNo(3);
				rows.add(mx13);

			}

			if (vitaminATotal > 0) {
				double p1 = vitaminAAnimals / vitaminATotal;
				DietaryStatistics mx15 = new DietaryStatistics();
				mx15.setTenantId(tenantId);
				mx15.setSysFlag(sysFlag);
				mx15.setSuitNo(suitNo);
				mx15.setDayOfWeek(dayOfWeek);
				mx15.setType("vitaminAAnimalsX2PercentPerDayFoodActualQuantity");
				mx15.setTitle("动物性V-A");
				mx15.setName("vitaminAAnimalsPercent");
				mx15.setValue(Math.round(p1 * 100D));
				mx15.setSortNo(3);
				rows.add(mx15);

				DietaryStatistics mx16 = new DietaryStatistics();
				mx16.setTenantId(tenantId);
				mx16.setSysFlag(sysFlag);
				mx16.setSuitNo(suitNo);
				mx16.setDayOfWeek(dayOfWeek);
				mx16.setType("vitaminAAnimalsX2PercentPerDayFoodActualQuantity");
				mx16.setTitle("其他");
				mx16.setName("vitaminAOthersPercent");
				mx16.setValue(100 - mx15.getValue());
				mx16.setSortNo(3);
				rows.add(mx16);
			}

			if (ironTotal > 0) {
				double p2 = ironAnimals / ironTotal;
				DietaryStatistics mx15 = new DietaryStatistics();
				mx15.setTenantId(tenantId);
				mx15.setSysFlag(sysFlag);
				mx15.setSuitNo(suitNo);
				mx15.setDayOfWeek(dayOfWeek);
				mx15.setType("ironAnimalsX2PercentPerDayFoodActualQuantity");
				mx15.setTitle("动物性铁");
				mx15.setName("ironAnimalsPercent");
				mx15.setValue(Math.round(p2 * 100D));
				mx15.setSortNo(3);
				rows.add(mx15);

				DietaryStatistics mx16 = new DietaryStatistics();
				mx16.setTenantId(tenantId);
				mx16.setSysFlag(sysFlag);
				mx16.setSuitNo(suitNo);
				mx16.setDayOfWeek(dayOfWeek);
				mx16.setType("ironAnimalsX2PercentPerDayFoodActualQuantity");
				mx16.setTitle("其他");
				mx16.setName("ironOthersPercent");
				mx16.setValue(100 - mx15.getValue());
				mx16.setSortNo(3);
				rows.add(mx16);
			}

			if (fatTotal2 > 0) {
				double p2 = fatAnimals2 / fatTotal2;
				DietaryStatistics mx15 = new DietaryStatistics();
				mx15.setTenantId(tenantId);
				mx15.setSysFlag(sysFlag);
				mx15.setSuitNo(suitNo);
				mx15.setDayOfWeek(dayOfWeek);
				mx15.setType("fatAnimalsX2PercentPerDayFoodActualQuantity");
				mx15.setTitle("动物性脂肪");
				mx15.setName("fatAnimalsPercent");
				mx15.setValue(Math.round(p2 * 100D));
				mx15.setSortNo(3);
				rows.add(mx15);

				DietaryStatistics mx16 = new DietaryStatistics();
				mx16.setTenantId(tenantId);
				mx16.setSysFlag(sysFlag);
				mx16.setSuitNo(suitNo);
				mx16.setDayOfWeek(dayOfWeek);
				mx16.setType("fatAnimalsX2PercentPerDayFoodActualQuantity");
				mx16.setTitle("其他");
				mx16.setName("fatOthersPercent");
				mx16.setValue(100 - mx15.getValue());
				mx16.setSortNo(3);
				rows.add(mx16);
			}

			if (calciumTotal2 > 0 && phosphorusTotal2 > 0) {
				double p2 = phosphorusTotal2 / (calciumTotal2 + phosphorusTotal2);
				DietaryStatistics mx15 = new DietaryStatistics();
				mx15.setTenantId(tenantId);
				mx15.setSysFlag(sysFlag);
				mx15.setSuitNo(suitNo);
				mx15.setDayOfWeek(dayOfWeek);
				mx15.setType("calciumPhosphorusX2PercentPerDayFoodActualQuantity");
				mx15.setTitle("磷占比");
				mx15.setName("phosphorusPercent");
				mx15.setValue(Math.round(p2 * 100D));
				mx15.setSortNo(3);
				rows.add(mx15);

				DietaryStatistics mx16 = new DietaryStatistics();
				mx16.setTenantId(tenantId);
				mx16.setSysFlag(sysFlag);
				mx16.setSuitNo(suitNo);
				mx16.setDayOfWeek(dayOfWeek);
				mx16.setType("calciumPhosphorusX2PercentPerDayFoodActualQuantity");
				mx16.setTitle("钙占比");
				mx16.setName("calciumPercent");
				mx16.setValue(100 - mx15.getValue());
				mx16.setSortNo(3);
				rows.add(mx16);
			}

			getDietaryStatisticsService().saveAll(tenantId, sysFlag, suitNo, dayOfWeek, rows);

		}
	}

	public void execute(String tenantId, String batchNo, Date startDate, Date endDate) {
		String sysFlag = "N";
		int suitNo = 10000;
		int dayOfWeek = 0;

		GoodsActualQuantityQuery query = new GoodsActualQuantityQuery();
		query.tenantId(tenantId);
		query.usageTimeGreaterThanOrEqual(startDate);
		query.usageTimeLessThanOrEqual(endDate);

		List<GoodsActualQuantity> list = getGoodsActualQuantityService().list(query);
		if (list != null && !list.isEmpty()) {
			FoodCompositionQuery query2 = new FoodCompositionQuery();
			List<FoodComposition> foods = getFoodCompositionService().list(query2);
			Map<Long, Long> foodMap = new HashMap<Long, Long>();
			Map<Long, FoodComposition> foodXMap = new HashMap<Long, FoodComposition>();
			for (FoodComposition food : foods) {
				foodMap.put(food.getId(), food.getNodeId());
				foodXMap.put(food.getId(), food);
			}

			FoodComposition food = null;

			double heatEnergyGrain = 0.0;
			double heatEnergyBeans = 0.0;
			double heatEnergyAnimals = 0.0;
			double heatEnergyOthers = 0.0;

			double proteinBeans = 0.0;
			double proteinAnimals = 0.0;
			double proteinOthers = 0.0;

			double heatEnergyPlant = 0.0;
			double heatEnergyAnimal = 0.0;

			double heatEnergyFat = 0.0;
			double heatEnergyProtein = 0.0;
			double heatEnergyCarbohydrate = 0.0;

			double vitaminAAnimals = 0.0;
			double vitaminATotal = 0.0;

			double ironAnimals = 0.0;
			double ironTotal = 0.0;

			double fatAnimals2 = 0.0;
			double fatTotal2 = 0.0;

			double calciumTotal2 = 0.0;
			double phosphorusTotal2 = 0.0;

			for (GoodsActualQuantity item : list) {
				Long nodeId = foodMap.get(item.getGoodsId());
				food = foodXMap.get(item.getGoodsId());
				if (nodeId != null && food != null) {
					vitaminATotal = vitaminATotal + food.getVitaminA() * item.getQuantity() / 100.0D;
					ironTotal = ironTotal + food.getIron() * item.getQuantity() / 100.0D;
					calciumTotal2 = calciumTotal2 + food.getCalcium() * item.getQuantity() / 100.0D;
					phosphorusTotal2 = phosphorusTotal2 + food.getPhosphorus() * item.getQuantity() / 100.0D;
					fatTotal2 = fatTotal2 + food.getFat() * item.getQuantity() / 100.0D;
					switch (nodeId.intValue()) {
					case 4402:// 谷类
						heatEnergyGrain = heatEnergyGrain + food.getHeatEnergy() * item.getQuantity() / 100.0D;
						proteinOthers = proteinOthers + food.getProtein();
						heatEnergyPlant = heatEnergyPlant + food.getHeatEnergy() * item.getQuantity() / 100.0D;
						break;
					case 4404:// 豆类
						heatEnergyBeans = heatEnergyBeans + food.getHeatEnergy() * item.getQuantity() / 100.0D;
						proteinBeans = proteinBeans + food.getProtein();
						heatEnergyPlant = heatEnergyPlant + food.getHeatEnergy() * item.getQuantity() / 100.0D;
						break;
					case 4409:// 动物类
					case 4410:
					case 4411:
					case 4412:
					case 4413:
						heatEnergyAnimals = heatEnergyAnimals + food.getHeatEnergy() * item.getQuantity() / 100.0D;
						proteinAnimals = proteinAnimals + food.getProtein();
						heatEnergyAnimal = heatEnergyAnimal + food.getHeatEnergy() * item.getQuantity() / 100.0D;
						vitaminAAnimals = vitaminAAnimals + food.getVitaminA() * item.getQuantity() / 100.0D;
						ironAnimals = ironAnimals + food.getIron() * item.getQuantity() / 100.0D;
						fatAnimals2 = fatAnimals2 + food.getFat() * item.getQuantity() / 100.0D;
						break;
					default:// 其他
						heatEnergyOthers = heatEnergyOthers + food.getHeatEnergy() * item.getQuantity() / 100.0D;
						proteinOthers = proteinOthers + food.getProtein();
						heatEnergyPlant = heatEnergyPlant + food.getHeatEnergy() * item.getQuantity() / 100.0D;
						break;
					}
					if (food.getCarbohydrate() > 0) {
						heatEnergyCarbohydrate = heatEnergyCarbohydrate
								+ food.getCarbohydrate() * item.getQuantity() / 100D * 4;
					}
					if (food.getFat() > 0) {
						heatEnergyFat = heatEnergyFat + food.getFat() * item.getQuantity() / 100D * 9;
					}
					if (food.getProtein() > 0) {
						heatEnergyProtein = heatEnergyProtein + food.getProtein() * item.getQuantity() / 100D * 4;
					}
				}
			}

			List<DietaryStatistics> rows = new ArrayList<DietaryStatistics>();

			double tt = heatEnergyGrain + heatEnergyBeans + heatEnergyAnimals + heatEnergyOthers;
			double pp = proteinBeans + proteinAnimals + proteinOthers;

			DietaryStatistics m1 = new DietaryStatistics();
			m1.setTenantId(tenantId);
			m1.setBatchNo(batchNo);
			m1.setSysFlag(sysFlag);
			m1.setSuitNo(suitNo);
			m1.setDayOfWeek(dayOfWeek);
			m1.setType("heatEnergyFoodActualQuantity");
			m1.setTitle("谷类");
			m1.setName("heatEnergyGrain");
			m1.setValue(Math.round(heatEnergyGrain));
			m1.setSortNo(4);
			rows.add(m1);

			DietaryStatistics m11 = new DietaryStatistics();
			m11.setTenantId(tenantId);
			m11.setBatchNo(batchNo);
			m11.setSysFlag(sysFlag);
			m11.setSuitNo(suitNo);
			m11.setDayOfWeek(dayOfWeek);
			m11.setType("heatEnergyPercentFoodActualQuantity");
			m11.setTitle("谷类");
			m11.setName("heatEnergyGrainPercent");
			m11.setValue(Math.round(heatEnergyGrain / tt * 100D));
			m11.setSortNo(4);
			rows.add(m11);

			DietaryStatistics m2 = new DietaryStatistics();
			m2.setTenantId(tenantId);
			m2.setBatchNo(batchNo);
			m2.setSysFlag(sysFlag);
			m2.setSuitNo(suitNo);
			m2.setDayOfWeek(dayOfWeek);
			m2.setType("heatEnergyFoodActualQuantity");
			m2.setTitle("豆类");
			m2.setName("heatEnergyBeans");
			m2.setValue(Math.round(heatEnergyBeans));
			m2.setSortNo(1);
			rows.add(m2);

			DietaryStatistics m22 = new DietaryStatistics();
			m22.setTenantId(tenantId);
			m22.setBatchNo(batchNo);
			m22.setSysFlag(sysFlag);
			m22.setSuitNo(suitNo);
			m22.setDayOfWeek(dayOfWeek);
			m22.setType("heatEnergyPercentFoodActualQuantity");
			m22.setTitle("豆类");
			m22.setName("heatEnergyBeansPercent");
			m22.setValue(Math.round(heatEnergyBeans / tt * 100D));
			m22.setSortNo(1);
			rows.add(m22);

			DietaryStatistics m3 = new DietaryStatistics();
			m3.setTenantId(tenantId);
			m3.setBatchNo(batchNo);
			m3.setSysFlag(sysFlag);
			m3.setSuitNo(suitNo);
			m3.setDayOfWeek(dayOfWeek);
			m3.setType("heatEnergyFoodActualQuantity");
			m3.setTitle("动物类食物");
			m3.setName("heatEnergyAnimals");
			m3.setValue(Math.round(heatEnergyAnimals));
			m3.setSortNo(2);
			rows.add(m3);

			DietaryStatistics m33 = new DietaryStatistics();
			m33.setTenantId(tenantId);
			m33.setBatchNo(batchNo);
			m33.setSysFlag(sysFlag);
			m33.setSuitNo(suitNo);
			m33.setDayOfWeek(dayOfWeek);
			m33.setType("heatEnergyPercentFoodActualQuantity");
			m33.setTitle("动物类食物");
			m33.setName("heatEnergyAnimalsPercent");
			m33.setValue(Math.round(heatEnergyAnimals / tt * 100D));
			m33.setSortNo(2);
			rows.add(m33);

			DietaryStatistics m4 = new DietaryStatistics();
			m4.setTenantId(tenantId);
			m4.setBatchNo(batchNo);
			m4.setSysFlag(sysFlag);
			m4.setSuitNo(suitNo);
			m4.setDayOfWeek(dayOfWeek);
			m4.setType("heatEnergyFoodActualQuantity");
			m4.setTitle("谷、薯、杂豆及其他");
			m4.setName("heatEnergyOthers");
			m4.setValue(Math.round(heatEnergyOthers));
			m4.setSortNo(3);
			rows.add(m4);

			DietaryStatistics m44 = new DietaryStatistics();
			m44.setTenantId(tenantId);
			m44.setBatchNo(batchNo);
			m44.setSysFlag(sysFlag);
			m44.setSuitNo(suitNo);
			m44.setDayOfWeek(dayOfWeek);
			m44.setType("heatEnergyPercentFoodActualQuantity");
			m44.setTitle("谷、薯、杂豆及其他");
			m44.setName("heatEnergyOthersPercent");
			m44.setValue(Math.round(heatEnergyOthers / tt * 100D));
			m44.setSortNo(3);
			rows.add(m44);

			DietaryStatistics mp2 = new DietaryStatistics();
			mp2.setTenantId(tenantId);
			mp2.setBatchNo(batchNo);
			mp2.setSysFlag(sysFlag);
			mp2.setSuitNo(suitNo);
			mp2.setDayOfWeek(dayOfWeek);
			mp2.setType("proteinFoodActualQuantity");
			mp2.setTitle("豆类");
			mp2.setName("proteinBeans");
			mp2.setValue(Math.round(proteinBeans));
			mp2.setSortNo(1);
			rows.add(mp2);

			DietaryStatistics mp22 = new DietaryStatistics();
			mp22.setTenantId(tenantId);
			mp22.setBatchNo(batchNo);
			mp22.setSysFlag(sysFlag);
			mp22.setSuitNo(suitNo);
			mp22.setDayOfWeek(dayOfWeek);
			mp22.setType("proteinPercentFoodActualQuantity");
			mp22.setTitle("豆类");
			mp22.setName("proteinBeansPercent");
			mp22.setValue(Math.round(proteinBeans / pp * 100D));
			mp22.setSortNo(1);
			rows.add(mp22);

			DietaryStatistics mp3 = new DietaryStatistics();
			mp3.setTenantId(tenantId);
			mp3.setBatchNo(batchNo);
			mp3.setSysFlag(sysFlag);
			mp3.setSuitNo(suitNo);
			mp3.setDayOfWeek(dayOfWeek);
			mp3.setType("proteinFoodActualQuantity");
			mp3.setTitle("动物类食物");
			mp3.setName("proteinAnimals");
			mp3.setValue(Math.round(proteinAnimals));
			mp3.setSortNo(2);
			rows.add(mp3);

			DietaryStatistics mp33 = new DietaryStatistics();
			mp33.setTenantId(tenantId);
			mp33.setBatchNo(batchNo);
			mp33.setSysFlag(sysFlag);
			mp33.setSuitNo(suitNo);
			mp33.setDayOfWeek(dayOfWeek);
			mp33.setType("proteinPercentFoodActualQuantity");
			mp33.setTitle("动物类食物");
			mp33.setName("proteinAnimalsPercent");
			mp33.setValue(Math.round(proteinAnimals / pp * 100D));
			mp33.setSortNo(2);
			rows.add(mp33);

			DietaryStatistics mp4 = new DietaryStatistics();
			mp4.setTenantId(tenantId);
			mp4.setBatchNo(batchNo);
			mp4.setSysFlag(sysFlag);
			mp4.setSuitNo(suitNo);
			mp4.setDayOfWeek(dayOfWeek);
			mp4.setType("proteinFoodActualQuantity");
			mp4.setTitle("谷、薯、杂豆及其他");
			mp4.setName("proteinOthers");
			mp4.setValue(Math.round(proteinOthers));
			mp4.setSortNo(3);
			rows.add(mp4);

			DietaryStatistics mp44 = new DietaryStatistics();
			mp44.setTenantId(tenantId);
			mp44.setBatchNo(batchNo);
			mp44.setSysFlag(sysFlag);
			mp44.setSuitNo(suitNo);
			mp44.setDayOfWeek(dayOfWeek);
			mp44.setType("proteinPercentFoodActualQuantity");
			mp44.setTitle("谷、薯、杂豆及其他");
			mp44.setName("proteinOthersPercent");
			mp44.setValue(Math.round(proteinOthers / pp * 100D));
			mp44.setSortNo(3);
			rows.add(mp44);

			tt = heatEnergyPlant + heatEnergyAnimal;

			if (tt > 0) {
				DietaryStatistics mx1 = new DietaryStatistics();
				mx1.setTenantId(tenantId);
				mx1.setSysFlag(sysFlag);
				mx1.setBatchNo(batchNo);
				mx1.setSuitNo(suitNo);
				mx1.setDayOfWeek(dayOfWeek);
				mx1.setType("heatEnergyX1FoodActualQuantity");
				mx1.setTitle("动物性食物");
				mx1.setName("heatEnergyAnimal");
				mx1.setValue(Math.round(heatEnergyAnimal));
				mx1.setSortNo(1);
				rows.add(mx1);

				DietaryStatistics mx11 = new DietaryStatistics();
				mx11.setTenantId(tenantId);
				mx11.setBatchNo(batchNo);
				mx11.setSysFlag(sysFlag);
				mx11.setSuitNo(suitNo);
				mx11.setDayOfWeek(dayOfWeek);
				mx11.setType("heatEnergyX1PercentFoodActualQuantity");
				mx11.setTitle("动物性食物");
				mx11.setName("heatEnergyAnimalPercent");
				mx11.setValue(Math.round(heatEnergyAnimal / tt * 100D));
				mx11.setSortNo(1);
				rows.add(mx11);

				DietaryStatistics mx2 = new DietaryStatistics();
				mx2.setTenantId(tenantId);
				mx2.setBatchNo(batchNo);
				mx2.setSysFlag(sysFlag);
				mx2.setSuitNo(suitNo);
				mx2.setDayOfWeek(dayOfWeek);
				mx2.setType("heatEnergyX1FoodActualQuantity");
				mx2.setTitle("植物性食物");
				mx2.setName("heatEnergyPlant");
				mx2.setValue(Math.round(heatEnergyPlant));
				mx2.setSortNo(2);
				rows.add(mx2);

				DietaryStatistics mx22 = new DietaryStatistics();
				mx22.setTenantId(tenantId);
				mx22.setBatchNo(batchNo);
				mx22.setSysFlag(sysFlag);
				mx22.setSuitNo(suitNo);
				mx22.setDayOfWeek(dayOfWeek);
				mx22.setType("heatEnergyX1PercentFoodActualQuantity");
				mx22.setTitle("植物性食物");
				mx22.setName("heatEnergyPlantPercent");
				mx22.setValue(Math.round(heatEnergyPlant / tt * 100D));
				mx22.setSortNo(2);
				rows.add(mx22);

			}

			tt = heatEnergyFat + heatEnergyProtein + heatEnergyCarbohydrate;
			if (tt > 0) {
				DietaryStatistics mx1 = new DietaryStatistics();
				mx1.setTenantId(tenantId);
				mx1.setBatchNo(batchNo);
				mx1.setSysFlag(sysFlag);
				mx1.setSuitNo(suitNo);
				mx1.setDayOfWeek(dayOfWeek);
				mx1.setType("heatEnergyX2FoodActualQuantity");
				mx1.setTitle("脂肪");
				mx1.setName("heatEnergyFat");
				mx1.setValue(Math.round(heatEnergyFat));
				mx1.setSortNo(2);
				rows.add(mx1);

				DietaryStatistics mx2 = new DietaryStatistics();
				mx2.setTenantId(tenantId);
				mx2.setBatchNo(batchNo);
				mx2.setSysFlag(sysFlag);
				mx2.setSuitNo(suitNo);
				mx2.setDayOfWeek(dayOfWeek);
				mx2.setType("heatEnergyX2FoodActualQuantity");
				mx2.setTitle("蛋白质");
				mx2.setName("heatEnergyProtein");
				mx2.setValue(Math.round(heatEnergyProtein));
				mx2.setSortNo(1);
				rows.add(mx2);

				DietaryStatistics mx3 = new DietaryStatistics();
				mx3.setTenantId(tenantId);
				mx3.setBatchNo(batchNo);
				mx3.setSysFlag(sysFlag);
				mx3.setSuitNo(suitNo);
				mx3.setDayOfWeek(dayOfWeek);
				mx3.setType("heatEnergyX2FoodActualQuantity");
				mx3.setTitle("碳水化合物");
				mx3.setName("heatEnergyCarbohydrate");
				mx3.setValue(Math.round(heatEnergyCarbohydrate));
				mx3.setSortNo(3);
				rows.add(mx3);

				DietaryStatistics mx11 = new DietaryStatistics();
				mx11.setTenantId(tenantId);
				mx11.setBatchNo(batchNo);
				mx11.setSysFlag(sysFlag);
				mx11.setSuitNo(suitNo);
				mx11.setDayOfWeek(dayOfWeek);
				mx11.setType("heatEnergyX2PercentFoodActualQuantity");
				mx11.setTitle("脂肪");
				mx11.setName("heatEnergyFatPercent");
				mx11.setValue(Math.round(heatEnergyFat / tt * 100D));
				mx11.setSortNo(2);
				rows.add(mx11);

				DietaryStatistics mx12 = new DietaryStatistics();
				mx12.setTenantId(tenantId);
				mx12.setBatchNo(batchNo);
				mx12.setSysFlag(sysFlag);
				mx12.setSuitNo(suitNo);
				mx12.setDayOfWeek(dayOfWeek);
				mx12.setType("heatEnergyX2PercentFoodActualQuantity");
				mx12.setTitle("蛋白质");
				mx12.setName("heatEnergyProteinPercent");
				mx12.setValue(Math.round(heatEnergyProtein / tt * 100D));
				mx12.setSortNo(1);
				rows.add(mx12);

				DietaryStatistics mx13 = new DietaryStatistics();
				mx13.setTenantId(tenantId);
				mx13.setBatchNo(batchNo);
				mx13.setSysFlag(sysFlag);
				mx13.setSuitNo(suitNo);
				mx13.setDayOfWeek(dayOfWeek);
				mx13.setType("heatEnergyX2PercentFoodActualQuantity");
				mx13.setTitle("碳水化合物");
				mx13.setName("heatEnergyCarbohydratePercent");
				mx13.setValue(Math.round(heatEnergyCarbohydrate / tt * 100D));
				mx13.setSortNo(3);
				rows.add(mx13);

			}

			if (vitaminATotal > 0) {
				double p1 = vitaminAAnimals / vitaminATotal;
				DietaryStatistics mx15 = new DietaryStatistics();
				mx15.setTenantId(tenantId);
				mx15.setBatchNo(batchNo);
				mx15.setSysFlag(sysFlag);
				mx15.setSuitNo(suitNo);
				mx15.setDayOfWeek(dayOfWeek);
				mx15.setType("vitaminAAnimalsX2PercentFoodActualQuantity");
				mx15.setTitle("动物性V-A");
				mx15.setName("vitaminAAnimalsPercent");
				mx15.setValue(Math.round(p1 * 100D));
				mx15.setSortNo(3);
				rows.add(mx15);

				DietaryStatistics mx16 = new DietaryStatistics();
				mx16.setTenantId(tenantId);
				mx16.setBatchNo(batchNo);
				mx16.setSysFlag(sysFlag);
				mx16.setSuitNo(suitNo);
				mx16.setDayOfWeek(dayOfWeek);
				mx16.setType("vitaminAAnimalsX2PercentFoodActualQuantity");
				mx16.setTitle("其他");
				mx16.setName("vitaminAOthersPercent");
				mx16.setValue(100 - mx15.getValue());
				mx16.setSortNo(3);
				rows.add(mx16);
			}

			if (ironTotal > 0) {
				double p2 = ironAnimals / ironTotal;
				DietaryStatistics mx15 = new DietaryStatistics();
				mx15.setTenantId(tenantId);
				mx15.setBatchNo(batchNo);
				mx15.setSysFlag(sysFlag);
				mx15.setSuitNo(suitNo);
				mx15.setDayOfWeek(dayOfWeek);
				mx15.setType("ironAnimalsX2PercentFoodActualQuantity");
				mx15.setTitle("动物性铁");
				mx15.setName("ironAnimalsPercent");
				mx15.setValue(Math.round(p2 * 100D));
				mx15.setSortNo(3);
				rows.add(mx15);

				DietaryStatistics mx16 = new DietaryStatistics();
				mx16.setTenantId(tenantId);
				mx16.setBatchNo(batchNo);
				mx16.setSysFlag(sysFlag);
				mx16.setSuitNo(suitNo);
				mx16.setDayOfWeek(dayOfWeek);
				mx16.setType("ironAnimalsX2PercentFoodActualQuantity");
				mx16.setTitle("其他");
				mx16.setName("ironOthersPercent");
				mx16.setValue(100 - mx15.getValue());
				mx16.setSortNo(3);
				rows.add(mx16);
			}

			if (fatTotal2 > 0) {
				double p2 = fatAnimals2 / fatTotal2;
				DietaryStatistics mx15 = new DietaryStatistics();
				mx15.setTenantId(tenantId);
				mx15.setBatchNo(batchNo);
				mx15.setSysFlag(sysFlag);
				mx15.setSuitNo(suitNo);
				mx15.setDayOfWeek(dayOfWeek);
				mx15.setType("fatAnimalsX2PercentFoodActualQuantity");
				mx15.setTitle("动物性脂肪");
				mx15.setName("fatAnimalsPercent");
				mx15.setValue(Math.round(p2 * 100D));
				mx15.setSortNo(3);
				rows.add(mx15);

				DietaryStatistics mx16 = new DietaryStatistics();
				mx16.setTenantId(tenantId);
				mx16.setBatchNo(batchNo);
				mx16.setSysFlag(sysFlag);
				mx16.setSuitNo(suitNo);
				mx16.setDayOfWeek(dayOfWeek);
				mx16.setType("fatAnimalsX2PercentFoodActualQuantity");
				mx16.setTitle("其他");
				mx16.setName("fatOthersPercent");
				mx16.setValue(100 - mx15.getValue());
				mx16.setSortNo(3);
				rows.add(mx16);
			}

			if (calciumTotal2 > 0 && phosphorusTotal2 > 0) {
				double p2 = phosphorusTotal2 / (calciumTotal2 + phosphorusTotal2);
				DietaryStatistics mx15 = new DietaryStatistics();
				mx15.setTenantId(tenantId);
				mx15.setBatchNo(batchNo);
				mx15.setSysFlag(sysFlag);
				mx15.setSuitNo(suitNo);
				mx15.setDayOfWeek(dayOfWeek);
				mx15.setType("calciumPhosphorusX2PercentFoodActualQuantity");
				mx15.setTitle("磷占比");
				mx15.setName("phosphorusPercent");
				mx15.setValue(Math.round(p2 * 100D));
				mx15.setSortNo(3);
				rows.add(mx15);

				DietaryStatistics mx16 = new DietaryStatistics();
				mx16.setTenantId(tenantId);
				mx16.setBatchNo(batchNo);
				mx16.setSysFlag(sysFlag);
				mx16.setSuitNo(suitNo);
				mx16.setDayOfWeek(dayOfWeek);
				mx16.setType("calciumPhosphorusX2PercentFoodActualQuantity");
				mx16.setTitle("钙占比");
				mx16.setName("calciumPercent");
				mx16.setValue(100 - mx15.getValue());
				mx16.setSortNo(3);
				rows.add(mx16);
			}

			getDietaryStatisticsService().saveAll(tenantId, batchNo, rows);

		}
	}

	public DietaryStatisticsService getDietaryStatisticsService() {
		if (dietaryStatisticsService == null) {
			dietaryStatisticsService = ContextFactory.getBean("com.glaf.heathcare.service.dietaryStatisticsService");
		}
		return dietaryStatisticsService;
	}

	public FoodCompositionService getFoodCompositionService() {
		if (foodCompositionService == null) {
			foodCompositionService = ContextFactory.getBean("com.glaf.heathcare.service.foodCompositionService");
		}
		return foodCompositionService;
	}

	public GoodsActualQuantityService getGoodsActualQuantityService() {
		if (goodsActualQuantityService == null) {
			goodsActualQuantityService = ContextFactory
					.getBean("com.glaf.heathcare.service.goodsActualQuantityService");
		}
		return goodsActualQuantityService;
	}

	public int getSize(List<DietaryRptModel> list) {
		int total = 0;
		for (DietaryRptModel rptModel : list) {
			if (rptModel.getItems() != null && rptModel.getItems().size() > 0) {
				total = total + rptModel.getItems().size();
			}
		}
		return total;
	}

	public TenantConfigService getTenantConfigService() {
		if (tenantConfigService == null) {
			tenantConfigService = ContextFactory.getBean("tenantConfigService");
		}
		return tenantConfigService;
	}

	public void setDietaryStatisticsService(DietaryStatisticsService dietaryStatisticsService) {
		this.dietaryStatisticsService = dietaryStatisticsService;
	}

	public void setGoodsActualQuantityService(GoodsActualQuantityService goodsActualQuantityService) {
		this.goodsActualQuantityService = goodsActualQuantityService;
	}

	public void setTenantConfigService(TenantConfigService tenantConfigService) {
		this.tenantConfigService = tenantConfigService;
	}

}
