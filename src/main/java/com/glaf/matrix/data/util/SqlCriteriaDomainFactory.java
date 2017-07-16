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
public class SqlCriteriaDomainFactory {

	public static final String TABLENAME = "SYS_SQL_CRITERIA";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("parentId", "PARENTID_");
		columnMap.put("tenantId", "TENANTID_");
		columnMap.put("name", "NAME_");
		columnMap.put("moduleId", "MODULEID_");
		columnMap.put("businessKey", "BUSINESSKEY_");
		columnMap.put("columnName", "COLUMNNAME_");
		columnMap.put("columnType", "COLUMNTYPE_");
		columnMap.put("tableName", "TABLENAME_");
		columnMap.put("tableAlias", "TABLEALIAS_");
		columnMap.put("paramName", "PARAMNAME_");
		columnMap.put("paramTitle", "PARAMTITLE_");
		columnMap.put("collectionFlag", "COLLECTIONFLAG_");
		columnMap.put("condition", "CONDITION_");
		columnMap.put("operator", "OPERATOR_");
		columnMap.put("separator", "SEPARATOR_");
		columnMap.put("requiredFlag", "REQUIREDFLAG_");
		columnMap.put("sql", "SQL_");
		columnMap.put("treeId", "TREEID_");
		columnMap.put("level", "LEVEL_");
		columnMap.put("sortNo", "SORTNO_");
		columnMap.put("locked", "LOCKED_");
		columnMap.put("createBy", "CREATEBY_");
		columnMap.put("createTime", "CREATETIME_");
		columnMap.put("updateBy", "UPDATEBY_");
		columnMap.put("updateTime", "UPDATETIME_");

		javaTypeMap.put("id", "Long");
		javaTypeMap.put("parentId", "Long");
		javaTypeMap.put("tenantId", "String");
		javaTypeMap.put("name", "String");
		javaTypeMap.put("moduleId", "String");
		javaTypeMap.put("businessKey", "String");
		javaTypeMap.put("columnName", "String");
		javaTypeMap.put("columnType", "String");
		javaTypeMap.put("tableName", "String");
		javaTypeMap.put("tableAlias", "String");
		javaTypeMap.put("paramName", "String");
		javaTypeMap.put("paramTitle", "String");
		javaTypeMap.put("collectionFlag", "String");
		javaTypeMap.put("condition", "String");
		javaTypeMap.put("operator", "String");
		javaTypeMap.put("separator", "String");
		javaTypeMap.put("requiredFlag", "String");
		javaTypeMap.put("sql", "String");
		javaTypeMap.put("treeId", "String");
		javaTypeMap.put("level", "Integer");
		javaTypeMap.put("sortNo", "Integer");
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
		tableDefinition.setName("SqlCriteria");

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

		ColumnDefinition moduleId = new ColumnDefinition();
		moduleId.setName("moduleId");
		moduleId.setColumnName("MODULEID_");
		moduleId.setJavaType("String");
		moduleId.setLength(50);
		tableDefinition.addColumn(moduleId);

		ColumnDefinition businessKey = new ColumnDefinition();
		businessKey.setName("businessKey");
		businessKey.setColumnName("BUSINESSKEY_");
		businessKey.setJavaType("String");
		businessKey.setLength(200);
		tableDefinition.addColumn(businessKey);

		ColumnDefinition columnName = new ColumnDefinition();
		columnName.setName("columnName");
		columnName.setColumnName("COLUMNNAME_");
		columnName.setJavaType("String");
		columnName.setLength(50);
		tableDefinition.addColumn(columnName);

		ColumnDefinition columnType = new ColumnDefinition();
		columnType.setName("columnType");
		columnType.setColumnName("COLUMNTYPE_");
		columnType.setJavaType("String");
		columnType.setLength(50);
		tableDefinition.addColumn(columnType);

		ColumnDefinition tableName2 = new ColumnDefinition();
		tableName2.setName("tableName");
		tableName2.setColumnName("TABLENAME_");
		tableName2.setJavaType("String");
		tableName2.setLength(50);
		tableDefinition.addColumn(tableName2);

		ColumnDefinition tableAlias = new ColumnDefinition();
		tableAlias.setName("tableAlias");
		tableAlias.setColumnName("TABLEALIAS_");
		tableAlias.setJavaType("String");
		tableAlias.setLength(50);
		tableDefinition.addColumn(tableAlias);

		ColumnDefinition paramName = new ColumnDefinition();
		paramName.setName("paramName");
		paramName.setColumnName("PARAMNAME_");
		paramName.setJavaType("String");
		paramName.setLength(50);
		tableDefinition.addColumn(paramName);

		ColumnDefinition paramTitle = new ColumnDefinition();
		paramTitle.setName("paramTitle");
		paramTitle.setColumnName("PARAMTITLE_");
		paramTitle.setJavaType("String");
		paramTitle.setLength(50);
		tableDefinition.addColumn(paramTitle);

		ColumnDefinition collectionFlag = new ColumnDefinition();
		collectionFlag.setName("collectionFlag");
		collectionFlag.setColumnName("COLLECTIONFLAG_");
		collectionFlag.setJavaType("String");
		collectionFlag.setLength(1);
		tableDefinition.addColumn(collectionFlag);

		ColumnDefinition condition = new ColumnDefinition();
		condition.setName("condition");
		condition.setColumnName("CONDITION_");
		condition.setJavaType("String");
		condition.setLength(50);
		tableDefinition.addColumn(condition);

		ColumnDefinition operator = new ColumnDefinition();
		operator.setName("operator");
		operator.setColumnName("OPERATOR_");
		operator.setJavaType("String");
		operator.setLength(50);
		tableDefinition.addColumn(operator);

		ColumnDefinition separator = new ColumnDefinition();
		separator.setName("separator");
		separator.setColumnName("SEPARATOR_");
		separator.setJavaType("String");
		separator.setLength(50);
		tableDefinition.addColumn(separator);

		ColumnDefinition requiredFlag = new ColumnDefinition();
		requiredFlag.setName("requiredFlag");
		requiredFlag.setColumnName("REQUIREDFLAG_");
		requiredFlag.setJavaType("String");
		requiredFlag.setLength(1);
		tableDefinition.addColumn(requiredFlag);

		ColumnDefinition sql = new ColumnDefinition();
		sql.setName("sql");
		sql.setColumnName("SQL_");
		sql.setJavaType("String");
		sql.setLength(4000);
		tableDefinition.addColumn(sql);

		ColumnDefinition treeId = new ColumnDefinition();
		treeId.setName("treeId");
		treeId.setColumnName("TREEID_");
		treeId.setJavaType("String");
		treeId.setLength(500);
		tableDefinition.addColumn(treeId);

		ColumnDefinition level = new ColumnDefinition();
		level.setName("level");
		level.setColumnName("LEVEL_");
		level.setJavaType("Integer");
		tableDefinition.addColumn(level);

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

	private SqlCriteriaDomainFactory() {

	}

}
