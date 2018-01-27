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
import java.io.StringWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.glaf.core.security.LoginContext;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.matrix.data.domain.SqlDefinition;
import com.glaf.matrix.data.factory.SqlQueryFactory;
import com.glaf.matrix.data.service.SqlDefinitionService;

@Controller("/sql/query")
@RequestMapping("/sql/query")
public class SqlQueryController {
	protected static final Log logger = LogFactory.getLog(SqlQueryController.class);

	protected IDatabaseService databaseService;

	protected SqlDefinitionService sqlDefinitionService;

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		String uuid = request.getParameter("uuid");
		SqlDefinition sqlDefinition = sqlDefinitionService.getSqlDefinitionByUUID(uuid);
		if (sqlDefinition != null) {
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

			long databaseId = RequestUtils.getLong(request, "databaseId");

			JSONObject result = SqlQueryFactory.getInstance().getJson(sqlDefinition.getId(), databaseId,
					loginContext.getActorId(), params, start, limit);
			return result.toJSONString().getBytes("UTF-8");
		}
		JSONObject result = new JSONObject();
		return result.toJSONString().getBytes();
	}

	@RequestMapping("/view")
	public void view(HttpServletRequest request, HttpServletResponse response) throws IOException {
		RequestUtils.setRequestParameterToAttribute(request);
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");

		StringWriter sw = new java.io.StringWriter();
		sw.flush();
	}

	@javax.annotation.Resource
	public void setDatabaseService(IDatabaseService databaseService) {
		this.databaseService = databaseService;
	}

	@javax.annotation.Resource
	public void setSqlDefinitionService(SqlDefinitionService sqlDefinitionService) {
		this.sqlDefinitionService = sqlDefinitionService;
	}
}
