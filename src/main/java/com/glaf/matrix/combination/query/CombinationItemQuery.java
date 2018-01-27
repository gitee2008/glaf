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

package com.glaf.matrix.combination.query;

import java.util.*;
import com.glaf.core.query.DataQuery;

public class CombinationItemQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected Long syncId;
	protected List<Long> syncIds;
	protected String deploymentId;
	protected List<String> deploymentIds;
	protected String sqlLike;

	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	public CombinationItemQuery() {

	}

	public CombinationItemQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public CombinationItemQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public CombinationItemQuery deploymentId(String deploymentId) {
		if (deploymentId == null) {
			throw new RuntimeException("deploymentId is null");
		}
		this.deploymentId = deploymentId;
		return this;
	}

	public CombinationItemQuery deploymentIds(List<String> deploymentIds) {
		if (deploymentIds == null) {
			throw new RuntimeException("deploymentIds is empty ");
		}
		this.deploymentIds = deploymentIds;
		return this;
	}

	public Date getCreateTimeGreaterThanOrEqual() {
		return createTimeGreaterThanOrEqual;
	}

	public Date getCreateTimeLessThanOrEqual() {
		return createTimeLessThanOrEqual;
	}

	public String getDeploymentId() {
		return deploymentId;
	}

	public List<String> getDeploymentIds() {
		return deploymentIds;
	}

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("syncId".equals(sortColumn)) {
				orderBy = "E.SYNCID_" + a_x;
			}

			if ("deploymentId".equals(sortColumn)) {
				orderBy = "E.DEPLOYMENTID_" + a_x;
			}

			if ("sql".equals(sortColumn)) {
				orderBy = "E.SQL_" + a_x;
			}

			if ("targetTableName".equals(sortColumn)) {
				orderBy = "E.TARGETTABLENAME_" + a_x;
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

	public String getSqlLike() {
		if (sqlLike != null && sqlLike.trim().length() > 0) {
			if (!sqlLike.startsWith("%")) {
				sqlLike = "%" + sqlLike;
			}
			if (!sqlLike.endsWith("%")) {
				sqlLike = sqlLike + "%";
			}
		}
		return sqlLike;
	}

	public Long getSyncId() {
		return syncId;
	}

	public List<Long> getSyncIds() {
		return syncIds;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("syncId", "SYNCID_");
		addColumn("deploymentId", "DEPLOYMENTID_");
		addColumn("sql", "SQL_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}

	public void setDeploymentIds(List<String> deploymentIds) {
		this.deploymentIds = deploymentIds;
	}

	public void setSqlLike(String sqlLike) {
		this.sqlLike = sqlLike;
	}

	public void setSyncId(Long syncId) {
		this.syncId = syncId;
	}

	public void setSyncIds(List<Long> syncIds) {
		this.syncIds = syncIds;
	}

	public CombinationItemQuery sqlLike(String sqlLike) {
		if (sqlLike == null) {
			throw new RuntimeException("sql is null");
		}
		this.sqlLike = sqlLike;
		return this;
	}

	public CombinationItemQuery syncId(Long syncId) {
		if (syncId == null) {
			throw new RuntimeException("syncId is null");
		}
		this.syncId = syncId;
		return this;
	}

	public CombinationItemQuery syncIds(List<Long> syncIds) {
		if (syncIds == null) {
			throw new RuntimeException("syncIds is empty ");
		}
		this.syncIds = syncIds;
		return this;
	}

}