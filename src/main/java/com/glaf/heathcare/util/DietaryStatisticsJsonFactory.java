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
public class DietaryStatisticsJsonFactory {

	public static java.util.List<DietaryStatistics> arrayToList(JSONArray array) {
		java.util.List<DietaryStatistics> list = new java.util.ArrayList<DietaryStatistics>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			DietaryStatistics model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	public static DietaryStatistics jsonToObject(JSONObject jsonObject) {
		DietaryStatistics model = new DietaryStatistics();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getLong("id"));
		}
		if (jsonObject.containsKey("tenantId")) {
			model.setTenantId(jsonObject.getString("tenantId"));
		}
		if (jsonObject.containsKey("name")) {
			model.setName(jsonObject.getString("name"));
		}
		if (jsonObject.containsKey("title")) {
			model.setTitle(jsonObject.getString("title"));
		}
		if (jsonObject.containsKey("value")) {
			model.setValue(jsonObject.getDouble("value"));
		}
		if (jsonObject.containsKey("mealType")) {
			model.setMealType(jsonObject.getString("mealType"));
		}
		if (jsonObject.containsKey("sysFlag")) {
			model.setSysFlag(jsonObject.getString("sysFlag"));
		}
		if (jsonObject.containsKey("type")) {
			model.setType(jsonObject.getString("type"));
		}
		if (jsonObject.containsKey("dayOfWeek")) {
			model.setDayOfWeek(jsonObject.getInteger("dayOfWeek"));
		}
		if (jsonObject.containsKey("semester")) {
			model.setSemester(jsonObject.getInteger("semester"));
		}
		if (jsonObject.containsKey("year")) {
			model.setYear(jsonObject.getInteger("year"));
		}
		if (jsonObject.containsKey("week")) {
			model.setWeek(jsonObject.getInteger("week"));
		}
		if (jsonObject.containsKey("batchNo")) {
			model.setBatchNo(jsonObject.getString("batchNo"));
		}
		if (jsonObject.containsKey("sortNo")) {
			model.setSortNo(jsonObject.getInteger("sortNo"));
		}
		if (jsonObject.containsKey("suitNo")) {
			model.setSuitNo(jsonObject.getInteger("suitNo"));
		}
		if (jsonObject.containsKey("createTime")) {
			model.setCreateTime(jsonObject.getDate("createTime"));
		}

		return model;
	}

	public static JSONArray listToArray(java.util.List<DietaryStatistics> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (DietaryStatistics model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static JSONObject toJsonObject(DietaryStatistics model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getTitle() != null) {
			jsonObject.put("title", model.getTitle());
		}
		jsonObject.put("value", model.getValue());
		if (model.getMealType() != null) {
			jsonObject.put("mealType", model.getMealType());
		}
		if (model.getSysFlag() != null) {
			jsonObject.put("sysFlag", model.getSysFlag());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		jsonObject.put("dayOfWeek", model.getDayOfWeek());
		jsonObject.put("semester", model.getSemester());
		jsonObject.put("year", model.getYear());
		jsonObject.put("week", model.getWeek());
		if (model.getBatchNo() != null) {
			jsonObject.put("batchNo", model.getBatchNo());
		}
		jsonObject.put("sortNo", model.getSortNo());
		jsonObject.put("suitNo", model.getSuitNo());
		if (model.getCreateTime() != null) {
			jsonObject.put("createTime", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_date", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_datetime", DateUtils.getDateTime(model.getCreateTime()));
		}
		return jsonObject;
	}

	public static ObjectNode toObjectNode(DietaryStatistics model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getTitle() != null) {
			jsonObject.put("title", model.getTitle());
		}
		jsonObject.put("value", model.getValue());
		if (model.getMealType() != null) {
			jsonObject.put("mealType", model.getMealType());
		}
		if (model.getSysFlag() != null) {
			jsonObject.put("sysFlag", model.getSysFlag());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		jsonObject.put("dayOfWeek", model.getDayOfWeek());
		jsonObject.put("semester", model.getSemester());
		jsonObject.put("year", model.getYear());
		jsonObject.put("week", model.getWeek());
		if (model.getBatchNo() != null) {
			jsonObject.put("batchNo", model.getBatchNo());
		}
		jsonObject.put("sortNo", model.getSortNo());
		jsonObject.put("suitNo", model.getSuitNo());
		if (model.getCreateTime() != null) {
			jsonObject.put("createTime", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_date", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_datetime", DateUtils.getDateTime(model.getCreateTime()));
		}
		return jsonObject;
	}

	private DietaryStatisticsJsonFactory() {

	}

}
