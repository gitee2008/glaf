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

package com.glaf.matrix.data.query;

import java.util.*;
import com.glaf.core.query.DataQuery;

public class ExecutionLogQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected String type;
	protected List<String> types;
	protected String jobNo;
	protected String jobNoLike;
	protected String titleLike;
	protected Date startTimeGreaterThanOrEqual;
	protected Date startTimeLessThanOrEqual;
	protected Date endTimeGreaterThanOrEqual;
	protected Date endTimeLessThanOrEqual;
	protected Integer runDay;
	protected Integer runHour;
	protected String exitCode;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	public ExecutionLogQuery() {

	}

	public ExecutionLogQuery businessKey(String businessKey) {
		if (businessKey == null) {
			throw new RuntimeException("businessKey is null");
		}
		this.businessKey = businessKey;
		return this;
	}

	public ExecutionLogQuery businessKeys(List<String> businessKeys) {
		if (businessKeys == null) {
			throw new RuntimeException("businessKeys is empty ");
		}
		this.businessKeys = businessKeys;
		return this;
	}

	public ExecutionLogQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public ExecutionLogQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public ExecutionLogQuery endTimeGreaterThanOrEqual(Date endTimeGreaterThanOrEqual) {
		if (endTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("endTime is null");
		}
		this.endTimeGreaterThanOrEqual = endTimeGreaterThanOrEqual;
		return this;
	}

	public ExecutionLogQuery endTimeLessThanOrEqual(Date endTimeLessThanOrEqual) {
		if (endTimeLessThanOrEqual == null) {
			throw new RuntimeException("endTime is null");
		}
		this.endTimeLessThanOrEqual = endTimeLessThanOrEqual;
		return this;
	}

	public ExecutionLogQuery exitCode(String exitCode) {
		if (exitCode == null) {
			throw new RuntimeException("exitCode is null");
		}
		this.exitCode = exitCode;
		return this;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public List<String> getBusinessKeys() {
		return businessKeys;
	}

	public Date getCreateTimeGreaterThanOrEqual() {
		return createTimeGreaterThanOrEqual;
	}

	public Date getCreateTimeLessThanOrEqual() {
		return createTimeLessThanOrEqual;
	}

	public Date getEndTimeGreaterThanOrEqual() {
		return endTimeGreaterThanOrEqual;
	}

	public Date getEndTimeLessThanOrEqual() {
		return endTimeLessThanOrEqual;
	}

	public String getExitCode() {
		return exitCode;
	}

	public String getJobNo() {
		return jobNo;
	}

	public String getJobNoLike() {
		if (jobNoLike != null && jobNoLike.trim().length() > 0) {
			if (!jobNoLike.startsWith("%")) {
				jobNoLike = "%" + jobNoLike;
			}
			if (!jobNoLike.endsWith("%")) {
				jobNoLike = jobNoLike + "%";
			}
		}
		return jobNoLike;
	}

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("type".equals(sortColumn)) {
				orderBy = "E.TYPE_" + a_x;
			}

			if ("businessKey".equals(sortColumn)) {
				orderBy = "E.BUSINESSKEY_" + a_x;
			}

			if ("jobNo".equals(sortColumn)) {
				orderBy = "E.JOBNO_" + a_x;
			}

			if ("title".equals(sortColumn)) {
				orderBy = "E.TITLE_" + a_x;
			}

			if ("content".equals(sortColumn)) {
				orderBy = "E.CONTENT_" + a_x;
			}

			if ("startTime".equals(sortColumn)) {
				orderBy = "E.STARTTIME_" + a_x;
			}

			if ("endTime".equals(sortColumn)) {
				orderBy = "E.ENDTIME_" + a_x;
			}

			if ("runDay".equals(sortColumn)) {
				orderBy = "E.RUNDAY_" + a_x;
			}

			if ("runHour".equals(sortColumn)) {
				orderBy = "E.RUNHOUR_" + a_x;
			}

			if ("runTime".equals(sortColumn)) {
				orderBy = "E.RUNTIME_" + a_x;
			}

			if ("status".equals(sortColumn)) {
				orderBy = "E.STATUS_" + a_x;
			}

			if ("exitCode".equals(sortColumn)) {
				orderBy = "E.EXITCODE_" + a_x;
			}

			if ("exitMessage".equals(sortColumn)) {
				orderBy = "E.EXITMESSAGE_" + a_x;
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

	public Integer getRunHour() {
		return runHour;
	}

	public Date getStartTimeGreaterThanOrEqual() {
		return startTimeGreaterThanOrEqual;
	}

	public Date getStartTimeLessThanOrEqual() {
		return startTimeLessThanOrEqual;
	}

	public Integer getStatusGreaterThanOrEqual() {
		return statusGreaterThanOrEqual;
	}

	public Integer getStatusLessThanOrEqual() {
		return statusLessThanOrEqual;
	}

	public String getTitleLike() {
		if (titleLike != null && titleLike.trim().length() > 0) {
			if (!titleLike.startsWith("%")) {
				titleLike = "%" + titleLike;
			}
			if (!titleLike.endsWith("%")) {
				titleLike = titleLike + "%";
			}
		}
		return titleLike;
	}

	public String getType() {
		return type;
	}

	public List<String> getTypes() {
		return types;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("type", "TYPE_");
		addColumn("businessKey", "BUSINESSKEY_");
		addColumn("jobNo", "JOBNO_");
		addColumn("title", "TITLE_");
		addColumn("content", "CONTENT_");
		addColumn("startTime", "STARTTIME_");
		addColumn("endTime", "ENDTIME_");
		addColumn("runDay", "RUNDAY_");
		addColumn("runHour", "RUNHOUR_");
		addColumn("runTime", "RUNTIME_");
		addColumn("status", "STATUS_");
		addColumn("exitCode", "EXITCODE_");
		addColumn("exitMessage", "EXITMESSAGE_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
	}

	public ExecutionLogQuery jobNo(String jobNo) {
		if (jobNo == null) {
			throw new RuntimeException("jobNo is null");
		}
		this.jobNo = jobNo;
		return this;
	}

	public ExecutionLogQuery jobNoLike(String jobNoLike) {
		if (jobNoLike == null) {
			throw new RuntimeException("jobNo is null");
		}
		this.jobNoLike = jobNoLike;
		return this;
	}

	public ExecutionLogQuery runDay(Integer runDay) {
		if (runDay == null) {
			throw new RuntimeException("runDay is null");
		}
		this.runDay = runDay;
		return this;
	}

	public ExecutionLogQuery runHour(Integer runHour) {
		if (runHour == null) {
			throw new RuntimeException("runHour is null");
		}
		this.runHour = runHour;
		return this;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public void setBusinessKeys(List<String> businessKeys) {
		this.businessKeys = businessKeys;
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setEndTimeGreaterThanOrEqual(Date endTimeGreaterThanOrEqual) {
		this.endTimeGreaterThanOrEqual = endTimeGreaterThanOrEqual;
	}

	public void setEndTimeLessThanOrEqual(Date endTimeLessThanOrEqual) {
		this.endTimeLessThanOrEqual = endTimeLessThanOrEqual;
	}

	public void setExitCode(String exitCode) {
		this.exitCode = exitCode;
	}

	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}

	public void setJobNoLike(String jobNoLike) {
		this.jobNoLike = jobNoLike;
	}

	public void setRunDay(Integer runDay) {
		this.runDay = runDay;
	}

	public void setRunHour(Integer runHour) {
		this.runHour = runHour;
	}

	public void setStartTimeGreaterThanOrEqual(Date startTimeGreaterThanOrEqual) {
		this.startTimeGreaterThanOrEqual = startTimeGreaterThanOrEqual;
	}

	public void setStartTimeLessThanOrEqual(Date startTimeLessThanOrEqual) {
		this.startTimeLessThanOrEqual = startTimeLessThanOrEqual;
	}

	public void setStatusGreaterThanOrEqual(Integer statusGreaterThanOrEqual) {
		this.statusGreaterThanOrEqual = statusGreaterThanOrEqual;
	}

	public void setStatusLessThanOrEqual(Integer statusLessThanOrEqual) {
		this.statusLessThanOrEqual = statusLessThanOrEqual;
	}

	public void setTitleLike(String titleLike) {
		this.titleLike = titleLike;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}

	public ExecutionLogQuery startTimeGreaterThanOrEqual(Date startTimeGreaterThanOrEqual) {
		if (startTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("startTime is null");
		}
		this.startTimeGreaterThanOrEqual = startTimeGreaterThanOrEqual;
		return this;
	}

	public ExecutionLogQuery startTimeLessThanOrEqual(Date startTimeLessThanOrEqual) {
		if (startTimeLessThanOrEqual == null) {
			throw new RuntimeException("startTime is null");
		}
		this.startTimeLessThanOrEqual = startTimeLessThanOrEqual;
		return this;
	}

	public ExecutionLogQuery statusGreaterThanOrEqual(Integer statusGreaterThanOrEqual) {
		if (statusGreaterThanOrEqual == null) {
			throw new RuntimeException("status is null");
		}
		this.statusGreaterThanOrEqual = statusGreaterThanOrEqual;
		return this;
	}

	public ExecutionLogQuery statusLessThanOrEqual(Integer statusLessThanOrEqual) {
		if (statusLessThanOrEqual == null) {
			throw new RuntimeException("status is null");
		}
		this.statusLessThanOrEqual = statusLessThanOrEqual;
		return this;
	}

	public ExecutionLogQuery titleLike(String titleLike) {
		if (titleLike == null) {
			throw new RuntimeException("title is null");
		}
		this.titleLike = titleLike;
		return this;
	}

	public ExecutionLogQuery type(String type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		this.type = type;
		return this;
	}

	public ExecutionLogQuery types(List<String> types) {
		if (types == null) {
			throw new RuntimeException("types is empty ");
		}
		this.types = types;
		return this;
	}

}