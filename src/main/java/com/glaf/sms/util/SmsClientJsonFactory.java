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
import com.glaf.sms.domain.SmsClient;

/**
 * 
 * JSON工厂类
 *
 */
public class SmsClientJsonFactory {

	public static SmsClient jsonToObject(JSONObject jsonObject) {
		SmsClient model = new SmsClient();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getString("id"));
		}
		if (jsonObject.containsKey("subject")) {
			model.setSubject(jsonObject.getString("subject"));
		}
		if (jsonObject.containsKey("remoteIP")) {
			model.setRemoteIP(jsonObject.getString("remoteIP"));
		}
		if (jsonObject.containsKey("sysCode")) {
			model.setSysCode(jsonObject.getString("sysCode"));
		}
		if (jsonObject.containsKey("sysPwd")) {
			model.setSysPwd(jsonObject.getString("sysPwd"));
		}
		if (jsonObject.containsKey("encryptionFlag")) {
			model.setEncryptionFlag(jsonObject.getString("encryptionFlag"));
		}
		if (jsonObject.containsKey("publicKey")) {
			model.setPublicKey(jsonObject.getString("publicKey"));
		}
		if (jsonObject.containsKey("privateKey")) {
			model.setPrivateKey(jsonObject.getString("privateKey"));
		}
		if (jsonObject.containsKey("peerPublicKey")) {
			model.setPeerPublicKey(jsonObject.getString("peerPublicKey"));
		}
		if (jsonObject.containsKey("token")) {
			model.setToken(jsonObject.getString("token"));
		}
		if (jsonObject.containsKey("type")) {
			model.setType(jsonObject.getString("type"));
		}
		if (jsonObject.containsKey("frequence")) {
			model.setFrequence(jsonObject.getInteger("frequence"));
		}
		if (jsonObject.containsKey("limit")) {
			model.setLimit(jsonObject.getInteger("limit"));
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

	public static JSONObject toJsonObject(SmsClient model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getSubject() != null) {
			jsonObject.put("subject", model.getSubject());
		}
		if (model.getRemoteIP() != null) {
			jsonObject.put("remoteIP", model.getRemoteIP());
		}
		if (model.getSysCode() != null) {
			jsonObject.put("sysCode", model.getSysCode());
		}
		if (model.getSysPwd() != null) {
			jsonObject.put("sysPwd", model.getSysPwd());
		}
		if (model.getEncryptionFlag() != null) {
			jsonObject.put("encryptionFlag", model.getEncryptionFlag());
		}
		if (model.getPublicKey() != null) {
			jsonObject.put("publicKey", model.getPublicKey());
		}
		if (model.getPrivateKey() != null) {
			jsonObject.put("privateKey", model.getPrivateKey());
		}
		if (model.getPeerPublicKey() != null) {
			jsonObject.put("peerPublicKey", model.getPeerPublicKey());
		}
		if (model.getToken() != null) {
			jsonObject.put("token", model.getToken());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		jsonObject.put("frequence", model.getFrequence());
		jsonObject.put("limit", model.getLimit());
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

	public static ObjectNode toObjectNode(SmsClient model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getSubject() != null) {
			jsonObject.put("subject", model.getSubject());
		}
		if (model.getRemoteIP() != null) {
			jsonObject.put("remoteIP", model.getRemoteIP());
		}
		if (model.getSysCode() != null) {
			jsonObject.put("sysCode", model.getSysCode());
		}
		if (model.getSysPwd() != null) {
			jsonObject.put("sysPwd", model.getSysPwd());
		}
		if (model.getEncryptionFlag() != null) {
			jsonObject.put("encryptionFlag", model.getEncryptionFlag());
		}
		if (model.getPublicKey() != null) {
			jsonObject.put("publicKey", model.getPublicKey());
		}
		if (model.getPrivateKey() != null) {
			jsonObject.put("privateKey", model.getPrivateKey());
		}
		if (model.getPeerPublicKey() != null) {
			jsonObject.put("peerPublicKey", model.getPeerPublicKey());
		}
		if (model.getToken() != null) {
			jsonObject.put("token", model.getToken());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		jsonObject.put("frequence", model.getFrequence());
		jsonObject.put("limit", model.getLimit());
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

	public static JSONArray listToArray(java.util.List<SmsClient> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (SmsClient model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static java.util.List<SmsClient> arrayToList(JSONArray array) {
		java.util.List<SmsClient> list = new java.util.ArrayList<SmsClient>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			SmsClient model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	private SmsClientJsonFactory() {

	}

}
