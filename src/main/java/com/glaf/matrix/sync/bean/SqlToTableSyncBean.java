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

package com.glaf.matrix.sync.bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.domain.Database;
import com.glaf.core.domain.TableDefinition;
import com.glaf.core.jdbc.BatchUpdateBean;
import com.glaf.core.jdbc.BulkInsertBean;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.jdbc.QueryHelper;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.JdbcUtils;
import com.glaf.core.util.LowerLinkedMap;
import com.glaf.core.util.ParamUtils;

import com.glaf.matrix.sync.domain.SyncApp;
import com.glaf.matrix.sync.domain.SyncItem;
import com.glaf.matrix.sync.service.SyncAppService;

public class SqlToTableSyncBean {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	public void execute(long srcDatabaseId, long targetDatabaseId, long syncId) {
		IDatabaseService databaseService = ContextFactory.getBean("databaseService");
		SyncAppService syncAppService = ContextFactory.getBean("com.glaf.matrix.sync.service.syncAppService");
		SyncApp syncApp = syncAppService.getSyncApp(syncId);
		if (syncApp == null || !StringUtils.equals(syncApp.getActive(), "Y")) {
			return;
		}
		QueryHelper helper = new QueryHelper();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Database srcDatabase = null;
		Database targetDatabase = null;
		Connection srcConn = null;
		Connection targetConn = null;
		PreparedStatement srcPsmt = null;
		PreparedStatement psmt = null;
		ResultSet srcRs = null;
		ResultSet rs = null;
		try {
			srcDatabase = databaseService.getDatabaseById(srcDatabaseId);
			targetDatabase = databaseService.getDatabaseById(targetDatabaseId);

			if (srcDatabase != null) {
				srcConn = DBConnectionFactory.getConnection(srcDatabase.getName());
				logger.debug("srcConn:" + srcConn);
			}

			if (targetDatabase != null) {
				targetConn = DBConnectionFactory.getConnection(targetDatabase.getName());
				logger.debug("targetConn:" + targetConn);
			}

			if (srcConn != null && targetConn != null) {

				targetConn.setAutoCommit(false);

				List<SyncItem> items = syncApp.getItems();
				for (SyncItem item : items) {
					TableDefinition tableDefinition = new TableDefinition();
					tableDefinition.setTableName(item.getTargetTableName());

					ColumnDefinition column1 = new ColumnDefinition();
					column1.setColumnName("EX_SYNC_KEY_");// 来源表的主键值
					column1.setJavaType("String");
					column1.setLength(500);
					tableDefinition.addColumn(column1);

					ColumnDefinition column2 = new ColumnDefinition();
					column2.setColumnName("EX_SYNC_FLAG_");// 设置同步标记
					column2.setJavaType("String");
					column2.setLength(1);
					tableDefinition.addColumn(column2);

					ColumnDefinition column3 = new ColumnDefinition();
					column3.setColumnName("EX_SYNC_TIME_");// 设置同步时间
					column3.setJavaType("Date");
					tableDefinition.addColumn(column3);

					ColumnDefinition column4 = new ColumnDefinition();
					column4.setColumnName("EX_SYNC_APPID_");// 设置同步编号
					column4.setJavaType("Long");
					tableDefinition.addColumn(column4);

					DBUtils.alterTable(targetConn, tableDefinition);
					targetConn.commit();
				}

				for (SyncItem item : items) {
					// ColumnDefinition idColumn = null;
					String primaryKey = null;
					List<ColumnDefinition> columns = DBUtils.getColumnDefinitions(targetConn,
							item.getTargetTableName());
					for (ColumnDefinition column : columns) {
						if (column.isPrimaryKey()) {
							// idColumn = column;
							primaryKey = column.getColumnName();
							break;
						}
					}
					StringBuilder buff = new StringBuilder();
					String sql = item.getSql();
					List<Map<String, Object>> resultList = helper.getResultList(srcConn, sql, paramMap, 500000);
					Map<String, String> srcPrimaryKeyMap = new HashMap<String, String>();
					if (resultList != null && !resultList.isEmpty()) {
						List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
						for (Map<String, Object> rowMap : resultList) {
							LowerLinkedMap dataMap = new LowerLinkedMap();
							dataMap.putAll(rowMap);
							dataList.add(dataMap);
						}
						resultList = dataList;
					}

					if (resultList != null && !resultList.isEmpty()) {
						for (Map<String, Object> rowMap : resultList) {
							buff.delete(0, buff.length());
							for (ColumnDefinition column : columns) {
								if (StringUtils.equals(column.getColumnName(), "EX_SYNC_KEY_")) {
									continue;
								}
								if (StringUtils.equals(column.getColumnName(), "EX_SYNC_FLAG_")) {
									continue;
								}
								if (StringUtils.equals(column.getColumnName(), "EX_SYNC_TIME_")) {
									continue;
								}
								if (StringUtils.equals(column.getColumnName(), "EX_SYNC_APPID_")) {
									continue;
								}
								buff.append(ParamUtils.getString(rowMap, column.getColumnName())).append("_");
							}
							srcPrimaryKeyMap.put(ParamUtils.getString(rowMap, primaryKey),
									DigestUtils.md5Hex(buff.toString()));
						}
					}

					if (StringUtils.equals(syncApp.getSyncFlag(), "DELETE_INSERT")) {
						String ddlStatements = " delete from " + item.getTargetTableName() + " where EX_SYNC_APPID_ = "
								+ syncApp.getId();
						logger.debug(ddlStatements);
						DBUtils.executeSchemaResource(targetConn, ddlStatements);
					}

					sql = " select * from " + item.getTargetTableName();
					List<Map<String, Object>> resultList2 = helper.getResultList(targetConn, sql, paramMap, 500000);
					Map<String, String> targetPrimaryKeyMap = new HashMap<String, String>();
					if (resultList2 != null && !resultList2.isEmpty()) {
						for (Map<String, Object> rowMap : resultList2) {
							buff.delete(0, buff.length());
							for (ColumnDefinition column : columns) {
								if (StringUtils.equals(column.getColumnName(), "EX_SYNC_KEY_")) {
									continue;
								}
								if (StringUtils.equals(column.getColumnName(), "EX_SYNC_FLAG_")) {
									continue;
								}
								if (StringUtils.equals(column.getColumnName(), "EX_SYNC_TIME_")) {
									continue;
								}
								if (StringUtils.equals(column.getColumnName(), "EX_SYNC_APPID_")) {
									continue;
								}
								buff.append(ParamUtils.getString(rowMap, column.getColumnName())).append("_");
							}
							targetPrimaryKeyMap.put(ParamUtils.getString(rowMap, primaryKey),
									DigestUtils.md5Hex(buff.toString()));
						}
					}

					if (resultList != null && !resultList.isEmpty()) {
						String key = null;
						Date now = new Date();
						List<String> keys = new ArrayList<String>();
						List<Map<String, Object>> insertList = new ArrayList<Map<String, Object>>();
						List<Map<String, Object>> updateList = new ArrayList<Map<String, Object>>();
						for (Map<String, Object> rowMap : resultList) {
							key = ParamUtils.getString(rowMap, primaryKey);
							if (keys.contains(key)) {
								continue;
							}
							if (StringUtils.equals(syncApp.getSyncFlag(), "INSERT_ONLY")) {
								if (targetPrimaryKeyMap.get(key) == null) {
									rowMap.put("EX_SYNC_KEY_", srcPrimaryKeyMap.get(key));
									rowMap.put("EX_SYNC_FLAG_", "Y");
									rowMap.put("EX_SYNC_TIME_", now);
									rowMap.put("EX_SYNC_APPID_", syncApp.getId());
									insertList.add(rowMap);
									keys.add(key);
								}
							} else if (StringUtils.equals(syncApp.getSyncFlag(), "INSERT_UPDATE")) {
								if (targetPrimaryKeyMap.get(key) == null) {
									rowMap.put("EX_SYNC_KEY_", srcPrimaryKeyMap.get(key));
									rowMap.put("EX_SYNC_FLAG_", "Y");
									rowMap.put("EX_SYNC_TIME_", now);
									rowMap.put("EX_SYNC_APPID_", syncApp.getId());
									insertList.add(rowMap);
									keys.add(key);
								} else {
									if (StringUtils.equals(srcPrimaryKeyMap.get(key), targetPrimaryKeyMap.get(key))) {
										continue;// 源记录MD5值与目标记录MD5值相等时不做更新
									}
									rowMap.put("EX_SYNC_KEY_", srcPrimaryKeyMap.get(key));
									rowMap.put("EX_SYNC_FLAG_", "Y");
									rowMap.put("EX_SYNC_TIME_", now);
									rowMap.put("EX_SYNC_APPID_", syncApp.getId());
									updateList.add(rowMap);
									keys.add(key);
								}
							} else if (StringUtils.equals(syncApp.getSyncFlag(), "DELETE_INSERT")) {
								rowMap.put("EX_SYNC_KEY_", srcPrimaryKeyMap.get(key));
								rowMap.put("EX_SYNC_FLAG_", "Y");
								rowMap.put("EX_SYNC_TIME_", now);
								rowMap.put("EX_SYNC_APPID_", syncApp.getId());
								insertList.add(rowMap);
								keys.add(key);
							}
						}

						logger.debug("insertList size:" + insertList.size());
						logger.debug("updateList size:" + updateList.size());
						// logger.debug("insertList:"+insertList);
						TableDefinition tableDefinition = new TableDefinition();
						tableDefinition.setTableName(item.getTargetTableName());
						tableDefinition.setColumns(columns);
						BulkInsertBean insertBean = new BulkInsertBean();
						insertBean.bulkInsert(null, targetConn, tableDefinition, insertList);

						BatchUpdateBean updateBean = new BatchUpdateBean();
						updateBean.executeBatch(null, targetConn, tableDefinition, updateList);

					}
				}

				targetConn.commit();
			}

		} catch (Exception ex) {
			logger.error("execute sync error", ex);
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(srcRs);
			JdbcUtils.close(srcPsmt);
			JdbcUtils.close(srcConn);

			JdbcUtils.close(rs);
			JdbcUtils.close(psmt);
			JdbcUtils.close(targetConn);
		}
	}

}
