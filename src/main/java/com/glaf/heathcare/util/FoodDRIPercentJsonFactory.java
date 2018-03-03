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
public class FoodDRIPercentJsonFactory {

	public static FoodDRIPercent jsonToObject(JSONObject jsonObject) {
		FoodDRIPercent model = new FoodDRIPercent();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getLong("id"));
		}
		if (jsonObject.containsKey("name")) {
			model.setName(jsonObject.getString("name"));
		}
		if (jsonObject.containsKey("description")) {
			model.setDescription(jsonObject.getString("description"));
		}
		if (jsonObject.containsKey("ageGroup")) {
			model.setAgeGroup(jsonObject.getString("ageGroup"));
		}
		if (jsonObject.containsKey("type")) {
			model.setType(jsonObject.getString("type"));
		}
		if (jsonObject.containsKey("typeId")) {
			model.setTypeId(jsonObject.getLong("typeId"));
		}
		if (jsonObject.containsKey("heatEnergy")) {
			model.setHeatEnergy(jsonObject.getDouble("heatEnergy"));
		}
		if (jsonObject.containsKey("protein")) {
			model.setProtein(jsonObject.getDouble("protein"));
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
		if (jsonObject.containsKey("sortNo")) {
			model.setSortNo(jsonObject.getInteger("sortNo"));
		}
		if (jsonObject.containsKey("enableFlag")) {
			model.setEnableFlag(jsonObject.getString("enableFlag"));
		}
		if (jsonObject.containsKey("createBy")) {
			model.setCreateBy(jsonObject.getString("createBy"));
		}
		if (jsonObject.containsKey("createTime")) {
			model.setCreateTime(jsonObject.getDate("createTime"));
		}
		if (jsonObject.containsKey("updateBy")) {
			model.setUpdateBy(jsonObject.getString("updateBy"));
		}
		if (jsonObject.containsKey("updateTime")) {
			model.setUpdateTime(jsonObject.getDate("updateTime"));
		}

		return model;
	}

	public static JSONObject toJsonObject(FoodDRIPercent model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getDescription() != null) {
			jsonObject.put("description", model.getDescription());
		}
		if (model.getAgeGroup() != null) {
			jsonObject.put("ageGroup", model.getAgeGroup());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		jsonObject.put("typeId", model.getTypeId());
		jsonObject.put("heatEnergy", model.getHeatEnergy());
		jsonObject.put("protein", model.getProtein());
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
		jsonObject.put("sortNo", model.getSortNo());
		if (model.getEnableFlag() != null) {
			jsonObject.put("enableFlag", model.getEnableFlag());
		}
		if (model.getCreateBy() != null) {
			jsonObject.put("createBy", model.getCreateBy());
		}
		if (model.getCreateTime() != null) {
			jsonObject.put("createTime", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_date", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_datetime", DateUtils.getDateTime(model.getCreateTime()));
		}
		if (model.getUpdateBy() != null) {
			jsonObject.put("updateBy", model.getUpdateBy());
		}
		if (model.getUpdateTime() != null) {
			jsonObject.put("updateTime", DateUtils.getDate(model.getUpdateTime()));
			jsonObject.put("updateTime_date", DateUtils.getDate(model.getUpdateTime()));
			jsonObject.put("updateTime_datetime", DateUtils.getDateTime(model.getUpdateTime()));
		}
		return jsonObject;
	}

	public static ObjectNode toObjectNode(FoodDRIPercent model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getDescription() != null) {
			jsonObject.put("description", model.getDescription());
		}
		if (model.getAgeGroup() != null) {
			jsonObject.put("ageGroup", model.getAgeGroup());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		jsonObject.put("typeId", model.getTypeId());
		jsonObject.put("heatEnergy", model.getHeatEnergy());
		jsonObject.put("protein", model.getProtein());
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
		jsonObject.put("sortNo", model.getSortNo());
		if (model.getEnableFlag() != null) {
			jsonObject.put("enableFlag", model.getEnableFlag());
		}
		if (model.getCreateBy() != null) {
			jsonObject.put("createBy", model.getCreateBy());
		}
		if (model.getCreateTime() != null) {
			jsonObject.put("createTime", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_date", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_datetime", DateUtils.getDateTime(model.getCreateTime()));
		}
		if (model.getUpdateBy() != null) {
			jsonObject.put("updateBy", model.getUpdateBy());
		}
		if (model.getUpdateTime() != null) {
			jsonObject.put("updateTime", DateUtils.getDate(model.getUpdateTime()));
			jsonObject.put("updateTime_date", DateUtils.getDate(model.getUpdateTime()));
			jsonObject.put("updateTime_datetime", DateUtils.getDateTime(model.getUpdateTime()));
		}
		return jsonObject;
	}

	public static JSONArray listToArray(java.util.List<FoodDRIPercent> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (FoodDRIPercent model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static java.util.List<FoodDRIPercent> arrayToList(JSONArray array) {
		java.util.List<FoodDRIPercent> list = new java.util.ArrayList<FoodDRIPercent>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			FoodDRIPercent model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	private FoodDRIPercentJsonFactory() {

	}

}
