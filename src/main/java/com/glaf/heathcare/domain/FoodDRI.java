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
@Table(name = "HEALTH_FOOD_DRI")
public class FoodDRI implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", nullable = false)
	protected long id;

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
	 * 年龄
	 */
	@Column(name = "AGE_")
	protected int age;

	/**
	 * 类别码
	 */
	@Column(name = "TYPE_", length = 50)
	protected String type;

	/**
	 * 餐点类别编号
	 */
	@Column(name = "TYPEID_")
	protected long typeId;

	/**
	 * 热能
	 */
	@Column(name = "HEATENERGY_")
	protected double heatEnergy;

	/**
	 * 蛋白质
	 */
	@Column(name = "PROTEIN_")
	protected double protein;

	/**
	 * 脂肪
	 */
	@Column(name = "FAT_")
	protected double fat;

	/**
	 * 碳水化合物
	 */
	@Column(name = "CARBOHYDRATE_")
	protected double carbohydrate;

	/**
	 * 微生素A
	 */
	@Column(name = "VITAMINA_")
	protected double vitaminA;

	/**
	 * 微生素B1
	 */
	@Column(name = "VITAMINB1_")
	protected double vitaminB1;

	/**
	 * 微生素B2
	 */
	@Column(name = "VITAMINB2_")
	protected double vitaminB2;

	/**
	 * 微生素B6
	 */
	@Column(name = "VITAMINB6_")
	protected double vitaminB6;

	/**
	 * 微生素B12
	 */
	@Column(name = "VITAMINB12_")
	protected double vitaminB12;

	/**
	 * 微生素C
	 */
	@Column(name = "VITAMINC_")
	protected double vitaminC;

	/**
	 * 微生素E
	 */
	@Column(name = "VITAMINE_")
	protected double vitaminE;

	/**
	 * 胡萝卜素
	 */
	@Column(name = "CAROTENE_")
	protected double carotene;

	/**
	 * 视黄醇
	 */
	@Column(name = "RETINOL_")
	protected double retinol;

	/**
	 * 尼克酸
	 */
	@Column(name = "NICOTINICCID_")
	protected double nicotinicCid;

	/**
	 * 钙
	 */
	@Column(name = "CALCIUM_")
	protected double calcium;

	/**
	 * 铁
	 */
	@Column(name = "IRON_")
	protected double iron;

	/**
	 * 锌
	 */
	@Column(name = "ZINC_")
	protected double zinc;

	/**
	 * 碘
	 */
	@Column(name = "IODINE_")
	protected double iodine;

	/**
	 * 磷
	 */
	@Column(name = "PHOSPHORUS_")
	protected double phosphorus;

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

	public FoodDRI() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FoodDRI other = (FoodDRI) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public int getAge() {
		return this.age;
	}

	public double getCalcium() {
		return this.calcium;
	}

	public double getCarbohydrate() {
		return this.carbohydrate;
	}

	public double getCarotene() {
		return this.carotene;
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

	public double getFat() {
		return this.fat;
	}

	public double getHeatEnergy() {
		return this.heatEnergy;
	}

	public long getId() {
		return this.id;
	}

	public double getIodine() {
		return iodine;
	}

	public double getIron() {
		return this.iron;
	}

	public String getName() {
		return this.name;
	}

	public double getNicotinicCid() {
		return this.nicotinicCid;
	}

	public double getPhosphorus() {
		return phosphorus;
	}

	public double getProtein() {
		return this.protein;
	}

	public double getRetinol() {
		return this.retinol;
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

	public double getVitaminA() {
		return this.vitaminA;
	}

	public double getVitaminB1() {
		return this.vitaminB1;
	}

	public double getVitaminB12() {
		return this.vitaminB12;
	}

	public double getVitaminB2() {
		return this.vitaminB2;
	}

	public double getVitaminB6() {
		return this.vitaminB6;
	}

	public double getVitaminC() {
		return this.vitaminC;
	}

	public double getVitaminE() {
		return vitaminE;
	}

	public double getZinc() {
		return this.zinc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public FoodDRI jsonToObject(JSONObject jsonObject) {
		return FoodDRIJsonFactory.jsonToObject(jsonObject);
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setCalcium(double calcium) {
		this.calcium = calcium;
	}

	public void setCarbohydrate(double carbohydrate) {
		this.carbohydrate = carbohydrate;
	}

	public void setCarotene(double carotene) {
		this.carotene = carotene;
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

	public void setFat(double fat) {
		this.fat = fat;
	}

	public void setHeatEnergy(double heatEnergy) {
		this.heatEnergy = heatEnergy;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setIodine(double iodine) {
		this.iodine = iodine;
	}

	public void setIron(double iron) {
		this.iron = iron;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNicotinicCid(double nicotinicCid) {
		this.nicotinicCid = nicotinicCid;
	}

	public void setPhosphorus(double phosphorus) {
		this.phosphorus = phosphorus;
	}

	public void setProtein(double protein) {
		this.protein = protein;
	}

	public void setRetinol(double retinol) {
		this.retinol = retinol;
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

	public void setVitaminA(double vitaminA) {
		this.vitaminA = vitaminA;
	}

	public void setVitaminB1(double vitaminB1) {
		this.vitaminB1 = vitaminB1;
	}

	public void setVitaminB12(double vitaminB12) {
		this.vitaminB12 = vitaminB12;
	}

	public void setVitaminB2(double vitaminB2) {
		this.vitaminB2 = vitaminB2;
	}

	public void setVitaminB6(double vitaminB6) {
		this.vitaminB6 = vitaminB6;
	}

	public void setVitaminC(double vitaminC) {
		this.vitaminC = vitaminC;
	}

	public void setVitaminE(double vitaminE) {
		this.vitaminE = vitaminE;
	}

	public void setZinc(double zinc) {
		this.zinc = zinc;
	}

	public JSONObject toJsonObject() {
		return FoodDRIJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return FoodDRIJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
