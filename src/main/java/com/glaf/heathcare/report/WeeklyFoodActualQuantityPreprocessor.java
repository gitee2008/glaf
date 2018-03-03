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

package com.glaf.heathcare.report;

import java.util.*;
import java.util.Map.Entry;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.ParamUtils;
import com.glaf.heathcare.domain.ActualRepastPerson;
import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.domain.GoodsActualQuantity;
import com.glaf.heathcare.domain.RptModel;
import com.glaf.heathcare.query.ActualRepastPersonQuery;
import com.glaf.heathcare.query.FoodCompositionQuery;
import com.glaf.heathcare.query.GoodsActualQuantityQuery;
import com.glaf.heathcare.service.ActualRepastPersonService;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.heathcare.service.GoodsActualQuantityService;

public class WeeklyFoodActualQuantityPreprocessor implements IReportPreprocessor {

	@Override
	public void prepare(LoginContext loginContext, int year, int month, Map<String, Object> params) {
		Date startDate = ParamUtils.getDate(params, "startDate");
		Date endDate = ParamUtils.getDate(params, "endDate");
		if (startDate != null && endDate != null) {
			String tenantId = loginContext.getTenantId();
			ActualRepastPersonService actualRepastPersonService = ContextFactory
					.getBean("com.glaf.heathcare.service.actualRepastPersonService");
			FoodCompositionService foodCompositionService = ContextFactory
					.getBean("com.glaf.heathcare.service.foodCompositionService");
			GoodsActualQuantityService goodsActualQuantityService = ContextFactory
					.getBean("com.glaf.heathcare.service.goodsActualQuantityService");

			FoodCompositionQuery q = new FoodCompositionQuery();
			List<FoodComposition> foods = foodCompositionService.list(q);
			Map<Long, FoodComposition> foodMap = new HashMap<Long, FoodComposition>();
			if (foods != null && !foods.isEmpty()) {
				for (FoodComposition food : foods) {
					foodMap.put(food.getId(), food);
				}
			}

			ActualRepastPersonQuery q2 = new ActualRepastPersonQuery();
			q2.tenantId(tenantId);
			q2.fullDayGreaterThanOrEqual(DateUtils.getYearMonthDay(startDate));
			q2.fullDayLessThanOrEqual(DateUtils.getYearMonthDay(endDate));

			Calendar calendar = Calendar.getInstance();

			List<ActualRepastPerson> persons = actualRepastPersonService.list(q2);
			Map<Integer, Integer> personMap = new HashMap<Integer, Integer>();
			int totalPerson = 0;
			if (persons != null && !persons.isEmpty()) {
				for (ActualRepastPerson p : persons) {
					totalPerson = totalPerson + p.getMale();
					totalPerson = totalPerson + p.getFemale();

					calendar.setTime(DateUtils.toDate(p.getFullDay()));
					int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
					Integer xy = personMap.get(dayOfWeek);
					if (xy == null) {
						xy = new Integer(0);
					}
					xy = xy + p.getMale() + p.getFemale();
					personMap.put(dayOfWeek, xy);
				}
			}

			Set<Entry<Integer, Integer>> entrySet = personMap.entrySet();
			for (Entry<Integer, Integer> entry : entrySet) {
				Integer key = entry.getKey();
				Integer value = entry.getValue();
				params.put("personTotal" + (key - 1), value);
			}

			params.put("personTotal", totalPerson);

			GoodsActualQuantityQuery query = new GoodsActualQuantityQuery();
			query.tenantId(tenantId);
			query.businessStatus(9);
			query.usageTimeGreaterThanOrEqual(startDate);
			query.usageTimeLessThanOrEqual(endDate);

			List<GoodsActualQuantity> rows = goodsActualQuantityService.list(query);

			Set<Integer> fullDaySet = new HashSet<Integer>();

			if (rows != null && !rows.isEmpty()) {
				for (GoodsActualQuantity m : rows) {
					if (!fullDaySet.contains(m.getFullDay())) {
						fullDaySet.add(m.getFullDay());
					}
				}
			}

			int days = fullDaySet.size();

			if (rows != null && !rows.isEmpty() && totalPerson * days > 0) {
				double quantity = 0.0;
				int totalX = totalPerson * days;
				Map<Long, RptModel> dataMap = new HashMap<Long, RptModel>();
				for (GoodsActualQuantity m : rows) {
					calendar.setTime(m.getUsageTime());
					quantity = m.getQuantity();
					quantity = quantity * 1000;// 统一将千克（kg）转换成克（g）
					quantity = quantity / totalX;// 转成每人食用克数
					RptModel model = dataMap.get(m.getGoodsId());
					if (model == null) {
						model = new RptModel();
						FoodComposition fd = foodMap.get(m.getGoodsId());
						if (fd != null) {
							model.setName(fd.getName());
						}
						dataMap.put(m.getGoodsId(), model);
					}
					int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
					switch (dayOfWeek) {
					case Calendar.MONDAY:
						model.setQuantity1(model.getQuantity1() + quantity);
						break;
					case Calendar.TUESDAY:
						model.setQuantity2(model.getQuantity2() + quantity);
						break;
					case Calendar.WEDNESDAY:
						model.setQuantity3(model.getQuantity3() + quantity);
						break;
					case Calendar.THURSDAY:
						model.setQuantity4(model.getQuantity4() + quantity);
						break;
					case Calendar.FRIDAY:
						model.setQuantity5(model.getQuantity5() + quantity);
						break;
					case Calendar.SATURDAY:
						model.setQuantity6(model.getQuantity6() + quantity);
						break;
					case Calendar.SUNDAY:
						model.setQuantity7(model.getQuantity7() + quantity);
						break;
					}
					model.setTotalQuantity(model.getTotalQuantity() + quantity);
					model.setAvgQuantity(model.getTotalQuantity() / days);
				}

				params.put("startDate", DateUtils.getDateTime("yyyy年MM月dd日", startDate));
				params.put("endDate", DateUtils.getDateTime("yyyy年MM月dd日", endDate));
				params.put("weekList", dataMap.values());
			}
		}
	}

}
