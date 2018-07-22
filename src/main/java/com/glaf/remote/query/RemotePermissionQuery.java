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

package com.glaf.remote.query;

import java.util.*;
import com.glaf.core.query.DataQuery;

public class RemotePermissionQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected String remoteIP;
	protected String remoteIPLike;
	protected String type;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	public RemotePermissionQuery() {

	}

	public RemotePermissionQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public RemotePermissionQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
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

			if ("remoteIP".equals(sortColumn)) {
				orderBy = "E.REMOTEIP_" + a_x;
			}

			if ("type".equals(sortColumn)) {
				orderBy = "E.TYPE_" + a_x;
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

	public String getRemoteIP() {
		return remoteIP;
	}

	public String getRemoteIPLike() {
		if (remoteIPLike != null && remoteIPLike.trim().length() > 0) {
			if (!remoteIPLike.startsWith("%")) {
				remoteIPLike = "%" + remoteIPLike;
			}
			if (!remoteIPLike.endsWith("%")) {
				remoteIPLike = remoteIPLike + "%";
			}
		}
		return remoteIPLike;
	}

	public String getType() {
		return type;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("remoteIP", "REMOTEIP_");
		addColumn("type", "TYPE_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
	}

	public RemotePermissionQuery remoteIP(String remoteIP) {
		if (remoteIP == null) {
			throw new RuntimeException("remoteIP is null");
		}
		this.remoteIP = remoteIP;
		return this;
	}

	public RemotePermissionQuery remoteIPLike(String remoteIPLike) {
		if (remoteIPLike == null) {
			throw new RuntimeException("remoteIP is null");
		}
		this.remoteIPLike = remoteIPLike;
		return this;
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setRemoteIP(String remoteIP) {
		this.remoteIP = remoteIP;
	}

	public void setRemoteIPLike(String remoteIPLike) {
		this.remoteIPLike = remoteIPLike;
	}

	public void setType(String type) {
		this.type = type;
	}

	public RemotePermissionQuery type(String type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		this.type = type;
		return this;
	}

}