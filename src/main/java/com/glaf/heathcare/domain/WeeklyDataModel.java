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

public class WeeklyDataModel implements Serializable {

	private static final long serialVersionUID = 1L;

	protected String name;

	protected String dateString;

	protected int maxSize;

	protected List<RptModel> items = new ArrayList<RptModel>();

	protected List<Object> items1 = new ArrayList<Object>();

	protected List<Object> items2 = new ArrayList<Object>();

	protected List<Object> items3 = new ArrayList<Object>();

	protected List<Object> items4 = new ArrayList<Object>();

	protected List<Object> items5 = new ArrayList<Object>();

	protected List<Object> items6 = new ArrayList<Object>();

	protected List<Object> items7 = new ArrayList<Object>();

	public WeeklyDataModel() {

	}

	public String getDateString() {
		return dateString;
	}

	public List<RptModel> getItems() {
		return items;
	}

	public List<Object> getItems1() {
		return items1;
	}

	public List<Object> getItems2() {
		return items2;
	}

	public List<Object> getItems3() {
		return items3;
	}

	public List<Object> getItems4() {
		return items4;
	}

	public List<Object> getItems5() {
		return items5;
	}

	public List<Object> getItems6() {
		return items6;
	}

	public List<Object> getItems7() {
		return items7;
	}

	public int getMaxSize() {
		return maxSize;
	}

	public String getName() {
		return name;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	public void setItems(List<RptModel> items) {
		this.items = items;
	}

	public void setItems1(List<Object> items1) {
		this.items1 = items1;
	}

	public void setItems2(List<Object> items2) {
		this.items2 = items2;
	}

	public void setItems3(List<Object> items3) {
		this.items3 = items3;
	}

	public void setItems4(List<Object> items4) {
		this.items4 = items4;
	}

	public void setItems5(List<Object> items5) {
		this.items5 = items5;
	}

	public void setItems6(List<Object> items6) {
		this.items6 = items6;
	}

	public void setItems7(List<Object> items7) {
		this.items7 = items7;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	public void setName(String name) {
		this.name = name;
	}

}
