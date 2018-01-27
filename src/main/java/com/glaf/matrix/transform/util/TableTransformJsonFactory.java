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

package com.glaf.matrix.transform.util;

import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.glaf.core.util.DateUtils;
import com.glaf.matrix.transform.domain.*;

/**
 * 
 * JSON工厂类
 *
 */
public class TableTransformJsonFactory {

	public static java.util.List<TableTransform> arrayToList(JSONArray array) {
		java.util.List<TableTransform> list = new java.util.ArrayList<TableTransform>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			TableTransform model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	public static TableTransform jsonToObject(JSONObject jsonObject) {
		TableTransform model = new TableTransform();
		if (jsonObject.containsKey("transformId")) {
			model.setTransformId(jsonObject.getString("transformId"));
		}
		if (jsonObject.containsKey("tableName")) {
			model.setTableName(jsonObject.getString("tableName"));
		}
		if (jsonObject.containsKey("title")) {
			model.setTitle(jsonObject.getString("title"));
		}
		if (jsonObject.containsKey("databaseIds")) {
			model.setDatabaseIds(jsonObject.getString("databaseIds"));
		}
		if (jsonObject.containsKey("primaryKey")) {
			model.setPrimaryKey(jsonObject.getString("primaryKey"));
		}
		if (jsonObject.containsKey("targetTableName")) {
			model.setTargetTableName(jsonObject.getString("targetTableName"));
		}
		if (jsonObject.containsKey("transformColumns")) {
			model.setTransformColumns(jsonObject.getString("transformColumns"));
		}
		if (jsonObject.containsKey("sqlCriteria")) {
			model.setSqlCriteria(jsonObject.getString("sqlCriteria"));
		}
		if (jsonObject.containsKey("sort")) {
			model.setSort(jsonObject.getInteger("sort"));
		}
		if (jsonObject.containsKey("transformIfTargetColumnNotEmpty")) {
			model.setTransformIfTargetColumnNotEmpty(jsonObject.getString("transformIfTargetColumnNotEmpty"));
		}
		if (jsonObject.containsKey("transformFlag")) {
			model.setTransformFlag(jsonObject.getString("transformFlag"));
		}
		if (jsonObject.containsKey("transformStatus")) {
			model.setTransformStatus(jsonObject.getInteger("transformStatus"));
		}
		if (jsonObject.containsKey("transformTime")) {
			model.setTransformTime(jsonObject.getDate("transformTime"));
		}
		if (jsonObject.containsKey("currentUserFlag")) {
			model.setCurrentUserFlag(jsonObject.getString("currentUserFlag"));
		}
		if (jsonObject.containsKey("deleteFlag")) {
			model.setDeleteFlag(jsonObject.getInteger("deleteFlag"));
		}
		if (jsonObject.containsKey("locked")) {
			model.setLocked(jsonObject.getInteger("locked"));
		}

		return model;
	}

	public static JSONArray listToArray(java.util.List<TableTransform> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (TableTransform model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static JSONObject toJsonObject(TableTransform model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("transformId", model.getTransformId());
		jsonObject.put("tableName", model.getTableName());
		jsonObject.put("_tableName_", model.getTableName());
		if (model.getTitle() != null) {
			jsonObject.put("title", model.getTitle());
		}
		if (model.getDatabaseIds() != null) {
			jsonObject.put("databaseIds", model.getDatabaseIds());
		}
		if (model.getPrimaryKey() != null) {
			jsonObject.put("primaryKey", model.getPrimaryKey());
		}
		if (model.getTargetTableName() != null) {
			jsonObject.put("targetTableName", model.getTargetTableName());
		}
		if (model.getTransformColumns() != null) {
			jsonObject.put("transformColumns", model.getTransformColumns());
		}
		if (model.getSqlCriteria() != null) {
			jsonObject.put("sqlCriteria", model.getSqlCriteria());
		}
		jsonObject.put("sort", model.getSort());

		if (model.getTransformIfTargetColumnNotEmpty() != null) {
			jsonObject.put("transformIfTargetColumnNotEmpty", model.getTransformIfTargetColumnNotEmpty());
		}

		if (model.getTransformFlag() != null) {
			jsonObject.put("transformFlag", model.getTransformFlag());
		}

		jsonObject.put("transformStatus", model.getTransformStatus());
		if (model.getTransformTime() != null) {
			jsonObject.put("transformTime", DateUtils.getDate(model.getTransformTime()));
			jsonObject.put("transformTime_date", DateUtils.getDate(model.getTransformTime()));
			jsonObject.put("transformTime_datetime", DateUtils.getDateTime(model.getTransformTime()));
		}

		if (model.getCurrentUserFlag() != null) {
			jsonObject.put("currentUserFlag", model.getCurrentUserFlag());
		}
		jsonObject.put("deleteFlag", model.getDeleteFlag());
		jsonObject.put("locked", model.getLocked());
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

	public static ObjectNode toObjectNode(TableTransform model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("transformId", model.getTransformId());
		jsonObject.put("tableName", model.getTableName());
		jsonObject.put("_tableName_", model.getTableName());
		if (model.getTitle() != null) {
			jsonObject.put("title", model.getTitle());
		}
		if (model.getDatabaseIds() != null) {
			jsonObject.put("databaseIds", model.getDatabaseIds());
		}
		if (model.getPrimaryKey() != null) {
			jsonObject.put("primaryKey", model.getPrimaryKey());
		}
		if (model.getTargetTableName() != null) {
			jsonObject.put("targetTableName", model.getTargetTableName());
		}
		if (model.getTransformColumns() != null) {
			jsonObject.put("transformColumns", model.getTransformColumns());
		}
		if (model.getSqlCriteria() != null) {
			jsonObject.put("sqlCriteria", model.getSqlCriteria());
		}
		jsonObject.put("sort", model.getSort());

		if (model.getTransformIfTargetColumnNotEmpty() != null) {
			jsonObject.put("transformIfTargetColumnNotEmpty", model.getTransformIfTargetColumnNotEmpty());
		}

		if (model.getTransformFlag() != null) {
			jsonObject.put("transformFlag", model.getTransformFlag());
		}

		jsonObject.put("transformStatus", model.getTransformStatus());
		if (model.getTransformTime() != null) {
			jsonObject.put("transformTime", DateUtils.getDate(model.getTransformTime()));
			jsonObject.put("transformTime_date", DateUtils.getDate(model.getTransformTime()));
			jsonObject.put("transformTime_datetime", DateUtils.getDateTime(model.getTransformTime()));
		}

		if (model.getCurrentUserFlag() != null) {
			jsonObject.put("currentUserFlag", model.getCurrentUserFlag());
		}
		jsonObject.put("deleteFlag", model.getDeleteFlag());
		jsonObject.put("locked", model.getLocked());
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

	private TableTransformJsonFactory() {

	}

}
