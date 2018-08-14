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

package com.glaf.base.job;

import java.util.concurrent.atomic.AtomicLong;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.glaf.base.modules.BaseDataManager;
import com.glaf.base.modules.sys.util.PinyinUtils;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.job.BaseJob;
import com.glaf.core.util.DateUtils;

public class BaseDataRefreshJob extends BaseJob {

	private static BaseDataManager manager = BaseDataManager.getInstance();// 基础信息管理

	private static AtomicLong lastExecuteTime = new AtomicLong(System.currentTimeMillis());

	@Override
	public void runJob(JobExecutionContext context) throws JobExecutionException {
		if ((System.currentTimeMillis() - lastExecuteTime.get()) < DateUtils.MINUTE * 5) {
			return;
		}
		if (SystemConfig.getBoolean("refreshBaseData")) {
			manager.refreshBaseData();

			try {
				logger.info("------------update tree pinyin-------------------");
				PinyinUtils.processSysTree();
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error("更新树结构错误！");
			}

			try {
				logger.info("------------update application pinyin---------------");
				PinyinUtils.processSysApplication();
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error("更新应用模块数据错误！");
			}

			try {
				logger.info("------------update organization pinyin---------------");
				PinyinUtils.processSysOrganization();
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error("更新机构数据错误！");
			}

			try {
				logger.info("------------update tenant pinyin-------------------");
				PinyinUtils.processSysTenant();
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error("更新租户错误！");
			}

			try {
				logger.info("------------update user pinyin---------------");
				PinyinUtils.processSysUser();
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error("更新用户数据错误！");
			}

		}
		lastExecuteTime.set(System.currentTimeMillis());
	}

}
