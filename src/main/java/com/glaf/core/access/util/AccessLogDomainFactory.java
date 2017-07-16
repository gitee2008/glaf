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

package com.glaf.core.access.util;

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
public class AccessLogDomainFactory {

	public static final String TABLENAME = "SYS_ACCESS_LOG";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("ip", "IP_");
		columnMap.put("method", "METHOD_");
		columnMap.put("content", "CONTENT_");
		columnMap.put("uri", "URI_");
		columnMap.put("uriRefId", "URIREFID_");
		columnMap.put("status", "STATUS_");
		columnMap.put("day", "DAY_");
		columnMap.put("hour", "HOUR_");
		columnMap.put("minute", "MINUTE_");
		columnMap.put("timeMillis", "TIMEMILLIS_");
		columnMap.put("type", "TYPE_");
		columnMap.put("userId", "USERID_");
		columnMap.put("accessTime", "ACCESSTIME_");

		javaTypeMap.put("id", "Long");
		javaTypeMap.put("ip", "String");
		javaTypeMap.put("method", "String");
		javaTypeMap.put("content", "String");
		javaTypeMap.put("uri", "String");
		javaTypeMap.put("uriRefId", "Integer");
		javaTypeMap.put("status", "Integer");
		javaTypeMap.put("day", "Integer");
		javaTypeMap.put("hour", "Integer");
		javaTypeMap.put("minute", "Integer");
		javaTypeMap.put("timeMillis", "Integer");
		javaTypeMap.put("type", "String");
		javaTypeMap.put("userId", "String");
		javaTypeMap.put("accessTime", "Date");
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
		tableDefinition.setName("AccessLog");

		ColumnDefinition idColumn = new ColumnDefinition();
		idColumn.setName("id");
		idColumn.setColumnName("ID_");
		idColumn.setJavaType("Long");
		tableDefinition.setIdColumn(idColumn);

		ColumnDefinition ip = new ColumnDefinition();
		ip.setName("ip");
		ip.setColumnName("IP_");
		ip.setJavaType("String");
		ip.setLength(250);
		tableDefinition.addColumn(ip);

		ColumnDefinition method = new ColumnDefinition();
		method.setName("method");
		method.setColumnName("METHOD_");
		method.setJavaType("String");
		method.setLength(50);
		tableDefinition.addColumn(method);

		ColumnDefinition content = new ColumnDefinition();
		content.setName("content");
		content.setColumnName("CONTENT_");
		content.setJavaType("Clob");
		content.setLength(4000);
		tableDefinition.addColumn(content);

		ColumnDefinition uri = new ColumnDefinition();
		uri.setName("uri");
		uri.setColumnName("URI_");
		uri.setJavaType("String");
		uri.setLength(500);
		tableDefinition.addColumn(uri);

		ColumnDefinition uriRefId = new ColumnDefinition();
		uriRefId.setName("uriRefId");
		uriRefId.setColumnName("URIREFID_");
		uriRefId.setJavaType("Integer");
		tableDefinition.addColumn(uriRefId);

		ColumnDefinition status = new ColumnDefinition();
		status.setName("status");
		status.setColumnName("STATUS_");
		status.setJavaType("Integer");
		tableDefinition.addColumn(status);

		ColumnDefinition day = new ColumnDefinition();
		day.setName("day");
		day.setColumnName("DAY_");
		day.setJavaType("Integer");
		tableDefinition.addColumn(day);

		ColumnDefinition hour = new ColumnDefinition();
		hour.setName("hour");
		hour.setColumnName("HOUR_");
		hour.setJavaType("Integer");
		tableDefinition.addColumn(hour);

		ColumnDefinition minute = new ColumnDefinition();
		minute.setName("minute");
		minute.setColumnName("MINUTE_");
		minute.setJavaType("Integer");
		tableDefinition.addColumn(minute);

		ColumnDefinition timeMillis = new ColumnDefinition();
		timeMillis.setName("timeMillis");
		timeMillis.setColumnName("TIMEMILLIS_");
		timeMillis.setJavaType("Integer");
		tableDefinition.addColumn(timeMillis);

		ColumnDefinition type = new ColumnDefinition();
		type.setName("type");
		type.setColumnName("TYPE_");
		type.setJavaType("String");
		type.setLength(50);
		tableDefinition.addColumn(type);

		ColumnDefinition userId = new ColumnDefinition();
		userId.setName("userId");
		userId.setColumnName("USERID_");
		userId.setJavaType("String");
		userId.setLength(50);
		tableDefinition.addColumn(userId);

		ColumnDefinition accessTime = new ColumnDefinition();
		accessTime.setName("accessTime");
		accessTime.setColumnName("ACCESSTIME_");
		accessTime.setJavaType("Date");
		tableDefinition.addColumn(accessTime);

		return tableDefinition;
	}

	private AccessLogDomainFactory() {

	}

}
