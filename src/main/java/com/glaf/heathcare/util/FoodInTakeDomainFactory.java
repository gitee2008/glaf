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
public class FoodInTakeDomainFactory {

	public static final String TABLENAME = "HEALTH_FOOD_INTAKE";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("tenantId", "TENANTID_");
		columnMap.put("gradeId", "GRADEID_");
		columnMap.put("foodId", "FOODID_");
		columnMap.put("foodName", "FOODNAME_");
		columnMap.put("foodNodeId", "FOODNODEID_");
		columnMap.put("typeId", "TYPEID_");
		columnMap.put("mealTime", "MEALTIME_");
		columnMap.put("person", "PERSON_");
		columnMap.put("semester", "SEMESTER_");
		columnMap.put("year", "YEAR_");
		columnMap.put("month", "MONTH_");
		columnMap.put("fullDay", "FULLDAY_");
		columnMap.put("allocationWeight", "ALLOCATIONWEIGHT_");
		columnMap.put("remainWeight", "REMAINWEIGHT_");
		columnMap.put("mealWeight", "MEALWEIGHT_");
		columnMap.put("createBy", "CREATEBY_");
		columnMap.put("createTime", "CREATETIME_");

		javaTypeMap.put("id", "Long");
		javaTypeMap.put("tenantId", "String");
		javaTypeMap.put("gradeId", "String");
		javaTypeMap.put("foodId", "Long");
		javaTypeMap.put("foodName", "String");
		javaTypeMap.put("foodNodeId", "Long");
		javaTypeMap.put("typeId", "Long");
		javaTypeMap.put("mealTime", "Date");
		javaTypeMap.put("person", "Integer");
		javaTypeMap.put("semester", "Integer");
		javaTypeMap.put("year", "Integer");
		javaTypeMap.put("month", "Integer");
		javaTypeMap.put("fullDay", "Integer");
		javaTypeMap.put("allocationWeight", "Double");
		javaTypeMap.put("remainWeight", "Double");
		javaTypeMap.put("mealWeight", "Double");
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
		tableDefinition.setName("FoodInTake");

		ColumnDefinition idColumn = new ColumnDefinition();
		idColumn.setName("id");
		idColumn.setColumnName("ID_");
		idColumn.setJavaType("Long");
		tableDefinition.setIdColumn(idColumn);

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

		ColumnDefinition foodId = new ColumnDefinition();
		foodId.setName("foodId");
		foodId.setColumnName("FOODID_");
		foodId.setJavaType("Long");
		tableDefinition.addColumn(foodId);

		ColumnDefinition foodName = new ColumnDefinition();
		foodName.setName("foodName");
		foodName.setColumnName("FOODNAME_");
		foodName.setJavaType("String");
		foodName.setLength(200);
		tableDefinition.addColumn(foodName);

		ColumnDefinition foodNodeId = new ColumnDefinition();
		foodNodeId.setName("foodNodeId");
		foodNodeId.setColumnName("FOODNODEID_");
		foodNodeId.setJavaType("Long");
		tableDefinition.addColumn(foodNodeId);

		ColumnDefinition typeId = new ColumnDefinition();
		typeId.setName("typeId");
		typeId.setColumnName("TYPEID_");
		typeId.setJavaType("Long");
		tableDefinition.addColumn(typeId);

		ColumnDefinition mealTime = new ColumnDefinition();
		mealTime.setName("mealTime");
		mealTime.setColumnName("MEALTIME_");
		mealTime.setJavaType("Date");
		tableDefinition.addColumn(mealTime);

		ColumnDefinition person = new ColumnDefinition();
		person.setName("person");
		person.setColumnName("PERSON_");
		person.setJavaType("Integer");
		tableDefinition.addColumn(person);

		ColumnDefinition semester = new ColumnDefinition();
		semester.setName("semester");
		semester.setColumnName("SEMESTER_");
		semester.setJavaType("Integer");
		tableDefinition.addColumn(semester);

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

		ColumnDefinition fullDay = new ColumnDefinition();
		fullDay.setName("fullDay");
		fullDay.setColumnName("FULLDAY_");
		fullDay.setJavaType("Integer");
		tableDefinition.addColumn(fullDay);

		ColumnDefinition allocationWeight = new ColumnDefinition();
		allocationWeight.setName("allocationWeight");
		allocationWeight.setColumnName("ALLOCATIONWEIGHT_");
		allocationWeight.setJavaType("Double");
		tableDefinition.addColumn(allocationWeight);

		ColumnDefinition remainWeight = new ColumnDefinition();
		remainWeight.setName("remainWeight");
		remainWeight.setColumnName("REMAINWEIGHT_");
		remainWeight.setJavaType("Double");
		tableDefinition.addColumn(remainWeight);

		ColumnDefinition mealWeight = new ColumnDefinition();
		mealWeight.setName("mealWeight");
		mealWeight.setColumnName("MEALWEIGHT_");
		mealWeight.setJavaType("Double");
		tableDefinition.addColumn(mealWeight);

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

	private FoodInTakeDomainFactory() {

	}

}
