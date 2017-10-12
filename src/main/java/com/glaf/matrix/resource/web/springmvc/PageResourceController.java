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

package com.glaf.matrix.resource.web.springmvc;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.FileUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.UUID32;

import com.glaf.matrix.resource.domain.PageResource;
import com.glaf.matrix.resource.service.PageResourceService;

@Controller("/matrix/resource")
@RequestMapping("/matrix/resource")
public class PageResourceController {
	protected final static Log logger = LogFactory.getLog(PageResourceController.class);

	protected PageResourceService pageResourceService;

	@ResponseBody
	@RequestMapping("/deleteById")
	public byte[] deleteById(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String fileId = request.getParameter("fileId");
		if (StringUtils.isNotEmpty(fileId)) {
			try {
				PageResource resource = pageResourceService.getPageResourceByFileId(fileId);
				if (resource != null && (loginContext.isSystemAdministrator()
						|| StringUtils.equals(resource.getCreateBy(), loginContext.getActorId())
						|| (loginContext.isTenantAdmin()
								&& StringUtils.equals(loginContext.getTenantId(), resource.getTenantId())))) {
					pageResourceService.deleteById(resource.getId());
					logger.debug(fileId + " delete success.");
					return ResponseUtils.responseJsonResult(true);
				}
			} catch (Exception ex) {
				logger.debug(ex);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@ResponseBody
	@RequestMapping("/doUpload")
	public byte[] doUpload(HttpServletRequest request) throws IOException {
		logger.debug("-------------------------doUpload----------------------");
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		JSONObject result = new JSONObject();
		// 将当前上下文初始化给 CommonsMutipartResolver（多部分解析器）
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 检查form中是否有enctype="multipart/form-data"
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
			String type = req.getParameter("type");
			if (StringUtils.isEmpty(type)) {
				type = "0";
			}
			try {
				JSONArray rowsJSON = new JSONArray();
				Map<String, MultipartFile> fileMap = req.getFileMap();
				Set<Entry<String, MultipartFile>> entrySet = fileMap.entrySet();
				for (Entry<String, MultipartFile> entry : entrySet) {
					MultipartFile mFile = entry.getValue();
					if (mFile.getOriginalFilename() != null && mFile.getSize() > 0
							&& mFile.getSize() <= FileUtils.MB_SIZE * 5) {
						String filename = mFile.getOriginalFilename();
						if (filename != null) {
							logger.debug("upload file:" + filename);
							String fileId = UUID32.getUUID();
							if (filename.indexOf("/") != -1) {
								filename = filename.substring(filename.lastIndexOf("/") + 1, filename.length());
							} else if (filename.indexOf("\\") != -1) {
								filename = filename.substring(filename.lastIndexOf("\\") + 1, filename.length());
							}
							PageResource resource = new PageResource();
							resource.setCreateBy(loginContext.getActorId());
							resource.setResFileId(fileId);
							resource.setResFileName(filename);
							resource.setResName(mFile.getName());
							resource.setResContentType(mFile.getContentType());
							resource.setResContent(mFile.getBytes());
							resource.setResType(type);
							pageResourceService.save(resource);

							JSONObject json = new JSONObject();
							json.put("name", resource.getResFileName());
							json.put("id", resource.getId());
							json.put("fileId", resource.getResFileId());
							rowsJSON.add(json);
						}
					}
				}
				result.put("files", rowsJSON);
			} catch (Exception ex) {
				logger.debug(ex);
			}
		}
		// logger.debug("json:" + result.toJSONString());
		return result.toJSONString().getBytes("UTF-8");
	}

	@ResponseBody
	@RequestMapping("/download")
	public void download(HttpServletRequest request, HttpServletResponse response) {
		String fileId = request.getParameter("fileId");
		if (StringUtils.isNotEmpty(fileId)) {
			logger.debug("fileId:" + fileId);
			PageResource resource = pageResourceService.getPageResourceByFileId(fileId);
			if (resource != null) {
				logger.debug("id:" + resource.getId());
				byte[] data = null;
				try {
					data = resource.getResContent();
					ResponseUtils.download(request, response, data, resource.getResFileName());
				} catch (Exception ex) {
				}
			}
		}
	}

	@javax.annotation.Resource(name = "com.glaf.matrix.resource.service.pageResourceService")
	public void setPageResourceService(PageResourceService pageResourceService) {
		this.pageResourceService = pageResourceService;
	}

}