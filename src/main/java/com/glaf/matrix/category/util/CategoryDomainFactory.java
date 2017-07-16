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

package com.glaf.matrix.category.util;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.domain.TableDefinition;
import com.glaf.core.util.DBUtils;

/**
 * 
 * 实体数据工厂类
 *
 */
public class CategoryDomainFactory {

	public static final String TABLENAME = "SYS_CATEGORY";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("parentId", "PARENTID_");
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
		columnMap.put("subIds", "SUBIDS_");
		columnMap.put("url", "URL_");
		columnMap.put("createBy", "CREATEBY_");
		columnMap.put("createTime", "CREATETIME_");
		columnMap.put("updateBy", "UPDATEBY_");
		columnMap.put("updateTime", "UPDATETIME_");

		javaTypeMap.put("id", "Long");
		javaTypeMap.put("parentId", "Long");
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
		javaTypeMap.put("subIds", "String");
		javaTypeMap.put("url", "String");
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
		tableDefinition.setName("Category");

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

		ColumnDefinition sort = new ColumnDefinition();
		sort.setName("sort");
		sort.setColumnName("SORTNO_");
		sort.setJavaType("Integer");
		tableDefinition.addColumn(sort);

		ColumnDefinition subIds = new ColumnDefinition();
		subIds.setName("subIds");
		subIds.setColumnName("SUBIDS_");
		subIds.setJavaType("String");
		subIds.setLength(500);
		tableDefinition.addColumn(subIds);

		ColumnDefinition url = new ColumnDefinition();
		url.setName("url");
		url.setColumnName("URL_");
		url.setJavaType("String");
		url.setLength(1);
		tableDefinition.addColumn(url);
		
		ColumnDefinition locked = new ColumnDefinition();
		locked.setName("locked");
		locked.setColumnName("SORTNO_");
		locked.setJavaType("Integer");
		tableDefinition.addColumn(locked);

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

	private CategoryDomainFactory() {

	}

}
