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

package com.glaf.matrix.category.web.springmvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.node.ArrayNode;

import com.glaf.matrix.category.domain.Category;
import com.glaf.matrix.category.domain.CategoryOwner;
import com.glaf.matrix.category.query.CategoryQuery;
import com.glaf.matrix.category.service.CategoryService;

import com.glaf.core.base.BaseTree;
import com.glaf.core.base.ColumnModel;
import com.glaf.core.base.TableModel;
import com.glaf.core.base.TreeModel;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.factory.DataServiceFactory;
import com.glaf.core.identity.User;
import com.glaf.core.query.TreeModelQuery;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.tree.helper.JacksonTreeHelper;
import com.glaf.core.tree.helper.TreeHelper;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/matrix/my/category")
@RequestMapping("/matrix/my/category")
public class MyCategoryController {
	protected static final Log logger = LogFactory.getLog(MyCategoryController.class);

	protected IDatabaseService databaseService;

	protected CategoryService categoryService;

	public MyCategoryController() {

	}

	@RequestMapping("categoryUsersJson")
	@ResponseBody
	public byte[] categoryUsersJson(HttpServletRequest request) throws IOException {
		// LoginContext loginContext = RequestUtils.getLoginContext(request);
		JSONObject result = new JSONObject();
		long categoryId = RequestUtils.getLong(request, "categoryId");
		String xtype = request.getParameter("xtype");
		String utype = request.getParameter("utype");
		Collection<String> userIds = new HashSet<String>();
		Category category = null;
		if (categoryId > 0) {
			category = categoryService.getCategory(categoryId);
			if (StringUtils.equals(xtype, "owners")) {
				if (category != null && category.getOwners() != null) {
					for (CategoryOwner owner : category.getOwners()) {
						userIds.add(owner.getActorId());
					}
				}
			} else {
				if (category != null && category.getActorIds() != null) {
					for (String actorId : category.getActorIds()) {
						userIds.add(actorId);
					}
				}
			}
		}

		TreeModelQuery query = new TreeModelQuery();
		List<TreeModel> manageTreeModels = IdentityFactory.getOrganizations(query);

		logger.debug("manageTreeModels:" + manageTreeModels);

		Map<Long, TreeModel> myTreeMap = new HashMap<Long, TreeModel>();
		if (manageTreeModels != null && !manageTreeModels.isEmpty()) {
			for (TreeModel t : manageTreeModels) {
				myTreeMap.put(t.getId(), t);
			}
		}

		List<User> users = IdentityFactory.getUsers();

		if (users != null && !users.isEmpty()) {
			logger.debug("users size:" + users.size());
			List<TreeModel> treeModels = new ArrayList<TreeModel>();

			Map<Long, TreeModel> organizationMap = IdentityFactory.getOrganizationMap();

			if (manageTreeModels != null && !manageTreeModels.isEmpty()) {
				logger.debug("organization tree size:" + manageTreeModels.size());
				Map<Long, TreeModel> treeMap = new HashMap<Long, TreeModel>();
				for (TreeModel tree : manageTreeModels) {
					if (myTreeMap.get(tree.getId()) == null) {
						continue;
					}
					TreeModel organization = organizationMap.get(tree.getId());
					treeMap.put(organization.getId(), tree);
				}

				for (TreeModel tree : manageTreeModels) {
					if (myTreeMap.get(tree.getId()) == null) {
						continue;
					}
					treeModels.add(tree);
					TreeModel organization = organizationMap.get(tree.getId());
					if (organization != null && organization.getId() > 0) {
						for (User user : users) {
							if (user.isSystemAdministrator()) {
								continue;
							}
							TreeModel t = treeMap.get(user.getOrganizationId());
							if (organization.getId() == user.getOrganizationId() && t != null) {
								TreeModel treeModel = new BaseTree();
								treeModel.setParentId(t.getId());
								treeModel.setId(100000000 + user.getActorId().hashCode());
								treeModel.setCode(user.getActorId());
								treeModel.setName(user.getActorId() + " " + user.getName());
								treeModel.setIconCls("icon-user");
								treeModel.setIcon(request.getContextPath() + "/images/user.png");
								if (userIds != null && userIds.contains(user.getActorId())) {
									treeModel.setChecked(true);
								}
								if (!treeModels.contains(treeModel)) {
									treeModels.add(treeModel);
								}
							}
						}
					}
				}
			}

			if (StringUtils.equals(utype, "ALL")) {
				if (users != null && !users.isEmpty()) {
					for (User user : users) {
						if (user.isSystemAdministrator()) {
							continue;
						}
						if (user.getOrganizationId() == 0) {
							TreeModel treeModel = new BaseTree();
							treeModel.setParentId(-1);
							treeModel.setId(100000000 + user.getActorId().hashCode());
							treeModel.setCode(user.getActorId());
							treeModel.setName(user.getActorId() + " " + user.getName());
							treeModel.setIconCls("icon-user");
							treeModel.setIcon(request.getContextPath() + "/images/user.png");
							if (userIds != null && userIds.contains(user.getActorId())) {
								treeModel.setChecked(true);
							}
							if (!treeModels.contains(treeModel)) {
								treeModels.add(treeModel);
							}
						}
					}
				}
			}
			// logger.debug("treeModels:" + treeModels.size());
			TreeHelper treeHelper = new TreeHelper();
			JSONArray jsonArray = treeHelper.getTreeJSONArray(treeModels);
			// logger.debug(jsonArray.toJSONString());
			return jsonArray.toJSONString().getBytes("UTF-8");
		}
		return result.toString().getBytes("UTF-8");
	}

	@RequestMapping("/chooseSubs")
	public ModelAndView chooseSubs(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		Category category = categoryService.getCategory(RequestUtils.getLong(request, "categoryId"));
		if (category != null) {
			request.setAttribute("category", category);
			if (category.getSubordinateIds() != null && !category.getSubordinateIds().isEmpty()) {
				CategoryQuery query = new CategoryQuery();
				query.setCategoryIds(category.getSubordinateIds());

				List<Category> categories = categoryService.list(query);

				if (categories != null && !categories.isEmpty()) {
					request.setAttribute("trees", categories);
				}
			}
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("matrix_mycategory.chooseSubs");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/matrix/mycategory/choose_subs", modelMap);
	}

	@ResponseBody
	@RequestMapping("/deleteById")
	public byte[] deleteById(HttpServletRequest request) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Category category = categoryService.getCategory(RequestUtils.getLong(request, "id"));
		if (category != null) {
			if (loginContext.isSystemAdministrator()
					|| StringUtils.equals(loginContext.getActorId(), category.getCreateBy())) {
				CategoryQuery query = new CategoryQuery();
				query.parentId(category.getId());
				int count = categoryService.getCategoryCountByQueryCriteria(query);
				if (count > 0) {
					return ResponseUtils.responseJsonResult(false, "不能删除存在子节点的分类。");
				}
				categoryService.deleteById(category.getId());
				return ResponseUtils.responseJsonResult(true);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		RequestUtils.setRequestParameterToAttribute(request);

		Category category = categoryService.getCategory(RequestUtils.getLong(request, "id"));
		if (category != null) {
			request.setAttribute("category", category);
			request.setAttribute("parentId", category.getParentId());
		}

		long parentId = RequestUtils.getLong(request, "parentId");
		Category parent = null;
		if (parentId > 0) {
			parent = categoryService.getCategory(parentId);
		}
		CategoryQuery query = new CategoryQuery();
		if (parent != null) {
			query.treeIdLike(parent.getTreeId() + "%");
		}

		List<Category> categories = null;
		if (loginContext.isSystemAdministrator()) {
			query.setOrderBy(" E.TREEID_ asc ");
			categories = categoryService.list(query);
		} else {
			categories = categoryService.getCategories(loginContext.getActorId());
		}

		if (categories != null && !categories.isEmpty()) {
			request.setAttribute("categories", categories);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("matrix_mycategory.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/matrix/mycategory/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		CategoryQuery query = new CategoryQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		long parentId = RequestUtils.getLong(request, "parentId");
		Category parent = null;
		if (parentId > 0) {
			parent = categoryService.getCategory(parentId);
		}
		if (parent != null) {
			query.treeIdLike(parent.getTreeId() + "%");
		}
		if (!loginContext.isSystemAdministrator()) {
			List<Long> categoryIds = new ArrayList<Long>();
			categoryIds.add(-1L);
			List<Category> list = categoryService.getCategories(loginContext.getActorId());
			if (list != null && !list.isEmpty()) {
				for (Category p : list) {
					categoryIds.add(p.getId());
				}
			}
			query.setCategoryIds(categoryIds);
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
		int total = categoryService.getCategoryCountByQueryCriteria(query);
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

			List<Category> list = categoryService.getCategoriesByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (Category category : list) {
					JSONObject rowJSON = category.toJsonObject();
					rowJSON.put("id", category.getId());
					rowJSON.put("categoryId", category.getId());
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

		return new ModelAndView("/matrix/mycategory/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("matrix_mycategory.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/matrix/mycategory/query", modelMap);
	}

	@RequestMapping("/save")
	public ModelAndView save(HttpServletRequest request, ModelMap modelMap) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);

		Category category = new Category();
		Tools.populate(category, params);

		category.setName(request.getParameter("name"));
		category.setCode(request.getParameter("code"));
		category.setTitle(request.getParameter("title"));
		category.setDiscriminator(request.getParameter("discriminator"));
		category.setIcon(request.getParameter("icon"));
		category.setIconCls(request.getParameter("iconCls"));
		category.setType(request.getParameter("type"));
		// category.setSort(RequestUtils.getInt(request, "sort"));
		category.setLocked(RequestUtils.getInt(request, "locked"));
		category.setCreateBy(actorId);

		if (StringUtils.isNotBlank(request.getParameter("owners"))) {
			StringTokenizer token = new StringTokenizer(request.getParameter("owners"), ",");
			while (token.hasMoreTokens()) {
				String str = token.nextToken();
				if (StringUtils.isNotEmpty(str)) {
					CategoryOwner owner = new CategoryOwner();
					owner.setActorId(str);
					category.addOwner(owner);
				}
			}
		}

		if (StringUtils.isNotBlank(request.getParameter("accessors"))) {
			StringTokenizer token2 = new StringTokenizer(request.getParameter("accessors"), ",");
			while (token2.hasMoreTokens()) {
				String str = token2.nextToken();
				if (StringUtils.isNotEmpty(str)) {
					category.addAccessor(str);
				}
			}
		}

		if (StringUtils.isNotBlank(request.getParameter("subordinateIds"))) {
			StringTokenizer token3 = new StringTokenizer(request.getParameter("subordinateIds"), ",");
			while (token3.hasMoreTokens()) {
				String str = token3.nextToken();
				if (StringUtils.isNotEmpty(str)) {
					category.addSubordinate(Long.parseLong(str));
				}
			}
			category.setSubIds(request.getParameter("subordinateIds"));
		}

		categoryService.save(category);

		return this.list(request, modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveAccessor")
	public byte[] saveAccessor(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		long categoryId = RequestUtils.getLong(request, "categoryId");
		String actorId = request.getParameter("actorId");
		String operation = request.getParameter("operation");
		if (categoryId > 0 && actorId != null) {
			Category category = categoryService.getCategory(categoryId);
			if (category != null) {
				if (loginContext.isSystemAdministrator()
						|| StringUtils.equals(loginContext.getActorId(), category.getCreateBy())) {
					if (StringUtils.equals(operation, "revoke")) {
						categoryService.deleteAccessor(categoryId, actorId);
					} else {
						categoryService.createAccessor(categoryId, actorId);
					}
					return ResponseUtils.responseResult(true);
				}
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@ResponseBody
	@RequestMapping("/saveCategory")
	public byte[] saveCategory(HttpServletRequest request) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug("params:" + params);
		Category category = new Category();
		try {
			Tools.populate(category, params);
			category.setName(request.getParameter("name"));
			category.setCode(request.getParameter("code"));
			category.setTitle(request.getParameter("title"));
			category.setDiscriminator(request.getParameter("discriminator"));
			category.setIcon(request.getParameter("icon"));
			category.setIconCls(request.getParameter("iconCls"));
			category.setType(request.getParameter("type"));
			// category.setSort(RequestUtils.getInt(request, "sort"));
			category.setLocked(RequestUtils.getInt(request, "locked"));
			category.setCreateBy(actorId);
			category.setUpdateBy(actorId);

			if (StringUtils.isNotBlank(request.getParameter("owners"))) {
				StringTokenizer token = new StringTokenizer(request.getParameter("owners"), ",");
				while (token.hasMoreTokens()) {
					String str = token.nextToken();
					if (StringUtils.isNotEmpty(str)) {
						CategoryOwner owner = new CategoryOwner();
						owner.setActorId(str);
						category.addOwner(owner);
					}
				}
			}

			if (StringUtils.isNotBlank(request.getParameter("accessors"))) {
				StringTokenizer token2 = new StringTokenizer(request.getParameter("accessors"), ",");
				while (token2.hasMoreTokens()) {
					String str = token2.nextToken();
					if (StringUtils.isNotEmpty(str)) {
						category.addAccessor(str);
					}
				}
			}

			if (StringUtils.isNotBlank(request.getParameter("subordinateIds"))) {
				StringTokenizer token3 = new StringTokenizer(request.getParameter("subordinateIds"), ",");
				while (token3.hasMoreTokens()) {
					String str = token3.nextToken();
					if (StringUtils.isNotEmpty(str)) {
						category.addSubordinate(Long.parseLong(str));
					}
				}
				category.setSubIds(request.getParameter("subordinateIds"));
			}

			this.categoryService.save(category);

			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	public @ResponseBody Category saveOrUpdate(HttpServletRequest request, @RequestBody Map<String, Object> model) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		Category category = new Category();
		try {
			Tools.populate(category, model);
			category.setName(ParamUtils.getString(model, "name"));
			category.setCode(ParamUtils.getString(model, "code"));
			category.setTitle(ParamUtils.getString(model, "title"));
			category.setDiscriminator(request.getParameter("discriminator"));
			category.setIcon(request.getParameter("icon"));
			category.setIconCls(request.getParameter("iconCls"));
			category.setType(request.getParameter("type"));
			// category.setSort(RequestUtils.getInt(request, "sort"));
			category.setLocked(RequestUtils.getInt(request, "locked"));
			category.setCreateBy(actorId);
			category.setUpdateBy(actorId);

			if (StringUtils.isNotBlank(request.getParameter("owners"))) {
				StringTokenizer token = new StringTokenizer(request.getParameter("owners"), ",");
				while (token.hasMoreTokens()) {
					String str = token.nextToken();
					if (StringUtils.isNotEmpty(str)) {
						CategoryOwner owner = new CategoryOwner();
						owner.setActorId(str);
						category.addOwner(owner);
					}
				}
			}

			if (StringUtils.isNotBlank(request.getParameter("accessors"))) {
				StringTokenizer token2 = new StringTokenizer(request.getParameter("accessors"), ",");
				while (token2.hasMoreTokens()) {
					String str = token2.nextToken();
					if (StringUtils.isNotEmpty(str)) {
						category.addAccessor(str);
					}
				}
			}

			if (StringUtils.isNotBlank(request.getParameter("subordinateIds"))) {
				StringTokenizer token3 = new StringTokenizer(request.getParameter("subordinateIds"), ",");
				while (token3.hasMoreTokens()) {
					String str = token3.nextToken();
					if (StringUtils.isNotEmpty(str)) {
						category.addSubordinate(Long.parseLong(str));
					}
				}
				category.setSubIds(request.getParameter("subordinateIds"));
			}

			this.categoryService.save(category);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return category;
	}

	/**
	 * 提交增加信息
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/saveSort")
	@ResponseBody
	public byte[] saveSort(HttpServletRequest request) {
		String items = request.getParameter("items");
		if (StringUtils.isNotEmpty(items)) {
			int sort = 0;
			List<TableModel> rows = new ArrayList<TableModel>();
			StringTokenizer token = new StringTokenizer(items, ",");
			while (token.hasMoreTokens()) {
				String item = token.nextToken();
				if (StringUtils.isNotEmpty(item)) {
					sort++;
					TableModel t1 = new TableModel();
					t1.setTableName("SYS_CATEGORY");
					ColumnModel idColumn1 = new ColumnModel();
					idColumn1.setColumnName("ID_");
					idColumn1.setValue(Long.parseLong(item));
					t1.setIdColumn(idColumn1);
					ColumnModel column = new ColumnModel();
					column.setColumnName("SORTNO_");
					column.setValue(sort);
					t1.addColumn(column);
					rows.add(t1);
				}
			}
			try {
				DataServiceFactory.getInstance().updateAllTableData(rows);
				return ResponseUtils.responseResult(true);
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@javax.annotation.Resource(name = "com.glaf.matrix.category.service.categoryService")
	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@javax.annotation.Resource
	public void setDatabaseService(IDatabaseService databaseService) {
		this.databaseService = databaseService;
	}

	/**
	 * 显示排序页面
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/showSort")
	public ModelAndView showSort(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		long parentId = RequestUtils.getLongParameter(request, "parentId", 0);

		CategoryQuery query = new CategoryQuery();
		query.parentId(parentId);

		if (!loginContext.isSystemAdministrator()) {
			List<Long> categoryIds = new ArrayList<Long>();
			categoryIds.add(-1L);
			List<Category> list = categoryService.getCategories(loginContext.getActorId());
			if (list != null && !list.isEmpty()) {
				for (Category p : list) {
					categoryIds.add(p.getId());
				}
			}
			query.setCategoryIds(categoryIds);
		}

		List<Category> categories = categoryService.list(query);
		request.setAttribute("categories", categories);

		String x_view = ViewProperties.getString("matrix_category.showSort");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/matrix/mycategory/showSort", modelMap);
	}

	@ResponseBody
	@RequestMapping("/treeJson")
	public byte[] treeJson(HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.debug("------------------------treeJson--------------------");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		RequestUtils.setRequestParameterToAttribute(request);

		List<Long> selected = new ArrayList<Long>();
		CategoryQuery query = new CategoryQuery();
		Category category = categoryService.getCategory(RequestUtils.getLong(request, "categoryId"));
		if (category != null) {
			request.setAttribute("category", category);
			if (category.getSubordinateIds() != null && !category.getSubordinateIds().isEmpty()) {
				query.setCategoryIds(category.getSubordinateIds());
				List<Category> categories = categoryService.list(query);
				if (categories != null && !categories.isEmpty()) {
					for (Category p : categories) {
						selected.add(p.getId());
					}
				}
			}
		}

		query = new CategoryQuery();
		List<Category> categories = null;
		if (loginContext.isSystemAdministrator()) {
			query.setOrderBy(" E.TREEID_ asc ");
			categories = categoryService.list(query);
		} else {
			categories = categoryService.getCategories(loginContext.getActorId());
		}

		List<TreeModel> treeModels = new ArrayList<TreeModel>();
		if (categories != null && !categories.isEmpty()) {
			for (Category p : categories) {
				TreeModel tree = new BaseTree();
				tree.setId(p.getId());
				tree.setParentId(p.getParentId());
				tree.setName(p.getName());
				tree.setTreeId(p.getTreeId());
				tree.setIcon(p.getIcon());
				tree.setIconCls(p.getIcon());
				tree.setDiscriminator(p.getDiscriminator());
				tree.setCode(p.getCode());
				tree.setSortNo(p.getSort());
				tree.setLevel(p.getLevel());
				Map<String, Object> dataMap = new HashMap<String, Object>();
				if (selected.contains(p.getId())) {
					tree.setChecked(true);
					dataMap.put("checked", true);
				} else {
					dataMap.put("checked", false);
				}
				tree.setDataMap(dataMap);

				treeModels.add(tree);
			}
		}

		JacksonTreeHelper treeHelper = new JacksonTreeHelper();
		ArrayNode responseJSON = treeHelper.getTreeArrayNode(treeModels);
		try {
			return responseJSON.toString().getBytes("UTF-8");
		} catch (IOException e) {
			return responseJSON.toString().getBytes();
		}

	}

	@RequestMapping("/update")
	public ModelAndView update(HttpServletRequest request, ModelMap modelMap) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);

		Category category = categoryService.getCategory(RequestUtils.getLong(request, "id"));

		Tools.populate(category, params);

		category.setName(request.getParameter("name"));
		category.setCode(request.getParameter("code"));
		category.setTitle(request.getParameter("title"));
		category.setDiscriminator(request.getParameter("discriminator"));
		category.setIcon(request.getParameter("icon"));
		category.setIconCls(request.getParameter("iconCls"));
		category.setType(request.getParameter("type"));
		// category.setSort(RequestUtils.getInt(request, "sort"));
		category.setLocked(RequestUtils.getInt(request, "locked"));
		category.setUpdateBy(actorId);

		if (StringUtils.isNotBlank(request.getParameter("owners"))) {
			StringTokenizer token = new StringTokenizer(request.getParameter("owners"), ",");
			while (token.hasMoreTokens()) {
				String str = token.nextToken();
				if (StringUtils.isNotEmpty(str)) {
					CategoryOwner owner = new CategoryOwner();
					owner.setActorId(str);
					category.addOwner(owner);
				}
			}
		}

		if (StringUtils.isNotBlank(request.getParameter("accessors"))) {
			StringTokenizer token2 = new StringTokenizer(request.getParameter("accessors"), ",");
			while (token2.hasMoreTokens()) {
				String str = token2.nextToken();
				if (StringUtils.isNotEmpty(str)) {
					category.addAccessor(str);
				}
			}
		}

		if (StringUtils.isNotBlank(request.getParameter("subordinateIds"))) {
			StringTokenizer token3 = new StringTokenizer(request.getParameter("subordinateIds"), ",");
			while (token3.hasMoreTokens()) {
				String str = token3.nextToken();
				if (StringUtils.isNotEmpty(str)) {
					category.addSubordinate(Long.parseLong(str));
				}
			}
			category.setSubIds(request.getParameter("subordinateIds"));
		}

		categoryService.save(category);

		return this.list(request, modelMap);
	}

	@RequestMapping("/view")
	public ModelAndView view(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		Category category = categoryService.getCategory(RequestUtils.getLong(request, "id"));
		request.setAttribute("category", category);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view);
		}

		String x_view = ViewProperties.getString("matrix_mycategory.view");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view);
		}

		return new ModelAndView("/matrix/mycategory/view");
	}
}
