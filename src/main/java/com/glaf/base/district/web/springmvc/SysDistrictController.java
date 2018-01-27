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

package com.glaf.base.district.web.springmvc;

import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import com.alibaba.fastjson.*;
import com.glaf.core.base.BaseTree;
import com.glaf.core.base.ColumnModel;
import com.glaf.core.base.TableModel;
import com.glaf.core.base.TreeModel;
import com.glaf.core.config.SystemProperties;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.factory.DataServiceFactory;
import com.glaf.core.security.*;
import com.glaf.core.tree.helper.TreeHelper;
import com.glaf.core.tree.helper.TreeUpdateBean;
import com.glaf.core.util.*;
import com.glaf.base.district.domain.*;
import com.glaf.base.district.query.*;
import com.glaf.base.district.service.*;
import com.glaf.base.modules.sys.model.SysUser;
import com.glaf.base.modules.sys.model.TreePermission;
import com.glaf.base.modules.sys.query.TreePermissionQuery;
import com.glaf.base.modules.sys.service.SysUserService;
import com.glaf.base.modules.sys.service.TreePermissionService;
import com.glaf.base.utils.XmlToDbImporter;

@Controller("/sys/district")
@RequestMapping("/sys/district")
public class SysDistrictController {
	protected static final Log logger = LogFactory.getLog(SysDistrictController.class);

	protected DistrictService districtService;

	protected SysUserService sysUserService;

	protected TreePermissionService treePermissionService;

	public SysDistrictController() {

	}

	@ResponseBody
	@RequestMapping("/delete")
	public byte[] delete(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Long id = RequestUtils.getLong(request, "id");
		String ids = request.getParameter("ids");
		if (StringUtils.isNotEmpty(ids)) {
			StringTokenizer token = new StringTokenizer(ids, ",");
			while (token.hasMoreTokens()) {
				String x = token.nextToken();
				if (StringUtils.isNotEmpty(x)) {
					District district = districtService.getDistrict(Long.valueOf(x));
					if (district != null && loginContext.isSystemAdministrator()) {
						// districtService.deleteById(district.getId());
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			District district = districtService.getDistrict(Long.valueOf(id));
			if (district != null && loginContext.isSystemAdministrator()) {
				// districtService.deleteById(district.getId());
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		District district = districtService.getDistrict(RequestUtils.getLong(request, "id"));
		if (district != null) {
			request.setAttribute("district", district);
		}

		Long parentId = RequestUtils.getLong(request, "parentId", 0);
		List<District> districts = districtService.getDistrictList(parentId);
		if (district != null) {
			List<District> children = districtService.getDistrictList(district.getId());
			if (districts != null && !districts.isEmpty()) {
				if (children != null && !children.isEmpty()) {
					districts.removeAll(children);
				}
				districts.remove(district);
			}
		}
		if (parentId > 0) {
			District parent = districtService.getDistrict(parentId);
			if (districts == null) {
				districts = new ArrayList<District>();
			}
			districts.add(parent);
		}

		request.setAttribute("trees", districts);
		request.setAttribute("districts", districts);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("district.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/district/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug("params:" + params);
		DistrictQuery query = new DistrictQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);
		String actorId = loginContext.getActorId();
		if (!loginContext.isSystemAdministrator()) {
			query.createBy(actorId);
		}

		Long parentId = RequestUtils.getLong(request, "parentId", 0);
		query.parentId(parentId);

		String gridType = ParamUtils.getString(params, "gridType");
		if (gridType == null) {
			gridType = "easyui";
		}
		int start = 0;
		int limit = 15;
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
			limit = 100;
		}

		JSONObject result = new JSONObject();
		int total = districtService.getDistrictCountByQueryCriteria(query);
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

			List<District> list = districtService.getDistrictsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (District district : list) {
					JSONObject rowJSON = district.toJsonObject();
					rowJSON.put("id", district.getId());
					rowJSON.put("pId", district.getParentId());
					rowJSON.put("districtId", district.getId());
					rowJSON.put("districtId", district.getId());
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

		return new ModelAndView("/sys/district/list", modelMap);
	}

	/**
	 * 显示授权页面
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/privilege")
	public ModelAndView privilege(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String userId = request.getParameter("userId");
		SysUser user = sysUserService.findById(userId);
		request.setAttribute("user", user);
		return new ModelAndView("/sys/district/privilege", modelMap);
	}

	@ResponseBody
	@RequestMapping("/reload")
	public byte[] reload(HttpServletRequest request) {
		try {
			XmlToDbImporter imp = new XmlToDbImporter();
			String path = SystemProperties.getConfigRootPath() + "/conf/data/district.xml";
			logger.debug("load config:" + path);
			imp.doImport(new File(path), "default");
			TreeUpdateBean updateBean = new TreeUpdateBean();
			updateBean.updateTreeIds("default", "SYS_DISTRICT", null, "ID_", "PARENTID_", "TREEID_", "LEVEL_", null);
			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@ResponseBody
	@RequestMapping("/saveDistrict")
	public byte[] saveDistrict(HttpServletRequest request) {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		District district = new District();
		try {
			Tools.populate(district, params);

			district.setName(request.getParameter("name"));
			district.setCode(request.getParameter("code"));

			this.districtService.save(district);

			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	/**
	 *
	 * 
	 * @param request
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
					t1.setTableName("SYS_DISTRICT");
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
				logger.error(ex);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@javax.annotation.Resource
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}

	@javax.annotation.Resource
	public void setSysUserService(SysUserService sysUserService) {
		this.sysUserService = sysUserService;
	}

	@javax.annotation.Resource
	public void setTreePermissionService(TreePermissionService treePermissionService) {
		this.treePermissionService = treePermissionService;
	}

	/**
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/showSort")
	public ModelAndView showSort(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		long parentId = RequestUtils.getLong(request, "parentId");

		List<District> trees = districtService.getDistrictList(parentId);
		request.setAttribute("trees", trees);

		String x_view = ViewProperties.getString("sys.district.showSort");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/district/showSort", modelMap);
	}

	@ResponseBody
	@RequestMapping("/treeJson")
	public byte[] treeJson(HttpServletRequest request) throws IOException {
		logger.debug("params:" + RequestUtils.getParameterMap(request));
		JSONArray array = new JSONArray();
		Long parentId = RequestUtils.getLong(request, "id", 0);
		List<District> districts = null;
		if (parentId != null) {
			districts = districtService.getDistrictList(parentId);
		}
		if (districts != null && !districts.isEmpty()) {
			String userId = request.getParameter("userId");
			String type = request.getParameter("type");
			List<Long> selected = new ArrayList<Long>();
			if (StringUtils.isNotEmpty(type) && StringUtils.isNotEmpty(userId)) {
				TreePermissionQuery query = new TreePermissionQuery();
				query.type(type);
				query.userId(userId);
				List<TreePermission> perms = treePermissionService.list(query);
				if (perms != null && !perms.isEmpty()) {
					for (TreePermission p : perms) {
						selected.add(p.getNodeId());
					}
				}
			}

			Map<Long, TreeModel> treeMap = new HashMap<Long, TreeModel>();
			List<TreeModel> treeModels = new ArrayList<TreeModel>();
			List<Long> districtIds = new ArrayList<Long>();
			for (District district : districts) {
				if (district.getLocked() != 0) {
					continue;
				}
				Map<String, Object> dataMap = new HashMap<String, Object>();
				TreeModel tree = new BaseTree();
				tree.setId(district.getId());
				tree.setParentId(district.getParentId());
				tree.setCode(district.getCode());
				tree.setName(district.getName());
				tree.setSortNo(district.getSortNo());
				tree.setIconCls("tree_folder");
				if (selected.contains(district.getId())) {
					if (selected.contains(district.getId())) {
						tree.setChecked(true);
						dataMap.put("checked", true);
					} else {
						dataMap.put("checked", false);
					}
				}
				tree.setDataMap(dataMap);
				treeModels.add(tree);
				districtIds.add(district.getId());
				treeMap.put(district.getId(), tree);
			}
			// logger.debug("treeModels:" + treeModels.size());
			TreeHelper treeHelper = new TreeHelper();
			JSONArray jsonArray = treeHelper.getTreeJSONArray(treeModels);
			for (int i = 0, len = jsonArray.size(); i < len; i++) {
				JSONObject json = jsonArray.getJSONObject(i);
				json.put("isParent", true);
			}
			// logger.debug(jsonArray.toJSONString());
			return jsonArray.toJSONString().getBytes("UTF-8");
		}
		return array.toJSONString().getBytes("UTF-8");
	}

	@ResponseBody
	@RequestMapping("/treeJson3")
	public byte[] treeJson3(HttpServletRequest request) throws IOException {
		logger.debug("params:" + RequestUtils.getParameterMap(request));
		JSONArray array = new JSONArray();
		Long parentId = RequestUtils.getLong(request, "id", 0);
		List<District> districts = null;
		if (parentId != null) {
			districts = districtService.getDistrictList(parentId);
		}
		if (districts != null && !districts.isEmpty()) {
			String userId = request.getParameter("userId");
			String type = request.getParameter("type");
			List<Long> selected = new ArrayList<Long>();
			if (StringUtils.isNotEmpty(type) && StringUtils.isNotEmpty(userId)) {
				TreePermissionQuery query = new TreePermissionQuery();
				query.type(type);
				query.userId(userId);
				List<TreePermission> perms = treePermissionService.list(query);
				if (perms != null && !perms.isEmpty()) {
					for (TreePermission p : perms) {
						selected.add(p.getNodeId());
					}
				}
			}

			Map<Long, TreeModel> treeMap = new HashMap<Long, TreeModel>();
			List<TreeModel> treeModels = new ArrayList<TreeModel>();
			List<Long> districtIds = new ArrayList<Long>();
			for (District district : districts) {
				if (district.getLocked() != 0) {
					continue;
				}
				if (district.getLevel() > 3) {
					continue;
				}
				Map<String, Object> dataMap = new HashMap<String, Object>();
				TreeModel tree = new BaseTree();
				tree.setId(district.getId());
				tree.setParentId(district.getParentId());
				tree.setCode(district.getCode());
				tree.setName(district.getName());
				tree.setSortNo(district.getSortNo());
				tree.setIconCls("tree_folder");
				if (selected.contains(district.getId())) {
					if (selected.contains(district.getId())) {
						tree.setChecked(true);
						dataMap.put("checked", true);
					} else {
						dataMap.put("checked", false);
					}
				}
				tree.setDataMap(dataMap);
				treeModels.add(tree);
				districtIds.add(district.getId());
				treeMap.put(district.getId(), tree);
			}
			// logger.debug("treeModels:" + treeModels.size());
			TreeHelper treeHelper = new TreeHelper();
			JSONArray jsonArray = treeHelper.getTreeJSONArray(treeModels);
			for (int i = 0, len = jsonArray.size(); i < len; i++) {
				JSONObject json = jsonArray.getJSONObject(i);
				json.put("isParent", true);
			}
			// logger.debug(jsonArray.toJSONString());
			return jsonArray.toJSONString().getBytes("UTF-8");
		}
		return array.toJSONString().getBytes("UTF-8");
	}

	@RequestMapping("/view/{id}")
	public ModelAndView view(@PathVariable("id") Long id, HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		District district = districtService.getDistrict(id);
		request.setAttribute("district", district);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view);
		}

		String x_view = ViewProperties.getString("district.view");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view);
		}

		return new ModelAndView("/sys/district/view");
	}

}
