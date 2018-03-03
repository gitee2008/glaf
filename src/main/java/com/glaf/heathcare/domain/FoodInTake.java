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
 * 每日进食量表
 *
 */

@Entity
@Table(name = "HEALTH_FOOD_INTAKE")
public class FoodInTake implements Serializable, JSONable {
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
	 * 食物编号
	 */
	@Column(name = "FOODID_")
	protected long foodId;

	/**
	 * 食物名称
	 */
	@Column(name = "FOODNAME_", length = 200)
	protected String foodName;

	/**
	 * 食物分类编号
	 */
	@Column(name = "FOODNODEID_")
	protected long foodNodeId;

	/**
	 * 餐点类型
	 */
	@Column(name = "TYPEID_")
	protected long typeId;

	/**
	 * 用餐时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MEALTIME_")
	protected Date mealTime;

	/**
	 * 就餐人数
	 */
	@Column(name = "PERSON_")
	protected int person;

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
	 * 月
	 */
	@Column(name = "MONTH_")
	protected int month;

	/**
	 * 年月日
	 */
	@Column(name = "FULLDAY_")
	protected int fullDay;

	/**
	 * 分配熟食量
	 */
	@Column(name = "ALLOCATIONWEIGHT_")
	protected double allocationWeight;

	/**
	 * 剩余熟食量
	 */
	@Column(name = "REMAINWEIGHT_")
	protected double remainWeight;

	/**
	 * 摄入生重量
	 */
	@Column(name = "MEALWEIGHT_")
	protected double mealWeight;

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

	public FoodInTake() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FoodInTake other = (FoodInTake) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public double getAllocationWeight() {
		return this.allocationWeight;
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

	public long getFoodId() {
		return this.foodId;
	}

	public String getFoodName() {
		return this.foodName;
	}

	public long getFoodNodeId() {
		return this.foodNodeId;
	}

	public int getFullDay() {
		return this.fullDay;
	}

	public String getGradeId() {
		return gradeId;
	}

	public long getId() {
		return this.id;
	}

	public Date getMealTime() {
		return this.mealTime;
	}

	public String getMealTimeString() {
		if (this.mealTime != null) {
			return DateUtils.getDateTime(this.mealTime);
		}
		return "";
	}

	public double getMealWeight() {
		return this.mealWeight;
	}

	public int getMonth() {
		return this.month;
	}

	public int getPerson() {
		return this.person;
	}

	public double getRemainWeight() {
		return this.remainWeight;
	}

	public int getSemester() {
		return this.semester;
	}

	public String getTenantId() {
		return this.tenantId;
	}

	public long getTypeId() {
		return this.typeId;
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

	public FoodInTake jsonToObject(JSONObject jsonObject) {
		return FoodInTakeJsonFactory.jsonToObject(jsonObject);
	}

	public void setAllocationWeight(double allocationWeight) {
		this.allocationWeight = allocationWeight;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setFoodId(long foodId) {
		this.foodId = foodId;
	}

	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}

	public void setFoodNodeId(long foodNodeId) {
		this.foodNodeId = foodNodeId;
	}

	public void setFullDay(int fullDay) {
		this.fullDay = fullDay;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setMealTime(Date mealTime) {
		this.mealTime = mealTime;
	}

	public void setMealWeight(double mealWeight) {
		this.mealWeight = mealWeight;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setPerson(int person) {
		this.person = person;
	}

	public void setRemainWeight(double remainWeight) {
		this.remainWeight = remainWeight;
	}

	public void setSemester(int semester) {
		this.semester = semester;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public JSONObject toJsonObject() {
		return FoodInTakeJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return FoodInTakeJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
