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
package com.glaf.ui.model;

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

import com.glaf.core.util.DateUtils;

/**
 * 
 * 板块权限
 *
 */

@Entity
@Table(name = "UI_PANEL_PERM")
public class PanelPermission implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@Column(name = "ID_", nullable = false)
	protected String id;

	/**
	 * 板块编号
	 */
	@Column(name = "PANELID_", length = 50)
	protected String panelId;

	/**
	 * 用户编号
	 */
	@Column(name = "ACTORID_", length = 50)
	protected String actorId;

	/**
	 * 角色编号
	 */
	@Column(name = "ROLEID_", length = 50)
	protected String roleId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATEDATE_")
	protected Date createDate;

	@Column(name = "CREATEBY_", length = 50)
	protected String createBy;

	public PanelPermission() {

	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPanelId() {
		return this.panelId;
	}

	public String getActorId() {
		return this.actorId;
	}

	public String getRoleId() {
		return this.roleId;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public String getCreateDateString() {
		if (this.createDate != null) {
			return DateUtils.getDateTime(this.createDate);
		}
		return "";
	}

	public String getCreateBy() {
		return this.createBy;
	}

	public void setPanelId(String panelId) {
		this.panelId = panelId;
	}

	public void setActorId(String actorId) {
		this.actorId = actorId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PanelPermission other = (PanelPermission) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

}
