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

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.core.base.DataFile;
import com.glaf.core.config.BaseConfiguration;
import com.glaf.core.config.Configuration;
import com.glaf.core.config.DatabaseConnectionConfig;
import com.glaf.core.config.Environment;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.domain.Database;
import com.glaf.core.domain.TableDefinition;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.JdbcUtils;

import com.glaf.matrix.data.query.DataFileQuery;
import com.glaf.matrix.data.service.IDataFileService;
import com.glaf.matrix.data.util.DataFileDomainFactory;

public class DataFileFactory {

	private static class DataFileHolder {
		public static DataFileFactory instance = new DataFileFactory();
	}

	protected final static Log logger = LogFactory.getLog(DataFileFactory.class);

	protected static ConcurrentMap<String, String> tables = new ConcurrentHashMap<String, String>();

	protected static volatile Configuration conf = BaseConfiguration.create();

	protected static volatile IDataFileService dataFileService;

	protected static volatile String systemName = null;

	public static IDataFileService getDataFileService() {
		if (dataFileService == null) {
			dataFileService = ContextFactory.getBean("dataFileService");
		}
		return dataFileService;
	}

	public static DataFileFactory getInstance() {
		return DataFileHolder.instance;
	}

	public static void loadTables(String systemName) {
		if (StringUtils.isNotEmpty(systemName)) {
			List<String> tablenames = DBUtils.getTables(systemName);
			if (tables != null && !tablenames.isEmpty()) {
				for (String table : tablenames) {
					String key = systemName + "_" + table;
					key = key.toLowerCase();
					tables.put(key, table);
				}
			}
		}
	}

	private DataFileFactory() {
		try {
			IDatabaseService databaseService = (IDatabaseService) ContextFactory.getBean("databaseService");
			Database database = databaseService.getDatabaseByMapping("file_storage_db");
			if (database != null) {
				DatabaseConnectionConfig cfg = new DatabaseConnectionConfig();
				if (cfg.checkConnectionImmediately(database)) {
					systemName = database.getName();
				}
			} else {
				this.checkAndCreateFileDB();
				database = databaseService.getDatabaseByMapping("file_storage_db");
				if (database != null) {
					DatabaseConnectionConfig cfg = new DatabaseConnectionConfig();
					if (cfg.checkConnectionImmediately(database)) {
						systemName = database.getName();
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
		if (StringUtils.isNotEmpty(systemName)) {
			try {
				loadTables(systemName);
			} catch (Exception ex) {
				logger.error(ex);
			}
		}
	}

	public void insertDataFile(String tenantId, DataFile dataFile, byte[] data) {
		String currentSystemName = Environment.getCurrentSystemName();
		try {
			if (dataFile != null) {
				Environment.setCurrentSystemName(Environment.DEFAULT_SYSTEM_NAME);
				dataFile.setData(null);// 主库不写入附件字节流
				getDataFileService().insertDataFile(tenantId, dataFile);
				if (systemName != null && !StringUtils.equals(Environment.DEFAULT_SYSTEM_NAME, systemName)) {
					logger.debug("file save system: " + systemName);
					Environment.setCurrentSystemName(systemName);
					dataFile.setData(data);// 附件库写入字节流
					getDataFileService().insertDataFile(tenantId, dataFile);
				}
			}
		} catch (Exception ex) {
			//ex.printStackTrace();
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			Environment.setCurrentSystemName(currentSystemName);
		}
	}

	public List<DataFile> getDataFileList(DataFileQuery query) {
		return getDataFileService().getDataFileList(query);
	}

	public DataFile getDataFileByFileId(String tenantId, String fileId) {
		return getDataFileService().getDataFileByFileId(tenantId, fileId);
	}

	public void deleteDataFileByFileId(String tenantId, String fileId) {
		String currentSystemName = Environment.getCurrentSystemName();
		try {
			DataFile dataFile = getDataFileService().getDataFileByFileId(tenantId, fileId);
			if (dataFile != null) {
				if (systemName != null && !StringUtils.equals(Environment.DEFAULT_SYSTEM_NAME, systemName)) {
					Environment.setCurrentSystemName(systemName);
					getDataFileService().deleteById(tenantId, dataFile.getId());// 先删除附件库
				}
				Environment.setCurrentSystemName(Environment.DEFAULT_SYSTEM_NAME);
				getDataFileService().deleteById(tenantId, dataFile.getId());// 再删除主库
			}
		} catch (Exception ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		} finally {
			Environment.setCurrentSystemName(currentSystemName);
		}
	}

	public void deleteByServiceKey(String tenantId, String serviceKey, String actorId, String filename, int status) {
		DataFileQuery query = new DataFileQuery();
		query.tenantId(tenantId);
		query.serviceKey(serviceKey);
		query.setCreateBy(actorId);
		query.status(status);
		query.filename(filename);
		List<DataFile> list = getDataFileService().getDataFileList(query);
		if (list != null && !list.isEmpty()) {
			if (list.size() == 1) {
				DataFile dataFile = list.get(0);
				String currentSystemName = Environment.getCurrentSystemName();
				try {
					if (systemName != null && !StringUtils.equals(Environment.DEFAULT_SYSTEM_NAME, systemName)) {
						Environment.setCurrentSystemName(systemName);
						getDataFileService().deleteById(tenantId, dataFile.getId());// 先删除附件库
					}
					Environment.setCurrentSystemName(Environment.DEFAULT_SYSTEM_NAME);
					getDataFileService().deleteById(tenantId, dataFile.getId());// 再删除主库
				} catch (Exception ex) {
					logger.error(ex);
					throw new RuntimeException(ex);
				} finally {
					Environment.setCurrentSystemName(currentSystemName);
				}
			}
		}
	}

	public InputStream getInputStreamById(String tenantId, String id) {
		DataFile dataFile = getDataFileService().getDataFileById(tenantId, id);
		if (dataFile != null) {
			String currentSystemName = Environment.getCurrentSystemName();
			try {
				if (systemName != null) {
					Environment.setCurrentSystemName(systemName);
					return getDataFileService().getInputStreamById(tenantId, dataFile.getId());
				} else {
					Environment.setCurrentSystemName(Environment.DEFAULT_SYSTEM_NAME);
					return getDataFileService().getInputStreamById(tenantId, dataFile.getId());
				}
			} catch (Exception ex) {
				logger.error(ex);
				throw new RuntimeException(ex);
			} finally {
				Environment.setCurrentSystemName(currentSystemName);
			}
		}
		return null;
	}

	public void checkAndCreateFileDB() {
		IDatabaseService databaseService = (IDatabaseService) ContextFactory.getBean("databaseService");
		Database database = databaseService.getDatabaseByMapping("file_storage_db");
		if (database == null) {// 不存在文件库，创建默认的文件库
			Database master = databaseService.getDatabaseByMapping("master");
			if (master != null && "Y".equals(master.getVerify())) {
				Database fileDB = master.clone();

				fileDB.setMapping("file_storage_db");
				fileDB.setSection("FILE");
				fileDB.setActive("1");
				fileDB.setInitFlag("Y");
				fileDB.setDiscriminator("L");
				fileDB.setLevel(3);
				fileDB.setUseType("FILE");
				fileDB.setRunType("INST");
				fileDB.setTitle("附件库");
				fileDB.setDbname(fileDB.getDbname() + "_FILE");
				fileDB.setKey(master.getKey());
				fileDB.setUser(master.getUser());
				fileDB.setPassword(master.getPassword());
				fileDB.setId(0);
				DatabaseConnectionConfig cfg = new DatabaseConnectionConfig();
				Connection conn = null;
				Statement stmt = null;
				boolean checkOK = false;
				try {
					conn = cfg.getConnection(fileDB);
					if (conn != null) {
						checkOK = true;
					}
				} catch (Exception ex) {
					logger.error(ex);
				} finally {
					JdbcUtils.close(stmt);
					JdbcUtils.close(conn);
				}
				try {
					if (!checkOK) {
						conn = cfg.getConnection(master);
						String dbType = DBConnectionFactory.getDatabaseType(conn);
						logger.debug("dbType:" + dbType);
						if (!StringUtils.equals(DBUtils.POSTGRESQL, dbType)) {
							conn.setAutoCommit(false);
						} else {
							conn.setAutoCommit(true);
						}
						if (StringUtils.equals(DBUtils.POSTGRESQL, dbType)) {
							fileDB.setDbname(fileDB.getDbname().toLowerCase());
						}
						stmt = conn.createStatement();
						stmt.executeUpdate(" CREATE DATABASE " + fileDB.getDbname());
						if (!StringUtils.equals(DBUtils.POSTGRESQL, dbType)) {
							conn.commit();
						}
						JdbcUtils.close(stmt);
						JdbcUtils.close(conn);

						fileDB.setActive("1");
						databaseService.insert(fileDB);

						conn = cfg.getConnection(fileDB);
						if (!StringUtils.equals(DBUtils.POSTGRESQL, dbType)) {
							conn.setAutoCommit(false);
						} else {
							conn.setAutoCommit(true);
						}
						TableDefinition tableDefinition = DataFileDomainFactory.getTableDefinition();
						DBUtils.createTable(conn, tableDefinition);
						if (!StringUtils.equals(DBUtils.POSTGRESQL, dbType)) {
							conn.commit();
						}
						JdbcUtils.close(conn);
					}
				} catch (Exception ex) {
					logger.error(ex);
				} finally {
					JdbcUtils.close(stmt);
					JdbcUtils.close(conn);
				}
			}
		}
	}

}
