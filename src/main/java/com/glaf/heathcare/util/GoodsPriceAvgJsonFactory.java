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
public class GoodsPriceAvgJsonFactory {

	public static java.util.List<GoodsPriceAvg> arrayToList(JSONArray array) {
		java.util.List<GoodsPriceAvg> list = new java.util.ArrayList<GoodsPriceAvg>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			GoodsPriceAvg model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	public static GoodsPriceAvg jsonToObject(JSONObject jsonObject) {
		GoodsPriceAvg model = new GoodsPriceAvg();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getLong("id"));
		}
		if (jsonObject.containsKey("province")) {
			model.setProvince(jsonObject.getString("province"));
		}
		if (jsonObject.containsKey("provinceId")) {
			model.setProvinceId(jsonObject.getLong("provinceId"));
		}
		if (jsonObject.containsKey("city")) {
			model.setCity(jsonObject.getString("city"));
		}
		if (jsonObject.containsKey("cityId")) {
			model.setCityId(jsonObject.getLong("cityId"));
		}
		if (jsonObject.containsKey("area")) {
			model.setArea(jsonObject.getString("area"));
		}
		if (jsonObject.containsKey("areaId")) {
			model.setAreaId(jsonObject.getLong("areaId"));
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
		if (jsonObject.containsKey("price")) {
			model.setPrice(jsonObject.getDouble("price"));
		}
		if (jsonObject.containsKey("year")) {
			model.setYear(jsonObject.getInteger("year"));
		}
		if (jsonObject.containsKey("month")) {
			model.setMonth(jsonObject.getInteger("month"));
		}
		if (jsonObject.containsKey("fullDay")) {
			model.setFullDay(jsonObject.getInteger("fullDay"));
		}
		if (jsonObject.containsKey("createTime")) {
			model.setCreateTime(jsonObject.getDate("createTime"));
		}

		return model;
	}

	public static JSONArray listToArray(java.util.List<GoodsPriceAvg> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (GoodsPriceAvg model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static JSONObject toJsonObject(GoodsPriceAvg model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getProvince() != null) {
			jsonObject.put("province", model.getProvince());
		}
		jsonObject.put("provinceId", model.getProvinceId());
		if (model.getCity() != null) {
			jsonObject.put("city", model.getCity());
		}
		jsonObject.put("cityId", model.getCityId());
		if (model.getArea() != null) {
			jsonObject.put("area", model.getArea());
		}
		jsonObject.put("areaId", model.getAreaId());
		jsonObject.put("goodsId", model.getGoodsId());
		if (model.getGoodsName() != null) {
			jsonObject.put("goodsName", model.getGoodsName());
		}
		jsonObject.put("goodsNodeId", model.getGoodsNodeId());
		jsonObject.put("price", model.getPrice());
		jsonObject.put("year", model.getYear());
		jsonObject.put("month", model.getMonth());
		jsonObject.put("fullDay", model.getFullDay());
		if (model.getCreateTime() != null) {
			jsonObject.put("createTime", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_date", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_datetime", DateUtils.getDateTime(model.getCreateTime()));
		}
		return jsonObject;
	}

	public static ObjectNode toObjectNode(GoodsPriceAvg model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getProvince() != null) {
			jsonObject.put("province", model.getProvince());
		}
		jsonObject.put("provinceId", model.getProvinceId());
		if (model.getCity() != null) {
			jsonObject.put("city", model.getCity());
		}
		jsonObject.put("cityId", model.getCityId());
		if (model.getArea() != null) {
			jsonObject.put("area", model.getArea());
		}
		jsonObject.put("areaId", model.getAreaId());
		jsonObject.put("goodsId", model.getGoodsId());
		if (model.getGoodsName() != null) {
			jsonObject.put("goodsName", model.getGoodsName());
		}
		jsonObject.put("goodsNodeId", model.getGoodsNodeId());
		jsonObject.put("price", model.getPrice());
		jsonObject.put("year", model.getYear());
		jsonObject.put("month", model.getMonth());
		jsonObject.put("fullDay", model.getFullDay());
		if (model.getCreateTime() != null) {
			jsonObject.put("createTime", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_date", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_datetime", DateUtils.getDateTime(model.getCreateTime()));
		}
		return jsonObject;
	}

	private GoodsPriceAvgJsonFactory() {

	}

}
