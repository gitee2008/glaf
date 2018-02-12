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

package com.glaf.base.web.springmvc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.glaf.base.modules.sys.model.SysApplication;
import com.glaf.base.modules.sys.model.TenantConfig;
import com.glaf.base.modules.sys.service.SysApplicationService;
import com.glaf.base.modules.sys.service.TenantConfigService;
import com.glaf.base.utils.LoginContextUtil;

import com.glaf.core.base.BaseTree;
import com.glaf.core.base.TreeModel;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.security.LoginContext;
import com.glaf.core.tree.helper.TreeHelper;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;

import com.glaf.ui.model.UserTheme;
import com.glaf.ui.query.UserThemeQuery;
import com.glaf.ui.service.ThemeService;
import com.glaf.ui.service.UserThemeService;

@Controller("/my/main")
@RequestMapping("/my/main")
public class MyMainController {
	protected static final Log logger = LogFactory.getLog(MyMainController.class);

	protected ThemeService themeService;

	protected UserThemeService userThemeService;

	protected TenantConfigService tenantConfigService;

	protected SysApplicationService sysApplicationService;

	@RequestMapping("/content")
	public ModelAndView content(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		String context = request.getContextPath();
		request.setAttribute("contextPath", context);
		request.setAttribute("homeTheme", "default");

		String indexUrl = LoginContextUtil.getIndexUrl(request);
		if (!(StringUtils.startsWithIgnoreCase(indexUrl, "http://")
				|| StringUtils.startsWithIgnoreCase(indexUrl, "https://"))) {
			indexUrl = request.getContextPath() + indexUrl;
		}
		request.setAttribute("indexUrl", indexUrl);
		request.setAttribute("tabmax", SystemConfig.getString("tabmax"));

		long parentId = RequestUtils.getLong(request, "parentId", 3);

		List<SysApplication> apps = null;
		if (loginContext.isSystemAdministrator()) {
			apps = sysApplicationService.getApplicationListWithChildren(parentId);
		} else {
			apps = sysApplicationService.getSysApplicationByUserId(actorId);
		}

		List<SysApplication> allApps = sysApplicationService.getAllSysApplications();

		SysApplication root = sysApplicationService.findById(parentId);
		if (root != null && apps != null && !apps.isEmpty()) {

			Map<Long, Long> lockedMap = new HashMap<Long, Long>();
			for (SysApplication app : allApps) {
				if (app.getLocked() != 0) {
					lockedMap.put(app.getId(), app.getId());
				}
				if (app.getDeleteFlag() != 0) {
					lockedMap.put(app.getId(), app.getId());
				}
			}

			for (int i = 0; i <= 10; i++) {
				for (SysApplication app : allApps) {
					if (lockedMap.containsKey(app.getParentId())) {
						lockedMap.put(app.getId(), app.getParentId());
					}
				}
			}

			List<SysApplication> myapps2 = new ArrayList<SysApplication>();
			for (SysApplication app : apps) {
				if (!lockedMap.containsKey(app.getId())) {
					if (app.getLocked() == 0 && app.getDeleteFlag() == 0) {
						myapps2.add(app);
					}
				}
			}

			List<TreeModel> treeModels = new ArrayList<TreeModel>();
			if (myapps2 != null && !myapps2.isEmpty()) {
				for (SysApplication p : myapps2) {
					TreeModel tree = new BaseTree();
					String name = p.getName();
					if (name.length() > 10) {
						name = name.substring(0, 10) + "...";
					}
					tree.setId(p.getId());
					tree.setParentId(p.getParentId());
					tree.setName(name);
					tree.setTreeId(p.getTreeId());
					tree.setIcon(p.getIcon());
					tree.setIconCls(p.getIcon());
					tree.setDiscriminator(p.getDiscriminator());
					tree.setCode(p.getCode());
					tree.setSortNo(p.getSort());
					tree.setLevel(p.getLevel());
					tree.setLocked(p.getLocked());
					Map<String, Object> dataMap = new HashMap<String, Object>();
					String url = p.getUrl();
					if (StringUtils.isNotEmpty(url)) {
						if (!(url.toLowerCase().startsWith("http://") || url.toLowerCase().startsWith("https://"))) {
							if (url.startsWith("/")) {
								url = request.getContextPath() + url;
							} else {
								url = request.getContextPath() + "/" + url;
							}
						}
						if (url.indexOf("?") != -1) {
							url = url + "&time=" + System.currentTimeMillis();
						} else {
							url = url + "?time=" + System.currentTimeMillis();
						}
					}
					dataMap.put("url", url);
					tree.setDataMap(dataMap);
					treeModels.add(tree);
				}
				TreeHelper treeHelper = new TreeHelper();
				JSONObject treeJson = treeHelper.getTreeJson(root, treeModels);
				modelMap.put("treeJson", treeJson);
				modelMap.put("json", treeJson.toJSONString());
				request.setAttribute("scripts", treeJson.toJSONString());
			}
		}

		String style = request.getParameter("style");
		String url = "/my/portal/default/content";
		if (style != null && style.trim().length() > 0) {
			url = "/my/portal/" + style + "/content";
		}
		return new ModelAndView(url, modelMap);
	}

	@RequestMapping("/footer")
	public ModelAndView foot(HttpServletRequest request, ModelMap modelMap) {
		String context = request.getContextPath();
		request.setAttribute("contextPath", context);
		request.setAttribute("homeTheme", "default");
		request.setAttribute("res_copyright", SystemConfig.getString("res_copyright"));
		request.setAttribute("curr_date", new SimpleDateFormat(" yyyy-MM-dd EEEE  ").format(new Date()));

		String style = request.getParameter("style");
		String url = "/my/portal/default/footer";
		if (style != null && style.trim().length() > 0) {
			url = "/my/portal/" + style + "/footer";
		}
		return new ModelAndView(url, modelMap);
	}

	@RequestMapping("/header")
	public ModelAndView header(HttpServletRequest request, ModelMap modelMap) {
		String context = request.getContextPath();
		request.setAttribute("contextPath", context);
		request.setAttribute("homeTheme", "default");

		String style = request.getParameter("style");
		String url = "/my/portal/default/header";
		if (style != null && style.trim().length() > 0) {
			url = "/my/portal/" + style + "/header";
		}
		return new ModelAndView(url, modelMap);
	}

	@RequestMapping("/left")
	public ModelAndView left(HttpServletRequest request, ModelMap modelMap) {
		String context = request.getContextPath();
		request.setAttribute("contextPath", context);
		request.setAttribute("homeTheme", "default");

		String style = request.getParameter("style");
		String url = "/my/portal/default/left";
		if (style != null && style.trim().length() > 0) {
			url = "/my/portal/" + style + "/left";
		}
		return new ModelAndView(url, modelMap);
	}

	@RequestMapping
	public ModelAndView mainPage(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws Exception {
		logger.debug("-----------------------main page-------------------------");

		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return null;
		}
		RequestUtils.setRequestParameterToAttribute(request);
		UserThemeQuery userThemeQuery = new UserThemeQuery();
		String userId = RequestUtils.getActorId(request);
		userThemeQuery.setActorId(userId);
		List<UserTheme> userThemes = null;
		try {
			userThemes = userThemeService.list(userThemeQuery);
		} catch (Exception ex) {
		}
		UserTheme userTheme = new UserTheme();
		if (userThemes != null && userThemes.size() > 0) {
			userTheme = userThemes.get(0);
		}
		request.setAttribute("userTheme", userTheme);

		String context = request.getContextPath();

		request.setAttribute("contextPath", context);
		String theme = RequestUtils.getTheme(request);
		request.setAttribute("theme", theme);
		String homeTheme = RequestUtils.getHomeTheme(request);
		request.setAttribute("homeTheme", homeTheme);
		request.setAttribute("sys_title", SystemConfig.getString("res_system_name"));

		TenantConfig tenantConfig = tenantConfigService.getTenantConfigByTenantId(loginContext.getTenantId());
		if (tenantConfig != null) {
			if (StringUtils.isNotEmpty(tenantConfig.getSysName())) {
				request.setAttribute("sys_title", tenantConfig.getSysName());
			}
		}

		String style = request.getParameter("style");
		String url = "/my/portal/default/main";
		if (style != null && style.trim().length() > 0) {
			url = "/my/portal/" + style + "/main";
		}
		return new ModelAndView(url, modelMap);
	}

	@javax.annotation.Resource
	public void setSysApplicationService(SysApplicationService sysApplicationService) {
		this.sysApplicationService = sysApplicationService;
	}

	@javax.annotation.Resource
	public void setTenantConfigService(TenantConfigService tenantConfigService) {
		this.tenantConfigService = tenantConfigService;
	}

	@RequestMapping("/setTheme")
	@ResponseBody
	public byte[] setTheme(HttpServletRequest request, HttpServletResponse response) {
		UserTheme userTheme = new UserTheme();
		userTheme.setId(RequestUtils.getInteger(request, "userThemeId", null));
		userTheme.setBackground(RequestUtils.getString(request, "background", ""));
		userTheme.setBackgroundType(RequestUtils.getString(request, "backgroundType", ""));
		userTheme.setCreateBy(RequestUtils.getActorId(request));
		userTheme.setCreateDate(new Date());
		userTheme.setLayoutModel(RequestUtils.getString(request, "layoutModel", ""));
		userTheme.setThemeStyle(RequestUtils.getString(request, "themeStyle", ""));
		userTheme.setHomeThemeStyle(RequestUtils.getString(request, "homeThemeStyle", ""));
		if (userTheme.getId() != null) {
			UserTheme oldUserTheme = userThemeService.getUserTheme(userTheme.getId());
			userTheme.setCourse(oldUserTheme.getLayoutModel().equals(userTheme.getLayoutModel()) ? 1 : 0);
		}
		try {
			userThemeService.save(userTheme);
			RequestUtils.setTheme(request, response, userTheme.getLayoutModel(), userTheme.getThemeStyle(),
					userTheme.getHomeThemeStyle());
			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			// ex.printStackTrace();
			logger.error("设置主题错误:" + ex.getMessage());
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource
	public void setThemeService(ThemeService themeService) {
		this.themeService = themeService;
	}

	@javax.annotation.Resource
	public void setUserThemeService(UserThemeService userThemeService) {
		this.userThemeService = userThemeService;
	}

	@RequestMapping("/top")
	public ModelAndView top(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		String context = request.getContextPath();
		request.setAttribute("contextPath", context);
		request.setAttribute("homeTheme", "default");
		request.setAttribute("username", loginContext.getUser().getName());
		request.setAttribute("sys_title", SystemConfig.getString("res_system_name"));

		TenantConfig tenantConfig = tenantConfigService.getTenantConfigByTenantId(loginContext.getTenantId());
		if (tenantConfig != null) {
			if (StringUtils.isNotEmpty(tenantConfig.getSysName())) {
				request.setAttribute("sys_title", tenantConfig.getSysName());
			}
		}

		String style = request.getParameter("style");
		String url = "/my/portal/default/top";
		if (style != null && style.trim().length() > 0) {
			url = "/my/portal/" + style + "/top";
		}
		return new ModelAndView(url, modelMap);
	}
}
