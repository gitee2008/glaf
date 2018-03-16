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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.core.base.DataFile;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.FileUtils;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.UUID32;
import com.glaf.core.util.security.RSAUtils;
import com.glaf.matrix.data.domain.DataFileEntity;
import com.glaf.matrix.data.factory.DataFileFactory;
import com.glaf.matrix.data.query.DataFileQuery;

@Controller("/matrix/image")
@RequestMapping("/matrix/image")
public class ImageController {
	protected final static Log logger = LogFactory.getLog(ImageController.class);

	@ResponseBody
	@RequestMapping("/deleteById")
	public byte[] deleteById(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String fileId = request.getParameter("fileId");
		if (StringUtils.isNotEmpty(fileId)) {
			try {
				DataFile dataFile = DataFileFactory.getInstance().getDataFileByFileId(loginContext.getTenantId(),
						fileId);
				if (dataFile != null && (loginContext.isSystemAdministrator()
						|| StringUtils.equals(dataFile.getCreateBy(), loginContext.getActorId())
						|| (loginContext.isTenantAdmin()
								&& StringUtils.equals(loginContext.getTenantId(), dataFile.getTenantId())))) {
					DataFileFactory.getInstance().deleteDataFileByFileId(loginContext.getTenantId(), fileId);
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
	@RequestMapping("/deleteByName")
	public byte[] deleteByName(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		logger.debug(RequestUtils.getParameterMap(request));
		String filename = request.getParameter("filename");
		String serviceKey = request.getParameter("serviceKey");
		// int status = RequestUtils.getInt(request, "status");
		int status = -1;
		Map<String, Object> paramMap = RequestUtils.getParameterMap(request);
		if (paramMap.get("status_enc") != null) {
			status = Integer.parseInt(RSAUtils.decryptString(ParamUtils.getString(paramMap, "status_enc")));
		}
		if (StringUtils.isNotEmpty(serviceKey) && StringUtils.isNotEmpty(filename)) {
			try {
				// filename = new String(Base64.decode(filename), "GBK");
				DataFileFactory.getInstance().deleteByServiceKey(loginContext.getTenantId(), serviceKey,
						loginContext.getActorId(), filename, status);
				return ResponseUtils.responseJsonResult(true);
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

			String serviceKey = req.getParameter("serviceKey");
			if (StringUtils.isEmpty(serviceKey)) {
				return result.toJSONString().getBytes();
			}

			Map<String, Object> paramMap = RequestUtils.getParameterMap(req);
			logger.debug("paramMap:" + paramMap);
			String businessKey = req.getParameter("businessKey_enc");
			if (StringUtils.isNotEmpty(businessKey)) {
				/**
				 * 使用处解密
				 */
				businessKey = RSAUtils.decryptString(businessKey);
			}
			int status = -1;
			if (paramMap.get("status_enc") != null) {
				status = Integer.parseInt(RSAUtils.decryptString(ParamUtils.getString(paramMap, "status_enc")));
			}

			try {

				JSONArray rowsJSON = new JSONArray();
				Map<String, MultipartFile> fileMap = req.getFileMap();
				Set<Entry<String, MultipartFile>> entrySet = fileMap.entrySet();
				for (Entry<String, MultipartFile> entry : entrySet) {
					MultipartFile mFile = entry.getValue();

					if (mFile.getOriginalFilename() != null && mFile.getSize() > 0
							&& mFile.getSize() <= FileUtils.MB_SIZE * 20) {
						String filename = mFile.getOriginalFilename();
						if (filename != null) {
							logger.debug("upload file:" + filename);

							String fileId = UUID32.getUUID();
							if (filename.indexOf("/") != -1) {
								filename = filename.substring(filename.lastIndexOf("/") + 1, filename.length());
							} else if (filename.indexOf("\\") != -1) {
								filename = filename.substring(filename.lastIndexOf("\\") + 1, filename.length());
							}

							DataFile dataFile = new DataFileEntity();
							dataFile.setId(fileId);
							dataFile.setCreateDate(new java.util.Date());
							dataFile.setLastModified(System.currentTimeMillis());
							dataFile.setCreateBy(loginContext.getActorId());
							dataFile.setFilename(filename);
							dataFile.setName(mFile.getName());
							dataFile.setContentType(mFile.getContentType());
							dataFile.setSize((int) mFile.getSize());
							dataFile.setType(type);
							dataFile.setStatus(status);
							if (StringUtils.isNotEmpty(businessKey)) {
								dataFile.setBusinessKey(businessKey);
							} else {
								dataFile.setBusinessKey(null);
							}
							dataFile.setServiceKey(serviceKey);
							DataFileFactory.getInstance().insertDataFile(loginContext.getTenantId(), dataFile,
									mFile.getBytes());

							JSONObject json = new JSONObject();
							json.put("name", dataFile.getFilename());
							json.put("id", dataFile.getId());
							json.put("fileId", dataFile.getId());
							rowsJSON.add(json);
						}
					}
				}

				result.put("files", rowsJSON);

			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		// logger.debug("json:" + result.toJSONString());
		return result.toJSONString().getBytes("UTF-8");
	}

	@ResponseBody
	@RequestMapping("/download")
	public void download(HttpServletRequest request, HttpServletResponse response) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String fileId = request.getParameter("fileId");
		if (StringUtils.isNotEmpty(fileId)) {
			logger.debug("fileId:" + fileId);
			DataFile blob = DataFileFactory.getInstance().getDataFileByFileId(loginContext.getTenantId(), fileId);
			if (blob != null) {
				logger.debug("id:" + blob.getId());
				InputStream inputStream = null;
				try {
					inputStream = DataFileFactory.getInstance().getInputStreamById(loginContext.getTenantId(),
							blob.getId());
					ResponseUtils.download(request, response, inputStream, blob.getFilename());
				} catch (Exception ex) {
				} finally {
					blob.setData(null);
					blob = null;
					com.glaf.core.util.IOUtils.closeStream(inputStream);
				}
			}
		}
	}

	@RequestMapping("/imagelist")
	public ModelAndView imagelist(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		RequestUtils.setRequestParameterToAttribute(request);
		String serviceKey = request.getParameter("serviceKey");
		if (StringUtils.isEmpty(serviceKey)) {
			modelMap.put("error_message", "您没有提供必要的信息，serviceKey是必须的！");
			return new ModelAndView("/error");
		}
		Map<String, Object> paramMap = RequestUtils.getParameterMap(request);
		logger.debug("paramMap:" + paramMap);
		String businessKey = request.getParameter("businessKey");
		if (StringUtils.isNotEmpty(businessKey)) {
			/**
			 * 入口处加密
			 */
			request.setAttribute("businessKey_enc", RSAUtils.encryptString(businessKey));
		}

		int status = -1;
		if (paramMap.get("status_enc") != null) {
			status = Integer.parseInt(RSAUtils.decryptString(ParamUtils.getString(paramMap, "status_enc")));
		}

		logger.debug("status:" + status);
		List<DataFile> dataFiles = new ArrayList<DataFile>();

		try {

			if (StringUtils.isNotEmpty(businessKey)) {
				DataFileQuery query = new DataFileQuery();
				query.tenantId(loginContext.getTenantId());
				query.serviceKey(serviceKey);
				query.businessKey(businessKey);
				List<DataFile> rows = DataFileFactory.getInstance().getDataFileList(query);
				if (rows != null && rows.size() > 0) {
					dataFiles.addAll(rows);
				}
			}

			DataFileQuery query = new DataFileQuery();
			query.tenantId(loginContext.getTenantId());
			query.createBy(loginContext.getActorId());
			query.serviceKey(serviceKey);
			if (StringUtils.isNotEmpty(businessKey)) {
				query.businessKey(businessKey);
			} else {
				query.setBusinessKeyIsNull("true");
			}
			query.status(status);
			logger.debug("tenantId:" + query.getTenantId());
			// logger.debug("query:" + query);
			List<DataFile> rows = DataFileFactory.getInstance().getDataFileList(query);
			if (rows != null && rows.size() > 0) {
				Iterator<DataFile> iterator = rows.iterator();
				while (iterator.hasNext()) {
					DataFile dataFile = iterator.next();
					if (!dataFiles.contains(dataFile)) {
						if (StringUtils.isNotEmpty(businessKey)) {
							if (StringUtils.equals(dataFile.getBusinessKey(), businessKey)) {
								dataFiles.add(dataFile);
							}
						} else {
							if (StringUtils.isEmpty(businessKey) && StringUtils.isEmpty(dataFile.getBusinessKey())) {
								dataFiles.add(dataFile);
							}
						}
					}
				}
			}

			if (dataFiles.size() > 0) {
				request.setAttribute("dataFiles", dataFiles);
			}

		} catch (Exception ex) {
			logger.error(ex);
			modelMap.put("error_message", "不能获取文件信息。");
			return new ModelAndView("/error");
		}

		String view = request.getParameter("view");
		if (StringUtils.isEmpty(view)) {
			view = "/tableData/imagelist";
		}

		return new ModelAndView(view, modelMap);
	}

	@RequestMapping("/showUpload")
	public ModelAndView showUpload(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		RequestUtils.setRequestParameterToAttribute(request);
		String serviceKey = request.getParameter("serviceKey");
		if (StringUtils.isEmpty(serviceKey)) {
			modelMap.put("error_message", "您没有提供必要的信息，serviceKey是必须的！");
			return new ModelAndView("/error");
		}
		Map<String, Object> paramMap = RequestUtils.getParameterMap(request);
		logger.debug("paramMap:" + paramMap);
		String businessKey = request.getParameter("businessKey_enc");
		if (StringUtils.isNotEmpty(businessKey)) {
			/**
			 * 使用处解密
			 */
			businessKey = RSAUtils.decryptString(businessKey);
		}

		int status = -1;
		if (paramMap.get("status_enc") != null) {
			status = Integer.parseInt(RSAUtils.decryptString(ParamUtils.getString(paramMap, "status_enc")));
		}

		try {
			List<DataFile> dataFiles = new ArrayList<DataFile>();

			if (StringUtils.isNotEmpty(businessKey)) {
				DataFileQuery query = new DataFileQuery();
				query.tenantId(loginContext.getTenantId());
				query.serviceKey(serviceKey);
				query.businessKey(businessKey);
				List<DataFile> rows = DataFileFactory.getInstance().getDataFileList(query);
				if (rows != null && rows.size() > 0) {
					dataFiles.addAll(rows);
				}
			}

			DataFileQuery query = new DataFileQuery();
			query.tenantId(loginContext.getTenantId());
			query.createBy(loginContext.getActorId());
			query.serviceKey(serviceKey);
			if (StringUtils.isNotEmpty(businessKey)) {
				query.businessKey(businessKey);
			} else {
				query.setBusinessKeyIsNull("true");
			}
			query.status(status);
			logger.debug("tenantId:" + query.getTenantId());
			// logger.debug("query:" + query);
			List<DataFile> rows = DataFileFactory.getInstance().getDataFileList(query);
			if (rows != null && rows.size() > 0) {
				Iterator<DataFile> iterator = rows.iterator();
				while (iterator.hasNext()) {
					DataFile dataFile = iterator.next();
					if (!dataFiles.contains(dataFile)) {
						if (StringUtils.isNotEmpty(businessKey)) {
							if (StringUtils.equals(dataFile.getBusinessKey(), businessKey)) {
								dataFiles.add(dataFile);
							}
						} else {
							if (StringUtils.isEmpty(businessKey) && StringUtils.isEmpty(dataFile.getBusinessKey())) {
								dataFiles.add(dataFile);
							}
						}
					}
				}
			}

			if (dataFiles.size() > 0) {
				request.setAttribute("dataFiles", dataFiles);
			}

			boolean canUpdate = false;

			if (StringUtils.isNotEmpty(businessKey)) {
				if (status == 0) {
					canUpdate = true;
				}
				request.setAttribute("canUpdate", canUpdate);
			} else {
				canUpdate = true;
				request.setAttribute("canUpdate", canUpdate);
			}

			request.setAttribute("canUpload", canUpdate);

		} catch (Exception ex) {
			logger.error(ex);
			modelMap.put("error_message", "不能获取文件信息。");
			return new ModelAndView("/error");
		}

		String view = request.getParameter("view");
		if (StringUtils.isEmpty(view)) {
			view = "/tableData/imageUpload";
		}

		return new ModelAndView(view, modelMap);
	}

}