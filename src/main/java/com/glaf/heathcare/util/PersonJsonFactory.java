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
public class PersonJsonFactory {

	public static java.util.List<Person> arrayToList(JSONArray array) {
		java.util.List<Person> list = new java.util.ArrayList<Person>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			Person model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	public static Person jsonToObject(JSONObject jsonObject) {
		Person model = new Person();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getString("id"));
		}
		if (jsonObject.containsKey("tenantId")) {
			model.setTenantId(jsonObject.getString("tenantId"));
		}
		if (jsonObject.containsKey("gradeId")) {
			model.setGradeId(jsonObject.getString("gradeId"));
		}
		if (jsonObject.containsKey("name")) {
			model.setName(jsonObject.getString("name"));
		}
		if (jsonObject.containsKey("idCardNo")) {
			model.setIdCardNo(jsonObject.getString("idCardNo"));
		}
		if (jsonObject.containsKey("patriarch")) {
			model.setPatriarch(jsonObject.getString("patriarch"));
		}
		if (jsonObject.containsKey("telephone")) {
			model.setTelephone(jsonObject.getString("telephone"));
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
		if (jsonObject.containsKey("homeAddress")) {
			model.setHomeAddress(jsonObject.getString("homeAddress"));
		}
		if (jsonObject.containsKey("birthAddress")) {
			model.setBirthAddress(jsonObject.getString("birthAddress"));
		}
		if (jsonObject.containsKey("sex")) {
			model.setSex(jsonObject.getString("sex"));
		}
		if (jsonObject.containsKey("birthday")) {
			model.setBirthday(jsonObject.getDate("birthday"));
		}
		if (jsonObject.containsKey("year")) {
			model.setYear(jsonObject.getInteger("year"));
		}
		if (jsonObject.containsKey("joinDate")) {
			model.setJoinDate(jsonObject.getDate("joinDate"));
		}
		if (jsonObject.containsKey("healthCondition")) {
			model.setHealthCondition(jsonObject.getString("healthCondition"));
		}
		if (jsonObject.containsKey("allergy")) {
			model.setAllergy(jsonObject.getString("allergy"));
		}
		if (jsonObject.containsKey("feedingHistory")) {
			model.setFeedingHistory(jsonObject.getString("feedingHistory"));
		}
		if (jsonObject.containsKey("previousHistory")) {
			model.setPreviousHistory(jsonObject.getString("previousHistory"));
		}
		if (jsonObject.containsKey("foodAllergy")) {
			model.setFoodAllergy(jsonObject.getString("foodAllergy"));
		}
		if (jsonObject.containsKey("medicineAllergy")) {
			model.setMedicineAllergy(jsonObject.getString("medicineAllergy"));
		}
		if (jsonObject.containsKey("height")) {
			model.setHeight(jsonObject.getDouble("height"));
		}
		if (jsonObject.containsKey("weight")) {
			model.setWeight(jsonObject.getDouble("weight"));
		}
		if (jsonObject.containsKey("father")) {
			model.setFather(jsonObject.getString("father"));
		}
		if (jsonObject.containsKey("fatherCompany")) {
			model.setFatherCompany(jsonObject.getString("fatherCompany"));
		}
		if (jsonObject.containsKey("fatherTelephone")) {
			model.setFatherTelephone(jsonObject.getString("fatherTelephone"));
		}
		if (jsonObject.containsKey("fatherWardship")) {
			model.setFatherWardship(jsonObject.getString("fatherWardship"));
		}
		if (jsonObject.containsKey("mother")) {
			model.setMother(jsonObject.getString("mother"));
		}
		if (jsonObject.containsKey("motherCompany")) {
			model.setMotherCompany(jsonObject.getString("motherCompany"));
		}
		if (jsonObject.containsKey("motherTelephone")) {
			model.setMotherTelephone(jsonObject.getString("motherTelephone"));
		}
		if (jsonObject.containsKey("motherWardship")) {
			model.setMotherWardship(jsonObject.getString("motherWardship"));
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
		if (jsonObject.containsKey("updateBy")) {
			model.setUpdateBy(jsonObject.getString("updateBy"));
		}
		if (jsonObject.containsKey("updateTime")) {
			model.setUpdateTime(jsonObject.getDate("updateTime"));
		}
		if (jsonObject.containsKey("deleteFlag")) {
			model.setDeleteFlag(jsonObject.getInteger("deleteFlag"));
		}

		return model;
	}

	public static JSONArray listToArray(java.util.List<Person> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (Person model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static JSONObject toJsonObject(Person model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		if (model.getGradeId() != null) {
			jsonObject.put("gradeId", model.getGradeId());
		}
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getIdCardNo() != null) {
			jsonObject.put("idCardNo", model.getIdCardNo());
		}
		if (model.getPatriarch() != null) {
			jsonObject.put("patriarch", model.getPatriarch());
		}
		if (model.getTelephone() != null) {
			jsonObject.put("telephone", model.getTelephone());
		}
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
		if (model.getHomeAddress() != null) {
			jsonObject.put("homeAddress", model.getHomeAddress());
		}
		if (model.getBirthAddress() != null) {
			jsonObject.put("birthAddress", model.getBirthAddress());
		}
		if (model.getSex() != null) {
			jsonObject.put("sex", model.getSex());
		}
		if (model.getBirthday() != null) {
			jsonObject.put("birthday", DateUtils.getDate(model.getBirthday()));
			jsonObject.put("birthday_date", DateUtils.getDate(model.getBirthday()));
			jsonObject.put("birthday_datetime", DateUtils.getDateTime(model.getBirthday()));
		}
		jsonObject.put("year", model.getYear());

		if (model.getJoinDate() != null) {
			jsonObject.put("joinDate", DateUtils.getDate(model.getJoinDate()));
			jsonObject.put("joinDate_date", DateUtils.getDate(model.getJoinDate()));
			jsonObject.put("joinDate_datetime", DateUtils.getDateTime(model.getJoinDate()));
		}
		if (model.getHealthCondition() != null) {
			jsonObject.put("healthCondition", model.getHealthCondition());
		}
		if (model.getAllergy() != null) {
			jsonObject.put("allergy", model.getAllergy());
		}
		if (model.getFeedingHistory() != null) {
			jsonObject.put("feedingHistory", model.getFeedingHistory());
		}
		if (model.getPreviousHistory() != null) {
			jsonObject.put("previousHistory", model.getPreviousHistory());
		}
		if (model.getFoodAllergy() != null) {
			jsonObject.put("foodAllergy", model.getFoodAllergy());
		}
		if (model.getMedicineAllergy() != null) {
			jsonObject.put("medicineAllergy", model.getMedicineAllergy());
		}
		jsonObject.put("height", model.getHeight());
		jsonObject.put("weight", model.getWeight());
		if (model.getFather() != null) {
			jsonObject.put("father", model.getFather());
		}
		if (model.getFatherCompany() != null) {
			jsonObject.put("fatherCompany", model.getFatherCompany());
		}
		if (model.getFatherTelephone() != null) {
			jsonObject.put("fatherTelephone", model.getFatherTelephone());
		}
		if (model.getFatherWardship() != null) {
			jsonObject.put("fatherWardship", model.getFatherWardship());
		}
		if (model.getMother() != null) {
			jsonObject.put("mother", model.getMother());
		}
		if (model.getMotherCompany() != null) {
			jsonObject.put("motherCompany", model.getMotherCompany());
		}
		if (model.getMotherTelephone() != null) {
			jsonObject.put("motherTelephone", model.getMotherTelephone());
		}
		if (model.getMotherWardship() != null) {
			jsonObject.put("motherWardship", model.getMotherWardship());
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
		if (model.getUpdateBy() != null) {
			jsonObject.put("updateBy", model.getUpdateBy());
		}
		if (model.getUpdateTime() != null) {
			jsonObject.put("updateTime", DateUtils.getDate(model.getUpdateTime()));
			jsonObject.put("updateTime_date", DateUtils.getDate(model.getUpdateTime()));
			jsonObject.put("updateTime_datetime", DateUtils.getDateTime(model.getUpdateTime()));
		}
		jsonObject.put("deleteFlag", model.getDeleteFlag());
		return jsonObject;
	}

	public static ObjectNode toObjectNode(Person model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		if (model.getGradeId() != null) {
			jsonObject.put("gradeId", model.getGradeId());
		}
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getIdCardNo() != null) {
			jsonObject.put("idCardNo", model.getIdCardNo());
		}
		if (model.getPatriarch() != null) {
			jsonObject.put("patriarch", model.getPatriarch());
		}
		if (model.getTelephone() != null) {
			jsonObject.put("telephone", model.getTelephone());
		}
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
		if (model.getHomeAddress() != null) {
			jsonObject.put("homeAddress", model.getHomeAddress());
		}
		if (model.getBirthAddress() != null) {
			jsonObject.put("birthAddress", model.getBirthAddress());
		}
		if (model.getSex() != null) {
			jsonObject.put("sex", model.getSex());
		}
		if (model.getBirthday() != null) {
			jsonObject.put("birthday", DateUtils.getDate(model.getBirthday()));
			jsonObject.put("birthday_date", DateUtils.getDate(model.getBirthday()));
			jsonObject.put("birthday_datetime", DateUtils.getDateTime(model.getBirthday()));
		}
		jsonObject.put("year", model.getYear());

		if (model.getJoinDate() != null) {
			jsonObject.put("joinDate", DateUtils.getDate(model.getJoinDate()));
			jsonObject.put("joinDate_date", DateUtils.getDate(model.getJoinDate()));
			jsonObject.put("joinDate_datetime", DateUtils.getDateTime(model.getJoinDate()));
		}
		if (model.getHealthCondition() != null) {
			jsonObject.put("healthCondition", model.getHealthCondition());
		}
		if (model.getAllergy() != null) {
			jsonObject.put("allergy", model.getAllergy());
		}
		if (model.getFeedingHistory() != null) {
			jsonObject.put("feedingHistory", model.getFeedingHistory());
		}
		if (model.getPreviousHistory() != null) {
			jsonObject.put("previousHistory", model.getPreviousHistory());
		}
		if (model.getFoodAllergy() != null) {
			jsonObject.put("foodAllergy", model.getFoodAllergy());
		}
		if (model.getMedicineAllergy() != null) {
			jsonObject.put("medicineAllergy", model.getMedicineAllergy());
		}
		jsonObject.put("height", model.getHeight());
		jsonObject.put("weight", model.getWeight());
		if (model.getFather() != null) {
			jsonObject.put("father", model.getFather());
		}
		if (model.getFatherCompany() != null) {
			jsonObject.put("fatherCompany", model.getFatherCompany());
		}
		if (model.getFatherTelephone() != null) {
			jsonObject.put("fatherTelephone", model.getFatherTelephone());
		}
		if (model.getFatherWardship() != null) {
			jsonObject.put("fatherWardship", model.getFatherWardship());
		}
		if (model.getMother() != null) {
			jsonObject.put("mother", model.getMother());
		}
		if (model.getMotherCompany() != null) {
			jsonObject.put("motherCompany", model.getMotherCompany());
		}
		if (model.getMotherTelephone() != null) {
			jsonObject.put("motherTelephone", model.getMotherTelephone());
		}
		if (model.getMotherWardship() != null) {
			jsonObject.put("motherWardship", model.getMotherWardship());
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
		if (model.getUpdateBy() != null) {
			jsonObject.put("updateBy", model.getUpdateBy());
		}
		if (model.getUpdateTime() != null) {
			jsonObject.put("updateTime", DateUtils.getDate(model.getUpdateTime()));
			jsonObject.put("updateTime_date", DateUtils.getDate(model.getUpdateTime()));
			jsonObject.put("updateTime_datetime", DateUtils.getDateTime(model.getUpdateTime()));
		}
		jsonObject.put("deleteFlag", model.getDeleteFlag());
		return jsonObject;
	}

	private PersonJsonFactory() {

	}

}
