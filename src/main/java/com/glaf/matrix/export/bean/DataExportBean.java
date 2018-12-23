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

package com.glaf.matrix.export.bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.domain.Database;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.jdbc.QueryHelper;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.JdbcUtils;
import com.glaf.core.util.LowerLinkedMap;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.QueryUtils;
import com.glaf.core.util.StringTools;
import com.glaf.core.util.UUID32;
import com.glaf.matrix.export.domain.DataExport;
import com.glaf.matrix.export.domain.DataExportItem;
import com.glaf.matrix.export.service.DataExportService;
import com.glaf.matrix.util.SysParams;

public class DataExportBean {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	public DataExport execute(String expId, Map<String, Object> params) {
		IDatabaseService databaseService = ContextFactory.getBean("databaseService");
		DataExportService dataExportService = ContextFactory
				.getBean("com.glaf.matrix.export.service.dataExportService");
		DataExport dataExport = dataExportService.getDataExport(expId);
		if (dataExport == null || !StringUtils.equals(dataExport.getActive(), "Y")) {
			return null;
		}
		logger.debug("exportId:" + dataExport.getId());
		logger.debug("subject:" + dataExport.getTitle());
		QueryHelper helper = new QueryHelper();

		Map<String, Object> parameter = new HashMap<String, Object>();
		if (params != null && !params.isEmpty()) {
			parameter.putAll(params);
		}

		List<DataExportItem> items = dataExport.getItems();
		SysParams.putInternalParams(parameter);

		int totalSize = 0;
		Object value = null;
		String idValue = null;
		Database srcDatabase = null;
		Connection srcConn = null;
		PreparedStatement srcPsmt = null;
		ResultSet srcRs = null;
		StringBuilder keyBuffer = new StringBuilder();
		List<Map<String, Object>> sourceResultList = null;

		try {

			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());

			try {
				srcDatabase = databaseService.getDatabaseById(dataExport.getSrcDatabaseId());
				srcConn = DBConnectionFactory.getConnection(srcDatabase.getName());
				logger.debug("srcConn:" + srcConn);

				for (DataExportItem item : items) {
					if (item.getLocked() == 1) {
						continue;
					}

					String sql = item.getSql();
					sql = QueryUtils.replaceSQLVars(sql, parameter);

					Map<String, Map<String, Object>> dataListMap = new HashMap<String, Map<String, Object>>();
					List<String> recursionKeys = StringTools.splitLowerCase(item.getRecursionColumns(), ",");
					boolean recursionKeyExists = false;
					if (recursionKeys != null && !recursionKeys.isEmpty()) {
						recursionKeyExists = true;
					}

					if (recursionKeyExists && StringUtils.isNotEmpty(item.getRecursionSql())) {
						if (!DBUtils.isLegalQuerySql(item.getRecursionSql())) {
							throw new RuntimeException(" SQL statement illegal ");
						}

						List<Map<String, Object>> recursionResultList = helper.getResultList(srcConn,
								item.getRecursionSql(), parameter, 5000);

						if (recursionResultList != null && !recursionResultList.isEmpty()) {
							LowerLinkedMap params2 = new LowerLinkedMap();
							final List<String> keys = StringTools.splitLowerCase(item.getPrimaryKey(), ",");

							int index = 0;
							logger.debug("recursionKeys:" + recursionKeys);
							for (Map<String, Object> paramMap : recursionResultList) {
								params2.clear();// 清空条件
								params2.putAll(parameter);// 放入外部输入条件
								params2.putAll(paramMap);// 放入循环输入条件
								// logger.debug(" paramMap:" + paramMap);
								index++;

								/**
								 * 通过新的参数获取每次循环的结果集
								 */
								sourceResultList = helper.getResultList(srcConn, sql, params2, SysParams.MAX_SIZE);

								if (sourceResultList != null && !sourceResultList.isEmpty()) {
									for (Map<String, Object> rowMap : sourceResultList) {
										Map<String, Object> dataMap2 = new LowerLinkedMap();
										dataMap2.putAll(parameter);
										dataMap2.putAll(paramMap);
										dataMap2.putAll(rowMap);

										keyBuffer.delete(0, keyBuffer.length());
										keyBuffer.append(srcDatabase.getId()).append("_");

										for (String key : keys) {
											value = ParamUtils.getObject(dataMap2, key);
											if (value == null) {
												value = ParamUtils.getObject(params2, key);
											}
											if (value != null) {
												if (value instanceof Date) {
													keyBuffer.append(formatter.format((Date) value)).append("_");
												} else {
													keyBuffer.append(value).append("_");
												}
											} else {
												keyBuffer.append("null").append("_");
											}
										}

										keyBuffer.delete(keyBuffer.length() - 1, keyBuffer.length());

										idValue = DigestUtils.md5Hex(keyBuffer.toString());
										// idValue = keyBuffer.toString();
										if (idValue != null) {
											Map<String, Object> dataMap = dataListMap.get(idValue);
											if (dataMap == null) {
												dataMap = new LowerLinkedMap();
												dataMap.put("ex_id_", idValue);
												dataMap.put("ex_complex_key_", keyBuffer.toString());
												dataMap.put("ex_databaseid_", srcDatabase.getId());
												dataMap.put("ex_discriminator_", srcDatabase.getDiscriminator());
												dataMap.put("ex_mapping_", srcDatabase.getMapping());
												if (recursionKeyExists) {
													for (String recursionKey : recursionKeys) {
														dataMap.put(recursionKey,
																ParamUtils.getObject(paramMap, recursionKey));
														if (index == 1) {

														}
													}
												}
											}

											dataMap.putAll(dataMap2);
											dataMap.put("ex_syncid_", dataExport.getId());
											dataListMap.put(idValue, dataMap);
										}
									}
									totalSize = dataListMap.size();
									if (totalSize > SysParams.MAX_SIZE) {
										logger.warn("totalSize:" + totalSize);
										throw new RuntimeException(
												"data list too large, please split small condition.");
									}
								}
							}
						}
					} else {
						sourceResultList = helper.getResultList(srcConn, sql, parameter, SysParams.MAX_SIZE);
						if (sourceResultList != null && !sourceResultList.isEmpty()) {
							List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
							for (Map<String, Object> rowMap : sourceResultList) {
								LowerLinkedMap dataMap2 = new LowerLinkedMap();
								dataMap2.putAll(parameter);
								dataMap2.putAll(rowMap);
								dataList.add(dataMap2);
							}
							sourceResultList.clear();

							final List<String> keys = StringTools.splitLowerCase(item.getPrimaryKey(), ",");
							if (keys != null && !keys.isEmpty()) {
								for (Map<String, Object> rowMap : dataList) {
									keyBuffer.delete(0, keyBuffer.length());
									keyBuffer.append(srcDatabase.getId()).append("_");
									for (String key : keys) {
										value = rowMap.get(key);
										if (value == null) {
											value = ParamUtils.getObject(parameter, key);
										}
										if (value != null) {
											if (value instanceof Date) {
												keyBuffer.append(formatter.format((Date) value)).append("_");
											} else {
												keyBuffer.append(value).append("_");
											}
										} else {
											keyBuffer.append("null").append("_");
										}
									}

									if (keyBuffer.length() > 0) {
										keyBuffer.delete(keyBuffer.length() - 1, keyBuffer.length());
									}

									idValue = DigestUtils.md5Hex(keyBuffer.toString());
									// idValue = keyBuffer.toString();
									if (idValue != null) {
										Map<String, Object> dataMap = dataListMap.get(idValue);
										if (dataMap == null) {
											dataMap = new LowerLinkedMap();
											dataMap.put("ex_id_", idValue);
											dataMap.put("ex_complex_key_", keyBuffer.toString());
											dataMap.put("ex_databaseid_", srcDatabase.getId());
											dataMap.put("ex_discriminator_", srcDatabase.getDiscriminator());
											dataMap.put("ex_mapping_", srcDatabase.getMapping());
										}
										dataMap.putAll(rowMap);
										dataMap.put("ex_syncid_", dataExport.getId());
										dataListMap.put(idValue, dataMap);
									}
								}
							} else {
								for (Map<String, Object> rowMap : dataList) {
									String uuid = UUID32.getUUID();
									rowMap.put("ex_id_", uuid);
									rowMap.put("ex_syncid_", dataExport.getId());
									rowMap.put("ex_databaseid_", srcDatabase.getId());
									rowMap.put("ex_discriminator_", srcDatabase.getDiscriminator());
									rowMap.put("ex_mapping_", srcDatabase.getMapping());
									dataListMap.put(uuid, rowMap);
								}
							}
						}
					}
					if (!dataListMap.isEmpty()) {
						totalSize = dataListMap.size();
						if (totalSize > SysParams.MAX_SIZE) {
							logger.warn(item.getTitle() + " total size:" + totalSize);
							throw new RuntimeException("data list too large, please split small condition.");
						} else {
							logger.debug(item.getTitle() + " rows size:" + totalSize);
						}
					}
					item.setDataList(dataListMap.values());
				}
			} finally {
				JdbcUtils.close(srcConn);
			}

			return dataExport;
		} catch (Exception ex) {
			// ex.printStackTrace();
			logger.error("execute sql error", ex);
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(srcRs);
			JdbcUtils.close(srcPsmt);
			JdbcUtils.close(srcConn);
		}
	}

}
