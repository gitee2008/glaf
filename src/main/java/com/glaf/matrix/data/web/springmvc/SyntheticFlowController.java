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

import java.util.ArrayList;
import java.util.List;
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
import com.glaf.core.config.ViewProperties;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.matrix.data.domain.DataItemDefinition;
import com.glaf.matrix.data.domain.SyntheticFlow;
import com.glaf.matrix.data.query.SyntheticFlowQuery;
import com.glaf.matrix.data.service.DataItemDefinitionService;
import com.glaf.matrix.data.service.SyntheticFlowService;
import com.glaf.matrix.data.util.DataItemFactory;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/sys/syntheticFlow")
@RequestMapping("/sys/syntheticFlow")
public class SyntheticFlowController {
	protected static final Log logger = LogFactory.getLog(SyntheticFlowController.class);

	protected DataItemDefinitionService dataItemDefinitionService;

	protected SyntheticFlowService syntheticFlowService;

	public SyntheticFlowController() {

	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		JSONArray array = null;
		String currentType = null;
		List<SyntheticFlow> rows = null;
		String code = request.getParameter("code");
		String currentStep = request.getParameter("currentStep");
		if (StringUtils.isNotEmpty(currentStep) && StringUtils.isNotEmpty(code)) {
			array = DataItemFactory.getJSONArray(code);
			request.setAttribute("array", array);
			DataItemDefinition item = dataItemDefinitionService.getDataItemDefinitionByCode(code);
			if (item != null) {
				request.setAttribute("item", item);
				currentType = item.getType();
				request.setAttribute("currentType", currentType);
				SyntheticFlowQuery query = new SyntheticFlowQuery();
				query.currentStep(currentStep);
				query.currentType(currentType);
				rows = syntheticFlowService.list(query);
				request.setAttribute("rows", rows);
			}
		}

		List<DataItemDefinition> definitions = dataItemDefinitionService.getDataItemDefinitions("synthetic");
		request.setAttribute("definitions", definitions);

		StringBuffer bufferx = new StringBuffer();
		StringBuffer bufferx2 = new StringBuffer();
		StringBuffer buffery = new StringBuffer();
		StringBuffer buffery2 = new StringBuffer();

		if (rows != null && array != null && array.size() > 0) {
			boolean includePrev = false;
			boolean inculdeNext = false;
			String key = null;
			for (int i = 0; i < array.size(); i++) {
				JSONObject json = array.getJSONObject(i);
				key = json.getString("key");
				includePrev = false;
				inculdeNext = false;
				if (rows != null && rows.size() > 0) {
					for (int j = 0; j < rows.size(); j++) {
						SyntheticFlow flow = (SyntheticFlow) rows.get(j);
						if (StringUtils.equals(key, flow.getPreviousStep())
								&& StringUtils.equals(currentType, flow.getPreviousType())) {
							bufferx2.append("\n<option value=\"").append(json.getString("key")).append("\">")
									.append(json.getString("value")).append(" [").append(json.getString("key"))
									.append("]</option>");
							request.setAttribute("bufferx2", bufferx2.toString());
							includePrev = true;
						}
						if (StringUtils.equals(key, flow.getNextStep())
								&& StringUtils.equals(currentType, flow.getNextType())) {
							buffery2.append("\n<option value=\"").append(json.getString("key")).append("\">")
									.append(json.getString("value")).append(" [").append(json.getString("key"))
									.append("]</option>");
							request.setAttribute("buffery2", buffery2.toString());
							inculdeNext = true;
						}
					}
				}
				if (!includePrev) {
					bufferx.append("\n<option value=\"").append(json.getString("key")).append("\">")
							.append(json.getString("value")).append(" [").append(json.getString("key"))
							.append("]</option>");
					request.setAttribute("bufferx", bufferx.toString());
				}
				if (!inculdeNext) {
					buffery.append("\n<option value=\"").append(json.getString("key")).append("\">")
							.append(json.getString("value")).append(" [").append(json.getString("key"))
							.append("]</option>");
					request.setAttribute("buffery", buffery.toString());
				}
			}
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("syntheticFlow.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/matrix/syntheticFlow/edit", modelMap);
	}

	@ResponseBody
	@RequestMapping("/save")
	public byte[] save(HttpServletRequest request) {
		RequestUtils.setRequestParameterToAttribute(request);
		String actorId = RequestUtils.getActorId(request);
		String currentType = null;
		String code = request.getParameter("code");
		String currentStep = request.getParameter("currentStep");
		String previousType = request.getParameter("previousType");
		String nextType = request.getParameter("nextType");
		String previousObjectIds = request.getParameter("previousObjectIds");
		String nextObjectIds = request.getParameter("nextObjectIds");
		if (StringUtils.isNotEmpty(currentStep) && StringUtils.isNotEmpty(code)) {
			DataItemDefinition current = dataItemDefinitionService.getDataItemDefinitionByCode(code);
			if (current != null) {
				currentType = current.getType();
				int sort = 0;
				List<SyntheticFlow> flows = new ArrayList<SyntheticFlow>();
				if (StringUtils.isNotEmpty(previousObjectIds)) {
					StringTokenizer token = new StringTokenizer(previousObjectIds, ",");
					while (token.hasMoreTokens()) {
						String str = token.nextToken();
						sort++;
						SyntheticFlow flow = new SyntheticFlow();
						flow.setPreviousStep(str);
						flow.setPreviousType(previousType);
						flow.setSort(sort);
						flow.setCreateBy(actorId);
						flows.add(flow);
					}
				}

				if (StringUtils.isNotEmpty(nextObjectIds)) {
					sort = 0;
					StringTokenizer token = new StringTokenizer(nextObjectIds, ",");
					while (token.hasMoreTokens()) {
						String str = token.nextToken();
						sort++;
						SyntheticFlow flow = new SyntheticFlow();
						flow.setNextStep(str);
						flow.setNextType(nextType);
						flow.setSort(sort);
						flow.setCreateBy(actorId);
						flows.add(flow);
					}
				}

				syntheticFlowService.saveAll(currentStep, currentType, flows);
				return ResponseUtils.responseResult(true);
			}
		}

		return ResponseUtils.responseResult(false);
	}

	@javax.annotation.Resource
	public void setDataItemDefinitionService(DataItemDefinitionService dataItemDefinitionService) {
		this.dataItemDefinitionService = dataItemDefinitionService;
	}

	@javax.annotation.Resource
	public void setSyntheticFlowService(SyntheticFlowService syntheticFlowService) {
		this.syntheticFlowService = syntheticFlowService;
	}

}
