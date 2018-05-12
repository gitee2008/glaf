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

package com.glaf.sms.web.springmvc;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

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

import com.glaf.core.config.ViewProperties;
import com.glaf.core.identity.User;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;
import com.glaf.sms.domain.SmsServer;
import com.glaf.sms.query.SmsServerQuery;
import com.glaf.sms.service.SmsServerService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/sys/smsServer")
@RequestMapping("/sys/smsServer")
public class SmsServerController {
	protected static final Log logger = LogFactory.getLog(SmsServerController.class);

	protected SmsServerService smsServerService;

	public SmsServerController() {

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
					SmsServer smsServer = smsServerService.getSmsServer(String.valueOf(x));
					if (smsServer != null && (StringUtils.equals(smsServer.getCreateBy(), loginContext.getActorId())
							|| loginContext.isSystemAdministrator())) {

					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			SmsServer smsServer = smsServerService.getSmsServer(String.valueOf(id));
			if (smsServer != null && (StringUtils.equals(smsServer.getCreateBy(), loginContext.getActorId())
					|| loginContext.isSystemAdministrator())) {

				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		SmsServer smsServer = smsServerService.getSmsServer(request.getParameter("id"));
		if (smsServer != null) {
			request.setAttribute("smsServer", smsServer);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("smsServer.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sms/smsServer/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SmsServerQuery query = new SmsServerQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

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
		int total = smsServerService.getSmsServerCountByQueryCriteria(query);
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

			List<SmsServer> list = smsServerService.getSmsServersByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (SmsServer smsServer : list) {
					JSONObject rowJSON = smsServer.toJsonObject();
					rowJSON.put("id", smsServer.getId());
					rowJSON.put("rowId", smsServer.getId());
					rowJSON.put("smsServerId", smsServer.getId());
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

		return new ModelAndView("/sms/smsServer/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("smsServer.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/sms/smsServer/query", modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveSmsServer")
	public byte[] saveSmsServer(HttpServletRequest request) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SmsServer smsServer = new SmsServer();
		try {
			Tools.populate(smsServer, params);
			smsServer.setSubject(request.getParameter("subject"));
			smsServer.setServerIP(request.getParameter("serverIP"));
			smsServer.setPort(RequestUtils.getInt(request, "port"));
			smsServer.setPath(request.getParameter("path"));
			smsServer.setRequestBody(request.getParameter("requestBody"));
			smsServer.setResponseBody(request.getParameter("responseBody"));
			smsServer.setResponseResult(request.getParameter("responseResult"));
			smsServer.setFrequence(RequestUtils.getInt(request, "frequence"));
			smsServer.setRetryTimes(RequestUtils.getInt(request, "retryTimes"));
			smsServer.setAccessKeyId(request.getParameter("accessKeyId"));
			smsServer.setAccessKeySecret(request.getParameter("accessKeySecret"));
			smsServer.setSignName(request.getParameter("signName"));
			smsServer.setTemplateCode(request.getParameter("templateCode"));
			smsServer.setProvider(request.getParameter("provider"));
			smsServer.setKey(request.getParameter("key"));
			smsServer.setType(request.getParameter("type"));

			if (smsServer.getId() != null && !smsServer.getId().isEmpty()) {

			} else {
				smsServer.setCreateBy(actorId);
				smsServer.setCreateTime(new Date());
			}
			String locked = request.getParameter("locked");
			if (locked != null && !locked.isEmpty()) {
				smsServer.setLocked(RequestUtils.getInt(request, "locked"));
			}

			this.smsServerService.save(smsServer);

			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource(name = "com.glaf.sms.service.smsServerService")
	public void setSmsServerService(SmsServerService smsServerService) {
		this.smsServerService = smsServerService;
	}

	@RequestMapping("/view")
	public ModelAndView view(HttpServletRequest request) {
		RequestUtils.setRequestParameterToAttribute(request);

		return new ModelAndView("/sms/smsClientManagement");
	}

}
