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

package com.glaf.core.query;

import java.util.Date;
import java.util.List;

public class UserQuery extends BaseQuery {
	private static final long serialVersionUID = 1L;
	protected Integer accountType;
	protected String activationCode;
	protected List<String> activationCodes;
	protected String actorIdLike;
	protected Long organizationId;
	protected List<Long> organizationIds;
	protected String tenantId;
	protected String isSystem;
	protected Date lastLoginDateGreaterThanOrEqual;
	protected Date lastLoginDateLessThanOrEqual;
	protected String loginIP;
	protected String loginIPLike;
	protected Integer loginRetry;
	protected Integer loginRetryGreaterThanOrEqual;
	protected Integer loginRetryLessThanOrEqual;
	protected String mail;
	protected String mailLike;
	protected String mobile;
	protected String mobileLike;
	protected String name;
	protected String nameLike;
	protected String phoneNumber;
	protected String phoneNumberLike;
	protected String principalshipCode;
	protected String remarkLike;
	protected String roleId;
	protected String superiorId;
	protected List<String> superiorIds;

	public UserQuery() {

	}

	public UserQuery accountType(Integer accountType) {
		if (accountType == null) {
			throw new RuntimeException("accountType is null");
		}
		this.accountType = accountType;
		return this;
	}

	public UserQuery activationCode(String activationCode) {
		if (activationCode == null) {
			throw new RuntimeException("activationCode is null");
		}
		this.activationCode = activationCode;
		return this;
	}

	public UserQuery activationCodes(List<String> activationCodes) {
		if (activationCodes == null) {
			throw new RuntimeException("activationCodes is empty ");
		}
		this.activationCodes = activationCodes;
		return this;
	}

	public UserQuery actorIdLike(String actorIdLike) {
		if (actorIdLike == null) {
			throw new RuntimeException("actorId is null");
		}
		this.actorIdLike = actorIdLike;
		return this;
	}

	public Integer getAccountType() {
		return accountType;
	}

	public String getActivationCode() {
		return activationCode;
	}

	public List<String> getActivationCodes() {
		return activationCodes;
	}

	public String getActorIdLike() {
		if (actorIdLike != null && actorIdLike.trim().length() > 0) {
			if (!actorIdLike.startsWith("%")) {
				actorIdLike = "%" + actorIdLike;
			}
			if (!actorIdLike.endsWith("%")) {
				actorIdLike = actorIdLike + "%";
			}
		}
		return actorIdLike;
	}

	public String getIsSystem() {
		return isSystem;
	}

	public Date getLastLoginDateGreaterThanOrEqual() {
		return lastLoginDateGreaterThanOrEqual;
	}

	public Date getLastLoginDateLessThanOrEqual() {
		return lastLoginDateLessThanOrEqual;
	}

	public String getLoginIP() {
		return loginIP;
	}

	public String getLoginIPLike() {
		if (loginIPLike != null && loginIPLike.trim().length() > 0) {
			if (!loginIPLike.startsWith("%")) {
				loginIPLike = "%" + loginIPLike;
			}
			if (!loginIPLike.endsWith("%")) {
				loginIPLike = loginIPLike + "%";
			}
		}
		return loginIPLike;
	}

	public Integer getLoginRetry() {
		return loginRetry;
	}

	public Integer getLoginRetryGreaterThanOrEqual() {
		return loginRetryGreaterThanOrEqual;
	}

	public Integer getLoginRetryLessThanOrEqual() {
		return loginRetryLessThanOrEqual;
	}

	public String getMail() {
		return mail;
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

	public String getName() {
		return name;
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

	public Long getOrganizationId() {
		return organizationId;
	}

	public List<Long> getOrganizationIds() {
		return organizationIds;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getPhoneNumberLike() {
		return phoneNumberLike;
	}

	public String getPrincipalshipCode() {
		return principalshipCode;
	}

	public String getRemarkLike() {
		if (remarkLike != null && remarkLike.trim().length() > 0) {
			if (!remarkLike.startsWith("%")) {
				remarkLike = "%" + remarkLike;
			}
			if (!remarkLike.endsWith("%")) {
				remarkLike = remarkLike + "%";
			}
		}
		return remarkLike;
	}

	public String getRoleId() {
		return roleId;
	}

	public String getSuperiorId() {
		return superiorId;
	}

	public List<String> getSuperiorIds() {
		return superiorIds;
	}

	public String getTenantId() {
		return tenantId;
	}

	public UserQuery isSystem(String isSystem) {
		if (isSystem == null) {
			throw new RuntimeException("isSystem is null");
		}
		this.isSystem = isSystem;
		return this;
	}

	public UserQuery lastLoginDateGreaterThanOrEqual(Date lastLoginDateGreaterThanOrEqual) {
		if (lastLoginDateGreaterThanOrEqual == null) {
			throw new RuntimeException("lastLoginDate is null");
		}
		this.lastLoginDateGreaterThanOrEqual = lastLoginDateGreaterThanOrEqual;
		return this;
	}

	public UserQuery lastLoginDateLessThanOrEqual(Date lastLoginDateLessThanOrEqual) {
		if (lastLoginDateLessThanOrEqual == null) {
			throw new RuntimeException("lastLoginDate is null");
		}
		this.lastLoginDateLessThanOrEqual = lastLoginDateLessThanOrEqual;
		return this;
	}

	public UserQuery loginIP(String loginIP) {
		if (loginIP == null) {
			throw new RuntimeException("loginIP is null");
		}
		this.loginIP = loginIP;
		return this;
	}

	public UserQuery loginIPLike(String loginIPLike) {
		if (loginIPLike == null) {
			throw new RuntimeException("loginIP is null");
		}
		this.loginIPLike = loginIPLike;
		return this;
	}

	public UserQuery loginRetry(Integer loginRetry) {
		if (loginRetry == null) {
			throw new RuntimeException("loginRetry is null");
		}
		this.loginRetry = loginRetry;
		return this;
	}

	public UserQuery loginRetryGreaterThanOrEqual(Integer loginRetryGreaterThanOrEqual) {
		if (loginRetryGreaterThanOrEqual == null) {
			throw new RuntimeException("loginRetry is null");
		}
		this.loginRetryGreaterThanOrEqual = loginRetryGreaterThanOrEqual;
		return this;
	}

	public UserQuery loginRetryLessThanOrEqual(Integer loginRetryLessThanOrEqual) {
		if (loginRetryLessThanOrEqual == null) {
			throw new RuntimeException("loginRetry is null");
		}
		this.loginRetryLessThanOrEqual = loginRetryLessThanOrEqual;
		return this;
	}

	public UserQuery mail(String mail) {
		if (mail == null) {
			throw new RuntimeException("mail is null");
		}
		this.mail = mail;
		return this;
	}

	public UserQuery mailLike(String mailLike) {
		if (mailLike == null) {
			throw new RuntimeException("mail is null");
		}
		this.mailLike = mailLike;
		return this;
	}

	public UserQuery mobile(String mobile) {
		if (mobile == null) {
			throw new RuntimeException("mobile is null");
		}
		this.mobile = mobile;
		return this;
	}

	public UserQuery mobileLike(String mobileLike) {
		if (mobileLike == null) {
			throw new RuntimeException("mobile is null");
		}
		this.mobileLike = mobileLike;
		return this;
	}

	public UserQuery name(String name) {
		if (name == null) {
			throw new RuntimeException("name is null");
		}
		this.name = name;
		return this;
	}

	public UserQuery nameLike(String nameLike) {
		if (nameLike == null) {
			throw new RuntimeException("name is null");
		}
		this.nameLike = nameLike;
		return this;
	}

	public UserQuery organizationId(Long organizationId) {
		if (organizationId == null) {
			throw new RuntimeException("organizationId is null");
		}
		this.organizationId = organizationId;
		return this;
	}

	public UserQuery organizationIds(List<Long> organizationIds) {
		if (organizationIds == null) {
			throw new RuntimeException("organizationIds is empty ");
		}
		this.organizationIds = organizationIds;
		return this;
	}

	public UserQuery phoneNumber(String phoneNumber) {
		if (phoneNumber == null) {
			throw new RuntimeException("phoneNumber is null");
		}
		this.phoneNumber = phoneNumber;
		return this;
	}

	public UserQuery phoneNumberLike(String phoneNumberLike) {
		if (phoneNumberLike == null) {
			throw new RuntimeException("phoneNumber is null");
		}
		this.phoneNumberLike = phoneNumberLike;
		return this;
	}

	public UserQuery principalshipCode(String principalshipCode) {
		if (principalshipCode == null) {
			throw new RuntimeException("principalshipCode is null");
		}
		this.principalshipCode = principalshipCode;
		return this;
	}

	public UserQuery remarkLike(String remarkLike) {
		if (remarkLike == null) {
			throw new RuntimeException("remark is null");
		}
		this.remarkLike = remarkLike;
		return this;
	}

	public UserQuery roleId(String roleId) {
		if (roleId == null) {
			throw new RuntimeException("roleId is null");
		}
		this.roleId = roleId;
		return this;
	}

	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}

	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}

	public void setActivationCodes(List<String> activationCodes) {
		this.activationCodes = activationCodes;
	}

	public void setActorIdLike(String actorIdLike) {
		this.actorIdLike = actorIdLike;
	}

	public void setIsSystem(String isSystem) {
		this.isSystem = isSystem;
	}

	public void setLastLoginDateGreaterThanOrEqual(Date lastLoginDateGreaterThanOrEqual) {
		this.lastLoginDateGreaterThanOrEqual = lastLoginDateGreaterThanOrEqual;
	}

	public void setLastLoginDateLessThanOrEqual(Date lastLoginDateLessThanOrEqual) {
		this.lastLoginDateLessThanOrEqual = lastLoginDateLessThanOrEqual;
	}

	public void setLoginIP(String loginIP) {
		this.loginIP = loginIP;
	}

	public void setLoginIPLike(String loginIPLike) {
		this.loginIPLike = loginIPLike;
	}

	public void setLoginRetry(Integer loginRetry) {
		this.loginRetry = loginRetry;
	}

	public void setLoginRetryGreaterThanOrEqual(Integer loginRetryGreaterThanOrEqual) {
		this.loginRetryGreaterThanOrEqual = loginRetryGreaterThanOrEqual;
	}

	public void setLoginRetryLessThanOrEqual(Integer loginRetryLessThanOrEqual) {
		this.loginRetryLessThanOrEqual = loginRetryLessThanOrEqual;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public void setMailLike(String mailLike) {
		this.mailLike = mailLike;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setMobileLike(String mobileLike) {
		this.mobileLike = mobileLike;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNameLike(String nameLike) {
		this.nameLike = nameLike;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public void setOrganizationIds(List<Long> organizationIds) {
		this.organizationIds = organizationIds;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setPhoneNumberLike(String phoneNumberLike) {
		if (phoneNumberLike != null) {
			if (!phoneNumberLike.startsWith("%")) {
				phoneNumberLike = "%" + phoneNumberLike;
			}
			if (!phoneNumberLike.endsWith("%")) {
				phoneNumberLike = phoneNumberLike + "%";
			}
		}
		this.phoneNumberLike = phoneNumberLike;
	}

	public void setPrincipalshipCode(String principalshipCode) {
		this.principalshipCode = principalshipCode;
	}

	public void setRemarkLike(String remarkLike) {
		if (remarkLike != null) {
			if (!remarkLike.startsWith("%")) {
				remarkLike = "%" + remarkLike;
			}
			if (!remarkLike.endsWith("%")) {
				remarkLike = remarkLike + "%";
			}
		}
		this.remarkLike = remarkLike;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public void setSuperiorId(String superiorId) {
		this.superiorId = superiorId;
	}

	public void setSuperiorIds(List<String> superiorIds) {
		this.superiorIds = superiorIds;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public UserQuery superiorId(String superiorId) {
		if (superiorId == null) {
			throw new RuntimeException("superiorId is null");
		}
		this.superiorId = superiorId;
		return this;
	}

	public UserQuery superiorIds(List<String> superiorIds) {
		if (superiorIds == null) {
			throw new RuntimeException("superiorIds is empty ");
		}
		this.superiorIds = superiorIds;
		return this;
	}

	public UserQuery tenantId(String tenantId) {
		if (tenantId == null) {
			throw new RuntimeException("tenantId is null");
		}
		this.tenantId = tenantId;
		return this;
	}

}