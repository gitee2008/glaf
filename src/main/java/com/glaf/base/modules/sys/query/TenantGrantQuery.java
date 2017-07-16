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

public class TenantGrantQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected String grantee;
	protected String granteeLike;
	protected String privilege;
	protected String privilegeLike;
	protected List<String> tenantIds;
	protected String type;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	public TenantGrantQuery() {

	}

	public TenantGrantQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public TenantGrantQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
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

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("grantee".equals(sortColumn)) {
				orderBy = "E.GRANTEE_" + a_x;
			}

			if ("privilege".equals(sortColumn)) {
				orderBy = "E.PRIVILEGE_" + a_x;
			}

			if ("tenantId".equals(sortColumn)) {
				orderBy = "E.TENANTID_" + a_x;
			}

			if ("type".equals(sortColumn)) {
				orderBy = "E.TYPE_" + a_x;
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
			if (!privilegeLike.startsWith("%")) {
				privilegeLike = "%" + privilegeLike;
			}
			if (!privilegeLike.endsWith("%")) {
				privilegeLike = privilegeLike + "%";
			}
		}
		return privilegeLike;
	}

	public List<String> getTenantIds() {
		return tenantIds;
	}

	public String getType() {
		return type;
	}

	public TenantGrantQuery grantee(String grantee) {
		if (grantee == null) {
			throw new RuntimeException("grantee is null");
		}
		this.grantee = grantee;
		return this;
	}

	public TenantGrantQuery granteeLike(String granteeLike) {
		if (granteeLike == null) {
			throw new RuntimeException("grantee is null");
		}
		this.granteeLike = granteeLike;
		return this;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("grantee", "GRANTEE_");
		addColumn("privilege", "PRIVILEGE_");
		addColumn("tenantId", "TENANTID_");
		addColumn("type", "TYPE_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
	}

	public TenantGrantQuery privilege(String privilege) {
		if (privilege == null) {
			throw new RuntimeException("privilege is null");
		}
		this.privilege = privilege;
		return this;
	}

	public TenantGrantQuery privilegeLike(String privilegeLike) {
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

	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}

	public void setPrivilegeLike(String privilegeLike) {
		this.privilegeLike = privilegeLike;
	}

	public void setTenantIds(List<String> tenantIds) {
		this.tenantIds = tenantIds;
	}

	public void setType(String type) {
		this.type = type;
	}

	public TenantGrantQuery tenantIds(List<String> tenantIds) {
		if (tenantIds == null) {
			throw new RuntimeException("tenantIds is empty ");
		}
		this.tenantIds = tenantIds;
		return this;
	}

	public TenantGrantQuery type(String type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		this.type = type;
		return this;
	}

}