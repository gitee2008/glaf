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
@Table(name = "SYS_AGGREGATION_DEF")
public class AggregationDefinition implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", nullable = false)
	protected long id;

	/**
	 * 主题
	 */
	@Column(name = "TITLE_", length = 200)
	protected String title;

	/**
	 * 服务标识
	 */
	@Column(name = "SERVICEKEY_", length = 50)
	protected String serviceKey;

	/**
	 * 统计项名称
	 */
	@Column(name = "NAME_", length = 80)
	protected String name;

	/**
	 * 统计项值
	 */
	@Column(name = "SQL_", length = 4000)
	protected String sql;

	/**
	 * 精度
	 */
	@Column(name = "PRECISION_")
	protected int precision;

	/**
	 * 目标表名称
	 */
	@Column(name = "TARGETTABLENAME_", length = 50)
	protected String targetTableName;

	/**
	 * 表分区标识
	 */
	@Column(name = "PARTITIONFLAG_", length = 1)
	protected String partitionFlag;

	/**
	 * 结果标识
	 */
	@Column(name = "RESULTFLAG_", length = 1)
	protected String resultFlag;

	/**
	 * 类型
	 */
	@Column(name = "TYPE_", length = 50)
	protected String type;

	/**
	 * 顺序
	 */
	@Column(name = "SORTNO_")
	protected int sortNo;

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

	public AggregationDefinition() {

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

	public long getId() {
		return this.id;
	}

	public int getLocked() {
		return this.locked;
	}

	public String getName() {
		return this.name;
	}

	public String getPartitionFlag() {
		return partitionFlag;
	}

	public int getPrecision() {
		return this.precision;
	}

	public String getResultFlag() {
		return resultFlag;
	}

	public String getServiceKey() {
		return serviceKey;
	}

	public int getSortNo() {
		return this.sortNo;
	}

	public String getSql() {
		return this.sql;
	}

	public String getTargetTableName() {
		return this.targetTableName;
	}

	public String getTitle() {
		return title;
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

	public AggregationDefinition jsonToObject(JSONObject jsonObject) {
		return AggregationDefinitionJsonFactory.jsonToObject(jsonObject);
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public void setPartitionFlag(String partitionFlag) {
		this.partitionFlag = partitionFlag;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public void setResultFlag(String resultFlag) {
		this.resultFlag = resultFlag;
	}

	public void setServiceKey(String serviceKey) {
		this.serviceKey = serviceKey;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
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

	public JSONObject toJsonObject() {
		return AggregationDefinitionJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return AggregationDefinitionJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
