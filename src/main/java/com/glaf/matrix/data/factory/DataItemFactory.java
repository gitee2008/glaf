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

package com.glaf.matrix.data.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glaf.core.base.BaseItem;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.service.ITablePageService;
import com.glaf.core.util.ParamUtils;
import com.glaf.matrix.data.domain.SqlDefinition;
import com.glaf.matrix.data.domain.TableDataItem;
import com.glaf.matrix.data.service.SqlDefinitionService;
import com.glaf.matrix.data.service.TableDataItemService;

public class DataItemFactory {
	private static class DataItemHolder {
		public static DataItemFactory instance = new DataItemFactory();
	}

	protected static ConcurrentMap<Long, List<BaseItem>> dictoryItemsMap = new ConcurrentHashMap<Long, List<BaseItem>>();

	protected static ConcurrentMap<Long, List<BaseItem>> treeItemsMap = new ConcurrentHashMap<Long, List<BaseItem>>();

	protected static ConcurrentMap<String, List<BaseItem>> tableItemsMap = new ConcurrentHashMap<String, List<BaseItem>>();

	public static synchronized void clearAll() {
		dictoryItemsMap.clear();
		tableItemsMap.clear();
		treeItemsMap.clear();
	}

	public static DataItemFactory getInstance() {
		return DataItemHolder.instance;
	}

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected ITablePageService tablePageService;

	protected SqlDefinitionService sqlDefinitionService;

	protected TableDataItemService tableDataItemService;

	private DataItemFactory() {

	}

	public List<BaseItem> getDictoryItems(long nodeId) {
		List<BaseItem> items = dictoryItemsMap.get(nodeId);
		if (items == null || items.isEmpty()) {
			Map<String, Object> params = new HashMap<String, Object>();
			List<Map<String, Object>> datalist = getTablePageService().getListData(
					"select NAME as \"name\", VALUE_ as \"value\" from sys_dictory where TYPEID = " + nodeId, params);
			if (datalist != null && !datalist.isEmpty()) {
				items = new ArrayList<BaseItem>();
				for (Map<String, Object> dataMap : datalist) {
					BaseItem item = new BaseItem();
					item.setName(ParamUtils.getString(dataMap, "name"));
					item.setValue(ParamUtils.getString(dataMap, "value"));
					items.add(item);
				}
				dictoryItemsMap.put(nodeId, items);
			}
		}
		return items;
	}

	public TableDataItemService getTableDataItemService() {
		if (tableDataItemService == null) {
			tableDataItemService = ContextFactory.getBean("tableDataItemService");
		}
		return tableDataItemService;
	}

	public List<BaseItem> getTableItems(LoginContext loginContext, String rowId, Map<String, Object> params) {
		List<BaseItem> items = null;
		TableDataItem tableDataItem = getTableDataItemService().getTableDataItem(rowId);
		if (tableDataItem != null && tableDataItem.getLocked() == 0) {
			if (params == null) {
				params = new HashMap<String, Object>();
			}

			params.put("userId", loginContext.getActorId());

			if (loginContext.getUser().getOrganizationId() != 0) {
				params.put("organizationId", loginContext.getUser().getOrganizationId());
			}
			if (loginContext.getTenantId() != null) {
				params.put("tenantId", loginContext.getTenantId());
			}

			StringBuilder sqlBuffer = new StringBuilder();
			sqlBuffer.append(" select ").append(tableDataItem.getNameColumn()).append(" as \"name\", ")
					.append(tableDataItem.getValueColumn()).append(" as \"value\" ").append(" from ")
					.append(tableDataItem.getTableName()).append(" where 1=1 ");
			if (StringUtils.equals(tableDataItem.getFilterFlag(), "O")) {
				if (loginContext.getOrganizationId() > 0) {
					sqlBuffer.append(" and E.ORGANIZATIONID_ = ").append(loginContext.getOrganizationId());
				}
			}

			if (StringUtils.equals(tableDataItem.getFilterFlag(), "T")) {
				if (!loginContext.isSystemAdministrator()) {
					if (loginContext.getTenantId() != null) {
						sqlBuffer.append(" and TENANTID_ = '").append(loginContext.getTenantId()).append("' ");
					}
				}
			}

			if (StringUtils.isNotEmpty(tableDataItem.getSqlCriteria())) {
				if (!tableDataItem.getSqlCriteria().toLowerCase().trim().startsWith("and ")) {
					sqlBuffer.append(" and ");
				}
				sqlBuffer.append(tableDataItem.getSqlCriteria());
			}

			if (StringUtils.isNotEmpty(tableDataItem.getSortColumn())) {
				sqlBuffer.append(" order by ").append(tableDataItem.getSortColumn());
			}

			List<Map<String, Object>> datalist = getTablePageService().getListData(sqlBuffer.toString(), params);
			if (datalist != null && !datalist.isEmpty()) {
				items = new ArrayList<BaseItem>();
				for (Map<String, Object> dataMap : datalist) {
					BaseItem item = new BaseItem();
					item.setName(ParamUtils.getString(dataMap, "name"));
					item.setValue(ParamUtils.getString(dataMap, "value"));
					items.add(item);
				}
			}
		}

		return items;
	}

	public List<BaseItem> getSqlDataItems(LoginContext loginContext, long sqlDefId, Map<String, Object> params) {
		List<BaseItem> items = null;
		SqlDefinition sqlDefinition = getSqlDefinitionService().getSqlDefinition(sqlDefId);
		if (sqlDefinition != null && sqlDefinition.getLocked() == 0) {
			if (params == null) {
				params = new HashMap<String, Object>();
			}

			params.put("userId", loginContext.getActorId());

			if (loginContext.getUser().getOrganizationId() != 0) {
				params.put("organizationid", loginContext.getUser().getOrganizationId());
				params.put("organizationId", loginContext.getUser().getOrganizationId());
			}
			if (loginContext.getTenantId() != null) {
				params.put("tenantid", loginContext.getTenantId());
				params.put("tenantId", loginContext.getTenantId());
			}

			StringBuilder sqlBuffer = new StringBuilder();
			sqlBuffer.append(sqlDefinition.getSql());

			List<Map<String, Object>> datalist = getTablePageService().getListData(sqlBuffer.toString(), params);
			if (datalist != null && !datalist.isEmpty()) {
				items = new ArrayList<BaseItem>();
				for (Map<String, Object> dataMap : datalist) {
					BaseItem item = new BaseItem();
					item.setName(ParamUtils.getString(dataMap, "name"));
					item.setValue(ParamUtils.getString(dataMap, "value"));
					items.add(item);
				}
			}
		}

		return items;
	}

	public SqlDefinitionService getSqlDefinitionService() {
		if (sqlDefinitionService == null) {
			sqlDefinitionService = ContextFactory.getBean("sqlDefinitionService");
		}
		return sqlDefinitionService;
	}

	public ITablePageService getTablePageService() {
		if (tablePageService == null) {
			tablePageService = ContextFactory.getBean("tablePageService");
		}
		return tablePageService;
	}

	public List<BaseItem> getTreeItems(long parentId) {
		List<BaseItem> items = treeItemsMap.get(parentId);
		if (items == null || items.isEmpty()) {
			Map<String, Object> params = new HashMap<String, Object>();
			List<Map<String, Object>> datalist = getTablePageService().getListData(
					"select ID as \"value\", NAME as \"name\" from sys_tree where PARENTID = " + parentId, params);
			if (datalist != null && !datalist.isEmpty()) {
				items = new ArrayList<BaseItem>();
				for (Map<String, Object> dataMap : datalist) {
					BaseItem item = new BaseItem();
					item.setName(ParamUtils.getString(dataMap, "name"));
					item.setValue(ParamUtils.getString(dataMap, "value"));
					items.add(item);
				}
				treeItemsMap.put(parentId, items);
			}
		}
		return items;
	}

}