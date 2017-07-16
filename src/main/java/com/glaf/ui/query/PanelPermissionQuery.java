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
package com.glaf.ui.query;

import java.util.*;
import com.glaf.core.query.DataQuery;

public class PanelPermissionQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected List<String> ids;
	protected Collection<String> appActorIds;
	protected String panelId;
	protected String roleId;
	protected List<String> roleIds;
	protected Date createDateGreaterThanOrEqual;
	protected Date createDateLessThanOrEqual;

	public PanelPermissionQuery() {

	}

	public PanelPermissionQuery actorIds(List<String> actorIds) {
		if (actorIds == null) {
			throw new RuntimeException("actorIds is empty ");
		}
		this.actorIds = actorIds;
		return this;
	}

	public PanelPermissionQuery createDateGreaterThanOrEqual(
			Date createDateGreaterThanOrEqual) {
		if (createDateGreaterThanOrEqual == null) {
			throw new RuntimeException("createDate is null");
		}
		this.createDateGreaterThanOrEqual = createDateGreaterThanOrEqual;
		return this;
	}

	public PanelPermissionQuery createDateLessThanOrEqual(
			Date createDateLessThanOrEqual) {
		if (createDateLessThanOrEqual == null) {
			throw new RuntimeException("createDate is null");
		}
		this.createDateLessThanOrEqual = createDateLessThanOrEqual;
		return this;
	}

	public List<String> getActorIds() {
		return actorIds;
	}

	public Collection<String> getAppActorIds() {
		return appActorIds;
	}

	public Date getCreateDateGreaterThanOrEqual() {
		return createDateGreaterThanOrEqual;
	}

	public Date getCreateDateLessThanOrEqual() {
		return createDateLessThanOrEqual;
	}

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("panelId".equals(sortColumn)) {
				orderBy = "E.PANELID_" + a_x;
			}

			if ("actorId".equals(sortColumn)) {
				orderBy = "E.ACTORID_" + a_x;
			}

			if ("roleId".equals(sortColumn)) {
				orderBy = "E.ROLEID_" + a_x;
			}

			if ("createDate".equals(sortColumn)) {
				orderBy = "E.CREATEDATE_" + a_x;
			}

			if ("createBy".equals(sortColumn)) {
				orderBy = "E.CREATEBY_" + a_x;
			}

		}
		return orderBy;
	}

	public String getPanelId() {
		return panelId;
	}

	public String getRoleId() {
		return roleId;
	}

	public List<String> getRoleIds() {
		return roleIds;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("panelId", "PANELID_");
		addColumn("actorId", "ACTORID_");
		addColumn("roleId", "ROLEID_");
		addColumn("createDate", "CREATEDATE_");
		addColumn("createBy", "CREATEBY_");
	}

	public PanelPermissionQuery panelId(String panelId) {
		if (panelId == null) {
			throw new RuntimeException("panelId is null");
		}
		this.panelId = panelId;
		return this;
	}

	public PanelPermissionQuery roleId(String roleId) {
		if (roleId == null) {
			throw new RuntimeException("roleId is null");
		}
		this.roleId = roleId;
		return this;
	}

	public PanelPermissionQuery roleIds(List<String> roleIds) {
		if (roleIds == null) {
			throw new RuntimeException("roleIds is empty ");
		}
		this.roleIds = roleIds;
		return this;
	}

	public void setActorId(String actorId) {
		this.actorId = actorId;
	}

	public void setActorIds(List<String> actorIds) {
		this.actorIds = actorIds;
	}

	public void setAppActorIds(Collection<String> appActorIds) {
		this.appActorIds = appActorIds;
	}

	public void setCreateDateGreaterThanOrEqual(
			Date createDateGreaterThanOrEqual) {
		this.createDateGreaterThanOrEqual = createDateGreaterThanOrEqual;
	}

	public void setCreateDateLessThanOrEqual(Date createDateLessThanOrEqual) {
		this.createDateLessThanOrEqual = createDateLessThanOrEqual;
	}

	public void setPanelId(String panelId) {
		this.panelId = panelId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public void setRoleIds(List<String> roleIds) {
		this.roleIds = roleIds;
	}

}