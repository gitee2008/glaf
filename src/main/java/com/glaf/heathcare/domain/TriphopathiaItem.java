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
@Table(name = "HEALTH_TRIPHOPATHIA_ITEM")
public class TriphopathiaItem implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", nullable = false)
	protected long id;

	/**
	 * 疾病编号
	 */
	@Column(name = "TRIPHOPATHIAID_")
	protected long triphopathiaId;

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
	 * 学生编号
	 */
	@Column(name = "PERSONID_", length = 50)
	protected String personId;

	/**
	 * 姓名
	 */
	@Column(name = "NAME_", length = 100)
	protected String name;

	@javax.persistence.Transient
	protected Date birthday;

	/**
	 * 性别
	 */
	@Column(name = "SEX_", length = 2)
	protected String sex;

	/**
	 * 类别
	 */
	@Column(name = "TYPE_", length = 50)
	protected String type;

	/**
	 * 身高
	 */
	@Column(name = "HEIGHT_")
	protected double height;

	/**
	 * 身高等级
	 */
	@Column(name = "HEIGHTLEVEL_")
	protected int heightLevel;

	/**
	 * 身高评价
	 */
	@Column(name = "HEIGHTEVALUATE_", length = 200)
	protected String heightEvaluate;

	/**
	 * 体重
	 */
	@Column(name = "WEIGHT_")
	protected double weight;

	/**
	 * 体重等级
	 */
	@Column(name = "WEIGHTLEVEL_")
	protected int weightLevel;

	/**
	 * 体重评价
	 */
	@Column(name = "WEIGHTEVALUATE_", length = 200)
	protected String weightEvaluate;

	/**
	 * 身高别体重
	 */
	@Column(name = "WEIGHTHEIGHTPERCENT_")
	protected double weightHeightPercent;

	/**
	 * BMI
	 */
	@Column(name = "BMI_")
	protected double bmi;

	/**
	 * 体重指数
	 */
	@Column(name = "BMIINDEX_")
	protected double bmiIndex;

	/**
	 * 身高别体重评价
	 */
	@Column(name = "BMIEVALUATE_", length = 200)
	protected String bmiEvaluate;

	/**
	 * 主要症状
	 */
	@Column(name = "SYMPTOM_", length = 500)
	protected String symptom;

	/**
	 * 建议及意见
	 */
	@Column(name = "SUGGEST_", length = 500)
	protected String suggest;

	/**
	 * 心理行为及发育评估结果
	 */
	@Column(name = "RESULT_", length = 50)
	protected String result;

	/**
	 * 心理行为及发育评估意见
	 */
	@Column(name = "EVALUATE_", length = 500)
	protected String evaluate;

	/**
	 * 月龄
	 */
	@Column(name = "AGEOFTHEMOON_")
	protected int ageOfTheMoon;

	/**
	 * 备注
	 */
	@Column(name = "REMARK_", length = 500)
	protected String remark;

	/**
	 * 检查时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CHECKDATE_")
	protected Date checkDate;

	/**
	 * 保健医生
	 */
	@Column(name = "CHECKDOCTOR_", length = 50)
	protected String checkDoctor;

	/**
	 * 保健医生ID
	 */
	@Column(name = "CHECKDOCTORID_", length = 50)
	protected String checkDoctorId;

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

	public TriphopathiaItem() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TriphopathiaItem other = (TriphopathiaItem) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public int getAgeOfTheMoon() {
		return this.ageOfTheMoon;
	}

	public Date getBirthday() {
		return birthday;
	}

	public double getBmi() {
		return this.bmi;
	}

	public String getBmiEvaluate() {
		return this.bmiEvaluate;
	}

	public double getBmiIndex() {
		return this.bmiIndex;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public String getCheckDoctor() {
		return this.checkDoctor;
	}

	public String getCheckDoctorId() {
		return this.checkDoctorId;
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

	public String getEvaluate() {
		return this.evaluate;
	}

	public String getGradeId() {
		return this.gradeId;
	}

	public double getHeight() {
		return this.height;
	}

	public String getHeightEvaluate() {
		return this.heightEvaluate;
	}

	public int getHeightLevel() {
		return this.heightLevel;
	}

	public long getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getPersonId() {
		return this.personId;
	}

	public String getRemark() {
		return this.remark;
	}

	public String getResult() {
		return this.result;
	}

	public String getSex() {
		return this.sex;
	}

	public String getSuggest() {
		return this.suggest;
	}

	public String getSymptom() {
		return this.symptom;
	}

	public String getTenantId() {
		return this.tenantId;
	}

	public long getTriphopathiaId() {
		return triphopathiaId;
	}

	public String getType() {
		return this.type;
	}

	public double getWeight() {
		return this.weight;
	}

	public String getWeightEvaluate() {
		return this.weightEvaluate;
	}

	public double getWeightHeightPercent() {
		return this.weightHeightPercent;
	}

	public int getWeightLevel() {
		return this.weightLevel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public TriphopathiaItem jsonToObject(JSONObject jsonObject) {
		return TriphopathiaItemJsonFactory.jsonToObject(jsonObject);
	}

	public void setAgeOfTheMoon(int ageOfTheMoon) {
		this.ageOfTheMoon = ageOfTheMoon;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public void setBmi(double bmi) {
		this.bmi = bmi;
	}

	public void setBmiEvaluate(String bmiEvaluate) {
		this.bmiEvaluate = bmiEvaluate;
	}

	public void setBmiIndex(double bmiIndex) {
		this.bmiIndex = bmiIndex;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public void setCheckDoctor(String checkDoctor) {
		this.checkDoctor = checkDoctor;
	}

	public void setCheckDoctorId(String checkDoctorId) {
		this.checkDoctorId = checkDoctorId;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setEvaluate(String evaluate) {
		this.evaluate = evaluate;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public void setHeightEvaluate(String heightEvaluate) {
		this.heightEvaluate = heightEvaluate;
	}

	public void setHeightLevel(int heightLevel) {
		this.heightLevel = heightLevel;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}

	public void setSymptom(String symptom) {
		this.symptom = symptom;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setTriphopathiaId(long triphopathiaId) {
		this.triphopathiaId = triphopathiaId;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public void setWeightEvaluate(String weightEvaluate) {
		this.weightEvaluate = weightEvaluate;
	}

	public void setWeightHeightPercent(double weightHeightPercent) {
		this.weightHeightPercent = weightHeightPercent;
	}

	public void setWeightLevel(int weightLevel) {
		this.weightLevel = weightLevel;
	}

	public JSONObject toJsonObject() {
		return TriphopathiaItemJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return TriphopathiaItemJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
