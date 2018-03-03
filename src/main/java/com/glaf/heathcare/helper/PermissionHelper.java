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

package com.glaf.heathcare.helper;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.RequestUtils;
import com.glaf.heathcare.service.GradeInfoService;

public class PermissionHelper {

	protected GradeInfoService gradeInfoService;

	public void checkPermission(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		request.removeAttribute("heathcare_curd_perm");
		if (loginContext.hasSystemPermission() || loginContext.isSystemAdministrator()) {
			request.setAttribute("heathcare_curd_perm", true);
			return;
		}
		if (loginContext.hasPermission("ORG_ADMIN", "or")) {
			request.setAttribute("heathcare_curd_perm", true);
			return;
		}
		throw new RuntimeException("access deny");
	}

	public GradeInfoService getGradeInfoService() {
		if (gradeInfoService == null) {
			gradeInfoService = ContextFactory.getBean("com.glaf.heathcare.service.gradeInfoService");
		}
		return gradeInfoService;
	}

	public void setGradeInfoService(GradeInfoService gradeInfoService) {
		this.gradeInfoService = gradeInfoService;
	}

	public void setPermission(HttpServletRequest request) {
		request.removeAttribute("privilege_export");
		request.removeAttribute("privilege_write");
		request.removeAttribute("privilege_admin");
		request.removeAttribute("heathcare_curd_perm");

		LoginContext loginContext = RequestUtils.getLoginContext(request);

		if (loginContext.hasSystemPermission() || loginContext.isSystemAdministrator()) {
			request.setAttribute("heathcare_curd_perm", true);
		}

		if (loginContext.hasPermission("TenantAdmin", "or")) {
			request.setAttribute("heathcare_curd_perm", true);
		}

		if (loginContext.isSystemAdministrator()) {
			request.setAttribute("privilege_export", true);
			request.setAttribute("privilege_write", true);
			request.setAttribute("privilege_admin", true);
			return;
		}

		if (loginContext.hasPermission("TenantAdmin", "or")) {
			request.setAttribute("privilege_export", true);
			request.setAttribute("privilege_write", true);
			request.setAttribute("privilege_admin", true);
			return;
		}

		List<String> privileges = getGradeInfoService().getPrivileges(null, loginContext.getTenantId(),
				loginContext.getActorId());
		request.setAttribute("privileges", privileges);

		if (privileges != null && !privileges.isEmpty()) {
			for (String privilege : privileges) {
				if (StringUtils.equals(privilege, "rx")) {
					request.setAttribute("privilege_export", true);
				}
				if (StringUtils.equals(privilege, "rw") || StringUtils.equals(privilege, "admin")) {
					request.setAttribute("privilege_write", true);
				}
				if (StringUtils.equals(privilege, "admin")) {
					request.setAttribute("privilege_admin", true);
				}
			}
		}

	}

	public void setUserPermission(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		request.removeAttribute("heathcare_curd_perm");
		if (loginContext.hasSystemPermission() || loginContext.isSystemAdministrator()) {
			request.setAttribute("heathcare_curd_perm", true);
		}
	}
}
