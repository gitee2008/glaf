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

package com.glaf.heathcare.domain;

import java.io.*;
import java.util.*;
import javax.persistence.*;
import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.glaf.core.base.*;
import com.glaf.core.util.DateUtils;
import com.glaf.heathcare.util.*;

/**
 * 
 * 实体对象
 *
 */

@Entity
@Table(name = "HEALTH_PERSON_TRANSFER_SCHOOL")
public class PersonTransferSchool implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", length = 50, nullable = false)
	protected String id;

	/**
	 * 租户编号
	 */
	@Column(name = "TENANTID_", length = 50)
	protected String tenantId;

	/**
	 * 学生编号
	 */
	@Column(name = "PERSONID_", length = 50)
	protected String personId;

	/**
	 * 姓名
	 */
	@Column(name = "NAME_", length = 100)
	protected String name;

	/**
	 * 性别
	 */
	@Column(name = "SEX_", length = 1)
	protected String sex;

	/**
	 * 来源租户编号
	 */
	@Column(name = "FROM_TENANTID_", length = 50)
	protected String fromTenantId;

	/**
	 * 转入前就读幼儿园名称
	 */
	@Column(name = "FROM_SCHOOL_", length = 200)
	protected String fromSchool;

	/**
	 * 转到租户编号
	 */
	@Column(name = "TO_TENANTID_", length = 50)
	protected String toTenantId;

	/**
	 * 转到幼儿园名称
	 */
	@Column(name = "TO_SCHOOL_", length = 200)
	protected String toSchool;

	/**
	 * 体检时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CHECKDATE_")
	protected Date checkDate;

	/**
	 * 体检机构
	 */
	@Column(name = "CHECKORGANIZATION_", length = 200)
	protected String checkOrganization;

	/**
	 * 体检机构ID
	 */
	@Column(name = "CHECKORGANIZATIONID_")
	protected long checkOrganizationId;

	/**
	 * 健康状况
	 */
	@Column(name = "CHECKRESULT_", length = 500)
	protected String checkResult;

	/**
	 * 备注
	 */
	@Column(name = "REMARK_", length = 500)
	protected String remark;

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

	public PersonTransferSchool() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonTransferSchool other = (PersonTransferSchool) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Date getCheckDate() {
		return this.checkDate;
	}

	public String getCheckDateString() {
		if (this.checkDate != null) {
			return DateUtils.getDateTime(this.checkDate);
		}
		return "";
	}

	public String getCheckOrganization() {
		return this.checkOrganization;
	}

	public long getCheckOrganizationId() {
		return this.checkOrganizationId;
	}

	public String getCheckResult() {
		return this.checkResult;
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

	public String getFromSchool() {
		return this.fromSchool;
	}

	public String getFromTenantId() {
		return this.fromTenantId;
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getPersonId() {
		return this.personId;
	}

	public String getRemark() {
		return this.remark;
	}

	public String getSex() {
		return this.sex;
	}

	public String getTenantId() {
		return this.tenantId;
	}

	public String getToSchool() {
		return toSchool;
	}

	public String getToTenantId() {
		return toTenantId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public PersonTransferSchool jsonToObject(JSONObject jsonObject) {
		return PersonTransferSchoolJsonFactory.jsonToObject(jsonObject);
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public void setCheckOrganization(String checkOrganization) {
		this.checkOrganization = checkOrganization;
	}

	public void setCheckOrganizationId(long checkOrganizationId) {
		this.checkOrganizationId = checkOrganizationId;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setFromSchool(String fromSchool) {
		this.fromSchool = fromSchool;
	}

	public void setFromTenantId(String fromTenantId) {
		this.fromTenantId = fromTenantId;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setToSchool(String toSchool) {
		this.toSchool = toSchool;
	}

	public void setToTenantId(String toTenantId) {
		this.toTenantId = toTenantId;
	}

	public JSONObject toJsonObject() {
		return PersonTransferSchoolJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return PersonTransferSchoolJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
