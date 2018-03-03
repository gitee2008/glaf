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
 * 每月膳食费
 *
 */

@Entity
@Table(name = "HEALTH_MONTHLY_MEAL_FEE")
public class MonthlyMealFee implements Serializable, JSONable {
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
	 * 班级类型
	 */
	@Column(name = "CLASSTYPE_", length = 50)
	protected String classType;

	/**
	 * 实收伙食费
	 */
	@Column(name = "RECEIPTFUND_")
	protected double receiptFund;

	/**
	 * 总人数
	 */
	@Column(name = "PERSONTOTAL_")
	protected int personTotal;

	/**
	 * 缺勤人数
	 */
	@Column(name = "ABSENTPERSON_")
	protected int absentPerson;

	/**
	 * 缺勤天数
	 */
	@Column(name = "ABSENTDAY_")
	protected int absentDay;

	/**
	 * 缺勤退款
	 */
	@Column(name = "ABSENTREFUND_")
	protected double absentRefund;

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
	 * 学期
	 */
	@Column(name = "SEMESTER_")
	protected int semester;

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

	public MonthlyMealFee() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MonthlyMealFee other = (MonthlyMealFee) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public int getAbsentDay() {
		return this.absentDay;
	}

	public int getAbsentPerson() {
		return this.absentPerson;
	}

	public double getAbsentRefund() {
		return this.absentRefund;
	}

	public String getClassType() {
		return this.classType;
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

	public long getId() {
		return this.id;
	}

	public int getMonth() {
		return this.month;
	}

	public int getPersonTotal() {
		return this.personTotal;
	}

	public double getReceiptFund() {
		return this.receiptFund;
	}

	public int getSemester() {
		return this.semester;
	}

	public String getTenantId() {
		return this.tenantId;
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

	public MonthlyMealFee jsonToObject(JSONObject jsonObject) {
		return MonthlyMealFeeJsonFactory.jsonToObject(jsonObject);
	}

	public void setAbsentDay(int absentDay) {
		this.absentDay = absentDay;
	}

	public void setAbsentPerson(int absentPerson) {
		this.absentPerson = absentPerson;
	}

	public void setAbsentRefund(double absentRefund) {
		this.absentRefund = absentRefund;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setPersonTotal(int personTotal) {
		this.personTotal = personTotal;
	}

	public void setReceiptFund(double receiptFund) {
		this.receiptFund = receiptFund;
	}

	public void setSemester(int semester) {
		this.semester = semester;
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

	public void setYear(int year) {
		this.year = year;
	}

	public JSONObject toJsonObject() {
		return MonthlyMealFeeJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return MonthlyMealFeeJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
