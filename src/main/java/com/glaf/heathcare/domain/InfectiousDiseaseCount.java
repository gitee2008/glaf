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

public class InfectiousDiseaseCount implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	protected String tenantId;

	protected String gradeId;

	protected int year;

	protected int month;

	protected int personTotal;

	protected int infectiousCount;

	protected int item1;

	protected int item2;

	protected int item3;

	protected int item4;

	protected int item5;

	protected int item6;

	protected int item7;

	protected int item8;

	protected int item9;

	protected int item10;

	public InfectiousDiseaseCount() {

	}

	public String getGradeId() {
		return gradeId;
	}

	public int getInfectiousCount() {
		return infectiousCount;
	}

	public int getItem1() {
		return item1;
	}

	public int getItem10() {
		return item10;
	}

	public int getItem2() {
		return item2;
	}

	public int getItem3() {
		return item3;
	}

	public int getItem4() {
		return item4;
	}

	public int getItem5() {
		return item5;
	}

	public int getItem6() {
		return item6;
	}

	public int getItem7() {
		return item7;
	}

	public int getItem8() {
		return item8;
	}

	public int getItem9() {
		return item9;
	}

	public int getMonth() {
		return month;
	}

	public int getPersonTotal() {
		return personTotal;
	}

	public String getTenantId() {
		return tenantId;
	}

	public int getYear() {
		return year;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public void setInfectiousCount(int infectiousCount) {
		this.infectiousCount = infectiousCount;
	}

	public void setItem1(int item1) {
		this.item1 = item1;
	}

	public void setItem10(int item10) {
		this.item10 = item10;
	}

	public void setItem2(int item2) {
		this.item2 = item2;
	}

	public void setItem3(int item3) {
		this.item3 = item3;
	}

	public void setItem4(int item4) {
		this.item4 = item4;
	}

	public void setItem5(int item5) {
		this.item5 = item5;
	}

	public void setItem6(int item6) {
		this.item6 = item6;
	}

	public void setItem7(int item7) {
		this.item7 = item7;
	}

	public void setItem8(int item8) {
		this.item8 = item8;
	}

	public void setItem9(int item9) {
		this.item9 = item9;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setPersonTotal(int personTotal) {
		this.personTotal = personTotal;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setYear(int year) {
		this.year = year;
	}

}
