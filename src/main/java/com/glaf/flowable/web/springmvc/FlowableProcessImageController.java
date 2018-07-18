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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.flowable.engine.repository.ProcessDefinition;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.glaf.core.util.IOUtils;
import com.glaf.flowable.service.FlowableDeployQueryService;
import com.glaf.flowable.service.FlowableDeployService;
import com.glaf.flowable.service.FlowableProcessQueryService;
import com.glaf.flowable.service.FlowableTaskQueryService;
import com.glaf.flowable.util.ProcessUtils;

@Controller("/flowable/image")
@RequestMapping("/flowable/image")
public class FlowableProcessImageController {
	protected final static Log logger = LogFactory.getLog(FlowableProcessImageController.class);

	protected FlowableDeployService flowableDeployService;

	protected FlowableDeployQueryService flowableDeployQueryService;

	protected FlowableProcessQueryService flowableProcessQueryService;

	protected FlowableTaskQueryService flowableTaskQueryService;

	@RequestMapping("/image")
	@ResponseBody
	public byte[] image(@RequestParam("deploymentId") String deploymentId) throws IOException {
		ProcessDefinition processDefinition = null;
		logger.debug("deploymentId:" + deploymentId);
		if (StringUtils.isNotEmpty(deploymentId)) {
			processDefinition = flowableProcessQueryService.getProcessDefinitionByDeploymentId(deploymentId);
		}
		if (processDefinition != null) {
			byte[] bytes = null;
			try {
				bytes = ProcessUtils.getImage(processDefinition.getId());
			} catch (Exception e) {
			}
			return bytes;
		}

		return null;
	}

	@javax.annotation.Resource
	public void setFlowableDeployQueryService(FlowableDeployQueryService flowableDeployQueryService) {
		this.flowableDeployQueryService = flowableDeployQueryService;
	}

	@javax.annotation.Resource
	public void setFlowableDeployService(FlowableDeployService flowableDeployService) {
		this.flowableDeployService = flowableDeployService;
	}

	@javax.annotation.Resource
	public void setFlowableProcessQueryService(FlowableProcessQueryService flowableProcessQueryService) {
		this.flowableProcessQueryService = flowableProcessQueryService;
	}

	@javax.annotation.Resource
	public void setFlowableTaskQueryService(FlowableTaskQueryService flowableTaskQueryService) {
		this.flowableTaskQueryService = flowableTaskQueryService;
	}

	@RequestMapping
	@ResponseBody
	public void showImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String processDefinitionId = request.getParameter("processDefinitionId");
		String processInstanceId = request.getParameter("processInstanceId");
		ProcessDefinition processDefinition = null;
		logger.debug("processDefinitionId:" + processDefinitionId);
		if (StringUtils.isNotEmpty(processDefinitionId)) {
			processDefinition = flowableProcessQueryService.getProcessDefinition(processDefinitionId);
		} else if (StringUtils.isNotEmpty(processInstanceId)) {
			processDefinition = flowableProcessQueryService.getProcessDefinitionByProcessInstanceId(processInstanceId);
		}
		if (processDefinition != null) {
			byte[] bytes = null;
			ByteArrayInputStream bais = null;
			BufferedInputStream bis = null;
			OutputStream outputStream = null;
			try {
				bytes = ProcessUtils.getImage(processDefinition.getId());
				bais = new ByteArrayInputStream(bytes);
				bis = new BufferedInputStream(bais);
				response.setContentType("image/jpg");
				outputStream = response.getOutputStream();
				IOUtils.write(bis, outputStream);
				outputStream.flush();
				outputStream.close();
			} catch (Exception ex) {
			} finally {
				IOUtils.closeStream(bis);
				IOUtils.closeStream(bais);
				IOUtils.closeStream(outputStream);
			}
		}
	}

	@RequestMapping("/viewImage")
	public ModelAndView viewImage(HttpServletRequest request) throws IOException {
		String processDefinitionId = request.getParameter("processDefinitionId");
		String processInstanceId = request.getParameter("processInstanceId");
		ProcessDefinition processDefinition = null;
		logger.debug("processDefinitionId:" + processDefinitionId);
		if (StringUtils.isNotEmpty(processDefinitionId)) {
			processDefinition = flowableProcessQueryService.getProcessDefinition(processDefinitionId);
		} else if (StringUtils.isNotEmpty(processInstanceId)) {
			processDefinition = flowableProcessQueryService.getProcessDefinitionByProcessInstanceId(processInstanceId);
		}
		if (processDefinition != null) {
			request.setAttribute("processDefinition", processDefinition);
			request.setAttribute("processDefinitionId", processDefinition.getId());
		}

		return new ModelAndView("/flowable/view/viewImage");
	}
}