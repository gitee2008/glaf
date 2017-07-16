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
package com.glaf.base.modules;

import java.io.*;
import java.sql.*;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.glaf.core.config.SystemProperties;
import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.domain.TableDefinition;
import com.glaf.core.jdbc.BatchUpdateBean;
import com.glaf.core.jdbc.BulkInsertBean;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.IOUtils;
import com.glaf.core.util.JdbcUtils;

public class InitDataBean {
	private final static Log logger = LogFactory.getLog(InitDataBean.class);

	public void reload() {
		InputStream inputStream = null;
		try {
			String config = SystemProperties.getConfigRootPath() + "/conf/data";
			File directory = new File(config);
			if (directory.exists() && directory.isDirectory()) {
				File[] filelist = directory.listFiles();
				if (filelist != null) {
					for (int i = 0, len = filelist.length; i < len; i++) {
						File file = filelist[i];
						if (file.isFile() && file.getName().endsWith(".xml")) {
							logger.info("load xml data:" + file.getName());
							inputStream = new FileInputStream(file);
							this.load(inputStream);
							IOUtils.closeStream(inputStream);
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			IOUtils.closeStream(inputStream);
		}
	}

	public void load(InputStream inputStream) {
		SAXReader xmlReader = new SAXReader();
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			Document doc = xmlReader.read(inputStream);
			Element root = doc.getRootElement();
			String primaryKey = root.attributeValue("PK");
			String tableName = root.attributeValue("TABLENAME");
			String updatable = root.attributeValue("UPDATABLE");
			List<?> elements = root.elements("ROW");
			if (tableName != null && elements != null) {
				List<String> keys = new ArrayList<String>();
				List<ColumnDefinition> columns = DBUtils.getColumnDefinitions(tableName);
				conn = DBConnectionFactory.getConnection();
				if (primaryKey != null) {
					psmt = conn.prepareStatement(
							" select " + primaryKey + " from " + tableName + " where " + primaryKey + " is not null ");
					rs = psmt.executeQuery();
					while (rs.next()) {
						keys.add(rs.getObject(1).toString().trim().toLowerCase());
					}
					JdbcUtils.close(rs);
					JdbcUtils.close(psmt);
				}

				List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> dataList2 = new ArrayList<Map<String, Object>>();
				Iterator<?> iterator = elements.iterator();
				while (iterator.hasNext()) {
					Element element = (Element) iterator.next();
					Map<String, Object> dataMap = new HashMap<String, Object>();
					List<?> list = element.elements();
					Iterator<?> iterator2 = list.iterator();
					while (iterator2.hasNext()) {
						Element elem = (Element) iterator2.next();
						String value = elem.getStringValue();
						if (value != null && !StringUtils.equals(value, "(null)")) {
							dataMap.put(elem.getName(), value);
							dataMap.put(elem.getName().toLowerCase(), value);
						}
					}

					if (primaryKey != null) {
						if (dataMap.get(primaryKey) != null
								&& keys.contains(dataMap.get(primaryKey).toString().trim().toLowerCase())) {
							dataList2.add(dataMap);
						} else {
							dataList.add(dataMap);
						}
					} else {
						dataList.add(dataMap);
					}
				}

				conn.setAutoCommit(false);

				TableDefinition tableDefinition = new TableDefinition();
				tableDefinition.setTableName(tableName);
				tableDefinition.setColumns(columns);

				if (dataList.size() > 0) {
					BulkInsertBean insertBean = new BulkInsertBean();
					insertBean.bulkInsert(null, conn, tableDefinition, dataList);
				}

				if (dataList2.size() > 0 && StringUtils.equals(updatable, "true")) {
					BatchUpdateBean updateBean = new BatchUpdateBean();
					updateBean.executeBatch(null, conn, tableDefinition, dataList2);
				}

				conn.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			JdbcUtils.close(rs);
			JdbcUtils.close(psmt);
			JdbcUtils.close(conn);
		}
	}

}
