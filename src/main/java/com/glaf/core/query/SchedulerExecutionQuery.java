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

package com.glaf.core.query;

import java.util.*;
import com.glaf.core.query.DataQuery;

public class SchedulerExecutionQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected List<Long> ids;
	protected Collection<String> appActorIds;
	protected String schedulerId;
	protected Integer runYear;
	protected Integer runMonth;
	protected Integer runWeek;
	protected Integer runQuarter;
	protected Integer runDay;
	protected Integer runDayGreaterThanOrEqual;
	protected Integer runDayLessThanOrEqual;
	protected String jobNo;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	public SchedulerExecutionQuery() {

	}

	public SchedulerExecutionQuery createTimeGreaterThanOrEqual(
			Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public SchedulerExecutionQuery createTimeLessThanOrEqual(
			Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public Collection<String> getAppActorIds() {
		return appActorIds;
	}

	public Date getCreateTimeGreaterThanOrEqual() {
		return createTimeGreaterThanOrEqual;
	}

	public Date getCreateTimeLessThanOrEqual() {
		return createTimeLessThanOrEqual;
	}

	public String getJobNo() {
		return jobNo;
	}

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("schedulerId".equals(sortColumn)) {
				orderBy = "E.SCHEDULERID_" + a_x;
			}

			if ("businessKey".equals(sortColumn)) {
				orderBy = "E.BUSINESSKEY_" + a_x;
			}

			if ("count".equals(sortColumn)) {
				orderBy = "E.COUNT_" + a_x;
			}

			if ("value".equals(sortColumn)) {
				orderBy = "E.VALUE_" + a_x;
			}

			if ("runYear".equals(sortColumn)) {
				orderBy = "E.RUNYEAR_" + a_x;
			}

			if ("runMonth".equals(sortColumn)) {
				orderBy = "E.RUNMONTH_" + a_x;
			}

			if ("runWeek".equals(sortColumn)) {
				orderBy = "E.RUNWEEK_" + a_x;
			}

			if ("runQuarter".equals(sortColumn)) {
				orderBy = "E.RUNQUARTER_" + a_x;
			}

			if ("runDay".equals(sortColumn)) {
				orderBy = "E.RUNDAY_" + a_x;
			}

			if ("runTime".equals(sortColumn)) {
				orderBy = "E.RUNTIME_" + a_x;
			}

			if ("jobNo".equals(sortColumn)) {
				orderBy = "E.JOBNO_" + a_x;
			}

			if ("status".equals(sortColumn)) {
				orderBy = "E.STATUS_" + a_x;
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

	public Integer getRunDay() {
		return runDay;
	}

	public Integer getRunDayGreaterThanOrEqual() {
		return runDayGreaterThanOrEqual;
	}

	public Integer getRunDayLessThanOrEqual() {
		return runDayLessThanOrEqual;
	}

	public Integer getRunMonth() {
		return runMonth;
	}

	public Integer getRunQuarter() {
		return runQuarter;
	}

	public Integer getRunWeek() {
		return runWeek;
	}

	public Integer getRunYear() {
		return runYear;
	}

	public String getSchedulerId() {
		return schedulerId;
	}

	public Integer getStatusGreaterThanOrEqual() {
		return statusGreaterThanOrEqual;
	}

	public Integer getStatusLessThanOrEqual() {
		return statusLessThanOrEqual;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("schedulerId", "SCHEDULERID_");
		addColumn("businessKey", "BUSINESSKEY_");
		addColumn("count", "COUNT_");
		addColumn("value", "VALUE_");
		addColumn("runYear", "RUNYEAR_");
		addColumn("runMonth", "RUNMONTH_");
		addColumn("runWeek", "RUNWEEK_");
		addColumn("runQuarter", "RUNQUARTER_");
		addColumn("runDay", "RUNDAY_");
		addColumn("runTime", "RUNTIME_");
		addColumn("jobNo", "JOBNO_");
		addColumn("status", "STATUS_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
	}

	public SchedulerExecutionQuery jobNo(String jobNo) {
		if (jobNo == null) {
			throw new RuntimeException("jobNo is null");
		}
		this.jobNo = jobNo;
		return this;
	}

	public SchedulerExecutionQuery runDay(Integer runDay) {
		if (runDay == null) {
			throw new RuntimeException("runDay is null");
		}
		this.runDay = runDay;
		return this;
	}

	public SchedulerExecutionQuery runDayGreaterThanOrEqual(
			Integer runDayGreaterThanOrEqual) {
		if (runDayGreaterThanOrEqual == null) {
			throw new RuntimeException("runDay is null");
		}
		this.runDayGreaterThanOrEqual = runDayGreaterThanOrEqual;
		return this;
	}

	public SchedulerExecutionQuery runDayLessThanOrEqual(
			Integer runDayLessThanOrEqual) {
		if (runDayLessThanOrEqual == null) {
			throw new RuntimeException("runDay is null");
		}
		this.runDayLessThanOrEqual = runDayLessThanOrEqual;
		return this;
	}

	public SchedulerExecutionQuery runMonth(Integer runMonth) {
		if (runMonth == null) {
			throw new RuntimeException("runMonth is null");
		}
		this.runMonth = runMonth;
		return this;
	}

	public SchedulerExecutionQuery runQuarter(Integer runQuarter) {
		if (runQuarter == null) {
			throw new RuntimeException("runQuarter is null");
		}
		this.runQuarter = runQuarter;
		return this;
	}

	public SchedulerExecutionQuery runWeek(Integer runWeek) {
		if (runWeek == null) {
			throw new RuntimeException("runWeek is null");
		}
		this.runWeek = runWeek;
		return this;
	}

	public SchedulerExecutionQuery runYear(Integer runYear) {
		if (runYear == null) {
			throw new RuntimeException("runYear is null");
		}
		this.runYear = runYear;
		return this;
	}

	public SchedulerExecutionQuery schedulerId(String schedulerId) {
		if (schedulerId == null) {
			throw new RuntimeException("schedulerId is null");
		}
		this.schedulerId = schedulerId;
		return this;
	}

	public void setAppActorIds(Collection<String> appActorIds) {
		this.appActorIds = appActorIds;
	}

	public void setCreateTimeGreaterThanOrEqual(
			Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}

	public void setRunDay(Integer runDay) {
		this.runDay = runDay;
	}

	public void setRunDayGreaterThanOrEqual(Integer runDayGreaterThanOrEqual) {
		this.runDayGreaterThanOrEqual = runDayGreaterThanOrEqual;
	}

	public void setRunDayLessThanOrEqual(Integer runDayLessThanOrEqual) {
		this.runDayLessThanOrEqual = runDayLessThanOrEqual;
	}

	public void setRunMonth(Integer runMonth) {
		this.runMonth = runMonth;
	}

	public void setRunQuarter(Integer runQuarter) {
		this.runQuarter = runQuarter;
	}

	public void setRunWeek(Integer runWeek) {
		this.runWeek = runWeek;
	}

	public void setRunYear(Integer runYear) {
		this.runYear = runYear;
	}

	public void setSchedulerId(String schedulerId) {
		this.schedulerId = schedulerId;
	}

	public void setStatusGreaterThanOrEqual(Integer statusGreaterThanOrEqual) {
		this.statusGreaterThanOrEqual = statusGreaterThanOrEqual;
	}

	public void setStatusLessThanOrEqual(Integer statusLessThanOrEqual) {
		this.statusLessThanOrEqual = statusLessThanOrEqual;
	}

	public SchedulerExecutionQuery statusGreaterThanOrEqual(
			Integer statusGreaterThanOrEqual) {
		if (statusGreaterThanOrEqual == null) {
			throw new RuntimeException("status is null");
		}
		this.statusGreaterThanOrEqual = statusGreaterThanOrEqual;
		return this;
	}

	public SchedulerExecutionQuery statusLessThanOrEqual(
			Integer statusLessThanOrEqual) {
		if (statusLessThanOrEqual == null) {
			throw new RuntimeException("status is null");
		}
		this.statusLessThanOrEqual = statusLessThanOrEqual;
		return this;
	}

}