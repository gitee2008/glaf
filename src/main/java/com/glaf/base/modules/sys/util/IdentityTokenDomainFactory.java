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

package com.glaf.base.modules.sys.util;

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
public class IdentityTokenDomainFactory {

	public static final String TABLENAME = "SYS_IDENTITY_TOKEN";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("sessionId", "SESSIONID_");
		columnMap.put("userId", "USERID_");
		columnMap.put("clientIP", "CLIENTIP_");
		columnMap.put("signature", "SIGNATURE_");
		columnMap.put("token", "TOKEN_");
		columnMap.put("nonce", "NONCE_");
		columnMap.put("rand1", "RAND1_");
		columnMap.put("rand2", "RAND2_");
		columnMap.put("type", "TYPE_");
		columnMap.put("timeLive", "TIMELIVE_");
		columnMap.put("timeMillis", "TIMEMILLIS_");
		columnMap.put("createTime", "CREATETIME_");

		javaTypeMap.put("id", "String");
		javaTypeMap.put("sessionId", "String");
		javaTypeMap.put("userId", "String");
		javaTypeMap.put("clientIP", "String");
		javaTypeMap.put("signature", "String");
		javaTypeMap.put("token", "String");
		javaTypeMap.put("nonce", "String");
		javaTypeMap.put("rand1", "String");
		javaTypeMap.put("rand2", "String");
		javaTypeMap.put("type", "String");
		javaTypeMap.put("timeLive", "Integer");
		javaTypeMap.put("timeMillis", "Long");
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
		tableDefinition.setName("IdentityToken");

		ColumnDefinition idColumn = new ColumnDefinition();
		idColumn.setName("id");
		idColumn.setColumnName("ID_");
		idColumn.setJavaType("String");
		idColumn.setLength(100);
		tableDefinition.setIdColumn(idColumn);

		ColumnDefinition sessionId = new ColumnDefinition();
		sessionId.setName("sessionId");
		sessionId.setColumnName("SESSIONID_");
		sessionId.setJavaType("String");
		sessionId.setLength(200);
		tableDefinition.addColumn(sessionId);

		ColumnDefinition userId = new ColumnDefinition();
		userId.setName("userId");
		userId.setColumnName("USERID_");
		userId.setJavaType("String");
		userId.setLength(200);
		tableDefinition.addColumn(userId);

		ColumnDefinition clientIP = new ColumnDefinition();
		clientIP.setName("clientIP");
		clientIP.setColumnName("CLIENTIP_");
		clientIP.setJavaType("String");
		clientIP.setLength(200);
		tableDefinition.addColumn(clientIP);

		ColumnDefinition signature = new ColumnDefinition();
		signature.setName("signature");
		signature.setColumnName("SIGNATURE_");
		signature.setJavaType("String");
		signature.setLength(500);
		tableDefinition.addColumn(signature);

		ColumnDefinition token = new ColumnDefinition();
		token.setName("token");
		token.setColumnName("TOKEN_");
		token.setJavaType("String");
		token.setLength(200);
		tableDefinition.addColumn(token);

		ColumnDefinition nonce = new ColumnDefinition();
		nonce.setName("nonce");
		nonce.setColumnName("NONCE_");
		nonce.setJavaType("String");
		nonce.setLength(50);
		tableDefinition.addColumn(nonce);

		ColumnDefinition rand1 = new ColumnDefinition();
		rand1.setName("rand1");
		rand1.setColumnName("RAND1_");
		rand1.setJavaType("String");
		rand1.setLength(200);
		tableDefinition.addColumn(rand1);

		ColumnDefinition rand2 = new ColumnDefinition();
		rand2.setName("rand2");
		rand2.setColumnName("RAND2_");
		rand2.setJavaType("String");
		rand2.setLength(200);
		tableDefinition.addColumn(rand2);

		ColumnDefinition type = new ColumnDefinition();
		type.setName("type");
		type.setColumnName("TYPE_");
		type.setJavaType("String");
		type.setLength(50);
		tableDefinition.addColumn(type);

		ColumnDefinition timeLive = new ColumnDefinition();
		timeLive.setName("timeLive");
		timeLive.setColumnName("TIMELIVE_");
		timeLive.setJavaType("Integer");
		tableDefinition.addColumn(timeLive);

		ColumnDefinition timeMillis = new ColumnDefinition();
		timeMillis.setName("timeMillis");
		timeMillis.setColumnName("TIMEMILLIS_");
		timeMillis.setJavaType("Long");
		tableDefinition.addColumn(timeMillis);

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

	private IdentityTokenDomainFactory() {

	}

}
