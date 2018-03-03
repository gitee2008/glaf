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
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.service.ITablePageService;
import com.glaf.core.util.ParamUtils;

import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.domain.GoodsInStock;
import com.glaf.heathcare.query.FoodCompositionQuery;
import com.glaf.heathcare.service.FoodCompositionService;

public class MonthGoodsInStockPreprocessor implements IReportPreprocessor {

	@Override
	public void prepare(LoginContext loginContext, int year, int month, Map<String, Object> params) {
		params.put("year", year);
		params.put("month", month);
		String tenantId = loginContext.getTenantId();

		StringBuilder sqlBuffer = new StringBuilder();
		sqlBuffer.append(
				" select E.GOODSID_ as goodsid, sum(QUANTITY_) as quantity, sum(TOTALPRICE_) as totalprice from GOODS_IN_STOCK")
				.append(String.valueOf(IdentityFactory.getTenantHash(tenantId))).append(" E ")
				.append(" where ( E.TENANTID_ = '").append(tenantId).append("' ) ").append(" and ( E.YEAR_ =")
				.append(year).append(" ) and ( E.MONTH_ = ").append(month).append(" ) and ( E.BUSINESSSTATUS_ = 9 ) ")
				.append(" group by E.GOODSID_");
		ITablePageService tablePageService = ContextFactory.getBean("tablePageService");
		FoodCompositionService foodCompositionService = ContextFactory
				.getBean("com.glaf.heathcare.service.foodCompositionService");
		FoodCompositionQuery query = new FoodCompositionQuery();
		query.locked(0);
		List<FoodComposition> foods = foodCompositionService.list(query);
		Map<Long, FoodComposition> foodMap = new HashMap<Long, FoodComposition>();
		for (FoodComposition food : foods) {
			foodMap.put(food.getId(), food);
		}
		List<Map<String, Object>> list = tablePageService.getListData(sqlBuffer.toString(), params);
		List<GoodsInStock> rows = new ArrayList<GoodsInStock>();
		if (list != null && !list.isEmpty()) {
			FoodComposition food = null;
			for (Map<String, Object> dataMap : list) {
				GoodsInStock m = new GoodsInStock();
				m.setGoodsId(ParamUtils.getLong(dataMap, "goodsid"));
				m.setQuantity(ParamUtils.getDouble(dataMap, "quantity"));
				m.setTotalPrice(ParamUtils.getDouble(dataMap, "totalprice"));
				if (foodMap.get(m.getGoodsId()) != null) {
					food = foodMap.get(m.getGoodsId());
					m.setGoodsName(food.getName());
					m.setGoodsNodeId(food.getNodeId());
					rows.add(m);
				}
			}
		}

		if (!rows.isEmpty()) {
			double total1 = 0;
			double total2 = 0;
			double total3 = 0;
			double total = 0;
			List<GoodsInStock> rows1 = new ArrayList<GoodsInStock>();
			List<GoodsInStock> rows2 = new ArrayList<GoodsInStock>();
			List<GoodsInStock> rows3 = new ArrayList<GoodsInStock>();
			for (GoodsInStock m : rows) {
				switch ((int) m.getGoodsNodeId()) {
				case 4405:
				case 4406:
				case 4407:
					rows1.add(m);
					total1 = total1 + m.getTotalPrice();
					break;
				case 4409:
				case 4410:
				case 4411:
				case 4412:
				case 4413:
					rows2.add(m);
					total2 = total2 + m.getTotalPrice();
					break;
				default:
					rows3.add(m);
					total3 = total3 + m.getTotalPrice();
					break;
				}
			}

			int max = Math.max(rows1.size(), rows2.size());
			max = Math.max(max, rows3.size());

			for (int i = 0, len = rows1.size(); i < max - len; i++) {
				GoodsInStock m = new GoodsInStock();
				rows1.add(m);
			}
			for (int i = 0, len = rows2.size(); i < max - len; i++) {
				GoodsInStock m = new GoodsInStock();
				rows2.add(m);
			}
			for (int i = 0, len = rows3.size(); i < max - len; i++) {
				GoodsInStock m = new GoodsInStock();
				rows3.add(m);
			}

			total = total1 + total2 + total3;
			params.put("total1", total1);
			params.put("total2", total2);
			params.put("total3", total3);

			if (total > 0) {
				params.put("total1Percent", Math.round(total1 / total * 10000D) / 100D);
				params.put("total2Percent", Math.round(total2 / total * 10000D) / 100D);
				params.put("total3Percent", Math.round(total3 / total * 10000D) / 100D);
			}

			params.put("total", total);

			params.put("items1", rows1);
			params.put("rows1", rows1);
			params.put("items2", rows2);
			params.put("rows2", rows2);
			params.put("items3", rows3);
			params.put("rows3", rows3);
		}

	}

}
