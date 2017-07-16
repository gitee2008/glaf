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

import java.util.Date;
import java.util.List;

public class TableDefinitionQuery extends BaseQuery {
	private static final long serialVersionUID = 1L;
	protected List<String> tableIds;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;
	protected String descriptionLike;
	protected Integer revision;
	protected String attachmentFlag;
	protected String privilegeFlag;
	protected String systemFlag;
	protected String titleLike;
	protected String type;
	protected Long nodeId;

	public TableDefinitionQuery() {

	}

	public TableDefinitionQuery attachmentFlag(String attachmentFlag) {
		if (attachmentFlag == null) {
			throw new RuntimeException("attachmentFlag is null");
		}
		this.attachmentFlag = attachmentFlag;
		return this;
	}

	public TableDefinitionQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public TableDefinitionQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public TableDefinitionQuery descriptionLike(String descriptionLike) {
		if (descriptionLike == null) {
			throw new RuntimeException("description is null");
		}
		this.descriptionLike = descriptionLike;
		return this;
	}

	public String getAttachmentFlag() {
		return attachmentFlag;
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

	public Integer getLocked() {
		return locked;
	}

	public Long getNodeId() {
		return nodeId;
	}

	public String getOrderBy() {
		if (sortField != null) {
			String a_x = " asc ";
			if (getSortOrder() != null) {
				a_x = " desc ";
			}

			if ("title".equals(sortField)) {
				orderBy = "E.TITLE_" + a_x;
			}

			if ("createTime".equals(sortField)) {
				orderBy = "E.CREATETIME_" + a_x;
			}

			if ("createBy".equals(sortField)) {
				orderBy = "E.CREATEBY_" + a_x;
			}

			if ("description".equals(sortField)) {
				orderBy = "E.DESCRIPTION_" + a_x;
			}

			if ("locked".equals(sortField)) {
				orderBy = "E.LOCKED_" + a_x;
			}

			if ("revision".equals(sortField)) {
				orderBy = "E.REVISION_" + a_x;
			}

		}
		return orderBy;
	}

	public String getPrivilegeFlag() {
		return privilegeFlag;
	}

	public Integer getRevision() {
		return revision;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public String getSystemFlag() {
		return systemFlag;
	}

	public List<String> getTableIds() {
		return tableIds;
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

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("nodeId", "NODEID_");
		addColumn("tableName", "TABLENAME_");
		addColumn("title", "TITLE_");
		addColumn("createTime", "CREATETIME_");
		addColumn("createBy", "CREATEBY_");
		addColumn("description", "DESCRIPTION_");
		addColumn("locked", "LOCKED_");
		addColumn("revision", "REVISION_");
	}

	public TableDefinitionQuery nodeId(Long nodeId) {
		if (nodeId == null) {
			throw new RuntimeException("nodeId is null");
		}
		this.nodeId = nodeId;
		return this;
	}

	public TableDefinitionQuery privilegeFlag(String privilegeFlag) {
		if (privilegeFlag == null) {
			throw new RuntimeException("privilegeFlag is null");
		}
		this.privilegeFlag = privilegeFlag;
		return this;
	}

	public TableDefinitionQuery revision(Integer revision) {
		if (revision == null) {
			throw new RuntimeException("revision is null");
		}
		this.revision = revision;
		return this;
	}

	public void setAttachmentFlag(String attachmentFlag) {
		this.attachmentFlag = attachmentFlag;
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

	public void setLocked(Integer locked) {
		this.locked = locked;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public void setPrivilegeFlag(String privilegeFlag) {
		this.privilegeFlag = privilegeFlag;
	}

	public void setRevision(Integer revision) {
		this.revision = revision;
	}

	public void setSystemFlag(String systemFlag) {
		this.systemFlag = systemFlag;
	}

	public void setTableIds(List<String> tableIds) {
		this.tableIds = tableIds;
	}

	public void setTitleLike(String titleLike) {
		this.titleLike = titleLike;
	}

	public void setType(String type) {
		this.type = type;
	}

	public TableDefinitionQuery systemFlag(String systemFlag) {
		if (systemFlag == null) {
			throw new RuntimeException("systemFlag is null");
		}
		this.systemFlag = systemFlag;
		return this;
	}

	public TableDefinitionQuery tableIds(List<String> tableIds) {
		if (tableIds == null) {
			throw new RuntimeException("tableIds is null");
		}
		this.tableIds = tableIds;
		return this;
	}

	public TableDefinitionQuery titleLike(String titleLike) {
		if (titleLike == null) {
			throw new RuntimeException("title is null");
		}
		this.titleLike = titleLike;
		return this;
	}

	public TableDefinitionQuery type(String type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		this.type = type;
		return this;
	}

}