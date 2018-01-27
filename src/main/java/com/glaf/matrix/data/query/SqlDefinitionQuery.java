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

package com.glaf.matrix.data.query;

import java.util.*;

import com.glaf.core.query.DataQuery;

public class SqlDefinitionQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected List<Long> ids;
	protected String code;
	protected List<String> codes;
	protected String name;
	protected String titleLike;
	protected String type;
	protected String dataItemFlag;
	protected String exportFlag;
	protected String fetchFlag;
	protected String publicFlag;
	protected String shareFlag;
	protected String scheduleFlag;
	protected String showShare;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;
	protected String operation;

	public SqlDefinitionQuery() {

	}

	public SqlDefinitionQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public SqlDefinitionQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public SqlDefinitionQuery dataItemFlag(String dataItemFlag) {
		if (dataItemFlag == null) {
			throw new RuntimeException("dataItemFlag is null");
		}
		this.dataItemFlag = dataItemFlag;
		return this;
	}

	public String getCode() {
		return code;
	}

	public List<String> getCodes() {
		return codes;
	}

	public Date getCreateTimeGreaterThanOrEqual() {
		return createTimeGreaterThanOrEqual;
	}

	public Date getCreateTimeLessThanOrEqual() {
		return createTimeLessThanOrEqual;
	}

	public String getDataItemFlag() {
		return dataItemFlag;
	}

	public String getExportFlag() {
		return exportFlag;
	}

	public String getFetchFlag() {
		return fetchFlag;
	}

	public List<Long> getIds() {
		return ids;
	}

	public String getName() {
		return name;
	}

	public String getOperation() {
		return operation;
	}

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("parentId".equals(sortColumn)) {
				orderBy = "E.PARENTID_" + a_x;
			}

			if ("name".equals(sortColumn)) {
				orderBy = "E.NAME_" + a_x;
			}

			if ("title".equals(sortColumn)) {
				orderBy = "E.TITLE_" + a_x;
			}

			if ("sql".equals(sortColumn)) {
				orderBy = "E.SQL_" + a_x;
			}

			if ("countSql".equals(sortColumn)) {
				orderBy = "E.COUNTSQL_" + a_x;
			}

			if ("type".equals(sortColumn)) {
				orderBy = "E.TYPE_" + a_x;
			}

			if ("operation".equals(sortColumn)) {
				orderBy = "E.OPERATION_" + a_x;
			}

			if ("rowKey".equals(sortColumn)) {
				orderBy = "E.ROWKEY_" + a_x;
			}

			if ("keyType".equals(sortColumn)) {
				orderBy = "E.KEYTYPE_" + a_x;
			}

			if ("scheduleFlag".equals(sortColumn)) {
				orderBy = "E.SCHEDULEFLAG_" + a_x;
			}

			if ("cacheFlag".equals(sortColumn)) {
				orderBy = "E.CACHEFLAG_" + a_x;
			}

			if ("createBy".equals(sortColumn)) {
				orderBy = "E.CREATEBY_" + a_x;
			}

			if ("createTime".equals(sortColumn)) {
				orderBy = "E.CREATETIME_" + a_x;
			}

			if ("updateBy".equals(sortColumn)) {
				orderBy = "E.UPDATEBY_" + a_x;
			}

			if ("updateTime".equals(sortColumn)) {
				orderBy = "E.UPDATETIME_" + a_x;
			}

			if ("code".equals(sortColumn)) {
				orderBy = "E.CODE_" + a_x;
			}

		}
		return orderBy;
	}

	public String getPublicFlag() {
		return publicFlag;
	}

	public String getScheduleFlag() {
		return scheduleFlag;
	}

	public String getShareFlag() {
		return shareFlag;
	}

	public String getShowShare() {
		return showShare;
	}

	public String getTitleLike() {
		if (titleLike != null && titleLike.trim().length() > 0) {
			if (!titleLike.startsWith("%")) {
				titleLike = "%" + titleLike;
			}
			if (!titleLike.endsWith("%")) {
				titleLike = titleLike + "%";
			}
		}
		return titleLike;
	}

	public String getType() {
		return type;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("parentId", "PARENTID_");
		addColumn("name", "NAME_");
		addColumn("title", "TITLE_");
		addColumn("sql", "SQL_");
		addColumn("countSql", "COUNTSQL_");
		addColumn("type", "TYPE_");
		addColumn("operation", "OPERATION_");
		addColumn("rowKey", "ROWKEY_");
		addColumn("keyType", "KEYTYPE_");
		addColumn("scheduleFlag", "SCHEDULEFLAG_");
		addColumn("cacheFlag", "CACHEFLAG_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
		addColumn("updateBy", "UPDATEBY_");
		addColumn("updateTime", "UPDATETIME_");
	}

	public SqlDefinitionQuery name(String name) {
		if (name == null) {
			throw new RuntimeException("name is null");
		}
		this.name = name;
		return this;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setCodes(List<String> codes) {
		this.codes = codes;
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setDataItemFlag(String dataItemFlag) {
		this.dataItemFlag = dataItemFlag;
	}

	public void setExportFlag(String exportFlag) {
		this.exportFlag = exportFlag;
	}

	public void setFetchFlag(String fetchFlag) {
		this.fetchFlag = fetchFlag;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public void setPublicFlag(String publicFlag) {
		this.publicFlag = publicFlag;
	}

	public void setScheduleFlag(String scheduleFlag) {
		this.scheduleFlag = scheduleFlag;
	}

	public void setShareFlag(String shareFlag) {
		this.shareFlag = shareFlag;
	}

	public void setShowShare(String showShare) {
		this.showShare = showShare;
	}

	public void setTitleLike(String titleLike) {
		this.titleLike = titleLike;
	}

	public void setType(String type) {
		this.type = type;
	}

	public SqlDefinitionQuery titleLike(String titleLike) {
		if (titleLike == null) {
			throw new RuntimeException("title is null");
		}
		this.titleLike = titleLike;
		return this;
	}

	public SqlDefinitionQuery type(String type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		this.type = type;
		return this;
	}

}