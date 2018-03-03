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

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.base.modules.sys.service.TenantConfigService;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.heathcare.domain.DietaryRptModel;
import com.glaf.heathcare.service.DietaryItemService;
import com.glaf.heathcare.service.DietaryStatisticsService;
import com.glaf.heathcare.service.DietaryTemplateService;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.heathcare.service.FoodDRIPercentService;
import com.glaf.heathcare.service.FoodDRIService;

public class DietaryStatisticsBean {
	protected static final Log logger = LogFactory.getLog(DietaryStatisticsBean.class);

	protected TenantConfigService tenantConfigService;

	protected DietaryItemService dietaryItemService;

	protected DietaryTemplateService dietaryTemplateService;

	protected DietaryStatisticsService dietaryStatisticsService;

	protected FoodCompositionService foodCompositionService;

	protected FoodDRIService foodDRIService;

	protected FoodDRIPercentService foodDRIPercentService;

	public void execute(LoginContext loginContext, int suitNo, int dayOfWeek, String sysFlag) {
		getDietaryStatisticsService().execute(loginContext, suitNo, dayOfWeek, sysFlag);
	}

	public void execute(LoginContext loginContext, int suitNo, String sysFlag) {
		getDietaryStatisticsService().execute(loginContext, suitNo, sysFlag);
	}

	public void executeAll() {
		LoginContext loginContext = com.glaf.core.security.IdentityFactory.getLoginContext("admin");
		DietaryStatisticsTask task = null;
		ForkJoinPool pool = ForkJoinPool.commonPool();
		try {
			logger.info("开始并行任务......");
			for (int i = 1; i <= 80; i++) {
				task = new DietaryStatisticsTask(loginContext, i, "Y");
				pool.submit(task);
				// com.glaf.core.util.ThreadFactory.execute(task);
				for (int j = 1; j <= 5; j++) {
					task = new DietaryStatisticsTask(loginContext, i, j, "Y");
					pool.submit(task);
					// com.glaf.core.util.ThreadFactory.execute(task);
				}
			}
			// 线程阻塞，等待所有任务完成
			try {
				pool.awaitTermination(2, TimeUnit.SECONDS);
			} catch (InterruptedException ex) {
			}
		} catch (Exception ex) {
			logger.error(ex);
		} finally {
			pool.shutdown();
			logger.info("并行任务已经结束。");
		}
	}

	public DietaryItemService getDietaryItemService() {
		if (dietaryItemService == null) {
			dietaryItemService = ContextFactory.getBean("com.glaf.heathcare.service.dietaryItemService");
		}
		return dietaryItemService;
	}

	public DietaryStatisticsService getDietaryStatisticsService() {
		if (dietaryStatisticsService == null) {
			dietaryStatisticsService = ContextFactory.getBean("com.glaf.heathcare.service.dietaryStatisticsService");
		}
		return dietaryStatisticsService;
	}

	public DietaryTemplateService getDietaryTemplateService() {
		if (dietaryTemplateService == null) {
			dietaryTemplateService = ContextFactory.getBean("com.glaf.heathcare.service.dietaryTemplateService");
		}
		return dietaryTemplateService;
	}

	public FoodCompositionService getFoodCompositionService() {
		if (foodCompositionService == null) {
			foodCompositionService = ContextFactory.getBean("com.glaf.heathcare.service.foodCompositionService");
		}
		return foodCompositionService;
	}

	public FoodDRIPercentService getFoodDRIPercentService() {
		if (foodDRIPercentService == null) {
			foodDRIPercentService = ContextFactory.getBean("com.glaf.heathcare.service.foodDRIPercentService");
		}
		return foodDRIPercentService;
	}

	public FoodDRIService getFoodDRIService() {
		if (foodDRIService == null) {
			foodDRIService = ContextFactory.getBean("com.glaf.heathcare.service.foodDRIService");
		}
		return foodDRIService;
	}

	public int getSize(List<DietaryRptModel> list) {
		int total = 0;
		for (DietaryRptModel rptModel : list) {
			if (rptModel.getItems() != null && rptModel.getItems().size() > 0) {
				total = total + rptModel.getItems().size();
			}
		}
		return total;
	}

	public TenantConfigService getTenantConfigService() {
		if (tenantConfigService == null) {
			tenantConfigService = ContextFactory.getBean("tenantConfigService");
		}
		return tenantConfigService;
	}

	public void setDietaryItemService(DietaryItemService dietaryItemService) {
		this.dietaryItemService = dietaryItemService;
	}

	public void setDietaryStatisticsService(DietaryStatisticsService dietaryStatisticsService) {
		this.dietaryStatisticsService = dietaryStatisticsService;
	}

	public void setDietaryTemplateService(DietaryTemplateService dietaryTemplateService) {
		this.dietaryTemplateService = dietaryTemplateService;
	}

	public void setTenantConfigService(TenantConfigService tenantConfigService) {
		this.tenantConfigService = tenantConfigService;
	}

}
