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

package com.glaf.matrix.export.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.domain.Database;
import com.glaf.core.el.ExpressionTools;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.tree.component.TreeComponent;
import com.glaf.core.tree.helper.XmlTreeHelper;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.ParamUtils;

import com.glaf.matrix.export.bean.XmlExportDataBean;
import com.glaf.matrix.export.domain.XmlExport;
import com.glaf.matrix.export.domain.XmlExportItem;
import com.glaf.matrix.export.factory.XmlExportFactory;

public class XmlExportDataHandler implements XmlDataHandler {

	protected static final Log logger = LogFactory.getLog(XmlExportDataHandler.class);

	protected IDatabaseService databaseService;

	public XmlExportDataHandler() {

	}

	/**
	 * 增加XML节点
	 * 
	 * @param xmlExport  导出定义
	 * @param root       根节点
	 * @param databaseId 数据库编号
	 */
	@Override
	public void addChild(XmlExport xmlExport, org.dom4j.Element root, long databaseId) {
		List<XmlExport> list = XmlExportFactory.getAllChildren(xmlExport.getNodeId());
		if (list != null && !list.isEmpty()) {
			for (XmlExport export : list) {
				List<XmlExport> children = XmlExportFactory.getChildrenWithItems(export.getNodeId());
				export.setChildren(children);
				if (export.getNodeParentId() == xmlExport.getNodeId()) {
					xmlExport.addChild(export);
				}
			}
		}

		List<XmlExportItem> items = XmlExportFactory.getXmlExportItemsByExpId(xmlExport.getId());
		xmlExport.setItems(items);
		Database srcDatabase = getDatabaseService().getDatabaseById(databaseId);

		if (xmlExport.getNodeParentId() == 0) {// 顶层节点，只能有一个根节点
			// 根据定义补上根节点的属性
			if (StringUtils.equals(xmlExport.getResultFlag(), "S")) {
				if (xmlExport.getItems() != null && !xmlExport.getItems().isEmpty()) {
					String value = null;
					try {
						Map<String, Object> dataMap = null;
						if (StringUtils.equals(xmlExport.getResultFlag(), "S")) {
							XmlExportDataBean bean = new XmlExportDataBean();
							dataMap = bean.getMapData(xmlExport, databaseId);
						}
						for (XmlExportItem item : xmlExport.getItems()) {
							/**
							 * 处理属性
							 */
							if (StringUtils.equals(item.getTagFlag(), "A")) {
								if (StringUtils.isNotEmpty(item.getExpression())) {
									value = ExpressionTools.evaluate(item.getExpression(), dataMap);
								} else {
									value = ParamUtils.getString(dataMap, item.getName().toLowerCase());
								}
								if (StringUtils.isNotEmpty(value)) {
									root.addAttribute(item.getName(), value);
								}
							} else {
								root.addElement(item.getName(), value);
							}
						}
					} catch (Exception ex) {
						throw new RuntimeException(ex);
					}
				}
			}
		}

		List<XmlExport> children = XmlExportFactory.getChildrenWithItems(xmlExport.getNodeId());
		if (children != null && !children.isEmpty()) {
			xmlExport.setElement(root);
			logger.debug("---------------------------gen child xml----------------------------");
			for (XmlExport child : children) {
				child.setParent(xmlExport);
				this.addChild(child, srcDatabase);
			}
		}
	}

	/**
	 * 增加XML节点
	 * 
	 * @param xmlExport
	 * @param databaseId
	 */
	public void addChild(XmlExport current, Database srcDatabase) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		String value = null;
		Map<String, Object> dataMap = null;
		List<Map<String, Object>> resultList = null;
		try {
			if (current.getItems() == null || current.getItems().isEmpty()) {
				List<XmlExportItem> items = XmlExportFactory.getXmlExportItemsByExpId(current.getId());
				current.setItems(items);
			}

			String sql = current.getSql();
			if (current.getItems() != null && !current.getItems().isEmpty() && StringUtils.isNotEmpty(sql)
					&& DBUtils.isLegalQuerySql(sql)) {
				XmlExportDataBean bean = new XmlExportDataBean();

				if (StringUtils.equals(current.getResultFlag(), "S")) {
					dataMap = bean.getMapData(current, srcDatabase);
				} else {
					resultList = bean.getListData(current, srcDatabase);
					current.setDataList(resultList);
				}

				/**
				 * 处理单一记录
				 */
				if (StringUtils.equals(current.getResultFlag(), "S")) {
					for (XmlExportItem item : current.getItems()) {
						/**
						 * 处理属性
						 */
						if (StringUtils.equals(item.getTagFlag(), "A")) {
							if (StringUtils.isNotEmpty(item.getExpression())) {
								value = ExpressionTools.evaluate(item.getExpression(), dataMap);
							} else {
								value = ParamUtils.getString(dataMap, item.getName().toLowerCase());
							}
							if (StringUtils.isNotEmpty(value)) {
								current.getElement().addAttribute(item.getName(), value);
							}
						} else {
							current.getElement().addElement(item.getName(), value);
						}
					}
				} else {
					/**
					 * 处理树形结构的叶子节点
					 */
					if (resultList != null && !resultList.isEmpty() && StringUtils.equals(current.getTreeFlag(), "Y")
							&& StringUtils.equals(current.getLeafFlag(), "Y")) {
						List<TreeComponent> trees = new ArrayList<TreeComponent>();
						for (Map<String, Object> rowMap : resultList) {
							TreeComponent tree = new TreeComponent();
							tree.setId(ParamUtils.getString(rowMap, "ext_tree_id"));
							tree.setParentId(ParamUtils.getString(rowMap, "ext_tree_parentid"));
							tree.setDataMap(rowMap);
							if (StringUtils.equals(tree.getParentId(), "0")
									|| StringUtils.equals(tree.getParentId(), "-1")) {
								tree.setParentId(null);
							}
							trees.add(tree);
						}
						// this.processTreeNode(current, trees);

						/**
						 * 有序HashMap
						 */
						Map<String, String> elemMap = new LinkedHashMap<String, String>();
						for (XmlExportItem item : current.getItems()) {
							elemMap.put(item.getName(), item.getTagFlag());
						}
						XmlTreeHelper xmlTreeHelper = new XmlTreeHelper();
						xmlTreeHelper.appendChild(current.getParent().getElement(), current.getXmlTag(), elemMap,
								trees);
					} else {
						if (resultList != null && !resultList.isEmpty()) {
							Element elem = null;
							for (Map<String, Object> rowMap : resultList) {
								/**
								 * 在当前节点的父节点上添加下级节点
								 */
								elem = current.getParent().getElement().addElement(current.getXmlTag());
								// logger.debug("----<" + current.getXmlTag() + ">" + current.getTitle() +
								// "----");
								// logger.debug("elem:" + elem);
								// logger.debug("current.getItems():" + current.getItems());
								if (elem != null) {
									for (XmlExportItem item : current.getItems()) {
										/**
										 * 处理属性
										 */
										if (StringUtils.equals(item.getTagFlag(), "A")) {
											if (StringUtils.isNotEmpty(item.getExpression())) {
												value = ExpressionTools.evaluate(item.getExpression(), rowMap);
											} else {
												value = ParamUtils.getString(rowMap,
														item.getName().trim().toLowerCase());
											}
											if (StringUtils.isNotEmpty(value)) {
												elem.addAttribute(item.getName(), value);
											}
										} else {
											elem.addElement(item.getName(), value);
										}
									}
								}

								// logger.debug("elem:" + elem);

								/**
								 * 处理每条记录的子孙节点
								 */
								List<XmlExport> children = current.getChildren();
								if (children == null || children.isEmpty()) {
									if (!StringUtils.equals(current.getLeafFlag(), "Y")) {
										children = XmlExportFactory.getChildrenWithItems(current.getNodeId());
									}
								}
								// logger.debug("->children:" + children);
								if (children != null && !children.isEmpty()) {
									current.setElement(elem);

									Set<Entry<String, Object>> entrySet0 = rowMap.entrySet();
									for (Entry<String, Object> entry : entrySet0) {
										String key = entry.getKey();
										Object val = entry.getValue();
										parameter.put(key, val);
									}

									if (StringUtils.isNotEmpty(current.getName())) {
										Set<Entry<String, Object>> entrySet = current.getParameter().entrySet();
										for (Entry<String, Object> entry : entrySet) {
											String key = entry.getKey();
											Object val = entry.getValue();
											parameter.put(current.getName() + "_" + key, val);
										}
									}

									if (StringUtils.isNotEmpty(current.getMapping())) {
										Set<Entry<String, Object>> entrySet = current.getParameter().entrySet();
										for (Entry<String, Object> entry : entrySet) {
											String key = entry.getKey();
											Object val = entry.getValue();
											parameter.put(current.getMapping() + "_" + key, val);
										}
									}

									if (StringUtils.isNotEmpty(current.getName())) {
										Set<Entry<String, Object>> entrySet = rowMap.entrySet();
										for (Entry<String, Object> entry : entrySet) {
											String key = entry.getKey();
											Object val = entry.getValue();
											parameter.put(current.getName() + "_" + key, val);
										}
									}

									if (StringUtils.isNotEmpty(current.getMapping())) {
										Set<Entry<String, Object>> entrySet = rowMap.entrySet();
										for (Entry<String, Object> entry : entrySet) {
											String key = entry.getKey();
											Object val = entry.getValue();
											parameter.put(current.getMapping() + "_" + key, val);
										}
									}

									for (XmlExport child : children) {
										child.setParent(current);
										child.setParameter(parameter);
										this.addChild(child, srcDatabase);
									}
								}
							}
						}
					}
				}
			} else {
				/**
				 * 未定义查询，只是XML节点的情况
				 */
				List<XmlExport> children = current.getChildren();
				if (children == null || children.isEmpty()) {
					if (!StringUtils.equals(current.getLeafFlag(), "Y")) {
						children = XmlExportFactory.getChildrenWithItems(current.getNodeId());
					}
				}
				// logger.debug("->children:" + children);
				if (children != null && !children.isEmpty()) {
					for (XmlExport child : children) {
						child.setParent(current);
						if (current.getElement() == null) {
							Element elem = current.getParent().getElement().addElement(child.getXmlTag());
							current.setElement(elem);
						}
						/**
						 * 在当前节点的父节点上添加下级节点
						 */
						if (current.getElement() != null) {
							Element childElem = current.getElement().addElement(child.getXmlTag());
							child.setElement(childElem);
							logger.debug("---------<" + child.getXmlTag() + ">" + child.getTitle() + "------------");
							this.addChild(child, srcDatabase);
						}
					}
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public IDatabaseService getDatabaseService() {
		if (databaseService == null) {
			databaseService = ContextFactory.getBean("databaseService");
		}
		return databaseService;
	}

	public void setDatabaseService(IDatabaseService databaseService) {
		this.databaseService = databaseService;
	}

}
