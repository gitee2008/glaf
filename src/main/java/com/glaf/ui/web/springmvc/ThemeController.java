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
package com.glaf.ui.web.springmvc;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.glaf.core.base.DataRequest;
import com.glaf.core.base.DataRequest.SortDescriptor;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.identity.User;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.PageResult;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;

import com.glaf.ui.model.Theme;
import com.glaf.ui.query.ThemeQuery;
import com.glaf.ui.service.ThemeService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/ui/theme")
@RequestMapping("/ui/theme")
public class ThemeController {
	protected static final Log logger = LogFactory.getLog(ThemeController.class);

	protected ThemeService themeService;

	public ThemeController() {

	}

	@ResponseBody
	@RequestMapping("/delete")
	public byte[] delete(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Integer id = RequestUtils.getInteger(request, "id");
		String ids = request.getParameter("ids");
		if (StringUtils.isNotEmpty(ids)) {
			StringTokenizer token = new StringTokenizer(ids, ",");
			while (token.hasMoreTokens()) {
				String x = token.nextToken();
				if (StringUtils.isNotEmpty(x)) {
					Theme theme = themeService.getTheme(Integer.valueOf(x));
					if (theme != null && (StringUtils.equals(theme.getCreateBy(), loginContext.getActorId())
							|| loginContext.isSystemAdministrator())) {
						// theme.setDeleteFlag(1);
						themeService.save(theme);
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			Theme theme = themeService.getTheme(Integer.valueOf(id));
			if (theme != null && (StringUtils.equals(theme.getCreateBy(), loginContext.getActorId())
					|| loginContext.isSystemAdministrator())) {
				// theme.setDeleteFlag(1);
				themeService.save(theme);
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		request.removeAttribute("canSubmit");

		Theme theme = themeService.getTheme(RequestUtils.getInt(request, "id"));
		if (theme != null) {
			request.setAttribute("theme", theme);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("theme.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/ui/theme/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		ThemeQuery query = new ThemeQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);
		/**
		 * 此处业务逻辑需自行调整
		 */
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
		int total = themeService.getThemeCountByQueryCriteria(query);
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

			List<Theme> list = themeService.getThemesByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (Theme theme : list) {
					JSONObject rowJSON = (JSONObject) JSON.toJSON(theme);
					rowJSON.put("id", theme.getId());
					rowJSON.put("rowId", theme.getId());
					rowJSON.put("themeId", theme.getId());
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

		return new ModelAndView("/ui/theme/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("theme.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/ui/theme/query", modelMap);
	}

	@RequestMapping("/read")
	@ResponseBody
	public byte[] read(HttpServletRequest request, @RequestBody DataRequest dataRequest) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		ThemeQuery query = new ThemeQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);
		query.setDataRequest(dataRequest);
		// ThemeDomainFactory.processDataRequest(dataRequest);

		/**
		 * 此处业务逻辑需自行调整
		 */
		if (!loginContext.isSystemAdministrator()) {
			String actorId = loginContext.getActorId();
			query.createBy(actorId);
		}

		int start = 0;
		int limit = PageResult.DEFAULT_PAGE_SIZE;

		int pageNo = dataRequest.getPage();
		limit = dataRequest.getPageSize();

		start = (pageNo - 1) * limit;

		if (start < 0) {
			start = 0;
		}

		if (limit <= 0) {
			limit = PageResult.DEFAULT_PAGE_SIZE;
		}

		JSONObject result = new JSONObject();
		int total = themeService.getThemeCountByQueryCriteria(query);
		if (total > 0) {
			result.put("total", total);
			result.put("totalCount", total);
			result.put("totalRecords", total);
			result.put("start", start);
			result.put("startIndex", start);
			result.put("limit", limit);
			result.put("pageSize", limit);

			String orderName = null;
			String order = null;

			if (dataRequest.getSort() != null && !dataRequest.getSort().isEmpty()) {
				SortDescriptor sort = dataRequest.getSort().get(0);
				orderName = sort.getField();
				order = sort.getDir();
				logger.debug("orderName:" + orderName);
				logger.debug("order:" + order);
			}

			if (StringUtils.isNotEmpty(orderName)) {
				query.setSortColumn(orderName);
				if (StringUtils.equals(order, "desc")) {
					query.setSortOrder(" desc ");
				}
			}

			List<Theme> list = themeService.getThemesByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (Theme theme : list) {
					JSONObject rowJSON = (JSONObject) JSON.toJSON(theme);
					rowJSON.put("id", theme.getId());
					rowJSON.put("themeId", theme.getId());
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

	@RequestMapping("/save")
	public ModelAndView save(HttpServletRequest request, ModelMap modelMap) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		params.remove("status");
		params.remove("wfStatus");

		Theme theme = new Theme();
		Tools.populate(theme, params);

		theme.setThemeStyle(request.getParameter("themeStyle"));
		theme.setLayoutModel(request.getParameter("layoutModel"));
		theme.setBackground(request.getParameter("background"));
		theme.setBackgroundType(request.getParameter("backgroundType"));
		theme.setLocked(RequestUtils.getInt(request, "locked"));
		theme.setCreateBy(actorId);

		themeService.save(theme);

		return this.list(request, modelMap);
	}

	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	public @ResponseBody Theme saveOrUpdate(HttpServletRequest request, @RequestBody Map<String, Object> model) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		Theme theme = new Theme();
		try {
			Tools.populate(theme, model);
			theme.setThemeStyle(ParamUtils.getString(model, "themeStyle"));
			theme.setLayoutModel(ParamUtils.getString(model, "layoutModel"));
			theme.setBackground(ParamUtils.getString(model, "background"));
			theme.setBackgroundType(ParamUtils.getString(model, "backgroundType"));
			theme.setLocked(ParamUtils.getInt(model, "locked"));
			theme.setCreateBy(ParamUtils.getString(model, "createBy"));
			theme.setCreateDate(ParamUtils.getDate(model, "createDate"));
			theme.setCreateBy(actorId);
			this.themeService.save(theme);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return theme;
	}

	@ResponseBody
	@RequestMapping("/saveTheme")
	public byte[] saveTheme(HttpServletRequest request) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		Theme theme = new Theme();
		try {
			Tools.populate(theme, params);
			theme.setThemeStyle(request.getParameter("themeStyle"));
			theme.setLayoutModel(request.getParameter("layoutModel"));
			theme.setBackground(request.getParameter("background"));
			theme.setBackgroundType(request.getParameter("backgroundType"));
			theme.setLocked(RequestUtils.getInt(request, "locked"));

			theme.setCreateDate(RequestUtils.getDate(request, "createDate"));
			theme.setCreateBy(actorId);
			this.themeService.save(theme);

			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource(name = "com.glaf.ui.service.themeService")
	public void setThemeService(ThemeService themeService) {
		this.themeService = themeService;
	}

	@RequestMapping("/update")
	public ModelAndView update(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		params.remove("status");
		params.remove("wfStatus");

		Theme theme = themeService.getTheme(RequestUtils.getInt(request, "id"));

		Tools.populate(theme, params);

		theme.setThemeStyle(request.getParameter("themeStyle"));
		theme.setLayoutModel(request.getParameter("layoutModel"));
		theme.setBackground(request.getParameter("background"));
		theme.setBackgroundType(request.getParameter("backgroundType"));
		theme.setLocked(RequestUtils.getInt(request, "locked"));

		themeService.save(theme);

		return this.list(request, modelMap);
	}

	@RequestMapping("/view")
	public ModelAndView view(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		Theme theme = themeService.getTheme(RequestUtils.getInt(request, "id"));
		request.setAttribute("theme", theme);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view);
		}

		String x_view = ViewProperties.getString("theme.view");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view);
		}

		return new ModelAndView("/ui/theme/view");
	}

}
