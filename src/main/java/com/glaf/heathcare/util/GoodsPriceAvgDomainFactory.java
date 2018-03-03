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
public class GoodsPriceAvgDomainFactory {

	public static final String TABLENAME = "GOODS_PRICE_AVG";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("province", "PROVINCE_");
		columnMap.put("provinceId", "PROVINCEID_");
		columnMap.put("city", "CITY_");
		columnMap.put("cityId", "CITYID_");
		columnMap.put("area", "AREA_");
		columnMap.put("areaId", "AREAID_");
		columnMap.put("goodsId", "GOODSID_");
		columnMap.put("goodsName", "GOODSNAME_");
		columnMap.put("goodsNodeId", "GOODSNODEID_");
		columnMap.put("price", "PRICE_");
		columnMap.put("year", "YEAR_");
		columnMap.put("month", "MONTH_");
		columnMap.put("fullDay", "FULLDAY_");
		columnMap.put("createTime", "CREATETIME_");

		javaTypeMap.put("id", "Long");
		javaTypeMap.put("province", "String");
		javaTypeMap.put("provinceId", "Long");
		javaTypeMap.put("city", "String");
		javaTypeMap.put("cityId", "Long");
		javaTypeMap.put("area", "String");
		javaTypeMap.put("areaId", "Long");
		javaTypeMap.put("goodsId", "Long");
		javaTypeMap.put("goodsName", "String");
		javaTypeMap.put("goodsNodeId", "Long");
		javaTypeMap.put("price", "Double");
		javaTypeMap.put("year", "Integer");
		javaTypeMap.put("month", "Integer");
		javaTypeMap.put("fullDay", "Integer");
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
		tableDefinition.setName("GoodsPriceAvg");

		ColumnDefinition idColumn = new ColumnDefinition();
		idColumn.setName("id");
		idColumn.setColumnName("ID_");
		idColumn.setJavaType("Long");
		tableDefinition.setIdColumn(idColumn);

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

		ColumnDefinition goodsId = new ColumnDefinition();
		goodsId.setName("goodsId");
		goodsId.setColumnName("GOODSID_");
		goodsId.setJavaType("Long");
		tableDefinition.addColumn(goodsId);

		ColumnDefinition goodsName = new ColumnDefinition();
		goodsName.setName("goodsName");
		goodsName.setColumnName("GOODSNAME_");
		goodsName.setJavaType("String");
		goodsName.setLength(200);
		tableDefinition.addColumn(goodsName);

		ColumnDefinition goodsNodeId = new ColumnDefinition();
		goodsNodeId.setName("goodsNodeId");
		goodsNodeId.setColumnName("GOODSNODEID_");
		goodsNodeId.setJavaType("Long");
		tableDefinition.addColumn(goodsNodeId);

		ColumnDefinition price = new ColumnDefinition();
		price.setName("price");
		price.setColumnName("PRICE_");
		price.setJavaType("Double");
		tableDefinition.addColumn(price);

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

	private GoodsPriceAvgDomainFactory() {

	}

}
