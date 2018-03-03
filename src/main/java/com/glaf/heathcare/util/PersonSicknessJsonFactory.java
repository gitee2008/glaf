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
public class PersonSicknessJsonFactory {

	public static PersonSickness jsonToObject(JSONObject jsonObject) {
		PersonSickness model = new PersonSickness();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getString("id"));
		}
		if (jsonObject.containsKey("tenantId")) {
			model.setTenantId(jsonObject.getString("tenantId"));
		}
		if (jsonObject.containsKey("gradeId")) {
			model.setGradeId(jsonObject.getString("gradeId"));
		}
		if (jsonObject.containsKey("personId")) {
			model.setPersonId(jsonObject.getString("personId"));
		}
		if (jsonObject.containsKey("name")) {
			model.setName(jsonObject.getString("name"));
		}
		if (jsonObject.containsKey("sickness")) {
			model.setSickness(jsonObject.getString("sickness"));
		}
		if (jsonObject.containsKey("infectiousFlag")) {
			model.setInfectiousFlag(jsonObject.getString("infectiousFlag"));
		}
		if (jsonObject.containsKey("infectiousDisease")) {
			model.setInfectiousDisease(jsonObject.getString("infectiousDisease"));
		}
		if (jsonObject.containsKey("discoverTime")) {
			model.setDiscoverTime(jsonObject.getDate("discoverTime"));
		}
		if (jsonObject.containsKey("reportTime")) {
			model.setReportTime(jsonObject.getDate("reportTime"));
		}
		if (jsonObject.containsKey("reportOrg")) {
			model.setReportOrg(jsonObject.getString("reportOrg"));
		}
		if (jsonObject.containsKey("reportResponsible")) {
			model.setReportResponsible(jsonObject.getString("reportResponsible"));
		}
		if (jsonObject.containsKey("reportWay")) {
			model.setReportWay(jsonObject.getString("reportWay"));
		}
		if (jsonObject.containsKey("supervisionOpinion")) {
			model.setSupervisionOpinion(jsonObject.getString("supervisionOpinion"));
		}
		if (jsonObject.containsKey("confirmTime")) {
			model.setConfirmTime(jsonObject.getDate("confirmTime"));
		}
		if (jsonObject.containsKey("receiver1")) {
			model.setReceiver1(jsonObject.getString("receiver1"));
		}
		if (jsonObject.containsKey("receiveOrg1")) {
			model.setReceiveOrg1(jsonObject.getString("receiveOrg1"));
		}
		if (jsonObject.containsKey("receiver2")) {
			model.setReceiver2(jsonObject.getString("receiver2"));
		}
		if (jsonObject.containsKey("receiveOrg2")) {
			model.setReceiveOrg2(jsonObject.getString("receiveOrg2"));
		}
		if (jsonObject.containsKey("symptom")) {
			model.setSymptom(jsonObject.getString("symptom"));
		}
		if (jsonObject.containsKey("treat")) {
			model.setTreat(jsonObject.getString("treat"));
		}
		if (jsonObject.containsKey("hospital")) {
			model.setHospital(jsonObject.getString("hospital"));
		}
		if (jsonObject.containsKey("clinicTime")) {
			model.setClinicTime(jsonObject.getDate("clinicTime"));
		}
		if (jsonObject.containsKey("result")) {
			model.setResult(jsonObject.getString("result"));
		}
		if (jsonObject.containsKey("remark")) {
			model.setRemark(jsonObject.getString("remark"));
		}
		if (jsonObject.containsKey("year")) {
			model.setYear(jsonObject.getInteger("year"));
		}
		if (jsonObject.containsKey("month")) {
			model.setMonth(jsonObject.getInteger("month"));
		}
		if (jsonObject.containsKey("createBy")) {
			model.setCreateBy(jsonObject.getString("createBy"));
		}
		if (jsonObject.containsKey("createTime")) {
			model.setCreateTime(jsonObject.getDate("createTime"));
		}

		return model;
	}

	public static JSONObject toJsonObject(PersonSickness model) {
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
		if (model.getPersonId() != null) {
			jsonObject.put("personId", model.getPersonId());
		}
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getSickness() != null) {
			jsonObject.put("sickness", model.getSickness());
		}
		if (model.getInfectiousFlag() != null) {
			jsonObject.put("infectiousFlag", model.getInfectiousFlag());
		}
		if (model.getInfectiousDisease() != null) {
			jsonObject.put("infectiousDisease", model.getInfectiousDisease());
		}
		if (model.getDiscoverTime() != null) {
			jsonObject.put("discoverTime", DateUtils.getDate(model.getDiscoverTime()));
			jsonObject.put("discoverTime_date", DateUtils.getDate(model.getDiscoverTime()));
			jsonObject.put("discoverTime_datetime", DateUtils.getDateTime(model.getDiscoverTime()));
		}
		if (model.getReportTime() != null) {
			jsonObject.put("reportTime", DateUtils.getDate(model.getReportTime()));
			jsonObject.put("reportTime_date", DateUtils.getDate(model.getReportTime()));
			jsonObject.put("reportTime_datetime", DateUtils.getDateTime(model.getReportTime()));
		}

		if (model.getReportOrg() != null) {
			jsonObject.put("reportOrg", model.getReportOrg());
		}
		if (model.getReportResponsible() != null) {
			jsonObject.put("reportResponsible", model.getReportResponsible());
		}
		if (model.getReportWay() != null) {
			jsonObject.put("reportWay", model.getReportWay());
		}
		if (model.getSupervisionOpinion() != null) {
			jsonObject.put("supervisionOpinion", model.getSupervisionOpinion());
		}

		if (model.getConfirmTime() != null) {
			jsonObject.put("confirmTime", DateUtils.getDate(model.getConfirmTime()));
			jsonObject.put("confirmTime_date", DateUtils.getDate(model.getConfirmTime()));
			jsonObject.put("confirmTime_datetime", DateUtils.getDateTime(model.getConfirmTime()));
		}
		if (model.getReceiver1() != null) {
			jsonObject.put("receiver1", model.getReceiver1());
		}
		if (model.getReceiveOrg1() != null) {
			jsonObject.put("receiveOrg1", model.getReceiveOrg1());
		}
		if (model.getReceiver2() != null) {
			jsonObject.put("receiver2", model.getReceiver2());
		}
		if (model.getReceiveOrg2() != null) {
			jsonObject.put("receiveOrg2", model.getReceiveOrg2());
		}
		if (model.getSymptom() != null) {
			jsonObject.put("symptom", model.getSymptom());
		}
		if (model.getTreat() != null) {
			jsonObject.put("treat", model.getTreat());
		}
		if (model.getHospital() != null) {
			jsonObject.put("hospital", model.getHospital());
		}
		if (model.getClinicTime() != null) {
			jsonObject.put("clinicTime", DateUtils.getDate(model.getClinicTime()));
			jsonObject.put("clinicTime_date", DateUtils.getDate(model.getClinicTime()));
			jsonObject.put("clinicTime_datetime", DateUtils.getDateTime(model.getClinicTime()));
		}
		if (model.getResult() != null) {
			jsonObject.put("result", model.getResult());
		}
		if (model.getRemark() != null) {
			jsonObject.put("remark", model.getRemark());
		}

		jsonObject.put("year", model.getYear());
		jsonObject.put("month", model.getMonth());

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

	public static ObjectNode toObjectNode(PersonSickness model) {
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
		if (model.getPersonId() != null) {
			jsonObject.put("personId", model.getPersonId());
		}
		if (model.getName() != null) {
			jsonObject.put("name", model.getName());
		}
		if (model.getSickness() != null) {
			jsonObject.put("sickness", model.getSickness());
		}
		if (model.getInfectiousFlag() != null) {
			jsonObject.put("infectiousFlag", model.getInfectiousFlag());
		}
		if (model.getInfectiousDisease() != null) {
			jsonObject.put("infectiousDisease", model.getInfectiousDisease());
		}
		if (model.getDiscoverTime() != null) {
			jsonObject.put("discoverTime", DateUtils.getDate(model.getDiscoverTime()));
			jsonObject.put("discoverTime_date", DateUtils.getDate(model.getDiscoverTime()));
			jsonObject.put("discoverTime_datetime", DateUtils.getDateTime(model.getDiscoverTime()));
		}
		if (model.getReportTime() != null) {
			jsonObject.put("reportTime", DateUtils.getDate(model.getReportTime()));
			jsonObject.put("reportTime_date", DateUtils.getDate(model.getReportTime()));
			jsonObject.put("reportTime_datetime", DateUtils.getDateTime(model.getReportTime()));
		}

		if (model.getReportOrg() != null) {
			jsonObject.put("reportOrg", model.getReportOrg());
		}
		if (model.getReportResponsible() != null) {
			jsonObject.put("reportResponsible", model.getReportResponsible());
		}
		if (model.getReportWay() != null) {
			jsonObject.put("reportWay", model.getReportWay());
		}
		if (model.getSupervisionOpinion() != null) {
			jsonObject.put("supervisionOpinion", model.getSupervisionOpinion());
		}

		if (model.getConfirmTime() != null) {
			jsonObject.put("confirmTime", DateUtils.getDate(model.getConfirmTime()));
			jsonObject.put("confirmTime_date", DateUtils.getDate(model.getConfirmTime()));
			jsonObject.put("confirmTime_datetime", DateUtils.getDateTime(model.getConfirmTime()));
		}
		if (model.getReceiver1() != null) {
			jsonObject.put("receiver1", model.getReceiver1());
		}
		if (model.getReceiveOrg1() != null) {
			jsonObject.put("receiveOrg1", model.getReceiveOrg1());
		}
		if (model.getReceiver2() != null) {
			jsonObject.put("receiver2", model.getReceiver2());
		}
		if (model.getReceiveOrg2() != null) {
			jsonObject.put("receiveOrg2", model.getReceiveOrg2());
		}
		if (model.getSymptom() != null) {
			jsonObject.put("symptom", model.getSymptom());
		}
		if (model.getTreat() != null) {
			jsonObject.put("treat", model.getTreat());
		}
		if (model.getHospital() != null) {
			jsonObject.put("hospital", model.getHospital());
		}
		if (model.getClinicTime() != null) {
			jsonObject.put("clinicTime", DateUtils.getDate(model.getClinicTime()));
			jsonObject.put("clinicTime_date", DateUtils.getDate(model.getClinicTime()));
			jsonObject.put("clinicTime_datetime", DateUtils.getDateTime(model.getClinicTime()));
		}
		if (model.getResult() != null) {
			jsonObject.put("result", model.getResult());
		}
		if (model.getRemark() != null) {
			jsonObject.put("remark", model.getRemark());
		}

		jsonObject.put("year", model.getYear());
		jsonObject.put("month", model.getMonth());

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

	public static JSONArray listToArray(java.util.List<PersonSickness> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (PersonSickness model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static java.util.List<PersonSickness> arrayToList(JSONArray array) {
		java.util.List<PersonSickness> list = new java.util.ArrayList<PersonSickness>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			PersonSickness model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	private PersonSicknessJsonFactory() {

	}

}
