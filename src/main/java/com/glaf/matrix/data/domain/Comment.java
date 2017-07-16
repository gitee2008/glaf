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

package com.glaf.matrix.data.domain;

import java.util.Date;

public class Comment implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	protected long id;

	protected long topId;

	protected String tenantId;

	protected long organizationId;

	protected String userId;

	protected String username;

	protected int approval;

	protected Date approvalDate;

	protected String content;

	public Comment() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Comment other = (Comment) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public int getApproval() {
		return approval;
	}

	public Date getApprovalDate() {
		return approvalDate;
	}

	public String getContent() {
		return content;
	}

	public long getId() {
		return id;
	}

	public long getOrganizationId() {
		return organizationId;
	}

	public String getTenantId() {
		return tenantId;
	}

	public long getTopId() {
		return topId;
	}

	public String getUserId() {
		return userId;
	}

	public String getUsername() {
		return username;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public void setApproval(int approval) {
		this.approval = approval;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setTopId(long topId) {
		this.topId = topId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
