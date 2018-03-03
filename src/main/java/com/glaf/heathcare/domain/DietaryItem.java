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
@Table(name = "HEALTH_DIETARY_ITEM")
public class DietaryItem implements java.lang.Comparable<DietaryItem>, Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", nullable = false)
	protected long id;

	/**
	 * 食谱编号
	 */
	@Column(name = "DIETARYID_")
	protected long dietaryId;

	/**
	 * 模板编号
	 */
	@Column(name = "TEMPLATEID_")
	protected long templateId;

	/**
	 * 租户编号
	 */
	@Column(name = "TENANTID_", length = 50)
	protected String tenantId;

	/**
	 * 名称
	 */
	@Column(name = "NAME_", length = 200)
	protected String name;

	/**
	 * 描述
	 */
	@Column(name = "DESCRIPTION_", length = 4000)
	protected String description;

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
	 * 餐点类别编号
	 */
	@Column(name = "TYPEID_")
	protected long typeId;

	/**
	 * 数量
	 */
	@Column(name = "QUANTITY_")
	protected double quantity;

	@javax.persistence.Transient
	protected String quantity2;

	/**
	 * 计量单位，默认为克g
	 */
	@Column(name = "UNIT_", length = 20)
	protected String unit;

	/**
	 * 年月日
	 */
	@Column(name = "FULLDAY_")
	protected int fullDay;

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
	 * 表后缀
	 */
	@javax.persistence.Transient
	protected String tableSuffix;

	public DietaryItem() {

	}

	public int compareTo(DietaryItem o) {
		if (o == null) {
			return -1;
		}

		DietaryItem field = o;

		int l = (int) (this.quantity - field.getQuantity());

		int ret = 0;

		if (l > 0) {
			ret = -1;
		} else if (l < 0) {
			ret = 1;
		}
		return ret;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DietaryItem other = (DietaryItem) obj;
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

	public String getDescription() {
		return this.description;
	}

	public long getDietaryId() {
		return dietaryId;
	}

	public long getFoodId() {
		return this.foodId;
	}

	public String getFoodName() {
		return this.foodName;
	}

	public int getFullDay() {
		return fullDay;
	}

	public long getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public double getQuantity() {
		return this.quantity;
	}

	public String getQuantity2() {
		quantity = Math.round(quantity);
		return quantity > 0 ? String.valueOf((int)quantity) : "";
	}

	public String getTableSuffix() {
		if (tableSuffix == null) {
			tableSuffix = "";
		}
		return tableSuffix;
	}

	public long getTemplateId() {
		return this.templateId;
	}

	public String getTenantId() {
		return tenantId;
	}

	public long getTypeId() {
		return typeId;
	}

	public String getUnit() {
		return this.unit;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public DietaryItem jsonToObject(JSONObject jsonObject) {
		return DietaryItemJsonFactory.jsonToObject(jsonObject);
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDietaryId(long dietaryId) {
		this.dietaryId = dietaryId;
	}

	public void setFoodId(long foodId) {
		this.foodId = foodId;
	}

	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}

	public void setFullDay(int fullDay) {
		this.fullDay = fullDay;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public void setTableSuffix(String tableSuffix) {
		this.tableSuffix = tableSuffix;
	}

	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public JSONObject toJsonObject() {
		return DietaryItemJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return DietaryItemJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
