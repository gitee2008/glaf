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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.glaf.base.modules.sys.model.SysOrganization;
import com.glaf.base.modules.sys.model.SysRole;
import com.glaf.base.modules.sys.model.SysUser;
import com.glaf.base.modules.sys.query.SysOrganizationQuery;
import com.glaf.base.modules.sys.service.DictoryService;
import com.glaf.base.modules.sys.service.SysOrganizationService;
import com.glaf.base.modules.sys.service.SysRoleService;
import com.glaf.base.modules.sys.service.SysUserService;
import com.glaf.base.modules.sys.util.SysRoleJsonFactory;
import com.glaf.base.utils.ParamUtil;
import com.glaf.core.base.BaseTree;
import com.glaf.core.base.ColumnModel;
import com.glaf.core.base.TableModel;
import com.glaf.core.base.TreeModel;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.factory.DataServiceFactory;
import com.glaf.core.tree.helper.JacksonTreeHelper;
import com.glaf.core.tree.helper.TreeUpdateBean;
import com.glaf.core.util.PageResult;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;

@Controller("/sys/organization")
@RequestMapping("/sys/organization")
public class SysOrganizationController {
	private static final Log logger = LogFactory.getLog(SysOrganizationController.class);

	protected DictoryService dictoryService;

	protected SysRoleService sysRoleService;

	protected SysUserService sysUserService;

	protected SysOrganizationService sysOrganizationService;

	/**
	 * 批量删除信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/batchDelete")
	@ResponseBody
	public byte[] batchDelete(HttpServletRequest request) {
		RequestUtils.setRequestParameterToAttribute(request);
		long[] ids = ParamUtil.getLongParameterValues(request, "id");
		if (ids != null && ids.length > 0) {
			try {
				sysOrganizationService.markDeleteFlag(ids, 1);
				return ResponseUtils.responseResult(true);
			} catch (Exception ex) {
				logger.error(ex);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request) throws IOException {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SysOrganizationQuery query = new SysOrganizationQuery();
		Tools.populate(query, params);
		query.setDeleteFlag(0);
		
		String namePinyinLike = request.getParameter("namePinyinLike");
		if (StringUtils.isNotEmpty(namePinyinLike)) {
			query.setNamePinyinLike(namePinyinLike);
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
			limit = PageResult.DEFAULT_PAGE_SIZE;
		}

		JSONObject result = new JSONObject();
		int total = sysOrganizationService.getSysOrganizationCountByQueryCriteria(query);
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
				if (StringUtils.equals(order, "description")) {
					query.setSortOrder(" description ");
				}
			}

			List<SysOrganization> list = sysOrganizationService.getSysOrganizationsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (SysOrganization sysOrganization : list) {
					JSONObject rowJSON = sysOrganization.toJsonObject();
					rowJSON.put("id", sysOrganization.getId());
					rowJSON.put("sysOrganizationId", sysOrganization.getId());
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

		List<String> charList = new ArrayList<String>();
		for (int i = 65; i < 91; i++) {
			charList.add("" + (char) i);
		}
		request.setAttribute("charList", charList);
		
		String x_view = ViewProperties.getString("organization.list");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/sys/organization/list", modelMap);
	}

	@ResponseBody
	@RequestMapping("/organizationRolesJson")
	public byte[] organizationRolesJson(HttpServletRequest request) {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug(params);

		SysOrganizationQuery query = new SysOrganizationQuery();

		query.setDeleteFlag(0);
		List<SysRole> roles = sysRoleService.getSysRoleList();

		JSONArray array = SysRoleJsonFactory.listToArray(roles);

		logger.debug(array.toString());
		try {
			return array.toString().getBytes("UTF-8");
		} catch (IOException e) {
			return array.toString().getBytes();
		}
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

		String x_view = ViewProperties.getString("organization.prepareAdd");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/organization/organization_add", modelMap);
	}

	/**
	 * 显示修改页面
	 * 
	 * 
	 * @param form
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/prepareModify")
	public ModelAndView prepareModify(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		long id = ParamUtil.getLongParameter(request, "id", 0);
		SysOrganization o = sysOrganizationService.findById(id);
		request.setAttribute("organization", o);

		if (o != null && o.getParentId() > 0) {
			SysOrganization parent = sysOrganizationService.findById(o.getParentId());
			request.setAttribute("parent", parent);
			if (parent != null) {
				request.setAttribute("parentName", parent.getName());
			}
		}

		String x_view = ViewProperties.getString("organization.prepareModify");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		// 显示列表页面
		return new ModelAndView("/sys/organization/organization_modify", modelMap);
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
			SysOrganization bean = new SysOrganization();
			Tools.populate(bean, RequestUtils.getParameterMap(request));
			bean.setName(ParamUtil.getParameter(request, "name"));
			bean.setDescription(ParamUtil.getParameter(request, "description"));
			bean.setCode(ParamUtil.getParameter(request, "code"));
			bean.setCode2(ParamUtil.getParameter(request, "code2"));
			bean.setNo(ParamUtil.getParameter(request, "no"));
			bean.setAddress(request.getParameter("address"));
			bean.setTelphone(request.getParameter("telphone"));
			bean.setPrincipal(request.getParameter("principal"));
			bean.setLevel(RequestUtils.getInt(request, "level"));
			bean.setCreateTime(new Date());
			bean.setCreateBy(RequestUtils.getActorId(request));
			bean.setLocked(0);
			sysOrganizationService.create(bean);
			return ResponseUtils.responseJsonResult(true, "添加机构成功。");
		} catch (Exception ex) {
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false, "添加机构失败。");
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
		long id = ParamUtil.getIntParameter(request, "id", 0);
		try {
			SysOrganization bean = sysOrganizationService.findById(id);
			if (bean != null) {
				Tools.populate(bean, RequestUtils.getParameterMap(request));
				bean.setUpdateBy(RequestUtils.getActorId(request));
				bean.setName(ParamUtil.getParameter(request, "name"));
				bean.setDescription(ParamUtil.getParameter(request, "description"));
				bean.setCode(ParamUtil.getParameter(request, "code"));
				bean.setCode2(ParamUtil.getParameter(request, "code2"));
				bean.setNo(ParamUtil.getParameter(request, "no"));
				bean.setAddress(request.getParameter("address"));
				bean.setTelphone(request.getParameter("telphone"));
				bean.setPrincipal(request.getParameter("principal"));
				bean.setLocked(ParamUtil.getIntParameter(request, "locked", 0));
				bean.setLevel(RequestUtils.getInt(request, "level"));
				sysOrganizationService.update(bean);

				TreeUpdateBean updateBean = new TreeUpdateBean();
				updateBean.updateTreeIds("default", "SYS_ORGANIZATION", null, "ID", "PARENTID", "TREEID", "LEVEL",
						null);

				return ResponseUtils.responseResult(true);
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
		return ResponseUtils.responseResult(false);
	}

	/**
	 * 排序
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
					t1.setTableName("SYS_ORGANIZATION");
					ColumnModel idColumn1 = new ColumnModel();
					idColumn1.setColumnName("ID");
					idColumn1.setValue(Long.parseLong(item));
					t1.setIdColumn(idColumn1);
					ColumnModel column = new ColumnModel();
					column.setColumnName("SORTNO");
					column.setValue(sort);
					t1.addColumn(column);
					rows.add(t1);
				}
			}
			try {
				DataServiceFactory.getInstance().updateAllTableData(rows);
				return ResponseUtils.responseResult(true);
			} catch (Exception ex) {
				logger.error(ex);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@javax.annotation.Resource
	public void setDictoryService(DictoryService dictoryService) {
		this.dictoryService = dictoryService;
	}

	@javax.annotation.Resource
	public void setSysOrganizationService(SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}

	@javax.annotation.Resource
	public void setSysRoleService(SysRoleService sysRoleService) {
		this.sysRoleService = sysRoleService;
	}

	@javax.annotation.Resource
	public void setSysUserService(SysUserService sysUserService) {
		this.sysUserService = sysUserService;
	}

	/**
	 * 显示所有列表
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/showRoleUsers")
	public ModelAndView showRoleUsers(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		long organizationId = ParamUtil.getLongParameter(request, "organizationId", 0);
		// String roleCode = request.getParameter("roleCode");

		SysOrganization organization = sysOrganizationService.getSysOrganization(organizationId);
		if (organization != null) {
			Collection<SysUser> users = sysUserService.getSysUserList(organization.getId());
			request.setAttribute("organization", organization);
			request.setAttribute("users", users);
		}

		String x_view = ViewProperties.getString("organization.showOrganizationRoleUsers");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/organization/organization_role_user", modelMap);
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
		RequestUtils.setRequestParameterToAttribute(request);
		long parentId = ParamUtil.getIntParameter(request, "parentId", 0);
		if (parentId > 0) {
			List<SysOrganization> list = sysOrganizationService.getSysOrganizationList(parentId);
			request.setAttribute("trees", list);
		}

		String x_view = ViewProperties.getString("sys.organization.showSort");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/organization/showSort", modelMap);
	}

	/**
	 * 显示tree页面
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/showTreeRadio")
	public ModelAndView showTree(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		long parentId = ParamUtil.getIntParameter(request, "parentId", 0);
		if (parentId > 0) {
			List<SysOrganization> list = sysOrganizationService.getSysOrganizationList(parentId);
			request.setAttribute("trees", list);
		}

		String x_view = ViewProperties.getString("sys.organization.showTreeRadio");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/organization/showTreeRadio", modelMap);
	}

	@ResponseBody
	@RequestMapping("/treeJson")
	public byte[] treeJson(HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.debug("------------------------treeJson--------------------");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		RequestUtils.setRequestParameterToAttribute(request);
		List<Long> selected = new ArrayList<Long>();

		SysOrganization root = null;
		long parentId = RequestUtils.getLong(request, "parentId");
		String code = request.getParameter("code");
		if (StringUtils.isNotEmpty(code)) {
			root = sysOrganizationService.findByCode(code);
		} else if (parentId > 0) {
			root = sysOrganizationService.findById(parentId);
		}

		List<SysOrganization> trees = null;
		if (root != null) {
			trees = sysOrganizationService.getSysOrganizationWithChildren(parentId);
		} else {
			trees = sysOrganizationService.getSysOrganizationList();
		}

		List<TreeModel> treeModels = new ArrayList<TreeModel>();
		if (trees != null && !trees.isEmpty()) {
			for (SysOrganization t : trees) {
				TreeModel tree = new BaseTree();
				tree.setId(t.getId());
				tree.setParentId(t.getParentId());
				tree.setName(t.getName());
				tree.setTreeId(t.getTreeId());
				tree.setIcon(t.getIcon());
				tree.setIconCls(t.getIcon());
				tree.setDiscriminator(t.getDiscriminator());
				tree.setCode(t.getCode());
				tree.setSortNo(t.getSort());
				tree.setLevel(t.getLevel());
				Map<String, Object> dataMap = new HashMap<String, Object>();
				String url = t.getUrl();
				if (StringUtils.isNotEmpty(url)) {
					if (!(url.toLowerCase().startsWith("http://") || url.toLowerCase().startsWith("https://"))) {
						if (url.startsWith("/")) {
							url = request.getContextPath() + url;
						} else {
							url = request.getContextPath() + "/" + url;
						}
					}
					if (url.indexOf("?") != -1) {
						url = url + "&time=" + System.currentTimeMillis();
					} else {
						url = url + "?time=" + System.currentTimeMillis();
					}
				}
				dataMap.put("url", url);
				if (selected.contains(t.getId())) {
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
}