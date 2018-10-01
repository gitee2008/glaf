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

package com.glaf.matrix.export.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.core.util.DateUtils;
import com.glaf.matrix.export.domain.XmlExportItem;

/**
 * 
 * JSON工厂类
 *
 */
public class XmlExportItemJsonFactory {

	public static XmlExportItem jsonToObject(JSONObject jsonObject) {
		XmlExportItem model = new XmlExportItem();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getString("id"));
		}
		if (jsonObject.containsKey("expId")) {
			model.setExpId(jsonObject.getString("expId"));
		}
		if (jsonObject.containsKey("name")) {
			model.setName(jsonObject.getString("name"));
		}
		if (jsonObject.containsKey("title")) {
			model.setTitle(jsonObject.getString("title"));
		}

		if (jsonObject.containsKey("tagFlag")) {
			model.setTagFlag(jsonObject.getString("tagFlag"));
		}

		if (jsonObject.containsKey("expression")) {
			model.setExpression(jsonObject.getString("expression"));
		}

		if (jsonObject.containsKey("defaultValue")) {
			model.setDefaultValue(jsonObject.getString("defaultValue"));
		}

		if (jsonObject.containsKey("dataType")) {
			model.setDataType(jsonObject.getString("dataType"));
		}

		if (jsonObject.containsKey("required")) {
			model.setRequired(jsonObject.getString("required"));
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

		return model;
	}

	public static JSONObject toJsonObject(XmlExportItem model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		jsonObject.put("expId", model.getExpId());
		jsonObject.put("sortNo", model.getSortNo());
		jsonObject.put("locked", model.getLocked());

		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getTitle() != null) {
			jsonObject.put("title", model.getTitle());
		}
		if (model.getTagFlag() != null) {
			jsonObject.put("tagFlag", model.getTagFlag());
		}

		if (model.getExpression() != null) {
			jsonObject.put("expression", model.getExpression());
		}

		if (model.getDefaultValue() != null) {
			jsonObject.put("defaultValue", model.getDefaultValue());
		}

		if (model.getDataType() != null) {
			jsonObject.put("dataType", model.getDataType());
		}

		if (model.getRequired() != null) {
			jsonObject.put("required", model.getRequired());
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

	public static ObjectNode toObjectNode(XmlExportItem model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		jsonObject.put("expId", model.getExpId());
		jsonObject.put("sortNo", model.getSortNo());
		jsonObject.put("locked", model.getLocked());

		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getTitle() != null) {
			jsonObject.put("title", model.getTitle());
		}
		if (model.getTagFlag() != null) {
			jsonObject.put("tagFlag", model.getTagFlag());
		}
		if (model.getExpression() != null) {
			jsonObject.put("expression", model.getExpression());
		}
		if (model.getDefaultValue() != null) {
			jsonObject.put("defaultValue", model.getDefaultValue());
		}
		if (model.getDataType() != null) {
			jsonObject.put("dataType", model.getDataType());
		}
		if (model.getRequired() != null) {
			jsonObject.put("required", model.getRequired());
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

	public static JSONArray listToArray(java.util.List<XmlExportItem> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (XmlExportItem model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static java.util.List<XmlExportItem> arrayToList(JSONArray array) {
		java.util.List<XmlExportItem> list = new java.util.ArrayList<XmlExportItem>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			XmlExportItem model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	private XmlExportItemJsonFactory() {

	}

}
