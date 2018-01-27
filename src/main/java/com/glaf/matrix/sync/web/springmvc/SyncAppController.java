package com.glaf.matrix.sync.web.springmvc;

import java.io.IOException;
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
import com.glaf.core.domain.Database;
import com.glaf.core.identity.*;
import com.glaf.core.security.*;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.util.*;
import com.glaf.matrix.sync.bean.SqlToTableSyncBean;
import com.glaf.matrix.sync.domain.*;
import com.glaf.matrix.sync.query.*;
import com.glaf.matrix.sync.service.*;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/sys/syncApp")
@RequestMapping("/sys/syncApp")
public class SyncAppController {
	protected static final Log logger = LogFactory.getLog(SyncAppController.class);

	protected IDatabaseService databaseService;

	protected SyncAppService syncAppService;

	protected SyncHistoryService syncHistoryService;

	public SyncAppController() {

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
					SyncApp syncApp = syncAppService.getSyncApp(Long.valueOf(x));
					if (syncApp != null && (StringUtils.equals(syncApp.getCreateBy(), loginContext.getActorId())
							|| loginContext.isSystemAdministrator())) {

					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			SyncApp syncApp = syncAppService.getSyncApp(Long.valueOf(id));
			if (syncApp != null && (StringUtils.equals(syncApp.getCreateBy(), loginContext.getActorId())
					|| loginContext.isSystemAdministrator())) {

				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		SyncApp syncApp = syncAppService.getSyncApp(RequestUtils.getLong(request, "id"));
		if (syncApp != null) {
			request.setAttribute("syncApp", syncApp);
		}

		LoginContext loginContext = RequestUtils.getLoginContext(request);

		DatabaseConnectionConfig cfg = new DatabaseConnectionConfig();
		List<Database> activeDatabases = cfg.getActiveDatabases(loginContext);
		if (activeDatabases != null && !activeDatabases.isEmpty()) {
			if (syncApp != null && StringUtils.isNotEmpty(syncApp.getTargetDatabaseIds())) {
				List<Long> databaseIds = StringTools.splitToLong(syncApp.getTargetDatabaseIds().trim());
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

		String x_view = ViewProperties.getString("syncApp.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/matrix/syncApp/edit", modelMap);
	}

	@ResponseBody
	@RequestMapping("/execute")
	public byte[] execute(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		long syncId = RequestUtils.getLong(request, "syncId");
		Database database = null;
		try {
			SyncApp syncApp = syncAppService.getSyncApp(syncId);
			if (syncApp != null && StringUtils.equals(syncApp.getActive(), "Y")) {
				List<Long> targetDBIds = StringTools.splitToLong(syncApp.getTargetDatabaseIds());
				SqlToTableSyncBean bean = new SqlToTableSyncBean();
				for (Long targetDBId : targetDBIds) {
					database = databaseService.getDatabaseById(targetDBId);
					if (database != null) {
						long start = System.currentTimeMillis();
						bean.execute(syncApp.getSrcDatabaseId(), targetDBId, syncId);
						SyncHistory his = new SyncHistory();
						his.setCreateBy(loginContext.getActorId());
						his.setDatabaseId(targetDBId);
						his.setDeploymentId(syncApp.getDeploymentId());
						his.setSyncId(syncApp.getId());
						his.setStatus(1);
						his.setTotalTime((int) (System.currentTimeMillis() - start));
						his.setDatabaseName(
								database.getTitle() + "[" + database.getHost() + ":" + database.getUser() + "]");
						syncHistoryService.save(his);
					}
				}
				return ResponseUtils.responseJsonResult(true);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SyncAppQuery query = new SyncAppQuery();
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
		int total = syncAppService.getSyncAppCountByQueryCriteria(query);
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

			List<SyncApp> list = syncAppService.getSyncAppsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (SyncApp syncApp : list) {
					JSONObject rowJSON = syncApp.toJsonObject();
					rowJSON.put("id", syncApp.getId());
					rowJSON.put("rowId", syncApp.getId());
					rowJSON.put("syncAppId", syncApp.getId());
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

		return new ModelAndView("/matrix/syncApp/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("syncApp.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/matrix/syncApp/query", modelMap);
	}

	@ResponseBody
	@RequestMapping("/save")
	public byte[] save(HttpServletRequest request) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SyncApp syncApp = new SyncApp();
		try {
			Tools.populate(syncApp, params);
			syncApp.setTitle(request.getParameter("title"));
			syncApp.setNodeId(RequestUtils.getLong(request, "nodeId"));
			syncApp.setDeploymentId(request.getParameter("deploymentId"));
			syncApp.setSrcDatabaseId(RequestUtils.getLong(request, "srcDatabaseId"));
			syncApp.setSyncFlag(request.getParameter("syncFlag"));
			syncApp.setType(request.getParameter("type"));
			syncApp.setActive(request.getParameter("active"));
			syncApp.setAutoSyncFlag(request.getParameter("autoSyncFlag"));
			syncApp.setInterval(RequestUtils.getInt(request, "interval"));
			syncApp.setCreateBy(actorId);
			syncApp.setUpdateBy(actorId);

			String[] targetDatabaseIds = request.getParameterValues("targetDatabaseIds");
			if (targetDatabaseIds != null && targetDatabaseIds.length > 0) {
				StringBuilder buff = new StringBuilder();
				for (String targetDatabaseId : targetDatabaseIds) {
					buff.append(targetDatabaseId).append(", ");
				}
				syncApp.setTargetDatabaseIds(buff.toString());
			}

			this.syncAppService.save(syncApp);

			return ResponseUtils.responseJsonResult(true);
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

	@javax.annotation.Resource(name = "com.glaf.matrix.sync.service.syncAppService")
	public void setSyncAppService(SyncAppService syncAppService) {
		this.syncAppService = syncAppService;
	}

	@javax.annotation.Resource(name = "com.glaf.matrix.sync.service.syncHistoryService")
	public void setSyncHistoryService(SyncHistoryService syncHistoryService) {
		this.syncHistoryService = syncHistoryService;
	}

}
