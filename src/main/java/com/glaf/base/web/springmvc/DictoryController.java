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

package com.glaf.base.web.springmvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.base.modules.BaseDataManager;
import com.glaf.base.modules.sys.SysConstants;
import com.glaf.base.modules.sys.model.Dictory;
import com.glaf.base.modules.sys.model.SysTree;
import com.glaf.base.modules.sys.query.DictoryQuery;
import com.glaf.base.modules.sys.service.DictoryService;
import com.glaf.base.modules.sys.service.SysTreeService;
import com.glaf.base.modules.sys.util.DictoryJsonFactory;
import com.glaf.base.utils.ParamUtil;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.domain.DictoryDefinition;
import com.glaf.core.service.DictoryDefinitionService;
import com.glaf.core.util.PageResult;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;

@Controller("/dictory")
@RequestMapping("/dictory")
public class DictoryController {
	private static final Log logger = LogFactory.getLog(DictoryController.class);

	protected DictoryDefinitionService dictoryDefinitionService;

	protected DictoryService dictoryService;

	protected SysTreeService sysTreeService;

	/**
	 * 提交删除
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/batchDelete")
	@ResponseBody
	public byte[] batchDelete(HttpServletRequest request) {
		try {
			long[] ids = ParamUtil.getLongParameterValues(request, "id");
			dictoryService.deleteAll(ids);
			return ResponseUtils.responseResult(true);
		} catch (Exception ex) {
			logger.error(ex);
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request) throws IOException {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		DictoryQuery query = new DictoryQuery();
		Tools.populate(query, params);

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
		int total = dictoryService.getDictoryCountByQueryCriteria(query);
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

			List<Dictory> list = dictoryService.getDictorysByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (Dictory dictory : list) {
					JSONObject rowJSON = dictory.toJsonObject();
					rowJSON.put("id", dictory.getId());
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
	
	@RequestMapping("/jsonArray")
	@ResponseBody
	public byte[] jsonArray(HttpServletRequest request) throws IOException {
		String nodeCode = request.getParameter("nodeCode");
		List<Dictory> dicts = dictoryService.getDictoryList(nodeCode);
		JSONArray array = DictoryJsonFactory.listToArray(dicts);
		return array.toString().getBytes("UTF-8");
	}

	@RequestMapping
	public ModelAndView list(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String x_view = ViewProperties.getString("base_dictory.list");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/base/dictory/list", modelMap);
	}

	/**
	 * 显示增加字典页面
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/prepareAdd")
	public ModelAndView prepareAdd(HttpServletRequest request, ModelMap modelMap) {
		// 显示列表页面
		RequestUtils.setRequestParameterToAttribute(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		Long nodeId = ParamUtils.getLong(params, "parent");
		if (nodeId > 0) {
			List<DictoryDefinition> list = dictoryDefinitionService.getDictoryDefinitions(nodeId, "SYS_DICTORY");
			if (list != null && !list.isEmpty()) {
				Collections.sort(list);
				request.setAttribute("list", list);
			}
		}

		String x_view = ViewProperties.getString("base_dictory.prepareAdd");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/base/dictory/dictory_add", modelMap);
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
		long id = ParamUtil.getIntParameter(request, "id", 0);
		Dictory bean = dictoryService.find(id);
		request.setAttribute("bean", bean);

		long nodeId = ParamUtil.getLongParameter(request, "parent", 0);
		if (nodeId > 0) {
			List<DictoryDefinition> list = dictoryDefinitionService.getDictoryDefinitions(nodeId, "SYS_DICTORY");
			if (list != null && !list.isEmpty()) {
				if (bean != null) {
					Map<String, Object> dataMap = Tools.getDataMap(bean);
					for (DictoryDefinition d : list) {
						Object value = dataMap.get(d.getName());
						d.setValue(value);
					}
				}
				Collections.sort(list);
				request.setAttribute("list", list);
			}
		}

		SysTree parent = sysTreeService.getSysTreeByCode(SysConstants.TREE_DICTORY);
		List<SysTree> list = new ArrayList<SysTree>();
		parent.setLevel(0);
		list.add(parent);
		sysTreeService.getSysTree(list, parent.getId(), 1);
		request.setAttribute("parent", list);

		String x_view = ViewProperties.getString("base_dictory.prepareModify");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/base/dictory/dictory_modify", modelMap);
	}

	/**
	 * 提交增加字典信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveAdd")
	@ResponseBody
	public byte[] saveAdd(HttpServletRequest request) {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug("params:" + params);
		Dictory bean = new Dictory();
		try {
			Tools.populate(bean, params);
			dictoryService.create(bean);
			bean.setCreateBy(RequestUtils.getActorId(request));
			return ResponseUtils.responseResult(true);
		} catch (Exception ex) {
			logger.error(ex);
		}
		return ResponseUtils.responseResult(false);
	}

	/**
	 * 提交修改字典信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveModify")
	@ResponseBody
	public byte[] saveModify(HttpServletRequest request) {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		long id = ParamUtil.getIntParameter(request, "id", 0);
		logger.debug("params:" + params);
		try {
			Dictory bean = dictoryService.find(id);
			Tools.populate(bean, params);
			bean.setUpdateBy(RequestUtils.getActorId(request));
			if (dictoryService.update(bean)) {// 保存成功
				BaseDataManager.getInstance().loadDictInfo();
				return ResponseUtils.responseResult(true);
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
		return ResponseUtils.responseResult(false);
	}

	@javax.annotation.Resource
	public void setDictoryDefinitionService(DictoryDefinitionService dictoryDefinitionService) {
		this.dictoryDefinitionService = dictoryDefinitionService;
	}

	@javax.annotation.Resource
	public void setDictoryService(DictoryService dictoryService) {
		this.dictoryService = dictoryService;
	}

	@javax.annotation.Resource
	public void setSysTreeService(SysTreeService sysTreeService) {
		this.sysTreeService = sysTreeService;
	}

	/**
	 * 显示字典数据
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/showDictData")
	public ModelAndView showDictData(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		ModelAndView view = null;
		String code = ParamUtil.getParameter(request, "code");
		Iterator<?> iter = null;
		long parent = ParamUtil.getLongParameter(request, "parent", -1);
		if (!(parent == -1)) {

		} else {
			iter = BaseDataManager.getInstance().getList(code);
			view = new ModelAndView("/base/dictory/dictory_select", modelMap);
		}
		request.setAttribute("list", iter);

		// 显示列表页面
		return view;
	}

	/**
	 * 显示框架页面
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/showFrame")
	public ModelAndView showFrame(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		SysTree bean = sysTreeService.getSysTreeByCode(SysConstants.TREE_DICTORY);
		request.setAttribute("parent", bean.getId() + "");

		String x_view = ViewProperties.getString("base_dictory.showFrame");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/base/dictory/dictory_frame", modelMap);
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
		int pageSize = ParamUtil.getIntParameter(request, "page_size", 10);
		PageResult pager = dictoryService.getDictoryList(parent, pageNo, pageSize);
		request.setAttribute("pager", pager);

		String x_view = ViewProperties.getString("base_dictory.showList");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		// 显示列表页面
		return new ModelAndView("/base/dictory/dictory_list", modelMap);
	}

	@ResponseBody
	@RequestMapping("/sort")
	public byte[] sort(@RequestParam(value = "parent") int parent, @RequestParam(value = "id") int id,
			@RequestParam(value = "operate") int operate) {
		logger.debug("parent:" + parent + "; id:" + id + "; operate:" + operate);
		try {
			Dictory bean = dictoryService.find(id);
			dictoryService.sort(parent, bean, operate);
			return ResponseUtils.responseResult(true);
		} catch (Exception ex) {
		}
		return ResponseUtils.responseResult(false);
	}
}