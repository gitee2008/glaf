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
import java.util.Set;
import java.util.Map.Entry;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.util.DateUtils;

import com.glaf.heathcare.SysConfig;
import com.glaf.heathcare.domain.CompositionCount;
import com.glaf.heathcare.domain.Dietary;
import com.glaf.heathcare.domain.DietaryCount;
import com.glaf.heathcare.domain.DietaryItem;
import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.query.DietaryItemQuery;
import com.glaf.heathcare.query.DietaryQuery;
import com.glaf.heathcare.query.FoodCompositionQuery;
import com.glaf.heathcare.service.DietaryCountService;
import com.glaf.heathcare.service.DietaryItemService;
import com.glaf.heathcare.service.DietaryService;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.heathcare.util.DietaryCountDomainFactory;

public class DayDietaryBean {

	protected DietaryService dietaryService;

	protected DietaryCountService dietaryCountService;

	protected DietaryItemService dietaryItemService;

	protected FoodCompositionService foodCompositionService;

	public DayDietaryBean() {

	}

	public void calculate(String tenantId, long dietaryId) {
		if (dietaryId > 0) {
			Dietary dietary = getDietaryService().getDietary(tenantId, dietaryId);
			List<DietaryItem> items = getDietaryItemService().getDietaryItemsByDietaryId(tenantId, dietaryId);
			if (items != null && !items.isEmpty()) {
				List<Long> foodIds = new ArrayList<Long>();
				for (DietaryItem item : items) {
					if (!foodIds.contains(item.getFoodId())) {
						foodIds.add(item.getFoodId());
					}
				}

				FoodCompositionQuery query3 = new FoodCompositionQuery();
				query3.setIds(foodIds);
				List<FoodComposition> foods = getFoodCompositionService().list(query3);// 获取食物成分
				Map<Long, FoodComposition> foodMap = new HashMap<Long, FoodComposition>();
				for (FoodComposition food : foods) {
					foodMap.put(food.getId(), food);
				}

				dietary.setHeatEnergy(0);
				dietary.setProtein(0);
				dietary.setFat(0);
				dietary.setCarbohydrate(0);
				dietary.setVitaminA(0);
				dietary.setVitaminB1(0);
				dietary.setVitaminB2(0);
				dietary.setVitaminB6(0);
				dietary.setVitaminB12(0);
				dietary.setVitaminC(0);
				dietary.setCarotene(0);
				dietary.setRetinol(0);
				dietary.setNicotinicCid(0);
				dietary.setCalcium(0);
				dietary.setIron(0);
				dietary.setZinc(0);
				dietary.setIodine(0);
				dietary.setPhosphorus(0);

				for (DietaryItem item : items) {
					FoodComposition food = foodMap.get(item.getFoodId());
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
					dietary.setHeatEnergy(dietary.getHeatEnergy() + food.getHeatEnergy() * factor);// 累加计算
					dietary.setProtein(dietary.getProtein() + food.getProtein() * factor);// 累加计算
					dietary.setFat(dietary.getFat() + food.getFat() * factor);// 累加计算
					dietary.setCarbohydrate(dietary.getCarbohydrate() + food.getCarbohydrate() * factor);// 累加计算
					dietary.setVitaminA(dietary.getVitaminA() + food.getVitaminA() * factor);// 累加计算
					dietary.setVitaminB1(dietary.getVitaminB1() + food.getVitaminB1() * factor);// 累加计算
					dietary.setVitaminB2(dietary.getVitaminB2() + food.getVitaminB2() * factor);// 累加计算
					dietary.setVitaminB6(dietary.getVitaminB6() + food.getVitaminB6() * factor);// 累加计算
					dietary.setVitaminB12(dietary.getVitaminB12() + food.getVitaminB12() * factor);// 累加计算
					dietary.setVitaminC(dietary.getVitaminC() + food.getVitaminC() * factor);// 累加计算
					dietary.setCarotene(dietary.getCarotene() + food.getCarotene() * factor);// 累加计算
					dietary.setRetinol(dietary.getRetinol() + food.getRetinol() * factor);// 累加计算
					dietary.setNicotinicCid(dietary.getNicotinicCid() + food.getNicotinicCid() * factor);// 累加计算
					dietary.setCalcium(dietary.getCalcium() + food.getCalcium() * factor);// 累加计算
					dietary.setIron(dietary.getIron() + food.getIron() * factor);// 累加计算
					dietary.setZinc(dietary.getZinc() + food.getZinc() * factor);// 累加计算
					dietary.setIodine(dietary.getIodine() + food.getIodine() * factor);// 累加计算
					dietary.setPhosphorus(dietary.getPhosphorus() + food.getPhosphorus() * factor);// 累加计算
				}

				getDietaryService().save(dietary);
			}
		}
	}

	public void calculateAll(String tenantId, List<Long> dietaryIds) {
		DietaryItemQuery query = new DietaryItemQuery();
		query.dietaryIds(dietaryIds);
		query.tenantId(tenantId);
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

		DietaryQuery query4 = new DietaryQuery();
		query4.setDietaryIds(dietaryIds);
		query4.tenantId(tenantId);
		List<Dietary> dietarys = getDietaryService().list(query4);
		if (dietarys != null && !dietarys.isEmpty()) {
			for (Dietary dietary : dietarys) {
				dietary.setHeatEnergy(0);
				dietary.setProtein(0);
				dietary.setFat(0);
				dietary.setCarbohydrate(0);
				dietary.setVitaminA(0);
				dietary.setVitaminB1(0);
				dietary.setVitaminB2(0);
				dietary.setVitaminB6(0);
				dietary.setVitaminB12(0);
				dietary.setVitaminC(0);
				dietary.setCarotene(0);
				dietary.setRetinol(0);
				dietary.setNicotinicCid(0);
				dietary.setCalcium(0);
				dietary.setIron(0);
				dietary.setZinc(0);
				dietary.setIodine(0);
				dietary.setPhosphorus(0);

				for (DietaryItem item : items) {
					if (!(item.getDietaryId() == dietary.getId())) {
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
					dietary.setHeatEnergy(dietary.getHeatEnergy() + food.getHeatEnergy() * factor);// 累加计算
					dietary.setProtein(dietary.getProtein() + food.getProtein() * factor);// 累加计算
					dietary.setFat(dietary.getFat() + food.getFat() * factor);// 累加计算
					dietary.setCarbohydrate(dietary.getCarbohydrate() + food.getCarbohydrate() * factor);// 累加计算
					dietary.setVitaminA(dietary.getVitaminA() + food.getVitaminA() * factor);// 累加计算
					dietary.setVitaminB1(dietary.getVitaminB1() + food.getVitaminB1() * factor);// 累加计算
					dietary.setVitaminB2(dietary.getVitaminB2() + food.getVitaminB2() * factor);// 累加计算
					dietary.setVitaminB6(dietary.getVitaminB6() + food.getVitaminB6() * factor);// 累加计算
					dietary.setVitaminB12(dietary.getVitaminB12() + food.getVitaminB12() * factor);// 累加计算
					dietary.setVitaminC(dietary.getVitaminC() + food.getVitaminC() * factor);// 累加计算
					dietary.setCarotene(dietary.getCarotene() + food.getCarotene() * factor);// 累加计算
					dietary.setRetinol(dietary.getRetinol() + food.getRetinol() * factor);// 累加计算
					dietary.setNicotinicCid(dietary.getNicotinicCid() + food.getNicotinicCid() * factor);// 累加计算
					dietary.setCalcium(dietary.getCalcium() + food.getCalcium() * factor);// 累加计算
					dietary.setIron(dietary.getIron() + food.getIron() * factor);// 累加计算
					dietary.setZinc(dietary.getZinc() + food.getZinc() * factor);// 累加计算
					dietary.setIodine(dietary.getIodine() + food.getIodine() * factor);// 累加计算
					dietary.setPhosphorus(dietary.getPhosphorus() + food.getPhosphorus() * factor);// 累加计算
				}
			}
			getDietaryService().updateAll(tenantId, dietarys);
		}
	}

	public CompositionCount countMulti(String tenantId, List<Long> dietaryIds) {
		CompositionCount count = new CompositionCount();
		DietaryItemQuery query = new DietaryItemQuery();
		query.dietaryIds(dietaryIds);
		query.tenantId(tenantId);
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

			if (food.getCarbohydrate() > 0) {
				count.setHeatEnergyCarbohydrate(
						count.getHeatEnergyCarbohydrate() + food.getCarbohydrate() * factor * 4);
			}

			if (food.getFat() > 0) {
				count.setHeatEnergyFat(count.getHeatEnergyFat() + food.getFat() * factor * 9);
			}

			if (food.getProtein() > 0) {
				count.setHeatEnergyProtein(count.getHeatEnergyProtein() + food.getProtein() * factor * 4);
			}

			if (food.getNodeId() == 4409 || food.getNodeId() == 4410 || food.getNodeId() == 4411
					|| food.getNodeId() == 4412 || food.getNodeId() == 4413) {
				count.setProteinAnimal(count.getProteinAnimal() + food.getProtein() * factor);
			}

			if (food.getNodeId() == 4404 || food.getNodeId() == 4409 || food.getNodeId() == 4410
					|| food.getNodeId() == 4411 || food.getNodeId() == 4412 || food.getNodeId() == 4413) {
				count.setProteinAnimalBeans(count.getProteinAnimalBeans() + food.getProtein() * factor);
			}
			// count.setHeatEnergy(count.getHeatEnergy() + food.getHeatEnergy()
			// * factor);// 累加计算
			count.setHeatEnergy(
					count.getHeatEnergyCarbohydrate() + count.getHeatEnergyFat() + count.getHeatEnergyProtein());
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

	public List<CompositionCount> countMultiItems(String tenantId, List<Long> dietaryIds) {
		List<CompositionCount> countList = new ArrayList<CompositionCount>();
		Map<Long, CompositionCount> countMap = new HashMap<Long, CompositionCount>();
		DietaryItemQuery query = new DietaryItemQuery();
		query.dietaryIds(dietaryIds);
		query.tenantId(tenantId);
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

			if (food.getCarbohydrate() > 0) {
				count.setHeatEnergyCarbohydrate(
						count.getHeatEnergyCarbohydrate() + food.getCarbohydrate() * factor * 4);
			}

			if (food.getFat() > 0) {
				count.setHeatEnergyFat(count.getHeatEnergyFat() + food.getFat() * factor * 9);
			}

			if (food.getProtein() > 0) {
				count.setHeatEnergyProtein(count.getHeatEnergyProtein() + food.getProtein() * factor * 4);
			}

			if (food.getNodeId() == 4409 || food.getNodeId() == 4410 || food.getNodeId() == 4411
					|| food.getNodeId() == 4412 || food.getNodeId() == 4413) {
				count.setProteinAnimal(count.getProteinAnimal() + food.getProtein() * factor);
			}

			if (food.getNodeId() == 4404 || food.getNodeId() == 4409 || food.getNodeId() == 4410
					|| food.getNodeId() == 4411 || food.getNodeId() == 4412 || food.getNodeId() == 4413) {
				count.setProteinAnimalBeans(count.getProteinAnimalBeans() + food.getProtein() * factor);
			}
			// count.setHeatEnergy(count.getHeatEnergy() + food.getHeatEnergy()
			// * factor);// 累加计算

			count.setHeatEnergy(
					count.getHeatEnergyCarbohydrate() + count.getHeatEnergyFat() + count.getHeatEnergyProtein());
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

	public void executeCountAll(String tenantId, int week, int fullDay) {
		DietaryQuery query = new DietaryQuery();
		query.tenantId(tenantId);
		query.fullDay(fullDay);

		List<Dietary> list = getDietaryService().list(query);
		if (list != null && !list.isEmpty()) {

			List<Long> dietaryIds = new ArrayList<Long>();
			for (Dietary dietary : list) {
				dietaryIds.add(dietary.getId());
			}

			this.calculateAll(tenantId, dietaryIds);

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(DateUtils.toDate(String.valueOf(fullDay)));

			Map<String, List<Long>> listMap = new HashMap<String, List<Long>>();

			for (Dietary dietary : list) {
				long typeId = dietary.getTypeId();
				if (typeId == 3302 || typeId == 3312 || typeId == 3402 || typeId == 3412) {
					List<Long> ids = listMap.get("breakfast");
					if (ids == null) {
						ids = new ArrayList<Long>();
					}
					ids.add(dietary.getId());
					listMap.put("breakfast", ids);
				}
				if (typeId == 3403 || typeId == 3413) {
					List<Long> ids = listMap.get("breakfastMid");
					if (ids == null) {
						ids = new ArrayList<Long>();
					}
					ids.add(dietary.getId());
					listMap.put("breakfastMid", ids);
				}
				if (typeId == 3303 || typeId == 3313 || typeId == 3323 || typeId == 3313 || typeId == 3404
						|| typeId == 3414) {
					List<Long> ids = listMap.get("lunch");
					if (ids == null) {
						ids = new ArrayList<Long>();
					}
					ids.add(dietary.getId());
					listMap.put("lunch", ids);
				}
				if (typeId == 3304 || typeId == 3314 || typeId == 3324 || typeId == 3405 || typeId == 3415) {
					List<Long> ids = listMap.get("snack");
					if (ids == null) {
						ids = new ArrayList<Long>();
					}
					ids.add(dietary.getId());
					listMap.put("snack", ids);
				}
				if (typeId == 3305 || typeId == 3406) {
					List<Long> ids = listMap.get("dinner");
					if (ids == null) {
						ids = new ArrayList<Long>();
					}
					ids.add(dietary.getId());
					listMap.put("dinner", ids);
				}
			}

			List<DietaryCount> countList = new ArrayList<DietaryCount>();
			Set<Entry<String, List<Long>>> entrySet = listMap.entrySet();
			for (Entry<String, List<Long>> entry : entrySet) {
				String mealType = entry.getKey();
				List<Long> ids = entry.getValue();
				CompositionCount count = this.countMulti(tenantId, ids);
				DietaryCount model = new DietaryCount();
				DietaryCountDomainFactory.copyProperties(model, count);
				model.setCreateTime(new Date());
				model.setYear(calendar.get(Calendar.YEAR));
				model.setMonth(calendar.get(Calendar.MONTH) + 1);
				model.setWeek(week);
				model.setDay(fullDay % 100);
				model.setFullDay(fullDay);
				model.setTenantId(tenantId);
				model.setDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
				model.setSemester(SysConfig.getSemester());
				model.setType("DAY");
				model.setMealType(mealType);
				countList.add(model);
			}

			getDietaryCountService().saveDay(tenantId, calendar.get(Calendar.YEAR), SysConfig.getSemester(), week,
					fullDay, "DAY", countList);
		}

	}

	public void executeCountItems(String tenantId, int week, int fullDay) {
		DietaryQuery query = new DietaryQuery();
		query.tenantId(tenantId);
		query.fullDay(fullDay);
		List<Dietary> list = getDietaryService().list(query);
		if (list != null && !list.isEmpty()) {

			List<Long> dietaryIds = new ArrayList<Long>();
			for (Dietary dietary : list) {
				dietaryIds.add(dietary.getId());
			}

			this.calculateAll(tenantId, dietaryIds);

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(DateUtils.toDate(String.valueOf(fullDay)));

			Map<String, List<Long>> listMap = new HashMap<String, List<Long>>();

			for (Dietary dietary : list) {
				long typeId = dietary.getTypeId();
				if (typeId == 3302 || typeId == 3312 || typeId == 3402 || typeId == 3412) {
					List<Long> ids = listMap.get("breakfast");
					if (ids == null) {
						ids = new ArrayList<Long>();
					}
					ids.add(dietary.getId());
					listMap.put("breakfast", ids);
				}
				if (typeId == 3403 || typeId == 3413) {
					List<Long> ids = listMap.get("breakfastMid");
					if (ids == null) {
						ids = new ArrayList<Long>();
					}
					ids.add(dietary.getId());
					listMap.put("breakfastMid", ids);
				}
				if (typeId == 3303 || typeId == 3313 || typeId == 3323 || typeId == 3313 || typeId == 3404
						|| typeId == 3414) {
					List<Long> ids = listMap.get("lunch");
					if (ids == null) {
						ids = new ArrayList<Long>();
					}
					ids.add(dietary.getId());
					listMap.put("lunch", ids);
				}
				if (typeId == 3304 || typeId == 3314 || typeId == 3324 || typeId == 3405 || typeId == 3415) {
					List<Long> ids = listMap.get("snack");
					if (ids == null) {
						ids = new ArrayList<Long>();
					}
					ids.add(dietary.getId());
					listMap.put("snack", ids);
				}
				if (typeId == 3305 || typeId == 3406) {
					List<Long> ids = listMap.get("dinner");
					if (ids == null) {
						ids = new ArrayList<Long>();
					}
					ids.add(dietary.getId());
					listMap.put("dinner", ids);
				}
			}

			List<DietaryCount> countList = new ArrayList<DietaryCount>();
			Set<Entry<String, List<Long>>> entrySet = listMap.entrySet();
			for (Entry<String, List<Long>> entry : entrySet) {
				String mealType = entry.getKey();
				List<Long> ids = entry.getValue();
				List<CompositionCount> countList2 = this.countMultiItems(tenantId, ids);
				for (CompositionCount cnt : countList2) {
					DietaryCount model = new DietaryCount();
					DietaryCountDomainFactory.copyProperties(model, cnt);
					model.setNodeId(cnt.getNodeId());
					model.setMealType(mealType);
					model.setCreateTime(new Date());
					model.setYear(calendar.get(Calendar.YEAR));
					model.setMonth(calendar.get(Calendar.MONTH) + 1);
					model.setWeek(week);
					model.setDay(fullDay % 100);
					model.setFullDay(fullDay);
					model.setTenantId(tenantId);
					model.setDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
					model.setSemester(SysConfig.getSemester());
					model.setType("DAY_ITEM");
					countList.add(model);
				}
			}

			getDietaryCountService().saveDay(tenantId, calendar.get(Calendar.YEAR), SysConfig.getSemester(), week,
					fullDay, "DAY_ITEM", countList);
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

}
