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
 * 健康检查
 *
 */

@Entity
@Table(name = "HEALTH_MEDICAL_EXAMINATION")
public class MedicalExamination implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", nullable = false)
	protected long id;

	/**
	 * 检查批次编号
	 */
	@Column(name = "BATCHID_")
	protected long batchId;

	/**
	 * CheckId
	 */
	@Column(name = "CHECKID_", length = 50)
	protected String checkId;

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

	/**
	 * 性别(0代表女生，1代表男生)
	 */
	@Column(name = "SEX_", length = 2)
	protected String sex;

	@javax.persistence.Transient
	protected int age;

	/**
	 * 身高
	 */
	@Column(name = "HEIGHT_")
	protected double height;

	/**
	 * 身高增长,和前一次体检比较
	 */
	@Column(name = "HEIGHTINCREMENT_")
	protected double heightIncrement;

	/**
	 * 身高评价
	 */
	@Column(name = "HEIGHTEVALUATE_", length = 200)
	protected String heightEvaluate;

	/**
	 * 身高等级
	 */
	@Column(name = "HEIGHTLEVEL_")
	protected int heightLevel;

	/**
	 * 体重
	 */
	@Column(name = "WEIGHT_")
	protected double weight;

	/**
	 * 体重增长,和前一次体检比较
	 */
	@Column(name = "WEIGHTINCREMENT_")
	protected double weightIncrement;

	/**
	 * 体重评价
	 */
	@Column(name = "WEIGHTEVALUATE_", length = 200)
	protected String weightEvaluate;

	/**
	 * 体重等级
	 */
	@Column(name = "WEIGHTLEVEL_")
	protected int weightLevel;

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
	 * 综合评价
	 */
	@Column(name = "BMIEVALUATE_", length = 200)
	protected String bmiEvaluate;

	/**
	 * 肥胖指数
	 */
	@Column(name = "OBESITYINDEX_")
	protected double obesityIndex;

	/**
	 * 过敏史
	 */
	@Column(name = "ALLERGY_", length = 200)
	protected String allergy;

	@javax.persistence.Transient
	protected String eye;

	/**
	 * 左眼
	 */
	@Column(name = "EYELEFT_", length = 200)
	protected String eyeLeft;

	/**
	 * 左眼Remark
	 */
	@Column(name = "EYELEFTREMARK_", length = 200)
	protected String eyeLeftRemark;

	/**
	 * 右眼
	 */
	@Column(name = "EYERIGHT_", length = 200)
	protected String eyeRight;

	/**
	 * 右眼Remark
	 */
	@Column(name = "EYERIGHTREMARK_", length = 200)
	protected String eyeRightRemark;

	/**
	 * 左视力
	 */
	@Column(name = "EYESIGHTLEFT_")
	protected double eyesightLeft;

	/**
	 * 右视力
	 */
	@Column(name = "EYESIGHTRIGHT_")
	protected double eyesightRight;

	@javax.persistence.Transient
	protected String ear;

	/**
	 * 左耳
	 */
	@Column(name = "EARLEFT_", length = 200)
	protected String earLeft;

	/**
	 * 左耳Remark
	 */
	@Column(name = "EARLEFTREMARK_", length = 200)
	protected String earLeftRemark;

	/**
	 * 右耳
	 */
	@Column(name = "EARRIGHT_", length = 200)
	protected String earRight;

	/**
	 * 右耳Remark
	 */
	@Column(name = "EARRIGHTREMARK_", length = 200)
	protected String earRightRemark;

	/**
	 * 牙齿数
	 */
	@Column(name = "TOOTH_")
	protected int tooth;

	/**
	 * 龋齿数
	 */
	@Column(name = "SAPRODONTIA_")
	protected int saprodontia;

	/**
	 * 头颅
	 */
	@Column(name = "HEAD_", length = 200)
	protected String head;

	/**
	 * 头颅Remark
	 */
	@Column(name = "HEADREMARK_", length = 200)
	protected String headRemark;

	/**
	 * 胸廓
	 */
	@Column(name = "THORAX_", length = 200)
	protected String thorax;

	/**
	 * 胸廓Remark
	 */
	@Column(name = "THORAXREMARK_", length = 200)
	protected String thoraxRemark;

	/**
	 * 脊柱四肢
	 */
	@Column(name = "SPINE_", length = 200)
	protected String spine;

	/**
	 * 脊柱四肢Remark
	 */
	@Column(name = "SPINEREMARK_", length = 200)
	protected String spineRemark;

	/**
	 * 咽部
	 */
	@Column(name = "PHARYNGEAL_", length = 200)
	protected String pharyngeal;

	/**
	 * 咽部Remark
	 */
	@Column(name = "PHARYNGEALREMARK_", length = 200)
	protected String pharyngealRemark;

	/**
	 * 心肺
	 */
	@Column(name = "CARDIOPULMONARY_", length = 200)
	protected String cardiopulmonary;

	/**
	 * 心肺Remark
	 */
	@Column(name = "CARDIOPULMONARYREMARK_", length = 200)
	protected String cardiopulmonaryRemark;

	/**
	 * 肝脾
	 */
	@Column(name = "HEPATOLIENAL_", length = 200)
	protected String hepatolienal;

	/**
	 * 肝脾Remark
	 */
	@Column(name = "HEPATOLIENALREMARK_", length = 200)
	protected String hepatolienalRemark;

	/**
	 * 外生殖器
	 */
	@Column(name = "PUDENDUM_", length = 200)
	protected String pudendum;

	/**
	 * 外生殖器Remark
	 */
	@Column(name = "PUDENDUMREMARK_", length = 200)
	protected String pudendumRemark;

	/**
	 * 皮肤
	 */
	@Column(name = "SKIN_", length = 200)
	protected String skin;

	/**
	 * 皮肤Remark
	 */
	@Column(name = "SKINREMARK_", length = 200)
	protected String skinRemark;

	/**
	 * 血红蛋白
	 */
	@Column(name = "HEMOGLOBIN_", length = 200)
	protected String hemoglobin;

	@javax.persistence.Transient
	protected String hemoglobinHtml;

	/**
	 * 血红蛋白Hb
	 */
	@Column(name = "HEMOGLOBIN_VALUE_")
	protected double hemoglobinValue;

	/**
	 * 丙氨酸氨基转移酶
	 */
	@Column(name = "ALT_", length = 200)
	protected String alt;

	/**
	 * 丙氨酸氨基转移酶ALT
	 */
	@Column(name = "ALT_VALUE_")
	protected double altValue;

	/**
	 * 乙肝表面抗体
	 */
	@Column(name = "HBSAB_", length = 1)
	protected String hbsab;

	/**
	 * 乙肝表面抗体
	 */
	@Column(name = "HBSAB_VALUE_")
	protected double hbsabValue;

	/**
	 * 肝功能
	 */
	@Column(name = "SGPT_", length = 1)
	protected String sgpt;

	/**
	 * 肝炎病毒
	 */
	@Column(name = "HVAIGM_", length = 1)
	protected String hvaigm;

	/**
	 * 淋巴结
	 */
	@Column(name = "LYMPHOID_", length = 200)
	protected String lymphoid;

	/**
	 * 淋巴结Remark
	 */
	@Column(name = "LYMPHOIDREMARK_", length = 200)
	protected String lymphoidRemark;

	/**
	 * 前囟
	 */
	@Column(name = "BREGMA_", length = 200)
	protected String bregma;

	/**
	 * 前囟Remark
	 */
	@Column(name = "BREGMAREMARK_", length = 200)
	protected String bregmaRemark;

	/**
	 * 口腔
	 */
	@Column(name = "ORALOGY_", length = 200)
	protected String oralogy;

	/**
	 * 口腔Remark
	 */
	@Column(name = "ORALOGYREMARK_", length = 200)
	protected String oralogyRemark;

	/**
	 * 扁桃体
	 */
	@Column(name = "TONSIL_", length = 200)
	protected String tonsil;
	/**
	 * 扁桃体Remark
	 */
	@Column(name = "TONSILREMARK_", length = 200)
	protected String tonsilRemark;

	/**
	 * 骨骼
	 */
	@Column(name = "BONE_", length = 200)
	protected String bone;

	/**
	 * 骨骼Remark
	 */
	@Column(name = "BONEREMARK_", length = 200)
	protected String boneRemark;

	/**
	 * 先天缺陷
	 */
	@Column(name = "BIRTHDEFECT_", length = 200)
	protected String birthDefect;

	/**
	 * 健康评价
	 */
	@Column(name = "HEALTHEVALUATE_", length = 500)
	protected String healthEvaluate;

	/**
	 * 类型
	 */
	@Column(name = "TYPE_", length = 50)
	protected String type;

	@javax.persistence.Transient
	protected Date birthday;

	/**
	 * 体检时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CHECKDATE_")
	protected Date checkDate;

	/**
	 * 体检医生
	 */
	@Column(name = "CHECKDOCTOR_", length = 50)
	protected String checkDoctor;

	/**
	 * 体检医生ID
	 */
	@Column(name = "CHECKDOCTORID_", length = 50)
	protected String checkDoctorId;

	/**
	 * 体检结果
	 */
	@Column(name = "CHECKRESULT_", length = 500)
	protected String checkResult;

	/**
	 * 体检机构
	 */
	@Column(name = "CHECKORGANIZATION_", length = 200)
	protected String checkOrganization;

	/**
	 * 体检机构ID
	 */
	@Column(name = "CHECKORGANIZATIONID_")
	protected long checkOrganizationId;

	/**
	 * 医生建议
	 */
	@Column(name = "DOCTORSUGGEST_", length = 50)
	protected String doctorSuggest;

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
	 * 体检时月龄
	 */
	@Column(name = "AGEOFTHEMOON_")
	protected int ageOfTheMoon;

	/**
	 * 备注
	 */
	@Column(name = "REMARK_", length = 500)
	protected String remark;

	/**
	 * 确认人
	 */
	@Column(name = "CONFIRMBY_", length = 50)
	protected String confirmBy;

	/**
	 * 确认时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CONFIRMTIME_")
	protected Date confirmTime;

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

	public MedicalExamination() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MedicalExamination other = (MedicalExamination) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public int getAge() {
		return age;
	}

	public int getAgeOfTheMoon() {
		if (checkDate == null) {
			checkDate = new java.util.Date();
		}
		if (birthday != null) {
			Calendar startDate = Calendar.getInstance();
			startDate.setTime(birthday);
			Calendar endDate = Calendar.getInstance();
			endDate.setTime(checkDate);
			int days = DateUtils.getDaysBetween(startDate, endDate);
			ageOfTheMoon = (int) Math.ceil(days / 30.0D);
		}
		return ageOfTheMoon;
	}

	public String getAllergy() {
		return this.allergy;
	}

	public String getAlt() {
		return this.alt;
	}

	public double getAltValue() {
		return altValue;
	}

	public long getBatchId() {
		return batchId;
	}

	public Date getBirthday() {
		return birthday;
	}

	public String getBirthDefect() {
		return birthDefect;
	}

	public double getBmi() {
		if (bmi > 0) {
			bmi = Math.round(bmi * 100D) / 100D;
		}
		return bmi;
	}

	public String getBmiEvaluate() {
		return bmiEvaluate;
	}

	public double getBmiIndex() {
		if (bmiIndex > 0) {
			bmiIndex = Math.round(bmiIndex * 100D) / 100D;
		}
		return bmiIndex;
	}

	public String getBone() {
		return bone;
	}

	public String getBoneRemark() {
		return boneRemark;
	}

	public String getBregma() {
		return bregma;
	}

	public String getBregmaRemark() {
		return bregmaRemark;
	}

	public String getCardiopulmonary() {
		return this.cardiopulmonary;
	}

	public String getCardiopulmonaryRemark() {
		return cardiopulmonaryRemark;
	}

	public Date getCheckDate() {
		if (checkDate == null) {
			checkDate = new java.util.Date();
		}
		return this.checkDate;
	}

	public String getCheckDateString() {
		if (this.checkDate != null) {
			return DateUtils.getDateTime(this.checkDate);
		}
		return "";
	}

	public String getCheckDoctor() {
		return this.checkDoctor;
	}

	public String getCheckDoctorId() {
		return this.checkDoctorId;
	}

	public String getCheckId() {
		return checkId;
	}

	public String getCheckOrganization() {
		return this.checkOrganization;
	}

	public long getCheckOrganizationId() {
		return this.checkOrganizationId;
	}

	public String getCheckResult() {
		return this.checkResult;
	}

	public String getConfirmBy() {
		return this.confirmBy;
	}

	public Date getConfirmTime() {
		return this.confirmTime;
	}

	public String getConfirmTimeString() {
		if (this.confirmTime != null) {
			return DateUtils.getDateTime(this.confirmTime);
		}
		return "";
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

	public String getDoctorSuggest() {
		return this.doctorSuggest;
	}

	public String getEar() {
		return ear;
	}

	public String getEarLeft() {
		return this.earLeft;
	}

	public String getEarLeftRemark() {
		return earLeftRemark;
	}

	public String getEarRight() {
		return this.earRight;
	}

	public String getEarRightRemark() {
		return earRightRemark;
	}

	public String getEye() {
		return eye;
	}

	public String getEyeLeft() {
		return this.eyeLeft;
	}

	public String getEyeLeftRemark() {
		return eyeLeftRemark;
	}

	public String getEyeRight() {
		return this.eyeRight;
	}

	public String getEyeRightRemark() {
		return eyeRightRemark;
	}

	public double getEyesightLeft() {
		return this.eyesightLeft;
	}

	public double getEyesightRight() {
		return this.eyesightRight;
	}

	public String getGradeId() {
		return this.gradeId;
	}

	public String getHbsab() {
		return hbsab;
	}

	public double getHbsabValue() {
		return hbsabValue;
	}

	public String getHead() {
		return this.head;
	}

	public String getHeadRemark() {
		return headRemark;
	}

	public String getHealthEvaluate() {
		return healthEvaluate;
	}

	public double getHeight() {
		return this.height;
	}

	public String getHeightEvaluate() {
		return this.heightEvaluate;
	}

	public double getHeightIncrement() {
		return heightIncrement;
	}

	public int getHeightLevel() {
		return heightLevel;
	}

	public String getHemoglobin() {
		hemoglobin = "正常";
		if (hemoglobinValue >= 90.0 && hemoglobinValue < 110.0) {
			hemoglobin = "轻度贫血";
		} else if (hemoglobinValue >= 60.0 && hemoglobinValue < 90.0) {
			hemoglobin = "中度贫血";
		} else if (hemoglobinValue >= 30.0 && hemoglobinValue < 60.0) {
			hemoglobin = "重度贫血";
		} else if (hemoglobinValue < 30.0) {
			hemoglobin = "极重度贫血";
		}
		return this.hemoglobin;
	}

	public String getHemoglobinHtml() {
		hemoglobinHtml = "<span style='color:#339933; font:14px 微软雅黑;'>正常</span>";
		if (hemoglobinValue >= 90.0 && hemoglobinValue < 110.0) {
			hemoglobinHtml = "<span style='color:#FF9966; font:bold 14px 微软雅黑;'>轻度贫血</span>";
		} else if (hemoglobinValue >= 60.0 && hemoglobinValue < 90.0) {
			hemoglobinHtml = "<span style='color:#FF6666; font:bold 14px 微软雅黑;'>中度贫血</span>";
		} else if (hemoglobinValue >= 30.0 && hemoglobinValue < 60.0) {
			hemoglobinHtml = "<span style='color:#FF0033; font:bold 14px 微软雅黑;'>重度贫血</span>";
		} else if (hemoglobinValue < 30.0 && hemoglobinValue > 0) {
			hemoglobinHtml = "<span style='color:#FF0000; font:bold 14px 微软雅黑;'>极重度贫血</span>";
		} else if (hemoglobinValue == 0) {
			hemoglobinHtml = "";
		}
		return this.hemoglobinHtml;
	}

	public double getHemoglobinValue() {
		return hemoglobinValue;
	}

	public String getHepatolienal() {
		return this.hepatolienal;
	}

	public String getHepatolienalRemark() {
		return hepatolienalRemark;
	}

	public String getHvaigm() {
		return hvaigm;
	}

	public long getId() {
		return this.id;
	}

	public String getLymphoid() {
		return lymphoid;
	}

	public String getLymphoidRemark() {
		return lymphoidRemark;
	}

	public int getMonth() {
		return this.month;
	}

	public String getName() {
		return this.name;
	}

	public double getObesityIndex() {
		return obesityIndex;
	}

	public String getOralogy() {
		return oralogy;
	}

	public String getOralogyRemark() {
		return oralogyRemark;
	}

	public String getPersonId() {
		return this.personId;
	}

	public String getPharyngeal() {
		return this.pharyngeal;
	}

	public String getPharyngealRemark() {
		return pharyngealRemark;
	}

	public String getPudendum() {
		return this.pudendum;
	}

	public String getPudendumRemark() {
		return pudendumRemark;
	}

	public String getRemark() {
		return this.remark;
	}

	public int getSaprodontia() {
		return this.saprodontia;
	}

	public String getSex() {
		return this.sex;
	}

	public String getSgpt() {
		return sgpt;
	}

	public String getSkin() {
		return this.skin;
	}

	public String getSkinRemark() {
		return skinRemark;
	}

	public String getSpine() {
		return this.spine;
	}

	public String getSpineRemark() {
		return spineRemark;
	}

	public String getTenantId() {
		return this.tenantId;
	}

	public String getThorax() {
		return this.thorax;
	}

	public String getThoraxRemark() {
		return thoraxRemark;
	}

	public String getTonsil() {
		return tonsil;
	}

	public String getTonsilRemark() {
		return tonsilRemark;
	}

	public int getTooth() {
		return this.tooth;
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

	public double getWeightIncrement() {
		return weightIncrement;
	}

	public int getWeightLevel() {
		return weightLevel;
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

	public MedicalExamination jsonToObject(JSONObject jsonObject) {
		return MedicalExaminationJsonFactory.jsonToObject(jsonObject);
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setAgeOfTheMoon(int ageOfTheMoon) {
		this.ageOfTheMoon = ageOfTheMoon;
	}

	public void setAllergy(String allergy) {
		this.allergy = allergy;
	}

	public void setAlt(String alt) {
		this.alt = alt;
	}

	public void setAltValue(double altValue) {
		this.altValue = altValue;
	}

	public void setBatchId(long batchId) {
		this.batchId = batchId;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public void setBirthDefect(String birthDefect) {
		this.birthDefect = birthDefect;
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

	public void setBone(String bone) {
		this.bone = bone;
	}

	public void setBoneRemark(String boneRemark) {
		this.boneRemark = boneRemark;
	}

	public void setBregma(String bregma) {
		this.bregma = bregma;
	}

	public void setBregmaRemark(String bregmaRemark) {
		this.bregmaRemark = bregmaRemark;
	}

	public void setCardiopulmonary(String cardiopulmonary) {
		this.cardiopulmonary = cardiopulmonary;
	}

	public void setCardiopulmonaryRemark(String cardiopulmonaryRemark) {
		this.cardiopulmonaryRemark = cardiopulmonaryRemark;
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

	public void setCheckId(String checkId) {
		this.checkId = checkId;
	}

	public void setCheckOrganization(String checkOrganization) {
		this.checkOrganization = checkOrganization;
	}

	public void setCheckOrganizationId(long checkOrganizationId) {
		this.checkOrganizationId = checkOrganizationId;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}

	public void setConfirmBy(String confirmBy) {
		this.confirmBy = confirmBy;
	}

	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setDoctorSuggest(String doctorSuggest) {
		this.doctorSuggest = doctorSuggest;
	}

	public void setEar(String ear) {
		this.ear = ear;
	}

	public void setEarLeft(String earLeft) {
		this.earLeft = earLeft;
	}

	public void setEarLeftRemark(String earLeftRemark) {
		this.earLeftRemark = earLeftRemark;
	}

	public void setEarRight(String earRight) {
		this.earRight = earRight;
	}

	public void setEarRightRemark(String earRightRemark) {
		this.earRightRemark = earRightRemark;
	}

	public void setEye(String eye) {
		this.eye = eye;
	}

	public void setEyeLeft(String eyeLeft) {
		this.eyeLeft = eyeLeft;
	}

	public void setEyeLeftRemark(String eyeLeftRemark) {
		this.eyeLeftRemark = eyeLeftRemark;
	}

	public void setEyeRight(String eyeRight) {
		this.eyeRight = eyeRight;
	}

	public void setEyeRightRemark(String eyeRightRemark) {
		this.eyeRightRemark = eyeRightRemark;
	}

	public void setEyesightLeft(double eyesightLeft) {
		this.eyesightLeft = eyesightLeft;
	}

	public void setEyesightRight(double eyesightRight) {
		this.eyesightRight = eyesightRight;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public void setHbsab(String hbsab) {
		this.hbsab = hbsab;
	}

	public void setHbsabValue(double hbsabValue) {
		this.hbsabValue = hbsabValue;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public void setHeadRemark(String headRemark) {
		this.headRemark = headRemark;
	}

	public void setHealthEvaluate(String healthEvaluate) {
		this.healthEvaluate = healthEvaluate;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public void setHeightEvaluate(String heightEvaluate) {
		this.heightEvaluate = heightEvaluate;
	}

	public void setHeightIncrement(double heightIncrement) {
		this.heightIncrement = heightIncrement;
	}

	public void setHeightLevel(int heightLevel) {
		this.heightLevel = heightLevel;
	}

	public void setHemoglobin(String hemoglobin) {
		this.hemoglobin = hemoglobin;
	}

	public void setHemoglobinValue(double hemoglobinValue) {
		this.hemoglobinValue = hemoglobinValue;
	}

	public void setHepatolienal(String hepatolienal) {
		this.hepatolienal = hepatolienal;
	}

	public void setHepatolienalRemark(String hepatolienalRemark) {
		this.hepatolienalRemark = hepatolienalRemark;
	}

	public void setHvaigm(String hvaigm) {
		this.hvaigm = hvaigm;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setLymphoid(String lymphoid) {
		this.lymphoid = lymphoid;
	}

	public void setLymphoidRemark(String lymphoidRemark) {
		this.lymphoidRemark = lymphoidRemark;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setObesityIndex(double obesityIndex) {
		this.obesityIndex = obesityIndex;
	}

	public void setOralogy(String oralogy) {
		this.oralogy = oralogy;
	}

	public void setOralogyRemark(String oralogyRemark) {
		this.oralogyRemark = oralogyRemark;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public void setPharyngeal(String pharyngeal) {
		this.pharyngeal = pharyngeal;
	}

	public void setPharyngealRemark(String pharyngealRemark) {
		this.pharyngealRemark = pharyngealRemark;
	}

	public void setPudendum(String pudendum) {
		this.pudendum = pudendum;
	}

	public void setPudendumRemark(String pudendumRemark) {
		this.pudendumRemark = pudendumRemark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setSaprodontia(int saprodontia) {
		this.saprodontia = saprodontia;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setSgpt(String sgpt) {
		this.sgpt = sgpt;
	}

	public void setSkin(String skin) {
		this.skin = skin;
	}

	public void setSkinRemark(String skinRemark) {
		this.skinRemark = skinRemark;
	}

	public void setSpine(String spine) {
		this.spine = spine;
	}

	public void setSpineRemark(String spineRemark) {
		this.spineRemark = spineRemark;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setThorax(String thorax) {
		this.thorax = thorax;
	}

	public void setThoraxRemark(String thoraxRemark) {
		this.thoraxRemark = thoraxRemark;
	}

	public void setTonsil(String tonsil) {
		this.tonsil = tonsil;
	}

	public void setTonsilRemark(String tonsilRemark) {
		this.tonsilRemark = tonsilRemark;
	}

	public void setTooth(int tooth) {
		this.tooth = tooth;
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

	public void setWeightIncrement(double weightIncrement) {
		this.weightIncrement = weightIncrement;
	}

	public void setWeightLevel(int weightLevel) {
		this.weightLevel = weightLevel;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public JSONObject toJsonObject() {
		return MedicalExaminationJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return MedicalExaminationJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
