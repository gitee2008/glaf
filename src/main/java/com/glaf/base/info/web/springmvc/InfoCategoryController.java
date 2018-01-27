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

package com.glaf.base.info.web.springmvc;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.core.base.BaseTree;
import com.glaf.core.base.TreeModel;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.LoginContext;
import com.glaf.core.tree.helper.TreeHelper;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;

import com.glaf.base.info.model.InfoCategory;
import com.glaf.base.info.query.InfoCategoryQuery;
import com.glaf.base.info.service.InfoCategoryService;

@Controller("/base/category")
@RequestMapping("/base/category")
public class InfoCategoryController {
	protected static final Log logger = LogFactory.getLog(InfoCategoryController.class);

	protected InfoCategoryService infoCategoryService;

	public InfoCategoryController() {

	}

	@ResponseBody
	@RequestMapping("/delete")
	public void delete(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Long id = RequestUtils.getLong(request, "id");
		String ids = request.getParameter("ids");
		if (StringUtils.isNotEmpty(ids)) {
			StringTokenizer token = new StringTokenizer(ids, ",");
			while (token.hasMoreTokens()) {
				String x = token.nextToken();
				if (StringUtils.isNotEmpty(x)) {
					InfoCategory infoCategory = infoCategoryService.getInfoCategory(Long.valueOf(x));
					if (infoCategory != null
							&& (StringUtils.equals(infoCategory.getCreateBy(), loginContext.getActorId())
									|| loginContext.isSystemAdministrator())) {
						infoCategoryService.deleteById(infoCategory.getId());
					}
				}
			}
		} else if (id != null) {
			InfoCategory infoCategory = infoCategoryService.getInfoCategory(Long.valueOf(id));
			if (infoCategory != null && (StringUtils.equals(infoCategory.getCreateBy(), loginContext.getActorId())
					|| loginContext.isSystemAdministrator())) {
				infoCategoryService.deleteById(infoCategory.getId());
			}
		}
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		RequestUtils.setRequestParameterToAttribute(request);
		request.removeAttribute("canSubmit");
		String type = request.getParameter("type");
		if (StringUtils.isEmpty(type)) {
			type = "category";
		}

		InfoCategory infoCategory = infoCategoryService.getInfoCategory(RequestUtils.getLong(request, "id"));
		if (infoCategory != null && (StringUtils.equals(infoCategory.getCreateBy(), loginContext.getActorId())
				|| loginContext.isSystemAdministrator())) {
			request.setAttribute("infoCategory", infoCategory);
		}

		String tenantId = loginContext.getTenantId();
		List<InfoCategory> categories = infoCategoryService.getCategoryList(tenantId, type);
		if (infoCategory != null) {
			List<InfoCategory> subCategories = infoCategoryService.getCategoryList(infoCategory.getId());
			if (categories != null && !categories.isEmpty()) {
				if (subCategories != null && !subCategories.isEmpty()) {
					categories.removeAll(subCategories);
				}
				categories.remove(infoCategory);
				for (InfoCategory cat : categories) {
					if (StringUtils.isNotEmpty(cat.getTreeId())) {
						StringTokenizer token = new StringTokenizer(cat.getTreeId(), "|");
						cat.setDeep(token.countTokens());
					}
				}
			}
		}

		request.setAttribute("categories", categories);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("infoCategory.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/base/category/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		InfoCategoryQuery query = new InfoCategoryQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);
		String actorId = loginContext.getActorId();
		query.createBy(actorId);
		query.setTenantId(loginContext.getTenantId());

		Long parentId = RequestUtils.getLong(request, "parentId", 0);
		query.parentId(parentId);

		String gridType = ParamUtils.getString(params, "gridType");
		if (gridType == null) {
			gridType = "easyui";
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
		int total = infoCategoryService.getInfoCategoryCountByQueryCriteria(query);
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

			List<InfoCategory> list = infoCategoryService.getInfoCategorysByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (InfoCategory infoCategory : list) {
					JSONObject rowJSON = infoCategory.toJsonObject();
					rowJSON.put("id", infoCategory.getId());
					rowJSON.put("pId", infoCategory.getParentId());
					rowJSON.put("categoryId", infoCategory.getId());
					rowJSON.put("infoCategoryId", infoCategory.getId());
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

		return new ModelAndView("/base/category/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("infoCategory.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/base/category/query", modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveInfoCategory")
	public byte[] saveInfoCategory(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		InfoCategory infoCategory = new InfoCategory();
		try {
			Tools.populate(infoCategory, params);

			infoCategory.setParentId(RequestUtils.getLong(request, "parentId"));
			infoCategory.setSort(RequestUtils.getInt(request, "sort"));
			infoCategory.setIcon(request.getParameter("icon"));
			infoCategory.setIconCls(request.getParameter("iconCls"));
			infoCategory.setCoverIcon(request.getParameter("coverIcon"));
			infoCategory.setIndexShow(RequestUtils.getInt(request, "indexShow"));
			infoCategory.setLocked(RequestUtils.getInt(request, "locked"));
			infoCategory.setName(request.getParameter("name"));
			infoCategory.setCode(request.getParameter("code"));
			infoCategory.setDesc(request.getParameter("desc"));
			infoCategory.setEventType(request.getParameter("eventType"));
			infoCategory.setUrl(request.getParameter("url"));
			infoCategory.setCreateBy(actorId);
			infoCategory.setLastUpdateBy(actorId);
			infoCategory.setTenantId(loginContext.getTenantId());

			this.infoCategoryService.save(infoCategory);

			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource
	public void setInfoCategoryService(InfoCategoryService infoCategoryService) {
		this.infoCategoryService = infoCategoryService;
	}

	@ResponseBody
	@RequestMapping("/treeJson")
	public byte[] treeJson(HttpServletRequest request) throws IOException {
		JSONArray array = new JSONArray();
		String type = request.getParameter("type");
		Long parentId = RequestUtils.getLong(request, "parentId", 0);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String tenantId = loginContext.getTenantId();
		List<InfoCategory> categories = null;
		if (parentId != null && parentId > 0) {
			categories = infoCategoryService.getCategoryList(tenantId, parentId);
		} else if (StringUtils.isNotEmpty(type)) {
			categories = infoCategoryService.getCategoryList(tenantId, type);
		}

		if (categories != null && !categories.isEmpty()) {
			Map<Long, TreeModel> treeMap = new java.util.concurrent.ConcurrentHashMap<Long, TreeModel>();
			List<TreeModel> treeModels = new java.util.concurrent.CopyOnWriteArrayList<TreeModel>();
			List<Long> categoryIds = new java.util.concurrent.CopyOnWriteArrayList<Long>();
			for (InfoCategory category : categories) {
				TreeModel tree = new BaseTree();
				tree.setId(category.getId());
				tree.setParentId(category.getParentId());
				tree.setCode(category.getCode());
				tree.setName(category.getName());
				tree.setSortNo(category.getSort());
				tree.setDescription(category.getDesc());
				tree.setCreateBy(category.getCreateBy());
				tree.setIconCls("tree_folder");
				tree.setTreeId(category.getTreeId());
				tree.setUrl(category.getUrl());
				treeModels.add(tree);
				categoryIds.add(category.getId());
				treeMap.put(category.getId(), tree);
			}
			logger.debug("treeModels:" + treeModels.size());
			TreeHelper treeHelper = new TreeHelper();
			JSONArray jsonArray = treeHelper.getTreeJSONArray(treeModels);
			logger.debug(jsonArray.toJSONString());
			return jsonArray.toJSONString().getBytes("UTF-8");
		}
		return array.toJSONString().getBytes("UTF-8");
	}

	@RequestMapping("/update/{id}")
	public ModelAndView update(@PathVariable("id") Long id, HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		params.remove("status");
		params.remove("wfStatus");

		InfoCategory infoCategory = infoCategoryService.getInfoCategory(id);
		if (infoCategory != null && (StringUtils.equals(infoCategory.getCreateBy(), loginContext.getActorId())
				|| loginContext.isSystemAdministrator())) {
			infoCategory.setParentId(RequestUtils.getLong(request, "parentId"));
			infoCategory.setSort(RequestUtils.getInt(request, "sort"));
			infoCategory.setIcon(request.getParameter("icon"));
			infoCategory.setIconCls(request.getParameter("iconCls"));
			infoCategory.setCoverIcon(request.getParameter("coverIcon"));
			infoCategory.setIndexShow(RequestUtils.getInt(request, "indexShow"));
			infoCategory.setLocked(RequestUtils.getInt(request, "locked"));
			infoCategory.setName(request.getParameter("name"));
			infoCategory.setCode(request.getParameter("code"));
			infoCategory.setDesc(request.getParameter("desc"));
			infoCategory.setEventType(request.getParameter("eventType"));
			infoCategory.setUrl(request.getParameter("url"));
			infoCategory.setLastUpdateBy(loginContext.getActorId());
			infoCategoryService.save(infoCategory);
		}

		return this.list(request, modelMap);
	}

	@RequestMapping("/view/{id}")
	public ModelAndView view(@PathVariable("id") Long id, HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		InfoCategory infoCategory = infoCategoryService.getInfoCategory(id);
		request.setAttribute("infoCategory", infoCategory);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view);
		}

		String x_view = ViewProperties.getString("infoCategory.view");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view);
		}

		return new ModelAndView("/base/category/view");
	}

}
