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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.core.base.BaseTree;
import com.glaf.core.base.TreeModel;
import com.glaf.core.util.DateUtils;

public class TreeModelJsonFactory {

	public static java.util.List<TreeModel> arrayToList(JSONArray array) {
		java.util.List<TreeModel> list = new java.util.ArrayList<TreeModel>();
		for (int i = 0; i < array.size(); i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			TreeModel model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	public static TreeModel jsonToObject(JSONObject jsonObject) {
		TreeModel model = new BaseTree();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getLong("id"));
		}
		if (jsonObject.containsKey("parentId")) {
			model.setParentId(jsonObject.getLong("parentId"));
		}
		if (jsonObject.containsKey("name")) {
			model.setName(jsonObject.getString("name"));
		}
		if (jsonObject.containsKey("code")) {
			model.setCode(jsonObject.getString("code"));
		}
		if (jsonObject.containsKey("description")) {
			model.setDescription(jsonObject.getString("description"));
		}
		if (jsonObject.containsKey("sortNo")) {
			model.setSortNo(jsonObject.getInteger("sortNo"));
		}
		if (jsonObject.containsKey("discriminator")) {
			model.setDiscriminator(jsonObject.getString("discriminator"));
		}
		if (jsonObject.containsKey("icon")) {
			model.setIcon(jsonObject.getString("icon"));
		}
		if (jsonObject.containsKey("iconCls")) {
			model.setIconCls(jsonObject.getString("iconCls"));
		}
		if (jsonObject.containsKey("treeId")) {
			model.setTreeId(jsonObject.getString("treeId"));
		}
		if (jsonObject.containsKey("url")) {
			model.setUrl(jsonObject.getString("url"));
		}
		if (jsonObject.containsKey("level")) {
			model.setLevel(jsonObject.getInteger("level"));
		}
		if (jsonObject.containsKey("locked")) {
			model.setLocked(jsonObject.getInteger("locked"));
		}

		return model;
	}

	public static JSONArray listToArray(java.util.List<TreeModel> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (TreeModel model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static JSONObject toJsonObject(TreeModel model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		jsonObject.put("parentId", model.getParentId());
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getCode() != null) {
			jsonObject.put("code", model.getCode());
		}
		if (model.getDescription() != null) {
			jsonObject.put("description", model.getDescription());
		}
		jsonObject.put("sortNo", model.getSortNo());

		if (model.getDiscriminator() != null) {
			jsonObject.put("discriminator", model.getDiscriminator());
		}
		if (model.getIcon() != null) {
			jsonObject.put("icon", model.getIcon());
		}
		if (model.getIconCls() != null) {
			jsonObject.put("iconCls", model.getIconCls());
		}
		if (model.getTreeId() != null) {
			jsonObject.put("treeId", model.getTreeId());
		}
		if (model.getUrl() != null) {
			jsonObject.put("url", model.getUrl());
		}
		jsonObject.put("level", model.getLevel());
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

	public static ObjectNode toObjectNode(TreeModel model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		jsonObject.put("parentId", model.getParentId());
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getCode() != null) {
			jsonObject.put("code", model.getCode());
		}
		if (model.getDescription() != null) {
			jsonObject.put("description", model.getDescription());
		}
		jsonObject.put("level", model.getLevel());
		jsonObject.put("sortNo", model.getSortNo());

		if (model.getDiscriminator() != null) {
			jsonObject.put("discriminator", model.getDiscriminator());
		}
		if (model.getIcon() != null) {
			jsonObject.put("icon", model.getIcon());
		}
		if (model.getIconCls() != null) {
			jsonObject.put("iconCls", model.getIconCls());
		}
		if (model.getTreeId() != null) {
			jsonObject.put("treeId", model.getTreeId());
		}
		if (model.getUrl() != null) {
			jsonObject.put("url", model.getUrl());
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

	private TreeModelJsonFactory() {

	}

}
