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

package com.glaf.core.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;

import com.glaf.core.config.BaseConfiguration;
import com.glaf.core.config.Configuration;
import com.glaf.core.entity.SqlExecutor;
import com.glaf.core.id.Dbid;
import com.glaf.core.id.IdBlock;
import com.glaf.core.util.ClassUtils;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.Paging;
import com.glaf.core.util.Tools;

public class MyBatisEntityDAO implements EntityDAO {

	protected final static Log logger = LogFactory.getLog(MyBatisEntityDAO.class);

	protected static Configuration conf = BaseConfiguration.create();

	protected final SqlSession sqlSession;

	public MyBatisEntityDAO(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@SuppressWarnings("unchecked")
	public void delete(String statementId, Object parameterObject) {
		if (parameterObject instanceof Map) {
			Map<String, Object> dataMap = (Map<String, Object>) parameterObject;
			String className = (String) dataMap.get("className");
			if (className != null) {
				Object object = ClassUtils.instantiateObject(className);
				Tools.populate(object, dataMap);
				getSqlSession().delete(statementId, object);
			} else {
				getSqlSession().delete(statementId, dataMap);
			}
		} else {
			getSqlSession().delete(statementId, parameterObject);
		}
	}

	public void deleteAll(String statementId, List<Object> rows) {
		for (Object parameter : rows) {
			this.delete(statementId, parameter);
		}
	}

	public void deleteById(String statementId, Object parameter) {
		this.delete(statementId, parameter);
	}

	public void execute(SqlExecutor sqlExecutor) {
		String statementId = sqlExecutor.getStatementId();
		String operation = sqlExecutor.getOperation();
		Object parameter = sqlExecutor.getParameter();
		if (StringUtils.equalsIgnoreCase("insert", operation)) {
			getSqlSession().insert(statementId, parameter);
		} else if (StringUtils.equalsIgnoreCase("update", operation)) {
			getSqlSession().update(statementId, parameter);
		} else if (StringUtils.equalsIgnoreCase("delete", operation)) {
			getSqlSession().delete(statementId, parameter);
		}
	}

	public void executeBatch(List<SqlExecutor> sqlExecutors) {
		for (SqlExecutor sqlExecutor : sqlExecutors) {
			String statementId = sqlExecutor.getStatementId();
			String operation = sqlExecutor.getOperation();
			Object parameter = sqlExecutor.getParameter();
			if (StringUtils.equalsIgnoreCase("insert", operation)) {
				getSqlSession().insert(statementId, parameter);
			} else if (StringUtils.equalsIgnoreCase("update", operation)) {
				getSqlSession().update(statementId, parameter);
			} else if (StringUtils.equalsIgnoreCase("delete", operation)) {
				getSqlSession().delete(statementId, parameter);
			}
		}
	}

	public Object getById(String statementId, Object parameterObject) {
		return getSqlSession().selectOne(statementId, parameterObject);
	}

	public int getCount(String statementId, Object parameterObject) {
		int totalCount = 0;
		SqlSession session = getSqlSession();

		Object object = null;
		if (parameterObject != null) {
			object = session.selectOne(statementId, parameterObject);
		} else {
			object = session.selectOne(statementId);
		}

		if (object instanceof Integer) {
			Integer iCount = (Integer) object;
			totalCount = iCount.intValue();
		} else if (object instanceof Long) {
			Long iCount = (Long) object;
			totalCount = iCount.intValue();
		} else if (object instanceof BigDecimal) {
			BigDecimal bg = (BigDecimal) object;
			totalCount = bg.intValue();
		} else if (object instanceof BigInteger) {
			BigInteger bi = (BigInteger) object;
			totalCount = bi.intValue();
		} else {
			String value = object.toString();
			totalCount = Integer.parseInt(value);
		}
		return totalCount;
	}

	public List<Object> getList(int pageNo, int pageSize, SqlExecutor queryExecutor) {
		int begin = (pageNo - 1) * pageSize;
		List<Object> rows = null;

		Object parameter = queryExecutor.getParameter();
		RowBounds rowBounds = new RowBounds(begin, pageSize);

		if (parameter != null) {
			rows = getSqlSession().selectList(queryExecutor.getStatementId(), parameter, rowBounds);
		} else {
			rows = getSqlSession().selectList(queryExecutor.getStatementId(), null, rowBounds);
		}
		return rows;
	}

	public List<Object> getList(String statementId, Object parameterObject) {
		logger.debug("statementId:" + statementId);
		return getSqlSession().selectList(statementId, parameterObject);
	}

	public long getMaxId(String tablename, String idColumn) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tablename", tablename);
		params.put("idColumn", idColumn);
		long oldValue = getSqlSession().selectOne("getMaxId", params);
		return oldValue;
	}

	public Object getObject(String statementId, Object parameterObject) {
		return getSqlSession().selectOne(statementId, parameterObject);
	}

	public Paging getPage(int pageNo, int pageSize, SqlExecutor countExecutor, SqlExecutor queryExecutor) {
		if (pageSize <= 0) {
			pageSize = Paging.DEFAULT_PAGE_SIZE;
		}
		if (pageNo <= 0) {
			pageNo = 1;
		}

		Object object = null;
		int totalCount = 0;
		Paging page = new Paging();
		SqlSession session = getSqlSession();

		Object parameter = countExecutor.getParameter();
		if (parameter != null) {
			object = session.selectOne(countExecutor.getStatementId(), parameter);
		} else {
			object = session.selectOne(countExecutor.getStatementId());
		}

		if (object instanceof Integer) {
			Integer iCount = (Integer) object;
			totalCount = iCount.intValue();
		} else if (object instanceof Long) {
			Long iCount = (Long) object;
			totalCount = iCount.intValue();
		} else if (object instanceof BigDecimal) {
			BigDecimal bg = (BigDecimal) object;
			totalCount = bg.intValue();
		} else if (object instanceof BigInteger) {
			BigInteger bi = (BigInteger) object;
			totalCount = bi.intValue();
		} else {
			String value = object.toString();
			totalCount = Integer.parseInt(value);
		}

		if (totalCount == 0) {
			page.setRows(new java.util.ArrayList<Object>());
			page.setCurrentPage(0);
			page.setPageSize(0);
			page.setTotal(0);
			return page;
		}

		page.setTotal(totalCount);

		int maxPageNo = (page.getTotal() + (pageSize - 1)) / pageSize;
		if (pageNo > maxPageNo) {
			pageNo = maxPageNo;
		}

		List<Object> rows = null;

		Object queryParams = queryExecutor.getParameter();

		int begin = (pageNo - 1) * pageSize;

		RowBounds rowBounds = new RowBounds(begin, pageSize);

		if (queryParams != null) {
			rows = session.selectList(queryExecutor.getStatementId(), queryParams, rowBounds);
		} else {
			rows = session.selectList(queryExecutor.getStatementId(), null, rowBounds);
		}

		page.setRows(rows);
		page.setPageSize(pageSize);
		page.setCurrentPage(pageNo);

		// logger.debug("params:" + queryParams);
		logger.debug("rows size:" + rows.size());

		return page;
	}

	public Object getSingleObject(String statementId, Object parameterObject) {
		return getSqlSession().selectOne(statementId, parameterObject);
	}

	public SqlSession getSqlSession() {
		return sqlSession;
	}

	public int getTableUserMaxId(String tablename, String idColumn, String createBy) {
		int day = DateUtils.getNowYearMonthDay();
		String idLike = String.valueOf(day) + "/" + createBy + "-%";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tablename", tablename);
		params.put("idColumn", idColumn);
		params.put("idLike", idLike);

		String str = getSqlSession().selectOne("getTableUserMaxId", params);
		if (StringUtils.isNotEmpty(str) && StringUtils.contains(str, "-")) {
			str = str.substring(str.lastIndexOf("-") + 1, str.length());
			str = str.trim();
			if (StringUtils.isNumeric(str)) {
				return Integer.parseInt(str) + 1;
			}
		}

		return 1;
	}

	public void insert(String statementId, Object parameterObject) {
		getSqlSession().insert(statementId, parameterObject);
	}

	public void insertAll(String statementId, List<Object> rows) {
		for (Object entity : rows) {
			getSqlSession().insert(statementId, entity);
		}
	}

	public IdBlock nextDbidBlock() {
		Dbid dbid = (Dbid) getSqlSession().selectOne("getNextDbId", "next.dbid");
		if (dbid == null) {
			dbid = new Dbid();
			dbid.setTitle("系统内置主键");
			dbid.setName("next.dbid");
			dbid.setValue("10001");
			dbid.setVersion(1);
			getSqlSession().insert("inertNextDbId", dbid);
			dbid = (Dbid) getSqlSession().selectOne("getNextDbId", "next.dbid");
		}
		long oldValue = Long.parseLong(dbid.getValue());
		long newValue = oldValue + conf.getInt("dbid_step", 100);
		dbid.setName("next.dbid");
		dbid.setTitle("系统内置主键");
		dbid.setValue(Long.toString(newValue + 1));
		dbid.setVersion(dbid.getVersion() + 1);
		getSqlSession().update("updateNextDbId", dbid);
		return new IdBlock(oldValue, newValue - 1);
	}

	public IdBlock nextDbidBlock(String name) {
		Dbid dbid = (Dbid) getSqlSession().selectOne("getNextDbId", name);
		if (dbid == null) {
			dbid = new Dbid();
			dbid.setTitle("系统内置主键");
			dbid.setName(name);
			dbid.setValue("1");
			dbid.setVersion(1);
			getSqlSession().insert("inertNextDbId", dbid);
			dbid = (Dbid) getSqlSession().selectOne("getNextDbId", name);
		}
		long oldValue = Long.parseLong(dbid.getValue());
		long newValue = oldValue + conf.getInt("dbid_step_" + name, 100);
		dbid.setName(name);
		dbid.setTitle("系统内置主键");
		dbid.setValue(Long.toString(newValue + 1));
		dbid.setVersion(dbid.getVersion() + 1);
		getSqlSession().update("updateNextDbId", dbid);
		return new IdBlock(oldValue, newValue - 1);
	}

	public void setConnection(Connection connection) {

	}

	public void update(String statementId, Object parameterObject) {
		getSqlSession().update(statementId, parameterObject);
	}

	public void updateAll(String statementId, List<Object> rows) {
		for (Object entity : rows) {
			getSqlSession().update(statementId, entity);
		}
	}

}