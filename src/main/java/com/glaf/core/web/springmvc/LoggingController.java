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
package com.glaf.core.web.springmvc;

import java.io.IOException;
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

import com.glaf.core.domain.SysDataLog;
import com.glaf.core.factory.SysLogFactory;
import com.glaf.core.identity.User;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/logging")
@RequestMapping("/logging")
public class LoggingController {
	protected static final Log logger = LogFactory.getLog(LoggingController.class);

	public LoggingController() {

	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug("query params:" + params);
		String moduleId = request.getParameter("moduleId");

		int start = 0;
		int limit = 10;

		int pageNo = ParamUtils.getInt(params, "page");
		limit = ParamUtils.getInt(params, "rows");
		start = (pageNo - 1) * limit;

		if (start < 0) {
			start = 0;
		}

		if (limit <= 0) {
			limit = Paging.DEFAULT_PAGE_SIZE;
		}

		JSONObject result = new JSONObject();
		int total = SysLogFactory.getInstance().getTotal(moduleId);
		if (total > 0) {
			result.put("total", total);
			result.put("totalCount", total);
			result.put("totalRecords", total);
			result.put("start", start);
			result.put("startIndex", start);
			result.put("limit", limit);
			result.put("pageSize", limit);

			Map<String, User> userMap = IdentityFactory.getUserMap();
			List<SysDataLog> list = SysLogFactory.getInstance().getLatestLogs(moduleId, start, limit);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();
				result.put("rows", rowsJSON);
				for (SysDataLog log : list) {
					JSONObject rowJSON = log.toJsonObject();
					if (userMap.get(log.getActorId()) != null) {
						rowJSON.put("createUserName", userMap.get(log.getActorId()).getName());
					} else {
						rowJSON.put("createUserName", log.getActorId());
					}
					rowJSON.put("id", log.getId());
					rowJSON.put("rowId", log.getId());
					rowJSON.put("dataLogId", log.getId());
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

		return new ModelAndView("/sys/logging/list", modelMap);
	}

}
