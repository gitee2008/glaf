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
public class SyntheticFlowDomainFactory {

	public static final String TABLENAME = "SYS_SYNTHETIC_FLOW";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("currentStep", "CURRENTSTEP_");
		columnMap.put("currentType", "CURRENTTYPE_");
		columnMap.put("previousStep", "PREVIOUSSTEP_");
		columnMap.put("previousType", "PREVIOUSTYPE_");
		columnMap.put("nextStep", "NEXTSTEP_");
		columnMap.put("nextType", "NEXTTYPE_");
		columnMap.put("sort", "SORT_");
		columnMap.put("createBy", "CREATEBY_");
		columnMap.put("createTime", "CREATETIME_");

		javaTypeMap.put("id", "Long");
		javaTypeMap.put("currentStep", "String");
		javaTypeMap.put("currentType", "String");
		javaTypeMap.put("previousStep", "String");
		javaTypeMap.put("previousType", "String");
		javaTypeMap.put("nextStep", "String");
		javaTypeMap.put("nextType", "String");
		javaTypeMap.put("sort", "Integer");
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
		tableDefinition.setName("SyntheticFlow");

		ColumnDefinition idColumn = new ColumnDefinition();
		idColumn.setName("id");
		idColumn.setColumnName("ID_");
		idColumn.setJavaType("Long");
		tableDefinition.setIdColumn(idColumn);

		ColumnDefinition currentStep = new ColumnDefinition();
		currentStep.setName("currentStep");
		currentStep.setColumnName("CURRENTSTEP_");
		currentStep.setJavaType("String");
		currentStep.setLength(200);
		tableDefinition.addColumn(currentStep);

		ColumnDefinition currentType = new ColumnDefinition();
		currentType.setName("currentType");
		currentType.setColumnName("CURRENTTYPE_");
		currentType.setJavaType("String");
		currentType.setLength(50);
		tableDefinition.addColumn(currentType);

		ColumnDefinition previousStep = new ColumnDefinition();
		previousStep.setName("previousStep");
		previousStep.setColumnName("PREVIOUSSTEP_");
		previousStep.setJavaType("String");
		previousStep.setLength(200);
		tableDefinition.addColumn(previousStep);

		ColumnDefinition previousType = new ColumnDefinition();
		previousType.setName("previousType");
		previousType.setColumnName("PREVIOUSTYPE_");
		previousType.setJavaType("String");
		previousType.setLength(50);
		tableDefinition.addColumn(previousType);

		ColumnDefinition nextStep = new ColumnDefinition();
		nextStep.setName("nextStep");
		nextStep.setColumnName("NEXTSTEP_");
		nextStep.setJavaType("String");
		nextStep.setLength(200);
		tableDefinition.addColumn(nextStep);

		ColumnDefinition nextType = new ColumnDefinition();
		nextType.setName("nextType");
		nextType.setColumnName("NEXTTYPE_");
		nextType.setJavaType("String");
		nextType.setLength(50);
		tableDefinition.addColumn(nextType);

		ColumnDefinition sort = new ColumnDefinition();
		sort.setName("sort");
		sort.setColumnName("SORT_");
		sort.setJavaType("Integer");
		tableDefinition.addColumn(sort);

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

	private SyntheticFlowDomainFactory() {

	}

}
