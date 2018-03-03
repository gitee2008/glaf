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

package com.glaf.heathcare.bean;

import java.util.concurrent.RecursiveAction;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.heathcare.service.DietaryStatisticsService;

public class DietaryStatisticsTask extends RecursiveAction {

	private static final long serialVersionUID = 1L;

	protected LoginContext loginContext;

	protected int suitNo;

	protected int dayOfWeek;

	protected String sysFlag;

	public DietaryStatisticsTask(LoginContext loginContext, int suitNo, int dayOfWeek, String sysFlag) {
		this.loginContext = loginContext;
		this.suitNo = suitNo;
		this.dayOfWeek = dayOfWeek;
		this.sysFlag = sysFlag;
	}

	public DietaryStatisticsTask(LoginContext loginContext, int suitNo, String sysFlag) {
		this.loginContext = loginContext;
		this.suitNo = suitNo;
		this.sysFlag = sysFlag;
	}

	protected void compute() {
		DietaryStatisticsService dietaryStatisticsService = ContextFactory
				.getBean("com.glaf.heathcare.service.dietaryStatisticsService");
		if (dayOfWeek > 0) {
			dietaryStatisticsService.execute(loginContext, suitNo, dayOfWeek, sysFlag);
		} else {
			dietaryStatisticsService.execute(loginContext, suitNo, sysFlag);
		}
	}

	public void run() {
		DietaryStatisticsService dietaryStatisticsService = ContextFactory
				.getBean("com.glaf.heathcare.service.dietaryStatisticsService");
		if (dayOfWeek > 0) {
			dietaryStatisticsService.execute(loginContext, suitNo, dayOfWeek, sysFlag);
		} else {
			dietaryStatisticsService.execute(loginContext, suitNo, sysFlag);
		}
	}

}
