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
public class GoodsStockDomainFactory {

	public static final String TABLENAME = "GOODS_STOCK";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("tenantId", "TENANTID_");
		columnMap.put("goodsId", "GOODSID_");
		columnMap.put("goodsName", "GOODSNAME_");
		columnMap.put("goodsNodeId", "GOODSNODEID_");
		columnMap.put("quantity", "QUANTITY_");
		columnMap.put("unit", "UNIT_");
		columnMap.put("expiryDate", "EXPIRYDATE_");
		columnMap.put("latestOutStockTime", "LATESTOUTSTOCKTIME_");

		javaTypeMap.put("id", "String");
		javaTypeMap.put("tenantId", "String");
		javaTypeMap.put("goodsId", "Long");
		javaTypeMap.put("goodsName", "String");
		javaTypeMap.put("goodsNodeId", "Long");
		javaTypeMap.put("quantity", "Double");
		javaTypeMap.put("unit", "String");
		javaTypeMap.put("expiryDate", "Date");
		javaTypeMap.put("latestOutStockTime", "Date");
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
		tableDefinition.setName("GoodsStock");

		ColumnDefinition idColumn = new ColumnDefinition();
		idColumn.setName("id");
		idColumn.setColumnName("ID_");
		idColumn.setJavaType("String");
		idColumn.setLength(80);
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

		ColumnDefinition quantity = new ColumnDefinition();
		quantity.setName("quantity");
		quantity.setColumnName("QUANTITY_");
		quantity.setJavaType("Double");
		tableDefinition.addColumn(quantity);

		ColumnDefinition unit = new ColumnDefinition();
		unit.setName("unit");
		unit.setColumnName("UNIT_");
		unit.setJavaType("String");
		unit.setLength(20);
		tableDefinition.addColumn(unit);

		ColumnDefinition expiryDate = new ColumnDefinition();
		expiryDate.setName("expiryDate");
		expiryDate.setColumnName("EXPIRYDATE_");
		expiryDate.setJavaType("Date");
		tableDefinition.addColumn(expiryDate);

		ColumnDefinition latestOutStockTime = new ColumnDefinition();
		latestOutStockTime.setName("latestOutStockTime");
		latestOutStockTime.setColumnName("LATESTOUTSTOCKTIME_");
		latestOutStockTime.setJavaType("Date");
		tableDefinition.addColumn(latestOutStockTime);

		return tableDefinition;
	}

	private GoodsStockDomainFactory() {

	}

}
