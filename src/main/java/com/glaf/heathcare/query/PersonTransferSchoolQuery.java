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

package com.glaf.heathcare.query;

import java.util.*;
import com.glaf.core.query.DataQuery;

public class PersonTransferSchoolQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected List<String> tenantIds;
	protected String fromTenantId;
	protected String fromSchoolLike;
	protected String toTenantId;
	protected String toSchoolLike;
	protected String personId;
	protected List<String> personIds;
	protected String name;
	protected String nameLike;
	protected Date checkDateGreaterThanOrEqual;
	protected Date checkDateLessThanOrEqual;
	protected String remarkLike;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	public PersonTransferSchoolQuery() {

	}

	public PersonTransferSchoolQuery checkDateGreaterThanOrEqual(Date checkDateGreaterThanOrEqual) {
		if (checkDateGreaterThanOrEqual == null) {
			throw new RuntimeException("checkDate is null");
		}
		this.checkDateGreaterThanOrEqual = checkDateGreaterThanOrEqual;
		return this;
	}

	public PersonTransferSchoolQuery checkDateLessThanOrEqual(Date checkDateLessThanOrEqual) {
		if (checkDateLessThanOrEqual == null) {
			throw new RuntimeException("checkDate is null");
		}
		this.checkDateLessThanOrEqual = checkDateLessThanOrEqual;
		return this;
	}

	public PersonTransferSchoolQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public PersonTransferSchoolQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public PersonTransferSchoolQuery fromSchoolLike(String fromSchoolLike) {
		if (fromSchoolLike == null) {
			throw new RuntimeException("fromSchool is null");
		}
		this.fromSchoolLike = fromSchoolLike;
		return this;
	}

	public PersonTransferSchoolQuery fromTenantId(String fromTenantId) {
		if (fromTenantId == null) {
			throw new RuntimeException("fromTenantId is null");
		}
		this.fromTenantId = fromTenantId;
		return this;
	}

	public Date getCheckDateGreaterThanOrEqual() {
		return checkDateGreaterThanOrEqual;
	}

	public Date getCheckDateLessThanOrEqual() {
		return checkDateLessThanOrEqual;
	}

	public Date getCreateTimeGreaterThanOrEqual() {
		return createTimeGreaterThanOrEqual;
	}

	public Date getCreateTimeLessThanOrEqual() {
		return createTimeLessThanOrEqual;
	}

	public String getFromSchoolLike() {
		if (fromSchoolLike != null && fromSchoolLike.trim().length() > 0) {
			if (!fromSchoolLike.startsWith("%")) {
				fromSchoolLike = "%" + fromSchoolLike;
			}
			if (!fromSchoolLike.endsWith("%")) {
				fromSchoolLike = fromSchoolLike + "%";
			}
		}
		return fromSchoolLike;
	}

	public String getFromTenantId() {
		return fromTenantId;
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

			if ("tenantId".equals(sortColumn)) {
				orderBy = "E.TENANTID_" + a_x;
			}

			if ("fromTenantId".equals(sortColumn)) {
				orderBy = "E.FROM_TENANTID_" + a_x;
			}

			if ("fromSchool".equals(sortColumn)) {
				orderBy = "E.FROM_SCHOOL_" + a_x;
			}

			if ("personId".equals(sortColumn)) {
				orderBy = "E.PERSONID_" + a_x;
			}

			if ("name".equals(sortColumn)) {
				orderBy = "E.NAME_" + a_x;
			}

			if ("sex".equals(sortColumn)) {
				orderBy = "E.SEX_" + a_x;
			}

			if ("checkDate".equals(sortColumn)) {
				orderBy = "E.CHECKDATE_" + a_x;
			}

			if ("checkOrganization".equals(sortColumn)) {
				orderBy = "E.CHECKORGANIZATION_" + a_x;
			}

			if ("checkOrganizationId".equals(sortColumn)) {
				orderBy = "E.CHECKORGANIZATIONID_" + a_x;
			}

			if ("checkResult".equals(sortColumn)) {
				orderBy = "E.CHECKRESULT_" + a_x;
			}

			if ("remark".equals(sortColumn)) {
				orderBy = "E.REMARK_" + a_x;
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

	public String getPersonId() {
		return personId;
	}

	public List<String> getPersonIds() {
		return personIds;
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

	public List<String> getTenantIds() {
		return tenantIds;
	}

	public String getToSchoolLike() {
		return toSchoolLike;
	}

	public String getToTenantId() {
		return toTenantId;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("tenantId", "TENANTID_");
		addColumn("fromTenantId", "FROM_TENANTID_");
		addColumn("fromSchool", "FROM_SCHOOL_");
		addColumn("personId", "PERSONID_");
		addColumn("name", "NAME_");
		addColumn("sex", "SEX_");
		addColumn("checkDate", "CHECKDATE_");
		addColumn("checkOrganization", "CHECKORGANIZATION_");
		addColumn("checkOrganizationId", "CHECKORGANIZATIONID_");
		addColumn("checkResult", "CHECKRESULT_");
		addColumn("remark", "REMARK_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
	}

	public PersonTransferSchoolQuery name(String name) {
		if (name == null) {
			throw new RuntimeException("name is null");
		}
		this.name = name;
		return this;
	}

	public PersonTransferSchoolQuery nameLike(String nameLike) {
		if (nameLike == null) {
			throw new RuntimeException("name is null");
		}
		this.nameLike = nameLike;
		return this;
	}

	public PersonTransferSchoolQuery personId(String personId) {
		if (personId == null) {
			throw new RuntimeException("personId is null");
		}
		this.personId = personId;
		return this;
	}

	public PersonTransferSchoolQuery personIds(List<String> personIds) {
		if (personIds == null) {
			throw new RuntimeException("personIds is empty ");
		}
		this.personIds = personIds;
		return this;
	}

	public PersonTransferSchoolQuery remarkLike(String remarkLike) {
		if (remarkLike == null) {
			throw new RuntimeException("remark is null");
		}
		this.remarkLike = remarkLike;
		return this;
	}

	public void setCheckDateGreaterThanOrEqual(Date checkDateGreaterThanOrEqual) {
		this.checkDateGreaterThanOrEqual = checkDateGreaterThanOrEqual;
	}

	public void setCheckDateLessThanOrEqual(Date checkDateLessThanOrEqual) {
		this.checkDateLessThanOrEqual = checkDateLessThanOrEqual;
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setFromSchoolLike(String fromSchoolLike) {
		this.fromSchoolLike = fromSchoolLike;
	}

	public void setFromTenantId(String fromTenantId) {
		this.fromTenantId = fromTenantId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNameLike(String nameLike) {
		this.nameLike = nameLike;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public void setPersonIds(List<String> personIds) {
		this.personIds = personIds;
	}

	public void setRemarkLike(String remarkLike) {
		this.remarkLike = remarkLike;
	}

	public void setTenantIds(List<String> tenantIds) {
		this.tenantIds = tenantIds;
	}

	public void setToSchoolLike(String toSchoolLike) {
		this.toSchoolLike = toSchoolLike;
	}

	public void setToTenantId(String toTenantId) {
		this.toTenantId = toTenantId;
	}

	public PersonTransferSchoolQuery tenantId(String tenantId) {
		if (tenantId == null) {
			throw new RuntimeException("tenantId is null");
		}
		this.tenantId = tenantId;
		return this;
	}

	public PersonTransferSchoolQuery tenantIds(List<String> tenantIds) {
		if (tenantIds == null) {
			throw new RuntimeException("tenantIds is empty ");
		}
		this.tenantIds = tenantIds;
		return this;
	}

	public PersonTransferSchoolQuery toSchoolLike(String toSchoolLike) {
		if (toSchoolLike == null) {
			throw new RuntimeException("toSchool is null");
		}
		this.toSchoolLike = toSchoolLike;
		return this;
	}

	public PersonTransferSchoolQuery toTenantId(String toTenantId) {
		if (toTenantId == null) {
			throw new RuntimeException("toTenantId is null");
		}
		this.toTenantId = toTenantId;
		return this;
	}

}