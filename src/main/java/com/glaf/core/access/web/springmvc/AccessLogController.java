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
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import com.alibaba.fastjson.*;

import com.glaf.core.identity.User;
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

@Controller("/sys/accessLog")
@RequestMapping("/sys/accessLog")
public class AccessLogController {
	protected static final Log logger = LogFactory.getLog(AccessLogController.class);

	protected AccessLogService accessLogService;

	public AccessLogController() {

	}

	@RequestMapping("/denyList")
	public ModelAndView denyList(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		Map<String, User> userMap = IdentityFactory.getUserMap();
		Map<String, Long> accessDenyMap = ContextUtils.getAccessDenyMap();
		if (accessDenyMap != null && !accessDenyMap.isEmpty() && userMap != null) {
			List<User> users = new ArrayList<User>();
			Set<Entry<String, Long>> entrySet = accessDenyMap.entrySet();
			for (Entry<String, Long> entry : entrySet) {
				String key = entry.getKey();
				User user = userMap.get(key);
				users.add(user);
			}
			request.setAttribute("users", users);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/sys/accessLog/denyList", modelMap);
	}

	@ResponseBody
	@RequestMapping("/denyListJson")
	public byte[] denyListJson(HttpServletRequest request, ModelMap modelMap) throws IOException {
		RequestUtils.setRequestParameterToAttribute(request);
		Map<String, User> userMap = IdentityFactory.getUserMap();
		Map<String, Long> accessDenyMap = ContextUtils.getAccessDenyMap();
		JSONObject result = new JSONObject();
		if (accessDenyMap != null && !accessDenyMap.isEmpty() && userMap != null) {
			List<User> users = new ArrayList<User>();
			Set<Entry<String, Long>> entrySet = accessDenyMap.entrySet();
			for (Entry<String, Long> entry : entrySet) {
				String key = entry.getKey();
				User user = userMap.get(key);
				users.add(user);
			}
			if (users != null && !users.isEmpty()) {
				result.put("total", users.size());
				JSONArray rowsJSON = new JSONArray();
				for (User user : users) {
					JSONObject rowJSON = user.toJsonObject();
					rowJSON.put("userId_enc", RequestUtils.encodeString(user.getActorId()));
					rowsJSON.add(rowJSON);
				}
				result.put("rows", rowsJSON);
			}
		}
		return result.toJSONString().getBytes("UTF-8");
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		AccessLogQuery query = new AccessLogQuery();
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
		int total = accessLogService.getAccessLogCountByQueryCriteria(query);
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

			List<AccessLog> list = accessLogService.getAccessLogsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();
				result.put("rows", rowsJSON);
				Map<String, User> userMap = IdentityFactory.getUserMap();
				for (AccessLog accessLog : list) {
					JSONObject rowJSON = accessLog.toJsonObject();
					rowJSON.put("id", accessLog.getId());
					rowJSON.put("rowId", accessLog.getId());
					rowJSON.put("accessLogId", accessLog.getId());
					rowJSON.put("startIndex", ++start);
					rowJSON.put("userId_enc", RequestUtils.encodeString(accessLog.getUserId()));
					if (userMap.get(accessLog.getUserId()) != null) {
						rowJSON.put("userName", userMap.get(accessLog.getUserId()).getName());
					} else {
						rowJSON.put("userName", accessLog.getUserId());
					}
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

		return new ModelAndView("/sys/accessLog/list", modelMap);
	}

	@javax.annotation.Resource
	public void setAccessLogService(AccessLogService accessLogService) {
		this.accessLogService = accessLogService;
	}

}
