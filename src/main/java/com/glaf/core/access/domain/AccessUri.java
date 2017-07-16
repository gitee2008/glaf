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

package com.glaf.core.access.domain;

import java.io.*;

import javax.persistence.*;
import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.glaf.core.base.*;
import com.glaf.core.access.util.*;

/**
 * 
 * 实体对象
 *
 */

@Entity
@Table(name = "SYS_ACCESS_URI")
public class AccessUri implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", nullable = false)
	protected long id;

	/**
	 * URI
	 */
	@Column(name = "URI_", length = 500)
	protected String uri;

	/**
	 * 限制次数
	 */
	@Column(name = "LIMIT_")
	protected int limit;

	/**
	 * 访问次数
	 */
	@Column(name = "TOTAL_")
	protected int total;

	/**
	 * 标题
	 */
	@Column(name = "TITLE_", length = 200)
	protected String title;

	/**
	 * 描述
	 */
	@Column(name = "DESCRIPTION_", length = 500)
	protected String description;

	public AccessUri() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccessUri other = (AccessUri) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public String getDescription() {
		return this.description;
	}

	public long getId() {
		return this.id;
	}

	public int getLimit() {
		return this.limit;
	}

	public String getTitle() {
		return this.title;
	}

	public int getTotal() {
		return this.total;
	}

	public String getUri() {
		return this.uri;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public AccessUri jsonToObject(JSONObject jsonObject) {
		return AccessUriJsonFactory.jsonToObject(jsonObject);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public JSONObject toJsonObject() {
		return AccessUriJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return AccessUriJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
