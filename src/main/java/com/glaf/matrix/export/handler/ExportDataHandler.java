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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.domain.Database;
import com.glaf.core.entity.SqlExecutor;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.util.LowerLinkedMap;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.JdbcUtils;
import com.glaf.core.util.QueryUtils;

import com.glaf.matrix.export.domain.XmlExport;
import com.glaf.matrix.export.domain.XmlExportItem;
import com.glaf.matrix.export.service.XmlExportItemService;
import com.glaf.matrix.export.service.XmlExportService;

public class ExportDataHandler implements DataHandler {

	protected static final Log logger = LogFactory.getLog(XmlExportDataHandler.class);

	protected IDatabaseService databaseService;

	protected XmlExportService xmlExportService;

	protected XmlExportItemService xmlExportItemService;

	public ExportDataHandler() {

	}

	/**
	 * 增加数据节点
	 * 
	 * @param xmlExport
	 *            导出定义
	 * @param dataMap
	 *            数据集合
	 * @param databaseId
	 *            数据库编号
	 */
	@Override
	public void addChild(XmlExport xmlExport, Map<String, Object> dataMap, long databaseId) {
		List<XmlExport> list = getXmlExportService().getAllChildren(xmlExport.getNodeId());
		if (list != null && !list.isEmpty()) {
			for (XmlExport export : list) {
				List<XmlExport> children = getXmlExportService().getChildrenWithItems(export.getNodeId());
				export.setChildren(children);
				if (export.getNodeParentId() == xmlExport.getNodeId()) {
					xmlExport.addChild(export);
				}
			}
		}

		List<XmlExportItem> items = getXmlExportItemService().getXmlExportItemsByExpId(xmlExport.getId());
		xmlExport.setItems(items);

		Database srcDatabase = getDatabaseService().getDatabaseById(databaseId);

		if (xmlExport.getNodeParentId() == 0) {// 顶层节点，只能有一个根节点

		}

		List<XmlExport> children = getXmlExportService().getChildrenWithItems(xmlExport.getNodeId());
		if (children != null && !children.isEmpty()) {
			logger.debug("---------------------------fetch child data----------------------------");
			for (XmlExport child : children) {
				child.setParent(xmlExport);
				int retry = 0;
				boolean success = false;
				while (retry < 3 && !success) {
					try {
						retry++;
						this.addChild(child, dataMap, srcDatabase);
						success = true;
					} catch (Exception ex) {
						logger.error(ex);
						try {
							TimeUnit.MILLISECONDS.sleep(20 + new Random().nextInt(50));
						} catch (InterruptedException e) {
						}
					}
				}
			}
		}
	}

	/**
	 * 增加XML节点
	 * 
	 * @param xmlExport
	 * @param databaseId
	 */
	@SuppressWarnings("unchecked")
	public void addChild(XmlExport current, Map<String, Object> dataMap, Database srcDatabase) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		Connection srcConn = null;
		PreparedStatement srcPsmt = null;
		ResultSet srcRs = null;
		try {
			if (current.getItems() == null || current.getItems().isEmpty()) {
				List<XmlExportItem> items = getXmlExportItemService().getXmlExportItemsByExpId(current.getId());
				current.setItems(items);
			}
			String sql = current.getSql();
			if (StringUtils.isNotEmpty(sql) && DBUtils.isLegalQuerySql(sql)) {
				Map<String, Object> params = current.getParameter();
				parameter.putAll(params);
				SqlExecutor sqlExecutor = QueryUtils.replaceSQL(sql, parameter);
				sql = sqlExecutor.getSql();
				logger.debug("sql:" + sql);
				// logger.debug("params:" + parameter);
				srcConn = DBConnectionFactory.getConnection(srcDatabase.getName());
				srcPsmt = srcConn.prepareStatement(sql);

				if (sqlExecutor.getParameter() != null) {
					// logger.debug("params:" + parameter);
					logger.debug("parameter:" + sqlExecutor.getParameter());
					List<Object> values = (List<Object>) sqlExecutor.getParameter();
					JdbcUtils.fillStatement(srcPsmt, values);
				}
				srcRs = srcPsmt.executeQuery();
				Map<String, Object> singleMap = new HashMap<String, Object>();
				singleMap.putAll(params);
				List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

				if (StringUtils.equals(current.getResultFlag(), "S")) {
					if (srcRs.next()) {
						singleMap.putAll(this.toMap(srcRs));
					}
				} else {
					while (srcRs.next()) {
						resultList.add(this.toMap(srcRs));
					}
				}

				JdbcUtils.close(srcRs);
				JdbcUtils.close(srcPsmt);
				JdbcUtils.close(srcConn);

				logger.debug("result size:" + resultList.size());
				current.setDataList(resultList);

				if (StringUtils.isNotEmpty(current.getName())) {
					dataMap.put(current.getName() + "_datamodel", current);
					dataMap.put(current.getName() + "_datalist", resultList);
				}

				if (StringUtils.isNotEmpty(current.getMapping())) {
					dataMap.put(current.getMapping() + "_datamodel", current);
					dataMap.put(current.getMapping() + "_datalist", resultList);
				}

				/**
				 * 处理单一记录
				 */
				if (StringUtils.equals(current.getResultFlag(), "S")) {

					if (StringUtils.isNotEmpty(current.getName())) {
						dataMap.put(current.getName() + "_sx", singleMap);
					}

					if (StringUtils.isNotEmpty(current.getMapping())) {
						dataMap.put(current.getMapping() + "_sx", singleMap);
					}

				} else {
					/**
					 * 处理多条记录
					 */
					for (Map<String, Object> rowMap : resultList) {
						/**
						 * 处理每条记录的子孙节点
						 */
						List<XmlExport> children = current.getChildren();
						if (children == null || children.isEmpty()) {
							if (!StringUtils.equals(current.getLeafFlag(), "Y")) {
								children = getXmlExportService().getChildrenWithItems(current.getNodeId());
							}
						}

						// logger.debug("->children:" + children);
						if (children != null && !children.isEmpty()) {
							if (StringUtils.isNotEmpty(current.getName())) {
								Set<Entry<String, Object>> entrySet = current.getParameter().entrySet();
								for (Entry<String, Object> entry : entrySet) {
									String key = entry.getKey();
									Object val = entry.getValue();
									dataMap.put(current.getName() + "_" + key, val);
									parameter.put(current.getName() + "_" + key, val);
								}
							}

							if (StringUtils.isNotEmpty(current.getMapping())) {
								Set<Entry<String, Object>> entrySet = current.getParameter().entrySet();
								for (Entry<String, Object> entry : entrySet) {
									String key = entry.getKey();
									Object val = entry.getValue();
									dataMap.put(current.getName() + "_" + key, val);
									parameter.put(current.getMapping() + "_" + key, val);
								}
							}

							for (XmlExport child : children) {

								if (StringUtils.isNotEmpty(current.getName())) {
									Set<Entry<String, Object>> entrySet = rowMap.entrySet();
									for (Entry<String, Object> entry : entrySet) {
										String key = entry.getKey();
										Object val = entry.getValue();
										dataMap.put(current.getName() + "_" + key, val);
										parameter.put(current.getName() + "_" + key, val);
									}
								}

								if (StringUtils.isNotEmpty(current.getMapping())) {
									Set<Entry<String, Object>> entrySet = rowMap.entrySet();
									for (Entry<String, Object> entry : entrySet) {
										String key = entry.getKey();
										Object val = entry.getValue();
										dataMap.put(current.getName() + "_" + key, val);
										parameter.put(current.getMapping() + "_" + key, val);
									}
								}

								child.setParent(current);
								child.setParameter(parameter);

								int retry = 0;
								boolean success = false;
								while (retry < 3 && !success) {
									try {
										retry++;
										this.addChild(child, dataMap, srcDatabase);
										success = true;
									} catch (Exception ex) {
										logger.error(ex);
										try {
											TimeUnit.MILLISECONDS.sleep(20 + new Random().nextInt(50));
										} catch (InterruptedException e) {
										}
									}
								}
							}
						}
					}
				}
			} else {
				/**
				 * 未定义查询，只是数据节点的情况
				 */
				List<XmlExport> children = current.getChildren();
				if (children == null || children.isEmpty()) {
					if (!StringUtils.equals(current.getLeafFlag(), "Y")) {
						children = getXmlExportService().getChildrenWithItems(current.getNodeId());
					}
				}
				// logger.debug("->children:" + children);
				if (children != null && !children.isEmpty()) {
					for (XmlExport child : children) {
						child.setParent(current);
						logger.debug("-----------" + child.getTitle() + "------------");
						int retry = 0;
						boolean success = false;
						while (retry < 3 && !success) {
							try {
								retry++;
								this.addChild(child, dataMap, srcDatabase);
								success = true;
							} catch (Exception ex) {
								logger.error(ex);
								try {
									TimeUnit.MILLISECONDS.sleep(20 + new Random().nextInt(50));
								} catch (InterruptedException e) {
								}
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("execute sql query error", ex);
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(srcRs);
			JdbcUtils.close(srcPsmt);
			JdbcUtils.close(srcConn);
		}
	}

	public IDatabaseService getDatabaseService() {
		if (databaseService == null) {
			databaseService = ContextFactory.getBean("databaseService");
		}
		return databaseService;
	}

	public XmlExportItemService getXmlExportItemService() {
		if (xmlExportItemService == null) {
			xmlExportItemService = ContextFactory.getBean("com.glaf.matrix.export.service.xmlExportItemService");
		}
		return xmlExportItemService;
	}

	public XmlExportService getXmlExportService() {
		if (xmlExportService == null) {
			xmlExportService = ContextFactory.getBean("com.glaf.matrix.export.service.xmlExportService");
		}
		return xmlExportService;
	}

	public void setDatabaseService(IDatabaseService databaseService) {
		this.databaseService = databaseService;
	}

	public void setXmlExportItemService(XmlExportItemService xmlExportItemService) {
		this.xmlExportItemService = xmlExportItemService;
	}

	public void setXmlExportService(XmlExportService xmlExportService) {
		this.xmlExportService = xmlExportService;
	}

	public Map<String, Object> toMap(ResultSet rs) throws SQLException {
		Map<String, Object> result = new LowerLinkedMap();
		ResultSetMetaData rsmd = rs.getMetaData();
		int count = rsmd.getColumnCount();
		for (int i = 1; i <= count; i++) {
			String columnName = rsmd.getColumnLabel(i);
			if (StringUtils.isEmpty(columnName)) {
				columnName = rsmd.getColumnName(i);
			}
			Object object = rs.getObject(i);
			columnName = columnName.toLowerCase();
			result.put(columnName, object);
		}
		return result;
	}

}
