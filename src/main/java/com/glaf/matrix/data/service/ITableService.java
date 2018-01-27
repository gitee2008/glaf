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

package com.glaf.matrix.data.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.glaf.matrix.data.domain.SysTable;
import com.glaf.matrix.data.domain.TableColumn;
import com.glaf.matrix.data.query.SysTableQuery;
import com.glaf.matrix.data.query.TableColumnQuery;

@Transactional(readOnly = true)
public interface ITableService {

	/**
	 * 删除列定义
	 * 
	 * @param columnId
	 */
	@Transactional
	void deleteColumn(String columnId);

	/**
	 * 删除表定义及管理查询
	 * 
	 * @param tableId
	 */
	@Transactional
	void deleteTable(String tableId);

	/**
	 * 根据查询参数获取记录列表
	 * 
	 * @return
	 */
	List<SysTable> getAllSysTables();

	/**
	 * 根据表Id获取表对象
	 * 
	 * @return
	 */
	SysTable getSysTableById(String tableId);

	SysTable getSysTableByTableName(String tableName);

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	int getSysTableCountByQueryCriteria(SysTableQuery query);

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	List<SysTable> getSysTablesByQueryCriteria(int start, int pageSize, SysTableQuery query);

	/**
	 * 
	 * @param columnId
	 * @return
	 */
	TableColumn getTableColumn(String columnId);

	int getTableColumnCount(TableColumnQuery query);

	List<TableColumn> getTableColumns(TableColumnQuery query);

	List<TableColumn> getTableColumnsByTableId(String tableId);

	List<TableColumn> getTableColumnsByTableName(String tableName);

	List<TableColumn> getTableColumnsByTargetId(String targetId);

	@Transactional
	void insertColumns(String tableName, List<TableColumn> columns);

	/**
	 * 根据查询参数获取记录列表
	 * 
	 * @return
	 */
	List<SysTable> list(SysTableQuery query);

	/**
	 * 获取某个类型的表名称
	 * 
	 * @param type
	 * @return
	 */
	@Transactional
	String nextTableName(String type);

	/**
	 * 保存表定义信息
	 * 
	 * @return
	 */
	@Transactional
	void save(SysTable tableDefinition);

	/**
	 * 保存表定义信息
	 * 
	 * @return
	 */
	@Transactional
	void saveAs(SysTable tableDefinition, List<TableColumn> columns);

	/**
	 * 保存字段信息
	 * 
	 * @param tableName
	 * @param column
	 */
	@Transactional
	void saveColumn(String tableName, TableColumn column);

	/**
	 * 保存列定义信息
	 * 
	 * @param targetId
	 * @param columns
	 */
	@Transactional
	void saveColumns(String targetId, List<TableColumn> columns);

	/**
	 * 保存定义
	 * 
	 * @param tableName
	 * @param rows
	 */
	@Transactional
	void saveSystemTable(String tableName, List<TableColumn> rows);

	@Transactional
	void saveTargetColumn(String targetId, TableColumn column);

	/**
	 * 保存字段信息
	 * 
	 * @param column
	 */
	@Transactional
	void updateColumn(TableColumn column);

}