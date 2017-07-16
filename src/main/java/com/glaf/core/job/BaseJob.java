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
package com.glaf.core.job;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

 
import com.glaf.core.context.ContextFactory;
import com.glaf.core.domain.Scheduler;
import com.glaf.core.domain.SchedulerExecution;
import com.glaf.core.domain.SchedulerLog;
import com.glaf.core.security.Authentication;
import com.glaf.core.service.ISchedulerExecutionService;
import com.glaf.core.service.ISchedulerLogService;
import com.glaf.core.service.ISysSchedulerService;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.UUID32;

/**
 * 调度基础类 <br/>
 * 所有的调度都应当继承本类
 */
public abstract class BaseJob implements Job {

	protected final static Log logger = LogFactory.getLog(BaseJob.class);

	/**
	 * 继承本类应该实现抽象方法
	 * 
	 * @param context
	 */
	public abstract void runJob(JobExecutionContext context) throws JobExecutionException;

	public void execute(JobExecutionContext context) throws JobExecutionException {
		String taskId = context.getJobDetail().getJobDataMap().getString("taskId");
		ISysSchedulerService sysSchedulerService = ContextFactory.getBean("sysSchedulerService");
		ISchedulerLogService schedulerLogService = ContextFactory.getBean("schedulerLogService");
		ISchedulerExecutionService schedulerExecutionService = ContextFactory.getBean("schedulerExecutionService");
		Scheduler scheduler = sysSchedulerService.getSchedulerByTaskId(taskId);
		if (scheduler != null) {
			int runDay = DateUtils.getNowYearMonthDay();
			int count = schedulerExecutionService.getSchedulerExecutionCount(taskId, runDay, 9);
			// （60*24*12=1440*12即不得超过每分钟12次）运行成功且超过最大允许次数，防止错误配置及恶意调度
			if (count > 17280) {
				logger.info(scheduler.getTitle() + " 已经在超过当天最大运行次数。");
				return;
			}

			if (scheduler.getRunType() == 0) {// 每天只能运行一次
				if (scheduler.getRunStatus() == 1 && (scheduler.getFireTime() != null
						&& (System.currentTimeMillis() - scheduler.getFireTime().getTime()) < DateUtils.HOUR)) {// 运行中
					logger.info(scheduler.getTitle() + " 已经在运行中了，请等待执行完成。");
					return;
				}
				if (count > 0) {// 运行成功
					logger.info(scheduler.getTitle() + " 已经运行了，不再重复运行，如需重新运行，请重置运行状态。");
					return;
				}
			} else if (scheduler.getRunType() == 1) {// 每天可重复运行，但只能有一个实例运行
				if (scheduler.getRunStatus() == 1 && (scheduler.getFireTime() != null
						&& (System.currentTimeMillis() - scheduler.getFireTime().getTime()) < DateUtils.MINUTE * 30)) {// 运行中
					logger.info(scheduler.getTitle() + " 已经在运行中了，请等待执行完成。");
					return;
				}
			}

			SchedulerExecution schedulerExecution = new SchedulerExecution();
			schedulerExecution.setJobNo("job_" + DateUtils.getNowYearMonthDayHHmmss());
			schedulerExecution.setSchedulerId(taskId);

			SchedulerLog log = new SchedulerLog();
			log.setId(UUID32.getUUID());
			log.setTaskId(scheduler.getTaskId());
			log.setTaskName(scheduler.getTaskName());
			log.setTitle(scheduler.getTitle());
			log.setContent(scheduler.getContent());
			log.setStartDate(new Date());

			if (Authentication.getAuthenticatedActorId() != null) {
				log.setCreateBy(Authentication.getAuthenticatedActorId());
				schedulerExecution.setCreateBy(Authentication.getAuthenticatedActorId());
			} else {
				log.setCreateBy("system");
				schedulerExecution.setCreateBy("system");
			}

			long start = System.currentTimeMillis();
			boolean success = false;
			Date now = new Date();
			try {
				scheduler.setFireTime(new Date());// 设置调度时间
				scheduler.setPreviousFireTime(context.getPreviousFireTime());// 设置前一次调度时间
				if (context.getNextFireTime() != null
						&& context.getNextFireTime().getTime() > System.currentTimeMillis()) {
					scheduler.setNextFireTime(context.getNextFireTime());// 设置下一次调度时间
				}
				scheduler.setRunStatus(1);// 设置状态为正在运行
				sysSchedulerService.update(scheduler);
				log.setStatus(1);
				schedulerLogService.save(log);

				logger.info(scheduler.getTitle() + " 下次运行时间: " + DateUtils.getDateTime(context.getNextFireTime()));

				this.runJob(context);
				success = true;
			} catch (Exception ex) {
				success = false;
				
				logger.error(ex);
				if (ex.getCause() != null) {
					log.setExitMessage(ex.getCause().getMessage());
				} else {
					log.setExitMessage(ex.getMessage());
				}
				// throw new RuntimeException(ex);
			} finally {
				long jobRunTime = System.currentTimeMillis() - start;
				log.setJobRunTime(jobRunTime);
				log.setEndDate(new Date());
				scheduler.setJobRunTime(jobRunTime);
				scheduler.setPreviousFireTime(now);
				schedulerExecution.setRunTime((int) Math.ceil(jobRunTime / 1000D));
				if (success) {
					schedulerExecution.setCount(1);
					schedulerExecution.setStatus(9);
					scheduler.setRunStatus(2);// 设置状态为运行成功完成
					log.setStatus(2);
				} else {
					schedulerExecution.setCount(0);
					schedulerExecution.setStatus(3);
					scheduler.setRunStatus(3);// 设置状态为运行失败
					log.setStatus(3);
				}
				if (scheduler.getRunType() == 0 && success) {
					scheduler.setNextFireTime(null);
				}
				try {
					sysSchedulerService.update(scheduler);
				} catch (Exception ex) {
					logger.error("update scheduler error", ex);
				}
				try {
					schedulerExecutionService.save(schedulerExecution);
				} catch (Exception ex) {
					logger.error("save scheduler execution error ", ex);
				}
				try {
					schedulerLogService.save(log);
				} catch (Exception ex) {
					logger.error("save scheduler log error ", ex);
				}
			}
		}
	}

}
