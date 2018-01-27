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

package com.glaf.matrix.data.web.springmvc;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

import com.glaf.core.security.LoginContext;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;

import com.glaf.matrix.data.domain.ExecutionLog;
import com.glaf.matrix.data.query.ExecutionLogQuery;
import com.glaf.matrix.data.service.ExecutionLogService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/sys/executionLog")
@RequestMapping("/sys/executionLog")
public class ExecutionLogController {
	protected static final Log logger = LogFactory.getLog(ExecutionLogController.class);

	protected ExecutionLogService executionLogService;

	public ExecutionLogController() {

	}

	@RequestMapping("/delete")
	@ResponseBody
	public byte[] delete(HttpServletRequest request) throws IOException {
		String type = request.getParameter("type");
		String businessKey = request.getParameter("businessKey");
		if (StringUtils.isNotEmpty(type) && StringUtils.isNotEmpty(businessKey)) {
			executionLogService.deleteTodayExecutionLogs(type, businessKey);
			return ResponseUtils.responseResult(true);
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/deleteOverdue")
	@ResponseBody
	public byte[] deleteOverdue(HttpServletRequest request) throws IOException {
		Date now = new Date();
		Date dateBefore = DateUtils.getDateBefore(now, 30);
		executionLogService.deleteOverdueExecutionLogs(dateBefore);
		return ResponseUtils.responseResult(true);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		ExecutionLogQuery query = new ExecutionLogQuery();
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

		String dateAfter = request.getParameter("dateAfter");
		if (StringUtils.isNotEmpty(dateAfter)) {
			Date date = null;

			if ("1D".equals(dateAfter)) {
				date = new Date(System.currentTimeMillis() - DateUtils.DAY);
			} else if ("2D".equals(dateAfter)) {
				date = new Date(System.currentTimeMillis() - DateUtils.DAY * 2);
			} else if ("3D".equals(dateAfter)) {
				date = new Date(System.currentTimeMillis() - DateUtils.DAY * 3);
			} else if ("4D".equals(dateAfter)) {
				date = new Date(System.currentTimeMillis() - DateUtils.DAY * 4);
			} else if ("5D".equals(dateAfter)) {
				date = new Date(System.currentTimeMillis() - DateUtils.DAY * 5);
			} else if ("6D".equals(dateAfter)) {
				date = new Date(System.currentTimeMillis() - DateUtils.DAY * 6);
			} else if ("1W".equals(dateAfter)) {
				date = new Date(System.currentTimeMillis() - DateUtils.DAY * 7);
			} else if ("2W".equals(dateAfter)) {
				date = new Date(System.currentTimeMillis() - DateUtils.DAY * 14);
			} else if ("1M".equals(dateAfter)) {
				date = new Date(System.currentTimeMillis() - DateUtils.DAY * 30);
			} else if ("2M".equals(dateAfter)) {
				date = new Date(System.currentTimeMillis() - DateUtils.DAY * 60);
			}

			if (date != null) {
				query.createTimeGreaterThanOrEqual(date);
			}
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
		int total = executionLogService.getExecutionLogCountByQueryCriteria(query);
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

			List<ExecutionLog> list = executionLogService.getExecutionLogsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (ExecutionLog executionLog : list) {
					JSONObject rowJSON = executionLog.toJsonObject();
					rowJSON.put("id", executionLog.getId());
					rowJSON.put("rowId", executionLog.getId());
					rowJSON.put("executionLogId", executionLog.getId());
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

		return new ModelAndView("/matrix/executionLog/list", modelMap);
	}

	@javax.annotation.Resource
	public void setExecutionLogService(ExecutionLogService executionLogService) {
		this.executionLogService = executionLogService;
	}

}
