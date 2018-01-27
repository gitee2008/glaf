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
@Table(name = "SYS_COMBINATION_HISTORY")
public class CombinationHistory implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", nullable = false)
	protected long id;

	/**
	 * 主数据编号
	 */
	@Column(name = "SYNCID_")
	protected long syncId;

	/**
	 * 部署编号
	 */
	@Column(name = "DEPLOYMENTID_", length = 50)
	protected String deploymentId;

	/**
	 * 执行数据库编号
	 */
	@Column(name = "DATABASEID_")
	protected long databaseId;

	/**
	 * 数据库名称
	 */
	@Column(name = "DATABASENAME_", length = 200)
	protected String databaseName;

	/**
	 * 执行状态
	 */
	@Column(name = "STATUS_")
	protected int status;

	/**
	 * 耗时(毫秒)
	 */
	@Column(name = "TOTALTIME_")
	protected int totalTime;

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

	public CombinationHistory() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CombinationHistory other = (CombinationHistory) obj;
		if (id != other.id)
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

	public long getDatabaseId() {
		return this.databaseId;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public String getDeploymentId() {
		return this.deploymentId;
	}

	public long getId() {
		return this.id;
	}

	public int getStatus() {
		return this.status;
	}

	public long getSyncId() {
		return this.syncId;
	}

	public int getTotalTime() {
		return this.totalTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public CombinationHistory jsonToObject(JSONObject jsonObject) {
		return CombinationHistoryJsonFactory.jsonToObject(jsonObject);
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setDatabaseId(long databaseId) {
		this.databaseId = databaseId;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setSyncId(long syncId) {
		this.syncId = syncId;
	}

	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}

	public JSONObject toJsonObject() {
		return CombinationHistoryJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return CombinationHistoryJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
