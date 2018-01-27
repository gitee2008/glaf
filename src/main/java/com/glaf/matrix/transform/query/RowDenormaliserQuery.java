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

package com.glaf.matrix.transform.query;

import java.util.*;
import com.glaf.core.query.DataQuery;

public class RowDenormaliserQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected String titleLike;
	protected Integer transformStatus;
	protected Date transformTimeGreaterThanOrEqual;
	protected Date transformTimeLessThanOrEqual;
	protected String transformFlag;
	protected String scheduleFlag;
	protected String targetTableName;
	protected String targetTableNameLike;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	public RowDenormaliserQuery() {

	}

	public RowDenormaliserQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public RowDenormaliserQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
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

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("title".equals(sortColumn)) {
				orderBy = "E.TITLE_" + a_x;
			}

			if ("databaseIds".equals(sortColumn)) {
				orderBy = "E.DATABASEIDS_" + a_x;
			}

			if ("primaryKey".equals(sortColumn)) {
				orderBy = "E.PRIMARYKEY_" + a_x;
			}

			if ("transformColumn".equals(sortColumn)) {
				orderBy = "E.TRANSFORMCOLUMN_" + a_x;
			}

			if ("sort".equals(sortColumn)) {
				orderBy = "E.SORT_" + a_x;
			}

			if ("transformStatus".equals(sortColumn)) {
				orderBy = "E.TRANSFORMSTATUS_" + a_x;
			}

			if ("transformTime".equals(sortColumn)) {
				orderBy = "E.TRANSFORMTIME_" + a_x;
			}

			if ("transformFlag".equals(sortColumn)) {
				orderBy = "E.TRANSFORMFLAG_" + a_x;
			}

			if ("targetTableName".equals(sortColumn)) {
				orderBy = "E.TARGETTABLENAME_" + a_x;
			}

			if ("targetColumn".equals(sortColumn)) {
				orderBy = "E.TARGETCOLUMN_" + a_x;
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

	public String getScheduleFlag() {
		return scheduleFlag;
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

	public String getTransformFlag() {
		return transformFlag;
	}

	public Integer getTransformStatus() {
		return transformStatus;
	}

	public Date getTransformTimeGreaterThanOrEqual() {
		return transformTimeGreaterThanOrEqual;
	}

	public Date getTransformTimeLessThanOrEqual() {
		return transformTimeLessThanOrEqual;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("title", "TITLE_");
		addColumn("databaseIds", "DATABASEIDS_");
		addColumn("primaryKey", "PRIMARYKEY_");
		addColumn("transformColumn", "TRANSFORMCOLUMN_");
		addColumn("sort", "SORT_");
		addColumn("transformStatus", "TRANSFORMSTATUS_");
		addColumn("transformTime", "TRANSFORMTIME_");
		addColumn("transformFlag", "TRANSFORMFLAG_");
		addColumn("targetTableName", "TARGETTABLENAME_");
		addColumn("targetColumn", "TARGETCOLUMN_");
		addColumn("locked", "LOCKED_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
		addColumn("updateBy", "UPDATEBY_");
		addColumn("updateTime", "UPDATETIME_");
	}

	public RowDenormaliserQuery scheduleFlag(String scheduleFlag) {
		if (scheduleFlag == null) {
			throw new RuntimeException("scheduleFlag is null");
		}
		this.scheduleFlag = scheduleFlag;
		return this;
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setScheduleFlag(String scheduleFlag) {
		this.scheduleFlag = scheduleFlag;
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

	public void setTransformFlag(String transformFlag) {
		this.transformFlag = transformFlag;
	}

	public void setTransformStatus(Integer transformStatus) {
		this.transformStatus = transformStatus;
	}

	public void setTransformTimeGreaterThanOrEqual(Date transformTimeGreaterThanOrEqual) {
		this.transformTimeGreaterThanOrEqual = transformTimeGreaterThanOrEqual;
	}

	public void setTransformTimeLessThanOrEqual(Date transformTimeLessThanOrEqual) {
		this.transformTimeLessThanOrEqual = transformTimeLessThanOrEqual;
	}

	public RowDenormaliserQuery targetTableName(String targetTableName) {
		if (targetTableName == null) {
			throw new RuntimeException("targetTableName is null");
		}
		this.targetTableName = targetTableName;
		return this;
	}

	public RowDenormaliserQuery targetTableNameLike(String targetTableNameLike) {
		if (targetTableNameLike == null) {
			throw new RuntimeException("targetTableName is null");
		}
		this.targetTableNameLike = targetTableNameLike;
		return this;
	}

	public RowDenormaliserQuery titleLike(String titleLike) {
		if (titleLike == null) {
			throw new RuntimeException("title is null");
		}
		this.titleLike = titleLike;
		return this;
	}

	public RowDenormaliserQuery transformFlag(String transformFlag) {
		if (transformFlag == null) {
			throw new RuntimeException("transformFlag is null");
		}
		this.transformFlag = transformFlag;
		return this;
	}

	public RowDenormaliserQuery transformStatus(Integer transformStatus) {
		if (transformStatus == null) {
			throw new RuntimeException("transformStatus is null");
		}
		this.transformStatus = transformStatus;
		return this;
	}

	public RowDenormaliserQuery transformTimeGreaterThanOrEqual(Date transformTimeGreaterThanOrEqual) {
		if (transformTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("transformTime is null");
		}
		this.transformTimeGreaterThanOrEqual = transformTimeGreaterThanOrEqual;
		return this;
	}

	public RowDenormaliserQuery transformTimeLessThanOrEqual(Date transformTimeLessThanOrEqual) {
		if (transformTimeLessThanOrEqual == null) {
			throw new RuntimeException("transformTime is null");
		}
		this.transformTimeLessThanOrEqual = transformTimeLessThanOrEqual;
		return this;
	}

}