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
public class MonthlyFeeDomainFactory {

	public static final String TABLENAME = "HEALTH_MONTHLY_FEE";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("tenantId", "TENANTID_");
		columnMap.put("lastMonthSurplus", "LASTMONTHSURPLUS_");
		columnMap.put("monthLeft", "MONTHLEFT_");
		columnMap.put("monthTotalLeft", "MONTHTOTALLEFT_");
		columnMap.put("leftPercent", "LEFTPERCENT_");
		columnMap.put("exceedPercent", "EXCEEDPERCENT_");
		columnMap.put("personMonthlyFee", "PERSONMONTHLYFEE_");
		columnMap.put("fuelFee", "FUELFEE_");
		columnMap.put("laborFee", "LABORFEE_");
		columnMap.put("dessertFee", "DESSERTFEE_");
		columnMap.put("otherFee", "OTHERFEE_");
		columnMap.put("workDay", "WORKDAY_");
		columnMap.put("totalRepastPerson", "TOTALREPASTPERSON_");
		columnMap.put("remark", "REMARK_");
		columnMap.put("year", "YEAR_");
		columnMap.put("month", "MONTH_");
		columnMap.put("semester", "SEMESTER_");
		columnMap.put("createBy", "CREATEBY_");
		columnMap.put("createTime", "CREATETIME_");
		columnMap.put("updateBy", "UPDATEBY_");
		columnMap.put("updateTime", "UPDATETIME_");

		javaTypeMap.put("id", "Long");
		javaTypeMap.put("tenantId", "String");
		javaTypeMap.put("lastMonthSurplus", "Double");
		javaTypeMap.put("monthLeft", "Double");
		javaTypeMap.put("monthTotalLeft", "Double");
		javaTypeMap.put("leftPercent", "Double");
		javaTypeMap.put("exceedPercent", "Double");
		javaTypeMap.put("personMonthlyFee", "Double");
		javaTypeMap.put("fuelFee", "Double");
		javaTypeMap.put("laborFee", "Double");
		javaTypeMap.put("dessertFee", "Double");
		javaTypeMap.put("otherFee", "Double");
		javaTypeMap.put("workDay", "Integer");
		javaTypeMap.put("totalRepastPerson", "Integer");
		javaTypeMap.put("remark", "String");
		javaTypeMap.put("year", "Integer");
		javaTypeMap.put("month", "Integer");
		javaTypeMap.put("semester", "Integer");
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
		tableDefinition.setName("MonthlyFee");

		ColumnDefinition idColumn = new ColumnDefinition();
		idColumn.setName("id");
		idColumn.setColumnName("ID_");
		idColumn.setJavaType("Long");
		tableDefinition.setIdColumn(idColumn);

		ColumnDefinition tenantId = new ColumnDefinition();
		tenantId.setName("tenantId");
		tenantId.setColumnName("TENANTID_");
		tenantId.setJavaType("String");
		tenantId.setLength(50);
		tableDefinition.addColumn(tenantId);

		ColumnDefinition lastMonthSurplus = new ColumnDefinition();
		lastMonthSurplus.setName("lastMonthSurplus");
		lastMonthSurplus.setColumnName("LASTMONTHSURPLUS_");
		lastMonthSurplus.setJavaType("Double");
		tableDefinition.addColumn(lastMonthSurplus);

		ColumnDefinition monthLeft = new ColumnDefinition();
		monthLeft.setName("monthLeft");
		monthLeft.setColumnName("MONTHLEFT_");
		monthLeft.setJavaType("Double");
		tableDefinition.addColumn(monthLeft);

		ColumnDefinition monthTotalLeft = new ColumnDefinition();
		monthTotalLeft.setName("monthTotalLeft");
		monthTotalLeft.setColumnName("MONTHTOTALLEFT_");
		monthTotalLeft.setJavaType("Double");
		tableDefinition.addColumn(monthTotalLeft);

		ColumnDefinition leftPercent = new ColumnDefinition();
		leftPercent.setName("leftPercent");
		leftPercent.setColumnName("LEFTPERCENT_");
		leftPercent.setJavaType("Double");
		tableDefinition.addColumn(leftPercent);

		ColumnDefinition exceedPercent = new ColumnDefinition();
		exceedPercent.setName("exceedPercent");
		exceedPercent.setColumnName("EXCEEDPERCENT_");
		exceedPercent.setJavaType("Double");
		tableDefinition.addColumn(exceedPercent);

		ColumnDefinition personMonthlyFee = new ColumnDefinition();
		personMonthlyFee.setName("personMonthlyFee");
		personMonthlyFee.setColumnName("PERSONMONTHLYFEE_");
		personMonthlyFee.setJavaType("Double");
		tableDefinition.addColumn(personMonthlyFee);

		ColumnDefinition fuelFee = new ColumnDefinition();
		fuelFee.setName("fuelFee");
		fuelFee.setColumnName("FUELFEE_");
		fuelFee.setJavaType("Double");
		tableDefinition.addColumn(fuelFee);

		ColumnDefinition laborFee = new ColumnDefinition();
		laborFee.setName("laborFee");
		laborFee.setColumnName("LABORFEE_");
		laborFee.setJavaType("Double");
		tableDefinition.addColumn(laborFee);

		ColumnDefinition dessertFee = new ColumnDefinition();
		dessertFee.setName("dessertFee");
		dessertFee.setColumnName("DESSERTFEE_");
		dessertFee.setJavaType("Double");
		tableDefinition.addColumn(dessertFee);

		ColumnDefinition otherFee = new ColumnDefinition();
		otherFee.setName("otherFee");
		otherFee.setColumnName("OTHERFEE_");
		otherFee.setJavaType("Double");
		tableDefinition.addColumn(otherFee);

		ColumnDefinition workDay = new ColumnDefinition();
		workDay.setName("workDay");
		workDay.setColumnName("WORKDAY_");
		workDay.setJavaType("Integer");
		tableDefinition.addColumn(workDay);

		ColumnDefinition totalRepastPerson = new ColumnDefinition();
		totalRepastPerson.setName("totalRepastPerson");
		totalRepastPerson.setColumnName("TOTALREPASTPERSON_");
		totalRepastPerson.setJavaType("Integer");
		tableDefinition.addColumn(totalRepastPerson);

		ColumnDefinition remark = new ColumnDefinition();
		remark.setName("remark");
		remark.setColumnName("REMARK_");
		remark.setJavaType("String");
		remark.setLength(4000);
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

		ColumnDefinition semester = new ColumnDefinition();
		semester.setName("semester");
		semester.setColumnName("SEMESTER_");
		semester.setJavaType("Integer");
		tableDefinition.addColumn(semester);

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

	private MonthlyFeeDomainFactory() {

	}

}
