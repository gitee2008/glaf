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
public class FoodCompositionJsonFactory {

	public static java.util.List<FoodComposition> arrayToList(JSONArray array) {
		java.util.List<FoodComposition> list = new java.util.ArrayList<FoodComposition>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			FoodComposition model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	public static FoodComposition jsonToObject(JSONObject jsonObject) {
		FoodComposition model = new FoodComposition();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getLong("id"));
		}
		if (jsonObject.containsKey("tenantId")) {
			model.setTenantId(jsonObject.getString("tenantId"));
		}
		if (jsonObject.containsKey("nodeId")) {
			model.setNodeId(jsonObject.getLong("nodeId"));
		}
		if (jsonObject.containsKey("treeId")) {
			model.setTreeId(jsonObject.getString("treeId"));
		}
		if (jsonObject.containsKey("name")) {
			model.setName(jsonObject.getString("name"));
		}
		if (jsonObject.containsKey("alias")) {
			model.setAlias(jsonObject.getString("alias"));
		}
		if (jsonObject.containsKey("code")) {
			model.setCode(jsonObject.getString("code"));
		}
		if (jsonObject.containsKey("discriminator")) {
			model.setDiscriminator(jsonObject.getString("discriminator"));
		}
		if (jsonObject.containsKey("description")) {
			model.setDescription(jsonObject.getString("description"));
		}
		if (jsonObject.containsKey("radical")) {
			model.setRadical(jsonObject.getDouble("radical"));
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
		if (jsonObject.containsKey("copper")) {
			model.setCopper(jsonObject.getDouble("copper"));
		}
		if (jsonObject.containsKey("magnesium")) {
			model.setMagnesium(jsonObject.getDouble("magnesium"));
		}
		if (jsonObject.containsKey("manganese")) {
			model.setManganese(jsonObject.getDouble("manganese"));
		}
		if (jsonObject.containsKey("potassium")) {
			model.setPotassium(jsonObject.getDouble("potassium"));
		}
		if (jsonObject.containsKey("selenium")) {
			model.setSelenium(jsonObject.getDouble("selenium"));
		}
		if (jsonObject.containsKey("sortNo")) {
			model.setSortNo(jsonObject.getInteger("sortNo"));
		}
		if (jsonObject.containsKey("dailyFlag")) {
			model.setDailyFlag(jsonObject.getString("dailyFlag"));
		}
		if (jsonObject.containsKey("colorFlag")) {
			model.setColorFlag(jsonObject.getString("colorFlag"));
		}
		if (jsonObject.containsKey("cerealFlag")) {
			model.setCerealFlag(jsonObject.getString("cerealFlag"));
		}
		if (jsonObject.containsKey("beansFlag")) {
			model.setBeansFlag(jsonObject.getString("beansFlag"));
		}
		if (jsonObject.containsKey("enableFlag")) {
			model.setEnableFlag(jsonObject.getString("enableFlag"));
		}
		if (jsonObject.containsKey("sysFlag")) {
			model.setSysFlag(jsonObject.getString("sysFlag"));
		}
		if (jsonObject.containsKey("useFlag")) {
			model.setUseFlag(jsonObject.getString("useFlag"));
		}

		return model;
	}

	public static JSONArray listToArray(java.util.List<FoodComposition> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (FoodComposition model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static JSONObject toJsonObject(FoodComposition model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		jsonObject.put("nodeId", model.getNodeId());
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		if (model.getTreeId() != null) {
			jsonObject.put("treeId", model.getTreeId());
		}
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getAlias() != null) {
			jsonObject.put("alias", model.getAlias());
		}
		if (model.getCode() != null) {
			jsonObject.put("code", model.getCode());
		}
		if (model.getDiscriminator() != null) {
			jsonObject.put("discriminator", model.getDiscriminator());
		}
		if (model.getDescription() != null) {
			jsonObject.put("description", model.getDescription());
		}
		jsonObject.put("radical", model.getRadical());
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
		jsonObject.put("copper", model.getCopper());
		jsonObject.put("magnesium", model.getMagnesium());
		jsonObject.put("manganese", model.getManganese());
		jsonObject.put("potassium", model.getPotassium());
		jsonObject.put("selenium", model.getSelenium());
		jsonObject.put("sortNo", model.getSortNo());
		if (model.getDailyFlag() != null) {
			jsonObject.put("dailyFlag", model.getDailyFlag());
		}
		if (model.getColorFlag() != null) {
			jsonObject.put("colorFlag", model.getColorFlag());
		}
		if (model.getCerealFlag() != null) {
			jsonObject.put("cerealFlag", model.getCerealFlag());
		}
		if (model.getBeansFlag() != null) {
			jsonObject.put("beansFlag", model.getBeansFlag());
		}
		if (model.getEnableFlag() != null) {
			jsonObject.put("enableFlag", model.getEnableFlag());
		}
		if (model.getSysFlag() != null) {
			jsonObject.put("sysFlag", model.getSysFlag());
		}
		if (model.getUseFlag() != null) {
			jsonObject.put("useFlag", model.getUseFlag());
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

	public static ObjectNode toObjectNode(FoodComposition model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		jsonObject.put("nodeId", model.getNodeId());
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		if (model.getTreeId() != null) {
			jsonObject.put("treeId", model.getTreeId());
		}
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getAlias() != null) {
			jsonObject.put("alias", model.getAlias());
		}
		if (model.getCode() != null) {
			jsonObject.put("code", model.getCode());
		}
		if (model.getDiscriminator() != null) {
			jsonObject.put("discriminator", model.getDiscriminator());
		}
		if (model.getDescription() != null) {
			jsonObject.put("description", model.getDescription());
		}
		jsonObject.put("radical", model.getRadical());
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
		jsonObject.put("copper", model.getCopper());
		jsonObject.put("magnesium", model.getMagnesium());
		jsonObject.put("manganese", model.getManganese());
		jsonObject.put("potassium", model.getPotassium());
		jsonObject.put("selenium", model.getSelenium());
		jsonObject.put("sortNo", model.getSortNo());
		if (model.getDailyFlag() != null) {
			jsonObject.put("dailyFlag", model.getDailyFlag());
		}
		if (model.getColorFlag() != null) {
			jsonObject.put("colorFlag", model.getColorFlag());
		}
		if (model.getCerealFlag() != null) {
			jsonObject.put("cerealFlag", model.getCerealFlag());
		}
		if (model.getBeansFlag() != null) {
			jsonObject.put("beansFlag", model.getBeansFlag());
		}
		if (model.getEnableFlag() != null) {
			jsonObject.put("enableFlag", model.getEnableFlag());
		}
		if (model.getSysFlag() != null) {
			jsonObject.put("sysFlag", model.getSysFlag());
		}
		if (model.getUseFlag() != null) {
			jsonObject.put("useFlag", model.getUseFlag());
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

	private FoodCompositionJsonFactory() {

	}

}
