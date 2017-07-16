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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.base.modules.sys.util.DictoryJsonFactory;

public class DictoryTree extends Dictory implements Serializable {
	private static final long serialVersionUID = -1L;
	protected long dictoryTreeId;
	protected String treeCode;
	protected String treeDesc;
	protected String icon;
	protected String iconCls;
	protected String treeName;
	protected long parentId;
	protected int treeSort;
	protected String treeId;
	protected String url;
	protected boolean leafFlag;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DictoryTree other = (DictoryTree) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public long getDictoryTreeId() {
		return dictoryTreeId;
	}

	public String getIcon() {
		return icon;
	}

	public String getIconCls() {
		return iconCls;
	}

	public boolean getLeafFlag() {
		return leafFlag;
	}

	public long getParentId() {
		return parentId;
	}

	public String getTreeCode() {
		return treeCode;
	}

	public String getTreeDesc() {
		return treeDesc;
	}

	public String getTreeId() {
		return treeId;
	}

	public String getTreeName() {
		return treeName;
	}

	public int getTreeSort() {
		return treeSort;
	}

	public String getUrl() {
		return url;
	}

	public void setDictoryTreeId(long dictoryTreeId) {
		this.dictoryTreeId = dictoryTreeId;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public void setLeafFlag(boolean leafFlag) {
		this.leafFlag = leafFlag;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public void setTreeCode(String treeCode) {
		this.treeCode = treeCode;
	}

	public void setTreeDesc(String treeDesc) {
		this.treeDesc = treeDesc;
	}

	public void setTreeId(String treeId) {
		this.treeId = treeId;
	}

	public void setTreeName(String treeName) {
		this.treeName = treeName;
	}

	public void setTreeSort(int treeSort) {
		this.treeSort = treeSort;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public JSONObject toJsonObject() {
		return DictoryJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return DictoryJsonFactory.toObjectNode(this);
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
