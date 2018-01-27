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

import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.core.util.DateUtils;
import com.glaf.matrix.data.domain.*;

/**
 * 
 * JSON工厂类
 *
 */
public class SqlDefinitionJsonFactory {

	public static java.util.List<SqlDefinition> arrayToList(JSONArray array) {
		java.util.List<SqlDefinition> list = new java.util.ArrayList<SqlDefinition>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			SqlDefinition model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	public static SqlDefinition jsonToObject(JSONObject jsonObject) {
		SqlDefinition model = new SqlDefinition();
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
		if (jsonObject.containsKey("title")) {
			model.setTitle(jsonObject.getString("title"));
		}
		if (jsonObject.containsKey("sql")) {
			model.setSql(jsonObject.getString("sql"));
		}
		if (jsonObject.containsKey("countSql")) {
			model.setCountSql(jsonObject.getString("countSql"));
		}
		if (jsonObject.containsKey("type")) {
			model.setType(jsonObject.getString("type"));
		}
		if (jsonObject.containsKey("operation")) {
			model.setOperation(jsonObject.getString("operation"));
		}
		if (jsonObject.containsKey("rowKey")) {
			model.setRowKey(jsonObject.getString("rowKey"));
		}

		if (jsonObject.containsKey("cacheFlag")) {
			model.setCacheFlag(jsonObject.getString("cacheFlag"));
		}
		if (jsonObject.containsKey("dataItemFlag")) {
			model.setDataItemFlag(jsonObject.getString("dataItemFlag"));
		}
		if (jsonObject.containsKey("fetchFlag")) {
			model.setFetchFlag(jsonObject.getString("fetchFlag"));
		}
		if (jsonObject.containsKey("deleteFetch")) {
			model.setDeleteFetch(jsonObject.getString("deleteFetch"));
		}

		if (jsonObject.containsKey("exportFlag")) {
			model.setExportFlag(jsonObject.getString("exportFlag"));
		}
		if (jsonObject.containsKey("exportTableName")) {
			model.setExportTableName(jsonObject.getString("exportTableName"));
		}

		if (jsonObject.containsKey("exportTemplate")) {
			model.setExportTemplate(jsonObject.getString("exportTemplate"));
		}

		if (jsonObject.containsKey("targetTableName")) {
			model.setTargetTableName(jsonObject.getString("targetTableName"));
		}
		if (jsonObject.containsKey("shareFlag")) {
			model.setShareFlag(jsonObject.getString("shareFlag"));
		}
		if (jsonObject.containsKey("scheduleFlag")) {
			model.setScheduleFlag(jsonObject.getString("scheduleFlag"));
		}
		if (jsonObject.containsKey("publicFlag")) {
			model.setPublicFlag(jsonObject.getString("publicFlag"));
		}
		if (jsonObject.containsKey("saveFlag")) {
			model.setSaveFlag(jsonObject.getString("saveFlag"));
		}
		if (jsonObject.containsKey("aggregationFlag")) {
			model.setAggregationFlag(jsonObject.getString("aggregationFlag"));
		}
		if (jsonObject.containsKey("locked")) {
			model.setLocked(jsonObject.getInteger("locked"));
		}
		if (jsonObject.containsKey("ordinal")) {
			model.setOrdinal(jsonObject.getInteger("ordinal"));
		}

		return model;
	}

	public static JSONArray listToArray(java.util.List<SqlDefinition> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (SqlDefinition model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static JSONObject toJsonObject(SqlDefinition model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		jsonObject.put("parentId", model.getParentId());
		jsonObject.put("locked", model.getLocked());
		jsonObject.put("ordinal", model.getOrdinal());

		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getCode() != null) {
			jsonObject.put("code", model.getCode());
		}
		if (model.getUuid() != null) {
			jsonObject.put("uuid", model.getUuid());
		}
		if (model.getTitle() != null) {
			jsonObject.put("title", model.getTitle());
		}
		if (model.getSql() != null) {
			jsonObject.put("sql", model.getSql());
		}
		if (model.getCountSql() != null) {
			jsonObject.put("countSql", model.getCountSql());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		if (model.getOperation() != null) {
			jsonObject.put("operation", model.getOperation());
		}
		if (model.getRowKey() != null) {
			jsonObject.put("rowKey", model.getRowKey());
		}

		if (model.getScheduleFlag() != null) {
			jsonObject.put("scheduleFlag", model.getScheduleFlag());
		}

		if (model.getCacheFlag() != null) {
			jsonObject.put("cacheFlag", model.getCacheFlag());
		}
		if (model.getDataItemFlag() != null) {
			jsonObject.put("dataItemFlag", model.getDataItemFlag());
		} 
		if (model.getFetchFlag() != null) {
			jsonObject.put("fetchFlag", model.getFetchFlag());
		}

		if (model.getExportFlag() != null) {
			jsonObject.put("exportFlag", model.getExportFlag());
		}
		if (model.getExportTableName() != null) {
			jsonObject.put("exportTableName", model.getExportTableName());
		}
		if (model.getExportTemplate() != null) {
			jsonObject.put("exportTemplate", model.getExportTemplate());
		}
		if (model.getDeleteFetch() != null) {
			jsonObject.put("deleteFetch", model.getDeleteFetch());
		}
		if (model.getTargetTableName() != null) {
			jsonObject.put("targetTableName", model.getTargetTableName());
		}

		if (model.getPublicFlag() != null) {
			jsonObject.put("publicFlag", model.getPublicFlag());
		}
		if (model.getShareFlag() != null) {
			jsonObject.put("shareFlag", model.getShareFlag());
		}
		if (model.getSaveFlag() != null) {
			jsonObject.put("saveFlag", model.getSaveFlag());
		}

		if (model.getAggregationFlag() != null) {
			jsonObject.put("aggregationFlag", model.getAggregationFlag());
		}

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

	public static ObjectNode toObjectNode(SqlDefinition model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		jsonObject.put("parentId", model.getParentId());
		jsonObject.put("locked", model.getLocked());
		jsonObject.put("ordinal", model.getOrdinal());

		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getCode() != null) {
			jsonObject.put("code", model.getCode());
		}
		if (model.getUuid() != null) {
			jsonObject.put("uuid", model.getUuid());
		}
		if (model.getTitle() != null) {
			jsonObject.put("title", model.getTitle());
		}
		if (model.getSql() != null) {
			jsonObject.put("sql", model.getSql());
		}
		if (model.getCountSql() != null) {
			jsonObject.put("countSql", model.getCountSql());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		if (model.getOperation() != null) {
			jsonObject.put("operation", model.getOperation());
		}
		if (model.getRowKey() != null) {
			jsonObject.put("rowKey", model.getRowKey());
		}

		if (model.getScheduleFlag() != null) {
			jsonObject.put("scheduleFlag", model.getScheduleFlag());
		}

		if (model.getCacheFlag() != null) {
			jsonObject.put("cacheFlag", model.getCacheFlag());
		}
		if (model.getDataItemFlag() != null) {
			jsonObject.put("dataItemFlag", model.getDataItemFlag());
		} 
		if (model.getFetchFlag() != null) {
			jsonObject.put("fetchFlag", model.getFetchFlag());
		}
		if (model.getDeleteFetch() != null) {
			jsonObject.put("deleteFetch", model.getDeleteFetch());
		}
		if (model.getTargetTableName() != null) {
			jsonObject.put("targetTableName", model.getTargetTableName());
		}

		if (model.getExportFlag() != null) {
			jsonObject.put("exportFlag", model.getExportFlag());
		}
		if (model.getExportTableName() != null) {
			jsonObject.put("exportTableName", model.getExportTableName());
		}
		if (model.getExportTemplate() != null) {
			jsonObject.put("exportTemplate", model.getExportTemplate());
		}
		if (model.getPublicFlag() != null) {
			jsonObject.put("publicFlag", model.getPublicFlag());
		}

		if (model.getShareFlag() != null) {
			jsonObject.put("shareFlag", model.getShareFlag());
		}

		if (model.getSaveFlag() != null) {
			jsonObject.put("saveFlag", model.getSaveFlag());
		}

		if (model.getAggregationFlag() != null) {
			jsonObject.put("aggregationFlag", model.getAggregationFlag());
		}

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

	private SqlDefinitionJsonFactory() {

	}

}
