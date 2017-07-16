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

import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.glaf.core.util.DateUtils;
import com.glaf.base.modules.sys.model.*;

/**
 * 
 * JSON工厂类
 *
 */
public class TenantGrantJsonFactory {

	public static java.util.List<TenantGrant> arrayToList(JSONArray array) {
		java.util.List<TenantGrant> list = new java.util.ArrayList<TenantGrant>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			TenantGrant model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	public static TenantGrant jsonToObject(JSONObject jsonObject) {
		TenantGrant model = new TenantGrant();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getString("id"));
		}
		if (jsonObject.containsKey("grantee")) {
			model.setGrantee(jsonObject.getString("grantee"));
		}
		if (jsonObject.containsKey("privilege")) {
			model.setPrivilege(jsonObject.getString("privilege"));
		}
		if (jsonObject.containsKey("tenantId")) {
			model.setTenantId(jsonObject.getString("tenantId"));
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

	public static JSONArray listToArray(java.util.List<TenantGrant> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (TenantGrant model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static JSONObject toJsonObject(TenantGrant model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getGrantee() != null) {
			jsonObject.put("grantee", model.getGrantee());
		}
		if (model.getPrivilege() != null) {
			jsonObject.put("privilege", model.getPrivilege());
		}
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
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

	public static ObjectNode toObjectNode(TenantGrant model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getGrantee() != null) {
			jsonObject.put("grantee", model.getGrantee());
		}
		if (model.getPrivilege() != null) {
			jsonObject.put("privilege", model.getPrivilege());
		}
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
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

	private TenantGrantJsonFactory() {

	}

}
