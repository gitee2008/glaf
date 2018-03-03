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
public class GrowthStandardDomainFactory {

	public static final String TABLENAME = "HEALTH_GROWTH_STANDARD";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("age", "AGE_");
		columnMap.put("ageOfTheMoon", "AGEOFTHEMOON_");
		columnMap.put("complexKey", "COMPLEXKEY_");
		columnMap.put("month", "MONTH_");
		columnMap.put("sex", "SEX_");
		columnMap.put("height", "HEIGHT_");
		columnMap.put("weight", "WEIGHT_");
		columnMap.put("percent3", "PERCENT3_");
		columnMap.put("percent15", "PERCENT15_");
		columnMap.put("percent50", "PERCENT50_");
		columnMap.put("percent85", "PERCENT85_");
		columnMap.put("percent97", "PERCENT97_");
		columnMap.put("oneDSDeviation", "ONEDSDEVIATION_");
		columnMap.put("twoDSDeviation", "TWODSDEVIATION_");
		columnMap.put("threeDSDeviation", "THREEDSDEVIATION_");
		columnMap.put("fourDSDeviation", "FOURDSDEVIATION_");
		columnMap.put("median", "MEDIAN_");
		columnMap.put("negativeOneDSDeviation", "NEGATIVEONEDSDEVIATION_");
		columnMap.put("negativeTwoDSDeviation", "NEGATIVETWODSDEVIATION_");
		columnMap.put("negativeThreeDSDeviation", "NEGATIVETHREEDSDEVIATION_");
		columnMap.put("negativeFourDSDeviation", "NEGATIVEFOURDSDEVIATION_");
		columnMap.put("standardType", "STANDARDTYPE_");
		columnMap.put("type", "TYPE_");
		columnMap.put("createBy", "CREATEBY_");
		columnMap.put("createTime", "CREATETIME_");

		javaTypeMap.put("id", "Long");
		javaTypeMap.put("age", "Integer");
		javaTypeMap.put("ageOfTheMoon", "Integer");
		javaTypeMap.put("complexKey", "String");
		javaTypeMap.put("month", "Integer");
		javaTypeMap.put("sex", "String");
		javaTypeMap.put("height", "Double");
		javaTypeMap.put("weight", "Double");
		javaTypeMap.put("percent3", "Double");
		javaTypeMap.put("percent15", "Double");
		javaTypeMap.put("percent50", "Double");
		javaTypeMap.put("percent85", "Double");
		javaTypeMap.put("percent97", "Double");
		javaTypeMap.put("oneDSDeviation", "Double");
		javaTypeMap.put("twoDSDeviation", "Double");
		javaTypeMap.put("threeDSDeviation", "Double");
		javaTypeMap.put("fourDSDeviation", "Double");
		javaTypeMap.put("median", "Double");
		javaTypeMap.put("negativeOneDSDeviation", "Double");
		javaTypeMap.put("negativeTwoDSDeviation", "Double");
		javaTypeMap.put("negativeThreeDSDeviation", "Double");
		javaTypeMap.put("negativeFourDSDeviation", "Double");
		javaTypeMap.put("standardType", "String");
		javaTypeMap.put("type", "String");
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
		tableDefinition.setName("GrowthStandard");

		ColumnDefinition idColumn = new ColumnDefinition();
		idColumn.setName("id");
		idColumn.setColumnName("ID_");
		idColumn.setJavaType("Long");
		tableDefinition.setIdColumn(idColumn);

		ColumnDefinition age = new ColumnDefinition();
		age.setName("age");
		age.setColumnName("AGE_");
		age.setJavaType("Integer");
		tableDefinition.addColumn(age);

		ColumnDefinition ageOfTheMoon = new ColumnDefinition();
		ageOfTheMoon.setName("ageOfTheMoon");
		ageOfTheMoon.setColumnName("AGEOFTHEMOON_");
		ageOfTheMoon.setJavaType("Integer");
		tableDefinition.addColumn(ageOfTheMoon);

		ColumnDefinition complexKey = new ColumnDefinition();
		complexKey.setName("complexKey");
		complexKey.setColumnName("COMPLEXKEY_");
		complexKey.setJavaType("String");
		complexKey.setLength(200);
		tableDefinition.addColumn(complexKey);

		ColumnDefinition month = new ColumnDefinition();
		month.setName("month");
		month.setColumnName("MONTH_");
		month.setJavaType("Integer");
		tableDefinition.addColumn(month);

		ColumnDefinition sex = new ColumnDefinition();
		sex.setName("sex");
		sex.setColumnName("SEX_");
		sex.setJavaType("String");
		sex.setLength(1);
		tableDefinition.addColumn(sex);

		ColumnDefinition height = new ColumnDefinition();
		height.setName("height");
		height.setColumnName("HEIGHT_");
		height.setJavaType("Double");
		tableDefinition.addColumn(height);

		ColumnDefinition weight = new ColumnDefinition();
		weight.setName("weight");
		weight.setColumnName("WEIGHT_");
		weight.setJavaType("Double");
		tableDefinition.addColumn(weight);

		ColumnDefinition percent3 = new ColumnDefinition();
		percent3.setName("percent3");
		percent3.setColumnName("PERCENT3_");
		percent3.setJavaType("Double");
		tableDefinition.addColumn(percent3);

		ColumnDefinition percent15 = new ColumnDefinition();
		percent15.setName("percent15");
		percent15.setColumnName("PERCENT15_");
		percent15.setJavaType("Double");
		tableDefinition.addColumn(percent15);

		ColumnDefinition percent50 = new ColumnDefinition();
		percent50.setName("percent50");
		percent50.setColumnName("PERCENT50_");
		percent50.setJavaType("Double");
		tableDefinition.addColumn(percent50);

		ColumnDefinition percent85 = new ColumnDefinition();
		percent85.setName("percent85");
		percent85.setColumnName("PERCENT85_");
		percent85.setJavaType("Double");
		tableDefinition.addColumn(percent85);

		ColumnDefinition percent97 = new ColumnDefinition();
		percent97.setName("percent97");
		percent97.setColumnName("PERCENT97_");
		percent97.setJavaType("Double");
		tableDefinition.addColumn(percent97);

		ColumnDefinition oneDSDeviation = new ColumnDefinition();
		oneDSDeviation.setName("oneDSDeviation");
		oneDSDeviation.setColumnName("ONEDSDEVIATION_");
		oneDSDeviation.setJavaType("Double");
		tableDefinition.addColumn(oneDSDeviation);

		ColumnDefinition twoDSDeviation = new ColumnDefinition();
		twoDSDeviation.setName("twoDSDeviation");
		twoDSDeviation.setColumnName("TWODSDEVIATION_");
		twoDSDeviation.setJavaType("Double");
		tableDefinition.addColumn(twoDSDeviation);

		ColumnDefinition threeDSDeviation = new ColumnDefinition();
		threeDSDeviation.setName("threeDSDeviation");
		threeDSDeviation.setColumnName("THREEDSDEVIATION_");
		threeDSDeviation.setJavaType("Double");
		tableDefinition.addColumn(threeDSDeviation);

		ColumnDefinition fourDSDeviation = new ColumnDefinition();
		fourDSDeviation.setName("fourDSDeviation");
		fourDSDeviation.setColumnName("FOURDSDEVIATION_");
		fourDSDeviation.setJavaType("Double");
		tableDefinition.addColumn(fourDSDeviation);

		ColumnDefinition median = new ColumnDefinition();
		median.setName("median");
		median.setColumnName("MEDIAN_");
		median.setJavaType("Double");
		tableDefinition.addColumn(median);

		ColumnDefinition negativeOneDSDeviation = new ColumnDefinition();
		negativeOneDSDeviation.setName("negativeOneDSDeviation");
		negativeOneDSDeviation.setColumnName("NEGATIVEONEDSDEVIATION_");
		negativeOneDSDeviation.setJavaType("Double");
		tableDefinition.addColumn(negativeOneDSDeviation);

		ColumnDefinition negativeTwoDSDeviation = new ColumnDefinition();
		negativeTwoDSDeviation.setName("negativeTwoDSDeviation");
		negativeTwoDSDeviation.setColumnName("NEGATIVETWODSDEVIATION_");
		negativeTwoDSDeviation.setJavaType("Double");
		tableDefinition.addColumn(negativeTwoDSDeviation);

		ColumnDefinition negativeThreeDSDeviation = new ColumnDefinition();
		negativeThreeDSDeviation.setName("negativeThreeDSDeviation");
		negativeThreeDSDeviation.setColumnName("NEGATIVETHREEDSDEVIATION_");
		negativeThreeDSDeviation.setJavaType("Double");
		tableDefinition.addColumn(negativeThreeDSDeviation);

		ColumnDefinition negativeFourDSDeviation = new ColumnDefinition();
		negativeFourDSDeviation.setName("negativeFourDSDeviation");
		negativeFourDSDeviation.setColumnName("NEGATIVEFOURDSDEVIATION_");
		negativeFourDSDeviation.setJavaType("Double");
		tableDefinition.addColumn(negativeFourDSDeviation);

		ColumnDefinition standardType = new ColumnDefinition();
		standardType.setName("standardType");
		standardType.setColumnName("STANDARDTYPE_");
		standardType.setJavaType("String");
		standardType.setLength(50);
		tableDefinition.addColumn(standardType);

		ColumnDefinition type = new ColumnDefinition();
		type.setName("type");
		type.setColumnName("TYPE_");
		type.setJavaType("String");
		type.setLength(50);
		tableDefinition.addColumn(type);

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

	private GrowthStandardDomainFactory() {

	}

}
