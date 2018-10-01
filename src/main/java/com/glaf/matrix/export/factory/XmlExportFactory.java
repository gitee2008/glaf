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

package com.glaf.matrix.export.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.core.config.Environment;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.identity.User;
import com.glaf.core.service.IDatabaseService;
import com.glaf.matrix.export.domain.XmlExport;
import com.glaf.matrix.export.domain.XmlExportItem;
import com.glaf.matrix.export.query.XmlExportItemQuery;
import com.glaf.matrix.export.query.XmlExportQuery;
import com.glaf.matrix.export.service.XmlExportItemService;
import com.glaf.matrix.export.service.XmlExportService;

public class XmlExportFactory {
	public class RefreshTask implements Runnable {

		public void run() {
			if (!dataList.isEmpty()) {

			}
		}
	}

	public class SyncBaseDataTask implements Runnable {

		public void run() {
			int retry = 0;
			boolean success = false;
			while (retry < 3 && !success) {
				try {
					retry++;
					logger.debug("准备同步基础数据......");

					reloadUserTokenMd5Map();

					reload();

					success = true;
				} catch (Exception ex) {
					logger.error(ex);
					try {
						TimeUnit.MILLISECONDS.sleep(new Random().nextInt(50));// 随机等待
					} catch (InterruptedException e) {
					}
				}
			}
		}

	}

	private static class XmlExportHolder {
		public static XmlExportFactory instance = new XmlExportFactory();
	}

	protected static final Log logger = LogFactory.getLog(XmlExportFactory.class);

	protected static volatile String systemName = Environment.DEFAULT_SYSTEM_NAME;

	protected static volatile IDatabaseService databaseService;

	protected static volatile XmlExportService xmlExportService;

	protected static volatile XmlExportItemService xmlExportItemService;

	protected static ConcurrentMap<String, String> userMap = new ConcurrentHashMap<String, String>();

	protected static ConcurrentMap<String, Map<String, Object>> dataMap = new ConcurrentHashMap<String, Map<String, Object>>();

	protected static ConcurrentMap<String, String> userTokenMd5Map = new ConcurrentHashMap<String, String>();

	protected static ConcurrentMap<String, XmlExport> masterDataMap = new ConcurrentHashMap<String, XmlExport>();

	protected static ConcurrentMap<Long, XmlExport> masterDataMap2 = new ConcurrentHashMap<Long, XmlExport>();

	protected static ConcurrentMap<Long, List<XmlExport>> childrenMap = new ConcurrentHashMap<Long, List<XmlExport>>();

	protected static ConcurrentMap<String, List<XmlExportItem>> slavesDataMap = new ConcurrentHashMap<String, List<XmlExportItem>>();

	protected static List<Map<String, Object>> dataList = new CopyOnWriteArrayList<Map<String, Object>>();

	public static void clearAll() {
		childrenMap.clear();
		masterDataMap.clear();
		masterDataMap2.clear();
		slavesDataMap.clear();
	}

	public static List<XmlExport> getAllChildren(long nodeParentId) {
		return getXmlExportService().getAllChildren(nodeParentId);
	}

	public static List<XmlExport> getChildrenWithItems(long nodeParentId) {
		if (!SystemConfig.getBoolean("use_query_cache")) {
			return getXmlExportService().getChildrenWithItems(nodeParentId);
		}
		List<XmlExport> list = childrenMap.get(nodeParentId);
		if (list == null || list.isEmpty()) {
			list = getXmlExportService().getChildrenWithItems(nodeParentId);
			if (list != null && !list.isEmpty()) {
				childrenMap.put(nodeParentId, list);
			}
		}
		return list;
	}

	public static IDatabaseService getDatabaseService() {
		if (databaseService == null) {
			databaseService = ContextFactory.getBean("databaseService");
		}
		return databaseService;
	}

	public static ConcurrentMap<String, Map<String, Object>> getDataMap() {
		return dataMap;
	}

	public static XmlExportFactory getInstance() {
		return XmlExportHolder.instance;
	}

	public static ConcurrentMap<String, XmlExport> getMasterDataMap() {
		if (masterDataMap.isEmpty()) {
			reload();
		}
		return masterDataMap;
	}

	public static ConcurrentMap<Long, XmlExport> getMasterDataMap2() {
		if (masterDataMap2.isEmpty()) {
			reload();
		}
		return masterDataMap2;
	}

	public static String getSystemName() {
		return systemName;
	}

	public static ConcurrentMap<String, String> getUserMap() {
		if (userMap.isEmpty()) {
			try {
				List<User> users = com.glaf.core.security.IdentityFactory.getUsers();
				if (users != null && !users.isEmpty()) {
					for (User user : users) {
						userMap.put(user.getActorId(), user.getName());
						if (user.getToken() != null) {
							userTokenMd5Map.put(user.getActorId(), DigestUtils.md5Hex(user.getToken()));
						}
					}
				}
			} catch (Exception ex) {
				logger.error(ex);
			}
		}
		return userMap;
	}

	public static String getUserToken(String userId) {
		if (userTokenMd5Map.containsKey(userId)) {
			return userTokenMd5Map.get(userId);
		}
		return DigestUtils.md5Hex(userId);
	}

	public static ConcurrentMap<String, String> getUserTokenMd5Map() {
		if (userTokenMd5Map.isEmpty()) {
			try {
				List<User> users = com.glaf.core.security.IdentityFactory.getUsers();
				if (users != null && !users.isEmpty()) {
					for (User user : users) {
						if (user.getToken() != null) {
							userTokenMd5Map.put(user.getActorId(), DigestUtils.md5Hex(user.getToken()));
						}
					}
				}
			} catch (Exception ex) {
				logger.error(ex);
			}
		}
		return userTokenMd5Map;
	}

	public static XmlExport getXmlExportById(String expId) {
		if (!SystemConfig.getBoolean("use_query_cache")) {
			return getXmlExportService().getXmlExport(expId);
		}
		if (masterDataMap.isEmpty()) {
			reload();
		}
		return masterDataMap.get(expId);
	}

	public static XmlExport getXmlExportByNodeId(long nodeId) {
		if (!SystemConfig.getBoolean("use_query_cache")) {
			return getXmlExportService().getXmlExportByNodeId(nodeId);
		}
		if (masterDataMap2.isEmpty()) {
			reload();
		}
		return masterDataMap2.get(nodeId);
	}

	public static List<XmlExportItem> getXmlExportItemsByExpId(String expId) {
		if (!SystemConfig.getBoolean("use_query_cache")) {
			return getXmlExportItemService().getXmlExportItemsByExpId(expId);
		}
		if (slavesDataMap.isEmpty()) {
			reload();
		}
		return slavesDataMap.get(expId);
	}

	public static XmlExportItemService getXmlExportItemService() {
		if (xmlExportItemService == null) {
			xmlExportItemService = ContextFactory.getBean("com.glaf.matrix.export.service.xmlExportItemService");
		}
		return xmlExportItemService;
	}

	public static XmlExportService getXmlExportService() {
		if (xmlExportService == null) {
			xmlExportService = ContextFactory.getBean("com.glaf.matrix.export.service.xmlExportService");
		}
		return xmlExportService;
	}

	public static void reload() {
		try {
			XmlExportQuery query = new XmlExportQuery();
			List<XmlExport> list = getXmlExportService().list(query);
			if (list != null && !list.isEmpty()) {
				XmlExportItemQuery query2 = new XmlExportItemQuery();
				query2.locked(0);
				List<XmlExportItem> sublist = getXmlExportItemService().list(query2);
				if (sublist != null && !sublist.isEmpty()) {
					for (XmlExportItem item : sublist) {
						List<XmlExportItem> items = slavesDataMap.get(item.getExpId());
						if (items == null) {
							items = new ArrayList<XmlExportItem>();
							items.add(item);
							slavesDataMap.put(item.getExpId(), items);
						} else {
							items.add(item);
						}
					}
				}
				for (XmlExport xmlExport : list) {
					List<XmlExportItem> items = slavesDataMap.get(xmlExport.getId());
					if (items != null && !items.isEmpty()) {
						xmlExport.setItems(items);
					}
					masterDataMap.put(xmlExport.getId(), xmlExport);
					masterDataMap2.put(xmlExport.getNodeId(), xmlExport);
				}
			}
			childrenMap.clear();
		} catch (Exception ex) {
			logger.error(ex);
		}
	}

	public static void reloadUserTokenMd5Map() {
		try {
			List<User> users = com.glaf.core.security.IdentityFactory.getUsers();
			if (users != null && !users.isEmpty()) {
				for (User user : users) {
					if (user.getToken() != null) {
						userTokenMd5Map.put(user.getActorId(), DigestUtils.md5Hex(user.getToken()));
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
	}

	protected ScheduledExecutorService scheduledThreadPool = Executors.newSingleThreadScheduledExecutor();

	protected ScheduledExecutorService scheduledThreadPool2 = Executors.newSingleThreadScheduledExecutor();

	private XmlExportFactory() {
		try {
			List<User> users = com.glaf.core.security.IdentityFactory.getUsers();
			if (users != null && !users.isEmpty()) {
				for (User user : users) {
					userMap.put(user.getActorId(), user.getName());
					if (user.getToken() != null) {
						userTokenMd5Map.put(user.getActorId(), DigestUtils.md5Hex(user.getToken()));
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
		startScheduler();
	}

	public void startScheduler() {
		RefreshTask command = new RefreshTask();
		scheduledThreadPool.scheduleAtFixedRate(command, 1, 500, TimeUnit.MILLISECONDS);// 500毫秒入库一次采集的数据

		SyncBaseDataTask command4 = new SyncBaseDataTask();
		scheduledThreadPool2.scheduleAtFixedRate(command4, 1, 5, TimeUnit.MINUTES);// 每5分钟执行一次同步基础数据
	}

}