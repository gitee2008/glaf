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

package com.glaf.matrix.data.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.core.base.JSONable;
import com.glaf.core.util.DateUtils;
import com.glaf.matrix.data.util.TableSysPermissionJsonFactory;

/**
 * 
 * 实体对象
 *
 */

@Entity
@Table(name = "SYS_TABLE_PERMISSION")
public class TableSysPermission implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", length = 50, nullable = false)
	protected String id;

	/**
	 * 表编号
	 */
	@Column(name = "TABLEID_", length = 50)
	protected String tableId;

	/**
	 * 表名称
	 */
	@Column(name = "TABLENAME_", length = 50)
	protected String TableName;

	/**
	 * 被授与者
	 */
	@Column(name = "GRANTEE_", length = 50)
	protected String grantee;

	/**
	 * 被授与者类型
	 */
	@Column(name = "GRANTEETYPE_", length = 50)
	protected String granteeType;

	/**
	 * 权限
	 */
	@Column(name = "PRIVILEGE_", length = 50)
	protected String privilege;

	/**
	 * 类型
	 */
	@Column(name = "TYPE_", length = 50)
	protected String type;

	/**
	 * 是否锁定
	 */
	@Column(name = "LOCKED_")
	protected int locked;

	/**
	 * 创建人
	 */
	@Column(name = "CREATEBY_", length = 50)
	protected String createBy;

	/**
	 * 创建日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME_")
	protected Date createTime;

	public TableSysPermission() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TableSysPermission other = (TableSysPermission) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getCreateBy() {
		return this.createBy;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public String getCreateTimeString() {
		if (this.createTime != null) {
			return DateUtils.getDateTime(this.createTime);
		}
		return "";
	}

	public String getGrantee() {
		return this.grantee;
	}

	public String getGranteeType() {
		return this.granteeType;
	}

	public String getId() {
		return this.id;
	}

	public int getLocked() {
		return this.locked;
	}

	public String getPrivilege() {
		return this.privilege;
	}

	public String getTableId() {
		return this.tableId;
	}

	public String getTableName() {
		return this.TableName;
	}

	public String getType() {
		return this.type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public TableSysPermission jsonToObject(JSONObject jsonObject) {
		return TableSysPermissionJsonFactory.jsonToObject(jsonObject);
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setGrantee(String grantee) {
		this.grantee = grantee;
	}

	public void setGranteeType(String granteeType) {
		this.granteeType = granteeType;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLocked(int locked) {
		this.locked = locked;
	}

	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public void setTableName(String TableName) {
		this.TableName = TableName;
	}

	public void setType(String type) {
		this.type = type;
	}

	public JSONObject toJsonObject() {
		return TableSysPermissionJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return TableSysPermissionJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
