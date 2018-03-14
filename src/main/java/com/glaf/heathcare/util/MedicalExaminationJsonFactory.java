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
public class MedicalExaminationJsonFactory {

	public static MedicalExamination jsonToObject(JSONObject jsonObject) {
		MedicalExamination model = new MedicalExamination();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getLong("id"));
		}
		if (jsonObject.containsKey("batchId")) {
			model.setBatchId(jsonObject.getLong("batchId"));
		}
		if (jsonObject.containsKey("checkId")) {
			model.setCheckId(jsonObject.getString("checkId"));
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
		if (jsonObject.containsKey("sex")) {
			model.setSex(jsonObject.getString("sex"));
		}
		if (jsonObject.containsKey("height")) {
			model.setHeight(jsonObject.getDouble("height"));
		}
		if (jsonObject.containsKey("heightIncrement")) {
			model.setHeightIncrement(jsonObject.getDouble("heightIncrement"));
		}
		if (jsonObject.containsKey("heightLevel")) {
			model.setHeightLevel(jsonObject.getInteger("heightLevel"));
		}
		if (jsonObject.containsKey("heightEvaluate")) {
			model.setHeightEvaluate(jsonObject.getString("heightEvaluate"));
		}
		if (jsonObject.containsKey("weight")) {
			model.setWeight(jsonObject.getDouble("weight"));
		}
		if (jsonObject.containsKey("weightIncrement")) {
			model.setWeightIncrement(jsonObject.getDouble("weightIncrement"));
		}
		if (jsonObject.containsKey("weightLevel")) {
			model.setWeightLevel(jsonObject.getInteger("weightLevel"));
		}
		if (jsonObject.containsKey("weightEvaluate")) {
			model.setWeightEvaluate(jsonObject.getString("weightEvaluate"));
		}
		if (jsonObject.containsKey("weightHeightPercent")) {
			model.setWeightHeightPercent(jsonObject.getDouble("weightHeightPercent"));
		}
		if (jsonObject.containsKey("bmi")) {
			model.setBmi(jsonObject.getDouble("bmi"));
		}
		if (jsonObject.containsKey("bmiIndex")) {
			model.setBmiIndex(jsonObject.getDouble("bmiIndex"));
		}
		if (jsonObject.containsKey("bmiEvaluate")) {
			model.setBmiEvaluate(jsonObject.getString("bmiEvaluate"));
		}
		if (jsonObject.containsKey("obesityIndex")) {
			model.setObesityIndex(jsonObject.getDouble("obesityIndex"));
		}
		if (jsonObject.containsKey("allergy")) {
			model.setAllergy(jsonObject.getString("allergy"));
		}
		if (jsonObject.containsKey("eyeLeft")) {
			model.setEyeLeft(jsonObject.getString("eyeLeft"));
		}
		if (jsonObject.containsKey("eyeLeftRemark")) {
			model.setEyeLeftRemark(jsonObject.getString("eyeLeftRemark"));
		}
		if (jsonObject.containsKey("eyeRight")) {
			model.setEyeRight(jsonObject.getString("eyeRight"));
		}
		if (jsonObject.containsKey("eyeRightRemark")) {
			model.setEyeRightRemark(jsonObject.getString("eyeRightRemark"));
		}
		if (jsonObject.containsKey("eyesightLeft")) {
			model.setEyesightLeft(jsonObject.getDouble("eyesightLeft"));
		}
		if (jsonObject.containsKey("eyesightRight")) {
			model.setEyesightRight(jsonObject.getDouble("eyesightRight"));
		}
		if (jsonObject.containsKey("earLeft")) {
			model.setEarLeft(jsonObject.getString("earLeft"));
		}
		if (jsonObject.containsKey("earLeftRemark")) {
			model.setEarLeftRemark(jsonObject.getString("earLeftRemark"));
		}
		if (jsonObject.containsKey("earRight")) {
			model.setEarRight(jsonObject.getString("earRight"));
		}
		if (jsonObject.containsKey("earRightRemark")) {
			model.setEarRightRemark(jsonObject.getString("earRightRemark"));
		}
		if (jsonObject.containsKey("tooth")) {
			model.setTooth(jsonObject.getInteger("tooth"));
		}
		if (jsonObject.containsKey("saprodontia")) {
			model.setSaprodontia(jsonObject.getInteger("saprodontia"));
		}
		if (jsonObject.containsKey("head")) {
			model.setHead(jsonObject.getString("head"));
		}
		if (jsonObject.containsKey("headRemark")) {
			model.setHeadRemark(jsonObject.getString("headRemark"));
		}
		if (jsonObject.containsKey("thorax")) {
			model.setThorax(jsonObject.getString("thorax"));
		}
		if (jsonObject.containsKey("thoraxRemark")) {
			model.setThoraxRemark(jsonObject.getString("thoraxRemark"));
		}
		if (jsonObject.containsKey("spine")) {
			model.setSpine(jsonObject.getString("spine"));
		}
		if (jsonObject.containsKey("spineRemark")) {
			model.setSpineRemark(jsonObject.getString("spineRemark"));
		}
		if (jsonObject.containsKey("pharyngeal")) {
			model.setPharyngeal(jsonObject.getString("pharyngeal"));
		}
		if (jsonObject.containsKey("pharyngealRemark")) {
			model.setPharyngealRemark(jsonObject.getString("pharyngealRemark"));
		}
		if (jsonObject.containsKey("cardiopulmonary")) {
			model.setCardiopulmonary(jsonObject.getString("cardiopulmonary"));
		}
		if (jsonObject.containsKey("cardiopulmonaryRemark")) {
			model.setCardiopulmonaryRemark(jsonObject.getString("cardiopulmonaryRemark"));
		}
		if (jsonObject.containsKey("hepatolienal")) {
			model.setHepatolienal(jsonObject.getString("hepatolienal"));
		}
		if (jsonObject.containsKey("hepatolienalRemark")) {
			model.setHepatolienalRemark(jsonObject.getString("hepatolienalRemark"));
		}
		if (jsonObject.containsKey("pudendum")) {
			model.setPudendum(jsonObject.getString("pudendum"));
		}
		if (jsonObject.containsKey("pudendumRemark")) {
			model.setPudendumRemark(jsonObject.getString("pudendumRemark"));
		}
		if (jsonObject.containsKey("skin")) {
			model.setSkin(jsonObject.getString("skin"));
		}
		if (jsonObject.containsKey("skinRemark")) {
			model.setSkinRemark(jsonObject.getString("skinRemark"));
		}
		if (jsonObject.containsKey("lymphoid")) {
			model.setLymphoid(jsonObject.getString("lymphoid"));
		}
		if (jsonObject.containsKey("lymphoidRemark")) {
			model.setLymphoidRemark(jsonObject.getString("lymphoidRemark"));
		}
		if (jsonObject.containsKey("bregma")) {
			model.setBregma(jsonObject.getString("bregma"));
		}
		if (jsonObject.containsKey("bregmaRemark")) {
			model.setBregmaRemark(jsonObject.getString("bregmaRemark"));
		}
		if (jsonObject.containsKey("oralogy")) {
			model.setOralogy(jsonObject.getString("oralogy"));
		}
		if (jsonObject.containsKey("oralogyRemark")) {
			model.setOralogyRemark(jsonObject.getString("oralogyRemark"));
		}
		if (jsonObject.containsKey("tonsil")) {
			model.setTonsil(jsonObject.getString("tonsil"));
		}
		if (jsonObject.containsKey("tonsilRemark")) {
			model.setTonsilRemark(jsonObject.getString("tonsilRemark"));
		}
		if (jsonObject.containsKey("bone")) {
			model.setBone(jsonObject.getString("bone"));
		}
		if (jsonObject.containsKey("boneRemark")) {
			model.setBoneRemark(jsonObject.getString("boneRemark"));
		}
		if (jsonObject.containsKey("birthDefect")) {
			model.setBirthDefect(jsonObject.getString("birthDefect"));
		}
		if (jsonObject.containsKey("previousHistory")) {
			model.setPreviousHistory(jsonObject.getString("previousHistory"));
		}
		if (jsonObject.containsKey("hemoglobin")) {
			model.setHemoglobin(jsonObject.getString("hemoglobin"));
		}
		if (jsonObject.containsKey("hemoglobinValue")) {
			model.setHemoglobinValue(jsonObject.getDouble("hemoglobinValue"));
		}
		if (jsonObject.containsKey("alt")) {
			model.setAlt(jsonObject.getString("alt"));
		}
		if (jsonObject.containsKey("altValue")) {
			model.setAltValue(jsonObject.getDouble("altValue"));
		}
		if (jsonObject.containsKey("hbsab")) {
			model.setHbsab(jsonObject.getString("hbsab"));
		}
		if (jsonObject.containsKey("hbsabValue")) {
			model.setHbsabValue(jsonObject.getDouble("hbsabValue"));
		}
		if (jsonObject.containsKey("sgpt")) {
			model.setSgpt(jsonObject.getString("sgpt"));
		}
		if (jsonObject.containsKey("hvaigm")) {
			model.setHvaigm(jsonObject.getString("hvaigm"));
		}
		if (jsonObject.containsKey("type")) {
			model.setType(jsonObject.getString("type"));
		}
		if (jsonObject.containsKey("checkDate")) {
			model.setCheckDate(jsonObject.getDate("checkDate"));
		}
		if (jsonObject.containsKey("checkDoctor")) {
			model.setCheckDoctor(jsonObject.getString("checkDoctor"));
		}
		if (jsonObject.containsKey("checkDoctorId")) {
			model.setCheckDoctorId(jsonObject.getString("checkDoctorId"));
		}
		if (jsonObject.containsKey("checkResult")) {
			model.setCheckResult(jsonObject.getString("checkResult"));
		}
		if (jsonObject.containsKey("checkOrganization")) {
			model.setCheckOrganization(jsonObject.getString("checkOrganization"));
		}
		if (jsonObject.containsKey("checkOrganizationId")) {
			model.setCheckOrganizationId(jsonObject.getLong("checkOrganizationId"));
		}
		if (jsonObject.containsKey("doctorSuggest")) {
			model.setDoctorSuggest(jsonObject.getString("doctorSuggest"));
		}
		if (jsonObject.containsKey("healthEvaluate")) {
			model.setHealthEvaluate(jsonObject.getString("healthEvaluate"));
		}
		if (jsonObject.containsKey("year")) {
			model.setYear(jsonObject.getInteger("year"));
		}
		if (jsonObject.containsKey("month")) {
			model.setMonth(jsonObject.getInteger("month"));
		}
		if (jsonObject.containsKey("ageOfTheMoon")) {
			model.setAgeOfTheMoon(jsonObject.getInteger("ageOfTheMoon"));
		}
		if (jsonObject.containsKey("remark")) {
			model.setRemark(jsonObject.getString("remark"));
		}
		if (jsonObject.containsKey("confirmBy")) {
			model.setConfirmBy(jsonObject.getString("confirmBy"));
		}
		if (jsonObject.containsKey("confirmTime")) {
			model.setConfirmTime(jsonObject.getDate("confirmTime"));
		}
		if (jsonObject.containsKey("createBy")) {
			model.setCreateBy(jsonObject.getString("createBy"));
		}
		if (jsonObject.containsKey("createTime")) {
			model.setCreateTime(jsonObject.getDate("createTime"));
		}

		return model;
	}

	public static JSONObject toJsonObject(MedicalExamination model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		jsonObject.put("batchId", model.getBatchId());
		if (model.getCheckId() != null) {
			jsonObject.put("checkId", model.getCheckId());
		}
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
		if (model.getSex() != null) {
			jsonObject.put("sex", model.getSex());
		}
		jsonObject.put("height", model.getHeight());
		jsonObject.put("heightIncrement", model.getHeightIncrement());
		jsonObject.put("heightLevel", model.getHeightLevel());
		if (model.getHeightEvaluate() != null) {
			jsonObject.put("heightEvaluate", model.getHeightEvaluate());
		}
		jsonObject.put("weight", model.getWeight());
		jsonObject.put("weightIncrement", model.getWeightIncrement());
		jsonObject.put("weightLevel", model.getWeightLevel());
		if (model.getWeightEvaluate() != null) {
			jsonObject.put("weightEvaluate", model.getWeightEvaluate());
		}
		jsonObject.put("weightHeightPercent", model.getWeightHeightPercent());
		jsonObject.put("bmi", model.getBmi());
		jsonObject.put("bmiIndex", model.getBmiIndex());
		if (model.getBmiEvaluate() != null) {
			jsonObject.put("bmiEvaluate", model.getBmiEvaluate());
		}
		jsonObject.put("obesityIndex", model.getObesityIndex());
		if (model.getAllergy() != null) {
			jsonObject.put("allergy", model.getAllergy());
		}
		if (model.getEyeLeft() != null) {
			jsonObject.put("eyeLeft", model.getEyeLeft());
		}
		if (model.getEyeLeftRemark() != null) {
			jsonObject.put("eyeLeftRemark", model.getEyeLeftRemark());
		}
		if (model.getEyeRight() != null) {
			jsonObject.put("eyeRight", model.getEyeRight());
		}
		if (model.getEyeRightRemark() != null) {
			jsonObject.put("eyeRightRemark", model.getEyeRightRemark());
		}
		jsonObject.put("eyesightLeft", model.getEyesightLeft());
		jsonObject.put("eyesightRight", model.getEyesightRight());
		if (model.getEarLeft() != null) {
			jsonObject.put("earLeft", model.getEarLeft());
		}
		if (model.getEarLeftRemark() != null) {
			jsonObject.put("earLeftRemark", model.getEarLeftRemark());
		}
		if (model.getEarRight() != null) {
			jsonObject.put("earRight", model.getEarRight());
		}
		if (model.getEarRightRemark() != null) {
			jsonObject.put("earRightRemark", model.getEarRightRemark());
		}
		jsonObject.put("tooth", model.getTooth());
		jsonObject.put("saprodontia", model.getSaprodontia());
		if (model.getHead() != null) {
			jsonObject.put("head", model.getHead());
		}
		if (model.getHeadRemark() != null) {
			jsonObject.put("headRemark", model.getHeadRemark());
		}
		if (model.getThorax() != null) {
			jsonObject.put("thorax", model.getThorax());
		}
		if (model.getThoraxRemark() != null) {
			jsonObject.put("thoraxRemark", model.getThoraxRemark());
		}
		if (model.getSpine() != null) {
			jsonObject.put("spine", model.getSpine());
		}
		if (model.getSpineRemark() != null) {
			jsonObject.put("spineRemark", model.getSpineRemark());
		}
		if (model.getPharyngeal() != null) {
			jsonObject.put("pharyngeal", model.getPharyngeal());
		}
		if (model.getPharyngealRemark() != null) {
			jsonObject.put("pharyngealRemark", model.getPharyngealRemark());
		}
		if (model.getCardiopulmonary() != null) {
			jsonObject.put("cardiopulmonary", model.getCardiopulmonary());
		}
		if (model.getCardiopulmonaryRemark() != null) {
			jsonObject.put("cardiopulmonaryRemark", model.getCardiopulmonaryRemark());
		}
		if (model.getHepatolienal() != null) {
			jsonObject.put("hepatolienal", model.getHepatolienal());
		}
		if (model.getHepatolienalRemark() != null) {
			jsonObject.put("hepatolienalRemark", model.getHepatolienalRemark());
		}
		if (model.getPudendum() != null) {
			jsonObject.put("pudendum", model.getPudendum());
		}
		if (model.getPudendumRemark() != null) {
			jsonObject.put("pudendumRemark", model.getPudendumRemark());
		}
		if (model.getSkin() != null) {
			jsonObject.put("skin", model.getSkin());
		}
		if (model.getSkinRemark() != null) {
			jsonObject.put("skinRemark", model.getSkinRemark());
		}
		if (model.getLymphoid() != null) {
			jsonObject.put("lymphoid", model.getLymphoid());
		}
		if (model.getLymphoidRemark() != null) {
			jsonObject.put("lymphoidRemark", model.getLymphoidRemark());
		}
		if (model.getBregma() != null) {
			jsonObject.put("bregma", model.getBregma());
		}
		if (model.getBregmaRemark() != null) {
			jsonObject.put("bregmaRemark", model.getBregmaRemark());
		}
		if (model.getOralogy() != null) {
			jsonObject.put("oralogy", model.getOralogy());
		}
		if (model.getOralogyRemark() != null) {
			jsonObject.put("oralogyRemark", model.getOralogyRemark());
		}
		if (model.getTonsil() != null) {
			jsonObject.put("tonsil", model.getTonsil());
		}
		if (model.getTonsilRemark() != null) {
			jsonObject.put("tonsilRemark", model.getTonsilRemark());
		}
		if (model.getBone() != null) {
			jsonObject.put("bone", model.getBone());
		}
		if (model.getBoneRemark() != null) {
			jsonObject.put("boneRemark", model.getBoneRemark());
		}
		if (model.getBirthDefect() != null) {
			jsonObject.put("birthDefect", model.getBirthDefect());
		}
		if (model.getPreviousHistory() != null) {
			jsonObject.put("previousHistory", model.getPreviousHistory());
		}
		if (model.getHemoglobin() != null) {
			jsonObject.put("hemoglobin", model.getHemoglobin());
		}
		if (model.getHemoglobinHtml() != null) {
			jsonObject.put("hemoglobinHtml", model.getHemoglobinHtml());
		}
		jsonObject.put("hemoglobinValue", model.getHemoglobinValue());
		if (model.getAlt() != null) {
			jsonObject.put("alt", model.getAlt());
		}
		jsonObject.put("altValue", model.getAltValue());
		if (model.getHbsab() != null) {
			jsonObject.put("hbsab", model.getHbsab());
		}
		jsonObject.put("hbsabValue", model.getHbsabValue());
		if (model.getSgpt() != null) {
			jsonObject.put("sgpt", model.getSgpt());
		}
		if (model.getHvaigm() != null) {
			jsonObject.put("hvaigm", model.getHvaigm());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		if (model.getCheckDate() != null) {
			jsonObject.put("checkDate", DateUtils.getDate(model.getCheckDate()));
			jsonObject.put("checkDate_date", DateUtils.getDate(model.getCheckDate()));
			jsonObject.put("checkDate_datetime", DateUtils.getDateTime(model.getCheckDate()));
		}
		if (model.getCheckDoctor() != null) {
			jsonObject.put("checkDoctor", model.getCheckDoctor());
		}
		if (model.getCheckDoctorId() != null) {
			jsonObject.put("checkDoctorId", model.getCheckDoctorId());
		}
		if (model.getCheckResult() != null) {
			jsonObject.put("checkResult", model.getCheckResult());
		}
		if (model.getCheckOrganization() != null) {
			jsonObject.put("checkOrganization", model.getCheckOrganization());
		}
		jsonObject.put("checkOrganizationId", model.getCheckOrganizationId());
		if (model.getDoctorSuggest() != null) {
			jsonObject.put("doctorSuggest", model.getDoctorSuggest());
		}
		if (model.getHealthEvaluate() != null) {
			jsonObject.put("healthEvaluate", model.getHealthEvaluate());
		}
		jsonObject.put("year", model.getYear());
		jsonObject.put("month", model.getMonth());
		jsonObject.put("ageOfTheMoon", model.getAgeOfTheMoon());
		if (model.getRemark() != null) {
			jsonObject.put("remark", model.getRemark());
		}
		if (model.getConfirmBy() != null) {
			jsonObject.put("confirmBy", model.getConfirmBy());
		}
		if (model.getConfirmTime() != null) {
			jsonObject.put("confirmTime", DateUtils.getDate(model.getConfirmTime()));
			jsonObject.put("confirmTime_date", DateUtils.getDate(model.getConfirmTime()));
			jsonObject.put("confirmTime_datetime", DateUtils.getDateTime(model.getConfirmTime()));
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

	public static ObjectNode toObjectNode(MedicalExamination model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		jsonObject.put("batchId", model.getBatchId());
		if (model.getCheckId() != null) {
			jsonObject.put("checkId", model.getCheckId());
		}
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
		if (model.getSex() != null) {
			jsonObject.put("sex", model.getSex());
		}
		jsonObject.put("height", model.getHeight());
		jsonObject.put("heightIncrement", model.getHeightIncrement());
		jsonObject.put("heightLevel", model.getHeightLevel());
		if (model.getHeightEvaluate() != null) {
			jsonObject.put("heightEvaluate", model.getHeightEvaluate());
		}
		jsonObject.put("weight", model.getWeight());
		jsonObject.put("weightIncrement", model.getWeightIncrement());
		jsonObject.put("weightLevel", model.getWeightLevel());
		if (model.getWeightEvaluate() != null) {
			jsonObject.put("weightEvaluate", model.getWeightEvaluate());
		}
		jsonObject.put("weightHeightPercent", model.getWeightHeightPercent());
		jsonObject.put("bmi", model.getBmi());
		jsonObject.put("bmiIndex", model.getBmiIndex());
		if (model.getBmiEvaluate() != null) {
			jsonObject.put("bmiEvaluate", model.getBmiEvaluate());
		}
		jsonObject.put("obesityIndex", model.getObesityIndex());
		if (model.getAllergy() != null) {
			jsonObject.put("allergy", model.getAllergy());
		}
		if (model.getEyeLeft() != null) {
			jsonObject.put("eyeLeft", model.getEyeLeft());
		}
		if (model.getEyeLeftRemark() != null) {
			jsonObject.put("eyeLeftRemark", model.getEyeLeftRemark());
		}
		if (model.getEyeRight() != null) {
			jsonObject.put("eyeRight", model.getEyeRight());
		}
		if (model.getEyeRightRemark() != null) {
			jsonObject.put("eyeRightRemark", model.getEyeRightRemark());
		}
		jsonObject.put("eyesightLeft", model.getEyesightLeft());
		jsonObject.put("eyesightRight", model.getEyesightRight());
		if (model.getEarLeft() != null) {
			jsonObject.put("earLeft", model.getEarLeft());
		}
		if (model.getEarLeftRemark() != null) {
			jsonObject.put("earLeftRemark", model.getEarLeftRemark());
		}
		if (model.getEarRight() != null) {
			jsonObject.put("earRight", model.getEarRight());
		}
		if (model.getEarRightRemark() != null) {
			jsonObject.put("earRightRemark", model.getEarRightRemark());
		}
		jsonObject.put("tooth", model.getTooth());
		jsonObject.put("saprodontia", model.getSaprodontia());
		if (model.getHead() != null) {
			jsonObject.put("head", model.getHead());
		}
		if (model.getHeadRemark() != null) {
			jsonObject.put("headRemark", model.getHeadRemark());
		}
		if (model.getThorax() != null) {
			jsonObject.put("thorax", model.getThorax());
		}
		if (model.getThoraxRemark() != null) {
			jsonObject.put("thoraxRemark", model.getThoraxRemark());
		}
		if (model.getSpine() != null) {
			jsonObject.put("spine", model.getSpine());
		}
		if (model.getSpineRemark() != null) {
			jsonObject.put("spineRemark", model.getSpineRemark());
		}
		if (model.getPharyngeal() != null) {
			jsonObject.put("pharyngeal", model.getPharyngeal());
		}
		if (model.getPharyngealRemark() != null) {
			jsonObject.put("pharyngealRemark", model.getPharyngealRemark());
		}
		if (model.getCardiopulmonary() != null) {
			jsonObject.put("cardiopulmonary", model.getCardiopulmonary());
		}
		if (model.getCardiopulmonaryRemark() != null) {
			jsonObject.put("cardiopulmonaryRemark", model.getCardiopulmonaryRemark());
		}
		if (model.getHepatolienal() != null) {
			jsonObject.put("hepatolienal", model.getHepatolienal());
		}
		if (model.getHepatolienalRemark() != null) {
			jsonObject.put("hepatolienalRemark", model.getHepatolienalRemark());
		}
		if (model.getPudendum() != null) {
			jsonObject.put("pudendum", model.getPudendum());
		}
		if (model.getPudendumRemark() != null) {
			jsonObject.put("pudendumRemark", model.getPudendumRemark());
		}
		if (model.getSkin() != null) {
			jsonObject.put("skin", model.getSkin());
		}
		if (model.getSkinRemark() != null) {
			jsonObject.put("skinRemark", model.getSkinRemark());
		}
		if (model.getLymphoid() != null) {
			jsonObject.put("lymphoid", model.getLymphoid());
		}
		if (model.getLymphoidRemark() != null) {
			jsonObject.put("lymphoidRemark", model.getLymphoidRemark());
		}
		if (model.getBregma() != null) {
			jsonObject.put("bregma", model.getBregma());
		}
		if (model.getBregmaRemark() != null) {
			jsonObject.put("bregmaRemark", model.getBregmaRemark());
		}
		if (model.getOralogy() != null) {
			jsonObject.put("oralogy", model.getOralogy());
		}
		if (model.getOralogyRemark() != null) {
			jsonObject.put("oralogyRemark", model.getOralogyRemark());
		}
		if (model.getTonsil() != null) {
			jsonObject.put("tonsil", model.getTonsil());
		}
		if (model.getTonsilRemark() != null) {
			jsonObject.put("tonsilRemark", model.getTonsilRemark());
		}
		if (model.getBone() != null) {
			jsonObject.put("bone", model.getBone());
		}
		if (model.getBoneRemark() != null) {
			jsonObject.put("boneRemark", model.getBoneRemark());
		}
		if (model.getBirthDefect() != null) {
			jsonObject.put("birthDefect", model.getBirthDefect());
		}
		if (model.getPreviousHistory() != null) {
			jsonObject.put("previousHistory", model.getPreviousHistory());
		}
		if (model.getHemoglobin() != null) {
			jsonObject.put("hemoglobin", model.getHemoglobin());
		}
		jsonObject.put("hemoglobinValue", model.getHemoglobinValue());
		if (model.getAlt() != null) {
			jsonObject.put("alt", model.getAlt());
		}
		jsonObject.put("altValue", model.getAltValue());
		if (model.getHbsab() != null) {
			jsonObject.put("hbsab", model.getHbsab());
		}
		jsonObject.put("hbsabValue", model.getHbsabValue());
		if (model.getSgpt() != null) {
			jsonObject.put("sgpt", model.getSgpt());
		}
		if (model.getHvaigm() != null) {
			jsonObject.put("hvaigm", model.getHvaigm());
		}
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		if (model.getCheckDate() != null) {
			jsonObject.put("checkDate", DateUtils.getDate(model.getCheckDate()));
			jsonObject.put("checkDate_date", DateUtils.getDate(model.getCheckDate()));
			jsonObject.put("checkDate_datetime", DateUtils.getDateTime(model.getCheckDate()));
		}
		if (model.getCheckDoctor() != null) {
			jsonObject.put("checkDoctor", model.getCheckDoctor());
		}
		if (model.getCheckDoctorId() != null) {
			jsonObject.put("checkDoctorId", model.getCheckDoctorId());
		}
		if (model.getCheckResult() != null) {
			jsonObject.put("checkResult", model.getCheckResult());
		}
		if (model.getCheckOrganization() != null) {
			jsonObject.put("checkOrganization", model.getCheckOrganization());
		}
		jsonObject.put("checkOrganizationId", model.getCheckOrganizationId());
		if (model.getDoctorSuggest() != null) {
			jsonObject.put("doctorSuggest", model.getDoctorSuggest());
		}
		if (model.getHealthEvaluate() != null) {
			jsonObject.put("healthEvaluate", model.getHealthEvaluate());
		}
		jsonObject.put("year", model.getYear());
		jsonObject.put("month", model.getMonth());
		jsonObject.put("ageOfTheMoon", model.getAgeOfTheMoon());
		if (model.getRemark() != null) {
			jsonObject.put("remark", model.getRemark());
		}
		if (model.getConfirmBy() != null) {
			jsonObject.put("confirmBy", model.getConfirmBy());
		}
		if (model.getConfirmTime() != null) {
			jsonObject.put("confirmTime", DateUtils.getDate(model.getConfirmTime()));
			jsonObject.put("confirmTime_date", DateUtils.getDate(model.getConfirmTime()));
			jsonObject.put("confirmTime_datetime", DateUtils.getDateTime(model.getConfirmTime()));
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

	public static JSONArray listToArray(java.util.List<MedicalExamination> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (MedicalExamination model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static java.util.List<MedicalExamination> arrayToList(JSONArray array) {
		java.util.List<MedicalExamination> list = new java.util.ArrayList<MedicalExamination>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			MedicalExamination model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	private MedicalExaminationJsonFactory() {

	}

}
