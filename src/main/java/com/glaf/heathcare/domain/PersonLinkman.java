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
@Table(name = "HEALTH_PERSON_LINKMAN")
public class PersonLinkman implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", nullable = false, length = 50)
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
	 * 关系
	 */
	@Column(name = "RELATIONSHIP_", length = 50)
	protected String relationship;

	/**
	 * 工作单位
	 */
	@Column(name = "COMPANY_", length = 200)
	protected String company;

	/**
	 * 手机号码
	 */
	@Column(name = "MOBILE_", length = 50)
	protected String mobile;

	/**
	 * 电子邮件
	 */
	@Column(name = "MAIL_", length = 200)
	protected String mail;

	/**
	 * 是否监护人
	 */
	@Column(name = "WARDSHIP_", length = 1)
	protected String wardship;

	/**
	 * 备注
	 */
	@Column(name = "REMARK_", length = 2000)
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

	public PersonLinkman() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonLinkman other = (PersonLinkman) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getCompany() {
		return this.company;
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

	public String getId() {
		return this.id;
	}

	public String getMail() {
		return this.mail;
	}

	public String getMobile() {
		return this.mobile;
	}

	public String getName() {
		return this.name;
	}

	public String getPersonId() {
		return this.personId;
	}

	public String getRelationship() {
		return this.relationship;
	}

	public String getRemark() {
		return this.remark;
	}

	public String getTenantId() {
		return tenantId;
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

	public String getWardship() {
		return this.wardship;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public PersonLinkman jsonToObject(JSONObject jsonObject) {
		return PersonLinkmanJsonFactory.jsonToObject(jsonObject);
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public void setWardship(String wardship) {
		this.wardship = wardship;
	}

	public JSONObject toJsonObject() {
		return PersonLinkmanJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return PersonLinkmanJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
