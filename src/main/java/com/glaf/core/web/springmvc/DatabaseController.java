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

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import com.alibaba.fastjson.*;
import com.glaf.core.base.ColumnModel;
import com.glaf.core.base.TableModel;
import com.glaf.core.config.DBConfiguration;
import com.glaf.core.config.DatabaseConnectionConfig;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.identity.User;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.security.*;
import com.glaf.core.util.*;
import com.glaf.core.util.security.RSAUtils;
import com.glaf.core.domain.*;
import com.glaf.core.domain.util.DbidDomainFactory;
import com.glaf.core.factory.*;
import com.glaf.core.query.*;
import com.glaf.core.service.*;

/**
 * 
 * SpringMVC控制器
 * 
 */

@Controller("/sys/database")
@RequestMapping("/sys/database")
public class DatabaseController {
	protected static final Log logger = LogFactory.getLog(DatabaseController.class);

	protected static AtomicBoolean running = new AtomicBoolean(false);

	protected IDatabaseService databaseService;

	protected ITableDataService tableDataService;

	protected IServerEntityService serverEntityService;

	public DatabaseController() {

	}

	@RequestMapping("/chooseDatabases")
	public ModelAndView chooseDatabases(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String selectedDatabaseIds = request.getParameter("selectedDatabaseIds");
		DatabaseConnectionConfig config = new DatabaseConnectionConfig();
		List<Database> databases = config.getDatabases();
		if (databases != null && !databases.isEmpty()) {
			List<Long> selected = new ArrayList<Long>();
			List<Database> unselected = new ArrayList<Database>();
			if (selectedDatabaseIds != null && StringUtils.isNotEmpty(selectedDatabaseIds)) {
				List<Long> ids = StringTools.splitToLong(selectedDatabaseIds);
				for (Database database : databases) {
					if (ids.contains(database.getId())) {
						selected.add(database.getId());
					} else {
						unselected.add(database);
					}
				}
				request.setAttribute("selected", selected);
				request.setAttribute("unselected", databases);
			} else {
				request.setAttribute("selected", selected);
				request.setAttribute("unselected", databases);
			}
			request.setAttribute("databases", databases);

			StringBuffer bufferx = new StringBuffer();
			StringBuffer buffery = new StringBuffer();

			if (databases != null && databases.size() > 0) {
				for (int j = 0; j < databases.size(); j++) {
					Database d = (Database) databases.get(j);
					if (selected != null && selected.contains(d.getId())) {
						buffery.append("\n<option value=\"").append(d.getId()).append("\">").append(d.getTitle())
								.append(" [").append(d.getMapping()).append("]").append("</option>");
					} else {
						bufferx.append("\n<option value=\"").append(d.getId()).append("\">").append(d.getTitle())
								.append(" [").append(d.getMapping()).append("]").append("</option>");
					}
				}
			}
			request.setAttribute("bufferx", bufferx.toString());
			request.setAttribute("buffery", buffery.toString());
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("database.chooseDatabases");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/database/chooseDatabases", modelMap);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		Database database = databaseService.getDatabaseById(RequestUtils.getLong(request, "id"));
		if (database != null) {
			request.setAttribute("database", database);
			request.setAttribute("databaseId_enc", RSAUtils.encryptString(String.valueOf(database.getId())));
			request.setAttribute("databaseName_enc", RSAUtils.encryptString(database.getName()));
			request.setAttribute("parentId", database.getParentId());
		} else {
			request.setAttribute("parentId", RequestUtils.getLong(request, "parentId"));
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("database.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/database/edit", modelMap);
	}

	@ResponseBody
	@RequestMapping("/initDB")
	public byte[] initDB(HttpServletRequest request) {
		if (running.get()) {
			return ResponseUtils.responseJsonResult(false, "不能执行初始化，已经有任务在执行中，请等待其他任务完成再执行。");
		}

		try {
			running.set(true);
			Database db = null;
			if (StringUtils.isNotEmpty(request.getParameter("id"))) {
				db = databaseService.getDatabaseById(RequestUtils.getLong(request, "id"));
				if (db != null) {
					String name = db.getName();
					String dbType = db.getType();
					String host = db.getHost();

					int port = db.getPort();
					String databaseName = db.getDbname();
					String user = db.getUser();
					String password = SecurityUtils.decode(db.getKey(), db.getPassword());

					DBConfiguration.addDataSourceProperties(name, dbType, host, port, databaseName, user, password);
					if (DBConnectionFactory.checkConnection(name)) {
						db.setVerify("Y");
						db.setInitFlag("Y");
						databaseService.update(db);

						if (!DBUtils.tableExists(db.getName(), "SYS_DBID")) {
							TableDefinition tableDefinition = DbidDomainFactory.getTableDefinition("SYS_DBID");
							DBUtils.createTable(db.getName(), tableDefinition);
						}

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
		DatabaseQuery query = new DatabaseQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		if (!loginContext.isSystemAdministrator()) {
			String actorId = loginContext.getActorId();
			query.createBy(actorId);
		}

		String keywordsLike_base64 = request.getParameter("keywordsLike_base64");
		if (StringUtils.isNotEmpty(keywordsLike_base64)) {
			String keywordsLike = new String(Base64.decodeBase64(keywordsLike_base64));
			query.setKeywordsLike(keywordsLike);
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
		int total = databaseService.getDatabaseCountByQueryCriteria(query);
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

			List<Database> list = databaseService.getDatabasesByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (Database database : list) {
					JSONObject rowJSON = database.toJsonObject();
					rowJSON.put("id", database.getId());
					rowJSON.put("rowId", database.getId());
					rowJSON.put("databaseId", database.getId());
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

		return new ModelAndView("/sys/database/list", modelMap);
	}

	@RequestMapping("/permission")
	public ModelAndView permission(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		logger.debug("->params:" + RequestUtils.getParameterMap(request));
		String nameLike = request.getParameter("nameLike");

		String op_view = request.getParameter("op_view");
		if (StringUtils.isEmpty(op_view)) {
			op_view = "user";
		}

		request.setAttribute("op_view", op_view);

		DatabaseQuery databaseQuery = new DatabaseQuery();
		databaseQuery.setActive("1");
		if (StringUtils.isNotEmpty(nameLike) && StringUtils.equals(op_view, "database")) {
			databaseQuery.setTitleLike(nameLike);
			logger.debug("titleLike:" + nameLike);
		}
		List<Database> databaseList = databaseService.list(databaseQuery);
		request.setAttribute("databaseList", databaseList);

		List<DatabaseAccess> accesses = databaseService.getAllDatabaseAccesses();

		UserQuery query = new UserQuery();

		if (StringUtils.isNotEmpty(nameLike) && StringUtils.equals(op_view, "user")) {
			query.nameLike(nameLike);
		}
		List<User> users = null;
		if (StringUtils.isNotEmpty(nameLike) && StringUtils.equals(op_view, "user")) {
			users = IdentityFactory.searchUsers(nameLike);
		} else {
			users = IdentityFactory.getUsers();
		}

		if (users != null && !users.isEmpty()) {
			for (User user : users) {
				if (accesses != null && !accesses.isEmpty()) {
					for (DatabaseAccess access : accesses) {
						if (StringUtils.equalsIgnoreCase(user.getActorId(), access.getActorId())) {
							user.addRowKey(String.valueOf(access.getDatabaseId()));
						}
					}
				}
			}
			request.setAttribute("users", users);
		}

		String x_query = request.getParameter("x_query");
		if (StringUtils.equals(x_query, "true")) {
			Map<String, Object> paramMap = RequestUtils.getParameterMap(request);
			String x_complex_query = JsonUtils.encode(paramMap);
			x_complex_query = RequestUtils.encodeString(x_complex_query);
			request.setAttribute("x_complex_query", x_complex_query);
		} else {
			request.setAttribute("x_complex_query", "");
		}

		String x_view = ViewProperties.getString("database.permission");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/sys/database/permission", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("database.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/sys/database/query", modelMap);
	}

	@ResponseBody
	@RequestMapping(value = "/reloadDB", method = RequestMethod.POST)
	public byte[] reloadDB(HttpServletRequest request) {
		try {
			DatabaseQuery query = new DatabaseQuery();
			query.active("1");
			List<Database> databases = databaseService.list(query);
			if (databases != null && !databases.isEmpty()) {
				DatabaseConnectionConfig cfg = new DatabaseConnectionConfig();
				for (Database database : databases) {
					if (StringUtils.equalsIgnoreCase(database.getType(), "mysql")
							|| StringUtils.equalsIgnoreCase(database.getType(), "sqlserver")
							|| StringUtils.equalsIgnoreCase(database.getType(), "postgresql")
							|| StringUtils.equalsIgnoreCase(database.getType(), "oracle")) {
						cfg.checkConfig(database);
					}
				}
			}
			DatabaseFactory.clearAll();
			return ResponseUtils.responseResult(true);
		} catch (Exception ex) {
			logger.error(ex);
		}

		return ResponseUtils.responseResult(false);
	}

	@ResponseBody
	@RequestMapping("/saveAccessor")
	public byte[] saveAccessor(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		long databaseId = RequestUtils.getLong(request, "databaseId");
		String actorId = request.getParameter("actorId");
		String operation = request.getParameter("operation");
		if (databaseId > 0 && actorId != null) {
			/**
			 * 保证添加的部门是分级管理员管辖的部门
			 */
			if (loginContext.isSystemAdministrator()) {
				if (StringUtils.equals(operation, "revoke")) {
					databaseService.deleteAccessor(databaseId, actorId);
				} else {
					databaseService.createAccessor(databaseId, actorId);
				}
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@ResponseBody
	@RequestMapping("/saveDB")
	public byte[] saveDB(HttpServletRequest request) {
		Database database = null;
		if (StringUtils.isNotEmpty(request.getParameter("id"))) {
			database = databaseService.getDatabaseById(RequestUtils.getLong(request, "id"));
		}
		if (database == null) {
			database = new Database();
		}

		String user = request.getParameter("user");
		String password = request.getParameter("password");
		database.setUser(user);
		database.setPassword(password);
		database.setTitle(request.getParameter("title"));
		database.setParentId(RequestUtils.getLong(request, "parentId"));
		database.setDiscriminator(request.getParameter("discriminator"));
		database.setHost(request.getParameter("host"));
		database.setPort(RequestUtils.getInt(request, "port"));
		database.setMapping(request.getParameter("mapping"));
		database.setSection(request.getParameter("section"));
		database.setType(request.getParameter("type"));
		database.setRunType(request.getParameter("runType"));
		database.setUseType(request.getParameter("useType"));
		database.setLevel(RequestUtils.getInt(request, "level"));
		database.setPriority(RequestUtils.getInt(request, "priority"));
		database.setOperation(RequestUtils.getInt(request, "operation"));
		database.setDbname(request.getParameter("dbname"));
		database.setBucket(request.getParameter("bucket"));
		database.setCatalog(request.getParameter("catalog"));
		database.setInfoServer(request.getParameter("infoServer"));
		database.setLoginAs(request.getParameter("loginAs"));
		database.setLoginUrl(request.getParameter("loginUrl"));
		database.setTicket(request.getParameter("ticket"));
		database.setProgramId(request.getParameter("programId"));
		database.setProgramName(request.getParameter("programName"));
		database.setUserNameKey(request.getParameter("userNameKey"));
		database.setProviderClass(request.getParameter("providerClass"));
		database.setRemoteUrl(request.getParameter("remoteUrl"));
		database.setServerId(RequestUtils.getLong(request, "serverId"));
		database.setSysId(request.getParameter("sysId"));
		database.setQueueName(request.getParameter("queueName"));
		database.setActive(request.getParameter("active"));
		database.setIntToken(RequestUtils.getInt(request, "intToken"));
		database.setSort(RequestUtils.getInt(request, "sort"));

		if (StringUtils.isEmpty(database.getType())) {
			database.setType("sqlserver");
		}

		try {
			this.databaseService.save(database);
			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {

			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	/**
	 * 提交增加信息
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/saveServerRef")
	@ResponseBody
	public byte[] saveServerRef(HttpServletRequest request) {
		Long serverId = RequestUtils.getLong(request, "serverId");
		String databaseIds = request.getParameter("databaseIds");
		if (serverId != null && serverId > 0 && StringUtils.isNotEmpty(databaseIds)) {
			List<TableModel> rows = new ArrayList<TableModel>();
			StringTokenizer token = new StringTokenizer(databaseIds, ",");
			while (token.hasMoreTokens()) {
				String databaseId = token.nextToken();
				if (StringUtils.isNotEmpty(databaseId)) {
					TableModel t1 = new TableModel();
					t1.setTableName("SYS_DATABASE");
					ColumnModel idColumn1 = new ColumnModel();
					idColumn1.setColumnName("ID_");
					idColumn1.setValue(Long.parseLong(databaseId));
					t1.setIdColumn(idColumn1);

					ColumnModel column = new ColumnModel();
					column.setColumnName("SERVERID_");
					column.setValue(serverId);
					t1.addColumn(column);
					rows.add(t1);
				}
			}
			try {
				tableDataService.updateAllTableData(rows);
				return ResponseUtils.responseResult(true);
			} catch (Exception ex) {
				logger.error(ex);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	/**
	 * 提交增加信息
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/saveSort")
	@ResponseBody
	public byte[] saveSort(HttpServletRequest request) {
		String items = request.getParameter("items");
		if (StringUtils.isNotEmpty(items)) {
			int sort = 0;
			List<TableModel> rows = new ArrayList<TableModel>();
			StringTokenizer token = new StringTokenizer(items, ",");
			while (token.hasMoreTokens()) {
				String item = token.nextToken();
				if (StringUtils.isNotEmpty(item)) {
					sort++;
					TableModel t1 = new TableModel();
					t1.setTableName("SYS_DATABASE");
					ColumnModel idColumn1 = new ColumnModel();
					idColumn1.setColumnName("ID_");
					idColumn1.setValue(Long.parseLong(item));
					t1.setIdColumn(idColumn1);
					ColumnModel column = new ColumnModel();
					column.setColumnName("SORTNO_");
					column.setValue(sort);
					t1.addColumn(column);
					rows.add(t1);
				}
			}
			try {
				tableDataService.updateAllTableData(rows);
				return ResponseUtils.responseResult(true);
			} catch (Exception ex) {

				logger.error(ex);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@javax.annotation.Resource
	public void setDatabaseService(IDatabaseService databaseService) {
		this.databaseService = databaseService;
	}

	@javax.annotation.Resource
	public void setServerEntityService(IServerEntityService serverEntityService) {
		this.serverEntityService = serverEntityService;
	}

	@javax.annotation.Resource
	public void setTableDataService(ITableDataService tableDataService) {
		this.tableDataService = tableDataService;
	}

	/**
	 * 显示服务器关联页面
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/showServerRef")
	public ModelAndView showServerRef(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		params.remove("serverId");
		DatabaseQuery query = new DatabaseQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.locked(0);

		List<Database> databases = databaseService.list(query);
		request.setAttribute("unselected", databases);

		long serverId = RequestUtils.getLong(request, "serverId");
		if (databases != null && serverId > 0) {
			List<Long> selected = new ArrayList<Long>();
			for (Database db : databases) {
				if (db.getServerId() != 0 && db.getServerId() == serverId) {
					selected.add(db.getId());
				}
			}
			request.setAttribute("selected", selected);
		}

		ServerEntityQuery query2 = new ServerEntityQuery();
		Tools.populate(query2, params);
		query2.deleteFlag(0);
		query2.locked(0);

		List<ServerEntity> servers = serverEntityService.list(query2);
		request.setAttribute("servers", servers);

		String x_view = ViewProperties.getString("database.showServerRef");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/database/showServerRef", modelMap);
	}

	/**
	 * 显示排序页面
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/showSort")
	public ModelAndView showSort(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		DatabaseQuery query = new DatabaseQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);

		List<Database> list = databaseService.list(query);
		request.setAttribute("list", list);

		String x_view = ViewProperties.getString("database.showSort");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/database/showSort", modelMap);
	}

	@ResponseBody
	@RequestMapping("/verify")
	public byte[] verify(HttpServletRequest request) {
		try {
			Database database = null;
			if (StringUtils.isNotEmpty(request.getParameter("id"))) {
				database = databaseService.getDatabaseById(RequestUtils.getLong(request, "id"));
			}
			if (database == null) {
				database = new Database();
			}

			String user = request.getParameter("user");
			String password = request.getParameter("password");

			if (!"88888888".equals(password)) {
				String key = SecurityUtils.genKey();
				String pass = SecurityUtils.encode(key, password);
				database.setKey(key);
				database.setPassword(pass);
			}

			database.setUser(user);
			database.setTitle(request.getParameter("title"));
			database.setParentId(RequestUtils.getLong(request, "parentId"));
			database.setDiscriminator(request.getParameter("discriminator"));
			database.setHost(request.getParameter("host"));
			database.setPort(RequestUtils.getInt(request, "port"));
			database.setMapping(request.getParameter("mapping"));
			database.setType(request.getParameter("type"));
			database.setRunType(request.getParameter("runType"));
			database.setLevel(RequestUtils.getInt(request, "level"));
			database.setPriority(RequestUtils.getInt(request, "priority"));
			database.setOperation(RequestUtils.getInt(request, "operation"));
			database.setDbname(request.getParameter("dbname"));
			database.setBucket(request.getParameter("bucket"));
			database.setCatalog(request.getParameter("catalog"));
			database.setInfoServer(request.getParameter("infoServer"));
			database.setLoginAs(request.getParameter("loginAs"));
			database.setLoginUrl(request.getParameter("loginUrl"));
			database.setTicket(request.getParameter("ticket"));
			database.setProgramId(request.getParameter("programId"));
			database.setProgramName(request.getParameter("programName"));
			database.setUserNameKey(request.getParameter("userNameKey"));
			database.setProviderClass(request.getParameter("providerClass"));
			database.setRemoteUrl(request.getParameter("remoteUrl"));
			database.setActive(request.getParameter("active"));
			database.setIntToken(RequestUtils.getInt(request, "intToken"));
			database.setSort(RequestUtils.getInt(request, "sort"));
			database.setServerId(RequestUtils.getLong(request, "serverId"));
			database.setSysId(request.getParameter("sysId"));
			database.setQueueName(request.getParameter("queueName"));

			String name = database.getName();
			String dbType = database.getType();
			String host = database.getHost();
			int port = database.getPort();
			String databaseName = database.getDbname();
			if ("88888888".equals(password)) {
				password = SecurityUtils.decode(database.getKey(), database.getPassword());
			}

			DatabaseConnectionConfig config = new DatabaseConnectionConfig();
			if (config.checkConnectionImmediately(database)) {
				DBConfiguration.addDataSourceProperties(name, dbType, host, port, databaseName, user, password);
				database.setVerify("Y");
				databaseService.update(database);

				if (!DBUtils.tableExists(database.getName(), "SYS_DBID")) {
					TableDefinition tableDefinition = DbidDomainFactory.getTableDefinition("SYS_DBID");
					DBUtils.createTable(database.getName(), tableDefinition);
				}

				return ResponseUtils.responseJsonResult(true, "数据库配置正确。");
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
			Database database = null;
			if (StringUtils.isNotEmpty(request.getParameter("id"))) {
				database = databaseService.getDatabaseById(RequestUtils.getLong(request, "id"));
				if (database != null) {

					String name = database.getName();

					if (DBConnectionFactory.checkConnection(name)) {
						database.setVerify("Y");
						databaseService.verify(database);
						return ResponseUtils.responseJsonResult(true, "数据库配置正确。");
					}

					String dbType = database.getType();
					String host = database.getHost();
					int port = database.getPort();
					String databaseName = database.getDbname();
					String user = database.getUser();
					String password = SecurityUtils.decode(database.getKey(), database.getPassword());
					// logger.debug("->password:" + password);

					DatabaseConnectionConfig config = new DatabaseConnectionConfig();
					if (config.checkConnectionImmediately(database)) {
						DBConfiguration.addDataSourceProperties(name, dbType, host, port, databaseName, user, password);
						logger.debug("->systemName:" + name);
						database.setVerify("Y");
						databaseService.verify(database);
						
						if (!DBUtils.tableExists(database.getName(), "SYS_DBID")) {
							TableDefinition tableDefinition = DbidDomainFactory.getTableDefinition("SYS_DBID");
							DBUtils.createTable(database.getName(), tableDefinition);
						}
						
						return ResponseUtils.responseJsonResult(true, "数据库配置正确。");
					}
				}
			}
		} catch (Exception ex) {

			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false, "服务器配置错误。");
	}

	@RequestMapping("/verifyAll")
	public ModelAndView verifyAll(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		DatabaseQuery query = new DatabaseQuery();
		query.active("1");
		List<Database> databases = databaseService.list(query);
		request.setAttribute("databases", databases);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view);
		}

		String x_view = ViewProperties.getString("database.verifyAll");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view);
		}

		return new ModelAndView("/sys/database/verifyAll");
	}

	@RequestMapping("/view")
	public ModelAndView view(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		Database database = databaseService.getDatabaseById(RequestUtils.getLong(request, "id"));
		request.setAttribute("database", database);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view);
		}

		String x_view = ViewProperties.getString("database.view");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view);
		}

		return new ModelAndView("/sys/database/view");
	}

}
