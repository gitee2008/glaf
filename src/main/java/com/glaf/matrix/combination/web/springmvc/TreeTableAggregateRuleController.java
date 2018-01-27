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

package com.glaf.matrix.combination.web.springmvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.domain.Database;
import com.glaf.core.factory.TableFactory;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.util.PageResult;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.StringTools;
import com.glaf.core.util.Tools;

import com.glaf.matrix.combination.domain.TreeTableAggregate;
import com.glaf.matrix.combination.domain.TreeTableAggregateRule;
import com.glaf.matrix.combination.service.TreeTableAggregateService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/sys/treeTableAggregateRule")
@RequestMapping("/sys/treeTableAggregateRule")
public class TreeTableAggregateRuleController {
	protected static final Log logger = LogFactory.getLog(TreeTableAggregateRuleController.class);

	protected IDatabaseService databaseService;

	protected TreeTableAggregateService treeTableAggregateService;

	public TreeTableAggregateRuleController() {

	}

	@ResponseBody
	@RequestMapping("/delete")
	public byte[] delete(HttpServletRequest request, ModelMap modelMap) {
		Long id = RequestUtils.getLong(request, "id");
		treeTableAggregateService.deleteRuleById(id);
		return ResponseUtils.responseResult(true);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		TreeTableAggregate treeTableAggregate = null;
		TreeTableAggregateRule treeTableAggregateRule = treeTableAggregateService
				.getTreeTableAggregateRule(RequestUtils.getLong(request, "id"));
		if (treeTableAggregateRule != null) {
			request.setAttribute("treeTableAggregateRule", treeTableAggregateRule);
			request.setAttribute("aggregateId", treeTableAggregateRule.getAggregateId());
			treeTableAggregate = treeTableAggregateService
					.getTreeTableAggregate(treeTableAggregateRule.getAggregateId());
		} else {
			long aggregateId = RequestUtils.getLong(request, "aggregateId");
			if (aggregateId > 0) {
				treeTableAggregate = treeTableAggregateService.getTreeTableAggregate(aggregateId);
			}
		}
		logger.debug("treeTableAggregate:" + treeTableAggregate);
		if (treeTableAggregate != null && treeTableAggregate.getTargetTableName() != null) {
			List<TreeTableAggregateRule> rules = treeTableAggregateService
					.getTreeTableAggregateRulesByTableName(treeTableAggregate.getTargetTableName());
			List<String> names = new ArrayList<String>();
			for (TreeTableAggregateRule rule : rules) {
				if (rule.getTargetColumnName() != null) {
					rule.setTargetColumnName(rule.getTargetColumnName().toLowerCase());
					names.add(rule.getTargetColumnName().toLowerCase());
				}
			}
			List<Long> databaseIds = StringTools.splitToLong(treeTableAggregate.getDatabaseIds(), ",");
			logger.debug("databaseIds:" + databaseIds);
			for (Long databaseId : databaseIds) {
				Database targetDatabase = databaseService.getDatabaseById(databaseId);
				List<ColumnDefinition> columns = TableFactory.getColumnDefinitions(targetDatabase.getName(),
						treeTableAggregate.getSourceTableName());
				if (columns != null && columns.size() > 0) {
					request.setAttribute("columns", columns);
					// logger.debug("columns:" + columns);
					break;
				}
			}
			request.setAttribute("rules", rules);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("treeTableAggregateRule.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/matrix/treeTableAggregate/rule_edit", modelMap);
	}

	@ResponseBody
	@RequestMapping("/json")
	public byte[] json(HttpServletRequest request) throws IOException {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		long aggregateId = ParamUtils.getLong(params, "aggregateId");

		String gridType = ParamUtils.getString(params, "gridType");
		if (gridType == null) {
			gridType = "kendoui";
		}
		int start = 0;
		int limit = PageResult.DEFAULT_PAGE_SIZE;

		int pageNo = ParamUtils.getInt(params, "page");
		limit = ParamUtils.getInt(params, "rows");
		start = (pageNo - 1) * limit;

		if (start < 0) {
			start = 0;
		}

		if (limit <= 0) {
			limit = Paging.DEFAULT_PAGE_SIZE;
		}

		JSONObject result = new JSONObject();
		TreeTableAggregate treeTableAggregate = treeTableAggregateService.getTreeTableAggregate(aggregateId);

		if (treeTableAggregate != null && treeTableAggregate.getRules() != null
				&& !treeTableAggregate.getRules().isEmpty()) {
			int total = treeTableAggregate.getRules().size();
			result.put("total", total);
			result.put("totalCount", total);
			result.put("totalRecords", total);
			result.put("start", start);
			result.put("startIndex", start);
			result.put("limit", limit);
			result.put("pageSize", limit);

			List<TreeTableAggregateRule> list = treeTableAggregate.getRules();

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (TreeTableAggregateRule r : list) {
					JSONObject rowJSON = r.toJsonObject();
					rowJSON.put("id", r.getId());
					rowJSON.put("ruleId", r.getId());
					rowJSON.put("startIndex", ++start);

					if (StringUtils.equals(r.getTargetColumnType(), "Integer")) {
						rowJSON.put("typeName", "整数型");
					} else if (StringUtils.equals(r.getTargetColumnType(), "Long")) {
						rowJSON.put("typeName", "长整数型");
					} else if (StringUtils.equals(r.getTargetColumnType(), "Double")) {
						rowJSON.put("typeName", "数值型");
					} else {
						rowJSON.put("typeName", "字符串型");
					}
					rowsJSON.add(rowJSON);
				}
			}
		} else {
			JSONArray rowsJSON = new JSONArray();
			result.put("rows", rowsJSON);
			result.put("total", 0);
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

		return new ModelAndView("/matrix/treeTableAggregate/rule_list", modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveRule")
	public byte[] saveRule(HttpServletRequest request) {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		TreeTableAggregateRule treeTableAggregateRule = new TreeTableAggregateRule();
		try {
			Tools.populate(treeTableAggregateRule, params);
			treeTableAggregateRule.setAggregateId(RequestUtils.getLong(request, "aggregateId"));
			treeTableAggregateRule.setAggregateType(request.getParameter("aggregateType"));
			treeTableAggregateRule.setName(request.getParameter("name"));
			treeTableAggregateRule.setTitle(request.getParameter("title"));
			treeTableAggregateRule.setSourceColumnName(request.getParameter("sourceColumnName"));
			treeTableAggregateRule.setSourceColumnTitle(request.getParameter("sourceColumnTitle"));
			treeTableAggregateRule.setTargetColumnName(request.getParameter("targetColumnName"));
			treeTableAggregateRule.setTargetColumnTitle(request.getParameter("targetColumnTitle"));
			treeTableAggregateRule.setTargetColumnType(request.getParameter("targetColumnType"));
			treeTableAggregateRule.setLocked(RequestUtils.getInt(request, "locked"));
			this.treeTableAggregateService.saveRule(treeTableAggregateRule);

			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource
	public void setDatabaseService(IDatabaseService databaseService) {
		this.databaseService = databaseService;
	}

	@javax.annotation.Resource
	public void setTreeTableAggregateService(TreeTableAggregateService treeTableAggregateService) {
		this.treeTableAggregateService = treeTableAggregateService;
	}

}
