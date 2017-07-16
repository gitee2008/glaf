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

package com.glaf.core.query;

import java.util.*;
import com.glaf.core.query.DataQuery;

public class DictoryDefinitionQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected String columnName;
	protected String columnNameLike;
	protected String name;
	protected String nameLike;
	protected Long nodeId;
	protected List<Long> nodeIds;
	protected String target;
	protected String titleLike;

	public DictoryDefinitionQuery() {

	}

	public DictoryDefinitionQuery columnName(String columnName) {
		if (columnName == null) {
			throw new RuntimeException("columnName is null");
		}
		this.columnName = columnName;
		return this;
	}

	public DictoryDefinitionQuery columnNameLike(String columnNameLike) {
		if (columnNameLike == null) {
			throw new RuntimeException("columnName is null");
		}
		this.columnNameLike = columnNameLike;
		return this;
	}

	public String getColumnName() {
		return columnName;
	}

	public String getColumnNameLike() {
		if (columnNameLike != null && columnNameLike.trim().length() > 0) {
			if (!columnNameLike.startsWith("%")) {
				columnNameLike = "%" + columnNameLike;
			}
			if (!columnNameLike.endsWith("%")) {
				columnNameLike = columnNameLike + "%";
			}
		}
		return columnNameLike;
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
				orderBy = "E.NODEID" + a_x;
			}

			if ("name".equals(sortColumn)) {
				orderBy = "E.NAME" + a_x;
			}

			if ("columnName".equals(sortColumn)) {
				orderBy = "E.COLUMNNAME" + a_x;
			}

			if ("title".equals(sortColumn)) {
				orderBy = "E.TITLE" + a_x;
			}

			if ("type".equals(sortColumn)) {
				orderBy = "E.TYPE" + a_x;
			}

			if ("length".equals(sortColumn)) {
				orderBy = "E.LENGTH" + a_x;
			}

			if ("sort".equals(sortColumn)) {
				orderBy = "E.SORTNO" + a_x;
			}

			if ("required".equals(sortColumn)) {
				orderBy = "E.REQUIRED" + a_x;
			}

			if ("target".equals(sortColumn)) {
				orderBy = "E.TARGET" + a_x;
			}

		}
		return orderBy;
	}

	public String getTarget() {
		return target;
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

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID");
		addColumn("nodeId", "NODEID");
		addColumn("name", "NAME");
		addColumn("columnName", "COLUMNNAME");
		addColumn("title", "TITLE");
		addColumn("type", "TYPE");
		addColumn("length", "LENGTH");
		addColumn("sort", "SORTNO");
		addColumn("required", "REQUIRED");
		addColumn("target", "TARGET");
	}

	public DictoryDefinitionQuery name(String name) {
		if (name == null) {
			throw new RuntimeException("name is null");
		}
		this.name = name;
		return this;
	}

	public DictoryDefinitionQuery nameLike(String nameLike) {
		if (nameLike == null) {
			throw new RuntimeException("name is null");
		}
		this.nameLike = nameLike;
		return this;
	}

	public DictoryDefinitionQuery nodeId(Long nodeId) {
		if (nodeId == null) {
			throw new RuntimeException("nodeId is null");
		}
		this.nodeId = nodeId;
		return this;
	}

	public DictoryDefinitionQuery nodeIds(List<Long> nodeIds) {
		if (nodeIds == null) {
			throw new RuntimeException("nodeIds is empty ");
		}
		this.nodeIds = nodeIds;
		return this;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public void setColumnNameLike(String columnNameLike) {
		this.columnNameLike = columnNameLike;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNameLike(String nameLike) {
		this.nameLike = nameLike;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public void setNodeIds(List<Long> nodeIds) {
		this.nodeIds = nodeIds;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public void setTitleLike(String titleLike) {
		this.titleLike = titleLike;
	}

	public DictoryDefinitionQuery target(String target) {
		if (target == null) {
			throw new RuntimeException("target is null");
		}
		this.target = target;
		return this;
	}

	public DictoryDefinitionQuery titleLike(String titleLike) {
		if (titleLike == null) {
			throw new RuntimeException("title is null");
		}
		this.titleLike = titleLike;
		return this;
	}

}