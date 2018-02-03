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
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.flowable.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.glaf.core.config.ViewProperties;
import com.glaf.core.util.FileUtils;
import com.glaf.core.util.IOUtils;

import com.glaf.flowable.service.FlowableDeployQueryService;
import com.glaf.flowable.service.FlowableDeployService;
import com.glaf.flowable.service.FlowableProcessQueryService;
import com.glaf.flowable.util.ProcessUtils;

@Controller("/flowable/deploy")
@RequestMapping("/flowable/deploy")
public class FlowableDeployController {

	protected final static Log logger = LogFactory.getLog(FlowableDeployController.class);

	protected FlowableDeployService flowableDeployService;

	protected FlowableDeployQueryService flowableDeployQueryService;

	protected FlowableProcessQueryService flowableProcessQueryService;

	/**
	 * 
	 * @param model
	 * @param mFile
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.POST)
	public void processSubmit(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("file") MultipartFile mFile) throws IOException {
		if (mFile != null && !mFile.isEmpty() && mFile.getOriginalFilename() != null) {
			String deploymentId = null;
			ProcessDefinition processDefinition = null;
			if (StringUtils.endsWithIgnoreCase(mFile.getOriginalFilename(), ".zip")
					|| StringUtils.endsWithIgnoreCase(mFile.getOriginalFilename(), ".jar")) {
				ZipInputStream zipInputStream = null;
				try {
					zipInputStream = new ZipInputStream(mFile.getInputStream());
					deploymentId = flowableDeployService.addZipInputStream(zipInputStream).getId();
				} finally {
					IOUtils.closeStream(zipInputStream);
				}
			} else {
				String resourceName = FileUtils.getFilename(mFile.getOriginalFilename());
				deploymentId = flowableDeployService.addInputStream(resourceName, mFile.getInputStream()).getId();
			}
			if (StringUtils.isNotEmpty(deploymentId)) {
				logger.debug("deploymentId:" + deploymentId);
				processDefinition = flowableProcessQueryService.getProcessDefinitionByDeploymentId(deploymentId);
				if (processDefinition != null) {
					String resourceName = processDefinition.getDiagramResourceName();
					if (resourceName != null) {
						byte[] bytes = null;
						ByteArrayInputStream bais = null;
						BufferedInputStream bis = null;
						OutputStream outputStream = null;
						try {
							ProcessUtils.saveProcessImageToFileSystem(processDefinition);
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
			}
		}

	}

	@javax.annotation.Resource
	public void setFlowableDeployService(FlowableDeployService flowableDeployService) {
		this.flowableDeployService = flowableDeployService;
	}

	@javax.annotation.Resource
	public void setFlowableDeployQueryService(FlowableDeployQueryService flowableDeployQueryService) {
		this.flowableDeployQueryService = flowableDeployQueryService;
	}

	@javax.annotation.Resource
	public void setFlowableProcessQueryService(FlowableProcessQueryService flowableProcessQueryService) {
		this.flowableProcessQueryService = flowableProcessQueryService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String showDeploy(Model model) {
		String view = ViewProperties.getString("flowable.showDeploy");
		if (StringUtils.isNotEmpty(view)) {
			return view;
		}
		return "/flowable/deploy/showDeploy";
	}

}