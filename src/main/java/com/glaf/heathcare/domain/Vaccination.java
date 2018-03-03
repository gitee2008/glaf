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
@Table(name = "HEALTH_VACCINATION")
public class Vaccination implements Serializable, JSONable {
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

	/**
	 * 性别
	 */
	@Column(name = "SEX_", length = 2)
	protected String sex;

	/**
	 * 疫苗
	 */
	@Column(name = "VACCINE_", length = 200)
	protected String vaccine;

	/**
	 * 序号
	 */
	@Column(name = "SORTNO_")
	protected int sortNo;

	/**
	 * 接种日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INOCULATEDATE_")
	protected Date inoculateDate;

	/**
	 * 接种医生
	 */
	@Column(name = "DOCTOR_", length = 50)
	protected String doctor;

	/**
	 * 年
	 */
	@Column(name = "YEAR_")
	protected int year;

	/**
	 * 月
	 */
	@Column(name = "MONTH_")
	protected int month;

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

	public Vaccination() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vaccination other = (Vaccination) obj;
		if (id != other.id)
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

	public String getDoctor() {
		return this.doctor;
	}

	public String getGradeId() {
		return this.gradeId;
	}

	public long getId() {
		return this.id;
	}

	public Date getInoculateDate() {
		return this.inoculateDate;
	}

	public String getInoculateDateString() {
		if (this.inoculateDate != null) {
			return DateUtils.getDateTime(this.inoculateDate);
		}
		return "";
	}

	public int getMonth() {
		return this.month;
	}

	public String getName() {
		return this.name;
	}

	public String getPersonId() {
		return this.personId;
	}

	public String getSex() {
		return this.sex;
	}

	public int getSortNo() {
		return this.sortNo;
	}

	public String getTenantId() {
		return this.tenantId;
	}

	public String getVaccine() {
		return this.vaccine;
	}

	public int getYear() {
		return this.year;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public Vaccination jsonToObject(JSONObject jsonObject) {
		return VaccinationJsonFactory.jsonToObject(jsonObject);
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setInoculateDate(Date inoculateDate) {
		this.inoculateDate = inoculateDate;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setVaccine(String vaccine) {
		this.vaccine = vaccine;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public JSONObject toJsonObject() {
		return VaccinationJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return VaccinationJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
