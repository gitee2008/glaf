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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.base.modules.sys.model.SysTenant;
import com.glaf.base.modules.sys.model.TenantGrant;
import com.glaf.base.modules.sys.query.SysTenantQuery;
import com.glaf.base.modules.sys.query.TenantGrantQuery;
import com.glaf.base.modules.sys.service.SysTenantService;
import com.glaf.base.modules.sys.service.TenantGrantService;

import com.glaf.core.base.BaseItem;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.identity.User;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.StringTools;
import com.glaf.core.util.Tools;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/sys/tenantGrant")
@RequestMapping("/sys/tenantGrant")
public class TenantGrantController {
	protected static final Log logger = LogFactory.getLog(TenantGrantController.class);

	protected SysTenantService sysTenantService;

	protected TenantGrantService tenantGrantService;

	public TenantGrantController() {

	}

	@ResponseBody
	@RequestMapping("/delete")
	public byte[] delete(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String id = RequestUtils.getString(request, "id");
		String ids = request.getParameter("ids");
		if (StringUtils.isNotEmpty(ids)) {
			StringTokenizer token = new StringTokenizer(ids, ",");
			while (token.hasMoreTokens()) {
				String x = token.nextToken();
				if (StringUtils.isNotEmpty(x)) {
					TenantGrant tenantGrant = tenantGrantService.getTenantGrant(String.valueOf(x));
					if (tenantGrant != null && (StringUtils.equals(tenantGrant.getCreateBy(), loginContext.getActorId())
							|| loginContext.isSystemAdministrator())) {
						tenantGrantService.deleteById(tenantGrant.getId());
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			TenantGrant tenantGrant = tenantGrantService.getTenantGrant(String.valueOf(id));
			if (tenantGrant != null && (StringUtils.equals(tenantGrant.getCreateBy(), loginContext.getActorId())
					|| loginContext.isSystemAdministrator())) {
				tenantGrantService.deleteById(tenantGrant.getId());
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		TenantGrantQuery query = new TenantGrantQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		if (!loginContext.isSystemAdministrator()) {
			String actorId = loginContext.getActorId();
			query.createBy(actorId);
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
		int total = tenantGrantService.getTenantGrantCountByQueryCriteria(query);
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

			List<TenantGrant> list = tenantGrantService.getTenantGrantsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (TenantGrant tenantGrant : list) {
					JSONObject rowJSON = tenantGrant.toJsonObject();
					rowJSON.put("id", tenantGrant.getId());
					rowJSON.put("rowId", tenantGrant.getId());
					rowJSON.put("tenantGrantId", tenantGrant.getId());
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

		return new ModelAndView("/sys/tenantGrant/list", modelMap);
	}

	@RequestMapping("/privilege")
	public ModelAndView privilege(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String type = request.getParameter("type");
		String userId = request.getParameter("userId");
		String privilege = request.getParameter("privilege");
		List<TenantGrant> list = null;
		if (StringUtils.isNotEmpty(userId) && StringUtils.isNotEmpty(privilege)) {
			TenantGrantQuery query = new TenantGrantQuery();
			query.grantee(userId);
			query.setType(type);
			query.setPrivilege(privilege);
			list = tenantGrantService.list(query);
			request.setAttribute("rows", list);
		}

		List<BaseItem> selected = new ArrayList<BaseItem>();
		List<BaseItem> unselected = new ArrayList<BaseItem>();

		SysTenantQuery query = new SysTenantQuery();
		List<SysTenant> rows = sysTenantService.list(query);
		if (rows != null && !rows.isEmpty()) {
			boolean include = false;
			for (SysTenant t : rows) {
				include = false;
				if (list != null && !list.isEmpty()) {
					for (TenantGrant p : list) {
						if (StringUtils.equals(t.getTenantId(), p.getTenantId())) {
							BaseItem item = new BaseItem();
							item.setName(t.getName() + "[" + t.getProvince() + t.getCity() + "]");
							item.setValue(t.getTenantId());
							selected.add(item);
							include = true;
						}
					}
				}
				if (!include) {
					BaseItem item = new BaseItem();
					item.setName(t.getName() + "[" + t.getProvince() + t.getCity() + "]");
					item.setValue(t.getTenantId());
					unselected.add(item);
				}
			}
		}

		request.setAttribute("selected", selected);
		request.setAttribute("unselected", unselected);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("tenantGrant.privilege");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/tenantGrant/privilege", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("tenantGrant.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/sys/tenantGrant/query", modelMap);
	}

	@RequestMapping("/save")
	public ModelAndView save(HttpServletRequest request, ModelMap modelMap) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);

		TenantGrant tenantGrant = new TenantGrant();
		Tools.populate(tenantGrant, params);

		tenantGrant.setGrantee(request.getParameter("grantee"));
		tenantGrant.setPrivilege(request.getParameter("privilege"));
		tenantGrant.setTenantId(request.getParameter("tenantId"));
		tenantGrant.setType(request.getParameter("type"));
		tenantGrant.setCreateBy(actorId);

		tenantGrantService.save(tenantGrant);

		return this.list(request, modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveAll")
	public byte[] saveAll(HttpServletRequest request) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		String type = request.getParameter("type");
		String userId = request.getParameter("userId");
		String objectIds = request.getParameter("objectIds");
		String privilege = request.getParameter("privilege");
		try {
			List<String> tenantIds = StringTools.split(objectIds);
			List<TenantGrant> list = new ArrayList<TenantGrant>();
			for (String tenantId : tenantIds) {
				TenantGrant tenantGrant = new TenantGrant();
				tenantGrant.setGrantee(userId);
				tenantGrant.setPrivilege(privilege);
				tenantGrant.setTenantId(tenantId);
				tenantGrant.setType(type);
				tenantGrant.setCreateBy(actorId);
				list.add(tenantGrant);
			}
			tenantGrantService.saveAll(userId, type, privilege, list);
			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	public @ResponseBody TenantGrant saveOrUpdate(HttpServletRequest request, @RequestBody Map<String, Object> model) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		TenantGrant tenantGrant = new TenantGrant();
		try {
			Tools.populate(tenantGrant, model);
			tenantGrant.setGrantee(ParamUtils.getString(model, "grantee"));
			tenantGrant.setPrivilege(ParamUtils.getString(model, "privilege"));
			tenantGrant.setTenantId(ParamUtils.getString(model, "tenantId"));
			tenantGrant.setType(ParamUtils.getString(model, "type"));
			tenantGrant.setCreateBy(actorId);
			this.tenantGrantService.save(tenantGrant);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return tenantGrant;
	}

	@ResponseBody
	@RequestMapping("/saveTenantGrant")
	public byte[] saveTenantGrant(HttpServletRequest request) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		TenantGrant tenantGrant = new TenantGrant();
		try {
			Tools.populate(tenantGrant, params);
			tenantGrant.setGrantee(request.getParameter("grantee"));
			tenantGrant.setPrivilege(request.getParameter("privilege"));
			tenantGrant.setTenantId(request.getParameter("tenantId"));
			tenantGrant.setType(request.getParameter("type"));
			tenantGrant.setCreateBy(actorId);
			this.tenantGrantService.save(tenantGrant);

			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource
	public void setSysTenantService(SysTenantService sysTenantService) {
		this.sysTenantService = sysTenantService;
	}

	@javax.annotation.Resource
	public void setTenantGrantService(TenantGrantService tenantGrantService) {
		this.tenantGrantService = tenantGrantService;
	}

}
