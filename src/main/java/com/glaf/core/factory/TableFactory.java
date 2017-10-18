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

package com.glaf.core.factory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.core.config.DatabaseConnectionConfig;
import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.domain.Database;
import com.glaf.core.domain.util.ColumnDefinitionJsonFactory;
import com.glaf.core.util.DBUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class TableFactory {

	protected static volatile Cache<String, String> cache;
	protected static int cacheSize = 10000;
	protected static int expireMinutes = 30;

	public static void clear() {
		getCache().cleanUp();
	}

	public static void clearCache(String systemName, String tableName) {
		String cacheKey = "cache_primarykey_" + tableName;
		getCache().invalidate(cacheKey);
		cacheKey = "cache_primarykey_" + systemName + "_" + tableName;
		getCache().invalidate(cacheKey);

		cacheKey = "cache_" + tableName;
		getCache().invalidate(cacheKey);
		cacheKey = "cache_" + systemName + "_" + tableName;
		getCache().invalidate(cacheKey);
	}

	private static Cache<String, String> getCache() {
		if (cache == null) {
			cache = CacheBuilder.newBuilder().maximumSize(cacheSize).expireAfterWrite(expireMinutes, TimeUnit.MINUTES)
					.build();
		}
		return cache;
	}

	/**
	 * 获取指定名称数据源的表字段定义信息
	 * 
	 * @param systemName
	 *            系统名
	 * @param tableName
	 *            表名
	 * @return
	 */
	public static List<ColumnDefinition> getColumnDefinitions(long databaseId, String tableName) {
		String cacheKey = "cache_" + databaseId + "_" + tableName;
		if (getCache().getIfPresent(cacheKey) != null) {
			String text = getCache().getIfPresent(cacheKey);
			try {
				JSONArray array = JSON.parseArray(text);
				return ColumnDefinitionJsonFactory.arrayToList(array);
			} catch (Exception ex) {
			}
		}
		if (databaseId == 0) {
			return getColumnDefinitions(tableName);
		}
		DatabaseConnectionConfig cfg = new DatabaseConnectionConfig();
		Database database = cfg.getDatabase(databaseId);
		List<ColumnDefinition> columns = DBUtils.getColumnDefinitions(database.getName(), tableName);
		JSONArray array = ColumnDefinitionJsonFactory.listToArray(columns);
		getCache().put(cacheKey, array.toJSONString());
		return columns;
	}

	/**
	 * 获取默认数据源的表字段定义信息
	 * 
	 * @param tableName
	 *            表名
	 * @return
	 */
	public static List<ColumnDefinition> getColumnDefinitions(String tableName) {
		String cacheKey = "cache_" + tableName;
		if (getCache().getIfPresent(cacheKey) != null) {
			String text = getCache().getIfPresent(cacheKey);
			try {
				JSONArray array = JSON.parseArray(text);
				return ColumnDefinitionJsonFactory.arrayToList(array);
			} catch (Exception ex) {
			}
		}
		List<ColumnDefinition> columns = DBUtils.getColumnDefinitions(tableName);
		JSONArray array = ColumnDefinitionJsonFactory.listToArray(columns);
		getCache().put(cacheKey, array.toJSONString());
		return columns;
	}

	/**
	 * 获取指定名称数据源的表字段定义信息
	 * 
	 * @param systemName
	 *            系统名
	 * @param tableName
	 *            表名
	 * @return
	 */
	public static List<ColumnDefinition> getColumnDefinitions(String systemName, String tableName) {
		String cacheKey = "cache_" + systemName + "_" + tableName;
		if (getCache().getIfPresent(cacheKey) != null) {
			String text = getCache().getIfPresent(cacheKey);
			try {
				JSONArray array = JSON.parseArray(text);
				return ColumnDefinitionJsonFactory.arrayToList(array);
			} catch (Exception ex) {
			}
		}
		List<ColumnDefinition> columns = DBUtils.getColumnDefinitions(systemName, tableName);
		JSONArray array = ColumnDefinitionJsonFactory.listToArray(columns);
		getCache().put(cacheKey, array.toJSONString());
		return columns;
	}

	/**
	 * 获取某个表的主键列，仅限只有单一主键的表
	 * 
	 * @param databaseId
	 * @param tableName
	 * @return
	 */
	public static ColumnDefinition getIdColumn(long databaseId, String tableName) {
		String cacheKey = "cache_primarykey_" + databaseId + "_" + tableName;
		if (getCache().getIfPresent(cacheKey) != null) {
			String text = getCache().getIfPresent(cacheKey);
			try {
				JSONObject json = JSON.parseObject(text);
				return ColumnDefinitionJsonFactory.jsonToObject(json);
			} catch (Exception ex) {
			}
		}
		DatabaseConnectionConfig cfg = new DatabaseConnectionConfig();
		Database database = cfg.getDatabase(databaseId);
		List<ColumnDefinition> columns = DBUtils.getColumnDefinitions(database.getName(), tableName);
		if (columns != null && !columns.isEmpty()) {
			for (ColumnDefinition column : columns) {
				if (column.isPrimaryKey()) {
					JSONObject json = ColumnDefinitionJsonFactory.toJsonObject(column);
					getCache().put(cacheKey, json.toJSONString());
					return column;
				}
			}
		}

		return null;
	}

	/**
	 * 获取某个表的主键列，仅限只有单一主键的表
	 * 
	 * @param tableName
	 * @return
	 */
	public static ColumnDefinition getIdColumn(String tableName) {
		String cacheKey = "cache_primarykey_" + tableName;
		if (getCache().getIfPresent(cacheKey) != null) {
			String text = getCache().getIfPresent(cacheKey);
			try {
				JSONObject json = JSON.parseObject(text);
				return ColumnDefinitionJsonFactory.jsonToObject(json);
			} catch (Exception ex) {
			}
		}

		List<ColumnDefinition> columns = DBUtils.getColumnDefinitions(tableName);
		if (columns != null && !columns.isEmpty()) {
			for (ColumnDefinition column : columns) {
				if (column.isPrimaryKey()) {
					JSONObject json = ColumnDefinitionJsonFactory.toJsonObject(column);
					getCache().put(cacheKey, json.toJSONString());
					return column;
				}
			}
		}

		return null;
	}

	/**
	 * 获取某个表的主键列，仅限只有单一主键的表
	 * 
	 * @param systemName
	 * @param tableName
	 * @return
	 */
	public static ColumnDefinition getIdColumn(String systemName, String tableName) {
		String cacheKey = "cache_primarykey_" + systemName + "_" + tableName;
		if (getCache().getIfPresent(cacheKey) != null) {
			String text = getCache().getIfPresent(cacheKey);
			try {
				JSONObject json = JSON.parseObject(text);
				return ColumnDefinitionJsonFactory.jsonToObject(json);
			} catch (Exception ex) {
			}
		}

		List<ColumnDefinition> columns = DBUtils.getColumnDefinitions(systemName, tableName);
		if (columns != null && !columns.isEmpty()) {
			for (ColumnDefinition column : columns) {
				if (column.isPrimaryKey()) {
					JSONObject json = ColumnDefinitionJsonFactory.toJsonObject(column);
					getCache().put(cacheKey, json.toJSONString());
					return column;
				}
			}
		}

		return null;
	}

	public static synchronized boolean renameTable(java.sql.Connection connection, String tableName) {
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		String str = f.format(date);
		String targetTable = "backup_" + tableName + "_" + str;
		return DBUtils.renameTable(connection, tableName, targetTable);
	}

	public static synchronized boolean renameTable(String systemName, String tableName) {
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		String str = f.format(date);
		String targetTable = "backup_" + tableName + "_" + str;
		return DBUtils.renameTable(systemName, tableName, targetTable);
	}

	private TableFactory() {

	}

}
