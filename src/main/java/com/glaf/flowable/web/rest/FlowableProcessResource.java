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

package com.glaf.flowable.web.rest;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import com.glaf.flowable.model.ActivityCoordinates;
import com.glaf.flowable.model.ActivityInfo;
import com.glaf.flowable.model.ProcessInstanceInfo;
import com.glaf.flowable.service.FlowableDeployQueryService;
import com.glaf.flowable.service.FlowableProcessQueryService;
import com.glaf.flowable.service.FlowableProcessService;
import com.glaf.flowable.service.FlowableTaskQueryService;
import com.glaf.flowable.util.ProcessUtils;
 
import com.glaf.core.config.ViewProperties;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.FileUtils;
import com.glaf.core.util.QueryUtils;

@Controller("/rs/flowable/process")
@Path("/rs/flowable/process")
public class FlowableProcessResource {
	private static ConcurrentMap<String, Object> cache = new ConcurrentHashMap<String, Object>();

	protected static final Log logger = LogFactory
			.getLog(FlowableProcessResource.class);

	protected FlowableDeployQueryService flowableDeployQueryService;

	protected FlowableProcessQueryService flowableProcessQueryService;

	protected FlowableProcessService flowableProcessService;

	protected FlowableTaskQueryService flowableTaskQueryService;

	@GET
	@Path("image/{processInstanceId}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM })
	@ResponseBody
	public byte[] image(@PathParam("processInstanceId") String processInstanceId) {
		ProcessDefinition processDefinition = null;
		if (StringUtils.isNotEmpty(processInstanceId)) {
			processDefinition = flowableProcessQueryService
					.getProcessDefinitionByProcessInstanceId(processInstanceId);
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
	public void setFlowableDeployQueryService(
			FlowableDeployQueryService flowableDeployQueryService) {
		this.flowableDeployQueryService = flowableDeployQueryService;
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

	@GET
	@Path("view/{processInstanceId}")
	@Produces({ MediaType.TEXT_HTML })
	@ResponseBody
	@Transactional(readOnly = true)
	public byte[] view(@Context HttpServletRequest request,
			@PathParam("processInstanceId") String processInstanceId) {
		String contextPath = request.getContextPath();
		StringBuffer buffer = new StringBuffer();
		if (StringUtils.isNotEmpty(processInstanceId)) {
			ProcessInstanceInfo processInstanceInfo = flowableProcessQueryService
					.getProcessInstanceInfo(processInstanceId);
			if (processInstanceInfo != null) {

				StringBuffer positionBuffer = new StringBuffer();
				StringBuffer position = new StringBuffer();

				StringBuffer text = new StringBuffer();

				int id = 0;
				List<ActivityInfo> processedActivityInfos = processInstanceInfo
						.getProcessedActivityInfos();
				List<ActivityInfo> activeActivityInfos = processInstanceInfo
						.getActiveActivityInfos();
				ActivityCoordinates coordinates = null;
				HistoricActivityInstance activityInstance = null;
				if (processedActivityInfos != null
						&& !processedActivityInfos.isEmpty()) {
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

						position.append("left:").append(coordinates.getX() - 2)
								.append("px;");
						position.append("top:").append(coordinates.getY() - 2)
								.append("px;");
						position.append("height:")
								.append(coordinates.getHeight() - 2)
								.append("px;");
						position.append("width:")
								.append(coordinates.getWidth() - 2)
								.append("px;");

						buffer.append(
								"\n        <div class=\"tip processed\" id=\"")
								.append(elId).append("\" style=\"")
								.append(position).append("\"></div>");

						Date startDate = activityInstance.getStartTime();
						Date endDate = activityInstance.getEndTime();
						buffer.append("\n        <script>$('").append(elId)
								.append("').store('tip:title', '")
								.append(title).append('\'')
								.append(").store('tip:text', '")
								.append("<br/><b>开始时间:</b> ")
								.append(DateUtils.getDateTime(startDate))
								.append("<br/><b>结束时间:</b> ")
								.append(DateUtils.getDateTime(endDate));
						if (activityInstance.getAssignee() != null) {
							buffer.append("<br/><b>执行人:</b> ").append(
									activityInstance.getAssignee());
						}
						buffer.append("');</script>");
					}
				}

				if (activeActivityInfos != null
						&& !activeActivityInfos.isEmpty()) {
					id = 0;
					for (ActivityInfo activityInfo : activeActivityInfos) {
						coordinates = activityInfo.getCoordinates();
						String elId = "_aai_" + (++id);
						positionBuffer.delete(0, positionBuffer.length());
						positionBuffer.append("left:")
								.append(coordinates.getX() - 2).append("px;");
						positionBuffer.append("top:")
								.append(coordinates.getY() - 2).append("px;");
						positionBuffer.append("height:")
								.append(coordinates.getHeight() - 2)
								.append("px;");
						positionBuffer.append("width:")
								.append(coordinates.getWidth() - 2)
								.append("px;");
						buffer.append(
								"\n        <div class=\"tip active\" id=\"")
								.append(elId).append("\" style=\"")
								.append(positionBuffer.toString())
								.append("\"></div>");
						activityInstance = activityInfo.getActivityInstance();
						if (activityInstance != null) {
							String title = activityInstance.getActivityName();
							Date startDate = activityInstance.getStartTime();
							buffer.append("\n        <script>$('").append(elId)
									.append("').store('tip:title', '")
									.append(title).append('\'')
									.append(").store('tip:text', '")
									.append("<br/><b>开始时间:</b> ")
									.append(DateUtils.getDateTime(startDate));
							if (activityInstance.getAssignee() != null) {
								buffer.append("<br/><b>执行人:</b> ").append(
										activityInstance.getAssignee());
							}
							buffer.append("');</script>");
						}
					}
				}
			}
		}

		String view = ViewProperties.getString("flowable.rs.view");
		if (StringUtils.isEmpty(view)) {
			view = "com/glaf/flowable/web/rest/view.ftl";
		}

		String content = null;

		if (cache.get(view) != null) {
			content = (String) cache.get(view);
		} else {
			try {
				Resource resouce = new ClassPathResource(view);
				content = FileUtils.readFile(resouce.getInputStream());
				cache.put(view, content);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Map<String, Object> context = new java.util.HashMap<String, Object>();
		context.put("contextPath", contextPath);
		context.put("x_script", buffer.toString());
		context.put("processInstanceId", processInstanceId);
		content = QueryUtils.replaceTextParas(content, context);

		try {
			return content.getBytes("UTF-8");
		} catch (IOException e) {
			return content.getBytes();
		}
	}

}