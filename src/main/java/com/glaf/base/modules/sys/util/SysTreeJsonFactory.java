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

package com.glaf.base.modules.sys.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.base.modules.sys.model.SysTree;
import com.glaf.core.util.DateUtils;

public class SysTreeJsonFactory {

	public static java.util.List<SysTree> arrayToList(JSONArray array) {
		java.util.List<SysTree> list = new java.util.ArrayList<SysTree>();
		for (int i = 0; i < array.size(); i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			SysTree model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	public static SysTree jsonToObject(JSONObject jsonObject) {
		SysTree model = new SysTree();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getLong("id"));
		}
		if (jsonObject.containsKey("parentId")) {
			model.setParentId(jsonObject.getLong("parentId"));
		}
		if (jsonObject.containsKey("name")) {
			model.setName(jsonObject.getString("name"));
		}
		if (jsonObject.containsKey("desc")) {
			model.setDesc(jsonObject.getString("desc"));
		}
		if (jsonObject.containsKey("sort")) {
			model.setSort(jsonObject.getInteger("sort"));
		}
		if (jsonObject.containsKey("code")) {
			model.setCode(jsonObject.getString("code"));
		}
		if (jsonObject.containsKey("discriminator")) {
			model.setDiscriminator(jsonObject.getString("discriminator"));
		}
		if (jsonObject.containsKey("level")) {
			model.setLevel(jsonObject.getInteger("level"));
		}
		if (jsonObject.containsKey("cacheFlag")) {
			model.setCacheFlag(jsonObject.getString("cacheFlag"));
		}
		if (jsonObject.containsKey("moveable")) {
			model.setMoveable(jsonObject.getString("moveable"));
		}
		if (jsonObject.containsKey("treeId")) {
			model.setTreeId(jsonObject.getString("treeId"));
		}
		if (jsonObject.containsKey("allowedFizeSize")) {
			model.setAllowedFizeSize(jsonObject.getInteger("allowedFizeSize"));
		}
		if (jsonObject.containsKey("allowedFileExts")) {
			model.setAllowedFileExts(jsonObject.getString("allowedFileExts"));
		}
		if (jsonObject.containsKey("providerClass")) {
			model.setProviderClass(jsonObject.getString("providerClass"));
		}
		if (jsonObject.containsKey("value")) {
			model.setValue(jsonObject.getString("value"));
		}
		if (jsonObject.containsKey("locked")) {
			model.setLocked(jsonObject.getInteger("locked"));
		}

		return model;
	}

	public static JSONArray listToArray(java.util.List<SysTree> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (SysTree model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static JSONObject toJsonObject(SysTree model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		jsonObject.put("parentId", model.getParentId());
		jsonObject.put("level", model.getLevel());

		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getDesc() != null) {
			jsonObject.put("desc", model.getDesc());
		}
		jsonObject.put("sort", model.getSort());
		if (model.getCode() != null) {
			jsonObject.put("code", model.getCode());
		}
		if (model.getDiscriminator() != null) {
			jsonObject.put("discriminator", model.getDiscriminator());
		}
		if (model.getCacheFlag() != null) {
			jsonObject.put("cacheFlag", model.getCacheFlag());
		}
		if (model.getMoveable() != null) {
			jsonObject.put("moveable", model.getMoveable());
		}
		if (model.getTreeId() != null) {
			jsonObject.put("treeId", model.getTreeId());
		}

		jsonObject.put("allowedFizeSize", model.getAllowedFizeSize());
		if (model.getAllowedFileExts() != null) {
			jsonObject.put("allowedFileExts", model.getAllowedFileExts());
		}
		if (model.getProviderClass() != null) {
			jsonObject.put("providerClass", model.getProviderClass());
		}
		if (model.getValue() != null) {
			jsonObject.put("value", model.getValue());
		}
		jsonObject.put("locked", model.getLocked());

		if (model.getCreateDate() != null) {
			jsonObject.put("createDate", DateUtils.getDate(model.getCreateDate()));
			jsonObject.put("createDate_date", DateUtils.getDate(model.getCreateDate()));
			jsonObject.put("createDate_datetime", DateUtils.getDateTime(model.getCreateDate()));
		}
		if (model.getCreateBy() != null) {
			jsonObject.put("createBy", model.getCreateBy());
		}
		if (model.getUpdateBy() != null) {
			jsonObject.put("updateBy", model.getUpdateBy());
		}
		if (model.getUpdateDate() != null) {
			jsonObject.put("updateDate", DateUtils.getDate(model.getUpdateDate()));
			jsonObject.put("updateDate_date", DateUtils.getDate(model.getUpdateDate()));
			jsonObject.put("updateDate_datetime", DateUtils.getDateTime(model.getUpdateDate()));
		}

		return jsonObject;
	}

	public static ObjectNode toObjectNode(SysTree model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		jsonObject.put("parentId", model.getParentId());
		jsonObject.put("level", model.getLevel());

		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getDesc() != null) {
			jsonObject.put("desc", model.getDesc());
		}
		jsonObject.put("sort", model.getSort());
		if (model.getCode() != null) {
			jsonObject.put("code", model.getCode());
		}
		if (model.getDiscriminator() != null) {
			jsonObject.put("discriminator", model.getDiscriminator());
		}
		if (model.getTreeId() != null) {
			jsonObject.put("treeId", model.getTreeId());
		}

		jsonObject.put("locked", model.getLocked());

		jsonObject.put("allowedFizeSize", model.getAllowedFizeSize());
		if (model.getAllowedFileExts() != null) {
			jsonObject.put("allowedFileExts", model.getAllowedFileExts());
		}
		if (model.getProviderClass() != null) {
			jsonObject.put("providerClass", model.getProviderClass());
		}
		if (model.getValue() != null) {
			jsonObject.put("value", model.getValue());
		}
		if (model.getCreateDate() != null) {
			jsonObject.put("createDate", DateUtils.getDate(model.getCreateDate()));
			jsonObject.put("createDate_date", DateUtils.getDate(model.getCreateDate()));
			jsonObject.put("createDate_datetime", DateUtils.getDateTime(model.getCreateDate()));
		}
		if (model.getCreateBy() != null) {
			jsonObject.put("createBy", model.getCreateBy());
		}
		if (model.getUpdateBy() != null) {
			jsonObject.put("updateBy", model.getUpdateBy());
		}
		if (model.getUpdateDate() != null) {
			jsonObject.put("updateDate", DateUtils.getDate(model.getUpdateDate()));
			jsonObject.put("updateDate_date", DateUtils.getDate(model.getUpdateDate()));
			jsonObject.put("updateDate_datetime", DateUtils.getDateTime(model.getUpdateDate()));
		}

		return jsonObject;
	}

	private SysTreeJsonFactory() {

	}

}
