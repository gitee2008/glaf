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
@Table(name = "SYS_ROW_DENORMALISER")
public class RowDenormaliser implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", length = 64, nullable = false)
	protected String id;

	/**
	 * 标题
	 */
	@Column(name = "TITLE_", length = 200)
	protected String title;

	/**
	 * 转换数据库编号
	 */
	@Column(name = "DATABASEIDS_", length = 2000)
	protected String databaseIds;

	/**
	 * 来源表名
	 */
	@Column(name = "SOURCETABLENAME_", length = 50)
	protected String sourceTableName;

	/**
	 * 聚合列
	 */
	@Column(name = "AGGREGATECOLUMNS_", length = 500)
	protected String aggregateColumns;

	/**
	 * 主键列
	 */
	@Column(name = "PRIMARYKEY_", length = 50)
	protected String primaryKey;

	/**
	 * 转换列
	 */
	@Column(name = "TRANSFORMCOLUMN_", length = 50)
	protected String transformColumn;

	/**
	 * 同步列
	 */
	@Column(name = "SYNCCOLUMNS_", length = 4000)
	protected String syncColumns;

	/**
	 * 日期维度列
	 */
	@Column(name = "DATEDIMENSIONCOLUMN_", length = 50)
	protected String dateDimensionColumn;

	/**
	 * 增量列
	 */
	@Column(name = "INCREMENTCOLUMN_", length = 50)
	protected String incrementColumn;

	/**
	 * 分隔符
	 */
	@Column(name = "DELIMITER_", length = 50)
	protected String delimiter;

	@Column(name = "SQLCRITERIA_", length = 4000)
	protected String sqlCriteria;
	/**
	 * 顺序
	 */
	@Column(name = "SORT_")
	protected int sort;

	/**
	 * 转换状态
	 */
	@Column(name = "TRANSFORMSTATUS_")
	protected int transformStatus;

	/**
	 * 最后转换时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TRANSFORMTIME_")
	protected Date transformTime;

	/**
	 * 转换标记
	 */
	@Column(name = "TRANSFORMFLAG_", length = 1)
	protected String transformFlag;

	/**
	 * 目标表名
	 */
	@Column(name = "TARGETTABLENAME_", length = 50)
	protected String targetTableName;

	/**
	 * 目标列
	 */
	@Column(name = "TARGETCOLUMN_", length = 50)
	protected String targetColumn;

	/**
	 * 目标列类型
	 */
	@Column(name = "TARGETCOLUMNTYPE_", length = 50)
	protected String targetColumnType;

	/**
	 * 是否调度执行
	 */
	@Column(name = "SCHEDULEFLAG_", length = 1)
	protected String scheduleFlag;

	/**
	 * 每次抓取前删除
	 */
	@Column(name = "DELETEFETCH_", length = 1)
	protected String deleteFetch;

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

	public RowDenormaliser() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RowDenormaliser other = (RowDenormaliser) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getAggregateColumns() {
		if (aggregateColumns != null) {
			aggregateColumns = aggregateColumns.trim().toLowerCase();
		}
		return aggregateColumns;
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

	public String getDatabaseIds() {
		return this.databaseIds;
	}

	public String getDateDimensionColumn() {
		if (dateDimensionColumn != null) {
			dateDimensionColumn = dateDimensionColumn.trim().toLowerCase();
		}
		return dateDimensionColumn;
	}

	public String getDeleteFetch() {
		return deleteFetch;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public String getId() {
		return this.id;
	}

	public String getIncrementColumn() {
		return incrementColumn;
	}

	public int getLocked() {
		return this.locked;
	}

	public String getPrimaryKey() {
		if (primaryKey != null) {
			primaryKey = primaryKey.trim().toLowerCase();
		}
		return this.primaryKey;
	}

	public String getScheduleFlag() {
		return scheduleFlag;
	}

	public int getSort() {
		return this.sort;
	}

	public String getSourceTableName() {
		return sourceTableName;
	}

	public String getSqlCriteria() {
		return sqlCriteria;
	}

	public String getSyncColumns() {
		if (syncColumns != null) {
			syncColumns = syncColumns.trim().toLowerCase();
		}
		return syncColumns;
	}

	public String getTargetColumn() {
		if (targetColumn != null) {
			targetColumn = targetColumn.trim().toLowerCase();
		}
		return this.targetColumn;
	}

	public String getTargetColumnType() {
		return targetColumnType;
	}

	public String getTargetTableName() {
		return this.targetTableName;
	}

	public String getTitle() {
		return this.title;
	}

	public String getTransformColumn() {
		if (transformColumn != null) {
			transformColumn = transformColumn.trim().toLowerCase();
		}
		return this.transformColumn;
	}

	public String getTransformFlag() {
		return this.transformFlag;
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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public RowDenormaliser jsonToObject(JSONObject jsonObject) {
		return RowDenormaliserJsonFactory.jsonToObject(jsonObject);
	}

	public void setAggregateColumns(String aggregateColumns) {
		this.aggregateColumns = aggregateColumns;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setDatabaseIds(String databaseIds) {
		this.databaseIds = databaseIds;
	}

	public void setDateDimensionColumn(String dateDimensionColumn) {
		this.dateDimensionColumn = dateDimensionColumn;
	}

	public void setDeleteFetch(String deleteFetch) {
		this.deleteFetch = deleteFetch;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setIncrementColumn(String incrementColumn) {
		this.incrementColumn = incrementColumn;
	}

	public void setLocked(int locked) {
		this.locked = locked;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public void setScheduleFlag(String scheduleFlag) {
		this.scheduleFlag = scheduleFlag;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public void setSourceTableName(String sourceTableName) {
		this.sourceTableName = sourceTableName;
	}

	public void setSqlCriteria(String sqlCriteria) {
		this.sqlCriteria = sqlCriteria;
	}

	public void setSyncColumns(String syncColumns) {
		this.syncColumns = syncColumns;
	}

	public void setTargetColumn(String targetColumn) {
		this.targetColumn = targetColumn;
	}

	public void setTargetColumnType(String targetColumnType) {
		this.targetColumnType = targetColumnType;
	}

	public void setTargetTableName(String targetTableName) {
		this.targetTableName = targetTableName;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTransformColumn(String transformColumn) {
		this.transformColumn = transformColumn;
	}

	public void setTransformFlag(String transformFlag) {
		this.transformFlag = transformFlag;
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
		return RowDenormaliserJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return RowDenormaliserJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
