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
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.base.modules.sys.SysConstants;
import com.glaf.base.modules.sys.util.SysUserJsonFactory;
import com.glaf.core.base.JSONable;
import com.glaf.core.identity.User;
import com.glaf.core.util.RequestUtils;

@Entity
@Table(name = "SYS_USER")
public class SysUser implements Serializable, User, JSONable {
	private static final long serialVersionUID = -7677600372139823989L;

	/**
	 * 用户名
	 */
	@Id
	@Column(name = "USERID", length = 50)
	protected String userId;

	/**
	 * 姓名
	 */
	@Column(name = "USERNAME", length = 50)
	protected String name;

	/**
	 * 名称拼音
	 */
	@Column(name = "NAMEPINYIN", length = 50)
	protected String namePinyin;

	/**
	 * 账号类型
	 */
	@Column(name = "ACCOUNTTYPE")
	protected int accountType;

	/**
	 * 管理员标识
	 */
	@Column(name = "ISSYSTEM", length = 10)
	protected String adminFlag;

	/**
	 * 机构编号
	 */
	@Column(name = "ORGANIZATIONID")
	protected long organizationId;

	/**
	 * 邮件
	 */
	@Column(name = "EMAIL", length = 200)
	protected String email;

	/**
	 * 出差
	 */
	@Column(name = "EVECTION")
	protected int evection;

	/**
	 * 传真
	 */
	@Column(name = "FAX", length = 50)
	protected String fax;

	/**
	 * 性别：默认男性为1，女性为0
	 */
	@Column(name = "SEX")
	protected int sex;

	/**
	 * 职务
	 */
	@Column(name = "HEADSHIP", length = 200)
	protected String headship;

	/**
	 * 最后登录IP
	 */
	@Column(name = "LASTLOGINIP", length = 200)
	protected String lastLoginIP;

	/**
	 * 最后登录时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LASTLOGINTIME")
	protected Date lastLoginTime;

	/**
	 * 锁定登录时间，密码输入次数超过最大重试次数后设定
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LOCKLOGINTIME")
	protected Date lockLoginTime;

	/**
	 * 登录重试次数
	 */
	@Column(name = "LOGINRETRY")
	protected int loginRetry;

	/**
	 * 手机
	 */
	@Column(name = "MOBILE", length = 50)
	protected String mobile;

	/**
	 * 手机验证标识
	 */
	@Column(name = "MOBILEVERIFYFLAG_", length = 1)
	protected String mobileVerifyFlag;

	/**
	 * Hash密码
	 */
	@Column(name = "PASSWORD_HASH", length = 250)
	protected String passwordHash;

	/**
	 * 电话
	 */
	@Column(name = "TELEPHONE", length = 50)
	protected String telephone;

	/**
	 * 用户类别
	 */
	@Column(name = "USERTYPE")
	protected int userType;

	/**
	 * 用户所属租户
	 */
	@Column(name = "TENANTID")
	protected String tenantId;

	/**
	 * 是否锁定
	 */
	@Column(name = "LOCKED")
	protected int locked;

	/**
	 * Token
	 */
	@Column(name = "TOKEN", length = 250)
	protected String token;

	/**
	 * TOKEN创建时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TOKENTIME")
	protected Date tokenTime;

	/**
	 * 用户同步标记
	 */
	@Column(name = "SYNCFLAG", columnDefinition = "int default 0")
	protected int syncFlag;

	/**
	 * 用户同步时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SYNCREATETIME")
	protected Date syncTime;

	/**
	 * 同步类型 用于判断该用户是新增、修改或删除等操作
	 */
	@Column(name = "SYNCOPERATORTYPE", length = 50)
	protected String syncOperatorType;

	/**
	 * 第三方密锁登录标记
	 */
	@Column(name = "SECRETLOGINFLAG_", length = 1)
	protected String secretLoginFlag;

	/**
	 * 登录密锁
	 */
	@Column(name = "LOGINSECRET_", length = 200)
	protected String loginSecret;

	/**
	 * 登录密锁更新时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LOGINSECRETUPDATETIME_")
	protected Date loginSecretUpdateTime;

	/**
	 * 创建人
	 */
	@Column(name = "CREATEBY", length = 50)
	protected String createBy;

	/**
	 * 创建时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME")
	protected Date createTime;

	/**
	 * 删除标记
	 */
	@Column(name = "DELETEFLAG")
	protected int deleteFlag;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DELETETIME")
	protected Date deleteTime;

	/**
	 * 修改人
	 */
	@Column(name = "UPDATEBY", length = 50)
	protected String updateBy;

	/**
	 * 修改日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATEDATE")
	protected Date updateDate;

	@javax.persistence.Transient
	protected String attribute;

	@javax.persistence.Transient
	private Collection<SysRole> roles = new HashSet<SysRole>();

	@javax.persistence.Transient
	private Set<SysUserRole> userRoles = new HashSet<SysUserRole>();

	@javax.persistence.Transient
	private Set<String> roleCodes = new HashSet<String>();

	@javax.persistence.Transient
	private Collection<String> rowKeys = new HashSet<String>();

	@javax.persistence.Transient
	private Collection<Object> objectIds = new HashSet<Object>();

	public SysUser() {

	}

	public void addObjectId(Object rowId) {
		if (objectIds == null) {
			objectIds = new HashSet<Object>();
		}
		objectIds.add(rowId);
	}

	public void addRowKey(String rowKey) {
		if (rowKeys == null) {
			rowKeys = new HashSet<String>();
		}
		rowKeys.add(rowKey);
	}

	public int getAccountType() {
		return accountType;
	}

	@Override
	public String getActorId() {
		return userId;
	}

	public String getAdminFlag() {
		return adminFlag;
	}

	public String getAttribute() {
		return attribute;
	}

	public String getCreateBy() {
		return createBy;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public int getDeleteFlag() {
		return deleteFlag;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}

	public String getEmail() {
		return email;
	}

	public String getEncodeActorId() {
		return RequestUtils.encodeString(this.getActorId());
	}

	public int getEvection() {
		return evection;
	}

	public String getFax() {
		return fax;
	}

	public String getHeadship() {
		return headship;
	}

	public long getId() {
		return (SysConstants.TEN_BILLION - userId.hashCode());
	}

	public Date getLastLoginDate() {
		return lastLoginTime;
	}

	public String getLastLoginIP() {
		return lastLoginIP;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public int getLocked() {
		return locked;
	}

	public Date getLockLoginTime() {
		return lockLoginTime;
	}

	public String getLoginIP() {
		return lastLoginIP;
	}

	public int getLoginRetry() {
		return loginRetry;
	}

	public String getLoginSecret() {
		return loginSecret;
	}

	public Date getLoginSecretUpdateTime() {
		return loginSecretUpdateTime;
	}

	public String getMail() {
		return email;
	}

	public String getMobile() {
		return mobile;
	}

	public String getMobileVerifyFlag() {
		return mobileVerifyFlag;
	}

	public String getName() {
		return name;
	}

	public String getNamePinyin() {
		return namePinyin;
	}

	public Collection<Object> getObjectIds() {
		return objectIds;
	}

	public long getOrganizationId() {
		return organizationId;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public String getRemark() {
		return null;
	}

	public Set<String> getRoleCodes() {
		if (roleCodes == null) {
			roleCodes = new HashSet<String>();
		}
		return roleCodes;
	}

	public Collection<SysRole> getRoles() {
		if (roles == null) {
			roles = new HashSet<SysRole>();
		}
		return roles;
	}

	public Collection<String> getRowKeys() {
		return rowKeys;
	}

	public String getSecretLoginFlag() {
		return secretLoginFlag;
	}

	public int getSex() {
		return sex;
	}

	public int getSyncFlag() {
		return syncFlag;
	}

	public String getSyncOperatorType() {
		return syncOperatorType;
	}

	public Date getSyncTime() {
		return syncTime;
	}

	public String getTelephone() {
		return telephone;
	}

	public String getTenantId() {
		return tenantId;
	}

	public String getToken() {
		return token;
	}

	public Date getTokenTime() {
		return tokenTime;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public String getUserId() {
		return userId;
	}

	public Set<SysUserRole> getUserRoles() {
		if (userRoles == null) {
			userRoles = new HashSet<SysUserRole>();
		}
		return userRoles;
	}

	public int getUserType() {
		return userType;
	}

	public boolean isDepartmentAdmin() {
		boolean isOrganizationAdmin = false;

		if (roles != null && !roles.isEmpty()) {
			for (SysRole r : roles) {
				if ("DepartmentManager".equals(r.getCode())) {
					isOrganizationAdmin = true;
					break;
				}
			}
		}

		if (!isOrganizationAdmin) {
			if (userRoles != null && !userRoles.isEmpty()) {
				for (SysUserRole r : userRoles) {
					if (r.getRole() != null) {
						if ("DepartmentManager".equals(r.getRole().getCode())) {
							isOrganizationAdmin = true;
							break;
						}
					}
				}
			}
		}
		return isOrganizationAdmin;
	}

	public boolean isSystemAdministrator() {
		boolean isAdmin = false;

		if (StringUtils.equals("admin", userId)) {
			isAdmin = true;
		}

		return isAdmin;
	}

	public boolean isTenantAdmin() {
		boolean isTenantAdmin = false;

		if (roles != null && !roles.isEmpty()) {
			for (SysRole r : roles) {
				if ("TenantAdmin".equals(r.getCode())) {
					isTenantAdmin = true;
					break;
				}
			}
		}

		if (!isTenantAdmin) {
			if (userRoles != null && !userRoles.isEmpty()) {
				for (SysUserRole r : userRoles) {
					if (r.getRole() != null) {
						if ("TenantAdmin".equals(r.getRole().getCode())) {
							isTenantAdmin = true;
							break;
						}
					}
				}
			}
		}
		return isTenantAdmin;
	}

	public SysUser jsonToObject(JSONObject jsonObject) {
		return SysUserJsonFactory.jsonToObject(jsonObject);
	}

	public void setAccountType(int accountType) {
		this.accountType = accountType;
	}

	public void setActorId(String actorId) {
		this.userId = actorId;
	}

	public void setAdminFlag(String adminFlag) {
		this.adminFlag = adminFlag;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateDate(Date createDate) {
		this.createTime = createDate;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setEvection(int evection) {
		this.evection = evection;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public void setHeadship(String headship) {
		this.headship = headship;
	}

	public void setId(long id) {

	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginTime = lastLoginDate;
	}

	public void setLastLoginIP(String lastLoginIP) {
		this.lastLoginIP = lastLoginIP;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public void setLocked(int locked) {
		this.locked = locked;
	}

	public void setLockLoginTime(Date lockLoginTime) {
		this.lockLoginTime = lockLoginTime;
	}

	public void setLoginIP(String loginIP) {
		this.lastLoginIP = loginIP;
	}

	public void setLoginRetry(int loginRetry) {
		this.loginRetry = loginRetry;
	}

	public void setLoginSecret(String loginSecret) {
		this.loginSecret = loginSecret;
	}

	public void setLoginSecretUpdateTime(Date loginSecretUpdateTime) {
		this.loginSecretUpdateTime = loginSecretUpdateTime;
	}

	public void setMail(String mail) {
		this.email = mail;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setMobileVerifyFlag(String mobileVerifyFlag) {
		this.mobileVerifyFlag = mobileVerifyFlag;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNamePinyin(String namePinyin) {
		this.namePinyin = namePinyin;
	}

	public void setObjectIds(Collection<Object> objectIds) {
		this.objectIds = objectIds;
	}

	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public void setRemark(String remark) {

	}

	public void setRoleCodes(Set<String> roleCodes) {
		this.roleCodes = roleCodes;
	}

	public void setRoles(Collection<SysRole> roles) {
		this.roles = roles;
	}

	public void setRowKeys(Collection<String> rowKeys) {
		this.rowKeys = rowKeys;
	}

	public void setSecretLoginFlag(String secretLoginFlag) {
		this.secretLoginFlag = secretLoginFlag;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public void setSyncFlag(int syncFlag) {
		this.syncFlag = syncFlag;
	}

	public void setSyncOperatorType(String syncOperatorType) {
		this.syncOperatorType = syncOperatorType;
	}

	public void setSyncTime(Date syncTime) {
		this.syncTime = syncTime;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setTokenTime(Date tokenTime) {
		this.tokenTime = tokenTime;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setUserRoles(Set<SysUserRole> userRoles) {
		this.userRoles = userRoles;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public JSONObject toJsonObject() {
		return SysUserJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return SysUserJsonFactory.toObjectNode(this);
	}

	public String toString() {
		return toJsonObject().toJSONString();
	}

}