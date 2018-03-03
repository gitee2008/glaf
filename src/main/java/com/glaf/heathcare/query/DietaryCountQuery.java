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

public class DietaryCountQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected Long id;
	protected List<Long> ids;
	protected List<String> tenantIds;
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
	protected Double iodineGreaterThanOrEqual;
	protected Double iodineLessThanOrEqual;
	protected Double phosphorusGreaterThanOrEqual;
	protected Double phosphorusLessThanOrEqual;
	protected String type;
	protected Long nodeId;
	protected List<Long> nodeIds;
	protected Integer semester;
	protected Integer year;
	protected Integer month;
	protected Integer day;
	protected Integer dayOfWeek;
	protected Integer week;
	protected Integer fullDay;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;
	protected String tableSuffix;

	public DietaryCountQuery() {

	}

	public DietaryCountQuery calciumGreaterThanOrEqual(Double calciumGreaterThanOrEqual) {
		if (calciumGreaterThanOrEqual == null) {
			throw new RuntimeException("calcium is null");
		}
		this.calciumGreaterThanOrEqual = calciumGreaterThanOrEqual;
		return this;
	}

	public DietaryCountQuery calciumLessThanOrEqual(Double calciumLessThanOrEqual) {
		if (calciumLessThanOrEqual == null) {
			throw new RuntimeException("calcium is null");
		}
		this.calciumLessThanOrEqual = calciumLessThanOrEqual;
		return this;
	}

	public DietaryCountQuery carbohydrateGreaterThanOrEqual(Double carbohydrateGreaterThanOrEqual) {
		if (carbohydrateGreaterThanOrEqual == null) {
			throw new RuntimeException("carbohydrate is null");
		}
		this.carbohydrateGreaterThanOrEqual = carbohydrateGreaterThanOrEqual;
		return this;
	}

	public DietaryCountQuery carbohydrateLessThanOrEqual(Double carbohydrateLessThanOrEqual) {
		if (carbohydrateLessThanOrEqual == null) {
			throw new RuntimeException("carbohydrate is null");
		}
		this.carbohydrateLessThanOrEqual = carbohydrateLessThanOrEqual;
		return this;
	}

	public DietaryCountQuery caroteneGreaterThanOrEqual(Double caroteneGreaterThanOrEqual) {
		if (caroteneGreaterThanOrEqual == null) {
			throw new RuntimeException("carotene is null");
		}
		this.caroteneGreaterThanOrEqual = caroteneGreaterThanOrEqual;
		return this;
	}

	public DietaryCountQuery caroteneLessThanOrEqual(Double caroteneLessThanOrEqual) {
		if (caroteneLessThanOrEqual == null) {
			throw new RuntimeException("carotene is null");
		}
		this.caroteneLessThanOrEqual = caroteneLessThanOrEqual;
		return this;
	}

	public DietaryCountQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public DietaryCountQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public DietaryCountQuery day(Integer day) {
		if (day == null) {
			throw new RuntimeException("day is null");
		}
		this.day = day;
		return this;
	}

	public DietaryCountQuery dayOfWeek(Integer dayOfWeek) {
		if (dayOfWeek == null) {
			throw new RuntimeException("dayOfWeek is null");
		}
		this.dayOfWeek = dayOfWeek;
		return this;
	}

	public DietaryCountQuery fatGreaterThanOrEqual(Double fatGreaterThanOrEqual) {
		if (fatGreaterThanOrEqual == null) {
			throw new RuntimeException("fat is null");
		}
		this.fatGreaterThanOrEqual = fatGreaterThanOrEqual;
		return this;
	}

	public DietaryCountQuery fatLessThanOrEqual(Double fatLessThanOrEqual) {
		if (fatLessThanOrEqual == null) {
			throw new RuntimeException("fat is null");
		}
		this.fatLessThanOrEqual = fatLessThanOrEqual;
		return this;
	}

	public DietaryCountQuery fullDay(Integer fullDay) {
		if (fullDay == null) {
			throw new RuntimeException("fullDay is null");
		}
		this.fullDay = fullDay;
		return this;
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

	public Double getFatGreaterThanOrEqual() {
		return fatGreaterThanOrEqual;
	}

	public Double getFatLessThanOrEqual() {
		return fatLessThanOrEqual;
	}

	public Integer getFullDay() {
		return fullDay;
	}

	public Double getHeatEnergyGreaterThanOrEqual() {
		return heatEnergyGreaterThanOrEqual;
	}

	public Double getHeatEnergyLessThanOrEqual() {
		return heatEnergyLessThanOrEqual;
	}

	public Long getId() {
		return id;
	}

	public List<Long> getIds() {
		return ids;
	}

	public Double getIodineGreaterThanOrEqual() {
		return iodineGreaterThanOrEqual;
	}

	public Double getIodineLessThanOrEqual() {
		return iodineLessThanOrEqual;
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

	public Double getNicotinicCidGreaterThanOrEqual() {
		return nicotinicCidGreaterThanOrEqual;
	}

	public Double getNicotinicCidLessThanOrEqual() {
		return nicotinicCidLessThanOrEqual;
	}

	public Long getNodeId() {
		return nodeId;
	}

	public List<Long> getNodeIds() {
		return nodeIds;
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

			if ("dayOfWeek".equals(sortColumn)) {
				orderBy = "E.DAYOFWEEK_" + a_x;
			}

			if ("week".equals(sortColumn)) {
				orderBy = "E.WEEK_" + a_x;
			}

			if ("fullDay".equals(sortColumn)) {
				orderBy = "E.FULLDAY_" + a_x;
			}

			if ("createTime".equals(sortColumn)) {
				orderBy = "E.CREATETIME_" + a_x;
			}

		}
		return orderBy;
	}

	public Double getPhosphorusGreaterThanOrEqual() {
		return phosphorusGreaterThanOrEqual;
	}

	public Double getPhosphorusLessThanOrEqual() {
		return phosphorusLessThanOrEqual;
	}

	public Double getProteinGreaterThanOrEqual() {
		return proteinGreaterThanOrEqual;
	}

	public Double getProteinLessThanOrEqual() {
		return proteinLessThanOrEqual;
	}

	public Double getRetinolGreaterThanOrEqual() {
		return retinolGreaterThanOrEqual;
	}

	public Double getRetinolLessThanOrEqual() {
		return retinolLessThanOrEqual;
	}

	public Integer getSemester() {
		return semester;
	}

	public String getTableSuffix() {
		if (tableSuffix == null) {
			tableSuffix = "";
		}
		return tableSuffix;
	}

	public List<String> getTenantIds() {
		return tenantIds;
	}

	public String getType() {
		return type;
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

	public DietaryCountQuery heatEnergyGreaterThanOrEqual(Double heatEnergyGreaterThanOrEqual) {
		if (heatEnergyGreaterThanOrEqual == null) {
			throw new RuntimeException("heatEnergy is null");
		}
		this.heatEnergyGreaterThanOrEqual = heatEnergyGreaterThanOrEqual;
		return this;
	}

	public DietaryCountQuery heatEnergyLessThanOrEqual(Double heatEnergyLessThanOrEqual) {
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
		addColumn("tenantId", "TENANTID_");
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
		addColumn("dayOfWeek", "DAYOFWEEK_");
		addColumn("week", "WEEK_");
		addColumn("fullDay", "FULLDAY_");
		addColumn("createTime", "CREATETIME_");
	}

	public DietaryCountQuery iodineGreaterThanOrEqual(Double iodineGreaterThanOrEqual) {
		if (iodineGreaterThanOrEqual == null) {
			throw new RuntimeException("iodine is null");
		}
		this.iodineGreaterThanOrEqual = iodineGreaterThanOrEqual;
		return this;
	}

	public DietaryCountQuery iodineLessThanOrEqual(Double iodineLessThanOrEqual) {
		if (iodineLessThanOrEqual == null) {
			throw new RuntimeException("iodine is null");
		}
		this.iodineLessThanOrEqual = iodineLessThanOrEqual;
		return this;
	}

	public DietaryCountQuery ironGreaterThanOrEqual(Double ironGreaterThanOrEqual) {
		if (ironGreaterThanOrEqual == null) {
			throw new RuntimeException("iron is null");
		}
		this.ironGreaterThanOrEqual = ironGreaterThanOrEqual;
		return this;
	}

	public DietaryCountQuery ironLessThanOrEqual(Double ironLessThanOrEqual) {
		if (ironLessThanOrEqual == null) {
			throw new RuntimeException("iron is null");
		}
		this.ironLessThanOrEqual = ironLessThanOrEqual;
		return this;
	}

	public DietaryCountQuery month(Integer month) {
		if (month == null) {
			throw new RuntimeException("month is null");
		}
		this.month = month;
		return this;
	}

	public DietaryCountQuery nicotinicCidGreaterThanOrEqual(Double nicotinicCidGreaterThanOrEqual) {
		if (nicotinicCidGreaterThanOrEqual == null) {
			throw new RuntimeException("nicotinicCid is null");
		}
		this.nicotinicCidGreaterThanOrEqual = nicotinicCidGreaterThanOrEqual;
		return this;
	}

	public DietaryCountQuery nicotinicCidLessThanOrEqual(Double nicotinicCidLessThanOrEqual) {
		if (nicotinicCidLessThanOrEqual == null) {
			throw new RuntimeException("nicotinicCid is null");
		}
		this.nicotinicCidLessThanOrEqual = nicotinicCidLessThanOrEqual;
		return this;
	}

	public DietaryCountQuery nodeId(Long nodeId) {
		if (nodeId == null) {
			throw new RuntimeException("nodeId is null");
		}
		this.nodeId = nodeId;
		return this;
	}

	public DietaryCountQuery nodeIds(List<Long> nodeIds) {
		if (nodeIds == null) {
			throw new RuntimeException("nodeIds is empty ");
		}
		this.nodeIds = nodeIds;
		return this;
	}

	public DietaryCountQuery parentId(Long parentId) {
		if (parentId == null) {
			throw new RuntimeException("parentId is null");
		}
		this.parentId = parentId;
		return this;
	}

	public DietaryCountQuery parentIds(List<Long> parentIds) {
		if (parentIds == null) {
			throw new RuntimeException("parentIds is empty ");
		}
		this.parentIds = parentIds;
		return this;
	}

	public DietaryCountQuery phosphorusGreaterThanOrEqual(Double phosphorusGreaterThanOrEqual) {
		if (phosphorusGreaterThanOrEqual == null) {
			throw new RuntimeException("phosphorus is null");
		}
		this.phosphorusGreaterThanOrEqual = phosphorusGreaterThanOrEqual;
		return this;
	}

	public DietaryCountQuery phosphorusLessThanOrEqual(Double phosphorusLessThanOrEqual) {
		if (phosphorusLessThanOrEqual == null) {
			throw new RuntimeException("phosphorus is null");
		}
		this.phosphorusLessThanOrEqual = phosphorusLessThanOrEqual;
		return this;
	}

	public DietaryCountQuery proteinGreaterThanOrEqual(Double proteinGreaterThanOrEqual) {
		if (proteinGreaterThanOrEqual == null) {
			throw new RuntimeException("protein is null");
		}
		this.proteinGreaterThanOrEqual = proteinGreaterThanOrEqual;
		return this;
	}

	public DietaryCountQuery proteinLessThanOrEqual(Double proteinLessThanOrEqual) {
		if (proteinLessThanOrEqual == null) {
			throw new RuntimeException("protein is null");
		}
		this.proteinLessThanOrEqual = proteinLessThanOrEqual;
		return this;
	}

	public DietaryCountQuery retinolGreaterThanOrEqual(Double retinolGreaterThanOrEqual) {
		if (retinolGreaterThanOrEqual == null) {
			throw new RuntimeException("retinol is null");
		}
		this.retinolGreaterThanOrEqual = retinolGreaterThanOrEqual;
		return this;
	}

	public DietaryCountQuery retinolLessThanOrEqual(Double retinolLessThanOrEqual) {
		if (retinolLessThanOrEqual == null) {
			throw new RuntimeException("retinol is null");
		}
		this.retinolLessThanOrEqual = retinolLessThanOrEqual;
		return this;
	}

	public DietaryCountQuery semester(Integer semester) {
		if (semester == null) {
			throw new RuntimeException("semester is null");
		}
		this.semester = semester;
		return this;
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

	public void setFatGreaterThanOrEqual(Double fatGreaterThanOrEqual) {
		this.fatGreaterThanOrEqual = fatGreaterThanOrEqual;
	}

	public void setFatLessThanOrEqual(Double fatLessThanOrEqual) {
		this.fatLessThanOrEqual = fatLessThanOrEqual;
	}

	public void setFullDay(Integer fullDay) {
		this.fullDay = fullDay;
	}

	public void setHeatEnergyGreaterThanOrEqual(Double heatEnergyGreaterThanOrEqual) {
		this.heatEnergyGreaterThanOrEqual = heatEnergyGreaterThanOrEqual;
	}

	public void setHeatEnergyLessThanOrEqual(Double heatEnergyLessThanOrEqual) {
		this.heatEnergyLessThanOrEqual = heatEnergyLessThanOrEqual;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}

	public void setIodineGreaterThanOrEqual(Double iodineGreaterThanOrEqual) {
		this.iodineGreaterThanOrEqual = iodineGreaterThanOrEqual;
	}

	public void setIodineLessThanOrEqual(Double iodineLessThanOrEqual) {
		this.iodineLessThanOrEqual = iodineLessThanOrEqual;
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

	public void setNicotinicCidGreaterThanOrEqual(Double nicotinicCidGreaterThanOrEqual) {
		this.nicotinicCidGreaterThanOrEqual = nicotinicCidGreaterThanOrEqual;
	}

	public void setNicotinicCidLessThanOrEqual(Double nicotinicCidLessThanOrEqual) {
		this.nicotinicCidLessThanOrEqual = nicotinicCidLessThanOrEqual;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public void setNodeIds(List<Long> nodeIds) {
		this.nodeIds = nodeIds;
	}

	public void setPhosphorusGreaterThanOrEqual(Double phosphorusGreaterThanOrEqual) {
		this.phosphorusGreaterThanOrEqual = phosphorusGreaterThanOrEqual;
	}

	public void setPhosphorusLessThanOrEqual(Double phosphorusLessThanOrEqual) {
		this.phosphorusLessThanOrEqual = phosphorusLessThanOrEqual;
	}

	public void setProteinGreaterThanOrEqual(Double proteinGreaterThanOrEqual) {
		this.proteinGreaterThanOrEqual = proteinGreaterThanOrEqual;
	}

	public void setProteinLessThanOrEqual(Double proteinLessThanOrEqual) {
		this.proteinLessThanOrEqual = proteinLessThanOrEqual;
	}

	public void setRetinolGreaterThanOrEqual(Double retinolGreaterThanOrEqual) {
		this.retinolGreaterThanOrEqual = retinolGreaterThanOrEqual;
	}

	public void setRetinolLessThanOrEqual(Double retinolLessThanOrEqual) {
		this.retinolLessThanOrEqual = retinolLessThanOrEqual;
	}

	public void setSemester(Integer semester) {
		this.semester = semester;
	}

	public void setTableSuffix(String tableSuffix) {
		this.tableSuffix = tableSuffix;
	}

	public void setTenantIds(List<String> tenantIds) {
		this.tenantIds = tenantIds;
	}

	public void setType(String type) {
		this.type = type;
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

	public DietaryCountQuery tenantIds(List<String> tenantIds) {
		if (tenantIds == null) {
			throw new RuntimeException("tenantIds is empty ");
		}
		this.tenantIds = tenantIds;
		return this;
	}

	public DietaryCountQuery type(String type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		this.type = type;
		return this;
	}

	public DietaryCountQuery vitaminAGreaterThanOrEqual(Double vitaminAGreaterThanOrEqual) {
		if (vitaminAGreaterThanOrEqual == null) {
			throw new RuntimeException("vitaminA is null");
		}
		this.vitaminAGreaterThanOrEqual = vitaminAGreaterThanOrEqual;
		return this;
	}

	public DietaryCountQuery vitaminALessThanOrEqual(Double vitaminALessThanOrEqual) {
		if (vitaminALessThanOrEqual == null) {
			throw new RuntimeException("vitaminA is null");
		}
		this.vitaminALessThanOrEqual = vitaminALessThanOrEqual;
		return this;
	}

	public DietaryCountQuery vitaminB12GreaterThanOrEqual(Double vitaminB12GreaterThanOrEqual) {
		if (vitaminB12GreaterThanOrEqual == null) {
			throw new RuntimeException("vitaminB12 is null");
		}
		this.vitaminB12GreaterThanOrEqual = vitaminB12GreaterThanOrEqual;
		return this;
	}

	public DietaryCountQuery vitaminB12LessThanOrEqual(Double vitaminB12LessThanOrEqual) {
		if (vitaminB12LessThanOrEqual == null) {
			throw new RuntimeException("vitaminB12 is null");
		}
		this.vitaminB12LessThanOrEqual = vitaminB12LessThanOrEqual;
		return this;
	}

	public DietaryCountQuery vitaminB1GreaterThanOrEqual(Double vitaminB1GreaterThanOrEqual) {
		if (vitaminB1GreaterThanOrEqual == null) {
			throw new RuntimeException("vitaminB1 is null");
		}
		this.vitaminB1GreaterThanOrEqual = vitaminB1GreaterThanOrEqual;
		return this;
	}

	public DietaryCountQuery vitaminB1LessThanOrEqual(Double vitaminB1LessThanOrEqual) {
		if (vitaminB1LessThanOrEqual == null) {
			throw new RuntimeException("vitaminB1 is null");
		}
		this.vitaminB1LessThanOrEqual = vitaminB1LessThanOrEqual;
		return this;
	}

	public DietaryCountQuery vitaminB2GreaterThanOrEqual(Double vitaminB2GreaterThanOrEqual) {
		if (vitaminB2GreaterThanOrEqual == null) {
			throw new RuntimeException("vitaminB2 is null");
		}
		this.vitaminB2GreaterThanOrEqual = vitaminB2GreaterThanOrEqual;
		return this;
	}

	public DietaryCountQuery vitaminB2LessThanOrEqual(Double vitaminB2LessThanOrEqual) {
		if (vitaminB2LessThanOrEqual == null) {
			throw new RuntimeException("vitaminB2 is null");
		}
		this.vitaminB2LessThanOrEqual = vitaminB2LessThanOrEqual;
		return this;
	}

	public DietaryCountQuery vitaminB6GreaterThanOrEqual(Double vitaminB6GreaterThanOrEqual) {
		if (vitaminB6GreaterThanOrEqual == null) {
			throw new RuntimeException("vitaminB6 is null");
		}
		this.vitaminB6GreaterThanOrEqual = vitaminB6GreaterThanOrEqual;
		return this;
	}

	public DietaryCountQuery vitaminB6LessThanOrEqual(Double vitaminB6LessThanOrEqual) {
		if (vitaminB6LessThanOrEqual == null) {
			throw new RuntimeException("vitaminB6 is null");
		}
		this.vitaminB6LessThanOrEqual = vitaminB6LessThanOrEqual;
		return this;
	}

	public DietaryCountQuery vitaminCGreaterThanOrEqual(Double vitaminCGreaterThanOrEqual) {
		if (vitaminCGreaterThanOrEqual == null) {
			throw new RuntimeException("vitaminC is null");
		}
		this.vitaminCGreaterThanOrEqual = vitaminCGreaterThanOrEqual;
		return this;
	}

	public DietaryCountQuery vitaminCLessThanOrEqual(Double vitaminCLessThanOrEqual) {
		if (vitaminCLessThanOrEqual == null) {
			throw new RuntimeException("vitaminC is null");
		}
		this.vitaminCLessThanOrEqual = vitaminCLessThanOrEqual;
		return this;
	}

	public DietaryCountQuery week(Integer week) {
		if (week == null) {
			throw new RuntimeException("week is null");
		}
		this.week = week;
		return this;
	}

	public DietaryCountQuery year(Integer year) {
		if (year == null) {
			throw new RuntimeException("year is null");
		}
		this.year = year;
		return this;
	}

	public DietaryCountQuery zincGreaterThanOrEqual(Double zincGreaterThanOrEqual) {
		if (zincGreaterThanOrEqual == null) {
			throw new RuntimeException("zinc is null");
		}
		this.zincGreaterThanOrEqual = zincGreaterThanOrEqual;
		return this;
	}

	public DietaryCountQuery zincLessThanOrEqual(Double zincLessThanOrEqual) {
		if (zincLessThanOrEqual == null) {
			throw new RuntimeException("zinc is null");
		}
		this.zincLessThanOrEqual = zincLessThanOrEqual;
		return this;
	}

}