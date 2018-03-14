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
 * 晨检及全日观察情况
 *
 */

@Entity
@Table(name = "HEALTH_PERSON_INSPECTION")
public class PersonInspection implements Serializable, JSONable {
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
	 * 班级编号
	 */
	@Column(name = "GRADEID_", length = 50)
	protected String gradeId;

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

	@javax.persistence.Transient
	protected String sex;

	/**
	 * 年月日
	 */
	@Column(name = "DAY_")
	protected int day;

	/**
	 * 考勤阶段（上午am、下午pm、全天day）
	 */
	@Column(name = "SECTION_", length = 50)
	protected String section;

	/**
	 * 考勤情况（0-正常出席、1-迟到、2-请病假、3-请事假）
	 */
	@Column(name = "STATUS_")
	protected int status;

	/**
	 * 类型
	 */
	@Column(name = "TYPE_", length = 50)
	protected String type;

	/**
	 * 巡检标识 （T-保教人员,H-保健医生）
	 */
	@Column(name = "INSPECTIONFLAG_", length = 1)
	protected String inspectionFlag;

	/**
	 * 处理情况
	 */
	@Column(name = "TREAT_", length = 2000)
	protected String treat;

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

	public PersonInspection() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonInspection other = (PersonInspection) obj;
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

	public int getDay() {
		return this.day;
	}

	public String getGradeId() {
		return this.gradeId;
	}

	public String getId() {
		return this.id;
	}

	public String getInspectionFlag() {
		return inspectionFlag;
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

	public String getSection() {
		return this.section;
	}

	public String getSex() {
		return sex;
	}

	public int getStatus() {
		return this.status;
	}

	public String getTenantId() {
		return this.tenantId;
	}

	public String getTreat() {
		return treat;
	}

	public String getType() {
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public PersonInspection jsonToObject(JSONObject jsonObject) {
		return PersonInspectionJsonFactory.jsonToObject(jsonObject);
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setInspectionFlag(String inspectionFlag) {
		this.inspectionFlag = inspectionFlag;
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

	public void setSection(String section) {
		this.section = section;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setTreat(String treat) {
		this.treat = treat;
	}

	public void setType(String type) {
		this.type = type;
	}

	public JSONObject toJsonObject() {
		return PersonInspectionJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return PersonInspectionJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
