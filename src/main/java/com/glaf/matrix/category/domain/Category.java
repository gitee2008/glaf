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
import java.util.*;
import javax.persistence.*;
import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.glaf.core.base.*;
import com.glaf.core.util.DateUtils;
import com.glaf.matrix.category.util.*;

/**
 * 
 * 实体对象
 *
 */

@Entity
@Table(name = "SYS_CATEGORY")
public class Category implements Serializable, JSONable, TreeModel {
	private static final long serialVersionUID = 1L;

	@Id
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
	@Column(name = "ICON_", length = 250)
	protected String icon;

	/**
	 * 图标样式
	 */
	@Column(name = "ICONCLS_", length = 250)
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
	@Column(name = "TITLE_", length = 200)
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
	 * 从属节点编号
	 */
	@Column(name = "SUBIDS_", length = 500)
	protected String subIds;

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
	protected Collection<Long> subordinateIds = new HashSet<Long>();

	@javax.persistence.Transient
	protected Collection<String> actorIds = new HashSet<String>();

	@javax.persistence.Transient
	protected List<CategoryAccess> accesses = new ArrayList<CategoryAccess>();

	@javax.persistence.Transient
	protected List<CategoryOwner> owners = new ArrayList<CategoryOwner>();

	@javax.persistence.Transient
	protected TreeModel parent;

	@javax.persistence.Transient
	protected Map<String, Object> dataMap;

	@javax.persistence.Transient
	protected List<TreeModel> children = new ArrayList<TreeModel>();

	public Category() {

	}

	public void addAccessor(String actorId) {
		if (actorIds == null) {
			actorIds = new HashSet<String>();
		}
		actorIds.add(actorId);
	}

	public void addChild(TreeModel treeModel) {
		if (children == null) {
			children = new ArrayList<TreeModel>();
		}
		children.add(treeModel);
	}

	public void addOwner(CategoryOwner owner) {
		if (owners == null) {
			owners = new ArrayList<CategoryOwner>();
		}
		owners.add(owner);
	}

	public void addSubordinate(Long subordinateId) {
		if (subordinateIds == null) {
			subordinateIds = new HashSet<Long>();
		}
		subordinateIds.add(subordinateId);
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
		Category other = (Category) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public List<CategoryAccess> getAccesses() {
		return accesses;
	}

	public Collection<String> getActorIds() {
		return actorIds;
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

	public List<CategoryOwner> getOwners() {
		return owners;
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

	public String getSubIds() {
		return subIds;
	}

	public Collection<Long> getSubordinateIds() {
		return subordinateIds;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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

	public void setAccesses(List<CategoryAccess> accesses) {
		this.accesses = accesses;
	}

	public void setActorIds(Collection<String> actorIds) {
		this.actorIds = actorIds;
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

	public void setOwners(List<CategoryOwner> owners) {
		this.owners = owners;
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

	public void setSubIds(String subIds) {
		this.subIds = subIds;
	}

	public void setSubordinateIds(Collection<Long> subordinateIds) {
		this.subordinateIds = subordinateIds;
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

	public JSONObject toJsonObject() {
		return CategoryJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return CategoryJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
