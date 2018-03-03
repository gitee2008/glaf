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

package com.glaf.heathcare.util;

import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.glaf.core.util.DateUtils;
import com.glaf.heathcare.domain.*;

/**
 * 
 * JSON工厂类
 *
 */
public class GoodsStockJsonFactory {

	public static GoodsStock jsonToObject(JSONObject jsonObject) {
		GoodsStock model = new GoodsStock();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getString("id"));
		}
		if (jsonObject.containsKey("tenantId")) {
			model.setTenantId(jsonObject.getString("tenantId"));
		}
		if (jsonObject.containsKey("goodsId")) {
			model.setGoodsId(jsonObject.getLong("goodsId"));
		}
		if (jsonObject.containsKey("goodsName")) {
			model.setGoodsName(jsonObject.getString("goodsName"));
		}
		if (jsonObject.containsKey("goodsNodeId")) {
			model.setGoodsNodeId(jsonObject.getLong("goodsNodeId"));
		}
		if (jsonObject.containsKey("quantity")) {
			model.setQuantity(jsonObject.getDouble("quantity"));
		}
		if (jsonObject.containsKey("unit")) {
			model.setUnit(jsonObject.getString("unit"));
		}
		if (jsonObject.containsKey("expiryDate")) {
			model.setExpiryDate(jsonObject.getDate("expiryDate"));
		}
		if (jsonObject.containsKey("latestOutStockTime")) {
			model.setLatestOutStockTime(jsonObject.getDate("latestOutStockTime"));
		}

		return model;
	}

	public static JSONObject toJsonObject(GoodsStock model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		jsonObject.put("goodsId", model.getGoodsId());
		if (model.getGoodsName() != null) {
			jsonObject.put("goodsName", model.getGoodsName());
		}
		jsonObject.put("goodsNodeId", model.getGoodsNodeId());
		jsonObject.put("quantity", model.getQuantity());
		if (model.getUnit() != null) {
			jsonObject.put("unit", model.getUnit());
		}
		if (model.getExpiryDate() != null) {
			jsonObject.put("expiryDate", DateUtils.getDate(model.getExpiryDate()));
			jsonObject.put("expiryDate_date", DateUtils.getDate(model.getExpiryDate()));
			jsonObject.put("expiryDate_datetime", DateUtils.getDateTime(model.getExpiryDate()));
		}
		if (model.getLatestOutStockTime() != null) {
			jsonObject.put("latestOutStockTime", DateUtils.getDate(model.getLatestOutStockTime()));
			jsonObject.put("latestOutStockTime_date", DateUtils.getDate(model.getLatestOutStockTime()));
			jsonObject.put("latestOutStockTime_datetime", DateUtils.getDateTime(model.getLatestOutStockTime()));
		}
		return jsonObject;
	}

	public static ObjectNode toObjectNode(GoodsStock model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getTenantId() != null) {
			jsonObject.put("tenantId", model.getTenantId());
		}
		jsonObject.put("goodsId", model.getGoodsId());
		if (model.getGoodsName() != null) {
			jsonObject.put("goodsName", model.getGoodsName());
		}
		jsonObject.put("goodsNodeId", model.getGoodsNodeId());
		jsonObject.put("quantity", model.getQuantity());
		if (model.getUnit() != null) {
			jsonObject.put("unit", model.getUnit());
		}
		if (model.getExpiryDate() != null) {
			jsonObject.put("expiryDate", DateUtils.getDate(model.getExpiryDate()));
			jsonObject.put("expiryDate_date", DateUtils.getDate(model.getExpiryDate()));
			jsonObject.put("expiryDate_datetime", DateUtils.getDateTime(model.getExpiryDate()));
		}
		if (model.getLatestOutStockTime() != null) {
			jsonObject.put("latestOutStockTime", DateUtils.getDate(model.getLatestOutStockTime()));
			jsonObject.put("latestOutStockTime_date", DateUtils.getDate(model.getLatestOutStockTime()));
			jsonObject.put("latestOutStockTime_datetime", DateUtils.getDateTime(model.getLatestOutStockTime()));
		}
		return jsonObject;
	}

	public static JSONArray listToArray(java.util.List<GoodsStock> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (GoodsStock model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static java.util.List<GoodsStock> arrayToList(JSONArray array) {
		java.util.List<GoodsStock> list = new java.util.ArrayList<GoodsStock>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			GoodsStock model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	private GoodsStockJsonFactory() {

	}

}
