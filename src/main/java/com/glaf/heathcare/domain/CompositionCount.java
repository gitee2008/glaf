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

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * 食物成分汇总
 *
 */

public class CompositionCount implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 食物分类编号
	 */
	protected long nodeId;

	/**
	 * 热能
	 */
	protected double heatEnergy;

	/**
	 * 碳水热
	 */
	protected double heatEnergyCarbohydrate;

	/**
	 * 脂肪热
	 */
	protected double heatEnergyFat;

	/**
	 * 蛋白热
	 */
	protected double heatEnergyProtein;

	/**
	 * 蛋白质
	 */
	protected double protein;

	/**
	 * 动物类蛋白质
	 */
	protected double proteinAnimal;

	/**
	 * 动豆类蛋白质
	 */
	protected double proteinAnimalBeans;

	/**
	 * 脂肪
	 */
	protected double fat;

	/**
	 * 碳水化合物
	 */
	protected double carbohydrate;

	/**
	 * 微生素A
	 */
	protected double vitaminA;

	/**
	 * 微生素B1
	 */
	protected double vitaminB1;

	/**
	 * 微生素B2
	 */
	protected double vitaminB2;

	/**
	 * 微生素B6
	 */
	protected double vitaminB6;

	/**
	 * 微生素B12
	 */
	protected double vitaminB12;

	/**
	 * 微生素C
	 */
	protected double vitaminC;

	/**
	 * 微生素E
	 */
	protected double vitaminE;

	/**
	 * 胡萝卜素
	 */
	protected double carotene;

	/**
	 * 视黄醇
	 */
	protected double retinol;

	/**
	 * 尼克酸
	 */
	protected double nicotinicCid;

	/**
	 * 钙
	 */
	protected double calcium;

	/**
	 * 铁
	 */
	protected double iron;

	/**
	 * 锌
	 */
	protected double zinc;

	/**
	 * 碘
	 */
	protected double iodine;

	/**
	 * 磷
	 */
	protected double phosphorus;

	/**
	 * 数量
	 */
	protected double quantity;

	public CompositionCount() {

	}

	public double getCalcium() {
		return this.calcium;
	}

	public double getCarbohydrate() {
		return this.carbohydrate;
	}

	public double getCarotene() {
		return this.carotene;
	}

	public double getFat() {
		return this.fat;
	}

	public double getHeatEnergy() {
		return this.heatEnergy;
	}

	public double getHeatEnergyCarbohydrate() {
		return heatEnergyCarbohydrate;
	}

	public double getHeatEnergyFat() {
		return heatEnergyFat;
	}

	public double getHeatEnergyProtein() {
		return heatEnergyProtein;
	}

	public double getIodine() {
		return iodine;
	}

	public double getIron() {
		return this.iron;
	}

	public double getNicotinicCid() {
		return this.nicotinicCid;
	}

	public long getNodeId() {
		return nodeId;
	}

	public double getPhosphorus() {
		return phosphorus;
	}

	public double getProtein() {
		return this.protein;
	}

	public double getProteinAnimal() {
		return proteinAnimal;
	}

	public double getProteinAnimalBeans() {
		return proteinAnimalBeans;
	}

	public double getQuantity() {
		return quantity;
	}

	public double getRetinol() {
		return this.retinol;
	}

	public double getVitaminA() {
		return this.vitaminA;
	}

	public double getVitaminB1() {
		return this.vitaminB1;
	}

	public double getVitaminB12() {
		return this.vitaminB12;
	}

	public double getVitaminB2() {
		return this.vitaminB2;
	}

	public double getVitaminB6() {
		return this.vitaminB6;
	}

	public double getVitaminC() {
		return this.vitaminC;
	}

	public double getVitaminE() {
		return vitaminE;
	}

	public double getZinc() {
		return this.zinc;
	}

	public void setCalcium(double calcium) {
		this.calcium = calcium;
	}

	public void setCarbohydrate(double carbohydrate) {
		this.carbohydrate = carbohydrate;
	}

	public void setCarotene(double carotene) {
		this.carotene = carotene;
	}

	public void setFat(double fat) {
		this.fat = fat;
	}

	public void setHeatEnergy(double heatEnergy) {
		this.heatEnergy = heatEnergy;
	}

	public void setHeatEnergyCarbohydrate(double heatEnergyCarbohydrate) {
		this.heatEnergyCarbohydrate = heatEnergyCarbohydrate;
	}

	public void setHeatEnergyFat(double heatEnergyFat) {
		this.heatEnergyFat = heatEnergyFat;
	}

	public void setHeatEnergyProtein(double heatEnergyProtein) {
		this.heatEnergyProtein = heatEnergyProtein;
	}

	public void setIodine(double iodine) {
		this.iodine = iodine;
	}

	public void setIron(double iron) {
		this.iron = iron;
	}

	public void setNicotinicCid(double nicotinicCid) {
		this.nicotinicCid = nicotinicCid;
	}

	public void setNodeId(long nodeId) {
		this.nodeId = nodeId;
	}

	public void setPhosphorus(double phosphorus) {
		this.phosphorus = phosphorus;
	}

	public void setProtein(double protein) {
		this.protein = protein;
	}

	public void setProteinAnimal(double proteinAnimal) {
		this.proteinAnimal = proteinAnimal;
	}

	public void setProteinAnimalBeans(double proteinAnimalBeans) {
		this.proteinAnimalBeans = proteinAnimalBeans;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public void setRetinol(double retinol) {
		this.retinol = retinol;
	}

	public void setVitaminA(double vitaminA) {
		this.vitaminA = vitaminA;
	}

	public void setVitaminB1(double vitaminB1) {
		this.vitaminB1 = vitaminB1;
	}

	public void setVitaminB12(double vitaminB12) {
		this.vitaminB12 = vitaminB12;
	}

	public void setVitaminB2(double vitaminB2) {
		this.vitaminB2 = vitaminB2;
	}

	public void setVitaminB6(double vitaminB6) {
		this.vitaminB6 = vitaminB6;
	}

	public void setVitaminC(double vitaminC) {
		this.vitaminC = vitaminC;
	}

	public void setVitaminE(double vitaminE) {
		this.vitaminE = vitaminE;
	}

	public void setZinc(double zinc) {
		this.zinc = zinc;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
