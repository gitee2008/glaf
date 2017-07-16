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

package com.glaf.core.access.domain;

import java.io.*;
import java.util.*;
import javax.persistence.*;
import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.glaf.core.base.*;
import com.glaf.core.util.DateUtils;
import com.glaf.core.access.util.*;

/**
 * 
 * 实体对象
 *
 */

@Entity
@Table(name = "SYS_ACCESS_LOG")
public class AccessLog implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", nullable = false)
	protected long id;

	/**
	 * IP地址
	 */
	@Column(name = "IP_", length = 250)
	protected String ip;

	/**
	 * 请求方法
	 */
	@Column(name = "METHOD_", length = 50)
	protected String method;

	/**
	 * 请求内容
	 */
	@Lob
	@Column(name = "CONTENT_", length = 4000)
	protected String content;

	/**
	 * 地址
	 */
	@Column(name = "URI_", length = 500)
	protected String uri;

	/**
	 * 地址引用编号
	 */
	@Column(name = "URIREFID_")
	protected long uriRefId;

	/**
	 * 状态
	 */
	@Column(name = "STATUS_")
	protected int status;

	/**
	 * 天
	 */
	@Column(name = "DAY_")
	protected int day;

	/**
	 * 小时
	 */
	@Column(name = "HOUR_")
	protected int hour;

	/**
	 * 分钟
	 */
	@Column(name = "MINUTE_")
	protected int minute;

	/**
	 * 耗时
	 */
	@Column(name = "TIMEMILLIS_")
	protected int timeMillis;

	/**
	 * 类型
	 */
	@Column(name = "TYPE_", length = 50)
	protected String type;

	/**
	 * 用户编号
	 */
	@Column(name = "USERID_", length = 50)
	protected String userId;

	/**
	 * 访问时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ACCESSTIME_")
	protected Date accessTime;

	public AccessLog() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccessLog other = (AccessLog) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public Date getAccessTime() {
		return accessTime;
	}

	public String getAccessTimeString() {
		if (this.accessTime != null) {
			return DateUtils.getDateTime(this.accessTime);
		}
		return "";
	}

	public String getContent() {
		return content;
	}

	public int getDay() {
		return day;
	}

	public int getHour() {
		return hour;
	}

	public long getId() {
		return id;
	}

	public String getIp() {
		return ip;
	}

	public String getMethod() {
		return method;
	}

	public int getMinute() {
		return minute;
	}

	public int getStatus() {
		return status;
	}

	public int getTimeMillis() {
		return timeMillis;
	}

	public String getType() {
		return type;
	}

	public String getUri() {
		return uri;
	}

	public long getUriRefId() {
		return uriRefId;
	}

	public String getUserId() {
		return userId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public AccessLog jsonToObject(JSONObject jsonObject) {
		return AccessLogJsonFactory.jsonToObject(jsonObject);
	}

	public void setAccessTime(Date accessTime) {
		this.accessTime = accessTime;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setTimeMillis(int timeMillis) {
		this.timeMillis = timeMillis;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public void setUriRefId(long uriRefId) {
		this.uriRefId = uriRefId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public JSONObject toJsonObject() {
		return AccessLogJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return AccessLogJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
