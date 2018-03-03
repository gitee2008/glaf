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
public class DietarySurveyJsonFactory {

	public static DietarySurvey jsonToObject(JSONObject jsonObject) {
		DietarySurvey model = new DietarySurvey();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getLong("id"));
		}
		if (jsonObject.containsKey("tenantId")) {
			model.setTenantId(jsonObject.getString("tenantId"));
		}
		if (jsonObject.containsKey("foodId")) {
			model.setFoodId(jsonObject.getLong("foodId"));
		}
		if (jsonObject.containsKey("foodName")) {
			model.setFoodName(jsonObject.getString("foodName"));
		}
		if (jsonObject.containsKey("foodNodeId")) {
			model.setFoodNodeId(jsonObject.getLong("foodNodeId"));
		}
		if (jsonObject.containsKey("typeId")) {
			model.setTypeId(jsonObject.getLong("typeId"));
		}
		if (jsonObject.containsKey("mealTime")) {
			model.setMealTime(jsonObject.getDate("mealTime"));
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
		if (jsonObject.containsKey("fullDay")) {
			model.setFullDay(jsonObject.getInteger("fullDay"));
		}
		if (jsonObject.containsKey("weight")) {
			model.setWeight(jsonObject.getDouble("weight"));
		}
		if (jsonObject.containsKey("weightPercent")) {
			model.setWeightPercent(jsonObject.getDouble("weightPercent"));
		}
		if (jsonObject.containsKey("cookedWeight")) {
			model.setCookedWeight(jsonObject.getDouble("cookedWeight"));
		}
		if (jsonObject.containsKey("weightCookedPercent")) {
			model.setWeightCookedPercent(jsonObject.getDouble("weightCookedPercent"));
		}
		if (jsonObject.containsKey("price")) {
			model.setPrice(jsonObject.getDouble("price"));
		}
		if (jsonObject.containsKey("totalPrice")) {
			model.setTotalPrice(jsonObject.getDouble("totalPrice"));
		}
		if (jsonObject.containsKey("remark")) {
			model.setRemark(jsonObject.getString("remark"));
		}
		if (jsonObject.containsKey("createBy")) {
			model.setCreateBy(jsonObject.getString("createBy"));
		}
		if (jsonObject.containsKey("createTime")) {
			model.setCreateTime(jsonObject.getDate("createTime"));
		}

		return model;
	}

	public static JSONObject toJsonObject(DietarySurvey model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		jsonObject.put("foodId", model.getFoodId());
		if (model.getFoodName() != null) {
			jsonObject.put("foodName", model.getFoodName());
		}
		jsonObject.put("foodNodeId", model.getFoodNodeId());
		jsonObject.put("typeId", model.getTypeId());
		if (model.getMealTime() != null) {
			jsonObject.put("mealTime", DateUtils.getDate(model.getMealTime()));
			jsonObject.put("mealTime_date", DateUtils.getDate(model.getMealTime()));
			jsonObject.put("mealTime_datetime", DateUtils.getDateTime(model.getMealTime()));
		}
		jsonObject.put("semester", model.getSemester());
		jsonObject.put("year", model.getYear());
		jsonObject.put("month", model.getMonth());
		jsonObject.put("fullDay", model.getFullDay());
		jsonObject.put("weight", model.getWeight());
		jsonObject.put("weightPercent", model.getWeightPercent());
		jsonObject.put("cookedWeight", model.getCookedWeight());
		jsonObject.put("weightCookedPercent", model.getWeightCookedPercent());
		jsonObject.put("price", model.getPrice());
		jsonObject.put("totalPrice", model.getTotalPrice());
		if (model.getRemark() != null) {
			jsonObject.put("remark", model.getRemark());
		}
		if (model.getCreateBy() != null) {
			jsonObject.put("createBy", model.getCreateBy());
		}
		if (model.getCreateTime() != null) {
			jsonObject.put("createTime", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_date", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_datetime", DateUtils.getDateTime(model.getCreateTime()));
		}
		return jsonObject;
	}

	public static ObjectNode toObjectNode(DietarySurvey model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		jsonObject.put("foodId", model.getFoodId());
		if (model.getFoodName() != null) {
			jsonObject.put("foodName", model.getFoodName());
		}
		jsonObject.put("foodNodeId", model.getFoodNodeId());
		jsonObject.put("typeId", model.getTypeId());
		if (model.getMealTime() != null) {
			jsonObject.put("mealTime", DateUtils.getDate(model.getMealTime()));
			jsonObject.put("mealTime_date", DateUtils.getDate(model.getMealTime()));
			jsonObject.put("mealTime_datetime", DateUtils.getDateTime(model.getMealTime()));
		}
		jsonObject.put("semester", model.getSemester());
		jsonObject.put("year", model.getYear());
		jsonObject.put("month", model.getMonth());
		jsonObject.put("fullDay", model.getFullDay());
		jsonObject.put("weight", model.getWeight());
		jsonObject.put("weightPercent", model.getWeightPercent());
		jsonObject.put("cookedWeight", model.getCookedWeight());
		jsonObject.put("weightCookedPercent", model.getWeightCookedPercent());
		jsonObject.put("price", model.getPrice());
		jsonObject.put("totalPrice", model.getTotalPrice());
		if (model.getRemark() != null) {
			jsonObject.put("remark", model.getRemark());
		}
		if (model.getCreateBy() != null) {
			jsonObject.put("createBy", model.getCreateBy());
		}
		if (model.getCreateTime() != null) {
			jsonObject.put("createTime", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_date", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_datetime", DateUtils.getDateTime(model.getCreateTime()));
		}
		return jsonObject;
	}

	public static JSONArray listToArray(java.util.List<DietarySurvey> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (DietarySurvey model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static java.util.List<DietarySurvey> arrayToList(JSONArray array) {
		java.util.List<DietarySurvey> list = new java.util.ArrayList<DietarySurvey>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			DietarySurvey model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	private DietarySurveyJsonFactory() {

	}

}
