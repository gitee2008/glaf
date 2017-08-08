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
package com.glaf.matrix.resource.util;

import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.core.util.DateUtils;
import com.glaf.matrix.resource.domain.*;

/**
 * 
 * JSON工厂类
 *
 */
public class PageResourceJsonFactory {

	public static java.util.List<PageResource> arrayToList(JSONArray array) {
		java.util.List<PageResource> list = new java.util.ArrayList<PageResource>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			PageResource model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	public static PageResource jsonToObject(JSONObject jsonObject) {
		PageResource model = new PageResource();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getLong("id"));
		}

		if (jsonObject.containsKey("resFileId")) {
			model.setResFileId(jsonObject.getString("resFileId"));
		}
		if (jsonObject.containsKey("resFileName")) {
			model.setResFileName(jsonObject.getString("resFileName"));
		}
		if (jsonObject.containsKey("resName")) {
			model.setResName(jsonObject.getString("resName"));
		}
		if (jsonObject.containsKey("resPath")) {
			model.setResPath(jsonObject.getString("resPath"));
		}
		if (jsonObject.containsKey("resType")) {
			model.setResType(jsonObject.getString("resType"));
		}
		if (jsonObject.containsKey("resContentType")) {
			model.setResContentType(jsonObject.getString("resContentType"));
		}
		if (jsonObject.containsKey("tenantId")) {
			model.setTenantId(jsonObject.getString("tenantId"));
		}
		if (jsonObject.containsKey("createBy")) {
			model.setCreateBy(jsonObject.getString("createBy"));
		}
		if (jsonObject.containsKey("createTime")) {
			model.setCreateTime(jsonObject.getDate("createTime"));
		}

		return model;
	}

	public static JSONArray listToArray(java.util.List<PageResource> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (PageResource model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static JSONObject toJsonObject(PageResource model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());

		if (model.getResFileId() != null) {
			jsonObject.put("resFileId", model.getResFileId());
		}

		if (model.getResFileName() != null) {
			jsonObject.put("resFileName", model.getResFileName());
		}
		if (model.getResName() != null) {
			jsonObject.put("resName", model.getResName());
		}
		if (model.getResPath() != null) {
			jsonObject.put("resPath", model.getResPath());
		}
		if (model.getResType() != null) {
			jsonObject.put("resType", model.getResType());
		}
		if (model.getResContentType() != null) {
			jsonObject.put("resContentType", model.getResContentType());
		}
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
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

	public static ObjectNode toObjectNode(PageResource model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());

		if (model.getResFileId() != null) {
			jsonObject.put("resFileId", model.getResFileId());
		}
		if (model.getResFileName() != null) {
			jsonObject.put("resFileName", model.getResFileName());
		}
		if (model.getResName() != null) {
			jsonObject.put("resName", model.getResName());
		}
		if (model.getResPath() != null) {
			jsonObject.put("resPath", model.getResPath());
		}
		if (model.getResType() != null) {
			jsonObject.put("resType", model.getResType());
		}
		if (model.getResContentType() != null) {
			jsonObject.put("resContentType", model.getResContentType());
		}
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
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

	private PageResourceJsonFactory() {

	}

}
