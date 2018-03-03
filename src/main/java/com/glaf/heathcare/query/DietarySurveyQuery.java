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

public class DietarySurveyQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected List<String> tenantIds;
	protected Long foodId;
	protected List<Long> foodIds;
	protected String foodNameLike;
	protected Long foodNodeId;
	protected List<Long> foodNodeIds;
	protected Long typeId;
	protected List<Long> typeIds;
	protected Integer semester;
	protected Integer year;
	protected Integer month;
	protected Integer fullDay;
	protected String remarkLike;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	public DietarySurveyQuery() {

	}

	public DietarySurveyQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public DietarySurveyQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public DietarySurveyQuery foodId(Long foodId) {
		if (foodId == null) {
			throw new RuntimeException("foodId is null");
		}
		this.foodId = foodId;
		return this;
	}

	public DietarySurveyQuery foodIds(List<Long> foodIds) {
		if (foodIds == null) {
			throw new RuntimeException("foodIds is empty ");
		}
		this.foodIds = foodIds;
		return this;
	}

	public DietarySurveyQuery foodNameLike(String foodNameLike) {
		if (foodNameLike == null) {
			throw new RuntimeException("foodName is null");
		}
		this.foodNameLike = foodNameLike;
		return this;
	}

	public DietarySurveyQuery foodNodeId(Long foodNodeId) {
		if (foodNodeId == null) {
			throw new RuntimeException("foodNodeId is null");
		}
		this.foodNodeId = foodNodeId;
		return this;
	}

	public DietarySurveyQuery foodNodeIds(List<Long> foodNodeIds) {
		if (foodNodeIds == null) {
			throw new RuntimeException("foodNodeIds is empty ");
		}
		this.foodNodeIds = foodNodeIds;
		return this;
	}

	public DietarySurveyQuery fullDay(Integer fullDay) {
		if (fullDay == null) {
			throw new RuntimeException("fullDay is null");
		}
		this.fullDay = fullDay;
		return this;
	}

	public Date getCreateTimeGreaterThanOrEqual() {
		return createTimeGreaterThanOrEqual;
	}

	public Date getCreateTimeLessThanOrEqual() {
		return createTimeLessThanOrEqual;
	}

	public Long getFoodId() {
		return foodId;
	}

	public List<Long> getFoodIds() {
		return foodIds;
	}

	public String getFoodNameLike() {
		if (foodNameLike != null && foodNameLike.trim().length() > 0) {
			if (!foodNameLike.startsWith("%")) {
				foodNameLike = "%" + foodNameLike;
			}
			if (!foodNameLike.endsWith("%")) {
				foodNameLike = foodNameLike + "%";
			}
		}
		return foodNameLike;
	}

	public Long getFoodNodeId() {
		return foodNodeId;
	}

	public List<Long> getFoodNodeIds() {
		return foodNodeIds;
	}

	public Integer getFullDay() {
		return fullDay;
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

			if ("tenantId".equals(sortColumn)) {
				orderBy = "E.TENANTID_" + a_x;
			}

			if ("foodId".equals(sortColumn)) {
				orderBy = "E.FOODID_" + a_x;
			}

			if ("foodName".equals(sortColumn)) {
				orderBy = "E.FOODNAME_" + a_x;
			}

			if ("foodNodeId".equals(sortColumn)) {
				orderBy = "E.FOODNODEID_" + a_x;
			}

			if ("typeId".equals(sortColumn)) {
				orderBy = "E.TYPEID_" + a_x;
			}

			if ("mealTime".equals(sortColumn)) {
				orderBy = "E.MEALTIME_" + a_x;
			}

			if ("semester".equals(sortColumn)) {
				orderBy = "E.SEMESTER_" + a_x;
			}

			if ("year".equals(sortColumn)) {
				orderBy = "E.YEAR_" + a_x;
			}

			if ("month".equals(sortColumn)) {
				orderBy = "E.MONTH_" + a_x;
			}

			if ("fullDay".equals(sortColumn)) {
				orderBy = "E.FULLDAY_" + a_x;
			}

			if ("weight".equals(sortColumn)) {
				orderBy = "E.WEIGHT_" + a_x;
			}

			if ("weightPercent".equals(sortColumn)) {
				orderBy = "E.WEIGHTPERCENT_" + a_x;
			}

			if ("cookedWeight".equals(sortColumn)) {
				orderBy = "E.COOKEDWEIGHT_" + a_x;
			}

			if ("weightCookedPercent".equals(sortColumn)) {
				orderBy = "E.WEIGHTCOOKEDPERCENT_" + a_x;
			}

			if ("price".equals(sortColumn)) {
				orderBy = "E.PRICE_" + a_x;
			}

			if ("totalPrice".equals(sortColumn)) {
				orderBy = "E.TOTALPRICE_" + a_x;
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

	public Integer getSemester() {
		return semester;
	}

	public List<String> getTenantIds() {
		return tenantIds;
	}

	public Long getTypeId() {
		return typeId;
	}

	public List<Long> getTypeIds() {
		return typeIds;
	}

	public Integer getYear() {
		return year;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("tenantId", "TENANTID_");
		addColumn("foodId", "FOODID_");
		addColumn("foodName", "FOODNAME_");
		addColumn("foodNodeId", "FOODNODEID_");
		addColumn("typeId", "TYPEID_");
		addColumn("mealTime", "MEALTIME_");
		addColumn("semester", "SEMESTER_");
		addColumn("year", "YEAR_");
		addColumn("month", "MONTH_");
		addColumn("fullDay", "FULLDAY_");
		addColumn("weight", "WEIGHT_");
		addColumn("weightPercent", "WEIGHTPERCENT_");
		addColumn("cookedWeight", "COOKEDWEIGHT_");
		addColumn("weightCookedPercent", "WEIGHTCOOKEDPERCENT_");
		addColumn("price", "PRICE_");
		addColumn("totalPrice", "TOTALPRICE_");
		addColumn("remark", "REMARK_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
	}

	public DietarySurveyQuery month(Integer month) {
		if (month == null) {
			throw new RuntimeException("month is null");
		}
		this.month = month;
		return this;
	}

	public DietarySurveyQuery remarkLike(String remarkLike) {
		if (remarkLike == null) {
			throw new RuntimeException("remark is null");
		}
		this.remarkLike = remarkLike;
		return this;
	}

	public DietarySurveyQuery semester(Integer semester) {
		if (semester == null) {
			throw new RuntimeException("semester is null");
		}
		this.semester = semester;
		return this;
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setFoodId(Long foodId) {
		this.foodId = foodId;
	}

	public void setFoodIds(List<Long> foodIds) {
		this.foodIds = foodIds;
	}

	public void setFoodNameLike(String foodNameLike) {
		this.foodNameLike = foodNameLike;
	}

	public void setFoodNodeId(Long foodNodeId) {
		this.foodNodeId = foodNodeId;
	}

	public void setFoodNodeIds(List<Long> foodNodeIds) {
		this.foodNodeIds = foodNodeIds;
	}

	public void setFullDay(Integer fullDay) {
		this.fullDay = fullDay;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public void setRemarkLike(String remarkLike) {
		this.remarkLike = remarkLike;
	}

	public void setSemester(Integer semester) {
		this.semester = semester;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setTenantIds(List<String> tenantIds) {
		this.tenantIds = tenantIds;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public void setTypeIds(List<Long> typeIds) {
		this.typeIds = typeIds;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public DietarySurveyQuery tenantIds(List<String> tenantIds) {
		if (tenantIds == null) {
			throw new RuntimeException("tenantIds is empty ");
		}
		this.tenantIds = tenantIds;
		return this;
	}

	public DietarySurveyQuery typeId(Long typeId) {
		if (typeId == null) {
			throw new RuntimeException("typeId is null");
		}
		this.typeId = typeId;
		return this;
	}

	public DietarySurveyQuery typeIds(List<Long> typeIds) {
		if (typeIds == null) {
			throw new RuntimeException("typeIds is empty ");
		}
		this.typeIds = typeIds;
		return this;
	}

	public DietarySurveyQuery year(Integer year) {
		if (year == null) {
			throw new RuntimeException("year is null");
		}
		this.year = year;
		return this;
	}

}