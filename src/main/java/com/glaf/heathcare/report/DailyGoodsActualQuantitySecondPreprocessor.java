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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.base.modules.sys.model.Dictory;
import com.glaf.base.modules.sys.model.TenantConfig;
import com.glaf.base.modules.sys.service.DictoryService;
import com.glaf.base.modules.sys.service.TenantConfigService;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.ParamUtils;
import com.glaf.heathcare.domain.ActualRepastPerson;
import com.glaf.heathcare.domain.GoodsActualQuantity;
import com.glaf.heathcare.query.ActualRepastPersonQuery;
import com.glaf.heathcare.query.GoodsActualQuantityQuery;
import com.glaf.heathcare.service.ActualRepastPersonService;
import com.glaf.heathcare.service.GoodsActualQuantityService;

public class DailyGoodsActualQuantitySecondPreprocessor implements IReportPreprocessor {
	protected static final Log logger = LogFactory.getLog(DailyGoodsActualQuantitySecondPreprocessor.class);

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
			DictoryService dictoryService = ContextFactory.getBean("dictoryService");
			GoodsActualQuantityService goodsActualQuantityService = ContextFactory
					.getBean("com.glaf.heathcare.service.goodsActualQuantityService");
			ActualRepastPersonService actualRepastPersonService = ContextFactory
					.getBean("com.glaf.heathcare.service.actualRepastPersonService");
			TenantConfigService tenantConfigService = ContextFactory.getBean("tenantConfigService");
			TenantConfig tenantConfig = tenantConfigService.getTenantConfigByTenantId(loginContext.getTenantId());
			long typeId = tenantConfig.getTypeId();

			params.put("typeName", "两餐两点");
			List<Dictory> dictoryList = dictoryService.getDictoryList(4501L);// 4501是餐点分类编号
			if (dictoryList != null && !dictoryList.isEmpty()) {
				for (Dictory d : dictoryList) {
					if (typeId == d.getId()) {
						params.put("typeName", d.getName());
					}
				}
			}

			ActualRepastPersonQuery q2 = new ActualRepastPersonQuery();
			q2.tenantId(tenantId);
			q2.fullDay(DateUtils.getYearMonthDay(date));

			List<ActualRepastPerson> persons = actualRepastPersonService.list(q2);
			int totalPerson = 0;
			if (persons != null && !persons.isEmpty()) {
				for (ActualRepastPerson p : persons) {
					totalPerson = totalPerson + p.getMale();
					totalPerson = totalPerson + p.getFemale();
				}
			}

			GoodsActualQuantityQuery query = new GoodsActualQuantityQuery();
			query.tenantId(tenantId);
			query.businessStatus(9);
			query.fullDay(DateUtils.getYearMonthDay(date));
			List<GoodsActualQuantity> rows = goodsActualQuantityService.list(query);
			if (rows != null && !rows.isEmpty()) {
				List<GoodsActualQuantity> rows1 = new ArrayList<GoodsActualQuantity>();// 谷类
				List<GoodsActualQuantity> rows2 = new ArrayList<GoodsActualQuantity>();// 蔬菜水果
				List<GoodsActualQuantity> rows3 = new ArrayList<GoodsActualQuantity>();// 动物性
				List<GoodsActualQuantity> rows4 = new ArrayList<GoodsActualQuantity>();// 乳类、大豆
				List<GoodsActualQuantity> rows5 = new ArrayList<GoodsActualQuantity>();// 油脂、调味品
				for (GoodsActualQuantity m : rows) {
					switch ((int) m.getGoodsNodeId()) {
					case 4402:
					case 4403:
						rows1.add(m);
						break;
					case 4405:
					case 4406:
					case 4407:
						rows2.add(m);
						break;
					case 4409:
					case 4410:
					case 4412:
					case 4413:
						rows3.add(m);
						break;
					case 4404:
					case 4408:
					case 4411:
						rows4.add(m);
						break;
					default:
						rows5.add(m);
						break;
					}
				}

				GoodsActualQuantity sum1 = new GoodsActualQuantity();
				GoodsActualQuantity sum2 = new GoodsActualQuantity();
				GoodsActualQuantity sum3 = new GoodsActualQuantity();
				GoodsActualQuantity sum4 = new GoodsActualQuantity();
				GoodsActualQuantity sum5 = new GoodsActualQuantity();

				for (int i = 0, len = rows1.size(); i < len; i++) {
					GoodsActualQuantity m = rows1.get(i);
					sum1.setTotalQuantity(sum1.getTotalQuantity() + m.getQuantity());
					sum1.setTotalPrice(sum1.getTotalPrice() + m.getTotalPrice());
					if (totalPerson > 0) {
						sum1.setAvgQuantity(sum1.getAvgQuantity() + m.getQuantity() * 1000D / totalPerson);
					}
				}

				for (int i = 0, len = rows2.size(); i < len; i++) {
					GoodsActualQuantity m = rows2.get(i);
					sum2.setTotalQuantity(sum2.getTotalQuantity() + m.getQuantity());
					sum2.setTotalPrice(sum2.getTotalPrice() + m.getTotalPrice());
					if (totalPerson > 0) {
						sum2.setAvgQuantity(sum2.getAvgQuantity() + m.getQuantity() * 1000D / totalPerson);
					}
				}

				for (int i = 0, len = rows3.size(); i < len; i++) {
					GoodsActualQuantity m = rows3.get(i);
					sum3.setTotalQuantity(sum3.getTotalQuantity() + m.getQuantity());
					sum3.setTotalPrice(sum3.getTotalPrice() + m.getTotalPrice());
					if (totalPerson > 0) {
						sum3.setAvgQuantity(sum3.getAvgQuantity() + m.getQuantity() * 1000D / totalPerson);
					}
				}

				for (int i = 0, len = rows4.size(); i < len; i++) {
					GoodsActualQuantity m = rows4.get(i);
					sum4.setTotalQuantity(sum4.getTotalQuantity() + m.getQuantity());
					sum4.setTotalPrice(sum4.getTotalPrice() + m.getTotalPrice());
					if (totalPerson > 0) {
						sum4.setAvgQuantity(sum4.getAvgQuantity() + m.getQuantity() * 1000D / totalPerson);
					}
				}

				for (int i = 0, len = rows5.size(); i < len; i++) {
					GoodsActualQuantity m = rows5.get(i);
					sum5.setTotalQuantity(sum5.getTotalQuantity() + m.getQuantity());
					sum5.setTotalPrice(sum5.getTotalPrice() + m.getTotalPrice());
					if (totalPerson > 0) {
						sum5.setAvgQuantity(sum5.getAvgQuantity() + m.getQuantity() * 1000D / totalPerson);
					}
				}

				int max = Math.max(rows1.size(), rows2.size());
				max = Math.max(max, rows3.size());
				max = Math.max(max, rows4.size());
				max = Math.max(max, rows5.size());

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
				for (int i = 0, len = rows4.size(); i < max - len; i++) {
					GoodsActualQuantity m = new GoodsActualQuantity();
					rows4.add(m);
				}
				for (int i = 0, len = rows5.size(); i < max - len; i++) {
					GoodsActualQuantity m = new GoodsActualQuantity();
					rows5.add(m);
				}

				logger.debug("rows1 size:" + rows1.size());
				params.put("items1", rows1);
				params.put("rows1", rows1);
				params.put("items2", rows2);
				params.put("rows2", rows2);
				params.put("items3", rows3);
				params.put("rows3", rows3);
				params.put("items4", rows4);
				params.put("rows4", rows4);
				params.put("items5", rows5);
				params.put("rows5", rows5);

				params.put("sum1", sum1);
				params.put("sum2", sum2);
				params.put("sum3", sum3);
				params.put("sum4", sum4);
				params.put("sum5", sum5);

			}
		}
	}

}
