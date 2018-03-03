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

import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.domain.TableDefinition;
import com.glaf.core.util.DBUtils;

/**
 * 
 * 实体数据工厂类
 *
 */
public class TriphopathiaItemDomainFactory {

	public static final String TABLENAME = "HEALTH_TRIPHOPATHIA_ITEM";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("triphopathiaId", "TRIPHOPATHIAID_");
		columnMap.put("tenantId", "TENANTID_");
		columnMap.put("gradeId", "GRADEID_");
		columnMap.put("personId", "PERSONID_");
		columnMap.put("name", "NAME_");
		columnMap.put("sex", "SEX_");
		columnMap.put("type", "TYPE_");
		columnMap.put("height", "HEIGHT_");
		columnMap.put("heightLevel", "HEIGHTLEVEL_");
		columnMap.put("heightEvaluate", "HEIGHTEVALUATE_");
		columnMap.put("weight", "WEIGHT_");
		columnMap.put("weightLevel", "WEIGHTLEVEL_");
		columnMap.put("weightEvaluate", "WEIGHTEVALUATE_");
		columnMap.put("weightHeightPercent", "WEIGHTHEIGHTPERCENT_");
		columnMap.put("bmi", "BMI_");
		columnMap.put("bmiIndex", "BMIINDEX_");
		columnMap.put("bmiEvaluate", "BMIEVALUATE_");
		columnMap.put("symptom", "SYMPTOM_");
		columnMap.put("suggest", "SUGGEST_");
		columnMap.put("result", "RESULT_");
		columnMap.put("evaluate", "EVALUATE_");
		columnMap.put("ageOfTheMoon", "AGEOFTHEMOON_");
		columnMap.put("remark", "REMARK_");
		columnMap.put("checkDate", "CHECKDATE_");
		columnMap.put("checkDoctor", "CHECKDOCTOR_");
		columnMap.put("checkDoctorId", "CHECKDOCTORID_");
		columnMap.put("createBy", "CREATEBY_");
		columnMap.put("createTime", "CREATETIME_");

		javaTypeMap.put("id", "Long");
		javaTypeMap.put("triphopathiaId", "Long");
		javaTypeMap.put("tenantId", "String");
		javaTypeMap.put("gradeId", "String");
		javaTypeMap.put("personId", "String");
		javaTypeMap.put("name", "String");
		javaTypeMap.put("sex", "String");
		javaTypeMap.put("type", "String");
		javaTypeMap.put("height", "Double");
		javaTypeMap.put("heightLevel", "Integer");
		javaTypeMap.put("heightEvaluate", "String");
		javaTypeMap.put("weight", "Double");
		javaTypeMap.put("weightLevel", "Integer");
		javaTypeMap.put("weightEvaluate", "String");
		javaTypeMap.put("weightHeightPercent", "Double");
		javaTypeMap.put("bmi", "Double");
		javaTypeMap.put("bmiIndex", "Double");
		javaTypeMap.put("bmiEvaluate", "String");
		javaTypeMap.put("symptom", "String");
		javaTypeMap.put("suggest", "String");
		javaTypeMap.put("result", "String");
		javaTypeMap.put("evaluate", "String");
		javaTypeMap.put("ageOfTheMoon", "Integer");
		javaTypeMap.put("remark", "String");
		javaTypeMap.put("checkDate", "Date");
		javaTypeMap.put("checkDoctor", "String");
		javaTypeMap.put("checkDoctorId", "String");
		javaTypeMap.put("createBy", "String");
		javaTypeMap.put("createTime", "Date");
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
		tableDefinition.setName("TriphopathiaItem");

		ColumnDefinition idColumn = new ColumnDefinition();
		idColumn.setName("id");
		idColumn.setColumnName("ID_");
		idColumn.setJavaType("Long");
		tableDefinition.setIdColumn(idColumn);

		ColumnDefinition triphopathiaId = new ColumnDefinition();
		triphopathiaId.setName("triphopathiaId");
		triphopathiaId.setColumnName("TRIPHOPATHIAID_");
		triphopathiaId.setJavaType("Long");
		tableDefinition.addColumn(triphopathiaId);

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

		ColumnDefinition type = new ColumnDefinition();
		type.setName("type");
		type.setColumnName("TYPE_");
		type.setJavaType("String");
		type.setLength(50);
		tableDefinition.addColumn(type);

		ColumnDefinition height = new ColumnDefinition();
		height.setName("height");
		height.setColumnName("HEIGHT_");
		height.setJavaType("Double");
		tableDefinition.addColumn(height);

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

		ColumnDefinition symptom = new ColumnDefinition();
		symptom.setName("symptom");
		symptom.setColumnName("SYMPTOM_");
		symptom.setJavaType("String");
		symptom.setLength(500);
		tableDefinition.addColumn(symptom);

		ColumnDefinition suggest = new ColumnDefinition();
		suggest.setName("suggest");
		suggest.setColumnName("SUGGEST_");
		suggest.setJavaType("String");
		suggest.setLength(500);
		tableDefinition.addColumn(suggest);

		ColumnDefinition result = new ColumnDefinition();
		result.setName("result");
		result.setColumnName("RESULT_");
		result.setJavaType("String");
		result.setLength(50);
		tableDefinition.addColumn(result);

		ColumnDefinition evaluate = new ColumnDefinition();
		evaluate.setName("evaluate");
		evaluate.setColumnName("EVALUATE_");
		evaluate.setJavaType("String");
		evaluate.setLength(500);
		tableDefinition.addColumn(evaluate);

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

	private TriphopathiaItemDomainFactory() {

	}

}
