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

package com.glaf.sms.query;

import java.util.*;
import com.glaf.core.query.DataQuery;

public class SmsMessageQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected String clientId;
	protected String serverId;
	protected String mobile;
	protected String mobileLike;
	protected Date sendTimeGreaterThanOrEqual;
	protected Date sendTimeLessThanOrEqual;
	protected Integer year;
	protected Integer month;
	protected Integer fullDay;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;
	protected Date sendLaterTimeGreaterThanOrEqual;

	protected Integer checkAbleSend;// 查询是否可发送的信息

	public SmsMessageQuery() {

	}

	public SmsMessageQuery clientId(String clientId) {
		if (clientId == null) {
			throw new RuntimeException("clientId is null");
		}
		this.clientId = clientId;
		return this;
	}

	public SmsMessageQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public SmsMessageQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public SmsMessageQuery fullDay(Integer fullDay) {
		if (fullDay == null) {
			throw new RuntimeException("fullDay is null");
		}
		this.fullDay = fullDay;
		return this;
	}

	public Integer getCheckAbleSend() {
		return checkAbleSend;
	}

	public String getClientId() {
		return clientId;
	}

	public Date getCreateTimeGreaterThanOrEqual() {
		return createTimeGreaterThanOrEqual;
	}

	public Date getCreateTimeLessThanOrEqual() {
		return createTimeLessThanOrEqual;
	}

	public Integer getFullDay() {
		return fullDay;
	}

	public String getMobile() {
		return mobile;
	}

	public String getMobileLike() {
		if (mobileLike != null && mobileLike.trim().length() > 0) {
			if (!mobileLike.startsWith("%")) {
				mobileLike = "%" + mobileLike;
			}
			if (!mobileLike.endsWith("%")) {
				mobileLike = mobileLike + "%";
			}
		}
		return mobileLike;
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

			if ("clientId".equals(sortColumn)) {
				orderBy = "E.CLIENTID_" + a_x;
			}

			if ("serverId".equals(sortColumn)) {
				orderBy = "E.SERVERID_" + a_x;
			}

			if ("name".equals(sortColumn)) {
				orderBy = "E.NAME_" + a_x;
			}

			if ("mobile".equals(sortColumn)) {
				orderBy = "E.MOBILE_" + a_x;
			}

			if ("subject".equals(sortColumn)) {
				orderBy = "E.SUBJECT_" + a_x;
			}

			if ("message".equals(sortColumn)) {
				orderBy = "E.MESSAGE_" + a_x;
			}

			if ("sendTime".equals(sortColumn)) {
				orderBy = "E.SENDTIME_" + a_x;
			}

			if ("status".equals(sortColumn)) {
				orderBy = "E.STATUS_" + a_x;
			}

			if ("year".equals(sortColumn)) {
				orderBy = "E.YEAR_" + a_x;
			}

			if ("month".equals(sortColumn)) {
				orderBy = "E.MONTH_" + a_x;
			}

			if ("fullDay".equals(sortColumn)) {
				orderBy = "E.FULLDAY_" + a_x;
			}

			if ("createTime".equals(sortColumn)) {
				orderBy = "E.CREATETIME_" + a_x;
			}

		}
		return orderBy;
	}

	public Date getSendLaterTimeGreaterThanOrEqual() {
		return sendLaterTimeGreaterThanOrEqual;
	}

	public Date getSendTimeGreaterThanOrEqual() {
		return sendTimeGreaterThanOrEqual;
	}

	public Date getSendTimeLessThanOrEqual() {
		return sendTimeLessThanOrEqual;
	}

	public String getServerId() {
		return serverId;
	}

	public Integer getYear() {
		return year;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("clientId", "CLIENTID_");
		addColumn("serverId", "SERVERID_");
		addColumn("name", "NAME_");
		addColumn("mobile", "MOBILE_");
		addColumn("subject", "SUBJECT_");
		addColumn("message", "MESSAGE_");
		addColumn("sendTime", "SENDTIME_");
		addColumn("status", "STATUS_");
		addColumn("year", "YEAR_");
		addColumn("month", "MONTH_");
		addColumn("fullDay", "FULLDAY_");
		addColumn("createTime", "CREATETIME_");
	}

	public SmsMessageQuery mobile(String mobile) {
		if (mobile == null) {
			throw new RuntimeException("mobile is null");
		}
		this.mobile = mobile;
		return this;
	}

	public SmsMessageQuery mobileLike(String mobileLike) {
		if (mobileLike == null) {
			throw new RuntimeException("mobile is null");
		}
		this.mobileLike = mobileLike;
		return this;
	}

	public SmsMessageQuery month(Integer month) {
		if (month == null) {
			throw new RuntimeException("month is null");
		}
		this.month = month;
		return this;
	}

	public SmsMessageQuery sendTimeGreaterThanOrEqual(Date sendTimeGreaterThanOrEqual) {
		if (sendTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("sendTime is null");
		}
		this.sendTimeGreaterThanOrEqual = sendTimeGreaterThanOrEqual;
		return this;
	}

	public SmsMessageQuery sendTimeLessThanOrEqual(Date sendTimeLessThanOrEqual) {
		if (sendTimeLessThanOrEqual == null) {
			throw new RuntimeException("sendTime is null");
		}
		this.sendTimeLessThanOrEqual = sendTimeLessThanOrEqual;
		return this;
	}

	public SmsMessageQuery serverId(String serverId) {
		if (serverId == null) {
			throw new RuntimeException("serverId is null");
		}
		this.serverId = serverId;
		return this;
	}

	public void setCheckAbleSend(Integer checkAbleSend) {
		this.checkAbleSend = checkAbleSend;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setFullDay(Integer fullDay) {
		this.fullDay = fullDay;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setMobileLike(String mobileLike) {
		this.mobileLike = mobileLike;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public void setSendLaterTimeGreaterThanOrEqual(Date sendLaterTimeGreaterThanOrEqual) {
		this.sendLaterTimeGreaterThanOrEqual = sendLaterTimeGreaterThanOrEqual;
	}

	public void setSendTimeGreaterThanOrEqual(Date sendTimeGreaterThanOrEqual) {
		this.sendTimeGreaterThanOrEqual = sendTimeGreaterThanOrEqual;
	}

	public void setSendTimeLessThanOrEqual(Date sendTimeLessThanOrEqual) {
		this.sendTimeLessThanOrEqual = sendTimeLessThanOrEqual;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public SmsMessageQuery year(Integer year) {
		if (year == null) {
			throw new RuntimeException("year is null");
		}
		this.year = year;
		return this;
	}

}