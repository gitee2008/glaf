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

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.matrix.data.util.SysTableJsonFactory;
import com.glaf.matrix.data.util.TableColumnJsonFactory;

/**
 * 数据表定义
 * 
 */
@Entity
@Table(name = "SYS_TABLE")
public class SysTable implements java.io.Serializable, java.lang.Comparable<SysTable> {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "TABLEID_", length = 50)
	protected String tableId;

	/**
	 * 表名
	 */
	@Column(name = "TABLENAME_", length = 50)
	protected String tableName;

	@Column(name = "ADDTYPE_")
	protected int addType;

	@Column(name = "NODEID_")
	protected Long nodeId;

	/**
	 * 报表编号
	 */
	@Column(name = "REPORTID_", length = 80)
	protected String reportId;

	/**
	 * 表单报表编号
	 */
	@Column(name = "FORMREPORTID_", length = 80)
	protected String formReportId;

	/**
	 * 聚合主键列集
	 */
	@Column(name = "AGGREGATIONKEY_", length = 500)
	protected String aggregationKey;

	/**
	 * 描述
	 */
	@Column(name = "DESCRIPTION_", length = 500)
	protected String description;

	/**
	 * 标题
	 */
	@Column(name = "ENGLISHTITLE_")
	protected String englishTitle;

	/**
	 * 是否从表(Y-从表，N-不是从表)
	 */
	@Column(name = "ISSUBTABLE_", length = 2)
	protected String isSubTable;

	@Column(name = "PRIMARYKEY_", length = 200)
	protected String primaryKey;

	/**
	 * 排序列名
	 */
	@Column(name = "SORTCOLUMN_", length = 50)
	protected String sortColumn;

	/**
	 * 排序方式
	 */
	@Column(name = "SORTORDER_", length = 10)
	protected String sortOrder;

	/**
	 * 修订版本
	 */
	@Column(name = "REVISION_")
	protected int revision;

	@Column(name = "SORTNO_")
	protected int sortNo;

	@Column(name = "SYSNUM_", length = 100)
	protected String sysnum;

	@Column(name = "SYSTEMFLAG_", length = 2)
	protected String systemFlag;

	@Column(name = "PARTITIONFLAG_", length = 1)
	protected String partitionFlag;

	/**
	 * 附件标识 0-无，1-1个附件，2-多个附件
	 */
	@Column(name = "ATTACHMENTFLAG_", length = 1)
	protected String attachmentFlag;

	/**
	 * 附件允许的扩展名
	 */
	@Column(name = "ATTACHMENTEXTS_", length = 200)
	protected String attachmentExts;

	/**
	 * 附件大小
	 */
	@Column(name = "ATTACHMENTSIZE_")
	protected int attachmentSize;

	/**
	 * 审核标记
	 */
	@Column(name = "AUDITFLAG_", length = 10)
	protected String auditFlag;

	/**
	 * 树型结构标识， Y-树型，N-非树型
	 */
	@Column(name = "TREEFLAG_", length = 1)
	protected String treeFlag;

	/**
	 * 权限标识
	 */
	@Column(name = "PRIVILEGEFLAG_", length = 10)
	protected String privilegeFlag;

	/**
	 * 是否临时表
	 */
	@Column(name = "TEMPORARYFLAG_", length = 1)
	protected String temporaryFlag;

	/**
	 * 标题
	 */
	@Column(name = "TITLE_")
	protected String title;

	@Column(name = "TOPID_", length = 50)
	protected String topId;

	/**
	 * 表类型
	 */
	@Column(name = "TYPE_", length = 50)
	protected String type;

	/**
	 * 级联插入
	 */
	@Column(name = "INSERTCASCADE_")
	protected int insertCascade;

	@Transient
	protected boolean insertOnly;

	@Transient
	protected boolean updateAllowed = true;

	/**
	 * 级联删除
	 */
	@Column(name = "DELETECASCADE_")
	protected int deleteCascade;

	/**
	 * 是否删除抓取数据
	 */
	@Column(name = "DELETEFETCH_", length = 1)
	protected String deleteFetch;

	@Column(name = "DELETEFLAG_")
	protected int deleteFlag;

	/**
	 * 级联更新
	 */
	@Column(name = "UPDATECASCADE_")
	protected int updateCascade;

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

	@Transient
	protected TableColumn idColumn;

	/**
	 * 表后缀
	 */
	@javax.persistence.Transient
	protected String tableSuffix;

	@javax.persistence.Transient
	protected TableCorrelation tableCorrelation;

	@Transient
	protected List<TableColumn> columns = new ArrayList<TableColumn>();

	public SysTable() {

	}

	public void addColumn(TableColumn column) {
		if (columns == null) {
			columns = new java.util.ArrayList<TableColumn>();
		}
		if (!columns.contains(column)) {
			columns.add(column);
		}
	}

	public void addField(TableColumn field) {
		if (columns == null) {
			columns = new java.util.ArrayList<TableColumn>();
		}
		JSONObject jsonObject = field.toJsonObject();
		TableColumn column = TableColumnJsonFactory.jsonToObject(jsonObject);
		columns.add(column);
	}

	public int compareTo(SysTable o) {
		if (o == null) {
			return -1;
		}

		SysTable field = o;

		int l = this.sortNo - field.getSortNo();

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
		SysTable other = (SysTable) obj;
		if (tableName == null) {
			if (other.tableName != null)
				return false;
		} else if (!tableName.equals(other.tableName))
			return false;
		return true;
	}

	public int getAddType() {
		return addType;
	}

	public String getAggregationKey() {
		return aggregationKey;
	}

	public String getAttachmentExts() {
		return attachmentExts;
	}

	public String getAttachmentFlag() {
		return attachmentFlag;
	}

	public int getAttachmentSize() {
		return attachmentSize;
	}

	public String getAuditFlag() {
		return auditFlag;
	}

	public List<TableColumn> getColumns() {
		return columns;
	}

	public String getCreateBy() {
		return createBy;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public int getDeleteCascade() {
		return deleteCascade;
	}

	public String getDeleteFetch() {
		return deleteFetch;
	}

	public int getDeleteFlag() {
		return deleteFlag;
	}

	public String getDescription() {
		return description;
	}

	public String getEnglishTitle() {
		return englishTitle;
	}

	public Map<String, TableColumn> getFields() {
		Map<String, TableColumn> fieldMap = new LinkedHashMap<String, TableColumn>();
		if (columns != null && !columns.isEmpty()) {
			for (TableColumn column : columns) {
				fieldMap.put(column.getName(), column);
			}
		}
		return fieldMap;
	}

	public String getFormReportId() {
		return formReportId;
	}

	public TableColumn getIdColumn() {
		return idColumn;
	}

	public TableColumn getIdField() {
		return idColumn;
	}

	public int getInsertCascade() {
		return insertCascade;
	}

	public String getIsSubTable() {
		return isSubTable;
	}

	public int getLocked() {
		return locked;
	}

	public Long getNodeId() {
		return nodeId;
	}

	public String getPartitionFlag() {
		return partitionFlag;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public String getPrivilegeFlag() {
		return privilegeFlag;
	}

	public String getReportId() {
		return reportId;
	}

	public int getRevision() {
		return revision;
	}

	public String getSortColumn() {
		return sortColumn;
	}

	public int getSortNo() {
		return sortNo;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public String getSysnum() {
		return sysnum;
	}

	public String getSystemFlag() {
		return systemFlag;
	}

	public TableCorrelation getTableCorrelation() {
		return tableCorrelation;
	}

	public String getTableId() {
		return tableId;
	}

	public String getTableName() {
		return tableName;
	}

	public String getTableSuffix() {
		if (tableSuffix == null) {
			tableSuffix = "";
		}
		return tableSuffix;
	}

	public String getTemporaryFlag() {
		return temporaryFlag;
	}

	public String getTitle() {
		return title;
	}

	public String getTopId() {
		return topId;
	}

	public String getTreeFlag() {
		return treeFlag;
	}

	public String getType() {
		return type;
	}

	public int getUpdateCascade() {
		return updateCascade;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tableName == null) ? 0 : tableName.hashCode());
		return result;
	}

	public boolean isInsertOnly() {
		return insertOnly;
	}

	public boolean isUpdateAllowed() {
		return updateAllowed;
	}

	public SysTable jsonToObject(JSONObject jsonObject) {
		return SysTableJsonFactory.jsonToObject(jsonObject);
	}

	public void setAddType(int addType) {
		this.addType = addType;
	}

	public void setAggregationKey(String aggregationKey) {
		this.aggregationKey = aggregationKey;
	}

	public void setAttachmentExts(String attachmentExts) {
		this.attachmentExts = attachmentExts;
	}

	public void setAttachmentFlag(String attachmentFlag) {
		this.attachmentFlag = attachmentFlag;
	}

	public void setAttachmentSize(int attachmentSize) {
		this.attachmentSize = attachmentSize;
	}

	public void setAuditFlag(String auditFlag) {
		this.auditFlag = auditFlag;
	}

	public void setColumns(List<TableColumn> columns) {
		this.columns = columns;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setDeleteCascade(int deleteCascade) {
		this.deleteCascade = deleteCascade;
	}

	public void setDeleteFetch(String deleteFetch) {
		this.deleteFetch = deleteFetch;
	}

	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setEnglishTitle(String englishTitle) {
		this.englishTitle = englishTitle;
	}

	public void setFormReportId(String formReportId) {
		this.formReportId = formReportId;
	}

	public void setIdColumn(TableColumn idColumn) {
		if (idColumn != null) {
			this.idColumn = idColumn;
			this.idColumn.setPrimaryKey(true);
			this.addColumn(idColumn);
		}
	}

	public void setIdField(TableColumn idField) {
		JSONObject jsonObject = idField.toJsonObject();
		this.idColumn = TableColumnJsonFactory.jsonToObject(jsonObject);
		idColumn.setPrimaryKey(true);
	}

	public void setInsertCascade(int insertCascade) {
		this.insertCascade = insertCascade;
	}

	public void setInsertOnly(boolean insertOnly) {
		this.insertOnly = insertOnly;
	}

	public void setIsSubTable(String isSubTable) {
		this.isSubTable = isSubTable;
	}

	public void setLocked(int locked) {
		this.locked = locked;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public void setPartitionFlag(String partitionFlag) {
		this.partitionFlag = partitionFlag;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public void setPrivilegeFlag(String privilegeFlag) {
		this.privilegeFlag = privilegeFlag;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public void setRevision(int revision) {
		this.revision = revision;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public void setSysnum(String sysnum) {
		this.sysnum = sysnum;
	}

	public void setSystemFlag(String systemFlag) {
		this.systemFlag = systemFlag;
	}

	public void setTableCorrelation(TableCorrelation tableCorrelation) {
		this.tableCorrelation = tableCorrelation;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setTableSuffix(String tableSuffix) {
		this.tableSuffix = tableSuffix;
	}

	public void setTemporaryFlag(String temporaryFlag) {
		this.temporaryFlag = temporaryFlag;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTopId(String topId) {
		this.topId = topId;
	}

	public void setTreeFlag(String treeFlag) {
		this.treeFlag = treeFlag;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUpdateAllowed(boolean updateAllowed) {
		this.updateAllowed = updateAllowed;
	}

	public void setUpdateCascade(int updateCascade) {
		this.updateCascade = updateCascade;
	}

	public JSONObject toJsonObject() {
		return SysTableJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return SysTableJsonFactory.toObjectNode(this);
	}

}