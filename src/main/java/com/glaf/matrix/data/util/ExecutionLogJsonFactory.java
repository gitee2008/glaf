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

import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.glaf.core.util.DateUtils;
import com.glaf.matrix.data.domain.*;

/**
 * 
 * JSON工厂类
 *
 */
public class ExecutionLogJsonFactory {

	public static ExecutionLog jsonToObject(JSONObject jsonObject) {
		ExecutionLog model = new ExecutionLog();
		if (jsonObject.containsKey("id")) {
			model.setId(jsonObject.getLong("id"));
		}
		if (jsonObject.containsKey("type")) {
			model.setType(jsonObject.getString("type"));
		}
		if (jsonObject.containsKey("businessKey")) {
			model.setBusinessKey(jsonObject.getString("businessKey"));
		}
		if (jsonObject.containsKey("jobNo")) {
			model.setJobNo(jsonObject.getString("jobNo"));
		}
		if (jsonObject.containsKey("title")) {
			model.setTitle(jsonObject.getString("title"));
		}
		if (jsonObject.containsKey("content")) {
			model.setContent(jsonObject.getString("content"));
		}
		if (jsonObject.containsKey("startTime")) {
			model.setStartTime(jsonObject.getDate("startTime"));
		}
		if (jsonObject.containsKey("endTime")) {
			model.setEndTime(jsonObject.getDate("endTime"));
		}
		if (jsonObject.containsKey("runDay")) {
			model.setRunDay(jsonObject.getInteger("runDay"));
		}
		if (jsonObject.containsKey("runHour")) {
			model.setRunHour(jsonObject.getInteger("runHour"));
		}
		if (jsonObject.containsKey("runTime")) {
			model.setRunTime(jsonObject.getLong("runTime"));
		}
		if (jsonObject.containsKey("status")) {
			model.setStatus(jsonObject.getInteger("status"));
		}
		if (jsonObject.containsKey("exitCode")) {
			model.setExitCode(jsonObject.getString("exitCode"));
		}
		if (jsonObject.containsKey("exitMessage")) {
			model.setExitMessage(jsonObject.getString("exitMessage"));
		}
		if (jsonObject.containsKey("createBy")) {
			model.setCreateBy(jsonObject.getString("createBy"));
		}
		if (jsonObject.containsKey("createTime")) {
			model.setCreateTime(jsonObject.getDate("createTime"));
		}

		return model;
	}

	public static JSONObject toJsonObject(ExecutionLog model) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		if (model.getBusinessKey() != null) {
			jsonObject.put("businessKey", model.getBusinessKey());
		}
		if (model.getJobNo() != null) {
			jsonObject.put("jobNo", model.getJobNo());
		}
		if (model.getTitle() != null) {
			jsonObject.put("title", model.getTitle());
		}
		if (model.getContent() != null) {
			jsonObject.put("content", model.getContent());
		}
		if (model.getStartTime() != null) {
			jsonObject.put("startTime", DateUtils.getDate(model.getStartTime()));
			jsonObject.put("startTime_date", DateUtils.getDate(model.getStartTime()));
			jsonObject.put("startTime_datetime", DateUtils.getDateTime(model.getStartTime()));
		}
		if (model.getEndTime() != null) {
			jsonObject.put("endTime", DateUtils.getDate(model.getEndTime()));
			jsonObject.put("endTime_date", DateUtils.getDate(model.getEndTime()));
			jsonObject.put("endTime_datetime", DateUtils.getDateTime(model.getEndTime()));
		}
		jsonObject.put("runDay", model.getRunDay());
		jsonObject.put("runHour", model.getRunHour());
		jsonObject.put("runTime", model.getRunTime());
		jsonObject.put("status", model.getStatus());
		if (model.getExitCode() != null) {
			jsonObject.put("exitCode", model.getExitCode());
		}
		if (model.getExitMessage() != null) {
			jsonObject.put("exitMessage", model.getExitMessage());
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

	public static ObjectNode toObjectNode(ExecutionLog model) {
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		jsonObject.put("id", model.getId());
		jsonObject.put("_id_", model.getId());
		jsonObject.put("_oid_", model.getId());
		if (model.getType() != null) {
			jsonObject.put("type", model.getType());
		}
		if (model.getBusinessKey() != null) {
			jsonObject.put("businessKey", model.getBusinessKey());
		}
		if (model.getJobNo() != null) {
			jsonObject.put("jobNo", model.getJobNo());
		}
		if (model.getTitle() != null) {
			jsonObject.put("title", model.getTitle());
		}
		if (model.getContent() != null) {
			jsonObject.put("content", model.getContent());
		}
		if (model.getStartTime() != null) {
			jsonObject.put("startTime", DateUtils.getDate(model.getStartTime()));
			jsonObject.put("startTime_date", DateUtils.getDate(model.getStartTime()));
			jsonObject.put("startTime_datetime", DateUtils.getDateTime(model.getStartTime()));
		}
		if (model.getEndTime() != null) {
			jsonObject.put("endTime", DateUtils.getDate(model.getEndTime()));
			jsonObject.put("endTime_date", DateUtils.getDate(model.getEndTime()));
			jsonObject.put("endTime_datetime", DateUtils.getDateTime(model.getEndTime()));
		}
		jsonObject.put("runDay", model.getRunDay());
		jsonObject.put("runHour", model.getRunHour());
		jsonObject.put("runTime", model.getRunTime());
		jsonObject.put("status", model.getStatus());
		if (model.getExitCode() != null) {
			jsonObject.put("exitCode", model.getExitCode());
		}
		if (model.getExitMessage() != null) {
			jsonObject.put("exitMessage", model.getExitMessage());
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

	public static JSONArray listToArray(java.util.List<ExecutionLog> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (ExecutionLog model : list) {
				JSONObject jsonObject = model.toJsonObject();
				array.add(jsonObject);
			}
		}
		return array;
	}

	public static java.util.List<ExecutionLog> arrayToList(JSONArray array) {
		java.util.List<ExecutionLog> list = new java.util.ArrayList<ExecutionLog>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			ExecutionLog model = jsonToObject(jsonObject);
			list.add(model);
		}
		return list;
	}

	private ExecutionLogJsonFactory() {

	}

}
