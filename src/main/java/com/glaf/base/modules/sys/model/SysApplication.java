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
import com.glaf.base.modules.sys.util.SysApplicationJsonFactory;
import com.glaf.core.base.JSONable;
import com.glaf.core.base.TreeModel;

@Entity
@Table(name = "SYS_APPLICATION")
public class SysApplication implements Serializable, JSONable, TreeModel {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID", nullable = false)
	protected long id;

	@Column(name = "PARENTID")
	private long parentId;

	/**
	 * 名称
	 */
	@Column(name = "NAME", length = 250)
	protected String name;

	/**
	 * 名称拼音
	 */
	@Column(name = "NAMEPINYIN", length = 250)
	protected String namePinyin;

	/**
	 * 编码
	 */
	@Column(name = "CODE", length = 50)
	protected String code;

	/**
	 * 描述
	 */
	@Column(name = "APPDESC", length = 500)
	protected String desc;

	/**
	 * 鉴别符
	 */
	@Column(name = "DISCRIMINATOR", length = 10)
	protected String discriminator;

	/**
	 * 图标
	 */
	@Column(name = "ICON", length = 250)
	protected String icon;

	/**
	 * 图标样式
	 */
	@Column(name = "ICONCLS", length = 250)
	protected String iconCls;

	/**
	 * 树型结构编号
	 */
	@Column(name = "TREEID", length = 500)
	protected String treeId;

	@Column(name = "LEVEL")
	protected int level;

	/**
	 * 是否启用
	 */
	@Column(name = "LOCKED")
	protected int locked;// 是否有效[默认有效0]

	/**
	 * 显示菜单
	 */
	@Column(name = "SHOWMENU")
	protected int showMenu;

	/**
	 * 显示类型，B-仅后台显示， F-仅前台显示，A-前后台都显示
	 */
	@Column(name = "SHOWTYPE")
	protected String showType;

	/**
	 * 系统标识
	 */
	@Column(name = "SYSFLAG_", length = 10)
	protected String sysFlag;

	/**
	 * 序号
	 */
	@Column(name = "SORTNO")
	protected int sort;

	/**
	 * URL
	 */
	@Column(name = "URL", length = 500)
	protected String url;

	/**
	 * imagePath
	 */
	@Column(name = "IMAGEPATH", length = 200)
	protected String imagePath;

	/**
	 * linkType
	 */
	@Column(name = "LINKTYPE", length = 50)
	protected String linkType;

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

	@Column(name = "DELETEFLAG")
	protected int deleteFlag;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DELETETIME")
	protected Date deleteTime;

	@javax.persistence.Transient
	protected String blank;

	@javax.persistence.Transient
	protected TreeModel parent;

	@javax.persistence.Transient
	protected Map<String, Object> dataMap;

	@javax.persistence.Transient
	protected List<TreeModel> children = new ArrayList<TreeModel>();

	public SysApplication() {

	}

	public void addChild(TreeModel treeModel) {
		if (children == null) {
			children = new ArrayList<TreeModel>();
		}
		children.add(treeModel);
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

	public String getBlank() {
		return blank;
	}

	public List<TreeModel> getChildren() {
		return this.children;
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

	public String getImagePath() {
		return imagePath;
	}

	public int getLevel() {
		return level;
	}

	public String getLinkType() {
		return linkType;
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

	public TreeModel getParent() {
		return parent;
	}

	public long getParentId() {
		return parentId;
	}

	public int getShowMenu() {
		return showMenu;
	}

	public String getShowType() {
		return showType;
	}

	public int getSort() {
		return sort;
	}

	public int getSortNo() {
		return sort;
	}

	public String getSysFlag() {
		return sysFlag;
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

	@Override
	public boolean isChecked() {
		return false;
	}

	public SysApplication jsonToObject(JSONObject jsonObject) {
		return SysApplicationJsonFactory.jsonToObject(jsonObject);
	}

	public void removeChild(TreeModel treeModel) {

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

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
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

	public void setParent(SysApplication parent) {
		this.parent = parent;
	}

	public void setParent(TreeModel parent) {
		this.parent = parent;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public void setShowMenu(int showMenu) {
		this.showMenu = showMenu;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public void setSortNo(int sortNo) {
		this.sort = sortNo;
	}

	public void setSysFlag(String sysFlag) {
		this.sysFlag = sysFlag;
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

	public JSONObject toJsonObject() {
		return SysApplicationJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return SysApplicationJsonFactory.toObjectNode(this);
	}

	public String toString() {
		return toJsonObject().toJSONString();
	}
}