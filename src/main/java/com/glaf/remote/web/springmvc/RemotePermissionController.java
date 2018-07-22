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

package com.glaf.remote.web.springmvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.glaf.base.modules.sys.model.Dictory;
import com.glaf.base.modules.sys.service.DictoryService;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.identity.User;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.StringTools;

import com.glaf.remote.domain.RemotePermission;
import com.glaf.remote.query.RemotePermissionQuery;
import com.glaf.remote.service.RemotePermissionService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/sys/remotePermission")
@RequestMapping("/sys/remotePermission")
public class RemotePermissionController {
	protected static final Log logger = LogFactory.getLog(RemotePermissionController.class);

	protected DictoryService dictoryService;

	protected RemotePermissionService remotePermissionService;

	public RemotePermissionController() {

	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String type = request.getParameter("type");
		try {
			List<Dictory> dicts = dictoryService.getDictoryList("RemotePermission");
			request.setAttribute("dicts", dicts);

			if (StringUtils.isNotEmpty(type)) {
				RemotePermissionQuery query = new RemotePermissionQuery();
				query.type(type);
				List<RemotePermission> perms = remotePermissionService.list(query);
				request.setAttribute("perms", perms);
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
			logger.error(ex);
		}
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("remotePermission.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/permission/remote", modelMap);
	}

	@ResponseBody
	@RequestMapping("/save")
	public ModelAndView save(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug("params:" + params);
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		String type = request.getParameter("type");
		String items = request.getParameter("items");
		String jsonData = request.getParameter("jsonData");
		try {
			if (StringUtils.isNotEmpty(type)) {
				if (StringUtils.isNotEmpty(items)) {
					List<String> ips = StringTools.split(items);
					List<RemotePermission> list = new ArrayList<RemotePermission>();
					for (String ip : ips) {
						RemotePermission p = new RemotePermission();
						p.setCreateBy(actorId);
						p.setRemoteIP(ip);
						p.setType(type);
						list.add(p);
					}
					remotePermissionService.saveAll(type, list);
				} else if (StringUtils.isNotEmpty(jsonData)) {

				}
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
			logger.error(ex);
		}
		return this.edit(request, modelMap);
	}

	@javax.annotation.Resource
	public void setDictoryService(DictoryService dictoryService) {
		this.dictoryService = dictoryService;
	}

	@javax.annotation.Resource(name = "com.glaf.remote.service.remotePermissionService")
	public void setRemotePermissionService(RemotePermissionService remotePermissionService) {
		this.remotePermissionService = remotePermissionService;
	}

}
