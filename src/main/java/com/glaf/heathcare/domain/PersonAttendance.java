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

public class PersonAttendance implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 租户编号
	 */
	protected String tenantId;

	/**
	 * 班级编号
	 */
	protected String gradeId;

	/**
	 * 学生编号
	 */
	protected String personId;

	/**
	 * 姓名
	 */
	protected String name;

	protected int days1;

	protected int days2;

	protected int days3;

	protected int days4;

	protected String status1;

	protected String status2;

	protected String status3;

	protected String status4;

	protected String status5;

	protected String status6;

	protected String status7;

	protected String status8;

	protected String status9;

	protected String status10;

	protected String status11;

	protected String status12;

	protected String status13;

	protected String status14;

	protected String status15;

	protected String status16;

	protected String status17;

	protected String status18;

	protected String status19;

	protected String status20;

	protected String status21;

	protected String status22;

	protected String status23;

	protected String status24;

	protected String status25;

	protected String status26;

	protected String status27;

	protected String status28;

	protected String status29;

	protected String status30;

	protected String status31;

	public PersonAttendance() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonAttendance other = (PersonAttendance) obj;
		if (personId == null) {
			if (other.personId != null)
				return false;
		} else if (!personId.equals(other.personId))
			return false;
		return true;
	}

	public int getDays1() {
		return days1;
	}

	public int getDays2() {
		return days2;
	}

	public int getDays3() {
		return days3;
	}

	public int getDays4() {
		return days4;
	}

	public String getGradeId() {
		return gradeId;
	}

	public String getName() {
		return name;
	}

	public String getPersonId() {
		return personId;
	}

	public String getStatus1() {
		return status1;
	}

	public String getStatus10() {
		return status10;
	}

	public String getStatus11() {
		return status11;
	}

	public String getStatus12() {
		return status12;
	}

	public String getStatus13() {
		return status13;
	}

	public String getStatus14() {
		return status14;
	}

	public String getStatus15() {
		return status15;
	}

	public String getStatus16() {
		return status16;
	}

	public String getStatus17() {
		return status17;
	}

	public String getStatus18() {
		return status18;
	}

	public String getStatus19() {
		return status19;
	}

	public String getStatus2() {
		return status2;
	}

	public String getStatus20() {
		return status20;
	}

	public String getStatus21() {
		return status21;
	}

	public String getStatus22() {
		return status22;
	}

	public String getStatus23() {
		return status23;
	}

	public String getStatus24() {
		return status24;
	}

	public String getStatus25() {
		return status25;
	}

	public String getStatus26() {
		return status26;
	}

	public String getStatus27() {
		return status27;
	}

	public String getStatus28() {
		return status28;
	}

	public String getStatus29() {
		return status29;
	}

	public String getStatus3() {
		return status3;
	}

	public String getStatus30() {
		return status30;
	}

	public String getStatus31() {
		return status31;
	}

	public String getStatus4() {
		return status4;
	}

	public String getStatus5() {
		return status5;
	}

	public String getStatus6() {
		return status6;
	}

	public String getStatus7() {
		return status7;
	}

	public String getStatus8() {
		return status8;
	}

	public String getStatus9() {
		return status9;
	}

	public String getTenantId() {
		return tenantId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((personId == null) ? 0 : personId.hashCode());
		return result;
	}

	public void setDays1(int days1) {
		this.days1 = days1;
	}

	public void setDays2(int days2) {
		this.days2 = days2;
	}

	public void setDays3(int days3) {
		this.days3 = days3;
	}

	public void setDays4(int days4) {
		this.days4 = days4;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public void setStatus1(String status1) {
		this.status1 = status1;
	}

	public void setStatus10(String status10) {
		this.status10 = status10;
	}

	public void setStatus11(String status11) {
		this.status11 = status11;
	}

	public void setStatus12(String status12) {
		this.status12 = status12;
	}

	public void setStatus13(String status13) {
		this.status13 = status13;
	}

	public void setStatus14(String status14) {
		this.status14 = status14;
	}

	public void setStatus15(String status15) {
		this.status15 = status15;
	}

	public void setStatus16(String status16) {
		this.status16 = status16;
	}

	public void setStatus17(String status17) {
		this.status17 = status17;
	}

	public void setStatus18(String status18) {
		this.status18 = status18;
	}

	public void setStatus19(String status19) {
		this.status19 = status19;
	}

	public void setStatus2(String status2) {
		this.status2 = status2;
	}

	public void setStatus20(String status20) {
		this.status20 = status20;
	}

	public void setStatus21(String status21) {
		this.status21 = status21;
	}

	public void setStatus22(String status22) {
		this.status22 = status22;
	}

	public void setStatus23(String status23) {
		this.status23 = status23;
	}

	public void setStatus24(String status24) {
		this.status24 = status24;
	}

	public void setStatus25(String status25) {
		this.status25 = status25;
	}

	public void setStatus26(String status26) {
		this.status26 = status26;
	}

	public void setStatus27(String status27) {
		this.status27 = status27;
	}

	public void setStatus28(String status28) {
		this.status28 = status28;
	}

	public void setStatus29(String status29) {
		this.status29 = status29;
	}

	public void setStatus3(String status3) {
		this.status3 = status3;
	}

	public void setStatus30(String status30) {
		this.status30 = status30;
	}

	public void setStatus31(String status31) {
		this.status31 = status31;
	}

	public void setStatus4(String status4) {
		this.status4 = status4;
	}

	public void setStatus5(String status5) {
		this.status5 = status5;
	}

	public void setStatus6(String status6) {
		this.status6 = status6;
	}

	public void setStatus7(String status7) {
		this.status7 = status7;
	}

	public void setStatus8(String status8) {
		this.status8 = status8;
	}

	public void setStatus9(String status9) {
		this.status9 = status9;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

}
