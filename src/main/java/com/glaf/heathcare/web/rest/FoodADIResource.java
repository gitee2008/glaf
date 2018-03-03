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
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import com.alibaba.fastjson.*;
import com.glaf.base.modules.sys.model.Dictory;
import com.glaf.base.modules.sys.model.SysTree;
import com.glaf.base.modules.sys.service.DictoryService;
import com.glaf.base.modules.sys.service.SysTreeService;
import com.glaf.core.base.DataRequest;
import com.glaf.core.base.DataRequest.SortDescriptor;
import com.glaf.core.util.*;

import com.glaf.heathcare.domain.FoodADI;
import com.glaf.heathcare.query.FoodADIQuery;
import com.glaf.heathcare.service.FoodADIService;
import com.glaf.heathcare.util.*;

/**
 * 
 * Rest响应类
 *
 */

@Controller
@Path("/rs/heathcare/foodADI")
public class FoodADIResource {
	protected static final Log logger = LogFactory.getLog(FoodADIResource.class);

	protected FoodADIService foodADIService;

	protected DictoryService dictoryService;

	protected SysTreeService sysTreeService;

	@POST
	@Path("/data")
	@ResponseBody
	@Produces({ MediaType.APPLICATION_JSON })
	public byte[] data(@Context HttpServletRequest request, @RequestBody DataRequest dataRequest) throws IOException {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		FoodADIQuery query = new FoodADIQuery();
		Tools.populate(query, params);
		query.setDataRequest(dataRequest);
		FoodADIDomainFactory.processDataRequest(dataRequest);
		
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
		int total = foodADIService.getFoodADICountByQueryCriteria(query);
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
				logger.debug("orderName:" + orderName);
				logger.debug("order:" + order);
			}

			if (StringUtils.isNotEmpty(orderName)) {
				query.setSortColumn(orderName);
				if (StringUtils.equals(order, "desc")) {
					query.setSortOrder(" desc ");
				}
			}

			List<FoodADI> list = foodADIService.getFoodADIsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {

				Map<Long, String> nameMap = new HashMap<Long, String>();
				SysTree root = sysTreeService.getSysTreeByCode("FoodCategory");
				if (root != null) {
					List<SysTree> foodCategories = sysTreeService.getSysTreeList(root.getId());
					if (foodCategories != null && !foodCategories.isEmpty()) {
						for (SysTree tree : foodCategories) {
							nameMap.put(tree.getId(), tree.getName());
						}
					}
				}

				Map<Long, String> nameMap2 = new HashMap<Long, String>();
				List<Dictory> dictoryList = dictoryService.getDictoryList(4501L);// 4501是餐点分类编号
				if (dictoryList != null && !dictoryList.isEmpty()) {
					for (Dictory d : dictoryList) {
						nameMap2.put(d.getId(), d.getName());
					}
				}

				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (FoodADI foodADI : list) {
					JSONObject rowJSON = foodADI.toJsonObject();
					rowJSON.put("typeName", nameMap2.get(foodADI.getTypeId()));
					rowJSON.put("categoryName", nameMap.get(foodADI.getNodeId()));
					rowJSON.put("foodADIId", foodADI.getId());
					rowJSON.put("id", foodADI.getId());
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

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.foodADIService")
	public void setFoodADIService(FoodADIService foodADIService) {
		this.foodADIService = foodADIService;
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
