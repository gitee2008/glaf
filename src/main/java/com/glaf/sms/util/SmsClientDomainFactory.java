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
public class SmsClientDomainFactory {

	public static final String TABLENAME = "SMS_CLIENT";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("subject", "SUBJECT_");
		columnMap.put("remoteIP", "REMOTEIP_");
		columnMap.put("sysCode", "SYSCODE_");
		columnMap.put("sysPwd", "SYSPWD_");
		columnMap.put("encryptionFlag", "ENCRYPTIONFLAG_");
		columnMap.put("publicKey", "PUBLICKEY_");
		columnMap.put("privateKey", "PRIVATEKEY_");
		columnMap.put("peerPublicKey", "PEERPUBLICKEY_");
		columnMap.put("token", "TOKEN_");
		columnMap.put("type", "TYPE_");
		columnMap.put("frequence", "FREQUENCE_");
		columnMap.put("limit", "LIMIT_");
		columnMap.put("locked", "LOCKED_");
		columnMap.put("createBy", "CREATEBY_");
		columnMap.put("createTime", "CREATETIME_");

		javaTypeMap.put("id", "String");
		javaTypeMap.put("subject", "String");
		javaTypeMap.put("remoteIP", "String");
		javaTypeMap.put("sysCode", "String");
		javaTypeMap.put("sysPwd", "String");
		javaTypeMap.put("encryptionFlag", "String");
		javaTypeMap.put("publicKey", "String");
		javaTypeMap.put("privateKey", "String");
		javaTypeMap.put("peerPublicKey", "String");
		javaTypeMap.put("token", "String");
		javaTypeMap.put("type", "String");
		javaTypeMap.put("frequence", "Integer");
		javaTypeMap.put("limit", "Integer");
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
		tableDefinition.setName("SmsClient");

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

		ColumnDefinition remoteIP = new ColumnDefinition();
		remoteIP.setName("remoteIP");
		remoteIP.setColumnName("REMOTEIP_");
		remoteIP.setJavaType("String");
		remoteIP.setLength(200);
		tableDefinition.addColumn(remoteIP);

		ColumnDefinition sysCode = new ColumnDefinition();
		sysCode.setName("sysCode");
		sysCode.setColumnName("SYSCODE_");
		sysCode.setJavaType("String");
		sysCode.setLength(200);
		tableDefinition.addColumn(sysCode);

		ColumnDefinition sysPwd = new ColumnDefinition();
		sysPwd.setName("sysPwd");
		sysPwd.setColumnName("SYSPWD_");
		sysPwd.setJavaType("String");
		sysPwd.setLength(500);
		tableDefinition.addColumn(sysPwd);

		ColumnDefinition encryptionFlag = new ColumnDefinition();
		encryptionFlag.setName("encryptionFlag");
		encryptionFlag.setColumnName("ENCRYPTIONFLAG_");
		encryptionFlag.setJavaType("String");
		encryptionFlag.setLength(50);
		tableDefinition.addColumn(encryptionFlag);

		ColumnDefinition publicKey = new ColumnDefinition();
		publicKey.setName("publicKey");
		publicKey.setColumnName("PUBLICKEY_");
		publicKey.setJavaType("String");
		publicKey.setLength(4000);
		tableDefinition.addColumn(publicKey);

		ColumnDefinition privateKey = new ColumnDefinition();
		privateKey.setName("privateKey");
		privateKey.setColumnName("PRIVATEKEY_");
		privateKey.setJavaType("String");
		privateKey.setLength(4000);
		tableDefinition.addColumn(privateKey);

		ColumnDefinition peerPublicKey = new ColumnDefinition();
		peerPublicKey.setName("peerPublicKey");
		peerPublicKey.setColumnName("PEERPUBLICKEY_");
		peerPublicKey.setJavaType("String");
		peerPublicKey.setLength(4000);
		tableDefinition.addColumn(peerPublicKey);

		ColumnDefinition token = new ColumnDefinition();
		token.setName("token");
		token.setColumnName("TOKEN_");
		token.setJavaType("String");
		token.setLength(200);
		tableDefinition.addColumn(token);

		ColumnDefinition type = new ColumnDefinition();
		type.setName("type");
		type.setColumnName("TYPE_");
		type.setJavaType("String");
		type.setLength(50);
		tableDefinition.addColumn(type);

		ColumnDefinition frequence = new ColumnDefinition();
		frequence.setName("frequence");
		frequence.setColumnName("FREQUENCE_");
		frequence.setJavaType("Integer");
		tableDefinition.addColumn(frequence);

		ColumnDefinition limit = new ColumnDefinition();
		limit.setName("limit");
		limit.setColumnName("LIMIT_");
		limit.setJavaType("Integer");
		tableDefinition.addColumn(limit);

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

	private SmsClientDomainFactory() {

	}

}
