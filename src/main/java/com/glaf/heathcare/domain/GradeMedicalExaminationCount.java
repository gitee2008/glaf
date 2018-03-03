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
import com.glaf.heathcare.util.GradeMedicalExaminationCountJsonFactory;

@Entity
@Table(name = "HEALTH_MEDICAL_EXAMINATIONCOUNT")
public class GradeMedicalExaminationCount implements Serializable, JSONable {

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

	/**
	 * 班级名称
	 */
	@javax.persistence.Transient
	protected String gradeName;

	/**
	 * 女生人数
	 */
	@Column(name = "FEMALE_")
	protected int female;

	/**
	 * 男生人数
	 */
	@Column(name = "MALE_")
	protected int male;

	/**
	 * 人数合计
	 */
	@Column(name = "PERSONCOUNT_")
	protected int personCount;

	/**
	 * 体检人数
	 */
	@Column(name = "CHECKPERSON_")
	protected int checkPerson;

	/**
	 * 体检率
	 */
	@Column(name = "CHECKPERCENT_")
	protected double checkPercent;

	/**
	 * 体重/年龄
	 */
	@javax.persistence.Transient
	protected PhysicalGrowthCount waCount;

	/**
	 * 身高/年龄
	 */
	@javax.persistence.Transient
	protected PhysicalGrowthCount haCount;

	/**
	 * 体重/身高
	 */
	@javax.persistence.Transient
	protected PhysicalGrowthCount whCount;

	/**
	 * 生长速度 实比人数
	 */
	@Column(name = "GROWTHRATEPERSON_")
	protected int growthRatePerson;

	/**
	 * 生长速度 实比率
	 */
	@Column(name = "GROWTHRATEPERSONPERCENT_")
	protected double growthRatePersonPercent;

	/**
	 * 体重
	 */
	@javax.persistence.Transient
	protected GrowthRateCount weightRate;

	/**
	 * 身高
	 */
	@javax.persistence.Transient
	protected GrowthRateCount heightRate;

	/**
	 * 双增
	 */
	@javax.persistence.Transient
	protected GrowthRateCount bothRate;

	/**
	 * 低体重人数
	 */
	@Column(name = "WEIGHTLOW_")
	protected int weightLow;

	/**
	 * 消瘦人数
	 */
	@Column(name = "WEIGHTSKINNY_")
	protected int weightSkinny;

	/**
	 * 发育迟缓人数
	 */
	@Column(name = "WEIGHTRETARDATION_")
	protected int weightRetardation;

	/**
	 * 严重营养不良
	 */
	@Column(name = "WEIGHTSEVEREMALNUTRITION_")
	protected int weightSevereMalnutrition;

	/**
	 * 超重人数
	 */
	@Column(name = "OVERWEIGHT_")
	protected int overWeight;

	/**
	 * 轻度肥胖人数
	 */
	@Column(name = "WEIGHTOBESITYLOW_")
	protected int weightObesityLow;

	/**
	 * 中度肥胖人数
	 */
	@Column(name = "WEIGHTOBESITYMID_")
	protected int weightObesityMid;

	/**
	 * 重度肥胖人数
	 */
	@Column(name = "WEIGHTOBESITYHIGH_")
	protected int weightObesityHigh;

	/**
	 * 贫血人数检查人数
	 */
	@Column(name = "ANEMIACHECK_")
	protected int anemiaCheck;

	/**
	 * 贫血人数检查正常人数
	 */
	@Column(name = "ANEMIACHECKNORMAL_")
	protected int anemiaCheckNormal;

	/**
	 * 贫血人数检查正常率
	 */
	@Column(name = "ANEMIACHECKNORMALPERCENT_")
	protected double anemiaCheckNormalPercent;

	/**
	 * 轻度贫血人数
	 */
	@Column(name = "ANEMIALOW_")
	protected int anemiaLow;

	/**
	 * 中度贫血人数
	 */
	@Column(name = "ANEMIAMID_")
	protected int anemiaMid;

	/**
	 * 重度贫血人数
	 */
	@Column(name = "ANEMIAHIGH_")
	protected int anemiaHigh;

	/**
	 * 血铅人数
	 */
	@Column(name = "BLOODLEAD_")
	protected int bloodLead;

	/**
	 * 年
	 */
	@Column(name = "YEAR_")
	protected int year;

	/**
	 * 学期
	 */
	@Column(name = "SEMESTER_")
	protected int semester;

	/**
	 * 年月日
	 */
	@Column(name = "FULLDAY_")
	protected int fullDay;

	/**
	 * 序号
	 */
	@Column(name = "SORTNO_")
	protected int sortNo;

	public GradeMedicalExaminationCount() {

	}

	public int getAnemiaCheck() {
		return anemiaCheck;
	}

	public int getAnemiaCheckNormal() {
		return anemiaCheckNormal;
	}

	public double getAnemiaCheckNormalPercent() {
		if (anemiaCheckNormalPercent > 0) {
			anemiaCheckNormalPercent = Math.round(anemiaCheckNormalPercent * 100D) / 100D;
		}
		return anemiaCheckNormalPercent;
	}

	public int getAnemiaHigh() {
		return anemiaHigh;
	}

	public int getAnemiaLow() {
		return anemiaLow;
	}

	public int getAnemiaMid() {
		return anemiaMid;
	}

	public int getBloodLead() {
		return bloodLead;
	}

	public GrowthRateCount getBothRate() {
		return bothRate;
	}

	public String getCheckId() {
		return checkId;
	}

	public double getCheckPercent() {
		if (checkPercent > 0) {
			checkPercent = Math.round(checkPercent * 100D) / 100D;
		}
		return checkPercent;
	}

	public int getCheckPerson() {
		return checkPerson;
	}

	public int getFemale() {
		return female;
	}

	public int getFullDay() {
		return fullDay;
	}

	public String getGradeId() {
		return gradeId;
	}

	public String getGradeName() {
		return gradeName;
	}

	public int getGrowthRatePerson() {
		return growthRatePerson;
	}

	public double getGrowthRatePersonPercent() {
		if (growthRatePersonPercent > 0) {
			growthRatePersonPercent = Math.round(growthRatePersonPercent * 100D) / 100D;
		}
		return growthRatePersonPercent;
	}

	public PhysicalGrowthCount getHaCount() {
		return haCount;
	}

	public GrowthRateCount getHeightRate() {
		return heightRate;
	}

	public long getId() {
		return id;
	}

	public int getMale() {
		return male;
	}

	public int getOverWeight() {
		return overWeight;
	}

	public int getPersonCount() {
		return personCount;
	}

	public int getSemester() {
		return semester;
	}

	public int getSortNo() {
		return sortNo;
	}

	public String getTenantId() {
		return tenantId;
	}

	public PhysicalGrowthCount getWaCount() {
		return waCount;
	}

	public int getWeightLow() {
		return weightLow;
	}

	public int getWeightObesityHigh() {
		return weightObesityHigh;
	}

	public int getWeightObesityLow() {
		return weightObesityLow;
	}

	public int getWeightObesityMid() {
		return weightObesityMid;
	}

	public GrowthRateCount getWeightRate() {
		return weightRate;
	}

	public int getWeightRetardation() {
		return weightRetardation;
	}

	public int getWeightSevereMalnutrition() {
		return weightSevereMalnutrition;
	}

	public int getWeightSkinny() {
		return weightSkinny;
	}

	public PhysicalGrowthCount getWhCount() {
		return whCount;
	}

	public int getYear() {
		return year;
	}

	public GradeMedicalExaminationCount jsonToObject(JSONObject jsonObject) {
		return GradeMedicalExaminationCountJsonFactory.jsonToObject(jsonObject);
	}

	public void setAnemiaCheck(int anemiaCheck) {
		this.anemiaCheck = anemiaCheck;
	}

	public void setAnemiaCheckNormal(int anemiaCheckNormal) {
		this.anemiaCheckNormal = anemiaCheckNormal;
	}

	public void setAnemiaCheckNormalPercent(double anemiaCheckNormalPercent) {
		this.anemiaCheckNormalPercent = anemiaCheckNormalPercent;
	}

	public void setAnemiaHigh(int anemiaHigh) {
		this.anemiaHigh = anemiaHigh;
	}

	public void setAnemiaLow(int anemiaLow) {
		this.anemiaLow = anemiaLow;
	}

	public void setAnemiaMid(int anemiaMid) {
		this.anemiaMid = anemiaMid;
	}

	public void setBloodLead(int bloodLead) {
		this.bloodLead = bloodLead;
	}

	public void setBothRate(GrowthRateCount bothRate) {
		this.bothRate = bothRate;
	}

	public void setCheckId(String checkId) {
		this.checkId = checkId;
	}

	public void setCheckPercent(double checkPercent) {
		this.checkPercent = checkPercent;
	}

	public void setCheckPerson(int checkPerson) {
		this.checkPerson = checkPerson;
	}

	public void setFemale(int female) {
		this.female = female;
	}

	public void setFullDay(int fullDay) {
		this.fullDay = fullDay;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public void setGrowthRatePerson(int growthRatePerson) {
		this.growthRatePerson = growthRatePerson;
	}

	public void setGrowthRatePersonPercent(double growthRatePersonPercent) {
		this.growthRatePersonPercent = growthRatePersonPercent;
	}

	public void setHaCount(PhysicalGrowthCount haCount) {
		this.haCount = haCount;
	}

	public void setHeightRate(GrowthRateCount heightRate) {
		this.heightRate = heightRate;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setMale(int male) {
		this.male = male;
	}

	public void setOverWeight(int overWeight) {
		this.overWeight = overWeight;
	}

	public void setPersonCount(int personCount) {
		this.personCount = personCount;
	}

	public void setSemester(int semester) {
		this.semester = semester;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setWaCount(PhysicalGrowthCount waCount) {
		this.waCount = waCount;
	}

	public void setWeightLow(int weightLow) {
		this.weightLow = weightLow;
	}

	public void setWeightObesityHigh(int weightObesityHigh) {
		this.weightObesityHigh = weightObesityHigh;
	}

	public void setWeightObesityLow(int weightObesityLow) {
		this.weightObesityLow = weightObesityLow;
	}

	public void setWeightObesityMid(int weightObesityMid) {
		this.weightObesityMid = weightObesityMid;
	}

	public void setWeightRate(GrowthRateCount weightRate) {
		this.weightRate = weightRate;
	}

	public void setWeightRetardation(int weightRetardation) {
		this.weightRetardation = weightRetardation;
	}

	public void setWeightSevereMalnutrition(int weightSevereMalnutrition) {
		this.weightSevereMalnutrition = weightSevereMalnutrition;
	}

	public void setWeightSkinny(int weightSkinny) {
		this.weightSkinny = weightSkinny;
	}

	public void setWhCount(PhysicalGrowthCount whCount) {
		this.whCount = whCount;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public JSONObject toJsonObject() {
		return GradeMedicalExaminationCountJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return GradeMedicalExaminationCountJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
