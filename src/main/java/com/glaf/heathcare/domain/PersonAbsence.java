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
import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.glaf.core.base.*;
import com.glaf.core.util.DateUtils;
import com.glaf.heathcare.util.*;

/**
 * 考勤情况
 *
 */

public class PersonAbsence implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	protected String id;

	/**
	 * 租户编号
	 */
	protected String tenantId;

	/**
	 * 班级编号
	 */
	protected String gradeId;

	/**
	 * 学生编号
	 */
	protected String personId;

	/**
	 * 姓名
	 */
	protected String name;

	/**
	 * 年
	 */
	protected int year;

	/**
	 * 月
	 */
	protected int month;

	/**
	 * 年月日
	 */
	protected int day;

	/**
	 * 考勤阶段（上午am、下午pm、全天day）
	 */
	protected String section;

	/**
	 * 考勤情况（0-正常出席、1-迟到、2-请病假、3-请事假）
	 */
	protected int status;

	/**
	 * 备注
	 */
	protected String remark;

	/**
	 * 创建人
	 */
	protected String createBy;

	/**
	 * 创建日期
	 */
	protected Date createTime;

	protected String tableSuffix;

	public PersonAbsence() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonAbsence other = (PersonAbsence) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
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

	public int getDay() {
		return this.day;
	}

	public String getGradeId() {
		return this.gradeId;
	}

	public String getId() {
		return this.id;
	}

	public int getMonth() {
		return month;
	}

	public String getName() {
		return this.name;
	}

	public String getPersonId() {
		return this.personId;
	}

	public String getRemark() {
		return this.remark;
	}

	public String getSection() {
		return this.section;
	}

	public int getStatus() {
		return this.status;
	}

	public String getTableSuffix() {
		return tableSuffix;
	}

	public String getTenantId() {
		return this.tenantId;
	}

	public int getYear() {
		return year;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public PersonAbsence jsonToObject(JSONObject jsonObject) {
		return PersonAbsenceJsonFactory.jsonToObject(jsonObject);
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setTableSuffix(String tableSuffix) {
		this.tableSuffix = tableSuffix;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public JSONObject toJsonObject() {
		return PersonAbsenceJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return PersonAbsenceJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
