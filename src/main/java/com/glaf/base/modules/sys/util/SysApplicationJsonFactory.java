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

import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.base.modules.sys.model.SysApplication;
import com.glaf.core.util.DateUtils;

/**
 * 
 * JSON工厂类
 *
 */
public class SysApplicationJsonFactory {

	public static java.util.List<SysApplication> arrayToList(JSONArray array) {
		java.util.List<SysApplication> list = new java.util.ArrayList<SysApplication>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			SysApplication model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	public static SysApplication jsonToObject(JSONObject jsonObject) {
		SysApplication model = new SysApplication();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getLong("id"));
		}
		if (jsonObject.containsKey("parentId")) {
			model.setParentId(jsonObject.getLong("parentId"));
		}
		if (jsonObject.containsKey("name")) {
			model.setName(jsonObject.getString("name"));
		}
		if (jsonObject.containsKey("namePinyin")) {
			model.setNamePinyin(jsonObject.getString("namePinyin"));
		}
		if (jsonObject.containsKey("code")) {
			model.setCode(jsonObject.getString("code"));
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
		if (jsonObject.containsKey("level")) {
			model.setLevel(jsonObject.getInteger("level"));
		}
		if (jsonObject.containsKey("desc")) {
			model.setDesc(jsonObject.getString("desc"));
		}
		if (jsonObject.containsKey("locked")) {
			model.setLocked(jsonObject.getInteger("locked"));
		}
		if (jsonObject.containsKey("deleteFlag")) {
			model.setDeleteFlag(jsonObject.getInteger("deleteFlag"));
		}
		if (jsonObject.containsKey("showMenu")) {
			model.setShowMenu(jsonObject.getInteger("showMenu"));
		}
		if (jsonObject.containsKey("showType")) {
			model.setShowType(jsonObject.getString("showType"));
		}
		if (jsonObject.containsKey("sysFlag")) {
			model.setSysFlag(jsonObject.getString("sysFlag"));
		}
		if (jsonObject.containsKey("sort")) {
			model.setSort(jsonObject.getInteger("sort"));
		}
		if (jsonObject.containsKey("url")) {
			model.setUrl(jsonObject.getString("url"));
		}
		if (jsonObject.containsKey("imagePath")) {
			model.setImagePath(jsonObject.getString("imagePath"));
		}
		if (jsonObject.containsKey("linkType")) {
			model.setLinkType(jsonObject.getString("linkType"));
		}
		if (jsonObject.containsKey("createBy")) {
			model.setCreateBy(jsonObject.getString("createBy"));
		}
		if (jsonObject.containsKey("createDate")) {
			model.setCreateDate(jsonObject.getDate("createDate"));
		}
		if (jsonObject.containsKey("updateBy")) {
			model.setUpdateBy(jsonObject.getString("updateBy"));
		}
		if (jsonObject.containsKey("updateDate")) {
			model.setUpdateDate(jsonObject.getDate("updateDate"));
		}

		return model;
	}

	public static JSONArray listToArray(java.util.List<SysApplication> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (SysApplication model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static JSONObject toJsonObject(SysApplication model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		jsonObject.put("parentId", model.getParentId());
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getNamePinyin() != null) {
			jsonObject.put("namePinyin", model.getNamePinyin());
		}
		if (model.getCode() != null) {
			jsonObject.put("code", model.getCode());
		}
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
		jsonObject.put("level", model.getLevel());
		if (model.getDesc() != null) {
			jsonObject.put("desc", model.getDesc());
		}
		jsonObject.put("locked", model.getLocked());
		jsonObject.put("deleteFlag", model.getDeleteFlag());
		jsonObject.put("showMenu", model.getShowMenu());
		if (model.getShowType() != null) {
			jsonObject.put("showType", model.getShowType());
		}
		if (model.getSysFlag() != null) {
			jsonObject.put("sysFlag", model.getSysFlag());
		}
		jsonObject.put("sort", model.getSort());
		if (model.getUrl() != null) {
			jsonObject.put("url", model.getUrl());
		}
		if (model.getImagePath() != null) {
			jsonObject.put("imagePath", model.getImagePath());
		}
		if (model.getLinkType() != null) {
			jsonObject.put("linkType", model.getLinkType());
		}
		if (model.getCreateBy() != null) {
			jsonObject.put("createBy", model.getCreateBy());
		}
		if (model.getCreateDate() != null) {
			jsonObject.put("createDate", DateUtils.getDate(model.getCreateDate()));
			jsonObject.put("createDate_date", DateUtils.getDate(model.getCreateDate()));
			jsonObject.put("createDate_datetime", DateUtils.getDateTime(model.getCreateDate()));
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

	public static ObjectNode toObjectNode(SysApplication model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		jsonObject.put("parentId", model.getParentId());
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getNamePinyin() != null) {
			jsonObject.put("namePinyin", model.getNamePinyin());
		}
		if (model.getCode() != null) {
			jsonObject.put("code", model.getCode());
		}
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
		jsonObject.put("level", model.getLevel());
		if (model.getDesc() != null) {
			jsonObject.put("desc", model.getDesc());
		}
		jsonObject.put("locked", model.getLocked());
		jsonObject.put("deleteFlag", model.getDeleteFlag());
		jsonObject.put("showMenu", model.getShowMenu());
		if (model.getShowType() != null) {
			jsonObject.put("showType", model.getShowType());
		}
		if (model.getSysFlag() != null) {
			jsonObject.put("sysFlag", model.getSysFlag());
		}
		jsonObject.put("sort", model.getSort());
		if (model.getUrl() != null) {
			jsonObject.put("url", model.getUrl());
		}
		if (model.getImagePath() != null) {
			jsonObject.put("imagePath", model.getImagePath());
		}
		if (model.getLinkType() != null) {
			jsonObject.put("linkType", model.getLinkType());
		}
		if (model.getCreateBy() != null) {
			jsonObject.put("createBy", model.getCreateBy());
		}
		if (model.getCreateDate() != null) {
			jsonObject.put("createDate", DateUtils.getDate(model.getCreateDate()));
			jsonObject.put("createDate_date", DateUtils.getDate(model.getCreateDate()));
			jsonObject.put("createDate_datetime", DateUtils.getDateTime(model.getCreateDate()));
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

	private SysApplicationJsonFactory() {

	}

}
