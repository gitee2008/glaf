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

package com.glaf.base.modules.sys.query;

import java.util.*;
import com.glaf.core.query.DataQuery;

public class LoginUserQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected List<String> ids;
	protected Collection<String> appActorIds;
	protected String userId;
	protected List<String> userIds;
	protected String nameLike;
	protected String systemCode;
	protected String organizationLike;
	protected String departmentLike;
	protected String positionLike;
	protected String mailLike;
	protected String mobileLike;
	protected Date loginTimeGreaterThanOrEqual;
	protected Date loginTimeLessThanOrEqual;
	protected List<String> createBys;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	public LoginUserQuery() {

	}

	public LoginUserQuery createBys(List<String> createBys) {
		if (createBys == null) {
			throw new RuntimeException("createBys is empty ");
		}
		this.createBys = createBys;
		return this;
	}

	public LoginUserQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public LoginUserQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public LoginUserQuery departmentLike(String departmentLike) {
		if (departmentLike == null) {
			throw new RuntimeException("department is null");
		}
		this.departmentLike = departmentLike;
		return this;
	}

	public Collection<String> getAppActorIds() {
		return appActorIds;
	}

	public List<String> getCreateBys() {
		return createBys;
	}

	public Date getCreateTimeGreaterThanOrEqual() {
		return createTimeGreaterThanOrEqual;
	}

	public Date getCreateTimeLessThanOrEqual() {
		return createTimeLessThanOrEqual;
	}

	public String getDepartmentLike() {
		if (departmentLike != null && departmentLike.trim().length() > 0) {
			if (!departmentLike.startsWith("%")) {
				departmentLike = "%" + departmentLike;
			}
			if (!departmentLike.endsWith("%")) {
				departmentLike = departmentLike + "%";
			}
		}
		return departmentLike;
	}

	public Date getLoginTimeGreaterThanOrEqual() {
		return loginTimeGreaterThanOrEqual;
	}

	public Date getLoginTimeLessThanOrEqual() {
		return loginTimeLessThanOrEqual;
	}

	public String getMailLike() {
		if (mailLike != null && mailLike.trim().length() > 0) {
			if (!mailLike.startsWith("%")) {
				mailLike = "%" + mailLike;
			}
			if (!mailLike.endsWith("%")) {
				mailLike = mailLike + "%";
			}
		}
		return mailLike;
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

	public String getNameLike() {
		if (nameLike != null && nameLike.trim().length() > 0) {
			if (!nameLike.startsWith("%")) {
				nameLike = "%" + nameLike;
			}
			if (!nameLike.endsWith("%")) {
				nameLike = nameLike + "%";
			}
		}
		return nameLike;
	}

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("userId".equals(sortColumn)) {
				orderBy = "E.USERID_" + a_x;
			}

			if ("name".equals(sortColumn)) {
				orderBy = "E.USERNAME_" + a_x;
			}

			if ("loginId".equals(sortColumn)) {
				orderBy = "E.LOGINID_" + a_x;
			}

			if ("password".equals(sortColumn)) {
				orderBy = "E.PASSWORD_" + a_x;
			}

			if ("passwordType".equals(sortColumn)) {
				orderBy = "E.PASSWORDTYPE_" + a_x;
			}

			if ("systemCode".equals(sortColumn)) {
				orderBy = "E.SYSTEMCODE_" + a_x;
			}

			if ("organization".equals(sortColumn)) {
				orderBy = "E.ORGANIZATION_" + a_x;
			}

			if ("department".equals(sortColumn)) {
				orderBy = "E.DEPARTMENT_" + a_x;
			}

			if ("position".equals(sortColumn)) {
				orderBy = "E.POSITION_" + a_x;
			}

			if ("mail".equals(sortColumn)) {
				orderBy = "E.MAIL_" + a_x;
			}

			if ("mobile".equals(sortColumn)) {
				orderBy = "E.MOBILE_" + a_x;
			}

			if ("timeLive".equals(sortColumn)) {
				orderBy = "E.TIMELIVE_" + a_x;
			}

			if ("loginTime".equals(sortColumn)) {
				orderBy = "E.LOGINTIME_" + a_x;
			}

			if ("attribute".equals(sortColumn)) {
				orderBy = "E.ATTRIBUTE_" + a_x;
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

	public String getOrganizationLike() {
		if (organizationLike != null && organizationLike.trim().length() > 0) {
			if (!organizationLike.startsWith("%")) {
				organizationLike = "%" + organizationLike;
			}
			if (!organizationLike.endsWith("%")) {
				organizationLike = organizationLike + "%";
			}
		}
		return organizationLike;
	}

	public String getPositionLike() {
		if (positionLike != null && positionLike.trim().length() > 0) {
			if (!positionLike.startsWith("%")) {
				positionLike = "%" + positionLike;
			}
			if (!positionLike.endsWith("%")) {
				positionLike = positionLike + "%";
			}
		}
		return positionLike;
	}

	public String getSystemCode() {
		return systemCode;
	}

	public String getUserId() {
		return userId;
	}

	public List<String> getUserIds() {
		return userIds;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("userId", "USERID_");
		addColumn("name", "USERNAME_");
		addColumn("loginId", "LOGINID_");
		addColumn("password", "PASSWORD_");
		addColumn("passwordType", "PASSWORDTYPE_");
		addColumn("systemCode", "SYSTEMCODE_");
		addColumn("organization", "ORGANIZATION_");
		addColumn("department", "DEPARTMENT_");
		addColumn("position", "POSITION_");
		addColumn("mail", "MAIL_");
		addColumn("mobile", "MOBILE_");
		addColumn("timeLive", "TIMELIVE_");
		addColumn("loginTime", "LOGINTIME_");
		addColumn("attribute", "ATTRIBUTE_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
	}

	public LoginUserQuery loginTimeGreaterThanOrEqual(Date loginTimeGreaterThanOrEqual) {
		if (loginTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("loginTime is null");
		}
		this.loginTimeGreaterThanOrEqual = loginTimeGreaterThanOrEqual;
		return this;
	}

	public LoginUserQuery loginTimeLessThanOrEqual(Date loginTimeLessThanOrEqual) {
		if (loginTimeLessThanOrEqual == null) {
			throw new RuntimeException("loginTime is null");
		}
		this.loginTimeLessThanOrEqual = loginTimeLessThanOrEqual;
		return this;
	}

	public LoginUserQuery mailLike(String mailLike) {
		if (mailLike == null) {
			throw new RuntimeException("mail is null");
		}
		this.mailLike = mailLike;
		return this;
	}

	public LoginUserQuery mobileLike(String mobileLike) {
		if (mobileLike == null) {
			throw new RuntimeException("mobile is null");
		}
		this.mobileLike = mobileLike;
		return this;
	}

	public LoginUserQuery nameLike(String nameLike) {
		if (nameLike == null) {
			throw new RuntimeException("name is null");
		}
		this.nameLike = nameLike;
		return this;
	}

	public LoginUserQuery organizationLike(String organizationLike) {
		if (organizationLike == null) {
			throw new RuntimeException("organization is null");
		}
		this.organizationLike = organizationLike;
		return this;
	}

	public LoginUserQuery positionLike(String positionLike) {
		if (positionLike == null) {
			throw new RuntimeException("position is null");
		}
		this.positionLike = positionLike;
		return this;
	}

	public void setAppActorIds(Collection<String> appActorIds) {
		this.appActorIds = appActorIds;
	}

	public void setCreateBys(List<String> createBys) {
		this.createBys = createBys;
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setDepartmentLike(String departmentLike) {
		this.departmentLike = departmentLike;
	}

	public void setLoginTimeGreaterThanOrEqual(Date loginTimeGreaterThanOrEqual) {
		this.loginTimeGreaterThanOrEqual = loginTimeGreaterThanOrEqual;
	}

	public void setLoginTimeLessThanOrEqual(Date loginTimeLessThanOrEqual) {
		this.loginTimeLessThanOrEqual = loginTimeLessThanOrEqual;
	}

	public void setMailLike(String mailLike) {
		this.mailLike = mailLike;
	}

	public void setMobileLike(String mobileLike) {
		this.mobileLike = mobileLike;
	}

	public void setNameLike(String nameLike) {
		this.nameLike = nameLike;
	}

	public void setOrganizationLike(String organizationLike) {
		this.organizationLike = organizationLike;
	}

	public void setPositionLike(String positionLike) {
		this.positionLike = positionLike;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setUserIds(List<String> userIds) {
		this.userIds = userIds;
	}

	public LoginUserQuery systemCode(String systemCode) {
		if (systemCode == null) {
			throw new RuntimeException("systemCode is null");
		}
		this.systemCode = systemCode;
		return this;
	}

	public LoginUserQuery userId(String userId) {
		if (userId == null) {
			throw new RuntimeException("userId is null");
		}
		this.userId = userId;
		return this;
	}

	public LoginUserQuery userIds(List<String> userIds) {
		if (userIds == null) {
			throw new RuntimeException("userIds is empty ");
		}
		this.userIds = userIds;
		return this;
	}

}