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
@Table(name = "HEALTH_PERSON")
public class Person implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", length = 50, nullable = false)
	protected String id;

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

	@javax.persistence.Transient
	protected String gradeName;

	/**
	 * 姓名
	 */
	@Column(name = "NAME_", length = 100)
	protected String name;

	/**
	 * 身份证编号
	 */
	@Column(name = "IDCARDNO_", length = 20)
	protected String idCardNo;

	/**
	 * 家长
	 */
	@Column(name = "PATRIARCH_", length = 50)
	protected String patriarch;

	/**
	 * 电话
	 */
	@Column(name = "TELEPHONE_", length = 200)
	protected String telephone;

	/**
	 * 省/直辖市
	 */
	@Column(name = "PROVINCE_", length = 100)
	protected String province;

	/**
	 * 省/直辖市编号
	 */
	@Column(name = "PROVINCEID_")
	protected long provinceId;

	/**
	 * 市
	 */
	@Column(name = "CITY_", length = 100)
	protected String city;

	/**
	 * 市编号
	 */
	@Column(name = "CITYID_")
	protected long cityId;

	/**
	 * 区/县
	 */
	@Column(name = "AREA_", length = 100)
	protected String area;

	/**
	 * 区/县编号
	 */
	@Column(name = "AREAID_")
	protected long areaId;

	/**
	 * 镇/街道
	 */
	@Column(name = "TOWN_", length = 200)
	protected String town;

	/**
	 * 镇/街道编号
	 */
	@Column(name = "TOWNID_")
	protected long townId;

	/**
	 * 家庭住址
	 */
	@Column(name = "HOMEADDRESS_", length = 250)
	protected String homeAddress;

	/**
	 * 出生地
	 */
	@Column(name = "BIRTHADDRESS_", length = 250)
	protected String birthAddress;

	/**
	 * 性别(0代表女生，1代表男生)
	 */
	@Column(name = "SEX_", length = 2)
	protected String sex;

	/**
	 * 出生日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BIRTHDAY_")
	protected Date birthday;

	/**
	 * 出生年份
	 */
	@Column(name = "YEAR_")
	protected int year;

	/**
	 * 入园日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "JOINDATE_")
	protected Date joinDate;

	/**
	 * 健康状况
	 */
	@Column(name = "HEALTHCONDITION_", length = 50)
	protected String healthCondition;

	/**
	 * 过敏史（食物、药物、其它过敏史）
	 */
	@Column(name = "ALLERGY_", length = 500)
	protected String allergy;

	/**
	 * 喂养史
	 */
	@Column(name = "FEEDINGHISTORY_", length = 500)
	protected String feedingHistory;

	/**
	 * 既往史
	 */
	@Column(name = "PREVIOUSHISTORY_", length = 500)
	protected String previousHistory;

	/**
	 * 食物过敏史
	 */
	@Column(name = "FOODALLERGY_", length = 500)
	protected String foodAllergy;

	/**
	 * 药物过敏史
	 */
	@Column(name = "MEDICINEALLERGY_", length = 500)
	protected String medicineAllergy;

	/**
	 * 入园时身高
	 */
	@Column(name = "HEIGHT_")
	protected double height;

	/**
	 * 入园时体重
	 */
	@Column(name = "WEIGHT_")
	protected double weight;

	/**
	 * 父亲
	 */
	@Column(name = "FATHER_", length = 50)
	protected String father;

	/**
	 * 单位
	 */
	@Column(name = "FATHERCOMPANY_", length = 200)
	protected String fatherCompany;

	/**
	 * 电话
	 */
	@Column(name = "FATHERTELEPHONE_", length = 200)
	protected String fatherTelephone;

	/**
	 * 父亲是否监护人
	 */
	@Column(name = "FATHERWARDSHIP_", length = 1)
	protected String fatherWardship;

	/**
	 * 母亲
	 */
	@Column(name = "MOTHER_", length = 50)
	protected String mother;

	/**
	 * 单位
	 */
	@Column(name = "MOTHERCOMPANY_", length = 200)
	protected String motherCompany;

	/**
	 * 电话
	 */
	@Column(name = "MOTHERTELEPHONE_", length = 200)
	protected String motherTelephone;

	/**
	 * 母亲是否监护人
	 */
	@Column(name = "MOTHERWARDSHIP_", length = 1)
	protected String motherWardship;

	/**
	 * 备注
	 */
	@Column(name = "REMARK_", length = 2000)
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

	@Column(name = "DELETEFLAG_")
	protected int deleteFlag;

	@javax.persistence.Transient
	protected int status;

	@javax.persistence.Transient
	protected String memo;

	@javax.persistence.Transient
	protected String treat;

	@javax.persistence.Transient
	protected List<PersonLinkman> linkmans = new ArrayList<PersonLinkman>();

	public Person() {

	}

	public void addLinkman(PersonLinkman linkman) {
		if (linkmans == null) {
			linkmans = new ArrayList<PersonLinkman>();
		}
		linkmans.add(linkman);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public int getAgeOfTheMoon() {
		int ageOfTheMoon = 0;
		if (birthday != null) {
			Calendar startDate = Calendar.getInstance();
			startDate.setTime(birthday);
			Calendar endDate = Calendar.getInstance();
			endDate.setTime(new java.util.Date());
			int days = DateUtils.getDaysBetween(startDate, endDate);
			ageOfTheMoon = (int) Math.ceil(days / 30.0D);
		}
		return ageOfTheMoon;
	}

	public String getAllergy() {
		return allergy;
	}

	public String getArea() {
		return this.area;
	}

	public long getAreaId() {
		return areaId;
	}

	public String getBirthAddress() {
		return this.birthAddress;
	}

	public Date getBirthday() {
		return this.birthday;
	}

	public String getBirthdayString() {
		if (this.birthday != null) {
			return DateUtils.getDateTime(this.birthday);
		}
		return "";
	}

	public String getCity() {
		return this.city;
	}

	public long getCityId() {
		return cityId;
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

	public int getDeleteFlag() {
		return deleteFlag;
	}

	public String getFather() {
		return father;
	}

	public String getFatherCompany() {
		return fatherCompany;
	}

	public String getFatherTelephone() {
		return fatherTelephone;
	}

	public String getFatherWardship() {
		return fatherWardship;
	}

	public String getFeedingHistory() {
		return feedingHistory;
	}

	public String getFoodAllergy() {
		return foodAllergy;
	}

	public String getGradeId() {
		return this.gradeId;
	}

	public String getGradeName() {
		return gradeName;
	}

	public String getHealthCondition() {
		return healthCondition;
	}

	public double getHeight() {
		return height;
	}

	public String getHomeAddress() {
		return this.homeAddress;
	}

	public String getId() {
		return this.id;
	}

	public String getIdCardNo() {
		return this.idCardNo;
	}

	public int getJoinAgeOfTheMoon() {
		int ageOfTheMoon = 36;
		if (birthday != null && joinDate != null) {
			Calendar startDate = Calendar.getInstance();
			startDate.setTime(birthday);
			Calendar endDate = Calendar.getInstance();
			endDate.setTime(joinDate);
			int days = DateUtils.getDaysBetween(startDate, endDate);
			ageOfTheMoon = (int) Math.ceil(days / 30.0D);
		}
		return ageOfTheMoon;
	}

	public Date getJoinDate() {
		return joinDate;
	}

	public List<PersonLinkman> getLinkmans() {
		return linkmans;
	}

	public String getMedicineAllergy() {
		return medicineAllergy;
	}

	public String getMemo() {
		return memo;
	}

	public String getMother() {
		return mother;
	}

	public String getMotherCompany() {
		return motherCompany;
	}

	public String getMotherTelephone() {
		return motherTelephone;
	}

	public String getMotherWardship() {
		return motherWardship;
	}

	public String getName() {
		return this.name;
	}

	public String getPatriarch() {
		return this.patriarch;
	}

	public String getPreviousHistory() {
		return previousHistory;
	}

	public String getProvince() {
		return this.province;
	}

	public long getProvinceId() {
		return provinceId;
	}

	public String getRemark() {
		return this.remark;
	}

	public String getSex() {
		return this.sex;
	}

	public int getStatus() {
		return status;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public String getTenantId() {
		return this.tenantId;
	}

	public String getTown() {
		return this.town;
	}

	public long getTownId() {
		return townId;
	}

	public String getTreat() {
		return treat;
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

	public double getWeight() {
		return weight;
	}

	public int getYear() {
		return this.year;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public Person jsonToObject(JSONObject jsonObject) {
		return PersonJsonFactory.jsonToObject(jsonObject);
	}

	public void setAllergy(String allergy) {
		this.allergy = allergy;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}

	public void setBirthAddress(String birthAddress) {
		this.birthAddress = birthAddress;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public void setFather(String father) {
		this.father = father;
	}

	public void setFatherCompany(String fatherCompany) {
		this.fatherCompany = fatherCompany;
	}

	public void setFatherTelephone(String fatherTelephone) {
		this.fatherTelephone = fatherTelephone;
	}

	public void setFatherWardship(String fatherWardship) {
		this.fatherWardship = fatherWardship;
	}

	public void setFeedingHistory(String feedingHistory) {
		this.feedingHistory = feedingHistory;
	}

	public void setFoodAllergy(String foodAllergy) {
		this.foodAllergy = foodAllergy;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public void setHealthCondition(String healthCondition) {
		this.healthCondition = healthCondition;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}

	public void setLinkmans(List<PersonLinkman> linkmans) {
		this.linkmans = linkmans;
	}

	public void setMedicineAllergy(String medicineAllergy) {
		this.medicineAllergy = medicineAllergy;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public void setMother(String mother) {
		this.mother = mother;
	}

	public void setMotherCompany(String motherCompany) {
		this.motherCompany = motherCompany;
	}

	public void setMotherTelephone(String motherTelephone) {
		this.motherTelephone = motherTelephone;
	}

	public void setMotherWardship(String motherWardship) {
		this.motherWardship = motherWardship;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPatriarch(String patriarch) {
		this.patriarch = patriarch;
	}

	public void setPreviousHistory(String previousHistory) {
		this.previousHistory = previousHistory;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public void setProvinceId(long provinceId) {
		this.provinceId = provinceId;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public void setTownId(long townId) {
		this.townId = townId;
	}

	public void setTreat(String treat) {
		this.treat = treat;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public JSONObject toJsonObject() {
		return PersonJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return PersonJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
