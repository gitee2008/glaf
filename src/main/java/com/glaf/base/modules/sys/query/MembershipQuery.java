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

package com.glaf.base.modules.sys.query;

import java.util.List;

import com.glaf.core.query.DataQuery;

public class MembershipQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected String attribute;
	protected String attributeLike;
	protected String modifyBy;
	protected Long nodeId;
	protected List<Long> nodeIds;
	protected List<String> objectIds;
	protected List<String> objectValues;
	protected String roleCode;
	protected List<String> roleCodes;
	protected String roleId;
	protected List<String> roleIds;
	protected String type;
	protected List<String> types;

	public MembershipQuery() {

	}

	public MembershipQuery attribute(String attribute) {
		if (attribute == null) {
			throw new RuntimeException("attribute is null");
		}
		this.attribute = attribute;
		return this;
	}

	public MembershipQuery attributeLike(String attributeLike) {
		if (attributeLike == null) {
			throw new RuntimeException("attribute is null");
		}
		this.attributeLike = attributeLike;
		return this;
	}

	public String getAttribute() {
		return attribute;
	}

	public String getAttributeLike() {
		if (attributeLike != null && attributeLike.trim().length() > 0) {
			if (!attributeLike.startsWith("%")) {
				attributeLike = "%" + attributeLike;
			}
			if (!attributeLike.endsWith("%")) {
				attributeLike = attributeLike + "%";
			}
		}
		return attributeLike;
	}

	public String getModifyBy() {
		return modifyBy;
	}

	public Long getNodeId() {
		return nodeId;
	}

	public List<Long> getNodeIds() {
		return nodeIds;
	}

	public List<String> getObjectIds() {
		return objectIds;
	}

	public List<String> getObjectValues() {
		return objectValues;
	}

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("superiorId".equals(sortColumn)) {
				orderBy = "E.SUPERIORID_" + a_x;
			}

			if ("nodeId".equals(sortColumn)) {
				orderBy = "E.NODEID_" + a_x;
			}

			if ("modifyBy".equals(sortColumn)) {
				orderBy = "E.MODIFYBY_" + a_x;
			}

			if ("objectId".equals(sortColumn)) {
				orderBy = "E.OBJECTID_" + a_x;
			}

			if ("actorId".equals(sortColumn)) {
				orderBy = "E.ACTORID_" + a_x;
			}

			if ("roleId".equals(sortColumn)) {
				orderBy = "E.ROLEID_" + a_x;
			}

			if ("attribute".equals(sortColumn)) {
				orderBy = "E.ATTRIBUTE_" + a_x;
			}

			if ("modifyDate".equals(sortColumn)) {
				orderBy = "E.MODIFYDATE_" + a_x;
			}

			if ("type".equals(sortColumn)) {
				orderBy = "E.TYPE_" + a_x;
			}

			if ("objectValue".equals(sortColumn)) {
				orderBy = "E.OBJECTVALUE_" + a_x;
			}

		}
		return orderBy;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public List<String> getRoleCodes() {
		return roleCodes;
	}

	public String getRoleId() {
		return roleId;
	}

	public List<String> getRoleIds() {
		return roleIds;
	}

	public String getTenantId() {
		return tenantId;
	}

	public String getType() {
		return type;
	}

	public List<String> getTypes() {
		return types;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("superiorId", "SUPERIORID_");
		addColumn("nodeId", "NODEID_");
		addColumn("modifyBy", "MODIFYBY_");
		addColumn("objectId", "OBJECTID_");
		addColumn("actorId", "ACTORID_");
		addColumn("roleId", "ROLEID_");
		addColumn("attribute", "ATTRIBUTE_");
		addColumn("modifyDate", "MODIFYDATE_");
		addColumn("type", "TYPE_");
		addColumn("objectValue", "OBJECTVALUE_");
	}

	public MembershipQuery modifyBy(String modifyBy) {
		if (modifyBy == null) {
			throw new RuntimeException("modifyBy is null");
		}
		this.modifyBy = modifyBy;
		return this;
	}

	public MembershipQuery nodeId(Long nodeId) {
		if (nodeId == null) {
			throw new RuntimeException("nodeId is null");
		}
		this.nodeId = nodeId;
		return this;
	}

	public MembershipQuery nodeIds(List<Long> nodeIds) {
		if (nodeIds == null) {
			throw new RuntimeException("nodeIds is empty ");
		}
		this.nodeIds = nodeIds;
		return this;
	}

	public MembershipQuery objectIds(List<String> objectIds) {
		if (objectIds == null) {
			throw new RuntimeException("objectIds is empty ");
		}
		this.objectIds = objectIds;
		return this;
	}

	public MembershipQuery objectValues(List<String> objectValues) {
		if (objectValues == null) {
			throw new RuntimeException("objectValues is empty ");
		}
		this.objectValues = objectValues;
		return this;
	}

	public MembershipQuery roleId(String roleId) {
		if (roleId == null) {
			throw new RuntimeException("roleId is null");
		}
		this.roleId = roleId;
		return this;
	}

	public MembershipQuery roleIds(List<String> roleIds) {
		if (roleIds == null) {
			throw new RuntimeException("roleIds is empty ");
		}
		this.roleIds = roleIds;
		return this;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public void setAttributeLike(String attributeLike) {
		this.attributeLike = attributeLike;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public void setNodeIds(List<Long> nodeIds) {
		this.nodeIds = nodeIds;
	}

	public void setObjectIds(List<String> objectIds) {
		this.objectIds = objectIds;
	}

	public void setObjectValues(List<String> objectValues) {
		this.objectValues = objectValues;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public void setRoleCodes(List<String> roleCodes) {
		this.roleCodes = roleCodes;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public void setRoleIds(List<String> roleIds) {
		this.roleIds = roleIds;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}

	public MembershipQuery tenantId(String tenantId) {
		if (tenantId == null) {
			throw new RuntimeException("tenantId is null");
		}
		this.tenantId = tenantId;
		return this;
	}

	public MembershipQuery type(String type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		this.type = type;
		return this;
	}

	public MembershipQuery types(List<String> types) {
		if (types == null) {
			throw new RuntimeException("types is empty ");
		}
		this.types = types;
		return this;
	}

}