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
import com.glaf.core.identity.Tenant;
import com.glaf.core.identity.util.TenantJsonFactory;

public class TenantImpl implements Tenant {

	private static final long serialVersionUID = 1L;

	protected long id;

	protected long databaseId;

	protected int limit;

	protected int locked;

	protected String name;

	protected String tenantId;

	protected int tenantType;

	protected String type;

	protected boolean isSystemAdministrator;

	public TenantImpl() {

	}

	public long getDatabaseId() {
		return databaseId;
	}

	public long getId() {
		return id;
	}

	public int getLimit() {
		return limit;
	}

	public int getLocked() {
		return locked;
	}

	public String getName() {
		return name;
	}

	public String getTenantId() {
		return tenantId;
	}

	public int getTenantType() {
		return tenantType;
	}

	public String getType() {
		return type;
	}

	public boolean isSystemAdministrator() {
		return isSystemAdministrator;
	}

	public Tenant jsonToObject(JSONObject jsonObject) {
		return TenantJsonFactory.jsonToObject(jsonObject);
	}

	public void setDatabaseId(long databaseId) {
		this.databaseId = databaseId;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public void setLocked(int locked) {
		this.locked = locked;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSystemAdministrator(boolean isSystemAdministrator) {
		this.isSystemAdministrator = isSystemAdministrator;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setTenantType(int tenantType) {
		this.tenantType = tenantType;
	}

	public void setType(String type) {
		this.type = type;
	}

	public JSONObject toJsonObject() {
		return TenantJsonFactory.toJsonObject(this);
	}

}
