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
public class TreeTableAggregateRuleDomainFactory {

	public static final String TABLENAME = "SYS_TREETABLE_AGGREGATE_RULE";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("aggregateId", "AGGREGATEID_");
		columnMap.put("aggregateType", "AGGREGATETYPE_");
		columnMap.put("name", "NAME_");
		columnMap.put("title", "TITLE_");
		columnMap.put("sourceColumnName", "SOURCECOLUMNNAME_");
		columnMap.put("sourceColumnTitle", "SOURCECOLUMNTITLE_");
		columnMap.put("targetColumnName", "TARGETCOLUMNNAME_");
		columnMap.put("targetColumnTitle", "TARGETCOLUMNTITLE_");
		columnMap.put("targetColumnType", "TARGETCOLUMNTYPE_");
		columnMap.put("locked", "LOCKED_");

		javaTypeMap.put("id", "Long");
		javaTypeMap.put("aggregateId", "Long");
		javaTypeMap.put("aggregateType", "String");
		javaTypeMap.put("name", "String");
		javaTypeMap.put("title", "String");
		javaTypeMap.put("sourceColumnName", "String");
		javaTypeMap.put("sourceColumnTitle", "String");
		javaTypeMap.put("targetColumnName", "String");
		javaTypeMap.put("targetColumnTitle", "String");
		javaTypeMap.put("targetColumnType", "String");
		javaTypeMap.put("locked", "Integer");
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
		tableDefinition.setName("TreeTableAggregateRule");

		ColumnDefinition idColumn = new ColumnDefinition();
		idColumn.setName("id");
		idColumn.setColumnName("ID_");
		idColumn.setJavaType("Long");
		tableDefinition.setIdColumn(idColumn);

		ColumnDefinition aggregateId = new ColumnDefinition();
		aggregateId.setName("aggregateId");
		aggregateId.setColumnName("AGGREGATEID_");
		aggregateId.setJavaType("Long");
		tableDefinition.addColumn(aggregateId);

		ColumnDefinition aggregateType = new ColumnDefinition();
		aggregateType.setName("aggregateType");
		aggregateType.setColumnName("AGGREGATETYPE_");
		aggregateType.setJavaType("String");
		aggregateType.setLength(50);
		tableDefinition.addColumn(aggregateType);

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

		ColumnDefinition sourceColumnName = new ColumnDefinition();
		sourceColumnName.setName("sourceColumnName");
		sourceColumnName.setColumnName("SOURCECOLUMNNAME_");
		sourceColumnName.setJavaType("String");
		sourceColumnName.setLength(50);
		tableDefinition.addColumn(sourceColumnName);

		ColumnDefinition sourceColumnTitle = new ColumnDefinition();
		sourceColumnTitle.setName("sourceColumnTitle");
		sourceColumnTitle.setColumnName("SOURCECOLUMNTITLE_");
		sourceColumnTitle.setJavaType("String");
		sourceColumnTitle.setLength(50);
		tableDefinition.addColumn(sourceColumnTitle);

		ColumnDefinition targetColumnName = new ColumnDefinition();
		targetColumnName.setName("targetColumnName");
		targetColumnName.setColumnName("TARGETCOLUMNNAME_");
		targetColumnName.setJavaType("String");
		targetColumnName.setLength(50);
		tableDefinition.addColumn(targetColumnName);

		ColumnDefinition targetColumnTitle = new ColumnDefinition();
		targetColumnTitle.setName("targetColumnTitle");
		targetColumnTitle.setColumnName("TARGETCOLUMNTITLE_");
		targetColumnTitle.setJavaType("String");
		targetColumnTitle.setLength(50);
		tableDefinition.addColumn(targetColumnTitle);

		ColumnDefinition targetColumnType = new ColumnDefinition();
		targetColumnType.setName("targetColumnType");
		targetColumnType.setColumnName("TARGETCOLUMNTYPE_");
		targetColumnType.setJavaType("String");
		targetColumnType.setLength(50);
		tableDefinition.addColumn(targetColumnType);

		ColumnDefinition locked = new ColumnDefinition();
		locked.setName("locked");
		locked.setColumnName("LOCKED_");
		locked.setJavaType("Integer");
		tableDefinition.addColumn(locked);

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

	private TreeTableAggregateRuleDomainFactory() {

	}

}
