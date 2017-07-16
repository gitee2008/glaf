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

package com.glaf.core.access.util;

import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.glaf.core.access.domain.*;

/**
 * 
 * JSON工厂类
 *
 */
public class AccessUriJsonFactory {

	public static AccessUri jsonToObject(JSONObject jsonObject) {
		AccessUri model = new AccessUri();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getLong("id"));
		}
		if (jsonObject.containsKey("uri")) {
			model.setUri(jsonObject.getString("uri"));
		}
		if (jsonObject.containsKey("limit")) {
			model.setLimit(jsonObject.getInteger("limit"));
		}
		if (jsonObject.containsKey("total")) {
			model.setTotal(jsonObject.getInteger("total"));
		}
		if (jsonObject.containsKey("title")) {
			model.setTitle(jsonObject.getString("title"));
		}
		if (jsonObject.containsKey("description")) {
			model.setDescription(jsonObject.getString("description"));
		}

		return model;
	}

	public static JSONObject toJsonObject(AccessUri model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getUri() != null) {
			jsonObject.put("uri", model.getUri());
		}
		jsonObject.put("limit", model.getLimit());
		jsonObject.put("total", model.getTotal());
		if (model.getTitle() != null) {
			jsonObject.put("title", model.getTitle());
		}
		if (model.getDescription() != null) {
			jsonObject.put("description", model.getDescription());
		}
		return jsonObject;
	}

	public static ObjectNode toObjectNode(AccessUri model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getUri() != null) {
			jsonObject.put("uri", model.getUri());
		}
		jsonObject.put("limit", model.getLimit());
		jsonObject.put("total", model.getTotal());
		if (model.getTitle() != null) {
			jsonObject.put("title", model.getTitle());
		}
		if (model.getDescription() != null) {
			jsonObject.put("description", model.getDescription());
		}
		return jsonObject;
	}

	public static JSONArray listToArray(java.util.List<AccessUri> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (AccessUri model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static java.util.List<AccessUri> arrayToList(JSONArray array) {
		java.util.List<AccessUri> list = new java.util.ArrayList<AccessUri>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			AccessUri model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	private AccessUriJsonFactory() {

	}

}
