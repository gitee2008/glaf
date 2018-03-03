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

public class FoodRestrictionQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected Long foodId;
	protected List<Long> foodIds;

	public FoodRestrictionQuery() {

	}

	public FoodRestrictionQuery foodId(Long foodId) {
		if (foodId == null) {
			throw new RuntimeException("foodId is null");
		}
		this.foodId = foodId;
		return this;
	}

	public FoodRestrictionQuery foodIds(List<Long> foodIds) {
		if (foodIds == null) {
			throw new RuntimeException("foodIds is empty ");
		}
		this.foodIds = foodIds;
		return this;
	}

	public Long getFoodId() {
		return foodId;
	}

	public List<Long> getFoodIds() {
		return foodIds;
	}

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("foodId".equals(sortColumn)) {
				orderBy = "E.FOODID_" + a_x;
			}

			if ("restrictionFoodId".equals(sortColumn)) {
				orderBy = "E.RESTRICTIONFOODID_" + a_x;
			}

			if ("description".equals(sortColumn)) {
				orderBy = "E.DESCRIPTION_" + a_x;
			}

			if ("createBy".equals(sortColumn)) {
				orderBy = "E.CREATEBY_" + a_x;
			}

			if ("createTime".equals(sortColumn)) {
				orderBy = "E.CREATETIME_" + a_x;
			}

			if ("updateBy".equals(sortColumn)) {
				orderBy = "E.UPDATEBY_" + a_x;
			}

			if ("updateTime".equals(sortColumn)) {
				orderBy = "E.UPDATETIME_" + a_x;
			}

		}
		return orderBy;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("foodId", "FOODID_");
		addColumn("restrictionFoodId", "RESTRICTIONFOODID_");
		addColumn("description", "DESCRIPTION_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
		addColumn("updateBy", "UPDATEBY_");
		addColumn("updateTime", "UPDATETIME_");
	}

	public void setFoodId(Long foodId) {
		this.foodId = foodId;
	}

	public void setFoodIds(List<Long> foodIds) {
		this.foodIds = foodIds;
	}

}