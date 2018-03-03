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

public class PhysicalGrowthCountQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected String checkId;
	protected List<String> checkIds;
	protected List<String> tenantIds;
	protected String gradeId;
	protected List<String> gradeIds;
	protected String type;

	public PhysicalGrowthCountQuery() {

	}

	public PhysicalGrowthCountQuery checkId(String checkId) {
		if (checkId == null) {
			throw new RuntimeException("checkId is null");
		}
		this.checkId = checkId;
		return this;
	}

	public PhysicalGrowthCountQuery checkIds(List<String> checkIds) {
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

			if ("level1".equals(sortColumn)) {
				orderBy = "E.LEVEL1_" + a_x;
			}

			if ("level2".equals(sortColumn)) {
				orderBy = "E.LEVEL2_" + a_x;
			}

			if ("level3".equals(sortColumn)) {
				orderBy = "E.LEVEL3_" + a_x;
			}

			if ("level5".equals(sortColumn)) {
				orderBy = "E.LEVEL5_" + a_x;
			}

			if ("level7".equals(sortColumn)) {
				orderBy = "E.LEVEL7_" + a_x;
			}

			if ("level8".equals(sortColumn)) {
				orderBy = "E.LEVEL8_" + a_x;
			}

			if ("level9".equals(sortColumn)) {
				orderBy = "E.LEVEL9_" + a_x;
			}

			if ("level1Percent".equals(sortColumn)) {
				orderBy = "E.LEVEL1PERCENT_" + a_x;
			}

			if ("level2Percent".equals(sortColumn)) {
				orderBy = "E.LEVEL2PERCENT_" + a_x;
			}

			if ("level3Percent".equals(sortColumn)) {
				orderBy = "E.LEVEL3PERCENT_" + a_x;
			}

			if ("level5Percent".equals(sortColumn)) {
				orderBy = "E.LEVEL5PERCENT_" + a_x;
			}

			if ("level7Percent".equals(sortColumn)) {
				orderBy = "E.LEVEL7PERCENT_" + a_x;
			}

			if ("level8Percent".equals(sortColumn)) {
				orderBy = "E.LEVEL8PERCENT_" + a_x;
			}

			if ("level9Percent".equals(sortColumn)) {
				orderBy = "E.LEVEL9PERCENT_" + a_x;
			}

			if ("normal".equals(sortColumn)) {
				orderBy = "E.NORMAL_" + a_x;
			}

			if ("normalPercent".equals(sortColumn)) {
				orderBy = "E.NORMALPERCENT_" + a_x;
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

	public PhysicalGrowthCountQuery gradeId(String gradeId) {
		if (gradeId == null) {
			throw new RuntimeException("gradeId is null");
		}
		this.gradeId = gradeId;
		return this;
	}

	public PhysicalGrowthCountQuery gradeIds(List<String> gradeIds) {
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
		addColumn("level1", "LEVEL1_");
		addColumn("level2", "LEVEL2_");
		addColumn("level3", "LEVEL3_");
		addColumn("level5", "LEVEL5_");
		addColumn("level7", "LEVEL7_");
		addColumn("level8", "LEVEL8_");
		addColumn("level9", "LEVEL9_");
		addColumn("level1Percent", "LEVEL1PERCENT_");
		addColumn("level2Percent", "LEVEL2PERCENT_");
		addColumn("level3Percent", "LEVEL3PERCENT_");
		addColumn("level5Percent", "LEVEL5PERCENT_");
		addColumn("level7Percent", "LEVEL7PERCENT_");
		addColumn("level8Percent", "LEVEL8PERCENT_");
		addColumn("level9Percent", "LEVEL9PERCENT_");
		addColumn("normal", "NORMAL_");
		addColumn("normalPercent", "NORMALPERCENT_");
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

	public PhysicalGrowthCountQuery tenantId(String tenantId) {
		if (tenantId == null) {
			throw new RuntimeException("tenantId is null");
		}
		this.tenantId = tenantId;
		return this;
	}

	public PhysicalGrowthCountQuery tenantIds(List<String> tenantIds) {
		if (tenantIds == null) {
			throw new RuntimeException("tenantIds is empty ");
		}
		this.tenantIds = tenantIds;
		return this;
	}

}