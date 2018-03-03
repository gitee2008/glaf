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

import com.glaf.core.context.ContextFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.ParamUtils;
import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.domain.GoodsActualQuantity;
import com.glaf.heathcare.domain.RptModel;
import com.glaf.heathcare.domain.WeeklyDataModel;
import com.glaf.heathcare.query.FoodCompositionQuery;
import com.glaf.heathcare.query.GoodsActualQuantityQuery;
import com.glaf.heathcare.service.FoodCompositionService;
import com.glaf.heathcare.service.GoodsActualQuantityService;

public class WeeklyFoodActualQuantityCountPreprocessor implements IReportPreprocessor {
	protected static final Log logger = LogFactory.getLog(WeeklyFoodActualQuantityCountPreprocessor.class);

	@Override
	public void prepare(LoginContext loginContext, int year, int month, Map<String, Object> params) {
		Date startDate = ParamUtils.getDate(params, "startDate");
		Date endDate = ParamUtils.getDate(params, "endDate");
		if (startDate != null && endDate != null) {
			String tenantId = loginContext.getTenantId();
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

			Calendar calendar = Calendar.getInstance();

			GoodsActualQuantityQuery query = new GoodsActualQuantityQuery();
			query.tenantId(tenantId);
			query.businessStatus(9);
			query.usageTimeGreaterThanOrEqual(startDate);
			query.usageTimeLessThanOrEqual(endDate);

			String key2 = null;
			List<GoodsActualQuantity> glist = null;

			Map<String, WeeklyDataModel> weeklyMap = new LinkedHashMap<String, WeeklyDataModel>();
			List<GoodsActualQuantity> rows = goodsActualQuantityService.list(query);
			if (rows != null && !rows.isEmpty()) {
				params.put("week", rows.get(0).getWeek());
				Map<String, GoodsActualQuantity> goodsMap = new HashMap<String, GoodsActualQuantity>();
				Map<String, List<GoodsActualQuantity>> goodsListMap = new HashMap<String, List<GoodsActualQuantity>>();
				for (GoodsActualQuantity m : rows) {
					calendar.setTime(m.getUsageTime());
					int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
					switch (dayOfWeek) {
					case Calendar.MONDAY:
						key2 = "day_1";
						break;
					case Calendar.TUESDAY:
						key2 = "day_2";
						break;
					case Calendar.WEDNESDAY:
						key2 = "day_3";
						break;
					case Calendar.THURSDAY:
						key2 = "day_4";
						break;
					case Calendar.FRIDAY:
						key2 = "day_5";
						break;
					}

					FoodComposition fd = foodMap.get(m.getGoodsId());
					if (fd != null) {
						GoodsActualQuantity aa = goodsMap.get(key2 + "_" + m.getGoodsId());
						if (aa == null) {
							aa = new GoodsActualQuantity();
							aa.setGoodsId(m.getGoodsId());
							aa.setGoodsName(fd.getName());
						}
						aa.setQuantity(aa.getQuantity() + m.getQuantity());

						switch ((int) fd.getNodeId()) {
						case 4402:// 谷类
						case 4403:// 薯类
							glist = goodsListMap.get(key2 + "_items1");
							if (glist == null) {
								glist = new ArrayList<GoodsActualQuantity>();
								goodsListMap.put(key2 + "_items1", glist);
							}
							glist.add(aa);
							break;
						case 4405:// 蔬菜
							glist = goodsListMap.get(key2 + "_items2");
							if (glist == null) {
								glist = new ArrayList<GoodsActualQuantity>();
								goodsListMap.put(key2 + "_items2", glist);
							}
							glist.add(aa);
							break;
						case 4407:// 水果
							glist = goodsListMap.get(key2 + "_items3");
							if (glist == null) {
								glist = new ArrayList<GoodsActualQuantity>();
								goodsListMap.put(key2 + "_items3", glist);
							}
							glist.add(aa);
							break;
						case 4409:// 畜肉
						case 4410:// 禽肉
						case 4413:// 鱼虾蟹贝类
							glist = goodsListMap.get(key2 + "_items4");
							if (glist == null) {
								glist = new ArrayList<GoodsActualQuantity>();
								goodsListMap.put(key2 + "_items4", glist);
							}
							glist.add(aa);
							break;
						case 4404:// 豆类
						case 4411:// 乳类
						case 4412:// 蛋类
							glist = goodsListMap.get(key2 + "_items5");
							if (glist == null) {
								glist = new ArrayList<GoodsActualQuantity>();
								goodsListMap.put(key2 + "_items5", glist);
							}
							glist.add(aa);
							break;
						default:
							glist = goodsListMap.get(key2 + "_items6");
							if (glist == null) {
								glist = new ArrayList<GoodsActualQuantity>();
								goodsListMap.put(key2 + "_items6", glist);
							}
							glist.add(aa);
							break;
						}
					}
				}

				RptModel rptModel = null;
				String key = null;
				int index = 0;
				int size = 0;

				for (int i = 1; i <= 5; i++) {
					key = "day_" + i;
					WeeklyDataModel dmodel = new WeeklyDataModel();
					dmodel.setDateString(key);
					if (i == 1) {
						dmodel.setName("星期一");
					} else if (i == 2) {
						dmodel.setName("星期二");
					} else if (i == 3) {
						dmodel.setName("星期三");
					} else if (i == 4) {
						dmodel.setName("星期四");
					} else if (i == 5) {
						dmodel.setName("星期五");
					}

					weeklyMap.put(dmodel.getDateString(), dmodel);
					for (int j = 1; j <= 20; j++) {
						rptModel = new RptModel();
						dmodel.getItems().add(rptModel);
					}

					glist = goodsListMap.get(key + "_items1");
					if (glist != null && !glist.isEmpty()) {
						index = 0;
						size = glist.size();
						for (GoodsActualQuantity g : glist) {
							if (index < size) {
								rptModel = dmodel.getItems().get(index++);
								if (rptModel != null) {
									rptModel.setName1(g.getGoodsName());
									rptModel.setQuantity1(g.getQuantity());
								}
							}
						}
					}

					glist = goodsListMap.get(key + "_items2");
					if (glist != null && !glist.isEmpty()) {
						index = 0;
						size = glist.size();
						for (GoodsActualQuantity g : glist) {
							if (index < size) {
								rptModel = dmodel.getItems().get(index++);
								if (rptModel != null) {
									rptModel.setName2(g.getGoodsName());
									rptModel.setQuantity2(g.getQuantity());
								}
							}
						}
					}

					glist = goodsListMap.get(key + "_items3");
					if (glist != null && !glist.isEmpty()) {
						index = 0;
						size = glist.size();
						for (GoodsActualQuantity g : glist) {
							if (index < size) {
								rptModel = dmodel.getItems().get(index++);
								if (rptModel != null) {
									rptModel.setName3(g.getGoodsName());
									rptModel.setQuantity3(g.getQuantity());
								}
							}
						}
					}

					glist = goodsListMap.get(key + "_items4");
					if (glist != null && !glist.isEmpty()) {
						index = 0;
						size = glist.size();
						for (GoodsActualQuantity g : glist) {
							if (index < size) {
								rptModel = dmodel.getItems().get(index++);
								if (rptModel != null) {
									rptModel.setName4(g.getGoodsName());
									rptModel.setQuantity4(g.getQuantity());
								}
							}
						}
					}

					glist = goodsListMap.get(key + "_items5");
					if (glist != null && !glist.isEmpty()) {
						index = 0;
						size = glist.size();
						for (GoodsActualQuantity g : glist) {
							if (index < size) {
								rptModel = dmodel.getItems().get(index++);
								if (rptModel != null) {
									rptModel.setName5(g.getGoodsName());
									rptModel.setQuantity5(g.getQuantity());
								}
							}
						}
					}

					glist = goodsListMap.get(key + "_items6");
					if (glist != null && !glist.isEmpty()) {
						index = 0;
						size = glist.size();
						for (GoodsActualQuantity g : glist) {
							if (index < size) {
								rptModel = dmodel.getItems().get(index++);
								if (rptModel != null) {
									rptModel.setName6(g.getGoodsName());
									rptModel.setQuantity6(g.getQuantity());
								}
							}
						}
					}

				}
			}

			Collection<WeeklyDataModel> wkdatalist = weeklyMap.values();
			for (WeeklyDataModel wdm : wkdatalist) {
				List<RptModel> items_new = new ArrayList<RptModel>();
				RptModel rm = new RptModel();
				items_new.add(rm);
				List<RptModel> items = wdm.getItems();
				for (RptModel m : items) {
					if (m.existsQuantity()) {
						items_new.add(m);
					}
				}
				wdm.setItems(items_new);
			}

			// params.putAll(dataMap);
			params.put("wkdatalist", weeklyMap.values());
			params.put("startDate", DateUtils.getDateTime("yyyy年MM月dd日", startDate));
			params.put("endDate", DateUtils.getDateTime("yyyy年MM月dd日", endDate));
			logger.debug("params:" + params);
		}
	}

}
