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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.glaf.base.modules.sys.service.TenantConfigService;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.util.DateUtils;

import com.glaf.heathcare.domain.DietaryItem;
import com.glaf.heathcare.domain.DietaryRptModel;
import com.glaf.heathcare.domain.DietaryStatistics;
import com.glaf.heathcare.domain.DietaryCount;
import com.glaf.heathcare.domain.DietaryDayRptModel;
import com.glaf.heathcare.domain.Dietary;
import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.query.DietaryItemQuery;
import com.glaf.heathcare.query.DietaryQuery;
import com.glaf.heathcare.query.FoodCompositionQuery;
import com.glaf.heathcare.service.DietaryItemService;
import com.glaf.heathcare.service.DietaryStatisticsService;
import com.glaf.heathcare.service.DietaryService;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.heathcare.service.FoodDRIPercentService;
import com.glaf.heathcare.service.FoodDRIService;

public class DayDietaryStatisticsBean {

	protected TenantConfigService tenantConfigService;

	protected DietaryItemService dietaryItemService;

	protected DietaryService dietaryService;

	protected DietaryStatisticsService dietaryStatisticsService;

	protected FoodCompositionService foodCompositionService;

	protected FoodDRIService foodDRIService;

	protected FoodDRIPercentService foodDRIPercentService;

	public void execute(String tenantId, int fullDay) {
		Map<String, Object> context = new HashMap<String, Object>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(DateUtils.toDate(String.valueOf(fullDay)));

		String sysFlag = "N";
		int suitNo = fullDay;
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

		DietaryQuery query = new DietaryQuery();
		query.tenantId(tenantId);
		query.fullDay(fullDay);

		List<Dietary> list = getDietaryService().list(query);
		if (list != null && !list.isEmpty()) {
			List<Long> dietaryIds = new ArrayList<Long>();
			for (Dietary dietary : list) {
				dietaryIds.add(dietary.getId());
			}

			FoodCompositionQuery queryx = new FoodCompositionQuery();
			List<FoodComposition> foods = getFoodCompositionService().list(queryx);
			Map<Long, Long> foodMap = new HashMap<Long, Long>();
			Map<Long, FoodComposition> foodXMap = new HashMap<Long, FoodComposition>();
			for (FoodComposition food : foods) {
				foodMap.put(food.getId(), food.getNodeId());
				foodXMap.put(food.getId(), food);
			}

			DietaryItemQuery query2 = new DietaryItemQuery();
			query2.tenantId(tenantId);
			query2.dietaryIds(dietaryIds);
			List<DietaryItem> allItems = getDietaryItemService().list(query2);
			if (allItems != null && !allItems.isEmpty()) {

				FoodComposition food = null;
				DietaryRptModel model = null;
				List<DietaryItem> items = null;

				Map<Long, List<DietaryItem>> dietaryMap = new HashMap<Long, List<DietaryItem>>();
				for (DietaryItem item : allItems) {
					items = dietaryMap.get(item.getDietaryId());
					if (items == null) {
						items = new ArrayList<DietaryItem>();
					}
					items.add(item);
					dietaryMap.put(item.getDietaryId(), items);
				}

				List<DietaryRptModel> breakfastList = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> breakfastMidList = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> lunchList = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> snackList = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> dinnerList = new ArrayList<DietaryRptModel>();

				double carbohydrateTotal = 0.0;
				double heatEnergyTotal = 0.0;
				double calciumTotal = 0.0;
				double proteinTotal = 0.0;
				double fatTotal = 0.0;

				double carbohydrateMomingTotal = 0.0;
				double heatEnergyMomingTotal = 0.0;
				double calciumMomingTotal = 0.0;
				double proteinMomingTotal = 0.0;
				double fatMomingTotal = 0.0;

				double carbohydrateNoonTotal = 0.0;
				double heatEnergyNoonTotal = 0.0;
				double calciumNoonTotal = 0.0;
				double proteinNoonTotal = 0.0;
				double fatNoonTotal = 0.0;

				double carbohydrateDinnerTotal = 0.0;
				double heatEnergyDinnerTotal = 0.0;
				double calciumDinnerTotal = 0.0;
				double proteinDinnerTotal = 0.0;
				double fatDinnerTotal = 0.0;

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

				double heatEnergyMoming = 0.0;
				double heatEnergyNoon = 0.0;
				double heatEnergyDinner = 0.0;

				double vitaminAAnimals = 0.0;
				double vitaminATotal = 0.0;

				double ironAnimals = 0.0;
				double ironTotal = 0.0;

				double fatAnimals2 = 0.0;
				double fatTotal2 = 0.0;

				double calciumTotal2 = 0.0;
				double phosphorusTotal2 = 0.0;

				for (Dietary dietary : list) {
					items = dietaryMap.get(dietary.getId());
					if (items != null && !items.isEmpty()) {
						model = new DietaryRptModel();
						model.setDietary(dietary);
						model.setName(dietary.getName());
						model.setItems(items);
						model.setCalcium(dietary.getCalcium());
						model.setCarbohydrate(dietary.getCarbohydrate());
						model.setHeatEnergy(dietary.getHeatEnergy());
						model.setProtein(dietary.getProtein());
						model.setFat(dietary.getFat());

						long typeId = dietary.getTypeId();

						if (typeId == 3302 || typeId == 3312 || typeId == 3402 || typeId == 3412) {
							breakfastList.add(model);
							carbohydrateMomingTotal = carbohydrateMomingTotal + dietary.getCarbohydrate();
							heatEnergyMomingTotal = heatEnergyMomingTotal + dietary.getHeatEnergy();
							calciumMomingTotal = calciumMomingTotal + dietary.getCalcium();
							proteinMomingTotal = proteinMomingTotal + dietary.getProtein();
							fatMomingTotal = fatMomingTotal + dietary.getFat();
							heatEnergyMoming = heatEnergyMoming + dietary.getHeatEnergy();
						}
						if (typeId == 3403 || typeId == 3413) {
							breakfastMidList.add(model);
							carbohydrateMomingTotal = carbohydrateMomingTotal + dietary.getCarbohydrate();
							heatEnergyMomingTotal = heatEnergyMomingTotal + dietary.getHeatEnergy();
							calciumMomingTotal = calciumMomingTotal + dietary.getCalcium();
							proteinMomingTotal = proteinMomingTotal + dietary.getProtein();
							fatMomingTotal = fatMomingTotal + dietary.getFat();
							heatEnergyMoming = heatEnergyMoming + dietary.getHeatEnergy();
						}
						if (typeId == 3303 || typeId == 3313 || typeId == 3323 || typeId == 3313 || typeId == 3404
								|| typeId == 3414) {
							lunchList.add(model);
							carbohydrateNoonTotal = carbohydrateNoonTotal + dietary.getCarbohydrate();
							heatEnergyNoonTotal = heatEnergyNoonTotal + dietary.getHeatEnergy();
							calciumNoonTotal = calciumNoonTotal + dietary.getCalcium();
							proteinNoonTotal = proteinNoonTotal + dietary.getProtein();
							fatNoonTotal = fatNoonTotal + dietary.getFat();
							heatEnergyNoon = heatEnergyNoon + dietary.getHeatEnergy();
						}
						if (typeId == 3304 || typeId == 3314 || typeId == 3324 || typeId == 3405 || typeId == 3415) {
							snackList.add(model);
							carbohydrateNoonTotal = carbohydrateNoonTotal + dietary.getCarbohydrate();
							heatEnergyNoonTotal = heatEnergyNoonTotal + dietary.getHeatEnergy();
							calciumNoonTotal = calciumNoonTotal + dietary.getCalcium();
							proteinNoonTotal = proteinNoonTotal + dietary.getProtein();
							fatNoonTotal = fatNoonTotal + dietary.getFat();
							heatEnergyNoon = heatEnergyNoon + dietary.getHeatEnergy();
						}
						if (typeId == 3305 || typeId == 3406) {
							dinnerList.add(model);
							carbohydrateDinnerTotal = carbohydrateDinnerTotal + dietary.getCarbohydrate();
							heatEnergyDinnerTotal = heatEnergyDinnerTotal + dietary.getHeatEnergy();
							calciumDinnerTotal = calciumDinnerTotal + dietary.getCalcium();
							proteinDinnerTotal = proteinDinnerTotal + dietary.getProtein();
							fatDinnerTotal = fatDinnerTotal + dietary.getFat();
							heatEnergyDinner = heatEnergyDinner + dietary.getHeatEnergy();
						}

						carbohydrateTotal = carbohydrateTotal + dietary.getCarbohydrate();
						heatEnergyTotal = heatEnergyTotal + dietary.getHeatEnergy();
						calciumTotal = calciumTotal + dietary.getCalcium();
						proteinTotal = proteinTotal + dietary.getProtein();
						fatTotal = fatTotal + dietary.getFat();

						for (DietaryItem item : items) {
							Long nodeId = foodMap.get(item.getFoodId());
							food = foodXMap.get(item.getFoodId());
							if (nodeId != null && food != null) {
								vitaminATotal = vitaminATotal + food.getVitaminA() * item.getQuantity() / 100.0D;
								ironTotal = ironTotal + food.getIron() * item.getQuantity() / 100.0D;
								calciumTotal2 = calciumTotal2 + food.getCalcium() * item.getQuantity() / 100.0D;
								phosphorusTotal2 = phosphorusTotal2
										+ food.getPhosphorus() * item.getQuantity() / 100.0D;
								fatTotal2 = fatTotal2 + food.getFat() * item.getQuantity() / 100.0D;
								switch (nodeId.intValue()) {
								case 4402:// 谷类
									heatEnergyGrain = heatEnergyGrain
											+ food.getHeatEnergy() * item.getQuantity() / 100.0D;
									proteinOthers = proteinOthers + food.getProtein();
									heatEnergyPlant = heatEnergyPlant
											+ food.getHeatEnergy() * item.getQuantity() / 100.0D;
									break;
								case 4404:// 豆类
									heatEnergyBeans = heatEnergyBeans
											+ food.getHeatEnergy() * item.getQuantity() / 100.0D;
									proteinBeans = proteinBeans + food.getProtein();
									heatEnergyPlant = heatEnergyPlant
											+ food.getHeatEnergy() * item.getQuantity() / 100.0D;
									break;
								case 4409:// 动物类
								case 4410:
								case 4411:
								case 4412:
								case 4413:
									heatEnergyAnimals = heatEnergyAnimals
											+ food.getHeatEnergy() * item.getQuantity() / 100.0D;
									proteinAnimals = proteinAnimals + food.getProtein();
									heatEnergyAnimal = heatEnergyAnimal
											+ food.getHeatEnergy() * item.getQuantity() / 100.0D;
									vitaminAAnimals = vitaminAAnimals
											+ food.getVitaminA() * item.getQuantity() / 100.0D;
									ironAnimals = ironAnimals + food.getIron() * item.getQuantity() / 100.0D;
									fatAnimals2 = fatAnimals2 + food.getFat() * item.getQuantity() / 100.0D;
									break;
								default:// 其他
									heatEnergyOthers = heatEnergyOthers
											+ food.getHeatEnergy() * item.getQuantity() / 100.0D;
									proteinOthers = proteinOthers + food.getProtein();
									heatEnergyPlant = heatEnergyPlant
											+ food.getHeatEnergy() * item.getQuantity() / 100.0D;
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
									heatEnergyProtein = heatEnergyProtein
											+ food.getProtein() * item.getQuantity() / 100D * 4;
								}
							}
						}
					}
				}

				if (breakfastList.size() > 0) {
					double carbohydrate = 0.0;
					double heatEnergy = 0.0;
					double calcium = 0.0;
					double protein = 0.0;
					double fat = 0.0;

					for (DietaryRptModel rptModel : breakfastList) {
						Dietary dietary = rptModel.getDietary();
						carbohydrate = carbohydrate + dietary.getCarbohydrate();
						heatEnergy = heatEnergy + dietary.getHeatEnergy();
						calcium = calcium + dietary.getCalcium();
						protein = protein + dietary.getProtein();
						fat = fat + dietary.getFat();
					}

					DietaryCount cnt = new DietaryCount();
					cnt.setCalcium(calcium);
					cnt.setCarbohydrate(carbohydrate);
					cnt.setHeatEnergy(heatEnergy);
					cnt.setProtein(protein);
					cnt.setFat(fat);

					DietaryCount cntPercent = new DietaryCount();

					if (calcium > 0) {
						cntPercent.setCalcium(calcium / calciumTotal * 100D);
					}

					if (carbohydrateTotal > 0) {
						cntPercent.setCarbohydrate(carbohydrate / carbohydrateTotal * 100D);
					}

					if (fatTotal > 0) {
						cntPercent.setFat(fat / fatTotal * 100D);
					}

					if (heatEnergy > 0) {
						cntPercent.setHeatEnergy(heatEnergy / heatEnergyTotal * 100D);
					}

					if (proteinTotal > 0) {
						cntPercent.setProtein(protein / proteinTotal * 100D);
					}

					context.put("dietaryCount_breakfast", cnt);
					context.put("dietaryCountPercent_breakfast", cntPercent);

					context.put("breakfastList", breakfastList);
				}

				if (breakfastMidList.size() > 0) {
					double carbohydrate = 0.0;
					double heatEnergy = 0.0;
					double calcium = 0.0;
					double protein = 0.0;
					double fat = 0.0;

					for (DietaryRptModel rptModel : breakfastMidList) {
						Dietary dietary = rptModel.getDietary();
						carbohydrate = carbohydrate + dietary.getCarbohydrate();
						heatEnergy = heatEnergy + dietary.getHeatEnergy();
						calcium = calcium + dietary.getCalcium();
						protein = protein + dietary.getProtein();
						fat = fat + dietary.getFat();
					}

					DietaryCount cnt = new DietaryCount();
					cnt.setCalcium(calcium);
					cnt.setCarbohydrate(carbohydrate);
					cnt.setHeatEnergy(heatEnergy);
					cnt.setProtein(protein);
					cnt.setFat(fat);

					DietaryCount cntPercent = new DietaryCount();

					if (calcium > 0) {
						cntPercent.setCalcium(calcium / calciumTotal * 100D);
					}

					if (carbohydrateTotal > 0) {
						cntPercent.setCarbohydrate(carbohydrate / carbohydrateTotal * 100D);
					}

					if (fatTotal > 0) {
						cntPercent.setFat(fat / fatTotal * 100D);
					}

					if (heatEnergy > 0) {
						cntPercent.setHeatEnergy(heatEnergy / heatEnergyTotal * 100D);
					}

					if (proteinTotal > 0) {
						cntPercent.setProtein(protein / proteinTotal * 100D);
					}

					context.put("dietaryCount_breakfastMid", cnt);
					context.put("dietaryCountPercent_breakfastMid", cntPercent);

					context.put("breakfastMidList", breakfastMidList);
				}

				if (lunchList.size() > 0) {
					double carbohydrate = 0.0;
					double heatEnergy = 0.0;
					double calcium = 0.0;
					double protein = 0.0;
					double fat = 0.0;

					for (DietaryRptModel rptModel : lunchList) {
						Dietary dietary = rptModel.getDietary();
						carbohydrate = carbohydrate + dietary.getCarbohydrate();
						heatEnergy = heatEnergy + dietary.getHeatEnergy();
						calcium = calcium + dietary.getCalcium();
						protein = protein + dietary.getProtein();
						fat = fat + dietary.getFat();
					}

					DietaryCount cnt = new DietaryCount();
					cnt.setCalcium(calcium);
					cnt.setCarbohydrate(carbohydrate);
					cnt.setHeatEnergy(heatEnergy);
					cnt.setProtein(protein);
					cnt.setFat(fat);

					DietaryCount cntPercent = new DietaryCount();

					if (calcium > 0) {
						cntPercent.setCalcium(calcium / calciumTotal * 100D);
					}

					if (carbohydrateTotal > 0) {
						cntPercent.setCarbohydrate(carbohydrate / carbohydrateTotal * 100D);
					}

					if (fatTotal > 0) {
						cntPercent.setFat(fat / fatTotal * 100D);
					}

					if (heatEnergy > 0) {
						cntPercent.setHeatEnergy(heatEnergy / heatEnergyTotal * 100D);
					}

					if (proteinTotal > 0) {
						cntPercent.setProtein(protein / proteinTotal * 100D);
					}

					context.put("dietaryCount_lunch", cnt);
					context.put("dietaryCountPercent_lunch", cntPercent);

					context.put("lunchList", lunchList);
				}

				if (snackList.size() > 0) {
					double carbohydrate = 0.0;
					double heatEnergy = 0.0;
					double calcium = 0.0;
					double protein = 0.0;
					double fat = 0.0;

					for (DietaryRptModel rptModel : snackList) {
						Dietary dietary = rptModel.getDietary();
						carbohydrate = carbohydrate + dietary.getCarbohydrate();
						heatEnergy = heatEnergy + dietary.getHeatEnergy();
						calcium = calcium + dietary.getCalcium();
						protein = protein + dietary.getProtein();
						fat = fat + dietary.getFat();
					}

					DietaryCount cnt = new DietaryCount();
					cnt.setCalcium(calcium);
					cnt.setCarbohydrate(carbohydrate);
					cnt.setHeatEnergy(heatEnergy);
					cnt.setProtein(protein);
					cnt.setFat(fat);

					DietaryCount cntPercent = new DietaryCount();

					if (calcium > 0) {
						cntPercent.setCalcium(calcium / calciumTotal * 100D);
					}

					if (carbohydrateTotal > 0) {
						cntPercent.setCarbohydrate(carbohydrate / carbohydrateTotal * 100D);
					}

					if (fatTotal > 0) {
						cntPercent.setFat(fat / fatTotal * 100D);
					}

					if (heatEnergy > 0) {
						cntPercent.setHeatEnergy(heatEnergy / heatEnergyTotal * 100D);
					}

					if (proteinTotal > 0) {
						cntPercent.setProtein(protein / proteinTotal * 100D);
					}

					context.put("dietaryCount_snack", cnt);
					context.put("dietaryCountPercent_snack", cntPercent);

					context.put("snackList", snackList);
				}

				if (dinnerList.size() > 0) {
					double carbohydrate = 0.0;
					double heatEnergy = 0.0;
					double calcium = 0.0;
					double protein = 0.0;
					double fat = 0.0;

					for (DietaryRptModel rptModel : dinnerList) {
						Dietary dietary = rptModel.getDietary();
						carbohydrate = carbohydrate + dietary.getCarbohydrate();
						heatEnergy = heatEnergy + dietary.getHeatEnergy();
						calcium = calcium + dietary.getCalcium();
						protein = protein + dietary.getProtein();
						fat = fat + dietary.getFat();
					}

					DietaryCount cnt = new DietaryCount();
					cnt.setCalcium(calcium);
					cnt.setCarbohydrate(carbohydrate);
					cnt.setHeatEnergy(heatEnergy);
					cnt.setProtein(protein);
					cnt.setFat(fat);

					DietaryCount cntPercent = new DietaryCount();

					if (calcium > 0) {
						cntPercent.setCalcium(calcium / calciumTotal * 100D);
					}

					if (carbohydrateTotal > 0) {
						cntPercent.setCarbohydrate(carbohydrate / carbohydrateTotal * 100D);
					}

					if (fatTotal > 0) {
						cntPercent.setFat(fat / fatTotal * 100D);
					}

					if (heatEnergy > 0) {
						cntPercent.setHeatEnergy(heatEnergy / heatEnergyTotal * 100D);
					}

					if (proteinTotal > 0) {
						cntPercent.setProtein(protein / proteinTotal * 100D);
					}

					context.put("dietaryCount_dinner", cnt);
					context.put("dietaryCountPercent_dinner", cntPercent);

					context.put("dinnerList", dinnerList);
				}

				DietaryCount dietaryCountSum = new DietaryCount();
				dietaryCountSum.setCalcium(calciumTotal);
				dietaryCountSum.setCarbohydrate(carbohydrateTotal);
				dietaryCountSum.setFat(fatTotal);
				dietaryCountSum.setProtein(proteinTotal);
				dietaryCountSum.setHeatEnergy(heatEnergyTotal);

				DietaryDayRptModel dayRptModel = new DietaryDayRptModel();
				dayRptModel.setBreakfastList(breakfastList);
				dayRptModel.setBreakfastMidList(breakfastMidList);
				dayRptModel.setLunchList(lunchList);
				dayRptModel.setSnackList(snackList);
				dayRptModel.setDinnerList(dinnerList);

				context.put("dayRptModel", dayRptModel);
				context.put("dietaryCountSum", dietaryCountSum);

				if (heatEnergyMomingTotal > 0) {
					DietaryCount momingTotal = new DietaryCount();
					momingTotal.setCalcium(calciumMomingTotal);
					momingTotal.setCarbohydrate(carbohydrateMomingTotal);
					momingTotal.setHeatEnergy(heatEnergyMomingTotal);
					momingTotal.setProtein(proteinMomingTotal);
					momingTotal.setFat(fatMomingTotal);

					context.put("momingTotal", momingTotal);

					DietaryCount momingTotalPercent = new DietaryCount();

					if (calciumMomingTotal > 0) {
						momingTotalPercent.setCalcium(calciumMomingTotal / calciumTotal * 100D);
					}

					if (carbohydrateTotal > 0) {
						momingTotalPercent.setCarbohydrate(carbohydrateMomingTotal / carbohydrateTotal * 100D);
					}

					if (fatTotal > 0) {
						momingTotalPercent.setFat(fatMomingTotal / fatTotal * 100D);
					}

					if (heatEnergyMomingTotal > 0) {
						momingTotalPercent.setHeatEnergy(heatEnergyMomingTotal / heatEnergyTotal * 100D);
					}

					if (proteinTotal > 0) {
						momingTotalPercent.setProtein(proteinMomingTotal / proteinTotal * 100D);
					}

					context.put("momingTotalPercent", momingTotalPercent);
				}

				if (heatEnergyNoonTotal > 0) {
					DietaryCount noonTotal = new DietaryCount();
					noonTotal.setCalcium(calciumNoonTotal);
					noonTotal.setCarbohydrate(carbohydrateNoonTotal);
					noonTotal.setHeatEnergy(heatEnergyNoonTotal);
					noonTotal.setProtein(proteinNoonTotal);
					noonTotal.setFat(fatNoonTotal);

					context.put("noonTotal", noonTotal);

					DietaryCount noonTotalPercent = new DietaryCount();

					if (calciumNoonTotal > 0) {
						noonTotalPercent.setCalcium(calciumNoonTotal / calciumTotal * 100D);
					}

					if (carbohydrateTotal > 0) {
						noonTotalPercent.setCarbohydrate(carbohydrateNoonTotal / carbohydrateTotal * 100D);
					}

					if (fatTotal > 0) {
						noonTotalPercent.setFat(fatNoonTotal / fatTotal * 100D);
					}

					if (heatEnergyNoonTotal > 0) {
						noonTotalPercent.setHeatEnergy(heatEnergyNoonTotal / heatEnergyTotal * 100D);
					}

					if (proteinTotal > 0) {
						noonTotalPercent.setProtein(proteinNoonTotal / proteinTotal * 100D);
					}

					context.put("noonTotalPercent", noonTotalPercent);
				}

				List<DietaryStatistics> rows = new ArrayList<DietaryStatistics>();

				double tt = heatEnergyGrain + heatEnergyBeans + heatEnergyAnimals + heatEnergyOthers;
				double pp = proteinBeans + proteinAnimals + proteinOthers;

				DietaryStatistics m1 = new DietaryStatistics();
				m1.setTenantId(query.getTenantId());
				m1.setSysFlag(sysFlag);
				m1.setSuitNo(suitNo);
				m1.setDayOfWeek(dayOfWeek);
				m1.setType("heatEnergyPerDayDietary");
				m1.setTitle("谷类");
				m1.setName("heatEnergyGrain");
				m1.setValue(Math.round(heatEnergyGrain));
				m1.setSortNo(4);
				rows.add(m1);

				DietaryStatistics m11 = new DietaryStatistics();
				m11.setTenantId(query.getTenantId());
				m11.setSysFlag(sysFlag);
				m11.setSuitNo(suitNo);
				m11.setDayOfWeek(dayOfWeek);
				m11.setType("heatEnergyPercentPerDayDietary");
				m11.setTitle("谷类");
				m11.setName("heatEnergyGrainPercent");
				m11.setValue(Math.round(heatEnergyGrain / tt * 100D));
				m11.setSortNo(4);
				rows.add(m11);

				DietaryStatistics m2 = new DietaryStatistics();
				m2.setTenantId(query.getTenantId());
				m2.setSysFlag(sysFlag);
				m2.setSuitNo(suitNo);
				m2.setDayOfWeek(dayOfWeek);
				m2.setType("heatEnergyPerDayDietary");
				m2.setTitle("豆类");
				m2.setName("heatEnergyBeans");
				m2.setValue(Math.round(heatEnergyBeans));
				m2.setSortNo(1);
				rows.add(m2);

				DietaryStatistics m22 = new DietaryStatistics();
				m22.setTenantId(query.getTenantId());
				m22.setSysFlag(sysFlag);
				m22.setSuitNo(suitNo);
				m22.setDayOfWeek(dayOfWeek);
				m22.setType("heatEnergyPercentPerDayDietary");
				m22.setTitle("豆类");
				m22.setName("heatEnergyBeansPercent");
				m22.setValue(Math.round(heatEnergyBeans / tt * 100D));
				m22.setSortNo(1);
				rows.add(m22);

				DietaryStatistics m3 = new DietaryStatistics();
				m3.setTenantId(query.getTenantId());
				m3.setSysFlag(sysFlag);
				m3.setSuitNo(suitNo);
				m3.setDayOfWeek(dayOfWeek);
				m3.setType("heatEnergyPerDayDietary");
				m3.setTitle("动物类食物");
				m3.setName("heatEnergyAnimals");
				m3.setValue(Math.round(heatEnergyAnimals));
				m3.setSortNo(2);
				rows.add(m3);

				DietaryStatistics m33 = new DietaryStatistics();
				m33.setTenantId(query.getTenantId());
				m33.setSysFlag(sysFlag);
				m33.setSuitNo(suitNo);
				m33.setDayOfWeek(dayOfWeek);
				m33.setType("heatEnergyPercentPerDayDietary");
				m33.setTitle("动物类食物");
				m33.setName("heatEnergyAnimalsPercent");
				m33.setValue(Math.round(heatEnergyAnimals / tt * 100D));
				m33.setSortNo(2);
				rows.add(m33);

				DietaryStatistics m4 = new DietaryStatistics();
				m4.setTenantId(query.getTenantId());
				m4.setSysFlag(sysFlag);
				m4.setSuitNo(suitNo);
				m4.setDayOfWeek(dayOfWeek);
				m4.setType("heatEnergyPerDayDietary");
				m4.setTitle("谷、薯、杂豆及其他");
				m4.setName("heatEnergyOthers");
				m4.setValue(Math.round(heatEnergyOthers));
				m4.setSortNo(3);
				rows.add(m4);

				DietaryStatistics m44 = new DietaryStatistics();
				m44.setTenantId(query.getTenantId());
				m44.setSysFlag(sysFlag);
				m44.setSuitNo(suitNo);
				m44.setDayOfWeek(dayOfWeek);
				m44.setType("heatEnergyPercentPerDayDietary");
				m44.setTitle("谷、薯、杂豆及其他");
				m44.setName("heatEnergyOthersPercent");
				m44.setValue(Math.round(heatEnergyOthers / tt * 100D));
				m44.setSortNo(3);
				rows.add(m44);

				DietaryStatistics mp2 = new DietaryStatistics();
				mp2.setTenantId(query.getTenantId());
				mp2.setSysFlag(sysFlag);
				mp2.setSuitNo(suitNo);
				mp2.setDayOfWeek(dayOfWeek);
				mp2.setType("proteinPerDietary");
				mp2.setTitle("豆类");
				mp2.setName("proteinBeans");
				mp2.setValue(Math.round(proteinBeans));
				mp2.setSortNo(1);
				rows.add(mp2);

				DietaryStatistics mp22 = new DietaryStatistics();
				mp22.setTenantId(query.getTenantId());
				mp22.setSysFlag(sysFlag);
				mp22.setSuitNo(suitNo);
				mp22.setDayOfWeek(dayOfWeek);
				mp22.setType("proteinPercentPerDayDietary");
				mp22.setTitle("豆类");
				mp22.setName("proteinBeansPercent");
				mp22.setValue(Math.round(proteinBeans / pp * 100D));
				mp22.setSortNo(1);
				rows.add(mp22);

				DietaryStatistics mp3 = new DietaryStatistics();
				mp3.setTenantId(query.getTenantId());
				mp3.setSysFlag(sysFlag);
				mp3.setSuitNo(suitNo);
				mp3.setDayOfWeek(dayOfWeek);
				mp3.setType("proteinPerDayDietary");
				mp3.setTitle("动物类食物");
				mp3.setName("proteinAnimals");
				mp3.setValue(Math.round(proteinAnimals));
				mp3.setSortNo(2);
				rows.add(mp3);

				DietaryStatistics mp33 = new DietaryStatistics();
				mp33.setTenantId(query.getTenantId());
				mp33.setSysFlag(sysFlag);
				mp33.setSuitNo(suitNo);
				mp33.setDayOfWeek(dayOfWeek);
				mp33.setType("proteinPercentPerDayDietary");
				mp33.setTitle("动物类食物");
				mp33.setName("proteinAnimalsPercent");
				mp33.setValue(Math.round(proteinAnimals / pp * 100D));
				mp33.setSortNo(2);
				rows.add(mp33);

				DietaryStatistics mp4 = new DietaryStatistics();
				mp4.setTenantId(query.getTenantId());
				mp4.setSysFlag(sysFlag);
				mp4.setSuitNo(suitNo);
				mp4.setDayOfWeek(dayOfWeek);
				mp4.setType("proteinPerDayDietary");
				mp4.setTitle("谷、薯、杂豆及其他");
				mp4.setName("proteinOthers");
				mp4.setValue(Math.round(proteinOthers));
				mp4.setSortNo(3);
				rows.add(mp4);

				DietaryStatistics mp44 = new DietaryStatistics();
				mp44.setTenantId(query.getTenantId());
				mp44.setSysFlag(sysFlag);
				mp44.setSuitNo(suitNo);
				mp44.setDayOfWeek(dayOfWeek);
				mp44.setType("proteinPercentPerDayDietary");
				mp44.setTitle("谷、薯、杂豆及其他");
				mp44.setName("proteinOthersPercent");
				mp44.setValue(Math.round(proteinOthers / pp * 100D));
				mp44.setSortNo(3);
				rows.add(mp44);

				tt = heatEnergyPlant + heatEnergyAnimal;

				if (tt > 0) {
					DietaryStatistics mx1 = new DietaryStatistics();
					mx1.setTenantId(query.getTenantId());
					mx1.setSysFlag(sysFlag);
					mx1.setSuitNo(suitNo);
					mx1.setDayOfWeek(dayOfWeek);
					mx1.setType("heatEnergyX1PerDayDietary");
					mx1.setTitle("动物性食物");
					mx1.setName("heatEnergyAnimal");
					mx1.setValue(Math.round(heatEnergyAnimal));
					mx1.setSortNo(1);
					rows.add(mx1);

					DietaryStatistics mx11 = new DietaryStatistics();
					mx11.setTenantId(query.getTenantId());
					mx11.setSysFlag(sysFlag);
					mx11.setSuitNo(suitNo);
					mx11.setDayOfWeek(dayOfWeek);
					mx11.setType("heatEnergyX1PercentPerDayDietary");
					mx11.setTitle("动物性食物");
					mx11.setName("heatEnergyAnimalPercent");
					mx11.setValue(Math.round(heatEnergyAnimal / tt * 100D));
					mx11.setSortNo(1);
					rows.add(mx11);

					DietaryStatistics mx2 = new DietaryStatistics();
					mx2.setTenantId(query.getTenantId());
					mx2.setSysFlag(sysFlag);
					mx2.setSuitNo(suitNo);
					mx2.setDayOfWeek(dayOfWeek);
					mx2.setType("heatEnergyX1PerDayDietary");
					mx2.setTitle("植物性食物");
					mx2.setName("heatEnergyPlant");
					mx2.setValue(Math.round(heatEnergyPlant));
					mx2.setSortNo(2);
					rows.add(mx2);

					DietaryStatistics mx22 = new DietaryStatistics();
					mx22.setTenantId(query.getTenantId());
					mx22.setSysFlag(sysFlag);
					mx22.setSuitNo(suitNo);
					mx22.setDayOfWeek(dayOfWeek);
					mx22.setType("heatEnergyX1PercentPerDayDietary");
					mx22.setTitle("植物性食物");
					mx22.setName("heatEnergyPlantPercent");
					mx22.setValue(Math.round(heatEnergyPlant / tt * 100D));
					mx22.setSortNo(2);
					rows.add(mx22);

				}

				tt = heatEnergyFat + heatEnergyProtein + heatEnergyCarbohydrate;
				if (tt > 0) {
					DietaryStatistics mx1 = new DietaryStatistics();
					mx1.setTenantId(query.getTenantId());
					mx1.setSysFlag(sysFlag);
					mx1.setSuitNo(suitNo);
					mx1.setDayOfWeek(dayOfWeek);
					mx1.setType("heatEnergyX2PerDayDietary");
					mx1.setTitle("脂肪");
					mx1.setName("heatEnergyFat");
					mx1.setValue(Math.round(heatEnergyFat));
					mx1.setSortNo(2);
					rows.add(mx1);

					DietaryStatistics mx2 = new DietaryStatistics();
					mx2.setTenantId(query.getTenantId());
					mx2.setSysFlag(sysFlag);
					mx2.setSuitNo(suitNo);
					mx2.setDayOfWeek(dayOfWeek);
					mx2.setType("heatEnergyX2PerDayDietary");
					mx2.setTitle("蛋白质");
					mx2.setName("heatEnergyProtein");
					mx2.setValue(Math.round(heatEnergyProtein));
					mx2.setSortNo(1);
					rows.add(mx2);

					DietaryStatistics mx3 = new DietaryStatistics();
					mx3.setTenantId(query.getTenantId());
					mx3.setSysFlag(sysFlag);
					mx3.setSuitNo(suitNo);
					mx3.setDayOfWeek(dayOfWeek);
					mx3.setType("heatEnergyX2PerDayDietary");
					mx3.setTitle("碳水化合物");
					mx3.setName("heatEnergyCarbohydrate");
					mx3.setValue(Math.round(heatEnergyCarbohydrate));
					mx3.setSortNo(3);
					rows.add(mx3);

					DietaryStatistics mx11 = new DietaryStatistics();
					mx11.setTenantId(query.getTenantId());
					mx11.setSysFlag(sysFlag);
					mx11.setSuitNo(suitNo);
					mx11.setDayOfWeek(dayOfWeek);
					mx11.setType("heatEnergyX2PercentPerDayDietary");
					mx11.setTitle("脂肪");
					mx11.setName("heatEnergyFatPercent");
					mx11.setValue(Math.round(heatEnergyFat / tt * 100D));
					mx11.setSortNo(2);
					rows.add(mx11);

					DietaryStatistics mx12 = new DietaryStatistics();
					mx12.setTenantId(query.getTenantId());
					mx12.setSysFlag(sysFlag);
					mx12.setSuitNo(suitNo);
					mx12.setDayOfWeek(dayOfWeek);
					mx12.setType("heatEnergyX2PercentPerDayDietary");
					mx12.setTitle("蛋白质");
					mx12.setName("heatEnergyProteinPercent");
					mx12.setValue(Math.round(heatEnergyProtein / tt * 100D));
					mx12.setSortNo(1);
					rows.add(mx12);

					DietaryStatistics mx13 = new DietaryStatistics();
					mx13.setTenantId(query.getTenantId());
					mx13.setSysFlag(sysFlag);
					mx13.setSuitNo(suitNo);
					mx13.setDayOfWeek(dayOfWeek);
					mx13.setType("heatEnergyX2PercentPerDayDietary");
					mx13.setTitle("碳水化合物");
					mx13.setName("heatEnergyCarbohydratePercent");
					mx13.setValue(Math.round(heatEnergyCarbohydrate / tt * 100D));
					mx13.setSortNo(3);
					rows.add(mx13);

				}

				tt = heatEnergyMoming + heatEnergyNoon + heatEnergyDinner;
				if (heatEnergyMoming > 0 && heatEnergyNoon > 0) {
					DietaryStatistics nx1 = new DietaryStatistics();
					nx1.setTenantId(query.getTenantId());
					nx1.setSysFlag(sysFlag);
					nx1.setSuitNo(suitNo);
					nx1.setDayOfWeek(dayOfWeek);
					nx1.setType("heatEnergyX3PerDayDietary");
					nx1.setTitle("早餐");
					nx1.setName("heatEnergyMoming");
					nx1.setValue(Math.round(heatEnergyMoming));
					nx1.setSortNo(1);
					rows.add(nx1);

					DietaryStatistics nx2 = new DietaryStatistics();
					nx2.setTenantId(query.getTenantId());
					nx2.setSysFlag(sysFlag);
					nx2.setSuitNo(suitNo);
					nx2.setDayOfWeek(dayOfWeek);
					nx2.setType("heatEnergyX3PerDayDietary");
					nx2.setTitle("中餐");
					nx2.setName("heatEnergyNoon");
					nx2.setValue(Math.round(heatEnergyNoon));
					nx2.setSortNo(2);
					rows.add(nx2);

					DietaryStatistics nx3 = new DietaryStatistics();
					nx3.setTenantId(query.getTenantId());
					nx3.setSysFlag(sysFlag);
					nx3.setSuitNo(suitNo);
					nx3.setDayOfWeek(dayOfWeek);
					nx3.setType("heatEnergyX3PerDayDietary");
					nx3.setTitle("晚餐");
					nx3.setName("heatEnergyDinner");
					nx3.setValue(Math.round(heatEnergyDinner));
					nx3.setSortNo(3);
					rows.add(nx3);

					if (heatEnergyDinner > 0) {
						DietaryStatistics nx11 = new DietaryStatistics();
						nx11.setTenantId(query.getTenantId());
						nx11.setSysFlag(sysFlag);
						nx11.setSuitNo(suitNo);
						nx11.setDayOfWeek(dayOfWeek);
						nx11.setType("heatEnergyX3PercentPerDayDietary");
						nx11.setTitle("早餐");
						nx11.setName("heatEnergyMoming");
						nx11.setValue(Math.round(heatEnergyMoming / tt * 100D));
						nx11.setSortNo(1);
						rows.add(nx11);

						DietaryStatistics nx12 = new DietaryStatistics();
						nx12.setTenantId(query.getTenantId());
						nx12.setSysFlag(sysFlag);
						nx12.setSuitNo(suitNo);
						nx12.setDayOfWeek(dayOfWeek);
						nx12.setType("heatEnergyX3PercentPerDayDietary");
						nx12.setTitle("中餐");
						nx12.setName("heatEnergyNoon");
						nx12.setValue(Math.round(heatEnergyNoon / tt * 100D));
						nx12.setSortNo(2);
						rows.add(nx12);

						DietaryStatistics nx13 = new DietaryStatistics();
						nx13.setTenantId(query.getTenantId());
						nx13.setSysFlag(sysFlag);
						nx13.setSuitNo(suitNo);
						nx13.setDayOfWeek(dayOfWeek);
						nx13.setType("heatEnergyX3PercentPerDayDietary");
						nx13.setTitle("晚餐");
						nx13.setName("heatEnergyDinner");
						nx13.setValue(Math.round(heatEnergyDinner / tt * 100D));
						nx13.setSortNo(3);
						rows.add(nx13);
					} else {
						tt = heatEnergyMoming + heatEnergyNoon;

						DietaryStatistics nx11 = new DietaryStatistics();
						nx11.setTenantId(query.getTenantId());
						nx11.setSysFlag(sysFlag);
						nx11.setSuitNo(suitNo);
						nx11.setDayOfWeek(dayOfWeek);
						nx11.setType("heatEnergyX3PercentPerDayDietary");
						nx11.setTitle("早餐");
						nx11.setName("heatEnergyMoming");
						nx11.setValue(Math.round(heatEnergyMoming / tt * 100D * 0.7));
						nx11.setSortNo(1);
						rows.add(nx11);

						DietaryStatistics nx12 = new DietaryStatistics();
						nx12.setTenantId(query.getTenantId());
						nx12.setSysFlag(sysFlag);
						nx12.setSuitNo(suitNo);
						nx12.setDayOfWeek(dayOfWeek);
						nx12.setType("heatEnergyX3PercentPerDayDietary");
						nx12.setTitle("中餐");
						nx12.setName("heatEnergyNoon");
						nx12.setValue(Math.round(heatEnergyNoon / tt * 100D * 0.7));
						nx12.setSortNo(2);
						rows.add(nx12);

						DietaryStatistics nx13 = new DietaryStatistics();
						nx13.setTenantId(query.getTenantId());
						nx13.setSysFlag(sysFlag);
						nx13.setSuitNo(suitNo);
						nx13.setDayOfWeek(dayOfWeek);
						nx13.setType("heatEnergyX3PercentPerDayDietary");
						nx13.setTitle("晚餐");
						nx13.setName("heatEnergyDinner");
						nx13.setValue(30);// 30%
						nx13.setSortNo(3);
						rows.add(nx13);

					}

				}

				if (vitaminATotal > 0) {
					double p1 = vitaminAAnimals / vitaminATotal;
					DietaryStatistics mx15 = new DietaryStatistics();
					mx15.setTenantId(query.getTenantId());
					mx15.setSysFlag(sysFlag);
					mx15.setSuitNo(suitNo);
					mx15.setDayOfWeek(dayOfWeek);
					mx15.setType("vitaminAAnimalsX2PercentPerDayDietary");
					mx15.setTitle("动物性V-A");
					mx15.setName("vitaminAAnimalsPercent");
					mx15.setValue(Math.round(p1 * 100D));
					mx15.setSortNo(3);
					rows.add(mx15);

					DietaryStatistics mx16 = new DietaryStatistics();
					mx16.setTenantId(query.getTenantId());
					mx16.setSysFlag(sysFlag);
					mx16.setSuitNo(suitNo);
					mx16.setDayOfWeek(dayOfWeek);
					mx16.setType("vitaminAAnimalsX2PercentPerDayDietary");
					mx16.setTitle("其他");
					mx16.setName("vitaminAOthersPercent");
					mx16.setValue(100 - mx15.getValue());
					mx16.setSortNo(3);
					rows.add(mx16);
				}

				if (ironTotal > 0) {
					double p2 = ironAnimals / ironTotal;
					DietaryStatistics mx15 = new DietaryStatistics();
					mx15.setTenantId(query.getTenantId());
					mx15.setSysFlag(sysFlag);
					mx15.setSuitNo(suitNo);
					mx15.setDayOfWeek(dayOfWeek);
					mx15.setType("ironAnimalsX2PercentPerDayDietary");
					mx15.setTitle("动物性铁");
					mx15.setName("ironAnimalsPercent");
					mx15.setValue(Math.round(p2 * 100D));
					mx15.setSortNo(3);
					rows.add(mx15);

					DietaryStatistics mx16 = new DietaryStatistics();
					mx16.setTenantId(query.getTenantId());
					mx16.setSysFlag(sysFlag);
					mx16.setSuitNo(suitNo);
					mx16.setDayOfWeek(dayOfWeek);
					mx16.setType("ironAnimalsX2PercentPerDayDietary");
					mx16.setTitle("其他");
					mx16.setName("ironOthersPercent");
					mx16.setValue(100 - mx15.getValue());
					mx16.setSortNo(3);
					rows.add(mx16);
				}

				if (fatTotal2 > 0) {
					double p2 = fatAnimals2 / fatTotal2;
					DietaryStatistics mx15 = new DietaryStatistics();
					mx15.setTenantId(query.getTenantId());
					mx15.setSysFlag(sysFlag);
					mx15.setSuitNo(suitNo);
					mx15.setDayOfWeek(dayOfWeek);
					mx15.setType("fatAnimalsX2PercentPerDayDietary");
					mx15.setTitle("动物性脂肪");
					mx15.setName("fatAnimalsPercent");
					mx15.setValue(Math.round(p2 * 100D));
					mx15.setSortNo(3);
					rows.add(mx15);

					DietaryStatistics mx16 = new DietaryStatistics();
					mx16.setTenantId(query.getTenantId());
					mx16.setSysFlag(sysFlag);
					mx16.setSuitNo(suitNo);
					mx16.setDayOfWeek(dayOfWeek);
					mx16.setType("fatAnimalsX2PercentPerDayDietary");
					mx16.setTitle("其他");
					mx16.setName("fatOthersPercent");
					mx16.setValue(100 - mx15.getValue());
					mx16.setSortNo(3);
					rows.add(mx16);
				}

				if (calciumTotal2 > 0 && phosphorusTotal2 > 0) {
					double p2 = phosphorusTotal2 / (calciumTotal2 + phosphorusTotal2);
					DietaryStatistics mx15 = new DietaryStatistics();
					mx15.setTenantId(query.getTenantId());
					mx15.setSysFlag(sysFlag);
					mx15.setSuitNo(suitNo);
					mx15.setDayOfWeek(dayOfWeek);
					mx15.setType("calciumPhosphorusX2PercentPerDayDietary");
					mx15.setTitle("磷占比");
					mx15.setName("phosphorusPercent");
					mx15.setValue(Math.round(p2 * 100D));
					mx15.setSortNo(3);
					rows.add(mx15);

					DietaryStatistics mx16 = new DietaryStatistics();
					mx16.setTenantId(query.getTenantId());
					mx16.setSysFlag(sysFlag);
					mx16.setSuitNo(suitNo);
					mx16.setDayOfWeek(dayOfWeek);
					mx16.setType("calciumPhosphorusX2PercentPerDayDietary");
					mx16.setTitle("钙占比");
					mx16.setName("calciumPercent");
					mx16.setValue(100 - mx15.getValue());
					mx16.setSortNo(3);
					rows.add(mx16);
				}

				getDietaryStatisticsService().saveAll(query.getTenantId(), sysFlag, suitNo, dayOfWeek, rows);

			}
		}
	}

	public void execute(String tenantId, int year, int semester, int week) {
		Map<String, Object> context = new HashMap<String, Object>();

		String sysFlag = "N";

		DietaryQuery query = new DietaryQuery();
		query.tenantId(tenantId);
		query.year(year);
		query.semester(semester);
		query.week(week);

		List<Dietary> list = getDietaryService().list(query);
		if (list != null && !list.isEmpty()) {
			List<Long> dietaryIds = new ArrayList<Long>();
			for (Dietary dietary : list) {
				dietaryIds.add(dietary.getId());
			}

			FoodCompositionQuery queryx = new FoodCompositionQuery();
			List<FoodComposition> foods = getFoodCompositionService().list(queryx);
			Map<Long, Long> foodMap = new HashMap<Long, Long>();
			Map<Long, FoodComposition> foodXMap = new HashMap<Long, FoodComposition>();
			for (FoodComposition food : foods) {
				foodMap.put(food.getId(), food.getNodeId());
				foodXMap.put(food.getId(), food);
			}

			DietaryItemQuery query2 = new DietaryItemQuery();
			query2.tenantId(tenantId);
			query2.dietaryIds(dietaryIds);
			List<DietaryItem> allItems = getDietaryItemService().list(query2);
			if (allItems != null && !allItems.isEmpty()) {

				FoodComposition food = null;
				DietaryRptModel model = null;
				List<DietaryItem> items = null;

				Map<Long, List<DietaryItem>> dietaryMap = new HashMap<Long, List<DietaryItem>>();
				for (DietaryItem item : allItems) {
					items = dietaryMap.get(item.getDietaryId());
					if (items == null) {
						items = new ArrayList<DietaryItem>();
					}
					items.add(item);
					dietaryMap.put(item.getDietaryId(), items);
				}

				List<DietaryRptModel> breakfastList = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> breakfastMidList = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> lunchList = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> snackList = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> dinnerList = new ArrayList<DietaryRptModel>();

				double carbohydrateTotal = 0.0;
				double heatEnergyTotal = 0.0;
				double calciumTotal = 0.0;
				double proteinTotal = 0.0;
				double fatTotal = 0.0;

				double carbohydrateMomingTotal = 0.0;
				double heatEnergyMomingTotal = 0.0;
				double calciumMomingTotal = 0.0;
				double proteinMomingTotal = 0.0;
				double fatMomingTotal = 0.0;

				double carbohydrateNoonTotal = 0.0;
				double heatEnergyNoonTotal = 0.0;
				double calciumNoonTotal = 0.0;
				double proteinNoonTotal = 0.0;
				double fatNoonTotal = 0.0;

				double carbohydrateDinnerTotal = 0.0;
				double heatEnergyDinnerTotal = 0.0;
				double calciumDinnerTotal = 0.0;
				double proteinDinnerTotal = 0.0;
				double fatDinnerTotal = 0.0;

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

				double heatEnergyMoming = 0.0;
				double heatEnergyNoon = 0.0;
				double heatEnergyDinner = 0.0;

				double vitaminAAnimals = 0.0;
				double vitaminATotal = 0.0;

				double ironAnimals = 0.0;
				double ironTotal = 0.0;

				double fatAnimals2 = 0.0;
				double fatTotal2 = 0.0;

				double calciumTotal2 = 0.0;
				double phosphorusTotal2 = 0.0;

				for (Dietary dietary : list) {
					items = dietaryMap.get(dietary.getId());
					if (items != null && !items.isEmpty()) {
						model = new DietaryRptModel();
						model.setDietary(dietary);
						model.setName(dietary.getName());
						model.setItems(items);
						model.setCalcium(dietary.getCalcium());
						model.setCarbohydrate(dietary.getCarbohydrate());
						model.setHeatEnergy(dietary.getHeatEnergy());
						model.setProtein(dietary.getProtein());
						model.setFat(dietary.getFat());

						long typeId = dietary.getTypeId();

						if (typeId == 3302 || typeId == 3312 || typeId == 3402 || typeId == 3412) {
							breakfastList.add(model);
							carbohydrateMomingTotal = carbohydrateMomingTotal + dietary.getCarbohydrate();
							heatEnergyMomingTotal = heatEnergyMomingTotal + dietary.getHeatEnergy();
							calciumMomingTotal = calciumMomingTotal + dietary.getCalcium();
							proteinMomingTotal = proteinMomingTotal + dietary.getProtein();
							fatMomingTotal = fatMomingTotal + dietary.getFat();
							heatEnergyMoming = heatEnergyMoming + dietary.getHeatEnergy();
						}
						if (typeId == 3403 || typeId == 3413) {
							breakfastMidList.add(model);
							carbohydrateMomingTotal = carbohydrateMomingTotal + dietary.getCarbohydrate();
							heatEnergyMomingTotal = heatEnergyMomingTotal + dietary.getHeatEnergy();
							calciumMomingTotal = calciumMomingTotal + dietary.getCalcium();
							proteinMomingTotal = proteinMomingTotal + dietary.getProtein();
							fatMomingTotal = fatMomingTotal + dietary.getFat();
							heatEnergyMoming = heatEnergyMoming + dietary.getHeatEnergy();
						}
						if (typeId == 3303 || typeId == 3313 || typeId == 3323 || typeId == 3313 || typeId == 3404
								|| typeId == 3414) {
							lunchList.add(model);
							carbohydrateNoonTotal = carbohydrateNoonTotal + dietary.getCarbohydrate();
							heatEnergyNoonTotal = heatEnergyNoonTotal + dietary.getHeatEnergy();
							calciumNoonTotal = calciumNoonTotal + dietary.getCalcium();
							proteinNoonTotal = proteinNoonTotal + dietary.getProtein();
							fatNoonTotal = fatNoonTotal + dietary.getFat();
							heatEnergyNoon = heatEnergyNoon + dietary.getHeatEnergy();
						}
						if (typeId == 3304 || typeId == 3314 || typeId == 3324 || typeId == 3405 || typeId == 3415) {
							snackList.add(model);
							carbohydrateNoonTotal = carbohydrateNoonTotal + dietary.getCarbohydrate();
							heatEnergyNoonTotal = heatEnergyNoonTotal + dietary.getHeatEnergy();
							calciumNoonTotal = calciumNoonTotal + dietary.getCalcium();
							proteinNoonTotal = proteinNoonTotal + dietary.getProtein();
							fatNoonTotal = fatNoonTotal + dietary.getFat();
							heatEnergyNoon = heatEnergyNoon + dietary.getHeatEnergy();
						}
						if (typeId == 3305 || typeId == 3406) {
							dinnerList.add(model);
							carbohydrateDinnerTotal = carbohydrateDinnerTotal + dietary.getCarbohydrate();
							heatEnergyDinnerTotal = heatEnergyDinnerTotal + dietary.getHeatEnergy();
							calciumDinnerTotal = calciumDinnerTotal + dietary.getCalcium();
							proteinDinnerTotal = proteinDinnerTotal + dietary.getProtein();
							fatDinnerTotal = fatDinnerTotal + dietary.getFat();
							heatEnergyDinner = heatEnergyDinner + dietary.getHeatEnergy();
						}

						carbohydrateTotal = carbohydrateTotal + dietary.getCarbohydrate();
						heatEnergyTotal = heatEnergyTotal + dietary.getHeatEnergy();
						calciumTotal = calciumTotal + dietary.getCalcium();
						proteinTotal = proteinTotal + dietary.getProtein();
						fatTotal = fatTotal + dietary.getFat();

						for (DietaryItem item : items) {
							Long nodeId = foodMap.get(item.getFoodId());
							food = foodXMap.get(item.getFoodId());
							if (nodeId != null && food != null) {
								vitaminATotal = vitaminATotal + food.getVitaminA() * item.getQuantity() / 100.0D;
								ironTotal = ironTotal + food.getIron() * item.getQuantity() / 100.0D;
								calciumTotal2 = calciumTotal2 + food.getCalcium() * item.getQuantity() / 100.0D;
								phosphorusTotal2 = phosphorusTotal2
										+ food.getPhosphorus() * item.getQuantity() / 100.0D;
								fatTotal2 = fatTotal2 + food.getFat() * item.getQuantity() / 100.0D;
								switch (nodeId.intValue()) {
								case 4402:// 谷类
									heatEnergyGrain = heatEnergyGrain
											+ food.getHeatEnergy() * item.getQuantity() / 100.0D;
									proteinOthers = proteinOthers + food.getProtein();
									heatEnergyPlant = heatEnergyPlant
											+ food.getHeatEnergy() * item.getQuantity() / 100.0D;
									break;
								case 4404:// 豆类
									heatEnergyBeans = heatEnergyBeans
											+ food.getHeatEnergy() * item.getQuantity() / 100.0D;
									proteinBeans = proteinBeans + food.getProtein();
									heatEnergyPlant = heatEnergyPlant
											+ food.getHeatEnergy() * item.getQuantity() / 100.0D;
									break;
								case 4409:// 动物类
								case 4410:
								case 4411:
								case 4412:
								case 4413:
									heatEnergyAnimals = heatEnergyAnimals
											+ food.getHeatEnergy() * item.getQuantity() / 100.0D;
									proteinAnimals = proteinAnimals + food.getProtein();
									heatEnergyAnimal = heatEnergyAnimal
											+ food.getHeatEnergy() * item.getQuantity() / 100.0D;
									vitaminAAnimals = vitaminAAnimals
											+ food.getVitaminA() * item.getQuantity() / 100.0D;
									ironAnimals = ironAnimals + food.getIron() * item.getQuantity() / 100.0D;
									fatAnimals2 = fatAnimals2 + food.getFat() * item.getQuantity() / 100.0D;
									break;
								default:// 其他
									heatEnergyOthers = heatEnergyOthers
											+ food.getHeatEnergy() * item.getQuantity() / 100.0D;
									proteinOthers = proteinOthers + food.getProtein();
									heatEnergyPlant = heatEnergyPlant
											+ food.getHeatEnergy() * item.getQuantity() / 100.0D;
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
									heatEnergyProtein = heatEnergyProtein
											+ food.getProtein() * item.getQuantity() / 100D * 4;
								}
							}
						}
					}
				}

				if (breakfastList.size() > 0) {
					double carbohydrate = 0.0;
					double heatEnergy = 0.0;
					double calcium = 0.0;
					double protein = 0.0;
					double fat = 0.0;

					for (DietaryRptModel rptModel : breakfastList) {
						Dietary dietary = rptModel.getDietary();
						carbohydrate = carbohydrate + dietary.getCarbohydrate();
						heatEnergy = heatEnergy + dietary.getHeatEnergy();
						calcium = calcium + dietary.getCalcium();
						protein = protein + dietary.getProtein();
						fat = fat + dietary.getFat();
					}

					DietaryCount cnt = new DietaryCount();
					cnt.setCalcium(calcium);
					cnt.setCarbohydrate(carbohydrate);
					cnt.setHeatEnergy(heatEnergy);
					cnt.setProtein(protein);
					cnt.setFat(fat);

					DietaryCount cntPercent = new DietaryCount();

					if (calcium > 0) {
						cntPercent.setCalcium(calcium / calciumTotal * 100D);
					}

					if (carbohydrateTotal > 0) {
						cntPercent.setCarbohydrate(carbohydrate / carbohydrateTotal * 100D);
					}

					if (fatTotal > 0) {
						cntPercent.setFat(fat / fatTotal * 100D);
					}

					if (heatEnergy > 0) {
						cntPercent.setHeatEnergy(heatEnergy / heatEnergyTotal * 100D);
					}

					if (proteinTotal > 0) {
						cntPercent.setProtein(protein / proteinTotal * 100D);
					}

					context.put("dietaryCount_breakfast", cnt);
					context.put("dietaryCountPercent_breakfast", cntPercent);

					context.put("breakfastList", breakfastList);
				}

				if (breakfastMidList.size() > 0) {
					double carbohydrate = 0.0;
					double heatEnergy = 0.0;
					double calcium = 0.0;
					double protein = 0.0;
					double fat = 0.0;

					for (DietaryRptModel rptModel : breakfastMidList) {
						Dietary dietary = rptModel.getDietary();
						carbohydrate = carbohydrate + dietary.getCarbohydrate();
						heatEnergy = heatEnergy + dietary.getHeatEnergy();
						calcium = calcium + dietary.getCalcium();
						protein = protein + dietary.getProtein();
						fat = fat + dietary.getFat();
					}

					DietaryCount cnt = new DietaryCount();
					cnt.setCalcium(calcium);
					cnt.setCarbohydrate(carbohydrate);
					cnt.setHeatEnergy(heatEnergy);
					cnt.setProtein(protein);
					cnt.setFat(fat);

					DietaryCount cntPercent = new DietaryCount();

					if (calcium > 0) {
						cntPercent.setCalcium(calcium / calciumTotal * 100D);
					}

					if (carbohydrateTotal > 0) {
						cntPercent.setCarbohydrate(carbohydrate / carbohydrateTotal * 100D);
					}

					if (fatTotal > 0) {
						cntPercent.setFat(fat / fatTotal * 100D);
					}

					if (heatEnergy > 0) {
						cntPercent.setHeatEnergy(heatEnergy / heatEnergyTotal * 100D);
					}

					if (proteinTotal > 0) {
						cntPercent.setProtein(protein / proteinTotal * 100D);
					}

					context.put("dietaryCount_breakfastMid", cnt);
					context.put("dietaryCountPercent_breakfastMid", cntPercent);

					context.put("breakfastMidList", breakfastMidList);
				}

				if (lunchList.size() > 0) {
					double carbohydrate = 0.0;
					double heatEnergy = 0.0;
					double calcium = 0.0;
					double protein = 0.0;
					double fat = 0.0;

					for (DietaryRptModel rptModel : lunchList) {
						Dietary dietary = rptModel.getDietary();
						carbohydrate = carbohydrate + dietary.getCarbohydrate();
						heatEnergy = heatEnergy + dietary.getHeatEnergy();
						calcium = calcium + dietary.getCalcium();
						protein = protein + dietary.getProtein();
						fat = fat + dietary.getFat();
					}

					DietaryCount cnt = new DietaryCount();
					cnt.setCalcium(calcium);
					cnt.setCarbohydrate(carbohydrate);
					cnt.setHeatEnergy(heatEnergy);
					cnt.setProtein(protein);
					cnt.setFat(fat);

					DietaryCount cntPercent = new DietaryCount();

					if (calcium > 0) {
						cntPercent.setCalcium(calcium / calciumTotal * 100D);
					}

					if (carbohydrateTotal > 0) {
						cntPercent.setCarbohydrate(carbohydrate / carbohydrateTotal * 100D);
					}

					if (fatTotal > 0) {
						cntPercent.setFat(fat / fatTotal * 100D);
					}

					if (heatEnergy > 0) {
						cntPercent.setHeatEnergy(heatEnergy / heatEnergyTotal * 100D);
					}

					if (proteinTotal > 0) {
						cntPercent.setProtein(protein / proteinTotal * 100D);
					}

					context.put("dietaryCount_lunch", cnt);
					context.put("dietaryCountPercent_lunch", cntPercent);

					context.put("lunchList", lunchList);
				}

				if (snackList.size() > 0) {
					double carbohydrate = 0.0;
					double heatEnergy = 0.0;
					double calcium = 0.0;
					double protein = 0.0;
					double fat = 0.0;

					for (DietaryRptModel rptModel : snackList) {
						Dietary dietary = rptModel.getDietary();
						carbohydrate = carbohydrate + dietary.getCarbohydrate();
						heatEnergy = heatEnergy + dietary.getHeatEnergy();
						calcium = calcium + dietary.getCalcium();
						protein = protein + dietary.getProtein();
						fat = fat + dietary.getFat();
					}

					DietaryCount cnt = new DietaryCount();
					cnt.setCalcium(calcium);
					cnt.setCarbohydrate(carbohydrate);
					cnt.setHeatEnergy(heatEnergy);
					cnt.setProtein(protein);
					cnt.setFat(fat);

					DietaryCount cntPercent = new DietaryCount();

					if (calcium > 0) {
						cntPercent.setCalcium(calcium / calciumTotal * 100D);
					}

					if (carbohydrateTotal > 0) {
						cntPercent.setCarbohydrate(carbohydrate / carbohydrateTotal * 100D);
					}

					if (fatTotal > 0) {
						cntPercent.setFat(fat / fatTotal * 100D);
					}

					if (heatEnergy > 0) {
						cntPercent.setHeatEnergy(heatEnergy / heatEnergyTotal * 100D);
					}

					if (proteinTotal > 0) {
						cntPercent.setProtein(protein / proteinTotal * 100D);
					}

					context.put("dietaryCount_snack", cnt);
					context.put("dietaryCountPercent_snack", cntPercent);

					context.put("snackList", snackList);
				}

				if (dinnerList.size() > 0) {
					double carbohydrate = 0.0;
					double heatEnergy = 0.0;
					double calcium = 0.0;
					double protein = 0.0;
					double fat = 0.0;

					for (DietaryRptModel rptModel : dinnerList) {
						Dietary dietary = rptModel.getDietary();
						carbohydrate = carbohydrate + dietary.getCarbohydrate();
						heatEnergy = heatEnergy + dietary.getHeatEnergy();
						calcium = calcium + dietary.getCalcium();
						protein = protein + dietary.getProtein();
						fat = fat + dietary.getFat();
					}

					DietaryCount cnt = new DietaryCount();
					cnt.setCalcium(calcium);
					cnt.setCarbohydrate(carbohydrate);
					cnt.setHeatEnergy(heatEnergy);
					cnt.setProtein(protein);
					cnt.setFat(fat);

					DietaryCount cntPercent = new DietaryCount();

					if (calcium > 0) {
						cntPercent.setCalcium(calcium / calciumTotal * 100D);
					}

					if (carbohydrateTotal > 0) {
						cntPercent.setCarbohydrate(carbohydrate / carbohydrateTotal * 100D);
					}

					if (fatTotal > 0) {
						cntPercent.setFat(fat / fatTotal * 100D);
					}

					if (heatEnergy > 0) {
						cntPercent.setHeatEnergy(heatEnergy / heatEnergyTotal * 100D);
					}

					if (proteinTotal > 0) {
						cntPercent.setProtein(protein / proteinTotal * 100D);
					}

					context.put("dietaryCount_dinner", cnt);
					context.put("dietaryCountPercent_dinner", cntPercent);

					context.put("dinnerList", dinnerList);
				}

				DietaryCount dietaryCountSum = new DietaryCount();
				dietaryCountSum.setCalcium(calciumTotal);
				dietaryCountSum.setCarbohydrate(carbohydrateTotal);
				dietaryCountSum.setFat(fatTotal);
				dietaryCountSum.setProtein(proteinTotal);
				dietaryCountSum.setHeatEnergy(heatEnergyTotal);

				DietaryDayRptModel dayRptModel = new DietaryDayRptModel();
				dayRptModel.setBreakfastList(breakfastList);
				dayRptModel.setBreakfastMidList(breakfastMidList);
				dayRptModel.setLunchList(lunchList);
				dayRptModel.setSnackList(snackList);
				dayRptModel.setDinnerList(dinnerList);

				context.put("dayRptModel", dayRptModel);
				context.put("dietaryCountSum", dietaryCountSum);

				if (heatEnergyMomingTotal > 0) {
					DietaryCount momingTotal = new DietaryCount();
					momingTotal.setCalcium(calciumMomingTotal);
					momingTotal.setCarbohydrate(carbohydrateMomingTotal);
					momingTotal.setHeatEnergy(heatEnergyMomingTotal);
					momingTotal.setProtein(proteinMomingTotal);
					momingTotal.setFat(fatMomingTotal);

					context.put("momingTotal", momingTotal);

					DietaryCount momingTotalPercent = new DietaryCount();

					if (calciumMomingTotal > 0) {
						momingTotalPercent.setCalcium(calciumMomingTotal / calciumTotal * 100D);
					}

					if (carbohydrateTotal > 0) {
						momingTotalPercent.setCarbohydrate(carbohydrateMomingTotal / carbohydrateTotal * 100D);
					}

					if (fatTotal > 0) {
						momingTotalPercent.setFat(fatMomingTotal / fatTotal * 100D);
					}

					if (heatEnergyMomingTotal > 0) {
						momingTotalPercent.setHeatEnergy(heatEnergyMomingTotal / heatEnergyTotal * 100D);
					}

					if (proteinTotal > 0) {
						momingTotalPercent.setProtein(proteinMomingTotal / proteinTotal * 100D);
					}

					context.put("momingTotalPercent", momingTotalPercent);
				}

				if (heatEnergyNoonTotal > 0) {
					DietaryCount noonTotal = new DietaryCount();
					noonTotal.setCalcium(calciumNoonTotal);
					noonTotal.setCarbohydrate(carbohydrateNoonTotal);
					noonTotal.setHeatEnergy(heatEnergyNoonTotal);
					noonTotal.setProtein(proteinNoonTotal);
					noonTotal.setFat(fatNoonTotal);

					context.put("noonTotal", noonTotal);

					DietaryCount noonTotalPercent = new DietaryCount();

					if (calciumNoonTotal > 0) {
						noonTotalPercent.setCalcium(calciumNoonTotal / calciumTotal * 100D);
					}

					if (carbohydrateTotal > 0) {
						noonTotalPercent.setCarbohydrate(carbohydrateNoonTotal / carbohydrateTotal * 100D);
					}

					if (fatTotal > 0) {
						noonTotalPercent.setFat(fatNoonTotal / fatTotal * 100D);
					}

					if (heatEnergyNoonTotal > 0) {
						noonTotalPercent.setHeatEnergy(heatEnergyNoonTotal / heatEnergyTotal * 100D);
					}

					if (proteinTotal > 0) {
						noonTotalPercent.setProtein(proteinNoonTotal / proteinTotal * 100D);
					}

					context.put("noonTotalPercent", noonTotalPercent);
				}

				List<DietaryStatistics> rows = new ArrayList<DietaryStatistics>();

				double tt = heatEnergyGrain + heatEnergyBeans + heatEnergyAnimals + heatEnergyOthers;
				double pp = proteinBeans + proteinAnimals + proteinOthers;

				DietaryStatistics m1 = new DietaryStatistics();
				m1.setTenantId(query.getTenantId());
				m1.setSysFlag(sysFlag);
				m1.setSemester(semester);
				m1.setWeek(week);
				m1.setYear(year);
				m1.setType("heatEnergyPerDayDietaryOfWeekTenant");
				m1.setTitle("谷类");
				m1.setName("heatEnergyGrain");
				m1.setValue(Math.round(heatEnergyGrain));
				m1.setSortNo(4);
				rows.add(m1);

				DietaryStatistics m11 = new DietaryStatistics();
				m11.setTenantId(query.getTenantId());
				m11.setSysFlag(sysFlag);
				m11.setSemester(semester);
				m11.setWeek(week);
				m11.setYear(year);
				m11.setType("heatEnergyPercentPerDayDietaryOfWeekTenant");
				m11.setTitle("谷类");
				m11.setName("heatEnergyGrainPercent");
				m11.setValue(Math.round(heatEnergyGrain / tt * 100D));
				m11.setSortNo(4);
				rows.add(m11);

				DietaryStatistics m2 = new DietaryStatistics();
				m2.setTenantId(query.getTenantId());
				m2.setSysFlag(sysFlag);
				m2.setSemester(semester);
				m2.setWeek(week);
				m2.setYear(year);
				m2.setType("heatEnergyPerDayDietaryOfWeekTenant");
				m2.setTitle("豆类");
				m2.setName("heatEnergyBeans");
				m2.setValue(Math.round(heatEnergyBeans));
				m2.setSortNo(1);
				rows.add(m2);

				DietaryStatistics m22 = new DietaryStatistics();
				m22.setTenantId(query.getTenantId());
				m22.setSysFlag(sysFlag);
				m22.setSemester(semester);
				m22.setWeek(week);
				m22.setYear(year);
				m22.setType("heatEnergyPercentPerDayDietaryOfWeekTenant");
				m22.setTitle("豆类");
				m22.setName("heatEnergyBeansPercent");
				m22.setValue(Math.round(heatEnergyBeans / tt * 100D));
				m22.setSortNo(1);
				rows.add(m22);

				DietaryStatistics m3 = new DietaryStatistics();
				m3.setTenantId(query.getTenantId());
				m3.setSysFlag(sysFlag);
				m3.setSemester(semester);
				m3.setWeek(week);
				m3.setYear(year);
				m3.setType("heatEnergyPerDayDietaryOfWeekTenant");
				m3.setTitle("动物类食物");
				m3.setName("heatEnergyAnimals");
				m3.setValue(Math.round(heatEnergyAnimals));
				m3.setSortNo(2);
				rows.add(m3);

				DietaryStatistics m33 = new DietaryStatistics();
				m33.setTenantId(query.getTenantId());
				m33.setSysFlag(sysFlag);
				m33.setSemester(semester);
				m33.setWeek(week);
				m33.setYear(year);
				m33.setType("heatEnergyPercentPerDayDietaryOfWeekTenant");
				m33.setTitle("动物类食物");
				m33.setName("heatEnergyAnimalsPercent");
				m33.setValue(Math.round(heatEnergyAnimals / tt * 100D));
				m33.setSortNo(2);
				rows.add(m33);

				DietaryStatistics m4 = new DietaryStatistics();
				m4.setTenantId(query.getTenantId());
				m4.setSysFlag(sysFlag);
				m4.setSemester(semester);
				m4.setWeek(week);
				m4.setYear(year);
				m4.setType("heatEnergyPerDayDietaryOfWeekTenant");
				m4.setTitle("谷、薯、杂豆及其他");
				m4.setName("heatEnergyOthers");
				m4.setValue(Math.round(heatEnergyOthers));
				m4.setSortNo(3);
				rows.add(m4);

				DietaryStatistics m44 = new DietaryStatistics();
				m44.setTenantId(query.getTenantId());
				m44.setSysFlag(sysFlag);
				m44.setSemester(semester);
				m44.setWeek(week);
				m44.setYear(year);
				m44.setType("heatEnergyPercentPerDayDietaryOfWeekTenant");
				m44.setTitle("谷、薯、杂豆及其他");
				m44.setName("heatEnergyOthersPercent");
				m44.setValue(Math.round(heatEnergyOthers / tt * 100D));
				m44.setSortNo(3);
				rows.add(m44);

				DietaryStatistics mp2 = new DietaryStatistics();
				mp2.setTenantId(query.getTenantId());
				mp2.setSysFlag(sysFlag);
				mp2.setSemester(semester);
				mp2.setWeek(week);
				mp2.setYear(year);
				mp2.setType("proteinPerDietaryOfWeekTenant");
				mp2.setTitle("豆类");
				mp2.setName("proteinBeans");
				mp2.setValue(Math.round(proteinBeans));
				mp2.setSortNo(1);
				rows.add(mp2);

				DietaryStatistics mp22 = new DietaryStatistics();
				mp22.setTenantId(query.getTenantId());
				mp22.setSysFlag(sysFlag);
				mp22.setSemester(semester);
				mp22.setWeek(week);
				mp22.setYear(year);
				mp22.setType("proteinPercentPerDayDietaryOfWeekTenant");
				mp22.setTitle("豆类");
				mp22.setName("proteinBeansPercent");
				mp22.setValue(Math.round(proteinBeans / pp * 100D));
				mp22.setSortNo(1);
				rows.add(mp22);

				DietaryStatistics mp3 = new DietaryStatistics();
				mp3.setTenantId(query.getTenantId());
				mp3.setSysFlag(sysFlag);
				mp3.setSemester(semester);
				mp3.setWeek(week);
				mp3.setYear(year);
				mp3.setType("proteinPerDayDietaryOfWeekTenant");
				mp3.setTitle("动物类食物");
				mp3.setName("proteinAnimals");
				mp3.setValue(Math.round(proteinAnimals));
				mp3.setSortNo(2);
				rows.add(mp3);

				DietaryStatistics mp33 = new DietaryStatistics();
				mp33.setTenantId(query.getTenantId());
				mp33.setSysFlag(sysFlag);
				mp33.setSemester(semester);
				mp33.setWeek(week);
				mp33.setYear(year);
				mp33.setType("proteinPercentPerDayDietaryOfWeekTenant");
				mp33.setTitle("动物类食物");
				mp33.setName("proteinAnimalsPercent");
				mp33.setValue(Math.round(proteinAnimals / pp * 100D));
				mp33.setSortNo(2);
				rows.add(mp33);

				DietaryStatistics mp4 = new DietaryStatistics();
				mp4.setTenantId(query.getTenantId());
				mp4.setSysFlag(sysFlag);
				mp4.setSemester(semester);
				mp4.setWeek(week);
				mp4.setYear(year);
				mp4.setType("proteinPerDayDietaryOfWeekTenant");
				mp4.setTitle("谷、薯、杂豆及其他");
				mp4.setName("proteinOthers");
				mp4.setValue(Math.round(proteinOthers));
				mp4.setSortNo(3);
				rows.add(mp4);

				DietaryStatistics mp44 = new DietaryStatistics();
				mp44.setTenantId(query.getTenantId());
				mp44.setSysFlag(sysFlag);
				mp44.setSemester(semester);
				mp44.setWeek(week);
				mp44.setYear(year);
				mp44.setType("proteinPercentPerDayDietaryOfWeekTenant");
				mp44.setTitle("谷、薯、杂豆及其他");
				mp44.setName("proteinOthersPercent");
				mp44.setValue(Math.round(proteinOthers / pp * 100D));
				mp44.setSortNo(3);
				rows.add(mp44);

				tt = heatEnergyPlant + heatEnergyAnimal;

				if (tt > 0) {
					DietaryStatistics mx1 = new DietaryStatistics();
					mx1.setTenantId(query.getTenantId());
					mx1.setSysFlag(sysFlag);
					mx1.setSemester(semester);
					mx1.setWeek(week);
					mx1.setYear(year);
					mx1.setType("heatEnergyX1PerDayDietaryOfWeekTenant");
					mx1.setTitle("动物性食物");
					mx1.setName("heatEnergyAnimal");
					mx1.setValue(Math.round(heatEnergyAnimal));
					mx1.setSortNo(1);
					rows.add(mx1);

					DietaryStatistics mx11 = new DietaryStatistics();
					mx11.setTenantId(query.getTenantId());
					mx11.setSysFlag(sysFlag);
					mx11.setSemester(semester);
					mx11.setWeek(week);
					mx11.setYear(year);
					mx11.setType("heatEnergyX1PercentPerDayDietaryOfWeekTenant");
					mx11.setTitle("动物性食物");
					mx11.setName("heatEnergyAnimalPercent");
					mx11.setValue(Math.round(heatEnergyAnimal / tt * 100D));
					mx11.setSortNo(1);
					rows.add(mx11);

					DietaryStatistics mx2 = new DietaryStatistics();
					mx2.setTenantId(query.getTenantId());
					mx2.setSysFlag(sysFlag);
					mx2.setSemester(semester);
					mx2.setWeek(week);
					mx2.setYear(year);
					mx2.setType("heatEnergyX1PerDayDietaryOfWeekTenant");
					mx2.setTitle("植物性食物");
					mx2.setName("heatEnergyPlant");
					mx2.setValue(Math.round(heatEnergyPlant));
					mx2.setSortNo(2);
					rows.add(mx2);

					DietaryStatistics mx22 = new DietaryStatistics();
					mx22.setTenantId(query.getTenantId());
					mx22.setSysFlag(sysFlag);
					mx22.setSemester(semester);
					mx22.setWeek(week);
					mx22.setYear(year);
					mx22.setType("heatEnergyX1PercentPerDayDietaryOfWeekTenant");
					mx22.setTitle("植物性食物");
					mx22.setName("heatEnergyPlantPercent");
					mx22.setValue(Math.round(heatEnergyPlant / tt * 100D));
					mx22.setSortNo(2);
					rows.add(mx22);

				}

				tt = heatEnergyFat + heatEnergyProtein + heatEnergyCarbohydrate;
				if (tt > 0) {
					DietaryStatistics mx1 = new DietaryStatistics();
					mx1.setTenantId(query.getTenantId());
					mx1.setSysFlag(sysFlag);
					mx1.setSemester(semester);
					mx1.setWeek(week);
					mx1.setYear(year);
					mx1.setType("heatEnergyX2PerDayDietaryOfWeekTenant");
					mx1.setTitle("脂肪");
					mx1.setName("heatEnergyFat");
					mx1.setValue(Math.round(heatEnergyFat));
					mx1.setSortNo(2);
					rows.add(mx1);

					DietaryStatistics mx2 = new DietaryStatistics();
					mx2.setTenantId(query.getTenantId());
					mx2.setSysFlag(sysFlag);
					mx2.setSemester(semester);
					mx2.setWeek(week);
					mx2.setYear(year);
					mx2.setType("heatEnergyX2PerDayDietaryOfWeekTenant");
					mx2.setTitle("蛋白质");
					mx2.setName("heatEnergyProtein");
					mx2.setValue(Math.round(heatEnergyProtein));
					mx2.setSortNo(1);
					rows.add(mx2);

					DietaryStatistics mx3 = new DietaryStatistics();
					mx3.setTenantId(query.getTenantId());
					mx3.setSysFlag(sysFlag);
					mx3.setSemester(semester);
					mx3.setWeek(week);
					mx3.setYear(year);
					mx3.setType("heatEnergyX2PerDayDietaryOfWeekTenant");
					mx3.setTitle("碳水化合物");
					mx3.setName("heatEnergyCarbohydrate");
					mx3.setValue(Math.round(heatEnergyCarbohydrate));
					mx3.setSortNo(3);
					rows.add(mx3);

					DietaryStatistics mx11 = new DietaryStatistics();
					mx11.setTenantId(query.getTenantId());
					mx11.setSysFlag(sysFlag);
					mx11.setSemester(semester);
					mx11.setWeek(week);
					mx11.setYear(year);
					mx11.setType("heatEnergyX2PercentPerDayDietaryOfWeekTenant");
					mx11.setTitle("脂肪");
					mx11.setName("heatEnergyFatPercent");
					mx11.setValue(Math.round(heatEnergyFat / tt * 100D));
					mx11.setSortNo(2);
					rows.add(mx11);

					DietaryStatistics mx12 = new DietaryStatistics();
					mx12.setTenantId(query.getTenantId());
					mx12.setSysFlag(sysFlag);
					mx12.setSemester(semester);
					mx12.setWeek(week);
					mx12.setYear(year);
					mx12.setType("heatEnergyX2PercentPerDayDietaryOfWeekTenant");
					mx12.setTitle("蛋白质");
					mx12.setName("heatEnergyProteinPercent");
					mx12.setValue(Math.round(heatEnergyProtein / tt * 100D));
					mx12.setSortNo(1);
					rows.add(mx12);

					DietaryStatistics mx13 = new DietaryStatistics();
					mx13.setTenantId(query.getTenantId());
					mx13.setSysFlag(sysFlag);
					mx13.setSemester(semester);
					mx13.setWeek(week);
					mx13.setYear(year);
					mx13.setType("heatEnergyX2PercentPerDayDietaryOfWeekTenant");
					mx13.setTitle("碳水化合物");
					mx13.setName("heatEnergyCarbohydratePercent");
					mx13.setValue(Math.round(heatEnergyCarbohydrate / tt * 100D));
					mx13.setSortNo(3);
					rows.add(mx13);

				}

				tt = heatEnergyMoming + heatEnergyNoon + heatEnergyDinner;
				if (heatEnergyMoming > 0 && heatEnergyNoon > 0) {
					DietaryStatistics nx1 = new DietaryStatistics();
					nx1.setTenantId(query.getTenantId());
					nx1.setSysFlag(sysFlag);
					nx1.setSemester(semester);
					nx1.setWeek(week);
					nx1.setYear(year);
					nx1.setType("heatEnergyX3PerDayDietaryOfWeekTenant");
					nx1.setTitle("早餐");
					nx1.setName("heatEnergyMoming");
					nx1.setValue(Math.round(heatEnergyMoming));
					nx1.setSortNo(1);
					rows.add(nx1);

					DietaryStatistics nx2 = new DietaryStatistics();
					nx2.setTenantId(query.getTenantId());
					nx2.setSysFlag(sysFlag);
					nx2.setSemester(semester);
					nx2.setWeek(week);
					nx2.setYear(year);
					nx2.setType("heatEnergyX3PerDayDietaryOfWeekTenant");
					nx2.setTitle("中餐");
					nx2.setName("heatEnergyNoon");
					nx2.setValue(Math.round(heatEnergyNoon));
					nx2.setSortNo(2);
					rows.add(nx2);

					DietaryStatistics nx3 = new DietaryStatistics();
					nx3.setTenantId(query.getTenantId());
					nx3.setSysFlag(sysFlag);
					nx3.setSemester(semester);
					nx3.setWeek(week);
					nx3.setYear(year);
					nx3.setType("heatEnergyX3PerDayDietaryOfWeekTenant");
					nx3.setTitle("晚餐");
					nx3.setName("heatEnergyDinner");
					nx3.setValue(Math.round(heatEnergyDinner));
					nx3.setSortNo(3);
					rows.add(nx3);

					if (heatEnergyDinner > 0) {
						DietaryStatistics nx11 = new DietaryStatistics();
						nx11.setTenantId(query.getTenantId());
						nx11.setSysFlag(sysFlag);
						nx11.setSemester(semester);
						nx11.setWeek(week);
						nx11.setYear(year);
						nx11.setType("heatEnergyX3PercentPerDayDietaryOfWeekTenant");
						nx11.setTitle("早餐");
						nx11.setName("heatEnergyMoming");
						nx11.setValue(Math.round(heatEnergyMoming / tt * 100D));
						nx11.setSortNo(1);
						rows.add(nx11);

						DietaryStatistics nx12 = new DietaryStatistics();
						nx12.setTenantId(query.getTenantId());
						nx12.setSysFlag(sysFlag);
						nx12.setSemester(semester);
						nx12.setWeek(week);
						nx12.setYear(year);
						nx12.setType("heatEnergyX3PercentPerDayDietaryOfWeekTenant");
						nx12.setTitle("中餐");
						nx12.setName("heatEnergyNoon");
						nx12.setValue(Math.round(heatEnergyNoon / tt * 100D));
						nx12.setSortNo(2);
						rows.add(nx12);

						DietaryStatistics nx13 = new DietaryStatistics();
						nx13.setTenantId(query.getTenantId());
						nx13.setSysFlag(sysFlag);
						nx13.setSemester(semester);
						nx13.setWeek(week);
						nx13.setYear(year);
						nx13.setType("heatEnergyX3PercentPerDayDietaryOfWeekTenant");
						nx13.setTitle("晚餐");
						nx13.setName("heatEnergyDinner");
						nx13.setValue(Math.round(heatEnergyDinner / tt * 100D));
						nx13.setSortNo(3);
						rows.add(nx13);
					} else {
						tt = heatEnergyMoming + heatEnergyNoon;

						DietaryStatistics nx11 = new DietaryStatistics();
						nx11.setTenantId(query.getTenantId());
						nx11.setSysFlag(sysFlag);
						nx11.setSemester(semester);
						nx11.setWeek(week);
						nx11.setYear(year);
						nx11.setType("heatEnergyX3PercentPerDayDietaryOfWeekTenant");
						nx11.setTitle("早餐");
						nx11.setName("heatEnergyMoming");
						nx11.setValue(Math.round(heatEnergyMoming / tt * 100D * 0.7));
						nx11.setSortNo(1);
						rows.add(nx11);

						DietaryStatistics nx12 = new DietaryStatistics();
						nx12.setTenantId(query.getTenantId());
						nx12.setSysFlag(sysFlag);
						nx12.setSemester(semester);
						nx12.setWeek(week);
						nx12.setYear(year);
						nx12.setType("heatEnergyX3PercentPerDayDietaryOfWeekTenant");
						nx12.setTitle("中餐");
						nx12.setName("heatEnergyNoon");
						nx12.setValue(Math.round(heatEnergyNoon / tt * 100D * 0.7));
						nx12.setSortNo(2);
						rows.add(nx12);

						DietaryStatistics nx13 = new DietaryStatistics();
						nx13.setTenantId(query.getTenantId());
						nx13.setSysFlag(sysFlag);
						nx13.setSemester(semester);
						nx13.setWeek(week);
						nx13.setYear(year);
						nx13.setType("heatEnergyX3PercentPerDayDietaryOfWeekTenant");
						nx13.setTitle("晚餐");
						nx13.setName("heatEnergyDinner");
						nx13.setValue(30);// 30%
						nx13.setSortNo(3);
						rows.add(nx13);

					}

				}

				if (vitaminATotal > 0) {
					double p1 = vitaminAAnimals / vitaminATotal;
					DietaryStatistics mx15 = new DietaryStatistics();
					mx15.setTenantId(query.getTenantId());
					mx15.setSysFlag(sysFlag);
					mx15.setSemester(semester);
					mx15.setWeek(week);
					mx15.setYear(year);
					mx15.setType("vitaminAAnimalsX3PercentPerDayDietaryOfWeekTenant");
					mx15.setTitle("动物性V-A");
					mx15.setName("vitaminAAnimalsPercent");
					mx15.setValue(Math.round(p1 * 100D));
					mx15.setSortNo(3);
					rows.add(mx15);

					DietaryStatistics mx16 = new DietaryStatistics();
					mx16.setTenantId(query.getTenantId());
					mx16.setSysFlag(sysFlag);
					mx16.setSemester(semester);
					mx16.setWeek(week);
					mx16.setYear(year);
					mx16.setType("vitaminAAnimalsX3PercentPerDayDietaryOfWeekTenant");
					mx16.setTitle("其他");
					mx16.setName("vitaminAOthersPercent");
					mx16.setValue(100 - mx15.getValue());
					mx16.setSortNo(3);
					rows.add(mx16);
				}

				if (ironTotal > 0) {
					double p2 = ironAnimals / ironTotal;
					DietaryStatistics mx15 = new DietaryStatistics();
					mx15.setTenantId(query.getTenantId());
					mx15.setSysFlag(sysFlag);
					mx15.setSemester(semester);
					mx15.setWeek(week);
					mx15.setYear(year);
					mx15.setType("ironAnimalsX3PercentPerDayDietaryOfWeekTenant");
					mx15.setTitle("动物性铁");
					mx15.setName("ironAnimalsPercent");
					mx15.setValue(Math.round(p2 * 100D));
					mx15.setSortNo(3);
					rows.add(mx15);

					DietaryStatistics mx16 = new DietaryStatistics();
					mx16.setTenantId(query.getTenantId());
					mx16.setSysFlag(sysFlag);
					mx16.setSemester(semester);
					mx16.setWeek(week);
					mx16.setYear(year);
					mx16.setType("ironAnimalsX3PercentPerDayDietaryOfWeekTenant");
					mx16.setTitle("其他");
					mx16.setName("ironOthersPercent");
					mx16.setValue(100 - mx15.getValue());
					mx16.setSortNo(3);
					rows.add(mx16);
				}

				if (fatTotal2 > 0) {
					double p2 = fatAnimals2 / fatTotal2;
					DietaryStatistics mx15 = new DietaryStatistics();
					mx15.setTenantId(query.getTenantId());
					mx15.setSysFlag(sysFlag);
					mx15.setSemester(semester);
					mx15.setWeek(week);
					mx15.setYear(year);
					mx15.setType("fatAnimalsX3PercentPerDayDietaryOfWeekTenant");
					mx15.setTitle("动物性脂肪");
					mx15.setName("fatAnimalsPercent");
					mx15.setValue(Math.round(p2 * 100D));
					mx15.setSortNo(3);
					rows.add(mx15);

					DietaryStatistics mx16 = new DietaryStatistics();
					mx16.setTenantId(query.getTenantId());
					mx16.setSysFlag(sysFlag);
					mx16.setSemester(semester);
					mx16.setWeek(week);
					mx16.setYear(year);
					mx16.setType("fatAnimalsX3PercentPerDayDietaryOfWeekTenant");
					mx16.setTitle("其他");
					mx16.setName("fatOthersPercent");
					mx16.setValue(100 - mx15.getValue());
					mx16.setSortNo(3);
					rows.add(mx16);
				}

				if (calciumTotal2 > 0 && phosphorusTotal2 > 0) {
					double p2 = phosphorusTotal2 / (calciumTotal2 + phosphorusTotal2);
					DietaryStatistics mx15 = new DietaryStatistics();
					mx15.setTenantId(query.getTenantId());
					mx15.setSysFlag(sysFlag);
					mx15.setSemester(semester);
					mx15.setWeek(week);
					mx15.setYear(year);
					mx15.setType("calciumPhosphorusX3PercentOfWeekTenant");
					mx15.setTitle("磷占比");
					mx15.setName("phosphorusPercent");
					mx15.setValue(Math.round(p2 * 100D));
					mx15.setSortNo(3);
					rows.add(mx15);

					DietaryStatistics mx16 = new DietaryStatistics();
					mx16.setTenantId(query.getTenantId());
					mx16.setSysFlag(sysFlag);
					mx16.setSemester(semester);
					mx16.setWeek(week);
					mx16.setYear(year);
					mx16.setType("calciumPhosphorusX3PercentOfWeekTenant");
					mx16.setTitle("钙占比");
					mx16.setName("calciumPercent");
					mx16.setValue(100 - mx15.getValue());
					mx16.setSortNo(3);
					rows.add(mx16);
				}

				getDietaryStatisticsService().saveAll(query.getTenantId(), sysFlag, year, semester, week, rows);

			}
		}
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

	public FoodDRIPercentService getFoodDRIPercentService() {
		if (foodDRIPercentService == null) {
			foodDRIPercentService = ContextFactory.getBean("com.glaf.heathcare.service.foodDRIPercentService");
		}
		return foodDRIPercentService;
	}

	public FoodDRIService getFoodDRIService() {
		if (foodDRIService == null) {
			foodDRIService = ContextFactory.getBean("com.glaf.heathcare.service.foodDRIService");
		}
		return foodDRIService;
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

	public void setDietaryItemService(DietaryItemService dietaryItemService) {
		this.dietaryItemService = dietaryItemService;
	}

	public void setDietaryService(DietaryService dietaryService) {
		this.dietaryService = dietaryService;
	}

	public void setDietaryStatisticsService(DietaryStatisticsService dietaryStatisticsService) {
		this.dietaryStatisticsService = dietaryStatisticsService;
	}

	public void setTenantConfigService(TenantConfigService tenantConfigService) {
		this.tenantConfigService = tenantConfigService;
	}

}
