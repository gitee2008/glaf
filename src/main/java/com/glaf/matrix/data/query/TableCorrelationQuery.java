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

public class TableCorrelationQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected String masterTableId;
	protected List<String> masterTableIds;
	protected String masterTableName;
	protected List<String> masterTableNames;
	protected String slaveTableId;
	protected List<String> slaveTableIds;
	protected String slaveTableName;
	protected List<String> slaveTableNames;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	public TableCorrelationQuery() {

	}

	public TableCorrelationQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public TableCorrelationQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
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

	public String getMasterTableId() {
		return masterTableId;
	}

	public List<String> getMasterTableIds() {
		return masterTableIds;
	}

	public String getMasterTableName() {
		return masterTableName;
	}

	public List<String> getMasterTableNames() {
		return masterTableNames;
	}

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("masterTableId".equals(sortColumn)) {
				orderBy = "E.MASTERTABLEID_" + a_x;
			}

			if ("masterTableName".equals(sortColumn)) {
				orderBy = "E.MASTERTABLENAME_" + a_x;
			}

			if ("slaveTableId".equals(sortColumn)) {
				orderBy = "E.SLAVETABLEID_" + a_x;
			}

			if ("slaveTableName".equals(sortColumn)) {
				orderBy = "E.SLAVETABLENAME_" + a_x;
			}

			if ("insertCascade".equals(sortColumn)) {
				orderBy = "E.INSERTCASCADE_" + a_x;
			}

			if ("deleteCascade".equals(sortColumn)) {
				orderBy = "E.DELETECASCADE_" + a_x;
			}

			if ("updateCascade".equals(sortColumn)) {
				orderBy = "E.UPDATECASCADE_" + a_x;
			}

			if ("relationshipType".equals(sortColumn)) {
				orderBy = "E.RELATIONSHIPTYPE_" + a_x;
			}

			if ("sortNo".equals(sortColumn)) {
				orderBy = "E.SORTNO_" + a_x;
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

	public String getSlaveTableId() {
		return slaveTableId;
	}

	public List<String> getSlaveTableIds() {
		return slaveTableIds;
	}

	public String getSlaveTableName() {
		return slaveTableName;
	}

	public List<String> getSlaveTableNames() {
		return slaveTableNames;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("masterTableId", "MASTERTABLEID_");
		addColumn("masterTableName", "MASTERTABLENAME_");
		addColumn("slaveTableId", "SLAVETABLEID_");
		addColumn("slaveTableName", "SLAVETABLENAME_");
		addColumn("insertCascade", "INSERTCASCADE_");
		addColumn("deleteCascade", "DELETECASCADE_");
		addColumn("updateCascade", "UPDATECASCADE_");
		addColumn("relationshipType", "RELATIONSHIPTYPE_");
		addColumn("sortNo", "SORTNO_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
		addColumn("updateBy", "UPDATEBY_");
		addColumn("updateTime", "UPDATETIME_");
	}

	public TableCorrelationQuery masterTableId(String masterTableId) {
		if (masterTableId == null) {
			throw new RuntimeException("masterTableId is null");
		}
		this.masterTableId = masterTableId;
		return this;
	}

	public TableCorrelationQuery masterTableIds(List<String> masterTableIds) {
		if (masterTableIds == null) {
			throw new RuntimeException("masterTableIds is empty ");
		}
		this.masterTableIds = masterTableIds;
		return this;
	}

	public TableCorrelationQuery masterTableName(String masterTableName) {
		if (masterTableName == null) {
			throw new RuntimeException("masterTableName is null");
		}
		this.masterTableName = masterTableName;
		return this;
	}

	public TableCorrelationQuery masterTableNames(List<String> masterTableNames) {
		if (masterTableNames == null) {
			throw new RuntimeException("masterTableNames is empty ");
		}
		this.masterTableNames = masterTableNames;
		return this;
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setMasterTableId(String masterTableId) {
		this.masterTableId = masterTableId;
	}

	public void setMasterTableIds(List<String> masterTableIds) {
		this.masterTableIds = masterTableIds;
	}

	public void setMasterTableName(String masterTableName) {
		this.masterTableName = masterTableName;
	}

	public void setMasterTableNames(List<String> masterTableNames) {
		this.masterTableNames = masterTableNames;
	}

	public void setSlaveTableId(String slaveTableId) {
		this.slaveTableId = slaveTableId;
	}

	public void setSlaveTableIds(List<String> slaveTableIds) {
		this.slaveTableIds = slaveTableIds;
	}

	public void setSlaveTableName(String slaveTableName) {
		this.slaveTableName = slaveTableName;
	}

	public void setSlaveTableNames(List<String> slaveTableNames) {
		this.slaveTableNames = slaveTableNames;
	}

	public TableCorrelationQuery slaveTableId(String slaveTableId) {
		if (slaveTableId == null) {
			throw new RuntimeException("slaveTableId is null");
		}
		this.slaveTableId = slaveTableId;
		return this;
	}

	public TableCorrelationQuery slaveTableIds(List<String> slaveTableIds) {
		if (slaveTableIds == null) {
			throw new RuntimeException("slaveTableIds is empty ");
		}
		this.slaveTableIds = slaveTableIds;
		return this;
	}

	public TableCorrelationQuery slaveTableName(String slaveTableName) {
		if (slaveTableName == null) {
			throw new RuntimeException("slaveTableName is null");
		}
		this.slaveTableName = slaveTableName;
		return this;
	}

	public TableCorrelationQuery slaveTableNames(List<String> slaveTableNames) {
		if (slaveTableNames == null) {
			throw new RuntimeException("slaveTableNames is empty ");
		}
		this.slaveTableNames = slaveTableNames;
		return this;
	}

}