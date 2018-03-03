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
@Table(name = "HEALTH_PERSON_SICKNESS")
public class PersonSickness implements Serializable, JSONable {
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

	@javax.persistence.Transient
	protected String gradeName;

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

	@javax.persistence.Transient
	protected int age;

	/**
	 * 病名
	 */
	@Column(name = "SICKNESS_", length = 200)
	protected String sickness;

	/**
	 * 传染性疾病
	 */
	@Column(name = "INFECTIOUSDISEASE_", length = 50)
	protected String infectiousDisease;

	/**
	 * 是否传染性疾病
	 */
	@Column(name = "INFECTIOUSFLAG_", length = 1)
	protected String infectiousFlag;

	/**
	 * 发现时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DISCOVERTIME_")
	protected Date discoverTime;

	/**
	 * 报告时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REPORTTIME_")
	protected Date reportTime;

	/**
	 * 报告何处
	 */
	@Column(name = "REPORTORG_", length = 200)
	protected String reportOrg;

	/**
	 * 报告责任人
	 */
	@Column(name = "REPORTRESPONSIBLE_", length = 200)
	protected String reportResponsible;

	/**
	 * 报告方式
	 */
	@Column(name = "REPORTWAY_", length = 50)
	protected String reportWay;

	/**
	 * 管理部门意见
	 */
	@Column(name = "SUPERVISIONOPINION_", length = 500)
	protected String supervisionOpinion;

	/**
	 * 确认时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CONFIRMTIME_")
	protected Date confirmTime;

	/**
	 * 接收人1
	 */
	@Column(name = "RECEIVER1_", length = 200)
	protected String receiver1;

	/**
	 * 接收机构1
	 */
	@Column(name = "RECEIVEORG1_", length = 200)
	protected String receiveOrg1;

	/**
	 * 接收人2
	 */
	@Column(name = "RECEIVER2_", length = 200)
	protected String receiver2;

	/**
	 * 接收机构2
	 */
	@Column(name = "RECEIVEORG2_", length = 200)
	protected String receiveOrg2;

	/**
	 * 主要症状
	 */
	@Column(name = "SYMPTOM_", length = 2000)
	protected String symptom;

	/**
	 * 治疗情况
	 */
	@Column(name = "TREAT_", length = 2000)
	protected String treat;

	/**
	 * 医院
	 */
	@Column(name = "HOSPITAL_", length = 200)
	protected String hospital;

	/**
	 * 就诊日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CLINICTIME_")
	protected Date clinicTime;

	/**
	 * 排查结果
	 */
	@Column(name = "RESULT_", length = 200)
	protected String result;

	/**
	 * 备注
	 */
	@Column(name = "REMARK_", length = 2000)
	protected String remark;

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

	@javax.persistence.Transient
	protected String createByName;

	/**
	 * 创建日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME_")
	protected Date createTime;

	public PersonSickness() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonSickness other = (PersonSickness) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public int getAge() {
		return age;
	}

	public Date getClinicTime() {
		return this.clinicTime;
	}

	public String getClinicTimeString() {
		if (this.clinicTime != null) {
			return DateUtils.getDateTime(this.clinicTime);
		}
		return "";
	}

	public Date getConfirmTime() {
		return this.confirmTime;
	}

	public String getConfirmTimeString() {
		if (this.confirmTime != null) {
			return DateUtils.getDateTime(this.confirmTime);
		}
		return "";
	}

	public String getCreateBy() {
		return this.createBy;
	}

	public String getCreateByName() {
		return createByName;
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

	public Date getDiscoverTime() {
		return this.discoverTime;
	}

	public String getDiscoverTimeString() {
		if (this.discoverTime != null) {
			return DateUtils.getDateTime(this.discoverTime);
		}
		return "";
	}

	public String getGradeId() {
		return this.gradeId;
	}

	public String getGradeName() {
		return gradeName;
	}

	public String getHospital() {
		return this.hospital;
	}

	public String getId() {
		return this.id;
	}

	public String getInfectiousDisease() {
		return infectiousDisease;
	}

	public String getInfectiousFlag() {
		return this.infectiousFlag;
	}

	public int getMonth() {
		return month;
	}

	public String getName() {
		return this.name;
	}

	public String getPersonId() {
		return this.personId;
	}

	public String getReceiveOrg1() {
		return this.receiveOrg1;
	}

	public String getReceiveOrg2() {
		return this.receiveOrg2;
	}

	public String getReceiver1() {
		return this.receiver1;
	}

	public String getReceiver2() {
		return this.receiver2;
	}

	public String getRemark() {
		return this.remark;
	}

	public String getReportOrg() {
		return reportOrg;
	}

	public String getReportResponsible() {
		return reportResponsible;
	}

	public Date getReportTime() {
		return this.reportTime;
	}

	public String getReportTimeString() {
		if (this.reportTime != null) {
			return DateUtils.getDateTime(this.reportTime);
		}
		return "";
	}

	public String getReportWay() {
		return reportWay;
	}

	public String getResult() {
		return this.result;
	}

	public String getSex() {
		return sex;
	}

	public String getSickness() {
		return this.sickness;
	}

	public String getSupervisionOpinion() {
		return supervisionOpinion;
	}

	public String getSymptom() {
		return symptom;
	}

	public String getTenantId() {
		return this.tenantId;
	}

	public String getTreat() {
		return this.treat;
	}

	public int getYear() {
		return year;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public PersonSickness jsonToObject(JSONObject jsonObject) {
		return PersonSicknessJsonFactory.jsonToObject(jsonObject);
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setClinicTime(Date clinicTime) {
		this.clinicTime = clinicTime;
	}

	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateByName(String createByName) {
		this.createByName = createByName;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setDiscoverTime(Date discoverTime) {
		this.discoverTime = discoverTime;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setInfectiousDisease(String infectiousDisease) {
		this.infectiousDisease = infectiousDisease;
	}

	public void setInfectiousFlag(String infectiousFlag) {
		this.infectiousFlag = infectiousFlag;
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

	public void setReceiveOrg1(String receiveOrg1) {
		this.receiveOrg1 = receiveOrg1;
	}

	public void setReceiveOrg2(String receiveOrg2) {
		this.receiveOrg2 = receiveOrg2;
	}

	public void setReceiver1(String receiver1) {
		this.receiver1 = receiver1;
	}

	public void setReceiver2(String receiver2) {
		this.receiver2 = receiver2;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setReportOrg(String reportOrg) {
		this.reportOrg = reportOrg;
	}

	public void setReportResponsible(String reportResponsible) {
		this.reportResponsible = reportResponsible;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	public void setReportWay(String reportWay) {
		this.reportWay = reportWay;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setSickness(String sickness) {
		this.sickness = sickness;
	}

	public void setSupervisionOpinion(String supervisionOpinion) {
		this.supervisionOpinion = supervisionOpinion;
	}

	public void setSymptom(String symptom) {
		this.symptom = symptom;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setTreat(String treat) {
		this.treat = treat;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public JSONObject toJsonObject() {
		return PersonSicknessJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return PersonSicknessJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
