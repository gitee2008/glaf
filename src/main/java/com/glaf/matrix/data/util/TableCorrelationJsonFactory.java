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

package com.glaf.matrix.data.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.core.util.DateUtils;
import com.glaf.matrix.data.domain.TableCorrelation;

/**
 * 
 * JSON工厂类
 *
 */
public class TableCorrelationJsonFactory {

	public static TableCorrelation jsonToObject(JSONObject jsonObject) {
		TableCorrelation model = new TableCorrelation();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getString("id"));
		}
		if (jsonObject.containsKey("masterTableId")) {
			model.setMasterTableId(jsonObject.getString("masterTableId"));
		}
		if (jsonObject.containsKey("masterTableName")) {
			model.setMasterTableName(jsonObject.getString("masterTableName"));
		}
		if (jsonObject.containsKey("slaveTableId")) {
			model.setSlaveTableId(jsonObject.getString("slaveTableId"));
		}
		if (jsonObject.containsKey("slaveTableName")) {
			model.setSlaveTableName(jsonObject.getString("slaveTableName"));
		}
		if (jsonObject.containsKey("insertCascade")) {
			model.setInsertCascade(jsonObject.getString("insertCascade"));
		}
		if (jsonObject.containsKey("deleteCascade")) {
			model.setDeleteCascade(jsonObject.getString("deleteCascade"));
		}
		if (jsonObject.containsKey("updateCascade")) {
			model.setUpdateCascade(jsonObject.getString("updateCascade"));
		}
		if (jsonObject.containsKey("relationshipType")) {
			model.setRelationshipType(jsonObject.getString("relationshipType"));
		}
		if (jsonObject.containsKey("sortNo")) {
			model.setSortNo(jsonObject.getInteger("sortNo"));
		}
		if (jsonObject.containsKey("createBy")) {
			model.setCreateBy(jsonObject.getString("createBy"));
		}
		if (jsonObject.containsKey("createTime")) {
			model.setCreateTime(jsonObject.getDate("createTime"));
		}
		if (jsonObject.containsKey("updateBy")) {
			model.setUpdateBy(jsonObject.getString("updateBy"));
		}
		if (jsonObject.containsKey("updateTime")) {
			model.setUpdateTime(jsonObject.getDate("updateTime"));
		}

		return model;
	}

	public static JSONObject toJsonObject(TableCorrelation model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getMasterTableId() != null) {
			jsonObject.put("masterTableId", model.getMasterTableId());
		}
		if (model.getMasterTableName() != null) {
			jsonObject.put("masterTableName", model.getMasterTableName());
		}
		if (model.getSlaveTableId() != null) {
			jsonObject.put("slaveTableId", model.getSlaveTableId());
		}
		if (model.getSlaveTableName() != null) {
			jsonObject.put("slaveTableName", model.getSlaveTableName());
		}
		if (model.getInsertCascade() != null) {
			jsonObject.put("insertCascade", model.getInsertCascade());
		}
		if (model.getDeleteCascade() != null) {
			jsonObject.put("deleteCascade", model.getDeleteCascade());
		}
		if (model.getUpdateCascade() != null) {
			jsonObject.put("updateCascade", model.getUpdateCascade());
		}
		if (model.getRelationshipType() != null) {
			jsonObject.put("relationshipType", model.getRelationshipType());
		}
		jsonObject.put("sortNo", model.getSortNo());
		if (model.getCreateBy() != null) {
			jsonObject.put("createBy", model.getCreateBy());
		}
		if (model.getCreateTime() != null) {
			jsonObject.put("createTime", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_date", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_datetime", DateUtils.getDateTime(model.getCreateTime()));
		}
		if (model.getUpdateBy() != null) {
			jsonObject.put("updateBy", model.getUpdateBy());
		}
		if (model.getUpdateTime() != null) {
			jsonObject.put("updateTime", DateUtils.getDate(model.getUpdateTime()));
			jsonObject.put("updateTime_date", DateUtils.getDate(model.getUpdateTime()));
			jsonObject.put("updateTime_datetime", DateUtils.getDateTime(model.getUpdateTime()));
		}
		return jsonObject;
	}

	public static ObjectNode toObjectNode(TableCorrelation model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getMasterTableId() != null) {
			jsonObject.put("masterTableId", model.getMasterTableId());
		}
		if (model.getMasterTableName() != null) {
			jsonObject.put("masterTableName", model.getMasterTableName());
		}
		if (model.getSlaveTableId() != null) {
			jsonObject.put("slaveTableId", model.getSlaveTableId());
		}
		if (model.getSlaveTableName() != null) {
			jsonObject.put("slaveTableName", model.getSlaveTableName());
		}
		if (model.getInsertCascade() != null) {
			jsonObject.put("insertCascade", model.getInsertCascade());
		}
		if (model.getDeleteCascade() != null) {
			jsonObject.put("deleteCascade", model.getDeleteCascade());
		}
		if (model.getUpdateCascade() != null) {
			jsonObject.put("updateCascade", model.getUpdateCascade());
		}
		if (model.getRelationshipType() != null) {
			jsonObject.put("relationshipType", model.getRelationshipType());
		}
		jsonObject.put("sortNo", model.getSortNo());
		if (model.getCreateBy() != null) {
			jsonObject.put("createBy", model.getCreateBy());
		}
		if (model.getCreateTime() != null) {
			jsonObject.put("createTime", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_date", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_datetime", DateUtils.getDateTime(model.getCreateTime()));
		}
		if (model.getUpdateBy() != null) {
			jsonObject.put("updateBy", model.getUpdateBy());
		}
		if (model.getUpdateTime() != null) {
			jsonObject.put("updateTime", DateUtils.getDate(model.getUpdateTime()));
			jsonObject.put("updateTime_date", DateUtils.getDate(model.getUpdateTime()));
			jsonObject.put("updateTime_datetime", DateUtils.getDateTime(model.getUpdateTime()));
		}
		return jsonObject;
	}

	public static JSONArray listToArray(java.util.List<TableCorrelation> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (TableCorrelation model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static java.util.List<TableCorrelation> arrayToList(JSONArray array) {
		java.util.List<TableCorrelation> list = new java.util.ArrayList<TableCorrelation>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			TableCorrelation model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	private TableCorrelationJsonFactory() {

	}

}
