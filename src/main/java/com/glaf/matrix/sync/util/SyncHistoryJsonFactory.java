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

package com.glaf.matrix.sync.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.core.util.DateUtils;
import com.glaf.matrix.sync.domain.SyncHistory;

/**
 * 
 * JSON工厂类
 *
 */
public class SyncHistoryJsonFactory {

	public static SyncHistory jsonToObject(JSONObject jsonObject) {
		SyncHistory model = new SyncHistory();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getLong("id"));
		}
		if (jsonObject.containsKey("syncId")) {
			model.setSyncId(jsonObject.getLong("syncId"));
		}
		if (jsonObject.containsKey("deploymentId")) {
			model.setDeploymentId(jsonObject.getString("deploymentId"));
		}
		if (jsonObject.containsKey("databaseId")) {
			model.setDatabaseId(jsonObject.getLong("databaseId"));
		}
		if (jsonObject.containsKey("databaseName")) {
			model.setDatabaseName(jsonObject.getString("databaseName"));
		}
		if (jsonObject.containsKey("status")) {
			model.setStatus(jsonObject.getInteger("status"));
		}
		if (jsonObject.containsKey("totalTime")) {
			model.setTotalTime(jsonObject.getInteger("totalTime"));
		}
		if (jsonObject.containsKey("createBy")) {
			model.setCreateBy(jsonObject.getString("createBy"));
		}
		if (jsonObject.containsKey("createTime")) {
			model.setCreateTime(jsonObject.getDate("createTime"));
		}

		return model;
	}

	public static JSONObject toJsonObject(SyncHistory model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		jsonObject.put("syncId", model.getSyncId());
		if (model.getDeploymentId() != null) {
			jsonObject.put("deploymentId", model.getDeploymentId());
		}
		jsonObject.put("databaseId", model.getDatabaseId());
		if (model.getDatabaseName() != null) {
			jsonObject.put("databaseName", model.getDatabaseName());
		}
		jsonObject.put("status", model.getStatus());
		jsonObject.put("totalTime", model.getTotalTime());
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

	public static ObjectNode toObjectNode(SyncHistory model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		jsonObject.put("syncId", model.getSyncId());
		if (model.getDeploymentId() != null) {
			jsonObject.put("deploymentId", model.getDeploymentId());
		}
		jsonObject.put("databaseId", model.getDatabaseId());
		if (model.getDatabaseName() != null) {
			jsonObject.put("databaseName", model.getDatabaseName());
		}
		jsonObject.put("status", model.getStatus());
		jsonObject.put("totalTime", model.getTotalTime());
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

	public static JSONArray listToArray(java.util.List<SyncHistory> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (SyncHistory model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static java.util.List<SyncHistory> arrayToList(JSONArray array) {
		java.util.List<SyncHistory> list = new java.util.ArrayList<SyncHistory>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			SyncHistory model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	private SyncHistoryJsonFactory() {

	}

}
