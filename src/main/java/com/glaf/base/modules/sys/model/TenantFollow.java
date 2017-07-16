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
@Table(name = "SYS_TENANT_FOLLOW")
public class TenantFollow implements Serializable, JSONable {
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
	 * 租户名称
	 */
	@Column(name = "TENANTNAME_", length = 200)
	protected String tenantName;

	/**
	 * 关注者编号
	 */
	@Column(name = "FOLLOWTENANTID_", length = 50)
	protected String followTenantId;

	/**
	 * 关注者名称
	 */
	@Column(name = "FOLLOWTENANTNAME_", length = 200)
	protected String followTenantName;

	/**
	 * 省/直辖市
	 */
	@Column(name = "PROVINCE_", length = 100)
	protected String province;

	/**
	 * 市
	 */
	@Column(name = "CITY_", length = 100)
	protected String city;

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

	public TenantFollow() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TenantFollow other = (TenantFollow) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public String getCity() {
		return city;
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

	public String getFollowTenantId() {
		return this.followTenantId;
	}

	public String getFollowTenantName() {
		return this.followTenantName;
	}

	public long getId() {
		return id;
	}

	public String getProvince() {
		return province;
	}

	public String getTenantId() {
		return this.tenantId;
	}

	public String getTenantName() {
		return this.tenantName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public TenantFollow jsonToObject(JSONObject jsonObject) {
		return TenantFollowJsonFactory.jsonToObject(jsonObject);
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setFollowTenantId(String followTenantId) {
		this.followTenantId = followTenantId;
	}

	public void setFollowTenantName(String followTenantName) {
		this.followTenantName = followTenantName;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public JSONObject toJsonObject() {
		return TenantFollowJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return TenantFollowJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
