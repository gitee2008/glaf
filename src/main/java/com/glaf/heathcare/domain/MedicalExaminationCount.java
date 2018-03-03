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
import java.util.Date;

public class MedicalExaminationCount implements Serializable {

	private static final long serialVersionUID = 1L;

	protected String tenantId;

	protected String gradeId;

	protected String gradeName;

	protected Date checkDate;

	protected int totalPerson;

	protected int checkPerson;

	protected double checkPercent;

	/**
	 * 体重正常人数
	 */
	protected int meanWeightNormal;

	protected double meanWeightNormalPercent;

	/**
	 * 身高正常人数
	 */
	protected int meanHeightNormal;

	protected double meanHeightNormalPercent;

	/**
	 * 体重低于2SD人数
	 */
	protected int meanWeightLow;

	protected double meanWeightLowPercent;

	/**
	 * 身高低于2SD人数
	 */
	protected int meanHeightLow;

	protected double meanHeightLowPercent;

	/**
	 * 消瘦人数
	 */
	protected int meanWeightSkinny;

	protected double meanWeightSkinnyPercent;

	/**
	 * 超重人数
	 */
	protected int meanOverWeight;

	protected double meanOverWeightPercent;

	/**
	 * 肥胖人数
	 */
	protected int meanWeightObesity;

	protected double meanWeightObesityPercent;

	/**
	 * W/H正常人数(身高别体重)
	 */
	protected int prctileWeightHeightNormal;

	protected double prctileWeightHeightNormalPercent;

	/**
	 * W/A正常人数(年龄别体重)
	 */
	protected int prctileWeightAgeNormal;

	protected double prctileWeightAgeNormalPercent;

	/**
	 * H/A正常人数(年龄别身高)
	 */
	protected int prctileHeightAgeNormal;

	protected double prctileHeightAgeNormalPercent;

	/**
	 * H/A小于P3人数(年龄别身高)
	 */
	protected int prctileHeightAgeLow;

	protected double prctileHeightAgeLowPercent;

	/**
	 * W/H小于P3人数(身高别体重)
	 */
	protected int prctileWeightHeightLow;

	protected double prctileWeightHeightLowPercent;

	/**
	 * W/A小于P3人数(年龄别体重)
	 */
	protected int prctileWeightAgeLow;

	protected double prctileWeightAgeLowPercent;

	/**
	 * 超重人数
	 */
	protected int prctileOverWeight;

	protected double prctileOverWeightPercent;

	/**
	 * 肥胖人数
	 */
	protected int prctileWeightObesity;

	protected double prctileWeightObesityPercent;

	public MedicalExaminationCount() {

	}

	public Date getCheckDate() {
		return checkDate;
	}

	public double getCheckPercent() {
		if (checkPercent > 0) {
			checkPercent = Math.round(checkPercent * 100D) / 100D;
		}
		return checkPercent;
	}

	public int getCheckPerson() {
		return checkPerson;
	}

	public String getGradeId() {
		return gradeId;
	}

	public String getGradeName() {
		return gradeName;
	}

	public int getMeanHeightLow() {
		return meanHeightLow;
	}

	public double getMeanHeightLowPercent() {
		if (meanHeightLowPercent > 0) {
			meanHeightLowPercent = Math.round(meanHeightLowPercent * 100D) / 100D;
		}
		return meanHeightLowPercent;
	}

	public int getMeanHeightNormal() {
		return meanHeightNormal;
	}

	public double getMeanHeightNormalPercent() {
		if (meanHeightNormalPercent > 0) {
			meanHeightNormalPercent = Math.round(meanHeightNormalPercent * 100D) / 100D;
		}
		return meanHeightNormalPercent;
	}

	public int getMeanOverWeight() {
		return meanOverWeight;
	}

	public double getMeanOverWeightPercent() {
		if (meanOverWeightPercent > 0) {
			meanOverWeightPercent = Math.round(meanOverWeightPercent * 100D) / 100D;
		}
		return meanOverWeightPercent;
	}

	public int getMeanWeightLow() {
		return meanWeightLow;
	}

	public double getMeanWeightLowPercent() {
		if (meanWeightLowPercent > 0) {
			meanWeightLowPercent = Math.round(meanWeightLowPercent * 100D) / 100D;
		}
		return meanWeightLowPercent;
	}

	public int getMeanWeightNormal() {
		return meanWeightNormal;
	}

	public double getMeanWeightNormalPercent() {
		if (meanWeightNormalPercent > 0) {
			meanWeightNormalPercent = Math.round(meanWeightNormalPercent * 100D) / 100D;
		}
		return meanWeightNormalPercent;
	}

	public int getMeanWeightObesity() {
		return meanWeightObesity;
	}

	public double getMeanWeightObesityPercent() {
		if (meanWeightObesityPercent > 0) {
			meanWeightObesityPercent = Math.round(meanWeightObesityPercent * 100D) / 100D;
		}
		return meanWeightObesityPercent;
	}

	public int getMeanWeightSkinny() {
		return meanWeightSkinny;
	}

	public double getMeanWeightSkinnyPercent() {
		if (meanWeightSkinnyPercent > 0) {
			meanWeightSkinnyPercent = Math.round(meanWeightSkinnyPercent * 100D) / 100D;
		}
		return meanWeightSkinnyPercent;
	}

	public int getPrctileHeightAgeLow() {
		return prctileHeightAgeLow;
	}

	public double getPrctileHeightAgeLowPercent() {
		if (prctileHeightAgeLowPercent > 0) {
			prctileHeightAgeLowPercent = Math.round(prctileHeightAgeLowPercent * 100D) / 100D;
		}
		return prctileHeightAgeLowPercent;
	}

	public int getPrctileHeightAgeNormal() {
		return prctileHeightAgeNormal;
	}

	public double getPrctileHeightAgeNormalPercent() {
		if (prctileHeightAgeNormalPercent > 0) {
			prctileHeightAgeNormalPercent = Math.round(prctileHeightAgeNormalPercent * 100D) / 100D;
		}
		return prctileHeightAgeNormalPercent;
	}

	public int getPrctileOverWeight() {
		return prctileOverWeight;
	}

	public double getPrctileOverWeightPercent() {
		if (prctileOverWeightPercent > 0) {
			prctileOverWeightPercent = Math.round(prctileOverWeightPercent * 100D) / 100D;
		}
		return prctileOverWeightPercent;
	}

	public int getPrctileWeightAgeLow() {
		return prctileWeightAgeLow;
	}

	public double getPrctileWeightAgeLowPercent() {
		if (prctileWeightAgeLowPercent > 0) {
			prctileWeightAgeLowPercent = Math.round(prctileWeightAgeLowPercent * 100D) / 100D;
		}
		return prctileWeightAgeLowPercent;
	}

	public int getPrctileWeightAgeNormal() {
		return prctileWeightAgeNormal;
	}

	public double getPrctileWeightAgeNormalPercent() {
		if (prctileWeightAgeNormalPercent > 0) {
			prctileWeightAgeNormalPercent = Math.round(prctileWeightAgeNormalPercent * 100D) / 100D;
		}
		return prctileWeightAgeNormalPercent;
	}

	public int getPrctileWeightHeightLow() {
		return prctileWeightHeightLow;
	}

	public double getPrctileWeightHeightLowPercent() {
		if (prctileWeightHeightLowPercent > 0) {
			prctileWeightHeightLowPercent = Math.round(prctileWeightHeightLowPercent * 100D) / 100D;
		}
		return prctileWeightHeightLowPercent;
	}

	public int getPrctileWeightHeightNormal() {
		return prctileWeightHeightNormal;
	}

	public double getPrctileWeightHeightNormalPercent() {
		if (prctileWeightHeightNormalPercent > 0) {
			prctileWeightHeightNormalPercent = Math.round(prctileWeightHeightNormalPercent * 100D) / 100D;
		}
		return prctileWeightHeightNormalPercent;
	}

	public int getPrctileWeightObesity() {
		return prctileWeightObesity;
	}

	public double getPrctileWeightObesityPercent() {
		if (prctileWeightObesityPercent > 0) {
			prctileWeightObesityPercent = Math.round(prctileWeightObesityPercent * 100D) / 100D;
		}
		return prctileWeightObesityPercent;
	}

	public String getTenantId() {
		return tenantId;
	}

	public int getTotalPerson() {
		return totalPerson;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public void setCheckPercent(double checkPercent) {
		this.checkPercent = checkPercent;
	}

	public void setCheckPerson(int checkPerson) {
		this.checkPerson = checkPerson;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public void setMeanHeightLow(int meanHeightLow) {
		this.meanHeightLow = meanHeightLow;
	}

	public void setMeanHeightLowPercent(double meanHeightLowPercent) {
		this.meanHeightLowPercent = meanHeightLowPercent;
	}

	public void setMeanHeightNormal(int meanHeightNormal) {
		this.meanHeightNormal = meanHeightNormal;
	}

	public void setMeanHeightNormalPercent(double meanHeightNormalPercent) {
		this.meanHeightNormalPercent = meanHeightNormalPercent;
	}

	public void setMeanOverWeight(int meanOverWeight) {
		this.meanOverWeight = meanOverWeight;
	}

	public void setMeanOverWeightPercent(double meanOverWeightPercent) {
		this.meanOverWeightPercent = meanOverWeightPercent;
	}

	public void setMeanWeightLow(int meanWeightLow) {
		this.meanWeightLow = meanWeightLow;
	}

	public void setMeanWeightLowPercent(double meanWeightLowPercent) {
		this.meanWeightLowPercent = meanWeightLowPercent;
	}

	public void setMeanWeightNormal(int meanWeightNormal) {
		this.meanWeightNormal = meanWeightNormal;
	}

	public void setMeanWeightNormalPercent(double meanWeightNormalPercent) {
		this.meanWeightNormalPercent = meanWeightNormalPercent;
	}

	public void setMeanWeightObesity(int meanWeightObesity) {
		this.meanWeightObesity = meanWeightObesity;
	}

	public void setMeanWeightObesityPercent(double meanWeightObesityPercent) {
		this.meanWeightObesityPercent = meanWeightObesityPercent;
	}

	public void setMeanWeightSkinny(int meanWeightSkinny) {
		this.meanWeightSkinny = meanWeightSkinny;
	}

	public void setMeanWeightSkinnyPercent(double meanWeightSkinnyPercent) {
		this.meanWeightSkinnyPercent = meanWeightSkinnyPercent;
	}

	public void setPrctileHeightAgeLow(int prctileHeightAgeLow) {
		this.prctileHeightAgeLow = prctileHeightAgeLow;
	}

	public void setPrctileHeightAgeLowPercent(double prctileHeightAgeLowPercent) {
		this.prctileHeightAgeLowPercent = prctileHeightAgeLowPercent;
	}

	public void setPrctileHeightAgeNormal(int prctileHeightAgeNormal) {
		this.prctileHeightAgeNormal = prctileHeightAgeNormal;
	}

	public void setPrctileHeightAgeNormalPercent(double prctileHeightAgeNormalPercent) {
		this.prctileHeightAgeNormalPercent = prctileHeightAgeNormalPercent;
	}

	public void setPrctileOverWeight(int prctileOverWeight) {
		this.prctileOverWeight = prctileOverWeight;
	}

	public void setPrctileOverWeightPercent(double prctileOverWeightPercent) {
		this.prctileOverWeightPercent = prctileOverWeightPercent;
	}

	public void setPrctileWeightAgeLow(int prctileWeightAgeLow) {
		this.prctileWeightAgeLow = prctileWeightAgeLow;
	}

	public void setPrctileWeightAgeLowPercent(double prctileWeightAgeLowPercent) {
		this.prctileWeightAgeLowPercent = prctileWeightAgeLowPercent;
	}

	public void setPrctileWeightAgeNormal(int prctileWeightAgeNormal) {
		this.prctileWeightAgeNormal = prctileWeightAgeNormal;
	}

	public void setPrctileWeightAgeNormalPercent(double prctileWeightAgeNormalPercent) {
		this.prctileWeightAgeNormalPercent = prctileWeightAgeNormalPercent;
	}

	public void setPrctileWeightHeightLow(int prctileWeightHeightLow) {
		this.prctileWeightHeightLow = prctileWeightHeightLow;
	}

	public void setPrctileWeightHeightLowPercent(double prctileWeightHeightLowPercent) {
		this.prctileWeightHeightLowPercent = prctileWeightHeightLowPercent;
	}

	public void setPrctileWeightHeightNormal(int prctileWeightHeightNormal) {
		this.prctileWeightHeightNormal = prctileWeightHeightNormal;
	}

	public void setPrctileWeightHeightNormalPercent(double prctileWeightHeightNormalPercent) {
		this.prctileWeightHeightNormalPercent = prctileWeightHeightNormalPercent;
	}

	public void setPrctileWeightObesity(int prctileWeightObesity) {
		this.prctileWeightObesity = prctileWeightObesity;
	}

	public void setPrctileWeightObesityPercent(double prctileWeightObesityPercent) {
		this.prctileWeightObesityPercent = prctileWeightObesityPercent;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setTotalPerson(int totalPerson) {
		this.totalPerson = totalPerson;
	}

}
