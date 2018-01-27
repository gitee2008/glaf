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
import com.glaf.matrix.combination.domain.*;

/**
 * 
 * JSON工厂类
 *
 */
public class TreeTableAggregateRuleJsonFactory {

	public static java.util.List<TreeTableAggregateRule> arrayToList(JSONArray array) {
		java.util.List<TreeTableAggregateRule> list = new java.util.ArrayList<TreeTableAggregateRule>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			TreeTableAggregateRule model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	public static TreeTableAggregateRule jsonToObject(JSONObject jsonObject) {
		TreeTableAggregateRule model = new TreeTableAggregateRule();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getLong("id"));
		}
		if (jsonObject.containsKey("aggregateId")) {
			model.setAggregateId(jsonObject.getLong("aggregateId"));
		}
		if (jsonObject.containsKey("aggregateType")) {
			model.setAggregateType(jsonObject.getString("aggregateType"));
		}
		if (jsonObject.containsKey("name")) {
			model.setName(jsonObject.getString("name"));
		}
		if (jsonObject.containsKey("title")) {
			model.setTitle(jsonObject.getString("title"));
		}
		if (jsonObject.containsKey("sourceColumnName")) {
			model.setSourceColumnName(jsonObject.getString("sourceColumnName"));
		}
		if (jsonObject.containsKey("sourceColumnTitle")) {
			model.setSourceColumnTitle(jsonObject.getString("sourceColumnTitle"));
		}
		if (jsonObject.containsKey("targetColumnName")) {
			model.setTargetColumnName(jsonObject.getString("targetColumnName"));
		}
		if (jsonObject.containsKey("targetColumnTitle")) {
			model.setTargetColumnTitle(jsonObject.getString("targetColumnTitle"));
		}
		if (jsonObject.containsKey("targetColumnType")) {
			model.setTargetColumnType(jsonObject.getString("targetColumnType"));
		}
		if (jsonObject.containsKey("locked")) {
			model.setLocked(jsonObject.getInteger("locked"));
		}

		return model;
	}

	public static JSONArray listToArray(java.util.List<TreeTableAggregateRule> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (TreeTableAggregateRule model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static JSONObject toJsonObject(TreeTableAggregateRule model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		jsonObject.put("aggregateId", model.getAggregateId());
		if (model.getAggregateType() != null) {
			jsonObject.put("aggregateType", model.getAggregateType());
		}
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getTitle() != null) {
			jsonObject.put("title", model.getTitle());
		}
		if (model.getSourceColumnName() != null) {
			jsonObject.put("sourceColumnName", model.getSourceColumnName());
		}
		if (model.getSourceColumnTitle() != null) {
			jsonObject.put("sourceColumnTitle", model.getSourceColumnTitle());
		}
		if (model.getTargetColumnName() != null) {
			jsonObject.put("targetColumnName", model.getTargetColumnName());
		}
		if (model.getTargetColumnTitle() != null) {
			jsonObject.put("targetColumnTitle", model.getTargetColumnTitle());
		}
		if (model.getTargetColumnType() != null) {
			jsonObject.put("targetColumnType", model.getTargetColumnType());
		}
		jsonObject.put("locked", model.getLocked());
		return jsonObject;
	}

	public static ObjectNode toObjectNode(TreeTableAggregateRule model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		jsonObject.put("aggregateId", model.getAggregateId());
		if (model.getAggregateType() != null) {
			jsonObject.put("aggregateType", model.getAggregateType());
		}
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getTitle() != null) {
			jsonObject.put("title", model.getTitle());
		}
		if (model.getSourceColumnName() != null) {
			jsonObject.put("sourceColumnName", model.getSourceColumnName());
		}
		if (model.getSourceColumnTitle() != null) {
			jsonObject.put("sourceColumnTitle", model.getSourceColumnTitle());
		}
		if (model.getTargetColumnName() != null) {
			jsonObject.put("targetColumnName", model.getTargetColumnName());
		}
		if (model.getTargetColumnTitle() != null) {
			jsonObject.put("targetColumnTitle", model.getTargetColumnTitle());
		}
		if (model.getTargetColumnType() != null) {
			jsonObject.put("targetColumnType", model.getTargetColumnType());
		}
		jsonObject.put("locked", model.getLocked());
		return jsonObject;
	}

	private TreeTableAggregateRuleJsonFactory() {

	}

}
