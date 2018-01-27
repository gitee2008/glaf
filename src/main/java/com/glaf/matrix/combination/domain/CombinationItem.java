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
@Table(name = "SYS_COMBINATION_ITEM")
public class CombinationItem implements Serializable, JSONable {
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
	 * 标题
	 */
	@Column(name = "TITLE_", length = 200)
	protected String title;

	/**
	 * SQL语句
	 */
	@Lob
	@Column(name = "SQL_")
	protected String sql;

	/**
	 * 遍历SQL
	 */
	@Lob
	@Column(name = "RECURSIONSQL_", length = 4000)
	protected String recursionSql;

	/**
	 * 循环列,写到目标表的条件列
	 */
	@Column(name = "RECURSIONCOLUMNS_", length = 4000)
	protected String recursionColumns;

	/**
	 * 主键
	 */
	@Column(name = "PRIMARYKEY_", length = 50)
	protected String primaryKey;

	/**
	 * 表达式
	 */
	@Column(name = "EXPRESSION_", length = 500)
	protected String expression;

	/**
	 * 是否需要建表
	 */
	@Column(name = "CREATETABLEFLAG_", length = 1)
	protected String createTableFlag;

	/**
	 * 每次抓取前删除
	 */
	@Column(name = "DELETEFETCH_", length = 1)
	protected String deleteFetch;

	/**
	 * 顺序号
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
	 * 创建时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME_")
	protected Date createTime;

	public CombinationItem() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CombinationItem other = (CombinationItem) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public String getCreateBy() {
		return this.createBy;
	}

	public String getCreateTableFlag() {
		return createTableFlag;
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

	public String getExpression() {
		return expression;
	}

	public long getId() {
		return this.id;
	}

	public int getLocked() {
		return locked;
	}

	public String getPrimaryKey() {
		if (primaryKey != null) {
			primaryKey = primaryKey.trim().toLowerCase();
		}
		return primaryKey;
	}

	public String getRecursionColumns() {
		if (recursionColumns != null) {
			recursionColumns = recursionColumns.trim().toLowerCase();
		}
		return recursionColumns;
	}

	public String getRecursionSql() {
		return recursionSql;
	}

	public int getSortNo() {
		return sortNo;
	}

	public String getSql() {
		return this.sql;
	}

	public long getSyncId() {
		return this.syncId;
	}

	public String getTitle() {
		return title;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public CombinationItem jsonToObject(JSONObject jsonObject) {
		return CombinationItemJsonFactory.jsonToObject(jsonObject);
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTableFlag(String createTableFlag) {
		this.createTableFlag = createTableFlag;
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

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setLocked(int locked) {
		this.locked = locked;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public void setRecursionColumns(String recursionColumns) {
		this.recursionColumns = recursionColumns;
	}

	public void setRecursionSql(String recursionSql) {
		this.recursionSql = recursionSql;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public void setSyncId(long syncId) {
		this.syncId = syncId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public JSONObject toJsonObject() {
		return CombinationItemJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return CombinationItemJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
