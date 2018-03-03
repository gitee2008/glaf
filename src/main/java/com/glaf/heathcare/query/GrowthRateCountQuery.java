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

public class GrowthRateCountQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected String checkId;
	protected List<String> checkIds;
	protected List<String> tenantIds;
	protected String gradeId;
	protected List<String> gradeIds;
	protected String type;

	public GrowthRateCountQuery() {

	}

	public GrowthRateCountQuery checkId(String checkId) {
		if (checkId == null) {
			throw new RuntimeException("checkId is null");
		}
		this.checkId = checkId;
		return this;
	}

	public GrowthRateCountQuery checkIds(List<String> checkIds) {
		if (checkIds == null) {
			throw new RuntimeException("checkIds is empty ");
		}
		this.checkIds = checkIds;
		return this;
	}

	public String getCheckId() {
		return checkId;
	}

	public List<String> getCheckIds() {
		return checkIds;
	}

	public String getGradeId() {
		return gradeId;
	}

	public List<String> getGradeIds() {
		return gradeIds;
	}

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("checkId".equals(sortColumn)) {
				orderBy = "E.CHECKID_" + a_x;
			}

			if ("tenantId".equals(sortColumn)) {
				orderBy = "E.TENANTID_" + a_x;
			}

			if ("gradeId".equals(sortColumn)) {
				orderBy = "E.GRADEID_" + a_x;
			}

			if ("increase".equals(sortColumn)) {
				orderBy = "E.INCREASE_" + a_x;
			}

			if ("increasePercent".equals(sortColumn)) {
				orderBy = "E.INCREASEPERCENT_" + a_x;
			}

			if ("standard".equals(sortColumn)) {
				orderBy = "E.STANDARD_" + a_x;
			}

			if ("standardPercent".equals(sortColumn)) {
				orderBy = "E.STANDARDPERCENT_" + a_x;
			}

		}
		return orderBy;
	}

	public String getTenantId() {
		return tenantId;
	}

	public List<String> getTenantIds() {
		return tenantIds;
	}

	public String getType() {
		return type;
	}

	public GrowthRateCountQuery gradeId(String gradeId) {
		if (gradeId == null) {
			throw new RuntimeException("gradeId is null");
		}
		this.gradeId = gradeId;
		return this;
	}

	public GrowthRateCountQuery gradeIds(List<String> gradeIds) {
		if (gradeIds == null) {
			throw new RuntimeException("gradeIds is empty ");
		}
		this.gradeIds = gradeIds;
		return this;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("checkId", "CHECKID_");
		addColumn("tenantId", "TENANTID_");
		addColumn("gradeId", "GRADEID_");
		addColumn("increase", "INCREASE_");
		addColumn("increasePercent", "INCREASEPERCENT_");
		addColumn("standard", "STANDARD_");
		addColumn("standardPercent", "STANDARDPERCENT_");
	}

	public void setCheckId(String checkId) {
		this.checkId = checkId;
	}

	public void setCheckIds(List<String> checkIds) {
		this.checkIds = checkIds;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public void setGradeIds(List<String> gradeIds) {
		this.gradeIds = gradeIds;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setTenantIds(List<String> tenantIds) {
		this.tenantIds = tenantIds;
	}

	public void setType(String type) {
		this.type = type;
	}

	public GrowthRateCountQuery tenantId(String tenantId) {
		if (tenantId == null) {
			throw new RuntimeException("tenantId is null");
		}
		this.tenantId = tenantId;
		return this;
	}

	public GrowthRateCountQuery tenantIds(List<String> tenantIds) {
		if (tenantIds == null) {
			throw new RuntimeException("tenantIds is empty ");
		}
		this.tenantIds = tenantIds;
		return this;
	}

}