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

public class VaccinationQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected List<String> tenantIds;
	protected String gradeId;
	protected List<String> gradeIds;
	protected String personId;
	protected List<String> personIds;
	protected String name;
	protected String nameLike;
	protected String vaccine;
	protected String vaccineLike;
	protected Date inoculateDateGreaterThanOrEqual;
	protected Date inoculateDateLessThanOrEqual;
	protected String doctor;
	protected String doctorLike;
	protected Integer year;
	protected Integer month;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	public VaccinationQuery() {

	}

	public VaccinationQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public VaccinationQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public VaccinationQuery doctor(String doctor) {
		if (doctor == null) {
			throw new RuntimeException("doctor is null");
		}
		this.doctor = doctor;
		return this;
	}

	public VaccinationQuery doctorLike(String doctorLike) {
		if (doctorLike == null) {
			throw new RuntimeException("doctor is null");
		}
		this.doctorLike = doctorLike;
		return this;
	}

	public Date getCreateTimeGreaterThanOrEqual() {
		return createTimeGreaterThanOrEqual;
	}

	public Date getCreateTimeLessThanOrEqual() {
		return createTimeLessThanOrEqual;
	}

	public String getDoctor() {
		return doctor;
	}

	public String getDoctorLike() {
		if (doctorLike != null && doctorLike.trim().length() > 0) {
			if (!doctorLike.startsWith("%")) {
				doctorLike = "%" + doctorLike;
			}
			if (!doctorLike.endsWith("%")) {
				doctorLike = doctorLike + "%";
			}
		}
		return doctorLike;
	}

	public String getGradeId() {
		return gradeId;
	}

	public List<String> getGradeIds() {
		return gradeIds;
	}

	public Date getInoculateDateGreaterThanOrEqual() {
		return inoculateDateGreaterThanOrEqual;
	}

	public Date getInoculateDateLessThanOrEqual() {
		return inoculateDateLessThanOrEqual;
	}

	public Integer getMonth() {
		return month;
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

			if ("personId".equals(sortColumn)) {
				orderBy = "E.PERSONID_" + a_x;
			}

			if ("name".equals(sortColumn)) {
				orderBy = "E.NAME_" + a_x;
			}

			if ("sex".equals(sortColumn)) {
				orderBy = "E.SEX_" + a_x;
			}

			if ("vaccine".equals(sortColumn)) {
				orderBy = "E.VACCINE_" + a_x;
			}

			if ("sortNo".equals(sortColumn)) {
				orderBy = "E.SORTNO_" + a_x;
			}

			if ("inoculateDate".equals(sortColumn)) {
				orderBy = "E.INOCULATEDATE_" + a_x;
			}

			if ("doctor".equals(sortColumn)) {
				orderBy = "E.DOCTOR_" + a_x;
			}

			if ("year".equals(sortColumn)) {
				orderBy = "E.YEAR_" + a_x;
			}

			if ("month".equals(sortColumn)) {
				orderBy = "E.MONTH_" + a_x;
			}

			if ("createBy".equals(sortColumn)) {
				orderBy = "E.CREATEBY_" + a_x;
			}

			if ("createTime".equals(sortColumn)) {
				orderBy = "E.CREATETIME_" + a_x;
			}

		}
		return orderBy;
	}

	public String getPersonId() {
		return personId;
	}

	public List<String> getPersonIds() {
		return personIds;
	}

	public List<String> getTenantIds() {
		return tenantIds;
	}

	public String getVaccine() {
		return vaccine;
	}

	public String getVaccineLike() {
		if (vaccineLike != null && vaccineLike.trim().length() > 0) {
			if (!vaccineLike.startsWith("%")) {
				vaccineLike = "%" + vaccineLike;
			}
			if (!vaccineLike.endsWith("%")) {
				vaccineLike = vaccineLike + "%";
			}
		}
		return vaccineLike;
	}

	public Integer getYear() {
		return year;
	}

	public VaccinationQuery gradeId(String gradeId) {
		if (gradeId == null) {
			throw new RuntimeException("gradeId is null");
		}
		this.gradeId = gradeId;
		return this;
	}

	public VaccinationQuery gradeIds(List<String> gradeIds) {
		if (gradeIds == null) {
			throw new RuntimeException("gradeIds is empty ");
		}
		this.gradeIds = gradeIds;
		return this;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("tenantId", "TENANTID_");
		addColumn("gradeId", "GRADEID_");
		addColumn("personId", "PERSONID_");
		addColumn("name", "NAME_");
		addColumn("sex", "SEX_");
		addColumn("vaccine", "VACCINE_");
		addColumn("sortNo", "SORTNO_");
		addColumn("inoculateDate", "INOCULATEDATE_");
		addColumn("doctor", "DOCTOR_");
		addColumn("year", "YEAR_");
		addColumn("month", "MONTH_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
	}

	public VaccinationQuery inoculateDateGreaterThanOrEqual(Date inoculateDateGreaterThanOrEqual) {
		if (inoculateDateGreaterThanOrEqual == null) {
			throw new RuntimeException("inoculateDate is null");
		}
		this.inoculateDateGreaterThanOrEqual = inoculateDateGreaterThanOrEqual;
		return this;
	}

	public VaccinationQuery inoculateDateLessThanOrEqual(Date inoculateDateLessThanOrEqual) {
		if (inoculateDateLessThanOrEqual == null) {
			throw new RuntimeException("inoculateDate is null");
		}
		this.inoculateDateLessThanOrEqual = inoculateDateLessThanOrEqual;
		return this;
	}

	public VaccinationQuery month(Integer month) {
		if (month == null) {
			throw new RuntimeException("month is null");
		}
		this.month = month;
		return this;
	}

	public VaccinationQuery name(String name) {
		if (name == null) {
			throw new RuntimeException("name is null");
		}
		this.name = name;
		return this;
	}

	public VaccinationQuery nameLike(String nameLike) {
		if (nameLike == null) {
			throw new RuntimeException("name is null");
		}
		this.nameLike = nameLike;
		return this;
	}

	public VaccinationQuery personId(String personId) {
		if (personId == null) {
			throw new RuntimeException("personId is null");
		}
		this.personId = personId;
		return this;
	}

	public VaccinationQuery personIds(List<String> personIds) {
		if (personIds == null) {
			throw new RuntimeException("personIds is empty ");
		}
		this.personIds = personIds;
		return this;
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}

	public void setDoctorLike(String doctorLike) {
		this.doctorLike = doctorLike;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public void setGradeIds(List<String> gradeIds) {
		this.gradeIds = gradeIds;
	}

	public void setInoculateDateGreaterThanOrEqual(Date inoculateDateGreaterThanOrEqual) {
		this.inoculateDateGreaterThanOrEqual = inoculateDateGreaterThanOrEqual;
	}

	public void setInoculateDateLessThanOrEqual(Date inoculateDateLessThanOrEqual) {
		this.inoculateDateLessThanOrEqual = inoculateDateLessThanOrEqual;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNameLike(String nameLike) {
		this.nameLike = nameLike;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public void setPersonIds(List<String> personIds) {
		this.personIds = personIds;
	}

	public void setTenantIds(List<String> tenantIds) {
		this.tenantIds = tenantIds;
	}

	public void setVaccine(String vaccine) {
		this.vaccine = vaccine;
	}

	public void setVaccineLike(String vaccineLike) {
		this.vaccineLike = vaccineLike;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public VaccinationQuery tenantIds(List<String> tenantIds) {
		if (tenantIds == null) {
			throw new RuntimeException("tenantIds is empty ");
		}
		this.tenantIds = tenantIds;
		return this;
	}

	public VaccinationQuery vaccine(String vaccine) {
		if (vaccine == null) {
			throw new RuntimeException("vaccine is null");
		}
		this.vaccine = vaccine;
		return this;
	}

	public VaccinationQuery vaccineLike(String vaccineLike) {
		if (vaccineLike == null) {
			throw new RuntimeException("vaccine is null");
		}
		this.vaccineLike = vaccineLike;
		return this;
	}

	public VaccinationQuery year(Integer year) {
		if (year == null) {
			throw new RuntimeException("year is null");
		}
		this.year = year;
		return this;
	}

}