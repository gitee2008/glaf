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

public class ActualRepastPersonQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected Long organizationId;
	protected List<Long> organizationIds;
	protected List<String> tenantIds;
	protected Integer year;
	protected Integer month;
	protected Integer monthGreaterThanOrEqual;
	protected Integer monthLessThanOrEqual;
	protected List<Integer> months;
	protected Integer day;
	protected Integer dayGreaterThanOrEqual;
	protected Integer dayLessThanOrEqual;
	protected List<Integer> days;
	protected Integer week;
	protected Integer weekGreaterThanOrEqual;
	protected Integer weekLessThanOrEqual;
	protected List<Integer> weeks;
	protected Integer fullDay;
	protected Integer fullDayGreaterThanOrEqual;
	protected Integer fullDayLessThanOrEqual;
	protected List<Integer> fullDays;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	public ActualRepastPersonQuery() {

	}

	public ActualRepastPersonQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public ActualRepastPersonQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public ActualRepastPersonQuery day(Integer day) {
		if (day == null) {
			throw new RuntimeException("day is null");
		}
		this.day = day;
		return this;
	}

	public ActualRepastPersonQuery dayGreaterThanOrEqual(Integer dayGreaterThanOrEqual) {
		if (dayGreaterThanOrEqual == null) {
			throw new RuntimeException("day is null");
		}
		this.dayGreaterThanOrEqual = dayGreaterThanOrEqual;
		return this;
	}

	public ActualRepastPersonQuery dayLessThanOrEqual(Integer dayLessThanOrEqual) {
		if (dayLessThanOrEqual == null) {
			throw new RuntimeException("day is null");
		}
		this.dayLessThanOrEqual = dayLessThanOrEqual;
		return this;
	}

	public ActualRepastPersonQuery days(List<Integer> days) {
		if (days == null) {
			throw new RuntimeException("days is empty ");
		}
		this.days = days;
		return this;
	}

	public ActualRepastPersonQuery fullDay(Integer fullDay) {
		if (fullDay == null) {
			throw new RuntimeException("fullDay is null");
		}
		this.fullDay = fullDay;
		return this;
	}

	public ActualRepastPersonQuery fullDayGreaterThanOrEqual(Integer fullDayGreaterThanOrEqual) {
		if (fullDayGreaterThanOrEqual == null) {
			throw new RuntimeException("fullDay is null");
		}
		this.fullDayGreaterThanOrEqual = fullDayGreaterThanOrEqual;
		return this;
	}

	public ActualRepastPersonQuery fullDayLessThanOrEqual(Integer fullDayLessThanOrEqual) {
		if (fullDayLessThanOrEqual == null) {
			throw new RuntimeException("fullDay is null");
		}
		this.fullDayLessThanOrEqual = fullDayLessThanOrEqual;
		return this;
	}

	public ActualRepastPersonQuery fullDays(List<Integer> fullDays) {
		if (fullDays == null) {
			throw new RuntimeException("fullDays is empty ");
		}
		this.fullDays = fullDays;
		return this;
	}

	public String getCreateBy() {
		return createBy;
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

	public Integer getFullDay() {
		return fullDay;
	}

	public Integer getFullDayGreaterThanOrEqual() {
		return fullDayGreaterThanOrEqual;
	}

	public Integer getFullDayLessThanOrEqual() {
		return fullDayLessThanOrEqual;
	}

	public List<Integer> getFullDays() {
		return fullDays;
	}

	public Integer getMonth() {
		return month;
	}

	public Integer getMonthGreaterThanOrEqual() {
		return monthGreaterThanOrEqual;
	}

	public Integer getMonthLessThanOrEqual() {
		return monthLessThanOrEqual;
	}

	public List<Integer> getMonths() {
		return months;
	}

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("organizationId".equals(sortColumn)) {
				orderBy = "E.ORGANIZATIONID_" + a_x;
			}

			if ("tenantId".equals(sortColumn)) {
				orderBy = "E.TENANTID_" + a_x;
			}

			if ("male".equals(sortColumn)) {
				orderBy = "E.MALE_" + a_x;
			}

			if ("female".equals(sortColumn)) {
				orderBy = "E.FEMALE_" + a_x;
			}

			if ("age".equals(sortColumn)) {
				orderBy = "E.AGE_" + a_x;
			}

			if ("sortNo".equals(sortColumn)) {
				orderBy = "E.SORTNO_" + a_x;
			}

			if ("year".equals(sortColumn)) {
				orderBy = "E.YEAR_" + a_x;
			}

			if ("month".equals(sortColumn)) {
				orderBy = "E.MONTH_" + a_x;
			}

			if ("day".equals(sortColumn)) {
				orderBy = "E.DAY_" + a_x;
			}

			if ("week".equals(sortColumn)) {
				orderBy = "E.WEEK_" + a_x;
			}

			if ("fullDay".equals(sortColumn)) {
				orderBy = "E.FULLDAY_" + a_x;
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

	public Long getOrganizationId() {
		return organizationId;
	}

	public List<Long> getOrganizationIds() {
		return organizationIds;
	}

	public List<String> getTenantIds() {
		return tenantIds;
	}

	public Integer getWeek() {
		return week;
	}

	public Integer getWeekGreaterThanOrEqual() {
		return weekGreaterThanOrEqual;
	}

	public Integer getWeekLessThanOrEqual() {
		return weekLessThanOrEqual;
	}

	public List<Integer> getWeeks() {
		return weeks;
	}

	public Integer getYear() {
		return year;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("organizationId", "ORGANIZATIONID_");
		addColumn("tenantId", "TENANTID_");
		addColumn("male", "MALE_");
		addColumn("female", "FEMALE_");
		addColumn("age", "AGE_");
		addColumn("sortNo", "SORTNO_");
		addColumn("year", "YEAR_");
		addColumn("month", "MONTH_");
		addColumn("day", "DAY_");
		addColumn("week", "WEEK_");
		addColumn("fullDay", "FULLDAY_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
		addColumn("updateBy", "UPDATEBY_");
		addColumn("updateTime", "UPDATETIME_");
	}

	public ActualRepastPersonQuery month(Integer month) {
		if (month == null) {
			throw new RuntimeException("month is null");
		}
		this.month = month;
		return this;
	}

	public ActualRepastPersonQuery monthGreaterThanOrEqual(Integer monthGreaterThanOrEqual) {
		if (monthGreaterThanOrEqual == null) {
			throw new RuntimeException("month is null");
		}
		this.monthGreaterThanOrEqual = monthGreaterThanOrEqual;
		return this;
	}

	public ActualRepastPersonQuery monthLessThanOrEqual(Integer monthLessThanOrEqual) {
		if (monthLessThanOrEqual == null) {
			throw new RuntimeException("month is null");
		}
		this.monthLessThanOrEqual = monthLessThanOrEqual;
		return this;
	}

	public ActualRepastPersonQuery months(List<Integer> months) {
		if (months == null) {
			throw new RuntimeException("months is empty ");
		}
		this.months = months;
		return this;
	}

	public ActualRepastPersonQuery organizationId(Long organizationId) {
		if (organizationId == null) {
			throw new RuntimeException("organizationId is null");
		}
		this.organizationId = organizationId;
		return this;
	}

	public ActualRepastPersonQuery organizationIds(List<Long> organizationIds) {
		if (organizationIds == null) {
			throw new RuntimeException("organizationIds is empty ");
		}
		this.organizationIds = organizationIds;
		return this;
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

	public void setFullDay(Integer fullDay) {
		this.fullDay = fullDay;
	}

	public void setFullDayGreaterThanOrEqual(Integer fullDayGreaterThanOrEqual) {
		this.fullDayGreaterThanOrEqual = fullDayGreaterThanOrEqual;
	}

	public void setFullDayLessThanOrEqual(Integer fullDayLessThanOrEqual) {
		this.fullDayLessThanOrEqual = fullDayLessThanOrEqual;
	}

	public void setFullDays(List<Integer> fullDays) {
		this.fullDays = fullDays;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public void setMonthGreaterThanOrEqual(Integer monthGreaterThanOrEqual) {
		this.monthGreaterThanOrEqual = monthGreaterThanOrEqual;
	}

	public void setMonthLessThanOrEqual(Integer monthLessThanOrEqual) {
		this.monthLessThanOrEqual = monthLessThanOrEqual;
	}

	public void setMonths(List<Integer> months) {
		this.months = months;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public void setOrganizationIds(List<Long> organizationIds) {
		this.organizationIds = organizationIds;
	}

	public void setTenantIds(List<String> tenantIds) {
		this.tenantIds = tenantIds;
	}

	public void setWeek(Integer week) {
		this.week = week;
	}

	public void setWeekGreaterThanOrEqual(Integer weekGreaterThanOrEqual) {
		this.weekGreaterThanOrEqual = weekGreaterThanOrEqual;
	}

	public void setWeekLessThanOrEqual(Integer weekLessThanOrEqual) {
		this.weekLessThanOrEqual = weekLessThanOrEqual;
	}

	public void setWeeks(List<Integer> weeks) {
		this.weeks = weeks;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public ActualRepastPersonQuery tenantIds(List<String> tenantIds) {
		if (tenantIds == null) {
			throw new RuntimeException("tenantIds is empty ");
		}
		this.tenantIds = tenantIds;
		return this;
	}

	public ActualRepastPersonQuery week(Integer week) {
		if (week == null) {
			throw new RuntimeException("week is null");
		}
		this.week = week;
		return this;
	}

	public ActualRepastPersonQuery weekGreaterThanOrEqual(Integer weekGreaterThanOrEqual) {
		if (weekGreaterThanOrEqual == null) {
			throw new RuntimeException("week is null");
		}
		this.weekGreaterThanOrEqual = weekGreaterThanOrEqual;
		return this;
	}

	public ActualRepastPersonQuery weekLessThanOrEqual(Integer weekLessThanOrEqual) {
		if (weekLessThanOrEqual == null) {
			throw new RuntimeException("week is null");
		}
		this.weekLessThanOrEqual = weekLessThanOrEqual;
		return this;
	}

	public ActualRepastPersonQuery weeks(List<Integer> weeks) {
		if (weeks == null) {
			throw new RuntimeException("weeks is empty ");
		}
		this.weeks = weeks;
		return this;
	}

	public ActualRepastPersonQuery year(Integer year) {
		if (year == null) {
			throw new RuntimeException("year is null");
		}
		this.year = year;
		return this;
	}

}