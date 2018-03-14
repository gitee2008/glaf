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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.glaf.core.base.DataRequest;
import com.glaf.core.base.DataRequest.FilterDescriptor;
import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.domain.TableDefinition;
import com.glaf.core.util.DBUtils;

/**
 * 
 * 实体数据工厂类
 *
 */
public class MedicalExaminationDomainFactory {

	public static final String TABLENAME = "HEALTH_MEDICAL_EXAMINATION";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("batchId", "BATCHID_");
		columnMap.put("checkId", "CHECKID_");
		columnMap.put("tenantId", "TENANTID_");
		columnMap.put("gradeId", "GRADEID_");
		columnMap.put("personId", "PERSONID_");
		columnMap.put("name", "NAME_");
		columnMap.put("sex", "SEX_");
		columnMap.put("height", "HEIGHT_");
		columnMap.put("heightIncrement", "HEIGHTINCREMENT_");
		columnMap.put("heightLevel", "HEIGHTLEVEL_");
		columnMap.put("heightEvaluate", "HEIGHTEVALUATE_");
		columnMap.put("weight", "WEIGHT_");
		columnMap.put("weightIncrement", "WEIGHTINCREMENT_");
		columnMap.put("weightLevel", "WEIGHTLEVEL_");
		columnMap.put("weightEvaluate", "WEIGHTEVALUATE_");
		columnMap.put("weightHeightPercent", "WEIGHTHEIGHTPERCENT_");
		columnMap.put("bmi", "BMI_");
		columnMap.put("bmiIndex", "BMIINDEX_");
		columnMap.put("bmiEvaluate", "BMIEVALUATE_");
		columnMap.put("obesityIndex", "OBESITYINDEX_");
		columnMap.put("allergy", "ALLERGY_");
		columnMap.put("eyeLeft", "EYELEFT_");
		columnMap.put("eyeLeftRemark", "EYELEFTREMARK_");
		columnMap.put("eyeRight", "EYERIGHT_");
		columnMap.put("eyeRightRemark", "EYERIGHTREMARK_");
		columnMap.put("eyesightLeft", "EYESIGHTLEFT_");
		columnMap.put("eyesightRight", "EYESIGHTRIGHT_");
		columnMap.put("earLeft", "EARLEFT_");
		columnMap.put("earLeftRemark", "EARLEFTREMARK_");
		columnMap.put("earRight", "EARRIGHT_");
		columnMap.put("earRightRemark", "EARRIGHTREMARK_");
		columnMap.put("tooth", "TOOTH_");
		columnMap.put("saprodontia", "SAPRODONTIA_");
		columnMap.put("head", "HEAD_");
		columnMap.put("headRemark", "HEADREMARK_");
		columnMap.put("thorax", "THORAX_");
		columnMap.put("thoraxRemark", "THORAXREMARK_");
		columnMap.put("spine", "SPINE_");
		columnMap.put("spineRemark", "SPINEREMARK_");
		columnMap.put("pharyngeal", "PHARYNGEAL_");
		columnMap.put("pharyngealRemark", "PHARYNGEALREMARK_");
		columnMap.put("cardiopulmonary", "CARDIOPULMONARY_");
		columnMap.put("cardiopulmonaryRemark", "CARDIOPULMONARYREMARK_");
		columnMap.put("hepatolienal", "HEPATOLIENAL_");
		columnMap.put("hepatolienalRemark", "HEPATOLIENALREMARK_");
		columnMap.put("pudendum", "PUDENDUM_");
		columnMap.put("pudendumRemark", "PUDENDUMREMARK_");
		columnMap.put("skin", "SKIN_");
		columnMap.put("skinRemark", "SKINREMARK_");
		columnMap.put("lymphoid", "LYMPHOID_");
		columnMap.put("lymphoidRemark", "LYMPHOIDREMARK_");
		columnMap.put("bregma", "BREGMA_");
		columnMap.put("bregmaRemark", "BREGMAREMARK_");
		columnMap.put("oralogy", "ORALOGY_");
		columnMap.put("oralogyRemark", "ORALOGYREMARK_");
		columnMap.put("tonsil", "TONSIL_");
		columnMap.put("tonsilRemark", "TONSILREMARK_");
		columnMap.put("bone", "BONE_");
		columnMap.put("boneRemark", "BONEREMARK_");
		columnMap.put("birthDefect", "BIRTHDEFECT_");
		columnMap.put("previousHistory", "PREVIOUSHISTORY_");
		columnMap.put("hemoglobin", "HEMOGLOBIN_");
		columnMap.put("hemoglobinValue", "HEMOGLOBIN_VALUE_");
		columnMap.put("alt", "ALT_");
		columnMap.put("altValue", "ALT_VALUE_");
		columnMap.put("hbsab", "HBSAB_");
		columnMap.put("hbsabValue", "HBSAB_VALUE_");
		columnMap.put("sgpt", "SGPT_");
		columnMap.put("hvaigm", "HVAIGM_");
		columnMap.put("type", "TYPE_");
		columnMap.put("checkDate", "CHECKDATE_");
		columnMap.put("checkDoctor", "CHECKDOCTOR_");
		columnMap.put("checkDoctorId", "CHECKDOCTORID_");
		columnMap.put("checkResult", "CHECKRESULT_");
		columnMap.put("checkOrganization", "CHECKORGANIZATION_");
		columnMap.put("checkOrganizationId", "CHECKORGANIZATIONID_");
		columnMap.put("doctorSuggest", "DOCTORSUGGEST_");
		columnMap.put("healthEvaluate", "HEALTHEVALUATE_");
		columnMap.put("year", "YEAR_");
		columnMap.put("month", "MONTH_");
		columnMap.put("ageOfTheMoon", "AGEOFTHEMOON_");
		columnMap.put("remark", "REMARK_");
		columnMap.put("confirmBy", "CONFIRMBY_");
		columnMap.put("confirmTime", "CONFIRMTIME_");
		columnMap.put("createBy", "CREATEBY_");
		columnMap.put("createTime", "CREATETIME_");

		javaTypeMap.put("id", "Long");
		javaTypeMap.put("batchId", "Long");
		javaTypeMap.put("checkId", "String");
		javaTypeMap.put("tenantId", "String");
		javaTypeMap.put("gradeId", "String");
		javaTypeMap.put("personId", "String");
		javaTypeMap.put("name", "String");
		javaTypeMap.put("sex", "String");
		javaTypeMap.put("height", "Double");
		javaTypeMap.put("heightIncrement", "Double");
		javaTypeMap.put("heightLevel", "Integer");
		javaTypeMap.put("heightEvaluate", "String");
		javaTypeMap.put("weight", "Double");
		javaTypeMap.put("weightIncrement", "Double");
		javaTypeMap.put("weightLevel", "Integer");
		javaTypeMap.put("weightEvaluate", "String");
		javaTypeMap.put("weightHeightPercent", "Double");
		javaTypeMap.put("bmi", "Double");
		javaTypeMap.put("bmiIndex", "Double");
		javaTypeMap.put("bmiEvaluate", "String");
		javaTypeMap.put("obesityIndex", "Double");
		javaTypeMap.put("allergy", "String");
		javaTypeMap.put("eyeLeft", "String");
		javaTypeMap.put("eyeLeftRemark", "String");
		javaTypeMap.put("eyeRight", "String");
		javaTypeMap.put("eyeRightRemark", "String");
		javaTypeMap.put("eyesightLeft", "Double");
		javaTypeMap.put("eyesightRight", "Double");
		javaTypeMap.put("earLeft", "String");
		javaTypeMap.put("earLeftRemark", "String");
		javaTypeMap.put("earRight", "String");
		javaTypeMap.put("earRightRemark", "String");
		javaTypeMap.put("tooth", "Integer");
		javaTypeMap.put("saprodontia", "Integer");
		javaTypeMap.put("head", "String");
		javaTypeMap.put("headRemark", "String");
		javaTypeMap.put("thorax", "String");
		javaTypeMap.put("thoraxRemark", "String");
		javaTypeMap.put("spine", "String");
		javaTypeMap.put("spineRemark", "String");
		javaTypeMap.put("pharyngeal", "String");
		javaTypeMap.put("pharyngealRemark", "String");
		javaTypeMap.put("cardiopulmonary", "String");
		javaTypeMap.put("cardiopulmonaryRemark", "String");
		javaTypeMap.put("hepatolienal", "String");
		javaTypeMap.put("hepatolienalRemark", "String");
		javaTypeMap.put("pudendum", "String");
		javaTypeMap.put("pudendumRemark", "String");
		javaTypeMap.put("skin", "String");
		javaTypeMap.put("skinRemark", "String");
		javaTypeMap.put("lymphoid", "String");
		javaTypeMap.put("lymphoidRemark", "String");
		javaTypeMap.put("bregma", "String");
		javaTypeMap.put("bregmaRemark", "String");
		javaTypeMap.put("oralogy", "String");
		javaTypeMap.put("oralogyRemark", "String");
		javaTypeMap.put("tonsil", "String");
		javaTypeMap.put("tonsilRemark", "String");
		javaTypeMap.put("bone", "String");
		javaTypeMap.put("boneRemark", "String");
		javaTypeMap.put("birthDefect", "String");
		javaTypeMap.put("previousHistory", "String");
		javaTypeMap.put("hemoglobin", "String");
		javaTypeMap.put("hemoglobinValue", "Double");
		javaTypeMap.put("alt", "String");
		javaTypeMap.put("altValue", "Double");
		javaTypeMap.put("hbsab", "String");
		javaTypeMap.put("hbsabValue", "Double");
		javaTypeMap.put("sgpt", "String");
		javaTypeMap.put("hvaigm", "String");
		javaTypeMap.put("type", "String");
		javaTypeMap.put("checkDate", "Date");
		javaTypeMap.put("checkDoctor", "String");
		javaTypeMap.put("checkDoctorId", "String");
		javaTypeMap.put("checkResult", "String");
		javaTypeMap.put("checkOrganization", "String");
		javaTypeMap.put("checkOrganizationId", "Long");
		javaTypeMap.put("doctorSuggest", "String");
		javaTypeMap.put("healthEvaluate", "String");
		javaTypeMap.put("year", "Integer");
		javaTypeMap.put("month", "Integer");
		javaTypeMap.put("ageOfTheMoon", "Integer");
		javaTypeMap.put("remark", "String");
		javaTypeMap.put("confirmBy", "String");
		javaTypeMap.put("confirmTime", "Date");
		javaTypeMap.put("createBy", "String");
		javaTypeMap.put("createTime", "Date");
	}

	public static Map<String, String> getColumnMap() {
		return columnMap;
	}

	public static Map<String, String> getJavaTypeMap() {
		return javaTypeMap;
	}

	public static TableDefinition getTableDefinition() {
		return getTableDefinition(TABLENAME);
	}

	public static TableDefinition getTableDefinition(String tableName) {
		tableName = tableName.toUpperCase();
		TableDefinition tableDefinition = new TableDefinition();
		tableDefinition.setTableName(tableName);
		tableDefinition.setName("MedicalExamination");

		ColumnDefinition idColumn = new ColumnDefinition();
		idColumn.setName("id");
		idColumn.setColumnName("ID_");
		idColumn.setJavaType("Long");
		tableDefinition.setIdColumn(idColumn);

		ColumnDefinition batchId = new ColumnDefinition();
		batchId.setName("batchId");
		batchId.setColumnName("BATCHID_");
		batchId.setJavaType("Long");
		tableDefinition.addColumn(batchId);

		ColumnDefinition checkId = new ColumnDefinition();
		checkId.setName("checkId");
		checkId.setColumnName("CHECKID_");
		checkId.setJavaType("String");
		checkId.setLength(50);
		tableDefinition.addColumn(checkId);

		ColumnDefinition tenantId = new ColumnDefinition();
		tenantId.setName("tenantId");
		tenantId.setColumnName("TENANTID_");
		tenantId.setJavaType("String");
		tenantId.setLength(50);
		tableDefinition.addColumn(tenantId);

		ColumnDefinition gradeId = new ColumnDefinition();
		gradeId.setName("gradeId");
		gradeId.setColumnName("GRADEID_");
		gradeId.setJavaType("String");
		gradeId.setLength(50);
		tableDefinition.addColumn(gradeId);

		ColumnDefinition personId = new ColumnDefinition();
		personId.setName("personId");
		personId.setColumnName("PERSONID_");
		personId.setJavaType("String");
		personId.setLength(50);
		tableDefinition.addColumn(personId);

		ColumnDefinition name = new ColumnDefinition();
		name.setName("name");
		name.setColumnName("NAME_");
		name.setJavaType("String");
		name.setLength(100);
		tableDefinition.addColumn(name);

		ColumnDefinition sex = new ColumnDefinition();
		sex.setName("sex");
		sex.setColumnName("SEX_");
		sex.setJavaType("String");
		sex.setLength(2);
		tableDefinition.addColumn(sex);

		ColumnDefinition height = new ColumnDefinition();
		height.setName("height");
		height.setColumnName("HEIGHT_");
		height.setJavaType("Double");
		tableDefinition.addColumn(height);

		ColumnDefinition heightIncrement = new ColumnDefinition();
		heightIncrement.setName("heightIncrement");
		heightIncrement.setColumnName("HEIGHTINCREMENT_");
		heightIncrement.setJavaType("Double");
		tableDefinition.addColumn(heightIncrement);

		ColumnDefinition heightLevel = new ColumnDefinition();
		heightLevel.setName("heightLevel");
		heightLevel.setColumnName("HEIGHTLEVEL_");
		heightLevel.setJavaType("Integer");
		tableDefinition.addColumn(heightLevel);

		ColumnDefinition heightEvaluate = new ColumnDefinition();
		heightEvaluate.setName("heightEvaluate");
		heightEvaluate.setColumnName("HEIGHTEVALUATE_");
		heightEvaluate.setJavaType("String");
		heightEvaluate.setLength(200);
		tableDefinition.addColumn(heightEvaluate);

		ColumnDefinition weight = new ColumnDefinition();
		weight.setName("weight");
		weight.setColumnName("WEIGHT_");
		weight.setJavaType("Double");
		tableDefinition.addColumn(weight);

		ColumnDefinition weightIncrement = new ColumnDefinition();
		weightIncrement.setName("weightIncrement");
		weightIncrement.setColumnName("WEIGHTINCREMENT_");
		weightIncrement.setJavaType("Double");
		tableDefinition.addColumn(weightIncrement);

		ColumnDefinition weightLevel = new ColumnDefinition();
		weightLevel.setName("weightLevel");
		weightLevel.setColumnName("WEIGHTLEVEL_");
		weightLevel.setJavaType("Integer");
		tableDefinition.addColumn(weightLevel);

		ColumnDefinition weightEvaluate = new ColumnDefinition();
		weightEvaluate.setName("weightEvaluate");
		weightEvaluate.setColumnName("WEIGHTEVALUATE_");
		weightEvaluate.setJavaType("String");
		weightEvaluate.setLength(200);
		tableDefinition.addColumn(weightEvaluate);

		ColumnDefinition weightHeightPercent = new ColumnDefinition();
		weightHeightPercent.setName("weightHeightPercent");
		weightHeightPercent.setColumnName("WEIGHTHEIGHTPERCENT_");
		weightHeightPercent.setJavaType("Double");
		tableDefinition.addColumn(weightHeightPercent);

		ColumnDefinition bmi = new ColumnDefinition();
		bmi.setName("bmi");
		bmi.setColumnName("BMI_");
		bmi.setJavaType("Double");
		tableDefinition.addColumn(bmi);

		ColumnDefinition bmiIndex = new ColumnDefinition();
		bmiIndex.setName("bmiIndex");
		bmiIndex.setColumnName("BMIINDEX_");
		bmiIndex.setJavaType("Double");
		tableDefinition.addColumn(bmiIndex);

		ColumnDefinition bmiEvaluate = new ColumnDefinition();
		bmiEvaluate.setName("bmiEvaluate");
		bmiEvaluate.setColumnName("BMIEVALUATE_");
		bmiEvaluate.setJavaType("String");
		bmiEvaluate.setLength(200);
		tableDefinition.addColumn(bmiEvaluate);

		ColumnDefinition obesityIndex = new ColumnDefinition();
		obesityIndex.setName("obesityIndex");
		obesityIndex.setColumnName("OBESITYINDEX_");
		obesityIndex.setJavaType("Double");
		tableDefinition.addColumn(obesityIndex);

		ColumnDefinition allergy = new ColumnDefinition();
		allergy.setName("allergy");
		allergy.setColumnName("ALLERGY_");
		allergy.setJavaType("String");
		allergy.setLength(200);
		tableDefinition.addColumn(allergy);

		ColumnDefinition eyeLeft = new ColumnDefinition();
		eyeLeft.setName("eyeLeft");
		eyeLeft.setColumnName("EYELEFT_");
		eyeLeft.setJavaType("String");
		eyeLeft.setLength(200);
		tableDefinition.addColumn(eyeLeft);

		ColumnDefinition eyeLeftRemark = new ColumnDefinition();
		eyeLeftRemark.setName("eyeLeftRemark");
		eyeLeftRemark.setColumnName("EYELEFTREMARK_");
		eyeLeftRemark.setJavaType("String");
		eyeLeftRemark.setLength(200);
		tableDefinition.addColumn(eyeLeftRemark);

		ColumnDefinition eyeRight = new ColumnDefinition();
		eyeRight.setName("eyeRight");
		eyeRight.setColumnName("EYERIGHT_");
		eyeRight.setJavaType("String");
		eyeRight.setLength(200);
		tableDefinition.addColumn(eyeRight);

		ColumnDefinition eyeRightRemark = new ColumnDefinition();
		eyeRightRemark.setName("eyeRightRemark");
		eyeRightRemark.setColumnName("EYERIGHTREMARK_");
		eyeRightRemark.setJavaType("String");
		eyeRightRemark.setLength(200);
		tableDefinition.addColumn(eyeRightRemark);

		ColumnDefinition eyesightLeft = new ColumnDefinition();
		eyesightLeft.setName("eyesightLeft");
		eyesightLeft.setColumnName("EYESIGHTLEFT_");
		eyesightLeft.setJavaType("Double");
		tableDefinition.addColumn(eyesightLeft);

		ColumnDefinition eyesightRight = new ColumnDefinition();
		eyesightRight.setName("eyesightRight");
		eyesightRight.setColumnName("EYESIGHTRIGHT_");
		eyesightRight.setJavaType("Double");
		tableDefinition.addColumn(eyesightRight);

		ColumnDefinition earLeft = new ColumnDefinition();
		earLeft.setName("earLeft");
		earLeft.setColumnName("EARLEFT_");
		earLeft.setJavaType("String");
		earLeft.setLength(200);
		tableDefinition.addColumn(earLeft);

		ColumnDefinition earLeftRemark = new ColumnDefinition();
		earLeftRemark.setName("earLeftRemark");
		earLeftRemark.setColumnName("EARLEFTREMARK_");
		earLeftRemark.setJavaType("String");
		earLeftRemark.setLength(200);
		tableDefinition.addColumn(earLeftRemark);

		ColumnDefinition earRight = new ColumnDefinition();
		earRight.setName("earRight");
		earRight.setColumnName("EARRIGHT_");
		earRight.setJavaType("String");
		earRight.setLength(200);
		tableDefinition.addColumn(earRight);

		ColumnDefinition earRightRemark = new ColumnDefinition();
		earRightRemark.setName("earRightRemark");
		earRightRemark.setColumnName("EARRIGHTREMARK_");
		earRightRemark.setJavaType("String");
		earRightRemark.setLength(200);
		tableDefinition.addColumn(earRightRemark);

		ColumnDefinition tooth = new ColumnDefinition();
		tooth.setName("tooth");
		tooth.setColumnName("TOOTH_");
		tooth.setJavaType("Integer");
		tableDefinition.addColumn(tooth);

		ColumnDefinition saprodontia = new ColumnDefinition();
		saprodontia.setName("saprodontia");
		saprodontia.setColumnName("SAPRODONTIA_");
		saprodontia.setJavaType("Integer");
		tableDefinition.addColumn(saprodontia);

		ColumnDefinition head = new ColumnDefinition();
		head.setName("head");
		head.setColumnName("HEAD_");
		head.setJavaType("String");
		head.setLength(200);
		tableDefinition.addColumn(head);

		ColumnDefinition headRemark = new ColumnDefinition();
		headRemark.setName("headRemark");
		headRemark.setColumnName("HEADREMARK_");
		headRemark.setJavaType("String");
		headRemark.setLength(200);
		tableDefinition.addColumn(headRemark);

		ColumnDefinition thorax = new ColumnDefinition();
		thorax.setName("thorax");
		thorax.setColumnName("THORAX_");
		thorax.setJavaType("String");
		thorax.setLength(200);
		tableDefinition.addColumn(thorax);

		ColumnDefinition thoraxRemark = new ColumnDefinition();
		thoraxRemark.setName("thoraxRemark");
		thoraxRemark.setColumnName("THORAXREMARK_");
		thoraxRemark.setJavaType("String");
		thoraxRemark.setLength(200);
		tableDefinition.addColumn(thoraxRemark);

		ColumnDefinition spine = new ColumnDefinition();
		spine.setName("spine");
		spine.setColumnName("SPINE_");
		spine.setJavaType("String");
		spine.setLength(200);
		tableDefinition.addColumn(spine);

		ColumnDefinition spineRemark = new ColumnDefinition();
		spineRemark.setName("spineRemark");
		spineRemark.setColumnName("SPINEREMARK_");
		spineRemark.setJavaType("String");
		spineRemark.setLength(200);
		tableDefinition.addColumn(spineRemark);

		ColumnDefinition pharyngeal = new ColumnDefinition();
		pharyngeal.setName("pharyngeal");
		pharyngeal.setColumnName("PHARYNGEAL_");
		pharyngeal.setJavaType("String");
		pharyngeal.setLength(200);
		tableDefinition.addColumn(pharyngeal);

		ColumnDefinition pharyngealRemark = new ColumnDefinition();
		pharyngealRemark.setName("pharyngealRemark");
		pharyngealRemark.setColumnName("PHARYNGEALREMARK_");
		pharyngealRemark.setJavaType("String");
		pharyngealRemark.setLength(200);
		tableDefinition.addColumn(pharyngealRemark);

		ColumnDefinition cardiopulmonary = new ColumnDefinition();
		cardiopulmonary.setName("cardiopulmonary");
		cardiopulmonary.setColumnName("CARDIOPULMONARY_");
		cardiopulmonary.setJavaType("String");
		cardiopulmonary.setLength(200);
		tableDefinition.addColumn(cardiopulmonary);

		ColumnDefinition cardiopulmonaryRemark = new ColumnDefinition();
		cardiopulmonaryRemark.setName("cardiopulmonaryRemark");
		cardiopulmonaryRemark.setColumnName("CARDIOPULMONARYREMARK_");
		cardiopulmonaryRemark.setJavaType("String");
		cardiopulmonaryRemark.setLength(200);
		tableDefinition.addColumn(cardiopulmonaryRemark);

		ColumnDefinition hepatolienal = new ColumnDefinition();
		hepatolienal.setName("hepatolienal");
		hepatolienal.setColumnName("HEPATOLIENAL_");
		hepatolienal.setJavaType("String");
		hepatolienal.setLength(200);
		tableDefinition.addColumn(hepatolienal);

		ColumnDefinition hepatolienalRemark = new ColumnDefinition();
		hepatolienalRemark.setName("hepatolienalRemark");
		hepatolienalRemark.setColumnName("HEPATOLIENALREMARK_");
		hepatolienalRemark.setJavaType("String");
		hepatolienalRemark.setLength(200);
		tableDefinition.addColumn(hepatolienalRemark);

		ColumnDefinition pudendum = new ColumnDefinition();
		pudendum.setName("pudendum");
		pudendum.setColumnName("PUDENDUM_");
		pudendum.setJavaType("String");
		pudendum.setLength(200);
		tableDefinition.addColumn(pudendum);

		ColumnDefinition pudendumRemark = new ColumnDefinition();
		pudendumRemark.setName("pudendumRemark");
		pudendumRemark.setColumnName("PUDENDUMREMARK_");
		pudendumRemark.setJavaType("String");
		pudendumRemark.setLength(200);
		tableDefinition.addColumn(pudendumRemark);

		ColumnDefinition skin = new ColumnDefinition();
		skin.setName("skin");
		skin.setColumnName("SKIN_");
		skin.setJavaType("String");
		skin.setLength(200);
		tableDefinition.addColumn(skin);

		ColumnDefinition skinRemark = new ColumnDefinition();
		skinRemark.setName("skinRemark");
		skinRemark.setColumnName("SKINREMARK_");
		skinRemark.setJavaType("String");
		skinRemark.setLength(200);
		tableDefinition.addColumn(skinRemark);

		ColumnDefinition lymphoid = new ColumnDefinition();
		lymphoid.setName("lymphoid");
		lymphoid.setColumnName("LYMPHOID_");
		lymphoid.setJavaType("String");
		lymphoid.setLength(200);
		tableDefinition.addColumn(lymphoid);

		ColumnDefinition lymphoidRemark = new ColumnDefinition();
		lymphoidRemark.setName("lymphoidRemark");
		lymphoidRemark.setColumnName("LYMPHOIDREMARK_");
		lymphoidRemark.setJavaType("String");
		lymphoidRemark.setLength(200);
		tableDefinition.addColumn(lymphoidRemark);

		ColumnDefinition bregma = new ColumnDefinition();
		bregma.setName("bregma");
		bregma.setColumnName("BREGMA_");
		bregma.setJavaType("String");
		bregma.setLength(200);
		tableDefinition.addColumn(bregma);

		ColumnDefinition bregmaRemark = new ColumnDefinition();
		bregmaRemark.setName("bregmaRemark");
		bregmaRemark.setColumnName("BREGMAREMARK_");
		bregmaRemark.setJavaType("String");
		bregmaRemark.setLength(200);
		tableDefinition.addColumn(bregmaRemark);

		ColumnDefinition oralogy = new ColumnDefinition();
		oralogy.setName("oralogy");
		oralogy.setColumnName("ORALOGY_");
		oralogy.setJavaType("String");
		oralogy.setLength(200);
		tableDefinition.addColumn(oralogy);

		ColumnDefinition oralogyRemark = new ColumnDefinition();
		oralogyRemark.setName("oralogyRemark");
		oralogyRemark.setColumnName("ORALOGYREMARK_");
		oralogyRemark.setJavaType("String");
		oralogyRemark.setLength(200);
		tableDefinition.addColumn(oralogyRemark);

		ColumnDefinition tonsil = new ColumnDefinition();
		tonsil.setName("tonsil");
		tonsil.setColumnName("TONSIL_");
		tonsil.setJavaType("String");
		tonsil.setLength(200);
		tableDefinition.addColumn(tonsil);

		ColumnDefinition tonsilRemark = new ColumnDefinition();
		tonsilRemark.setName("tonsilRemark");
		tonsilRemark.setColumnName("TONSILREMARK_");
		tonsilRemark.setJavaType("String");
		tonsilRemark.setLength(200);
		tableDefinition.addColumn(tonsilRemark);

		ColumnDefinition bone = new ColumnDefinition();
		bone.setName("bone");
		bone.setColumnName("BONE_");
		bone.setJavaType("String");
		bone.setLength(200);
		tableDefinition.addColumn(bone);

		ColumnDefinition boneRemark = new ColumnDefinition();
		boneRemark.setName("boneRemark");
		boneRemark.setColumnName("BONEREMARK_");
		boneRemark.setJavaType("String");
		boneRemark.setLength(200);
		tableDefinition.addColumn(boneRemark);

		ColumnDefinition birthDefect = new ColumnDefinition();
		birthDefect.setName("birthDefect");
		birthDefect.setColumnName("BIRTHDEFECT_");
		birthDefect.setJavaType("String");
		birthDefect.setLength(200);
		tableDefinition.addColumn(birthDefect);

		ColumnDefinition previousHistory = new ColumnDefinition();
		previousHistory.setName("previousHistory");
		previousHistory.setColumnName("PREVIOUSHISTORY_");
		previousHistory.setJavaType("String");
		previousHistory.setLength(500);
		tableDefinition.addColumn(previousHistory);

		ColumnDefinition hemoglobin = new ColumnDefinition();
		hemoglobin.setName("hemoglobin");
		hemoglobin.setColumnName("HEMOGLOBIN_");
		hemoglobin.setJavaType("String");
		hemoglobin.setLength(200);
		tableDefinition.addColumn(hemoglobin);

		ColumnDefinition hemoglobinValue = new ColumnDefinition();
		hemoglobinValue.setName("hemoglobinValue");
		hemoglobinValue.setColumnName("HEMOGLOBIN_VALUE_");
		hemoglobinValue.setJavaType("Double");
		tableDefinition.addColumn(hemoglobinValue);

		ColumnDefinition alt = new ColumnDefinition();
		alt.setName("alt");
		alt.setColumnName("ALT_");
		alt.setJavaType("String");
		alt.setLength(200);
		tableDefinition.addColumn(alt);

		ColumnDefinition altValue = new ColumnDefinition();
		altValue.setName("altValue");
		altValue.setColumnName("ALT_VALUE_");
		altValue.setJavaType("Double");
		tableDefinition.addColumn(altValue);

		ColumnDefinition hbsab = new ColumnDefinition();
		hbsab.setName("hbsab");
		hbsab.setColumnName("HBSAB_");
		hbsab.setJavaType("String");
		hbsab.setLength(1);
		tableDefinition.addColumn(hbsab);

		ColumnDefinition hbsabValue = new ColumnDefinition();
		hbsabValue.setName("hbsabValue");
		hbsabValue.setColumnName("HBSAB_VALUE_");
		hbsabValue.setJavaType("Double");
		tableDefinition.addColumn(hbsabValue);

		ColumnDefinition sgpt = new ColumnDefinition();
		sgpt.setName("sgpt");
		sgpt.setColumnName("SGPT_");
		sgpt.setJavaType("String");
		sgpt.setLength(1);
		tableDefinition.addColumn(sgpt);

		ColumnDefinition hvaigm = new ColumnDefinition();
		hvaigm.setName("hvaigm");
		hvaigm.setColumnName("HVAIGM_");
		hvaigm.setJavaType("String");
		hvaigm.setLength(1);
		tableDefinition.addColumn(hvaigm);

		ColumnDefinition type = new ColumnDefinition();
		type.setName("type");
		type.setColumnName("TYPE_");
		type.setJavaType("String");
		type.setLength(50);
		tableDefinition.addColumn(type);

		ColumnDefinition checkDate = new ColumnDefinition();
		checkDate.setName("checkDate");
		checkDate.setColumnName("CHECKDATE_");
		checkDate.setJavaType("Date");
		tableDefinition.addColumn(checkDate);

		ColumnDefinition checkDoctor = new ColumnDefinition();
		checkDoctor.setName("checkDoctor");
		checkDoctor.setColumnName("CHECKDOCTOR_");
		checkDoctor.setJavaType("String");
		checkDoctor.setLength(50);
		tableDefinition.addColumn(checkDoctor);

		ColumnDefinition checkDoctorId = new ColumnDefinition();
		checkDoctorId.setName("checkDoctorId");
		checkDoctorId.setColumnName("CHECKDOCTORID_");
		checkDoctorId.setJavaType("String");
		checkDoctorId.setLength(50);
		tableDefinition.addColumn(checkDoctorId);

		ColumnDefinition checkResult = new ColumnDefinition();
		checkResult.setName("checkResult");
		checkResult.setColumnName("CHECKRESULT_");
		checkResult.setJavaType("String");
		checkResult.setLength(500);
		tableDefinition.addColumn(checkResult);

		ColumnDefinition checkOrganization = new ColumnDefinition();
		checkOrganization.setName("checkOrganization");
		checkOrganization.setColumnName("CHECKORGANIZATION_");
		checkOrganization.setJavaType("String");
		checkOrganization.setLength(200);
		tableDefinition.addColumn(checkOrganization);

		ColumnDefinition checkOrganizationId = new ColumnDefinition();
		checkOrganizationId.setName("checkOrganizationId");
		checkOrganizationId.setColumnName("CHECKORGANIZATIONID_");
		checkOrganizationId.setJavaType("Long");
		tableDefinition.addColumn(checkOrganizationId);

		ColumnDefinition doctorSuggest = new ColumnDefinition();
		doctorSuggest.setName("doctorSuggest");
		doctorSuggest.setColumnName("DOCTORSUGGEST_");
		doctorSuggest.setJavaType("String");
		doctorSuggest.setLength(50);
		tableDefinition.addColumn(doctorSuggest);

		ColumnDefinition healthEvaluate = new ColumnDefinition();
		healthEvaluate.setName("healthEvaluate");
		healthEvaluate.setColumnName("HEALTHEVALUATE_");
		healthEvaluate.setJavaType("String");
		healthEvaluate.setLength(500);
		tableDefinition.addColumn(healthEvaluate);

		ColumnDefinition year = new ColumnDefinition();
		year.setName("year");
		year.setColumnName("YEAR_");
		year.setJavaType("Integer");
		tableDefinition.addColumn(year);

		ColumnDefinition month = new ColumnDefinition();
		month.setName("month");
		month.setColumnName("MONTH_");
		month.setJavaType("Integer");
		tableDefinition.addColumn(month);

		ColumnDefinition ageOfTheMoon = new ColumnDefinition();
		ageOfTheMoon.setName("ageOfTheMoon");
		ageOfTheMoon.setColumnName("AGEOFTHEMOON_");
		ageOfTheMoon.setJavaType("Integer");
		tableDefinition.addColumn(ageOfTheMoon);

		ColumnDefinition remark = new ColumnDefinition();
		remark.setName("remark");
		remark.setColumnName("REMARK_");
		remark.setJavaType("String");
		remark.setLength(500);
		tableDefinition.addColumn(remark);

		ColumnDefinition confirmBy = new ColumnDefinition();
		confirmBy.setName("confirmBy");
		confirmBy.setColumnName("CONFIRMBY_");
		confirmBy.setJavaType("String");
		confirmBy.setLength(50);
		tableDefinition.addColumn(confirmBy);

		ColumnDefinition confirmTime = new ColumnDefinition();
		confirmTime.setName("confirmTime");
		confirmTime.setColumnName("CONFIRMTIME_");
		confirmTime.setJavaType("Date");
		tableDefinition.addColumn(confirmTime);

		ColumnDefinition createBy = new ColumnDefinition();
		createBy.setName("createBy");
		createBy.setColumnName("CREATEBY_");
		createBy.setJavaType("String");
		createBy.setLength(50);
		tableDefinition.addColumn(createBy);

		ColumnDefinition createTime = new ColumnDefinition();
		createTime.setName("createTime");
		createTime.setColumnName("CREATETIME_");
		createTime.setJavaType("Date");
		tableDefinition.addColumn(createTime);

		return tableDefinition;
	}

	public static TableDefinition createTable() {
		TableDefinition tableDefinition = getTableDefinition(TABLENAME);
		if (!DBUtils.tableExists(TABLENAME)) {
			DBUtils.createTable(tableDefinition);
		} else {
			DBUtils.alterTable(tableDefinition);
		}
		return tableDefinition;
	}

	public static TableDefinition createTable(String tableName) {
		TableDefinition tableDefinition = getTableDefinition(tableName);
		if (!DBUtils.tableExists(tableName)) {
			DBUtils.createTable(tableDefinition);
		} else {
			DBUtils.alterTable(tableDefinition);
		}
		return tableDefinition;
	}

	public static void processDataRequest(DataRequest dataRequest) {
		if (dataRequest != null) {
			if (dataRequest.getFilter() != null) {
				if (dataRequest.getFilter().getField() != null) {
					dataRequest.getFilter().setColumn(columnMap.get(dataRequest.getFilter().getField()));
					dataRequest.getFilter().setJavaType(javaTypeMap.get(dataRequest.getFilter().getField()));
				}

				List<FilterDescriptor> filters = dataRequest.getFilter().getFilters();
				for (FilterDescriptor filter : filters) {
					filter.setParent(dataRequest.getFilter());
					if (filter.getField() != null) {
						filter.setColumn(columnMap.get(filter.getField()));
						filter.setJavaType(javaTypeMap.get(filter.getField()));
					}

					List<FilterDescriptor> subFilters = filter.getFilters();
					for (FilterDescriptor f : subFilters) {
						f.setColumn(columnMap.get(f.getField()));
						f.setJavaType(javaTypeMap.get(f.getField()));
						f.setParent(filter);
					}
				}
			}
		}
	}

	private MedicalExaminationDomainFactory() {

	}

}
