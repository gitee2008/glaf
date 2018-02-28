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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.base.modules.sys.model.Group;
import com.glaf.base.modules.sys.query.GroupQuery;
import com.glaf.base.modules.sys.service.GroupService;
import com.glaf.base.modules.sys.service.SysUserService;
import com.glaf.base.utils.ParamUtil;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.identity.User;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.PageResult;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;

@Controller("/group")
@RequestMapping("/group")
public class GroupController {
	private static final Log logger = LogFactory.getLog(GroupController.class);

	protected GroupService groupService;

	protected SysUserService sysUserService;

	/**
	 * 提交增加信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/create")
	@ResponseBody
	public byte[] create(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String type = request.getParameter("type");
		Group bean = new Group();
		bean.setName(ParamUtil.getParameter(request, "name"));
		bean.setCode(ParamUtil.getParameter(request, "code"));
		bean.setDesc(ParamUtil.getParameter(request, "desc"));
		bean.setCreateBy(RequestUtils.getActorId(request));
		bean.setUpdateBy(RequestUtils.getActorId(request));
		bean.setTenantId(loginContext.getTenantId());
		bean.setType(type);
		try {
			groupService.save(bean);
			return ResponseUtils.responseResult(true);
		} catch (Exception ex) {
			logger.error(ex);
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String id = request.getParameter("groupId");
		Group bean = groupService.getGroup(id);
		request.setAttribute("bean", bean);

		String x_view = ViewProperties.getString("group.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/group/group_edit", modelMap);
	}

	/**
	 * 显示群组用户页面
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("groupLeaders")
	public ModelAndView groupLeaders(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String x_view = ViewProperties.getString("group.groupLeaders");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		// 显示群组用户页面
		return new ModelAndView("/group/groupLeaders", modelMap);
	}

	/**
	 * 显示群组用户页面
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/groupUsers")
	public ModelAndView groupUsers(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String x_view = ViewProperties.getString("group.groupUsers");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		// 显示群组用户页面
		return new ModelAndView("/group/group_users", modelMap);
	}

	@RequestMapping
	public ModelAndView list(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("group.list");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/group/list", modelMap);
	}

	@RequestMapping("/listGroupLeader")
	public ModelAndView listGroupLeader(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String x_view = ViewProperties.getString("group.listGroupLeader");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/group/listGroupLeader", modelMap);
	}

	@RequestMapping("/listGroupLeaderJson")
	@ResponseBody
	public byte[] listGroupLeaderJson(HttpServletRequest request) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		GroupQuery query = new GroupQuery();
		Tools.populate(query, params);
		query.tenantId(loginContext.getTenantId());
		// String groupId=RequestUtils.getString(request, "groupId","");
		List<Group> list = groupService.getGroupLeadersByGroupId(query);
		JSONObject result = new JSONObject();
		int start = 0;
		if (list != null && !list.isEmpty()) {
			JSONArray rowsJSON = new JSONArray();

			result.put("rows", rowsJSON);

			for (Group group : list) {
				JSONObject rowJSON = group.toJsonObject();

				rowJSON.put("startIndex", ++start);
				rowsJSON.add(rowJSON);
			}

		} else {
			JSONArray rowsJSON = new JSONArray();
			result.put("rows", rowsJSON);
			result.put("total", 0);
		}
		return result.toString().getBytes("UTF-8");
	}

	@RequestMapping("/listGroupUser")
	public ModelAndView listGroupUser(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String x_view = ViewProperties.getString("group.listGroupUser");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/group/listGroupUser", modelMap);
	}

	@RequestMapping("/listGroupUserJson")
	@ResponseBody
	public byte[] listGroupUserJson(HttpServletRequest request) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		GroupQuery query = new GroupQuery();
		Tools.populate(query, params);
		query.tenantId(loginContext.getTenantId());
		// String groupId=RequestUtils.getString(request, "groupId","");
		List<Group> list = groupService.getGroupUsersByGroupId(query);
		JSONObject result = new JSONObject();
		int start = 0;
		if (list != null && !list.isEmpty()) {
			JSONArray rowsJSON = new JSONArray();

			result.put("rows", rowsJSON);

			for (Group group : list) {
				JSONObject rowJSON = group.toJsonObject();

				rowJSON.put("startIndex", ++start);
				rowsJSON.add(rowJSON);
			}

		} else {
			JSONArray rowsJSON = new JSONArray();
			result.put("rows", rowsJSON);
			result.put("total", 0);
		}
		return result.toString().getBytes("UTF-8");
	}

	@RequestMapping("/listJson")
	@ResponseBody
	public byte[] listJson(HttpServletRequest request) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		GroupQuery query = new GroupQuery();
		Tools.populate(query, params);
		query.tenantId(loginContext.getTenantId());

		int start = 0;
		int limit = 10;
		String orderName = null;
		String order = null;

		int pageNo = ParamUtils.getInt(params, "page");
		limit = ParamUtils.getInt(params, "rows");
		start = (pageNo - 1) * limit;
		orderName = ParamUtil.getParameter(request, "sort", "");
		order = ParamUtil.getParameter(request, "order", "");

		if (start < 0) {
			start = 0;
		}

		if (limit <= 0) {
			limit = PageResult.DEFAULT_PAGE_SIZE;
		}

		String type = request.getParameter("type");
		query.type(type);

		JSONObject result = new JSONObject();
		int total = groupService.getGroupCountByQueryCriteria(query);
		if (total > 0) {
			result.put("total", total);
			result.put("totalCount", total);
			result.put("totalRecords", total);
			result.put("start", start);
			result.put("startIndex", start);
			result.put("limit", limit);
			result.put("pageSize", limit);

			if (StringUtils.isNotEmpty(orderName)) {
				query.setOrderBy(orderName + " " + order);
			}

			List<Group> list = groupService.getGroupsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (Group group : list) {
					JSONObject rowJSON = group.toJsonObject();
					rowJSON.put("startIndex", ++start);
					rowJSON.put("groupId", group.getGroupId());
					rowsJSON.add(rowJSON);
				}

			}
		} else {
			JSONArray rowsJSON = new JSONArray();
			result.put("rows", rowsJSON);
			result.put("total", 0);
		}
		return result.toString().getBytes("UTF-8");
	}

	/**
	 * 显示增加页面
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/prepareAdd")
	public ModelAndView prepareAdd(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String x_view = ViewProperties.getString("group.prepareAdd");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		// 显示列表页面
		return new ModelAndView("/group/group_add", modelMap);
	}

	/**
	 * 显示修改页面
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/prepareModify")
	public ModelAndView prepareModify(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String id = request.getParameter("groupId");
		Group bean = groupService.getGroup(id);
		request.setAttribute("bean", bean);

		String x_view = ViewProperties.getString("group.prepareModify");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		// 显示列表页面
		return new ModelAndView("/group/group_modify", modelMap);
	}

	@RequestMapping("/saveEdit")
	@ResponseBody
	public byte[] saveEdit(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		String type = request.getParameter("type");
		String groupId = request.getParameter("groupId");
		Group bean = groupService.getGroup(groupId);
		if (null == bean) {
			bean = new Group();
		}
		Tools.populate(bean, params);

		bean.setCreateBy(RequestUtils.getActorId(request));
		bean.setTenantId(loginContext.getTenantId());
		bean.setType(type);
		try {
			groupService.save(bean);
			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {

		}
		return ResponseUtils.responseJsonResult(false);
	}

	@RequestMapping("/saveGroupLeaders")
	@ResponseBody
	public byte[] saveGroupLeaders(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String groupId = request.getParameter("groupId");
		String objectId = request.getParameter("userIds");
		String type = RequestUtils.getString(request, "type", "");
		if (StringUtils.isNotEmpty(groupId)) {
			Map<String, User> userMap = IdentityFactory.getUserMap();
			Set<String> userIds = new HashSet<String>();
			if (StringUtils.isNotEmpty(objectId)) {
				StringTokenizer token = new StringTokenizer(objectId, ",");
				while (token.hasMoreTokens()) {
					String userId = token.nextToken();
					if (userMap.containsKey(userId)) {
						userIds.add(userId);
					}
				}
			}
			try {
				if (StringUtils.isEmpty(type)) {
					groupService.saveGroupLeaders(loginContext.getTenantId(), groupId, userIds);
				} else {
					groupService.saveOrUpdateGroupLeaders(loginContext.getTenantId(), groupId, userIds);
				}
				return ResponseUtils.responseJsonResult(true);
			} catch (Exception ex) {

			}
		}

		return ResponseUtils.responseJsonResult(true);
	}

	@RequestMapping("/saveGroupUsers")
	@ResponseBody
	public byte[] saveGroupUsers(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String groupId = request.getParameter("groupId");
		String objectId = request.getParameter("userIds");
		String type = RequestUtils.getString(request, "type", "");
		if (StringUtils.isNotEmpty(groupId)) {
			Map<String, User> userMap = IdentityFactory.getUserMap();
			Set<String> userIds = new HashSet<String>();
			if (StringUtils.isNotEmpty(objectId)) {
				StringTokenizer token = new StringTokenizer(objectId, ",");
				while (token.hasMoreTokens()) {
					String userId = token.nextToken();
					if (userMap.containsKey(userId)) {
						userIds.add(userId);
					}
				}
			}
			try {
				if (StringUtils.isEmpty(type)) {
					groupService.saveGroupUsers(loginContext.getTenantId(), groupId, userIds);
				} else {
					groupService.saveOrUpdateGroupUsers(loginContext.getTenantId(), groupId, userIds);
				}
				return ResponseUtils.responseJsonResult(true);
			} catch (Exception ex) {

			}
		}

		return ResponseUtils.responseJsonResult(true);
	}

	@javax.annotation.Resource
	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	@javax.annotation.Resource
	public void setSysUserService(SysUserService sysUserService) {
		this.sysUserService = sysUserService;
	}

	/**
	 * 提交修改信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/update")
	@ResponseBody
	public byte[] update(HttpServletRequest request) {
		String groupId = request.getParameter("groupId");
		Group bean = groupService.getGroup(groupId);
		if (bean != null) {
			bean.setName(ParamUtil.getParameter(request, "name"));
			bean.setDesc(ParamUtil.getParameter(request, "desc"));
			bean.setUpdateBy(RequestUtils.getActorId(request));
		}
		try {
			groupService.save(bean);
			return ResponseUtils.responseResult(true);
		} catch (Exception ex) {
			logger.error(ex);
		}
		return ResponseUtils.responseResult(false);
	}
}