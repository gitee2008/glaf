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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.glaf.core.base.*;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.StringTools;
import com.glaf.matrix.transform.util.*;

/**
 * 
 * 实体对象
 *
 */

@Entity
@Table(name = "SYS_TABLE_TRANSFORM")
public class TableTransform implements java.lang.Comparable<TableTransform>, Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "TRANSFORMID_", length = 50, nullable = false)
	protected String transformId;
	
	@Column(name = "TABLENAME_", length = 100, nullable = false)
	protected String tableName;

	/**
	 * 标题
	 */
	@Column(name = "TITLE_", length = 200)
	protected String title;

	/**
	 * 数据库编号
	 */
	@Column(name = "DATABASEIDS_", length = 2000)
	protected String databaseIds;

	/**
	 * 主键列
	 */
	@Column(name = "PRIMARYKEY_", length = 50)
	protected String primaryKey;

	/**
	 * 目标表名
	 */
	@Column(name = "TARGETTABLENAME_", length = 50)
	protected String targetTableName;

	/**
	 * 转换列
	 */
	@Column(name = "TRANSFORMCOLUMNS_", length = 2000)
	protected String transformColumns;

	/**
	 * SQL条件
	 */
	@Column(name = "SQLCRITERIA_", length = 4000)
	protected String sqlCriteria;

	/**
	 * 顺序
	 */
	@Column(name = "SORT_")
	protected int sort;

	/**
	 * 当目标列不为空时是否转换
	 */
	@Column(name = "TRANSFORM_IF_FLAG_", length = 1)
	protected String transformIfTargetColumnNotEmpty;

	/**
	 * 转换标记
	 */
	@Column(name = "TRANSFORMFLAG_", length = 1)
	protected String transformFlag;

	/**
	 * 同步状态
	 */
	@Column(name = "TRANSFORMSTATUS_")
	protected int transformStatus;

	/**
	 * 最后同步时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TRANSFORMTIME_")
	protected Date transformTime;

	/**
	 * 当前用户关联标识
	 */
	@Column(name = "CURRENTUSERFLAG_", length = 1)
	protected String currentUserFlag;

	/**
	 * 删除标记
	 */
	@Column(name = "DELETEFLAG_")
	protected int deleteFlag;

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
	protected List<ColumnTransform> columns = new ArrayList<ColumnTransform>();

	public TableTransform() {

	}
	
	

	public int compareTo(TableTransform o) {
		if (o == null) {
			return -1;
		}

		TableTransform field = o;

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
		TableTransform other = (TableTransform) obj;
		if (tableName == null) {
			if (other.tableName != null)
				return false;
		} else if (!tableName.equals(other.tableName))
			return false;
		return true;
	}



	public List<ColumnTransform> getColumns() {
		return columns;
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

	public String getCurrentUserFlag() {
		return currentUserFlag;
	}

	public String getDatabaseIds() {
		return this.databaseIds;
	}

	public int getDeleteFlag() {
		return this.deleteFlag;
	}

	public int getLocked() {
		return this.locked;
	}

	public String getPrimaryKey() {
		return this.primaryKey;
	}

	public int getSort() {
		return this.sort;
	}

	public String getSqlCriteria() {
		if (StringUtils.isNotEmpty(sqlCriteria)) {
			sqlCriteria = StringTools.replace(sqlCriteria, "‘", "'");
			sqlCriteria = StringTools.replace(sqlCriteria, "’", "'");
		}
		return sqlCriteria;
	}

	public String getTableName() {
		return this.tableName;
	}

	public String getTargetTableName() {
		return targetTableName;
	}

	public String getTitle() {
		return this.title;
	}

	public String getTransformColumns() {
		if (transformColumns != null) {
			transformColumns = transformColumns.toLowerCase();
		}
		return transformColumns;
	}

	public String getTransformFlag() {
		return transformFlag;
	}

	public String getTransformId() {
		return transformId;
	}

	public String getTransformIfTargetColumnNotEmpty() {
		return transformIfTargetColumnNotEmpty;
	}

	public int getTransformStatus() {
		return this.transformStatus;
	}

	public Date getTransformTime() {
		return this.transformTime;
	}

	public String getTransformTimeString() {
		if (this.transformTime != null) {
			return DateUtils.getDateTime(this.transformTime);
		}
		return "";
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
		result = prime * result + ((tableName == null) ? 0 : tableName.hashCode());
		return result;
	}

	public TableTransform jsonToObject(JSONObject jsonObject) {
		return TableTransformJsonFactory.jsonToObject(jsonObject);
	}

	public void setColumns(List<ColumnTransform> columns) {
		this.columns = columns;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setCurrentUserFlag(String currentUserFlag) {
		this.currentUserFlag = currentUserFlag;
	}

	public void setDatabaseIds(String databaseIds) {
		this.databaseIds = databaseIds;
	}

	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public void setLocked(int locked) {
		this.locked = locked;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public void setSqlCriteria(String sqlCriteria) {
		this.sqlCriteria = sqlCriteria;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setTargetTableName(String targetTableName) {
		this.targetTableName = targetTableName;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTransformColumns(String transformColumns) {
		this.transformColumns = transformColumns;
	}

	public void setTransformFlag(String transformFlag) {
		this.transformFlag = transformFlag;
	}

	public void setTransformId(String transformId) {
		this.transformId = transformId;
	}

	public void setTransformIfTargetColumnNotEmpty(String transformIfTargetColumnNotEmpty) {
		this.transformIfTargetColumnNotEmpty = transformIfTargetColumnNotEmpty;
	}

	public void setTransformStatus(int transformStatus) {
		this.transformStatus = transformStatus;
	}

	public void setTransformTime(Date transformTime) {
		this.transformTime = transformTime;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public JSONObject toJsonObject() {
		return TableTransformJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return TableTransformJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
