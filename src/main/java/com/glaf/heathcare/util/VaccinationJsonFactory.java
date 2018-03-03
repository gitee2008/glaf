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
public class VaccinationJsonFactory {

	public static Vaccination jsonToObject(JSONObject jsonObject) {
		Vaccination model = new Vaccination();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getLong("id"));
		}
		if (jsonObject.containsKey("tenantId")) {
			model.setTenantId(jsonObject.getString("tenantId"));
		}
		if (jsonObject.containsKey("gradeId")) {
			model.setGradeId(jsonObject.getString("gradeId"));
		}
		if (jsonObject.containsKey("personId")) {
			model.setPersonId(jsonObject.getString("personId"));
		}
		if (jsonObject.containsKey("name")) {
			model.setName(jsonObject.getString("name"));
		}
		if (jsonObject.containsKey("sex")) {
			model.setSex(jsonObject.getString("sex"));
		}
		if (jsonObject.containsKey("vaccine")) {
			model.setVaccine(jsonObject.getString("vaccine"));
		}
		if (jsonObject.containsKey("sortNo")) {
			model.setSortNo(jsonObject.getInteger("sortNo"));
		}
		if (jsonObject.containsKey("inoculateDate")) {
			model.setInoculateDate(jsonObject.getDate("inoculateDate"));
		}
		if (jsonObject.containsKey("doctor")) {
			model.setDoctor(jsonObject.getString("doctor"));
		}
		if (jsonObject.containsKey("year")) {
			model.setYear(jsonObject.getInteger("year"));
		}
		if (jsonObject.containsKey("month")) {
			model.setMonth(jsonObject.getInteger("month"));
		}
		if (jsonObject.containsKey("createBy")) {
			model.setCreateBy(jsonObject.getString("createBy"));
		}
		if (jsonObject.containsKey("createTime")) {
			model.setCreateTime(jsonObject.getDate("createTime"));
		}

		return model;
	}

	public static JSONObject toJsonObject(Vaccination model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		if (model.getGradeId() != null) {
			jsonObject.put("gradeId", model.getGradeId());
		}
		if (model.getPersonId() != null) {
			jsonObject.put("personId", model.getPersonId());
		}
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getSex() != null) {
			jsonObject.put("sex", model.getSex());
		}
		if (model.getVaccine() != null) {
			jsonObject.put("vaccine", model.getVaccine());
		}
		jsonObject.put("sortNo", model.getSortNo());
		if (model.getInoculateDate() != null) {
			jsonObject.put("inoculateDate", DateUtils.getDate(model.getInoculateDate()));
			jsonObject.put("inoculateDate_date", DateUtils.getDate(model.getInoculateDate()));
			jsonObject.put("inoculateDate_datetime", DateUtils.getDateTime(model.getInoculateDate()));
		}
		if (model.getDoctor() != null) {
			jsonObject.put("doctor", model.getDoctor());
		}
		jsonObject.put("year", model.getYear());
		jsonObject.put("month", model.getMonth());
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

	public static ObjectNode toObjectNode(Vaccination model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		if (model.getGradeId() != null) {
			jsonObject.put("gradeId", model.getGradeId());
		}
		if (model.getPersonId() != null) {
			jsonObject.put("personId", model.getPersonId());
		}
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getSex() != null) {
			jsonObject.put("sex", model.getSex());
		}
		if (model.getVaccine() != null) {
			jsonObject.put("vaccine", model.getVaccine());
		}
		jsonObject.put("sortNo", model.getSortNo());
		if (model.getInoculateDate() != null) {
			jsonObject.put("inoculateDate", DateUtils.getDate(model.getInoculateDate()));
			jsonObject.put("inoculateDate_date", DateUtils.getDate(model.getInoculateDate()));
			jsonObject.put("inoculateDate_datetime", DateUtils.getDateTime(model.getInoculateDate()));
		}
		if (model.getDoctor() != null) {
			jsonObject.put("doctor", model.getDoctor());
		}
		jsonObject.put("year", model.getYear());
		jsonObject.put("month", model.getMonth());
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

	public static JSONArray listToArray(java.util.List<Vaccination> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (Vaccination model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static java.util.List<Vaccination> arrayToList(JSONArray array) {
		java.util.List<Vaccination> list = new java.util.ArrayList<Vaccination>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			Vaccination model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	private VaccinationJsonFactory() {

	}

}
