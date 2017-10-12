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

import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.core.util.DateUtils;
import com.glaf.core.domain.*;

/**
 * 
 * JSON工厂类
 * 
 */
public class ServerEntityJsonFactory {

	public static java.util.List<ServerEntity> arrayToList(JSONArray array) {
		java.util.List<ServerEntity> list = new java.util.ArrayList<ServerEntity>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			ServerEntity model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	public static ServerEntity jsonToObject(JSONObject jsonObject) {
		ServerEntity model = new ServerEntity();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getLong("id"));
		}
		if (jsonObject.containsKey("nodeId")) {
			model.setNodeId(jsonObject.getLong("nodeId"));
		}
		if (jsonObject.containsKey("title")) {
			model.setTitle(jsonObject.getString("title"));
		}
		if (jsonObject.containsKey("host")) {
			model.setHost(jsonObject.getString("host"));
		}
		if (jsonObject.containsKey("port")) {
			model.setPort(jsonObject.getInteger("port"));
		}
		if (jsonObject.containsKey("name")) {
			model.setName(jsonObject.getString("name"));
		}
		if (jsonObject.containsKey("code")) {
			model.setCode(jsonObject.getString("code"));
		}
		if (jsonObject.containsKey("discriminator")) {
			model.setDiscriminator(jsonObject.getString("discriminator"));
		}
		if (jsonObject.containsKey("mapping")) {
			model.setMapping(jsonObject.getString("mapping"));
		}
		if (jsonObject.containsKey("dbname")) {
			model.setDbname(jsonObject.getString("dbname"));
		}
		if (jsonObject.containsKey("path")) {
			model.setPath(jsonObject.getString("path"));
		}
		if (jsonObject.containsKey("program")) {
			model.setProgram(jsonObject.getString("program"));
		}
		if (jsonObject.containsKey("catalog")) {
			model.setCatalog(jsonObject.getString("catalog"));
		}
		if (jsonObject.containsKey("user")) {
			model.setUser(jsonObject.getString("user"));
		}

		if (jsonObject.containsKey("password")) {
			model.setPassword(jsonObject.getString("password"));
		}
		if (jsonObject.containsKey("key")) {
			model.setKey(jsonObject.getString("key"));
		}
		if (jsonObject.containsKey("type")) {
			model.setType(jsonObject.getString("type"));
		}
		if (jsonObject.containsKey("level")) {
			model.setLevel(jsonObject.getInteger("level"));
		}
		if (jsonObject.containsKey("priority")) {
			model.setPriority(jsonObject.getInteger("priority"));
		}
		if (jsonObject.containsKey("operation")) {
			model.setOperation(jsonObject.getInteger("operation"));
		}
		if (jsonObject.containsKey("providerClass")) {
			model.setProviderClass(jsonObject.getString("providerClass"));
		}
		if (jsonObject.containsKey("active")) {
			model.setActive(jsonObject.getString("active"));
		}
		if (jsonObject.containsKey("detectionFlag")) {
			model.setDetectionFlag(jsonObject.getString("detectionFlag"));
		}
		if (jsonObject.containsKey("verify")) {
			model.setVerify(jsonObject.getString("verify"));
		}
		if (jsonObject.containsKey("initFlag")) {
			model.setInitFlag(jsonObject.getString("initFlag"));
		}

		if (jsonObject.containsKey("secretAlgorithm")) {
			model.setSecretAlgorithm(jsonObject.getString("secretAlgorithm"));
		}
		if (jsonObject.containsKey("secretKey")) {
			model.setSecretKey(jsonObject.getString("secretKey"));
		}
		if (jsonObject.containsKey("secretIv")) {
			model.setSecretIv(jsonObject.getString("secretIv"));
		}

		if (jsonObject.containsKey("addressPerms")) {
			model.setAddressPerms(jsonObject.getString("addressPerms"));
		}

		if (jsonObject.containsKey("perms")) {
			model.setPerms(jsonObject.getString("perms"));
		}

		if (jsonObject.containsKey("attribute")) {
			model.setAttribute(jsonObject.getString("attribute"));
		}

		return model;
	}

	public static JSONArray listToArray(java.util.List<ServerEntity> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (ServerEntity model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static JSONObject toJsonObject(ServerEntity model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		jsonObject.put("nodeId", model.getNodeId());
		if (model.getTitle() != null) {
			jsonObject.put("title", model.getTitle());
		}
		if (model.getHost() != null) {
			jsonObject.put("host", model.getHost());
		}
		jsonObject.put("port", model.getPort());

		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getCode() != null) {
			jsonObject.put("code", model.getCode());
		}
		if (model.getDiscriminator() != null) {
			jsonObject.put("discriminator", model.getDiscriminator());
		}
		if (model.getMapping() != null) {
			jsonObject.put("mapping", model.getMapping());
		}
		if (model.getDbname() != null) {
			jsonObject.put("dbname", model.getDbname());
		}
		if (model.getPath() != null) {
			jsonObject.put("path", model.getPath());
		}
		if (model.getProgram() != null) {
			jsonObject.put("program", model.getProgram());
		}
		if (model.getCatalog() != null) {
			jsonObject.put("catalog", model.getCatalog());
		}
		if (model.getUser() != null) {
			jsonObject.put("user", model.getUser());
		}
		if (model.getPassword() != null) {
			jsonObject.put("password", model.getPassword());
		}
		if (model.getKey() != null) {
			jsonObject.put("key", model.getKey());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		if (jsonObject.containsKey("secretAlgorithm")) {
			model.setSecretAlgorithm(jsonObject.getString("secretAlgorithm"));
		}
		if (jsonObject.containsKey("secretKey")) {
			model.setSecretKey(jsonObject.getString("secretKey"));
		}
		if (jsonObject.containsKey("secretIv")) {
			model.setSecretIv(jsonObject.getString("secretIv"));
		}
		jsonObject.put("level", model.getLevel());
		jsonObject.put("priority", model.getPriority());
		jsonObject.put("operation", model.getOperation());

		if (model.getProviderClass() != null) {
			jsonObject.put("providerClass", model.getProviderClass());
		}
		if (model.getActive() != null) {
			jsonObject.put("active", model.getActive());
		}
		if (model.getVerify() != null) {
			jsonObject.put("verify", model.getVerify());
		}
		if (model.getDetectionFlag() != null) {
			jsonObject.put("detectionFlag", model.getDetectionFlag());
		}
		if (model.getInitFlag() != null) {
			jsonObject.put("initFlag", model.getInitFlag());
		}

		if (model.getSecretAlgorithm() != null) {
			jsonObject.put("secretAlgorithm", model.getSecretAlgorithm());
		}
		if (model.getSecretKey() != null) {
			jsonObject.put("secretKey", model.getSecretKey());
		}
		if (model.getSecretIv() != null) {
			jsonObject.put("secretIv", model.getSecretIv());
		}

		if (model.getAddressPerms() != null) {
			jsonObject.put("addressPerms", model.getAddressPerms());
		}

		if (model.getPerms() != null) {
			jsonObject.put("perms", model.getPerms());
		}
		
		if (model.getKey() != null) {
			jsonObject.put("key", model.getKey());
		}
		
		if (model.getPassword() != null) {
			jsonObject.put("password", model.getPassword());
		}

		if (model.getAttribute() != null) {
			jsonObject.put("attribute", model.getAttribute());
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

	public static ObjectNode toObjectNode(ServerEntity model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		jsonObject.put("nodeId", model.getNodeId());
		if (model.getTitle() != null) {
			jsonObject.put("title", model.getTitle());
		}
		if (model.getHost() != null) {
			jsonObject.put("host", model.getHost());
		}
		jsonObject.put("port", model.getPort());

		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}

		if (model.getCode() != null) {
			jsonObject.put("code", model.getCode());
		}

		if (model.getDiscriminator() != null) {
			jsonObject.put("discriminator", model.getDiscriminator());
		}

		if (model.getMapping() != null) {
			jsonObject.put("mapping", model.getMapping());
		}

		if (model.getDbname() != null) {
			jsonObject.put("dbname", model.getDbname());
		}

		if (model.getPath() != null) {
			jsonObject.put("path", model.getPath());
		}

		if (model.getProgram() != null) {
			jsonObject.put("program", model.getProgram());
		}

		if (model.getCatalog() != null) {
			jsonObject.put("catalog", model.getCatalog());
		}

		if (model.getUser() != null) {
			jsonObject.put("user", model.getUser());
		}

		if (model.getPassword() != null) {
			jsonObject.put("password", model.getPassword());
		}

		if (model.getKey() != null) {
			jsonObject.put("key", model.getKey());
		}

		if (model.getSecretAlgorithm() != null) {
			jsonObject.put("secretAlgorithm", model.getSecretAlgorithm());
		}
		if (model.getSecretKey() != null) {
			jsonObject.put("secretKey", model.getSecretKey());
		}
		if (model.getSecretIv() != null) {
			jsonObject.put("secretIv", model.getSecretIv());
		}

		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}

		jsonObject.put("level", model.getLevel());
		jsonObject.put("priority", model.getPriority());
		jsonObject.put("operation", model.getOperation());

		if (model.getProviderClass() != null) {
			jsonObject.put("providerClass", model.getProviderClass());
		}
		if (model.getActive() != null) {
			jsonObject.put("active", model.getActive());
		}
		if (model.getVerify() != null) {
			jsonObject.put("verify", model.getVerify());
		}
		if (model.getDetectionFlag() != null) {
			jsonObject.put("detectionFlag", model.getDetectionFlag());
		}
		if (model.getInitFlag() != null) {
			jsonObject.put("initFlag", model.getInitFlag());
		}

		if (model.getAddressPerms() != null) {
			jsonObject.put("addressPerms", model.getAddressPerms());
		}

		if (model.getPerms() != null) {
			jsonObject.put("perms", model.getPerms());
		}

		if (model.getAttribute() != null) {
			jsonObject.put("attribute", model.getAttribute());
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

	private ServerEntityJsonFactory() {

	}

}
