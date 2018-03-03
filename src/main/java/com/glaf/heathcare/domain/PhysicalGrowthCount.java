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
import com.glaf.heathcare.util.PhysicalGrowthCountJsonFactory;

@Entity
@Table(name = "HEALTH_PHYSICAL_GROWTH_COUNT")
public class PhysicalGrowthCount implements Serializable, JSONable {

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
	 * 下
	 */
	@Column(name = "LEVEL1_")
	protected int level1;

	/**
	 * 中下
	 */
	@Column(name = "LEVEL2_")
	protected int level2;

	/**
	 * 中-
	 */
	@Column(name = "LEVEL3_")
	protected int level3;

	/**
	 * 中
	 */
	@Column(name = "LEVEL5_")
	protected int level5;

	/**
	 * 中+
	 */
	@Column(name = "LEVEL7_")
	protected int level7;

	/**
	 * 中上
	 */
	@Column(name = "LEVEL8_")
	protected int level8;

	/**
	 * 上
	 */
	@Column(name = "LEVEL9_")
	protected int level9;

	/**
	 * 下
	 */
	@Column(name = "LEVEL1PERCENT_")
	protected double level1Percent;

	/**
	 * 中下
	 */
	@Column(name = "LEVEL2PERCENT_")
	protected double level2Percent;

	/**
	 * 中-
	 */
	@Column(name = "LEVEL3PERCENT_")
	protected double level3Percent;

	/**
	 * 中
	 */
	@Column(name = "LEVEL5PERCENT_")
	protected double level5Percent;

	/**
	 * 中+
	 */
	@Column(name = "LEVEL7PERCENT_")
	protected double level7Percent;

	/**
	 * 中上
	 */
	@Column(name = "LEVEL8PERCENT_")
	protected double level8Percent;

	/**
	 * 上
	 */
	@Column(name = "LEVEL9PERCENT_")
	protected double level9Percent;

	/**
	 * 正常人数
	 */
	@Column(name = "NORMAL_")
	protected int normal;

	/**
	 * 正常率
	 */
	@Column(name = "NORMALPERCENT_")
	protected double normalPercent;

	public PhysicalGrowthCount() {

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

	public int getLevel1() {
		return level1;
	}

	public double getLevel1Percent() {
		if (level1Percent > 0) {
			level1Percent = Math.round(level1Percent * 100D) / 100D;
		}
		return level1Percent;
	}

	public int getLevel2() {
		return level2;
	}

	public double getLevel2Percent() {
		if (level2Percent > 0) {
			level2Percent = Math.round(level2Percent * 100D) / 100D;
		}
		return level2Percent;
	}

	public int getLevel3() {
		return level3;
	}

	public double getLevel3Percent() {
		if (level3Percent > 0) {
			level3Percent = Math.round(level3Percent * 100D) / 100D;
		}
		return level3Percent;
	}

	public int getLevel5() {
		return level5;
	}

	public double getLevel5Percent() {
		if (level5Percent > 0) {
			level5Percent = Math.round(level5Percent * 100D) / 100D;
		}
		return level5Percent;
	}

	public int getLevel7() {
		return level7;
	}

	public double getLevel7Percent() {
		if (level7Percent > 0) {
			level7Percent = Math.round(level7Percent * 100D) / 100D;
		}
		return level7Percent;
	}

	public int getLevel8() {
		return level8;
	}

	public double getLevel8Percent() {
		if (level8Percent > 0) {
			level8Percent = Math.round(level8Percent * 100D) / 100D;
		}
		return level8Percent;
	}

	public int getLevel9() {
		return level9;
	}

	public double getLevel9Percent() {
		if (level9Percent > 0) {
			level9Percent = Math.round(level9Percent * 100D) / 100D;
		}
		return level9Percent;
	}

	public int getNormal() {
		return normal;
	}

	public double getNormalPercent() {
		if (normalPercent > 0) {
			normalPercent = Math.round(normalPercent * 100D) / 100D;
		}
		return normalPercent;
	}

	public String getTenantId() {
		return tenantId;
	}

	public String getType() {
		return type;
	}

	public PhysicalGrowthCount jsonToObject(JSONObject jsonObject) {
		return PhysicalGrowthCountJsonFactory.jsonToObject(jsonObject);
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

	public void setLevel1(int level1) {
		this.level1 = level1;
	}

	public void setLevel1Percent(double level1Percent) {
		this.level1Percent = level1Percent;
	}

	public void setLevel2(int level2) {
		this.level2 = level2;
	}

	public void setLevel2Percent(double level2Percent) {
		this.level2Percent = level2Percent;
	}

	public void setLevel3(int level3) {
		this.level3 = level3;
	}

	public void setLevel3Percent(double level3Percent) {
		this.level3Percent = level3Percent;
	}

	public void setLevel5(int level5) {
		this.level5 = level5;
	}

	public void setLevel5Percent(double level5Percent) {
		this.level5Percent = level5Percent;
	}

	public void setLevel7(int level7) {
		this.level7 = level7;
	}

	public void setLevel7Percent(double level7Percent) {
		this.level7Percent = level7Percent;
	}

	public void setLevel8(int level8) {
		this.level8 = level8;
	}

	public void setLevel8Percent(double level8Percent) {
		this.level8Percent = level8Percent;
	}

	public void setLevel9(int level9) {
		this.level9 = level9;
	}

	public void setLevel9Percent(double level9Percent) {
		this.level9Percent = level9Percent;
	}

	public void setNormal(int normal) {
		this.normal = normal;
	}

	public void setNormalPercent(double normalPercent) {
		this.normalPercent = normalPercent;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setType(String type) {
		this.type = type;
	}

	public JSONObject toJsonObject() {
		return PhysicalGrowthCountJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return PhysicalGrowthCountJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
