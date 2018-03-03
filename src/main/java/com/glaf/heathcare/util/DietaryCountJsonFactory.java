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

package com.glaf.heathcare.util;

import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.glaf.core.util.DateUtils;
import com.glaf.heathcare.domain.*;

/**
 * 
 * JSON工厂类
 *
 */
public class DietaryCountJsonFactory {

	public static java.util.List<DietaryCount> arrayToList(JSONArray array) {
		java.util.List<DietaryCount> list = new java.util.ArrayList<DietaryCount>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			DietaryCount model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	public static DietaryCount jsonToObject(JSONObject jsonObject) {
		DietaryCount model = new DietaryCount();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getLong("id"));
		}
		if (jsonObject.containsKey("parentId")) {
			model.setParentId(jsonObject.getLong("parentId"));
		}
		if (jsonObject.containsKey("tenantId")) {
			model.setTenantId(jsonObject.getString("tenantId"));
		}
		if (jsonObject.containsKey("heatEnergy")) {
			model.setHeatEnergy(jsonObject.getDouble("heatEnergy"));
		}
		if (jsonObject.containsKey("heatEnergyCarbohydrate")) {
			model.setHeatEnergyCarbohydrate(jsonObject.getDouble("heatEnergyCarbohydrate"));
		}
		if (jsonObject.containsKey("heatEnergyFat")) {
			model.setHeatEnergyFat(jsonObject.getDouble("heatEnergyFat"));
		}
		if (jsonObject.containsKey("protein")) {
			model.setProtein(jsonObject.getDouble("protein"));
		}
		if (jsonObject.containsKey("proteinAnimal")) {
			model.setProteinAnimal(jsonObject.getDouble("proteinAnimal"));
		}
		if (jsonObject.containsKey("proteinAnimalBeans")) {
			model.setProteinAnimalBeans(jsonObject.getDouble("proteinAnimalBeans"));
		}
		if (jsonObject.containsKey("fat")) {
			model.setFat(jsonObject.getDouble("fat"));
		}
		if (jsonObject.containsKey("carbohydrate")) {
			model.setCarbohydrate(jsonObject.getDouble("carbohydrate"));
		}
		if (jsonObject.containsKey("vitaminA")) {
			model.setVitaminA(jsonObject.getDouble("vitaminA"));
		}
		if (jsonObject.containsKey("vitaminB1")) {
			model.setVitaminB1(jsonObject.getDouble("vitaminB1"));
		}
		if (jsonObject.containsKey("vitaminB2")) {
			model.setVitaminB2(jsonObject.getDouble("vitaminB2"));
		}
		if (jsonObject.containsKey("vitaminB6")) {
			model.setVitaminB6(jsonObject.getDouble("vitaminB6"));
		}
		if (jsonObject.containsKey("vitaminB12")) {
			model.setVitaminB12(jsonObject.getDouble("vitaminB12"));
		}
		if (jsonObject.containsKey("vitaminC")) {
			model.setVitaminC(jsonObject.getDouble("vitaminC"));
		}
		if (jsonObject.containsKey("vitaminE")) {
			model.setVitaminE(jsonObject.getDouble("vitaminE"));
		}
		if (jsonObject.containsKey("carotene")) {
			model.setCarotene(jsonObject.getDouble("carotene"));
		}
		if (jsonObject.containsKey("retinol")) {
			model.setRetinol(jsonObject.getDouble("retinol"));
		}
		if (jsonObject.containsKey("nicotinicCid")) {
			model.setNicotinicCid(jsonObject.getDouble("nicotinicCid"));
		}
		if (jsonObject.containsKey("calcium")) {
			model.setCalcium(jsonObject.getDouble("calcium"));
		}
		if (jsonObject.containsKey("iron")) {
			model.setIron(jsonObject.getDouble("iron"));
		}
		if (jsonObject.containsKey("zinc")) {
			model.setZinc(jsonObject.getDouble("zinc"));
		}
		if (jsonObject.containsKey("iodine")) {
			model.setIodine(jsonObject.getDouble("iodine"));
		}
		if (jsonObject.containsKey("phosphorus")) {
			model.setPhosphorus(jsonObject.getDouble("phosphorus"));
		}
		if (jsonObject.containsKey("mealType")) {
			model.setMealType(jsonObject.getString("mealType"));
		}
		if (jsonObject.containsKey("type")) {
			model.setType(jsonObject.getString("type"));
		}
		if (jsonObject.containsKey("nodeId")) {
			model.setNodeId(jsonObject.getLong("nodeId"));
		}
		if (jsonObject.containsKey("semester")) {
			model.setSemester(jsonObject.getInteger("semester"));
		}
		if (jsonObject.containsKey("year")) {
			model.setYear(jsonObject.getInteger("year"));
		}
		if (jsonObject.containsKey("month")) {
			model.setMonth(jsonObject.getInteger("month"));
		}
		if (jsonObject.containsKey("day")) {
			model.setDay(jsonObject.getInteger("day"));
		}
		if (jsonObject.containsKey("dayOfWeek")) {
			model.setDayOfWeek(jsonObject.getInteger("dayOfWeek"));
		}
		if (jsonObject.containsKey("week")) {
			model.setWeek(jsonObject.getInteger("week"));
		}
		if (jsonObject.containsKey("fullDay")) {
			model.setFullDay(jsonObject.getInteger("fullDay"));
		}
		if (jsonObject.containsKey("createTime")) {
			model.setCreateTime(jsonObject.getDate("createTime"));
		}

		return model;
	}

	public static JSONArray listToArray(java.util.List<DietaryCount> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (DietaryCount model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static JSONObject toJsonObject(DietaryCount model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		jsonObject.put("parentId", model.getParentId());
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		jsonObject.put("heatEnergy", model.getHeatEnergy());
		jsonObject.put("heatEnergyKJ", Math.round(model.getHeatEnergy() * Constants.CALORIE_TO_JOULE));
		jsonObject.put("heatEnergyCarbohydrate", model.getHeatEnergyCarbohydrate());
		jsonObject.put("heatEnergyCarbohydrateKJ",
				Math.round(model.getHeatEnergyCarbohydrate() * Constants.CALORIE_TO_JOULE));
		jsonObject.put("heatEnergyFat", model.getHeatEnergyFat());
		jsonObject.put("heatEnergyFatKJ", Math.round(model.getHeatEnergyFat() * Constants.CALORIE_TO_JOULE));
		jsonObject.put("protein", model.getProtein());
		jsonObject.put("proteinAnimal", model.getProteinAnimal());
		jsonObject.put("proteinAnimalBeans", model.getProteinAnimalBeans());
		jsonObject.put("fat", model.getFat());
		jsonObject.put("carbohydrate", model.getCarbohydrate());
		jsonObject.put("vitaminA", model.getVitaminA());
		jsonObject.put("vitaminB1", model.getVitaminB1());
		jsonObject.put("vitaminB2", model.getVitaminB2());
		jsonObject.put("vitaminB6", model.getVitaminB6());
		jsonObject.put("vitaminB12", model.getVitaminB12());
		jsonObject.put("vitaminC", model.getVitaminC());
		jsonObject.put("vitaminE", model.getVitaminE());
		jsonObject.put("carotene", model.getCarotene());
		jsonObject.put("retinol", model.getRetinol());
		jsonObject.put("nicotinicCid", model.getNicotinicCid());
		jsonObject.put("calcium", model.getCalcium());
		jsonObject.put("iron", model.getIron());
		jsonObject.put("zinc", model.getZinc());
		jsonObject.put("iodine", model.getIodine());
		jsonObject.put("phosphorus", model.getPhosphorus());
		if (model.getMealType() != null) {
			jsonObject.put("mealType", model.getMealType());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		jsonObject.put("nodeId", model.getNodeId());
		jsonObject.put("semester", model.getSemester());
		jsonObject.put("year", model.getYear());
		jsonObject.put("month", model.getMonth());
		jsonObject.put("day", model.getDay());
		jsonObject.put("dayOfWeek", model.getDayOfWeek());
		jsonObject.put("week", model.getWeek());
		jsonObject.put("fullDay", model.getFullDay());
		if (model.getCreateTime() != null) {
			jsonObject.put("createTime", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_date", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_datetime", DateUtils.getDateTime(model.getCreateTime()));
		}
		return jsonObject;
	}

	public static ObjectNode toObjectNode(DietaryCount model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		jsonObject.put("parentId", model.getParentId());
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		jsonObject.put("heatEnergy", model.getHeatEnergy());
		jsonObject.put("heatEnergyKJ", Math.round(model.getHeatEnergy() * Constants.CALORIE_TO_JOULE));
		jsonObject.put("heatEnergyKJ", Math.round(model.getHeatEnergy() * Constants.CALORIE_TO_JOULE));
		jsonObject.put("heatEnergyCarbohydrate", model.getHeatEnergyCarbohydrate());
		jsonObject.put("heatEnergyCarbohydrateKJ",
				Math.round(model.getHeatEnergyCarbohydrate() * Constants.CALORIE_TO_JOULE));
		jsonObject.put("heatEnergyFat", model.getHeatEnergyFat());
		jsonObject.put("heatEnergyFatKJ", Math.round(model.getHeatEnergyFat() * Constants.CALORIE_TO_JOULE));
		jsonObject.put("protein", model.getProtein());
		jsonObject.put("proteinAnimal", model.getProteinAnimal());
		jsonObject.put("proteinAnimalBeans", model.getProteinAnimalBeans());
		jsonObject.put("fat", model.getFat());
		jsonObject.put("carbohydrate", model.getCarbohydrate());
		jsonObject.put("vitaminA", model.getVitaminA());
		jsonObject.put("vitaminB1", model.getVitaminB1());
		jsonObject.put("vitaminB2", model.getVitaminB2());
		jsonObject.put("vitaminB6", model.getVitaminB6());
		jsonObject.put("vitaminB12", model.getVitaminB12());
		jsonObject.put("vitaminC", model.getVitaminC());
		jsonObject.put("vitaminE", model.getVitaminE());
		jsonObject.put("carotene", model.getCarotene());
		jsonObject.put("retinol", model.getRetinol());
		jsonObject.put("nicotinicCid", model.getNicotinicCid());
		jsonObject.put("calcium", model.getCalcium());
		jsonObject.put("iron", model.getIron());
		jsonObject.put("zinc", model.getZinc());
		jsonObject.put("iodine", model.getIodine());
		jsonObject.put("phosphorus", model.getPhosphorus());
		if (model.getMealType() != null) {
			jsonObject.put("mealType", model.getMealType());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		jsonObject.put("nodeId", model.getNodeId());
		jsonObject.put("semester", model.getSemester());
		jsonObject.put("year", model.getYear());
		jsonObject.put("month", model.getMonth());
		jsonObject.put("day", model.getDay());
		jsonObject.put("dayOfWeek", model.getDayOfWeek());
		jsonObject.put("week", model.getWeek());
		jsonObject.put("fullDay", model.getFullDay());
		if (model.getCreateTime() != null) {
			jsonObject.put("createTime", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_date", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_datetime", DateUtils.getDateTime(model.getCreateTime()));
		}
		return jsonObject;
	}

	private DietaryCountJsonFactory() {

	}

}
