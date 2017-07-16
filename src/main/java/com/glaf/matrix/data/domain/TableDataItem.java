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

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.core.base.JSONable;
import com.glaf.core.util.DateUtils;
import com.glaf.matrix.data.util.TableDataItemJsonFactory;

/**
 * 
 * 实体对象
 *
 */

@Entity
@Table(name = "SYS_TABLE_DATA_ITEM")
public class TableDataItem implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", length = 50, nullable = false)
	protected String id;

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

	/**
	 * 表名称
	 */
	@Column(name = "TABLENAME_", length = 50)
	protected String tableName;

	/**
	 * 标题列名
	 */
	@Column(name = "NAMECOLUMN_", length = 50)
	protected String nameColumn;

	/**
	 * 值列名
	 */
	@Column(name = "VALUECOLUMN_", length = 50)
	protected String valueColumn;

	/**
	 * 排序列名
	 */
	@Column(name = "SORTCOLUMN_", length = 50)
	protected String sortColumn;

	/**
	 * 过滤标识
	 */
	@Column(name = "FILTERFLAG_", length = 50)
	protected String filterFlag;

	@Column(name = "SQLCRITERIA_", length = 4000)
	protected String sqlCriteria;

	/**
	 * 类型
	 */
	@Column(name = "TYPE_", length = 50)
	protected String type;

	/**
	 * 是否锁定
	 */
	@Column(name = "LOCKED_")
	protected int locked;

	/**
	 * 创建人
	 */
	@Column(name = "CREATEBY_", length = 50)
	protected String createBy;

	/**
	 * 创建日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME_")
	protected Date createTime;

	/**
	 * 修改人
	 */
	@Column(name = "UPDATEBY_", length = 50)
	protected String updateBy;

	/**
	 * 修改日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATETIME_")
	protected Date updateTime;

	public TableDataItem() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TableDataItem other = (TableDataItem) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
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

	public String getDescription() {
		return this.description;
	}

	public String getFilterFlag() {
		return this.filterFlag;
	}

	public String getId() {
		return this.id;
	}

	public int getLocked() {
		return locked;
	}

	public String getNameColumn() {
		return this.nameColumn;
	}

	public String getSortColumn() {
		return sortColumn;
	}

	public String getSqlCriteria() {
		return sqlCriteria;
	}

	public String getTableName() {
		return this.tableName;
	}

	public String getTitle() {
		return this.title;
	}

	public String getType() {
		return this.type;
	}

	public String getUpdateBy() {
		return this.updateBy;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public String getUpdateTimeString() {
		if (this.updateTime != null) {
			return DateUtils.getDateTime(this.updateTime);
		}
		return "";
	}

	public String getValueColumn() {
		return this.valueColumn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public TableDataItem jsonToObject(JSONObject jsonObject) {
		return TableDataItemJsonFactory.jsonToObject(jsonObject);
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setFilterFlag(String filterFlag) {
		this.filterFlag = filterFlag;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLocked(int locked) {
		this.locked = locked;
	}

	public void setNameColumn(String nameColumn) {
		this.nameColumn = nameColumn;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public void setSqlCriteria(String sqlCriteria) {
		this.sqlCriteria = sqlCriteria;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public void setValueColumn(String valueColumn) {
		this.valueColumn = valueColumn;
	}

	public JSONObject toJsonObject() {
		return TableDataItemJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return TableDataItemJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
