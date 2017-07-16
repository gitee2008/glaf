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

package com.glaf.base.modules.sys.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.base.modules.sys.util.SysRoleJsonFactory;
import com.glaf.core.base.JSONable;

@Entity
@Table(name = "SYS_ROLE")
public class SysRole implements java.lang.Comparable<SysRole>, Serializable, JSONable {
	private static final long serialVersionUID = 7738558740111388611L;

	@Id
	@Column(name = "ID", length = 50, nullable = false)
	protected String id;

	/**
	 * 名称
	 */
	@Column(name = "ROLENAME", length = 100)
	protected String name;

	/**
	 * 编码
	 */
	@Column(name = "CODE", length = 50)
	protected String code;

	/**
	 * 描述
	 */
	@Column(name = "CONTENT", length = 500)
	protected String content;

	/**
	 * 类型
	 */
	@Column(name = "TYPE", length = 50)
	protected String type;

	/**
	 * 创建人
	 */
	@Column(name = "CREATEBY", length = 50)
	protected String createBy;

	/**
	 * 创建日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATEDATE")
	protected Date createDate;

	@javax.persistence.Transient
	protected long nodeId;

	/**
	 * 序号
	 */
	@Column(name = "SORTNO")
	protected int sort;

	/**
	 * 首页链接
	 */
	@Column(name = "INDEXURL", length = 500)
	protected String indexUrl;

	/**
	 * 是否开放分级管理
	 */
	@Column(name = "ISUSEBRANCH", length = 10)
	protected String isUseBranch;

	/**
	 * 是否开放给机构管理
	 */
	@Column(name = "ISUSEORGANIZATION", length = 10)
	protected String isUseOrganization;

	/**
	 * 修改人
	 */
	@Column(name = "UPDATEBY", length = 50)
	protected String updateBy;

	/**
	 * 修改日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATEDATE")
	protected Date updateDate;

	@Column(name = "DELETEFLAG")
	protected int deleteFlag;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DELETETIME")
	protected Date deleteTime;

	@javax.persistence.Transient
	protected List<SysUser> users = new ArrayList<SysUser>();

	public SysRole() {

	}

	public int compareTo(SysRole o) {
		if (o == null) {
			return -1;
		}

		SysRole obj = o;

		int l = this.sort - obj.getSort();

		int ret = 0;

		if (l > 0) {
			ret = 1;
		} else if (l < 0) {
			ret = -1;
		}
		return ret;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SysRole other = (SysRole) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getCode() {
		return this.code;
	}

	public String getContent() {
		return content;
	}

	public String getCreateBy() {
		return this.createBy;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public int getDeleteFlag() {
		return deleteFlag;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}

	public String getId() {
		return this.id;
	}

	public String getIndexUrl() {
		return indexUrl;
	}

	public String getIsUseBranch() {
		return isUseBranch;
	}

	public String getIsUseOrganization() {
		return isUseOrganization;
	}

	public String getName() {
		return this.name;
	}

	public long getNodeId() {
		return nodeId;
	}

	public String getRoleId() {
		return this.id;
	}

	public int getSort() {
		return this.sort;
	}

	public String getType() {
		return type;
	}

	public String getUpdateBy() {
		return this.updateBy;
	}

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public List<SysUser> getUsers() {
		if (users == null) {
			users = new ArrayList<SysUser>();
		}
		return users;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public SysRole jsonToObject(JSONObject jsonObject) {
		return SysRoleJsonFactory.jsonToObject(jsonObject);
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setIndexUrl(String indexUrl) {
		this.indexUrl = indexUrl;
	}

	public void setIsUseBranch(String isUseBranch) {
		this.isUseBranch = isUseBranch;
	}

	public void setIsUseOrganization(String isUseOrganization) {
		this.isUseOrganization = isUseOrganization;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNodeId(long nodeId) {
		this.nodeId = nodeId;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public void setUsers(List<SysUser> users) {
		this.users = users;
	}

	public JSONObject toJsonObject() {
		return SysRoleJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return SysRoleJsonFactory.toObjectNode(this);
	}

	public String toString() {
		return toJsonObject().toJSONString();
	}

}