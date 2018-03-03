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

package com.glaf.heathcare.web.springmvc;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
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

import com.glaf.base.modules.sys.model.SysTree;
import com.glaf.base.modules.sys.service.DictoryService;
import com.glaf.base.modules.sys.service.SysTreeService;
 
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.StringTools;
import com.glaf.core.util.Tools;
import com.glaf.heathcare.SysConfig;
import com.glaf.heathcare.domain.DietaryCount;
import com.glaf.heathcare.query.DietaryCountQuery;
import com.glaf.heathcare.service.DietaryCountService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/dietaryCount")
@RequestMapping("/heathcare/dietaryCount")
public class DietaryCountController {
	protected static final Log logger = LogFactory.getLog(DietaryCountController.class);

	protected DictoryService dictoryService;

	protected DietaryCountService dietaryCountService;

	protected SysTreeService sysTreeService;

	public DietaryCountController() {

	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		DietaryCountQuery query = new DietaryCountQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);
		query.setSemester(SysConfig.getSemester());

		String dateString = request.getParameter("dateString");
		if (StringUtils.isNotEmpty(dateString)) {
			query.fullDay(Integer.parseInt(StringTools.replace(dateString, "-", "")));
		}

		if (!loginContext.isSystemAdministrator()) {
			query.tenantId(loginContext.getTenantId());
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
		int total = dietaryCountService.getDietaryCountCountByQueryCriteria(query);
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

			List<DietaryCount> list = dietaryCountService.getDietaryCountsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {

				Map<Long, String> nameMap2 = new HashMap<Long, String>();
				List<SysTree> treelist = sysTreeService.getSysTreeList(4401L);
				if (treelist != null && !treelist.isEmpty()) {
					for (SysTree tree : treelist) {
						nameMap2.put(tree.getId(), tree.getName());
					}
				}

				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (DietaryCount dietaryCount : list) {
					JSONObject rowJSON = dietaryCount.toJsonObject();
					rowJSON.put("id", dietaryCount.getId());
					rowJSON.put("startIndex", ++start);
					if (dietaryCount.getNodeId() > 0) {
						rowJSON.put("catName", nameMap2.get(dietaryCount.getNodeId()));
					} else {
						rowJSON.put("catName", "全部");
					}
					int wx = dietaryCount.getDayOfWeek();
					switch (wx) {
					case Calendar.MONDAY:
						rowJSON.put("wname", "星期一");
						break;
					case Calendar.TUESDAY:
						rowJSON.put("wname", "星期二");
						break;
					case Calendar.WEDNESDAY:
						rowJSON.put("wname", "星期三");
						break;
					case Calendar.THURSDAY:
						rowJSON.put("wname", "星期四");
						break;
					case Calendar.FRIDAY:
						rowJSON.put("wname", "星期五");
						break;
					case Calendar.SATURDAY:
						rowJSON.put("wname", "星期六");
						break;
					case Calendar.SUNDAY:
						rowJSON.put("wname", "星期日");
						break;
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

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/dietaryCount/list", modelMap);
	}

	@javax.annotation.Resource
	public void setDictoryService(DictoryService dictoryService) {
		this.dictoryService = dictoryService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.dietaryCountService")
	public void setDietaryCountService(DietaryCountService dietaryCountService) {
		this.dietaryCountService = dietaryCountService;
	}

	@javax.annotation.Resource
	public void setSysTreeService(SysTreeService sysTreeService) {
		this.sysTreeService = sysTreeService;
	}

}
