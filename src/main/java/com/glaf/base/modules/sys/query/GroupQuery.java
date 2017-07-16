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
package com.glaf.base.modules.sys.query;

import java.util.*;
import com.glaf.core.query.DataQuery;

public class GroupQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected String descLike;
	protected String name;
	protected String nameLike;
	protected String codeLike;
	protected Integer sortGreaterThan;
	protected Integer sortGreaterThanOrEqual;
	protected Integer sortLessThan;
	protected Integer sortLessThanOrEqual;
	protected String type;
	protected String typeLike;
	protected List<String> types;
	protected String userId;
	protected String userIdLike;
	protected String unameLike;
	protected String dnameLike;
	protected String groupId;
	protected List<String> userIds;
	protected String manageUsersLike;

	public GroupQuery() {

	}

	public GroupQuery descLike(String descLike) {
		if (descLike == null) {
			throw new RuntimeException("desc is null");
		}
		this.descLike = descLike;
		return this;
	}

	public String getCodeLike() {
		return codeLike;
	}

	public String getDescLike() {
		if (descLike != null && descLike.trim().length() > 0) {
			if (!descLike.startsWith("%")) {
				descLike = "%" + descLike;
			}
			if (!descLike.endsWith("%")) {
				descLike = descLike + "%";
			}
		}
		return descLike;
	}

	public String getDnameLike() {
		if (dnameLike != null && dnameLike.trim().length() > 0) {
			if (!dnameLike.startsWith("%"))
				dnameLike = "%" + dnameLike;
			if (!dnameLike.endsWith("%"))
				dnameLike = dnameLike + "%";
		}
		return dnameLike;
	}

	public String getGroupId() {
		return groupId;
	}

	public String getManageUsersLike() {
		if (manageUsersLike != null && manageUsersLike.trim().length() > 0) {
			if (!manageUsersLike.startsWith("%")) {
				manageUsersLike = "%" + manageUsersLike;
			}
			if (!manageUsersLike.endsWith("%")) {
				manageUsersLike = manageUsersLike + "%";
			}
		}
		return manageUsersLike;
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

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("name".equals(sortColumn)) {
				orderBy = "E.NAME" + a_x;
			}

			if ("desc".equals(sortColumn)) {
				orderBy = "E.GROUPDESC" + a_x;
			}

			if ("type".equals(sortColumn)) {
				orderBy = "E.TYPE" + a_x;
			}

			if ("createBy".equals(sortColumn)) {
				orderBy = "E.CREATEBY" + a_x;
			}

			if ("sort".equals(sortColumn)) {
				orderBy = "E.SORTNONO" + a_x;
			}

		}
		return orderBy;
	}

	public Integer getSortGreaterThan() {
		return sortGreaterThan;
	}

	public Integer getSortGreaterThanOrEqual() {
		return sortGreaterThanOrEqual;
	}

	public Integer getSortLessThan() {
		return sortLessThan;
	}

	public Integer getSortLessThanOrEqual() {
		return sortLessThanOrEqual;
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

	public String getUnameLike() {
		if (unameLike != null && unameLike.trim().length() > 0) {
			if (!unameLike.startsWith("%"))
				unameLike = "%" + unameLike;
			if (!unameLike.endsWith("%"))
				unameLike = unameLike + "%";
		}
		return unameLike;
	}

	public String getUserId() {
		return userId;
	}

	public String getUserIdLike() {
		if (userIdLike != null && userIdLike.trim().length() > 0) {
			if (!userIdLike.startsWith("%"))
				userIdLike = "%" + userIdLike;
			if (!userIdLike.endsWith("%"))
				userIdLike = userIdLike + "%";
		}
		return userIdLike;
	}

	public List<String> getUserIds() {
		return userIds;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("groupId", "GROUPID");
		addColumn("name", "NAME");
		addColumn("desc", "GROUPDESC");
		addColumn("type", "TYPE");
		addColumn("createBy", "CREATEBY");
		addColumn("sort", "SORTNONO");
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

	public void setDescLike(String descLike) {
		this.descLike = descLike;
	}

	public void setDnameLike(String dnameLike) {
		this.dnameLike = dnameLike;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public void setManageUsersLike(String manageUsersLike) {
		this.manageUsersLike = manageUsersLike;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNameLike(String nameLike) {
		this.nameLike = nameLike;
	}

	public void setSortGreaterThan(Integer sortGreaterThan) {
		this.sortGreaterThan = sortGreaterThan;
	}

	public void setSortGreaterThanOrEqual(Integer sortGreaterThanOrEqual) {
		this.sortGreaterThanOrEqual = sortGreaterThanOrEqual;
	}

	public void setSortLessThan(Integer sortLessThan) {
		this.sortLessThan = sortLessThan;
	}

	public void setSortLessThanOrEqual(Integer sortLessThanOrEqual) {
		this.sortLessThanOrEqual = sortLessThanOrEqual;
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

	public void setUnameLike(String unameLike) {
		this.unameLike = unameLike;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setUserIdLike(String userIdLike) {
		this.userIdLike = userIdLike;
	}

	public void setUserIds(List<String> userIds) {
		this.userIds = userIds;
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