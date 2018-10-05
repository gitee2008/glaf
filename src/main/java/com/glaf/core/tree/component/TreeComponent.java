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

package com.glaf.core.tree.component;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.glaf.core.base.TreeModel;

public class TreeComponent extends TreeBase implements Serializable, Component {

	// ~ Static fields/initializers
	// =============================================
	protected static TreeComponent[] _treeComponent = new TreeComponent[0];

	private static final long serialVersionUID = 1L;

	// ~ Instance fields
	// ========================================================

	protected Map<String, Object> dataMap;
	private boolean last;
	protected List<TreeComponent> components = Collections.synchronizedList(new java.util.ArrayList<TreeComponent>());
	protected TreeComponent parent;
	protected TreeModel treeModel;
	protected Object treeObject;
	protected String parentId;

	public void addChild(TreeComponent component) {
		if (component != null && component.getId() != null && !components.contains(component)) {
			components.add(component);
			component.setParent(this);
			component.setParentId(this.getId());
		}
	}

	/**
	 * This method compares all attributes, except for parent and children
	 * 
	 * @param o the object to compare to
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TreeComponent other = (TreeComponent) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public List<TreeComponent> getComponents() {
		if (components != null) {
			java.util.Collections.sort(components);
		}
		return components;
	}

	public Map<String, Object> getDataMap() {
		if (dataMap == null) {
			dataMap = new HashMap<String, Object>();
		}
		return dataMap;
	}

	public TreeComponent getParent() {
		return parent;
	}

	public String getParentId() {
		return parentId;
	}

	public TreeComponent[] getTreeComponents() {
		return (TreeComponent[]) components.toArray(_treeComponent);
	}

	/**
	 * Get the depth of the menu
	 * 
	 * @return Depth of menu
	 */
	public int getTreeDepth() {
		return getTreeDepth(this, 0);
	}

	private int getTreeDepth(TreeComponent component, int currentDepth) {
		int depth = currentDepth + 1;

		TreeComponent[] subTrees = component.getTreeComponents();
		if (subTrees != null) {
			for (int a = 0; a < subTrees.length; a++) {
				int depthx = getTreeDepth(subTrees[a], currentDepth + 1);
				if (depth < depthx) {
					depth = depthx;
				}
			}
		}

		return depth;
	}

	public TreeModel getTreeModel() {
		return treeModel;
	}

	public Object getTreeObject() {
		return treeObject;
	}

	public int hashCode() {
		if (id != null) {
			return id.hashCode();
		}
		return -1;
	}

	/**
	 * Returns the last.
	 * 
	 * @return boolean
	 */
	public boolean isLast() {
		return last;
	}

	/**
	 * Remove all children from a parent menu item
	 */
	public void removeChildren() {
		for (Iterator<TreeComponent> iterator = this.getComponents().iterator(); iterator.hasNext();) {
			TreeComponent child = (TreeComponent) iterator.next();
			child.setParent(null);
			iterator.remove();
		}
	}

	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}

	/**
	 * Sets the last.
	 * 
	 * @param last The last to set
	 */
	public void setLast(boolean last) {
		this.last = last;
	}

	public void setParent(TreeComponent parentTree) {
		if (parentTree != null) {
			// look up the parent and make sure that it has this menu as a child
			if (!parentTree.getComponents().contains(this)) {
				parentTree.addChild(this);
			}
		}
		this.parent = parentTree;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public void setTreeComponents(TreeComponent[] menuComponents) {
		for (int i = 0; i < menuComponents.length; i++) {
			TreeComponent component = menuComponents[i];
			this.components.add(component);
		}
	}

	public void setTreeModel(TreeModel treeModel) {
		this.treeModel = treeModel;
	}

	public void setTreeObject(Object treeObject) {
		this.treeObject = treeObject;
	}

	public String toString() {
		return "id: " + this.id;
	}
}