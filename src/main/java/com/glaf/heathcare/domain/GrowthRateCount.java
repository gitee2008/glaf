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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.core.base.JSONable;
import com.glaf.heathcare.util.GrowthRateCountJsonFactory;

@Entity
@Table(name = "HEALTH_GROWTH_RATE_COUNT")
public class GrowthRateCount implements Serializable, JSONable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", nullable = false)
	protected long id;

	@Column(name = "CHECKID_", length = 50)
	protected String checkId;

	@Column(name = "TENANTID_", length = 50)
	protected String tenantId;

	/**
	 * 班级编号
	 */
	@Column(name = "GRADEID_", length = 50)
	protected String gradeId;

	@Column(name = "TYPE_", length = 50)
	protected String type;

	/**
	 * 增长人数
	 */
	@Column(name = "INCREASE_")
	protected int increase;

	/**
	 * 增长率
	 */
	@Column(name = "INCREASEPERCENT_")
	protected double increasePercent;

	/**
	 * 达标人数/合格人数
	 */
	@Column(name = "STANDARD_")
	protected int standard;

	/**
	 * 达标率/合格率
	 */
	@Column(name = "STANDARDPERCENT_")
	protected double standardPercent;

	public GrowthRateCount() {

	}

	public String getCheckId() {
		return checkId;
	}

	public String getGradeId() {
		return gradeId;
	}

	public long getId() {
		return id;
	}

	public int getIncrease() {
		return increase;
	}

	public double getIncreasePercent() {
		if (increasePercent > 0) {
			increasePercent = Math.round(increasePercent * 100D) / 100D;
		}
		return increasePercent;
	}

	public int getStandard() {
		return standard;
	}

	public double getStandardPercent() {
		if (standardPercent > 0) {
			standardPercent = Math.round(standardPercent * 100D) / 100D;
		}
		return standardPercent;
	}

	public String getTenantId() {
		return tenantId;
	}

	public String getType() {
		return type;
	}

	public GrowthRateCount jsonToObject(JSONObject jsonObject) {
		return GrowthRateCountJsonFactory.jsonToObject(jsonObject);
	}

	public void setCheckId(String checkId) {
		this.checkId = checkId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setIncrease(int increase) {
		this.increase = increase;
	}

	public void setIncreasePercent(double increasePercent) {
		this.increasePercent = increasePercent;
	}

	public void setStandard(int standard) {
		this.standard = standard;
	}

	public void setStandardPercent(double standardPercent) {
		this.standardPercent = standardPercent;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setType(String type) {
		this.type = type;
	}

	public JSONObject toJsonObject() {
		return GrowthRateCountJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return GrowthRateCountJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
