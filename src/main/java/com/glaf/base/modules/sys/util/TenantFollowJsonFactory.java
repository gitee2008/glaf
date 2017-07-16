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

package com.glaf.base.modules.sys.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.base.modules.sys.model.TenantFollow;
import com.glaf.core.util.DateUtils;

/**
 * 
 * JSON工厂类
 *
 */
public class TenantFollowJsonFactory {

	public static java.util.List<TenantFollow> arrayToList(JSONArray array) {
		java.util.List<TenantFollow> list = new java.util.ArrayList<TenantFollow>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			TenantFollow model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	public static TenantFollow jsonToObject(JSONObject jsonObject) {
		TenantFollow model = new TenantFollow();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getLong("id"));
		}
		if (jsonObject.containsKey("tenantId")) {
			model.setTenantId(jsonObject.getString("tenantId"));
		}
		if (jsonObject.containsKey("tenantName")) {
			model.setTenantName(jsonObject.getString("tenantName"));
		}
		if (jsonObject.containsKey("followTenantId")) {
			model.setFollowTenantId(jsonObject.getString("followTenantId"));
		}
		if (jsonObject.containsKey("followTenantName")) {
			model.setFollowTenantName(jsonObject.getString("followTenantName"));
		}
		if (jsonObject.containsKey("province")) {
			model.setProvince(jsonObject.getString("province"));
		}
		if (jsonObject.containsKey("city")) {
			model.setCity(jsonObject.getString("city"));
		}
		if (jsonObject.containsKey("createBy")) {
			model.setCreateBy(jsonObject.getString("createBy"));
		}
		if (jsonObject.containsKey("createTime")) {
			model.setCreateTime(jsonObject.getDate("createTime"));
		}

		return model;
	}

	public static JSONArray listToArray(java.util.List<TenantFollow> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (TenantFollow model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static JSONObject toJsonObject(TenantFollow model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		if (model.getTenantName() != null) {
			jsonObject.put("tenantName", model.getTenantName());
		}
		if (model.getFollowTenantId() != null) {
			jsonObject.put("followTenantId", model.getFollowTenantId());
		}
		if (model.getFollowTenantName() != null) {
			jsonObject.put("followTenantName", model.getFollowTenantName());
		}
		if (model.getProvince() != null) {
			jsonObject.put("province", model.getProvince());
		}
		if (model.getCity() != null) {
			jsonObject.put("city", model.getCity());
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

	public static ObjectNode toObjectNode(TenantFollow model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		if (model.getTenantName() != null) {
			jsonObject.put("tenantName", model.getTenantName());
		}
		if (model.getFollowTenantId() != null) {
			jsonObject.put("followTenantId", model.getFollowTenantId());
		}
		if (model.getFollowTenantName() != null) {
			jsonObject.put("followTenantName", model.getFollowTenantName());
		}
		if (model.getProvince() != null) {
			jsonObject.put("province", model.getProvince());
		}
		if (model.getCity() != null) {
			jsonObject.put("city", model.getCity());
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

	private TenantFollowJsonFactory() {

	}

}
