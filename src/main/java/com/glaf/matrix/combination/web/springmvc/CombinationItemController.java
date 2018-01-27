package com.glaf.matrix.combination.web.springmvc;

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

import com.glaf.core.config.ViewProperties;
import com.glaf.core.identity.*;
import com.glaf.core.security.*;
import com.glaf.core.util.*;

import com.glaf.matrix.combination.domain.*;
import com.glaf.matrix.combination.query.*;
import com.glaf.matrix.combination.service.*;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/sys/combinationItem")
@RequestMapping("/sys/combinationItem")
public class CombinationItemController {
	protected static final Log logger = LogFactory.getLog(CombinationItemController.class);

	protected CombinationItemService combinationItemService;

	public CombinationItemController() {

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
					CombinationItem combinationItem = combinationItemService.getCombinationItem(Long.valueOf(x));
					if (combinationItem != null
							&& (StringUtils.equals(combinationItem.getCreateBy(), loginContext.getActorId())
									|| loginContext.isSystemAdministrator())) {
						combinationItemService.deleteById(combinationItem.getId());
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			CombinationItem combinationItem = combinationItemService.getCombinationItem(Long.valueOf(id));
			if (combinationItem != null && (StringUtils.equals(combinationItem.getCreateBy(), loginContext.getActorId())
					|| loginContext.isSystemAdministrator())) {
				combinationItemService.deleteById(combinationItem.getId());
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		CombinationItem combinationItem = combinationItemService
				.getCombinationItem(RequestUtils.getLong(request, "id"));
		if (combinationItem != null) {
			request.setAttribute("combinationItem", combinationItem);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("combinationItem.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/matrix/combinationItem/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		CombinationItemQuery query = new CombinationItemQuery();
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
		int total = combinationItemService.getCombinationItemCountByQueryCriteria(query);
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

			List<CombinationItem> list = combinationItemService.getCombinationItemsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (CombinationItem combinationItem : list) {
					JSONObject rowJSON = combinationItem.toJsonObject();
					rowJSON.put("id", combinationItem.getId());
					rowJSON.put("rowId", combinationItem.getId());
					rowJSON.put("combinationItemId", combinationItem.getId());
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

		return new ModelAndView("/matrix/combinationItem/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("combinationItem.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/matrix/combinationItem/query", modelMap);
	}

	@ResponseBody
	@RequestMapping("/save")
	public byte[] save(HttpServletRequest request) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		CombinationItem combinationItem = new CombinationItem();
		try {
			Tools.populate(combinationItem, params);
			combinationItem.setSyncId(RequestUtils.getLong(request, "syncId"));
			combinationItem.setDeploymentId(request.getParameter("deploymentId"));
			combinationItem.setTitle(request.getParameter("title"));
			combinationItem.setSql(request.getParameter("sql"));
			combinationItem.setRecursionSql(request.getParameter("recursionSql"));
			combinationItem.setRecursionColumns(request.getParameter("recursionColumns"));
			combinationItem.setPrimaryKey(request.getParameter("primaryKey"));
			combinationItem.setExpression(request.getParameter("expression"));
			combinationItem.setCreateTableFlag(request.getParameter("createTableFlag"));
			combinationItem.setDeleteFetch(request.getParameter("deleteFetch"));
			combinationItem.setSortNo(RequestUtils.getInt(request, "sortNo"));
			combinationItem.setLocked(RequestUtils.getInt(request, "locked"));
			combinationItem.setCreateBy(actorId);
			this.combinationItemService.save(combinationItem);

			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource(name = "com.glaf.matrix.combination.service.combinationItemService")
	public void setCombinationItemService(CombinationItemService combinationItemService) {
		this.combinationItemService = combinationItemService;
	}

}
