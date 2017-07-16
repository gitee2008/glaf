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
public class AggregationDefinitionJsonFactory {

	public static java.util.List<AggregationDefinition> arrayToList(JSONArray array) {
		java.util.List<AggregationDefinition> list = new java.util.ArrayList<AggregationDefinition>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			AggregationDefinition model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	public static AggregationDefinition jsonToObject(JSONObject jsonObject) {
		AggregationDefinition model = new AggregationDefinition();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getLong("id"));
		}

		if (jsonObject.containsKey("title")) {
			model.setTitle(jsonObject.getString("title"));
		}
		if (jsonObject.containsKey("serviceKey")) {
			model.setServiceKey(jsonObject.getString("serviceKey"));
		}

		if (jsonObject.containsKey("name")) {
			model.setName(jsonObject.getString("name"));
		}
		if (jsonObject.containsKey("sql")) {
			model.setSql(jsonObject.getString("sql"));
		}
		if (jsonObject.containsKey("precision")) {
			model.setPrecision(jsonObject.getInteger("precision"));
		}
		if (jsonObject.containsKey("targetTableName")) {
			model.setTargetTableName(jsonObject.getString("targetTableName"));
		}
		if (jsonObject.containsKey("partitionFlag")) {
			model.setPartitionFlag(jsonObject.getString("partitionFlag"));
		}
		if (jsonObject.containsKey("resultFlag")) {
			model.setResultFlag(jsonObject.getString("resultFlag"));
		}
		if (jsonObject.containsKey("type")) {
			model.setType(jsonObject.getString("type"));
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

	public static JSONArray listToArray(java.util.List<AggregationDefinition> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (AggregationDefinition model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static JSONObject toJsonObject(AggregationDefinition model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());

		if (model.getTitle() != null) {
			jsonObject.put("title", model.getTitle());
		}
		if (model.getServiceKey() != null) {
			jsonObject.put("serviceKey", model.getServiceKey());
		}

		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getSql() != null) {
			jsonObject.put("sql", model.getSql());
		}
		jsonObject.put("precision", model.getPrecision());
		if (model.getTargetTableName() != null) {
			jsonObject.put("targetTableName", model.getTargetTableName());
		}
		if (model.getPartitionFlag() != null) {
			jsonObject.put("partitionFlag", model.getPartitionFlag());
		}
		if (model.getResultFlag() != null) {
			jsonObject.put("resultFlag", model.getResultFlag());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
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

	public static ObjectNode toObjectNode(AggregationDefinition model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());

		if (model.getTitle() != null) {
			jsonObject.put("title", model.getTitle());
		}
		if (model.getServiceKey() != null) {
			jsonObject.put("serviceKey", model.getServiceKey());
		}

		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getSql() != null) {
			jsonObject.put("sql", model.getSql());
		}
		jsonObject.put("precision", model.getPrecision());
		if (model.getTargetTableName() != null) {
			jsonObject.put("targetTableName", model.getTargetTableName());
		}
		if (model.getPartitionFlag() != null) {
			jsonObject.put("partitionFlag", model.getPartitionFlag());
		}

		if (model.getResultFlag() != null) {
			jsonObject.put("resultFlag", model.getResultFlag());
		}

		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
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

	private AggregationDefinitionJsonFactory() {

	}

}
