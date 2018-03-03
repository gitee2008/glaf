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
public class DietaryStatisticsDomainFactory {

	public static final String TABLENAME = "HEALTH_DIETARY_STATISTICS";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("tenantId", "TENANTID_");
		columnMap.put("name", "NAME_");
		columnMap.put("title", "TITLE_");
		columnMap.put("value", "VALUE_");
		columnMap.put("mealType", "MEALTYPE_");
		columnMap.put("sysFlag", "SYSFLAG_");
		columnMap.put("type", "TYPE_");
		columnMap.put("dayOfWeek", "DAYOFWEEK_");
		columnMap.put("semester", "SEMESTER_");
		columnMap.put("year", "YEAR_");
		columnMap.put("week", "WEEK_");
		columnMap.put("batchNo", "BATCHNO_");
		columnMap.put("sortNo", "SORTNO_");
		columnMap.put("suitNo", "SUITNO_");
		columnMap.put("createTime", "CREATETIME_");

		javaTypeMap.put("id", "Long");
		javaTypeMap.put("tenantId", "String");
		javaTypeMap.put("name", "String");
		javaTypeMap.put("title", "String");
		javaTypeMap.put("value", "Double");
		javaTypeMap.put("mealType", "String");
		javaTypeMap.put("sysFlag", "String");
		javaTypeMap.put("type", "String");
		javaTypeMap.put("dayOfWeek", "Integer");
		javaTypeMap.put("semester", "Integer");
		javaTypeMap.put("year", "Integer");
		javaTypeMap.put("week", "Integer");
		javaTypeMap.put("batchNo", "String");
		javaTypeMap.put("sortNo", "Integer");
		javaTypeMap.put("suitNo", "Integer");
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
		tableDefinition.setName("DietaryStatistics");

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

		ColumnDefinition name = new ColumnDefinition();
		name.setName("name");
		name.setColumnName("NAME_");
		name.setJavaType("String");
		name.setLength(200);
		tableDefinition.addColumn(name);

		ColumnDefinition title = new ColumnDefinition();
		title.setName("title");
		title.setColumnName("TITLE_");
		title.setJavaType("String");
		title.setLength(200);
		tableDefinition.addColumn(title);

		ColumnDefinition value = new ColumnDefinition();
		value.setName("value");
		value.setColumnName("VALUE_");
		value.setJavaType("Double");
		tableDefinition.addColumn(value);

		ColumnDefinition mealType = new ColumnDefinition();
		mealType.setName("mealType");
		mealType.setColumnName("MEALTYPE_");
		mealType.setJavaType("String");
		mealType.setLength(50);
		tableDefinition.addColumn(mealType);

		ColumnDefinition sysFlag = new ColumnDefinition();
		sysFlag.setName("sysFlag");
		sysFlag.setColumnName("SYSFLAG_");
		sysFlag.setJavaType("String");
		sysFlag.setLength(1);
		tableDefinition.addColumn(sysFlag);

		ColumnDefinition type = new ColumnDefinition();
		type.setName("type");
		type.setColumnName("TYPE_");
		type.setJavaType("String");
		type.setLength(50);
		tableDefinition.addColumn(type);

		ColumnDefinition dayOfWeek = new ColumnDefinition();
		dayOfWeek.setName("dayOfWeek");
		dayOfWeek.setColumnName("DAYOFWEEK_");
		dayOfWeek.setJavaType("Integer");
		tableDefinition.addColumn(dayOfWeek);

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

		ColumnDefinition week = new ColumnDefinition();
		week.setName("week");
		week.setColumnName("WEEK_");
		week.setJavaType("Integer");
		tableDefinition.addColumn(week);

		ColumnDefinition batchNo = new ColumnDefinition();
		batchNo.setName("batchNo");
		batchNo.setColumnName("BATCHNO_");
		batchNo.setJavaType("String");
		batchNo.setLength(200);
		tableDefinition.addColumn(batchNo);

		ColumnDefinition sortNo = new ColumnDefinition();
		sortNo.setName("sortNo");
		sortNo.setColumnName("SORTNO_");
		sortNo.setJavaType("Integer");
		tableDefinition.addColumn(sortNo);

		ColumnDefinition suitNo = new ColumnDefinition();
		suitNo.setName("suitNo");
		suitNo.setColumnName("SUITNO_");
		suitNo.setJavaType("Integer");
		tableDefinition.addColumn(suitNo);

		ColumnDefinition createTime = new ColumnDefinition();
		createTime.setName("createTime");
		createTime.setColumnName("CREATETIME_");
		createTime.setJavaType("Date");
		tableDefinition.addColumn(createTime);

		return tableDefinition;
	}

	private DietaryStatisticsDomainFactory() {

	}

}
