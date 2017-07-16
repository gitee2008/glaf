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

public class SysTenantQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected String name;
	protected String nameLike;
	protected String code;
	protected String codeLike;
	protected List<String> tenantIds;
	protected Integer tenantType;
	protected Long databaseId;
	protected List<Long> databaseIds;
	protected String province;
	protected Long provinceId;
	protected String city;
	protected Long cityId;
	protected String area;
	protected Long areaId;
	protected String town;
	protected Long townId;
	protected String addressLike;
	protected String property;
	protected String verify;
	protected Integer level;
	protected Integer levelGreaterThanOrEqual;
	protected Integer levelLessThanOrEqual;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;
	protected Date updateTimeGreaterThanOrEqual;
	protected Date updateTimeLessThanOrEqual;

	public SysTenantQuery() {

	}

	public SysTenantQuery addressLike(String addressLike) {
		if (addressLike == null) {
			throw new RuntimeException("address is null");
		}
		this.addressLike = addressLike;
		return this;
	}

	public SysTenantQuery area(String area) {
		if (area == null) {
			throw new RuntimeException("area is null");
		}
		this.area = area;
		return this;
	}

	public SysTenantQuery areaId(Long areaId) {
		if (areaId == null) {
			throw new RuntimeException("areaId is null");
		}
		this.areaId = areaId;
		return this;
	}

	public SysTenantQuery city(String city) {
		if (city == null) {
			throw new RuntimeException("city is null");
		}
		this.city = city;
		return this;
	}

	public SysTenantQuery cityId(Long cityId) {
		if (cityId == null) {
			throw new RuntimeException("cityId is null");
		}
		this.cityId = cityId;
		return this;
	}

	public SysTenantQuery code(String code) {
		if (code == null) {
			throw new RuntimeException("code is null");
		}
		this.code = code;
		return this;
	}

	public SysTenantQuery codeLike(String codeLike) {
		if (codeLike == null) {
			throw new RuntimeException("code is null");
		}
		this.codeLike = codeLike;
		return this;
	}

	public SysTenantQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public SysTenantQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public SysTenantQuery databaseId(Long databaseId) {
		if (databaseId == null) {
			throw new RuntimeException("databaseId is null");
		}
		this.databaseId = databaseId;
		return this;
	}

	public SysTenantQuery databaseIds(List<Long> databaseIds) {
		if (databaseIds == null) {
			throw new RuntimeException("databaseIds is empty ");
		}
		this.databaseIds = databaseIds;
		return this;
	}

	public String getAddressLike() {
		if (addressLike != null && addressLike.trim().length() > 0) {
			if (!addressLike.startsWith("%")) {
				addressLike = "%" + addressLike;
			}
			if (!addressLike.endsWith("%")) {
				addressLike = addressLike + "%";
			}
		}
		return addressLike;
	}

	public String getArea() {
		return area;
	}

	public Long getAreaId() {
		return areaId;
	}

	public String getCity() {
		return city;
	}

	public Long getCityId() {
		return cityId;
	}

	public String getCode() {
		return code;
	}

	public String getCodeLike() {
		if (codeLike != null && codeLike.trim().length() > 0) {
			if (!codeLike.startsWith("%")) {
				codeLike = "%" + codeLike;
			}
			if (!codeLike.endsWith("%")) {
				codeLike = codeLike + "%";
			}
		}
		return codeLike;
	}

	public Date getCreateTimeGreaterThanOrEqual() {
		return createTimeGreaterThanOrEqual;
	}

	public Date getCreateTimeLessThanOrEqual() {
		return createTimeLessThanOrEqual;
	}

	public Long getDatabaseId() {
		return databaseId;
	}

	public List<Long> getDatabaseIds() {
		return databaseIds;
	}

	public Integer getLevel() {
		return level;
	}

	public Integer getLevelGreaterThanOrEqual() {
		return levelGreaterThanOrEqual;
	}

	public Integer getLevelLessThanOrEqual() {
		return levelLessThanOrEqual;
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

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("name".equals(sortColumn)) {
				orderBy = "E.NAME_" + a_x;
			}

			if ("code".equals(sortColumn)) {
				orderBy = "E.CODE_" + a_x;
			}

			if ("tenantId".equals(sortColumn)) {
				orderBy = "E.TENANTID_" + a_x;
			}

			if ("tenantType".equals(sortColumn)) {
				orderBy = "E.TENANTTYPE_" + a_x;
			}

			if ("databaseId".equals(sortColumn)) {
				orderBy = "E.DATABASEID_" + a_x;
			}

			if ("principal".equals(sortColumn)) {
				orderBy = "E.PRINCIPAL_" + a_x;
			}

			if ("telephone".equals(sortColumn)) {
				orderBy = "E.TELEPHONE_" + a_x;
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

			if ("updateBy".equals(sortColumn)) {
				orderBy = "E.UPDATEBY_" + a_x;
			}

			if ("updateTime".equals(sortColumn)) {
				orderBy = "E.UPDATETIME_" + a_x;
			}

		}
		return orderBy;
	}

	public String getProperty() {
		return property;
	}

	public String getProvince() {
		return province;
	}

	public Long getProvinceId() {
		return provinceId;
	}

	public String getTenantId() {
		return tenantId;
	}

	public List<String> getTenantIds() {
		return tenantIds;
	}

	public Integer getTenantType() {
		return tenantType;
	}

	public String getTown() {
		return town;
	}

	public Long getTownId() {
		return townId;
	}

	public Date getUpdateTimeGreaterThanOrEqual() {
		return updateTimeGreaterThanOrEqual;
	}

	public Date getUpdateTimeLessThanOrEqual() {
		return updateTimeLessThanOrEqual;
	}

	public String getVerify() {
		return verify;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("name", "NAME_");
		addColumn("code", "CODE_");
		addColumn("tenantId", "TENANTID_");
		addColumn("tenantType", "TENANTTYPE_");
		addColumn("databaseId", "DATABASEID_");
		addColumn("principal", "PRINCIPAL_");
		addColumn("telephone", "TELEPHONE_");
		addColumn("locked", "LOCKED_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
		addColumn("updateBy", "UPDATEBY_");
		addColumn("updateTime", "UPDATETIME_");
	}

	public SysTenantQuery level(Integer level) {
		if (level == null) {
			throw new RuntimeException("level is null");
		}
		this.level = level;
		return this;
	}

	public SysTenantQuery levelGreaterThanOrEqual(Integer levelGreaterThanOrEqual) {
		if (levelGreaterThanOrEqual == null) {
			throw new RuntimeException("level is null");
		}
		this.levelGreaterThanOrEqual = levelGreaterThanOrEqual;
		return this;
	}

	public SysTenantQuery levelLessThanOrEqual(Integer levelLessThanOrEqual) {
		if (levelLessThanOrEqual == null) {
			throw new RuntimeException("level is null");
		}
		this.levelLessThanOrEqual = levelLessThanOrEqual;
		return this;
	}

	public SysTenantQuery name(String name) {
		if (name == null) {
			throw new RuntimeException("name is null");
		}
		this.name = name;
		return this;
	}

	public SysTenantQuery nameLike(String nameLike) {
		if (nameLike == null) {
			throw new RuntimeException("name is null");
		}
		this.nameLike = nameLike;
		return this;
	}

	public SysTenantQuery property(String property) {
		if (property == null) {
			throw new RuntimeException("property is null");
		}
		this.property = property;
		return this;
	}

	public SysTenantQuery province(String province) {
		if (province == null) {
			throw new RuntimeException("province is null");
		}
		this.province = province;
		return this;
	}

	public SysTenantQuery provinceId(Long provinceId) {
		if (provinceId == null) {
			throw new RuntimeException("provinceId is null");
		}
		this.provinceId = provinceId;
		return this;
	}

	public void setAddressLike(String addressLike) {
		this.addressLike = addressLike;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setCodeLike(String codeLike) {
		this.codeLike = codeLike;
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setDatabaseId(Long databaseId) {
		this.databaseId = databaseId;
	}

	public void setDatabaseIds(List<Long> databaseIds) {
		this.databaseIds = databaseIds;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public void setLevelGreaterThanOrEqual(Integer levelGreaterThanOrEqual) {
		this.levelGreaterThanOrEqual = levelGreaterThanOrEqual;
	}

	public void setLevelLessThanOrEqual(Integer levelLessThanOrEqual) {
		this.levelLessThanOrEqual = levelLessThanOrEqual;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNameLike(String nameLike) {
		this.nameLike = nameLike;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setTenantIds(List<String> tenantIds) {
		this.tenantIds = tenantIds;
	}

	public void setTenantType(Integer tenantType) {
		this.tenantType = tenantType;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public void setTownId(Long townId) {
		this.townId = townId;
	}

	public void setUpdateTimeGreaterThanOrEqual(Date updateTimeGreaterThanOrEqual) {
		this.updateTimeGreaterThanOrEqual = updateTimeGreaterThanOrEqual;
	}

	public void setUpdateTimeLessThanOrEqual(Date updateTimeLessThanOrEqual) {
		this.updateTimeLessThanOrEqual = updateTimeLessThanOrEqual;
	}

	public void setVerify(String verify) {
		this.verify = verify;
	}

	public SysTenantQuery tenantId(String tenantId) {
		if (tenantId == null) {
			throw new RuntimeException("tenantId is null");
		}
		this.tenantId = tenantId;
		return this;
	}

	public SysTenantQuery tenantIds(List<String> tenantIds) {
		if (tenantIds == null) {
			throw new RuntimeException("tenantIds is empty ");
		}
		this.tenantIds = tenantIds;
		return this;
	}

	public SysTenantQuery tenantType(Integer tenantType) {
		if (tenantType == null) {
			throw new RuntimeException("tenantType is null");
		}
		this.tenantType = tenantType;
		return this;
	}

	public SysTenantQuery town(String town) {
		if (town == null) {
			throw new RuntimeException("town is null");
		}
		this.town = town;
		return this;
	}

	public SysTenantQuery townId(Long townId) {
		if (townId == null) {
			throw new RuntimeException("townId is null");
		}
		this.townId = townId;
		return this;
	}

	public SysTenantQuery updateTimeGreaterThanOrEqual(Date updateTimeGreaterThanOrEqual) {
		if (updateTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("updateTime is null");
		}
		this.updateTimeGreaterThanOrEqual = updateTimeGreaterThanOrEqual;
		return this;
	}

	public SysTenantQuery updateTimeLessThanOrEqual(Date updateTimeLessThanOrEqual) {
		if (updateTimeLessThanOrEqual == null) {
			throw new RuntimeException("updateTime is null");
		}
		this.updateTimeLessThanOrEqual = updateTimeLessThanOrEqual;
		return this;
	}

	public SysTenantQuery verify(String verify) {
		if (verify == null) {
			throw new RuntimeException("verify is null");
		}
		this.verify = verify;
		return this;
	}

}