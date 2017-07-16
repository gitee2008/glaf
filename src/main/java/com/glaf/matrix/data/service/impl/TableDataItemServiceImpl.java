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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.util.DBUtils;
import com.glaf.matrix.data.domain.TableDataItem;
import com.glaf.matrix.data.mapper.TableDataItemMapper;
import com.glaf.matrix.data.query.TableDataItemQuery;
import com.glaf.matrix.data.service.TableDataItemService;

@Service("tableDataItemService")
@Transactional(readOnly = true)
public class TableDataItemServiceImpl implements TableDataItemService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected TableDataItemMapper tableDataItemMapper;

	public TableDataItemServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<TableDataItem> list) {
		for (TableDataItem tableDataItem : list) {
			if (StringUtils.isEmpty(tableDataItem.getId())) {
				tableDataItem.setId(idGenerator.getNextId("SYS_TABLE_DATA_ITEM"));
				tableDataItem.setCreateTime(new Date());
			}
		}

		int batch_size = 200;
		List<TableDataItem> rows = new ArrayList<TableDataItem>(batch_size);

		for (TableDataItem bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % 100 == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					tableDataItemMapper.bulkInsertTableDataItem_oracle(rows);
				} else {
					tableDataItemMapper.bulkInsertTableDataItem(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				tableDataItemMapper.bulkInsertTableDataItem_oracle(rows);
			} else {
				tableDataItemMapper.bulkInsertTableDataItem(rows);
			}
			rows.clear();
		}
	}

	public int count(TableDataItemQuery query) {
		return tableDataItemMapper.getTableDataItemCount(query);
	}

	@Transactional
	public void deleteById(String id) {
		if (id != null) {
			tableDataItemMapper.deleteTableDataItemById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (String id : ids) {
				tableDataItemMapper.deleteTableDataItemById(id);
			}
		}
	}

	public TableDataItem getTableDataItem(String id) {
		if (id == null) {
			return null;
		}
		TableDataItem tableDataItem = tableDataItemMapper.getTableDataItemById(id);
		return tableDataItem;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getTableDataItemCountByQueryCriteria(TableDataItemQuery query) {
		return tableDataItemMapper.getTableDataItemCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<TableDataItem> getTableDataItemsByQueryCriteria(int start, int pageSize, TableDataItemQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<TableDataItem> rows = sqlSessionTemplate.selectList("getTableDataItems", query, rowBounds);
		return rows;
	}

	public List<TableDataItem> list(TableDataItemQuery query) {
		List<TableDataItem> list = tableDataItemMapper.getTableDataItems(query);
		return list;
	}

	@Transactional
	public void save(TableDataItem tableDataItem) {
		if (StringUtils.isEmpty(tableDataItem.getId())) {
			tableDataItem.setId(idGenerator.getNextId("SYS_TABLE_DATA_ITEM"));
			tableDataItem.setCreateTime(new Date());
			tableDataItemMapper.insertTableDataItem(tableDataItem);
		} else {
			tableDataItem.setUpdateTime(new Date());
			tableDataItemMapper.updateTableDataItem(tableDataItem);
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
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@javax.annotation.Resource
	public void setTableDataItemMapper(TableDataItemMapper tableDataItemMapper) {
		this.tableDataItemMapper = tableDataItemMapper;
	}

}
