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

package com.glaf.core.access.query;

import java.util.*;
import com.glaf.core.query.DataQuery;

public class AccessUriQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected List<Long> ids;
	protected String uri;
	protected String uriLike;
	protected String titleLike;
	protected String descriptionLike;

	public AccessUriQuery() {

	}

	public AccessUriQuery descriptionLike(String descriptionLike) {
		if (descriptionLike == null) {
			throw new RuntimeException("description is null");
		}
		this.descriptionLike = descriptionLike;
		return this;
	}

	public String getDescriptionLike() {
		if (descriptionLike != null && descriptionLike.trim().length() > 0) {
			if (!descriptionLike.startsWith("%")) {
				descriptionLike = "%" + descriptionLike;
			}
			if (!descriptionLike.endsWith("%")) {
				descriptionLike = descriptionLike + "%";
			}
		}
		return descriptionLike;
	}

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("uri".equals(sortColumn)) {
				orderBy = "E.URI_" + a_x;
			}

			if ("limit".equals(sortColumn)) {
				orderBy = "E.LIMIT_" + a_x;
			}

			if ("total".equals(sortColumn)) {
				orderBy = "E.TOTAL_" + a_x;
			}

			if ("title".equals(sortColumn)) {
				orderBy = "E.TITLE_" + a_x;
			}

			if ("description".equals(sortColumn)) {
				orderBy = "E.DESCRIPTION_" + a_x;
			}

		}
		return orderBy;
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

	public String getUri() {
		return uri;
	}

	public String getUriLike() {
		if (uriLike != null && uriLike.trim().length() > 0) {
			if (!uriLike.startsWith("%")) {
				uriLike = "%" + uriLike;
			}
			if (!uriLike.endsWith("%")) {
				uriLike = uriLike + "%";
			}
		}
		return uriLike;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("uri", "URI_");
		addColumn("limit", "LIMIT_");
		addColumn("total", "TOTAL_");
		addColumn("title", "TITLE_");
		addColumn("description", "DESCRIPTION_");
	}

	public void setDescriptionLike(String descriptionLike) {
		this.descriptionLike = descriptionLike;
	}

	public void setTitleLike(String titleLike) {
		this.titleLike = titleLike;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public void setUriLike(String uriLike) {
		this.uriLike = uriLike;
	}

	public AccessUriQuery titleLike(String titleLike) {
		if (titleLike == null) {
			throw new RuntimeException("title is null");
		}
		this.titleLike = titleLike;
		return this;
	}

	public AccessUriQuery uri(String uri) {
		if (uri == null) {
			throw new RuntimeException("uri is null");
		}
		this.uri = uri;
		return this;
	}

	public AccessUriQuery uriLike(String uriLike) {
		if (uriLike == null) {
			throw new RuntimeException("uri is null");
		}
		this.uriLike = uriLike;
		return this;
	}

}