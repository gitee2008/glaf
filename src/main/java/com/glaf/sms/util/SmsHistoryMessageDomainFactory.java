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

package com.glaf.sms.util;

import java.util.List;
import java.util.Map;
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
public class SmsHistoryMessageDomainFactory {

	public static final String TABLENAME = "SMS_HISTORY_MESSAGE";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("clientId", "CLIENTID_");
		columnMap.put("serverId", "SERVERID_");
		columnMap.put("name", "NAME_");
		columnMap.put("mobile", "MOBILE_");
		columnMap.put("subject", "SUBJECT_");
		columnMap.put("message", "MESSAGE_");
		columnMap.put("sendTime", "SENDTIME_");
		columnMap.put("status", "STATUS_");
		columnMap.put("year", "YEAR_");
		columnMap.put("month", "MONTH_");
		columnMap.put("fullDay", "FULLDAY_");
		columnMap.put("createTime", "CREATETIME_");
		columnMap.put("result", "RESULT_");

		javaTypeMap.put("id", "String");
		javaTypeMap.put("clientId", "String");
		javaTypeMap.put("serverId", "String");
		javaTypeMap.put("name", "String");
		javaTypeMap.put("mobile", "String");
		javaTypeMap.put("subject", "String");
		javaTypeMap.put("message", "String");
		javaTypeMap.put("sendTime", "Date");
		javaTypeMap.put("status", "Integer");
		javaTypeMap.put("year", "Integer");
		javaTypeMap.put("month", "Integer");
		javaTypeMap.put("fullDay", "Integer");
		javaTypeMap.put("createTime", "Date");
		javaTypeMap.put("result", "String");
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
		tableDefinition.setName("SmsHistoryMessage");

		ColumnDefinition idColumn = new ColumnDefinition();
		idColumn.setName("id");
		idColumn.setColumnName("ID_");
		idColumn.setJavaType("String");
		idColumn.setLength(50);
		tableDefinition.setIdColumn(idColumn);

		ColumnDefinition clientId = new ColumnDefinition();
		clientId.setName("clientId");
		clientId.setColumnName("CLIENTID_");
		clientId.setJavaType("String");
		clientId.setLength(50);
		tableDefinition.addColumn(clientId);

		ColumnDefinition serverId = new ColumnDefinition();
		serverId.setName("serverId");
		serverId.setColumnName("SERVERID_");
		serverId.setJavaType("String");
		serverId.setLength(50);
		tableDefinition.addColumn(serverId);

		ColumnDefinition name = new ColumnDefinition();
		name.setName("name");
		name.setColumnName("NAME_");
		name.setJavaType("String");
		name.setLength(200);
		tableDefinition.addColumn(name);

		ColumnDefinition mobile = new ColumnDefinition();
		mobile.setName("mobile");
		mobile.setColumnName("MOBILE_");
		mobile.setJavaType("String");
		mobile.setLength(20);
		tableDefinition.addColumn(mobile);

		ColumnDefinition subject = new ColumnDefinition();
		subject.setName("subject");
		subject.setColumnName("SUBJECT_");
		subject.setJavaType("String");
		subject.setLength(200);
		tableDefinition.addColumn(subject);

		ColumnDefinition message = new ColumnDefinition();
		message.setName("message");
		message.setColumnName("MESSAGE_");
		message.setJavaType("String");
		message.setLength(4000);
		tableDefinition.addColumn(message);

		ColumnDefinition result = new ColumnDefinition();
		message.setName("result");
		message.setColumnName("RESULT_");
		message.setJavaType("String");
		message.setLength(500);
		tableDefinition.addColumn(result);

		ColumnDefinition sendTime = new ColumnDefinition();
		sendTime.setName("sendTime");
		sendTime.setColumnName("SENDTIME_");
		sendTime.setJavaType("Date");
		tableDefinition.addColumn(sendTime);

		ColumnDefinition status = new ColumnDefinition();
		status.setName("status");
		status.setColumnName("STATUS_");
		status.setJavaType("Integer");
		tableDefinition.addColumn(status);

		ColumnDefinition year = new ColumnDefinition();
		year.setName("year");
		year.setColumnName("YEAR_");
		year.setJavaType("Integer");
		tableDefinition.addColumn(year);

		ColumnDefinition month = new ColumnDefinition();
		month.setName("month");
		month.setColumnName("MONTH_");
		month.setJavaType("Integer");
		tableDefinition.addColumn(month);

		ColumnDefinition fullDay = new ColumnDefinition();
		fullDay.setName("fullDay");
		fullDay.setColumnName("FULLDAY_");
		fullDay.setJavaType("Integer");
		tableDefinition.addColumn(fullDay);

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

	private SmsHistoryMessageDomainFactory() {

	}

}
