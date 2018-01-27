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
import com.glaf.core.domain.ColumnDefinition;

import com.glaf.core.util.DateUtils;
import com.glaf.matrix.data.util.*;
import com.glaf.matrix.data.domain.SqlCriteria;

/**
 * 
 * 实体对象
 *
 */

@Entity
@Table(name = "SYS_SQL")
public class SqlDefinition implements java.lang.Comparable<SqlDefinition>, Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", nullable = false)
	protected long id;

	@Column(name = "PARENTID_")
	protected long parentId;

	@Column(name = "UUID_", length = 50)
	protected String uuid;

	@Column(name = "NAME_", length = 50)
	protected String name;

	@Column(name = "CODE_", length = 50)
	protected String code;

	@Column(name = "TITLE_", length = 200)
	protected String title;

	@Lob
	@Column(name = "SQL_")
	protected String sql;

	@Lob
	@Column(name = "COUNTSQL_")
	protected String countSql;

	@Column(name = "TYPE_", length = 50)
	protected String type;

	@Column(name = "OPERATION_", length = 50)
	protected String operation;

	@Column(name = "ROWKEY_", length = 500)
	protected String rowKey;

	@Column(name = "SCHEDULEFLAG_", length = 1)
	protected String scheduleFlag;

	@Column(name = "CACHEFLAG_", length = 1)
	protected String cacheFlag;

	/**
	 * 是否数据项
	 */
	@Column(name = "DATAITEMFLAG_", length = 1)
	protected String dataItemFlag;

	@Column(name = "FETCHFLAG_", length = 1)
	protected String fetchFlag;

	@Column(name = "DELETEFETCH_", length = 1)
	protected String deleteFetch;

	/**
	 * 是否导出
	 */
	@Column(name = "EXPORTFLAG_", length = 1)
	protected String exportFlag;

	/**
	 * 导出表名
	 */
	@Column(name = "EXPORTTABLENAME_", length = 50)
	protected String exportTableName;

	/**
	 * 导出模板
	 */
	@Column(name = "EXPORTTEMPLATE_", length = 250)
	protected String exportTemplate;

	@Column(name = "TARGETTABLENAME_", length = 50)
	protected String targetTableName;

	@Column(name = "SHAREFLAG_", length = 1)
	protected String shareFlag;

	@Column(name = "PUBLICFLAG_", length = 1)
	protected String publicFlag;

	@Column(name = "SAVEFLAG_", length = 1)
	protected String saveFlag;

	/**
	 * 聚合标记
	 */
	@Column(name = "AGGREGATIONFLAG_", length = 1)
	protected String aggregationFlag;

	/**
	 * 顺序
	 */
	@Column(name = "ORDINAL_")
	protected int ordinal;

	@Column(name = "LOCKED_")
	protected int locked;

	@Column(name = "CREATEBY_", length = 50)
	protected String createBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME_")
	protected Date createTime;

	@Column(name = "UPDATEBY_", length = 50)
	protected String updateBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATETIME_")
	protected Date updateTime;

	@javax.persistence.Transient
	protected int width = 880;

	@javax.persistence.Transient
	protected List<ColumnDefinition> columns = new ArrayList<ColumnDefinition>();

	@javax.persistence.Transient
	protected List<SqlCriteria> parameters = new ArrayList<SqlCriteria>();

	public SqlDefinition() {

	}

	public void addColumn(ColumnDefinition column) {
		if (columns == null) {
			columns = new ArrayList<ColumnDefinition>();
		}
		columns.add(column);
	}

	public void addParameter(SqlCriteria c) {
		if (parameters == null) {
			parameters = new ArrayList<SqlCriteria>();
		}
		parameters.add(c);
	}

	public int compareTo(SqlDefinition o) {
		if (o == null) {
			return -1;
		}

		SqlDefinition field = o;

		int l = this.ordinal - field.getOrdinal();

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
		SqlDefinition other = (SqlDefinition) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public String getAggregationFlag() {
		return aggregationFlag;
	}

	public String getCacheFlag() {
		return this.cacheFlag;
	}

	public String getCode() {
		return code;
	}

	public List<ColumnDefinition> getColumns() {
		return columns;
	}

	public String getCountSql() {
		return this.countSql;
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

	public String getDataItemFlag() {
		return dataItemFlag;
	}

	public String getDeleteFetch() {
		return deleteFetch;
	}

	public String getExportFlag() {
		return exportFlag;
	}

	public String getExportTableName() {
		return exportTableName;
	}

	public String getExportTemplate() {
		return exportTemplate;
	}

	public String getFetchFlag() {
		return fetchFlag;
	}

	public long getId() {
		return this.id;
	}

	public int getLocked() {
		return locked;
	}

	public String getName() {
		return this.name;
	}

	public String getOperation() {
		return this.operation;
	}

	public int getOrdinal() {
		return ordinal;
	}

	public List<SqlCriteria> getParameters() {
		return parameters;
	}

	public long getParentId() {
		return this.parentId;
	}

	public String getPublicFlag() {
		return publicFlag;
	}

	public String getRowKey() {
		return this.rowKey;
	}

	public String getSaveFlag() {
		return saveFlag;
	}

	public String getScheduleFlag() {
		return this.scheduleFlag;
	}

	public String getShareFlag() {
		return shareFlag;
	}

	public String getSql() {
		return this.sql;
	}

	public String getTargetTableName() {
		return targetTableName;
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

	public String getUuid() {
		return uuid;
	}

	public int getWidth() {
		return width;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public SqlDefinition jsonToObject(JSONObject jsonObject) {
		return SqlDefinitionJsonFactory.jsonToObject(jsonObject);
	}

	public void setAggregationFlag(String aggregationFlag) {
		this.aggregationFlag = aggregationFlag;
	}

	public void setCacheFlag(String cacheFlag) {
		this.cacheFlag = cacheFlag;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setColumns(List<ColumnDefinition> columns) {
		this.columns = columns;
	}

	public void setCountSql(String countSql) {
		this.countSql = countSql;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setDataItemFlag(String dataItemFlag) {
		this.dataItemFlag = dataItemFlag;
	}

	public void setDeleteFetch(String deleteFetch) {
		this.deleteFetch = deleteFetch;
	}

	public void setExportFlag(String exportFlag) {
		this.exportFlag = exportFlag;
	}

	public void setExportTableName(String exportTableName) {
		this.exportTableName = exportTableName;
	}

	public void setExportTemplate(String exportTemplate) {
		this.exportTemplate = exportTemplate;
	}

	public void setFetchFlag(String fetchFlag) {
		this.fetchFlag = fetchFlag;
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

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public void setOrdinal(int ordinal) {
		this.ordinal = ordinal;
	}

	public void setParameters(List<SqlCriteria> parameters) {
		this.parameters = parameters;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public void setPublicFlag(String publicFlag) {
		this.publicFlag = publicFlag;
	}

	public void setRowKey(String rowKey) {
		this.rowKey = rowKey;
	}

	public void setSaveFlag(String saveFlag) {
		this.saveFlag = saveFlag;
	}

	public void setScheduleFlag(String scheduleFlag) {
		this.scheduleFlag = scheduleFlag;
	}

	public void setShareFlag(String shareFlag) {
		this.shareFlag = shareFlag;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public void setTargetTableName(String targetTableName) {
		this.targetTableName = targetTableName;
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

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public JSONObject toJsonObject() {
		return SqlDefinitionJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return SqlDefinitionJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
