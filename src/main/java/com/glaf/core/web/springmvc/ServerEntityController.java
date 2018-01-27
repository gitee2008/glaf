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

package com.glaf.core.web.springmvc;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import com.alibaba.fastjson.*;
import com.glaf.core.config.DBConfiguration;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.security.*;
import com.glaf.core.server.ServerValidatorFactory;
import com.glaf.core.util.*;
import com.glaf.core.util.security.RSAUtils;
import com.glaf.core.domain.*;
import com.glaf.core.identity.Role;
import com.glaf.core.query.*;
import com.glaf.core.service.*;

/**
 * 
 * SpringMVC控制器
 * 
 */

@Controller("/sys/server")
@RequestMapping("/sys/server")
public class ServerEntityController {
	protected static final Log logger = LogFactory.getLog(ServerEntityController.class);

	protected static AtomicBoolean running = new AtomicBoolean(false);

	protected IServerEntityService serverEntityService;

	public ServerEntityController() {

	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		ServerEntity serverEntity = serverEntityService.getServerEntityById(RequestUtils.getLong(request, "id"));
		if (serverEntity != null) {
			request.setAttribute("serverEntity", serverEntity);
			request.setAttribute("serverEntityId_enc", RSAUtils.encryptString(String.valueOf(serverEntity.getId())));
			request.setAttribute("serverEntityName_enc", RSAUtils.encryptString(serverEntity.getName()));
			request.setAttribute("nodeId", serverEntity.getNodeId());

			List<String> perms = StringTools.split(serverEntity.getPerms());
			StringBuilder buffer = new StringBuilder();
			List<Role> roles = IdentityFactory.getRoles();

			if (roles != null && !roles.isEmpty()) {
				for (Role role : roles) {
					if (perms.contains(String.valueOf(role.getId()))) {
						buffer.append(role.getName());
						buffer.append(FileUtils.newline);
					}
				}
			}

			request.setAttribute("x_role_names", buffer.toString());

		} else {
			request.setAttribute("nodeId", RequestUtils.getLong(request, "nodeId"));
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("serverEntity.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/server/edit", modelMap);
	}

	@ResponseBody
	@RequestMapping("/initServer")
	public byte[] initServer(HttpServletRequest request) {
		if (running.get()) {
			return ResponseUtils.responseJsonResult(false, "不能执行初始化，已经有任务在执行中，请等待其他任务完成再执行。");
		}

		try {
			running.set(true);
			ServerEntity repository = null;
			if (StringUtils.isNotEmpty(request.getParameter("id"))) {
				repository = serverEntityService.getServerEntityById(RequestUtils.getLong(request, "id"));
				if (repository != null) {

					String name = repository.getName();
					String dbType = repository.getType();
					String host = repository.getHost();

					int port = repository.getPort();
					String serverEntityName = repository.getDbname();
					String user = repository.getUser();
					String password = SecurityUtils.decode(repository.getKey(), repository.getPassword());

					DBConfiguration.addDataSourceProperties(name, dbType, host, port, serverEntityName, user, password);
					if (DBConnectionFactory.checkConnection(name)) {

						repository.setVerify("Y");
						repository.setInitFlag("Y");
						serverEntityService.update(repository);

						return ResponseUtils.responseJsonResult(true, "数据库已经成功初始化。");
					}
				}
			}

		} catch (Exception ex) {

			logger.error(ex);
		} finally {
			running.set(false);
		}
		return ResponseUtils.responseJsonResult(false, "服务器配置错误。");
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		ServerEntityQuery query = new ServerEntityQuery();
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
		int total = serverEntityService.getServerEntityCountByQueryCriteria(query);
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

			List<ServerEntity> list = serverEntityService.getServerEntitiesByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (ServerEntity m : list) {
					JSONObject rowJSON = m.toJsonObject();
					rowJSON.put("id", m.getId());
					rowJSON.put("rowId", m.getId());
					rowJSON.put("serverId", m.getId());
					rowJSON.put("startIndex", ++start);
					rowJSON.remove("key");
					rowJSON.remove("user");
					rowJSON.remove("password");
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

		return new ModelAndView("/sys/server/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("serverEntity.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/sys/server/query", modelMap);
	}

	@ResponseBody
	@RequestMapping(value = "/reloadServer", method = RequestMethod.POST)
	public byte[] reloadServer(HttpServletRequest request) {
		return ResponseUtils.responseResult(true);
	}

	@ResponseBody
	@RequestMapping("/saveServer")
	public byte[] saveServer(HttpServletRequest request) {
		ServerEntity serverEntity = null;
		if (StringUtils.isNotEmpty(request.getParameter("id"))) {
			serverEntity = serverEntityService.getServerEntityById(RequestUtils.getLong(request, "id"));
		}
		if (serverEntity == null) {
			serverEntity = new ServerEntity();
			Tools.populate(serverEntity, RequestUtils.getParameterMap(request));
		}

		String user = request.getParameter("user");
		String password = request.getParameter("password");
		serverEntity.setUser(user);
		serverEntity.setPassword(password);
		serverEntity.setTitle(request.getParameter("title"));
		serverEntity.setCode(request.getParameter("code"));
		serverEntity.setDiscriminator(request.getParameter("discriminator"));
		serverEntity.setNodeId(RequestUtils.getLong(request, "nodeId"));
		serverEntity.setHost(request.getParameter("host"));
		serverEntity.setPort(RequestUtils.getInt(request, "port"));
		serverEntity.setName(request.getParameter("name"));
		serverEntity.setMapping(request.getParameter("mapping"));
		serverEntity.setType(request.getParameter("type"));
		serverEntity.setLevel(RequestUtils.getInt(request, "level"));
		serverEntity.setPriority(RequestUtils.getInt(request, "priority"));
		serverEntity.setOperation(RequestUtils.getInt(request, "operation"));
		serverEntity.setDbname(request.getParameter("dbname"));
		serverEntity.setPath(request.getParameter("path"));
		serverEntity.setProgram(request.getParameter("program"));
		serverEntity.setCatalog(request.getParameter("catalog"));
		serverEntity.setProviderClass(request.getParameter("providerClass"));
		serverEntity.setActive(request.getParameter("active"));
		serverEntity.setDetectionFlag(request.getParameter("detectionFlag"));
		serverEntity.setSecretAlgorithm(request.getParameter("secretAlgorithm"));
		serverEntity.setSecretKey(request.getParameter("secretKey"));
		serverEntity.setSecretIv(request.getParameter("secretIv"));
		serverEntity.setAddressPerms(request.getParameter("addressPerms"));
		serverEntity.setPerms(request.getParameter("perms"));
		serverEntity.setAttribute(request.getParameter("attribute"));

		try {
			this.serverEntityService.save(serverEntity);
			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {

			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource
	public void setServerEntityService(IServerEntityService serverEntityService) {
		this.serverEntityService = serverEntityService;
	}

	@ResponseBody
	@RequestMapping("/verify")
	public byte[] verify(HttpServletRequest request) {
		try {
			ServerEntity serverEntity = null;
			if (StringUtils.isNotEmpty(request.getParameter("id"))) {
				serverEntity = serverEntityService.getServerEntityById(RequestUtils.getLong(request, "id"));
			}
			if (serverEntity == null) {
				serverEntity = new ServerEntity();
				Tools.populate(serverEntity, RequestUtils.getParameterMap(request));
			}

			String user = request.getParameter("user");
			String password = request.getParameter("password");

			if (!"88888888".equals(password)) {
				String key = SecurityUtils.genKey();
				String pass = SecurityUtils.encode(key, password);
				serverEntity.setKey(key);
				serverEntity.setPassword(pass);
			}

			serverEntity.setUser(user);
			serverEntity.setTitle(request.getParameter("title"));
			serverEntity.setDiscriminator(request.getParameter("discriminator"));
			serverEntity.setNodeId(RequestUtils.getLong(request, "nodeId"));
			serverEntity.setHost(request.getParameter("host"));
			serverEntity.setPort(RequestUtils.getInt(request, "port"));
			serverEntity.setMapping(request.getParameter("mapping"));
			serverEntity.setType(request.getParameter("type"));
			serverEntity.setLevel(RequestUtils.getInt(request, "level"));
			serverEntity.setPriority(RequestUtils.getInt(request, "priority"));
			serverEntity.setOperation(RequestUtils.getInt(request, "operation"));
			serverEntity.setDbname(request.getParameter("dbname"));
			serverEntity.setPath(request.getParameter("path"));
			serverEntity.setProgram(request.getParameter("program"));
			serverEntity.setCatalog(request.getParameter("catalog"));
			serverEntity.setProviderClass(request.getParameter("providerClass"));
			serverEntity.setActive(request.getParameter("active"));
			serverEntity.setDetectionFlag(request.getParameter("detectionFlag"));
			serverEntity.setSecretAlgorithm(request.getParameter("secretAlgorithm"));
			serverEntity.setSecretKey(request.getParameter("secretKey"));
			serverEntity.setSecretIv(request.getParameter("secretIv"));
			serverEntity.setAddressPerms(request.getParameter("addressPerms"));
			serverEntity.setPerms(request.getParameter("perms"));
			serverEntity.setAttribute(request.getParameter("attribute"));

			ServerEntity model = serverEntity.clone();

			if ("88888888".equals(password)) {
				password = SecurityUtils.decode(serverEntity.getKey(), serverEntity.getPassword());
				model.setPassword(password);
			}

			if (ServerValidatorFactory.getInstance().verify(model)) {
				serverEntity.setVerify("Y");
				serverEntityService.update(serverEntity);
				return ResponseUtils.responseJsonResult(false, "服务器配置正确。");
			}

		} catch (Exception ex) {

			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false, "服务器配置错误。");
	}

	@ResponseBody
	@RequestMapping("/verify2")
	public byte[] verify2(HttpServletRequest request) {
		try {
			ServerEntity serverEntity = null;
			if (StringUtils.isNotEmpty(request.getParameter("id"))) {
				serverEntity = serverEntityService.getServerEntityById(RequestUtils.getLong(request, "id"));
				if (serverEntity != null) {
					String password = SecurityUtils.decode(serverEntity.getKey(), serverEntity.getPassword());
					ServerEntity model = serverEntity.clone();
					model.setPassword(password);
					if (ServerValidatorFactory.getInstance().verify(model)) {
						serverEntity.setVerify("Y");
						serverEntityService.update(serverEntity);
						return ResponseUtils.responseJsonResult(false, "服务器配置正确。");
					}
				}
			}
		} catch (Exception ex) {

			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false, "服务器配置错误。");
	}

	@RequestMapping("/view")
	public ModelAndView view(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		ServerEntity serverEntity = serverEntityService.getServerEntityById(RequestUtils.getLong(request, "id"));
		request.setAttribute("serverEntity", serverEntity);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view);
		}

		String x_view = ViewProperties.getString("serverEntity.view");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view);
		}

		return new ModelAndView("/sys/server/view");
	}

}
