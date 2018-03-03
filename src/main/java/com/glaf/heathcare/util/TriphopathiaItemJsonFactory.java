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
public class TriphopathiaItemJsonFactory {

	public static java.util.List<TriphopathiaItem> arrayToList(JSONArray array) {
		java.util.List<TriphopathiaItem> list = new java.util.ArrayList<TriphopathiaItem>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			TriphopathiaItem model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	public static TriphopathiaItem jsonToObject(JSONObject jsonObject) {
		TriphopathiaItem model = new TriphopathiaItem();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getLong("id"));
		}
		
		if (jsonObject.containsKey("triphopathiaId")) {
			model.setTriphopathiaId(jsonObject.getLong("triphopathiaId"));
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
		if (jsonObject.containsKey("type")) {
			model.setType(jsonObject.getString("type"));
		}
		if (jsonObject.containsKey("height")) {
			model.setHeight(jsonObject.getDouble("height"));
		}
		if (jsonObject.containsKey("heightLevel")) {
			model.setHeightLevel(jsonObject.getInteger("heightLevel"));
		}
		if (jsonObject.containsKey("heightEvaluate")) {
			model.setHeightEvaluate(jsonObject.getString("heightEvaluate"));
		}
		if (jsonObject.containsKey("weight")) {
			model.setWeight(jsonObject.getDouble("weight"));
		}
		if (jsonObject.containsKey("weightLevel")) {
			model.setWeightLevel(jsonObject.getInteger("weightLevel"));
		}
		if (jsonObject.containsKey("weightEvaluate")) {
			model.setWeightEvaluate(jsonObject.getString("weightEvaluate"));
		}
		if (jsonObject.containsKey("weightHeightPercent")) {
			model.setWeightHeightPercent(jsonObject.getDouble("weightHeightPercent"));
		}
		if (jsonObject.containsKey("bmi")) {
			model.setBmi(jsonObject.getDouble("bmi"));
		}
		if (jsonObject.containsKey("bmiIndex")) {
			model.setBmiIndex(jsonObject.getDouble("bmiIndex"));
		}
		if (jsonObject.containsKey("bmiEvaluate")) {
			model.setBmiEvaluate(jsonObject.getString("bmiEvaluate"));
		}
		if (jsonObject.containsKey("symptom")) {
			model.setSymptom(jsonObject.getString("symptom"));
		}
		if (jsonObject.containsKey("suggest")) {
			model.setSuggest(jsonObject.getString("suggest"));
		}
		if (jsonObject.containsKey("result")) {
			model.setResult(jsonObject.getString("result"));
		}
		if (jsonObject.containsKey("evaluate")) {
			model.setEvaluate(jsonObject.getString("evaluate"));
		}
		if (jsonObject.containsKey("ageOfTheMoon")) {
			model.setAgeOfTheMoon(jsonObject.getInteger("ageOfTheMoon"));
		}
		if (jsonObject.containsKey("remark")) {
			model.setRemark(jsonObject.getString("remark"));
		}
		if (jsonObject.containsKey("checkDate")) {
			model.setCheckDate(jsonObject.getDate("checkDate"));
		}
		if (jsonObject.containsKey("checkDoctor")) {
			model.setCheckDoctor(jsonObject.getString("checkDoctor"));
		}
		if (jsonObject.containsKey("checkDoctorId")) {
			model.setCheckDoctorId(jsonObject.getString("checkDoctorId"));
		}
		if (jsonObject.containsKey("createBy")) {
			model.setCreateBy(jsonObject.getString("createBy"));
		}
		if (jsonObject.containsKey("createTime")) {
			model.setCreateTime(jsonObject.getDate("createTime"));
		}

		return model;
	}

	public static JSONArray listToArray(java.util.List<TriphopathiaItem> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (TriphopathiaItem model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static JSONObject toJsonObject(TriphopathiaItem model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		jsonObject.put("triphopathiaId", model.getTriphopathiaId());
		
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
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		jsonObject.put("height", model.getHeight());
		jsonObject.put("heightLevel", model.getHeightLevel());
		if (model.getHeightEvaluate() != null) {
			jsonObject.put("heightEvaluate", model.getHeightEvaluate());
		}
		jsonObject.put("weight", model.getWeight());
		jsonObject.put("weightLevel", model.getWeightLevel());
		if (model.getWeightEvaluate() != null) {
			jsonObject.put("weightEvaluate", model.getWeightEvaluate());
		}
		jsonObject.put("weightHeightPercent", model.getWeightHeightPercent());
		jsonObject.put("bmi", model.getBmi());
		jsonObject.put("bmiIndex", model.getBmiIndex());
		if (model.getBmiEvaluate() != null) {
			jsonObject.put("bmiEvaluate", model.getBmiEvaluate());
		}
		if (model.getSymptom() != null) {
			jsonObject.put("symptom", model.getSymptom());
		}
		if (model.getSuggest() != null) {
			jsonObject.put("suggest", model.getSuggest());
		}
		if (model.getResult() != null) {
			jsonObject.put("result", model.getResult());
		}
		if (model.getEvaluate() != null) {
			jsonObject.put("evaluate", model.getEvaluate());
		}
		jsonObject.put("ageOfTheMoon", model.getAgeOfTheMoon());
		if (model.getRemark() != null) {
			jsonObject.put("remark", model.getRemark());
		}
		if (model.getCheckDate() != null) {
			jsonObject.put("checkDate", DateUtils.getDate(model.getCheckDate()));
			jsonObject.put("checkDate_date", DateUtils.getDate(model.getCheckDate()));
			jsonObject.put("checkDate_datetime", DateUtils.getDateTime(model.getCheckDate()));
		}
		if (model.getCheckDoctor() != null) {
			jsonObject.put("checkDoctor", model.getCheckDoctor());
		}
		if (model.getCheckDoctorId() != null) {
			jsonObject.put("checkDoctorId", model.getCheckDoctorId());
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

	public static ObjectNode toObjectNode(TriphopathiaItem model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		jsonObject.put("triphopathiaId", model.getTriphopathiaId());
		
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
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		jsonObject.put("height", model.getHeight());
		jsonObject.put("heightLevel", model.getHeightLevel());
		if (model.getHeightEvaluate() != null) {
			jsonObject.put("heightEvaluate", model.getHeightEvaluate());
		}
		jsonObject.put("weight", model.getWeight());
		jsonObject.put("weightLevel", model.getWeightLevel());
		if (model.getWeightEvaluate() != null) {
			jsonObject.put("weightEvaluate", model.getWeightEvaluate());
		}
		jsonObject.put("weightHeightPercent", model.getWeightHeightPercent());
		jsonObject.put("bmi", model.getBmi());
		jsonObject.put("bmiIndex", model.getBmiIndex());
		if (model.getBmiEvaluate() != null) {
			jsonObject.put("bmiEvaluate", model.getBmiEvaluate());
		}
		if (model.getSymptom() != null) {
			jsonObject.put("symptom", model.getSymptom());
		}
		if (model.getSuggest() != null) {
			jsonObject.put("suggest", model.getSuggest());
		}
		if (model.getResult() != null) {
			jsonObject.put("result", model.getResult());
		}
		if (model.getEvaluate() != null) {
			jsonObject.put("evaluate", model.getEvaluate());
		}
		jsonObject.put("ageOfTheMoon", model.getAgeOfTheMoon());
		if (model.getRemark() != null) {
			jsonObject.put("remark", model.getRemark());
		}
		if (model.getCheckDate() != null) {
			jsonObject.put("checkDate", DateUtils.getDate(model.getCheckDate()));
			jsonObject.put("checkDate_date", DateUtils.getDate(model.getCheckDate()));
			jsonObject.put("checkDate_datetime", DateUtils.getDateTime(model.getCheckDate()));
		}
		if (model.getCheckDoctor() != null) {
			jsonObject.put("checkDoctor", model.getCheckDoctor());
		}
		if (model.getCheckDoctorId() != null) {
			jsonObject.put("checkDoctorId", model.getCheckDoctorId());
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

	private TriphopathiaItemJsonFactory() {

	}

}
