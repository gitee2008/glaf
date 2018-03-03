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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.glaf.core.base.ColumnModel;
import com.glaf.core.base.TableModel;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.service.EntityService;
import com.glaf.core.service.ITableDataService;
import com.glaf.core.service.ITablePageService;
import com.glaf.core.util.Constants;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;

import com.glaf.matrix.data.bean.TableDataBean;
import com.glaf.matrix.data.domain.DataModel;
import com.glaf.matrix.data.domain.SysTable;
import com.glaf.matrix.data.domain.TableColumn;
import com.glaf.matrix.data.domain.TableCorrelation;
import com.glaf.matrix.data.service.ITableService;
import com.glaf.matrix.data.service.TableCorrelationService;

@Controller("/tableDataDelete")
@RequestMapping("/tableDataDelete")
public class TableDataDeleteController {

	protected static final Log logger = LogFactory.getLog(TableDataDeleteController.class);

	protected EntityService entityService;

	protected ITableDataService tableDataService;

	protected ITableService tableService;

	protected ITablePageService tablePageService;

	protected TableCorrelationService tableCorrelationService;

	@ResponseBody
	@RequestMapping("/deleteById")
	public byte[] deleteById(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		RequestUtils.setRequestParameterToAttribute(request);
		String tableId = request.getParameter("tableId");
		if (StringUtils.isNotEmpty(tableId)) {
			SysTable sysTable = tableService.getSysTableById(tableId);
			if (sysTable != null) {
				TableDataBean tableDataBean = new TableDataBean();
				if (!tableDataBean.hasPermission(loginContext, sysTable, Constants.PRIVILEGE_READ_WRITE)) {
					return ResponseUtils.responseJsonResult(false, "您没有操作权限，请联系您的管理员授权。");
				}

				SysTable slaveTable = null;

				// TableCorrelationQuery query = new TableCorrelationQuery();
				// query.masterTableId(tableId);
				List<TableCorrelation> list = tableCorrelationService.getTableCorrelationsByMasterTableId(tableId);
				List<TableColumn> columns = tableService.getTableColumnsByTableId(tableId);
				if (columns != null && !columns.isEmpty()) {
					DataModel dataModel = null;
					Date createDate = null;
					String uuid = request.getParameter("uuid");
					if (StringUtils.isNotEmpty(uuid)) {
						dataModel = tableDataBean.getDataModel(loginContext, sysTable, uuid);
					}

					if (dataModel != null && dataModel.getBusinessStatus() == 9) {
						return ResponseUtils.responseJsonResult(false, "审核通过的数据不能删除。");
					}

					if (dataModel != null && dataModel.getBusinessStatus() != 9) {

						boolean canDelete = false;
						boolean hasDeletePermission = false;

						createDate = dataModel.getCreateTime();

						if (loginContext.isSystemAdministrator()) {
							hasDeletePermission = true;
						} else {
							if (StringUtils.equals(loginContext.getActorId(), dataModel.getCreateBy())) {
								hasDeletePermission = true;
							}
							if (loginContext.getRoles() != null && loginContext.getRoles().contains("TenantAdmin")) {
								if (StringUtils.equals(loginContext.getUser().getTenantId(), dataModel.getTenantId())) {
									hasDeletePermission = true;
								} else if (loginContext.getUser().getOrganizationId() == dataModel
										.getOrganizationId()) {
									hasDeletePermission = true;
								}
							}
						}

						if (hasDeletePermission) {
							switch (sysTable.getDeleteCascade()) {
							case 0:// 可物理删除，记录删除后不能恢复
								canDelete = true;
								break;
							case 1:// 不做物理删除，只打删除标记
								canDelete = true;
								break;
							case 2:// 不能删除
								canDelete = false;
								break;
							case 3:// 当天可以删除
								if (DateUtils.getNowYearMonthDay() == DateUtils.getYearMonthDay(createDate)) {
									canDelete = true;
								}
								break;
							case 4:// 两周内可以删除
								if (DateUtils.getDaysBetween(createDate, new Date()) <= 14) {
									canDelete = true;
								}
								break;
							case 5:// 当月内可删除
								if (DateUtils.getNowYearMonth() == DateUtils.getYearMonth(createDate)) {
									canDelete = true;
								}
								break;
							default:
								canDelete = false;
								break;
							}
						} else {
							canDelete = false;
							return ResponseUtils.responseJsonResult(false, "您没有删除该数据的权限！");
						}

						if (canDelete && list != null && !list.isEmpty()) {
							/**
							 * 检查是否存在从表，并检查是否可以删除从表数据
							 */
							for (TableCorrelation t : list) {
								/**
								 * 先删除子表才能删除主表的情况，必须检查子表是否存在记录，存在不允许删除，不存在才可以删除。
								 */
								if (StringUtils.equals(t.getDeleteCascade(), "M")) {
									canDelete = false;
									slaveTable = tableService.getSysTableById(t.getSlaveTableId());
									if (StringUtils.equals(slaveTable.getPartitionFlag(), "Y")
											&& StringUtils.isNotEmpty(loginContext.getTenantId())) {
										int hash = IdentityFactory.getTenantHash(loginContext.getTenantId());
										slaveTable.setTableName(slaveTable.getTableName() + hash);
									}
									StringBuilder sqlBuffer = new StringBuilder();
									params = new HashMap<String, Object>();
									params.put("topId", dataModel.getId());
									sqlBuffer.append(" select E.* from ").append(slaveTable.getTableName())
											.append(" E ");
									sqlBuffer.append(" where 1=1 ");
									sqlBuffer.append(" and E.TOPID_ = #{topId} ");
									Map<String, Object> rowMap = tablePageService.getOne(sqlBuffer.toString(), params);
									if (rowMap == null || rowMap.isEmpty()) {
										canDelete = true;
									} else {
										return ResponseUtils.responseJsonResult(false, "请先删除从表记录再删除主表记录。");
									}
								} else if (StringUtils.equals(t.getDeleteCascade(), "N")) {
									canDelete = false;
									return ResponseUtils.responseJsonResult(false, "从表记录已经存在记录且不允许删除。");
								}
							}
						}

						if (canDelete) {
							List<TableModel> rows = new ArrayList<TableModel>();

							if (sysTable.getDeleteCascade() == 0 || sysTable.getDeleteCascade() == 3
									|| sysTable.getDeleteCascade() == 4 || sysTable.getDeleteCascade() == 5) {
								if (list != null && !list.isEmpty()) {
									/**
									 * 检查是否存在从表，并检查是否可以删除从表数据
									 */
									for (TableCorrelation t : list) {
										/**
										 * 与主表一同删除
										 */
										if (StringUtils.equals(t.getDeleteCascade(), "S")) {
											slaveTable = tableService.getSysTableById(t.getSlaveTableId());
											if (StringUtils.equals(slaveTable.getPartitionFlag(), "Y")
													&& StringUtils.isNotEmpty(loginContext.getTenantId())) {
												int hash = IdentityFactory.getTenantHash(loginContext.getTenantId());
												slaveTable.setTableName(slaveTable.getTableName() + hash);
											}
											TableModel tableModel = new TableModel();
											tableModel.setTableName(slaveTable.getTableName());
											ColumnModel topIdColumn = new ColumnModel();
											topIdColumn.setColumnName("TOPID_");
											topIdColumn.setValue(dataModel.getId());
											tableModel.setIdColumn(topIdColumn);
											tableModel.addColumn(topIdColumn);
											rows.add(tableModel);
										}
									}
								}

								if (StringUtils.equals(sysTable.getPartitionFlag(), "Y")
										&& StringUtils.isNotEmpty(loginContext.getTenantId())) {
									int hash = IdentityFactory.getTenantHash(loginContext.getTenantId());
									sysTable.setTableName(sysTable.getTableName() + hash);
								}

								TableModel tableModel = new TableModel();
								tableModel.setTableName(sysTable.getTableName());
								ColumnModel idColumn = new ColumnModel();
								idColumn.setColumnName("ID_");
								idColumn.setValue(dataModel.getId());
								tableModel.setIdColumn(idColumn);
								tableModel.addColumn(idColumn);
								rows.add(tableModel);
								tableDataService.deleteTableDataList(rows);

								/**
								 * 判断是否存在附件，如果有附件，删除附件表的内容
								 */

							} else if (sysTable.getDeleteCascade() == 1) {
								if (list != null && !list.isEmpty()) {
									/**
									 * 检查是否存在从表，并检查是否可以删除从表数据
									 */
									for (TableCorrelation t : list) {
										/**
										 * 与主表一同删除
										 */
										if (StringUtils.equals(t.getDeleteCascade(), "S")) {
											slaveTable = tableService.getSysTableById(t.getSlaveTableId());
											if (StringUtils.equals(slaveTable.getPartitionFlag(), "Y")
													&& StringUtils.isNotEmpty(loginContext.getTenantId())) {
												int hash = IdentityFactory.getTenantHash(loginContext.getTenantId());
												slaveTable.setTableName(slaveTable.getTableName() + hash);
											}

											TableModel tableModel = new TableModel();
											tableModel.setTableName(slaveTable.getTableName());
											ColumnModel topIdColumn = new ColumnModel();
											topIdColumn.setColumnName("TOPID_");
											topIdColumn.setValue(dataModel.getId());
											topIdColumn.setJavaType("Long");
											tableModel.setIdColumn(topIdColumn);

											ColumnModel deleteFlagColumn = new ColumnModel();
											deleteFlagColumn.setColumnName("DELETEFLAG_");
											deleteFlagColumn.setValue(1);
											tableModel.addColumn(deleteFlagColumn);

											ColumnModel deleteTimeColumn = new ColumnModel();
											deleteTimeColumn.setColumnName("DELETETIME_");
											deleteTimeColumn.setValue(new java.util.Date());
											deleteTimeColumn.setJavaType("Date");
											tableModel.addColumn(deleteTimeColumn);

											rows.add(tableModel);
										}
									}
								}

								if (StringUtils.equals(sysTable.getPartitionFlag(), "Y")
										&& StringUtils.isNotEmpty(loginContext.getTenantId())) {
									int hash = IdentityFactory.getTenantHash(loginContext.getTenantId());
									sysTable.setTableName(sysTable.getTableName() + hash);
								}

								TableModel tableModel = new TableModel();
								tableModel.setTableName(sysTable.getTableName());
								ColumnModel idColumn = new ColumnModel();
								idColumn.setColumnName("ID_");
								idColumn.setValue(dataModel.getId());
								idColumn.setJavaType("Long");
								tableModel.setIdColumn(idColumn);

								ColumnModel deleteFlagColumn = new ColumnModel();
								deleteFlagColumn.setColumnName("DELETEFLAG_");
								deleteFlagColumn.setValue(1);
								tableModel.addColumn(deleteFlagColumn);

								ColumnModel deleteTimeColumn = new ColumnModel();
								deleteTimeColumn.setColumnName("DELETETIME_");
								deleteTimeColumn.setValue(new java.util.Date());
								deleteTimeColumn.setJavaType("Date");
								tableModel.addColumn(deleteTimeColumn);
								rows.add(tableModel);
								/**
								 * 更新记录，对记录打删除标记
								 */
								tableDataService.updateAllTableData(rows);

								/**
								 * 判断是否存在附件，如果有附件， 标识附件表的内容
								 */

							}

							return ResponseUtils.responseResult(true);
						} else {
							return ResponseUtils.responseJsonResult(false, "历史数据不允许删除！");
						}
					}
				}
			}
		}
		return ResponseUtils.responseJsonResult(false, "删除数据失败！");
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
