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
import com.glaf.matrix.data.domain.TableColumn;

public class TableColumnJsonFactory {

	public static java.util.List<TableColumn> arrayToList(JSONArray array) {
		java.util.List<TableColumn> list = new java.util.ArrayList<TableColumn>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			TableColumn model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	public static TableColumn jsonToObject(JSONObject jsonObject) {
		TableColumn model = new TableColumn();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getString("id"));
		}
		if (jsonObject.containsKey("tableName")) {
			model.setTableName(jsonObject.getString("tableName"));
		}
		if (jsonObject.containsKey("targetId")) {
			model.setTargetId(jsonObject.getString("targetId"));
		}
		if (jsonObject.containsKey("columnName")) {
			model.setColumnName(jsonObject.getString("columnName"));
		}
		if (jsonObject.containsKey("columnLabel")) {
			model.setColumnLabel(jsonObject.getString("columnLabel"));
		}
		if (jsonObject.containsKey("comment")) {
			model.setComment(jsonObject.getString("comment"));
		}
		if (jsonObject.containsKey("name")) {
			model.setName(jsonObject.getString("name"));
		}
		if (jsonObject.containsKey("title")) {
			model.setTitle(jsonObject.getString("title"));
		}
		if (jsonObject.containsKey("englishTitle")) {
			model.setEnglishTitle(jsonObject.getString("englishTitle"));
		}
		if (jsonObject.containsKey("length")) {
			model.setLength(jsonObject.getInteger("length"));
		}
		if (jsonObject.containsKey("listWidth")) {
			model.setListWidth(jsonObject.getInteger("listWidth"));
		}
		if (jsonObject.containsKey("scale")) {
			model.setScale(jsonObject.getInteger("scale"));
		}
		if (jsonObject.containsKey("precision")) {
			model.setPrecision(jsonObject.getInteger("precision"));
		}
		if (jsonObject.containsKey("position")) {
			model.setPosition(jsonObject.getInteger("position"));
		}
		if (jsonObject.containsKey("primaryKey")) {
			model.setPrimaryKeyField(jsonObject.getString("primaryKey"));
		}
		if (jsonObject.containsKey("nullable")) {
			model.setNullableField(jsonObject.getString("nullable"));
		}
		if (jsonObject.containsKey("frozen")) {
			model.setFrozenField(jsonObject.getString("frozen"));
		}
		if (jsonObject.containsKey("unique")) {
			model.setUniqueField(jsonObject.getString("unique"));
		}
		if (jsonObject.containsKey("searchable")) {
			model.setSearchableField(jsonObject.getString("searchable"));
		}
		if (jsonObject.containsKey("editable")) {
			model.setEditableField(jsonObject.getString("editable"));
		}
		if (jsonObject.containsKey("updatable")) {
			model.setUpdatableField(jsonObject.getString("updatable"));
		}
		if (jsonObject.containsKey("resizable")) {
			model.setResizableField(jsonObject.getString("resizable"));
		}
		if (jsonObject.containsKey("hidden")) {
			model.setHiddenField(jsonObject.getString("hidden"));
		}
		if (jsonObject.containsKey("tooltip")) {
			model.setTooltip(jsonObject.getString("tooltip"));
		}
		if (jsonObject.containsKey("ordinal")) {
			model.setOrdinal(jsonObject.getInteger("ordinal"));
		}
		if (jsonObject.containsKey("javaType")) {
			model.setJavaType(jsonObject.getString("javaType"));
		}
		if (jsonObject.containsKey("inputType")) {
			model.setInputType(jsonObject.getString("inputType"));
		}
		if (jsonObject.containsKey("valueField")) {
			model.setValueField(jsonObject.getString("valueField"));
		}
		if (jsonObject.containsKey("textField")) {
			model.setTextField(jsonObject.getString("textField"));
		}
		if (jsonObject.containsKey("validType")) {
			model.setValidType(jsonObject.getString("validType"));
		}
		if (jsonObject.containsKey("required")) {
			model.setRequiredField(jsonObject.getString("required"));
		}
		if (jsonObject.containsKey("regex")) {
			model.setRegex(jsonObject.getString("regex"));
		}
		if (jsonObject.containsKey("defaultValue")) {
			model.setDefaultValue(jsonObject.getString("defaultValue"));
		}
		if (jsonObject.containsKey("discriminator")) {
			model.setDiscriminator(jsonObject.getString("discriminator"));
		}
		if (jsonObject.containsKey("formula")) {
			model.setFormula(jsonObject.getString("formula"));
		}
		if (jsonObject.containsKey("mask")) {
			model.setMask(jsonObject.getString("mask"));
		}
		if (jsonObject.containsKey("dataCode")) {
			model.setDataCode(jsonObject.getString("dataCode"));
		}
		if (jsonObject.containsKey("renderType")) {
			model.setRenderType(jsonObject.getString("renderType"));
		}
		if (jsonObject.containsKey("summaryType")) {
			model.setSummaryType(jsonObject.getString("summaryType"));
		}
		if (jsonObject.containsKey("summaryExpr")) {
			model.setSummaryExpr(jsonObject.getString("summaryExpr"));
		}
		if (jsonObject.containsKey("displayType")) {
			model.setDisplayType(jsonObject.getInteger("displayType"));
		}
		if (jsonObject.containsKey("sortable")) {
			model.setSortableField(jsonObject.getString("sortable"));
		}
		if (jsonObject.containsKey("exportFlag")) {
			model.setExportFlag(jsonObject.getString("exportFlag"));
		}
		if (jsonObject.containsKey("systemFlag")) {
			model.setSystemFlag(jsonObject.getString("systemFlag"));
		}
		if (jsonObject.containsKey("formatter")) {
			model.setFormatter(jsonObject.getString("formatter"));
		}
		if (jsonObject.containsKey("height")) {
			model.setHeight(jsonObject.getString("height"));
		}
		if (jsonObject.containsKey("width")) {
			model.setWidth(jsonObject.getString("width"));
		}
		if (jsonObject.containsKey("link")) {
			model.setLink(jsonObject.getString("link"));
		}
		if (jsonObject.containsKey("valueExpression")) {
			model.setValueExpression(jsonObject.getString("valueExpression"));
		}
		if (jsonObject.containsKey("renderer")) {
			model.setRenderer(jsonObject.getString("renderer"));
		}
		if (jsonObject.containsKey("locked")) {
			model.setLocked(jsonObject.getInteger("locked"));
		}

		return model;
	}

	public static JSONArray listToArray(java.util.List<TableColumn> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (TableColumn model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static JSONObject toJsonObject(TableColumn model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());

		if (model.getTableName() != null) {
			jsonObject.put("tableName", model.getTableName());
		}
		if (model.getTargetId() != null) {
			jsonObject.put("targetId", model.getTargetId());
		}
		if (model.getColumnName() != null) {
			jsonObject.put("columnName", model.getColumnName());
		}
		if (model.getColumnLabel() != null) {
			jsonObject.put("columnLabel", model.getColumnLabel());
		}
		if (model.getComment() != null) {
			jsonObject.put("comment", model.getComment());
		}
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getTitle() != null) {
			jsonObject.put("title", model.getTitle());
		}
		if (model.getEnglishTitle() != null) {
			jsonObject.put("englishTitle", model.getEnglishTitle());
		}
		jsonObject.put("length", model.getLength());
		jsonObject.put("listWidth", model.getListWidth());
		jsonObject.put("scale", model.getScale());
		jsonObject.put("precision", model.getPrecision());
		jsonObject.put("position", model.getPosition());
		if (model.getPrimaryKeyField() != null) {
			jsonObject.put("primaryKey", model.getPrimaryKeyField());
		}
		if (model.getNullableField() != null) {
			jsonObject.put("nullable", model.getNullableField());
		}
		if (model.getFrozenField() != null) {
			jsonObject.put("frozen", model.getFrozenField());
		}
		if (model.getUniqueField() != null) {
			jsonObject.put("unique", model.getUniqueField());
		}
		if (model.getSearchableField() != null) {
			jsonObject.put("searchable", model.getSearchableField());
		}
		if (model.getEditableField() != null) {
			jsonObject.put("editable", model.getEditableField());
		}
		if (model.getUpdatableField() != null) {
			jsonObject.put("updatable", model.getUpdatableField());
		}
		if (model.getResizableField() != null) {
			jsonObject.put("resizable", model.getResizableField());
		}
		if (model.getHiddenField() != null) {
			jsonObject.put("hidden", model.getHiddenField());
		}
		if (model.getTooltip() != null) {
			jsonObject.put("tooltip", model.getTooltip());
		}
		jsonObject.put("ordinal", model.getOrdinal());
		if (model.getJavaType() != null) {
			jsonObject.put("javaType", model.getJavaType());
			if ("Integer".equals(model.getJavaType())) {
				jsonObject.put("dataTypeName", "整数");
			} else if ("Long".equals(model.getJavaType())) {
				jsonObject.put("dataTypeName", "长整数");
			} else if ("Double".equals(model.getJavaType())) {
				jsonObject.put("dataTypeName", "数值");
			} else if ("Date".equals(model.getJavaType())) {
				jsonObject.put("dataTypeName", "日期时间");
			} else {
				jsonObject.put("dataTypeName", "字符串");
			}
		}
		if (model.getInputType() != null) {
			jsonObject.put("inputType", model.getInputType());
		}
		if (model.getValueField() != null) {
			jsonObject.put("valueField", model.getValueField());
		}
		if (model.getTextField() != null) {
			jsonObject.put("textField", model.getTextField());
		}
		if (model.getValidType() != null) {
			jsonObject.put("validType", model.getValidType());
		}
		if (model.getRequiredField() != null) {
			jsonObject.put("required", model.getRequiredField());
		}
		if (model.getRegex() != null) {
			jsonObject.put("regex", model.getRegex());
		}
		if (model.getDefaultValue() != null) {
			jsonObject.put("defaultValue", model.getDefaultValue());
		}
		if (model.getDiscriminator() != null) {
			jsonObject.put("discriminator", model.getDiscriminator());
		}
		if (model.getFormula() != null) {
			jsonObject.put("formula", model.getFormula());
		}
		if (model.getMask() != null) {
			jsonObject.put("mask", model.getMask());
		}
		if (model.getDataCode() != null) {
			jsonObject.put("dataCode", model.getDataCode());
		}
		if (model.getRenderType() != null) {
			jsonObject.put("renderType", model.getRenderType());
		}
		if (model.getSummaryType() != null) {
			jsonObject.put("summaryType", model.getSummaryType());
		}
		if (model.getSummaryExpr() != null) {
			jsonObject.put("summaryExpr", model.getSummaryExpr());
		}
		jsonObject.put("displayType", model.getDisplayType());
		if (model.getSortableField() != null) {
			jsonObject.put("sortable", model.getSortableField());
		}
		if (model.getExportFlag() != null) {
			jsonObject.put("exportFlag", model.getExportFlag());
		}
		if (model.getSystemFlag() != null) {
			jsonObject.put("systemFlag", model.getSystemFlag());
		}
		if (model.getFormatter() != null) {
			jsonObject.put("formatter", model.getFormatter());
		}
		if (model.getHeight() != null) {
			jsonObject.put("height", model.getHeight());
		}
		if (model.getWidth() != null) {
			jsonObject.put("width", model.getWidth());
		}
		if (model.getLink() != null) {
			jsonObject.put("link", model.getLink());
		}
		if (model.getValueExpression() != null) {
			jsonObject.put("valueExpression", model.getValueExpression());
		}
		if (model.getRenderer() != null) {
			jsonObject.put("renderer", model.getRenderer());
		}
		jsonObject.put("locked", model.getLocked());

		return jsonObject;
	}

	public static ObjectNode toObjectNode(TableColumn model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());

		if (model.getTableName() != null) {
			jsonObject.put("tableName", model.getTableName());
		}
		if (model.getTargetId() != null) {
			jsonObject.put("targetId", model.getTargetId());
		}
		if (model.getColumnName() != null) {
			jsonObject.put("columnName", model.getColumnName());
		}
		if (model.getColumnLabel() != null) {
			jsonObject.put("columnLabel", model.getColumnLabel());
		}
		if (model.getComment() != null) {
			jsonObject.put("comment", model.getComment());
		}
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getTitle() != null) {
			jsonObject.put("title", model.getTitle());
		}
		if (model.getEnglishTitle() != null) {
			jsonObject.put("englishTitle", model.getEnglishTitle());
		}
		jsonObject.put("length", model.getLength());
		jsonObject.put("listWidth", model.getListWidth());
		jsonObject.put("scale", model.getScale());
		jsonObject.put("precision", model.getPrecision());
		jsonObject.put("position", model.getPosition());

		if (model.getPrimaryKeyField() != null) {
			jsonObject.put("primaryKey", model.getPrimaryKeyField());
		}
		if (model.getNullableField() != null) {
			jsonObject.put("nullable", model.getNullableField());
		}
		if (model.getFrozenField() != null) {
			jsonObject.put("frozen", model.getFrozenField());
		}
		if (model.getUniqueField() != null) {
			jsonObject.put("unique", model.getUniqueField());
		}
		if (model.getSearchableField() != null) {
			jsonObject.put("searchable", model.getSearchableField());
		}
		if (model.getEditableField() != null) {
			jsonObject.put("editable", model.getEditableField());
		}
		if (model.getUpdatableField() != null) {
			jsonObject.put("updatable", model.getUpdatableField());
		}
		if (model.getResizableField() != null) {
			jsonObject.put("resizable", model.getResizableField());
		}
		if (model.getHiddenField() != null) {
			jsonObject.put("hidden", model.getHiddenField());
		}
		if (model.getTooltip() != null) {
			jsonObject.put("tooltip", model.getTooltip());
		}
		jsonObject.put("ordinal", model.getOrdinal());
		if (model.getJavaType() != null) {
			jsonObject.put("javaType", model.getJavaType());
			if ("Integer".equals(model.getJavaType())) {
				jsonObject.put("dataTypeName", "整数");
			} else if ("Long".equals(model.getJavaType())) {
				jsonObject.put("dataTypeName", "长整数");
			} else if ("Double".equals(model.getJavaType())) {
				jsonObject.put("dataTypeName", "数值");
			} else if ("Date".equals(model.getJavaType())) {
				jsonObject.put("dataTypeName", "日期时间");
			} else {
				jsonObject.put("dataTypeName", "字符串");
			}
		}
		if (model.getInputType() != null) {
			jsonObject.put("inputType", model.getInputType());
		}
		if (model.getValueField() != null) {
			jsonObject.put("valueField", model.getValueField());
		}
		if (model.getTextField() != null) {
			jsonObject.put("textField", model.getTextField());
		}
		if (model.getValidType() != null) {
			jsonObject.put("validType", model.getValidType());
		}
		if (model.getRequiredField() != null) {
			jsonObject.put("required", model.getRequiredField());
		}
		if (model.getRegex() != null) {
			jsonObject.put("regex", model.getRegex());
		}

		if (model.getDefaultValue() != null) {
			jsonObject.put("defaultValue", model.getDefaultValue());
		}
		if (model.getDiscriminator() != null) {
			jsonObject.put("discriminator", model.getDiscriminator());
		}
		if (model.getFormula() != null) {
			jsonObject.put("formula", model.getFormula());
		}
		if (model.getMask() != null) {
			jsonObject.put("mask", model.getMask());
		}
		if (model.getDataCode() != null) {
			jsonObject.put("dataCode", model.getDataCode());
		}
		if (model.getRenderType() != null) {
			jsonObject.put("renderType", model.getRenderType());
		}
		if (model.getSummaryType() != null) {
			jsonObject.put("summaryType", model.getSummaryType());
		}
		if (model.getSummaryExpr() != null) {
			jsonObject.put("summaryExpr", model.getSummaryExpr());
		}
		jsonObject.put("displayType", model.getDisplayType());
		if (model.getSortableField() != null) {
			jsonObject.put("sortable", model.getSortableField());
		}
		if (model.getExportFlag() != null) {
			jsonObject.put("exportFlag", model.getExportFlag());
		}
		if (model.getSystemFlag() != null) {
			jsonObject.put("systemFlag", model.getSystemFlag());
		}
		if (model.getFormatter() != null) {
			jsonObject.put("formatter", model.getFormatter());
		}
		if (model.getHeight() != null) {
			jsonObject.put("height", model.getHeight());
		}
		if (model.getWidth() != null) {
			jsonObject.put("width", model.getWidth());
		}
		if (model.getLink() != null) {
			jsonObject.put("link", model.getLink());
		}
		if (model.getValueExpression() != null) {
			jsonObject.put("valueExpression", model.getValueExpression());
		}
		if (model.getRenderer() != null) {
			jsonObject.put("renderer", model.getRenderer());
		}
		jsonObject.put("locked", model.getLocked());
		return jsonObject;
	}

	private TableColumnJsonFactory() {

	}

}
