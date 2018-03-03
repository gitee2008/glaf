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

public class MedicalExaminationSicknessPositiveSign implements Serializable {

	private static final long serialVersionUID = 1L;

	protected String tenantId;

	protected String gradeId;

	protected String gradeName;

	protected int personTotal;

	protected int personCheckTotal;

	protected int internalDiseaseTotal;

	protected int surgicalDiseaseTotal;

	protected int saprodontiaTotal;

	protected int trachomaTotal;

	protected int amblyopiaTotal;

	protected int hemoglobin110Total;

	protected int hemoglobin90Total;

	protected int hbsabTotal;

	protected int sgptTotal;

	protected int hvaigmTotal;

	public MedicalExaminationSicknessPositiveSign() {

	}

	public int getAmblyopiaTotal() {
		return amblyopiaTotal;
	}

	public String getGradeId() {
		return gradeId;
	}

	public String getGradeName() {
		return gradeName;
	}

	public int getHbsabTotal() {
		return hbsabTotal;
	}

	public int getHemoglobin110Total() {
		return hemoglobin110Total;
	}

	public int getHemoglobin90Total() {
		return hemoglobin90Total;
	}

	public int getHvaigmTotal() {
		return hvaigmTotal;
	}

	public int getInternalDiseaseTotal() {
		return internalDiseaseTotal;
	}

	public int getPersonCheckTotal() {
		return personCheckTotal;
	}

	public int getPersonTotal() {
		return personTotal;
	}

	public int getSaprodontiaTotal() {
		return saprodontiaTotal;
	}

	public int getSgptTotal() {
		return sgptTotal;
	}

	public int getSurgicalDiseaseTotal() {
		return surgicalDiseaseTotal;
	}

	public String getTenantId() {
		return tenantId;
	}

	public int getTrachomaTotal() {
		return trachomaTotal;
	}

	public void setAmblyopiaTotal(int amblyopiaTotal) {
		this.amblyopiaTotal = amblyopiaTotal;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public void setHbsabTotal(int hbsabTotal) {
		this.hbsabTotal = hbsabTotal;
	}

	public void setHemoglobin110Total(int hemoglobin110Total) {
		this.hemoglobin110Total = hemoglobin110Total;
	}

	public void setHemoglobin90Total(int hemoglobin90Total) {
		this.hemoglobin90Total = hemoglobin90Total;
	}

	public void setHvaigmTotal(int hvaigmTotal) {
		this.hvaigmTotal = hvaigmTotal;
	}

	public void setInternalDiseaseTotal(int internalDiseaseTotal) {
		this.internalDiseaseTotal = internalDiseaseTotal;
	}

	public void setPersonCheckTotal(int personCheckTotal) {
		this.personCheckTotal = personCheckTotal;
	}

	public void setPersonTotal(int personTotal) {
		this.personTotal = personTotal;
	}

	public void setSaprodontiaTotal(int saprodontiaTotal) {
		this.saprodontiaTotal = saprodontiaTotal;
	}

	public void setSgptTotal(int sgptTotal) {
		this.sgptTotal = sgptTotal;
	}

	public void setSurgicalDiseaseTotal(int surgicalDiseaseTotal) {
		this.surgicalDiseaseTotal = surgicalDiseaseTotal;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setTrachomaTotal(int trachomaTotal) {
		this.trachomaTotal = trachomaTotal;
	}

}
