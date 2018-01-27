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

package com.glaf.matrix.transform.service.impl;

import java.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.id.*;
import com.glaf.core.dao.*;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.util.*;

import com.glaf.matrix.transform.mapper.*;
import com.glaf.matrix.transform.domain.*;
import com.glaf.matrix.transform.query.*;
import com.glaf.matrix.transform.service.TableTransformService;

@Service("tableTransformService")
@Transactional(readOnly = true)
public class TableTransformServiceImpl implements TableTransformService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected ColumnTransformMapper columnTransformMapper;

	protected TableTransformMapper tableTransformMapper;

	public TableTransformServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<TableTransform> list) {
		for (TableTransform tableTransform : list) {
			if (StringUtils.isEmpty(tableTransform.getTransformId())) {
				tableTransform.setTransformId(idGenerator.getNextId("SYS_TABLE_TRANSFORM"));
			}
		}
		if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
			tableTransformMapper.bulkInsertTableTransform_oracle(list);
		} else {
			tableTransformMapper.bulkInsertTableTransform(list);
		}
	}

	public int count(TableTransformQuery query) {
		return tableTransformMapper.getTableTransformCount(query);
	}

	@Transactional
	public void deleteById(String transformId) {
		if (transformId != null) {
			columnTransformMapper.deleteColumnTransformsByTransformId(transformId);
			tableTransformMapper.deleteTableTransformById(transformId);
		}
	}

	public TableTransform getTableTransform(String transformId) {
		if (transformId == null) {
			return null;
		}
		TableTransform tableTransform = tableTransformMapper.getTableTransformById(transformId);
		if (tableTransform != null) {
			List<ColumnTransform> columns = columnTransformMapper
					.getColumnTransformsByTransformId(transformId);
			tableTransform.setColumns(columns);
		}
		return tableTransform;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getTableTransformCountByQueryCriteria(TableTransformQuery query) {
		return tableTransformMapper.getTableTransformCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<TableTransform> getTableTransformsByQueryCriteria(int start, int pageSize,
			TableTransformQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<TableTransform> rows = sqlSessionTemplate.selectList("getTableTransforms", query, rowBounds);
		return rows;
	}

	public List<TableTransform> list(TableTransformQuery query) {
		List<TableTransform> list = tableTransformMapper.getTableTransforms(query);
		return list;
	}

	@Transactional
	public void resetAllTableTransformStatus() {
		tableTransformMapper.resetAllTableTransformStatus();
	}

	@Transactional
	public void save(TableTransform tableTransform) {
		TableTransform bean = this.getTableTransform(tableTransform.getTransformId());
		if (bean == null) {
			if (StringUtils.isEmpty(tableTransform.getTransformId())) {
				tableTransform.setTransformId(idGenerator.getNextId("SYS_TABLE_TRANSFORM"));
			}
			tableTransform.setCreateTime(new Date());
			tableTransform.setDeleteFlag(0);
			logger.debug(tableTransform.toJsonObject().toJSONString());
			tableTransformMapper.insertTableTransform(tableTransform);
		} else {
			tableTransformMapper.updateTableTransform(tableTransform);
		}
	}

	@javax.annotation.Resource
	public void setColumnTransformMapper(ColumnTransformMapper columnTransformMapper) {
		this.columnTransformMapper = columnTransformMapper;
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
	public void setTableTransformMapper(TableTransformMapper tableTransformMapper) {
		this.tableTransformMapper = tableTransformMapper;
	}

	@Transactional
	public void updateTableTransformStatus(TableTransform model) {
		model.setTransformTime(new java.util.Date());
		tableTransformMapper.updateTableTransformStatus(model);
	}

}
