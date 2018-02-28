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

import org.apache.commons.lang3.StringUtils;

import com.glaf.core.base.TreeModel;

public class TreeComponent extends TreeBase implements Serializable, Component {

	// ~ Static fields/initializers
	// =============================================
	protected static TreeComponent[] _menuComponent = new TreeComponent[0];

	private static final long serialVersionUID = 1L;

	// ~ Instance fields
	// ========================================================

	protected Map<String, Object> dataMap;
	private boolean last;
	protected List<TreeComponent> components = Collections
			.synchronizedList(new java.util.concurrent.CopyOnWriteArrayList<TreeComponent>());
	protected TreeComponent parentTree;
	protected TreeModel treeModel;
	protected Object treeObject;
	protected String parentId;

	public void addTreeComponent(TreeComponent component) {
		if (component != null && component.getId() != null && !components.contains(component)) {
			components.add(component);
			component.setParent(this);
		}
	}

	/**
	 * This method compares all attributes, except for parent and children
	 * 
	 * @param o
	 *            the object to compare to
	 */
	public boolean equals(Object o) {
		if (!(o instanceof TreeComponent)) {
			return false;
		}
		TreeComponent m = (TreeComponent) o;
		// Compare using StringUtils to avoid NullPointerExceptions
		return StringUtils.equals(m.getAction(), this.action) && StringUtils.equals(m.getAlign(), this.align)
				&& StringUtils.equals(m.getAltImage(), this.altImage)
				&& StringUtils.equals(m.getDescription(), this.description)
				&& StringUtils.equals(m.getForward(), this.forward) && StringUtils.equals(m.getHeight(), this.height)
				&& StringUtils.equals(m.getImage(), this.image) && StringUtils.equals(m.getLocation(), this.location)
				&& StringUtils.equals(m.getId(), this.id) && StringUtils.equals(m.getOnclick(), this.onclick)
				&& StringUtils.equals(m.getOndblclick(), this.ondblclick)
				&& StringUtils.equals(m.getOnmouseout(), this.onmouseout)
				&& StringUtils.equals(m.getOnmouseover(), this.onmouseover)
				&& StringUtils.equals(m.getOnContextTree(), this.onContextTree)
				&& StringUtils.equals(m.getPage(), this.page) && StringUtils.equals(m.getRoles(), this.roles)
				&& StringUtils.equals(m.getTarget(), this.target) && StringUtils.equals(m.getTitle(), this.title)
				&& StringUtils.equals(m.getToolTip(), this.toolTip) && StringUtils.equals(m.getWidth(), this.width)
				&& StringUtils.equals(m.getModule(), this.module);
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
		return parentTree;
	}

	public String getParentId() {
		return parentId;
	}

	public TreeComponent[] getTreeComponents() {
		return (TreeComponent[]) components.toArray(_menuComponent);
	}

	/**
	 * Get the organizationh of the menu
	 * 
	 * @return Organizationh of menu
	 */
	public int getTreeOrganizationh() {
		return getTreeOrganizationh(this, 0);
	}

	private int getTreeOrganizationh(TreeComponent menu, int currentOrganizationh) {
		int organizationh = currentOrganizationh + 1;

		TreeComponent[] subTrees = menu.getTreeComponents();
		if (subTrees != null) {
			for (int a = 0; a < subTrees.length; a++) {
				int organizationhx = getTreeOrganizationh(subTrees[a], currentOrganizationh + 1);
				if (organizationh < organizationhx)
					organizationh = organizationhx;
			}
		}

		return organizationh;
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
	 * @param last
	 *            The last to set
	 */
	public void setLast(boolean last) {
		this.last = last;
	}

	public void setParent(TreeComponent parentTree) {
		if (parentTree != null) {
			// look up the parent and make sure that it has this menu as a child
			if (!parentTree.getComponents().contains(this)) {
				parentTree.addTreeComponent(this);
			}
		}
		this.parentTree = parentTree;
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