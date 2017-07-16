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

package com.glaf.base.modules.sys.model;

import java.io.Serializable;

public class UserRole implements Serializable {

	private static final long serialVersionUID = 1L;

	protected String tenantId;

	protected String actorId;

	protected String actorName;

	protected String roleId;

	protected String roleCode;

	protected String roleName;

	public UserRole() {

	}

	public String getActorId() {
		return actorId;
	}

	public String getActorName() {
		return actorName;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public String getRoleId() {
		return roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setActorId(String actorId) {
		this.actorId = actorId;
	}

	public void setActorName(String actorName) {
		this.actorName = actorName;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

}
