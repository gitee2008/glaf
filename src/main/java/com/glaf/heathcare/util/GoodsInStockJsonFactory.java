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
public class GoodsInStockJsonFactory {

	public static GoodsInStock jsonToObject(JSONObject jsonObject) {
		GoodsInStock model = new GoodsInStock();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getLong("id"));
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
		if (jsonObject.containsKey("inStockTime")) {
			model.setInStockTime(jsonObject.getDate("inStockTime"));
		}
		if (jsonObject.containsKey("semester")) {
			model.setSemester(jsonObject.getInteger("semester"));
		}
		if (jsonObject.containsKey("year")) {
			model.setYear(jsonObject.getInteger("year"));
		}
		if (jsonObject.containsKey("month")) {
			model.setMonth(jsonObject.getInteger("month"));
		}
		if (jsonObject.containsKey("day")) {
			model.setDay(jsonObject.getInteger("day"));
		}
		if (jsonObject.containsKey("week")) {
			model.setWeek(jsonObject.getInteger("week"));
		}
		if (jsonObject.containsKey("fullDay")) {
			model.setFullDay(jsonObject.getInteger("fullDay"));
		}
		if (jsonObject.containsKey("batchNo")) {
			model.setBatchNo(jsonObject.getString("batchNo"));
		}
		if (jsonObject.containsKey("supplier")) {
			model.setSupplier(jsonObject.getString("supplier"));
		}
		if (jsonObject.containsKey("contact")) {
			model.setContact(jsonObject.getString("contact"));
		}
		if (jsonObject.containsKey("standard")) {
			model.setStandard(jsonObject.getString("standard"));
		}
		if (jsonObject.containsKey("address")) {
			model.setAddress(jsonObject.getString("address"));
		}
		if (jsonObject.containsKey("quantity")) {
			model.setQuantity(jsonObject.getDouble("quantity"));
		}
		if (jsonObject.containsKey("unit")) {
			model.setUnit(jsonObject.getString("unit"));
		}
		if (jsonObject.containsKey("price")) {
			model.setPrice(jsonObject.getDouble("price"));
		}
		if (jsonObject.containsKey("totalPrice")) {
			model.setTotalPrice(jsonObject.getDouble("totalPrice"));
		}
		if (jsonObject.containsKey("expiryDate")) {
			model.setExpiryDate(jsonObject.getDate("expiryDate"));
		}
		if (jsonObject.containsKey("available")) {
			model.setAvailable(jsonObject.getInteger("available"));
		}
		if (jsonObject.containsKey("receiverId")) {
			model.setReceiverId(jsonObject.getString("receiverId"));
		}
		if (jsonObject.containsKey("receiverName")) {
			model.setReceiverName(jsonObject.getString("receiverName"));
		}
		if (jsonObject.containsKey("remark")) {
			model.setRemark(jsonObject.getString("remark"));
		}
		if (jsonObject.containsKey("businessStatus")) {
			model.setBusinessStatus(jsonObject.getInteger("businessStatus"));
		}
		if (jsonObject.containsKey("confirmTime")) {
			model.setConfirmTime(jsonObject.getDate("confirmTime"));
		}
		if (jsonObject.containsKey("confirmBy")) {
			model.setConfirmBy(jsonObject.getString("confirmBy"));
		}
		if (jsonObject.containsKey("createBy")) {
			model.setCreateBy(jsonObject.getString("createBy"));
		}
		if (jsonObject.containsKey("createTime")) {
			model.setCreateTime(jsonObject.getDate("createTime"));
		}

		return model;
	}

	public static JSONObject toJsonObject(GoodsInStock model) {
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

		if (model.getInStockTime() != null) {
			jsonObject.put("inStockTime", DateUtils.getDate(model.getInStockTime()));
			jsonObject.put("inStockTime_date", DateUtils.getDate(model.getInStockTime()));
			jsonObject.put("inStockTime_datetime", DateUtils.getDateTime(model.getInStockTime()));
		}
		jsonObject.put("semester", model.getSemester());
		jsonObject.put("year", model.getYear());
		jsonObject.put("month", model.getMonth());
		jsonObject.put("day", model.getDay());
		jsonObject.put("week", model.getWeek());
		jsonObject.put("fullDay", model.getFullDay());

		if (model.getBatchNo() != null) {
			jsonObject.put("batchNo", model.getBatchNo());
		}
		if (model.getSupplier() != null) {
			jsonObject.put("supplier", model.getSupplier());
		}
		if (model.getContact() != null) {
			jsonObject.put("contact", model.getContact());
		}
		if (model.getStandard() != null) {
			jsonObject.put("standard", model.getStandard());
		}

		if (model.getAddress() != null) {
			jsonObject.put("address", model.getAddress());
		}

		jsonObject.put("quantity", model.getQuantity());
		if (model.getUnit() != null) {
			jsonObject.put("unit", model.getUnit());
		}
		jsonObject.put("price", model.getPrice());
		jsonObject.put("totalPrice", model.getTotalPrice());
		jsonObject.put("available", model.getAvailable());

		if (model.getExpiryDate() != null) {
			jsonObject.put("expiryDate", DateUtils.getDate(model.getExpiryDate()));
			jsonObject.put("expiryDate_date", DateUtils.getDate(model.getExpiryDate()));
			jsonObject.put("expiryDate_datetime", DateUtils.getDateTime(model.getExpiryDate()));
		}
		if (model.getReceiverId() != null) {
			jsonObject.put("receiverId", model.getReceiverId());
		}
		if (model.getReceiverName() != null) {
			jsonObject.put("receiverName", model.getReceiverName());
		}
		if (model.getRemark() != null) {
			jsonObject.put("remark", model.getRemark());
		}

		jsonObject.put("businessStatus", model.getBusinessStatus());

		if (model.getConfirmBy() != null) {
			jsonObject.put("confirmBy", model.getConfirmBy());
		}

		if (model.getConfirmTime() != null) {
			jsonObject.put("confirmTime", DateUtils.getDate(model.getConfirmTime()));
			jsonObject.put("confirmTime_date", DateUtils.getDate(model.getConfirmTime()));
			jsonObject.put("confirmTime_datetime", DateUtils.getDateTime(model.getConfirmTime()));
		}

		if (model.getCreateBy() != null) {
			jsonObject.put("createBy", model.getCreateBy());
		}
		if (model.getCreateTime() != null) {
			jsonObject.put("createTime", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_date", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_datetime", DateUtils.getDateTime(model.getCreateTime()));
		}
		return jsonObject;
	}

	public static ObjectNode toObjectNode(GoodsInStock model) {
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

		if (model.getInStockTime() != null) {
			jsonObject.put("inStockTime", DateUtils.getDate(model.getInStockTime()));
			jsonObject.put("inStockTime_date", DateUtils.getDate(model.getInStockTime()));
			jsonObject.put("inStockTime_datetime", DateUtils.getDateTime(model.getInStockTime()));
		}

		jsonObject.put("semester", model.getSemester());
		jsonObject.put("year", model.getYear());
		jsonObject.put("month", model.getMonth());
		jsonObject.put("day", model.getDay());
		jsonObject.put("week", model.getWeek());
		jsonObject.put("fullDay", model.getFullDay());

		if (model.getBatchNo() != null) {
			jsonObject.put("batchNo", model.getBatchNo());
		}
		if (model.getSupplier() != null) {
			jsonObject.put("supplier", model.getSupplier());
		}
		if (model.getContact() != null) {
			jsonObject.put("contact", model.getContact());
		}
		if (model.getStandard() != null) {
			jsonObject.put("standard", model.getStandard());
		}

		if (model.getAddress() != null) {
			jsonObject.put("address", model.getAddress());
		}

		jsonObject.put("quantity", model.getQuantity());
		if (model.getUnit() != null) {
			jsonObject.put("unit", model.getUnit());
		}
		jsonObject.put("price", model.getPrice());
		jsonObject.put("totalPrice", model.getTotalPrice());
		jsonObject.put("available", model.getAvailable());

		if (model.getExpiryDate() != null) {
			jsonObject.put("expiryDate", DateUtils.getDate(model.getExpiryDate()));
			jsonObject.put("expiryDate_date", DateUtils.getDate(model.getExpiryDate()));
			jsonObject.put("expiryDate_datetime", DateUtils.getDateTime(model.getExpiryDate()));
		}
		if (model.getReceiverId() != null) {
			jsonObject.put("receiverId", model.getReceiverId());
		}
		if (model.getReceiverName() != null) {
			jsonObject.put("receiverName", model.getReceiverName());
		}
		if (model.getRemark() != null) {
			jsonObject.put("remark", model.getRemark());
		}

		jsonObject.put("businessStatus", model.getBusinessStatus());
		if (model.getConfirmBy() != null) {
			jsonObject.put("confirmBy", model.getConfirmBy());
		}
		if (model.getConfirmTime() != null) {
			jsonObject.put("confirmTime", DateUtils.getDate(model.getConfirmTime()));
			jsonObject.put("confirmTime_date", DateUtils.getDate(model.getConfirmTime()));
			jsonObject.put("confirmTime_datetime", DateUtils.getDateTime(model.getConfirmTime()));
		}
		if (model.getCreateBy() != null) {
			jsonObject.put("createBy", model.getCreateBy());
		}
		if (model.getCreateTime() != null) {
			jsonObject.put("createTime", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_date", DateUtils.getDate(model.getCreateTime()));
			jsonObject.put("createTime_datetime", DateUtils.getDateTime(model.getCreateTime()));
		}
		return jsonObject;
	}

	public static JSONArray listToArray(java.util.List<GoodsInStock> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (GoodsInStock model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static java.util.List<GoodsInStock> arrayToList(JSONArray array) {
		java.util.List<GoodsInStock> list = new java.util.ArrayList<GoodsInStock>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			GoodsInStock model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	private GoodsInStockJsonFactory() {

	}

}
