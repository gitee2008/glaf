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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.glaf.flowable.model.ProcessInstanceInfo;
import com.glaf.flowable.model.TaskItem;
import com.glaf.flowable.service.FlowableProcessQueryService;
import com.glaf.flowable.service.FlowableProcessService;
import com.glaf.flowable.service.FlowableTaskQueryService;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.identity.User;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.Paging;
import com.glaf.core.util.RequestUtils;

@Controller("/flowable/task")
@RequestMapping("/flowable/task")
public class FlowableTaskController {

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

	/**
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping
	public String task(HttpServletRequest request, Model model) {
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

			HistoricProcessInstance historyProcessInstance = flowableProcessQueryService
					.getHistoricProcessInstance(processInstanceId);

			ProcessInstanceInfo processInstanceInfo = flowableProcessQueryService
					.getProcessInstanceInfo(processInstanceId);
			if (processInstanceInfo != null) {
				model.addAttribute("activeActivityInfos", processInstanceInfo.getActiveActivityInfos());
				model.addAttribute("processedActivityInfos", processInstanceInfo.getProcessedActivityInfos());
				model.addAttribute("processInstance", processInstanceInfo.getProcessInstance());
				model.addAttribute("historyProcessInstance", processInstanceInfo.getHistoricProcessInstance());
			}

			model.addAttribute("tasks", tasks);
			model.addAttribute("historyTasks", historyTasks);

			model.addAttribute("processInstanceId", processInstanceId);
			model.addAttribute("historyProcessInstance", historyProcessInstance);

			if (historyProcessInstance != null) {
				List<HistoricActivityInstance> historyActivityInstances = flowableProcessQueryService
						.getHistoricActivityInstances(historyProcessInstance.getId());
				model.addAttribute("historyActivityInstances", historyActivityInstances);
			}

			List<Task> allTasks = flowableTaskQueryService.getAllTasks(processInstanceId);

			List<Object> historyTaskItems = new java.util.ArrayList<Object>();

			List<?> row01 = flowableTaskQueryService.getHistoryTasks(processInstanceId);
			if (row01 != null && row01.size() > 0) {
				historyTaskItems.addAll(row01);
			}

			Iterator<?> iterator = historyTaskItems.iterator();
			while (iterator.hasNext()) {
				com.glaf.flowable.model.TaskItem taskItem = (com.glaf.flowable.model.TaskItem) iterator.next();
				String actorId = taskItem.getActorId();
				User user = IdentityFactory.getUser(actorId);
				if (user != null) {
					taskItem.setActorName(user.getName());
				}
			}
			model.addAttribute("historyTaskItems", historyTaskItems);

			List<TaskItem> taskItems = new java.util.ArrayList<TaskItem>();
			if (allTasks != null && !allTasks.isEmpty()) {
				for (Task task : allTasks) {
					if (task.getAssignee() != null) {
						TaskItem item = new TaskItem();
						User user = IdentityFactory.getUser(task.getAssignee());
						item.setActorId(task.getAssignee());
						if (user != null) {
							item.setActorName(user.getName());
						}
						item.setTaskDefinitionKey(task.getTaskDefinitionKey());
						item.setTaskName(task.getName());
						item.setStartTime(task.getCreateTime());
						item.setProcessInstanceId(processInstanceId);
						item.setTaskDescription(task.getDescription());
						item.setTaskInstanceId(task.getId());

						taskItems.add(item);
					} else {
						List<IdentityLink> list = flowableTaskQueryService.getTaskIdentityLinks(task.getId());
						if (list != null && list.size() > 0) {
							for (IdentityLink p : list) {
								TaskItem item = new TaskItem();
								item.setTaskDefinitionKey(task.getTaskDefinitionKey());
								item.setTaskName(task.getName());
								item.setStartTime(task.getCreateTime());
								item.setProcessInstanceId(processInstanceId);
								item.setTaskDescription(task.getDescription());
								item.setTaskInstanceId(task.getId());
								item.setIdentityLinkType(p.getType());
								if (p.getUserId() != null) {
									User userProfile = IdentityFactory.getUser(p.getUserId());
									item.setActorId(p.getUserId());
									if (userProfile != null) {
										item.setActorName(userProfile.getName());
									}
								}
								if (p.getGroupId() != null) {
									item.setGroupId(p.getGroupId());
								}
								taskItems.add(item);
							}
						}
					}
				}

				Iterator<?> iterator2 = taskItems.iterator();
				while (iterator2.hasNext()) {
					com.glaf.flowable.model.TaskItem taskItem = (com.glaf.flowable.model.TaskItem) iterator2.next();
					String actorId = taskItem.getActorId();
					User user = IdentityFactory.getUser(actorId);
					if (user != null) {
						taskItem.setActorName(user.getName());
					}
				}

				model.addAttribute("taskItems", taskItems);
			}
		}

		String jx_view = request.getParameter("jx_view");

		if (StringUtils.isNotEmpty(jx_view)) {
			return jx_view;
		}

		String view = ViewProperties.getString("flowable.task");
		if (StringUtils.isNotEmpty(view)) {
			return view;
		}

		return "/flowable/task/task";
	}

	/**
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/taskList")
	public String taskList(HttpServletRequest request, Model model) {
		String jx_view = request.getParameter("jx_view");

		if (StringUtils.isNotEmpty(jx_view)) {
			return jx_view;
		}

		String view = ViewProperties.getString("flowable.taskList");
		if (StringUtils.isNotEmpty(view)) {
			return view;
		}

		return "/flowable/task/taskList";
	}

	@RequestMapping("/taskListJson")
	@ResponseBody
	@Transactional(readOnly = true)
	public byte[] taskListJson(HttpServletRequest request, Model model) throws IOException {
		Map<String, Object> paramMap = RequestUtils.getParameterMap(request);
		int start = RequestUtils.getInt(request, "startIndex", 0);
		int limit = RequestUtils.getInt(request, "results", Paging.DEFAULT_PAGE_SIZE);

		long total = flowableTaskQueryService.getTaskCount(paramMap);

		List<Task> rows = flowableTaskQueryService.getTasks(start, limit, paramMap);

		List<Object> list = new java.util.ArrayList<Object>();

		if (rows != null && rows.size() > 0) {
			for (Task task : rows) {
				Map<String, Object> rowMap = new java.util.HashMap<String, Object>();
				rowMap.put("id", task.getId());
				rowMap.put("processInstanceId", task.getProcessInstanceId());
				rowMap.put("executionId", task.getExecutionId());
				rowMap.put("taskDefinitionKey", task.getTaskDefinitionKey());
				rowMap.put("name", task.getName());
				rowMap.put("description", task.getDescription());

				rowMap.put("priority", task.getPriority());
				rowMap.put("createTime", task.getCreateTime());
				rowMap.put("createDate", DateUtils.getDate(task.getCreateTime()));
				rowMap.put("createDateTime", DateUtils.getDateTime(task.getCreateTime()));

				if (task.getAssignee() != null) {
					rowMap.put("assignee", task.getAssignee());
					rowMap.put("actorId", task.getAssignee());
					rowMap.put("actorName", task.getAssignee());
					User userProfile = IdentityFactory.getUser(task.getAssignee());
					if (userProfile != null) {
						rowMap.put("actorName", userProfile.getName());
						// rowMap.put("deptName", userProfile.getDeptName());
					}
				} else {
					List<IdentityLink> identityLinks = flowableTaskQueryService.getTaskIdentityLinks(task.getId());
					if (identityLinks != null && identityLinks.size() > 0) {
						StringBuffer buffer = new StringBuffer();
						for (IdentityLink p : identityLinks) {
							if (p.getUserId() != null) {
								User userProfile = IdentityFactory.getUser(p.getUserId());
								if (userProfile != null && userProfile.getName() != null) {
									buffer.append(userProfile.getName()).append(", ");
								} else {
									buffer.append(p.getUserId()).append(", ");
								}
							} else if (p.getGroupId() != null) {
								buffer.append(p.getGroupId()).append("(group), ");
							}
						}
						buffer.delete(buffer.length() - 2, buffer.length());
						rowMap.put("actorName", buffer.toString());
					}
				}

				list.add(rowMap);
			}
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

}