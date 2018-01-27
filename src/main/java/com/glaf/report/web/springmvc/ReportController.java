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

package com.glaf.report.web.springmvc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.converter.ExcelToHtmlConverter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jxls.common.Context;
import org.jxls.transform.poi.PoiTransformer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.chart.domain.Chart;
import com.glaf.chart.query.ChartQuery;
import com.glaf.chart.service.IChartService;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.FileUtils;
import com.glaf.core.util.LogUtils;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.StringTools;
import com.glaf.core.util.Tools;

import com.glaf.matrix.data.domain.SqlDefinition;
import com.glaf.matrix.data.query.SqlDefinitionQuery;
import com.glaf.matrix.data.service.SqlDefinitionService;
import com.glaf.report.bean.ReportContainer;
import com.glaf.report.config.ReportConfig;
import com.glaf.report.data.ReportDefinition;
import com.glaf.report.data.ReportPreprocessor;
import com.glaf.report.domain.Report;
import com.glaf.report.gen.ReportFactory;
import com.glaf.report.jxls.Jxls2ReportContainer;
import com.glaf.report.jxls.JxlsReportContainer;
import com.glaf.report.query.ReportQuery;
import com.glaf.report.service.IReportService;

@Controller("/report")
@RequestMapping("/report")
public class ReportController {

	protected final static Log logger = LogFactory.getLog(ReportController.class);

	protected IReportService reportService;

	protected IChartService chartService;

	protected SqlDefinitionService sqlDefinitionService;

	@RequestMapping("/chooseChart")
	public ModelAndView chooseChart(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		String rowId = ParamUtils.getString(params, "reportId");
		ChartQuery query = new ChartQuery();
		List<Chart> list = chartService.list(query);
		request.setAttribute("unselecteds", list);
		Report report = null;
		if (StringUtils.isNotEmpty(rowId)) {
			report = reportService.getReport(rowId);
			request.setAttribute("report", report);
			if (StringUtils.isNotEmpty(report.getChartIds())) {
				StringBuilder sb01 = new StringBuilder();
				StringBuilder sb02 = new StringBuilder();
				List<String> selecteds = new java.util.ArrayList<String>();
				List<String> chartIds = StringTools.split(report.getChartIds());
				for (Chart c : list) {
					if (chartIds.contains(c.getId())) {
						selecteds.add(c.getId());
						sb01.append(c.getId()).append(",");
						sb02.append(c.getSubject()).append(",");
					}
				}
				if (sb01.toString().endsWith(",")) {
					sb01.delete(sb01.length() - 1, sb01.length());
				}
				if (sb02.toString().endsWith(",")) {
					sb02.delete(sb02.length() - 1, sb02.length());
				}
				request.setAttribute("selecteds", selecteds);
				request.setAttribute("chartIds", sb01.toString());

				request.setAttribute("chartNames", sb02.toString());
			}

			if (StringUtils.isNotEmpty(report.getQueryIds())) {
				List<String> queryIds = StringTools.split(report.getQueryIds());
				StringBuilder sb01 = new StringBuilder();
				StringBuilder sb02 = new StringBuilder();
				for (String queryId : queryIds) {
					SqlDefinition qlDefinition = sqlDefinitionService.getSqlDefinitionByUUID(queryId);
					if (qlDefinition != null) {
						sb01.append(qlDefinition.getId()).append(",");
						sb02.append(qlDefinition.getTitle()).append("[").append(qlDefinition.getId()).append("],");
					}
				}
				if (sb01.toString().endsWith(",")) {
					sb01.delete(sb01.length() - 1, sb01.length());
				}
				if (sb02.toString().endsWith(",")) {
					sb02.delete(sb02.length() - 1, sb02.length());
				}
				request.setAttribute("queryIds", sb01.toString());
				request.setAttribute("queryNames", sb02.toString());
			}
		}

		String x_view = ViewProperties.getString("report.chooseChart");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/report/chooseChart", modelMap);
	}

	@RequestMapping("/chooseFile")
	public ModelAndView chooseFile(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LogUtils.debug("params=" + RequestUtils.getParameterMap(request));
		String reportId = request.getParameter("reportId");
		Report report = null;
		if (StringUtils.isNotEmpty(reportId)) {
			report = reportService.getReport(reportId);
			if (report != null) {
				modelMap.put("report", report);
			}
		}

		String path = request.getParameter("path");
		if (StringUtils.isNotEmpty(path)) {
			modelMap.put("path", path);
		}
		return new ModelAndView("/report/chooseFile", modelMap);
	}

	@RequestMapping("/chooseQuery")
	public ModelAndView chooseQuery(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		Map<String, Object> params = RequestUtils.getParameterMap(request);
		String rowId = ParamUtils.getString(params, "reportId");
		SqlDefinitionQuery query = new SqlDefinitionQuery();
		List<SqlDefinition> list = sqlDefinitionService.list(query);
		request.setAttribute("unselecteds", list);
		Report report = null;
		if (StringUtils.isNotEmpty(rowId)) {
			report = reportService.getReport(rowId);
			request.setAttribute("report", report);
			if (StringUtils.isNotEmpty(report.getQueryIds())) {
				StringBuilder sb01 = new StringBuilder();
				StringBuilder sb02 = new StringBuilder();
				List<String> selecteds = new java.util.ArrayList<String>();
				for (SqlDefinition q : list) {
					if (StringUtils.contains(report.getQueryIds(), q.getUuid())) {
						selecteds.add(q.getUuid());
						sb01.append(q.getId()).append(",");
						sb02.append(q.getName()).append(",");
					}
				}
				if (sb01.toString().endsWith(",")) {
					sb01.delete(sb01.length() - 1, sb01.length());
				}
				if (sb02.toString().endsWith(",")) {
					sb02.delete(sb02.length() - 1, sb02.length());
				}
				request.setAttribute("selecteds", selecteds);
				request.setAttribute("queryIds", sb01.toString());

				request.setAttribute("queryNames", sb02.toString());
			}
		}

		String x_view = ViewProperties.getString("report.chooseQuery");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/report/chooseQuery", modelMap);
	}

	@RequestMapping("/createReport")
	public void createReport(HttpServletRequest request, HttpServletResponse response) {
		RequestUtils.setRequestParameterToAttribute(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		String reportId = ParamUtils.getString(params, "reportId");
		Report report = null;
		if (StringUtils.isNotEmpty(reportId)) {
			report = reportService.getReport(reportId);
			if (report != null) {
				String filename = report.getSubject() + DateUtils.getNowYearMonthDayHHmmss() + "."
						+ report.getReportFormat();
				try {
					byte[] bytes = ReportFactory.createReportStream(report, params);
					if (bytes != null) {
						String destFileName = ReportConfig.getReportDestFileName(report);
						FileUtils.save(destFileName, bytes);
						ResponseUtils.download(request, response, bytes, filename);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	@ResponseBody
	@RequestMapping("/delete")
	public byte[] delete(HttpServletRequest request, ModelMap modelMap) {
		LoginContext securityContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		String rowId = ParamUtils.getString(params, "reportId");
		String rowIds = request.getParameter("reportIds");
		if (StringUtils.isNotEmpty(rowIds)) {
			StringTokenizer token = new StringTokenizer(rowIds, ",");
			while (token.hasMoreTokens()) {
				String x = token.nextToken();
				if (StringUtils.isNotEmpty(x)) {
					Report report = reportService.getReport(x);
					if (report != null && StringUtils.equals(report.getCreateBy(), securityContext.getActorId())) {
						reportService.deleteById(report.getId());
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (StringUtils.isNotEmpty(rowId)) {
			Report report = reportService.getReport(rowId);
			if (report != null && StringUtils.equals(report.getCreateBy(), securityContext.getActorId())) {
				reportService.deleteById(report.getId());
				return ResponseUtils.responseResult(true);
			}
		}

		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		Map<String, Object> params = RequestUtils.getParameterMap(request);
		String reportId = ParamUtils.getString(params, "reportId");
		Report report = null;
		if (StringUtils.isNotEmpty(reportId)) {
			report = reportService.getReport(reportId);
			request.setAttribute("report", report);
			if (StringUtils.isNotEmpty(report.getChartIds())) {
				StringBuilder sb01 = new StringBuilder();
				StringBuilder sb02 = new StringBuilder();
				List<Chart> selecteds = new java.util.ArrayList<Chart>();
				ChartQuery query = new ChartQuery();
				List<Chart> list = chartService.list(query);
				request.setAttribute("unselecteds", list);
				List<String> selected = StringTools.split(report.getChartIds());
				for (Chart c : list) {
					if (selected.contains(c.getId())) {
						selecteds.add(c);
						sb01.append(c.getId()).append(",");
						sb02.append(c.getSubject()).append(",");
					}
				}
				if (sb01.toString().endsWith(",")) {
					sb01.delete(sb01.length() - 1, sb01.length());
				}
				if (sb02.toString().endsWith(",")) {
					sb02.delete(sb02.length() - 1, sb02.length());
				}
				request.setAttribute("selecteds", selecteds);
				request.setAttribute("chartIds", sb01.toString());

				request.setAttribute("chartNames", sb02.toString());
			}

			if (StringUtils.isNotEmpty(report.getQueryIds())) {
				List<String> queryIds = StringTools.split(report.getQueryIds());
				StringBuilder sb01 = new StringBuilder();
				StringBuilder sb02 = new StringBuilder();
				for (String queryId : queryIds) {
					SqlDefinition qlDefinition = sqlDefinitionService.getSqlDefinitionByUUID(queryId);
					if (qlDefinition != null) {
						sb01.append(qlDefinition.getId()).append(",");
						sb02.append(qlDefinition.getTitle()).append("[").append(qlDefinition.getId()).append("],");
					}
				}
				if (sb01.toString().endsWith(",")) {
					sb01.delete(sb01.length() - 1, sb01.length());
				}
				if (sb02.toString().endsWith(",")) {
					sb02.delete(sb02.length() - 1, sb02.length());
				}
				request.setAttribute("queryIds", sb01.toString());
				request.setAttribute("queryNames", sb02.toString());
			}
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("report.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/report/edit", modelMap);
	}

	@ResponseBody
	@RequestMapping("/exportXls")
	public void exportXls(HttpServletRequest request, HttpServletResponse response) {
		String reportId = request.getParameter("reportId");
		if (StringUtils.isNotEmpty(reportId)) {
			LoginContext loginContext = RequestUtils.getLoginContext(request);
			Map<String, Object> params = RequestUtils.getParameterMap(request);
			params.put("loginContext", loginContext);
			params.put("contextPath", request.getContextPath());
			params.put("serviceUrl", RequestUtils.getServiceUrl(request));

			Report report = reportService.getReport(reportId);

			String contentDisposition = "attachment;filename=\"export" + DateUtils.getNowYearMonthDayHHmmss()
					+ ".xls\"";
			response.setHeader("Content-Transfer-Encoding", "base64");
			response.setHeader("Content-Disposition", contentDisposition);
			response.setContentType("application/octet-stream");
			if (StringUtils.equals(report.getReportFormat(), "xlsx")) {
				contentDisposition = "attachment;filename=\"export" + DateUtils.getNowYearMonthDayHHmmss() + ".xlsx\"";
				response.setHeader("Content-Disposition", contentDisposition);
			}

			ReportDefinition rdf = ReportContainer.getContainer().getReportDefinition(reportId);
			ReportPreprocessor reportPreprocessor = null;
			byte[] bytes = null;
			InputStream is = null;
			ByteArrayInputStream bais = null;
			ByteArrayOutputStream baos = null;
			BufferedOutputStream bos = null;
			OutputStream outputStream = null;
			try {
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
					bytes = ReportFactory.createReportStream(report, params);
				}
				if (bytes != null) {
					outputStream = response.getOutputStream();
					outputStream.write(bytes);
					outputStream.flush();
					outputStream.close();
					outputStream = null;
					bytes = null;
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				com.glaf.core.util.IOUtils.closeStream(is);
				com.glaf.core.util.IOUtils.closeStream(bais);
				com.glaf.core.util.IOUtils.closeStream(baos);
				com.glaf.core.util.IOUtils.closeStream(bos);
				com.glaf.core.util.IOUtils.closeStream(outputStream);
			}
		}
	}

	@ResponseBody
	@RequestMapping("/exportXls2")
	public void exportXls2(HttpServletRequest request, HttpServletResponse response) {
		String reportId = request.getParameter("reportId");
		if (StringUtils.isNotEmpty(reportId)) {
			LoginContext loginContext = RequestUtils.getLoginContext(request);
			ReportDefinition rdf = ReportContainer.getContainer().getReportDefinition(reportId);
			Map<String, Object> params = RequestUtils.getParameterMap(request);
			params.put("contextPath", request.getContextPath());
			params.put("serviceUrl", RequestUtils.getServiceUrl(request));
			byte[] data = null;

			String contentDisposition = "attachment;filename=\"export" + DateUtils.getNowYearMonthDayHHmmss()
					+ ".xls\"";
			response.setHeader("Content-Transfer-Encoding", "base64");
			response.setHeader("Content-Disposition", contentDisposition);
			response.setContentType("application/octet-stream");
			Report report = reportService.getReport(reportId);
			if (StringUtils.equals(report.getReportFormat(), "xlsx")) {
				contentDisposition = "attachment;filename=\"export" + DateUtils.getNowYearMonthDayHHmmss() + ".xlsx\"";
				response.setHeader("Content-Disposition", contentDisposition);
			}

			java.io.OutputStream outputStream = null;
			try {
				if (StringUtils.equalsIgnoreCase(rdf.getTemplateType(), "jxls2")) {
					data = Jxls2ReportContainer.getContainer().execute(reportId, loginContext.getActorId(), params);
				} else {
					data = JxlsReportContainer.getContainer().execute(reportId, loginContext.getActorId(), params);
				}
				if (data != null) {
					outputStream = response.getOutputStream();
					outputStream.write(data);
					outputStream.flush();
					com.glaf.core.util.IOUtils.closeStream(outputStream);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				com.glaf.core.util.IOUtils.closeStream(outputStream);
			}
		}
	}

	@ResponseBody
	@RequestMapping("/exportXls3")
	public void exportXls3(HttpServletRequest request, HttpServletResponse response) {
		String reportId = request.getParameter("reportId");
		if (StringUtils.isNotEmpty(reportId)) {
			LoginContext loginContext = RequestUtils.getLoginContext(request);
			Map<String, Object> params = RequestUtils.getParameterMap(request);
			params.put("loginContext", loginContext);
			params.put("contextPath", request.getContextPath());
			params.put("serviceUrl", RequestUtils.getServiceUrl(request));

			Report report = reportService.getReport(reportId);

			response.setHeader("Content-Disposition",
					"inline;filename=\"export" + DateUtils.getNowYearMonthDayHHmmss() + ".xls\"");
			response.setContentType("application/vnd.ms-excel");
			if (StringUtils.equals(report.getReportFormat(), "xlsx")) {
				response.setHeader("Content-Disposition",
						"inline;filename=\"export" + DateUtils.getNowYearMonthDayHHmmss() + ".xlsx\"");
				response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			}
			ReportDefinition rdf = ReportContainer.getContainer().getReportDefinition(reportId);
			ReportPreprocessor reportPreprocessor = null;
			byte[] bytes = null;
			InputStream is = null;
			ByteArrayInputStream bais = null;
			ByteArrayOutputStream baos = null;
			BufferedOutputStream bos = null;
			OutputStream outputStream = null;
			try {
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
					bytes = ReportFactory.createReportStream(report, params);
				}
				if (bytes != null) {
					outputStream = response.getOutputStream();
					outputStream.write(bytes);
					outputStream.flush();
					outputStream.close();
					outputStream = null;
					bytes = null;
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				com.glaf.core.util.IOUtils.closeStream(is);
				com.glaf.core.util.IOUtils.closeStream(bais);
				com.glaf.core.util.IOUtils.closeStream(baos);
				com.glaf.core.util.IOUtils.closeStream(bos);
				com.glaf.core.util.IOUtils.closeStream(outputStream);
			}
		}
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		ReportQuery query = new ReportQuery();
		Tools.populate(query, params);

		Long nodeId = RequestUtils.getLong(request, "nodeId");
		if (nodeId != null && nodeId > 0) {
			query.nodeId(nodeId);
		}

		String gridType = ParamUtils.getString(params, "gridType");
		if (gridType == null) {
			gridType = "easyui";
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

		JSONObject result = new JSONObject();
		int total = reportService.getReportCountByQueryCriteria(query);
		if (total > 0) {
			result.put("total", total);
			result.put("totalCount", total);
			result.put("totalRecords", total);
			result.put("start", start);
			result.put("startIndex", start);
			result.put("limit", limit);
			result.put("pageSize", limit);

			if (StringUtils.isNotEmpty(orderName)) {
				query.setSortOrder(orderName);
				if (StringUtils.equals(order, "desc")) {
					query.setSortOrder(" desc ");
				}
			}

			List<Report> list = reportService.getReportsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (Report report : list) {
					JSONObject rowJSON = report.toJsonObject();
					rowsJSON.add(rowJSON);
				}

			}
		} else {
			JSONArray rowsJSON = new JSONArray();
			result.put("rows", rowsJSON);
			result.put("total", total);
		}
		LogUtils.debug(result.toJSONString());
		return result.toString().getBytes("UTF-8");
	}

	@RequestMapping
	public ModelAndView list(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/report/list", modelMap);
	}

	@ResponseBody
	@RequestMapping("/outputHtml")
	public void outputHtml(HttpServletRequest request, HttpServletResponse response) {
		String reportId = request.getParameter("reportId");
		if (StringUtils.isNotEmpty(reportId)) {
			Map<String, Object> params = RequestUtils.getParameterMap(request);

			LoginContext loginContext = RequestUtils.getLoginContext(request);
			params.put("loginContext", loginContext);
			params.put("contextPath", request.getContextPath());
			params.put("serviceUrl", RequestUtils.getServiceUrl(request));

			Report report = reportService.getReport(reportId);

			java.io.PrintWriter writer = null;
			if (StringUtils.equals(report.getReportFormat(), "html")) {
				try {
					response.setCharacterEncoding("UTF-8");
					byte[] bytes = ReportFactory.createReportStream(report, params);
					if (bytes != null) {
						writer = response.getWriter();
						writer.write(new String(bytes));
						writer.flush();
						writer.close();
						writer = null;
						bytes = null;
					}
				} catch (IOException ex) {
					// ex.printStackTrace();
				} finally {
					com.glaf.core.util.IOUtils.closeStream(writer);
				}
			} else if (StringUtils.equals(report.getReportFormat(), "xls")) {
				BufferedInputStream bis = null;
				ByteArrayInputStream bais = null;
				try {
					response.setCharacterEncoding("UTF-8");
					byte[] bytes = ReportFactory.createReportStream(report, params);
					if (bytes != null) {
						bais = new ByteArrayInputStream(bytes);
						bis = new BufferedInputStream(bais);
						HSSFWorkbook wb = new HSSFWorkbook(bis);
						ExcelToHtmlConverter converter = new ExcelToHtmlConverter(
								DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
						converter.setOutputColumnHeaders(false);
						converter.setOutputRowNumbers(false);
						converter.setUseDivsToSpan(false);
						converter.setOutputHiddenColumns(false);
						converter.setOutputHiddenRows(false);
						converter.setOutputLeadingSpacesAsNonBreaking(false);
						converter.processWorkbook(wb);
						String sheetName = wb.getSheetName(0);

						Document document = converter.getDocument();
						ByteArrayOutputStream outStream = new ByteArrayOutputStream();
						DOMSource domSource = new DOMSource(document);
						StreamResult streamResult = new StreamResult(outStream);
						TransformerFactory tf = TransformerFactory.newInstance();
						Transformer serializer = tf.newTransformer();
						serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
						serializer.setOutputProperty(OutputKeys.INDENT, "no");
						serializer.setOutputProperty(OutputKeys.METHOD, "html");
						serializer.transform(domSource, streamResult);
						outStream.close();
						bytes = outStream.toByteArray();
						String text = new String(bytes, "UTF-8");
						text = StringTools.replaceFirst(text, "<h2>" + sheetName + "</h2>", "");
						writer = response.getWriter();
						writer.write(text);
						writer.flush();
						writer.close();
						writer = null;
						bytes = null;
					}
				} catch (Exception ex) {
					// ex.printStackTrace();
					logger.error(ex);
				} finally {
					com.glaf.core.util.IOUtils.closeStream(bis);
					com.glaf.core.util.IOUtils.closeStream(bais);
					com.glaf.core.util.IOUtils.closeStream(writer);
				}
			}
		}
	}

	@RequestMapping("/preview")
	public ModelAndView preview(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		String rowId = ParamUtils.getString(params, "reportId");
		Report report = null;
		if (StringUtils.isNotEmpty(rowId)) {
			report = reportService.getReport(rowId);
			request.setAttribute("report", report);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view);
		}

		String x_view = ViewProperties.getString("report.preview");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view);
		}

		return new ModelAndView("/report/preview");
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("report.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/report/query", modelMap);
	}

	@RequestMapping("/reportTree")
	public ModelAndView reportTree(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String x_view = ViewProperties.getString("report.reportTree");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/report/report_tree", modelMap);
	}

	@RequestMapping("/save")
	public ModelAndView save(HttpServletRequest request, ModelMap modelMap) {
		LoginContext securityContext = RequestUtils.getLoginContext(request);
		String actorId = securityContext.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);

		Report report = new Report();
		Tools.populate(report, params);
		report.setCreateBy(actorId);

		reportService.save(report);

		return this.list(request, modelMap);
	}

	@javax.annotation.Resource
	public void setChartService(IChartService chartService) {
		this.chartService = chartService;
	}

	@javax.annotation.Resource
	public void setReportService(IReportService reportService) {
		this.reportService = reportService;
	}

	@javax.annotation.Resource
	public void setSqlDefinitionService(SqlDefinitionService sqlDefinitionService) {
		this.sqlDefinitionService = sqlDefinitionService;
	}

	@RequestMapping("/update")
	public ModelAndView update(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = RequestUtils.getParameterMap(request);

		String rowId = ParamUtils.getString(params, "reportId");
		Report report = null;
		if (StringUtils.isNotEmpty(rowId)) {
			report = reportService.getReport(rowId);
			Tools.populate(report, params);
			reportService.save(report);
		}

		return this.list(request, modelMap);
	}

	@RequestMapping("/view")
	public ModelAndView view(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		String rowId = ParamUtils.getString(params, "reportId");
		Report report = null;
		if (StringUtils.isNotEmpty(rowId)) {
			report = reportService.getReport(rowId);
			request.setAttribute("report", report);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view);
		}

		String x_view = ViewProperties.getString("report.view");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view);
		}

		return new ModelAndView("/report/view");
	}

}