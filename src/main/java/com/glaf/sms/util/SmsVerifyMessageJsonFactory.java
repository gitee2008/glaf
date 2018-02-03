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

import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.glaf.core.util.DateUtils;
import com.glaf.sms.domain.*;

/**
 * 
 * JSON工厂类
 *
 */
public class SmsVerifyMessageJsonFactory {

	public static SmsVerifyMessage jsonToObject(JSONObject jsonObject) {
		SmsVerifyMessage model = new SmsVerifyMessage();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getString("id"));
		}
		if (jsonObject.containsKey("name")) {
			model.setName(jsonObject.getString("name"));
		}
		if (jsonObject.containsKey("mobile")) {
			model.setMobile(jsonObject.getString("mobile"));
		}
		if (jsonObject.containsKey("type")) {
			model.setType(jsonObject.getString("type"));
		}
		if (jsonObject.containsKey("verificationCode")) {
			model.setVerificationCode(jsonObject.getString("verificationCode"));
		}
		if (jsonObject.containsKey("status")) {
			model.setStatus(jsonObject.getInteger("status"));
		}
		if (jsonObject.containsKey("sendTime")) {
			model.setSendTime(jsonObject.getDate("sendTime"));
		}
		if (jsonObject.containsKey("sendTimeMs")) {
			model.setSendTimeMs(jsonObject.getLong("sendTimeMs"));
		}
		if (jsonObject.containsKey("createTime")) {
			model.setCreateTime(jsonObject.getDate("createTime"));
		}

		return model;
	}

	public static JSONObject toJsonObject(SmsVerifyMessage model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getMobile() != null) {
			jsonObject.put("mobile", model.getMobile());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		if (model.getVerificationCode() != null) {
			jsonObject.put("verificationCode", model.getVerificationCode());
		}
		jsonObject.put("status", model.getStatus());
		if (model.getSendTime() != null) {
			jsonObject.put("sendTime", DateUtils.getDate(model.getSendTime()));
			jsonObject.put("sendTime_date", DateUtils.getDate(model.getSendTime()));
			jsonObject.put("sendTime_datetime", DateUtils.getDateTime(model.getSendTime()));
		}
		jsonObject.put("sendTimeMs", model.getSendTimeMs());
		if (model.getCreateTime() != null) {
			jsonObject.put("createTime", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_date", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_datetime", DateUtils.getDateTime(model.getCreateTime()));
		}
		return jsonObject;
	}

	public static ObjectNode toObjectNode(SmsVerifyMessage model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getMobile() != null) {
			jsonObject.put("mobile", model.getMobile());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		if (model.getVerificationCode() != null) {
			jsonObject.put("verificationCode", model.getVerificationCode());
		}
		jsonObject.put("status", model.getStatus());
		if (model.getSendTime() != null) {
			jsonObject.put("sendTime", DateUtils.getDate(model.getSendTime()));
			jsonObject.put("sendTime_date", DateUtils.getDate(model.getSendTime()));
			jsonObject.put("sendTime_datetime", DateUtils.getDateTime(model.getSendTime()));
		}
		jsonObject.put("sendTimeMs", model.getSendTimeMs());
		if (model.getCreateTime() != null) {
			jsonObject.put("createTime", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_date", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_datetime", DateUtils.getDateTime(model.getCreateTime()));
		}
		return jsonObject;
	}

	public static JSONArray listToArray(java.util.List<SmsVerifyMessage> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (SmsVerifyMessage model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static java.util.List<SmsVerifyMessage> arrayToList(JSONArray array) {
		java.util.List<SmsVerifyMessage> list = new java.util.ArrayList<SmsVerifyMessage>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			SmsVerifyMessage model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	private SmsVerifyMessageJsonFactory() {

	}

}
