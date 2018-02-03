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

import java.io.*;
import java.util.*;
import javax.persistence.*;
import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.glaf.core.base.*;
import com.glaf.core.util.DateUtils;
import com.glaf.base.modules.sys.util.*;

/**
 * 
 * 实体对象
 *
 */

@Entity
@Table(name = "SYS_TENANT_CONFIG")
public class TenantConfig implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", nullable = false)
	protected long id;

	/**
	 * 租户编号
	 */
	@Column(name = "TENANTID_", length = 50)
	protected String tenantId;

	/**
	 * 系统名称
	 */
	@Column(name = "SYSNAME_", length = 200)
	protected String sysName;

	/**
	 * 餐点类型
	 */
	@Column(name = "TYPEID_")
	protected long typeId;

	/**
	 * 早餐时间
	 */
	@Column(name = "BREAKFASTTIME_", length = 10)
	protected String breakfastTime;

	/**
	 * 早点时间
	 */
	@Column(name = "BREAKFASMIDTTIME_", length = 10)
	protected String breakfastMidTime;

	/**
	 * 午餐时间
	 */
	@Column(name = "LUNCHTIME_", length = 10)
	protected String lunchTime;

	/**
	 * 午点时间
	 */
	@Column(name = "SNACKTIME_", length = 10)
	protected String snackTime;

	/**
	 * 晚餐时间
	 */
	@Column(name = "DINNERTIME_", length = 10)
	protected String dinnerTime;

	/**
	 * 是否分享
	 */
	@Column(name = "SHAREFLAG_", length = 1)
	protected String shareFlag;

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

	/**
	 * 修改人
	 */
	@Column(name = "UPDATEBY_", length = 50)
	protected String updateBy;

	/**
	 * 修改日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATETIME_")
	protected Date updateTime;

	public TenantConfig() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TenantConfig other = (TenantConfig) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public String getBreakfastMidTime() {
		return this.breakfastMidTime;
	}

	public String getBreakfastTime() {
		return this.breakfastTime;
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

	public String getDinnerTime() {
		return this.dinnerTime;
	}

	public long getId() {
		return this.id;
	}

	public String getLunchTime() {
		return this.lunchTime;
	}

	public String getShareFlag() {
		return shareFlag;
	}

	public String getSnackTime() {
		return this.snackTime;
	}

	public String getSysName() {
		return this.sysName;
	}

	public String getTenantId() {
		return this.tenantId;
	}

	public long getTypeId() {
		return this.typeId;
	}

	public String getUpdateBy() {
		return this.updateBy;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public String getUpdateTimeString() {
		if (this.updateTime != null) {
			return DateUtils.getDateTime(this.updateTime);
		}
		return "";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public TenantConfig jsonToObject(JSONObject jsonObject) {
		return TenantConfigJsonFactory.jsonToObject(jsonObject);
	}

	public void setBreakfastMidTime(String breakfastMidTime) {
		this.breakfastMidTime = breakfastMidTime;
	}

	public void setBreakfastTime(String breakfastTime) {
		this.breakfastTime = breakfastTime;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setDinnerTime(String dinnerTime) {
		this.dinnerTime = dinnerTime;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setLunchTime(String lunchTime) {
		this.lunchTime = lunchTime;
	}

	public void setShareFlag(String shareFlag) {
		this.shareFlag = shareFlag;
	}

	public void setSnackTime(String snackTime) {
		this.snackTime = snackTime;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public JSONObject toJsonObject() {
		return TenantConfigJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return TenantConfigJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
