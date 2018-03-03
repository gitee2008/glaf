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

public class GrowthStandardQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected Integer age;
	protected Integer ageOfTheMoon;
	protected Integer ageOfTheMoonGreaterThanOrEqual;
	protected Integer ageOfTheMoonLessThanOrEqual;
	protected Integer month;
	protected String sex;
	protected String type;
	protected String standardType;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	public GrowthStandardQuery() {

	}

	public GrowthStandardQuery age(Integer age) {
		if (age == null) {
			throw new RuntimeException("age is null");
		}
		this.age = age;
		return this;
	}

	public GrowthStandardQuery ageOfTheMoon(Integer ageOfTheMoon) {
		if (ageOfTheMoon == null) {
			throw new RuntimeException("ageOfTheMoon is null");
		}
		this.ageOfTheMoon = ageOfTheMoon;
		return this;
	}

	public GrowthStandardQuery ageOfTheMoonGreaterThanOrEqual(Integer ageOfTheMoonGreaterThanOrEqual) {
		if (ageOfTheMoonGreaterThanOrEqual == null) {
			throw new RuntimeException("ageOfTheMoonGreaterThanOrEqual is null");
		}
		this.ageOfTheMoonGreaterThanOrEqual = ageOfTheMoonGreaterThanOrEqual;
		return this;
	}

	public GrowthStandardQuery ageOfTheMoonLessThanOrEqual(Integer ageOfTheMoonLessThanOrEqual) {
		if (ageOfTheMoonLessThanOrEqual == null) {
			throw new RuntimeException("ageOfTheMoonLessThanOrEqual is null");
		}
		this.ageOfTheMoonLessThanOrEqual = ageOfTheMoonLessThanOrEqual;
		return this;
	}

	public Integer getAgeOfTheMoonLessThanOrEqual() {
		return ageOfTheMoonLessThanOrEqual;
	}

	public void setAgeOfTheMoonLessThanOrEqual(Integer ageOfTheMoonLessThanOrEqual) {
		this.ageOfTheMoonLessThanOrEqual = ageOfTheMoonLessThanOrEqual;
	}

	public GrowthStandardQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public GrowthStandardQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public Integer getAge() {
		return age;
	}

	public Integer getAgeOfTheMoon() {
		return ageOfTheMoon;
	}

	public Integer getAgeOfTheMoonGreaterThanOrEqual() {
		return ageOfTheMoonGreaterThanOrEqual;
	}

	public Date getCreateTimeGreaterThanOrEqual() {
		return createTimeGreaterThanOrEqual;
	}

	public Date getCreateTimeLessThanOrEqual() {
		return createTimeLessThanOrEqual;
	}

	public Integer getMonth() {
		return month;
	}

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("age".equals(sortColumn)) {
				orderBy = "E.AGE_" + a_x;
			}

			if ("month".equals(sortColumn)) {
				orderBy = "E.MONTH_" + a_x;
			}

			if ("sex".equals(sortColumn)) {
				orderBy = "E.SEX_" + a_x;
			}

			if ("oneDSDeviation".equals(sortColumn)) {
				orderBy = "E.ONEDSDEVIATION_" + a_x;
			}

			if ("twoDSDeviation".equals(sortColumn)) {
				orderBy = "E.TWODSDEVIATION_" + a_x;
			}

			if ("threeDSDeviation".equals(sortColumn)) {
				orderBy = "E.THREEDSDEVIATION_" + a_x;
			}

			if ("median".equals(sortColumn)) {
				orderBy = "E.MEDIAN_" + a_x;
			}

			if ("negativeOneDSDeviation".equals(sortColumn)) {
				orderBy = "E.NEGATIVEONEDSDEVIATION_" + a_x;
			}

			if ("negativeTwoDSDeviation".equals(sortColumn)) {
				orderBy = "E.NEGATIVETWODSDEVIATION_" + a_x;
			}

			if ("negativeThreeDSDeviation".equals(sortColumn)) {
				orderBy = "E.NEGATIVETHREEDSDEVIATION_" + a_x;
			}

			if ("type".equals(sortColumn)) {
				orderBy = "E.TYPE_" + a_x;
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

	public String getSex() {
		return sex;
	}

	public String getStandardType() {
		return standardType;
	}

	public String getType() {
		return type;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("age", "AGE_");
		addColumn("month", "MONTH_");
		addColumn("sex", "SEX_");
		addColumn("oneDSDeviation", "ONEDSDEVIATION_");
		addColumn("twoDSDeviation", "TWODSDEVIATION_");
		addColumn("threeDSDeviation", "THREEDSDEVIATION_");
		addColumn("median", "MEDIAN_");
		addColumn("negativeOneDSDeviation", "NEGATIVEONEDSDEVIATION_");
		addColumn("negativeTwoDSDeviation", "NEGATIVETWODSDEVIATION_");
		addColumn("negativeThreeDSDeviation", "NEGATIVETHREEDSDEVIATION_");
		addColumn("type", "TYPE_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
	}

	public GrowthStandardQuery month(Integer month) {
		if (month == null) {
			throw new RuntimeException("month is null");
		}
		this.month = month;
		return this;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public void setAgeOfTheMoon(Integer ageOfTheMoon) {
		this.ageOfTheMoon = ageOfTheMoon;
	}

	public void setAgeOfTheMoonGreaterThanOrEqual(Integer ageOfTheMoonGreaterThanOrEqual) {
		this.ageOfTheMoonGreaterThanOrEqual = ageOfTheMoonGreaterThanOrEqual;
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setStandardType(String standardType) {
		this.standardType = standardType;
	}

	public void setType(String type) {
		this.type = type;
	}

	public GrowthStandardQuery sex(String sex) {
		if (sex == null) {
			throw new RuntimeException("sex is null");
		}
		this.sex = sex;
		return this;
	}

	public GrowthStandardQuery standardType(String standardType) {
		if (standardType == null) {
			throw new RuntimeException("standardType is null");
		}
		this.standardType = standardType;
		return this;
	}

	public GrowthStandardQuery type(String type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		this.type = type;
		return this;
	}

}