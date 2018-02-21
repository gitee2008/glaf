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

import com.glaf.core.identity.Tenant;
import com.glaf.core.identity.impl.TenantImpl;

/**
 * 
 * JSON工厂类
 *
 */
public class TenantJsonFactory {

	public static java.util.List<Tenant> arrayToList(JSONArray array) {
		java.util.List<Tenant> list = new java.util.ArrayList<Tenant>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			Tenant model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	public static Tenant jsonToObject(JSONObject jsonObject) {
		Tenant model = new TenantImpl();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getLong("id"));
		}

		if (jsonObject.containsKey("name")) {
			model.setName(jsonObject.getString("name"));
		}

		if (jsonObject.containsKey("tenantId")) {
			model.setTenantId(jsonObject.getString("tenantId"));
		}

		if (jsonObject.containsKey("tenantType")) {
			model.setTenantType(jsonObject.getInteger("tenantType"));
		}

		if (jsonObject.containsKey("databaseId")) {
			model.setDatabaseId(jsonObject.getLong("databaseId"));
		}

		if (jsonObject.containsKey("limit")) {
			model.setLimit(jsonObject.getInteger("limit"));
		}

		if (jsonObject.containsKey("locked")) {
			model.setLocked(jsonObject.getInteger("locked"));
		}

		return model;
	}

	public static JSONArray listToArray(java.util.List<Tenant> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (Tenant model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static JSONObject toJsonObject(Tenant model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());

		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}

		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}

		jsonObject.put("tenantType", model.getTenantType());

		jsonObject.put("databaseId", model.getDatabaseId());

		jsonObject.put("limit", model.getLimit());

		jsonObject.put("locked", model.getLocked());

		return jsonObject;
	}

	public static ObjectNode toObjectNode(Tenant model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());

		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}

		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}

		jsonObject.put("tenantType", model.getTenantType());

		jsonObject.put("databaseId", model.getDatabaseId());

		jsonObject.put("limit", model.getLimit());

		jsonObject.put("locked", model.getLocked());

		return jsonObject;
	}

	private TenantJsonFactory() {

	}

}
