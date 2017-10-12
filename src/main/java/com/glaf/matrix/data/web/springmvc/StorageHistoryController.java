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

package com.glaf.matrix.data.web.springmvc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.glaf.core.config.SystemProperties;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.domain.Database;
import com.glaf.core.identity.User;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.FileUtils;
import com.glaf.core.util.JdbcUtils;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.StringTools;
import com.glaf.core.util.Tools;
import com.glaf.matrix.data.bean.GenaralDBToSQLite;
import com.glaf.matrix.data.bean.SQLiteToGenaralDB;
import com.glaf.matrix.data.domain.StorageApp;
import com.glaf.matrix.data.domain.StorageHistory;
import com.glaf.matrix.data.query.StorageHistoryQuery;
import com.glaf.matrix.data.service.StorageAppService;
import com.glaf.matrix.data.service.StorageHistoryService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/sys/storageHistory")
@RequestMapping("/sys/storageHistory")
public class StorageHistoryController {
	protected static final Log logger = LogFactory.getLog(StorageHistoryController.class);

	protected IDatabaseService databaseService;

	protected StorageAppService storageAppService;

	protected StorageHistoryService storageHistoryService;

	public StorageHistoryController() {

	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		StorageHistory storageHistory = storageHistoryService.getStorageHistory(RequestUtils.getLong(request, "id"));
		if (storageHistory != null) {
			request.setAttribute("storageHistory", storageHistory);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("storageHistory.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/matrix/storageHistory/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		StorageHistoryQuery query = new StorageHistoryQuery();
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
		int total = storageHistoryService.getStorageHistoryCountByQueryCriteria(query);
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

			List<StorageHistory> list = storageHistoryService.getStorageHistorysByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (StorageHistory storageHistory : list) {
					JSONObject rowJSON = storageHistory.toJsonObject();
					rowJSON.put("id", storageHistory.getId());
					rowJSON.put("rowId", storageHistory.getId());
					rowJSON.put("storageHistoryId", storageHistory.getId());
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

		return new ModelAndView("/matrix/storageHistory/list", modelMap);
	}

	@ResponseBody
	@RequestMapping("/restore")
	public byte[] restore(HttpServletRequest request) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		StorageHistory storageHistory = null;
		String systemName = null;
		String backup_sqliteDB = null;
		try {
			storageHistory = storageHistoryService.getStorageHistory(RequestUtils.getLong(request, "id"));
			StorageApp storageApp = storageAppService.getStorageApp(storageHistory.getStorageId());
			if (storageApp != null) {
				backup_sqliteDB = storageHistory.getPath();
				storageHistory.setCreateBy(actorId);
				int version = storageAppService.incrementVersion(storageApp.getId());
				storageHistory.setVersion(version);
				String sqliteDB = "sys_storage_" + storageApp.getId() + "_" + version + "_"
						+ DateUtils.getNowYearMonthDay() + ".db";
				String path = SystemProperties.getConfigRootPath() + "/storage";
				FileUtils.mkdirs(path);

				if (StringUtils.isNotEmpty(storageApp.getTableNames())) {
					List<String> tables = StringTools.split(storageApp.getTableNames());
					GenaralDBToSQLite bean = new GenaralDBToSQLite();
					Database database = null;

					if (storageApp.getDatabaseId() > 0) {
						database = databaseService.getDatabaseById(storageApp.getDatabaseId());
					}
					if (database != null) {
						systemName = database.getName();
					} else {
						systemName = com.glaf.core.config.Environment.DEFAULT_SYSTEM_NAME;
					}

					bean.export(systemName, tables, sqliteDB);

					String dbpath = SystemProperties.getConfigRootPath() + "/db/" + sqliteDB;
					FileUtils.copy(dbpath, path + "/" + sqliteDB);

					storageHistory = new StorageHistory();
					storageHistory.setSysFlag("Y");
					storageHistory.setCreateBy(actorId);
					storageHistory.setDeploymentId(storageApp.getDeploymentId());
					storageHistory.setPath(sqliteDB);
					storageHistory.setStorageId(storageApp.getId());
					storageHistory.setVersion(version);
					this.storageHistoryService.save(storageHistory);

					FileUtils.copy(path + "/" + backup_sqliteDB,
							SystemProperties.getConfigRootPath() + "/db/" + backup_sqliteDB);

					if (StringUtils.equals(storageApp.getRestoreFlag(), "DELETE_INSERT")) {
						Connection connection = null;
						Statement statement = null;
						try {
							connection = DBConnectionFactory.getConnection(systemName);
							for (String tableName : tables) {
								if (DBUtils.tableExists(connection, tableName)) {
									connection.setAutoCommit(false);
									statement = connection.createStatement();
									statement.executeUpdate(" delete from " + tableName);
									JdbcUtils.close(statement);
									connection.commit();
									logger.info("empty table:" + tableName);
								}
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							throw new RuntimeException(ex);
						} finally {
							JdbcUtils.close(statement);
							JdbcUtils.close(connection);
						}
					}
					SQLiteToGenaralDB sqliteToGenaralDB = new SQLiteToGenaralDB();
					sqliteToGenaralDB.importData(systemName, backup_sqliteDB, tables);
					return ResponseUtils.responseJsonResult(true);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@ResponseBody
	@RequestMapping("/save")
	public byte[] save(HttpServletRequest request) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		StorageHistory storageHistory = new StorageHistory();
		String systemName = null;
		try {
			Tools.populate(storageHistory, params);
			storageHistory.setStorageId(RequestUtils.getLong(request, "storageId"));
			StorageApp storageApp = storageAppService.getStorageApp(RequestUtils.getLong(request, "storageId"));
			if (storageApp != null) {
				storageHistory.setCreateBy(actorId);
				int version = storageAppService.incrementVersion(storageApp.getId());
				storageHistory.setVersion(version);
				String sqliteDB = "storage_" + storageApp.getId() + "_" + version + "_" + DateUtils.getNowYearMonthDay()
						+ ".db";
				String path = SystemProperties.getConfigRootPath() + "/storage";
				FileUtils.mkdirs(path);
				storageHistory.setPath(sqliteDB);

				if (StringUtils.isNotEmpty(storageApp.getTableNames())) {
					List<String> tables = StringTools.split(storageApp.getTableNames());
					GenaralDBToSQLite bean = new GenaralDBToSQLite();
					Database database = null;

					if (storageApp.getDatabaseId() > 0) {
						database = databaseService.getDatabaseById(storageApp.getDatabaseId());
					}
					if (database != null) {
						systemName = database.getName();
					} else {
						systemName = com.glaf.core.config.Environment.DEFAULT_SYSTEM_NAME;
					}

					logger.debug("tables:" + tables);
					bean.export(systemName, tables, sqliteDB);

					String dbpath = SystemProperties.getConfigRootPath() + "/db/" + sqliteDB;
					FileUtils.copy(dbpath, path + "/" + sqliteDB);

					storageHistory.setCreateBy(actorId);
					storageHistory.setDeploymentId(storageApp.getDeploymentId());
					this.storageHistoryService.save(storageHistory);
					return ResponseUtils.responseJsonResult(true);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource
	public void setDatabaseService(IDatabaseService databaseService) {
		this.databaseService = databaseService;
	}

	@javax.annotation.Resource(name = "com.glaf.matrix.data.service.storageAppService")
	public void setStorageAppService(StorageAppService storageAppService) {
		this.storageAppService = storageAppService;
	}

	@javax.annotation.Resource(name = "com.glaf.matrix.data.service.storageHistoryService")
	public void setStorageHistoryService(StorageHistoryService storageHistoryService) {
		this.storageHistoryService = storageHistoryService;
	}

}
