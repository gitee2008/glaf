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

package com.glaf.heathcare.web.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.base.modules.sys.model.Dictory;
import com.glaf.base.modules.sys.service.DictoryService;
import com.glaf.base.modules.sys.service.SysTreeService;
import com.glaf.core.base.DataRequest;
import com.glaf.core.base.DataRequest.SortDescriptor;
import com.glaf.core.util.PageResult;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.Tools;

import com.glaf.heathcare.domain.DietaryTemplate;
import com.glaf.heathcare.query.DietaryTemplateQuery;
import com.glaf.heathcare.service.DietaryTemplateService;
import com.glaf.heathcare.util.DietaryTemplateDomainFactory;

/**
 * 
 * Rest响应类
 *
 */

@Controller
@Path("/rs/heathcare/dietaryTemplate")
public class DietaryTemplateResource {
	protected static final Log logger = LogFactory.getLog(DietaryTemplateResource.class);

	protected DietaryTemplateService dietaryTemplateService;

	protected DictoryService dictoryService;

	protected SysTreeService sysTreeService;

	@POST
	@Path("/data")
	@ResponseBody
	@Produces({ MediaType.APPLICATION_JSON })
	public byte[] data(@Context HttpServletRequest request, @RequestBody DataRequest dataRequest) throws IOException {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		DietaryTemplateQuery query = new DietaryTemplateQuery();
		Tools.populate(query, params);
		query.setDataRequest(dataRequest);
		DietaryTemplateDomainFactory.processDataRequest(dataRequest);
		query.instanceFlag("N");

		String sysFlag = request.getParameter("sysFlag");

		if (StringUtils.equals(sysFlag, "Y")) {
			query.sysFlag("Y");
		}

		String wordLike = request.getParameter("wordLike_enc");
		if (StringUtils.isNotEmpty(wordLike)) {
			query.setNameLike(RequestUtils.decodeString(wordLike));
		}

		String nameLike = request.getParameter("nameLike_enc");
		if (StringUtils.isNotEmpty(nameLike)) {
			query.setNameLike(RequestUtils.decodeString(nameLike));
		}

		int start = 0;
		int limit = PageResult.DEFAULT_PAGE_SIZE;

		int pageNo = dataRequest.getPage();
		limit = dataRequest.getPageSize();

		start = (pageNo - 1) * limit;

		if (start < 0) {
			start = 0;
		}

		if (limit <= 0) {
			limit = PageResult.DEFAULT_PAGE_SIZE;
		}

		JSONObject result = new JSONObject();
		int total = dietaryTemplateService.getDietaryTemplateCountByQueryCriteria(query);
		if (total > 0) {
			result.put("total", total);
			result.put("totalCount", total);
			result.put("totalRecords", total);
			result.put("start", start);
			result.put("startIndex", start);
			result.put("limit", limit);
			result.put("pageSize", limit);

			String orderName = null;
			String order = null;

			if (dataRequest.getSort() != null && !dataRequest.getSort().isEmpty()) {
				SortDescriptor sort = dataRequest.getSort().get(0);
				orderName = sort.getField();
				order = sort.getDir();
				//logger.debug("orderName:" + orderName);
				//logger.debug("order:" + order);
			}

			if (StringUtils.isNotEmpty(orderName)) {
				query.setSortColumn(orderName);
				if (StringUtils.equals(order, "desc")) {
					query.setSortOrder(" desc ");
				}
			}

			List<DietaryTemplate> list = dietaryTemplateService.getDietaryTemplatesByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				Map<Long, String> nameMap2 = new HashMap<Long, String>();
				List<Dictory> dictoryList = dictoryService.getDictoryList(4501L);// 4501是餐点分类编号
				if (dictoryList != null && !dictoryList.isEmpty()) {
					for (Dictory d : dictoryList) {
						nameMap2.put(d.getId(), d.getName());
					}
				}

				for (DietaryTemplate dietaryTemplate : list) {
					JSONObject rowJSON = dietaryTemplate.toJsonObject();
					rowJSON.put("id", dietaryTemplate.getId());
					rowJSON.put("typeName", nameMap2.get(dietaryTemplate.getTypeId()));
					rowJSON.put("dietaryTemplateId", dietaryTemplate.getId());
					rowJSON.put("startIndex", ++start);
					switch (dietaryTemplate.getSeason()) {
					case 1:
						rowJSON.put("seasonName", "春季");
						break;
					case 2:
						rowJSON.put("seasonName", "夏季");
						break;
					case 3:
						rowJSON.put("seasonName", "秋季");
						break;
					case 4:
						rowJSON.put("seasonName", "冬季");
						break;
					default:
						rowJSON.put("seasonName", "-");
						break;
					}

					switch (dietaryTemplate.getDayOfWeek()) {
					case 1:
						rowJSON.put("dayOfWeekName", "星期一");
						break;
					case 2:
						rowJSON.put("dayOfWeekName", "星期二");
						break;
					case 3:
						rowJSON.put("dayOfWeekName", "星期三");
						break;
					case 4:
						rowJSON.put("dayOfWeekName", "星期四");
						break;
					case 5:
						rowJSON.put("dayOfWeekName", "星期五");
						break;
					case 6:
						rowJSON.put("dayOfWeekName", "星期六");
						break;
					case 7:
						rowJSON.put("dayOfWeekName", "星期日");
						break;
					default:
						rowJSON.put("dayOfWeekName", "-");
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

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.dietaryTemplateService")
	public void setDietaryTemplateService(DietaryTemplateService dietaryTemplateService) {
		this.dietaryTemplateService = dietaryTemplateService;
	}

	@javax.annotation.Resource
	public void setDictoryService(DictoryService dictoryService) {
		this.dictoryService = dictoryService;
	}

	@javax.annotation.Resource
	public void setSysTreeService(SysTreeService sysTreeService) {
		this.sysTreeService = sysTreeService;
	}

}
