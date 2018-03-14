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
import java.util.ArrayList;
import java.util.List;

public class PersonInspectionCount implements Serializable {

	private static final long serialVersionUID = 1L;

	protected String dateString;

	protected int total;

	protected int nomal1;

	protected int nomal2;

	protected int abnormal1;

	protected int abnormal2;

	protected List<PersonInspection> children1 = new ArrayList<PersonInspection>();

	protected List<PersonInspection> children2 = new ArrayList<PersonInspection>();

	public PersonInspectionCount() {

	}

	public void addChild1(PersonInspection child1) {
		if (children1 == null) {
			children1 = new ArrayList<PersonInspection>();
		}
		children1.add(child1);
	}

	public void addChild2(PersonInspection child2) {
		if (children2 == null) {
			children2 = new ArrayList<PersonInspection>();
		}
		children2.add(child2);
	}

	public int getAbnormal1() {
		return abnormal1;
	}

	public int getAbnormal2() {
		return abnormal2;
	}

	public List<PersonInspection> getChildren1() {
		return children1;
	}

	public List<PersonInspection> getChildren2() {
		return children2;
	}

	public String getDateString() {
		return dateString;
	}

	public int getNomal1() {
		return nomal1;
	}

	public int getNomal2() {
		return nomal2;
	}

	public int getTotal() {
		return total;
	}

	public void setAbnormal1(int abnormal1) {
		this.abnormal1 = abnormal1;
	}

	public void setAbnormal2(int abnormal2) {
		this.abnormal2 = abnormal2;
	}

	public void setChildren1(List<PersonInspection> children1) {
		this.children1 = children1;
	}

	public void setChildren2(List<PersonInspection> children2) {
		this.children2 = children2;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	public void setNomal1(int nomal1) {
		this.nomal1 = nomal1;
	}

	public void setNomal2(int nomal2) {
		this.nomal2 = nomal2;
	}

	public void setTotal(int total) {
		this.total = total;
	}

}
