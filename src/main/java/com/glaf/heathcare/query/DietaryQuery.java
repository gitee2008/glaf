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

public class DietaryQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected Long dietaryId;
	protected List<Long> dietaryIds;
	protected Long templateId;
	protected List<Long> templateIds;
	protected String name;
	protected String nameLike;
	protected String descriptionLike;
	protected String type;
	protected Long typeId;
	protected List<Long> typeIds;
	protected Integer semester;
	protected Integer year;
	protected Integer month;
	protected Integer day;
	protected Integer dayGreaterThanOrEqual;
	protected Integer week;
	protected Integer fullDay;
	protected String purchaseFlag;
	protected String purchaseFlagIsNull;
	protected String shareFlag;
	protected String verifyFlag;
	protected String tenantCanSee;
	protected String tableSuffix;
	protected Integer businessStatus;
	protected String confirmBy;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	public DietaryQuery() {

	}

	public DietaryQuery businessStatus(Integer businessStatus) {
		if (businessStatus == null) {
			throw new RuntimeException("businessStatus is null");
		}
		this.businessStatus = businessStatus;
		return this;
	}

	public DietaryQuery confirmBy(String confirmBy) {
		if (confirmBy == null) {
			throw new RuntimeException("confirmBy is null");
		}
		this.confirmBy = confirmBy;
		return this;
	}

	public DietaryQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public DietaryQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public DietaryQuery day(Integer day) {
		if (day == null) {
			throw new RuntimeException("day is null");
		}
		this.day = day;
		return this;
	}

	public DietaryQuery dayGreaterThanOrEqual(Integer dayGreaterThanOrEqual) {
		if (dayGreaterThanOrEqual == null) {
			throw new RuntimeException("dayGreaterThanOrEqual is null");
		}
		this.dayGreaterThanOrEqual = dayGreaterThanOrEqual;
		return this;
	}

	public DietaryQuery descriptionLike(String descriptionLike) {
		if (descriptionLike == null) {
			throw new RuntimeException("description is null");
		}
		this.descriptionLike = descriptionLike;
		return this;
	}

	public DietaryQuery fullDay(Integer fullDay) {
		if (fullDay == null) {
			throw new RuntimeException("fullDay is null");
		}
		this.fullDay = fullDay;
		return this;
	}

	public Integer getBusinessStatus() {
		return businessStatus;
	}

	public String getConfirmBy() {
		return confirmBy;
	}

	public Date getCreateTimeGreaterThanOrEqual() {
		return createTimeGreaterThanOrEqual;
	}

	public Date getCreateTimeLessThanOrEqual() {
		return createTimeLessThanOrEqual;
	}

	public Integer getDay() {
		return day;
	}

	public Integer getDayGreaterThanOrEqual() {
		return dayGreaterThanOrEqual;
	}

	public String getDescriptionLike() {
		if (descriptionLike != null && descriptionLike.trim().length() > 0) {
			if (!descriptionLike.startsWith("%")) {
				descriptionLike = "%" + descriptionLike;
			}
			if (!descriptionLike.endsWith("%")) {
				descriptionLike = descriptionLike + "%";
			}
		}
		return descriptionLike;
	}

	public Long getDietaryId() {
		return dietaryId;
	}

	public List<Long> getDietaryIds() {
		return dietaryIds;
	}

	public Integer getFullDay() {
		return fullDay;
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

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("templateId".equals(sortColumn)) {
				orderBy = "E.TEMPLATEID_" + a_x;
			}

			if ("tenantId".equals(sortColumn)) {
				orderBy = "E.TENANTID_" + a_x;
			}

			if ("name".equals(sortColumn)) {
				orderBy = "E.NAME_" + a_x;
			}

			if ("description".equals(sortColumn)) {
				orderBy = "E.DESCRIPTION_" + a_x;
			}

			if ("type".equals(sortColumn)) {
				orderBy = "E.TYPE_" + a_x;
			}

			if ("typeId".equals(sortColumn)) {
				orderBy = "E.TYPEID_" + a_x;
			}

			if ("heatEnergy".equals(sortColumn)) {
				orderBy = "E.HEATENERGY_" + a_x;
			}

			if ("protein".equals(sortColumn)) {
				orderBy = "E.PROTEIN_" + a_x;
			}

			if ("fat".equals(sortColumn)) {
				orderBy = "E.FAT_" + a_x;
			}

			if ("carbohydrate".equals(sortColumn)) {
				orderBy = "E.CARBOHYDRATE_" + a_x;
			}

			if ("vitaminA".equals(sortColumn)) {
				orderBy = "E.VITAMINA_" + a_x;
			}

			if ("vitaminB1".equals(sortColumn)) {
				orderBy = "E.VITAMINB1_" + a_x;
			}

			if ("vitaminB2".equals(sortColumn)) {
				orderBy = "E.VITAMINB2_" + a_x;
			}

			if ("vitaminB6".equals(sortColumn)) {
				orderBy = "E.VITAMINB6_" + a_x;
			}

			if ("vitaminB12".equals(sortColumn)) {
				orderBy = "E.VITAMINB12_" + a_x;
			}

			if ("vitaminC".equals(sortColumn)) {
				orderBy = "E.VITAMINC_" + a_x;
			}

			if ("carotene".equals(sortColumn)) {
				orderBy = "E.CAROTENE_" + a_x;
			}

			if ("retinol".equals(sortColumn)) {
				orderBy = "E.RETINOL_" + a_x;
			}

			if ("nicotinicCid".equals(sortColumn)) {
				orderBy = "E.NICOTINICCID_" + a_x;
			}

			if ("calcium".equals(sortColumn)) {
				orderBy = "E.CALCIUM_" + a_x;
			}

			if ("iron".equals(sortColumn)) {
				orderBy = "E.IRON_" + a_x;
			}

			if ("zinc".equals(sortColumn)) {
				orderBy = "E.ZINC_" + a_x;
			}

			if ("iodine".equals(sortColumn)) {
				orderBy = "E.IODINE_" + a_x;
			}

			if ("phosphorus".equals(sortColumn)) {
				orderBy = "E.PHOSPHORUS_" + a_x;
			}

			if ("year".equals(sortColumn)) {
				orderBy = "E.YEAR_" + a_x;
			}

			if ("month".equals(sortColumn)) {
				orderBy = "E.MONTH_" + a_x;
			}

			if ("day".equals(sortColumn)) {
				orderBy = "E.DAY_" + a_x;
			}

			if ("week".equals(sortColumn)) {
				orderBy = "E.WEEK_" + a_x;
			}

			if ("fullDay".equals(sortColumn)) {
				orderBy = "E.FULLDAY_" + a_x;
			}

			if ("sortNo".equals(sortColumn)) {
				orderBy = "E.SORTNO_" + a_x;
			}

			if ("purchaseFlag".equals(sortColumn)) {
				orderBy = "E.PURCHASEFLAG_" + a_x;
			}

			if ("verifyFlag".equals(sortColumn)) {
				orderBy = "E.VERIFYFLAG_" + a_x;
			}

			if ("businessStatus".equals(sortColumn)) {
				orderBy = "E.BUSINESSSTATUS_" + a_x;
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

			if ("updateBy".equals(sortColumn)) {
				orderBy = "E.UPDATEBY_" + a_x;
			}

			if ("updateTime".equals(sortColumn)) {
				orderBy = "E.UPDATETIME_" + a_x;
			}

		}
		return orderBy;
	}

	public String getPurchaseFlag() {
		return purchaseFlag;
	}

	public String getPurchaseFlagIsNull() {
		return purchaseFlagIsNull;
	}

	public Integer getSemester() {
		return semester;
	}

	public String getShareFlag() {
		return shareFlag;
	}

	public String getTableSuffix() {
		return tableSuffix;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public List<Long> getTemplateIds() {
		return templateIds;
	}

	public String getTenantCanSee() {
		return tenantCanSee;
	}

	public String getTenantId() {
		return tenantId;
	}

	public String getType() {
		return type;
	}

	public Long getTypeId() {
		return typeId;
	}

	public List<Long> getTypeIds() {
		return typeIds;
	}

	public String getVerifyFlag() {
		return verifyFlag;
	}

	public Integer getWeek() {
		return week;
	}

	public Integer getYear() {
		return year;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("templateId", "TEMPLATEID_");
		addColumn("tenantId", "TENANTID_");
		addColumn("name", "NAME_");
		addColumn("description", "DESCRIPTION_");
		addColumn("type", "TYPE_");
		addColumn("typeId", "TYPEID_");
		addColumn("heatEnergy", "HEATENERGY_");
		addColumn("protein", "PROTEIN_");
		addColumn("fat", "FAT_");
		addColumn("carbohydrate", "CARBOHYDRATE_");
		addColumn("vitaminA", "VITAMINA_");
		addColumn("vitaminB1", "VITAMINB1_");
		addColumn("vitaminB2", "VITAMINB2_");
		addColumn("vitaminB6", "VITAMINB6_");
		addColumn("vitaminB12", "VITAMINB12_");
		addColumn("vitaminC", "VITAMINC_");
		addColumn("carotene", "CAROTENE_");
		addColumn("retinol", "RETINOL_");
		addColumn("nicotinicCid", "NICOTINICCID_");
		addColumn("calcium", "CALCIUM_");
		addColumn("iron", "IRON_");
		addColumn("zinc", "ZINC_");
		addColumn("iodine", "IODINE_");
		addColumn("phosphorus", "PHOSPHORUS_");
		addColumn("year", "YEAR_");
		addColumn("month", "MONTH_");
		addColumn("day", "DAY_");
		addColumn("week", "WEEK_");
		addColumn("fullDay", "FULLDAY_");
		addColumn("sortNo", "SORTNO_");
		addColumn("purchaseFlag", "PURCHASEFLAG_");
		addColumn("verifyFlag", "VERIFYFLAG_");
		addColumn("businessStatus", "BUSINESSSTATUS_");
		addColumn("confirmBy", "CONFIRMBY_");
		addColumn("confirmTime", "CONFIRMTIME_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
		addColumn("updateBy", "UPDATEBY_");
		addColumn("updateTime", "UPDATETIME_");
	}

	public DietaryQuery month(Integer month) {
		if (month == null) {
			throw new RuntimeException("month is null");
		}
		this.month = month;
		return this;
	}

	public DietaryQuery name(String name) {
		if (name == null) {
			throw new RuntimeException("name is null");
		}
		this.name = name;
		return this;
	}

	public DietaryQuery nameLike(String nameLike) {
		if (nameLike == null) {
			throw new RuntimeException("name is null");
		}
		this.nameLike = nameLike;
		return this;
	}

	public DietaryQuery purchaseFlag(String purchaseFlag) {
		if (purchaseFlag == null) {
			throw new RuntimeException("purchaseFlag is null");
		}
		this.purchaseFlag = purchaseFlag;
		return this;
	}

	public DietaryQuery semester(Integer semester) {
		if (semester == null) {
			throw new RuntimeException("semester is null");
		}
		this.semester = semester;
		return this;
	}

	public void setBusinessStatus(Integer businessStatus) {
		this.businessStatus = businessStatus;
	}

	public void setConfirmBy(String confirmBy) {
		this.confirmBy = confirmBy;
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public void setDayGreaterThanOrEqual(Integer dayGreaterThanOrEqual) {
		this.dayGreaterThanOrEqual = dayGreaterThanOrEqual;
	}

	public void setDescriptionLike(String descriptionLike) {
		this.descriptionLike = descriptionLike;
	}

	public void setDietaryId(Long dietaryId) {
		this.dietaryId = dietaryId;
	}

	public void setDietaryIds(List<Long> dietaryIds) {
		this.dietaryIds = dietaryIds;
	}

	public void setFullDay(Integer fullDay) {
		this.fullDay = fullDay;
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

	public void setPurchaseFlag(String purchaseFlag) {
		this.purchaseFlag = purchaseFlag;
	}

	public void setPurchaseFlagIsNull(String purchaseFlagIsNull) {
		this.purchaseFlagIsNull = purchaseFlagIsNull;
	}

	public void setSemester(Integer semester) {
		this.semester = semester;
	}

	public void setShareFlag(String shareFlag) {
		this.shareFlag = shareFlag;
	}

	public void setTableSuffix(String tableSuffix) {
		this.tableSuffix = tableSuffix;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public void setTemplateIds(List<Long> templateIds) {
		this.templateIds = templateIds;
	}

	public void setTenantCanSee(String tenantCanSee) {
		this.tenantCanSee = tenantCanSee;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public void setTypeIds(List<Long> typeIds) {
		this.typeIds = typeIds;
	}

	public void setVerifyFlag(String verifyFlag) {
		this.verifyFlag = verifyFlag;
	}

	public void setWeek(Integer week) {
		this.week = week;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public DietaryQuery shareFlag(String shareFlag) {
		if (shareFlag == null) {
			throw new RuntimeException("shareFlag is null");
		}
		this.shareFlag = shareFlag;
		return this;
	}

	public DietaryQuery tableSuffix(String tableSuffix) {
		if (tableSuffix == null) {
			throw new RuntimeException("tableSuffix is null");
		}
		this.tableSuffix = tableSuffix;
		return this;
	}

	public DietaryQuery templateId(Long templateId) {
		if (templateId == null) {
			throw new RuntimeException("templateId is null");
		}
		this.templateId = templateId;
		return this;
	}

	public DietaryQuery templateIds(List<Long> templateIds) {
		if (templateIds == null) {
			throw new RuntimeException("templateIds is empty ");
		}
		this.templateIds = templateIds;
		return this;
	}

	public DietaryQuery tenantId(String tenantId) {
		if (tenantId == null) {
			throw new RuntimeException("tenantId is null");
		}
		this.tenantId = tenantId;
		return this;
	}

	public DietaryQuery type(String type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		this.type = type;
		return this;
	}

	public DietaryQuery typeId(Long typeId) {
		if (typeId == null) {
			throw new RuntimeException("typeId is null");
		}
		this.typeId = typeId;
		return this;
	}

	public DietaryQuery typeIds(List<Long> typeIds) {
		if (typeIds == null) {
			throw new RuntimeException("typeIds is empty ");
		}
		this.typeIds = typeIds;
		return this;
	}

	public DietaryQuery verifyFlag(String verifyFlag) {
		if (verifyFlag == null) {
			throw new RuntimeException("verifyFlag is null");
		}
		this.verifyFlag = verifyFlag;
		return this;
	}

	public DietaryQuery week(Integer week) {
		if (week == null) {
			throw new RuntimeException("week is null");
		}
		this.week = week;
		return this;
	}

	public DietaryQuery year(Integer year) {
		if (year == null) {
			throw new RuntimeException("year is null");
		}
		this.year = year;
		return this;
	}

}