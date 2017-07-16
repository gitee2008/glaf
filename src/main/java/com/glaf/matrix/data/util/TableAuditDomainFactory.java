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

package com.glaf.matrix.data.util;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
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
 * 数据表实体工厂类
 *
 */
public class TableAuditDomainFactory {

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("topId", "TOPID_");// 关联表的编号
		columnMap.put("organizationId", "ORGANIZATIONID_");
		columnMap.put("tenantId", "TENANTID_");
		columnMap.put("approval", "APPROVAL_");
		columnMap.put("approvalDate", "APPROVALDATE_");
		columnMap.put("approver", "APPROVER_");
		columnMap.put("content", "CONTENT_");

		javaTypeMap.put("id", "Long");
		javaTypeMap.put("topId", "Long");
		javaTypeMap.put("organizationId", "Long");
		javaTypeMap.put("tenantId", "String");
		javaTypeMap.put("approval", "Integer");
		javaTypeMap.put("approvalDate", "Date");
		javaTypeMap.put("approver", "String");
		javaTypeMap.put("content", "String");

	}

	public static void alterTables(Connection conn, String tableName) {
		TableDefinition tableDefinition = getTableDefinition(tableName);
		Statement statement = null;
		try {
			for (int i = 0; i < com.glaf.core.util.Constants.TABLE_PARTITION; i++) {
				tableDefinition.setTableName(tableName + i + "_AUDIT");
				DBUtils.alterTable(conn, tableDefinition);
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(statement);
		}
	}

	public static void alterTables(long databaseId, String tableName) {
		TableDefinition tableDefinition = getTableDefinition(tableName);
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
				tableDefinition.setTableName(tableName + i + "_AUDIT");
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

	public static void createTables(Connection conn, String tableName) {
		TableDefinition tableDefinition = getTableDefinition(tableName);
		String sqlStatement = null;
		Statement statement = null;
		try {
			String dbType = DBConnectionFactory.getDatabaseType(conn);
			for (int i = 0; i < com.glaf.core.util.Constants.TABLE_PARTITION; i++) {
				tableDefinition.setTableName(tableName + i + "_AUDIT");
				sqlStatement = DBUtils.getCreateTableScript(dbType, tableDefinition);
				statement = conn.createStatement();
				statement.executeUpdate(sqlStatement);
				JdbcUtils.close(statement);
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(statement);
		}
	}

	public static void createTables(long databaseId, String tableName) {
		TableDefinition tableDefinition = getTableDefinition(tableName);
		String sqlStatement = null;
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
				tableDefinition.setTableName(tableName + i + "_AUDIT");
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

	public static TableDefinition getTableDefinition(String tableName) {
		tableName = tableName.toUpperCase();
		TableDefinition tableDefinition = new TableDefinition();
		tableDefinition.setTableName(tableName);

		ColumnDefinition idColumn = new ColumnDefinition();
		idColumn.setName("id");
		idColumn.setColumnName("ID_");
		idColumn.setJavaType("Long");
		tableDefinition.setIdColumn(idColumn);

		ColumnDefinition topId = new ColumnDefinition();
		topId.setName("topId");
		topId.setColumnName("TOPID_");// 关联表的编号
		topId.setJavaType("Long");
		tableDefinition.addColumn(topId);

		ColumnDefinition tenantId = new ColumnDefinition();
		tenantId.setName("tenantId");
		tenantId.setColumnName("TENANTID_");// 租户编号
		tenantId.setJavaType("String");
		tenantId.setLength(50);
		tableDefinition.addColumn(tenantId);

		ColumnDefinition organizationId = new ColumnDefinition();
		organizationId.setName("organizationId");
		organizationId.setColumnName("ORGANIZATIONID_");// 机构编号
		organizationId.setJavaType("Long");
		tableDefinition.addColumn(organizationId);

		ColumnDefinition approval = new ColumnDefinition();
		approval.setName("approval");
		approval.setColumnName("APPROVAL_");
		approval.setJavaType("Integer");
		tableDefinition.addColumn(approval);

		ColumnDefinition approver = new ColumnDefinition();
		approver.setName("approver");
		approver.setColumnName("APPROVER_");
		approver.setJavaType("String");
		approver.setLength(50);
		tableDefinition.addColumn(approver);

		ColumnDefinition approvalDate = new ColumnDefinition();
		approvalDate.setName("approvalDate");
		approvalDate.setColumnName("APPROVALDATE_");
		approvalDate.setJavaType("Date");
		tableDefinition.addColumn(approvalDate);

		ColumnDefinition content = new ColumnDefinition();
		content.setName("content");
		content.setColumnName("CONTENT_");
		content.setJavaType("String");
		content.setLength(4000);
		tableDefinition.addColumn(content);

		return tableDefinition;
	}

	public static TableDefinition updateSchema(String tableName, List<ColumnDefinition> extendColumns) {
		TableDefinition tableDefinition = getTableDefinition(tableName);
		if (extendColumns != null && !extendColumns.isEmpty()) {
			tableDefinition.getColumns().addAll(extendColumns);
		}
		if (!DBUtils.tableExists(tableName)) {
			DBUtils.createTable(tableDefinition);
		} else {
			DBUtils.alterTable(tableDefinition);
		}
		return tableDefinition;
	}

	private TableAuditDomainFactory() {

	}

}
