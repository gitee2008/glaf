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

import com.glaf.core.base.TablePage;
import com.glaf.core.entity.SqlExecutor;
import com.glaf.core.mapper.TablePageMapper;
import com.glaf.core.query.TablePageQuery;
import com.glaf.core.service.ITablePageService;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.Paging;
import com.glaf.core.util.QueryUtils;
import com.glaf.core.util.StringTools;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.Map.Entry;

@Service("tablePageService")
@Transactional(readOnly = true)
public class TablePageServiceImpl implements ITablePageService {
	protected final static Log logger = LogFactory.getLog(TablePageServiceImpl.class);

	private SqlSession sqlSession;

	private TablePageMapper tablePageMapper;

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getListData(String sql, Map<String, Object> params) {
		if (!DBUtils.isLegalQuerySql(sql)) {
			throw new RuntimeException(" SQL statement illegal ");
		}
		if (!DBUtils.isAllowedSql(sql)) {
			throw new RuntimeException(" SQL statement illegal ");
		}

		Map<String, Object> queryMap = new HashMap<String, Object>();

		if (params != null && !params.isEmpty()) {
			queryMap.putAll(params);
			Set<Entry<String, Object>> entrySet = params.entrySet();
			for (Entry<String, Object> entry : entrySet) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (value != null) {
					if (value instanceof Collection) {
						Collection<?> collection = (Collection<?>) value;
						boolean isCollection = false;
						if (StringUtils.contains(sql, "$[" + key + "]")) {
							isCollection = true;
						}
						if (isCollection) {
							int index = 1;
							StringBuilder buff = new StringBuilder();
							buff.append(" ( ");
							Iterator<?> iterator = collection.iterator();
							while (iterator.hasNext()) {
								Object val = iterator.next();
								String nkey = "my_param_" + index;
								buff.append("#{").append(nkey).append("}");
								queryMap.put(nkey, val);
								if (iterator.hasNext()) {
									buff.append(", ");
								}
								index++;
							}
							buff.append(" ) ");
							sql = StringTools.replace(sql, "$[" + key + "]", buff.toString());
						}
					}
				}
			}
		}

		if (StringUtils.containsAny(sql, "(")) {
			SqlExecutor sqlExecutor = QueryUtils.replaceMyBatisInSQLParas(sql, params);
			if (sqlExecutor.getParameter() != null) {
				queryMap.putAll((Map<String, Object>) sqlExecutor.getParameter());
			}
			queryMap.put("queryString", sqlExecutor.getSql());
		} else {
			queryMap.putAll(Objects.requireNonNull(params));
			queryMap.put("queryString", sql);
		}

		return tablePageMapper.getSqlQueryList(queryMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getListData(String sql, Map<String, Object> params, int begin, int limit) {
		if (!DBUtils.isLegalQuerySql(sql)) {
			throw new RuntimeException(" SQL statement illegal ");
		}
		if (!DBUtils.isAllowedSql(sql)) {
			throw new RuntimeException(" SQL statement illegal ");
		}

		Map<String, Object> queryMap = new HashMap<String, Object>();

		if (params != null && !params.isEmpty()) {
			queryMap.putAll(params);
			Set<Entry<String, Object>> entrySet = params.entrySet();
			for (Entry<String, Object> entry : entrySet) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (value != null) {
					if (value instanceof Collection) {
						Collection<?> collection = (Collection<?>) value;
						boolean isCollection = false;
						if (StringUtils.contains(sql, "$[" + key + "]")) {
							isCollection = true;
						}
						if (isCollection) {
							int index = 1;
							StringBuilder buff = new StringBuilder();
							buff.append(" ( ");
							Iterator<?> iterator = collection.iterator();
							while (iterator.hasNext()) {
								Object val = iterator.next();
								String nkey = "my_param_" + index;
								buff.append("#{").append(nkey).append("}");
								queryMap.put(nkey, val);
								if (iterator.hasNext()) {
									buff.append(", ");
								}
								index++;
							}
							buff.append(" ) ");
							sql = StringTools.replace(sql, "$[" + key + "]", buff.toString());
						}
					}
				}
			}
		}

		if (StringUtils.containsAny(sql, "(")) {
			SqlExecutor sqlExecutor = QueryUtils.replaceMyBatisInSQLParas(sql, params);
			if (sqlExecutor.getParameter() != null) {
				queryMap.putAll((Map<String, Object>) sqlExecutor.getParameter());
			}
			queryMap.put("queryString", sqlExecutor.getSql());
		} else {
			queryMap.putAll(Objects.requireNonNull(params));
			queryMap.put("queryString", sql);
		}

		RowBounds rowBounds = new RowBounds(begin, limit);
		return sqlSession.selectList("getSqlQueryList", queryMap, rowBounds);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getOne(String sql, Map<String, Object> params) {
		if (!DBUtils.isLegalQuerySql(sql)) {
			throw new RuntimeException(" SQL statement illegal ");
		}
		if (!DBUtils.isAllowedSql(sql)) {
			throw new RuntimeException(" SQL statement illegal ");
		}

		Map<String, Object> queryMap = new HashMap<String, Object>();

		if (params != null && !params.isEmpty()) {
			queryMap.putAll(params);
			Set<Entry<String, Object>> entrySet = params.entrySet();
			for (Entry<String, Object> entry : entrySet) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (value != null) {
					if (value instanceof Collection) {
						Collection<?> collection = (Collection<?>) value;
						boolean isCollection = false;
						if (StringUtils.contains(sql, "$[" + key + "]")) {
							isCollection = true;
						}
						if (isCollection) {
							int index = 1;
							StringBuilder buff = new StringBuilder();
							buff.append(" ( ");
							Iterator<?> iterator = collection.iterator();
							while (iterator.hasNext()) {
								Object val = iterator.next();
								String nkey = "my_param_" + index;
								buff.append("#{").append(nkey).append("}");
								queryMap.put(nkey, val);
								if (iterator.hasNext()) {
									buff.append(", ");
								}
								index++;
							}
							buff.append(" ) ");
							sql = StringTools.replace(sql, "$[" + key + "]", buff.toString());
						}
					}
				}
			}
		}

		if (StringUtils.containsAny(sql, "(")) {
			SqlExecutor sqlExecutor = QueryUtils.replaceMyBatisInSQLParas(sql, params);
			if (sqlExecutor.getParameter() != null) {
				queryMap.putAll((Map<String, Object>) sqlExecutor.getParameter());
			}
			queryMap.put("queryString", sqlExecutor.getSql());
		} else {
			queryMap.putAll(Objects.requireNonNull(params));
			queryMap.put("queryString", sql);
		}

		List<Map<String, Object>> dataList = sqlSession.selectList("getSqlQueryList", queryMap);
		if (dataList != null && !dataList.isEmpty()) {
			return dataList.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public int getQueryCount(String sql, Map<String, Object> params) {
		if (!DBUtils.isLegalQuerySql(sql)) {
			throw new RuntimeException(" SQL statement illegal ");
		}
		if (!DBUtils.isAllowedSql(sql)) {
			throw new RuntimeException(" SQL statement illegal ");
		}

		Map<String, Object> queryMap = new HashMap<String, Object>();

		if (params != null && !params.isEmpty()) {
			queryMap.putAll(params);
			Set<Entry<String, Object>> entrySet = params.entrySet();
			for (Entry<String, Object> entry : entrySet) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (value != null) {
					if (value instanceof Collection) {
						Collection<?> collection = (Collection<?>) value;
						boolean isCollection = false;
						if (StringUtils.contains(sql, "$[" + key + "]")) {
							isCollection = true;
						}
						if (isCollection) {
							int index = 1;
							StringBuilder buff = new StringBuilder();
							buff.append(" ( ");
							Iterator<?> iterator = collection.iterator();
							while (iterator.hasNext()) {
								Object val = iterator.next();
								String nkey = "my_param_" + index;
								buff.append("#{").append(nkey).append("}");
								queryMap.put(nkey, val);
								if (iterator.hasNext()) {
									buff.append(", ");
								}
								index++;
							}
							buff.append(" ) ");
							sql = StringTools.replace(sql, "$[" + key + "]", buff.toString());
						}
					}
				}
			}
		}

		if (StringUtils.containsAny(sql, "(")) {
			SqlExecutor sqlExecutor = QueryUtils.replaceMyBatisInSQLParas(sql, params);
			if (sqlExecutor.getParameter() != null) {
				queryMap.putAll((Map<String, Object>) sqlExecutor.getParameter());
			}
			queryMap.put("queryString", sqlExecutor.getSql());
		} else {
			queryMap.putAll(Objects.requireNonNull(params));
			queryMap.put("queryString", sql);
		}

		return tablePageMapper.getSqlQueryCount(queryMap);
	}

	public int getTableCount(TablePageQuery query) {
		return tablePageMapper.getTableCount(query);
	}

	public List<Map<String, Object>> getTableData(String tableName, int firstResult, int maxResults) {
		TablePageQuery query = new TablePageQuery();
		query.tableName(tableName);
		int begin = query.getFirstResult();
		int pageSize = query.getMaxResults();
		if (begin < 0) {
			begin = 0;
		}
		if (pageSize <= 0) {
			pageSize = Paging.DEFAULT_PAGE_SIZE;
		}
		RowBounds rowBounds = new RowBounds(begin, pageSize);
		return sqlSession.selectList("getTableData", query, rowBounds);
	}

	@Transactional(readOnly = true)
	public List<Map<String, Object>> getTableData(TablePageQuery query) {
		int begin = query.getFirstResult();
		int pageSize = query.getMaxResults();
		if (begin < 0) {
			begin = 0;
		}
		if (pageSize <= 0) {
			pageSize = Paging.DEFAULT_PAGE_SIZE;
		}
		RowBounds rowBounds = new RowBounds(begin, pageSize);
		return sqlSession.selectList("getTableData", query, rowBounds);
	}

	public TablePage getTablePage(TablePageQuery query, int firstResult, int maxResults) {
		TablePage tablePage = new TablePage();
		String tableName = query.getTableName();
		tablePage.setTableName(tableName);
		int begin = query.getFirstResult();
		int pageSize = query.getMaxResults();
		if (begin < 0) {
			begin = 0;
		}
		if (pageSize <= 0) {
			pageSize = Paging.DEFAULT_PAGE_SIZE;
		}

		Integer count = sqlSession.selectOne("getTableCount", query);
		if (count > 0) {
			tablePage.setTotal(count);
			RowBounds rowBounds = new RowBounds(begin, pageSize);
			List<Map<String, Object>> tableData = sqlSession.selectList("getTableData", query, rowBounds);
			tablePage.setRows(tableData);
			tablePage.setFirstResult(firstResult);
		}

		return tablePage;
	}

	public Paging getTablePaging(TablePageQuery query, int firstResult, int maxResults) {
		Paging paging = new Paging();
		String tableName = query.getTableName();
		int begin = query.getFirstResult();
		int pageSize = query.getMaxResults();
		if (begin < 0) {
			begin = 0;
		}
		if (pageSize <= 0) {
			pageSize = Paging.DEFAULT_PAGE_SIZE;
		}

		Integer count = sqlSession.selectOne("getTableCount", Collections.singletonMap("tableName", tableName));
		if (count > 0) {
			paging.setTotal(count);
			RowBounds rowBounds = new RowBounds(begin, pageSize);
			List<Object> tableData = sqlSession.selectList("getTableData", query, rowBounds);
			paging.setRows(tableData);
			int currentPage = begin / pageSize + 1;
			paging.setCurrentPage(currentPage);
		}

		return paging;
	}

	@javax.annotation.Resource
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@javax.annotation.Resource
	public void setTablePageMapper(TablePageMapper tablePageMapper) {
		this.tablePageMapper = tablePageMapper;
	}

}