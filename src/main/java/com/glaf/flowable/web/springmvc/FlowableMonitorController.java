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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.identitylink.service.IdentityLinkType;
import org.flowable.task.api.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.glaf.flowable.service.FlowableProcessQueryService;
import com.glaf.flowable.service.FlowableProcessService;
import com.glaf.flowable.service.FlowableTaskQueryService;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.StringTools;

@Controller("/flowable/monitor")
@RequestMapping("/flowable/monitor")
public class FlowableMonitorController {

	protected FlowableProcessService flowableProcessService;

	protected FlowableProcessQueryService flowableProcessQueryService;

	protected FlowableTaskQueryService flowableTaskQueryService;

	@RequestMapping("/addGroupIdentityLink")
	@ResponseBody
	public byte[] addGroupIdentityLink(HttpServletRequest request) {
		String taskId = request.getParameter("taskId");
		String groupIds = request.getParameter("groupIds");
		String identityLinkType = request.getParameter("identityLinkType");
		if (StringUtils.isEmpty(identityLinkType)) {
			identityLinkType = IdentityLinkType.CANDIDATE;
		}
		boolean success = false;
		if (StringUtils.isNotEmpty(groupIds) && StringUtils.isNotEmpty(taskId)) {
			Task task = flowableTaskQueryService.getTask(taskId);
			List<String> groups = StringTools.split(groupIds);
			if (task != null && groups != null && groups.size() > 0) {
				flowableProcessService.addGroupIdentityLink(taskId, groupIds, identityLinkType);
				success = true;
			}
		}
		return ResponseUtils.responseResult(success);
	}

	@RequestMapping("/addUserIdentityLink")
	@ResponseBody
	public byte[] addUserIdentityLink(HttpServletRequest request) {
		String taskId = request.getParameter("taskId");
		String actorIds = request.getParameter("actorIds");
		String identityLinkType = request.getParameter("identityLinkType");
		if (StringUtils.isEmpty(identityLinkType)) {
			identityLinkType = IdentityLinkType.CANDIDATE;
		}
		boolean success = false;
		if (StringUtils.isNotEmpty(actorIds) && StringUtils.isNotEmpty(taskId)) {
			Task task = flowableTaskQueryService.getTask(taskId);
			List<String> actors = StringTools.split(actorIds);
			if (task != null && actors != null && actors.size() > 0) {
				flowableProcessService.addUserIdentityLink(taskId, actorIds, identityLinkType);
				success = true;
			}
		}
		return ResponseUtils.responseResult(success);
	}

	@RequestMapping("/addVisitTasks")
	@ResponseBody
	public byte[] addVisitTasks(HttpServletRequest request) {
		String taskId = request.getParameter("taskId");
		String actorIds = request.getParameter("actorIds");
		boolean success = false;
		if (StringUtils.isNotEmpty(actorIds) && StringUtils.isNotEmpty(taskId)) {
			Task task = flowableTaskQueryService.getTask(taskId);
			List<String> actors = StringTools.split(actorIds);
			if (task != null && actors != null && actors.size() > 0) {

				success = true;
			}
		}
		return ResponseUtils.responseResult(success);
	}

	@RequestMapping("/claimTask")
	@ResponseBody
	public byte[] claimTask(HttpServletRequest request) {
		String taskId = request.getParameter("taskId");
		String actorId = request.getParameter("actorId");
		boolean success = false;
		if (StringUtils.isNotEmpty(actorId) && StringUtils.isNotEmpty(taskId)) {
			Task task = flowableTaskQueryService.getTask(taskId);
			if (task != null) {
				flowableProcessService.claimTask(taskId, actorId);
				success = true;
			}
		}
		return ResponseUtils.responseResult(success);
	}

	@RequestMapping("/resume")
	@ResponseBody
	public byte[] resume(HttpServletRequest request) {
		String processInstanceId = request.getParameter("processInstanceId");
		if (StringUtils.isNotEmpty(processInstanceId)) {
			ProcessInstance processInstance = flowableProcessQueryService.getProcessInstance(processInstanceId);
			if (processInstance != null) {

			}
		}
		return null;
	}

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

	@RequestMapping("/suspend")
	@ResponseBody
	public byte[] suspend(HttpServletRequest request) {
		String processInstanceId = request.getParameter("processInstanceId");
		if (StringUtils.isNotEmpty(processInstanceId)) {
			ProcessInstance processInstance = flowableProcessQueryService.getProcessInstance(processInstanceId);
			if (processInstance != null) {

			}
		}
		return null;
	}

}