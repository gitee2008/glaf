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
 * 膳食调查
 *
 */

@Entity
@Table(name = "HEALTH_DIETARY_SURVEY")
public class DietarySurvey implements Serializable, JSONable {
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
	 * 生重
	 */
	@Column(name = "WEIGHT_")
	protected double weight;

	/**
	 * 生重比例
	 */
	@Column(name = "WEIGHTPERCENT_")
	protected double weightPercent;

	/**
	 * 熟重
	 */
	@Column(name = "COOKEDWEIGHT_")
	protected double cookedWeight;

	/**
	 * 生熟重比例
	 */
	@Column(name = "WEIGHTCOOKEDPERCENT_")
	protected double weightCookedPercent;

	/**
	 * 单价
	 */
	@Column(name = "PRICE_")
	protected double price;

	/**
	 * 总价格
	 */
	@Column(name = "TOTALPRICE_")
	protected double totalPrice;

	/**
	 * 备注
	 */
	@Column(name = "REMARK_", length = 4000)
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

	public DietarySurvey() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DietarySurvey other = (DietarySurvey) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public double getCookedWeight() {
		if (cookedWeight > 0) {
			cookedWeight = Math.round(cookedWeight * 10D) / 10D;
		}
		return this.cookedWeight;
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

	public int getMonth() {
		return this.month;
	}

	public double getPrice() {
		if (price > 0) {
			price = Math.round(price * 100D) / 100D;
		}
		return this.price;
	}

	public String getRemark() {
		return this.remark;
	}

	public int getSemester() {
		return this.semester;
	}

	public String getTenantId() {
		return this.tenantId;
	}

	public double getTotalPrice() {
		if (totalPrice > 0) {
			totalPrice = Math.round(totalPrice * 100D) / 100D;
		}
		return this.totalPrice;
	}

	public long getTypeId() {
		return this.typeId;
	}

	public double getWeight() {
		if (weight > 0) {
			weight = Math.round(weight * 10D) / 10D;
		}
		return this.weight;
	}

	public double getWeightCookedPercent() {
		if (weightCookedPercent > 0) {
			weightCookedPercent = Math.round(weightCookedPercent * 100D) / 100D;
		}
		return this.weightCookedPercent;
	}

	public double getWeightPercent() {
		if (weightPercent > 0) {
			weightPercent = Math.round(weightPercent * 100D) / 100D;
		}
		return this.weightPercent;
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

	public DietarySurvey jsonToObject(JSONObject jsonObject) {
		return DietarySurveyJsonFactory.jsonToObject(jsonObject);
	}

	public void setCookedWeight(double cookedWeight) {
		this.cookedWeight = cookedWeight;
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

	public void setId(long id) {
		this.id = id;
	}

	public void setMealTime(Date mealTime) {
		this.mealTime = mealTime;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setPrice(double price) {
		this.price = price;
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

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public void setWeightCookedPercent(double weightCookedPercent) {
		this.weightCookedPercent = weightCookedPercent;
	}

	public void setWeightPercent(double weightPercent) {
		this.weightPercent = weightPercent;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public JSONObject toJsonObject() {
		return DietarySurveyJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return DietarySurveyJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
