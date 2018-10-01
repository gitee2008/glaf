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

package com.glaf.matrix.export.domain;

import java.io.*;
import java.util.*;
import javax.persistence.*;
import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.glaf.core.base.*;
import com.glaf.core.util.DateUtils;
import com.glaf.matrix.export.util.*;

/**
 * 
 * 实体对象
 *
 */

@Entity
@Table(name = "SYS_XML_EXPORT_ITEM")
public class XmlExportItem implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", length = 50, nullable = false)
	protected String id;

	/**
	 * 主数据编号
	 */
	@Column(name = "EXPID_", length = 50)
	protected String expId;

	/**
	 * 名称
	 */
	@Column(name = "NAME_", length = 50)
	protected String name;

	/**
	 * 标题
	 */
	@Column(name = "TITLE_", length = 200)
	protected String title;

	/**
	 * 属性A，子元素E
	 */
	@Column(name = "TAGFLAG_", length = 1)
	protected String tagFlag;

	/**
	 * 表达式
	 */
	@Column(name = "EXPRESSION_", length = 500)
	protected String expression;

	/**
	 * 默认值
	 */
	@Column(name = "DEFAULTVALUE_", length = 500)
	protected String defaultValue;

	@Column(name = "DATATYPE_", length = 20)
	protected String dataType;

	/**
	 * 是否必须
	 */
	@Column(name = "REQUIRED_", length = 1)
	protected String required;

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

	@javax.persistence.Transient
	protected Collection<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

	public XmlExportItem() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		XmlExportItem other = (XmlExportItem) obj;
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

	public Collection<Map<String, Object>> getDataList() {
		return dataList;
	}

	public String getDataType() {
		return dataType;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public String getExpId() {
		return this.expId;
	}

	public String getExpression() {
		return expression;
	}

	public String getId() {
		return this.id;
	}

	public int getLocked() {
		return locked;
	}

	public String getName() {
		if (name != null) {
			name = name.trim();
		}
		return name;
	}

	public String getRequired() {
		return required;
	}

	public int getSortNo() {
		return sortNo;
	}

	public String getTagFlag() {
		return tagFlag;
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

	public XmlExportItem jsonToObject(JSONObject jsonObject) {
		return XmlExportItemJsonFactory.jsonToObject(jsonObject);
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setDataList(Collection<Map<String, Object>> dataList) {
		this.dataList = dataList;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public void setExpId(String expId) {
		this.expId = expId;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLocked(int locked) {
		this.locked = locked;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	public void setTagFlag(String tagFlag) {
		this.tagFlag = tagFlag;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public JSONObject toJsonObject() {
		return XmlExportItemJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return XmlExportItemJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
