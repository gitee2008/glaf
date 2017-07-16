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

package com.glaf.core.access.query;

import java.util.*;
import com.glaf.core.query.DataQuery;

public class AccessLogQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected String ip;
	protected String uri;
	protected String uriLike;
	protected Long uriRefId;
	protected Integer day;
	protected Integer hour;
	protected Integer hourGreaterThanOrEqual;
	protected Integer minute;
	protected String type;
	protected String userId;
	protected Date accessTimeGreaterThanOrEqual;
	protected Date accessTimeLessThanOrEqual;

	public AccessLogQuery() {

	}

	public AccessLogQuery accessTimeGreaterThanOrEqual(Date accessTimeGreaterThanOrEqual) {
		if (accessTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("accessTime is null");
		}
		this.accessTimeGreaterThanOrEqual = accessTimeGreaterThanOrEqual;
		return this;
	}

	public AccessLogQuery accessTimeLessThanOrEqual(Date accessTimeLessThanOrEqual) {
		if (accessTimeLessThanOrEqual == null) {
			throw new RuntimeException("accessTime is null");
		}
		this.accessTimeLessThanOrEqual = accessTimeLessThanOrEqual;
		return this;
	}

	public AccessLogQuery day(Integer day) {
		if (day == null) {
			throw new RuntimeException("day is null");
		}
		this.day = day;
		return this;
	}

	public Date getAccessTimeGreaterThanOrEqual() {
		return accessTimeGreaterThanOrEqual;
	}

	public Date getAccessTimeLessThanOrEqual() {
		return accessTimeLessThanOrEqual;
	}

	public Integer getDay() {
		return day;
	}

	public Integer getHour() {
		return hour;
	}

	public Integer getHourGreaterThanOrEqual() {
		return hourGreaterThanOrEqual;
	}

	public String getIp() {
		return ip;
	}

	public Integer getMinute() {
		return minute;
	}

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("ip".equals(sortColumn)) {
				orderBy = "E.IP_" + a_x;
			}

			if ("method".equals(sortColumn)) {
				orderBy = "E.METHOD_" + a_x;
			}

			if ("uri".equals(sortColumn)) {
				orderBy = "E.URI_" + a_x;
			}

			if ("uriRefId".equals(sortColumn)) {
				orderBy = "E.URIREFID_" + a_x;
			}

			if ("status".equals(sortColumn)) {
				orderBy = "E.STATUS_" + a_x;
			}

			if ("hour".equals(sortColumn)) {
				orderBy = "E.HOUR_" + a_x;
			}

			if ("minute".equals(sortColumn)) {
				orderBy = "E.MINUTE_" + a_x;
			}

			if ("timeMillis".equals(sortColumn)) {
				orderBy = "E.TIMEMILLIS_" + a_x;
			}

			if ("type".equals(sortColumn)) {
				orderBy = "E.TYPE_" + a_x;
			}

			if ("accessTime".equals(sortColumn)) {
				orderBy = "E.ACCESSTIME_" + a_x;
			}

		}
		return orderBy;
	}

	public Integer getStatus() {
		return status;
	}

	public Integer getStatusGreaterThanOrEqual() {
		return statusGreaterThanOrEqual;
	}

	public Integer getStatusLessThanOrEqual() {
		return statusLessThanOrEqual;
	}

	public String getType() {
		return type;
	}

	public String getUri() {
		return uri;
	}

	public String getUriLike() {
		if (uriLike != null && uriLike.trim().length() > 0) {
			if (!uriLike.startsWith("%")) {
				uriLike = "%" + uriLike;
			}
			if (!uriLike.endsWith("%")) {
				uriLike = uriLike + "%";
			}
		}
		return uriLike;
	}

	public Long getUriRefId() {
		return uriRefId;
	}

	public String getUserId() {
		return userId;
	}

	public AccessLogQuery hour(Integer hour) {
		if (hour == null) {
			throw new RuntimeException("hour is null");
		}
		this.hour = hour;
		return this;
	}

	public AccessLogQuery hourGreaterThanOrEqual(Integer hourGreaterThanOrEqual) {
		if (hourGreaterThanOrEqual == null) {
			throw new RuntimeException("hour is null");
		}
		this.hourGreaterThanOrEqual = hourGreaterThanOrEqual;
		return this;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("ip", "IP_");
		addColumn("method", "METHOD_");
		addColumn("uri", "URI_");
		addColumn("uriRefId", "URIREFID_");
		addColumn("status", "STATUS_");
		addColumn("hour", "HOUR_");
		addColumn("minute", "MINUTE_");
		addColumn("timeMillis", "TIMEMILLIS_");
		addColumn("type", "TYPE_");
		addColumn("accessTime", "ACCESSTIME_");
	}

	public AccessLogQuery ip(String ip) {
		if (ip == null) {
			throw new RuntimeException("ip is null");
		}
		this.ip = ip;
		return this;
	}

	public AccessLogQuery minute(Integer minute) {
		if (minute == null) {
			throw new RuntimeException("minute is null");
		}
		this.minute = minute;
		return this;
	}

	public void setAccessTimeGreaterThanOrEqual(Date accessTimeGreaterThanOrEqual) {
		this.accessTimeGreaterThanOrEqual = accessTimeGreaterThanOrEqual;
	}

	public void setAccessTimeLessThanOrEqual(Date accessTimeLessThanOrEqual) {
		this.accessTimeLessThanOrEqual = accessTimeLessThanOrEqual;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public void setHour(Integer hour) {
		this.hour = hour;
	}

	public void setHourGreaterThanOrEqual(Integer hourGreaterThanOrEqual) {
		this.hourGreaterThanOrEqual = hourGreaterThanOrEqual;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setMinute(Integer minute) {
		this.minute = minute;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public void setUriLike(String uriLike) {
		this.uriLike = uriLike;
	}

	public void setUriRefId(Long uriRefId) {
		this.uriRefId = uriRefId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public AccessLogQuery type(String type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		this.type = type;
		return this;
	}

	public AccessLogQuery uri(String uri) {
		if (uri == null) {
			throw new RuntimeException("uri is null");
		}
		this.uri = uri;
		return this;
	}

	public AccessLogQuery uriLike(String uriLike) {
		if (uriLike == null) {
			throw new RuntimeException("uri is null");
		}
		this.uriLike = uriLike;
		return this;
	}

	public AccessLogQuery uriRefId(Long uriRefId) {
		if (uriRefId == null) {
			throw new RuntimeException("uriRefId is null");
		}
		this.uriRefId = uriRefId;
		return this;
	}

	public AccessLogQuery userId(String userId) {
		if (userId == null) {
			throw new RuntimeException("userId is null");
		}
		this.userId = userId;
		return this;
	}

}