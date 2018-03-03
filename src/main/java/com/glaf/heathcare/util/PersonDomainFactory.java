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
public class PersonDomainFactory {

	public static final String TABLENAME = "HEALTH_PERSON";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("tenantId", "TENANTID_");
		columnMap.put("gradeId", "GRADEID_");
		columnMap.put("name", "NAME_");
		columnMap.put("idCardNo", "IDCARDNO_");
		columnMap.put("patriarch", "PATRIARCH_");
		columnMap.put("telephone", "TELEPHONE_");
		columnMap.put("province", "PROVINCE_");
		columnMap.put("provinceId", "PROVINCEID_");
		columnMap.put("city", "CITY_");
		columnMap.put("cityId", "CITYID_");
		columnMap.put("area", "AREA_");
		columnMap.put("areaId", "AREAID_");
		columnMap.put("town", "TOWN_");
		columnMap.put("townId", "TOWNID_");
		columnMap.put("homeAddress", "HOMEADDRESS_");
		columnMap.put("birthAddress", "BIRTHADDRESS_");
		columnMap.put("sex", "SEX_");
		columnMap.put("birthday", "BIRTHDAY_");
		columnMap.put("year", "YEAR_");
		columnMap.put("joinDate", "JOINDATE_");
		columnMap.put("healthCondition", "HEALTHCONDITION_");
		columnMap.put("allergy", "ALLERGY_");
		columnMap.put("feedingHistory", "FEEDINGHISTORY_");
		columnMap.put("previousHistory", "PREVIOUSHISTORY_");
		columnMap.put("foodAllergy", "FOODALLERGY_");
		columnMap.put("medicineAllergy", "MEDICINEALLERGY_");
		columnMap.put("height", "HEIGHT_");
		columnMap.put("weight", "WEIGHT_");
		columnMap.put("father", "FATHER_");
		columnMap.put("fatherCompany", "FATHERCOMPANY_");
		columnMap.put("fatherTelephone", "FATHERTELEPHONE_");
		columnMap.put("fatherWardship", "FATHERWARDSHIP_");
		columnMap.put("mother", "MOTHER_");
		columnMap.put("motherCompany", "MOTHERCOMPANY_");
		columnMap.put("motherTelephone", "MOTHERTELEPHONE_");
		columnMap.put("motherWardship", "MOTHERWARDSHIP_");
		columnMap.put("remark", "REMARK_");
		columnMap.put("createBy", "CREATEBY_");
		columnMap.put("createTime", "CREATETIME_");
		columnMap.put("updateBy", "UPDATEBY_");
		columnMap.put("updateTime", "UPDATETIME_");
		columnMap.put("deleteFlag", "DELETEFLAG_");

		javaTypeMap.put("id", "String");
		javaTypeMap.put("tenantId", "String");
		javaTypeMap.put("gradeId", "String");
		javaTypeMap.put("name", "String");
		javaTypeMap.put("idCardNo", "String");
		javaTypeMap.put("patriarch", "String");
		javaTypeMap.put("telephone", "String");
		javaTypeMap.put("province", "String");
		javaTypeMap.put("provinceId", "Long");
		javaTypeMap.put("city", "String");
		javaTypeMap.put("cityId", "Long");
		javaTypeMap.put("area", "String");
		javaTypeMap.put("areaId", "Long");
		javaTypeMap.put("town", "String");
		javaTypeMap.put("townId", "Long");
		javaTypeMap.put("homeAddress", "String");
		javaTypeMap.put("birthAddress", "String");
		javaTypeMap.put("sex", "String");
		javaTypeMap.put("birthday", "Date");
		javaTypeMap.put("year", "Integer");
		javaTypeMap.put("joinDate", "Date");
		javaTypeMap.put("healthCondition", "String");
		javaTypeMap.put("allergy", "String");
		javaTypeMap.put("feedingHistory", "String");
		javaTypeMap.put("previousHistory", "String");
		javaTypeMap.put("foodAllergy", "String");
		javaTypeMap.put("medicineAllergy", "String");
		javaTypeMap.put("height", "Double");
		javaTypeMap.put("weight", "Double");
		javaTypeMap.put("father", "String");
		javaTypeMap.put("fatherCompany", "String");
		javaTypeMap.put("fatherTelephone", "String");
		javaTypeMap.put("fatherWardship", "String");
		javaTypeMap.put("mother", "String");
		javaTypeMap.put("motherCompany", "String");
		javaTypeMap.put("motherTelephone", "String");
		javaTypeMap.put("motherWardship", "String");
		javaTypeMap.put("remark", "String");
		javaTypeMap.put("createBy", "String");
		javaTypeMap.put("createTime", "Date");
		javaTypeMap.put("updateBy", "String");
		javaTypeMap.put("updateTime", "Date");
		javaTypeMap.put("deleteFlag", "Integer");
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
		tableDefinition.setName("Person");

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

		ColumnDefinition name = new ColumnDefinition();
		name.setName("name");
		name.setColumnName("NAME_");
		name.setJavaType("String");
		name.setLength(100);
		tableDefinition.addColumn(name);

		ColumnDefinition idCardNo = new ColumnDefinition();
		idCardNo.setName("idCardNo");
		idCardNo.setColumnName("IDCARDNO_");
		idCardNo.setJavaType("String");
		idCardNo.setLength(20);
		tableDefinition.addColumn(idCardNo);

		ColumnDefinition patriarch = new ColumnDefinition();
		patriarch.setName("patriarch");
		patriarch.setColumnName("PATRIARCH_");
		patriarch.setJavaType("String");
		patriarch.setLength(50);
		tableDefinition.addColumn(patriarch);

		ColumnDefinition telephone = new ColumnDefinition();
		telephone.setName("telephone");
		telephone.setColumnName("TELEPHONE_");
		telephone.setJavaType("String");
		telephone.setLength(200);
		tableDefinition.addColumn(telephone);

		ColumnDefinition province = new ColumnDefinition();
		province.setName("province");
		province.setColumnName("PROVINCE_");
		province.setJavaType("String");
		province.setLength(100);
		tableDefinition.addColumn(province);

		ColumnDefinition provinceId = new ColumnDefinition();
		provinceId.setName("provinceId");
		provinceId.setColumnName("PROVINCEID_");
		provinceId.setJavaType("Long");
		tableDefinition.addColumn(provinceId);

		ColumnDefinition city = new ColumnDefinition();
		city.setName("city");
		city.setColumnName("CITY_");
		city.setJavaType("String");
		city.setLength(100);
		tableDefinition.addColumn(city);

		ColumnDefinition cityId = new ColumnDefinition();
		cityId.setName("cityId");
		cityId.setColumnName("CITYID_");
		cityId.setJavaType("Long");
		tableDefinition.addColumn(cityId);

		ColumnDefinition area = new ColumnDefinition();
		area.setName("area");
		area.setColumnName("AREA_");
		area.setJavaType("String");
		area.setLength(100);
		tableDefinition.addColumn(area);

		ColumnDefinition areaId = new ColumnDefinition();
		areaId.setName("areaId");
		areaId.setColumnName("AREAID_");
		areaId.setJavaType("Long");
		tableDefinition.addColumn(areaId);

		ColumnDefinition town = new ColumnDefinition();
		town.setName("town");
		town.setColumnName("TOWN_");
		town.setJavaType("String");
		town.setLength(200);
		tableDefinition.addColumn(town);

		ColumnDefinition townId = new ColumnDefinition();
		townId.setName("townId");
		townId.setColumnName("TOWNID_");
		townId.setJavaType("Long");
		tableDefinition.addColumn(townId);

		ColumnDefinition homeAddress = new ColumnDefinition();
		homeAddress.setName("homeAddress");
		homeAddress.setColumnName("HOMEADDRESS_");
		homeAddress.setJavaType("String");
		homeAddress.setLength(250);
		tableDefinition.addColumn(homeAddress);

		ColumnDefinition birthAddress = new ColumnDefinition();
		birthAddress.setName("birthAddress");
		birthAddress.setColumnName("BIRTHADDRESS_");
		birthAddress.setJavaType("String");
		birthAddress.setLength(250);
		tableDefinition.addColumn(birthAddress);

		ColumnDefinition sex = new ColumnDefinition();
		sex.setName("sex");
		sex.setColumnName("SEX_");
		sex.setJavaType("String");
		sex.setLength(2);
		tableDefinition.addColumn(sex);

		ColumnDefinition birthday = new ColumnDefinition();
		birthday.setName("birthday");
		birthday.setColumnName("BIRTHDAY_");
		birthday.setJavaType("Date");
		tableDefinition.addColumn(birthday);

		ColumnDefinition year = new ColumnDefinition();
		year.setName("year");
		year.setColumnName("YEAR_");
		year.setJavaType("Integer");
		tableDefinition.addColumn(year);

		ColumnDefinition joinDate = new ColumnDefinition();
		joinDate.setName("joinDate");
		joinDate.setColumnName("JOINDATE_");
		joinDate.setJavaType("Date");
		tableDefinition.addColumn(joinDate);

		ColumnDefinition healthCondition = new ColumnDefinition();
		healthCondition.setName("healthCondition");
		healthCondition.setColumnName("HEALTHCONDITION_");
		healthCondition.setJavaType("String");
		healthCondition.setLength(50);
		tableDefinition.addColumn(healthCondition);

		ColumnDefinition allergy = new ColumnDefinition();
		allergy.setName("allergy");
		allergy.setColumnName("ALLERGY_");
		allergy.setJavaType("String");
		allergy.setLength(500);
		tableDefinition.addColumn(allergy);

		ColumnDefinition feedingHistory = new ColumnDefinition();
		feedingHistory.setName("feedingHistory");
		feedingHistory.setColumnName("FEEDINGHISTORY_");
		feedingHistory.setJavaType("String");
		feedingHistory.setLength(500);
		tableDefinition.addColumn(feedingHistory);

		ColumnDefinition previousHistory = new ColumnDefinition();
		previousHistory.setName("previousHistory");
		previousHistory.setColumnName("PREVIOUSHISTORY_");
		previousHistory.setJavaType("String");
		previousHistory.setLength(500);
		tableDefinition.addColumn(previousHistory);

		ColumnDefinition foodAllergy = new ColumnDefinition();
		foodAllergy.setName("foodAllergy");
		foodAllergy.setColumnName("FOODALLERGY_");
		foodAllergy.setJavaType("String");
		foodAllergy.setLength(500);
		tableDefinition.addColumn(foodAllergy);

		ColumnDefinition medicineAllergy = new ColumnDefinition();
		medicineAllergy.setName("medicineAllergy");
		medicineAllergy.setColumnName("MEDICINEALLERGY_");
		medicineAllergy.setJavaType("String");
		medicineAllergy.setLength(500);
		tableDefinition.addColumn(medicineAllergy);

		ColumnDefinition height = new ColumnDefinition();
		height.setName("height");
		height.setColumnName("HEIGHT_");
		height.setJavaType("Double");
		tableDefinition.addColumn(height);

		ColumnDefinition weight = new ColumnDefinition();
		weight.setName("weight");
		weight.setColumnName("WEIGHT_");
		weight.setJavaType("Double");
		tableDefinition.addColumn(weight);

		ColumnDefinition father = new ColumnDefinition();
		father.setName("father");
		father.setColumnName("FATHER_");
		father.setJavaType("String");
		father.setLength(50);
		tableDefinition.addColumn(father);

		ColumnDefinition fatherCompany = new ColumnDefinition();
		fatherCompany.setName("fatherCompany");
		fatherCompany.setColumnName("FATHERCOMPANY_");
		fatherCompany.setJavaType("String");
		fatherCompany.setLength(200);
		tableDefinition.addColumn(fatherCompany);

		ColumnDefinition fatherTelephone = new ColumnDefinition();
		fatherTelephone.setName("fatherTelephone");
		fatherTelephone.setColumnName("FATHERTELEPHONE_");
		fatherTelephone.setJavaType("String");
		fatherTelephone.setLength(200);
		tableDefinition.addColumn(fatherTelephone);

		ColumnDefinition fatherWardship = new ColumnDefinition();
		fatherWardship.setName("fatherWardship");
		fatherWardship.setColumnName("FATHERWARDSHIP_");
		fatherWardship.setJavaType("String");
		fatherWardship.setLength(1);
		tableDefinition.addColumn(fatherWardship);

		ColumnDefinition mother = new ColumnDefinition();
		mother.setName("mother");
		mother.setColumnName("MOTHER_");
		mother.setJavaType("String");
		mother.setLength(50);
		tableDefinition.addColumn(mother);

		ColumnDefinition motherCompany = new ColumnDefinition();
		motherCompany.setName("motherCompany");
		motherCompany.setColumnName("MOTHERCOMPANY_");
		motherCompany.setJavaType("String");
		motherCompany.setLength(200);
		tableDefinition.addColumn(motherCompany);

		ColumnDefinition motherTelephone = new ColumnDefinition();
		motherTelephone.setName("motherTelephone");
		motherTelephone.setColumnName("MOTHERTELEPHONE_");
		motherTelephone.setJavaType("String");
		motherTelephone.setLength(200);
		tableDefinition.addColumn(motherTelephone);

		ColumnDefinition motherWardship = new ColumnDefinition();
		motherWardship.setName("motherWardship");
		motherWardship.setColumnName("MOTHERWARDSHIP_");
		motherWardship.setJavaType("String");
		motherWardship.setLength(1);
		tableDefinition.addColumn(motherWardship);

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

		ColumnDefinition deleteFlag = new ColumnDefinition();
		deleteFlag.setName("deleteFlag");
		deleteFlag.setColumnName("DELETEFLAG_");
		deleteFlag.setJavaType("Integer");
		tableDefinition.addColumn(deleteFlag);

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

	private PersonDomainFactory() {

	}

}
