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

package com.glaf.base.info.query;

import java.util.*;

import com.glaf.core.query.DataQuery;

public class InfoCategoryQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected List<String> treeIds;
	protected Integer sortGreaterThanOrEqual;
	protected Integer sortLessThanOrEqual;
	protected String icon;
	protected String iconLike;
	protected String iconCls;
	protected String iconClsLike;
	protected String indexIcon;
	protected String indexIconLike;
	protected Integer indexShow;
	protected String name;
	protected String nameLike;
	protected List<String> names;
	protected String code;
	protected String codeLike;
	protected List<String> codes;
	protected String descLike;
	protected String type;
	protected String eventType;
	protected String eventTypeLike;
	protected List<String> eventTypes;
	protected String urlLike;
	protected List<String> uuids;
	protected Date createDateGreaterThanOrEqual;
	protected Date createDateLessThanOrEqual;

	public InfoCategoryQuery() {

	}

	public InfoCategoryQuery code(String code) {
		if (code == null) {
			throw new RuntimeException("code is null");
		}
		this.code = code;
		return this;
	}

	public InfoCategoryQuery codeLike(String codeLike) {
		if (codeLike == null) {
			throw new RuntimeException("code is null");
		}
		this.codeLike = codeLike;
		return this;
	}

	public InfoCategoryQuery codes(List<String> codes) {
		if (codes == null) {
			throw new RuntimeException("codes is empty ");
		}
		this.codes = codes;
		return this;
	}

	public InfoCategoryQuery createDateGreaterThanOrEqual(Date createDateGreaterThanOrEqual) {
		if (createDateGreaterThanOrEqual == null) {
			throw new RuntimeException("createDate is null");
		}
		this.createDateGreaterThanOrEqual = createDateGreaterThanOrEqual;
		return this;
	}

	public InfoCategoryQuery createDateLessThanOrEqual(Date createDateLessThanOrEqual) {
		if (createDateLessThanOrEqual == null) {
			throw new RuntimeException("createDate is null");
		}
		this.createDateLessThanOrEqual = createDateLessThanOrEqual;
		return this;
	}

	public InfoCategoryQuery descLike(String descLike) {
		if (descLike == null) {
			throw new RuntimeException("desc is null");
		}
		this.descLike = descLike;
		return this;
	}

	public InfoCategoryQuery eventType(String eventType) {
		if (eventType == null) {
			throw new RuntimeException("eventType is null");
		}
		this.eventType = eventType;
		return this;
	}

	public InfoCategoryQuery eventTypeLike(String eventTypeLike) {
		if (eventTypeLike == null) {
			throw new RuntimeException("eventType is null");
		}
		this.eventTypeLike = eventTypeLike;
		return this;
	}

	public InfoCategoryQuery eventTypes(List<String> eventTypes) {
		if (eventTypes == null) {
			throw new RuntimeException("eventTypes is empty ");
		}
		this.eventTypes = eventTypes;
		return this;
	}

	public String getCode() {
		return code;
	}

	public String getCodeLike() {
		if (codeLike != null && codeLike.trim().length() > 0) {
			if (!codeLike.startsWith("%")) {
				codeLike = "%" + codeLike;
			}
			if (!codeLike.endsWith("%")) {
				codeLike = codeLike + "%";
			}
		}
		return codeLike;
	}

	public List<String> getCodes() {
		return codes;
	}

	public Date getCreateDateGreaterThanOrEqual() {
		return createDateGreaterThanOrEqual;
	}

	public Date getCreateDateLessThanOrEqual() {
		return createDateLessThanOrEqual;
	}

	public String getDescLike() {
		if (descLike != null && descLike.trim().length() > 0) {
			if (!descLike.startsWith("%")) {
				descLike = "%" + descLike;
			}
			if (!descLike.endsWith("%")) {
				descLike = descLike + "%";
			}
		}
		return descLike;
	}

	public String getEventType() {
		return eventType;
	}

	public String getEventTypeLike() {
		if (eventTypeLike != null && eventTypeLike.trim().length() > 0) {
			if (!eventTypeLike.startsWith("%")) {
				eventTypeLike = "%" + eventTypeLike;
			}
			if (!eventTypeLike.endsWith("%")) {
				eventTypeLike = eventTypeLike + "%";
			}
		}
		return eventTypeLike;
	}

	public List<String> getEventTypes() {
		return eventTypes;
	}

	public String getIcon() {
		return icon;
	}

	public String getIconCls() {
		return iconCls;
	}

	public String getIconClsLike() {
		if (iconClsLike != null && iconClsLike.trim().length() > 0) {
			if (!iconClsLike.startsWith("%")) {
				iconClsLike = "%" + iconClsLike;
			}
			if (!iconClsLike.endsWith("%")) {
				iconClsLike = iconClsLike + "%";
			}
		}
		return iconClsLike;
	}

	public String getIconLike() {
		if (iconLike != null && iconLike.trim().length() > 0) {
			if (!iconLike.startsWith("%")) {
				iconLike = "%" + iconLike;
			}
			if (!iconLike.endsWith("%")) {
				iconLike = iconLike + "%";
			}
		}
		return iconLike;
	}

	public String getIndexIcon() {
		return indexIcon;
	}

	public String getIndexIconLike() {
		if (indexIconLike != null && indexIconLike.trim().length() > 0) {
			if (!indexIconLike.startsWith("%")) {
				indexIconLike = "%" + indexIconLike;
			}
			if (!indexIconLike.endsWith("%")) {
				indexIconLike = indexIconLike + "%";
			}
		}
		return indexIconLike;
	}

	public Integer getIndexShow() {
		return indexShow;
	}

	public String getName() {
		return name;
	}

	public String getNameLike() {
		if (nameLike != null && nameLike.trim().length() > 0) {
			if (!nameLike.startsWith("%")) {
				nameLike = "%" + nameLike;
			}
			if (!nameLike.endsWith("%")) {
				nameLike = nameLike + "%";
			}
		}
		return nameLike;
	}

	public List<String> getNames() {
		return names;
	}

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("parentId".equals(sortColumn)) {
				orderBy = "E.PARENT_" + a_x;
			}

			if ("treeId".equals(sortColumn)) {
				orderBy = "E.TREEID_" + a_x;
			}

			if ("sort".equals(sortColumn)) {
				orderBy = "E.SORT_" + a_x;
			}

			if ("icon".equals(sortColumn)) {
				orderBy = "E.ICON_" + a_x;
			}

			if ("iconCls".equals(sortColumn)) {
				orderBy = "E.ICONCLS_" + a_x;
			}

			if ("indexIcon".equals(sortColumn)) {
				orderBy = "E.INDEXICON_" + a_x;
			}

			if ("indexShow".equals(sortColumn)) {
				orderBy = "E.INDEXSHOW_" + a_x;
			}

			if ("locked".equals(sortColumn)) {
				orderBy = "E.LOCKED_" + a_x;
			}

			if ("name".equals(sortColumn)) {
				orderBy = "E.NAME_" + a_x;
			}

			if ("code".equals(sortColumn)) {
				orderBy = "E.CODE_" + a_x;
			}

			if ("desc".equals(sortColumn)) {
				orderBy = "E.DESC_" + a_x;
			}

			if ("eventType".equals(sortColumn)) {
				orderBy = "E.EVENTTYPE_" + a_x;
			}

			if ("url".equals(sortColumn)) {
				orderBy = "E.URL_" + a_x;
			}

			if ("createBy".equals(sortColumn)) {
				orderBy = "E.CREATEBY_" + a_x;
			}

			if ("createDate".equals(sortColumn)) {
				orderBy = "E.CREATEDATE_" + a_x;
			}

		}
		return orderBy;
	}

	public Integer getSortGreaterThanOrEqual() {
		return sortGreaterThanOrEqual;
	}

	public Integer getSortLessThanOrEqual() {
		return sortLessThanOrEqual;
	}

	public String getTreeId() {
		return treeId;
	}

	public List<String> getTreeIds() {
		return treeIds;
	}

	public String getType() {
		return type;
	}

	public String getUrlLike() {
		if (urlLike != null && urlLike.trim().length() > 0) {
			if (!urlLike.startsWith("%")) {
				urlLike = "%" + urlLike;
			}
			if (!urlLike.endsWith("%")) {
				urlLike = urlLike + "%";
			}
		}
		return urlLike;
	}

	public List<String> getUuids() {
		return uuids;
	}

	public InfoCategoryQuery icon(String icon) {
		if (icon == null) {
			throw new RuntimeException("icon is null");
		}
		this.icon = icon;
		return this;
	}

	public InfoCategoryQuery iconCls(String iconCls) {
		if (iconCls == null) {
			throw new RuntimeException("iconCls is null");
		}
		this.iconCls = iconCls;
		return this;
	}

	public InfoCategoryQuery iconClsLike(String iconClsLike) {
		if (iconClsLike == null) {
			throw new RuntimeException("iconCls is null");
		}
		this.iconClsLike = iconClsLike;
		return this;
	}

	public InfoCategoryQuery iconLike(String iconLike) {
		if (iconLike == null) {
			throw new RuntimeException("icon is null");
		}
		this.iconLike = iconLike;
		return this;
	}

	public InfoCategoryQuery indexIcon(String indexIcon) {
		if (indexIcon == null) {
			throw new RuntimeException("indexIcon is null");
		}
		this.indexIcon = indexIcon;
		return this;
	}

	public InfoCategoryQuery indexIconLike(String indexIconLike) {
		if (indexIconLike == null) {
			throw new RuntimeException("indexIcon is null");
		}
		this.indexIconLike = indexIconLike;
		return this;
	}

	public InfoCategoryQuery indexShow(Integer indexShow) {
		if (indexShow == null) {
			throw new RuntimeException("indexShow is null");
		}
		this.indexShow = indexShow;
		return this;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("parentId", "PARENT_");
		addColumn("treeId", "TREEID_");
		addColumn("sort", "SORT_");
		addColumn("icon", "ICON_");
		addColumn("iconCls", "ICONCLS_");
		addColumn("indexIcon", "INDEXICON_");
		addColumn("indexShow", "INDEXSHOW_");
		addColumn("locked", "LOCKED_");
		addColumn("name", "NAME_");
		addColumn("code", "CODE_");
		addColumn("desc", "DESC_");
		addColumn("eventType", "EVENTTYPE_");
		addColumn("url", "URL_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createDate", "CREATEDATE_");
	}

	public InfoCategoryQuery name(String name) {
		if (name == null) {
			throw new RuntimeException("name is null");
		}
		this.name = name;
		return this;
	}

	public InfoCategoryQuery nameLike(String nameLike) {
		if (nameLike == null) {
			throw new RuntimeException("name is null");
		}
		this.nameLike = nameLike;
		return this;
	}

	public InfoCategoryQuery names(List<String> names) {
		if (names == null) {
			throw new RuntimeException("names is empty ");
		}
		this.names = names;
		return this;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setCodeLike(String codeLike) {
		this.codeLike = codeLike;
	}

	public void setCodes(List<String> codes) {
		this.codes = codes;
	}

	public void setCreateDateGreaterThanOrEqual(Date createDateGreaterThanOrEqual) {
		this.createDateGreaterThanOrEqual = createDateGreaterThanOrEqual;
	}

	public void setCreateDateLessThanOrEqual(Date createDateLessThanOrEqual) {
		this.createDateLessThanOrEqual = createDateLessThanOrEqual;
	}

	public void setDescLike(String descLike) {
		this.descLike = descLike;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public void setEventTypeLike(String eventTypeLike) {
		this.eventTypeLike = eventTypeLike;
	}

	public void setEventTypes(List<String> eventTypes) {
		this.eventTypes = eventTypes;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public void setIconClsLike(String iconClsLike) {
		this.iconClsLike = iconClsLike;
	}

	public void setIconLike(String iconLike) {
		this.iconLike = iconLike;
	}

	public void setIndexIcon(String indexIcon) {
		this.indexIcon = indexIcon;
	}

	public void setIndexIconLike(String indexIconLike) {
		this.indexIconLike = indexIconLike;
	}

	public void setIndexShow(Integer indexShow) {
		this.indexShow = indexShow;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNameLike(String nameLike) {
		this.nameLike = nameLike;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}

	public void setSortGreaterThanOrEqual(Integer sortGreaterThanOrEqual) {
		this.sortGreaterThanOrEqual = sortGreaterThanOrEqual;
	}

	public void setSortLessThanOrEqual(Integer sortLessThanOrEqual) {
		this.sortLessThanOrEqual = sortLessThanOrEqual;
	}

	public void setTreeId(String treeId) {
		this.treeId = treeId;
	}

	public void setTreeIds(List<String> treeIds) {
		this.treeIds = treeIds;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUrlLike(String urlLike) {
		this.urlLike = urlLike;
	}

	public void setUuids(List<String> uuids) {
		this.uuids = uuids;
	}

	public InfoCategoryQuery sortGreaterThanOrEqual(Integer sortGreaterThanOrEqual) {
		if (sortGreaterThanOrEqual == null) {
			throw new RuntimeException("sort is null");
		}
		this.sortGreaterThanOrEqual = sortGreaterThanOrEqual;
		return this;
	}

	public InfoCategoryQuery sortLessThanOrEqual(Integer sortLessThanOrEqual) {
		if (sortLessThanOrEqual == null) {
			throw new RuntimeException("sort is null");
		}
		this.sortLessThanOrEqual = sortLessThanOrEqual;
		return this;
	}

	public InfoCategoryQuery treeId(String treeId) {
		if (treeId == null) {
			throw new RuntimeException("treeId is null");
		}
		this.treeId = treeId;
		return this;
	}

	public InfoCategoryQuery treeIds(List<String> treeIds) {
		if (treeIds == null) {
			throw new RuntimeException("treeIds is empty ");
		}
		this.treeIds = treeIds;
		return this;
	}

	public InfoCategoryQuery type(String type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		this.type = type;
		return this;
	}

	public InfoCategoryQuery urlLike(String urlLike) {
		if (urlLike == null) {
			throw new RuntimeException("url is null");
		}
		this.urlLike = urlLike;
		return this;
	}

	public InfoCategoryQuery uuids(List<String> uuids) {
		if (uuids == null) {
			throw new RuntimeException("uuids is empty ");
		}
		this.uuids = uuids;
		return this;
	}

}