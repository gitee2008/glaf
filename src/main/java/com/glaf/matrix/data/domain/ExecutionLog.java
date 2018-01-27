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

package com.glaf.matrix.data.domain;

import java.io.*;
import java.util.*;
import javax.persistence.*;
import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.glaf.core.base.*;
import com.glaf.core.util.DateUtils;
import com.glaf.matrix.data.util.*;

/**
 * 
 * 实体对象
 *
 */

@Entity
@Table(name = "SYS_EXECUTION_LOG")
public class ExecutionLog implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", nullable = false)
	protected long id;

	/**
	 * 类型
	 */
	@Column(name = "TYPE_", length = 50)
	protected String type;

	/**
	 * 业务主键
	 */
	@Column(name = "BUSINESSKEY_", length = 200)
	protected String businessKey;

	/**
	 * 作业编号
	 */
	@Column(name = "JOBNO_", length = 250)
	protected String jobNo;

	/**
	 * 主题
	 */
	@Column(name = "TITLE_", length = 200)
	protected String title;

	/**
	 * 内容
	 */
	@Lob
	@Column(name = "CONTENT_", length = 2000)
	protected String content;

	/**
	 * 开始时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "STARTTIME_")
	protected Date startTime;

	/**
	 * 结束时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENDTIME_")
	protected Date endTime;

	/**
	 * 执行年月日
	 */
	@Column(name = "RUNDAY_")
	protected int runDay;

	/**
	 * 执行小时
	 */
	@Column(name = "RUNHOUR_")
	protected int runHour;

	/**
	 * 运行时间
	 */
	@Column(name = "RUNTIME_")
	protected long runTime;

	/**
	 * 状态
	 */
	@Column(name = "STATUS_")
	protected int status;

	/**
	 * 退出代码
	 */
	@Column(name = "EXITCODE_", length = 200)
	protected String exitCode;

	/**
	 * 退出信息
	 */
	@Lob
	@Column(name = "EXITMESSAGE_", length = 4000)
	protected String exitMessage;

	/**
	 * 创建人
	 */
	@Column(name = "CREATEBY_", length = 50)
	protected String createBy;

	/**
	 * 创建时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME_")
	protected Date createTime;

	public ExecutionLog() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExecutionLog other = (ExecutionLog) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public String getBusinessKey() {
		return this.businessKey;
	}

	public String getContent() {
		return this.content;
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

	public Date getEndTime() {
		return this.endTime;
	}

	public String getEndTimeString() {
		if (this.endTime != null) {
			return DateUtils.getDateTime(this.endTime);
		}
		return "";
	}

	public String getExitCode() {
		return this.exitCode;
	}

	public String getExitMessage() {
		return this.exitMessage;
	}

	public long getId() {
		return this.id;
	}

	public String getJobNo() {
		return this.jobNo;
	}

	public int getRunDay() {
		return this.runDay;
	}

	public int getRunHour() {
		return this.runHour;
	}

	public long getRunTime() {
		return this.runTime;
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public String getStartTimeString() {
		if (this.startTime != null) {
			return DateUtils.getDateTime(this.startTime);
		}
		return "";
	}

	public int getStatus() {
		return this.status;
	}

	public String getTitle() {
		return this.title;
	}

	public String getType() {
		return this.type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public ExecutionLog jsonToObject(JSONObject jsonObject) {
		return ExecutionLogJsonFactory.jsonToObject(jsonObject);
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public void setExitCode(String exitCode) {
		this.exitCode = exitCode;
	}

	public void setExitMessage(String exitMessage) {
		this.exitMessage = exitMessage;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}

	public void setRunDay(int runDay) {
		this.runDay = runDay;
	}

	public void setRunHour(int runHour) {
		this.runHour = runHour;
	}

	public void setRunTime(long runTime) {
		this.runTime = runTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setType(String type) {
		this.type = type;
	}

	public JSONObject toJsonObject() {
		return ExecutionLogJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return ExecutionLogJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
