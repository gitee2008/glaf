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

public class TenantConfigQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected List<String> tenantIds;
	protected String sysName;
	protected String sysNameLike;
	protected String shareFlag;
	protected Long typeId;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	public TenantConfigQuery() {

	}

	public TenantConfigQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public TenantConfigQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
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

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("tenantId".equals(sortColumn)) {
				orderBy = "E.TENANTID_" + a_x;
			}

			if ("sysName".equals(sortColumn)) {
				orderBy = "E.SYSNAME_" + a_x;
			}

			if ("typeId".equals(sortColumn)) {
				orderBy = "E.TYPEID_" + a_x;
			}

			if ("breakfastTime".equals(sortColumn)) {
				orderBy = "E.BREAKFASTTIME_" + a_x;
			}

			if ("breakfastMidTime".equals(sortColumn)) {
				orderBy = "E.BREAKFASMIDTTIME_" + a_x;
			}

			if ("lunchTime".equals(sortColumn)) {
				orderBy = "E.LUNCHTIME_" + a_x;
			}

			if ("snackTime".equals(sortColumn)) {
				orderBy = "E.SNACKTIME_" + a_x;
			}

			if ("dinnerTime".equals(sortColumn)) {
				orderBy = "E.DINNERTIME_" + a_x;
			}

			if ("createBy".equals(sortColumn)) {
				orderBy = "E.CREATEBY_" + a_x;
			}

			if ("createTime".equals(sortColumn)) {
				orderBy = "E.CREATETIME_" + a_x;
			}

			if ("updateBy".equals(sortColumn)) {
				orderBy = "E.UPDATEBY_" + a_x;
			}

			if ("updateTime".equals(sortColumn)) {
				orderBy = "E.UPDATETIME_" + a_x;
			}

		}
		return orderBy;
	}

	public String getShareFlag() {
		return shareFlag;
	}

	public String getSysName() {
		return sysName;
	}

	public String getSysNameLike() {
		if (sysNameLike != null && sysNameLike.trim().length() > 0) {
			if (!sysNameLike.startsWith("%")) {
				sysNameLike = "%" + sysNameLike;
			}
			if (!sysNameLike.endsWith("%")) {
				sysNameLike = sysNameLike + "%";
			}
		}
		return sysNameLike;
	}

	public String getTenantId() {
		return tenantId;
	}

	public List<String> getTenantIds() {
		return tenantIds;
	}

	public Long getTypeId() {
		return typeId;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("tenantId", "TENANTID_");
		addColumn("sysName", "SYSNAME_");
		addColumn("typeId", "TYPEID_");
		addColumn("breakfastTime", "BREAKFASTTIME_");
		addColumn("breakfastMidTime", "BREAKFASMIDTTIME_");
		addColumn("lunchTime", "LUNCHTIME_");
		addColumn("snackTime", "SNACKTIME_");
		addColumn("dinnerTime", "DINNERTIME_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
		addColumn("updateBy", "UPDATEBY_");
		addColumn("updateTime", "UPDATETIME_");
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setShareFlag(String shareFlag) {
		this.shareFlag = shareFlag;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}

	public void setSysNameLike(String sysNameLike) {
		this.sysNameLike = sysNameLike;
	}

	public void setTenantIds(List<String> tenantIds) {
		this.tenantIds = tenantIds;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public TenantConfigQuery shareFlag(String shareFlag) {
		if (shareFlag == null) {
			throw new RuntimeException("shareFlag is null");
		}
		this.shareFlag = shareFlag;
		return this;
	}

	public TenantConfigQuery sysName(String sysName) {
		if (sysName == null) {
			throw new RuntimeException("sysName is null");
		}
		this.sysName = sysName;
		return this;
	}

	public TenantConfigQuery sysNameLike(String sysNameLike) {
		if (sysNameLike == null) {
			throw new RuntimeException("sysName is null");
		}
		this.sysNameLike = sysNameLike;
		return this;
	}

	public TenantConfigQuery tenantIds(List<String> tenantIds) {
		if (tenantIds == null) {
			throw new RuntimeException("tenantIds is empty ");
		}
		this.tenantIds = tenantIds;
		return this;
	}

	public TenantConfigQuery typeId(Long typeId) {
		if (typeId == null) {
			throw new RuntimeException("typeId is null");
		}
		this.typeId = typeId;
		return this;
	}

}