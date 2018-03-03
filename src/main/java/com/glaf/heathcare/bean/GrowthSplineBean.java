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
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.core.context.ContextFactory;
import com.glaf.heathcare.domain.GrowthStandard;
import com.glaf.heathcare.query.GrowthStandardQuery;
import com.glaf.heathcare.service.GrowthStandardService;

public class GrowthSplineBean {

	protected static final Log logger = LogFactory.getLog(GrowthSplineBean.class);

	protected static final int AGE_OF_MOON = 72;

	protected GrowthStandardService growthStandardService;

	public GrowthSplineBean() {

	}

	public JSONArray buildJsonArrayCategories(String type, String sex, String standardType) {
		JSONArray result = new JSONArray();
		GrowthStandardQuery query = new GrowthStandardQuery();
		query.type(type);
		query.sex(sex);
		query.standardType(standardType);
		query.ageOfTheMoonGreaterThanOrEqual(24);
		query.ageOfTheMoonLessThanOrEqual(AGE_OF_MOON);
		List<GrowthStandard> list = getGrowthStandardService().list(query);
		if (list != null && !list.isEmpty()) {
			Collection<Integer> categories = new HashSet<Integer>();
			for (GrowthStandard m : list) {
				if (!categories.contains(m.getAgeOfTheMoon())) {
					if (m.getAgeOfTheMoon() % 12 == 0) {
						result.add(String.valueOf(m.getAgeOfTheMoon() / 12) + "岁");
					} else {
						result.add(String.valueOf(m.getAgeOfTheMoon() % 12));
					}
					categories.add(m.getAgeOfTheMoon());
				}
			}
		}

		return result;
	}

	public JSONArray buildJsonArrayData(String type, String sex, String standardType,
			Map<String, List<Double>> myData) {
		JSONArray result = new JSONArray();
		GrowthStandardQuery query = new GrowthStandardQuery();
		query.type(type);
		query.sex(sex);
		query.standardType(standardType);
		query.ageOfTheMoonGreaterThanOrEqual(24);
		query.ageOfTheMoonLessThanOrEqual(AGE_OF_MOON);
		List<GrowthStandard> list = getGrowthStandardService().list(query);
		if (list != null && !list.isEmpty()) {
			Map<String, List<Double>> dataListMap = new LinkedHashMap<String, List<Double>>();
			for (GrowthStandard m : list) {
				Map<String, Double> jsonObject = toDeviationMap(m);
				Set<Entry<String, Double>> entrySet = jsonObject.entrySet();
				for (Entry<String, Double> entry : entrySet) {
					String key = entry.getKey();
					Double val = entry.getValue();
					List<Double> list2 = dataListMap.get(key);
					if (list2 == null) {
						list2 = new ArrayList<Double>();
						dataListMap.put(key, list2);
					}
					list2.add(val);
				}
			}

			if (dataListMap.size() > 0) {
				Set<Entry<String, List<Double>>> entrySet = dataListMap.entrySet();
				for (Entry<String, List<Double>> entry : entrySet) {
					String key = entry.getKey();
					List<Double> list2 = entry.getValue();
					JSONObject jsonObject = new JSONObject();
					if (list2 != null && !list2.isEmpty()) {
						JSONArray array = new JSONArray();
						for (Double val : list2) {
							array.add(val);
						}
						jsonObject.put("name", key);
						jsonObject.put("data", array);
						result.add(jsonObject);
					}
				}
			}
		}

		if (myData != null && !myData.isEmpty()) {
			Set<Entry<String, List<Double>>> entrySet = myData.entrySet();
			for (Entry<String, List<Double>> entry : entrySet) {
				String key = entry.getKey();
				List<Double> list2 = entry.getValue();
				JSONObject jsonObject = new JSONObject();
				if (list2 != null && !list2.isEmpty()) {
					JSONArray array = new JSONArray();
					for (Double val : list2) {
						array.add(val);
					}
					jsonObject.put("name", key);
					jsonObject.put("data", array);
					result.add(jsonObject);
				}
			}
		}

		return result;
	}

	public List<Integer> getCategories(String type, String sex, String standardType) {
		List<Integer> result = new ArrayList<Integer>();
		GrowthStandardQuery query = new GrowthStandardQuery();
		query.type(type);
		query.sex(sex);
		query.standardType(standardType);
		query.ageOfTheMoonGreaterThanOrEqual(24);
		query.ageOfTheMoonLessThanOrEqual(AGE_OF_MOON);
		List<GrowthStandard> list = getGrowthStandardService().list(query);
		if (list != null && !list.isEmpty()) {
			Collection<Integer> categories = new HashSet<Integer>();
			for (GrowthStandard m : list) {
				if (!categories.contains(m.getAgeOfTheMoon())) {
					result.add(m.getAgeOfTheMoon());
					categories.add(m.getAgeOfTheMoon());
				}
			}
		}

		return result;
	}

	public GrowthStandardService getGrowthStandardService() {
		if (growthStandardService == null) {
			growthStandardService = ContextFactory.getBean("com.glaf.heathcare.service.growthStandardService");
		}
		return growthStandardService;
	}

	public void setGrowthStandardService(GrowthStandardService growthStandardService) {
		this.growthStandardService = growthStandardService;
	}

	public Map<String, Double> toDeviationMap(GrowthStandard model) {
		Map<String, Double> jsonObject = new LinkedHashMap<String, Double>();
		// jsonObject.put("4", model.getFourDSDeviation());
		jsonObject.put("3", model.getThreeDSDeviation());
		jsonObject.put("2", model.getTwoDSDeviation());
		// jsonObject.put("1", model.getOneDSDeviation());
		jsonObject.put("0", model.getMedian());
		// jsonObject.put("-1", model.getNegativeOneDSDeviation());
		jsonObject.put("-2", model.getNegativeTwoDSDeviation());
		jsonObject.put("-3", model.getNegativeThreeDSDeviation());
		// jsonObject.put("-4", model.getNegativeFourDSDeviation());
		return jsonObject;
	}

	public Map<String, Double> toPercentMap(GrowthStandard model) {
		Map<String, Double> jsonObject = new LinkedHashMap<String, Double>();
		jsonObject.put("3", model.getPercent3());
		jsonObject.put("15", model.getPercent15());
		jsonObject.put("50", model.getPercent50());
		jsonObject.put("85", model.getPercent85());
		jsonObject.put("97", model.getPercent97());
		return jsonObject;
	}

}
