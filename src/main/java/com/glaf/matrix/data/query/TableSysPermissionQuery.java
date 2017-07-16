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

package com.glaf.matrix.data.query;

import java.util.Date;
import java.util.List;

import com.glaf.core.query.DataQuery;

public class TableSysPermissionQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected String tableId;
	protected List<String> tableIds;
	protected String tableName;
	protected String tableNameLike;
	protected String grantee;
	protected String granteeLike;
	protected String granteeType;
	protected String privilege;
	protected String privilegeLike;
	protected String type;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	public TableSysPermissionQuery() {

	}

	public TableSysPermissionQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public TableSysPermissionQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public Date getCreateTimeGreaterThanOrEqual() {
		return createTimeGreaterThanOrEqual;
	}

	public Date getCreateTimeLessThanOrEqual() {
		return createTimeLessThanOrEqual;
	}

	public String getGrantee() {
		return grantee;
	}

	public String getGranteeLike() {
		if (granteeLike != null && granteeLike.trim().length() > 0) {
			if (!granteeLike.startsWith("%")) {
				granteeLike = "%" + granteeLike;
			}
			if (!granteeLike.endsWith("%")) {
				granteeLike = granteeLike + "%";
			}
		}
		return granteeLike;
	}

	public String getGranteeType() {
		return granteeType;
	}

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("tableId".equals(sortColumn)) {
				orderBy = "E.TABLEID_" + a_x;
			}

			if ("tableName".equals(sortColumn)) {
				orderBy = "E.TABLENAME_" + a_x;
			}

			if ("grantee".equals(sortColumn)) {
				orderBy = "E.GRANTEE_" + a_x;
			}

			if ("granteeType".equals(sortColumn)) {
				orderBy = "E.GRANTEETYPE_" + a_x;
			}

			if ("privilege".equals(sortColumn)) {
				orderBy = "E.PRIVILEGE_" + a_x;
			}

			if ("type".equals(sortColumn)) {
				orderBy = "E.TYPE_" + a_x;
			}

			if ("locked".equals(sortColumn)) {
				orderBy = "E.LOCKED_" + a_x;
			}

			if ("createBy".equals(sortColumn)) {
				orderBy = "E.CREATEBY_" + a_x;
			}

			if ("createTime".equals(sortColumn)) {
				orderBy = "E.CREATETIME_" + a_x;
			}

		}
		return orderBy;
	}

	public String getPrivilege() {
		return privilege;
	}

	public String getPrivilegeLike() {
		if (privilegeLike != null && privilegeLike.trim().length() > 0) {
			if (!privilegeLike.endsWith("%")) {
				privilegeLike = privilegeLike + "%";
			}
		}
		return privilegeLike;
	}

	public String getTableId() {
		return tableId;
	}

	public List<String> getTableIds() {
		return tableIds;
	}

	public String getTableName() {
		return tableName;
	}

	public String getTableNameLike() {
		if (tableNameLike != null && tableNameLike.trim().length() > 0) {
			if (!tableNameLike.startsWith("%")) {
				tableNameLike = "%" + tableNameLike;
			}
			if (!tableNameLike.endsWith("%")) {
				tableNameLike = tableNameLike + "%";
			}
		}
		return tableNameLike;
	}

	public String getType() {
		return type;
	}

	public TableSysPermissionQuery grantee(String grantee) {
		if (grantee == null) {
			throw new RuntimeException("grantee is null");
		}
		this.grantee = grantee;
		return this;
	}

	public TableSysPermissionQuery granteeLike(String granteeLike) {
		if (granteeLike == null) {
			throw new RuntimeException("grantee is null");
		}
		this.granteeLike = granteeLike;
		return this;
	}

	public TableSysPermissionQuery granteeType(String granteeType) {
		if (granteeType == null) {
			throw new RuntimeException("granteeType is null");
		}
		this.granteeType = granteeType;
		return this;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("tableId", "TABLEID_");
		addColumn("tableName", "TABLENAME_");
		addColumn("grantee", "GRANTEE_");
		addColumn("granteeType", "GRANTEETYPE_");
		addColumn("privilege", "PRIVILEGE_");
		addColumn("type", "TYPE_");
		addColumn("locked", "LOCKED_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
	}

	public TableSysPermissionQuery privilege(String privilege) {
		if (privilege == null) {
			throw new RuntimeException("privilege is null");
		}
		this.privilege = privilege;
		return this;
	}

	public TableSysPermissionQuery privilegeLike(String privilegeLike) {
		if (privilegeLike == null) {
			throw new RuntimeException("privilege is null");
		}
		this.privilegeLike = privilegeLike;
		return this;
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setGrantee(String grantee) {
		this.grantee = grantee;
	}

	public void setGranteeLike(String granteeLike) {
		this.granteeLike = granteeLike;
	}

	public void setGranteeType(String granteeType) {
		this.granteeType = granteeType;
	}

	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}

	public void setPrivilegeLike(String privilegeLike) {
		this.privilegeLike = privilegeLike;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public void setTableIds(List<String> tableIds) {
		this.tableIds = tableIds;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setTableNameLike(String tableNameLike) {
		this.tableNameLike = tableNameLike;
	}

	public void setType(String type) {
		this.type = type;
	}

	public TableSysPermissionQuery tableId(String tableId) {
		if (tableId == null) {
			throw new RuntimeException("tableId is null");
		}
		this.tableId = tableId;
		return this;
	}

	public TableSysPermissionQuery tableIds(List<String> tableIds) {
		if (tableIds == null) {
			throw new RuntimeException("tableIds is empty ");
		}
		this.tableIds = tableIds;
		return this;
	}

	public TableSysPermissionQuery tableName(String tableName) {
		if (tableName == null) {
			throw new RuntimeException("tableName is null");
		}
		this.tableName = tableName;
		return this;
	}

	public TableSysPermissionQuery tableNameLike(String tableNameLike) {
		if (tableNameLike == null) {
			throw new RuntimeException("tableName is null");
		}
		this.tableNameLike = tableNameLike;
		return this;
	}

	public TableSysPermissionQuery type(String type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		this.type = type;
		return this;
	}

}