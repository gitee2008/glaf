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

package com.glaf.heathcare.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.base.TableModel;
import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.service.ITableDataService;
import com.glaf.core.util.DBUtils;

import com.glaf.heathcare.domain.DietaryCount;
import com.glaf.heathcare.domain.DietaryDayRptModel;
import com.glaf.heathcare.domain.DietaryItem;
import com.glaf.heathcare.domain.DietaryRptModel;
import com.glaf.heathcare.domain.DietaryStatistics;
import com.glaf.heathcare.domain.DietaryTemplate;
import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.mapper.DietaryStatisticsMapper;
import com.glaf.heathcare.query.DietaryItemQuery;
import com.glaf.heathcare.query.DietaryStatisticsQuery;
import com.glaf.heathcare.query.DietaryTemplateQuery;
import com.glaf.heathcare.query.FoodCompositionQuery;
import com.glaf.heathcare.service.DietaryItemService;
import com.glaf.heathcare.service.DietaryStatisticsService;
import com.glaf.heathcare.service.DietaryTemplateService;
import com.glaf.heathcare.service.FoodCompositionService;

@Service("com.glaf.heathcare.service.dietaryStatisticsService")
@Transactional(readOnly = true)
public class DietaryStatisticsServiceImpl implements DietaryStatisticsService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected DietaryStatisticsMapper dietaryStatisticsMapper;

	protected DietaryTemplateService dietaryTemplateService;

	protected DietaryItemService dietaryItemService;

	protected FoodCompositionService foodCompositionService;

	protected ITableDataService tableDataService;

	public DietaryStatisticsServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<DietaryStatistics> list) {
		for (DietaryStatistics dietaryStatistics : list) {
			if (dietaryStatistics.getId() == 0) {
				dietaryStatistics.setId(idGenerator.nextId("HEALTH_DIETARY_STATISTICS"));
				dietaryStatistics.setCreateTime(new Date());
			}
		}

		int batch_size = 100;
		List<DietaryStatistics> rows = new ArrayList<DietaryStatistics>(batch_size);

		for (DietaryStatistics bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					dietaryStatisticsMapper.bulkInsertDietaryStatistics_oracle(rows);
				} else {
					dietaryStatisticsMapper.bulkInsertDietaryStatistics(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				dietaryStatisticsMapper.bulkInsertDietaryStatistics_oracle(rows);
			} else {
				dietaryStatisticsMapper.bulkInsertDietaryStatistics(rows);
			}
			rows.clear();
		}
	}

	public int count(DietaryStatisticsQuery query) {
		return dietaryStatisticsMapper.getDietaryStatisticsCount(query);
	}

	@Transactional
	public void deleteById(Long id) {
		if (id != null) {
			dietaryStatisticsMapper.deleteDietaryStatisticsById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				dietaryStatisticsMapper.deleteDietaryStatisticsById(id);
			}
		}
	}

	@Transactional
	public void deleteDietaryStatisticsByBatchNo(String batchNo) {
		dietaryStatisticsMapper.deleteDietaryStatisticsByBatchNo(batchNo);
	}

	@Transactional
	public void execute(LoginContext loginContext, int suitNo, int dayOfWeek, String sysFlag) {
		Map<String, Object> context = new HashMap<String, Object>();

		DietaryTemplateQuery query = new DietaryTemplateQuery();
		if (StringUtils.equals(sysFlag, "Y")) {
			query.sysFlag(sysFlag);
		} else {
			query.tenantId(loginContext.getTenantId());
		}
		query.suitNo(suitNo);
		query.dayOfWeek(dayOfWeek);

		List<DietaryTemplate> list = dietaryTemplateService.list(query);
		if (list != null && !list.isEmpty()) {
			List<Long> templateIds = new ArrayList<Long>();
			for (DietaryTemplate dietaryTemplate : list) {
				// dietaryTemplateService.calculate(dietaryTemplate.getId());
				templateIds.add(dietaryTemplate.getId());
			}

			FoodCompositionQuery queryx = new FoodCompositionQuery();
			List<FoodComposition> foods = foodCompositionService.list(queryx);
			Map<Long, Long> foodMap = new HashMap<Long, Long>();
			Map<Long, FoodComposition> foodXMap = new HashMap<Long, FoodComposition>();
			for (FoodComposition food : foods) {
				foodMap.put(food.getId(), food.getNodeId());
				foodXMap.put(food.getId(), food);
			}

			DietaryItemQuery query2 = new DietaryItemQuery();
			query2.templateIds(templateIds);
			List<DietaryItem> allItems = dietaryItemService.list(query2);
			if (allItems != null && !allItems.isEmpty()) {

				FoodComposition food = null;
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
							heatEnergyMoming = heatEnergyMoming + template.getHeatEnergy();
						}
						if (typeId == 3403 || typeId == 3413) {
							breakfastMidList.add(model);
							carbohydrateMomingTotal = carbohydrateMomingTotal + template.getCarbohydrate();
							heatEnergyMomingTotal = heatEnergyMomingTotal + template.getHeatEnergy();
							calciumMomingTotal = calciumMomingTotal + template.getCalcium();
							proteinMomingTotal = proteinMomingTotal + template.getProtein();
							fatMomingTotal = fatMomingTotal + template.getFat();
							heatEnergyMoming = heatEnergyMoming + template.getHeatEnergy();
						}
						if (typeId == 3303 || typeId == 3313 || typeId == 3323 || typeId == 3313 || typeId == 3404
								|| typeId == 3414) {
							lunchList.add(model);
							carbohydrateNoonTotal = carbohydrateNoonTotal + template.getCarbohydrate();
							heatEnergyNoonTotal = heatEnergyNoonTotal + template.getHeatEnergy();
							calciumNoonTotal = calciumNoonTotal + template.getCalcium();
							proteinNoonTotal = proteinNoonTotal + template.getProtein();
							fatNoonTotal = fatNoonTotal + template.getFat();
							heatEnergyNoon = heatEnergyNoon + template.getHeatEnergy();
						}
						if (typeId == 3304 || typeId == 3314 || typeId == 3324 || typeId == 3405 || typeId == 3415) {
							snackList.add(model);
							carbohydrateNoonTotal = carbohydrateNoonTotal + template.getCarbohydrate();
							heatEnergyNoonTotal = heatEnergyNoonTotal + template.getHeatEnergy();
							calciumNoonTotal = calciumNoonTotal + template.getCalcium();
							proteinNoonTotal = proteinNoonTotal + template.getProtein();
							fatNoonTotal = fatNoonTotal + template.getFat();
							heatEnergyNoon = heatEnergyNoon + template.getHeatEnergy();
						}
						if (typeId == 3305 || typeId == 3406) {
							dinnerList.add(model);
							carbohydrateDinnerTotal = carbohydrateDinnerTotal + template.getCarbohydrate();
							heatEnergyDinnerTotal = heatEnergyDinnerTotal + template.getHeatEnergy();
							calciumDinnerTotal = calciumDinnerTotal + template.getCalcium();
							proteinDinnerTotal = proteinDinnerTotal + template.getProtein();
							fatDinnerTotal = fatDinnerTotal + template.getFat();
							heatEnergyDinner = heatEnergyDinner + template.getHeatEnergy();
						}

						carbohydrateTotal = carbohydrateTotal + template.getCarbohydrate();
						heatEnergyTotal = heatEnergyTotal + template.getHeatEnergy();
						calciumTotal = calciumTotal + template.getCalcium();
						proteinTotal = proteinTotal + template.getProtein();
						fatTotal = fatTotal + template.getFat();

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
									proteinOthers = proteinOthers + food.getProtein() * item.getQuantity() / 100.0D;
									heatEnergyPlant = heatEnergyPlant
											+ food.getHeatEnergy() * item.getQuantity() / 100.0D;
									break;
								case 4404:// 豆类
									heatEnergyBeans = heatEnergyBeans
											+ food.getHeatEnergy() * item.getQuantity() / 100.0D;
									proteinBeans = proteinBeans + food.getProtein() * item.getQuantity() / 100.0D;
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
									proteinAnimals = proteinAnimals + food.getProtein() * item.getQuantity() / 100.0D;
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
									proteinOthers = proteinOthers + food.getProtein() * item.getQuantity() / 100.0D;
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
				m1.setType("heatEnergyPerDietary");
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
				m11.setType("heatEnergyPercentPerDietary");
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
				m2.setType("heatEnergyPerDietary");
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
				m22.setType("heatEnergyPercentPerDietary");
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
				m3.setType("heatEnergyPerDietary");
				m3.setTitle("动物性食物");
				m3.setName("heatEnergyAnimals");
				m3.setValue(Math.round(heatEnergyAnimals));
				m3.setSortNo(2);
				rows.add(m3);

				DietaryStatistics m33 = new DietaryStatistics();
				m33.setTenantId(query.getTenantId());
				m33.setSysFlag(sysFlag);
				m33.setSuitNo(suitNo);
				m33.setDayOfWeek(dayOfWeek);
				m33.setType("heatEnergyPercentPerDietary");
				m33.setTitle("动物性食物");
				m33.setName("heatEnergyAnimalsPercent");
				m33.setValue(Math.round(heatEnergyAnimals / tt * 100D));
				m33.setSortNo(2);
				rows.add(m33);

				DietaryStatistics m4 = new DietaryStatistics();
				m4.setTenantId(query.getTenantId());
				m4.setSysFlag(sysFlag);
				m4.setSuitNo(suitNo);
				m4.setDayOfWeek(dayOfWeek);
				m4.setType("heatEnergyPerDietary");
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
				m44.setType("heatEnergyPercentPerDietary");
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
				mp22.setType("proteinPercentPerDietary");
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
				mp3.setType("proteinPerDietary");
				mp3.setTitle("动物性食物");
				mp3.setName("proteinAnimals");
				mp3.setValue(Math.round(proteinAnimals));
				mp3.setSortNo(2);
				rows.add(mp3);

				DietaryStatistics mp33 = new DietaryStatistics();
				mp33.setTenantId(query.getTenantId());
				mp33.setSysFlag(sysFlag);
				mp33.setSuitNo(suitNo);
				mp33.setDayOfWeek(dayOfWeek);
				mp33.setType("proteinPercentPerDietary");
				mp33.setTitle("动物性食物");
				mp33.setName("proteinAnimalsPercent");
				mp33.setValue(Math.round(proteinAnimals / pp * 100D));
				mp33.setSortNo(2);
				rows.add(mp33);

				DietaryStatistics mp4 = new DietaryStatistics();
				mp4.setTenantId(query.getTenantId());
				mp4.setSysFlag(sysFlag);
				mp4.setSuitNo(suitNo);
				mp4.setDayOfWeek(dayOfWeek);
				mp4.setType("proteinPerDietary");
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
				mp44.setType("proteinPercentPerDietary");
				mp44.setTitle("谷、薯、杂豆及其他");
				mp44.setName("proteinOthersPercent");
				mp44.setValue(Math.round(proteinOthers / pp * 100D));
				mp44.setSortNo(3);
				rows.add(mp44);

				tt = heatEnergyAnimal + heatEnergyPlant;

				if (tt > 0) {
					DietaryStatistics mx1 = new DietaryStatistics();
					mx1.setTenantId(query.getTenantId());
					mx1.setSysFlag(sysFlag);
					mx1.setSuitNo(suitNo);
					mx1.setDayOfWeek(dayOfWeek);
					mx1.setType("heatEnergyX1PerDietary");
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
					mx11.setType("heatEnergyX1PercentPerDietary");
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
					mx2.setType("heatEnergyX1PerDietary");
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
					mx22.setType("heatEnergyX1PercentPerDietary");
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
					mx1.setType("heatEnergyX2PerDietary");
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
					mx2.setType("heatEnergyX2PerDietary");
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
					mx3.setType("heatEnergyX2PerDietary");
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
					mx11.setType("heatEnergyX2PercentPerDietary");
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
					mx12.setType("heatEnergyX2PercentPerDietary");
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
					mx13.setType("heatEnergyX2PercentPerDietary");
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
					nx1.setType("heatEnergyX3PerDietary");
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
					nx2.setType("heatEnergyX3PerDietary");
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
					nx3.setType("heatEnergyX3PerDietary");
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
						nx11.setType("heatEnergyX3PercentPerDietary");
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
						nx12.setType("heatEnergyX3PercentPerDietary");
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
						nx13.setType("heatEnergyX3PercentPerDietary");
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
						nx11.setType("heatEnergyX3PercentPerDietary");
						nx11.setTitle("早餐");
						nx11.setName("heatEnergyMoming");
						nx11.setValue(Math.round(heatEnergyMoming / tt * 70));
						nx11.setSortNo(1);
						rows.add(nx11);

						DietaryStatistics nx12 = new DietaryStatistics();
						nx12.setTenantId(query.getTenantId());
						nx12.setSysFlag(sysFlag);
						nx12.setSuitNo(suitNo);
						nx12.setDayOfWeek(dayOfWeek);
						nx12.setType("heatEnergyX3PercentPerDietary");
						nx12.setTitle("中餐");
						nx12.setName("heatEnergyNoon");
						nx12.setValue(Math.round(heatEnergyNoon / tt * 70D));
						nx12.setSortNo(2);
						rows.add(nx12);

						DietaryStatistics nx13 = new DietaryStatistics();
						nx13.setTenantId(query.getTenantId());
						nx13.setSysFlag(sysFlag);
						nx13.setSuitNo(suitNo);
						nx13.setDayOfWeek(dayOfWeek);
						nx13.setType("heatEnergyX3PercentPerDietary");
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
					mx15.setType("vitaminAAnimalsX3PercentPerDayDietary");
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
					mx16.setType("vitaminAAnimalsX3PercentPerDayDietary");
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
					mx15.setType("ironAnimalsX3PercentPerDayDietary");
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
					mx16.setType("ironAnimalsX3PercentPerDayDietary");
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
					mx15.setType("fatAnimalsX3PercentPerDayDietary");
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
					mx16.setType("fatAnimalsX3PercentPerDayDietary");
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
					mx15.setType("calciumPhosphorusX3PercentPerDayDietary");
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
					mx16.setType("calciumPhosphorusX3PercentPerDayDietary");
					mx16.setTitle("钙占比");
					mx16.setName("calciumPercent");
					mx16.setValue(100 - mx15.getValue());
					mx16.setSortNo(3);
					rows.add(mx16);
				}

				saveAll(query.getTenantId(), sysFlag, suitNo, dayOfWeek, rows);

			}
		}
	}

	@Transactional
	public void execute(LoginContext loginContext, int suitNo, String sysFlag) {
		Map<String, Object> context = new HashMap<String, Object>();

		DietaryTemplateQuery query = new DietaryTemplateQuery();
		if (StringUtils.equals(sysFlag, "Y")) {
			query.sysFlag(sysFlag);
		} else {
			query.tenantId(loginContext.getTenantId());
		}
		query.suitNo(suitNo);

		List<DietaryTemplate> list = dietaryTemplateService.list(query);
		if (list != null && !list.isEmpty()) {
			List<Long> templateIds = new ArrayList<Long>();
			for (DietaryTemplate dietaryTemplate : list) {
				// dietaryTemplateService.calculate(dietaryTemplate.getId());
				templateIds.add(dietaryTemplate.getId());
			}

			FoodCompositionQuery queryx = new FoodCompositionQuery();
			List<FoodComposition> foods = foodCompositionService.list(queryx);
			Map<Long, Long> foodMap = new HashMap<Long, Long>();
			Map<Long, FoodComposition> foodXMap = new HashMap<Long, FoodComposition>();
			for (FoodComposition food : foods) {
				foodMap.put(food.getId(), food.getNodeId());
				foodXMap.put(food.getId(), food);
			}

			DietaryItemQuery query2 = new DietaryItemQuery();
			query2.templateIds(templateIds);
			List<DietaryItem> allItems = dietaryItemService.list(query2);
			if (allItems != null && !allItems.isEmpty()) {

				FoodComposition food = null;
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
							heatEnergyMoming = heatEnergyMoming + template.getHeatEnergy();
						}
						if (typeId == 3403 || typeId == 3413) {
							breakfastMidList.add(model);
							carbohydrateMomingTotal = carbohydrateMomingTotal + template.getCarbohydrate();
							heatEnergyMomingTotal = heatEnergyMomingTotal + template.getHeatEnergy();
							calciumMomingTotal = calciumMomingTotal + template.getCalcium();
							proteinMomingTotal = proteinMomingTotal + template.getProtein();
							fatMomingTotal = fatMomingTotal + template.getFat();
							heatEnergyMoming = heatEnergyMoming + template.getHeatEnergy();
						}
						if (typeId == 3303 || typeId == 3313 || typeId == 3323 || typeId == 3313 || typeId == 3404
								|| typeId == 3414) {
							lunchList.add(model);
							carbohydrateNoonTotal = carbohydrateNoonTotal + template.getCarbohydrate();
							heatEnergyNoonTotal = heatEnergyNoonTotal + template.getHeatEnergy();
							calciumNoonTotal = calciumNoonTotal + template.getCalcium();
							proteinNoonTotal = proteinNoonTotal + template.getProtein();
							fatNoonTotal = fatNoonTotal + template.getFat();
							heatEnergyNoon = heatEnergyNoon + template.getHeatEnergy();
						}
						if (typeId == 3304 || typeId == 3314 || typeId == 3324 || typeId == 3405 || typeId == 3415) {
							snackList.add(model);
							carbohydrateNoonTotal = carbohydrateNoonTotal + template.getCarbohydrate();
							heatEnergyNoonTotal = heatEnergyNoonTotal + template.getHeatEnergy();
							calciumNoonTotal = calciumNoonTotal + template.getCalcium();
							proteinNoonTotal = proteinNoonTotal + template.getProtein();
							fatNoonTotal = fatNoonTotal + template.getFat();
							heatEnergyNoon = heatEnergyNoon + template.getHeatEnergy();
						}
						if (typeId == 3305 || typeId == 3406) {
							dinnerList.add(model);
							carbohydrateDinnerTotal = carbohydrateDinnerTotal + template.getCarbohydrate();
							heatEnergyDinnerTotal = heatEnergyDinnerTotal + template.getHeatEnergy();
							calciumDinnerTotal = calciumDinnerTotal + template.getCalcium();
							proteinDinnerTotal = proteinDinnerTotal + template.getProtein();
							fatDinnerTotal = fatDinnerTotal + template.getFat();
							heatEnergyDinner = heatEnergyDinner + template.getHeatEnergy();
						}

						carbohydrateTotal = carbohydrateTotal + template.getCarbohydrate();
						heatEnergyTotal = heatEnergyTotal + template.getHeatEnergy();
						calciumTotal = calciumTotal + template.getCalcium();
						proteinTotal = proteinTotal + template.getProtein();
						fatTotal = fatTotal + template.getFat();

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
									proteinOthers = proteinOthers + food.getProtein() * item.getQuantity() / 100.0D;
									heatEnergyPlant = heatEnergyPlant
											+ food.getHeatEnergy() * item.getQuantity() / 100.0D;
									break;
								case 4404:// 豆类
									heatEnergyBeans = heatEnergyBeans
											+ food.getHeatEnergy() * item.getQuantity() / 100.0D;
									proteinBeans = proteinBeans + food.getProtein() * item.getQuantity() / 100.0D;
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
									proteinAnimals = proteinAnimals + food.getProtein() * item.getQuantity() / 100.0D;
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
									proteinOthers = proteinOthers + food.getProtein() * item.getQuantity() / 100.0D;
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
				m1.setType("heatEnergyPerDietaryOfWeek");
				m1.setTitle("谷类");
				m1.setName("heatEnergyGrain");
				m1.setValue(Math.round(heatEnergyGrain));
				m1.setSortNo(4);
				rows.add(m1);

				DietaryStatistics m11 = new DietaryStatistics();
				m11.setTenantId(query.getTenantId());
				m11.setSysFlag(sysFlag);
				m11.setSuitNo(suitNo);
				m11.setType("heatEnergyPercentPerDietaryOfWeek");
				m11.setTitle("谷类");
				m11.setName("heatEnergyGrainPercent");
				m11.setValue(Math.round(heatEnergyGrain / tt * 100D));
				m11.setSortNo(4);
				rows.add(m11);

				DietaryStatistics m2 = new DietaryStatistics();
				m2.setTenantId(query.getTenantId());
				m2.setSysFlag(sysFlag);
				m2.setSuitNo(suitNo);
				m2.setType("heatEnergyPerDietaryOfWeek");
				m2.setTitle("豆类");
				m2.setName("heatEnergyBeans");
				m2.setValue(Math.round(heatEnergyBeans));
				m2.setSortNo(1);
				rows.add(m2);

				DietaryStatistics m22 = new DietaryStatistics();
				m22.setTenantId(query.getTenantId());
				m22.setSysFlag(sysFlag);
				m22.setSuitNo(suitNo);
				m22.setType("heatEnergyPercentPerDietaryOfWeek");
				m22.setTitle("豆类");
				m22.setName("heatEnergyBeansPercent");
				m22.setValue(Math.round(heatEnergyBeans / tt * 100D));
				m22.setSortNo(1);
				rows.add(m22);

				DietaryStatistics m3 = new DietaryStatistics();
				m3.setTenantId(query.getTenantId());
				m3.setSysFlag(sysFlag);
				m3.setSuitNo(suitNo);
				m3.setType("heatEnergyPerDietaryOfWeek");
				m3.setTitle("动物性食物");
				m3.setName("heatEnergyAnimals");
				m3.setValue(Math.round(heatEnergyAnimals));
				m3.setSortNo(2);
				rows.add(m3);

				DietaryStatistics m33 = new DietaryStatistics();
				m33.setTenantId(query.getTenantId());
				m33.setSysFlag(sysFlag);
				m33.setSuitNo(suitNo);
				m33.setType("heatEnergyPercentPerDietaryOfWeek");
				m33.setTitle("动物性食物");
				m33.setName("heatEnergyAnimalsPercent");
				m33.setValue(Math.round(heatEnergyAnimals / tt * 100D));
				m33.setSortNo(2);
				rows.add(m33);

				DietaryStatistics m4 = new DietaryStatistics();
				m4.setTenantId(query.getTenantId());
				m4.setSysFlag(sysFlag);
				m4.setSuitNo(suitNo);
				m4.setType("heatEnergyPerDietaryOfWeek");
				m4.setTitle("谷、薯、杂豆及其他");
				m4.setName("heatEnergyOthers");
				m4.setValue(Math.round(heatEnergyOthers));
				m4.setSortNo(3);
				rows.add(m4);

				DietaryStatistics m44 = new DietaryStatistics();
				m44.setTenantId(query.getTenantId());
				m44.setSysFlag(sysFlag);
				m44.setSuitNo(suitNo);
				m44.setType("heatEnergyPercentPerDietaryOfWeek");
				m44.setTitle("谷、薯、杂豆及其他");
				m44.setName("heatEnergyOthersPercent");
				m44.setValue(Math.round(heatEnergyOthers / tt * 100D));
				m44.setSortNo(3);
				rows.add(m44);

				DietaryStatistics mp2 = new DietaryStatistics();
				mp2.setTenantId(query.getTenantId());
				mp2.setSysFlag(sysFlag);
				mp2.setSuitNo(suitNo);
				mp2.setType("proteinPerDietaryOfWeek");
				mp2.setTitle("豆类");
				mp2.setName("proteinBeans");
				mp2.setValue(Math.round(proteinBeans));
				mp2.setSortNo(1);
				rows.add(mp2);

				DietaryStatistics mp22 = new DietaryStatistics();
				mp22.setTenantId(query.getTenantId());
				mp22.setSysFlag(sysFlag);
				mp22.setSuitNo(suitNo);
				mp22.setType("proteinPercentPerDietaryOfWeek");
				mp22.setTitle("豆类");
				mp22.setName("proteinBeansPercent");
				mp22.setValue(Math.round(proteinBeans / pp * 100D));
				mp22.setSortNo(1);
				rows.add(mp22);

				DietaryStatistics mp3 = new DietaryStatistics();
				mp3.setTenantId(query.getTenantId());
				mp3.setSysFlag(sysFlag);
				mp3.setSuitNo(suitNo);
				mp3.setType("proteinPerDietaryOfWeek");
				mp3.setTitle("动物性食物");
				mp3.setName("proteinAnimals");
				mp3.setValue(Math.round(proteinAnimals));
				mp3.setSortNo(2);
				rows.add(mp3);

				DietaryStatistics mp33 = new DietaryStatistics();
				mp33.setTenantId(query.getTenantId());
				mp33.setSysFlag(sysFlag);
				mp33.setSuitNo(suitNo);
				mp33.setType("proteinPercentPerDietaryOfWeek");
				mp33.setTitle("动物性食物");
				mp33.setName("proteinAnimalsPercent");
				mp33.setValue(Math.round(proteinAnimals / pp * 100D));
				mp33.setSortNo(2);
				rows.add(mp33);

				DietaryStatistics mp4 = new DietaryStatistics();
				mp4.setTenantId(query.getTenantId());
				mp4.setSysFlag(sysFlag);
				mp4.setSuitNo(suitNo);
				mp4.setType("proteinPerDietaryOfWeek");
				mp4.setTitle("谷、薯、杂豆及其他");
				mp4.setName("proteinOthers");
				mp4.setValue(Math.round(proteinOthers));
				mp4.setSortNo(3);
				rows.add(mp4);

				DietaryStatistics mp44 = new DietaryStatistics();
				mp44.setTenantId(query.getTenantId());
				mp44.setSysFlag(sysFlag);
				mp44.setSuitNo(suitNo);
				mp44.setType("proteinPercentPerDietaryOfWeek");
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
					mx1.setType("heatEnergyX1PerDietaryOfWeek");
					mx1.setTitle("动物性食物");
					mx1.setName("heatEnergyAnimal");
					mx1.setValue(Math.round(heatEnergyAnimal));
					mx1.setSortNo(1);
					rows.add(mx1);

					DietaryStatistics mx11 = new DietaryStatistics();
					mx11.setTenantId(query.getTenantId());
					mx11.setSysFlag(sysFlag);
					mx11.setSuitNo(suitNo);
					mx11.setType("heatEnergyX1PercentPerDietaryOfWeek");
					mx11.setTitle("动物性食物");
					mx11.setName("heatEnergyAnimalPercent");
					mx11.setValue(Math.round(heatEnergyAnimal / tt * 100D));
					mx11.setSortNo(1);
					rows.add(mx11);

					DietaryStatistics mx2 = new DietaryStatistics();
					mx2.setTenantId(query.getTenantId());
					mx2.setSysFlag(sysFlag);
					mx2.setSuitNo(suitNo);
					mx2.setType("heatEnergyX1PerDietaryOfWeek");
					mx2.setTitle("植物性食物");
					mx2.setName("heatEnergyPlant");
					mx2.setValue(Math.round(heatEnergyPlant));
					mx2.setSortNo(2);
					rows.add(mx2);

					DietaryStatistics mx22 = new DietaryStatistics();
					mx22.setTenantId(query.getTenantId());
					mx22.setSysFlag(sysFlag);
					mx22.setSuitNo(suitNo);
					mx22.setType("heatEnergyX1PercentPerDietaryOfWeek");
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
					mx1.setType("heatEnergyX2PerDietaryOfWeek");
					mx1.setTitle("脂肪");
					mx1.setName("heatEnergyFat");
					mx1.setValue(Math.round(heatEnergyFat));
					mx1.setSortNo(2);
					rows.add(mx1);

					DietaryStatistics mx2 = new DietaryStatistics();
					mx2.setTenantId(query.getTenantId());
					mx2.setSysFlag(sysFlag);
					mx2.setSuitNo(suitNo);
					mx2.setType("heatEnergyX2PerDietaryOfWeek");
					mx2.setTitle("蛋白质");
					mx2.setName("heatEnergyProtein");
					mx2.setValue(Math.round(heatEnergyProtein));
					mx2.setSortNo(1);
					rows.add(mx2);

					DietaryStatistics mx3 = new DietaryStatistics();
					mx3.setTenantId(query.getTenantId());
					mx3.setSysFlag(sysFlag);
					mx3.setSuitNo(suitNo);
					mx3.setType("heatEnergyX2PerDietaryOfWeek");
					mx3.setTitle("碳水化合物");
					mx3.setName("heatEnergyCarbohydrate");
					mx3.setValue(Math.round(heatEnergyCarbohydrate));
					mx3.setSortNo(3);
					rows.add(mx3);

					DietaryStatistics mx11 = new DietaryStatistics();
					mx11.setTenantId(query.getTenantId());
					mx11.setSysFlag(sysFlag);
					mx11.setSuitNo(suitNo);
					mx11.setType("heatEnergyX2PercentPerDietaryOfWeek");
					mx11.setTitle("脂肪");
					mx11.setName("heatEnergyFatPercent");
					mx11.setValue(Math.round(heatEnergyFat / tt * 100D));
					mx11.setSortNo(2);
					rows.add(mx11);

					DietaryStatistics mx12 = new DietaryStatistics();
					mx12.setTenantId(query.getTenantId());
					mx12.setSysFlag(sysFlag);
					mx12.setSuitNo(suitNo);
					mx12.setType("heatEnergyX2PercentPerDietaryOfWeek");
					mx12.setTitle("蛋白质");
					mx12.setName("heatEnergyProteinPercent");
					mx12.setValue(Math.round(heatEnergyProtein / tt * 100D));
					mx12.setSortNo(1);
					rows.add(mx12);

					DietaryStatistics mx13 = new DietaryStatistics();
					mx13.setTenantId(query.getTenantId());
					mx13.setSysFlag(sysFlag);
					mx13.setSuitNo(suitNo);
					mx13.setType("heatEnergyX2PercentPerDietaryOfWeek");
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
					nx1.setType("heatEnergyX3PerDietaryOfWeek");
					nx1.setTitle("早餐");
					nx1.setName("heatEnergyMoming");
					nx1.setValue(Math.round(heatEnergyMoming));
					nx1.setSortNo(1);
					rows.add(nx1);

					DietaryStatistics nx2 = new DietaryStatistics();
					nx2.setTenantId(query.getTenantId());
					nx2.setSysFlag(sysFlag);
					nx2.setSuitNo(suitNo);
					nx2.setType("heatEnergyX3PerDietaryOfWeek");
					nx2.setTitle("中餐");
					nx2.setName("heatEnergyNoon");
					nx2.setValue(Math.round(heatEnergyNoon));
					nx2.setSortNo(2);
					rows.add(nx2);

					DietaryStatistics nx3 = new DietaryStatistics();
					nx3.setTenantId(query.getTenantId());
					nx3.setSysFlag(sysFlag);
					nx3.setSuitNo(suitNo);
					nx3.setType("heatEnergyX3PerDietaryOfWeek");
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
						nx11.setType("heatEnergyX3PercentPerDietaryOfWeek");
						nx11.setTitle("早餐");
						nx11.setName("heatEnergyMoming");
						nx11.setValue(Math.round(heatEnergyMoming / tt * 100D));
						nx11.setSortNo(1);
						rows.add(nx11);

						DietaryStatistics nx12 = new DietaryStatistics();
						nx12.setTenantId(query.getTenantId());
						nx12.setSysFlag(sysFlag);
						nx12.setSuitNo(suitNo);
						nx12.setType("heatEnergyX3PercentPerDietaryOfWeek");
						nx12.setTitle("中餐");
						nx12.setName("heatEnergyNoon");
						nx12.setValue(Math.round(heatEnergyNoon / tt * 100D));
						nx12.setSortNo(2);
						rows.add(nx12);

						DietaryStatistics nx13 = new DietaryStatistics();
						nx13.setTenantId(query.getTenantId());
						nx13.setSysFlag(sysFlag);
						nx13.setSuitNo(suitNo);
						nx13.setType("heatEnergyX3PercentPerDietaryOfWeek");
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
						nx11.setType("heatEnergyX3PercentPerDietaryOfWeek");
						nx11.setTitle("早餐");
						nx11.setName("heatEnergyMoming");
						nx11.setValue(Math.round(heatEnergyMoming / tt * 70));
						nx11.setSortNo(1);
						rows.add(nx11);

						DietaryStatistics nx12 = new DietaryStatistics();
						nx12.setTenantId(query.getTenantId());
						nx12.setSysFlag(sysFlag);
						nx12.setSuitNo(suitNo);
						nx12.setType("heatEnergyX3PercentPerDietaryOfWeek");
						nx12.setTitle("中餐");
						nx12.setName("heatEnergyNoon");
						nx12.setValue(Math.round(heatEnergyNoon / tt * 70D));
						nx12.setSortNo(2);
						rows.add(nx12);

						DietaryStatistics nx13 = new DietaryStatistics();
						nx13.setTenantId(query.getTenantId());
						nx13.setSysFlag(sysFlag);
						nx13.setSuitNo(suitNo);
						nx13.setType("heatEnergyX3PercentPerDietaryOfWeek");
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
					mx15.setType("vitaminAAnimalsX3PercentPerDietaryOfWeek");
					mx15.setTitle("动物性V-A");
					mx15.setName("vitaminAAnimalsPercent");
					mx15.setValue(Math.round(p1 * 100D));
					mx15.setSortNo(3);
					rows.add(mx15);

					DietaryStatistics mx16 = new DietaryStatistics();
					mx16.setTenantId(query.getTenantId());
					mx16.setSysFlag(sysFlag);
					mx16.setSuitNo(suitNo);
					mx16.setType("vitaminAAnimalsX3PercentPerDietaryOfWeek");
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
					mx15.setType("ironAnimalsX3PercentPerDietaryOfWeek");
					mx15.setTitle("动物性铁");
					mx15.setName("ironAnimalsPercent");
					mx15.setValue(Math.round(p2 * 100D));
					mx15.setSortNo(3);
					rows.add(mx15);

					DietaryStatistics mx16 = new DietaryStatistics();
					mx16.setTenantId(query.getTenantId());
					mx16.setSysFlag(sysFlag);
					mx16.setSuitNo(suitNo);
					mx16.setType("ironAnimalsX3PercentPerDietaryOfWeek");
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
					mx15.setType("fatAnimalsX3PercentPerDietaryOfWeek");
					mx15.setTitle("动物性脂肪");
					mx15.setName("fatAnimalsPercent");
					mx15.setValue(Math.round(p2 * 100D));
					mx15.setSortNo(3);
					rows.add(mx15);

					DietaryStatistics mx16 = new DietaryStatistics();
					mx16.setTenantId(query.getTenantId());
					mx16.setSysFlag(sysFlag);
					mx16.setSuitNo(suitNo);
					mx16.setType("fatAnimalsX3PercentPerDietaryOfWeek");
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
					mx15.setSuitNo(suitNo);
					mx15.setSysFlag(sysFlag);
					mx15.setType("calciumPhosphorusX3PercentPerDietaryOfWeek");
					mx15.setTitle("磷占比");
					mx15.setName("phosphorusPercent");
					mx15.setValue(Math.round(p2 * 100D));
					mx15.setSortNo(3);
					rows.add(mx15);

					DietaryStatistics mx16 = new DietaryStatistics();
					mx16.setTenantId(query.getTenantId());
					mx16.setSysFlag(sysFlag);
					mx16.setSuitNo(suitNo);
					mx16.setType("calciumPhosphorusX3PercentPerDietaryOfWeek");
					mx16.setTitle("钙占比");
					mx16.setName("calciumPercent");
					mx16.setValue(100 - mx15.getValue());
					mx16.setSortNo(3);
					rows.add(mx16);
				}

				saveAll(query.getTenantId(), sysFlag, suitNo, rows);

			}
		}
	}

	public DietaryStatistics getDietaryStatistics(Long id) {
		if (id == null) {
			return null;
		}
		DietaryStatistics dietaryStatistics = dietaryStatisticsMapper.getDietaryStatisticsById(id);
		return dietaryStatistics;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getDietaryStatisticsCountByQueryCriteria(DietaryStatisticsQuery query) {
		return dietaryStatisticsMapper.getDietaryStatisticsCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<DietaryStatistics> getDietaryStatisticssByQueryCriteria(int start, int pageSize,
			DietaryStatisticsQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<DietaryStatistics> rows = sqlSessionTemplate.selectList("getDietaryStatisticss", query, rowBounds);
		return rows;
	}

	public List<DietaryStatistics> list(DietaryStatisticsQuery query) {
		List<DietaryStatistics> list = dietaryStatisticsMapper.getDietaryStatisticss(query);
		return list;
	}

	@Transactional
	public void saveAll(String tenantId, String sysFlag, int year, int semester, int week,
			List<DietaryStatistics> list) {
		DietaryStatisticsQuery query = new DietaryStatisticsQuery();
		if (StringUtils.isNotEmpty(tenantId)) {
			query.tenantId(tenantId);
		}
		if (sysFlag != null) {
			query.sysFlag(sysFlag);
		}
		if (year > 0) {
			query.year(year);
		}
		if (semester > 0) {
			query.semester(semester);
		}
		if (week > 0) {
			query.week(week);
		}
		List<DietaryStatistics> rows = dietaryStatisticsMapper.getDietaryStatisticss(query);
		List<Long> ids = new ArrayList<Long>();
		for (DietaryStatistics row : rows) {
			// dietaryStatisticsMapper.deleteDietaryStatisticsById(row.getId());
			ids.add(row.getId());
		}
		if (ids.size() > 0) {
			DietaryStatisticsQuery q = new DietaryStatisticsQuery();
			q.setIds(ids);
			dietaryStatisticsMapper.deleteDietaryStatisticses(q);
		}
		this.bulkInsert(list);
	}

	@Transactional
	public void saveAll(String tenantId, String sysFlag, int suitNo, int dayOfWeek, List<DietaryStatistics> list) {
		DietaryStatisticsQuery query = new DietaryStatisticsQuery();
		if (StringUtils.isNotEmpty(tenantId)) {
			query.tenantId(tenantId);
		}
		if (sysFlag != null) {
			query.sysFlag(sysFlag);
		}
		if (suitNo > 0) {
			query.suitNo(suitNo);
		}
		if (dayOfWeek > 0) {
			query.dayOfWeek(dayOfWeek);
		}
		List<DietaryStatistics> rows = dietaryStatisticsMapper.getDietaryStatisticss(query);
		List<Long> ids = new ArrayList<Long>();
		for (DietaryStatistics row : rows) {
			// dietaryStatisticsMapper.deleteDietaryStatisticsById(row.getId());
			ids.add(row.getId());
		}
		if (ids.size() > 0) {
			DietaryStatisticsQuery q = new DietaryStatisticsQuery();
			q.setIds(ids);
			dietaryStatisticsMapper.deleteDietaryStatisticses(q);
		}
		for (DietaryStatistics row : list) {
			row.setDayOfWeek(dayOfWeek);
		}
		this.bulkInsert(list);
	}

	@Transactional
	public void saveAll(String tenantId, String sysFlag, int suitNo, List<DietaryStatistics> list) {
		DietaryStatisticsQuery query = new DietaryStatisticsQuery();
		query.dayOfWeek(9999);
		if (StringUtils.isNotEmpty(tenantId)) {
			query.tenantId(tenantId);
		}
		if (sysFlag != null) {
			query.sysFlag(sysFlag);
		}
		if (suitNo > 0) {
			query.suitNo(suitNo);
		}

		List<DietaryStatistics> rows = dietaryStatisticsMapper.getDietaryStatisticss(query);
		List<Long> ids = new ArrayList<Long>();
		for (DietaryStatistics row : rows) {
			// dietaryStatisticsMapper.deleteDietaryStatisticsById(row.getId());
			ids.add(row.getId());
		}
		if (ids.size() > 0) {
			DietaryStatisticsQuery q = new DietaryStatisticsQuery();
			q.setIds(ids);
			dietaryStatisticsMapper.deleteDietaryStatisticses(q);
		}
		for (DietaryStatistics row : list) {
			row.setDayOfWeek(9999);
		}
		this.bulkInsert(list);
	}

	@Transactional
	public void saveAll(String tenantId, String batchNo, List<DietaryStatistics> list) {
		TableModel table = new TableModel();
		table.setTableName("HEALTH_DIETARY_STATISTICS");
		table.addStringColumn("BATCHNO_", batchNo);
		table.addStringColumn("TENANTID_", tenantId);
		tableDataService.deleteTableData(table);

		this.bulkInsert(list);
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.dietaryItemService")
	public void setDietaryItemService(DietaryItemService dietaryItemService) {
		this.dietaryItemService = dietaryItemService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.DietaryStatisticsMapper")
	public void setDietaryStatisticsMapper(DietaryStatisticsMapper dietaryStatisticsMapper) {
		this.dietaryStatisticsMapper = dietaryStatisticsMapper;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.dietaryTemplateService")
	public void setDietaryTemplateService(DietaryTemplateService dietaryTemplateService) {
		this.dietaryTemplateService = dietaryTemplateService;
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.foodCompositionService")
	public void setFoodCompositionService(FoodCompositionService foodCompositionService) {
		this.foodCompositionService = foodCompositionService;
	}

	@javax.annotation.Resource
	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	@javax.annotation.Resource
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@javax.annotation.Resource
	public void setTableDataService(ITableDataService tableDataService) {
		this.tableDataService = tableDataService;
	}

}
