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

import java.util.Date;
import java.util.List;

import com.glaf.core.query.DataQuery;

public class DataFileQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected String id;
	protected String businessKeyIsNull;
	protected String contentType;
	protected Date createDateGreaterThanOrEqual;
	protected Date createDateLessThanOrEqual;
	protected Long databaseId;
	protected String deviceId;
	protected String fileId;
	protected String filename;
	protected String filenameLike;
	protected Long lastModifiedGreaterThanOrEqual;
	protected Long lastModifiedLessThanOrEqual;
	protected String path;
	protected String pathLike;
	protected String name;
	protected String nameLike;
	protected List<String> names;
	protected List<String> objectIds;
	protected List<String> objectValues;
	protected Long size;
	protected Long sizeGreaterThanOrEqual;
	protected Long sizeLessThanOrEqual;
	protected String type;
	protected String tableSuffix;

	public DataFileQuery() {

	}

	public DataFileQuery contentType(String contentType) {
		if (contentType == null) {
			throw new RuntimeException("contentType is null");
		}
		this.contentType = contentType;
		return this;
	}

	public DataFileQuery createDateGreaterThanOrEqual(Date createDateGreaterThanOrEqual) {
		if (createDateGreaterThanOrEqual == null) {
			throw new RuntimeException("createDate is null");
		}
		this.createDateGreaterThanOrEqual = createDateGreaterThanOrEqual;
		return this;
	}

	public DataFileQuery createDateLessThanOrEqual(Date createDateLessThanOrEqual) {
		if (createDateLessThanOrEqual == null) {
			throw new RuntimeException("createDate is null");
		}
		this.createDateLessThanOrEqual = createDateLessThanOrEqual;
		return this;
	}

	public DataFileQuery databaseId(Long databaseId) {
		if (databaseId == null) {
			throw new RuntimeException("databaseId is null");
		}
		this.databaseId = databaseId;
		return this;
	}

	public DataFileQuery deviceId(String deviceId) {
		if (deviceId == null) {
			throw new RuntimeException("deviceId is null");
		}
		this.deviceId = deviceId;
		return this;
	}

	public DataFileQuery fileId(String fileId) {
		if (fileId == null) {
			throw new RuntimeException("fileId is null");
		}
		this.fileId = fileId;
		return this;
	}

	public DataFileQuery filename(String filename) {
		if (filename == null) {
			throw new RuntimeException("filename is null");
		}
		this.filename = filename;
		return this;
	}

	public DataFileQuery filenameLike(String filenameLike) {
		if (filenameLike == null) {
			throw new RuntimeException("filename is null");
		}
		this.filenameLike = filenameLike;
		return this;
	}

	public String getBusinessKeyIsNull() {
		return businessKeyIsNull;
	}

	public String getContentType() {
		return contentType;
	}

	public Date getCreateDateGreaterThanOrEqual() {
		return createDateGreaterThanOrEqual;
	}

	public Date getCreateDateLessThanOrEqual() {
		return createDateLessThanOrEqual;
	}

	public Long getDatabaseId() {
		return databaseId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public String getFileId() {
		return fileId;
	}

	public String getFilename() {
		return filename;
	}

	public String getFilenameLike() {
		return filenameLike;
	}

	public String getId() {
		return id;
	}

	public Long getLastModifiedGreaterThanOrEqual() {
		return lastModifiedGreaterThanOrEqual;
	}

	public Long getLastModifiedLessThanOrEqual() {
		return lastModifiedLessThanOrEqual;
	}

	public String getName() {
		return name;
	}

	public String getNameLike() {
		return nameLike;
	}

	public List<String> getNames() {
		return names;
	}

	public List<String> getObjectIds() {
		return objectIds;
	}

	public List<String> getObjectValues() {
		return objectValues;
	}

	public String getPath() {
		return path;
	}

	public String getPathLike() {
		if (pathLike != null && pathLike.trim().length() > 0) {
			if (!pathLike.endsWith("%")) {
				pathLike = pathLike + "%";
			}
		}
		return pathLike;
	}

	public Long getSize() {
		return size;
	}

	public Long getSizeGreaterThanOrEqual() {
		return sizeGreaterThanOrEqual;
	}

	public Long getSizeLessThanOrEqual() {
		return sizeLessThanOrEqual;
	}

	public String getTableSuffix() {
		if (tableSuffix == null) {
			tableSuffix = "";
		}
		return tableSuffix;
	}

	public String getType() {
		return type;
	}

	public DataFileQuery id(String id) {
		if (id == null) {
			throw new RuntimeException("id is null");
		}
		this.id = id;
		return this;
	}

	public DataFileQuery lastModifiedGreaterThanOrEqual(Long lastModifiedGreaterThanOrEqual) {
		if (lastModifiedGreaterThanOrEqual == null) {
			throw new RuntimeException("lastModified is null");
		}
		this.lastModifiedGreaterThanOrEqual = lastModifiedGreaterThanOrEqual;
		return this;
	}

	public DataFileQuery lastModifiedLessThanOrEqual(Long lastModifiedLessThanOrEqual) {
		if (lastModifiedLessThanOrEqual == null) {
			throw new RuntimeException("lastModified is null");
		}
		this.lastModifiedLessThanOrEqual = lastModifiedLessThanOrEqual;
		return this;
	}

	public DataFileQuery name(String name) {
		if (name == null) {
			throw new RuntimeException("name is null");
		}
		this.name = name;
		return this;
	}

	public DataFileQuery nameLike(String nameLike) {
		if (nameLike == null) {
			throw new RuntimeException("name is null");
		}
		this.nameLike = nameLike;
		return this;
	}

	public DataFileQuery names(List<String> names) {
		if (names == null) {
			throw new RuntimeException("name is null");
		}
		this.names = names;
		return this;
	}

	public DataFileQuery objectIds(List<String> objectIds) {
		if (objectIds == null) {
			throw new RuntimeException("objectIds is empty ");
		}
		this.objectIds = objectIds;
		return this;
	}

	public DataFileQuery objectValues(List<String> objectValues) {
		if (objectValues == null) {
			throw new RuntimeException("objectValues is empty ");
		}
		this.objectValues = objectValues;
		return this;
	}

	public DataFileQuery path(String path) {
		if (path == null) {
			throw new RuntimeException("path is null");
		}
		this.path = path;
		return this;
	}

	public DataFileQuery pathLike(String pathLike) {
		if (pathLike == null) {
			throw new RuntimeException("pathLike is null");
		}
		this.pathLike = pathLike;
		return this;
	}

	public void setBusinessKeyIsNull(String businessKeyIsNull) {
		this.businessKeyIsNull = businessKeyIsNull;
	}

	public void setBusinessKeys(List<String> businessKeys) {
		this.businessKeys = businessKeys;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setCreateDateGreaterThanOrEqual(Date createDateGreaterThanOrEqual) {
		this.createDateGreaterThanOrEqual = createDateGreaterThanOrEqual;
	}

	public void setCreateDateLessThanOrEqual(Date createDateLessThanOrEqual) {
		this.createDateLessThanOrEqual = createDateLessThanOrEqual;
	}

	public void setDatabaseId(Long databaseId) {
		this.databaseId = databaseId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setFilenameLike(String filenameLike) {
		this.filenameLike = filenameLike;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLastModifiedGreaterThanOrEqual(Long lastModifiedGreaterThanOrEqual) {
		this.lastModifiedGreaterThanOrEqual = lastModifiedGreaterThanOrEqual;
	}

	public void setLastModifiedLessThanOrEqual(Long lastModifiedLessThanOrEqual) {
		this.lastModifiedLessThanOrEqual = lastModifiedLessThanOrEqual;
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

	public void setObjectIds(List<String> objectIds) {
		this.objectIds = objectIds;
	}

	public void setObjectValues(List<String> objectValues) {
		this.objectValues = objectValues;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setPathLike(String pathLike) {
		this.pathLike = pathLike;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public void setSizeGreaterThanOrEqual(Long sizeGreaterThanOrEqual) {
		this.sizeGreaterThanOrEqual = sizeGreaterThanOrEqual;
	}

	public void setSizeLessThanOrEqual(Long sizeLessThanOrEqual) {
		this.sizeLessThanOrEqual = sizeLessThanOrEqual;
	}

	public void setTableSuffix(String tableSuffix) {
		this.tableSuffix = tableSuffix;
	}

	public void setType(String type) {
		this.type = type;
	}

	public DataFileQuery size(Long size) {
		if (size == null) {
			throw new RuntimeException("size is null");
		}
		this.size = size;
		return this;
	}

	public DataFileQuery sizeGreaterThanOrEqual(Long sizeGreaterThanOrEqual) {
		if (sizeGreaterThanOrEqual == null) {
			throw new RuntimeException("size is null");
		}
		this.sizeGreaterThanOrEqual = sizeGreaterThanOrEqual;
		return this;
	}

	public DataFileQuery sizeLessThanOrEqual(Long sizeLessThanOrEqual) {
		if (sizeLessThanOrEqual == null) {
			throw new RuntimeException("size is null");
		}
		this.sizeLessThanOrEqual = sizeLessThanOrEqual;
		return this;
	}

	public DataFileQuery status(Integer status) {
		if (status == null) {
			throw new RuntimeException("status is null");
		}
		this.status = status;
		return this;
	}

	public DataFileQuery type(String type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		this.type = type;
		return this;
	}

}