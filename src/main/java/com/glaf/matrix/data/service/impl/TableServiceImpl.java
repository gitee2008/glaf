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

@Service("tableService")
@Transactional(readOnly = true)
public class TableServiceImpl implements ITableService {
	protected final static Log logger = LogFactory.getLog(TableServiceImpl.class);

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected SqlSession sqlSession;

	protected TableColumnMapper columnDefinitionMapper;

	protected SysTableMapper tableDefinitionMapper;

	public TableServiceImpl() {

	}

	public int count(SysTableQuery query) {
		if (StringUtils.isEmpty(query.getType())) {
			throw new RuntimeException(" type is null ");
		}
		return tableDefinitionMapper.getSysTableCount(query);
	}

	@Transactional
	public void deleteColumn(String columnId) {
		columnDefinitionMapper.deleteTableColumnById(columnId);
	}

	/**
	 * 删除表定义
	 * 
	 * @param tableId
	 */
	@Transactional
	public void deleteTable(String tableId) {
		columnDefinitionMapper.deleteTableColumnByTableId(tableId);
		tableDefinitionMapper.deleteSysTableById(tableId);
	}

	public List<SysTable> getAllSysTables() {
		SysTableQuery query = new SysTableQuery();
		query.systemFlag("U");
		return tableDefinitionMapper.getSysTables(query);
	}

	public SysTable getSysTableById(String tableId) {
		if (tableId == null) {
			return null;
		}
		SysTable tableDefinition = tableDefinitionMapper.getSysTableById(tableId);
		if (tableDefinition != null) {
			List<TableColumn> columns = columnDefinitionMapper.getTableColumnsByTableId(tableId);
			if (columns != null && !columns.isEmpty()) {
				tableDefinition.setColumns(columns);
				for (TableColumn column : columns) {
					if (column.isPrimaryKey()) {
						logger.debug("##PrimaryKey:" + column.toJsonObject().toJSONString());
						tableDefinition.setIdColumn(column);
						columns.remove(column);
						break;
					}
				}
			}
		}

		return tableDefinition;
	}

	public SysTable getSysTableByTableName(String tableName) {
		if (tableName == null) {
			return null;
		}
		SysTable tableDefinition = tableDefinitionMapper.getSysTableByTableName(tableName);
		if (tableDefinition != null) {
			List<TableColumn> columns = columnDefinitionMapper.getTableColumnsByTableId(tableDefinition.getTableId());
			if (columns != null && !columns.isEmpty()) {
				tableDefinition.setColumns(columns);
				for (TableColumn column : columns) {
					if (column.isPrimaryKey()) {
						logger.debug("##PrimaryKey:" + column.toJsonObject().toJSONString());
						tableDefinition.setIdColumn(column);
						columns.remove(column);
						break;
					}
				}
			}
		}

		return tableDefinition;
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
		return tableDefinitionMapper.getSysTableCount(query);
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
		return columnDefinitionMapper.getTableColumnById(columnId);
	}

	public int getTableColumnCount(TableColumnQuery query) {
		return columnDefinitionMapper.getTableColumnCount(query);
	}

	public List<SysTable> getTableColumns(SysTableQuery query) {
		List<SysTable> list = tableDefinitionMapper.getTableColumns(query);
		return list;
	}

	public List<TableColumn> getTableColumnsByTableId(String tableId) {
		SysTable tableDefinition = this.getSysTableById(tableId);
		if (tableDefinition != null) {
			return tableDefinition.getColumns();
		}
		return null;
	}

	public List<TableColumn> getTableColumnsByTableName(String tableName) {
		return columnDefinitionMapper.getTableColumnsByTableName(tableName);
	}

	public List<TableColumn> getTableColumnsByTargetId(String targetId) {
		return columnDefinitionMapper.getTableColumnsByTargetId(targetId);
	}

	@Transactional
	public void insertColumns(String tableName, List<TableColumn> columns) {
		tableName = tableName.toUpperCase();
		SysTable table = tableDefinitionMapper.getSysTableByTableName(tableName);
		if (table != null) {
			for (TableColumn column : columns) {
				String id = column.getId();
				if (id == null || columnDefinitionMapper.getTableColumnById(id) == null) {
					column.setId(UUID32.getUUID());
					column.setSystemFlag("1");
					column.setTableName(tableName);
					column.setTableId(table.getTableId());
					columnDefinitionMapper.insertTableColumn(column);
				}
			}
		}
	}

	public List<SysTable> list(SysTableQuery query) {
		if (StringUtils.isEmpty(query.getType())) {
			throw new RuntimeException(" type is null ");
		}
		List<SysTable> list = tableDefinitionMapper.getSysTables(query);
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
	public void save(SysTable tableDefinition) {
		if (StringUtils.isEmpty(tableDefinition.getType())) {
			throw new RuntimeException(" type is null ");
		}
		String tableName = tableDefinition.getTableName();
		tableName = tableName.toUpperCase();
		tableDefinition.setTableName(tableName);
		SysTable table = tableDefinitionMapper.getSysTableByTableName(tableDefinition.getTableName());
		if (table == null) {
			tableDefinition.setTableId(UUID32.getUUID());
			tableDefinition.setCreateTime(new Date());
			tableDefinition.setRevision(1);
			tableDefinitionMapper.insertSysTable(tableDefinition);
		} else {
			tableDefinition.setRevision(tableDefinition.getRevision() + 1);
			tableDefinitionMapper.updateSysTable(tableDefinition);
		}

		if (tableDefinition.getColumns() != null && !tableDefinition.getColumns().isEmpty()) {
			for (TableColumn column : tableDefinition.getColumns()) {
				String id = column.getId();
				if (id == null || columnDefinitionMapper.getTableColumnById(id) == null) {
					column.setId(UUID32.getUUID());
					column.setTableName(tableName);
					column.setTableId(tableDefinition.getTableId());
					if ("SYS".equals(tableDefinition.getType())) {
						column.setSystemFlag("1");
					}
					columnDefinitionMapper.insertTableColumn(column);
				} else {
					columnDefinitionMapper.updateTableColumn(column);
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
	public void saveAs(SysTable table, List<TableColumn> columns) {
		table.setTableId(UUID32.getUUID());
		tableDefinitionMapper.insertSysTable(table);
		for (TableColumn column : columns) {
			column.setId(UUID32.getUUID());
			column.setSystemFlag("Y");
			column.setTableName(table.getTableName());
			column.setTableId(table.getTableId());
			column.setColumnName(
					(table.getTableName() + "_user" + idGenerator.getNextId(table.getTableName())).toLowerCase());
			columnDefinitionMapper.insertTableColumn(column);
		}
	}

	@Transactional
	public void saveColumn(String tableName, TableColumn columnDefinition) {
		tableName = tableName.toUpperCase();
		SysTable tableDefinition = getSysTableByTableName(tableName);
		if (tableDefinition != null) {
			List<TableColumn> columns = tableDefinition.getColumns();
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
						columnDefinitionMapper.updateTableColumn(column);
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
				columnDefinition.setTableId(tableDefinition.getTableId());
				columnDefinition.setOrdinal(++sortNo);
				columnDefinitionMapper.insertTableColumn(columnDefinition);
			}
		}
	}

	@Transactional
	public void saveColumns(String targetId, List<TableColumn> columns) {
		columnDefinitionMapper.deleteTableColumnByTargetId(targetId);
		if (columns != null && !columns.isEmpty()) {
			for (TableColumn col : columns) {
				if (StringUtils.isEmpty(col.getId())) {
					col.setId(UUID32.getUUID());
				}
				col.setTargetId(targetId);
				columnDefinitionMapper.insertTableColumn(col);
			}
		}
	}

	@Transactional
	public void saveSystemTable(String tableName, List<TableColumn> columns) {
		tableName = tableName.toUpperCase();
		SysTable table = tableDefinitionMapper.getSysTableByTableName(tableName);
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
			tableDefinitionMapper.insertSysTable(table);
		} else {
			tableDefinitionMapper.updateSysTable(table);
		}
		columnDefinitionMapper.deleteTableColumnByTableId(table.getTableId());
		for (TableColumn column : columns) {
			column.setId(UUID32.getUUID());
			column.setSystemFlag("Y");
			column.setTableName(tableName);
			column.setTableId(table.getTableId());
			columnDefinitionMapper.insertTableColumn(column);
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
	public void setSysTableMapper(SysTableMapper tableDefinitionMapper) {
		this.tableDefinitionMapper = tableDefinitionMapper;
	}

	@javax.annotation.Resource
	public void setTableColumnMapper(TableColumnMapper columnDefinitionMapper) {
		this.columnDefinitionMapper = columnDefinitionMapper;
	}

	/**
	 * 保存字段信息
	 * 
	 * @param columnDefinition
	 */
	@Transactional
	public void updateColumn(TableColumn column) {
		columnDefinitionMapper.updateTableColumn(column);
	}

}