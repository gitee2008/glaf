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
public class SqlCriteriaJsonFactory {

	public static SqlCriteria jsonToObject(JSONObject jsonObject) {
		SqlCriteria model = new SqlCriteria();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getLong("id"));
		}
		if (jsonObject.containsKey("parentId")) {
			model.setParentId(jsonObject.getLong("parentId"));
		}
		if (jsonObject.containsKey("tenantId")) {
			model.setTenantId(jsonObject.getString("tenantId"));
		}
		if (jsonObject.containsKey("name")) {
			model.setName(jsonObject.getString("name"));
		}
		if (jsonObject.containsKey("moduleId")) {
			model.setModuleId(jsonObject.getString("moduleId"));
		}
		if (jsonObject.containsKey("businessKey")) {
			model.setBusinessKey(jsonObject.getString("businessKey"));
		}
		if (jsonObject.containsKey("columnName")) {
			model.setColumnName(jsonObject.getString("columnName"));
		}
		if (jsonObject.containsKey("columnType")) {
			model.setColumnType(jsonObject.getString("columnType"));
		}
		if (jsonObject.containsKey("tableName")) {
			model.setTableName(jsonObject.getString("tableName"));
		}
		if (jsonObject.containsKey("tableAlias")) {
			model.setTableAlias(jsonObject.getString("tableAlias"));
		}
		if (jsonObject.containsKey("paramName")) {
			model.setParamName(jsonObject.getString("paramName"));
		}
		if (jsonObject.containsKey("paramTitle")) {
			model.setParamTitle(jsonObject.getString("paramTitle"));
		}
		if (jsonObject.containsKey("collectionFlag")) {
			model.setCollectionFlag(jsonObject.getString("collectionFlag"));
		}
		if (jsonObject.containsKey("condition")) {
			model.setCondition(jsonObject.getString("condition"));
		}
		if (jsonObject.containsKey("operator")) {
			model.setOperator(jsonObject.getString("operator"));
		}
		if (jsonObject.containsKey("separator")) {
			model.setSeparator(jsonObject.getString("separator"));
		}
		if (jsonObject.containsKey("requiredFlag")) {
			model.setRequiredFlag(jsonObject.getString("requiredFlag"));
		}
		if (jsonObject.containsKey("sql")) {
			model.setSql(jsonObject.getString("sql"));
		}
		if (jsonObject.containsKey("treeId")) {
			model.setTreeId(jsonObject.getString("treeId"));
		}
		if (jsonObject.containsKey("level")) {
			model.setLevel(jsonObject.getInteger("level"));
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

	public static JSONObject toJsonObject(SqlCriteria model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		jsonObject.put("parentId", model.getParentId());
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getModuleId() != null) {
			jsonObject.put("moduleId", model.getModuleId());
		}
		if (model.getBusinessKey() != null) {
			jsonObject.put("businessKey", model.getBusinessKey());
		}
		if (model.getColumnName() != null) {
			jsonObject.put("columnName", model.getColumnName());
		}
		if (model.getColumnType() != null) {
			jsonObject.put("columnType", model.getColumnType());
		}
		if (model.getTableName() != null) {
			jsonObject.put("tableName", model.getTableName());
		}
		if (model.getTableAlias() != null) {
			jsonObject.put("tableAlias", model.getTableAlias());
		}
		if (model.getParamName() != null) {
			jsonObject.put("paramName", model.getParamName());
		}
		if (model.getParamTitle() != null) {
			jsonObject.put("paramTitle", model.getParamTitle());
		}
		if (model.getCollectionFlag() != null) {
			jsonObject.put("collectionFlag", model.getCollectionFlag());
		}
		if (model.getCondition() != null) {
			jsonObject.put("condition", model.getCondition());
		}
		if (model.getOperator() != null) {
			jsonObject.put("operator", model.getOperator());
		}
		if (model.getSeparator() != null) {
			jsonObject.put("separator", model.getSeparator());
		}
		if (model.getRequiredFlag() != null) {
			jsonObject.put("requiredFlag", model.getRequiredFlag());
		}
		if (model.getSql() != null) {
			jsonObject.put("sql", model.getSql());
		}
		if (model.getTreeId() != null) {
			jsonObject.put("treeId", model.getTreeId());
		}
		jsonObject.put("level", model.getLevel());
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

	public static ObjectNode toObjectNode(SqlCriteria model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		jsonObject.put("parentId", model.getParentId());
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getModuleId() != null) {
			jsonObject.put("moduleId", model.getModuleId());
		}
		if (model.getBusinessKey() != null) {
			jsonObject.put("businessKey", model.getBusinessKey());
		}
		if (model.getColumnName() != null) {
			jsonObject.put("columnName", model.getColumnName());
		}
		if (model.getColumnType() != null) {
			jsonObject.put("columnType", model.getColumnType());
		}
		if (model.getTableName() != null) {
			jsonObject.put("tableName", model.getTableName());
		}
		if (model.getTableAlias() != null) {
			jsonObject.put("tableAlias", model.getTableAlias());
		}
		if (model.getParamName() != null) {
			jsonObject.put("paramName", model.getParamName());
		}
		if (model.getParamTitle() != null) {
			jsonObject.put("paramTitle", model.getParamTitle());
		}
		if (model.getCollectionFlag() != null) {
			jsonObject.put("collectionFlag", model.getCollectionFlag());
		}
		if (model.getCondition() != null) {
			jsonObject.put("condition", model.getCondition());
		}
		if (model.getOperator() != null) {
			jsonObject.put("operator", model.getOperator());
		}
		if (model.getSeparator() != null) {
			jsonObject.put("separator", model.getSeparator());
		}
		if (model.getRequiredFlag() != null) {
			jsonObject.put("requiredFlag", model.getRequiredFlag());
		}
		if (model.getSql() != null) {
			jsonObject.put("sql", model.getSql());
		}
		if (model.getTreeId() != null) {
			jsonObject.put("treeId", model.getTreeId());
		}
		jsonObject.put("level", model.getLevel());
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

	public static JSONArray listToArray(java.util.List<SqlCriteria> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (SqlCriteria model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static java.util.List<SqlCriteria> arrayToList(JSONArray array) {
		java.util.List<SqlCriteria> list = new java.util.ArrayList<SqlCriteria>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			SqlCriteria model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	private SqlCriteriaJsonFactory() {

	}

}
