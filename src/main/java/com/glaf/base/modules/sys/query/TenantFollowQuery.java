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

public class TenantFollowQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected List<String> tenantIds;
	protected String tenantName;
	protected String tenantNameLike;
	protected String followTenantId;
	protected String followTenantIdLike;
	protected List<String> followTenantIds;
	protected String followTenantName;
	protected String followTenantNameLike;
	protected String province;
	protected String city;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	public TenantFollowQuery() {

	}

	public TenantFollowQuery city(String city) {
		if (city == null) {
			throw new RuntimeException("city is null");
		}
		this.city = city;
		return this;
	}

	public TenantFollowQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public TenantFollowQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public TenantFollowQuery followTenantId(String followTenantId) {
		if (followTenantId == null) {
			throw new RuntimeException("followTenantId is null");
		}
		this.followTenantId = followTenantId;
		return this;
	}

	public TenantFollowQuery followTenantIdLike(String followTenantIdLike) {
		if (followTenantIdLike == null) {
			throw new RuntimeException("followTenantId is null");
		}
		this.followTenantIdLike = followTenantIdLike;
		return this;
	}

	public TenantFollowQuery followTenantIds(List<String> followTenantIds) {
		if (followTenantIds == null) {
			throw new RuntimeException("followTenantIds is empty ");
		}
		this.followTenantIds = followTenantIds;
		return this;
	}

	public TenantFollowQuery followTenantName(String followTenantName) {
		if (followTenantName == null) {
			throw new RuntimeException("followTenantName is null");
		}
		this.followTenantName = followTenantName;
		return this;
	}

	public TenantFollowQuery followTenantNameLike(String followTenantNameLike) {
		if (followTenantNameLike == null) {
			throw new RuntimeException("followTenantName is null");
		}
		this.followTenantNameLike = followTenantNameLike;
		return this;
	}

	public String getCity() {
		return city;
	}

	public Date getCreateTimeGreaterThanOrEqual() {
		return createTimeGreaterThanOrEqual;
	}

	public Date getCreateTimeLessThanOrEqual() {
		return createTimeLessThanOrEqual;
	}

	public String getFollowTenantId() {
		return followTenantId;
	}

	public String getFollowTenantIdLike() {
		if (followTenantIdLike != null && followTenantIdLike.trim().length() > 0) {
			if (!followTenantIdLike.startsWith("%")) {
				followTenantIdLike = "%" + followTenantIdLike;
			}
			if (!followTenantIdLike.endsWith("%")) {
				followTenantIdLike = followTenantIdLike + "%";
			}
		}
		return followTenantIdLike;
	}

	public List<String> getFollowTenantIds() {
		return followTenantIds;
	}

	public String getFollowTenantName() {
		return followTenantName;
	}

	public String getFollowTenantNameLike() {
		if (followTenantNameLike != null && followTenantNameLike.trim().length() > 0) {
			if (!followTenantNameLike.startsWith("%")) {
				followTenantNameLike = "%" + followTenantNameLike;
			}
			if (!followTenantNameLike.endsWith("%")) {
				followTenantNameLike = followTenantNameLike + "%";
			}
		}
		return followTenantNameLike;
	}

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("tenantId".equals(sortColumn)) {
				orderBy = "E.TENANTID_" + a_x;
			}

			if ("tenantName".equals(sortColumn)) {
				orderBy = "E.TENANTNAME_" + a_x;
			}

			if ("followTenantId".equals(sortColumn)) {
				orderBy = "E.FOLLOWTENANTID_" + a_x;
			}

			if ("followTenantName".equals(sortColumn)) {
				orderBy = "E.FOLLOWTENANTNAME_" + a_x;
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

	public String getProvince() {
		return province;
	}

	public List<String> getTenantIds() {
		return tenantIds;
	}

	public String getTenantName() {
		return tenantName;
	}

	public String getTenantNameLike() {
		if (tenantNameLike != null && tenantNameLike.trim().length() > 0) {
			if (!tenantNameLike.startsWith("%")) {
				tenantNameLike = "%" + tenantNameLike;
			}
			if (!tenantNameLike.endsWith("%")) {
				tenantNameLike = tenantNameLike + "%";
			}
		}
		return tenantNameLike;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("tenantId", "TENANTID_");
		addColumn("tenantName", "TENANTNAME_");
		addColumn("followTenantId", "FOLLOWTENANTID_");
		addColumn("followTenantName", "FOLLOWTENANTNAME_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
	}

	public TenantFollowQuery province(String province) {
		if (province == null) {
			throw new RuntimeException("province is null");
		}
		this.province = province;
		return this;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setFollowTenantId(String followTenantId) {
		this.followTenantId = followTenantId;
	}

	public void setFollowTenantIdLike(String followTenantIdLike) {
		this.followTenantIdLike = followTenantIdLike;
	}

	public void setFollowTenantIds(List<String> followTenantIds) {
		this.followTenantIds = followTenantIds;
	}

	public void setFollowTenantName(String followTenantName) {
		this.followTenantName = followTenantName;
	}

	public void setFollowTenantNameLike(String followTenantNameLike) {
		this.followTenantNameLike = followTenantNameLike;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public void setTenantIds(List<String> tenantIds) {
		this.tenantIds = tenantIds;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public void setTenantNameLike(String tenantNameLike) {
		this.tenantNameLike = tenantNameLike;
	}

	public TenantFollowQuery tenantIds(List<String> tenantIds) {
		if (tenantIds == null) {
			throw new RuntimeException("tenantIds is empty ");
		}
		this.tenantIds = tenantIds;
		return this;
	}

	public TenantFollowQuery tenantName(String tenantName) {
		if (tenantName == null) {
			throw new RuntimeException("tenantName is null");
		}
		this.tenantName = tenantName;
		return this;
	}

	public TenantFollowQuery tenantNameLike(String tenantNameLike) {
		if (tenantNameLike == null) {
			throw new RuntimeException("tenantName is null");
		}
		this.tenantNameLike = tenantNameLike;
		return this;
	}

}