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
@Table(name = "HEALTH_MEDICAL_EXAMINATION_DEF")
public class MedicalExaminationDef implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", nullable = false)
	protected long id;

	@Column(name = "CHECKID_", length = 50)
	protected String checkId;

	/**
	 * 租户编号
	 */
	@Column(name = "TENANTID_", length = 50)
	protected String tenantId;

	/**
	 * 主题
	 */
	@Column(name = "TITLE_", length = 100)
	protected String title;

	/**
	 * 类型
	 */
	@Column(name = "TYPE_", length = 100)
	protected String type;

	/**
	 * 体检时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CHECKDATE_")
	protected Date checkDate;

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
	 * 日
	 */
	@Column(name = "DAY_")
	protected int day;

	/**
	 * 备注
	 */
	@Column(name = "REMARK_", length = 500)
	protected String remark;

	/**
	 * 启用标识
	 */
	@Column(name = "ENABLEFLAG_", length = 1)
	protected String enableFlag;

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

	public MedicalExaminationDef() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MedicalExaminationDef other = (MedicalExaminationDef) obj;
		if (id != other.id)
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

	public String getCheckId() {
		return checkId;
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

	public String getEnableFlag() {
		return enableFlag;
	}

	public long getId() {
		return this.id;
	}

	public int getMonth() {
		return this.month;
	}

	public String getRemark() {
		return this.remark;
	}

	public String getTenantId() {
		return this.tenantId;
	}

	public String getTitle() {
		return this.title;
	}

	public String getType() {
		return this.type;
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

	public MedicalExaminationDef jsonToObject(JSONObject jsonObject) {
		return MedicalExaminationDefJsonFactory.jsonToObject(jsonObject);
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public void setCheckId(String checkId) {
		this.checkId = checkId;
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

	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public JSONObject toJsonObject() {
		return MedicalExaminationDefJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return MedicalExaminationDefJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
