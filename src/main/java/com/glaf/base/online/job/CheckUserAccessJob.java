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

package com.glaf.base.online.job;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.glaf.base.online.domain.UserOnline;
import com.glaf.base.online.service.UserOnlineService;
import com.glaf.core.access.domain.AccessTotal;
import com.glaf.core.access.factory.AccessLogFactory;
import com.glaf.core.access.query.AccessLogQuery;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.job.BaseJob;
import com.glaf.core.util.ContextUtils;
import com.glaf.core.util.DateUtils;

/**
 * 检查用户访问数量Job
 *
 */
public class CheckUserAccessJob extends BaseJob {

	protected static AtomicLong lastExecuteTime = new AtomicLong(System.currentTimeMillis());

	@Override
	public void runJob(JobExecutionContext context) throws JobExecutionException {
		if ((System.currentTimeMillis() - lastExecuteTime.get()) < DateUtils.MINUTE) {
			return;
		}
		UserOnlineService userOnlineService = ContextFactory.getBean("userOnlineService");
		List<UserOnline> list = userOnlineService.getTodayUserOnlines();
		if (list != null && !list.isEmpty()) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			int hour = cal.get(Calendar.HOUR_OF_DAY) - 1;
			if (hour < 0) {
				hour = 0;
			}
			AccessLogQuery query = new AccessLogQuery();
			for (UserOnline userOnline : list) {
				String userId = userOnline.getActorId();
				if (StringUtils.equals(userId, "admin")) {
					continue;
				}
				try {
					query.userId(userId);
					query.setHourGreaterThanOrEqual(hour);
					List<AccessTotal> rows = AccessLogFactory.getInstance().getAccessTotal(query);
					ContextUtils.removeDenyAccess(userId);// 默认不设置黑名单
					if (rows != null && !rows.isEmpty()) {
						int total = 0;
						int perMinute = 0;
						for (AccessTotal model : rows) {
							total = total + model.getQuantity();
							if (model.getQuantity() > 500) {
								perMinute = perMinute + 1;
							}
						}
						/**
						 * 如果1小时内有10次每分钟超过限制或1小时内总访问次数超过限制，设置黑名单
						 */
						if (perMinute >= 10 || total >= 8000) {
							ContextUtils.addDenyAccess(userId);
						}
					}
				} catch (Exception ex) {
					logger.error(ex);
				}
			}
		}
		lastExecuteTime.set(System.currentTimeMillis());
	}

}
