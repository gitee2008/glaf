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

package com.glaf.core.service.impl;

import com.glaf.core.dao.EntityDAO;
import com.glaf.core.entity.SqlExecutor;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.service.EntityService;
import com.glaf.core.util.Paging;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("entityService")
@Transactional
public class MyBatisEntityService implements EntityService {

	private EntityDAO entityDAO;

	private IdGenerator idGenerator;

	public MyBatisEntityService() {

	}

	@Transactional
	public void delete(String statementId, Object parameter) {
		entityDAO.delete(statementId, parameter);
	}

	@Transactional
	public void deleteAll(String statementId, List<Object> rows) {
		if (rows != null && rows.size() > 0) {
			for (Object object : rows) {
				entityDAO.deleteById(statementId, object);
			}
		}
	}

	@Transactional
	public void deleteById(String statementId, Object rowId) {
		entityDAO.deleteById(statementId, rowId);
	}

	@Transactional
	public void executeBatch(List<SqlExecutor> sqlExecutors) {
		entityDAO.executeBatch(sqlExecutors);
	}

	@Transactional(readOnly = true)
	public Object getById(String statementId, Object parameterObject) {
		return entityDAO.getById(statementId, parameterObject);
	}

	@Transactional(readOnly = true)
	public int getCount(String statementId, Object parameterObject) {
		return entityDAO.getCount(statementId, parameterObject);
	}

	@Transactional(readOnly = true)
	public List<Object> getList(int pageNo, int pageSize, SqlExecutor queryExecutor) {
		return entityDAO.getList(pageNo, pageSize, queryExecutor);
	}

	/**
	 * 查询
	 *
	 * @param statementId
	 * @param parameter
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Object> getList(String statementId, Object parameter) {
		return entityDAO.getList(statementId, parameter);
	}

	@Transactional
	public String getNextId(String tablename, String idColumn, String createBy) {
		// int day = DateUtils.getNowYearMonthDay();
		// String idLike = String.valueOf(day) + "/" + createBy + "-";
		// int maxId = entityDAO.getTableUserMaxId(tablename, idColumn,
		// createBy);
		// String newId = idLike + StringTools.getDigit7Id(maxId);
		return idGenerator.getNextId(tablename, idColumn, createBy);
	}

	@Transactional(readOnly = true)
	public Paging getPage(int pageNo, int pageSize, SqlExecutor countExecutor, SqlExecutor queryExecutor) {
		return entityDAO.getPage(pageNo, pageSize, countExecutor, queryExecutor);
	}

	@Transactional(readOnly = true)
	public Object getSingleObject(String statementId, Object parameterObject) {
		return entityDAO.getSingleObject(statementId, parameterObject);
	}

	@Transactional
	public void insert(String statementId, Object obj) {
		entityDAO.insert(statementId, obj);
	}

	@Transactional
	public void insertAll(String statementId, List<Object> rows) {
		if (rows != null && rows.size() > 0) {
			entityDAO.insertAll(statementId, rows);
		}
	}

	@Transactional
	public Long nextId() {
		return idGenerator.nextId();
	}

	@Transactional
	public Long nextId(String name) {
		return idGenerator.nextId(name);
	}

	/**
	 * 获取某个表用户记录编号
	 *
	 * @param tablename 表名称
	 * @param idColumn  表主键列名
	 * @return
	 */
	@Transactional
	public Long nextId(String tablename, String idColumn) {
		// Map<String, Object> params = new HashMap<String, Object>();
		// params.put("tablename", tablename);
		// params.put("idColumn", idColumn);
		// return (Long) entityDAO.getSingleObject("getTableMaxId", params);
		return idGenerator.nextId(tablename, idColumn);
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource
	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	@Transactional
	public void update(String statementId, Object obj) {
		entityDAO.update(statementId, obj);
	}

	@Transactional
	public void updateAll(String statementId, List<Object> rows) {
		if (rows != null && rows.size() > 0) {
			entityDAO.updateAll(statementId, rows);
		}
	}

}