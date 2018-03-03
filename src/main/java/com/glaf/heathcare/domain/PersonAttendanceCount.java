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

import java.util.Date;

public class PersonAttendanceCount implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 租户编号
	 */
	protected String tenantId;

	/**
	 * 班级编号
	 */
	protected String gradeId;

	protected Date date;

	protected int totalPerson;

	protected int actualPerson;

	protected double actualPercent;

	protected int sicknessPerson;

	protected double sicknessPercent;

	protected int affairPerson;

	protected double affairPercent;

	protected int otherPerson;

	protected double otherPercent;

	public PersonAttendanceCount() {

	}

	public double getActualPercent() {
		if (actualPercent > 0) {
			actualPercent = Math.round(actualPercent * 10000D) / 100D;
		}
		return actualPercent;
	}

	public int getActualPerson() {
		return actualPerson;
	}

	public double getAffairPercent() {
		if (affairPercent > 0) {
			affairPercent = Math.round(affairPercent * 10000D) / 100D;
		}
		return affairPercent;
	}

	public int getAffairPerson() {
		return affairPerson;
	}

	public Date getDate() {
		return date;
	}

	public String getGradeId() {
		return gradeId;
	}

	public double getOtherPercent() {
		if (otherPercent > 0) {
			otherPercent = Math.round(otherPercent * 10000D) / 100D;
		}
		return otherPercent;
	}

	public int getOtherPerson() {
		return otherPerson;
	}

	public double getSicknessPercent() {
		if (sicknessPercent > 0) {
			sicknessPercent = Math.round(sicknessPercent * 10000D) / 100D;
		}
		return sicknessPercent;
	}

	public int getSicknessPerson() {
		return sicknessPerson;
	}

	public String getTenantId() {
		return tenantId;
	}

	public int getTotalPerson() {
		return totalPerson;
	}

	public void setActualPercent(double actualPercent) {
		this.actualPercent = actualPercent;
	}

	public void setActualPerson(int actualPerson) {
		this.actualPerson = actualPerson;
	}

	public void setAffairPercent(double affairPercent) {
		this.affairPercent = affairPercent;
	}

	public void setAffairPerson(int affairPerson) {
		this.affairPerson = affairPerson;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public void setOtherPercent(double otherPercent) {
		this.otherPercent = otherPercent;
	}

	public void setOtherPerson(int otherPerson) {
		this.otherPerson = otherPerson;
	}

	public void setSicknessPercent(double sicknessPercent) {
		this.sicknessPercent = sicknessPercent;
	}

	public void setSicknessPerson(int sicknessPerson) {
		this.sicknessPerson = sicknessPerson;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setTotalPerson(int totalPerson) {
		this.totalPerson = totalPerson;
	}

}
