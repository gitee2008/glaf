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

package com.glaf.base.online.domain;

import java.io.*;
import java.util.*;
import javax.persistence.*;
import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.glaf.core.base.*;
import com.glaf.base.online.util.*;

@Entity
@Table(name = "USER_ONLINE_LOG")
public class UserOnlineLog implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", nullable = false)
	protected long id;

	@Column(name = "ACTORID_", length = 50)
	protected String actorId;

	@Column(name = "NAME_", length = 50)
	protected String name;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LOGINDATE_")
	protected Date loginDate;

	@Column(name = "LOGINIP_", length = 100)
	protected String loginIP;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LOGOUTDATE_")
	protected Date logoutDate;

	@Column(name = "YEAR_")
	protected int year;

	@Column(name = "MONTH_")
	protected int month;

	@Column(name = "QUARTER_")
	protected int quarter;

	@Column(name = "DAY_")
	protected int day;

	public UserOnlineLog() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserOnlineLog other = (UserOnlineLog) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public String getActorId() {
		return this.actorId;
	}

	public int getDay() {
		return day;
	}

	public long getId() {
		return this.id;
	}

	public Date getLoginDate() {
		return this.loginDate;
	}

	public String getLoginIP() {
		return this.loginIP;
	}

	public Date getLogoutDate() {
		return logoutDate;
	}

	public int getMonth() {
		return month;
	}

	public String getName() {
		return this.name;
	}

	public int getQuarter() {
		return quarter;
	}

	public int getYear() {
		return year;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public UserOnlineLog jsonToObject(JSONObject jsonObject) {
		return UserOnlineLogJsonFactory.jsonToObject(jsonObject);
	}

	public void setActorId(String actorId) {
		this.actorId = actorId;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public void setLoginIP(String loginIP) {
		this.loginIP = loginIP;
	}

	public void setLogoutDate(Date logoutDate) {
		this.logoutDate = logoutDate;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setQuarter(int quarter) {
		this.quarter = quarter;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public JSONObject toJsonObject() {
		return UserOnlineLogJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return UserOnlineLogJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
