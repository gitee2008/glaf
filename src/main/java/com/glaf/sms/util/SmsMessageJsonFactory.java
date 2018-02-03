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
import com.glaf.sms.domain.SmsMessage;

/**
 * 
 * JSON工厂类
 *
 */
public class SmsMessageJsonFactory {

	public static SmsMessage jsonToObject(JSONObject jsonObject) {
		SmsMessage model = new SmsMessage();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getString("id"));
		}
		if (jsonObject.containsKey("clientId")) {
			model.setClientId(jsonObject.getString("clientId"));
		}
		if (jsonObject.containsKey("serverId")) {
			model.setServerId(jsonObject.getString("serverId"));
		}
		if (jsonObject.containsKey("name")) {
			model.setName(jsonObject.getString("name"));
		}
		if (jsonObject.containsKey("mobile")) {
			model.setMobile(jsonObject.getString("mobile"));
		}
		if (jsonObject.containsKey("subject")) {
			model.setSubject(jsonObject.getString("subject"));
		}
		if (jsonObject.containsKey("message")) {
			model.setMessage(jsonObject.getString("message"));
		}
		if (jsonObject.containsKey("retryTimes")) {
			model.setRetryTimes(jsonObject.getInteger("retryTimes"));
		}
		if (jsonObject.containsKey("sendTime")) {
			model.setSendTime(jsonObject.getDate("sendTime"));
		}
		if (jsonObject.containsKey("status")) {
			model.setStatus(jsonObject.getInteger("status"));
		}
		if (jsonObject.containsKey("year")) {
			model.setYear(jsonObject.getInteger("year"));
		}
		if (jsonObject.containsKey("month")) {
			model.setMonth(jsonObject.getInteger("month"));
		}
		if (jsonObject.containsKey("fullDay")) {
			model.setFullDay(jsonObject.getInteger("fullDay"));
		}
		if (jsonObject.containsKey("createTime")) {
			model.setCreateTime(jsonObject.getDate("createTime"));
		}
		if (jsonObject.containsKey("result")) {
			model.setMessage(jsonObject.getString("result"));
		}
		return model;
	}

	public static JSONObject toJsonObject(SmsMessage model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getClientId() != null) {
			jsonObject.put("clientId", model.getClientId());
		}
		if (model.getServerId() != null) {
			jsonObject.put("serverId", model.getServerId());
		}
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getMobile() != null) {
			jsonObject.put("mobile", model.getMobile());
		}
		if (model.getSubject() != null) {
			jsonObject.put("subject", model.getSubject());
		}
		if (model.getMessage() != null) {
			jsonObject.put("message", model.getMessage());
		}
		if (model.getResult() != null) {
			jsonObject.put("result", model.getResult());
		}
		jsonObject.put("retryTimes", model.getRetryTimes());
		if (model.getSendTime() != null) {
			jsonObject.put("sendTime", DateUtils.getDate(model.getSendTime()));
			jsonObject.put("sendTime_date", DateUtils.getDate(model.getSendTime()));
			jsonObject.put("sendTime_datetime", DateUtils.getDateTime(model.getSendTime()));
		}
		jsonObject.put("status", model.getStatus());
		jsonObject.put("year", model.getYear());
		jsonObject.put("month", model.getMonth());
		jsonObject.put("fullDay", model.getFullDay());
		if (model.getCreateTime() != null) {
			jsonObject.put("createTime", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_date", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_datetime", DateUtils.getDateTime(model.getCreateTime()));
		}
		return jsonObject;
	}

	public static ObjectNode toObjectNode(SmsMessage model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getClientId() != null) {
			jsonObject.put("clientId", model.getClientId());
		}
		if (model.getServerId() != null) {
			jsonObject.put("serverId", model.getServerId());
		}
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getMobile() != null) {
			jsonObject.put("mobile", model.getMobile());
		}
		if (model.getSubject() != null) {
			jsonObject.put("subject", model.getSubject());
		}
		if (model.getMessage() != null) {
			jsonObject.put("message", model.getMessage());
		}
		if (model.getResult() != null) {
			jsonObject.put("result", model.getResult());
		}
		jsonObject.put("retryTimes", model.getRetryTimes());
		if (model.getSendTime() != null) {
			jsonObject.put("sendTime", DateUtils.getDate(model.getSendTime()));
			jsonObject.put("sendTime_date", DateUtils.getDate(model.getSendTime()));
			jsonObject.put("sendTime_datetime", DateUtils.getDateTime(model.getSendTime()));
		}
		jsonObject.put("status", model.getStatus());
		jsonObject.put("year", model.getYear());
		jsonObject.put("month", model.getMonth());
		jsonObject.put("fullDay", model.getFullDay());
		if (model.getCreateTime() != null) {
			jsonObject.put("createTime", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_date", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_datetime", DateUtils.getDateTime(model.getCreateTime()));
		}
		return jsonObject;
	}

	public static JSONArray listToArray(java.util.List<SmsMessage> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (SmsMessage model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static java.util.List<SmsMessage> arrayToList(JSONArray array) {
		java.util.List<SmsMessage> list = new java.util.ArrayList<SmsMessage>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			SmsMessage model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	private SmsMessageJsonFactory() {

	}

}
