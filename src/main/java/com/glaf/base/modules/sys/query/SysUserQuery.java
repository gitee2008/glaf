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

public class SysUserQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected String userId;
	protected Integer accountType;
	protected String adminFlag;
	protected Date availDateEndGreaterThanOrEqual;
	protected Date availDateEndLessThanOrEqual;
	protected Date availDateStartGreaterThanOrEqual;
	protected Date availDateStartLessThanOrEqual;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;
	protected Long organizationId;
	protected List<Long> organizationIds;
	protected String email;
	protected String emailLike;
	protected String lastLoginIP;
	protected String lastLoginIPLike;
	protected List<String> lastLoginIPs;
	protected Date lastLoginTimeGreaterThanOrEqual;
	protected Date lastLoginTimeLessThanOrEqual;
	protected String mobile;
	protected String mobileLike;
	protected String name;
	protected String nameLike;
	protected String namePinyinLike;
	protected List<String> names;
	protected String roleCode;
	protected List<String> roleCodes;
	protected String telephone;
	protected String telephoneLike;
	protected int userType;
	protected int syncFlag;
	protected String syncOperatorType;
	protected String searchWord;
	protected String organizationName;
	protected String organizationCode;
	protected String organizationNameLike;
	protected String organizationCodeLike;
	protected String organizationNameOrCode;
	protected Long sysOrganizationId;
	protected String groupUserId;
	protected String groupLeaderId;
	protected String roleId;
	protected List<String> userIds;
	protected List<String> userIdsNotIn;

	public SysUserQuery() {

	}

	public SysUserQuery accountType(Integer accountType) {
		if (accountType == null) {
			throw new RuntimeException("accountType is null");
		}
		this.accountType = accountType;
		return this;
	}

	public SysUserQuery adminFlag(String adminFlag) {
		if (adminFlag == null) {
			throw new RuntimeException("adminFlag is null");
		}
		this.adminFlag = adminFlag;
		return this;
	}

	public SysUserQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public SysUserQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public SysUserQuery email(String email) {
		if (email == null) {
			throw new RuntimeException("email is null");
		}
		this.email = email;
		return this;
	}

	public SysUserQuery emailLike(String emailLike) {
		if (emailLike == null) {
			throw new RuntimeException("email is null");
		}
		this.emailLike = emailLike;
		return this;
	}

	public Integer getAccountType() {
		return accountType;
	}

	public String getAdminFlag() {
		return adminFlag;
	}

	public Date getAvailDateEndGreaterThanOrEqual() {
		return availDateEndGreaterThanOrEqual;
	}

	public Date getAvailDateEndLessThanOrEqual() {
		return availDateEndLessThanOrEqual;
	}

	public Date getAvailDateStartGreaterThanOrEqual() {
		return availDateStartGreaterThanOrEqual;
	}

	public Date getAvailDateStartLessThanOrEqual() {
		return availDateStartLessThanOrEqual;
	}

	public Map<String, String> getColumnMap() {
		Map<String, String> columnMap = new HashMap<String, String>();
		columnMap.put("actorId", "USERID");
		columnMap.put("name", "USERNAME");
		columnMap.put("createTime", "CREATETIME");
		columnMap.put("organizationId", "ORGANIZATIONID");
		columnMap.put("adminFlag", "ISSYSTEM");
		columnMap.put("locked", "LOCKED");
		columnMap.put("mobile", "MOBILE");
		columnMap.put("email", "EMAIL");
		columnMap.put("telephone", "TELEPHONE");
		columnMap.put("userType", "USERTYPE");
		return columnMap;
	}

	public Date getCreateTimeGreaterThanOrEqual() {
		return createTimeGreaterThanOrEqual;
	}

	public Date getCreateTimeLessThanOrEqual() {
		return createTimeLessThanOrEqual;
	}

	public String getEmail() {
		return email;
	}

	public String getEmailLike() {
		if (emailLike != null && emailLike.trim().length() > 0) {
			if (!emailLike.startsWith("%")) {
				emailLike = "%" + emailLike;
			}
			if (!emailLike.endsWith("%")) {
				emailLike = emailLike + "%";
			}
		}
		return emailLike;
	}

	public String getGroupLeaderId() {
		return groupLeaderId;
	}

	public String getGroupUserId() {
		return groupUserId;
	}

	public Map<String, String> getJavaTypeMap() {
		Map<String, String> javaTypeMap = new HashMap<String, String>();
		javaTypeMap.put("actorId", "String");
		javaTypeMap.put("name", "String");
		javaTypeMap.put("createTime", "Date");
		javaTypeMap.put("organizationId", "Long");
		javaTypeMap.put("adminFlag", "String");
		javaTypeMap.put("status", "Integer");
		javaTypeMap.put("mobile", "String");
		javaTypeMap.put("email", "String");
		javaTypeMap.put("telephone", "String");
		javaTypeMap.put("userType", "Integer");
		return javaTypeMap;
	}

	public String getLastLoginIP() {
		return lastLoginIP;
	}

	public String getLastLoginIPLike() {
		if (lastLoginIPLike != null && lastLoginIPLike.trim().length() > 0) {
			if (!lastLoginIPLike.startsWith("%")) {
				lastLoginIPLike = "%" + lastLoginIPLike;
			}
			if (!lastLoginIPLike.endsWith("%")) {
				lastLoginIPLike = lastLoginIPLike + "%";
			}
		}
		return lastLoginIPLike;
	}

	public List<String> getLastLoginIPs() {
		return lastLoginIPs;
	}

	public Date getLastLoginTimeGreaterThanOrEqual() {
		return lastLoginTimeGreaterThanOrEqual;
	}

	public Date getLastLoginTimeLessThanOrEqual() {
		return lastLoginTimeLessThanOrEqual;
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

	public String getNamePinyinLike() {
		if (namePinyinLike != null && namePinyinLike.trim().length() > 0) {
			if (!namePinyinLike.endsWith("%")) {
				namePinyinLike = namePinyinLike + "%";
			}
		}
		return namePinyinLike;
	}

	public List<String> getNames() {
		return names;
	}

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("account".equals(sortColumn)) {
				orderBy = "E.USERID" + a_x;
			}

			if ("actorId".equals(sortColumn)) {
				orderBy = "E.USERID" + a_x;
			}

			if ("password".equals(sortColumn)) {
				orderBy = "E.PASSWORD" + a_x;
			}

			if ("code".equals(sortColumn)) {
				orderBy = "E.CODE" + a_x;
			}

			if ("name".equals(sortColumn)) {
				orderBy = "E.USERNAME" + a_x;
			}

			if ("locked".equals(sortColumn)) {
				orderBy = "E.LOCKED" + a_x;
			}

			if ("createTime".equals(sortColumn)) {
				orderBy = "E.CREATETIME" + a_x;
			}

			if ("lastLoginTime".equals(sortColumn)) {
				orderBy = "E.LASTLOGINTIME" + a_x;
			}

			if ("lastLoginIP".equals(sortColumn)) {
				orderBy = "E.LASTLOGINIP" + a_x;
			}

			if ("mobile".equals(sortColumn)) {
				orderBy = "E.MOBILE" + a_x;
			}

			if ("email".equals(sortColumn)) {
				orderBy = "E.EMAIL" + a_x;
			}

			if ("telephone".equals(sortColumn)) {
				orderBy = "E.TELEPHONE" + a_x;
			}

			if ("accountType".equals(sortColumn)) {
				orderBy = "E.ACCOUNTTYPE" + a_x;
			}

			if ("organizationId".equals(sortColumn)) {
				orderBy = "E.ORGANIZATIONID" + a_x;
			}

			if ("organizationName".equals(sortColumn)) {
				orderBy = "E.ORGANIZATIONID" + a_x;
			}

			if ("adminFlag".equals(sortColumn)) {
				orderBy = "E.ISSYSTEM" + a_x;
			}

		}
		return orderBy;
	}

	public String getOrganizationCode() {

		return organizationCode;
	}

	public String getOrganizationCodeLike() {
		if (organizationCodeLike != null && organizationCodeLike.trim().length() > 0) {
			if (!organizationCodeLike.startsWith("%")) {
				organizationCodeLike = "%" + organizationCodeLike;
			}
			if (!organizationCodeLike.endsWith("%")) {
				organizationCodeLike = organizationCodeLike + "%";
			}
		}
		return organizationCodeLike;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public List<Long> getOrganizationIds() {
		return organizationIds;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public String getOrganizationNameLike() {
		if (organizationNameLike != null && organizationNameLike.trim().length() > 0) {
			if (!organizationNameLike.startsWith("%")) {
				organizationNameLike = "%" + organizationNameLike;
			}
			if (!organizationNameLike.endsWith("%")) {
				organizationNameLike = organizationNameLike + "%";
			}
		}
		return organizationNameLike;
	}

	public String getOrganizationNameOrCode() {
		return organizationNameOrCode;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public List<String> getRoleCodes() {
		return roleCodes;
	}

	public String getRoleId() {
		return roleId;
	}

	public String getSearchWord() {
		if (searchWord != null && searchWord.trim().length() > 0) {
			if (!searchWord.startsWith("%")) {
				searchWord = "%" + searchWord;
			}
			if (!searchWord.endsWith("%")) {
				searchWord = searchWord + "%";
			}
		}
		return searchWord;
	}

	public int getSyncFlag() {
		return syncFlag;
	}

	public String getSyncOperatorType() {
		return syncOperatorType;
	}

	public Long getSysOrganizationId() {
		return sysOrganizationId;
	}

	public String getTelephone() {
		return telephone;
	}

	public String getTelephoneLike() {
		if (telephoneLike != null && telephoneLike.trim().length() > 0) {
			if (!telephoneLike.startsWith("%")) {
				telephoneLike = "%" + telephoneLike;
			}
			if (!telephoneLike.endsWith("%")) {
				telephoneLike = telephoneLike + "%";
			}
		}
		return telephoneLike;
	}

	public String getTenantId() {
		return tenantId;
	}

	public String getUserId() {
		return userId;
	}

	public List<String> getUserIds() {
		return userIds;
	}

	public List<String> getUserIdsNotIn() {
		return userIdsNotIn;
	}

	public int getUserType() {
		return userType;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID");
		addColumn("account", "USERID");
		addColumn("name", "NAME");
		addColumn("createTime", "CREATETIME");
		addColumn("lastLoginTime", "LASTLOGINTIME");
		addColumn("lastLoginIP", "LASTLOGINIP");
		addColumn("mobile", "MOBILE");
		addColumn("email", "EMAIL");
		addColumn("userType", "USERTYPE");
		addColumn("accountType", "ACCOUNTTYPE");
		addColumn("organizationId", "ORGANIZATIONID");
		addColumn("adminFlag", "ADMINFLAG");
	}

	public SysUserQuery lastLoginIP(String lastLoginIP) {
		if (lastLoginIP == null) {
			throw new RuntimeException("lastLoginIP is null");
		}
		this.lastLoginIP = lastLoginIP;
		return this;
	}

	public SysUserQuery lastLoginIPLike(String lastLoginIPLike) {
		if (lastLoginIPLike == null) {
			throw new RuntimeException("lastLoginIP is null");
		}
		this.lastLoginIPLike = lastLoginIPLike;
		return this;
	}

	public SysUserQuery lastLoginIPs(List<String> lastLoginIPs) {
		if (lastLoginIPs == null) {
			throw new RuntimeException("lastLoginIPs is empty ");
		}
		this.lastLoginIPs = lastLoginIPs;
		return this;
	}

	public SysUserQuery lastLoginTimeGreaterThanOrEqual(Date lastLoginTimeGreaterThanOrEqual) {
		if (lastLoginTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("lastLoginTime is null");
		}
		this.lastLoginTimeGreaterThanOrEqual = lastLoginTimeGreaterThanOrEqual;
		return this;
	}

	public SysUserQuery lastLoginTimeLessThanOrEqual(Date lastLoginTimeLessThanOrEqual) {
		if (lastLoginTimeLessThanOrEqual == null) {
			throw new RuntimeException("lastLoginTime is null");
		}
		this.lastLoginTimeLessThanOrEqual = lastLoginTimeLessThanOrEqual;
		return this;
	}

	public SysUserQuery mobile(String mobile) {
		if (mobile == null) {
			throw new RuntimeException("mobile is null");
		}
		this.mobile = mobile;
		return this;
	}

	public SysUserQuery mobileLike(String mobileLike) {
		if (mobileLike == null) {
			throw new RuntimeException("mobile is null");
		}
		this.mobileLike = mobileLike;
		return this;
	}

	public SysUserQuery name(String name) {
		if (name == null) {
			throw new RuntimeException("name is null");
		}
		this.name = name;
		return this;
	}

	public SysUserQuery nameLike(String nameLike) {
		if (nameLike == null) {
			throw new RuntimeException("name is null");
		}
		this.nameLike = nameLike;
		return this;
	}

	public SysUserQuery namePinyinLike(String namePinyinLike) {
		if (namePinyinLike == null) {
			throw new RuntimeException("namePinyinLike is null");
		}
		this.namePinyinLike = namePinyinLike;
		return this;
	}

	public SysUserQuery names(List<String> names) {
		if (names == null) {
			throw new RuntimeException("names is empty ");
		}
		this.names = names;
		return this;
	}

	public SysUserQuery organizationId(Long organizationId) {
		if (organizationId == null) {
			throw new RuntimeException("organizationId is null");
		}
		this.organizationId = organizationId;
		return this;
	}

	public SysUserQuery organizationIds(List<Long> organizationIds) {
		if (organizationIds == null) {
			throw new RuntimeException("organizationIds is empty ");
		}
		this.organizationIds = organizationIds;
		return this;
	}

	public SysUserQuery organizationNameLike(String organizationNameLike) {
		if (organizationNameLike == null) {
			throw new RuntimeException("organizationNameLike is null");
		}
		this.organizationNameLike = organizationNameLike;
		return this;
	}

	public SysUserQuery roleCode(String roleCode) {
		if (roleCode == null) {
			throw new RuntimeException("roleCode is null");
		}
		this.roleCode = roleCode;
		return this;
	}

	public SysUserQuery searchWord(String searchWord) {
		if (searchWord == null) {
			throw new RuntimeException("searchWord is null");
		}
		this.searchWord = searchWord;
		return this;
	}

	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}

	public void setAdminFlag(String adminFlag) {
		this.adminFlag = adminFlag;
	}

	public void setAvailDateEndGreaterThanOrEqual(Date availDateEndGreaterThanOrEqual) {
		this.availDateEndGreaterThanOrEqual = availDateEndGreaterThanOrEqual;
	}

	public void setAvailDateEndLessThanOrEqual(Date availDateEndLessThanOrEqual) {
		this.availDateEndLessThanOrEqual = availDateEndLessThanOrEqual;
	}

	public void setAvailDateStartGreaterThanOrEqual(Date availDateStartGreaterThanOrEqual) {
		this.availDateStartGreaterThanOrEqual = availDateStartGreaterThanOrEqual;
	}

	public void setAvailDateStartLessThanOrEqual(Date availDateStartLessThanOrEqual) {
		this.availDateStartLessThanOrEqual = availDateStartLessThanOrEqual;
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setEmailLike(String emailLike) {
		this.emailLike = emailLike;
	}

	public void setGroupLeaderId(String groupLeaderId) {
		this.groupLeaderId = groupLeaderId;
	}

	public void setGroupUserId(String groupUserId) {
		this.groupUserId = groupUserId;
	}

	public void setLastLoginIP(String lastLoginIP) {
		this.lastLoginIP = lastLoginIP;
	}

	public void setLastLoginIPLike(String lastLoginIPLike) {
		this.lastLoginIPLike = lastLoginIPLike;
	}

	public void setLastLoginIPs(List<String> lastLoginIPs) {
		this.lastLoginIPs = lastLoginIPs;
	}

	public void setLastLoginTimeGreaterThanOrEqual(Date lastLoginTimeGreaterThanOrEqual) {
		this.lastLoginTimeGreaterThanOrEqual = lastLoginTimeGreaterThanOrEqual;
	}

	public void setLastLoginTimeLessThanOrEqual(Date lastLoginTimeLessThanOrEqual) {
		this.lastLoginTimeLessThanOrEqual = lastLoginTimeLessThanOrEqual;
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

	public void setNamePinyinLike(String namePinyinLike) {
		this.namePinyinLike = namePinyinLike;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public void setOrganizationCodeLike(String organizationCodeLike) {
		this.organizationCodeLike = organizationCodeLike;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public void setOrganizationIds(List<Long> organizationIds) {
		this.organizationIds = organizationIds;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public void setOrganizationNameLike(String organizationNameLike) {
		this.organizationNameLike = organizationNameLike;
	}

	public void setOrganizationNameOrCode(String organizationNameOrCode) {
		this.organizationNameOrCode = organizationNameOrCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public void setRoleCodes(List<String> roleCodes) {
		this.roleCodes = roleCodes;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public void setSearchWord(String searchWord) {
		this.searchWord = searchWord;
	}

	public void setSyncFlag(int syncFlag) {
		this.syncFlag = syncFlag;
	}

	public void setSyncOperatorType(String syncOperatorType) {
		this.syncOperatorType = syncOperatorType;
	}

	public void setSysOrganizationId(Long sysOrganizationId) {
		this.sysOrganizationId = sysOrganizationId;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public void setTelephoneLike(String telephoneLike) {
		this.telephoneLike = telephoneLike;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setUserIds(List<String> userIds) {
		this.userIds = userIds;
	}

	public void setUserIdsNotIn(List<String> userIdsNotIn) {
		this.userIdsNotIn = userIdsNotIn;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public SysUserQuery telephone(String telephone) {
		if (telephone == null) {
			throw new RuntimeException("telephone is null");
		}
		this.telephone = telephone;
		return this;
	}

	public SysUserQuery telephoneLike(String telephoneLike) {
		if (telephoneLike == null) {
			throw new RuntimeException("telephone is null");
		}
		this.telephoneLike = telephoneLike;
		return this;
	}

	public SysUserQuery tenantId(String tenantId) {
		if (tenantId == null) {
			throw new RuntimeException("tenantId is null");
		}
		this.tenantId = tenantId;
		return this;
	}

	public SysUserQuery userType(int userType) {
		this.userType = userType;
		return this;
	}

}