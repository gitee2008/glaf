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

public class WeeklyActualQuantityModel implements Serializable {

	private static final long serialVersionUID = 1L;

	protected String name;

	protected String dateString;

	protected List<GoodsActualQuantity> rows1 = new ArrayList<GoodsActualQuantity>();

	protected List<GoodsActualQuantity> rows2 = new ArrayList<GoodsActualQuantity>();

	protected List<GoodsActualQuantity> rows3 = new ArrayList<GoodsActualQuantity>();

	protected int pageNo;

	public WeeklyActualQuantityModel() {

	}

	public String getDateString() {
		return dateString;
	}

	public String getName() {
		return name;
	}

	public int getPageNo() {
		return pageNo;
	}

	public List<GoodsActualQuantity> getRows1() {
		return rows1;
	}

	public List<GoodsActualQuantity> getRows2() {
		return rows2;
	}

	public List<GoodsActualQuantity> getRows3() {
		return rows3;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public void setRows1(List<GoodsActualQuantity> rows1) {
		this.rows1 = rows1;
	}

	public void setRows2(List<GoodsActualQuantity> rows2) {
		this.rows2 = rows2;
	}

	public void setRows3(List<GoodsActualQuantity> rows3) {
		this.rows3 = rows3;
	}

}
