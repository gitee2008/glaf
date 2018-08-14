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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.base.modules.sys.model.SysRole;
import com.glaf.base.modules.sys.model.SysUser;
import com.glaf.base.modules.sys.model.SysUserRole;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.RequestUtils;

public class SysUserJsonFactory {

	public static java.util.List<SysUser> arrayToList(JSONArray array) {
		java.util.List<SysUser> list = new java.util.ArrayList<SysUser>();
		for (int i = 0; i < array.size(); i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			SysUser model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	public static SysUser jsonToObject(JSONObject jsonObject) {
		SysUser model = new SysUser();

		if (jsonObject.containsKey("actorId")) {
			model.setActorId(jsonObject.getString("actorId"));
		}
		if (jsonObject.containsKey("account")) {
			model.setActorId(jsonObject.getString("account"));
		}
		if (jsonObject.containsKey("name")) {
			model.setName(jsonObject.getString("name"));
		}
		if (jsonObject.containsKey("namePinyin")) {
			model.setNamePinyin(jsonObject.getString("namePinyin"));
		}
		if (jsonObject.containsKey("evection")) {
			model.setEvection(jsonObject.getIntValue("evection"));
		}

		if (jsonObject.containsKey("sex")) {
			model.setSex(jsonObject.getIntValue("sex"));
		}

		if (jsonObject.containsKey("userType")) {
			model.setUserType(jsonObject.getIntValue("userType"));
		}

		if (jsonObject.containsKey("accountType")) {
			model.setAccountType(jsonObject.getIntValue("accountType"));
		}

		if (jsonObject.containsKey("lastLoginTime")) {
			model.setLastLoginTime(jsonObject.getDate("lastLoginTime"));
		}

		if (jsonObject.containsKey("lastLoginIP")) {
			model.setLastLoginIP(jsonObject.getString("lastLoginIP"));
		}

		if (jsonObject.containsKey("secretLoginFlag")) {
			model.setSecretLoginFlag(jsonObject.getString("secretLoginFlag"));
		}

		if (jsonObject.containsKey("remark")) {
			model.setRemark(jsonObject.getString("remark"));
		}

		if (jsonObject.containsKey("organizationId")) {
			model.setOrganizationId(jsonObject.getLong("organizationId"));
		}

		if (jsonObject.containsKey("tenantId")) {
			model.setTenantId(jsonObject.getString("tenantId"));
		}

		if (jsonObject.containsKey("mail")) {
			model.setMail(jsonObject.getString("mail"));
		}

		if (jsonObject.containsKey("mobile")) {
			model.setMobile(jsonObject.getString("mobile"));
		}

		if (jsonObject.containsKey("mobileVerifyFlag")) {
			model.setMobileVerifyFlag(jsonObject.getString("mobileVerifyFlag"));
		}

		if (jsonObject.containsKey("telephone")) {
			model.setTelephone(jsonObject.getString("telephone"));
		}

		if (jsonObject.containsKey("headship")) {
			model.setHeadship(jsonObject.getString("headship"));
		}

		if (jsonObject.containsKey("locked")) {
			model.setLocked(jsonObject.getInteger("locked"));
		}

		if (jsonObject.containsKey("adminFlag")) {
			model.setAdminFlag(jsonObject.getString("adminFlag"));
		}

		if (jsonObject.containsKey("lastLoginTime")) {
			model.setLastLoginTime(jsonObject.getDate("lastLoginTime"));
		}
		if (jsonObject.containsKey("lastLoginIP")) {
			model.setLastLoginIP(jsonObject.getString("lastLoginIP"));
		}
		if (jsonObject.containsKey("lockLoginTime")) {
			model.setLockLoginTime(jsonObject.getDate("lockLoginTime"));
		}
		if (jsonObject.containsKey("loginRetry")) {
			model.setLoginRetry(jsonObject.getInteger("loginRetry"));
		}

		if (jsonObject.containsKey("loginSecretUpdateTime")) {
			model.setLoginSecretUpdateTime(jsonObject.getDate("loginSecretUpdateTime"));
		}

		if (jsonObject.containsKey("token")) {
			model.setToken(jsonObject.getString("token"));
		}

		if (jsonObject.containsKey("roles")) {
			JSONArray array = jsonObject.getJSONArray("roles");
			if (array != null && !array.isEmpty()) {
				for (int i = 0; i < array.size(); i++) {
					JSONObject json = array.getJSONObject(i);
					SysRole role = SysRoleJsonFactory.jsonToObject(json);
					model.getRoles().add(role);
				}
			}
		}

		if (jsonObject.containsKey("userRoles")) {
			JSONArray array = jsonObject.getJSONArray("userRoles");
			if (array != null && !array.isEmpty()) {
				for (int i = 0; i < array.size(); i++) {
					JSONObject json = array.getJSONObject(i);
					SysUserRole userrole = SysUserRoleJsonFactory.jsonToObject(json);
					model.getUserRoles().add(userrole);
				}
			}
		}

		return model;
	}

	public static JSONArray listToArray(java.util.List<SysUser> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (SysUser model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static JSONObject toJsonObject(SysUser user) {
		JSONObject jsonObject = new JSONObject();

		jsonObject.put("actorId", user.getUserId());
		jsonObject.put("actorId_enc", RequestUtils.encodeString(user.getUserId()));
		jsonObject.put("userId", user.getUserId());
		jsonObject.put("userId_enc", RequestUtils.encodeString(user.getUserId()));

		jsonObject.put("name", user.getName());
		if (user.getNamePinyin() != null) {
			jsonObject.put("namePinyin", user.getNamePinyin());
		}
		jsonObject.put("locked", user.getLocked());

		jsonObject.put("accountType", user.getAccountType());
		jsonObject.put("userType", user.getUserType());
		jsonObject.put("tenantId", user.getTenantId());
		jsonObject.put("organizationId", user.getOrganizationId());

		jsonObject.put("sex", user.getSex());
		jsonObject.put("evection", user.getEvection());

		jsonObject.put("fax", user.getFax());
		jsonObject.put("telephone", user.getTelephone());
		jsonObject.put("headship", user.getHeadship());
		jsonObject.put("adminFlag", user.getAdminFlag());

		if (user.getEmail() != null) {
			jsonObject.put("mail", user.getEmail());
			jsonObject.put("email", user.getEmail());
		}
		if (user.getMobile() != null) {
			jsonObject.put("mobile", user.getMobile());
		}
		if (user.getMobileVerifyFlag() != null) {
			jsonObject.put("mobileVerifyFlag", user.getMobileVerifyFlag());
		}
		if (user.getLastLoginTime() != null) {
			jsonObject.put("lastLoginDate", DateUtils.getDateTime(user.getLastLoginDate()));
			jsonObject.put("lastLoginTime", DateUtils.getDateTime(user.getLastLoginDate()));
		}

		if (user.getLastLoginIP() != null) {
			jsonObject.put("loginIP", user.getLastLoginIP());
		}

		if (user.getSecretLoginFlag() != null) {
			jsonObject.put("secretLoginFlag", user.getSecretLoginFlag());
		}

		if (user.getToken() != null) {
			jsonObject.put("token", user.getToken());
		}

		if (user.getCreateBy() != null) {
			jsonObject.put("createBy", user.getCreateBy());
		}

		if (user.getCreateTime() != null) {
			jsonObject.put("createTime", DateUtils.getDate(user.getCreateTime()));
			jsonObject.put("createDate", DateUtils.getDate(user.getCreateTime()));
			jsonObject.put("createDate_date", DateUtils.getDate(user.getCreateTime()));
			jsonObject.put("createDate_datetime", DateUtils.getDateTime(user.getCreateTime()));
		}

		if (user.getUpdateBy() != null) {
			jsonObject.put("updateBy", user.getUpdateBy());
		}
		if (user.getUpdateDate() != null) {
			jsonObject.put("updateDate", DateUtils.getDate(user.getUpdateDate()));
			jsonObject.put("updateDate_date", DateUtils.getDate(user.getUpdateDate()));
			jsonObject.put("updateDate_datetime", DateUtils.getDateTime(user.getUpdateDate()));
		}

		if (user.getLastLoginTime() != null) {
			jsonObject.put("lastLoginTime", DateUtils.getDate(user.getLastLoginTime()));
			jsonObject.put("lastLoginTime_date", DateUtils.getDate(user.getLastLoginTime()));
			jsonObject.put("lastLoginTime_datetime", DateUtils.getDateTime(user.getLastLoginTime()));
		}
		if (user.getLastLoginIP() != null) {
			jsonObject.put("lastLoginIP", user.getLastLoginIP());
		}
		if (user.getLockLoginTime() != null) {
			jsonObject.put("lockLoginTime", DateUtils.getDate(user.getLockLoginTime()));
			jsonObject.put("lockLoginTime_date", DateUtils.getDate(user.getLockLoginTime()));
			jsonObject.put("lockLoginTime_datetime", DateUtils.getDateTime(user.getLockLoginTime()));
		}
		jsonObject.put("loginRetry", user.getLoginRetry());

		if (user.getLoginSecretUpdateTime() != null) {
			jsonObject.put("loginSecretUpdateTime", DateUtils.getDate(user.getLoginSecretUpdateTime()));
			jsonObject.put("loginSecretUpdateTime_date", DateUtils.getDate(user.getLoginSecretUpdateTime()));
			jsonObject.put("loginSecretUpdateTime_datetime", DateUtils.getDateTime(user.getLoginSecretUpdateTime()));
		}

		if (user.getRoles() != null && !user.getRoles().isEmpty()) {
			JSONArray array = new JSONArray();
			for (SysRole sysole : user.getRoles()) {
				array.add(sysole.toJsonObject());
			}
			jsonObject.put("roles", array);
		}

		if (user.getUserRoles() != null && !user.getUserRoles().isEmpty()) {
			JSONArray array = new JSONArray();
			for (SysUserRole sysUserRole : user.getUserRoles()) {
				array.add(sysUserRole.toJsonObject());
			}
			jsonObject.put("userRoles", array);
		}

		return jsonObject;
	}

	public static ObjectNode toObjectNode(SysUser user) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();

		jsonObject.put("actorId", user.getUserId());
		jsonObject.put("actorId_enc", RequestUtils.encodeString(user.getUserId()));
		jsonObject.put("userId", user.getUserId());
		jsonObject.put("userId_enc", RequestUtils.encodeString(user.getUserId()));

		jsonObject.put("name", user.getName());
		if (user.getNamePinyin() != null) {
			jsonObject.put("namePinyin", user.getNamePinyin());
		}
		jsonObject.put("locked", user.getLocked());

		jsonObject.put("accountType", user.getAccountType());
		jsonObject.put("userType", user.getUserType());
		jsonObject.put("tenantId", user.getTenantId());
		jsonObject.put("organizationId", user.getOrganizationId());

		jsonObject.put("sex", user.getSex());
		jsonObject.put("evection", user.getEvection());

		jsonObject.put("fax", user.getFax());
		jsonObject.put("telephone", user.getTelephone());
		jsonObject.put("headship", user.getHeadship());
		jsonObject.put("adminFlag", user.getAdminFlag());

		if (user.getEmail() != null) {
			jsonObject.put("mail", user.getEmail());
			jsonObject.put("email", user.getEmail());
		}
		if (user.getMobile() != null) {
			jsonObject.put("mobile", user.getMobile());
		}
		if (user.getMobileVerifyFlag() != null) {
			jsonObject.put("mobileVerifyFlag", user.getMobileVerifyFlag());
		}
		if (user.getToken() != null) {
			jsonObject.put("token", user.getToken());
		}
		if (user.getLastLoginTime() != null) {
			jsonObject.put("lastLoginDate", DateUtils.getDateTime(user.getLastLoginDate()));
			jsonObject.put("lastLoginTime", DateUtils.getDateTime(user.getLastLoginDate()));
		}
		if (user.getLastLoginIP() != null) {
			jsonObject.put("loginIP", user.getLastLoginIP());
		}

		if (user.getSecretLoginFlag() != null) {
			jsonObject.put("secretLoginFlag", user.getSecretLoginFlag());
		}

		if (user.getLoginSecretUpdateTime() != null) {
			jsonObject.put("loginSecretUpdateTime", DateUtils.getDate(user.getLoginSecretUpdateTime()));
			jsonObject.put("loginSecretUpdateTime_date", DateUtils.getDate(user.getLoginSecretUpdateTime()));
			jsonObject.put("loginSecretUpdateTime_datetime", DateUtils.getDateTime(user.getLoginSecretUpdateTime()));
		}

		if (user.getCreateBy() != null) {
			jsonObject.put("createBy", user.getCreateBy());
		}
		if (user.getUpdateBy() != null) {
			jsonObject.put("updateBy", user.getUpdateBy());
		}
		if (user.getUpdateDate() != null) {
			jsonObject.put("updateDate", DateUtils.getDate(user.getUpdateDate()));
			jsonObject.put("updateDate_date", DateUtils.getDate(user.getUpdateDate()));
			jsonObject.put("updateDate_datetime", DateUtils.getDateTime(user.getUpdateDate()));
		}

		if (user.getLastLoginTime() != null) {
			jsonObject.put("lastLoginTime", DateUtils.getDate(user.getLastLoginTime()));
			jsonObject.put("lastLoginTime_date", DateUtils.getDate(user.getLastLoginTime()));
			jsonObject.put("lastLoginTime_datetime", DateUtils.getDateTime(user.getLastLoginTime()));
		}
		if (user.getLastLoginIP() != null) {
			jsonObject.put("lastLoginIP", user.getLastLoginIP());
		}
		if (user.getLockLoginTime() != null) {
			jsonObject.put("lockLoginTime", DateUtils.getDate(user.getLockLoginTime()));
			jsonObject.put("lockLoginTime_date", DateUtils.getDate(user.getLockLoginTime()));
			jsonObject.put("lockLoginTime_datetime", DateUtils.getDateTime(user.getLockLoginTime()));
		}
		jsonObject.put("loginRetry", user.getLoginRetry());

		if (user.getRoles() != null && !user.getRoles().isEmpty()) {
			ArrayNode array = new ObjectMapper().createArrayNode();
			for (SysRole sysRole : user.getRoles()) {
				array.add(sysRole.toObjectNode());
			}
			jsonObject.set("roles", array);
		}

		if (user.getUserRoles() != null && !user.getUserRoles().isEmpty()) {
			ArrayNode array = new ObjectMapper().createArrayNode();
			for (SysUserRole sysUserRole : user.getUserRoles()) {
				array.add(sysUserRole.toObjectNode());
			}
			jsonObject.set("userRoles", array);
		}

		return jsonObject;
	}

	private SysUserJsonFactory() {

	}

}
