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
@Table(name = "USER_ONLINE")
public class UserOnline implements Serializable, JSONable {
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
	@Column(name = "CHECKDATE_")
	protected Date checkDate;

	@Column(name = "CHECKDATEMS_")
	protected long checkDateMs;

	public UserOnline() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserOnline other = (UserOnline) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public String getActorId() {
		return this.actorId;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public long getCheckDateMs() {
		return checkDateMs;
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

	public String getName() {
		return this.name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public UserOnline jsonToObject(JSONObject jsonObject) {
		return UserOnlineJsonFactory.jsonToObject(jsonObject);
	}

	public void setActorId(String actorId) {
		this.actorId = actorId;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public void setCheckDateMs(long checkDateMs) {
		this.checkDateMs = checkDateMs;
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

	public void setName(String name) {
		this.name = name;
	}

	public JSONObject toJsonObject() {
		return UserOnlineJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return UserOnlineJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
