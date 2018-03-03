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
@Table(name = "HEALTH_DIETARY_STATISTICS")
public class DietaryStatistics implements Serializable, JSONable {
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
	 * 统计项名称
	 */
	@Column(name = "NAME_", length = 200)
	protected String name;

	/**
	 * 统计项标题
	 */
	@Column(name = "TITLE_", length = 200)
	protected String title;

	/**
	 * 统计项值
	 */
	@Column(name = "VALUE_")
	protected double value;

	/**
	 * 餐类型
	 */
	@Column(name = "MEALTYPE_", length = 50)
	protected String mealType;

	/**
	 * 系统内置
	 */
	@Column(name = "SYSFLAG_", length = 1)
	protected String sysFlag;

	/**
	 * 统计类型
	 */
	@Column(name = "TYPE_", length = 50)
	protected String type;

	/**
	 * 星期X
	 */
	@Column(name = "DAYOFWEEK_")
	protected int dayOfWeek;

	/**
	 * 周,第X周
	 */
	@Column(name = "WEEK_")
	protected int week;

	/**
	 * 学期
	 */
	@Column(name = "SEMESTER_")
	protected int semester;

	/**
	 * 年
	 */
	@Column(name = "YEAR_")
	protected int year;

	/**
	 * 批次号
	 */
	@Column(name = "BATCHNO_", length = 200)
	protected String batchNo;

	/**
	 * 顺序号
	 */
	@Column(name = "SORTNO_")
	protected int sortNo;

	/**
	 * 模板套编号
	 */
	@Column(name = "SUITNO_")
	protected int suitNo;

	/**
	 * 创建日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME_")
	protected Date createTime;

	public DietaryStatistics() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DietaryStatistics other = (DietaryStatistics) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public String getBatchNo() {
		return batchNo;
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

	public int getDayOfWeek() {
		return this.dayOfWeek;
	}

	public long getId() {
		return this.id;
	}

	public String getMealType() {
		return this.mealType;
	}

	public String getName() {
		return this.name;
	}

	public int getSemester() {
		return semester;
	}

	public int getSortNo() {
		return sortNo;
	}

	public int getSuitNo() {
		return this.suitNo;
	}

	public String getSysFlag() {
		return this.sysFlag;
	}

	public String getTenantId() {
		return this.tenantId;
	}

	public String getTitle() {
		return title;
	}

	public String getType() {
		return this.type;
	}

	public double getValue() {
		return this.value;
	}

	public int getWeek() {
		return week;
	}

	public int getYear() {
		return year;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public DietaryStatistics jsonToObject(JSONObject jsonObject) {
		return DietaryStatisticsJsonFactory.jsonToObject(jsonObject);
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setMealType(String mealType) {
		this.mealType = mealType;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSemester(int semester) {
		this.semester = semester;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	public void setSuitNo(int suitNo) {
		this.suitNo = suitNo;
	}

	public void setSysFlag(String sysFlag) {
		this.sysFlag = sysFlag;
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

	public void setValue(double value) {
		this.value = value;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public JSONObject toJsonObject() {
		return DietaryStatisticsJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return DietaryStatisticsJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
