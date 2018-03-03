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
public class PersonSicknessDomainFactory {

	public static final String TABLENAME = "HEALTH_PERSON_SICKNESS";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("tenantId", "TENANTID_");
		columnMap.put("gradeId", "GRADEID_");
		columnMap.put("personId", "PERSONID_");
		columnMap.put("name", "NAME_");
		columnMap.put("sickness", "SICKNESS_");
		columnMap.put("infectiousFlag", "INFECTIOUSFLAG_");
		columnMap.put("infectiousDisease", "INFECTIOUSDISEASE_");
		columnMap.put("discoverTime", "DISCOVERTIME_");
		columnMap.put("reportTime", "REPORTTIME_");
		columnMap.put("reportOrg", "REPORTORG_");
		columnMap.put("reportResponsible", "REPORTRESPONSIBLE_");
		columnMap.put("reportWay", "REPORTWAY_");
		columnMap.put("supervisionOpinion", "SUPERVISIONOPINION_");
		columnMap.put("confirmTime", "CONFIRMTIME_");
		columnMap.put("receiver1", "RECEIVER1_");
		columnMap.put("receiveOrg1", "RECEIVEORG1_");
		columnMap.put("receiver2", "RECEIVER2_");
		columnMap.put("receiveOrg2", "RECEIVEORG2_");
		columnMap.put("symptom", "SYMPTOM_");
		columnMap.put("treat", "TREAT_");
		columnMap.put("hospital", "HOSPITAL_");
		columnMap.put("clinicTime", "CLINICTIME_");
		columnMap.put("result", "RESULT_");
		columnMap.put("remark", "REMARK_");
		columnMap.put("year", "YEAR_");
		columnMap.put("month", "MONTH_");
		columnMap.put("createBy", "CREATEBY_");
		columnMap.put("createTime", "CREATETIME_");

		javaTypeMap.put("id", "String");
		javaTypeMap.put("tenantId", "String");
		javaTypeMap.put("gradeId", "String");
		javaTypeMap.put("personId", "String");
		javaTypeMap.put("name", "String");
		javaTypeMap.put("sickness", "String");
		javaTypeMap.put("infectiousFlag", "String");
		javaTypeMap.put("infectiousDisease", "String");
		javaTypeMap.put("discoverTime", "Date");
		javaTypeMap.put("reportTime", "Date");
		javaTypeMap.put("reportOrg", "String");
		javaTypeMap.put("reportResponsible", "String");
		javaTypeMap.put("reportWay", "String");
		javaTypeMap.put("supervisionOpinion", "String");
		javaTypeMap.put("confirmTime", "Date");
		javaTypeMap.put("receiver1", "String");
		javaTypeMap.put("receiveOrg1", "String");
		javaTypeMap.put("receiver2", "String");
		javaTypeMap.put("receiveOrg2", "String");
		javaTypeMap.put("symptom", "String");
		javaTypeMap.put("treat", "String");
		javaTypeMap.put("hospital", "String");
		javaTypeMap.put("clinicTime", "Date");
		javaTypeMap.put("result", "String");
		javaTypeMap.put("remark", "String");
		javaTypeMap.put("year", "Integer");
		javaTypeMap.put("month", "Integer");
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
		tableDefinition.setName("PersonSickness");

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

		ColumnDefinition gradeId = new ColumnDefinition();
		gradeId.setName("gradeId");
		gradeId.setColumnName("GRADEID_");
		gradeId.setJavaType("String");
		gradeId.setLength(50);
		tableDefinition.addColumn(gradeId);

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

		ColumnDefinition sickness = new ColumnDefinition();
		sickness.setName("sickness");
		sickness.setColumnName("SICKNESS_");
		sickness.setJavaType("String");
		sickness.setLength(200);
		tableDefinition.addColumn(sickness);

		ColumnDefinition infectiousFlag = new ColumnDefinition();
		infectiousFlag.setName("infectiousFlag");
		infectiousFlag.setColumnName("INFECTIOUSFLAG_");
		infectiousFlag.setJavaType("String");
		infectiousFlag.setLength(1);
		tableDefinition.addColumn(infectiousFlag);

		ColumnDefinition infectiousDisease = new ColumnDefinition();
		infectiousDisease.setName("infectiousDisease");
		infectiousDisease.setColumnName("INFECTIOUSDISEASE_");
		infectiousDisease.setJavaType("String");
		infectiousDisease.setLength(50);
		tableDefinition.addColumn(infectiousDisease);

		ColumnDefinition discoverTime = new ColumnDefinition();
		discoverTime.setName("discoverTime");
		discoverTime.setColumnName("DISCOVERTIME_");
		discoverTime.setJavaType("Date");
		tableDefinition.addColumn(discoverTime);

		ColumnDefinition reportTime = new ColumnDefinition();
		reportTime.setName("reportTime");
		reportTime.setColumnName("REPORTTIME_");
		reportTime.setJavaType("Date");
		tableDefinition.addColumn(reportTime);

		ColumnDefinition reportOrg = new ColumnDefinition();
		reportOrg.setName("reportOrg");
		reportOrg.setColumnName("REPORTORG_");
		reportOrg.setJavaType("String");
		reportOrg.setLength(200);
		tableDefinition.addColumn(reportOrg);

		ColumnDefinition reportResponsible = new ColumnDefinition();
		reportResponsible.setName("reportResponsible");
		reportResponsible.setColumnName("REPORTRESPONSIBLE_");
		reportResponsible.setJavaType("String");
		reportResponsible.setLength(200);
		tableDefinition.addColumn(reportResponsible);

		ColumnDefinition reportWay = new ColumnDefinition();
		reportWay.setName("reportWay");
		reportWay.setColumnName("REPORTWAY_");
		reportWay.setJavaType("String");
		reportWay.setLength(50);
		tableDefinition.addColumn(reportWay);

		ColumnDefinition supervisionOpinion = new ColumnDefinition();
		supervisionOpinion.setName("supervisionOpinion");
		supervisionOpinion.setColumnName("SUPERVISIONOPINION_");
		supervisionOpinion.setJavaType("String");
		supervisionOpinion.setLength(500);
		tableDefinition.addColumn(supervisionOpinion);

		ColumnDefinition confirmTime = new ColumnDefinition();
		confirmTime.setName("confirmTime");
		confirmTime.setColumnName("CONFIRMTIME_");
		confirmTime.setJavaType("Date");
		tableDefinition.addColumn(confirmTime);

		ColumnDefinition receiver1 = new ColumnDefinition();
		receiver1.setName("receiver1");
		receiver1.setColumnName("RECEIVER1_");
		receiver1.setJavaType("String");
		receiver1.setLength(200);
		tableDefinition.addColumn(receiver1);

		ColumnDefinition receiveOrg1 = new ColumnDefinition();
		receiveOrg1.setName("receiveOrg1");
		receiveOrg1.setColumnName("RECEIVEORG1_");
		receiveOrg1.setJavaType("String");
		receiveOrg1.setLength(200);
		tableDefinition.addColumn(receiveOrg1);

		ColumnDefinition receiver2 = new ColumnDefinition();
		receiver2.setName("receiver2");
		receiver2.setColumnName("RECEIVER2_");
		receiver2.setJavaType("String");
		receiver2.setLength(200);
		tableDefinition.addColumn(receiver2);

		ColumnDefinition receiveOrg2 = new ColumnDefinition();
		receiveOrg2.setName("receiveOrg2");
		receiveOrg2.setColumnName("RECEIVEORG2_");
		receiveOrg2.setJavaType("String");
		receiveOrg2.setLength(200);
		tableDefinition.addColumn(receiveOrg2);

		ColumnDefinition symptom = new ColumnDefinition();
		symptom.setName("symptom");
		symptom.setColumnName("SYMPTOM_");
		symptom.setJavaType("String");
		symptom.setLength(2000);
		tableDefinition.addColumn(symptom);

		ColumnDefinition treat = new ColumnDefinition();
		treat.setName("treat");
		treat.setColumnName("TREAT_");
		treat.setJavaType("String");
		treat.setLength(2000);
		tableDefinition.addColumn(treat);

		ColumnDefinition hospital = new ColumnDefinition();
		hospital.setName("hospital");
		hospital.setColumnName("HOSPITAL_");
		hospital.setJavaType("String");
		hospital.setLength(200);
		tableDefinition.addColumn(hospital);

		ColumnDefinition clinicTime = new ColumnDefinition();
		clinicTime.setName("clinicTime");
		clinicTime.setColumnName("CLINICTIME_");
		clinicTime.setJavaType("Date");
		tableDefinition.addColumn(clinicTime);

		ColumnDefinition result = new ColumnDefinition();
		result.setName("result");
		result.setColumnName("RESULT_");
		result.setJavaType("String");
		result.setLength(200);
		tableDefinition.addColumn(result);

		ColumnDefinition remark = new ColumnDefinition();
		remark.setName("remark");
		remark.setColumnName("REMARK_");
		remark.setJavaType("String");
		remark.setLength(2000);
		tableDefinition.addColumn(remark);

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

	private PersonSicknessDomainFactory() {

	}

}
