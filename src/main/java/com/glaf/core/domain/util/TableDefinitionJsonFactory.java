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

package com.glaf.core.domain.util;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.domain.TableDefinition;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.RequestUtils;

public class TableDefinitionJsonFactory {

	public static java.util.List<TableDefinition> arrayToList(JSONArray array) {
		java.util.List<TableDefinition> list = new java.util.ArrayList<TableDefinition>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			TableDefinition model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	public static TableDefinition jsonToObject(JSONObject jsonObject) {
		TableDefinition model = new TableDefinition();
		if (jsonObject.containsKey("tableId")) {
			model.setTableId(jsonObject.getString("tableId"));
		}
		if (jsonObject.containsKey("tableName")) {
			model.setTableName(jsonObject.getString("tableName"));
		}
		if (jsonObject.containsKey("nodeId")) {
			model.setNodeId(jsonObject.getLong("nodeId"));
		}
		if (jsonObject.containsKey("packageName")) {
			model.setPackageName(jsonObject.getString("packageName"));
		}
		if (jsonObject.containsKey("entityName")) {
			model.setEntityName(jsonObject.getString("entityName"));
		}
		if (jsonObject.containsKey("className")) {
			model.setClassName(jsonObject.getString("className"));
		}
		if (jsonObject.containsKey("title")) {
			model.setTitle(jsonObject.getString("title"));
		}
		if (jsonObject.containsKey("englishTitle")) {
			model.setEnglishTitle(jsonObject.getString("englishTitle"));
		}
		if (jsonObject.containsKey("columnQty")) {
			model.setColumnQty(jsonObject.getInteger("columnQty"));
		}
		if (jsonObject.containsKey("addType")) {
			model.setAddType(jsonObject.getInteger("addType"));
		}
		if (jsonObject.containsKey("sysnum")) {
			model.setSysnum(jsonObject.getString("sysnum"));
		}
		if (jsonObject.containsKey("isSubTable")) {
			model.setIsSubTable(jsonObject.getString("isSubTable"));
		}
		if (jsonObject.containsKey("topId")) {
			model.setTopId(jsonObject.getString("topId"));
		}
		if (jsonObject.containsKey("aggregationKey")) {
			model.setAggregationKey(jsonObject.getString("aggregationKey"));
		}
		if (jsonObject.containsKey("primaryKey")) {
			model.setPrimaryKey(jsonObject.getString("primaryKey"));
		}
		if (jsonObject.containsKey("attachmentFlag")) {
			model.setAttachmentFlag(jsonObject.getString("attachmentFlag"));
		}
		if (jsonObject.containsKey("attachmentExts")) {
			model.setAttachmentExts(jsonObject.getString("attachmentExts"));
		}
		if (jsonObject.containsKey("attachmentSize")) {
			model.setAttachmentSize(jsonObject.getInteger("attachmentSize"));
		}
		if (jsonObject.containsKey("auditFlag")) {
			model.setAuditFlag(jsonObject.getString("auditFlag"));
		}
		if (jsonObject.containsKey("privilegeFlag")) {
			model.setPrivilegeFlag(jsonObject.getString("privilegeFlag"));
		}
		if (jsonObject.containsKey("treeFlag")) {
			model.setTreeFlag(jsonObject.getString("treeFlag"));
		}
		if (jsonObject.containsKey("temporaryFlag")) {
			model.setTemporaryFlag(jsonObject.getString("temporaryFlag"));
		}
		if (jsonObject.containsKey("deleteFetch")) {
			model.setDeleteFetch(jsonObject.getString("deleteFetch"));
		}
		if (jsonObject.containsKey("createTime")) {
			model.setCreateTime(jsonObject.getDate("createTime"));
		}
		if (jsonObject.containsKey("createBy")) {
			model.setCreateBy(jsonObject.getString("createBy"));
		}
		if (jsonObject.containsKey("description")) {
			model.setDescription(jsonObject.getString("description"));
		}
		if (jsonObject.containsKey("type")) {
			model.setType(jsonObject.getString("type"));
		}
		if (jsonObject.containsKey("displayType")) {
			model.setDisplayType(jsonObject.getString("displayType"));
		}
		if (jsonObject.containsKey("insertCascade")) {
			model.setInsertCascade(jsonObject.getInteger("insertCascade"));
		}
		if (jsonObject.containsKey("updateCascade")) {
			model.setUpdateCascade(jsonObject.getInteger("updateCascade"));
		}
		if (jsonObject.containsKey("deleteCascade")) {
			model.setDeleteCascade(jsonObject.getInteger("deleteCascade"));
		}
		if (jsonObject.containsKey("locked")) {
			model.setLocked(jsonObject.getInteger("locked"));
		}
		if (jsonObject.containsKey("deleteFlag")) {
			model.setDeleteFlag(jsonObject.getInteger("deleteFlag"));
		}
		if (jsonObject.containsKey("systemFlag")) {
			model.setSystemFlag(jsonObject.getString("systemFlag"));
		}
		if (jsonObject.containsKey("revision")) {
			model.setRevision(jsonObject.getInteger("revision"));
		}

		if (jsonObject.containsKey("columns")) {
			JSONArray array = jsonObject.getJSONArray("columns");
			List<ColumnDefinition> columns = ColumnDefinitionJsonFactory.arrayToList(array);
			model.setColumns(columns);
		}

		return model;
	}

	public static JSONArray listToArray(java.util.List<TableDefinition> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (TableDefinition model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static JSONObject toJsonObject(TableDefinition model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("tableId", model.getTableId());
		jsonObject.put("tableName", model.getTableName());
		jsonObject.put("_tableName_", model.getTableName());
		jsonObject.put("tableName_enc", RequestUtils.encodeString(model.getTableName()));

		if (model.getNodeId() != null) {
			jsonObject.put("nodeId", model.getNodeId());
		}
		if (model.getPackageName() != null) {
			jsonObject.put("packageName", model.getPackageName());
		}
		if (model.getEntityName() != null) {
			jsonObject.put("entityName", model.getEntityName());
		}
		if (model.getClassName() != null) {
			jsonObject.put("className", model.getClassName());
		}
		if (model.getTitle() != null) {
			jsonObject.put("title", model.getTitle());
		}
		if (model.getEnglishTitle() != null) {
			jsonObject.put("englishTitle", model.getEnglishTitle());
		}
		jsonObject.put("columnQty", model.getColumnQty());
		jsonObject.put("addType", model.getAddType());
		if (model.getSysnum() != null) {
			jsonObject.put("sysnum", model.getSysnum());
		}
		if (model.getIsSubTable() != null) {
			jsonObject.put("isSubTable", model.getIsSubTable());
		}
		if (model.getTopId() != null) {
			jsonObject.put("topId", model.getTopId());
		}
		if (model.getAggregationKey() != null) {
			jsonObject.put("aggregationKey", model.getAggregationKey());
		}
		if (model.getPrimaryKey() != null) {
			jsonObject.put("primaryKey", model.getPrimaryKey());
		}
		if (model.getAttachmentFlag() != null) {
			jsonObject.put("attachmentFlag", model.getAttachmentFlag());
		}
		if (model.getAttachmentExts() != null) {
			jsonObject.put("attachmentExts", model.getAttachmentExts());
		}
		jsonObject.put("attachmentSize", model.getAttachmentSize());
		if (model.getAuditFlag() != null) {
			jsonObject.put("auditFlag", model.getAuditFlag());
		}
		if (model.getPrivilegeFlag() != null) {
			jsonObject.put("privilegeFlag", model.getPrivilegeFlag());
		}
		if (model.getTreeFlag() != null) {
			jsonObject.put("treeFlag", model.getTreeFlag());
		}
		if (model.getTemporaryFlag() != null) {
			jsonObject.put("temporaryFlag", model.getTemporaryFlag());
		}
		if (model.getDeleteFetch() != null) {
			jsonObject.put("deleteFetch", model.getDeleteFetch());
		}
		if (model.getCreateTime() != null) {
			jsonObject.put("createTime", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_date", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_datetime", DateUtils.getDateTime(model.getCreateTime()));
		}
		if (model.getCreateBy() != null) {
			jsonObject.put("createBy", model.getCreateBy());
		}
		if (model.getDescription() != null) {
			jsonObject.put("description", model.getDescription());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		if (model.getDisplayType() != null) {
			jsonObject.put("displayType", model.getDisplayType());
		}
		jsonObject.put("insertCascade", model.getInsertCascade());
		jsonObject.put("updateCascade", model.getUpdateCascade());
		jsonObject.put("deleteCascade", model.getDeleteCascade());
		jsonObject.put("locked", model.getLocked());
		jsonObject.put("deleteFlag", model.getDeleteFlag());
		jsonObject.put("sortNo", model.getSortNo());
		if (model.getSystemFlag() != null) {
			jsonObject.put("systemFlag", model.getSystemFlag());
		}
		jsonObject.put("revision", model.getRevision());

		if (model.getColumns() != null && !model.getColumns().isEmpty()) {
			JSONArray array = new JSONArray();
			for (ColumnDefinition col : model.getColumns()) {
				JSONObject json = ColumnDefinitionJsonFactory.toJsonObject(col);
				array.add(json);
			}
			jsonObject.put("columns", array);
		}

		return jsonObject;
	}

	public static ObjectNode toObjectNode(TableDefinition model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("tableId", model.getTableId());
		jsonObject.put("tableName", model.getTableName());
		jsonObject.put("tableName_enc", RequestUtils.encodeString(model.getTableName()));

		if (model.getNodeId() != null) {
			jsonObject.put("nodeId", model.getNodeId());
		}
		if (model.getPackageName() != null) {
			jsonObject.put("packageName", model.getPackageName());
		}
		if (model.getEntityName() != null) {
			jsonObject.put("entityName", model.getEntityName());
		}
		if (model.getClassName() != null) {
			jsonObject.put("className", model.getClassName());
		}
		if (model.getTitle() != null) {
			jsonObject.put("title", model.getTitle());
		}
		if (model.getEnglishTitle() != null) {
			jsonObject.put("englishTitle", model.getEnglishTitle());
		}
		jsonObject.put("columnQty", model.getColumnQty());
		jsonObject.put("addType", model.getAddType());
		if (model.getSysnum() != null) {
			jsonObject.put("sysnum", model.getSysnum());
		}
		if (model.getIsSubTable() != null) {
			jsonObject.put("isSubTable", model.getIsSubTable());
		}
		if (model.getTopId() != null) {
			jsonObject.put("topId", model.getTopId());
		}
		if (model.getAggregationKey() != null) {
			jsonObject.put("aggregationKey", model.getAggregationKey());
		}
		if (model.getPrimaryKey() != null) {
			jsonObject.put("primaryKey", model.getPrimaryKey());
		}
		if (model.getAttachmentFlag() != null) {
			jsonObject.put("attachmentFlag", model.getAttachmentFlag());
		}
		if (model.getAttachmentExts() != null) {
			jsonObject.put("attachmentExts", model.getAttachmentExts());
		}
		jsonObject.put("attachmentSize", model.getAttachmentSize());

		if (model.getAuditFlag() != null) {
			jsonObject.put("auditFlag", model.getAuditFlag());
		}

		if (model.getPrivilegeFlag() != null) {
			jsonObject.put("privilegeFlag", model.getPrivilegeFlag());
		}
		if (model.getTreeFlag() != null) {
			jsonObject.put("treeFlag", model.getTreeFlag());
		}
		if (model.getTemporaryFlag() != null) {
			jsonObject.put("temporaryFlag", model.getTemporaryFlag());
		}
		if (model.getDeleteFetch() != null) {
			jsonObject.put("deleteFetch", model.getDeleteFetch());
		}
		if (model.getCreateTime() != null) {
			jsonObject.put("createTime", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_date", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_datetime", DateUtils.getDateTime(model.getCreateTime()));
		}
		if (model.getCreateBy() != null) {
			jsonObject.put("createBy", model.getCreateBy());
		}
		if (model.getDescription() != null) {
			jsonObject.put("description", model.getDescription());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		if (model.getDisplayType() != null) {
			jsonObject.put("displayType", model.getDisplayType());
		}
		jsonObject.put("insertCascade", model.getInsertCascade());
		jsonObject.put("updateCascade", model.getUpdateCascade());
		jsonObject.put("deleteCascade", model.getDeleteCascade());
		jsonObject.put("locked", model.getLocked());
		jsonObject.put("deleteFlag", model.getDeleteFlag());
		if (model.getSystemFlag() != null) {
			jsonObject.put("systemFlag", model.getSystemFlag());
		}
		jsonObject.put("revision", model.getRevision());

		if (model.getColumns() != null && !model.getColumns().isEmpty()) {
			ArrayNode array = new ObjectMapper().createArrayNode();
			for (ColumnDefinition col : model.getColumns()) {
				ObjectNode json = ColumnDefinitionJsonFactory.toObjectNode(col);
				array.add(json);
			}
			jsonObject.set("columns", array);
		}

		return jsonObject;
	}

	private TableDefinitionJsonFactory() {

	}

}
