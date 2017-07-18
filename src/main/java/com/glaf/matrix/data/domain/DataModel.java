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

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DataModel implements Serializable {

	private static final long serialVersionUID = 1L;

	protected long id;

	protected long parentId;

	protected long topId;

	protected String uuid;

	protected String name;

	protected String code;

	protected String desc;

	protected String discriminator;

	protected String icon;

	protected String iconCls;

	protected int level;

	protected String treeId;

	protected String title;

	protected String type;

	protected String tenantId;

	protected long organizationId;

	protected String gradeId;

	protected int sortNo;

	protected int businessStatus;

	protected String approver;

	protected Date approvalDate;

	protected String createBy;

	protected Date createTime;

	protected String updateBy;

	protected Date updateTime;

	protected int deleteFlag;

	protected Date deleteTime;

	protected Map<String, Object> dataMap = new HashMap<String, Object>();

	public DataModel() {

	}

	public Date getApprovalDate() {
		return approvalDate;
	}

	public String getApprover() {
		return approver;
	}

	public int getBusinessStatus() {
		return businessStatus;
	}

	public String getCode() {
		return code;
	}

	public String getCreateBy() {
		return createBy;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public Map<String, Object> getDataMap() {
		return dataMap;
	}

	public int getDeleteFlag() {
		return deleteFlag;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}

	public String getDesc() {
		return desc;
	}

	public String getDiscriminator() {
		return discriminator;
	}

	public String getGradeId() {
		return gradeId;
	}

	public String getIcon() {
		return icon;
	}

	public String getIconCls() {
		return iconCls;
	}

	public long getId() {
		return id;
	}

	public int getLevel() {
		return level;
	}

	public String getName() {
		return name;
	}

	public long getOrganizationId() {
		return organizationId;
	}

	public long getParentId() {
		return parentId;
	}

	public int getSortNo() {
		return sortNo;
	}

	public String getTenantId() {
		return tenantId;
	}

	public String getTitle() {
		return title;
	}

	public long getTopId() {
		return topId;
	}

	public String getTreeId() {
		return treeId;
	}

	public String getType() {
		return type;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public String getUuid() {
		return uuid;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public void setApprover(String approver) {
		this.approver = approver;
	}

	public void setBusinessStatus(int businessStatus) {
		this.businessStatus = businessStatus;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}

	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setDiscriminator(String discriminator) {
		this.discriminator = discriminator;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTopId(long topId) {
		this.topId = topId;
	}

	public void setTreeId(String treeId) {
		this.treeId = treeId;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
