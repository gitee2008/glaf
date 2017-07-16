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

package com.glaf.core.identity.impl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.core.identity.Group;
import com.glaf.core.identity.util.GroupJsonFactory;

public class GroupImpl implements Group {
	private static final long serialVersionUID = 1L;
	protected String groupId;
	protected String name;
	protected String code;
	protected String type;
	protected int sort;
	protected String tenantId;

	public GroupImpl() {

	}

	public String getCode() {
		return code;
	}

	public String getGroupId() {
		return groupId;
	}

	public String getName() {
		return name;
	}

	public int getSort() {
		return sort;
	}

	public String getTenantId() {
		return tenantId;
	}

	public String getType() {
		return type;
	}

	public Group jsonToObject(JSONObject jsonObject) {
		return GroupJsonFactory.jsonToObject(jsonObject);
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setType(String type) {
		this.type = type;
	}

	public JSONObject toJsonObject() {
		return GroupJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return GroupJsonFactory.toObjectNode(this);
	}

}
