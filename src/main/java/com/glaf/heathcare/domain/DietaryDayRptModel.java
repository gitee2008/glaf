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

import java.util.ArrayList;
import java.util.List;

public class DietaryDayRptModel implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	protected String weekName;

	protected String dateName;

	protected int year;

	protected int week;

	protected int fullDay;

	protected int breakfastSize;

	protected int breakfastMidSize;

	protected int lunchSize;

	protected int snackSize;

	protected int dinnerSize;

	protected double heatEnergy;// 热能实际值

	protected double heatEnergyStandard;// 热能标准值

	protected double heatEnergyPercent;// 热能百分比

	protected String heatEnergyEvaluate;// 热能评价

	protected double protein;// 蛋白质实际值

	protected double proteinStandard;// 蛋白质标准值

	protected double proteinPercent;// 蛋白质百分比

	protected String proteinEvaluate;// 蛋白质评价

	protected double calcium;// 钙实际值

	protected double calciumStandard;// 钙标准值

	protected double calciumPercent;// 钙百分比

	protected String calciumEvaluate;// 钙评价

	protected double fat;// 脂肪实际值

	protected double fatStandard;// 脂肪标准值

	protected double fatPercent;// 脂肪百分比

	protected String fatEvaluate;// 脂肪评价

	protected double carbohydrate;// 碳水化合物实际值

	protected double carbohydrateStandard;// 碳水化合物标准值

	protected double carbohydratePercent;// 碳水化合物百分比

	protected String carbohydrateEvaluate;// 碳水化合物评价

	protected List<DietaryRptModel> breakfastList = new ArrayList<DietaryRptModel>();

	protected List<DietaryRptModel> breakfastMidList = new ArrayList<DietaryRptModel>();

	protected List<DietaryRptModel> lunchList = new ArrayList<DietaryRptModel>();

	protected List<DietaryRptModel> snackList = new ArrayList<DietaryRptModel>();

	protected List<DietaryRptModel> dinnerList = new ArrayList<DietaryRptModel>();

	public DietaryDayRptModel() {

	}

	public List<DietaryRptModel> getBreakfastList() {
		return breakfastList;
	}

	public List<DietaryRptModel> getBreakfastMidList() {
		return breakfastMidList;
	}

	public int getBreakfastMidSize() {
		if (breakfastMidList != null && !breakfastMidList.isEmpty()) {
			breakfastMidSize = breakfastMidList.size();
		}
		return breakfastMidSize;
	}

	public int getBreakfastSize() {
		if (breakfastList != null && !breakfastList.isEmpty()) {
			breakfastSize = breakfastList.size();
		}
		return breakfastSize;
	}

	public double getCalcium() {
		if (calcium > 0) {
			calcium = Math.round(calcium);
		}
		return calcium;
	}

	public String getCalciumEvaluate() {
		return calciumEvaluate;
	}

	public double getCalciumPercent() {
		if (calciumStandard > 0) {
			calciumPercent = Math.round((calcium / calciumStandard * 100D));
		}
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

	public String getDateName() {
		return dateName;
	}

	public List<DietaryRptModel> getDinnerList() {
		return dinnerList;
	}

	public int getDinnerSize() {
		if (dinnerList != null && !dinnerList.isEmpty()) {
			dinnerSize = dinnerList.size();
		}
		return dinnerSize;
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

	public int getFullDay() {
		return fullDay;
	}

	public double getHeatEnergy() {
		return heatEnergy;
	}

	public String getHeatEnergyEvaluate() {
		return heatEnergyEvaluate;
	}

	public double getHeatEnergyPercent() {
		if (heatEnergyStandard > 0) {
			heatEnergyPercent = Math.round((heatEnergy / heatEnergyStandard * 100D));
		}
		return heatEnergyPercent;
	}

	public double getHeatEnergyStandard() {
		return heatEnergyStandard;
	}

	public List<DietaryRptModel> getLunchList() {
		return lunchList;
	}

	public int getLunchSize() {
		if (lunchList != null && !lunchList.isEmpty()) {
			lunchSize = lunchList.size();
		}
		return lunchSize;
	}

	public double getProtein() {
		return protein;
	}

	public String getProteinEvaluate() {
		return proteinEvaluate;
	}

	public double getProteinPercent() {
		if (proteinStandard > 0) {
			proteinPercent = Math.round((protein / proteinStandard * 100D));
		}
		return proteinPercent;
	}

	public double getProteinStandard() {
		return proteinStandard;
	}

	public List<DietaryRptModel> getSnackList() {
		return snackList;
	}

	public int getSnackSize() {
		if (snackList != null && !snackList.isEmpty()) {
			snackSize = snackList.size();
		}
		return snackSize;
	}

	public int getWeek() {
		return week;
	}

	public String getWeekName() {
		return weekName;
	}

	public int getYear() {
		return year;
	}

	public void setBreakfastList(List<DietaryRptModel> breakfastList) {
		this.breakfastList = breakfastList;
	}

	public void setBreakfastMidList(List<DietaryRptModel> breakfastMidList) {
		this.breakfastMidList = breakfastMidList;
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

	public void setDateName(String dateName) {
		this.dateName = dateName;
	}

	public void setDinnerList(List<DietaryRptModel> dinnerList) {
		this.dinnerList = dinnerList;
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

	public void setFullDay(int fullDay) {
		this.fullDay = fullDay;
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

	public void setLunchList(List<DietaryRptModel> lunchList) {
		this.lunchList = lunchList;
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

	public void setSnackList(List<DietaryRptModel> snackList) {
		this.snackList = snackList;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public void setWeekName(String weekName) {
		this.weekName = weekName;
	}

	public void setYear(int year) {
		this.year = year;
	}

}
