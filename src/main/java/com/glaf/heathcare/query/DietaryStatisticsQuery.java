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

package com.glaf.heathcare.query;

import java.util.*;
import com.glaf.core.query.DataQuery;

public class DietaryStatisticsQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected List<Long> ids;
	protected List<String> tenantIds;
	protected String name;
	protected String nameLike;
	protected String mealType;
	protected String sysFlag;
	protected String type;
	protected Integer dayOfWeek;
	protected Integer suitNo;
	protected Integer semester;
	protected Integer year;
	protected Integer week;
	protected String batchNo;

	public DietaryStatisticsQuery() {

	}

	public DietaryStatisticsQuery batchNo(String batchNo) {
		if (batchNo == null) {
			throw new RuntimeException("batchNo is null");
		}
		this.batchNo = batchNo;
		return this;
	}

	public DietaryStatisticsQuery dayOfWeek(Integer dayOfWeek) {
		if (dayOfWeek == null) {
			throw new RuntimeException("dayOfWeek is null");
		}
		this.dayOfWeek = dayOfWeek;
		return this;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public Integer getDayOfWeek() {
		return dayOfWeek;
	}

	public List<Long> getIds() {
		return ids;
	}

	public String getMealType() {
		return mealType;
	}

	public String getName() {
		return name;
	}

	public String getNameLike() {
		if (nameLike != null && nameLike.trim().length() > 0) {
			if (!nameLike.startsWith("%")) {
				nameLike = "%" + nameLike;
			}
			if (!nameLike.endsWith("%")) {
				nameLike = nameLike + "%";
			}
		}
		return nameLike;
	}

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("tenantId".equals(sortColumn)) {
				orderBy = "E.TENANTID_" + a_x;
			}

			if ("name".equals(sortColumn)) {
				orderBy = "E.NAME_" + a_x;
			}

			if ("value".equals(sortColumn)) {
				orderBy = "E.VALUE_" + a_x;
			}

			if ("mealType".equals(sortColumn)) {
				orderBy = "E.MEALTYPE_" + a_x;
			}

			if ("sysFlag".equals(sortColumn)) {
				orderBy = "E.SYSFLAG_" + a_x;
			}

			if ("type".equals(sortColumn)) {
				orderBy = "E.TYPE_" + a_x;
			}

			if ("dayOfWeek".equals(sortColumn)) {
				orderBy = "E.DAYOFWEEK_" + a_x;
			}

			if ("suitNo".equals(sortColumn)) {
				orderBy = "E.SUITNO_" + a_x;
			}

			if ("createTime".equals(sortColumn)) {
				orderBy = "E.CREATETIME_" + a_x;
			}

		}
		return orderBy;
	}

	public Integer getSemester() {
		return semester;
	}

	public Integer getSuitNo() {
		return suitNo;
	}

	public String getSysFlag() {
		return sysFlag;
	}

	public List<String> getTenantIds() {
		return tenantIds;
	}

	public String getType() {
		return type;
	}

	public Integer getWeek() {
		return week;
	}

	public Integer getYear() {
		return year;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("tenantId", "TENANTID_");
		addColumn("name", "NAME_");
		addColumn("value", "VALUE_");
		addColumn("mealType", "MEALTYPE_");
		addColumn("sysFlag", "SYSFLAG_");
		addColumn("type", "TYPE_");
		addColumn("dayOfWeek", "DAYOFWEEK_");
		addColumn("suitNo", "SUITNO_");
		addColumn("createTime", "CREATETIME_");
	}

	public DietaryStatisticsQuery mealType(String mealType) {
		if (mealType == null) {
			throw new RuntimeException("mealType is null");
		}
		this.mealType = mealType;
		return this;
	}

	public DietaryStatisticsQuery name(String name) {
		if (name == null) {
			throw new RuntimeException("name is null");
		}
		this.name = name;
		return this;
	}

	public DietaryStatisticsQuery nameLike(String nameLike) {
		if (nameLike == null) {
			throw new RuntimeException("name is null");
		}
		this.nameLike = nameLike;
		return this;
	}

	public DietaryStatisticsQuery semester(Integer semester) {
		if (semester == null) {
			throw new RuntimeException("semester is null");
		}
		this.semester = semester;
		return this;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public void setDayOfWeek(Integer dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}

	public void setMealType(String mealType) {
		this.mealType = mealType;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNameLike(String nameLike) {
		this.nameLike = nameLike;
	}

	public void setSemester(Integer semester) {
		this.semester = semester;
	}

	public void setSuitNo(Integer suitNo) {
		this.suitNo = suitNo;
	}

	public void setSysFlag(String sysFlag) {
		this.sysFlag = sysFlag;
	}

	public void setTenantIds(List<String> tenantIds) {
		this.tenantIds = tenantIds;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setWeek(Integer week) {
		this.week = week;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public DietaryStatisticsQuery suitNo(Integer suitNo) {
		if (suitNo == null) {
			throw new RuntimeException("suitNo is null");
		}
		this.suitNo = suitNo;
		return this;
	}

	public DietaryStatisticsQuery sysFlag(String sysFlag) {
		if (sysFlag == null) {
			throw new RuntimeException("sysFlag is null");
		}
		this.sysFlag = sysFlag;
		return this;
	}

	public DietaryStatisticsQuery tenantId(String tenantId) {
		if (tenantId == null) {
			throw new RuntimeException("tenantId is null");
		}
		this.tenantId = tenantId;
		return this;
	}

	public DietaryStatisticsQuery tenantIds(List<String> tenantIds) {
		if (tenantIds == null) {
			throw new RuntimeException("tenantIds is empty ");
		}
		this.tenantIds = tenantIds;
		return this;
	}

	public DietaryStatisticsQuery type(String type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		this.type = type;
		return this;
	}

	public DietaryStatisticsQuery week(Integer week) {
		if (week == null) {
			throw new RuntimeException("week is null");
		}
		this.week = week;
		return this;
	}

	public DietaryStatisticsQuery year(Integer year) {
		if (year == null) {
			throw new RuntimeException("year is null");
		}
		this.year = year;
		return this;
	}

}