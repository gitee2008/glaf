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

package com.glaf.base.district.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.base.district.domain.District;

public class DistrictJsonFactory {

	public static java.util.List<District> arrayToList(JSONArray array) {
		java.util.List<District> list = new java.util.ArrayList<District>();
		for (int i = 0; i < array.size(); i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			District model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	public static District jsonToObject(JSONObject jsonObject) {
		District model = new District();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getLong("id"));
		}
		if (jsonObject.containsKey("parentId")) {
			model.setParentId(jsonObject.getLong("parentId"));
		}
		if (jsonObject.containsKey("name")) {
			model.setName(jsonObject.getString("name"));
		}
		if (jsonObject.containsKey("code")) {
			model.setCode(jsonObject.getString("code"));
		}
		if (jsonObject.containsKey("treeId")) {
			model.setTreeId(jsonObject.getString("treeId"));
		}
		if (jsonObject.containsKey("level")) {
			model.setLevel(jsonObject.getInteger("level"));
		}
		if (jsonObject.containsKey("useType")) {
			model.setUseType(jsonObject.getString("useType"));
		}
		if (jsonObject.containsKey("sortNo")) {
			model.setSortNo(jsonObject.getInteger("sortNo"));
		}
		if (jsonObject.containsKey("locked")) {
			model.setLocked(jsonObject.getInteger("locked"));
		}
		return model;
	}

	public static JSONArray listToArray(java.util.List<District> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (District model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static JSONObject toJsonObject(District bean) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", bean.getId());
		jsonObject.put("parentId", bean.getParentId());

		if (bean.getName() != null) {
			jsonObject.put("name", bean.getName());
		}
		if (bean.getCode() != null) {
			jsonObject.put("code", bean.getCode());
		}
		if (bean.getTreeId() != null) {
			jsonObject.put("treeId", bean.getTreeId());
		}
		jsonObject.put("level", bean.getLevel());
		if (bean.getUseType() != null) {
			jsonObject.put("useType", bean.getUseType());
		}

		jsonObject.put("sortNo", bean.getSortNo());
		jsonObject.put("locked", bean.getLocked());
		return jsonObject;
	}

	public static ObjectNode toObjectNode(District bean) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", bean.getId());
		jsonObject.put("parentId", bean.getParentId());

		if (bean.getName() != null) {
			jsonObject.put("name", bean.getName());
		}
		if (bean.getCode() != null) {
			jsonObject.put("code", bean.getCode());
		}
		if (bean.getTreeId() != null) {
			jsonObject.put("treeId", bean.getTreeId());
		}
		jsonObject.put("level", bean.getLevel());
		if (bean.getUseType() != null) {
			jsonObject.put("useType", bean.getUseType());
		}
		jsonObject.put("sortNo", bean.getSortNo());
		jsonObject.put("locked", bean.getLocked());

		return jsonObject;
	}

}
