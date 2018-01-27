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

package com.glaf.matrix.data.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.jdbc.QueryHelper;
import com.glaf.core.util.ParamUtils;
import com.glaf.matrix.data.domain.DataItemDefinition;
import com.glaf.matrix.data.service.DataItemDefinitionService;

public class DataItemFactory {

	protected static DataItemDefinitionService dataItemDefinitionService;

	public static DataItemDefinitionService getDataItemDefinitionService() {
		if (dataItemDefinitionService == null) {
			dataItemDefinitionService = ContextFactory.getBean("dataItemDefinitionService");
		}
		return dataItemDefinitionService;
	}

	public static JSONArray getJSONArray(String code) {
		JSONArray array = new JSONArray();
		DataItemDefinition bean = getDataItemDefinitionService().getDataItemDefinitionByCode(code);
		if (bean != null) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			QueryHelper helper = new QueryHelper();
			List<Map<String, Object>> dataList = helper.getResultList(bean.getSql(), paramMap);
			if (dataList != null && !dataList.isEmpty()) {
				for (Map<String, Object> dataMap : dataList) {
					JSONObject json = new JSONObject();
					json.put("key", ParamUtils.getString(dataMap, bean.getKeyColumn()));
					json.put("value", ParamUtils.getString(dataMap, bean.getValueColumn()));
					array.add(json);
				}
			}
		}
		return array;
	}

	private DataItemFactory() {

	}
}
