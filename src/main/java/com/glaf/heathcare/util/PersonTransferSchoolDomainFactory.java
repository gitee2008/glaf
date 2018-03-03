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
public class PersonTransferSchoolDomainFactory {

	public static final String TABLENAME = "HEALTH_PERSON_TRANSFER_SCHOOL";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("tenantId", "TENANTID_");
		columnMap.put("fromTenantId", "FROM_TENANTID_");
		columnMap.put("fromSchool", "FROM_SCHOOL_");
		columnMap.put("toTenantId", "TO_TENANTID_");
		columnMap.put("toSchool", "TO_SCHOOL_");
		columnMap.put("personId", "PERSONID_");
		columnMap.put("name", "NAME_");
		columnMap.put("sex", "SEX_");
		columnMap.put("checkDate", "CHECKDATE_");
		columnMap.put("checkOrganization", "CHECKORGANIZATION_");
		columnMap.put("checkOrganizationId", "CHECKORGANIZATIONID_");
		columnMap.put("checkResult", "CHECKRESULT_");
		columnMap.put("remark", "REMARK_");
		columnMap.put("createBy", "CREATEBY_");
		columnMap.put("createTime", "CREATETIME_");

		javaTypeMap.put("id", "String");
		javaTypeMap.put("tenantId", "String");
		javaTypeMap.put("fromTenantId", "String");
		javaTypeMap.put("fromSchool", "String");
		javaTypeMap.put("toTenantId", "String");
		javaTypeMap.put("toSchool", "String");
		javaTypeMap.put("personId", "String");
		javaTypeMap.put("name", "String");
		javaTypeMap.put("sex", "String");
		javaTypeMap.put("checkDate", "Date");
		javaTypeMap.put("checkOrganization", "String");
		javaTypeMap.put("checkOrganizationId", "Long");
		javaTypeMap.put("checkResult", "String");
		javaTypeMap.put("remark", "String");
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
		tableDefinition.setName("PersonTransferSchool");

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

		ColumnDefinition fromTenantId = new ColumnDefinition();
		fromTenantId.setName("fromTenantId");
		fromTenantId.setColumnName("FROM_TENANTID_");
		fromTenantId.setJavaType("String");
		fromTenantId.setLength(50);
		tableDefinition.addColumn(fromTenantId);

		ColumnDefinition fromSchool = new ColumnDefinition();
		fromSchool.setName("fromSchool");
		fromSchool.setColumnName("FROM_SCHOOL_");
		fromSchool.setJavaType("String");
		fromSchool.setLength(200);
		tableDefinition.addColumn(fromSchool);

		ColumnDefinition toTenantId = new ColumnDefinition();
		toTenantId.setName("toTenantId");
		toTenantId.setColumnName("TO_TENANTID_");
		toTenantId.setJavaType("String");
		toTenantId.setLength(50);
		tableDefinition.addColumn(toTenantId);

		ColumnDefinition toSchool = new ColumnDefinition();
		toSchool.setName("toSchool");
		toSchool.setColumnName("TO_SCHOOL_");
		toSchool.setJavaType("String");
		toSchool.setLength(200);
		tableDefinition.addColumn(toSchool);

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

		ColumnDefinition sex = new ColumnDefinition();
		sex.setName("sex");
		sex.setColumnName("SEX_");
		sex.setJavaType("String");
		sex.setLength(1);
		tableDefinition.addColumn(sex);

		ColumnDefinition checkDate = new ColumnDefinition();
		checkDate.setName("checkDate");
		checkDate.setColumnName("CHECKDATE_");
		checkDate.setJavaType("Date");
		tableDefinition.addColumn(checkDate);

		ColumnDefinition checkOrganization = new ColumnDefinition();
		checkOrganization.setName("checkOrganization");
		checkOrganization.setColumnName("CHECKORGANIZATION_");
		checkOrganization.setJavaType("String");
		checkOrganization.setLength(200);
		tableDefinition.addColumn(checkOrganization);

		ColumnDefinition checkOrganizationId = new ColumnDefinition();
		checkOrganizationId.setName("checkOrganizationId");
		checkOrganizationId.setColumnName("CHECKORGANIZATIONID_");
		checkOrganizationId.setJavaType("Long");
		tableDefinition.addColumn(checkOrganizationId);

		ColumnDefinition checkResult = new ColumnDefinition();
		checkResult.setName("checkResult");
		checkResult.setColumnName("CHECKRESULT_");
		checkResult.setJavaType("String");
		checkResult.setLength(500);
		tableDefinition.addColumn(checkResult);

		ColumnDefinition remark = new ColumnDefinition();
		remark.setName("remark");
		remark.setColumnName("REMARK_");
		remark.setJavaType("String");
		remark.setLength(500);
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

	private PersonTransferSchoolDomainFactory() {

	}

}
