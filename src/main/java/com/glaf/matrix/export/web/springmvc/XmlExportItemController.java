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

package com.glaf.matrix.export.web.springmvc;

import java.io.IOException;
import java.util.ArrayList;
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
import com.glaf.matrix.export.domain.XmlExportItem;
import com.glaf.matrix.export.query.XmlExportItemQuery;
import com.glaf.matrix.export.service.XmlExportItemService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/matrix/xmlExportItem")
@RequestMapping("/matrix/xmlExportItem")
public class XmlExportItemController {
	protected static final Log logger = LogFactory.getLog(XmlExportItemController.class);

	protected XmlExportItemService xmlExportItemService;

	public XmlExportItemController() {

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
					XmlExportItem xmlExportItem = xmlExportItemService.getXmlExportItem(x);
					if (xmlExportItem != null
							&& (StringUtils.equals(xmlExportItem.getCreateBy(), loginContext.getActorId())
									|| loginContext.isSystemAdministrator())) {
						xmlExportItemService.deleteById(xmlExportItem.getId());
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			XmlExportItem xmlExportItem = xmlExportItemService.getXmlExportItem(id);
			if (xmlExportItem != null && (StringUtils.equals(xmlExportItem.getCreateBy(), loginContext.getActorId())
					|| loginContext.isSystemAdministrator())) {
				xmlExportItemService.deleteById(xmlExportItem.getId());
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		XmlExportItem xmlExportItem = xmlExportItemService.getXmlExportItem(RequestUtils.getString(request, "id"));
		if (xmlExportItem != null) {
			request.setAttribute("xmlExportItem", xmlExportItem);
		}

		List<Integer> sortNoList = new ArrayList<Integer>();
		for (int i = 1; i < 50; i++) {
			sortNoList.add(i);
		}
		request.setAttribute("sortNoList", sortNoList);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("xmlExportItem.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/matrix/xmlExportItem/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		XmlExportItemQuery query = new XmlExportItemQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		String expId = RequestUtils.getString(request, "expId");
		query.expId(expId);

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
		int total = xmlExportItemService.getXmlExportItemCountByQueryCriteria(query);
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

			List<XmlExportItem> list = xmlExportItemService.getXmlExportItemsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (XmlExportItem xmlExportItem : list) {
					JSONObject rowJSON = xmlExportItem.toJsonObject();
					rowJSON.put("id", xmlExportItem.getId());
					rowJSON.put("rowId", xmlExportItem.getId());
					rowJSON.put("xmlExportItemId", xmlExportItem.getId());
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

		return new ModelAndView("/matrix/xmlExportItem/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("xmlExportItem.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/matrix/xmlExportItem/query", modelMap);
	}

	@ResponseBody
	@RequestMapping("/save")
	public byte[] save(HttpServletRequest request) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		XmlExportItem xmlExportItem = new XmlExportItem();
		try {
			Tools.populate(xmlExportItem, params);
			xmlExportItem.setExpId(RequestUtils.getString(request, "expId"));
			xmlExportItem.setName(request.getParameter("name"));
			xmlExportItem.setTitle(request.getParameter("title"));
			xmlExportItem.setExpression(request.getParameter("expression"));
			xmlExportItem.setDefaultValue(request.getParameter("defaultValue"));
			xmlExportItem.setDataType(request.getParameter("dataType"));
			xmlExportItem.setRequired(request.getParameter("required"));
			xmlExportItem.setTagFlag(request.getParameter("tagFlag"));
			xmlExportItem.setSortNo(RequestUtils.getInt(request, "sortNo"));
			xmlExportItem.setLocked(RequestUtils.getInt(request, "locked"));
			xmlExportItem.setCreateBy(actorId);
			this.xmlExportItemService.save(xmlExportItem);

			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource(name = "com.glaf.matrix.export.service.xmlExportItemService")
	public void setXmlExportItemService(XmlExportItemService xmlExportItemService) {
		this.xmlExportItemService = xmlExportItemService;
	}

}
