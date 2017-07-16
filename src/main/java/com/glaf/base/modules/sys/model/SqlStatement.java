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

package com.glaf.base.modules.sys.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "SYS_SQL_STATEMENT")
public class SqlStatement implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", length = 50, nullable = false)
	protected String id;

	/**
	 * 数据库编号
	 */
	@Column(name = "DATABASEID_")
	protected long databaseId;

	@Column(name = "TITLE_", length = 250)
	protected String title;

	@Lob
	@Column(name = "SQL_", length = 4000)
	protected String sql;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXECUTETIME_")
	protected Date executeTime;

	@Column(name = "STATUS_")
	protected int status;

	public SqlStatement() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SqlStatement other = (SqlStatement) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public long getDatabaseId() {
		return databaseId;
	}

	public Date getExecuteTime() {
		return executeTime;
	}

	public String getId() {
		return id;
	}

	public String getSql() {
		return sql;
	}

	public int getStatus() {
		return status;
	}

	public String getTitle() {
		return title;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public void setDatabaseId(long databaseId) {
		this.databaseId = databaseId;
	}

	public void setExecuteTime(Date executeTime) {
		this.executeTime = executeTime;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
