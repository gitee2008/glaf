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
public class GrowthStandardJsonFactory {

	public static java.util.List<GrowthStandard> arrayToList(JSONArray array) {
		java.util.List<GrowthStandard> list = new java.util.ArrayList<GrowthStandard>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			GrowthStandard model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	public static GrowthStandard jsonToObject(JSONObject jsonObject) {
		GrowthStandard model = new GrowthStandard();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getLong("id"));
		}
		if (jsonObject.containsKey("age")) {
			model.setAge(jsonObject.getInteger("age"));
		}
		if (jsonObject.containsKey("ageOfTheMoon")) {
			model.setAgeOfTheMoon(jsonObject.getInteger("ageOfTheMoon"));
		}
		if (jsonObject.containsKey("complexKey")) {
			model.setComplexKey(jsonObject.getString("complexKey"));
		}
		if (jsonObject.containsKey("month")) {
			model.setMonth(jsonObject.getInteger("month"));
		}
		if (jsonObject.containsKey("sex")) {
			model.setSex(jsonObject.getString("sex"));
		}
		if (jsonObject.containsKey("height")) {
			model.setHeight(jsonObject.getDouble("height"));
		}
		if (jsonObject.containsKey("weight")) {
			model.setWeight(jsonObject.getDouble("weight"));
		}
		if (jsonObject.containsKey("percent3")) {
			model.setPercent3(jsonObject.getDouble("percent3"));
		}
		if (jsonObject.containsKey("percent15")) {
			model.setPercent15(jsonObject.getDouble("percent15"));
		}
		if (jsonObject.containsKey("percent50")) {
			model.setPercent50(jsonObject.getDouble("percent50"));
		}
		if (jsonObject.containsKey("percent85")) {
			model.setPercent85(jsonObject.getDouble("percent85"));
		}
		if (jsonObject.containsKey("percent97")) {
			model.setPercent97(jsonObject.getDouble("percent97"));
		}
		if (jsonObject.containsKey("oneDSDeviation")) {
			model.setOneDSDeviation(jsonObject.getDouble("oneDSDeviation"));
		}
		if (jsonObject.containsKey("twoDSDeviation")) {
			model.setTwoDSDeviation(jsonObject.getDouble("twoDSDeviation"));
		}
		if (jsonObject.containsKey("threeDSDeviation")) {
			model.setThreeDSDeviation(jsonObject.getDouble("threeDSDeviation"));
		}
		if (jsonObject.containsKey("fourDSDeviation")) {
			model.setFourDSDeviation(jsonObject.getDouble("fourDSDeviation"));
		}
		if (jsonObject.containsKey("median")) {
			model.setMedian(jsonObject.getDouble("median"));
		}
		if (jsonObject.containsKey("negativeOneDSDeviation")) {
			model.setNegativeOneDSDeviation(jsonObject.getDouble("negativeOneDSDeviation"));
		}
		if (jsonObject.containsKey("negativeTwoDSDeviation")) {
			model.setNegativeTwoDSDeviation(jsonObject.getDouble("negativeTwoDSDeviation"));
		}
		if (jsonObject.containsKey("negativeThreeDSDeviation")) {
			model.setNegativeThreeDSDeviation(jsonObject.getDouble("negativeThreeDSDeviation"));
		}
		if (jsonObject.containsKey("negativeFourDSDeviation")) {
			model.setNegativeFourDSDeviation(jsonObject.getDouble("negativeFourDSDeviation"));
		}
		if (jsonObject.containsKey("standardType")) {
			model.setStandardType(jsonObject.getString("standardType"));
		}
		if (jsonObject.containsKey("type")) {
			model.setType(jsonObject.getString("type"));
		}
		if (jsonObject.containsKey("createBy")) {
			model.setCreateBy(jsonObject.getString("createBy"));
		}
		if (jsonObject.containsKey("createTime")) {
			model.setCreateTime(jsonObject.getDate("createTime"));
		}

		return model;
	}

	public static JSONArray listToArray(java.util.List<GrowthStandard> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (GrowthStandard model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static JSONObject toJsonObject(GrowthStandard model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		jsonObject.put("age", model.getAge());
		jsonObject.put("ageOfTheMoon", model.getAgeOfTheMoon());
		if (model.getComplexKey() != null) {
			jsonObject.put("complexKey", model.getComplexKey());
		}
		jsonObject.put("month", model.getMonth());
		if (model.getSex() != null) {
			jsonObject.put("sex", model.getSex());
		}
		jsonObject.put("height", model.getHeight());
		jsonObject.put("weight", model.getWeight());
		jsonObject.put("percent3", model.getPercent3());
		jsonObject.put("percent15", model.getPercent15());
		jsonObject.put("percent50", model.getPercent50());
		jsonObject.put("percent85", model.getPercent85());
		jsonObject.put("percent97", model.getPercent97());
		jsonObject.put("oneDSDeviation", model.getOneDSDeviation());
		jsonObject.put("twoDSDeviation", model.getTwoDSDeviation());
		jsonObject.put("threeDSDeviation", model.getThreeDSDeviation());
		jsonObject.put("fourDSDeviation", model.getFourDSDeviation());
		jsonObject.put("median", model.getMedian());
		jsonObject.put("negativeOneDSDeviation", model.getNegativeOneDSDeviation());
		jsonObject.put("negativeTwoDSDeviation", model.getNegativeTwoDSDeviation());
		jsonObject.put("negativeThreeDSDeviation", model.getNegativeThreeDSDeviation());
		jsonObject.put("negativeFourDSDeviation", model.getNegativeFourDSDeviation());
		if (model.getStandardType() != null) {
			jsonObject.put("standardType", model.getStandardType());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
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

	public static ObjectNode toObjectNode(GrowthStandard model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		jsonObject.put("age", model.getAge());
		jsonObject.put("ageOfTheMoon", model.getAgeOfTheMoon());
		if (model.getComplexKey() != null) {
			jsonObject.put("complexKey", model.getComplexKey());
		}
		jsonObject.put("month", model.getMonth());
		if (model.getSex() != null) {
			jsonObject.put("sex", model.getSex());
		}
		jsonObject.put("height", model.getHeight());
		jsonObject.put("weight", model.getWeight());
		jsonObject.put("percent3", model.getPercent3());
		jsonObject.put("percent15", model.getPercent15());
		jsonObject.put("percent50", model.getPercent50());
		jsonObject.put("percent85", model.getPercent85());
		jsonObject.put("percent97", model.getPercent97());
		jsonObject.put("oneDSDeviation", model.getOneDSDeviation());
		jsonObject.put("twoDSDeviation", model.getTwoDSDeviation());
		jsonObject.put("threeDSDeviation", model.getThreeDSDeviation());
		jsonObject.put("fourDSDeviation", model.getFourDSDeviation());
		jsonObject.put("median", model.getMedian());
		jsonObject.put("negativeOneDSDeviation", model.getNegativeOneDSDeviation());
		jsonObject.put("negativeTwoDSDeviation", model.getNegativeTwoDSDeviation());
		jsonObject.put("negativeThreeDSDeviation", model.getNegativeThreeDSDeviation());
		jsonObject.put("negativeFourDSDeviation", model.getNegativeFourDSDeviation());
		if (model.getStandardType() != null) {
			jsonObject.put("standardType", model.getStandardType());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
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

	private GrowthStandardJsonFactory() {

	}

}
