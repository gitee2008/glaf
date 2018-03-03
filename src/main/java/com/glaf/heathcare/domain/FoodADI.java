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
 * 每日食物参考摄入量
 *
 */

@Entity
@Table(name = "HEALTH_FOOD_ADI")
public class FoodADI implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", nullable = false)
	protected long id;

	/**
	 * 食物分类
	 */
	@Column(name = "NODEID_")
	protected long nodeId;

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
	 * 最低推荐量
	 */
	@Column(name = "LOWEST_")
	protected double lowest;

	/**
	 * 平均值
	 */
	@Column(name = "AVERAGE_")
	protected double average;

	/**
	 * 最高推荐量
	 */
	@Column(name = "HIGHEST_")
	protected double highest;

	/**
	 * 年龄段
	 */
	@Column(name = "AGEGROUP_", length = 50)
	protected String ageGroup;

	/**
	 * 类别码
	 */
	@Column(name = "TYPE_", length = 50)
	protected String type;

	/**
	 * 类别编号
	 */
	@Column(name = "TYPEID_")
	protected long typeId;

	/**
	 * 排序号
	 */
	@Column(name = "SORTNO_")
	protected int sortNo;

	/**
	 * 是否有效
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

	public FoodADI() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FoodADI other = (FoodADI) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public String getAgeGroup() {
		return this.ageGroup;
	}

	public Double getAverage() {
		return this.average;
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

	public String getEnableFlag() {
		return this.enableFlag;
	}

	public double getHighest() {
		return this.highest;
	}

	public long getId() {
		return this.id;
	}

	public double getLowest() {
		return this.lowest;
	}

	public String getName() {
		return this.name;
	}

	public long getNodeId() {
		return nodeId;
	}

	public int getSortNo() {
		return this.sortNo;
	}

	public String getType() {
		return this.type;
	}

	public long getTypeId() {
		return this.typeId;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public FoodADI jsonToObject(JSONObject jsonObject) {
		return FoodADIJsonFactory.jsonToObject(jsonObject);
	}

	public void setAgeGroup(String ageGroup) {
		this.ageGroup = ageGroup;
	}

	public void setAverage(double average) {
		this.average = average;
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

	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
	}

	public void setHighest(double highest) {
		this.highest = highest;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setLowest(double lowest) {
		this.lowest = lowest;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNodeId(long nodeId) {
		this.nodeId = nodeId;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public JSONObject toJsonObject() {
		return FoodADIJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return FoodADIJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
