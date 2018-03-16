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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

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
import com.glaf.matrix.data.bean.TableDataBean;
import com.glaf.matrix.data.domain.DataFileEntity;
import com.glaf.matrix.data.domain.DataModel;
import com.glaf.matrix.data.domain.SysTable;
import com.glaf.matrix.data.factory.DataFileFactory;
import com.glaf.matrix.data.factory.RedisFileStorageFactory;
import com.glaf.matrix.data.query.DataFileQuery;
import com.glaf.matrix.data.service.ITableService;

@Controller("/dataFile")
@RequestMapping("/dataFile")
public class DataFileController {
	protected final static Log logger = LogFactory.getLog(DataFileController.class);

	protected ITableService tableService;

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

			String tableId = req.getParameter("tableId");
			if (StringUtils.isEmpty(tableId)) {
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

			logger.debug("status:" + status);

			try {
				SysTable tableDefinition = tableService.getSysTableById(tableId);
				if (tableDefinition != null) {
					if (StringUtils.equals(tableDefinition.getAttachmentFlag(), "1")
							|| StringUtils.equals(tableDefinition.getAttachmentFlag(), "2")) {
						/**
						 * 判断只允许上传一个文件的情况
						 */
						boolean onlyOneAttachment = false;
						if (StringUtils.equals(tableDefinition.getAttachmentFlag(), "1")) {
							onlyOneAttachment = true;
							DataFileQuery query = new DataFileQuery();
							query.tenantId(loginContext.getTenantId());

							if (StringUtils.isNotEmpty(businessKey)) {
								query.businessKey(businessKey);
								query.status(status);
							} else {
								query.status(0);
								query.serviceKey(tableId);
								query.setCreateBy(loginContext.getActorId());
								query.setBusinessKeyIsNull("true");
							}
							/**
							 * 如果已经有一个文件了，就不允许再上传了。
							 */
							List<DataFile> list = DataFileFactory.getInstance().getDataFileList(query);
							if (list != null && !list.isEmpty()) {
								return result.toJSONString().getBytes();
							}
						}

						JSONArray rowsJSON = new JSONArray();
						Map<String, MultipartFile> fileMap = req.getFileMap();
						Set<Entry<String, MultipartFile>> entrySet = fileMap.entrySet();
						for (Entry<String, MultipartFile> entry : entrySet) {
							MultipartFile mFile = entry.getValue();

							if (mFile.getOriginalFilename() != null && mFile.getSize() > 0
									&& mFile.getSize() <= FileUtils.MB_SIZE * tableDefinition.getAttachmentSize()) {
								String filename = mFile.getOriginalFilename();
								logger.debug("upload file:" + filename);
								if (StringUtils.isNotEmpty(tableDefinition.getAttachmentExts())) {
									if (!StringUtils.containsIgnoreCase(tableDefinition.getAttachmentExts(),
											FileUtils.getFileExt(filename))) {
										throw new RuntimeException("file size is not allowed");
									}
								}

								String fileId = UUID32.getUUID();
								if (filename.indexOf("/") != -1) {
									filename = filename.substring(filename.lastIndexOf("/") + 1, filename.length());
								} else if (filename.indexOf("\\") != -1) {
									filename = filename.substring(filename.lastIndexOf("\\") + 1, filename.length());
								}

								DataFile dataFile = new DataFileEntity();
								dataFile.setId(fileId);
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
								dataFile.setServiceKey(tableId);
								DataFileFactory.getInstance().insertDataFile(loginContext.getTenantId(), dataFile,
										mFile.getBytes());

								JSONObject json = new JSONObject();
								json.put("name", dataFile.getFilename());
								json.put("id", dataFile.getId());
								json.put("fileId", dataFile.getId());
								rowsJSON.add(json);

								if (onlyOneAttachment) {
									logger.debug("只允许上次一个附件.");
									break;// 只允许上次一个附件
								}
							}
						}

						result.put("files", rowsJSON);
					}
				}
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

	@ResponseBody
	@RequestMapping("/getRedisKeys")
	public byte[] getRedisKeys(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isSystemAdministrator()) {
			logger.debug(RequestUtils.getParameterMap(request));
			String region = request.getParameter("region");
			String server_name = request.getParameter("server_name");
			if (region != null && server_name != null) {
				Collection<String> keys = RedisFileStorageFactory.getInstance().getKeys(region, server_name);
				if (keys != null) {
					JSONArray array = new JSONArray();
					for (String key : keys) {
						array.add(key);
					}
					return array.toJSONString().getBytes();
				}
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource
	public void setTableService(ITableService tableService) {
		this.tableService = tableService;
	}

	@RequestMapping("/showUpload")
	public ModelAndView showUpload(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		RequestUtils.setRequestParameterToAttribute(request);
		String tableId = request.getParameter("tableId");
		if (StringUtils.isEmpty(tableId)) {
			modelMap.put("error_message", "您没有提供必要的信息，tableId是必须的！");
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

		logger.debug("status:" + status);

		try {
			List<DataFile> dataFiles = new ArrayList<DataFile>();
			SysTable sysTable = tableService.getSysTableById(tableId);
			if (sysTable != null) {
				if (StringUtils.equals(sysTable.getAttachmentFlag(), "1")
						|| StringUtils.equals(sysTable.getAttachmentFlag(), "2")) {
					if (StringUtils.isNotEmpty(businessKey)) {
						DataFileQuery query = new DataFileQuery();
						query.tenantId(loginContext.getTenantId());
						query.serviceKey(tableId);
						query.businessKey(businessKey);
						List<DataFile> rows = DataFileFactory.getInstance().getDataFileList(query);
						if (rows != null && rows.size() > 0) {
							dataFiles.addAll(rows);
						}
					}

					DataFileQuery query = new DataFileQuery();
					query.tenantId(loginContext.getTenantId());
					query.status(status);
					query.createBy(loginContext.getActorId());
					query.serviceKey(tableId);
					if (StringUtils.isNotEmpty(businessKey)) {
						query.businessKey(businessKey);
					} else {
						query.setBusinessKeyIsNull("true");
					}

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
									if (StringUtils.isEmpty(businessKey)
											&& StringUtils.isEmpty(dataFile.getBusinessKey())) {
										dataFiles.add(dataFile);
									}
								}
							}
						}
					}

					if (dataFiles.size() > 0) {
						request.setAttribute("dataFiles", dataFiles);
					}
				}
				request.setAttribute("table", sysTable);
				request.setAttribute("tableDefinition", sysTable);

				DataModel dataModel = null;

				boolean canUpdate = false;

				if (StringUtils.isNotEmpty(businessKey)) {
					TableDataBean tableDataBean = new TableDataBean();
					dataModel = tableDataBean.getDataModel(loginContext, sysTable, businessKey);
					if (dataModel != null) {
						canUpdate = tableDataBean.canUpdate(loginContext, sysTable, businessKey);
						logger.debug(loginContext.getActorId() + " canUpdate:" + canUpdate);
						int bstatus = dataModel.getBusinessStatus();
						if (bstatus == 9) {
							canUpdate = false;
						}
						request.setAttribute("canUpdate", canUpdate);
					}
				} else {
					canUpdate = true;
					request.setAttribute("canUpdate", true);
				}

				request.setAttribute("canUpload", false);
				if (StringUtils.equals(sysTable.getAttachmentFlag(), "1")) {
					if (dataFiles.size() == 0) {
						request.setAttribute("canUpload", true);
					}
				} else if (StringUtils.equals(sysTable.getAttachmentFlag(), "2")) {
					if (canUpdate) {
						request.setAttribute("canUpload", true);
					}
				}

				String audit = request.getParameter("audit");
				if (StringUtils.equals(audit, "true")) {
					request.setAttribute("canUpdate", false);
					request.setAttribute("canUpload", false);
				}

				if (status == 9) {
					request.setAttribute("canUpdate", false);
					request.setAttribute("canUpload", false);
				}
			}
		} catch (Exception ex) {
			logger.debug(ex);
			modelMap.put("error_message", "不能获取文件信息。");
			return new ModelAndView("/error");
		}

		String view = request.getParameter("view");
		if (StringUtils.isEmpty(view)) {
			view = "/tableData/showUpload";
		}

		return new ModelAndView(view, modelMap);
	}

}