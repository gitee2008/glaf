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
public class DietaryItemJsonFactory {

	public static DietaryItem jsonToObject(JSONObject jsonObject) {
		DietaryItem model = new DietaryItem();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getLong("id"));
		}
		if (jsonObject.containsKey("tenantId")) {
			model.setTenantId(jsonObject.getString("tenantId"));
		}
		if (jsonObject.containsKey("name")) {
			model.setName(jsonObject.getString("name"));
		}
		if (jsonObject.containsKey("description")) {
			model.setDescription(jsonObject.getString("description"));
		}
		if (jsonObject.containsKey("foodId")) {
			model.setFoodId(jsonObject.getLong("foodId"));
		}
		if (jsonObject.containsKey("foodName")) {
			model.setFoodName(jsonObject.getString("foodName"));
		}
		if (jsonObject.containsKey("dietaryId")) {
			model.setDietaryId(jsonObject.getLong("dietaryId"));
		}
		if (jsonObject.containsKey("templateId")) {
			model.setTemplateId(jsonObject.getLong("templateId"));
		}
		if (jsonObject.containsKey("typeId")) {
			model.setTypeId(jsonObject.getLong("typeId"));
		}
		if (jsonObject.containsKey("quantity")) {
			model.setQuantity(jsonObject.getDouble("quantity"));
		}
		if (jsonObject.containsKey("unit")) {
			model.setUnit(jsonObject.getString("unit"));
		}
		if (jsonObject.containsKey("fullDay")) {
			model.setFullDay(jsonObject.getInteger("fullDay"));
		}
		if (jsonObject.containsKey("createBy")) {
			model.setCreateBy(jsonObject.getString("createBy"));
		}
		if (jsonObject.containsKey("createTime")) {
			model.setCreateTime(jsonObject.getDate("createTime"));
		}

		return model;
	}

	public static JSONObject toJsonObject(DietaryItem model) {
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
		if (model.getDescription() != null) {
			jsonObject.put("description", model.getDescription());
		}
		jsonObject.put("foodId", model.getFoodId());
		if (model.getFoodName() != null) {
			jsonObject.put("foodName", model.getFoodName());
		}
		jsonObject.put("dietaryId", model.getDietaryId());
		jsonObject.put("templateId", model.getTemplateId());
		jsonObject.put("typeId", model.getTypeId());
		jsonObject.put("quantity", model.getQuantity());
		if (model.getUnit() != null) {
			jsonObject.put("unit", model.getUnit());
		}
		jsonObject.put("fullDay", model.getFullDay());
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

	public static ObjectNode toObjectNode(DietaryItem model) {
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
		if (model.getDescription() != null) {
			jsonObject.put("description", model.getDescription());
		}
		jsonObject.put("foodId", model.getFoodId());
		if (model.getFoodName() != null) {
			jsonObject.put("foodName", model.getFoodName());
		}
		jsonObject.put("dietaryId", model.getDietaryId());
		jsonObject.put("templateId", model.getTemplateId());
		jsonObject.put("typeId", model.getTypeId());
		jsonObject.put("quantity", model.getQuantity());
		if (model.getUnit() != null) {
			jsonObject.put("unit", model.getUnit());
		}
		jsonObject.put("fullDay", model.getFullDay());
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

	public static JSONArray listToArray(java.util.List<DietaryItem> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (DietaryItem model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static java.util.List<DietaryItem> arrayToList(JSONArray array) {
		java.util.List<DietaryItem> list = new java.util.ArrayList<DietaryItem>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			DietaryItem model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	private DietaryItemJsonFactory() {

	}

}
