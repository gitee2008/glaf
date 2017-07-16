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

public class SqlCriteriaQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected String name;
	protected String nameLike;
	protected String moduleId;
	protected String tableName;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	public SqlCriteriaQuery() {

	}

	public SqlCriteriaQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public SqlCriteriaQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
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

	public String getModuleId() {
		return moduleId;
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

			if ("parentId".equals(sortColumn)) {
				orderBy = "E.PARENTID_" + a_x;
			}

			if ("tenantId".equals(sortColumn)) {
				orderBy = "E.TENANTID_" + a_x;
			}

			if ("name".equals(sortColumn)) {
				orderBy = "E.NAME_" + a_x;
			}

			if ("moduleId".equals(sortColumn)) {
				orderBy = "E.MODULEID_" + a_x;
			}

			if ("businessKey".equals(sortColumn)) {
				orderBy = "E.BUSINESSKEY_" + a_x;
			}

			if ("columnName".equals(sortColumn)) {
				orderBy = "E.COLUMNNAME_" + a_x;
			}

			if ("columnType".equals(sortColumn)) {
				orderBy = "E.COLUMNTYPE_" + a_x;
			}

			if ("tableName".equals(sortColumn)) {
				orderBy = "E.TABLENAME_" + a_x;
			}

			if ("tableAlias".equals(sortColumn)) {
				orderBy = "E.TABLEALIAS_" + a_x;
			}

			if ("paramName".equals(sortColumn)) {
				orderBy = "E.PARAMNAME_" + a_x;
			}

			if ("paramTitle".equals(sortColumn)) {
				orderBy = "E.PARAMTITLE_" + a_x;
			}

			if ("collectionFlag".equals(sortColumn)) {
				orderBy = "E.COLLECTIONFLAG_" + a_x;
			}

			if ("condition".equals(sortColumn)) {
				orderBy = "E.CONDITION_" + a_x;
			}

			if ("separator".equals(sortColumn)) {
				orderBy = "E.SEPARATOR_" + a_x;
			}

			if ("sql".equals(sortColumn)) {
				orderBy = "E.SQL_" + a_x;
			}

			if ("treeId".equals(sortColumn)) {
				orderBy = "E.TREEID_" + a_x;
			}

			if ("level".equals(sortColumn)) {
				orderBy = "E.LEVEL_" + a_x;
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

	public String getTableName() {
		return tableName;
	}

	public String getTreeId() {
		return treeId;
	}

	public String getTreeIdLike() {
		if (treeIdLike != null && treeIdLike.trim().length() > 0) {
			if (!treeIdLike.startsWith("%")) {
				treeIdLike = "%" + treeIdLike;
			}
			if (!treeIdLike.endsWith("%")) {
				treeIdLike = treeIdLike + "%";
			}
		}
		return treeIdLike;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("parentId", "PARENTID_");
		addColumn("tenantId", "TENANTID_");
		addColumn("name", "NAME_");
		addColumn("moduleId", "MODULEID_");
		addColumn("businessKey", "BUSINESSKEY_");
		addColumn("columnName", "COLUMNNAME_");
		addColumn("columnType", "COLUMNTYPE_");
		addColumn("tableName", "TABLENAME_");
		addColumn("tableAlias", "TABLEALIAS_");
		addColumn("paramName", "PARAMNAME_");
		addColumn("paramTitle", "PARAMTITLE_");
		addColumn("collectionFlag", "COLLECTIONFLAG_");
		addColumn("condition", "CONDITION_");
		addColumn("separator", "SEPARATOR_");
		addColumn("sql", "SQL_");
		addColumn("treeId", "TREEID_");
		addColumn("level", "LEVEL_");
		addColumn("sortNo", "SORTNO_");
		addColumn("locked", "LOCKED_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
		addColumn("updateBy", "UPDATEBY_");
		addColumn("updateTime", "UPDATETIME_");
	}

	public SqlCriteriaQuery moduleId(String moduleId) {
		if (moduleId == null) {
			throw new RuntimeException("moduleId is null");
		}
		this.moduleId = moduleId;
		return this;
	}

	public SqlCriteriaQuery name(String name) {
		if (name == null) {
			throw new RuntimeException("name is null");
		}
		this.name = name;
		return this;
	}

	public SqlCriteriaQuery nameLike(String nameLike) {
		if (nameLike == null) {
			throw new RuntimeException("name is null");
		}
		this.nameLike = nameLike;
		return this;
	}

	public void setBusinessKeys(List<String> businessKeys) {
		this.businessKeys = businessKeys;
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setLocked(Integer locked) {
		this.locked = locked;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNameLike(String nameLike) {
		this.nameLike = nameLike;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setTreeId(String treeId) {
		this.treeId = treeId;
	}

	public void setTreeIdLike(String treeIdLike) {
		this.treeIdLike = treeIdLike;
	}

	public SqlCriteriaQuery tableName(String tableName) {
		if (tableName == null) {
			throw new RuntimeException("tableName is null");
		}
		this.tableName = tableName;
		return this;
	}

	public SqlCriteriaQuery tenantId(String tenantId) {
		if (tenantId == null) {
			throw new RuntimeException("tenantId is null");
		}
		this.tenantId = tenantId;
		return this;
	}

	public SqlCriteriaQuery treeId(String treeId) {
		if (treeId == null) {
			throw new RuntimeException("treeId is null");
		}
		this.treeId = treeId;
		return this;
	}

	public SqlCriteriaQuery treeIdLike(String treeIdLike) {
		if (treeIdLike == null) {
			throw new RuntimeException("treeId is null");
		}
		this.treeIdLike = treeIdLike;
		return this;
	}

}