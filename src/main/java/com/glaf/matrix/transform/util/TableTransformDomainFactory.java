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

package com.glaf.matrix.transform.util;

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
public class TableTransformDomainFactory {

	public static final String TABLENAME = "SYS_TABLE_TRANSFORM";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("transformId", "TRANSFORMID_");
		columnMap.put("tableName", "TABLENAME_");
		columnMap.put("title", "TITLE_");
		columnMap.put("databaseIds", "DATABASEIDS_");
		columnMap.put("primaryKey", "PRIMARYKEY_");
		columnMap.put("targetTableName", "TARGETTABLENAME_");
		columnMap.put("transformColumns", "TRANSFORMCOLUMNS_");
		columnMap.put("sqlCriteria", "SQLCRITERIA_");
		columnMap.put("sort", "SORT_");
		columnMap.put("transformFlag", "TRANSFORMFLAG_");
		columnMap.put("transformIfTargetColumnNotEmpty", "TRANSFORM_IF_FLAG_");
		columnMap.put("transformStatus", "TRANSFORMSTATUS_");
		columnMap.put("transformTime", "TRANSFORMTIME_");
		columnMap.put("currentUserFlag", "CURRENTUSERFLAG_");
		columnMap.put("deleteFlag", "DELETEFLAG_");
		columnMap.put("locked", "LOCKED_");
		columnMap.put("createBy", "CREATEBY_");
		columnMap.put("createTime", "CREATETIME_");
		columnMap.put("updateBy", "UPDATEBY_");
		columnMap.put("updateTime", "UPDATETIME_");

		javaTypeMap.put("transformId", "String");
		javaTypeMap.put("tableName", "String");
		javaTypeMap.put("title", "String");
		javaTypeMap.put("databaseIds", "String");
		javaTypeMap.put("primaryKey", "String");
		javaTypeMap.put("targetTableName", "String");
		javaTypeMap.put("transformColumns", "String");
		javaTypeMap.put("sqlCriteria", "String");
		javaTypeMap.put("sort", "Integer");
		javaTypeMap.put("transformIfTargetColumnNotEmpty", "String");
		javaTypeMap.put("transformFlag", "String");
		javaTypeMap.put("transformStatus", "Integer");
		javaTypeMap.put("transformTime", "Date");
		javaTypeMap.put("currentUserFlag", "String");
		javaTypeMap.put("deleteFlag", "Integer");
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
		tableDefinition.setName("TableTransform");

		ColumnDefinition transformId = new ColumnDefinition();
		transformId.setName("transformId");
		transformId.setColumnName("TRANSFORMID_");
		transformId.setJavaType("String");
		transformId.setLength(50);
		tableDefinition.setIdColumn(transformId);

		ColumnDefinition idColumn = new ColumnDefinition();
		idColumn.setName("tableName");
		idColumn.setColumnName("TABLENAME_");
		idColumn.setJavaType("String");
		idColumn.setLength(100);
		tableDefinition.addColumn(idColumn);

		ColumnDefinition title = new ColumnDefinition();
		title.setName("title");
		title.setColumnName("TITLE_");
		title.setJavaType("String");
		title.setLength(200);
		tableDefinition.addColumn(title);

		ColumnDefinition databaseIds = new ColumnDefinition();
		databaseIds.setName("databaseIds");
		databaseIds.setColumnName("DATABASEIDS_");
		databaseIds.setJavaType("String");
		databaseIds.setLength(2000);
		tableDefinition.addColumn(databaseIds);

		ColumnDefinition primaryKey = new ColumnDefinition();
		primaryKey.setName("primaryKey");
		primaryKey.setColumnName("PRIMARYKEY_");
		primaryKey.setJavaType("String");
		primaryKey.setLength(50);
		tableDefinition.addColumn(primaryKey);

		ColumnDefinition targetTableName = new ColumnDefinition();
		targetTableName.setName("targetTableName");
		targetTableName.setColumnName("TARGETTABLENAME_");
		targetTableName.setJavaType("String");
		targetTableName.setLength(50);
		tableDefinition.addColumn(targetTableName);

		ColumnDefinition transformColumns = new ColumnDefinition();
		transformColumns.setName("transformColumns");
		transformColumns.setColumnName("TRANSFORMCOLUMNS_");
		transformColumns.setJavaType("String");
		transformColumns.setLength(2000);
		tableDefinition.addColumn(transformColumns);

		ColumnDefinition sqlCriteria = new ColumnDefinition();
		sqlCriteria.setName("sqlCriteria");
		sqlCriteria.setColumnName("SQLCRITERIA_");
		sqlCriteria.setJavaType("String");
		sqlCriteria.setLength(4000);
		tableDefinition.addColumn(sqlCriteria);

		ColumnDefinition sort = new ColumnDefinition();
		sort.setName("sort");
		sort.setColumnName("SORT_");
		sort.setJavaType("Integer");
		tableDefinition.addColumn(sort);

		ColumnDefinition transformIfTargetColumnNotEmpty = new ColumnDefinition();
		transformIfTargetColumnNotEmpty.setName("transformIfTargetColumnNotEmpty");
		transformIfTargetColumnNotEmpty.setColumnName("TRANSFORM_IF_FLAG_");
		transformIfTargetColumnNotEmpty.setJavaType("String");
		transformIfTargetColumnNotEmpty.setLength(1);
		tableDefinition.addColumn(transformIfTargetColumnNotEmpty);

		ColumnDefinition transformFlag = new ColumnDefinition();
		transformFlag.setName("transformFlag");
		transformFlag.setColumnName("TRANSFORMFLAG_");
		transformFlag.setJavaType("String");
		transformFlag.setLength(1);
		tableDefinition.addColumn(transformFlag);

		ColumnDefinition transformStatus = new ColumnDefinition();
		transformStatus.setName("transformStatus");
		transformStatus.setColumnName("TRANSFORMSTATUS_");
		transformStatus.setJavaType("Integer");
		tableDefinition.addColumn(transformStatus);

		ColumnDefinition transformTime = new ColumnDefinition();
		transformTime.setName("transformTime");
		transformTime.setColumnName("TRANSFORMTIME_");
		transformTime.setJavaType("Date");
		tableDefinition.addColumn(transformTime);

		ColumnDefinition currentUserFlag = new ColumnDefinition();
		currentUserFlag.setName("currentUserFlag");
		currentUserFlag.setColumnName("CURRENTUSERFLAG_");
		currentUserFlag.setJavaType("String");
		currentUserFlag.setLength(1);
		tableDefinition.addColumn(currentUserFlag);

		ColumnDefinition deleteFlag = new ColumnDefinition();
		deleteFlag.setName("deleteFlag");
		deleteFlag.setColumnName("DELETEFLAG_");
		deleteFlag.setJavaType("Integer");
		tableDefinition.addColumn(deleteFlag);

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

	private TableTransformDomainFactory() {

	}

}
