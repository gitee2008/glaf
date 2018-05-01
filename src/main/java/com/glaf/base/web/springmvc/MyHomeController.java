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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import org.springframework.web.servlet.ModelAndView;

import com.glaf.base.modules.sys.model.SysApplication;
import com.glaf.base.modules.sys.model.SysTenant;
import com.glaf.base.modules.sys.model.TenantConfig;
import com.glaf.base.modules.sys.service.SysApplicationService;
import com.glaf.base.modules.sys.service.SysTenantService;
import com.glaf.base.modules.sys.service.TenantConfigService;
import com.glaf.base.modules.sys.service.TreePermissionService;
import com.glaf.core.base.BaseTree;
import com.glaf.core.base.TreeModel;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.security.LoginContext;
import com.glaf.core.tree.component.TreeComponent;
import com.glaf.core.tree.component.TreeRepository;
import com.glaf.core.tree.helper.TreeRepositoryBuilder;
import com.glaf.core.util.RequestUtils;

@Controller("/my/home")
@RequestMapping("/my/home")
public class MyHomeController {
	protected static final Log logger = LogFactory.getLog(MyHomeController.class);

	protected SysApplicationService sysApplicationService;

	protected SysTenantService sysTenantService;

	protected TenantConfigService tenantConfigService;

	protected TreePermissionService treePermissionService;

	@RequestMapping
	public ModelAndView home(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws Exception {
		logger.debug("-----------------------home page-------------------------");

		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return null;
		}

		RequestUtils.setRequestParameterToAttribute(request);
		String context = request.getContextPath();
		request.setAttribute("contextPath", context);
		String theme = RequestUtils.getTheme(request);
		request.setAttribute("theme", theme);
		request.setAttribute("sys_title", SystemConfig.getString("res_system_name"));
		request.setAttribute("username", loginContext.getUser().getName());

		TenantConfig tenantConfig = tenantConfigService.getTenantConfigByTenantId(loginContext.getTenantId());
		if (tenantConfig != null) {
			if (StringUtils.isNotEmpty(tenantConfig.getSysName())) {
				request.setAttribute("sys_title", tenantConfig.getSysName());
			}
		}

		String res_logo = SystemConfig.getString("res_logo");
		request.setAttribute("res_logo", res_logo);

		long parentId = RequestUtils.getLong(request, "parentId", 3);

		List<SysApplication> apps = null;
		List<Long> nodeIds = null;
		if (loginContext.isSystemAdministrator()) {
			apps = sysApplicationService.getApplicationListWithChildren(parentId);
		} else {
			apps = sysApplicationService.getSysApplicationByUserId(loginContext.getActorId());

			SysTenant tenant = sysTenantService.getSysTenantByTenantId(loginContext.getTenantId());
			if (tenant != null && StringUtils.isNotEmpty(tenant.getType())) {
				nodeIds = treePermissionService.getTenantNodeIds(tenant.getTenantId(), tenant.getType());
			}

		}

		List<SysApplication> allApps = sysApplicationService.getAllSysApplications();

		SysApplication root = sysApplicationService.findById(parentId);
		if (root != null && apps != null && !apps.isEmpty()) {

			Map<Long, Long> lockedMap = new LinkedHashMap<Long, Long>();
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
				if (app.getId() == root.getId()) {
					continue;
				}

				if (nodeIds != null && !nodeIds.isEmpty()) {
					if (!nodeIds.contains(app.getId())) {
						continue;
					}
				}

				if (!lockedMap.containsKey(app.getId())) {
					if (app.getLocked() == 0 && app.getDeleteFlag() == 0) {
						myapps2.add(app);
					}
				}
			}

			List<TreeModel> treeModels = new ArrayList<TreeModel>();
			if (myapps2 != null && !myapps2.isEmpty()) {
				for (SysApplication app : myapps2) {
					TreeModel tree = new BaseTree();
					String name = app.getName();
					if (name.length() > 10) {
						name = name.substring(0, 10) + "...";
					}
					tree.setId(app.getId());
					tree.setParentId(app.getParentId());
					tree.setName(name);
					tree.setCode(app.getCode());
					tree.setTreeId(app.getTreeId());
					tree.setIcon(app.getIcon());
					tree.setIconCls(app.getIcon());
					tree.setDiscriminator(app.getDiscriminator());
					tree.setSortNo(app.getSort());
					tree.setLevel(app.getLevel());
					tree.setLocked(app.getLocked());
					Map<String, Object> dataMap = new HashMap<String, Object>();
					String url = app.getUrl();
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
					tree.setUrl(url);
					tree.setDataMap(dataMap);
					treeModels.add(tree);
				}
				TreeRepositoryBuilder builder = new TreeRepositoryBuilder();
				TreeRepository treeRepository = builder.build(treeModels);
				List<TreeComponent> topTrees = treeRepository.getTopTrees();
				java.util.Collections.sort(topTrees);
				request.setAttribute("topTrees", topTrees);
			}
		}

		String view = "/my/home";
		return new ModelAndView(view, modelMap);
	}

	@javax.annotation.Resource
	public void setSysApplicationService(SysApplicationService sysApplicationService) {
		this.sysApplicationService = sysApplicationService;
	}

	@javax.annotation.Resource
	public void setSysTenantService(SysTenantService sysTenantService) {
		this.sysTenantService = sysTenantService;
	}

	@javax.annotation.Resource
	public void setTenantConfigService(TenantConfigService tenantConfigService) {
		this.tenantConfigService = tenantConfigService;
	}

	@javax.annotation.Resource
	public void setTreePermissionService(TreePermissionService treePermissionService) {
		this.treePermissionService = treePermissionService;
	}

}
