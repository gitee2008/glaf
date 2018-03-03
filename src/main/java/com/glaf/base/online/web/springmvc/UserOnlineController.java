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

package com.glaf.base.online.web.springmvc;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import com.alibaba.fastjson.*;
import com.glaf.core.cache.CacheFactory;
import com.glaf.core.domain.SystemProperty;
import com.glaf.core.security.*;
import com.glaf.core.service.ISystemPropertyService;
import com.glaf.core.util.*;

import com.glaf.base.online.domain.*;
import com.glaf.base.online.query.*;
import com.glaf.base.online.service.*;

@Controller("/user/online")
@RequestMapping("/user/online")
public class UserOnlineController {
	protected static final Log logger = LogFactory.getLog(UserOnlineController.class);

	protected UserOnlineService userOnlineService;

	protected ISystemPropertyService systemPropertyService;

	public UserOnlineController() {

	}

	@RequestMapping("/doKickOut")
	@ResponseBody
	public byte[] doKickOut(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isSystemAdministrator()) {
			String actorId = request.getParameter("actorId");
			if (!(StringUtils.equals(actorId, "admin"))) {
				try {
					userOnlineService.logout(actorId);
					String cacheKey = Constants.CACHE_LOGIN_CONTEXT_KEY + actorId;
					CacheFactory.remove(Constants.CACHE_LOGIN_CONTEXT_REGION, cacheKey);
					cacheKey = Constants.CACHE_USER_KEY + actorId;
					CacheFactory.remove(Constants.CACHE_USER_REGION, cacheKey);
					logger.info("用户" + actorId + "已经下线！");
					return ResponseUtils.responseJsonResult(true);
				} catch (Exception ex) {
				}
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@RequestMapping("/doRemain")
	@ResponseBody
	public byte[] doRemain(HttpServletRequest request, HttpServletResponse response) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext != null) {
			String actorId = loginContext.getActorId();
			try {
				UserOnline userOnline = userOnlineService.getUserOnline(loginContext.getActorId());
				if (userOnline != null) {
					// 如果有在线信息，更新检查时间
					userOnlineService.remain(loginContext.getActorId());
					return ResponseUtils.responseJsonResult(true);
				} else {
					// 如果被踢出的用户不是系统管理员，注销用户
					if (!loginContext.isSystemAdministrator()) {
						// 退出系统，清除session对象
						RequestUtils.removeLoginUser(request, response);
						request.getSession().removeAttribute(Constants.LOGIN_INFO);
						try {
							userOnlineService.logout(actorId);
							String cacheKey = Constants.CACHE_LOGIN_CONTEXT_KEY + actorId;
							CacheFactory.remove(Constants.CACHE_LOGIN_CONTEXT_REGION, cacheKey);
							cacheKey = Constants.CACHE_USER_KEY + actorId;
							CacheFactory.remove(Constants.CACHE_USER_REGION, cacheKey);
							// com.glaf.shiro.ShiroSecurity.logout();
							logger.info("用户" + actorId + "已经下线！");
							return ResponseUtils.responseJsonResult(true);
						} catch (Exception ex) {
						}
					}
				}
			} catch (Exception ex) {
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request) throws IOException {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		UserOnlineQuery query = new UserOnlineQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setSearchWord(request.getParameter("searchWord"));

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
		int total = userOnlineService.getUserOnlineCountByQueryCriteria(query);
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

			List<UserOnline> list = userOnlineService.getUserOnlinesByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (UserOnline userOnline : list) {
					JSONObject rowJSON = userOnline.toJsonObject();
					rowJSON.put("id", userOnline.getId());
					rowJSON.put("userOnlineId", userOnline.getId());
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

		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isSystemAdministrator()) {
			request.setAttribute("permission", "SystemAdministrator");
		} else {
			request.setAttribute("permission", "anyone");
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/user/online/list", modelMap);
	}

	@RequestMapping("/remain")
	public ModelAndView remain(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		int timeoutSeconds = 300;

		SystemProperty p = systemPropertyService.getSystemProperty("SYS", "login_time_check");
		if (p != null && p.getValue() != null && StringUtils.isNumeric(p.getValue())) {
			timeoutSeconds = Integer.parseInt(p.getValue());
		}

		if (timeoutSeconds > 3600) {
			timeoutSeconds = 3600;
		}

		timeoutSeconds = timeoutSeconds - 60;
		if (timeoutSeconds < 60) {
			timeoutSeconds = 60;
		}
		request.setAttribute("timeoutSeconds", timeoutSeconds);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/user/online/remain", modelMap);
	}

	@javax.annotation.Resource
	public void setSystemPropertyService(ISystemPropertyService systemPropertyService) {
		this.systemPropertyService = systemPropertyService;
	}

	@javax.annotation.Resource
	public void setUserOnlineService(UserOnlineService userOnlineService) {
		this.userOnlineService = userOnlineService;
	}

}
