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

package com.glaf.heathcare.util;

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
public class PersonLinkmanDomainFactory {

	public static final String TABLENAME = "HEALTH_PERSON_LINKMAN";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("tenantId", "TENANTID_");
		columnMap.put("personId", "PERSONID_");
		columnMap.put("name", "NAME_");
		columnMap.put("relationship", "RELATIONSHIP_");
		columnMap.put("company", "COMPANY_");
		columnMap.put("mobile", "MOBILE_");
		columnMap.put("mail", "MAIL_");
		columnMap.put("wardship", "WARDSHIP_");
		columnMap.put("remark", "REMARK_");
		columnMap.put("createBy", "CREATEBY_");
		columnMap.put("createTime", "CREATETIME_");
		columnMap.put("updateBy", "UPDATEBY_");
		columnMap.put("updateTime", "UPDATETIME_");

		javaTypeMap.put("id", "String");
		javaTypeMap.put("tenantId", "String");
		javaTypeMap.put("personId", "String");
		javaTypeMap.put("name", "String");
		javaTypeMap.put("relationship", "String");
		javaTypeMap.put("company", "String");
		javaTypeMap.put("mobile", "String");
		javaTypeMap.put("mail", "String");
		javaTypeMap.put("wardship", "String");
		javaTypeMap.put("remark", "String");
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
		tableDefinition.setName("PersonLinkman");

		ColumnDefinition idColumn = new ColumnDefinition();
		idColumn.setName("id");
		idColumn.setColumnName("ID_");
		idColumn.setJavaType("String");
		idColumn.setLength(50);
		tableDefinition.setIdColumn(idColumn);

		ColumnDefinition tenantId = new ColumnDefinition();
		tenantId.setName("tenantId");
		tenantId.setColumnName("TENANTID_");
		tenantId.setJavaType("String");
		tenantId.setLength(50);
		tableDefinition.addColumn(tenantId);

		ColumnDefinition personId = new ColumnDefinition();
		personId.setName("personId");
		personId.setColumnName("PERSONID_");
		personId.setJavaType("String");
		personId.setLength(50);
		tableDefinition.addColumn(personId);

		ColumnDefinition name = new ColumnDefinition();
		name.setName("name");
		name.setColumnName("NAME_");
		name.setJavaType("String");
		name.setLength(100);
		tableDefinition.addColumn(name);

		ColumnDefinition relationship = new ColumnDefinition();
		relationship.setName("relationship");
		relationship.setColumnName("RELATIONSHIP_");
		relationship.setJavaType("String");
		relationship.setLength(50);
		tableDefinition.addColumn(relationship);

		ColumnDefinition company = new ColumnDefinition();
		company.setName("company");
		company.setColumnName("COMPANY_");
		company.setJavaType("String");
		company.setLength(200);
		tableDefinition.addColumn(company);

		ColumnDefinition mobile = new ColumnDefinition();
		mobile.setName("mobile");
		mobile.setColumnName("MOBILE_");
		mobile.setJavaType("String");
		mobile.setLength(50);
		tableDefinition.addColumn(mobile);

		ColumnDefinition mail = new ColumnDefinition();
		mail.setName("mail");
		mail.setColumnName("MAIL_");
		mail.setJavaType("String");
		mail.setLength(200);
		tableDefinition.addColumn(mail);

		ColumnDefinition wardship = new ColumnDefinition();
		wardship.setName("wardship");
		wardship.setColumnName("WARDSHIP_");
		wardship.setJavaType("String");
		wardship.setLength(1);
		tableDefinition.addColumn(wardship);

		ColumnDefinition remark = new ColumnDefinition();
		remark.setName("remark");
		remark.setColumnName("REMARK_");
		remark.setJavaType("String");
		remark.setLength(2000);
		tableDefinition.addColumn(remark);

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

	private PersonLinkmanDomainFactory() {

	}

}
