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

import com.glaf.core.context.ContextFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.ParamUtils;
import com.glaf.heathcare.domain.GoodsActualQuantity;
import com.glaf.heathcare.query.GoodsActualQuantityQuery;
import com.glaf.heathcare.service.GoodsActualQuantityService;

public class DailyGoodsActualQuantityPreprocessor implements IReportPreprocessor {

	@Override
	public void prepare(LoginContext loginContext, int year, int month, Map<String, Object> params) {
		Date date = ParamUtils.getDate(params, "date");
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			params.put("year", calendar.get(Calendar.YEAR));
			params.put("month", calendar.get(Calendar.MONTH) + 1);
			params.put("day", calendar.get(Calendar.DAY_OF_MONTH));

			String tenantId = loginContext.getTenantId();
			GoodsActualQuantityService goodsActualQuantityService = ContextFactory
					.getBean("com.glaf.heathcare.service.goodsActualQuantityService");
			GoodsActualQuantityQuery query = new GoodsActualQuantityQuery();
			query.tenantId(tenantId);
			query.businessStatus(9);
			query.fullDay(DateUtils.getYearMonthDay(date));
			List<GoodsActualQuantity> rows = goodsActualQuantityService.list(query);
			if (rows != null && !rows.isEmpty()) {
				List<GoodsActualQuantity> rows1 = new ArrayList<GoodsActualQuantity>();
				List<GoodsActualQuantity> rows2 = new ArrayList<GoodsActualQuantity>();
				List<GoodsActualQuantity> rows3 = new ArrayList<GoodsActualQuantity>();
				for (GoodsActualQuantity m : rows) {
					switch ((int) m.getGoodsNodeId()) {
					case 4405:
					case 4406:
					case 4407:
						rows1.add(m);
						break;
					case 4409:
					case 4410:
					case 4411:
					case 4412:
					case 4413:
						rows2.add(m);
						break;
					default:
						rows3.add(m);
						break;
					}
				}
				
				int max = Math.max(rows1.size(), rows2.size());
				max = Math.max(max, rows3.size());
				
				for (int i = 0, len = rows1.size(); i < max - len; i++) {
					GoodsActualQuantity m = new GoodsActualQuantity();
					rows1.add(m);
				}
				for (int i = 0, len = rows2.size(); i < max - len; i++) {
					GoodsActualQuantity m = new GoodsActualQuantity();
					rows2.add(m);
				}
				for (int i = 0, len = rows3.size(); i < max - len; i++) {
					GoodsActualQuantity m = new GoodsActualQuantity();
					rows3.add(m);
				}
				params.put("items1", rows1);
				params.put("rows1", rows1);
				params.put("items2", rows2);
				params.put("rows2", rows2);
				params.put("items3", rows3);
				params.put("rows3", rows3);
			}
		}
	}

}
