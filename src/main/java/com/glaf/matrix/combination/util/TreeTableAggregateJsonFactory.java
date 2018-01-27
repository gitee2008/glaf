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

package com.glaf.matrix.combination.util;

import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.glaf.core.util.DateUtils;
import com.glaf.matrix.combination.domain.*;

/**
 * 
 * JSON工厂类
 *
 */
public class TreeTableAggregateJsonFactory {

	public static TreeTableAggregate jsonToObject(JSONObject jsonObject) {
		TreeTableAggregate model = new TreeTableAggregate();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getLong("id"));
		}
		if (jsonObject.containsKey("name")) {
			model.setName(jsonObject.getString("name"));
		}
		if (jsonObject.containsKey("title")) {
			model.setTitle(jsonObject.getString("title"));
		}
		if (jsonObject.containsKey("type")) {
			model.setType(jsonObject.getString("type"));
		}
		if (jsonObject.containsKey("sourceTableName")) {
			model.setSourceTableName(jsonObject.getString("sourceTableName"));
		}
		if (jsonObject.containsKey("sourceIdColumn")) {
			model.setSourceIdColumn(jsonObject.getString("sourceIdColumn"));
		}
		if (jsonObject.containsKey("sourceIndexIdColumn")) {
			model.setSourceIndexIdColumn(jsonObject.getString("sourceIndexIdColumn"));
		}
		if (jsonObject.containsKey("sourceParentIdColumn")) {
			model.setSourceParentIdColumn(jsonObject.getString("sourceParentIdColumn"));
		}
		if (jsonObject.containsKey("sourceTreeIdColumn")) {
			model.setSourceTreeIdColumn(jsonObject.getString("sourceTreeIdColumn"));
		}
		if (jsonObject.containsKey("sourceTextColumn")) {
			model.setSourceTextColumn(jsonObject.getString("sourceTextColumn"));
		}
		if (jsonObject.containsKey("sourceWbsIndexColumn")) {
			model.setSourceWbsIndexColumn(jsonObject.getString("sourceWbsIndexColumn"));
		}
		if (jsonObject.containsKey("sourceTableDateSplitColumn")) {
			model.setSourceTableDateSplitColumn(jsonObject.getString("sourceTableDateSplitColumn"));
		}
		if (jsonObject.containsKey("sourceTableExecutionIds")) {
			model.setSourceTableExecutionIds(jsonObject.getString("sourceTableExecutionIds"));
		}
		if (jsonObject.containsKey("databaseIds")) {
			model.setDatabaseIds(jsonObject.getString("databaseIds"));
		}
		if (jsonObject.containsKey("targetTableName")) {
			model.setTargetTableName(jsonObject.getString("targetTableName"));
		}
		if (jsonObject.containsKey("targetTableExecutionIds")) {
			model.setTargetTableExecutionIds(jsonObject.getString("targetTableExecutionIds"));
		}
		if (jsonObject.containsKey("createTableFlag")) {
			model.setCreateTableFlag(jsonObject.getString("createTableFlag"));
		}
		if (jsonObject.containsKey("aggregateFlag")) {
			model.setAggregateFlag(jsonObject.getString("aggregateFlag"));
		}
		if (jsonObject.containsKey("scheduleFlag")) {
			model.setScheduleFlag(jsonObject.getString("scheduleFlag"));
		}
		if (jsonObject.containsKey("genByMonth")) {
			model.setGenByMonth(jsonObject.getString("genByMonth"));
		}
		if (jsonObject.containsKey("deleteFetch")) {
			model.setDeleteFetch(jsonObject.getString("deleteFetch"));
		}
		if (jsonObject.containsKey("dynamicCondition")) {
			model.setDynamicCondition(jsonObject.getString("dynamicCondition"));
		}
		if (jsonObject.containsKey("jobNo")) {
			model.setJobNo(jsonObject.getString("jobNo"));
		}
		if (jsonObject.containsKey("syncColumns")) {
			model.setSyncColumns(jsonObject.getString("syncColumns"));
		}
		if (jsonObject.containsKey("syncStatus")) {
			model.setSyncStatus(jsonObject.getInteger("syncStatus"));
		}
		if (jsonObject.containsKey("syncTime")) {
			model.setSyncTime(jsonObject.getDate("syncTime"));
		}
		if (jsonObject.containsKey("startYear")) {
			model.setStartYear(jsonObject.getInteger("startYear"));
		}
		if (jsonObject.containsKey("stopYear")) {
			model.setStopYear(jsonObject.getInteger("stopYear"));
		}
		if (jsonObject.containsKey("sortNo")) {
			model.setSortNo(jsonObject.getInteger("sortNo"));
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

	public static JSONObject toJsonObject(TreeTableAggregate model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getTitle() != null) {
			jsonObject.put("title", model.getTitle());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		if (model.getSourceTableName() != null) {
			jsonObject.put("sourceTableName", model.getSourceTableName());
		}
		if (model.getSourceIdColumn() != null) {
			jsonObject.put("sourceIdColumn", model.getSourceIdColumn());
		}
		if (model.getSourceIndexIdColumn() != null) {
			jsonObject.put("sourceIndexIdColumn", model.getSourceIndexIdColumn());
		}
		if (model.getSourceParentIdColumn() != null) {
			jsonObject.put("sourceParentIdColumn", model.getSourceParentIdColumn());
		}
		if (model.getSourceTreeIdColumn() != null) {
			jsonObject.put("sourceTreeIdColumn", model.getSourceTreeIdColumn());
		}
		if (model.getSourceTextColumn() != null) {
			jsonObject.put("sourceTextColumn", model.getSourceTextColumn());
		}
		if (model.getSourceWbsIndexColumn() != null) {
			jsonObject.put("sourceWbsIndexColumn", model.getSourceWbsIndexColumn());
		}
		if (model.getSourceTableDateSplitColumn() != null) {
			jsonObject.put("sourceTableDateSplitColumn", model.getSourceTableDateSplitColumn());
		}
		if (model.getSourceTableExecutionIds() != null) {
			jsonObject.put("sourceTableExecutionIds", model.getSourceTableExecutionIds());
		}
		if (model.getDatabaseIds() != null) {
			jsonObject.put("databaseIds", model.getDatabaseIds());
		}
		if (model.getDynamicCondition() != null) {
			jsonObject.put("dynamicCondition", model.getDynamicCondition());
		}
		if (model.getTargetTableName() != null) {
			jsonObject.put("targetTableName", model.getTargetTableName());
		}
		if (model.getTargetTableExecutionIds() != null) {
			jsonObject.put("targetTableExecutionIds", model.getTargetTableExecutionIds());
		}
		if (model.getCreateTableFlag() != null) {
			jsonObject.put("createTableFlag", model.getCreateTableFlag());
		}
		if (model.getScheduleFlag() != null) {
			jsonObject.put("scheduleFlag", model.getScheduleFlag());
		}
		if (model.getAggregateFlag() != null) {
			jsonObject.put("aggregateFlag", model.getAggregateFlag());
		}
		if (model.getGenByMonth() != null) {
			jsonObject.put("genByMonth", model.getGenByMonth());
		}
		if (model.getDeleteFetch() != null) {
			jsonObject.put("deleteFetch", model.getDeleteFetch());
		}
		if (model.getJobNo() != null) {
			jsonObject.put("jobNo", model.getJobNo());
		}
		if (model.getSyncColumns() != null) {
			jsonObject.put("syncColumns", model.getSyncColumns());
		}
		jsonObject.put("syncStatus", model.getSyncStatus());
		if (model.getSyncTime() != null) {
			jsonObject.put("syncTime", DateUtils.getDate(model.getSyncTime()));
			jsonObject.put("syncTime_date", DateUtils.getDate(model.getSyncTime()));
			jsonObject.put("syncTime_datetime", DateUtils.getDateTime(model.getSyncTime()));
		}
		jsonObject.put("startYear", model.getStartYear());
		jsonObject.put("stopYear", model.getStopYear());
		jsonObject.put("sortNo", model.getSortNo());
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

	public static ObjectNode toObjectNode(TreeTableAggregate model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getTitle() != null) {
			jsonObject.put("title", model.getTitle());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		if (model.getSourceTableName() != null) {
			jsonObject.put("sourceTableName", model.getSourceTableName());
		}
		if (model.getSourceIdColumn() != null) {
			jsonObject.put("sourceIdColumn", model.getSourceIdColumn());
		}
		if (model.getSourceIndexIdColumn() != null) {
			jsonObject.put("sourceIndexIdColumn", model.getSourceIndexIdColumn());
		}
		if (model.getSourceParentIdColumn() != null) {
			jsonObject.put("sourceParentIdColumn", model.getSourceParentIdColumn());
		}
		if (model.getSourceTreeIdColumn() != null) {
			jsonObject.put("sourceTreeIdColumn", model.getSourceTreeIdColumn());
		}
		if (model.getSourceTextColumn() != null) {
			jsonObject.put("sourceTextColumn", model.getSourceTextColumn());
		}
		if (model.getSourceWbsIndexColumn() != null) {
			jsonObject.put("sourceWbsIndexColumn", model.getSourceWbsIndexColumn());
		}
		if (model.getSourceTableDateSplitColumn() != null) {
			jsonObject.put("sourceTableDateSplitColumn", model.getSourceTableDateSplitColumn());
		}
		if (model.getSourceTableExecutionIds() != null) {
			jsonObject.put("sourceTableExecutionIds", model.getSourceTableExecutionIds());
		}
		if (model.getDatabaseIds() != null) {
			jsonObject.put("databaseIds", model.getDatabaseIds());
		}
		if (model.getTargetTableName() != null) {
			jsonObject.put("targetTableName", model.getTargetTableName());
		}
		if (model.getTargetTableExecutionIds() != null) {
			jsonObject.put("targetTableExecutionIds", model.getTargetTableExecutionIds());
		}
		if (model.getCreateTableFlag() != null) {
			jsonObject.put("createTableFlag", model.getCreateTableFlag());
		}
		if (model.getAggregateFlag() != null) {
			jsonObject.put("aggregateFlag", model.getAggregateFlag());
		}
		if (model.getDynamicCondition() != null) {
			jsonObject.put("dynamicCondition", model.getDynamicCondition());
		}
		if (model.getScheduleFlag() != null) {
			jsonObject.put("scheduleFlag", model.getScheduleFlag());
		}
		if (model.getGenByMonth() != null) {
			jsonObject.put("genByMonth", model.getGenByMonth());
		}
		if (model.getDeleteFetch() != null) {
			jsonObject.put("deleteFetch", model.getDeleteFetch());
		}
		if (model.getJobNo() != null) {
			jsonObject.put("jobNo", model.getJobNo());
		}
		if (model.getSyncColumns() != null) {
			jsonObject.put("syncColumns", model.getSyncColumns());
		}
		jsonObject.put("syncStatus", model.getSyncStatus());
		if (model.getSyncTime() != null) {
			jsonObject.put("syncTime", DateUtils.getDate(model.getSyncTime()));
			jsonObject.put("syncTime_date", DateUtils.getDate(model.getSyncTime()));
			jsonObject.put("syncTime_datetime", DateUtils.getDateTime(model.getSyncTime()));
		}
		jsonObject.put("startYear", model.getStartYear());
		jsonObject.put("stopYear", model.getStopYear());
		jsonObject.put("sortNo", model.getSortNo());
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

	public static JSONArray listToArray(java.util.List<TreeTableAggregate> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (TreeTableAggregate model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static java.util.List<TreeTableAggregate> arrayToList(JSONArray array) {
		java.util.List<TreeTableAggregate> list = new java.util.ArrayList<TreeTableAggregate>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			TreeTableAggregate model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	private TreeTableAggregateJsonFactory() {

	}

}
