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

package com.glaf.heathcare.domain;

import java.util.*;

public class DietaryRptModel implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	protected String name;

	protected Dietary dietary;

	protected DietaryTemplate dietaryTemplate;

	protected double heatEnergy;// 热能实际值

	protected double heatEnergyStandard;// 热能标准值

	protected double heatEnergyPercent;// 热能百分比

	protected String heatEnergyEvaluate;// 热能评价

	protected double protein;// 蛋白质实际值

	protected double proteinStandard;// 蛋白质标准值

	protected double proteinPercent;// 蛋白质百分比

	protected String proteinEvaluate;// 蛋白质评价

	protected double carbohydrate;// 碳水化合物实际值

	protected double carbohydrateStandard;// 碳水化合物标准值

	protected double carbohydratePercent;// 碳水化合物百分比

	protected String carbohydrateEvaluate;// 碳水化合物评价

	protected double fat;// 脂肪实际值

	protected double fatStandard;// 脂肪标准值

	protected double fatPercent;// 脂肪百分比

	protected String fatEvaluate;// 脂肪评价

	protected double calcium;// 钙实际值

	protected double calciumStandard;// 钙标准值

	protected double calciumPercent;// 钙百分比

	protected String calciumEvaluate;// 钙评价

	protected List<DietaryItem> items = new ArrayList<DietaryItem>();

	public DietaryRptModel() {

	}

	public double getCalcium() {
		return calcium;
	}

	public String getCalciumEvaluate() {
		return calciumEvaluate;
	}

	public double getCalciumPercent() {
		return calciumPercent;
	}

	public double getCalciumStandard() {
		return calciumStandard;
	}

	public double getCarbohydrate() {
		return carbohydrate;
	}

	public String getCarbohydrateEvaluate() {
		return carbohydrateEvaluate;
	}

	public double getCarbohydratePercent() {
		return carbohydratePercent;
	}

	public double getCarbohydrateStandard() {
		return carbohydrateStandard;
	}

	public Dietary getDietary() {
		return dietary;
	}

	public DietaryTemplate getDietaryTemplate() {
		return dietaryTemplate;
	}

	public double getFat() {
		return fat;
	}

	public String getFatEvaluate() {
		return fatEvaluate;
	}

	public double getFatPercent() {
		return fatPercent;
	}

	public double getFatStandard() {
		return fatStandard;
	}

	public double getHeatEnergy() {
		return heatEnergy;
	}

	public String getHeatEnergyEvaluate() {
		return heatEnergyEvaluate;
	}

	public double getHeatEnergyPercent() {
		return heatEnergyPercent;
	}

	public double getHeatEnergyStandard() {
		return heatEnergyStandard;
	}

	public List<DietaryItem> getItems() {
		if (items != null && !items.isEmpty()) {
			Collections.sort(items);
		}
		return items;
	}

	public String getName() {
		return name;
	}

	public double getProtein() {
		return protein;
	}

	public String getProteinEvaluate() {
		return proteinEvaluate;
	}

	public double getProteinPercent() {
		return proteinPercent;
	}

	public double getProteinStandard() {
		return proteinStandard;
	}

	public void setCalcium(double calcium) {
		this.calcium = calcium;
	}

	public void setCalciumEvaluate(String calciumEvaluate) {
		this.calciumEvaluate = calciumEvaluate;
	}

	public void setCalciumPercent(double calciumPercent) {
		this.calciumPercent = calciumPercent;
	}

	public void setCalciumStandard(double calciumStandard) {
		this.calciumStandard = calciumStandard;
	}

	public void setCarbohydrate(double carbohydrate) {
		this.carbohydrate = carbohydrate;
	}

	public void setCarbohydrateEvaluate(String carbohydrateEvaluate) {
		this.carbohydrateEvaluate = carbohydrateEvaluate;
	}

	public void setCarbohydratePercent(double carbohydratePercent) {
		this.carbohydratePercent = carbohydratePercent;
	}

	public void setCarbohydrateStandard(double carbohydrateStandard) {
		this.carbohydrateStandard = carbohydrateStandard;
	}

	public void setDietary(Dietary dietary) {
		this.dietary = dietary;
	}

	public void setDietaryTemplate(DietaryTemplate dietaryTemplate) {
		this.dietaryTemplate = dietaryTemplate;
	}

	public void setFat(double fat) {
		this.fat = fat;
	}

	public void setFatEvaluate(String fatEvaluate) {
		this.fatEvaluate = fatEvaluate;
	}

	public void setFatPercent(double fatPercent) {
		this.fatPercent = fatPercent;
	}

	public void setFatStandard(double fatStandard) {
		this.fatStandard = fatStandard;
	}

	public void setHeatEnergy(double heatEnergy) {
		this.heatEnergy = heatEnergy;
	}

	public void setHeatEnergyEvaluate(String heatEnergyEvaluate) {
		this.heatEnergyEvaluate = heatEnergyEvaluate;
	}

	public void setHeatEnergyPercent(double heatEnergyPercent) {
		this.heatEnergyPercent = heatEnergyPercent;
	}

	public void setHeatEnergyStandard(double heatEnergyStandard) {
		this.heatEnergyStandard = heatEnergyStandard;
	}

	public void setItems(List<DietaryItem> items) {
		this.items = items;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setProtein(double protein) {
		this.protein = protein;
	}

	public void setProteinEvaluate(String proteinEvaluate) {
		this.proteinEvaluate = proteinEvaluate;
	}

	public void setProteinPercent(double proteinPercent) {
		this.proteinPercent = proteinPercent;
	}

	public void setProteinStandard(double proteinStandard) {
		this.proteinStandard = proteinStandard;
	}

}
