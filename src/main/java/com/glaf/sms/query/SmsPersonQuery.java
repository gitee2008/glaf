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

import com.glaf.core.query.DataQuery;

public class SmsPersonQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected String clientId;
	protected String mobile;
	protected String mobileLike;

	public SmsPersonQuery() {

	}

	public SmsPersonQuery clientId(String clientId) {
		if (clientId == null) {
			throw new RuntimeException("clientId is null");
		}
		this.clientId = clientId;
		return this;
	}

	public String getClientId() {
		return clientId;
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

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("clientId".equals(sortColumn)) {
				orderBy = "E.CLIENTID_" + a_x;
			}

			if ("name".equals(sortColumn)) {
				orderBy = "E.NAME_" + a_x;
			}

			if ("mobile".equals(sortColumn)) {
				orderBy = "E.MOBILE_" + a_x;
			}

			if ("limit".equals(sortColumn)) {
				orderBy = "E.LIMIT_" + a_x;
			}

			if ("locked".equals(sortColumn)) {
				orderBy = "E.LOCKED_" + a_x;
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

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("clientId", "CLIENTID_");
		addColumn("name", "NAME_");
		addColumn("mobile", "MOBILE_");
		addColumn("limit", "LIMIT_");
		addColumn("locked", "LOCKED_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
	}

	public SmsPersonQuery mobile(String mobile) {
		if (mobile == null) {
			throw new RuntimeException("mobile is null");
		}
		this.mobile = mobile;
		return this;
	}

	public SmsPersonQuery mobileLike(String mobileLike) {
		if (mobileLike == null) {
			throw new RuntimeException("mobile is null");
		}
		this.mobileLike = mobileLike;
		return this;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setMobileLike(String mobileLike) {
		this.mobileLike = mobileLike;
	}

}