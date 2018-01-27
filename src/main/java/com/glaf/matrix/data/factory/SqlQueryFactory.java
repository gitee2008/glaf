/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.glaf.matrix.data.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.core.config.DatabaseConnectionConfig;
import com.glaf.core.config.Environment;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.domain.Database;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.service.ITableDataService;
import com.glaf.core.service.ITablePageService;
import com.glaf.matrix.data.domain.SqlDefinition;
import com.glaf.matrix.data.service.SqlDefinitionService;

public class SqlQueryFactory {
	private static class SqlQueryHolder {
		public static SqlQueryFactory instance = new SqlQueryFactory();
	}

	protected final static Log logger = LogFactory.getLog(SqlQueryFactory.class);

	public static SqlQueryFactory getInstance() {
		return SqlQueryHolder.instance;
	}

	protected ITableDataService tableDataService;

	protected ITablePageService tablePageService;

	protected SqlDefinitionService sqlDefinitionService;

	private SqlQueryFactory() {

	}

	public JSONArray getArray(long sqlDefId, long databaseId, String actorId, Map<String, Object> params) {
		JSONArray array = new JSONArray();
		SqlDefinition sqlDefinition = getSqlDefinitionService().getSqlDefinition(sqlDefId);
		if (sqlDefinition != null) {
			DatabaseConnectionConfig config = new DatabaseConnectionConfig();
			LoginContext loginContext = IdentityFactory.getLoginContext(actorId);
			String systemName = Environment.getCurrentSystemName();
			try {
				if (databaseId > 0) {
					Database currentDB = config.getDatabase(loginContext, databaseId);
					if (currentDB != null) {
						Environment.setCurrentSystemName(currentDB.getName());
					}
				}
				StringBuilder sqlBuffer = new StringBuilder();
				sqlBuffer.append(sqlDefinition.getSql());

				String querySQL = sqlBuffer.toString();
				List<Map<String, Object>> rows = getTablePageService().getListData(querySQL, params);
				if (rows != null && !rows.isEmpty()) {
					JSONObject json = null;
					for (Map<String, Object> rowMap : rows) {
						json = new JSONObject();
						Set<Entry<String, Object>> entrySet = rowMap.entrySet();
						for (Entry<String, Object> entry : entrySet) {
							String key = entry.getKey();
							Object value = entry.getValue();
							json.put(key, value);
							json.put(key.toLowerCase(), value);
						}
						array.add(json);
					}
				}
			} catch (Exception ex) {
				// ex.printStackTrace();
				logger.error(ex);
			} finally {
				Environment.setCurrentSystemName(systemName);
			}
		}
		return array;
	}

	public Map<String, Object> getDataMap(long sqlDefId, long databaseId, String actorId, Map<String, Object> params,
			int start, int limit) {
		Map<String, Object> result = new HashMap<String, Object>();
		SqlDefinition sqlDefinition = getSqlDefinitionService().getSqlDefinition(sqlDefId);
		if (sqlDefinition != null) {
			DatabaseConnectionConfig config = new DatabaseConnectionConfig();
			LoginContext loginContext = IdentityFactory.getLoginContext(actorId);
			String systemName = Environment.getCurrentSystemName();
			try {
				if (databaseId > 0) {
					Database currentDB = config.getDatabase(loginContext, databaseId);
					if (currentDB != null) {
						Environment.setCurrentSystemName(currentDB.getName());
					}
				}
				StringBuilder sqlBuffer = new StringBuilder();
				sqlBuffer.append(sqlDefinition.getSql());
				String querySQL = sqlBuffer.toString();
				int total = getTablePageService().getQueryCount(querySQL, params);
				if (total > 0) {
					List<Map<String, Object>> rows = getTablePageService().getListData(querySQL, params, start, limit);
					if (rows != null && !rows.isEmpty()) {
						result.put("start", start);
						result.put("limit", limit);
						result.put("total", total);
						result.put("rows", rows);
					}
				}
			} catch (Exception ex) {
				// ex.printStackTrace();
				logger.error(ex);
			} finally {
				Environment.setCurrentSystemName(systemName);
			}
		}
		return result;
	}

	public JSONObject getJson(long sqlDefId, long databaseId, String actorId, Map<String, Object> params, int start,
			int limit) {
		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();
		SqlDefinition sqlDefinition = getSqlDefinitionService().getSqlDefinition(sqlDefId);
		if (sqlDefinition != null) {
			DatabaseConnectionConfig config = new DatabaseConnectionConfig();
			LoginContext loginContext = IdentityFactory.getLoginContext(actorId);
			String systemName = Environment.getCurrentSystemName();
			try {
				if (databaseId > 0) {
					Database currentDB = config.getDatabase(loginContext, databaseId);
					if (currentDB != null) {
						Environment.setCurrentSystemName(currentDB.getName());
					}
				}
				StringBuilder sqlBuffer = new StringBuilder();
				sqlBuffer.append(sqlDefinition.getSql());
				String querySQL = sqlBuffer.toString();
				int total = getTablePageService().getQueryCount(querySQL, params);
				if (total > 0) {
					List<Map<String, Object>> rows = getTablePageService().getListData(querySQL, params, start, limit);
					if (rows != null && !rows.isEmpty()) {
						JSONObject json = null;
						for (Map<String, Object> rowMap : rows) {
							json = new JSONObject();
							Set<Entry<String, Object>> entrySet = rowMap.entrySet();
							for (Entry<String, Object> entry : entrySet) {
								String key = entry.getKey();
								Object value = entry.getValue();
								json.put(key, value);
								json.put(key.toLowerCase(), value);
							}
							array.add(json);
						}
						result.put("start", start);
						result.put("limit", limit);
						result.put("total", total);
						result.put("rows", array);
					}
				}
			} catch (Exception ex) {
				// ex.printStackTrace();
				logger.error(ex);
			} finally {
				Environment.setCurrentSystemName(systemName);
			}
		}
		return result;
	}

	public SqlDefinitionService getSqlDefinitionService() {
		if (sqlDefinitionService == null) {
			sqlDefinitionService = ContextFactory.getBean("sqlDefinitionService");
		}
		return sqlDefinitionService;
	}

	public ITableDataService getTableDataService() {
		if (tableDataService == null) {
			tableDataService = ContextFactory.getBean("tableDataService");
		}
		return tableDataService;
	}

	public ITablePageService getTablePageService() {
		if (tablePageService == null) {
			tablePageService = ContextFactory.getBean("tablePageService");
		}
		return tablePageService;
	}

	public void setSqlDefinitionService(SqlDefinitionService sqlDefinitionService) {
		this.sqlDefinitionService = sqlDefinitionService;
	}

	public void setTableDataService(ITableDataService tableDataService) {
		this.tableDataService = tableDataService;
	}

	public void setTablePageService(ITablePageService tablePageService) {
		this.tablePageService = tablePageService;
	}

}
