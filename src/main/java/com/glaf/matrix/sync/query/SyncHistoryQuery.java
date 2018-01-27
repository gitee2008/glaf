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

package com.glaf.matrix.sync.query;

import java.util.*;
import com.glaf.core.query.DataQuery;

public class SyncHistoryQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected Long syncId;
	protected List<Long> syncIds;
	protected String deploymentId;
	protected List<String> deploymentIds;
	protected Long databaseId;
	protected List<Long> databaseIds;
	protected Integer totalTimeGreaterThanOrEqual;
	protected Integer totalTimeLessThanOrEqual;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	public SyncHistoryQuery() {

	}

	public SyncHistoryQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public SyncHistoryQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public SyncHistoryQuery databaseId(Long databaseId) {
		if (databaseId == null) {
			throw new RuntimeException("databaseId is null");
		}
		this.databaseId = databaseId;
		return this;
	}

	public SyncHistoryQuery databaseIds(List<Long> databaseIds) {
		if (databaseIds == null) {
			throw new RuntimeException("databaseIds is empty ");
		}
		this.databaseIds = databaseIds;
		return this;
	}

	public SyncHistoryQuery deploymentId(String deploymentId) {
		if (deploymentId == null) {
			throw new RuntimeException("deploymentId is null");
		}
		this.deploymentId = deploymentId;
		return this;
	}

	public SyncHistoryQuery deploymentIds(List<String> deploymentIds) {
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

	public Long getDatabaseId() {
		return databaseId;
	}

	public List<Long> getDatabaseIds() {
		return databaseIds;
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

			if ("databaseId".equals(sortColumn)) {
				orderBy = "E.DATABASEID_" + a_x;
			}

			if ("status".equals(sortColumn)) {
				orderBy = "E.STATUS_" + a_x;
			}

			if ("totalTime".equals(sortColumn)) {
				orderBy = "E.TOTALTIME_" + a_x;
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

	public Integer getStatusGreaterThanOrEqual() {
		return statusGreaterThanOrEqual;
	}

	public Integer getStatusLessThanOrEqual() {
		return statusLessThanOrEqual;
	}

	public Long getSyncId() {
		return syncId;
	}

	public List<Long> getSyncIds() {
		return syncIds;
	}

	public Integer getTotalTimeGreaterThanOrEqual() {
		return totalTimeGreaterThanOrEqual;
	}

	public Integer getTotalTimeLessThanOrEqual() {
		return totalTimeLessThanOrEqual;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("syncId", "SYNCID_");
		addColumn("deploymentId", "DEPLOYMENTID_");
		addColumn("databaseId", "DATABASEID_");
		addColumn("status", "STATUS_");
		addColumn("totalTime", "TOTALTIME_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setDatabaseId(Long databaseId) {
		this.databaseId = databaseId;
	}

	public void setDatabaseIds(List<Long> databaseIds) {
		this.databaseIds = databaseIds;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}

	public void setDeploymentIds(List<String> deploymentIds) {
		this.deploymentIds = deploymentIds;
	}

	public void setStatusGreaterThanOrEqual(Integer statusGreaterThanOrEqual) {
		this.statusGreaterThanOrEqual = statusGreaterThanOrEqual;
	}

	public void setStatusLessThanOrEqual(Integer statusLessThanOrEqual) {
		this.statusLessThanOrEqual = statusLessThanOrEqual;
	}

	public void setSyncId(Long syncId) {
		this.syncId = syncId;
	}

	public void setSyncIds(List<Long> syncIds) {
		this.syncIds = syncIds;
	}

	public void setTotalTimeGreaterThanOrEqual(Integer totalTimeGreaterThanOrEqual) {
		this.totalTimeGreaterThanOrEqual = totalTimeGreaterThanOrEqual;
	}

	public void setTotalTimeLessThanOrEqual(Integer totalTimeLessThanOrEqual) {
		this.totalTimeLessThanOrEqual = totalTimeLessThanOrEqual;
	}

	public SyncHistoryQuery syncId(Long syncId) {
		if (syncId == null) {
			throw new RuntimeException("syncId is null");
		}
		this.syncId = syncId;
		return this;
	}

	public SyncHistoryQuery syncIds(List<Long> syncIds) {
		if (syncIds == null) {
			throw new RuntimeException("syncIds is empty ");
		}
		this.syncIds = syncIds;
		return this;
	}

	public SyncHistoryQuery totalTimeGreaterThanOrEqual(Integer totalTimeGreaterThanOrEqual) {
		if (totalTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("totalTime is null");
		}
		this.totalTimeGreaterThanOrEqual = totalTimeGreaterThanOrEqual;
		return this;
	}

	public SyncHistoryQuery totalTimeLessThanOrEqual(Integer totalTimeLessThanOrEqual) {
		if (totalTimeLessThanOrEqual == null) {
			throw new RuntimeException("totalTime is null");
		}
		this.totalTimeLessThanOrEqual = totalTimeLessThanOrEqual;
		return this;
	}

}