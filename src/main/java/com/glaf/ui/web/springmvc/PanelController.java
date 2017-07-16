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
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.glaf.core.config.ViewProperties;
import com.glaf.core.identity.Role;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.Tools;
import com.glaf.core.util.UUID32;
import com.glaf.ui.model.Panel;
import com.glaf.ui.model.PanelButton;
import com.glaf.ui.model.PanelPermission;
import com.glaf.ui.query.PanelPermissionQuery;
import com.glaf.ui.service.LayoutService;
import com.glaf.ui.service.PanelButtonService;
import com.glaf.ui.service.PanelPermissionService;
import com.glaf.ui.service.PanelService;

@Controller("/ui/panel")
@RequestMapping("/ui/panel")
public class PanelController {
	protected final static Log logger = LogFactory.getLog(PanelController.class);

	protected PanelService panelService;

	protected LayoutService layoutService;

	protected PanelPermissionService panelPermissionService;

	protected PanelButtonService panelButtonService;

	@RequestMapping("/content")
	public ModelAndView content(@RequestParam(value = "pid", required = true) String panelId, ModelMap modelMap) {

		Panel panel = null;

		if (StringUtils.isNotEmpty(panelId)) {
			panel = panelService.getPanel(panelId);
		}

		if (panel != null) {
			modelMap.addAttribute("panel", panel);
		}

		String x_view = ViewProperties.getString("sys_panel.content");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/ui/panel/content", modelMap);

	}

	@RequestMapping("/edit")
	public ModelAndView edit(@RequestParam(value = "panelId", required = false) String panelId, ModelMap modelMap,
			HttpServletRequest request) {

		Panel panel = null;

		if (StringUtils.isNotEmpty(panelId)) {
			panel = panelService.getPanel(panelId);
			PanelPermissionQuery query = new PanelPermissionQuery();
			query.setPanelId(panel.getId());
			List<PanelPermission> list = panelPermissionService.list(query);
			if (list != null && !list.isEmpty()) {
				List<String> roleIds = new ArrayList<String>();
				for (PanelPermission p : list) {
					if (p.getRoleId() != null) {
						roleIds.add(p.getRoleId());
					}
				}
				logger.debug("roleIds:" + roleIds);
				List<Role> roles = IdentityFactory.getRoles();
				// logger.debug("roles:" + roles);
				if (roles != null && !roles.isEmpty()) {
					StringBuilder buffer = new StringBuilder();
					List<Role> rows = new ArrayList<Role>();
					for (Role r : roles) {
						if (roleIds.contains(String.valueOf(r.getId()))) {
							rows.add(r);
							buffer.append(r.getId()).append(",");
						}
					}
					logger.debug("selected:" + buffer.toString());
					if (!rows.isEmpty()) {
						request.setAttribute("roles", rows);
						request.setAttribute("selected", buffer.toString());
					}
				}
			}

			List<PanelButton> panelButtons = panelButtonService.getPanelButtonByParentId(panel.getId());
			if (panelButtons != null && panelButtons.size() > 0) {
				request.setAttribute("panelButton", panelButtons.get(0));
			}
		}
		request.setAttribute("panel", panel);

		String x_view = ViewProperties.getString("sys_panel.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/ui/panel/edit", modelMap);
	}

	@RequestMapping
	public ModelAndView list(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		List<Panel> panels = new ArrayList<Panel>();
		String actorId = loginContext.getActorId();
		String isSystem = request.getParameter("isSystem");

		if (loginContext.isSystemAdministrator() && StringUtils.equals(isSystem, "true")) {
			panels = panelService.getSystemPanels();
		}
		List<Panel> panels2 = panelService.getPanels(actorId);
		if (panels2 != null && panels2.size() > 0) {
			panels.addAll(panels2);
		}
		modelMap.put("panels", panels);

		String jx_view = request.getParameter("jx_view");

		if (StringUtils.isNotEmpty(jx_view)) {
			return new ModelAndView(jx_view, modelMap);
		}

		String x_view = ViewProperties.getString("sys_panel.list");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/ui/panel/list", modelMap);
	}

	@RequestMapping("/save")
	public ModelAndView save(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug(params);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();

		String isSystem = request.getParameter("isSystem");
		String isGobal = RequestUtils.getString(request, "isGobal", "false");
		if (loginContext.isSystemAdministrator() && StringUtils.equals(isSystem, "true")
				&& StringUtils.equals(isGobal, "true")) {
			actorId = "system";
		}

		Panel panel = new Panel();
		Tools.populate(panel, params);
		Panel model = null;
		if (panel.getId() != null) {
			model = panelService.getPanel(panel.getId());
		}
		if (model == null) {
			if ((loginContext.isSystemAdministrator() && StringUtils.equals(isSystem, "true"))) {
				if (StringUtils.isEmpty(panel.getName())) {
					panel.setName("sys_panel_" + UUID32.getUUID());
				}
			} else {
				panel.setName("user_panel_" + UUID32.getUUID());
			}
			panel.setActorId(actorId);
			panelService.savePanel(panel);
		} else {
			panel.setActorId(actorId);
			if (StringUtils.equals(panel.getActorId(), actorId)) {
				logger.debug(panel);
				panelService.updatePanel(panel);
			} else if ((loginContext.isSystemAdministrator() && StringUtils.equals(isSystem, "true"))
					&& StringUtils.equals(isGobal, "true")) {
				logger.debug(panel);
				panelService.updatePanel(panel);
			} else {
				logger.debug(panel);
				panelService.updatePanel(panel);
			}
		}
		// 更新权限
		String roleIds = RequestUtils.getString(request, "prem");
		if (roleIds != null) {
			StringTokenizer rids = new StringTokenizer(roleIds, ",");
			PanelPermission panelPermission = null;
			List<PanelPermission> panelPermissions = new ArrayList<PanelPermission>();
			while (rids.hasMoreElements()) {
				String roleId = (String) rids.nextElement();
				panelPermission = new PanelPermission();
				panelPermission.setPanelId(panel.getId());
				panelPermission.setRoleId(roleId);
				panelPermission.setCreateBy(actorId);
				panelPermission.setCreateDate(new Date());
				panelPermissions.add(panelPermission);
			}
			panelPermissionService.saveAll(panel.getId(), panelPermissions);
		} else {
			panelPermissionService.saveAll(panel.getId(), null);
		}

		// 保存自定义按钮
		if (panel.getCustom() == 1) {
			PanelButton panelButton = new PanelButton();
			panelButton.setId(RequestUtils.getString(request, "panelButtonId", null));
			panelButton.setOpenType(RequestUtils.getInt(request, "openType", 0));
			panelButton.setImgUrl(RequestUtils.getString(request, "imgUrl", null));
			panelButton.setHref(RequestUtils.getString(request, "href", null));
			panelButton.setPid(Integer.parseInt(panel.getId()));
			if (panelButton.getImgUrl() != null && panelButton.getHref() != null) {
				panelButtonService.save(panelButton);
			}
		}

		return this.list(request, modelMap);
	}

	@javax.annotation.Resource
	public void setLayoutService(LayoutService layoutService) {
		this.layoutService = layoutService;
	}

	@javax.annotation.Resource
	public void setPanelService(PanelService panelService) {
		this.panelService = panelService;
	}

	@javax.annotation.Resource
	public void setPanelPermissionService(PanelPermissionService panelPermissionService) {
		this.panelPermissionService = panelPermissionService;
	}

	@javax.annotation.Resource
	public void setPanelButtonService(PanelButtonService panelButtonService) {
		this.panelButtonService = panelButtonService;
	}

}