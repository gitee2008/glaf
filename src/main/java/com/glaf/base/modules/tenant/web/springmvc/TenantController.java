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

package com.glaf.base.modules.tenant.web.springmvc;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.glaf.base.modules.sys.model.SysTenant;
import com.glaf.base.modules.sys.service.SysTenantService;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/tenant")
@RequestMapping("/tenant")
public class TenantController {
	protected static final Log logger = LogFactory.getLog(TenantController.class);

	protected SysTenantService sysTenantService;

	public TenantController() {

	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		SysTenant sysTenant = sysTenantService.getSysTenantByTenantId(loginContext.getTenantId());
		if (sysTenant != null) {
			request.setAttribute("tenant", sysTenant);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("tenant.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/tenant/edit", modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveSysTenant")
	public byte[] saveSysTenant(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		try {
			SysTenant sysTenant = sysTenantService.getSysTenantByTenantId(loginContext.getTenantId());
			if (sysTenant != null && (loginContext.isTenantAdmin()
					|| StringUtils.equals(sysTenant.getCreateBy(), loginContext.getActorId()))) {
				sysTenant.setName(request.getParameter("name"));
				sysTenant.setCode(request.getParameter("code"));
				sysTenant.setPrincipal(request.getParameter("principal"));
				sysTenant.setTelephone(request.getParameter("telephone"));
				this.sysTenantService.save(sysTenant);
				return ResponseUtils.responseJsonResult(true);
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource
	public void setSysTenantService(SysTenantService sysTenantService) {
		this.sysTenantService = sysTenantService;
	}

	@RequestMapping("/view")
	public ModelAndView view(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		request.removeAttribute("tenant_edit");
		SysTenant sysTenant = sysTenantService.getSysTenantByTenantId(loginContext.getTenantId());
		if (sysTenant != null) {
			request.setAttribute("tenant", sysTenant);
			if (loginContext.isTenantAdmin() || StringUtils.equals(sysTenant.getCreateBy(), loginContext.getActorId())) {
				request.setAttribute("tenant_edit", true);
			}
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("tenant.view");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/tenant/view", modelMap);
	}

}
