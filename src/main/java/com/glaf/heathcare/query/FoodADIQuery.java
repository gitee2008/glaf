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

public class FoodADIQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected Long nodeId;
	protected Collection<String> appActorIds;
	protected String name;
	protected String nameLike;
	protected String descriptionLike;
	protected Double lowestGreaterThanOrEqual;
	protected Double lowestLessThanOrEqual;
	protected Double averageGreaterThanOrEqual;
	protected Double averageLessThanOrEqual;
	protected Double highestGreaterThanOrEqual;
	protected Double highestLessThanOrEqual;
	protected String ageGroup;
	protected String type;
	protected String typeLike;
	protected Long typeId;
	protected List<Long> typeIds;
	protected String enableFlag;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	public FoodADIQuery() {

	}

	public FoodADIQuery ageGroup(String ageGroup) {
		if (ageGroup == null) {
			throw new RuntimeException("ageGroup is null");
		}
		this.ageGroup = ageGroup;
		return this;
	}

	public FoodADIQuery averageGreaterThanOrEqual(Double averageGreaterThanOrEqual) {
		if (averageGreaterThanOrEqual == null) {
			throw new RuntimeException("average is null");
		}
		this.averageGreaterThanOrEqual = averageGreaterThanOrEqual;
		return this;
	}

	public FoodADIQuery averageLessThanOrEqual(Double averageLessThanOrEqual) {
		if (averageLessThanOrEqual == null) {
			throw new RuntimeException("average is null");
		}
		this.averageLessThanOrEqual = averageLessThanOrEqual;
		return this;
	}

	public FoodADIQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public FoodADIQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public FoodADIQuery descriptionLike(String descriptionLike) {
		if (descriptionLike == null) {
			throw new RuntimeException("description is null");
		}
		this.descriptionLike = descriptionLike;
		return this;
	}

	public FoodADIQuery enableFlag(String enableFlag) {
		if (enableFlag == null) {
			throw new RuntimeException("enableFlag is null");
		}
		this.enableFlag = enableFlag;
		return this;
	}

	public String getAgeGroup() {
		return ageGroup;
	}

	public Collection<String> getAppActorIds() {
		return appActorIds;
	}

	public Double getAverageGreaterThanOrEqual() {
		return averageGreaterThanOrEqual;
	}

	public Double getAverageLessThanOrEqual() {
		return averageLessThanOrEqual;
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

	public String getEnableFlag() {
		return enableFlag;
	}

	public Double getHighestGreaterThanOrEqual() {
		return highestGreaterThanOrEqual;
	}

	public Double getHighestLessThanOrEqual() {
		return highestLessThanOrEqual;
	}

	public Double getLowestGreaterThanOrEqual() {
		return lowestGreaterThanOrEqual;
	}

	public Double getLowestLessThanOrEqual() {
		return lowestLessThanOrEqual;
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

	public Long getNodeId() {
		return nodeId;
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

			if ("lowest".equals(sortColumn)) {
				orderBy = "E.LOWEST_" + a_x;
			}

			if ("average".equals(sortColumn)) {
				orderBy = "E.AVERAGE_" + a_x;
			}

			if ("highest".equals(sortColumn)) {
				orderBy = "E.HIGHEST_" + a_x;
			}

			if ("ageGroup".equals(sortColumn)) {
				orderBy = "E.AGEGROUP_" + a_x;
			}

			if ("type".equals(sortColumn)) {
				orderBy = "E.TYPE_" + a_x;
			}

			if ("typeId".equals(sortColumn)) {
				orderBy = "E.TYPEID_" + a_x;
			}

			if ("sortNo".equals(sortColumn)) {
				orderBy = "E.SORTNO_" + a_x;
			}

			if ("enableFlag".equals(sortColumn)) {
				orderBy = "E.ENABLEFLAG_" + a_x;
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

	public String getType() {
		return type;
	}

	public Long getTypeId() {
		return typeId;
	}

	public List<Long> getTypeIds() {
		return typeIds;
	}

	public String getTypeLike() {
		if (typeLike != null && typeLike.trim().length() > 0) {
			if (!typeLike.startsWith("%")) {
				typeLike = "%" + typeLike;
			}
			if (!typeLike.endsWith("%")) {
				typeLike = typeLike + "%";
			}
		}
		return typeLike;
	}

	public FoodADIQuery highestGreaterThanOrEqual(Double highestGreaterThanOrEqual) {
		if (highestGreaterThanOrEqual == null) {
			throw new RuntimeException("highest is null");
		}
		this.highestGreaterThanOrEqual = highestGreaterThanOrEqual;
		return this;
	}

	public FoodADIQuery highestLessThanOrEqual(Double highestLessThanOrEqual) {
		if (highestLessThanOrEqual == null) {
			throw new RuntimeException("highest is null");
		}
		this.highestLessThanOrEqual = highestLessThanOrEqual;
		return this;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("name", "NAME_");
		addColumn("description", "DESCRIPTION_");
		addColumn("lowest", "LOWEST_");
		addColumn("average", "AVERAGE_");
		addColumn("highest", "HIGHEST_");
		addColumn("ageGroup", "AGEGROUP_");
		addColumn("type", "TYPE_");
		addColumn("typeId", "TYPEID_");
		addColumn("sortNo", "SORTNO_");
		addColumn("enableFlag", "ENABLEFLAG_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
		addColumn("updateBy", "UPDATEBY_");
		addColumn("updateTime", "UPDATETIME_");
	}

	public FoodADIQuery lowestGreaterThanOrEqual(Double lowestGreaterThanOrEqual) {
		if (lowestGreaterThanOrEqual == null) {
			throw new RuntimeException("lowest is null");
		}
		this.lowestGreaterThanOrEqual = lowestGreaterThanOrEqual;
		return this;
	}

	public FoodADIQuery lowestLessThanOrEqual(Double lowestLessThanOrEqual) {
		if (lowestLessThanOrEqual == null) {
			throw new RuntimeException("lowest is null");
		}
		this.lowestLessThanOrEqual = lowestLessThanOrEqual;
		return this;
	}

	public FoodADIQuery name(String name) {
		if (name == null) {
			throw new RuntimeException("name is null");
		}
		this.name = name;
		return this;
	}

	public FoodADIQuery nameLike(String nameLike) {
		if (nameLike == null) {
			throw new RuntimeException("name is null");
		}
		this.nameLike = nameLike;
		return this;
	}

	public FoodADIQuery nodeId(Long nodeId) {
		if (nodeId == null) {
			throw new RuntimeException("nodeId is null");
		}
		this.nodeId = nodeId;
		return this;
	}

	public void setAgeGroup(String ageGroup) {
		this.ageGroup = ageGroup;
	}

	public void setAppActorIds(Collection<String> appActorIds) {
		this.appActorIds = appActorIds;
	}

	public void setAverageGreaterThanOrEqual(Double averageGreaterThanOrEqual) {
		this.averageGreaterThanOrEqual = averageGreaterThanOrEqual;
	}

	public void setAverageLessThanOrEqual(Double averageLessThanOrEqual) {
		this.averageLessThanOrEqual = averageLessThanOrEqual;
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

	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
	}

	public void setHighestGreaterThanOrEqual(Double highestGreaterThanOrEqual) {
		this.highestGreaterThanOrEqual = highestGreaterThanOrEqual;
	}

	public void setHighestLessThanOrEqual(Double highestLessThanOrEqual) {
		this.highestLessThanOrEqual = highestLessThanOrEqual;
	}

	public void setLowestGreaterThanOrEqual(Double lowestGreaterThanOrEqual) {
		this.lowestGreaterThanOrEqual = lowestGreaterThanOrEqual;
	}

	public void setLowestLessThanOrEqual(Double lowestLessThanOrEqual) {
		this.lowestLessThanOrEqual = lowestLessThanOrEqual;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNameLike(String nameLike) {
		this.nameLike = nameLike;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public void setTypeIds(List<Long> typeIds) {
		this.typeIds = typeIds;
	}

	public void setTypeLike(String typeLike) {
		this.typeLike = typeLike;
	}

	public FoodADIQuery type(String type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		this.type = type;
		return this;
	}

	public FoodADIQuery typeId(Long typeId) {
		if (typeId == null) {
			throw new RuntimeException("typeId is null");
		}
		this.typeId = typeId;
		return this;
	}

	public FoodADIQuery typeIds(List<Long> typeIds) {
		if (typeIds == null) {
			throw new RuntimeException("typeIds is empty ");
		}
		this.typeIds = typeIds;
		return this;
	}

	public FoodADIQuery typeLike(String typeLike) {
		if (typeLike == null) {
			throw new RuntimeException("type is null");
		}
		this.typeLike = typeLike;
		return this;
	}

}