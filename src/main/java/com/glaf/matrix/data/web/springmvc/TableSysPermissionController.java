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
import com.glaf.core.base.BaseItem;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.identity.Group;
import com.glaf.core.identity.Role;
import com.glaf.core.identity.User;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;
import com.glaf.matrix.data.domain.SysTable;
import com.glaf.matrix.data.domain.TableSysPermission;
import com.glaf.matrix.data.query.TableSysPermissionQuery;
import com.glaf.matrix.data.service.ITableService;
import com.glaf.matrix.data.service.TableSysPermissionService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/sys/tableSysPermission")
@RequestMapping("/sys/tableSysPermission")
public class TableSysPermissionController {
	protected static final Log logger = LogFactory.getLog(TableSysPermissionController.class);

	protected ITableService tableService;

	protected TableSysPermissionService tableSysPermissionService;

	public TableSysPermissionController() {

	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		TableSysPermissionQuery query = new TableSysPermissionQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		if (!loginContext.isSystemAdministrator()) {
			String actorId = loginContext.getActorId();
			query.createBy(actorId);
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
		int total = tableSysPermissionService.getTableSysPermissionCountByQueryCriteria(query);
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

			List<TableSysPermission> list = tableSysPermissionService.getTableSysPermissionsByQueryCriteria(start,
					limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (TableSysPermission tableSysPermission : list) {
					JSONObject rowJSON = tableSysPermission.toJsonObject();
					rowJSON.put("id", tableSysPermission.getId());
					rowJSON.put("rowId", tableSysPermission.getId());
					rowJSON.put("tableSysPermissionId", tableSysPermission.getId());
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

		return new ModelAndView("/sys/tableSysPermission/list", modelMap);
	}

	@RequestMapping("/privilege")
	public ModelAndView privilege(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		RequestUtils.setRequestParameterToAttribute(request);
		String tableId = request.getParameter("tableId");
		String privilege = request.getParameter("privilege");
		String granteeType = request.getParameter("granteeType");
		logger.debug("params:" + RequestUtils.getParameterMap(request));
		if (StringUtils.isNotEmpty(tableId) && StringUtils.isNotEmpty(granteeType)) {
			SysTable tableDefinition = tableService.getSysTableById(tableId);
			request.setAttribute("tableDefinition", tableDefinition);
			TableSysPermissionQuery query = new TableSysPermissionQuery();
			query.tableId(tableId);
			query.privilege(privilege != null ? privilege : "XX");
			query.granteeType(granteeType);
			query.type("SYS");
			List<TableSysPermission> list = tableSysPermissionService.list(query);
			List<BaseItem> selected = new ArrayList<BaseItem>();
			List<BaseItem> unselected = new ArrayList<BaseItem>();
			if (StringUtils.equals(granteeType, "role")) {
				request.setAttribute("title", "角色");
				List<Role> roles = IdentityFactory.getRoles();
				if (roles != null && !roles.isEmpty()) {
					boolean include = false;
					for (Role role : roles) {
						include = false;
						if (list != null && !list.isEmpty()) {
							for (TableSysPermission p : list) {
								if (StringUtils.equals(role.getId(), p.getGrantee())) {
									BaseItem item = new BaseItem();
									item.setName(role.getName());
									item.setValue(role.getId());
									selected.add(item);
									include = true;
								}
							}
						}
						if (!include) {
							BaseItem item = new BaseItem();
							item.setName(role.getName());
							item.setValue(role.getId());
							unselected.add(item);
						}
					}
				}
			} else if (StringUtils.equals(granteeType, "group")) {
				request.setAttribute("title", "用户组");
				List<Group> groups = IdentityFactory.getGroups(loginContext.getTenantId());
				if (groups != null && !groups.isEmpty()) {
					boolean include = false;
					for (Group group : groups) {
						include = false;
						if (list != null && !list.isEmpty()) {
							for (TableSysPermission p : list) {
								if (StringUtils.equals(group.getGroupId(), p.getGrantee())) {
									BaseItem item = new BaseItem();
									item.setName(group.getName());
									item.setValue(group.getGroupId());
									selected.add(item);
									include = true;
								}
							}
						}
						if (!include) {
							BaseItem item = new BaseItem();
							item.setName(group.getName());
							item.setValue(group.getGroupId());
							unselected.add(item);
						}
					}
				}
			}
			request.setAttribute("selected", selected);
			request.setAttribute("unselected", unselected);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("tableSysPermission.privilege");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/tableSysPermission/privilege", modelMap);
	}

	@ResponseBody
	@RequestMapping("/savePrivileges")
	public byte[] savePrivileges(HttpServletRequest request) {
		RequestUtils.setRequestParameterToAttribute(request);
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		String tableId = request.getParameter("tableId");
		String privilege = request.getParameter("privilege");
		String granteeType = request.getParameter("granteeType");
		String objectIds = request.getParameter("objectIds");
		if (StringUtils.isNotEmpty(tableId) && StringUtils.isNotEmpty(granteeType)) {
			try {
				SysTable tableDefinition = tableService.getSysTableById(tableId);
				List<TableSysPermission> rows = new ArrayList<TableSysPermission>();
				StringTokenizer token = new StringTokenizer(objectIds, ",");
				while (token.hasMoreTokens()) {
					String str = token.nextToken();
					TableSysPermission p = new TableSysPermission();
					p.setCreateBy(actorId);
					p.setGrantee(str);
					p.setGranteeType(granteeType);
					p.setPrivilege(privilege);
					p.setTableId(tableId);
					p.setTableName(tableDefinition.getTableName());
					p.setLocked(0);
					rows.add(p);
				}
				tableSysPermissionService.savePrivileges(tableId, granteeType, privilege, "SYS", rows);
				return ResponseUtils.responseJsonResult(true);
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource
	public void setTableService(ITableService tableService) {
		this.tableService = tableService;
	}

	@javax.annotation.Resource
	public void setTableSysPermissionService(TableSysPermissionService tableSysPermissionService) {
		this.tableSysPermissionService = tableSysPermissionService;
	}

}
