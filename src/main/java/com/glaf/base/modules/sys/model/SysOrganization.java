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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.*;

import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.base.modules.sys.util.SysOrganizationJsonFactory;
import com.glaf.core.base.JSONable;
import com.glaf.core.base.TreeModel;

@Entity
@Table(name = "SYS_ORGANIZATION")
public class SysOrganization implements Serializable, JSONable, TreeModel {
	private static final long serialVersionUID = -1700125499848402378L;

	@Id
	@Column(name = "ID", nullable = false)
	protected long id;

	@Column(name = "PARENTID")
	private long parentId;

	/**
	 * 租户编号
	 */
	@Column(name = "TENANTID", length = 50)
	protected String tenantId;

	/**
	 * 编码
	 */
	@Column(name = "CODE", length = 250)
	protected String code;

	/**
	 * 编码2
	 */
	@Column(name = "CODE2", length = 250)
	protected String code2;

	/**
	 * 鉴别符
	 */
	@Column(name = "DISCRIMINATOR", length = 10)
	protected String discriminator;

	/**
	 * 图标
	 */
	@Column(name = "ICON", length = 50)
	protected String icon;

	/**
	 * 图标样式
	 */
	@Column(name = "ICONCLS", length = 50)
	protected String iconCls;

	/**
	 * 树型结构编号
	 */
	@Column(name = "TREEID", length = 500)
	protected String treeId;

	/**
	 * 创建人
	 */
	@Column(name = "CREATEBY", length = 50)
	protected String createBy;

	/**
	 * 创建日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME")
	protected Date createTime;

	/**
	 * 描述
	 */
	@Column(name = "ORG_DESC", length = 500)
	protected String description;

	/**
	 * 财务代码
	 */
	@Column(name = "FINCODE", length = 250)
	protected String fincode;

	/**
	 * 机构级别
	 */
	@Column(name = "LEVEL")
	protected int level;

	/**
	 * 名称
	 */
	@Column(name = "NAME", length = 200)
	protected String name;

	/**
	 * 名称拼音
	 */
	@Column(name = "NAMEPINYIN", length = 200)
	protected String namePinyin;

	/**
	 * 地址
	 */
	@Column(name = "ADDRESS", length = 250)
	protected String address;

	/**
	 * 电话
	 */
	@Column(name = "TELPHONE", length = 100)
	protected String telphone;

	/**
	 * 负责人
	 */
	@Column(name = "PRINCIPAL", length = 200)
	protected String principal;

	/**
	 * 部门编号
	 */
	@Column(name = "ORG_NO")
	protected String no;

	/**
	 * 序号
	 */
	@Column(name = "SORTNO")
	protected int sort;

	/**
	 * 状态
	 */
	@Column(name = "LOCKED")
	protected int locked = 0;// 是否有效[默认有效]

	/**
	 * 结构类型
	 */
	@Column(name = "TYPE", length = 50)
	protected String type;

	/**
	 * 链接地址
	 */
	@Column(name = "URL", length = 500)
	protected String url;

	@Column(name = "DELETEFLAG")
	protected int deleteFlag;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DELETETIME")
	protected Date deleteTime;

	/**
	 * 修改人
	 */
	@Column(name = "UPDATEBY", length = 50)
	protected String updateBy;

	/**
	 * 修改日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATEDATE")
	protected Date updateDate;

	@javax.persistence.Transient
	protected String blank;

	@javax.persistence.Transient
	private TreeModel parent;

	@javax.persistence.Transient
	protected Map<String, Object> dataMap;

	@javax.persistence.Transient
	protected List<TreeModel> children = new ArrayList<TreeModel>();

	public SysOrganization() {

	}

	public void addChild(TreeModel organization) {
		if (children == null) {
			children = new ArrayList<TreeModel>();
		}
		organization.setParent(this);
		children.add(organization);
	}

	public int compareTo(TreeModel o) {
		if (o == null) {
			return -1;
		}

		TreeModel obj = o;

		int l = this.sort - obj.getSortNo();

		int ret = 0;

		if (l > 0) {
			ret = 1;
		} else if (l < 0) {
			ret = -1;
		}
		return ret;
	}

	public String getAddress() {
		return address;
	}

	public String getBlank() {
		return blank;
	}

	public List<TreeModel> getChildren() {
		return children;
	}

	public String getCode() {
		return code;
	}

	public String getCode2() {
		return code2;
	}

	public String getCreateBy() {
		return createBy;
	}

	public Date getCreateDate() {
		return createTime;
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

	public String getDescription() {
		return description;
	}

	public String getDiscriminator() {
		return discriminator;
	}

	public String getFincode() {
		return fincode;
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

	public int getLocked() {
		return locked;
	}

	public String getName() {
		return name;
	}

	public String getNamePinyin() {
		return namePinyin;
	}

	public String getNo() {
		return no;
	}

	public TreeModel getParent() {
		return parent;
	}

	public long getParentId() {
		return parentId;
	}

	public String getPrincipal() {
		return principal;
	}

	public int getSort() {
		return sort;
	}

	public int getSortNo() {
		return sort;
	}

	public String getTelphone() {
		return telphone;
	}

	public String getTenantId() {
		return tenantId;
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

	public Date getUpdateDate() {
		return updateDate;
	}

	public String getUrl() {
		return url;
	}

	public boolean isChecked() {
		return false;
	}

	public SysOrganization jsonToObject(JSONObject jsonObject) {
		return SysOrganizationJsonFactory.jsonToObject(jsonObject);
	}

	public void removeChild(TreeModel treeModel) {

	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setBlank(String blank) {
		this.blank = blank;
	}

	public void setChecked(boolean checked) {

	}

	public void setChildren(List<TreeModel> children) {
		this.children = children;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setCode2(String code2) {
		this.code2 = code2;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateDate(Date createDate) {
		this.createTime = createDate;
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

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDiscriminator(String discriminator) {
		this.discriminator = discriminator;
	}

	public void setFincode(String fincode) {
		this.fincode = fincode;
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

	public void setLocked(int locked) {
		this.locked = locked;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNamePinyin(String namePinyin) {
		this.namePinyin = namePinyin;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public void setParent(TreeModel parent) {
		this.parent = parent;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public void setSortNo(int sortNo) {
		this.sort = sortNo;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
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

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public JSONObject toJsonObject() {
		return SysOrganizationJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return SysOrganizationJsonFactory.toObjectNode(this);
	}

	public String toString() {
		return toJsonObject().toJSONString();
	}

}