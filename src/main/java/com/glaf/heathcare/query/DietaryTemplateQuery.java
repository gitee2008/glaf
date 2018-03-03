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

public class DietaryTemplateQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected List<Long> ids;
	protected String name;
	protected String nameLike;
	protected String descriptionLike;
	protected String ageGroup;
	protected String province;
	protected String region;
	protected Integer season;
	protected String type;
	protected String typeLike;
	protected Long typeId;
	protected List<Long> typeIds;
	protected Double heatEnergyGreaterThanOrEqual;
	protected Double heatEnergyLessThanOrEqual;
	protected Double proteinGreaterThanOrEqual;
	protected Double proteinLessThanOrEqual;
	protected Double fatGreaterThanOrEqual;
	protected Double fatLessThanOrEqual;
	protected Double carbohydrateGreaterThanOrEqual;
	protected Double carbohydrateLessThanOrEqual;
	protected Double vitaminAGreaterThanOrEqual;
	protected Double vitaminALessThanOrEqual;
	protected Double vitaminB1GreaterThanOrEqual;
	protected Double vitaminB1LessThanOrEqual;
	protected Double vitaminB2GreaterThanOrEqual;
	protected Double vitaminB2LessThanOrEqual;
	protected Double vitaminB6GreaterThanOrEqual;
	protected Double vitaminB6LessThanOrEqual;
	protected Double vitaminB12GreaterThanOrEqual;
	protected Double vitaminB12LessThanOrEqual;
	protected Double vitaminCGreaterThanOrEqual;
	protected Double vitaminCLessThanOrEqual;
	protected Double caroteneGreaterThanOrEqual;
	protected Double caroteneLessThanOrEqual;
	protected Double retinolGreaterThanOrEqual;
	protected Double retinolLessThanOrEqual;
	protected Double nicotinicCidGreaterThanOrEqual;
	protected Double nicotinicCidLessThanOrEqual;
	protected Double calciumGreaterThanOrEqual;
	protected Double calciumLessThanOrEqual;
	protected Double ironGreaterThanOrEqual;
	protected Double ironLessThanOrEqual;
	protected Double zincGreaterThanOrEqual;
	protected Double zincLessThanOrEqual;
	protected Integer year;
	protected Integer month;
	protected Integer day;
	protected Integer dayOfWeek;
	protected Integer week;
	protected Integer suitNo;
	protected String enableFlag;
	protected String instanceFlag;
	protected String shareFlag;
	protected String sysFlag;
	protected String verifyFlag;
	protected String tenantCanSee;
	protected Integer businessStatus;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;
	protected Date updateTimeGreaterThanOrEqual;
	protected Date updateTimeLessThanOrEqual;

	public DietaryTemplateQuery() {

	}

	public DietaryTemplateQuery ageGroup(String ageGroup) {
		if (ageGroup == null) {
			throw new RuntimeException("ageGroup is null");
		}
		this.ageGroup = ageGroup;
		return this;
	}

	public DietaryTemplateQuery businessStatus(Integer businessStatus) {
		if (businessStatus == null) {
			throw new RuntimeException("businessStatus is null");
		}
		this.businessStatus = businessStatus;
		return this;
	}

	public DietaryTemplateQuery calciumGreaterThanOrEqual(Double calciumGreaterThanOrEqual) {
		if (calciumGreaterThanOrEqual == null) {
			throw new RuntimeException("calcium is null");
		}
		this.calciumGreaterThanOrEqual = calciumGreaterThanOrEqual;
		return this;
	}

	public DietaryTemplateQuery calciumLessThanOrEqual(Double calciumLessThanOrEqual) {
		if (calciumLessThanOrEqual == null) {
			throw new RuntimeException("calcium is null");
		}
		this.calciumLessThanOrEqual = calciumLessThanOrEqual;
		return this;
	}

	public DietaryTemplateQuery carbohydrateGreaterThanOrEqual(Double carbohydrateGreaterThanOrEqual) {
		if (carbohydrateGreaterThanOrEqual == null) {
			throw new RuntimeException("carbohydrate is null");
		}
		this.carbohydrateGreaterThanOrEqual = carbohydrateGreaterThanOrEqual;
		return this;
	}

	public DietaryTemplateQuery carbohydrateLessThanOrEqual(Double carbohydrateLessThanOrEqual) {
		if (carbohydrateLessThanOrEqual == null) {
			throw new RuntimeException("carbohydrate is null");
		}
		this.carbohydrateLessThanOrEqual = carbohydrateLessThanOrEqual;
		return this;
	}

	public DietaryTemplateQuery caroteneGreaterThanOrEqual(Double caroteneGreaterThanOrEqual) {
		if (caroteneGreaterThanOrEqual == null) {
			throw new RuntimeException("carotene is null");
		}
		this.caroteneGreaterThanOrEqual = caroteneGreaterThanOrEqual;
		return this;
	}

	public DietaryTemplateQuery caroteneLessThanOrEqual(Double caroteneLessThanOrEqual) {
		if (caroteneLessThanOrEqual == null) {
			throw new RuntimeException("carotene is null");
		}
		this.caroteneLessThanOrEqual = caroteneLessThanOrEqual;
		return this;
	}

	public DietaryTemplateQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public DietaryTemplateQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public DietaryTemplateQuery day(Integer day) {
		if (day == null) {
			throw new RuntimeException("day is null");
		}
		this.day = day;
		return this;
	}

	public DietaryTemplateQuery dayOfWeek(Integer dayOfWeek) {
		if (dayOfWeek == null) {
			throw new RuntimeException("dayOfWeek is null");
		}
		this.dayOfWeek = dayOfWeek;
		return this;
	}

	public DietaryTemplateQuery descriptionLike(String descriptionLike) {
		if (descriptionLike == null) {
			throw new RuntimeException("description is null");
		}
		this.descriptionLike = descriptionLike;
		return this;
	}

	public DietaryTemplateQuery enableFlag(String enableFlag) {
		if (enableFlag == null) {
			throw new RuntimeException("enableFlag is null");
		}
		this.enableFlag = enableFlag;
		return this;
	}

	public DietaryTemplateQuery fatGreaterThanOrEqual(Double fatGreaterThanOrEqual) {
		if (fatGreaterThanOrEqual == null) {
			throw new RuntimeException("fat is null");
		}
		this.fatGreaterThanOrEqual = fatGreaterThanOrEqual;
		return this;
	}

	public DietaryTemplateQuery fatLessThanOrEqual(Double fatLessThanOrEqual) {
		if (fatLessThanOrEqual == null) {
			throw new RuntimeException("fat is null");
		}
		this.fatLessThanOrEqual = fatLessThanOrEqual;
		return this;
	}

	public String getAgeGroup() {
		return ageGroup;
	}

	public Integer getBusinessStatus() {
		return businessStatus;
	}

	public Double getCalciumGreaterThanOrEqual() {
		return calciumGreaterThanOrEqual;
	}

	public Double getCalciumLessThanOrEqual() {
		return calciumLessThanOrEqual;
	}

	public Double getCarbohydrateGreaterThanOrEqual() {
		return carbohydrateGreaterThanOrEqual;
	}

	public Double getCarbohydrateLessThanOrEqual() {
		return carbohydrateLessThanOrEqual;
	}

	public Double getCaroteneGreaterThanOrEqual() {
		return caroteneGreaterThanOrEqual;
	}

	public Double getCaroteneLessThanOrEqual() {
		return caroteneLessThanOrEqual;
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

	public Integer getDayOfWeek() {
		return dayOfWeek;
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

	public String getEnableFlag() {
		return enableFlag;
	}

	public Double getFatGreaterThanOrEqual() {
		return fatGreaterThanOrEqual;
	}

	public Double getFatLessThanOrEqual() {
		return fatLessThanOrEqual;
	}

	public Double getHeatEnergyGreaterThanOrEqual() {
		return heatEnergyGreaterThanOrEqual;
	}

	public Double getHeatEnergyLessThanOrEqual() {
		return heatEnergyLessThanOrEqual;
	}

	public List<Long> getIds() {
		return ids;
	}

	public String getInstanceFlag() {
		return instanceFlag;
	}

	public Double getIronGreaterThanOrEqual() {
		return ironGreaterThanOrEqual;
	}

	public Double getIronLessThanOrEqual() {
		return ironLessThanOrEqual;
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

	public Double getNicotinicCidGreaterThanOrEqual() {
		return nicotinicCidGreaterThanOrEqual;
	}

	public Double getNicotinicCidLessThanOrEqual() {
		return nicotinicCidLessThanOrEqual;
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

			if ("description".equals(sortColumn)) {
				orderBy = "E.DESCRIPTION_" + a_x;
			}

			if ("ageGroup".equals(sortColumn)) {
				orderBy = "E.AGEGROUP_" + a_x;
			}

			if ("season".equals(sortColumn)) {
				orderBy = "E.SEASON_" + a_x;
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

			if ("birthday".equals(sortColumn)) {
				orderBy = "E.BIRTHDAY_" + a_x;
			}

			if ("sortNo".equals(sortColumn)) {
				orderBy = "E.SORTNO_" + a_x;
			}

			if ("sysFlag".equals(sortColumn)) {
				orderBy = "E.SYSFLAG_" + a_x;
			}

			if ("enableFlag".equals(sortColumn)) {
				orderBy = "E.ENABLEFLAG_" + a_x;
			}

			if ("verifyFlag".equals(sortColumn)) {
				orderBy = "E.VERIFYFLAG_" + a_x;
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

	public Double getProteinGreaterThanOrEqual() {
		return proteinGreaterThanOrEqual;
	}

	public Double getProteinLessThanOrEqual() {
		return proteinLessThanOrEqual;
	}

	public String getProvince() {
		return province;
	}

	public String getRegion() {
		return region;
	}

	public Double getRetinolGreaterThanOrEqual() {
		return retinolGreaterThanOrEqual;
	}

	public Double getRetinolLessThanOrEqual() {
		return retinolLessThanOrEqual;
	}

	public Integer getSeason() {
		return season;
	}

	public String getShareFlag() {
		return shareFlag;
	}

	public Integer getSuitNo() {
		return suitNo;
	}

	public String getSysFlag() {
		return sysFlag;
	}

	public String getTenantCanSee() {
		return tenantCanSee;
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

	public String getTypeLike() {
		if (typeLike != null && typeLike.trim().length() > 0) {
			if (!typeLike.startsWith("%")) {
				typeLike = "%" + typeLike;
			}
			if (!typeLike.endsWith("%")) {
				typeLike = typeLike + "%";
			}
		}
		return typeLike;
	}

	public Date getUpdateTimeGreaterThanOrEqual() {
		return updateTimeGreaterThanOrEqual;
	}

	public Date getUpdateTimeLessThanOrEqual() {
		return updateTimeLessThanOrEqual;
	}

	public String getVerifyFlag() {
		return verifyFlag;
	}

	public Double getVitaminAGreaterThanOrEqual() {
		return vitaminAGreaterThanOrEqual;
	}

	public Double getVitaminALessThanOrEqual() {
		return vitaminALessThanOrEqual;
	}

	public Double getVitaminB12GreaterThanOrEqual() {
		return vitaminB12GreaterThanOrEqual;
	}

	public Double getVitaminB12LessThanOrEqual() {
		return vitaminB12LessThanOrEqual;
	}

	public Double getVitaminB1GreaterThanOrEqual() {
		return vitaminB1GreaterThanOrEqual;
	}

	public Double getVitaminB1LessThanOrEqual() {
		return vitaminB1LessThanOrEqual;
	}

	public Double getVitaminB2GreaterThanOrEqual() {
		return vitaminB2GreaterThanOrEqual;
	}

	public Double getVitaminB2LessThanOrEqual() {
		return vitaminB2LessThanOrEqual;
	}

	public Double getVitaminB6GreaterThanOrEqual() {
		return vitaminB6GreaterThanOrEqual;
	}

	public Double getVitaminB6LessThanOrEqual() {
		return vitaminB6LessThanOrEqual;
	}

	public Double getVitaminCGreaterThanOrEqual() {
		return vitaminCGreaterThanOrEqual;
	}

	public Double getVitaminCLessThanOrEqual() {
		return vitaminCLessThanOrEqual;
	}

	public Integer getWeek() {
		return week;
	}

	public Integer getYear() {
		return year;
	}

	public Double getZincGreaterThanOrEqual() {
		return zincGreaterThanOrEqual;
	}

	public Double getZincLessThanOrEqual() {
		return zincLessThanOrEqual;
	}

	public DietaryTemplateQuery heatEnergyGreaterThanOrEqual(Double heatEnergyGreaterThanOrEqual) {
		if (heatEnergyGreaterThanOrEqual == null) {
			throw new RuntimeException("heatEnergy is null");
		}
		this.heatEnergyGreaterThanOrEqual = heatEnergyGreaterThanOrEqual;
		return this;
	}

	public DietaryTemplateQuery heatEnergyLessThanOrEqual(Double heatEnergyLessThanOrEqual) {
		if (heatEnergyLessThanOrEqual == null) {
			throw new RuntimeException("heatEnergy is null");
		}
		this.heatEnergyLessThanOrEqual = heatEnergyLessThanOrEqual;
		return this;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("name", "NAME_");
		addColumn("description", "DESCRIPTION_");
		addColumn("ageGroup", "AGEGROUP_");
		addColumn("season", "SEASON_");
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
		addColumn("year", "YEAR_");
		addColumn("month", "MONTH_");
		addColumn("day", "DAY_");
		addColumn("week", "WEEK_");
		addColumn("birthday", "BIRTHDAY_");
		addColumn("sortNo", "SORTNO_");
		addColumn("sysFlag", "SYSFLAG_");
		addColumn("enableFlag", "ENABLEFLAG_");
		addColumn("verifyFlag", "VERIFYFLAG_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
		addColumn("updateBy", "UPDATEBY_");
		addColumn("updateTime", "UPDATETIME_");
	}

	public DietaryTemplateQuery instanceFlag(String instanceFlag) {
		if (instanceFlag == null) {
			throw new RuntimeException("instanceFlag is null");
		}
		this.instanceFlag = instanceFlag;
		return this;
	}

	public DietaryTemplateQuery ironGreaterThanOrEqual(Double ironGreaterThanOrEqual) {
		if (ironGreaterThanOrEqual == null) {
			throw new RuntimeException("iron is null");
		}
		this.ironGreaterThanOrEqual = ironGreaterThanOrEqual;
		return this;
	}

	public DietaryTemplateQuery ironLessThanOrEqual(Double ironLessThanOrEqual) {
		if (ironLessThanOrEqual == null) {
			throw new RuntimeException("iron is null");
		}
		this.ironLessThanOrEqual = ironLessThanOrEqual;
		return this;
	}

	public DietaryTemplateQuery month(Integer month) {
		if (month == null) {
			throw new RuntimeException("month is null");
		}
		this.month = month;
		return this;
	}

	public DietaryTemplateQuery name(String name) {
		if (name == null) {
			throw new RuntimeException("name is null");
		}
		this.name = name;
		return this;
	}

	public DietaryTemplateQuery nameLike(String nameLike) {
		if (nameLike == null) {
			throw new RuntimeException("name is null");
		}
		this.nameLike = nameLike;
		return this;
	}

	public DietaryTemplateQuery nicotinicCidGreaterThanOrEqual(Double nicotinicCidGreaterThanOrEqual) {
		if (nicotinicCidGreaterThanOrEqual == null) {
			throw new RuntimeException("nicotinicCid is null");
		}
		this.nicotinicCidGreaterThanOrEqual = nicotinicCidGreaterThanOrEqual;
		return this;
	}

	public DietaryTemplateQuery nicotinicCidLessThanOrEqual(Double nicotinicCidLessThanOrEqual) {
		if (nicotinicCidLessThanOrEqual == null) {
			throw new RuntimeException("nicotinicCid is null");
		}
		this.nicotinicCidLessThanOrEqual = nicotinicCidLessThanOrEqual;
		return this;
	}


	public DietaryTemplateQuery proteinGreaterThanOrEqual(Double proteinGreaterThanOrEqual) {
		if (proteinGreaterThanOrEqual == null) {
			throw new RuntimeException("protein is null");
		}
		this.proteinGreaterThanOrEqual = proteinGreaterThanOrEqual;
		return this;
	}

	public DietaryTemplateQuery proteinLessThanOrEqual(Double proteinLessThanOrEqual) {
		if (proteinLessThanOrEqual == null) {
			throw new RuntimeException("protein is null");
		}
		this.proteinLessThanOrEqual = proteinLessThanOrEqual;
		return this;
	}

	public DietaryTemplateQuery province(String province) {
		if (province == null) {
			throw new RuntimeException("province is null");
		}
		this.province = province;
		return this;
	}

	public DietaryTemplateQuery region(String region) {
		if (region == null) {
			throw new RuntimeException("region is null");
		}
		this.region = region;
		return this;
	}

	public DietaryTemplateQuery retinolGreaterThanOrEqual(Double retinolGreaterThanOrEqual) {
		if (retinolGreaterThanOrEqual == null) {
			throw new RuntimeException("retinol is null");
		}
		this.retinolGreaterThanOrEqual = retinolGreaterThanOrEqual;
		return this;
	}

	public DietaryTemplateQuery retinolLessThanOrEqual(Double retinolLessThanOrEqual) {
		if (retinolLessThanOrEqual == null) {
			throw new RuntimeException("retinol is null");
		}
		this.retinolLessThanOrEqual = retinolLessThanOrEqual;
		return this;
	}

	public DietaryTemplateQuery season(Integer season) {
		if (season == null) {
			throw new RuntimeException("season is null");
		}
		this.season = season;
		return this;
	}

	public void setAgeGroup(String ageGroup) {
		this.ageGroup = ageGroup;
	}

	public void setBusinessStatus(Integer businessStatus) {
		this.businessStatus = businessStatus;
	}

	public void setCalciumGreaterThanOrEqual(Double calciumGreaterThanOrEqual) {
		this.calciumGreaterThanOrEqual = calciumGreaterThanOrEqual;
	}

	public void setCalciumLessThanOrEqual(Double calciumLessThanOrEqual) {
		this.calciumLessThanOrEqual = calciumLessThanOrEqual;
	}

	public void setCarbohydrateGreaterThanOrEqual(Double carbohydrateGreaterThanOrEqual) {
		this.carbohydrateGreaterThanOrEqual = carbohydrateGreaterThanOrEqual;
	}

	public void setCarbohydrateLessThanOrEqual(Double carbohydrateLessThanOrEqual) {
		this.carbohydrateLessThanOrEqual = carbohydrateLessThanOrEqual;
	}

	public void setCaroteneGreaterThanOrEqual(Double caroteneGreaterThanOrEqual) {
		this.caroteneGreaterThanOrEqual = caroteneGreaterThanOrEqual;
	}

	public void setCaroteneLessThanOrEqual(Double caroteneLessThanOrEqual) {
		this.caroteneLessThanOrEqual = caroteneLessThanOrEqual;
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

	public void setDayOfWeek(Integer dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public void setDescriptionLike(String descriptionLike) {
		this.descriptionLike = descriptionLike;
	}

	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
	}

	public void setFatGreaterThanOrEqual(Double fatGreaterThanOrEqual) {
		this.fatGreaterThanOrEqual = fatGreaterThanOrEqual;
	}

	public void setFatLessThanOrEqual(Double fatLessThanOrEqual) {
		this.fatLessThanOrEqual = fatLessThanOrEqual;
	}

	public void setHeatEnergyGreaterThanOrEqual(Double heatEnergyGreaterThanOrEqual) {
		this.heatEnergyGreaterThanOrEqual = heatEnergyGreaterThanOrEqual;
	}

	public void setHeatEnergyLessThanOrEqual(Double heatEnergyLessThanOrEqual) {
		this.heatEnergyLessThanOrEqual = heatEnergyLessThanOrEqual;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}

	public void setInstanceFlag(String instanceFlag) {
		this.instanceFlag = instanceFlag;
	}

	public void setIronGreaterThanOrEqual(Double ironGreaterThanOrEqual) {
		this.ironGreaterThanOrEqual = ironGreaterThanOrEqual;
	}

	public void setIronLessThanOrEqual(Double ironLessThanOrEqual) {
		this.ironLessThanOrEqual = ironLessThanOrEqual;
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

	public void setNicotinicCidGreaterThanOrEqual(Double nicotinicCidGreaterThanOrEqual) {
		this.nicotinicCidGreaterThanOrEqual = nicotinicCidGreaterThanOrEqual;
	}

	public void setNicotinicCidLessThanOrEqual(Double nicotinicCidLessThanOrEqual) {
		this.nicotinicCidLessThanOrEqual = nicotinicCidLessThanOrEqual;
	}

	public void setProteinGreaterThanOrEqual(Double proteinGreaterThanOrEqual) {
		this.proteinGreaterThanOrEqual = proteinGreaterThanOrEqual;
	}

	public void setProteinLessThanOrEqual(Double proteinLessThanOrEqual) {
		this.proteinLessThanOrEqual = proteinLessThanOrEqual;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public void setRetinolGreaterThanOrEqual(Double retinolGreaterThanOrEqual) {
		this.retinolGreaterThanOrEqual = retinolGreaterThanOrEqual;
	}

	public void setRetinolLessThanOrEqual(Double retinolLessThanOrEqual) {
		this.retinolLessThanOrEqual = retinolLessThanOrEqual;
	}

	public void setSeason(Integer season) {
		this.season = season;
	}

	public void setShareFlag(String shareFlag) {
		this.shareFlag = shareFlag;
	}

	public void setSuitNo(Integer suitNo) {
		this.suitNo = suitNo;
	}

	public void setSysFlag(String sysFlag) {
		this.sysFlag = sysFlag;
	}

	public void setTenantCanSee(String tenantCanSee) {
		this.tenantCanSee = tenantCanSee;
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

	public void setTypeLike(String typeLike) {
		this.typeLike = typeLike;
	}

	public void setUpdateTimeGreaterThanOrEqual(Date updateTimeGreaterThanOrEqual) {
		this.updateTimeGreaterThanOrEqual = updateTimeGreaterThanOrEqual;
	}

	public void setUpdateTimeLessThanOrEqual(Date updateTimeLessThanOrEqual) {
		this.updateTimeLessThanOrEqual = updateTimeLessThanOrEqual;
	}

	public void setVerifyFlag(String verifyFlag) {
		this.verifyFlag = verifyFlag;
	}

	public void setVitaminAGreaterThanOrEqual(Double vitaminAGreaterThanOrEqual) {
		this.vitaminAGreaterThanOrEqual = vitaminAGreaterThanOrEqual;
	}

	public void setVitaminALessThanOrEqual(Double vitaminALessThanOrEqual) {
		this.vitaminALessThanOrEqual = vitaminALessThanOrEqual;
	}

	public void setVitaminB12GreaterThanOrEqual(Double vitaminB12GreaterThanOrEqual) {
		this.vitaminB12GreaterThanOrEqual = vitaminB12GreaterThanOrEqual;
	}

	public void setVitaminB12LessThanOrEqual(Double vitaminB12LessThanOrEqual) {
		this.vitaminB12LessThanOrEqual = vitaminB12LessThanOrEqual;
	}

	public void setVitaminB1GreaterThanOrEqual(Double vitaminB1GreaterThanOrEqual) {
		this.vitaminB1GreaterThanOrEqual = vitaminB1GreaterThanOrEqual;
	}

	public void setVitaminB1LessThanOrEqual(Double vitaminB1LessThanOrEqual) {
		this.vitaminB1LessThanOrEqual = vitaminB1LessThanOrEqual;
	}

	public void setVitaminB2GreaterThanOrEqual(Double vitaminB2GreaterThanOrEqual) {
		this.vitaminB2GreaterThanOrEqual = vitaminB2GreaterThanOrEqual;
	}

	public void setVitaminB2LessThanOrEqual(Double vitaminB2LessThanOrEqual) {
		this.vitaminB2LessThanOrEqual = vitaminB2LessThanOrEqual;
	}

	public void setVitaminB6GreaterThanOrEqual(Double vitaminB6GreaterThanOrEqual) {
		this.vitaminB6GreaterThanOrEqual = vitaminB6GreaterThanOrEqual;
	}

	public void setVitaminB6LessThanOrEqual(Double vitaminB6LessThanOrEqual) {
		this.vitaminB6LessThanOrEqual = vitaminB6LessThanOrEqual;
	}

	public void setVitaminCGreaterThanOrEqual(Double vitaminCGreaterThanOrEqual) {
		this.vitaminCGreaterThanOrEqual = vitaminCGreaterThanOrEqual;
	}

	public void setVitaminCLessThanOrEqual(Double vitaminCLessThanOrEqual) {
		this.vitaminCLessThanOrEqual = vitaminCLessThanOrEqual;
	}

	public void setWeek(Integer week) {
		this.week = week;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public void setZincGreaterThanOrEqual(Double zincGreaterThanOrEqual) {
		this.zincGreaterThanOrEqual = zincGreaterThanOrEqual;
	}

	public void setZincLessThanOrEqual(Double zincLessThanOrEqual) {
		this.zincLessThanOrEqual = zincLessThanOrEqual;
	}

	public DietaryTemplateQuery shareFlag(String shareFlag) {
		if (shareFlag == null) {
			throw new RuntimeException("shareFlag is null");
		}
		this.shareFlag = shareFlag;
		return this;
	}

	public DietaryTemplateQuery suitNo(Integer suitNo) {
		if (suitNo == null) {
			throw new RuntimeException("suitNo is null");
		}
		this.suitNo = suitNo;
		return this;
	}

	public DietaryTemplateQuery sysFlag(String sysFlag) {
		if (sysFlag == null) {
			throw new RuntimeException("sysFlag is null");
		}
		this.sysFlag = sysFlag;
		return this;
	}

	public DietaryTemplateQuery type(String type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		this.type = type;
		return this;
	}

	public DietaryTemplateQuery typeId(Long typeId) {
		if (typeId == null) {
			throw new RuntimeException("typeId is null");
		}
		this.typeId = typeId;
		return this;
	}

	public DietaryTemplateQuery typeIds(List<Long> typeIds) {
		if (typeIds == null) {
			throw new RuntimeException("typeIds is empty ");
		}
		this.typeIds = typeIds;
		return this;
	}

	public DietaryTemplateQuery typeLike(String typeLike) {
		if (typeLike == null) {
			throw new RuntimeException("type is null");
		}
		this.typeLike = typeLike;
		return this;
	}

	public DietaryTemplateQuery updateTimeGreaterThanOrEqual(Date updateTimeGreaterThanOrEqual) {
		if (updateTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("updateTime is null");
		}
		this.updateTimeGreaterThanOrEqual = updateTimeGreaterThanOrEqual;
		return this;
	}

	public DietaryTemplateQuery updateTimeLessThanOrEqual(Date updateTimeLessThanOrEqual) {
		if (updateTimeLessThanOrEqual == null) {
			throw new RuntimeException("updateTime is null");
		}
		this.updateTimeLessThanOrEqual = updateTimeLessThanOrEqual;
		return this;
	}

	public DietaryTemplateQuery verifyFlag(String verifyFlag) {
		if (verifyFlag == null) {
			throw new RuntimeException("verifyFlag is null");
		}
		this.verifyFlag = verifyFlag;
		return this;
	}

	public DietaryTemplateQuery vitaminAGreaterThanOrEqual(Double vitaminAGreaterThanOrEqual) {
		if (vitaminAGreaterThanOrEqual == null) {
			throw new RuntimeException("vitaminA is null");
		}
		this.vitaminAGreaterThanOrEqual = vitaminAGreaterThanOrEqual;
		return this;
	}

	public DietaryTemplateQuery vitaminALessThanOrEqual(Double vitaminALessThanOrEqual) {
		if (vitaminALessThanOrEqual == null) {
			throw new RuntimeException("vitaminA is null");
		}
		this.vitaminALessThanOrEqual = vitaminALessThanOrEqual;
		return this;
	}

	public DietaryTemplateQuery vitaminB12GreaterThanOrEqual(Double vitaminB12GreaterThanOrEqual) {
		if (vitaminB12GreaterThanOrEqual == null) {
			throw new RuntimeException("vitaminB12 is null");
		}
		this.vitaminB12GreaterThanOrEqual = vitaminB12GreaterThanOrEqual;
		return this;
	}

	public DietaryTemplateQuery vitaminB12LessThanOrEqual(Double vitaminB12LessThanOrEqual) {
		if (vitaminB12LessThanOrEqual == null) {
			throw new RuntimeException("vitaminB12 is null");
		}
		this.vitaminB12LessThanOrEqual = vitaminB12LessThanOrEqual;
		return this;
	}

	public DietaryTemplateQuery vitaminB1GreaterThanOrEqual(Double vitaminB1GreaterThanOrEqual) {
		if (vitaminB1GreaterThanOrEqual == null) {
			throw new RuntimeException("vitaminB1 is null");
		}
		this.vitaminB1GreaterThanOrEqual = vitaminB1GreaterThanOrEqual;
		return this;
	}

	public DietaryTemplateQuery vitaminB1LessThanOrEqual(Double vitaminB1LessThanOrEqual) {
		if (vitaminB1LessThanOrEqual == null) {
			throw new RuntimeException("vitaminB1 is null");
		}
		this.vitaminB1LessThanOrEqual = vitaminB1LessThanOrEqual;
		return this;
	}

	public DietaryTemplateQuery vitaminB2GreaterThanOrEqual(Double vitaminB2GreaterThanOrEqual) {
		if (vitaminB2GreaterThanOrEqual == null) {
			throw new RuntimeException("vitaminB2 is null");
		}
		this.vitaminB2GreaterThanOrEqual = vitaminB2GreaterThanOrEqual;
		return this;
	}

	public DietaryTemplateQuery vitaminB2LessThanOrEqual(Double vitaminB2LessThanOrEqual) {
		if (vitaminB2LessThanOrEqual == null) {
			throw new RuntimeException("vitaminB2 is null");
		}
		this.vitaminB2LessThanOrEqual = vitaminB2LessThanOrEqual;
		return this;
	}

	public DietaryTemplateQuery vitaminB6GreaterThanOrEqual(Double vitaminB6GreaterThanOrEqual) {
		if (vitaminB6GreaterThanOrEqual == null) {
			throw new RuntimeException("vitaminB6 is null");
		}
		this.vitaminB6GreaterThanOrEqual = vitaminB6GreaterThanOrEqual;
		return this;
	}

	public DietaryTemplateQuery vitaminB6LessThanOrEqual(Double vitaminB6LessThanOrEqual) {
		if (vitaminB6LessThanOrEqual == null) {
			throw new RuntimeException("vitaminB6 is null");
		}
		this.vitaminB6LessThanOrEqual = vitaminB6LessThanOrEqual;
		return this;
	}

	public DietaryTemplateQuery vitaminCGreaterThanOrEqual(Double vitaminCGreaterThanOrEqual) {
		if (vitaminCGreaterThanOrEqual == null) {
			throw new RuntimeException("vitaminC is null");
		}
		this.vitaminCGreaterThanOrEqual = vitaminCGreaterThanOrEqual;
		return this;
	}

	public DietaryTemplateQuery vitaminCLessThanOrEqual(Double vitaminCLessThanOrEqual) {
		if (vitaminCLessThanOrEqual == null) {
			throw new RuntimeException("vitaminC is null");
		}
		this.vitaminCLessThanOrEqual = vitaminCLessThanOrEqual;
		return this;
	}

	public DietaryTemplateQuery week(Integer week) {
		if (week == null) {
			throw new RuntimeException("week is null");
		}
		this.week = week;
		return this;
	}

	public DietaryTemplateQuery year(Integer year) {
		if (year == null) {
			throw new RuntimeException("year is null");
		}
		this.year = year;
		return this;
	}

	public DietaryTemplateQuery zincGreaterThanOrEqual(Double zincGreaterThanOrEqual) {
		if (zincGreaterThanOrEqual == null) {
			throw new RuntimeException("zinc is null");
		}
		this.zincGreaterThanOrEqual = zincGreaterThanOrEqual;
		return this;
	}

	public DietaryTemplateQuery zincLessThanOrEqual(Double zincLessThanOrEqual) {
		if (zincLessThanOrEqual == null) {
			throw new RuntimeException("zinc is null");
		}
		this.zincLessThanOrEqual = zincLessThanOrEqual;
		return this;
	}

}