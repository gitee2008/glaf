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

package com.glaf.matrix.transform.domain;

import java.io.*;
import java.util.*;
import javax.persistence.*;
import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.glaf.core.base.*;
import com.glaf.core.util.DateUtils;
import com.glaf.matrix.transform.util.*;

/**
 * 
 * 实体对象
 *
 */

@Entity
@Table(name = "SYS_COLUMN_TRANSFORM")
public class ColumnTransform implements java.lang.Comparable<ColumnTransform>, Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", nullable = false)
	protected long id;

	@Column(name = "TRANSFORMID_", length = 50, nullable = false)
	protected String transformId;

	/**
	 * 名称
	 */
	@Column(name = "NAME_", length = 50)
	protected String name;

	/**
	 * 标题
	 */
	@Column(name = "TITLE_", length = 200)
	protected String title;

	/**
	 * 表名
	 */
	@Column(name = "TABLENAME_", length = 50)
	protected String tableName;

	/**
	 * 转换列
	 */
	@Column(name = "COLUMNNAME_", length = 50)
	protected String columnName;

	/**
	 * 转换后新列名
	 */
	@Column(name = "TARGETCOLUMNNAME_", length = 50)
	protected String targetColumnName;

	/**
	 * 目标列精度
	 */
	@Column(name = "TARGETCOLUMNPRECISION_")
	protected int targetColumnPrecision;

	/**
	 * 转换后新列类型
	 */
	@Column(name = "TARGETTYPE_", length = 50)
	protected String targetType;

	/**
	 * 转换条件
	 */
	@Column(name = "CONDITION_", length = 500)
	protected String condition;

	/**
	 * SQL条件
	 */
	@Column(name = "SQLCRITERIA_", length = 4000)
	protected String sqlCriteria;

	/**
	 * 转换表达式
	 */
	@Column(name = "EXPRESSION_", length = 500)
	protected String expression;

	/**
	 * 当目标列不为空时是否转换
	 */
	@Column(name = "TRANSFORM_IF_FLAG_", length = 1)
	protected String transformIfTargetColumnNotEmpty;

	/**
	 * 类型
	 */
	@Column(name = "TYPE_", length = 50)
	protected String type;

	/**
	 * 同步状态
	 */
	@Column(name = "SYNCSTATUS_")
	protected int syncStatus;

	/**
	 * 最后同步时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SYNCTIME_")
	protected Date syncTime;

	/**
	 * 顺序
	 */
	@Column(name = "SORT_")
	protected int sort;

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
	 * 创建时间
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
	 * 修改时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATETIME_")
	protected Date updateTime;

	@javax.persistence.Transient
	protected int position;

	public ColumnTransform() {

	}

	public int compareTo(ColumnTransform o) {
		if (o == null) {
			return -1;
		}

		ColumnTransform field = o;

		int l = this.sort - field.getSort();

		int ret = 0;

		if (l > 0) {
			ret = 1;
		} else if (l < 0) {
			ret = -1;
		}
		return ret;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ColumnTransform other = (ColumnTransform) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public String getColumnName() {
		return this.columnName;
	}

	public String getCondition() {
		return condition;
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

	public String getExpression() {
		return this.expression;
	}

	public long getId() {
		return this.id;
	}

	public int getLocked() {
		return this.locked;
	}

	public String getName() {
		return this.name;
	}

	public int getPosition() {
		return position;
	}

	public int getSort() {
		return this.sort;
	}

	public String getSqlCriteria() {
		return sqlCriteria;
	}

	public int getSyncStatus() {
		return this.syncStatus;
	}

	public Date getSyncTime() {
		return this.syncTime;
	}

	public String getSyncTimeString() {
		if (this.syncTime != null) {
			return DateUtils.getDateTime(this.syncTime);
		}
		return "";
	}

	public String getTableName() {
		return this.tableName;
	}

	public String getTargetColumnName() {
		if (targetColumnName != null) {
			targetColumnName = targetColumnName.trim().toUpperCase();
		}
		return this.targetColumnName;
	}

	public int getTargetColumnPrecision() {
		return targetColumnPrecision;
	}

	public String getTargetType() {
		return this.targetType;
	}

	public String getTitle() {
		return this.title;
	}

	public String getTransformId() {
		return transformId;
	}

	public String getTransformIfTargetColumnNotEmpty() {
		return transformIfTargetColumnNotEmpty;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public ColumnTransform jsonToObject(JSONObject jsonObject) {
		return ColumnTransformJsonFactory.jsonToObject(jsonObject);
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setLocked(int locked) {
		this.locked = locked;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public void setSqlCriteria(String sqlCriteria) {
		this.sqlCriteria = sqlCriteria;
	}

	public void setSyncStatus(int syncStatus) {
		this.syncStatus = syncStatus;
	}

	public void setSyncTime(Date syncTime) {
		this.syncTime = syncTime;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setTargetColumnName(String targetColumnName) {
		this.targetColumnName = targetColumnName;
	}

	public void setTargetColumnPrecision(int targetColumnPrecision) {
		this.targetColumnPrecision = targetColumnPrecision;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTransformId(String transformId) {
		this.transformId = transformId;
	}

	public void setTransformIfTargetColumnNotEmpty(String transformIfTargetColumnNotEmpty) {
		this.transformIfTargetColumnNotEmpty = transformIfTargetColumnNotEmpty;
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

	public JSONObject toJsonObject() {
		return ColumnTransformJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return ColumnTransformJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
