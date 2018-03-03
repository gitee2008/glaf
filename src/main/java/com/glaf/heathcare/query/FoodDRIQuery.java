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

public class FoodDRIQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected String name;
	protected String nameLike;
	protected String descriptionLike;
	protected Integer age;
	protected Integer ageGreaterThanOrEqual;
	protected Integer ageLessThanOrEqual;
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
	protected String enableFlag;

	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	protected Date updateTimeGreaterThanOrEqual;
	protected Date updateTimeLessThanOrEqual;

	public FoodDRIQuery() {

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

	public Integer getAge() {
		return age;
	}

	public Integer getAgeGreaterThanOrEqual() {
		return ageGreaterThanOrEqual;
	}

	public Integer getAgeLessThanOrEqual() {
		return ageLessThanOrEqual;
	}

	public String getType() {
		return type;
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

	public Long getTypeId() {
		return typeId;
	}

	public List<Long> getTypeIds() {
		return typeIds;
	}

	public Double getHeatEnergyGreaterThanOrEqual() {
		return heatEnergyGreaterThanOrEqual;
	}

	public Double getHeatEnergyLessThanOrEqual() {
		return heatEnergyLessThanOrEqual;
	}

	public Double getProteinGreaterThanOrEqual() {
		return proteinGreaterThanOrEqual;
	}

	public Double getProteinLessThanOrEqual() {
		return proteinLessThanOrEqual;
	}

	public Double getFatGreaterThanOrEqual() {
		return fatGreaterThanOrEqual;
	}

	public Double getFatLessThanOrEqual() {
		return fatLessThanOrEqual;
	}

	public Double getCarbohydrateGreaterThanOrEqual() {
		return carbohydrateGreaterThanOrEqual;
	}

	public Double getCarbohydrateLessThanOrEqual() {
		return carbohydrateLessThanOrEqual;
	}

	public Double getVitaminAGreaterThanOrEqual() {
		return vitaminAGreaterThanOrEqual;
	}

	public Double getVitaminALessThanOrEqual() {
		return vitaminALessThanOrEqual;
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

	public Double getVitaminB12GreaterThanOrEqual() {
		return vitaminB12GreaterThanOrEqual;
	}

	public Double getVitaminB12LessThanOrEqual() {
		return vitaminB12LessThanOrEqual;
	}

	public Double getVitaminCGreaterThanOrEqual() {
		return vitaminCGreaterThanOrEqual;
	}

	public Double getVitaminCLessThanOrEqual() {
		return vitaminCLessThanOrEqual;
	}

	public Double getCaroteneGreaterThanOrEqual() {
		return caroteneGreaterThanOrEqual;
	}

	public Double getCaroteneLessThanOrEqual() {
		return caroteneLessThanOrEqual;
	}

	public Double getRetinolGreaterThanOrEqual() {
		return retinolGreaterThanOrEqual;
	}

	public Double getRetinolLessThanOrEqual() {
		return retinolLessThanOrEqual;
	}

	public Double getNicotinicCidGreaterThanOrEqual() {
		return nicotinicCidGreaterThanOrEqual;
	}

	public Double getNicotinicCidLessThanOrEqual() {
		return nicotinicCidLessThanOrEqual;
	}

	public Double getCalciumGreaterThanOrEqual() {
		return calciumGreaterThanOrEqual;
	}

	public Double getCalciumLessThanOrEqual() {
		return calciumLessThanOrEqual;
	}

	public Double getIronGreaterThanOrEqual() {
		return ironGreaterThanOrEqual;
	}

	public Double getIronLessThanOrEqual() {
		return ironLessThanOrEqual;
	}

	public Double getZincGreaterThanOrEqual() {
		return zincGreaterThanOrEqual;
	}

	public Double getZincLessThanOrEqual() {
		return zincLessThanOrEqual;
	}

	public String getEnableFlag() {
		return enableFlag;
	}

	public Date getCreateTimeGreaterThanOrEqual() {
		return createTimeGreaterThanOrEqual;
	}

	public Date getCreateTimeLessThanOrEqual() {
		return createTimeLessThanOrEqual;
	}

	public Date getUpdateTimeGreaterThanOrEqual() {
		return updateTimeGreaterThanOrEqual;
	}

	public Date getUpdateTimeLessThanOrEqual() {
		return updateTimeLessThanOrEqual;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNameLike(String nameLike) {
		this.nameLike = nameLike;
	}

	public void setDescriptionLike(String descriptionLike) {
		this.descriptionLike = descriptionLike;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public void setAgeGreaterThanOrEqual(Integer ageGreaterThanOrEqual) {
		this.ageGreaterThanOrEqual = ageGreaterThanOrEqual;
	}

	public void setAgeLessThanOrEqual(Integer ageLessThanOrEqual) {
		this.ageLessThanOrEqual = ageLessThanOrEqual;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setTypeLike(String typeLike) {
		this.typeLike = typeLike;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public void setTypeIds(List<Long> typeIds) {
		this.typeIds = typeIds;
	}

	public void setHeatEnergyGreaterThanOrEqual(Double heatEnergyGreaterThanOrEqual) {
		this.heatEnergyGreaterThanOrEqual = heatEnergyGreaterThanOrEqual;
	}

	public void setHeatEnergyLessThanOrEqual(Double heatEnergyLessThanOrEqual) {
		this.heatEnergyLessThanOrEqual = heatEnergyLessThanOrEqual;
	}

	public void setProteinGreaterThanOrEqual(Double proteinGreaterThanOrEqual) {
		this.proteinGreaterThanOrEqual = proteinGreaterThanOrEqual;
	}

	public void setProteinLessThanOrEqual(Double proteinLessThanOrEqual) {
		this.proteinLessThanOrEqual = proteinLessThanOrEqual;
	}

	public void setFatGreaterThanOrEqual(Double fatGreaterThanOrEqual) {
		this.fatGreaterThanOrEqual = fatGreaterThanOrEqual;
	}

	public void setFatLessThanOrEqual(Double fatLessThanOrEqual) {
		this.fatLessThanOrEqual = fatLessThanOrEqual;
	}

	public void setCarbohydrateGreaterThanOrEqual(Double carbohydrateGreaterThanOrEqual) {
		this.carbohydrateGreaterThanOrEqual = carbohydrateGreaterThanOrEqual;
	}

	public void setCarbohydrateLessThanOrEqual(Double carbohydrateLessThanOrEqual) {
		this.carbohydrateLessThanOrEqual = carbohydrateLessThanOrEqual;
	}

	public void setVitaminAGreaterThanOrEqual(Double vitaminAGreaterThanOrEqual) {
		this.vitaminAGreaterThanOrEqual = vitaminAGreaterThanOrEqual;
	}

	public void setVitaminALessThanOrEqual(Double vitaminALessThanOrEqual) {
		this.vitaminALessThanOrEqual = vitaminALessThanOrEqual;
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

	public void setVitaminB12GreaterThanOrEqual(Double vitaminB12GreaterThanOrEqual) {
		this.vitaminB12GreaterThanOrEqual = vitaminB12GreaterThanOrEqual;
	}

	public void setVitaminB12LessThanOrEqual(Double vitaminB12LessThanOrEqual) {
		this.vitaminB12LessThanOrEqual = vitaminB12LessThanOrEqual;
	}

	public void setVitaminCGreaterThanOrEqual(Double vitaminCGreaterThanOrEqual) {
		this.vitaminCGreaterThanOrEqual = vitaminCGreaterThanOrEqual;
	}

	public void setVitaminCLessThanOrEqual(Double vitaminCLessThanOrEqual) {
		this.vitaminCLessThanOrEqual = vitaminCLessThanOrEqual;
	}

	public void setCaroteneGreaterThanOrEqual(Double caroteneGreaterThanOrEqual) {
		this.caroteneGreaterThanOrEqual = caroteneGreaterThanOrEqual;
	}

	public void setCaroteneLessThanOrEqual(Double caroteneLessThanOrEqual) {
		this.caroteneLessThanOrEqual = caroteneLessThanOrEqual;
	}

	public void setRetinolGreaterThanOrEqual(Double retinolGreaterThanOrEqual) {
		this.retinolGreaterThanOrEqual = retinolGreaterThanOrEqual;
	}

	public void setRetinolLessThanOrEqual(Double retinolLessThanOrEqual) {
		this.retinolLessThanOrEqual = retinolLessThanOrEqual;
	}

	public void setNicotinicCidGreaterThanOrEqual(Double nicotinicCidGreaterThanOrEqual) {
		this.nicotinicCidGreaterThanOrEqual = nicotinicCidGreaterThanOrEqual;
	}

	public void setNicotinicCidLessThanOrEqual(Double nicotinicCidLessThanOrEqual) {
		this.nicotinicCidLessThanOrEqual = nicotinicCidLessThanOrEqual;
	}

	public void setCalciumGreaterThanOrEqual(Double calciumGreaterThanOrEqual) {
		this.calciumGreaterThanOrEqual = calciumGreaterThanOrEqual;
	}

	public void setCalciumLessThanOrEqual(Double calciumLessThanOrEqual) {
		this.calciumLessThanOrEqual = calciumLessThanOrEqual;
	}

	public void setIronGreaterThanOrEqual(Double ironGreaterThanOrEqual) {
		this.ironGreaterThanOrEqual = ironGreaterThanOrEqual;
	}

	public void setIronLessThanOrEqual(Double ironLessThanOrEqual) {
		this.ironLessThanOrEqual = ironLessThanOrEqual;
	}

	public void setZincGreaterThanOrEqual(Double zincGreaterThanOrEqual) {
		this.zincGreaterThanOrEqual = zincGreaterThanOrEqual;
	}

	public void setZincLessThanOrEqual(Double zincLessThanOrEqual) {
		this.zincLessThanOrEqual = zincLessThanOrEqual;
	}

	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setUpdateTimeGreaterThanOrEqual(Date updateTimeGreaterThanOrEqual) {
		this.updateTimeGreaterThanOrEqual = updateTimeGreaterThanOrEqual;
	}

	public void setUpdateTimeLessThanOrEqual(Date updateTimeLessThanOrEqual) {
		this.updateTimeLessThanOrEqual = updateTimeLessThanOrEqual;
	}

	public FoodDRIQuery name(String name) {
		if (name == null) {
			throw new RuntimeException("name is null");
		}
		this.name = name;
		return this;
	}

	public FoodDRIQuery nameLike(String nameLike) {
		if (nameLike == null) {
			throw new RuntimeException("name is null");
		}
		this.nameLike = nameLike;
		return this;
	}

	public FoodDRIQuery descriptionLike(String descriptionLike) {
		if (descriptionLike == null) {
			throw new RuntimeException("description is null");
		}
		this.descriptionLike = descriptionLike;
		return this;
	}

	public FoodDRIQuery age(Integer age) {
		if (age == null) {
			throw new RuntimeException("age is null");
		}
		this.age = age;
		return this;
	}

	public FoodDRIQuery ageGreaterThanOrEqual(Integer ageGreaterThanOrEqual) {
		if (ageGreaterThanOrEqual == null) {
			throw new RuntimeException("age is null");
		}
		this.ageGreaterThanOrEqual = ageGreaterThanOrEqual;
		return this;
	}

	public FoodDRIQuery ageLessThanOrEqual(Integer ageLessThanOrEqual) {
		if (ageLessThanOrEqual == null) {
			throw new RuntimeException("age is null");
		}
		this.ageLessThanOrEqual = ageLessThanOrEqual;
		return this;
	}

	public FoodDRIQuery type(String type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		this.type = type;
		return this;
	}

	public FoodDRIQuery typeLike(String typeLike) {
		if (typeLike == null) {
			throw new RuntimeException("type is null");
		}
		this.typeLike = typeLike;
		return this;
	}

	public FoodDRIQuery typeId(Long typeId) {
		if (typeId == null) {
			throw new RuntimeException("typeId is null");
		}
		this.typeId = typeId;
		return this;
	}

	public FoodDRIQuery typeIds(List<Long> typeIds) {
		if (typeIds == null) {
			throw new RuntimeException("typeIds is empty ");
		}
		this.typeIds = typeIds;
		return this;
	}

	public FoodDRIQuery heatEnergyGreaterThanOrEqual(Double heatEnergyGreaterThanOrEqual) {
		if (heatEnergyGreaterThanOrEqual == null) {
			throw new RuntimeException("heatEnergy is null");
		}
		this.heatEnergyGreaterThanOrEqual = heatEnergyGreaterThanOrEqual;
		return this;
	}

	public FoodDRIQuery heatEnergyLessThanOrEqual(Double heatEnergyLessThanOrEqual) {
		if (heatEnergyLessThanOrEqual == null) {
			throw new RuntimeException("heatEnergy is null");
		}
		this.heatEnergyLessThanOrEqual = heatEnergyLessThanOrEqual;
		return this;
	}

	public FoodDRIQuery proteinGreaterThanOrEqual(Double proteinGreaterThanOrEqual) {
		if (proteinGreaterThanOrEqual == null) {
			throw new RuntimeException("protein is null");
		}
		this.proteinGreaterThanOrEqual = proteinGreaterThanOrEqual;
		return this;
	}

	public FoodDRIQuery proteinLessThanOrEqual(Double proteinLessThanOrEqual) {
		if (proteinLessThanOrEqual == null) {
			throw new RuntimeException("protein is null");
		}
		this.proteinLessThanOrEqual = proteinLessThanOrEqual;
		return this;
	}

	public FoodDRIQuery fatGreaterThanOrEqual(Double fatGreaterThanOrEqual) {
		if (fatGreaterThanOrEqual == null) {
			throw new RuntimeException("fat is null");
		}
		this.fatGreaterThanOrEqual = fatGreaterThanOrEqual;
		return this;
	}

	public FoodDRIQuery fatLessThanOrEqual(Double fatLessThanOrEqual) {
		if (fatLessThanOrEqual == null) {
			throw new RuntimeException("fat is null");
		}
		this.fatLessThanOrEqual = fatLessThanOrEqual;
		return this;
	}

	public FoodDRIQuery carbohydrateGreaterThanOrEqual(Double carbohydrateGreaterThanOrEqual) {
		if (carbohydrateGreaterThanOrEqual == null) {
			throw new RuntimeException("carbohydrate is null");
		}
		this.carbohydrateGreaterThanOrEqual = carbohydrateGreaterThanOrEqual;
		return this;
	}

	public FoodDRIQuery carbohydrateLessThanOrEqual(Double carbohydrateLessThanOrEqual) {
		if (carbohydrateLessThanOrEqual == null) {
			throw new RuntimeException("carbohydrate is null");
		}
		this.carbohydrateLessThanOrEqual = carbohydrateLessThanOrEqual;
		return this;
	}

	public FoodDRIQuery vitaminAGreaterThanOrEqual(Double vitaminAGreaterThanOrEqual) {
		if (vitaminAGreaterThanOrEqual == null) {
			throw new RuntimeException("vitaminA is null");
		}
		this.vitaminAGreaterThanOrEqual = vitaminAGreaterThanOrEqual;
		return this;
	}

	public FoodDRIQuery vitaminALessThanOrEqual(Double vitaminALessThanOrEqual) {
		if (vitaminALessThanOrEqual == null) {
			throw new RuntimeException("vitaminA is null");
		}
		this.vitaminALessThanOrEqual = vitaminALessThanOrEqual;
		return this;
	}

	public FoodDRIQuery vitaminB1GreaterThanOrEqual(Double vitaminB1GreaterThanOrEqual) {
		if (vitaminB1GreaterThanOrEqual == null) {
			throw new RuntimeException("vitaminB1 is null");
		}
		this.vitaminB1GreaterThanOrEqual = vitaminB1GreaterThanOrEqual;
		return this;
	}

	public FoodDRIQuery vitaminB1LessThanOrEqual(Double vitaminB1LessThanOrEqual) {
		if (vitaminB1LessThanOrEqual == null) {
			throw new RuntimeException("vitaminB1 is null");
		}
		this.vitaminB1LessThanOrEqual = vitaminB1LessThanOrEqual;
		return this;
	}

	public FoodDRIQuery vitaminB2GreaterThanOrEqual(Double vitaminB2GreaterThanOrEqual) {
		if (vitaminB2GreaterThanOrEqual == null) {
			throw new RuntimeException("vitaminB2 is null");
		}
		this.vitaminB2GreaterThanOrEqual = vitaminB2GreaterThanOrEqual;
		return this;
	}

	public FoodDRIQuery vitaminB2LessThanOrEqual(Double vitaminB2LessThanOrEqual) {
		if (vitaminB2LessThanOrEqual == null) {
			throw new RuntimeException("vitaminB2 is null");
		}
		this.vitaminB2LessThanOrEqual = vitaminB2LessThanOrEqual;
		return this;
	}

	public FoodDRIQuery vitaminB6GreaterThanOrEqual(Double vitaminB6GreaterThanOrEqual) {
		if (vitaminB6GreaterThanOrEqual == null) {
			throw new RuntimeException("vitaminB6 is null");
		}
		this.vitaminB6GreaterThanOrEqual = vitaminB6GreaterThanOrEqual;
		return this;
	}

	public FoodDRIQuery vitaminB6LessThanOrEqual(Double vitaminB6LessThanOrEqual) {
		if (vitaminB6LessThanOrEqual == null) {
			throw new RuntimeException("vitaminB6 is null");
		}
		this.vitaminB6LessThanOrEqual = vitaminB6LessThanOrEqual;
		return this;
	}

	public FoodDRIQuery vitaminB12GreaterThanOrEqual(Double vitaminB12GreaterThanOrEqual) {
		if (vitaminB12GreaterThanOrEqual == null) {
			throw new RuntimeException("vitaminB12 is null");
		}
		this.vitaminB12GreaterThanOrEqual = vitaminB12GreaterThanOrEqual;
		return this;
	}

	public FoodDRIQuery vitaminB12LessThanOrEqual(Double vitaminB12LessThanOrEqual) {
		if (vitaminB12LessThanOrEqual == null) {
			throw new RuntimeException("vitaminB12 is null");
		}
		this.vitaminB12LessThanOrEqual = vitaminB12LessThanOrEqual;
		return this;
	}

	public FoodDRIQuery vitaminCGreaterThanOrEqual(Double vitaminCGreaterThanOrEqual) {
		if (vitaminCGreaterThanOrEqual == null) {
			throw new RuntimeException("vitaminC is null");
		}
		this.vitaminCGreaterThanOrEqual = vitaminCGreaterThanOrEqual;
		return this;
	}

	public FoodDRIQuery vitaminCLessThanOrEqual(Double vitaminCLessThanOrEqual) {
		if (vitaminCLessThanOrEqual == null) {
			throw new RuntimeException("vitaminC is null");
		}
		this.vitaminCLessThanOrEqual = vitaminCLessThanOrEqual;
		return this;
	}

	public FoodDRIQuery caroteneGreaterThanOrEqual(Double caroteneGreaterThanOrEqual) {
		if (caroteneGreaterThanOrEqual == null) {
			throw new RuntimeException("carotene is null");
		}
		this.caroteneGreaterThanOrEqual = caroteneGreaterThanOrEqual;
		return this;
	}

	public FoodDRIQuery caroteneLessThanOrEqual(Double caroteneLessThanOrEqual) {
		if (caroteneLessThanOrEqual == null) {
			throw new RuntimeException("carotene is null");
		}
		this.caroteneLessThanOrEqual = caroteneLessThanOrEqual;
		return this;
	}

	public FoodDRIQuery retinolGreaterThanOrEqual(Double retinolGreaterThanOrEqual) {
		if (retinolGreaterThanOrEqual == null) {
			throw new RuntimeException("retinol is null");
		}
		this.retinolGreaterThanOrEqual = retinolGreaterThanOrEqual;
		return this;
	}

	public FoodDRIQuery retinolLessThanOrEqual(Double retinolLessThanOrEqual) {
		if (retinolLessThanOrEqual == null) {
			throw new RuntimeException("retinol is null");
		}
		this.retinolLessThanOrEqual = retinolLessThanOrEqual;
		return this;
	}

	public FoodDRIQuery nicotinicCidGreaterThanOrEqual(Double nicotinicCidGreaterThanOrEqual) {
		if (nicotinicCidGreaterThanOrEqual == null) {
			throw new RuntimeException("nicotinicCid is null");
		}
		this.nicotinicCidGreaterThanOrEqual = nicotinicCidGreaterThanOrEqual;
		return this;
	}

	public FoodDRIQuery nicotinicCidLessThanOrEqual(Double nicotinicCidLessThanOrEqual) {
		if (nicotinicCidLessThanOrEqual == null) {
			throw new RuntimeException("nicotinicCid is null");
		}
		this.nicotinicCidLessThanOrEqual = nicotinicCidLessThanOrEqual;
		return this;
	}

	public FoodDRIQuery calciumGreaterThanOrEqual(Double calciumGreaterThanOrEqual) {
		if (calciumGreaterThanOrEqual == null) {
			throw new RuntimeException("calcium is null");
		}
		this.calciumGreaterThanOrEqual = calciumGreaterThanOrEqual;
		return this;
	}

	public FoodDRIQuery calciumLessThanOrEqual(Double calciumLessThanOrEqual) {
		if (calciumLessThanOrEqual == null) {
			throw new RuntimeException("calcium is null");
		}
		this.calciumLessThanOrEqual = calciumLessThanOrEqual;
		return this;
	}

	public FoodDRIQuery ironGreaterThanOrEqual(Double ironGreaterThanOrEqual) {
		if (ironGreaterThanOrEqual == null) {
			throw new RuntimeException("iron is null");
		}
		this.ironGreaterThanOrEqual = ironGreaterThanOrEqual;
		return this;
	}

	public FoodDRIQuery ironLessThanOrEqual(Double ironLessThanOrEqual) {
		if (ironLessThanOrEqual == null) {
			throw new RuntimeException("iron is null");
		}
		this.ironLessThanOrEqual = ironLessThanOrEqual;
		return this;
	}

	public FoodDRIQuery zincGreaterThanOrEqual(Double zincGreaterThanOrEqual) {
		if (zincGreaterThanOrEqual == null) {
			throw new RuntimeException("zinc is null");
		}
		this.zincGreaterThanOrEqual = zincGreaterThanOrEqual;
		return this;
	}

	public FoodDRIQuery zincLessThanOrEqual(Double zincLessThanOrEqual) {
		if (zincLessThanOrEqual == null) {
			throw new RuntimeException("zinc is null");
		}
		this.zincLessThanOrEqual = zincLessThanOrEqual;
		return this;
	}

	public FoodDRIQuery enableFlag(String enableFlag) {
		if (enableFlag == null) {
			throw new RuntimeException("enableFlag is null");
		}
		this.enableFlag = enableFlag;
		return this;
	}

	public FoodDRIQuery createBy(String createBy) {
		if (createBy == null) {
			throw new RuntimeException("createBy is null");
		}
		this.createBy = createBy;
		return this;
	}

	public FoodDRIQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public FoodDRIQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public FoodDRIQuery updateTimeGreaterThanOrEqual(Date updateTimeGreaterThanOrEqual) {
		if (updateTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("updateTime is null");
		}
		this.updateTimeGreaterThanOrEqual = updateTimeGreaterThanOrEqual;
		return this;
	}

	public FoodDRIQuery updateTimeLessThanOrEqual(Date updateTimeLessThanOrEqual) {
		if (updateTimeLessThanOrEqual == null) {
			throw new RuntimeException("updateTime is null");
		}
		this.updateTimeLessThanOrEqual = updateTimeLessThanOrEqual;
		return this;
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

			if ("age".equals(sortColumn)) {
				orderBy = "E.AGE_" + a_x;
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

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("name", "NAME_");
		addColumn("description", "DESCRIPTION_");
		addColumn("age", "AGE_");
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
		addColumn("sortNo", "SORTNO_");
		addColumn("enableFlag", "ENABLEFLAG_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
		addColumn("updateBy", "UPDATEBY_");
		addColumn("updateTime", "UPDATETIME_");
	}

}