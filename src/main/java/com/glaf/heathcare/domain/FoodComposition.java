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
 * 食物成分
 *
 */

@Entity
@Table(name = "HEALTH_FOOD_COMPOSITION")
public class FoodComposition implements Serializable, JSONable {
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
	 * 分类编号
	 */
	@Column(name = "NODEID_")
	protected long nodeId;

	/**
	 * 分类树编号
	 */
	@Column(name = "TREEID_", length = 200)
	protected String treeId;

	/**
	 * 名称
	 */
	@Column(name = "NAME_", length = 200)
	protected String name;

	/**
	 * 别名
	 */
	@Column(name = "ALIAS_", length = 200)
	protected String alias;

	/**
	 * 代码
	 */
	@Column(name = "CODE_", length = 50)
	protected String code;

	/**
	 * 识别码
	 */
	@Column(name = "DISCRIMINATOR_", length = 50)
	protected String discriminator;

	/**
	 * 描述
	 */
	@Column(name = "DESCRIPTION_", length = 4000)
	protected String description;

	/**
	 * 食部
	 */
	@Column(name = "RADICAL_")
	protected double radical;

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
	 * 铜(毫克mg)
	 */
	@Column(name = "COPPER_")
	protected double copper;

	/**
	 * 镁(毫克mg)
	 */
	@Column(name = "MAGNESIUM_")
	protected double magnesium;

	/**
	 * 锰(毫克mg)
	 */
	@Column(name = "MANGANESE_")
	protected double manganese;

	/**
	 * 钾(毫克mg)
	 */
	@Column(name = "POTASSIUM_")
	protected double potassium;

	/**
	 * 硒(微克ug)
	 */
	@Column(name = "SELENIUM_")
	protected double selenium;

	/**
	 * 排序号
	 */
	@Column(name = "SORTNO_")
	protected int sortNo;

	/**
	 * 每日采购
	 */
	@Column(name = "DAILYFLAG_", length = 1)
	protected String dailyFlag;

	/**
	 * 颜色标识
	 */
	@Column(name = "COLORFLAG_", length = 1)
	protected String colorFlag;

	/**
	 * 粮食分类
	 */
	@Column(name = "CEREALFLAG_", length = 1)
	protected String cerealFlag;

	/**
	 * 大豆分类
	 */
	@Column(name = "BEANSFLAG_", length = 1)
	protected String beansFlag;

	/**
	 * 是否有效
	 */
	@Column(name = "ENABLEFLAG_", length = 1)
	protected String enableFlag;

	/**
	 * 系统内置
	 */
	@Column(name = "SYSFLAG_", length = 1)
	protected String sysFlag;

	/**
	 * 是否已经使用
	 */
	@Column(name = "USEFLAG_", length = 1)
	protected String useFlag;

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

	@javax.persistence.Transient
	protected Date expiryDate;

	@javax.persistence.Transient
	protected double quantity;

	@javax.persistence.Transient
	protected String unit;

	public FoodComposition() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FoodComposition other = (FoodComposition) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public String getAlias() {
		return this.alias;
	}

	public String getBeansFlag() {
		return beansFlag;
	}

	public double getCalcium() {
		if (calcium > 0) {
			calcium = Math.round(calcium * 1000D) / 1000D;
		}
		return this.calcium;
	}

	public double getCarbohydrate() {
		if (carbohydrate > 0) {
			carbohydrate = Math.round(carbohydrate * 1000D) / 1000D;
		}
		return this.carbohydrate;
	}

	public double getCarotene() {
		if (carotene > 0) {
			carotene = Math.round(carotene * 1000D) / 1000D;
		}
		return this.carotene;
	}

	public String getCerealFlag() {
		return cerealFlag;
	}

	public String getCode() {
		return this.code;
	}

	public String getColorFlag() {
		return colorFlag;
	}

	public double getCopper() {
		if (copper > 0) {
			copper = Math.round(copper * 1000D) / 1000D;
		}
		return copper;
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

	public String getDailyFlag() {
		return dailyFlag;
	}

	public String getDescription() {
		return this.description;
	}

	public String getDiscriminator() {
		return this.discriminator;
	}

	public String getEnableFlag() {
		return this.enableFlag;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public double getFat() {
		if (fat > 0) {
			fat = Math.round(fat * 1000D) / 1000D;
		}
		return this.fat;
	}

	public double getHeatEnergy() {
		if (heatEnergy > 0) {
			heatEnergy = Math.round(heatEnergy * 10D) / 10D;
		}
		return this.heatEnergy;
	}

	public long getId() {
		return this.id;
	}

	public double getIodine() {
		if (iodine > 0) {
			iodine = Math.round(iodine * 1000D) / 1000D;
		}
		return iodine;
	}

	public double getIron() {
		if (iron > 0) {
			iron = Math.round(iron * 1000D) / 1000D;
		}
		return this.iron;
	}

	public double getMagnesium() {
		if (magnesium > 0) {
			magnesium = Math.round(magnesium * 1000D) / 1000D;
		}
		return magnesium;
	}

	public double getManganese() {
		if (manganese > 0) {
			manganese = Math.round(manganese * 1000D) / 1000D;
		}
		return manganese;
	}

	public String getName() {
		return this.name;
	}

	public double getNicotinicCid() {
		if (nicotinicCid > 0) {
			nicotinicCid = Math.round(nicotinicCid * 1000D) / 1000D;
		}
		return this.nicotinicCid;
	}

	public long getNodeId() {
		return this.nodeId;
	}

	public double getPhosphorus() {
		if (phosphorus > 0) {
			phosphorus = Math.round(phosphorus * 1000D) / 1000D;
		}
		return phosphorus;
	}

	public double getPotassium() {
		if (potassium > 0) {
			potassium = Math.round(potassium * 1000D) / 1000D;
		}
		return potassium;
	}

	public double getProtein() {
		if (protein > 0) {
			protein = Math.round(protein * 1000D) / 1000D;
		}
		return this.protein;
	}

	public double getQuantity() {
		if (quantity > 0) {
			quantity = Math.round(quantity * 100D) / 100D;
		}
		return quantity;
	}

	public double getRadical() {
		if (radical > 0) {
			radical = Math.round(radical * 100D) / 100D;
		}
		return this.radical;
	}

	public double getRetinol() {
		if (retinol > 0) {
			retinol = Math.round(retinol * 1000D) / 1000D;
		}
		return this.retinol;
	}

	public double getSelenium() {
		if (selenium > 0) {
			selenium = Math.round(selenium * 1000D) / 1000D;
		}
		return selenium;
	}

	public int getSortNo() {
		return this.sortNo;
	}

	public String getSysFlag() {
		return sysFlag;
	}

	public String getTenantId() {
		return tenantId;
	}

	public String getTreeId() {
		return this.treeId;
	}

	public String getUnit() {
		return unit;
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

	public String getUseFlag() {
		return useFlag;
	}

	public double getVitaminA() {
		if (vitaminA > 0) {
			vitaminA = Math.round(vitaminA * 1000D) / 1000D;
		}
		return this.vitaminA;
	}

	public double getVitaminB1() {
		if (vitaminB1 > 0) {
			vitaminB1 = Math.round(vitaminB1 * 1000D) / 1000D;
		}
		return this.vitaminB1;
	}

	public double getVitaminB12() {
		if (vitaminB12 > 0) {
			vitaminB12 = Math.round(vitaminB12 * 1000D) / 1000D;
		}
		return this.vitaminB12;
	}

	public double getVitaminB2() {
		if (vitaminB2 > 0) {
			vitaminB2 = Math.round(vitaminB2 * 1000D) / 1000D;
		}
		return this.vitaminB2;
	}

	public double getVitaminB6() {
		if (vitaminB6 > 0) {
			vitaminB6 = Math.round(vitaminB6 * 1000D) / 1000D;
		}
		return this.vitaminB6;
	}

	public double getVitaminC() {
		if (vitaminC > 0) {
			vitaminC = Math.round(vitaminC * 1000D) / 1000D;
		}
		return this.vitaminC;
	}

	public double getVitaminE() {
		if (vitaminE > 0) {
			vitaminE = Math.round(vitaminE * 1000D) / 1000D;
		}
		return vitaminE;
	}

	public double getZinc() {
		if (zinc > 0) {
			zinc = Math.round(zinc * 1000D) / 1000D;
		}
		return this.zinc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public FoodComposition jsonToObject(JSONObject jsonObject) {
		return FoodCompositionJsonFactory.jsonToObject(jsonObject);
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public void setBeansFlag(String beansFlag) {
		this.beansFlag = beansFlag;
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

	public void setCerealFlag(String cerealFlag) {
		this.cerealFlag = cerealFlag;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setColorFlag(String colorFlag) {
		this.colorFlag = colorFlag;
	}

	public void setCopper(double copper) {
		this.copper = copper;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setDailyFlag(String dailyFlag) {
		this.dailyFlag = dailyFlag;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDiscriminator(String discriminator) {
		this.discriminator = discriminator;
	}

	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
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

	public void setMagnesium(double magnesium) {
		this.magnesium = magnesium;
	}

	public void setManganese(double manganese) {
		this.manganese = manganese;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNicotinicCid(double nicotinicCid) {
		this.nicotinicCid = nicotinicCid;
	}

	public void setNodeId(long nodeId) {
		this.nodeId = nodeId;
	}

	public void setPhosphorus(double phosphorus) {
		this.phosphorus = phosphorus;
	}

	public void setPotassium(double potassium) {
		this.potassium = potassium;
	}

	public void setProtein(double protein) {
		this.protein = protein;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public void setRadical(double radical) {
		this.radical = radical;
	}

	public void setRetinol(double retinol) {
		this.retinol = retinol;
	}

	public void setSelenium(double selenium) {
		this.selenium = selenium;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	public void setSysFlag(String sysFlag) {
		this.sysFlag = sysFlag;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setTreeId(String treeId) {
		this.treeId = treeId;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public void setUseFlag(String useFlag) {
		this.useFlag = useFlag;
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
		return FoodCompositionJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return FoodCompositionJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
