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

package com.glaf.core.identity.util;

import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.core.identity.Group;
import com.glaf.core.identity.impl.GroupImpl;

public class GroupJsonFactory {

	public static java.util.List<Group> arrayToList(JSONArray array) {
		java.util.List<Group> list = new java.util.ArrayList<Group>();
		for (int i = 0; i < array.size(); i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			Group model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	public static Group jsonToObject(JSONObject jsonObject) {
		Group model = new GroupImpl();
		if (jsonObject.containsKey("groupId")) {
			model.setGroupId(jsonObject.getString("groupId"));
		}
		if (jsonObject.containsKey("name")) {
			model.setName(jsonObject.getString("name"));
		}
		if (jsonObject.containsKey("code")) {
			model.setCode(jsonObject.getString("code"));
		}

		if (jsonObject.containsKey("type")) {
			model.setType(jsonObject.getString("type"));
		}

		if (jsonObject.containsKey("sort")) {
			model.setSort(jsonObject.getInteger("sort"));
		}

		return model;
	}

	public static JSONArray listToArray(java.util.List<Group> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (Group model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static JSONObject toJsonObject(Group model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("groupId", model.getGroupId());
		jsonObject.put("_groupId_", model.getGroupId());
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}

		if (model.getCode() != null) {
			jsonObject.put("code", model.getCode());
		}

		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}

		jsonObject.put("sort", model.getSort());
		return jsonObject;
	}

	public static ObjectNode toObjectNode(Group model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("groupId", model.getGroupId());
		jsonObject.put("_groupId_", model.getGroupId());
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}

		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}

		jsonObject.put("sort", model.getSort());
		return jsonObject;
	}

	private GroupJsonFactory() {

	}

}
