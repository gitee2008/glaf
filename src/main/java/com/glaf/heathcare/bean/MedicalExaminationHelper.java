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

package com.glaf.heathcare.bean;

import java.util.Map;

import com.glaf.heathcare.domain.GrowthStandard;
import com.glaf.heathcare.domain.MedicalExamination;

public class MedicalExaminationHelper {

	/**
	 * 均值离差法
	 * 
	 * @param gsMap
	 * @param medicalExamination
	 */
	public void evaluate(Map<String, GrowthStandard> gsMap, MedicalExamination medicalExamination) {
		if (medicalExamination.getHeight() > 0 && medicalExamination.getWeight() > 0) {
			double BMI = medicalExamination.getWeight()
					/ (medicalExamination.getHeight() * medicalExamination.getHeight() / 10000D);
			medicalExamination.setBmi(BMI);
			GrowthStandard gs = gsMap
					.get(medicalExamination.getAgeOfTheMoon() + "_" + medicalExamination.getSex() + "_5");// BMI
			if (gs != null) {
				if (BMI > gs.getThreeDSDeviation()) {
					medicalExamination.setBmiIndex(3);
					medicalExamination.setBmiEvaluate("严重肥胖");
				} else if (BMI > gs.getTwoDSDeviation() && BMI <= gs.getThreeDSDeviation()) {
					medicalExamination.setBmiIndex(2);
					medicalExamination.setBmiEvaluate("肥胖");
				} else if (BMI > gs.getOneDSDeviation() && BMI <= gs.getTwoDSDeviation()) {
					medicalExamination.setBmiIndex(1);
					medicalExamination.setBmiEvaluate("超重");
				} else if (BMI < gs.getNegativeThreeDSDeviation()) {
					medicalExamination.setBmiIndex(-3);
					medicalExamination.setBmiEvaluate("消瘦");
				} else if (BMI < gs.getNegativeTwoDSDeviation() && BMI >= gs.getNegativeThreeDSDeviation()) {
					medicalExamination.setBmiIndex(-2);
					medicalExamination.setBmiEvaluate("较瘦");
				} else if (BMI < gs.getNegativeOneDSDeviation() && BMI >= gs.getNegativeTwoDSDeviation()) {
					medicalExamination.setBmiIndex(-1);
					medicalExamination.setBmiEvaluate("偏瘦");
				} else {
					medicalExamination.setBmiIndex(0);
					medicalExamination.setBmiEvaluate("正常");
				}
			} else {
				throw new RuntimeException("growth standard not config");
			}
		}

		if (medicalExamination.getHeight() > 0) {
			double index = medicalExamination.getHeight();
			GrowthStandard gs = gsMap
					.get(medicalExamination.getAgeOfTheMoon() + "_" + medicalExamination.getSex() + "_2");// H/A年龄别身高
			if (gs != null) {
				if (index > gs.getThreeDSDeviation()) {
					medicalExamination.setHeightLevel(3);
					medicalExamination.setHeightEvaluate("上");
				} else if (index > gs.getTwoDSDeviation() && index <= gs.getThreeDSDeviation()) {
					medicalExamination.setHeightLevel(2);
					medicalExamination.setHeightEvaluate("中上");
				} else if (index > gs.getOneDSDeviation() && index <= gs.getTwoDSDeviation()) {
					medicalExamination.setHeightLevel(1);
					medicalExamination.setHeightEvaluate("中+");
				} else if (index < gs.getNegativeThreeDSDeviation()) {
					medicalExamination.setHeightLevel(-3);
					medicalExamination.setHeightEvaluate("下");
				} else if (index < gs.getNegativeTwoDSDeviation() && index >= gs.getNegativeThreeDSDeviation()) {
					medicalExamination.setHeightLevel(-2);
					medicalExamination.setHeightEvaluate("中下");
				} else if (index < gs.getNegativeOneDSDeviation() && index >= gs.getNegativeTwoDSDeviation()) {
					medicalExamination.setHeightLevel(-1);
					medicalExamination.setHeightEvaluate("中-");
				} else {
					medicalExamination.setHeightLevel(0);
					medicalExamination.setHeightEvaluate("正常");
				}
			}
		}

		if (medicalExamination.getWeight() > 0) {
			double index = medicalExamination.getWeight();
			GrowthStandard gs = gsMap
					.get(medicalExamination.getAgeOfTheMoon() + "_" + medicalExamination.getSex() + "_3");// W/A年龄别体重
			if (gs != null) {
				if (index > gs.getThreeDSDeviation()) {
					medicalExamination.setWeightLevel(3);
					medicalExamination.setWeightEvaluate("严重肥胖");
				} else if (index > gs.getTwoDSDeviation() && index <= gs.getThreeDSDeviation()) {
					medicalExamination.setWeightLevel(2);
					medicalExamination.setWeightEvaluate("肥胖");
				} else if (index > gs.getOneDSDeviation() && index <= gs.getTwoDSDeviation()) {
					medicalExamination.setWeightLevel(1);
					medicalExamination.setWeightEvaluate("超重");
				} else if (index < gs.getNegativeThreeDSDeviation()) {
					medicalExamination.setWeightLevel(-3);
					medicalExamination.setWeightEvaluate("消瘦");
				} else if (index < gs.getNegativeTwoDSDeviation() && index >= gs.getNegativeThreeDSDeviation()) {
					medicalExamination.setWeightLevel(-2);
					medicalExamination.setWeightEvaluate("较瘦");
				} else if (index < gs.getNegativeOneDSDeviation() && index >= gs.getNegativeTwoDSDeviation()) {
					medicalExamination.setWeightLevel(-1);
					medicalExamination.setWeightEvaluate("偏瘦");
				} else {
					medicalExamination.setWeightLevel(0);
					medicalExamination.setWeightEvaluate("正常");
				}
			}
		}
	}

	/**
	 * 百分位数评价法
	 * 
	 * @param gsMap
	 * @param medicalExamination
	 */
	public void evaluateByPrctile(Map<String, GrowthStandard> gsMap, MedicalExamination medicalExamination) {
		if (medicalExamination.getHeight() > 0 && medicalExamination.getWeight() > 0) {
			double BMI = medicalExamination.getWeight()
					/ (medicalExamination.getHeight() * medicalExamination.getHeight() / 10000D);
			medicalExamination.setBmi(BMI);
			GrowthStandard gs = gsMap
					.get(medicalExamination.getAgeOfTheMoon() + "_" + medicalExamination.getSex() + "_4");// W/H身高别体重
			if (gs != null) {
				if (BMI > gs.getPercent97()) {
					medicalExamination.setBmiIndex(3);
					medicalExamination.setBmiEvaluate("肥胖");
				} else if (BMI > gs.getPercent85() && BMI <= gs.getPercent97()) {
					medicalExamination.setBmiIndex(2);
					medicalExamination.setBmiEvaluate("超重");
				} else if (BMI < gs.getPercent3()) {
					medicalExamination.setBmiIndex(-3);
					medicalExamination.setBmiEvaluate("消瘦");
				} else if (BMI < gs.getPercent15() && BMI >= gs.getPercent3()) {
					medicalExamination.setBmiIndex(-2);
					medicalExamination.setBmiEvaluate("偏瘦");
				} else {
					medicalExamination.setBmiIndex(0);
					medicalExamination.setBmiEvaluate("正常");
				}
			}
		}

		if (medicalExamination.getHeight() > 0) {
			double index = medicalExamination.getHeight();
			GrowthStandard gs = gsMap
					.get(medicalExamination.getAgeOfTheMoon() + "_" + medicalExamination.getSex() + "_2");// H/A年龄别身高
			if (gs != null) {
				if (index > gs.getPercent97()) {
					medicalExamination.setHeightLevel(3);
					medicalExamination.setHeightEvaluate("上");
				} else if (index > gs.getPercent85() && index <= gs.getPercent97()) {
					medicalExamination.setHeightLevel(2);
					medicalExamination.setHeightEvaluate("中上");
				} else if (index < gs.getPercent3()) {
					medicalExamination.setHeightLevel(-3);
					medicalExamination.setHeightEvaluate("下");
				} else if (index < gs.getPercent15() && index >= gs.getPercent3()) {
					medicalExamination.setHeightLevel(-2);
					medicalExamination.setHeightEvaluate("中下");
				} else {
					medicalExamination.setHeightLevel(0);
					medicalExamination.setHeightEvaluate("正常");
				}
			}
		}

		if (medicalExamination.getWeight() > 0) {
			double index = medicalExamination.getWeight();
			GrowthStandard gs = gsMap
					.get(medicalExamination.getAgeOfTheMoon() + "_" + medicalExamination.getSex() + "_3");// W/A年龄别体重
			if (gs != null) {
				if (index > gs.getPercent97()) {
					medicalExamination.setWeightLevel(3);
					medicalExamination.setWeightEvaluate("肥胖");
				} else if (index > gs.getPercent85() && index <= gs.getPercent97()) {
					medicalExamination.setWeightLevel(2);
					medicalExamination.setWeightEvaluate("超重");
				} else if (index < gs.getPercent3()) {
					medicalExamination.setWeightLevel(-3);
					medicalExamination.setWeightEvaluate("消瘦");
				} else if (index < gs.getPercent15() && index >= gs.getPercent3()) {
					medicalExamination.setWeightLevel(-2);
					medicalExamination.setWeightEvaluate("偏瘦");
				} else {
					medicalExamination.setWeightLevel(0);
					medicalExamination.setWeightEvaluate("正常");
				}
			}
		}
	}
}
