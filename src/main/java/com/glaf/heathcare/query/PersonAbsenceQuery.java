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

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.glaf.core.query.DataQuery;

public class PersonAbsenceQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected String id;
	protected Collection<String> appActorIds;
	protected List<String> tenantIds;
	protected String gradeId;
	protected String gradeIdLike;
	protected List<String> gradeIds;
	protected String personId;
	protected String personIdLike;
	protected List<String> personIds;
	protected String name;
	protected String nameLike;
	protected List<String> names;
	protected Integer year;
	protected Integer month;
	protected Integer day;
	protected Integer dayGreaterThanOrEqual;
	protected Integer dayLessThanOrEqual;
	protected List<Integer> days;
	protected String section;
	protected String sectionLike;
	protected List<String> sections;
	protected String remark;
	protected String remarkLike;
	protected List<String> remarks;
	protected String createByLike;
	protected List<String> createBys;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;
	protected String tableSuffix;

	public PersonAbsenceQuery() {

	}

	public PersonAbsenceQuery createBy(String createBy) {
		if (createBy == null) {
			throw new RuntimeException("createBy is null");
		}
		this.createBy = createBy;
		return this;
	}

	public PersonAbsenceQuery createByLike(String createByLike) {
		if (createByLike == null) {
			throw new RuntimeException("createBy is null");
		}
		this.createByLike = createByLike;
		return this;
	}

	public PersonAbsenceQuery createBys(List<String> createBys) {
		if (createBys == null) {
			throw new RuntimeException("createBys is empty ");
		}
		this.createBys = createBys;
		return this;
	}

	public PersonAbsenceQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public PersonAbsenceQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public PersonAbsenceQuery day(Integer day) {
		if (day == null) {
			throw new RuntimeException("day is null");
		}
		this.day = day;
		return this;
	}

	public PersonAbsenceQuery dayGreaterThanOrEqual(Integer dayGreaterThanOrEqual) {
		if (dayGreaterThanOrEqual == null) {
			throw new RuntimeException("day is null");
		}
		this.dayGreaterThanOrEqual = dayGreaterThanOrEqual;
		return this;
	}

	public PersonAbsenceQuery dayLessThanOrEqual(Integer dayLessThanOrEqual) {
		if (dayLessThanOrEqual == null) {
			throw new RuntimeException("day is null");
		}
		this.dayLessThanOrEqual = dayLessThanOrEqual;
		return this;
	}

	public PersonAbsenceQuery days(List<Integer> days) {
		if (days == null) {
			throw new RuntimeException("days is empty ");
		}
		this.days = days;
		return this;
	}

	public Collection<String> getAppActorIds() {
		return appActorIds;
	}

	public String getCreateBy() {
		return createBy;
	}

	public String getCreateByLike() {
		if (createByLike != null && createByLike.trim().length() > 0) {
			if (!createByLike.startsWith("%")) {
				createByLike = "%" + createByLike;
			}
			if (!createByLike.endsWith("%")) {
				createByLike = createByLike + "%";
			}
		}
		return createByLike;
	}

	public List<String> getCreateBys() {
		return createBys;
	}

	public Date getCreateTimeGreaterThanOrEqual() {
		return createTimeGreaterThanOrEqual;
	}

	public Date getCreateTimeLessThanOrEqual() {
		return createTimeLessThanOrEqual;
	}

	public Integer getDay() {
		return day;
	}

	public Integer getDayGreaterThanOrEqual() {
		return dayGreaterThanOrEqual;
	}

	public Integer getDayLessThanOrEqual() {
		return dayLessThanOrEqual;
	}

	public List<Integer> getDays() {
		return days;
	}

	public String getGradeId() {
		return gradeId;
	}

	public String getGradeIdLike() {
		if (gradeIdLike != null && gradeIdLike.trim().length() > 0) {
			if (!gradeIdLike.startsWith("%")) {
				gradeIdLike = "%" + gradeIdLike;
			}
			if (!gradeIdLike.endsWith("%")) {
				gradeIdLike = gradeIdLike + "%";
			}
		}
		return gradeIdLike;
	}

	public List<String> getGradeIds() {
		return gradeIds;
	}

	public String getId() {
		return id;
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

	public List<String> getNames() {
		return names;
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

			if ("day".equals(sortColumn)) {
				orderBy = "E.DAY_" + a_x;
			}

			if ("section".equals(sortColumn)) {
				orderBy = "E.SECTION_" + a_x;
			}

			if ("status".equals(sortColumn)) {
				orderBy = "E.STATUS_" + a_x;
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

		}
		return orderBy;
	}

	public String getPersonId() {
		return personId;
	}

	public String getPersonIdLike() {
		if (personIdLike != null && personIdLike.trim().length() > 0) {
			if (!personIdLike.startsWith("%")) {
				personIdLike = "%" + personIdLike;
			}
			if (!personIdLike.endsWith("%")) {
				personIdLike = personIdLike + "%";
			}
		}
		return personIdLike;
	}

	public List<String> getPersonIds() {
		return personIds;
	}

	public String getRemark() {
		return remark;
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

	public List<String> getRemarks() {
		return remarks;
	}

	public String getSection() {
		return section;
	}

	public String getSectionLike() {
		if (sectionLike != null && sectionLike.trim().length() > 0) {
			if (!sectionLike.startsWith("%")) {
				sectionLike = "%" + sectionLike;
			}
			if (!sectionLike.endsWith("%")) {
				sectionLike = sectionLike + "%";
			}
		}
		return sectionLike;
	}

	public List<String> getSections() {
		return sections;
	}

	public Integer getStatus() {
		return status;
	}

	public Integer getStatusGreaterThanOrEqual() {
		return statusGreaterThanOrEqual;
	}

	public Integer getStatusLessThanOrEqual() {
		return statusLessThanOrEqual;
	}

	public String getTableSuffix() {
		return tableSuffix;
	}

	public List<String> getTenantIds() {
		return tenantIds;
	}

	public Integer getYear() {
		return year;
	}

	public PersonAbsenceQuery gradeId(String gradeId) {
		if (gradeId == null) {
			throw new RuntimeException("gradeId is null");
		}
		this.gradeId = gradeId;
		return this;
	}

	public PersonAbsenceQuery gradeIdLike(String gradeIdLike) {
		if (gradeIdLike == null) {
			throw new RuntimeException("gradeId is null");
		}
		this.gradeIdLike = gradeIdLike;
		return this;
	}

	public PersonAbsenceQuery gradeIds(List<String> gradeIds) {
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
		addColumn("day", "DAY_");
		addColumn("section", "SECTION_");
		addColumn("status", "STATUS_");
		addColumn("remark", "REMARK_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
	}

	public PersonAbsenceQuery month(Integer month) {
		if (month == null) {
			throw new RuntimeException("month is null");
		}
		this.month = month;
		return this;
	}

	public PersonAbsenceQuery name(String name) {
		if (name == null) {
			throw new RuntimeException("name is null");
		}
		this.name = name;
		return this;
	}

	public PersonAbsenceQuery nameLike(String nameLike) {
		if (nameLike == null) {
			throw new RuntimeException("name is null");
		}
		this.nameLike = nameLike;
		return this;
	}

	public PersonAbsenceQuery names(List<String> names) {
		if (names == null) {
			throw new RuntimeException("names is empty ");
		}
		this.names = names;
		return this;
	}

	public PersonAbsenceQuery personId(String personId) {
		if (personId == null) {
			throw new RuntimeException("personId is null");
		}
		this.personId = personId;
		return this;
	}

	public PersonAbsenceQuery personIdLike(String personIdLike) {
		if (personIdLike == null) {
			throw new RuntimeException("personId is null");
		}
		this.personIdLike = personIdLike;
		return this;
	}

	public PersonAbsenceQuery personIds(List<String> personIds) {
		if (personIds == null) {
			throw new RuntimeException("personIds is empty ");
		}
		this.personIds = personIds;
		return this;
	}

	public PersonAbsenceQuery remark(String remark) {
		if (remark == null) {
			throw new RuntimeException("remark is null");
		}
		this.remark = remark;
		return this;
	}

	public PersonAbsenceQuery remarkLike(String remarkLike) {
		if (remarkLike == null) {
			throw new RuntimeException("remark is null");
		}
		this.remarkLike = remarkLike;
		return this;
	}

	public PersonAbsenceQuery remarks(List<String> remarks) {
		if (remarks == null) {
			throw new RuntimeException("remarks is empty ");
		}
		this.remarks = remarks;
		return this;
	}

	public PersonAbsenceQuery section(String section) {
		if (section == null) {
			throw new RuntimeException("section is null");
		}
		this.section = section;
		return this;
	}

	public PersonAbsenceQuery sectionLike(String sectionLike) {
		if (sectionLike == null) {
			throw new RuntimeException("section is null");
		}
		this.sectionLike = sectionLike;
		return this;
	}

	public PersonAbsenceQuery sections(List<String> sections) {
		if (sections == null) {
			throw new RuntimeException("sections is empty ");
		}
		this.sections = sections;
		return this;
	}

	public void setAppActorIds(Collection<String> appActorIds) {
		this.appActorIds = appActorIds;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateByLike(String createByLike) {
		this.createByLike = createByLike;
	}

	public void setCreateBys(List<String> createBys) {
		this.createBys = createBys;
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public void setDayGreaterThanOrEqual(Integer dayGreaterThanOrEqual) {
		this.dayGreaterThanOrEqual = dayGreaterThanOrEqual;
	}

	public void setDayLessThanOrEqual(Integer dayLessThanOrEqual) {
		this.dayLessThanOrEqual = dayLessThanOrEqual;
	}

	public void setDays(List<Integer> days) {
		this.days = days;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public void setGradeIdLike(String gradeIdLike) {
		this.gradeIdLike = gradeIdLike;
	}

	public void setGradeIds(List<String> gradeIds) {
		this.gradeIds = gradeIds;
	}

	public void setId(String id) {
		this.id = id;
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

	public void setNames(List<String> names) {
		this.names = names;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public void setPersonIdLike(String personIdLike) {
		this.personIdLike = personIdLike;
	}

	public void setPersonIds(List<String> personIds) {
		this.personIds = personIds;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setRemarkLike(String remarkLike) {
		this.remarkLike = remarkLike;
	}

	public void setRemarks(List<String> remarks) {
		this.remarks = remarks;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public void setSectionLike(String sectionLike) {
		this.sectionLike = sectionLike;
	}

	public void setSections(List<String> sections) {
		this.sections = sections;
	}

	public void setTableSuffix(String tableSuffix) {
		this.tableSuffix = tableSuffix;
	}

	public void setTenantIds(List<String> tenantIds) {
		this.tenantIds = tenantIds;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public PersonAbsenceQuery tableSuffix(String tableSuffix) {
		if (tableSuffix == null) {
			throw new RuntimeException("tableSuffix is null");
		}
		this.tableSuffix = tableSuffix;
		return this;
	}

	public PersonAbsenceQuery tenantIds(List<String> tenantIds) {
		if (tenantIds == null) {
			throw new RuntimeException("tenantIds is empty ");
		}
		this.tenantIds = tenantIds;
		return this;
	}

	public PersonAbsenceQuery year(Integer year) {
		if (year == null) {
			throw new RuntimeException("year is null");
		}
		this.year = year;
		return this;
	}

}