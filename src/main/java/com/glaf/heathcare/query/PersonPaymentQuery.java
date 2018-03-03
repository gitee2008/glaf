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

public class PersonPaymentQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected List<String> ids;
	protected String gradeId;
	protected List<String> gradeIds;
	protected String personId;
	protected List<String> personIds;
	protected String type;
	protected Integer semester;
	protected Integer year;
	protected Integer month;
	protected String remarkLike;
	protected Integer businessStatus;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	public PersonPaymentQuery() {

	}

	public PersonPaymentQuery businessStatus(Integer businessStatus) {
		if (businessStatus == null) {
			throw new RuntimeException("businessStatus is null");
		}
		this.businessStatus = businessStatus;
		return this;
	}

	public PersonPaymentQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public PersonPaymentQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public Integer getBusinessStatus() {
		return businessStatus;
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

	public List<String> getGradeIds() {
		return gradeIds;
	}

	public List<String> getIds() {
		return ids;
	}

	public Integer getMonth() {
		return month;
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

			if ("type".equals(sortColumn)) {
				orderBy = "E.TYPE_" + a_x;
			}

			if ("money".equals(sortColumn)) {
				orderBy = "E.MONEY_" + a_x;
			}

			if ("payTime".equals(sortColumn)) {
				orderBy = "E.PAYTIME_" + a_x;
			}

			if ("semester".equals(sortColumn)) {
				orderBy = "E.SEMESTER_" + a_x;
			}

			if ("year".equals(sortColumn)) {
				orderBy = "E.YEAR_" + a_x;
			}

			if ("month".equals(sortColumn)) {
				orderBy = "E.MONTH_" + a_x;
			}

			if ("remark".equals(sortColumn)) {
				orderBy = "E.REMARK_" + a_x;
			}

			if ("businessStatus".equals(sortColumn)) {
				orderBy = "E.BUSINESSSTATUS_" + a_x;
			}

			if ("createBy".equals(sortColumn)) {
				orderBy = "E.CREATEBY_" + a_x;
			}

			if ("createTime".equals(sortColumn)) {
				orderBy = "E.CREATETIME_" + a_x;
			}

			if ("confirmBy".equals(sortColumn)) {
				orderBy = "E.CONFIRMBY_" + a_x;
			}

			if ("confirmTime".equals(sortColumn)) {
				orderBy = "E.CONFIRMTIME_" + a_x;
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

	public String getPersonId() {
		return personId;
	}

	public List<String> getPersonIds() {
		return personIds;
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

	public Integer getSemester() {
		return semester;
	}

	public String getType() {
		return type;
	}

	public Integer getYear() {
		return year;
	}

	public PersonPaymentQuery gradeId(String gradeId) {
		if (gradeId == null) {
			throw new RuntimeException("gradeId is null");
		}
		this.gradeId = gradeId;
		return this;
	}

	public PersonPaymentQuery gradeIds(List<String> gradeIds) {
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
		addColumn("type", "TYPE_");
		addColumn("money", "MONEY_");
		addColumn("payTime", "PAYTIME_");
		addColumn("semester", "SEMESTER_");
		addColumn("year", "YEAR_");
		addColumn("month", "MONTH_");
		addColumn("remark", "REMARK_");
		addColumn("businessStatus", "BUSINESSSTATUS_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
		addColumn("confirmBy", "CONFIRMBY_");
		addColumn("confirmTime", "CONFIRMTIME_");
		addColumn("updateBy", "UPDATEBY_");
		addColumn("updateTime", "UPDATETIME_");
	}

	public PersonPaymentQuery month(Integer month) {
		if (month == null) {
			throw new RuntimeException("month is null");
		}
		this.month = month;
		return this;
	}

	public PersonPaymentQuery personId(String personId) {
		if (personId == null) {
			throw new RuntimeException("personId is null");
		}
		this.personId = personId;
		return this;
	}

	public PersonPaymentQuery personIds(List<String> personIds) {
		if (personIds == null) {
			throw new RuntimeException("personIds is empty ");
		}
		this.personIds = personIds;
		return this;
	}

	public PersonPaymentQuery remarkLike(String remarkLike) {
		if (remarkLike == null) {
			throw new RuntimeException("remark is null");
		}
		this.remarkLike = remarkLike;
		return this;
	}

	public PersonPaymentQuery semester(Integer semester) {
		if (semester == null) {
			throw new RuntimeException("semester is null");
		}
		this.semester = semester;
		return this;
	}

	public void setBusinessStatus(Integer businessStatus) {
		this.businessStatus = businessStatus;
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

	public void setGradeIds(List<String> gradeIds) {
		this.gradeIds = gradeIds;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public void setPersonIds(List<String> personIds) {
		this.personIds = personIds;
	}

	public void setRemarkLike(String remarkLike) {
		this.remarkLike = remarkLike;
	}

	public void setSemester(Integer semester) {
		this.semester = semester;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public PersonPaymentQuery tenantId(String tenantId) {
		if (tenantId == null) {
			throw new RuntimeException("tenantId is null");
		}
		this.tenantId = tenantId;
		return this;
	}

	public PersonPaymentQuery type(String type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		this.type = type;
		return this;
	}

	public PersonPaymentQuery year(Integer year) {
		if (year == null) {
			throw new RuntimeException("year is null");
		}
		this.year = year;
		return this;
	}

}