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

public class SyncAppQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected Long nodeId;
	protected List<Long> nodeIds;
	protected String deploymentId;
	protected List<String> deploymentIds;
	protected String titleLike;
	protected String syncFlag;
	protected String type;
	protected String autoSyncFlag;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;
	protected String updateBy;
	protected Date updateTimeGreaterThanOrEqual;
	protected Date updateTimeLessThanOrEqual;

	public SyncAppQuery() {

	}

	public SyncAppQuery autoSyncFlag(String autoSyncFlag) {
		if (autoSyncFlag == null) {
			throw new RuntimeException("autoSyncFlag is null");
		}
		this.autoSyncFlag = autoSyncFlag;
		return this;
	}

	public SyncAppQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public SyncAppQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public SyncAppQuery deploymentId(String deploymentId) {
		if (deploymentId == null) {
			throw new RuntimeException("deploymentId is null");
		}
		this.deploymentId = deploymentId;
		return this;
	}

	public SyncAppQuery deploymentIds(List<String> deploymentIds) {
		if (deploymentIds == null) {
			throw new RuntimeException("deploymentIds is empty ");
		}
		this.deploymentIds = deploymentIds;
		return this;
	}

	public String getAutoSyncFlag() {
		return autoSyncFlag;
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

	public Long getNodeId() {
		return nodeId;
	}

	public List<Long> getNodeIds() {
		return nodeIds;
	}

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("nodeId".equals(sortColumn)) {
				orderBy = "E.NODEID_" + a_x;
			}

			if ("deploymentId".equals(sortColumn)) {
				orderBy = "E.DEPLOYMENTID_" + a_x;
			}

			if ("title".equals(sortColumn)) {
				orderBy = "E.TITLE_" + a_x;
			}

			if ("srcDatabaseId".equals(sortColumn)) {
				orderBy = "E.SRCDATABASEID_" + a_x;
			}

			if ("syncFlag".equals(sortColumn)) {
				orderBy = "E.SYNCFLAG_" + a_x;
			}

			if ("targetDatabaseIds".equals(sortColumn)) {
				orderBy = "E.TARGETDATABASEIDS_" + a_x;
			}

			if ("type".equals(sortColumn)) {
				orderBy = "E.TYPE_" + a_x;
			}

			if ("autoSyncFlag".equals(sortColumn)) {
				orderBy = "E.AUTOSYNCFLAG_" + a_x;
			}

			if ("interval".equals(sortColumn)) {
				orderBy = "E.INTERVAL_" + a_x;
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

	public String getSyncFlag() {
		return syncFlag;
	}

	public String getTitleLike() {
		if (titleLike != null && titleLike.trim().length() > 0) {
			if (!titleLike.startsWith("%")) {
				titleLike = "%" + titleLike;
			}
			if (!titleLike.endsWith("%")) {
				titleLike = titleLike + "%";
			}
		}
		return titleLike;
	}

	public String getType() {
		return type;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public Date getUpdateTimeGreaterThanOrEqual() {
		return updateTimeGreaterThanOrEqual;
	}

	public Date getUpdateTimeLessThanOrEqual() {
		return updateTimeLessThanOrEqual;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("nodeId", "NODEID_");
		addColumn("deploymentId", "DEPLOYMENTID_");
		addColumn("title", "TITLE_");
		addColumn("srcDatabaseId", "SRCDATABASEID_");
		addColumn("syncFlag", "SYNCFLAG_");
		addColumn("targetDatabaseIds", "TARGETDATABASEIDS_");
		addColumn("type", "TYPE_");
		addColumn("autoSyncFlag", "AUTOSYNCFLAG_");
		addColumn("interval", "INTERVAL_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
		addColumn("updateBy", "UPDATEBY_");
		addColumn("updateTime", "UPDATETIME_");
	}

	public SyncAppQuery nodeId(Long nodeId) {
		if (nodeId == null) {
			throw new RuntimeException("nodeId is null");
		}
		this.nodeId = nodeId;
		return this;
	}

	public SyncAppQuery nodeIds(List<Long> nodeIds) {
		if (nodeIds == null) {
			throw new RuntimeException("nodeIds is empty ");
		}
		this.nodeIds = nodeIds;
		return this;
	}

	public void setAutoSyncFlag(String autoSyncFlag) {
		this.autoSyncFlag = autoSyncFlag;
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

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public void setNodeIds(List<Long> nodeIds) {
		this.nodeIds = nodeIds;
	}

	public void setSyncFlag(String syncFlag) {
		this.syncFlag = syncFlag;
	}

	public void setTitleLike(String titleLike) {
		this.titleLike = titleLike;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public void setUpdateTimeGreaterThanOrEqual(Date updateTimeGreaterThanOrEqual) {
		this.updateTimeGreaterThanOrEqual = updateTimeGreaterThanOrEqual;
	}

	public void setUpdateTimeLessThanOrEqual(Date updateTimeLessThanOrEqual) {
		this.updateTimeLessThanOrEqual = updateTimeLessThanOrEqual;
	}

	public SyncAppQuery syncFlag(String syncFlag) {
		if (syncFlag == null) {
			throw new RuntimeException("syncFlag is null");
		}
		this.syncFlag = syncFlag;
		return this;
	}

	public SyncAppQuery titleLike(String titleLike) {
		if (titleLike == null) {
			throw new RuntimeException("title is null");
		}
		this.titleLike = titleLike;
		return this;
	}

	public SyncAppQuery type(String type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		this.type = type;
		return this;
	}

	public SyncAppQuery updateBy(String updateBy) {
		if (updateBy == null) {
			throw new RuntimeException("updateBy is null");
		}
		this.updateBy = updateBy;
		return this;
	}

	public SyncAppQuery updateTimeGreaterThanOrEqual(Date updateTimeGreaterThanOrEqual) {
		if (updateTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("updateTime is null");
		}
		this.updateTimeGreaterThanOrEqual = updateTimeGreaterThanOrEqual;
		return this;
	}

	public SyncAppQuery updateTimeLessThanOrEqual(Date updateTimeLessThanOrEqual) {
		if (updateTimeLessThanOrEqual == null) {
			throw new RuntimeException("updateTime is null");
		}
		this.updateTimeLessThanOrEqual = updateTimeLessThanOrEqual;
		return this;
	}

}