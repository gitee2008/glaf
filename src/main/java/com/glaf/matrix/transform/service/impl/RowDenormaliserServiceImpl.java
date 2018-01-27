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
import com.glaf.matrix.transform.service.RowDenormaliserService;

@Service("com.glaf.matrix.transform.service.rowDenormaliserService")
@Transactional(readOnly = true)
public class RowDenormaliserServiceImpl implements RowDenormaliserService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected RowDenormaliserMapper rowDenormaliserMapper;

	public RowDenormaliserServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<RowDenormaliser> list) {
		for (RowDenormaliser rowDenormaliser : list) {
			if (StringUtils.isEmpty(rowDenormaliser.getId())) {
				rowDenormaliser.setId(idGenerator.getNextId("SYS_ROW_DENORMALISER"));
			}
		}

		int batch_size = 100;
		List<RowDenormaliser> rows = new ArrayList<RowDenormaliser>(batch_size);

		for (RowDenormaliser bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					rowDenormaliserMapper.bulkInsertRowDenormaliser_oracle(rows);
				} else {
					rowDenormaliserMapper.bulkInsertRowDenormaliser(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				rowDenormaliserMapper.bulkInsertRowDenormaliser_oracle(rows);
			} else {
				rowDenormaliserMapper.bulkInsertRowDenormaliser(rows);
			}
			rows.clear();
		}
	}

	public int count(RowDenormaliserQuery query) {
		return rowDenormaliserMapper.getRowDenormaliserCount(query);
	}

	@Transactional
	public void deleteById(String id) {
		if (id != null) {
			rowDenormaliserMapper.deleteRowDenormaliserById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (String id : ids) {
				rowDenormaliserMapper.deleteRowDenormaliserById(id);
			}
		}
	}

	public RowDenormaliser getRowDenormaliser(String id) {
		if (id == null) {
			return null;
		}
		RowDenormaliser rowDenormaliser = rowDenormaliserMapper.getRowDenormaliserById(id);
		return rowDenormaliser;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getRowDenormaliserCountByQueryCriteria(RowDenormaliserQuery query) {
		return rowDenormaliserMapper.getRowDenormaliserCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<RowDenormaliser> getRowDenormalisersByQueryCriteria(int start, int pageSize,
			RowDenormaliserQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<RowDenormaliser> rows = sqlSessionTemplate.selectList("getRowDenormalisers", query, rowBounds);
		return rows;
	}

	public List<RowDenormaliser> list(RowDenormaliserQuery query) {
		List<RowDenormaliser> list = rowDenormaliserMapper.getRowDenormalisers(query);
		return list;
	}

	@Transactional
	public void save(RowDenormaliser rowDenormaliser) {
		if (StringUtils.isEmpty(rowDenormaliser.getId())) {
			rowDenormaliser.setId(idGenerator.getNextId("SYS_ROW_DENORMALISER"));
			rowDenormaliser.setCreateTime(new Date());

			rowDenormaliserMapper.insertRowDenormaliser(rowDenormaliser);
		} else {
			rowDenormaliser.setUpdateTime(new Date());
			rowDenormaliserMapper.updateRowDenormaliser(rowDenormaliser);
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

	@javax.annotation.Resource(name = "com.glaf.matrix.transform.mapper.RowDenormaliserMapper")
	public void setRowDenormaliserMapper(RowDenormaliserMapper rowDenormaliserMapper) {
		this.rowDenormaliserMapper = rowDenormaliserMapper;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

}
