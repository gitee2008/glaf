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
public class SmsServerDomainFactory {

	public static final String TABLENAME = "SMS_SERVER";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("subject", "SUBJECT_");
		columnMap.put("serverIP", "SERVERIP_");
		columnMap.put("port", "PORT_");
		columnMap.put("path", "PATH_");
		columnMap.put("requestBody", "REQUESTBODY_");
		columnMap.put("responseBody", "RESPONSEBODY_");
		columnMap.put("responseResult", "RESPONSERESULT_");
		columnMap.put("frequence", "FREQUENCE_");
		columnMap.put("retryTimes", "RETRYTIMES_");
		columnMap.put("key", "KEY_");
		columnMap.put("accessKeyId", "ACCESSKEYID_");
		columnMap.put("accessKeySecret", "ACCESSKEYSECRET_");
		columnMap.put("signName", "SIGNNAME_");
		columnMap.put("templateCode", "TEMPLATECODE_");
		columnMap.put("provider", "PROVIDER_");
		columnMap.put("type", "TYPE_");
		columnMap.put("locked", "LOCKED_");
		columnMap.put("createBy", "CREATEBY_");
		columnMap.put("createTime", "CREATETIME_");

		javaTypeMap.put("id", "String");
		javaTypeMap.put("subject", "String");
		javaTypeMap.put("serverIP", "String");
		javaTypeMap.put("port", "Integer");
		javaTypeMap.put("path", "String");
		javaTypeMap.put("requestBody", "String");
		javaTypeMap.put("responseBody", "String");
		javaTypeMap.put("responseResult", "String");
		javaTypeMap.put("frequence", "Integer");
		javaTypeMap.put("retryTimes", "Integer");
		javaTypeMap.put("key", "String");
		javaTypeMap.put("accessKeyId", "String");
		javaTypeMap.put("accessKeySecret", "String");
		javaTypeMap.put("signName", "String");
		javaTypeMap.put("templateCode", "String");
		javaTypeMap.put("provider", "String");
		javaTypeMap.put("type", "String");
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
		tableDefinition.setName("SmsServer");

		ColumnDefinition idColumn = new ColumnDefinition();
		idColumn.setName("id");
		idColumn.setColumnName("ID_");
		idColumn.setJavaType("String");
		idColumn.setLength(50);
		tableDefinition.setIdColumn(idColumn);

		ColumnDefinition subject = new ColumnDefinition();
		subject.setName("subject");
		subject.setColumnName("SUBJECT_");
		subject.setJavaType("String");
		subject.setLength(200);
		tableDefinition.addColumn(subject);

		ColumnDefinition serverIP = new ColumnDefinition();
		serverIP.setName("serverIP");
		serverIP.setColumnName("SERVERIP_");
		serverIP.setJavaType("String");
		serverIP.setLength(200);
		tableDefinition.addColumn(serverIP);

		ColumnDefinition port = new ColumnDefinition();
		port.setName("port");
		port.setColumnName("PORT_");
		port.setJavaType("Integer");
		tableDefinition.addColumn(port);

		ColumnDefinition path = new ColumnDefinition();
		path.setName("path");
		path.setColumnName("PATH_");
		path.setJavaType("String");
		path.setLength(500);
		tableDefinition.addColumn(path);

		ColumnDefinition requestBody = new ColumnDefinition();
		requestBody.setName("requestBody");
		requestBody.setColumnName("REQUESTBODY_");
		requestBody.setJavaType("String");
		requestBody.setLength(4000);
		tableDefinition.addColumn(requestBody);

		ColumnDefinition responseBody = new ColumnDefinition();
		responseBody.setName("responseBody");
		responseBody.setColumnName("RESPONSEBODY_");
		responseBody.setJavaType("String");
		responseBody.setLength(4000);
		tableDefinition.addColumn(responseBody);

		ColumnDefinition responseResult = new ColumnDefinition();
		responseResult.setName("responseResult");
		responseResult.setColumnName("RESPONSERESULT_");
		responseResult.setJavaType("String");
		responseResult.setLength(200);
		tableDefinition.addColumn(responseResult);

		ColumnDefinition frequence = new ColumnDefinition();
		frequence.setName("frequence");
		frequence.setColumnName("FREQUENCE_");
		frequence.setJavaType("Integer");
		tableDefinition.addColumn(frequence);

		ColumnDefinition retryTimes = new ColumnDefinition();
		retryTimes.setName("retryTimes");
		retryTimes.setColumnName("RETRYTIMES_");
		retryTimes.setJavaType("Integer");
		tableDefinition.addColumn(retryTimes);

		ColumnDefinition key = new ColumnDefinition();
		key.setName("key");
		key.setColumnName("KEY_");
		key.setJavaType("String");
		key.setLength(500);
		tableDefinition.addColumn(key);

		ColumnDefinition accessKeyId = new ColumnDefinition();
		accessKeyId.setName("accessKeyId");
		accessKeyId.setColumnName("ACCESSKEYID_");
		accessKeyId.setJavaType("String");
		accessKeyId.setLength(500);
		tableDefinition.addColumn(accessKeyId);

		ColumnDefinition accessKeySecret = new ColumnDefinition();
		accessKeySecret.setName("accessKeySecret");
		accessKeySecret.setColumnName("ACCESSKEYSECRET_");
		accessKeySecret.setJavaType("String");
		accessKeySecret.setLength(500);
		tableDefinition.addColumn(accessKeySecret);

		ColumnDefinition signName = new ColumnDefinition();
		signName.setName("signName");
		signName.setColumnName("SIGNNAME_");
		signName.setJavaType("String");
		signName.setLength(500);
		tableDefinition.addColumn(signName);

		ColumnDefinition templateCode = new ColumnDefinition();
		templateCode.setName("templateCode");
		templateCode.setColumnName("TEMPLATECODE_");
		templateCode.setJavaType("String");
		templateCode.setLength(500);
		tableDefinition.addColumn(templateCode);

		ColumnDefinition provider = new ColumnDefinition();
		provider.setName("provider");
		provider.setColumnName("PROVIDER_");
		provider.setJavaType("String");
		provider.setLength(50);
		tableDefinition.addColumn(provider);

		ColumnDefinition type = new ColumnDefinition();
		type.setName("type");
		type.setColumnName("TYPE_");
		type.setJavaType("String");
		type.setLength(50);
		tableDefinition.addColumn(type);

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

	private SmsServerDomainFactory() {

	}

}
