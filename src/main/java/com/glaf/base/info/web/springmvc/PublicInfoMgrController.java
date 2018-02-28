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

package com.glaf.base.info.web.springmvc;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.base.info.model.PublicInfo;
import com.glaf.base.info.query.PublicInfoQuery;
import com.glaf.base.info.service.PublicInfoService;
import com.glaf.core.base.DataFile;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.PageResult;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;
import com.glaf.matrix.data.factory.DataFileFactory;
import com.glaf.matrix.data.query.DataFileQuery;
import com.glaf.matrix.data.service.IDataFileService;

@Controller("/base/infoMgr")
@RequestMapping("/base/infoMgr")
public class PublicInfoMgrController {
	protected static final Log logger = LogFactory.getLog(PublicInfoMgrController.class);

	protected IDataFileService dataFileService;

	protected PublicInfoService publicInfoService;

	public PublicInfoMgrController() {

	}

	@ResponseBody
	@RequestMapping("/delete")
	public byte[] delete(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		String id = ParamUtils.getString(params, "id");
		String ids = request.getParameter("ids");
		if (StringUtils.isNotEmpty(ids)) {
			StringTokenizer token = new StringTokenizer(ids, ",");
			while (token.hasMoreTokens()) {
				String x = token.nextToken();
				if (StringUtils.isNotEmpty(x)) {
					PublicInfo publicInfo = publicInfoService.getPublicInfo(x);
					if (publicInfo != null && (StringUtils.equals(publicInfo.getCreateBy(), loginContext.getActorId())
							|| loginContext.isSystemAdministrator() || (loginContext.isTenantAdmin()
									&& StringUtils.equals(publicInfo.getTenantId(), loginContext.getTenantId())))) {
						DataFileFactory.getInstance().deleteByBusinessKey(publicInfo.getTenantId(),
								publicInfo.getServiceKey(), publicInfo.getId());
						publicInfoService.deleteById(publicInfo.getId());
					}
				}
			}
			return ResponseUtils.responseJsonResult(true);
		} else if (StringUtils.isNotEmpty(id)) {
			PublicInfo publicInfo = publicInfoService.getPublicInfo(id);
			if (publicInfo != null && (StringUtils.equals(publicInfo.getCreateBy(), loginContext.getActorId())
					|| loginContext.isSystemAdministrator() || (loginContext.isTenantAdmin()
							&& StringUtils.equals(publicInfo.getTenantId(), loginContext.getTenantId())))) {
				DataFileFactory.getInstance().deleteByBusinessKey(publicInfo.getTenantId(), publicInfo.getServiceKey(),
						publicInfo.getId());
				publicInfoService.deleteById(publicInfo.getId());
				return ResponseUtils.responseJsonResult(true);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		PublicInfo publicInfo = publicInfoService.getPublicInfo(request.getParameter("id"));

		if (publicInfo != null) {
			request.setAttribute("publicInfo", publicInfo);
			JSONObject rowJSON = publicInfo.toJsonObject();
			request.setAttribute("x_json", rowJSON.toJSONString());

			String serviceKey = request.getParameter("serviceKey");
			if (StringUtils.isEmpty(serviceKey)) {
				serviceKey = "PublicInfo";
			}

			DataFileQuery query = new DataFileQuery();
			query.tenantId(publicInfo.getTenantId());
			query.businessKey(publicInfo.getId());
			query.serviceKey(serviceKey);
			List<DataFile> dataFiles = dataFileService.getDataFileList(query);
			if (dataFiles != null && !dataFiles.isEmpty()) {
				request.setAttribute("dataFiles", dataFiles);
			}
		}

		request.setAttribute("serviceUrl", RequestUtils.getServiceUrl(request));

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("publicInfo.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/base/info/edit", modelMap);
	}

	@RequestMapping("/edit2")
	public ModelAndView edit2(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		PublicInfo publicInfo = publicInfoService.getPublicInfo(request.getParameter("id"));

		if (publicInfo != null) {
			request.setAttribute("publicInfo", publicInfo);
			JSONObject rowJSON = publicInfo.toJsonObject();
			request.setAttribute("x_json", rowJSON.toJSONString());

			String serviceKey = request.getParameter("serviceKey");
			if (StringUtils.isEmpty(serviceKey)) {
				serviceKey = "PublicInfo";
			}

			DataFileQuery query = new DataFileQuery();
			query.tenantId(publicInfo.getTenantId());
			query.businessKey(publicInfo.getId());
			query.serviceKey(serviceKey);
			List<DataFile> dataFiles = dataFileService.getDataFileList(query);
			if (dataFiles != null && !dataFiles.isEmpty()) {
				request.setAttribute("dataFiles", dataFiles);
			}
		}

		request.setAttribute("serviceUrl", RequestUtils.getServiceUrl(request));

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("publicInfo.edit2");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/base/info/edit2", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		RequestUtils.setRequestParameterToAttribute(request);

		Map<String, Object> params = RequestUtils.getParameterMap(request);
		PublicInfoQuery query = new PublicInfoQuery();
		Tools.populate(query, params);

		String serviceKey = request.getParameter("serviceKey");
		if (StringUtils.isEmpty(serviceKey)) {
			serviceKey = "PublicInfo";
		}

		query.serviceKey(serviceKey);
		query.tenantId(loginContext.getTenantId());

		int start = 0;
		int limit = 10;
		String orderName = null;
		String order = null;

		int pageNo = ParamUtils.getInt(params, "page");
		limit = ParamUtils.getInt(params, "rows");
		start = (pageNo - 1) * limit;
		orderName = ParamUtils.getString(params, "sortName");
		order = ParamUtils.getString(params, "sortOrder");

		if (start < 0) {
			start = 0;
		}

		if (limit <= 0) {
			limit = PageResult.DEFAULT_PAGE_SIZE;
		}

		JSONObject result = new JSONObject();
		int total = publicInfoService.getPublicInfoCountByQueryCriteria(query);
		if (total > 0) {
			result.put("total", total);
			result.put("totalCount", total);
			result.put("totalRecords", total);
			result.put("start", start);
			result.put("startIndex", start);
			result.put("limit", limit);
			result.put("pageSize", limit);

			if (StringUtils.isNotEmpty(orderName)) {
				query.setSortOrder(orderName);
				if (StringUtils.equals(order, "desc")) {
					query.setSortOrder(" desc ");
				}
			}

			List<PublicInfo> list = publicInfoService.getPublicInfosByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (PublicInfo publicInfo : list) {
					JSONObject rowJSON = publicInfo.toJsonObject();
					rowJSON.put("id", publicInfo.getId());
					rowJSON.put("publicInfoId", publicInfo.getId());
					rowJSON.put("startIndex", ++start);
					rowsJSON.add(rowJSON);
				}

			}
		} else {
			JSONArray rowsJSON = new JSONArray();
			result.put("rows", rowsJSON);
			result.put("total", total);
		}
		return result.toJSONString().getBytes("UTF-8");
	}

	@RequestMapping
	public ModelAndView list(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/base/info/mgr_list", modelMap);
	}

	@ResponseBody
	@RequestMapping("/publish")
	public byte[] publish(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		/**
		 * 只有系统管理员及租户管理员才能发布信息
		 */
		if (loginContext.isSystemAdministrator() || loginContext.isTenantAdmin()) {
			String id = request.getParameter("id");
			int publishFlag = RequestUtils.getInt(request, "publishFlag");
			PublicInfo publicInfo = null;
			try {
				if (StringUtils.isNotEmpty(id)) {
					publicInfo = publicInfoService.getPublicInfo(id);
					if (StringUtils.equals(publicInfo.getTenantId(), loginContext.getTenantId())) {
						publicInfo.setPublishFlag(publishFlag);
						publicInfoService.save(publicInfo);
						return ResponseUtils.responseJsonResult(true);
					}
				}
			} catch (Exception ex) {
				logger.error(ex);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@ResponseBody
	@RequestMapping("/save")
	public byte[] save(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug(params);
		String id = request.getParameter("id");
		PublicInfo publicInfo = null;

		/**
		 * 只有系统管理员及租户管理员才能发布信息
		 */
		if (loginContext.isSystemAdministrator() || loginContext.isTenantAdmin()) {
			try {
				if (StringUtils.isNotEmpty(id)) {
					publicInfo = publicInfoService.getPublicInfo(id);
					if (publicInfo != null) {
						if (publicInfo.getPublishFlag() == 1) {
							return ResponseUtils.responseJsonResult(false, "已经审核并发布的数据不能修改！");
						}
						if (!StringUtils.equals(publicInfo.getTenantId(), loginContext.getTenantId())) {
							return ResponseUtils.responseJsonResult(false, "您没有该数据的修改权限！");
						}
					}
				}
				if (publicInfo == null) {
					publicInfo = new PublicInfo();
					publicInfo.setCreateBy(loginContext.getActorId());
					publicInfo.setTenantId(loginContext.getTenantId());
				}
				params.remove("tenantId");
				params.remove("createBy");
				Tools.populate(publicInfo, params);
				publicInfo.setOriginalFlag(RequestUtils.getInt(request, "originalFlag"));
				publicInfo.setStartDate(RequestUtils.getDate(request, "startDate"));
				publicInfo.setTag(request.getParameter("tag"));
				publicInfo.setSubject(request.getParameter("subject"));
				publicInfo.setLink(request.getParameter("link"));
				publicInfo.setEndDate(RequestUtils.getDate(request, "endDate"));
				publicInfo.setRefererUrl(request.getParameter("refererUrl"));
				publicInfo.setAuthor(request.getParameter("author"));
				publicInfo.setName(request.getParameter("name"));
				publicInfo.setCommentFlag(RequestUtils.getInt(request, "commentFlag"));
				publicInfo.setUpdateBy(actorId);
				publicInfo.setKeywords(request.getParameter("keywords"));
				publicInfo.setContent(request.getParameter("editorValue"));
				publicInfo.setUnitName(request.getParameter("unitName"));

				String serviceKey = request.getParameter("serviceKey");
				if (StringUtils.isEmpty(serviceKey)) {
					serviceKey = "PublicInfo";
				}

				publicInfo.setServiceKey(serviceKey);
				publicInfoService.save(publicInfo);

				DataFileFactory.getInstance().updateStatus(loginContext.getTenantId(), serviceKey, publicInfo.getId(),
						1);
				return ResponseUtils.responseJsonResult(true);
			} catch (Exception ex) {
				logger.error(ex);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource
	public void setDataFileService(IDataFileService dataFileService) {
		this.dataFileService = dataFileService;
	}

	@javax.annotation.Resource
	public void setPublicInfoService(PublicInfoService publicInfoService) {
		this.publicInfoService = publicInfoService;
	}

}
