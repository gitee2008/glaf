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

package com.glaf.matrix.combination.domain;

import java.io.*;
import java.util.*;
import javax.persistence.*;
import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.glaf.core.base.*;
import com.glaf.core.util.DateUtils;
import com.glaf.matrix.combination.util.*;

/**
 * 
 * 实体对象
 *
 */

@Entity
@Table(name = "SYS_COMBINATION_APP")
public class CombinationApp implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", nullable = false)
	protected long id;

	/**
	 * 节点编号
	 */
	@Column(name = "NODEID_")
	protected long nodeId;

	/**
	 * 部署编号
	 */
	@Column(name = "DEPLOYMENTID_", length = 50)
	protected String deploymentId;

	/**
	 * 标题
	 */
	@Column(name = "TITLE_", length = 200)
	protected String title;

	/**
	 * 来源数据库编号
	 */
	@Column(name = "SRCDATABASEID_")
	protected long srcDatabaseId;

	/**
	 * 同步标识
	 */
	@Column(name = "SYNCFLAG_", length = 50)
	protected String syncFlag;

	/**
	 * 目标库编号
	 */
	@Column(name = "TARGETDATABASEIDS_", length = 4000)
	protected String targetDatabaseIds;

	/**
	 * 目标表
	 */
	@Column(name = "TARGETTABLENAME_", length = 50)
	protected String targetTableName;

	/**
	 * 类型
	 */
	@Column(name = "TYPE_", length = 50)
	protected String type;

	/**
	 * 自动同步
	 */
	@Column(name = "AUTOSYNCFLAG_", length = 50)
	protected String autoSyncFlag;

	/**
	 * 每次抓取前删除
	 */
	@Column(name = "DELETEFETCH_", length = 1)
	protected String deleteFetch;

	/**
	 * 外部列定义
	 */
	@Column(name = "EXTERNALCOLUMNSFLAG_", length = 50)
	protected String externalColumnsFlag;

	/**
	 * 时间间隔
	 */
	@Column(name = "INTERVAL_")
	protected int interval;

	/**
	 * 顺序号
	 */
	@Column(name = "SORTNO_")
	protected int sortNo;

	/**
	 * 是否有效
	 */
	@Column(name = "ACTIVE_", length = 1)
	protected String active;

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
	 * 更新人
	 */
	@Column(name = "UPDATEBY_", length = 50)
	protected String updateBy;

	/**
	 * 更新时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATETIME_")
	protected Date updateTime;

	@javax.persistence.Transient
	protected List<CombinationItem> items = new ArrayList<CombinationItem>();

	public CombinationApp() {

	}

	public void addItem(CombinationItem item) {
		if (items == null) {
			items = new ArrayList<CombinationItem>();
		}
		items.add(item);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CombinationApp other = (CombinationApp) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public String getActive() {
		return active;
	}

	public String getAutoSyncFlag() {
		return this.autoSyncFlag;
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

	public String getDeleteFetch() {
		return deleteFetch;
	}

	public String getDeploymentId() {
		return this.deploymentId;
	}

	public String getExternalColumnsFlag() {
		return externalColumnsFlag;
	}

	public long getId() {
		return this.id;
	}

	public int getInterval() {
		return this.interval;
	}

	public List<CombinationItem> getItems() {
		return items;
	}

	public long getNodeId() {
		return this.nodeId;
	}

	public int getSortNo() {
		return sortNo;
	}

	public long getSrcDatabaseId() {
		return this.srcDatabaseId;
	}

	public String getSyncFlag() {
		return this.syncFlag;
	}

	public String getTargetDatabaseIds() {
		return this.targetDatabaseIds;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public CombinationApp jsonToObject(JSONObject jsonObject) {
		return CombinationAppJsonFactory.jsonToObject(jsonObject);
	}

	public void setActive(String active) {
		this.active = active;
	}

	public void setAutoSyncFlag(String autoSyncFlag) {
		this.autoSyncFlag = autoSyncFlag;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setDeleteFetch(String deleteFetch) {
		this.deleteFetch = deleteFetch;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}

	public void setExternalColumnsFlag(String externalColumnsFlag) {
		this.externalColumnsFlag = externalColumnsFlag;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public void setItems(List<CombinationItem> items) {
		this.items = items;
	}

	public void setNodeId(long nodeId) {
		this.nodeId = nodeId;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	public void setSrcDatabaseId(long srcDatabaseId) {
		this.srcDatabaseId = srcDatabaseId;
	}

	public void setSyncFlag(String syncFlag) {
		this.syncFlag = syncFlag;
	}

	public void setTargetDatabaseIds(String targetDatabaseIds) {
		this.targetDatabaseIds = targetDatabaseIds;
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

	public JSONObject toJsonObject() {
		return CombinationAppJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return CombinationAppJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
