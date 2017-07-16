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

package com.glaf.core.access.util;

import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.glaf.core.util.DateUtils;
import com.glaf.core.access.domain.*;

/**
 * 
 * JSON工厂类
 *
 */
public class AccessLogJsonFactory {

	public static java.util.List<AccessLog> arrayToList(JSONArray array) {
		java.util.List<AccessLog> list = new java.util.ArrayList<AccessLog>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			AccessLog model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	public static AccessLog jsonToObject(JSONObject jsonObject) {
		AccessLog model = new AccessLog();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getLong("id"));
		}
		if (jsonObject.containsKey("ip")) {
			model.setIp(jsonObject.getString("ip"));
		}
		if (jsonObject.containsKey("method")) {
			model.setMethod(jsonObject.getString("method"));
		}
		if (jsonObject.containsKey("content")) {
			model.setContent(jsonObject.getString("content"));
		}
		if (jsonObject.containsKey("uri")) {
			model.setUri(jsonObject.getString("uri"));
		}
		if (jsonObject.containsKey("uriRefId")) {
			model.setUriRefId(jsonObject.getLong("uriRefId"));
		}
		if (jsonObject.containsKey("status")) {
			model.setStatus(jsonObject.getInteger("status"));
		}
		if (jsonObject.containsKey("day")) {
			model.setDay(jsonObject.getInteger("day"));
		}
		if (jsonObject.containsKey("hour")) {
			model.setHour(jsonObject.getInteger("hour"));
		}
		if (jsonObject.containsKey("minute")) {
			model.setMinute(jsonObject.getInteger("minute"));
		}
		if (jsonObject.containsKey("timeMillis")) {
			model.setTimeMillis(jsonObject.getInteger("timeMillis"));
		}
		if (jsonObject.containsKey("type")) {
			model.setType(jsonObject.getString("type"));
		}
		if (jsonObject.containsKey("userId")) {
			model.setUserId(jsonObject.getString("userId"));
		}
		if (jsonObject.containsKey("accessTime")) {
			model.setAccessTime(jsonObject.getDate("accessTime"));
		}

		return model;
	}

	public static JSONArray listToArray(java.util.List<AccessLog> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (AccessLog model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static JSONObject toJsonObject(AccessLog model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getIp() != null) {
			jsonObject.put("ip", model.getIp());
		}
		if (model.getMethod() != null) {
			jsonObject.put("method", model.getMethod());
		}
		if (model.getContent() != null) {
			jsonObject.put("content", model.getContent());
		}
		if (model.getUri() != null) {
			jsonObject.put("uri", model.getUri());
		}
		jsonObject.put("uriRefId", model.getUriRefId());
		jsonObject.put("status", model.getStatus());
		jsonObject.put("day", model.getDay());
		jsonObject.put("hour", model.getHour());
		jsonObject.put("minute", model.getMinute());
		jsonObject.put("timeMillis", model.getTimeMillis());
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		if (model.getUserId() != null) {
			jsonObject.put("userId", model.getUserId());
		}
		if (model.getAccessTime() != null) {
			jsonObject.put("accessTime", DateUtils.getDate(model.getAccessTime()));
			jsonObject.put("accessTime_date", DateUtils.getDate(model.getAccessTime()));
			jsonObject.put("accessTime_datetime", DateUtils.getDateTime(model.getAccessTime()));
		}
		return jsonObject;
	}

	public static ObjectNode toObjectNode(AccessLog model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getIp() != null) {
			jsonObject.put("ip", model.getIp());
		}
		if (model.getMethod() != null) {
			jsonObject.put("method", model.getMethod());
		}
		if (model.getContent() != null) {
			jsonObject.put("content", model.getContent());
		}
		if (model.getUri() != null) {
			jsonObject.put("uri", model.getUri());
		}
		jsonObject.put("uriRefId", model.getUriRefId());
		jsonObject.put("status", model.getStatus());
		jsonObject.put("day", model.getDay());
		jsonObject.put("hour", model.getHour());
		jsonObject.put("minute", model.getMinute());
		jsonObject.put("timeMillis", model.getTimeMillis());
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		if (model.getUserId() != null) {
			jsonObject.put("userId", model.getUserId());
		}
		if (model.getAccessTime() != null) {
			jsonObject.put("accessTime", DateUtils.getDate(model.getAccessTime()));
			jsonObject.put("accessTime_date", DateUtils.getDate(model.getAccessTime()));
			jsonObject.put("accessTime_datetime", DateUtils.getDateTime(model.getAccessTime()));
		}
		return jsonObject;
	}

	private AccessLogJsonFactory() {

	}

}
