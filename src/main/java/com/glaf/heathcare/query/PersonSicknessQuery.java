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

public class PersonSicknessQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected Collection<String> tenantIds;
	protected String gradeId;
	protected String gradeIdLike;
	protected Collection<String> gradeIds;
	protected String personId;
	protected String personIdLike;
	protected List<String> personIds;
	protected String name;
	protected String nameLike;
	protected List<String> names;
	protected String sickness;
	protected String sicknessLike;
	protected List<String> sicknesss;
	protected String infectiousFlag;
	protected String infectiousFlagLike;
	protected List<String> infectiousFlags;
	protected Date discoverTimeGreaterThanOrEqual;
	protected Date discoverTimeLessThanOrEqual;
	protected Date reportTimeGreaterThanOrEqual;
	protected Date reportTimeLessThanOrEqual;
	protected Date confirmTimeGreaterThanOrEqual;
	protected Date confirmTimeLessThanOrEqual;
	protected String receiver1;
	protected String receiver1Like;
	protected List<String> receiver1s;
	protected String receiveOrg1;
	protected String receiveOrg1Like;
	protected List<String> receiveOrg1s;
	protected String receiver2;
	protected String receiver2Like;
	protected List<String> receiver2s;
	protected String receiveOrg2;
	protected String receiveOrg2Like;
	protected List<String> receiveOrg2s;
	protected String treat;
	protected String treatLike;
	protected List<String> treats;
	protected String hospital;
	protected String hospitalLike;
	protected List<String> hospitals;
	protected Date clinicTimeGreaterThanOrEqual;
	protected Date clinicTimeLessThanOrEqual;
	protected String result;
	protected String resultLike;
	protected String remarkLike;
	protected Integer year;
	protected Integer month;
	protected String createByLike;
	protected List<String> createBys;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	public PersonSicknessQuery() {

	}

	public PersonSicknessQuery clinicTimeGreaterThanOrEqual(Date clinicTimeGreaterThanOrEqual) {
		if (clinicTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("clinicTime is null");
		}
		this.clinicTimeGreaterThanOrEqual = clinicTimeGreaterThanOrEqual;
		return this;
	}

	public PersonSicknessQuery clinicTimeLessThanOrEqual(Date clinicTimeLessThanOrEqual) {
		if (clinicTimeLessThanOrEqual == null) {
			throw new RuntimeException("clinicTime is null");
		}
		this.clinicTimeLessThanOrEqual = clinicTimeLessThanOrEqual;
		return this;
	}

	public PersonSicknessQuery confirmTimeGreaterThanOrEqual(Date confirmTimeGreaterThanOrEqual) {
		if (confirmTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("confirmTime is null");
		}
		this.confirmTimeGreaterThanOrEqual = confirmTimeGreaterThanOrEqual;
		return this;
	}

	public PersonSicknessQuery confirmTimeLessThanOrEqual(Date confirmTimeLessThanOrEqual) {
		if (confirmTimeLessThanOrEqual == null) {
			throw new RuntimeException("confirmTime is null");
		}
		this.confirmTimeLessThanOrEqual = confirmTimeLessThanOrEqual;
		return this;
	}

	public PersonSicknessQuery createByLike(String createByLike) {
		if (createByLike == null) {
			throw new RuntimeException("createBy is null");
		}
		this.createByLike = createByLike;
		return this;
	}

	public PersonSicknessQuery createBys(List<String> createBys) {
		if (createBys == null) {
			throw new RuntimeException("createBys is empty ");
		}
		this.createBys = createBys;
		return this;
	}

	public PersonSicknessQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public PersonSicknessQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public PersonSicknessQuery discoverTimeGreaterThanOrEqual(Date discoverTimeGreaterThanOrEqual) {
		if (discoverTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("discoverTime is null");
		}
		this.discoverTimeGreaterThanOrEqual = discoverTimeGreaterThanOrEqual;
		return this;
	}

	public PersonSicknessQuery discoverTimeLessThanOrEqual(Date discoverTimeLessThanOrEqual) {
		if (discoverTimeLessThanOrEqual == null) {
			throw new RuntimeException("discoverTime is null");
		}
		this.discoverTimeLessThanOrEqual = discoverTimeLessThanOrEqual;
		return this;
	}

	public Date getClinicTimeGreaterThanOrEqual() {
		return clinicTimeGreaterThanOrEqual;
	}

	public Date getClinicTimeLessThanOrEqual() {
		return clinicTimeLessThanOrEqual;
	}

	public Date getConfirmTimeGreaterThanOrEqual() {
		return confirmTimeGreaterThanOrEqual;
	}

	public Date getConfirmTimeLessThanOrEqual() {
		return confirmTimeLessThanOrEqual;
	}

	public String getCreateByLike() {
		if (createByLike != null && createByLike.trim().length() > 0) {
			if (!createByLike.startsWith("%")) {
				createByLike = "%" + createByLike;
			}
			if (!createByLike.endsWith("%")) {
				createByLike = createByLike + "%";
			}
		}
		return createByLike;
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

	public Date getDiscoverTimeGreaterThanOrEqual() {
		return discoverTimeGreaterThanOrEqual;
	}

	public Date getDiscoverTimeLessThanOrEqual() {
		return discoverTimeLessThanOrEqual;
	}

	public String getGradeId() {
		return gradeId;
	}

	public String getGradeIdLike() {
		if (gradeIdLike != null && gradeIdLike.trim().length() > 0) {
			if (!gradeIdLike.startsWith("%")) {
				gradeIdLike = "%" + gradeIdLike;
			}
			if (!gradeIdLike.endsWith("%")) {
				gradeIdLike = gradeIdLike + "%";
			}
		}
		return gradeIdLike;
	}

	public Collection<String> getGradeIds() {
		return gradeIds;
	}

	public String getHospital() {
		return hospital;
	}

	public String getHospitalLike() {
		if (hospitalLike != null && hospitalLike.trim().length() > 0) {
			if (!hospitalLike.startsWith("%")) {
				hospitalLike = "%" + hospitalLike;
			}
			if (!hospitalLike.endsWith("%")) {
				hospitalLike = hospitalLike + "%";
			}
		}
		return hospitalLike;
	}

	public List<String> getHospitals() {
		return hospitals;
	}

	public String getInfectiousFlag() {
		return infectiousFlag;
	}

	public String getInfectiousFlagLike() {
		if (infectiousFlagLike != null && infectiousFlagLike.trim().length() > 0) {
			if (!infectiousFlagLike.startsWith("%")) {
				infectiousFlagLike = "%" + infectiousFlagLike;
			}
			if (!infectiousFlagLike.endsWith("%")) {
				infectiousFlagLike = infectiousFlagLike + "%";
			}
		}
		return infectiousFlagLike;
	}

	public List<String> getInfectiousFlags() {
		return infectiousFlags;
	}

	public Integer getMonth() {
		return month;
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

	public List<String> getNames() {
		return names;
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

			if ("gradeId".equals(sortColumn)) {
				orderBy = "E.GRADEID_" + a_x;
			}

			if ("personId".equals(sortColumn)) {
				orderBy = "E.PERSONID_" + a_x;
			}

			if ("name".equals(sortColumn)) {
				orderBy = "E.NAME_" + a_x;
			}

			if ("sickness".equals(sortColumn)) {
				orderBy = "E.SICKNESS_" + a_x;
			}

			if ("infectiousFlag".equals(sortColumn)) {
				orderBy = "E.INFECTIOUSFLAG_" + a_x;
			}

			if ("discoverTime".equals(sortColumn)) {
				orderBy = "E.DISCOVERTIME_" + a_x;
			}

			if ("reportTime".equals(sortColumn)) {
				orderBy = "E.REPORTTIME_" + a_x;
			}

			if ("confirmTime".equals(sortColumn)) {
				orderBy = "E.CONFIRMTIME_" + a_x;
			}

			if ("receiver1".equals(sortColumn)) {
				orderBy = "E.RECEIVER1_" + a_x;
			}

			if ("receiveOrg1".equals(sortColumn)) {
				orderBy = "E.RECEIVEORG1_" + a_x;
			}

			if ("receiver2".equals(sortColumn)) {
				orderBy = "E.RECEIVER2_" + a_x;
			}

			if ("receiveOrg2".equals(sortColumn)) {
				orderBy = "E.RECEIVEORG2_" + a_x;
			}

			if ("treat".equals(sortColumn)) {
				orderBy = "E.TREAT_" + a_x;
			}

			if ("hospital".equals(sortColumn)) {
				orderBy = "E.HOSPITAL_" + a_x;
			}

			if ("clinicTime".equals(sortColumn)) {
				orderBy = "E.CLINICTIME_" + a_x;
			}

			if ("result".equals(sortColumn)) {
				orderBy = "E.RESULT_" + a_x;
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

	public String getPersonIdLike() {
		if (personIdLike != null && personIdLike.trim().length() > 0) {
			if (!personIdLike.startsWith("%")) {
				personIdLike = "%" + personIdLike;
			}
			if (!personIdLike.endsWith("%")) {
				personIdLike = personIdLike + "%";
			}
		}
		return personIdLike;
	}

	public List<String> getPersonIds() {
		return personIds;
	}

	public String getReceiveOrg1() {
		return receiveOrg1;
	}

	public String getReceiveOrg1Like() {
		if (receiveOrg1Like != null && receiveOrg1Like.trim().length() > 0) {
			if (!receiveOrg1Like.startsWith("%")) {
				receiveOrg1Like = "%" + receiveOrg1Like;
			}
			if (!receiveOrg1Like.endsWith("%")) {
				receiveOrg1Like = receiveOrg1Like + "%";
			}
		}
		return receiveOrg1Like;
	}

	public List<String> getReceiveOrg1s() {
		return receiveOrg1s;
	}

	public String getReceiveOrg2() {
		return receiveOrg2;
	}

	public String getReceiveOrg2Like() {
		if (receiveOrg2Like != null && receiveOrg2Like.trim().length() > 0) {
			if (!receiveOrg2Like.startsWith("%")) {
				receiveOrg2Like = "%" + receiveOrg2Like;
			}
			if (!receiveOrg2Like.endsWith("%")) {
				receiveOrg2Like = receiveOrg2Like + "%";
			}
		}
		return receiveOrg2Like;
	}

	public List<String> getReceiveOrg2s() {
		return receiveOrg2s;
	}

	public String getReceiver1() {
		return receiver1;
	}

	public String getReceiver1Like() {
		if (receiver1Like != null && receiver1Like.trim().length() > 0) {
			if (!receiver1Like.startsWith("%")) {
				receiver1Like = "%" + receiver1Like;
			}
			if (!receiver1Like.endsWith("%")) {
				receiver1Like = receiver1Like + "%";
			}
		}
		return receiver1Like;
	}

	public List<String> getReceiver1s() {
		return receiver1s;
	}

	public String getReceiver2() {
		return receiver2;
	}

	public String getReceiver2Like() {
		if (receiver2Like != null && receiver2Like.trim().length() > 0) {
			if (!receiver2Like.startsWith("%")) {
				receiver2Like = "%" + receiver2Like;
			}
			if (!receiver2Like.endsWith("%")) {
				receiver2Like = receiver2Like + "%";
			}
		}
		return receiver2Like;
	}

	public List<String> getReceiver2s() {
		return receiver2s;
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

	public Date getReportTimeGreaterThanOrEqual() {
		return reportTimeGreaterThanOrEqual;
	}

	public Date getReportTimeLessThanOrEqual() {
		return reportTimeLessThanOrEqual;
	}

	public String getResult() {
		return result;
	}

	public String getResultLike() {
		if (resultLike != null && resultLike.trim().length() > 0) {
			if (!resultLike.startsWith("%")) {
				resultLike = "%" + resultLike;
			}
			if (!resultLike.endsWith("%")) {
				resultLike = resultLike + "%";
			}
		}
		return resultLike;
	}

	public String getSickness() {
		return sickness;
	}

	public String getSicknessLike() {
		if (sicknessLike != null && sicknessLike.trim().length() > 0) {
			if (!sicknessLike.startsWith("%")) {
				sicknessLike = "%" + sicknessLike;
			}
			if (!sicknessLike.endsWith("%")) {
				sicknessLike = sicknessLike + "%";
			}
		}
		return sicknessLike;
	}

	public List<String> getSicknesss() {
		return sicknesss;
	}

	public Collection<String> getTenantIds() {
		return tenantIds;
	}

	public String getTreat() {
		return treat;
	}

	public String getTreatLike() {
		if (treatLike != null && treatLike.trim().length() > 0) {
			if (!treatLike.startsWith("%")) {
				treatLike = "%" + treatLike;
			}
			if (!treatLike.endsWith("%")) {
				treatLike = treatLike + "%";
			}
		}
		return treatLike;
	}

	public List<String> getTreats() {
		return treats;
	}

	public Integer getYear() {
		return year;
	}

	public PersonSicknessQuery gradeId(String gradeId) {
		if (gradeId == null) {
			throw new RuntimeException("gradeId is null");
		}
		this.gradeId = gradeId;
		return this;
	}

	public PersonSicknessQuery gradeIdLike(String gradeIdLike) {
		if (gradeIdLike == null) {
			throw new RuntimeException("gradeId is null");
		}
		this.gradeIdLike = gradeIdLike;
		return this;
	}

	public PersonSicknessQuery gradeIds(Collection<String> gradeIds) {
		if (gradeIds == null) {
			throw new RuntimeException("gradeIds is empty ");
		}
		this.gradeIds = gradeIds;
		return this;
	}

	public PersonSicknessQuery hospital(String hospital) {
		if (hospital == null) {
			throw new RuntimeException("hospital is null");
		}
		this.hospital = hospital;
		return this;
	}

	public PersonSicknessQuery hospitalLike(String hospitalLike) {
		if (hospitalLike == null) {
			throw new RuntimeException("hospital is null");
		}
		this.hospitalLike = hospitalLike;
		return this;
	}

	public PersonSicknessQuery hospitals(List<String> hospitals) {
		if (hospitals == null) {
			throw new RuntimeException("hospitals is empty ");
		}
		this.hospitals = hospitals;
		return this;
	}

	public PersonSicknessQuery infectiousFlag(String infectiousFlag) {
		if (infectiousFlag == null) {
			throw new RuntimeException("infectiousFlag is null");
		}
		this.infectiousFlag = infectiousFlag;
		return this;
	}

	public PersonSicknessQuery infectiousFlagLike(String infectiousFlagLike) {
		if (infectiousFlagLike == null) {
			throw new RuntimeException("infectiousFlag is null");
		}
		this.infectiousFlagLike = infectiousFlagLike;
		return this;
	}

	public PersonSicknessQuery infectiousFlags(List<String> infectiousFlags) {
		if (infectiousFlags == null) {
			throw new RuntimeException("infectiousFlags is empty ");
		}
		this.infectiousFlags = infectiousFlags;
		return this;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("tenantId", "TENANTID_");
		addColumn("gradeId", "GRADEID_");
		addColumn("personId", "PERSONID_");
		addColumn("name", "NAME_");
		addColumn("sickness", "SICKNESS_");
		addColumn("infectiousFlag", "INFECTIOUSFLAG_");
		addColumn("discoverTime", "DISCOVERTIME_");
		addColumn("reportTime", "REPORTTIME_");
		addColumn("confirmTime", "CONFIRMTIME_");
		addColumn("receiver1", "RECEIVER1_");
		addColumn("receiveOrg1", "RECEIVEORG1_");
		addColumn("receiver2", "RECEIVER2_");
		addColumn("receiveOrg2", "RECEIVEORG2_");
		addColumn("treat", "TREAT_");
		addColumn("hospital", "HOSPITAL_");
		addColumn("clinicTime", "CLINICTIME_");
		addColumn("result", "RESULT_");
		addColumn("remark", "REMARK_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
	}

	public PersonSicknessQuery month(Integer month) {
		if (month == null) {
			throw new RuntimeException("month is null");
		}
		this.month = month;
		return this;
	}

	public PersonSicknessQuery name(String name) {
		if (name == null) {
			throw new RuntimeException("name is null");
		}
		this.name = name;
		return this;
	}

	public PersonSicknessQuery nameLike(String nameLike) {
		if (nameLike == null) {
			throw new RuntimeException("name is null");
		}
		this.nameLike = nameLike;
		return this;
	}

	public PersonSicknessQuery names(List<String> names) {
		if (names == null) {
			throw new RuntimeException("names is empty ");
		}
		this.names = names;
		return this;
	}

	public PersonSicknessQuery personId(String personId) {
		if (personId == null) {
			throw new RuntimeException("personId is null");
		}
		this.personId = personId;
		return this;
	}

	public PersonSicknessQuery personIdLike(String personIdLike) {
		if (personIdLike == null) {
			throw new RuntimeException("personId is null");
		}
		this.personIdLike = personIdLike;
		return this;
	}

	public PersonSicknessQuery personIds(List<String> personIds) {
		if (personIds == null) {
			throw new RuntimeException("personIds is empty ");
		}
		this.personIds = personIds;
		return this;
	}

	public PersonSicknessQuery receiveOrg1(String receiveOrg1) {
		if (receiveOrg1 == null) {
			throw new RuntimeException("receiveOrg1 is null");
		}
		this.receiveOrg1 = receiveOrg1;
		return this;
	}

	public PersonSicknessQuery receiveOrg1Like(String receiveOrg1Like) {
		if (receiveOrg1Like == null) {
			throw new RuntimeException("receiveOrg1 is null");
		}
		this.receiveOrg1Like = receiveOrg1Like;
		return this;
	}

	public PersonSicknessQuery receiveOrg1s(List<String> receiveOrg1s) {
		if (receiveOrg1s == null) {
			throw new RuntimeException("receiveOrg1s is empty ");
		}
		this.receiveOrg1s = receiveOrg1s;
		return this;
	}

	public PersonSicknessQuery receiveOrg2(String receiveOrg2) {
		if (receiveOrg2 == null) {
			throw new RuntimeException("receiveOrg2 is null");
		}
		this.receiveOrg2 = receiveOrg2;
		return this;
	}

	public PersonSicknessQuery receiveOrg2Like(String receiveOrg2Like) {
		if (receiveOrg2Like == null) {
			throw new RuntimeException("receiveOrg2 is null");
		}
		this.receiveOrg2Like = receiveOrg2Like;
		return this;
	}

	public PersonSicknessQuery receiveOrg2s(List<String> receiveOrg2s) {
		if (receiveOrg2s == null) {
			throw new RuntimeException("receiveOrg2s is empty ");
		}
		this.receiveOrg2s = receiveOrg2s;
		return this;
	}

	public PersonSicknessQuery receiver1(String receiver1) {
		if (receiver1 == null) {
			throw new RuntimeException("receiver1 is null");
		}
		this.receiver1 = receiver1;
		return this;
	}

	public PersonSicknessQuery receiver1Like(String receiver1Like) {
		if (receiver1Like == null) {
			throw new RuntimeException("receiver1 is null");
		}
		this.receiver1Like = receiver1Like;
		return this;
	}

	public PersonSicknessQuery receiver1s(List<String> receiver1s) {
		if (receiver1s == null) {
			throw new RuntimeException("receiver1s is empty ");
		}
		this.receiver1s = receiver1s;
		return this;
	}

	public PersonSicknessQuery receiver2(String receiver2) {
		if (receiver2 == null) {
			throw new RuntimeException("receiver2 is null");
		}
		this.receiver2 = receiver2;
		return this;
	}

	public PersonSicknessQuery receiver2Like(String receiver2Like) {
		if (receiver2Like == null) {
			throw new RuntimeException("receiver2 is null");
		}
		this.receiver2Like = receiver2Like;
		return this;
	}

	public PersonSicknessQuery receiver2s(List<String> receiver2s) {
		if (receiver2s == null) {
			throw new RuntimeException("receiver2s is empty ");
		}
		this.receiver2s = receiver2s;
		return this;
	}

	public PersonSicknessQuery remarkLike(String remarkLike) {
		if (remarkLike == null) {
			throw new RuntimeException("remark is null");
		}
		this.remarkLike = remarkLike;
		return this;
	}

	public PersonSicknessQuery reportTimeGreaterThanOrEqual(Date reportTimeGreaterThanOrEqual) {
		if (reportTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("reportTime is null");
		}
		this.reportTimeGreaterThanOrEqual = reportTimeGreaterThanOrEqual;
		return this;
	}

	public PersonSicknessQuery reportTimeLessThanOrEqual(Date reportTimeLessThanOrEqual) {
		if (reportTimeLessThanOrEqual == null) {
			throw new RuntimeException("reportTime is null");
		}
		this.reportTimeLessThanOrEqual = reportTimeLessThanOrEqual;
		return this;
	}

	public PersonSicknessQuery result(String result) {
		if (result == null) {
			throw new RuntimeException("result is null");
		}
		this.result = result;
		return this;
	}

	public PersonSicknessQuery resultLike(String resultLike) {
		if (resultLike == null) {
			throw new RuntimeException("result is null");
		}
		this.resultLike = resultLike;
		return this;
	}

	public void setClinicTimeGreaterThanOrEqual(Date clinicTimeGreaterThanOrEqual) {
		this.clinicTimeGreaterThanOrEqual = clinicTimeGreaterThanOrEqual;
	}

	public void setClinicTimeLessThanOrEqual(Date clinicTimeLessThanOrEqual) {
		this.clinicTimeLessThanOrEqual = clinicTimeLessThanOrEqual;
	}

	public void setConfirmTimeGreaterThanOrEqual(Date confirmTimeGreaterThanOrEqual) {
		this.confirmTimeGreaterThanOrEqual = confirmTimeGreaterThanOrEqual;
	}

	public void setConfirmTimeLessThanOrEqual(Date confirmTimeLessThanOrEqual) {
		this.confirmTimeLessThanOrEqual = confirmTimeLessThanOrEqual;
	}

	public void setCreateByLike(String createByLike) {
		this.createByLike = createByLike;
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

	public void setDiscoverTimeGreaterThanOrEqual(Date discoverTimeGreaterThanOrEqual) {
		this.discoverTimeGreaterThanOrEqual = discoverTimeGreaterThanOrEqual;
	}

	public void setDiscoverTimeLessThanOrEqual(Date discoverTimeLessThanOrEqual) {
		this.discoverTimeLessThanOrEqual = discoverTimeLessThanOrEqual;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public void setGradeIdLike(String gradeIdLike) {
		this.gradeIdLike = gradeIdLike;
	}

	public void setGradeIds(Collection<String> gradeIds) {
		this.gradeIds = gradeIds;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	public void setHospitalLike(String hospitalLike) {
		this.hospitalLike = hospitalLike;
	}

	public void setHospitals(List<String> hospitals) {
		this.hospitals = hospitals;
	}

	public void setInfectiousFlag(String infectiousFlag) {
		this.infectiousFlag = infectiousFlag;
	}

	public void setInfectiousFlagLike(String infectiousFlagLike) {
		this.infectiousFlagLike = infectiousFlagLike;
	}

	public void setInfectiousFlags(List<String> infectiousFlags) {
		this.infectiousFlags = infectiousFlags;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNameLike(String nameLike) {
		this.nameLike = nameLike;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public void setPersonIdLike(String personIdLike) {
		this.personIdLike = personIdLike;
	}

	public void setPersonIds(List<String> personIds) {
		this.personIds = personIds;
	}

	public void setReceiveOrg1(String receiveOrg1) {
		this.receiveOrg1 = receiveOrg1;
	}

	public void setReceiveOrg1Like(String receiveOrg1Like) {
		this.receiveOrg1Like = receiveOrg1Like;
	}

	public void setReceiveOrg1s(List<String> receiveOrg1s) {
		this.receiveOrg1s = receiveOrg1s;
	}

	public void setReceiveOrg2(String receiveOrg2) {
		this.receiveOrg2 = receiveOrg2;
	}

	public void setReceiveOrg2Like(String receiveOrg2Like) {
		this.receiveOrg2Like = receiveOrg2Like;
	}

	public void setReceiveOrg2s(List<String> receiveOrg2s) {
		this.receiveOrg2s = receiveOrg2s;
	}

	public void setReceiver1(String receiver1) {
		this.receiver1 = receiver1;
	}

	public void setReceiver1Like(String receiver1Like) {
		this.receiver1Like = receiver1Like;
	}

	public void setReceiver1s(List<String> receiver1s) {
		this.receiver1s = receiver1s;
	}

	public void setReceiver2(String receiver2) {
		this.receiver2 = receiver2;
	}

	public void setReceiver2Like(String receiver2Like) {
		this.receiver2Like = receiver2Like;
	}

	public void setReceiver2s(List<String> receiver2s) {
		this.receiver2s = receiver2s;
	}

	public void setRemarkLike(String remarkLike) {
		this.remarkLike = remarkLike;
	}

	public void setReportTimeGreaterThanOrEqual(Date reportTimeGreaterThanOrEqual) {
		this.reportTimeGreaterThanOrEqual = reportTimeGreaterThanOrEqual;
	}

	public void setReportTimeLessThanOrEqual(Date reportTimeLessThanOrEqual) {
		this.reportTimeLessThanOrEqual = reportTimeLessThanOrEqual;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public void setResultLike(String resultLike) {
		this.resultLike = resultLike;
	}

	public void setSickness(String sickness) {
		this.sickness = sickness;
	}

	public void setSicknessLike(String sicknessLike) {
		this.sicknessLike = sicknessLike;
	}

	public void setSicknesss(List<String> sicknesss) {
		this.sicknesss = sicknesss;
	}

	public void setTenantIds(Collection<String> tenantIds) {
		this.tenantIds = tenantIds;
	}

	public void setTreat(String treat) {
		this.treat = treat;
	}

	public void setTreatLike(String treatLike) {
		this.treatLike = treatLike;
	}

	public void setTreats(List<String> treats) {
		this.treats = treats;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public PersonSicknessQuery sickness(String sickness) {
		if (sickness == null) {
			throw new RuntimeException("sickness is null");
		}
		this.sickness = sickness;
		return this;
	}

	public PersonSicknessQuery sicknessLike(String sicknessLike) {
		if (sicknessLike == null) {
			throw new RuntimeException("sickness is null");
		}
		this.sicknessLike = sicknessLike;
		return this;
	}

	public PersonSicknessQuery sicknesss(List<String> sicknesss) {
		if (sicknesss == null) {
			throw new RuntimeException("sicknesss is empty ");
		}
		this.sicknesss = sicknesss;
		return this;
	}

	public PersonSicknessQuery tenantIds(List<String> tenantIds) {
		if (tenantIds == null) {
			throw new RuntimeException("tenantIds is empty ");
		}
		this.tenantIds = tenantIds;
		return this;
	}

	public PersonSicknessQuery treat(String treat) {
		if (treat == null) {
			throw new RuntimeException("treat is null");
		}
		this.treat = treat;
		return this;
	}

	public PersonSicknessQuery treatLike(String treatLike) {
		if (treatLike == null) {
			throw new RuntimeException("treat is null");
		}
		this.treatLike = treatLike;
		return this;
	}

	public PersonSicknessQuery treats(List<String> treats) {
		if (treats == null) {
			throw new RuntimeException("treats is empty ");
		}
		this.treats = treats;
		return this;
	}

	public PersonSicknessQuery year(Integer year) {
		if (year == null) {
			throw new RuntimeException("year is null");
		}
		this.year = year;
		return this;
	}

}