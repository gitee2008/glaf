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

import com.glaf.core.query.DataQuery;

public class TableDataItemQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected String titleLike;
	protected String descriptionLike;
	protected String tableName;
	protected String tableNameLike;
	protected String nameColumn;
	protected String nameColumnLike;
	protected String valueColumn;
	protected String valueColumnLike;
	protected String type;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	public TableDataItemQuery() {

	}

	public TableDataItemQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public TableDataItemQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public TableDataItemQuery descriptionLike(String descriptionLike) {
		if (descriptionLike == null) {
			throw new RuntimeException("description is null");
		}
		this.descriptionLike = descriptionLike;
		return this;
	}

	public Date getCreateTimeGreaterThanOrEqual() {
		return createTimeGreaterThanOrEqual;
	}

	public Date getCreateTimeLessThanOrEqual() {
		return createTimeLessThanOrEqual;
	}

	public String getDescriptionLike() {
		if (descriptionLike != null && descriptionLike.trim().length() > 0) {
			if (!descriptionLike.startsWith("%")) {
				descriptionLike = "%" + descriptionLike;
			}
			if (!descriptionLike.endsWith("%")) {
				descriptionLike = descriptionLike + "%";
			}
		}
		return descriptionLike;
	}

	public String getNameColumn() {
		return nameColumn;
	}

	public String getNameColumnLike() {
		if (nameColumnLike != null && nameColumnLike.trim().length() > 0) {
			if (!nameColumnLike.startsWith("%")) {
				nameColumnLike = "%" + nameColumnLike;
			}
			if (!nameColumnLike.endsWith("%")) {
				nameColumnLike = nameColumnLike + "%";
			}
		}
		return nameColumnLike;
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

			if ("description".equals(sortColumn)) {
				orderBy = "E.DESCRIPTION_" + a_x;
			}

			if ("tableName".equals(sortColumn)) {
				orderBy = "E.TABLENAME_" + a_x;
			}

			if ("nameColumn".equals(sortColumn)) {
				orderBy = "E.NAMECOLUMN_" + a_x;
			}

			if ("valueColumn".equals(sortColumn)) {
				orderBy = "E.VALUECOLUMN_" + a_x;
			}

			if ("filterFlag".equals(sortColumn)) {
				orderBy = "E.FILTERFLAG_" + a_x;
			}

			if ("type".equals(sortColumn)) {
				orderBy = "E.TYPE_" + a_x;
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

	public String getTableName() {
		return tableName;
	}

	public String getTableNameLike() {
		if (tableNameLike != null && tableNameLike.trim().length() > 0) {
			if (!tableNameLike.startsWith("%")) {
				tableNameLike = "%" + tableNameLike;
			}
			if (!tableNameLike.endsWith("%")) {
				tableNameLike = tableNameLike + "%";
			}
		}
		return tableNameLike;
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

	public String getValueColumn() {
		return valueColumn;
	}

	public String getValueColumnLike() {
		if (valueColumnLike != null && valueColumnLike.trim().length() > 0) {
			if (!valueColumnLike.startsWith("%")) {
				valueColumnLike = "%" + valueColumnLike;
			}
			if (!valueColumnLike.endsWith("%")) {
				valueColumnLike = valueColumnLike + "%";
			}
		}
		return valueColumnLike;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("title", "TITLE_");
		addColumn("description", "DESCRIPTION_");
		addColumn("tableName", "TABLENAME_");
		addColumn("nameColumn", "NAMECOLUMN_");
		addColumn("valueColumn", "VALUECOLUMN_");
		addColumn("filterFlag", "FILTERFLAG_");
		addColumn("type", "TYPE_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
		addColumn("updateBy", "UPDATEBY_");
		addColumn("updateTime", "UPDATETIME_");
	}

	public TableDataItemQuery nameColumn(String nameColumn) {
		if (nameColumn == null) {
			throw new RuntimeException("nameColumn is null");
		}
		this.nameColumn = nameColumn;
		return this;
	}

	public TableDataItemQuery nameColumnLike(String nameColumnLike) {
		if (nameColumnLike == null) {
			throw new RuntimeException("nameColumn is null");
		}
		this.nameColumnLike = nameColumnLike;
		return this;
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setDescriptionLike(String descriptionLike) {
		this.descriptionLike = descriptionLike;
	}

	public void setNameColumn(String nameColumn) {
		this.nameColumn = nameColumn;
	}

	public void setNameColumnLike(String nameColumnLike) {
		this.nameColumnLike = nameColumnLike;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setTableNameLike(String tableNameLike) {
		this.tableNameLike = tableNameLike;
	}

	public void setTitleLike(String titleLike) {
		this.titleLike = titleLike;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setValueColumn(String valueColumn) {
		this.valueColumn = valueColumn;
	}

	public void setValueColumnLike(String valueColumnLike) {
		this.valueColumnLike = valueColumnLike;
	}

	public TableDataItemQuery tableName(String tableName) {
		if (tableName == null) {
			throw new RuntimeException("tableName is null");
		}
		this.tableName = tableName;
		return this;
	}

	public TableDataItemQuery tableNameLike(String tableNameLike) {
		if (tableNameLike == null) {
			throw new RuntimeException("tableName is null");
		}
		this.tableNameLike = tableNameLike;
		return this;
	}

	public TableDataItemQuery titleLike(String titleLike) {
		if (titleLike == null) {
			throw new RuntimeException("title is null");
		}
		this.titleLike = titleLike;
		return this;
	}

	public TableDataItemQuery type(String type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		this.type = type;
		return this;
	}

	public TableDataItemQuery valueColumn(String valueColumn) {
		if (valueColumn == null) {
			throw new RuntimeException("valueColumn is null");
		}
		this.valueColumn = valueColumn;
		return this;
	}

	public TableDataItemQuery valueColumnLike(String valueColumnLike) {
		if (valueColumnLike == null) {
			throw new RuntimeException("valueColumn is null");
		}
		this.valueColumnLike = valueColumnLike;
		return this;
	}

}