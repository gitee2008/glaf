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

import org.apache.commons.lang.StringUtils;

import com.glaf.base.modules.sys.service.TenantConfigService;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.security.LoginContext;

import com.glaf.heathcare.domain.DietaryItem;
import com.glaf.heathcare.domain.DietaryRptModel;
import com.glaf.heathcare.domain.DietaryCount;
import com.glaf.heathcare.domain.DietaryDayRptModel;
import com.glaf.heathcare.domain.DietaryTemplate;
import com.glaf.heathcare.domain.FoodDRI;
import com.glaf.heathcare.domain.FoodDRIPercent;
import com.glaf.heathcare.query.DietaryItemQuery;
import com.glaf.heathcare.query.DietaryTemplateQuery;
import com.glaf.heathcare.service.DietaryItemService;
import com.glaf.heathcare.service.DietaryTemplateService;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.heathcare.service.FoodDRIPercentService;
import com.glaf.heathcare.service.FoodDRIService;

public class DayDietaryTemplateExportBean {

	protected TenantConfigService tenantConfigService;

	protected DietaryTemplateService dietaryTemplateService;

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

	public Map<String, Object> prepareData(LoginContext loginContext, int suitNo, int dayOfWeek, long typeIdX,
			String sysFlag, Map<String, Object> params) {
		Map<String, Object> context = new HashMap<String, Object>();

		DietaryTemplateQuery query = new DietaryTemplateQuery();
		if (StringUtils.equals(sysFlag, "Y")) {
			query.sysFlag(sysFlag);
		} else {
			query.tenantId(loginContext.getTenantId());
		}
		query.suitNo(suitNo);
		query.dayOfWeek(dayOfWeek);

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

		List<DietaryTemplate> list = null;
		if (StringUtils.equals(sysFlag, "Y")) {
			list = getDietaryTemplateService().getDietaryTemplates(dayOfWeek, suitNo, sysFlag);
		} else {
			list = getDietaryTemplateService().list(query);
		}

		if (list != null && !list.isEmpty()) {
			List<Long> templateIds = new ArrayList<Long>();
			for (DietaryTemplate dietaryTemplate : list) {
				templateIds.add(dietaryTemplate.getId());
			}

			DietaryItemQuery query2 = new DietaryItemQuery();
			query2.templateIds(templateIds);
			List<DietaryItem> allItems = getDietaryItemService().list(query2);
			if (allItems != null && !allItems.isEmpty()) {

				DietaryRptModel model = null;
				List<DietaryItem> items = null;

				Map<Long, List<DietaryItem>> dietaryTemplateMap = new HashMap<Long, List<DietaryItem>>();
				for (DietaryItem item : allItems) {
					items = dietaryTemplateMap.get(item.getTemplateId());
					if (items == null) {
						items = new ArrayList<DietaryItem>();
					}
					items.add(item);
					dietaryTemplateMap.put(item.getTemplateId(), items);
				}

				List<DietaryRptModel> breakfastList = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> breakfastMidList = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> lunchList = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> snackList = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> dinnerList = new ArrayList<DietaryRptModel>();

				DietaryDayRptModel dayRptModel = new DietaryDayRptModel();
				dayRptModel.setBreakfastList(breakfastList);
				dayRptModel.setBreakfastMidList(breakfastMidList);
				dayRptModel.setLunchList(lunchList);
				dayRptModel.setSnackList(snackList);
				dayRptModel.setDinnerList(dinnerList);

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

				for (DietaryTemplate template : list) {
					items = dietaryTemplateMap.get(template.getId());
					if (items != null && !items.isEmpty()) {
						model = new DietaryRptModel();
						model.setDietaryTemplate(template);
						model.setName(template.getName());
						model.setItems(items);
						model.setCalcium(template.getCalcium());
						model.setCarbohydrate(template.getCarbohydrate());
						model.setHeatEnergy(template.getHeatEnergy());
						model.setProtein(template.getProtein());
						model.setFat(template.getFat());

						long typeId = template.getTypeId();

						if (typeId == 3302 || typeId == 3312 || typeId == 3402 || typeId == 3412) {
							breakfastList.add(model);
							carbohydrateMomingTotal = carbohydrateMomingTotal + template.getCarbohydrate();
							heatEnergyMomingTotal = heatEnergyMomingTotal + template.getHeatEnergy();
							calciumMomingTotal = calciumMomingTotal + template.getCalcium();
							proteinMomingTotal = proteinMomingTotal + template.getProtein();
							fatMomingTotal = fatMomingTotal + template.getFat();
						}
						if (typeId == 3403 || typeId == 3413) {
							breakfastMidList.add(model);
							carbohydrateMomingTotal = carbohydrateMomingTotal + template.getCarbohydrate();
							heatEnergyMomingTotal = heatEnergyMomingTotal + template.getHeatEnergy();
							calciumMomingTotal = calciumMomingTotal + template.getCalcium();
							proteinMomingTotal = proteinMomingTotal + template.getProtein();
							fatMomingTotal = fatMomingTotal + template.getFat();
						}
						if (typeId == 3303 || typeId == 3313 || typeId == 3323 || typeId == 3313 || typeId == 3404
								|| typeId == 3414) {
							lunchList.add(model);
							carbohydrateNoonTotal = carbohydrateNoonTotal + template.getCarbohydrate();
							heatEnergyNoonTotal = heatEnergyNoonTotal + template.getHeatEnergy();
							calciumNoonTotal = calciumNoonTotal + template.getCalcium();
							proteinNoonTotal = proteinNoonTotal + template.getProtein();
							fatNoonTotal = fatNoonTotal + template.getFat();
						}
						if (typeId == 3304 || typeId == 3314 || typeId == 3324 || typeId == 3405 || typeId == 3415) {
							snackList.add(model);
							carbohydrateNoonTotal = carbohydrateNoonTotal + template.getCarbohydrate();
							heatEnergyNoonTotal = heatEnergyNoonTotal + template.getHeatEnergy();
							calciumNoonTotal = calciumNoonTotal + template.getCalcium();
							proteinNoonTotal = proteinNoonTotal + template.getProtein();
							fatNoonTotal = fatNoonTotal + template.getFat();
						}
						if (typeId == 3305 || typeId == 3406) {
							dinnerList.add(model);
							carbohydrateDinnerTotal = carbohydrateDinnerTotal + template.getCarbohydrate();
							heatEnergyDinnerTotal = heatEnergyDinnerTotal + template.getHeatEnergy();
							calciumDinnerTotal = calciumDinnerTotal + template.getCalcium();
							proteinDinnerTotal = proteinDinnerTotal + template.getProtein();
							fatDinnerTotal = fatDinnerTotal + template.getFat();
						}

						carbohydrateTotal = carbohydrateTotal + template.getCarbohydrate();
						heatEnergyTotal = heatEnergyTotal + template.getHeatEnergy();
						calciumTotal = calciumTotal + template.getCalcium();
						proteinTotal = proteinTotal + template.getProtein();
						fatTotal = fatTotal + template.getFat();

					}
				}

				if (breakfastList.size() > 0) {
					double carbohydrate = 0.0;
					double heatEnergy = 0.0;
					double calcium = 0.0;
					double protein = 0.0;
					double fat = 0.0;

					for (DietaryRptModel rptModel : breakfastList) {
						DietaryTemplate template = rptModel.getDietaryTemplate();
						carbohydrate = carbohydrate + template.getCarbohydrate();
						heatEnergy = heatEnergy + template.getHeatEnergy();
						calcium = calcium + template.getCalcium();
						protein = protein + template.getProtein();
						fat = fat + template.getFat();
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
						DietaryTemplate template = rptModel.getDietaryTemplate();
						carbohydrate = carbohydrate + template.getCarbohydrate();
						heatEnergy = heatEnergy + template.getHeatEnergy();
						calcium = calcium + template.getCalcium();
						protein = protein + template.getProtein();
						fat = fat + template.getFat();
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
						DietaryTemplate template = rptModel.getDietaryTemplate();
						carbohydrate = carbohydrate + template.getCarbohydrate();
						heatEnergy = heatEnergy + template.getHeatEnergy();
						calcium = calcium + template.getCalcium();
						protein = protein + template.getProtein();
						fat = fat + template.getFat();
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
						DietaryTemplate template = rptModel.getDietaryTemplate();
						carbohydrate = carbohydrate + template.getCarbohydrate();
						heatEnergy = heatEnergy + template.getHeatEnergy();
						calcium = calcium + template.getCalcium();
						protein = protein + template.getProtein();
						fat = fat + template.getFat();
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
						DietaryTemplate template = rptModel.getDietaryTemplate();
						carbohydrate = carbohydrate + template.getCarbohydrate();
						heatEnergy = heatEnergy + template.getHeatEnergy();
						calcium = calcium + template.getCalcium();
						protein = protein + template.getProtein();
						fat = fat + template.getFat();
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

				if (foodDRI.getHeatEnergy() > 0) {
					dayRptModel.setHeatEnergyPercent(heatEnergyTotal / foodDRI.getHeatEnergy() * 100D);
				}

				if (foodDRI.getProtein() > 0) {
					dayRptModel.setProteinPercent(proteinTotal / foodDRI.getProtein() * 100D);
				}

				if (foodDRI.getCarbohydrate() > 0) {
					dayRptModel.setCarbohydratePercent(carbohydrateTotal / foodDRI.getCarbohydrate() * 100D);
				}

				if (foodDRI.getFat() > 0) {
					dayRptModel.setFatPercent(fatTotal / foodDRI.getFat() * 100D);
				}

				if (foodDRI.getCalcium() > 0) {
					dayRptModel.setCalciumPercent(calciumTotal / foodDRI.getCalcium() * 100D);
				}

				DietaryCount dietaryCountSum = new DietaryCount();
				dietaryCountSum.setCalcium(calciumTotal);
				dietaryCountSum.setCarbohydrate(carbohydrateTotal);
				dietaryCountSum.setFat(fatTotal);
				dietaryCountSum.setProtein(proteinTotal);
				dietaryCountSum.setHeatEnergy(heatEnergyTotal);

				this.populate(dayRptModel, dietaryCountSum, foodDRI, foodDRIPercent);

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
			}
		}
		return context;
	}

	public void setDietaryItemService(DietaryItemService dietaryItemService) {
		this.dietaryItemService = dietaryItemService;
	}

	public void setDietaryTemplateService(DietaryTemplateService dietaryTemplateService) {
		this.dietaryTemplateService = dietaryTemplateService;
	}

	public void setTenantConfigService(TenantConfigService tenantConfigService) {
		this.tenantConfigService = tenantConfigService;
	}

}
