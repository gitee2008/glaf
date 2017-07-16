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

public class AggregationDefinitionQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected String titleLike;
	protected String name;
	protected String nameLike;
	protected String targetTableName;
	protected String targetTableNameLike;
	protected String type;
	protected String typeLike;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;
	protected Date updateTimeGreaterThanOrEqual;
	protected Date updateTimeLessThanOrEqual;

	public AggregationDefinitionQuery() {

	}

	public AggregationDefinitionQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public AggregationDefinitionQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
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

			if ("sql".equals(sortColumn)) {
				orderBy = "E.SQL_" + a_x;
			}

			if ("precision".equals(sortColumn)) {
				orderBy = "E.PRECISION_" + a_x;
			}

			if ("targetTableName".equals(sortColumn)) {
				orderBy = "E.TARGETTABLENAME_" + a_x;
			}

			if ("type".equals(sortColumn)) {
				orderBy = "E.TYPE_" + a_x;
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

	public String getTypeLike() {
		if (typeLike != null && typeLike.trim().length() > 0) {
			if (!typeLike.startsWith("%")) {
				typeLike = "%" + typeLike;
			}
			if (!typeLike.endsWith("%")) {
				typeLike = typeLike + "%";
			}
		}
		return typeLike;
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
		addColumn("sql", "SQL_");
		addColumn("precision", "PRECISION_");
		addColumn("targetTableName", "TARGETTABLENAME_");
		addColumn("type", "TYPE_");
		addColumn("sortNo", "SORTNO_");
		addColumn("locked", "LOCKED_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
		addColumn("updateBy", "UPDATEBY_");
		addColumn("updateTime", "UPDATETIME_");
	}

	public AggregationDefinitionQuery name(String name) {
		if (name == null) {
			throw new RuntimeException("name is null");
		}
		this.name = name;
		return this;
	}

	public AggregationDefinitionQuery nameLike(String nameLike) {
		if (nameLike == null) {
			throw new RuntimeException("name is null");
		}
		this.nameLike = nameLike;
		return this;
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNameLike(String nameLike) {
		this.nameLike = nameLike;
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

	public void setTypeLike(String typeLike) {
		this.typeLike = typeLike;
	}

	public void setUpdateTimeGreaterThanOrEqual(Date updateTimeGreaterThanOrEqual) {
		this.updateTimeGreaterThanOrEqual = updateTimeGreaterThanOrEqual;
	}

	public void setUpdateTimeLessThanOrEqual(Date updateTimeLessThanOrEqual) {
		this.updateTimeLessThanOrEqual = updateTimeLessThanOrEqual;
	}

	public AggregationDefinitionQuery targetTableName(String targetTableName) {
		if (targetTableName == null) {
			throw new RuntimeException("targetTableName is null");
		}
		this.targetTableName = targetTableName;
		return this;
	}

	public AggregationDefinitionQuery targetTableNameLike(String targetTableNameLike) {
		if (targetTableNameLike == null) {
			throw new RuntimeException("targetTableName is null");
		}
		this.targetTableNameLike = targetTableNameLike;
		return this;
	}

	public AggregationDefinitionQuery type(String type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		this.type = type;
		return this;
	}

	public AggregationDefinitionQuery typeLike(String typeLike) {
		if (typeLike == null) {
			throw new RuntimeException("type is null");
		}
		this.typeLike = typeLike;
		return this;
	}

	public AggregationDefinitionQuery updateTimeGreaterThanOrEqual(Date updateTimeGreaterThanOrEqual) {
		if (updateTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("updateTime is null");
		}
		this.updateTimeGreaterThanOrEqual = updateTimeGreaterThanOrEqual;
		return this;
	}

	public AggregationDefinitionQuery updateTimeLessThanOrEqual(Date updateTimeLessThanOrEqual) {
		if (updateTimeLessThanOrEqual == null) {
			throw new RuntimeException("updateTime is null");
		}
		this.updateTimeLessThanOrEqual = updateTimeLessThanOrEqual;
		return this;
	}

}