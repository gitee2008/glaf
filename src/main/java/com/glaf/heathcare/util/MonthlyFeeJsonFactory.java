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
public class MonthlyFeeJsonFactory {

	public static MonthlyFee jsonToObject(JSONObject jsonObject) {
		MonthlyFee model = new MonthlyFee();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getLong("id"));
		}
		if (jsonObject.containsKey("tenantId")) {
			model.setTenantId(jsonObject.getString("tenantId"));
		}
		if (jsonObject.containsKey("lastMonthSurplus")) {
			model.setLastMonthSurplus(jsonObject.getDouble("lastMonthSurplus"));
		}
		if (jsonObject.containsKey("monthLeft")) {
			model.setMonthLeft(jsonObject.getDouble("monthLeft"));
		}
		if (jsonObject.containsKey("monthTotalLeft")) {
			model.setMonthTotalLeft(jsonObject.getDouble("monthTotalLeft"));
		}
		if (jsonObject.containsKey("leftPercent")) {
			model.setLeftPercent(jsonObject.getDouble("leftPercent"));
		}
		if (jsonObject.containsKey("exceedPercent")) {
			model.setExceedPercent(jsonObject.getDouble("exceedPercent"));
		}
		if (jsonObject.containsKey("personMonthlyFee")) {
			model.setPersonMonthlyFee(jsonObject.getDouble("personMonthlyFee"));
		}
		if (jsonObject.containsKey("fuelFee")) {
			model.setFuelFee(jsonObject.getDouble("fuelFee"));
		}
		if (jsonObject.containsKey("laborFee")) {
			model.setLaborFee(jsonObject.getDouble("laborFee"));
		}
		if (jsonObject.containsKey("dessertFee")) {
			model.setDessertFee(jsonObject.getDouble("dessertFee"));
		}
		if (jsonObject.containsKey("otherFee")) {
			model.setOtherFee(jsonObject.getDouble("otherFee"));
		}
		if (jsonObject.containsKey("workDay")) {
			model.setWorkDay(jsonObject.getInteger("workDay"));
		}
		if (jsonObject.containsKey("totalRepastPerson")) {
			model.setTotalRepastPerson(jsonObject.getInteger("totalRepastPerson"));
		}
		if (jsonObject.containsKey("remark")) {
			model.setRemark(jsonObject.getString("remark"));
		}
		if (jsonObject.containsKey("year")) {
			model.setYear(jsonObject.getInteger("year"));
		}
		if (jsonObject.containsKey("month")) {
			model.setMonth(jsonObject.getInteger("month"));
		}
		if (jsonObject.containsKey("semester")) {
			model.setSemester(jsonObject.getInteger("semester"));
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

	public static JSONObject toJsonObject(MonthlyFee model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		jsonObject.put("lastMonthSurplus", model.getLastMonthSurplus());
		jsonObject.put("monthLeft", model.getMonthLeft());
		jsonObject.put("monthTotalLeft", model.getMonthTotalLeft());
		jsonObject.put("leftPercent", model.getLeftPercent());
		jsonObject.put("exceedPercent", model.getExceedPercent());
		jsonObject.put("personMonthlyFee", model.getPersonMonthlyFee());
		jsonObject.put("fuelFee", model.getFuelFee());
		jsonObject.put("laborFee", model.getLaborFee());
		jsonObject.put("dessertFee", model.getDessertFee());
		jsonObject.put("otherFee", model.getOtherFee());
		jsonObject.put("workDay", model.getWorkDay());
		jsonObject.put("totalRepastPerson", model.getTotalRepastPerson());
		jsonObject.put("year", model.getYear());
		jsonObject.put("month", model.getMonth());
		jsonObject.put("semester", model.getSemester());

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

	public static ObjectNode toObjectNode(MonthlyFee model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		jsonObject.put("lastMonthSurplus", model.getLastMonthSurplus());
		jsonObject.put("monthLeft", model.getMonthLeft());
		jsonObject.put("monthTotalLeft", model.getMonthTotalLeft());
		jsonObject.put("leftPercent", model.getLeftPercent());
		jsonObject.put("exceedPercent", model.getExceedPercent());
		jsonObject.put("personMonthlyFee", model.getPersonMonthlyFee());
		jsonObject.put("fuelFee", model.getFuelFee());
		jsonObject.put("laborFee", model.getLaborFee());
		jsonObject.put("dessertFee", model.getDessertFee());
		jsonObject.put("otherFee", model.getOtherFee());
		jsonObject.put("workDay", model.getWorkDay());
		jsonObject.put("totalRepastPerson", model.getTotalRepastPerson());
		jsonObject.put("year", model.getYear());
		jsonObject.put("month", model.getMonth());
		jsonObject.put("semester", model.getSemester());

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

	public static JSONArray listToArray(java.util.List<MonthlyFee> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (MonthlyFee model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static java.util.List<MonthlyFee> arrayToList(JSONArray array) {
		java.util.List<MonthlyFee> list = new java.util.ArrayList<MonthlyFee>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			MonthlyFee model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	private MonthlyFeeJsonFactory() {

	}

}
