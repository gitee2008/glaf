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
package com.glaf.matrix.resource.domain;

import java.io.*;
import java.util.*;

import javax.persistence.*;

import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.glaf.core.base.*;
import com.glaf.core.util.DateUtils;
import com.glaf.matrix.resource.util.*;

/**
 * 页面资源，主要存储首页logo,自定义样式等静态资源
 *
 */

@Entity
@Table(name = "PAGE_RESOURCE")
public class PageResource implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", nullable = false)
	protected long id;

	/**
	 * 文件编号
	 */
	@Column(name = "FILEID_", length = 50)
	protected String resFileId;

	/**
	 * 文件名
	 */
	@Column(name = "FILENAME_", length = 250)
	protected String resFileName;

	/**
	 * 名称
	 */
	@Column(name = "NAME_", length = 250)
	protected String resName;

	/**
	 * 路径
	 */
	@Column(name = "PATH_", length = 250)
	protected String resPath;

	/**
	 * 二进制流
	 */
	@Lob
	@Column(name = "CONTENT_")
	protected byte[] resContent;

	/**
	 * 类型
	 */
	@Column(name = "TYPE_", length = 50)
	protected String resType;

	/**
	 * ContentType
	 */
	@Column(name = "CONTENTTYPE_", length = 80)
	protected String resContentType;

	/**
	 * 租户编号
	 */
	@Column(name = "TENANTID_", length = 50)
	protected String tenantId;

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

	public PageResource() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PageResource other = (PageResource) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public String getCreateBy() {
		return createBy;
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

	public byte[] getResContent() {
		return this.resContent;
	}

	public String getResContentType() {
		return this.resContentType;
	}

	public String getResFileId() {
		return resFileId;
	}

	public String getResFileName() {
		return this.resFileName;
	}

	public String getResName() {
		return this.resName;
	}

	public String getResPath() {
		return this.resPath;
	}

	public String getResType() {
		return this.resType;
	}

	public String getTenantId() {
		return tenantId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public PageResource jsonToObject(JSONObject jsonObject) {
		return PageResourceJsonFactory.jsonToObject(jsonObject);
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

	public void setResContent(byte[] resContent) {
		this.resContent = resContent;
	}

	public void setResContentType(String resContentType) {
		this.resContentType = resContentType;
	}

	public void setResFileId(String resFileId) {
		this.resFileId = resFileId;
	}

	public void setResFileName(String resFileName) {
		this.resFileName = resFileName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}

	public void setResPath(String resPath) {
		this.resPath = resPath;
	}

	public void setResType(String resType) {
		this.resType = resType;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public JSONObject toJsonObject() {
		return PageResourceJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return PageResourceJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
