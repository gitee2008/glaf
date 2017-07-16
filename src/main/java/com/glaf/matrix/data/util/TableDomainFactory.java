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

import org.apache.commons.lang3.StringUtils;

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
public class TableDomainFactory {

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("parentId", "PARENTID_");
		columnMap.put("topId", "TOPID_");// 关联表的编号
		columnMap.put("nodeId", "NODEID_");// 关联到另外的树表的节点编号，即另外一个树表的ID_
		columnMap.put("uuid", "UUID_");
		columnMap.put("name", "NAME_");
		columnMap.put("code", "CODE_");
		columnMap.put("description", "DESC_");
		columnMap.put("discriminator", "DISCRIMINATOR_");
		columnMap.put("icon", "ICON_");
		columnMap.put("iconCls", "ICONCLS_");
		columnMap.put("level", "LEVEL_");
		columnMap.put("treeId", "TREEID_");
		columnMap.put("title", "TITLE_");
		columnMap.put("type", "TYPE_");
		columnMap.put("sort", "SORTNO_");
		columnMap.put("locked", "LOCKED_");
		columnMap.put("organizationId", "ORGANIZATIONID_");
		columnMap.put("gradeId", "GRADEID_");
		columnMap.put("tenantId", "TENANTID_");
		columnMap.put("aggregationKey", "AGGREGATIONKEY_");
		columnMap.put("businessStatus", "BUSINESS_STATUS_");
		columnMap.put("processIntanceId", "PROCESSINTANCEID_");
		columnMap.put("processKey", "PROCESSKEY_");
		columnMap.put("processStatus", "PROCESS_STATUS_");
		columnMap.put("approvalDate", "APPROVALDATE_");
		columnMap.put("approver", "APPROVER_");
		columnMap.put("createBy", "CREATEBY_");
		columnMap.put("createTime", "CREATETIME_");
		columnMap.put("updateBy", "UPDATEBY_");
		columnMap.put("updateTime", "UPDATETIME_");
		columnMap.put("deleteFlag", "DELETEFLAG_");
		columnMap.put("deleteTime", "DELETETIME_");

		javaTypeMap.put("id", "Long");
		javaTypeMap.put("parentId", "Long");
		javaTypeMap.put("topId", "Long");
		javaTypeMap.put("nodeId", "Long");
		javaTypeMap.put("uuid", "String");
		javaTypeMap.put("name", "String");
		javaTypeMap.put("code", "String");
		javaTypeMap.put("description", "String");
		javaTypeMap.put("discriminator", "String");
		javaTypeMap.put("icon", "String");
		javaTypeMap.put("iconCls", "String");
		javaTypeMap.put("level", "Integer");
		javaTypeMap.put("treeId", "String");
		javaTypeMap.put("title", "String");
		javaTypeMap.put("type", "String");
		javaTypeMap.put("sort", "Integer");
		javaTypeMap.put("locked", "Integer");
		javaTypeMap.put("organizationId", "Long");
		javaTypeMap.put("gradeId", "String");
		javaTypeMap.put("tenantId", "String");
		javaTypeMap.put("aggregationKey", "String");
		javaTypeMap.put("businessStatus", "Integer");
		javaTypeMap.put("processIntanceId", "String");
		javaTypeMap.put("processKey", "String");
		javaTypeMap.put("processStatus", "Integer");
		javaTypeMap.put("approvalDate", "Date");
		javaTypeMap.put("approver", "String");
		javaTypeMap.put("createBy", "String");
		javaTypeMap.put("createTime", "Date");
		javaTypeMap.put("updateBy", "String");
		javaTypeMap.put("updateTime", "Date");
		javaTypeMap.put("deleteFlag", "Integer");
		javaTypeMap.put("deleteTime", "Date");
	}

	public static void alterTables(long databaseId, String tableName, String auditFlag,
			List<ColumnDefinition> extendColumns) {
		TableDefinition tableDefinition = getTableDefinition(tableName);
		if (extendColumns != null && !extendColumns.isEmpty()) {
			tableDefinition.getColumns().addAll(extendColumns);
		}
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
				tableDefinition.setTableName(tableName + i);
				DBUtils.alterTable(conn, tableDefinition);
			}
			if (StringUtils.equals(auditFlag, "Y")) {
				TableAuditDomainFactory.alterTables(conn, tableName);
			}
			conn.commit();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(statement);
			JdbcUtils.close(conn);
		}
	}

	public static void createTables(long databaseId, String tableName, String auditFlag,
			List<ColumnDefinition> extendColumns) {
		TableDefinition tableDefinition = getTableDefinition(tableName);
		if (extendColumns != null && !extendColumns.isEmpty()) {
			tableDefinition.getColumns().addAll(extendColumns);
		}
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
				tableDefinition.setTableName(tableName + i);
				sqlStatement = DBUtils.getCreateTableScript(dbType, tableDefinition);
				statement = conn.createStatement();
				statement.executeUpdate(sqlStatement);
				JdbcUtils.close(statement);
			}
			if (StringUtils.equals(auditFlag, "Y")) {
				TableAuditDomainFactory.createTables(conn, tableName);
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

		ColumnDefinition parentId = new ColumnDefinition();
		parentId.setName("parentId");
		parentId.setColumnName("PARENTID_");
		parentId.setJavaType("Long");
		tableDefinition.addColumn(parentId);

		ColumnDefinition topId = new ColumnDefinition();
		topId.setName("topId");
		topId.setColumnName("TOPID_");// 关联表的编号
		topId.setJavaType("Long");
		tableDefinition.addColumn(topId);

		ColumnDefinition nodeId = new ColumnDefinition();
		nodeId.setName("nodeId");
		nodeId.setColumnName("NODEID_");// 关联到另外的树表的节点编号，即另外一个树表的ID_
		nodeId.setJavaType("Long");
		tableDefinition.addColumn(nodeId);

		ColumnDefinition uuid = new ColumnDefinition();
		uuid.setName("uuid");
		uuid.setColumnName("UUID_");
		uuid.setJavaType("String");
		uuid.setLength(50);
		tableDefinition.addColumn(uuid);

		ColumnDefinition name = new ColumnDefinition();
		name.setName("name");
		name.setColumnName("NAME_");
		name.setJavaType("String");
		name.setLength(200);
		tableDefinition.addColumn(name);

		ColumnDefinition code = new ColumnDefinition();
		code.setName("code");
		code.setColumnName("CODE_");
		code.setJavaType("String");
		code.setLength(50);
		tableDefinition.addColumn(code);

		ColumnDefinition description = new ColumnDefinition();
		description.setName("description");
		description.setColumnName("DESC_");
		description.setJavaType("String");
		description.setLength(500);
		tableDefinition.addColumn(description);

		ColumnDefinition discriminator = new ColumnDefinition();
		discriminator.setName("discriminator");
		discriminator.setColumnName("DISCRIMINATOR_");
		discriminator.setJavaType("String");
		discriminator.setLength(10);
		tableDefinition.addColumn(discriminator);

		ColumnDefinition icon = new ColumnDefinition();
		icon.setName("icon");
		icon.setColumnName("ICON_");
		icon.setJavaType("String");
		icon.setLength(50);
		tableDefinition.addColumn(icon);

		ColumnDefinition iconCls = new ColumnDefinition();
		iconCls.setName("iconCls");
		iconCls.setColumnName("ICONCLS_");
		iconCls.setJavaType("String");
		iconCls.setLength(50);
		tableDefinition.addColumn(iconCls);

		ColumnDefinition level = new ColumnDefinition();
		level.setName("level");
		level.setColumnName("LEVEL_");
		level.setJavaType("Integer");
		tableDefinition.addColumn(level);

		ColumnDefinition treeId = new ColumnDefinition();
		treeId.setName("treeId");
		treeId.setColumnName("TREEID_");
		treeId.setJavaType("String");
		treeId.setLength(500);
		tableDefinition.addColumn(treeId);

		ColumnDefinition title = new ColumnDefinition();
		title.setName("title");
		title.setColumnName("TITLE_");
		title.setJavaType("String");
		title.setLength(100);
		tableDefinition.addColumn(title);

		ColumnDefinition type = new ColumnDefinition();
		type.setName("type");
		type.setColumnName("TYPE_");
		type.setJavaType("String");
		type.setLength(50);
		tableDefinition.addColumn(type);

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

		ColumnDefinition gradeId = new ColumnDefinition();
		gradeId.setName("gradeId");
		gradeId.setColumnName("GRADEID_");// 业务等级编号
		gradeId.setJavaType("String");
		gradeId.setLength(50);
		tableDefinition.addColumn(gradeId);

		ColumnDefinition sort = new ColumnDefinition();
		sort.setName("sort");
		sort.setColumnName("SORTNO_");
		sort.setJavaType("Integer");
		tableDefinition.addColumn(sort);

		ColumnDefinition locked = new ColumnDefinition();
		locked.setName("locked");
		locked.setColumnName("SORTNO_");
		locked.setJavaType("Integer");
		tableDefinition.addColumn(locked);

		ColumnDefinition processIntanceId = new ColumnDefinition();
		processIntanceId.setName("processIntanceId");
		processIntanceId.setColumnName("PROCESSINTANCEID_");// 流程实例编号
		processIntanceId.setJavaType("String");
		processIntanceId.setLength(64);
		tableDefinition.addColumn(processIntanceId);

		ColumnDefinition processKey = new ColumnDefinition();
		processKey.setName("processIntanceId");
		processKey.setColumnName("PROCESSKEY_");// 流程定义编号
		processKey.setJavaType("String");
		processKey.setLength(64);
		tableDefinition.addColumn(processKey);

		ColumnDefinition processStatus = new ColumnDefinition();
		processStatus.setName("processStatus");
		processStatus.setColumnName("PROCESS_STATUS_");// 流程状态
		processStatus.setJavaType("Integer");
		tableDefinition.addColumn(processStatus);

		ColumnDefinition businessStatus = new ColumnDefinition();
		businessStatus.setName("businessStatus");
		businessStatus.setColumnName("BUSINESS_STATUS_");// 业务状态
		businessStatus.setJavaType("Integer");
		tableDefinition.addColumn(businessStatus);

		ColumnDefinition aggregationKey = new ColumnDefinition();
		aggregationKey.setName("aggregationKey");
		aggregationKey.setColumnName("AGGREGATIONKEY_");
		aggregationKey.setJavaType("String");
		aggregationKey.setLength(500);
		tableDefinition.addColumn(aggregationKey);

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

		ColumnDefinition deleteTime = new ColumnDefinition();
		deleteTime.setName("deleteTime");
		deleteTime.setColumnName("DELETETIME_");
		deleteTime.setJavaType("Date");
		tableDefinition.addColumn(deleteTime);

		return tableDefinition;
	}

	public static TableDefinition updateSchema(String tableName, String auditFlag,
			List<ColumnDefinition> extendColumns) {
		TableDefinition tableDefinition = getTableDefinition(tableName);
		if (extendColumns != null && !extendColumns.isEmpty()) {
			tableDefinition.getColumns().addAll(extendColumns);
		}
		if (!DBUtils.tableExists(tableName)) {
			DBUtils.createTable(tableDefinition);
		} else {
			DBUtils.alterTable(tableDefinition);
		}

		if (StringUtils.equals(auditFlag, "Y")) {
			TableAuditDomainFactory.updateSchema(tableName + "_AUDIT", null);
		}

		return tableDefinition;
	}

	private TableDomainFactory() {

	}

}
