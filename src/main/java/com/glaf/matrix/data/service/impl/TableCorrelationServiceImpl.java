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
import com.glaf.matrix.data.domain.TableCorrelation;
import com.glaf.matrix.data.mapper.TableCorrelationMapper;
import com.glaf.matrix.data.query.TableCorrelationQuery;
import com.glaf.matrix.data.service.TableCorrelationService;

@Service("tableCorrelationService")
@Transactional(readOnly = true)
public class TableCorrelationServiceImpl implements TableCorrelationService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected TableCorrelationMapper tableCorrelationMapper;

	public TableCorrelationServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<TableCorrelation> list) {
		for (TableCorrelation tableCorrelation : list) {
			if (StringUtils.isEmpty(tableCorrelation.getId())) {
				tableCorrelation.setId(idGenerator.getNextId("SYS_TABLE_CORRELATION"));
				tableCorrelation.setCreateTime(new Date());

			}
		}

		int batch_size = 200;
		List<TableCorrelation> rows = new ArrayList<TableCorrelation>(batch_size);

		for (TableCorrelation bean : list) {
			if (StringUtils.isNotEmpty(bean.getMasterTableName()) && StringUtils.isNotEmpty(bean.getSlaveTableName())) {
				rows.add(bean);
			}
			if (rows.size() > 0 && rows.size() % 100 == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					tableCorrelationMapper.bulkInsertTableCorrelation_oracle(rows);
				} else {
					tableCorrelationMapper.bulkInsertTableCorrelation(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				tableCorrelationMapper.bulkInsertTableCorrelation_oracle(rows);
			} else {
				tableCorrelationMapper.bulkInsertTableCorrelation(rows);
			}
			rows.clear();
		}
	}

	public int count(TableCorrelationQuery query) {
		return tableCorrelationMapper.getTableCorrelationCount(query);
	}

	@Transactional
	public void deleteById(String id) {
		if (id != null) {
			tableCorrelationMapper.deleteTableCorrelationById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (String id : ids) {
				tableCorrelationMapper.deleteTableCorrelationById(id);
			}
		}
	}

	public TableCorrelation getTableCorrelation(String id) {
		if (id == null) {
			return null;
		}
		TableCorrelation tableCorrelation = tableCorrelationMapper.getTableCorrelationById(id);
		return tableCorrelation;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getTableCorrelationCountByQueryCriteria(TableCorrelationQuery query) {
		return tableCorrelationMapper.getTableCorrelationCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<TableCorrelation> getTableCorrelationsByQueryCriteria(int start, int pageSize,
			TableCorrelationQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<TableCorrelation> rows = sqlSessionTemplate.selectList("getTableCorrelations", query, rowBounds);
		return rows;
	}

	public List<TableCorrelation> list(TableCorrelationQuery query) {
		List<TableCorrelation> list = tableCorrelationMapper.getTableCorrelations(query);
		return list;
	}

	@Transactional
	public void save(TableCorrelation tableCorrelation) {
		if (StringUtils.isEmpty(tableCorrelation.getId())) {
			tableCorrelation.setId(idGenerator.getNextId("SYS_TABLE_CORRELATION"));
			tableCorrelation.setCreateTime(new Date());
			if (StringUtils.isNotEmpty(tableCorrelation.getMasterTableName())
					&& StringUtils.isNotEmpty(tableCorrelation.getSlaveTableName())) {
				tableCorrelationMapper.insertTableCorrelation(tableCorrelation);
			}
		} else {
			if (StringUtils.isNotEmpty(tableCorrelation.getMasterTableName())
					&& StringUtils.isNotEmpty(tableCorrelation.getSlaveTableName())) {
				tableCorrelation.setUpdateTime(new Date());
				tableCorrelationMapper.updateTableCorrelation(tableCorrelation);
			}
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
	public void setTableCorrelationMapper(TableCorrelationMapper tableCorrelationMapper) {
		this.tableCorrelationMapper = tableCorrelationMapper;
	}

}
