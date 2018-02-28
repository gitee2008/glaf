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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.glaf.base.modules.sys.SysConstants;
import com.glaf.base.modules.sys.business.UpdateTreeBean;
import com.glaf.base.modules.sys.model.SysApplication;
import com.glaf.base.modules.sys.query.SysApplicationQuery;
import com.glaf.base.modules.sys.service.SysApplicationService;
import com.glaf.base.utils.ParamUtil;

import com.glaf.core.base.BaseTree;
import com.glaf.core.base.ColumnModel;
import com.glaf.core.base.TableModel;
import com.glaf.core.base.TreeModel;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.factory.DataServiceFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.tree.helper.JacksonTreeHelper;
import com.glaf.core.tree.helper.TreeUpdateBean;
import com.glaf.core.util.FileUtils;
import com.glaf.core.util.PageResult;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;
import com.glaf.core.util.ZipUtils;

@Controller("/sys/application")
@RequestMapping("/sys/application")
public class SysApplicationController {
	private static final Log logger = LogFactory.getLog(SysApplicationController.class);

	private SysApplicationService sysApplicationService;

	/**
	 * 批量删除信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/batchDelete")
	@ResponseBody
	public byte[] batchDelete(HttpServletRequest request) {
		long[] ids = ParamUtil.getLongParameterValues(request, "id");
		if (ids != null && ids.length > 0) {
			try {
				sysApplicationService.markDeleteFlag(ids, 1);
				return ResponseUtils.responseResult(true);
			} catch (Exception ex) {
				logger.error(ex);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	/**
	 * 删除信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/deleteById")
	@ResponseBody
	public byte[] deleteById(HttpServletRequest request) {
		long id = RequestUtils.getLong(request, "id");
		if (id > 0) {
			try {
				sysApplicationService.delete(id);
				return ResponseUtils.responseResult(true);
			} catch (Exception ex) {
				logger.error(ex);
			}
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
		// RequestUtils.setRequestParameterToAttribute(request);
		long id = ParamUtil.getLongParameter(request, "id", 0);
		SysApplication app = sysApplicationService.findById(id);
		if (app != null) {
			request.setAttribute("app", app);
			request.setAttribute("parentId", app.getParentId());
		}
		long parentId = ParamUtil.getIntParameter(request, "parentId", 3);
		List<SysApplication> list = sysApplicationService.getApplicationListWithChildren(parentId);
		if (list != null && !list.isEmpty()) {
			Map<Long, TreeModel> treeMap = new HashMap<Long, TreeModel>();
			List<TreeModel> treeModels = new ArrayList<TreeModel>();
			for (SysApplication app2 : list) {
				treeModels.add(app2);
				treeMap.put(app2.getId(), app2);
			}
			StringTokenizer token = null;
			StringBuilder blank = new StringBuilder();
			UpdateTreeBean bean = new UpdateTreeBean();
			List<SysApplication> appList = new ArrayList<SysApplication>();
			for (SysApplication app2 : list) {
				String treeId = bean.getTreeId(treeMap, app2);
				if (treeId != null && treeId.indexOf("|") != -1) {
					token = new StringTokenizer(treeId, "|");
					if (token != null) {
						app2.setLevel(token.countTokens());
						blank.delete(0, blank.length());
						for (int i = 0; i < app2.getLevel(); i++) {
							blank.append("&nbsp;&nbsp;&nbsp;&nbsp;");
						}
						app2.setBlank(blank.toString());
					}
				}
				appList.add(app2);
			}
			request.setAttribute("apps", appList);
		}

		String x_view = ViewProperties.getString("application.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/app/edit", modelMap);
	}

	@ResponseBody
	@RequestMapping("/importJson")
	public byte[] importJson(HttpServletRequest request) throws IOException {
		// int parentId = ParamUtil.getIntParameter(request, "parentId", 0);
		// LoginContext loginContext = RequestUtils.getLoginContext(request);
		ZipInputStream zipInputStream = null;
		boolean status = false;
		// 将当前上下文初始化给 CommonsMutipartResolver（多部分解析器）
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 检查form中是否有enctype="multipart/form-data"
		if (multipartResolver.isMultipart(request)) {
			try {
				MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
				Map<String, MultipartFile> fileMap = req.getFileMap();
				Set<Entry<String, MultipartFile>> entrySet = fileMap.entrySet();
				int maxSize = 5 * FileUtils.MB_SIZE;
				for (Entry<String, MultipartFile> entry : entrySet) {
					MultipartFile mFile = entry.getValue();
					if (mFile.getOriginalFilename() != null && mFile.getSize() > 0) {
						String filename = mFile.getOriginalFilename();
						logger.debug("upload file:" + filename);
						logger.debug("fize size:" + mFile.getSize());
						if (StringUtils.endsWithIgnoreCase(filename, ".json") && mFile.getSize() < maxSize) {

						} else if (StringUtils.endsWithIgnoreCase(filename, ".zip") && mFile.getSize() < maxSize) {
							zipInputStream = new ZipInputStream(mFile.getInputStream());
							Map<String, byte[]> zipMap = ZipUtils.getZipBytesMap(zipInputStream);
							if (zipMap != null && !zipMap.isEmpty()) {
								Set<Entry<String, byte[]>> entrySet2 = zipMap.entrySet();
								for (Entry<String, byte[]> entry2 : entrySet2) {
									String key = entry2.getKey();
									if (StringUtils.endsWithIgnoreCase(key, ".json")) {
										// byte[] value = entry2.getValue();
										try {
											// 3-根应用的编号

										} catch (Exception ex) {
											status = false;

											logger.error("error import json data ", ex);
										}
									}
								}
							}
						}
					}
				}
				status = true;
			} catch (Exception ex) {
				status = false;
				logger.error("error import data ", ex);
			}
		}
		return ResponseUtils.responseResult(status);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request) throws IOException {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SysApplicationQuery query = new SysApplicationQuery();
		Tools.populate(query, params);
		query.setDeleteFlag(0);

		int start = 0;
		int limit = 20;
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
			limit = 20;
		}

		JSONObject result = new JSONObject();
		int total = sysApplicationService.getSysApplicationCountByQueryCriteria(query);
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

			List<SysApplication> list = sysApplicationService.getSysApplicationsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (SysApplication sysApplication : list) {
					JSONObject rowJSON = sysApplication.toJsonObject();
					rowJSON.put("id", sysApplication.getId());
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

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("application.list");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/app/list", modelMap);
	}

	/**
	 * 提交增加信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/save")
	@ResponseBody
	public byte[] save(HttpServletRequest req) {
		long id = RequestUtils.getLong(req, "id");
		SysApplication app = null;
		if (id > 0) {
			app = sysApplicationService.findById(id);
		}
		Map<String, Object> params = RequestUtils.getParameterMap(req);
		logger.debug("params:" + params);
		if (app == null) {
			SysApplication bean = new SysApplication();
			try {
				Tools.populate(bean, params);
			} catch (Exception ex) {
			}
			bean.setName(ParamUtil.getParameter(req, "name"));
			bean.setCode(ParamUtil.getParameter(req, "code"));
			bean.setDesc(ParamUtil.getParameter(req, "desc"));
			bean.setUrl(ParamUtil.getParameter(req, "url"));
			bean.setImagePath(ParamUtil.getParameter(req, "imagePath"));
			bean.setLocked(ParamUtil.getIntParameter(req, "locked", 0));
			bean.setShowMenu(ParamUtil.getIntParameter(req, "showMenu", 0));
			bean.setShowType(req.getParameter("showType"));
			try {
				sysApplicationService.create(bean);
				return ResponseUtils.responseResult(true);
			} catch (Exception ex) {
				logger.error(ex);
			}
		} else {
			try {
				Tools.populate(app, params);
			} catch (Exception ex) {
			}
			app.setName(ParamUtil.getParameter(req, "name"));
			app.setCode(ParamUtil.getParameter(req, "code"));
			app.setDesc(ParamUtil.getParameter(req, "desc"));
			app.setUrl(ParamUtil.getParameter(req, "url"));
			app.setImagePath(ParamUtil.getParameter(req, "imagePath"));
			app.setShowMenu(ParamUtil.getIntParameter(req, "showMenu", 0));
			app.setShowType(req.getParameter("showType"));
			app.setUpdateBy(RequestUtils.getActorId(req));
			app.setLocked(ParamUtil.getIntParameter(req, "locked", 0));
		}
		try {
			sysApplicationService.update(app);
			TreeUpdateBean updateBean = new TreeUpdateBean();
			updateBean.updateTreeIds("default", "SYS_APPLICATION", null, "ID", "PARENTID", "TREEID", "LEVEL", null);
			return ResponseUtils.responseResult(true);
		} catch (Exception ex) {
			logger.error(ex);
		}

		return ResponseUtils.responseResult(false);
	}

	/**
	 * 提交增加信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveAdd")
	@ResponseBody
	public byte[] saveAdd(HttpServletRequest req) {
		SysApplication bean = new SysApplication();
		Map<String, Object> dataMap = RequestUtils.getParameterMap(req);
		try {
			Tools.populate(bean, dataMap);
		} catch (Exception ex) {
		}
		bean.setName(ParamUtil.getParameter(req, "name"));
		bean.setCode(ParamUtil.getParameter(req, "code"));
		bean.setDesc(ParamUtil.getParameter(req, "desc"));
		bean.setUrl(ParamUtil.getParameter(req, "url"));
		bean.setImagePath(ParamUtil.getParameter(req, "imagePath"));
		bean.setShowMenu(ParamUtil.getIntParameter(req, "showMenu", 0));
		bean.setShowType(req.getParameter("showType"));

		try {
			sysApplicationService.create(bean);
			return ResponseUtils.responseResult(true);
		} catch (Exception ex) {
			ex.printStackTrace();
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
	public byte[] saveModify(HttpServletRequest req) {
		long id = ParamUtil.getIntParameter(req, "id", 0);
		SysApplication bean = sysApplicationService.findById(id);
		if (bean != null) {
			Map<String, Object> dataMap = RequestUtils.getParameterMap(req);
			try {
				Tools.populate(bean, dataMap);
			} catch (Exception ex) {
			}
			bean.setName(ParamUtil.getParameter(req, "name"));
			bean.setCode(ParamUtil.getParameter(req, "code"));
			bean.setDesc(ParamUtil.getParameter(req, "desc"));
			bean.setUrl(ParamUtil.getParameter(req, "url"));
			bean.setSort(ParamUtil.getIntParameter(req, "sort", 0));
			bean.setImagePath(ParamUtil.getParameter(req, "imagePath"));
			bean.setShowMenu(ParamUtil.getIntParameter(req, "showMenu", 0));
			bean.setShowType(req.getParameter("showType"));
			bean.setUpdateBy(RequestUtils.getActorId(req));
			bean.setLocked(ParamUtil.getIntParameter(req, "locked", 0));
		}
		try {
			sysApplicationService.update(bean);
			return ResponseUtils.responseResult(true);
		} catch (Exception ex) {
			logger.error(ex);
		}
		return ResponseUtils.responseResult(false);
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
					t1.setTableName("SYS_APPLICATION");
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
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@javax.annotation.Resource
	public void setSysApplicationService(SysApplicationService sysApplicationService) {
		this.sysApplicationService = sysApplicationService;
	}

	/**
	 * 显示所有列表
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/showList")
	public ModelAndView showList(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		int parent = ParamUtil.getIntParameter(request, "parent", 0);
		int pageNo = ParamUtil.getIntParameter(request, "page_no", 1);
		int pageSize = ParamUtil.getIntParameter(request, "page_size", SysConstants.PAGE_SIZE);

		SysApplicationQuery query = new SysApplicationQuery();
		String rq = ParamUtil.getParameter(request, "_rq_", "");
		logger.debug("_rq_:" + rq);
		String nameLike_encode = ParamUtil.getParameter(request, "nameLike_encode", "");
		String codeLike_encode = ParamUtil.getParameter(request, "codeLike_encode", "");

		if ("1".equals(rq)) {
			logger.debug("-----------------------参数查询-----------------------");
			String nameLike = ParamUtil.getParameter(request, "nameLike", "");
			String codeLike = ParamUtil.getParameter(request, "codeLike", "");
			if (StringUtils.isNotEmpty(nameLike)) {
				query.setNameLike(nameLike);
				request.setAttribute("nameLike_encode", RequestUtils.encodeString(nameLike));
				request.setAttribute("nameLike", nameLike);
			} else {
				request.removeAttribute("nameLike");
				request.removeAttribute("nameLike_encode");
				request.setAttribute("nameLike", "");
			}
			if (StringUtils.isNotEmpty(codeLike)) {
				query.setCodeLike(codeLike);
				request.setAttribute("codeLike_encode", RequestUtils.encodeString(codeLike));
				request.setAttribute("codeLike", codeLike);
			} else {
				request.removeAttribute("codeLike");
				request.removeAttribute("codeLike_encode");
				request.setAttribute("codeLike", "");
			}
		} else {
			logger.debug("-----------------------链接查询-----------------------");
			if (StringUtils.isNotEmpty(nameLike_encode)) {
				String nameLike = RequestUtils.decodeString(nameLike_encode);
				query.setNameLike(nameLike);
				request.setAttribute("nameLike_encode", nameLike_encode);
				request.setAttribute("nameLike", nameLike);
			}
			if (StringUtils.isNotEmpty(codeLike_encode)) {
				String codeLike = RequestUtils.decodeString(codeLike_encode);
				query.setCodeLike(codeLike);
				request.setAttribute("codeLike_encode", codeLike_encode);
				request.setAttribute("codeLike", codeLike);
			}

		}

		query.setDeleteFlag(0);

		PageResult pager = null;

		if (parent > 0) {
			pager = sysApplicationService.getApplicationList(parent, pageNo, pageSize);
		} else {
			pager = sysApplicationService.getApplicationList(pageNo, pageSize, query);
		}
		request.setAttribute("pager", pager);

		String x_view = ViewProperties.getString("application.showList");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		// 显示列表页面
		return new ModelAndView("/sys/app/app_list", modelMap);
	}

	@RequestMapping("/showPermission")
	public ModelAndView showPermission(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String x_view = ViewProperties.getString("application.showPermission");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/app/permission_frame", modelMap);
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
		long parentId = RequestUtils.getLongParameter(request, "parentId", 3);
		SysApplicationQuery query = new SysApplicationQuery();
		query.parentId(parentId);
		query.locked(0);
		query.deleteFlag(0);
		List<SysApplication> apps = sysApplicationService.getSysApplicationsByQueryCriteria(0, 100, query);
		request.setAttribute("apps", apps);
		return new ModelAndView("/sys/app/showSort", modelMap);
	}

	@ResponseBody
	@RequestMapping("/treeJson")
	public byte[] treeJson(HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.debug("------------------------treeJson--------------------");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		RequestUtils.setRequestParameterToAttribute(request);
		long parentId = RequestUtils.getLong(request, "parentId", 3);
		List<Long> selected = new ArrayList<Long>();
		SysApplication sysApplication = sysApplicationService.findById(parentId);
		if (sysApplication != null) {

		}

		List<SysApplication> sysApplications = null;
		if (loginContext.isSystemAdministrator()) {
			sysApplications = sysApplicationService.getApplicationListWithChildren(parentId);
		} else {
			sysApplications = sysApplicationService.getSysApplicationByUserId(loginContext.getActorId());
		}

		List<TreeModel> treeModels = new ArrayList<TreeModel>();
		if (sysApplications != null && !sysApplications.isEmpty()) {
			for (SysApplication p : sysApplications) {
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

}