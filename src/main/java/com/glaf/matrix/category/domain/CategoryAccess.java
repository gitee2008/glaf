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
package com.glaf.matrix.category.domain;

import java.io.*;

import javax.persistence.*;

import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.glaf.core.base.*;
import com.glaf.matrix.category.util.CategoryAccessJsonFactory;

/**
 * 
 * 分类权限表
 *
 */
@Entity
@Table(name = "SYS_CATEGORY_ACCESS")
public class CategoryAccess implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", nullable = false)
	protected long id;

	@Column(name = "PROJECTID_")
	protected long categoryId;

	@Column(name = "ACTORID_", length = 50)
	protected String actorId;

	@Column(name = "DYNAMIC_", length = 1)
	protected String dynamic;

	public CategoryAccess() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CategoryAccess other = (CategoryAccess) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public String getActorId() {
		return this.actorId;
	}

	public String getDynamic() {
		return dynamic;
	}

	public long getId() {
		return this.id;
	}

	public long getCategoryId() {
		return this.categoryId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public CategoryAccess jsonToObject(JSONObject jsonObject) {
		return CategoryAccessJsonFactory.jsonToObject(jsonObject);
	}

	public void setActorId(String actorId) {
		this.actorId = actorId;
	}

	public void setDynamic(String dynamic) {
		this.dynamic = dynamic;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public JSONObject toJsonObject() {
		return CategoryAccessJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return CategoryAccessJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
