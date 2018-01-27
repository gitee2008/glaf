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

package com.glaf.matrix.combination.util;

import java.util.Map;
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
public class CombinationItemDomainFactory {

	public static final String TABLENAME = "SYS_COMBINATION_ITEM";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("syncId", "SYNCID_");
		columnMap.put("deploymentId", "DEPLOYMENTID_");
		columnMap.put("title", "TITLE_");
		columnMap.put("sql", "SQL_");
		columnMap.put("recursionSql", "RECURSIONSQL_");
		columnMap.put("recursionColumns", "RECURSIONCOLUMNS_");
		columnMap.put("primaryKey", "PRIMARYKEY_");
		columnMap.put("expression", "EXPRESSION_");
		columnMap.put("createTableFlag", "CREATETABLEFLAG_");
		columnMap.put("deleteFetch", "DELETEFETCH_");
		columnMap.put("sortNo", "SORTNO_");
		columnMap.put("locked", "LOCKED_");
		columnMap.put("createBy", "CREATEBY_");
		columnMap.put("createTime", "CREATETIME_");

		javaTypeMap.put("id", "Long");
		javaTypeMap.put("syncId", "Long");
		javaTypeMap.put("deploymentId", "String");
		javaTypeMap.put("title", "String");
		javaTypeMap.put("sql", "String");
		javaTypeMap.put("recursionSql", "String");
		javaTypeMap.put("recursionColumns", "String");
		javaTypeMap.put("primaryKey", "String");
		javaTypeMap.put("expression", "String");
		javaTypeMap.put("createTableFlag", "String");
		javaTypeMap.put("deleteFetch", "String");
		javaTypeMap.put("sortNo", "Integer");
		javaTypeMap.put("locked", "Integer");
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
		tableDefinition.setName("CombinationItem");

		ColumnDefinition idColumn = new ColumnDefinition();
		idColumn.setName("id");
		idColumn.setColumnName("ID_");
		idColumn.setJavaType("Long");
		tableDefinition.setIdColumn(idColumn);

		ColumnDefinition syncId = new ColumnDefinition();
		syncId.setName("syncId");
		syncId.setColumnName("SYNCID_");
		syncId.setJavaType("Long");
		tableDefinition.addColumn(syncId);

		ColumnDefinition deploymentId = new ColumnDefinition();
		deploymentId.setName("deploymentId");
		deploymentId.setColumnName("DEPLOYMENTID_");
		deploymentId.setJavaType("String");
		deploymentId.setLength(50);
		tableDefinition.addColumn(deploymentId);

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
		sql.setLength(4000);
		tableDefinition.addColumn(sql);

		ColumnDefinition recursionSql = new ColumnDefinition();
		recursionSql.setName("recursionSql");
		recursionSql.setColumnName("RECURSIONSQL_");
		recursionSql.setJavaType("String");
		recursionSql.setLength(4000);
		tableDefinition.addColumn(recursionSql);

		ColumnDefinition recursionColumns = new ColumnDefinition();
		recursionColumns.setName("recursionColumns");
		recursionColumns.setColumnName("RECURSIONCOLUMNS_");
		recursionColumns.setJavaType("String");
		recursionColumns.setLength(4000);
		tableDefinition.addColumn(recursionColumns);

		ColumnDefinition primaryKey = new ColumnDefinition();
		primaryKey.setName("primaryKey");
		primaryKey.setColumnName("PRIMARYKEY_");
		primaryKey.setJavaType("String");
		primaryKey.setLength(50);
		tableDefinition.addColumn(primaryKey);

		ColumnDefinition expression = new ColumnDefinition();
		expression.setName("expression");
		expression.setColumnName("EXPRESSION_");
		expression.setJavaType("String");
		expression.setLength(500);
		tableDefinition.addColumn(expression);

		ColumnDefinition createTableFlag = new ColumnDefinition();
		createTableFlag.setName("createTableFlag");
		createTableFlag.setColumnName("CREATETABLEFLAG_");
		createTableFlag.setJavaType("String");
		createTableFlag.setLength(1);
		tableDefinition.addColumn(createTableFlag);

		ColumnDefinition deleteFetch = new ColumnDefinition();
		deleteFetch.setName("deleteFetch");
		deleteFetch.setColumnName("DELETEFETCH_");
		deleteFetch.setJavaType("String");
		deleteFetch.setLength(1);
		tableDefinition.addColumn(deleteFetch);

		ColumnDefinition sortNo = new ColumnDefinition();
		sortNo.setName("sortNo");
		sortNo.setColumnName("SORTNO_");
		sortNo.setJavaType("Integer");
		tableDefinition.addColumn(sortNo);
		
		ColumnDefinition locked = new ColumnDefinition();
		locked.setName("locked");
		locked.setColumnName("LOCKED_");
		locked.setJavaType("Integer");
		tableDefinition.addColumn(locked);

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

	private CombinationItemDomainFactory() {

	}

}
