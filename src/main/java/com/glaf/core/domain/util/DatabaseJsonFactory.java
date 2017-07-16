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
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.core.util.DateUtils;
import com.glaf.core.domain.*;

/**
 * 
 * JSON工厂类
 * 
 */
public class DatabaseJsonFactory {

	public static Database jsonToObject(JSONObject jsonObject) {
		Database model = new Database();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getLong("id"));
		}
		if (jsonObject.containsKey("parentId")) {
			model.setParentId(jsonObject.getLong("parentId"));
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
		if (jsonObject.containsKey("section")) {
			model.setSection(jsonObject.getString("section"));
		}
		if (jsonObject.containsKey("dbname")) {
			model.setDbname(jsonObject.getString("dbname"));
		}

		if (jsonObject.containsKey("bucket")) {
			model.setBucket(jsonObject.getString("bucket"));
		}

		if (jsonObject.containsKey("catalog")) {
			model.setCatalog(jsonObject.getString("catalog"));
		}
		if (jsonObject.containsKey("infoServer")) {
			model.setInfoServer(jsonObject.getString("infoServer"));
		}
		if (jsonObject.containsKey("loginAs")) {
			model.setLoginAs(jsonObject.getString("loginAs"));
		}

		if (jsonObject.containsKey("loginUrl")) {
			model.setLoginUrl(jsonObject.getString("loginUrl"));
		}
		if (jsonObject.containsKey("ticket")) {
			model.setTicket(jsonObject.getString("ticket"));
		}
		if (jsonObject.containsKey("programId")) {
			model.setProgramId(jsonObject.getString("programId"));
		}
		if (jsonObject.containsKey("programName")) {
			model.setProgramName(jsonObject.getString("programName"));
		}
		if (jsonObject.containsKey("userNameKey")) {
			model.setUserNameKey(jsonObject.getString("userNameKey"));
		}

		if (jsonObject.containsKey("serverId")) {
			model.setServerId(jsonObject.getLong("serverId"));
		}

		if (jsonObject.containsKey("sysId")) {
			model.setSysId(jsonObject.getString("sysId"));
		}

		if (jsonObject.containsKey("queueName")) {
			model.setQueueName(jsonObject.getString("queueName"));
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

		if (jsonObject.containsKey("intToken")) {
			model.setIntToken(jsonObject.getInteger("intToken"));
		}

		if (jsonObject.containsKey("sort")) {
			model.setSort(jsonObject.getInteger("sort"));
		}

		if (jsonObject.containsKey("token")) {
			model.setToken(jsonObject.getString("token"));
		}

		if (jsonObject.containsKey("type")) {
			model.setType(jsonObject.getString("type"));
		}
		if (jsonObject.containsKey("runType")) {
			model.setRunType(jsonObject.getString("runType"));
		}
		if (jsonObject.containsKey("useType")) {
			model.setUseType(jsonObject.getString("useType"));
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
		if (jsonObject.containsKey("remoteUrl")) {
			model.setRemoteUrl(jsonObject.getString("remoteUrl"));
		}
		if (jsonObject.containsKey("active")) {
			model.setActive(jsonObject.getString("active"));
		}
		if (jsonObject.containsKey("verify")) {
			model.setVerify(jsonObject.getString("verify"));
		}
		if (jsonObject.containsKey("initFlag")) {
			model.setInitFlag(jsonObject.getString("initFlag"));
		}
		if (jsonObject.containsKey("removeFlag")) {
			model.setRemoveFlag(jsonObject.getString("removeFlag"));
		}

		if (jsonObject.containsKey("accesses")) {
			JSONArray array = jsonObject.getJSONArray("accesses");
			if (array != null && !array.isEmpty()) {
				java.util.List<DatabaseAccess> list = DatabaseAccessJsonFactory.arrayToList(array);
				model.setAccesses(list);
			}
		}

		return model;
	}

	public static JSONObject toJsonObject(Database model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		jsonObject.put("parentId", model.getParentId());
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
		if (model.getSection() != null) {
			jsonObject.put("section", model.getSection());
		}
		if (model.getDbname() != null) {
			jsonObject.put("dbname", model.getDbname());
		}
		if (model.getBucket() != null) {
			jsonObject.put("bucket", model.getBucket());
		}

		if (model.getCatalog() != null) {
			jsonObject.put("catalog", model.getCatalog());
		}
		if (model.getInfoServer() != null) {
			jsonObject.put("infoServer", model.getInfoServer());
		}
		if (model.getLoginAs() != null) {
			jsonObject.put("loginAs", model.getLoginAs());
		}
		if (model.getLoginUrl() != null) {
			jsonObject.put("loginUrl", model.getLoginUrl());
		}
		if (model.getTicket() != null) {
			jsonObject.put("ticket", model.getTicket());
		}
		if (model.getProgramId() != null) {
			jsonObject.put("programId", model.getProgramId());
		}
		if (model.getProgramName() != null) {
			jsonObject.put("programName", model.getProgramName());
		}
		if (model.getUserNameKey() != null) {
			jsonObject.put("userNameKey", model.getUserNameKey());
		}
		jsonObject.put("serverId", model.getServerId());

		if (model.getSysId() != null) {
			jsonObject.put("sysId", model.getSysId());
		}

		if (model.getQueueName() != null) {
			jsonObject.put("queueName", model.getQueueName());
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
		if (model.getRunType() != null) {
			jsonObject.put("runType", model.getRunType());
		}
		if (model.getUseType() != null) {
			jsonObject.put("useType", model.getUseType());
		}
		jsonObject.put("level", model.getLevel());
		jsonObject.put("priority", model.getPriority());
		jsonObject.put("operation", model.getOperation());
		jsonObject.put("intToken", model.getIntToken());
		jsonObject.put("sort", model.getSort());

		if (model.getProviderClass() != null) {
			jsonObject.put("providerClass", model.getProviderClass());
		}
		if (model.getRemoteUrl() != null) {
			jsonObject.put("remoteUrl", model.getRemoteUrl());
		}
		if (model.getActive() != null) {
			jsonObject.put("active", model.getActive());
		}
		if (model.getVerify() != null) {
			jsonObject.put("verify", model.getVerify());
		}
		if (model.getInitFlag() != null) {
			jsonObject.put("initFlag", model.getInitFlag());
		}
		if (model.getRemoveFlag() != null) {
			jsonObject.put("removeFlag", model.getRemoveFlag());
		}
		if (model.getToken() != null) {
			jsonObject.put("token", model.getToken());
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

		java.util.List<DatabaseAccess> list = model.getAccesses();
		if (list != null && !list.isEmpty()) {
			JSONArray array = new JSONArray();
			if (list != null && !list.isEmpty()) {
				for (DatabaseAccess da : list) {
					JSONObject json = da.toJsonObject();
					array.add(json);
				}
				jsonObject.put("accesses", array);
			}
		}
		return jsonObject;
	}

	public static ObjectNode toObjectNode(Database model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		jsonObject.put("parentId", model.getParentId());
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

		if (model.getSection() != null) {
			jsonObject.put("section", model.getSection());
		}

		if (model.getDbname() != null) {
			jsonObject.put("dbname", model.getDbname());
		}

		if (model.getBucket() != null) {
			jsonObject.put("bucket", model.getBucket());
		}

		if (model.getCatalog() != null) {
			jsonObject.put("catalog", model.getCatalog());
		}
		if (model.getInfoServer() != null) {
			jsonObject.put("infoServer", model.getInfoServer());
		}
		if (model.getLoginAs() != null) {
			jsonObject.put("loginAs", model.getLoginAs());
		}
		if (model.getLoginUrl() != null) {
			jsonObject.put("loginUrl", model.getLoginUrl());
		}
		if (model.getTicket() != null) {
			jsonObject.put("ticket", model.getTicket());
		}
		if (model.getProgramId() != null) {
			jsonObject.put("programId", model.getProgramId());
		}
		if (model.getProgramName() != null) {
			jsonObject.put("programName", model.getProgramName());
		}
		if (model.getUserNameKey() != null) {
			jsonObject.put("userNameKey", model.getUserNameKey());
		}
		jsonObject.put("serverId", model.getServerId());

		if (model.getSysId() != null) {
			jsonObject.put("sysId", model.getSysId());
		}

		if (model.getQueueName() != null) {
			jsonObject.put("queueName", model.getQueueName());
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

		if (model.getRunType() != null) {
			jsonObject.put("runType", model.getRunType());
		}

		if (model.getUseType() != null) {
			jsonObject.put("useType", model.getUseType());
		}
		if (model.getToken() != null) {
			jsonObject.put("token", model.getToken());
		}
		jsonObject.put("level", model.getLevel());
		jsonObject.put("priority", model.getPriority());
		jsonObject.put("operation", model.getOperation());
		jsonObject.put("intToken", model.getIntToken());
		jsonObject.put("sort", model.getSort());

		if (model.getProviderClass() != null) {
			jsonObject.put("providerClass", model.getProviderClass());
		}

		if (model.getRemoteUrl() != null) {
			jsonObject.put("remoteUrl", model.getRemoteUrl());
		}

		if (model.getActive() != null) {
			jsonObject.put("active", model.getActive());
		}
		if (model.getVerify() != null) {
			jsonObject.put("verify", model.getVerify());
		}
		if (model.getInitFlag() != null) {
			jsonObject.put("initFlag", model.getInitFlag());
		}
		if (model.getRemoveFlag() != null) {
			jsonObject.put("removeFlag", model.getRemoveFlag());
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

		java.util.List<DatabaseAccess> list = model.getAccesses();
		if (list != null && !list.isEmpty()) {
			ArrayNode array = new ObjectMapper().createArrayNode();
			if (list != null && !list.isEmpty()) {
				for (DatabaseAccess da : list) {
					ObjectNode json = da.toObjectNode();
					array.add(json);
				}
				jsonObject.set("accesses", array);
			}
		}

		return jsonObject;
	}

	public static JSONArray listToArray(java.util.List<Database> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (Database model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static java.util.List<Database> arrayToList(JSONArray array) {
		java.util.List<Database> list = new java.util.ArrayList<Database>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			Database model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	private DatabaseJsonFactory() {

	}

}
