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

import com.glaf.core.util.DateUtils;
import com.glaf.base.modules.sys.model.*;

/**
 * 
 * JSON工厂类
 *
 */
public class IdentityTokenJsonFactory {

	public static java.util.List<IdentityToken> arrayToList(JSONArray array) {
		java.util.List<IdentityToken> list = new java.util.ArrayList<IdentityToken>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			IdentityToken model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	public static IdentityToken jsonToObject(JSONObject jsonObject) {
		IdentityToken model = new IdentityToken();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getString("id"));
		}
		if (jsonObject.containsKey("userId")) {
			model.setUserId(jsonObject.getString("userId"));
		}
		if (jsonObject.containsKey("clientIP")) {
			model.setClientIP(jsonObject.getString("clientIP"));
		}
		if (jsonObject.containsKey("signature")) {
			model.setSignature(jsonObject.getString("signature"));
		}
		if (jsonObject.containsKey("token")) {
			model.setToken(jsonObject.getString("token"));
		}
		if (jsonObject.containsKey("nonce")) {
			model.setNonce(jsonObject.getString("nonce"));
		}
		if (jsonObject.containsKey("rand1")) {
			model.setRand1(jsonObject.getString("rand1"));
		}
		if (jsonObject.containsKey("rand2")) {
			model.setRand2(jsonObject.getString("rand2"));
		}
		if (jsonObject.containsKey("type")) {
			model.setType(jsonObject.getString("type"));
		}
		if (jsonObject.containsKey("timeLive")) {
			model.setTimeLive(jsonObject.getInteger("timeLive"));
		}
		if (jsonObject.containsKey("timeMillis")) {
			model.setTimeMillis(jsonObject.getLong("timeMillis"));
		}
		if (jsonObject.containsKey("createTime")) {
			model.setCreateTime(jsonObject.getDate("createTime"));
		}

		return model;
	}

	public static JSONArray listToArray(java.util.List<IdentityToken> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (IdentityToken model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static JSONObject toJsonObject(IdentityToken model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());

		if (model.getUserId() != null) {
			jsonObject.put("userId", model.getUserId());
		}
		if (model.getClientIP() != null) {
			jsonObject.put("clientIP", model.getClientIP());
		}
		if (model.getSignature() != null) {
			jsonObject.put("signature", model.getSignature());
		}
		if (model.getToken() != null) {
			jsonObject.put("token", model.getToken());
		}
		if (model.getNonce() != null) {
			jsonObject.put("nonce", model.getNonce());
		}
		if (model.getRand1() != null) {
			jsonObject.put("rand1", model.getRand1());
		}
		if (model.getRand2() != null) {
			jsonObject.put("rand2", model.getRand2());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		jsonObject.put("timeLive", model.getTimeLive());
		jsonObject.put("timeMillis", model.getTimeMillis());
		if (model.getCreateTime() != null) {
			jsonObject.put("createTime", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_date", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_datetime", DateUtils.getDateTime(model.getCreateTime()));
		}
		return jsonObject;
	}

	public static ObjectNode toObjectNode(IdentityToken model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		
		if (model.getUserId() != null) {
			jsonObject.put("userId", model.getUserId());
		}
		if (model.getClientIP() != null) {
			jsonObject.put("clientIP", model.getClientIP());
		}
		if (model.getSignature() != null) {
			jsonObject.put("signature", model.getSignature());
		}
		if (model.getToken() != null) {
			jsonObject.put("token", model.getToken());
		}
		if (model.getNonce() != null) {
			jsonObject.put("nonce", model.getNonce());
		}
		if (model.getRand1() != null) {
			jsonObject.put("rand1", model.getRand1());
		}
		if (model.getRand2() != null) {
			jsonObject.put("rand2", model.getRand2());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		jsonObject.put("timeLive", model.getTimeLive());
		jsonObject.put("timeMillis", model.getTimeMillis());
		if (model.getCreateTime() != null) {
			jsonObject.put("createTime", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_date", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_datetime", DateUtils.getDateTime(model.getCreateTime()));
		}
		return jsonObject;
	}

	private IdentityTokenJsonFactory() {

	}

}
