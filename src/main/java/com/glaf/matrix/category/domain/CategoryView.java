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

package com.glaf.matrix.category.domain;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.*;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.alibaba.fastjson.JSONObject;
import com.glaf.matrix.category.util.CategoryJsonFactory;
import com.glaf.core.base.TreeModel;
import com.glaf.core.util.DateUtils;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 
 * 实体对象
 *
 */

@Entity
@Table(name = "SYS_CATEGORY_VIEW")
public class CategoryView implements Serializable, TreeModel {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "_UID_", length = 50)
	protected String uid;

	@Column(name = "ID_", nullable = false)
	protected long id;

	/**
	 * 父编号
	 */
	@Column(name = "PARENTID_")
	protected long parentId;

	/**
	 * 名称
	 */
	@Column(name = "NAME_", length = 200)
	protected String name;

	/**
	 * 代码
	 */
	@Column(name = "CODE_", length = 50)
	protected String code;

	/**
	 * 描述
	 */
	@Column(name = "DESC_", length = 500)
	protected String description;

	/**
	 * 识别码
	 */
	@Column(name = "DISCRIMINATOR_", length = 10)
	protected String discriminator;

	/**
	 * 图标
	 */
	@Column(name = "ICON_", length = 50)
	protected String icon;

	/**
	 * 图标样式
	 */
	@Column(name = "ICONCLS_", length = 50)
	protected String iconCls;

	/**
	 * 层级
	 */
	@Column(name = "LEVEL_")
	protected int level;

	/**
	 * 是否锁定
	 */
	@Column(name = "LOCKED_")
	protected int locked;

	/**
	 * 树编号
	 */
	@Column(name = "TREEID_", length = 500)
	protected String treeId;

	/**
	 * 标题
	 */
	@Column(name = "TITLE_", length = 100)
	protected String title;

	/**
	 * 类型
	 */
	@Column(name = "TYPE_", length = 50)
	protected String type;

	/**
	 * 顺序
	 */
	@Column(name = "SORTNO_")
	protected int sort;

	/**
	 * 用户名
	 */
	@Column(name = "USERID_", length = 50)
	protected String userId;

	/**
	 * 用户姓名
	 */
	@Column(name = "USERNAME_", length = 200)
	protected String userName;

	/**
	 * 机构名称
	 */
	@Column(name = "ORGNAME_", length = 200)
	protected String orgName;

	/**
	 * 链接地址
	 */
	@Column(name = "URL_", length = 500)
	protected String url;

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

	/**
	 * 更新人
	 */
	@Column(name = "UPDATEBY_", length = 50)
	protected String updateBy;

	/**
	 * 更新时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATETIME_")
	protected Date updateTime;

	@javax.persistence.Transient
	protected TreeModel parent;

	@javax.persistence.Transient
	protected Map<String, Object> dataMap;

	@javax.persistence.Transient
	protected List<TreeModel> children = new ArrayList<TreeModel>();

	public CategoryView() {

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
		CategoryView other = (CategoryView) obj;
		if (uid == null) {
			if (other.uid != null)
				return false;
		} else if (!uid.equals(other.uid))
			return false;
		return true;
	}

	public List<TreeModel> getChildren() {
		return children;
	}

	public String getCode() {
		return this.code;
	}

	public String getCreateBy() {
		return this.createBy;
	}

	public Date getCreateDate() {
		return createTime;
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

	public Map<String, Object> getDataMap() {
		return dataMap;
	}

	public String getDescription() {
		return description;
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

	public String getName() {
		return this.name;
	}

	public String getOrgName() {
		return orgName;
	}

	public TreeModel getParent() {
		return parent;
	}

	public long getParentId() {
		return parentId;
	}

	public int getSort() {
		return sort;
	}

	public int getSortNo() {
		return sort;
	}

	public String getTitle() {
		return this.title;
	}

	public String getTreeId() {
		return treeId;
	}

	public String getType() {
		return type;
	}

	public String getUid() {
		return uid;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public Date getUpdateDate() {
		return updateTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public String getUrl() {
		return url;
	}

	public String getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
		return result;
	}

	public boolean isChecked() {

		return false;
	}

	public Category jsonToObject(JSONObject jsonObject) {
		return CategoryJsonFactory.jsonToObject(jsonObject);
	}

	public void removeChild(TreeModel treeModel) {

	}

	public void setChecked(boolean checked) {

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
		this.createTime = createDate;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public void setName(String name) {
		this.name = name;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public void setParent(TreeModel parent) {
		this.parent = parent;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public void setSortNo(int sortNo) {
		this.sort = sortNo;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTreeId(String treeId) {
		this.treeId = treeId;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateTime = updateDate;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public JSONObject toJsonObject() {
		return null;
	}

	public ObjectNode toObjectNode() {
		return null;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
