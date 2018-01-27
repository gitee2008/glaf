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

package com.glaf.matrix.data.domain;

import java.io.*;
import java.util.*;
import javax.persistence.*;
import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.glaf.core.base.*;
import com.glaf.core.util.DateUtils;
import com.glaf.matrix.data.util.*;

/**
 * 
 * 实体对象
 *
 */

@Entity
@Table(name = "SYS_DATA_ITEM_DEF")
public class DataItemDefinition implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", nullable = false)
	protected Long id;

	/**
	 * 节点编号
	 */
	@Column(name = "NODEID_")
	protected Long nodeId;

	/**
	 * 分类
	 */
	@Column(name = "CATEGORY_", length = 50)
	protected String category;

	/**
	 * 名称
	 */
	@Column(name = "TITLE_", length = 200)
	protected String title;

	/**
	 * 编码
	 */
	@Column(name = "CODE_", length = 50)
	protected String code;

	/**
	 * 类型
	 */
	@Column(name = "TYPE_", length = 50)
	protected String type;

	/**
	 * 名称字段
	 */
	@Column(name = "KEYCOLUMN_", length = 100)
	protected String keyColumn;

	/**
	 * 值字段
	 */
	@Column(name = "VALUECOLUMN_", length = 100)
	protected String valueColumn;

	/**
	 * SQL
	 */
	@Column(name = "SQL_", length = 4000)
	protected String sql;

	/**
	 * 创建人
	 */
	@Column(name = "CREATEBY_", length = 50)
	protected String createBy;

	/**
	 * 创建时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME_")
	protected Date createTime;

	public DataItemDefinition() {

	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getNodeId() {
		return this.nodeId;
	}

	public String getCategory() {
		return this.category;
	}

	public String getTitle() {
		return this.title;
	}

	public String getCode() {
		return this.code;
	}

	public String getType() {
		return this.type;
	}

	public String getKeyColumn() {
		return this.keyColumn;
	}

	public String getValueColumn() {
		return this.valueColumn;
	}

	public String getSql() {
		return this.sql;
	}

	public String getCreateBy() {
		return this.createBy;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public String getCreateTimeString() {
		if (this.createTime != null) {
			return DateUtils.getDateTime(this.createTime);
		}
		return "";
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setKeyColumn(String keyColumn) {
		this.keyColumn = keyColumn;
	}

	public void setValueColumn(String valueColumn) {
		this.valueColumn = valueColumn;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataItemDefinition other = (DataItemDefinition) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public DataItemDefinition jsonToObject(JSONObject jsonObject) {
		return DataItemDefinitionJsonFactory.jsonToObject(jsonObject);
	}

	public JSONObject toJsonObject() {
		return DataItemDefinitionJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return DataItemDefinitionJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
