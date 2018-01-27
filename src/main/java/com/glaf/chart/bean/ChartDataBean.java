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

package com.glaf.chart.bean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.chart.domain.Chart;
import com.glaf.chart.service.IChartService;
import com.glaf.chart.util.ChartCache;
import com.glaf.core.base.ColumnModel;
import com.glaf.core.base.Parameter;
import com.glaf.core.base.SqlParameter;
import com.glaf.core.base.TableModel;
import com.glaf.core.config.DatabaseConnectionConfig;
import com.glaf.core.config.Environment;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.domain.Database;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.service.EntityService;
import com.glaf.core.service.ITableDataService;
import com.glaf.core.service.ITablePageService;
import com.glaf.core.util.QueryUtils;
import com.glaf.core.util.StringTools;

import com.glaf.matrix.data.domain.SqlDefinition;
import com.glaf.matrix.data.service.SqlDefinitionService;

public class ChartDataBean {
	protected final static Log logger = LogFactory.getLog(ChartDataBean.class);

	protected IChartService chartService;

	protected EntityService entityService;

	protected SqlDefinitionService sqlDefinitionService;

	protected ITableDataService tableDataService;

	protected ITablePageService tablePageService;

	@SuppressWarnings("unchecked")
	protected void fetchData(Chart chart, String statementId, Map<String, Object> paramMap, String actorId) {

		if (StringUtils.isNumeric(SystemConfig.getCurrentYYYYMM())) {
			paramMap.put("curr_yyyymm", Integer.parseInt(SystemConfig.getCurrentYYYYMM()));
		}
		if (StringUtils.isNumeric(SystemConfig.getCurrentYYYYMMDD())) {
			paramMap.put("curr_yyyymmdd", Integer.parseInt(SystemConfig.getCurrentYYYYMMDD()));
		}

		// querySQL = QueryUtils.replaceSQLParas(querySQL, paramMap);

		logger.debug("##paramMap=" + paramMap);

		Map<String, Parameter> params = new HashMap<String, Parameter>();

		StringBuffer buffer = new StringBuffer(200);
		String datetimeStr = null;
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmm");
		Date date = new Date();
		datetimeStr = f.format(date);
		buffer.append(datetimeStr).append("_").append(chart.getId()).append("_").append(statementId);
		Set<Entry<String, Object>> entrySet = paramMap.entrySet();
		for (Entry<String, Object> entry : entrySet) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (value instanceof String) {
				Parameter param = new SqlParameter();
				param.setName(key);
				param.setStringVal((String) value);
				param.setAutoTypeConvert(true);
				params.put(key, param);
				buffer.append("_").append(key).append("_").append(value);
			}
		}

		LoginContext loginContext = IdentityFactory.getLoginContext(actorId);

		String cacheKey = org.apache.commons.codec.digest.DigestUtils.md5Hex(buffer.toString());
		//List<ColumnModel> columns = null;// ChartCache.get(loginContext.getTenantId(), cacheKey);

		//if (columns != null && !columns.isEmpty()) {
		//	chart.setColumns(columns);
		//	logger.debug("get chart data from guava cache.");
		//	return;
		//}

		logger.debug("statementId:" + statementId);
		logger.debug("params:" + params);

		DatabaseConnectionConfig config = new DatabaseConnectionConfig();
		Long databaseId = chart.getDatabaseId();

		Database currentDB = config.getDatabase(loginContext, databaseId);
		String systemName = Environment.getCurrentSystemName();
		try {
			if (currentDB != null) {
				Environment.setCurrentSystemName(currentDB.getName());
			}
			List<Object> rows = getEntityService().getList(statementId, params);
			if (rows != null && !rows.isEmpty()) {
				logger.debug(rows);
				int index = 0;
				Map<String, Object> dataMap = new java.util.HashMap<String, Object>();
				for (Object object : rows) {
					if (object == null) {
						continue;
					}
					Map<String, Object> rowMap = (Map<String, Object>) object;
					dataMap.clear();
					Set<Entry<String, Object>> entrySet2 = rowMap.entrySet();
					for (Entry<String, Object> entry : entrySet2) {
						String key = entry.getKey();
						Object value = entry.getValue();
						dataMap.put(key, value);
						dataMap.put(key.toLowerCase(), value);
					}
					index++;
					ColumnModel cell = new ColumnModel();
					cell.setColumnName("col_" + index);

					if (StringUtils.isNotEmpty(MapUtils.getString(dataMap, "category"))) {
						cell.setCategory(MapUtils.getString(dataMap, "category"));
					} else if (StringUtils.isNotEmpty(MapUtils.getString(dataMap, "c"))) {
						cell.setCategory(MapUtils.getString(dataMap, "c"));
					}

					if (StringUtils.isNotEmpty(MapUtils.getString(dataMap, "series"))) {
						cell.setSeries(MapUtils.getString(dataMap, "series"));
					} else if (StringUtils.isNotEmpty(MapUtils.getString(dataMap, "s"))) {
						cell.setSeries(MapUtils.getString(dataMap, "s"));
					}

					if (MapUtils.getDouble(dataMap, "doublevalue") != null) {
						cell.setDoubleValue(MapUtils.getDouble(dataMap, "doublevalue"));
					} else if (MapUtils.getDouble(dataMap, "value") != null) {
						cell.setDoubleValue(MapUtils.getDouble(dataMap, "value"));
					} else if (MapUtils.getDouble(dataMap, "v") != null) {
						cell.setDoubleValue(MapUtils.getDouble(dataMap, "v"));
					}
					chart.addCellData(cell);
				}
				if (chart.getColumns() != null && !chart.getColumns().isEmpty()) {
					ChartCache.put(loginContext.getTenantId(), cacheKey, chart.getColumns());
					logger.debug("rows size:" + chart.getColumns().size());
				}
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
			logger.error(ex);
		} finally {
			Environment.setCurrentSystemName(systemName);
		}
	}

	protected void fetchData(Chart chart, TableModel rowMode, String querySQL, Map<String, Object> paramMap,
			String actorId) {
		if (StringUtils.isNotEmpty(querySQL)) {
			if (StringUtils.isNumeric(SystemConfig.getCurrentYYYYMM())) {
				paramMap.put("curr_yyyymm", Integer.parseInt(SystemConfig.getCurrentYYYYMM()));
			}
			if (StringUtils.isNumeric(SystemConfig.getCurrentYYYYMMDD())) {
				paramMap.put("curr_yyyymmdd", Integer.parseInt(SystemConfig.getCurrentYYYYMMDD()));
			}
			querySQL = QueryUtils.replaceSQLVars(querySQL);
			// querySQL = QueryUtils.replaceSQLParas(querySQL, paramMap);

			logger.debug("paramMap=" + paramMap);
			logger.debug("querySQL=" + querySQL);

			rowMode.setSql(querySQL);
			DatabaseConnectionConfig config = new DatabaseConnectionConfig();
			Long databaseId = chart.getDatabaseId();
			LoginContext loginContext = IdentityFactory.getLoginContext(actorId);
			Database currentDB = config.getDatabase(loginContext, databaseId);
			String systemName = Environment.getCurrentSystemName();
			try {
				if (currentDB != null) {
					Environment.setCurrentSystemName(currentDB.getName());
				}
				List<Map<String, Object>> rows = getTablePageService().getListData(querySQL, paramMap);
				if (rows != null && !rows.isEmpty()) {
					logger.debug(rows);
					int index = 0;
					Map<String, Object> dataMap = new java.util.HashMap<String, Object>();
					for (Map<String, Object> rowMap : rows) {
						dataMap.clear();
						Set<Entry<String, Object>> entrySet = rowMap.entrySet();
						for (Entry<String, Object> entry : entrySet) {
							String key = entry.getKey();
							Object value = entry.getValue();
							dataMap.put(key, value);
							dataMap.put(key.toLowerCase(), value);
						}
						index++;
						ColumnModel cell = new ColumnModel();
						cell.setColumnName("col_" + index);

						if (StringUtils.isNotEmpty(MapUtils.getString(dataMap, "category"))) {
							cell.setCategory(MapUtils.getString(dataMap, "category"));
						} else if (StringUtils.isNotEmpty(MapUtils.getString(dataMap, "c"))) {
							cell.setCategory(MapUtils.getString(dataMap, "c"));
						}

						if (StringUtils.isNotEmpty(MapUtils.getString(dataMap, "series"))) {
							cell.setSeries(MapUtils.getString(dataMap, "series"));
						} else if (StringUtils.isNotEmpty(MapUtils.getString(dataMap, "s"))) {
							cell.setSeries(MapUtils.getString(dataMap, "s"));
						}

						if (MapUtils.getDouble(dataMap, "doublevalue") != null) {
							cell.setDoubleValue(MapUtils.getDouble(dataMap, "doublevalue"));
						} else if (MapUtils.getDouble(dataMap, "value") != null) {
							cell.setDoubleValue(MapUtils.getDouble(dataMap, "value"));
						} else if (MapUtils.getDouble(dataMap, "v") != null) {
							cell.setDoubleValue(MapUtils.getDouble(dataMap, "v"));
						}
						chart.addCellData(cell);
					}
					logger.debug("rows size:" + chart.getColumns().size());
				}
			} catch (Exception ex) {
				// ex.printStackTrace();
				logger.error(ex);
			} finally {
				Environment.setCurrentSystemName(systemName);
			}
		}
	}

	/**
	 * 获取图表定义并取数
	 * 
	 * @param name
	 * @return
	 */
	public Chart getChartAndFetchDataById(String id, Map<String, Object> paramMap, String actorId) {
		Chart chart = getChartService().getChart(id);
		if (chart != null) {
			TableModel rowMode = new TableModel();
			String querySQL = null;
			if (StringUtils.isNotEmpty(chart.getQueryIds())) {
				List<String> queryIds = StringTools.split(chart.getQueryIds());
				for (String queryId : queryIds) {
					SqlDefinition sqlDefinition = getSqlDefinitionService().getSqlDefinition(Long.parseLong(queryId));
					if (sqlDefinition != null && StringUtils.isNotEmpty(sqlDefinition.getSql())) {
						logger.debug("query title=" + sqlDefinition.getTitle());
						querySQL = sqlDefinition.getSql();
						this.fetchData(chart, rowMode, querySQL, paramMap, actorId);
					}
				}
			} else if (StringUtils.isNotEmpty(chart.getStatementId())) {
				this.fetchData(chart, chart.getStatementId(), paramMap, actorId);
			}
			logger.debug("columns size:" + chart.getColumns().size());
		}

		return chart;
	}

	public IChartService getChartService() {
		if (chartService == null) {
			chartService = ContextFactory.getBean("chartService");
		}
		return chartService;
	}

	public EntityService getEntityService() {
		if (entityService == null) {
			entityService = ContextFactory.getBean("entityService");
		}
		return entityService;
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

	public void setChartService(IChartService chartService) {
		this.chartService = chartService;
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
