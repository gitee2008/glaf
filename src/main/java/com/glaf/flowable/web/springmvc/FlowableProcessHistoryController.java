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

package com.glaf.flowable.web.springmvc;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.glaf.flowable.service.FlowableDeployService;
import com.glaf.flowable.service.FlowableProcessQueryService;
import com.glaf.flowable.service.FlowableProcessService;
import com.glaf.flowable.service.FlowableTaskQueryService;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.Paging;
import com.glaf.core.util.RequestUtils;

@Controller("/flowable/history")
@RequestMapping("/flowable/history")
public class FlowableProcessHistoryController {

	protected FlowableDeployService flowableDeployService;

	protected FlowableProcessService flowableProcessService;

	protected FlowableProcessQueryService flowableProcessQueryService;

	protected FlowableTaskQueryService flowableTaskQueryService;

	@RequestMapping("/historyProcessInstanceJson")
	@ResponseBody
	public byte[] historyProcessInstanceJson(HttpServletRequest request,
			Model model) throws IOException {
		Map<String, Object> paramMap = RequestUtils.getParameterMap(request);
		int start = RequestUtils.getInt(request, "startIndex", 0);
		int limit = RequestUtils.getInt(request, "results",
				Paging.DEFAULT_PAGE_SIZE);

		String deploymentId = request.getParameter("deploymentId");
		String processDefinitionId = request
				.getParameter("processDefinitionId");
		ProcessDefinition processDefinition = null;
		if (StringUtils.isNotEmpty(deploymentId)) {
			processDefinition = flowableProcessQueryService
					.getProcessDefinitionByDeploymentId(deploymentId);
		} else if (StringUtils.isNotEmpty(processDefinitionId)) {
			processDefinition = flowableProcessQueryService
					.getProcessDefinition(processDefinitionId);
		}
		if (processDefinition != null) {
			model.addAttribute("processDefinition", processDefinition);
			paramMap.put("deploymentId", processDefinition.getDeploymentId());
			paramMap.put("processDefinitionId", processDefinition.getId());
		}

		long total = flowableProcessQueryService
				.getHistoricProcessInstanceCount(paramMap);
		List<HistoricProcessInstance> rows = flowableProcessQueryService
				.getHistoricProcessInstances(start, limit, paramMap);

		List<Object> list = new java.util.ArrayList<Object>();

		for (HistoricProcessInstance processInstance : rows) {
			Map<String, Object> rowMap = new java.util.HashMap<String, Object>();
			rowMap.put("id", processInstance.getId());
			rowMap.put("processInstanceId", processInstance.getId());
			rowMap.put("processDefinitionId",
					processInstance.getProcessDefinitionId());
			rowMap.put("businessKey", processInstance.getBusinessKey());
			rowMap.put("startTime", processInstance.getStartTime());
			rowMap.put("endTime", processInstance.getEndTime());
			rowMap.put("startUserId", processInstance.getStartUserId());
			rowMap.put("startDate",
					DateUtils.getDate(processInstance.getStartTime()));
			rowMap.put("endDate",
					DateUtils.getDate(processInstance.getEndTime()));
			rowMap.put("startDateTime",
					DateUtils.getDateTime(processInstance.getStartTime()));
			rowMap.put("endDateTime",
					DateUtils.getDateTime(processInstance.getEndTime()));
			rowMap.put("durationInMillis",
					processInstance.getDurationInMillis());
			rowMap.put("processDefinitionId",
					processInstance.getProcessDefinitionId());
			list.add(rowMap);
		}

		Map<String, Object> pageInfo = new java.util.HashMap<String, Object>();
		// 当前页数设置
		pageInfo.put("startIndex", start);

		// 每页记录数
		pageInfo.put("pageSize", limit);

		// 总数据量设置
		pageInfo.put("totalRecords", total);
		pageInfo.put("records", list);

		JSONObject object = new JSONObject(pageInfo);

		return object.toString().getBytes("UTF-8");
	}

	@RequestMapping("/historyProcessInstances")
	public String historyProcessInstances(HttpServletRequest request,
			Model model) {
		Map<String, Object> paramMap = RequestUtils.getParameterMap(request);

		Set<Entry<String, Object>> entrySet = paramMap.entrySet();
		for (Entry<String, Object> entry : entrySet) {
			String name = entry.getKey();
			Object value = entry.getValue();
			if (value != null) {
				model.addAttribute(name, value);
			}
		}

		String jx_view = request.getParameter("jx_view");

		if (StringUtils.isNotEmpty(jx_view)) {
			return jx_view;
		}

		String view = ViewProperties
				.getString("flowable.history.historyProcessInstances");
		if (StringUtils.isNotEmpty(view)) {
			return view;
		}

		return "/flowable/history/historyProcessInstances";
	}

	@javax.annotation.Resource
	public void setFlowableDeployService(
			FlowableDeployService flowableDeployService) {
		this.flowableDeployService = flowableDeployService;
	}

	@javax.annotation.Resource
	public void setFlowableProcessQueryService(
			FlowableProcessQueryService flowableProcessQueryService) {
		this.flowableProcessQueryService = flowableProcessQueryService;
	}

	@javax.annotation.Resource
	public void setFlowableProcessService(
			FlowableProcessService flowableProcessService) {
		this.flowableProcessService = flowableProcessService;
	}

	@javax.annotation.Resource
	public void setFlowableTaskQueryService(
			FlowableTaskQueryService flowableTaskQueryService) {
		this.flowableTaskQueryService = flowableTaskQueryService;
	}

}