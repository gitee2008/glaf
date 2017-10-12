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

import java.util.*;
import com.glaf.core.query.DataQuery;

public class StorageHistoryQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected Long storageId;
	protected List<Long> storageIds;
	protected String deploymentId;
	protected List<String> deploymentIds;
	protected String sysFlag;
	protected Integer version;
	protected Integer versionGreaterThanOrEqual;
	protected Integer versionLessThanOrEqual;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	public StorageHistoryQuery() {

	}

	public StorageHistoryQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public StorageHistoryQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public StorageHistoryQuery deploymentId(String deploymentId) {
		if (deploymentId == null) {
			throw new RuntimeException("deploymentId is null");
		}
		this.deploymentId = deploymentId;
		return this;
	}

	public StorageHistoryQuery deploymentIds(List<String> deploymentIds) {
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

			if ("storageId".equals(sortColumn)) {
				orderBy = "E.STORAGEID_" + a_x;
			}

			if ("deploymentId".equals(sortColumn)) {
				orderBy = "E.DEPLOYMENTID_" + a_x;
			}

			if ("path".equals(sortColumn)) {
				orderBy = "E.PATH_" + a_x;
			}

			if ("sysFlag".equals(sortColumn)) {
				orderBy = "E.SYSFLAG_" + a_x;
			}

			if ("version".equals(sortColumn)) {
				orderBy = "E.VERSION_" + a_x;
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

	public Long getStorageId() {
		return storageId;
	}

	public List<Long> getStorageIds() {
		return storageIds;
	}

	public String getSysFlag() {
		return sysFlag;
	}

	public Integer getVersion() {
		return version;
	}

	public Integer getVersionGreaterThanOrEqual() {
		return versionGreaterThanOrEqual;
	}

	public Integer getVersionLessThanOrEqual() {
		return versionLessThanOrEqual;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("storageId", "STORAGEID_");
		addColumn("deploymentId", "DEPLOYMENTID_");
		addColumn("path", "PATH_");
		addColumn("sysFlag", "SYSFLAG_");
		addColumn("version", "VERSION_");
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

	public void setStorageId(Long storageId) {
		this.storageId = storageId;
	}

	public void setStorageIds(List<Long> storageIds) {
		this.storageIds = storageIds;
	}

	public void setSysFlag(String sysFlag) {
		this.sysFlag = sysFlag;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public void setVersionGreaterThanOrEqual(Integer versionGreaterThanOrEqual) {
		this.versionGreaterThanOrEqual = versionGreaterThanOrEqual;
	}

	public void setVersionLessThanOrEqual(Integer versionLessThanOrEqual) {
		this.versionLessThanOrEqual = versionLessThanOrEqual;
	}

	public StorageHistoryQuery storageId(Long storageId) {
		if (storageId == null) {
			throw new RuntimeException("storageId is null");
		}
		this.storageId = storageId;
		return this;
	}

	public StorageHistoryQuery storageIds(List<Long> storageIds) {
		if (storageIds == null) {
			throw new RuntimeException("storageIds is empty ");
		}
		this.storageIds = storageIds;
		return this;
	}

	public StorageHistoryQuery sysFlag(String sysFlag) {
		if (sysFlag == null) {
			throw new RuntimeException("sysFlag is null");
		}
		this.sysFlag = sysFlag;
		return this;
	}

	public StorageHistoryQuery version(Integer version) {
		if (version == null) {
			throw new RuntimeException("version is null");
		}
		this.version = version;
		return this;
	}

	public StorageHistoryQuery versionGreaterThanOrEqual(Integer versionGreaterThanOrEqual) {
		if (versionGreaterThanOrEqual == null) {
			throw new RuntimeException("version is null");
		}
		this.versionGreaterThanOrEqual = versionGreaterThanOrEqual;
		return this;
	}

	public StorageHistoryQuery versionLessThanOrEqual(Integer versionLessThanOrEqual) {
		if (versionLessThanOrEqual == null) {
			throw new RuntimeException("version is null");
		}
		this.versionLessThanOrEqual = versionLessThanOrEqual;
		return this;
	}

}