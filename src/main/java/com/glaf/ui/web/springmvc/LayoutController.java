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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.RequestUtils;
import com.glaf.ui.model.Layout;
import com.glaf.ui.model.Panel;
import com.glaf.ui.model.PanelInstance;
import com.glaf.ui.model.UserPanel;
import com.glaf.ui.service.LayoutService;
import com.glaf.ui.service.PanelService;

@Controller("/ui/layout")
@RequestMapping("/ui/layout")
public class LayoutController {
	protected final static Log logger = LogFactory.getLog(LayoutController.class);

	protected final static String SHOW_ACTION = "redirect:/ui/layout";

	protected PanelService panelService;

	protected LayoutService layoutService;

	@RequestMapping("/save")
	public ModelAndView save(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		logger.debug(RequestUtils.getParameterMap(request));

		String actorId = loginContext.getActorId();

		String layoutName = request.getParameter("layoutName");

		String isSystem = request.getParameter("isSystem");

		if (loginContext.isSystemAdministrator() && StringUtils.equals(isSystem, "true")) {
			actorId = "system";
		}

		Layout layout = layoutService.getLayoutByName(layoutName);

		UserPanel userPanel = new UserPanel();
		userPanel.setActorId(actorId);
		userPanel.setLayout(layout);
		userPanel.setLayoutName(layoutName);
		userPanel.setCreateDate(new Date());
		userPanel.setDashboardId(RequestUtils.getString(request, "dashboardId", "default"));

		Enumeration<?> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String paramName = (String) enumeration.nextElement();
			if (StringUtils.isNotEmpty(paramName) && paramName.startsWith("x_grid_")) {
				paramName = paramName.replaceAll("x_grid_", "");
				String paramValue = RequestUtils.getParameter(request, "panel_" + paramName);
				if (StringUtils.isNotEmpty(paramValue)) {
					Panel panel = panelService.getPanelByName(paramValue);
					PanelInstance model = new PanelInstance();
					model.setName(paramName);
					model.setPanel(panel);
					model.setUserPanel(userPanel);
					userPanel.getPanelInstances().add(model);
				}
			}
		}

		panelService.saveUserPanel(userPanel);

		return showLayout(request, modelMap);
	}

	@javax.annotation.Resource
	public void setLayoutService(LayoutService layoutService) {
		this.layoutService = layoutService;
	}

	@javax.annotation.Resource
	public void setPanelService(PanelService panelService) {
		this.panelService = panelService;
	}

	@RequestMapping
	public ModelAndView showLayout(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		String actorId = loginContext.getActorId();
		String isSystem = request.getParameter("isSystem");
		if (loginContext.isSystemAdministrator() && StringUtils.equals(isSystem, "true")) {
			actorId = "system";
		}

		// dashboardId获取分类
		String dashboardId = RequestUtils.getString(request, "dashboardId", "default");
		request.setAttribute("dashboardId", dashboardId);

		UserPanel userPanel = panelService.getUserPanelByDashboardId(actorId, dashboardId);
		if (userPanel == null) {
			userPanel = panelService.getUserPanel("system");
		}

		if (userPanel != null && userPanel.getLayout() != null) {
			if (userPanel.getLayout().getTemplateId() != null) {

			}
		}

		request.setAttribute("userPanel", userPanel);

		List<Layout> layouts = layoutService.getAllLayouts();
		modelMap.put("layouts", layouts);

		List<Panel> panels = new java.util.ArrayList<Panel>();

		// 添加系统设置
		List<Panel> panels01 = panelService.getPanels("system");
		if (panels01 != null && panels01.size() > 0) {
			for (int i = 0; i < panels01.size(); i++) {
				Panel panel = panels01.get(i);
				if (panel.getLocked() == 0) {
					panels.add(panel);
				}
			}
		}
		// 添加用户自己设置
		if (!(loginContext.isSystemAdministrator() && StringUtils.equals(isSystem, "true"))) {
			List<Panel> panels02 = panelService.getPanels(actorId);
			if (panels02 != null && panels02.size() > 0) {
				for (int i = 0; i < panels02.size(); i++) {
					Panel panel = panels02.get(i);
					if (panel.getLocked() == 0) {
						panels.add(panel);
					}
				}
			}
		}

		// 添加当前角色权限的设置
		Collection<String> roles = loginContext.getRoles();
		if (roles != null && roles.size() > 0) {
			Iterator<String> iterator = roles.iterator();
			List<String> rIds = new ArrayList<String>();
			while (iterator.hasNext()) {
				String type = (String) iterator.next();
				rIds.add(type);
			}
			List<Panel> panels03 = panelService.getPanelsByRole(rIds);
			if (panels03 != null && panels03.size() > 0) {
				for (int i = 0; i < panels03.size(); i++) {
					Panel panel = panels03.get(i);
					if (panel.getLocked() == 0) {
						panels.add(panel);
					}
				}
			}
		}

		modelMap.put("panels", panels);

		String jx_view = request.getParameter("jx_view");

		if (StringUtils.isNotEmpty(jx_view)) {
			return new ModelAndView(jx_view, modelMap);
		}

		String x_view = ViewProperties.getString("ui_layout.showLayout");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/ui/layout/showLayout", modelMap);
	}

}