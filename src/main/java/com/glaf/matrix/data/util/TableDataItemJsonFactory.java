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
import com.glaf.matrix.data.domain.TableDataItem;

/**
 * 
 * JSON工厂类
 *
 */
public class TableDataItemJsonFactory {

	public static TableDataItem jsonToObject(JSONObject jsonObject) {
		TableDataItem model = new TableDataItem();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getString("id"));
		}
		if (jsonObject.containsKey("title")) {
			model.setTitle(jsonObject.getString("title"));
		}
		if (jsonObject.containsKey("description")) {
			model.setDescription(jsonObject.getString("description"));
		}
		if (jsonObject.containsKey("tableName")) {
			model.setTableName(jsonObject.getString("tableName"));
		}
		if (jsonObject.containsKey("nameColumn")) {
			model.setNameColumn(jsonObject.getString("nameColumn"));
		}
		if (jsonObject.containsKey("valueColumn")) {
			model.setValueColumn(jsonObject.getString("valueColumn"));
		}
		if (jsonObject.containsKey("sortColumn")) {
			model.setSortColumn(jsonObject.getString("sortColumn"));
		}
		if (jsonObject.containsKey("filterFlag")) {
			model.setFilterFlag(jsonObject.getString("filterFlag"));
		}
		if (jsonObject.containsKey("type")) {
			model.setType(jsonObject.getString("type"));
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

	public static JSONObject toJsonObject(TableDataItem model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getTitle() != null) {
			jsonObject.put("title", model.getTitle());
		}
		if (model.getDescription() != null) {
			jsonObject.put("description", model.getDescription());
		}
		if (model.getTableName() != null) {
			jsonObject.put("tableName", model.getTableName());
		}
		if (model.getNameColumn() != null) {
			jsonObject.put("nameColumn", model.getNameColumn());
		}
		if (model.getValueColumn() != null) {
			jsonObject.put("valueColumn", model.getValueColumn());
		}
		if (model.getSortColumn() != null) {
			jsonObject.put("sortColumn", model.getSortColumn());
		}
		if (model.getFilterFlag() != null) {
			jsonObject.put("filterFlag", model.getFilterFlag());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
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

	public static ObjectNode toObjectNode(TableDataItem model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getTitle() != null) {
			jsonObject.put("title", model.getTitle());
		}
		if (model.getDescription() != null) {
			jsonObject.put("description", model.getDescription());
		}
		if (model.getTableName() != null) {
			jsonObject.put("tableName", model.getTableName());
		}
		if (model.getNameColumn() != null) {
			jsonObject.put("nameColumn", model.getNameColumn());
		}
		if (model.getValueColumn() != null) {
			jsonObject.put("valueColumn", model.getValueColumn());
		}
		if (model.getSortColumn() != null) {
			jsonObject.put("sortColumn", model.getSortColumn());
		}
		if (model.getFilterFlag() != null) {
			jsonObject.put("filterFlag", model.getFilterFlag());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
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

	public static JSONArray listToArray(java.util.List<TableDataItem> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (TableDataItem model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static java.util.List<TableDataItem> arrayToList(JSONArray array) {
		java.util.List<TableDataItem> list = new java.util.ArrayList<TableDataItem>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			TableDataItem model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	private TableDataItemJsonFactory() {

	}

}
