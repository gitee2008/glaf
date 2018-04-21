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
package com.glaf.core.factory;

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
import org.apache.ibatis.session.SqlSessionFactory;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.entity.SqlExecutor;
import com.glaf.core.id.Dbid;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.util.ClassUtils;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.JdbcUtils;
import com.glaf.core.util.Paging;
import com.glaf.core.util.Tools;

public class EntityFactory {
	private static class EntityHolder {
		public static EntityFactory instance = new EntityFactory();
	}

	protected static final Log logger = LogFactory.getLog(EntityFactory.class);

	public static EntityFactory getInstance() {
		return EntityHolder.instance;
	}

	protected SqlSessionFactory sqlSessionFactory;

	private EntityFactory() {

	}

	/**
	 * 删除记录
	 * 
	 * @param statementId
	 * @param parameterObject
	 */
	@SuppressWarnings("unchecked")
	public void delete(String systemName, String statementId, Object parameterObject) {
		SqlSession sqlSession = null;
		Connection conn = null;
		try {
			if (systemName != null) {
				conn = DBConnectionFactory.getConnection(systemName);
			} else {
				conn = DBConnectionFactory.getConnection();
			}
			conn.setAutoCommit(false);
			sqlSession = getSqlSessionFactory().openSession(conn);
			if (parameterObject instanceof Map) {
				Map<String, Object> dataMap = (Map<String, Object>) parameterObject;
				String className = (String) dataMap.get("className");
				if (className != null) {
					Object object = ClassUtils.instantiateObject(className);
					Tools.populate(object, dataMap);
					sqlSession.delete(statementId, object);
				} else {
					sqlSession.delete(statementId, dataMap);
				}
			} else {
				sqlSession.delete(statementId, parameterObject);
			}
			sqlSession.commit(true);
			conn.commit();
		} catch (Exception ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
			JdbcUtils.close(conn);
		}
	}

	/**
	 * 删除多条记录
	 * 
	 * @param statementId
	 * @param parameterObject
	 */
	@SuppressWarnings("unchecked")
	public void deleteAll(String systemName, String statementId, List<Object> rows) {
		SqlSession sqlSession = null;
		Connection conn = null;
		try {
			if (systemName != null) {
				conn = DBConnectionFactory.getConnection(systemName);
			} else {
				conn = DBConnectionFactory.getConnection();
			}
			conn.setAutoCommit(false);
			sqlSession = getSqlSessionFactory().openSession(conn);
			for (Object parameterObject : rows) {
				if (parameterObject instanceof Map) {
					Map<String, Object> dataMap = (Map<String, Object>) parameterObject;
					String className = (String) dataMap.get("className");
					if (className != null) {
						Object object = ClassUtils.instantiateObject(className);
						Tools.populate(object, dataMap);
						sqlSession.delete(statementId, object);
					} else {
						sqlSession.delete(statementId, dataMap);
					}
				} else {
					sqlSession.delete(statementId, parameterObject);
				}
			}
			sqlSession.commit(true);
			conn.commit();
		} catch (Exception ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
			JdbcUtils.close(conn);
		}
	}

	/**
	 * 根据记录主键删除记录
	 * 
	 * @param statementId
	 * @param rowId
	 */
	public void deleteById(String systemName, String statementId, Object rowId) {
		SqlSession sqlSession = null;
		Connection conn = null;
		try {
			if (systemName != null) {
				conn = DBConnectionFactory.getConnection(systemName);
			} else {
				conn = DBConnectionFactory.getConnection();
			}
			conn.setAutoCommit(false);
			sqlSession = getSqlSessionFactory().openSession(conn);
			sqlSession.delete(statementId, rowId);
			sqlSession.commit(true);
			conn.commit();
		} catch (Exception ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
			JdbcUtils.close(conn);
		}
	}

	/**
	 * 执行批量更新
	 * 
	 * @param sqlExecutors
	 */
	public void executeBatch(String systemName, List<SqlExecutor> sqlExecutors) {
		SqlSession sqlSession = null;
		Connection conn = null;
		try {
			if (systemName != null) {
				conn = DBConnectionFactory.getConnection(systemName);
			} else {
				conn = DBConnectionFactory.getConnection();
			}
			conn.setAutoCommit(false);
			sqlSession = getSqlSessionFactory().openSession(conn);
			for (SqlExecutor sqlExecutor : sqlExecutors) {
				String statementId = sqlExecutor.getStatementId();
				String operation = sqlExecutor.getOperation();
				Object parameter = sqlExecutor.getParameter();
				if (StringUtils.equalsIgnoreCase("insert", operation)) {
					sqlSession.insert(statementId, parameter);
				} else if (StringUtils.equalsIgnoreCase("update", operation)) {
					sqlSession.update(statementId, parameter);
				} else if (StringUtils.equalsIgnoreCase("delete", operation)) {
					sqlSession.delete(statementId, parameter);
				}
			}
			sqlSession.commit(true);
			conn.commit();
		} catch (Exception ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
			JdbcUtils.close(conn);
		}
	}

	/**
	 * 根据主键获取记录
	 * 
	 * @param statementId
	 * @param parameterObject
	 * @return
	 */
	public Object getById(String systemName, String statementId, Object parameterObject) {
		SqlSession sqlSession = null;
		Connection conn = null;
		try {
			if (systemName != null) {
				conn = DBConnectionFactory.getConnection(systemName);
			} else {
				conn = DBConnectionFactory.getConnection();
			}
			sqlSession = getSqlSessionFactory().openSession(conn);
			return sqlSession.selectOne(statementId, parameterObject);
		} catch (Exception ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
			JdbcUtils.close(conn);
		}
	}

	/**
	 * 获取总记录数
	 * 
	 * @param statementId
	 * @param parameterObject
	 * @return
	 */
	public int getCount(String systemName, String statementId, Object parameterObject) {
		int totalCount = 0;
		SqlSession sqlSession = null;
		Connection conn = null;
		try {
			if (systemName != null) {
				conn = DBConnectionFactory.getConnection(systemName);
			} else {
				conn = DBConnectionFactory.getConnection();
			}
			sqlSession = getSqlSessionFactory().openSession(conn);
			Object object = null;
			if (parameterObject != null) {
				object = sqlSession.selectOne(statementId, parameterObject);
			} else {
				object = sqlSession.selectOne(statementId);
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
		} catch (Exception ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
			JdbcUtils.close(conn);
		}
	}

	/**
	 * 获取一页数据
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param sqlExecutor
	 * @return
	 */
	public List<Object> getList(String systemName, int pageNo, int pageSize, SqlExecutor sqlExecutor) {
		List<Object> rows = null;
		SqlSession sqlSession = null;
		Connection conn = null;
		try {
			if (systemName != null) {
				conn = DBConnectionFactory.getConnection(systemName);
			} else {
				conn = DBConnectionFactory.getConnection();
			}
			sqlSession = getSqlSessionFactory().openSession(conn);
			int begin = (pageNo - 1) * pageSize;

			Object parameter = sqlExecutor.getParameter();
			RowBounds rowBounds = new RowBounds(begin, pageSize);

			if (parameter != null) {
				rows = sqlSession.selectList(sqlExecutor.getStatementId(), parameter, rowBounds);
			} else {
				rows = sqlSession.selectList(sqlExecutor.getStatementId(), null, rowBounds);
			}
			return rows;
		} catch (Exception ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
			JdbcUtils.close(conn);
		}
	}

	/**
	 * 获取数据集
	 * 
	 * @param statementId
	 * @param parameterObject
	 * @return
	 */
	public List<Object> getList(String systemName, String statementId, Object parameterObject) {
		SqlSession sqlSession = null;
		Connection conn = null;
		try {
			if (systemName != null) {
				conn = DBConnectionFactory.getConnection(systemName);
			} else {
				conn = DBConnectionFactory.getConnection();
			}
			sqlSession = getSqlSessionFactory().openSession(conn);
			return sqlSession.selectList(statementId, parameterObject);
		} catch (Exception ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
			JdbcUtils.close(conn);
		}
	}

	/**
	 * 获取下一条记录编号
	 * 
	 * @param tablename
	 *            表名称
	 * @param idColumn
	 *            主键列名
	 * @return
	 */
	public long getMaxId(String systemName, String tablename, String idColumn) {
		SqlSession sqlSession = null;
		Connection conn = null;
		try {
			if (systemName != null) {
				conn = DBConnectionFactory.getConnection(systemName);
			} else {
				conn = DBConnectionFactory.getConnection();
			}
			sqlSession = getSqlSessionFactory().openSession(conn);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("tablename", tablename);
			params.put("idColumn", idColumn);
			long oldValue = sqlSession.selectOne("getMaxId", params);
			return oldValue;
		} catch (Exception ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
			JdbcUtils.close(conn);
		}
	}

	/**
	 * 获取下一条记录编号
	 * 
	 * @param tablename
	 *            表名称
	 * @param idColumn
	 *            主键列名
	 * @return
	 */
	public long getNextId(String systemName, String name) {
		SqlSession sqlSession = null;
		Connection conn = null;
		try {
			if (systemName != null) {
				conn = DBConnectionFactory.getConnection(systemName);
			} else {
				conn = DBConnectionFactory.getConnection();
			}
			sqlSession = getSqlSessionFactory().openSession(conn);
			Dbid dbid = (Dbid) sqlSession.selectOne("getNextDbId", name);
			if (dbid == null) {
				dbid = new Dbid();
				dbid.setTitle("系统内置主键");
				dbid.setName(name);
				dbid.setValue("1");
				dbid.setVersion(1);
				sqlSession.insert("inertNextDbId", dbid);
				dbid = (Dbid) sqlSession.selectOne("getNextDbId", name);
			}
			long oldValue = Long.parseLong(dbid.getValue());
			long newValue = oldValue + 1;
			dbid.setName(name);
			dbid.setTitle("系统内置主键");
			dbid.setValue(Long.toString(newValue));
			dbid.setVersion(dbid.getVersion() + 1);
			sqlSession.update("updateNextDbId", dbid);
			return newValue;
		} catch (Exception ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
			JdbcUtils.close(conn);
		}
	}

	/**
	 * 获取一页记录
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param countExecutor
	 * @param queryExecutor
	 * @return
	 */
	public Paging getPage(String systemName, int pageNo, int pageSize, SqlExecutor countExecutor,
			SqlExecutor queryExecutor) {
		Paging page = new Paging();
		SqlSession sqlSession = null;
		Connection conn = null;
		try {
			if (systemName != null) {
				conn = DBConnectionFactory.getConnection(systemName);
			} else {
				conn = DBConnectionFactory.getConnection();
			}
			sqlSession = getSqlSessionFactory().openSession(conn);
			if (pageSize <= 0) {
				pageSize = Paging.DEFAULT_PAGE_SIZE;
			}
			if (pageNo <= 0) {
				pageNo = 1;
			}

			Object object = null;
			int totalCount = 0;

			Object parameter = countExecutor.getParameter();
			if (parameter != null) {
				object = sqlSession.selectOne(countExecutor.getStatementId(), parameter);
			} else {
				object = sqlSession.selectOne(countExecutor.getStatementId());
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
				rows = sqlSession.selectList(queryExecutor.getStatementId(), queryParams, rowBounds);
			} else {
				rows = sqlSession.selectList(queryExecutor.getStatementId(), null, rowBounds);
			}

			page.setRows(rows);
			page.setPageSize(pageSize);
			page.setCurrentPage(pageNo);

			logger.debug("params:" + queryParams);
			logger.debug("rows size:" + rows.size());

		} catch (Exception ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
			JdbcUtils.close(conn);
		}
		return page;
	}

	public SqlSessionFactory getSqlSessionFactory() {
		if (sqlSessionFactory == null) {
			sqlSessionFactory = ContextFactory.getBean("sqlSessionFactory");
		}
		return sqlSessionFactory;
	}

	/**
	 * 获取下一条记录编号
	 * 
	 * @return
	 */
	public int getTableUserMaxId(String systemName, String tablename, String idColumn, String createBy) {
		SqlSession sqlSession = null;
		Connection conn = null;
		try {
			if (systemName != null) {
				conn = DBConnectionFactory.getConnection(systemName);
			} else {
				conn = DBConnectionFactory.getConnection();
			}
			sqlSession = getSqlSessionFactory().openSession(conn);
			int day = DateUtils.getNowYearMonthDay();
			String idLike = String.valueOf(day) + "/" + createBy + "-%";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("tablename", tablename);
			params.put("idColumn", idColumn);
			params.put("idLike", idLike);

			String str = sqlSession.selectOne("getTableUserMaxId", params);
			if (StringUtils.isNotEmpty(str) && StringUtils.contains(str, "-")) {
				str = str.substring(str.lastIndexOf("-") + 1, str.length());
				str = str.trim();
				if (StringUtils.isNumeric(str)) {
					return Integer.parseInt(str) + 1;
				}
			}
		} catch (Exception ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
			JdbcUtils.close(conn);
		}
		return 1;
	}

	/**
	 * 插入一条记录
	 * 
	 * @param statementId
	 * @param parameterObject
	 */
	public void insert(String systemName, String statementId, Object parameterObject) {
		SqlSession sqlSession = null;
		Connection conn = null;
		try {
			if (systemName != null) {
				conn = DBConnectionFactory.getConnection(systemName);
			} else {
				conn = DBConnectionFactory.getConnection();
			}
			conn.setAutoCommit(false);
			sqlSession = getSqlSessionFactory().openSession(conn);
			sqlSession.insert(statementId, parameterObject);
			sqlSession.commit(true);
			conn.commit();
		} catch (Exception ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
			JdbcUtils.close(conn);
		}
	}

	/**
	 * 插入多条记录
	 * 
	 * @param statementId
	 * @param parameterObject
	 */
	public void insertAll(String systemName, String statementId, List<Object> rows) {
		SqlSession sqlSession = null;
		Connection conn = null;
		try {
			if (systemName != null) {
				conn = DBConnectionFactory.getConnection(systemName);
			} else {
				conn = DBConnectionFactory.getConnection();
			}
			conn.setAutoCommit(false);
			sqlSession = getSqlSessionFactory().openSession(conn);
			for (Object parameterObject : rows) {
				sqlSession.insert(statementId, parameterObject);
			}
			sqlSession.commit(true);
			conn.commit();
		} catch (Exception ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
			JdbcUtils.close(conn);
		}
	}

	/**
	 * 根据主键获取记录
	 * 
	 * @param statementId
	 * @param parameterObject
	 * @return
	 */
	public Object selectOne(String systemName, String statementId, Object parameterObject) {
		SqlSession sqlSession = null;
		Connection conn = null;
		try {
			if (systemName != null) {
				conn = DBConnectionFactory.getConnection(systemName);
			} else {
				conn = DBConnectionFactory.getConnection();
			}
			sqlSession = getSqlSessionFactory().openSession(conn);
			return sqlSession.selectOne(statementId, parameterObject);
		} catch (Exception ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
			JdbcUtils.close(conn);
		}
	}

	/**
	 * 修改一条记录
	 * 
	 * @param statementId
	 * @param parameterObject
	 */
	public void update(String systemName, String statementId, Object parameterObject) {
		SqlSession sqlSession = null;
		Connection conn = null;
		try {
			if (systemName != null) {
				conn = DBConnectionFactory.getConnection(systemName);
			} else {
				conn = DBConnectionFactory.getConnection();
			}
			conn.setAutoCommit(false);
			sqlSession = getSqlSessionFactory().openSession(conn);
			sqlSession.update(statementId, parameterObject);
			sqlSession.commit(true);
			conn.commit();
		} catch (Exception ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
			JdbcUtils.close(conn);
		}
	}

	/**
	 * 修改多条记录
	 * 
	 * @param statementId
	 * @param parameterObject
	 */
	public void updateAll(String systemName, String statementId, List<Object> rows) {
		SqlSession sqlSession = null;
		Connection conn = null;
		try {
			if (systemName != null) {
				conn = DBConnectionFactory.getConnection(systemName);
			} else {
				conn = DBConnectionFactory.getConnection();
			}
			conn.setAutoCommit(false);
			sqlSession = getSqlSessionFactory().openSession(conn);
			for (Object parameterObject : rows) {
				sqlSession.update(statementId, parameterObject);
			}
			sqlSession.commit(true);
			conn.commit();
		} catch (Exception ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
			JdbcUtils.close(conn);
		}
	}

}
