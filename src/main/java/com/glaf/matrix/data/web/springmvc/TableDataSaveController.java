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

package com.glaf.matrix.data.web.springmvc;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.domain.TableDefinition;
import com.glaf.core.security.LoginContext;
import com.glaf.core.service.EntityService;
import com.glaf.core.service.ITableDataService;
import com.glaf.core.service.ITablePageService;
import com.glaf.core.util.Constants;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.LowerLinkedMap;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.matrix.data.bean.TableDataBean;
import com.glaf.matrix.data.domain.DataModel;
import com.glaf.matrix.data.domain.SysTable;
import com.glaf.matrix.data.domain.TableColumn;
import com.glaf.matrix.data.service.ITableService;
import com.glaf.matrix.data.service.TableCorrelationService;
import com.glaf.matrix.data.util.TableDomainFactory;

@Controller("/tableDataSave")
@RequestMapping("/tableDataSave")
public class TableDataSaveController {

	protected static final Log logger = LogFactory.getLog(TableDataSaveController.class);

	protected EntityService entityService;

	protected ITableDataService tableDataService;

	protected ITableService tableService;

	protected ITablePageService tablePageService;

	protected TableCorrelationService tableCorrelationService;

	@ResponseBody
	@RequestMapping("/audit")
	public byte[] audit(HttpServletRequest request) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String tableId = request.getParameter("tableId");
		if (StringUtils.isNotEmpty(tableId)) {
			SysTable sysTable = tableService.getSysTableById(tableId);
			if (sysTable != null) {
				TableDataBean tableDataBean = new TableDataBean();
				if (!tableDataBean.hasPermission(loginContext, sysTable, Constants.PRIVILEGE_AUDIT)) {
					return ResponseUtils.responseJsonResult(false, "您没有审核权限，请联系您的管理员授权。");
				}
				List<TableColumn> columns = tableService.getTableColumnsByTableId(tableId);
				if (columns != null && !columns.isEmpty()) {
					List<String> list = new ArrayList<String>();
					List<String> useradd_list = new ArrayList<String>();
					Map<String, String> columnMap = new HashMap<String, String>();
					list.add("id_");
					list.add("parentid_");
					list.add("treeid_");
					list.add("uuid_");
					list.add("name_");
					list.add("code_");
					list.add("title_");
					list.add("type_");
					list.add("level_");
					list.add("sortno_");
					list.add("locked_");
					list.add("organizationid_");
					list.add("tenantid_");
					list.add("createby_");
					list.add("createtime_");
					list.add("updateby_");
					list.add("updatetime_");

					for (TableColumn column : columns) {
						list.add(column.getColumnName().toLowerCase());
						useradd_list.add(column.getColumnName().toLowerCase());
						columnMap.put(column.getColumnName().toLowerCase(), column.getId());
					}

					TableDefinition table = TableDomainFactory.getTableDefinition(sysTable.getTableName());

					List<TableColumn> extColumns = new ArrayList<TableColumn>();
					if (table.getColumns() != null && !table.getColumns().isEmpty()) {
						for (ColumnDefinition col : table.getColumns()) {
							TableColumn c = new TableColumn();
							try {
								org.apache.commons.beanutils.PropertyUtils.copyProperties(c, col);
							} catch (Exception e) {

							}
							c.setColumnName(col.getColumnName());
							c.setJavaType(col.getJavaType());
							c.setLength(col.getLength());
							extColumns.add(c);
						}
					}

					sysTable.getColumns().addAll(extColumns);

					LowerLinkedMap dataMap = new LowerLinkedMap();

					Map<String, Object> rowMap = null;
					String uuid = request.getParameter("uuid");
					if (StringUtils.isNotEmpty(uuid)) {
						rowMap = tableDataBean.getSimpleRowMap(loginContext, sysTable, uuid);
					}
					if (rowMap != null && !rowMap.isEmpty()) {
						dataMap.putAll(rowMap);
						dataMap.put("id_", rowMap.get("id"));
						dataMap.put("uuid_", uuid);
						boolean approval = RequestUtils.getBoolean(request, "approval");
						String comment = request.getParameter("comment");
						tableDataBean.audit(loginContext, sysTable, dataMap, approval, comment);
						return ResponseUtils.responseResult(true);
					}
				}
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@ResponseBody
	@RequestMapping("/saveData")
	public byte[] saveData(HttpServletRequest request) throws IOException {
		logger.debug(RequestUtils.getParameterMap(request));
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String tableId = request.getParameter("tableId");
		if (StringUtils.isNotEmpty(tableId)) {
			SysTable sysTable = tableService.getSysTableById(tableId);
			if (sysTable != null) {
				TableDataBean tableDataBean = new TableDataBean();
				if (!tableDataBean.hasPermission(loginContext, sysTable, Constants.PRIVILEGE_READ_WRITE)) {
					return ResponseUtils.responseJsonResult(false, "您没有操作权限，请联系您的管理员授权。");
				}
				List<TableColumn> columns = tableService.getTableColumnsByTableId(tableId);
				if (columns != null && !columns.isEmpty()) {
					List<String> list = new ArrayList<String>();
					List<String> useradd_list = new ArrayList<String>();
					Map<String, String> columnMap = new HashMap<String, String>();
					list.add("id_");
					list.add("parentid_");
					list.add("treeid_");
					list.add("uuid_");
					list.add("name_");
					list.add("code_");
					list.add("title_");
					list.add("type_");
					list.add("level_");
					list.add("sortno_");
					list.add("locked_");
					list.add("organizationid_");
					list.add("tenantid_");
					list.add("createby_");
					list.add("createtime_");
					list.add("updateby_");
					list.add("updatetime_");

					for (TableColumn column : columns) {
						list.add(column.getColumnName().toLowerCase());
						useradd_list.add(column.getColumnName().toLowerCase());
						columnMap.put(column.getColumnName().toLowerCase(), column.getId());
					}

					TableDefinition table = TableDomainFactory.getTableDefinition(sysTable.getTableName());
					List<TableColumn> extColumns = new ArrayList<TableColumn>();
					if (table.getColumns() != null && !table.getColumns().isEmpty()) {
						for (ColumnDefinition col : table.getColumns()) {
							TableColumn c = new TableColumn();
							try {
								org.apache.commons.beanutils.PropertyUtils.copyProperties(c, col);
							} catch (Exception e) {

							}
							c.setColumnName(col.getColumnName());
							c.setJavaType(col.getJavaType());
							c.setLength(col.getLength());
							extColumns.add(c);
						}
					}
					sysTable.getColumns().addAll(extColumns);

					Map<String, Object> dataMap = RequestUtils.getParameterMap(request);

					boolean isInsert = false;

					String masterDataJson = request.getParameter("masterDataJson");
					if (StringUtils.isNotEmpty(masterDataJson)) {
						JSONObject masterJson = JSON.parseObject(masterDataJson);
						if (useradd_list.size() > 0) {
							for (String name : useradd_list) {
								Object value = masterJson.get(columnMap.get(name));
								dataMap.put(name, value);
							}
						}
					} else {
						DataModel dataModel = null;
						String uuid = request.getParameter("uuid");
						if (StringUtils.isNotEmpty(uuid)) {
							dataModel = tableDataBean.getDataModel(loginContext, sysTable, uuid);
						}
						if (dataModel != null) {
							dataMap.putAll(dataModel.getDataMap());
							dataMap.put("id_", dataModel.getId());
							dataMap.put("uuid_", uuid);
						} else {
							isInsert = true;
						}
						if (useradd_list.size() > 0) {
							dataMap.put("topid_", RequestUtils.getLong(request, "topId"));
							for (String name : useradd_list) {
								dataMap.put(name, request.getParameter(columnMap.get(name)));
							}
						}
					}

					logger.debug("save data: " + dataMap);

					boolean hasUpdatePermission = false;
					boolean canUpdate = false;

					Date createDate = ParamUtils.getDate(dataMap, "createtime_");

					if (loginContext.isSystemAdministrator()) {
						hasUpdatePermission = true;
					} else {
						if (StringUtils.equals(loginContext.getActorId(), ParamUtils.getString(dataMap, "createby_"))) {
							hasUpdatePermission = true;
						}
						if (loginContext.getRoles() != null && loginContext.getRoles().contains("TenantAdmin")) {
							if (StringUtils.equals(loginContext.getUser().getTenantId(),
									ParamUtils.getString(dataMap, "tenantid_"))) {
								hasUpdatePermission = true;
							}
						}
					}

					if (hasUpdatePermission && createDate != null) {
						switch (sysTable.getUpdateCascade()) {
						case 0:// 可以修改
							canUpdate = true;
							break;
						case 1:// 可以修改
							canUpdate = true;
							break;
						case 2:// 不能修改
							canUpdate = false;
							break;
						case 3:// 当天可以修改
							if (DateUtils.getNowYearMonthDay() == DateUtils.getYearMonthDay(createDate)) {
								canUpdate = true;
							}
							break;
						case 4:// 两周内可以修改
							if (DateUtils.getDaysBetween(createDate, new Date()) <= 14) {
								canUpdate = true;
							}
							break;
						case 5:// 当月内可修改
							if (DateUtils.getNowYearMonth() == DateUtils.getYearMonth(createDate)) {
								canUpdate = true;
							}
							break;
						default:
							canUpdate = false;
							break;
						}
					} else {
						canUpdate = false;
						if (dataMap.get("uuid_") == null) {
							isInsert = true;
						}
					}

					if (isInsert || canUpdate) {
						tableDataBean.saveOrUpdate(loginContext, sysTable, dataMap);
						return ResponseUtils.responseResult(true);
					} else {
						return ResponseUtils.responseJsonResult(false, "历史数据不允许修改！");
					}
				}
			}
		}
		return ResponseUtils.responseJsonResult(false, "更新数据失败！");
	}

	@javax.annotation.Resource
	public void setEntityService(EntityService entityService) {
		this.entityService = entityService;
	}

	@javax.annotation.Resource
	public void setTableCorrelationService(TableCorrelationService tableCorrelationService) {
		this.tableCorrelationService = tableCorrelationService;
	}

	@javax.annotation.Resource
	public void setTableDataService(ITableDataService tableDataService) {
		this.tableDataService = tableDataService;
	}

	@javax.annotation.Resource
	public void setTablePageService(ITablePageService tablePageService) {
		this.tablePageService = tablePageService;
	}

	@javax.annotation.Resource
	public void setTableService(ITableService tableService) {
		this.tableService = tableService;
	}
}
