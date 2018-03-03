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

public class MedicalExaminationQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected Long batchId;
	protected String checkId;
	protected List<String> tenantIds;
	protected String gradeId;
	protected List<String> gradeIds;
	protected String personId;
	protected List<String> personIds;
	protected String nameLike;
	protected String sex;
	protected Double heightGreaterThanOrEqual;
	protected Double heightLessThanOrEqual;
	protected Integer heightLevel;
	protected Integer heightLevelGreaterThanOrEqual;
	protected Integer heightLevelLessThanOrEqual;
	protected String heightEvaluate;
	protected String heightEvaluateLike;
	protected Double weightGreaterThanOrEqual;
	protected Double weightLessThanOrEqual;
	protected Integer weightLevel;
	protected Integer weightLevelGreaterThanOrEqual;
	protected Integer weightLevelLessThanOrEqual;
	protected String weightEvaluate;
	protected String weightEvaluateLike;
	protected Double weightHeightPercentGreaterThanOrEqual;
	protected Double weightHeightPercentLessThanOrEqual;
	protected Double bmiGreaterThanOrEqual;
	protected Double bmiLessThanOrEqual;
	protected String allergy;
	protected String allergyLike;
	protected String eyeLeft;
	protected String eyeLeftLike;
	protected String eyeRight;
	protected String eyeRightLike;
	protected Double eyesightLeftGreaterThanOrEqual;
	protected Double eyesightLeftLessThanOrEqual;
	protected Double eyesightRightGreaterThanOrEqual;
	protected Double eyesightRightLessThanOrEqual;
	protected String earLeft;
	protected String earLeftLike;
	protected String earRight;
	protected String earRightLike;
	protected Integer saprodontia;
	protected Integer saprodontiaGreaterThanOrEqual;
	protected Integer saprodontiaLessThanOrEqual;
	protected String head;
	protected String headLike;
	protected String thorax;
	protected String thoraxLike;
	protected String spine;
	protected String spineLike;
	protected String pharyngeal;
	protected String pharyngealLike;
	protected String cardiopulmonary;
	protected String cardiopulmonaryLike;
	protected String hepatolienal;
	protected String hepatolienalLike;
	protected String pudendum;
	protected String pudendumLike;
	protected String skin;
	protected String skinLike;
	protected String hemoglobin;
	protected String hemoglobinLike;
	protected String alt;
	protected String altLike;
	protected String type;
	protected List<String> types;
	protected Date checkDateGreaterThanOrEqual;
	protected Date checkDateLessThanOrEqual;
	protected String checkDoctor;
	protected String checkDoctorLike;
	protected String checkDoctorId;
	protected String checkDoctorIdLike;
	protected String checkResult;
	protected String checkResultLike;
	protected String checkOrganization;
	protected String checkOrganizationLike;
	protected Long checkOrganizationId;
	protected List<Long> checkOrganizationIds;
	protected String doctorSuggest;
	protected String doctorSuggestLike;
	protected Integer year;
	protected Integer yearGreaterThanOrEqual;
	protected Integer yearLessThanOrEqual;
	protected List<Integer> years;
	protected Integer month;
	protected Integer monthGreaterThanOrEqual;
	protected Integer monthLessThanOrEqual;
	protected List<Integer> months;
	protected String remarkLike;
	protected String confirmBy;
	protected String confirmByLike;
	protected Date confirmTimeGreaterThanOrEqual;
	protected Date confirmTimeLessThanOrEqual;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	public MedicalExaminationQuery() {

	}

	public MedicalExaminationQuery allergy(String allergy) {
		if (allergy == null) {
			throw new RuntimeException("allergy is null");
		}
		this.allergy = allergy;
		return this;
	}

	public MedicalExaminationQuery allergyLike(String allergyLike) {
		if (allergyLike == null) {
			throw new RuntimeException("allergy is null");
		}
		this.allergyLike = allergyLike;
		return this;
	}

	public MedicalExaminationQuery alt(String alt) {
		if (alt == null) {
			throw new RuntimeException("alt is null");
		}
		this.alt = alt;
		return this;
	}

	public MedicalExaminationQuery altLike(String altLike) {
		if (altLike == null) {
			throw new RuntimeException("alt is null");
		}
		this.altLike = altLike;
		return this;
	}

	public MedicalExaminationQuery batchId(Long batchId) {
		if (batchId == null) {
			throw new RuntimeException("batchId is null");
		}
		this.batchId = batchId;
		return this;
	}

	public MedicalExaminationQuery bmiGreaterThanOrEqual(Double bmiGreaterThanOrEqual) {
		if (bmiGreaterThanOrEqual == null) {
			throw new RuntimeException("bmi is null");
		}
		this.bmiGreaterThanOrEqual = bmiGreaterThanOrEqual;
		return this;
	}

	public MedicalExaminationQuery bmiLessThanOrEqual(Double bmiLessThanOrEqual) {
		if (bmiLessThanOrEqual == null) {
			throw new RuntimeException("bmi is null");
		}
		this.bmiLessThanOrEqual = bmiLessThanOrEqual;
		return this;
	}

	public MedicalExaminationQuery cardiopulmonary(String cardiopulmonary) {
		if (cardiopulmonary == null) {
			throw new RuntimeException("cardiopulmonary is null");
		}
		this.cardiopulmonary = cardiopulmonary;
		return this;
	}

	public MedicalExaminationQuery cardiopulmonaryLike(String cardiopulmonaryLike) {
		if (cardiopulmonaryLike == null) {
			throw new RuntimeException("cardiopulmonary is null");
		}
		this.cardiopulmonaryLike = cardiopulmonaryLike;
		return this;
	}

	public MedicalExaminationQuery checkDateGreaterThanOrEqual(Date checkDateGreaterThanOrEqual) {
		if (checkDateGreaterThanOrEqual == null) {
			throw new RuntimeException("checkDate is null");
		}
		this.checkDateGreaterThanOrEqual = checkDateGreaterThanOrEqual;
		return this;
	}

	public MedicalExaminationQuery checkDateLessThanOrEqual(Date checkDateLessThanOrEqual) {
		if (checkDateLessThanOrEqual == null) {
			throw new RuntimeException("checkDate is null");
		}
		this.checkDateLessThanOrEqual = checkDateLessThanOrEqual;
		return this;
	}

	public MedicalExaminationQuery checkDoctor(String checkDoctor) {
		if (checkDoctor == null) {
			throw new RuntimeException("checkDoctor is null");
		}
		this.checkDoctor = checkDoctor;
		return this;
	}

	public MedicalExaminationQuery checkDoctorId(String checkDoctorId) {
		if (checkDoctorId == null) {
			throw new RuntimeException("checkDoctorId is null");
		}
		this.checkDoctorId = checkDoctorId;
		return this;
	}

	public MedicalExaminationQuery checkDoctorIdLike(String checkDoctorIdLike) {
		if (checkDoctorIdLike == null) {
			throw new RuntimeException("checkDoctorId is null");
		}
		this.checkDoctorIdLike = checkDoctorIdLike;
		return this;
	}

	public MedicalExaminationQuery checkDoctorLike(String checkDoctorLike) {
		if (checkDoctorLike == null) {
			throw new RuntimeException("checkDoctor is null");
		}
		this.checkDoctorLike = checkDoctorLike;
		return this;
	}

	public MedicalExaminationQuery checkId(String checkId) {
		if (checkId == null) {
			throw new RuntimeException("checkId is null");
		}
		this.checkId = checkId;
		return this;
	}

	public MedicalExaminationQuery checkOrganization(String checkOrganization) {
		if (checkOrganization == null) {
			throw new RuntimeException("checkOrganization is null");
		}
		this.checkOrganization = checkOrganization;
		return this;
	}

	public MedicalExaminationQuery checkOrganizationId(Long checkOrganizationId) {
		if (checkOrganizationId == null) {
			throw new RuntimeException("checkOrganizationId is null");
		}
		this.checkOrganizationId = checkOrganizationId;
		return this;
	}

	public MedicalExaminationQuery checkOrganizationIds(List<Long> checkOrganizationIds) {
		if (checkOrganizationIds == null) {
			throw new RuntimeException("checkOrganizationIds is empty ");
		}
		this.checkOrganizationIds = checkOrganizationIds;
		return this;
	}

	public MedicalExaminationQuery checkOrganizationLike(String checkOrganizationLike) {
		if (checkOrganizationLike == null) {
			throw new RuntimeException("checkOrganization is null");
		}
		this.checkOrganizationLike = checkOrganizationLike;
		return this;
	}

	public MedicalExaminationQuery checkResult(String checkResult) {
		if (checkResult == null) {
			throw new RuntimeException("checkResult is null");
		}
		this.checkResult = checkResult;
		return this;
	}

	public MedicalExaminationQuery checkResultLike(String checkResultLike) {
		if (checkResultLike == null) {
			throw new RuntimeException("checkResult is null");
		}
		this.checkResultLike = checkResultLike;
		return this;
	}

	public MedicalExaminationQuery confirmBy(String confirmBy) {
		if (confirmBy == null) {
			throw new RuntimeException("confirmBy is null");
		}
		this.confirmBy = confirmBy;
		return this;
	}

	public MedicalExaminationQuery confirmByLike(String confirmByLike) {
		if (confirmByLike == null) {
			throw new RuntimeException("confirmBy is null");
		}
		this.confirmByLike = confirmByLike;
		return this;
	}

	public MedicalExaminationQuery confirmTimeGreaterThanOrEqual(Date confirmTimeGreaterThanOrEqual) {
		if (confirmTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("confirmTime is null");
		}
		this.confirmTimeGreaterThanOrEqual = confirmTimeGreaterThanOrEqual;
		return this;
	}

	public MedicalExaminationQuery confirmTimeLessThanOrEqual(Date confirmTimeLessThanOrEqual) {
		if (confirmTimeLessThanOrEqual == null) {
			throw new RuntimeException("confirmTime is null");
		}
		this.confirmTimeLessThanOrEqual = confirmTimeLessThanOrEqual;
		return this;
	}

	public MedicalExaminationQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public MedicalExaminationQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public MedicalExaminationQuery doctorSuggest(String doctorSuggest) {
		if (doctorSuggest == null) {
			throw new RuntimeException("doctorSuggest is null");
		}
		this.doctorSuggest = doctorSuggest;
		return this;
	}

	public MedicalExaminationQuery doctorSuggestLike(String doctorSuggestLike) {
		if (doctorSuggestLike == null) {
			throw new RuntimeException("doctorSuggest is null");
		}
		this.doctorSuggestLike = doctorSuggestLike;
		return this;
	}

	public MedicalExaminationQuery earLeft(String earLeft) {
		if (earLeft == null) {
			throw new RuntimeException("earLeft is null");
		}
		this.earLeft = earLeft;
		return this;
	}

	public MedicalExaminationQuery earLeftLike(String earLeftLike) {
		if (earLeftLike == null) {
			throw new RuntimeException("earLeft is null");
		}
		this.earLeftLike = earLeftLike;
		return this;
	}

	public MedicalExaminationQuery earRight(String earRight) {
		if (earRight == null) {
			throw new RuntimeException("earRight is null");
		}
		this.earRight = earRight;
		return this;
	}

	public MedicalExaminationQuery earRightLike(String earRightLike) {
		if (earRightLike == null) {
			throw new RuntimeException("earRight is null");
		}
		this.earRightLike = earRightLike;
		return this;
	}

	public MedicalExaminationQuery eyeLeft(String eyeLeft) {
		if (eyeLeft == null) {
			throw new RuntimeException("eyeLeft is null");
		}
		this.eyeLeft = eyeLeft;
		return this;
	}

	public MedicalExaminationQuery eyeLeftLike(String eyeLeftLike) {
		if (eyeLeftLike == null) {
			throw new RuntimeException("eyeLeft is null");
		}
		this.eyeLeftLike = eyeLeftLike;
		return this;
	}

	public MedicalExaminationQuery eyeRight(String eyeRight) {
		if (eyeRight == null) {
			throw new RuntimeException("eyeRight is null");
		}
		this.eyeRight = eyeRight;
		return this;
	}

	public MedicalExaminationQuery eyeRightLike(String eyeRightLike) {
		if (eyeRightLike == null) {
			throw new RuntimeException("eyeRight is null");
		}
		this.eyeRightLike = eyeRightLike;
		return this;
	}

	public MedicalExaminationQuery eyesightLeftGreaterThanOrEqual(Double eyesightLeftGreaterThanOrEqual) {
		if (eyesightLeftGreaterThanOrEqual == null) {
			throw new RuntimeException("eyesightLeft is null");
		}
		this.eyesightLeftGreaterThanOrEqual = eyesightLeftGreaterThanOrEqual;
		return this;
	}

	public MedicalExaminationQuery eyesightLeftLessThanOrEqual(Double eyesightLeftLessThanOrEqual) {
		if (eyesightLeftLessThanOrEqual == null) {
			throw new RuntimeException("eyesightLeft is null");
		}
		this.eyesightLeftLessThanOrEqual = eyesightLeftLessThanOrEqual;
		return this;
	}

	public MedicalExaminationQuery eyesightRightGreaterThanOrEqual(Double eyesightRightGreaterThanOrEqual) {
		if (eyesightRightGreaterThanOrEqual == null) {
			throw new RuntimeException("eyesightRight is null");
		}
		this.eyesightRightGreaterThanOrEqual = eyesightRightGreaterThanOrEqual;
		return this;
	}

	public MedicalExaminationQuery eyesightRightLessThanOrEqual(Double eyesightRightLessThanOrEqual) {
		if (eyesightRightLessThanOrEqual == null) {
			throw new RuntimeException("eyesightRight is null");
		}
		this.eyesightRightLessThanOrEqual = eyesightRightLessThanOrEqual;
		return this;
	}

	public String getAllergy() {
		return allergy;
	}

	public String getAllergyLike() {
		if (allergyLike != null && allergyLike.trim().length() > 0) {
			if (!allergyLike.startsWith("%")) {
				allergyLike = "%" + allergyLike;
			}
			if (!allergyLike.endsWith("%")) {
				allergyLike = allergyLike + "%";
			}
		}
		return allergyLike;
	}

	public String getAlt() {
		return alt;
	}

	public String getAltLike() {
		if (altLike != null && altLike.trim().length() > 0) {
			if (!altLike.startsWith("%")) {
				altLike = "%" + altLike;
			}
			if (!altLike.endsWith("%")) {
				altLike = altLike + "%";
			}
		}
		return altLike;
	}

	public Long getBatchId() {
		return batchId;
	}

	public Double getBmiGreaterThanOrEqual() {
		return bmiGreaterThanOrEqual;
	}

	public Double getBmiLessThanOrEqual() {
		return bmiLessThanOrEqual;
	}

	public String getCardiopulmonary() {
		return cardiopulmonary;
	}

	public String getCardiopulmonaryLike() {
		if (cardiopulmonaryLike != null && cardiopulmonaryLike.trim().length() > 0) {
			if (!cardiopulmonaryLike.startsWith("%")) {
				cardiopulmonaryLike = "%" + cardiopulmonaryLike;
			}
			if (!cardiopulmonaryLike.endsWith("%")) {
				cardiopulmonaryLike = cardiopulmonaryLike + "%";
			}
		}
		return cardiopulmonaryLike;
	}

	public Date getCheckDateGreaterThanOrEqual() {
		return checkDateGreaterThanOrEqual;
	}

	public Date getCheckDateLessThanOrEqual() {
		return checkDateLessThanOrEqual;
	}

	public String getCheckDoctor() {
		return checkDoctor;
	}

	public String getCheckDoctorId() {
		return checkDoctorId;
	}

	public String getCheckDoctorIdLike() {
		if (checkDoctorIdLike != null && checkDoctorIdLike.trim().length() > 0) {
			if (!checkDoctorIdLike.startsWith("%")) {
				checkDoctorIdLike = "%" + checkDoctorIdLike;
			}
			if (!checkDoctorIdLike.endsWith("%")) {
				checkDoctorIdLike = checkDoctorIdLike + "%";
			}
		}
		return checkDoctorIdLike;
	}

	public String getCheckDoctorLike() {
		if (checkDoctorLike != null && checkDoctorLike.trim().length() > 0) {
			if (!checkDoctorLike.startsWith("%")) {
				checkDoctorLike = "%" + checkDoctorLike;
			}
			if (!checkDoctorLike.endsWith("%")) {
				checkDoctorLike = checkDoctorLike + "%";
			}
		}
		return checkDoctorLike;
	}

	public String getCheckId() {
		return checkId;
	}

	public String getCheckOrganization() {
		return checkOrganization;
	}

	public Long getCheckOrganizationId() {
		return checkOrganizationId;
	}

	public List<Long> getCheckOrganizationIds() {
		return checkOrganizationIds;
	}

	public String getCheckOrganizationLike() {
		if (checkOrganizationLike != null && checkOrganizationLike.trim().length() > 0) {
			if (!checkOrganizationLike.startsWith("%")) {
				checkOrganizationLike = "%" + checkOrganizationLike;
			}
			if (!checkOrganizationLike.endsWith("%")) {
				checkOrganizationLike = checkOrganizationLike + "%";
			}
		}
		return checkOrganizationLike;
	}

	public String getCheckResult() {
		return checkResult;
	}

	public String getCheckResultLike() {
		if (checkResultLike != null && checkResultLike.trim().length() > 0) {
			if (!checkResultLike.startsWith("%")) {
				checkResultLike = "%" + checkResultLike;
			}
			if (!checkResultLike.endsWith("%")) {
				checkResultLike = checkResultLike + "%";
			}
		}
		return checkResultLike;
	}

	public String getConfirmBy() {
		return confirmBy;
	}

	public String getConfirmByLike() {
		if (confirmByLike != null && confirmByLike.trim().length() > 0) {
			if (!confirmByLike.startsWith("%")) {
				confirmByLike = "%" + confirmByLike;
			}
			if (!confirmByLike.endsWith("%")) {
				confirmByLike = confirmByLike + "%";
			}
		}
		return confirmByLike;
	}

	public Date getConfirmTimeGreaterThanOrEqual() {
		return confirmTimeGreaterThanOrEqual;
	}

	public Date getConfirmTimeLessThanOrEqual() {
		return confirmTimeLessThanOrEqual;
	}

	public Date getCreateTimeGreaterThanOrEqual() {
		return createTimeGreaterThanOrEqual;
	}

	public Date getCreateTimeLessThanOrEqual() {
		return createTimeLessThanOrEqual;
	}

	public String getDoctorSuggest() {
		return doctorSuggest;
	}

	public String getDoctorSuggestLike() {
		if (doctorSuggestLike != null && doctorSuggestLike.trim().length() > 0) {
			if (!doctorSuggestLike.startsWith("%")) {
				doctorSuggestLike = "%" + doctorSuggestLike;
			}
			if (!doctorSuggestLike.endsWith("%")) {
				doctorSuggestLike = doctorSuggestLike + "%";
			}
		}
		return doctorSuggestLike;
	}

	public String getEarLeft() {
		return earLeft;
	}

	public String getEarLeftLike() {
		if (earLeftLike != null && earLeftLike.trim().length() > 0) {
			if (!earLeftLike.startsWith("%")) {
				earLeftLike = "%" + earLeftLike;
			}
			if (!earLeftLike.endsWith("%")) {
				earLeftLike = earLeftLike + "%";
			}
		}
		return earLeftLike;
	}

	public String getEarRight() {
		return earRight;
	}

	public String getEarRightLike() {
		if (earRightLike != null && earRightLike.trim().length() > 0) {
			if (!earRightLike.startsWith("%")) {
				earRightLike = "%" + earRightLike;
			}
			if (!earRightLike.endsWith("%")) {
				earRightLike = earRightLike + "%";
			}
		}
		return earRightLike;
	}

	public String getEyeLeft() {
		return eyeLeft;
	}

	public String getEyeLeftLike() {
		if (eyeLeftLike != null && eyeLeftLike.trim().length() > 0) {
			if (!eyeLeftLike.startsWith("%")) {
				eyeLeftLike = "%" + eyeLeftLike;
			}
			if (!eyeLeftLike.endsWith("%")) {
				eyeLeftLike = eyeLeftLike + "%";
			}
		}
		return eyeLeftLike;
	}

	public String getEyeRight() {
		return eyeRight;
	}

	public String getEyeRightLike() {
		if (eyeRightLike != null && eyeRightLike.trim().length() > 0) {
			if (!eyeRightLike.startsWith("%")) {
				eyeRightLike = "%" + eyeRightLike;
			}
			if (!eyeRightLike.endsWith("%")) {
				eyeRightLike = eyeRightLike + "%";
			}
		}
		return eyeRightLike;
	}

	public Double getEyesightLeftGreaterThanOrEqual() {
		return eyesightLeftGreaterThanOrEqual;
	}

	public Double getEyesightLeftLessThanOrEqual() {
		return eyesightLeftLessThanOrEqual;
	}

	public Double getEyesightRightGreaterThanOrEqual() {
		return eyesightRightGreaterThanOrEqual;
	}

	public Double getEyesightRightLessThanOrEqual() {
		return eyesightRightLessThanOrEqual;
	}

	public String getGradeId() {
		return gradeId;
	}

	public List<String> getGradeIds() {
		return gradeIds;
	}

	public String getHead() {
		return head;
	}

	public String getHeadLike() {
		if (headLike != null && headLike.trim().length() > 0) {
			if (!headLike.startsWith("%")) {
				headLike = "%" + headLike;
			}
			if (!headLike.endsWith("%")) {
				headLike = headLike + "%";
			}
		}
		return headLike;
	}

	public String getHeightEvaluate() {
		return heightEvaluate;
	}

	public String getHeightEvaluateLike() {
		if (heightEvaluateLike != null && heightEvaluateLike.trim().length() > 0) {
			if (!heightEvaluateLike.startsWith("%")) {
				heightEvaluateLike = "%" + heightEvaluateLike;
			}
			if (!heightEvaluateLike.endsWith("%")) {
				heightEvaluateLike = heightEvaluateLike + "%";
			}
		}
		return heightEvaluateLike;
	}

	public Double getHeightGreaterThanOrEqual() {
		return heightGreaterThanOrEqual;
	}

	public Double getHeightLessThanOrEqual() {
		return heightLessThanOrEqual;
	}

	public Integer getHeightLevel() {
		return heightLevel;
	}

	public Integer getHeightLevelGreaterThanOrEqual() {
		return heightLevelGreaterThanOrEqual;
	}

	public Integer getHeightLevelLessThanOrEqual() {
		return heightLevelLessThanOrEqual;
	}

	public String getHemoglobin() {
		return hemoglobin;
	}

	public String getHemoglobinLike() {
		if (hemoglobinLike != null && hemoglobinLike.trim().length() > 0) {
			if (!hemoglobinLike.startsWith("%")) {
				hemoglobinLike = "%" + hemoglobinLike;
			}
			if (!hemoglobinLike.endsWith("%")) {
				hemoglobinLike = hemoglobinLike + "%";
			}
		}
		return hemoglobinLike;
	}

	public String getHepatolienal() {
		return hepatolienal;
	}

	public String getHepatolienalLike() {
		if (hepatolienalLike != null && hepatolienalLike.trim().length() > 0) {
			if (!hepatolienalLike.startsWith("%")) {
				hepatolienalLike = "%" + hepatolienalLike;
			}
			if (!hepatolienalLike.endsWith("%")) {
				hepatolienalLike = hepatolienalLike + "%";
			}
		}
		return hepatolienalLike;
	}

	public Integer getMonth() {
		return month;
	}

	public Integer getMonthGreaterThanOrEqual() {
		return monthGreaterThanOrEqual;
	}

	public Integer getMonthLessThanOrEqual() {
		return monthLessThanOrEqual;
	}

	public List<Integer> getMonths() {
		return months;
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

			if ("gradeId".equals(sortColumn)) {
				orderBy = "E.GRADEID_" + a_x;
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

			if ("height".equals(sortColumn)) {
				orderBy = "E.HEIGHT_" + a_x;
			}

			if ("heightEvaluate".equals(sortColumn)) {
				orderBy = "E.HEIGHTEVALUATE_" + a_x;
			}

			if ("weight".equals(sortColumn)) {
				orderBy = "E.WEIGHT_" + a_x;
			}

			if ("weightEvaluate".equals(sortColumn)) {
				orderBy = "E.WEIGHTEVALUATE_" + a_x;
			}

			if ("weightHeightPercent".equals(sortColumn)) {
				orderBy = "E.WEIGHTHEIGHTPERCENT_" + a_x;
			}

			if ("allergy".equals(sortColumn)) {
				orderBy = "E.ALLERGY_" + a_x;
			}

			if ("eyeLeft".equals(sortColumn)) {
				orderBy = "E.EYELEFT_" + a_x;
			}

			if ("eyeRight".equals(sortColumn)) {
				orderBy = "E.EYERIGHT_" + a_x;
			}

			if ("eyesightLeft".equals(sortColumn)) {
				orderBy = "E.EYESIGHTLEFT_" + a_x;
			}

			if ("eyesightRight".equals(sortColumn)) {
				orderBy = "E.EYESIGHTRIGHT_" + a_x;
			}

			if ("earLeft".equals(sortColumn)) {
				orderBy = "E.EARLEFT_" + a_x;
			}

			if ("earRight".equals(sortColumn)) {
				orderBy = "E.EARRIGHT_" + a_x;
			}

			if ("tooth".equals(sortColumn)) {
				orderBy = "E.TOOTH_" + a_x;
			}

			if ("saprodontia".equals(sortColumn)) {
				orderBy = "E.SAPRODONTIA_" + a_x;
			}

			if ("head".equals(sortColumn)) {
				orderBy = "E.HEAD_" + a_x;
			}

			if ("thorax".equals(sortColumn)) {
				orderBy = "E.THORAX_" + a_x;
			}

			if ("spine".equals(sortColumn)) {
				orderBy = "E.SPINE_" + a_x;
			}

			if ("pharyngeal".equals(sortColumn)) {
				orderBy = "E.PHARYNGEAL_" + a_x;
			}

			if ("cardiopulmonary".equals(sortColumn)) {
				orderBy = "E.CARDIOPULMONARY_" + a_x;
			}

			if ("hepatolienal".equals(sortColumn)) {
				orderBy = "E.HEPATOLIENAL_" + a_x;
			}

			if ("pudendum".equals(sortColumn)) {
				orderBy = "E.PUDENDUM_" + a_x;
			}

			if ("skin".equals(sortColumn)) {
				orderBy = "E.SKIN_" + a_x;
			}

			if ("hemoglobin".equals(sortColumn)) {
				orderBy = "E.HEMOGLOBIN_" + a_x;
			}

			if ("alt".equals(sortColumn)) {
				orderBy = "E.ALT_" + a_x;
			}

			if ("type".equals(sortColumn)) {
				orderBy = "E.TYPE_" + a_x;
			}

			if ("checkDate".equals(sortColumn)) {
				orderBy = "E.CHECKDATE_" + a_x;
			}

			if ("checkDoctor".equals(sortColumn)) {
				orderBy = "E.CHECKDOCTOR_" + a_x;
			}

			if ("checkDoctorId".equals(sortColumn)) {
				orderBy = "E.CHECKDOCTORID_" + a_x;
			}

			if ("checkResult".equals(sortColumn)) {
				orderBy = "E.CHECKRESULT_" + a_x;
			}

			if ("checkOrganization".equals(sortColumn)) {
				orderBy = "E.CHECKORGANIZATION_" + a_x;
			}

			if ("checkOrganizationId".equals(sortColumn)) {
				orderBy = "E.CHECKORGANIZATIONID_" + a_x;
			}

			if ("doctorSuggest".equals(sortColumn)) {
				orderBy = "E.DOCTORSUGGEST_" + a_x;
			}

			if ("year".equals(sortColumn)) {
				orderBy = "E.YEAR_" + a_x;
			}

			if ("month".equals(sortColumn)) {
				orderBy = "E.MONTH_" + a_x;
			}

			if ("remark".equals(sortColumn)) {
				orderBy = "E.REMARK_" + a_x;
			}

			if ("confirmBy".equals(sortColumn)) {
				orderBy = "E.CONFIRMBY_" + a_x;
			}

			if ("confirmTime".equals(sortColumn)) {
				orderBy = "E.CONFIRMTIME_" + a_x;
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

	public String getPharyngeal() {
		return pharyngeal;
	}

	public String getPharyngealLike() {
		if (pharyngealLike != null && pharyngealLike.trim().length() > 0) {
			if (!pharyngealLike.startsWith("%")) {
				pharyngealLike = "%" + pharyngealLike;
			}
			if (!pharyngealLike.endsWith("%")) {
				pharyngealLike = pharyngealLike + "%";
			}
		}
		return pharyngealLike;
	}

	public String getPudendum() {
		return pudendum;
	}

	public String getPudendumLike() {
		if (pudendumLike != null && pudendumLike.trim().length() > 0) {
			if (!pudendumLike.startsWith("%")) {
				pudendumLike = "%" + pudendumLike;
			}
			if (!pudendumLike.endsWith("%")) {
				pudendumLike = pudendumLike + "%";
			}
		}
		return pudendumLike;
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

	public Integer getSaprodontia() {
		return saprodontia;
	}

	public Integer getSaprodontiaGreaterThanOrEqual() {
		return saprodontiaGreaterThanOrEqual;
	}

	public Integer getSaprodontiaLessThanOrEqual() {
		return saprodontiaLessThanOrEqual;
	}

	public String getSex() {
		return sex;
	}

	public String getSkin() {
		return skin;
	}

	public String getSkinLike() {
		if (skinLike != null && skinLike.trim().length() > 0) {
			if (!skinLike.startsWith("%")) {
				skinLike = "%" + skinLike;
			}
			if (!skinLike.endsWith("%")) {
				skinLike = skinLike + "%";
			}
		}
		return skinLike;
	}

	public String getSpine() {
		return spine;
	}

	public String getSpineLike() {
		if (spineLike != null && spineLike.trim().length() > 0) {
			if (!spineLike.startsWith("%")) {
				spineLike = "%" + spineLike;
			}
			if (!spineLike.endsWith("%")) {
				spineLike = spineLike + "%";
			}
		}
		return spineLike;
	}

	public List<String> getTenantIds() {
		return tenantIds;
	}

	public String getThorax() {
		return thorax;
	}

	public String getThoraxLike() {
		if (thoraxLike != null && thoraxLike.trim().length() > 0) {
			if (!thoraxLike.startsWith("%")) {
				thoraxLike = "%" + thoraxLike;
			}
			if (!thoraxLike.endsWith("%")) {
				thoraxLike = thoraxLike + "%";
			}
		}
		return thoraxLike;
	}

	public String getType() {
		return type;
	}

	public List<String> getTypes() {
		return types;
	}

	public String getWeightEvaluate() {
		return weightEvaluate;
	}

	public String getWeightEvaluateLike() {
		if (weightEvaluateLike != null && weightEvaluateLike.trim().length() > 0) {
			if (!weightEvaluateLike.startsWith("%")) {
				weightEvaluateLike = "%" + weightEvaluateLike;
			}
			if (!weightEvaluateLike.endsWith("%")) {
				weightEvaluateLike = weightEvaluateLike + "%";
			}
		}
		return weightEvaluateLike;
	}

	public Double getWeightGreaterThanOrEqual() {
		return weightGreaterThanOrEqual;
	}

	public Double getWeightHeightPercentGreaterThanOrEqual() {
		return weightHeightPercentGreaterThanOrEqual;
	}

	public Double getWeightHeightPercentLessThanOrEqual() {
		return weightHeightPercentLessThanOrEqual;
	}

	public Double getWeightLessThanOrEqual() {
		return weightLessThanOrEqual;
	}

	public Integer getWeightLevel() {
		return weightLevel;
	}

	public Integer getWeightLevelGreaterThanOrEqual() {
		return weightLevelGreaterThanOrEqual;
	}

	public Integer getWeightLevelLessThanOrEqual() {
		return weightLevelLessThanOrEqual;
	}

	public Integer getYear() {
		return year;
	}

	public Integer getYearGreaterThanOrEqual() {
		return yearGreaterThanOrEqual;
	}

	public Integer getYearLessThanOrEqual() {
		return yearLessThanOrEqual;
	}

	public List<Integer> getYears() {
		return years;
	}

	public MedicalExaminationQuery gradeId(String gradeId) {
		if (gradeId == null) {
			throw new RuntimeException("gradeId is null");
		}
		this.gradeId = gradeId;
		return this;
	}

	public MedicalExaminationQuery gradeIds(List<String> gradeIds) {
		if (gradeIds == null) {
			throw new RuntimeException("gradeIds is empty ");
		}
		this.gradeIds = gradeIds;
		return this;
	}

	public MedicalExaminationQuery head(String head) {
		if (head == null) {
			throw new RuntimeException("head is null");
		}
		this.head = head;
		return this;
	}

	public MedicalExaminationQuery headLike(String headLike) {
		if (headLike == null) {
			throw new RuntimeException("head is null");
		}
		this.headLike = headLike;
		return this;
	}

	public MedicalExaminationQuery heightEvaluate(String heightEvaluate) {
		if (heightEvaluate == null) {
			throw new RuntimeException("heightEvaluate is null");
		}
		this.heightEvaluate = heightEvaluate;
		return this;
	}

	public MedicalExaminationQuery heightEvaluateLike(String heightEvaluateLike) {
		if (heightEvaluateLike == null) {
			throw new RuntimeException("heightEvaluate is null");
		}
		this.heightEvaluateLike = heightEvaluateLike;
		return this;
	}

	public MedicalExaminationQuery heightGreaterThanOrEqual(Double heightGreaterThanOrEqual) {
		if (heightGreaterThanOrEqual == null) {
			throw new RuntimeException("height is null");
		}
		this.heightGreaterThanOrEqual = heightGreaterThanOrEqual;
		return this;
	}

	public MedicalExaminationQuery heightLessThanOrEqual(Double heightLessThanOrEqual) {
		if (heightLessThanOrEqual == null) {
			throw new RuntimeException("height is null");
		}
		this.heightLessThanOrEqual = heightLessThanOrEqual;
		return this;
	}

	public MedicalExaminationQuery heightLevel(Integer heightLevel) {
		if (heightLevel == null) {
			throw new RuntimeException("heightLevel is null");
		}
		this.heightLevel = heightLevel;
		return this;
	}

	public MedicalExaminationQuery heightLevelGreaterThanOrEqual(Integer heightLevelGreaterThanOrEqual) {
		if (heightLevelGreaterThanOrEqual == null) {
			throw new RuntimeException("heightLevel is null");
		}
		this.heightLevelGreaterThanOrEqual = heightLevelGreaterThanOrEqual;
		return this;
	}

	public MedicalExaminationQuery heightLevelLessThanOrEqual(Integer heightLevelLessThanOrEqual) {
		if (heightLevelLessThanOrEqual == null) {
			throw new RuntimeException("heightLevel is null");
		}
		this.heightLevelLessThanOrEqual = heightLevelLessThanOrEqual;
		return this;
	}

	public MedicalExaminationQuery hemoglobin(String hemoglobin) {
		if (hemoglobin == null) {
			throw new RuntimeException("hemoglobin is null");
		}
		this.hemoglobin = hemoglobin;
		return this;
	}

	public MedicalExaminationQuery hemoglobinLike(String hemoglobinLike) {
		if (hemoglobinLike == null) {
			throw new RuntimeException("hemoglobin is null");
		}
		this.hemoglobinLike = hemoglobinLike;
		return this;
	}

	public MedicalExaminationQuery hepatolienal(String hepatolienal) {
		if (hepatolienal == null) {
			throw new RuntimeException("hepatolienal is null");
		}
		this.hepatolienal = hepatolienal;
		return this;
	}

	public MedicalExaminationQuery hepatolienalLike(String hepatolienalLike) {
		if (hepatolienalLike == null) {
			throw new RuntimeException("hepatolienal is null");
		}
		this.hepatolienalLike = hepatolienalLike;
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
		addColumn("sex", "SEX_");
		addColumn("height", "HEIGHT_");
		addColumn("heightEvaluate", "HEIGHTEVALUATE_");
		addColumn("weight", "WEIGHT_");
		addColumn("weightEvaluate", "WEIGHTEVALUATE_");
		addColumn("weightHeightPercent", "WEIGHTHEIGHTPERCENT_");
		addColumn("allergy", "ALLERGY_");
		addColumn("eyeLeft", "EYELEFT_");
		addColumn("eyeRight", "EYERIGHT_");
		addColumn("eyesightLeft", "EYESIGHTLEFT_");
		addColumn("eyesightRight", "EYESIGHTRIGHT_");
		addColumn("earLeft", "EARLEFT_");
		addColumn("earRight", "EARRIGHT_");
		addColumn("tooth", "TOOTH_");
		addColumn("saprodontia", "SAPRODONTIA_");
		addColumn("head", "HEAD_");
		addColumn("thorax", "THORAX_");
		addColumn("spine", "SPINE_");
		addColumn("pharyngeal", "PHARYNGEAL_");
		addColumn("cardiopulmonary", "CARDIOPULMONARY_");
		addColumn("hepatolienal", "HEPATOLIENAL_");
		addColumn("pudendum", "PUDENDUM_");
		addColumn("skin", "SKIN_");
		addColumn("hemoglobin", "HEMOGLOBIN_");
		addColumn("alt", "ALT_");
		addColumn("type", "TYPE_");
		addColumn("checkDate", "CHECKDATE_");
		addColumn("checkDoctor", "CHECKDOCTOR_");
		addColumn("checkDoctorId", "CHECKDOCTORID_");
		addColumn("checkResult", "CHECKRESULT_");
		addColumn("checkOrganization", "CHECKORGANIZATION_");
		addColumn("checkOrganizationId", "CHECKORGANIZATIONID_");
		addColumn("doctorSuggest", "DOCTORSUGGEST_");
		addColumn("year", "YEAR_");
		addColumn("month", "MONTH_");
		addColumn("remark", "REMARK_");
		addColumn("confirmBy", "CONFIRMBY_");
		addColumn("confirmTime", "CONFIRMTIME_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
	}

	public MedicalExaminationQuery month(Integer month) {
		if (month == null) {
			throw new RuntimeException("month is null");
		}
		this.month = month;
		return this;
	}

	public MedicalExaminationQuery monthGreaterThanOrEqual(Integer monthGreaterThanOrEqual) {
		if (monthGreaterThanOrEqual == null) {
			throw new RuntimeException("month is null");
		}
		this.monthGreaterThanOrEqual = monthGreaterThanOrEqual;
		return this;
	}

	public MedicalExaminationQuery monthLessThanOrEqual(Integer monthLessThanOrEqual) {
		if (monthLessThanOrEqual == null) {
			throw new RuntimeException("month is null");
		}
		this.monthLessThanOrEqual = monthLessThanOrEqual;
		return this;
	}

	public MedicalExaminationQuery months(List<Integer> months) {
		if (months == null) {
			throw new RuntimeException("months is empty ");
		}
		this.months = months;
		return this;
	}

	public MedicalExaminationQuery nameLike(String nameLike) {
		if (nameLike == null) {
			throw new RuntimeException("name is null");
		}
		this.nameLike = nameLike;
		return this;
	}

	public MedicalExaminationQuery personId(String personId) {
		if (personId == null) {
			throw new RuntimeException("personId is null");
		}
		this.personId = personId;
		return this;
	}

	public MedicalExaminationQuery personIds(List<String> personIds) {
		if (personIds == null) {
			throw new RuntimeException("personIds is empty ");
		}
		this.personIds = personIds;
		return this;
	}

	public MedicalExaminationQuery pharyngeal(String pharyngeal) {
		if (pharyngeal == null) {
			throw new RuntimeException("pharyngeal is null");
		}
		this.pharyngeal = pharyngeal;
		return this;
	}

	public MedicalExaminationQuery pharyngealLike(String pharyngealLike) {
		if (pharyngealLike == null) {
			throw new RuntimeException("pharyngeal is null");
		}
		this.pharyngealLike = pharyngealLike;
		return this;
	}

	public MedicalExaminationQuery pudendum(String pudendum) {
		if (pudendum == null) {
			throw new RuntimeException("pudendum is null");
		}
		this.pudendum = pudendum;
		return this;
	}

	public MedicalExaminationQuery pudendumLike(String pudendumLike) {
		if (pudendumLike == null) {
			throw new RuntimeException("pudendum is null");
		}
		this.pudendumLike = pudendumLike;
		return this;
	}

	public MedicalExaminationQuery remarkLike(String remarkLike) {
		if (remarkLike == null) {
			throw new RuntimeException("remark is null");
		}
		this.remarkLike = remarkLike;
		return this;
	}

	public MedicalExaminationQuery saprodontia(Integer saprodontia) {
		if (saprodontia == null) {
			throw new RuntimeException("saprodontia is null");
		}
		this.saprodontia = saprodontia;
		return this;
	}

	public MedicalExaminationQuery saprodontiaGreaterThanOrEqual(Integer saprodontiaGreaterThanOrEqual) {
		if (saprodontiaGreaterThanOrEqual == null) {
			throw new RuntimeException("saprodontia is null");
		}
		this.saprodontiaGreaterThanOrEqual = saprodontiaGreaterThanOrEqual;
		return this;
	}

	public MedicalExaminationQuery saprodontiaLessThanOrEqual(Integer saprodontiaLessThanOrEqual) {
		if (saprodontiaLessThanOrEqual == null) {
			throw new RuntimeException("saprodontia is null");
		}
		this.saprodontiaLessThanOrEqual = saprodontiaLessThanOrEqual;
		return this;
	}

	public void setAllergy(String allergy) {
		this.allergy = allergy;
	}

	public void setAllergyLike(String allergyLike) {
		this.allergyLike = allergyLike;
	}

	public void setAlt(String alt) {
		this.alt = alt;
	}

	public void setAltLike(String altLike) {
		this.altLike = altLike;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public void setBmiGreaterThanOrEqual(Double bmiGreaterThanOrEqual) {
		this.bmiGreaterThanOrEqual = bmiGreaterThanOrEqual;
	}

	public void setBmiLessThanOrEqual(Double bmiLessThanOrEqual) {
		this.bmiLessThanOrEqual = bmiLessThanOrEqual;
	}

	public void setCardiopulmonary(String cardiopulmonary) {
		this.cardiopulmonary = cardiopulmonary;
	}

	public void setCardiopulmonaryLike(String cardiopulmonaryLike) {
		this.cardiopulmonaryLike = cardiopulmonaryLike;
	}

	public void setCheckDateGreaterThanOrEqual(Date checkDateGreaterThanOrEqual) {
		this.checkDateGreaterThanOrEqual = checkDateGreaterThanOrEqual;
	}

	public void setCheckDateLessThanOrEqual(Date checkDateLessThanOrEqual) {
		this.checkDateLessThanOrEqual = checkDateLessThanOrEqual;
	}

	public void setCheckDoctor(String checkDoctor) {
		this.checkDoctor = checkDoctor;
	}

	public void setCheckDoctorId(String checkDoctorId) {
		this.checkDoctorId = checkDoctorId;
	}

	public void setCheckDoctorIdLike(String checkDoctorIdLike) {
		this.checkDoctorIdLike = checkDoctorIdLike;
	}

	public void setCheckDoctorLike(String checkDoctorLike) {
		this.checkDoctorLike = checkDoctorLike;
	}

	public void setCheckId(String checkId) {
		this.checkId = checkId;
	}

	public void setCheckOrganization(String checkOrganization) {
		this.checkOrganization = checkOrganization;
	}

	public void setCheckOrganizationId(Long checkOrganizationId) {
		this.checkOrganizationId = checkOrganizationId;
	}

	public void setCheckOrganizationIds(List<Long> checkOrganizationIds) {
		this.checkOrganizationIds = checkOrganizationIds;
	}

	public void setCheckOrganizationLike(String checkOrganizationLike) {
		this.checkOrganizationLike = checkOrganizationLike;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}

	public void setCheckResultLike(String checkResultLike) {
		this.checkResultLike = checkResultLike;
	}

	public void setConfirmBy(String confirmBy) {
		this.confirmBy = confirmBy;
	}

	public void setConfirmByLike(String confirmByLike) {
		this.confirmByLike = confirmByLike;
	}

	public void setConfirmTimeGreaterThanOrEqual(Date confirmTimeGreaterThanOrEqual) {
		this.confirmTimeGreaterThanOrEqual = confirmTimeGreaterThanOrEqual;
	}

	public void setConfirmTimeLessThanOrEqual(Date confirmTimeLessThanOrEqual) {
		this.confirmTimeLessThanOrEqual = confirmTimeLessThanOrEqual;
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setDoctorSuggest(String doctorSuggest) {
		this.doctorSuggest = doctorSuggest;
	}

	public void setDoctorSuggestLike(String doctorSuggestLike) {
		this.doctorSuggestLike = doctorSuggestLike;
	}

	public void setEarLeft(String earLeft) {
		this.earLeft = earLeft;
	}

	public void setEarLeftLike(String earLeftLike) {
		this.earLeftLike = earLeftLike;
	}

	public void setEarRight(String earRight) {
		this.earRight = earRight;
	}

	public void setEarRightLike(String earRightLike) {
		this.earRightLike = earRightLike;
	}

	public void setEyeLeft(String eyeLeft) {
		this.eyeLeft = eyeLeft;
	}

	public void setEyeLeftLike(String eyeLeftLike) {
		this.eyeLeftLike = eyeLeftLike;
	}

	public void setEyeRight(String eyeRight) {
		this.eyeRight = eyeRight;
	}

	public void setEyeRightLike(String eyeRightLike) {
		this.eyeRightLike = eyeRightLike;
	}

	public void setEyesightLeftGreaterThanOrEqual(Double eyesightLeftGreaterThanOrEqual) {
		this.eyesightLeftGreaterThanOrEqual = eyesightLeftGreaterThanOrEqual;
	}

	public void setEyesightLeftLessThanOrEqual(Double eyesightLeftLessThanOrEqual) {
		this.eyesightLeftLessThanOrEqual = eyesightLeftLessThanOrEqual;
	}

	public void setEyesightRightGreaterThanOrEqual(Double eyesightRightGreaterThanOrEqual) {
		this.eyesightRightGreaterThanOrEqual = eyesightRightGreaterThanOrEqual;
	}

	public void setEyesightRightLessThanOrEqual(Double eyesightRightLessThanOrEqual) {
		this.eyesightRightLessThanOrEqual = eyesightRightLessThanOrEqual;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public void setGradeIds(List<String> gradeIds) {
		this.gradeIds = gradeIds;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public void setHeadLike(String headLike) {
		this.headLike = headLike;
	}

	public void setHeightEvaluate(String heightEvaluate) {
		this.heightEvaluate = heightEvaluate;
	}

	public void setHeightEvaluateLike(String heightEvaluateLike) {
		this.heightEvaluateLike = heightEvaluateLike;
	}

	public void setHeightGreaterThanOrEqual(Double heightGreaterThanOrEqual) {
		this.heightGreaterThanOrEqual = heightGreaterThanOrEqual;
	}

	public void setHeightLessThanOrEqual(Double heightLessThanOrEqual) {
		this.heightLessThanOrEqual = heightLessThanOrEqual;
	}

	public void setHeightLevel(Integer heightLevel) {
		this.heightLevel = heightLevel;
	}

	public void setHeightLevelGreaterThanOrEqual(Integer heightLevelGreaterThanOrEqual) {
		this.heightLevelGreaterThanOrEqual = heightLevelGreaterThanOrEqual;
	}

	public void setHeightLevelLessThanOrEqual(Integer heightLevelLessThanOrEqual) {
		this.heightLevelLessThanOrEqual = heightLevelLessThanOrEqual;
	}

	public void setHemoglobin(String hemoglobin) {
		this.hemoglobin = hemoglobin;
	}

	public void setHemoglobinLike(String hemoglobinLike) {
		this.hemoglobinLike = hemoglobinLike;
	}

	public void setHepatolienal(String hepatolienal) {
		this.hepatolienal = hepatolienal;
	}

	public void setHepatolienalLike(String hepatolienalLike) {
		this.hepatolienalLike = hepatolienalLike;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public void setMonthGreaterThanOrEqual(Integer monthGreaterThanOrEqual) {
		this.monthGreaterThanOrEqual = monthGreaterThanOrEqual;
	}

	public void setMonthLessThanOrEqual(Integer monthLessThanOrEqual) {
		this.monthLessThanOrEqual = monthLessThanOrEqual;
	}

	public void setMonths(List<Integer> months) {
		this.months = months;
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

	public void setPharyngeal(String pharyngeal) {
		this.pharyngeal = pharyngeal;
	}

	public void setPharyngealLike(String pharyngealLike) {
		this.pharyngealLike = pharyngealLike;
	}

	public void setPudendum(String pudendum) {
		this.pudendum = pudendum;
	}

	public void setPudendumLike(String pudendumLike) {
		this.pudendumLike = pudendumLike;
	}

	public void setRemarkLike(String remarkLike) {
		this.remarkLike = remarkLike;
	}

	public void setSaprodontia(Integer saprodontia) {
		this.saprodontia = saprodontia;
	}

	public void setSaprodontiaGreaterThanOrEqual(Integer saprodontiaGreaterThanOrEqual) {
		this.saprodontiaGreaterThanOrEqual = saprodontiaGreaterThanOrEqual;
	}

	public void setSaprodontiaLessThanOrEqual(Integer saprodontiaLessThanOrEqual) {
		this.saprodontiaLessThanOrEqual = saprodontiaLessThanOrEqual;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setSkin(String skin) {
		this.skin = skin;
	}

	public void setSkinLike(String skinLike) {
		this.skinLike = skinLike;
	}

	public void setSpine(String spine) {
		this.spine = spine;
	}

	public void setSpineLike(String spineLike) {
		this.spineLike = spineLike;
	}

	public void setTenantIds(List<String> tenantIds) {
		this.tenantIds = tenantIds;
	}

	public void setThorax(String thorax) {
		this.thorax = thorax;
	}

	public void setThoraxLike(String thoraxLike) {
		this.thoraxLike = thoraxLike;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}

	public void setWeightEvaluate(String weightEvaluate) {
		this.weightEvaluate = weightEvaluate;
	}

	public void setWeightEvaluateLike(String weightEvaluateLike) {
		this.weightEvaluateLike = weightEvaluateLike;
	}

	public void setWeightGreaterThanOrEqual(Double weightGreaterThanOrEqual) {
		this.weightGreaterThanOrEqual = weightGreaterThanOrEqual;
	}

	public void setWeightHeightPercentGreaterThanOrEqual(Double weightHeightPercentGreaterThanOrEqual) {
		this.weightHeightPercentGreaterThanOrEqual = weightHeightPercentGreaterThanOrEqual;
	}

	public void setWeightHeightPercentLessThanOrEqual(Double weightHeightPercentLessThanOrEqual) {
		this.weightHeightPercentLessThanOrEqual = weightHeightPercentLessThanOrEqual;
	}

	public void setWeightLessThanOrEqual(Double weightLessThanOrEqual) {
		this.weightLessThanOrEqual = weightLessThanOrEqual;
	}

	public void setWeightLevel(Integer weightLevel) {
		this.weightLevel = weightLevel;
	}

	public void setWeightLevelGreaterThanOrEqual(Integer weightLevelGreaterThanOrEqual) {
		this.weightLevelGreaterThanOrEqual = weightLevelGreaterThanOrEqual;
	}

	public void setWeightLevelLessThanOrEqual(Integer weightLevelLessThanOrEqual) {
		this.weightLevelLessThanOrEqual = weightLevelLessThanOrEqual;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public void setYearGreaterThanOrEqual(Integer yearGreaterThanOrEqual) {
		this.yearGreaterThanOrEqual = yearGreaterThanOrEqual;
	}

	public void setYearLessThanOrEqual(Integer yearLessThanOrEqual) {
		this.yearLessThanOrEqual = yearLessThanOrEqual;
	}

	public void setYears(List<Integer> years) {
		this.years = years;
	}

	public MedicalExaminationQuery sex(String sex) {
		if (sex == null) {
			throw new RuntimeException("sex is null");
		}
		this.sex = sex;
		return this;
	}

	public MedicalExaminationQuery skin(String skin) {
		if (skin == null) {
			throw new RuntimeException("skin is null");
		}
		this.skin = skin;
		return this;
	}

	public MedicalExaminationQuery skinLike(String skinLike) {
		if (skinLike == null) {
			throw new RuntimeException("skin is null");
		}
		this.skinLike = skinLike;
		return this;
	}

	public MedicalExaminationQuery spine(String spine) {
		if (spine == null) {
			throw new RuntimeException("spine is null");
		}
		this.spine = spine;
		return this;
	}

	public MedicalExaminationQuery spineLike(String spineLike) {
		if (spineLike == null) {
			throw new RuntimeException("spine is null");
		}
		this.spineLike = spineLike;
		return this;
	}

	public MedicalExaminationQuery tenantId(String tenantId) {
		if (tenantId == null) {
			throw new RuntimeException("tenantId is null");
		}
		this.tenantId = tenantId;
		return this;
	}

	public MedicalExaminationQuery tenantIds(List<String> tenantIds) {
		if (tenantIds == null) {
			throw new RuntimeException("tenantIds is empty ");
		}
		this.tenantIds = tenantIds;
		return this;
	}

	public MedicalExaminationQuery thorax(String thorax) {
		if (thorax == null) {
			throw new RuntimeException("thorax is null");
		}
		this.thorax = thorax;
		return this;
	}

	public MedicalExaminationQuery thoraxLike(String thoraxLike) {
		if (thoraxLike == null) {
			throw new RuntimeException("thorax is null");
		}
		this.thoraxLike = thoraxLike;
		return this;
	}

	public MedicalExaminationQuery type(String type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		this.type = type;
		return this;
	}

	public MedicalExaminationQuery types(List<String> types) {
		if (types == null) {
			throw new RuntimeException("types is empty ");
		}
		this.types = types;
		return this;
	}

	public MedicalExaminationQuery weightEvaluate(String weightEvaluate) {
		if (weightEvaluate == null) {
			throw new RuntimeException("weightEvaluate is null");
		}
		this.weightEvaluate = weightEvaluate;
		return this;
	}

	public MedicalExaminationQuery weightEvaluateLike(String weightEvaluateLike) {
		if (weightEvaluateLike == null) {
			throw new RuntimeException("weightEvaluate is null");
		}
		this.weightEvaluateLike = weightEvaluateLike;
		return this;
	}

	public MedicalExaminationQuery weightGreaterThanOrEqual(Double weightGreaterThanOrEqual) {
		if (weightGreaterThanOrEqual == null) {
			throw new RuntimeException("weight is null");
		}
		this.weightGreaterThanOrEqual = weightGreaterThanOrEqual;
		return this;
	}

	public MedicalExaminationQuery weightHeightPercentGreaterThanOrEqual(Double weightHeightPercentGreaterThanOrEqual) {
		if (weightHeightPercentGreaterThanOrEqual == null) {
			throw new RuntimeException("weightHeightPercent is null");
		}
		this.weightHeightPercentGreaterThanOrEqual = weightHeightPercentGreaterThanOrEqual;
		return this;
	}

	public MedicalExaminationQuery weightHeightPercentLessThanOrEqual(Double weightHeightPercentLessThanOrEqual) {
		if (weightHeightPercentLessThanOrEqual == null) {
			throw new RuntimeException("weightHeightPercent is null");
		}
		this.weightHeightPercentLessThanOrEqual = weightHeightPercentLessThanOrEqual;
		return this;
	}

	public MedicalExaminationQuery weightLessThanOrEqual(Double weightLessThanOrEqual) {
		if (weightLessThanOrEqual == null) {
			throw new RuntimeException("weight is null");
		}
		this.weightLessThanOrEqual = weightLessThanOrEqual;
		return this;
	}

	public MedicalExaminationQuery weightLevel(Integer weightLevel) {
		if (weightLevel == null) {
			throw new RuntimeException("weightLevel is null");
		}
		this.weightLevel = weightLevel;
		return this;
	}

	public MedicalExaminationQuery weightLevelGreaterThanOrEqual(Integer weightLevelGreaterThanOrEqual) {
		if (weightLevelGreaterThanOrEqual == null) {
			throw new RuntimeException("weightLevel is null");
		}
		this.weightLevelGreaterThanOrEqual = weightLevelGreaterThanOrEqual;
		return this;
	}

	public MedicalExaminationQuery weightLevelLessThanOrEqual(Integer weightLevelLessThanOrEqual) {
		if (weightLevelLessThanOrEqual == null) {
			throw new RuntimeException("weightLevel is null");
		}
		this.weightLevelLessThanOrEqual = weightLevelLessThanOrEqual;
		return this;
	}

	public MedicalExaminationQuery year(Integer year) {
		if (year == null) {
			throw new RuntimeException("year is null");
		}
		this.year = year;
		return this;
	}

	public MedicalExaminationQuery yearGreaterThanOrEqual(Integer yearGreaterThanOrEqual) {
		if (yearGreaterThanOrEqual == null) {
			throw new RuntimeException("year is null");
		}
		this.yearGreaterThanOrEqual = yearGreaterThanOrEqual;
		return this;
	}

	public MedicalExaminationQuery yearLessThanOrEqual(Integer yearLessThanOrEqual) {
		if (yearLessThanOrEqual == null) {
			throw new RuntimeException("year is null");
		}
		this.yearLessThanOrEqual = yearLessThanOrEqual;
		return this;
	}

	public MedicalExaminationQuery years(List<Integer> years) {
		if (years == null) {
			throw new RuntimeException("years is empty ");
		}
		this.years = years;
		return this;
	}

}