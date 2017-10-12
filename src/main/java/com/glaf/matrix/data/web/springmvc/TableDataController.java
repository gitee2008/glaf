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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jxls.common.Context;
import org.jxls.transform.poi.PoiTransformer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.glaf.core.base.BaseItem;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.security.RSAUtils;
import com.glaf.core.service.EntityService;
import com.glaf.core.service.ITableDataService;
import com.glaf.core.service.ITablePageService;
import com.glaf.core.util.Constants;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.LowerLinkedMap;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;

import com.glaf.matrix.data.bean.TableDataBean;
import com.glaf.matrix.data.bean.TableExcelExportBean;
import com.glaf.matrix.data.domain.Comment;
import com.glaf.matrix.data.domain.DataModel;
import com.glaf.matrix.data.domain.SqlCriteria;
import com.glaf.matrix.data.domain.SysTable;
import com.glaf.matrix.data.domain.TableColumn;
import com.glaf.matrix.data.domain.TableCorrelation;
import com.glaf.matrix.data.factory.DataItemFactory;
import com.glaf.matrix.data.service.ITableService;
import com.glaf.matrix.data.service.SqlCriteriaService;
import com.glaf.matrix.data.service.TableCorrelationService;
import com.glaf.report.bean.ReportContainer;
import com.glaf.report.data.ReportDefinition;
import com.glaf.report.data.ReportPreprocessor;

@Controller("/tableData")
@RequestMapping("/tableData")
public class TableDataController {

	protected static final Log logger = LogFactory.getLog(TableDataController.class);

	protected EntityService entityService;

	protected ITableDataService tableDataService;

	protected ITableService tableService;

	protected ITablePageService tablePageService;

	protected TableCorrelationService tableCorrelationService;

	protected SqlCriteriaService sqlCriteriaService;

	@RequestMapping("/audit")
	public ModelAndView audit(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		RequestUtils.setRequestParameterToAttribute(request);
		request.setAttribute("canUpdate", false);
		request.setAttribute("status_enc", RSAUtils.encryptString(String.valueOf("1")));
		String tableId = request.getParameter("tableId");
		if (StringUtils.isNotEmpty(tableId)) {
			SysTable tableDefinition = tableService.getSysTableById(tableId);
			if (tableDefinition != null) {
				TableDataBean tableDataBean = new TableDataBean();
				if (tableDataBean.hasPermission(loginContext, tableDefinition, Constants.PRIVILEGE_READ)) {
					List<TableColumn> columns = tableService.getTableColumnsByTableId(tableId);
					if (columns != null && !columns.isEmpty()) {
						DataModel dataModel = null;
						String uuid = request.getParameter("uuid");
						if (StringUtils.isNotEmpty(uuid)) {
							dataModel = tableDataBean.getDataModel(loginContext, tableDefinition, uuid);
							if (dataModel != null) {
								boolean canUpdate = tableDataBean.canUpdate(loginContext, tableDefinition, uuid);
								request.setAttribute("dataModel", dataModel);
								request.setAttribute("canUpdate", canUpdate);
								request.setAttribute("status", 1);

								int status = dataModel.getBusinessStatus();
								request.setAttribute("status_enc", RSAUtils.encryptString(String.valueOf(status)));
							}
							/**
							 * 入口处加密
							 */
							request.setAttribute("businessKey_enc", RSAUtils.encryptString(uuid));
						} else {
							request.setAttribute("canUpdate", true);
						}

						LowerLinkedMap dataMap = new LowerLinkedMap();
						if (dataModel != null && dataModel.getDataMap() != null && !dataModel.getDataMap().isEmpty()) {
							dataMap.putAll(dataModel.getDataMap());
						}

						logger.debug("dataMap:" + dataMap);

						List<TableColumn> list = new ArrayList<TableColumn>();
						for (TableColumn column : columns) {
							column.setValue(dataMap.get(column.getColumnName().toLowerCase()));
							if (StringUtils.isNotEmpty(column.getDataCode())) {
								if (StringUtils.startsWith(column.getDataCode(), "@sys_dict:")) {
									long nodeId = Long.parseLong(
											column.getDataCode().substring(10, column.getDataCode().length()));
									List<BaseItem> items = DataItemFactory.getInstance().getDictoryItems(nodeId);
									column.setItems(items);
								} else if (StringUtils.startsWith(column.getDataCode(), "@table:")) {
									String rowId = column.getDataCode().substring(7, column.getDataCode().length());
									List<BaseItem> items = DataItemFactory.getInstance().getTableItems(loginContext,
											rowId, params);
									column.setItems(items);
								}
							}
							list.add(column);
						}

						request.setAttribute("columns", columns);
						request.setAttribute("table", tableDefinition);
						request.setAttribute("tableDefinition", tableDefinition);
					}
				}
			}
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/tableData/audit", modelMap);
	}

	@RequestMapping("/auditdatalist")
	public ModelAndView auditdatalist(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug("params:" + params);
		String tableId = request.getParameter("tableId");
		if (StringUtils.isNotEmpty(tableId)) {
			SysTable sysTable = tableService.getSysTableById(tableId);
			if (sysTable != null) {
				List<TableColumn> columns = tableService.getTableColumnsByTableId(tableId);
				if (columns != null && !columns.isEmpty()) {
					List<TableColumn> list = new ArrayList<TableColumn>();
					for (TableColumn column : columns) {
						if (column.getDisplayType() == 2 || column.getDisplayType() == 4) {
							list.add(column);
						}
					}
					request.setAttribute("columns", columns);
					request.setAttribute("table", sysTable);
					request.setAttribute("tableDefinition", sysTable);
				}

				//TableCorrelationQuery query = new TableCorrelationQuery();
				//query.masterTableId(tableId);
				List<TableCorrelation> list = tableCorrelationService.getTableCorrelationsByMasterTableId(tableId);
				if (list != null && !list.isEmpty()) {
					List<SysTable> correlations = new ArrayList<SysTable>();
					for (TableCorrelation t : list) {
						SysTable table = tableService.getSysTableById(t.getSlaveTableId());
						table.setTableCorrelation(t);
						correlations.add(table);
					}
					request.setAttribute("correlations", correlations);
				}

				// SqlCriteriaQuery query2 = new SqlCriteriaQuery();
				// query2.businessKey(sysTable.getTableName());
				// query2.moduleId(tableId);
				List<SqlCriteria> sqlCriterias = sqlCriteriaService.getSqlCriterias(sysTable.getTableName(), tableId);
				if (sqlCriterias != null && !sqlCriterias.isEmpty()) {
					for (SqlCriteria col : sqlCriterias) {
						if (StringUtils.isNotEmpty(request.getParameter(col.getParamName()))) {
							col.setValue(request.getParameter(col.getParamName()));
							if (col.getValue() != null) {
								col.setValueEnc(RequestUtils.encodeString(col.getValue().toString()));
							}
						}
					}
					request.setAttribute("sqlCriterias", sqlCriterias);
				}
			}
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/tableData/auditdatalist", modelMap);
	}

	@RequestMapping("/comments")
	public ModelAndView comments(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		RequestUtils.setRequestParameterToAttribute(request);
		String tableId = request.getParameter("tableId");
		long topId = RequestUtils.getLong(request, "topId");
		logger.debug("topId:" + topId);
		if (StringUtils.isNotEmpty(tableId)) {
			SysTable sysTable = tableService.getSysTableById(tableId);
			if (sysTable != null) {
				TableDataBean tableDataBean = new TableDataBean();
				if (tableDataBean.hasPermission(loginContext, sysTable, Constants.PRIVILEGE_READ)) {
					if (StringUtils.equals(sysTable.getPartitionFlag(), "Y")
							&& StringUtils.isNotEmpty(loginContext.getTenantId())) {
						int hash = IdentityFactory.getTenantHash(loginContext.getTenantId());
						sysTable.setTableName(sysTable.getTableName() + hash);
					}
					sysTable.setTableName(sysTable.getTableName() + "_AUDIT");
					List<Comment> comments = tableDataBean.getComments(loginContext, sysTable, topId);
					request.setAttribute("comments", comments);
				}
			}
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/tableData/comments", modelMap);
	}

	@RequestMapping("/datalist")
	public ModelAndView datalist(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		RequestUtils.setRequestParameterToAttribute(request);
		request.setAttribute("canEdit", true);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug("params:" + params);
		String tableId = request.getParameter("tableId");
		if (StringUtils.isNotEmpty(tableId)) {
			SysTable sysTable = tableService.getSysTableById(tableId);
			if (sysTable != null) {
				List<TableColumn> columns = tableService.getTableColumnsByTableId(tableId);
				if (columns != null && !columns.isEmpty()) {
					List<TableColumn> list = new ArrayList<TableColumn>();
					for (TableColumn column : columns) {
						if (column.getDisplayType() == 2 || column.getDisplayType() == 4) {
							list.add(column);
						}
					}
					request.setAttribute("columns", columns);
					request.setAttribute("table", sysTable);
					request.setAttribute("sysTable", sysTable);
					request.setAttribute("tableDefinition", sysTable);
				}

				List<TableCorrelation> list = tableCorrelationService.getTableCorrelationsByMasterTableId(tableId);
				if (list != null && !list.isEmpty()) {
					List<SysTable> correlations = new ArrayList<SysTable>();
					for (TableCorrelation t : list) {
						SysTable table = tableService.getSysTableById(t.getSlaveTableId());
						table.setTableCorrelation(t);
						correlations.add(table);
					}
					request.setAttribute("correlations", correlations);
				}

				list = tableCorrelationService.getTableCorrelationsBySlaveTableId(tableId);
				if (list != null && !list.isEmpty()) {
					TableCorrelation tc = list.get(0);
					SysTable masterTable = tableService.getSysTableById(tc.getMasterTableId());
					long topId = RequestUtils.getLong(request, "topId");
					if (topId > 0) {
						TableDataBean tableDataBean = new TableDataBean();
						DataModel dataModel = tableDataBean.getDataModelById(loginContext, masterTable, topId);
						if (dataModel != null) {
							LowerLinkedMap dataMap = new LowerLinkedMap();
							dataMap.putAll(dataModel.getDataMap());
							int business_status = dataModel.getBusinessStatus();
							if (business_status == 9) {
								request.setAttribute("canEdit", false);
							}
						}
					}
				}

				// SqlCriteriaQuery query2 = new SqlCriteriaQuery();
				// query2.businessKey(sysTable.getTableName());
				// query2.moduleId(tableId);
				List<SqlCriteria> sqlCriterias = sqlCriteriaService.getSqlCriterias(sysTable.getTableName(), tableId);
				if (sqlCriterias != null && !sqlCriterias.isEmpty()) {
					for (SqlCriteria col : sqlCriterias) {
						if (StringUtils.isNotEmpty(request.getParameter(col.getParamName()))) {
							col.setValue(request.getParameter(col.getParamName()));
							if (col.getValue() != null) {
								col.setValueEnc(RequestUtils.encodeString(col.getValue().toString()));
							}
						}
					}
					request.setAttribute("sqlCriterias", sqlCriterias);
				}
			}
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/tableData/datalist", modelMap);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		RequestUtils.setRequestParameterToAttribute(request);
		request.setAttribute("status", 0);
		request.setAttribute("canUpdate", false);
		request.setAttribute("canEdit", true);
		request.setAttribute("status_enc", RSAUtils.encryptString(String.valueOf("-1")));
		String tableId = request.getParameter("tableId");
		long topId = RequestUtils.getLong(request, "topId");
		if (StringUtils.isNotEmpty(tableId)) {
			SysTable sysTable = tableService.getSysTableById(tableId);
			if (sysTable != null) {
				TableDataBean tableDataBean = new TableDataBean();
				if (tableDataBean.hasPermission(loginContext, sysTable, Constants.PRIVILEGE_READ)) {
					List<TableColumn> columns = tableService.getTableColumnsByTableId(tableId);
					if (columns != null && !columns.isEmpty()) {
						DataModel dataModel = null;
						String uuid = request.getParameter("uuid");
						if (StringUtils.isNotEmpty(uuid)) {
							/**
							 * 入口处加密
							 */
							request.setAttribute("businessKey_enc", RSAUtils.encryptString(uuid));
							dataModel = tableDataBean.getDataModel(loginContext, sysTable, uuid);
							if (dataModel != null) {
								request.setAttribute("dataModel", dataModel);
								boolean canUpdate = false;
								int status = dataModel.getBusinessStatus();
								request.setAttribute("status_enc", RSAUtils.encryptString(String.valueOf(status)));
								if (status == 9) {
									canUpdate = false;// 审核通过的不允许修改
								} else {

									boolean hasUpdatePermission = false;

									Date createDate = dataModel.getCreateTime();

									if (loginContext.isSystemAdministrator()) {
										hasUpdatePermission = true;
									} else {
										if (StringUtils.equals(loginContext.getActorId(), dataModel.getCreateBy())) {
											hasUpdatePermission = true;
										}
										if (loginContext.getRoles() != null
												&& loginContext.getRoles().contains("TenantAdmin")) {
											if (StringUtils.equals(loginContext.getUser().getTenantId(),
													dataModel.getTenantId())) {
												hasUpdatePermission = true;
											}
										}
									}

									if (hasUpdatePermission) {
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
											if (DateUtils.getNowYearMonthDay() == DateUtils
													.getYearMonthDay(createDate)) {
												canUpdate = true;
											}
											break;
										case 4:// 两周内可以修改
											if ((DateUtils.getNowYearMonthDay()
													- DateUtils.getYearMonthDay(createDate)) <= 14) {
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
									}
								}

								logger.debug("canUpdate:" + canUpdate);
								request.setAttribute("status", 1);
								request.setAttribute("canUpdate", canUpdate);

								if (canUpdate) {
									request.setAttribute("status_enc", RSAUtils.encryptString(String.valueOf("0")));
								}

								if (dataModel.getTopId() > 0) {
									topId = dataModel.getTopId();
									logger.debug("topId:" + topId);
								}
							}
						} else {
							request.setAttribute("canUpdate", true);
							request.setAttribute("status_enc", RSAUtils.encryptString(String.valueOf("0")));
						}

						if (topId > 0) {
							//TableCorrelationQuery query = new TableCorrelationQuery();
							//query.slaveTableId(tableId);
							List<TableCorrelation> list = tableCorrelationService.getTableCorrelationsBySlaveTableId(tableId);
							// logger.debug("list:" + list);
							if (list != null && !list.isEmpty()) {
								TableCorrelation tc = list.get(0);
								SysTable masterTable = tableService.getSysTableById(tc.getMasterTableId());
								DataModel dataModel2 = tableDataBean.getDataModelById(loginContext, masterTable, topId);
								if (dataModel2 != null) {
									int business_status = dataModel2.getBusinessStatus();
									if (business_status == 9) {
										request.setAttribute("canEdit", false);
										request.setAttribute("canUpdate", false);
									}
								}
							}
						}

						LowerLinkedMap dataMap = new LowerLinkedMap();
						if (dataModel != null && dataModel.getDataMap() != null && !dataModel.getDataMap().isEmpty()) {
							dataMap.putAll(dataModel.getDataMap());
						}

						logger.debug("dataMap:" + dataMap);

						List<TableColumn> list = new ArrayList<TableColumn>();
						for (TableColumn column : columns) {
							if (column.getLocked() == 1) {
								continue;
							}
							column.setValue(dataMap.get(column.getColumnName().toLowerCase()));
							if (StringUtils.isNotEmpty(column.getDataCode())) {
								if (StringUtils.startsWith(column.getDataCode(), "@sys_dict:")) {
									long nodeId = Long.parseLong(
											column.getDataCode().substring(10, column.getDataCode().length()));
									List<BaseItem> items = DataItemFactory.getInstance().getDictoryItems(nodeId);
									column.setItems(items);
								} else if (StringUtils.startsWith(column.getDataCode(), "@table:")) {
									String rowId = column.getDataCode().substring(7, column.getDataCode().length());
									List<BaseItem> items = DataItemFactory.getInstance().getTableItems(loginContext,
											rowId, params);
									column.setItems(items);
								}
								if (column.getValue() == null) {
									if (StringUtils.equals(column.getDataCode(), "CURR_DATE")) {
										column.setValue(new Date());
									} else if (StringUtils.equals(column.getDataCode(), "CURR_USER")) {
										column.setValue(loginContext.getUser().getName());
									}
								}
							}

							list.add(column);
						}

						request.setAttribute("columns", list);
						request.setAttribute("table", sysTable);
						request.setAttribute("tableDefinition", sysTable);
					}
				}
			}
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/tableData/edit", modelMap);
	}

	@ResponseBody
	@RequestMapping("/exportXls")
	public void exportXls(HttpServletRequest request, HttpServletResponse response) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		// String systemName = Environment.getCurrentSystemName();
		ReportDefinition rdf = null;
		ReportPreprocessor reportPreprocessor = null;
		byte[] bytes = null;
		InputStream is = null;
		ByteArrayInputStream bais = null;
		ByteArrayOutputStream baos = null;
		BufferedOutputStream bos = null;
		try {
			// EnvUtils.setEnv(loginContext, false);// 切换到读库
			request.setAttribute("exportXls", true);
			String reportId = null;
			String tableId = request.getParameter("tableId");
			if (StringUtils.isNotEmpty(tableId)) {
				SysTable sysTable = tableService.getSysTableById(tableId);
				if (sysTable != null) {
					reportId = sysTable.getReportId();
				}
			}

			if (StringUtils.isNotEmpty(reportId)) {
				rdf = ReportContainer.getContainer().getReportDefinition(reportId);
			}

			if (rdf != null && rdf.getData() != null) {
				if (StringUtils.isNotEmpty(rdf.getPrepareClass())) {
					reportPreprocessor = (ReportPreprocessor) com.glaf.core.util.ReflectUtils
							.instantiate(rdf.getPrepareClass());
					reportPreprocessor.prepare(loginContext, params);
				}
				bais = new ByteArrayInputStream(rdf.getData());
				is = new BufferedInputStream(bais);
				baos = new ByteArrayOutputStream();
				bos = new BufferedOutputStream(baos);

				Context context2 = PoiTransformer.createInitialContext();

				Set<Entry<String, Object>> entrySet = params.entrySet();
				for (Entry<String, Object> entry : entrySet) {
					String key = entry.getKey();
					Object value = entry.getValue();
					context2.putVar(key, value);
				}

				org.jxls.util.JxlsHelper.getInstance().processTemplate(is, bos, context2);

				bos.flush();
				baos.flush();
				bytes = baos.toByteArray();
			} else {
				TableExcelExportBean exportBean = new TableExcelExportBean();
				// long databaseId = loginContext.getTenant().getDatabaseId();
				XSSFWorkbook wb = exportBean.export(request);
				baos = new ByteArrayOutputStream();
				bos = new BufferedOutputStream(baos);
				wb.write(bos);
				bos.flush();
				baos.flush();
				bytes = baos.toByteArray();
			}
			ResponseUtils.download(request, response, bytes, "export" + DateUtils.getNowYearMonthDayHHmmss() + ".xlsx");
		} catch (Exception ex) {
			logger.error(ex);
		} finally {
			// Environment.setCurrentSystemName(systemName);
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(bais);
			IOUtils.closeQuietly(baos);
			IOUtils.closeQuietly(bos);
		}
	}

	@ResponseBody
	@RequestMapping("/json")
	public byte[] json(HttpServletRequest request) throws IOException {
		TableDataBean tableDataBean = new TableDataBean();
		JSONObject result = tableDataBean.toJson(request);
		return result.toJSONString().getBytes("UTF-8");
	}

	@javax.annotation.Resource
	public void setEntityService(EntityService entityService) {
		this.entityService = entityService;
	}

	@javax.annotation.Resource
	public void setSqlCriteriaService(SqlCriteriaService sqlCriteriaService) {
		this.sqlCriteriaService = sqlCriteriaService;
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
