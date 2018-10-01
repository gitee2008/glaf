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

package com.glaf.core.tree.helper;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import com.glaf.core.tree.component.TreeComponent;
import com.glaf.core.tree.component.TreeRepository;
import com.glaf.core.util.DateUtils;

public class XmlTreeHelper {

	/**
	 * 附加XML数据节点
	 * 
	 * @param element
	 *            XML节点元素
	 * @param xmlTag
	 *            XML标记
	 * @param elemMap
	 *            元素或属性集合
	 * @param treeModels
	 *            树节点
	 */
	public void appendChild(Element element, String xmlTag, Map<String, String> elemMap,
			List<TreeComponent> treeModels) {
		if (treeModels != null && treeModels.size() > 0) {
			TreeRepositoryBuilder builder = new TreeRepositoryBuilder();
			TreeRepository menuRepository = builder.buildTree(treeModels);
			List<TreeComponent> topTrees = menuRepository.getTopTrees();
			if (topTrees != null && topTrees.size() > 0) {
				for (int i = 0, len = topTrees.size(); i < len; i++) {
					TreeComponent component = (TreeComponent) topTrees.get(i);
					Element elem = this.appendData(component, element, xmlTag, elemMap);
					this.processTreeNode(elem, component, xmlTag, elemMap);
				}
			}
		}
	}

	protected Element appendData(TreeComponent component, Element element, String xmlTag, Map<String, String> elemMap) {
		Element elem = element.addElement(xmlTag);
		if (component.getDataMap() != null) {
			String text = null;
			Map<String, Object> dataMap = component.getDataMap();
			Set<Entry<String, String>> entrySet = elemMap.entrySet();
			for (Entry<String, String> entry : entrySet) {
				String name = entry.getKey();
				String type = entry.getValue();
				Object value = dataMap.get(name.toLowerCase());
				if (value != null) {
					if (value instanceof Date) {
						Date date = (Date) value;
						text = DateUtils.getDateTime(date);
					} else {
						text = value.toString();
					}
					if (StringUtils.equals(type, "A")) {
						elem.addAttribute(name, text);
					} else {
						elem.addElement(name, text);
					}
				}
			}
		}
		return elem;
	}

	protected void processTreeNode(Element element, TreeComponent component, String xmlTag,
			Map<String, String> elemMap) {
		if (component.getComponents() != null && component.getComponents().size() > 0) {
			for (TreeComponent child : component.getComponents()) {
				Element elem = this.appendData(child, element, xmlTag, elemMap);
				this.processTreeNode(elem, child, xmlTag, elemMap);
			}
		}
	}

}
