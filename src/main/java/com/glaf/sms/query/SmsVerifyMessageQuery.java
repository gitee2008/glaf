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

public class SmsVerifyMessageQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected String mobile;
	protected String type;
	protected Date sendTimeGreaterThanOrEqual;
	protected Date sendTimeLessThanOrEqual;
	protected Long sendTimeMs;
	protected Long sendTimeMsGreaterThanOrEqual;
	protected Long sendTimeMsLessThanOrEqual;

	public SmsVerifyMessageQuery() {

	}

	public String getMobile() {
		return mobile;
	}

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("name".equals(sortColumn)) {
				orderBy = "E.NAME_" + a_x;
			}

			if ("mobile".equals(sortColumn)) {
				orderBy = "E.MOBILE_" + a_x;
			}

			if ("type".equals(sortColumn)) {
				orderBy = "E.TYPE_" + a_x;
			}

			if ("verificationCode".equals(sortColumn)) {
				orderBy = "E.VERIFICATIONCODE_" + a_x;
			}

			if ("status".equals(sortColumn)) {
				orderBy = "E.STATUS_" + a_x;
			}

			if ("sendTime".equals(sortColumn)) {
				orderBy = "E.SENDTIME_" + a_x;
			}

			if ("sendTimeMs".equals(sortColumn)) {
				orderBy = "E.SENDTIMEMS_" + a_x;
			}

			if ("createTime".equals(sortColumn)) {
				orderBy = "E.CREATETIME_" + a_x;
			}

		}
		return orderBy;
	}

	public Date getSendTimeGreaterThanOrEqual() {
		return sendTimeGreaterThanOrEqual;
	}

	public Date getSendTimeLessThanOrEqual() {
		return sendTimeLessThanOrEqual;
	}

	public Long getSendTimeMs() {
		return sendTimeMs;
	}

	public Long getSendTimeMsGreaterThanOrEqual() {
		return sendTimeMsGreaterThanOrEqual;
	}

	public Long getSendTimeMsLessThanOrEqual() {
		return sendTimeMsLessThanOrEqual;
	}

	public String getType() {
		return type;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("name", "NAME_");
		addColumn("mobile", "MOBILE_");
		addColumn("type", "TYPE_");
		addColumn("verificationCode", "VERIFICATIONCODE_");
		addColumn("status", "STATUS_");
		addColumn("sendTime", "SENDTIME_");
		addColumn("sendTimeMs", "SENDTIMEMS_");
		addColumn("createTime", "CREATETIME_");
	}

	public SmsVerifyMessageQuery mobile(String mobile) {
		if (mobile == null) {
			throw new RuntimeException("mobile is null");
		}
		this.mobile = mobile;
		return this;
	}

	public SmsVerifyMessageQuery sendTimeGreaterThanOrEqual(Date sendTimeGreaterThanOrEqual) {
		if (sendTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("sendTime is null");
		}
		this.sendTimeGreaterThanOrEqual = sendTimeGreaterThanOrEqual;
		return this;
	}

	public SmsVerifyMessageQuery sendTimeLessThanOrEqual(Date sendTimeLessThanOrEqual) {
		if (sendTimeLessThanOrEqual == null) {
			throw new RuntimeException("sendTime is null");
		}
		this.sendTimeLessThanOrEqual = sendTimeLessThanOrEqual;
		return this;
	}

	public SmsVerifyMessageQuery sendTimeMs(Long sendTimeMs) {
		if (sendTimeMs == null) {
			throw new RuntimeException("sendTimeMs is null");
		}
		this.sendTimeMs = sendTimeMs;
		return this;
	}

	public SmsVerifyMessageQuery sendTimeMsGreaterThanOrEqual(Long sendTimeMsGreaterThanOrEqual) {
		if (sendTimeMsGreaterThanOrEqual == null) {
			throw new RuntimeException("sendTimeMs is null");
		}
		this.sendTimeMsGreaterThanOrEqual = sendTimeMsGreaterThanOrEqual;
		return this;
	}

	public SmsVerifyMessageQuery sendTimeMsLessThanOrEqual(Long sendTimeMsLessThanOrEqual) {
		if (sendTimeMsLessThanOrEqual == null) {
			throw new RuntimeException("sendTimeMs is null");
		}
		this.sendTimeMsLessThanOrEqual = sendTimeMsLessThanOrEqual;
		return this;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setSendTimeGreaterThanOrEqual(Date sendTimeGreaterThanOrEqual) {
		this.sendTimeGreaterThanOrEqual = sendTimeGreaterThanOrEqual;
	}

	public void setSendTimeLessThanOrEqual(Date sendTimeLessThanOrEqual) {
		this.sendTimeLessThanOrEqual = sendTimeLessThanOrEqual;
	}

	public void setSendTimeMs(Long sendTimeMs) {
		this.sendTimeMs = sendTimeMs;
	}

	public void setSendTimeMsGreaterThanOrEqual(Long sendTimeMsGreaterThanOrEqual) {
		this.sendTimeMsGreaterThanOrEqual = sendTimeMsGreaterThanOrEqual;
	}

	public void setSendTimeMsLessThanOrEqual(Long sendTimeMsLessThanOrEqual) {
		this.sendTimeMsLessThanOrEqual = sendTimeMsLessThanOrEqual;
	}

	public void setType(String type) {
		this.type = type;
	}

	public SmsVerifyMessageQuery type(String type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		this.type = type;
		return this;
	}

}