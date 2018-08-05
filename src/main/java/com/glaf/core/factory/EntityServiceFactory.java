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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.core.config.Environment;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.entity.SqlExecutor;
import com.glaf.core.service.EntityService;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.Paging;

public class EntityServiceFactory {
	private static class EntityServiceHolder {
		public static EntityServiceFactory instance = new EntityServiceFactory();
	}

	protected static final Log logger = LogFactory
			.getLog(EntityServiceFactory.class);

	public static EntityServiceFactory getInstance() {
		return EntityServiceHolder.instance;
	}

	private EntityService entityService;

	private EntityServiceFactory() {

	}

	/**
	 * 删除记录
	 * 
	 * @param statementId
	 * @param parameterObject
	 */
	public void delete(String systemName, String statementId,
			Object parameterObject) {
		String currentName = Environment.getCurrentSystemName();
		if (systemName != null) {
			Environment.setCurrentSystemName(systemName);
		}
		try {
			getEntityService().delete(statementId, parameterObject);
		} catch (Exception ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			Environment.setCurrentSystemName(currentName);
		}
	}

	/**
	 * 删除多条记录
	 * 
	 * @param statementId
	 * @param parameterObject
	 */
	public void deleteAll(String systemName, String statementId,
			List<Object> rows) {
		String currentName = Environment.getCurrentSystemName();
		if (systemName != null) {
			Environment.setCurrentSystemName(systemName);
		}
		try {
			getEntityService().deleteAll(statementId, rows);
		} catch (Exception ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			Environment.setCurrentSystemName(currentName);
		}
	}

	/**
	 * 根据记录主键删除记录
	 * 
	 * @param statementId
	 * @param row
	 */
	public void deleteById(String systemName, String statementId, Object row) {
		String currentName = Environment.getCurrentSystemName();
		if (systemName != null) {
			Environment.setCurrentSystemName(systemName);
		}
		try {
			getEntityService().deleteById(statementId, row);
		} catch (Exception ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			Environment.setCurrentSystemName(currentName);
		}
	}

	/**
	 * 执行批量更新
	 * 
	 * @param sqlExecutors
	 */
	public void executeBatch(String systemName, List<SqlExecutor> sqlExecutors) {
		String currentName = Environment.getCurrentSystemName();
		if (systemName != null) {
			Environment.setCurrentSystemName(systemName);
		}
		try {
			getEntityService().executeBatch(sqlExecutors);
		} catch (Exception ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			Environment.setCurrentSystemName(currentName);
		}
	}

	/**
	 * 根据主键获取记录
	 * 
	 * @param statementId
	 * @param parameterObject
	 * @return
	 */
	public Object getById(String systemName, String statementId,
			Object parameterObject) {
		String currentName = Environment.getCurrentSystemName();
		if (systemName != null) {
			Environment.setCurrentSystemName(systemName);
		}
		try {
			return getEntityService().getById(statementId, parameterObject);
		} catch (Exception ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			Environment.setCurrentSystemName(currentName);
		}
	}

	/**
	 * 获取总记录数
	 * 
	 * @param statementId
	 * @param parameterObject
	 * @return
	 */
	public int getCount(String systemName, String statementId,
			Object parameterObject) {
		String currentName = Environment.getCurrentSystemName();
		if (systemName != null) {
			Environment.setCurrentSystemName(systemName);
		}
		try {
			return getEntityService().getCount(statementId, parameterObject);
		} catch (Exception ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			Environment.setCurrentSystemName(currentName);
		}
	}

	public EntityService getEntityService() {
		if (entityService == null) {
			entityService = ContextFactory.getBean("entityService");
		}
		return entityService;
	}

	/**
	 * 获取一页数据
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param sqlExecutor
	 * @return
	 */
	public List<Object> getList(String systemName, int pageNo, int pageSize,
			SqlExecutor sqlExecutor) {
		String currentName = Environment.getCurrentSystemName();
		if (systemName != null) {
			Environment.setCurrentSystemName(systemName);
		}
		try {
			return getEntityService().getList(pageNo, pageSize, sqlExecutor);
		} catch (Exception ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			Environment.setCurrentSystemName(currentName);
		}
	}

	/**
	 * 获取数据集
	 * 
	 * @param statementId
	 * @param parameterObject
	 * @return
	 */
	public List<Object> getList(String systemName, String statementId,
			Object parameterObject) {
		String currentName = Environment.getCurrentSystemName();
		if (systemName != null) {
			Environment.setCurrentSystemName(systemName);
		}
		try {
			return getEntityService().getList(statementId, parameterObject);
		} catch (Exception ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			Environment.setCurrentSystemName(currentName);
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
		String currentName = Environment.getCurrentSystemName();
		if (systemName != null) {
			Environment.setCurrentSystemName(systemName);
		}
		try {
			return getEntityService().nextId(name);
		} catch (Exception ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			Environment.setCurrentSystemName(currentName);
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
	public long getNextId(String systemName, String tablename, String idColumn) {
		String currentName = Environment.getCurrentSystemName();
		if (systemName != null) {
			Environment.setCurrentSystemName(systemName);
		}
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("tablename", tablename);
			params.put("idColumn", idColumn);
			Long oldValue = (Long) getEntityService().getSingleObject(
					"getMaxId", params);
			return oldValue;
		} catch (Exception ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			Environment.setCurrentSystemName(currentName);
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
	public Paging getPage(String systemName, int pageNo, int pageSize,
			SqlExecutor countExecutor, SqlExecutor queryExecutor) {
		String currentName = Environment.getCurrentSystemName();
		if (systemName != null) {
			Environment.setCurrentSystemName(systemName);
		}
		try {
			return getEntityService().getPage(pageNo, pageSize, countExecutor,
					queryExecutor);
		} catch (Exception ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			Environment.setCurrentSystemName(currentName);
		}
	}

	/**
	 * 获取下一条记录编号
	 * 
	 * @return
	 */
	public int getTableUserMaxId(String systemName, String tablename,
			String idColumn, String createBy) {
		String currentName = Environment.getCurrentSystemName();
		if (systemName != null) {
			Environment.setCurrentSystemName(systemName);
		}
		try {
			int day = DateUtils.getNowYearMonthDay();
			String idLike = String.valueOf(day) + "/" + createBy + "-%";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("tablename", tablename);
			params.put("idColumn", idColumn);
			params.put("idLike", idLike);

			String str = (String) getEntityService().getSingleObject(
					"getTableUserMaxId", params);
			if (StringUtils.isNotEmpty(str) && StringUtils.contains(str, "-")) {
				str = str.substring(str.lastIndexOf("-") + 1, str.length());
				str = str.trim();
				if (StringUtils.isNumeric(str)) {
					return Integer.parseInt(str) + 1;
				}
			}

			return 1;

		} catch (Exception ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			Environment.setCurrentSystemName(currentName);
		}
	}

	/**
	 * 插入一条记录
	 * 
	 * @param statementId
	 * @param parameterObject
	 */
	public void insert(String systemName, String statementId,
			Object parameterObject) {
		String currentName = Environment.getCurrentSystemName();
		if (systemName != null) {
			Environment.setCurrentSystemName(systemName);
		}
		try {
			getEntityService().insert(statementId, parameterObject);
		} catch (Exception ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			Environment.setCurrentSystemName(currentName);
		}
	}

	/**
	 * 插入多条记录
	 * 
	 * @param statementId
	 * @param parameterObject
	 */
	public void insertAll(String systemName, String statementId,
			List<Object> rows) {
		String currentName = Environment.getCurrentSystemName();
		if (systemName != null) {
			Environment.setCurrentSystemName(systemName);
		}
		try {
			getEntityService().insertAll(statementId, rows);
		} catch (Exception ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			Environment.setCurrentSystemName(currentName);
		}
	}

	/**
	 * 根据主键获取记录
	 * 
	 * @param statementId
	 * @param parameterObject
	 * @return
	 */
	public Object selectOne(String systemName, String statementId,
			Object parameterObject) {
		String currentName = Environment.getCurrentSystemName();
		if (systemName != null) {
			Environment.setCurrentSystemName(systemName);
		}
		try {
			return getEntityService().getSingleObject(statementId,
					parameterObject);
		} catch (Exception ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			Environment.setCurrentSystemName(currentName);
		}
	}

	/**
	 * 修改一条记录
	 * 
	 * @param statementId
	 * @param parameterObject
	 */
	public void update(String systemName, String statementId,
			Object parameterObject) {
		String currentName = Environment.getCurrentSystemName();
		if (systemName != null) {
			Environment.setCurrentSystemName(systemName);
		}
		try {
			getEntityService().update(statementId, parameterObject);
		} catch (Exception ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			Environment.setCurrentSystemName(currentName);
		}
	}

	/**
	 * 修改多条记录
	 * 
	 * @param statementId
	 * @param parameterObject
	 */
	public void updateAll(String systemName, String statementId,
			List<Object> rows) {
		String currentName = Environment.getCurrentSystemName();
		if (systemName != null) {
			Environment.setCurrentSystemName(systemName);
		}
		try {
			getEntityService().updateAll(statementId, rows);
		} catch (Exception ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			Environment.setCurrentSystemName(currentName);
		}
	}

}
