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

package com.glaf.heathcare.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.core.base.BaseItem;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.entity.SqlExecutor;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.service.EntityService;
import com.glaf.core.service.ITablePageService;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.LowerLinkedMap;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;

import com.glaf.matrix.data.domain.SysTable;
import com.glaf.matrix.data.domain.TableColumn;
import com.glaf.matrix.data.factory.DataItemFactory;
import com.glaf.matrix.data.helper.SqlCriteriaHelper;
import com.glaf.matrix.data.service.ITableService;
import com.glaf.matrix.data.service.SqlCriteriaService;
import com.glaf.matrix.data.service.TableCorrelationService;
import com.glaf.matrix.data.service.TableSysPermissionService;

public class TableDataBean {
	protected final static Log logger = LogFactory.getLog(TableDataBean.class);

	protected EntityService entityService;

	protected ITableService tableService;

	protected ITablePageService tablePageService;

	protected SqlCriteriaService sqlCriteriaService;

	protected TableCorrelationService tableCorrelationService;

	protected TableSysPermissionService tableSysPermissionService;

	public EntityService getEntityService() {
		if (entityService == null) {
			entityService = ContextFactory.getBean("entityService");
		}
		return entityService;
	}

	public SqlCriteriaService getSqlCriteriaService() {
		if (sqlCriteriaService == null) {
			sqlCriteriaService = ContextFactory.getBean("sqlCriteriaService");
		}
		return sqlCriteriaService;
	}

	public TableCorrelationService getTableCorrelationService() {
		if (tableCorrelationService == null) {
			tableCorrelationService = ContextFactory.getBean("tableCorrelationService");
		}
		return tableCorrelationService;
	}

	public ITablePageService getTablePageService() {
		if (tablePageService == null) {
			tablePageService = ContextFactory.getBean("tablePageService");
		}
		return tablePageService;
	}

	public ITableService getTableService() {
		if (tableService == null) {
			tableService = ContextFactory.getBean("tableService");
		}
		return tableService;
	}

	public TableSysPermissionService getTableSysPermissionService() {
		if (tableSysPermissionService == null) {
			tableSysPermissionService = ContextFactory.getBean("tableSysPermissionService");
		}
		return tableSysPermissionService;
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJson(HttpServletRequest request) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug("params:" + params);
		JSONObject result = new JSONObject();
		String tableId = request.getParameter("tableId");
		if (StringUtils.isNotEmpty(tableId)) {
			SysTable sysTable = getTableService().getSysTableById(tableId);
			if (sysTable != null) {
				List<TableColumn> columns = getTableService().getTableColumnsByTableId(tableId);
				if (columns != null && !columns.isEmpty()) {
					List<String> list = new ArrayList<String>();
					Map<String, TableColumn> columnMap = new HashMap<String, TableColumn>();
					for (TableColumn column : columns) {
						columnMap.put(column.getId(), column);
						columnMap.put(column.getColumnName().toLowerCase(), column);
						if (column.getDisplayType() == 2 || column.getDisplayType() == 4) {
							list.add(column.getColumnName().toLowerCase());
						}
						if (StringUtils.isNotEmpty(column.getDataCode())) {
							if (StringUtils.startsWith(column.getDataCode(), "@sys_dict:")) {
								long nodeId = Long
										.parseLong(column.getDataCode().substring(10, column.getDataCode().length()));
								List<BaseItem> items = DataItemFactory.getInstance().getDictoryItems(nodeId);
								column.setItems(items);
								if (items != null && !items.isEmpty()) {
									for (BaseItem item : items) {
										column.addProperty(item.getValue(), item.getName());
									}
								}
							} else if (StringUtils.startsWith(column.getDataCode(), "@sql:")) {
								String rowId = column.getDataCode().substring(5, column.getDataCode().length());
								List<BaseItem> items = DataItemFactory.getInstance().getSqlDataItems(loginContext,
										Long.parseLong(rowId), params);
								column.setItems(items);
								if (items != null && !items.isEmpty()) {
									for (BaseItem item : items) {
										column.addProperty(item.getValue(), item.getName());
									}
								}
							} else if (StringUtils.startsWith(column.getDataCode(), "@table:")) {
								String rowId = column.getDataCode().substring(7, column.getDataCode().length());
								List<BaseItem> items = DataItemFactory.getInstance().getTableItems(loginContext, rowId,
										params);
								column.setItems(items);
								if (items != null && !items.isEmpty()) {
									for (BaseItem item : items) {
										column.addProperty(item.getValue(), item.getName());
									}
								}
							}
						}
					}

					int start = 0;
					int limit = 10;
					String orderName = null;
					String order = null;

					int pageNo = ParamUtils.getInt(params, "page");
					limit = ParamUtils.getInt(params, "rows");
					start = (pageNo - 1) * limit;
					orderName = ParamUtils.getString(params, "sortName");
					order = ParamUtils.getString(params, "sortOrder");

					if (start < 0) {
						start = 0;
					}

					if (limit <= 0) {
						limit = Paging.DEFAULT_PAGE_SIZE;
					}

					if (request.getAttribute("exportXls") != null) {
						limit = 50000;
					}

					String tableName = sysTable.getTableName();

					if (StringUtils.equals(sysTable.getPartitionFlag(), "Y")) {
						tableName = tableName
								+ String.valueOf(IdentityFactory.getTenantHash(loginContext.getTenantId()));
					}

					StringBuilder sqlBuffer = new StringBuilder();
					sqlBuffer.append(" from ").append(tableName).append(" E ");
					sqlBuffer.append(" where E.TENANTID_ = '").append(loginContext.getTenantId()).append("' ");

					SqlCriteriaHelper helper = new SqlCriteriaHelper();
					SqlExecutor sqlExecutor = helper.buildMyBatisSql(sysTable.getTableId(), sysTable.getTableName(),
							"E", params);
					if (sqlExecutor.getSql() != null && sqlExecutor.getParameter() != null) {
						sqlBuffer.append(sqlExecutor.getSql());
						params.putAll((Map<String, Object>) sqlExecutor.getParameter());
					}

					try {
						int total = getTablePageService().getQueryCount(" select count(*) " + sqlBuffer.toString(),
								params);
						if (total > 0) {
							if (orderName != null && list.contains(orderName.trim().toLowerCase())) {
								sqlBuffer.append(" order by E.").append(orderName);
								if (StringUtils.equalsIgnoreCase(order, "desc")) {
									sqlBuffer.append(" desc ");
								}
							}
							List<Map<String, Object>> dataList = getTablePageService()
									.getListData(" select E.* " + sqlBuffer.toString(), params, start, limit);
							if (dataList != null && !dataList.isEmpty()) {
								JSONArray rowsJSON = new JSONArray();
								Map<String, Object> dataMap = null;
								Date date = null;
								for (Map<String, Object> rowMap : dataList) {
									dataMap = new LowerLinkedMap();
									dataMap.putAll(rowMap);
									JSONObject json = new JSONObject();
									for (String columnName : list) {
										if (StringUtils.equalsIgnoreCase("id_", columnName)) {
											json.put("id", dataMap.get(columnName));
										}
										if (StringUtils.equalsIgnoreCase("uuid_", columnName)) {
											json.put("uuid", dataMap.get(columnName));
										}
										TableColumn col = columnMap.get(columnName);
										if (col != null && col.getJavaType() != null) {
											switch (col.getJavaType()) {
											case "Date":
												date = ParamUtils.getDate(dataMap, columnName);
												if (date != null) {
													json.put(columnName, DateUtils.getDateTime(date));
												}
												break;
											default:
												if (col.getProperties() != null && !col.getProperties().isEmpty()) {
													json.put(columnName,
															col.getProperties().get(dataMap.get(columnName)));
												} else {
													json.put(columnName, dataMap.get(columnName));
												}
												break;
											}
										} else {
											json.put(columnName, dataMap.get(columnName));
										}
									}
									rowsJSON.add(json);
								}
								result.put("rows", rowsJSON);
							}
						}

						result.put("total", total);
						result.put("totalCount", total);
						result.put("totalRecords", total);
						result.put("start", start);
						result.put("startIndex", start);
						result.put("limit", limit);
						result.put("pageSize", limit);

					} catch (Exception ex) {
						logger.error(ex);
					}
				}
			}
		}
		// logger.debug(result.toJSONString());
		return result;
	}

}
