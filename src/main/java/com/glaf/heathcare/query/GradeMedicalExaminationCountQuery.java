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

public class GradeMedicalExaminationCountQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected String checkId;
	protected List<String> checkIds;
	protected List<String> tenantIds;
	protected String gradeId;
	protected List<String> gradeIds;
	protected Integer year;
	protected List<Integer> years;

	public GradeMedicalExaminationCountQuery() {

	}

	public GradeMedicalExaminationCountQuery checkId(String checkId) {
		if (checkId == null) {
			throw new RuntimeException("checkId is null");
		}
		this.checkId = checkId;
		return this;
	}

	public GradeMedicalExaminationCountQuery checkIds(List<String> checkIds) {
		if (checkIds == null) {
			throw new RuntimeException("checkIds is empty ");
		}
		this.checkIds = checkIds;
		return this;
	}

	public String getCheckId() {
		return checkId;
	}

	public List<String> getCheckIds() {
		return checkIds;
	}

	public String getGradeId() {
		return gradeId;
	}

	public List<String> getGradeIds() {
		return gradeIds;
	}

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("checkId".equals(sortColumn)) {
				orderBy = "E.CHECKID_" + a_x;
			}

			if ("tenantId".equals(sortColumn)) {
				orderBy = "E.TENANTID_" + a_x;
			}

			if ("gradeId".equals(sortColumn)) {
				orderBy = "E.GRADEID_" + a_x;
			}

			if ("female".equals(sortColumn)) {
				orderBy = "E.FEMALE_" + a_x;
			}

			if ("male".equals(sortColumn)) {
				orderBy = "E.MALE_" + a_x;
			}

			if ("personCount".equals(sortColumn)) {
				orderBy = "E.PERSONCOUNT_" + a_x;
			}

			if ("checkPerson".equals(sortColumn)) {
				orderBy = "E.CHECKPERSON_" + a_x;
			}

			if ("checkPercent".equals(sortColumn)) {
				orderBy = "E.CHECKPERCENT_" + a_x;
			}

			if ("growthRatePerson".equals(sortColumn)) {
				orderBy = "E.GROWTHRATEPERSON_" + a_x;
			}

			if ("growthRatePersonPercent".equals(sortColumn)) {
				orderBy = "E.GROWTHRATEPERSONPERCENT_" + a_x;
			}

			if ("weightLow".equals(sortColumn)) {
				orderBy = "E.WEIGHTLOW_" + a_x;
			}

			if ("weightSkinny".equals(sortColumn)) {
				orderBy = "E.WEIGHTSKINNY_" + a_x;
			}

			if ("weightRetardation".equals(sortColumn)) {
				orderBy = "E.WEIGHTRETARDATION_" + a_x;
			}

			if ("weightSevereMalnutrition".equals(sortColumn)) {
				orderBy = "E.WEIGHTSEVEREMALNUTRITION_" + a_x;
			}

			if ("overWeight".equals(sortColumn)) {
				orderBy = "E.OVERWEIGHT_" + a_x;
			}

			if ("weightObesityLow".equals(sortColumn)) {
				orderBy = "E.WEIGHTOBESITYLOW_" + a_x;
			}

			if ("weightObesityMid".equals(sortColumn)) {
				orderBy = "E.WEIGHTOBESITYMID_" + a_x;
			}

			if ("weightObesityHigh".equals(sortColumn)) {
				orderBy = "E.WEIGHTOBESITYHIGH_" + a_x;
			}

			if ("anemiaCheck".equals(sortColumn)) {
				orderBy = "E.ANEMIACHECK_" + a_x;
			}

			if ("anemiaCheckNormal".equals(sortColumn)) {
				orderBy = "E.ANEMIACHECKNORMAL_" + a_x;
			}

			if ("anemiaCheckNormalPercent".equals(sortColumn)) {
				orderBy = "E.ANEMIACHECKNORMALPERCENT_" + a_x;
			}

			if ("anemiaLow".equals(sortColumn)) {
				orderBy = "E.ANEMIALOW_" + a_x;
			}

			if ("anemiaMid".equals(sortColumn)) {
				orderBy = "E.ANEMIAMID_" + a_x;
			}

			if ("anemiaHigh".equals(sortColumn)) {
				orderBy = "E.ANEMIAHIGH_" + a_x;
			}

			if ("bloodLead".equals(sortColumn)) {
				orderBy = "E.BLOODLEAD_" + a_x;
			}

			if ("year".equals(sortColumn)) {
				orderBy = "E.YEAR_" + a_x;
			}

			if ("semester".equals(sortColumn)) {
				orderBy = "E.SEMESTER_" + a_x;
			}

			if ("fullDay".equals(sortColumn)) {
				orderBy = "E.FULLDAY_" + a_x;
			}

			if ("sortNo".equals(sortColumn)) {
				orderBy = "E.SORTNO_" + a_x;
			}

		}
		return orderBy;
	}

	public String getTenantId() {
		return tenantId;
	}

	public List<String> getTenantIds() {
		return tenantIds;
	}

	public Integer getYear() {
		return year;
	}

	public List<Integer> getYears() {
		return years;
	}

	public GradeMedicalExaminationCountQuery gradeId(String gradeId) {
		if (gradeId == null) {
			throw new RuntimeException("gradeId is null");
		}
		this.gradeId = gradeId;
		return this;
	}

	public GradeMedicalExaminationCountQuery gradeIds(List<String> gradeIds) {
		if (gradeIds == null) {
			throw new RuntimeException("gradeIds is empty ");
		}
		this.gradeIds = gradeIds;
		return this;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("checkId", "CHECKID_");
		addColumn("tenantId", "TENANTID_");
		addColumn("gradeId", "GRADEID_");
		addColumn("female", "FEMALE_");
		addColumn("male", "MALE_");
		addColumn("personCount", "PERSONCOUNT_");
		addColumn("checkPerson", "CHECKPERSON_");
		addColumn("checkPercent", "CHECKPERCENT_");
		addColumn("growthRatePerson", "GROWTHRATEPERSON_");
		addColumn("growthRatePersonPercent", "GROWTHRATEPERSONPERCENT_");
		addColumn("weightLow", "WEIGHTLOW_");
		addColumn("weightSkinny", "WEIGHTSKINNY_");
		addColumn("weightRetardation", "WEIGHTRETARDATION_");
		addColumn("weightSevereMalnutrition", "WEIGHTSEVEREMALNUTRITION_");
		addColumn("overWeight", "OVERWEIGHT_");
		addColumn("weightObesityLow", "WEIGHTOBESITYLOW_");
		addColumn("weightObesityMid", "WEIGHTOBESITYMID_");
		addColumn("weightObesityHigh", "WEIGHTOBESITYHIGH_");
		addColumn("anemiaCheck", "ANEMIACHECK_");
		addColumn("anemiaCheckNormal", "ANEMIACHECKNORMAL_");
		addColumn("anemiaCheckNormalPercent", "ANEMIACHECKNORMALPERCENT_");
		addColumn("anemiaLow", "ANEMIALOW_");
		addColumn("anemiaMid", "ANEMIAMID_");
		addColumn("anemiaHigh", "ANEMIAHIGH_");
		addColumn("bloodLead", "BLOODLEAD_");
		addColumn("year", "YEAR_");
		addColumn("semester", "SEMESTER_");
		addColumn("fullDay", "FULLDAY_");
		addColumn("sortNo", "SORTNO_");
	}

	public void setCheckId(String checkId) {
		this.checkId = checkId;
	}

	public void setCheckIds(List<String> checkIds) {
		this.checkIds = checkIds;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public void setGradeIds(List<String> gradeIds) {
		this.gradeIds = gradeIds;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setTenantIds(List<String> tenantIds) {
		this.tenantIds = tenantIds;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public void setYears(List<Integer> years) {
		this.years = years;
	}

	public GradeMedicalExaminationCountQuery tenantId(String tenantId) {
		if (tenantId == null) {
			throw new RuntimeException("tenantId is null");
		}
		this.tenantId = tenantId;
		return this;
	}

	public GradeMedicalExaminationCountQuery tenantIds(List<String> tenantIds) {
		if (tenantIds == null) {
			throw new RuntimeException("tenantIds is empty ");
		}
		this.tenantIds = tenantIds;
		return this;
	}

	public GradeMedicalExaminationCountQuery year(Integer year) {
		if (year == null) {
			throw new RuntimeException("year is null");
		}
		this.year = year;
		return this;
	}

	public GradeMedicalExaminationCountQuery years(List<Integer> years) {
		if (years == null) {
			throw new RuntimeException("years is empty ");
		}
		this.years = years;
		return this;
	}

}