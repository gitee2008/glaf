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

public class MonthlyFeeQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected List<String> tenantIds;
	protected Integer year;
	protected Integer month;
	protected Integer semester;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	public MonthlyFeeQuery() {

	}

	public MonthlyFeeQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public MonthlyFeeQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public Date getCreateTimeGreaterThanOrEqual() {
		return createTimeGreaterThanOrEqual;
	}

	public Date getCreateTimeLessThanOrEqual() {
		return createTimeLessThanOrEqual;
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

			if ("monthLeft".equals(sortColumn)) {
				orderBy = "E.MONTHLEFT_" + a_x;
			}

			if ("monthTotalLeft".equals(sortColumn)) {
				orderBy = "E.MONTHTOTALLEFT_" + a_x;
			}

			if ("leftPercent".equals(sortColumn)) {
				orderBy = "E.LEFTPERCENT_" + a_x;
			}

			if ("exceedPercent".equals(sortColumn)) {
				orderBy = "E.EXCEEDPERCENT_" + a_x;
			}

			if ("personMonthlyFee".equals(sortColumn)) {
				orderBy = "E.PERSONMONTHLYFEE_" + a_x;
			}

			if ("fuelFee".equals(sortColumn)) {
				orderBy = "E.FUELFEE_" + a_x;
			}

			if ("laborFee".equals(sortColumn)) {
				orderBy = "E.LABORFEE_" + a_x;
			}

			if ("dessertFee".equals(sortColumn)) {
				orderBy = "E.DESSERTFEE_" + a_x;
			}

			if ("otherFee".equals(sortColumn)) {
				orderBy = "E.OTHERFEE_" + a_x;
			}

			if ("workDay".equals(sortColumn)) {
				orderBy = "E.WORKDAY_" + a_x;
			}

			if ("totalRepastPerson".equals(sortColumn)) {
				orderBy = "E.TOTALREPASTPERSON_" + a_x;
			}

			if ("year".equals(sortColumn)) {
				orderBy = "E.YEAR_" + a_x;
			}

			if ("month".equals(sortColumn)) {
				orderBy = "E.MONTH_" + a_x;
			}

			if ("semester".equals(sortColumn)) {
				orderBy = "E.SEMESTER_" + a_x;
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

	public Integer getSemester() {
		return semester;
	}

	public List<String> getTenantIds() {
		return tenantIds;
	}

	public Integer getYear() {
		return year;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("tenantId", "TENANTID_");
		addColumn("monthLeft", "MONTHLEFT_");
		addColumn("monthTotalLeft", "MONTHTOTALLEFT_");
		addColumn("leftPercent", "LEFTPERCENT_");
		addColumn("exceedPercent", "EXCEEDPERCENT_");
		addColumn("personMonthlyFee", "PERSONMONTHLYFEE_");
		addColumn("fuelFee", "FUELFEE_");
		addColumn("laborFee", "LABORFEE_");
		addColumn("dessertFee", "DESSERTFEE_");
		addColumn("otherFee", "OTHERFEE_");
		addColumn("workDay", "WORKDAY_");
		addColumn("totalRepastPerson", "TOTALREPASTPERSON_");
		addColumn("year", "YEAR_");
		addColumn("month", "MONTH_");
		addColumn("semester", "SEMESTER_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
		addColumn("updateBy", "UPDATEBY_");
		addColumn("updateTime", "UPDATETIME_");
	}

	public MonthlyFeeQuery month(Integer month) {
		if (month == null) {
			throw new RuntimeException("month is null");
		}
		this.month = month;
		return this;
	}

	public MonthlyFeeQuery semester(Integer semester) {
		if (semester == null) {
			throw new RuntimeException("semester is null");
		}
		this.semester = semester;
		return this;
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public void setSemester(Integer semester) {
		this.semester = semester;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setTenantIds(List<String> tenantIds) {
		this.tenantIds = tenantIds;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public MonthlyFeeQuery tenantId(String tenantId) {
		if (tenantId == null) {
			throw new RuntimeException("tenantId is null");
		}
		this.tenantId = tenantId;
		return this;
	}

	public MonthlyFeeQuery tenantIds(List<String> tenantIds) {
		if (tenantIds == null) {
			throw new RuntimeException("tenantIds is empty ");
		}
		this.tenantIds = tenantIds;
		return this;
	}

	public MonthlyFeeQuery year(Integer year) {
		if (year == null) {
			throw new RuntimeException("year is null");
		}
		this.year = year;
		return this;
	}

}