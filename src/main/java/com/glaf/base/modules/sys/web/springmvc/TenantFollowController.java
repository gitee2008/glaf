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

package com.glaf.base.modules.sys.web.springmvc;

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
import com.glaf.base.modules.sys.model.SysTenant;
import com.glaf.base.modules.sys.model.TenantFollow;
import com.glaf.base.modules.sys.query.SysTenantQuery;
import com.glaf.base.modules.sys.query.TenantFollowQuery;
import com.glaf.base.modules.sys.service.SysTenantService;
import com.glaf.base.modules.sys.service.TenantFollowService;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/tenant/follow")
@RequestMapping("/tenant/follow")
public class TenantFollowController {
	protected static final Log logger = LogFactory.getLog(TenantFollowController.class);

	protected SysTenantService sysTenantService;

	protected TenantFollowService tenantFollowService;

	public TenantFollowController() {

	}

	@ResponseBody
	@RequestMapping("/delete")
	public byte[] delete(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Long id = RequestUtils.getLong(request, "id");
		String ids = request.getParameter("ids");
		if (StringUtils.isNotEmpty(ids)) {
			StringTokenizer token = new StringTokenizer(ids, ",");
			while (token.hasMoreTokens()) {
				String x = token.nextToken();
				if (StringUtils.isNotEmpty(x)) {
					TenantFollow tenantFollow = tenantFollowService.getTenantFollow(Long.valueOf(x));
					if (tenantFollow != null) {
						if (StringUtils.equals(tenantFollow.getTenantId(), loginContext.getTenantId())
								&& loginContext.getRoles().contains("TenantAdmin")) {
							tenantFollowService.deleteById(tenantFollow.getId());
						}
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			TenantFollow tenantFollow = tenantFollowService.getTenantFollow(Long.valueOf(id));
			if (tenantFollow != null) {
				if (StringUtils.equals(tenantFollow.getTenantId(), loginContext.getTenantId())
						&& loginContext.getRoles().contains("TenantAdmin")) {
					tenantFollowService.deleteById(tenantFollow.getId());
				}
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("tenant_follow.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/tenant/follow/edit", modelMap);
	}

	@RequestMapping("/followMeJson")
	@ResponseBody
	public byte[] followMeJson(HttpServletRequest request) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		TenantFollowQuery query = new TenantFollowQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		if (!loginContext.isSystemAdministrator()) {
			query.followTenantId(loginContext.getTenantId());
		}

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
			limit = Paging.DEFAULT_PAGE_SIZE;
		}

		JSONObject result = new JSONObject();
		int total = tenantFollowService.getTenantFollowCountByQueryCriteria(query);
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

			List<TenantFollow> list = tenantFollowService.getTenantFollowsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (TenantFollow tenantFollow : list) {
					JSONObject rowJSON = tenantFollow.toJsonObject();
					rowJSON.put("id", tenantFollow.getId());
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

	@RequestMapping("/followmelist")
	public ModelAndView followmelist(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/tenant/follow/followmelist", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		TenantFollowQuery query = new TenantFollowQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		if (!loginContext.isSystemAdministrator()) {
			query.tenantId(loginContext.getTenantId());
		}

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
			limit = Paging.DEFAULT_PAGE_SIZE;
		}

		JSONObject result = new JSONObject();
		int total = tenantFollowService.getTenantFollowCountByQueryCriteria(query);
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

			List<TenantFollow> list = tenantFollowService.getTenantFollowsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (TenantFollow tenantFollow : list) {
					JSONObject rowJSON = tenantFollow.toJsonObject();
					rowJSON.put("id", tenantFollow.getId());
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

		return new ModelAndView("/tenant/follow/list", modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveTenantFollow")
	public byte[] saveTenantFollow(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		if (loginContext.isTenantAdmin()) {
			try {
				TenantFollowQuery query = new TenantFollowQuery();
				query.tenantId(loginContext.getTenantId());
				query.followTenantId(request.getParameter("followTenantId"));
				int count = tenantFollowService.getTenantFollowCountByQueryCriteria(query);
				if (count == 0) {
					TenantFollow tenantFollow = new TenantFollow();
					tenantFollow.setTenantId(loginContext.getTenantId());
					SysTenant tenant = sysTenantService.getSysTenantByTenantId(loginContext.getTenantId());
					tenantFollow.setTenantName(tenant.getName());
					tenantFollow.setFollowTenantId(request.getParameter("followTenantId"));
					tenant = sysTenantService.getSysTenantByTenantId(tenantFollow.getFollowTenantId());
					tenantFollow.setFollowTenantName(tenant.getName());
					tenantFollow.setProvince(tenant.getProvince());
					tenantFollow.setCity(tenant.getCity());
					tenantFollow.setCreateBy(actorId);
					this.tenantFollowService.save(tenantFollow);
				}
				return ResponseUtils.responseJsonResult(true);
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@RequestMapping("/search")
	public ModelAndView search(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String nameLike = request.getParameter("nameLike");
		if (StringUtils.isNotEmpty(nameLike)) {
			request.setAttribute("nameLike_enc", RequestUtils.encodeString(nameLike));
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/tenant/follow/search", modelMap);
	}

	@javax.annotation.Resource
	public void setSysTenantService(SysTenantService sysTenantService) {
		this.sysTenantService = sysTenantService;
	}

	@javax.annotation.Resource
	public void setTenantFollowService(TenantFollowService tenantFollowService) {
		this.tenantFollowService = tenantFollowService;
	}

	@RequestMapping("/tenantJson")
	@ResponseBody
	public byte[] TenantJson(HttpServletRequest request) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SysTenantQuery query = new SysTenantQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		String nameLike = request.getParameter("nameLike_enc");
		if (StringUtils.isNotEmpty(nameLike)) {
			query.nameLike(RequestUtils.decodeString(nameLike));
		}

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
			limit = Paging.DEFAULT_PAGE_SIZE;
		}

		JSONObject result = new JSONObject();
		int total = sysTenantService.getSysTenantCountByQueryCriteria(query);
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

			List<SysTenant> list = sysTenantService.getSysTenantsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (SysTenant sysTenant : list) {
					if (StringUtils.equals(loginContext.getTenantId(), sysTenant.getTenantId())) {
						continue;
					}
					JSONObject rowJSON = sysTenant.toJsonObject();
					rowJSON.put("id", sysTenant.getId());
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

}
