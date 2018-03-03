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

public class PersonLinkmanQuery extends DataQuery {
	private static final long serialVersionUID = 1L;

	protected String personId;
	protected List<String> personIds;
	protected String name;
	protected String nameLike;
	protected String relationship;
	protected String relationshipLike;
	protected String company;
	protected String companyLike;
	protected String mobile;
	protected String mobileLike;
	protected String mail;
	protected String mailLike;
	protected String wardship;
	protected String remarkLike;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	public PersonLinkmanQuery() {

	}

	public PersonLinkmanQuery company(String company) {
		if (company == null) {
			throw new RuntimeException("company is null");
		}
		this.company = company;
		return this;
	}

	public PersonLinkmanQuery companyLike(String companyLike) {
		if (companyLike == null) {
			throw new RuntimeException("company is null");
		}
		this.companyLike = companyLike;
		return this;
	}

	public PersonLinkmanQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public PersonLinkmanQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public String getCompany() {
		return company;
	}

	public String getCompanyLike() {
		if (companyLike != null && companyLike.trim().length() > 0) {
			if (!companyLike.startsWith("%")) {
				companyLike = "%" + companyLike;
			}
			if (!companyLike.endsWith("%")) {
				companyLike = companyLike + "%";
			}
		}
		return companyLike;
	}

	public Date getCreateTimeGreaterThanOrEqual() {
		return createTimeGreaterThanOrEqual;
	}

	public Date getCreateTimeLessThanOrEqual() {
		return createTimeLessThanOrEqual;
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

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("personId".equals(sortColumn)) {
				orderBy = "E.PERSONID_" + a_x;
			}

			if ("name".equals(sortColumn)) {
				orderBy = "E.NAME_" + a_x;
			}

			if ("relationship".equals(sortColumn)) {
				orderBy = "E.RELATIONSHIP_" + a_x;
			}

			if ("company".equals(sortColumn)) {
				orderBy = "E.COMPANY_" + a_x;
			}

			if ("mobile".equals(sortColumn)) {
				orderBy = "E.MOBILE_" + a_x;
			}

			if ("mail".equals(sortColumn)) {
				orderBy = "E.MAIL_" + a_x;
			}

			if ("wardship".equals(sortColumn)) {
				orderBy = "E.WARDSHIP_" + a_x;
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

			if ("updateBy".equals(sortColumn)) {
				orderBy = "E.UPDATEBY_" + a_x;
			}

			if ("updateTime".equals(sortColumn)) {
				orderBy = "E.UPDATETIME_" + a_x;
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

	public String getRelationship() {
		return relationship;
	}

	public String getRelationshipLike() {
		if (relationshipLike != null && relationshipLike.trim().length() > 0) {
			if (!relationshipLike.startsWith("%")) {
				relationshipLike = "%" + relationshipLike;
			}
			if (!relationshipLike.endsWith("%")) {
				relationshipLike = relationshipLike + "%";
			}
		}
		return relationshipLike;
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

	public String getWardship() {
		return wardship;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("personId", "PERSONID_");
		addColumn("name", "NAME_");
		addColumn("relationship", "RELATIONSHIP_");
		addColumn("company", "COMPANY_");
		addColumn("mobile", "MOBILE_");
		addColumn("mail", "MAIL_");
		addColumn("wardship", "WARDSHIP_");
		addColumn("remark", "REMARK_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
		addColumn("updateBy", "UPDATEBY_");
		addColumn("updateTime", "UPDATETIME_");
	}

	public PersonLinkmanQuery mail(String mail) {
		if (mail == null) {
			throw new RuntimeException("mail is null");
		}
		this.mail = mail;
		return this;
	}

	public PersonLinkmanQuery mailLike(String mailLike) {
		if (mailLike == null) {
			throw new RuntimeException("mail is null");
		}
		this.mailLike = mailLike;
		return this;
	}

	public PersonLinkmanQuery mobile(String mobile) {
		if (mobile == null) {
			throw new RuntimeException("mobile is null");
		}
		this.mobile = mobile;
		return this;
	}

	public PersonLinkmanQuery mobileLike(String mobileLike) {
		if (mobileLike == null) {
			throw new RuntimeException("mobile is null");
		}
		this.mobileLike = mobileLike;
		return this;
	}

	public PersonLinkmanQuery name(String name) {
		if (name == null) {
			throw new RuntimeException("name is null");
		}
		this.name = name;
		return this;
	}

	public PersonLinkmanQuery nameLike(String nameLike) {
		if (nameLike == null) {
			throw new RuntimeException("name is null");
		}
		this.nameLike = nameLike;
		return this;
	}

	public PersonLinkmanQuery personId(String personId) {
		if (personId == null) {
			throw new RuntimeException("personId is null");
		}
		this.personId = personId;
		return this;
	}

	public PersonLinkmanQuery personIds(List<String> personIds) {
		if (personIds == null) {
			throw new RuntimeException("personIds is empty ");
		}
		this.personIds = personIds;
		return this;
	}

	public PersonLinkmanQuery relationship(String relationship) {
		if (relationship == null) {
			throw new RuntimeException("relationship is null");
		}
		this.relationship = relationship;
		return this;
	}

	public PersonLinkmanQuery relationshipLike(String relationshipLike) {
		if (relationshipLike == null) {
			throw new RuntimeException("relationship is null");
		}
		this.relationshipLike = relationshipLike;
		return this;
	}

	public PersonLinkmanQuery remarkLike(String remarkLike) {
		if (remarkLike == null) {
			throw new RuntimeException("remark is null");
		}
		this.remarkLike = remarkLike;
		return this;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public void setCompanyLike(String companyLike) {
		this.companyLike = companyLike;
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
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

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public void setPersonIds(List<String> personIds) {
		this.personIds = personIds;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	public void setRelationshipLike(String relationshipLike) {
		this.relationshipLike = relationshipLike;
	}

	public void setRemarkLike(String remarkLike) {
		this.remarkLike = remarkLike;
	}

	public void setWardship(String wardship) {
		this.wardship = wardship;
	}

	public PersonLinkmanQuery wardship(String wardship) {
		if (wardship == null) {
			throw new RuntimeException("wardship is null");
		}
		this.wardship = wardship;
		return this;
	}

}