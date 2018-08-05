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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.domain.Database;
import com.glaf.core.domain.TableDefinition;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.JdbcUtils;

/**
 * 
 * 实体数据工厂类
 * 
 */
public class DataFileDomainFactory {

	public final static String TABLENAME = "DATA_FILE";

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

			tableDefinition = getTableDefinition(TABLENAME);
			DBUtils.alterTable(conn, tableDefinition);

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

			tableDefinition = getTableDefinition(TABLENAME);
			sqlStatement = DBUtils.getCreateTableScript(dbType, tableDefinition);
			statement = conn.createStatement();
			statement.executeUpdate(sqlStatement);
			JdbcUtils.close(statement);

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

	public static void createTables(long databaseId, int year) {
		List<Integer> days = new ArrayList<Integer>();
		int now = DateUtils.getNowYearMonthDay();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());

		StringBuilder buffer = new StringBuilder();
		for (int month = 1; month <= 12; month++) {
			int daysOfMonth = 31;
			switch (month) {
			case 2:
				if (year % 4 == 0) {
					daysOfMonth = 29;
				} else {
					daysOfMonth = 28;
				}
				break;
			case 4:
			case 6:
			case 9:
			case 11:
				daysOfMonth = 30;
				break;
			default:
				break;
			}
			for (int day = 1; day <= daysOfMonth; day++) {
				buffer.delete(0, buffer.length());
				buffer.append(year);
				if (month < 10) {
					buffer.append("0");
				}
				buffer.append(month);
				if (day < 10) {
					buffer.append("0");
				}
				buffer.append(day);
				if (Integer.parseInt(buffer.toString()) >= now) {
					days.add(Integer.parseInt(buffer.toString()));
				}
			}
		}

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
			int size = days.size();
			for (int i = 0; i < size; i++) {
				tableDefinition = getTableDefinition(TABLENAME + days.get(i));
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

	public static TableDefinition getTableDefinition() {
		return getTableDefinition(TABLENAME);
	}

	public static TableDefinition getTableDefinition(String tableName) {
		tableName = tableName.toUpperCase();
		TableDefinition tableDefinition = new TableDefinition();
		tableDefinition.setTableName(tableName);

		ColumnDefinition idColumn = new ColumnDefinition();
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

		ColumnDefinition databaseId = new ColumnDefinition();
		databaseId.setName("databaseId");
		databaseId.setColumnName("DATABASEID_");
		databaseId.setJavaType("Long");
		tableDefinition.addColumn(databaseId);

		ColumnDefinition businessKey = new ColumnDefinition();
		businessKey.setColumnName("BUSINESSKEY_");
		businessKey.setJavaType("String");
		businessKey.setLength(200);
		tableDefinition.addColumn(businessKey);

		ColumnDefinition serviceKey = new ColumnDefinition();
		serviceKey.setColumnName("SERVICEKEY_");
		serviceKey.setJavaType("String");
		serviceKey.setLength(80);
		tableDefinition.addColumn(serviceKey);

		ColumnDefinition masterDataId = new ColumnDefinition();
		masterDataId.setName("masterDataId");
		masterDataId.setColumnName("MASTERDATAID_");
		masterDataId.setJavaType("String");
		masterDataId.setLength(80);
		tableDefinition.addColumn(masterDataId);

		ColumnDefinition deviceId = new ColumnDefinition();
		deviceId.setColumnName("DEVICEID_");
		deviceId.setJavaType("String");
		deviceId.setLength(50);
		tableDefinition.addColumn(deviceId);

		ColumnDefinition name = new ColumnDefinition();
		name.setColumnName("NAME_");
		name.setJavaType("String");
		name.setLength(50);
		tableDefinition.addColumn(name);

		ColumnDefinition type = new ColumnDefinition();
		type.setColumnName("TYPE_");
		type.setJavaType("String");
		type.setLength(50);
		tableDefinition.addColumn(type);

		ColumnDefinition filename = new ColumnDefinition();
		filename.setColumnName("FILENAME_");
		filename.setJavaType("String");
		filename.setLength(500);
		tableDefinition.addColumn(filename);

		ColumnDefinition path = new ColumnDefinition();
		path.setColumnName("PATH_");
		path.setJavaType("String");
		path.setLength(500);
		tableDefinition.addColumn(path);

		ColumnDefinition contentType = new ColumnDefinition();
		contentType.setColumnName("CONTENTTYPE_");
		contentType.setJavaType("String");
		contentType.setLength(80);
		tableDefinition.addColumn(contentType);

		ColumnDefinition objectId = new ColumnDefinition();
		objectId.setColumnName("OBJECTID_");
		objectId.setJavaType("String");
		objectId.setLength(250);
		tableDefinition.addColumn(objectId);

		ColumnDefinition objectValue = new ColumnDefinition();
		objectValue.setColumnName("OBJECTVALUE_");
		objectValue.setJavaType("String");
		objectValue.setLength(250);
		tableDefinition.addColumn(objectValue);

		ColumnDefinition size = new ColumnDefinition();
		size.setColumnName("SIZE_");
		size.setJavaType("Long");
		tableDefinition.addColumn(size);

		ColumnDefinition lastModified = new ColumnDefinition();
		lastModified.setColumnName("LASTMODIFIED_");
		lastModified.setJavaType("Long");
		tableDefinition.addColumn(lastModified);

		ColumnDefinition locked = new ColumnDefinition();
		locked.setColumnName("LOCKED_");
		locked.setJavaType("Integer");
		tableDefinition.addColumn(locked);

		ColumnDefinition status = new ColumnDefinition();
		status.setColumnName("STATUS_");
		status.setJavaType("Integer");
		tableDefinition.addColumn(status);

		ColumnDefinition deleteFlag = new ColumnDefinition();
		deleteFlag.setColumnName("DELETEFLAG_");
		deleteFlag.setJavaType("Integer");
		tableDefinition.addColumn(deleteFlag);

		ColumnDefinition createBy = new ColumnDefinition();
		createBy.setColumnName("CREATEBY_");
		createBy.setJavaType("String");
		createBy.setLength(50);
		tableDefinition.addColumn(createBy);

		ColumnDefinition createDate = new ColumnDefinition();
		createDate.setColumnName("CREATEDATE_");
		createDate.setJavaType("Date");
		tableDefinition.addColumn(createDate);

		ColumnDefinition data = new ColumnDefinition();
		data.setColumnName("DATA_");
		data.setJavaType("Blob");
		tableDefinition.addColumn(data);

		return tableDefinition;
	}

	private DataFileDomainFactory() {

	}

}
