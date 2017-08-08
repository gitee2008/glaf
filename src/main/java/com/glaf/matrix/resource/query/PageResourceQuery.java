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
package com.glaf.matrix.resource.query;

import java.util.*;
import com.glaf.core.query.DataQuery;

public class PageResourceQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected String resPath;
	protected String resPathLike;
	protected String resFileName;
	protected String resFileNameLike;
	protected String resName;
	protected String resNameLike;
	protected String resType;
	protected String resTypeLike;
	protected String resContentType;
	protected String resContentTypeLike;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	public PageResourceQuery() {

	}

	public PageResourceQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public PageResourceQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public Date getCreateTimeGreaterThanOrEqual() {
		return createTimeGreaterThanOrEqual;
	}

	public Date getCreateTimeLessThanOrEqual() {
		return createTimeLessThanOrEqual;
	}

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("resPath".equals(sortColumn)) {
				orderBy = "E.PATH_" + a_x;
			}

			if ("resFileName".equals(sortColumn)) {
				orderBy = "E.FILENAME_" + a_x;
			}

			if ("resName".equals(sortColumn)) {
				orderBy = "E.NAME_" + a_x;
			}

			if ("resType".equals(sortColumn)) {
				orderBy = "E.TYPE_" + a_x;
			}

			if ("resContentType".equals(sortColumn)) {
				orderBy = "E.CONTENTTYPE_" + a_x;
			}

			if ("createTime".equals(sortColumn)) {
				orderBy = "E.CREATETIME_" + a_x;
			}

		}
		return orderBy;
	}

	public String getResContentType() {
		return resContentType;
	}

	public String getResContentTypeLike() {
		if (resContentTypeLike != null && resContentTypeLike.trim().length() > 0) {
			if (!resContentTypeLike.startsWith("%")) {
				resContentTypeLike = "%" + resContentTypeLike;
			}
			if (!resContentTypeLike.endsWith("%")) {
				resContentTypeLike = resContentTypeLike + "%";
			}
		}
		return resContentTypeLike;
	}

	public String getResFileName() {
		return resFileName;
	}

	public String getResFileNameLike() {
		if (resFileNameLike != null && resFileNameLike.trim().length() > 0) {
			if (!resFileNameLike.startsWith("%")) {
				resFileNameLike = "%" + resFileNameLike;
			}
			if (!resFileNameLike.endsWith("%")) {
				resFileNameLike = resFileNameLike + "%";
			}
		}
		return resFileNameLike;
	}

	public String getResName() {
		return resName;
	}

	public String getResNameLike() {
		if (resNameLike != null && resNameLike.trim().length() > 0) {
			if (!resNameLike.startsWith("%")) {
				resNameLike = "%" + resNameLike;
			}
			if (!resNameLike.endsWith("%")) {
				resNameLike = resNameLike + "%";
			}
		}
		return resNameLike;
	}

	public String getResPath() {
		return resPath;
	}

	public String getResPathLike() {
		if (resPathLike != null && resPathLike.trim().length() > 0) {
			if (!resPathLike.startsWith("%")) {
				resPathLike = "%" + resPathLike;
			}
			if (!resPathLike.endsWith("%")) {
				resPathLike = resPathLike + "%";
			}
		}
		return resPathLike;
	}

	public String getResType() {
		return resType;
	}

	public String getResTypeLike() {
		if (resTypeLike != null && resTypeLike.trim().length() > 0) {
			if (!resTypeLike.startsWith("%")) {
				resTypeLike = "%" + resTypeLike;
			}
			if (!resTypeLike.endsWith("%")) {
				resTypeLike = resTypeLike + "%";
			}
		}
		return resTypeLike;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("resPath", "PATH_");
		addColumn("resFileName", "FILENAME_");
		addColumn("resName", "NAME_");
		addColumn("resType", "TYPE_");
		addColumn("resContentType", "CONTENTTYPE_");
		addColumn("createTime", "CREATETIME_");
	}

	public PageResourceQuery resContentType(String resContentType) {
		if (resContentType == null) {
			throw new RuntimeException("resContentType is null");
		}
		this.resContentType = resContentType;
		return this;
	}

	public PageResourceQuery resContentTypeLike(String resContentTypeLike) {
		if (resContentTypeLike == null) {
			throw new RuntimeException("resContentType is null");
		}
		this.resContentTypeLike = resContentTypeLike;
		return this;
	}

	public PageResourceQuery resFileName(String resFileName) {
		if (resFileName == null) {
			throw new RuntimeException("resFileName is null");
		}
		this.resFileName = resFileName;
		return this;
	}

	public PageResourceQuery resFileNameLike(String resFileNameLike) {
		if (resFileNameLike == null) {
			throw new RuntimeException("resFileName is null");
		}
		this.resFileNameLike = resFileNameLike;
		return this;
	}

	public PageResourceQuery resName(String resName) {
		if (resName == null) {
			throw new RuntimeException("resName is null");
		}
		this.resName = resName;
		return this;
	}

	public PageResourceQuery resNameLike(String resNameLike) {
		if (resNameLike == null) {
			throw new RuntimeException("resName is null");
		}
		this.resNameLike = resNameLike;
		return this;
	}

	public PageResourceQuery resPath(String resPath) {
		if (resPath == null) {
			throw new RuntimeException("resPath is null");
		}
		this.resPath = resPath;
		return this;
	}

	public PageResourceQuery resPathLike(String resPathLike) {
		if (resPathLike == null) {
			throw new RuntimeException("resPath is null");
		}
		this.resPathLike = resPathLike;
		return this;
	}

	public PageResourceQuery resType(String resType) {
		if (resType == null) {
			throw new RuntimeException("resType is null");
		}
		this.resType = resType;
		return this;
	}

	public PageResourceQuery resTypeLike(String resTypeLike) {
		if (resTypeLike == null) {
			throw new RuntimeException("resType is null");
		}
		this.resTypeLike = resTypeLike;
		return this;
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setResContentType(String resContentType) {
		this.resContentType = resContentType;
	}

	public void setResContentTypeLike(String resContentTypeLike) {
		this.resContentTypeLike = resContentTypeLike;
	}

	public void setResFileName(String resFileName) {
		this.resFileName = resFileName;
	}

	public void setResFileNameLike(String resFileNameLike) {
		this.resFileNameLike = resFileNameLike;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}

	public void setResNameLike(String resNameLike) {
		this.resNameLike = resNameLike;
	}

	public void setResPath(String resPath) {
		this.resPath = resPath;
	}

	public void setResPathLike(String resPathLike) {
		this.resPathLike = resPathLike;
	}

	public void setResType(String resType) {
		this.resType = resType;
	}

	public void setResTypeLike(String resTypeLike) {
		this.resTypeLike = resTypeLike;
	}

}