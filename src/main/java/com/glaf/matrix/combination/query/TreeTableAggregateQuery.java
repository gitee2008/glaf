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

public class TreeTableAggregateQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected String name;
	protected String nameLike;
	protected String titleLike;
	protected String type;
	protected String sourceTableName;
	protected String sourceTableNameLike;
	protected String targetTableName;
	protected String targetTableNameLike;
	protected String aggregateFlag;
	protected String scheduleFlag;
	protected Integer privateFlag;
	protected Integer syncStatus;
	protected Date syncTimeGreaterThanOrEqual;
	protected Date syncTimeLessThanOrEqual;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;
	protected Date updateTimeGreaterThanOrEqual;
	protected Date updateTimeLessThanOrEqual;

	public TreeTableAggregateQuery() {

	}

	public TreeTableAggregateQuery aggregateFlag(String aggregateFlag) {
		if (aggregateFlag == null) {
			throw new RuntimeException("aggregateFlag is null");
		}
		this.aggregateFlag = aggregateFlag;
		return this;
	}

	public TreeTableAggregateQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public TreeTableAggregateQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public TreeTableAggregateQuery deleteFlag(Integer deleteFlag) {
		if (deleteFlag == null) {
			throw new RuntimeException("deleteFlag is null");
		}
		this.deleteFlag = deleteFlag;
		return this;
	}

	public String getAggregateFlag() {
		return aggregateFlag;
	}

	public String getCreateBy() {
		return createBy;
	}

	public Date getCreateTimeGreaterThanOrEqual() {
		return createTimeGreaterThanOrEqual;
	}

	public Date getCreateTimeLessThanOrEqual() {
		return createTimeLessThanOrEqual;
	}

	public Integer getDeleteFlag() {
		return deleteFlag;
	}

	public Integer getLocked() {
		return locked;
	}

	public String getName() {
		return name;
	}

	public String getNameLike() {
		if (nameLike != null && nameLike.trim().length() > 0) {
			if (!nameLike.startsWith("%")) {
				nameLike = "%" + nameLike;
			}
			if (!nameLike.endsWith("%")) {
				nameLike = nameLike + "%";
			}
		}
		return nameLike;
	}

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("name".equals(sortColumn)) {
				orderBy = "E.NAME_" + a_x;
			}

			if ("title".equals(sortColumn)) {
				orderBy = "E.TITLE_" + a_x;
			}

			if ("type".equals(sortColumn)) {
				orderBy = "E.TYPE_" + a_x;
			}

			if ("sourceTableName".equals(sortColumn)) {
				orderBy = "E.SOURCETABLENAME_" + a_x;
			}

			if ("sourceIdColumn".equals(sortColumn)) {
				orderBy = "E.SOURCEIDCOLUMN_" + a_x;
			}

			if ("sourceParentIdColumn".equals(sortColumn)) {
				orderBy = "E.SOURCEPARENTIDCOLUMN_" + a_x;
			}

			if ("sourceTreeIdColumn".equals(sortColumn)) {
				orderBy = "E.SOURCETREEIDCOLUMN_" + a_x;
			}

			if ("sourceTextColumn".equals(sortColumn)) {
				orderBy = "E.SOURCETEXTCOLUMN_" + a_x;
			}

			if ("databaseIds".equals(sortColumn)) {
				orderBy = "E.DATABASEIDS_" + a_x;
			}

			if ("targetTableName".equals(sortColumn)) {
				orderBy = "E.TARGETTABLENAME_" + a_x;
			}

			if ("targetDatabaseId".equals(sortColumn)) {
				orderBy = "E.TARGETDATABASEID_" + a_x;
			}

			if ("createTableFlag".equals(sortColumn)) {
				orderBy = "E.CREATETABLEFLAG_" + a_x;
			}

			if ("aggregateFlag".equals(sortColumn)) {
				orderBy = "E.AGGREGATEFLAG_" + a_x;
			}

			if ("deleteFetch".equals(sortColumn)) {
				orderBy = "E.DELETEFETCH_" + a_x;
			}

			if ("syncStatus".equals(sortColumn)) {
				orderBy = "E.SYNCSTATUS_" + a_x;
			}

			if ("syncTime".equals(sortColumn)) {
				orderBy = "E.SYNCTIME_" + a_x;
			}

			if ("sortNo".equals(sortColumn)) {
				orderBy = "E.SORTNO_" + a_x;
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

			if ("updateBy".equals(sortColumn)) {
				orderBy = "E.UPDATEBY_" + a_x;
			}

			if ("updateTime".equals(sortColumn)) {
				orderBy = "E.UPDATETIME_" + a_x;
			}

		}
		return orderBy;
	}

	public Integer getPrivateFlag() {
		return privateFlag;
	}

	public String getScheduleFlag() {
		return scheduleFlag;
	}

	public String getSourceTableName() {
		return sourceTableName;
	}

	public String getSourceTableNameLike() {
		if (sourceTableNameLike != null && sourceTableNameLike.trim().length() > 0) {
			if (!sourceTableNameLike.startsWith("%")) {
				sourceTableNameLike = "%" + sourceTableNameLike;
			}
			if (!sourceTableNameLike.endsWith("%")) {
				sourceTableNameLike = sourceTableNameLike + "%";
			}
		}
		return sourceTableNameLike;
	}

	public Integer getSyncStatus() {
		return syncStatus;
	}

	public Date getSyncTimeGreaterThanOrEqual() {
		return syncTimeGreaterThanOrEqual;
	}

	public Date getSyncTimeLessThanOrEqual() {
		return syncTimeLessThanOrEqual;
	}

	public String getTargetTableName() {
		return targetTableName;
	}

	public String getTargetTableNameLike() {
		if (targetTableNameLike != null && targetTableNameLike.trim().length() > 0) {
			if (!targetTableNameLike.startsWith("%")) {
				targetTableNameLike = "%" + targetTableNameLike;
			}
			if (!targetTableNameLike.endsWith("%")) {
				targetTableNameLike = targetTableNameLike + "%";
			}
		}
		return targetTableNameLike;
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
		addColumn("name", "NAME_");
		addColumn("title", "TITLE_");
		addColumn("type", "TYPE_");
		addColumn("sourceTableName", "SOURCETABLENAME_");
		addColumn("sourceIdColumn", "SOURCEIDCOLUMN_");
		addColumn("sourceParentIdColumn", "SOURCEPARENTIDCOLUMN_");
		addColumn("sourceTreeIdColumn", "SOURCETREEIDCOLUMN_");
		addColumn("sourceTextColumn", "SOURCETEXTCOLUMN_");
		addColumn("databaseIds", "DATABASEIDS_");
		addColumn("targetTableName", "TARGETTABLENAME_");
		addColumn("targetDatabaseId", "TARGETDATABASEID_");
		addColumn("createTableFlag", "CREATETABLEFLAG_");
		addColumn("aggregateFlag", "AGGREGATEFLAG_");
		addColumn("deleteFetch", "DELETEFETCH_");
		addColumn("syncStatus", "SYNCSTATUS_");
		addColumn("syncTime", "SYNCTIME_");
		addColumn("sortNo", "SORTNO_");
		addColumn("locked", "LOCKED_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
		addColumn("updateBy", "UPDATEBY_");
		addColumn("updateTime", "UPDATETIME_");
	}

	public TreeTableAggregateQuery name(String name) {
		if (name == null) {
			throw new RuntimeException("name is null");
		}
		this.name = name;
		return this;
	}

	public TreeTableAggregateQuery nameLike(String nameLike) {
		if (nameLike == null) {
			throw new RuntimeException("name is null");
		}
		this.nameLike = nameLike;
		return this;
	}

	public TreeTableAggregateQuery privateFlag(Integer privateFlag) {
		if (privateFlag == null) {
			throw new RuntimeException("privateFlag is null");
		}
		this.privateFlag = privateFlag;
		return this;
	}

	public void setAggregateFlag(String aggregateFlag) {
		this.aggregateFlag = aggregateFlag;
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNameLike(String nameLike) {
		this.nameLike = nameLike;
	}

	public void setPrivateFlag(Integer privateFlag) {
		this.privateFlag = privateFlag;
	}

	public void setScheduleFlag(String scheduleFlag) {
		this.scheduleFlag = scheduleFlag;
	}

	public void setSourceTableName(String sourceTableName) {
		this.sourceTableName = sourceTableName;
	}

	public void setSourceTableNameLike(String sourceTableNameLike) {
		this.sourceTableNameLike = sourceTableNameLike;
	}

	public void setSyncStatus(Integer syncStatus) {
		this.syncStatus = syncStatus;
	}

	public void setSyncTimeGreaterThanOrEqual(Date syncTimeGreaterThanOrEqual) {
		this.syncTimeGreaterThanOrEqual = syncTimeGreaterThanOrEqual;
	}

	public void setSyncTimeLessThanOrEqual(Date syncTimeLessThanOrEqual) {
		this.syncTimeLessThanOrEqual = syncTimeLessThanOrEqual;
	}

	public void setTargetTableName(String targetTableName) {
		this.targetTableName = targetTableName;
	}

	public void setTargetTableNameLike(String targetTableNameLike) {
		this.targetTableNameLike = targetTableNameLike;
	}

	public void setTitleLike(String titleLike) {
		this.titleLike = titleLike;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUpdateTimeGreaterThanOrEqual(Date updateTimeGreaterThanOrEqual) {
		this.updateTimeGreaterThanOrEqual = updateTimeGreaterThanOrEqual;
	}

	public void setUpdateTimeLessThanOrEqual(Date updateTimeLessThanOrEqual) {
		this.updateTimeLessThanOrEqual = updateTimeLessThanOrEqual;
	}

	public TreeTableAggregateQuery sourceTableName(String sourceTableName) {
		if (sourceTableName == null) {
			throw new RuntimeException("sourceTableName is null");
		}
		this.sourceTableName = sourceTableName;
		return this;
	}

	public TreeTableAggregateQuery sourceTableNameLike(String sourceTableNameLike) {
		if (sourceTableNameLike == null) {
			throw new RuntimeException("sourceTableName is null");
		}
		this.sourceTableNameLike = sourceTableNameLike;
		return this;
	}

	public TreeTableAggregateQuery syncStatus(Integer syncStatus) {
		if (syncStatus == null) {
			throw new RuntimeException("syncStatus is null");
		}
		this.syncStatus = syncStatus;
		return this;
	}

	public TreeTableAggregateQuery syncTimeGreaterThanOrEqual(Date syncTimeGreaterThanOrEqual) {
		if (syncTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("syncTime is null");
		}
		this.syncTimeGreaterThanOrEqual = syncTimeGreaterThanOrEqual;
		return this;
	}

	public TreeTableAggregateQuery syncTimeLessThanOrEqual(Date syncTimeLessThanOrEqual) {
		if (syncTimeLessThanOrEqual == null) {
			throw new RuntimeException("syncTime is null");
		}
		this.syncTimeLessThanOrEqual = syncTimeLessThanOrEqual;
		return this;
	}

	public TreeTableAggregateQuery targetTableName(String targetTableName) {
		if (targetTableName == null) {
			throw new RuntimeException("targetTableName is null");
		}
		this.targetTableName = targetTableName;
		return this;
	}

	public TreeTableAggregateQuery targetTableNameLike(String targetTableNameLike) {
		if (targetTableNameLike == null) {
			throw new RuntimeException("targetTableName is null");
		}
		this.targetTableNameLike = targetTableNameLike;
		return this;
	}

	public TreeTableAggregateQuery titleLike(String titleLike) {
		if (titleLike == null) {
			throw new RuntimeException("title is null");
		}
		this.titleLike = titleLike;
		return this;
	}

	public TreeTableAggregateQuery type(String type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		this.type = type;
		return this;
	}

	public TreeTableAggregateQuery updateTimeGreaterThanOrEqual(Date updateTimeGreaterThanOrEqual) {
		if (updateTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("updateTime is null");
		}
		this.updateTimeGreaterThanOrEqual = updateTimeGreaterThanOrEqual;
		return this;
	}

	public TreeTableAggregateQuery updateTimeLessThanOrEqual(Date updateTimeLessThanOrEqual) {
		if (updateTimeLessThanOrEqual == null) {
			throw new RuntimeException("updateTime is null");
		}
		this.updateTimeLessThanOrEqual = updateTimeLessThanOrEqual;
		return this;
	}

}