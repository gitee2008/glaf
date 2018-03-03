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

package com.glaf.heathcare.query;

import java.util.*;
import com.glaf.core.query.DataQuery;

public class PersonQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected List<String> tenantIds;
	protected String gradeId;
	protected Collection<String> gradeIds;
	protected String name;
	protected String nameLike;
	protected String idCardNo;
	protected String idCardNoLike;
	protected String patriarch;
	protected String patriarchLike;
	protected String telephone;
	protected String telephoneLike;
	protected String province;
	protected Long provinceId;
	protected String city;
	protected Long cityId;
	protected String area;
	protected Long areaId;
	protected String town;
	protected Long townId;
	protected String homeAddress;
	protected String homeAddressLike;
	protected String birthAddress;
	protected String birthAddressLike;
	protected String sex;
	protected Date birthdayGreaterThanOrEqual;
	protected Date birthdayLessThanOrEqual;
	protected Integer year;
	protected Integer yearGreaterThanOrEqual;
	protected Integer yearLessThanOrEqual;
	protected String remarkLike;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	public PersonQuery() {

	}

	public PersonQuery area(String area) {
		if (area == null) {
			throw new RuntimeException("area is null");
		}
		this.area = area;
		return this;
	}

	public PersonQuery areaId(Long areaId) {
		if (areaId == null) {
			throw new RuntimeException("areaId is null");
		}
		this.areaId = areaId;
		return this;
	}

	public PersonQuery birthAddress(String birthAddress) {
		if (birthAddress == null) {
			throw new RuntimeException("birthAddress is null");
		}
		this.birthAddress = birthAddress;
		return this;
	}

	public PersonQuery birthAddressLike(String birthAddressLike) {
		if (birthAddressLike == null) {
			throw new RuntimeException("birthAddress is null");
		}
		this.birthAddressLike = birthAddressLike;
		return this;
	}

	public PersonQuery birthdayGreaterThanOrEqual(Date birthdayGreaterThanOrEqual) {
		if (birthdayGreaterThanOrEqual == null) {
			throw new RuntimeException("birthday is null");
		}
		this.birthdayGreaterThanOrEqual = birthdayGreaterThanOrEqual;
		return this;
	}

	public PersonQuery birthdayLessThanOrEqual(Date birthdayLessThanOrEqual) {
		if (birthdayLessThanOrEqual == null) {
			throw new RuntimeException("birthday is null");
		}
		this.birthdayLessThanOrEqual = birthdayLessThanOrEqual;
		return this;
	}

	public PersonQuery city(String city) {
		if (city == null) {
			throw new RuntimeException("city is null");
		}
		this.city = city;
		return this;
	}

	public PersonQuery cityId(Long cityId) {
		if (cityId == null) {
			throw new RuntimeException("cityId is null");
		}
		this.cityId = cityId;
		return this;
	}

	public PersonQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public PersonQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public String getArea() {
		return area;
	}

	public Long getAreaId() {
		return areaId;
	}

	public String getBirthAddress() {
		return birthAddress;
	}

	public String getBirthAddressLike() {
		if (birthAddressLike != null && birthAddressLike.trim().length() > 0) {
			if (!birthAddressLike.startsWith("%")) {
				birthAddressLike = "%" + birthAddressLike;
			}
			if (!birthAddressLike.endsWith("%")) {
				birthAddressLike = birthAddressLike + "%";
			}
		}
		return birthAddressLike;
	}

	public Date getBirthdayGreaterThanOrEqual() {
		return birthdayGreaterThanOrEqual;
	}

	public Date getBirthdayLessThanOrEqual() {
		return birthdayLessThanOrEqual;
	}

	public String getCity() {
		return city;
	}

	public Long getCityId() {
		return cityId;
	}

	public Date getCreateTimeGreaterThanOrEqual() {
		return createTimeGreaterThanOrEqual;
	}

	public Date getCreateTimeLessThanOrEqual() {
		return createTimeLessThanOrEqual;
	}

	public String getGradeId() {
		return gradeId;
	}

	public Collection<String> getGradeIds() {
		return gradeIds;
	}

	public String getHomeAddress() {
		return homeAddress;
	}

	public String getHomeAddressLike() {
		if (homeAddressLike != null && homeAddressLike.trim().length() > 0) {
			if (!homeAddressLike.startsWith("%")) {
				homeAddressLike = "%" + homeAddressLike;
			}
			if (!homeAddressLike.endsWith("%")) {
				homeAddressLike = homeAddressLike + "%";
			}
		}
		return homeAddressLike;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public String getIdCardNoLike() {
		if (idCardNoLike != null && idCardNoLike.trim().length() > 0) {
			if (!idCardNoLike.startsWith("%")) {
				idCardNoLike = "%" + idCardNoLike;
			}
			if (!idCardNoLike.endsWith("%")) {
				idCardNoLike = idCardNoLike + "%";
			}
		}
		return idCardNoLike;
	}

	public String getName() {
		return name;
	}

	public String getNameLike() {
		if (nameLike != null && nameLike.trim().length() > 0) {
			if (!nameLike.startsWith("%")) {
				nameLike = "%" + nameLike;
			}
			if (!nameLike.endsWith("%")) {
				nameLike = nameLike + "%";
			}
		}
		return nameLike;
	}

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("tenantId".equals(sortColumn)) {
				orderBy = "E.TENANTID_" + a_x;
			}

			if ("gradeId".equals(sortColumn)) {
				orderBy = "E.GRADEID_" + a_x;
			}

			if ("name".equals(sortColumn)) {
				orderBy = "E.NAME_" + a_x;
			}

			if ("idCardNo".equals(sortColumn)) {
				orderBy = "E.IDCARDNO_" + a_x;
			}

			if ("patriarch".equals(sortColumn)) {
				orderBy = "E.PATRIARCH_" + a_x;
			}

			if ("telephone".equals(sortColumn)) {
				orderBy = "E.TELEPHONE_" + a_x;
			}

			if ("province".equals(sortColumn)) {
				orderBy = "E.PROVINCE_" + a_x;
			}

			if ("city".equals(sortColumn)) {
				orderBy = "E.CITY_" + a_x;
			}

			if ("area".equals(sortColumn)) {
				orderBy = "E.AREA_" + a_x;
			}

			if ("town".equals(sortColumn)) {
				orderBy = "E.TOWN_" + a_x;
			}

			if ("homeAddress".equals(sortColumn)) {
				orderBy = "E.HOMEADDRESS_" + a_x;
			}

			if ("birthAddress".equals(sortColumn)) {
				orderBy = "E.BIRTHADDRESS_" + a_x;
			}

			if ("sex".equals(sortColumn)) {
				orderBy = "E.SEX_" + a_x;
			}

			if ("birthday".equals(sortColumn)) {
				orderBy = "E.BIRTHDAY_" + a_x;
			}

			if ("year".equals(sortColumn)) {
				orderBy = "E.YEAR_" + a_x;
			}

			if ("remark".equals(sortColumn)) {
				orderBy = "E.REMARK_" + a_x;
			}

			if ("createBy".equals(sortColumn)) {
				orderBy = "E.CREATEBY_" + a_x;
			}

			if ("createTime".equals(sortColumn)) {
				orderBy = "E.CREATETIME_" + a_x;
			}

			if ("updateBy".equals(sortColumn)) {
				orderBy = "E.UPDATEBY_" + a_x;
			}

			if ("updateTime".equals(sortColumn)) {
				orderBy = "E.UPDATETIME_" + a_x;
			}

		}
		return orderBy;
	}

	public String getPatriarch() {
		return patriarch;
	}

	public String getPatriarchLike() {
		if (patriarchLike != null && patriarchLike.trim().length() > 0) {
			if (!patriarchLike.startsWith("%")) {
				patriarchLike = "%" + patriarchLike;
			}
			if (!patriarchLike.endsWith("%")) {
				patriarchLike = patriarchLike + "%";
			}
		}
		return patriarchLike;
	}

	public String getProvince() {
		return province;
	}

	public Long getProvinceId() {
		return provinceId;
	}

	public String getRemarkLike() {
		if (remarkLike != null && remarkLike.trim().length() > 0) {
			if (!remarkLike.startsWith("%")) {
				remarkLike = "%" + remarkLike;
			}
			if (!remarkLike.endsWith("%")) {
				remarkLike = remarkLike + "%";
			}
		}
		return remarkLike;
	}

	public String getSex() {
		return sex;
	}

	public String getTelephone() {
		return telephone;
	}

	public String getTelephoneLike() {
		if (telephoneLike != null && telephoneLike.trim().length() > 0) {
			if (!telephoneLike.startsWith("%")) {
				telephoneLike = "%" + telephoneLike;
			}
			if (!telephoneLike.endsWith("%")) {
				telephoneLike = telephoneLike + "%";
			}
		}
		return telephoneLike;
	}

	public String getTenantId() {
		return tenantId;
	}

	public List<String> getTenantIds() {
		return tenantIds;
	}

	public String getTown() {
		return town;
	}

	public Long getTownId() {
		return townId;
	}

	public Integer getYear() {
		return year;
	}

	public Integer getYearGreaterThanOrEqual() {
		return yearGreaterThanOrEqual;
	}

	public Integer getYearLessThanOrEqual() {
		return yearLessThanOrEqual;
	}

	public PersonQuery gradeId(String gradeId) {
		if (gradeId == null) {
			throw new RuntimeException("gradeId is null");
		}
		this.gradeId = gradeId;
		return this;
	}

	public PersonQuery gradeIds(Collection<String> gradeIds) {
		if (gradeIds == null) {
			throw new RuntimeException("gradeIds is empty ");
		}
		this.gradeIds = gradeIds;
		return this;
	}

	public PersonQuery homeAddress(String homeAddress) {
		if (homeAddress == null) {
			throw new RuntimeException("homeAddress is null");
		}
		this.homeAddress = homeAddress;
		return this;
	}

	public PersonQuery homeAddressLike(String homeAddressLike) {
		if (homeAddressLike == null) {
			throw new RuntimeException("homeAddress is null");
		}
		this.homeAddressLike = homeAddressLike;
		return this;
	}

	public PersonQuery idCardNo(String idCardNo) {
		if (idCardNo == null) {
			throw new RuntimeException("idCardNo is null");
		}
		this.idCardNo = idCardNo;
		return this;
	}

	public PersonQuery idCardNoLike(String idCardNoLike) {
		if (idCardNoLike == null) {
			throw new RuntimeException("idCardNo is null");
		}
		this.idCardNoLike = idCardNoLike;
		return this;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("tenantId", "TENANTID_");
		addColumn("gradeId", "GRADEID_");
		addColumn("name", "NAME_");
		addColumn("idCardNo", "IDCARDNO_");
		addColumn("patriarch", "PATRIARCH_");
		addColumn("telephone", "TELEPHONE_");
		addColumn("province", "PROVINCE_");
		addColumn("city", "CITY_");
		addColumn("area", "AREA_");
		addColumn("town", "TOWN_");
		addColumn("homeAddress", "HOMEADDRESS_");
		addColumn("birthAddress", "BIRTHADDRESS_");
		addColumn("sex", "SEX_");
		addColumn("birthday", "BIRTHDAY_");
		addColumn("year", "YEAR_");
		addColumn("remark", "REMARK_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
		addColumn("updateBy", "UPDATEBY_");
		addColumn("updateTime", "UPDATETIME_");
	}

	public PersonQuery name(String name) {
		if (name == null) {
			throw new RuntimeException("name is null");
		}
		this.name = name;
		return this;
	}

	public PersonQuery nameLike(String nameLike) {
		if (nameLike == null) {
			throw new RuntimeException("name is null");
		}
		this.nameLike = nameLike;
		return this;
	}

	public PersonQuery patriarch(String patriarch) {
		if (patriarch == null) {
			throw new RuntimeException("patriarch is null");
		}
		this.patriarch = patriarch;
		return this;
	}

	public PersonQuery patriarchLike(String patriarchLike) {
		if (patriarchLike == null) {
			throw new RuntimeException("patriarch is null");
		}
		this.patriarchLike = patriarchLike;
		return this;
	}

	public PersonQuery province(String province) {
		if (province == null) {
			throw new RuntimeException("province is null");
		}
		this.province = province;
		return this;
	}

	public PersonQuery provinceId(Long provinceId) {
		if (provinceId == null) {
			throw new RuntimeException("provinceId is null");
		}
		this.provinceId = provinceId;
		return this;
	}

	public PersonQuery remarkLike(String remarkLike) {
		if (remarkLike == null) {
			throw new RuntimeException("remark is null");
		}
		this.remarkLike = remarkLike;
		return this;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public void setBirthAddress(String birthAddress) {
		this.birthAddress = birthAddress;
	}

	public void setBirthAddressLike(String birthAddressLike) {
		this.birthAddressLike = birthAddressLike;
	}

	public void setBirthdayGreaterThanOrEqual(Date birthdayGreaterThanOrEqual) {
		this.birthdayGreaterThanOrEqual = birthdayGreaterThanOrEqual;
	}

	public void setBirthdayLessThanOrEqual(Date birthdayLessThanOrEqual) {
		this.birthdayLessThanOrEqual = birthdayLessThanOrEqual;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public void setGradeIds(Collection<String> gradeIds) {
		this.gradeIds = gradeIds;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	public void setHomeAddressLike(String homeAddressLike) {
		this.homeAddressLike = homeAddressLike;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public void setIdCardNoLike(String idCardNoLike) {
		this.idCardNoLike = idCardNoLike;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNameLike(String nameLike) {
		this.nameLike = nameLike;
	}

	public void setPatriarch(String patriarch) {
		this.patriarch = patriarch;
	}

	public void setPatriarchLike(String patriarchLike) {
		this.patriarchLike = patriarchLike;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	public void setRemarkLike(String remarkLike) {
		this.remarkLike = remarkLike;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public void setTelephoneLike(String telephoneLike) {
		this.telephoneLike = telephoneLike;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setTenantIds(List<String> tenantIds) {
		this.tenantIds = tenantIds;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public void setTownId(Long townId) {
		this.townId = townId;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public void setYearGreaterThanOrEqual(Integer yearGreaterThanOrEqual) {
		this.yearGreaterThanOrEqual = yearGreaterThanOrEqual;
	}

	public void setYearLessThanOrEqual(Integer yearLessThanOrEqual) {
		this.yearLessThanOrEqual = yearLessThanOrEqual;
	}

	public PersonQuery sex(String sex) {
		if (sex == null) {
			throw new RuntimeException("sex is null");
		}
		this.sex = sex;
		return this;
	}

	public PersonQuery telephone(String telephone) {
		if (telephone == null) {
			throw new RuntimeException("telephone is null");
		}
		this.telephone = telephone;
		return this;
	}

	public PersonQuery telephoneLike(String telephoneLike) {
		if (telephoneLike == null) {
			throw new RuntimeException("telephone is null");
		}
		this.telephoneLike = telephoneLike;
		return this;
	}

	public PersonQuery tenantIds(List<String> tenantIds) {
		if (tenantIds == null) {
			throw new RuntimeException("tenantIds is empty ");
		}
		this.tenantIds = tenantIds;
		return this;
	}

	public PersonQuery town(String town) {
		if (town == null) {
			throw new RuntimeException("town is null");
		}
		this.town = town;
		return this;
	}

	public PersonQuery townId(Long townId) {
		if (townId == null) {
			throw new RuntimeException("townId is null");
		}
		this.townId = townId;
		return this;
	}

	public PersonQuery year(Integer year) {
		if (year == null) {
			throw new RuntimeException("year is null");
		}
		this.year = year;
		return this;
	}

	public PersonQuery yearGreaterThanOrEqual(Integer yearGreaterThanOrEqual) {
		if (yearGreaterThanOrEqual == null) {
			throw new RuntimeException("year is null");
		}
		this.yearGreaterThanOrEqual = yearGreaterThanOrEqual;
		return this;
	}

	public PersonQuery yearLessThanOrEqual(Integer yearLessThanOrEqual) {
		if (yearLessThanOrEqual == null) {
			throw new RuntimeException("year is null");
		}
		this.yearLessThanOrEqual = yearLessThanOrEqual;
		return this;
	}

}