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

public class TriphopathiaQuery extends DataQuery {
	private static final long serialVersionUID = 1L;

	protected List<String> tenantIds;
	protected String gradeId;
	protected List<String> gradeIds;
	protected String personId;
	protected List<String> personIds;
	protected String name;
	protected String nameLike;
	protected String sex;
	protected String type;
	protected Double heightGreaterThanOrEqual;
	protected Double heightLessThanOrEqual;
	protected Integer heightLevel;
	protected Integer heightLevelGreaterThanOrEqual;
	protected Integer heightLevelLessThanOrEqual;
	protected Double weightGreaterThanOrEqual;
	protected Double weightLessThanOrEqual;
	protected Integer weightLevel;
	protected Integer weightLevelGreaterThanOrEqual;
	protected Integer weightLevelLessThanOrEqual;
	protected Double weightHeightPercentGreaterThanOrEqual;
	protected Double weightHeightPercentLessThanOrEqual;
	protected Double bmiGreaterThanOrEqual;
	protected Double bmiLessThanOrEqual;
	protected Double bmiIndexGreaterThanOrEqual;
	protected Double bmiIndexLessThanOrEqual;
	protected Integer ageOfTheMoon;
	protected String remarkLike;
	protected String checkDoctorId;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	public TriphopathiaQuery() {

	}

	public TriphopathiaQuery ageOfTheMoon(Integer ageOfTheMoon) {
		if (ageOfTheMoon == null) {
			throw new RuntimeException("ageOfTheMoon is null");
		}
		this.ageOfTheMoon = ageOfTheMoon;
		return this;
	}

	public TriphopathiaQuery bmiGreaterThanOrEqual(Double bmiGreaterThanOrEqual) {
		if (bmiGreaterThanOrEqual == null) {
			throw new RuntimeException("bmi is null");
		}
		this.bmiGreaterThanOrEqual = bmiGreaterThanOrEqual;
		return this;
	}

	public TriphopathiaQuery bmiIndexGreaterThanOrEqual(Double bmiIndexGreaterThanOrEqual) {
		if (bmiIndexGreaterThanOrEqual == null) {
			throw new RuntimeException("bmiIndex is null");
		}
		this.bmiIndexGreaterThanOrEqual = bmiIndexGreaterThanOrEqual;
		return this;
	}

	public TriphopathiaQuery bmiIndexLessThanOrEqual(Double bmiIndexLessThanOrEqual) {
		if (bmiIndexLessThanOrEqual == null) {
			throw new RuntimeException("bmiIndex is null");
		}
		this.bmiIndexLessThanOrEqual = bmiIndexLessThanOrEqual;
		return this;
	}

	public TriphopathiaQuery bmiLessThanOrEqual(Double bmiLessThanOrEqual) {
		if (bmiLessThanOrEqual == null) {
			throw new RuntimeException("bmi is null");
		}
		this.bmiLessThanOrEqual = bmiLessThanOrEqual;
		return this;
	}

	public TriphopathiaQuery checkDoctorId(String checkDoctorId) {
		if (checkDoctorId == null) {
			throw new RuntimeException("checkDoctorId is null");
		}
		this.checkDoctorId = checkDoctorId;
		return this;
	}

	public TriphopathiaQuery createBy(String createBy) {
		if (createBy == null) {
			throw new RuntimeException("createBy is null");
		}
		this.createBy = createBy;
		return this;
	}

	public TriphopathiaQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public TriphopathiaQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public Integer getAgeOfTheMoon() {
		return ageOfTheMoon;
	}

	public Double getBmiGreaterThanOrEqual() {
		return bmiGreaterThanOrEqual;
	}

	public Double getBmiIndexGreaterThanOrEqual() {
		return bmiIndexGreaterThanOrEqual;
	}

	public Double getBmiIndexLessThanOrEqual() {
		return bmiIndexLessThanOrEqual;
	}

	public Double getBmiLessThanOrEqual() {
		return bmiLessThanOrEqual;
	}

	public String getCheckDoctorId() {
		return checkDoctorId;
	}

	public Date getCreateTimeGreaterThanOrEqual() {
		return createTimeGreaterThanOrEqual;
	}

	public Date getCreateTimeLessThanOrEqual() {
		return createTimeLessThanOrEqual;
	}

	public String getGradeId() {
		return gradeId;
	}

	public List<String> getGradeIds() {
		return gradeIds;
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

			if ("type".equals(sortColumn)) {
				orderBy = "E.TYPE_" + a_x;
			}

			if ("height".equals(sortColumn)) {
				orderBy = "E.HEIGHT_" + a_x;
			}

			if ("heightLevel".equals(sortColumn)) {
				orderBy = "E.HEIGHTLEVEL_" + a_x;
			}

			if ("heightEvaluate".equals(sortColumn)) {
				orderBy = "E.HEIGHTEVALUATE_" + a_x;
			}

			if ("weight".equals(sortColumn)) {
				orderBy = "E.WEIGHT_" + a_x;
			}

			if ("weightLevel".equals(sortColumn)) {
				orderBy = "E.WEIGHTLEVEL_" + a_x;
			}

			if ("weightEvaluate".equals(sortColumn)) {
				orderBy = "E.WEIGHTEVALUATE_" + a_x;
			}

			if ("weightHeightPercent".equals(sortColumn)) {
				orderBy = "E.WEIGHTHEIGHTPERCENT_" + a_x;
			}

			if ("bmi".equals(sortColumn)) {
				orderBy = "E.BMI_" + a_x;
			}

			if ("bmiIndex".equals(sortColumn)) {
				orderBy = "E.BMIINDEX_" + a_x;
			}

			if ("bmiEvaluate".equals(sortColumn)) {
				orderBy = "E.BMIEVALUATE_" + a_x;
			}

			if ("symptom".equals(sortColumn)) {
				orderBy = "E.SYMPTOM_" + a_x;
			}

			if ("suggest".equals(sortColumn)) {
				orderBy = "E.SUGGEST_" + a_x;
			}

			if ("result".equals(sortColumn)) {
				orderBy = "E.RESULT_" + a_x;
			}

			if ("evaluate".equals(sortColumn)) {
				orderBy = "E.EVALUATE_" + a_x;
			}

			if ("ageOfTheMoon".equals(sortColumn)) {
				orderBy = "E.AGEOFTHEMOON_" + a_x;
			}

			if ("remark".equals(sortColumn)) {
				orderBy = "E.REMARK_" + a_x;
			}

			if ("checkDoctor".equals(sortColumn)) {
				orderBy = "E.CHECKDOCTOR_" + a_x;
			}

			if ("checkDoctorId".equals(sortColumn)) {
				orderBy = "E.CHECKDOCTORID_" + a_x;
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

	public String getSex() {
		return sex;
	}

	public List<String> getTenantIds() {
		return tenantIds;
	}

	public String getType() {
		return type;
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

	public TriphopathiaQuery gradeId(String gradeId) {
		if (gradeId == null) {
			throw new RuntimeException("gradeId is null");
		}
		this.gradeId = gradeId;
		return this;
	}

	public TriphopathiaQuery gradeIds(List<String> gradeIds) {
		if (gradeIds == null) {
			throw new RuntimeException("gradeIds is empty ");
		}
		this.gradeIds = gradeIds;
		return this;
	}

	public TriphopathiaQuery heightGreaterThanOrEqual(Double heightGreaterThanOrEqual) {
		if (heightGreaterThanOrEqual == null) {
			throw new RuntimeException("height is null");
		}
		this.heightGreaterThanOrEqual = heightGreaterThanOrEqual;
		return this;
	}

	public TriphopathiaQuery heightLessThanOrEqual(Double heightLessThanOrEqual) {
		if (heightLessThanOrEqual == null) {
			throw new RuntimeException("height is null");
		}
		this.heightLessThanOrEqual = heightLessThanOrEqual;
		return this;
	}

	public TriphopathiaQuery heightLevel(Integer heightLevel) {
		if (heightLevel == null) {
			throw new RuntimeException("heightLevel is null");
		}
		this.heightLevel = heightLevel;
		return this;
	}

	public TriphopathiaQuery heightLevelGreaterThanOrEqual(Integer heightLevelGreaterThanOrEqual) {
		if (heightLevelGreaterThanOrEqual == null) {
			throw new RuntimeException("heightLevel is null");
		}
		this.heightLevelGreaterThanOrEqual = heightLevelGreaterThanOrEqual;
		return this;
	}

	public TriphopathiaQuery heightLevelLessThanOrEqual(Integer heightLevelLessThanOrEqual) {
		if (heightLevelLessThanOrEqual == null) {
			throw new RuntimeException("heightLevel is null");
		}
		this.heightLevelLessThanOrEqual = heightLevelLessThanOrEqual;
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
		addColumn("type", "TYPE_");
		addColumn("height", "HEIGHT_");
		addColumn("heightLevel", "HEIGHTLEVEL_");
		addColumn("heightEvaluate", "HEIGHTEVALUATE_");
		addColumn("weight", "WEIGHT_");
		addColumn("weightLevel", "WEIGHTLEVEL_");
		addColumn("weightEvaluate", "WEIGHTEVALUATE_");
		addColumn("weightHeightPercent", "WEIGHTHEIGHTPERCENT_");
		addColumn("bmi", "BMI_");
		addColumn("bmiIndex", "BMIINDEX_");
		addColumn("bmiEvaluate", "BMIEVALUATE_");
		addColumn("symptom", "SYMPTOM_");
		addColumn("suggest", "SUGGEST_");
		addColumn("result", "RESULT_");
		addColumn("evaluate", "EVALUATE_");
		addColumn("ageOfTheMoon", "AGEOFTHEMOON_");
		addColumn("remark", "REMARK_");
		addColumn("checkDoctor", "CHECKDOCTOR_");
		addColumn("checkDoctorId", "CHECKDOCTORID_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
	}

	public TriphopathiaQuery name(String name) {
		if (name == null) {
			throw new RuntimeException("name is null");
		}
		this.name = name;
		return this;
	}

	public TriphopathiaQuery nameLike(String nameLike) {
		if (nameLike == null) {
			throw new RuntimeException("name is null");
		}
		this.nameLike = nameLike;
		return this;
	}

	public TriphopathiaQuery personId(String personId) {
		if (personId == null) {
			throw new RuntimeException("personId is null");
		}
		this.personId = personId;
		return this;
	}

	public TriphopathiaQuery personIds(List<String> personIds) {
		if (personIds == null) {
			throw new RuntimeException("personIds is empty ");
		}
		this.personIds = personIds;
		return this;
	}

	public TriphopathiaQuery remarkLike(String remarkLike) {
		if (remarkLike == null) {
			throw new RuntimeException("remark is null");
		}
		this.remarkLike = remarkLike;
		return this;
	}

	public void setAgeOfTheMoon(Integer ageOfTheMoon) {
		this.ageOfTheMoon = ageOfTheMoon;
	}

	public void setBmiGreaterThanOrEqual(Double bmiGreaterThanOrEqual) {
		this.bmiGreaterThanOrEqual = bmiGreaterThanOrEqual;
	}

	public void setBmiIndexGreaterThanOrEqual(Double bmiIndexGreaterThanOrEqual) {
		this.bmiIndexGreaterThanOrEqual = bmiIndexGreaterThanOrEqual;
	}

	public void setBmiIndexLessThanOrEqual(Double bmiIndexLessThanOrEqual) {
		this.bmiIndexLessThanOrEqual = bmiIndexLessThanOrEqual;
	}

	public void setBmiLessThanOrEqual(Double bmiLessThanOrEqual) {
		this.bmiLessThanOrEqual = bmiLessThanOrEqual;
	}

	public void setCheckDoctorId(String checkDoctorId) {
		this.checkDoctorId = checkDoctorId;
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public void setGradeIds(List<String> gradeIds) {
		this.gradeIds = gradeIds;
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

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setTenantIds(List<String> tenantIds) {
		this.tenantIds = tenantIds;
	}

	public void setType(String type) {
		this.type = type;
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

	public TriphopathiaQuery sex(String sex) {
		if (sex == null) {
			throw new RuntimeException("sex is null");
		}
		this.sex = sex;
		return this;
	}

	public TriphopathiaQuery tenantId(String tenantId) {
		if (tenantId == null) {
			throw new RuntimeException("tenantId is null");
		}
		this.tenantId = tenantId;
		return this;
	}

	public TriphopathiaQuery tenantIds(List<String> tenantIds) {
		if (tenantIds == null) {
			throw new RuntimeException("tenantIds is empty ");
		}
		this.tenantIds = tenantIds;
		return this;
	}

	public TriphopathiaQuery type(String type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		this.type = type;
		return this;
	}

	public TriphopathiaQuery weightGreaterThanOrEqual(Double weightGreaterThanOrEqual) {
		if (weightGreaterThanOrEqual == null) {
			throw new RuntimeException("weight is null");
		}
		this.weightGreaterThanOrEqual = weightGreaterThanOrEqual;
		return this;
	}

	public TriphopathiaQuery weightHeightPercentGreaterThanOrEqual(Double weightHeightPercentGreaterThanOrEqual) {
		if (weightHeightPercentGreaterThanOrEqual == null) {
			throw new RuntimeException("weightHeightPercent is null");
		}
		this.weightHeightPercentGreaterThanOrEqual = weightHeightPercentGreaterThanOrEqual;
		return this;
	}

	public TriphopathiaQuery weightHeightPercentLessThanOrEqual(Double weightHeightPercentLessThanOrEqual) {
		if (weightHeightPercentLessThanOrEqual == null) {
			throw new RuntimeException("weightHeightPercent is null");
		}
		this.weightHeightPercentLessThanOrEqual = weightHeightPercentLessThanOrEqual;
		return this;
	}

	public TriphopathiaQuery weightLessThanOrEqual(Double weightLessThanOrEqual) {
		if (weightLessThanOrEqual == null) {
			throw new RuntimeException("weight is null");
		}
		this.weightLessThanOrEqual = weightLessThanOrEqual;
		return this;
	}

	public TriphopathiaQuery weightLevel(Integer weightLevel) {
		if (weightLevel == null) {
			throw new RuntimeException("weightLevel is null");
		}
		this.weightLevel = weightLevel;
		return this;
	}

	public TriphopathiaQuery weightLevelGreaterThanOrEqual(Integer weightLevelGreaterThanOrEqual) {
		if (weightLevelGreaterThanOrEqual == null) {
			throw new RuntimeException("weightLevel is null");
		}
		this.weightLevelGreaterThanOrEqual = weightLevelGreaterThanOrEqual;
		return this;
	}

	public TriphopathiaQuery weightLevelLessThanOrEqual(Integer weightLevelLessThanOrEqual) {
		if (weightLevelLessThanOrEqual == null) {
			throw new RuntimeException("weightLevel is null");
		}
		this.weightLevelLessThanOrEqual = weightLevelLessThanOrEqual;
		return this;
	}

}