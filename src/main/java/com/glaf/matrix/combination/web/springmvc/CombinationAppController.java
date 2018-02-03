package com.glaf.matrix.combination.web.springmvc;

import java.io.IOException;
import java.sql.Connection;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import com.alibaba.fastjson.*;
import com.glaf.core.config.DatabaseConnectionConfig;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.domain.Database;
import com.glaf.core.factory.TableFactory;
import com.glaf.core.identity.*;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.jdbc.QueryHelper;
import com.glaf.core.security.*;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.util.*;
import com.glaf.matrix.combination.bean.SqlToCombinationTableBean;
import com.glaf.matrix.combination.domain.*;
import com.glaf.matrix.combination.query.*;
import com.glaf.matrix.combination.service.*;
import com.glaf.matrix.util.SysParams;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/sys/combinationApp")
@RequestMapping("/sys/combinationApp")
public class CombinationAppController {
	protected static final Log logger = LogFactory.getLog(CombinationAppController.class);

	protected IDatabaseService databaseService;

	protected CombinationAppService combinationAppService;

	protected CombinationHistoryService combinationHistoryService;

	public CombinationAppController() {

	}

	@ResponseBody
	@RequestMapping("/delete")
	public byte[] delete(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Long id = RequestUtils.getLong(request, "id");
		String ids = request.getParameter("ids");
		if (StringUtils.isNotEmpty(ids)) {
			StringTokenizer token = new StringTokenizer(ids, ",");
			while (token.hasMoreTokens()) {
				String x = token.nextToken();
				if (StringUtils.isNotEmpty(x)) {
					CombinationApp combinationApp = combinationAppService.getCombinationApp(Long.valueOf(x));
					if (combinationApp != null
							&& (StringUtils.equals(combinationApp.getCreateBy(), loginContext.getActorId())
									|| loginContext.isSystemAdministrator())) {

					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			CombinationApp combinationApp = combinationAppService.getCombinationApp(Long.valueOf(id));
			if (combinationApp != null && (StringUtils.equals(combinationApp.getCreateBy(), loginContext.getActorId())
					|| loginContext.isSystemAdministrator())) {

				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@ResponseBody
	@RequestMapping("/dropTable")
	public byte[] dropTable(HttpServletRequest request, ModelMap modelMap) {
		Long id = RequestUtils.getLong(request, "id");
		CombinationApp combinationApp = combinationAppService.getCombinationApp(id);
		if (combinationApp != null && StringUtils.isNotEmpty(combinationApp.getTargetTableName())) {
			TableFactory.clear();
			boolean error = false;
			List<Long> databaseIds = StringTools.splitToLong(combinationApp.getTargetDatabaseIds());
			for (Long databaseId : databaseIds) {
				try {
					Database targetDatabase = databaseService.getDatabaseById(databaseId);
					if (targetDatabase != null) {
						if (StringUtils.startsWithIgnoreCase(combinationApp.getTargetTableName(), "etl_")
								|| StringUtils.startsWithIgnoreCase(combinationApp.getTargetTableName(), "sync_")
								|| StringUtils.startsWithIgnoreCase(combinationApp.getTargetTableName(), "tmp")
								|| StringUtils.startsWithIgnoreCase(combinationApp.getTargetTableName(), "useradd_")
								|| StringUtils.startsWithIgnoreCase(combinationApp.getTargetTableName(),
										"tree_table_")) {
							if (DBUtils.tableExists(targetDatabase.getName(), combinationApp.getTargetTableName())) {
								String ddlStatements = " drop table " + combinationApp.getTargetTableName();
								logger.warn(ddlStatements);
								DBUtils.executeSchemaResource(targetDatabase.getName(), ddlStatements);
							}
						}
					}
				} catch (Exception ex) {
					error = true;
					logger.error(ex);
				}
			}
			if (!error) {
				TableFactory.clear();
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		CombinationApp combinationApp = combinationAppService.getCombinationApp(RequestUtils.getLong(request, "id"));
		if (combinationApp != null) {
			request.setAttribute("combinationApp", combinationApp);
		}

		LoginContext loginContext = RequestUtils.getLoginContext(request);

		DatabaseConnectionConfig cfg = new DatabaseConnectionConfig();
		List<Database> activeDatabases = cfg.getActiveDatabases(loginContext);
		if (activeDatabases != null && !activeDatabases.isEmpty()) {
			if (combinationApp != null && StringUtils.isNotEmpty(combinationApp.getTargetDatabaseIds())) {
				List<Long> databaseIds = StringTools.splitToLong(combinationApp.getTargetDatabaseIds().trim());
				for (Database db : activeDatabases) {
					if (databaseIds.contains(db.getId())) {
						db.setSelected("selected");
					} else {
						db.setSelected("");
					}
				}
			}
		}
		request.setAttribute("databases", activeDatabases);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("combinationApp.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/matrix/combinationApp/edit", modelMap);
	}

	@ResponseBody
	@RequestMapping("/execute")
	public byte[] execute(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		QueryHelper helper = new QueryHelper();
		SysParams.putInternalParams(params);
		long syncId = RequestUtils.getLong(request, "syncId");
		Database database = null;
		Database srcDatabase = null;
		Connection srcConn = null;
		try {
			CombinationApp combinationApp = combinationAppService.getCombinationApp(syncId);
			if (combinationApp != null && StringUtils.equals(combinationApp.getActive(), "Y")) {
				srcDatabase = databaseService.getDatabaseById(combinationApp.getSrcDatabaseId());
				if (srcDatabase != null) {
					srcConn = DBConnectionFactory.getConnection(srcDatabase.getName());
					logger.debug("srcConn:" + srcConn);
				}

				List<CombinationItem> items = combinationApp.getItems();
				for (CombinationItem item : items) {
					String sql = item.getSql();
					if (!DBUtils.isLegalQuerySql(sql)) {
						return ResponseUtils.responseJsonResult(false, item.getTitle() + "\nSQL已经不合法:" + sql);
					}
					try {
						List<ColumnDefinition> cols = helper.getColumnList(srcConn, sql, params);
						logger.debug(cols);
					} catch (Exception ex) {
						return ResponseUtils.responseJsonResult(false,
								item.getTitle() + "\nSql error:" + ex.getMessage());
					}
				}
				JdbcUtils.close(srcConn);

				List<Long> targetDBIds = StringTools.splitToLong(combinationApp.getTargetDatabaseIds());
				SqlToCombinationTableBean bean = new SqlToCombinationTableBean();
				for (Long targetDBId : targetDBIds) {
					database = databaseService.getDatabaseById(targetDBId);
					if (database != null) {
						long start = System.currentTimeMillis();
						bean.execute(combinationApp.getSrcDatabaseId(), targetDBId, syncId, params);
						CombinationHistory his = new CombinationHistory();
						his.setCreateBy(loginContext.getActorId());
						his.setDatabaseId(targetDBId);
						his.setDeploymentId(combinationApp.getDeploymentId());
						his.setSyncId(combinationApp.getId());
						his.setStatus(1);
						his.setTotalTime((int) (System.currentTimeMillis() - start));
						his.setDatabaseName(database.getTitle() + "[" + database.getHost() + "]");
						combinationHistoryService.save(his);
					}
				}
				return ResponseUtils.responseJsonResult(true);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		} finally {
			JdbcUtils.close(srcConn);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		CombinationAppQuery query = new CombinationAppQuery();
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
		int total = combinationAppService.getCombinationAppCountByQueryCriteria(query);
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

			List<CombinationApp> list = combinationAppService.getCombinationAppsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (CombinationApp combinationApp : list) {
					JSONObject rowJSON = combinationApp.toJsonObject();
					rowJSON.put("id", combinationApp.getId());
					rowJSON.put("rowId", combinationApp.getId());
					rowJSON.put("combinationAppId", combinationApp.getId());
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

		return new ModelAndView("/matrix/combinationApp/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("combinationApp.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/matrix/combinationApp/query", modelMap);
	}

	@ResponseBody
	@RequestMapping("/save")
	public byte[] save(HttpServletRequest request) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		String targetTableName = request.getParameter("targetTableName");
		targetTableName = targetTableName.trim();
		if (!(StringUtils.startsWithIgnoreCase(targetTableName, "etl_")
				|| StringUtils.startsWithIgnoreCase(targetTableName, "sync_")
				|| StringUtils.startsWithIgnoreCase(targetTableName, "tmp")
				|| StringUtils.startsWithIgnoreCase(targetTableName, "useradd_")
				|| StringUtils.startsWithIgnoreCase(targetTableName, "tree_table_"))) {
			return ResponseUtils.responseJsonResult(false, "目标表必须是以etl_,sync_,useradd_,tree_table_,tmp开头");
		}
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		CombinationApp combinationApp = new CombinationApp();
		try {
			Tools.populate(combinationApp, params);
			combinationApp.setTitle(request.getParameter("title"));
			combinationApp.setNodeId(RequestUtils.getLong(request, "nodeId"));
			combinationApp.setDeploymentId(request.getParameter("deploymentId"));
			combinationApp.setSrcDatabaseId(RequestUtils.getLong(request, "srcDatabaseId"));
			combinationApp.setTargetTableName(targetTableName);
			combinationApp.setSyncFlag(request.getParameter("syncFlag"));
			combinationApp.setType(request.getParameter("type"));
			combinationApp.setActive(request.getParameter("active"));
			combinationApp.setAutoSyncFlag(request.getParameter("autoSyncFlag"));
			combinationApp.setDeleteFetch(request.getParameter("deleteFetch"));
			combinationApp.setExternalColumnsFlag(request.getParameter("externalColumnsFlag"));
			combinationApp.setInterval(RequestUtils.getInt(request, "interval"));
			combinationApp.setSortNo(RequestUtils.getInt(request, "sortNo"));
			combinationApp.setCreateBy(actorId);
			combinationApp.setUpdateBy(actorId);

			String[] targetDatabaseIds = request.getParameterValues("targetDatabaseIds");
			if (targetDatabaseIds != null && targetDatabaseIds.length > 0) {
				StringBuilder buff = new StringBuilder();
				for (String targetDatabaseId : targetDatabaseIds) {
					buff.append(targetDatabaseId).append(", ");
				}
				combinationApp.setTargetDatabaseIds(buff.toString());
			}

			this.combinationAppService.save(combinationApp);

			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			// ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@ResponseBody
	@RequestMapping("/saveAs")
	public byte[] saveAs(HttpServletRequest request) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		try {
			long syncId = RequestUtils.getLong(request, "syncId");
			if (syncId > 0) {
				long nid = combinationAppService.saveAs(syncId, actorId);
				if (nid > 0) {
					return ResponseUtils.responseJsonResult(true);
				}
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource(name = "com.glaf.matrix.combination.service.combinationAppService")
	public void setCombinationAppService(CombinationAppService combinationAppService) {
		this.combinationAppService = combinationAppService;
	}

	@javax.annotation.Resource(name = "com.glaf.matrix.combination.service.combinationHistoryService")
	public void setCombinationHistoryService(CombinationHistoryService combinationHistoryService) {
		this.combinationHistoryService = combinationHistoryService;
	}

	@javax.annotation.Resource
	public void setDatabaseService(IDatabaseService databaseService) {
		this.databaseService = databaseService;
	}

}
