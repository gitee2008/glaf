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

package com.glaf.matrix.combination.tree;

import java.util.ArrayList;
import java.util.List;

public class TreeNode implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	protected long id;

	protected long parentId;

	protected double value;

	protected double sumValue;

	protected int locked;

	protected Object pkValue = null;

	protected TreeNode parent = null;

	protected List<TreeNode> children = new ArrayList<TreeNode>();

	public TreeNode() {

	}

	public void addChild(TreeNode child) {
		if (children == null) {
			children = new ArrayList<TreeNode>();
		}
		if (!children.contains(child)) {
			children.add(child);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TreeNode other = (TreeNode) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public long getId() {
		return id;
	}

	public int getLocked() {
		return locked;
	}

	public TreeNode getParent() {
		return parent;
	}

	public long getParentId() {
		return parentId;
	}

	public Object getPkValue() {
		return pkValue;
	}

	public double getSumValue() {
		return sumValue;
	}

	public double getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setLocked(int locked) {
		this.locked = locked;
	}

	public void setParent(TreeNode parent) {
		this.parent = parent;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public void setPkValue(Object pkValue) {
		this.pkValue = pkValue;
	}

	public void setSumValue(double sumValue) {
		this.sumValue = sumValue;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "TreeNode [id=" + id + ", parentId=" + parentId + ", value=" + value + ", sumValue=" + sumValue + "]";
	}

}
