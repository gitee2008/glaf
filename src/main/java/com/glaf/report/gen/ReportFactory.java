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

package com.glaf.report.gen;

import java.io.IOException;
import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.JFreeChart;

import com.glaf.chart.bean.ChartDataBean;
import com.glaf.chart.domain.Chart;
import com.glaf.chart.gen.ChartGen;
import com.glaf.chart.gen.JFreeChartFactory;
import com.glaf.chart.util.ChartUtils;
import com.glaf.core.config.Environment;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.el.ExpressionTools;
import com.glaf.core.el.Mvel2ExpressionEvaluator;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.security.Authentication;
import com.glaf.core.service.ITablePageService;
import com.glaf.core.util.ClassUtils;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.FileUtils;
import com.glaf.core.util.JdbcUtils;
import com.glaf.core.util.JsonUtils;
import com.glaf.core.util.QueryUtils;
import com.glaf.core.util.ReflectUtils;
import com.glaf.core.util.StringTools;

import com.glaf.matrix.data.domain.SqlDefinition;
import com.glaf.matrix.data.service.SqlDefinitionService;
import com.glaf.report.bean.ReportContainer;
import com.glaf.report.data.ReportDataSet;
import com.glaf.report.data.ReportDefinition;
import com.glaf.report.data.ReportRowSet;
import com.glaf.report.domain.Report;
import com.glaf.report.domain.ReportFile;
import com.glaf.report.jxls.MyBatisJsonReportManagerImpl;
import com.glaf.report.jxls.MyBatisReportManagerImpl;
import com.glaf.report.jxls.ReportManagerImpl;
import com.glaf.report.query.ReportQuery;
import com.glaf.report.service.IReportFileService;
import com.glaf.report.service.IReportService;

import net.sf.jxls.report.ReportManager;

public class ReportFactory {
	protected static final Log logger = LogFactory.getLog(ReportFactory.class);

	/**
	 * 创建全部报表文件
	 */
	public static void createAllReportFiles() {
		IReportService reportService = ContextFactory.getBean("reportService");
		ReportQuery query = new ReportQuery();
		List<Report> reports = reportService.list(query);
		if (reports != null && !reports.isEmpty()) {
			createReportFiles(reports);
		}
	}

	/**
	 * 生成图表
	 * 
	 * @param chartDefinition
	 * @param params
	 */
	public static void createChart(Chart chartDefinition, Map<String, Object> params) {
		if (chartDefinition != null) {
			ChartGen chartGen = JFreeChartFactory.getChartGen(chartDefinition.getChartType());
			if (chartGen != null) {
				JFreeChart chart = chartGen.createChart(chartDefinition);
				byte[] bytes = ChartUtils.createChart(chartDefinition, chart);
				String path = SystemConfig.getReportSavePath() + "/temp/" + SystemConfig.getCurrentYYYYMMDD();
				try {
					FileUtils.mkdirs(path);
				} catch (IOException e) {
				}
				String filename = path + "/" + chartDefinition.getId();
				if ("png".equalsIgnoreCase(chartDefinition.getImageType())) {
					filename = filename + ".png";
				} else {
					filename = filename + ".jpg";
				}
				try {
					FileUtils.save(filename, bytes);
				} catch (Exception ex) {
					throw new RuntimeException("save chart failed", ex);
				}

				if (params != null) {
					if (!params.containsKey(chartDefinition.getChartName())) {
						params.put(chartDefinition.getChartName(), filename);
					}
					if (chartDefinition.getMapping() != null) {
						if (!params.containsKey(chartDefinition.getMapping())) {
							params.put(chartDefinition.getMapping(), filename);
						}
					}
				}
			}
		}
	}

	/**
	 * 创建某个报表的报表文件
	 * 
	 * @param reportId
	 */
	public static void createReportFile(String reportId) {
		IReportService reportService = ContextFactory.getBean("reportService");
		Report report = reportService.getReport(reportId);
		byte[] bytes = createReportStream(report);

		Map<String, Object> contextMap = SystemConfig.getContextMap();
		String json = report.getJsonParameter();
		if (StringUtils.isNotEmpty(json)) {
			Map<String, Object> jsonMap = JsonUtils.decode(json);
			if (jsonMap != null && !jsonMap.isEmpty()) {
				Set<Entry<String, Object>> entrySet = jsonMap.entrySet();
				for (Entry<String, Object> entry : entrySet) {
					String key = entry.getKey();
					Object value = entry.getValue();
					if (!contextMap.containsKey(key)) {
						contextMap.put(key, value);
					}
				}
			}
		}

		String filename = null;
		String reportName = report.getReportName();
		if (reportName != null) {
			filename = ExpressionTools.evaluate(reportName, contextMap);
		}
		ReportFile reportFile = new ReportFile();
		reportFile.setCreateDate(new Date());
		reportFile.setFileContent(bytes);
		reportFile.setFileSize(bytes.length);
		reportFile.setReportId(report.getId());
		reportFile.setReportYearMonthDay(DateUtils.getYearMonthDay(new Date()));
		if (filename != null) {
			reportFile.setFilename(filename + "." + report.getReportFormat());
		} else {
			reportFile.setFilename(
					report.getId() + "_" + reportFile.getReportYearMonthDay() + "." + report.getReportFormat());
		}
		IReportFileService reportFileService = ContextFactory.getBean("reportFileService");
		reportFile.setId(report.getId() + "_" + reportFile.getReportYearMonthDay());
		reportFileService.save(reportFile);
	}

	/**
	 * 创建全部报表文件
	 */
	public static void createReportFiles(List<Report> reports) {
		if (reports != null && !reports.isEmpty()) {
			for (Report report : reports) {
				if (report != null && report.getReportTemplate() != null) {
					createReportFile(report.getId());
				}
			}
		}
	}

	/**
	 * 创建某个报表的报表文件
	 * 
	 * @param report
	 */
	public static byte[] createReportStream(Report report) {
		Map<String, Object> params = SystemConfig.getContextMap();
		byte[] bytes = createReportStream(report, params);
		return bytes;
	}

	/**
	 * 根据指定参数生成
	 * 
	 * @param report
	 * @param params
	 */
	public static byte[] createReportStream(Report report, Map<String, Object> params) {
		byte[] rptBytes = null;
		ReportGen reportGen = null;
		if ("jasper".equals(report.getType())) {
			reportGen = new JasperReportGen();
		} else if ("jxls".equals(report.getType())) {
			reportGen = new JxlsReportGen();
		} else if ("jxls2".equals(report.getType())) {
			reportGen = new Jxls2ReportGen();
		} else if ("ftl".equals(report.getType())) {
			reportGen = new FreemarkerReportGen();
		}
		if (reportGen != null) {
			// 准备查询结果集和生成图片信息
			ITablePageService tablePageService = ContextFactory.getBean("tablePageService");
			SqlDefinitionService sqlDefinitionService = ContextFactory.getBean("sqlDefinitionService");

			String json = report.getJsonParameter();
			if (StringUtils.isNotEmpty(json)) {
				Map<String, Object> jsonMap = JsonUtils.decode(json);
				if (jsonMap != null && !jsonMap.isEmpty()) {
					Set<Entry<String, Object>> entrySet = jsonMap.entrySet();
					for (Entry<String, Object> entry : entrySet) {
						String key = entry.getKey();
						Object value = entry.getValue();
						if (!params.containsKey(key)) {
							params.put(key, value);
						}
					}
				}
			}

			logger.debug("params:" + params);

			String value = null;
			String reportMonth = report.getReportMonth();
			if (reportMonth != null) {
				value = ExpressionTools.evaluate(reportMonth, params);
				params.put("reportMonth", value);
			}
			String reportName = report.getReportName();
			if (reportName != null) {
				value = ExpressionTools.evaluate(reportName, params);
				params.put("reportName", value);
			}

			String reportTitleDate = report.getReportTitleDate();
			if (reportTitleDate != null) {
				value = ExpressionTools.evaluate(reportTitleDate, params);
				params.put("reportDate", value);
			}

			String reportDateYYYYMMDD = report.getReportDateYYYYMMDD();
			if (reportDateYYYYMMDD != null) {
				Object val = Mvel2ExpressionEvaluator.evaluate(reportDateYYYYMMDD, params);
				params.put("reportDateYYYYMMdd", val);
				params.put("reportDateYYYYMMDD", val);
			}

			if (StringUtils.isNotEmpty(report.getQueryIds())) {
				String systemName = Environment.getCurrentSystemName();
				List<String> queryIds = StringTools.split(report.getQueryIds());
				if (queryIds != null && !queryIds.isEmpty()) {

					for (String queryId : queryIds) {
						boolean success = false;
						int retry = 0;
						while (retry < 2 && !success) {
							try {
								retry++;
								SqlDefinition queryDef = sqlDefinitionService.getSqlDefinitionByUUID(queryId);
								if (queryDef != null) {
									String querySQL = queryDef.getSql();
									querySQL = QueryUtils.replaceSQLVars(querySQL);
									querySQL = QueryUtils.replaceSQLParas(querySQL, params);

									logger.debug("querySQL:" + querySQL);
									logger.debug("params:" + params);

									List<Map<String, Object>> rows = tablePageService.getListData(querySQL, params);
									// logger.debug("rows:" + rows);
									if (rows != null && !rows.isEmpty()) {
										if (rows.size() == 1) {
											Map<String, Object> rowMap = rows.get(0);
											if (StringUtils.isNotEmpty(queryDef.getName())
													&& !params.containsKey(queryDef.getName())) {
												params.put(queryDef.getName(), rowMap);
												Set<Entry<String, Object>> entrySet = rowMap.entrySet();
												for (Entry<String, Object> entry : entrySet) {
													String key = entry.getKey();
													Object val = entry.getValue();
													if (val != null) {
														params.put(queryDef.getName() + "_" + key, val);
													}
												}
											}
											if (StringUtils.isNotEmpty(queryDef.getCode())
													&& !params.containsKey(queryDef.getCode())) {
												params.put(queryDef.getCode(), rowMap);
												Set<Entry<String, Object>> entrySet = rowMap.entrySet();
												for (Entry<String, Object> entry : entrySet) {
													String key = entry.getKey();
													Object val = entry.getValue();
													if (val != null) {
														params.put(queryDef.getCode() + "_" + key, val);
													}
												}
											}
										} else {
											if (StringUtils.isNotEmpty(queryDef.getName())
													&& !params.containsKey(queryDef.getName())) {
												params.put(queryDef.getName(), rows);
											}
											if (StringUtils.isNotEmpty(queryDef.getCode())
													&& !params.containsKey(queryDef.getCode())) {
												params.put(queryDef.getCode(), rows);
											}
										}
									}
								}
								success = true;
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					}
				}
				Environment.setCurrentSystemName(systemName);
			}
			if (StringUtils.isNotEmpty(report.getChartIds())) {
				List<String> chartIds = StringTools.split(report.getChartIds());
				if (chartIds != null && !chartIds.isEmpty()) {
					for (String chartId : chartIds) {
						boolean success = false;
						int retry = 0;
						while (retry < 2 && !success) {
							try {
								retry++;
								ChartDataBean manager = new ChartDataBean();
								Chart chart = manager.getChartAndFetchDataById(chartId, params,
										Authentication.getAuthenticatedActorId());
								createChart(chart, params);
								success = true;
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					}
				}
			}

			ReportDefinition reportDefinition = ReportContainer.getContainer().getReportDefinition(report.getId());

			logger.debug("report parameter:" + params);
			// 产生报表
			Connection connection = null;
			try {
				connection = DBConnectionFactory.getConnection();
				if (reportDefinition != null) {
					List<ReportDataSet> dataSetList = reportDefinition.getDataSetList();
					if (dataSetList != null && !dataSetList.isEmpty()) {
						for (ReportDataSet rds : dataSetList) {
							List<ReportRowSet> rowSetList = rds.getRowSetList();
							if (rowSetList != null && !rowSetList.isEmpty()) {
								for (ReportRowSet rs : rowSetList) {
									String rptMgr = rs.getRptMgr();
									String rptMgrMapping = rs.getRptMgrMapping();
									ReportManager rm = null;
									if ("sql".equals(rptMgr)) {
										rm = new ReportManagerImpl(connection, params);
									} else {
										String rptMgrClassName = rs.getRptMgrClassName();
										if (StringUtils.isNotEmpty(rptMgrClassName)) {
											rm = (ReportManager) ClassUtils.instantiateObject(rptMgrClassName);
											try {
												ReflectUtils.setFieldValue(rm, "connection", connection);
											} catch (Exception ex) {
											}
											try {
												ReflectUtils.setFieldValue(rm, "properties", rs.getProperties());
											} catch (Exception ex) {
											}
										}
									}
									if (rm != null) {
										params.put(rptMgrMapping, rm);
									}
								}
							}
						}
					}
				} else {
					ReportManager rm = new ReportManagerImpl(connection, params);
					params.put("rm", rm);
				}

				params.put("con", connection);
				params.put("conn", connection);
				params.put("connection", connection);

				ReportManager mybatis = new MyBatisReportManagerImpl(connection, params);
				params.put("mybatis", mybatis);

				ReportManager mybatisx = new MyBatisJsonReportManagerImpl(connection, params);
				params.put("mybatisx", mybatisx);

				rptBytes = reportGen.createReport(report, connection, params);
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			} finally {
				JdbcUtils.close(connection);
			}
		}
		return rptBytes;
	}

	/**
	 * 创建某个报表的报表文件
	 * 
	 * @param reportId
	 */
	public static byte[] createReportStream(String reportId) {
		IReportService reportService = ContextFactory.getBean("reportService");
		Report report = reportService.getReport(reportId);
		return createReportStream(report);
	}

	public static void genAllReportFile() {
		IReportService reportService = ContextFactory.getBean("reportService");
		ReportQuery query = new ReportQuery();
		List<Report> reports = reportService.list(query);
		if (reports != null && !reports.isEmpty()) {
			for (Report report : reports) {
				boolean success = false;
				int retry = 0;
				while (retry < 2 && !success) {
					try {
						retry++;
						createReportFile(report.getId());
						success = true;
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}
	}

	private ReportFactory() {

	}

}