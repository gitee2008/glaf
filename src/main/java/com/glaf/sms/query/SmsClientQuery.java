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

public class SmsClientQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected String type;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	public SmsClientQuery() {

	}

	public SmsClientQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public SmsClientQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public Date getCreateTimeGreaterThanOrEqual() {
		return createTimeGreaterThanOrEqual;
	}

	public Date getCreateTimeLessThanOrEqual() {
		return createTimeLessThanOrEqual;
	}

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("subject".equals(sortColumn)) {
				orderBy = "E.SUBJECT_" + a_x;
			}

			if ("remoteIP".equals(sortColumn)) {
				orderBy = "E.REMOTEIP_" + a_x;
			}

			if ("sysCode".equals(sortColumn)) {
				orderBy = "E.SYSCODE_" + a_x;
			}

			if ("sysPwd".equals(sortColumn)) {
				orderBy = "E.SYSPWD_" + a_x;
			}

			if ("publicKey".equals(sortColumn)) {
				orderBy = "E.PUBLICKEY_" + a_x;
			}

			if ("privateKey".equals(sortColumn)) {
				orderBy = "E.PRIVATEKEY_" + a_x;
			}

			if ("peerPublicKey".equals(sortColumn)) {
				orderBy = "E.PEERPUBLICKEY_" + a_x;
			}

			if ("token".equals(sortColumn)) {
				orderBy = "E.TOKEN_" + a_x;
			}

			if ("type".equals(sortColumn)) {
				orderBy = "E.TYPE_" + a_x;
			}

			if ("frequence".equals(sortColumn)) {
				orderBy = "E.FREQUENCE_" + a_x;
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

	public String getType() {
		return type;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("subject", "SUBJECT_");
		addColumn("remoteIP", "REMOTEIP_");
		addColumn("sysCode", "SYSCODE_");
		addColumn("sysPwd", "SYSPWD_");
		addColumn("publicKey", "PUBLICKEY_");
		addColumn("privateKey", "PRIVATEKEY_");
		addColumn("peerPublicKey", "PEERPUBLICKEY_");
		addColumn("token", "TOKEN_");
		addColumn("type", "TYPE_");
		addColumn("frequence", "FREQUENCE_");
		addColumn("limit", "LIMIT_");
		addColumn("locked", "LOCKED_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setType(String type) {
		this.type = type;
	}

	public SmsClientQuery type(String type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		this.type = type;
		return this;
	}

}