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

import java.io.*;
import java.util.*;
import javax.persistence.*;
import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.web.multipart.MultipartFile;

import com.glaf.core.base.*;
import com.glaf.core.identity.Tenant;
import com.glaf.core.util.DateUtils;
import com.glaf.base.modules.sys.util.*;

/**
 * 实体对象
 */

@Entity
@Table(name = "SYS_TENANT")
public class SysTenant implements Serializable, JSONable, Tenant {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", nullable = false)
	protected long id;

	/**
	 * 名称
	 */
	@Column(name = "NAME_", length = 200)
	protected String name;

	/**
	 * 名称拼音
	 */
	@Column(name = "NAMEPINYIN_", length = 200)
	protected String namePinyin;

	/**
	 * 代码
	 */
	@Column(name = "CODE_", length = 50)
	protected String code;

	/**
	 * 租户编号
	 */
	@Column(name = "TENANTID_", length = 50)
	protected String tenantId;

	/**
	 * 租户类型
	 */
	@Column(name = "TENANTTYPE_")
	protected int tenantType;

	/**
	 * 数据库编号
	 */
	@Column(name = "DATABASEID_")
	protected long databaseId;

	/**
	 * 等级
	 */
	@Column(name = "LEVEL_")
	protected int level;

	/**
	 * 省/直辖市
	 */
	@Column(name = "PROVINCE_", length = 100)
	protected String province;

	/**
	 * 省/直辖市编号
	 */
	@Column(name = "PROVINCEID_")
	protected long provinceId;

	/**
	 * 市
	 */
	@Column(name = "CITY_", length = 100)
	protected String city;

	/**
	 * 市编号
	 */
	@Column(name = "CITYID_")
	protected long cityId;

	/**
	 * 区/县
	 */
	@Column(name = "AREA_", length = 100)
	protected String area;

	/**
	 * 区/县编号
	 */
	@Column(name = "AREAID_")
	protected long areaId;

	/**
	 * 镇/街道
	 */
	@Column(name = "TOWN_", length = 200)
	protected String town;

	/**
	 * 镇/街道编号
	 */
	@Column(name = "TOWNID_")
	protected long townId;

	/**
	 * 性质
	 */
	@Column(name = "PROPERTY_", length = 50)
	protected String property;

	/**
	 * 地址
	 */
	@Column(name = "ADDRESS_", length = 200)
	protected String address;

	/**
	 * 认证标识
	 */
	@Column(name = "VERIFY_", length = 1)
	protected String verify;

	/**
	 * 负责人
	 */
	@Column(name = "PRINCIPAL_", length = 250)
	protected String principal;

	/**
	 * 电话
	 */
	@Column(name = "TELEPHONE_", length = 200)
	protected String telephone;

	/**
	 * 票据标识
	 */
	@Column(name = "TICKETFLAG_", length = 1)
	protected String ticketFlag;

	/**
	 * 类型
	 */
	@Column(name = "TYPE_", length = 50)
	protected String type;

	/**
	 * 是否限制
	 */
	@Column(name = "LIMIT_")
	protected int limit;

	/**
	 * 是否锁定
	 */
	@Column(name = "LOCKED_")
	protected int locked;

	/**
	 * 数据约束
	 */
	@Column(name = "DISABLEDATACONSTRAINT_", length = 1)
	protected String disableDataConstraint;

	/**
	 * 创建人
	 */
	@Column(name = "CREATEBY_", length = 50)
	protected String createBy;

	/**
	 * 创建日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME_")
	protected Date createTime;

	/**
	 * 修改人
	 */
	@Column(name = "UPDATEBY_", length = 50)
	protected String updateBy;

	/**
	 * 修改日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATETIME_")
	protected Date updateTime;

	@javax.persistence.Transient
	protected MultipartFile file;

	@javax.persistence.Transient
	protected boolean isSystemAdministrator;

	public SysTenant() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SysTenant other = (SysTenant) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public String getAddress() {
		return address;
	}

	public String getArea() {
		return area;
	}

	public long getAreaId() {
		return areaId;
	}

	public String getCity() {
		return city;
	}

	public long getCityId() {
		return cityId;
	}

	public String getCode() {
		return this.code;
	}

	public String getCreateBy() {
		return this.createBy;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public String getCreateTimeString() {
		if (this.createTime != null) {
			return DateUtils.getDateTime(this.createTime);
		}
		return "";
	}

	public long getDatabaseId() {
		return this.databaseId;
	}

	public String getDisableDataConstraint() {
		return disableDataConstraint;
	}

	public MultipartFile getFile() {
		return file;
	}

	public long getId() {
		return this.id;
	}

	public int getLevel() {
		return level;
	}

	public int getLimit() {
		return limit;
	}

	public int getLocked() {
		return this.locked;
	}

	public String getName() {
		return this.name;
	}

	public String getNamePinyin() {
		return namePinyin;
	}

	public String getPrincipal() {
		return this.principal;
	}

	public String getProperty() {
		return property;
	}

	public String getProvince() {
		return province;
	}

	public long getProvinceId() {
		return provinceId;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public String getTenantId() {
		return this.tenantId;
	}

	public int getTenantType() {
		return this.tenantType;
	}

	public String getTicketFlag() {
		return ticketFlag;
	}

	public String getTown() {
		return town;
	}

	public long getTownId() {
		return townId;
	}

	public String getType() {
		return type;
	}

	public String getUpdateBy() {
		return this.updateBy;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public String getUpdateTimeString() {
		if (this.updateTime != null) {
			return DateUtils.getDateTime(this.updateTime);
		}
		return "";
	}

	public String getVerify() {
		return verify;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public boolean isSystemAdministrator() {
		return isSystemAdministrator;
	}

	public SysTenant jsonToObject(JSONObject jsonObject) {
		return SysTenantJsonFactory.jsonToObject(jsonObject);
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setDatabaseId(long databaseId) {
		this.databaseId = databaseId;
	}

	public void setDisableDataConstraint(String disableDataConstraint) {
		this.disableDataConstraint = disableDataConstraint;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public void setLocked(int locked) {
		this.locked = locked;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNamePinyin(String namePinyin) {
		this.namePinyin = namePinyin;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public void setProvinceId(long provinceId) {
		this.provinceId = provinceId;
	}

	public void setSystemAdministrator(boolean isSystemAdministrator) {
		this.isSystemAdministrator = isSystemAdministrator;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setTenantType(int tenantType) {
		this.tenantType = tenantType;
	}

	public void setTicketFlag(String ticketFlag) {
		this.ticketFlag = ticketFlag;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public void setTownId(long townId) {
		this.townId = townId;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public void setVerify(String verify) {
		this.verify = verify;
	}

	public JSONObject toJsonObject() {
		return SysTenantJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return SysTenantJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
