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
public class ExecutionLogDomainFactory {

	public static final String TABLENAME = "SYS_EXECUTION_LOG";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("type", "TYPE_");
		columnMap.put("businessKey", "BUSINESSKEY_");
		columnMap.put("jobNo", "JOBNO_");
		columnMap.put("title", "TITLE_");
		columnMap.put("content", "CONTENT_");
		columnMap.put("startTime", "STARTTIME_");
		columnMap.put("endTime", "ENDTIME_");
		columnMap.put("runDay", "RUNDAY_");
		columnMap.put("runHour", "RUNHOUR_");
		columnMap.put("runTime", "RUNTIME_");
		columnMap.put("status", "STATUS_");
		columnMap.put("exitCode", "EXITCODE_");
		columnMap.put("exitMessage", "EXITMESSAGE_");
		columnMap.put("createBy", "CREATEBY_");
		columnMap.put("createTime", "CREATETIME_");

		javaTypeMap.put("id", "Long");
		javaTypeMap.put("type", "String");
		javaTypeMap.put("businessKey", "String");
		javaTypeMap.put("jobNo", "String");
		javaTypeMap.put("title", "String");
		javaTypeMap.put("content", "String");
		javaTypeMap.put("startTime", "Date");
		javaTypeMap.put("endTime", "Date");
		javaTypeMap.put("runDay", "Integer");
		javaTypeMap.put("runHour", "Integer");
		javaTypeMap.put("runTime", "Long");
		javaTypeMap.put("status", "Integer");
		javaTypeMap.put("exitCode", "String");
		javaTypeMap.put("exitMessage", "String");
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
		tableDefinition.setName("ExecutionLog");

		ColumnDefinition idColumn = new ColumnDefinition();
		idColumn.setName("id");
		idColumn.setColumnName("ID_");
		idColumn.setJavaType("Long");
		tableDefinition.setIdColumn(idColumn);

		ColumnDefinition type = new ColumnDefinition();
		type.setName("type");
		type.setColumnName("TYPE_");
		type.setJavaType("String");
		type.setLength(50);
		tableDefinition.addColumn(type);

		ColumnDefinition businessKey = new ColumnDefinition();
		businessKey.setName("businessKey");
		businessKey.setColumnName("BUSINESSKEY_");
		businessKey.setJavaType("String");
		businessKey.setLength(200);
		tableDefinition.addColumn(businessKey);

		ColumnDefinition jobNo = new ColumnDefinition();
		jobNo.setName("jobNo");
		jobNo.setColumnName("JOBNO_");
		jobNo.setJavaType("String");
		jobNo.setLength(250);
		tableDefinition.addColumn(jobNo);

		ColumnDefinition title = new ColumnDefinition();
		title.setName("title");
		title.setColumnName("TITLE_");
		title.setJavaType("String");
		title.setLength(200);
		tableDefinition.addColumn(title);

		ColumnDefinition content = new ColumnDefinition();
		content.setName("content");
		content.setColumnName("CONTENT_");
		content.setJavaType("String");
		content.setLength(500);
		tableDefinition.addColumn(content);

		ColumnDefinition startTime = new ColumnDefinition();
		startTime.setName("startTime");
		startTime.setColumnName("STARTTIME_");
		startTime.setJavaType("Date");
		tableDefinition.addColumn(startTime);

		ColumnDefinition endTime = new ColumnDefinition();
		endTime.setName("endTime");
		endTime.setColumnName("ENDTIME_");
		endTime.setJavaType("Date");
		tableDefinition.addColumn(endTime);

		ColumnDefinition runDay = new ColumnDefinition();
		runDay.setName("runDay");
		runDay.setColumnName("RUNDAY_");
		runDay.setJavaType("Integer");
		tableDefinition.addColumn(runDay);

		ColumnDefinition runHour = new ColumnDefinition();
		runHour.setName("runHour");
		runHour.setColumnName("RUNHOUR_");
		runHour.setJavaType("Integer");
		tableDefinition.addColumn(runHour);

		ColumnDefinition runTime = new ColumnDefinition();
		runTime.setName("runTime");
		runTime.setColumnName("RUNTIME_");
		runTime.setJavaType("Long");
		tableDefinition.addColumn(runTime);

		ColumnDefinition status = new ColumnDefinition();
		status.setName("status");
		status.setColumnName("STATUS_");
		status.setJavaType("Integer");
		tableDefinition.addColumn(status);

		ColumnDefinition exitCode = new ColumnDefinition();
		exitCode.setName("exitCode");
		exitCode.setColumnName("EXITCODE_");
		exitCode.setJavaType("String");
		exitCode.setLength(200);
		tableDefinition.addColumn(exitCode);

		ColumnDefinition exitMessage = new ColumnDefinition();
		exitMessage.setName("exitMessage");
		exitMessage.setColumnName("EXITMESSAGE_");
		exitMessage.setJavaType("String");
		exitMessage.setLength(500);
		tableDefinition.addColumn(exitMessage);

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

	private ExecutionLogDomainFactory() {

	}

}
