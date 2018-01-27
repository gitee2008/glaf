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

package com.glaf.matrix.data.util;

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
public class SqlDefinitionDomainFactory {

	public static final String TABLENAME = "SYS_SQL";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("parentId", "PARENTID_");
		columnMap.put("name", "NAME_");
		columnMap.put("code", "CODE_");
		columnMap.put("uuid", "UUID_");
		columnMap.put("title", "TITLE_");
		columnMap.put("sql", "SQL_");
		columnMap.put("countSql", "COUNTSQL_");
		columnMap.put("type", "TYPE_");
		columnMap.put("operation", "OPERATION_");
		columnMap.put("rowKey", "ROWKEY_");
		columnMap.put("scheduleFlag", "SCHEDULEFLAG_");
		columnMap.put("cacheFlag", "CACHEFLAG_");
		columnMap.put("dataItemFlag", "DATAITEMFLAG_");
		columnMap.put("fetchFlag", "FETCHFLAG_");
		columnMap.put("deleteFetch", "DELETEFETCH_");
		columnMap.put("exportFlag", "EXPORTFLAG_");
		columnMap.put("exportTableName", "EXPORTTABLENAME_");
		columnMap.put("exportTemplate", "EXPORTTEMPLATE_");
		columnMap.put("targetTableName", "TARGETTABLENAME_");
		columnMap.put("shareFlag", "SHAREFLAG_");
		columnMap.put("publicFlag", "PUBLICFLAG_");
		columnMap.put("saveFlag", "SAVEFLAG_");
		columnMap.put("aggregationFlag", "AGGREGATIONFLAG_");
		columnMap.put("ordinal", "ORDINAL_");
		columnMap.put("locked", "LOCKED_");
		columnMap.put("createBy", "CREATEBY_");
		columnMap.put("createTime", "CREATETIME_");
		columnMap.put("updateBy", "UPDATEBY_");
		columnMap.put("updateTime", "UPDATETIME_");

		javaTypeMap.put("id", "Long");
		javaTypeMap.put("parentId", "Long");
		javaTypeMap.put("name", "String");
		javaTypeMap.put("code", "String");
		javaTypeMap.put("uuid", "String");
		javaTypeMap.put("title", "String");
		javaTypeMap.put("sql", "String");
		javaTypeMap.put("countSql", "String");
		javaTypeMap.put("type", "String");
		javaTypeMap.put("operation", "String");
		javaTypeMap.put("rowKey", "String");
		javaTypeMap.put("scheduleFlag", "String");
		javaTypeMap.put("cacheFlag", "String");
		javaTypeMap.put("dataItemFlag", "String");
		javaTypeMap.put("deleteFetch", "String");
		javaTypeMap.put("fetchFlag", "String");
		javaTypeMap.put("exportFlag", "String");
		javaTypeMap.put("exportTableName", "String");
		javaTypeMap.put("exportTemplate", "String");
		javaTypeMap.put("targetTableName", "String");
		javaTypeMap.put("shareFlag", "String");
		javaTypeMap.put("publicFlag", "String");
		javaTypeMap.put("saveFlag", "String");
		javaTypeMap.put("aggregationFlag", "String");
		javaTypeMap.put("ordinal", "Integer");
		javaTypeMap.put("locked", "Integer");
		javaTypeMap.put("createBy", "String");
		javaTypeMap.put("createTime", "Date");
		javaTypeMap.put("updateBy", "String");
		javaTypeMap.put("updateTime", "Date");
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
		tableDefinition.setName("SqlDefinition");

		ColumnDefinition idColumn = new ColumnDefinition();
		idColumn.setName("id");
		idColumn.setColumnName("ID_");
		idColumn.setJavaType("Long");
		tableDefinition.setIdColumn(idColumn);

		ColumnDefinition parentId = new ColumnDefinition();
		parentId.setName("parentId");
		parentId.setColumnName("PARENTID_");
		parentId.setJavaType("Long");
		tableDefinition.addColumn(parentId);

		ColumnDefinition name = new ColumnDefinition();
		name.setName("name");
		name.setColumnName("NAME_");
		name.setJavaType("String");
		name.setLength(50);
		tableDefinition.addColumn(name);

		ColumnDefinition code = new ColumnDefinition();
		code.setName("code");
		code.setColumnName("CODE_");
		code.setJavaType("String");
		code.setLength(50);
		tableDefinition.addColumn(code);

		ColumnDefinition uuid = new ColumnDefinition();
		uuid.setName("uuid");
		uuid.setColumnName("UUID_");
		uuid.setJavaType("String");
		uuid.setLength(50);
		tableDefinition.addColumn(uuid);

		ColumnDefinition title = new ColumnDefinition();
		title.setName("title");
		title.setColumnName("TITLE_");
		title.setJavaType("String");
		title.setLength(200);
		tableDefinition.addColumn(title);

		ColumnDefinition sql = new ColumnDefinition();
		sql.setName("sql");
		sql.setColumnName("SQL_");
		sql.setJavaType("String");
		sql.setLength(2000);
		tableDefinition.addColumn(sql);

		ColumnDefinition countSql = new ColumnDefinition();
		countSql.setName("countSql");
		countSql.setColumnName("COUNTSQL_");
		countSql.setJavaType("String");
		countSql.setLength(2000);
		tableDefinition.addColumn(countSql);

		ColumnDefinition type = new ColumnDefinition();
		type.setName("type");
		type.setColumnName("TYPE_");
		type.setJavaType("String");
		type.setLength(20);
		tableDefinition.addColumn(type);

		ColumnDefinition operation = new ColumnDefinition();
		operation.setName("operation");
		operation.setColumnName("OPERATION_");
		operation.setJavaType("String");
		operation.setLength(50);
		tableDefinition.addColumn(operation);

		ColumnDefinition rowKey = new ColumnDefinition();
		rowKey.setName("rowKey");
		rowKey.setColumnName("ROWKEY_");
		rowKey.setJavaType("String");
		rowKey.setLength(50);
		tableDefinition.addColumn(rowKey);

		ColumnDefinition scheduleFlag = new ColumnDefinition();
		scheduleFlag.setName("scheduleFlag");
		scheduleFlag.setColumnName("SCHEDULEFLAG_");
		scheduleFlag.setJavaType("String");
		scheduleFlag.setLength(1);
		tableDefinition.addColumn(scheduleFlag);

		ColumnDefinition cacheFlag = new ColumnDefinition();
		cacheFlag.setName("cacheFlag");
		cacheFlag.setColumnName("CACHEFLAG_");
		cacheFlag.setJavaType("String");
		cacheFlag.setLength(1);
		tableDefinition.addColumn(cacheFlag);

		ColumnDefinition dataItemFlag = new ColumnDefinition();
		dataItemFlag.setName("dataItemFlag");
		dataItemFlag.setColumnName("DATAITEMFLAG_");
		dataItemFlag.setJavaType("String");
		dataItemFlag.setLength(2);
		tableDefinition.addColumn(dataItemFlag);

		ColumnDefinition fetchFlag = new ColumnDefinition();
		fetchFlag.setName("fetchFlag");
		fetchFlag.setColumnName("FETCHFLAG_");
		fetchFlag.setJavaType("String");
		fetchFlag.setLength(1);
		tableDefinition.addColumn(fetchFlag);

		ColumnDefinition deleteFetch = new ColumnDefinition();
		deleteFetch.setName("deleteFetch");
		deleteFetch.setColumnName("DELETEFETCH_");
		deleteFetch.setJavaType("String");
		deleteFetch.setLength(1);
		tableDefinition.addColumn(deleteFetch);

		ColumnDefinition exportFlag = new ColumnDefinition();
		exportFlag.setName("exportFlag");
		exportFlag.setColumnName("EXPORTFLAG_");
		exportFlag.setJavaType("String");
		exportFlag.setLength(1);
		tableDefinition.addColumn(exportFlag);

		ColumnDefinition exportTableName = new ColumnDefinition();
		exportTableName.setName("exportTableName");
		exportTableName.setColumnName("EXPORTTABLENAME_");
		exportTableName.setJavaType("String");
		exportTableName.setLength(50);
		tableDefinition.addColumn(exportTableName);

		ColumnDefinition exportTemplate = new ColumnDefinition();
		exportTemplate.setName("exportTemplate");
		exportTemplate.setColumnName("EXPORTTEMPLATE_");
		exportTemplate.setJavaType("String");
		exportTemplate.setLength(250);
		tableDefinition.addColumn(exportTemplate);

		ColumnDefinition targetTableName = new ColumnDefinition();
		targetTableName.setName("targetTableName");
		targetTableName.setColumnName("TARGETTABLENAME_");
		targetTableName.setJavaType("String");
		targetTableName.setLength(50);
		tableDefinition.addColumn(targetTableName);

		ColumnDefinition shareFlag = new ColumnDefinition();
		shareFlag.setName("shareFlag");
		shareFlag.setColumnName("SHAREFLAG_");
		shareFlag.setJavaType("String");
		shareFlag.setLength(1);
		tableDefinition.addColumn(shareFlag);

		ColumnDefinition publicFlag = new ColumnDefinition();
		publicFlag.setName("publicFlag");
		publicFlag.setColumnName("PUBLICFLAG_");
		publicFlag.setJavaType("String");
		publicFlag.setLength(1);
		tableDefinition.addColumn(publicFlag);

		ColumnDefinition saveFlag = new ColumnDefinition();
		saveFlag.setName("saveFlag");
		saveFlag.setColumnName("SAVEFLAG_");
		saveFlag.setJavaType("String");
		saveFlag.setLength(1);
		tableDefinition.addColumn(saveFlag);

		ColumnDefinition aggregationFlag = new ColumnDefinition();
		aggregationFlag.setName("aggregationFlag");
		aggregationFlag.setColumnName("AGGREGATIONFLAG_");
		aggregationFlag.setJavaType("String");
		aggregationFlag.setLength(1);
		tableDefinition.addColumn(aggregationFlag);

		ColumnDefinition locked = new ColumnDefinition();
		locked.setName("locked");
		locked.setColumnName("LOCKED_");
		locked.setJavaType("Integer");
		tableDefinition.addColumn(locked);

		ColumnDefinition ordinal = new ColumnDefinition();
		ordinal.setName("ordinal");
		ordinal.setColumnName("ORDINAL_");
		ordinal.setJavaType("Integer");
		tableDefinition.addColumn(ordinal);

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

		ColumnDefinition updateBy = new ColumnDefinition();
		updateBy.setName("updateBy");
		updateBy.setColumnName("UPDATEBY_");
		updateBy.setJavaType("String");
		updateBy.setLength(50);
		tableDefinition.addColumn(updateBy);

		ColumnDefinition updateTime = new ColumnDefinition();
		updateTime.setName("updateTime");
		updateTime.setColumnName("UPDATETIME_");
		updateTime.setJavaType("Date");
		tableDefinition.addColumn(updateTime);

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

	private SqlDefinitionDomainFactory() {

	}

}
