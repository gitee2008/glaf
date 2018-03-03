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

public class FoodCompositionQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected List<Long> ids;
	protected Long nodeId;
	protected List<Long> nodeIds;
	protected String name;
	protected String nameLike;
	protected String alias;
	protected String aliasLike;
	protected String code;
	protected String codeLike;
	protected String discriminator;
	protected String descriptionLike;
	protected Double radicalGreaterThanOrEqual;
	protected Double radicalLessThanOrEqual;
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
	protected String dailyFlag;
	protected String enableFlag;
	protected String sysFlag;
	protected String useFlag;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;
	protected Date updateTimeGreaterThanOrEqual;
	protected Date updateTimeLessThanOrEqual;

	public FoodCompositionQuery() {

	}

	public FoodCompositionQuery alias(String alias) {
		if (alias == null) {
			throw new RuntimeException("alias is null");
		}
		this.alias = alias;
		return this;
	}

	public FoodCompositionQuery aliasLike(String aliasLike) {
		if (aliasLike == null) {
			throw new RuntimeException("alias is null");
		}
		this.aliasLike = aliasLike;
		return this;
	}

	public FoodCompositionQuery calciumGreaterThanOrEqual(Double calciumGreaterThanOrEqual) {
		if (calciumGreaterThanOrEqual == null) {
			throw new RuntimeException("calcium is null");
		}
		this.calciumGreaterThanOrEqual = calciumGreaterThanOrEqual;
		return this;
	}

	public FoodCompositionQuery calciumLessThanOrEqual(Double calciumLessThanOrEqual) {
		if (calciumLessThanOrEqual == null) {
			throw new RuntimeException("calcium is null");
		}
		this.calciumLessThanOrEqual = calciumLessThanOrEqual;
		return this;
	}

	public FoodCompositionQuery carbohydrateGreaterThanOrEqual(Double carbohydrateGreaterThanOrEqual) {
		if (carbohydrateGreaterThanOrEqual == null) {
			throw new RuntimeException("carbohydrate is null");
		}
		this.carbohydrateGreaterThanOrEqual = carbohydrateGreaterThanOrEqual;
		return this;
	}

	public FoodCompositionQuery carbohydrateLessThanOrEqual(Double carbohydrateLessThanOrEqual) {
		if (carbohydrateLessThanOrEqual == null) {
			throw new RuntimeException("carbohydrate is null");
		}
		this.carbohydrateLessThanOrEqual = carbohydrateLessThanOrEqual;
		return this;
	}

	public FoodCompositionQuery caroteneGreaterThanOrEqual(Double caroteneGreaterThanOrEqual) {
		if (caroteneGreaterThanOrEqual == null) {
			throw new RuntimeException("carotene is null");
		}
		this.caroteneGreaterThanOrEqual = caroteneGreaterThanOrEqual;
		return this;
	}

	public FoodCompositionQuery caroteneLessThanOrEqual(Double caroteneLessThanOrEqual) {
		if (caroteneLessThanOrEqual == null) {
			throw new RuntimeException("carotene is null");
		}
		this.caroteneLessThanOrEqual = caroteneLessThanOrEqual;
		return this;
	}

	public FoodCompositionQuery code(String code) {
		if (code == null) {
			throw new RuntimeException("code is null");
		}
		this.code = code;
		return this;
	}

	public FoodCompositionQuery codeLike(String codeLike) {
		if (codeLike == null) {
			throw new RuntimeException("code is null");
		}
		this.codeLike = codeLike;
		return this;
	}

	public FoodCompositionQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public FoodCompositionQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public FoodCompositionQuery dailyFlag(String dailyFlag) {
		if (dailyFlag == null) {
			throw new RuntimeException("dailyFlag is null");
		}
		this.dailyFlag = dailyFlag;
		return this;
	}

	public FoodCompositionQuery descriptionLike(String descriptionLike) {
		if (descriptionLike == null) {
			throw new RuntimeException("description is null");
		}
		this.descriptionLike = descriptionLike;
		return this;
	}

	public FoodCompositionQuery discriminator(String discriminator) {
		if (discriminator == null) {
			throw new RuntimeException("discriminator is null");
		}
		this.discriminator = discriminator;
		return this;
	}

	public FoodCompositionQuery enableFlag(String enableFlag) {
		if (enableFlag == null) {
			throw new RuntimeException("enableFlag is null");
		}
		this.enableFlag = enableFlag;
		return this;
	}

	public FoodCompositionQuery fatGreaterThanOrEqual(Double fatGreaterThanOrEqual) {
		if (fatGreaterThanOrEqual == null) {
			throw new RuntimeException("fat is null");
		}
		this.fatGreaterThanOrEqual = fatGreaterThanOrEqual;
		return this;
	}

	public FoodCompositionQuery fatLessThanOrEqual(Double fatLessThanOrEqual) {
		if (fatLessThanOrEqual == null) {
			throw new RuntimeException("fat is null");
		}
		this.fatLessThanOrEqual = fatLessThanOrEqual;
		return this;
	}

	public String getAlias() {
		return alias;
	}

	public String getAliasLike() {
		if (aliasLike != null && aliasLike.trim().length() > 0) {
			if (!aliasLike.startsWith("%")) {
				aliasLike = "%" + aliasLike;
			}
			if (!aliasLike.endsWith("%")) {
				aliasLike = aliasLike + "%";
			}
		}
		return aliasLike;
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

	public String getCode() {
		return code;
	}

	public String getCodeLike() {
		if (codeLike != null && codeLike.trim().length() > 0) {
			if (!codeLike.startsWith("%")) {
				codeLike = "%" + codeLike;
			}
			if (!codeLike.endsWith("%")) {
				codeLike = codeLike + "%";
			}
		}
		return codeLike;
	}

	public Date getCreateTimeGreaterThanOrEqual() {
		return createTimeGreaterThanOrEqual;
	}

	public Date getCreateTimeLessThanOrEqual() {
		return createTimeLessThanOrEqual;
	}

	public String getDailyFlag() {
		return dailyFlag;
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

	public String getDiscriminator() {
		return discriminator;
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

	public Double getIronGreaterThanOrEqual() {
		return ironGreaterThanOrEqual;
	}

	public Double getIronLessThanOrEqual() {
		return ironLessThanOrEqual;
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

			if ("nodeId".equals(sortColumn)) {
				orderBy = "E.NODEID_" + a_x;
			}

			if ("treeId".equals(sortColumn)) {
				orderBy = "E.TREEID_" + a_x;
			}

			if ("name".equals(sortColumn)) {
				orderBy = "E.NAME_" + a_x;
			}

			if ("alias".equals(sortColumn)) {
				orderBy = "E.ALIAS_" + a_x;
			}

			if ("code".equals(sortColumn)) {
				orderBy = "E.CODE_" + a_x;
			}

			if ("discriminator".equals(sortColumn)) {
				orderBy = "E.DISCRIMINATOR_" + a_x;
			}

			if ("description".equals(sortColumn)) {
				orderBy = "E.DESCRIPTION_" + a_x;
			}

			if ("radical".equals(sortColumn)) {
				orderBy = "E.RADICAL_" + a_x;
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

			if ("sortNo".equals(sortColumn)) {
				orderBy = "E.SORTNO_" + a_x;
			}

			if ("enableFlag".equals(sortColumn)) {
				orderBy = "E.ENABLEFLAG_" + a_x;
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

	public Double getRadicalGreaterThanOrEqual() {
		return radicalGreaterThanOrEqual;
	}

	public Double getRadicalLessThanOrEqual() {
		return radicalLessThanOrEqual;
	}

	public Double getRetinolGreaterThanOrEqual() {
		return retinolGreaterThanOrEqual;
	}

	public Double getRetinolLessThanOrEqual() {
		return retinolLessThanOrEqual;
	}

	public String getSysFlag() {
		return sysFlag;
	}

	public String getTreeId() {
		return treeId;
	}

	public String getTreeIdLike() {
		if (treeIdLike != null && treeIdLike.trim().length() > 0) {
			if (!treeIdLike.startsWith("%")) {
				treeIdLike = "%" + treeIdLike;
			}
			if (!treeIdLike.endsWith("%")) {
				treeIdLike = treeIdLike + "%";
			}
		}
		return treeIdLike;
	}

	public Date getUpdateTimeGreaterThanOrEqual() {
		return updateTimeGreaterThanOrEqual;
	}

	public Date getUpdateTimeLessThanOrEqual() {
		return updateTimeLessThanOrEqual;
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

	public Double getZincGreaterThanOrEqual() {
		return zincGreaterThanOrEqual;
	}

	public Double getZincLessThanOrEqual() {
		return zincLessThanOrEqual;
	}

	public FoodCompositionQuery heatEnergyGreaterThanOrEqual(Double heatEnergyGreaterThanOrEqual) {
		if (heatEnergyGreaterThanOrEqual == null) {
			throw new RuntimeException("heatEnergy is null");
		}
		this.heatEnergyGreaterThanOrEqual = heatEnergyGreaterThanOrEqual;
		return this;
	}

	public FoodCompositionQuery heatEnergyLessThanOrEqual(Double heatEnergyLessThanOrEqual) {
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
		addColumn("nodeId", "NODEID_");
		addColumn("treeId", "TREEID_");
		addColumn("name", "NAME_");
		addColumn("alias", "ALIAS_");
		addColumn("code", "CODE_");
		addColumn("discriminator", "DISCRIMINATOR_");
		addColumn("description", "DESCRIPTION_");
		addColumn("radical", "RADICAL_");
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
		addColumn("sortNo", "SORTNO_");
		addColumn("enableFlag", "ENABLEFLAG_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
		addColumn("updateBy", "UPDATEBY_");
		addColumn("updateTime", "UPDATETIME_");
	}

	public FoodCompositionQuery ironGreaterThanOrEqual(Double ironGreaterThanOrEqual) {
		if (ironGreaterThanOrEqual == null) {
			throw new RuntimeException("iron is null");
		}
		this.ironGreaterThanOrEqual = ironGreaterThanOrEqual;
		return this;
	}

	public FoodCompositionQuery ironLessThanOrEqual(Double ironLessThanOrEqual) {
		if (ironLessThanOrEqual == null) {
			throw new RuntimeException("iron is null");
		}
		this.ironLessThanOrEqual = ironLessThanOrEqual;
		return this;
	}

	public FoodCompositionQuery name(String name) {
		if (name == null) {
			throw new RuntimeException("name is null");
		}
		this.name = name;
		return this;
	}

	public FoodCompositionQuery nameLike(String nameLike) {
		if (nameLike == null) {
			throw new RuntimeException("name is null");
		}
		this.nameLike = nameLike;
		return this;
	}

	public FoodCompositionQuery nicotinicCidGreaterThanOrEqual(Double nicotinicCidGreaterThanOrEqual) {
		if (nicotinicCidGreaterThanOrEqual == null) {
			throw new RuntimeException("nicotinicCid is null");
		}
		this.nicotinicCidGreaterThanOrEqual = nicotinicCidGreaterThanOrEqual;
		return this;
	}

	public FoodCompositionQuery nicotinicCidLessThanOrEqual(Double nicotinicCidLessThanOrEqual) {
		if (nicotinicCidLessThanOrEqual == null) {
			throw new RuntimeException("nicotinicCid is null");
		}
		this.nicotinicCidLessThanOrEqual = nicotinicCidLessThanOrEqual;
		return this;
	}

	public FoodCompositionQuery nodeId(Long nodeId) {
		if (nodeId == null) {
			throw new RuntimeException("nodeId is null");
		}
		this.nodeId = nodeId;
		return this;
	}

	public FoodCompositionQuery nodeIds(List<Long> nodeIds) {
		if (nodeIds == null) {
			throw new RuntimeException("nodeIds is empty ");
		}
		this.nodeIds = nodeIds;
		return this;
	}

	public FoodCompositionQuery proteinGreaterThanOrEqual(Double proteinGreaterThanOrEqual) {
		if (proteinGreaterThanOrEqual == null) {
			throw new RuntimeException("protein is null");
		}
		this.proteinGreaterThanOrEqual = proteinGreaterThanOrEqual;
		return this;
	}

	public FoodCompositionQuery proteinLessThanOrEqual(Double proteinLessThanOrEqual) {
		if (proteinLessThanOrEqual == null) {
			throw new RuntimeException("protein is null");
		}
		this.proteinLessThanOrEqual = proteinLessThanOrEqual;
		return this;
	}

	public FoodCompositionQuery radicalGreaterThanOrEqual(Double radicalGreaterThanOrEqual) {
		if (radicalGreaterThanOrEqual == null) {
			throw new RuntimeException("radical is null");
		}
		this.radicalGreaterThanOrEqual = radicalGreaterThanOrEqual;
		return this;
	}

	public FoodCompositionQuery radicalLessThanOrEqual(Double radicalLessThanOrEqual) {
		if (radicalLessThanOrEqual == null) {
			throw new RuntimeException("radical is null");
		}
		this.radicalLessThanOrEqual = radicalLessThanOrEqual;
		return this;
	}

	public FoodCompositionQuery retinolGreaterThanOrEqual(Double retinolGreaterThanOrEqual) {
		if (retinolGreaterThanOrEqual == null) {
			throw new RuntimeException("retinol is null");
		}
		this.retinolGreaterThanOrEqual = retinolGreaterThanOrEqual;
		return this;
	}

	public FoodCompositionQuery retinolLessThanOrEqual(Double retinolLessThanOrEqual) {
		if (retinolLessThanOrEqual == null) {
			throw new RuntimeException("retinol is null");
		}
		this.retinolLessThanOrEqual = retinolLessThanOrEqual;
		return this;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public void setAliasLike(String aliasLike) {
		this.aliasLike = aliasLike;
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

	public void setCode(String code) {
		this.code = code;
	}

	public void setCodeLike(String codeLike) {
		this.codeLike = codeLike;
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setDailyFlag(String dailyFlag) {
		this.dailyFlag = dailyFlag;
	}

	public void setDescriptionLike(String descriptionLike) {
		this.descriptionLike = descriptionLike;
	}

	public void setDiscriminator(String discriminator) {
		this.discriminator = discriminator;
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

	public void setIronGreaterThanOrEqual(Double ironGreaterThanOrEqual) {
		this.ironGreaterThanOrEqual = ironGreaterThanOrEqual;
	}

	public void setIronLessThanOrEqual(Double ironLessThanOrEqual) {
		this.ironLessThanOrEqual = ironLessThanOrEqual;
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

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public void setNodeIds(List<Long> nodeIds) {
		this.nodeIds = nodeIds;
	}

	public void setProteinGreaterThanOrEqual(Double proteinGreaterThanOrEqual) {
		this.proteinGreaterThanOrEqual = proteinGreaterThanOrEqual;
	}

	public void setProteinLessThanOrEqual(Double proteinLessThanOrEqual) {
		this.proteinLessThanOrEqual = proteinLessThanOrEqual;
	}

	public void setRadicalGreaterThanOrEqual(Double radicalGreaterThanOrEqual) {
		this.radicalGreaterThanOrEqual = radicalGreaterThanOrEqual;
	}

	public void setRadicalLessThanOrEqual(Double radicalLessThanOrEqual) {
		this.radicalLessThanOrEqual = radicalLessThanOrEqual;
	}

	public void setRetinolGreaterThanOrEqual(Double retinolGreaterThanOrEqual) {
		this.retinolGreaterThanOrEqual = retinolGreaterThanOrEqual;
	}

	public void setRetinolLessThanOrEqual(Double retinolLessThanOrEqual) {
		this.retinolLessThanOrEqual = retinolLessThanOrEqual;
	}

	public void setSysFlag(String sysFlag) {
		this.sysFlag = sysFlag;
	}

	public void setTreeId(String treeId) {
		this.treeId = treeId;
	}

	public void setTreeIdLike(String treeIdLike) {
		this.treeIdLike = treeIdLike;
	}

	public void setUpdateTimeGreaterThanOrEqual(Date updateTimeGreaterThanOrEqual) {
		this.updateTimeGreaterThanOrEqual = updateTimeGreaterThanOrEqual;
	}

	public void setUpdateTimeLessThanOrEqual(Date updateTimeLessThanOrEqual) {
		this.updateTimeLessThanOrEqual = updateTimeLessThanOrEqual;
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

	public void setZincGreaterThanOrEqual(Double zincGreaterThanOrEqual) {
		this.zincGreaterThanOrEqual = zincGreaterThanOrEqual;
	}

	public void setZincLessThanOrEqual(Double zincLessThanOrEqual) {
		this.zincLessThanOrEqual = zincLessThanOrEqual;
	}

	public FoodCompositionQuery sysFlag(String sysFlag) {
		if (sysFlag == null) {
			throw new RuntimeException("sysFlag is null");
		}
		this.sysFlag = sysFlag;
		return this;
	}

	public FoodCompositionQuery treeId(String treeId) {
		if (treeId == null) {
			throw new RuntimeException("treeId is null");
		}
		this.treeId = treeId;
		return this;
	}

	public FoodCompositionQuery treeIdLike(String treeIdLike) {
		if (treeIdLike == null) {
			throw new RuntimeException("treeId is null");
		}
		this.treeIdLike = treeIdLike;
		return this;
	}

	public FoodCompositionQuery updateTimeGreaterThanOrEqual(Date updateTimeGreaterThanOrEqual) {
		if (updateTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("updateTime is null");
		}
		this.updateTimeGreaterThanOrEqual = updateTimeGreaterThanOrEqual;
		return this;
	}

	public FoodCompositionQuery updateTimeLessThanOrEqual(Date updateTimeLessThanOrEqual) {
		if (updateTimeLessThanOrEqual == null) {
			throw new RuntimeException("updateTime is null");
		}
		this.updateTimeLessThanOrEqual = updateTimeLessThanOrEqual;
		return this;
	}

	public FoodCompositionQuery useFlag(String useFlag) {
		if (useFlag == null) {
			throw new RuntimeException("useFlag is null");
		}
		this.useFlag = useFlag;
		return this;
	}

	public FoodCompositionQuery vitaminAGreaterThanOrEqual(Double vitaminAGreaterThanOrEqual) {
		if (vitaminAGreaterThanOrEqual == null) {
			throw new RuntimeException("vitaminA is null");
		}
		this.vitaminAGreaterThanOrEqual = vitaminAGreaterThanOrEqual;
		return this;
	}

	public FoodCompositionQuery vitaminALessThanOrEqual(Double vitaminALessThanOrEqual) {
		if (vitaminALessThanOrEqual == null) {
			throw new RuntimeException("vitaminA is null");
		}
		this.vitaminALessThanOrEqual = vitaminALessThanOrEqual;
		return this;
	}

	public FoodCompositionQuery vitaminB12GreaterThanOrEqual(Double vitaminB12GreaterThanOrEqual) {
		if (vitaminB12GreaterThanOrEqual == null) {
			throw new RuntimeException("vitaminB12 is null");
		}
		this.vitaminB12GreaterThanOrEqual = vitaminB12GreaterThanOrEqual;
		return this;
	}

	public FoodCompositionQuery vitaminB12LessThanOrEqual(Double vitaminB12LessThanOrEqual) {
		if (vitaminB12LessThanOrEqual == null) {
			throw new RuntimeException("vitaminB12 is null");
		}
		this.vitaminB12LessThanOrEqual = vitaminB12LessThanOrEqual;
		return this;
	}

	public FoodCompositionQuery vitaminB1GreaterThanOrEqual(Double vitaminB1GreaterThanOrEqual) {
		if (vitaminB1GreaterThanOrEqual == null) {
			throw new RuntimeException("vitaminB1 is null");
		}
		this.vitaminB1GreaterThanOrEqual = vitaminB1GreaterThanOrEqual;
		return this;
	}

	public FoodCompositionQuery vitaminB1LessThanOrEqual(Double vitaminB1LessThanOrEqual) {
		if (vitaminB1LessThanOrEqual == null) {
			throw new RuntimeException("vitaminB1 is null");
		}
		this.vitaminB1LessThanOrEqual = vitaminB1LessThanOrEqual;
		return this;
	}

	public FoodCompositionQuery vitaminB2GreaterThanOrEqual(Double vitaminB2GreaterThanOrEqual) {
		if (vitaminB2GreaterThanOrEqual == null) {
			throw new RuntimeException("vitaminB2 is null");
		}
		this.vitaminB2GreaterThanOrEqual = vitaminB2GreaterThanOrEqual;
		return this;
	}

	public FoodCompositionQuery vitaminB2LessThanOrEqual(Double vitaminB2LessThanOrEqual) {
		if (vitaminB2LessThanOrEqual == null) {
			throw new RuntimeException("vitaminB2 is null");
		}
		this.vitaminB2LessThanOrEqual = vitaminB2LessThanOrEqual;
		return this;
	}

	public FoodCompositionQuery vitaminB6GreaterThanOrEqual(Double vitaminB6GreaterThanOrEqual) {
		if (vitaminB6GreaterThanOrEqual == null) {
			throw new RuntimeException("vitaminB6 is null");
		}
		this.vitaminB6GreaterThanOrEqual = vitaminB6GreaterThanOrEqual;
		return this;
	}

	public FoodCompositionQuery vitaminB6LessThanOrEqual(Double vitaminB6LessThanOrEqual) {
		if (vitaminB6LessThanOrEqual == null) {
			throw new RuntimeException("vitaminB6 is null");
		}
		this.vitaminB6LessThanOrEqual = vitaminB6LessThanOrEqual;
		return this;
	}

	public FoodCompositionQuery vitaminCGreaterThanOrEqual(Double vitaminCGreaterThanOrEqual) {
		if (vitaminCGreaterThanOrEqual == null) {
			throw new RuntimeException("vitaminC is null");
		}
		this.vitaminCGreaterThanOrEqual = vitaminCGreaterThanOrEqual;
		return this;
	}

	public FoodCompositionQuery vitaminCLessThanOrEqual(Double vitaminCLessThanOrEqual) {
		if (vitaminCLessThanOrEqual == null) {
			throw new RuntimeException("vitaminC is null");
		}
		this.vitaminCLessThanOrEqual = vitaminCLessThanOrEqual;
		return this;
	}

	public FoodCompositionQuery zincGreaterThanOrEqual(Double zincGreaterThanOrEqual) {
		if (zincGreaterThanOrEqual == null) {
			throw new RuntimeException("zinc is null");
		}
		this.zincGreaterThanOrEqual = zincGreaterThanOrEqual;
		return this;
	}

	public FoodCompositionQuery zincLessThanOrEqual(Double zincLessThanOrEqual) {
		if (zincLessThanOrEqual == null) {
			throw new RuntimeException("zinc is null");
		}
		this.zincLessThanOrEqual = zincLessThanOrEqual;
		return this;
	}

}