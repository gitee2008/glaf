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

package com.glaf.base.modules.tenant.web.springmvc;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.base.modules.sys.model.SysTenant;
import com.glaf.base.modules.sys.model.TreePermission;
import com.glaf.base.modules.sys.query.SysTenantQuery;
import com.glaf.base.modules.sys.query.TreePermissionQuery;
import com.glaf.base.modules.sys.service.SysTenantService;
import com.glaf.base.modules.sys.service.TreePermissionService;
import com.glaf.core.base.DataFile;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.IOUtils;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;

import com.glaf.matrix.data.factory.DataFileFactory;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/tenant")
@RequestMapping("/tenant")
public class TenantController {
	protected static final Log logger = LogFactory.getLog(TenantController.class);

	protected SysTenantService sysTenantService;

	protected TreePermissionService treePermissionService;

	public TenantController() {

	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		SysTenant sysTenant = sysTenantService.getSysTenantByTenantId(loginContext.getTenantId());
		if (sysTenant != null) {
			request.setAttribute("tenant", sysTenant);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("tenant.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/tenant/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SysTenantQuery query = new SysTenantQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);
		query.setType(null);

		List<Long> selected = new ArrayList<Long>();
		selected.add(0L);
		String type = request.getParameter("type");
		TreePermissionQuery query2 = new TreePermissionQuery();
		query2.type(type);
		query2.userId(loginContext.getActorId());
		List<TreePermission> perms = treePermissionService.list(query2);
		if (perms != null && !perms.isEmpty()) {
			for (TreePermission p : perms) {
				selected.add(p.getNodeId());
			}
		}

		query.areaIds(selected);

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
		int total = sysTenantService.getSysTenantCountByQueryCriteria(query);
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

			List<SysTenant> list = sysTenantService.getSysTenantsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (SysTenant sysTenant : list) {
					JSONObject rowJSON = sysTenant.toJsonObject();
					rowJSON.put("id", sysTenant.getId());
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

	@RequestMapping("/list")
	public ModelAndView list(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("tenant.list");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/tenant/list", modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveSysTenant")
	public byte[] saveSysTenant(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		try {
			SysTenant sysTenant = sysTenantService.getSysTenantByTenantId(loginContext.getTenantId());
			if (sysTenant != null && (loginContext.isTenantAdmin()
					|| StringUtils.equals(sysTenant.getCreateBy(), loginContext.getActorId()))) {
				sysTenant.setName(request.getParameter("name"));
				sysTenant.setCode(request.getParameter("code"));
				sysTenant.setPrincipal(request.getParameter("principal"));
				sysTenant.setTelephone(request.getParameter("telephone"));
				this.sysTenantService.save(sysTenant);
				return ResponseUtils.responseJsonResult(true);
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource
	public void setSysTenantService(SysTenantService sysTenantService) {
		this.sysTenantService = sysTenantService;
	}

	@javax.annotation.Resource
	public void setTreePermissionService(TreePermissionService treePermissionService) {
		this.treePermissionService = treePermissionService;
	}

	@RequestMapping("/tcFile")
	public ModelAndView tcFile(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String tenantId = request.getParameter("tenantId");
		if (StringUtils.isNotEmpty(tenantId)) {
			try {
				DataFile dataFile = DataFileFactory.getInstance().getDataFileByFileId(tenantId, tenantId + "_tc_image");
				if (dataFile != null) {
					request.setAttribute("dataFile", dataFile);
				}
			} catch (Exception ex) {
				// ex.printStackTrace();
			}
		}
		return new ModelAndView("/tenant/tcFile", modelMap);
	}

	@RequestMapping("/tcImage")
	public void tcImage(HttpServletRequest request, HttpServletResponse response) {
		String tenantId = request.getParameter("tenantId");
		if (StringUtils.isNotEmpty(tenantId)) {
			InputStream inputStream = null;
			PrintWriter writer = null;
			try {
				DataFile dataFile = DataFileFactory.getInstance().getDataFileByFileId(tenantId, tenantId + "_tc_image");
				if (dataFile != null) {
					inputStream = DataFileFactory.getInstance().getInputStreamById(tenantId, tenantId + "_tc_image");
					if (inputStream != null) {
						ResponseUtils.download(request, response, inputStream, dataFile.getFilename());
						return;
					}
				}
				request.setCharacterEncoding("UTF-8");
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/html; charset=UTF-8");
				writer = response.getWriter();
				writer.println("<h3><font color='red'>未上传相关证件！</font></h3>");
				writer.flush();
				writer.close();
			} catch (Exception ex) {
				// ex.printStackTrace();
			} finally {
				IOUtils.closeStream(inputStream);
				IOUtils.closeStream(writer);
			}
		}
	}

	@RequestMapping("/view")
	public ModelAndView view(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		request.removeAttribute("tenant_edit");
		SysTenant sysTenant = sysTenantService.getSysTenantByTenantId(loginContext.getTenantId());
		if (sysTenant != null) {
			request.setAttribute("tenant", sysTenant);
			if (loginContext.isTenantAdmin()
					|| StringUtils.equals(sysTenant.getCreateBy(), loginContext.getActorId())) {
				request.setAttribute("tenant_edit", true);
			}
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("tenant.view");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/tenant/view", modelMap);
	}

}
