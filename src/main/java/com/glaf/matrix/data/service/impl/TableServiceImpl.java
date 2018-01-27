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

package com.glaf.matrix.data.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.glaf.core.cache.CacheFactory;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.util.UUID32;
import com.glaf.matrix.data.domain.SysTable;
import com.glaf.matrix.data.domain.TableColumn;
import com.glaf.matrix.data.mapper.SysTableMapper;
import com.glaf.matrix.data.mapper.TableColumnMapper;
import com.glaf.matrix.data.query.SysTableQuery;
import com.glaf.matrix.data.query.TableColumnQuery;
import com.glaf.matrix.data.service.ITableService;
import com.glaf.matrix.data.util.SysTableJsonFactory;

@Service("tableService")
@Transactional(readOnly = true)
public class TableServiceImpl implements ITableService {
	protected final static Log logger = LogFactory.getLog(TableServiceImpl.class);

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected SqlSession sqlSession;

	protected TableColumnMapper tableColumnMapper;

	protected SysTableMapper sysTableMapper;

	public TableServiceImpl() {

	}

	public int count(SysTableQuery query) {
		if (StringUtils.isEmpty(query.getType())) {
			throw new RuntimeException(" type is null ");
		}
		return sysTableMapper.getSysTableCount(query);
	}

	@Transactional
	public void deleteColumn(String columnId) {
		CacheFactory.clear("sys_table");
		tableColumnMapper.deleteTableColumnById(columnId);
	}

	/**
	 * 删除表定义
	 * 
	 * @param tableId
	 */
	@Transactional
	public void deleteTable(String tableId) {
		CacheFactory.clear("sys_table");
		tableColumnMapper.deleteTableColumnByTableId(tableId);
		sysTableMapper.deleteSysTableById(tableId);
	}

	public List<SysTable> getAllSysTables() {
		SysTableQuery query = new SysTableQuery();
		query.systemFlag("U");
		return sysTableMapper.getSysTables(query);
	}

	public SysTable getSysTableById(String tableId) {
		if (tableId == null) {
			return null;
		}
		String cacheKey = "sys_table_" + tableId;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString("sys_table", cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					com.alibaba.fastjson.JSONObject json = JSON.parseObject(text);
					SysTable model = SysTableJsonFactory.jsonToObject(json);
					if (model != null) {
						return model;
					}
				} catch (Exception ex) {
				}
			}
		}

		SysTable sysTable = sysTableMapper.getSysTableById(tableId);
		if (sysTable != null) {
			List<TableColumn> columns = tableColumnMapper.getTableColumnsByTableId(tableId);
			if (columns != null && !columns.isEmpty()) {
				sysTable.setColumns(columns);
				for (TableColumn column : columns) {
					if (column.isPrimaryKey()) {
						logger.debug("##PrimaryKey:" + column.toJsonObject().toJSONString());
						sysTable.setIdColumn(column);
						columns.remove(column);
						break;
					}
				}
			}
			CacheFactory.put("sys_table", cacheKey, sysTable.toJsonObject().toJSONString());
		}

		return sysTable;
	}

	public SysTable getSysTableByTableName(String tableName) {
		if (tableName == null) {
			return null;
		}
		String cacheKey = "sys_table_" + tableName;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString("sys_table", cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					com.alibaba.fastjson.JSONObject json = JSON.parseObject(text);
					SysTable model = SysTableJsonFactory.jsonToObject(json);
					if (model != null) {
						return model;
					}
				} catch (Exception ex) {
				}
			}
		}

		SysTable sysTable = sysTableMapper.getSysTableByTableName(tableName);
		if (sysTable != null) {
			List<TableColumn> columns = tableColumnMapper.getTableColumnsByTableId(sysTable.getTableId());
			if (columns != null && !columns.isEmpty()) {
				sysTable.setColumns(columns);
				for (TableColumn column : columns) {
					if (column.isPrimaryKey()) {
						logger.debug("##PrimaryKey:" + column.toJsonObject().toJSONString());
						sysTable.setIdColumn(column);
						columns.remove(column);
						break;
					}
				}
			}
			CacheFactory.put("sys_table", cacheKey, sysTable.toJsonObject().toJSONString());
		}

		return sysTable;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getSysTableCountByQueryCriteria(SysTableQuery query) {
		if (StringUtils.isEmpty(query.getType())) {
			throw new RuntimeException(" type is null ");
		}
		return sysTableMapper.getSysTableCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<SysTable> getSysTablesByQueryCriteria(int start, int pageSize, SysTableQuery query) {
		if (StringUtils.isEmpty(query.getType())) {
			throw new RuntimeException(" type is null ");
		}
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<SysTable> rows = sqlSession.selectList("getSysTables", query, rowBounds);
		return rows;
	}

	public TableColumn getTableColumn(String columnId) {
		return tableColumnMapper.getTableColumnById(columnId);
	}

	public int getTableColumnCount(TableColumnQuery query) {
		return tableColumnMapper.getTableColumnCount(query);
	}

	public List<TableColumn> getTableColumns(TableColumnQuery query) {
		 return tableColumnMapper.getTableColumns(query);
	}

	public List<TableColumn> getTableColumnsByTableId(String tableId) {
		SysTable sysTable = this.getSysTableById(tableId);
		if (sysTable != null) {
			return sysTable.getColumns();
		}
		return null;
	}

	public List<TableColumn> getTableColumnsByTableName(String tableName) {
		return tableColumnMapper.getTableColumnsByTableName(tableName);
	}

	public List<TableColumn> getTableColumnsByTargetId(String targetId) {
		return tableColumnMapper.getTableColumnsByTargetId(targetId);
	}

	@Transactional
	public void insertColumns(String tableName, List<TableColumn> columns) {
		tableName = tableName.toUpperCase();
		SysTable sysTable = sysTableMapper.getSysTableByTableName(tableName);
		if (sysTable != null) {
			String cacheKey = "sys_table_" + sysTable.getTableId();
			CacheFactory.remove("sys_table", cacheKey);
			cacheKey = "sys_table_" + tableName;
			CacheFactory.remove("sys_table", cacheKey);

			for (TableColumn column : columns) {
				String id = column.getId();
				if (id == null || tableColumnMapper.getTableColumnById(id) == null) {
					column.setId(UUID32.getUUID());
					column.setSystemFlag("1");
					column.setTableName(tableName);
					column.setTableId(sysTable.getTableId());
					tableColumnMapper.insertTableColumn(column);
				}
			}
		}
	}

	public List<SysTable> list(SysTableQuery query) {
		if (StringUtils.isEmpty(query.getType())) {
			throw new RuntimeException(" type is null ");
		}
		List<SysTable> list = sysTableMapper.getSysTables(query);
		return list;
	}

	/**
	 * 获取某个类型的表名称
	 * 
	 * @param type
	 * @return
	 */
	@Transactional
	public String nextTableName(String type) {
		Long nextId = idGenerator.nextId(type);
		return type.toUpperCase() + nextId;
	}

	@Transactional
	public void save(SysTable sysTable) {
		if (StringUtils.isEmpty(sysTable.getType())) {
			throw new RuntimeException(" type is null ");
		}
		String tableName = sysTable.getTableName();
		tableName = tableName.toUpperCase();
		sysTable.setTableName(tableName);
		SysTable table = sysTableMapper.getSysTableByTableName(sysTable.getTableName());
		if (table == null) {
			sysTable.setTableId(UUID32.getUUID());
			sysTable.setCreateTime(new Date());
			sysTable.setRevision(1);
			sysTableMapper.insertSysTable(sysTable);
		} else {
			String cacheKey = "sys_table_" + sysTable.getTableId();
			CacheFactory.remove("sys_table", cacheKey);
			cacheKey = "sys_table_" + tableName;
			CacheFactory.remove("sys_table", cacheKey);

			sysTable.setRevision(sysTable.getRevision() + 1);
			sysTableMapper.updateSysTable(sysTable);
		}

		if (sysTable.getColumns() != null && !sysTable.getColumns().isEmpty()) {
			for (TableColumn column : sysTable.getColumns()) {
				String id = column.getId();
				if (id == null || tableColumnMapper.getTableColumnById(id) == null) {
					column.setId(UUID32.getUUID());
					column.setTableName(tableName);
					column.setTableId(sysTable.getTableId());
					if ("SYS".equals(sysTable.getType())) {
						column.setSystemFlag("1");
					}
					tableColumnMapper.insertTableColumn(column);
				} else {
					tableColumnMapper.updateTableColumn(column);
				}
			}
		}
	}

	/**
	 * 保存表定义信息
	 * 
	 * @return
	 */
	@Transactional
	public void saveAs(SysTable sysTable, List<TableColumn> columns) {
		sysTable.setTableId(UUID32.getUUID());
		sysTableMapper.insertSysTable(sysTable);
		for (TableColumn column : columns) {
			column.setId(UUID32.getUUID());
			column.setSystemFlag("Y");
			column.setTableName(sysTable.getTableName());
			column.setTableId(sysTable.getTableId());
			column.setColumnName(
					(sysTable.getTableName() + "_user" + idGenerator.getNextId(sysTable.getTableName())).toLowerCase());
			tableColumnMapper.insertTableColumn(column);
		}
	}

	@Transactional
	public void saveColumn(String tableName, TableColumn columnDefinition) {
		tableName = tableName.toUpperCase();
		SysTable sysTable = getSysTableByTableName(tableName);
		if (sysTable != null) {
			String cacheKey = "sys_table_" + sysTable.getTableId();
			CacheFactory.remove("sys_table", cacheKey);
			cacheKey = "sys_table_" + tableName;
			CacheFactory.remove("sys_table", cacheKey);

			List<TableColumn> columns = sysTable.getColumns();
			boolean exists = false;
			int sortNo = 1;
			if (columns != null && !columns.isEmpty()) {
				sortNo = columns.size();
				for (TableColumn column : columns) {
					if (StringUtils.equalsIgnoreCase(column.getColumnName(), columnDefinition.getColumnName())) {
						column.setValueExpression(columnDefinition.getValueExpression());
						column.setFormula(columnDefinition.getFormula());
						column.setName(columnDefinition.getName());
						column.setTitle(columnDefinition.getTitle());
						column.setEnglishTitle(columnDefinition.getEnglishTitle());
						column.setHeight(columnDefinition.getHeight());
						column.setLength(columnDefinition.getLength());
						column.setPrecision(columnDefinition.getPrecision());
						column.setScale(columnDefinition.getScale());
						column.setFrozen(columnDefinition.isFrozen());
						column.setNullable(columnDefinition.isNullable());
						column.setSortable(columnDefinition.isSortable());
						column.setDisplayType(columnDefinition.getDisplayType());
						column.setDiscriminator(columnDefinition.getDiscriminator());
						column.setFormatter(columnDefinition.getFormatter());
						column.setLink(columnDefinition.getLink());
						column.setOrdinal(columnDefinition.getOrdinal());
						column.setRegex(columnDefinition.getRegex());
						column.setSummaryExpr(columnDefinition.getSummaryExpr());
						column.setSummaryType(columnDefinition.getSummaryType());
						column.setExportFlag(columnDefinition.getExportFlag());
						tableColumnMapper.updateTableColumn(column);
						exists = true;
						break;
					}
				}
			}

			if (!exists) {
				if (StringUtils.isEmpty(columnDefinition.getColumnName())) {
					columnDefinition
							.setColumnName((tableName + "_user" + idGenerator.getNextId(tableName)).toLowerCase());
				}
				columnDefinition.setId(UUID32.getUUID());
				columnDefinition.setTableName(tableName);
				columnDefinition.setTableId(sysTable.getTableId());
				columnDefinition.setOrdinal(++sortNo);
				tableColumnMapper.insertTableColumn(columnDefinition);
			}
		}
	}

	@Transactional
	public void saveColumns(String targetId, List<TableColumn> columns) {
		CacheFactory.clear("sys_table");
		tableColumnMapper.deleteTableColumnByTargetId(targetId);
		if (columns != null && !columns.isEmpty()) {
			for (TableColumn col : columns) {
				if (StringUtils.isEmpty(col.getId())) {
					col.setId(UUID32.getUUID());
				}
				col.setTargetId(targetId);
				tableColumnMapper.insertTableColumn(col);
			}
		}
	}
	
	@Transactional
	public void saveTargetColumn(String targetId, TableColumn column) {
		CacheFactory.clear("sys_table");
		if (StringUtils.isNotEmpty(column.getId())) {
			TableColumn model = this.getTableColumn(column.getId());
			if (model == null) {
				column.setTargetId(targetId);
				tableColumnMapper.insertTableColumn(column);
			}
		}
	}

	@Transactional
	public void saveSystemTable(String tableName, List<TableColumn> columns) {
		tableName = tableName.toUpperCase();
		SysTable table = sysTableMapper.getSysTableByTableName(tableName);
		if (table == null) {
			table = new SysTable();
			table.setType("SYS");
			table.setSystemFlag("Y");
			table.setTableName(tableName.toUpperCase());
			table.setRevision(1);
			table.setDeleteFlag(0);
			table.setLocked(0);
			table.setEnglishTitle(tableName);
			table.setCreateBy("system");
			table.setCreateTime(new Date());
			table.setTableId(UUID32.getUUID());
			sysTableMapper.insertSysTable(table);
		} else {

			String cacheKey = "sys_table_" + table.getTableId();
			CacheFactory.remove("sys_table", cacheKey);
			cacheKey = "sys_table_" + tableName;
			CacheFactory.remove("sys_table", cacheKey);

			sysTableMapper.updateSysTable(table);
		}
		tableColumnMapper.deleteTableColumnByTableId(table.getTableId());
		for (TableColumn column : columns) {
			column.setId(UUID32.getUUID());
			column.setSystemFlag("Y");
			column.setTableName(tableName);
			column.setTableId(table.getTableId());
			tableColumnMapper.insertTableColumn(column);
		}
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource
	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	@javax.annotation.Resource
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@javax.annotation.Resource
	public void setSysTableMapper(SysTableMapper sysTableMapper) {
		this.sysTableMapper = sysTableMapper;
	}

	@javax.annotation.Resource
	public void setTableColumnMapper(TableColumnMapper tableColumnMapper) {
		this.tableColumnMapper = tableColumnMapper;
	}

	/**
	 * 保存字段信息
	 * 
	 * @param columnDefinition
	 */
	@Transactional
	public void updateColumn(TableColumn column) {
		CacheFactory.clear("sys_table");
		tableColumnMapper.updateTableColumn(column);
	}

}