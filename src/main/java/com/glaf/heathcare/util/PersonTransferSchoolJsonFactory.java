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

package com.glaf.heathcare.util;

import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.glaf.core.util.DateUtils;
import com.glaf.heathcare.domain.*;

/**
 * 
 * JSON工厂类
 *
 */
public class PersonTransferSchoolJsonFactory {

	public static PersonTransferSchool jsonToObject(JSONObject jsonObject) {
		PersonTransferSchool model = new PersonTransferSchool();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getString("id"));
		}
		if (jsonObject.containsKey("tenantId")) {
			model.setTenantId(jsonObject.getString("tenantId"));
		}
		if (jsonObject.containsKey("fromTenantId")) {
			model.setFromTenantId(jsonObject.getString("fromTenantId"));
		}
		if (jsonObject.containsKey("fromSchool")) {
			model.setFromSchool(jsonObject.getString("fromSchool"));
		}
		if (jsonObject.containsKey("toTenantId")) {
			model.setToTenantId(jsonObject.getString("toTenantId"));
		}
		if (jsonObject.containsKey("toSchool")) {
			model.setToSchool(jsonObject.getString("toSchool"));
		}
		if (jsonObject.containsKey("personId")) {
			model.setPersonId(jsonObject.getString("personId"));
		}
		if (jsonObject.containsKey("name")) {
			model.setName(jsonObject.getString("name"));
		}
		if (jsonObject.containsKey("sex")) {
			model.setSex(jsonObject.getString("sex"));
		}
		if (jsonObject.containsKey("checkDate")) {
			model.setCheckDate(jsonObject.getDate("checkDate"));
		}
		if (jsonObject.containsKey("checkOrganization")) {
			model.setCheckOrganization(jsonObject.getString("checkOrganization"));
		}
		if (jsonObject.containsKey("checkOrganizationId")) {
			model.setCheckOrganizationId(jsonObject.getLong("checkOrganizationId"));
		}
		if (jsonObject.containsKey("checkResult")) {
			model.setCheckResult(jsonObject.getString("checkResult"));
		}
		if (jsonObject.containsKey("remark")) {
			model.setRemark(jsonObject.getString("remark"));
		}
		if (jsonObject.containsKey("createBy")) {
			model.setCreateBy(jsonObject.getString("createBy"));
		}
		if (jsonObject.containsKey("createTime")) {
			model.setCreateTime(jsonObject.getDate("createTime"));
		}

		return model;
	}

	public static JSONObject toJsonObject(PersonTransferSchool model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		if (model.getFromTenantId() != null) {
			jsonObject.put("fromTenantId", model.getFromTenantId());
		}
		if (model.getFromSchool() != null) {
			jsonObject.put("fromSchool", model.getFromSchool());
		}
		if (model.getToTenantId() != null) {
			jsonObject.put("toTenantId", model.getToTenantId());
		}
		if (model.getToSchool() != null) {
			jsonObject.put("toSchool", model.getToSchool());
		}
		if (model.getPersonId() != null) {
			jsonObject.put("personId", model.getPersonId());
		}
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getSex() != null) {
			jsonObject.put("sex", model.getSex());
		}
		if (model.getCheckDate() != null) {
			jsonObject.put("checkDate", DateUtils.getDate(model.getCheckDate()));
			jsonObject.put("checkDate_date", DateUtils.getDate(model.getCheckDate()));
			jsonObject.put("checkDate_datetime", DateUtils.getDateTime(model.getCheckDate()));
		}
		if (model.getCheckOrganization() != null) {
			jsonObject.put("checkOrganization", model.getCheckOrganization());
		}
		jsonObject.put("checkOrganizationId", model.getCheckOrganizationId());
		if (model.getCheckResult() != null) {
			jsonObject.put("checkResult", model.getCheckResult());
		}
		if (model.getRemark() != null) {
			jsonObject.put("remark", model.getRemark());
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

	public static ObjectNode toObjectNode(PersonTransferSchool model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		if (model.getFromTenantId() != null) {
			jsonObject.put("fromTenantId", model.getFromTenantId());
		}
		if (model.getFromSchool() != null) {
			jsonObject.put("fromSchool", model.getFromSchool());
		}
		if (model.getToTenantId() != null) {
			jsonObject.put("toTenantId", model.getToTenantId());
		}
		if (model.getToSchool() != null) {
			jsonObject.put("toSchool", model.getToSchool());
		}
		if (model.getPersonId() != null) {
			jsonObject.put("personId", model.getPersonId());
		}
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getSex() != null) {
			jsonObject.put("sex", model.getSex());
		}
		if (model.getCheckDate() != null) {
			jsonObject.put("checkDate", DateUtils.getDate(model.getCheckDate()));
			jsonObject.put("checkDate_date", DateUtils.getDate(model.getCheckDate()));
			jsonObject.put("checkDate_datetime", DateUtils.getDateTime(model.getCheckDate()));
		}
		if (model.getCheckOrganization() != null) {
			jsonObject.put("checkOrganization", model.getCheckOrganization());
		}
		jsonObject.put("checkOrganizationId", model.getCheckOrganizationId());
		if (model.getCheckResult() != null) {
			jsonObject.put("checkResult", model.getCheckResult());
		}
		if (model.getRemark() != null) {
			jsonObject.put("remark", model.getRemark());
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

	public static JSONArray listToArray(java.util.List<PersonTransferSchool> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (PersonTransferSchool model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static java.util.List<PersonTransferSchool> arrayToList(JSONArray array) {
		java.util.List<PersonTransferSchool> list = new java.util.ArrayList<PersonTransferSchool>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			PersonTransferSchool model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	private PersonTransferSchoolJsonFactory() {

	}

}
