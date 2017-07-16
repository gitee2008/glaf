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

import com.glaf.base.modules.sys.util.SysTreeJsonFactory;

import com.glaf.core.base.JSONable;
import com.glaf.core.base.TreeModel;

@Entity
@Table(name = "SYS_TREE")
public class SysTree implements Serializable, TreeModel, JSONable {
	private static final long serialVersionUID = 2666681837822864771L;

	@Id
	@Column(name = "ID", nullable = false)
	protected long id;

	/**
	 * 父节点编号
	 */
	@Column(name = "PARENTID")
	protected long parentId;

	/**
	 * 编码
	 */
	@Column(name = "CODE", length = 50)
	protected String code;

	/**
	 * 节点描述
	 */
	@Column(name = "NODEDESC", length = 500)
	protected String desc;

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

	@Column(name = "LEVEL")
	protected int level;

	/**
	 * 是否启用
	 */
	@Column(name = "LOCKED")
	protected int locked;// 是否有效[默认有效0]

	@Column(name = "DELETEFLAG")
	protected int deleteFlag;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DELETETIME")
	protected Date deleteTime;

	/**
	 * 是否可以移动
	 */
	@Column(name = "MOVEABLE", length = 10)
	protected String moveable;

	/**
	 * 名称
	 */
	@Column(name = "NAME", length = 100)
	protected String name;

	/**
	 * 序号
	 */
	@Column(name = "SORTNO")
	protected int sort;

	/**
	 * 树型结构编号
	 */
	@Column(name = "TREEID", length = 500)
	protected String treeId;

	/**
	 * 允许单个文件大小
	 */
	@Column(name = "ALLOWEDFIZESIZE")
	protected int allowedFizeSize;

	/**
	 * 允许文件列表
	 */
	@Column(name = "ALLOWEDFILEEXTS", length = 200)
	protected String allowedFileExts;

	/**
	 * Provider
	 */
	@Column(name = "PROVIDERCLASS", length = 100)
	protected String providerClass;

	/**
	 * 值
	 */
	@Column(name = "VALUE_", length = 2000)
	protected String value;

	/**
	 * 创建人
	 */
	@Column(name = "CREATEBY", length = 50)
	protected String createBy;

	/**
	 * 创建日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATEDATE")
	protected Date createDate;

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

	/**
	 * 链接地址
	 */
	@Column(name = "URL", length = 500)
	protected String url;

	@javax.persistence.Transient
	protected String blank;

	@javax.persistence.Transient
	protected String cacheFlag;

	@javax.persistence.Transient
	protected boolean checked;

	@javax.persistence.Transient
	protected TreeModel parent;

	@javax.persistence.Transient
	protected SysTree parentTree;

	@javax.persistence.Transient
	protected Map<String, Object> dataMap;

	@javax.persistence.Transient
	protected List<TreeModel> children = new ArrayList<TreeModel>();

	public SysTree() {

	}

	public void addChild(TreeModel treeModel) {
		if (children == null) {
			children = new ArrayList<TreeModel>();
		}
		children.add(treeModel);
	}

	@Override
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SysTree other = (SysTree) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public String getAllowedFileExts() {
		return allowedFileExts;
	}

	public int getAllowedFizeSize() {
		return allowedFizeSize;
	}

	public String getBlank() {
		return blank;
	}

	public String getCacheFlag() {
		return cacheFlag;
	}

	public List<TreeModel> getChildren() {
		return children;
	}

	public String getCode() {
		return code;
	}

	public String getCreateBy() {
		return createBy;
	}

	public Date getCreateDate() {
		return createDate;
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

	public String getDescription() {
		return desc;
	}

	public String getDiscriminator() {
		return discriminator;
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

	public String getMoveable() {
		return moveable;
	}

	public String getName() {
		return name;
	}

	public TreeModel getParent() {
		return parent;
	}

	public long getParentId() {
		return parentId;
	}

	public SysTree getParentTree() {
		return parentTree;
	}

	public String getProviderClass() {
		return providerClass;
	}

	public int getSort() {
		return sort;
	}

	public int getSortNo() {
		return sort;
	}

	public String getTreeId() {
		return treeId;
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

	public String getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public boolean isChecked() {
		return checked;
	}

	public SysTree jsonToObject(JSONObject jsonObject) {
		return SysTreeJsonFactory.jsonToObject(jsonObject);
	}

	@Override
	public void removeChild(TreeModel treeModel) {
		if (children != null) {
			children.remove(treeModel);
		}
	}

	public void setAllowedFileExts(String allowedFileExts) {
		this.allowedFileExts = allowedFileExts;
	}

	public void setAllowedFizeSize(int allowedFizeSize) {
		this.allowedFizeSize = allowedFizeSize;
	}

	public void setBlank(String blank) {
		this.blank = blank;
	}

	public void setCacheFlag(String cacheFlag) {
		this.cacheFlag = cacheFlag;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public void setChildren(List<TreeModel> children) {
		this.children = children;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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

	public void setDescription(String description) {
		this.desc = description;
	}

	public void setDiscriminator(String discriminator) {
		this.discriminator = discriminator;
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

	public void setMoveable(String moveable) {
		this.moveable = moveable;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setParent(TreeModel parent) {
		this.parent = parent;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public void setParentTree(SysTree parentTree) {
		this.parentTree = parentTree;
	}

	public void setProviderClass(String providerClass) {
		this.providerClass = providerClass;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public void setSortNo(int sortNo) {
		this.sort = sortNo;
	}

	public void setTreeId(String treeId) {
		this.treeId = treeId;
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

	public void setValue(String value) {
		this.value = value;
	}

	public JSONObject toJsonObject() {
		return SysTreeJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return SysTreeJsonFactory.toObjectNode(this);
	}

	public String toString() {
		return toJsonObject().toJSONString();
	}

}