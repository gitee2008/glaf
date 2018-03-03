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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.glaf.base.modules.sys.service.TenantConfigService;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.DateUtils;

import com.glaf.heathcare.domain.DietaryItem;
import com.glaf.heathcare.domain.DietaryRptModel;
import com.glaf.heathcare.domain.DietaryDayRptModel;
import com.glaf.heathcare.domain.DietaryTemplate;
import com.glaf.heathcare.query.DietaryItemQuery;
import com.glaf.heathcare.query.DietaryTemplateQuery;
import com.glaf.heathcare.service.DietaryItemService;
import com.glaf.heathcare.service.DietaryTemplateService;

public class DietaryTemplateExportBean {

	protected TenantConfigService tenantConfigService;

	protected DietaryTemplateService dietaryTemplateService;

	protected DietaryItemService dietaryItemService;

	public void fillBlank(List<DietaryRptModel> list, int size) {
		int total = 0;
		for (DietaryRptModel rptModel : list) {
			if (rptModel.getItems() != null && rptModel.getItems().size() > 0) {
				total = total + rptModel.getItems().size();
			}
		}

		if (total < size) {
			DietaryItem item = null;
			DietaryRptModel model = new DietaryRptModel();
			List<DietaryItem> items = new ArrayList<DietaryItem>();
			for (int i = 0; i < size - total; i++) {
				item = new DietaryItem();
				items.add(item);
			}
			model.setItems(items);
			list.add(model);
		}
	}

	public DietaryItemService getDietaryItemService() {
		if (dietaryItemService == null) {
			dietaryItemService = ContextFactory.getBean("com.glaf.heathcare.service.dietaryItemService");
		}
		return dietaryItemService;
	}

	public DietaryTemplateService getDietaryTemplateService() {
		if (dietaryTemplateService == null) {
			dietaryTemplateService = ContextFactory.getBean("com.glaf.heathcare.service.dietaryTemplateService");
		}
		return dietaryTemplateService;
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

	public Map<String, Object> prepareData(LoginContext loginContext, int suitNo, String sysFlag,
			Map<String, Object> params) {
		Map<String, Object> context = new HashMap<String, Object>();

		DietaryTemplateQuery query = new DietaryTemplateQuery();
		if (StringUtils.equals(sysFlag, "Y")) {
			query.sysFlag(sysFlag);
		} else {
			if (loginContext.isSystemAdministrator()) {
				query.sysFlag("Y");
			} else {
				query.tenantId(loginContext.getTenantId());
			}
		}
		query.suitNo(suitNo);

		// List<DietaryTemplate> list = getDietaryTemplateService().list(query);

		List<DietaryTemplate> list = null;
		if (StringUtils.equals(sysFlag, "Y")) {
			list = getDietaryTemplateService().getDietaryTemplates(suitNo, sysFlag);
		} else {
			list = getDietaryTemplateService().list(query);
		}

		if (list != null && !list.isEmpty()) {
			List<Long> templateIds = new ArrayList<Long>();
			for (DietaryTemplate dietaryTemplate : list) {
				templateIds.add(dietaryTemplate.getId());
			}
			DietaryItemQuery query2 = new DietaryItemQuery();
			query2.templateIds(templateIds);
			List<DietaryItem> allItems = getDietaryItemService().list(query2);
			if (allItems != null && !allItems.isEmpty()) {

				DietaryRptModel model = null;
				List<DietaryItem> items = null;

				Map<Long, List<DietaryItem>> dietaryTemplateMap = new HashMap<Long, List<DietaryItem>>();
				for (DietaryItem item : allItems) {
					items = dietaryTemplateMap.get(item.getTemplateId());
					if (items == null) {
						items = new ArrayList<DietaryItem>();
					}
					items.add(item);
					dietaryTemplateMap.put(item.getTemplateId(), items);
				}

				Calendar calendar = Calendar.getInstance();

				List<DietaryRptModel> breakfastList1 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> breakfastList2 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> breakfastList3 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> breakfastList4 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> breakfastList5 = new ArrayList<DietaryRptModel>();

				List<DietaryRptModel> breakfastMidList1 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> breakfastMidList2 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> breakfastMidList3 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> breakfastMidList4 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> breakfastMidList5 = new ArrayList<DietaryRptModel>();

				List<DietaryRptModel> lunchList1 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> lunchList2 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> lunchList3 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> lunchList4 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> lunchList5 = new ArrayList<DietaryRptModel>();

				List<DietaryRptModel> snackList1 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> snackList2 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> snackList3 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> snackList4 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> snackList5 = new ArrayList<DietaryRptModel>();

				List<DietaryRptModel> dinnerList1 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> dinnerList2 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> dinnerList3 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> dinnerList4 = new ArrayList<DietaryRptModel>();
				List<DietaryRptModel> dinnerList5 = new ArrayList<DietaryRptModel>();

				List<DietaryDayRptModel> weekList = new ArrayList<DietaryDayRptModel>();

				DietaryDayRptModel day1 = new DietaryDayRptModel();
				day1.setWeekName("星期一");
				day1.setBreakfastList(breakfastList1);
				day1.setBreakfastMidList(breakfastMidList1);
				day1.setLunchList(lunchList1);
				day1.setSnackList(snackList1);
				day1.setDinnerList(dinnerList1);
				weekList.add(day1);

				DietaryDayRptModel day2 = new DietaryDayRptModel();
				day2.setWeekName("星期二");
				day2.setBreakfastList(breakfastList2);
				day2.setBreakfastMidList(breakfastMidList2);
				day2.setLunchList(lunchList2);
				day2.setSnackList(snackList2);
				day2.setDinnerList(dinnerList2);
				weekList.add(day2);

				DietaryDayRptModel day3 = new DietaryDayRptModel();
				day3.setWeekName("星期三");
				day3.setBreakfastList(breakfastList3);
				day3.setBreakfastMidList(breakfastMidList3);
				day3.setLunchList(lunchList3);
				day3.setSnackList(snackList3);
				day3.setDinnerList(dinnerList3);
				weekList.add(day3);

				DietaryDayRptModel day4 = new DietaryDayRptModel();
				day4.setWeekName("星期四");
				day4.setBreakfastList(breakfastList4);
				day4.setBreakfastMidList(breakfastMidList4);
				day4.setLunchList(lunchList4);
				day4.setSnackList(snackList4);
				day4.setDinnerList(dinnerList4);
				weekList.add(day4);

				DietaryDayRptModel day5 = new DietaryDayRptModel();
				day5.setWeekName("星期五");
				day5.setBreakfastList(breakfastList5);
				day5.setBreakfastMidList(breakfastMidList5);
				day5.setLunchList(lunchList5);
				day5.setSnackList(snackList5);
				day5.setDinnerList(dinnerList5);
				weekList.add(day5);

				for (DietaryTemplate template : list) {
					items = dietaryTemplateMap.get(template.getId());
					if (items != null && !items.isEmpty()) {
						model = new DietaryRptModel();
						model.setDietaryTemplate(template);
						model.setName(template.getName());
						model.setItems(items);

						int dayOfWeek = template.getDayOfWeek() + 1;// 加1转换
						long typeId = template.getTypeId();
						switch (dayOfWeek) {
						case Calendar.MONDAY:
							if (day1.getDateName() == null) {
								day1.setDateName(DateUtils.getDate(calendar.getTime()));
							}
							if (typeId == 3302 || typeId == 3312 || typeId == 3402 || typeId == 3412) {
								breakfastList1.add(model);
							}
							if (typeId == 3403 || typeId == 3413) {
								breakfastMidList1.add(model);
							}
							if (typeId == 3303 || typeId == 3313 || typeId == 3323 || typeId == 3313 || typeId == 3404
									|| typeId == 3414) {
								lunchList1.add(model);
							}
							if (typeId == 3304 || typeId == 3314 || typeId == 3324 || typeId == 3405
									|| typeId == 3415) {
								snackList1.add(model);
							}
							if (typeId == 3305 || typeId == 3406) {
								dinnerList1.add(model);
							}
							break;
						case Calendar.TUESDAY:
							if (day2.getDateName() == null) {
								day2.setDateName(DateUtils.getDate(calendar.getTime()));
							}
							if (typeId == 3302 || typeId == 3312 || typeId == 3402 || typeId == 3412) {
								breakfastList2.add(model);
							}
							if (typeId == 3403 || typeId == 3413) {
								breakfastMidList2.add(model);
							}
							if (typeId == 3303 || typeId == 3313 || typeId == 3323 || typeId == 3313 || typeId == 3404
									|| typeId == 3414) {
								lunchList2.add(model);
							}
							if (typeId == 3304 || typeId == 3314 || typeId == 3324 || typeId == 3405
									|| typeId == 3415) {
								snackList2.add(model);
							}
							if (typeId == 3305 || typeId == 3406) {
								dinnerList2.add(model);
							}
							break;
						case Calendar.WEDNESDAY:
							if (day3.getDateName() == null) {
								day3.setDateName(DateUtils.getDate(calendar.getTime()));
							}
							if (typeId == 3302 || typeId == 3312 || typeId == 3402 || typeId == 3412) {
								breakfastList3.add(model);
							}
							if (typeId == 3403 || typeId == 3413) {
								breakfastMidList3.add(model);
							}
							if (typeId == 3303 || typeId == 3313 || typeId == 3323 || typeId == 3313 || typeId == 3404
									|| typeId == 3414) {
								lunchList3.add(model);
							}
							if (typeId == 3304 || typeId == 3314 || typeId == 3324 || typeId == 3405
									|| typeId == 3415) {
								snackList3.add(model);
							}
							if (typeId == 3305 || typeId == 3406) {
								dinnerList3.add(model);
							}
							break;
						case Calendar.THURSDAY:
							if (day4.getDateName() == null) {
								day4.setDateName(DateUtils.getDate(calendar.getTime()));
							}
							if (typeId == 3302 || typeId == 3312 || typeId == 3402 || typeId == 3412) {
								breakfastList4.add(model);
							}
							if (typeId == 3403 || typeId == 3413) {
								breakfastMidList4.add(model);
							}
							if (typeId == 3303 || typeId == 3313 || typeId == 3323 || typeId == 3313 || typeId == 3404
									|| typeId == 3414) {
								lunchList4.add(model);
							}
							if (typeId == 3304 || typeId == 3314 || typeId == 3324 || typeId == 3405
									|| typeId == 3415) {
								snackList4.add(model);
							}
							if (typeId == 3305 || typeId == 3406) {
								dinnerList4.add(model);
							}
							break;
						case Calendar.FRIDAY:
							if (day5.getDateName() == null) {
								day5.setDateName(DateUtils.getDate(calendar.getTime()));
							}
							if (typeId == 3302 || typeId == 3312 || typeId == 3402 || typeId == 3412) {
								breakfastList5.add(model);
							}
							if (typeId == 3403 || typeId == 3413) {
								breakfastMidList5.add(model);
							}
							if (typeId == 3303 || typeId == 3313 || typeId == 3323 || typeId == 3313 || typeId == 3404
									|| typeId == 3414) {
								lunchList5.add(model);
							}
							if (typeId == 3304 || typeId == 3314 || typeId == 3324 || typeId == 3405
									|| typeId == 3415) {
								snackList5.add(model);
							}
							if (typeId == 3305 || typeId == 3406) {
								dinnerList5.add(model);
							}
							break;
						}
					}
				}

				int total = 0;
				total = Math.max(getSize(breakfastList1), getSize(breakfastList2));
				total = Math.max(total, getSize(breakfastList3));
				total = Math.max(total, getSize(breakfastList4));
				total = Math.max(total, getSize(breakfastList5));

				this.fillBlank(breakfastList1, total);
				this.fillBlank(breakfastList2, total);
				this.fillBlank(breakfastList3, total);
				this.fillBlank(breakfastList4, total);
				this.fillBlank(breakfastList5, total);

				context.put("breakfastList1", breakfastList1);
				context.put("breakfastList2", breakfastList2);
				context.put("breakfastList3", breakfastList3);
				context.put("breakfastList4", breakfastList4);
				context.put("breakfastList5", breakfastList5);

				total = Math.max(getSize(breakfastMidList1), getSize(breakfastMidList2));
				total = Math.max(total, getSize(breakfastMidList3));
				total = Math.max(total, getSize(breakfastMidList4));
				total = Math.max(total, getSize(breakfastMidList5));

				this.fillBlank(breakfastMidList1, total);
				this.fillBlank(breakfastMidList2, total);
				this.fillBlank(breakfastMidList3, total);
				this.fillBlank(breakfastMidList4, total);
				this.fillBlank(breakfastMidList5, total);

				context.put("breakfastMidList1", breakfastMidList1);
				context.put("breakfastMidList2", breakfastMidList2);
				context.put("breakfastMidList3", breakfastMidList3);
				context.put("breakfastMidList4", breakfastMidList4);
				context.put("breakfastMidList5", breakfastMidList5);

				total = Math.max(getSize(lunchList1), getSize(lunchList2));
				total = Math.max(total, getSize(lunchList3));
				total = Math.max(total, getSize(lunchList4));
				total = Math.max(total, getSize(lunchList5));

				this.fillBlank(lunchList1, total);
				this.fillBlank(lunchList2, total);
				this.fillBlank(lunchList3, total);
				this.fillBlank(lunchList4, total);
				this.fillBlank(lunchList5, total);

				context.put("lunchList1", lunchList1);
				context.put("lunchList2", lunchList2);
				context.put("lunchList3", lunchList3);
				context.put("lunchList4", lunchList4);
				context.put("lunchList5", lunchList5);

				total = Math.max(getSize(snackList1), getSize(snackList2));
				total = Math.max(total, getSize(snackList3));
				total = Math.max(total, getSize(snackList4));
				total = Math.max(total, getSize(snackList5));

				this.fillBlank(snackList1, total);
				this.fillBlank(snackList2, total);
				this.fillBlank(snackList3, total);
				this.fillBlank(snackList4, total);
				this.fillBlank(snackList5, total);

				context.put("snackList1", snackList1);
				context.put("snackList2", snackList2);
				context.put("snackList3", snackList3);
				context.put("snackList4", snackList4);
				context.put("snackList5", snackList5);

				total = Math.max(getSize(dinnerList1), getSize(dinnerList2));
				total = Math.max(total, getSize(dinnerList3));
				total = Math.max(total, getSize(dinnerList4));
				total = Math.max(total, getSize(dinnerList5));

				this.fillBlank(dinnerList1, total);
				this.fillBlank(dinnerList2, total);
				this.fillBlank(dinnerList3, total);
				this.fillBlank(dinnerList4, total);
				this.fillBlank(dinnerList5, total);

				context.put("dinnerList1", dinnerList1);
				context.put("dinnerList2", dinnerList2);
				context.put("dinnerList3", dinnerList3);
				context.put("dinnerList4", dinnerList4);
				context.put("dinnerList5", dinnerList5);

				context.put("day1", day1);
				context.put("day2", day2);
				context.put("day3", day3);
				context.put("day4", day4);
				context.put("day5", day5);
				context.put("wkdata1", day1);
				context.put("wkdata2", day2);
				context.put("wkdata3", day3);
				context.put("wkdata4", day4);
				context.put("wkdata5", day5);
				context.put("weekList", weekList);

			}
		}
		return context;
	}

	public void setDietaryItemService(DietaryItemService dietaryItemService) {
		this.dietaryItemService = dietaryItemService;
	}

	public void setDietaryTemplateService(DietaryTemplateService dietaryTemplateService) {
		this.dietaryTemplateService = dietaryTemplateService;
	}

	public void setTenantConfigService(TenantConfigService tenantConfigService) {
		this.tenantConfigService = tenantConfigService;
	}

}
