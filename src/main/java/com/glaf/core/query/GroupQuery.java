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

import java.util.*;

public class GroupQuery extends BaseQuery {
	private static final long serialVersionUID = 1L;
	protected String name;
	protected String nameLike;
	protected String codeLike;
	protected String type;
	protected String typeLike;
	protected List<String> types;
	protected String groupId;
	protected List<String> groupIds;
	protected String tenantId;

	public GroupQuery() {

	}

	public String getCodeLike() {
		return codeLike;
	}

	public String getGroupId() {
		return groupId;
	}

	public List<String> getGroupIds() {
		return groupIds;
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

	public String getTenantId() {
		return tenantId;
	}

	public String getType() {
		return type;
	}

	public String getTypeLike() {
		if (typeLike != null && typeLike.trim().length() > 0) {
			if (!typeLike.startsWith("%")) {
				typeLike = "%" + typeLike;
			}
			if (!typeLike.endsWith("%")) {
				typeLike = typeLike + "%";
			}
		}
		return typeLike;
	}

	public List<String> getTypes() {
		return types;
	}

	public GroupQuery groupIds(List<String> groupIds) {
		if (groupIds == null) {
			throw new RuntimeException("groupIds is empty ");
		}
		this.groupIds = groupIds;
		return this;
	}

	public GroupQuery name(String name) {
		if (name == null) {
			throw new RuntimeException("name is null");
		}
		this.name = name;
		return this;
	}

	public GroupQuery nameLike(String nameLike) {
		if (nameLike == null) {
			throw new RuntimeException("name is null");
		}
		this.nameLike = nameLike;
		return this;
	}

	public void setCodeLike(String codeLike) {
		this.codeLike = codeLike;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public void setGroupIds(List<String> groupIds) {
		this.groupIds = groupIds;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNameLike(String nameLike) {
		this.nameLike = nameLike;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setTypeLike(String typeLike) {
		this.typeLike = typeLike;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}

	public GroupQuery tenantId(String tenantId) {
		if (tenantId == null) {
			throw new RuntimeException("tenantId is null");
		}
		this.tenantId = tenantId;
		return this;
	}

	public GroupQuery type(String type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		this.type = type;
		return this;
	}

	public GroupQuery typeLike(String typeLike) {
		if (typeLike == null) {
			throw new RuntimeException("type is null");
		}
		this.typeLike = typeLike;
		return this;
	}

	public GroupQuery types(List<String> types) {
		if (types == null) {
			throw new RuntimeException("types is empty ");
		}
		this.types = types;
		return this;
	}

}