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

package com.glaf.matrix.data.web.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.core.base.DataFile;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.matrix.data.factory.DataFileFactory;

public class ImageDispatcherServlet extends HttpServlet {

	private static final long serialVersionUID = 5305608071138971757L;

	protected static final Log logger = LogFactory.getLog(ImageDispatcherServlet.class);

	public ImageDispatcherServlet() {
		super();
	}

	protected void service(HttpServletRequest request, HttpServletResponse response) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		// 获取上下文路径
		String contextPath = request.getContextPath();
		// 获取请求路径
		String requestURI = request.getRequestURI();
		String resPath = requestURI.substring(contextPath.length(), requestURI.length());
		String urlPattern = "";
		urlPattern = urlPattern.replace("*", "");
		// 获取有效文件路径
		String filePath = resPath.replace(urlPattern, "");
		try {
			logger.debug("filePath:" + filePath);
			if (filePath != null && filePath.trim().length() > 0) {
				if (filePath.startsWith("//")) {
					filePath = filePath.substring(1, filePath.length());
				}
				DataFile dataFile = DataFileFactory.getInstance().getDataFileByPath(loginContext.getTenantId(),
						filePath);
				// logger.debug("dataFile:"+dataFile);
				if (dataFile != null && dataFile.getInputStream() != null && dataFile.getFilename() != null) {
					// 获取文件类型
					String contentType = dataFile.getContentType();
					response.setContentType(contentType);
					logger.debug("filename:" + dataFile.getFilename());
					ResponseUtils.download(request, response, dataFile.getInputStream(), dataFile.getFilename());
					response.flushBuffer();
				}
			}
		} catch (IOException ex) {

		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		service(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		service(request, response);
	}
}
