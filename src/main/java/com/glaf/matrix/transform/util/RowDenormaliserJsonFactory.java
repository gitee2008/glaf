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
public class RowDenormaliserJsonFactory {

	public static RowDenormaliser jsonToObject(JSONObject jsonObject) {
		RowDenormaliser model = new RowDenormaliser();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getString("id"));
		}
		if (jsonObject.containsKey("title")) {
			model.setTitle(jsonObject.getString("title"));
		}
		if (jsonObject.containsKey("databaseIds")) {
			model.setDatabaseIds(jsonObject.getString("databaseIds"));
		}
		if (jsonObject.containsKey("sourceTableName")) {
			model.setSourceTableName(jsonObject.getString("sourceTableName"));
		}
		if (jsonObject.containsKey("aggregateColumns")) {
			model.setAggregateColumns(jsonObject.getString("aggregateColumns"));
		}
		if (jsonObject.containsKey("primaryKey")) {
			model.setPrimaryKey(jsonObject.getString("primaryKey"));
		}
		if (jsonObject.containsKey("delimiter")) {
			model.setDelimiter(jsonObject.getString("delimiter"));
		}
		if (jsonObject.containsKey("transformColumn")) {
			model.setTransformColumn(jsonObject.getString("transformColumn"));
		}
		if (jsonObject.containsKey("syncColumns")) {
			model.setSyncColumns(jsonObject.getString("syncColumns"));
		}
		if (jsonObject.containsKey("dateDimensionColumn")) {
			model.setDateDimensionColumn(jsonObject.getString("dateDimensionColumn"));
		}
		if (jsonObject.containsKey("incrementColumn")) {
			model.setIncrementColumn(jsonObject.getString("incrementColumn"));
		}
		if (jsonObject.containsKey("sqlCriteria")) {
			model.setSqlCriteria(jsonObject.getString("sqlCriteria"));
		}
		if (jsonObject.containsKey("sort")) {
			model.setSort(jsonObject.getInteger("sort"));
		}
		if (jsonObject.containsKey("transformStatus")) {
			model.setTransformStatus(jsonObject.getInteger("transformStatus"));
		}
		if (jsonObject.containsKey("transformTime")) {
			model.setTransformTime(jsonObject.getDate("transformTime"));
		}
		if (jsonObject.containsKey("transformFlag")) {
			model.setTransformFlag(jsonObject.getString("transformFlag"));
		}
		if (jsonObject.containsKey("targetTableName")) {
			model.setTargetTableName(jsonObject.getString("targetTableName"));
		}
		if (jsonObject.containsKey("targetColumn")) {
			model.setTargetColumn(jsonObject.getString("targetColumn"));
		}
		if (jsonObject.containsKey("targetColumnType")) {
			model.setTargetColumnType(jsonObject.getString("targetColumnType"));
		}
		if (jsonObject.containsKey("scheduleFlag")) {
			model.setScheduleFlag(jsonObject.getString("scheduleFlag"));
		}
		if (jsonObject.containsKey("deleteFetch")) {
			model.setDeleteFetch(jsonObject.getString("deleteFetch"));
		}
		if (jsonObject.containsKey("locked")) {
			model.setLocked(jsonObject.getInteger("locked"));
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

	public static JSONObject toJsonObject(RowDenormaliser model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getTitle() != null) {
			jsonObject.put("title", model.getTitle());
		}
		if (model.getDatabaseIds() != null) {
			jsonObject.put("databaseIds", model.getDatabaseIds());
		}
		if (model.getSourceTableName() != null) {
			jsonObject.put("sourceTableName", model.getSourceTableName());
		}
		if (model.getAggregateColumns() != null) {
			jsonObject.put("aggregateColumns", model.getAggregateColumns());
		}
		if (model.getPrimaryKey() != null) {
			jsonObject.put("primaryKey", model.getPrimaryKey());
		}
		if (model.getTransformColumn() != null) {
			jsonObject.put("transformColumn", model.getTransformColumn());
		}
		if (model.getSyncColumns() != null) {
			jsonObject.put("syncColumns", model.getSyncColumns());
		}
		if (model.getDateDimensionColumn() != null) {
			jsonObject.put("dateDimensionColumn", model.getDateDimensionColumn());
		}
		if (model.getIncrementColumn() != null) {
			jsonObject.put("incrementColumn", model.getIncrementColumn());
		}
		if (model.getSqlCriteria() != null) {
			jsonObject.put("sqlCriteria", model.getSqlCriteria());
		}
		if (model.getDelimiter() != null) {
			jsonObject.put("delimiter", model.getDelimiter());
		}
		jsonObject.put("sort", model.getSort());
		jsonObject.put("transformStatus", model.getTransformStatus());
		if (model.getTransformTime() != null) {
			jsonObject.put("transformTime", DateUtils.getDate(model.getTransformTime()));
			jsonObject.put("transformTime_date", DateUtils.getDate(model.getTransformTime()));
			jsonObject.put("transformTime_datetime", DateUtils.getDateTime(model.getTransformTime()));
		}
		if (model.getTransformFlag() != null) {
			jsonObject.put("transformFlag", model.getTransformFlag());
		}
		if (model.getTargetTableName() != null) {
			jsonObject.put("targetTableName", model.getTargetTableName());
		}
		if (model.getTargetColumn() != null) {
			jsonObject.put("targetColumn", model.getTargetColumn());
		}
		if (model.getTargetColumnType() != null) {
			jsonObject.put("targetColumnType", model.getTargetColumnType());
		}
		if (model.getScheduleFlag() != null) {
			jsonObject.put("scheduleFlag", model.getScheduleFlag());
		}
		if (model.getDeleteFetch() != null) {
			jsonObject.put("deleteFetch", model.getDeleteFetch());
		}
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

	public static ObjectNode toObjectNode(RowDenormaliser model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getTitle() != null) {
			jsonObject.put("title", model.getTitle());
		}
		if (model.getDatabaseIds() != null) {
			jsonObject.put("databaseIds", model.getDatabaseIds());
		}
		if (model.getSourceTableName() != null) {
			jsonObject.put("sourceTableName", model.getSourceTableName());
		}
		if (model.getAggregateColumns() != null) {
			jsonObject.put("aggregateColumns", model.getAggregateColumns());
		}
		if (model.getPrimaryKey() != null) {
			jsonObject.put("primaryKey", model.getPrimaryKey());
		}
		if (model.getTransformColumn() != null) {
			jsonObject.put("transformColumn", model.getTransformColumn());
		}
		if (model.getSyncColumns() != null) {
			jsonObject.put("syncColumns", model.getSyncColumns());
		}
		if (model.getDateDimensionColumn() != null) {
			jsonObject.put("dateDimensionColumn", model.getDateDimensionColumn());
		}
		if (model.getIncrementColumn() != null) {
			jsonObject.put("incrementColumn", model.getIncrementColumn());
		}
		if (model.getSqlCriteria() != null) {
			jsonObject.put("sqlCriteria", model.getSqlCriteria());
		}
		if (model.getDelimiter() != null) {
			jsonObject.put("delimiter", model.getDelimiter());
		}
		jsonObject.put("sort", model.getSort());
		jsonObject.put("transformStatus", model.getTransformStatus());
		if (model.getTransformTime() != null) {
			jsonObject.put("transformTime", DateUtils.getDate(model.getTransformTime()));
			jsonObject.put("transformTime_date", DateUtils.getDate(model.getTransformTime()));
			jsonObject.put("transformTime_datetime", DateUtils.getDateTime(model.getTransformTime()));
		}
		if (model.getTransformFlag() != null) {
			jsonObject.put("transformFlag", model.getTransformFlag());
		}
		if (model.getTargetTableName() != null) {
			jsonObject.put("targetTableName", model.getTargetTableName());
		}
		if (model.getTargetColumn() != null) {
			jsonObject.put("targetColumn", model.getTargetColumn());
		}
		if (model.getTargetColumnType() != null) {
			jsonObject.put("targetColumnType", model.getTargetColumnType());
		}
		if (model.getScheduleFlag() != null) {
			jsonObject.put("scheduleFlag", model.getScheduleFlag());
		}
		if (model.getDeleteFetch() != null) {
			jsonObject.put("deleteFetch", model.getDeleteFetch());
		}
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

	public static JSONArray listToArray(java.util.List<RowDenormaliser> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (RowDenormaliser model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static java.util.List<RowDenormaliser> arrayToList(JSONArray array) {
		java.util.List<RowDenormaliser> list = new java.util.ArrayList<RowDenormaliser>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			RowDenormaliser model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	private RowDenormaliserJsonFactory() {

	}

}
