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
public class ColumnTransformDomainFactory {

	public static final String TABLENAME = "SYS_COLUMN_TRANSFORM";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("transformId", "TRANSFORMID_");
		columnMap.put("name", "NAME_");
		columnMap.put("title", "TITLE_");
		columnMap.put("tableName", "TABLENAME_");
		columnMap.put("columnName", "COLUMNNAME_");
		columnMap.put("targetColumnName", "TARGETCOLUMNNAME_");
		columnMap.put("targetColumnPrecision", "TARGETCOLUMNPRECISION_");
		columnMap.put("targetType", "TARGETTYPE_");
		columnMap.put("sqlCriteria", "SQLCRITERIA_");
		columnMap.put("condition", "CONDITION_");
		columnMap.put("expression", "EXPRESSION_");
		columnMap.put("transformIfTargetColumnNotEmpty", "TRANSFORM_IF_FLAG_");
		columnMap.put("type", "TYPE_");
		columnMap.put("syncStatus", "SYNCSTATUS_");
		columnMap.put("syncTime", "SYNCTIME_");
		columnMap.put("sort", "SORT_");
		columnMap.put("locked", "LOCKED_");
		columnMap.put("createBy", "CREATEBY_");
		columnMap.put("createTime", "CREATETIME_");
		columnMap.put("updateBy", "UPDATEBY_");
		columnMap.put("updateTime", "UPDATETIME_");

		javaTypeMap.put("id", "Long");
		javaTypeMap.put("transformId", "String");
		javaTypeMap.put("name", "String");
		javaTypeMap.put("title", "String");
		javaTypeMap.put("tableName", "String");
		javaTypeMap.put("columnName", "String");
		javaTypeMap.put("targetColumnName", "String");
		javaTypeMap.put("targetColumnPrecision", "Integer");
		javaTypeMap.put("targetType", "String");
		javaTypeMap.put("sqlCriteria", "String");
		javaTypeMap.put("condition", "String");
		javaTypeMap.put("expression", "String");
		javaTypeMap.put("transformIfTargetColumnNotEmpty", "String");
		javaTypeMap.put("type", "String");
		javaTypeMap.put("syncStatus", "Integer");
		javaTypeMap.put("syncTime", "Date");
		javaTypeMap.put("sort", "Integer");
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
		tableDefinition.setName("ColumnTransform");

		ColumnDefinition idColumn = new ColumnDefinition();
		idColumn.setName("id");
		idColumn.setColumnName("ID_");
		idColumn.setJavaType("Long");
		tableDefinition.setIdColumn(idColumn);

		ColumnDefinition transformId = new ColumnDefinition();
		transformId.setName("transformId");
		transformId.setColumnName("TRANSFORMID_");
		transformId.setJavaType("String");
		transformId.setLength(50);
		tableDefinition.addColumn(transformId);

		ColumnDefinition name = new ColumnDefinition();
		name.setName("name");
		name.setColumnName("NAME_");
		name.setJavaType("String");
		name.setLength(50);
		tableDefinition.addColumn(name);

		ColumnDefinition title = new ColumnDefinition();
		title.setName("title");
		title.setColumnName("TITLE_");
		title.setJavaType("String");
		title.setLength(200);
		tableDefinition.addColumn(title);

		ColumnDefinition tname = new ColumnDefinition();
		tname.setName("tableName");
		tname.setColumnName("TABLENAME_");
		tname.setJavaType("String");
		tname.setLength(50);
		tableDefinition.addColumn(tname);

		ColumnDefinition columnName = new ColumnDefinition();
		columnName.setName("columnName");
		columnName.setColumnName("COLUMNNAME_");
		columnName.setJavaType("String");
		columnName.setLength(50);
		tableDefinition.addColumn(columnName);

		ColumnDefinition targetColumnName = new ColumnDefinition();
		targetColumnName.setName("targetColumnName");
		targetColumnName.setColumnName("TARGETCOLUMNNAME_");
		targetColumnName.setJavaType("String");
		targetColumnName.setLength(50);
		tableDefinition.addColumn(targetColumnName);

		ColumnDefinition targetColumnPrecision = new ColumnDefinition();
		targetColumnPrecision.setName("targetColumnPrecision");
		targetColumnPrecision.setColumnName("TARGETCOLUMNPRECISION_");
		targetColumnPrecision.setJavaType("Integer");
		tableDefinition.addColumn(targetColumnPrecision);

		ColumnDefinition targetType = new ColumnDefinition();
		targetType.setName("targetType");
		targetType.setColumnName("TARGETTYPE_");
		targetType.setJavaType("String");
		targetType.setLength(50);
		tableDefinition.addColumn(targetType);

		ColumnDefinition sqlCriteria = new ColumnDefinition();
		sqlCriteria.setName("sqlCriteria");
		sqlCriteria.setColumnName("SQLCRITERIA_");
		sqlCriteria.setJavaType("String");
		sqlCriteria.setLength(4000);
		tableDefinition.addColumn(sqlCriteria);

		ColumnDefinition condition = new ColumnDefinition();
		condition.setName("condition");
		condition.setColumnName("CONDITION_");
		condition.setJavaType("String");
		condition.setLength(500);
		tableDefinition.addColumn(condition);

		ColumnDefinition expression = new ColumnDefinition();
		expression.setName("expression");
		expression.setColumnName("EXPRESSION_");
		expression.setJavaType("String");
		expression.setLength(500);
		tableDefinition.addColumn(expression);

		ColumnDefinition transformIfTargetColumnNotEmpty = new ColumnDefinition();
		transformIfTargetColumnNotEmpty.setName("transformIfTargetColumnNotEmpty");
		transformIfTargetColumnNotEmpty.setColumnName("TRANSFORM_IF_FLAG_");
		transformIfTargetColumnNotEmpty.setJavaType("String");
		transformIfTargetColumnNotEmpty.setLength(1);
		tableDefinition.addColumn(transformIfTargetColumnNotEmpty);

		ColumnDefinition type = new ColumnDefinition();
		type.setName("type");
		type.setColumnName("TYPE_");
		type.setJavaType("String");
		type.setLength(50);
		tableDefinition.addColumn(type);

		ColumnDefinition syncStatus = new ColumnDefinition();
		syncStatus.setName("syncStatus");
		syncStatus.setColumnName("SYNCSTATUS_");
		syncStatus.setJavaType("Integer");
		tableDefinition.addColumn(syncStatus);

		ColumnDefinition syncTime = new ColumnDefinition();
		syncTime.setName("syncTime");
		syncTime.setColumnName("SYNCTIME_");
		syncTime.setJavaType("Date");
		tableDefinition.addColumn(syncTime);

		ColumnDefinition sort = new ColumnDefinition();
		sort.setName("sort");
		sort.setColumnName("SORT_");
		sort.setJavaType("Integer");
		tableDefinition.addColumn(sort);

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

	private ColumnTransformDomainFactory() {

	}

}
