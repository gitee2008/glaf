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

package com.glaf.core.domain.util;

import java.util.Map;
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
public class DatabaseDomainFactory {

	public static final String TABLENAME = "SYS_DATABASE";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("parentId", "PARENTID_");
		columnMap.put("name", "NAME_");
		columnMap.put("code", "CODE_");
		columnMap.put("discriminator", "DISCRIMINATOR_");
		columnMap.put("mapping", "MAPPING_");
		columnMap.put("section", "SECTION_");
		columnMap.put("title", "TITLE_");
		columnMap.put("host", "HOST_");
		columnMap.put("port", "PORT_");
		columnMap.put("user", "USER_");
		columnMap.put("password", "PASSWORD_");
		columnMap.put("key", "KEY_");
		columnMap.put("intToken", "INTTOKEN_");
		columnMap.put("token", "TOKEN_");
		columnMap.put("type", "TYPE_");
		columnMap.put("runType", "RUNTYPE_");
		columnMap.put("useType", "USEYPE_");
		columnMap.put("level", "LEVEL_");
		columnMap.put("priority", "PRIORITY_");
		columnMap.put("operation", "OPERATION_");
		columnMap.put("dbname", "DBNAME_");
		columnMap.put("bucket", "BUCKET_");
		columnMap.put("catalog", "CATALOG_");
		columnMap.put("infoServer", "INFOSERVER_");
		columnMap.put("loginAs", "LOGINAS_");
		columnMap.put("loginUrl", "LOGINURL_");
		columnMap.put("ticket", "TICKET_");
		columnMap.put("programId", "PROGRAMID_");
		columnMap.put("programName", "PROGRAMNAME_");
		columnMap.put("userNameKey", "USERNAMEKEY_");
		columnMap.put("active", "ACTIVE_");
		columnMap.put("verify", "VERIFY_");
		columnMap.put("initFlag", "INITFLAG_");
		columnMap.put("removeFlag", "REMOVEFLAG_");
		columnMap.put("providerClass", "PROVIDERCLASS_");
		columnMap.put("remoteUrl", "REMOTEURL_");
		columnMap.put("serverId", "SERVERID_");
		columnMap.put("sysId", "SYSID_");
		columnMap.put("queueName", "QUEUENAME_");
		columnMap.put("sort", "SORTNO_");
		columnMap.put("createBy", "CREATEBY_");
		columnMap.put("createTime", "CREATETIME_");
		columnMap.put("updateBy", "UPDATEBY_");
		columnMap.put("updateTime", "UPDATETIME_");

		javaTypeMap.put("id", "Long");
		javaTypeMap.put("parentId", "Long");
		javaTypeMap.put("name", "String");
		javaTypeMap.put("mapping", "String");
		javaTypeMap.put("section", "String");
		javaTypeMap.put("code", "String");
		javaTypeMap.put("discriminator", "String");
		javaTypeMap.put("title", "String");
		javaTypeMap.put("host", "String");
		javaTypeMap.put("port", "Integer");
		javaTypeMap.put("user", "String");
		javaTypeMap.put("password", "String");
		javaTypeMap.put("key", "String");
		javaTypeMap.put("intToken", "Integer");
		javaTypeMap.put("token", "String");
		javaTypeMap.put("type", "String");
		javaTypeMap.put("runType", "String");
		javaTypeMap.put("useType", "String");
		javaTypeMap.put("level", "Integer");
		javaTypeMap.put("priority", "Integer");
		javaTypeMap.put("operation", "Integer");
		javaTypeMap.put("dbname", "String");
		javaTypeMap.put("bucket", "String");
		javaTypeMap.put("catalog", "String");
		javaTypeMap.put("infoServer", "String");
		javaTypeMap.put("loginAs", "String");
		javaTypeMap.put("loginUrl", "String");
		javaTypeMap.put("ticket", "String");
		javaTypeMap.put("programId", "String");
		javaTypeMap.put("programName", "String");
		javaTypeMap.put("userNameKey", "String");
		javaTypeMap.put("serverId", "Long");
		javaTypeMap.put("sysId", "String");
		javaTypeMap.put("queueName", "String");
		javaTypeMap.put("active", "String");
		javaTypeMap.put("verify", "String");
		javaTypeMap.put("initFlag", "String");
		javaTypeMap.put("removeFlag", "String");
		javaTypeMap.put("providerClass", "String");
		javaTypeMap.put("remoteUrl", "String");
		javaTypeMap.put("sort", "Integer");
		javaTypeMap.put("createBy", "String");
		javaTypeMap.put("createTime", "Date");
		javaTypeMap.put("updateBy", "String");
		javaTypeMap.put("updateTime", "Date");
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

		ColumnDefinition title = new ColumnDefinition();
		title.setName("title");
		title.setColumnName("TITLE_");
		title.setJavaType("String");
		title.setLength(200);
		tableDefinition.addColumn(title);

		ColumnDefinition code = new ColumnDefinition();
		code.setName("code");
		code.setColumnName("CODE_");
		code.setJavaType("String");
		code.setLength(50);
		tableDefinition.addColumn(code);

		ColumnDefinition discriminator = new ColumnDefinition();
		discriminator.setName("discriminator");
		discriminator.setColumnName("DISCRIMINATOR_");
		discriminator.setJavaType("String");
		discriminator.setLength(10);
		tableDefinition.addColumn(discriminator);

		ColumnDefinition mapping = new ColumnDefinition();
		mapping.setName("mapping");
		mapping.setColumnName("MAPPING_");
		mapping.setJavaType("String");
		mapping.setLength(50);
		tableDefinition.addColumn(mapping);

		ColumnDefinition section = new ColumnDefinition();
		section.setName("section");
		section.setColumnName("SECTION_");
		section.setJavaType("String");
		section.setLength(50);
		tableDefinition.addColumn(section);

		ColumnDefinition host = new ColumnDefinition();
		host.setName("host");
		host.setColumnName("HOST_");
		host.setJavaType("String");
		host.setLength(100);
		tableDefinition.addColumn(host);

		ColumnDefinition port = new ColumnDefinition();
		port.setName("port");
		port.setColumnName("PORT_");
		port.setJavaType("Integer");
		tableDefinition.addColumn(port);

		ColumnDefinition user = new ColumnDefinition();
		user.setName("user");
		user.setColumnName("USER_");
		user.setJavaType("String");
		user.setLength(50);
		tableDefinition.addColumn(user);

		ColumnDefinition password = new ColumnDefinition();
		password.setName("password");
		password.setColumnName("PASSWORD_");
		password.setJavaType("String");
		password.setLength(100);
		tableDefinition.addColumn(password);

		ColumnDefinition key = new ColumnDefinition();
		key.setName("key");
		key.setColumnName("KEY_");
		key.setJavaType("String");
		key.setLength(1024);
		tableDefinition.addColumn(key);

		ColumnDefinition intToken = new ColumnDefinition();
		intToken.setName("intToken");
		intToken.setColumnName("INTTOKEN_");
		intToken.setJavaType("Integer");
		tableDefinition.addColumn(intToken);

		ColumnDefinition token = new ColumnDefinition();
		token.setName("token");
		token.setColumnName("TOKEN_");
		token.setJavaType("String");
		token.setLength(200);
		tableDefinition.addColumn(token);

		ColumnDefinition name = new ColumnDefinition();
		name.setName("name");
		name.setColumnName("NAME_");
		name.setJavaType("String");
		name.setLength(200);
		tableDefinition.addColumn(name);

		ColumnDefinition dbname = new ColumnDefinition();
		dbname.setName("dbname");
		dbname.setColumnName("DBNAME_");
		dbname.setJavaType("String");
		dbname.setLength(50);
		tableDefinition.addColumn(dbname);

		ColumnDefinition bucket = new ColumnDefinition();
		bucket.setName("bucket");
		bucket.setColumnName("BUCKET_");
		bucket.setJavaType("String");
		bucket.setLength(50);
		tableDefinition.addColumn(bucket);

		ColumnDefinition catalog = new ColumnDefinition();
		catalog.setName("catalog");
		catalog.setColumnName("CATALOG_");
		catalog.setJavaType("String");
		catalog.setLength(50);
		tableDefinition.addColumn(catalog);

		ColumnDefinition infoServer = new ColumnDefinition();
		infoServer.setName("infoServer");
		infoServer.setColumnName("INFOSERVER_");
		infoServer.setJavaType("String");
		infoServer.setLength(50);
		tableDefinition.addColumn(infoServer);

		ColumnDefinition loginAs = new ColumnDefinition();
		loginAs.setName("loginAs");
		loginAs.setColumnName("LOGINAS_");
		loginAs.setJavaType("String");
		loginAs.setLength(50);
		tableDefinition.addColumn(loginAs);

		ColumnDefinition loginUrl = new ColumnDefinition();
		loginUrl.setName("loginUrl");
		loginUrl.setColumnName("LOGINURL_");
		loginUrl.setJavaType("String");
		loginUrl.setLength(250);
		tableDefinition.addColumn(loginUrl);

		ColumnDefinition ticket = new ColumnDefinition();
		ticket.setName("ticket");
		ticket.setColumnName("TICKET_");
		ticket.setJavaType("String");
		ticket.setLength(100);
		tableDefinition.addColumn(ticket);

		ColumnDefinition programId = new ColumnDefinition();
		programId.setName("programId");
		programId.setColumnName("PROGRAMID_");
		programId.setJavaType("String");
		programId.setLength(250);
		tableDefinition.addColumn(programId);

		ColumnDefinition programName = new ColumnDefinition();
		programName.setName("programName");
		programName.setColumnName("PROGRAMNAME_");
		programName.setJavaType("String");
		programName.setLength(250);
		tableDefinition.addColumn(programName);

		ColumnDefinition userNameKey = new ColumnDefinition();
		userNameKey.setName("userNameKey");
		userNameKey.setColumnName("USERNAMEKEY_");
		userNameKey.setJavaType("String");
		userNameKey.setLength(100);
		tableDefinition.addColumn(userNameKey);

		ColumnDefinition serverId = new ColumnDefinition();
		serverId.setName("serverId");
		serverId.setColumnName("SERVERID_");
		serverId.setJavaType("Long");
		tableDefinition.addColumn(serverId);

		ColumnDefinition sysId = new ColumnDefinition();
		sysId.setName("sysId");
		sysId.setColumnName("SYSID_");
		sysId.setJavaType("String");
		sysId.setLength(50);
		tableDefinition.addColumn(sysId);

		ColumnDefinition queueName = new ColumnDefinition();
		queueName.setName("queueName");
		queueName.setColumnName("QUEUENAME_");
		queueName.setJavaType("String");
		queueName.setLength(200);
		tableDefinition.addColumn(queueName);

		ColumnDefinition type = new ColumnDefinition();
		type.setName("type");
		type.setColumnName("TYPE_");
		type.setJavaType("String");
		type.setLength(50);
		tableDefinition.addColumn(type);

		ColumnDefinition runType = new ColumnDefinition();
		runType.setName("runType");
		runType.setColumnName("RUNTYPE_");
		runType.setJavaType("String");
		runType.setLength(50);
		tableDefinition.addColumn(runType);

		ColumnDefinition useType = new ColumnDefinition();
		useType.setName("useType");
		useType.setColumnName("USEYPE_");
		useType.setJavaType("String");
		useType.setLength(50);
		tableDefinition.addColumn(useType);

		ColumnDefinition level = new ColumnDefinition();
		level.setName("level");
		level.setColumnName("LEVEL_");
		level.setJavaType("Integer");
		tableDefinition.addColumn(level);

		ColumnDefinition priority = new ColumnDefinition();
		priority.setName("priority");
		priority.setColumnName("PRIORITY_");
		priority.setJavaType("Integer");
		tableDefinition.addColumn(priority);

		ColumnDefinition operation = new ColumnDefinition();
		operation.setName("operation");
		operation.setColumnName("OPERATION_");
		operation.setJavaType("Integer");
		tableDefinition.addColumn(operation);

		ColumnDefinition providerClass = new ColumnDefinition();
		providerClass.setName("providerClass");
		providerClass.setColumnName("PROVIDERCLASS_");
		providerClass.setJavaType("String");
		providerClass.setLength(100);
		tableDefinition.addColumn(providerClass);

		ColumnDefinition remoteUrl = new ColumnDefinition();
		remoteUrl.setName("remoteUrl");
		remoteUrl.setColumnName("REMOTEURL_");
		remoteUrl.setJavaType("String");
		remoteUrl.setLength(500);
		tableDefinition.addColumn(remoteUrl);

		ColumnDefinition active = new ColumnDefinition();
		active.setName("active");
		active.setColumnName("ACTIVE_");
		active.setJavaType("String");
		active.setLength(10);
		tableDefinition.addColumn(active);

		ColumnDefinition verify = new ColumnDefinition();
		verify.setName("verify");
		verify.setColumnName("VERIFY_");
		verify.setJavaType("String");
		verify.setLength(10);
		tableDefinition.addColumn(verify);

		ColumnDefinition initFlag = new ColumnDefinition();
		initFlag.setName("initFlag");
		initFlag.setColumnName("INITFLAG_");
		initFlag.setJavaType("String");
		initFlag.setLength(10);
		tableDefinition.addColumn(initFlag);

		ColumnDefinition removeFlag = new ColumnDefinition();
		removeFlag.setName("removeFlag");
		removeFlag.setColumnName("REMOVEFLAG_");
		removeFlag.setJavaType("String");
		removeFlag.setLength(10);
		tableDefinition.addColumn(removeFlag);

		ColumnDefinition sort = new ColumnDefinition();
		sort.setName("sort");
		sort.setColumnName("SORTNO_");
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

	private DatabaseDomainFactory() {

	}

}
