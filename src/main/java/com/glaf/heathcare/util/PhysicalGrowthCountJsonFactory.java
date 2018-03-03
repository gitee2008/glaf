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
public class PhysicalGrowthCountJsonFactory {

	public static java.util.List<PhysicalGrowthCount> arrayToList(JSONArray array) {
		java.util.List<PhysicalGrowthCount> list = new java.util.ArrayList<PhysicalGrowthCount>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			PhysicalGrowthCount model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	public static PhysicalGrowthCount jsonToObject(JSONObject jsonObject) {
		PhysicalGrowthCount model = new PhysicalGrowthCount();
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
		if (jsonObject.containsKey("level1")) {
			model.setLevel1(jsonObject.getInteger("level1"));
		}
		if (jsonObject.containsKey("level2")) {
			model.setLevel2(jsonObject.getInteger("level2"));
		}
		if (jsonObject.containsKey("level3")) {
			model.setLevel3(jsonObject.getInteger("level3"));
		}
		if (jsonObject.containsKey("level5")) {
			model.setLevel5(jsonObject.getInteger("level5"));
		}
		if (jsonObject.containsKey("level7")) {
			model.setLevel7(jsonObject.getInteger("level7"));
		}
		if (jsonObject.containsKey("level8")) {
			model.setLevel8(jsonObject.getInteger("level8"));
		}
		if (jsonObject.containsKey("level9")) {
			model.setLevel9(jsonObject.getInteger("level9"));
		}
		if (jsonObject.containsKey("level1Percent")) {
			model.setLevel1Percent(jsonObject.getDouble("level1Percent"));
		}
		if (jsonObject.containsKey("level2Percent")) {
			model.setLevel2Percent(jsonObject.getDouble("level2Percent"));
		}
		if (jsonObject.containsKey("level3Percent")) {
			model.setLevel3Percent(jsonObject.getDouble("level3Percent"));
		}
		if (jsonObject.containsKey("level5Percent")) {
			model.setLevel5Percent(jsonObject.getDouble("level5Percent"));
		}
		if (jsonObject.containsKey("level7Percent")) {
			model.setLevel7Percent(jsonObject.getDouble("level7Percent"));
		}
		if (jsonObject.containsKey("level8Percent")) {
			model.setLevel8Percent(jsonObject.getDouble("level8Percent"));
		}
		if (jsonObject.containsKey("level9Percent")) {
			model.setLevel9Percent(jsonObject.getDouble("level9Percent"));
		}
		if (jsonObject.containsKey("normal")) {
			model.setNormal(jsonObject.getInteger("normal"));
		}
		if (jsonObject.containsKey("normalPercent")) {
			model.setNormalPercent(jsonObject.getDouble("normalPercent"));
		}

		return model;
	}

	public static JSONArray listToArray(java.util.List<PhysicalGrowthCount> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (PhysicalGrowthCount model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static JSONObject toJsonObject(PhysicalGrowthCount model) {
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
		jsonObject.put("level1", model.getLevel1());
		jsonObject.put("level2", model.getLevel2());
		jsonObject.put("level3", model.getLevel3());
		jsonObject.put("level5", model.getLevel5());
		jsonObject.put("level7", model.getLevel7());
		jsonObject.put("level8", model.getLevel8());
		jsonObject.put("level9", model.getLevel9());
		jsonObject.put("level1Percent", model.getLevel1Percent());
		jsonObject.put("level2Percent", model.getLevel2Percent());
		jsonObject.put("level3Percent", model.getLevel3Percent());
		jsonObject.put("level5Percent", model.getLevel5Percent());
		jsonObject.put("level7Percent", model.getLevel7Percent());
		jsonObject.put("level8Percent", model.getLevel8Percent());
		jsonObject.put("level9Percent", model.getLevel9Percent());
		jsonObject.put("normal", model.getNormal());
		jsonObject.put("normalPercent", model.getNormalPercent());
		return jsonObject;
	}

	public static ObjectNode toObjectNode(PhysicalGrowthCount model) {
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
		jsonObject.put("level1", model.getLevel1());
		jsonObject.put("level2", model.getLevel2());
		jsonObject.put("level3", model.getLevel3());
		jsonObject.put("level5", model.getLevel5());
		jsonObject.put("level7", model.getLevel7());
		jsonObject.put("level8", model.getLevel8());
		jsonObject.put("level9", model.getLevel9());
		jsonObject.put("level1Percent", model.getLevel1Percent());
		jsonObject.put("level2Percent", model.getLevel2Percent());
		jsonObject.put("level3Percent", model.getLevel3Percent());
		jsonObject.put("level5Percent", model.getLevel5Percent());
		jsonObject.put("level7Percent", model.getLevel7Percent());
		jsonObject.put("level8Percent", model.getLevel8Percent());
		jsonObject.put("level9Percent", model.getLevel9Percent());
		jsonObject.put("normal", model.getNormal());
		jsonObject.put("normalPercent", model.getNormalPercent());
		return jsonObject;
	}

	private PhysicalGrowthCountJsonFactory() {

	}

}
