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

package com.glaf.base.modules.sys.web.springmvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.glaf.base.modules.sys.SysConstants;
import com.glaf.base.modules.sys.model.SysApplication;
import com.glaf.base.modules.sys.model.SysRole;
import com.glaf.base.modules.sys.model.SysUser;
import com.glaf.base.modules.sys.query.SysRoleQuery;
import com.glaf.base.modules.sys.service.SysApplicationService;
import com.glaf.base.modules.sys.service.SysRoleService;
import com.glaf.base.modules.sys.service.SysUserService;
import com.glaf.base.utils.ParamUtil;
import com.glaf.core.base.TreeModel;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.tree.helper.TreeHelper;
import com.glaf.core.util.PageResult;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.StringTools;
import com.glaf.core.util.Tools;

@Controller("/sys/role")
@RequestMapping("/sys/role")
public class SysRoleController {
	private static final Log logger = LogFactory.getLog(SysRoleController.class);

	protected SysApplicationService sysApplicationService;

	protected SysRoleService sysRoleService;

	protected SysUserService sysUserService;

	@RequestMapping("/authorityUsers")
	public ModelAndView authorityUsers(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String id = ParamUtil.getParameter(request, "id");
		SysRole sysRole = null;
		if (id != null) {
			sysRole = sysRoleService.findById(id);
			request.setAttribute("sysRole", sysRole);
		}

		if (sysRole == null) {
			id = ParamUtil.getParameter(request, "roleId");
			sysRole = sysRoleService.findById(id);
			request.setAttribute("sysRole", sysRole);
		}

		String x_view = ViewProperties.getString("role.authorityUsers");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/role/authorityUsers", modelMap);
	}

	/**
	 * 批量删除信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/batchDelete")
	@ResponseBody
	public byte[] batchDelete(HttpServletRequest request) {
		String[] ids = ParamUtil.getParameterValues(request, "id");
		String roleIds = RequestUtils.getParameter(request, "roleIds");
		try {
			if (ids != null && ids.length > 0) {
				for (String id : ids) {
					SysRole role = sysRoleService.findById(id);
					if (role != null && !StringUtils.equals(role.getType(), "SYS")) {
						role.setDeleteFlag(1);
						role.setDeleteTime(new Date());
						sysRoleService.update(role);
					}
				}
				return ResponseUtils.responseResult(true);
			} else if (StringUtils.isNotEmpty(roleIds)) {
				List<String> rids = StringTools.split(roleIds);
				for (String id : rids) {
					SysRole role = sysRoleService.findById(id);
					if (role != null && !StringUtils.equals(role.getType(), "SYS")) {
						role.setDeleteFlag(1);
						role.setDeleteTime(new Date());
						sysRoleService.update(role);
					}
				}
				return ResponseUtils.responseResult(true);
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
		return ResponseUtils.responseResult(false);
	}

	/**
	 * 显示修改页面
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String id = ParamUtil.getParameter(request, "id");
		SysRole bean = sysRoleService.findById(id);
		request.setAttribute("bean", bean);
		request.setAttribute("role", bean);

		String x_view = ViewProperties.getString("role.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		// 显示列表页面
		return new ModelAndView("/sys/role/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request) throws IOException {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SysRoleQuery query = new SysRoleQuery();
		Tools.populate(query, params);
		query.setDeleteFlag(0);

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
			limit = PageResult.DEFAULT_PAGE_SIZE;
		}

		JSONObject result = new JSONObject();
		int total = sysRoleService.getSysRoleCountByQueryCriteria(query);
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

			List<SysRole> list = sysRoleService.getSysRolesByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();
				result.put("rows", rowsJSON);
				for (SysRole sysRole : list) {
					JSONObject rowJSON = sysRole.toJsonObject();
					rowJSON.put("id", sysRole.getId());
					rowJSON.put("startIndex", ++start);
					rowsJSON.add(rowJSON);
				}
			}
		} else {
			result.put("total", total);
			result.put("totalCount", total);
			JSONArray rowsJSON = new JSONArray();
			result.put("rows", rowsJSON);
		}
		return result.toString().getBytes("UTF-8");
	}

	@RequestMapping
	public ModelAndView list(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String x_view = ViewProperties.getString("role.list");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/sys/role/list", modelMap);
	}

	/**
	 * 显示菜单页面
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/privilege")
	public ModelAndView privilege(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String roleId = ParamUtil.getParameter(request, "roleId");
		SysRole bean = sysRoleService.findById(roleId);
		request.setAttribute("role", bean);

		// long parentId = ParamUtil.getIntParameter(request, "parentId", 3);

		String x_view = ViewProperties.getString("role.privilege");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		// 显示列表页面
		return new ModelAndView("/sys/role/role_privilege", modelMap);
	}

	/**
	 * 显示菜单页面
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/roleMenus")
	public ModelAndView roleMenus(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String roleId = ParamUtil.getParameter(request, "roleId");
		SysRole bean = sysRoleService.findById(roleId);
		request.setAttribute("role", bean);

		String x_view = ViewProperties.getString("role.roleMenus");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		// 显示列表页面
		return new ModelAndView("/sys/role/roleMenus", modelMap);
	}

	@RequestMapping("roleMenusJson")
	@ResponseBody
	public byte[] roleMenusJson(HttpServletRequest request) throws IOException {
		String roleId = RequestUtils.getString(request, "roleId");
		long parentId = ParamUtil.getLongParameter(request, "parentId", 3);
		SysApplication root = sysApplicationService.findById(parentId);
		List<SysApplication> list = sysApplicationService.getApplicationListWithChildren(parentId);
		List<TreeModel> treeModels = new ArrayList<TreeModel>();
		Map<Long, SysApplication> disableMap = new HashMap<Long, SysApplication>();

		for (SysApplication tree : list) {
			if (tree.getLocked() == 1 || tree.getDeleteFlag() == 1) {
				disableMap.put(tree.getId(), tree);
			}
			if (disableMap.get(tree.getParentId()) != null) {
				disableMap.put(tree.getParentId(), tree);
			}

			for (SysApplication t : list) {
				if (t.getLocked() == 1 || t.getDeleteFlag() == 1) {
					disableMap.put(t.getId(), t);
					continue;
				}
				if (disableMap.get(t.getParentId()) != null) {
					disableMap.put(t.getId(), t);
					continue;
				}
			}
		}

		List<SysApplication> apps = sysApplicationService.getSysApplicationsByRoleId(roleId);

		logger.debug("tree list size:" + list.size());
		logger.debug("disableMap size:" + disableMap.size());

		for (SysApplication bean : list) {
			if (bean.getLocked() == 1 || bean.getDeleteFlag() == 1) {
				disableMap.put(bean.getId(), bean);
				continue;
			}
			if (disableMap.get(bean.getParentId()) != null) {
				disableMap.put(bean.getId(), bean);
				continue;
			}
			if (bean.getId() != root.getId()) {
				treeModels.add(bean);
			}

			if (apps != null && !apps.isEmpty()) {
				for (SysApplication app : apps) {
					if (app.getLocked() == 1 || app.getDeleteFlag() == 1) {
						disableMap.put(bean.getId(), bean);
						treeModels.remove(bean);
						continue;
					}
					if (bean.getId() == app.getId()) {
						Map<String, Object> dataMap = new HashMap<String, Object>();
						dataMap.put("checked", true);
						bean.setDataMap(dataMap);
						bean.setChecked(true);
					}
				}
			}
		}

		// logger.debug("treeModels:" + treeModels.size());
		TreeHelper treeHelper = new TreeHelper();
		JSONArray jsonArray = treeHelper.getTreeJSONArray(treeModels);
		return jsonArray.toJSONString().getBytes("UTF-8");
	}

	@RequestMapping("/roleUsers")
	public ModelAndView roleUsers(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String id = ParamUtil.getParameter(request, "id");
		SysRole sysRole = null;
		if (id != null) {
			sysRole = sysRoleService.findById(id);
			request.setAttribute("sysRole", sysRole);
		}

		if (sysRole == null) {
			id = ParamUtil.getParameter(request, "roleId");
			sysRole = sysRoleService.findById(id);
			request.setAttribute("sysRole", sysRole);
		}

		List<SysUser> allUsers = sysUserService.getSysUserList(0);
		List<SysUser> users = sysUserService.getSysUsersByRoleId(sysRole.getId());

		List<String> selecteds = new ArrayList<String>();

		if (users != null && !users.isEmpty()) {
			for (SysUser u : users) {
				selecteds.add(u.getUserId());
			}
		}

		StringBuilder bufferx = new StringBuilder();
		StringBuilder buffery = new StringBuilder();

		if (allUsers != null && allUsers.size() > 0) {
			for (int j = 0, len = allUsers.size(); j < len; j++) {
				SysUser r = (SysUser) allUsers.get(j);
				if (selecteds != null && selecteds.contains(r.getUserId())) {
					buffery.append("\n<option value=\"").append(r.getUserId()).append("\">").append(r.getName())
							.append(" [").append(r.getUserId()).append("]").append("</option>");
				} else {
					bufferx.append("\n<option value=\"").append(r.getUserId()).append("\">").append(r.getName())
							.append(" [").append(r.getUserId()).append("]").append("</option>");
				}
			}
		}

		request.setAttribute("bufferx", bufferx.toString());
		request.setAttribute("buffery", buffery.toString());

		String x_view = ViewProperties.getString("role.roleUsers");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/role/roleUsers", modelMap);
	}

	@RequestMapping("/roleUsersJson")
	@ResponseBody
	public byte[] roleUsersJson(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		JSONObject result = new JSONObject();
		String roleCode = request.getParameter("roleCode");
		List<SysUser> roleUsers = sysUserService.getSysUsersByRoleCode(roleCode);
		Collection<String> userIds = new HashSet<String>();
		if (roleUsers != null && !roleUsers.isEmpty()) {
			for (SysUser u : roleUsers) {
				userIds.add(u.getUserId());
			}
		}
		List<SysUser> users = sysUserService.getSysUserWithOrganizationList();
		if (users != null) {
			logger.debug("users size:" + users.size());
			List<TreeModel> treeModels = new ArrayList<TreeModel>();
			logger.debug("treeModels:" + treeModels.size());
			TreeHelper treeHelper = new TreeHelper();
			JSONArray jsonArray = treeHelper.getTreeJSONArray(treeModels);
			// logger.debug(jsonArray.toJSONString());
			return jsonArray.toJSONString().getBytes("UTF-8");
		}
		return result.toString().getBytes("UTF-8");
	}

	/**
	 * 提交增加信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveAdd")
	@ResponseBody
	public byte[] saveAdd(HttpServletRequest request) {
		try {
			if (sysRoleService.findByCode(ParamUtil.getParameter(request, "code")) == null) {
				SysRole bean = new SysRole();
				Tools.populate(bean, RequestUtils.getParameterMap(request));
				bean.setName(ParamUtil.getParameter(request, "name"));
				bean.setContent(ParamUtil.getParameter(request, "content"));
				bean.setCode(ParamUtil.getParameter(request, "code"));
				bean.setIndexUrl(ParamUtil.getParameter(request, "indexUrl"));
				bean.setIsUseBranch(ParamUtil.getParameter(request, "isUseBranch"));
				bean.setIsUseOrganization(ParamUtil.getParameter(request, "isUseOrganization"));
				bean.setSort(ParamUtil.getIntParameter(request, "sort", 0));
				bean.setCreateBy(RequestUtils.getActorId(request));
				bean.setUpdateBy(RequestUtils.getActorId(request));
				sysRoleService.create(bean);
				return ResponseUtils.responseResult(true);
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
		return ResponseUtils.responseResult(false);
	}

	/**
	 * 提交修改信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveModify")
	@ResponseBody
	public byte[] saveModify(HttpServletRequest request) {
		String roleId = ParamUtil.getParameter(request, "id");
		try {
			SysRole bean = sysRoleService.findById(roleId);
			if (bean != null) {
				Tools.populate(bean, RequestUtils.getParameterMap(request));
				bean.setName(ParamUtil.getParameter(request, "name"));
				bean.setContent(ParamUtil.getParameter(request, "content"));
				bean.setCode(ParamUtil.getParameter(request, "code"));
				bean.setIndexUrl(ParamUtil.getParameter(request, "indexUrl"));
				bean.setIsUseBranch(ParamUtil.getParameter(request, "isUseBranch"));
				bean.setIsUseOrganization(ParamUtil.getParameter(request, "isUseOrganization"));
				bean.setSort(ParamUtil.getIntParameter(request, "sort", 0));
				bean.setUpdateBy(RequestUtils.getActorId(request));
				boolean ret = sysRoleService.update(bean);
				return ResponseUtils.responseResult(ret);
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("saveRoleMenus")
	@ResponseBody
	public byte[] saveRoleMenus(HttpServletRequest request) throws IOException {
		String roleId = RequestUtils.getString(request, "roleId");
		if (roleId != null) {
			SysRole role = sysRoleService.findById(roleId);
			if (role != null) {
				String x_nodeIds = request.getParameter("nodeIds");
				List<Long> nodeIds = new ArrayList<Long>();
				String[] ids = x_nodeIds.split(",");
				for (int i = 0; i < ids.length; i++) {
					String nodeId = ids[i];
					nodeIds.add(Long.parseLong(nodeId));
				}
				if (!nodeIds.isEmpty()) {
					long parentId = ParamUtil.getIntParameter(request, "parentId", 3);
					List<Long> appIds = new ArrayList<Long>();
					List<SysApplication> list = sysApplicationService.getApplicationListWithChildren(parentId);
					for (SysApplication app : list) {
						if (nodeIds.contains(app.getId())) {
							if (!appIds.contains(app.getId())) {
								appIds.add(app.getId());
							}
						}
					}
					if (appIds != null && !appIds.isEmpty()) {
						sysApplicationService.saveRoleApplications(roleId, appIds);
					}
					return ResponseUtils.responseJsonResult(true);
				}
			}
		}

		return ResponseUtils.responseJsonResult(false);
	}

	@RequestMapping("saveRoleUsers")
	@ResponseBody
	public byte[] saveRoleUsers(HttpServletRequest request) throws IOException {
		String roleId = RequestUtils.getString(request, "roleId");
		if (roleId != null) {
			SysRole role = sysRoleService.findById(roleId);
			if (role != null) {
				String userIds = request.getParameter("objectIds");
				List<String> actorIds = new ArrayList<String>();
				String[] ids = userIds.split(",");
				for (int i = 0; i < ids.length; i++) {
					String userId = ids[i];
					actorIds.add(userId);
				}
				sysUserService.saveRoleUsers(SysConstants.SYSTEM_TENANT, roleId, 0, actorIds);
				return ResponseUtils.responseJsonResult(true);
			}
		}

		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource
	public void setSysApplicationService(SysApplicationService sysApplicationService) {
		this.sysApplicationService = sysApplicationService;
	}

	@javax.annotation.Resource
	public void setSysRoleService(SysRoleService sysRoleService) {
		this.sysRoleService = sysRoleService;
	}

	@javax.annotation.Resource
	public void setSysUserService(SysUserService sysUserService) {
		this.sysUserService = sysUserService;
	}

	@ResponseBody
	@RequestMapping("/viewJson")
	public byte[] viewJson(HttpServletRequest request) throws IOException {
		RequestUtils.setRequestParameterToAttribute(request);
		String roleId = ParamUtil.getParameter(request, "roleId");
		SysRole sysRole = sysRoleService.findById(roleId);
		return sysRole.toJsonObject().toJSONString().getBytes("UTF-8");
	}
}