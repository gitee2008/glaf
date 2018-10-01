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

import org.jxls.common.Context;
import org.jxls.transform.poi.PoiTransformer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.glaf.core.config.DatabaseConnectionConfig;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.domain.Database;
import com.glaf.core.identity.Role;
import com.glaf.core.identity.User;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.StringTools;
import com.glaf.core.util.Tools;

import com.glaf.matrix.export.bean.DataExportBean;
import com.glaf.matrix.export.domain.DataExport;
import com.glaf.matrix.export.domain.DataExportItem;
import com.glaf.matrix.export.query.DataExportQuery;
import com.glaf.matrix.export.service.DataExportService;
import com.glaf.matrix.util.SysParams;
import com.glaf.template.Template;
import com.glaf.template.service.ITemplateService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/matrix/dataExport")
@RequestMapping("/matrix/dataExport")
public class DataExportController {
	protected static final Log logger = LogFactory.getLog(DataExportController.class);

	protected IDatabaseService databaseService;

	protected DataExportService dataExportService;

	protected ITemplateService templateService;

	public DataExportController() {

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
					DataExport dataExport = dataExportService.getDataExport(x);
					if (dataExport != null && (StringUtils.equals(dataExport.getCreateBy(), loginContext.getActorId())
							|| loginContext.isSystemAdministrator())) {

					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			DataExport dataExport = dataExportService.getDataExport(id);
			if (dataExport != null && (StringUtils.equals(dataExport.getCreateBy(), loginContext.getActorId())
					|| loginContext.isSystemAdministrator())) {

				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		DataExport dataExport = dataExportService.getDataExport(RequestUtils.getString(request, "id"));
		if (dataExport != null) {
			request.setAttribute("dataExport", dataExport);
		}

		List<Role> roles = IdentityFactory.getRoles();
		request.setAttribute("roles", roles);

		Map<String, Template> templateMap = templateService.getAllTemplate();
		if (templateMap != null && !templateMap.isEmpty()) {
			request.setAttribute("templates", templateMap.values());
		}

		LoginContext loginContext = RequestUtils.getLoginContext(request);

		DatabaseConnectionConfig cfg = new DatabaseConnectionConfig();
		List<Database> activeDatabases = cfg.getActiveDatabases(loginContext);
		if (activeDatabases != null && !activeDatabases.isEmpty()) {

		}
		request.setAttribute("databases", activeDatabases);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("dataExport.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/matrix/dataExport/edit", modelMap);
	}

	@ResponseBody
	@RequestMapping("/exportXls")
	public void exportXls(HttpServletRequest request, HttpServletResponse response) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SysParams.putInternalParams(params);
		String expId = RequestUtils.getString(request, "expId");
		InputStream is = null;
		ByteArrayInputStream bais = null;
		ByteArrayOutputStream baos = null;
		BufferedOutputStream bos = null;
		try {
			DataExport dataExport = dataExportService.getDataExport(expId);
			if (dataExport != null && StringUtils.equals(dataExport.getActive(), "Y")) {
				Template tpl = templateService.getTemplate(dataExport.getTemplateId());
				if (tpl != null && tpl.getData() != null) {
					boolean hasPerm = true;
					if (StringUtils.isNotEmpty(dataExport.getAllowRoles())) {
						hasPerm = false;
						List<String> roles = StringTools.split(dataExport.getAllowRoles());
						if (loginContext.isSystemAdministrator()) {
							hasPerm = true;
						} else {
							params.put("tenantId", loginContext.getTenantId());
							params.put("userId", loginContext.getActorId());
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
						DataExportBean bean = new DataExportBean();
						dataExport = bean.execute(dataExport.getId(), params);
						for (DataExportItem item : dataExport.getItems()) {
							params.put(item.getName(), item.getDataList());
						}

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
									"export" + DateUtils.getNowYearMonthDayHHmmss() + ".xlsx");
						} else {
							ResponseUtils.download(request, response, data,
									"export" + DateUtils.getNowYearMonthDayHHmmss() + ".xls");
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		DataExportQuery query = new DataExportQuery();
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
		int total = dataExportService.getDataExportCountByQueryCriteria(query);
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

			List<DataExport> list = dataExportService.getDataExportsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (DataExport dataExport : list) {
					JSONObject rowJSON = dataExport.toJsonObject();
					rowJSON.put("id", dataExport.getId());
					rowJSON.put("rowId", dataExport.getId());
					rowJSON.put("dataExportId", dataExport.getId());
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

		return new ModelAndView("/matrix/dataExport/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("dataExport.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/matrix/dataExport/query", modelMap);
	}

	@ResponseBody
	@RequestMapping("/save")
	public byte[] save(HttpServletRequest request) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		DataExport dataExport = new DataExport();
		try {
			Tools.populate(dataExport, params);
			dataExport.setTitle(request.getParameter("title"));
			dataExport.setNodeId(RequestUtils.getLong(request, "nodeId"));
			dataExport.setSrcDatabaseId(RequestUtils.getLong(request, "srcDatabaseId"));
			dataExport.setSyncFlag(request.getParameter("syncFlag"));
			dataExport.setType(request.getParameter("type"));
			dataExport.setActive(request.getParameter("active"));
			dataExport.setAllowRoles(request.getParameter("allowRoles"));
			dataExport.setTemplateId(request.getParameter("templateId"));
			dataExport.setSortNo(RequestUtils.getInt(request, "sortNo"));
			dataExport.setCreateBy(actorId);
			dataExport.setUpdateBy(actorId);

			this.dataExportService.save(dataExport);

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
				String nid = dataExportService.saveAs(expId, actorId);
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

	@javax.annotation.Resource(name = "com.glaf.matrix.export.service.dataExportService")
	public void setDataExportService(DataExportService dataExportService) {
		this.dataExportService = dataExportService;
	}

	@javax.annotation.Resource
	public void setTemplateService(ITemplateService templateService) {
		this.templateService = templateService;
	}

}
