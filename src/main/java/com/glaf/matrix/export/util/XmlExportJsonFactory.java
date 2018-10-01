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

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.core.util.DateUtils;
import com.glaf.matrix.export.domain.XmlExport;
import com.glaf.matrix.export.domain.XmlExportItem;

/**
 * 
 * JSON工厂类
 *
 */
public class XmlExportJsonFactory {

	public static XmlExport jsonToObject(JSONObject jsonObject) {
		XmlExport model = new XmlExport();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getString("id"));
		}
		if (jsonObject.containsKey("nodeId")) {
			model.setNodeId(jsonObject.getLong("nodeId"));
		}
		if (jsonObject.containsKey("nodeParentId")) {
			model.setNodeParentId(jsonObject.getLong("nodeParentId"));
		}
		if (jsonObject.containsKey("name")) {
			model.setName(jsonObject.getString("name"));
		}
		if (jsonObject.containsKey("title")) {
			model.setTitle(jsonObject.getString("title"));
		}
		if (jsonObject.containsKey("sql")) {
			model.setSql(jsonObject.getString("sql"));
		}
		if (jsonObject.containsKey("resultFlag")) {
			model.setResultFlag(jsonObject.getString("resultFlag"));
		}
		if (jsonObject.containsKey("leafFlag")) {
			model.setLeafFlag(jsonObject.getString("leafFlag"));
		}
		if (jsonObject.containsKey("treeFlag")) {
			model.setTreeFlag(jsonObject.getString("treeFlag"));
		}
		if (jsonObject.containsKey("type")) {
			model.setType(jsonObject.getString("type"));
		}
		if (jsonObject.containsKey("active")) {
			model.setActive(jsonObject.getString("active"));
		}
		if (jsonObject.containsKey("xmlTag")) {
			model.setXmlTag(jsonObject.getString("xmlTag"));
		}
		if (jsonObject.containsKey("templateId")) {
			model.setTemplateId(jsonObject.getString("templateId"));
		}
		if (jsonObject.containsKey("externalAttrsFlag")) {
			model.setExternalAttrsFlag(jsonObject.getString("externalAttrsFlag"));
		}
		if (jsonObject.containsKey("allowRoles")) {
			model.setAllowRoles(jsonObject.getString("allowRoles"));
		}
		if (jsonObject.containsKey("interval")) {
			model.setInterval(jsonObject.getInteger("interval"));
		}
		if (jsonObject.containsKey("sortNo")) {
			model.setSortNo(jsonObject.getInteger("sortNo"));
		}

		if (jsonObject.containsKey("items")) {
			JSONArray array = jsonObject.getJSONArray("items");
			List<XmlExportItem> items = XmlExportItemJsonFactory.arrayToList(array);
			model.setItems(items);
		}

		return model;
	}

	public static JSONObject toJsonObject(XmlExport model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		jsonObject.put("nodeId", model.getNodeId());
		jsonObject.put("sortNo", model.getSortNo());

		if (model.getNodeParentId() != 0) {
			jsonObject.put("nodeParentId", model.getNodeParentId());
		}
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getTitle() != null) {
			jsonObject.put("title", model.getTitle());
		}
		if (model.getSql() != null) {
			jsonObject.put("sql", model.getSql());
		}
		if (model.getResultFlag() != null) {
			jsonObject.put("resultFlag", model.getResultFlag());
		}
		if (model.getLeafFlag() != null) {
			jsonObject.put("leafFlag", model.getLeafFlag());
		}
		if (model.getTreeFlag() != null) {
			jsonObject.put("treeFlag", model.getTreeFlag());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		if (model.getActive() != null) {
			jsonObject.put("active", model.getActive());
		}
		if (model.getXmlTag() != null) {
			jsonObject.put("xmlTag", model.getXmlTag());
		}
		if (model.getTemplateId() != null) {
			jsonObject.put("templateId", model.getTemplateId());
		}
		if (model.getExternalAttrsFlag() != null) {
			jsonObject.put("externalAttrsFlag", model.getExternalAttrsFlag());
		}
		if (model.getAllowRoles() != null) {
			jsonObject.put("allowRoles", model.getAllowRoles());
		}
		jsonObject.put("interval", model.getInterval());
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

		if (model.getItems() != null && !model.getItems().isEmpty()) {
			JSONArray array = XmlExportItemJsonFactory.listToArray(model.getItems());
			jsonObject.put("items", array);
		}

		return jsonObject;
	}

	public static ObjectNode toObjectNode(XmlExport model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		jsonObject.put("nodeId", model.getNodeId());
		jsonObject.put("sortNo", model.getSortNo());

		if (model.getNodeParentId() != 0) {
			jsonObject.put("nodeParentId", model.getNodeParentId());
		}
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getMapping() != null) {
			jsonObject.put("mapping", model.getMapping());
		}
		if (model.getTitle() != null) {
			jsonObject.put("title", model.getTitle());
		}
		if (model.getSql() != null) {
			jsonObject.put("sql", model.getSql());
		}
		if (model.getResultFlag() != null) {
			jsonObject.put("resultFlag", model.getResultFlag());
		}
		if (model.getLeafFlag() != null) {
			jsonObject.put("leafFlag", model.getLeafFlag());
		}
		if (model.getTreeFlag() != null) {
			jsonObject.put("treeFlag", model.getTreeFlag());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		if (model.getActive() != null) {
			jsonObject.put("active", model.getActive());
		}
		if (model.getXmlTag() != null) {
			jsonObject.put("xmlTag", model.getXmlTag());
		}
		if (model.getTemplateId() != null) {
			jsonObject.put("templateId", model.getTemplateId());
		}
		if (model.getExternalAttrsFlag() != null) {
			jsonObject.put("externalAttrsFlag", model.getExternalAttrsFlag());
		}
		if (model.getAllowRoles() != null) {
			jsonObject.put("allowRoles", model.getAllowRoles());
		}
		jsonObject.put("interval", model.getInterval());
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

	public static JSONArray listToArray(java.util.List<XmlExport> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (XmlExport model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static java.util.List<XmlExport> arrayToList(JSONArray array) {
		java.util.List<XmlExport> list = new java.util.ArrayList<XmlExport>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			XmlExport model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	private XmlExportJsonFactory() {

	}

}
