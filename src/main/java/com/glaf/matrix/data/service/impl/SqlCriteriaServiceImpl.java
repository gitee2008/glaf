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
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.glaf.core.cache.CacheFactory;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.dao.*;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.util.*;

import com.glaf.matrix.data.mapper.*;
import com.glaf.matrix.data.domain.*;
import com.glaf.matrix.data.query.*;
import com.glaf.matrix.data.service.SqlCriteriaService;
import com.glaf.matrix.data.util.SqlCriteriaJsonFactory;

@Service("sqlCriteriaService")
@Transactional(readOnly = true)
public class SqlCriteriaServiceImpl implements SqlCriteriaService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected SqlCriteriaMapper sqlCriteriaMapper;

	public SqlCriteriaServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<SqlCriteria> list) {
		for (SqlCriteria sqlCriteria : list) {
			if (sqlCriteria.getId() == 0) {
				sqlCriteria.setId(idGenerator.nextId("SYS_SQL_CRITERIA"));
				sqlCriteria.setCreateTime(new Date());
			}
		}

		int batch_size = 200;
		List<SqlCriteria> rows = new ArrayList<SqlCriteria>(batch_size);

		for (SqlCriteria bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % 100 == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					sqlCriteriaMapper.bulkInsertSqlCriteria_oracle(rows);
				} else {
					sqlCriteriaMapper.bulkInsertSqlCriteria(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				sqlCriteriaMapper.bulkInsertSqlCriteria_oracle(rows);
			} else {
				sqlCriteriaMapper.bulkInsertSqlCriteria(rows);
			}
			rows.clear();
		}
	}

	public int count(SqlCriteriaQuery query) {
		return sqlCriteriaMapper.getSqlCriteriaCount(query);
	}

	@Transactional
	public void deleteById(Long id) {
		if (id != null) {
			sqlCriteriaMapper.deleteSqlCriteriaById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				sqlCriteriaMapper.deleteSqlCriteriaById(id);
			}
		}
	}

	public SqlCriteria getSqlCriteria(Long id) {
		if (id == null) {
			return null;
		}
		SqlCriteria sqlCriteria = sqlCriteriaMapper.getSqlCriteriaById(id);
		return sqlCriteria;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getSqlCriteriaCountByQueryCriteria(SqlCriteriaQuery query) {
		return sqlCriteriaMapper.getSqlCriteriaCount(query);
	}

	public List<SqlCriteria> getSqlCriterias(String businessKey, String moduleId) {
		String cacheKey = "sys_sql_criteria_" + businessKey + "_" + moduleId;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString("sys_sql_criteria", cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONArray array = JSON.parseArray(text);
					List<SqlCriteria> list = SqlCriteriaJsonFactory.arrayToList(array);
					return list;
				} catch (Exception ex) {
					// ex.printStackTrace();
				}
			}
		}

		SqlCriteriaQuery query = new SqlCriteriaQuery();
		query.businessKey(businessKey);
		query.moduleId(moduleId);
		List<SqlCriteria> sqlCriterias = this.list(query);
		if (sqlCriterias != null && !sqlCriterias.isEmpty()) {
			JSONArray array = SqlCriteriaJsonFactory.listToArray(sqlCriterias);
			CacheFactory.put("sys_sql_criteria", cacheKey, array.toJSONString());
		} else {
			CacheFactory.put("sys_sql_criteria", cacheKey, "[]");
		}
		return sqlCriterias;
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<SqlCriteria> getSqlCriteriasByQueryCriteria(int start, int pageSize, SqlCriteriaQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<SqlCriteria> rows = sqlSessionTemplate.selectList("getSqlCriterias", query, rowBounds);
		return rows;
	}

	public List<SqlCriteria> list(SqlCriteriaQuery query) {
		List<SqlCriteria> list = sqlCriteriaMapper.getSqlCriterias(query);
		return list;
	}

	@Transactional
	public void save(SqlCriteria sqlCriteria) {
		if (sqlCriteria.getId() == 0) {
			sqlCriteria.setId(idGenerator.nextId("SYS_SQL_CRITERIA"));
			sqlCriteria.setCreateTime(new Date());
			long parentId = sqlCriteria.getParentId();
			if (parentId > 0) {
				SqlCriteria parent = this.getSqlCriteria(parentId);
				if (parent != null) {
					sqlCriteria.setLevel(parent.getLevel() + 1);
					if (parent.getTreeId() != null) {
						sqlCriteria.setTreeId(parent.getTreeId() + sqlCriteria.getId() + "|");
					}
				}
			} else {
				sqlCriteria.setLevel(0);
				sqlCriteria.setTreeId(sqlCriteria.getId() + "|");
			}
			sqlCriteriaMapper.insertSqlCriteria(sqlCriteria);
		} else {
			long parentId = sqlCriteria.getParentId();
			if (parentId > 0 && parentId != sqlCriteria.getId()) {
				SqlCriteria parent = this.getSqlCriteria(parentId);
				if (parent != null) {
					sqlCriteria.setLevel(parent.getLevel() + 1);
					if (parent.getTreeId() != null) {
						sqlCriteria.setTreeId(parent.getTreeId() + sqlCriteria.getId() + "|");
					}
				}
			} else {
				sqlCriteria.setLevel(0);
				sqlCriteria.setTreeId(sqlCriteria.getId() + "|");
			}
			sqlCriteria.setUpdateTime(new Date());
			sqlCriteriaMapper.updateSqlCriteria(sqlCriteria);
		}

		CacheFactory.clear("sys_sql_criteria");
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
	public void setSqlCriteriaMapper(SqlCriteriaMapper sqlCriteriaMapper) {
		this.sqlCriteriaMapper = sqlCriteriaMapper;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

}
