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

package com.glaf.sms.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.core.util.DateUtils;
import com.glaf.sms.domain.SmsServer;

/**
 * 
 * JSON工厂类
 *
 */
public class SmsServerJsonFactory {

	public static java.util.List<SmsServer> arrayToList(JSONArray array) {
		java.util.List<SmsServer> list = new java.util.ArrayList<SmsServer>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			SmsServer model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	public static SmsServer jsonToObject(JSONObject jsonObject) {
		SmsServer model = new SmsServer();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getString("id"));
		}
		if (jsonObject.containsKey("subject")) {
			model.setSubject(jsonObject.getString("subject"));
		}
		if (jsonObject.containsKey("serverIP")) {
			model.setServerIP(jsonObject.getString("serverIP"));
		}
		if (jsonObject.containsKey("port")) {
			model.setPort(jsonObject.getInteger("port"));
		}
		if (jsonObject.containsKey("path")) {
			model.setPath(jsonObject.getString("path"));
		}
		if (jsonObject.containsKey("requestBody")) {
			model.setRequestBody(jsonObject.getString("requestBody"));
		}
		if (jsonObject.containsKey("responseBody")) {
			model.setResponseBody(jsonObject.getString("responseBody"));
		}
		if (jsonObject.containsKey("responseResult")) {
			model.setResponseResult(jsonObject.getString("responseResult"));
		}
		if (jsonObject.containsKey("frequence")) {
			model.setFrequence(jsonObject.getInteger("frequence"));
		}
		if (jsonObject.containsKey("retryTimes")) {
			model.setRetryTimes(jsonObject.getInteger("retryTimes"));
		}
		if (jsonObject.containsKey("key")) {
			model.setKey(jsonObject.getString("key"));
		}
		if (jsonObject.containsKey("accessKeyId")) {
			model.setAccessKeyId(jsonObject.getString("accessKeyId"));
		}
		if (jsonObject.containsKey("accessKeySecret")) {
			model.setAccessKeySecret(jsonObject.getString("accessKeySecret"));
		}
		if (jsonObject.containsKey("signName")) {
			model.setSignName(jsonObject.getString("signName"));
		}
		if (jsonObject.containsKey("templateCode")) {
			model.setTemplateCode(jsonObject.getString("templateCode"));
		}
		if (jsonObject.containsKey("provider")) {
			model.setProvider(jsonObject.getString("provider"));
		}
		if (jsonObject.containsKey("type")) {
			model.setType(jsonObject.getString("type"));
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

		return model;
	}

	public static JSONArray listToArray(java.util.List<SmsServer> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (SmsServer model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static JSONObject toJsonObject(SmsServer model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getSubject() != null) {
			jsonObject.put("subject", model.getSubject());
		}
		if (model.getServerIP() != null) {
			jsonObject.put("serverIP", model.getServerIP());
		}
		jsonObject.put("port", model.getPort());
		if (model.getPath() != null) {
			jsonObject.put("path", model.getPath());
		}
		if (model.getKey() != null) {
			jsonObject.put("key", model.getKey());
		}
		if (model.getAccessKeyId() != null) {
			jsonObject.put("accessKeyId", model.getAccessKeyId());
		}
		if (model.getAccessKeySecret() != null) {
			jsonObject.put("accessKeySecret", model.getAccessKeySecret());
		}
		if (model.getSignName() != null) {
			jsonObject.put("signName", model.getSignName());
		}
		if (model.getTemplateCode() != null) {
			jsonObject.put("templateCode", model.getTemplateCode());
		}
		if (model.getProvider() != null) {
			jsonObject.put("provider", model.getProvider());
		}
		if (model.getRequestBody() != null) {
			jsonObject.put("requestBody", model.getRequestBody());
		}
		if (model.getResponseBody() != null) {
			jsonObject.put("responseBody", model.getResponseBody());
		}
		if (model.getResponseResult() != null) {
			jsonObject.put("responseResult", model.getResponseResult());
		}
		jsonObject.put("frequence", model.getFrequence());
		jsonObject.put("retryTimes", model.getRetryTimes());
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		jsonObject.put("locked", model.getLocked());
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

	public static ObjectNode toObjectNode(SmsServer model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getSubject() != null) {
			jsonObject.put("subject", model.getSubject());
		}
		if (model.getServerIP() != null) {
			jsonObject.put("serverIP", model.getServerIP());
		}
		jsonObject.put("port", model.getPort());
		if (model.getPath() != null) {
			jsonObject.put("path", model.getPath());
		}
		if (model.getKey() != null) {
			jsonObject.put("key", model.getKey());
		}
		if (model.getAccessKeyId() != null) {
			jsonObject.put("accessKeyId", model.getAccessKeyId());
		}
		if (model.getAccessKeySecret() != null) {
			jsonObject.put("accessKeySecret", model.getAccessKeySecret());
		}
		if (model.getSignName() != null) {
			jsonObject.put("signName", model.getSignName());
		}
		if (model.getTemplateCode() != null) {
			jsonObject.put("templateCode", model.getTemplateCode());
		}
		if (model.getProvider() != null) {
			jsonObject.put("provider", model.getProvider());
		}
		if (model.getRequestBody() != null) {
			jsonObject.put("requestBody", model.getRequestBody());
		}
		if (model.getResponseBody() != null) {
			jsonObject.put("responseBody", model.getResponseBody());
		}
		if (model.getResponseResult() != null) {
			jsonObject.put("responseResult", model.getResponseResult());
		}
		jsonObject.put("frequence", model.getFrequence());
		jsonObject.put("retryTimes", model.getRetryTimes());
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		jsonObject.put("locked", model.getLocked());
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

	private SmsServerJsonFactory() {

	}

}
