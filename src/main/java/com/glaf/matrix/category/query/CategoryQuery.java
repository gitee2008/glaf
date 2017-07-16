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

package com.glaf.matrix.category.query;

import java.util.*;
import com.glaf.core.query.DataQuery;

public class CategoryQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected Collection<Long> categoryIds;
	protected String name;
	protected String nameLike;
	protected String code;
	protected String codeLike;
	protected String discriminator;
	protected String discriminatorLike;
	protected Integer level;
	protected String titleLike;
	protected String type;
	protected String typeLike;
	protected String active;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;
	protected String updateBy;
	protected Date updateTimeGreaterThanOrEqual;
	protected Date updateTimeLessThanOrEqual;

	public CategoryQuery() {

	}

	public CategoryQuery active(String active) {
		if (active == null) {
			throw new RuntimeException("active is null");
		}
		this.active = active;
		return this;
	}

	public CategoryQuery code(String code) {
		if (code == null) {
			throw new RuntimeException("code is null");
		}
		this.code = code;
		return this;
	}

	public CategoryQuery codeLike(String codeLike) {
		if (codeLike == null) {
			throw new RuntimeException("code is null");
		}
		this.codeLike = codeLike;
		return this;
	}

	public CategoryQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public CategoryQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public CategoryQuery discriminator(String discriminator) {
		if (discriminator == null) {
			throw new RuntimeException("discriminator is null");
		}
		this.discriminator = discriminator;
		return this;
	}

	public CategoryQuery discriminatorLike(String discriminatorLike) {
		if (discriminatorLike == null) {
			throw new RuntimeException("discriminator is null");
		}
		this.discriminatorLike = discriminatorLike;
		return this;
	}

	public String getActive() {
		return active;
	}

	public String getCode() {
		return code;
	}

	public String getCodeLike() {
		if (codeLike != null && codeLike.trim().length() > 0) {
			if (!codeLike.startsWith("%")) {
				codeLike = "%" + codeLike;
			}
			if (!codeLike.endsWith("%")) {
				codeLike = codeLike + "%";
			}
		}
		return codeLike;
	}

	public Date getCreateTimeGreaterThanOrEqual() {
		return createTimeGreaterThanOrEqual;
	}

	public Date getCreateTimeLessThanOrEqual() {
		return createTimeLessThanOrEqual;
	}

	public String getDiscriminator() {
		return discriminator;
	}

	public String getDiscriminatorLike() {
		if (discriminatorLike != null && discriminatorLike.trim().length() > 0) {
			if (!discriminatorLike.startsWith("%")) {
				discriminatorLike = "%" + discriminatorLike;
			}
			if (!discriminatorLike.endsWith("%")) {
				discriminatorLike = discriminatorLike + "%";
			}
		}
		return discriminatorLike;
	}

	public Integer getLevel() {
		return level;
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

			if ("parentId".equals(sortColumn)) {
				orderBy = "E.PARENTID_" + a_x;
			}

			if ("name".equals(sortColumn)) {
				orderBy = "E.NAME_" + a_x;
			}

			if ("code".equals(sortColumn)) {
				orderBy = "E.CODE_" + a_x;
			}

			if ("discriminator".equals(sortColumn)) {
				orderBy = "E.DISCRIMINATOR_" + a_x;
			}

			if ("icon".equals(sortColumn)) {
				orderBy = "E.ICON_" + a_x;
			}

			if ("iconCls".equals(sortColumn)) {
				orderBy = "E.ICONCLS_" + a_x;
			}

			if ("level".equals(sortColumn)) {
				orderBy = "E.LEVEL_" + a_x;
			}

			if ("treeId".equals(sortColumn)) {
				orderBy = "E.TREEID_" + a_x;
			}

			if ("title".equals(sortColumn)) {
				orderBy = "E.TITLE_" + a_x;
			}

			if ("type".equals(sortColumn)) {
				orderBy = "E.TYPE_" + a_x;
			}

			if ("sort".equals(sortColumn)) {
				orderBy = "E.SORTNO_" + a_x;
			}

			if ("subIds".equals(sortColumn)) {
				orderBy = "E.SUBIDS_" + a_x;
			}

			if ("active".equals(sortColumn)) {
				orderBy = "E.ACTIVE_" + a_x;
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

	public Long getParentId() {
		return parentId;
	}

	public List<Long> getParentIds() {
		return parentIds;
	}

	public Collection<Long> getCategoryIds() {
		return categoryIds;
	}

	public String getTitleLike() {
		if (titleLike != null && titleLike.trim().length() > 0) {
			if (!titleLike.startsWith("%")) {
				titleLike = "%" + titleLike;
			}
			if (!titleLike.endsWith("%")) {
				titleLike = titleLike + "%";
			}
		}
		return titleLike;
	}

	public String getTreeId() {
		return treeId;
	}

	public String getTreeIdLike() {
		if (treeIdLike != null && treeIdLike.trim().length() > 0) {
			if (!treeIdLike.endsWith("%")) {
				treeIdLike = treeIdLike + "%";
			}
		}
		return treeIdLike;
	}

	public String getType() {
		return type;
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

	public String getUpdateBy() {
		return updateBy;
	}

	public Date getUpdateTimeGreaterThanOrEqual() {
		return updateTimeGreaterThanOrEqual;
	}

	public Date getUpdateTimeLessThanOrEqual() {
		return updateTimeLessThanOrEqual;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("parentId", "PARENTID_");
		addColumn("name", "NAME_");
		addColumn("code", "CODE_");
		addColumn("discriminator", "DISCRIMINATOR_");
		addColumn("icon", "ICON_");
		addColumn("iconCls", "ICONCLS_");
		addColumn("level", "LEVEL_");
		addColumn("treeId", "TREEID_");
		addColumn("title", "TITLE_");
		addColumn("type", "TYPE_");
		addColumn("sort", "SORTNO_");
		addColumn("subIds", "SUBIDS_");
		addColumn("active", "ACTIVE_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
		addColumn("updateBy", "UPDATEBY_");
		addColumn("updateTime", "UPDATETIME_");
	}

	public CategoryQuery level(Integer level) {
		if (level == null) {
			throw new RuntimeException("level is null");
		}
		this.level = level;
		return this;
	}

	public CategoryQuery name(String name) {
		if (name == null) {
			throw new RuntimeException("name is null");
		}
		this.name = name;
		return this;
	}

	public CategoryQuery nameLike(String nameLike) {
		if (nameLike == null) {
			throw new RuntimeException("name is null");
		}
		this.nameLike = nameLike;
		return this;
	}

	public CategoryQuery parentId(Long parentId) {
		if (parentId == null) {
			throw new RuntimeException("parentId is null");
		}
		this.parentId = parentId;
		return this;
	}

	public CategoryQuery parentIds(List<Long> parentIds) {
		if (parentIds == null) {
			throw new RuntimeException("parentIds is empty ");
		}
		this.parentIds = parentIds;
		return this;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setCodeLike(String codeLike) {
		this.codeLike = codeLike;
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setDiscriminator(String discriminator) {
		this.discriminator = discriminator;
	}

	public void setDiscriminatorLike(String discriminatorLike) {
		this.discriminatorLike = discriminatorLike;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNameLike(String nameLike) {
		this.nameLike = nameLike;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public void setParentIds(List<Long> parentIds) {
		this.parentIds = parentIds;
	}

	public void setCategoryIds(Collection<Long> categoryIds) {
		this.categoryIds = categoryIds;
	}

	public void setTitleLike(String titleLike) {
		this.titleLike = titleLike;
	}

	public void setTreeId(String treeId) {
		this.treeId = treeId;
	}

	public void setTreeIdLike(String treeIdLike) {
		this.treeIdLike = treeIdLike;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setTypeLike(String typeLike) {
		this.typeLike = typeLike;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public void setUpdateTimeGreaterThanOrEqual(Date updateTimeGreaterThanOrEqual) {
		this.updateTimeGreaterThanOrEqual = updateTimeGreaterThanOrEqual;
	}

	public void setUpdateTimeLessThanOrEqual(Date updateTimeLessThanOrEqual) {
		this.updateTimeLessThanOrEqual = updateTimeLessThanOrEqual;
	}

	public CategoryQuery titleLike(String titleLike) {
		if (titleLike == null) {
			throw new RuntimeException("title is null");
		}
		this.titleLike = titleLike;
		return this;
	}

	public CategoryQuery treeId(String treeId) {
		if (treeId == null) {
			throw new RuntimeException("treeId is null");
		}
		this.treeId = treeId;
		return this;
	}

	public CategoryQuery treeIdLike(String treeIdLike) {
		if (treeIdLike == null) {
			throw new RuntimeException("treeId is null");
		}
		this.treeIdLike = treeIdLike;
		return this;
	}

	public CategoryQuery type(String type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		this.type = type;
		return this;
	}

	public CategoryQuery typeLike(String typeLike) {
		if (typeLike == null) {
			throw new RuntimeException("type is null");
		}
		this.typeLike = typeLike;
		return this;
	}

	public CategoryQuery updateBy(String updateBy) {
		if (updateBy == null) {
			throw new RuntimeException("updateBy is null");
		}
		this.updateBy = updateBy;
		return this;
	}

	public CategoryQuery updateTimeGreaterThanOrEqual(Date updateTimeGreaterThanOrEqual) {
		if (updateTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("updateTime is null");
		}
		this.updateTimeGreaterThanOrEqual = updateTimeGreaterThanOrEqual;
		return this;
	}

	public CategoryQuery updateTimeLessThanOrEqual(Date updateTimeLessThanOrEqual) {
		if (updateTimeLessThanOrEqual == null) {
			throw new RuntimeException("updateTime is null");
		}
		this.updateTimeLessThanOrEqual = updateTimeLessThanOrEqual;
		return this;
	}

}