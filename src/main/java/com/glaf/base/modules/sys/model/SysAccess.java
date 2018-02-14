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

package com.glaf.base.modules.sys.model;

import java.io.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.base.modules.sys.util.*;
import com.glaf.core.base.JSONable;

@Entity
@Table(name = "SYS_APP_ACCESS")
public class SysAccess implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID", nullable = false)
	protected long id;

	/**
	 * 应用编号
	 */
	@Column(name = "APPID", nullable = false)
	protected long appId;

	/**
	 * 角色编号
	 */
	@Column(name = "ROLEID", nullable = false)
	protected String roleId;

	/**
	 * 所属租户
	 */
	@Column(name = "TENANTID")
	protected String tenantId;

	public SysAccess() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SysAccess other = (SysAccess) obj;
		if (appId != other.appId)
			return false;
		if (roleId == null) {
			if (other.roleId != null)
				return false;
		} else if (!roleId.equals(other.roleId))
			return false;
		return true;
	}

	public long getAppId() {
		return this.appId;
	}

	public long getId() {
		return id;
	}

	public String getRoleId() {
		return this.roleId;
	}

	public String getTenantId() {
		return tenantId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (appId ^ (appId >>> 32));
		result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
		return result;
	}

	public SysAccess jsonToObject(JSONObject jsonObject) {
		return SysAccessJsonFactory.jsonToObject(jsonObject);
	}

	public void setAppId(long appId) {
		this.appId = appId;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public JSONObject toJsonObject() {
		return SysAccessJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return SysAccessJsonFactory.toObjectNode(this);
	}

	public String toString() {
		return toJsonObject().toJSONString();
	}

}
