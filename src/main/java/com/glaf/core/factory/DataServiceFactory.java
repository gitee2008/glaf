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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.glaf.core.base.TableModel;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.domain.SysDataLog;
import com.glaf.core.service.ITableDataService;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.JsonUtils;

public class DataServiceFactory {

	private static class DataServiceFactoryHolder {
		public static DataServiceFactory instance = new DataServiceFactory();
	}

	public static DataServiceFactory getInstance() {
		return DataServiceFactoryHolder.instance;
	}

	protected ITableDataService tableDataService;

	private DataServiceFactory() {

	}

	/**
	 * 删除数据
	 * 
	 * @param model
	 */
	public void deleteTableData(TableModel model) {
		String tablename = model.getTableName().toLowerCase();
		if (DBUtils.getUnWritableSecretTables().contains(tablename)) {
			throw new RuntimeException("access deny");
		}
		long start = System.currentTimeMillis();
		try {
			Map<String, Object> dataMap = this.getTableDataByPrimaryKey(model);
			if (dataMap != null) {
				this.getTableDataService().deleteTableData(model);
				SysDataLog log = new SysDataLog();
				log.setActorId(model.getActorId());
				if (model.getIdColumn() != null && model.getIdColumn().getValue() != null) {
					log.setBusinessKey(model.getIdColumn().getValue().toString());
				}
				log.setCreateTime(new Date());
				log.setOperate("delete");
				log.setModuleId(model.getTableName());
				log.setContent(JsonUtils.toJSONObject(dataMap).toJSONString());
				log.setTimeMS((int) (System.currentTimeMillis() - start));
				log.setFlag(1);
				SysLogFactory.getInstance().addLog(log);
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public void deleteTableDataList(List<TableModel> rows) {
		long start = System.currentTimeMillis();
		int timeMS = 0;
		int flag = 0;
		List<SysDataLog> logs = new ArrayList<SysDataLog>();
		try {
			if (rows != null && !rows.isEmpty()) {
				for (TableModel model : rows) {
					String tablename = model.getTableName().toLowerCase();
					if (DBUtils.getUnWritableSecretTables().contains(tablename)) {
						throw new RuntimeException("access deny");
					}
					Map<String, Object> dataMap = this.getTableDataByPrimaryKey(model);
					if (dataMap != null) {
						SysDataLog log = new SysDataLog();
						log.setActorId(model.getActorId());
						if (model.getIdColumn() != null && model.getIdColumn().getValue() != null) {
							log.setBusinessKey(model.getIdColumn().getValue().toString());
						}
						log.setCreateTime(new Date());
						log.setOperate("delete");
						log.setModuleId(model.getTableName());
						log.setContent(JsonUtils.toJSONObject(dataMap).toJSONString());
						logs.add(log);
					}
				}
				this.getTableDataService().deleteTableDataList(rows);
				timeMS = (int) (System.currentTimeMillis() - start);
				flag = 1;
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		for (SysDataLog log : logs) {
			log.setFlag(flag);
			log.setTimeMS(timeMS);
			SysLogFactory.getInstance().addLog(log);
		}
	}

	/**
	 * 通过表及主键获取一条记录
	 * 
	 * @param model
	 * @return
	 */
	public Map<String, Object> getTableDataByPrimaryKey(TableModel model) {
		String tablename = model.getTableName().toLowerCase();
		if (DBUtils.getSecretTables().contains(tablename)) {
			throw new RuntimeException("access deny");
		}
		return this.getTableDataService().getTableDataByPrimaryKey(model);
	}

	public ITableDataService getTableDataService() {
		if (tableDataService == null) {
			tableDataService = ContextFactory.getBean("tableDataService");
		}
		return tableDataService;
	}

	/**
	 * 批量插入数据
	 * 
	 * @param rows
	 */
	public void insertAllTableData(List<TableModel> rows) {
		long start = System.currentTimeMillis();
		int timeMS = 0;
		int flag = 0;
		try {
			this.getTableDataService().insertAllTableData(rows);
			timeMS = (int) (System.currentTimeMillis() - start);
			flag = 1;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		for (TableModel model : rows) {
			String tablename = model.getTableName().toLowerCase();
			if (DBUtils.getUnWritableSecretTables().contains(tablename)) {
				throw new RuntimeException("access deny");
			}
			SysDataLog log = new SysDataLog();
			log.setActorId(model.getActorId());
			if (model.getIdColumn() != null && model.getIdColumn().getValue() != null) {
				log.setBusinessKey(model.getIdColumn().getValue().toString());
			}
			log.setCreateTime(new Date());
			log.setOperate("insert");
			log.setModuleId(model.getTableName());
			log.setContent(model.toJsonObject().toJSONString());
			log.setFlag(flag);
			log.setTimeMS(timeMS);
			SysLogFactory.getInstance().addLog(log);
		}
	}

	public void insertTableData(TableModel model) {
		String tablename = model.getTableName().toLowerCase();
		if (DBUtils.getUnWritableSecretTables().contains(tablename)) {
			throw new RuntimeException("access deny");
		}
		long start = System.currentTimeMillis();
		try {
			this.getTableDataService().insertTableData(model);
			SysDataLog log = new SysDataLog();
			log.setActorId(model.getActorId());
			if (model.getIdColumn() != null && model.getIdColumn().getValue() != null) {
				log.setBusinessKey(model.getIdColumn().getValue().toString());
			}
			log.setCreateTime(new Date());
			log.setOperate("insert");
			log.setModuleId(model.getTableName());
			log.setContent(model.toJsonObject().toJSONString());
			log.setTimeMS((int) (System.currentTimeMillis() - start));
			log.setFlag(1);
			SysLogFactory.getInstance().addLog(log);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 批量更新数据
	 * 
	 * @param rows
	 */
	public void updateAllTableData(List<TableModel> rows) {
		long start = System.currentTimeMillis();
		int timeMS = 0;
		int flag = 0;
		try {
			this.getTableDataService().updateAllTableData(rows);
			timeMS = (int) (System.currentTimeMillis() - start);
			flag = 1;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		for (TableModel model : rows) {
			String tablename = model.getTableName().toLowerCase();
			if (DBUtils.getUnWritableSecretTables().contains(tablename)) {
				throw new RuntimeException("access deny");
			}
			SysDataLog log = new SysDataLog();
			log.setActorId(model.getActorId());
			if (model.getIdColumn() != null && model.getIdColumn().getValue() != null) {
				log.setBusinessKey(model.getIdColumn().getValue().toString());
			}
			log.setCreateTime(new Date());
			log.setOperate("update");
			log.setModuleId(model.getTableName());
			log.setContent(model.toJsonObject().toJSONString());
			log.setFlag(flag);
			log.setTimeMS(timeMS);
			SysLogFactory.getInstance().addLog(log);
		}
	}

	public void updateTableData(TableModel model) {
		String tablename = model.getTableName().toLowerCase();
		if (DBUtils.getUnWritableSecretTables().contains(tablename)) {
			throw new RuntimeException("access deny");
		}
		long start = System.currentTimeMillis();
		try {
			this.getTableDataService().updateTableData(model);
			SysDataLog log = new SysDataLog();
			log.setActorId(model.getActorId());
			if (model.getIdColumn() != null && model.getIdColumn().getValue() != null) {
				log.setBusinessKey(model.getIdColumn().getValue().toString());
			}
			log.setCreateTime(new Date());
			log.setOperate("update");
			log.setModuleId(model.getTableName());
			log.setContent(model.toJsonObject().toJSONString());
			log.setTimeMS((int) (System.currentTimeMillis() - start));
			log.setFlag(1);
			SysLogFactory.getInstance().addLog(log);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public void updateTableDataByWhere(TableModel model) {
		String tablename = model.getTableName().toLowerCase();
		if (DBUtils.getUnWritableSecretTables().contains(tablename)) {
			throw new RuntimeException("access deny");
		}
		long start = System.currentTimeMillis();
		try {
			this.getTableDataService().updateTableDataByWhere(model);
			SysDataLog log = new SysDataLog();
			log.setActorId(model.getActorId());
			if (model.getIdColumn() != null && model.getIdColumn().getValue() != null) {
				log.setBusinessKey(model.getIdColumn().getValue().toString());
			}
			log.setCreateTime(new Date());
			log.setOperate("update");
			log.setModuleId(model.getTableName());
			log.setContent(model.toJsonObject().toJSONString());
			log.setTimeMS((int) (System.currentTimeMillis() - start));
			log.setFlag(1);
			SysLogFactory.getInstance().addLog(log);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

}
