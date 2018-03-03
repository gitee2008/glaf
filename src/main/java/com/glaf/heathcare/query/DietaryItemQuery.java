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

public class DietaryItemQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected Long id;
	protected String name;
	protected String nameLike;
	protected String descriptionLike;
	protected Long foodId;
	protected List<Long> foodIds;
	protected String foodNameLike;
	protected Long dietaryId;
	protected List<Long> dietaryIds;
	protected Long templateId;
	protected List<Long> templateIds;
	protected Long typeId;
	protected List<Long> typeIds;
	protected String tableSuffix;
	protected Double quantityGreaterThanOrEqual;
	protected Double quantityLessThanOrEqual;
	protected Integer fullDay;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	public DietaryItemQuery() {

	}

	public DietaryItemQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public DietaryItemQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public DietaryItemQuery descriptionLike(String descriptionLike) {
		if (descriptionLike == null) {
			throw new RuntimeException("description is null");
		}
		this.descriptionLike = descriptionLike;
		return this;
	}

	public DietaryItemQuery dietaryId(Long dietaryId) {
		if (dietaryId == null) {
			throw new RuntimeException("dietaryId is null");
		}
		this.dietaryId = dietaryId;
		return this;
	}

	public DietaryItemQuery dietaryIds(List<Long> dietaryIds) {
		if (dietaryIds == null) {
			throw new RuntimeException("dietaryIds is empty ");
		}
		this.dietaryIds = dietaryIds;
		return this;
	}

	public DietaryItemQuery foodId(Long foodId) {
		if (foodId == null) {
			throw new RuntimeException("foodId is null");
		}
		this.foodId = foodId;
		return this;
	}

	public DietaryItemQuery foodIds(List<Long> foodIds) {
		if (foodIds == null) {
			throw new RuntimeException("foodIds is empty ");
		}
		this.foodIds = foodIds;
		return this;
	}

	public DietaryItemQuery foodNameLike(String foodNameLike) {
		if (foodNameLike == null) {
			throw new RuntimeException("foodName is null");
		}
		this.foodNameLike = foodNameLike;
		return this;
	}

	public DietaryItemQuery fullDay(Integer fullDay) {
		if (fullDay == null) {
			throw new RuntimeException("fullDay is null");
		}
		this.fullDay = fullDay;
		return this;
	}

	public Date getCreateTimeGreaterThanOrEqual() {
		return createTimeGreaterThanOrEqual;
	}

	public Date getCreateTimeLessThanOrEqual() {
		return createTimeLessThanOrEqual;
	}

	public String getDescriptionLike() {
		if (descriptionLike != null && descriptionLike.trim().length() > 0) {
			if (!descriptionLike.startsWith("%")) {
				descriptionLike = "%" + descriptionLike;
			}
			if (!descriptionLike.endsWith("%")) {
				descriptionLike = descriptionLike + "%";
			}
		}
		return descriptionLike;
	}

	public Long getDietaryId() {
		return dietaryId;
	}

	public List<Long> getDietaryIds() {
		return dietaryIds;
	}

	public Long getFoodId() {
		return foodId;
	}

	public List<Long> getFoodIds() {
		return foodIds;
	}

	public String getFoodNameLike() {
		if (foodNameLike != null && foodNameLike.trim().length() > 0) {
			if (!foodNameLike.startsWith("%")) {
				foodNameLike = "%" + foodNameLike;
			}
			if (!foodNameLike.endsWith("%")) {
				foodNameLike = foodNameLike + "%";
			}
		}
		return foodNameLike;
	}

	public Integer getFullDay() {
		return fullDay;
	}

	public Long getId() {
		return id;
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

			if ("name".equals(sortColumn)) {
				orderBy = "E.NAME_" + a_x;
			}

			if ("description".equals(sortColumn)) {
				orderBy = "E.DESCRIPTION_" + a_x;
			}

			if ("foodId".equals(sortColumn)) {
				orderBy = "E.FOODID_" + a_x;
			}

			if ("foodName".equals(sortColumn)) {
				orderBy = "E.FOODNAME_" + a_x;
			}

			if ("templateId".equals(sortColumn)) {
				orderBy = "E.TEMPLATEID_" + a_x;
			}

			if ("quantity".equals(sortColumn)) {
				orderBy = "E.QUANTITY_" + a_x;
			}

			if ("unit".equals(sortColumn)) {
				orderBy = "E.UNIT_" + a_x;
			}

			if ("createBy".equals(sortColumn)) {
				orderBy = "E.CREATEBY_" + a_x;
			}

			if ("createTime".equals(sortColumn)) {
				orderBy = "E.CREATETIME_" + a_x;
			}

		}
		return orderBy;
	}

	public Double getQuantityGreaterThanOrEqual() {
		return quantityGreaterThanOrEqual;
	}

	public Double getQuantityLessThanOrEqual() {
		return quantityLessThanOrEqual;
	}

	public String getTableSuffix() {
		return tableSuffix;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public List<Long> getTemplateIds() {
		return templateIds;
	}

	public Long getTypeId() {
		return typeId;
	}

	public List<Long> getTypeIds() {
		return typeIds;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("name", "NAME_");
		addColumn("description", "DESCRIPTION_");
		addColumn("foodId", "FOODID_");
		addColumn("foodName", "FOODNAME_");
		addColumn("templateId", "TEMPLATEID_");
		addColumn("quantity", "QUANTITY_");
		addColumn("unit", "UNIT_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
	}

	public DietaryItemQuery name(String name) {
		if (name == null) {
			throw new RuntimeException("name is null");
		}
		this.name = name;
		return this;
	}

	public DietaryItemQuery nameLike(String nameLike) {
		if (nameLike == null) {
			throw new RuntimeException("name is null");
		}
		this.nameLike = nameLike;
		return this;
	}

	public DietaryItemQuery quantityGreaterThanOrEqual(Double quantityGreaterThanOrEqual) {
		if (quantityGreaterThanOrEqual == null) {
			throw new RuntimeException("quantity is null");
		}
		this.quantityGreaterThanOrEqual = quantityGreaterThanOrEqual;
		return this;
	}

	public DietaryItemQuery quantityLessThanOrEqual(Double quantityLessThanOrEqual) {
		if (quantityLessThanOrEqual == null) {
			throw new RuntimeException("quantity is null");
		}
		this.quantityLessThanOrEqual = quantityLessThanOrEqual;
		return this;
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setDescriptionLike(String descriptionLike) {
		this.descriptionLike = descriptionLike;
	}

	public void setDietaryId(Long dietaryId) {
		this.dietaryId = dietaryId;
	}

	public void setDietaryIds(List<Long> dietaryIds) {
		this.dietaryIds = dietaryIds;
	}

	public void setFoodId(Long foodId) {
		this.foodId = foodId;
	}

	public void setFoodIds(List<Long> foodIds) {
		this.foodIds = foodIds;
	}

	public void setFoodNameLike(String foodNameLike) {
		this.foodNameLike = foodNameLike;
	}

	public void setFullDay(Integer fullDay) {
		this.fullDay = fullDay;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNameLike(String nameLike) {
		this.nameLike = nameLike;
	}

	public void setQuantityGreaterThanOrEqual(Double quantityGreaterThanOrEqual) {
		this.quantityGreaterThanOrEqual = quantityGreaterThanOrEqual;
	}

	public void setQuantityLessThanOrEqual(Double quantityLessThanOrEqual) {
		this.quantityLessThanOrEqual = quantityLessThanOrEqual;
	}

	public void setTableSuffix(String tableSuffix) {
		this.tableSuffix = tableSuffix;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public void setTemplateIds(List<Long> templateIds) {
		this.templateIds = templateIds;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public void setTypeIds(List<Long> typeIds) {
		this.typeIds = typeIds;
	}

	public DietaryItemQuery tableSuffix(String tableSuffix) {
		if (tableSuffix == null) {
			throw new RuntimeException("tableSuffix is null");
		}
		this.tableSuffix = tableSuffix;
		return this;
	}

	public DietaryItemQuery templateId(Long templateId) {
		if (templateId == null) {
			throw new RuntimeException("templateId is null");
		}
		this.templateId = templateId;
		return this;
	}

	public DietaryItemQuery templateIds(List<Long> templateIds) {
		if (templateIds == null) {
			throw new RuntimeException("templateIds is empty ");
		}
		this.templateIds = templateIds;
		return this;
	}

	public DietaryItemQuery typeId(Long typeId) {
		if (typeId == null) {
			throw new RuntimeException("typeId is null");
		}
		this.typeId = typeId;
		return this;
	}

	public DietaryItemQuery typeIds(List<Long> typeIds) {
		if (typeIds == null) {
			throw new RuntimeException("typeIds is empty ");
		}
		this.typeIds = typeIds;
		return this;
	}

}