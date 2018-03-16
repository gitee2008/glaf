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

import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.core.base.DataFile;
import com.glaf.core.base.JSONable;
import com.glaf.matrix.data.util.DataFileJsonFactory;

public class DataFileEntity implements DataFile, Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	protected String id;

	protected long databaseId;

	/**
	 * 租户编号
	 */
	protected String tenantId;

	protected String businessKey;

	protected String serviceKey;

	protected String masterDataId;

	protected String contentType;

	protected byte[] data = null;

	protected String deviceId;

	protected String filename;

	protected long lastModified = -1;

	protected int locked = 0;

	protected String name;

	protected String objectId;

	protected String objectValue;

	protected String path = null;

	protected long size = -1;

	protected int status = 0;

	protected String type;

	protected String createBy;

	protected Date createDate;

	protected int deleteFlag = 0;

	/**
	 * 表后缀
	 */
	protected String tableSuffix;

	protected transient InputStream inputStream = null;

	public DataFileEntity() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataFileEntity other = (DataFileEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public String getContentType() {
		return contentType;
	}

	public String getCreateBy() {
		return createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public byte[] getData() {
		return data;
	}

	public long getDatabaseId() {
		return databaseId;
	}

	public int getDeleteFlag() {
		return deleteFlag;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public String getFilename() {
		return filename;
	}

	public String getId() {
		return id;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public long getLastModified() {
		return lastModified;
	}

	public int getLocked() {
		return locked;
	}

	public String getMasterDataId() {
		return masterDataId;
	}

	public String getName() {
		return name;
	}

	public String getObjectId() {
		return objectId;
	}

	public String getObjectValue() {
		return objectValue;
	}

	public String getPath() {
		return path;
	}

	public String getServiceKey() {
		return serviceKey;
	}

	public long getSize() {
		return size;
	}

	public int getStatus() {
		return status;
	}

	public String getTableSuffix() {
		if (tableSuffix == null) {
			tableSuffix = "";
		}
		return tableSuffix;
	}

	public String getTenantId() {
		return tenantId;
	}

	public String getType() {
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public DataFile jsonToObject(JSONObject jsonObject) {
		return DataFileJsonFactory.jsonToObject(jsonObject);
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public void setDatabaseId(long databaseId) {
		this.databaseId = databaseId;
	}

	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

	public void setLocked(int locked) {
		this.locked = locked;
	}

	public void setMasterDataId(String masterDataId) {
		this.masterDataId = masterDataId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public void setObjectValue(String objectValue) {
		this.objectValue = objectValue;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setServiceKey(String serviceKey) {
		this.serviceKey = serviceKey;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setTableSuffix(String tableSuffix) {
		this.tableSuffix = tableSuffix;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setType(String type) {
		this.type = type;
	}

	public JSONObject toJsonObject() {
		return DataFileJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return DataFileJsonFactory.toObjectNode(this);
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}