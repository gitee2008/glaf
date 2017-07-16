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
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.core.base.BaseItem;
import com.glaf.core.domain.Scheduler;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.identity.User;
import com.glaf.core.job.JobProperties;
import com.glaf.core.query.SchedulerQuery;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.service.ISchedulerLogService;
import com.glaf.core.service.ISysSchedulerService;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.QuartzUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/sys/scheduler")
@RequestMapping("/sys/scheduler")
public class SystemSchedulerController {
	protected static final Log logger = LogFactory.getLog(SystemSchedulerController.class);

	protected ISysSchedulerService sysSchedulerService;

	protected ISchedulerLogService schedulerLogService;

	public SystemSchedulerController() {

	}

	@RequestMapping("/choose")
	public ModelAndView choose(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("scheduler.choose");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/scheduler/choose", modelMap);
	}

	@ResponseBody
	@RequestMapping("/delete")
	public byte[] delete(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String id = RequestUtils.getString(request, "id");
		String ids = request.getParameter("ids");
		if (StringUtils.isNotEmpty(ids)) {
			StringTokenizer token = new StringTokenizer(ids, ",");
			while (token.hasMoreTokens()) {
				String x = token.nextToken();
				if (StringUtils.isNotEmpty(x)) {
					Scheduler scheduler = sysSchedulerService.getSchedulerByTaskId(String.valueOf(x));
					if (scheduler != null && (StringUtils.equals(scheduler.getCreateBy(), loginContext.getActorId())
							|| loginContext.isSystemAdministrator())) {
						QuartzUtils.stop(scheduler.getId());
						schedulerLogService.deleteSchedulerLogByTaskId(id);
						sysSchedulerService.deleteScheduler(id);
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			Scheduler scheduler = sysSchedulerService.getSchedulerByTaskId(String.valueOf(id));
			if (scheduler != null && (StringUtils.equals(scheduler.getCreateBy(), loginContext.getActorId())
					|| loginContext.isSystemAdministrator())) {
				QuartzUtils.stop(scheduler.getId());
				schedulerLogService.deleteSchedulerLogByTaskId(id);
				sysSchedulerService.deleteScheduler(scheduler.getId());
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@ResponseBody
	@RequestMapping("/disableRows")
	public byte[] disableRows(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String id = RequestUtils.getString(request, "id");
		String ids = request.getParameter("ids");
		if (StringUtils.isNotEmpty(ids)) {
			StringTokenizer token = new StringTokenizer(ids, ",");
			while (token.hasMoreTokens()) {
				String x = token.nextToken();
				if (StringUtils.isNotEmpty(x)) {
					Scheduler scheduler = sysSchedulerService.getSchedulerByTaskId(String.valueOf(x));
					if (scheduler != null && (StringUtils.equals(scheduler.getCreateBy(), loginContext.getActorId())
							|| loginContext.isSystemAdministrator())) {
						QuartzUtils.stop(scheduler.getId());
						scheduler.setLocked(1);
						sysSchedulerService.save(scheduler);
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			Scheduler scheduler = sysSchedulerService.getSchedulerByTaskId(String.valueOf(id));
			if (scheduler != null && (StringUtils.equals(scheduler.getCreateBy(), loginContext.getActorId())
					|| loginContext.isSystemAdministrator())) {
				QuartzUtils.stop(scheduler.getId());
				scheduler.setLocked(1);
				sysSchedulerService.save(scheduler);
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		Scheduler scheduler = sysSchedulerService.getSchedulerByTaskId(request.getParameter("id"));
		if (scheduler != null) {
			request.setAttribute("scheduler", scheduler);
		}

		List<BaseItem> items = new ArrayList<BaseItem>();
		JobProperties.reload();
		Properties props = JobProperties.getProperties();
		Enumeration<?> e = props.keys();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			String value = props.getProperty(key);
			BaseItem item = new BaseItem();
			item.setName(key);
			item.setValue(value);
			items.add(item);
		}

		request.setAttribute("items", items);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("scheduler.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/scheduler/edit", modelMap);
	}

	@ResponseBody
	@RequestMapping("/enableRows")
	public byte[] enableRows(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String id = RequestUtils.getString(request, "id");
		String ids = request.getParameter("ids");
		if (StringUtils.isNotEmpty(ids)) {
			StringTokenizer token = new StringTokenizer(ids, ",");
			while (token.hasMoreTokens()) {
				String x = token.nextToken();
				if (StringUtils.isNotEmpty(x)) {
					Scheduler scheduler = sysSchedulerService.getSchedulerByTaskId(String.valueOf(x));
					if (scheduler != null && (StringUtils.equals(scheduler.getCreateBy(), loginContext.getActorId())
							|| loginContext.isSystemAdministrator())) {
						// QuartzUtils.stop(scheduler.getId());
						scheduler.setLocked(0);
						// scheduler.setAutoStartup(1);
						sysSchedulerService.save(scheduler);
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			Scheduler scheduler = sysSchedulerService.getSchedulerByTaskId(String.valueOf(id));
			if (scheduler != null && (StringUtils.equals(scheduler.getCreateBy(), loginContext.getActorId())
					|| loginContext.isSystemAdministrator())) {
				// QuartzUtils.stop(scheduler.getId());
				scheduler.setLocked(0);
				// scheduler.setAutoStartup(1);
				sysSchedulerService.save(scheduler);
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug("search params:" + params);
		SchedulerQuery query = new SchedulerQuery();
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
		int total = sysSchedulerService.getSchedulerCountByQueryCriteria(query);
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

			Map<String, User> userMap = IdentityFactory.getUserMap();
			List<Scheduler> list = sysSchedulerService.getSchedulersByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (Scheduler scheduler : list) {
					JSONObject rowJSON = scheduler.toJsonObject();
					if (userMap.get(scheduler.getCreateBy()) != null) {
						rowJSON.put("createUserName", userMap.get(scheduler.getCreateBy()).getName());
					} else {
						rowJSON.put("createUserName", scheduler.getCreateBy());
					}
					rowJSON.put("id", scheduler.getId());
					rowJSON.put("rowId", scheduler.getId());
					rowJSON.put("taskId", scheduler.getId());
					rowJSON.put("schedulerId", scheduler.getId());
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

		return new ModelAndView("/sys/scheduler/list", modelMap);
	}

	@RequestMapping("/locked")
	public ModelAndView locked(HttpServletRequest request, ModelMap modelMap) {
		String taskId = request.getParameter("taskId");
		int locked = 0;
		Scheduler scheduler = null;
		if (StringUtils.isNotEmpty(taskId)) {
			scheduler = sysSchedulerService.getSchedulerByTaskId(taskId);
			if (scheduler != null) {
				sysSchedulerService.locked(taskId, locked);
				if (scheduler.getLocked() == 1) {
					QuartzUtils.stop(taskId);
				}
			}
		}
		return this.showList(request, modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("scheduler.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/sys/scheduler/query", modelMap);
	}

	@ResponseBody
	@RequestMapping("/restart")
	public byte[] restart(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String id = RequestUtils.getString(request, "id");
		String ids = request.getParameter("ids");
		if (StringUtils.isNotEmpty(ids)) {
			StringTokenizer token = new StringTokenizer(ids, ",");
			while (token.hasMoreTokens()) {
				String x = token.nextToken();
				if (StringUtils.isNotEmpty(x)) {
					Scheduler scheduler = sysSchedulerService.getSchedulerByTaskId(String.valueOf(x));
					if (scheduler != null && (StringUtils.equals(scheduler.getCreateBy(), loginContext.getActorId())
							|| loginContext.isSystemAdministrator())) {
						QuartzUtils.restart(scheduler.getId());
						logger.info("正在运行任务“" + scheduler.getTaskName() + "”");
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			Scheduler scheduler = sysSchedulerService.getSchedulerByTaskId(String.valueOf(id));
			if (scheduler != null && (StringUtils.equals(scheduler.getCreateBy(), loginContext.getActorId())
					|| loginContext.isSystemAdministrator())) {
				QuartzUtils.restart(scheduler.getId());
				logger.info("正在运行任务“" + scheduler.getTaskName() + "”");
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@ResponseBody
	@RequestMapping("/runIt")
	public byte[] runIt(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String id = RequestUtils.getString(request, "id");
		String ids = request.getParameter("ids");
		if (StringUtils.isNotEmpty(ids)) {
			StringTokenizer token = new StringTokenizer(ids, ",");
			while (token.hasMoreTokens()) {
				String x = token.nextToken();
				if (StringUtils.isNotEmpty(x)) {
					Scheduler scheduler = sysSchedulerService.getSchedulerByTaskId(String.valueOf(x));
					if (scheduler != null && (StringUtils.equals(scheduler.getCreateBy(), loginContext.getActorId())
							|| loginContext.isSystemAdministrator())) {
						QuartzUtils.runJob(scheduler.getId());
						logger.info("正在运行任务“" + scheduler.getTaskName() + "”");
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			Scheduler scheduler = sysSchedulerService.getSchedulerByTaskId(String.valueOf(id));
			if (scheduler != null && (StringUtils.equals(scheduler.getCreateBy(), loginContext.getActorId())
					|| loginContext.isSystemAdministrator())) {
				QuartzUtils.runJob(scheduler.getId());
				logger.info("正在运行任务“" + scheduler.getTaskName() + "”");
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/save")
	public ModelAndView save(HttpServletRequest request, ModelMap modelMap) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);

		Scheduler scheduler = new Scheduler();
		Tools.populate(scheduler, params);

		scheduler.setAttribute(request.getParameter("attribute"));
		scheduler.setAutoStartup(RequestUtils.getInt(request, "autoStartup"));
		scheduler.setContent(request.getParameter("content"));
		scheduler.setCreateBy(request.getParameter("createBy"));
		scheduler.setCreateDate(RequestUtils.getDate(request, "createDate"));
		scheduler.setEndDate(RequestUtils.getDate(request, "endDate"));
		scheduler.setExpression(request.getParameter("expression"));
		scheduler.setIntervalTime(request.getParameter("intervalTime"));
		scheduler.setIntervalType(request.getParameter("intervalType"));
		scheduler.setIntervalValue(request.getParameter("intervalValue"));
		scheduler.setJobClass(request.getParameter("jobClass"));
		scheduler.setSpringClass(request.getParameter("springClass"));
		scheduler.setSpringBeanId(request.getParameter("springBeanId"));
		scheduler.setMethodName(request.getParameter("methodName"));
		scheduler.setLocked(RequestUtils.getInt(request, "locked"));
		scheduler.setPriority(RequestUtils.getInt(request, "priority"));
		scheduler.setRepeatCount(RequestUtils.getInt(request, "repeatCount"));
		scheduler.setRepeatInterval(RequestUtils.getInt(request, "repeatInterval"));
		scheduler.setStartDate(RequestUtils.getDate(request, "startDate"));
		scheduler.setStartDelay(RequestUtils.getInt(request, "startDelay"));
		scheduler.setStartup(RequestUtils.getInt(request, "startup"));
		scheduler.setTaskName(request.getParameter("taskName"));
		scheduler.setTaskType(request.getParameter("taskType"));
		scheduler.setThreadSize(RequestUtils.getInt(request, "threadSize"));
		scheduler.setTitle(request.getParameter("title"));
		scheduler.setRunType(RequestUtils.getInt(request, "runType"));
		scheduler.setRunStatus(RequestUtils.getInt(request, "runStatus"));
		scheduler.setJobRunTime(RequestUtils.getLong(request, "jobRunTime"));
		scheduler.setNextFireTime(RequestUtils.getDate(request, "nextFireTime"));
		scheduler.setPreviousFireTime(RequestUtils.getDate(request, "previousFireTime"));

		scheduler.setCreateBy(actorId);

		sysSchedulerService.save(scheduler);

		return this.list(request, modelMap);
	}

	@RequestMapping("/saveModify")
	public ModelAndView saveModify(HttpServletRequest request, ModelMap modelMap, Scheduler schedulerForm) {
		Scheduler scheduler = new Scheduler();

		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug(params);
		try {
			PropertyUtils.copyProperties(scheduler, schedulerForm);
		} catch (Exception ex) {

		}
		scheduler.setJobClass(request.getParameter("jobClass"));
		Date startDate = ParamUtils.getDate(params, "startDate");
		Date endDate = ParamUtils.getDate(params, "endDate");
		scheduler.setStartDate(startDate);
		scheduler.setEndDate(endDate);
		if (StringUtils.isNotEmpty(scheduler.getJobClass())) {
			try {
				Class<?> clazz = Class.forName(scheduler.getJobClass());
				Object object = clazz.newInstance();
				if (!(object instanceof org.quartz.Job)) {

				}
			} catch (Exception ex) {
				logger.debug(ex);
			}
		}

		String actorId = RequestUtils.getActorId(request);
		scheduler.setCreateBy(actorId);
		sysSchedulerService.save(scheduler);

		return this.showList(request, modelMap);
	}

	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	public @ResponseBody Scheduler saveOrUpdate(HttpServletRequest request, @RequestBody Map<String, Object> model) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		Scheduler scheduler = new Scheduler();
		try {
			Tools.populate(scheduler, model);
			scheduler.setAttribute(ParamUtils.getString(model, "attribute"));
			scheduler.setAutoStartup(ParamUtils.getInt(model, "autoStartup"));
			scheduler.setContent(ParamUtils.getString(model, "content"));
			scheduler.setCreateBy(ParamUtils.getString(model, "createBy"));
			scheduler.setCreateDate(ParamUtils.getDate(model, "createDate"));
			scheduler.setEndDate(ParamUtils.getDate(model, "endDate"));
			scheduler.setExpression(ParamUtils.getString(model, "expression"));
			scheduler.setIntervalTime(ParamUtils.getString(model, "intervalTime"));
			scheduler.setIntervalType(ParamUtils.getString(model, "intervalType"));
			scheduler.setIntervalValue(ParamUtils.getString(model, "intervalValue"));
			scheduler.setJobClass(ParamUtils.getString(model, "jobClass"));
			scheduler.setSpringClass(request.getParameter("springClass"));
			scheduler.setSpringBeanId(request.getParameter("springBeanId"));
			scheduler.setMethodName(request.getParameter("methodName"));
			scheduler.setLocked(ParamUtils.getInt(model, "locked"));
			scheduler.setPriority(ParamUtils.getInt(model, "priority"));
			scheduler.setRepeatCount(ParamUtils.getInt(model, "repeatCount"));
			scheduler.setRepeatInterval(ParamUtils.getInt(model, "repeatInterval"));
			scheduler.setStartDate(ParamUtils.getDate(model, "startDate"));
			scheduler.setStartDelay(ParamUtils.getInt(model, "startDelay"));
			scheduler.setStartup(ParamUtils.getInt(model, "startup"));
			scheduler.setTaskName(ParamUtils.getString(model, "taskName"));
			scheduler.setTaskType(ParamUtils.getString(model, "taskType"));
			scheduler.setThreadSize(ParamUtils.getInt(model, "threadSize"));
			scheduler.setTitle(ParamUtils.getString(model, "title"));
			scheduler.setRunType(ParamUtils.getInt(model, "runType"));
			scheduler.setRunStatus(ParamUtils.getInt(model, "runStatus"));
			scheduler.setJobRunTime(ParamUtils.getLong(model, "jobRunTime"));
			scheduler.setNextFireTime(ParamUtils.getDate(model, "nextFireTime"));
			scheduler.setPreviousFireTime(ParamUtils.getDate(model, "previousFireTime"));
			scheduler.setCreateBy(actorId);

			this.sysSchedulerService.save(scheduler);
		} catch (Exception ex) {

			logger.error(ex);
		}
		return scheduler;
	}

	@ResponseBody
	@RequestMapping("/saveScheduler ")
	public byte[] saveScheduler(HttpServletRequest request) {
		User user = RequestUtils.getUser(request);
		String actorId = user.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		Scheduler scheduler = new Scheduler();
		try {
			Tools.populate(scheduler, params);
			scheduler.setAttribute(request.getParameter("attribute"));
			scheduler.setAutoStartup(RequestUtils.getInt(request, "autoStartup"));
			scheduler.setContent(request.getParameter("content"));
			scheduler.setCreateBy(request.getParameter("createBy"));
			scheduler.setCreateDate(RequestUtils.getDate(request, "createDate"));
			scheduler.setEndDate(RequestUtils.getDate(request, "endDate"));
			scheduler.setExpression(request.getParameter("expression"));
			scheduler.setIntervalTime(request.getParameter("intervalTime"));
			scheduler.setIntervalType(request.getParameter("intervalType"));
			scheduler.setIntervalValue(request.getParameter("intervalValue"));
			scheduler.setJobClass(request.getParameter("jobClass"));
			scheduler.setSpringClass(request.getParameter("springClass"));
			scheduler.setSpringBeanId(request.getParameter("springBeanId"));
			scheduler.setMethodName(request.getParameter("methodName"));
			scheduler.setLocked(RequestUtils.getInt(request, "locked"));
			scheduler.setPriority(RequestUtils.getInt(request, "priority"));
			scheduler.setRepeatCount(RequestUtils.getInt(request, "repeatCount"));
			scheduler.setRepeatInterval(RequestUtils.getInt(request, "repeatInterval"));
			scheduler.setStartDate(RequestUtils.getDate(request, "startDate"));
			scheduler.setStartDelay(RequestUtils.getInt(request, "startDelay"));
			scheduler.setStartup(RequestUtils.getInt(request, "startup"));
			scheduler.setTaskName(request.getParameter("taskName"));
			scheduler.setTaskType(request.getParameter("taskType"));
			scheduler.setThreadSize(RequestUtils.getInt(request, "threadSize"));
			scheduler.setTitle(request.getParameter("title"));
			scheduler.setRunType(RequestUtils.getInt(request, "runType"));
			scheduler.setRunStatus(RequestUtils.getInt(request, "runStatus"));
			scheduler.setJobRunTime(RequestUtils.getLong(request, "jobRunTime"));
			scheduler.setNextFireTime(RequestUtils.getDate(request, "nextFireTime"));
			scheduler.setPreviousFireTime(RequestUtils.getDate(request, "previousFireTime"));
			scheduler.setCreateBy(actorId);

			this.sysSchedulerService.save(scheduler);

			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {

			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource
	public void setSchedulerLogService(ISchedulerLogService schedulerLogService) {
		this.schedulerLogService = schedulerLogService;
	}

	@javax.annotation.Resource
	public void setSysSchedulerService(ISysSchedulerService sysSchedulerService) {
		this.sysSchedulerService = sysSchedulerService;
	}

	@RequestMapping("/showList")
	public ModelAndView showList(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String actorId = RequestUtils.getActorId(request);
		List<Scheduler> list = sysSchedulerService.getUserSchedulers(actorId);
		request.setAttribute("schedulers", list);

		String x_view = ViewProperties.getString("scheduler.showList");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/scheduler/scheduler_list", modelMap);
	}

	@RequestMapping("/showModify")
	public ModelAndView showModify(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String taskId = request.getParameter("taskId");
		Scheduler scheduler = null;
		if (StringUtils.isNotEmpty(taskId)) {
			scheduler = sysSchedulerService.getSchedulerByTaskId(taskId);
		}
		request.setAttribute("scheduler", scheduler);

		String x_view = ViewProperties.getString("scheduler.showModify");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/scheduler/scheduler_modify", modelMap);
	}

	@RequestMapping("/startup")
	public ModelAndView startup(HttpServletRequest request, ModelMap modelMap) {
		String taskId = request.getParameter("taskId");
		String startup = request.getParameter("startup");
		Scheduler scheduler = null;
		if (StringUtils.isNotEmpty(taskId)) {
			scheduler = sysSchedulerService.getSchedulerByTaskId(taskId);
			if (scheduler != null) {
				if (StringUtils.equals(startup, "1")) {
					logger.debug("停止调度......");
					QuartzUtils.stop(taskId);
					logger.debug("重启调度......");
					QuartzUtils.restart(taskId);
				} else {
					logger.debug("停止调度......");
					QuartzUtils.stop(taskId);
				}
			}
		}
		return this.showList(request, modelMap);
	}

	@RequestMapping("/update")
	public ModelAndView update(HttpServletRequest request, ModelMap modelMap) {
		User user = RequestUtils.getUser(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);

		Scheduler scheduler = sysSchedulerService.getSchedulerByTaskId(request.getParameter("id"));

		if (scheduler != null) {
			Tools.populate(scheduler, params);

			scheduler.setCreateBy(user.getActorId());
			scheduler.setAttribute(request.getParameter("attribute"));
			scheduler.setAutoStartup(RequestUtils.getInt(request, "autoStartup"));
			scheduler.setContent(request.getParameter("content"));
			scheduler.setCreateBy(request.getParameter("createBy"));
			scheduler.setCreateDate(RequestUtils.getDate(request, "createDate"));
			scheduler.setEndDate(RequestUtils.getDate(request, "endDate"));
			scheduler.setExpression(request.getParameter("expression"));
			scheduler.setIntervalTime(request.getParameter("intervalTime"));
			scheduler.setIntervalType(request.getParameter("intervalType"));
			scheduler.setIntervalValue(request.getParameter("intervalValue"));
			scheduler.setJobClass(request.getParameter("jobClass"));
			scheduler.setSpringClass(request.getParameter("springClass"));
			scheduler.setSpringBeanId(request.getParameter("springBeanId"));
			scheduler.setMethodName(request.getParameter("methodName"));
			scheduler.setLocked(RequestUtils.getInt(request, "locked"));
			scheduler.setPriority(RequestUtils.getInt(request, "priority"));
			scheduler.setRepeatCount(RequestUtils.getInt(request, "repeatCount"));
			scheduler.setRepeatInterval(RequestUtils.getInt(request, "repeatInterval"));
			scheduler.setStartDate(RequestUtils.getDate(request, "startDate"));
			scheduler.setStartDelay(RequestUtils.getInt(request, "startDelay"));
			scheduler.setStartup(RequestUtils.getInt(request, "startup"));
			scheduler.setTaskName(request.getParameter("taskName"));
			scheduler.setTaskType(request.getParameter("taskType"));
			scheduler.setThreadSize(RequestUtils.getInt(request, "threadSize"));
			scheduler.setTitle(request.getParameter("title"));
			scheduler.setRunType(RequestUtils.getInt(request, "runType"));
			scheduler.setRunStatus(RequestUtils.getInt(request, "runStatus"));
			scheduler.setJobRunTime(RequestUtils.getLong(request, "jobRunTime"));
			scheduler.setNextFireTime(RequestUtils.getDate(request, "nextFireTime"));
			scheduler.setPreviousFireTime(RequestUtils.getDate(request, "previousFireTime"));

			sysSchedulerService.save(scheduler);
		}

		return this.list(request, modelMap);
	}

	@RequestMapping("/view")
	public ModelAndView view(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		Scheduler scheduler = sysSchedulerService.getSchedulerByTaskId(request.getParameter("id"));
		request.setAttribute("scheduler", scheduler);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view);
		}

		String x_view = ViewProperties.getString("scheduler.view");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view);
		}

		return new ModelAndView("/sys/scheduler/view");
	}

}
