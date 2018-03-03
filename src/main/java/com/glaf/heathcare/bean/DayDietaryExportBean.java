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

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import com.glaf.base.modules.sys.model.TenantConfig;
import com.glaf.base.modules.sys.service.TenantConfigService;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.security.LoginContext;

import com.glaf.heathcare.domain.Dietary;
import com.glaf.heathcare.domain.DietaryCount;
import com.glaf.heathcare.domain.DietaryItem;
import com.glaf.heathcare.domain.DietaryRptModel;
import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.domain.FoodDRI;
import com.glaf.heathcare.domain.FoodDRIPercent;
import com.glaf.heathcare.domain.DietaryDayRptModel;
import com.glaf.heathcare.query.DietaryCountQuery;
import com.glaf.heathcare.query.DietaryItemQuery;
import com.glaf.heathcare.query.DietaryQuery;
import com.glaf.heathcare.query.FoodCompositionQuery;
import com.glaf.heathcare.service.DietaryCountService;
import com.glaf.heathcare.service.DietaryItemService;
import com.glaf.heathcare.service.DietaryService;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.heathcare.service.FoodDRIPercentService;
import com.glaf.heathcare.service.FoodDRIService;

public class DayDietaryExportBean {

	protected TenantConfigService tenantConfigService;

	protected DietaryCountService dietaryCountService;

	protected DietaryService dietaryService;

	protected DietaryItemService dietaryItemService;

	protected FoodCompositionService foodCompositionService;

	protected FoodDRIService foodDRIService;

	protected FoodDRIPercentService foodDRIPercentService;

	public void fillBlank(List<DietaryRptModel> list, int size) {
		int total = 0;
		for (DietaryRptModel rptModel : list) {
			if (rptModel.getItems() != null && rptModel.getItems().size() > 0) {
				total = total + rptModel.getItems().size();
			}
		}

		if (total < size) {
			DietaryItem item = null;
			DietaryRptModel model = new DietaryRptModel();
			List<DietaryItem> items = new ArrayList<DietaryItem>();
			for (int i = 0; i < size - total; i++) {
				item = new DietaryItem();
				items.add(item);
			}
			model.setItems(items);
			list.add(model);
		}
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

	public DietaryService getDietaryService() {
		if (dietaryService == null) {
			dietaryService = ContextFactory.getBean("com.glaf.heathcare.service.dietaryService");
		}
		return dietaryService;
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

	protected void populate(DietaryDayRptModel m, DietaryCount c, FoodDRI foodDRI, FoodDRIPercent foodDRIPercent) {
		m.setCalcium(c.getCalcium());
		m.setHeatEnergy(c.getHeatEnergy());
		m.setProtein(c.getProtein());
		if (foodDRI != null && foodDRIPercent != null) {
			double d1 = c.getCalcium() / foodDRI.getCalcium();
			if (d1 > foodDRIPercent.getCalcium()) {
				m.setCalciumEvaluate("OK!");
			} else {
				m.setCalciumEvaluate("<span class='red'>少!</span>");
			}

			double d2 = c.getHeatEnergy() / foodDRI.getHeatEnergy();
			if (d2 > foodDRIPercent.getHeatEnergy()) {
				m.setHeatEnergyEvaluate("OK!");
			} else {
				m.setHeatEnergyEvaluate("<span class='red'>低!</span>");
			}

			double d3 = c.getProtein() / foodDRI.getProtein();
			if (d3 > foodDRIPercent.getProtein()) {
				m.setProteinEvaluate("OK!");
			} else {
				m.setProteinEvaluate("<span class='red'>少!</span>");
			}

			double d4 = c.getFat() / foodDRI.getFat();
			if (d4 > foodDRIPercent.getFat()) {
				m.setFatEvaluate("OK!");
			} else {
				m.setFatEvaluate("<span class='red'>少!</span>");
			}

			double d5 = c.getCarbohydrate() / foodDRI.getCarbohydrate();
			if (d5 > foodDRIPercent.getCarbohydrate()) {
				m.setCarbohydrateEvaluate("OK!");
			} else {
				m.setCarbohydrateEvaluate("<span class='red'>少!</span>");
			}

		}
	}

	protected void populate(DietaryDayRptModel m, FoodDRI foodDRI) {
		if (foodDRI != null) {
			m.setCalciumStandard(foodDRI.getCalcium());
			m.setHeatEnergyStandard(foodDRI.getHeatEnergy());
			m.setProteinStandard(foodDRI.getProtein());
		}
	}

	public Map<String, Object> prepareData(LoginContext loginContext, int week, int fullDay) {
		Map<String, Object> context = new HashMap<String, Object>();
		TenantConfig config = getTenantConfigService().getTenantConfigByTenantId(loginContext.getTenantId());
		// context.put("breakfastTime", "08:30");
		// context.put("breakfastMidTime", "10:00");
		// context.put("lunchTime", "11:40");
		// context.put("snackTime", "14:30");
		// context.put("dinnerTime", "16:30");

		long typeIdX = 0;
		if (config != null) {
			typeIdX = config.getTypeId();
			context.put("tenantConfig", config);

			if (StringUtils.isNotEmpty(config.getBreakfastTime())) {
				context.put("breakfastTime", config.getBreakfastTime());
			}
			if (StringUtils.isNotEmpty(config.getBreakfastMidTime())) {
				context.put("breakfastMidTime", config.getBreakfastMidTime());
			}
			if (StringUtils.isNotEmpty(config.getLunchTime())) {
				context.put("lunchTime", config.getLunchTime());
			}
			if (StringUtils.isNotEmpty(config.getSnackTime())) {
				context.put("snackTime", config.getSnackTime());
			}
			if (StringUtils.isNotEmpty(config.getDinnerTime())) {
				context.put("dinnerTime", config.getDinnerTime());
			}
		}

		FoodDRI foodDRI = null;
		FoodDRIPercent foodDRIPercent = null;
		if (typeIdX > 0) {
			foodDRI = getFoodDRIService().getFoodDRIByAge(4);
			foodDRIPercent = getFoodDRIPercentService().getFoodDRIPercent("3-6", typeIdX);
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
				context.put("foodDRI", foodDRI);
				context.put("foodDRIPercent", foodDRIPercent);
			}
		}

		DietaryCountQuery xquery = new DietaryCountQuery();
		xquery.tenantId(loginContext.getTenantId());
		xquery.fullDay(fullDay);
		xquery.type("DAY");

		List<DietaryCount> countList = getDietaryCountService().list(xquery);

		if (countList != null && !countList.isEmpty()) {
			double heatEnergy = 0.0;
			double heatEnergyCarbohydrate = 0.0;
			double heatEnergyFat = 0.0;
			double protein = 0.0;
			double proteinAnimal = 0.0;
			double proteinAnimalBeans = 0.0;
			double fat = 0.0;
			double carbohydrate = 0.0;
			double vitaminA = 0.0;
			double vitaminB1 = 0.0;
			double vitaminB2 = 0.0;
			double vitaminC = 0.0;
			double vitaminE = 0.0;
			double calcium = 0.0;
			double iron = 0.0;
			double zinc = 0.0;
			double iodine = 0.0;
			double phosphorus = 0.0;
			double nicotinicCid = 0.0;

			for (DietaryCount cnt : countList) {
				heatEnergy = heatEnergy + cnt.getHeatEnergy();
				heatEnergyCarbohydrate = heatEnergyCarbohydrate + cnt.getHeatEnergyCarbohydrate();
				heatEnergyFat = heatEnergyFat + cnt.getHeatEnergyFat();
				protein = protein + cnt.getProtein();
				proteinAnimal = proteinAnimal + cnt.getProteinAnimal();
				proteinAnimalBeans = proteinAnimalBeans + cnt.getProteinAnimalBeans();
				fat = fat + cnt.getFat();
				carbohydrate = carbohydrate + cnt.getCarbohydrate();
				vitaminA = vitaminA + cnt.getVitaminA();
				vitaminB1 = vitaminB1 + cnt.getVitaminB1();
				vitaminB2 = vitaminB2 + cnt.getVitaminB2();
				vitaminC = vitaminC + cnt.getVitaminC();
				vitaminE = vitaminE + cnt.getVitaminE();
				calcium = calcium + cnt.getCalcium();
				iron = iron + cnt.getIron();
				zinc = zinc + cnt.getZinc();
				iodine = iodine + cnt.getIodine();
				nicotinicCid = nicotinicCid + cnt.getNicotinicCid();
				phosphorus = phosphorus + cnt.getPhosphorus();
			}

			for (DietaryCount dietaryCount : countList) {
				DietaryCount model = new DietaryCount();
				try {
					PropertyUtils.copyProperties(model, dietaryCount);
				} catch (Exception ex) {
				}
				context.put("dietaryCount_" + dietaryCount.getMealType(), model);
			}

			for (DietaryCount dietaryCount : countList) {

				DietaryCount model = new DietaryCount();
				try {
					PropertyUtils.copyProperties(model, dietaryCount);
				} catch (Exception ex) {
				}

				if (heatEnergy > 0) {
					model.setHeatEnergy(dietaryCount.getHeatEnergy() / heatEnergy * 100D);
				}

				if (protein > 0) {
					model.setProtein(dietaryCount.getProtein() / protein * 100D);
				}

				if (fat > 0) {
					model.setFat(dietaryCount.getFat() / fat * 100D);
				}

				if (carbohydrate > 0) {
					model.setCarbohydrate(dietaryCount.getCarbohydrate() / carbohydrate * 100D);
				}

				if (calcium > 0) {
					model.setCalcium(dietaryCount.getCalcium() / calcium * 100D);
				}
				context.put("dietaryCountPercent_" + model.getMealType(), model);
			}

			DietaryCount dietaryCount = new DietaryCount();
			dietaryCount.setHeatEnergy(heatEnergy);
			dietaryCount.setHeatEnergyCarbohydrate(heatEnergyCarbohydrate);
			dietaryCount.setHeatEnergyFat(heatEnergyFat);
			dietaryCount.setProtein(protein);
			dietaryCount.setProteinAnimal(proteinAnimal);
			dietaryCount.setProteinAnimalBeans(proteinAnimalBeans);
			dietaryCount.setFat(fat);
			dietaryCount.setCarbohydrate(carbohydrate);
			dietaryCount.setVitaminA(vitaminA);
			dietaryCount.setVitaminB1(vitaminB1);
			dietaryCount.setVitaminB2(vitaminB2);
			dietaryCount.setVitaminC(vitaminC);
			dietaryCount.setVitaminE(vitaminE);
			dietaryCount.setCalcium(calcium);
			dietaryCount.setIron(iron);
			dietaryCount.setZinc(zinc);
			dietaryCount.setIodine(iodine);
			dietaryCount.setPhosphorus(phosphorus);
			dietaryCount.setNicotinicCid(nicotinicCid);
			context.put("dietaryCountSum", dietaryCount);

			DietaryQuery query = new DietaryQuery();
			query.tenantId(loginContext.getTenantId());
			query.fullDay(fullDay);

			FoodCompositionQuery queryx = new FoodCompositionQuery();
			queryx.locked(0);
			List<FoodComposition> foods = getFoodCompositionService().list(queryx);
			Map<Long, Long> typeIdMap = new HashMap<Long, Long>();
			for (FoodComposition fd : foods) {
				typeIdMap.put(fd.getId(), fd.getNodeId());
			}

			List<Dietary> list = getDietaryService().list(query);
			if (list != null && !list.isEmpty()) {
				List<Long> dietaryIds = new ArrayList<Long>();
				for (Dietary dietary : list) {
					dietaryIds.add(dietary.getId());
				}
				DietaryItemQuery query2 = new DietaryItemQuery();
				query2.dietaryIds(dietaryIds);
				query2.tenantId(loginContext.getTenantId());
				List<DietaryItem> allItems = getDietaryItemService().list(query2);
				if (allItems != null && !allItems.isEmpty()) {

					List<DietaryItem> items = null;

					Map<Long, List<DietaryItem>> dietaryMap = new HashMap<Long, List<DietaryItem>>();
					for (DietaryItem item : allItems) {
						items = dietaryMap.get(item.getDietaryId());
						if (items == null) {
							items = new ArrayList<DietaryItem>();
						}
						item.setTypeId(typeIdMap.get(item.getFoodId()));
						items.add(item);
						dietaryMap.put(item.getDietaryId(), items);
					}

					for (Dietary dietary : list) {
						items = dietaryMap.get(dietary.getId());
						dietary.setItems(items);
					}

					List<DietaryRptModel> breakfastList = new ArrayList<DietaryRptModel>();
					List<DietaryRptModel> breakfastMidList = new ArrayList<DietaryRptModel>();
					List<DietaryRptModel> lunchList = new ArrayList<DietaryRptModel>();
					List<DietaryRptModel> snackList = new ArrayList<DietaryRptModel>();
					List<DietaryRptModel> dinnerList = new ArrayList<DietaryRptModel>();

					for (Dietary dietary : list) {
						items = dietaryMap.get(dietary.getId());
						if (items != null && !items.isEmpty()) {
							long typeId = dietary.getTypeId();

							DietaryRptModel model = new DietaryRptModel();
							model.setName(dietary.getName());
							model.setItems(items);
							model.setHeatEnergy(dietary.getHeatEnergy());
							model.setCarbohydrate(dietary.getCarbohydrate());
							model.setProtein(dietary.getProtein());
							model.setFat(dietary.getFat());
							model.setCalcium(dietary.getCalcium());

							if (typeId == 3302 || typeId == 3312 || typeId == 3402 || typeId == 3412) {
								breakfastList.add(model);
							}
							if (typeId == 3403 || typeId == 3413) {
								breakfastMidList.add(model);
							}
							if (typeId == 3303 || typeId == 3313 || typeId == 3323 || typeId == 3313 || typeId == 3404
									|| typeId == 3414) {
								lunchList.add(model);
							}
							if (typeId == 3304 || typeId == 3314 || typeId == 3324 || typeId == 3405
									|| typeId == 3415) {
								snackList.add(model);
							}
							if (typeId == 3305 || typeId == 3406) {
								dinnerList.add(model);
							}
						}
					}

					DietaryDayRptModel dayRptModel = new DietaryDayRptModel();
					dayRptModel.setBreakfastList(breakfastList);
					dayRptModel.setBreakfastMidList(breakfastMidList);
					dayRptModel.setLunchList(lunchList);
					dayRptModel.setSnackList(snackList);
					dayRptModel.setDinnerList(dinnerList);

					this.populate(dayRptModel, foodDRI);
					this.populate(dayRptModel, dietaryCount, foodDRI, foodDRIPercent);

					if (foodDRI.getHeatEnergy() > 0) {
						dayRptModel.setHeatEnergyPercent(dietaryCount.getHeatEnergy() / foodDRI.getHeatEnergy() * 100D);
					}

					if (foodDRI.getProtein() > 0) {
						dayRptModel.setProteinPercent(dietaryCount.getProtein() / foodDRI.getProtein() * 100D);
					}

					if (foodDRI.getCarbohydrate() > 0) {
						dayRptModel.setCarbohydratePercent(
								dietaryCount.getCarbohydrate() / foodDRI.getCarbohydrate() * 100D);
					}

					if (foodDRI.getFat() > 0) {
						dayRptModel.setFatPercent(dietaryCount.getFat() / foodDRI.getFat() * 100D);
					}

					if (foodDRI.getCalcium() > 0) {
						dayRptModel.setCalciumPercent(dietaryCount.getCalcium() / foodDRI.getCalcium() * 100D);
					}

					context.put("breakfastList", breakfastList);
					context.put("breakfastMidList", breakfastMidList);
					context.put("lunchList", lunchList);
					context.put("snackList", snackList);
					context.put("dinnerList", dinnerList);
					context.put("dayRptModel", dayRptModel);
					context.put("foodDRI", foodDRI);

				}
			}
		}
		return context;
	}

	public void setDietaryCountService(DietaryCountService dietaryCountService) {
		this.dietaryCountService = dietaryCountService;
	}

	public void setDietaryItemService(DietaryItemService dietaryItemService) {
		this.dietaryItemService = dietaryItemService;
	}

	public void setDietaryService(DietaryService dietaryService) {
		this.dietaryService = dietaryService;
	}

	public void setFoodCompositionService(FoodCompositionService foodCompositionService) {
		this.foodCompositionService = foodCompositionService;
	}

	public void setFoodDRIPercentService(FoodDRIPercentService foodDRIPercentService) {
		this.foodDRIPercentService = foodDRIPercentService;
	}

	public void setFoodDRIService(FoodDRIService foodDRIService) {
		this.foodDRIService = foodDRIService;
	}

	public void setTenantConfigService(TenantConfigService tenantConfigService) {
		this.tenantConfigService = tenantConfigService;
	}

}
