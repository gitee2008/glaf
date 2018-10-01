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

package com.glaf.matrix.export.web.springmvc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentHelper;
import org.jxls.common.Context;
import org.jxls.transform.poi.PoiTransformer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import com.glaf.core.base.BaseTree;
import com.glaf.core.base.TreeModel;
import com.glaf.core.config.DatabaseConnectionConfig;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.domain.Database;
import com.glaf.core.identity.User;
import com.glaf.core.security.LoginContext;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.tree.helper.JacksonTreeHelper;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.Dom4jUtils;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.StaxonUtils;
import com.glaf.core.util.StringTools;
import com.glaf.core.util.Tools;

import com.glaf.matrix.export.domain.XmlExport;
import com.glaf.matrix.export.handler.DataHandler;
import com.glaf.matrix.export.handler.ExportDataHandler;
import com.glaf.matrix.export.handler.XmlDataHandler;
import com.glaf.matrix.export.handler.XmlExportDataHandler;
import com.glaf.matrix.export.query.XmlExportQuery;
import com.glaf.matrix.export.service.XmlExportService;
import com.glaf.matrix.util.SysParams;
import com.glaf.template.Template;
import com.glaf.template.service.ITemplateService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/matrix/xmlExport")
@RequestMapping("/matrix/xmlExport")
public class XmlExportController {
	protected static final Log logger = LogFactory.getLog(XmlExportController.class);

	protected IDatabaseService databaseService;

	protected XmlExportService xmlExportService;

	protected ITemplateService templateService;

	public XmlExportController() {

	}

	@ResponseBody
	@RequestMapping("/delete")
	public byte[] delete(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String id = RequestUtils.getString(request, "id");
		String ids = request.getParameter("ids");
		if (StringUtils.isNotEmpty(ids)) {
			StringTokenizer token = new StringTokenizer(ids, ",");
			while (token.hasMoreTokens()) {
				String x = token.nextToken();
				if (StringUtils.isNotEmpty(x)) {
					XmlExport xmlExport = xmlExportService.getXmlExport(x);
					if (xmlExport != null && (StringUtils.equals(xmlExport.getCreateBy(), loginContext.getActorId())
							|| loginContext.isSystemAdministrator())) {

					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			XmlExport xmlExport = xmlExportService.getXmlExport(id);
			if (xmlExport != null && (StringUtils.equals(xmlExport.getCreateBy(), loginContext.getActorId())
					|| loginContext.isSystemAdministrator())) {

				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		long nodeId = RequestUtils.getLong(request, "nodeId");
		long nodeParentId = RequestUtils.getLong(request, "nodeParentId");
		XmlExport xmlExport = null;

		if (StringUtils.isNotEmpty(RequestUtils.getString(request, "id"))) {
			xmlExport = xmlExportService.getXmlExport(RequestUtils.getString(request, "id"));
		} else {
			if (nodeId > 0) {
				xmlExport = xmlExportService.getXmlExportByNodeId(nodeId);
			}
		}

		if (xmlExport != null) {
			request.setAttribute("xmlExport", xmlExport);
			XmlExport parent = xmlExportService.getXmlExportByNodeId(xmlExport.getNodeParentId());
			if (parent != null) {
				request.setAttribute("parent", parent);
			}
		}

		if (nodeParentId > 0) {
			XmlExport parent = xmlExportService.getXmlExportByNodeId(nodeParentId);
			if (parent != null) {
				request.setAttribute("parent", parent);
			}
			List<XmlExport> children = xmlExportService.getAllChildren(nodeParentId);
			if (children != null && !children.isEmpty()) {
				if (xmlExport != null) {
					children.remove(xmlExport);
				}
				request.setAttribute("children", children);
			}
		} else {
			List<XmlExport> children = xmlExportService.getAllChildren(0);
			if (children != null && !children.isEmpty()) {
				if (xmlExport != null) {
					children.remove(xmlExport);
				}
				request.setAttribute("children", children);
			}
		}

		List<Integer> sortNoList = new ArrayList<Integer>();
		for (int i = 1; i < 50; i++) {
			sortNoList.add(i);
		}
		request.setAttribute("sortNoList", sortNoList);

		LoginContext loginContext = RequestUtils.getLoginContext(request);

		DatabaseConnectionConfig cfg = new DatabaseConnectionConfig();
		List<Database> activeDatabases = cfg.getActiveDatabases(loginContext);
		if (activeDatabases != null && !activeDatabases.isEmpty()) {

		}
		request.setAttribute("databases", activeDatabases);

		Map<String, Template> templateMap = templateService.getAllTemplate();
		if (templateMap != null && !templateMap.isEmpty()) {
			request.setAttribute("templates", templateMap.values());
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("xmlExport.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/matrix/xmlExport/edit", modelMap);
	}

	@ResponseBody
	@RequestMapping("/exportDef")
	public void exportDef(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SysParams.putInternalParams(params);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		params.put("login_user", loginContext.getUser());
		params.put("login_userid", loginContext.getActorId());
		params.put("login_tenantid", loginContext.getTenantId());
		String expId = RequestUtils.getString(request, "expId");
		long nodeId = RequestUtils.getLong(request, "nodeId");
		try {
			XmlExport xmlExport = null;
			if (nodeId > 0) {
				xmlExport = xmlExportService.getXmlExportByNodeId(nodeId);
			} else {
				xmlExport = xmlExportService.getXmlExport(expId);
			}
			if (xmlExport != null && StringUtils.equals(xmlExport.getActive(), "Y")) {
				JSONObject jsonObject = xmlExportService.exportJson(xmlExport.getId());
				ResponseUtils.download(request, response, jsonObject.toJSONString().getBytes("UTF-8"),
						xmlExport.getTitle() + DateUtils.getNowYearMonthDayHHmmss() + ".json");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
	}

	@ResponseBody
	@RequestMapping("/exportJson")
	public void exportJson(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SysParams.putInternalParams(params);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		params.put("login_user", loginContext.getUser());
		params.put("login_userid", loginContext.getActorId());
		params.put("login_tenantid", loginContext.getTenantId());
		long databaseId = RequestUtils.getLong(request, "databaseId");
		String expId = RequestUtils.getString(request, "expId");
		try {
			XmlExport xmlExport = xmlExportService.getXmlExport(expId);
			if (xmlExport != null && StringUtils.equals(xmlExport.getActive(), "Y")) {
				boolean hasPerm = true;
				if (StringUtils.isNotEmpty(xmlExport.getAllowRoles())) {
					hasPerm = false;
					List<String> roles = StringTools.split(xmlExport.getAllowRoles());
					if (loginContext.isSystemAdministrator()) {
						hasPerm = true;
					}
					Collection<String> permissions = loginContext.getPermissions();
					for (String perm : permissions) {
						if (roles.contains(perm)) {
							hasPerm = true;
							break;
						}
					}
				}
				if (hasPerm) {
					xmlExport.setParameter(params);
					XmlDataHandler xmlDataHandler = new XmlExportDataHandler();
					org.dom4j.Document document = DocumentHelper.createDocument();
					org.dom4j.Element root = document.addElement(xmlExport.getXmlTag());
					xmlExport.setElement(root);
					xmlDataHandler.addChild(xmlExport, root, databaseId);
					byte[] data = Dom4jUtils.getBytesFromPrettyDocument(document, "UTF-8");
					data = StaxonUtils.xml2json(new String(data, "UTF-8")).getBytes();
					ResponseUtils.download(request, response, data,
							xmlExport.getTitle() + DateUtils.getNowYearMonthDayHHmmss() + ".json");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
	}

	@ResponseBody
	@RequestMapping("/exportVar")
	public void exportVar(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SysParams.putInternalParams(params);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		params.put("login_user", loginContext.getUser());
		params.put("login_userid", loginContext.getActorId());
		params.put("login_tenantid", loginContext.getTenantId());
		String expId = RequestUtils.getString(request, "expId");
		long databaseId = RequestUtils.getLong(request, "databaseId");
		PrintWriter writer = null;
		try {
			XmlExport xmlExport = xmlExportService.getXmlExport(expId);
			if (xmlExport != null && StringUtils.equals(xmlExport.getActive(), "Y")) {
				xmlExport.setParameter(params);
				DataHandler dataHandler = new ExportDataHandler();
				dataHandler.addChild(xmlExport, params, databaseId);
				// JSONObject json = new JSONObject();
				StringBuilder buff = new StringBuilder();
				Set<Entry<String, Object>> entrySet = params.entrySet();
				for (Entry<String, Object> entry : entrySet) {
					String key = entry.getKey();
					Object value = entry.getValue();
					// json.put(key, value);
					if (value != null) {
						buff.append("<div><span style='font:bold 14px 宋体;color:#FF6666;'>").append(key)
								.append("</span>=");
						buff.append("<span style='font:bold 14px 宋体;color:#3399CC;'>");
						if (value instanceof java.util.Date) {
							buff.append(DateUtils.getDateTime((java.util.Date) value));
						} else {
							buff.append(JSON.toJSONString(value));
						}
						buff.append("</span></div>");
					}
				}
				writer = response.getWriter();
				writer.println(buff.toString());
				writer.flush();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}

	@ResponseBody
	@RequestMapping("/exportXls")
	public void exportXls(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SysParams.putInternalParams(params);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		params.put("login_user", loginContext.getUser());
		params.put("login_userid", loginContext.getActorId());
		params.put("login_tenantid", loginContext.getTenantId());
		String expId = RequestUtils.getString(request, "expId");
		long databaseId = RequestUtils.getLong(request, "databaseId");
		String templateId = RequestUtils.getString(request, "templateId");
		InputStream is = null;
		ByteArrayInputStream bais = null;
		ByteArrayOutputStream baos = null;
		BufferedOutputStream bos = null;
		try {
			XmlExport xmlExport = xmlExportService.getXmlExport(expId);
			if (xmlExport != null && StringUtils.equals(xmlExport.getActive(), "Y")) {

				boolean hasPerm = true;
				if (StringUtils.isNotEmpty(xmlExport.getAllowRoles())) {
					hasPerm = false;
					List<String> roles = StringTools.split(xmlExport.getAllowRoles());
					if (loginContext.isSystemAdministrator()) {
						hasPerm = true;
					}
					Collection<String> permissions = loginContext.getPermissions();
					for (String perm : permissions) {
						if (roles.contains(perm)) {
							hasPerm = true;
							break;
						}
					}
				}
				if (hasPerm) {
					if (StringUtils.isEmpty(templateId)) {
						templateId = xmlExport.getTemplateId();
					}
					if (StringUtils.isNotEmpty(templateId)) {
						Template tpl = templateService.getTemplate(templateId);
						if (tpl != null && tpl.getData() != null) {

							xmlExport.setParameter(params);
							DataHandler dataHandler = new ExportDataHandler();
							dataHandler.addChild(xmlExport, params, databaseId);

							bais = new ByteArrayInputStream(tpl.getData());
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
							IOUtils.closeQuietly(is);
							IOUtils.closeQuietly(bais);

							bos.flush();
							baos.flush();
							byte[] data = baos.toByteArray();

							if (StringUtils.endsWithIgnoreCase(tpl.getDataFile(), ".xlsx")) {
								ResponseUtils.download(request, response, data,
										xmlExport.getTitle() + DateUtils.getNowYearMonthDayHHmmss() + ".xlsx");
							} else {
								ResponseUtils.download(request, response, data,
										xmlExport.getTitle() + DateUtils.getNowYearMonthDayHHmmss() + ".xls");
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
	}

	@ResponseBody
	@RequestMapping("/exportXml")
	public void exportXml(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SysParams.putInternalParams(params);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		params.put("login_user", loginContext.getUser());
		params.put("login_userid", loginContext.getActorId());
		params.put("login_tenantid", loginContext.getTenantId());
		long databaseId = RequestUtils.getLong(request, "databaseId");
		String expId = RequestUtils.getString(request, "expId");
		try {
			XmlExport xmlExport = xmlExportService.getXmlExport(expId);
			if (xmlExport != null && StringUtils.equals(xmlExport.getActive(), "Y")) {
				boolean hasPerm = true;
				if (StringUtils.isNotEmpty(xmlExport.getAllowRoles())) {
					hasPerm = false;
					List<String> roles = StringTools.split(xmlExport.getAllowRoles());
					if (loginContext.isSystemAdministrator()) {
						hasPerm = true;
					}
					Collection<String> permissions = loginContext.getPermissions();
					for (String perm : permissions) {
						if (roles.contains(perm)) {
							hasPerm = true;
							break;
						}
					}
				}
				if (hasPerm) {
					xmlExport.setParameter(params);
					XmlDataHandler xmlDataHandler = new XmlExportDataHandler();
					org.dom4j.Document document = DocumentHelper.createDocument();
					org.dom4j.Element root = document.addElement(xmlExport.getXmlTag());
					xmlExport.setElement(root);
					xmlDataHandler.addChild(xmlExport, root, databaseId);
					byte[] data = Dom4jUtils.getBytesFromPrettyDocument(document, "UTF-8");
					ResponseUtils.download(request, response, data,
							xmlExport.getTitle() + DateUtils.getNowYearMonthDayHHmmss() + ".xml");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
	}

	@ResponseBody
	@RequestMapping("/importDef")
	public void importDef(HttpServletRequest request, HttpServletResponse response, MultipartFile mFile)
			throws IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SysParams.putInternalParams(params);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		params.put("login_user", loginContext.getUser());
		params.put("login_userid", loginContext.getActorId());
		params.put("login_tenantid", loginContext.getTenantId());
		long nodeId = RequestUtils.getLong(request, "nodeId");
		String expId = RequestUtils.getString(request, "expId");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			XmlExport xmlExport = null;
			if (nodeId > 0) {
				xmlExport = xmlExportService.getXmlExportByNodeId(nodeId);
			} else {
				xmlExport = xmlExportService.getXmlExport(expId);
			}
			if (mFile != null && xmlExport != null && StringUtils.equals(xmlExport.getActive(), "Y")) {
				JSONObject jsonObject = JSON.parseObject(new String(mFile.getBytes(), "UTF-8"));
				xmlExportService.importAll(xmlExport.getId(), jsonObject);
				writer.println("<h3><span style='color:#339933;'>导入成功！</span><h3>");
				writer.flush();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
			if (writer != null) {
				writer.println("<h3><span style='color:#ff0066;'>导入失败！</span><h3><br>");
				writer.println(ex.getCause().getMessage());
				writer.flush();
			}
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}

	@ResponseBody
	@RequestMapping("/importDef2")
	public void importDef2(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SysParams.putInternalParams(params);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		params.put("login_user", loginContext.getUser());
		params.put("login_userid", loginContext.getActorId());
		params.put("login_tenantid", loginContext.getTenantId());
		long nodeId = RequestUtils.getLong(request, "nodeId");
		String expId = RequestUtils.getString(request, "expId");
		String json = RequestUtils.getString(request, "json");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			XmlExport xmlExport = null;
			if (nodeId > 0) {
				xmlExport = xmlExportService.getXmlExportByNodeId(nodeId);
			} else {
				xmlExport = xmlExportService.getXmlExport(expId);
			}
			if (StringUtils.isNotEmpty(json) && xmlExport != null && StringUtils.equals(xmlExport.getActive(), "Y")) {
				JSONObject jsonObject = JSON.parseObject(json);
				xmlExportService.importAll(xmlExport.getId(), jsonObject);
				writer.println("<h3>导入成功！<h3>");
				writer.flush();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
			if (writer != null) {
				writer.println("<h3>导入失败！<h3><br>");
				writer.println(ex.getCause().getMessage());
				writer.flush();
			}
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		XmlExportQuery query = new XmlExportQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);
		/**
		 * 此处业务逻辑需自行调整
		 */
		if (!loginContext.isSystemAdministrator()) {
			String actorId = loginContext.getActorId();
			query.createBy(actorId);
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
		int total = xmlExportService.getXmlExportCountByQueryCriteria(query);
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

			List<XmlExport> list = xmlExportService.getXmlExportsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (XmlExport xmlExport : list) {
					JSONObject rowJSON = xmlExport.toJsonObject();
					rowJSON.put("id", xmlExport.getId());
					rowJSON.put("rowId", xmlExport.getId());
					rowJSON.put("xmlExportId", xmlExport.getId());
					rowJSON.put("startIndex", ++start);
					rowsJSON.add(rowJSON);
				}

			}
		} else {
			JSONArray rowsJSON = new JSONArray();
			result.put("rows", rowsJSON);
			result.put("total", total);
		}
		return result.toJSONString().getBytes("UTF-8");
	}

	@RequestMapping
	public ModelAndView list(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/matrix/xmlExport/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("xmlExport.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/matrix/xmlExport/query", modelMap);
	}

	@ResponseBody
	@RequestMapping("/save")
	public byte[] save(HttpServletRequest request) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		XmlExport xmlExport = new XmlExport();
		try {
			Tools.populate(xmlExport, params);
			xmlExport.setName(request.getParameter("name"));
			xmlExport.setMapping(request.getParameter("mapping"));
			xmlExport.setTitle(request.getParameter("title"));
			xmlExport.setSql(request.getParameter("sql"));
			xmlExport.setResultFlag(request.getParameter("resultFlag"));
			xmlExport.setNodeParentId(RequestUtils.getLong(request, "nodeParentId"));
			xmlExport.setLeafFlag(request.getParameter("leafFlag"));
			xmlExport.setTreeFlag(request.getParameter("treeFlag"));
			xmlExport.setType(request.getParameter("type"));
			xmlExport.setActive(request.getParameter("active"));
			xmlExport.setXmlTag(request.getParameter("xmlTag"));
			xmlExport.setTemplateId(request.getParameter("templateId"));
			xmlExport.setAllowRoles(request.getParameter("allowRoles"));
			xmlExport.setExternalAttrsFlag(request.getParameter("externalAttrsFlag"));
			xmlExport.setInterval(RequestUtils.getInt(request, "interval"));
			xmlExport.setSortNo(RequestUtils.getInt(request, "sortNo"));
			xmlExport.setCreateBy(actorId);
			xmlExport.setUpdateBy(actorId);
			if (StringUtils.isEmpty(xmlExport.getActive())) {
				xmlExport.setActive("Y");
			}

			this.xmlExportService.save(xmlExport);

			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			// ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@ResponseBody
	@RequestMapping("/saveAs")
	public byte[] saveAs(HttpServletRequest request) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		try {
			String expId = RequestUtils.getString(request, "expId");
			if (expId != null) {
				String nid = xmlExportService.saveAs(expId, actorId);
				if (nid != null) {
					return ResponseUtils.responseJsonResult(true);
				}
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource
	public void setDatabaseService(IDatabaseService databaseService) {
		this.databaseService = databaseService;
	}

	@javax.annotation.Resource
	public void setTemplateService(ITemplateService templateService) {
		this.templateService = templateService;
	}

	@javax.annotation.Resource(name = "com.glaf.matrix.export.service.xmlExportService")
	public void setXmlExportService(XmlExportService xmlExportService) {
		this.xmlExportService = xmlExportService;
	}

	@RequestMapping("/showExport")
	public ModelAndView showExport(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		long nodeId = RequestUtils.getLong(request, "nodeId");
		long nodeParentId = RequestUtils.getLong(request, "nodeParentId");
		XmlExport xmlExport = null;
		if (StringUtils.isNotEmpty(RequestUtils.getString(request, "id"))) {
			xmlExport = xmlExportService.getXmlExport(RequestUtils.getString(request, "id"));
		} else {
			if (nodeId > 0) {
				xmlExport = xmlExportService.getXmlExportByNodeId(nodeId);
			}
		}

		if (xmlExport != null) {
			request.setAttribute("xmlExport", xmlExport);
			XmlExport parent = xmlExportService.getXmlExportByNodeId(xmlExport.getNodeParentId());
			if (parent != null) {
				request.setAttribute("parent", parent);
			}
		}

		if (nodeParentId > 0) {
			XmlExport parent = xmlExportService.getXmlExportByNodeId(nodeParentId);
			if (parent != null) {
				request.setAttribute("parent", parent);
			}
			List<XmlExport> children = xmlExportService.getAllChildren(nodeParentId);
			if (children != null && !children.isEmpty()) {
				if (xmlExport != null) {
					children.remove(xmlExport);
				}
				request.setAttribute("children", children);
			}
		} else {
			List<XmlExport> children = xmlExportService.getAllChildren(0);
			if (children != null && !children.isEmpty()) {
				if (xmlExport != null) {
					children.remove(xmlExport);
				}
				request.setAttribute("children", children);
			}
		}

		List<Integer> sortNoList = new ArrayList<Integer>();
		for (int i = 1; i < 50; i++) {
			sortNoList.add(i);
		}
		request.setAttribute("sortNoList", sortNoList);

		LoginContext loginContext = RequestUtils.getLoginContext(request);

		DatabaseConnectionConfig cfg = new DatabaseConnectionConfig();
		List<Database> activeDatabases = cfg.getActiveDatabases(loginContext);
		if (activeDatabases != null && !activeDatabases.isEmpty()) {

		}
		request.setAttribute("databases", activeDatabases);

		Map<String, Template> templateMap = templateService.getAllTemplate();
		if (templateMap != null && !templateMap.isEmpty()) {
			request.setAttribute("templates", templateMap.values());
		}

		request.setAttribute("ts", System.currentTimeMillis());

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("xmlExport.showExport");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/matrix/xmlExport/showExport", modelMap);
	}

	@RequestMapping("/showImportDef")
	public ModelAndView showImportDef(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		long nodeId = RequestUtils.getLong(request, "nodeId");
		XmlExport xmlExport = null;
		if (StringUtils.isNotEmpty(RequestUtils.getString(request, "id"))) {
			xmlExport = xmlExportService.getXmlExport(RequestUtils.getString(request, "id"));
		} else {
			if (nodeId > 0) {
				xmlExport = xmlExportService.getXmlExportByNodeId(nodeId);
			}
		}

		if (xmlExport != null) {
			request.setAttribute("xmlExport", xmlExport);
			XmlExport parent = xmlExportService.getXmlExportByNodeId(xmlExport.getNodeParentId());
			if (parent != null) {
				request.setAttribute("parent", parent);
			}
		}

		request.setAttribute("ts", System.currentTimeMillis());

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("xmlExport.showImportDef");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/matrix/xmlExport/showImportDef", modelMap);
	}

	@ResponseBody
	@RequestMapping("/treeJson")
	public byte[] treeJson(HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.debug("------------------------treeJson--------------------");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		long parentNodeId = RequestUtils.getLong(request, "parentNodeId");
		List<TreeModel> treeModels = new ArrayList<TreeModel>();
		JacksonTreeHelper treeHelper = new JacksonTreeHelper();
		ArrayNode responseJSON = new ObjectMapper().createArrayNode();
		try {
			List<XmlExport> children = xmlExportService.getAllChildren(parentNodeId);
			if (children != null && !children.isEmpty()) {
				for (XmlExport model : children) {
					TreeModel tree = new BaseTree();
					tree.setId(model.getNodeId());
					tree.setParentId(model.getNodeParentId());
					tree.setName(model.getTitle() + "[" + model.getXmlTag() + "]");
					tree.setSortNo(model.getSortNo());
					tree.setLevel(model.getLevel());
					treeModels.add(tree);
				}
				java.util.Collections.sort(treeModels);
				responseJSON = treeHelper.getTreeArrayNode(treeModels);
				logger.debug(responseJSON.toString());
			}
			return responseJSON.toString().getBytes("UTF-8");
		} catch (IOException e) {
			return responseJSON.toString().getBytes();
		}
	}

}
