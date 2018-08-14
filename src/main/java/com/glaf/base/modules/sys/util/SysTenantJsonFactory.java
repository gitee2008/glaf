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
public class SysTenantJsonFactory {

	public static java.util.List<SysTenant> arrayToList(JSONArray array) {
		java.util.List<SysTenant> list = new java.util.ArrayList<SysTenant>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			SysTenant model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	public static SysTenant jsonToObject(JSONObject jsonObject) {
		SysTenant model = new SysTenant();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getLong("id"));
		}
		if (jsonObject.containsKey("name")) {
			model.setName(jsonObject.getString("name"));
		}
		if (jsonObject.containsKey("namePinyin")) {
			model.setNamePinyin(jsonObject.getString("namePinyin"));
		}
		if (jsonObject.containsKey("code")) {
			model.setCode(jsonObject.getString("code"));
		}
		if (jsonObject.containsKey("tenantId")) {
			model.setTenantId(jsonObject.getString("tenantId"));
		}
		if (jsonObject.containsKey("tenantType")) {
			model.setTenantType(jsonObject.getInteger("tenantType"));
		}
		if (jsonObject.containsKey("databaseId")) {
			model.setDatabaseId(jsonObject.getLong("databaseId"));
		}
		if (jsonObject.containsKey("level")) {
			model.setLevel(jsonObject.getInteger("level"));
		}
		if (jsonObject.containsKey("province")) {
			model.setProvince(jsonObject.getString("province"));
		}
		if (jsonObject.containsKey("provinceId")) {
			model.setProvinceId(jsonObject.getLong("provinceId"));
		}
		if (jsonObject.containsKey("city")) {
			model.setCity(jsonObject.getString("city"));
		}
		if (jsonObject.containsKey("cityId")) {
			model.setCityId(jsonObject.getLong("cityId"));
		}
		if (jsonObject.containsKey("area")) {
			model.setArea(jsonObject.getString("area"));
		}
		if (jsonObject.containsKey("areaId")) {
			model.setAreaId(jsonObject.getLong("areaId"));
		}
		if (jsonObject.containsKey("town")) {
			model.setTown(jsonObject.getString("town"));
		}
		if (jsonObject.containsKey("townId")) {
			model.setTownId(jsonObject.getLong("townId"));
		}
		if (jsonObject.containsKey("principal")) {
			model.setPrincipal(jsonObject.getString("principal"));
		}
		if (jsonObject.containsKey("telephone")) {
			model.setTelephone(jsonObject.getString("telephone"));
		}
		if (jsonObject.containsKey("address")) {
			model.setAddress(jsonObject.getString("address"));
		}
		if (jsonObject.containsKey("property")) {
			model.setProperty(jsonObject.getString("property"));
		}
		if (jsonObject.containsKey("verify")) {
			model.setVerify(jsonObject.getString("verify"));
		}
		if (jsonObject.containsKey("ticketFlag")) {
			model.setTicketFlag(jsonObject.getString("ticketFlag"));
		}
		if (jsonObject.containsKey("type")) {
			model.setType(jsonObject.getString("type"));
		}
		if (jsonObject.containsKey("locked")) {
			model.setLocked(jsonObject.getInteger("locked"));
		}
		if (jsonObject.containsKey("limit")) {
			model.setLimit(jsonObject.getInteger("limit"));
		}
		if (jsonObject.containsKey("disableDataConstraint")) {
			model.setDisableDataConstraint(jsonObject.getString("disableDataConstraint"));
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

		return model;
	}

	public static JSONArray listToArray(java.util.List<SysTenant> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (SysTenant model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static JSONObject toJsonObject(SysTenant model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getNamePinyin() != null) {
			jsonObject.put("namePinyin", model.getNamePinyin());
		}
		if (model.getCode() != null) {
			jsonObject.put("code", model.getCode());
		}
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		jsonObject.put("tenantType", model.getTenantType());
		jsonObject.put("databaseId", model.getDatabaseId());
		jsonObject.put("level", model.getLevel());

		if (model.getProvince() != null) {
			jsonObject.put("province", model.getProvince());
		}
		jsonObject.put("provinceId", model.getProvinceId());
		if (model.getCity() != null) {
			jsonObject.put("city", model.getCity());
		}
		jsonObject.put("cityId", model.getCityId());
		if (model.getArea() != null) {
			jsonObject.put("area", model.getArea());
		}
		jsonObject.put("areaId", model.getAreaId());
		if (model.getTown() != null) {
			jsonObject.put("town", model.getTown());
		}
		jsonObject.put("townId", model.getTownId());
		if (model.getPrincipal() != null) {
			jsonObject.put("principal", model.getPrincipal());
		}
		if (model.getTelephone() != null) {
			jsonObject.put("telephone", model.getTelephone());
		}
		if (model.getAddress() != null) {
			jsonObject.put("address", model.getAddress());
		}
		if (model.getProperty() != null) {
			jsonObject.put("property", model.getProperty());
		}
		if (model.getVerify() != null) {
			jsonObject.put("verify", model.getVerify());
		}
		if (model.getTicketFlag() != null) {
			jsonObject.put("ticketFlag", model.getTicketFlag());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		jsonObject.put("limit", model.getLimit());
		jsonObject.put("locked", model.getLocked());

		if (model.getDisableDataConstraint() != null) {
			jsonObject.put("disableDataConstraint", model.getDisableDataConstraint());
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

	public static ObjectNode toObjectNode(SysTenant model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getNamePinyin() != null) {
			jsonObject.put("namePinyin", model.getNamePinyin());
		}
		if (model.getCode() != null) {
			jsonObject.put("code", model.getCode());
		}
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		jsonObject.put("tenantType", model.getTenantType());
		jsonObject.put("databaseId", model.getDatabaseId());
		jsonObject.put("level", model.getLevel());

		if (model.getProvince() != null) {
			jsonObject.put("province", model.getProvince());
		}
		jsonObject.put("provinceId", model.getProvinceId());
		if (model.getCity() != null) {
			jsonObject.put("city", model.getCity());
		}
		jsonObject.put("cityId", model.getCityId());
		if (model.getArea() != null) {
			jsonObject.put("area", model.getArea());
		}
		jsonObject.put("areaId", model.getAreaId());
		if (model.getTown() != null) {
			jsonObject.put("town", model.getTown());
		}
		jsonObject.put("townId", model.getTownId());
		if (model.getPrincipal() != null) {
			jsonObject.put("principal", model.getPrincipal());
		}
		if (model.getTelephone() != null) {
			jsonObject.put("telephone", model.getTelephone());
		}
		if (model.getAddress() != null) {
			jsonObject.put("address", model.getAddress());
		}
		if (model.getProperty() != null) {
			jsonObject.put("property", model.getProperty());
		}
		if (model.getVerify() != null) {
			jsonObject.put("verify", model.getVerify());
		}
		if (model.getTicketFlag() != null) {
			jsonObject.put("ticketFlag", model.getTicketFlag());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		jsonObject.put("locked", model.getLocked());

		if (model.getDisableDataConstraint() != null) {
			jsonObject.put("disableDataConstraint", model.getDisableDataConstraint());
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

	private SysTenantJsonFactory() {

	}

}
