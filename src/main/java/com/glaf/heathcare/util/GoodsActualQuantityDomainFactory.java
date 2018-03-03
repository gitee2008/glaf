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

import java.sql.Connection;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.glaf.core.base.DataRequest;
import com.glaf.core.base.DataRequest.FilterDescriptor;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.domain.Database;
import com.glaf.core.domain.TableDefinition;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.JdbcUtils;

/**
 * 
 * 实体数据工厂类
 *
 */
public class GoodsActualQuantityDomainFactory {

	public static final String TABLENAME = "GOODS_ACTUAL_QUANTITY";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("tenantId", "TENANTID_");
		columnMap.put("goodsId", "GOODSID_");
		columnMap.put("goodsName", "GOODSNAME_");
		columnMap.put("goodsNodeId", "GOODSNODEID_");
		columnMap.put("semester", "SEMESTER_");
		columnMap.put("year", "YEAR_");
		columnMap.put("month", "MONTH_");
		columnMap.put("day", "DAY_");
		columnMap.put("week", "WEEK_");
		columnMap.put("fullDay", "FULLDAY_");
		columnMap.put("quantity", "QUANTITY_");
		columnMap.put("price", "PRICE_");
		columnMap.put("totalPrice", "TOTALPRICE_");
		columnMap.put("unit", "UNIT_");
		columnMap.put("usageTime", "USAGETIME_");
		columnMap.put("businessStatus", "BUSINESSSTATUS_");
		columnMap.put("confirmBy", "CONFIRMBY_");
		columnMap.put("confirmTime", "CONFIRMTIME_");
		columnMap.put("createBy", "CREATEBY_");
		columnMap.put("createTime", "CREATETIME_");

		javaTypeMap.put("id", "Long");
		javaTypeMap.put("tenantId", "String");
		javaTypeMap.put("goodsId", "Long");
		javaTypeMap.put("goodsName", "String");
		javaTypeMap.put("goodsNodeId", "Long");
		javaTypeMap.put("semester", "Integer");
		javaTypeMap.put("year", "Integer");
		javaTypeMap.put("month", "Integer");
		javaTypeMap.put("day", "Integer");
		javaTypeMap.put("week", "Integer");
		javaTypeMap.put("fullDay", "Integer");
		javaTypeMap.put("quantity", "Double");
		javaTypeMap.put("price", "Double");
		javaTypeMap.put("totalPrice", "Double");
		javaTypeMap.put("unit", "String");
		javaTypeMap.put("usageTime", "Date");
		javaTypeMap.put("businessStatus", "Integer");
		javaTypeMap.put("confirmBy", "String");
		javaTypeMap.put("confirmTime", "Date");
		javaTypeMap.put("createBy", "String");
		javaTypeMap.put("createTime", "Date");
	}

	public static void alterTables(long databaseId) {
		TableDefinition tableDefinition = null;
		Connection conn = null;
		Statement statement = null;
		Database database = null;
		try {
			if (databaseId > 0) {
				IDatabaseService databaseService = ContextFactory.getBean("databaseService");
				database = databaseService.getDatabaseById(databaseId);
			}

			if (database != null) {
				conn = DBConnectionFactory.getConnection(database.getName());
			} else {
				conn = DBConnectionFactory.getConnection();
			}

			conn.setAutoCommit(false);
			for (int i = 0; i < com.glaf.core.util.Constants.TABLE_PARTITION; i++) {
				tableDefinition = getTableDefinition(TABLENAME + i);
				DBUtils.alterTable(conn, tableDefinition);
			}
			conn.commit();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(statement);
			JdbcUtils.close(conn);
		}
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

	public static void createTables(long databaseId) {
		String sqlStatement = null;
		TableDefinition tableDefinition = null;

		Connection conn = null;
		Statement statement = null;
		Database database = null;
		try {
			if (databaseId > 0) {
				IDatabaseService databaseService = ContextFactory.getBean("databaseService");
				database = databaseService.getDatabaseById(databaseId);
			}

			if (database != null) {
				conn = DBConnectionFactory.getConnection(database.getName());
			} else {
				conn = DBConnectionFactory.getConnection();
			}

			String dbType = DBConnectionFactory.getDatabaseType(conn);
			conn.setAutoCommit(false);
			for (int i = 0; i < com.glaf.core.util.Constants.TABLE_PARTITION; i++) {
				tableDefinition = getTableDefinition(TABLENAME + i);
				sqlStatement = DBUtils.getCreateTableScript(dbType, tableDefinition);
				statement = conn.createStatement();
				statement.executeUpdate(sqlStatement);
				JdbcUtils.close(statement);
			}
			conn.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(statement);
			JdbcUtils.close(conn);
		}
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
		tableDefinition.setName("GoodsActualQuantity");

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

		ColumnDefinition year = new ColumnDefinition();
		year.setName("year");
		year.setColumnName("YEAR_");
		year.setJavaType("Integer");
		tableDefinition.addColumn(year);

		ColumnDefinition semester = new ColumnDefinition();
		semester.setName("semester");
		semester.setColumnName("SEMESTER_");
		semester.setJavaType("Integer");
		tableDefinition.addColumn(semester);

		ColumnDefinition month = new ColumnDefinition();
		month.setName("month");
		month.setColumnName("MONTH_");
		month.setJavaType("Integer");
		tableDefinition.addColumn(month);

		ColumnDefinition day = new ColumnDefinition();
		day.setName("day");
		day.setColumnName("DAY_");
		day.setJavaType("Integer");
		tableDefinition.addColumn(day);

		ColumnDefinition week = new ColumnDefinition();
		week.setName("week");
		week.setColumnName("WEEK_");
		week.setJavaType("Integer");
		tableDefinition.addColumn(week);

		ColumnDefinition fullDay = new ColumnDefinition();
		fullDay.setName("fullDay");
		fullDay.setColumnName("FULLDAY_");
		fullDay.setJavaType("Integer");
		tableDefinition.addColumn(fullDay);

		ColumnDefinition quantity = new ColumnDefinition();
		quantity.setName("quantity");
		quantity.setColumnName("QUANTITY_");
		quantity.setJavaType("Double");
		tableDefinition.addColumn(quantity);

		ColumnDefinition price = new ColumnDefinition();
		price.setName("price");
		price.setColumnName("PRICE_");
		price.setJavaType("Double");
		tableDefinition.addColumn(price);

		ColumnDefinition totalPrice = new ColumnDefinition();
		totalPrice.setName("totalPrice");
		totalPrice.setColumnName("TOTALPRICE_");
		totalPrice.setJavaType("Double");
		tableDefinition.addColumn(totalPrice);

		ColumnDefinition unit = new ColumnDefinition();
		unit.setName("unit");
		unit.setColumnName("UNIT_");
		unit.setJavaType("String");
		unit.setLength(20);
		tableDefinition.addColumn(unit);

		ColumnDefinition usageTime = new ColumnDefinition();
		usageTime.setName("usageTime");
		usageTime.setColumnName("USAGETIME_");
		usageTime.setJavaType("Date");
		tableDefinition.addColumn(usageTime);

		ColumnDefinition businessStatus = new ColumnDefinition();
		businessStatus.setName("businessStatus");
		businessStatus.setColumnName("BUSINESSSTATUS_");
		businessStatus.setJavaType("Integer");
		tableDefinition.addColumn(businessStatus);

		ColumnDefinition confirmBy = new ColumnDefinition();
		confirmBy.setName("confirmBy");
		confirmBy.setColumnName("CONFIRMBY_");
		confirmBy.setJavaType("String");
		confirmBy.setLength(50);
		tableDefinition.addColumn(confirmBy);

		ColumnDefinition confirmTime = new ColumnDefinition();
		confirmTime.setName("confirmTime");
		confirmTime.setColumnName("CONFIRMTIME_");
		confirmTime.setJavaType("Date");
		tableDefinition.addColumn(confirmTime);

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

	private GoodsActualQuantityDomainFactory() {

	}

}
