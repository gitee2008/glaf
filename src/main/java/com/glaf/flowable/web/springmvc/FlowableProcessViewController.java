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

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.flowable.engine.history.HistoricActivityInstance;

import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.glaf.flowable.model.ActivityCoordinates;
import com.glaf.flowable.model.ActivityInfo;
import com.glaf.flowable.model.ProcessInstanceInfo;
import com.glaf.flowable.service.FlowableProcessQueryService;
import com.glaf.flowable.service.FlowableProcessService;
import com.glaf.flowable.service.FlowableTaskQueryService;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.util.DateUtils;

@Controller("/flowable/view")
@RequestMapping("/flowable/view")
public class FlowableProcessViewController {
	protected static final Log logger = LogFactory.getLog(FlowableProcessViewController.class);

	protected FlowableProcessQueryService flowableProcessQueryService;

	protected FlowableProcessService flowableProcessService;

	protected FlowableTaskQueryService flowableTaskQueryService;

	@javax.annotation.Resource
	public void setFlowableProcessQueryService(FlowableProcessQueryService flowableProcessQueryService) {
		this.flowableProcessQueryService = flowableProcessQueryService;
	}

	@javax.annotation.Resource
	public void setFlowableProcessService(FlowableProcessService flowableProcessService) {
		this.flowableProcessService = flowableProcessService;
	}

	@javax.annotation.Resource
	public void setFlowableTaskQueryService(FlowableTaskQueryService flowableTaskQueryService) {
		this.flowableTaskQueryService = flowableTaskQueryService;
	}

	@RequestMapping
	@Transactional(readOnly = true)
	public String showImage(HttpServletRequest request, Model model) {
		String processInstanceId = request.getParameter("processInstanceId");
		ProcessDefinition processDefinition = null;
		if (StringUtils.isNotEmpty(processInstanceId)) {
			processDefinition = flowableProcessQueryService.getProcessDefinitionByProcessInstanceId(processInstanceId);
			model.addAttribute("processDefinition", processDefinition);
			if (processDefinition != null) {
				model.addAttribute("processDefinitionId", processDefinition.getId());
			}

			List<Task> tasks = flowableTaskQueryService.getAssigneeTasks(processInstanceId);
			List<HistoricTaskInstance> historyTasks = flowableTaskQueryService
					.getHistoricTaskInstances(processInstanceId);

			model.addAttribute("tasks", tasks);
			model.addAttribute("historyTasks", historyTasks);

			ProcessInstanceInfo processInstanceInfo = flowableProcessQueryService
					.getProcessInstanceInfo(processInstanceId);
			if (processInstanceInfo != null) {
				model.addAttribute("activeActivityInfos", processInstanceInfo.getActiveActivityInfos());
				model.addAttribute("processedActivityInfos", processInstanceInfo.getProcessedActivityInfos());
				model.addAttribute("processInstance", processInstanceInfo.getProcessInstance());
				model.addAttribute("historyProcessInstance", processInstanceInfo.getHistoricProcessInstance());

				StringBuffer positionBuffer = new StringBuffer();
				StringBuffer position = new StringBuffer();
				StringBuffer buffer = new StringBuffer();
				StringBuffer text = new StringBuffer();

				int id = 0;
				List<ActivityInfo> processedActivityInfos = processInstanceInfo.getProcessedActivityInfos();
				List<ActivityInfo> activeActivityInfos = processInstanceInfo.getActiveActivityInfos();
				ActivityCoordinates coordinates = null;
				HistoricActivityInstance activityInstance = null;
				if (processedActivityInfos != null && !processedActivityInfos.isEmpty()) {
					for (ActivityInfo info : processedActivityInfos) {
						coordinates = info.getCoordinates();
						activityInstance = info.getActivityInstance();
						if (activityInstance == null) {
							continue;
						}
						position.delete(0, position.length());
						text.delete(0, text.length());

						String elId = "_pai_" + (++id);
						String title = activityInstance.getActivityName();

						position.append("left:").append(coordinates.getX() - 2).append("px;");
						position.append("top:").append(coordinates.getY() - 2).append("px;");
						position.append("height:").append(coordinates.getHeight() - 2).append("px;");
						position.append("width:").append(coordinates.getWidth() - 2).append("px;");

						buffer.append("\n        <div class=\"tip processed\" id=\"").append(elId).append("\" style=\"")
								.append(position).append("\"></div>");

						Date startDate = activityInstance.getStartTime();
						Date endDate = activityInstance.getEndTime();
						buffer.append("\n        <script>$('").append(elId).append("').store('tip:title', '")
								.append(title).append('\'').append(").store('tip:text', '").append("<br/><b>开始时间:</b> ")
								.append(DateUtils.getDateTime(startDate)).append("<br/><b>结束时间:</b> ")
								.append(DateUtils.getDateTime(endDate));
						if (activityInstance.getAssignee() != null) {
							buffer.append("<br/><b>执行人:</b> ").append(activityInstance.getAssignee());
						}
						buffer.append("');</script>");
					}
				}

				if (activeActivityInfos != null && !activeActivityInfos.isEmpty()) {
					id = 0;
					for (ActivityInfo activityInfo : activeActivityInfos) {
						coordinates = activityInfo.getCoordinates();
						String elId = "_aai_" + (++id);
						positionBuffer.delete(0, positionBuffer.length());
						positionBuffer.append("left:").append(coordinates.getX() - 2).append("px;");
						positionBuffer.append("top:").append(coordinates.getY() - 2).append("px;");
						positionBuffer.append("height:").append(coordinates.getHeight() - 2).append("px;");
						positionBuffer.append("width:").append(coordinates.getWidth() - 2).append("px;");
						buffer.append("\n        <div class=\"tip active\" id=\"").append(elId).append("\" style=\"")
								.append(positionBuffer.toString()).append("\"></div>");
						activityInstance = activityInfo.getActivityInstance();
						if (activityInstance != null) {
							String title = activityInstance.getActivityName();
							Date startDate = activityInstance.getStartTime();
							buffer.append("\n        <script>$('").append(elId).append("').store('tip:title', '")
									.append(title).append('\'').append(").store('tip:text', '")
									.append("<br/><b>开始时间:</b> ").append(DateUtils.getDateTime(startDate));
							if (activityInstance.getAssignee() != null) {
								buffer.append("<br/><b>执行人:</b> ").append(activityInstance.getAssignee());
							}
							buffer.append("');</script>");
						}
					}
				}
 
				request.setAttribute("x_script", buffer.toString());
				// logger.debug(buffer.toString());
			}
		}

		String jx_view = request.getParameter("jx_view");

		if (StringUtils.isNotEmpty(jx_view)) {
			return jx_view;
		}

		String view = ViewProperties.getString("flowable.view");
		if (StringUtils.isNotEmpty(view)) {
			return view;
		}

		return "/flowable/view/view";
	}

}