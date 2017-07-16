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
import com.glaf.matrix.data.util.TableCorrelationJsonFactory;

/**
 * 
 * 实体对象
 *
 */

@Entity
@Table(name = "SYS_TABLE_CORRELATION")
public class TableCorrelation implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", length = 50, nullable = false)
	protected String id;

	/**
	 * 主表编号
	 */
	@Column(name = "MASTERTABLEID_", length = 50)
	protected String masterTableId;

	/**
	 * 主表名称
	 */
	@Column(name = "MASTERTABLENAME_", length = 50)
	protected String masterTableName;

	@javax.persistence.Transient
	protected String masterTableTitle;

	/**
	 * 从表编号
	 */
	@Column(name = "SLAVETABLEID_", length = 50)
	protected String slaveTableId;

	/**
	 * 从表名称
	 */
	@Column(name = "SLAVETABLENAME_", length = 50)
	protected String slaveTableName;

	@javax.persistence.Transient
	protected String slaveTableTitle;

	/**
	 * 级联新增
	 */
	@Column(name = "INSERTCASCADE_", length = 20)
	protected String insertCascade;

	/**
	 * 级联删除
	 */
	@Column(name = "DELETECASCADE_", length = 20)
	protected String deleteCascade;

	/**
	 * 级联更新
	 */
	@Column(name = "UPDATECASCADE_", length = 20)
	protected String updateCascade;

	/**
	 * 关联类型
	 */
	@Column(name = "RELATIONSHIPTYPE_", length = 20)
	protected String relationshipType;

	/**
	 * 排序
	 */
	@Column(name = "SORTNO_")
	protected int sortNo;

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

	public TableCorrelation() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TableCorrelation other = (TableCorrelation) obj;
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

	public String getDeleteCascade() {
		return this.deleteCascade;
	}

	public String getId() {
		return this.id;
	}

	public String getInsertCascade() {
		return this.insertCascade;
	}

	public String getMasterTableId() {
		return this.masterTableId;
	}

	public String getMasterTableName() {
		return this.masterTableName;
	}

	public String getMasterTableTitle() {
		return masterTableTitle;
	}

	public String getRelationshipType() {
		return this.relationshipType;
	}

	public String getSlaveTableId() {
		return this.slaveTableId;
	}

	public String getSlaveTableName() {
		return this.slaveTableName;
	}

	public String getSlaveTableTitle() {
		return slaveTableTitle;
	}

	public int getSortNo() {
		return this.sortNo;
	}

	public String getUpdateBy() {
		return this.updateBy;
	}

	public String getUpdateCascade() {
		return this.updateCascade;
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

	public TableCorrelation jsonToObject(JSONObject jsonObject) {
		return TableCorrelationJsonFactory.jsonToObject(jsonObject);
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setDeleteCascade(String deleteCascade) {
		this.deleteCascade = deleteCascade;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setInsertCascade(String insertCascade) {
		this.insertCascade = insertCascade;
	}

	public void setMasterTableId(String masterTableId) {
		this.masterTableId = masterTableId;
	}

	public void setMasterTableName(String masterTableName) {
		this.masterTableName = masterTableName;
	}

	public void setMasterTableTitle(String masterTableTitle) {
		this.masterTableTitle = masterTableTitle;
	}

	public void setRelationshipType(String relationshipType) {
		this.relationshipType = relationshipType;
	}

	public void setSlaveTableId(String slaveTableId) {
		this.slaveTableId = slaveTableId;
	}

	public void setSlaveTableName(String slaveTableName) {
		this.slaveTableName = slaveTableName;
	}

	public void setSlaveTableTitle(String slaveTableTitle) {
		this.slaveTableTitle = slaveTableTitle;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public void setUpdateCascade(String updateCascade) {
		this.updateCascade = updateCascade;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public JSONObject toJsonObject() {
		return TableCorrelationJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return TableCorrelationJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
