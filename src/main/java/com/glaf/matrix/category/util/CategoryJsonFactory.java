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

package com.glaf.matrix.category.util;

import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.glaf.core.util.DateUtils;
import com.glaf.matrix.category.domain.*;

/**
 * 
 * JSON工厂类
 *
 */
public class CategoryJsonFactory {

	public static java.util.List<Category> arrayToList(JSONArray array) {
		java.util.List<Category> list = new java.util.ArrayList<Category>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			Category model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	public static Category jsonToObject(JSONObject jsonObject) {
		Category model = new Category();
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
		if (jsonObject.containsKey("discriminator")) {
			model.setDiscriminator(jsonObject.getString("discriminator"));
		}
		if (jsonObject.containsKey("icon")) {
			model.setIcon(jsonObject.getString("icon"));
		}
		if (jsonObject.containsKey("iconCls")) {
			model.setIconCls(jsonObject.getString("iconCls"));
		}
		if (jsonObject.containsKey("level")) {
			model.setLevel(jsonObject.getInteger("level"));
		}
		if (jsonObject.containsKey("treeId")) {
			model.setTreeId(jsonObject.getString("treeId"));
		}
		if (jsonObject.containsKey("title")) {
			model.setTitle(jsonObject.getString("title"));
		}
		if (jsonObject.containsKey("type")) {
			model.setType(jsonObject.getString("type"));
		}
		if (jsonObject.containsKey("sort")) {
			model.setSort(jsonObject.getInteger("sort"));
		}
		if (jsonObject.containsKey("locked")) {
			model.setLocked(jsonObject.getInteger("locked"));
		}
		if (jsonObject.containsKey("subIds")) {
			model.setSubIds(jsonObject.getString("subIds"));
		}
		if (jsonObject.containsKey("url")) {
			model.setUrl(jsonObject.getString("url"));
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
		if (jsonObject.containsKey("accessors")) {
			StringTokenizer token = new StringTokenizer(jsonObject.getString("accessors"), ",");
			while (token.hasMoreTokens()) {
				String str = token.nextToken();
				if (StringUtils.isNotEmpty(str)) {
					CategoryAccess access = new CategoryAccess();
					access.setActorId(str);
					access.setCategoryId(model.getId());
					model.addAccessor(str);
				}
			}
		}

		if (jsonObject.containsKey("owners")) {
			StringTokenizer token = new StringTokenizer(jsonObject.getString("owners"), ",");
			while (token.hasMoreTokens()) {
				String str = token.nextToken();
				if (StringUtils.isNotEmpty(str)) {
					CategoryOwner owner = new CategoryOwner();
					owner.setActorId(str);
					owner.setCategoryId(model.getId());
					model.addOwner(owner);
				}
			}
		}

		if (jsonObject.containsKey("subordinateIds")) {
			StringTokenizer token = new StringTokenizer(jsonObject.getString("subordinateIds"), ",");
			while (token.hasMoreTokens()) {
				String str = token.nextToken();
				if (StringUtils.isNotEmpty(str)) {
					CategorySubordinate sub = new CategorySubordinate();
					sub.setCategoryId(model.getId());
					sub.setSubordinateId(Long.parseLong(str));
					model.addSubordinate(Long.parseLong(str));
				}
			}
		}

		return model;
	}

	public static JSONArray listToArray(java.util.List<Category> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (Category model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static JSONObject toJsonObject(Category model) {
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
		if (model.getDiscriminator() != null) {
			jsonObject.put("discriminator", model.getDiscriminator());
		}
		if (model.getIcon() != null) {
			jsonObject.put("icon", model.getIcon());
		}
		if (model.getIconCls() != null) {
			jsonObject.put("iconCls", model.getIconCls());
		}
		jsonObject.put("level", model.getLevel());
		if (model.getTreeId() != null) {
			jsonObject.put("treeId", model.getTreeId());
		}
		if (model.getTitle() != null) {
			jsonObject.put("title", model.getTitle());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		jsonObject.put("sort", model.getSort());
		jsonObject.put("locked", model.getLocked());
		if (model.getSubIds() != null) {
			jsonObject.put("subIds", model.getSubIds());
		}
		if (model.getUrl() != null) {
			jsonObject.put("url", model.getUrl());
		}

		if (model.getActorIds() != null && !model.getActorIds().isEmpty()) {
			StringBuilder buff = new StringBuilder();
			for (String actorId : model.getActorIds()) {
				buff.append(actorId).append(",");
			}
			jsonObject.put("accessors", buff.toString());
		}

		if (model.getOwners() != null && !model.getOwners().isEmpty()) {
			StringBuilder buff = new StringBuilder();
			for (CategoryOwner owner : model.getOwners()) {
				buff.append(owner.getActorId()).append(",");
			}
			jsonObject.put("owners", buff.toString());
		}

		if (model.getSubordinateIds() != null && !model.getSubordinateIds().isEmpty()) {
			StringBuilder buff = new StringBuilder();
			for (Long subordinateId : model.getSubordinateIds()) {
				buff.append(subordinateId).append(",");
			}
			jsonObject.put("subordinateIds", buff.toString());
		}

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

	public static ObjectNode toObjectNode(Category model) {
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
		if (model.getDiscriminator() != null) {
			jsonObject.put("discriminator", model.getDiscriminator());
		}
		if (model.getIcon() != null) {
			jsonObject.put("icon", model.getIcon());
		}
		if (model.getIconCls() != null) {
			jsonObject.put("iconCls", model.getIconCls());
		}
		jsonObject.put("level", model.getLevel());
		if (model.getTreeId() != null) {
			jsonObject.put("treeId", model.getTreeId());
		}
		if (model.getTitle() != null) {
			jsonObject.put("title", model.getTitle());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		jsonObject.put("sort", model.getSort());
		jsonObject.put("locked", model.getLocked());
		if (model.getSubIds() != null) {
			jsonObject.put("subIds", model.getSubIds());
		}
		if (model.getUrl() != null) {
			jsonObject.put("url", model.getUrl());
		}
		if (model.getActorIds() != null && !model.getActorIds().isEmpty()) {
			StringBuilder buff = new StringBuilder();
			for (String actorId : model.getActorIds()) {
				buff.append(actorId).append(",");
			}
			jsonObject.put("accessors", buff.toString());
		}

		if (model.getOwners() != null && !model.getOwners().isEmpty()) {
			StringBuilder buff = new StringBuilder();
			for (CategoryOwner owner : model.getOwners()) {
				buff.append(owner.getActorId()).append(",");
			}
			jsonObject.put("owners", buff.toString());
		}

		if (model.getSubordinateIds() != null && !model.getSubordinateIds().isEmpty()) {
			StringBuilder buff = new StringBuilder();
			for (Long subordinateId : model.getSubordinateIds()) {
				buff.append(subordinateId).append(",");
			}
			jsonObject.put("subordinateIds", buff.toString());
		}

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

	private CategoryJsonFactory() {

	}

}
