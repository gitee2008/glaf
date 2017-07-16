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

package com.glaf.core.query;

import java.util.List;

public class RoleQuery extends BaseQuery {
	private static final long serialVersionUID = 1L;
	protected String code;
	protected String name;
	protected String nameLike;
	protected String remarkLike;
	protected String roleId;
	protected List<String> roleIds;
	protected String tenantId;
	protected String type;
	protected List<String> types;

	public RoleQuery() {

	}

	public RoleQuery code(String code) {
		if (code == null) {
			throw new RuntimeException("code is null");
		}
		this.code = code;
		return this;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getNameLike() {
		if (nameLike != null && nameLike.trim().length() > 0) {
			if (!nameLike.startsWith("%")) {
				nameLike = "%" + nameLike;
			}
			if (!nameLike.endsWith("%")) {
				nameLike = nameLike + "%";
			}
		}
		return nameLike;
	}

	public String getRemarkLike() {
		if (remarkLike != null && remarkLike.trim().length() > 0) {
			if (!remarkLike.startsWith("%")) {
				remarkLike = "%" + remarkLike;
			}
			if (!remarkLike.endsWith("%")) {
				remarkLike = remarkLike + "%";
			}
		}
		return remarkLike;
	}

	public String getRoleId() {
		return roleId;
	}

	public List<String> getRoleIds() {
		return roleIds;
	}

	public String getTenantId() {
		return tenantId;
	}

	public String getType() {
		return type;
	}

	public List<String> getTypes() {
		return types;
	}

	public RoleQuery name(String name) {
		if (name == null) {
			throw new RuntimeException("name is null");
		}
		this.name = name;
		return this;
	}

	public RoleQuery nameLike(String nameLike) {
		if (nameLike == null) {
			throw new RuntimeException("name is null");
		}
		this.nameLike = nameLike;
		return this;
	}

	public RoleQuery remarkLike(String remarkLike) {
		if (remarkLike == null) {
			throw new RuntimeException("remark is null");
		}
		this.remarkLike = remarkLike;
		return this;
	}

	public RoleQuery roleId(String roleId) {
		if (roleId == null) {
			throw new RuntimeException("roleId is null");
		}
		this.roleId = roleId;
		return this;
	}

	public RoleQuery roleIds(List<String> roleIds) {
		if (roleIds == null) {
			throw new RuntimeException("roleIds is empty ");
		}
		this.roleIds = roleIds;
		return this;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNameLike(String nameLike) {
		this.nameLike = nameLike;
	}

	public void setRemarkLike(String remarkLike) {
		this.remarkLike = remarkLike;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public void setRoleIds(List<String> roleIds) {
		this.roleIds = roleIds;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}

	public RoleQuery tenantId(String tenantId) {
		if (tenantId == null) {
			throw new RuntimeException("tenantId is null");
		}
		this.tenantId = tenantId;
		return this;
	}

	public RoleQuery type(String type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		this.type = type;
		return this;
	}

}