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

package com.glaf.core.access.web.springmvc;

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

import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.*;
import com.glaf.core.util.*;

import com.glaf.core.access.domain.*;
import com.glaf.core.access.query.*;
import com.glaf.core.access.service.*;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/sys/accessUri")
@RequestMapping("/sys/accessUri")
public class AccessUriController {
	protected static final Log logger = LogFactory.getLog(AccessUriController.class);

	protected AccessUriService accessUriService;

	public AccessUriController() {

	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		AccessUri accessUri = accessUriService.getAccessUri(RequestUtils.getLong(request, "id"));
		if (accessUri != null) {
			request.setAttribute("accessUri", accessUri);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("accessUri.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/accessUri/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		AccessUriQuery query = new AccessUriQuery();
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
		int total = accessUriService.getAccessUriCountByQueryCriteria(query);
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

			List<AccessUri> list = accessUriService.getAccessUrisByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (AccessUri accessUri : list) {
					JSONObject rowJSON = accessUri.toJsonObject();
					rowJSON.put("id", accessUri.getId());
					rowJSON.put("rowId", accessUri.getId());
					rowJSON.put("accessUriId", accessUri.getId());
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
		String x_query = request.getParameter("x_query");
		if (StringUtils.equals(x_query, "true")) {
			Map<String, Object> paramMap = RequestUtils.getParameterMap(request);
			String x_complex_query = JsonUtils.encode(paramMap);
			x_complex_query = RequestUtils.encodeString(x_complex_query);
			request.setAttribute("x_complex_query", x_complex_query);
		} else {
			request.setAttribute("x_complex_query", "");
		}

		String requestURI = request.getRequestURI();
		if (request.getQueryString() != null) {
			request.setAttribute("fromUrl", RequestUtils.encodeURL(requestURI + "?" + request.getQueryString()));
		} else {
			request.setAttribute("fromUrl", RequestUtils.encodeURL(requestURI));
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/sys/accessUri/list", modelMap);
	}

	@RequestMapping("/save")
	public ModelAndView save(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = RequestUtils.getParameterMap(request);

		AccessUri accessUri = new AccessUri();
		Tools.populate(accessUri, params);

		accessUri.setLimit(RequestUtils.getInt(request, "limit"));
		accessUri.setTitle(request.getParameter("title"));
		accessUri.setDescription(request.getParameter("description"));

		accessUriService.save(accessUri);

		return this.list(request, modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveAccessUri")
	public byte[] saveAccessUri(HttpServletRequest request) {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		AccessUri accessUri = new AccessUri();
		try {
			Tools.populate(accessUri, params);
			accessUri.setLimit(RequestUtils.getInt(request, "limit"));
			accessUri.setTitle(request.getParameter("title"));
			accessUri.setDescription(request.getParameter("description"));
			this.accessUriService.save(accessUri);

			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {

			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	public @ResponseBody AccessUri saveOrUpdate(HttpServletRequest request, @RequestBody Map<String, Object> model) {
		AccessUri accessUri = new AccessUri();
		try {
			Tools.populate(accessUri, model);
			accessUri.setLimit(ParamUtils.getInt(model, "limit"));
			accessUri.setTitle(ParamUtils.getString(model, "title"));
			accessUri.setDescription(ParamUtils.getString(model, "description"));

			this.accessUriService.save(accessUri);
		} catch (Exception ex) {

			logger.error(ex);
		}
		return accessUri;
	}

	@javax.annotation.Resource
	public void setAccessUriService(AccessUriService accessUriService) {
		this.accessUriService = accessUriService;
	}

	@RequestMapping("/update")
	public ModelAndView update(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = RequestUtils.getParameterMap(request);

		AccessUri accessUri = accessUriService.getAccessUri(RequestUtils.getLong(request, "id"));

		Tools.populate(accessUri, params);

		accessUri.setLimit(RequestUtils.getInt(request, "limit"));
		accessUri.setTitle(request.getParameter("title"));
		accessUri.setDescription(request.getParameter("description"));

		accessUriService.save(accessUri);

		return this.list(request, modelMap);
	}

}
