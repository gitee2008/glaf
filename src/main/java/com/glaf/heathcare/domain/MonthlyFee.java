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
 * 月度费用
 *
 */

@Entity
@Table(name = "HEALTH_MONTHLY_FEE")
public class MonthlyFee implements Serializable, JSONable {
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
	 * 上月累计结余
	 */
	@Column(name = "LASTMONTHSURPLUS_")
	protected double lastMonthSurplus;

	/**
	 * 本月结余
	 */
	@Column(name = "MONTHLEFT_")
	protected double monthLeft;

	/**
	 * 本月累计结余
	 */
	@Column(name = "MONTHTOTALLEFT_")
	protected double monthTotalLeft;

	/**
	 * 本月结余比例
	 */
	@Column(name = "LEFTPERCENT_")
	protected double leftPercent;

	/**
	 * 本月超支比例
	 */
	@Column(name = "EXCEEDPERCENT_")
	protected double exceedPercent;

	/**
	 * 本月人均膳费
	 */
	@Column(name = "PERSONMONTHLYFEE_")
	protected double personMonthlyFee;

	/**
	 * 燃料费
	 */
	@Column(name = "FUELFEE_")
	protected double fuelFee;

	/**
	 * 人工费
	 */
	@Column(name = "LABORFEE_")
	protected double laborFee;

	/**
	 * 外购点心费用
	 */
	@Column(name = "DESSERTFEE_")
	protected double dessertFee;

	/**
	 * 其他费
	 */
	@Column(name = "OTHERFEE_")
	protected double otherFee;

	/**
	 * 出勤天数
	 */
	@Column(name = "WORKDAY_")
	protected int workDay;

	/**
	 * 总就餐人日数
	 */
	@Column(name = "TOTALREPASTPERSON_")
	protected int totalRepastPerson;

	/**
	 * 备注
	 */
	@Column(name = "REMARK_", length = 4000)
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

	public MonthlyFee() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MonthlyFee other = (MonthlyFee) obj;
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

	public double getDessertFee() {
		return this.dessertFee;
	}

	public double getExceedPercent() {
		return this.exceedPercent;
	}

	public double getFuelFee() {
		return this.fuelFee;
	}

	public long getId() {
		return this.id;
	}

	public double getLaborFee() {
		return this.laborFee;
	}

	public double getLastMonthSurplus() {
		return lastMonthSurplus;
	}

	public double getLeftPercent() {
		return this.leftPercent;
	}

	public int getMonth() {
		return this.month;
	}

	public double getMonthLeft() {
		return this.monthLeft;
	}

	public double getMonthTotalLeft() {
		return this.monthTotalLeft;
	}

	public double getOtherFee() {
		return this.otherFee;
	}

	public double getPersonMonthlyFee() {
		return this.personMonthlyFee;
	}

	public String getRemark() {
		return remark;
	}

	public int getSemester() {
		return this.semester;
	}

	public String getTenantId() {
		return this.tenantId;
	}

	public int getTotalRepastPerson() {
		return this.totalRepastPerson;
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

	public int getWorkDay() {
		return this.workDay;
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

	public MonthlyFee jsonToObject(JSONObject jsonObject) {
		return MonthlyFeeJsonFactory.jsonToObject(jsonObject);
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setDessertFee(double dessertFee) {
		this.dessertFee = dessertFee;
	}

	public void setExceedPercent(double exceedPercent) {
		this.exceedPercent = exceedPercent;
	}

	public void setFuelFee(double fuelFee) {
		this.fuelFee = fuelFee;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setLaborFee(double laborFee) {
		this.laborFee = laborFee;
	}

	public void setLastMonthSurplus(double lastMonthSurplus) {
		this.lastMonthSurplus = lastMonthSurplus;
	}

	public void setLeftPercent(double leftPercent) {
		this.leftPercent = leftPercent;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setMonthLeft(double monthLeft) {
		this.monthLeft = monthLeft;
	}

	public void setMonthTotalLeft(double monthTotalLeft) {
		this.monthTotalLeft = monthTotalLeft;
	}

	public void setOtherFee(double otherFee) {
		this.otherFee = otherFee;
	}

	public void setPersonMonthlyFee(double personMonthlyFee) {
		this.personMonthlyFee = personMonthlyFee;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setSemester(int semester) {
		this.semester = semester;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setTotalRepastPerson(int totalRepastPerson) {
		this.totalRepastPerson = totalRepastPerson;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public void setWorkDay(int workDay) {
		this.workDay = workDay;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public JSONObject toJsonObject() {
		return MonthlyFeeJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return MonthlyFeeJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
