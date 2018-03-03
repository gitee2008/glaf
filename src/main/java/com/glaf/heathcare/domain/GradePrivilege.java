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

package com.glaf.heathcare.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "HEALTH_GRADE_PRIVILEGE")
public class GradePrivilege implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", length = 50, nullable = false)
	protected String id;

	/**
	 * 租户编号
	 */
	@Column(name = "TENANTID_", length = 50, nullable = false)
	protected String tenantId;

	@Column(name = "GRADEID_", length = 50, nullable = false)
	protected String gradeId;

	@Column(name = "USERID_", length = 50, nullable = false)
	protected String userId;

	@Column(name = "PRIVILEGE_", length = 50, nullable = false)
	protected String privilege;

	public GradePrivilege() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GradePrivilege other = (GradePrivilege) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getGradeId() {
		return gradeId;
	}

	public String getId() {
		return id;
	}

	public String getPrivilege() {
		return privilege;
	}

	public String getTenantId() {
		return tenantId;
	}

	public String getUserId() {
		return userId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
