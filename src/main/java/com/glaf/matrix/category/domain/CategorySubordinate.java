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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * 实体对象
 *
 */

@Entity
@Table(name = "SYS_CATEGORY_SUBORDINATE")
public class CategorySubordinate implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", nullable = false)
	protected long id;

	@Column(name = "PROJECTID_")
	protected long categoryId;

	@Column(name = "SUBORDINATEID_")
	protected long subordinateId;

	public CategorySubordinate() {

	}

	public long getCategoryId() {
		return categoryId;
	}

	public long getId() {
		return id;
	}

	public long getSubordinateId() {
		return subordinateId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setSubordinateId(long subordinateId) {
		this.subordinateId = subordinateId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
