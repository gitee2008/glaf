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

package com.glaf.base.info.web.springmvc;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import com.alibaba.fastjson.*;
import com.glaf.core.base.DataFile;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.identity.User;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.service.ISystemParamService;
import com.glaf.core.util.*;
import com.glaf.matrix.data.query.DataFileQuery;
import com.glaf.matrix.data.service.IDataFileService;
import com.glaf.base.info.model.*;
import com.glaf.base.info.query.*;
import com.glaf.base.info.service.*;

@Controller("/base/info")
@RequestMapping("/base/info")
public class PublicInfoController {
	protected static final Log logger = LogFactory.getLog(PublicInfoController.class);

	protected IDataFileService dataFileService;

	protected PublicInfoService publicInfoService;

	protected ISystemParamService systemParamService;

	public PublicInfoController() {

	}

	@ResponseBody
	@RequestMapping("/detail")
	public byte[] detail(HttpServletRequest request) throws IOException {
		PublicInfo publicInfo = publicInfoService.getPublicInfo(request.getParameter("id"));
		try {
			publicInfo.setViewCount(publicInfo.getViewCount() + 1);
			publicInfoService.save(publicInfo);
		} catch (Exception ex) {
			logger.error(ex);
		}

		String serviceKey = request.getParameter("serviceKey");
		if (StringUtils.isEmpty(serviceKey)) {
			serviceKey = "PublicInfo";
		}

		JSONObject rowJSON = publicInfo.toJsonObject();

		DataFileQuery query = new DataFileQuery();
		query.tenantId(publicInfo.getTenantId());
		query.businessKey(publicInfo.getId());
		query.serviceKey(serviceKey);
		List<DataFile> dataFiles = dataFileService.getDataFileList(query);
		if (dataFiles != null && !dataFiles.isEmpty()) {
			JSONArray array = new JSONArray();
			if (dataFiles != null && !dataFiles.isEmpty()) {
				for (DataFile model : dataFiles) {
					JSONObject jsonObject = model.toJsonObject();
					array.add(jsonObject);
				}
			}
			rowJSON.put("files", array);
		}

		return rowJSON.toJSONString().getBytes("UTF-8");
	}

	@RequestMapping("/indexList")
	public ModelAndView indexList(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);

		String serviceKey = request.getParameter("serviceKey");
		if (StringUtils.isEmpty(serviceKey)) {
			serviceKey = "PublicInfo";
		}

		PublicInfoQuery query = new PublicInfoQuery();
		Tools.populate(query, params);
		query.setPublishFlag(1);
		query.serviceKey(serviceKey);
		query.tenantId(loginContext.getTenantId());

		List<PublicInfo> rows = publicInfoService.list(query);
		request.setAttribute("rows", rows);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/base/info/indexList", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		RequestUtils.setRequestParameterToAttribute(request);

		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);

		String serviceKey = request.getParameter("serviceKey");
		if (StringUtils.isEmpty(serviceKey)) {
			serviceKey = "PublicInfo";
		}

		PublicInfoQuery query = new PublicInfoQuery();
		Tools.populate(query, params);
		query.setPublishFlag(1);
		query.serviceKey(serviceKey);
		query.tenantId(loginContext.getTenantId());

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
			limit = PageResult.DEFAULT_PAGE_SIZE;
		}

		JSONObject result = new JSONObject();
		int total = publicInfoService.getPublicInfoCountByQueryCriteria(query);
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

			List<PublicInfo> list = publicInfoService.getPublicInfosByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (PublicInfo publicInfo : list) {

					JSONObject rowJSON = publicInfo.toJsonObject();
					rowJSON.put("id", publicInfo.getId());
					rowJSON.put("publicInfoId", publicInfo.getId());
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

		return new ModelAndView("/base/info/list", modelMap);
	}

	@javax.annotation.Resource
	public void setDataFileService(IDataFileService dataFileService) {
		this.dataFileService = dataFileService;
	}

	@javax.annotation.Resource
	public void setPublicInfoService(PublicInfoService publicInfoService) {
		this.publicInfoService = publicInfoService;
	}

	@javax.annotation.Resource
	public void setSystemParamService(ISystemParamService systemParamService) {
		this.systemParamService = systemParamService;
	}

	@RequestMapping("/view")
	public ModelAndView view(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		PublicInfo publicInfo = publicInfoService.getPublicInfo(request.getParameter("id"));
		if (publicInfo != null) {
			request.setAttribute("publicInfo", publicInfo);
			User user = IdentityFactory.getUser(publicInfo.getCreateBy());
			if (user != null) {
				publicInfo.setCreateByName(user.getName());
			}
			try {
				publicInfoService.updateViewCount(publicInfo.getId());
			} catch (Exception ex) {
				logger.error(ex);
			}

			JSONObject rowJSON = publicInfo.toJsonObject();
			request.setAttribute("x_json", rowJSON.toJSONString());

			String serviceKey = request.getParameter("serviceKey");
			if (StringUtils.isEmpty(serviceKey)) {
				serviceKey = "PublicInfo";
			}

			DataFileQuery query = new DataFileQuery();
			query.tenantId(publicInfo.getTenantId());
			query.businessKey(publicInfo.getId());
			query.serviceKey(serviceKey);

			List<DataFile> dataFiles = dataFileService.getDataFileList(query);
			request.setAttribute("dataFiles", dataFiles);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view);
		}

		String x_view = ViewProperties.getString("publicInfo.view");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view);
		}

		return new ModelAndView("/base/info/view");
	}

}
