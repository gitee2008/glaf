package com.glaf.heathcare.util;

import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.glaf.heathcare.domain.*;

/**
 * 
 * JSON工厂类
 *
 */
public class GrowthRateCountJsonFactory {

	public static GrowthRateCount jsonToObject(JSONObject jsonObject) {
		GrowthRateCount model = new GrowthRateCount();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getLong("id"));
		}
		if (jsonObject.containsKey("checkId")) {
			model.setCheckId(jsonObject.getString("checkId"));
		}
		if (jsonObject.containsKey("tenantId")) {
			model.setTenantId(jsonObject.getString("tenantId"));
		}
		if (jsonObject.containsKey("gradeId")) {
			model.setGradeId(jsonObject.getString("gradeId"));
		}
		if (jsonObject.containsKey("type")) {
			model.setType(jsonObject.getString("type"));
		}
		if (jsonObject.containsKey("increase")) {
			model.setIncrease(jsonObject.getInteger("increase"));
		}
		if (jsonObject.containsKey("increasePercent")) {
			model.setIncreasePercent(jsonObject.getDouble("increasePercent"));
		}
		if (jsonObject.containsKey("standard")) {
			model.setStandard(jsonObject.getInteger("standard"));
		}
		if (jsonObject.containsKey("standardPercent")) {
			model.setStandardPercent(jsonObject.getDouble("standardPercent"));
		}

		return model;
	}

	public static JSONObject toJsonObject(GrowthRateCount model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getCheckId() != null) {
			jsonObject.put("checkId", model.getCheckId());
		}
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		if (model.getGradeId() != null) {
			jsonObject.put("gradeId", model.getGradeId());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		jsonObject.put("increase", model.getIncrease());
		jsonObject.put("increasePercent", model.getIncreasePercent());
		jsonObject.put("standard", model.getStandard());
		jsonObject.put("standardPercent", model.getStandardPercent());
		return jsonObject;
	}

	public static ObjectNode toObjectNode(GrowthRateCount model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getCheckId() != null) {
			jsonObject.put("checkId", model.getCheckId());
		}
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		if (model.getGradeId() != null) {
			jsonObject.put("gradeId", model.getGradeId());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		jsonObject.put("increase", model.getIncrease());
		jsonObject.put("increasePercent", model.getIncreasePercent());
		jsonObject.put("standard", model.getStandard());
		jsonObject.put("standardPercent", model.getStandardPercent());
		return jsonObject;
	}

	public static JSONArray listToArray(java.util.List<GrowthRateCount> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (GrowthRateCount model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static java.util.List<GrowthRateCount> arrayToList(JSONArray array) {
		java.util.List<GrowthRateCount> list = new java.util.ArrayList<GrowthRateCount>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			GrowthRateCount model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	private GrowthRateCountJsonFactory() {

	}

}
